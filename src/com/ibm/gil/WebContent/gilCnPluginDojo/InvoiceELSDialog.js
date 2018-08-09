define([ "dojo/_base/declare",
         "dojo/_base/lang",
         "ecm/model/Request", 
         "ecm/widget/dialog/BaseDialog",
         "dojo/parser",
         "dojo/dom-style",
 	    "dijit/registry",
         "dijit/layout/TabContainer",
		"dijit/layout/ContentPane",
		"dojo/text!./templates/InvoiceELSDialog.html",
        "dijit/_WidgetsInTemplateMixin",
        "dojo/_base/json",
	    "dojo/date/locale",
		"gilCnPluginDojo/util/JsonUtils",
		"gilCnPluginDojo/util/MathJs",
        "gilCnPluginDojo/ELSDialog",
        "gilCnPluginDojo/LineItemsDialog",
        "dijit/Tooltip",
	    "dojo/date/stamp",
	    "dijit/focus",
	    "dojo/_base/connect", 
	    "dojo/_base/event",
	    "gilCnPluginDojo/SupplierSearchELSDialog",
	    "gilCnPluginDojo/CustomerSearchELSDialog",
	    "dojox/grid/DataGrid",
	    "dojo/data/ItemFileWriteStore",
	    "dojo/io-query",
	    "dojo/query",
	    "dojo/dom-construct",
	    "dijit/form/DateTextBox",
	    "dojo/parser",
	    "dojo/dom-attr",
	    "gilCnPluginDojo/ConfirmDialog",
	    "gilCnPluginDojo/NonModalConfirmDialog",
	    "gilCnPluginDojo/DialogMessages",
	    "gilCnPluginDojo/GILDialog",
	    "gilCnPluginDojo/ReferenceInvoicePickerDialog",
	    "gilCnPluginDojo/ErrorMessage",
	    "gilCnPluginDojo/USStateDialog2",
	    "dojo/date",
	    "dojo/i18n",
	    "dojo/window",
	    "dojo/dom-class",
	    "gilCnPluginDojo/CommentsDialog",
	    "dojo/aspect",
	    "dojo/dom",
	    "dojo/on",
	    "gilCnPluginDojo/constants/ValidationConstantsCA",
	    "dojo/domReady!",

	    
	    ],
		function(declare, lang, Request, BaseDialog, parser, domStyle, registry, TabContainer, ContentPane,
				template, _WidgetsInTemplateMixin, json, locale, JsonUtils, MathJs, ELSDialog, LineItemsDialog,
				Tooltip, stamp, focus, connect, event, SupplierSearchELSDialog, CustomerSearchELSDialog,
				DataGrid, ItemFileWriteStore,ioquery, query, construct, DateTextBox, parser, domattr, ConfirmDialog,
				NonModalConfirmDialog, DialogMessages, GILDialog, ReferenceInvoicePickerDialog,ErrorMessage, USStateDialog2,
				date, i18n, window,domclass, CommentsDialog, aspect, dom, on, ValidationConstantsCA) {
				
		
	var InvoiceELSDialog =  declare("gilCnPluginDojo.InvoiceELSDialog", [ BaseDialog, ELSDialog, GILDialog ], {
		contentString : template,
		widgetsInTemplate : true,
		jsonInvoiceEls:null,
		option:"",
		that:null,
		isAddListDbCr:false,
		isAddListInvType:false,
		isAddInvCurListener:false,
		isAddPoexListener:false,
		poexCodesforInvoiceType:null,
		defaultCountryCurrency:null,
		dialogCounter:null,
		counterCancel:null,
		ADDMODE:'1',
		tmpInvType:null,
		invoiceDate:null,
		defaultVENPoex:null,
		defaultCOMPoex:null,
		contentAreaH:null,
		usTaxStateSelected:null,
		
		postCreate : function() {
			
			
			this.inherited(arguments);
			this.setResizable(true);
			var screen = window.getBox();
			var width = 1570;
			var height = 810;
			
			//if the dialog is larger than the screen, resize it
			if(width > screen.w  ){
				console.log("Screen.w: "+screen.w +"Width:" +width);
				width = screen.w * 0.94;

			}
			if(height >screen.h){
				console.log("Screen.h: "+screen.h +"Height:" +height);
				height = screen.h - 10;
			}
			
			this.setSize(width, height);
			this.showActionBar(true);
			dojo.style(this.cancelButton.domNode,"display","none");//removing  from view the default cancel button
			this.addButton("Cancel", "close", false, true);
			this.addButton("Save", "validateBeforeSave", false, true);
			this.addButton("Comments", "openCommentsDialog", false, true);
			this.addButton("CTS Call", "ctsCall", false, true,this.id+"_cts");
			domStyle.set(registry.byId(this.id+"_cts_dijit_form_Button_0").domNode, 'display', 'none');
			this.addButton("Line Items", "openLineItemsDialog", false, true);
			
			
			
			//will not display until Taxes checkbox is checked
			
			debugger;
			that=this;
			poexCodesforInvoiceType={};
			poexCodesforInvoiceType["VEN"] = "US01,US08,US11";
			poexCodesforInvoiceType["SWV"] = "US04,US07,US10";
			poexCodesforInvoiceType["IBM"] = "US03,US06,US09";
			poexCodesforInvoiceType["COM"] = "US02";
			this.initializeFields();
			this.checkInvDate();

			this.hideFieldsForCountry();
			this.toChange();
			this.setWidgetsInitialization();
			that.dialogCounter=0;
			that.counterCancel=0;
			that.validateNumberFields();
			that.initializeDateFields();
			that.upperCaseFields();
			that.setLayout();
			that.disableEnterForInputs();			
			
			
		},
		setLayout:function(){
		var restoreGrid = (foo) => {
			if(foo != null){
				
				if(!domclass.contains(this.actionBar,"invoiceActionBarR")){			
					domclass.add(this.actionBar,"invoiceActionBarR");
					domclass.remove(this.actionBar, 'invoiceActionBarM');
				}
//				var ctsbutton=this.id+"_cts_dijit_form_Button_0";
//				if(registry.byId(this.id+"_cts_dijit_form_Button_0").domNode){
//					domStyle.set(registry.byId(this.id+"_cts_dijit_form_Button_0").domNode,'display', 'block');
//		    		domStyle.set(registry.byId(this.id+"_cts_dijit_form_Button_0").domNode,'display', 'initial');
//				}
				
				if(this.taxes.get('value')=="true"){
		    		
					
		    		domStyle.set(registry.byId(this.id+"_cts_dijit_form_Button_0").domNode,'display', "");
		    		domStyle.set(this.contentArea,'height', this.contentAreaH+"px");
//		    		dijit.byId(this.id+"_cts_dijit_form_Button_0").set("disabled",true);
		     		
		    	}else{

//		    		domStyle.set(dojo.byId(this.id+"_cts_dijit_form_Button_0"), 'display', 'none');//GilCnPluginDojo_InvoiceELSDialog_0_cts_dijit_form_Button_dijit_form_Button_0
//		    		domStyle.set(registry.byId(nodeId).domNode, 'display', 'none');
		    		domStyle.set(registry.byId(this.id+"_cts_dijit_form_Button_0").domNode, 'display', 'none');
		    	}
				
				
			}
		};
		//DataGrid
		var maximizeGrid = (foo) => {
			if(foo != null){
				
				if(!domclass.contains(this.actionBar,"invoiceActionBarM")){			
					domclass.add(this.actionBar,"invoiceActionBarM");
					domclass.remove(this.actionBar, 'invoiceActionBarR');
				}
				if(this.taxes.get('value')=="true"){
					
		    		domStyle.set(registry.byId(this.id+"_cts_dijit_form_Button_0").domNode,'display', 'block');
		    		
//		    		dijit.byId(this.id+"_cts_dijit_form_Button_0").set("disabled",true);
		     		
		    	}else{

//		    		domStyle.set(dojo.byId(this.id+"_cts_dijit_form_Button_0"), 'display', 'none');//GilCnPluginDojo_InvoiceELSDialog_0_cts_dijit_form_Button_dijit_form_Button_0
//		    		domStyle.set(registry.byId(nodeId).domNode, 'display', 'none');
		    		domStyle.set(registry.byId(this.id+"_cts_dijit_form_Button_0").domNode, 'display', 'none');
		    	}
					
			}
		};
//		aspect.after(this.resizeHandle, 'onResize', resizeGrid, thisDialog.grid);
		aspect.after(this._maximizeButton, 'onClick', maximizeGrid, this._maximizeButton);
		aspect.after(this._restoreButton, 'onClick', restoreGrid, this._restoreButton);
		domclass.add(this.actionBar,"invoiceActionBarR");
	},

		validateNumberFields:function(){
//			utilGilDialog=new GILDialog();
			that.isNumberKey(that.id+"_invAmt");
			that.isNumberKey(that.id+"_taxAmt");
			that.isNumberKey(that.id+"_netAmt");
			that.isNumberKey(that.id+"_lnItemTot");
			that.isNumberKey(that.id+"_lnItemTxTot");
			that.isNumberKey(that.id+"_lnItemNetTot");
			that.isNumberKey(that.id+"_exchgRate");
			that.isNumberKey(that.id+"_taxBilledIgf");
			that.isNumberKey(that.id+"_taxBilledCus");
			
			that.isDateKey(that.id+"_invDate");
			
			
					
			
			
			
		},
		show: function() {
			this.inherited("show", []);
		},
		ctsCall:function(){
			that.openUSState();
		},
		
		setWidgetsInitialization: function() {
			
			
			dojo.style(this.cancelButton.domNode,"display","none"); 
	        that.disableListeners();
			
	        
		},
		onChangeInvoiceType: function() {
	    	
		     on(dijit.byId(that.id+ "_invoiceType"), "change", function(event) {
					var value=that.invoiceType.get("value");
					
		    	 that.enableReferenceInvoiceBasedOnInvType(value);
					that.changeDefaultPOEXCodes(value);
					that.checkInvType(value);
					that.removeQuoteLineitems(value);
		    	

		     });
		}, 
		onChangeOLCS:function(){
			 on(dijit.byId(that.id+ "_olCustNum"), "blur", function(event) {
				 	if(that.olCustNum.get('value').trim()== ''){
				 		
						that.cleanOLCustomerValues();
									  
					}else{
						that.openOLCS();
					}
		     });
		},
		toChange:function(){
			that=this;

			//Defect 1836083- Found it was workinf fluently adding here the events
			dojo.connect(dojo.byId(this.id+"_legalId"),"change", function(){
				that.openVendorSearchELSDialogLegalId();
				});
			dojo.connect(dojo.byId(this.id+"_supplierNumber"),"change", function(){
				that.openVendorSearchELSDialogSupplierNumber();
				});
			dojo.connect(dojo.byId(this.id+"_supplierName"),"change", function(){
				that.openVendorSearchELSDialogSupplierName();
				});
			dojo.connect(dojo.byId(this.id+"_taxes"),"change", function(){
				that.enableTaxesforIGFandCustomer();
				});
			
			/**
			 * Changes for Story 1750051 - [ELS Canada] CA GIL changes
			 */	
			dojo.connect(dojo.byId(this.id+"_billedToCustomer"),"change", function(){
				option="recalculateInvAmt";
				that.changeValidation();
				});
			/**
			 * End Changes for Story 1750051 - [ELS Canada] CA GIL changes
			 */	
			
			
			dojo.connect(dojo.byId(this.id+"_invNum"),"change", function(){
				
				that.checkInvNum(null);
				});
			
			dojo.connect(dojo.byId(this.id+"_invAmt"),"change", function(){
				
				if(that.invAmt.get('value')=="")that.invAmt.set("0.00");
				option="recalculateInvAmt";
				that.changeValidation();
				});
//			dojo.connect(dojo.byId(this.id+"_taxAmt"),"change", function(){
//				if(that.taxAmt.get('value')=="")that.taxAmt.set("0.00");
//				option="recalculateVatAmt";
//				that.changeValidation();
//				});
			
			dojo.connect(dojo.byId(this.id+"_taxAmt"),"blur", function(){
				debugger;
				if(that.taxAmt.get('value')=="")that.taxAmt.set("0.00");
				var tmp=parseFloat(that.taxAmt.get('value'))||0;
//				if(tmp == 0){
//					
//					option="recalculateVatAmt";
//					that.changeValidation();
//				}
				
				option="recalculateVatAmt";
				that.changeValidation();
				});
			dojo.connect(dojo.byId(this.id+"_olNum"),"change", function(){
				option="searchByOffering";
				that.changeValidation();
				});
			
			/*******Defect: Opening Customer Search Dialog  was being **************************
			 * displayed after loading the invoice dialog, when it was reindexed****************
			 * Had to add next lines instead of calling the event from div**********************/
			
			dojo.connect(dojo.byId(this.id+"_instCustNum"),"change", function(){
				
				that.openCS();
				});
			dojo.connect(dojo.byId(this.id+"_cityS"),"change", function(){
				
				that.openCSN();
				});
			/***********************************************************************************/
			dojo.connect(dojo.byId(this.id+"_refInvNum"),"change", function(){
				option="searchByReferenceInvoice";
				that.changeValidation();
				});
			
			dojo.connect(dojo.byId(this.id+"_taxBilledIgf"),"change", function(){
				var tmp=0.00;
				
				if(that.taxBilledIgf.get('value')=="")that.taxBilledIgf.set("0.00");
				tmp=that.taxBilledIgf.get('value');				
		    	that.taxBilledIgf.set('value',that.formatNumber(tmp,2));
		    	
				});
			dojo.connect(dojo.byId(this.id+"_taxBilledCus"),"change", function(){
				
				var tmp=0.00;
				if(that.taxBilledCus.get('value')=="")that.taxBilledCus.set("0.00");
				tmp=that.taxBilledCus.get('value');
				that.taxBilledCus.set('value',that.formatNumber(tmp,2));
				
				});
			
		},

		adddbCrListener:function(){
			that=this;
			debugger;
			if(that.isAddListDbCr==false){
			var dd=dojo.byId(this.id+"_dbCr_menu");
			var opt=dd.getElementsByClassName("dijitReset dijitMenuItemLabel");
			var opts=Array.from(opt);
			opts.forEach(function(item){item.addEventListener("click",function(){
				console.log(item.innerText);				
				that.enableReferenceInvoiceBasedOnDBCR(item.innerText);
				});});
			that.isAddListDbCr=true;
			}
		},
		addInvTypeListener:function(){
			that=this;
			debugger;
			if(that.isAddListInvType==false){
			var dd=dojo.byId(this.id+"_invoiceType_menu");
			var opt=dd.getElementsByClassName("dijitReset dijitMenuItemLabel");
			var opts=Array.from(opt);
			opts.forEach(function(item){item.addEventListener("click",function(){
				console.log(item.innerText);				
				that.enableReferenceInvoiceBasedOnInvType(item.innerText);
				that.changeDefaultPOEXCodes(item.innerText);
				that.checkInvType(item.innerText);
				that.removeQuoteLineitems(item.innerText);
				});});
			that.isAddListInvType=true;
			}
		},
		addPoexListener:function(){
			that=this;
			debugger;
			if(that.isAddPoexListener==false){
			var dd=dojo.byId(this.id+"_poex_menu");
			var opt=dd.getElementsByClassName("dijitReset dijitMenuItemLabel");
			var opts=Array.from(opt);
			opts.forEach(function(item){item.addEventListener("click",function(){
//				console.log(item.innerText);				
//				that.enableReferenceInvoiceBasedOnInvType(item.innerText);
//				that.changeDefaultPOEXCodes(item.innerText);
				that.checkInvType(that.invoiceType.get('value'),null,item.innerText);//item.innerText);
				});});
			that.isAddPoexListener=true;
			}
		},
		addInvCurListener:function(){
			that=this;
			debugger;
			if(that.isAddInvCurListener==false){
			var dd=dojo.byId(this.id+"_invCur_menu");
			var opt=dd.getElementsByClassName("dijitReset dijitMenuItemLabel");
			var opts=Array.from(opt);
			opts.forEach(function(item){item.addEventListener("click",function(){
				console.log(item.innerText);				
				that.enableExchangeRateBasedonCurrency(item.innerText);
			});});
			that.isAddInvCurListener=true;
			}
		},
		removeQuoteLineitems: function(item){
			
			if(jsonInvoiceEls.DOCUMENTMODE==this.ADDMODE){
				
				if(item=="COM" 	|| item=="STP" 	|| item =="RBD"){
					if(that.hasQuoteLineItems()){
						var dialogMsgs=new DialogMessages();
						dialogMsgs.addConfirmMessages("Changing invoice type.  Remove quote line items?");
						that.showMessage("Confirm",dialogMsgs.confirmMessages(), true, that.choiceRemoveY,that.choiceRemoveN);
					}
				}
			}
		},
		choiceRemoveY:function(){
			
			that.serviceParameters['jsonInvoiceEls']= JSON.stringify(jsonInvoiceEls);
			that.serviceParameters['request']="removeQuoteLineItems"
			
			ecm.messages.progress_message_InvoiceELSService = "Removing Quote Line Items...";
			 
			
			Request.postPluginService("GilCnPlugin","InvoiceELSService",'application/x-www-form-urlencoded',{
				
				requestBody : ioquery.objectToQuery(that.serviceParameters),
				requestCompleteCallback: lang.hitch(that,function(response) {
					debugger;
					
					jsonInvoiceEls=JSON.parse(response.invoiceElsJson);

					var dialogMsgs=new DialogMessages();
					if(jsonInvoiceEls.dialogErrorMsgs.length>0){
						var msgs=jsonInvoiceEls.dialogErrorMsgs;
						for (var i in msgs) {
							dialogMsgs.addErrorMessages(msgs[i]);
						}
					}
					debugger;
					if(dialogMsgs.hasErrorMessages()){
						
							that.showMessage("Error",dialogMsgs.errorMessages(),false, null, null);
												
					}else{//if there are no errors then update fields
						
						that.lnItemTot.set("value",jsonInvoiceEls.lineItemTotalAmount);
						that.lnItemTxTot.set("value",jsonInvoiceEls.lineItemVatAmount);
						that.lnItemNetTot.set("value",jsonInvoiceEls.lineItemNetAmount);
						

						
					}
	

				})
			
			});
		
		

		}, 
		choiceRemoveN:function(){
 
			that.invoiceType.set('value',tmpInvType);
		},
		hasQuoteLineItems:function(){
			if(jsonInvoiceEls.lineItems!=null && jsonInvoiceEls.lineItems.length!=0){
				for(var i in jsonInvoiceEls.lineItems ){
					if(jsonInvoiceEls.lineItems[i].FROMQUOTE=="Y"){
						return true;
					}
				}
			}
			return false;
		},
		validateTMFromQuote:function(){
			if(jsonInvoiceEls.lineItems!=null && jsonInvoiceEls.lineItems.length!=0){
				for(var i in jsonInvoiceEls.lineItems ){
					if(jsonInvoiceEls.lineItems[i].FROMQUOTE=="Y" &&
							(jsonInvoiceEls.lineItems[i].DESCRIPTION=="" || jsonInvoiceEls.lineItems[i].VATCODE=="")){
						var dialogMsgs=new DialogMessages();
						console.log("TM description or vatcode missing, check line items coming from OL.")
						dialogMsgs.addErrorMessages("Type/Model not found.");
						that.showMessage("Error",dialogMsgs.errorMessages(), false, null,null);
						
						return false;
						
					}
				}
			}
			return true;
		},
		checkIndexingCombination:function(invType,poexItem,currency,compCode,msg) {
			
			var poexCd = poexItem.substring(0, 4);
			
			if(this.serviceParameters['country']=='CA')
			{
				
				var allowed = {};
				allowed['INVTYPE'] = ValidationConstantsCA("INVOICE_TYPE_FOR_"+ poexCd);
				allowed['CURRENCY'] = ValidationConstantsCA("CURRENCY_FOR_"+ poexCd);
				allowed['COMPCODE'] = ValidationConstantsCA("COMPANY_CODE_FOR_"+ poexCd);
				
				var combination = true;  
	    		if(allowed['INVTYPE'].includes(invType) && allowed['CURRENCY'].includes(currency) && allowed['COMPCODE'].includes(compCode)){			
	    			combination = true;
	    		}else{
	    			combination = false;
	    		}
				
	    		
	    		if (!combination) {
	    			var errorMsg = ValidationConstantsCA("COMBINATION_ERROR")
					    			.replace('#pcode',poexItem)
					    			.replace('#itype',allowed['INVTYPE'])
					    			.replace('#cur',allowed['CURRENCY'])
					    			.replace('#cc',allowed['COMPCODE'] );
	    			
	    			if(allowed['INVTYPE']!="" && allowed['CURRENCY']!="" && allowed['COMPCODE']!=""){
	    				return errorMsg;
	    			}
	    		}
	    		return "";
			}
		},
		
		checkInvType:function(item,message,poexItem){
			that=this;
			that.invoiceForSWVinvoiceType(item);
			var error="";
			
			if(item.startsWith("RBD") && this.dbCr.get('value')!="CR"){
				error="RBD invoice type must be CR";
				if(message==null){
					dialogMsgs=new DialogMessages();
					dialogMsgs.addErrorMessages(error);
					that.showMessage("Error",dialogMsgs.errorMessages(), false, null,null);
				}else{
					message.addErrorMessages(error);
				}
			}
			if(poexItem==null){
				poexItem=this.poex.get('value');
			}
			if(this.serviceParameters['country']=='US'&& (item.toUpperCase()=="SWV" || item.toUpperCase()=="IBM" ||
					item.toUpperCase()=="COM" || item.toUpperCase()=="VEN")){
				if(!poexCodesforInvoiceType[item].includes(poexItem.substring(0,4))){
					 error="POEX code is not valid for this type of invoice. Please select valid poexcode.";
					if(message==null){
						dialogMsgs=new DialogMessages();
						dialogMsgs.addErrorMessages(error);
						that.showMessage("Error",dialogMsgs.errorMessages(), false, null,null);
				
					}else{
						message.addErrorMessages(error);
					}
				}
			}
		},
		invoiceForSWVinvoiceType:function(item){
			debugger;
			that=this;
			if(item==null){
				item=this.invoiceType.get('value');
			}
			if(this.serviceParameters['country']=='US'){
				
				if(item=="SWV" && jsonInvoiceEls.DOCUMENTMODE=="1"){
					 var tempInvNum=jsonInvoiceEls.invoiceNumber;
					if(this.olNum.get('value').trim().length>0 &&
							this.olCustNum.get('value').trim().length>0){
						tempInvNum="SWV" + this.olNum.get('value').trim();
					}
					jsonInvoiceEls['invoiceNumber']=tempInvNum;
					this.invNum.set('value', tempInvNum);
				}
			}	
		},
		


		
		checkInvNum:function(message){
			that=this;
			that.invoiceForSWVinvoiceType(null);
			if(this.serviceParameters['country']=='US'){
				
				if(this.invNum.get('value').toUpperCase().startsWith("SWV")&&
						this.invoiceType.get('value').toUpperCase()!="SWV"){
					var error="Invoice type must be SWV";
						if(message==null){
							dialogMsgs=new DialogMessages();
							dialogMsgs.addErrorMessages(error);
							that.showMessage("Error",dialogMsgs.errorMessages(),
									false,null,null);
							this.invNum.set('value',jsonInvoiceEls.invoiceNumber);
						}else{
							message.addErrorMessages(error);
						}
				}
				
			}
		},
		checkInvDate:function(){
			that=this;
			lang.getObject('invDate', false, this).validator = function() {
			    
		        
				
			    var parsedInvoiceDate = locale.parse( that.invDate.get('displayedValue'), {
				        locale:"en-us",
				        selector: 'date'
				    });
			    
			    
			    that.invoiceDate=parsedInvoiceDate;
			    console.log("invoiceDate: "+that.invoiceDate);
			    if(parsedInvoiceDate == null){
			    	
			    	that.invDate.set("invalidMessage","Invoice Date fomat must have format "+['dateFormat-short']);	
			    	
			    }			    
			    
			    return true;
			}

		},
		validateDate:function(){

			if (that.invoiceDate!=null) {
				
				if (date.difference(that.invoiceDate,new Date()) > 300	)	{
					
					var dialogMsgs=new DialogMessages();
					dialogMsgs.addWarningMessages("Invoice date is greater than 300 days old and specific approvals may apply for processing. Do you wish to correct ?");

	    			that.showMessage("Warning",dialogMsgs.warningMessages(), true, that.choiceYdate, that.validateInput);

				}
				else{
					that.validateInput();
				}
			}	else{
				that.choiceYdate();
			}
		    
		},
		choiceYdate:function(){
			that.requestFocus(that.id+"_invDate");
		},
		validateInput:function(){
			
			
			
			this.msg=new DialogMessages();
			thisValidate=this;
			debugger;

			dialogMessage=new DialogMessages();
			
			that.checkInvNum(dialogMessage);
//			addWarningMessages: function(strMsg) {
//			 addInfoMessages: function(strMsg) {
			
			if(jsonInvoiceEls.invoiceNumber.trim().length==0){
				thisValidate.msg.addErrorMessages("Invoice Number is a required field.");
			}
			if(jsonInvoiceEls.invoiceNumber.trim().length>=16)
				thisValidate.msg.addErrorMessages("Invoice Number must not be greater than 15 characters in length.");
			if(jsonInvoiceEls.invoiceDate==" / / ")
				thisValidate.msg.addErrorMessages("Invoice Date is a required field.");
			if(jsonInvoiceEls.toleranceIndc=="N")
				thisValidate.msg.addErrorMessages("Taxes on the invoice are not within the states tax tolerance values." );
			
			if(that.serviceParameters['country']=='US' 
				&& jsonInvoiceEls.invoiceNumber.trim().toUpperCase().startsWith("SWV") 
					&& jsonInvoiceEls.invoiceType.toUpperCase()!=("SWV") ){
				thisValidate.msg.addErrorMessages("Invoice type must be SWV.");
			}
			that.checkInvType(that.invoiceType.get('value'),thisValidate.msg,null);
			
			var totAmt=parseFloat(jsonInvoiceEls.totalAmount)||0;
			if(totAmt<=0){
				thisValidate.msg.addErrorMessages("Invoice Amount must be > 0.00");
			}
			var vatAmt=parseFloat(jsonInvoiceEls.vatAmount)||0;
			if(vatAmt<0){
				thisValidate.msg.addErrorMessages("Tax Amount must be >= 0.00");
				
			}
			var netAmt=parseFloat(jsonInvoiceEls.netAmount)||0;

			if(netAmt<=0){
				var netAmtMsg=new DialogMessages();
				netAmtMsg.addErrorMessages("Net Amount must be > 0.00");
				
				that.showMessage("Error",netAmtMsg.errorMessages(),false, null, null,"onlyOk");
				return false;
			}else if(vatAmt.toFixed(2)!=0 && netAmt.toFixed(2)<vatAmt){
				thisValidate.msg.addErrorMessages("Tax amount cannot be more than the Net amount.");				
			
			}
			var vatB=parseFloat(jsonInvoiceEls.vatBalance) || 0;
			var netB=parseFloat(jsonInvoiceEls.netBalance) || 0;
			if(vatB!=jsonInvoiceEls.vatAmount || netB !=jsonInvoiceEls.netAmount || 
					jsonInvoiceEls.invoiceStatus !="INEW" ||jsonInvoiceEls.invoiceStatus !="IRESRCH"){
				if(jsonInvoiceEls.originalDbCr.trim().length>0 && jsonInvoiceEls.originalDbCr != jsonInvoiceEls.dbCr){
					thisValidate.msg.addErrorMessages("DB/CR Indicator cannot be changed for configured Invoice.");
				}
			}
			debugger;
			if(jsonInvoiceEls.configuredInvIndc=="Y"){
				thisValidate.msg.addErrorMessages("All items on this invoice are currently configured in GCMS.\nNo changes to the invoice are allowed in GIL at this time.");
			}
			if(jsonInvoiceEls.vendorName.trim().length ==0 &&
					jsonInvoiceEls.vendorNum.trim().length==0){
				thisValidate.msg.addErrorMessages("Vendor Name or Vendor Number are required fields.");
			}
			
			console.log("Invoice ELS Dialog -validating- VendorCommission value: "+ jsonInvoiceEls.vendorCommission);
			if(jsonInvoiceEls.invoiceType== "COM" && (jsonInvoiceEls.srNumber.trim()== "EXEMPT" || jsonInvoiceEls.srNumber.trim().startsWith("DEV")) ){ //Test Defect 1852013
				thisValidate.msg.addErrorMessages("Commission Invoices are not valid for this supplier.");
				
			}
			if(jsonInvoiceEls.vatRegistrationNumReq=="Y" && jsonInvoiceEls.vatRegistrationNumber.trim().length ==0){
				thisValidate.msg.addErrorMessages("Legal Id Number is a required field.");
			}
			
			if(jsonInvoiceEls.currency != that.olCur.get('value')){
				var xR=parseFloat(jsonInvoiceEls.exchangeRate) ||0;
				if(jsonInvoiceEls.offeringLetterNum.trim().length !=0 ){
					
					if(xR ==1){
						thisValidate.msg.addErrorMessages("Exchange rate must not be 1.0");
					}
					
				}
				if(xR==0){
					thisValidate.msg.addErrorMessages("Exchange rate must not be 0.0");
				}
			}
			if(jsonInvoiceEls.rofInvoiceIndc=="Y" && jsonInvoiceEls.dbCr =="CR"){
				
				thisValidate.msg.addErrorMessages("ROF Invoice must be debit invoice only.");
			}
			var customerValid=true;//changing this value as it is in cm8.4
			if(jsonInvoiceEls.installedCustomerName.trim().length !=0 && jsonInvoiceEls.installedCustomerNum.trim().length!=0){
				customerValid=true;
			}
	
			if(customerValid==false &&	(jsonInvoiceEls.offeringLetterNum !=null) &&
					jsonInvoiceEls.offeringLetterNum.trim().length>0){
				thisValidate.msg.addErrorMessages("Customer doesn't exist.");
			}
			
			if(jsonInvoiceEls.offeringLetterNum.trim().length !=0  &&
					jsonInvoiceEls.customerNum.trim().length !=0 &&
					jsonInvoiceEls.oLCustomerNum!=jsonInvoiceEls.customerNum){
				thisValidate.msg.addErrorMessages("Customer Number must be blank or equal to the Offer Letter Customer Number.");
			}
			if(jsonInvoiceEls.country=="BR" && jsonInvoiceEls.companyCode.trim().length =="0001" &&
					jsonInvoiceEls.invoiceType.trim()!="IBM" &&jsonInvoiceEls.invoiceType !="COM"){
				thisValidate.msg.addErrorMessages("Please select Invoice Type of IBM or COM for the Comp Code of 0001 and Country BR.");
			}
			
			if(jsonInvoiceEls.lineItems.length==0){
				thisValidate.msg.addErrorMessages("Please add the Line Items in the Line Items Dialog Screen.");
			}
				
			var indexCombMsg = that.checkIndexingCombination(that.invoiceType.get('value'),that.poex.get('value'),that.invCur.get("value"),that.compCode.get("value"),thisValidate.msg);
			
			if(thisValidate.msg.hasErrorMessages()){
				that.showMessage("Error",thisValidate.msg.errorMessages(),false, null, null);
				return false;
			} else if(indexCombMsg!=null && indexCombMsg.length>0) {
					  thisValidate.msg.addErrorMessages(indexCombMsg);
					  that.showMessage("Error",thisValidate.msg.errorMessages(),false, null, null);
			} else {	
				
				
				//request to server
				
	//			jsonInvoiceEls["dialogErrorMsgs"]= msg
				that.serviceParameters['request']=arguments.callee.nom;
				//clean server error messages
				
				that.serviceParameters['jsonInvoiceEls']= JSON.stringify(jsonInvoiceEls);
				
	
				ecm.messages.progress_message_InvoiceELSService = "Validating Invoice...";
				 
				
				Request.postPluginService("GilCnPlugin","InvoiceELSService",'application/x-www-form-urlencoded',{
					
					requestBody : ioquery.objectToQuery(that.serviceParameters),
					requestCompleteCallback: lang.hitch(that,function(response) {
						debugger;
						
						jsonInvoiceEls=JSON.parse(response.invoiceElsJson);
	
	
						if(jsonInvoiceEls.dialogErrorMsgs.length>0){
							var msgs=jsonInvoiceEls.dialogErrorMsgs;
							for (var i in msgs) {
								thisValidate.msg.addErrorMessages(msgs[i]);
							}
						}
						debugger;
						if(thisValidate.msg.hasErrorMessages()){
							
								that.showMessage("Error",thisValidate.msg.errorMessages(),false, null, null);
													
						}else{//ifthere no errors the it can be saved
							debugger;
							
							that.save();
							
						}
		
	
					})
				
				});
			
			
			}
			
		},
		
		/**
		 * Changes for Story 1750051 - [ELS Canada] CA GIL changes
		 */
		
		setIndcs: function(field){
			
			if(lang.getObject(field, false, this).get("value")){
				jsonInvoiceEls[field]="Y";
			} else {
				jsonInvoiceEls[field]="N";
			}
		},
		/**
		 * End Changes for Story 1750051 - [ELS Canada] CA GIL changes
		 */
		
		
		choiceYoL:function(){
			
			that.requestFocus(that.id+"_olTab");
			that.requestFocus(that.id+"_olNum");
			return;
		},
		
		loadAllInJsonInvoiceEls:function(){
			
			console.log("VendorCommission value loadAllJsonInvoice: "+jsonInvoiceEls.vendorCommission);
			
			jsonInvoiceEls["invoiceNumber"]=	this.invNum.get('value');
			jsonInvoiceEls["totalAmount"]= 	this.invAmt.get('value');
			jsonInvoiceEls["vatAmount"]= 		this.taxAmt.get('value');
			jsonInvoiceEls["netAmount"]=		this.netAmt.get('value');
			jsonInvoiceEls["dbCr"]=			this.dbCr.get('value');
			jsonInvoiceEls["currency"]=		this.invCur.get('value');
			jsonInvoiceEls["poexCode"]= 		this.poex.get('value');
			
			
			jsonInvoiceEls["invoiceDate"]=		this.invDate.get('displayedValue');
			jsonInvoiceEls["lineItemTotalAmount"]=this.lnItemTot.get('value');
			jsonInvoiceEls["lineItemVatAmount"]=	this.lnItemTxTot.get('value');
			jsonInvoiceEls["lineItemNetAmount"]=	this.lnItemNetTot.get('value');
			jsonInvoiceEls["referenceInvNum"]=	this.refInvNum.get('value');
			jsonInvoiceEls["exchangeRate"]= 		this.exchgRate.get('value');
			jsonInvoiceEls["invoiceType"]= 		this.invoiceType.get('value');
			jsonInvoiceEls["costCenter"]= 		this.costCenter.get('value');
			jsonInvoiceEls["purchaseOrderNum"]=	this.purchOrdNum.get('value');
			jsonInvoiceEls["companyCode"]=		this.compCode.get("value");
					
			if(jsonInvoiceEls.country=="US"){
			
				jsonInvoiceEls["taxesBilledToIgf"]=	this.taxBilledIgf.get('value');
				jsonInvoiceEls["taxesBilledToCust"]=	this.taxBilledCus.get('value');
				//supplier tab
				jsonInvoiceEls["customerAdd5"]=		this.statea5.get('value');	
				//installed customer tab
				jsonInvoiceEls["installedCustomerAdd5"]=this.statea6.get('value');
				if(jsonInvoiceEls.usTaxPercent==null )
					jsonInvoiceEls["ustaxPercent"]="";
			}
			
			/**
			 * Changes for Story 1750051 - [ELS Canada] CA GIL changes
			 */	
			if(jsonInvoiceEls.country=="CA"){
				
				jsonInvoiceEls["billedToCustomer"]=	this.billedToCustomer.get('value');
				jsonInvoiceEls["provinceCode"]=	this.shipToProvinceCode.get('value');
				//ol tab
				jsonInvoiceEls["province"]=		this.province.get('value');	
				jsonInvoiceEls["customerAdd5"]=		this.province.get('value');
				
			}
			/**
			 * End Changes for Story 1750051 - [ELS Canada] CA GIL changes
			 */
			
			

			that.loadIntoJsonIndcs();
			//OL tab
			jsonInvoiceEls["offeringLetterNum"]=	this.olNum.get('value');
			jsonInvoiceEls["oLCurrency"]=			this.olCur.get('value');
			jsonInvoiceEls["responsiblePartyId"]=	this.coaBsp.get('value');
			jsonInvoiceEls["oLCustomerNum"]=		this.olCustNum.get('value');
			jsonInvoiceEls["oLCustomerName"]=		this.custName.get('value');
			jsonInvoiceEls["customerAdd1"]=		this.olcustAdd.get('value');
			jsonInvoiceEls["customerAdd2"]=		this.olcustAdd2.get('value');
			jsonInvoiceEls["customerAdd3"]=		this.city.get('value');
			jsonInvoiceEls["customerAdd4"]=		this.postalC.get('value');
			
			//supplier search						
			jsonInvoiceEls["vendorName"]=		this.suppName.get('value');
			jsonInvoiceEls["vendorNum"]=		this.suppNumber.get('value');
			jsonInvoiceEls["vatRegistrationNumber"]=this.legalId.get('value');
			jsonInvoiceEls["srNumber"]=		this.saNumber.get('value');
			jsonInvoiceEls["srEndDate"]=		this.saDate.get('displayedValue');
			jsonInvoiceEls["vendorAdd1"]=		this.suppAddress.get('value');
			jsonInvoiceEls["vendorAdd2"]=		this.suppAddress2.get('value');
			jsonInvoiceEls["vendorAdd3"]=		this.suppAddress3.get('value');
			if (dojo.byId(this.id+"_distrNum")!=null){
				
				jsonInvoiceEls["distributorNum"]= registry.byId(this.id+"_distrNum").get('value');
			}
			//installed at customer tab			
			jsonInvoiceEls["installedCustomerNum"]=		this.instCustNum.get('value');
			jsonInvoiceEls["installedCustomerName"]=		this.custNameSearch.get('value');
			jsonInvoiceEls["installedCustomerAdd1"]=		this.custAdd.get('value');
			jsonInvoiceEls["installedCustomerAdd2"]=		this.custAdd2.get('value');
			jsonInvoiceEls["installedCustomerAdd3"]=		this.cityS.get('value');
			jsonInvoiceEls["installedCustomerAdd4"]=		this.postalCS.get('value');
			
			
			
			
			
			
		},
		loadIntoJsonIndcs:function(){
			if(jsonInvoiceEls.country=="US"){
				if(this.taxes.get('value')){
					jsonInvoiceEls['taxesIndc']="Y";
				}else{
					jsonInvoiceEls['taxesIndc']="N";
				}
			}else{
				jsonInvoiceEls['taxesIndc']="N";
			}
			
			/**
			 * Changes for Story 1750051 - [ELS Canada] CA GIL changes
			 */
			
			if(jsonInvoiceEls.country=="CA"){
				if(this.billedToCustomer.get('value')){
					jsonInvoiceEls['billedToCustomer']="Y";
				}else{
					jsonInvoiceEls['billedToCustomer']="N";
				}
			}else{
				jsonInvoiceEls['billedToCustomer']="N";
			}
			/**
			 * End Changes for Story 1750051 - [ELS Canada] CA GIL changes
			 */
			
			
			if(this.coaInv.get('value')){
				jsonInvoiceEls['coaWithInvIndc']="Y";
			}else{
				jsonInvoiceEls['coaWithInvIndc']="N";
			}
			
			if(this.exceptions.get('value')){
				jsonInvoiceEls['exceptionIndc']="Y";
			}else{
				jsonInvoiceEls['exceptionIndc']="N";
			}
			
			if(this.urgent.get('value')){
				jsonInvoiceEls['urgentIndc']="Y";
			}else{
				jsonInvoiceEls['urgentIndc']="N";
			}
			
			if(this.rof.get('value')){
				jsonInvoiceEls['rofInvoiceIndc']="Y";
			}else{
				jsonInvoiceEls['rofInvoiceIndc']="N";
			}
			
			if(this.rofOl.get('value')){
				jsonInvoiceEls['rofOLIndc']="Y";
			}else{
				jsonInvoiceEls['rofOLIndc']="N";
			}
			
		},
		changeValidation:function(){
			
			var tmp=0.00;
			debugger;
			switch(option) {

		    case "recalculateInvAmt":
		    	tmp=this.invAmt.get('value');
		    	this.invAmt.set('value',this.formatNumber(tmp,2));
	    		jsonInvoiceEls['totalAmount']=this.formatNumber(tmp,2);
				/**
				 * Changes for Story 1750051 - [ELS Canada] CA GIL changes
				 */
	    		this.setIndcs('billedToCustomer');
				/**
				 * End Changes for Story 1750051 - [ELS Canada] CA GIL changes
				 */
	    		
		    	if(this.serviceParameters['country']!='US'){

		    		
//					tmp=this.invAmt.get('value');
//		    		jsonInvoiceEls['totalAmount']=this.formatNumber(tmp,2);
		    		
		    		this.recalculateInvAmt();//recalculateNetAmount
		    	}
		        break;
		    case "recalculateVatAmt":
		    	registry.byId(that.id+'_taxAmt').set('_onBlurActive', false);
		    	tmp=this.taxAmt.get('value');
		    	this.taxAmt.set('value',this.formatNumber(tmp,2));
		    	jsonInvoiceEls['vatAmount']=this.formatNumber(tmp,2);
		    	
		    	if(this.serviceParameters['country']=='US'){
		    		option="usTaxCalculation";
		    		this.usTaxCalculation();
		    	}else{
		    		option="recalculateInvAmt";
		    		this.recalculateInvAmt();//before named recalculateNetAmount
		    	}
		        break;
		        
		    case "searchByOffering":
		    	
		    	if(jsonInvoiceEls.offeringLetterNum!=this.olNum.get('value')){
//			    	this.olNum.set("value",jsonInvoiceEls.offeringLetterNum);
		    		jsonInvoiceEls.offeringLetterNum=this.olNum.get('value').trim();
		    		this.loadAllInJsonInvoiceEls();
			    	this.searchByOffering();


		    	}

		        break;
		        

		    case "searchByReferenceInvoice":
		    	if(this.refInvNum.get('value')!=""){
		    		jsonInvoiceEls["referenceInvNum"]=this.refInvNum.get('value');
		    		jsonInvoiceEls["dbCr"]=this.dbCr.get('value');
		    		that.searchByReferenceInvoice();
		    	}
		    	break;
		} 
			
		},changeDefaultPOEXCodes:function(item){
			that=this;
			if(item=="COM"){
		 /***	
	     ** Story 1582060: GILGUI reading POEX default code
       	 ****/	
				this.poex.set("value",this.defaultCOMPoex);
				//plugin request service modifying poex
			}else {//Defect 1859271
				this.poex.set("value",this.defaultVENPoex);
			}
//			else if(item=="IBM"){
//				this.poex.set("value",this.defaultVENPoex);
//			}
			 /***	
           	 * End:	Story 1582060: GILGUI reading POEX default code
           	 ****/
			
		},
		searchByReferenceInvoice:function(){
			this.serviceParameters['request']=arguments.callee.nom;
			this.serviceParameters['jsonInvoiceEls']= JSON.stringify(jsonInvoiceEls);
			ecm.messages.progress_message_InvoiceELSService = "Searching Ref Invoice...";				 
			Request.postPluginService("GilCnPlugin","InvoiceELSService",'application/x-www-form-urlencoded',{
						
					requestBody : ioquery.objectToQuery(that.serviceParameters),
					requestCompleteCallback: lang.hitch(this,function(response) {
						debugger;
							
						jsonInvoiceEls=JSON.parse(response.invoiceElsJson);
					
						if(jsonInvoiceEls.displayRefInvSearch=="Y"){
							
							that.openRefInvPickerDialog(jsonInvoiceEls);
							

						}else if(jsonInvoiceEls.dialogErrorMsgs!=""){
							
							var dialogMsgs=new DialogMessages();
							for(var i in jsonInvoiceEls.dialogErrorMsgs ){
								dialogMsgs.addErrorMessages(jsonInvoiceEls.dialogErrorMsgs[i]);
							}
							
							that.showMessage("Error",dialogMsgs.errorMessages(),false,null,null);
							this.refInvNum.set('value',"");
						}else if(jsonInvoiceEls	.dialogWarningMsgs!=""){ //Test Defect 1852623
							var dialogMsgs=new DialogMessages();
							for(var i in jsonInvoiceEls.dialogWarningMsgs ){
								dialogMsgs.addWarningMessages(jsonInvoiceEls.dialogWarningMsgs[i]);
							}
							
							that.showMessage("Warning",dialogMsgs.warningMessages(),false,null,null,"onlyOk");
							this.refInvNum.set('value',"");

						}
								
					})	
				});
	
		},
		enableReferenceInvoiceBasedOnDBCR:function(itemSelected){
			that=this;
			debugger;
    		var disable=true;
    		if(itemSelected=="CR"){
    			disable=false;
    		}
			if(this.invoiceType.get("value")=="COM" || this.invoiceType.get("value")=="STP"){
				disable = false;//means it will enable, disabling false
			}
			this.refInvNum.set('disabled',disable);
			if(disable){
				this.refInvNum.set("value", "");
			}
		
		},
		enableReferenceInvoiceBasedOnInvType:function(itemSelected){
			
			debugger;
    		var disable=true;
    		if(this.dbCr.get("value")=="CR"){
    			disable=false;
    		}
//    		if(this.invoiceType.get('value')!= itemSelected && jsonInvoiceEls.DOCUMENTMODE==3)// IndexingDataModel.UPDATEMOD
    		if( jsonInvoiceEls.DOCUMENTMODE==3)
    		{
    			dialogMsgs=new DialogMessages();
				dialogMsgs.addWarningMessages("Changing invoice type.  Please validate line items");

    			that.showMessage("Warning",dialogMsgs.warningMessages(), false, null, null);
    		}
    			
    			
              
			if(itemSelected=="COM" || itemSelected=="STP"){
				disable = false;//means it will enable, disabling false
			}
			this.refInvNum.set('disabled',disable);
			if(disable){
				this.refInvNum.set("value", "");
			}
		
		},
		enableCustomerFieldsBasedOnOfferingNumber:function(){
			that=this;
			debugger;
			var disable;
			if(this.olNum.get('value')==""){
				disable=false;
			
			}else{
				disable=true;
			}
			this.coaBsp.set('disabled',disable);
			this.olCustNum.set('disabled',!disable);
			if(this.olCustNum.get('value')==""){
				disable=false;
				

			}else{
				disable=true;
			}
			this.olcustAdd.set('disabled',disable);
			this.olcustAdd2.set('disabled',disable);
			this.city.set('disabled',disable);
			this.postalC.set('disabled',disable);
			this.statea5.set('disabled',disable);
//			this.statea6.set('disabled',disable);
			
			/**
			 * Changes for Story 1750051 - [ELS Canada] CA GIL changes
			 */
			this.province.set('disabled',disable);
			/**
			 * End Changes for Story 1750051 - [ELS Canada] CA GIL changes
			 */
			
		},
	
		
		enableExchangeRateBasedonCurrency: function(itemSelected){
			that=this;
			debugger;
			
			
			if(this.olNum.get('value')==""){
				this.exchgRate.set('disabled',true);
				this.exchgRate.set('value',"1.0");
				return;
			}
//			var ="";
//			if( arguments.length==1){
//				 itemSelected=arguments[0];
//				
//			}else{
//				itemSelected=this.invCur.get("value");
//			}
//			this.invCur.set("options", jsonInvoiceEls.currencyCodeSelectList);
//    		this.invCur.set("value", jsonUtils.getValueIfOtherKeyExists(jsonInvoiceEls.currencyCodeSelectList,"value", true));

			if(itemSelected==jsonInvoiceEls.oLCurrency){//if its equal to he olDefault currency
				this.exchgRate.set('disabled',true);
				this.exchgRate.set('value',"1.0");
			}else{
				this.exchgRate.set('disabled',false);
			}
		},
		searchByInstalledCustomer:function(){
			that=this;
			debugger;
			option="searchByInstalledCustomer";
			//Request plugin servicehere *****
			
			//loading new values retrieved
//			that.loadBasicFields();	
//			that.loadOLFields();
			that.loadInstalledCustomer();
			
			that.enableCustomerFieldsBasedOnOfferingNumber();
		},
			searchByOffering: function(){
			that=this;
			debugger;
			if(jsonInvoiceEls.offeringLetterNum==""){
				that.cleanOfferingValues();
				that.removeQuoteLineItems();
				that.recalculateUnitAmounts();
				
			}else{
			that.msg=new DialogMessages();
			this.serviceParameters['request']=arguments.callee.nom;
			this.serviceParameters['jsonInvoiceEls']= JSON.stringify(jsonInvoiceEls);
			ecm.messages.progress_message_InvoiceELSService = "Searching OL";				 
			Request.postPluginService("GilCnPlugin","InvoiceELSService",'application/x-www-form-urlencoded',{
						
					requestBody : ioquery.objectToQuery(that.serviceParameters),
					requestCompleteCallback: lang.hitch(this,function(response) {
						debugger;
							
						jsonInvoiceEls=JSON.parse(response.invoiceElsJson);
				    	

						if(jsonInvoiceEls.dialogErrorMsgs.length>0){
							var msgs=jsonInvoiceEls.dialogErrorMsgs;
							for (var i in msgs) {
								that.msg.addErrorMessages(msgs[i]);
							}
						}
						if(that.msg.hasErrorMessages()){
							
								that.showMessage("Error",that.msg.errorMessages(),false, null, null);
								//clean values added
								that.cleanOfferingValues();
								
						}else{
							//load new values
							
//							this.invCur.get("options")
							if(that.exchgRate.get('value') !=jsonInvoiceEls.oLCurrency){
								that.exchgRate.set("disabled",false);
							}else{
								that.exchgRate.set("disabled",true);
								that.exchgRate.set("value","1.0");
							}
//							this.invCur.set("value",jsonInvoiceEls.oLCurrency);

							that.loadBasicFields();	
							//check if any basi field needsto refresh could be line items fields
							that.loadOLFields();
							that.invoiceForSWVinvoiceType(null);
							that.enableExchangeRateBasedonCurrency(that.invCur.get('value'));
					    	that.enableCustomerFieldsBasedOnOfferingNumber();
					    	that.validateTMFromQuote();
					    	that.openOLCS();
						}

							
					})	
				});
	
			}
		},
		removeQuoteLineItems:function(){
			
			for(var i= jsonInvoiceEls.lineItems.length-1; i>=0; i--){
		    	if( jsonInvoiceEls.lineItems[i].FROMQUOTE=="Y"){
		    		jsonInvoiceEls.lineItems.splice(i, 1);
		        }
		    }
			
		},
		recalculateUnitAmounts:function(){
			debugger;
			var vatVariance=0.00;
			var totalUnitNet=0.00;
			var totalUnitVat=0.00;
			var availableUnitNet=0.00;
			var availableUnitVat=0.00;
			var extendedUnitPrice=0.00;
			var extendedUnitVat=0.00;
			var totalAmt=0.00;
			var lineItemTotalAmt=0.00;
			var lineItemVatAmt=0.00;
			var isConfigured=false;
			var unitVatRounding=0;
			var lnItem=null;
			for(var i in jsonInvoiceEls.lineItems){
				lnItem=jsonInvoiceEls.lineItems[i];
				extendedUnitPrice=lnItem.EXTENDEDPRICE;
				extendedUnitVat=lnItem.TOTALVATAMOUNT;
				if(lnItem.CONFIGUSED=="Y" || lnItem.CONFIGUSED=="y"){
					isConfigured=true;
				}else{
					isConfigured=false;
				}
				
				if(extendedUnitPrice != null){
					totalUnitNet=MathJs.add(totalUnitNet,extendedUnitPrice);
					if(!isConfigured){
						availableUnitNet = MathJs.add(availableUnitNet,extendedUnitPrice);
					}
				}
				
				if(extendedUnitVat != null){
					totalUnitVat=MathJs.add(totalUnitVat,extendedUnitVat);
					if(!isConfigured){
						availableUnitVat = MathJs.add(availableUnitVat,extendedUnitVat);
					}
				}
				
			}
			
			//missing set and toFixed
			jsonInvoiceEls['lineItemNetAmount']=that.formatNumber(totalUnitNet,2);
			jsonInvoiceEls['lineItemVatAmount']=that.formatNumber(totalUnitVat,2);
			jsonInvoiceEls['lineItemTotalAmount']=that.formatNumber(MathJs.add(totalUnitVat,totalUnitNet),2);
			jsonInvoiceEls['netBalance']=that.formatNumber(availableUnitNet,2);
			jsonInvoiceEls['vatBalance']=that.formatNumber(availableUnitVat,2);
			that.lnItemTot.set('value',jsonInvoiceEls.lineItemTotalAmount);
			that.lnItemTxTot.set('value',jsonInvoiceEls.lineItemVatAmount);
			that.lnItemNetTot.set('value',jsonInvoiceEls.lineItemNetAmount);

		},
		 formatNumber: function(num, precis) {
		    	var res = MathJs.format(  MathJs.round(num, precis),  {notation: 'fixed', precision: precis});
		    	return res;
		    },
		////Defect 1836083
		cleanSupplierValues: function(){
			that.suppName.set('value',"");
			that.suppNumber.set('value',"");
			that.legalId.set('value',"");
			that.saDate.set('value',"");
			that.saNumber.set('value',"");
			that.suppAddress.set('value',"");
			that.suppAddress2.set('value',"");
			that.suppAddress3.set('value',"");
			
			var enable=false;
			this.suppName.set("disabled",enable);
			this.suppNumber.set("disabled",enable);
			this.legalId.set("disabled",enable);
			
		},
		cleanOfferingValues:function(){
			that.exchgRate.set("disabled",true);
			that.exchgRate.set("value","1.0");

			that.rof.set("checked",false);
			that.rofOl.set("checked",false);
			that.billEntIndc.set('value',"");
			
			
			that.olCur.set('value',"");
//			that.rofOl.set('value',"");
			that.coaBsp.set('value',"");
			that.olCustNum.set('value',"");
			that.custName.set('value',"");
			that.olcustAdd.set('value',"");
			that.olcustAdd2.set('value',"");
			that.postalC.set('value',"");
			that.city.set('value',"");
			that.statea5.set('value',"");
			that.province.set('value',"");
			
			that.rof.set("disabled",true);
			that.billEntIndc.set("disabled",true);			
			that.olCur.set("disabled",true);			
			that.rofOl.set("disabled",true);
			that.coaBsp.set("disabled",false);
			that.olCustNum.set("disabled",true);
			that.custName.set("disabled",true);
			that.olcustAdd.set("disabled",true);
			that.olcustAdd2.set("disabled",true);
			that.city.set("disabled",true);
			that.postalC.set("disabled",true);
			
			that.statea5.set("disabled",true);
			that.province.set("disabled",true);
		},
		cleanOLCustomerValues:function(){
				
			that.olCustNum.set('value',"");
			that.custName.set('value',"");
			that.olcustAdd.set('value',"");
			that.olcustAdd2.set('value',"");
			that.postalC.set('value',"");
			that.city.set('value',"");
			that.statea5.set('value',"");
			that.province.set('value',"");
			
		
			that.olCustNum.set("disabled",true);
			that.custName.set("disabled",true);
			that.olcustAdd.set("disabled",true);
			that.olcustAdd2.set("disabled",true);
			that.city.set("disabled",true);
			that.postalC.set("disabled",true);
			
			that.statea5.set("disabled",true);
			that.province.set("disabled",true);
		},
		recalculateInvAmt: function(){
			that=this;
			debugger;
			this.serviceParameters['request']=arguments.callee.nom;
			this.serviceParameters['jsonInvoiceEls']= JSON.stringify(jsonInvoiceEls);
			ecm.messages.progress_message_InvoiceELSService = "Recalculating";				 
			Request.postPluginService("GilCnPlugin","InvoiceELSService",'application/x-www-form-urlencoded',{
						
					requestBody : ioquery.objectToQuery(that.serviceParameters),
					requestCompleteCallback: lang.hitch(this,function(response) {
						debugger;
							
						jsonInvoiceEls=JSON.parse(response.invoiceElsJson);
						if(jsonInvoiceEls.totalAmount<0){
							this.invAmt.set("value","0.00");
						}else{
							this.invAmt.set("value",jsonInvoiceEls.totalAmount);
						}
						if(jsonInvoiceEls.vatAmount<0){
							this.taxAmt.set("value","0.00");
						}else{
							this.taxAmt.set("value",jsonInvoiceEls.vatAmount);
						}
					
						
						if(jsonInvoiceEls.netAmount<0){
							this.netAmt.set("value","0.00");
						}else{
							this.netAmt.set("value",jsonInvoiceEls.netAmount);
						}
						
						registry.byId(that.id+'_taxAmt').set('_onBlurActive', true);
							
					})	
				});

			
		},
		usTaxCalculation: function(callback){
			that=this;
//			option="usTaxCalculation";

			debugger;
			if(this.taxes.get('value')){
				jsonInvoiceEls['taxesIndc']="Y";
			}else{
				jsonInvoiceEls['taxesIndc']="N";
			}
			this.serviceParameters['request']=arguments.callee.nom;
			this.serviceParameters['jsonInvoiceEls']= JSON.stringify(jsonInvoiceEls);
			ecm.messages.progress_message_InvoiceELSService = "US Tax Calculating";			 
			Request.postPluginService("GilCnPlugin","InvoiceELSService",'application/x-www-form-urlencoded',{
						
					requestBody : ioquery.objectToQuery(that.serviceParameters),
					requestCompleteCallback: lang.hitch(this,function(response) {
						debugger;
							
						jsonInvoiceEls=JSON.parse(response.invoiceElsJson);
						
						if(jsonInvoiceEls.displayTaxWarn=="Y"){
							dialogMsgs=new DialogMessages();
							dialogMsgs.addConfirmMessages("If the invoice has two billed to entities, please select Taxes check box and enter the Tax amounts to IGF and Customer.");
							that.showMessage("Confirm",dialogMsgs.confirmMessages(),
									true,that.usTaxChoiceY,that.usTaxChoiceN,"usToBill");
							debugger;
							jsonInvoiceEls.displayTaxWarn="N";
							//result=display warning fornow result=false;
						}else{
							that.loadBasicFields();
						}
						registry.byId(that.id+'_taxAmt').set('_onBlurActive', true);
							
					})	
				});
			
		},
		usTaxChoiceY:function(){
		
			debugger;

			that.setCheckBoxTaxes(false);//disabling false= enable
			var tmp=parseFloat(that.invAmt.get('value'))||0
			if(tmp!=0){
				that.recalculateInvAmt();
			}
			that.dialogCounter=0;//clean counter
		},
		usTaxChoiceN:function(){
			debugger;
			
			//open us tax dialog US STATE Dialog
			that.dialogCounter=0;//clean counter
			that.setCheckBoxTaxes(true);//disabling true:disable
			that.openUSState();
		
			
			
		},
		offeringValidation:function(){
			if(jsonInvoiceEls.offeringLetterNum.trim().length==0 && jsonInvoiceEls.DOCUMENTMODE=="1"){
				debugger;
				dialogMsgs=new DialogMessages();
				dialogMsgs.addConfirmMessages("Offer Letter Number is blank.\nDo you wish to correct?");
				that.showMessage("Info",dialogMsgs.confirmMessages(),true,that.choiceYoL,that.validateDate);
				
			}else{
				that.validateDate();
			}
		},
		validateBeforeSave:function (){
			debugger;
			that.loadAllInJsonInvoiceEls();
			
			dialogMessage=new DialogMessages();
			
			var supNum = jsonInvoiceEls.vendorNum.trim();
//			debugger;
			if(jsonInvoiceEls.DOCUMENTMODE == "1" &&
					supNum.length>0){//ADDMODE
				
					var flag=false;
					var supNums=jsonInvoiceEls.supplierNums;
					for (var i in supNums) {
			    	    if (supNums[i] == supNum){
			    	    	flag=true;
			    	    	break;
			    	    }
			    	}
					if(flag){
						dialogMsgs=new DialogMessages();
						dialogMsgs.addConfirmMessages("Invoice number/Supplier number already exists, Do you wish to change the invoice number ?");
						that.showMessage("Confirm",dialogMsgs.confirmMessages(),
		    	    			true,that.choiceYSupNum,that.offeringValidation);
		    	    	
					}else{
						that.offeringValidation();
					}
					
				
			}else{
				that.offeringValidation();
			}
		
			
			
			
		},
		choiceYSupNum:function(){
			
			that.requestFocus(that.id+"_invNum");
			return;
			
		},
		choiceNSupNum:function(){
			
		},
		requestFocus:function(toSelect){
			
			dojo.byId(toSelect).focus();

		},
		save:function(){

		
				//Save request
				
				console.log("Successfully Validation");	
				that.serviceParameters['request']=arguments.callee.nom;
				that.serviceParameters['jsonInvoiceEls']= JSON.stringify(jsonInvoiceEls);
				ecm.messages.progress_message_InvoiceELSService = "Saving...";				 
				Request.postPluginService("GilCnPlugin","InvoiceELSService",'application/x-www-form-urlencoded',{
							
					requestBody : ioquery.objectToQuery(that.serviceParameters),
						requestCompleteCallback: lang.hitch(that,function(response) {
							debugger;
								
							jsonInvoiceEls=JSON.parse(response.invoiceElsJson);
						
							if(jsonInvoiceEls.dialogErrorMsgs.length>0){
								var msgs=jsonInvoiceEls.dialogErrorMsgs;
								for (var i in msgs) {
									thisValidate.msg.addErrorMessages(msgs[i]);
								}
							}
							if(thisValidate.msg.hasErrorMessages()){
								
									that.showMessage("Error",thisValidate.msg.errorMessages(),false, null, null);
									
							}else{
								console.log("Successfully saved!");
								that.counterCancel=1;
								
								that.destroyRecursive();
							}	
									
						})	
					});

				
				
			
		},
		index: function() {
			that=this;
			
			this.setTitle("Index Invoice[" + this.serviceParameters['className'] + "]");

	   		debugger;
	   		that.msg=new DialogMessages();
			this.serviceParameters['request']=arguments.callee.nom;
			var bAttributes=this.serviceParameters["businessAttributes"];
			var attr=dojo.fromJson(bAttributes);
			ecm.messages.progress_message_InvoiceELSService = "Loading Index Invoice: " + attr.invNum + "...";
			 
				
			Request.postPluginService("GilCnPlugin","InvoiceELSService",'application/x-www-form-urlencoded',{
				
				requestBody : ioquery.objectToQuery(that.serviceParameters),
				requestCompleteCallback: lang.hitch(this,function(response) {
					debugger;
					
					jsonInvoiceEls=JSON.parse(response.invoiceElsJson);
				
					
						that.loadBasicFields();	
						that.loadCombos();
						that.loadOLFields();
						that.loadInstalledCustomer();
						
									
	
						//*****Supplier tab***
						this.suppName.set("value",jsonInvoiceEls.vendorName);
						this.suppNumber.set("value",jsonInvoiceEls.vendorNum);
						this.legalId.set("value",jsonInvoiceEls.vatRegistrationNumber);
						this.saNumber.set("value",jsonInvoiceEls.srNumber);
		                							
						this.saDate.set("value", this.parseDateforSupplier(jsonInvoiceEls.srEndDate,false));//----change
						if(this.saDate.get('value')=="NaN/NaN/0NaN" ||this.saDate.get('value')=="NAN/NAN/0NAN"  )this.saDate.set('value',"");
						this.suppAddress.set("value",jsonInvoiceEls.vendorAdd1);
						this.suppAddress2.set("value",jsonInvoiceEls.vendorAdd2);
						this.suppAddress3.set("value",jsonInvoiceEls.vendorAdd3);
						
						registry.byId(that.id+'_supplierName').set('_onChangeActive', true); 
			    		registry.byId(that.id+'_supplierNumber').set('_onChangeActive', true);
			    		registry.byId(that.id+'_legalId').set('_onChangeActive', true);
			    		
			    		
			    		//*******Customer tab***
			    		
		//	    		registry.byId(that.id+'_instCustNum').set('_onChangeActive', true);
				 		registry.byId(that.id+'_custNameSearch').set('_onChangeActive', true);
				 		registry.byId(that.id+'_cityS').set('_onChangeActive', true);
						
						
						
						var jsonUtils = new JsonUtils();
						if(jsonInvoiceEls.isDistributorListExisting== true) {
							if (jsonInvoiceEls.distributorCodeSelectList!=null && jsonInvoiceEls.distributorCodeSelectList.length>0)	{
					        	
								var _label = null;
								 var _widget = null;
					        	
								_label =	construct.toDom("  <label  id='${id}_distrNumLabel' for='distrNum'>Distributor Number</label>&nbsp;");
								
						        _widget	 = new ecm.widget.Select({
						        	
						        	id:that.id+"_distrNum", 
						        	name:"distrNum",
			
						          });
						        
						          if (dojo.byId(that.id+"_replacebleField") != null){
					          	        construct.place(_label, that.id+"_replacebleLabel","replace");
					          	        construct.place(_widget.domNode, that.id+"_replacebleField","replace");
			
						          } else {
					          	        construct.place(_label, that.id+"_replacebleLabel2","replace");
					          	        construct.place(_widget.domNode, that.id+"_replacebleField2","replace");
						          }
						        
					    		registry.byId(that.id+"_distrNum").set("options", jsonInvoiceEls.distributorCodeSelectList );
					    		registry.byId(that.id+"_distrNum").set("value", jsonUtils.getDefaultOrSelected(jsonInvoiceEls.distributorCodeSelectList,"value", true) );
					    		registry.byId(that.id+"_distrNum").startup();
					    		
					    		if(jsonInvoiceEls.distributorNum!=null && jsonInvoiceEls.distributorNum!=""){
					    			registry.byId(that.id+"_distrNum").set("value",jsonInvoiceEls.distributorNum);
					    		}
						        
					        }
						}
		
						that.show();
						that.setOnChanges();
		
						var cA=this.contentArea;
			    		that.contentAreaH = domStyle.get(cA, "height");
			    		domStyle.set(cA, "overflow",'auto');
						that.checkFlagsRetrieved();
						
						if(jsonInvoiceEls.displayRefInvSearch=="Y"){
								
								that.openRefInvPickerDialog(jsonInvoiceEls);
								
			
						}
						
						
						if(jsonInvoiceEls.displayErrorDuplicateInvNum=="Y"){
							that.msg.clearMessages();
							that.msg.addErrorMessages("Duplicate Invoice Number / Invoice Date entered");
							that.openErrorDialog(that.msg.errorMessages());
							
						}else if(jsonInvoiceEls.dialogErrorMsgs.length>0){
								var msgs=jsonInvoiceEls.dialogErrorMsgs;
								for (var i in msgs) {
									that.msg.addErrorMessages(msgs[i]);
								}
								if(that.msg.hasErrorMessages()){
									
									that.showMessage("Error",that.msg.errorMessages(),false, null, null);
								}
						}
						if(jsonInvoiceEls.dialogWarningMsgs.length>0){
							var msgs=jsonInvoiceEls.dialogWarningMsgs;
							for (var i in msgs) {
								that.msg.addWarningMessages(msgs[i]);
							}
							if(that.msg.hasWarningMessages()){
								
								that.showMessage("Warning",that.msg.warningMessages(),false, null, null);
							}
						}
						if(jsonInvoiceEls.DOCUMENTMODE!=this.ADDMODE){
							that.serialFormClean=that.serializeForm(that.id+"_invoiceElsForm");
						}
						 
						that.enableListeners();
						
										
				})
				
				
			});
			
			
			

			
		},
		setOnChanges:function(){

    		that.onChangeInvoiceType();
    		that.onChangeOLCS();
		},
		disableListeners:function(){
			
			dijit.registry.byId(this.id+'_supplierName').set('_onChangeActive', false);
    		dijit.registry.byId(this.id+'_supplierNumber').set('_onChangeActive', false);
    		dijit.registry.byId(this.id+'_legalId').set('_onChangeActive', false);
    		dijit.registry.byId(this.id+'_invoiceType').set('_onChangeActive', false);
    		dijit.registry.byId(this.id+'_olCustNum').set('_onChangeActive', false);
    		
//    		// with this nest line won't display customer search dialog just right after opening invoicedialog
    		 registry.byId(this.id+'_instCustNum').set('_onChangeActive', false	);
	 		//registry.byId(this.id+'_custNameSearch').set('_onChangeActive', true);
	 		registry.byId(this.id+'_cityS').set('_onChangeActive', true);
	 		
		},
		enableListeners:function(){
			//**enabling the listener event for invoice type dropdown
			dijit.registry.byId(this.id+'_invoiceType').set('_onChangeActive', true);
			dijit.registry.byId(this.id+'_olCustNum').set('_onChangeActive', true);

		},
		changeY:function(){
			jsonInvoiceEls['DIRTYFLAG']=true;
//        	jsonInvoiceEls[invoiceNumber]=newInvoiceNumber;
//        	jsonInvoiceEls[invoiceDate]=newInvoiceDate;
        	that.invNum.set('value', jsonInvoiceEls.invoiceNumber);
//        	var utilGilDialog=new GilDialog();
        	that.invDate.set('value', that.parseDate(jsonInvoiceEls.invoiceDate,false));
		},
		changeN:function(){
			//nothing to do
			var bAttributes=that.serviceParameters["businessAttributes"];
			var attr=dojo.fromJson(bAttributes);
			
			jsonInvoiceEls['invoiceNumber']=jsonInvoiceEls.oldInvoiceNumber;
			jsonInvoiceEls['invoiceDate']=jsonInvoiceEls.oldInvoiceDate;
			that.invNum.set('value', jsonInvoiceEls.oldInvoiceNumber);
//			that.utilGilDialog=new GilDialog();
        	that.invDate.set('value', that.parseDate(jsonInvoiceEls.oldInvoiceDate,false));
		},
		checkFlagsRetrieved:function(){
//			that=this;
			if(jsonInvoiceEls.displayChangeInvNum=="Y"){
//				// prompt this warning Changing Invoice Number/Date if yes 
				dialogMsgs=new DialogMessages();
				dialogMsgs.addConfirmMessages("Changing Invoice Number/Date. Are you sure?");
				that.showMessage("Confirm",dialogMsgs.confirmMessages(), true, that.changeY,that.changeN);
			}
			
			
			
			that.setEditableFields(jsonInvoiceEls.invDialfieldsEditable);


			
		},
		hideFieldsForCountry:function(){
			
			
			/**
			 * Changes for Story 1750051 - [ELS Canada] CA GIL changes
			 */
			if(this.serviceParameters['country']!='US'){
				
				domStyle.set(dojo.byId(this.id+"_lstate"), 'display', 'none');
				domStyle.set(registry.byId(this.id+"_statea5").domNode, 'display', 'none');	
				domStyle.set(dojo.byId(this.id+"_divState6"), 'display', 'none');
				domStyle.set(dojo.byId(this.id+"_usContent"), 'display', 'none');
				domStyle.set(dojo.byId(this.id+"_ltaxes"), 'display', 'none');
				domStyle.set(registry.byId(this.id+"_taxes").domNode, 'display', 'none');
			}
			
			if(this.serviceParameters['country']!='CA'){
				
				domStyle.set(dojo.byId(this.id+"_caContent"), 'display', 'none');
				domStyle.set(dojo.byId(this.id+"_divProvince"), 'display', 'none');
			}
			
			/**
			 * End Changes for Story 1750051 - [ELS Canada] CA GIL changes
			 */


		},enableTaxesforIGFandCustomer:function(){

			debugger;

			this.taxBilledIgf.set('value',"0.00");
			this.taxBilledCus.set('value',"0.00");
			
		    	if(this.taxes.get('value')=="true"){
		    		
		    		domStyle.set(registry.byId(this.id+"_cts_dijit_form_Button_0").domNode,'display', "");
		    		
//		    		dijit.byId(this.id+"_cts_dijit_form_Button_0").set("disabled",true);
		     		
		    	}else{

//		    		domStyle.set(dojo.byId(this.id+"_cts_dijit_form_Button_0"), 'display', 'none');//GilCnPluginDojo_InvoiceELSDialog_0_cts_dijit_form_Button_dijit_form_Button_0
//		    		domStyle.set(registry.byId(nodeId).domNode, 'display', 'none');
		    		domStyle.set(registry.byId(this.id+"_cts_dijit_form_Button_0").domNode, 'display', 'none');
		    	}

		},setCheckBoxTaxes:function(enable){

			
			this.taxes.set("disabled",enable);

	    	this.taxBilledIgf.set("disabled",enable);
	    	this.taxBilledCus.set("disabled",enable);
	    	this.taxBilledIgf.set("value","0.00");
	    	this.taxBilledCus.set("value","0.00");

		},
		initializeFields: function(){
			//Fixing font & width of poex combo to make it fit 
			domStyle.set(registry.byId(this.id+"_poex").domNode, 'font-size', '12px');
			domStyle.set(registry.byId(this.id+"_poex").domNode, 'width', '70%');
			
			this.invAmt.set('value',"0.00");
			this.netAmt.set('value',"0.00");
			this.taxAmt.set('value',"0.00");
			this.lnItemTot.set('value',"0.00");
			this.lnItemTxTot.set('value',"0.00");
			this.lnItemNetTot.set('value',"0.00");
			this.exchgRate.set('value',"1.0");
			this.taxBilledCus.set('value',"0.00");
			this.taxBilledIgf.set('value',"0.00");
			
			this.netAmt.set("disabled",true);
			this.lnItemTot.set("disabled",true);
			this.lnItemTxTot.set("disabled",true);
			this.lnItemNetTot.set("disabled",true);			
			this.refInvNum.set("disabled",true);
			this.taxes.set("disabled",true);			
			this.exchgRate.set("disabled",true);
			this.taxBilledCus.set("disabled",true);
			this.taxBilledIgf.set("disabled",true);
			
						
			this.rof.set("disabled",true);
			this.billEntIndc.set("disabled",true);			
			this.olCur.set("disabled",true);			
			this.rofOl.set("disabled",true);
			
			this.olCustNum.set("disabled",true);
			this.custName.set("disabled",true);
			this.olcustAdd.set("disabled",true);
			this.olcustAdd2.set("disabled",true);
			this.city.set("disabled",true);
			this.postalC.set("disabled",true);
			
			this.statea5.set("disabled",true);
			this.statea6.set("disabled",true);
			this.province.set("disabled",true);
			
			this.saNumber.set("disabled",true);
			this.saDate.set("disabled",true);
			this.suppAddress.set("disabled",true);
			this.suppAddress2.set("disabled",true);
			this.suppAddress3.set("disabled",true);
			
			
			
			
			
		},
		setEditableFields:function(enable){
			
			this.invNum.set("disabled",enable);
			
			this.invDate.set("disabled",enable);
			this.costCenter.set("disabled",enable);
			this.purchOrdNum.set("disabled",enable);
			this.suppName.set("disabled",enable);
			this.suppNumber.set("disabled",enable);
			this.legalId.set("disabled",enable);
			this.olNum.set("disabled",enable);
			this.coaBsp.set("disabled",enable);//GIL code was setting to disable hardcode
			
			this.instCustNum.set("disabled",enable);
			this.custNameSearch.set("disabled",enable);
			this.custAdd.set("disabled",enable);
			this.custAdd2.set("disabled",enable);
			this.cityS.set("disabled",enable);
			this.postalCS.set("disabled",enable);
			this.statea6.set("disabled",enable);
					
			this.coaInv.set("disabled",enable);
			this.exceptions.set("disabled",enable);
			
			this.rof.set("disabled",true);
			
			/**
			 * Changes for Story 1750051 - [ELS Canada] CA GIL changes
			 */
			this.province.set("disabled",true);
			/**
			 * End Changes for Story 1750051 - [ELS Canada] CA GIL changes
			 */
			
			
			if(jsonInvoiceEls.invoiceStatus =="INEW" || jsonInvoiceEls.invoiceStatus =="IRESRCH" || jsonInvoiceEls.invoiceStatus ==""){
				this.compCode.set("disabled",false);
				this.poex.set("disabled",false);
			}else{
				this.compCode.set("disabled",true);
				this.poex.set("disabled",true);
			}
            

			
		},
		
     setEditableFieldsforCustomer: function(enable){
			
			this.custAdd.set("disabled",enable);
			this.custAdd2.set("disabled",enable);
			this.cityS.set("disabled",enable);
			this.postalCS.set("disabled",enable);
			this.statea6.set("disabled",enable);
			this.province.set("disabled",enable);
			
			
		},
	    formatNumber: function(num, precis) {

	    	var res = MathJs.format(  MathJs.round(num, precis),  {notation: 'fixed', precision: precis});
	    	return res;
	    	
	    },
	    
      
		loadBasicFields:function(){
			
			
			this.invDate.set("value",that.parseDate(jsonInvoiceEls.invoiceDate,false));
			this.invNum.set("value",jsonInvoiceEls.invoiceNumber);			
			this.invAmt.set("value",jsonInvoiceEls.totalAmount);
			this.taxAmt.set("value",jsonInvoiceEls.vatAmount);
			this.netAmt.set("value",jsonInvoiceEls.netAmount);
	
			this.taxes.set("checked",this.getBoolean(jsonInvoiceEls.taxesIndc));
			if(jsonInvoiceEls.lineItemTotalAmount==""){
				this.lnItemTot.set("value","0.00");
			}else{
				this.lnItemTot.set("value",jsonInvoiceEls.lineItemTotalAmount);
			}
			if(jsonInvoiceEls.lineItemVatAmount==""){
				this.lnItemTxTot.set("value","0.00");
			}else{
				this.lnItemTxTot.set("value",jsonInvoiceEls.lineItemVatAmount);
			}
			if(jsonInvoiceEls.lineItemNetAmount==""){
				this.lnItemNetTot.set("value","0.00");
			}else{
				this.lnItemNetTot.set("value",jsonInvoiceEls.lineItemNetAmount);
			}
			
			
			
			this.refInvNum.set("value",jsonInvoiceEls.referenceInvNum);
			this.exchgRate.set("value",jsonInvoiceEls.exchangeRate);
			this.taxBilledIgf.set("value",jsonInvoiceEls.taxesBilledToIgf);
//			  "taxesBilledToIgf": "0.00",
//			  "taxesBilledToCust": "0.00",
			this.costCenter.set("value",jsonInvoiceEls.costCenter);
			this.purchOrdNum.set("value",jsonInvoiceEls.purchaseOrderNum);
			
			
//set('checked', true);
    		this.coaInv.set("checked",this.getBoolean(jsonInvoiceEls.coaWithInvIndc));
    		
    		this.exceptions.set("checked",this.getBoolean(jsonInvoiceEls.exceptionIndc));
    		this.urgent.set("checked",this.getBoolean(jsonInvoiceEls.urgentIndc));
    		
			/**
			 * Changes for Story 1750051 - [ELS Canada] CA GIL changes
			 */
    		this.billedToCustomer.set("checked",this.getBoolean(jsonInvoiceEls.billedToCustomer));
			/**
			 * End Changes for Story 1750051 - [ELS Canada] CA GIL changes
			 */
		

		},
		loadCombos:function(){
			that=this;
			var jsonUtils = new JsonUtils();
			this.dbCr.set("options", jsonInvoiceEls.dbCrSelectList);
			
			if(jsonInvoiceEls.dbCr  !="" && jsonInvoiceEls.dbCr!=null ){
    			this.dbCr.set("value", jsonInvoiceEls.dbCr);
    		}else{
    			this.dbCr.set("value", jsonUtils.getValueIfOtherKeyExists(jsonInvoiceEls.dbCrSelectList,"value", true));
    		}
			
    		
	    		
    		this.invCur.set("options", jsonInvoiceEls.currencyCodeSelectList);
    		if(jsonInvoiceEls.currency  !="" && jsonInvoiceEls.currency!=null ){
    			this.invCur.set("value", jsonInvoiceEls.currency);
    		}else{
    			this.invCur.set("value", jsonUtils.getValueIfOtherKeyExists(jsonInvoiceEls.currencyCodeSelectList,"value", true));
    		}
    		
    		that.defaultCountryCurrency=this.invCur.get('value');
    		
    		this.poex.set("options", jsonInvoiceEls.poexCodesSelectList);
    		
    		this.defaultVENPoex=jsonInvoiceEls.defaultVENPoex;
    		this.defaultCOMPoex=jsonInvoiceEls.defaultCOMPoex;
    		console.log("Default VEN: "+this.defaultVENPoex);
    		console.log("Default COM: "+this.defaultCOMPoex);
    		if(jsonInvoiceEls.poexCode !=""&& jsonInvoiceEls.poexCode!=null ){
    			this.poex.set("value", jsonInvoiceEls.poexCode);
			}else{
	    		this.poex.set("value", jsonUtils.getValueIfOtherKeyExists(jsonInvoiceEls.poexCodesSelectList,"value", true));
			}
    		    		
    		this.invoiceType.set("options", jsonInvoiceEls.invoiceTypesSelectList);
    		if(jsonInvoiceEls.invoiceType !=""&& jsonInvoiceEls.invoiceType!=null ){
    			this.invoiceType.set("value", jsonInvoiceEls.invoiceType);
			}else{
				this.invoiceType.set("value", jsonUtils.getValueIfOtherKeyExists(jsonInvoiceEls.invoiceTypesSelectList,"value", true));
			}
    		
    		
    		this.compCode.set("options", jsonInvoiceEls.companyCodeSelectList);
    		
    		if(jsonInvoiceEls.companyCode !=""&& jsonInvoiceEls.companyCode!=null ){
    			this.compCode.set("value", jsonInvoiceEls.companyCode);
			}else{
				this.compCode.set("value", jsonUtils.getValueIfOtherKeyExists(jsonInvoiceEls.companyCodeSelectList,"value", true));
			}
    		
			/**
			 * Changes for Story 1750051 - [ELS Canada] CA GIL changes
			 */
	    	this.shipToProvinceCode.set("options", jsonInvoiceEls.provinceCodesSelectList);
	    	this.shipToProvinceCode.set("value", jsonUtils.getDefaultOrSelected(jsonInvoiceEls.provinceCodesSelectList,"value", true));
			/**
			 * End Changes for Story 1750051 - [ELS Canada] CA GIL changes
			 */
    		

		},
		loadOLFields:function(){
			var jsonUtils = new JsonUtils();
			this.olNum.set("value",jsonInvoiceEls.offeringLetterNum);
			
			this.rof.set("checked",this.getBoolean(jsonInvoiceEls.rofInvoiceIndc));
			this.billEntIndc.set("value",jsonInvoiceEls.billEntityIndc);		
			this.rofOl.set("checked",this.getBoolean(jsonInvoiceEls.rofOLIndc));
			
			this.olCur.set("value",jsonInvoiceEls.oLCurrency);
			this.coaBsp.set("value",jsonInvoiceEls.responsiblePartyId);
			this.olCustNum.set("value",jsonInvoiceEls.oLCustomerNum);
			this.custName.set("value",jsonInvoiceEls.oLCustomerName);
			this.olcustAdd.set("value",jsonInvoiceEls.customerAdd1);
			this.olcustAdd2.set("value",jsonInvoiceEls.customerAdd2);
			this.city.set("value",jsonInvoiceEls.customerAdd3);
			this.postalC.set("value",jsonInvoiceEls.customerAdd4);
			debugger;
			this.statea5.set('value',jsonInvoiceEls.customerAdd5);
			
			/**
			 * Changes for Story 1750051 - [ELS Canada] CA GIL changes
			 */
    		this.province.set("value", jsonInvoiceEls.customerAdd5);
			/**
			 * End Changes for Story 1750051 - [ELS Canada] CA GIL changes
			 */
			
		},
		loadInstalledCustomer:function(){
			
			this.instCustNum.set("value",jsonInvoiceEls.installedCustomerNum);
			this.custNameSearch.set("value",jsonInvoiceEls.installedCustomerName);
			this.custAdd.set("value",jsonInvoiceEls.installedCustomerAdd1);
			this.custAdd2.set("value",jsonInvoiceEls.installedCustomerAdd2);
			this.cityS.set("value",jsonInvoiceEls.installedCustomerAdd3);
			this.postalCS.set("value",jsonInvoiceEls.installedCustomerAdd4);
			this.statea6.set('value',jsonInvoiceEls.installedCustomerAdd5);
		},
		
		initializeDateFields: function() {

			that.setDatePattern(registry.byId(that.id+"_invDate"));

		},
		upperCaseFields: function() {
			var inputsToUpperCase = dojo.query('input#[id^="'+ this.id +'_"]');
			for(var i=0; i<inputsToUpperCase.length; i++){
				that.toUpperCase(inputsToUpperCase[i]);
			}
		},
		

	    getBoolean: function(indc){
	    	that=this;
	    	if( indc=="Y" || indc =="y")
	    		return true;
	    	else
	    		return false;
	    },
		openLineItemsDialog:function(evt){
			that=this;
			debugger;
			var lineItemValue="";
			that.loadAllInJsonInvoiceEls();
			that.serviceParameters['jsonInvoiceEls']= JSON.stringify(jsonInvoiceEls);
			var lineItemsDialog=new LineItemsDialog({callerDialog:that,serviceParameters: that.serviceParameters });
//			lineItemsDialog.retrieveValues();
//			lineItemsDialog.show();
			
		},
		
		openCommentsDialog: function() {
			debugger;
			console.debug("inside openCommentsDialog");
            var dialogSupplier = null;
			var thisForm = this;
			dialogComments= new CommentsDialog({callerForm:thisForm, serviceParameters: this.serviceParameters});


		},
		close : function() {
			that.counterCancel=that.counterCancel+1;
			if(jsonInvoiceEls.DIRTYFLAG == true && that.counterCancel==1 || that.isFormDirty(that.id+"_invoiceElsForm")){//Test Defect 1852597:
				dialogMsgs=new DialogMessages();
				dialogMsgs.addConfirmMessages("You have unsaved changes. Are you sure you want to quit?");
				that.showMessage("Confirm",dialogMsgs.confirmMessages(), true, that.choiceYCancel,that.choiceNCancel);

			}else if(that.counterCancel==1){
				try { 
					that.rollbackIndexing();
//					that.destroyRecursive();
					
				
				}catch(e){
					console.log(e);
				}
			}else{
				that.destroyRecursive();
			}
			

		},
		choiceYCancel:function(){
	
			try { 
				that.rollbackIndexing();
//				that.destroyRecursive();
				
			
			}catch(e){
				console.log(e);
			}
		},
		choiceNCancel:function(){
			//nothing to do
			that.counterCancel=0;
		},
		rollbackIndexing: function(){
			
			that.serviceParameters['request'] = arguments.callee.nom;
			that.serviceParameters['jsonInvoiceEls']= JSON.stringify(jsonInvoiceEls);
			ecm.messages.progress_message_InvoiceELSService = "Closing...";
			Request.postPluginService("GilCnPlugin", "InvoiceELSService",'application/x-www-form-urlencoded', {
	    		requestBody : ioquery.objectToQuery(that.serviceParameters),
			    requestCompleteCallback : lang.hitch(that, function (response) {
			    	
	    			console.log('Index rolled back successfully.');
//	    			if(needToDestroy!=null && needToDestroy==true){
	    				that.destroyRecursive();
//	    			}
	    			
	    			return true;
		    		
	    		})
			});
		},

	
		openErrorDialog: function(error) {
			that=this;

			var errorDialog  = new ErrorMessage({ callerDialog:that,errorMsg: error, destroyCallerDialog:true});

		},
		openRefInvPickerDialog: function(jsonObject) {
			that=this;

			var dialogRefPicker = null;			
			
//			var buttonClicked =  registry.getEnclosingWidget(evt.target);
			var e =jsonInvoiceEls.country;
			console.log("inside reference invoice dialog ");
			var jsonObject=jsonInvoiceEls;
			var those=that;
			dialogRefPicker = new ReferenceInvoicePickerDialog({ callerDialog:that, jsonObject:jsonObject});
			
			
			
		},
		openUSState:function(){
			var usStateDialog = null;			
			that.loadAllInJsonInvoiceEls();
			
//			var jsonObject=jsonInvoiceEls;
	
			usStateDialog = new USStateDialog2({callerDialog:that,jsonObject:jsonInvoiceEls, serviceParameters: this.serviceParameters});//, serviceParameters: this.serviceParameters  });
			
			
		},
		
		openVendorSearchELSDialogSupplierName: function() {
			debugger;
			//Defect 1836083
			console.log("openVendorSearchELSDialogSupplierName");
			var dialogSupplier = null;
			
			var thisForm = this;		
			if (thisForm.suppName.get("value")==""){
				thisForm.cleanSupplierValues();
			}else{
				dialogSupplier = new SupplierSearchELSDialog({	searchType:'supplierName', callerForm:thisForm, jsonObject:jsonInvoiceEls, serviceParameters: this.serviceParameters  });
				
				dialogSupplier.showSupplierSearchDataGrid();
			}
			
		},

		openVendorSearchELSDialogSupplierNumber : function() {
			console.log("openVendorSearchELSDialogSupplierNumber");
			debugger;
			//Defect 1836083
				var dialogSupplier = null;
				var thisForm = this;
				if (thisForm.suppNumber.get("value")==""){
					thisForm.cleanSupplierValues();
				}else{
					dialogSupplier = new SupplierSearchELSDialog({
						searchType : 'supplierNumber',
						callerForm : thisForm,
						jsonObject:jsonInvoiceEls,
						serviceParameters : this.serviceParameters
					});
					dialogSupplier.showSupplierSearchDataGrid();
				}
				

		},

		openVendorSearchELSDialogLegalId : function() {
			console.log("openVendorSearchELSDialogLegalId");
			debugger;
			//Defect 1836083
			var dialogSupplier = null;
			var thisForm = this;
			if (thisForm.legalId.get("value")==""){
							thisForm.cleanSupplierValues();
			}else{
				dialogSupplier = new SupplierSearchELSDialog({
					searchType : 'legalId',
					callerForm : thisForm,
					jsonObject:jsonInvoiceEls,
					serviceParameters : this.serviceParameters
				});
				dialogSupplier.showSupplierSearchDataGrid();
			}
							

		},
		

		
		resetCustomerFieldValues: function()
		{
			
			debugger;
			
			console.log("inside resetCustomerFieldValues");
						
			this.custNameSearch.set("value","");
			this.custAdd.set("value","");
			this.custAdd2.set("value","");
			this.cityS.set("value","");
			this.postalCS.set("value","");
			this.statea6.set("value","");
			return;
			
		},
		
		openOLCS :function(){
			console.log("openOLCS");
		
           var customerSearch = null;
           if(jsonInvoiceEls.customerNum.trim().length==0 || that.olCustNum.get('value')=="" ){
        	   that.clearCustomerValues();
           }
           if(that.olCustNum.get('value')==""){
        	   var dialogMsgs=new DialogMessages();
				
				dialogMsgs.addErrorMessages("Customer not found.");
				that.showMessage("Error",dialogMsgs.errorMessages(), false, null,null);
				that.cleanOLCustomerValues();
           }else{
	           customerSearch = new CustomerSearchELSDialog({	searchType:'OLCustomerNum', callerForm:that, serviceParameters: this.serviceParameters  });
			   customerSearch.showCustomerSearchDataGrid();
           }
		},
		clearCustomerValues:function(){
			jsonInvoiceEls.customerNum="";
			jsonInvoiceEls.customerName="";
			jsonInvoiceEls.customerAdd1="";
			jsonInvoiceEls.customerAdd2="";
			jsonInvoiceEls.customerAdd3="";
			jsonInvoiceEls.customerAdd4="";
			jsonInvoiceEls.customerAdd5="";
		},
		openCS :function()
		{
			
           var dialogSupplier = null;
			
			var thisForm = this;	
			
					
			this.setEditableFieldsforCustomer(false);
			
			
			if(this.instCustNum.get('value')== '')
			{
				thisForm.resetCustomerFieldValues();
				console.log("after resetting the values inside if ");
			
			  
			}
			console.log("after resetting the values");
			
			if(!(this.instCustNum.get('value')== ''))
				{
			dialogSupplier = new CustomerSearchELSDialog({	searchType:'InstCustomerNum', callerForm:thisForm, serviceParameters: this.serviceParameters  });
			
			dialogSupplier.showCustomerSearchDataGrid();
			
				}
			
			
		},
		
		openCSN :function()
		{
			
           var dialogSupplier = null;
			
			var thisForm = this;	
									
			this.setEditableFieldsforCustomer(false);
			
			if(this.custNameSearch.get('value')== '')
			{
				thisForm.resetCustomerFieldValues();			
			  
			}
			
			if(!(this.cityS.get('value')== ''))
			{
			
			dialogSupplier = new CustomerSearchELSDialog({	searchType:'CustName', callerForm:thisForm, serviceParameters: this.serviceParameters  });
			
			dialogSupplier.showCustomerSearchDataGrid();			
			
			}
			
		},
		
		  
		 parseDateforSupplier: function(dateAux, isISO8601){
		    	
		    	if(isISO8601){
		    		
		    		return stamp.fromISOString(dateAux.toString()).toGMTString();
		    		
		    	} else {
		    	
		    		debugger;
		    			    				    		
		    		var date= new Date(dateAux);
		    		
			    		var parsedDate = locale.format(date, {
			    			datePattern : "MM/dd/yyyy",
			    			selector: "date"
					});
				    
			    		return parsedDate;
		    	}
		    },	
			
		
		
		
		
		showMessage: function(titl, msg, needConfirmation, callbackYes, callbackNo,idName) {	
			if(idName=="usToBill"){
				if(that.dialogCounter>0){
					return;
				}else{
					that.dialogCounter=that.dialogCounter+1;
					idName=idName+that.dialogCounter;
				}
				
			}
			
				
			thisForm = this;
            var d1 = new NonModalConfirmDialog({
//            	id:idName,
            	
                title: titl, 
                style: "min-width:350px;",
                content:msg,
                name:"dialogMessage"
                	
            })
            
            if(needConfirmation){
            	
		      
		        var actionBar = dojo.create("div", {"class": "dijitDialogPaneActionBar"   }, d1.containerNode);
	
		        var buttonYes=new dijit.form.Button({ 
		        	"label": "Yes",
		        	onClick: function(){
		            	
		            	callbackYes();
		            	d1.destroyRecursive();  
		            	
		            }});
		        buttonYes.placeAt(actionBar);
		        domclass.add(buttonYes.focusNode, "yesNoOkButton");
		        var buttonNo =new dijit.form.Button({
		        	"label": "No",
		        	
		            onClick: function(){
		            	
		            	callbackNo();
		            	d1.destroyRecursive(); 
		            	
		            	}});
		        buttonNo.placeAt(actionBar);
		        domclass.add(buttonNo.focusNode, "yesNoOkButton");
            } else if(idName=="onlyOk"){
            
			  
			        var actionBar = dojo.create("div", { "class": "dijitDialogPaneActionBar"  }, d1.containerNode);
		
			        var buttonOk=new dijit.form.Button({"label": "OK",
			            onClick: function(){
			            	
			            	d1.destroyRecursive(); 
			            	
			            } });
			        buttonOk.placeAt(actionBar);
			        domclass.add(buttonOk.focusNode, "yesNoOkButton");
			        
//			        var buttonCancel=new dijit.form.Button({"label": "Cancel",
//			            onClick: function(){
//			            	
//			            	d1.destroyRecursive();
//			            	
//			            	} });
//			        buttonCancel.placeAt(actionBar);
//			        domclass.add(buttonCancel.focusNode, "cancelButton");
            }else if(idName=="okCancelCallback" && needConfirmation==false){
            	  var actionBar = dojo.create("div", { "class": "dijitDialogPaneActionBar"  }, d1.containerNode);
          		
			        var buttonOk=new dijit.form.Button({"label": "OK",
			            onClick: function(){
			            	callbackYes();
			            	d1.destroyRecursive(); 
			            	
			            } });
			        buttonOk.placeAt(actionBar);
			        domclass.add(buttonOk.focusNode, "yesNoOkButton");
			        
			        var buttonCancel=new dijit.form.Button({"label": "Cancel",
			            onClick: function(){
			            	callbackYes();
			            	d1.destroyRecursive();
			            	
			            	} });
			        buttonCancel.placeAt(actionBar);
			        domclass.add(buttonCancel.focusNode, "cancelButton");
            }else{
            	 var actionBar = dojo.create("div", { "class": "dijitDialogPaneActionBar"  }, d1.containerNode);
           		
			        var buttonOk=new dijit.form.Button({"label": "OK",
			            onClick: function(){
			            	
			            	d1.destroyRecursive(); 
			            	
			            } });
			        buttonOk.placeAt(actionBar);
			        domclass.add(buttonOk.focusNode, "yesNoOkButton");
			        
			        var buttonCancel=new dijit.form.Button({"label": "Cancel",
			            onClick: function(){
			            	
			            	d1.destroyRecursive();
			            	
			            	} });
			        buttonCancel.placeAt(actionBar);
			        domclass.add(buttonCancel.focusNode, "cancelButton");
            }
         
           
            
	        d1.startup();
	        
            d1.show();
			
		},
	
		
	});
	return InvoiceELSDialog;
});


