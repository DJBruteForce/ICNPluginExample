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
	    "gilCnPluginDojo/NonModalConfirmDialog",
	    "dojo/dom-class",
	    "dijit/form/Button",
	    "gilCnPluginDojo/DialogMessages",
	    "dijit/registry",
	    "dojo/query",
	    "dojo/dom-attr"
         ], 
         
   function (declare, lang, Action, Request, MessagesDialog, PopupDialog, Desktop,GILInitializerForm, GILInitializerFields,
		   StringFormatterUtils,json, OfferingLetterDialog, InvoiceELSDialog,ValidationConstants,date,
		   locale,stamp,EditPropertiesDialog,aspect,domStyle,NonModalConfirmDialog,domclass,Button,DialogMessages,
		   registry, query, domAttr) {
	
	/**
	 * the first class that is called when user clicks 'Index Document'.
	 */  
	return declare(Action, {

			/**
			 * parameters that will be passed to the service.
			 */ 
		  	serviceParams :null,
		  	counter:0,

		    /**
		     * Overriding method performAction
		     */
			 performAction : function (repository, itemList, callback, teamspace, resultSet, parameterMap) {

					this.serviceParams = {  "repository":repository,
							  				"desktopUserId": Desktop.userId.trim().toUpperCase()  };			
			   		this.serviceParams['repositoryId']  = itemList[0].repository.id;
			   		this.serviceParams['userId'] = Desktop.userId.trim().toUpperCase();

					/**
					 * this code gets the selected documents attributes values and use a callback 
					 * function(a function that is passed as a parameter to another function) to
					 * to create a JSON object with all theses attributes and then call the 
					 * correspondent form dialog.
					 * 
					 */
					itemList[0].retrieveAttributes(lang.hitch(this, function(item) {
						
						thisOpenNoIndexDlg = this;
						thisOpenNoIndexDlg.counter = 0;
						
						if(item.locked && thisOpenNoIndexDlg.serviceParams['userId'].trim().toUpperCase()!=item.lockedUser.trim().toUpperCase() ) {
							var dialogMessages = new DialogMessages();
							dialogMessages.addConfirmMessages("The document is checked out to user "+ item.lockedUser +". Do you want to open the item in browse mode instead?");
							thisOpenNoIndexDlg.showMessage("Confirm",dialogMessages.confirmMessages(),true ,thisOpenNoIndexDlg.openProperties.bind(null,item,itemList[0],teamspace), thisOpenNoIndexDlg.cancel);
						} else {
							thisOpenNoIndexDlg.openProperties(item,itemList[0],teamspace);
						}

					}), false);
					   	
			    }
	
	,
	
	 openProperties: function(itemObj, itemListObj, teamSpaceObj) {
			var prop = new EditPropertiesDialog();
			//if the action is called via work view, an ecm.model.WorkItem is retrieved instead an ecm.model.ContentItem
			if(itemObj.declaredClass == "ecm.model.WorkItem")
				itemObj = itemObj.contentItem;
			prop.showProperties(itemObj, null, teamSpaceObj);
			
			// After rendering, display notelogs tab
			aspect.after(prop._itemEditPane, 'onCompleteRendering',
				() => prop._itemEditPane._tabContainer.selectChild(prop._itemEditPane._itemNotelogsPane));
			
	},
	
	
	showMessage: function(titl, msg, needConfirmation, callbackYes, callbackNo) {	
		var d1 = new NonModalConfirmDialog({
            title: titl, 
            style: "min-width:350px;",
            content:msg,
            name:"dialogMessage"
        });
        
        if(needConfirmation){
        	
	        var actionBar = dojo.create("div", {"class": "dijitDialogPaneContentArea",}, d1.containerNode);
	        var actionBar = dojo.create("div", {"class": "dijitDialogPaneActionBar"   }, d1.containerNode);

	       var buttonYes = new dijit.form.Button({ "label": "Yes", 
	            onClick: function(){
	            	callbackYes();
	            	d1.destroyRecursive(); 
	            }});
	        
	        buttonYes.placeAt(actionBar);
	        domclass.add(buttonYes.focusNode, "yesNoOkButton");
	        												
	        var buttonNo = new dijit.form.Button({"label": "No",
	            onClick: function(){
	            	d1.destroyRecursive(); 
	            	}});
	        
	        buttonNo.placeAt(actionBar);
	        domclass.add(buttonNo.focusNode, "yesNoOkButton");
        	
        } else {
        
		        var actionBar = dojo.create("div", {"class": "dijitDialogPaneContentArea",}, d1.containerNode);
		        var actionBar = dojo.create("div", { "class": "dijitDialogPaneActionBar"  }, d1.containerNode);
	
		        var buttonOk = new dijit.form.Button({"label": "Ok",
		            onClick: function(){
		            	document.getElementById(d1.id).remove();
		            } });
		        
		        buttonOk.placeAt(actionBar);
		        domclass.add(buttonOk.focusNode, "yesNoOkButton");
		        
		      var buttonCancel = new dijit.form.Button({"label": "Cancel", 
		            onClick: function(){
		            	document.getElementById(d1.id).remove();
		            	} });
		        
		        buttonCancel.placeAt(actionBar);
		        domclass.add(buttonCancel.focusNode, "cancelButton")
        }
        
        d1.startup();
        d1.show();
		
	},
	
	});
});
