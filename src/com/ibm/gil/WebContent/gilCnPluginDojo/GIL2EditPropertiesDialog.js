define(["dojo/_base/declare", 
        "dojo/_base/lang", 
        "dojo/aspect", 
        "dojo/dom-class", "dojo/dom-style",
        "dojo/dom-construct", "dojo/store/Memory",
        "dojo/cookie", "dijit/registry", "dijit/layout/BorderContainer",
        "dijit/layout/StackContainer", "ecm/Messages",
        "ecm/LoggerMixin",
        "ecm/model/Desktop",
        "ecm/widget/dialog/BaseDialog",
        "ecm/widget/dialog/ConfirmationDialog",
        "ecm/widget/Breadcrumb",
        "ecm/widget/Ellipsis", 
        "ecm/widget/ItemEditPane",
        "ecm/widget/ItemSocialContent",
//        "ecm/widget/dialog/EditPropertiesDialog",
        "dojo/text!./templates/GILEditPropertiesDialog.html"],

		function (declare, lang,
        		aspect, domClass, domStyle, domConstruct, Memory, 
        		cookie, registry, BorderContainer, StackContainer, Messages, 
        		LoggerMixin,
        		Desktop,
        		BaseDialog,
        		ConfirmationDialog, BreadCrumb, Ellipsis, ItemEditPane,
        		ItemSocialContent,
//        		EditPropertiesDialog, 
        		template
				) {
        return declare("ecm.widget.dialog.EditPropertiesDialog", [BaseDialog, LoggerMixin], {contentString:template, _versionSeperator:".", postCreate:function () {
            this.inherited(arguments);
            domClass.add(this.domNode, "ecmEditPropertiesDialog");
            this.setMaximized(true);
            this.setSizeToViewportRatio(true);
            this.autofocus = false;
            this.set("title", ecm.messages.edit_properties_dialog_title);
            this.setIntroText(ecm.messages.edit_properties_dialog_info);
            this._saveButton = this.addButton(this.messages.save_template, "onSave", false, true, "SAVE");
            this._addActionsButton();
            this._previousButton = this.addButton(this.messages.back_label, "onPrevious", false, false, "PREVIOUS");
            domClass.add(this._previousButton.domNode, "previousButton");
            domStyle.set(this._previousButton.domNode, "display", "none");
            this.own(aspect.after(this._itemEditPane, "onCompleteRendering", lang.hitch(this, "_enableDisableSaveButton"), true));
            this.own(aspect.after(this._itemEditPane, "onChangeProperties", lang.hitch(this, "_enableDisableSaveButton"), true));
            this.own(aspect.after(this._itemEditPane, "onSecurityChange", lang.hitch(this, "_enableDisableSaveButton"), true));
            this.own(aspect.after(this._itemEditPane, "onClassChange", lang.hitch(this, "_enableDisableSaveButton"), true));
            this.own(aspect.after(this._itemEditPane, "onAnyOtherChange", lang.hitch(this, "_enableDisableSaveButton"), true));
            this.own(aspect.after(this._itemEditPane, "onClickReferenceAttribute", lang.hitch(this, "_onClickReferenceAttribute"), true));
            this.own(aspect.after(this._itemEditPane, "onRetrySave", lang.hitch(this, "onSave"), true));
            this.own(aspect.after(this._breadcrumb, "onPop", lang.hitch(this, "_onPopBreadcrumb"), true));
            this.own(aspect.after(this._breadcrumb, "onClick", lang.hitch(this, "_onClickBreadcrumb"), true));
        }, show:function (item, callback, teamspace, entryTemplate) {
            this._item = item;
            this._getVersionSeperatorBasedOnLocales();
            if (this._item) {
                if (item.repository && item.repository.type == "od") {
                    this.setIntroText(ecm.messages.edit_properties_od_dialog_info);
                } else {
                    this.setIntroText(ecm.messages.edit_properties_dialog_info);
                }
                this._callback = callback;
                this._entryTemplate = entryTemplate;
                this._useTemplate = this._entryTemplate ? true : false;
                this._itemEditPane._editPropertiesDialog = this;
                this._itemEditPane._topContainer = this._topContainer;
                this._itemEditPane.setItem(item, lang.hitch(this, function () {
                    if ((this._item.repository.type == "p8" || this._item.repository.type == "cm" || this._item.repository.type == "cmis") && !this._item.isFolder()) {
                        domStyle.set(this._versionSelectorPane.domNode, "display", "");
                        this._itemEditPane._versionSelectorPane = this._versionSelectorPane;
                        this._itemEditPane._versionSelector = this._versionSelector;
                        this._versionSelector.set("readonly", true);
                        this._item.retrieveAllVersions(lang.hitch(this, function (resultSet) {
                            this._versionResultSet = resultSet;
                            this._setVersionSelector(this._versionResultSet, item);
                        }));
                        this.own(aspect.after(this._versionSelector, "onChange", lang.hitch(this, "_changeVersion"), true));
                    } else {
                        domStyle.set(this._versionSelectorPane.domNode, "display", "none");
                    }
                    if (this._canViewItem(this._item)) {
                        domClass.remove(this._viewContentButton.domNode, "dijitHidden");
                    } else {
                        domClass.add(this._viewContentButton.domNode, "dijitHidden");
                    }
                    this.inherited("show", []);
                    domStyle.set(this._breadcrumbContainer.domNode, "display", "none");
                    this.resize();
                }), teamspace, entryTemplate);
                if (item.repository.type != "box" && item.isAvailableForSocialCollaboration(null, true)) {
                    if (!this._itemSocialContent) {
                        this._itemSocialContent = new ItemSocialContent();
                        domConstruct.place(this._itemSocialContent.domNode, this._previousButton.domNode, "before");
                    }
                    this._itemSocialContent.set("item", item);
                } else {
                    if (this._itemSocialContent) {
                        domConstruct.destroy(this._itemSocialContent.domNode);
                        this._itemSocialContent.destroy();
                        this._itemSocialContent = null;
                    }
                }
            }
        }, _getVersionSeperatorBasedOnLocales:function () {
            this._versionSeperator = ".";
            return this._versionSeperator;
        }, _setVersionSelector:function (resultSet, item) {
            if (item.repository && item.repository.type == "p8") {
                var versionStore = new Memory();
                versionStore.data = [];
                for (var i = 0; i < resultSet.items.length; i++) {
                    var resultItem = resultSet.items[i];
                    versionStore.data.push({name:resultItem.attributes.MajorVersionNumber + this._versionSeperator + resultItem.attributes.MinorVersionNumber, id:resultItem.id, item:resultItem});
                }
                this._versionSelector.set("store", versionStore);
                this._versionSelector.set("value", item.attributes.MajorVersionNumber + this._versionSeperator + item.attributes.MinorVersionNumber);
                this._versionSelector.set("searchAttr", "name");
            } else {
                if (item.repository && item.repository.type == "cm") {
                    var versionStore = new Memory();
                    versionStore.data = [];
                    for (var i = 0; i < resultSet.items.length; i++) {
                        var resultItem = resultSet.items[i];
                        versionStore.data.push({name:resultItem.attributes.icn_sys_version, id:resultItem.id, item:resultItem});
                        if (!item.attributes.icn_sys_version && item.id == resultItem.id) {
                            item.attributes.icn_sys_version = resultItem.attributes.icn_sys_version;
                        }
                    }
                    this._versionSelector.set("store", versionStore);
                    this._versionSelector.set("value", item.attributes.icn_sys_version);
                    this._versionSelector.set("searchAttr", "name");
                } else {
                    if (item.repository && item.repository.type == "cmis") {
                        var versionStore = new Memory();
                        versionStore.data = [];
                        for (var i = 0; i < resultSet.items.length; i++) {
                            var resultItem = resultSet.items[i];
                            versionStore.data.push({name:resultItem.attributes["cmis:versionLabel"], id:resultItem.id, item:resultItem});
                        }
                        this._versionSelector.set("store", versionStore);
                        this._versionSelector.set("value", item.attributes["cmis:versionLabel"]);
                        this._versionSelector.set("searchAttr", "name");
                    }
                }
            }
        }, _changeVersion:function () {
            var value = this._versionSelector.get("value");
            var selectedItem;
            for (var i in this._versionSelector.store.data) {
                if (this._versionSelector.store.data[i].name == value) {
                    selectedItem = this._versionSelector.store.data[i].item;
                    break;
                }
            }
            if (selectedItem) {
                var currentVersion;
                if (selectedItem.repository && selectedItem.repository.type == "p8") {
                    currentVersion = this._item.attributes.MajorVersionNumber + this._versionSeperator + this._item.attributes.MinorVersionNumber;
                } else {
                    if (selectedItem.repository && selectedItem.repository.type == "cm") {
                        currentVersion = this._item.attributes.icn_sys_version;
                        this._itemEditPane._needUnlock = this._itemEditPane._needUnlock || (!this._itemEditPane._readonlyWithNotelog() && this._itemEditPane._item && this._itemEditPane._item.locked && this._itemEditPane._itemLockedForEdit && !this._itemEditPane._item.deleted);
                        if (selectedItem.attributes.icn_sys_version <= currentVersion && !this._itemEditPane._cmCurrentVersionItem) {
                            this._itemEditPane._cmCurrentVersionItem = this._item;
                        }
                    } else {
                        if (selectedItem.repository && selectedItem.repository.type == "cmis") {
                            currentVersion = this._item.attributes["cmis:versionLabel"];
                        }
                    }
                }
                if (currentVersion && currentVersion != value) {
                    if (this._itemEditPane._itemNotelogsPane && this._itemEditPane._itemNotelogsPane._notelogAddingTextarea) {
                        this._itemEditPane._itemNotelogsPane._notelogAddingTextarea.getParent().removeChild(0);
                    }
                    ecm.model.desktop.getActionsHandler().actionEdit(this._item.repository, [selectedItem]);
                }
            }
        }, showProperties:function (item, callback, teamspace) {
            var methodName = "showProperties";
            var entryTemplateId = item.entryTemplateId;
            if (entryTemplateId && (entryTemplateId.length > 0)) {
                var entryTemplate = item.repository.getEntryTemplateById(entryTemplateId);
                var propertiesEntryTemplateRetrievedHandler = lang.hitch(this, function (entryTemplate) {
                    if (!entryTemplate.isRetrieved) {
                        if (!ecm.model.desktop.hideEntryTemplateNotFoundWarning) {
                            this.setMessage(ecm.messages.property_entry_template_not_retrieved_warning, "warning");
                        }
                        this.logWarning(methodName, ecm.messages.property_entry_template_not_retrieved_warning);
                        this.show(item, callback, teamspace, null);
                    } else {
                        if (entryTemplate.useForProperties) {
                            this.show(item, callback, teamspace, entryTemplate);
                        } else {
                            this.show(item, callback, teamspace, null);
                        }
                    }
                });
                if (!entryTemplate.isRetrieved) {
                    entryTemplate.retrieveEntryTemplate(propertiesEntryTemplateRetrievedHandler, false, true);
                } else {
                    propertiesEntryTemplateRetrievedHandler(entryTemplate);
                }
            } else {
                this.show(item, callback, teamspace, null);
            }
        }, _canViewItem:function (item) {
            return item.hasContent() && item.hasPrivilege("privViewDoc") && item.isViewable();
        }, _onView:function (evt) {
            var actionsHandler = ecm.model.desktop.getActionsHandler();
            if (actionsHandler) {
                var item = this._getSelectedItemEditPane().getItem();
                actionsHandler.actionView(null, [item]);
            }
        }, _addActionsButton:function () {
            this._viewContentButton = this.addButton(this.messages.open, "_onView", false, false, "VIEW");
        }, _onClickReferenceAttribute:function (evt) {
            var referenceItemId = registry.getEnclosingWidget(evt.target).get("value");
            if (referenceItemId) {
                var loadedPane = null;
                var children = this._stackContainer.getChildren();
                for (var i in children) {
                    var pane = children[i];
                    if (pane.getItem().id == referenceItemId) {
                        loadedPane = pane;
                        break;
                    }
                }
                if (loadedPane) {
                    this._viewContentButton.set("disabled", !this._canViewItem(loadedPane.getItem()));
                    this._addToBreadcrumb(loadedPane.getItem(), loadedPane);
                    this._stackContainer.selectChild(loadedPane);
                    this.resize();
                } else {
                    this._loadReferenceAttributePane(referenceItemId);
                }
            }
        }, _loadReferenceAttributePane:function (referenceItemId) {
            if (referenceItemId) {
                this._item.repository.retrieveItem(referenceItemId, lang.hitch(this, function (referenceItem) {
                    var referenceEditPane = ecm.widget.ItemEditPane();
                    this.own(aspect.after(referenceEditPane, "onCompleteRendering", lang.hitch(this, "_enableDisableSaveButton"), true));
                    this.own(aspect.after(referenceEditPane, "onChangeProperties", lang.hitch(this, "_enableDisableSaveButton"), true));
                    this.own(aspect.after(referenceEditPane, "onClickReferenceAttribute", lang.hitch(this, "_onClickReferenceAttribute"), true));
                    referenceEditPane.setItem(referenceItem, lang.hitch(this, function () {
                        this._viewContentButton.set("disabled", !this._canViewItem(referenceItem));
                        this._addToBreadcrumb(referenceItem, referenceEditPane);
                        this._stackContainer.addChild(referenceEditPane);
                        this._stackContainer.selectChild(referenceEditPane);
                        referenceEditPane.getContentClassSelector().set("disabled", true);
                        this.resize();
                    }));
                }));
            }
        }, _addToBreadcrumb:function (referenceItem, referenceEditPane) {
            var array = this._breadcrumb.getData() ? this._breadcrumb.getData() : [];
            if (array.length == 0) {
                array.push({label:this._item.getContentClass().name, item:this._item, pane:this._itemEditPane});
            }
            array.push({label:referenceItem.getContentClass().name, item:referenceItem, pane:referenceEditPane});
            this._breadcrumb.setData(array);
            domStyle.set(this._breadcrumbContainer.domNode, "display", "");
            domStyle.set(this._previousButton.domNode, "display", "");
            this._previousButton.set("disabled", false);
        }, _onPopBreadcrumb:function (breadcrumb) {
        }, _onClickBreadcrumb:function () {
            this._breadcrumbContainer.layout();
            var lastBreadcrumbData = this._breadcrumb.getData()[this._breadcrumb.getData().length - 1];
            var pane = lastBreadcrumbData.pane;
            this._stackContainer.selectChild(pane);
            this._enableDisableSaveButton();
            this._viewContentButton.set("disabled", !this._canViewItem(pane.getItem()));
            if (this._breadcrumb.getData().length == 1) {
                this._previousButton.set("disabled", true);
            }
        }, onPrevious:function () {
            this._breadcrumb.pop();
            this._onClickBreadcrumb();
        }, _enableDisableSaveButton:function () {
            var selectedPane = this._getSelectedItemEditPane();
            if (selectedPane) {
                var otherChange = lang.isFunction(selectedPane.isAnyOtherChanged) && selectedPane.isAnyOtherChanged();
                if (selectedPane.getFieldsChanged() == true || selectedPane.isSecurityChanged() || selectedPane.isClassChanged() || otherChange) {
                    if (otherChange) {
                        this._saveButton.set("disabled", false);
                    } else {
                        this._saveButton.set("disabled", selectedPane.isReadOnly() && !selectedPane.canModifySecurity() ? true : !((selectedPane == this._itemEditPane) ? true : selectedPane.isValid(false, true, false)));
                    }
                } else {
                    this._saveButton.set("disabled", true);
                }
                var disabled = selectedPane.isReadOnly() ? false : !selectedPane.isValid(false, true, false);
                this._getSelectedItemEditPane().setDisabledNavigation(disabled);
                this._breadcrumb.set("disabled", disabled);
                this._previousButton.set("disabled", this._breadcrumb.getData().length > 1 ? disabled : true);
            }
        }, _getSelectedItemEditPane:function () {
            return this._stackContainer.selectedChildWidget;
        }, onSave:function () {
            if (this._breadcrumb.getData().length == 0) {
                if (this._itemEditPane.isValid(true, false, true)) {
                    this._itemEditPane.save(lang.hitch(this, function () {
                        this.hide();
                        if (this._callback) {
                            this._callback();
                        }
                    }));
                }
            } else {
                this._saveDirtyNonBreadcrumbPanes();
            }
        }, _saveDirtyNonBreadcrumbPanes:function () {
            var pane = this._getNonBreadcrumbPane();
            if (pane) {
                if (pane.isDirty()) {
                    pane.save(lang.hitch(this, function () {
                        this._stackContainer.removeChild(pane);
                        pane.destroy();
                        this._saveDirtyNonBreadcrumbPanes();
                    }));
                } else {
                    pane.onCancel();
                    this._stackContainer.removeChild(pane);
                    pane.destroy();
                    this._saveDirtyNonBreadcrumbPanes();
                }
            } else {
                this._saveBreadcrumbPanes();
            }
        }, _getNonBreadcrumbPane:function () {
            var children = this._stackContainer.getChildren();
            for (i in children) {
                var pane = children[i];
                if (!this._isInBreadcrumb(pane.getItem().id)) {
                    return pane;
                }
            }
            return null;
        }, _saveBreadcrumbPanes:function () {
            if (this._breadcrumb.getData().length == 0) {
                this.hide();
                if (this._callback) {
                    this._callback();
                }
            } else {
                var lastBreadcrumbData = this._breadcrumb.getData()[this._breadcrumb.getData().length - 1];
                var pane = lastBreadcrumbData.pane;
                this._stackContainer.selectChild(pane);
                if (!pane.isReadOnly()) {
                    pane.save(lang.hitch(this, function () {
                        this._breadcrumb.pop();
                        this._stackContainer.removeChild(pane);
                        if (this._itemEditPane != pane) {
                            pane.destroy();
                        }
                        this._saveBreadcrumbPanes();
                    }));
                } else {
                    this._breadcrumb.pop();
                    this._stackContainer.removeChild(pane);
                    if (this._itemEditPane != pane) {
                        pane.destroy();
                    }
                    this._saveBreadcrumbPanes();
                }
            }
        }, onCancel:function () {
            var breadcrumbData = this._breadcrumb.getData();
            if (breadcrumbData && breadcrumbData.length > 0) {
                var children = this._stackContainer.getChildren();
                for (i in children) {
                    var pane = children[i];
                    if (!this._isInBreadcrumb(pane.getItem().id)) {
                        pane.onCancel();
                        this._stackContainer.removeChild(pane);
                        pane.destroy();
                    }
                }
                while (this._breadcrumb.getData().length > 0) {
                    var pane = this._breadcrumb.getData()[this._breadcrumb.getData().length - 1].pane;
                    pane.onCancel();
                    this._stackContainer.removeChild(pane);
                    if (pane != this._itemEditPane) {
                        pane.destroy();
                    }
                    this._breadcrumb.pop();
                }
            } else {
                if (this._itemEditPane.isAnyOtherChanged()) {
                    this._getCancelConfirmDialog().show();
                    return;
                } else {
                    this._itemEditPane.onCancel();
                }
            }
            this._itemEditPane.destroy();
            this.inherited(arguments);
        }, _getCancelConfirmDialog:function () {
            if (this._cancelConfirmDialog) {
                return this._cancelConfirmDialog;
            }
            var self = this;
            var confirmDialog = new ConfirmationDialog({text:this._itemEditPane._getCancelConfirmationMessage(), title:this.messages.close, buttonLabel:this.messages.yes, cancelButtonLabel:this.messages.no, cancelButtonDefault:true, onExecute:lang.hitch(this, function () {
                confirmDialog.hide();
                self._itemEditPane._cancelOtherChanges();
                self.onCancel();
            })});
            confirmDialog.show();
            this._cancelConfirmDialog = confirmDialog;
            return this._cancelConfirmDialog;
        }, hide:function () {
            if (this._cancelConfirmDialog && this._cancelConfirmDialog.open) {
                return;
            }
            this.inherited(arguments);
        }, destroy:function () {
            if (this._cancelConfirmDialog) {
                this._cancelConfirmDialog.destroyRecursive();
                delete this._cancelConfirmDialog;
            }
            if (this._itemEditPane) {
                this._itemEditPane.destroy();
                delete this._itemEditPane;
            }
            if (this._itemSocialContent) {
                this._itemSocialContent.destroy();
                delete this._itemSocialContent;
            }
            this.inherited(arguments);
        }, _isInBreadcrumb:function (itemId) {
            var array = this._breadcrumb.getData();
            for (var i in array) {
                var data = array[i];
                if (data.item.id == itemId) {
                    return true;
                }
            }
            return false;
        }});
    });