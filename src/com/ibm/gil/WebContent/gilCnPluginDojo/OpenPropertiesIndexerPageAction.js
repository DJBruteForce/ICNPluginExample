define([ "dojo/_base/declare", 
         "dojo/_base/lang", 
         "ecm/model/Action", 
         "ecm/model/Request", 
         "gilCnPluginDojo/MessagesDialog", 
         "gilCnPluginDojo/PopupDialog",
         "ecm/model/Desktop",
         "gilCnPluginDojo/GILInitializerForm",
         "gilCnPluginDojo/GILInitializerFields",
         "gilCnPluginDojo/util/StringFormatterUtils",
         "dojo/_base/json" ,
         "gilCnPluginDojo/OfferingLetterDialog",
         "gilCnPluginDojo/InvoiceELSDialog",
         "gilCnPluginDojo/constants/ValidationConstants",
 	    "dojo/date",
	    "dojo/date/locale",
	    "dojo/date/stamp",
	    "ecm/widget/dialog/EditPropertiesDialog",

	    "dojo/aspect",
	    "dojo/dom-style",
         ], 
         
   function (declare, lang, Action, Request, MessagesDialog, PopupDialog, Desktop,GILInitializerForm, GILInitializerFields,
		   StringFormatterUtils,json, OfferingLetterDialog, InvoiceELSDialog,ValidationConstants,date,
		   locale,stamp,EditPropertiesDialog,aspect,domstyle) {
	
	/**
	 * the first class that is called when user clicks 'Index Document'.
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
							  "desktopUserId": Desktop.userId
		
							};
					
			   		this.serviceParams['repositoryId']  = itemList[0].repository.id;
			   		this.serviceParams['repositoryType']  = itemList[0].repository.type;
			   		this.serviceParams['docId']  = itemList[0].id;
			   		this.serviceParams['userId'] = Desktop.userId;
			   	
		   	
					/**
					 * this code gets the selected documents attributes values and use a callback 
					 * function(a function that is passed as a parameter to another function) to
					 * to create a JSON object with all theses attributes and then call the 
					 * correspondent form dialog.
					 * 
					 */
					itemList[0].retrieveAttributes(lang.hitch(this, function(item) {
						
						thisForm = this;
						var prop = new EditPropertiesDialog();
						prop.showProperties(item, function(){
							
							
							var businessAttributesJson = {};
							var stringFormatter = new StringFormatterUtils();
							var fields = new GILInitializerFields(itemList[0].getContentClass().name);
						    var now = new Date();
							
							var fieldsArray = fields.get("doc");
							var formFieldsArray = fields.get("form");
						
					   		 for (var attr in item.attributes) {
								
					   			var indexOfField = fieldsArray.indexOf( attr.toString().trim() );
					   			 
									if ( indexOfField > -1 )	{
										
										var field = stringFormatter.removeSpecialCharacters(attr);
										var fieldValue = item.attributes[attr];
										
										if(field == 'createdTimestamp') {
											
											var parsedCreatedTimestamp = stamp.fromISOString(fieldValue).toGMTString();
											fieldValue = locale.format(new Date(parsedCreatedTimestamp),{ selector: "date", datePattern : ValidationConstants("DefaultTimeStampPattern")  });								 
										}
										
										businessAttributesJson[formFieldsArray[indexOfField]] = fieldValue;
			
									}
									
								}
					   		 

					   		thisForm.serviceParams['businessAttributes'] = dojo.toJson(businessAttributesJson);
					   		thisForm.serviceParams['className'] = itemList[0].getContentClass().name;
					   		thisForm.serviceParams['itemId'] 	= itemList[0].itemId;
					   		thisForm.serviceParams['userTimeZone'] 	= dojo.date.getTimezoneName(now);
					   		thisForm.serviceParams['gcmsServer'] = null;
					   		thisForm.serviceParams['rdcServer'] =null;
					   		thisForm.serviceParams['accessLevelPrivileges'] = null;
					   		
							var gilForm = new GILInitializerForm(thisForm.serviceParams);
							dialog =  gilForm.buildIndexingFormDialog();
							
						}, teamspace);
						

					}), false);
					
					   	
			    }
	  });
	});