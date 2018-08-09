/**
 * @author mdevino
 */
define([
    'dojo/_base/declare',
	'dojo/_base/lang',
	'ecm/model/Request',
	'ecm/widget/dialog/BaseDialog',
	'gilCnPluginDojo/GILDialog',
	'dojo/store/Memory',
	'gridx/core/model/cache/Sync',
	'gridx/Grid',
	'dojo/dom-style',
	'dojo/dom-class',
	'dojo/io-query',
	'gilCnPluginDojo/NonModalConfirmDialog',
	'dojox/grid/DataGrid',
	'dojo/data/ItemFileWriteStore',
	'dojo/on',
	'dojo/_base/event',
	'gilCnPluginDojo/NonModalConfirmDialog',
	'dojo/text!./templates/RemoveFromFolderDialog.html'
	],	
function(declare, lang, Request, BaseDialog, GILDialog, Memory, Synch, Grid, domStyle, domClass, 
		ioquery, NonModalConfirmDialog, DataGrid, ItemFileWriteStore, on, event, NonModalConfirmDialog, template){


	return declare('gilCnPluginDojo.RemoveFromFolderDialog', [ BaseDialog, GILDialog ], {

		contentString: template,
		widgetsInTemplate: true,
		    
		constructor: function(args){	
			this.serviceParameters = args;
			this.grid = null;
			this.noFolderDialog = null;
			
		},
	
		postCreate: function() {
			this.inherited(arguments);
			this.setMaximized(false);
			this.setTitle('Remove from Folder');
			this.addButton('Remove', 'removeFromFolder', false, true);
			this.showActionBar(true);
			this.setSize(600, 300);
			this.setResizable(true);
			this.setMaximized(false);
			this.retrieveFolders();
			
    	},
		
		cancel: function() {
			this.destroyRecursive();
			
		},
		
		retrieveFolders: function() {
			this.serviceParameters.frontEndAction = 'retrieveContainingFolders';
			Request.postPluginService('GilCnPlugin', 'FolderService','application/x-www-form-urlencoded', {
				requestBody : ioquery.objectToQuery(this.serviceParameters),
				requestCompleteCallback : lang.hitch(this, function (response) {
					if(response.folders.length != 0){
						this.initGrid(response.folders);
						this.show();
					} else {
						this.resultsDialog = new NonModalConfirmDialog({
			            	id: this.id + '_no_folders_dialog',
			            	title: 'No folders found', 
			                style: "width:300px;height:150px;",
			                content: "No folders containing all items selected were found.",
			            });
						this.resultsDialog.show();
						var actionBar = dojo.create("div", { "class": "dijitDialogPaneActionBar"  }, this.resultsDialog.containerNode);
				        var buttonOk = new dijit.form.Button({"label": "OK",
				            onClick: () => {
				            	this.resultsDialog.destroyRecursive(); 
				            	
				            } });
				        buttonOk.placeAt(actionBar);
				        domClass.add(buttonOk.focusNode, "yesNoOkButton");
						
						this.cancel();
					}
				})
			});
			
		},
		


		initGrid: function(folders){
			var data = { identifier: 'itemId', items: folders};
			var store = ItemFileWriteStore({data: data});
			var layout = [[
			    {'name': 'Name', 'field': 'name', 'width': '50%' },
			    {'name': 'Class', 'field': 'class', 'width': '50%' }
			]];
			
			var ga = this.id + '_gridArea';
			
			this.grid = new DataGrid({
				store: store,
				selectionMode:'extended',
				autoHeight: true,
				rowSelector: '1px', 
				structure: layout,
				
				onRowDblClick: (e) => {
					this.doubleClick = e.grid.getItem(e.rowIndex).itemId;
					this.removeFromFolder();
					
				}
			});
			
			domStyle.set(ga, 'width', '100%');
			domStyle.set(ga, 'height', '100%');
			domStyle.set(ga, 'overflow', 'auto');
			
			
			this.grid.placeAt(ga);
			this.grid.startup();
			
		},
		
		removeFromFolder: function(){
			var folderIds = null;
				if(this.doubleClick)
					folderIds = this.doubleClick;
				else
					folderIds = this.grid.selection.getSelected().map((folder) => folder.itemId);
			lang.mixin(this.serviceParameters, 
					{
						frontEndAction: "removeFromFolder", 
						folderIds: JSON.stringify([].concat.apply([], folderIds)), // flattens and stringifies folder ids
					}
			);
			
			Request.postPluginService('GilCnPlugin', 'FolderService','application/x-www-form-urlencoded', {
				requestBody : ioquery.objectToQuery(this.serviceParameters),
				requestCompleteCallback : lang.hitch(this, function (response) {
					if(response.success){
						this.resultsDialog = new NonModalConfirmDialog({
			            	id: this.id + '_no_folders_dialog',
			            	title: 'Items successfully removed', 
			                style: "width:300px;height:180px;",
			                content: "The selected items were successfully removed from the folders.",
			            });
						var actionBar = dojo.create("div", { "class": "dijitDialogPaneActionBar"  }, this.resultsDialog.containerNode);
				        var buttonOk = new dijit.form.Button({"label": "OK",
				            onClick: () => {
				            	this.resultsDialog.destroyRecursive(); 
				            	
				            } });
				        buttonOk.placeAt(actionBar);
						this.resultsDialog.show();
					} else {
						this.resultsDialog = new NonModalConfirmDialog({
			            	id: this.id + '_no_folders_dialog',
			            	title: 'Something went wrong', 
			                style: "width:300px;height:180px;",
			                content: "Something went wrong. Please contact your system administrator to check logs.",
			            });
						var actionBar = dojo.create("div", { "class": "dijitDialogPaneActionBar"  }, this.resultsDialog.containerNode);
				        var buttonOk = new dijit.form.Button({"label": "OK",
				            onClick: () => {
				            	this.resultsDialog.destroyRecursive(); 
				            	
				            } });
				        buttonOk.placeAt(actionBar);
						this.resultsDialog.show();
					}
					this.cancel();
				})
			});
		},
		
		onlyUnique: function(value, index, self) { 
			return self.indexOf(value) === index;
		},
		

		
	});
});