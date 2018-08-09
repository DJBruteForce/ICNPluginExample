define([ "dojo/_base/declare",
		 "dojo/_base/lang",         
		 "ecm/model/Action", 
	     "dojo/dom-class",
		 "ecm/model/Request", 
		 "dojo/_base/declare",
		 "ecm/widget/dialog/BaseDialog", 
		 "dojo/data/ItemFileWriteStore",
		 "dojo/dom",
		 "dojo/Deferred",
		 "dijit/form/Form",
		 "gilCnPluginDojo/util/MathJs",
		 "dojox/grid/DataGrid",
		 "dojo/_base/lang",
		 "dojo/_base/event",
		 "dojo/_base/array",
		 "dojo/dom-class",
		 "dojo/dom-style",
		 "dijit/registry",
		 "dojo/store/Memory",
		 "dijit/form/FilteringSelect",
		 "gilCnPluginDojo/util/JsonUtils",
		 "gilCnPluginDojo/NonModalConfirmDialog",
		 "gilCnPluginDojo/DialogMessages",
		 "ecm/widget/dialog/BaseDialog",
		 "gilCnPluginDojo/util/ManageStyles",
		 "dojo/parser",
		 "dojo/dom-construct",
		 "dojo/text!./templates/LineItemsDialog.html",
		 "dijit/_WidgetsInTemplateMixin",
		 "dojo/_base/json",
		 "gilCnPluginDojo/SerialNumberDialog",
		 "gilCnPluginDojo/OEMProductDialog",
		 
		 "gilCnPluginDojo/GILDialog",
		 "dojox/form/Uploader",
		 "dojox/form/uploader/plugins/Flash",
		 "dojox/form/uploader/FileList",
		 "dojo/keys",
		 "dojo/window",
		 "dojo/io-query",
		 "dojo/on",
		 "dojo/query",
		 "dojo/NodeList-dom",
		 "dojo/aspect",
	      ],
		 
        function(declare, lang,  Action, domClass, Request,declare, BaseDialog,ItemFileWriteStore,dom,Deferred,form,MathJs,DataGrid,lang,on,array, domclass,
				domStyle,registry,Memory, FilteringSelect, JsonUtils,NonModalConfirmDialog,DialogMessages, BaseDialog, MStyles, parser, construct, template, _WidgetsInTemplateMixin,
				json,SerialNumberDialog, OEMProductDialog, GILDialog,Uploader,Flash,fileList,Keys, window, ioquery,on,query,nodelist,aspect) {
	
	var LineItemsDialog =  declare("gilCnPluginDojo.LineItemsDialog", [ BaseDialog, GILDialog ], {
		
		contentString : template,
		widgetsInTemplate : true,
		callerDialog:null,
		jsonInvoiceObject:null,
		thisDialog:null,
		serviceParameters:null,
		jsonLineItems:null,
		jsonSerialNumbers:null,
		jsonImportSerialNumberList:null,
		jsonLineItemGeneral:null,
		jsonLineItem:null,
		newLineItem:null,
		grid:null,
		changeFlag:true,
		lineItemsGrid:null,
		tmpIndex:null,
		tmpItem:null,
		tmDescFlag:null,
		tmpItemTMDesc:null,
		wentToBackend:false,
		isAddTaxCodeListener:false,
		isTMLookupFinished:true,
		waitCounter:0,
		timer:null,
		charge:null,
		isErrorDisplayed:false,
		TYPE_MODEL:  "TM",
		PARTNUMBER : "P",
		OTHER_CHARGE: "OC",
		/****
		 * 	Story 1840627: [CM 8.5]  GIL - US - moving to iERP needs to change VAT codes	
		 */
		WITH_TAX:"I1",
		NO_TAX: "U1",
		/**End**/
		
		constructor : function(args) {
			lang.mixin(this, args);
	
		},
		
		postCreate : function() {
			
			this.inherited(arguments);
			dojo.style(this.cancelButton.domNode,"display","none"); 
			
			var screen = window.getBox();
			var width = 985;
			var height = 780;
			
			//if the dialog is larger than the screen, resize it
			if(width > screen.w  ){
				console.log("Screen.w: "+screen.w +"Width:" +width);
				width = screen.w - 10;

			}
			if(height >screen.h){
				console.log("Screen.h: "+screen.h +"Height:" +height);
				height = screen.h - 10;
			}
			this.setSize(width, height);
			this.setResizable(false);
						
			this.addButton("Cancel", "cancel", false, true);
			this.addButton("OK", "saveOk", false, true);
			this.setTitle("Invoice Line Items");
			thisDialog=this;
			
			thisDialog.initLineItems();
			dojo.style(this.contentArea, 'overflow', 'auto');
//			MStyles.disableStyleSheet("stylesheetOEM1");
//			MStyles.disableStyleSheet("stylesheetOEM2");
			
			
			
		},
		otherSettings:function(){
			thisDialog.onAChange();
			thisDialog.initializeDecimalFields();
//			thisDialog.onChangeOtherCharge();
			thisDialog.upperCaseFields();
			thisDialog.setLayout();	
			thisDialog.customizeDialog();
			thisDialog.hideBilledToIgf();
			thisDialog.disableEnterForInputs();
		},
		hideBilledToIgf:function(){
			thisDialog.jsonInvoiceObject=JSON.parse(thisDialog.serviceParameters["jsonInvoiceEls"]);
		//comes empty this flag
			if(thisDialog.serviceParameters["country"]!="US" || thisDialog.jsonInvoiceObject.taxesIndc=="N"){
				domStyle.set(dojo.byId(this.id+"_llnBillIgf"), 'display', 'none');
				domStyle.set(registry.byId(this.id+"_lnBillIgf").domNode, 'display', 'none');
			}
		},
		
		requestFocus:function(toSelect){
			
				dojo.byId(toSelect).focus();
			
			

		},
		
		validateSerialMes:function(evt){
			
			if(!thisDialog.serial.isValid() || !thisDialog.mesNum.isValid()){
				thisDialog.add.set("disabled",true);
				thisDialog.update.set("disabled",true);
			}else{
				thisDialog.add.set("disabled",false);
				thisDialog.update.set("disabled",false);
			}
		},
		validateTypeString:function(evt){
			
			if(evt.keyCode==Keys.DELETE || evt.keyCode==Keys.LEFT_ARROW  || evt.keyCode==Keys.RIGHT_ARROW|| evt.keyCode==Keys.CTRL  || evt.keyCode==Keys.SHIFT ){
				//do nothing
			}else if(evt.target.value.length==4){
				thisDialog.requestFocus(thisDialog.id+"_model");
			}else if(evt.target.value.length==1 || evt.target.value.length==0){
				
				thisDialog.desc.set("value","");
			}
		},
		validateModelString:function(evt){
			
			if(evt.keyCode==Keys.DELETE || evt.keyCode==Keys.LEFT_ARROW  || evt.keyCode==Keys.RIGHT_ARROW|| evt.keyCode==Keys.CTRL  || evt.keyCode==Keys.SHIFT ){
				//do nothing
			}else if(evt.target.value.length==3 || evt.target.value.length==4){
				thisDialog.lookupTypeModelDescription();
			}else if (evt.target.value.length==2 || evt.target.value.length==1){
				thisDialog.desc.set("value","");
			}
		},
		onAChange:function(){
			dojo.connect(dojo.byId(thisDialog.id+"_type"),"keyup", function(evt){
				thisDialog.validateTypeString(evt);
				});
			dojo.connect(dojo.byId(thisDialog.id+"_mesNum"),"keyup", function(evt){
				thisDialog.validateSerialMes(evt);
				});
			dojo.connect(dojo.byId(thisDialog.id+"_serial"),"keyup", function(evt){
				thisDialog.validateSerialMes(evt);
				});
			dojo.connect(dojo.byId(thisDialog.id+"_model"),"keyup", function(evt){
				thisDialog.validateModelString(evt);
				});
			dojo.connect(dojo.byId(thisDialog.id+"_unitPrice"),"change", function(){
				
				if(thisDialog.unitPrice.get('value')=="")thisDialog.unitPrice.set("0.00");
				/**Removing this condition in LineItemFrame I see 2 methods invoking recalculateVatamount,
				 *  one saying only if country is US and second doesn't have any condition, leaving this code without conditions**/				
				//if(thisDialog.serviceParameters["country"]=="US"){ 
						
					thisDialog.recalculateVATAmountFilled();
//				}
			});
			dojo.connect(dojo.byId(thisDialog.id+"_qty"),"change", function(){
				
				
						
				thisDialog.recalculateVATAmountFilled();
				thisDialog.enableSerialMESBasedOnQuantity();
//				}
			});
			
			dojo.connect(dojo.byId(thisDialog.id+"_type"),"blur", function(){
				if(thisDialog.type.get("value").trim()!="" && thisDialog.model.get("value").trim() != ""){
					thisDialog.lookupTypeModelDescription();
				}else{
					thisDialog.desc.set("value","");	
				}
					
			});
			dojo.connect(dojo.byId(thisDialog.id+"_model"),"blur", function(){
				if(thisDialog.model.get("value").trim()!="" && thisDialog.type.get("value").trim() != ""){
					thisDialog.lookupTypeModelDescription();
				}else{
					thisDialog.desc.set("value","");
				}
				
				

			});
			dojo.connect(dojo.byId(thisDialog.id+"_partNum"),"change", function(){
				//found the methos in cm8.4 is empty
//				thisDialog.convertPartToTypeModel();

			});
			
		},
		addTaxCodeListener:function(){
			debugger;
			if(thisDialog.isAddTaxCodeListener==false){
			var dd=dojo.byId(this.id+"_taxCode_menu");
			var opt=dd.getElementsByClassName("dijitReset dijitMenuItemLabel");
			var opts=Array.from(opt);
			opts.forEach(function(item){item.addEventListener("click",function(){
				console.log(item.innerText);	
				var taxCode=item.innerText.substring(0,item.innerText.indexOf("-"))
				thisDialog.recalculateVATAmountFilled(taxCode.trim());
				});});
			thisDialog.isAddTaxCodeListener=true;
			}

		},
		enableSerialMESBasedOnQuantity:function(){
			var disable=false;
		
			if(thisDialog.qty.get('value')>1){
				disable=true;
			}
			if(!thisDialog.typeModel.get("checked")){//if it's a TM
				disable=true;
			}
			
			thisDialog.serial.set('disabled',disable);
			thisDialog.mesNum.set('disabled',disable);
			if(disable){
				thisDialog.mesNum.set('value',"");
				thisDialog.serial.set("value","");
			}
			
		},
		lookupTypeModelDescription:function(){
			
			thisDialog.isTMLookupFinished=false;
			thisDialog.initItem("1");
			thisDialog.initNewLnItem();
			//load Type and model in lineItem to look for description, can't load prodcat, prodtype...etc since it is a new item
			thisDialog.newLineItem["TYPE"]=thisDialog.type.get('value');
			thisDialog.newLineItem["MODEL"]=thisDialog.model.get('value');
			//prepare values to be send to backend
			thisDialog.serviceParameters['jsonLineItem']=JSON.stringify(thisDialog.newLineItem);
			thisDialog.serviceParameters['request']=arguments.callee.nom;
			
			
			
			ecm.messages.progress_message_LineItemELSService = "Looking for description.";
			
			Request.postPluginService("GilCnPlugin","LineItemELSService",'application/x-www-form-urlencoded',{
				requestBody : ioquery.objectToQuery(this.serviceParameters),
				requestCompleteCallback: lang.hitch(this,function(response) {
					thisDialog.msg.clearMessages();
					thisDialog.newLineItem=JSON.parse(response.currentLineItem);
					debugger;
					if(thisDialog.newLineItem.dialogErrors.length>0){
							var msgs=thisDialog.newLineItem.dialogErrors;
							for (var i in msgs) {
								thisDialog.msg.addErrorMessages(msgs[i]);
							}
					}
					
					if(thisDialog.msg.hasErrorMessages()){
						thisDialog.showMessage("Error",thisDialog.msg.errorMessages(),false, null, null);
					} else {
						if(thisDialog.newLineItem.tmDescFlag){//means there was a result for that TM 
							thisDialog.wentToBackend=true;
							thisDialog.desc.set("value", thisDialog.newLineItem.DESCRIPTION);
							thisDialog.isTMLookupFinished=true;
							console.log("PROD_CAT returned: "+ thisDialog.newLineItem.PRODCAT);
							console.log("MAXUNITPRICE returned: "+ thisDialog.newLineItem.MAXUNITPRICE);
							console.log("MINUNITPRICE returned: "+ thisDialog.newLineItem.MINUNITPRICE);
							
							
						}else{
							thisDialog.newLineItem=null;
							thisDialog.desc.set("value","");
						}
							
					}
					
				})	
			});	
			
		},
		recalculateVATAmountFilled:function(taxCodeSelected){
			debugger;
			//initialize tmp Item that will be used
			thisDialog.initItem("1");
			thisDialog.initNewLnItem();
			//loading into init item the values given in the dialog
			thisDialog.getFilledValues(taxCodeSelected);
			//recalculate
			if(thisDialog.serviceParameters['country']=='US'){
				
				thisDialog.setUsTaxPercentage();
			}
			thisDialog.recalculateVATAmount(thisDialog.newLineItem);
			//fill dialog with recalculated values
			thisDialog.loadRecalculatedVatAmts();
			
		},
		loadRecalculatedVatAmts:function(){
			debugger;
			
			if(thisDialog.newLineItem.EXTENDEDPRICE!=null && !isNaN(thisDialog.newLineItem.EXTENDEDPRICE)){
				thisDialog.extLnPr.set("value", thisDialog.newLineItem.EXTENDEDPRICE);
			}
			
			if(thisDialog.newLineItem.VATAMOUNT!=null &&  !isNaN(thisDialog.newLineItem.VATAMOUNT)){
				thisDialog.taxAmtLn.set("value", thisDialog.newLineItem.VATAMOUNT);
				
			}
			
			if(thisDialog.newLineItem.TOTALVATAMOUNT!=null && !isNaN(thisDialog.newLineItem.TOTALVATAMOUNT)){
				
				thisDialog.totTaxAmt.set("value", thisDialog.newLineItem.TOTALVATAMOUNT);
			}
						
			if(thisDialog.newLineItem.UNITPRICE!=null && !isNaN(thisDialog.newLineItem.UNITPRICE)){
				
				thisDialog.unitPrice.set("value", thisDialog.newLineItem.UNITPRICE);
			}
				
			
		},
		
		saveOk: function(){
			if(!thisDialog.isErrorDisplayed)//Test Defect 1860348: CM8.5-ELS Line items- Ok button on line items with error message
				thisDialog.revalidateInput();
			

			
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
			jsonInvoiceEls['lineItemNetAmount']=thisDialog.formatNumber(totalUnitNet,2);
			jsonInvoiceEls['lineItemVatAmount']=thisDialog.formatNumber(totalUnitVat,2);
			jsonInvoiceEls['lineItemTotalAmount']=thisDialog.formatNumber(MathJs.add(totalUnitVat,totalUnitNet),2);
			jsonInvoiceEls['netBalance']=thisDialog.formatNumber(availableUnitNet,2);
			jsonInvoiceEls['vatBalance']=thisDialog.formatNumber(availableUnitVat,2);
			thisDialog.callerDialog.lnItemTot.set('value',jsonInvoiceEls.lineItemTotalAmount);
			thisDialog.callerDialog.lnItemTxTot.set('value',jsonInvoiceEls.lineItemVatAmount);
			thisDialog.callerDialog.lnItemNetTot.set('value',jsonInvoiceEls.lineItemNetAmount);
//			thisDialog.callerDialog.lnItemTot.set('value',jsonInvoiceEls.lineItemTotalAmount);
//			thisDialog.callerDialog.lnItemTot.set('value',jsonInvoiceEls.lineItemTotalAmount);
			//				setLINEITEMNETAMOUNT(totalUnitNet.toString());
//	        setLINEITEMVATAMOUNT(totalUnitVat.toString());
//	        setLINEITEMTOTALAMOUNT(totalUnitVat.add(totalUnitNet).toString());
//	        setNETBALANCE(availableUnitNet.toString());
//	        setVATBALANCE(availableUnitVat.toString());
		},
		cancel:function(){
			if(thisDialog.changeFlag){
					thisDialog.changeFlag=false;
					thisDialog.msg=new DialogMessages();		
					thisDialog.msg.addConfirmMessages("Your changes will be lost. Are you sure you want to cancel?");
					thisDialog.showMessage("Confirm",thisDialog.msg.confirmMessages(), true, thisDialog.choiceYCancel,thisDialog.choiceNCancel);;
				
			} else{
				thisDialog.destroyRecursive();
			}
			
		},
		choiceYCancel:function(){
			this.destroyRecursive();
		},
		choiceNCancel:function(){
			//nothing to do
			
			
		},
	    onChangeOtherCharge: function() {
	    	debugger;
		     on(dijit.byId(thisDialog.id+ "_otherChargesSelect"), "change", function(event) {
					
		    	var value = thisDialog.charge.get('value'); 
		    	var descValue = thisDialog.charge.get('displayedValue').trim();
		        thisDialog.desc.set('value',descValue.substring(descValue.indexOf(" - ") +3,descValue.length));
		 	    thisDialog.type.set('value',value.substring(0,value.indexOf("/")));
		 	    thisDialog.model.set('value',value.substring(value.indexOf("/")+1,value.length));

		     });
		},
		
		upperCaseFields: function() {
			var allValTextbox = dojo.query('input#[id^="'+ this.id +'_"]');
			for(var i=0; i<allValTextbox.length; i++){
				this.toUpperCase(allValTextbox[i]);
			}
		},
		
		getValue:function(value){

			if(value!=null && value.length!=0){
				return value.toString().trim();
			} else {
				return ""
			}
		},
		
		
		
		getMatchingSerial: function() {
			
	        if (thisDialog.jsonSerialNumbers != null) {
	        
		        for (var i = 0; i < thisDialog.jsonSerialNumbers.length; i++) {	
		        	var item = lang.clone(thisDialog.jsonSerialNumbers[i]);
		        	if(item.TYPE == thisDialog.newLineItem.TYPE){
			        	if(item.MODEL == thisDialog.newLineItem.MODEL){
				        	if(item.UNITPRICE == thisDialog.newLineItem.UNITPRICE){
				        		thisDialog.jsonSerialNumbers.splice(i, 1);
				        		return item;	
				        	} else if(item.UNITPRICE!=null && ( parseFloat(thisDialog.newLineItem.UNITPRICE) == parseFloat("0"))) {
					        		thisDialog.jsonSerialNumbers.splice(i, 1);
					        		return item;
				        	}
			        	}
		        	}
		        }
		        for (var i = 0; i < thisDialog.jsonSerialNumbers.length; i++) {
		        	var item = lang.clone(thisDialog.jsonSerialNumbers[i]);
		        	if(item.TYPE == thisDialog.newLineItem.TYPE){
			        	if(item.MODEL == thisDialog.newLineItem.MODEL){
			        		if(item.UNITPRICE!=null && ( parseFloat(thisDialog.newLineItem.UNITPRICE) == parseFloat("0"))) {
				        		thisDialog.jsonSerialNumbers.splice(i, 1);
				        		return item;
				        	}
			        	}
		        	}
		        }
		        for (var i = 0; i < thisDialog.jsonSerialNumbers.length; i++) {
		        	var item = lang.clone(thisDialog.jsonSerialNumbers[i]);
		        	if( (item.TYPE!=null && item.TYPE.length == 0) &&
		        	    (item.MODEL!=null && item.MODEL.length == 0) ) {
		        		thisDialog.jsonSerialNumbers.splice(i, 1);
		        		return item;
		        	}
		        }
	        
	        }

	        return null;
		},
		
		
		
		countMatchingSerials: function() {
			
	        var unitpricematchcount = 0;
	        var typemodelmatchcount = 0;
	        var matchcount = 0;
	        
	        if (thisDialog.jsonSerialNumbers != null) {
	        
		        for (var i = 0; i < thisDialog.jsonSerialNumbers.length; i++) {
		        	if(thisDialog.jsonSerialNumbers[i].TYPE == thisDialog.newLineItem.TYPE){
			        	if(thisDialog.jsonSerialNumbers[i].MODEL == thisDialog.newLineItem.MODEL){
				        	if(thisDialog.jsonSerialNumbers[i].UNITPRICE == thisDialog.newLineItem.UNITPRICE){
				        		unitpricematchcount++; // the type model and unit price match
				        	} else if(thisDialog.jsonSerialNumbers[i].UNITPRICE!=null && ( parseFloat(thisDialog.newLineItem.UNITPRICE) == parseFloat("0"))) {
				        		unitpricematchcount++;
				        	}
			        	}
		        	}
		        }
		        for (var i = 0; i < thisDialog.jsonSerialNumbers.length; i++) {
		        	if(thisDialog.jsonSerialNumbers[i].TYPE == thisDialog.newLineItem.TYPE){
			        	if(thisDialog.jsonSerialNumbers[i].MODEL == thisDialog.newLineItem.MODEL){
			        		if(thisDialog.jsonSerialNumbers[i].UNITPRICE!=null && ( parseFloat(thisDialog.newLineItem.UNITPRICE) == parseFloat("0"))) {
				        		typemodelmatchcount++; // if the price was zero, we want to match just on the // type model
				        	}
			        	}
		        	}
		        }
		        for (var i = 0; i < thisDialog.jsonSerialNumbers.length; i++) {
		        	if( (thisDialog.jsonSerialNumbers[i].TYPE!=null && thisDialog.jsonSerialNumbers[i].TYPE.length == 0) &&
		        	    (thisDialog.jsonSerialNumbers[i].MODEL!=null && thisDialog.jsonSerialNumbers[i].MODEL.length == 0) ) {
		        		 matchcount++;// the type model match
		        	}
		        }
	        
	        }
	        // next we'd use the type / model / price matches
	        if (unitpricematchcount > 0)
	            return unitpricematchcount;

	        // then we'd use just type / model matches
	        if (typemodelmatchcount > 0)
	            return typemodelmatchcount;

	        // otherwise we match serials without a type / model
	        return matchcount;
		},
		
	    formatNumber: function(num, precis) {
	    	var res = MathJs.format(  MathJs.round(num, precis),  {notation: 'fixed', precision: precis});
	    	return res;
	    },
	    
		formatAmount: function() {
			var num = this.formatNumber(this.unitPrice.get('value'),2);
	        this.unitPrice.set('value',num);
		},
		
		initializeDecimalFields: function() {
			this.isAlphaNumeric(dijit.byId(this.id+"_type"));
			this.isAlphaNumeric(dijit.byId(this.id+"_model"));
			this.isAlphaNumeric(dijit.byId(this.id+"_partNum"));
			this.isAlphaNumeric(dijit.byId(this.id+"_term"));
			
			  this.isNumberKey(dijit.byId(this.id+"_unitPrice"));
			  this.isInteger(dijit.byId(this.id+"_qty"));
			  this.isInteger(dijit.byId(this.id+"_invLnNum"));
			  this.isInteger(dijit.byId(this.id+"_invLnNum2"));
		},
		
		ok: function() {
			 
		},
		
		retrieveValues:function(){

		},
		show: function() {
			this.inherited("show", []);
		},

		
		customizeDialog:function(){
			
			domStyle.set(thisDialog.contentArea,'paddingBottom',"0px !important");
			//need specific
			 dijit.byId(this.add).domNode.firstChild.firstChild.childNodes[2].style.width="72px";
			 dijit.byId(this.update).domNode.firstChild.firstChild.childNodes[2].style.width="72px";
			 dijit.byId(this.deleteB).domNode.firstChild.firstChild.childNodes[2].style.width="72px";
			 dijit.byId(this.clear).domNode.firstChild.firstChild.childNodes[2].style.width="72px";
//			 dijit.byId(this.importTS).domNode.childNodes[0].firstChild.childNodes[2].style.width="72px";
//			 dijit.byId(this.importPart).domNode.childNodes[0].firstChild.childNodes[2].style.width="72px";
			 thisDialog.typeModelEnable();//always have to be selected the type model radiobutton, then disabling some fields
			 
			 thisDialog.poNum.set("value",jsonInvoiceEls.purchaseOrderNum); 
			 thisDialog.costCenter.set("value",jsonInvoiceEls.costCenter);
			
		},
		revalidateInput:function(){
			debugger;	
			//call server
			thisDialog.msg.clearMessages();
			var items=thisDialog.jsonLineItems;
			if(items.length>2500){
				
				thisDialog.msg.addErrorMessages("Exceed maximum quantity of line items.");
				
				thisDialog.showMessage("Error",thisDialog.msg.errorMessages(),false,null, null);
				return;
			}
			
			thisDialog.msg=new DialogMessages();
			thisDialog.serviceParameters['request']="revalidateInput";
			this.serviceParameters['jsonLineItems']= JSON.stringify(thisDialog.jsonLineItems);
			ecm.messages.progress_message_LineItemELSService = "Validating...";
			
			Request.postPluginService("GilCnPlugin","LineItemELSService",'application/x-www-form-urlencoded',{
				requestBody : ioquery.objectToQuery(this.serviceParameters),
				requestCompleteCallback: lang.hitch(this,function(response) {
					debugger;
						var answer=response;
					if(response.lnItemDialogJson!= null){
						var lnItemDialogJson=JSON.parse(response.lnItemDialogJson);
						
						thisDialog.jsonLineItems=lnItemDialogJson.lineItems;
						if(lnItemDialogJson.dialogWarns.length>0){
								var msgs=lnItemDialogJson.dialogWarns;
								for (var i in msgs) {
									
									thisDialog.msg.addWarningMessages(msgs[i]);
								}
						}
						
						console.log("RevalidateInput: There are no erros neither warnings. Saving");
						thisDialog.validateMaxMinPrice();

							
					}else if(response.currentLineItem != null){
						

						var tmpLineItem=JSON.parse(response.currentLineItem);
						if(tmpLineItem.dialogErrors.length>0){
								var msgs=tmpLineItem.dialogErrors;
								for (var i in msgs) {
									
									thisDialog.msg.addErrorMessages(msgs[i]);
								}
						}
						
						if(thisDialog.msg.hasErrorMessages()){
							
							thisDialog.selectItem(tmpLineItem.gridIndex);
							var store = thisDialog.grid.store;
							var item = thisDialog.grid.getItem(tmpLineItem.gridIndex);
							
							store.setValue(item, 'DESCRIPTION', tmpLineItem.DESCRIPTION);
							store.setValue(item, 'PRODCAT', tmpLineItem.PRODCAT);
							store.setValue(item, 'PRODTYPE', tmpLineItem.PRODTYPE);
							store.setValue(item, 'MINUNITPRICE',tmpLineItem.MINUNITPRICE);
							store.setValue(item, 'MAXUNITPRICE', tmpLineItem.MAXUNITPRICE);
//							thisDialog.grid.update();
							thisDialog.grid.updateRow(tmpLineItem.gridIndex);
							thisDialog.loadValues(item);//thisDialog.grid.selection.getSelected()[0]);
							thisDialog.showMessage("Error",thisDialog.msg.errorMessages(),false, null, null);
						} else{
							console.log("RevalidateInput: It was returned a currentLnItem but doesn't contain an error. This is not correct.");
						}
						
//						
					}

	
					
				})	
			});	
			
			
			
		},
		validateMaxMinPrice:function(){
			var i=0;
			thisDialog.validateItems(i,thisDialog.jsonLineItems);
			
			
		},
		saving:function(){
			debugger;
			jsonInvoiceEls.lineItems=thisDialog.jsonLineItems;
			
			thisDialog.recalculateUnitAmounts();
			thisDialog.enablePONumberBasedOnUnits();
			thisDialog.refreshStore();
			console.log("saveOk");
			if(thisDialog.msg.hasWarningMessages()){
				selectSeveralItems(lnItemDialogJson.itemsWithWarnings);
				thisDialog.showMessage("Warning",thisDialog.msg.warningMessages(),true, thisDialog.destroyRecursive, null, true);
			}else{
				thisDialog.destroyRecursive();
			}
		},
		choiceYCorrect:function(){
			debugger;
			thisDialog.selectItem(thisDialog.tmpIndex);
			var selected=thisDialog.grid.selection.getSelected();
			thisDialog.loadValues(thisDialog.grid.selection.getSelected()[0]);
			thisDialog.tmpIndex=null;
			//this.destroyRecursive();
			return;
		},
		choiceNCorrect:function(){
			debugger;
			console.log("IN choiceNCorrect ");
			console.log("tmpIndex+1 ",MathJs.add(thisDialog.tmpIndex,1) + "items.length: "+thisDialog.jsonLineItems.length);
			thisDialog.validateItems(MathJs.add(thisDialog.tmpIndex,1),thisDialog.jsonLineItems);
		},
		checkTMDescFlag:function(item){
			debugger;
			//set the item values retrieved from the server 
			if(item.DESCRIPTION==""){
				item.DESCRIPTION=thisDialog.tmpItemTMDesc.DESCRIPTION;
			}
			if(item.PRODCAT==""){
				item.PRODCAT=thisDialog.tmpItemTMDesc.PRODCAT;
			}
			if(item.PRODTYPE==""){
				item.PRODTYPE=thisDialog.tmpItemTMDesc.PRODTYPE;
			}
			if(item.MINUNITPRICE==""){
				item.MINUNITPRICE=thisDialog.tmpItemTMDesc.MINUNITPRICE;
			}
			if(item.MAXUNITPRICE==""){
				item.MAXUNITPRICE=thisDialog.tmpItemTMDesc.MAXUNITPRICE;
			}
			
			return thisDialog.tmDescFlag;
			
			
		},
//		choiceYmaxPrice:function(){
//			console.log("choiceYMinPrice item:"+thisDialog.tmpIndex)
//			thisDialog.selectItem(thisDialog.tmpIndex);
//			thisDialog.loadValues(thisDialog.grid.selection.getSelected()[thisDialog.tmpIndex]);
//			this.destroyRecursive();
//			return;
//		},
//		choiceNmaxPrice:function(){
//			//continue with next validations
//			console.log("choiceNMaxPrice item:"+thisDialog.tmpIndex);
//			thisdialog.checkMinPrice();
//			//return  ......
//			
//		},
//		checkMinPrice:function(){
//			var unitPrice=item.UNITPRICE;
//			var	minPrice=item.MINUNITPRICE;
//			if(unitPrice<minPrice){
//				thisDialog.msg.addErrorMessages("Unit Price is less than minimum allowable value of " + minPrice + ".\nDo you wish to correct?");
//				
//				thisDialog.showMessage("Confirmation",thisDialog.msg.errorMessages(),true,thisDialog.choiceYminPrice, thisDialog.choiceNminPrice);
//				return false
//			}
//			return true;
//		},
//		choiceYminPrice:function(){
//			console.log("choiceYMinPrice item:"+thisDialog.tmpIndex)
//			thisDialog.selectItem(thisDialog.tmpIndex);
//			thisDialog.loadValues(thisDialog.grid.selection.getSelected()[thisDialog.tmpIndex]);
//			this.destroyRecursive();
//			return;
//		},
//		choiceNminPrice:function(){
//			//Continue with next item.. continue with logic, or just add a flag to the item and remove it from the list to validate?
//			//still stop the for..... and user should click again Ok or call again revalidateInput() but this time will have flag Y and we should take car of asking to the user....
//		//return;
//			return;
//		},
		validateItems:function(index,items){
			console.log("validateItems: before for index: "+index);
			var i=0;
			if(index==items.length){
				console.log("validateItems() Finished validatingMaxMinPrices index value: "+i+" items.length: "+items.length)
				thisDialog.saving();
			}else{
				for(i =index; i < items.length; i++){						
					thisDialog.msg.clearMessages();
					item=items[i];
					console.log("for item ["+i+"]");
						if(item.INVOICEITEMTYPE==thisDialog.TYPE_MODEL){
						

						var unitPrice=Number(item.UNITPRICE);
							
						var maxPrice=Number(item.MAXUNITPRICE);
						var	minPrice=Number(item.MINUNITPRICE);
						if(maxPrice!=0){
							thisDialog.tmpIndex=i;
							console.log("validateItems: tmpIndex:"+ i);
							if(unitPrice>maxPrice){
							
								thisDialog.msg.addErrorMessages(" Unit Price exceeds maximum allowable value of " + maxPrice + ".\nDo you wish to correct?");
								thisDialog.tmpItem=item;
								
	//							thisDialog.showMessage("Confirmation",thisDialog.msg.errorMessages(),true,thisDialog.choiceYmaxPrice, thisDialog.choiceNmaxPrice);
							}
							if(unitPrice<minPrice){
								thisDialog.msg.addErrorMessages("Unit Price is less than minimum allowable value of " + minPrice + ".\nDo you wish to correct?");
								
							}
							if(thisDialog.msg.hasErrorMessages()){
								thisDialog.showMessage("Confirmation",thisDialog.msg.errorMessages(),true,thisDialog.choiceYCorrect, thisDialog.choiceNCorrect);
								console.log("showing msg return");
								return;
							}

						}
					}
				}
				console.log("outside For");
				debugger;
				if(i==items.length){//means it iterated whole array of items the need to finish with saving
					console.log("validateItems() Finished validatingMaxMinPrices index value: "+i+" items.length: "+items.length)
					thisDialog.saving();
				}else{
					console.log("validateItems() index value: "+i+" items.length: "+items.length)
				}
			}
		},
		selectItem:function(index){
			debugger;
			console.log("selectItem");
			if (thisDialog.grid.selection.selectedIndex >= 0)
		    {
		        // If there is a currently selected row, deselect it now
		        thisDialog.grid.selection.deselectAll();
		      //  thisDialog.grid.selection.setSelected(index, false);
		    }
		    thisDialog.grid.selection.setSelected(index, true);
		    thisDialog.grid.render();
		    
		},
		selectSeveralItems:function(itemsIndex){
			
				debugger;
				console.log("selectSeveralItems");
				if (thisDialog.grid.selection.selectedIndex >= 0)
			    {
			        // If there is a currently selected row, deselect it now
			        thisDialog.grid.selection.deselectAll();
			      //  thisDialog.grid.selection.setSelected(index, false);
			    }
				if(itemsIndex != null){
					for(var i in itemsIndex){
				    	thisDialog.grid.selection.setSelected(i, true);
					}
				}
			    thisDialog.grid.render();

		},
		initLineItems: function(){
			
			
			thisDialog.msg=new DialogMessages();
			thisDialog.serviceParameters['request']=arguments.callee.nom;
			ecm.messages.progress_message_LineItemELSService = "Opening Line Items ...";
			 
			Request.postPluginService("GilCnPlugin","LineItemELSService",'application/x-www-form-urlencoded',{
				requestBody : ioquery.objectToQuery(this.serviceParameters),	
				requestCompleteCallback: lang.hitch(this,function(response) {
					
					thisDialog.jsonLineItemGeneral=JSON.parse(response.lnItemDialogJson);
					if(thisDialog.jsonLineItemGeneral.dialogErrorMsgs.length>0){
							var msgs=thisDialog.jsonLineItemGeneral.dialogErrorMsgs;
							for (var i in msgs) {
								thisDialog.msg.addErrorMessages(msgs[i]);
							}
					}
					
					if(thisDialog.msg.hasErrorMessages()){
							
								thisDialog.showMessage("Error",thisDialog.msg.errorMessages(),false, null, null);
					}else{//continue with logic
						thisDialog.jsonLineItems=thisDialog.jsonLineItemGeneral.lineItems;
						
						thisDialog.loadCombos();
						
					}		
						
					
					thisDialog.showLineItemsGrid();
					thisDialog.otherSettings();
//					thisDialog.show();

				
					
				})
				
				
			});
			
		},
		
		loadCombos:function(){
			var jsonUtils = new JsonUtils();
			if(thisDialog.jsonLineItemGeneral.disableVatCodes){//for US
				thisDialog.taxCode.set('disabled',true);							
			}else{
				//load vatcode/taxCode combo
				debugger;
				thisDialog.taxCode.set("options", thisDialog.jsonLineItemGeneral.vatCodes);
				thisDialog.taxCode.set("value", jsonUtils.getValueIfOtherKeyExists(thisDialog.jsonLineItemGeneral.vatCodes,"value", true));
			}
			thisDialog.createOtherCharges();
//			thisDialog.charge.set("options", thisDialog.jsonLineItemGeneral.otherCharges);
//			thisDialog.charge.set("value", jsonUtils.getValueIfOtherKeyExists(thisDialog.jsonLineItemGeneral.otherCharges,"value", true));
		},

		getFilledValues:function(taxCodeSelected){
			
		    if(thisDialog.typeModel.get("checked")){
		    	
		    	thisDialog.newLineItem['INVOICEITEMTYPE']=thisDialog.TYPE_MODEL;
		    	thisDialog.newLineItem['TYPE']=thisDialog.type.get('value');
		    	thisDialog.newLineItem['MODEL']=thisDialog.model.get('value');
		    	thisDialog.newLineItem['PARTNUMBER']="";
		    	
		    }else if(thisDialog.part.get("checked")){
		    	
		    	thisDialog.newLineItem['INVOICEITEMTYPE']=thisDialog.PARTNUMBER;
		    	thisDialog.newLineItem['PARTNUMBER']=thisDialog.partNum.get('value');
		    	thisDialog.newLineItem['TYPE']="";
		    	thisDialog.newLineItem['MODEL']="";
		    	
		    }else if(thisDialog.otherCharge.get("checked")){
		    	
		    	thisDialog.newLineItem['INVOICEITEMTYPE']=thisDialog.OTHER_CHARGE;
		    	thisDialog.newLineItem['OTHERCHARGE']=thisDialog.charge.get('value');
		    	thisDialog.newLineItem['TYPE']=thisDialog.type.get('value');
		    	thisDialog.newLineItem['MODEL']=thisDialog.model.get('value');
		    }
			
			if(thisDialog.invLnNum.get('value').trim().length!=0){
				thisDialog.newLineItem['LINENUMBER']=thisDialog.invLnNum.get('value').trim();
			}
			if(thisDialog.invLnNum2.get('value').trim().length!=0){
				thisDialog.newLineItem['SUBLINENUMBER']=thisDialog.invLnNum2.get('value').trim();
			}
			if(thisDialog.qty.get('value').trim().length!=0){
				thisDialog.newLineItem['QUANTITY']=thisDialog.qty.get('value').trim();
			}
//			if(thisDialog.serial.get('value').trim().length!=0){
				thisDialog.newLineItem['SERIAL']=thisDialog.serial.get('value').trim();
//			}
//			if(thisDialog.mesNum.get('value').trim().length!=0){
				thisDialog.newLineItem['MESNUMBER']=thisDialog.mesNum.get('value').trim();
//			}

			var unitPrice_=parseFloat(thisDialog.unitPrice.get('value'))||0;
			if(unitPrice_!=0){
				thisDialog.newLineItem['UNITPRICE']=thisDialog.unitPrice.get('value').trim();
				thisDialog.newLineItem['EXTENDEDPRICE']=thisDialog.extLnPr.get('value').trim();
				thisDialog.newLineItem['VATAMOUNT']=thisDialog.taxAmtLn.get('value').trim();
				thisDialog.newLineItem['TOTALVATAMOUNT']=thisDialog.totTaxAmt.get('value').trim();
			}
//			if(thisDialog.term.get('value').trim().length!=0){
				thisDialog.newLineItem['TERM']=thisDialog.term.get('value').trim();
//			}
//			if(thisDialog.desc.get('value').trim().length!=0){
				thisDialog.newLineItem['DESCRIPTION']=thisDialog.desc.get('value').trim();
//			}
//			if(thisDialog.poNum.get('value').trim().length!=0){
				thisDialog.newLineItem['PONUMBER']=thisDialog.poNum.get('value').trim();
//			}
				thisDialog.newLineItem['COSTCENTER']=thisDialog.costCenter.get('value').trim();
			if(taxCodeSelected!=null && taxCodeSelected!=""){
				console.log("taxCode selected:"+taxCodeSelected.trim())
				thisDialog.newLineItem['VATCODE']=taxCodeSelected.trim();
				
			}else if(thisDialog.serviceParameters['country']!="US"){
				debugger;
				console.log("taxCode value:"+thisDialog.taxCode.get('value').trim());
				thisDialog.newLineItem['VATCODE']=thisDialog.taxCode.get('value').trim();
			}
//			if(thisDialog.finType.get('value').trim().length!=0){
				thisDialog.newLineItem['FINANCINGTYPE']=thisDialog.finType.get('value').trim();
//			}
			

			if(thisDialog.lnBillIgf.get('value')){
				thisDialog.newLineItem['BILLEDTOIGFINDC']="Y";
			}else{
				thisDialog.newLineItem['BILLEDTOIGFINDC']="N";
			}

		},
		
		initValues:function(){
			
			thisDialog.loadCurrentList();
			thisDialog.initItem("1");
			thisDialog.initNewLnItem();
			thisDialog.getFilledValues();
		
		},
		
		areSiblingsConfigured:function(){
			
			var itemsSelected = this.grid.selection.getSelected();
			var lineNumber="";
			var lastLineNumber="";
			var item=null;
			var itemGrid=null;
			if(itemsSelected == null || itemsSelected.length==0){
				return false;
			}else{
				for(var i = 0; i < itemsSelected.length; i++){
					item=itemsSelected[i];
					if(item !=null){
						lineNumber=item.LINENUMBER[0];
						if(lineNumber == lastLineNumber){
							   // we check this line all ready
	                        continue;
						}else{
							lastLineNumber=lineNumber;
							for(var j=0;j<thisDialog.grid.store._arrayOfAllItems.length; j++){
								itemGrid=thisDialog.grid.store._arrayOfAllItems[j];
								if((itemGrid!=null) && lineNumber == itemGrid.LINENUMBER[0] && (itemGrid.CONFIGUSED == "Y" || itemGrid.CONFIGUSED == "y")){
									return true;
								}
								
							}
						}
					}
				}
			}
			return false;
		},
		
		
		addTypeAndModel: function(callback){
			
			thisDialog.msg=new DialogMessages();
			thisDialog.serviceParameters['request']=arguments.callee.nom;
			thisDialog.serviceParameters['jsonLineItem']=JSON.stringify(thisDialog.newLineItem);
			thisDialog.serviceParameters['jsonLineItems']=JSON.stringify(thisDialog.jsonLineItems);
			ecm.messages.progress_message_LineItemELSService = "Processing...";
			
			Request.postPluginService("GilCnPlugin","LineItemELSService",'application/x-www-form-urlencoded',{
				requestBody : ioquery.objectToQuery(this.serviceParameters),
				requestCompleteCallback: lang.hitch(this,function(response) {

					thisDialog.newLineItem=JSON.parse(response.currentLineItem);
					if(thisDialog.newLineItem.dialogErrors.length>0){
							var msgs=thisDialog.newLineItem.dialogErrors;
							for (var i in msgs) {
								thisDialog.msg.addErrorMessages(msgs[i]);
							}
					}
					
					if(thisDialog.msg.hasErrorMessages()){
						thisDialog.showMessage("Error",thisDialog.msg.errorMessages(),false, null, null);
					} else {
						callback();
						return true;	
					}		
				})	
			});	
					
		},
		
		
		updateLnItem:function(){

			var dialogMsgs=new DialogMessages();
			dialogMsgs.addErrorMessages("The selected line numbers are configured and cannot be updated.");
			
			if(thisDialog.areSiblingsConfigured()){
				thisDialog.showMessage("Error",dialogMsgs.errorMessages(),false,null, null);
				return;
			}
			debugger;
			var items = thisDialog.grid.selection.getSelected();
			
			if(thisDialog.newLineItem!=null && thisDialog.wentToBackend){
				thisDialog.wentToBackend=false;
				//do not initialize it, it was already initialized when tabbing out from a field
				thisDialog.newLineItem["DOCUMENTMODE"]="3";
				thisDialog.loadCurrentList();				
				thisDialog.getFilledValues();//get las values from dialog
			
			}else{
				thisDialog.initItem("3");	
				
				thisDialog.initNewLnItem();
				thisDialog.copyExistingItem();
				thisDialog.getFilledValues();
			}
			
		
			
			if(thisDialog.newLineItem.COUNTRY=="US"){
				thisDialog.setUsTaxPercentage();
				if(thisDialog.newLineItem.USTAXPERCENT==null){
					thisDialog.newLineItem["VATCODE"]=thisDialog.NO_TAX;
				}else if(isNaN(Number(thisDialog.newLineItem.USTAXPERCENT.trim()))  ||Number( thisDialog.newLineItem.USTAXPERCENT.trim())==0){
					thisDialog.newLineItem["VATCODE"]=thisDialog.NO_TAX;
				}else{
					thisDialog.newLineItem["VATCODE"]=thisDialog.WITH_TAX;
				}
				thisDialog.recalculateVATAmount(thisDialog.newLineItem);

			}
			
			thisDialog.jsonLineItem = thisDialog.newLineItem;
			
			if(thisDialog.newLineItem.INVOICEITEMTYPE==thisDialog.TYPE_MODEL) {
				
				if(items!=null && items.length==1){
					thisDialog.addTypeAndModel(
							thisDialog.validate.bind(null,
									thisDialog.updateLastProcess));
				} else {
					thisDialog.validate(
							thisDialog.updateLastProcess);
				}

			} else {
				thisDialog.validate(
						thisDialog.updateLastProcess);
			}
			


		},
		
/*		updateLastProcess:function(){
			
			var items = thisDialog.grid.selection.getSelected();
			var item=null;
			if(items==null || items.length==0){
				thisDialog.clearItem();
			} else if(items.length ==1){
					 item=items[0];
					 if(item !=null){
						 var quantity=parseFloat(thisDialog.newLineItem.QUANTITY)||0;
						 if(quantity>1 && thisDialog.INVOICEITEMTYPE==thisDialog.PARTNUMBER || quantity>1 && thisDialog.newLineItem.INVOICEITEMTYPE==thisDialog.OTHER_CHARGE ){
							 thisDialog.updateItemForPart();
						 }else if(quantity>1&&thisDialog.newLineItem.INVOICEITEMTYPE==thisDialog.TYPE_MODEL){
								//Fernando*** serial logic
								//thisDialog.updateItemSerials()
						 		} else {
						 				thisDialog.jsonLineItem=thisDialog.newLineItem;
						 				thisDialog.renumberAndSpread();
						 		}
					 }
					} else {
						
						var result=true;
						for(var k=0;k<items.length;k++){
							item=items[k];
							thisDialog.copyItem(item);
							result=thisDialog.validateInput(thisDialog.newLineItem,thisDialog.noAction);
							if(!result)break;
						}
						if(result){
							for(var l=0;l<items.length;l++){
								item=items[l];
								thisDialog.copyItem(item);
								thisDialog.recalculateVATAmount(thisDialog.newLineItem,thisDialog.noAction);
							}
						}
						thisDialog.renumberAndSpread();//should it be spread and then renumber?
					}
		},*/
		
		updateLastProcess:function(){
			debugger;
			var items = thisDialog.grid.selection.getSelected();
			var item=null;
			if(items==null || items.length==0){
				thisDialog.clearItem();
			} else if(items.length ==1){
					 item=items[0];
					 if(item !=null){
						 var quantity=parseFloat(thisDialog.newLineItem.QUANTITY)||0;
						 if(quantity>1 && thisDialog.newLineItem.INVOICEITEMTYPE==thisDialog.PARTNUMBER || 
								 quantity>1 && thisDialog.newLineItem.INVOICEITEMTYPE==thisDialog.OTHER_CHARGE ){
							 thisDialog.updateItemForPart(item);
						 }else if(quantity>1&&thisDialog.newLineItem.INVOICEITEMTYPE==thisDialog.TYPE_MODEL){
									thisDialog.updateItemSerials(item)
						 		} else {
						 				thisDialog.jsonLineItem=thisDialog.newLineItem;
						 				var mixItem={};
						 				thisDialog.copyChangedFields(item,thisDialog.newLineItem,mixItem);
						 				
						 				thisDialog.loadCurrentList();
						 				thisDialog.renumberAndSpread(
						 						thisDialog.clearFields);
						 		}
					 }
					} else {
						
						var result=true;
						var mixItem={};
						result=thisDialog.validateInput(thisDialog.newLineItem,thisDialog.noAction);
						for(var k=0;k<items.length;k++){
							item=items[k];
							
							mixItem={};
							
							thisDialog.copyChangedFields(item,thisDialog.newLineItem,mixItem);
							 var tmpItem = lang.mixin(lang.clone(thisDialog.newLineItem), mixItem);
							if(result) {
								thisDialog.recalculateVATAmount(thisDialog.tmpItem);
							}
							
							
							item.VATCODE[0]=tmpItem.VATCODE;
							item.EXTENDEDPRICE[0]=tmpItem.EXTENDEDPRICE;
							item.VATAMOUNT[0]=tmpItem.VATAMOUNT;
							item.TOTALVATAMOUNT[0]=tmpItem.TOTALVATAMOUNT;
							item.UNITPRICE[0]=tmpItem.UNITPRICE;
						 	
						}
						thisDialog.loadCurrentList();
						thisDialog.renumberAndSpread(
								thisDialog.clearFields);//should it be spread and then renumber?
						
					}
			thisDialog.grid.selection.deselectAll();
			thisDialog.grid.selection.clear();
		},
		
	
		
		updateItemSerials:function(item){

			thisDialog.initItem("1");
			thisDialog.initNewLnItem();
			thisDialog.getFilledValues();
			thisDialog.newLineItem["QUANTITY"] = "1";
			thisDialog.newLineItem["TOTALVATAMOUNT"] = thisDialog.newLineItem["VATAMOUNT"];
			thisDialog.newLineItem["EXTENDEDPRICE"] = thisDialog.newLineItem["UNITPRICE"];
			if(thisDialog.serviceParameters['country']=='US'){
				if(item.USTAXPERCENT[0]==null){
					thisDialog.newLineItem["VATCODE"]=thisDialog.NO_TAX;
				}else if(isNaN(Number(item.USTAXPERCENT[0].trim()))  ||Number( item.USTAXPERCENT[0].trim())==0){
					thisDialog.newLineItem["VATCODE"]=thisDialog.NO_TAX;
				}else{
					thisDialog.newLineItem["VATCODE"]=thisDialog.WITH_TAX;
				}

			}
			
			var qty = thisDialog.qty.get('value');
			var zeroPrice = false;
			var matchingSerialCount = thisDialog.countMatchingSerials();
			var auxLnItem = null;
			var auxArrOfLnItems= [];
			
			var unitPrice = parseFloat(thisDialog.newLineItem["UNITPRICE"]);
			
			if(isNaN(unitPrice) || MathJs.compare(unitPrice,0)!=0) {
				zeroPrice = true;
			}
		    // if the quantity is zero, that means copy in all matching type / model
		    // / $ matching records
		    if (qty == 0){
		        qty = matchingSerialCount;
		    }
		    
		   var mixItem={};
		    thisDialog.copyChangedFields(item,thisDialog.newLineItem,mixItem);
			var auxLnItem ={
						'BILLEDTOIGFINDC':thisDialog.getValue(item.BILLEDTOIGFINDC),
						'BLANKTYPEMODEL':thisDialog.getValue(item.BLANKTYPEMODEL),  
						'CONFIGUSED':thisDialog.getValue(item.CONFIGUSED),         
						'COUNTRY': thisDialog.getValue(item.COUNTRY),
						'LINENUMBER': thisDialog.getValue(item.LINENUMBER),
						'TYPE': thisDialog.getValue(item.TYPE),
						'MODEL': thisDialog.getValue(item.MODEL),
						'SERIAL': thisDialog.getValue(item.SERIAL),
						'QUANTITY': thisDialog.getValue(item.QUANTITY),
						'PARTNUMBER': thisDialog.getValue(item.PARTNUMBER),
						'DESCRIPTION': thisDialog.getValue(item.DESCRIPTION),
						'UNITPRICE': thisDialog.getValue(item.UNITPRICE),
						'VATAMOUNT': thisDialog.getValue(item.VATAMOUNT),
						'EXTENDEDPRICE': thisDialog.getValue(item.EXTENDEDPRICE),
						'TOTALVATAMOUNT':thisDialog.getValue( item.TOTALVATAMOUNT),
						'MESNUMBER': thisDialog.getValue(item.MESNUMBER),
						'VATCODE':thisDialog.getValue( item.VATCODE),
						'SUBLINENUMBER':thisDialog.getValue( item.SUBLINENUMBER),
						'CONFIGUSED': thisDialog.getValue(item.CONFIGUSED),
						'COSTCENTER':thisDialog.getValue(item.COSTCENTER),          
						'DESCRIPTION':thisDialog.getValue(item.DESCRIPTION),        
						'EQUIPSOURCE':thisDialog.getValue(item.EQUIPSOURCE),        
						'EXTENDEDPRICE':thisDialog.getValue(item.EXTENDEDPRICE),    
						'EXTENSIONIND':thisDialog.getValue(item.EXTENSIONIND),      
						'FINANCINGTYPE':thisDialog.getValue(item.FINANCINGTYPE),    
						'FROMQUOTE':thisDialog.getValue(item.FROMQUOTE),            
						'INVOICEITEMTYPE':thisDialog.getValue(item.INVOICEITEMTYPE),
						'LINENUMBER':thisDialog.getValue(item.LINENUMBER),          
						'MAXUNITPRICE':thisDialog.getValue(item.MAXUNITPRICE),      
						'MESINDICATOR':thisDialog.getValue(item.MESINDICATOR),      
						'MESNUMBER':thisDialog.getValue(item.MESNUMBER),            
						'MINUNITPRICE':thisDialog.getValue(item.MINUNITPRICE),      
						'MODEL':thisDialog.getValue(item.MODEL),                    
						'OTHERCHARGE':thisDialog.getValue(item.OTHERCHARGE),        
						'PARTNUMBER':thisDialog.getValue(item.PARTNUMBER),          
						'PONUMBER':thisDialog.getValue(item.PONUMBER),              
						'PRODCAT':thisDialog.getValue(item.PRODCAT),                
						'QUANTITY':thisDialog.getValue(item.QUANTITY),              
						'SERIAL':thisDialog.getValue(item.SERIAL),                  
						'SUBLINENUMBER':thisDialog.getValue(item.SUBLINENUMBER),    
						'TAXESINDICATOR':thisDialog.getValue(item.TAXESINDICATOR),  
						'TERM':thisDialog.getValue(item.TERM),                      
						'TOTALVATAMOUNT':thisDialog.getValue(item.TOTALVATAMOUNT),  
						'TRANSACTIONTYPE':thisDialog.getValue(item.TRANSACTIONTYPE),
						'TYPE':thisDialog.getValue(item.TYPE),                      
						'UNITPRICE':thisDialog.getValue(item.UNITPRICE),            
						'USTAXPERCENT':thisDialog.getValue(item.USTAXPERCENT),      
						'VATAMOUNT':thisDialog.getValue(item.VATAMOUNT),            
						'VATCODE':thisDialog.getValue(item.VATCODE),                

						
					};
	        	
	        	auxArrOfLnItems.push(auxLnItem);

	        thisDialog.newLineItem=auxLnItem;
	        thisDialog.grid.store.deleteItem(item);
		    thisDialog.grid._refresh();
		     
		    thisDialog.grid.selection.deselectAll();
		    thisDialog.grid.render();
	        thisDialog.loadCurrentList();
	        //adding Serial dialog functionality in update flow. March 2018 by Lisas request

	        thisDialog.openSerialDialog();

		

		},
		openSerialDialog:function(callback){
			debugger;
			if(typeof callback === "function"){
				callback();
			}
		 var serialNumberDlg = new SerialNumberDialog({LineItemsDialog:thisDialog, serviceParameters: thisDialog.serviceParameters });
		 serialNumberDlg.show();
		},

		
		getVatPercentage:function(item){
			var percentage=null;
			if(thisDialog.serviceParameters['country']=="US"){
				if(item.TAXESINDICATOR=="Y" && item.BILLEDTOIGFINDC=="N"){
					percentage=0;
					return percentage;
				}else{
					 if(item.USTAXPERCENT==""){
						 item.USTAXPERCENT=0;
					 }
					 return thisDialog.toNumber(item.USTAXPERCENT);
				}
			}
			
			if ( item.VATCODE.toString().trim().length > 0) {
				percentage = thisDialog.toNumber(thisDialog.jsonLineItemGeneral.vatPercentagesStr[item.VATCODE.toString().trim()]);
			}
			return percentage;
		},
		  toNumber: function(num) {

		    	var res = MathJs.eval(num); 
		    	return res;
		    },
		    
		recalculateVATAmount:function(item){
			
			try {
					var price = 0.00;
					price =item.UNITPRICE;
					var qty=0;
					qty=item.QUANTITY;
					var totalVat=0.00;
					var vat=0.00;
					var vatPercent = 0.00
					vatPercent=thisDialog.getVatPercentage(item);
					
					var extendedPrice=0.00;
					extendedPrice= MathJs.multiply(price,qty);
					
					if(MathJs.compare(Number(vatPercent),0.00)!=0) {
						vat= MathJs.multiply(price,vatPercent);
						totalVat= MathJs.multiply(vat,qty);

						vat=MathJs.divide(vat,100.00);
						vat = MathJs.round(vat, 2) ;
						totalVat=MathJs.divide(totalVat,100);
						totalVat= MathJs.round(totalVat, 2) ;
					}
					if(thisDialog.serviceParameters['country']=='US'){
						
						if(isNaN(vat)  || vat==0){
							item["VATCODE"]=thisDialog.NO_TAX;
						}else{
							item["VATCODE"]=thisDialog.WITH_TAX;
						}
					}
					if(extendedPrice!=null && !isNaN(extendedPrice)){
						item["EXTENDEDPRICE"]=thisDialog.formatNumber(extendedPrice,2);
					}
					
					if(vat!=null && !isNaN(vat)){
						item["VATAMOUNT"]=thisDialog.formatNumber(vat,2);
					}
					
					if(totalVat!=null && !isNaN(totalVat)){
						item["TOTALVATAMOUNT"]=thisDialog.formatNumber(totalVat,2);
					}
								
					if(price!=null && !isNaN(price)){
						item["UNITPRICE"]=thisDialog.formatNumber(price,2);
					}
						
					
			} catch(e) {
				console.log('Error in method ' + arguments.callee.nom + ' - ' + e);
			}

		},
		
		copyExistingItem:function(){
			var items = this.grid.selection.getSelected();
			if(items!=null && items.length==1){
				item=items[0];
				thisDialog.newLineItem["BILLEDTOIGFINDC"]=item.BILLEDTOIGFINDC[0];
				thisDialog.newLineItem['BLANKTYPEMODEL']=item.BLANKTYPEMODEL[0];  
				thisDialog.newLineItem['CONFIGUSED']=item.CONFIGUSED[0];       
//				thisDialog.newLineItem['COUNTRY']= item.COUNTRY[0];
				thisDialog.newLineItem['LINENUMBER']= item.LINENUMBER[0];
				thisDialog.newLineItem['TYPE']= item.TYPE[0];
				thisDialog.newLineItem['MODEL']= item.MODEL[0];
				thisDialog.newLineItem['SERIAL']= item.SERIAL[0];
				thisDialog.newLineItem['QUANTITY']= item.QUANTITY[0];
				thisDialog.newLineItem['PARTNUMBER']= item.PARTNUMBER[0];
				thisDialog.newLineItem['DESCRIPTION']= item.DESCRIPTION[0];
				thisDialog.newLineItem['UNITPRICE']= item.UNITPRICE[0];
				thisDialog.newLineItem['VATAMOUNT']= item.VATAMOUNT[0];
				thisDialog.newLineItem['EXTENDEDPRICE']= item.EXTENDEDPRICE[0];
				thisDialog.newLineItem['TOTALVATAMOUNT']= item.TOTALVATAMOUNT[0];
				thisDialog.newLineItem['MESNUMBER']= item.MESNUMBER[0];
				thisDialog.newLineItem['VATCODE']= item.VATCODE[0];
				thisDialog.newLineItem['SUBLINENUMBER']= item.SUBLINENUMBER[0];
				thisDialog.newLineItem['CONFIGUSED']= item.CONFIGUSED[0];
				thisDialog.newLineItem['COSTCENTER']=item.COSTCENTER[0];          
				thisDialog.newLineItem['DESCRIPTION']=item.DESCRIPTION[0];       
				thisDialog.newLineItem['EQUIPSOURCE']=item.EQUIPSOURCE[0];        
				thisDialog.newLineItem['EXTENDEDPRICE']=item.EXTENDEDPRICE[0];    
				thisDialog.newLineItem['EXTENSIONIND']=item.EXTENSIONIND[0];      
				thisDialog.newLineItem['FINANCINGTYPE']=item.FINANCINGTYPE[0];    
				thisDialog.newLineItem['FROMQUOTE']=item.FROMQUOTE[0];      
				thisDialog.newLineItem['INVOICEITEMTYPE']=item.INVOICEITEMTYPE[0];
				thisDialog.newLineItem['LINENUMBER']=item.LINENUMBER[0];        
				thisDialog.newLineItem['MAXUNITPRICE']=item.MAXUNITPRICE[0];     
				thisDialog.newLineItem['MESINDICATOR']=item.MESINDICATOR[0];     
				thisDialog.newLineItem['MESNUMBER']=item.MESNUMBER[0];       
				thisDialog.newLineItem['MINUNITPRICE']=item.MINUNITPRICE[0];    
				thisDialog.newLineItem['MODEL']=item.MODEL[0];         
				thisDialog.newLineItem['OTHERCHARGE']=item.OTHERCHARGE[0];       
				thisDialog.newLineItem['PARTNUMBER']=item.PARTNUMBER[0];          
				thisDialog.newLineItem['PONUMBER']=item.PONUMBER[0];           
				thisDialog.newLineItem['PRODCAT']=item.PRODCAT[0];              
				thisDialog.newLineItem['QUANTITY']=item.QUANTITY[0];              
				thisDialog.newLineItem['SERIAL']=item.SERIAL[0];                 
				thisDialog.newLineItem['SUBLINENUMBER']=item.SUBLINENUMBER[0];    
				thisDialog.newLineItem['TAXESINDICATOR']=item.TAXESINDICATOR[0];
				thisDialog.newLineItem['TERM']=item.TERM[0];   
				thisDialog.newLineItem['TOTALVATAMOUNT']=item.TOTALVATAMOUNT[0];  
				thisDialog.newLineItem['TRANSACTIONTYPE']=item.TRANSACTIONTYPE[0];
				thisDialog.newLineItem['TYPE']=item.TYPE[0];                     
				thisDialog.newLineItem['UNITPRICE']=item.UNITPRICE[0];            
				thisDialog.newLineItem['USTAXPERCENT']=item.USTAXPERCENT[0];      
				thisDialog.newLineItem['VATAMOUNT']=item.VATAMOUNT[0];           
				thisDialog.newLineItem['VATCODE']=item.VATCODE[0];

			}
		},
		
		renumberAndSpread:function(callback){

					
			this.serviceParameters['jsonLineItems']= JSON.stringify(thisDialog.jsonLineItems);	
			this.serviceParameters['request']=arguments.callee.nom;
			ecm.messages.progress_message_LineItemELSService = "Processing...";	
			
			Request.postPluginService("GilCnPlugin","LineItemELSService",'application/x-www-form-urlencoded',{	
					requestBody : ioquery.objectToQuery(this.serviceParameters),
					requestCompleteCallback: lang.hitch(this,function(response) {
						thisDialog.jsonLineItems=JSON.parse(response.listLineItems);
						thisDialog.refreshStore();
						if(typeof callback === "function"){
							callback();
						}
					})	
				});
			
		},
//		wait:function(){
//			debugger;
//			
//			if( thisDialog.waitCounter>2 || thisDialog.isTMLookupFinished){
//				thisDialog.waitCounter=0;
//				thisDialog.isTMLookupFinished=true;
//				clearInterval(thisDialog.timer);
//				
//			}else{
//				
//				thisDialog.waitCounter++;
//				console.log("waiting..."+thisDialog.waitCounter);
//				
//			}
//
//	
//			   
//		},
//		:function(){
//			thisDialog.timer=setInterval(thisDialog.wait,2000);
//			thisDialog.wait();
//			
//		   thisDialog.doAddItem();		
//		},


		addLnItem:function(){
			
			debugger;
			thisDialog.jsonInvoiceObject=JSON.parse(thisDialog.serviceParameters["jsonInvoiceEls"]);
			var dialogMsgs=new DialogMessages();
			dialogMsgs.addErrorMessages("The invoice is configured, lines cannot be added.");

			if(thisDialog.jsonInvoiceObject.configuredInvIndc=="Y" || thisDialog.jsonInvoiceObject.configuredInvIndc=="y"){
				thisDialog.showMessage("Error",dialogMsgs.errorMessages(),false,null, null);
				return;
			}
			if(thisDialog.newLineItem!=null && thisDialog.wentToBackend){
				thisDialog.wentToBackend=false;
				//do not initialize it, it was already initialized when tabbing out from a field
				
				thisDialog.loadCurrentList();			
				thisDialog.getFilledValues();//get las values from dialog
			
			}else{
				thisDialog.initValues();
			}
			
			thisDialog.newLineItem["CONFIGUSED"]="N";
			
			if(thisDialog.serviceParameters['country']=='US'){
				thisDialog.setUsTaxPercentage();
				if(thisDialog.newLineItem.USTAXPERCENT==null){
					thisDialog.newLineItem["VATCODE"]=thisDialog.NO_TAX;
				}else if(isNaN(Number(thisDialog.newLineItem.USTAXPERCENT.trim()))  ||Number( thisDialog.newLineItem.USTAXPERCENT.trim())==0){
					thisDialog.newLineItem["VATCODE"]=thisDialog.NO_TAX;
				}else{
					thisDialog.newLineItem["VATCODE"]=thisDialog.WITH_TAX;
				//	Defect 1836079: CM8.5-ELS Invoice-Line items- Bill to IGF
					thisDialog.recalculateVATAmount(thisDialog.newLineItem);
				}
				
			}
			if(thisDialog.typeModel.get("checked")){//if it's a TM
				thisDialog.addTypeAndModel(
						thisDialog.validate.bind(null,
								thisDialog.settingMoreLineItemValues));
			}else{
				thisDialog.validate(
						thisDialog.settingMoreLineItemValues);
			}
			
			

		},
		enablePONumberBasedOnUnits:function(){
			
			thisDialog.callerDialog.purchOrdNum.set('disabled', false);//enabling
			var numLines=jsonInvoiceEls.lineItems.length;
			var tmpLnItem=null;
			for(var i in jsonInvoiceEls.lineItems){
				tmpLnItem=jsonInvoiceEls.lineItems[i];
				if(tmpLnItem.PONUMBER.trim().length>0){
					thisDialog.callerDialog.purchOrdNum.set('disabled', true);//disabling
					thisDialog.callerDialog.purchOrdNum.set('value',tmpLnItem.PONUMBER);
					break;
					
				}
			}
			
			
			
		},	
		loadCurrentList:function(){
			
				var lineItems = [];
				var i = 0;
				var lnItem;
				thisDialog.lineItemsGrid.items;
				debugger;
				var numLines=thisDialog.grid.store._arrayOfAllItems.length;
				var items=thisDialog.grid.store._arrayOfAllItems;
				for(i = 0; i < numLines; i++){
					
					lnItem=items[i];
					if(lnItem==null){
						continue;
					}
					lineItems.push({
						'BILLEDTOIGFINDC':thisDialog.getValue(lnItem.BILLEDTOIGFINDC),
						'BLANKTYPEMODEL':thisDialog.getValue(lnItem.BLANKTYPEMODEL),  
						'CONFIGUSED':thisDialog.getValue(lnItem.CONFIGUSED),         
						'COUNTRY': thisDialog.getValue(lnItem.COUNTRY),
						'LINENUMBER': thisDialog.getValue(lnItem.LINENUMBER),
						'TYPE': thisDialog.getValue(lnItem.TYPE),
						'MODEL': thisDialog.getValue(lnItem.MODEL),
						'SERIAL': thisDialog.getValue(lnItem.SERIAL),
						'QUANTITY': thisDialog.getValue(lnItem.QUANTITY),
						'PARTNUMBER': thisDialog.getValue(lnItem.PARTNUMBER),
						'DESCRIPTION': thisDialog.getValue(lnItem.DESCRIPTION),
						'UNITPRICE': thisDialog.getValue(lnItem.UNITPRICE),
						'VATAMOUNT': thisDialog.getValue(lnItem.VATAMOUNT),
						'EXTENDEDPRICE': thisDialog.getValue(lnItem.EXTENDEDPRICE),
						'TOTALVATAMOUNT':thisDialog.getValue( lnItem.TOTALVATAMOUNT),
						'MESNUMBER': thisDialog.getValue(lnItem.MESNUMBER),
						'VATCODE':thisDialog.getValue( lnItem.VATCODE),
						'SUBLINENUMBER':thisDialog.getValue( lnItem.SUBLINENUMBER),
						'CONFIGUSED': thisDialog.getValue(lnItem.CONFIGUSED),
						'COSTCENTER':thisDialog.getValue(lnItem.COSTCENTER),          
						'DESCRIPTION':thisDialog.getValue(lnItem.DESCRIPTION),        
						'EQUIPSOURCE':thisDialog.getValue(lnItem.EQUIPSOURCE),        
						'EXTENDEDPRICE':thisDialog.getValue(lnItem.EXTENDEDPRICE),    
						'EXTENSIONIND':thisDialog.getValue(lnItem.EXTENSIONIND),      
						'FINANCINGTYPE':thisDialog.getValue(lnItem.FINANCINGTYPE),    
						'FROMQUOTE':thisDialog.getValue(lnItem.FROMQUOTE),            
						'INVOICEITEMTYPE':thisDialog.getValue(lnItem.INVOICEITEMTYPE),
						'LINENUMBER':thisDialog.getValue(lnItem.LINENUMBER),          
						'MAXUNITPRICE':thisDialog.getValue(lnItem.MAXUNITPRICE),      
						'MESINDICATOR':thisDialog.getValue(lnItem.MESINDICATOR),      
						'MESNUMBER':thisDialog.getValue(lnItem.MESNUMBER),            
						'MINUNITPRICE':thisDialog.getValue(lnItem.MINUNITPRICE),      
						'MODEL':thisDialog.getValue(lnItem.MODEL),                    
						'OTHERCHARGE':thisDialog.getValue(lnItem.OTHERCHARGE),        
						'PARTNUMBER':thisDialog.getValue(lnItem.PARTNUMBER),          
						'PONUMBER':thisDialog.getValue(lnItem.PONUMBER),              
						'PRODCAT':thisDialog.getValue(lnItem.PRODCAT),                
						'QUANTITY':thisDialog.getValue(lnItem.QUANTITY),              
						'SERIAL':thisDialog.getValue(lnItem.SERIAL),                  
						'SUBLINENUMBER':thisDialog.getValue(lnItem.SUBLINENUMBER),    
						'TAXESINDICATOR':thisDialog.getValue(lnItem.TAXESINDICATOR),  
						'TERM':thisDialog.getValue(lnItem.TERM),                      
						'TOTALVATAMOUNT':thisDialog.getValue(lnItem.TOTALVATAMOUNT),  
						'TRANSACTIONTYPE':thisDialog.getValue(lnItem.TRANSACTIONTYPE),
						'TYPE':thisDialog.getValue(lnItem.TYPE),                      
						'UNITPRICE':thisDialog.getValue(lnItem.UNITPRICE),            
						'USTAXPERCENT':thisDialog.getValue(lnItem.USTAXPERCENT),      
						'VATAMOUNT':thisDialog.getValue(lnItem.VATAMOUNT),            
						'VATCODE':thisDialog.getValue(lnItem.VATCODE),                

						
					});
				}
				
				 
				console.log(JSON.stringify(lineItems));
			//set lineItems list into global lineitems var
				thisDialog.jsonLineItems=lineItems;
				
		
			},
		
		validate:function(callback){
			
			thisDialog.validateInput(thisDialog.newLineItem, callback);
		},
		
		settingMoreLineItemValues:function(){

			var qty=thisDialog.qty.get('value');
	        
			if(	(qty==1 && thisDialog.newLineItem.INVOICEITEMTYPE == thisDialog.PARTNUMBER) ||
				(qty==1 && thisDialog.newLineItem.INVOICEITEMTYPE == thisDialog.OTHER_CHARGE ) ||
				(qty==1 &&thisDialog.newLineItem.INVOICEITEMTYPE == thisDialog.TYPE_MODEL)  )	{
				thisDialog.newLineItem["TOTALVATAMOUNT"] = thisDialog.newLineItem["VATAMOUNT"];
				thisDialog.newLineItem["EXTENDEDPRICE"] = thisDialog.newLineItem["UNITPRICE"];
				thisDialog.jsonLineItem=thisDialog.newLineItem;
				
				var auxArr = [thisDialog.jsonLineItem];
				thisDialog.renumberLineItems(auxArr,thisDialog.clearFields);
				
			} else if ( (qty>1 && thisDialog.newLineItem.INVOICEITEMTYPE == thisDialog.PARTNUMBER) ||
					   (qty>1 && thisDialog.newLineItem.INVOICEITEMTYPE == thisDialog.OTHER_CHARGE) ) {
				
					 thisDialog.addItemforPart();
				
			} else if (qty==0 && thisDialog.newLineItem.INVOICEITEMTYPE == thisDialog.TYPE_MODEL){

					 var serialNumberDlg = new SerialNumberDialog({LineItemsDialog:thisDialog, serviceParameters: thisDialog.serviceParameters });
					 serialNumberDlg.show();
				
			} else if (qty<=thisDialog.countMatchingSerials()) {
				
					 thisDialog.addItemSerials();
					 thisDialog.renumberAndSpread(thisDialog.clearFields);
					 
			} else if (qty>1) {
				
					  var serialNumberDlg = new SerialNumberDialog({LineItemsDialog:thisDialog, serviceParameters: thisDialog.serviceParameters });
					  serialNumberDlg.show();
			}

		},
		
		importParts:function(){
			console.log("importParts");
			debugger;
			thisDialog.jsonInvoiceObject=JSON.parse(thisDialog.serviceParameters["jsonInvoiceEls"]);
			var dialogMsgs=new DialogMessages();
			dialogMsgs.addErrorMessages("The invoice is configured, lines cannot be added.");

			if(thisDialog.jsonInvoiceObject.configuredInvIndc=="Y" || thisDialog.jsonInvoiceObject.configuredInvIndc=="y"){
				thisDialog.showMessage("Error",dialogMsgs.errorMessages(),false,null, null);
				return;
			}
			thisDialog.jsonInvoiceObject=JSON.parse(thisDialog.serviceParameters["jsonInvoiceEls"]);
			thisDialog.initValues();
			debugger;
			var file = this.importPart._files[0];
			
			var parm_part_filename = (file.fileName ? file.fileName : file.name)

			var formData = new FormData();
			formData.append('frontEndAction', 'importSerial');
			formData.append('file', file);
			formData.append('mimetype', file.type);
			formData.append('fileName', parm_part_filename); 
			var taxIndc="";
			if(thisDialog.jsonInvoiceObject.taxesIndc==""){
				taxIndc="N";
			}else{
				taxIndc=thisDialog.jsonInvoiceObject.taxesIndc;
			}
			var itemsNum=thisDialog.jsonLineItems.length-1;
			formData.append('repositoryId',thisDialog.serviceParameters['repositoryId']);
			var usTax="";
			if(thisDialog.jsonInvoiceObject.usTaxPercent==null || thisDialog.jsonInvoiceObject.usTaxPercent==""){
				usTax=0;
			}else{
				usTax=thisDialog.jsonInvoiceObject.usTaxPercent;
			}
			formData.append('usTaxPercent',usTax);
			
			formData.append('usTaxIndc',taxIndc);
			formData.append('billedToIgfIndc',thisDialog.newLineItem.BILLEDTOIGFINDC);
			formData.append('country', thisDialog.serviceParameters['country']); 
			formData.append('request', arguments.callee.nom);
			if(thisDialog.taxCode.get('value').trim().length!=0){
				formData.append("vatCode",thisDialog.taxCode.get('value').trim())
			}else{
				formData.append("vatCode","");
			}
			formData.append('jsonLineItems',JSON.stringify(thisDialog.jsonLineItems));		
			formData.append('invoiceNumber',thisDialog.jsonInvoiceObject.invoiceNumber);
			formData.append('invoiceDate',thisDialog.jsonInvoiceObject.invoiceDate);
			formData.append('vatAmount',thisDialog.jsonInvoiceObject.vatAmount);
			formData.append('provinceCode',thisDialog.jsonInvoiceObject.provinceCode);
			
			 Request.postFormToPluginService("GilCnPlugin", "LineItemELSService", formData,{

				 
				 requestCompleteCallback: lang.hitch(this, function (response) {
				    	var dialogMessagesResponse = new DialogMessages();
				    	debugger;
				    	
				    	thisDialog.jsonLineItems=JSON.parse(response.listLineItems);
				    	thisDialog.importPart.focusNode.value="";
				    	
						thisDialog.refreshStore();
						var recordsLoaded= (thisDialog.jsonLineItems.length-1)-itemsNum;
						
						var msg = new DialogMessages();
						msg.addInfoMessages("Records Loaded: "+recordsLoaded);
						thisDialog.showMessage("Notice",msg.infoMessages(),false, null, null, true);
//						if(typeof callback === "function"){
//							callback();
//						}
				    	
//				    	this.jsonImportSerialNumberList	= JSON.parse(response.snImportJsonList);
//				    	this.importLineItems(this.jsonImportSerialNumberList);
//				    	this.numberAvailable.set('value',this.rowCount);
				    })	
			 	} );
			 
		},
		
		importSerial : function(){
			console.log("importSerial");
			debugger;
			thisDialog.jsonInvoiceObject=JSON.parse(thisDialog.serviceParameters["jsonInvoiceEls"]);
			var dialogMsgs=new DialogMessages();
			dialogMsgs.addErrorMessages("The invoice is configured, lines cannot be added.");

			if(thisDialog.jsonInvoiceObject.configuredInvIndc=="Y" || thisDialog.jsonInvoiceObject.configuredInvIndc=="y"){
				thisDialog.showMessage("Error",dialogMsgs.errorMessages(),false,null, null);
				return;
			}
			
			thisDialog.initValues();
			debugger;
			var file = this.importTS._files[0];

			var parm_part_filename = (file.fileName ? file.fileName : file.name)

			var formData = new FormData();
			formData.append('frontEndAction', 'importSerial');
			formData.append('file', file);
			formData.append('mimetype', file.type);
			formData.append('fileName', parm_part_filename); 
			var taxIndc="";
			if(thisDialog.jsonInvoiceObject.taxesIndc==""){
				taxIndc="N";
			}else{
				taxIndc=thisDialog.jsonInvoiceObject.taxesIndc;
			}
			formData.append('repositoryId',thisDialog.serviceParameters['repositoryId']);
			var usTax="";
			if(thisDialog.jsonInvoiceObject.usTaxPercent==null || thisDialog.jsonInvoiceObject.usTaxPercent==""){
				usTax=0;
			}else{
				usTax=thisDialog.jsonInvoiceObject.usTaxPercent;
			}
			formData.append('usTaxPercent',usTax);
			formData.append('usTaxIndc',taxIndc);
			formData.append('billedToIgfIndc',thisDialog.newLineItem.BILLEDTOIGFINDC);
			formData.append('country', thisDialog.serviceParameters['country']); 
//			formData.append('serviceParameters',thisDialog.serviceParameters);
			formData.append('request', arguments.callee.nom);
			if(thisDialog.taxCode.get('value').trim().length!=0){
				formData.append("vatCode",thisDialog.taxCode.get('value').trim())
			}else{
				formData.append("vatCode","");
			}
			var itemsNum=thisDialog.jsonLineItems.length-1;
			formData.append('jsonLineItems',JSON.stringify(thisDialog.jsonLineItems));		
			formData.append('invoiceNumber',thisDialog.jsonInvoiceObject.invoiceNumber);
			formData.append('invoiceDate',thisDialog.jsonInvoiceObject.invoiceDate);
			formData.append('vatAmount',thisDialog.jsonInvoiceObject.vatAmount);
			formData.append('provinceCode',thisDialog.jsonInvoiceObject.provinceCode);
			
			var pluginParams = {
//						
					    requestCompleteCallback: lang.hitch(this, function (response) {
					    	var dialogMessagesResponse = new DialogMessages();
					    	debugger;
					    	thisDialog.importTS.focusNode.value="";
					    	thisDialog.jsonLineItems=JSON.parse(response.listLineItems);
							thisDialog.refreshStore();
							var recordsLoaded= (thisDialog.jsonLineItems.length-1)-itemsNum;
							
							var msg = new DialogMessages();
							msg.addInfoMessages("Records Loaded: "+recordsLoaded);
							thisDialog.showMessage("Notice",msg.infoMessages(),false, null, null, true);
//							if(typeof callback === "function"){
//								callback();
//							}
					    	
//					    	this.jsonImportSerialNumberList	= JSON.parse(response.snImportJsonList);
//					    	this.importLineItems(this.jsonImportSerialNumberList);
//					    	this.numberAvailable.set('value',this.rowCount);
					    })
					}; 
			 Request.postFormToPluginService("GilCnPlugin", "LineItemELSService", formData, pluginParams);
		},
		
		
//		importLineItems: function(snList) {
//			
//			thisDialog.initValues();
//			
//			var data = {  items: [] };
//			var arr = [];
//			
//			var auxLnItem = null;
//			var auxArrOfLnItems= [];
//			
//			for (var i in snList) {
//				
//				thisDialog.newLineItem['QUANTITY']='1';
//				thisDialog.newLineItem['INVOICEITEMTYPE']='TM';
//				thisDialog.newLineItem['PARTNUMBER']='';
//				thisDialog.newLineItem['SUBLINENUMBER']='0';
//				
//				if(thisDialog.serviceParameters['country']=='US'){
//					
//					thisDialog.setUsTaxPercentage();
//					var n=Number(thisDialog.newLineItem.USTAXPERCENT.trim());
//					if(isNaN(n)  || n==0){
//						thisDialog.newLineItem["VATCODE"]=thisDialog.NO_TAX;
//					}else{
//						thisDialog.newLineItem["VATCODE"]=thisDialog.WITH_TAX;
//					}
//					
//				}
//				
//				this.rowCount++;
//				
//				
//				thisDialog.newLineItem['TYPE']=snList[i].TYPE;
//				thisDialog.newLineItem['MODEL']=snList[i].MODEL;
//				thisDialog.newLineItem['SERIAL']=snList[i].SERIALNUMBER;
//				thisDialog.newLineItem['UNITPRICE']=snList[i].UNITPRICE;
//				thisDialog.newLineItem['TERM']=snList[i].TERM;
//				thisDialog.recalculateVATAmount(thisDialog.newLineItem);
//				var copy = JSON.parse(JSON.stringify( thisDialog.newLineItem));
////				auxLnItem = lang.clone();
//	        	auxArrOfLnItems.push(copy);
////	        	auxLnItem = null;	
//				
//			}
//			
////			thisDialog.renumberLineItems(auxArrOfLnItems,
////					thisDialog.spreadTaxRoundingError.bind(null,
////							thisDialog.clearFields)); 
//			
//			
//			//sendthe list of itemsto backend to check selectTypeModelDescription and thenrenumber
//			thisDialog.jsonLineItems.push.apply(thisDialog.jsonLineItems,arrOfLineItems);
//			
//			this.serviceParameters['jsonLineItems']= JSON.stringify(thisDialog.jsonLineItems);
//			this.serviceParameters['request']=arguments.callee.nom;
//				
//			thisDialog.selectTypeModelDescription();
//				
//				
//				
//				
//			
////				if (this.serialNumberGrid!=null && this.serialNumberGrid.store!=null){
////					this.serialNumberGrid.store.newItem(newItem);
////				} else {
////					  this.storeSerialNumberGrid = new ItemFileWriteStore({data:data});
////					  this.storeSerialNumberGrid.newItem(newItem);
////				}
////			
//			
////		    this.serialNumberGrid.set("store", this.storeSerialNumberGrid);
////		    this.serialNumberGrid._lastScrollTop=this.serialNumberGrid.scrollTop;
////		    this.serialNumberGrid._refresh();
//
//		},
		selectTypeModelDescription: function(){//server
			
			thisDialog.serviceParameters['jsonLineItem']=JSON.stringify(thisDialog.newLineItem);
			
			this.serviceParameters['request']=arguments.callee.nom;

			ecm.messages.progress_message_LineItemELSService = "Processing...";			 
			Request.postPluginService("GilCnPlugin","LineItemELSService",'application/x-www-form-urlencoded',{
					requestBody : ioquery.objectToQuery(this.serviceParameters),	
					requestCompleteCallback: lang.hitch(this,function(response) {
						thisDialog.jsonLineItem=JSON.parse(response.currentLineItem);
						
//						thisDialog.refreshStore();
						if(typeof callback === "function"){
							callback();
						}
					})	
				});
		},
		setUsTaxPercentage: function(){
			debugger;
			thisDialog.jsonInvoiceObject=JSON.parse(thisDialog.serviceParameters["jsonInvoiceEls"]);
			if(thisDialog.jsonInvoiceObject.taxesIndc=="Y" &&
					thisDialog.newLineItem.BILLEDTOIGFINDC=='N'){
				thisDialog.newLineItem['USTAXPERCENT']="";
			}else{
				if(thisDialog.jsonInvoiceObject.usTaxPercent==null){
					thisDialog.newLineItem['USTAXPERCENT']="";
				}else{
					thisDialog.newLineItem['USTAXPERCENT']=thisDialog.jsonInvoiceObject.usTaxPercent;
				}
			
				
			}
		},
		 addItemforPart:function(){
			 
				thisDialog.initValues();
				thisDialog.newLineItem["QUANTITY"] = "1";
				thisDialog.newLineItem["TOTALVATAMOUNT"] = thisDialog.newLineItem["VATAMOUNT"];
				thisDialog.newLineItem["EXTENDEDPRICE"] = thisDialog.newLineItem["UNITPRICE"];
				if(thisDialog.serviceParameters['country']=='US'){
					thisDialog.setUsTaxPercentage();
					if(thisDialog.newLineItem.USTAXPERCENT==null){
						thisDialog.newLineItem["VATCODE"]=thisDialog.NO_TAX;
					}else if(isNaN(Number(thisDialog.newLineItem.USTAXPERCENT.trim()))  ||Number( thisDialog.newLineItem.USTAXPERCENT.trim())==0){
						thisDialog.newLineItem["VATCODE"]=thisDialog.NO_TAX;
					}else{
						thisDialog.newLineItem["VATCODE"]=thisDialog.WITH_TAX;
					}
					thisDialog.recalculateVATAmount(thisDialog.newLineItem);
				}
				
				var qty = thisDialog.qty.get('value');
				var zeroPrice = false;
				var matchingSerialCount = thisDialog.countMatchingSerials();
				var auxLnItem = null;
				var auxArrOfLnItems= [];
				
				var unitPrice = parseFloat(thisDialog.newLineItem["UNITPRICE"]);
				
				if(isNaN(unitPrice) || MathJs.compare(unitPrice,0)!=0) {
					zeroPrice = true;
				}
			    // if the quantity is zero, that means copy in all matching type / model
			    // / $ matching records
			    if (qty == 0){
			        qty = matchingSerialCount;
			    }
			    
		        for (var i = 0; i < qty; i++)  {
		        	var copy = JSON.parse(JSON.stringify( thisDialog.newLineItem));
//		        	auxLnItem = lang.clone(thisDialog.newLineItem);
		        	auxArrOfLnItems.push(copy);
//		        	auxLnItem = null;	
		        }
		        
//				thisDialog.renumberLineItems(auxArrOfLnItems,
//						thisDialog.spreadTaxRoundingError.bind(null,
//								thisDialog.clearFields)); 
				thisDialog.jsonLineItems.push.apply(thisDialog.jsonLineItems,auxArrOfLnItems); 
				thisDialog.renumberAndSpread(thisDialog.clearFields);
				
		 },
	 
		 addItemSerials:function(){
			 debugger;
//				thisDialog.initValues();
				thisDialog.newLineItem["CONFIGUSED"]="N";
				thisDialog.newLineItem["QUANTITY"] = "1";
				thisDialog.newLineItem["TOTALVATAMOUNT"] = thisDialog.newLineItem["VATAMOUNT"];
				thisDialog.newLineItem["EXTENDEDPRICE"] = thisDialog.newLineItem["UNITPRICE"];
				if(thisDialog.serviceParameters['country']=='US'){
					thisDialog.setUsTaxPercentage();
					
					if(thisDialog.newLineItem.USTAXPERCENT==null){
						thisDialog.newLineItem["VATCODE"]=thisDialog.NO_TAX;
					}else if(isNaN(Number(thisDialog.newLineItem.USTAXPERCENT.trim()))  ||Number( thisDialog.newLineItem.USTAXPERCENT.trim())==0){
						thisDialog.newLineItem["VATCODE"]=thisDialog.NO_TAX;
					}else{
						thisDialog.newLineItem["VATCODE"]=thisDialog.WITH_TAX;
					}
					thisDialog.recalculateVATAmount(thisDialog.newLineItem);
//					thisDialog.newLineItem["VATCODE"]=thisDialog.NO_TAX;
				}
				
				var qty = thisDialog.qty.get('value');
				var zeroPrice = false;
				var matchingSerialCount = thisDialog.countMatchingSerials();
				var auxLnItem = null;
				var auxArrOfLnItems= [];
				
				var unitPrice = parseFloat(thisDialog.newLineItem["UNITPRICE"]);
				
				if(isNaN(unitPrice) || MathJs.compare(unitPrice,0)!=0) {
					zeroPrice = true;
				}
			    // if the quantity is zero, that means copy in all matching type / model
			    // / $ matching records
			    if (qty == 0){
			        qty = matchingSerialCount;
			    }
			    
		        for (var i = 0; i < qty; i++)  {
		        	auxLnItem = lang.clone(thisDialog.newLineItem);
		        	debugger;
		        	 if ((matchingSerialCount > 0) && (auxLnItem.INVOICEITEMTYPE == thisDialog.TYPE_MODEL )) {
		        		 var serial = thisDialog.getMatchingSerial();
		        		 if (serial != null){

		        			 auxLnItem["SERIAL"] = serial.SERIALNUMBER.trim();
		        			 console.log("serial value added:"+ auxLnItem.SERIAL+"--");
		                     matchingSerialCount--;
		                     if (zeroPrice) {
		                    	 auxLnItem["UNITPRICE"] = serial.UNITPRICE;
		                         thisDialog.recalculateVATAmount(auxLnItem);
		                     }
		        		 }
		        	 }
		        	auxArrOfLnItems.push(auxLnItem);
		        	auxLnItem = null;	
		        }
		        thisDialog.jsonLineItems.push.apply(thisDialog.jsonLineItems,auxArrOfLnItems);
		        
//		        thisDialog.renumberAndSpread(thisDialog.clearFields);
			
//				thisDialog.renumberLineItems(auxArrOfLnItems,
//						thisDialog.spreadTaxRoundingError.bind(null,
//								thisDialog.clearFields));
				thisDialog.jsonSerialNumbers=null;
		 },

		
		renumberLineItems:function(arrOfLineItems,callback){
debugger;
			thisDialog.jsonLineItems.push.apply(thisDialog.jsonLineItems,arrOfLineItems); 
			this.serviceParameters['jsonLineItems']= JSON.stringify(thisDialog.jsonLineItems);	
			this.serviceParameters['request']=arguments.callee.nom;

			ecm.messages.progress_message_LineItemELSService = "Processing...";			 
			Request.postPluginService("GilCnPlugin","LineItemELSService",'application/x-www-form-urlencoded',{
					requestBody : ioquery.objectToQuery(this.serviceParameters),	
					requestCompleteCallback: lang.hitch(this,function(response) {
						thisDialog.jsonLineItems=JSON.parse(response.listLineItems);
						thisDialog.refreshStore();
						if(typeof callback === "function"){
							callback();
						}
					})	
				});
			
		},
//		renumberLineItemsSave:function(){
//			this.serviceParameters['jsonLineItems']= JSON.stringify(thisDialog.jsonLineItems);	
//			this.serviceParameters['request']="renumberLineItems";
//
//			ecm.messages.progress_message_LineItemELSService = "Processing...";			 
//			Request.postPluginService("GilCnPlugin","LineItemELSService",'application/x-www-form-urlencoded',{
//					requestBody : ioquery.objectToQuery(this.serviceParameters),	
//					requestCompleteCallback: lang.hitch(this,function(response) {
//						thisDialog.jsonLineItems=JSON.parse(response.listLineItems);						
//
//						jsonInvoiceEls.lineItems=thisDialog.jsonLineItems;
//						thisDialog.recalculateUnitAmounts();
//						thisDialog.enablePONumberBasedOnUnits();
//						thisDialog.refreshStore();
//						
//					})	
//				});
//
//		},
		
		refreshTable:function(){
			
		},
		
		add:function(){
			thisDialog.grid.store.newItem({                
				'LINENUMBER':thisDialog.newLineItem.LINENUMBER,
				'TYPE': thisDialog.newLineItem.TYPE,
				'MODEL':thisDialog.newLineItem.MODEL,	
				'SERIAL': thisDialog.newLineItem.SERIAL,		
				'QUANTITY':thisDialog.newLineItem.QUANTITY,
				'PARTNUMBER': thisDialog.newLineItem.PARTNUMBER,	
				'DESCRIPTION': thisDialog.newLineItem.DESCRIPTION,	
				'UNITPRICE':thisDialog.newLineItem.UNITPRICE,	
				'VATAMOUNT': thisDialog.newLineItem.VATAMOUNT,
				'EXTENDEDPRICE':thisDialog.newLineItem.EXTENDEDPRICE,
				'TOTALVATAMOUNT':thisDialog.newLineItem.TOTALVATAMOUNT,
				'MESNUMBER':thisDialog.newLineItem.MESNUMBER,
				'VATCODE': 	thisDialog.newLineItem.VATCODE,
				'SUBLINENUMBER':thisDialog.newLineItem.SUBLINENUMBER,
				'CONFIGUSED': thisDialog.newLineItem.CONFIGUSED,
			});
			thisDialog.grid.store.save();
		},
		
		copyLnItem:function(){
			
			thisDialog.jsonLineItem=null;
			thisDialog.jsonLineItem={DOCUMENTMODE:thisDialog.newLineItem.DOCUMENTMODE};
			thisDialog.jsonLineItem['BILLEDTOIGFINDC']=thisDialog.newLineItem.BILLEDTOIGFINDC;
			thisDialog.jsonLineItem['BLANKTYPEMODEL']=thisDialog.newLineItem.BLANKTYPEMODEL;
			thisDialog.jsonLineItem['CONFIGUSED']=thisDialog.newLineItem.CONFIGUSED;
			thisDialog.jsonLineItem['COSTCENTER']=thisDialog.newLineItem.COSTCENTER;
			thisDialog.jsonLineItem['DESCRIPTION']=thisDialog.newLineItem.DESCRIPTION;
			thisDialog.jsonLineItem['EQUIPSOURCE']=thisDialog.newLineItem.EQUIPSOURCE;
			thisDialog.jsonLineItem['EXTENDEDPRICE']=thisDialog.newLineItem.EXTENDEDPRICE;
			thisDialog.jsonLineItem['EXTENSIONIND']=thisDialog.newLineItem.EXTENSIONIND;
			thisDialog.jsonLineItem['FINANCINGTYPE']=thisDialog.newLineItem.FINANCINGTYPE;
			thisDialog.jsonLineItem['FROMQUOTE']=thisDialog.newLineItem.FROMQUOTE;
			thisDialog.jsonLineItem['INVOICEITEMTYPE']=thisDialog.newLineItem.INVOICEITEMTYPE;
			thisDialog.jsonLineItem['LINENUMBER']=thisDialog.newLineItem.LINENUMBER;
			thisDialog.jsonLineItem['MAXUNITPRICE']=thisDialog.newLineItem.MAXUNITPRICE;
			thisDialog.jsonLineItem['MESINDICATOR']=thisDialog.newLineItem.MESINDICATOR;
			thisDialog.jsonLineItem['MESNUMBER']=thisDialog.newLineItem.MESNUMBER;
			thisDialog.jsonLineItem['MINUNITPRICE']=thisDialog.newLineItem.MINUNITPRICE;
			thisDialog.jsonLineItem['MODEL']=thisDialog.newLineItem.MODEL;
			thisDialog.jsonLineItem['OTHERCHARGE']=thisDialog.newLineItem.OTHERCHARGE;
			thisDialog.jsonLineItem['PARTNUMBER']=thisDialog.newLineItem.PARTNUMBER;
			thisDialog.jsonLineItem['PONUMBER']=thisDialog.newLineItem.PONUMBER;
			thisDialog.jsonLineItem['PRODCAT']=thisDialog.newLineItem.PRODCAT;
			thisDialog.jsonLineItem['QUANTITY']=thisDialog.newLineItem.QUANTITY;
			thisDialog.jsonLineItem['SERIAL']=thisDialog.newLineItem.SERIAL;
			thisDialog.jsonLineItem['SUBLINENUMBER']=thisDialog.newLineItem.SUBLINENUMBER;
			thisDialog.jsonLineItem['TAXESINDICATOR']=thisDialog.newLineItem.TAXESINDICATOR;
			thisDialog.jsonLineItem['TERM']=thisDialog.newLineItem.TERM;
			thisDialog.jsonLineItem['TOTALVATAMOUNT']=thisDialog.newLineItem.TOTALVATAMOUNT;
			thisDialog.jsonLineItem['TRANSACTIONTYPE']=thisDialog.newLineItem.TRANSACTIONTYPE;
			thisDialog.jsonLineItem['TYPE']=thisDialog.newLineItem.TYPE;
			thisDialog.jsonLineItem['UNITPRICE']=thisDialog.newLineItem.UNITPRICE;
			thisDialog.jsonLineItem['USTAXPERCENT']=thisDialog.newLineItem.USTAXPERCENT;
			thisDialog.jsonLineItem['VATAMOUNT']=thisDialog.newLineItem.VATAMOUNT;
			thisDialog.jsonLineItem['VATCODE']=thisDialog.newLineItem.VATCODE;
	
		},
		
		validateInput: function(lineItem,callback){
			debugger;
			var validate=true;
			thisDialog.msg=new DialogMessages();	
			if(lineItem.CONFIGUSED =="Y"){
				thisDialog.msg.addErrorMessages("Item has been configured and can not be changed.");
				
				thisDialog.showMessage("Error",thisDialog.msg.errorMessages(),false, null, null);
				return false;
				
			}
			
			var items = this.grid.selection.getSelected();
			thisDialog.lineItemsGrid;
			if(!(items.length>1 && lineItem.DOCUMENTMODE == 3) &&
					lineItem.INVOICEITEMTYPE == thisDialog.PARTNUMBER){
				if(lineItem.PARTNUMBER.trim().length==0){
					thisDialog.msg.addErrorMessages("Part Number is a required field.");
					
					thisDialog.showMessage("Error",thisDialog.msg.errorMessages(),false, null, null);
					return false;
				}
			}
			if(!(items.length>1 && lineItem.DOCUMENTMODE == 3) &&
					lineItem.INVOICEITEMTYPE == thisDialog.OTHER_CHARGE){
				if(lineItem.OTHERCHARGE.trim().length==0){
					thisDialog.msg.addErrorMessages("Other Charge code is a required field.");
					
					thisDialog.showMessage("Error",thisDialog.msg.errorMessages(),false, null, null);
					return false;
				}
				
			}
			var qty=lineItem.QUANTITY;
			if(lineItem.INVOICEITEMTYPE == thisDialog.TYPE_MODEL){
				if(qty<0){
					thisDialog.msg.addErrorMessages("Quantity must be >= 0.");
					
					thisDialog.showMessage("Error",thisDialog.msg.errorMessages(),false, null, null);
					return false;
				}
			}else{
				if(lineItem.DOCUMENTMODE == 3 && items.length>1 && qty<0){
					thisDialog.msg.addErrorMessages("Quantity must be > 0.");
					
					thisDialog.showMessage("Error",thisDialog.msg.errorMessages(),false, null, null);
					return false;
				}
			}
			
			 //story-1588866 restrict the line item quantity to be less than or 
			if(qty>250){
				thisDialog.msg.addErrorMessages("Exceed maximum quantity.");
				
				thisDialog.showMessage("Error",thisDialog.msg.errorMessages(),false, null, null);
				return false;
			}
			if((this.serviceParameters['country']!='US')){
				if(lineItem.DOCUMENTMODE == 1 && lineItem.VATCODE.trim().length == 0){
					thisDialog.msg.addErrorMessages("Tax Code Required.");					
					thisDialog.showMessage("Error",thisDialog.msg.errorMessages(),false, null, null);
					return false;
				}
			}
			
			
			var unitPrice = parseFloat(lineItem.UNITPRICE);
			if(lineItem.INVOICEITEMTYPE == thisDialog.TYPE_MODEL){
				if(isNaN(unitPrice) || MathJs.compare(unitPrice,0)<0) {
					thisDialog.msg.addErrorMessages("Unit Price must be >= 0.00");
					thisDialog.showMessage("Error",thisDialog.msg.errorMessages(),false, null, null);
					return false;
				}
			} else {
				if(isNaN(unitPrice) || MathJs.compare(unitPrice,0)<=0) {
					thisDialog.msg.addErrorMessages("Unit Price must be > 0.00");
					thisDialog.showMessage("Error",thisDialog.msg.errorMessages(),false, null, null);
					return false;
				}
			}
			
			var lineNumber=lineItem.LINENUMBER;
			var otherLnItem=null;
			var otherLnNumber=null;
			if(lineNumber !=0){
				var items=thisDialog.jsonLineItems;//thisDialog.grid.store._arrayOfAllItems;
				for(var i in items ){
					otherLnItem=items[i];
					otherLnNumber=otherLnItem.LINENUMBER;
					if(lineNumber == otherLnNumber){
						if(!thisDialog.isMatchinLineNumberItem(lineItem,otherLnItem)){
							//thisDialog.jsonLineItem
							lineItem['LINENUMBER']="0";
							lineItem['SUBLINENUMBER']="0";
							break;
						}
					}
				}
			}
			
			
			var maxprice = parseFloat(lineItem.MAXUNITPRICE);
			var minprice = parseFloat(lineItem.MINUNITPRICE);
			
			if(!isNaN(maxprice) &&  MathJs.compare(maxprice,0) != 0){
				if(MathJs.compare(unitPrice,maxprice) > 0) {
					thisDialog.msg.addWarningMessages("Unit Price exceeds maximum allowable value of " + maxprice);
					thisDialog.showMessage("Error",thisDialog.msg.warningMessages(),false, null, null);
				}
				if(MathJs.compare(unitPrice,minprice) < 0) {
					thisDialog.msg.addWarningMessages("Unit Price is less than minimum allowable value of " + minprice);
					thisDialog.showMessage("Error",thisDialog.msg.warningMessages(),false, null, null);
				}
				
			}
			
			if(typeof callback === "function"){
				callback();
			}
			
			return true;
			
		},
		
		//validateOnServer is now done in ValidateInput only 
		validateOnServer:function(item, callback){
		
			this.serviceParameters['request']=arguments.callee.nom;
			this.serviceParameters['jsonLineItem']= JSON.stringify(item);
			ecm.messages.progress_message_LineItemELSService = "Validating...";				 
			
			Request.postPluginService("GilCnPlugin","LineItemELSService",'application/x-www-form-urlencoded',{	
					requestBody : ioquery.objectToQuery(this.serviceParameters),
					requestCompleteCallback: lang.hitch(this,function(response) {
						
						thisDialog.jsonLineItem=JSON.parse(response.currentLineItem);
						if(thisDialog.scanResponseLineItem()){
							callback();
							return true;
						}	else{
							return false;
						}
								
					})	
				});

		},
		
		scanResponseLineItem: function(){
			
			var lineItem=thisDialog.jsonLineItem;
			if(lineItem.dialogErrors.length>0){
					var msgs=lineItem.dialogErrors;
					for (var i in msgs) {
						thisDialog.msg.addErrorMessages(msgs[i]);
					}
			}
			
			if(thisDialog.msg.hasErrorMessages()){
					
					thisDialog.showMessage("Error",thisDialog.msg.errorMessages(),false, null, null);
					return false;
			}else{//continue with logic
//				thisDialog.jsonLineItems=thisDialog.jsonLineItemGeneral.lineItems;
				
				if(lineItem.dialogWarns.length>0){
					var msgs=lineItem.dialogWarns;
					for (var i in msgs) {
						thisDialog.msg.addWarningMessages(msgs[i]);
					}
				}
				if(thisDialog.msg.hasWarningMessages()){
					thisDialog.showMessage("Warning",thisDialog.msg.warningMessages(),false, null, null);
					return false;
				}else{
					
					thisDialog.se
					return true;
				}
				
			}	
		},
		
		isMatchinLineNumberItem: function(lineItem,otherLnItem){
			
			if(lineItem.INVOICEITEMTYPE==thisDialog.PARTNUMBER){
				if(lineItem.PARTNUMBER != otherLnItem.PARTNUMBER){
					return false;
				}
				if(lineItem.VATCODE != otherLnItem.VATCODE){
					return false;
				}
				if(lineItem.UNITPRICE != otherLnItem.UNITPRICE){
					return false;
				}
				if(lineItem.BILLEDTOIGFINDC != otherLnItem.BILLEDTOIGFINDC){
					return false;
				}
			}else{
				if(lineItem.TYPE != otherLnItem.TYPE){
					return false;
				}
				if(lineItem.MODEL != otherLnItem.MODEL){
					return false;
				}
				if(lineItem.VATCODE != otherLnItem.VATCODE){
					return false;
				}
				if(lineItem.UNITPRICE != otherLnItem.UNITPRICE){
					return false;
				}
				if(lineItem.BILLEDTOIGFINDC != otherLnItem.BILLEDTOIGFINDC){
					return false;
				}
				
			}
			return true;
		},
		
		initItem:function(docMode){
			
			thisDialog.newLineItem=null;
			thisDialog.newLineItem={COUNTRY:thisDialog.serviceParameters['country'],DOCUMENTMODE:docMode};//ADDMODE
		},
		
		initNewLnItem:function(){
			
			thisDialog.newLineItem["LINENUMBER"]="0";
			thisDialog.newLineItem["SUBLINENUMBER"]="0";
			thisDialog.newLineItem["QUANTITY"]="1";
			thisDialog.newLineItem["CONFIGUSED"]="N";
			thisDialog.newLineItem["FROMQUOTE"]="N";
			thisDialog.newLineItem["BLANKTYPEMODEL"]="N";
			thisDialog.newLineItem["BILLEDTOIGFINDC"]="N";
			thisDialog.newLineItem["UNITPRICE"]="0.00";
			thisDialog.newLineItem["VATAMOUNT"]="0.00";
			thisDialog.newLineItem["EXTENDEDPRICE"]="0.00";
			thisDialog.newLineItem["TOTALVATAMOUNT"]="0.00";
			
			thisDialog.newLineItem['COSTCENTER']="";
			thisDialog.newLineItem['DESCRIPTION']="";
			thisDialog.newLineItem['EQUIPSOURCE']="";
			thisDialog.newLineItem['EXTENSIONIND']="N";
			thisDialog.newLineItem['FINANCINGTYPE']="";
			thisDialog.newLineItem['INVOICEITEMTYPE']="";
			thisDialog.newLineItem['MAXUNITPRICE']="0.00";
			thisDialog.newLineItem['MESINDICATOR']="N";
			thisDialog.newLineItem['MESNUMBER']="";
			thisDialog.newLineItem['MINUNITPRICE']="0.00";
			thisDialog.newLineItem['MODEL']="";
			thisDialog.newLineItem['OTHERCHARGE']="";
			thisDialog.newLineItem['PARTNUMBER']="";
			thisDialog.newLineItem['PONUMBER']="";
			thisDialog.newLineItem['PRODCAT']="";
			thisDialog.newLineItem['SERIAL']="";
			thisDialog.newLineItem['TAXESINDICATOR']="N";
			thisDialog.newLineItem['TERM']="";
			thisDialog.newLineItem['TRANSACTIONTYPE']="";
			thisDialog.newLineItem['TYPE']="";
			thisDialog.newLineItem['USTAXPERCENT']="";
			thisDialog.newLineItem['VATCODE']="";

						
			
		},
		
		typeModelEnable:function(){
			
			var disable=true;
			
			//disabling these fields
			thisDialog.partNum.set('disabled',disable);
			thisDialog.charge.set('disabled',disable);
//			thisDialog.taxCode.set('disabled',disable);
			thisDialog.taxAmtLn.set('disabled',disable);
			thisDialog.costCenter.set('disabled',disable);
			
			thisDialog.totTaxAmt.set('disabled',disable);
			thisDialog.extLnPr.set('disabled',disable);
			//ensuring these fields are enabled
			disable=false;
			thisDialog.desc.set('disabled',disable);//Test Defect 1835603: CM8.5-ELS invoice- Not able to update the description on type/model or other charge
			thisDialog.type.set('disabled',disable);
			thisDialog.model.set('disabled',disable);
			thisDialog.oemButton.set('disabled',disable);
//			thisDialog.invLnNum.set('disabled',disable);
//			thisDialog.invLnNum2.set('disabled',disable);
			thisDialog.serial.set('disabled',disable);			
//			thisDialog.mes.set('disabled',disable); always it's enable
//			thisDialog.term.set('disabled',disable); always it's enable
//			thisDialog.unitPrice.set('disabled',disable); always it's enable
//			thisDialog.qty.set('disabled',disable); always it's enable
//			thisDialog.mesNum.set('disabled',disable);always it's enable
//			thisDialog.finType.set('disabled',disable);always it's enable
//			thisDialog.poNum.set('disabled',disable);always it's enable
			thisDialog.clearFields();
		},
		
		partEnable:function(disable){
			
			var disable=true;
			//disabling these fields
			thisDialog.type.set('disabled',disable);
			thisDialog.model.set('disabled',disable);
			thisDialog.oemButton.set('disabled',disable);
			thisDialog.charge.set('disabled',disable);
			thisDialog.serial.set('disabled',disable);
			thisDialog.taxAmtLn.set('disabled',disable);
			thisDialog.costCenter.set('disabled',disable);
			thisDialog.totTaxAmt.set('disabled',disable);
			thisDialog.extLnPr.set('disabled',disable);
			//ensuring these fields are enabled
			disable=false;
			thisDialog.partNum.set('disabled',disable);
			thisDialog.desc.set('disabled',disable);			
			thisDialog.clearFields();
		},
		
		otherChargeEnable:function(){
			
			var disable=true;
			
			//disabling these fields
			thisDialog.type.set('disabled',disable);
			thisDialog.model.set('disabled',disable);
			thisDialog.oemButton.set('disabled',disable);
			thisDialog.partNum.set('disabled',disable);
			thisDialog.serial.set('disabled',disable);	
			thisDialog.taxAmtLn.set('disabled',disable);
			thisDialog.costCenter.set('disabled',disable);
			
			thisDialog.totTaxAmt.set('disabled',disable);
			thisDialog.extLnPr.set('disabled',disable);
			//ensuring these fields are enabled
			disable=false;
//			Test Defect 1835603: CM8.5-ELS invoice- Not able to update the description on type/model or other charge
			thisDialog.desc.set('disabled',disable);
			thisDialog.charge.set('disabled',disable);
			thisDialog.clearFields();
		},
		
	
		deleteLnItem:function(){
			thisDialog.jsonInvoiceObject=JSON.parse(thisDialog.serviceParameters["jsonInvoiceEls"]);
			var dialogMsgs=new DialogMessages();
			dialogMsgs.addErrorMessages("The selected line numbers are configured and cannot be updated.");
			
				
			if(thisDialog.areSiblingsConfigured()){
				thisDialog.showMessage("Error",dialogMsgs.errorMessages(),false,null, null);
				return;
			}
			
			var itemsSelected = thisDialog.grid.selection.getSelected();
			if(itemsSelected == null || itemsSelected.length==0){
				thisDialog.clearItem();
			}else{
				
		        if(itemsSelected.length){
		            // Iterate through the list of selected items.
		          
		            dojo.forEach(itemsSelected, function(selectedItem){
		                if(selectedItem !== null){
		                	thisDialog.grid.store.deleteItem(selectedItem);
//		                	thisDialog.grid._refresh(); removing causing issues
		                	
		                } 
		            }); // end forEach
		        } 
		        thisDialog.grid.selection.deselectAll();
//		        thisDialog.grid.render(); removing causing issues 
//		        Defect 1837313: CM8.5-ELS Invoice- Line items- other charge field
		        thisDialog.clearFields();
		        thisDialog.spreadTaxRoundingError();
			}
		},
		
		spreadTaxRoundingError:function(callback){
			
			thisDialog.msg=new DialogMessages();
			thisDialog.loadCurrentList();
			thisDialog.serviceParameters['request']=arguments.callee.nom;
			thisDialog.serviceParameters['jsonLineItems']=JSON.stringify(thisDialog.jsonLineItems);
			ecm.messages.progress_message_LineItemELSService = "Processing...";

			Request.postPluginService("GilCnPlugin","LineItemELSService",'application/x-www-form-urlencoded',{
				requestBody : ioquery.objectToQuery(thisDialog.serviceParameters),
				requestCompleteCallback: lang.hitch(this,function(response) {
					
						thisDialog.jsonLineItems=JSON.parse(response.listLineItems);
						thisDialog.refreshStore();
						if(typeof callback === "function"){
							callback();
						}
							
				})	
			});	
		},
		
		clearItem:function(){
			
			thisDialog.initValues();
			thisDialog.jsonInvoiceObject=JSON.parse(thisDialog.serviceParameters["jsonInvoiceEls"]);
			thisDialog.jsonLineItemGeneral['PONUMBER']=thisDialog.jsonInvoiceObject.PONUMBER;
			thisDialog.jsonLineItemGeneral['COSTCENTER']=thisDialog.jsonInvoiceObject.COSTCENTER;
			thisDialog.clearFields();
			thisDialog.grid.selection.deselectAll();
			thisDialog.grid.selection.clear();
		},
		
		clearFields:function(){

			thisDialog.invLnNum.set('value',"0");
			thisDialog.invLnNum2.set('value',"0");
			thisDialog.qty.set('value',"1");
			thisDialog.invLnNum.set('value',"0");
			thisDialog.lnBillIgf.set("checked",false);
			thisDialog.mes.set("checked",false);
			thisDialog.unitPrice.set('value',"0.00");
			thisDialog.taxAmtLn.set('value',"0.00");
			thisDialog.extLnPr.set('value',"0.00");
			thisDialog.totTaxAmt.set('value',"0.00");
			thisDialog.poNum.set("value",jsonInvoiceEls.purchaseOrderNum); 
			thisDialog.type.set("value","");
			thisDialog.model.set("value","");
			thisDialog.partNum.set("value","");
			thisDialog.serial.set("value","");
			thisDialog.term.set("value","");
//			thisDialog.costCenter.set("value","");
			thisDialog.mesNum.set("value","");
			thisDialog.desc.set("value","");
			//Defect 1837313: CM8.5-ELS Invoice- Line items- other charge field
			//clean combos ----
			
			thisDialog.charge.set("value","");
			thisDialog.finType.set("value","");
			//--------
			if(thisDialog.jsonLineItemGeneral!=null){
			if(thisDialog.serviceParameters["country"]=="US"){
				thisDialog.jsonLineItemGeneral['country']="US";
			}else{
				var jsonUtils = new JsonUtils();
				thisDialog.taxCode.set("value", jsonUtils.getValueIfOtherKeyExists(thisDialog.jsonLineItemGeneral.vatCodes,"value", true));	
			}
			}	
		},
		clearLnItem:function(){
			
			thisDialog.clearItem();
			//need to clear selection
			
			
		},
	
	
		
		refreshStore:function(){
			
			thisDialog.lineItemsGrid={
					identifier: "id",
					items:[]
			};
			for(var i=0;i<thisDialog.jsonLineItems.length;i++ ){
				thisDialog.lineItemsGrid.items.push(lang.mixin({id:i+1},thisDialog.jsonLineItems[i%thisDialog.jsonLineItems.length]));
			}
		
			
			var store=new ItemFileWriteStore({data:thisDialog.lineItemsGrid});
			store.data=thisDialog.lineItemsGrid;
			thisDialog.grid.setStore(store);
			
			/*** Removing 3 next lines were causing issues in delete function, 
			evaluate on next months if we need to uncomment anything
			*/
//			thisDialog.grid.store.fetch();
//			thisDialog.grid._refresh();
//			thisDialog.grid.render();
		},
		createOtherCharges:function(){
			var otherChargesPairs=[];
			var selectedVal="";
			for(var i=0;i<thisDialog.jsonLineItemGeneral.otherCharges.length; i++ ){
				if(thisDialog.jsonLineItemGeneral.otherCharges[i].selected)
					selectVal=thisDialog.jsonLineItemGeneral.otherCharges[i].value;
				otherChargesPairs[i]= {name:thisDialog.jsonLineItemGeneral.otherCharges[i].label, id:thisDialog.jsonLineItemGeneral.otherCharges[i].value};
			}
			var oCStore = new Memory({
				data:otherChargesPairs, 
			});
			
			thisDialog.charge=new dijit.form.FilteringSelect({
		        id:thisDialog.id+ "_otherChargesSelect",
		        store:oCStore,
		        required: false,
		        autoComplete: true,
		        style: "width: 352px;",
		        onChange: function(oC){
		        	console.log(oC);
		        	var value = thisDialog.charge.get('value'); 
			    	var descValue = thisDialog.charge.get('displayedValue').trim();
			        thisDialog.desc.set('value',descValue.substring(descValue.indexOf(" - ") +3,descValue.length));
			 	    thisDialog.type.set('value',value.substring(0,value.indexOf("/")));
			 	    thisDialog.model.set('value',value.substring(value.indexOf("/")+1,value.length));
//		        	
//		        	
//		        	
//		            dijit.byId('city').query.state = this.item.state || /.*/;
		         },		        
		    }, thisDialog.id+ "_otherChargesSelect");
			
			construct.place(thisDialog.charge.domNode, thisDialog.id+"_charge","replace");
			thisDialog.charge.startup();
			thisDialog.charge.set("value",selectedVal);
			 
		},
		showLineItemsGrid:function(){

			thisDialog.lineItemsGrid={
					identifier: "id",
					items:[]
			};
			for(var i=0;i<thisDialog.jsonLineItems.length;i++ ){
				thisDialog.lineItemsGrid.items.push(lang.mixin({id:i+1},thisDialog.jsonLineItems[i%thisDialog.jsonLineItems.length]));
			}
			var store=new ItemFileWriteStore({data: thisDialog.lineItemsGrid});
			store.data=thisDialog.lineItemsGrid;
			
			var layout=[[
			   {'name': 'Line #',		'field':	'LINENUMBER',	'width':	'50px'},
			   {'name': 'Type',			'field':	'TYPE', 		'width':	'60px'},
			   {'name': 'Model',		'field':	'MODEL',		'width':	'60px'},
			   {'name': 'Serial',		'field':	'SERIAL', 		'width':	'70px'},
			   {'name': 'Quantity',		'field':	'QUANTITY',		'width':	'60px'},
			   {'name': 'Part #',		'field':	'PARTNUMBER', 	'width':	'60px'},
			   {'name': 'Description',	'field':	'DESCRIPTION', 	'width':	'75px'},
			   {'name': 'Unit Price',	'field':	'UNITPRICE', 	'width':	'60px'},
			   {'name': 'Tax',			'field':	'VATAMOUNT', 	'width':	'60px'},
			   {'name': 'Total Price',	'field':	'EXTENDEDPRICE','width':	'60px'},
			   {'name': 'Total Tax',	'field':	'TOTALVATAMOUNT','width':	'60px'},
			   {'name': 'MES Number',	'field':	'MESNUMBER', 	 'width':	'75px'},
			   {'name': 'Tax Code',		'field':	'VATCODE', 	     'width':	'60px'},
			   {'name': 'Sub Line Number','field':	'SUBLINENUMBER', 'width':	'75px'},
			   {'name': 'Used',			'field':	'CONFIGUSED', 	 'width':	'50px'},			
			]];
			thisDialog.grid = new DataGrid({
				store:store,
				 selectionMode:'extended', //using ctrl+click in rows to select more than one, or just one click to select a lonely item
				 structure: layout,
				 columnReordering: true,
//				 style:"width:95%; height: 95%;",
				 rowSelector: '1px',
				 query: { id: "*" },
			});
			
//			domStyle.set(this.id + '_lineItems', 'width', '96%');
			domStyle.set(this.id + '_lineItems', 'height', '190px');
						
			thisDialog.grid.placeAt(thisDialog.id+'_lineItems');
			thisDialog.grid.on("RowClick", function(e){
				//in case they don't like to use ctrl+clicking lines

				 thisDialog.fillFields( e.grid.getItem(e.rowIndex));
	    	    }, true);
			thisDialog.grid.startup();	
			domClass.add(dijit.byId(thisDialog.grid).domNode, 'itemsGridR');
	    	thisDialog.show();
	    	var cA=thisDialog.contentArea;
	    	
	    	domStyle.set(cA, "overflow",'auto');
		},
	
		setLayout: function(){			
			//DataGrid
			var restoreGrid = (foo) => {
				if(foo != null){
					thisDialog.grid.resize();
					var th = thisDialog.grid.viewsHeaderNode.children[1].children[0].children[0]
						.children[0].children[0].children[0].children[0];
					if(!domClass.contains(th,'linesTableHeader'))
						domClass.add(th, 'linesTableHeader');
					var dojoGridScrollBoxx=thisDialog.grid.viewsNode.children[0].children[2];
					if(!domClass.contains(dojoGridScrollBoxx,"gridScrollBoxC"))				
						domClass.add(dojoGridScrollBoxx,"gridScrollBoxC");
					if(!domClass.contains(dijit.byId(thisDialog.grid).domNode,'itemsGridR')){
						domClass.add(dijit.byId(thisDialog.grid).domNode, 'itemsGridR');
						domClass.remove(dijit.byId(thisDialog.grid).domNode, 'itemsGridM');
						}
					var lIG= dojo.byId(thisDialog.id+"_lineItems");
					if(!domClass.contains(lIG,'lineItemsGridR') && domClass.contains(lIG,'lineItemsGrid') ){
						domClass.add(lIG, 'lineItemsGridR');
						domClass.remove(lIG, 'lineItemsGrid');
					}else if(!domClass.contains(lIG,'lineItemsGridR') && domClass.contains(lIG,'lineItemsGridM') ){
						domClass.add(lIG, 'lineItemsGridR');
						domClass.remove(lIG, 'lineItemsGridM');
						
					}
					if(!domclass.contains(this.actionBar,"lineItemActionBarR")){			
						domclass.add(this.actionBar,"lineItemActionBarR");
						domclass.remove(this.actionBar, 'lineItemActionBarM');
					}
					domStyle.set(this.id + '_lineItems', 'height', '190px');//205
					thisDialog.grid.resize();
					thisDialog.grid.render();
				}
			};
			//DataGrid
			var maximizeGrid = (foo) => {
				if(foo != null){
					
					var th = thisDialog.grid.viewsHeaderNode.children[1].children[0].children[0]
						.children[0].children[0].children[0].children[0];
					if(!domClass.contains(th,'linesTableHeader'))
						domClass.add(th, 'linesTableHeader');
					var dojoGridScrollBoxx=thisDialog.grid.viewsNode.children[0].children[2];
					if(!domClass.contains(dojoGridScrollBoxx,"gridScrollBoxC"))				
						domClass.add(dojoGridScrollBoxx,"gridScrollBoxC");
					if(!domClass.contains(dijit.byId(thisDialog.grid).domNode,'itemsGridM')){
						domClass.add(dijit.byId(thisDialog.grid).domNode, 'itemsGridM');
						domClass.remove(dijit.byId(thisDialog.grid).domNode, 'itemsGridR');
					}
					var lIG= dojo.byId(thisDialog.id+"_lineItems");
					if(!domClass.contains(lIG,'lineItemsGridM') && domClass.contains(lIG,'lineItemsGrid') ){
						domClass.add(lIG, 'lineItemsGridM');
						domClass.remove(lIG, 'lineItemsGrid');
					}else if(!domClass.contains(lIG,'lineItemsGridM') && domClass.contains(lIG,'lineItemsGridR') ){
						domClass.add(lIG, 'lineItemsGridM');
						domClass.remove(lIG, 'lineItemsGridR');
						
					}
					if(!domclass.contains(this.actionBar,"lineItemActionBarM")){			
						domclass.add(this.actionBar,"lineItemActionBarM");
						domclass.remove(this.actionBar, 'lineItemActionBarR');
					}
					domStyle.set(this.id + '_lineItems', 'height', '300px');//320
					thisDialog.grid.resize();
					
					
					
				}
			};
			domclass.add(this.actionBar,"lineItemActionBarR");
//			aspect.after(this.resizeHandle, 'onResize', resizeGrid, thisDialog.grid);
			aspect.after(this._maximizeButton, 'onClick', maximizeGrid, this._maximizeButton);
			aspect.after(this._restoreButton, 'onClick', restoreGrid, this._restoreButton);
			
			
			var th = thisDialog.grid.viewsHeaderNode.children[1].children[0].children[0]
				.children[0].children[0].children[0].children[0];
			domClass.add(th, 'linesTableHeader');
			var dojoGridScrollBoxx=thisDialog.grid.viewsNode.children[0].children[2];
			domClass.add(dojoGridScrollBoxx,"gridScrollBoxC")
			
			//Button
//			domStyle.set(this.id + '_addButton', 'text-align', 'center');
//			domStyle.set(this.id + '_addButton', 'margin-top', '10px');
//			domStyle.set(this.id + '_addButton', 'margin-bottom', '10px');
			
		},
		fillFields:function(item){

			var items = thisDialog.grid.selection.getSelected();
			var hasTP = false;
			var hasPN = false;
			var hasOC = false;
			var firstItem = null;
			thisDialog.update.set('disabled',false);
			var disabling=false;
			if(items==null || items.length==0){
				thisDialog.clearItem();
			} else if(items.length > 1){
//				thisDialog.clearFields();
				thisDialog.qty.set('disabled',true);
					 firstItem = items[0];
					 for(var k=0;k<items.length;k++){
						item=items[k];
						if(thisDialog.getValue(firstItem.INVOICEITEMTYPE)!=
						   thisDialog.getValue(item.INVOICEITEMTYPE))
						{	
							disabling=true;
							thisDialog.update.set('disabled',true);
						}		
					 }
				if(!disabling){
					thisDialog.loadSharedValuesInDialog();
				}
						
			} else {
				thisDialog.qty.set('disabled',false);
					if(item.INVOICEITEMTYPE[0]==thisDialog.TYPE_MODEL){
						thisDialog.typeModel.set("checked",true);
						thisDialog.typeModelEnable();
		
					}else if(item.INVOICEITEMTYPE[0]==thisDialog.PARTNUMBER){
						thisDialog.part.set("checked",true);
						thisDialog.partEnable();
						
					}else if (item.INVOICEITEMTYPE[0]==thisDialog.OTHER_CHARGE){
						thisDialog.otherCharge.set("checked",true);
						thisDialog.otherChargeEnable();
					}
					thisDialog.loadValues(item);
			
			}
		},
		loadSharedValuesInDialog:function(){
			var items = thisDialog.grid.selection.getSelected();
			if(items!=null && items.length>1){
                
				var typeExist = true;              
				var modelExist = true;             
				var partExist = true;              
				var otherChargeExist = true;       
				var serialExist = true;            
				var mesNumExist = true;            
				var lineNumExist = true;           
				var sublineNumExist = true;        
				var unitPriceExist = true;         
				var termExist = true;              
				var quantityExist = true;          
				var poNumExist = true;             
				var financeTypeExist = true;       
				var vatAmountExist = true;
				var vatCodeExist=true;
				var extLnExist=true;
				var descriptionExist = true; 
				var totalVatExist=true;
				var item=items[0];
				var tmpItem=null;
				for(var k=1;k<items.length;k++){
					tmpItem=items[k];
					if(item.INVOICEITEMTYPE[0]==thisDialog.TYPE_MODEL){
						if(item.TYPE[0]!=tmpItem.TYPE[0]){
							thisDialog.type.set("value","");
							typeExist=false;
						}else if(typeExist){
							thisDialog.type.set("value",item.TYPE[0]);
						}
						if(item.MODEL[0]!=tmpItem.MODEL[0]){
	        				thisDialog.model.set("value","");
	        				modelExist = false;
	        			}else if(modelExist){
	        				thisDialog.model.set("value",item.MODEL[0]);
	        			}
	           			if(!isNaN(Number(item.QUANTITY[0])) && 
	           					Number(item.QUANTITY[0])<=1 &&
	           					item.SERIAL[0]!=tmpItem.SERIAL[0]){
	           				thisDialog.serial.set("value","");
	           				serialExist = false;
	        			}else if(serialExist){
	        				thisDialog.serial.set("value",item.SERIAL[0]);
	        			}
	        			if( !isNaN(Number(item.QUANTITY[0])) &&
	        					Number(item.QUANTITY[0])<=1 && 
	        					item.MESNUMBER[0]!=tmpItem.MESNUMBER[0]){
	        				thisDialog.mesNum.set("value","");
	        				mesNumExist = false;
	        			}else if(mesNumExist){
	        				thisDialog.mesNum.set("value",item.MESNUMBER[0]);
	        			}
	        			
	        			thisDialog.partNum.set("value","");
	        			thisDialog.charge.set("value","");

					}
					if(item.INVOICEITEMTYPE[0]==thisDialog.PARTNUMBER){
	        			if(item.PARTNUMBER[0]!=tmpItem.PARTNUMBER[0]){
	        				thisDialog.partNum.set("value","");
	        				partExist = false;
	        			}else if(partExist){
	        				thisDialog.partNum.set("value",item.PARTNUMBER[0]);
	        			}
	        			thisDialog.type.set("value","");
	        			thisDialog.model.set("value","");
	        			thisDialog.charge.set("value","");
	        			
	            		
	        		}
					if(item.INVOICEITEMTYPE[0]==thisDialog.OTHER_CHARGE){
	        			if(item.OTHERCHARGE[0]!=tmpItem.OTHERCHARGE[0]){
	        				thisDialog.charge.set("value","");
	        				otherChargeExist = false;
	        			}else if(otherChargeExist){
	        				thisDialog.charge.set("value",item.OTHERCHARGE[0]);
	        			}
	        			thisDialog.type.set("value","");
	        			thisDialog.model.set("value","");
	        			thisDialog.partNum.set("value","");
	        			
	            		
	        		}
	        		
					if(item.DESCRIPTION[0]!=tmpItem.DESCRIPTION[0]){
        				thisDialog.desc.set("value","");
        				descriptionExist = false;
        			}else if(descriptionExist){
        				thisDialog.desc.set("value",item.DESCRIPTION[0]);
        			}
					if(item.LINENUMBER[0]!=tmpItem.LINENUMBER[0]){
        				thisDialog.invLnNum.set("value","");
        				lineNumExist = false;
        			}else if(lineNumExist){
        				thisDialog.invLnNum.set("value",item.LINENUMBER[0]);
        			}
					if(item.SUBLINENUMBER[0]!=tmpItem.SUBLINENUMBER[0]){
        				thisDialog.invLnNum2.set("value","");
        				sublineNumExist = false;
        			}else if(sublineNumExist){
        				thisDialog.invLnNum2.set("value",item.SUBLINENUMBER[0]);
        			}
					if(item.UNITPRICE[0]!=tmpItem.UNITPRICE[0]){
        				thisDialog.unitPrice.set("value","");
        				unitPriceExist = false;
        			}else if(unitPriceExist){
        				thisDialog.unitPrice.set("value",item.UNITPRICE[0]);
        			}
					if(item.TERM[0]!=tmpItem.TERM[0]){
        				thisDialog.term.set("value","");
        				termExist = false;
        			}else if(termExist){
        				thisDialog.term.set("value",item.TERM[0]);
        			}
					if(item.QUANTITY[0]!=tmpItem.QUANTITY[0]){
        				thisDialog.qty.set("value","");
        				quantityExist = false;
        			}else if(quantityExist){
        				thisDialog.qty.set("value",item.QUANTITY[0]);
        			}
        			if(item.FINANCINGTYPE[0]!=tmpItem.FINANCINGTYPE[0]){
        				thisDialog.finType.set("value","");
        				financeTypeExist = false;
        			}else if(financeTypeExist){
        				thisDialog.finType.set("value",item.FINANCINGTYPE[0]);
        			}
        			if(item.PONUMBER[0]!=tmpItem.PONUMBER[0]){
        				thisDialog.poNum.set("value","");
        				poNumExist = false;
        			}else if(poNumExist){
        				thisDialog.poNum.set("value",item.PONUMBER[0]);
        			}
        			if(item.VATAMOUNT[0]!=tmpItem.VATAMOUNT[0]){
        			
        				thisDialog.taxAmtLn.set("value","0.00");
        				
        				vatAmountExist = false;
        			}else if(vatAmountExist){
        
        				thisDialog.taxAmtLn.set("value",item.VATAMOUNT[0]);
        			}
        			
        			if(thisDialog.serviceParameters["country"]!="US" &&  
        					item.VATCODE[0]!=tmpItem.VATCODE[0]){
            			
        				thisDialog.taxCode.set("value","");
        				
        				vatCodeExist = false;
        			}else if(vatCodeExist){
        
        				thisDialog.taxCode.set("value",item.VATCODE[0]);
        			}
        			
        			if(item.EXTENDEDPRICE[0]!=tmpItem.EXTENDEDPRICE[0]){
            			
        				thisDialog.extLnPr.set("value","0.00");
        				
        				extLnExist = false;
        			}else if(extLnExist){
        
        				thisDialog.extLnPr.set("value",item.EXTENDEDPRICE[0]);
        			}
        			if(	item.TOTALVATAMOUNT[0]!=tmpItem.TOTALVATAMOUNT[0]){
            			
        				thisDialog.totTaxAmt.set("value","0.00");
        				
        				totalVatExist = false;
        			}else if(totalVatExist){
        
        				thisDialog.totTaxAmt.set("value",item.TOTALVATAMOUNT[0]);
        			}
				}                               
			}
		},
		
		cancel : function() {
			try { 
				
				thisDialog.destroyRecursive();
				this.destroyRecursive();
			
			}catch(e){
				console.log(e);
		}
		},
		loadAllValuesToSave:function(){

		},
		
		updateItemForPart:function(item){

			thisDialog.initItem("1");
			thisDialog.initNewLnItem();
			thisDialog.getFilledValues();
			thisDialog.newLineItem["QUANTITY"] = "1";
			thisDialog.newLineItem["TOTALVATAMOUNT"] = thisDialog.newLineItem["VATAMOUNT"];
			thisDialog.newLineItem["EXTENDEDPRICE"] = thisDialog.newLineItem["UNITPRICE"];
			if(thisDialog.serviceParameters['country']=='US'){
				thisDialog.setUsTaxPercentage();
				if(item.USTAXPERCENT[0]==null){
					thisDialog.newLineItem["VATCODE"]=thisDialog.NO_TAX;
				}else if(isNaN(Number(item.USTAXPERCENT[0].trim()))  ||Number( item.USTAXPERCENT[0].trim())==0){
					thisDialog.newLineItem["VATCODE"]=thisDialog.NO_TAX;
				}else{
					thisDialog.newLineItem["VATCODE"]=thisDialog.WITH_TAX;
				}
				thisDialog.recalculateVATAmount(thisDialog.newLineItem);

			}
			
			var qty = thisDialog.qty.get('value');
			var zeroPrice = false;
			var matchingSerialCount = thisDialog.countMatchingSerials();
			var auxLnItem = null;
			var auxArrOfLnItems= [];
			
			var unitPrice = parseFloat(thisDialog.newLineItem["UNITPRICE"]);
			
			if(isNaN(unitPrice) || MathJs.compare(unitPrice,0)!=0) {
				zeroPrice = true;
			}
		    // if the quantity is zero, that means copy in all matching type / model
		    // / $ matching records
		    if (qty == 0){
		        qty = matchingSerialCount;
		    }
		    var mixItem={};
			thisDialog.copyChangedFields(item,thisDialog.newLineItem,mixItem);
		    debugger;
	        for (var i = 0; i < qty; i++) {
	        
//	        	var copy = lang.mixin({}, item);
//	        	var copy = JSON.parse(JSON.stringify( item));
//	        	var auxLnItem = lang.clone(item);
//	        	auxArrOfLnItems.push(copy);
	        	
	        	auxArrOfLnItems.push({
					'BILLEDTOIGFINDC':thisDialog.getValue(item.BILLEDTOIGFINDC),
					'BLANKTYPEMODEL':thisDialog.getValue(item.BLANKTYPEMODEL),  
					'CONFIGUSED':thisDialog.getValue(item.CONFIGUSED),         
					'COUNTRY': thisDialog.getValue(item.COUNTRY),
					'LINENUMBER': thisDialog.getValue(item.LINENUMBER),
					'TYPE': thisDialog.getValue(item.TYPE),
					'MODEL': thisDialog.getValue(item.MODEL),
					'SERIAL': thisDialog.getValue(item.SERIAL),
					'QUANTITY': thisDialog.getValue(item.QUANTITY),
					'PARTNUMBER': thisDialog.getValue(item.PARTNUMBER),
					'DESCRIPTION': thisDialog.getValue(item.DESCRIPTION),
					'UNITPRICE': thisDialog.getValue(item.UNITPRICE),
					'VATAMOUNT': thisDialog.getValue(item.VATAMOUNT),
					'EXTENDEDPRICE': thisDialog.getValue(item.EXTENDEDPRICE),
					'TOTALVATAMOUNT':thisDialog.getValue( item.TOTALVATAMOUNT),
					'MESNUMBER': thisDialog.getValue(item.MESNUMBER),
					'VATCODE':thisDialog.getValue( item.VATCODE),
					'SUBLINENUMBER':thisDialog.getValue( item.SUBLINENUMBER),
					'CONFIGUSED': thisDialog.getValue(item.CONFIGUSED),
					'COSTCENTER':thisDialog.getValue(item.COSTCENTER),          
					'DESCRIPTION':thisDialog.getValue(item.DESCRIPTION),        
					'EQUIPSOURCE':thisDialog.getValue(item.EQUIPSOURCE),        
					'EXTENDEDPRICE':thisDialog.getValue(item.EXTENDEDPRICE),    
					'EXTENSIONIND':thisDialog.getValue(item.EXTENSIONIND),      
					'FINANCINGTYPE':thisDialog.getValue(item.FINANCINGTYPE),    
					'FROMQUOTE':thisDialog.getValue(item.FROMQUOTE),            
					'INVOICEITEMTYPE':thisDialog.getValue(item.INVOICEITEMTYPE),
					'LINENUMBER':thisDialog.getValue(item.LINENUMBER),          
					'MAXUNITPRICE':thisDialog.getValue(item.MAXUNITPRICE),      
					'MESINDICATOR':thisDialog.getValue(item.MESINDICATOR),      
					'MESNUMBER':thisDialog.getValue(item.MESNUMBER),            
					'MINUNITPRICE':thisDialog.getValue(item.MINUNITPRICE),      
					'MODEL':thisDialog.getValue(item.MODEL),                    
					'OTHERCHARGE':thisDialog.getValue(item.OTHERCHARGE),        
					'PARTNUMBER':thisDialog.getValue(item.PARTNUMBER),          
					'PONUMBER':thisDialog.getValue(item.PONUMBER),              
					'PRODCAT':thisDialog.getValue(item.PRODCAT),                
					'QUANTITY':thisDialog.getValue(item.QUANTITY),              
					'SERIAL':thisDialog.getValue(item.SERIAL),                  
					'SUBLINENUMBER':thisDialog.getValue(item.SUBLINENUMBER),    
					'TAXESINDICATOR':thisDialog.getValue(item.TAXESINDICATOR),  
					'TERM':thisDialog.getValue(item.TERM),                      
					'TOTALVATAMOUNT':thisDialog.getValue(item.TOTALVATAMOUNT),  
					'TRANSACTIONTYPE':thisDialog.getValue(item.TRANSACTIONTYPE),
					'TYPE':thisDialog.getValue(item.TYPE),                      
					'UNITPRICE':thisDialog.getValue(item.UNITPRICE),            
					'USTAXPERCENT':thisDialog.getValue(item.USTAXPERCENT),      
					'VATAMOUNT':thisDialog.getValue(item.VATAMOUNT),            
					'VATCODE':thisDialog.getValue(item.VATCODE),                

					
				});
			}
	        
	    
		    thisDialog.grid.store.deleteItem(item);
		    thisDialog.grid._refresh();
		     
		    thisDialog.grid.selection.deselectAll();
		    thisDialog.grid.render();
		    debugger;
	        thisDialog.loadCurrentList();
	        
	        thisDialog.jsonLineItems.push.apply(thisDialog.jsonLineItems,auxArrOfLnItems);
	        thisDialog.renumberAndSpread(thisDialog.clearFields);
//			thisDialog.spreadTaxRoundingError(
//					thisDialog.renumberLineItems.bind(null,auxArrOfLnItems,
//							thisDialog.clearFields))
		},

		copyChangedFields:function(gridItem,newLnItem, mixItem) {

	    	if(newLnItem.INVOICEITEMTYPE==thisDialog.TYPE_MODEL){
	    	   	if(newLnItem.TYPE.trim().length!=0){
	    	   		gridItem.TYPE[0] = newLnItem.TYPE.trim();
	        	}
	    	   	if(newLnItem.MODEL.trim().length!=0){
	    	   		gridItem.MODEL[0] = newLnItem.MODEL.trim();
	    	   	}
	    	   	if(gridItem.TYPE[0]=="XXXX"){
	    	   		gridItem.BLANKTYPEMODEL[0]="Y";
	    	   	}else{
	    	   		gridItem.BLANKTYPEMODEL[0]="N";
	    	   	}
	    	   	gridItem.OTHERCHARGE[0]=gridItem.TYPE[0]+"/"+gridItem.MODEL[0];
	    	   	gridItem.PARTNUMBER[0]= "";
	    	   	if(gridItem.TYPE[0].trim().length!=0 &&
	    	   			gridItem.MODEL[0].trim().length!=0 &&
	    	   			newLnItem.DESCRIPTION.trim().length!=0){
	    	   		gridItem.DESCRIPTION[0] = (newLnItem.DESCRIPTION.trim());	
	    	   		//all these values changed if there is a new description 
	    	   		gridItem.MAXUNITPRICE[0]=newLnItem.MAXUNITPRICE;
	    			gridItem.MINUNITPRICE[0]=newLnItem.MINUNITPRICE;  			          
	    			gridItem.PRODTYPE[0]=newLnItem.PRODTYPE;
	    			gridItem.PRODCAT[0]=newLnItem.PRODCAT;
	    	   	}
	    	   	gridItem.INVOICEITEMTYPE[0] = newLnItem.INVOICEITEMTYPE;
	    	}
	    	
	    	
	    	if(newLnItem.INVOICEITEMTYPE==thisDialog.PARTNUMBER){
	    		if(newLnItem.PARTNUMBER.trim().length!=0){
	    			gridItem.PARTNUMBER[0] = (newLnItem.PARTNUMBER.trim());
	    		}
	    		gridItem.TYPE[0]="";
	    		gridItem.MODEL[0]="";
	    		gridItem.OTHERCHARGE[0] ="";
	    		gridItem.INVOICEITEMTYPE[0] = newLnItem.INVOICEITEMTYPE;
	    		var items = thisDialog.grid.selection.getSelected();
	    		if(items.length==1){//if it is not mass update allow to set any description,
	    			gridItem.DESCRIPTION[0] = (newLnItem.DESCRIPTION.trim());
	    		}else if(items.length>1 && newLnItem.DESCRIPTION.trim().length!=0){ 
	    			//else if it mass update and description different from empty it will be changed, 
	    			//if it's empty means it shouldn't affect the value the gridItem has in description.
	    			gridItem.DESCRIPTION[0] = (newLnItem.DESCRIPTION.trim());
	    		}
	    	}
	 
	    	
	    	if(newLnItem.INVOICEITEMTYPE==thisDialog.OTHER_CHARGE){
	    		if(newLnItem.OTHERCHARGE.trim().length!=0){
	    			gridItem.OTHERCHARGE[0] = (newLnItem.OTHERCHARGE.trim());
	    		}
	    		if(newLnItem.TYPE.trim().length!=0){
	    	   		gridItem.TYPE[0] = newLnItem.TYPE.trim();
	        	}
	    	   	if(newLnItem.MODEL.trim().length!=0){
	    	   		gridItem.MODEL[0] = newLnItem.MODEL.trim();
	    	   	}
	    	   	if(gridItem.TYPE[0].trim().length!=0 &&
	    	   			gridItem.MODEL[0].trim().length!=0 &&
	    	   			newLnItem.DESCRIPTION.trim().length!=0)
	    	   	gridItem.DESCRIPTION[0] = (newLnItem.DESCRIPTION.trim());
	    	   	
	    		gridItem.PARTNUMBER[0]= "";
	    		gridItem.INVOICEITEMTYPE[0] = newLnItem.INVOICEITEMTYPE;
	    	}
	    	
	       	if(newLnItem.LINENUMBER.trim().length != 0){
	       		gridItem.LINENUMBER[0] = (newLnItem.LINENUMBER.trim());
	       	}
	       	if(newLnItem.SUBLINENUMBER.trim().length!=0){
	       		gridItem.SUBLINENUMBER[0] = (newLnItem.SUBLINENUMBER.trim());
	       	}
	       	if(newLnItem.QUANTITY.trim().length!=0){
	       		gridItem.QUANTITY[0] = (newLnItem.QUANTITY.trim());
	       	}
	       	if(newLnItem.SERIAL.trim().length!=0){
	       		gridItem.SERIAL[0] = (newLnItem.SERIAL.trim());
	       	}
	       	if(newLnItem.MESNUMBER.trim().length!=0){
	       		gridItem.MESNUMBER[0] = (newLnItem.MESNUMBER.trim());
	       	}
	       	
			var unitPrice = parseFloat(newLnItem.UNITPRICE);
			
			if(!isNaN(unitPrice) && MathJs.compare(unitPrice,0)!=0) {
				
				gridItem.UNITPRICE[0] = (newLnItem.UNITPRICE.trim());
				
			}
			mixItem.UNITPRICE=gridItem.UNITPRICE[0];
			
			var extPrice = parseFloat(newLnItem.EXTENDEDPRICE);
			
			if(!isNaN(extPrice) && MathJs.compare(extPrice,0)!=0) {
				
				
				gridItem.EXTENDEDPRICE[0] = (newLnItem.EXTENDEDPRICE.trim());
				
			}
			mixItem.EXTENDEDPRICE=gridItem.EXTENDEDPRICE[0];
			
			var vatPrice = parseFloat(newLnItem.VATAMOUNT);
		
			if((thisDialog.serviceParameters['country']=='US' && !isNaN(vatPrice)) || 
						( thisDialog.serviceParameters['country']!='US' && (  !isNaN(vatPrice) && MathJs.compare(vatPrice,0)!=0))) {
						
				gridItem.VATAMOUNT[0] = (newLnItem.VATAMOUNT.trim());
				
			}
			mixItem.VATAMOUNT=gridItem.VATAMOUNT[0];
			
			var totVatPrice = parseFloat(newLnItem.TOTALVATAMOUNT);
			
			if((thisDialog.serviceParameters['country']=='US' && !isNaN(totVatPrice)) || 
					( thisDialog.serviceParameters['country']!='US' && (  !isNaN(totVatPrice) && MathJs.compare(totVatPrice,0)!=0))) {
				
				gridItem.TOTALVATAMOUNT[0] = (newLnItem.TOTALVATAMOUNT.trim());
				
			}
			mixItem.TOTALVATAMOUNT=gridItem.TOTALVATAMOUNT[0];
			
			if(newLnItem.VATCODE.length!=0) {
				
				gridItem.VATCODE[0]= (newLnItem.VATCODE.trim());
			}
			mixItem.VATCODE=gridItem.VATCODE[0];
			
			if(newLnItem.TERM.trim().length!=0)       	
	       	gridItem.TERM[0]= (newLnItem.TERM.trim()); 
			
			if(newLnItem.PONUMBER.trim().length!=0)	       	  	    	
	       	gridItem.PONUMBER[0] = (newLnItem.PONUMBER.trim());
			
			if(newLnItem.FINANCINGTYPE.trim().length!=0)
	       	gridItem.FINANCINGTYPE[0] = (newLnItem.FINANCINGTYPE.trim());
			//always will have a value since it's a checkbox
	       	gridItem.BILLEDTOIGFINDC[0] = (newLnItem.BILLEDTOIGFINDC);	       	
	       	       	
	       	if(thisDialog.serviceParameters['country']=='US'){
				
				thisDialog.setUsTaxPercentage();
				gridItem.USTAXPERCENT[0] = (newLnItem.USTAXPERCENT);
				
			}
	       	if(newLnItem.COSTCENTER.trim().length!=0)	 
			gridItem.COSTCENTER[0]=newLnItem.COSTCENTER;  
	       	
			if(newLnItem.MESINDICATOR.trim().length!=0)	 
			gridItem.MESINDICATOR[0]=newLnItem.MESINDICATOR;
			
			if(newLnItem.MESNUMBER.trim().length!=0)	 
			gridItem.MESNUMBER[0]=newLnItem.MESNUMBER;  
			
			
	       
	     },
		
		
		loadValues:function(item){
			
			thisDialog.type.set('value',item.TYPE[0]);
			thisDialog.model.set('value',item.MODEL[0]);
			thisDialog.partNum.set('value',item.PARTNUMBER[0]);
			thisDialog.charge.set('value',item.OTHERCHARGE[0]);//combo
			thisDialog.invLnNum.set('value', item.LINENUMBER[0]);
			thisDialog.invLnNum2.set('value',item.SUBLINENUMBER[0]);
			thisDialog.serial.set('value',item.SERIAL[0]);
			thisDialog.unitPrice.set('value',item.UNITPRICE[0]);
			thisDialog.mesNum.set('value',item.MESNUMBER[0]);
			thisDialog.taxCode.set('value',item.VATCODE[0]);//combo
			thisDialog.taxAmtLn.set('value',item.VATAMOUNT[0]);
			thisDialog.extLnPr.set('value',item.EXTENDEDPRICE[0]);
			thisDialog.totTaxAmt.set('value',item.TOTALVATAMOUNT[0]);
			thisDialog.costCenter.set('value',item.COSTCENTER[0]);
			thisDialog.qty.set('value',item.QUANTITY[0]);
			thisDialog.poNum.set('value',item.PONUMBER[0]);
			thisDialog.desc.set('value',item.DESCRIPTION[0]);
			thisDialog.mes.set("checked",thisDialog.getBoolean(item.MESINDICATOR[0]));
			thisDialog.term.set('value',item.TERM[0]);
			thisDialog.finType.set('value',item.FINANCINGTYPE[0]);
			thisDialog.lnBillIgf.set("checked",thisDialog.getBoolean(item.BILLEDTOIGFINDC[0]));

		},
		 openOEMDialog:function(){

				var dialogOEM= null;				
				var thisForm = this;							
				dialogSupplier = new OEMProductDialog({callerForm:thisForm, serviceParameters: this.serviceParameters  });

			}, 
		 getBoolean: function(indc){
		    	
		    	if( indc=="Y" || indc =="y")
		    		return true;
		    	else
		    		return false;
		    },
		    
		    showMessage: function(titl, msg, needConfirmation, callbackYes, callbackNo, onlyOkbutton) {	

	            var d1 = new NonModalConfirmDialog({
	                title: titl, 
	                style: "min-width:350px;",
	                content:msg,
	                name:"dialogMessage"	
	            })
	            
	            if(needConfirmation && onlyOkbutton){
	            	   var actionBar = dojo.create("div", { "class": "dijitDialogPaneActionBar"  }, d1.containerNode);
	       			
				        var buttonOk=new dijit.form.Button({"label": "OK",
				            onClick: function(){
				            	thisDialog.isErrorDisplayed=false;
				            	callbackYes();
				            	d1.destroyRecursive();  
				            	
				            } });
				        buttonOk.placeAt(actionBar);
				        domclass.add(buttonOk.focusNode, "yesNoOkButton");

	            }else if(needConfirmation){

			        var actionBar = dojo.create("div", {"class": "dijitDialogPaneActionBar"   }, d1.containerNode);
			        var buttonYes=new dijit.form.Button({ 
			        	"label": "Yes",
			        	onClick: function(){
			        		thisDialog.isErrorDisplayed=false;
			            	callbackYes();
			            	d1.destroyRecursive();   
			            	
			            }});
			        buttonYes.placeAt(actionBar);
			        domclass.add(buttonYes.focusNode, "yesNoOkButton");
			        var buttonNo =new dijit.form.Button({
			        	"label": "No",
			        	
			            onClick: function(){
			            	thisDialog.isErrorDisplayed=false;
			            	callbackNo();
			            	d1.destroyRecursive(); 
			            	
			            	}});
			        buttonNo.placeAt(actionBar);
			        domclass.add(buttonNo.focusNode, "yesNoOkButton");
	            }else if( onlyOkbutton && !needConfirmation){
	            	 var actionBar = dojo.create("div", { "class": "dijitDialogPaneActionBar"  }, d1.containerNode);
		       			
				        var buttonOk=new dijit.form.Button({"label": "OK",
				            onClick: function(){
				            	thisDialog.isErrorDisplayed=false;
				            	d1.destroyRecursive(); 
				            	
				            } });
				        buttonOk.placeAt(actionBar);
				        domclass.add(buttonOk.focusNode, "yesNoOkButton");

	            }else {
	            
	            
				  
				        var actionBar = dojo.create("div", { "class": "dijitDialogPaneActionBar"  }, d1.containerNode);
			
				        var buttonOk=new dijit.form.Button({"label": "OK",
				            onClick: function(){
				            	thisDialog.isErrorDisplayed=false;
				            	d1.destroyRecursive(); 
				            	
				            } });
				        buttonOk.placeAt(actionBar);
				        domclass.add(buttonOk.focusNode, "yesNoOkButton");
				        
				        var buttonCancel=new dijit.form.Button({"label": "Cancel",
				            onClick: function(){
				            	thisDialog.isErrorDisplayed=false;
				            	d1.destroyRecursive();
				            	
				            	} });
				        buttonCancel.placeAt(actionBar);
				        domclass.add(buttonCancel.focusNode, "cancelButton");
	            }
	            
		        d1.startup();
		        
	            d1.show();
	            thisDialog.isErrorDisplayed=true;
				
			},
		
		});
	return LineItemsDialog;		
});



