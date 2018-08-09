define(["dojo/_base/lang",
        "ecm/model/Action", 
        "ecm/model/Request", 
        "dojo/_base/declare", 
		"ecm/widget/dialog/BaseDialog",
		"dojo/json",
		"gilCnPluginDojo/util/JsonUtils",
		"gilCnPluginDojo/util/MathJs",
		"dojo/text!./templates/OfferingLetterELSDialog.html",
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
	    "dojo/io-query",
	    "gilCnPluginDojo/GILDialog",
	    "dojo/dom-class",
	    "dojo/aspect" ,
	],
	
	function(lang, action, request,declare,	BaseDialog,	json,JsonUtils, MathJs, template, 
			array, dom,domReady,form,date,locale,domStyle,registry,query,construct,DateTextBox,
			parser,domattr,ELSDialog,ValidationConstants,ConfirmDialog,NonModalConfirmDialog,i18n,
			number,Button,ConfirmationDialog,Builder,DialogMessages,ECMConfirmationDialog,Tooltip,stamp ,
			focus,connect, event,array,ioquery,GILDialog,domclass,aspect) {

		
		var COAELSDialog = declare("gilCnPluginDojo.OfferingLetterELSDialog",[BaseDialog,GILDialog,ELSDialog], {
		
		contentString: template,
		widgetsInTemplate: true,
		jsonOLELSObject : null,
		cmValues:null,
		localeBundle:null,
		
	    MANDATORY : "M",
		OPTIONAL : "O",
		HIDDEN : "H",
		CUSTOMERSIGNAME : "CSIGNAME",
		CUSTOMERSIGDATE : "CSIGDATE",
		IBMSIGDATE : "ISIGDATE",
		DEEMEDCUSTOMERSIGNAME : "DMCSIGNAME",
		DEEMEDCUSTOMERSIGDATE : "DMCSIGDATE",
		DEEMEDIBMSIGDATE : "DMISIGDATE",

		postCreate: function() {
			
			this.inherited(arguments);
			this.setSize(650, 400);
			this.setResizable(false);
			this.setMaximized(false);
			this.addButton("Cancel", "cancel", false, true);
			this.addButton("Save", "save", false, true);
			this.setTitle("GCMS Index Offer Letter [" + this.serviceParameters['className'] + "]");
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

					if(!domclass.contains(this.actionBar,"olELSEcmDialogPaneActionBar")){			
						domclass.add(this.actionBar,"olELSEcmDialogPaneActionBar");
					}

				}
			};

			var maximizing = (maximize) => {
				if(maximize != null){

					if(domclass.contains(this.actionBar,"olELSEcmDialogPaneActionBar")){			
						domclass.remove(this.actionBar, 'olELSEcmDialogPaneActionBar');
					}

				}
			};

			aspect.after(this._maximizeButton, 'onClick', maximizing, this._maximizeButton);
			aspect.after(this._restoreButton, 'onClick', restoring, this._restoreButton);
			domclass.add(this.actionBar,"olELSEcmDialogPaneActionBar");
		}, 
		
		
		upperCaseFields: function() {
			var allValTextbox = dojo.query('input#[id^="'+ this.id +'_"]');
			for(var i=0; i<allValTextbox.length; i++){
				this.toUpperCase(allValTextbox[i]);
			}
		},
		
		initializeDateFields: function() {
			var allDtInputWid = dojo.query('input#[id*="Date"][id^="'+ this.id +'_"]');
			for(var i=0; i<allDtInputWid.length; i++){
			  this.setDatePattern(registry.byId(allDtInputWid[i].id));
			}
		},
		
		initializeAmountFields: function() {
			this.isNumberKey(dijit.byId(this.id+"_offerringLetterAmount"));
		},
		
		cancel: function(dirtCheck) {
			
			var dlgMsg = new DialogMessages();
			if(dirtCheck) {
				if( thisForm.isFormDirty(thisForm.id+"_offeringLetterForm") || thisForm.jsonOLELSObject['DIRTYFLAG']){
					dlgMsg.addConfirmMessages("You have unsaved changes. Are you sure you want to quit?");
						thisForm.showMessage("Confirm",dlgMsg.confirmMessages(),true ,thisForm.performCancel, thisForm.noop);
				} else {
					thisForm.performCancel();
				}
			} else {
				thisForm.performCancel();
			}
			
		},
		
		performCancel: function() {
			
			if (thisForm.jsonOLELSObject['DIRTYFLAG']){
				
				thisForm.serviceParameters['frontEndAction'] = "rollbackIndexing";
				request.postPluginService("GilCnPlugin", "OfferingLetterELSService", 'application/x-www-form-urlencoded',{
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
		
		
		formatAmount: function() {
			
			var num = this.formatNumber(this.coaAmount.get('value'),2);
	        this.coaAmount.set('value',num);
		},
		
		save: function() {
			
			this.serviceParameters['frontEndAction'] = arguments.callee.nom;
			var dialogMessages = null;
			thisForm = this;

			try {

				if (this.offeringLetterForm.validate()) {
			    
			    thisForm.jsonOLELSObject['validationOfferLetterShouldBeIndexedIntoMyStampduty'] = false;
				thisForm.jsonOLELSObject['validationOfferLetterIncorrectStatusForCSignedOfferLetter'] = false;
				thisForm.jsonOLELSObject['validationOfferLetterIncorrectStatusForSignedOfferLetter']= false;
				thisForm.jsonOLELSObject['validationOfferletterDeactivatedInGCMS'] = false;
				thisForm.jsonOLELSObject['validationOfferLetterNotFoundInGCMS'] = false;
				thisForm.jsonOLELSObject['validationDuplicateOfferLetterEntered'] = false;
				thisForm.jsonOLELSObject['validationDuplicatedOfferLetterFromCM'] = false;
				thisForm.jsonOLELSObject['validationIsPendingPricingApprovalStatus'] = false;
				thisForm.jsonOLELSObject['validationOfferLetterIncorrectStatusForStampduty'] = false;
				thisForm.jsonOLELSObject['validationIsDocumentProperlyIndexed'] = true;
				
				thisForm.jsonOLELSObject['OFFERINGNUMBER'] 	= thisForm.offerringLetterNumber.get('value');
				thisForm.jsonOLELSObject['CURRENCY'] = thisForm.offeringLetterCurrency.get('value');
				thisForm.jsonOLELSObject['CUSTOMERNAME'] = thisForm.offeringLetterCustomerName.get('value');
				thisForm.jsonOLELSObject['CUSTOMERNUMBER'] = thisForm.offeringLetterCustomerNumber.get('value');
				thisForm.jsonOLELSObject['CUSTOMERSIGNATURE'] = thisForm.customerSignature.get('value');
				thisForm.jsonOLELSObject['CUSTOMERSIGNATUREDATE'] = thisForm.customerSignatureDate.get('displayedValue');
				thisForm.jsonOLELSObject['IBMSIGNATUREDATE']  = thisForm.ibmSignatureDate.get('displayedValue');
				thisForm.jsonOLELSObject['AMOUNT'] = thisForm.offerringLetterAmount.get('value');
					
				thisForm.serviceParameters['jsonDataObject']	= JSON.stringify(thisForm.jsonOLELSObject);
				
				this.validateOnSave(thisForm.performSave);
				
				console.log("OL ELS JSON being submitted: "+JSON.stringify(thisForm.jsonOLELSObject, null, 2));
	    			
			} else return false;
			
			} catch(e) {
				console.log('Error in method ' + arguments.callee.nom + ' - ' + e);
			}
			

		},
		
		setNonEditableFields: function() {
			
			this.offerringLetterNumber.set('disabled',true);
			this.offeringLetterCurrency.set('disabled',true);
			this.offeringLetterCustomerName.set('disabled',true);
			this.offeringLetterCustomerNumber.set('disabled',true);
			this.offerringLetterAmount.set('disabled',true);		
		},
		
		
		setFieldOptionValues: function() {
			
			try {
					var listOfOptionFields = thisForm.jsonOLELSObject.listOfFieldOptionValues;
					
					if(listOfOptionFields!=null && listOfOptionFields.length>0) {
					
						for (var i=0; i<listOfOptionFields.length; i++){
							
					        var visible = false;
					        var mandatory = false;
					        if (listOfOptionFields[i].option==this.MANDATORY){
					            visible = true;
					            mandatory = true;
					        }
					        if (listOfOptionFields[i].option==this.OPTIONAL) {
					            visible = true;
					            mandatory = false;
					        }
					        if (listOfOptionFields[i].option==this.HIDDEN) {
					            visible = false;
					            mandatory = false;
					        }
					        
				        
					        if (listOfOptionFields[i].isCountersigned && listOfOptionFields[i].field==this.CUSTOMERSIGNAME) {
					        	this.constructCustomerSignatureNameField(mandatory,visible);
					        }
					        
					        if (listOfOptionFields[i].isCountersigned && listOfOptionFields[i].field==this.CUSTOMERSIGDATE){
					        	this.constructCustomerSignatureDateField(mandatory,visible);
					        }
					        
					        if (listOfOptionFields[i].isCountersigned && listOfOptionFields[i].field==this.IBMSIGDATE) {
					        	this.constructIBMSignatureDateField(mandatory,visible);
					        }
					        
					        if (! listOfOptionFields[i].isCountersigned && listOfOptionFields[i].field==this.DEEMEDCUSTOMERSIGNAME){
					        	this.constructCustomerSignatureNameField(mandatory,visible);
					        }
					        
					        if (! listOfOptionFields[i].isCountersigned && listOfOptionFields[i].field==this.DEEMEDCUSTOMERSIGDATE) {
					        	this.constructCustomerSignatureDateField(mandatory,visible);
					        }
					        
					        if (! listOfOptionFields[i].isCountersigned && listOfOptionFields[i].field==this.DEEMEDIBMSIGDATE){
					        	this.constructIBMSignatureDateField(mandatory,visible);
					        }
					        
						}
						
					}
			        
				}catch(e){
					console.log('Error in method ' + arguments.callee.nom + ' - ' + e);
				}

			},

			constructCustomerSignatureNameField: function(isRequired, isVisible) {
	
		        if( !isVisible ){
		        	dojo.style(this.customerSignatureTr,"display","none");
		            dojo.style(this.customerSignature.domNode,"display","none"); 
		            dojo.style(dojo.byId(thisForm.id+"_customerSignatureLabel"), "display", "none");
		        } else {
		        	dojo.style(this.customerSignatureTr,"display","run-in");
		            dojo.style(this.customerSignature.domNode,"display","run-in");
		            dojo.style(dojo.byId(thisForm.id+"_customerSignatureLabel"), "display", "run-in");
		        }
		        
		        
		        if (isRequired) {
		        
					lang.getObject('customerSignature', false, this).validator = function() {
						
						if (	lang.getObject('customerSignature', false, thisForm).get("value") == null ||
								lang.getObject('customerSignature', false, thisForm).get("value").trim().length == 0  	)	{
	
							lang.getObject('customerSignature', false, thisForm).set("invalidMessage","Customer Signature Name is a required field.");					
							lang.getObject('customerSignature', false, thisForm).set("missingMessage","Customer Signature Name is a required field.");
							
							return false;
						}
						
						return true;		
					}
		        }
			},
			
			constructCustomerSignatureDateField: function(isRequired, isVisible) {
			
		        if( !isVisible ){
		        	dojo.style(this.customerSignatureDateTr,"display","none");
		            dojo.style(this.customerSignatureDate.domNode,"display","none"); 
		            dojo.style(dojo.byId(thisForm.id+"_customerSignatureDateLabel"), "display", "none");
		        } else {
		        	dojo.style(this.customerSignatureDateTr,"display","run-in");
		            dojo.style(this.customerSignatureDate.domNode,"display","run-in");
		            dojo.style(dojo.byId(thisForm.id+"_customerSignatureDateLabel"), "display", "run-in");
		        }
		        
		        if (isRequired) {
		        	
					lang.getObject('customerSignatureDate', false, this).validator = function() {
						
						if (	lang.getObject('customerSignatureDate', false, thisForm).get("displayedValue") == null ||
								lang.getObject('customerSignatureDate', false, thisForm).get("displayedValue").trim().length == 0  	)	{
							
							lang.getObject('customerSignatureDate', false, thisForm).set("invalidMessage","Customer Signature Date is a required field.");					
							lang.getObject('customerSignatureDate', false, thisForm).set("missingMessage","Customer Signature Date is a required field.");

							return false;
						}
						
					   // var parsedDate = locale.parse( thisForm.customerSignatureDate.get('displayedValue'), { locale:dojo.locale, selector: 'date' });
					    var parsedDate = thisForm.parseDate(domattr.get(dom.byId(thisForm.id+"_customerSignatureDate"), "value"), false);
					    
					    if(parsedDate == null){
					    	thisForm.customerSignatureDate.set("invalidMessage","Customer Signature Date must have format "+thisForm.gilDatePattern );
					    	return false; 
					    }
						if (parsedDate!=null) {
							if (date.difference(parsedDate,new Date()) > 300	)	{
								thisForm.customerSignatureDate.set("invalidMessage","Customer Signature Date must not be greater than 300 days old.");					
								return false;
							}
						}
						   
					    return true;
				}
	
		        } else {
		        	
					lang.getObject('customerSignatureDate', false, this).validator = function() {
						
						if (	lang.getObject('customerSignatureDate', false, thisForm).get("displayedValue") != null &&
								lang.getObject('customerSignatureDate', false, thisForm).get("displayedValue").trim().length > 0  	)	{
							
							lang.getObject('customerSignatureDate', false, thisForm).set("invalidMessage","Customer Signature Date is a required field.");					
							lang.getObject('customerSignatureDate', false, thisForm).set("missingMessage","Customer Signature Date is a required field.");

						   // var parsedDate = locale.parse( thisForm.customerSignatureDate.get('displayedValue'), { locale:dojo.locale, selector: 'date' });
						    var parsedDate = thisForm.parseDate(domattr.get(dom.byId(thisForm.id+"_customerSignatureDate"), "value"), false);
						    
						    if(parsedDate == null){
						    	thisForm.customerSignatureDate.set("invalidMessage","Customer Signature Date must have format "+thisForm.gilDatePattern );
						    	return false;
						    }
							if (parsedDate!=null) {
								if (date.difference(parsedDate,new Date()) > 300	)	{
									thisForm.customerSignatureDate.set("invalidMessage","Customer Signature Date must not be greater than 300 days old.");					
									return false;
								}
							}
							
						}
				
						

						   
					    return true;
		        	
		        }
					
		        
		        }
			
				    	
			},
			
			constructIBMSignatureDateField: function(isRequired, isVisible) {
				
	     		var thisForm = this;
	     		
		        if( !isVisible ){
		        	dojo.style(this.ibmSignatureDateTr,"display","none");
		            dojo.style(this.ibmSignatureDate.domNode,"display","none"); 
		            dojo.style(dojo.byId(thisForm.id+"_ibmSignatureDateLabel"), "display", "none");
		        } else {
		        	dojo.style(this.ibmSignatureDateTr,"display","run-in");
		            dojo.style(this.ibmSignatureDate.domNode,"display","run-in"); 
		            dojo.style(dojo.byId(thisForm.id+"_ibmSignatureDateLabel"), "display", "run-in");
		        }
		        
		        if (isRequired) {
		        	
					lang.getObject('ibmSignatureDate', false, this).validator = function() {
						
						if (	lang.getObject('ibmSignatureDate', false, thisForm).get("displayedValue") == null ||
								lang.getObject('ibmSignatureDate', false, thisForm).get("displayedValue").trim().length == 0  	)	{
							
							lang.getObject('ibmSignatureDate', false, thisForm).set("invalidMessage","IGF/IBM Signature Date is a required field.");					
							lang.getObject('ibmSignatureDate', false, thisForm).set("missingMessage","IGF/IBM Signature Date is a required field.");

							return false;
						}
						
					    //var parsedDate = locale.parse( thisForm.ibmSignatureDate.get('displayedValue'), {  locale:dojo.locale,  selector: 'date' });
					    var parsedDate = thisForm.parseDate(domattr.get(dom.byId(thisForm.id+"_ibmSignatureDate"), "value"), false);
					    
					    if(parsedDate == null) {
					    	thisForm.ibmSignatureDate.set("invalidMessage","IGF/IBM Signature Date must have format "+thisForm.gilDatePattern );
					    	return false;
					    }
						if (parsedDate!=null) {
							if (date.difference(parsedDate,new Date()) > 300	)	{
								thisForm.ibmSignatureDate.set("invalidMessage","IGF/IBM Signature Date must not be greater than 300 days old.");
								return false;
							}
						}
					    
					    return true;
				}
					
		        }  
		        
		        else {
		        	
					lang.getObject('ibmSignatureDate', false, this).validator = function() {
						
						if (	lang.getObject('ibmSignatureDate', false, thisForm).get("displayedValue") != null &&
								lang.getObject('ibmSignatureDate', false, thisForm).get("displayedValue").trim().length > 0  	)	{
							
							lang.getObject('ibmSignatureDate', false, thisForm).set("invalidMessage","IGF/IBM Signature Date is a required field.");					
							lang.getObject('ibmSignatureDate', false, thisForm).set("missingMessage","IGF/IBM Signature Date is a required field.");

						
					    //var parsedDate = locale.parse( thisForm.ibmSignatureDate.get('displayedValue'), {  locale:dojo.locale,  selector: 'date' });
					    var parsedDate = thisForm.parseDate(domattr.get(dom.byId(thisForm.id+"_ibmSignatureDate"), "value"), false);
					    
					    if(parsedDate == null) {
					    	thisForm.ibmSignatureDate.set("invalidMessage","IGF/IBM Signature Date must have format "+thisForm.gilDatePattern );
					    	return false;
					    }
						if (parsedDate!=null) {
							if (date.difference(parsedDate,new Date()) > 300	)	{
								thisForm.ibmSignatureDate.set("invalidMessage","IGF/IBM Signature Date must not be greater than 300 days old.");
								return false;
							}
						}
					    
					   
					    
						}
						
						 return true;
				}
					
		        }

			},
			
		validateOnSave: function(perfomSaveCallback) {
			
			this.serviceParameters['frontEndAction'] = arguments.callee.nom;
			var dialogMessages = new DialogMessages();
			
			try {
				
				request.postPluginService("GilCnPlugin", "OfferingLetterELSService", 'application/x-www-form-urlencoded', {
		    		requestBody : ioquery.objectToQuery(thisForm.serviceParameters),
		    		requestCompleteCallback : lang.hitch(this, function (response) {
		    			
		    			this.jsonOLELSObject	= JSON.parse(response.OLELSJsonString);
		    			
				    	if(!this.jsonOLELSObject.validationIsDocumentProperlyIndexed) {
				    		
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
					request.postPluginService("GilCnPlugin", "OfferingLetterELSService",'application/x-www-form-urlencoded', {
			    							requestBody : ioquery.objectToQuery(thisForm.serviceParameters),
								    		requestCompleteCallback : lang.hitch(this, function (response) {
								    			
								    			this.jsonOLELSObject = JSON.parse(response.OLELSJsonString);
								    			
												if(this.jsonOLELSObject.dialogErrorMsgs.length>0){
													var msgs=this.jsonOLELSObject.dialogErrorMsgs;
													for (var i in msgs) {
														dialogMessages.addErrorMessages(msgs[i]);
													}
													
													thisForm.showMessage("Error",dialogMessages.getAllMessages(),false,null,null);
												} else {
													thisForm.cancel(false);
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
		

		performIndex: function(dialogMessages) {
					
			var hasPromptMessages = false;
			
			if (thisForm.jsonOLELSObject['promptChangingOLNumber'] == true ) {
				
				hasPromptMessages = true;
			}
			
			if(dialogMessages.hasErrorMessages() ){
							
				thisForm.showMessage("Error",dialogMessages.getAllMessages(),false,null,null);
			    thisForm.cancel(false);
			
			}else if(hasPromptMessages){
								
					  thisForm.startPromptingOnIndex();
				}
			
			if(dialogMessages.hasInfoMessages()){
				
				thisForm.showMessage("Information",dialogMessages.infoMessages(),false,null,null);	
			}
			
		},
		
		
		
		
		show: function() {
			this.inherited("show", []);
		},
		
		isROF: function() {
			
			if (this.serviceParameters.className == 'ROF Signed Offer Letter') {
				return true;
			}  else {
				return false;
			}
		},
		
		getBackendErrorMsgs: function(dlgMsg) {
			
			if(this.jsonOLELSObject.dialogErrorMsgs.length>0){
				var msgs=this.jsonOLELSObject.dialogErrorMsgs;
				for (var i in msgs) {
					dlgMsg.addErrorMessages(msgs[i]);
				}
			} 

			return dlgMsg;
		},
		
		
		index: function() {
			
			try {

					this.serviceParameters['frontEndAction'] = arguments.callee.nom;
					cmValues = JSON.parse(this.serviceParameters['businessAttributes']);
					thisForm = this;
					var dialogMessages = new DialogMessages();

					request.postPluginService("GilCnPlugin", "OfferingLetterELSService",'application/x-www-form-urlencoded', {
			    							requestBody : ioquery.objectToQuery(thisForm.serviceParameters),
								    		requestCompleteCallback : lang.hitch(this, function (response) {
								    			
									    		this.jsonOLELSObject = JSON.parse(response.OLELSJsonString);
									    		dialogMessages = this.getBackendErrorMsgs(dialogMessages);
												
												if (this.isROF()) {								
									    			if (dialogMessages.hasErrorMessages()) {
									    				thisForm.showMessage("Error",dialogMessages.getAllMessages(),false,null,null);
									    				return;
									    			}
													if (this.jsonOLELSObject.isGCPSOLCreated) {
														dialogMessages.addInfoMessages(this.jsonOLELSObject.createOfferingLetterStatusMessage)
									    				thisForm.showMessage("Info",dialogMessages.infoMessages(),false,null,null);
									    			} else {
														dialogMessages.addErrorMessages(this.jsonOLELSObject.createOfferingLetterStatusMessage)
									    				thisForm.showMessage("Error",dialogMessages.getAllMessages(),false,null,null);
									    			}
													return
												}
									    		
								    			registry.byId(this.id+"_offerringLetterNumber").set('value',this.jsonOLELSObject.OFFERINGNUMBER);
								    			registry.byId(this.id+"_countryCode").set('value',this.jsonOLELSObject.COUNTRY);
								    			registry.byId(this.id+"_offeringLetterCurrency").set('value',this.jsonOLELSObject.CURRENCY );
								    			registry.byId(this.id+"_offeringLetterCustomerName").set('value',this.jsonOLELSObject.CUSTOMERNAME);		    			
								    			registry.byId(this.id+"_offeringLetterCustomerNumber").set('value',this.jsonOLELSObject.CUSTOMERNUMBER);
								    			registry.byId(this.id+"_customerSignature").set('value',thisForm.jsonOLELSObject.CUSTOMERSIGNATURE);
								    			registry.byId(this.id+"_customerSignatureDate").set('value',thisForm.parseDate(thisForm.jsonOLELSObject.CUSTOMERSIGNATUREDATE,false));
								    			registry.byId(this.id+"_ibmSignatureDate").set('value',thisForm.parseDate(thisForm.jsonOLELSObject.IBMSIGNATUREDATE,false));
								    			if(this.jsonOLELSObject.AMOUNT!=null && this.jsonOLELSObject.AMOUNT.trim()!="" ) {
								    				registry.byId(this.id+"_offerringLetterAmount").set('value',this.jsonOLELSObject.AMOUNT.replace(/,/g, '.') );
								    			}
								    			this.setFieldOptionValues();
								    			
								    			if (dialogMessages.hasErrorMessages()) {
								    				thisForm.showMessage("Error",dialogMessages.getAllMessages(),false,null,null);
								    			} else {
									    			this.validateOnIndex(this.performIndex);
								    			}
								    			
								    			this.show();
								    			this.serialFormClean=this.serializeForm(thisForm.id+"_offeringLetterForm");
								    		})
								});
					
			} catch(e) {
				console.log('Error in method ' + arguments.callee.nom + ' - ' + e);
			}
		},
		
		validateOnIndex : function(callback) {

			var dialogMessages = new DialogMessages();
			 
			 try {

			
				 if (this.jsonOLELSObject.validationOfferLetterShouldBeIndexedIntoMyStampduty) {
					 
					 dialogMessages.addErrorMessages("Offer Letter should be indexed into 'MY Stampduty OL' item type.");
				 }
				 
				 if (this.jsonOLELSObject.validationOfferLetterIncorrectStatusForCSignedOfferLetter) {
					 
					 dialogMessages.addErrorMessages("Offer Letter has incorrect status for Countersigned Offer Letter.");
				 }
				 
				 if (this.jsonOLELSObject.validationOfferLetterIncorrectStatusForSignedOfferLetter) {
					 
					 dialogMessages.addErrorMessages("Offer Letter has incorrect status for Signed Offer Letter.");
				 }
				 
				 if (this.jsonOLELSObject.validationOfferletterDeactivatedInGCMS) {
					 
					 dialogMessages.addErrorMessages("Offer Letter has been deactivated in GCMS.");
				 }
				 
				 if (this.jsonOLELSObject.validationOfferLetterNotFoundInGCMS) {
					 
					 dialogMessages.addErrorMessages("Offer Letter not found in GCMS.");
				 }
				 
				 if (this.jsonOLELSObject.validationDuplicateOfferLetterEntered) {
					 
					 dialogMessages.addErrorMessages("Duplicate Offer Letter Number entered.");
				 }
				 
				 if (this.jsonOLELSObject.validationDuplicatedOfferLetterFromCM) {
					 
					 dialogMessages.addErrorMessages("Duplicate Offer Letter retrieved from Content Manager.");
				 }
				 				 
				 if (this.jsonOLELSObject.validationOfferLetterIncorrectStatusForStampduty) {
					 
					 dialogMessages.addErrorMessages("Offer Letter has incorrect status for Stampduty Offer Letter.");
				 }
				 
				if(this.jsonOLELSObject.OLDOFFERINGNUMBER != this.jsonOLELSObject.OFFERINGNUMBER) {
						
					this.jsonOLELSObject['promptChangingOLNumber'] = true; 
				}
			                
				 callback(dialogMessages);    
				 
			 } catch(e) {
					console.log('Error in method ' + arguments.callee.nom + ' - ' + e);
				}
		},
		
		
		
		startPromptingOnIndex: function() {
			
			var dialogMsgs = new DialogMessages();
			
			if(this.jsonOLELSObject.promptChangingOLNumber) {
				dialogMsgs.addConfirmMessages("Changing Offer Letter Number. Are you sure?");
				this.showMessage("Confirm",dialogMsgs.confirmMessages(),true ,this.promptYesForChangingOLNumber, this.promptNoForChangingOLNumber);
			} 
			
		},
		
		promptYesForChangingOLNumber: function() {

			thisForm.jsonOLELSObject.DIRTYFLAG = true;
		},
		
		promptNoForChangingOLNumber: function() {
			
			thisForm.jsonOLELSObject.OFFERINGNUMBER = thisForm.jsonOLELSObject.OLDOFFERINGNUMBER;
			registry.byId(thisForm.id+"_contractNumber").set('value', thisForm.jsonOLELSObject.OLDOFFERINGNUMBER);

		},
		

		setCustomizedValidation : function() {
			
			lang.getObject('offerringLetterNumber', false, this).validator = function() {
				
				if (lang.getObject('offerringLetterNumber', false, thisForm).get("value") == null ||
						lang.getObject('offerringLetterNumber', false, thisForm).get("value").trim().length == 0  	)	{

					lang.getObject('offerringLetterNumber', false, thisForm).set("invalidMessage","Offering Letter Number is a required field.");					
					lang.getObject('offerringLetterNumber', false, thisForm).set("missingMessage","Offering Letter Number is a required field.");
					
					return false;
				}
				
				return true;		
			}
			
		    lang.getObject('offerringLetterAmount', false, this).validator = function() {
		    	
				var decimalRegex = ValidationConstants("decimalRegex");

					if(!decimalRegex.test(thisForm.offerringLetterAmount.get('value')))	{

						thisForm.invoiceAmountTax.set("Offering Letter Amount is a required field.");					
						return false;
					}
		    	return true;	
		    }

			lang.getObject('offeringLetterCustomerName', false, this).validator = function() {
				
				if (lang.getObject('offeringLetterCustomerName', false, thisForm).get("value") == null ||
						lang.getObject('offeringLetterCustomerName', false, thisForm).get("value").trim().length == 0  	)	{

					lang.getObject('offeringLetterCustomerName', false, thisForm).set("invalidMessage","Customer Name is a required field.");					
					lang.getObject('offeringLetterCustomerName', false, thisForm).set("missingMessage","Customer Name is a required field.");
					
					return false;
				}
				
				return true;		
			}

			
		},

		rollbackIndexing : function() {

			thisForm.serviceParameters['frontEndAction'] = arguments.callee.nom;
			request.postPluginService("GilCnPlugin", "OfferingLetterELSService", 'application/x-www-form-urlencoded', {
	    		requestBody : ioquery.objectToQuery(thisForm.serviceParameters),
	    		requestCompleteCallback : lang.hitch(this, function (response) {
	    			
	    			console.log('Index rolled back successfully.');

	    		})
			});
		},
		
		
		rollbackCountryIndexing : function() {

			thisForm.serviceParameters['frontEndAction'] = arguments.callee.nom;
			request.postPluginService("GilCnPlugin", "OfferingLetterELSService", 'application/x-www-form-urlencoded', {
	    		requestBody : ioquery.objectToQuery(thisForm.serviceParameters),
	    		backgroundRequest: true,
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
