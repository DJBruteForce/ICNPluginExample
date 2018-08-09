/**
 * @author mdevino
 */
define([
	'dojo/_base/declare', 
	'dojo/_base/lang', 
	'ecm/model/Action', 
	'ecm/model/Request',
	'gilCnPluginDojo/RemoveFromFolderDialog',
	'ecm/model/Desktop'
	],
	
function (declare, lang, Action, Request, RemoveFromFolderDialog, Desktop) {
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
				'repository':repository,
				'desktopUserId': Desktop.userId.trim().toUpperCase()
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
			if(itemList[0].declaredClass == 'ecm.model.WorkItem')
				itemList[0] = itemList[0].contentItem;
			this.serviceParams['itemIds'] = JSON.stringify(itemList.map((item) => item.itemId));
			this.showAddToFolder();
					   	
		},

		showAddToFolder: function() {
			var dialog = new RemoveFromFolderDialog(this.serviceParams);
		}
		
	});
});
