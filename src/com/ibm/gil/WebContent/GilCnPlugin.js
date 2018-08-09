require(["dojo/_base/declare",
         "dojo/_base/lang",
         "dojo/aspect",
         "gilCnPluginDojo/util/PropertiesDialogInitializer"], 
function(declare, lang,aspect,PropertiesDialogInitializer) {		
	/**
	 * Use this function to add any global JavaScript methods your plug-in requires.
	 */
lang.setObject("openIndexerAction", function(repository, items, callback, teamspace, resultSet, parameterMap) {
 /*
  * Add custom code for your action here. For example, your action might launch a dialog or call a plug-in service.
  */
});

lang.setObject("openNoIndexAction", function(repository, items, callback, teamspace, resultSet, parameterMap) {
	 /*
	  * Add custom code for your action here. For example, your action might launch a dialog or call a plug-in service.
	  */
	});

	var propertiesInitializer = new PropertiesDialogInitializer();
	propertiesInitializer.run();


});
