/**
 * @author mdevino
 */
define([
    'dojo/_base/declare',
	'dojo/_base/lang',
	'ecm/model/Action',
	'ecm/model/Request',
	'ecm/model/Desktop',
	'ecm/widget/dialog/BaseDialog',
	'gilCnPluginDojo/GILDialog',
	'ecm/widget/search/SearchBuilder',
	'dojo/io-query',
	'gilCnPluginDojo/NonModalConfirmDialog',
	'dojo/dom-class',
	'dojo/text!./templates/AddToFolderDialog.html',
	'dijit/registry',
	'dojo/dom-construct',
	'dijit/form/Button',
	'dojo/aspect'
	],	
function(declare, lang, Action, Request, Desktop, BaseDialog, GILDialog,
		SearchBuilder, ioquery, NonModalConfirmDialog, domClass, template, 
		registry, domConstruct, Button, aspect){


	return declare('gilCnPluginDojo.AddToFolderDialog', [ BaseDialog, GILDialog ], {

		contentString: template,
		widgetsInTemplate: true,
		serviceParameters: null,
		helpDialog: null,
		    
		constructor: function(args){	
			this.serviceParameters = args;
			
		},
	
		postCreate: function() {
			this.inherited(arguments);
			this.setMaximized(false);
			dojo.style(this.cancelButton.domNode,'display','none');
			this.setTitle('Add to Folder');
			this.addButton('Close', 'cancel', false, true);
			this.addButton('Add', 'onAddButtonClick', false, true);
			this.showActionBar(true);
			this.setResizable(true);
			this.setMaximized(true);
			//aspect.after(this.searchTab.searchDefinition.contentClassSelector, 'onLoaded', this.filterContentClasses);
			this.startSearchTab();
			
    	},
		
		cancel: function() {
			this.searchTab.destroyRecursive();
			this.destroyRecursive();
			
		},
		
		onAddButtonClick: function(){
			var items = this.searchTab.searchResults.getSelectedItems();
			if(items.length == 0){
				this.successDialog = new NonModalConfirmDialog({
	            	id: this.id + '_no_folders_dialog',
	            	title: 'No folders selected', 
	                style: "width:300px;height:150px;",
	                content: "Select at least one folder to add the document.",
	            });
				var actionBar = dojo.create("div", { "class": "dijitDialogPaneActionBar"  }, this.successDialog.containerNode);
				var buttonOk = new dijit.form.Button({"label": "OK",
		            onClick: () => {
		            	this.successDialog.destroyRecursive();
		            	
		            } });
		        buttonOk.placeAt(actionBar);
		        domClass.add(buttonOk.focusNode, "yesNoOkButton");
				this.successDialog.show();
		        
			} else{
				this.addToFolder(items.map((item) => item.itemId));
			}
			
		},
		
		startSearchTab: function() {
			
			this.searchTab.setRepository(this.serviceParameters.repository, this.serviceParameters.teamspace);
			//this.filterContentClasses();
			lang.mixin(this.searchTab.searchResults, {onRowDblClick: lang.hitch(this, this.addToFolder)});
			
//			var rcm = this.searchTab.searchResults.getGridModule('rowContextMenu');
//			lang.mixin(rcm, {preload: lang.hitch(rcm, this.preload)});
			
		},
		
		filterContentClasses: function() {
   			this.repository.retrieveContentClasses(classes => {
   				var notFolders = classes.filter(c => {
   					return	!c.name.toLowerCase().includes("folder");
   				});
   				this.setExcludedItems(notFolders.map(c => {return c.id;}));
   				
   			});
		},
		
		addToFolder: function(items, event){
			if(!Array.isArray(items))
				items = [items.itemId];
			this.serviceParameters.folderIds = JSON.stringify(items);
			this.serviceParameters.frontEndAction = 'addToFolder';
			Request.postPluginService("GilCnPlugin", "FolderService", 'application/x-www-form-urlencoded',{
				requestBody : ioquery.objectToQuery(this.serviceParameters),
	    		requestCompleteCallback : lang.hitch(this, function (response) {
	    			if(!response.error){
						this.successDialog = new NonModalConfirmDialog({
			            	id: this.id + '_no_folders_dialog',
			            	title: 'Items added to folder', 
			                style: "width:300px;height:150px;",
			                content: "The selected items were succesfully added to the folder.",
			            });
						this.successDialog.show();
						var actionBar = dojo.create("div", { "class": "dijitDialogPaneActionBar"  }, this.successDialog.containerNode);
				        var buttonOk = new dijit.form.Button({"label": "OK",
				            onClick: () => {
				            	this.successDialog.destroyRecursive();
				            	
				            } });
				        buttonOk.placeAt(actionBar);
				        domClass.add(buttonOk.focusNode, "yesNoOkButton");
	    			} else {
						this.successDialog = new NonModalConfirmDialog({
			            	id: this.id + '_no_folders_dialog',
			            	title: 'Failed to add item to folder', 
			                style: "width:300px;height:180px;",
			                content: response.error,
			            });
						this.successDialog.show();
						var actionBar = dojo.create("div", { "class": "dijitDialogPaneActionBar"  }, this.successDialog.containerNode);
				        var buttonOk = new dijit.form.Button({"label": "OK",
				            onClick: () => {
				            	this.successDialog.destroyRecursive();
				            	
				            } });
				        buttonOk.placeAt(actionBar);
				        domClass.add(buttonOk.focusNode, "yesNoOkButton");
	    				
	    			}
	    		})
			});
			this.cancel();
			
		},
		
		// Overridden to remove closing dialog on pressing enter.
		addButton:function (buttonLabel, clickFunction, disabled, isDefault, idPrefix) {
            this.cancelButton.set("label", this.cancelButtonLabel);
            var params = {label:buttonLabel};
            if (idPrefix) {
                params.id = registry.getUniqueId(idPrefix + "_dijit_form_Button");
            }
            var button = new Button(params);
            if (clickFunction) {
                this.connect(button, "onClick", clickFunction);
            }
            domConstruct.place(button.domNode, this.actionBar.firstChild, "before");
            domConstruct.place(this.messageRef, this.actionBar.firstChild, "before");
            if (disabled) {
                button.set("disabled", true);
            }
            if (!this._addedButtons) {
                this._addedButtons = [];
            }
            this._addedButtons.push(button);
            return button;
        },
        
        preload:function () {
            var t = this, g = t.grid;
            t.batchConnect(/*[g, "onRowContextMenu", "_showContextMenu"],*/ [g.domNode, "onkeydown", "_onKeyDown"], /*[g, "onRowDblClick", "performDefaultAction"],*/ [g.bodyNode, "oncontextmenu", "_checkForContextMenuInEmptySpace"], [ecm.model.desktop, "onDisplayStatusDialog", "_onDisplayStatusDialog"], [ecm.model.desktop, "onHideStatusDialog", "_onHideStatusDialog"], [g.select.row, "onSelectionChange", "_onRowSelectionChange"]);
            //t.inherited(arguments);
        }
		

		
	});
});