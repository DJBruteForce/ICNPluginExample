define(["dojo/_base/lang",
        "ecm/model/Action", 
        "ecm/model/Request", 
        "dojo/_base/declare", 
		"ecm/widget/dialog/BaseDialog",
		"dojo/json",
		"gilCnPluginDojo/util/JsonUtils",
		"gilCnPluginDojo/util/MathJs",
		"dojo/text!./templates/COAELSDialog.html",
	    "dojo/_base/array",
	    "dojo/dom",
	    "dojo/domReady!",
	    "dijit/form/Form",
	    "dojo/date",
	    "dojo/date/locale",
	    "dojo/dom-style",
	    "dijit/registry",
	    "dojo/query",
	    "dojo/dom-construct",
	    "dijit/form/DateTextBox",
	    "dojo/parser",
	    "dojo/dom-attr",
	    "gilCnPluginDojo/ELSDialog",
	    "gilCnPluginDojo/constants/ValidationConstants",
	    "gilCnPluginDojo/ConfirmDialog",
	    "gilCnPluginDojo/NonModalConfirmDialog",
	    "dojo/i18n",
	    "dojo/number",
	    "dijit/form/Button",
	    "gilCnPluginDojo/ConfirmationDialog",
	    "dojox/string/Builder",
	    "gilCnPluginDojo/DialogMessages",
	    "ecm/widget/dialog/ConfirmationDialog",
	    "dijit/Tooltip",
	    "dojo/date/stamp",
	    "dijit/focus",
	    "dojo/_base/connect", 
	    "dojo/_base/event",
	    "dojo/_base/array",
	    "dojo/dom-class",
	    "gilCnPluginDojo/GILDialog",
	    "dojo/io-query",
	    "dojo/aspect" 
	    
	],
	
	function(lang, action, request,declare,	BaseDialog,	json,JsonUtils, MathJs, template, 
			array, dom,domReady,form,date,locale,domStyle,registry,query,construct,DateTextBox,
			parser,domattr,ELSDialog,ValidationConstants,ConfirmDialog,NonModalConfirmDialog,i18n,
			number,Button,ConfirmationDialog,Builder,DialogMessages,ECMConfirmationDialog,Tooltip,stamp ,
			focus,connect, event,array,domclass,GILDialog,ioquery,aspect) {

		
		var COAELSDialog = declare("gilCnPluginDojo.COAELSDialog",[BaseDialog,GILDialog,ELSDialog], {
		
		contentString: template,
		widgetsInTemplate: true,
		jsonCoaObject : null,
		cmValues:null,
		localeBundle:null,

		postCreate: function() {
			
			this.inherited(arguments);
			this.setSize(800, 330);
			this.setResizable(false);
			this.setMaximized(false);
			this.addButton("Cancel", "cancelIndexing", false, true);
			this.addButton("Save", "save", false, true);
			this.setTitle("GCMS Index COA [" + this.serviceParameters['className'] + "]");
			this.setWidgetsInitialization();
			this.setNonEditableFields();
			this.setCustomizedValidation();
			this.initializeDateFields();
			this.initializeAmountFields();
			this.upperCaseFields();
			this.setLayout();
		},

		setWidgetsInitialization: function() {
			
			dojo.style(this.cancelButton.domNode,"display","none"); 
		},
		
		setLayout:function(){
			
			var restoring = (restore) => {
				if(restore != null){

					if(!domclass.contains(this.actionBar,"coaELSEcmDialogPaneActionBar")){			
						domclass.add(this.actionBar,"coaELSEcmDialogPaneActionBar");
					}

				}
			};

			var maximizing = (maximize) => {
				if(maximize != null){

					if(domclass.contains(this.actionBar,"coaELSEcmDialogPaneActionBar")){			
						domclass.remove(this.actionBar, 'coaELSEcmDialogPaneActionBar');
					}

				}
			};

			aspect.after(this._maximizeButton, 'onClick', maximizing, this._maximizeButton);
			aspect.after(this._restoreButton, 'onClick', restoring, this._restoreButton);
			domclass.add(this.actionBar,"coaELSEcmDialogPaneActionBar");
		}, 
		
		upperCaseFields: function() {
			var allValTextbox = dojo.query('input#[id^="'+ this.id +'_"]');
			for(var i=0; i<allValTextbox.length; i++){
				this.toUpperCase(allValTextbox[i]);
			}
		},
		
		initializeDateFields: function() {
			var allFormInputWid = dojo.query('input#[id*="Date"][id^="'+ this.id +'_"]');
			for(var i=0; i<allFormInputWid.length; i++){
			  this.setDatePattern(registry.byId(allFormInputWid[i].id));
			}
		},
		
		initializeAmountFields: function() {
    		this.isNumberKey(dijit.byId(this.id+"_coaAmount"));
		},
		
		
		cancel: function() {

			thisForm.finalizeGUI();
		},
		
		performCancelIndexing: function() {
			
			if (thisForm.jsonCoaObject['DIRTYFLAG']){
				thisForm.serviceParameters['frontEndAction'] = "rollbackIndexing";
				request.postPluginService("GilCnPlugin", "COAELSService",'application/x-www-form-urlencoded', {
		    		requestBody : ioquery.objectToQuery(thisForm.serviceParameters),
				    requestCompleteCallback : lang.hitch(this, function (response) {
		    			
		    			thisForm.finalizeGUI();
		    			console.log('Index rolled back successfully.');
		    		})
				});	
			} else {
				thisForm.finalizeGUI();
			}
		},

		cancelIndexing: function() {
			
			var dlgMsg = new DialogMessages();
			if( thisForm.isFormDirty(thisForm.id+"_coaForm") || thisForm.jsonCoaObject['DIRTYFLAG']){
					dlgMsg.addConfirmMessages("You have unsaved changes. Are you sure you want to quit?");
						thisForm.showMessage("Confirm",dlgMsg.confirmMessages(),true ,thisForm.performCancelIndexing, thisForm.noop);
				} else {
					thisForm.performCancelIndexing();
				}
			
		},

		formatAmount: function() {
			
			var num = this.formatNumber(this.coaAmount.get('value'),2);
			this.coaAmount.set('value',num);
		},
		
		save: function() {
			
			this.serviceParameters['frontEndAction'] = arguments.callee.nom;
			var dialogMessages = null;
			thisForm = this;

			try {

				if (!this.coaForm.validate()) {
					console.log('Errors found on screen.');
		            return false;
				}
			    
				thisForm.jsonCoaObject['validationIsDuplicateCOAFromCM'] = false;
				thisForm.jsonCoaObject['validationIsDuplicateCOANumberEntered'] = false;
				thisForm.jsonCoaObject['validationIsCoaNotFoundInGCMS'] = false;
				thisForm.jsonCoaObject['validationIsCoaLockedInGCMS'] = false;
				thisForm.jsonCoaObject['validationIsCoaDeactivatedInGCMS'] = false;
				thisForm.jsonCoaObject['validationIsPendingPricingApprovalStatus'] = false;
				thisForm.jsonCoaObject['validationIsIncorrectStatusForCoa'] = false;
				thisForm.jsonCoaObject['validationIsDocumentProperlyIndexed'] = true;
				
				thisForm.jsonCoaObject['COANUMBER'] = thisForm.coaNumber.get('value');
				thisForm.jsonCoaObject['OFFERINGLETTER'] = thisForm.coaOfferingLetterNumber.get('value');
				thisForm.jsonCoaObject['CUSTOMERNAME'] = thisForm.coaCustomerName.get('value');
				thisForm.jsonCoaObject['OFFERINGLETTERVALIDITYDATE']  = thisForm.coaOLValididityDate.get('displayedValue');
				thisForm.jsonCoaObject['CURRENCY'] = thisForm.coaCurrency.get('value');
				thisForm.jsonCoaObject['AMOUNT'] = thisForm.coaAmount.get('value');
				thisForm.jsonCoaObject['SIGNEDBY'] = thisForm.coaSignedby.get('value');
					
				thisForm.serviceParameters['jsonDataObject']	= JSON.stringify(thisForm.jsonCoaObject);
				
				dialogMessages = this.validateOnSave(thisForm.performSave);
				
				console.log("COA JSON being submitted: "+JSON.stringify(thisForm.jsonCoaObject, null, 2));
					
			} catch(e) {
				
				console.log('Error in method ' + arguments.callee.nom + ' - ' + e);
			}
		},
		
		
		setNonEditableFields: function() {
			
			this.coaNumber.set('disabled',true);
			this.coaOfferingLetterNumber.set('disabled',true);
			this.coaCustomerName.set('disabled',true);
			this.coaOLValididityDate.set('disabled',true);
			this.coaCurrency.set('disabled',true);
			this.coaAmount.set('disabled',true);
		},
		
		
		validateOnSave: function(perfomSaveCallback) {
			
			this.serviceParameters['frontEndAction'] = arguments.callee.nom;
			var dialogMessages = new DialogMessages();
			
			try {
				
				request.postPluginService("GilCnPlugin", "COAELSService",'application/x-www-form-urlencoded', {
		    		requestBody : ioquery.objectToQuery(thisForm.serviceParameters),
				    requestCompleteCallback : lang.hitch(this, function (response) {
		    			
		    			this.jsonCoaObject	= JSON.parse(response.coaJsonString);
		    			
				    	if(!this.jsonCoaObject.validationIsDocumentProperlyIndexed) {
				    		
				    		dialogMessages.addErrorMessages("Document did not index correctly.");
				    	}
					    	
				    	perfomSaveCallback(dialogMessages);
		    		})
				});
				
			}catch(e){
				
				console.log('Error in method ' + arguments.callee.nom + ' - ' + e);
			}

		},

		performSave: function(dialogMessages) {
			
			try {
			
				if(!dialogMessages.hasErrorMessages() ){
					
					thisForm.serviceParameters['frontEndAction'] = 'save';
					var jsonUtils = new JsonUtils();
					
					request.postPluginService("GilCnPlugin", "COAELSService",'application/x-www-form-urlencoded', {
			    		requestBody : ioquery.objectToQuery(thisForm.serviceParameters),
					    requestCompleteCallback : lang.hitch(this, function (response) {
								    			
								    			this.jsonCoaObject	= JSON.parse(response.coaJsonString);
								    			
												if(this.jsonCoaObject.dialogErrorMsgs.length>0){
													var msgs=this.jsonCoaObject.dialogErrorMsgs;
													for (var i in msgs) {
														dialogMessages.addErrorMessages(msgs[i]);
													}
													
													thisForm.showMessage("Error",dialogMessages.getAllMessages(),false,null,null);
												} else {
													thisForm.destroyRecursive()
												}

								    		})
								});
						
				} else {
					
	    			thisForm.showMessage("Validation",dialogMessages.getAllMessages(),false,null,null);
	    			return false;   
				}
				
			} catch(e) {
				
				console.log('Error in method ' + arguments.callee.nom + ' - ' + e);
			}
		},

		show: function() {
			this.inherited("show", []);
		},
		
		
		index: function() {
			
			try {

					this.serviceParameters['frontEndAction'] = arguments.callee.nom;
					cmValues = JSON.parse(this.serviceParameters['businessAttributes']);
					thisForm = this;
					var jsonUtils = new JsonUtils();
					var dialogMessages = new DialogMessages();
					
					request.postPluginService("GilCnPlugin", "COAELSService",'application/x-www-form-urlencoded', {
			    		requestBody : ioquery.objectToQuery(this.serviceParameters),
					    requestCompleteCallback : lang.hitch(this, function (response) {
								    			
									    			this.jsonCoaObject	= JSON.parse(response.coaJsonString);
								    			
								    			registry.byId(this.id+"_coaNumber").set('value',this.jsonCoaObject.COANUMBER);
								    			registry.byId(this.id+"_coaOfferingLetterNumber").set('value',this.jsonCoaObject.OFFERINGLETTER );
								    			registry.byId(this.id+"_coaCustomerName").set('value',this.jsonCoaObject.CUSTOMERNAME );
								    			registry.byId(this.id+"_coaOLValididityDate").set('value',this.parseDate(this.jsonCoaObject.OFFERINGLETTERVALIDITYDATE),false);	    			
								    			registry.byId(this.id+"_coaCurrency").set('value',this.jsonCoaObject.CURRENCY);
								    			registry.byId(this.id+"_coaSignedby").set('value',this.jsonCoaObject.SIGNEDBY);

								    			if(this.jsonCoaObject.AMOUNT!=null && this.jsonCoaObject.AMOUNT.trim()!="" ) {
								    				
								    				registry.byId(this.id+"_coaAmount").set('value',this.jsonCoaObject.AMOUNT.replace(/,/g, '.') );
								    			}
								    			
								    			dialogMessages = this.validateOnIndex();
         							    		
												if(!dialogMessages.hasErrorMessages() && !dialogMessages.hasWarningMessages() ){
													
													console.log('Index loading successfully.')
														
												} else if(dialogMessages.hasErrorMessages() ){
													
									    			this.showMessage("Error",dialogMessages.getAllMessages(),false,null,null);
									    			this.rollbackIndexing();
									    			this.cancel();
												}
								    			this.show();
								    			this.serialFormClean=this.serializeForm(thisForm.id+"_coaForm");  
								    		})
								});
					
			} catch(e) {
				
				console.log('Error in method ' + arguments.callee.nom + ' - ' + e);
			}
		},
		
		validateOnIndex : function() {

			var dialogMessages = new DialogMessages();
			 
			 try {
			 
				 if (this.jsonCoaObject.validationIsDuplicateCOAFromCM) {
					 
					 dialogMessages.addErrorMessages("Duplicate COA retrieved from Content Manager.");
					 return dialogMessages;
				 }
				 
				 if (this.jsonCoaObject.validationIsDuplicateCOANumberEntered) {
					 
					 dialogMessages.addErrorMessages("Duplicate COA Number entered.");
					 return dialogMessages;
				 }
				 
				 if (this.jsonCoaObject.validationIsCoaNotFoundInGCMS) {
					 
					 dialogMessages.addErrorMessages("COA not found in GCMS");
					 return dialogMessages;
				 }
				 
				 if (this.jsonCoaObject.validationIsCoaLockedInGCMS) {
					 
					 dialogMessages.addErrorMessages("COA is locked in GCMS, please unlock and try later.");
					 return dialogMessages;
				 }
				 
				 if (this.jsonCoaObject.validationIsCoaDeactivatedInGCMS) {
					 
					 dialogMessages.addErrorMessages("COA has been deactivated in GCMS.");
					 return dialogMessages;
				 }
				 
				 if (this.jsonCoaObject.validationIsPendingPricingApprovalStatus) {
					 
					 dialogMessages.addErrorMessages("Do not allow Sign COA to be reindexed if in Pending Pricer Approval Status.");
					 return dialogMessages;
				 }
				 
				 if (this.jsonCoaObject.validationIsIncorrectStatusForCoa) {
					 
					 dialogMessages.addErrorMessages("COA has incorrect status for Signed COA.");
					 return dialogMessages;
				 }
	                
	                return dialogMessages;  
				 
			 } catch(e) {
					
					console.log('Error in method ' + arguments.callee.nom + ' - ' + e);
				}
		},
		
		
		setCustomizedValidation : function() {

			lang.getObject('coaNumber', false, this).validator = function() {
				
				if (lang.getObject('coaNumber', false, thisForm).get("value") == null ||
						lang.getObject('coaNumber', false, thisForm).get("value").trim().length == 0  	)	{

					lang.getObject('coaNumber', false, thisForm).set("invalidMessage","COA Number is a required field.");					
					lang.getObject('coaNumber', false, thisForm).set("missingMessage","COA Number is a required field.");
					
					return false;
				}
				
				return true;		
			}

		},

		rollbackIndexing : function() {

			thisForm.serviceParameters['frontEndAction'] = arguments.callee.nom;
			
			request.postPluginService("GilCnPlugin", "COAELSService",'application/x-www-form-urlencoded', {
	    		requestBody : ioquery.objectToQuery(thisForm.serviceParameters),
			    requestCompleteCallback : lang.hitch(this, function (response) {

	    			console.log('Index rolled back successfully.');

	    		})
			});
		},
		
		
		rollbackCountryIndexing : function() {

			thisForm.serviceParameters['frontEndAction'] = arguments.callee.nom;
			
			request.postPluginService("GilCnPlugin", "COAELSService",'application/x-www-form-urlencoded', {
	    		requestBody : ioquery.objectToQuery(thisForm.serviceParameters),
			    requestCompleteCallback : lang.hitch(this, function (response) {

	    			console.log('Country index rolled back successfully.');

	    		})
			});
		},
	        	
		showMessage: function(titl, msg, needConfirmation, callbackYes, callbackNo) {	
			
			thisForm = this;
            var d1 = new NonModalConfirmDialog({
                title: titl, 
                style: "min-width:350px;",
                content:msg,
                name:"dialogMessage"
            })
            
            if(needConfirmation){
            	
		        var actionBar = dojo.create("div", {"class": "dijitDialogPaneContentArea",}, d1.containerNode);
		        var actionBar = dojo.create("div", {"class": "dijitDialogPaneActionBar"   }, d1.containerNode);
	
		        var buttonYes = new dijit.form.Button({ "label": "Yes",
		            onClick: function(){
		            	
		            	callbackYes();
		            	d1.destroyRecursive(); 
		            	
		            } });
		        buttonYes.placeAt(actionBar);
		        domclass.add(buttonYes.focusNode, "yesNoOkButton");
		        
		        var buttonNo = new dijit.form.Button({"label": "No",
		            onClick: function(){
		            	
		            	callbackNo();
		            	d1.destroyRecursive();
		            	
		            } });
		        buttonNo.placeAt(actionBar);
		        domclass.add(buttonNo.focusNode, "yesNoOkButton");
            	
            } else {
            
			        var actionBar = dojo.create("div", {"class": "dijitDialogPaneContentArea",}, d1.containerNode);
			        var actionBar = dojo.create("div", { "class": "dijitDialogPaneActionBar"  }, d1.containerNode);
		
			        var buttonOk = new dijit.form.Button({"label": "Ok",
			            onClick: function(){
			            	
			            	d1.destroyRecursive();
			            	
			            } });
			        buttonOk.placeAt(actionBar);
			        domclass.add(buttonOk.focusNode, "yesNoOkButton");
			        
			        var buttonCancel = new dijit.form.Button({"label": "Cancel",
			            onClick: function(){
			            	
			            	d1.destroyRecursive();
			            	
			            } });
			        buttonCancel.placeAt(actionBar);
			        domclass.add(buttonCancel.focusNode, "cancelButton")
   
            }
            
	        d1.startup();
            d1.show();
			
		},

	});
		
		return COAELSDialog
});
