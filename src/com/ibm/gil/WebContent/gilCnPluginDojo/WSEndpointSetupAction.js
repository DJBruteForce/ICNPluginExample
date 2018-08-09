/**
 * @author mdevino
 */
define([
	"dojo/_base/declare", 
	"dojo/_base/lang", 
	"ecm/model/Action", 
	"ecm/model/Request",
	"gilCnPluginDojo/WSEnpointSetupDialog",
	"ecm/model/Desktop"
	],
	
function (declare, lang, Action, Request, WSEnpointSetupDialog, Desktop) {
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
				"desktopUserId": Desktop.userId.trim().toUpperCase(),
				"teamspace": teamspace
			};			
			this.serviceParams['userId'] = Desktop.userId.trim().toUpperCase();
				this.showMenu();	   	
		},

		showMenu: function() {
			var dialog = new WSEnpointSetupDialog(this.serviceParams);
			dialog.show();
		}
		
	});
});

