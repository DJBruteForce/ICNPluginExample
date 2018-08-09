define(["dojo/_base/lang",
        "ecm/model/Action", 
        "ecm/model/Request", 
        "dojo/_base/declare", 
		"ecm/widget/dialog/BaseDialog",
		"dojo/json",
		"gilCnPluginDojo/util/JsonUtils",
		"gilCnPluginDojo/util/MathJs",
		"dojo/text!./templates/InvoiceDialog.html",
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
	    "gilCnPluginDojo/SupplierSearchDialog",
	    "dojox/grid/DataGrid",
	    "dojo/data/ItemFileWriteStore",
	    "gilCnPluginDojo/InvoiceDialogMessages",
	    "dojo/io-query",
	    "dojo/dom-class",
	    "dojo/on", 
	    "dojo/keys",
	    "gilCnPluginDojo/GILDialog",
	    "dojo/fx/Toggler",
	    "dojo/aspect",
	    "ecm/LoggerMixin",
	    "ecm/Logger",
	    "dojo/dom-form"
	    
	],
	
	function(lang, action, request,declare,	BaseDialog,	json,JsonUtils, MathJs, template, array, dom,domReady,form,date,locale,
			domStyle,registry,query,construct,DateTextBox,parser,domattr,AptsDialog,ValidationConstants,ConfirmDialog,
			NonModalConfirmDialog,i18n,	number,Button,ConfirmationDialog,Builder,ECMConfirmationDialog,Tooltip,stamp ,
			focus,connect, event,SupplierSearchDialog,DataGrid,ItemFileWriteStore, InvoiceDialogMessages,ioquery,domclass,
			on,keys,GILDialog,Toggler,aspect,Log,Logger,domForm) {

	/**
	 * @name gilCnPluginDojo.InvoiceDialog
	 * @class Provides a dialog box that is used to edit the properties of a document or folder.
	 * @augments ecm.widget.dialog.BaseDialog
	 */
	var InvoiceDialog = declare("gilCnPluginDojo.InvoiceDialog",[BaseDialog,GILDialog,AptsDialog], {
		
		contentString: template,
		widgetsInTemplate: true,
		jsonInvoiceObject : null,
		cmValues:null,
		localeBundle:null,
		isSupplySearchOpen: false,
		saveBtnId:null,
		addContractBtnId:null,
		defaultsBtnId:null,
		allFormWid:null,
		isCountryIN : false,

		
		postCreate: function() {
		
			var width = 980;
			var height = 550;
			this.inherited(arguments);
			this.setSize(width, height);
			this.setResizable(false);
			this.setMaximized(false);
			this.addButton("Cancel", "cancel", false, true, "cancel");
			this.addButton("Save", "save", false, true, "save");
			this.addButton("Defaults", "defaults", false, true, "defaults");
			this.addButton("Add Contract", "addContract", false, true, "addContract");
			this.setTitle("APTS Index Invoice [" + this.serviceParameters['className'] + "]");
			this.setWidgetsInitialization();
			this.hideFieldsForCountry();
			this.setNonEditableFields();
			this.initializeDateFields();
			this.initializeAmountFields();
			this.upperCaseFields();
			this.setLayout();
			
			//domclass.add(this.dbCr,"disabledDropDown");

			

		},
		
		onChangeIndiaGSTAmtFields: function() {
	    	
		     on(dijit.byId(this.billTo.id), "change", lang.hitch(this,function(event) {
		    	var billToVal = this.billTo.get('displayedValue').trim();
		    	var billFromVal = this.billFrom.get('displayedValue').trim();
		    	var loan = this.loanIndicator.get('value');
		    	this.setBillingAmountFieldsVisibility(billToVal,billFromVal,loan);
		     }));  
		     on(dijit.byId(this.billFrom.id), "change",  lang.hitch(this,function(event) {
			    	var billToVal = this.billTo.get('displayedValue').trim();
			    	var billFromVal = this.billFrom.get('displayedValue').trim();
			    	var loan = this.loanIndicator.get('value');
			    	this.setBillingAmountFieldsVisibility(billToVal,billFromVal,loan);
			     }));
		     on(dijit.byId(this.loanIndicator.id), "change",  lang.hitch(this,function(event) {
			    	var billToVal = this.billTo.get('displayedValue').trim();
			    	var billFromVal = this.billFrom.get('displayedValue').trim();
			    	var loan = this.loanIndicator.get('value');
			    	this.setBillingAmountFieldsVisibility(billToVal,billFromVal,loan);
			     }));
		},
		
		setBillingAmountFieldsVisibility: function(billToVal,billFromVal,loan){
			
	    	if(loan == 'N' && (billToVal != billFromVal)) {
	    		this.showCGST(false);
	    		this.cgst.set('disabled',true);
	    		this.showSGST(false);
	    		this.sgst.set('disabled',true);
	    		this.showIGST(true);
	    		this.igst.set('disabled',false);

	    	} else if(loan != 'N'  && (billToVal == billFromVal)) {
		    		this.showCGST(true);
		    		this.cgst.set('disabled',true);
		    		this.showSGST(true);
		    		this.sgst.set('disabled',true);
		    		this.showIGST(false);
		    		this.igst.set('disabled',true);
	    		
	    	} else if(loan != 'N'  && (billToVal != billFromVal)) {
		    		this.showCGST(false);
		    		this.cgst.set('disabled',true);
		    		this.showSGST(false);
		    		this.sgst.set('disabled',true);
		    		this.showIGST(true);
		    		this.igst.set('disabled',true);
	    	} else {
	    		this.showCGST(true);
	    		this.cgst.set('disabled',false);
	    		this.showSGST(true);
	    		this.sgst.set('disabled',false);
	    		this.showIGST(false);
	    		this.igst.set('disabled',true);
	    	}
    		
	    	this.recalculateINNetAmount();
	    	this.checkPaid();
	    	this.checkWorkAcces();
		},
		
		
		checkPaid: function() {
	    	if(this.jsonInvoiceObject!=null) { 
	    		if(this.isFullyPaid() || this.isPartiallyPaid() || this.isVarianced() ){
	         	   this.setFieldsNotEditable(true);
	            }
	    	}
		},
		
		checkWorkAcces: function() {
			if(this.jsonInvoiceObject!=null) { 
		        if (this.jsonInvoiceObject.isWorkAcess) {
	    			this.setAmountFieldNotEditable(true);
	    		}
			}
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
			
    		this.isNumberKey(dijit.byId(this.id+"_invoiceAmountTotal"));
    		this.isNumberKey(dijit.byId(this.id+"_invoiceAmountTax"));
    		this.isNumberKey(dijit.byId(this.id+"_igst"));
    		this.isNumberKey(dijit.byId(this.id+"_cgst"));
    		this.isNumberKey(dijit.byId(this.id+"_sgst"));
		},

		
		setLayout:function(){
			var restoring = (restore) => {
				if(restore != null){

					if(!domclass.contains(this.actionBar,"invoiceNonElsEcmDialogPaneActionBar")){			
						domclass.add(this.actionBar,"invoiceNonElsEcmDialogPaneActionBar");
					}

				}
			};

			var maximizing = (maximize) => {
				if(maximize != null){

					if(domclass.contains(this.actionBar,"invoiceNonElsEcmDialogPaneActionBar")){			
						domclass.remove(this.actionBar, 'invoiceNonElsEcmDialogPaneActionBar');
					}

				}
			};

			aspect.after(this._maximizeButton, 'onClick', maximizing, this._maximizeButton);
			aspect.after(this._restoreButton, 'onClick', restoring, this._restoreButton);
			domclass.add(this.actionBar,"invoiceNonElsEcmDialogPaneActionBar");
		}, 
		
		
		setWidgetsInitialization: function() {

		    dojo.style(this.cancelButton.domNode,"display","none"); 

    		registry.byId(this.id+'_supplierName').set('_onChangeActive', false);
    		registry.byId(this.id+'_supplierNumber').set('_onChangeActive', false);
    		registry.byId(this.id+'_accountNumber').set('_onChangeActive', false);
       		registry.byId(this.id+'_invoiceType').set('_onChangeActive', false);
       		
    		this.saveBtnId = registry.byId(dojo.query('[id^="save"]')[0].id);
    		this.addContractBtnId = registry.byId(dojo.query('[id^="addContract"]')[0].id);;
    		this.defaultsBtnId = registry.byId(dojo.query('[id^="defaults"]')[0].id);
    		
    		
    		this.saveBtnId = registry.byId(dojo.query('[id^="save"]')[0].id);
    		this.addContractBtnId = registry.byId(dojo.query('[id^="addContract"]')[0].id);;
    		this.defaultsBtnId = registry.byId(dojo.query('[id^="defaults"]')[0].id);
    		
    		this.isCountryIN = this.isIndiaGSTFieldsAllowed(this.serviceParameters['country']);
    		
			this.showIGST(false)
			this.showCGST(false);
    		this.showSGST(false)
    		this.showGstRegNumLoadIndicator(false);
    		this.showBillToBillFromShipTo(false);
    		

		},
		
		disableCommonFieldsForIndiaGST: function(){
			
    	    dojo.style(this.accountNumberTr,"display","none");
    		dojo.style(this.taxCodeTd,"display","none");
    		dojo.style(this.taxCodeLabelTd,"display","none");
    		dojo.style(this.taxCodeReplacebleTr,"display","none");	
	    	dojo.style(this.taxTr,"display","none");
		},
		
		
		
		setIndiaDstFieldsVisible: function(editable,visible){
			
			this.showGstRegNumLoadIndicator(true);
			this.showBillToBillFromShipTo(true);
			
			if (editable) {
				if (visible){
					this.showIGST(false)
					this.igst.set('disabled',true);
					
					this.showCGST(true);
		    		this.cgst.set('disabled',false);
		    		
		    		this.showSGST(true)
		    		this.sgst.set('disabled',false);
		    		
				} else {
					this.showIGST(true)
		    		this.igst.set('disabled',false);
					
					this.showCGST(false);
		    		this.cgst.set('disabled',true);
		    		
		    		this.showSGST(false)
		    		this.sgst.set('disabled',true);
				}
				
			} else {
				if(visible){
					
					this.showIGST(true)
		    		this.igst.set('disabled',true);
					
					this.showCGST(true);
		    		this.cgst.set('disabled',false);
		    		
		    		this.showSGST(true)
		    		this.sgst.set('disabled',false);

				} else {
					
					this.showIGST(true)
		    		this.igst.set('disabled',true);
	    			
					this.showCGST(false);
		    		this.cgst.set('disabled',true);
		    		
		    		this.showSGST(false)
		    		this.sgst.set('disabled',true);  
	    			
				}
			}	
		},
		

		defaults: function(e) {
			
			if (this.isDoubleClick(this.defaultsBtnId)) return;
			
			console.log(arguments.callee.nom + ' was clicked:' + this.actionClick + ' time(s).');
			this.serviceParameters['frontEndAction'] = arguments.callee.nom;
			this.serviceParameters['jsonDataObject']	= JSON.stringify(this.jsonInvoiceObject);
			var jsonUtils = new JsonUtils();
			
			request.postPluginService("GilCnPlugin", "InvoiceService",'application/x-www-form-urlencoded', {
	    		requestBody : ioquery.objectToQuery(this.serviceParameters),
			    requestCompleteCallback : lang.hitch(this, function (response) {
	    			
	    			this.jsonInvoiceObject	= JSON.parse(response.invoiceJsonString);
	    			this.setFieldsNotEditable(false);
	    			this.supplierName.set("value",this.jsonInvoiceObject.VENDORNAME,false);
	    			this.invoiceAmountTotal.set("value","");
	    			this.invoiceAmountTotal.set("value",this.jsonInvoiceObject.TOTALAMOUNT.replace(/,/g, '.'));
	    			this.supplierNumber.set("value",this.jsonInvoiceObject.VENDORNUMBER);
	    			this.accountNumber.set("value",this.jsonInvoiceObject.ACCOUNTNUMBER);
	    			this.customerName.set("value",this.jsonInvoiceObject.CUSTOMERNAME);
	    			this.dbCr.set("options", this.jsonInvoiceObject.dbCrSelectList);
	    			this.dbCr.set("value", this.jsonInvoiceObject.DBCR);
		    		this.invoiceCurrency.set("options", this.jsonInvoiceObject.currencyCodeSelectList);
		    		this.invoiceCurrency.set("value", this.jsonInvoiceObject.CURRENCY);
		    		this.invoiceType.set("options", this.jsonInvoiceObject.invoiceTypesSelectList);
		    		this.invoiceType.set("value", this.jsonInvoiceObject.INVOICETYPE);
		    		this.enableClick(this.defaultsBtnId);
	    		})
			});
		},
		
		
		performCancel: function() {
			
			try {

				if (thisForm.jsonInvoiceObject['DIRTYFLAG']){
					
					thisForm.serviceParameters['frontEndAction'] = "rollbackIndexing";
					request.postPluginService("GilCnPlugin", "InvoiceService",'application/x-www-form-urlencoded', {
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
		
		cancel: function(dirtCheck) {
			
			var dlgMsg = new InvoiceDialogMessages();
			try {
				if(dirtCheck) {
					if( thisForm.isFormDirty(thisForm.id+"_invoiceForm") || thisForm.jsonInvoiceObject['DIRTYFLAG']){
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
		
		
		addContract: function() {
			
			this.serviceParameters['frontEndAction'] = arguments.callee.nom;
			var dialogMessages = null;
			if (this.isDoubleClick(this.addContractBtnId) ) return;
			
			console.log(arguments.callee.nom + ' was clicked:' + this.actionClick + ' time(s).');

			try {

				if (!this.invoiceForm.validate()){
					
					this.enableClick(this.saveBtnId);	
					this.enableClick(this.addContractBtnId);
			        return false;
			    }
	
				this.setValues();
				this.serviceParameters['jsonDataObject']	= JSON.stringify(this.jsonInvoiceObject);
				thisForm = this;
				dialogMessages = this.validateInput(this.performAddContract);
				console.log("Invoice JSON being submitted: "+JSON.stringify(this.jsonInvoiceObject, null, 2));
					
			} catch(e) {
				console.log('Error in method ' + arguments.callee.nom + ' - ' + e);
				this.enableClick(this.addContractBtnId);
			}
		},
		
		
		
		performAddContract: function(dialogMessages,fieldToFocus) {
			
		 try {
			
			if(dialogMessages.hasErrorMessages()) {
					
	    			thisForm.showMessage("Validation",dialogMessages.getAllMessages(),false,null,null,fieldToFocus);
				 	this.enableClick(thisForm.saveBtnId);	
				 	this.enableClick(thisForm.addContractBtnId );
	    			return false;  
			
			} else if(thisForm.jsonInvoiceObject.promptInvoiceSuppyExists == true || thisForm.jsonInvoiceObject.promptWrongDateDiff== true ||
				    thisForm.jsonInvoiceObject.promptTaxInvoiceNumber== true ||	 thisForm.jsonInvoiceObject.promptRoundHUF == true || thisForm.jsonInvoiceObject.promptDatePopup == true || thisForm.jsonInvoiceObject.promptSupplyDatePopup == true) {
				
						thisForm.startPromptingOn('addContract');
						
			} else {

    			thisForm.enableClick(thisForm.saveBtnId);	
    			thisForm.enableClick(thisForm.addContractBtnId );
				thisForm.submitConfirmationYes('addContract');

			}
			
		 }catch(e){
				console.log('Error in method ' + arguments.callee.nom + ' - ' + e);
				thisForm.enableClick(thisForm.saveBtnId);	
				thisForm.enableClick(thisForm.addContractBtnId );
			} finally{

			}
		},
		
		
		setValues: function() {
			
		    this.jsonInvoiceObject['validationIsOfferLetterInCM'] = true;
		    this.jsonInvoiceObject['validationIsInvoiceNumberChanging'] = false;
		    this.jsonInvoiceObject['validationIsDocumentIndexedProperly'] = false;
		    this.jsonInvoiceObject['validationIsDuplicatedInvoice'] = false;
		    this.jsonInvoiceObject['validationTaxAmountEqualTargetvatQuestion'] = false;
        	//this.jsonInvoiceObject['validationPaid'] = true;
        	this.jsonInvoiceObject['validationVarianced'] = true;
        	this.jsonInvoiceObject['validationIsContractFolderCreated'] = true;
        	this.jsonInvoiceObject['validationIsContractCreated'] = true;
		
			this.jsonInvoiceObject['INVOICENUMBER'] = this.invoiceNumber.get('value');
			this.jsonInvoiceObject['ACCOUNTNUMBER'] = this.accountNumber.get('value');
			this.jsonInvoiceObject['CONTRACTNUMBER'] = this.contractNumber.get('value');
			this.jsonInvoiceObject['CUSTOMERNUMBER'] = this.customerNumber.get('value');
			this.jsonInvoiceObject['CUSTOMERNAME'] = this.customerName.get('value');
			this.jsonInvoiceObject['INVOICEDATE'] = this.invoiceDate.get('displayedValue');
			this.jsonInvoiceObject['TOTALAMOUNT'] = this.invoiceAmountTotal.get('value');
			this.jsonInvoiceObject['VATAMOUNT'] = this.invoiceAmountTax.get('value');
			this.jsonInvoiceObject['NETAMOUNT'] = this.netAmount.get('value');
			this.jsonInvoiceObject['OFFERINGLETTER'] = this.offeringLetterNumber.get('value');
			this.jsonInvoiceObject['VENDORNAME'] = this.supplierName.get('value');
			this.jsonInvoiceObject['VENDORNUMBER'] = this.supplierNumber.get('value');
			this.jsonInvoiceObject['OCR'] = this.ocrKid.get('value');
			this.jsonInvoiceObject['CURRENCY'] = this.invoiceCurrency.get('value');
			this.jsonInvoiceObject['INVOICETYPE'] = this.invoiceType.get('value');
			this.jsonInvoiceObject['VATCODE'] = this.taxCode.get('value');
			this.jsonInvoiceObject['COMPANYCODE'] = this.compCode.get('value');
			this.jsonInvoiceObject['POEXCODE'] = this.poexCode.get('value');
			this.jsonInvoiceObject['DBCR'] = this.dbCr.get('value');
			
			
			
			if (dojo.byId(this.id+"_distrNum")!=null){
				this.jsonInvoiceObject['DISTRIBUTORNUMBER'] 	= registry.byId(this.id+"_distrNum").get('value');
			}
			
    		if (this.isCAICAEFieldsAllowed(this.serviceParameters['country'])){
    			
    			this.jsonInvoiceObject['CAICAEDate'] = domattr.get(dom.byId(thisForm.id+"_caicaeDueDate"), "value");
    			this.jsonInvoiceObject['CAICAE'] 	= registry.byId(this.id+"_caicae").get('value');
    		}
		
    		if (this.isSilentCOAExhibitionAllowed(this.serviceParameters['country'])) {
	    		this.jsonInvoiceObject['SILENTCOA'] = registry.byId(this.id+"_silentCoa").get('value');	
    		}
    		
    		if (this.isTaxInvoiceExhibitionAllowed(this.serviceParameters['country'])) {
    			this.jsonInvoiceObject['TAXINVOICENUMBER'] = registry.byId(this.id+"_taxInvoiceNumber").get('value');	
    		}
    		
    		if (this.isInvoiceSuffixExhibitionAllowed(this.serviceParameters['country'])) {
	    		this.jsonInvoiceObject['INVOICESUFFIX'] = registry.byId(this.id+"_invoiceSuffix").get('value');
    		}	
    		 
    		if (this.isSupplyDateExhibitionAllowed(this.serviceParameters['country'])) {
    			this.jsonInvoiceObject['TAXSUPPLYDATE'] = domattr.get(dom.byId(thisForm.id+"_supplyDate"), "value");
    		}
    		
    		if(this.isCountryIN && this.jsonInvoiceObject.EFEDATE == 'Y'){

    			this.jsonInvoiceObject['BILLTO'] = this.billTo.get("value");
    			this.jsonInvoiceObject['BILLFROM'] = this.billFrom.get("value");
    			this.jsonInvoiceObject['SHIPTO'] = this.shipTo.get("value");
    			this.jsonInvoiceObject['DISTRIBUTORNUMBER'] = this.loanIndicator.get("value");
    			this.jsonInvoiceObject['INIGST'] = this.igst.get("value");
    			this.jsonInvoiceObject['INCGST'] = this.cgst.get("value");
    			this.jsonInvoiceObject['INSGST'] = this.sgst.get("value");
    			this.jsonInvoiceObject['SUPPGSTREGNUMBER'] = this.supplierGSTRegNumber.get("value");
    			this.jsonInvoiceObject['LOAN'] = this.loanIndicator.get("value");

    		}
			
		},
		
		save: function() {
			
			if (this.isDoubleClick(this.saveBtnId)) return;
			
			this.serviceParameters['frontEndAction'] = arguments.callee.nom;
			var dialogMessages = null;

			try {
					if (!this.invoiceForm.validate()){
						console.log('Errors found on screen.');
						this.enableClick(this.saveBtnId);
			            return false;
					} 
			    
					this.setValues();
					this.serviceParameters['jsonDataObject']	= JSON.stringify(this.jsonInvoiceObject);
					thisForm = this;
					dialogMessages = this.validateInput(this.performSave);
					console.log("Invoice JSON being submitted: "+JSON.stringify(this.jsonInvoiceObject, null, 2));
					
			} catch(e) {
				console.log('Error in method ' + arguments.callee.nom + ' - ' + e);	
				thisForm.enableClick(thisForm.saveBtnId);
			} 
		},

		
		/**
		 * Method : validateInput
		 * 
		 * usage : this method will trigget when user tap on save or addcontact buttons. 
		 * 
		 * */
		validateInput: function(perfomSaveCallback) {
			
			this.serviceParameters['frontEndAction'] = "validateOnSave";
			var dialogMessages = new InvoiceDialogMessages();
			
			try {
					
					thisForm = this;
					var jsonUtils = new JsonUtils();
					var NinetyNinePointNineNineNine = 99.999;
					
					this.jsonInvoiceObject['promptInvoiceSuppyExists'] = false;
					this.jsonInvoiceObject['promptWrongDateDiff'] = false;
					this.jsonInvoiceObject['promptTaxInvoiceNumber'] = false;
					this.jsonInvoiceObject['promptRoundHUF'] = false;
					this.jsonInvoiceObject['promptDatePopup'] = false;
					this.jsonInvoiceObject['promptSupplyDatePopup'] = false;
					
					request.postPluginService("GilCnPlugin", "InvoiceService",'application/x-www-form-urlencoded', {
				    		requestBody : ioquery.objectToQuery(thisForm.serviceParameters),
						    requestCompleteCallback : lang.hitch(this, function (response) {
					    			
					    			this.jsonInvoiceObject	= JSON.parse(response.invoiceJsonString);
					    			
/*									if(this.jsonInvoiceObject.dialogErrorMsgs.length>0){
										var msgs=thisForm.jsonInvoiceObject.dialogErrorMsgs;
										for (var i in msgs) {
											dialogMessages.addErrorMessages(msgs[i]);
										}
									} 
				    			
					    			if (dialogMessages.hasErrorMessages()) {
										perfomSaveCallback(dialogMessages);
										return
					    			}*/ 
					    			
					    			
					    			
									var invTotalNum = this.toNumber(this.invoiceAmountTotal.get("value"));
									var invTotalStr = this.invoiceAmountTotal.get("value");
									if ( invTotalNum==null || invTotalNum == undefined || MathJs.compare(invTotalNum,0) <=0  )	 {		
										dialogMessages.addErrorMessages("Invoice Amount must be > 0.00");	
										perfomSaveCallback(dialogMessages);
										return;
									}
							        
							        
									var decimalRegex = ValidationConstants("decimalRegex");
									
									if(!decimalRegex.test(thisForm.invoiceAmountTax.get('value')))	{				
										dialogMessages.addErrorMessages("Tax amount must be >= 0.00");
										perfomSaveCallback(dialogMessages,dom.byId(thisForm.id+"_invoiceAmountTax"));
										return;
									}

									if (thisForm.isTaxAmountGreaterThenTotalAmout()) {
										dialogMessages.addErrorMessages("Tax amount must not be greater than Total amount.");
										perfomSaveCallback(dialogMessages,dom.byId(thisForm.id+"_invoiceAmountTax"));
										return;
									}
									
					    			if (!this.jsonInvoiceObject.validationIsOfferLetterValidHybr) {
					    				dialogMessages.addErrorMessages("Error validating Offering Letter.");
					    				perfomSaveCallback(dialogMessages,dom.byId(thisForm.id+"_offeringLetterNumber"));
										return;
					    			}
						    			
					    			if(this.serviceParameters['country'] == 'KR' && this.jsonInvoiceObject.TAXINVOICENUMBER.trim().length == 0) {
					    				this.jsonInvoiceObject['promptTaxInvoiceNumber'] = true;	
					    			}
					    			
					    			if (this.taxCode.get('value') ==  null || this.taxCode.get('value').trim().length == 0) {
					    				dialogMessages.addErrorMessages("Tax Code is a required field.");
										perfomSaveCallback(dialogMessages);
										return;
					    			}
					    			
					    			if (this.invoiceCurrency.get('value') ==  null || this.invoiceCurrency.get('value').trim().length == 0) {
					    				dialogMessages.addErrorMessages("Currency is a required field.");
										perfomSaveCallback(dialogMessages);
										return;
					    			}
					    			
					    			if (!this.jsonInvoiceObject.validationIsOfferLetterInCM) {
					    				dialogMessages.addErrorMessages("Offering Letter Number not found in Content Manager.");
					    				perfomSaveCallback(dialogMessages,dom.byId(thisForm.id+"_offeringLetterNumber"));
										return;
					    			}
					    			
					    			if (this.jsonInvoiceObject.validationIsInvoiceNumberChanging &&
					    				this.jsonInvoiceObject.validationIsDuplicatedInvoice) {
					    				dialogMessages.addErrorMessages("Duplicate Invoice entered.");
					    				perfomSaveCallback(dialogMessages,dom.byId(thisForm.id+"_invoiceNumber"));
										return;
					    				
					    			} else if ( this.jsonInvoiceObject.validationIsInvoiceNumberChanging == true &&
					    					   this.jsonInvoiceObject.validationIsDuplicatedInvoice == false) {
					    				
					    						this.jsonInvoiceObject['promptInvoiceSuppyExists'] = true;
					    				
					    			}else if ( this.jsonInvoiceObject.validationIsInvoiceNumberChanging == false &&
					    					   this.jsonInvoiceObject.validationIsDuplicatedInvoice == true) {
					    				
					    					dialogMessages.addErrorMessages("Duplicate Invoice entered.");
					    					perfomSaveCallback(dialogMessages,dom.byId(thisForm.id+"_invoiceNumber"));
											return;
					    			}
					    			
					    			if (!this.jsonInvoiceObject.validationIsDocumentIndexedProperly) {
					    				
					    				dialogMessages.addErrorMessages("Document did not index correctly.");
										perfomSaveCallback(dialogMessages);
										return;
					    			}
					    			
							        if ( this.invoiceType.get('value').startsWith("RBD") && (this.dbCr.get('value') != 'CR') ){
							        	
							        	dialogMessages.addErrorMessages("RBD invoice type must be CR.");
										perfomSaveCallback(dialogMessages,dom.byId(thisForm.id+"_dbCr"));
										return;
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
										 perfomSaveCallback(dialogMessages);
										 return;
						            }
						        }

					       
					        if(this.isCOMInvoice(this.jsonInvoiceObject.INVOICETYPE) && this.isEXEMPTorStartswithDEVofSRNumber()) {
					        	
				            	 dialogMessages.addErrorMessages("Commission Invoices are not valid for this supplier.");
								 perfomSaveCallback(dialogMessages);
								 return;
					        }

					        if ((this.jsonInvoiceObject.OCRREQUIRED=='R') && (this.ocrKid.get('value')==null || this.ocrKid.get('value').trim() == '')) {
					        	
					        	dialogMessages.addErrorMessages("OCR/KID is a required field.");
			    				perfomSaveCallback(dialogMessages,dom.byId(thisForm.id+"_ocrKid"));
								return;
					        }   
					            
					        if(this.serviceParameters['country'] == 'BR' && this.compCode.get('value') == '0001' && this.invoiceType.get('value') != 'IBM') {
					    
					        	dialogMessages.addErrorMessages("Please select Invoice Type of IBM for the Comp Code of 0001.");
								perfomSaveCallback(dialogMessages,dom.byId(thisForm.id+"_invoiceType"));
								return;
					        } 
					        
					        
					        if (this.isSupplyDateExhibitionAllowed(this.serviceParameters['country']) ){
					        	
					        	var countryPL = this.serviceParameters['country'] == 'PL';
					           	var supplyDate = domattr.get(dom.byId(thisForm.id+"_supplyDate"), "value");
					           	var isSupplyDateValid = true;
					           	
					           	try {
					           		
					           		var parsedSupplyDate = this.parseDate(domattr.get(dom.byId(thisForm.id+"_supplyDate"), "value"), false);
								    if (parsedSupplyDate == null) {
								    	isSupplyDateValid = false;
								    }
							    
					           	} catch(e) {
					           		isSupplyDateValid = false;
					           	}
					        	
					        	if (!countryPL || supplyDate.length > 0) {
					        		
						        	if(!isSupplyDateValid)	{
							        		
							        		dialogMessages.addErrorMessages("Tax Supply Date is a required field and  must have format "+ this.gilDatePattern);
											perfomSaveCallback(dialogMessages,dom.byId(thisForm.id+"_supplyDate"));
											return;
							        		
						        	} /*else {
						        		
										if (date.difference(parsedSupplyDate,new Date()) > 300	)	{
											
											dialogMessages.addErrorMessages("Supply Date must not be greater than 300 days old.");
											perfomSaveCallback(dialogMessages);
											return;
											
										}
						        	}*/
					        	}
					        }
					        
					        if (this.isCAICAEFieldsAllowed(this.serviceParameters['country']) ){
					        	
					           	var caicaeDueDate = domattr.get(dom.byId(thisForm.id+"_caicaeDueDate"), "value");
					        	var isCaicaeDueDateValid = true;
					           	if(caicaeDueDate==null || caicaeDueDate.trim() == ''){
							        dialogMessages.addErrorMessages("CAI/CAE due Date is a required field");
									perfomSaveCallback(dialogMessages);
									return;
					           	}

					           	try {
					           		
					           		var parsedcaicaeDueDate = this.parseDate(domattr.get(dom.byId(thisForm.id+"_caicaeDueDate"), "value"), false);
								    if (parsedcaicaeDueDate == null) {
								    	isCaicaeDueDateValid = false;
								    }
							    
					           	} catch(e) {
					           		isCaicaeDueDateValid = false;
					           	}
	
						        if(!isCaicaeDueDateValid)	{	
							        dialogMessages.addErrorMessages("CAI/CAE due Date is a required field and must have format "+ this.gilDatePattern);
									perfomSaveCallback(dialogMessages);
									return;	
						        } 
					        
					        }
					        
					        if(this.serviceParameters['country'] == "NO" &&  ( this.ocrKid.get('value')!=null && this.ocrKid.get('value').trim())){
					        	
					        	val = this.ocrKid.get('value').trim();
								var digitsOnlyRegex = ValidationConstants("digitsOnlyRegex");
								if(!digitsOnlyRegex.test(val)) {
									dialogMessages.addErrorMessages("OCR/KID field must contain numbers only");
									perfomSaveCallback(dialogMessages,dom.byId(thisForm.id+"_ocrKid"));
									return;
								}
					        }
					        
							

							
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
									this.jsonInvoiceObject['promptRoundHUF'] = true;
								}
								
							}
					        
					        
					        if(!(this.isCountryIN && this.jsonInvoiceObject.EFEDATE == 'Y')){
						        
						        var vatamount = 0.00;
						        vatamount = this.jsonInvoiceObject.VATAMOUNT;
						        var targetVat = this.toNumber(this.jsonInvoiceObject.VATTARGETAMOUNT);
						        var vatVariance = this.toNumber(this.jsonInvoiceObject.VATVarianceStr);      
						        var vatDiff  = MathJs.abs(MathJs.subtract(targetVat,vatamount));
						        
						        if (MathJs.compare(vatDiff,vatVariance)>0 ){
						        	
						        	
						        	if (this.jsonInvoiceObject.NETEQBAL == 'Y' || this.jsonInvoiceObject.DOCUMENTMODE == 1){

						        				this.jsonInvoiceObject['VATAMOUNT'] = this.formatNumber(targetVat,2);			        				
						        				this.jsonInvoiceObject['promptWrongDateDiff'] = true;
						        				this.jsonInvoiceObject['WRONGVATDIFF'] = true;

						        	}
						        	
						       		if(this.jsonInvoiceObject.DOCUMENTMODE == 3){
						       			
					                	var invVarAmt = this.toNumber(this.jsonInvoiceObject.INVVARAMT);
					                	var invBalAmt = this.toNumber(this.jsonInvoiceObject.INVBALAMT);
					                	
					                	if(MathJs.compare(invVarAmt,0.00) > 0 && MathJs.compare(invBalAmt,0.00) == 0  ){
					                		dialogMessages.addWarningMessages("Invoice has been varianced and has a balance of 0.00 . To change invoice amount unvariance in GAPTS first");
					                		
					                	}
					                	if(MathJs.compare(invVarAmt,0.00) > 0 && MathJs.compare(invBalAmt,0.00) > 0  ){
					                		dialogMessages.addWarningMessages("Invoice has been varianced and has a balance of "+invBalAmt+". To change invoice amount unvariance in GAPTS first");
					                		
					                	}
					                	if(MathJs.compare(invVarAmt,0.00) == 0 && MathJs.compare(invBalAmt,0.00) == 0  ){
					                		dialogMessages.addErrorMessages("Invoice has a balance of 0.00 and cannot be re-indexed");
					                		
					                    }
					                }
						        }
						        /***
							     *	Defect : 	1774555  
							     *  Usage : 	Alert to user while Re-indexing partially paid Invoices
							     * 
							     * */
						        else if(this.jsonInvoiceObject.DOCUMENTMODE == 3){
					       		
				                	var invTotalAmt = this.toNumber(this.jsonInvoiceObject.validationNetAmountStr);
				                	var invBalAmt = this.toNumber(this.jsonInvoiceObject.validationNetBalanceStr);
				                	
									if(MathJs.compare(invTotalAmt,invBalAmt) > 0)
									 {
										dialogMessages.addErrorMessages("Invoice has a payment against it and cannot be updated.");  
										
									 }
						    	}
					        } else {
			        	
				    			if (this.billTo.get('value') ==  null || this.billTo.get('value').trim().length == 0) {
				    				dialogMessages.addErrorMessages("Bill To is a required field");
									perfomSaveCallback(dialogMessages);
									return false;
				    			}
				    			
				    			if (this.billFrom.get('value') ==  null || this.billFrom.get('value').trim().length == 0) {
				    				dialogMessages.addErrorMessages("Bill From is a required field");
									perfomSaveCallback(dialogMessages);
									return false;
				    			}
				    			
				    			if (this.supplierGSTRegNumber.get('value') ==  null || this.supplierGSTRegNumber.get('value').trim().length == 0) {
				    				dialogMessages.addErrorMessages("Supplier GST Registration Number is a required field");
									perfomSaveCallback(dialogMessages);
									return false;
				    			}
					        }
					        
							if(this.jsonInvoiceObject.dialogErrorMsgs.length>0){
								var msgs=this.jsonInvoiceObject.dialogErrorMsgs;
								for (var i in msgs) {
									dialogMessages.addErrorMessages(msgs[i]);
								}
								perfomSaveCallback(dialogMessages);
								return;
							} 
							
					        perfomSaveCallback(dialogMessages);
		    		})
					});

			}catch(e){
				console.log('Error in method ' + arguments.callee.nom + ' - ' + e);
				this.enableClick(this.saveBtnId);
				this.enableClick(this.addContractBtnId );
			}
		},
		
		
		performSave: function(dialogMessages,fieldToFocus) {
			
			try {
			
			 if(dialogMessages.hasErrorMessages()) {
				 
				 	thisForm.enableClick(thisForm.saveBtnId);	
	    			thisForm.showMessage("Validation",dialogMessages.getAllMessages(),false,null,null,fieldToFocus);
	    			
	    			return false;  
			
			} else if(thisForm.jsonInvoiceObject.promptInvoiceSuppyExists == true || thisForm.jsonInvoiceObject.promptWrongDateDiff== true ||
				    thisForm.jsonInvoiceObject.promptTaxInvoiceNumber== true ||	 thisForm.jsonInvoiceObject.promptRoundHUF == true || thisForm.jsonInvoiceObject.promptDatePopup == true || thisForm.jsonInvoiceObject.promptSupplyDatePopup == true) {
				
						thisForm.startPromptingOn('save');
						
			} else {
				
    			thisForm.enableClick(thisForm.saveBtnId);	
    			thisForm.enableClick(thisForm.addContractBtnId );
				thisForm.submitConfirmationYes('save');

			}
			}catch(e){
				console.log('Error in method ' + arguments.callee.nom + ' - ' + e);
				thisForm.enableClick(thisForm.saveBtnId);
			} 
		},
		
		startPromptingOn: function(action) {
			
			var dialogMsgs = new InvoiceDialogMessages();
			
			if(thisForm.jsonInvoiceObject.promptWrongDateDiff) {
				dialogMsgs.addConfirmMessages("Tax Amount must equal " +  this.jsonInvoiceObject.VATTARGETAMOUNT + " +/- $" + this.jsonInvoiceObject.VATVarianceStr + ".\nDo you wish to correct?");
				thisForm.showMessage("Confirm",dialogMsgs.confirmMessages(),true ,thisForm.promptYesForWrongVatDiff.bind(null,action), thisForm.promptNoForWrongVatDiff.bind(null,action));
				
			} else 	if(thisForm.jsonInvoiceObject.promptInvoiceSuppyExists) {
					   dialogMsgs.addConfirmMessages("Invoice number/Supplier number already exists, Do you wish to change the invoice number?");
					   thisForm.showMessage("Confirm",dialogMsgs.confirmMessages(),true ,thisForm.promptYesForInvoiceSuppyExists.bind(null,action), thisForm.promptNoForInvoiceSuppyExists.bind(null,action));
				
			} else if(thisForm.jsonInvoiceObject.promptTaxInvoiceNumber) {
					 dialogMsgs.addConfirmMessages("Tax Invoice Number is not entered and do you want to add?");
					 thisForm.showMessage("Confirm",dialogMsgs.confirmMessages(),true ,thisForm.promptYesForTaxInvoiceNumber.bind(null,action),thisForm.promptNoForTaxInvoiceNumber.bind(null,action));

			} else if(thisForm.jsonInvoiceObject.promptRoundHUF) {
					 dialogMsgs.addWarningMessages("HUF amounts must be rounded to the nearest dollar. Do you wish to correct?");
					 thisForm.showMessage("Warning",dialogMsgs.warningMessages(),true ,thisForm.promptYesForHufRound.bind(null,action), thisForm.promptNoForHufRound.bind(null,action));

			} else if(thisForm.jsonInvoiceObject.promptDatePopup) {
					 dialogMsgs.addWarningMessages("Invoice Date is greater than 300 days old and specific approvals may apply for processing. Do you wish to correct?");
					 thisForm.showMessage("Warning",dialogMsgs.warningMessages(),true ,thisForm.promptYesForDatePopup.bind(null,action), thisForm.promptNoForDatePopup.bind(null,action));

			} else if(thisForm.jsonInvoiceObject.promptSupplyDatePopup) {
					 dialogMsgs.addWarningMessages("Tax Supply Date is greater than 300 days old and specific approvals may apply for processing. Do you wish to correct?");
					 thisForm.showMessage("Warning",dialogMsgs.warningMessages(),true ,thisForm.promptYesSupplyDatePopup.bind(null,action), thisForm.promptNoForSupplyDatePopup.bind(null,action));
			} else {
				thisForm.enableClick(registry.byId(dojo.query('[id^="'+action+'"]')[0].id));
				thisForm.submitConfirmationYes(action);
			}
		},
		
		
		promptNoForSupplyDatePopup: function(action) {
			
			thisForm.jsonInvoiceObject.promptSupplyDatePopup = false;
			thisForm.startPromptingOn(action);
		},
		
		promptYesSupplyDatePopup: function(action) {
			
			focus.focus(dom.byId(thisForm.id+"_supplyDate"));
			thisForm.enableClick(registry.byId(dojo.query('[id^="'+action+'"]')[0].id));
		},
		
		promptNoForDatePopup: function(action) {
			
			thisForm.jsonInvoiceObject.promptDatePopup = false;
			thisForm.startPromptingOn(action);
		},
		
		promptYesForDatePopup: function(action) {
			
			focus.focus(dom.byId(thisForm.id+"_invoiceDate"));
			thisForm.enableClick(registry.byId(dojo.query('[id^="'+action+'"]')[0].id));
		},
		
		
		
		promptNoForWrongVatDiff: function(action) {
			
			thisForm.jsonInvoiceObject.promptWrongDateDiff = false;
			thisForm.startPromptingOn(action);
		},
		
		promptYesForWrongVatDiff: function(action) {
			
			if (thisForm.jsonInvoiceObject['WRONGVATDIFF']){
				thisForm.invoiceAmountTax.set("value", thisForm.formatNumber(thisForm.toNumber(thisForm.jsonInvoiceObject['VATTARGETAMOUNT']),2));
				thisForm.recalculate();
				thisForm.jsonInvoiceObject['WRONGVATDIFF'] = false;
			}
			
			thisForm.enableClick(registry.byId(dojo.query('[id^="'+action+'"]')[0].id));
		},
		
		promptNoForHufRound: function(action) {
			
			thisForm.jsonInvoiceObject.promptRoundHUF = false;
			thisForm.startPromptingOn(action);
		},
		
		promptYesForHufRound: function(action) {
			
			focus.focus(dom.byId(thisForm.id+"_invoiceAmountTotal"));
			thisForm.enableClick(registry.byId(dojo.query('[id^="'+action+'"]')[0].id));
		},
		
		
		promptNoForInvoiceSuppyExists: function(action) {
			
			thisForm.jsonInvoiceObject.promptInvoiceSuppyExists = false;
			thisForm.startPromptingOn(action);
		},
		
		promptYesForInvoiceSuppyExists: function(action) {
			
			focus.focus(dom.byId(thisForm.id+"_invoiceNumber"));
			thisForm.enableClick(registry.byId(dojo.query('[id^="'+action+'"]')[0].id));
		},
		
		promptNoForTaxInvoiceNumber: function(action) {
			
			thisForm.jsonInvoiceObject.promptTaxInvoiceNumber = false;
			thisForm.startPromptingOn(action);	
		},
		
		promptYesForTaxInvoiceNumber: function(action) {
			
			focus.focus(dom.byId(thisForm.id+"_taxInvoiceNumber"));
			thisForm.enableClick(registry.byId(dojo.query('[id^="'+action+'"]')[0].id));
		},
		
			
		submitConfirmationYes: function(action) {
			
		try {
			
			if (this.isDoubleClick(registry.byId(dojo.query('[id^="'+action+'"]')[0].id))) return;
			
			console.log(arguments.callee.nom + ' was clicked:' + thisForm.actionClick + ' time(s).');

			thisForm.serviceParameters['frontEndAction'] = action;
			request.postPluginService("GilCnPlugin", "InvoiceService",'application/x-www-form-urlencoded', {
	    		requestBody : ioquery.objectToQuery(thisForm.serviceParameters),
			    requestCompleteCallback : lang.hitch(this, function (response) {
	    			
	    			dialogMessagesResponse = new InvoiceDialogMessages();
	    			thisForm.jsonInvoiceObject	= JSON.parse(response.invoiceJsonString);
	    			thisForm.enableClick(registry.byId(dojo.query('[id^="'+action+'"]')[0].id))
	    			console.log('Invoice saved successfully.');

	    			 if ((thisForm.jsonInvoiceObject.CONTRACTNUMBER!=null && thisForm.jsonInvoiceObject.INVOICENUMBER!=null)
	    					&&( thisForm.jsonInvoiceObject.CONTRACTNUMBER.trim()==thisForm.jsonInvoiceObject.INVOICENUMBER.trim())) {
			    			if (thisForm.jsonInvoiceObject.validationIsContractCreated) {
			    				dialogMessagesResponse.addErrorMessages("Contract created");
			    			} else {
			    				dialogMessagesResponse.addErrorMessages("Contract creation failed");
			    			}
	    			 }
	    			
		    			if (!thisForm.jsonInvoiceObject.validationIsContractFolderCreated) {
			    			dialogMessagesResponse.addErrorMessages("Error Creating Contract Folder");	
			    			console.log(thisForm.jsonInvoiceObject.validationErrorIsContractFolderCreated);	
		    			}
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
		}
		
		},
		
		
		stopAction: function() {

			if (thisForm.jsonInvoiceObject['WRONGVATDIFF']){
				thisForm.recalculate();
				thisForm.invoiceAmountTax.set("value", thisForm.jsonInvoiceObject['VATTARGETAMOUNT']); 
				thisForm.jsonInvoiceObject['WRONGVATDIFF'] = false;
			} 
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
					this.cmValues = JSON.parse(this.serviceParameters['businessAttributes']);
					thisForm = this;
					var jsonUtils = new JsonUtils();
					var dialogMessages = new InvoiceDialogMessages();
					
					request.postPluginService("GilCnPlugin", "InvoiceService",'application/x-www-form-urlencoded', {
			    		requestBody : ioquery.objectToQuery(thisForm.serviceParameters),
					    requestCompleteCallback : lang.hitch(this, function (response) {
					    	
					    	this.setCustomizedValidation();
								    			
								    		this.jsonInvoiceObject	= JSON.parse(response.invoiceJsonString);
								    		this.setFieldsNotEditable(false);
								    			
								    		if (this.jsonInvoiceObject.isFromGCMS == true) {         
								    	 	     confirmDialog.addErrorMessage("Invoice is from GCMS and can't be indexed into APTS");
								    	 	     return false;    				
								    		}
								    		

									    	thisForm.show();

											thisForm.jsonInvoiceObject['promptInvoiceSuppyExists'] = true;
											thisForm.jsonInvoiceObject['promptWrongDateDiff'] = true;
											thisForm.jsonInvoiceObject['promptTaxInvoiceNumber'] = true;
																				    								    		
								    		thisForm.invoiceNumber.set("value",this.jsonInvoiceObject.INVOICENUMBER);						    		
								    		thisForm.contractNumber.set("value",this.jsonInvoiceObject.CONTRACTNUMBER);						    		
								    		thisForm.customerNumber.set("value",this.jsonInvoiceObject.CUSTOMERNUMBER);
								    		thisForm.customerName.set("value",this.jsonInvoiceObject.CUSTOMERNAME);									    		
								    		thisForm.invoiceDate.set("value",this.parseDate(this.jsonInvoiceObject.INVOICEDATE) );
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
								    		thisForm.offeringLetterNumber.set("value",this.jsonInvoiceObject.OFFERINGLETTER);
								    		
								    		registry.byId(thisForm.id+'_supplierName').set("value",this.jsonInvoiceObject.VENDORNAME);									    		
								    		registry.byId(thisForm.id+'_supplierNumber').set("value",this.jsonInvoiceObject.VENDORNUMBER);									    		
								    		registry.byId(thisForm.id+'_accountNumber').set("value",this.jsonInvoiceObject.ACCOUNTNUMBER);									    		
								    		
								    		registry.byId(thisForm.id+'_supplierName').set('_onChangeActive', true); 
								    		registry.byId(thisForm.id+'_supplierNumber').set('_onChangeActive', true);
								    		registry.byId(thisForm.id+'_accountNumber').set('_onChangeActive', true); 
								    		
								    		thisForm.ocrKid.set("value",this.jsonInvoiceObject.OCR);
									    		
								    		thisForm.dbCr.set("options", this.jsonInvoiceObject.dbCrSelectList);
								    		thisForm.dbCr.set("value", jsonUtils.getDefaultOrSelected(this.jsonInvoiceObject.dbCrSelectList,"value", true));
		 						    		
								    		thisForm.poexCode.set("options", this.jsonInvoiceObject.poexCodesSelectList);
								    		thisForm.poexCode.set("value", jsonUtils.getDefaultOrSelected(this.jsonInvoiceObject.poexCodesSelectList,"value", true));
								    		
								    		thisForm.invoiceCurrency.set("options", this.jsonInvoiceObject.currencyCodeSelectList);
								    		thisForm.invoiceCurrency.set("value", jsonUtils.getDefaultOrSelected(this.jsonInvoiceObject.currencyCodeSelectList,"value", true));
								    		   			                        	
								    		thisForm.invoiceType.set("options", this.jsonInvoiceObject.invoiceTypesSelectList);
								    		thisForm.invoiceType.set("value", jsonUtils.getDefaultOrSelected(this.jsonInvoiceObject.invoiceTypesSelectList,"value", true));
								    		registry.byId(this.id+'_invoiceType').set('_onChangeActive', true);

								    		
								    		thisForm.compCode.set("options", this.jsonInvoiceObject.companyCodeSelectList);
								    		thisForm.compCode.set("value", jsonUtils.getDefaultOrSelected(this.jsonInvoiceObject.companyCodeSelectList,"value", true));
		
								    		thisForm.taxCode.set("options", this.jsonInvoiceObject.vatCodesSelectList);
								    		thisForm.taxCode.set("value", jsonUtils.getDefaultOrSelected(this.jsonInvoiceObject.vatCodesSelectList,"value", true));
								    		
								    		
								    		if(this.isCountryIN && this.jsonInvoiceObject.EFEDATE == 'Y'){
								    			
								    			this.disableCommonFieldsForIndiaGST();
								    			this.setIndiaDstFieldsVisible(this.jsonInvoiceObject.indiaGSTFieldsEditable,this.jsonInvoiceObject.indiaGSTFieldsVisible);
								    			this.setIndiaGSTCustomizedValidation();
								    			this.onChangeIndiaGSTAmtFields();
								    			
								    			this.billTo.set("options", this.jsonInvoiceObject.billToSelectList);
								    			this.billTo.set("value", jsonUtils.getDefaultOrSelected(this.jsonInvoiceObject.billToSelectList,"value", true));
									    		
								    			this.billFrom.set("options", this.jsonInvoiceObject.billFromSelectList);
								    			this.billFrom.set("value", jsonUtils.getDefaultOrSelected(this.jsonInvoiceObject.billFromSelectList,"value", true));
									    		
								    			this.shipTo.set("options", this.jsonInvoiceObject.shipToSelectList);
								    			this.shipTo.set("value", jsonUtils.getDefaultOrSelected(this.jsonInvoiceObject.shipToSelectList,"value", true));
								    			
								    			this.loanIndicator.set("options", this.jsonInvoiceObject.loanSelectList);
								    			this.loanIndicator.set("value", jsonUtils.getDefaultOrSelected(this.jsonInvoiceObject.loanSelectList,"value", true));

								    			this.igst.set("value",this.jsonInvoiceObject.INIGST);
								    			this.cgst.set("value",this.jsonInvoiceObject.INCGST);
								    			this.sgst.set("value",this.jsonInvoiceObject.INSGST);
								    			this.supplierGSTRegNumber.set("value",this.jsonInvoiceObject.SUPPGSTREGNUMBER);
								    			
								    			this.recalculateINNetAmount();
								    		}
								    		
								    		if (this.isCAICAEFieldsAllowed(this.serviceParameters['country'])){
								    			registry.byId(thisForm.id+"_caicae").set( "value", this.jsonInvoiceObject.CAICAE);								    			
								    			if(this.jsonInvoiceObject.CAICAEDate !=null && this.jsonInvoiceObject.CAICAEDate.trim()!="")
								    				try {
								    					registry.byId(thisForm.id+"_caicaeDueDate").set( "value",this.parseDate(this.jsonInvoiceObject.CAICAEDate,false));	
								    				} catch(e){
								    					console.log('CAI CAE Due Date is blank: '+this.jsonInvoiceObject.CAICAEDate );
								    				}		
								    		}
								    		
								    		if (thisForm.isSilentCOAExhibitionAllowed(this.serviceParameters['country'])) {
									    		var silentCoa = registry.byId(thisForm.id+"_silentCoa");
									    		registry.byId(thisForm.id+"_silentCoa").set("options", this.jsonInvoiceObject.silentCoaSelectList );
									    		registry.byId(thisForm.id+"_silentCoa").set("value", jsonUtils.getValueIfOtherKeyExists(this.jsonInvoiceObject.silentCoaSelectList,"value", true) );
									    		registry.byId(thisForm.id+"_silentCoa").startup();
								    		}
								    		
								    		if (thisForm.isTaxInvoiceExhibitionAllowed(this.serviceParameters['country'])) {
								    			registry.byId(thisForm.id+"_taxInvoiceNumber").set( "value", this.jsonInvoiceObject.TAXINVOICENUMBER);	
								    		}
								    		
								    		if (thisForm.isInvoiceSuffixExhibitionAllowed(this.serviceParameters['country'])) {
								    		
									    		registry.byId(thisForm.id+"_invoiceSuffix").set("options", this.jsonInvoiceObject.invoiceSuffixesSelectList );
									    		registry.byId(thisForm.id+"_invoiceSuffix").set("value", jsonUtils.getValueIfOtherKeyExists(this.jsonInvoiceObject.invoiceSuffixesSelectList,"value", true) );
									    		registry.byId(thisForm.id+"_invoiceSuffix").startup();
								    		}	
								    		 
								    		if  (thisForm.isSupplyDateExhibitionAllowed(this.serviceParameters['country'])) {
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
										        _widget	 = new ecm.widget.Select({id:thisForm.id+"_distrNum", name:"distrNum",   });
										        
							          	        construct.place(_label, thisForm.id+"_replacebleLabelForDistrNum","replace");
							          	        construct.place(_widget.domNode, thisForm.id+"_replacebleFieldForDistrNum","replace");
									    		registry.byId(thisForm.id+"_distrNum").set("options", this.jsonInvoiceObject.distributorCodeSelectList );
									    		registry.byId(thisForm.id+"_distrNum").set("value", jsonUtils.getDefaultOrSelected(this.jsonInvoiceObject.distributorCodeSelectList,"value", true) );
									    		registry.byId(thisForm.id+"_distrNum").startup();
										        
									        }
								    										    
									    	this.checkWorkAcces();
								    		
								    		if(this.jsonInvoiceObject.DOCUMENTMODE == 3) {
								    			
								    			dialogMessages = this.validateOnIndex(this.performIndex);
								    		}
								    		if (this.jsonInvoiceObject.vendorSearchRequired) {

								    			this.searchVendorOnIndex();
								    		}
								    		
								    		this.serialFormClean=this.serializeForm(thisForm.id+"_invoiceForm"); 
								    		
								    		})
								});
					
					
			} catch(e) {
				console.log('Error in method ' + arguments.callee.nom + ' - ' + e);
			}
		},
		
		
		searchVendorOnIndex : function() {
			console.log("openVendorSearchDialogSuppliersearchVendorOnIndex:::");
	    		if (this.jsonInvoiceObject.VENDORNAME!=null && this.jsonInvoiceObject.VENDORNAME.length > 0) {	
	    			this.openVendorSearchDialogSupplierName() 
	    			
	    		} else if (this.jsonInvoiceObject.VENDORNUMBER!=null && this.jsonInvoiceObject.VENDORNUMBER.length > 0) {
	    				  this.openVendorSearchDialogSupplierNumber()
	    		}
		},
		
		performIndex: function(dialogMessages) {

			var hasPromptMessages = false;
			
			if (thisForm.jsonInvoiceObject['promptChangingInvoiceNumberDate'] == true ) {
				hasPromptMessages = true;
			}
			
			if(dialogMessages.hasErrorMessages() ){
							
				thisForm.showMessage("Error",dialogMessages.getAllMessages(),false,null,null);
			    thisForm.cancel(false);
			
			}else if(hasPromptMessages){
								
					  thisForm.startPromptingOnIndex();
				}
			
			if(dialogMessages.hasInfoMessages() && !dialogMessages.hasErrorMessages() ){
				
				thisForm.showMessage("Information",dialogMessages.infoMessages(),false,null,null);	
			}
			
		},
		
		
		startPromptingOnIndex: function() {
	        
			var dialogMsgs = new InvoiceDialogMessages();
			
			if(thisForm.jsonInvoiceObject.promptChangingInvoiceNumberDate) {
				dialogMsgs.addConfirmMessages("Changing Invoice Number/Date. Are you sure?");
				thisForm.showMessage("Confirm",dialogMsgs.confirmMessages(),true ,thisForm.promptYesForPromptChangingInvoiceNumberDate, thisForm.promptNoForPromptChangingInvoiceNumberDate);		
			} 
			
		},
		
		promptYesForPromptChangingInvoiceNumberDate: function() {

			focus.focus(dom.byId(thisForm.id+"_invoiceNumber"));
			thisForm.jsonInvoiceObject.DIRTYFLAG = true;
		},
		
		promptNoForPromptChangingInvoiceNumberDate: function() {
			
			thisForm.jsonInvoiceObject.INVOICENUMBER = thisForm.jsonInvoiceObject.OLDINVOICENUMBER;
			thisForm.jsonInvoiceObject.INVOICEDATE = thisForm.jsonInvoiceObject.OLDINVOICEDATE;		
    		thisForm.invoiceNumber.set("value", 		thisForm.jsonInvoiceObject.OLDINVOICENUMBER);						    							    		
    		thisForm.invoiceDate.set("value", 			thisForm.parseDate(thisForm.jsonInvoiceObject.OLDINVOICEDATE) );
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
		
		 /***
	     *Method : 	validateOnIndexForCountryCustomerVendername	   
	     * Usage :Used to update old values while user try to reindex paid invoice.
	     * 
	     * */
		
		validateOnIndexForCountryCustomerVendorname : function(oldvalue,attrName,errorMsg,callback) {
			
			var dialogMessages = new InvoiceDialogMessages();
        	this.jsonInvoiceObject[''+oldvalue]=this.jsonInvoiceObject.OLDCUSTOMERNAME;
        	dialogMessages.addErrorMessages(errorMsg);
        	registry.byId(thisForm.id+attrName).set("value", this.jsonInvoiceObject.OLDCUSTOMERNAME);	
     		callback(dialogMessages);
		},
		
		isFullyPaid : function() {
			
			if(this.jsonInvoiceObject.DOCUMENTMODE == 1){
				return false;
			}
			
			 return (MathJs.compare(thisForm.toNumber(this.jsonInvoiceObject.INVBALAMT),0.00)==0 
	 				   && 
	 				   MathJs.compare(thisForm.toNumber(this.jsonInvoiceObject.INVVARAMT),0.00)==0) ;
		}
		,
		
		isPartiallyPaid : function() {
			
			if(this.jsonInvoiceObject.DOCUMENTMODE == 1){
				return false;
			}
			
			return (MathJs.compare(this.toNumber(this.jsonInvoiceObject.validationNetAmountStr), this.toNumber(this.jsonInvoiceObject.validationNetBalanceStr)) > 0 
	 				&&  
	 				MathJs.compare(this.toNumber(this.jsonInvoiceObject.validationNetBalanceStr),0.00) > 0);
		}
		,
		
		isVarianced : function() {
			
			return MathJs.compare(this.toNumber(this.jsonInvoiceObject.validationInvVarAmountStr), 0.00) >0
		}
		,
		validateOnIndex : function(callback) {

			var dialogMessages = new InvoiceDialogMessages();
			var isChangingInvoiceNUmber = false;
			 this.jsonInvoiceObject['promptChangingInvoiceNumberDate'] = false;
			 		 
			 try {
				 
					 var isVarianced = thisForm.isVarianced();
					 var isFullyPaid = thisForm.isFullyPaid();
					 var isPartiallyPaid = thisForm.isPartiallyPaid();
					 var hasPayments = isPartiallyPaid || isFullyPaid; 				
				 
					if(thisForm.isOfferingLetterChanging() || thisForm.isCountryChanging() ||
							thisForm.isVendorNameChanging()  || thisForm.isCustomerNameChanging()){
						this.jsonInvoiceObject['DIRTYFLAG'] = true;
					}
						
				
					if( isFullyPaid) {
		
						dialogMessages.addInfoMessages( "Invoice has a balance of 0.00 and cannot be re-indexed.");
										    					
		                if(thisForm.isCountryChanging()){
		                	
		                	this.jsonInvoiceObject['COUNTRY']=this.serviceParameters['country'];
		                	dialogMessages.addErrorMessages( "Invoice has a balance of 0.00 and cannot be re-indexed.");
		                	this.rollbackCountryIndexing();
		             		callback(dialogMessages);
		             		return;
		                }
		                

		                  
		                /***
					     *	Defect : 	1774555  
					     *  Usage : 	Alert to user while Re-indexing Fully paid Invoices
					     * 
					     * */
		                if(thisForm.isCustomerNameChanging())
		                {	
		             		this.validateOnIndexForCountryCustomerVendorname("CUSTOMERNAME","_customerName","Invoice has a balance of 0.00 and cannot be re-indexed.",callback);
		             		return;
		                }
		                if(thisForm.isVendorNameChanging())
		                { 
		                	this.validateOnIndexForCountryCustomerVendorname("VENDORNAME","_supplierName","Invoice has a balance of 0.00 and cannot be re-indexed.",callback);
		             		return;
		                }
		                
					} 	
					
					 /***
				     *				   
				     * Usage : Alert to user while Re-indexing partially paid Invoices
				     * 
				     * */
					if(isPartiallyPaid)	
					 {
						  if(thisForm.isCustomerNameChanging())
			                {			              
			                	
			             		this.validateOnIndexForCountryCustomerVendorname("CUSTOMERNAME","_customerName","Invoice has a payment against it and cannot be updated.",callback);
			             		return;
			                }
						   if(thisForm.isVendorNameChanging())
			                { 
			                	this.validateOnIndexForCountryCustomerVendorname("VENDORNAME","_supplierName","Invoice has a payment against it and cannot be updated.",callback);
			             		return;
			                }
					 }
					  
		    		
					if( thisForm.isCommissionInvoice(this.jsonInvoiceObject.INVOICETYPE)  && (thisForm.isVendorNameChanging()  || this.isAccountNumberChanging() )) {
			 	          
						dialogMessages.addInfoMessages( "Vendor and Account cannot be changed for commision invoices.");
						
						this.jsonInvoiceObject['DIRTYFLAG'] = true;
						this.jsonInvoiceObject['ACCOUNTNUMBER'] = this.jsonInvoiceObject.OLDACCOUNTNUMBER;
						this.jsonInvoiceObject['VENDORNAME'] = this.jsonInvoiceObject.OLDVENDORNAME;
			    		registry.byId(thisForm.id+'_supplierName').set("value", 			this.jsonInvoiceObject.OLDVENDORNAME);									    		
			    		registry.byId(thisForm.id+'_supplierNumber').set("value", 			this.jsonInvoiceObject.OLDVENDORNUMBER);									    		
			    		registry.byId(thisForm.id+'_accountNumber').set("value", 			this.jsonInvoiceObject.OLDACCOUNTNUMBER);	
	
						
					} else if ( (thisForm.isVendorNameChanging() && (!isFullyPaid && !isPartiallyPaid))  || this.isAccountNumberChanging() ) {
						
								this.jsonInvoiceObject['vendorSearchRequired'] = true;
								this.jsonInvoiceObject['DIRTYFLAG'] = true;
					}
					

		             if(thisForm.isInvoiceNumberChanging() || thisForm.isInvoiceDateChanging()) {
		            	 
		            	this.jsonInvoiceObject['promptChangingInvoiceNumberDate'] = true;
		            	this.jsonInvoiceObject['DIRTYFLAG'] = true;
		            	
		            	if(isFullyPaid){
		
		             		dialogMessages.addErrorMessages("Invoice has a balance of 0.00 and cannot be re-indexed.");
		             		this.rollbackIndexing();
		             		 callback(dialogMessages);
		             		 return;
		             	}
		            	
		            	if(isPartiallyPaid){
		            		
		             		dialogMessages.addErrorMessages( "Invoice has a payment against it and cannot be updated.");
		             		this.rollbackIndexing();
		             		 callback(dialogMessages);
		             		 return;
		             	}

		            	if(isVarianced) {
		             		
		             		dialogMessages.addErrorMessages( "Invoice has been varianced and has a balance of "+this.jsonInvoiceObject.validationNetBalanceStr+". To change invoice number/invoice date unvariance in GAPTS first.");
		             		this.rollbackIndexing();
		             		 callback(dialogMessages);
		             		 return;
		             	}

		             }
          
		 	    	this.checkPaid();
		            callback(dialogMessages);
			
				} catch(e) {
					console.log('Error in method ' + arguments.callee.nom + ' - ' + e);
				}
		},
		
		
		rollbackIndexing : function() {

			thisForm.serviceParameters['frontEndAction'] = arguments.callee.nom;
			request.postPluginService("GilCnPlugin", "InvoiceService",'application/x-www-form-urlencoded', {
	    		requestBody : ioquery.objectToQuery(this.serviceParameters),
			    requestCompleteCallback : lang.hitch(this, function (response) {
	    			
	    			console.log('Index rolled back successfully.');
	    			
	    			return true;
		    		
	    		})
			});
		},
		
		
		rollbackCountryIndexing : function() {

			thisForm.serviceParameters['frontEndAction'] = arguments.callee.nom;
			request.postPluginService("GilCnPlugin", "InvoiceService",'application/x-www-form-urlencoded', {
	    		requestBody : ioquery.objectToQuery(this.serviceParameters),
			    requestCompleteCallback : lang.hitch(this, function (response) {
	    			
	    			console.log('Country index rolled back successfully.');
		    		
	    		})
			});
		},
		
		
		setIndiaGSTCustomizedValidation : function() {
			
			var thisForm = this;
			lang.getObject('supplierGSTRegNumber', false, thisForm).set("missingMessage","Supplier GST Registration Number is a required field.");
			lang.getObject('supplierGSTRegNumber', false, thisForm).set("invalidMessage","Supplier GST Registration Number is a required field.");
			
			lang.getObject('supplierGSTRegNumber', false, this).validator = function() {
				
				if (
						lang.getObject('supplierGSTRegNumber', false, thisForm).get("value") == null ||
						lang.getObject('supplierGSTRegNumber', false, thisForm).get("value").trim().length == 0  	)	{

					lang.getObject('supplierGSTRegNumber', false, thisForm).set("missingMessage","Supplier GST Registration Number is a required field.");
					lang.getObject('supplierGSTRegNumber', false, thisForm).set("invalidMessage","Supplier GST Registration Number is a required field.");
				
					return false;
				}
				return true;		
			}
		}
		,
		
		setCustomizedValidation : function() {
			
			try {
					this.invoiceNumber.set("missingMessage","Invoice Number is a required field.");
				
					lang.getObject('customerName', false, this).validator = function() {
						
						if (lang.getObject('customerName', false, thisForm).get("value") == null ||
								lang.getObject('customerName', false, thisForm).get("value").trim().length == 0  	)	{
		
							lang.getObject('customerName', false, thisForm).set("invalidMessage","Customer Name is a required field.");					
							lang.getObject('customerName', false, thisForm).set("missingMessage","Customer Name is a required field.");
							
							return false;
						}
						
						return true;		
					}
					
					
					lang.getObject('customerName', false, this).validator = function() {
						
						if (lang.getObject('customerName', false, thisForm).get("value") == null ||
								lang.getObject('customerName', false, thisForm).get("value").trim().length == 0  	)	{
		
							lang.getObject('customerName', false, thisForm).set("invalidMessage","Customer Name is a required field.");					
							lang.getObject('customerName', false, thisForm).set("missingMessage","Customer Name is a required field.");
							
							return false;
						}
						
						return true;		
					}

					lang.getObject('supplierName', false, this).validator = function() {
						
						if (lang.getObject('supplierName', false, thisForm).get("value").trim().length == 0   &&
								lang.getObject('accountNumber', false, thisForm).get("value").trim().length == 0   	)	{
		
							lang.getObject('supplierName', false, thisForm).set("invalidMessage","Vendor Name or Account Number are required fields.");					
							lang.getObject('supplierName', false, thisForm).set("missingMessage","Vendor Name or Account Number are required fields.");
							
							return false;
						}
						
						return true;		
					}
						
					lang.getObject('accountNumber', false, this).validator = function() {
						
						if (lang.getObject('supplierName', false, thisForm).get("value").trim().length == 0   &&
								lang.getObject('accountNumber', false, thisForm).get("value").trim().length == 0   	)	{
		
							lang.getObject('accountNumber', false, thisForm).set("invalidMessage","Vendor Name or Account Number are required fields.");					
							lang.getObject('accountNumber', false, thisForm).set("missingMessage","Vendor Name or Account Number are required fields.");
							
							return false;
						}
						
						return true;		
					}
					
					lang.getObject('invoiceDate', false, this).validator = function() {
					   var parsedInvoiceDate = thisForm.parseDate(domattr.get(dom.byId(thisForm.id+"_invoiceDate"), "value"), false);

					    if(parsedInvoiceDate == null){
					    	thisForm.invoiceDate.set("invalidMessage","Invoice Date fomat must have format "+ thisForm.gilDatePattern);	
					    }
/*						if (parsedInvoiceDate!=null) {
							if (date.difference(parsedInvoiceDate,new Date()) > 300	)	{
								thisForm.invoiceDate.set("invalidMessage","Invoice Date must not be greater than 300 days old.");
								//thisForm.invoiceDate.set("invalidMessage","Invoice date is greater than 300 days old and specific approvals may apply for processing. Do you wish to correct ?");
								
								*//**
								 * just showing messgae to end user on screen
								 * modified for defect : 1819908
								 * 
								 *//*
								//return true;
								
								return false;
							}
						}*/
						var parsedCreatedTimestamp = thisForm.parseTimestamp(thisForm.createdTimestamp.get('value'));
	
					    if ( date.compare(parsedInvoiceDate, parsedCreatedTimestamp, "date") > 0) {
							thisForm.invoiceDate.set("invalidMessage","Scan Date cannot be before the Invoice Date.");					
							return false;
						}
					    
					    return true;
					}
					
			
					
					lang.getObject('caicaeDueDate', false, this).validator = function() {
						    
						var parsedcaicaeDueDate = thisForm.parseDate(domattr.get(dom.byId(thisForm.id+"_caicaeDueDate"), "value"), false);
						if(parsedcaicaeDueDate == null && thisForm.isCAICAEFieldsAllowed(thisForm.serviceParameters['country'])){
						    thisForm.caicaeDueDate.set("invalidMessage","CAI/CAE due Date is a required field");
						    thisForm.caicaeDueDate.set("missingMessage","CAI/CAE due Date is a required field");
						    return false;
						}
						
						return true;
						
					}
					
					lang.getObject('caicae', false, this).validator = function() {
						
						if (thisForm.isCAICAEFieldsAllowed(thisForm.serviceParameters['country']) && (
								lang.getObject('caicae', false, thisForm).get("value") == null ||
								lang.getObject('caicae', false, thisForm).get("value").trim().length == 0 ) 	)	{
		
							lang.getObject('caicae', false, thisForm).set("invalidMessage","CAI/CAE is a required field");					
							lang.getObject('caicae', false, thisForm).set("missingMessage","CAI/CAE is a required field");
							
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
			
			if(this.isCountryIN && this.jsonInvoiceObject.EFEDATE == 'Y'){
				this.recalculateINNetAmount();
			} else {
				this.recalculateNetAmount();
			}
			
		},
		
		recalculateNetAmount: function() {
			
			try {
					var vatVariance = this.toNumber(this.jsonInvoiceObject['VATVarianceStr']);
					var total = 0.00;
					total =	this.invoiceAmountTotal.get('value');
					var tax = 0.00;
					tax =	this.invoiceAmountTax.get('value');
					net = MathJs.subtract(total,tax);
					var vatpercentage = 0.00

					if (this.taxCode.get('value') !=  null && this.taxCode.get('value').trim().length > 0) {
						vatpercentage = this.toNumber(this.jsonInvoiceObject.VATPercentagesStr[this.taxCode.get('value')]);
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
					this.jsonInvoiceObject['NETAMOUNT'] = this.formatNumber(net,2).toString();
					
			        this.invoiceAmountTotal.set('value',this.formatNumber(total,2));
			        this.jsonInvoiceObject['TOTALAMOUNT'] = this.formatNumber(total,2).toString();
			        
			        this.invoiceAmountTax.set('value',this.formatNumber(tax,2));
			        this.jsonInvoiceObject['VATAMOUNT'] = this.formatNumber(tax,2).toString();
			        
			        this.jsonInvoiceObject['VATTARGETAMOUNT'] = this.formatNumber(targetVat,2).toString();
			        this.jsonInvoiceObject['VATVarianceStr']  = this.formatNumber(vatVariance,2).toString();
        
			} catch(e) {
				console.log('Error in method ' + arguments.callee.nom + ' - ' + e);
			}
			
		},
		
		recalculateINNetAmount: function() {	        
			
				var total  = this.formatNumber(0.00,2);
		        var CGST = this.formatNumber(0.00,2);
		        var SGST = this.formatNumber(0.00,2);
		        var IGST = this.formatNumber(0.00,2);
		        var net = this.formatNumber(0.00,2);
		        var tax = this.formatNumber(0.00,2);
		        var Default = this.formatNumber(0.00,2);
		        var vatVariance = this.toNumber(this.jsonInvoiceObject['VATVarianceStr']);
			                
			        try{
		
			        	total = this.invoiceAmountTotal.get('value');
			        	if(!isNaN(total) ) 
			        		total = this.formatNumber(total,2);
			        	else
			        		total = this.formatNumber(0,2);
		
			        } catch (e){
			        	console.log('Error parsing invoiceAmountTotal:' + arguments.callee.nom + ' - ' + e);
			        	 total  = this.formatNumber(0,2);
			        }
			        try{
			        	CGST = this.cgst.get('value');
			        	if(!isNaN(CGST) ) 
			        		CGST = this.formatNumber(CGST,2);
			        	else
			        		CGST = this.formatNumber(0,2);
		
			        	this.cgst.set('value',CGST);	
			        		
			        } catch (e) {
			        	console.log('Error parsing cgst:' + arguments.callee.nom + ' - ' + e);
			        	CGST = this.formatNumber(0,2);
			        }
			        try {
			        	SGST = this.sgst.get('value');
			        	if(!isNaN(SGST) ) 
			        		SGST = this.formatNumber(SGST,2);
			        	else
			        		SGST = this.formatNumber(0,2);
		
			        		this.sgst.set('value',SGST);	
			        } catch (e){
			        	console.log('Error parsing sgst:' + arguments.callee.nom + ' - ' + e);
			        	 SGST = this.formatNumber(0,2);
			        }
			        try{
			        	IGST = this.igst.get('value');
			        	if(!isNaN(IGST)) 
			        		IGST = this.formatNumber(IGST,2);
			        	else
			        		IGST = this.formatNumber(0,2);
		
			        		this.igst.set('value',IGST);	
			        } catch (e){
			        	console.log('Error parsing igst:' + arguments.callee.nom + ' - ' + e);
			        	IGST = this.formatNumber(0,2);
			        }
			        
			        try{     
			        	if((this.loanIndicator.get('value')=='N') && (this.billTo.get('value')==this.billFrom.get('value') )){
			        		net = MathJs.subtract(total,CGST);
			        		net = MathJs.subtract(net,SGST);
			        		tax = MathJs.add(CGST,SGST);
			        		this.igst.set('value',Default);
			        	}else if((this.loanIndicator.get('value')=='N') && (this.billTo.get('value')!=this.billFrom.get('value'))){        		
			        		net = MathJs.subtract(total,IGST);
			        		tax = IGST;
			        		this.cgst.set('value',Default);
			        		this.sgst.set('value',Default);
			        	}else{   
			        		net = total;
			        		tax = Default;
			        		this.igst.set('value',Default);
			        		this.cgst.set('value',Default);    
			        		this.sgst.set('value',Default); 
			            }
			        } catch (e){
			        	console.log('Error during calculation:' + arguments.callee.nom + ' - ' + e);
			        }
			        
			        net = Math.max(0,net);
		
			        this.invoiceAmountTotal.set('value',this.formatNumber(total,2));
			        this.jsonInvoiceObject['TOTALAMOUNT'] = this.formatNumber(total,2).toString();
				    this.invoiceAmountTax.set('value',this.formatNumber(tax,2));
				    this.jsonInvoiceObject['VATAMOUNT'] = this.formatNumber(tax,2).toString();
				    this.netAmount.set('value',this.formatNumber(net,2));
					this.jsonInvoiceObject['NETAMOUNT'] = this.formatNumber(net,2).toString();
					this.jsonInvoiceObject['VATVarianceStr']  = this.formatNumber(vatVariance,2).toString();
			
		},
		
	    isCommissionInvoice :  function(invoiceType) {
	    
	        if (invoiceType!=null && ( (invoiceType.trim() == "COM") || invoiceType.trim() == "STP"))
	        	return true;
	        
	        return false;
	     
	    },
	    
	    isVendorNameChanging :  function() {
	    	
	    	return this.jsonInvoiceObject.VENDORNAME != this.jsonInvoiceObject.OLDVENDORNAME;
	    },
	    
	    isInvoiceNumberChanging : function() {
	    	
          return this.jsonInvoiceObject.OLDINVOICENUMBER  != this.jsonInvoiceObject.INVOICENUMBER ;
	    	
	    },
	    
	    isInvoiceDateChanging : function() {
	    	
            var oldInvoiceDate 	= this.parseDate(this.jsonInvoiceObject.OLDINVOICEDATE,false);
            var currentInvoiceDate = this.parseDate(this.jsonInvoiceObject.INVOICEDATE,false);
            
           if (date.compare(oldInvoiceDate, currentInvoiceDate)!=0 ) {
        	   return true;
           } else {
        	   return false;
           }
        	   
	    },
	    
	    //commented temporerly by srinivasu have to check with fenando
	    
//	    isCustomerChanging :  function() {
//	    	
//	    	console.log("FunctioninCustomerchange::::"+this.cmValues['customerName']+"::::"+this.jsonInvoiceObject.CUSTOMERNAME+"this.jsonInvoiceObject.OLDCUSTOMERNAME"+this.jsonInvoiceObject.OLDCUSTOMERNAME);
//	    	return (this.jsonInvoiceObject.CUSTOMERNAME != this.cmValues['customerName'] ||
//	    			this.jsonInvoiceObject.CUSTOMERNUMBER != this.cmValues['customerNumber']);
//	    },
	    
	    /***
	     * method : isCustomerNameChanging
	     * 
	     * Usage : Used to check weather customer name changed or not
	     * 
	     * */
    isCustomerNameChanging :  function() {
	    	return this.jsonInvoiceObject.CUSTOMERNAME != this.jsonInvoiceObject.OLDCUSTOMERNAME ;
	    },
	    
	    

	    isOfferingLetterChanging :  function() {
	    	
	    	return this.jsonInvoiceObject.OFFERINGLETTER != this.cmValues['offeringLetterNumber'];
	    },
	    
	    isCountryChanging :  function() {
	    	
	    	return this.jsonInvoiceObject.COUNTRY!=this.jsonInvoiceObject.OLDCOUNTRY;
	    },
	    
	    
	    isAccountNumberChanging :  function() {
	    
	    	return this.jsonInvoiceObject.ACCOUNTNUMBER != this.jsonInvoiceObject.OLDACCOUNTNUMBER;
	    },
	    

		
		setNonEditableFields: function() {
			
			this.netAmount.set('readonly',true);
			this.netAmount.set('disabled',true);
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
		
		isIndiaGSTFieldsAllowed :  function(country) {
	        return (country == 'IN')
		},
		
		isCAICAEFieldsAllowed :  function(country) {
	        return (country == 'AR')
		},
		
		
		isSilentCOAExhibitionAllowed :  function(country) {
	        return (country == 'SE')
		},
		
		isTaxInvoiceExhibitionAllowed :  function(country) {
	        return (country == 'KR')
		},
		
		isInvoiceSuffixExhibitionAllowed :  function(country) {
	        return ((country == 'CL' || country == 'CO' || country == 'MX' || country == 'PE')) 
		},

		hideFieldsForCountry :  function() {
			
			var country = this.serviceParameters['country'];
			var _thisform = this;
			var _label = null;
			var _widget = null;
			 
			 if(!this.isCAICAEFieldsAllowed(country))  {
				 dojo.style(this.caicaeDueDateTr,"display","none");
				 dojo.style(this.caicaeTr,"display","none");
			 }
			
	        if(this.isSupplyDateExhibitionAllowed(country))  {
	        	
				_label =	construct.toDom("  <label  id='${id}_taxSupplyDateLabel' for='taxSupplyDate'>Tax Supply Date</label>&nbsp;");
		        _widget	 = new DateTextBox({id:_thisform.id+"_supplyDate", name:"supplyDate",invalidMessage: "Invoice Date fomat must have format "+_thisform.gilDatePattern, constraints:  { min: new Date( 1950, 2, 10),}});
		        var replacebleFieldNumber = this.getAvailableReplacebleField();
		        construct.place(_label, _thisform.id+"_replacebleLabel"+replacebleFieldNumber,"replace");
		        construct.place(_widget.domNode, _thisform.id+"_replacebleField"+replacebleFieldNumber,"replace");
		        registry.byId(_thisform.id+"_supplyDate").set("placeholder", _thisform.gilDatePattern );
	        }
	        
	        
	        if (this.isSilentCOAExhibitionAllowed(country))	{
	        	
				_label =	construct.toDom("  <label  id='${id}_silentCoaLabel' for='silentCoa'>Silent COA</label>&nbsp;");
		        _widget	 = new ecm.widget.Select({id:_thisform.id+"_silentCoa", name:"silentCoa",  });
		        var replacebleFieldNumber = this.getAvailableReplacebleField();
		        construct.place(_label, _thisform.id+"_replacebleLabel"+replacebleFieldNumber,"replace");
		        construct.place(_widget.domNode, _thisform.id+"_replacebleField"+replacebleFieldNumber,"replace");
	        }
		        
		        
		        if(this.isTaxInvoiceExhibitionAllowed(country)) {
		        	
					_label =	construct.toDom("<label  id='${id}_taxInvoiceNumberLabel' for='taxInvoiceNumber'>Tax Invoice Number</label>&nbsp;");
					_widget	 = new  ecm.widget.ValidationTextBox({id:_thisform.id+"_taxInvoiceNumber", style:"width: 220px;", name:"taxInvoiceNumber", maxLength:'24',});
					domclass.add(_widget.focusNode, "uppercaseField");
					var replacebleFieldNumber = this.getAvailableReplacebleField();
			        construct.place(_label, _thisform.id+"_replacebleLabel"+replacebleFieldNumber,"replace");
			        construct.place(_widget.domNode, _thisform.id+"_replacebleField"+replacebleFieldNumber,"replace");
		        }
		        
		        if (this.isInvoiceSuffixExhibitionAllowed(country))  {

					_label =	construct.toDom("<label  id='${id}_invoiceSuffixLabel' for='invoiceSuffix'>Invoice Suffix</label>&nbsp;");
			        _widget	 = new ecm.widget.Select({id:_thisform.id+"_invoiceSuffix", name:"invoiceSuffix",   });
			        var replacebleFieldNumber = this.getAvailableReplacebleField();
			        construct.place(_label, _thisform.id+"_replacebleLabel"+replacebleFieldNumber,"replace");
			        construct.place(_widget.domNode, _thisform.id+"_replacebleField"+replacebleFieldNumber,"replace");
		        }

		},
		
		setFieldsNotEditable :  function(editable)	{
			
			country = this.serviceParameters['country'];
			
			this.invoiceNumber.set('disabled',editable);
			this.invoiceDate.set('disabled',editable);
			this.invoiceAmountTotal.set('disabled',editable);
			this.invoiceAmountTax.set('disabled',editable);
			this.netAmount.set('disabled',editable);
			this.supplierName.set('disabled',editable);
			this.supplierNumber.set('disabled',editable);
			this.accountNumber.set('disabled',editable);
			this.customerNumber.set('disabled',editable);
			this.customerName.set('disabled',editable);
			
				
			this.billTo.set('disabled',editable);
			this.billFrom.set('disabled',editable);
			this.shipTo.set('disabled',editable);
			this.loanIndicator.set('disabled',editable);
			this.supplierGSTRegNumber.set('disabled',editable);	
			this.sgst.set('disabled',editable);
			this.cgst.set('disabled',editable);
			this.igst.set('disabled',editable);
			
			this.poexCode.set('disabled',editable);
			this.invoiceCurrency.set('disabled',editable);
			this.invoiceType.set('disabled',editable);
			this.compCode.set('disabled',editable);
			this.taxCode.set('disabled',editable);
			this.dbCr.set('disabled',editable);

			registry.byId(dojo.query('[id^="defaults"]')[0].id).set("disabled", editable );	
						
			if(this.isSupplyDateExhibitionAllowed(country)) {
				registry.byId(thisForm.id+"_supplyDate").set("disabled", editable );
			}
			
			if(this.isTaxInvoiceExhibitionAllowed(country)){
				registry.byId(thisForm.id+"_taxInvoiceNumber").set("disabled", editable );
			}
			
			if(this.isInvoiceSuffixExhibitionAllowed(country)){
				 registry.byId(thisForm.id+"_invoiceSuffix").set("disabled", editable );
			}

			if (this.isSilentCOAExhibitionAllowed(country)){
		        if (this.jsonInvoiceObject.DOCUMENTMODE == 3)
		        {
		        	registry.byId(thisForm.id+"_silentCoa").set("disabled", false );
		        } else
		        {
		        	registry.byId(thisForm.id+"_silentCoa").set("disabled", editable );
		        }
	            
			}
 
	    },
	    
	    
	    setInvoiceAmountFields :  function(editable) {
	    
			country = this.serviceParameters['country'];
			this.invoiceAmountTotal.set('disabled',editable);
			this.invoiceAmountTax.set('disabled',editable);
			this.netAmount.set('disabled',editable);
			this.taxCode.set('disabled',editable);   
			
			
			this.billTo.set('disabled',editable);
			this.billFrom.set('disabled',editable);
			this.loanIndicator.set('disabled',editable);
			this.sgst.set('disabled',editable);
			this.cgst.set('disabled',editable);
			this.igst.set('disabled',editable);
	    },
	    
	    setAmountFieldNotEditable :  function(editable){
	    	
	    	this.setInvoiceAmountFields(editable);
	    	this.dbCr.set('disabled',editable);
	    },
		
		showMessage: function(titl, msg, needConfirmation, callbackYes, callbackNo, fieldtoFocus) {	
			
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
		            	
		            }});
		        
		        buttonYes.placeAt(actionBar);
		        domclass.add(buttonYes.focusNode, "yesNoOkButton");
		        												
		        var buttonNo = new dijit.form.Button({"label": "No",
		            onClick: function(){
		            	
		            	callbackNo();
		            	d1.destroyRecursive();
		            	
		            	}});
		        
		        buttonNo.placeAt(actionBar);
		        domclass.add(buttonNo.focusNode, "yesNoOkButton");
            	
            } else {
            
			        var actionBar = dojo.create("div", {"class": "dijitDialogPaneContentArea",}, d1.containerNode);
			        var actionBar = dojo.create("div", { "class": "dijitDialogPaneActionBar"  }, d1.containerNode);
		
			        var buttonOk = new dijit.form.Button({"label": "Ok",
			            onClick: function(){
			            	
							if(callbackYes!=null && typeof callbackYes === "function"){
								callbackYes();
							}
							
							d1.destroyRecursive();
							if(fieldtoFocus!=null && fieldtoFocus!= undefined){
								focus.focus(fieldtoFocus);
							}
			            	
			            } });
			        
			        buttonOk.placeAt(actionBar);
			        domclass.add(buttonOk.focusNode, "yesNoOkButton");
			        
			      var buttonCancel = new dijit.form.Button({"label": "Cancel", 
			            onClick: function(){
			            	
							if(callbackNo!=null && typeof callbackNo === "function"){
								callbackNo();
							}
							d1.destroyRecursive();
							if(fieldtoFocus!=null && fieldtoFocus!= undefined){
								focus.focus(fieldtoFocus);
							}
			            	
			            	} });
			        
			        buttonCancel.placeAt(actionBar);
			        domclass.add(buttonCancel.focusNode, "cancelButton")
            }
            
	        d1.startup();
            d1.show();
			
		},
		
		showBillToBillFromShipTo: function (visible) {
			
			if(visible){
				dojo.style(this.billTr,"display","table-row");
				dojo.style(this.shipTr,"display","table-row");
			} else {
				dojo.style(this.billTr,"display","none");
				dojo.style(this.shipTr,"display","none");
			}
    		
		},
		
		showGstRegNumLoadIndicator: function (visible) {
			
			if(visible){
				dojo.style(this.supplierGstRegNumLoadIndicatorTr,"display","table-row");
			} else {
				dojo.style(this.supplierGstRegNumLoadIndicatorTr,"display","none");
			}
		},
		
		
		showIGST : function(visible) {
			
			if(visible) {
	    		domStyle.set(dojo.byId(this.id+"_igstTr"),"display","table-row");
	    		domStyle.set(dojo.byId(this.id+"_igstLabelTd"),"display","table-cell");
	    		domStyle.set(dojo.byId(this.id+"_igstTd") ,"display","table-cell");
	    		domStyle.set(this.igst.focusNode ,"display","table-cell");
			} else {
	    		domStyle.set(dojo.byId(this.id+"_igstTr"),"display","none");
	    		domStyle.set(dojo.byId(this.id+"_igstLabelTd"),"display","none");
	    		domStyle.set(dojo.byId(this.id+"_igstTd") ,"display","none");
	    		domStyle.set(this.igst.focusNode ,"display","none");
			}
		},
		
		showCGST : function(visible) {
			
			if(visible) {
	    		domStyle.set(dojo.byId(this.id+"_cgstTr"),"display","table-row");
	    		domStyle.set(dojo.byId(this.id+"_cgstLabelTd"),"display","table-cell");
	    		domStyle.set(dojo.byId(this.id+"_cgstTd") ,"display","table-cell");
	    		domStyle.set(this.cgst.focusNode ,"display","table-cell");
			}else {
	    		domStyle.set(dojo.byId(this.id+"_cgstTr"),"display","none");
	    		domStyle.set(dojo.byId(this.id+"_cgstLabelTd"),"display","none");
	    		domStyle.set(dojo.byId(this.id+"_cgstTd") ,"display","none");
	    		domStyle.set(this.cgst.focusNode ,"display","none");
			}
		},
		
		showSGST : function(visible) {
			
			if(visible) {
	    		domStyle.set(dojo.byId(this.id+"_sgstTr"),"display","table-row");
	    		domStyle.set(dojo.byId(this.id+"_sgstLabelTd"),"display","table-cell");
	    		domStyle.set(dojo.byId(this.id+"_sgstTd") ,"display","table-cell");
	    		domStyle.set(this.sgst.focusNode ,"display","table-cell");
			}else {
	    		domStyle.set(dojo.byId(this.id+"_sgstTr"),"display","none");
	    		domStyle.set(dojo.byId(this.id+"_sgstLabelTd"),"display","none");
	    		domStyle.set(dojo.byId(this.id+"_sgstTd") ,"display","none");
	    		domStyle.set(this.sgst.focusNode ,"display","none");
			}
		},
		
		

		openVendorSearchDialog: function(evt) {
				
				var dialogSupplier = null;
				var buttonClicked =  registry.getEnclosingWidget(evt.target);
				dialogSupplier = new SupplierSearchDialog({	searchType:buttonClicked.searchtype, callerForm:thisForm, serviceParameters: this.serviceParameters  });
				dialogSupplier.showSupplierSearchDataGrid();
		},
	
		 /***
	     *Method : 	openVendorSearchDialogSupplierName	   
	     * Usage :  Used to POPUP VENDERNAMES FOR NON INDEX INVOICE AND NON PAYMENT INVOICE.
	     * 
	     * */
		openVendorSearchDialogSupplierName: function() {
			
			 if(!this.isFullyPaid() && !this.isPartiallyPaid()){
				 var dialogSupplier = null;
					dialogSupplier = new SupplierSearchDialog({	searchType:'supplierName', callerForm:thisForm, serviceParameters: this.serviceParameters  });
					dialogSupplier.showSupplierSearchDataGrid();
			 }
		},
			
		openVendorSearchDialogSupplierNumber: function() {
				
				var dialogSupplier = null;
				dialogSupplier = new SupplierSearchDialog({	searchType:'supplierNumber', callerForm:thisForm, serviceParameters: this.serviceParameters  });
				dialogSupplier.showSupplierSearchDataGrid();	
		},
			
		openVendorSearchDialogAccountNumber: function() {

				var dialogSupplier = null;
				dialogSupplier = new SupplierSearchDialog({	searchType:'accountNumber', callerForm:thisForm, serviceParameters: this.serviceParameters  });
				dialogSupplier.showSupplierSearchDataGrid();	
		},
		
		// story 1582060 
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
		// end story 1582060 
	});
		return InvoiceDialog;		
});
