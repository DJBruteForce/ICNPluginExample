define(["dojo/_base/lang",
        "ecm/model/Action", 
        "ecm/model/Request", 
        "dojo/_base/declare", 
		"ecm/widget/dialog/BaseDialog",
		"dojo/json",
		"gilCnPluginDojo/util/JsonUtils",
		"gilCnPluginDojo/util/MathJs",
		"dojo/text!./templates/ContractDialog.html",
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
	    "gilCnPluginDojo/AptsDialog",
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
	    "dojox/grid/DataGrid",
	    "dojo/data/ItemFileWriteStore",
	    "dojo/_base/event",
	    "dojo/_base/array",
	    "gilCnPluginDojo/InvoiceSearchDialog",
	    "dojo/io-query",
	    "gilCnPluginDojo/GILDialog",
	    "dojo/dom-class",
	    "dojo/aspect" 
	    
	],
	
	function(lang, action, request,declare,	BaseDialog,	json,JsonUtils, MathJs, template, 
			array, dom,domReady,form,date,locale,domStyle,registry,query,construct,DateTextBox,
			parser,domattr,AptsDialog,ValidationConstants,ConfirmDialog,NonModalConfirmDialog,i18n,
			number,Button,ConfirmationDialog,Builder,DialogMessages,ECMConfirmationDialog,Tooltip,stamp ,
			focus,connect, event,DataGrid,ItemFileWriteStore,event,array,InvoiceSearchDialog,ioquery,
			GILDialog,domclass,aspect) {

		
		var ContractDialog = declare("gilCnPluginDojo.ContractDialog",[BaseDialog,GILDialog,AptsDialog], {
		
		contentString: template,
		widgetsInTemplate: true,
		jsonInvoiceObject : null,
		cmValues:null,
		localeBundle:null,
		invoiceListJsonData:null,
		invoiceListDataStore:null,
		invoiceListGrid:null,
		originalInvoicesListString:null,
		//get the array list selected from the invoice search screen
		invoiceSearchSelectedList: [],
		addCmInvoiceBtnId:null,
		saveBtnId: null,
		addGaptsInvoiceBtnId:null,
		
		postCreate: function() {
		
			this.inherited(arguments);
			this.setSize(800, 330);
			this.setResizable(false);
			this.setMaximized(false);
			this.addButton("Add GAPTS Invoices", "openInvoiceSearchDialog", false, true,"addGaptsInvoice");
			this.addButton("Add CM Invoice", "addCMInvoice", false, true, "addCMInvoice");
			this.addButton("Cancel", "cancel", false, true,"cancel");
			this.addButton("Save", "save", false, true,"save");
			this.setTitle("APTS Index Contract [" + this.serviceParameters['className'] + "]");
			this.setWidgetsInitialization();
			this.setCustomizedValidation();
			this.initializeDateFields();
			this.initializeAmountFields();
			this.upperCaseFields();
			this.setLayout();
		},
		
		setWidgetsInitialization: function() {
		    
			dojo.style(this.cancelButton.domNode,"display","none");       
			this.addCmInvoiceBtnId = registry.byId(dojo.query('[id^="addCMInvoice"]')[0].id);
			this.saveBtnId = registry.byId(dojo.query('[id^="save"]')[0].id);
			this.addGaptsInvoiceBtnId = registry.byId(dojo.query('[id^="addGaptsInvoice"]')[0].id);
		},
		
		setLayout:function(){
			
			var restoring = (restore) => {
				if(restore != null){

					if(!domclass.contains(this.actionBar,"contractEcmDialogPaneActionBar")){			
						domclass.add(this.actionBar,"contractEcmDialogPaneActionBar");
					}

				}
			};

			var maximizing = (maximize) => {
				if(maximize != null){

					if(domclass.contains(this.actionBar,"contractEcmDialogPaneActionBar")){			
						domclass.remove(this.actionBar, 'contractEcmDialogPaneActionBar');
					}

				}
			};

			aspect.after(this._maximizeButton, 'onClick', maximizing, this._maximizeButton);
			aspect.after(this._restoreButton, 'onClick', restoring, this._restoreButton);
			domclass.add(this.actionBar,"contractEcmDialogPaneActionBar");
		}, 
		
		
		
		upperCaseFields: function() {
			var allValTextbox = dojo.query('input#[id^="'+ this.id +'_"]');
			for(var i=0; i<allValTextbox.length; i++){
				this.toUpperCase(allValTextbox[i]);
			}
		},
		
		initializeDateFields: function() {
			this.allFormInputWid = dojo.query('input#[id*="Date"][id^="'+ this.id +'_"]');
			for(var i=0; i<this.allFormInputWid.length; i++){
			  this.setDatePattern(registry.byId(this.allFormInputWid[i].id));
			}
		},
		
		initializeAmountFields: function() {
			  this.isNumberKey(dijit.byId(this.id+"_contractAmount"));
		},
		
		createInvoiceListData: function(jsonInvoicesList) {
			
			try {
					var invoiceListObject;
					var arr = [];
					
			    	for (var i in jsonInvoicesList) {
				    	    
			    	    arr.push({
			    	        invoiceNumber: jsonInvoicesList[i]['INVOICENUMBER'],
			    	        invoiceDate: jsonInvoicesList[i]['INVOICEDATE'],
			    	        objectId: jsonInvoicesList[i]['OBJECTID'],
			    	        fromDatabase:true
			    	    
			    	    });
			    	}
			} catch(e) {
				console.log('Error in method ' + arguments.callee.nom + ' - ' + e);
			}	 
	    	 return arr;
		},
		
		initializeInvoicesGrid: function(jsonInvoicesList) {
		
			try {
		    
					var data = {  items: [] };
				    this.invoiceListJsonData = this.createInvoiceListData(jsonInvoicesList);

				    for(var i = 0, l = this.invoiceListJsonData.length; i < l; i++) {
				    	data.items.push(lang.mixin({ id: i+1 }, this.invoiceListJsonData[i%l]));
				    }
				    
				    this.invoiceListDataStore = new ItemFileWriteStore({data: data});
				    var layout = [[
							    	      {'name': 'Invoice Number', 'field': 'invoiceNumber', 	'width': '110px'},
							    	      {'name': 'Invoice Date', 	 'field': 'invoiceDate', 	'width': '100px'},
							    	      {'name': 'Object ID', 	 'field': 'objectId', 		'width': '95px', hidden: true},
							    	      {'name': 'Country', 		 'field': 'country', 		'width': 'auto', hidden: true},	
							    	      {'name': 'Contract Number','field': 'contractNumber', 'width': 'auto', hidden: true},	
							    	      {'name': 'fromDatabase',	 'field': 'fromDatabase', 	'width': 'auto', hidden: true},
		
							    	     ]];
		
				   this.invoiceListGrid = new DataGrid({
													    store: this.invoiceListDataStore,
													    selectionMode:'none',
													    structure: layout,
													    rowSelector: '1px',
													    autoHeight: true,
													    id: this.id+"_invoiceListGrid",
				    	        						});
					
				    	    this.invoiceListGrid.placeAt("idInvoiceList");
				    	    this.invoiceListGrid.startup();   	    
			} catch(e) {
				console.log('Error in method ' + arguments.callee.nom + ' - ' + e);
			}
		},
		
		performCancel: function() {
			
			if (thisForm.jsonContractObject['DIRTYFLAG']){
				
				thisForm.setRollbackValues();
				thisForm.serviceParameters['jsonDataObject']	= JSON.stringify(thisForm.jsonContractObject);
				thisForm.serviceParameters['frontEndAction'] = "rollbackIndexing";
				request.postPluginService("GilCnPlugin", "ContractService", 'application/x-www-form-urlencoded',{
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
		
		
		cancel: function(dirtCheck) {
			
			var dlgMsg = new DialogMessages();
			if(dirtCheck) {
				if( thisForm.isFormDirty(thisForm.id+"_contractForm") || thisForm.jsonContractObject['DIRTYFLAG']){
					dlgMsg.addConfirmMessages("You have unsaved changes. Are you sure you want to quit?");
						thisForm.showMessage("Confirm",dlgMsg.confirmMessages(),true ,thisForm.performCancel, thisForm.noop);
				} else {
					thisForm.performCancel();
				}
			} else {
				thisForm.performCancel();
			}

		},
		
		
		formatAmount: function() {
			var num = this.formatNumber(this.contractAmount.get('value'),2);
	        this.contractAmount.set('value',num);
		},
		

		addCMInvoice: function() {
			
			if (this.isDoubleClick(this.addCmInvoiceBtnId)) return;
			console.log(arguments.callee.nom + ' was clicked:' + this.actionClick + ' time(s).');
			
			try {
					var dialogMessages = new DialogMessages();
					var invoiceToAdd = registry.byId(thisForm.id+"_invoiceNumber").get('value').trim();
					var invoiceDateToAdd = registry.byId(thisForm.id+"_invoiceDate").get('value');
					var existsInDataStore = false;
					var store = this.invoiceListDataStore;
					
					if((invoiceToAdd==null || invoiceToAdd.trim().length == 0) || (invoiceDateToAdd==null || invoiceDateToAdd.length == 0)){
						dialogMessages.addErrorMessages( "Invoice Number and Invoice Date are required.");						
						thisForm.showMessage("Error",dialogMessages.getAllMessages(),false,null,null);
						thisForm.enableClick(thisForm.addCmInvoiceBtnId);
						return;
					}
					
					 store.fetch({   
					               onItem: function(item) {
					            	   var invoiceNumAux = store.getValue( item, 'invoiceNumber');
					            	   if(invoiceNumAux == null) invoiceNumAux = '';
					            	   if(invoiceNumAux.toUpperCase() == invoiceToAdd.toUpperCase() ) {	 existsInDataStore = true; }
					               }
					 		});
					
					if (thisForm.jsonContractObject.invoicesList.filter(function(e) {
						
						
						if(	e.INVOICENUMBER==null) e.INVOICENUMBER ='';		
							return (e.INVOICENUMBER.toUpperCase()) == (invoiceToAdd.toUpperCase()) 
							}).length > 0 || existsInDataStore
						) 	{
						
			         		
							dialogMessages.addErrorMessages( "Invoice is already associated to contract.");
							thisForm.showMessage("Error",dialogMessages.getAllMessages(),false,null,null);
							thisForm.enableClick(thisForm.addCmInvoiceBtnId);;
		
						} else {
		
								var newInvoiceListItem = {
														  invoiceNumber:  registry.byId(this.id+"_invoiceNumber").get('value')	, 
														  invoiceDate: 	  registry.byId(this.id+"_invoiceDate").get('displayedValue'),
														  fromDatabase:	  false,
														};
								
						        this.invoiceListDataStore.newItem(newInvoiceListItem);
							}
					
					registry.byId(this.id+"_invoiceNumber").set('value',null);
					registry.byId(this.id+"_invoiceDate").set('value',null);
					thisForm.enableClick(thisForm.addCmInvoiceBtnId);;
			
			} catch(e) {
				console.log('Error in method ' + arguments.callee.nom + ' - ' + e);
			}
		},
		
		
		removeInvoicesFromList: function() {
			
			try {
			        var items = this.invoiceListGrid.selection.getSelected();
			        if(items.length) {
			            dojo.forEach(items, function(selectedItem){
			                if(selectedItem !== null){  
			                	thisForm.invoiceListDataStore.deleteItem(selectedItem);
			                } 
			            })
			        }
	        
			} catch(e) {
				console.log('Error in method ' + arguments.callee.nom + ' - ' + e);
			}
		},

		
		save: function() {
			
			if (this.isDoubleClick(this.saveBtnId)) return;
			console.log(arguments.callee.nom + ' was clicked:' + this.actionClick + ' time(s).');
			
			this.serviceParameters['frontEndAction'] = arguments.callee.nom;
			var dialogMessages = null;
			thisForm = this;

			try {

					if (!this.contractForm.validate()) {
						this.enableClick(thisForm.saveBtnId);
			            return false;
					}
			    
					thisForm.jsonContractObject['validationPaymentDone'] 				= false;
					thisForm.jsonContractObject['validationPartiallyPaid'] 				= false;
					thisForm.jsonContractObject['validationIsDuplicateContractNumber'] = false;
					thisForm.jsonContractObject['validationIsOfferLetterInCM'] 		   = true;
					thisForm.jsonContractObject['validationIsDocumentIndexedProperly'] = true;
					
					thisForm.jsonContractObject['CONTRACTNUMBER'] 			= thisForm.contractNumber.get('value');
					thisForm.jsonContractObject['AMOUNT'] 					= thisForm.contractAmount.get('value');
					thisForm.jsonContractObject['CURRENCY'] 				= thisForm.contractCurrency.get('value');
					thisForm.jsonContractObject['OFFERINGLETTER'] 			= thisForm.contractOfferingLetterNumber.get('value');
					thisForm.jsonContractObject['CUSTOMERNAME'] 			= thisForm.contractCustomerName.get('value');
					thisForm.jsonContractObject['CUSTOMERNUMBER'] 			= thisForm.contractCustomerNumber.get('value');
					
					thisForm.jsonContractObject.invoicesList = JSON.parse(this.originalInvoicesListString)
					
					var store = registry.byId(thisForm.id+"_invoiceListGrid").store			
					store.fetch( {
					               onItem: function(item) {
					            	   
					            	   var invoiceNumAux = store.getValue( item, 'invoiceNumber');
					            	   if(!store.getValue( item, 'fromDatabase' )) {
					            		   
						            	   thisForm.jsonContractObject.invoicesList.push({
						            		   INVOICENUMBER: 	store.getValue( item, 'invoiceNumber' ),
						            		   INVOICEDATE: 	store.getValue( item, 'invoiceDate' ),
						            		   OBJECTID: 		store.getValue( item, 'objectId' ),
						            		   CONTRACTNUMBER: 	store.getValue( item, 'contractNumber' ),
						            		   COUNTRY: 		store.getValue( item, 'country' ),
						            	   });
					            	   }

					               }
					});
					
							
					thisForm.serviceParameters['jsonDataObject']	= JSON.stringify(thisForm.jsonContractObject);
					dialogMessages = this.validateOnSave(thisForm.performSave);
					console.log("Contract JSON being submitted: "+JSON.stringify(thisForm.jsonContractObject, null, 2));
					
			} catch(e) {
				console.log('Error in method ' + arguments.callee.nom + ' - ' + e);
			}
		},
		
		validateOnSave: function(perfomSaveCallback) {
			
			this.serviceParameters['frontEndAction'] = arguments.callee.nom;
			var dialogMessages = new DialogMessages();
			
			
			try {
				
				request.postPluginService("GilCnPlugin", "ContractService",'application/x-www-form-urlencoded', {
					requestBody : ioquery.objectToQuery(this.serviceParameters),
				    requestCompleteCallback : lang.hitch(this, function (response) {
				    	
				    	this.jsonContractObject	= JSON.parse(response.contractJsonString);
				        
				    	if (this.jsonContractObject.validationIsDuplicateContractNumber) {
				    		dialogMessages.addErrorMessages("Duplicate contract number entered.");
				    		focus.focus(dom.byId(this.id+"_contractNumber"));
				    		perfomSaveCallback(dialogMessages);
				    		return false;
				    	}
				        
				    	if (!this.jsonContractObject.validationIsOfferLetterInCM) {
				    		dialogMessages.addErrorMessages("Offering Letter Number does not exist in Content Manager.");
				    		focus.focus(dom.byId(this.id+"_contractOfferingLetterNumber"));
				    		perfomSaveCallback(dialogMessages);
				    		return false;
				    	}
				        
				    	if(!this.jsonContractObject.validationIsDocumentIndexedProperly) {
				    		dialogMessages.addErrorMessages("Document did not index correctly.");
				    		perfomSaveCallback(dialogMessages);
				    		return false;
				    	}
				    	
				       	if(this.jsonContractObject.ZEROBAL == 'Y') {
				       		dialogMessages.addErrorMessages("Contract has a balance of 0.00 and cannot be re-indexed");
				    		perfomSaveCallback(dialogMessages);
				    		return false;
				       	}
				       	//******Restoring original list of invoices start******
				       	var store = registry.byId(thisForm.id+"_invoiceListGrid").store	
				     	var auxInvoicesList = null;
				       	this.jsonContractObject.invoicesList.forEach( function (contractInvoice)	{
				       		
				       	contractInvoice.errors.forEach( function (errors){
					       		store.fetch({query:{invoiceNumber: contractInvoice.INVOICENUMBER}, onComplete: function (items) {
								    for(i = 0; i < items.length; i++){
								      var item = items[i];
								      store.deleteItem(item);
								    }
								}});
					       			dialogMessages.addErrorMessages(errors);
					       	});
				       		
				       		auxInvoicesList = thisForm.jsonContractObject.invoicesList.filter(function(c) {
					       	    return c.INVOICENUMBER !== contractInvoice.INVOICENUMBER;
					       	});
				       		
				       	});
				      //******Restoring original list of invoices end ******
				       	
				       	thisForm.jsonContractObject.invoicesList = auxInvoicesList;

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
					request.postPluginService("GilCnPlugin", "ContractService",'application/x-www-form-urlencoded', {
											requestBody : ioquery.objectToQuery(thisForm.serviceParameters),
								    		requestCompleteCallback : lang.hitch(this, function (response) {
								    			
								    			var dlgResponseMsgs = new DialogMessages();
								    			this.jsonContractObject	= JSON.parse(response.contractJsonString);
								    			if(this.jsonContractObject.dialogErrorMsgs.length!=0) {
													var msgs=this.jsonContractObject.dialogErrorMsgs;
													for (var i in msgs) {
														dlgResponseMsgs.addErrorMessages(msgs[i]);
													}												
								    			} else {
									    			if (!this.jsonContractObject.validationIsContractFolderCreated) {
									    				dlgResponseMsgs.addErrorMessages("Error Creating Contract Folder");	
									    			}
								    			}
								    			if (dlgResponseMsgs.hasErrorMessages()) {
								    				thisForm.showMessage("Error",dlgResponseMsgs.getAllMessages(),false,null,null);
								    			}
								    			

								    		})
								});
						
				} else {
					
					thisForm.enableClick(thisForm.saveBtnId);
	    			thisForm.showMessage("Validation",dialogMessages.getAllMessages(),false,null,null);
	    			return false;
		              
				}

					thisForm.cancel(false);
				
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
					
					request.postPluginService("GilCnPlugin", "ContractService", 'application/x-www-form-urlencoded', {
											requestBody : ioquery.objectToQuery(this.serviceParameters),
								    		requestCompleteCallback : lang.hitch(this, function (response) {
								    			this.jsonContractObject	= JSON.parse(response.contractJsonString);
								    			
												if(this.jsonContractObject.dialogErrorMsgs.length>0){
													var msgs=this.jsonContractObject.dialogErrorMsgs;
													for (var i in msgs) {
														dialogMessages.addErrorMessages(msgs[i]);
													}
												} 
							    			
								    			if (dialogMessages.hasErrorMessages()) {
								    				this.showMessage("Error",dialogMessages.getAllMessages(),false,null,null);
								    			}
								    				
								    			
								    			this.originalInvoicesListString  =  JSON.stringify(this.jsonContractObject.invoicesList);
								    			this.initializeInvoicesGrid(this.jsonContractObject.invoicesList);
								    			registry.byId(this.id+"_contractNumber").set('value',this.jsonContractObject.CONTRACTNUMBER );
								    			if(this.jsonContractObject.AMOUNT!=null && this.jsonContractObject.AMOUNT.trim()!="" ){
								    				registry.byId(this.id+"_contractAmount").set('value',this.jsonContractObject.AMOUNT.replace(/,/g, '.') );
								    			}
								    			registry.byId(this.id+"_contractOfferingLetterNumber").set('value',this.jsonContractObject.OFFERINGLETTER );
								    			registry.byId(this.id+"_contractCustomerName").set('value',this.jsonContractObject.CUSTOMERNAME );
								    			registry.byId(this.id+"_contractCustomerNumber").set('value',this.jsonContractObject.CUSTOMERNUMBER );
								    			registry.byId(this.id+"_contractCurrency").set('options',this.jsonContractObject.currencyCodeSelectList );
								    			registry.byId(this.id+"_contractCurrency").set('value',jsonUtils.getDefaultOrSelected(this.jsonContractObject.currencyCodeSelectList,"value", true));
								    			
								    			this.show();
								    			
									    		if(this.jsonContractObject.DOCUMENTMODE == 3) {
									    			this.validateOnIndex(this.performIndex);
									    		}
									    		
									    		this.serialFormClean=this.serializeForm(thisForm.id+"_contractForm");  
								    			
								    		})
								});
					
			} catch(e) {
				console.log('Error in method ' + arguments.callee.nom + ' - ' + e);
			}
		},
		
		setRollbackValues: function() {
			
			if(this.serviceParameters.oldItemAttributes.Contract_Date!=null && this.serviceParameters.oldItemAttributes.Contract_Date!="") {
				
				var parsedDt1 = stamp.fromISOString(this.serviceParameters.oldItemAttributes.Contract_Date).toGMTString();
				this.jsonContractObject.OLDCONTRACTDATE = locale.format(new Date(parsedDt1),{ selector: "date", datePattern : this.gilDatePattern  });
			} else {
				this.jsonContractObject.OLDCONTRACTDATE = "";
			}
			if(this.serviceParameters.newItemAttributes.Contract_Date!=null && this.serviceParameters.newItemAttributes.Contract_Date!="") {
				
				var parsedDt2 = stamp.fromISOString(this.serviceParameters.newItemAttributes.Contract_Date).toGMTString();
				this.jsonContractObject.CONTRACTDATE = locale.format(new Date(parsedDt2),{ selector: "date", datePattern : this.gilDatePattern  });
			}else {
				this.jsonContractObject.CONTRACTDATE = "";
			}
			
			this.jsonContractObject.OLDOFFERINGLETTER  = this.serviceParameters.oldItemAttributes.Offering_L_0002;
			this.jsonContractObject.OFFERINGLETTER = this.serviceParameters.newItemAttributes.Offering_L_0002;
			this.jsonContractObject.CUSTOMERNAME = this.serviceParameters.newItemAttributes.Customer_Name;
			this.jsonContractObject.OLDCUSTOMERNAME = this.serviceParameters.oldItemAttributes.Customer_Name;
			this.jsonContractObject.OLDCONTRACTNUMBER = this.serviceParameters.oldItemAttributes.Contract_Number;
			this.jsonContractObject.CONTRACTNUMBER = this.serviceParameters.oldItemAttributes.Contract_Number;
			
		},
		
		
		startPromptingOnIndex: function() {

			 var dialogMsgs = new DialogMessages();
			 
			if(this.jsonContractObject.promptChangingContractNumber) {
				dialogMsgs.addConfirmMessages("Changing Contract Number. Are you sure?");
				this.showMessage("Confirm",dialogMsgs.confirmMessages(),true ,this.promptYesForChangingContractNumber, this.promptNoForChangingContractNumber);
			} 
			
		},
		
		promptYesForChangingContractNumber: function() {

			thisForm.jsonContractObject.DIRTYFLAG = true;
			focus.focus(dom.byId(thisForm.id+"_contractNumber"));
		},
		
		promptNoForChangingContractNumber: function() {
			
			thisForm.jsonContractObject.CONTRACTNUMBER = thisForm.jsonContractObject.OLDCONTRACTNUMBER;
			registry.byId(thisForm.id+"_contractNumber").set('value', thisForm.jsonContractObject.OLDCONTRACTNUMBER);

		},
		
		performIndex: function(dialogMessages) {
			
			var hasPromptMessages = false;
			if (thisForm.jsonContractObject['promptChangingContractNumber'] == true ) {	
				hasPromptMessages = true;
			}
			
			if(dialogMessages.hasErrorMessages()) {		
				thisForm.showMessage("Error",dialogMessages.getAllMessages(),false,null,null);
			    thisForm.cancel(false);
			}else if(hasPromptMessages) {		
					  thisForm.startPromptingOnIndex();
					}
			
			if(dialogMessages.hasWarningMessages()){
				thisForm.showMessage("Warning",dialogMessages.warningMessages(),false,null,null);	
			}
			
		},
		
		isFullyPaid : function() {
			
			var balance = this.toNumber(this.jsonContractObject.BALANCE);
			return (MathJs.compare(balance,0.00) ==0 );
		},
		
		isPartiallyPaid : function() {
			
            var amount = this.toNumber(this.jsonContractObject.AMOUNT);
            var balance = this.toNumber(this.jsonContractObject.BALANCE);
            return (MathJs.compare(amount,balance) > 0 && MathJs.compare(balance,0.00)>0);
		},
		
		isContractNumberChanging : function() {
			
			return (registry.byId(this.id+"_contractNumber").get('value').trim()!=this.jsonContractObject.OLDCONTRACTNUMBER.trim());
		},
		
		isCustomerNameChanging : function() {
			
			return (registry.byId(this.id+"_contractCustomerName").get('value').trim()!=this.jsonContractObject.OLDCUSTOMERNAME.trim() );
		},
	
		isCountryChanging : function() {
		
		 return (this.jsonContractObject.OLDCOUNTRY != this.serviceParameters['country'])
		},
		
		
		isContractDateChanging : function() {

			if (this.serviceParameters.oldItemAttributes.Contract_Date!=null) {
				
				if(this.serviceParameters.oldItemAttributes.Contract_Date.trim()!=this.serviceParameters.newItemAttributes.Contract_Date.trim()){
					return true;
				}
				else {
					return false;
				}
			}
			
			return false;

		},
		
		isOfferLetterChanging : function() {

			if (this.serviceParameters.oldItemAttributes.Offering_L_0002!=null) {
				
				if(this.serviceParameters.oldItemAttributes.Offering_L_0002.trim()!=this.serviceParameters.newItemAttributes.Offering_L_0002.trim()){
					return true;
				}
				else {
					return false;
				}
			}
			
			return false;

		},
		
		
		validateOnIndex : function(callback) {
			
			var dialogMessages = new DialogMessages();
			 
			 try {
				 
				 	this.jsonContractObject['promptChangingContractNumber'] = false;
	                var isFullyPaid =  this.isFullyPaid();
	                var isPartiallyPaid = this.isPartiallyPaid();
				 
	               /**
	                * Defect : 1774899
	                * Prevent the user from change his name for partially and fully paid contract
	                */
	                if (this.isContractNumberChanging() || 
	                	this.isCustomerNameChanging() || 
	                	this.isContractDateChanging() || 
	                	this.isOfferLetterChanging())  
	                {
	                	
	                	this.jsonContractObject['DIRTYFLAG'] = true;
	                	
	                	if(isPartiallyPaid){
		             		dialogMessages.addErrorMessages( "Contract has a payment against it and cannot be updated.");
		             		this.rollbackIndexing();
		             		callback(dialogMessages);
		             		return;
	                	}
	                	
	                	if(isFullyPaid ) {
		             		dialogMessages.addErrorMessages( "Contract has a balance of 0.00 and cannot be re-indexed.");
		             		this.rollbackIndexing();
		             		callback(dialogMessages);
		             		return;
	                	} else {
	                			this.jsonContractObject['promptChangingContractNumber'] = this.isContractNumberChanging();
	                	} 
	                } else {
	                	
	                	if(isPartiallyPaid){
		             		dialogMessages.addWarningMessages( "Contract has a payment against it and cannot be updated.");
	                	}
	                	
	                	if(isFullyPaid ) {
		             		dialogMessages.addWarningMessages( "Contract has a balance of 0.00 and cannot be re-indexed.");
	                	}
	                	
	                }
	                
	                if(this.isCountryChanging())
	                {
	                		if(isFullyPaid) {
	                			this.jsonContractObject['INDEXCLASS'] = "Contract " + this.jsonContractObject.OLDCOUNTRY;
					           dialogMessages.addErrorMessages( "Contract has a balance of 0.00 and cannot be re-indexed.");
			                   this.rollbackCountryIndexing();
	                		} else {
	                			this.jsonContractObject['COUNTRY']  = this.serviceParameters['country'];
	                		}
	                }
	                
	                if(isPartiallyPaid || isFullyPaid){
	                	this.setFieldsNotEditable(true);
	                	this.setInvoiceFieldsNotEditable(true);
	                }
	                
	                if(this.jsonContractObject.isWorkAcess){
	                	this.setAmountFieldNotEditable(true);
	                }

	                this.jsonContractObject['validationPaymentDone'] = isFullyPaid;
	                this.jsonContractObject['validationPartiallyPaid'] = isPartiallyPaid;
	                
	                callback(dialogMessages);  
				 
			 } catch(e) {
					console.log('Error in method ' + arguments.callee.nom + ' - ' + e);
				}
		},
		

		setCustomizedValidation : function() {
			
			
			lang.getObject('contractNumber', false, this).validator = function() {
				
				if (lang.getObject('contractNumber', false, thisForm).get("value") == null ||
						lang.getObject('contractNumber', false, thisForm).get("value").trim().length == 0  	)	{
					lang.getObject('contractNumber', false, thisForm).set("invalidMessage","Contract Number is a required field.");					
					lang.getObject('contractNumber', false, thisForm).set("missingMessage","Contract Number is a required field.");
					return false;
				}
				return true;		
			}
			
			
			lang.getObject('contractCustomerName', false, this).validator = function() {
				
				if (lang.getObject('contractCustomerName', false, thisForm).get("value") == null ||
						lang.getObject('contractCustomerName', false, thisForm).get("value").trim().length == 0  	)	{
					lang.getObject('contractCustomerName', false, thisForm).set("invalidMessage","Customer Name is a required field.");					
					lang.getObject('contractCustomerName', false, thisForm).set("missingMessage","Customer Name is a required field.");
					return false;
				}
				
				return true;		
			}
			

		    lang.getObject('contractAmount', false, this).validator = function() {

		    	thisForm.contractAmount.set("missingMessage","Contract amount is a required field.");
				var decimalRegex = ValidationConstants("decimalRegex");

					if(!decimalRegex.test(thisForm.contractAmount.get('value')))	{
						thisForm.contractAmount.set("invalidMessage", "Contract amount is invalid.");					
						return false;

					} else if(MathJs.compare(thisForm.toNumber(thisForm.contractAmount.get('value')),0.00) <=0) {
								thisForm.contractAmount.set("invalidMessage", "Contract amount must be > 0.00");					
								return false;
					}

		    	return true;	
		    }

		},
		

		changingContractNumberYes:  function() {
			
			this.jsonContractObject['DIRTYFLAG'] = true;
			return true;
		},
		
		changingContractNumberNo:  function() {
			
			this.jsonContractObject['CONTRACTNUMBER'] = this.jsonContractObject.OLDCONTRACTNUMBER;
			return true;
		},
		

		rollbackIndexing : function() {
			
			thisForm.setRollbackValues();
			thisForm.serviceParameters['jsonDataObject']	= JSON.stringify(thisForm.jsonContractObject);	
			thisForm.serviceParameters['frontEndAction'] = arguments.callee.nom;
			request.postPluginService("GilCnPlugin", "ContractService", 'application/x-www-form-urlencoded',{
				requestBody : ioquery.objectToQuery(thisForm.serviceParameters),
	    		requestCompleteCallback : lang.hitch(this, function (response) {

	    			console.log('Index rolled back successfully.');

	    		})
			});
		},
		
		
		rollbackCountryIndexing : function() {

			thisForm.serviceParameters['frontEndAction'] = arguments.callee.nom;
			request.postPluginService("GilCnPlugin", "ContractService", 'application/x-www-form-urlencoded',{
				requestBody : ioquery.objectToQuery(thisForm.serviceParameters),
	    		requestCompleteCallback : lang.hitch(this, function (response) {

	    			console.log('Country index rolled back successfully.');

	    		})
			});
		},
	    
	    

		setFieldsNotEditable :  function(editable) {
			
			this.contractNumber.set('disabled',editable);
			this.contractCustomerName.set('disabled',editable);
			this.contractCustomerNumber.set('disabled',editable);
			this.contractAmount.set('disabled',editable);
			this.contractCurrency.set('disabled',editable);	
			this.contractOfferingLetterNumber.set('disabled',editable); 
			this.addCmInvoiceBtnId.set("disabled", editable );	
			this.saveBtnId.set("disabled", editable );	
			this.addGaptsInvoiceBtnId.set("disabled", editable );	
	    },
	    
	    
		setInvoiceFieldsNotEditable :  function(editable) {
			
			this.contractInvoiceNumber.set('disabled',editable);	
			this.contractInvoiceDate.set('disabled',editable);
	    },
	    
	    
		setAmountFieldNotEditable :  function(editable) {
			
			this.contractAmount.set('disabled',editable);
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
		        domclass.add(buttonYes.focusNode, "yesNoOkButton")
		        
		        var buttonNo = new dijit.form.Button({"label": "No",
		            onClick: function(){
		            	
		            	callbackNo();
		            	d1.destroyRecursive();  
		            	
		            } });
		        buttonNo.placeAt(actionBar);
		        domclass.add(buttonNo.focusNode, "yesNoOkButton")
            	
            } else {
            
			        var actionBar = dojo.create("div", {"class": "dijitDialogPaneContentArea",}, d1.containerNode);
			        var actionBar = dojo.create("div", { "class": "dijitDialogPaneActionBar"  }, d1.containerNode);
		
			        var buttonOk =  new dijit.form.Button({"label": "Ok",
			            onClick: function(){
			            	
			            	d1.destroyRecursive();
			            	
			            } });
			        buttonOk.placeAt(actionBar);
			        domclass.add(buttonOk.focusNode, "yesNoOkButton")
			        
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
		
		
		openInvoiceSearchDialog: function(evt) {
			
		try {	
			
			if (this.isDoubleClick(this.addGaptsInvoiceBtnId)) return;
			
			var dialogInvoice = null;
			var buttonClicked =  registry.getEnclosingWidget(evt.target);
			dialogInvoice = new InvoiceSearchDialog({	callerForm:thisForm, serviceParameters: this.serviceParameters  });
			dialogInvoice.showInvoiceSearchDataGrid();
			
		}catch(e){
			console.log(e);
		}
			
		},

	});
		
		return ContractDialog;
});
