define(["dojo/_base/lang",
        "ecm/model/Action", 
        "ecm/model/Request", 
        "dojo/_base/declare", 
		"ecm/widget/dialog/BaseDialog",
		"dojo/json",
		"gilCnPluginDojo/util/JsonUtils",
		"gilCnPluginDojo/util/MathJs",
		"dojo/text!./templates/MiscInvoiceDialog.html",
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
	    "ecm/widget/dialog/ConfirmationDialog",
	    "dijit/Tooltip",
	    "dojo/date/stamp",
	    "dijit/focus",
	    "dojo/_base/connect", 
	    "dojo/_base/event",
	    "gilCnPluginDojo/MiscSupplierSearchDialog",
	    "dojox/grid/DataGrid",
	    "dojo/data/ItemFileWriteStore",
	    "gilCnPluginDojo/InvoiceDialogMessages",
	    "dojo/io-query",
	    "dojo/on",
	    "dojo/dom",
	    "dojo/dom-style",
	    "gilCnPluginDojo/GILDialog", 
	    "dojo/dom-class",
	    "dojo/aspect" ,
	    "gilCnPluginDojo/DialogMessages",
	    
	],
	
	function(lang, action, request,declare,	BaseDialog,	json,JsonUtils, MathJs, template, array, dom,domReady,form,date,locale,
			domStyle,registry,query,construct,DateTextBox,parser,domattr,AptsDialog,ValidationConstants,ConfirmDialog,
			NonModalConfirmDialog,i18n,	number,Button,ConfirmationDialog,Builder,ECMConfirmationDialog,Tooltip,stamp ,
			focus,connect, event,MiscSupplierSearchDialog,DataGrid,ItemFileWriteStore, InvoiceDialogMessages,ioquery,on,dom,domStyle,GILDialog,domclass,aspect,DialogMessages) {

	/**
	 * @name gilCnPluginDojo.MiscInvoiceDialog
	 * @class Provides a dialog box that is used to edit the properties of a document or folder.
	 * @augments ecm.widget.dialog.BaseDialog
	 */
	var MiscInvoiceDialog = declare("gilCnPluginDojo.MiscInvoiceDialog",[BaseDialog,AptsDialog,GILDialog], {
		
		contentString: template,
		widgetsInTemplate: true,
		jsonInvoiceObject : null,
		cmValues:null,
		localeBundle:null,
		isSupplySearchOpen: false,
		saveBtnId:null,
		addContractBtnId:null,
		defaultsBtnId:null,	
		countryCode :null,
		searchType:null,
		errorCurrencyType : false,
		
		postCreate: function() {
			
			this.inherited(arguments);
			this.setSize(980, 550);
			this.setResizable(true);
			this.setMaximized(false);
			this.addButton("Cancel", "cancel", false, true, "cancel");
			this.addButton("Save", "save", false, true, "save");
			this.setTitle("MISC Index Invoice [" + this.serviceParameters['className'] + "]");
			this.setWidgetsInitialization();
			this.setCustomizedValidation();
			this.hideFieldsForCountry();
			this.setNonEditableFields(); 	
			this.showProvinceCombo();
			
			//for defect : 1832048
			this.initializeDateFields();
			this.upperCaseFields();
			//for defect : 1832048
			
			this.initializeAmountFields();
			
			
			this.setLayout();
						
		},

		setWidgetsInitialization: function() {
			
			var packageName = "dojo.cldr";
		    var bundleName = "gregorian";
		    var localeName = dojo.locale; 
		    this.localeBundle = i18n.getLocalization(packageName, bundleName, localeName);
		    this.saveBtnId = registry.byId(dojo.query('[id^="save"]')[0].id);
			dojo.style(this.cancelButton.domNode,"display","none"); 
			//for defect : 1832048
	        //registry.byId(this.id+"_invoiceDate").set("placeholder", this.localeBundle['dateFormat-short'] );
    		registry.byId(this.id+'_supplierName').set('_onChangeActive', false);
    		registry.byId(this.id+'_supplierNumber').set('_onChangeActive', false);
    		//registry.byId(this.id+'_accountNumber').set('_onChangeActive', false);
       		registry.byId(this.id+'_invoiceType').set('_onChangeActive', false);
       		registry.byId(this.id+'_invoiceCurrency').set('_onChangeActive', false);
       		dijit.registry.byId(this.id+'_invoiceDate').set('_onChangeActive', false);
			dijit.registry.byId(this.id+'_provinceCode').set('_onChangeActive', false);	
       		      	
    	

		},
		
 // added for defect : 1832048
	    
	    initializeDateFields: function() {
			debugger;
			var allDtInputWid = dojo.query('input#[id*="Date"][id^="'+ this.id +'_"]');
			for(var i=0; i<allDtInputWid.length; i++){
			  this.setDatePattern(registry.byId(allDtInputWid[i].id));
			}
		},
		
		upperCaseFields: function() {
			
			var allValTextbox = dojo.query('input#[id^="'+ this.id +'_"]');
			for(var i=0; i<allValTextbox.length; i++){
				this.toUpperCase(allValTextbox[i]);
			}
		},
		
       initializeAmountFields: function() {
			
    		this.isNumberKey(dijit.byId(this.id+"_invoiceAmountTotal"));
    		this.isNumberKey(dijit.byId(this.id+"_invoiceAmountTax"));
    		
		},		
		
		
     
		
//		cancel : function() {
//			that = this;
//			that.counterCancel=that.counterCancel+1;
//			if(that.isFormDirty(that.id+"_miscInvoiceForm") || that.jsonInvoiceObject.DIRTYFLAG == true){
//				var	dialogMsgs=new DialogMessages();
//				dialogMsgs.addConfirmMessages("You have unsaved changes. Are you sure you want to quit?");
//				that.showMessage("Confirm",dialogMsgs.confirmMessages(), true, that.choiceYCancel,that.choiceNCancel);
//
//			}else if(that.counterCancel==1){
//				try { 
//					that.rollbackIndexing();
////					that.destroyRecursive();
//					
//				
//				}catch(e){
//					console.log(e);
//				}
//			}else{
//				that.destroyRecursive();
//			}
//			
//
//		},
		
cancel: function(dirtCheck) {
			
			var dlgMsg = new InvoiceDialogMessages();
			console.log("dirtCheckdirtCheckdirtCheck:::"+dirtCheck);
			try {
				if(dirtCheck) {
					if( thisForm.isFormDirty(thisForm.id+"_miscInvoiceForm") || thisForm.jsonInvoiceObject['DIRTYFLAG']){
						dlgMsg.addConfirmMessages("You have unsaved changes. Are you sure you want to quit?");
							thisForm.showMessage("Confirm",dlgMsg.confirmMessages(),true ,thisForm.performCancel, thisForm.noop);
					} else {
						thisForm.performCancel();
					}
				} else {
					thisForm.performCancel();
				}
			}catch(e){
				console.log('Error in method ' + arguments.callee.nom + ' - ' + e);
			}
		},
		
performCancel: function() {
			
			try {

				if (thisForm.jsonInvoiceObject['DIRTYFLAG']){
					
					thisForm.serviceParameters['frontEndAction'] = "rollbackIndexing";
					request.postPluginService("GilCnPlugin", "MiscInvoiceService",'application/x-www-form-urlencoded', {
			    		requestBody : ioquery.objectToQuery(thisForm.serviceParameters),
					    requestCompleteCallback : lang.hitch(this, function (response) {
					    	thisForm.finalizeGUI();
			    		})
					});
				} else {
					thisForm.finalizeGUI();
				}
				
			}catch(e){
				console.log('Error in method ' + arguments.callee.nom + ' - ' + e);
			}
		},
		
//		choiceYCancel:function(){
//			
//			try { 
//				that.rollbackIndexing();		
//			
//			}catch(e){
//				console.log(e);
//			}
//		},
//		choiceNCancel:function(){
//			//nothing to do
//			that.counterCancel=0;
//		},
//		
		
		
      rollbackIndexing: function(){
			that=this;
			that.serviceParameters['frontEndAction'] = arguments.callee.nom;		
			//that.serviceParameters['frontEndAction'] = "rollbackIndexing";
			request.postPluginService("GilCnPlugin", "MiscInvoiceService",'application/x-www-form-urlencoded', {
	    		requestBody : ioquery.objectToQuery(that.serviceParameters),
			    requestCompleteCallback : lang.hitch(that, function (response) {
			    	
	    			console.log('Index rolled back successfully.');
	    			that.finalizeGUI();
	    			return true;
		    		
	    		})
			});
		},
		
		showProvinceCombo: function(){
			
			this.cmValues = JSON.parse(this.serviceParameters['businessAttributes']);	
			var country=this.cmValues['countryCode'];
			
			if(country!='CA') {    		
	    		domStyle.set(dojo.byId(this.id+"_provinceCode"),"display","none");   
	    		domStyle.set(registry.byId(this.id+"_provinceCode").domNode, 'display', 'none');		
	        	
	    		
			}
			
			
		},
		
		
		
		setLayout:function(){
			var restoring = (restore) => {
				if(restore != null){

					if(!domclass.contains(this.actionBar,"miscInvoiceEcmDialogPaneActionBar")){			
						domclass.add(this.actionBar,"miscInvoiceEcmDialogPaneActionBarR");
						domclass.remove(this.actionBar, 'miscInvoiceEcmDialogPaneActionBarM');
						domStyle.set(this.actionBar, "padding-right", "410px");
					}

				}
			};

			var maximizing = (maximize) => {
				if(maximize != null){

					if(!domclass.contains(this.actionBar,"miscInvoiceEcmDialogPaneActionBarM")){
						domclass.add(this.actionBar,"miscInvoiceEcmDialogPaneActionBarM");
						domclass.remove(this.actionBar, 'miscInvoiceEcmDialogPaneActionBarR');
						domStyle.set(this.actionBar, "padding-right", "15px");
					}

				}
			};

			aspect.after(this._maximizeButton, 'onClick', maximizing, this._maximizeButton);
			aspect.after(this._restoreButton, 'onClick', restoring, this._restoreButton);			
			domclass.add(this.actionBar,"miscInvoiceEcmDialogPaneActionBarR");
			domStyle.set(this.actionBar, "padding-right", "410px");
		}, 
		
		
	
		
		
		
		save: function() {
			
			if (this.isDoubleClick(this.saveBtnId)) return;
			console.log(arguments.callee.nom + ' was clicked:' + this.actionClick + ' time(s).');
			
			this.serviceParameters['frontEndAction'] = arguments.callee.nom;
			var dialogMessages = null;
			 this.cmValues = JSON.parse(this.serviceParameters['businessAttributes']);	
			 var country=this.cmValues['countryCode'];
			

			try {

					if (!this.miscInvoiceForm.validate()){
						console.log('Errors found on screen.');
						this.enableClick(this.saveBtnId);
			            return false;
					} 
			    
				    this.jsonInvoiceObject['validationIsOfferLetterInCM'] 				= true;
				    this.jsonInvoiceObject['validationIsInvoiceNumberChanging']  		= false;
				    this.jsonInvoiceObject['validationIsDocumentIndexedProperly'] 		= false;
				    this.jsonInvoiceObject['isDuplicateInvoicenumber'] 			= false;
				    this.jsonInvoiceObject['validationTaxAmountEqualTargetvatQuestion'] = false;
	            	this.jsonInvoiceObject['validationPaid'] 						  	= true;
	            	this.jsonInvoiceObject['validationVarianced'] 					  	= true;
	            	/*this.jsonInvoiceObject['validationIsContractFolderCreated'] 		= true;
	            	this.jsonInvoiceObject['validationIsContractCreated'] 				= true;*/
				
					this.jsonInvoiceObject['INVOICENUMBER'] 			= this.invoiceNumber.get('value');
					this.jsonInvoiceObject['INVOICEDATE'] 			= this.invoiceDate.get('displayedValue');
					this.jsonInvoiceObject['TOTALAMOUNT'] 			= this.invoiceAmountTotal.get('value');
					this.jsonInvoiceObject['VATAMOUNT'] 				= this.invoiceAmountTax.get('value');
					this.jsonInvoiceObject['NETAMOUNT'] 				= this.netAmount.get('value');
					this.jsonInvoiceObject['VENDORNAME'] 			= this.supplierName.get('value');
					this.jsonInvoiceObject['VENDORNUMBER'] 			= this.supplierNumber.get('value');
					this.jsonInvoiceObject['OCR'] 					= this.ocrKid.get('value');
					this.jsonInvoiceObject['CURRENCY'] 				= this.invoiceCurrency.get('value');
					this.jsonInvoiceObject['INVOICETYPE'] 			= this.invoiceType.get('value');
					this.jsonInvoiceObject['VATCODE'] 				= this.taxCode.get('value');
					this.jsonInvoiceObject['COMPANYCODE'] 			= this.compCode.get('value');
					this.jsonInvoiceObject['POEXCODE'] 				= this.poexCode.get('value');
					this.jsonInvoiceObject['DBCR'] 					= this.dbCr.get('value');
					this.jsonInvoiceObject['COUNTRY']               = country;					
					
		    		
		    		
		    		if (this.isTaxInvoiceExhibitionAllowed(country)) {
		    			
		    			this.jsonInvoiceObject['TAXINVOICENUMBER'] = registry.byId(this.id+"_taxInvoiceNumber").get('value');	
		    		}
		    		
		    		if (this.isInvoiceSuffixExhibitionAllowed(country)) {

			    		this.jsonInvoiceObject['INVOICESUFFIX'] = registry.byId(this.id+"_invoiceSuffix").get('value');
		    		}	
		    		
		    		if (this.isProvinceCodesExhibitionAllowed(country)) {   			
		    			

			    		this.jsonInvoiceObject['PROVINCECODE'] = registry.byId(this.id+"_provinceCode").get('value');
		    		}
		    		 
		    		if (this.isSupplyDateExhibitionAllowed(country)) {
		    			
		    			this.jsonInvoiceObject['TAXSUPPLYDATE'] = domattr.get(dom.byId(thisForm.id+"_supplyDate"), "value");
		    		}
						
					this.serviceParameters['jsonDataObject']	= JSON.stringify(this.jsonInvoiceObject);

					thisForm = this;
					
					dialogMessages = this.validateOnSave(this.performSave);			
					
					
			} catch(e) {
				console.log('Error in method ' + arguments.callee.nom + ' - ' + e);	
				thisForm.enableClick(thisForm.saveBtnId);
			} finally{
				
			}

		},
		
		
		validateOnSave: function(perfomSaveCallback) {
			
			this.serviceParameters['frontEndAction'] = arguments.callee.nom;
			var dialogMessages = new InvoiceDialogMessages();
			this.cmValues = JSON.parse(this.serviceParameters['businessAttributes']);	
			var country=this.cmValues['countryCode'];
			
			try {
					
					thisForm = this;
					var jsonUtils = new JsonUtils();
					var NinetyNinePointNineNineNine = 99.999;
					
					this.jsonInvoiceObject['promptInvoiceSuppyExists'] = false;
					this.jsonInvoiceObject['promptWrongDateDiff'] = false;
					this.jsonInvoiceObject['promptTaxInvoiceNumber'] = false;
					this.jsonInvoiceObject['promptHUFReview'] = false ;
					this.jsonInvoiceObject['promptDatePopup'] = false ;
					this.jsonInvoiceObject['promptSupplyDatePopup'] = false;
					
					request.postPluginService("GilCnPlugin", "MiscInvoiceService",'application/x-www-form-urlencoded', {
				    		requestBody : ioquery.objectToQuery(thisForm.serviceParameters),
						    requestCompleteCallback : lang.hitch(this, function (response) {
					    			
					    			this.jsonInvoiceObject	= JSON.parse(response.miscInvoiceJsonString);
					    			
					    			var invTotalNum = this.toNumber(this.invoiceAmountTotal.get("value"));
									var invTotalStr = this.invoiceAmountTotal.get("value");
									if ( invTotalNum==null || invTotalNum == undefined || MathJs.compare(invTotalNum,0) <=0  )	 {		
										dialogMessages.addErrorMessages("Invoice Amount must be > 0.00");	
										perfomSaveCallback(dialogMessages);
										return;
									}
									
									var invnetAmount = this.toNumber(this.netAmount.get("value"));
									var invTotalStr = this.netAmount.get("value");
									if ( invnetAmount==null || invnetAmount == undefined || MathJs.compare(invnetAmount,0) <=0  )	 {		
										dialogMessages.addErrorMessages("Net Amount must be > 0.00");	
										perfomSaveCallback(dialogMessages);
										return;
									}
						    			
					    			if(country == 'KR' && this.jsonInvoiceObject.TAXINVOICENUMBER.trim().length == 0) {
					    				
					    				this.jsonInvoiceObject['promptTaxInvoiceNumber'] = true;	
					    			}
					    			
					    			if (this.taxCode.get('value') ==  null || this.taxCode.get('value').trim().length == 0) {
					    				
					    				dialogMessages.addErrorMessages("Tax Code is a required field.");	
					    			}
					    			
					    			if (this.invoiceCurrency.get('value') ==  null || this.invoiceCurrency.get('value').trim().length == 0) {
					    				
					    				dialogMessages.addErrorMessages("Currency is a required field.");	
					    			}
					    			
					    			//check any waring messgaes in response
//									if(this.jsonInvoiceObject.dialogwarnMsgs.length>0){
//										this.jsonInvoiceObject['promptDatePopup'] = true;	
//										perfomSaveCallback(dialogMessages);
//										return;
//									}								
					    			
					    							    								    			
					    			if (this.jsonInvoiceObject.validationIsInvoiceNumberChanging &&
					    				this.jsonInvoiceObject.isDuplicateInvoicenumber) {
					    				
					    				dialogMessages.addErrorMessages("Duplicate Invoice entered.");	
					    				
					    			} else if ( this.jsonInvoiceObject.validationIsInvoiceNumberChanging == true &&
					    					   this.jsonInvoiceObject.isDuplicateInvoicenumber == false) {
					    				
					    						this.jsonInvoiceObject['promptInvoiceSuppyExists'] = true;
					    				
					    			}else if ( this.jsonInvoiceObject.validationIsInvoiceNumberChanging == false &&
					    					   this.jsonInvoiceObject.isDuplicateInvoicenumber == true) {
					    				
					    					dialogMessages.addErrorMessages("Duplicate Invoice entered.");
					    			}
					    			
					    			/*if (!this.jsonInvoiceObject.validationIsDocumentIndexedProperly) {
					    				
					    				dialogMessages.addErrorMessages("Document did not index correctly.");
					    			}*/	    			
					    					   			
					    			
					    								    			
					    			if(this.invoiceCurrency.get("value").trim() == 'HUF'){
										
										var totalAmount = this.toNumber(this.invoiceAmountTotal.get("value"));
										var vatAmount = this.toNumber(this.invoiceAmountTax.get("value"));
										var netAmount = this.toNumber(this.netAmount.get("value"));
										var zero = this.toNumber('0');
										
										var totalAmountSubtract = MathJs.subtract(totalAmount,MathJs.fix(MathJs.abs(totalAmount)));
										var vatAmountubtract = MathJs.subtract(vatAmount,MathJs.fix(MathJs.abs(vatAmount)));
										var netAmountSubtract = MathJs.subtract(netAmount,MathJs.fix(MathJs.abs(netAmount)));
										
										if( MathJs.compare(totalAmountSubtract, zero) > 0 ||
										    MathJs.compare(vatAmountubtract, zero) > 0 ||	
										    MathJs.compare(netAmountSubtract, zero) > 0   ){
											this.jsonInvoiceObject['promptHUFReview'] = true;
										}
										
									}
					    			
					    			
					    			
							        
							        if (this.errorCurrencyType == true)
							        {
							        	  				        	  
							    		  dialogMessages.addErrorMessages( "Currency not on file for invoice type");
							    		  dialogMessages.setHasErrorMessages(true);	
							       	}
					    			
					    			

					    		if(this.jsonInvoiceObject.VENDORCOMMISSION == null) {
					    			
					    			this.jsonInvoiceObject.VENDORCOMMISSION = 0;
					    		}
					    		
						    	var commission = this.toNumber(this.jsonInvoiceObject.VENDORCOMMISSION);
						    	
						        if ((this.isCommissionInvoice(this.jsonInvoiceObject.INVOICETYPE )) &&
						        	((MathJs.compare(commission,NinetyNinePointNineNineNine)!=0)) )
						        {
						            if (this.jsonInvoiceObject.DOCUMENTMODE != 3)
						            {
						            	 dialogMessages.addErrorMessages("Commission Invoices are not valid for this supplier.");
						            }
						        }

					       
					        if(this.isCOMInvoice(this.jsonInvoiceObject.INVOICETYPE) && this.isEXEMPTorStartswithDEVofSRNumber()) {
					        	
				            	 dialogMessages.addErrorMessages("Commission Invoices are not valid for this supplier.");
					        }

					        if ((this.jsonInvoiceObject.OCRREQUIRED=='R') && (this.ocrKid.get('value')==null || this.ocrKid.get('value').trim() == '')) {
					        	
					        	dialogMessages.addErrorMessages("OCR/KID is a required field.");
					        }   	            
					       
					        
					        
					        
					        if (this.isSupplyDateExhibitionAllowed(country )){
					        	
					        	var countryPL = country=='PL';
					           	var supplyDate = domattr.get(dom.byId(thisForm.id+"_supplyDate"), "value");
					           	var isSupplyDateValid = true;
					           	
					           	try {
					           		
					           		var parsedSupplyDate = locale.parse(domattr.get(dom.byId(thisForm.id+"_supplyDate"), "value"), {
																        locale:dojo.locale,
																        selector: 'date'
														           		});
					           		
							    if (parsedSupplyDate == null) {
							    	
							    	isSupplyDateValid = false;
							    }
							    
					           	} catch(e) {
					           		isSupplyDateValid = false;
					           	}
					        	
					        	if (!countryPL || supplyDate.length > 0) {
					        		
						        	if(!isSupplyDateValid)	{
							        		
							        		//dialogMessages.addErrorMessages("Tax Supply Date is a required field and  must have format "+ this.localeBundle['dateFormat-short']);
						        		dialogMessages.addErrorMessages("Tax Supply Date is a required field and  must have format MM/DD/YYYY");	
							        		
						        	} 
//						        	else {
//						        		
//										if (date.difference(parsedSupplyDate,new Date()) > 300	)	{
//											
//											dialogMessages.addErrorMessages("Tax Supply Date must not be greater than 300 days old.");					
//											
//										}
//						        	}
					        	}
								    

					        }
					        
					        
					        if(country == "NO" &&  ( this.ocrKid.get('value')!=null && this.ocrKid.get('value').trim())){
					        	
					        	val = this.ocrKid.get('value').trim();
				
								var digitsOnlyRegex = ValidationConstants("digitsOnlyRegex");
				
								if(!digitsOnlyRegex.test(val)) {
									
									dialogMessages.addErrorMessages("OCR/KID field must contain numbers only");
								}
					        }	
					        
					        if(this.jsonInvoiceObject.DOCUMENTMODE == 3){
					        var invVarAmt = this.toNumber(this.jsonInvoiceObject.INVVARAMT);
		                	var invBalAmt = this.toNumber(this.jsonInvoiceObject.INVBALAMT);
					        
					        if(MathJs.compare(invVarAmt,0.00) == 0 && MathJs.compare(invBalAmt,0.00) == 0  ){
		                		console.log("inside compare block");
		                		dialogMessages.addErrorMessages("Invoice has a balance of 0.00 and cannot be re-indexed");
		                		
		                    }
					        if(this.isPartiallyPaid()){
					        		console.log("inside compare block for partial");
			                		dialogMessages.addErrorMessages("Invoice has a payment against it and cannot be updated.");
					        }
					        }
					        
					        if(country== "CA"){
					        	
					        	 var vatamount = 0.00;
							        vatamount = this.jsonInvoiceObject.VATAMOUNT;
							        var targetVat = this.toNumber(this.jsonInvoiceObject.VATTARGETAMOUNT);
							        var vatVariance = this.toNumber(this.jsonInvoiceObject.VATVarianceStr);      
							        var vatDiff  = MathJs.abs(MathJs.subtract(targetVat,vatamount));				        
							        							        		
							        						        
							        if (MathJs.compare(vatDiff,vatVariance)>0 ){   				        		   	    
					        	   	
					        	
					        		
					        		this.jsonInvoiceObject['promptWrongDateDiff'] = true;
					        		this.jsonInvoiceObject['WRONGVATDIFF'] = true;
					        		this.jsonInvoiceObject.OLDVATCODE= this.jsonInvoiceObject.VATCODE;					        	
					       						        	
						 }
					   }
					        
					        if(country!="IN"){  
					        
						        var vatamount = 0.00;
						        vatamount = this.jsonInvoiceObject.VATAMOUNT;
						        var targetVat = this.toNumber(this.jsonInvoiceObject.VATTARGETAMOUNT);
						        var vatVariance = this.toNumber(this.jsonInvoiceObject.VATVarianceStr);      
						        var vatDiff  = MathJs.abs(MathJs.subtract(targetVat,vatamount));
						        
						        this.jsonInvoiceObject['VATTARGETAMOUNT'] = this.formatNumber(targetVat,2).toString();
						        this.jsonInvoiceObject['VATVarianceStr']  = this.formatNumber(vatVariance,2).toString();
						        
						        if (MathJs.compare(vatDiff,vatVariance)>0 ){
						        	
						        	console.log("inside vat diff block  ");
						        	
						        	if (this.jsonInvoiceObject.NETEQBAL == 'Y' || this.jsonInvoiceObject.DOCUMENTMODE == 1){

						        				this.jsonInvoiceObject['VATAMOUNT'] = targetVat;			        				
						        				this.jsonInvoiceObject['promptWrongDateDiff'] = true;
						        				this.jsonInvoiceObject['WRONGVATDIFF'] = true;

						        	}
						        	
						       		if(this.jsonInvoiceObject.DOCUMENTMODE == 3){
						       			
						       			console.log("inside document mode 3 block");
					                	var invVarAmt = this.toNumber(this.jsonInvoiceObject.INVVARAMT);
					                	var invBalAmt = this.toNumber(this.jsonInvoiceObject.INVBALAMT);
					                	
					                	if(MathJs.compare(invVarAmt,0.00) > 0 && MathJs.compare(invBalAmt,0.00) == 0  ){
					                		dialogMessages.addWarningMessages("Invoice has been varianced and has a balance of 0.00 . To change invoice amount unvariance in GAPTS first");
					                		
					                	}
					                	if(MathJs.compare(invVarAmt,0.00) > 0 && MathJs.compare(invBalAmt,0.00) > 0  ){
					                		dialogMessages.addWarningMessages("Invoice has been varianced and has a balance of "+invBalAmt+". To change invoice amount unvariance in GAPTS first");
					                		
					                	}
					                	
					                }
						       		
						        }
					        
					        }
					        
					        perfomSaveCallback(dialogMessages);
		    		})
		    		
		    		
					});
						
					
			}catch(e){
				console.log('Error in method ' + arguments.callee.nom + ' - ' + e);
				this.enableClick(this.saveBtnId);
				this.enableClick(this.addContractBtnId );
			} finally{

			}

		},
		
		
		performSave: function(dialogMessages) {
			
			try {
			
			 if(dialogMessages.hasErrorMessages()) {
				 
				 	thisForm.enableClick(thisForm.saveBtnId);	
	    			thisForm.showMessage("Validation",dialogMessages.getAllMessages(),false,null,null);
	    			return false;  
			
			} 	else if (dialogMessages.hasWarningMessages()){
				
				thisForm.enableClick(thisForm.saveBtnId);	
    			thisForm.showMessage("Warning",dialogMessages.getAllMessages(),"onlyOk",null,null);
    			return;  
				
			}	 
			 else if(thisForm.jsonInvoiceObject['promptInvoiceSuppyExists'] == true || 
					thisForm.jsonInvoiceObject['promptWrongDateDiff'] == true ||
				    thisForm.jsonInvoiceObject['promptTaxInvoiceNumber'] == true ||
				    thisForm.jsonInvoiceObject['promptHUFReview'] == true || thisForm.jsonInvoiceObject['promptDatePopup'] == true || thisForm.jsonInvoiceObject['promptSupplyDatePopup'] == true)  {
				
						thisForm.startPromptingOn('save');
						
			} else {
				
				//thisForm.promptOkForSaveMiscInvoice('save');
				thisForm.enableClick(thisForm.saveBtnId);	
				thisForm.submitConfirmationYes('save');
			}
			
			}catch(e){
				console.log('Error in method ' + arguments.callee.nom + ' - ' + e);
				thisForm.enableClick(thisForm.saveBtnId);
			} finally{
				
			}
			
		},
		
		
		startPromptingOn: function(action) {
			
			var dialogMessages = new InvoiceDialogMessages();
	        
			if(thisForm.jsonInvoiceObject.promptWrongDateDiff) {
				
				thisForm.showMessage("Warnings","Tax Amount must equal " +  this.toNumber(this.jsonInvoiceObject.VATTARGETAMOUNT).toFixed(2) + " +/- $" + this.toNumber(this.jsonInvoiceObject.VATVarianceStr).toFixed(2) + ".\nDo you wish to correct?",true ,thisForm.promptYesForWrongVatDiff.bind(null,action), thisForm.promptNoForWrongVatDiff.bind(null,action));
				
			} else 	if(thisForm.jsonInvoiceObject.promptInvoiceSuppyExists) {
				
						thisForm.showMessage("Warnings","Invoice number/Supplier number already exists, Do you wish to change the invoice number ?",true ,thisForm.promptYesForInvoiceSuppyExists.bind(null,action), thisForm.promptNoForInvoiceSuppyExists.bind(null,action));
				
			} else if(thisForm.jsonInvoiceObject.promptTaxInvoiceNumber) {
				
					 thisForm.showMessage("Warnings","Tax Invoice Number is not entered and do you want to add ?",true ,thisForm.promptYesForTaxInvoiceNumber.bind(null,action),thisForm.promptNoForTaxInvoiceNumber.bind(null,action));

			} else if(thisForm.jsonInvoiceObject.promptHUFReview) {
				dialogMessages.addWarningMessages("HUF amounts must be rounded to the nearest dollar. Do you wish to correct ?");
				 thisForm.showMessage("Warning",dialogMessages.warningMessages(),true ,thisForm.promptYesForHufRound.bind(null,action), thisForm.promptNoForHufRound.bind(null,action));

				
			} else if(thisForm.jsonInvoiceObject.promptDatePopup) {
				dialogMessages.addWarningMessages("Invoice Date is greater than 300 days old and specific approvals may apply for processing. Do you wish to correct?");
				 thisForm.showMessage("Warning",dialogMessages.warningMessages(),true ,thisForm.promptYesForDatePopup.bind(null,action), thisForm.promptNoForDatePopup.bind(null,action));

			} else if(thisForm.jsonInvoiceObject.promptSupplyDatePopup) {
				 dialogMessages.addWarningMessages("Tax Supply Date is greater than 300 days old and specific approvals may apply for processing. Do you wish to correct?");
				 thisForm.showMessage("Warning",dialogMessages.warningMessages(),true ,thisForm.promptYesSupplyDatePopup.bind(null,action), thisForm.promptNoForSupplyDatePopup.bind(null,action));
			} 
			else {
				thisForm.enableClick(registry.byId(dojo.query('[id^="'+action+'"]')[0].id));
				thisForm.submitConfirmationYes(action);
			}
			
		},
		
		promptNoForSupplyDatePopup: function(action) {
			
			thisForm.jsonInvoiceObject.promptSupplyDatePopup = false;
			thisForm.startPromptingOn(action);
		},
		
		promptYesSupplyDatePopup: function(action) {
			
			thisForm.focustoField("_supplyDate");			
			thisForm.enableClick(registry.byId(dojo.query('[id^="'+action+'"]')[0].id));
		},
		
		promptNoForDatePopup: function(action) {
			
			thisForm.jsonInvoiceObject.promptDatePopup = false;
			thisForm.startPromptingOn(action);
		},
		
		promptYesForDatePopup: function(action) {
			thisForm.focustoField("_invoiceDate");
			thisForm.enableClick(registry.byId(dojo.query('[id^="'+action+'"]')[0].id));
		},
		
		//method used to focus the input field based on ID
		focustoField: function(inputid) {
			 registry.byId(thisForm.id+inputid).focus();
		},
		
		promptNoForWrongVatDiff: function(action) {
			
			thisForm.jsonInvoiceObject.promptWrongDateDiff = false;
			thisForm.startPromptingOn(action);
		},
		
		promptYesForWrongVatDiff: function(action) {
			
			if (thisForm.jsonInvoiceObject['WRONGVATDIFF']){

				thisForm.recalculate();
				thisForm.invoiceAmountTax.set("value", thisForm.jsonInvoiceObject['VATTARGETAMOUNT']); 
				thisForm.jsonInvoiceObject['WRONGVATDIFF'] = false;
				thisForm.invoiceAmountTax.focus();
			}
			
			
			thisForm.enableClick(registry.byId(dojo.query('[id^="'+action+'"]')[0].id));
		},
		
		promptNoForHufRound: function(action) {
			
			thisForm.jsonInvoiceObject.promptHUFReview = false;
			thisForm.startPromptingOn(action);
		},
		
		promptYesForHufRound: function(action) {
			thisForm.invoiceAmountTax.focus();
			thisForm.enableClick(registry.byId(dojo.query('[id^="'+action+'"]')[0].id));
		},
		
		
		
		promptNoForInvoiceSuppyExists: function(action) {
			
			thisForm.jsonInvoiceObject.promptInvoiceSuppyExists = false;
			thisForm.startPromptingOn(action);
		},
		
		promptYesForInvoiceSuppyExists: function(action) {
			thisForm.invoiceNumber.focus();
			thisForm.enableClick(registry.byId(dojo.query('[id^="'+action+'"]')[0].id));
		},
		
		promptNoForTaxInvoiceNumber: function(action) {
			
			thisForm.jsonInvoiceObject.promptTaxInvoiceNumber = false;
			thisForm.startPromptingOn(action);	
		},
		
		promptYesForTaxInvoiceNumber: function(action) {
			
			thisForm.enableClick(registry.byId(dojo.query('[id^="'+action+'"]')[0].id));
		},
		
			
		submitConfirmationYes: function(action) {
			
		try {
			// check with Fernando regarding below commented code
			//if (this.isDoubleClick(registry.byId(dojo.query('[id^="'+action+'"]')[0].id))) return;
			
			//if (thisForm.isDoubleClick(registry.byId(dojo.query('[id^="'+action+'"]')[0].id))) return;
			
			console.log(arguments.callee.nom + ' was clicked:' + thisForm.actionClick + ' time(s).');

			thisForm.serviceParameters['frontEndAction'] = action;
			request.postPluginService("GilCnPlugin", "MiscInvoiceService",'application/x-www-form-urlencoded', {
	    		requestBody : ioquery.objectToQuery(thisForm.serviceParameters),
			    requestCompleteCallback : lang.hitch(this, function (response) {
	    			
	    			dialogMessagesResponse = new InvoiceDialogMessages();
	    			thisForm.jsonInvoiceObject	= JSON.parse(response.miscInvoiceJsonString);
	    			thisForm.enableClick(registry.byId(dojo.query('[id^="'+action+'"]')[0].id))
	    			console.log('Invoice saved successfully.');	    			
	    			
		    			
						if(thisForm.jsonInvoiceObject.dialogErrorMsgs.length>0){
							var msgs=thisForm.jsonInvoiceObject.dialogErrorMsgs;
							for (var i in msgs) {
								dialogMessagesResponse.addErrorMessages(msgs[i]);
							}
						} 
	    			
		    			if (dialogMessagesResponse.hasErrorMessages()) {
		    				thisForm.showMessage("Error",dialogMessagesResponse.getAllMessages(),false,null,null);
		    			} 
		    			
	    				if (action=='save') {
			    			thisForm.cancel(false);
			    		} 
		    			
		    			thisForm.jsonInvoiceObject.dialogErrorMsgs = [];
	    		
	    		})
			});
			
		}catch(e){
			console.log('Error in method ' + arguments.callee.nom + ' - ' + e);
			thisForm.enableClick(registry.byId(dojo.query('[id^="'+action+'"]')[0].id));
		}finally{
				
			}
		},
		
		
		
		
		stopAction: function() {

			if (thisForm.jsonInvoiceObject['WRONGVATDIFF']){

				thisForm.recalculate();
				thisForm.invoiceAmountTax.set("value", thisForm.jsonInvoiceObject['VATTARGETAMOUNT']); 
				thisForm.jsonInvoiceObject['WRONGVATDIFF'] = false;
			} 
			console.log('Invoice saving was canceled by user.');
		},
		
		show: function() {
			this.inherited("show", []);
		},
		
		
		exit: function() {
			
			this.destroy();
		},
		
		
		
		index: function() {
			
			try {
				
					this.serviceParameters['frontEndAction'] = arguments.callee.nom;
					thisForm = this;
					var jsonUtils = new JsonUtils();
					var dialogMessages = new InvoiceDialogMessages();
					this.cmValues = JSON.parse(this.serviceParameters['businessAttributes']);	
					var country=this.cmValues['countryCode'];	
					
										
					
					
					
					request.postPluginService("GilCnPlugin", "MiscInvoiceService",'application/x-www-form-urlencoded', {
			    		requestBody : ioquery.objectToQuery(thisForm.serviceParameters),
					    requestCompleteCallback : lang.hitch(this, function (response) {
								    			
								    		this.jsonInvoiceObject	= JSON.parse(response.miscInvoiceJsonString);
								    			
								    		this.setFieldsNotEditable(false);
								    		this.setPoexandCompCodefieldsDisabled(true);
								    		
								    			
								    		thisForm.show();
								    		
											thisForm.jsonInvoiceObject['promptInvoiceSuppyExists'] = true;
											thisForm.jsonInvoiceObject['promptWrongDateDiff'] = true;
											thisForm.jsonInvoiceObject['promptTaxInvoiceNumber'] = true;
																				    								    		
								    		thisForm.invoiceNumber.set("value",this.jsonInvoiceObject.INVOICENUMBER);			    		
								    							    		
								    		thisForm.invoiceDate.set("value",this.parseDate(this.jsonInvoiceObject.INVOICEDATE) );
								    		dijit.registry.byId(this.id+'_invoiceDate').set('_onChangeActive', true);
								    		thisForm.supplierName.set("value",this.jsonInvoiceObject.CUSTOMERNAME);						    		
								    		thisForm.additionalInfo.set("value","");
								    		thisForm.team.set("value", this.jsonInvoiceObject.TEAM);						    		
								    		thisForm.dueDate.set("value","");
								    		thisForm.source.set("value",this.jsonInvoiceObject.SOURCE);						    		
								    		thisForm.createdTimestamp.set("value",this.jsonInvoiceObject.createdTimeStamp);
								    		thisForm.userId.set("value","");						    		
								    		thisForm.invoiceAmountTotal.set("value",this.jsonInvoiceObject.TOTALAMOUNT.replace(/,/g, '.'));
								    		thisForm.invoiceAmountTax.set("value",this.jsonInvoiceObject.VATAMOUNT.replace(/,/g, '.'));						    		
								    		thisForm.netAmount.set("value",this.jsonInvoiceObject.NETAMOUNT.replace(/,/g, '.'));	
								    		thisForm.poexCode.set("value",this.jsonInvoiceObject.POEXCODE);
								    		thisForm.compCode.set("value",this.jsonInvoiceObject.COMPANYCODE);
								    		
								    		
								    		registry.byId(thisForm.id+'_supplierName').set("value",this.jsonInvoiceObject.VENDORNAME);									    		
								    		registry.byId(thisForm.id+'_supplierNumber').set("value",this.jsonInvoiceObject.VENDORNUMBER);								    		
								    									    		
								    		
								    		registry.byId(thisForm.id+'_supplierName').set('_onChangeActive', true); 
								    		registry.byId(thisForm.id+'_supplierNumber').set('_onChangeActive', true);
								    		
								    		
											
								    	 
								    		
								    		thisForm.ocrKid.set("value",this.jsonInvoiceObject.OCR);
									    		
								    		thisForm.dbCr.set("options", this.jsonInvoiceObject.dbCrSelectList);
								    		thisForm.dbCr.set("value", jsonUtils.getDefaultOrSelected(this.jsonInvoiceObject.dbCrSelectList,"value", true)); 						    		
								    		
								    		
								    		thisForm.invoiceCurrency.set("options", this.jsonInvoiceObject.currencyCodeSelectList);
								    		thisForm.invoiceCurrency.set("value", jsonUtils.getDefaultOrSelected(this.jsonInvoiceObject.currencyCodeSelectList,"value", true));
								    		registry.byId(this.id+'_invoiceCurrency').set('_onChangeActive', true);
								    		
								    		thisForm.invoiceType.set("options", this.jsonInvoiceObject.invoiceTypesSelectList);
								    		thisForm.invoiceType.set("value", jsonUtils.getDefaultOrSelected(this.jsonInvoiceObject.invoiceTypesSelectList,"value", true));
								    		registry.byId(this.id+'_invoiceType').set('_onChangeActive', true);
		
								    	
		
								    		thisForm.taxCode.set("options", this.jsonInvoiceObject.vatCodesSelectList);
								    		thisForm.taxCode.set("value", jsonUtils.getDefaultOrSelected(this.jsonInvoiceObject.vatCodesSelectList,"value", true));
	
								    		
								    		
								    		if (thisForm.isTaxInvoiceExhibitionAllowed(country)) {
								    			
								    			registry.byId(thisForm.id+"_taxInvoiceNumber").set( "value", this.jsonInvoiceObject.TAXINVOICENUMBER);	
								    		}
								    		
								    		if (thisForm.isInvoiceSuffixExhibitionAllowed(country)) {
								    		
									    		registry.byId(thisForm.id+"_invoiceSuffix").set("options", this.jsonInvoiceObject.invoiceSuffixesSelectList );
									    		registry.byId(thisForm.id+"_invoiceSuffix").set("value", jsonUtils.getValueIfOtherKeyExists(this.jsonInvoiceObject.invoiceSuffixesSelectList,"value", true) );
									    		registry.byId(thisForm.id+"_invoiceSuffix").startup();
								    		}	
								    		
								    		
								    		
								    		if (thisForm.isProvinceCodesExhibitionAllowed(country)) {
								    								    			
									    	    registry.byId(thisForm.id+"_provinceCode").set("options", this.jsonInvoiceObject.provinceCodesSelectList );
									    		registry.byId(thisForm.id+"_provinceCode").set("value", jsonUtils.getDefaultOrSelected(this.jsonInvoiceObject.provinceCodesSelectList,"value", true) );
									    		registry.byId(thisForm.id+"_provinceCode").startup();
									    		dijit.registry.byId(this.id+'_provinceCode').set('_onChangeActive', true);
									    		
									    		
									    		
								    		}
								    		
								    		 
								    		if  (thisForm.isSupplyDateExhibitionAllowed(country)) {
								    			
								    			if(this.jsonInvoiceObject.TAXSUPPLYDATE !=null && this.jsonInvoiceObject.TAXSUPPLYDATE.trim()!="")
								    				
								    				try {

								    					registry.byId(thisForm.id+"_supplyDate").set( "value",this.parseDate(this.jsonInvoiceObject.TAXSUPPLYDATE,false));	
								    					
								    				} catch(e){
								    					console.log('Tax Supply Date is blank: '+this.jsonInvoiceObject.TAXSUPPLYDATE );
								    				}
								    			
								    		}
									    		
									        if (this.jsonInvoiceObject.distributorCodeSelectList!=null && this.jsonInvoiceObject.distributorCodeSelectList.length>1)	{
									        	
												var _label = null;
												 var _widget = null;
									        	
												_label =	construct.toDom("  <label  id='${id}_distrNumLabel' for='distrNum'>Distributor Number</label>&nbsp;");
												
										        _widget	 = new ecm.widget.Select({
										        	
										        	id:thisForm.id+"_distrNum", 
										        	name:"distrNum",

										          });
										        
							          	        construct.place(_label, thisForm.id+"_replacebleLabelForDistrNum","replace");
							          	        construct.place(_widget.domNode, thisForm.id+"_replacebleFieldForDistrNum","replace");
										        
									    		registry.byId(thisForm.id+"_distrNum").set("options", this.jsonInvoiceObject.distributorCodeSelectList );
									    		registry.byId(thisForm.id+"_distrNum").set("value", jsonUtils.getDefaultOrSelected(this.jsonInvoiceObject.distributorCodeSelectList,"value", true) );
									    		registry.byId(thisForm.id+"_distrNum").startup();
										        
									        }
									    		
								    		//TODO GILCN Iplement the methoid that sets this invoice attribute in backend
								    		//TODO GILCN for user ferandra does not aloow to do anything check that later 
									        if (this.jsonInvoiceObject.isWorkAcess) {
								    			
								    			this.setAmountFields(false);
								    		}						        
									        
								    		
								    		
								    			
								    			dialogMessages = this.validateOnIndex(this.performIndex);
								    		
								    		
								    			this.serialFormClean=this.serializeForm(thisForm.id+"_miscInvoiceForm"); 
								    		
								    		})
								});
					
			} catch(e) {
				console.log('Error in method index:::' + arguments.callee.nom + ' - ' + e);
			}
		},		
		
		
	
		
		setCAVATCodes: function(evt){
			
			this.serviceParameters['frontEndAction'] = arguments.callee.nom;
			thisForm = this;
			var jsonUtils = new JsonUtils();
			var dialogMessages = new InvoiceDialogMessages();
			this.cmValues = JSON.parse(this.serviceParameters['businessAttributes']);	
			var country=this.cmValues['countryCode'];
			var buttonClicked =  registry.getEnclosingWidget(evt.target);
			thisForm.searchType= buttonClicked;			
		
			thisForm.jsonInvoiceObject['PROVINCECODE']           = thisForm.provinceCode.get('value');    
			thisForm.jsonInvoiceObject['INVOICEDATE']            = thisForm.invoiceDate.get('value');
			thisForm.jsonInvoiceObject['SEARCHTYPE']             = thisForm.searchType 
			
			
			
			this.serviceParameters['jsonDataObject']	= JSON.stringify(this.jsonInvoiceObject);
			
			
			
			request.postPluginService("GilCnPlugin", "MiscInvoiceService",'application/x-www-form-urlencoded', {
	    		requestBody : ioquery.objectToQuery(thisForm.serviceParameters),
			    requestCompleteCallback : lang.hitch(this, function (response) {   
			    	this.jsonInvoiceObject	= JSON.parse(response.miscInvoiceJsonString);
			    	try{
			    	thisForm.taxCode.set("options", this.jsonInvoiceObject.caVATCodesSelectList);
		    		thisForm.taxCode.set("value", jsonUtils.getDefaultOrSelected(this.jsonInvoiceObject.caVATCodesSelectList,"value", true));
		    		
		    		registry.byId(thisForm.id+'_taxCode').set('_onChangeActive', true); 
			    	}
			    	catch(e)
			    	{
			    		console.log('Error in method ' + arguments.callee.nom + ' - ' + e);
			    	}
			    	
			    	
		  		})
			});
			
			
			
		},
		
		addConvination: function(){
			this.serviceParameters['frontEndAction'] = arguments.callee.nom;
			thisForm = this;
			var jsonUtils = new JsonUtils();
			var dialogMessages = new InvoiceDialogMessages();
			this.cmValues = JSON.parse(this.serviceParameters['businessAttributes']);	
			var country=this.cmValues['countryCode'];	
			
			
			thisForm.jsonInvoiceObject['COMPANYCODE'] 			= thisForm.compCode.get('value');
			thisForm.jsonInvoiceObject['POEXCODE'] 				= thisForm.poexCode.get('value');
			thisForm.jsonInvoiceObject['INVOICETYPE']           = thisForm.invoiceType.get('value'); 
			thisForm.jsonInvoiceObject['CURRENCY']            = thisForm.invoiceCurrency.get('value');
			
			
			this.serviceParameters['jsonDataObject']	= JSON.stringify(this.jsonInvoiceObject);
			
			request.postPluginService("GilCnPlugin", "MiscInvoiceService",'application/x-www-form-urlencoded', {
	    		requestBody : ioquery.objectToQuery(thisForm.serviceParameters),
			    requestCompleteCallback : lang.hitch(this, function (response) {   
			    	this.jsonInvoiceObject	= JSON.parse(response.miscInvoiceJsonString);
			    	
			    	
			    	try{
			    	if(thisForm.jsonInvoiceObject.validationSaveConvination==true)
			    		{
			    		  this.showMessage("<img src='./plugin/GilCnPlugin/getResource/error.png'  style='width:15px;height:15px;'> &nbsp;"+"Error","Currency not on file for invoice type",false,null,null);
			    		  thisForm.poexCode.set("value","");
				    	  thisForm.compCode.set("value","");
				    	  thisForm.errorCurrencyType = true;				    	  
			    		  dialogMessages.addErrorMessages( "Currency not on file for invoice type");
			    		  dialogMessages.setHasErrorMessages(true);			    		  
			    		  return dialogMessages;
			    		}
			    	else{
			    		thisForm.errorCurrencyType =false ;
			    		thisForm.poexCode.set("value",this.jsonInvoiceObject.POEXCODE);
			    		thisForm.compCode.set("value",this.jsonInvoiceObject.COMPANYCODE);
			    	}
			    	}
			    	catch(e){
			    		
			    		console.log('Error in method ' + arguments.callee.nom + ' - ' + e);
			    		
			    	}
			    	
		  		})
			}); 			
			
		},
		
		
		
		
		performIndex: function(dialogMessages) {
			
		
			var hasPromptMessages = false;
			
			if (thisForm.jsonInvoiceObject['promptChangingInvoiceNumberDate'] == true ) {
				
				hasPromptMessages = true;
			}
			
			if(dialogMessages.hasErrorMessages() ){
							
				thisForm.showMessage("Error",dialogMessages.getAllMessages(),false,null,null);
				//thisForm.rollbackIndexing();
			    thisForm.cancel(false);
			
			}else if(hasPromptMessages){
								
					  thisForm.startPromptingOnIndex();
				}
			
			if(dialogMessages.hasInfoMessages()){
				
				thisForm.showMessage("Information",dialogMessages.infoMessages(),false,null,null);	
			}
			
		},
		
		
		startPromptingOnIndex: function() {
	        
			if(thisForm.jsonInvoiceObject.promptChangingInvoiceNumberDate) {
				
				thisForm.showMessage("Warnings","Changing Invoice Number/Date.  Are you sure?",true ,thisForm.promptYesForPromptChangingInvoiceNumberDate, thisForm.promptNoForPromptChangingInvoiceNumberDate);
				
			} 
			
		},
		
		promptYesForPromptChangingInvoiceNumberDate: function() {

			thisForm.jsonInvoiceObject.DIRTYFLAG = true;
		},
		
		promptNoForPromptChangingInvoiceNumberDate: function() {
			
			thisForm.jsonInvoiceObject.INVOICENUMBER = thisForm.jsonInvoiceObject.OLDINVOICENUMBER;
			thisForm.jsonInvoiceObject.INVOICEDATE = thisForm.jsonInvoiceObject.OLDINVOICEDATE;		
    		
    		thisForm.invoiceNumber.set("value", 		thisForm.jsonInvoiceObject.OLDINVOICENUMBER);						    							    		
    		thisForm.invoiceDate.set("value", 			thisForm.parseDate(thisForm.jsonInvoiceObject.OLDINVOICEDATE) );
    		//thisForm.jsonInvoiceObject.DIRTYFLAG = false;

		},
		
		getAvailableReplacebleField : function() {
			
			if(dojo.byId(this.id+"_replacebleField1")!=null ) {
				
				return "1";
			} else if(dojo.byId(this.id+"_replacebleField2")!=null ) {
				
					 return "2";
			} else if(dojo.byId(this.id+"_replacebleFiel3")!=null ) {
				
					 return "3";
			}
			
		},
		
		
   isFullyPaid : function() {
			
			if(this.jsonInvoiceObject.DOCUMENTMODE == 1){
				return false;
			}
			
			 return (MathJs.compare(thisForm.toNumber(this.jsonInvoiceObject.INVBALAMT),0.00)==0 
	 				   && 
	 				   MathJs.compare(thisForm.toNumber(this.jsonInvoiceObject.INVVARAMT),0.00)==0) ;
		},
		
		
		isPartiallyPaid : function() {
			
			if(this.jsonInvoiceObject.DOCUMENTMODE == 1){
				return false;
			}
			
			return (MathJs.compare(this.toNumber(this.jsonInvoiceObject.validationNetAmountStr), this.toNumber(this.jsonInvoiceObject.validationNetBalanceStr)) > 0 
	 				&&  
	 				MathJs.compare(this.toNumber(this.jsonInvoiceObject.validationNetBalanceStr),0.00) > 0);
		},
		
		
		isVarianced : function() {
			
			return MathJs.compare(this.toNumber(this.jsonInvoiceObject.validationInvVarAmountStr), 0.00) >0
		},
		
		checkPaid: function() {
	    	if(this.jsonInvoiceObject!=null) { 
	           // if((this.jsonInvoiceObject.validationVarianced || this.jsonInvoiceObject.validationPaid)){
	    		if(this.isFullyPaid() || this.isPartiallyPaid() || this.isVarianced() ){
	         	   this.setFieldsNotEditable(true);
	            }
	    	}
		},
		
		validateOnIndex : function(callback) {

		
			var dialogMessages = new InvoiceDialogMessages();
			var isChangingInvoiceNUmber = false;
			 this.jsonInvoiceObject['promptChangingInvoiceNumberDate'] = false;
			 this.cmValues = JSON.parse(this.serviceParameters['businessAttributes']);	
			 var country=this.cmValues['countryCode'];
			 
			 try {
				
			 
					if( MathJs.compare(thisForm.toNumber(this.jsonInvoiceObject.INVBALAMT),0.00)==0 && 
						MathJs.compare(thisForm.toNumber(this.jsonInvoiceObject.INVVARAMT),0.00)==0 ) {

		
						dialogMessages.addInfoMessages( "Invoice has a balance of 0.00 and cannot be re-indexed.");
										    					
		                if(thisForm.isCountryChanging()){
		                	
		                	this.jsonInvoiceObject['COUNTRY']=country;
		                	dialogMessages.addErrorMessages( "Invoice has a balance of 0.00 and cannot be re-indexed.");
		                	dialogMessages.addErrorMessages( "Invoice is changing countries.");
		                	dialogMessages.setHasErrorMessages(true);
		                	this.rollbackCountryIndexing();

		             		//return dialogMessages;
		             		 callback(dialogMessages);
		             		 return;
		                	
		                }
		                this.jsonInvoiceObject['validationPaid'] = true;
					} 	
					

		    		
					if( thisForm.isCommissionInvoice(this.jsonInvoiceObject.INVOICETYPE)  && (thisForm.isVendorNameChanging() /* || this.isAccountNumberChanging()*/ )) {
			 	          
						dialogMessages.addInfoMessages( "Vendor and Account cannot be changed for commision invoices.");
						
						this.jsonInvoiceObject['DIRTYFLAG'] = true;
						//this.jsonInvoiceObject['ACCOUNTNUMBER'] = this.jsonInvoiceObject.OLDACCOUNTNUMBER;
						this.jsonInvoiceObject['VENDORNAME'] = this.jsonInvoiceObject.OLDVENDORNAME;
			    		registry.byId(thisForm.id+'_supplierName').set("value", 			this.jsonInvoiceObject.OLDVENDORNAME);									    		
			    		registry.byId(thisForm.id+'_supplierNumber').set("value", 			this.jsonInvoiceObject.OLDVENDORNUMBER);									    		
			    		//registry.byId(thisForm.id+'_accountNumber').set("value", 			this.jsonInvoiceObject.OLDACCOUNTNUMBER);	
	
						
					} else if ( thisForm.isVendorNameChanging()  /*|| this.isAccountNumberChanging()*/ ) {
						
								this.jsonInvoiceObject['vendorSearchRequired'] = true;
					}
					
					
		             if(MathJs.compare(this.toNumber(this.jsonInvoiceObject.validationNetAmountStr), this.toNumber(this.jsonInvoiceObject.validationNetBalanceStr)) > 0 &&  
		            		 MathJs.compare(this.toNumber(this.jsonInvoiceObject.validationNetBalanceStr),0.00) > 0){
		            	 
		            	 this.jsonInvoiceObject['validationPaid'] = true;
		            }
		            
		           
		             if(MathJs.compare(this.toNumber(this.jsonInvoiceObject.validationInvVarAmountStr), 0.00) >0){
		          
		            	this.jsonInvoiceObject['validationVarianced'] = true;
		            }
		             
			             
		             var oldInvoiceDate 	= this.parseDate(this.jsonInvoiceObject.OLDINVOICEDATE,false);
		             var currentInvoiceDate = this.parseDate(this.jsonInvoiceObject.INVOICEDATE,false);
		             
		             
		            	//this.jsonInvoiceObject['promptChangingInvoiceNumberDate'] = true;
		            	//this.jsonInvoiceObject['DIRTYFLAG'] = true;
		             
		             if ( ( this.jsonInvoiceObject.OLDINVOICENUMBER  != this.jsonInvoiceObject.INVOICENUMBER )  ||
		            		( date.compare(oldInvoiceDate, currentInvoiceDate)!=0 ) )
		             {
		            	 
		            	this.jsonInvoiceObject['promptChangingInvoiceNumberDate'] = true;
		            	this.jsonInvoiceObject['DIRTYFLAG'] = true;
		            	 
		             	if(this.jsonInvoiceObject.validationPaid){
		             		this.jsonInvoiceObject.DIRTYFLAG= false ;   
		             		dialogMessages.addErrorMessages( "Invoice has a payment against it and cannot be updated.");
		             		this.rollbackIndexing();
		             		callback(dialogMessages);
		             		 return;
		             	}
		             	
		             	if(this.jsonInvoiceObject.validationVarianced){
		             		
		             		dialogMessages.addErrorMessages( "Invoice has been varianced and has a balance of "+this.jsonInvoiceObject.validationNetBalanceStr+". To change invoice number/invoice date unvariance in GAPTS first.");
		             		this.rollbackIndexing();
		             		 callback(dialogMessages);
		             		 return;
		             	}
		             	
		
		             }
		                         
		              
		            	 
		               if(this.jsonInvoiceObject.validationVarianced || this.jsonInvoiceObject.validationPaid){
		            	
		            	   this.setFieldsNotEditable(true);
		               } 
		               
		               this.checkPaid();
		               callback(dialogMessages);
			
				} catch(e) {
					console.log('Error in method ' + arguments.callee.nom + ' - ' + e);
				}
		},
		
		
		/*rollbackIndexing : function() {

			thisForm.serviceParameters['frontEndAction'] = arguments.callee.nom;
			request.postPluginService("GilCnPlugin", "MiscInvoiceService",'application/x-www-form-urlencoded', {
	    		requestBody : ioquery.objectToQuery(this.serviceParameters),
			    requestCompleteCallback : lang.hitch(this, function (response) {
	    			
	    			console.log('Index rolled back successfully.');
		    		
	    		})
			});
		},*/
		
		
		rollbackCountryIndexing : function() {

			thisForm.serviceParameters['frontEndAction'] = arguments.callee.nom;			
			request.postPluginService("GilCnPlugin", "MiscInvoiceService",'application/x-www-form-urlencoded', {
	    		requestBody : ioquery.objectToQuery(this.serviceParameters),
			    requestCompleteCallback : lang.hitch(this, function (response) {
	    			
	    			console.log('Country index rolled back successfully.');
		    		
	    		})
			});
		},
		
		
		setCustomizedValidation : function() {
			
			try {
			
					var thisForm = this;
					
					this.invoiceNumber.set("missingMessage","Invoice Number is a required field.");
					this.invoiceAmountTotal.set("missingMessage","Invoice Total is a required field.");
			
					
					/*lang.getObject('customerName', false, this).validator = function() {
						
						if (lang.getObject('customerName', false, thisForm).get("value") == null ||
								lang.getObject('customerName', false, thisForm).get("value").trim().length == 0  	)	{
		
							lang.getObject('customerName', false, thisForm).set("invalidMessage","Customer Name is a required field.");					
							lang.getObject('customerName', false, thisForm).set("missingMessage","Customer Name is a required field.");
							
							return false;
						}
						
						return true;		
					}*/
					
					
					
					lang.getObject('supplierName', false, this).validator = function() {
						
						if (lang.getObject('supplierName', false, thisForm).get("value").trim().length == 0 )	{
		
							lang.getObject('supplierName', false, thisForm).set("invalidMessage","Vendor Name or Account Number are required fields.");					
							lang.getObject('supplierName', false, thisForm).set("missingMessage","Vendor Name or Account Number are required fields.");
							
							return false;
						}
						
						return true;		
					}
						
					/*lang.getObject('accountNumber', false, this).validator = function() {
						
						if (lang.getObject('supplierName', false, thisForm).get("value").trim().length == 0 )	{
		
							lang.getObject('accountNumber', false, thisForm).set("invalidMessage","Vendor Name or Account Number are required fields.");					
							lang.getObject('accountNumber', false, thisForm).set("missingMessage","Vendor Name or Account Number are required fields.");
							
							return false;
						}
						
						return true;		
					}*/
					
					/*lang.getObject('invoiceDate', false, this).validator = function() {
		
					    var parsedInvoiceDate = locale.parse( thisForm.invoiceDate.get('displayedValue'), {locale:dojo.locale,selector: 'date'});
					    
					    if(parsedInvoiceDate == null){
					    	thisForm.invoiceDate.set("invalidMessage","Invoice Date fomat must have format "+['dateFormat-short']);	
					    }
						if (parsedInvoiceDate!=null) {
							if (date.difference(parsedInvoiceDate,new Date()) > 300	)	{
								thisForm.invoiceDate.set("invalidMessage","Invoice Date must not be greater than 300 days old.");
								return true;
							}
						}

					    var parsedCreatedTimestamp = locale.parse(thisForm.createdTimestamp.get('value'), {
					        datePattern: ValidationConstants("DefaultTimeStampPattern"), 
					        selector: 'date'
					    });
					    
							
					    if ( date.compare(parsedInvoiceDate, parsedCreatedTimestamp, "date") > 0) {
							thisForm.invoiceDate.set("invalidMessage","Scan Date cannot be before the Invoice Date.");					
							return false;
						}
					    
					    return true;
					}*/
					
					// added for defect : 1832048
					lang.getObject('invoiceDate', false, this).validator = function() {
						   var parsedInvoiceDate = thisForm.parseDate(domattr.get(dom.byId(thisForm.id+"_invoiceDate"), "value"), false);

						    if(parsedInvoiceDate == null){
						    	thisForm.invoiceDate.set("invalidMessage","Invoice Date fomat must have format "+ thisForm.gilDatePattern);	
						    }

							var parsedCreatedTimestamp = thisForm.parseTimestamp(thisForm.createdTimestamp.get('value'));
		
						    if ( date.compare(parsedInvoiceDate, parsedCreatedTimestamp, "date") > 0) {
								thisForm.invoiceDate.set("invalidMessage","Scan Date cannot be before the Invoice Date.");					
								return false;
							}
						    
						    return true;
						}
						
					    
				    lang.getObject('invoiceAmountTax', false, this).validator = function() {
					    	
							var decimalRegex = ValidationConstants("decimalRegex");
		
								if(!decimalRegex.test(thisForm.invoiceAmountTax.get('value')))	{
									thisForm.invoiceAmountTax.set("invalidMessage", "Tax amount must be >= 0.00");					
									return false;
								}

								if (thisForm.isTaxAmountGreaterThenTotalAmout()) {
					    			thisForm.invoiceAmountTax.set("invalidMessage","Tax amount must not be greater than Total amount.");
					    			return false;
					    	}
					    	return true;
					    }

			} catch(e) {
				console.log('Error in method ' + arguments.callee.nom + ' - ' + e);
			}
	
			
		},
		
		isTaxAmountGreaterThenTotalAmout: function() {
			
			var res = false;
			
			if (this.invoiceAmountTax.get('value')!=null && this.invoiceAmountTotal.get('value')!=null)	{
				res = MathJs.compare(this.toNumber(this.invoiceAmountTax.get('value')),this.toNumber(this.invoiceAmountTotal.get('value')))>0
			}
			return res;
		},
		
		recalculate: function() {
			
			debugger;
			
			try {
					var total = 0.00;
					
					total =	this.invoiceAmountTotal.get('value');
					var tax = 0.00;
					
					tax =	this.invoiceAmountTax.get('value');
					net = MathJs.subtract(total,tax);
					var vatpercentage = 0.00               
					
					var taxC = this.taxCode.get('value');
					var taxS= taxC.substr(0,2);
					
					//var intTax= parseInt(tax);							
							
				   	
					/*if (this.taxCode.get('value') !=  null && this.taxCode.get('value').trim().length > 0) {
						vatpercentage = this.toNumber(this.jsonInvoiceObject.VATPercentagesStr[this.taxCode.get('value')]);
					}*/					
				   
					if (taxS !=  null && taxS.trim().length > 0) {
						vatpercentage = this.toNumber(this.jsonInvoiceObject.VATPercentagesStr[taxS]);
					}
					
					
					var targetVat = 0.00;
					var hundred = 100.00;
					
					if(MathJs.compare(vatpercentage,0.00)!=0) {
			
		                targetVat = MathJs.multiply(total,vatpercentage);
		                targetVat = MathJs.round(targetVat, 4) 
		                targetVat = MathJs.divide(targetVat,MathJs.add(vatpercentage, hundred));
		                targetVat = MathJs.round(targetVat, 2) 	
					}
					
					net = Math.max(0,net);
					this.netAmount.set('value',this.formatNumber(net,2));
					this.jsonInvoiceObject['NETAMOUNT'] = net.toString();
					
			        this.invoiceAmountTotal.set('value',this.formatNumber(total,2));
			        this.jsonInvoiceObject['TOTALAMOUNT'] = total.toString();
			        
			        this.invoiceAmountTax.set('value',this.formatNumber(tax,2));
			        this.jsonInvoiceObject['VATAMOUNT'] = tax.toString();
			        
			        this.jsonInvoiceObject['VATTARGETAMOUNT'] = targetVat.toString();
			        
			       
        
			} catch(e) {
				console.log('Error in method ' + arguments.callee.nom + ' - ' + e);
			}
			
		},
		
	    isCommissionInvoice :  function(invoiceType) {
	    
	        if (invoiceType!=null && ( (invoiceType.trim() == "COM") || invoiceType.trim() == "STP"))
	        	return true;
	        
	        return false;
	     
	    },
	    
	    isVendorNameChanging :  function() {
	    	
	    	return this.jsonInvoiceObject.VENDORNAME != this.jsonInvoiceObject.OLDVENDORNAME;
	    },
	    
	    isCountryChanging :  function() {
	    	
	    	return this.jsonInvoiceObject.COUNTRY!=this.jsonInvoiceObject.OLDCOUNTRY;
	    },
	    
	    
	   /* isAccountNumberChanging :  function() {
	    
	    	return this.jsonInvoiceObject.ACCOUNTNUMBER != this.jsonInvoiceObject.OLDACCOUNTNUMBER;
	    },*/
	    
	    toNumber: function(num) {

	    	var res = MathJs.eval(num); 
	    	return res;
	    },
	    
	    formatNumber: function(num, precis) {

	    	var res = MathJs.format(  MathJs.round(num, precis),  {notation: 'fixed', precision: precis});
	    	return res;
	    },
	    
		
		setNonEditableFields: function() {	
						
			this.netAmount.set('readonly',true);
		},
		
		
	     isCOMInvoice: function(invoice) {
	    	 
	        if (invoice == 'COM')
	            return true;   
	        
	        return false;
	     },
	    
	     isEXEMPTorStartswithDEVofSRNumber: function() {
	    	 
	    	if( (this.jsonInvoiceObject.SRNUMBER!=null && this.jsonInvoiceObject.SRNUMBER.trim()!="") &&
	    		(this.jsonInvoiceObject.SRNUMBER.trim() == "EXEMPT" || this.jsonInvoiceObject.SRNUMBER.trim().startsWith("DEV")))
	    		return true;
	    	
	    	return false;
	    },
	    

		
		isSupplyDateExhibitionAllowed :  function(country) {
			
	        return (country == 'CZ' || country == 'HU'  || country == 'PL')
		},
		
		
		isTaxInvoiceExhibitionAllowed :  function(country) {
			
	        return (country == 'KR')
		},
		
		isInvoiceSuffixExhibitionAllowed :  function(country) {
			
	        return ((country == 'CL' || country == 'CO' || country == 'MX' || country == 'PE')) 
		},
		
		isProvinceCodesExhibitionAllowed :  function(country) {
			
	        return (country == 'CA') 
		},

		
		hideFieldsForCountry :  function() {		
				
		 
		 this.cmValues = JSON.parse(this.serviceParameters['businessAttributes']);	
		 var country=this.cmValues['countryCode'];
		
			var _thisform = this;
			var _label = null;
			 var _widget = null;	 
			
					 
			
	        if(this.isSupplyDateExhibitionAllowed(country))  {
	        	
				_label =	construct.toDom("  <label  id='${id}_taxSupplyDateLabel' for='taxSupplyDate'>Tax Supply Date</label>&nbsp;");
				
		        _widget	 = new DateTextBox({
		        	id:_thisform.id+"_supplyDate", 
		        	name:"supplyDate",
		        	invalidMessage: "Invoice Date fomat must have format "+_thisform.localeBundle['dateFormat-short'],
		        	constraints:  { min: new Date( 1950, 2, 10),}
		          });
		        
		        var replacebleFieldNumber = this.getAvailableReplacebleField();
		        
		        construct.place(_label, _thisform.id+"_replacebleLabel"+replacebleFieldNumber,"replace");
		        construct.place(_widget.domNode, _thisform.id+"_replacebleField"+replacebleFieldNumber,"replace");
		        
		        registry.byId(_thisform.id+"_supplyDate").set("placeholder", _thisform.localeBundle['dateFormat-short'] );
	        }	        
	        
	      
		        
		        
		        if(this.isTaxInvoiceExhibitionAllowed(country)) {
		        	
					_label =	construct.toDom("<label  id='${id}_taxInvoiceNumberLabel' for='taxInvoiceNumber'>Tax Invoice Number</label>&nbsp;");

					_widget	 = new  ecm.widget.ValidationTextBox({
			        	id:_thisform.id+"_taxInvoiceNumber", 
			        	name:"taxInvoiceNumber",
			        	  maxLength:'24',
			          });
					
					 var replacebleFieldNumber = this.getAvailableReplacebleField();
			        construct.place(_label, _thisform.id+"_replacebleLabel"+replacebleFieldNumber,"replace");
			        construct.place(_widget.domNode, _thisform.id+"_replacebleField"+replacebleFieldNumber,"replace");
		        	
		        }
		        
		        if (this.isInvoiceSuffixExhibitionAllowed(country))  {
		        	
		        	
					_label =	construct.toDom("<label  id='${id}_invoiceSuffixLabel' for='invoiceSuffix'>Invoice Suffix</label>&nbsp;");

			        _widget	 = new ecm.widget.Select({
			        	id:_thisform.id+"_invoiceSuffix", 
			        	name:"invoiceSuffix",
			          });
			        
			        var replacebleFieldNumber = this.getAvailableReplacebleField();
			        construct.place(_label, _thisform.id+"_replacebleLabel"+replacebleFieldNumber,"replace");
			        construct.place(_widget.domNode, _thisform.id+"_replacebleField"+replacebleFieldNumber,"replace");
		        }       
              
		        
		        
			
		},
		
		setPoexandCompCodefieldsDisabled : function(editable){
			
			this.poexCode.set('disabled',editable);
			this.compCode.set('disabled',editable);
			
		},
		
		setFieldsNotEditable :  function(editable)	{
			
			this.cmValues = JSON.parse(this.serviceParameters['businessAttributes']);	
			var country=this.cmValues['countryCode'];
			
			this.invoiceNumber.set('disabled',editable);
			this.invoiceDate.set('disabled',editable);
			this.invoiceAmountTotal.set('disabled',editable);
			this.invoiceAmountTax.set('disabled',editable);
			this.netAmount.set('disabled',editable);
			this.supplierName.set('disabled',editable);
			this.supplierNumber.set('disabled',editable); 			
			this.poexCode.set('disabled',editable);
			this.invoiceCurrency.set('disabled',editable);
			this.invoiceType.set('disabled',editable);
			this.compCode.set('disabled',editable);
			this.taxCode.set('disabled',editable);
			this.dbCr.set('disabled',editable);
			
			
						
			if(this.isSupplyDateExhibitionAllowed(country)) {
				registry.byId(thisForm.id+"_supplyDate").set("disabled", editable );
			}
			
			if(this.isTaxInvoiceExhibitionAllowed(country)){
				registry.byId(thisForm.id+"_taxInvoiceNumber").set("disabled", editable );
			}
			
			if(this.isInvoiceSuffixExhibitionAllowed(country)){
				 registry.byId(thisForm.id+"_invoiceSuffix").set("disabled", editable );
			}
			
			if(this.isProvinceCodesExhibitionAllowed(country)){
				 registry.byId(thisForm.id+"_provinceCode").set("disabled", editable );
			}      
	        

	    
	    },
	    
	    
	    setInvoiceAmountFields :  function(editable) {
	    
	    	this.cmValues = JSON.parse(this.serviceParameters['businessAttributes']);	
			var country=this.cmValues['countryCode'];
			this.invoiceAmountTotal.set('disabled',editable);
			this.invoiceAmountTax.set('disabled',editable);
			this.netAmount.set('disabled',editable);
			this.taxCode.set('disabled',editable);   
	    },
	    
	    setAmountFields :  function(editable){
	    	
	    	this.setInvoiceAmountFields(editable);
	    	this.dbCr.set('disabled',editable);
	    },
	    
	    parseDate: function(dateAux, isISO8601){
	    	
	    	if(isISO8601){
	    		
	    		return stamp.fromISOString(dateAux.toString()).toGMTString();
	    		
	    	} else {
	    	
		    		var parsedDate = locale.parse(dateAux, {
		    			formatLength: "short",
		    			selector: "date"
				});
			    
		    		return parsedDate;
	    	}
	    },
		
		showMessage: function(titl, msg, needConfirmation, callbackYes, callbackNo) {	
			
			thisForm = this;
            var d1 = new NonModalConfirmDialog({
                title: titl, 
                style: "min-width:350px;",
                content:msg,
                name:"dialogMessage"
            })
            
            if(needConfirmation == true){
            	
		        var actionBar = dojo.create("div", {"class": "dijitDialogPaneContentArea",}, d1.containerNode);
		        var actionBar = dojo.create("div", {"class": "dijitDialogPaneActionBar"   }, d1.containerNode);
	
		        new dijit.form.Button({ "label": "Yes",
		            onClick: function(){
		            	
		            	callbackYes();
		            	
		            	d1.destroyRecursive();
		            	
		            }}).placeAt(actionBar);
		        
		        new dijit.form.Button({"label": "No ",
		            onClick: function(){
		            	
		            	callbackNo();		            	
		            	d1.destroyRecursive();		            	
		            	}}).placeAt(actionBar);
            	
            } else if(needConfirmation == false) {
            
			        var actionBar = dojo.create("div", {"class": "dijitDialogPaneContentArea",}, d1.containerNode);
			        var actionBar = dojo.create("div", { "class": "dijitDialogPaneActionBar"  }, d1.containerNode);
		
			        new dijit.form.Button({"label": "Ok",
			            onClick: function(){
			            	
			            	d1.destroyRecursive();
			            	
			            	
			            } }).placeAt(actionBar);
			        
			        new dijit.form.Button({"label": "Cancel",
			            onClick: function(){
			            	
			            	d1.destroyRecursive();
			            
			            	
			            	} }).placeAt(actionBar);
            } else if(needConfirmation == "onlyOk"){            	
            
            	 var actionBar = dojo.create("div", {"class": "dijitDialogPaneContentArea",}, d1.containerNode);
			      var actionBar = dojo.create("div", { "class": "dijitDialogPaneActionBar"  }, d1.containerNode);
		
			        new dijit.form.Button({"label": "Ok",
			            onClick: function(){
			            	
			            	callbackYes();			            	
			            	document.getElementById(d1.id).remove();
			            	d1.destroyRecursive();
			            	
			            } }).placeAt(actionBar);
            	
            	
            }
            
            
	        d1.startup();
            d1.show();
			
		},

			openVendorSearchDialog: function(evt) {
				
				var dialogSupplier = null;
				var buttonClicked =  registry.getEnclosingWidget(evt.target);
				dialogSupplier = new MiscSupplierSearchDialog({	searchType:buttonClicked.searchtype, callerForm:thisForm, serviceParameters: this.serviceParameters  });
				dialogSupplier.showSupplierSearchDataGrid();
			},
			
			openVendorSearchDialogSupplierName: function() {

				var dialogSupplier = null;
				dialogSupplier = new MiscSupplierSearchDialog({	searchType:'supplierName', callerForm:thisForm, serviceParameters: this.serviceParameters  });
				dialogSupplier.showSupplierSearchDataGrid();
			},
			
			openVendorSearchDialogSupplierNumber: function() {
				
				var dialogSupplier = null;
				dialogSupplier = new MiscSupplierSearchDialog({	searchType:'supplierNumber', callerForm:thisForm, serviceParameters: this.serviceParameters  });
				dialogSupplier.showSupplierSearchDataGrid();	
			},
			
			openVendorSearchDialogAccountNumber: function() {

				var dialogSupplier = null;
				dialogSupplier = new MiscSupplierSearchDialog({	searchType:'accountNumber', callerForm:thisForm, serviceParameters: this.serviceParameters  });
				dialogSupplier.showSupplierSearchDataGrid();	
			},
			
			changeDefaultPoexCode : function() {
				
				var defaultPoexCodeForInvoiceTypeCom = null;
				var defaultPoexCode = null;
				
				if (thisForm.jsonInvoiceObject.defaultPoexCodeForinvoiceTypeCom!=null)
					defaultPoexCodeForInvoiceTypeCom = thisForm.jsonInvoiceObject.defaultPoexCodeForinvoiceTypeCom.value;
				
				if(thisForm.jsonInvoiceObject.defaultPoexCode!=null)
					defaultPoexCode = thisForm.jsonInvoiceObject.defaultPoexCode.value;
				
				if(thisForm.invoiceType.get('value') == 'COM' && defaultPoexCodeForInvoiceTypeCom!=null) {
					
					thisForm.poexCode.set("value", defaultPoexCodeForInvoiceTypeCom);
					
				} else if(defaultPoexCode!=null) {
					
					thisForm.poexCode.set("value", defaultPoexCode);
				} else {
					return;
				}
			}
	});
		return MiscInvoiceDialog;		
});
