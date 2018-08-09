define(["dojo/_base/declare", 
        "dojo/_base/lang", 
//        "dojo/aspect", 
//        "dojo/dom-class", "dojo/dom-style",
//        "dojo/dom-construct", "dojo/store/Memory",
//        "dojo/cookie", "dijit/registry", "dijit/layout/BorderContainer",
//        "dijit/layout/StackContainer", "ecm/Messages",
        "ecm/LoggerMixin",
//        "ecm/model/Desktop",
//        "ecm/widget/dialog/BaseDialog",
//        "ecm/widget/dialog/ConfirmationDialog",
//        "ecm/widget/Breadcrumb",
//        "ecm/widget/Ellipsis", 
//        "ecm/widget/ItemEditPane",
//        "ecm/widget/ItemSocialContent",
        "ecm/widget/dialog/EditPropertiesDialog",
        "dojo/text!./templates/GILEditPropertiesDialog.html"],
        function(declare, lang,
//        		aspect, domClass, domStyle, domConstruct, Memory, 
//        		cookie, registry, BorderContainer, StackContainer, Messages, 
        		LoggerMixin,
//        		Desktop,
        		BaseDialog,
//        		ConfirmationDialog, BreadCrumb, Ellipsis, ItemEditPane,
//        		ItemSocialContent,
        		EditPropertiesDialog, template){
		return declare("gilCnPluginDojo.GIlEditPropertiesDialog",[BaseDialog],{
//			contentString:template, _versionSeperator:".", 
			postCreate:function () {
	            this.inherited(arguments);
//	            domClass.add(this.domNode, "ecmEditPropertiesDialog");
//	            this.setMaximized(true);
//	            this.setSizeToViewportRatio(true);
//	            this.autofocus = false;
//	            this.set("title", ecm.messages.edit_properties_dialog_title);
//	            this.setIntroText(ecm.messages.edit_properties_dialog_info);
//	            this._saveButton = this.addButton(this.messages.save_template, "onSave", false, true, "SAVE");
//	            this._addActionsButton();
//	            this._previousButton = this.addButton(this.messages.back_label, "onPrevious", false, false, "PREVIOUS");
//	            domClass.add(this._previousButton.domNode, "previousButton");
//	            domStyle.set(this._previousButton.domNode, "display", "none");
//	            this.own(aspect.after(this._itemEditPane, "onCompleteRendering", lang.hitch(this, "_enableDisableSaveButton"), true));
//	            this.own(aspect.after(this._itemEditPane, "onChangeProperties", lang.hitch(this, "_enableDisableSaveButton"), true));
//	            this.own(aspect.after(this._itemEditPane, "onSecurityChange", lang.hitch(this, "_enableDisableSaveButton"), true));
//	            this.own(aspect.after(this._itemEditPane, "onClassChange", lang.hitch(this, "_enableDisableSaveButton"), true));
//	            this.own(aspect.after(this._itemEditPane, "onAnyOtherChange", lang.hitch(this, "_enableDisableSaveButton"), true));
//	            this.own(aspect.after(this._itemEditPane, "onClickReferenceAttribute", lang.hitch(this, "_onClickReferenceAttribute"), true));
//	            this.own(aspect.after(this._itemEditPane, "onRetrySave", lang.hitch(this, "onSave"), true));
//	            this.own(aspect.after(this._breadcrumb, "onPop", lang.hitch(this, "_onPopBreadcrumb"), true));
//	            this.own(aspect.after(this._breadcrumb, "onClick", lang.hitch(this, "_onClickBreadcrumb"), true));
	        }
		});

});