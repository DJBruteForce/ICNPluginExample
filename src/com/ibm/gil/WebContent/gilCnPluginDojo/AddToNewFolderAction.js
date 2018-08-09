/**
 * @author mdevino
 */
define([
	'dojo/_base/declare', 
	'dojo/_base/lang', 
	'ecm/model/Action', 
	'ecm/model/Request',
	'ecm/model/Desktop',
	'ecm/widget/dialog/AddContentItemDialog',
	'dojo/io-query',
	'gilCnPluginDojo/NonModalConfirmDialog',
	'dojo/dom-class',
	'dojo/aspect'
	
	],
	
function (declare, lang, Action, Request, Desktop, AddContentItemDialog, ioquery, NonModalConfirmDialog, domClass,
		aspect) {
	/**
	 * The first class that is called when user clicks 'History Log'.
	 */  
	return declare(Action, {

		/**
		 * parameters that will be passed to the service.
		 */ 
		serviceParams :null,

		/**
		 * Overriding method performAction
		 */
		performAction : function (repository, itemList, callback, teamspace, resultSet, parameterMap) {
			this.serviceParams = {  
				"repository":repository,
				"desktopUserId": Desktop.userId.trim().toUpperCase()
			};			
			this.serviceParams['repositoryId']  = itemList[0].repository.id;
			this.serviceParams['userId'] = Desktop.userId.trim().toUpperCase();
			/**
			 * this code gets the selected documents attributes values and use a callback 
			 * function(a function that is passed as a parameter to another function) to
			 * to create a JSON object with all theses attributes and then call the 
			 * correspondent form dialog.
			 * 
			 */
			if(itemList[0].declaredClass == "ecm.model.WorkItem")
				itemList.map(item => item.contentItem);
			this.serviceParams['itemIds'] = itemList.map(item => {return item.itemId});
			this.serviceParams.itemIds = JSON.stringify(this.serviceParams.itemIds);
			this.showAddToFolder(repository, null, false, false, this.addItemsToNewFolder, teamspace, false, null);
					   	
		},

		showAddToFolder: function(repository, parentFolder, typeDocument, virtualItems, callback, teamspace, useTemplate, entryTemplate, showMultiRepoFolderSelector, repositoryTypes, repositoryDocumentMode, virtualItemObjects) {
			var dialog = new AddContentItemDialog({serviceParams: this.serviceParams, destroyWhenFinished:true, skipPreloadingFileTracker:true});
			repository.retrieveContentClasses(classes => {
   				var notFolders = classes.filter(c => !c.name.toLowerCase().includes("folder"));
   				notFolders = notFolders.map(c => c.id);
   				dialog.addContentItemPropertiesPane._contentClassSelector.setExcludedItems(notFolders);
   				dialog.removeGilClassesAspectHandler.remove();
   				dialog.show(repository, parentFolder, typeDocument, virtualItems, callback, teamspace, useTemplate, entryTemplate, showMultiRepoFolderSelector, repositoryTypes, repositoryDocumentMode, virtualItemObjects);
   				
   				
   			});
		},
		
		addItemsToNewFolder: function(newFolder){
			this.serviceParams.folderId = newFolder.itemId;
			this.serviceParams.frontEndAction = 'addToFolder';
			Request.postPluginService("GilCnPlugin", "FolderService", 'application/x-www-form-urlencoded',{
				requestBody : ioquery.objectToQuery(this.serviceParams),
	    		requestCompleteCallback : lang.hitch(this, function (response) {
					this.successDialog = new NonModalConfirmDialog({
		            	id: this.id + '_no_folders_dialog',
		            	title: 'Items added to folder', 
		                style: "width:300px;height:150px;",
		                content: "The selected items were succesfully added to the new folder.",
		            });
					this.successDialog.show();
					var actionBar = dojo.create("div", { "class": "dijitDialogPaneActionBar"  }, this.successDialog.containerNode);
			        var buttonOk = new dijit.form.Button({"label": "OK",
			            onClick: () => {
			            	this.successDialog.destroyRecursive(); 
			            	
			            } });
			        buttonOk.placeAt(actionBar);
			        domClass.add(buttonOk.focusNode, "yesNoOkButton");
	    		})
			});
		},
		
	});
});
