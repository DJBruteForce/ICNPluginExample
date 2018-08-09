define(["dojo/_base/lang",
        "ecm/model/Action", 
        "ecm/model/Request", 
        "dojo/_base/declare", 
		"ecm/widget/dialog/BaseDialog",
		"dojo/json",
		"gilCnPluginDojo/util/JsonUtils",
		"gilCnPluginDojo/util/MathJs",
		"dojo/text!./templates/OfferingLetterDialog.html",
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
	    "gilCnPluginDojo/SupplierSearchDialog",
	    "dojox/grid/DataGrid",
	    "dojo/data/ItemFileWriteStore",
	    "gilCnPluginDojo/constants/ValidationConstants",
	    "gilCnPluginDojo/GILDialog",
	    "dojo/_base/connect", 
	    "dojo/_base/event",
	    "gilCnPluginDojo/util/MathJs",
	    "dojo/io-query",
	    "dijit/focus",
	    "dojo/dom-class",
	    "dijit/registry",
	    "dojo/aspect" ,
	    "gilCnPluginDojo/util/MathJs",
	],
	
	function(lang, action, Request,declare,	BaseDialog,	json,JsonUtils, MathJs, template, array, dom,domReady,form,date,locale,domStyle,registry,query,construct,DateTextBox,parser,domattr,AptsDialog,ValidationConstants,ConfirmDialog ,NonModalConfirmDialog,i18n,
			number,Button,ConfirmationDialog,Builder,DialogMessages,ECMConfirmationDialog,Tooltip,stamp ,
			focus,connect, event,SupplierSearchDialog,DataGrid,ItemFileWriteStore,ValidationConstants,GILDialog,connect,event,MathJs,ioquery,focusUtil ,domclass,registry,aspect,MathJs ) {
	
	var OfferingLetterDialog = declare("gilCnPluginDojo.OfferingLetterDialog", [BaseDialog,GILDialog,AptsDialog], {
		
		contentString: template,
		widgetsInTemplate: true,
		jsonOfferingLetterObject: null,
		BALANCECHECK : null,
		constructor: function(args){
	        declare.safeMixin(this,args);
	        this.serviceParameters['country'] = this.serviceParameters['className'].substring(this.serviceParameters['className'].length - 2);
	    },

		postCreate: function() {
			
			this.inherited(arguments);
			this.setResizable(true);
			this.setMaximized(false);
			this.addButton("Cancel", "cancel", false, true);
					
			this.addButton("Save", "save", false, true,this.id+"_saveOL");
			this.setTitle("APTS Index Offer Letter [" + this.serviceParameters['className'] + "]");
			dojo.style(this.cancelButton.domNode,"display","none"); 
			this.setSize(550, 500);
			
			this.initializeFields();
			this.toChange();
			this.setCustomizedValidation();
			this.initializeDateFields();
			this.upperCaseFields();
			this.validatePartialCounter=0;
			this.setLayout();

		},
		
cancel: function(dirtCheck) {
			
			var dlgMsg = new DialogMessages();
			console.log("dirtCheckdirtCheckdirtCheck:::"+dirtCheck);
			try {
				if(dirtCheck) {
					if( thisForm.isFormDirty(thisForm.id+"_invoiceForm") || thisForm.jsonOfferingLetterObject.DIRTYFLAG){
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

				if (thisForm.jsonOfferingLetterObject.DIRTYFLAG){
					
					thisForm.serviceParameters['frontEndAction'] = "rollbackIndexing";
					Request.postPluginService("GilCnPlugin", "OfferingLetterService",'application/x-www-form-urlencoded', {
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
		
//		cancel : function() {
//			that = this;
//			that.counterCancel=that.counterCancel+1;
//			console.log("inside cancel");
//			if(thisForm.isFormDirty(thisForm.id+"_invoiceForm") || that.jsonOfferingLetterObject.DIRTYFLAG == true ){
//			var	dialogMsgs=new DialogMessages();
//				dialogMsgs.addConfirmMessages("You have unsaved changes. Are you sure you want to quit?");				
//				that.showMessage("Confirm",dialogMsgs.confirmMessages(), true, that.choiceYCancel,that.choiceNCancel);
//
//			}else if(that.counterCancel==1 && that.jsonOfferingLetterObject.BALANCECHECK == true){
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
//		
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
		
		
		show: function() {
			this.inherited("show", []);
		},
		
       exit: function() {
			
			this.destroy();
		},
		
		defaults: function() {

			this.destroy();
		},
		
		
		
		initializeFields: function(){
			
			this.amount.set('value',"0.00");
			
		},
		
		toChange: function(){
			that =this;
			
			console.log("inside toChange function ");
			
        dojo.connect(dojo.byId(this.id+"_amount"),"change", function(){
        	
        	 var amountChanged = thisForm.amount.get("value");        	 
        	        	 
        	 that.amount.set('value',that.formatNumber(amountChanged,2));
        	 
				
        	console.log("inside toChange function dojo connect");
				if(that.amount.get('value')=="")
					{
					console.log("inside if condition in dojo connect:"+ that.amount.get('value'));
					that.amount.set("Value","0.00");}
				
				});
			
		},
		
		
		

		setLayout:function(){
			var restoring = (restore) => {
				if(restore != null){

					if(!domclass.contains(this.actionBar,"offeringletterNonElsEcmDialogPaneActionBar")){			
						domclass.add(this.actionBar,"offeringletterNonElsEcmDialogPaneActionBarR");
						domclass.remove(this.actionBar,"offeringletterNonElsEcmDialogPaneActionBarM");
						domStyle.set(this.actionBar, "padding-right", "200px");
					}

				}
			};

			var maximizing = (maximize) => {
				if(maximize != null){

					if(domclass.contains(this.actionBar,"offeringletterNonElsEcmDialogPaneActionBarR")){			
						domclass.add(this.actionBar,"offeringletterNonElsEcmDialogPaneActionBarM");
						domclass.remove(this.actionBar,"offeringletterNonElsEcmDialogPaneActionBarR");
						domStyle.set(this.actionBar, "padding-right", "15px");
					}

				}
			};

			aspect.after(this._maximizeButton, 'onClick', maximizing, this._maximizeButton);
			aspect.after(this._restoreButton, 'onClick', restoring, this._restoreButton);
			domclass.add(this.actionBar,"offeringletterNonElsEcmDialogPaneActionBarR");
			domStyle.set(this.actionBar, "padding-right", "200px");
		},

		
		index: function() {
				
			this.serviceParameters['frontEndAction'] = arguments.callee.nom;
			thisForm = this;
			var jsonUtils = new JsonUtils();
			var date= null;
			var indexed=null;
			
				
			Request.invokePluginService("GilCnPlugin", "OfferingLetterService", {
						    		requestParams : this.serviceParameters,
						    		requestCompleteCallback : lang.hitch(this, function (response) {

						    			this.jsonOfferingLetterObject	= JSON.parse(response.offeringLetterJsonString);
						    			thisForm.show();
						    			
						    			thisForm.number.set("value", this.jsonOfferingLetterObject.OFFERINGNUMBER);
						    			thisForm.amount.set("value", this.jsonOfferingLetterObject.AMOUNT);
						    			thisForm.currency.set("options", this.jsonOfferingLetterObject.currencyCodeSelectList);
							    		thisForm.currency.set("value", jsonUtils.getDefaultOrSelected(this.jsonOfferingLetterObject.currencyCodeSelectList,"value", true));
						    			thisForm.customerName.set("value",this.jsonOfferingLetterObject.CUSTOMERNAME );
						    			thisForm.createdTimestamp.set("value",this.jsonOfferingLetterObject.createdTimeStamp);
						    			thisForm.customerNumber.set("value",this.jsonOfferingLetterObject.CUSTOMERNUMBER);
						    			date = this.jsonOfferingLetterObject.OFFERDATE ;
						    			indexed= this.jsonOfferingLetterObject.INDEXED;
						    								    			
						    			if(indexed ==false)
						    				{
						    				   			
						    				thisForm.olDate.set("value", this.parseDate(date ,false) );
						    	        	}
						    			else
						    			{
						    				thisForm.olDate.set("value",date);
						    			}
						    			
						    			if(this.jsonOfferingLetterObject.isWorkAcess){
						    				 thisForm.amount.set('disabled',true);
							             }
						    			
							    			
						    			if(this.jsonOfferingLetterObject.DOCUMENTMODE == 3) {
							    			
						    				
							    			var dialogMessages = this.validateOnIndex();
//							    			 removing this, Lisa: it is appearing disabled this field when user tries to reindex and unpaid OL, user should be able to change the date
//							    			thisForm.setDateNotEditable(true);
							    			if(this.jsonOfferingLetterObject.validationIsPartiallypaidOL ){ //if it is partially paid OL or Fully paid OL then disable fields
							    			
							    				thisForm.setFieldsNotEditablePartially(true);
								    			if(this.validatePartialCounter==0)
								    				{
								    			this.showMessage("Error","Offering Letter has a payment against it and cannot be updated.",false,null,null);
								    				}
								    			
								    			
								    		}
							    			if( this.jsonOfferingLetterObject.isValidChange==false){ //if it is  Fully paid OL then disable fields
							    				
							    				thisForm.setFieldsNotEditableFull(true);
							    			}
							    			
							    			debugger;
						                 							    		
											if(!dialogMessages.hasErrorMessages() && !dialogMessages.hasConfirmMessages() ){
												
												console.log('Index loading successfully.')
													
											} else if(dialogMessages.hasErrorMessages() ){												
												
								    			this.showMessage("Error",dialogMessages.getAllMessages(),false,null,null);
								    			this.rollbackIndexing();
								    			thisForm.cancel(false);
								    		
											}
											else if(dialogMessages.hasConfirmMessages()){
												
												this.showMessage("Confirm",dialogMessages.confirmMessages(),true,thisForm.changingOlY,thisForm.changingOlN);	
											}
											
							    		}
						    			this.serialFormClean=this.serializeForm(thisForm.id+"_invoiceForm");  
						    		})
						});	
	
		},
		changingOlY: function(){
			//thisForm.jsonOfferingLetterObject['DIRTYFLAG']=true;
			thisForm.number.set('value',thisForm.jsonOfferingLetterObject.OFFERINGNUMBER);
		},
		
		changingOlN:function(){
			//thisForm.jsonOfferingLetterObject['OFFERINGNUMBER']=thisForm.OLDOFFERINGNUMBER;
			thisForm.jsonOfferingLetterObject['OFFERINGNUMBER']=thisForm.jsonOfferingLetterObject.OLDOFFERINGNUMBER;
			thisForm.number.set("value",thisForm.jsonOfferingLetterObject.OLDOFFERINGNUMBER);
			//thisForm.jsonOfferingLetterObject['DIRTYFLAG']=false;
		},
		
		save: function() {
			
			this.serviceParameters['frontEndAction'] = arguments.callee.nom;
			var businessAttributesJson = {};
			if (!this.offeringLetterForm.validate()){
				console.log('Errors found on screen.');
	            return false;
			}

			this.jsonOfferingLetterObject['OFFERINGNUMBER'] = this.number.get('value');
			this.jsonOfferingLetterObject['AMOUNT'] = this.amount.get('value');
			this.jsonOfferingLetterObject['CURRENCY'] = this.currency.get('value');
			this.jsonOfferingLetterObject['CUSTOMERNUMBER'] = this.customerNumber.get('value');
			this.jsonOfferingLetterObject['CUSTOMERNAME'] = this.customerName.get('value');
			this.jsonOfferingLetterObject['OFFERDATE'] = this.olDate.get('displayedValue');

			this.serviceParameters['jsonDataObject'] = JSON.stringify(this.jsonOfferingLetterObject);		
			thisForm = this;
			dialogMessages = this.validateOnSave(this.performSave);			
			this.serviceParameters['jsonDataObject'] = JSON.stringify(this.jsonOfferingLetterObject);	
			console.log("Indexing saved successfully" );

		},

		validateOnIndex : function() {

			var dialogMessages = new DialogMessages();
			this.jsonOfferingLetterObject.BALANCECHECK = false;
			debugger;
			 
			 try {
				
				 
				 console.log("validate on index:" + this.jsonOfferingLetterObject.validationIsPartiallypaidOL);
				 
			
					 
					
					    if(this.jsonOfferingLetterObject.validationIsPartiallypaidOL==true){					    		                      
					    	           
										if ((this.jsonOfferingLetterObject.OLDCUSTOMERNAME != this.jsonOfferingLetterObject.CUSTOMERNAME)
													|| (this.jsonOfferingLetterObject.OLDOFFERINGNUMBER != this.jsonOfferingLetterObject.OFFERINGNUMBER)
													|| (this.jsonOfferingLetterObject.OLDCUSTOMERNUMBER != this.jsonOfferingLetterObject.CUSTOMERNUMBER)
													|| (this.jsonOfferingLetterObject.OLDOFFERDATE != this.jsonOfferingLetterObject.OFFERDATE)) {
											    this.validatePartialCounter= 1;											   
												dialogMessages.addErrorMessages("Offering Letter has a payment against it and cannot be updated.");
												this.rollbackIndexing();
												return dialogMessages;

											}     				    	
					    	
					    } 
				 
				 
				if((this.jsonOfferingLetterObject.OLDOFFERINGNUMBER!=this.jsonOfferingLetterObject.OFFERINGNUMBER))
				{
					  if(this.jsonOfferingLetterObject.changeOLFlag){
						  this.jsonOfferingLetterObject['DIRTYFLAG']=true;
							dialogMessages.addConfirmMessages("Changing Offering Letter Number.  Are you sure?");	
							return dialogMessages;
				     }
				     
				}    
			    //moving this out, it doesn't depend on OL num
			    if(this.jsonOfferingLetterObject.isValidChange==false){
			    	 console.log("inside offerletter isValidChange");
			    	 thisForm.jsonOfferingLetterObject.DIRTYFLAG = false;
			    	 thisForm.jsonOfferingLetterObject.BALANCECHECK = true;
			    	 dialogMessages.addErrorMessages("Offer Letter has a 0.00 balance and cannot be updated.");			    	 
			    	 dialogMessages.setHasErrorMessages(true);
			    	 return dialogMessages;
			    }
			                
				 return dialogMessages; 

			
				} catch(e) {
					
					console.log('Error in method ' + arguments.callee.nom + ' - ' + e);
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
		
		  
	    
	    formatNumber: function(num, precis) {

	    	var res = MathJs.format(  MathJs.round(num, precis),  {notation: 'fixed', precision: precis});
	    	return res;
	    },
	    
		validateOnSave: function(perfomSaveCallback) {
			
			this.serviceParameters['frontEndAction'] = arguments.callee.nom;
			var dialogMessages = new DialogMessages();
			
			try {
					thisForm = this;
					var jsonUtils = new JsonUtils();
					
					this.serviceParameters['jsonDataObject'] = JSON.stringify(this.jsonOfferingLetterObject);
					Request.invokePluginService("GilCnPlugin", "OfferingLetterService", {
							    		requestParams : this.serviceParameters,
							    		requestCompleteCallback : lang.hitch(this, function (response) {
							    			this.jsonOfferingLetterObject	= JSON.parse(response.offeringLetterJsonString);
							    			if (this.jsonOfferingLetterObject.validationIsDuplicatedOfferingLetter){
							    				dialogMessages.addErrorMessages("Duplicate offering letter number entered");
							    				perfomSaveCallback(dialogMessages);
							    				return;
							    			}
							    			
							    			var offeringAmountNum = this.toNumber(this.amount.get("value"));										
											if ( offeringAmountNum==null || offeringAmountNum == undefined || MathJs.compare(offeringAmountNum,0) <=0  )	 {		
												dialogMessages.addErrorMessages("Offering Letter Amount must be > 0.00");	
												perfomSaveCallback(dialogMessages);
												return;
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
				if(!dialogMessages.hasErrorMessages() && !dialogMessages.hasWarningMessages() ){
					
					thisForm.serviceParameters['frontEndAction'] = 'save';	
					var jsonUtils = new JsonUtils();
					var dialogMessagesResponse = new DialogMessages(); 
					Request.postPluginService("GilCnPlugin", "OfferingLetterService",'application/x-www-form-urlencoded', {
				    		requestBody : ioquery.objectToQuery(thisForm.serviceParameters),
							requestCompleteCallback : lang.hitch(this, function (response) {
								
								    			thisForm.jsonOfferingLetterObject	= JSON.parse(response.offeringLetterJsonString);
								    			
								    			if (!thisForm.jsonOfferingLetterObject.validationIsContractFolderCreated) {
								    				dialogMessagesResponse.addErrorMessages("Error creating offering letter folder");
								    			}
								    			
								    			if (dialogMessagesResponse.hasErrorMessages()) {
								    				thisForm.showMessage("Error",dialogMessagesResponse.getAllMessages(),false,null,null);
								    			}
								    			
									    		thisForm.cancel(false);	
									    	 })
									});
	
		      } else if(dialogMessages.hasErrorMessages() ){
				        thisForm.showMessage("Validation",dialogMessages.getAllMessages(),false,null,null);
				        return false;
		      }
		   }
		   catch(e) {
			   console.log("exception in performsave:"+e);
		   }
		},
		
		rollbackIndexing : function() {

			thisForm.serviceParameters['frontEndAction'] = arguments.callee.nom;
			Request.invokePluginService("GilCnPlugin", "OfferingLetterService",{
					    		requestParams : thisForm.serviceParameters,
					    		requestCompleteCallback : lang.hitch(this, function (response) {
					    				console.log('Index rolled back successfully.');
					    				that.finalizeGUI();
						    			return true;
							    		
	    		})
			});
		},	
		

	setFieldsNotEditablePartially :  function(editable){
			
			var thisForm = this;
			console.log("inside setFieldsNotEditablePartially ");
			thisForm.number.set('disabled',editable);
			thisForm.amount.set('disabled',editable);
			thisForm.currency.set('disabled',editable);
			thisForm.customerName.set('disabled',editable);
			thisForm.olDate.set('disabled',editable);

		},	
		setFieldsNotEditableFull :  function(editable){
			
			var thisForm = this;
			console.log("inside setFieldsNotEditableFull ");
			thisForm.number.set('disabled',editable);
			thisForm.amount.set('disabled',editable);
			thisForm.currency.set('disabled',editable);
			thisForm.customerNumber.set('disabled',editable);
			thisForm.customerName.set('disabled',editable);
			thisForm.olDate.set('disabled',editable);
			dijit.byId(this.id+"_saveOL_dijit_form_Button_0").set("disabled",true); //this won't allow user to save something just check values
			
		},	
		
   setDateNotEditable :  function(editable){
			
			var thisForm = this;
			thisForm.olDate.set('disabled',editable);
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
	
		        new dijit.form.Button({ "label": "Yes",
		            onClick: function(){
		            	
		            	callbackYes();
		            	d1.destroyRecursive(); 
		            	
		            }}).placeAt(actionBar);
		        
		        new dijit.form.Button({"label": "No",
		            onClick: function(){
		            	
		            	callbackNo();
		            	d1.destroyRecursive();
		            	
		            	}}).placeAt(actionBar);
            	
            } else {
            
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
            }
            
	        d1.startup();
            d1.show();
			
		},
		
		setCustomizedValidation :function() {
  
			var thisForm = this;

			lang.getObject('amount', false, this).validator = function() {
				
		        var amountInt = parseInt(thisForm.amount.get("value"));
		       		        		        
		        /*if(amountInt=='0'){
		        	lang.getObject('amount', false, thisForm).invalidMessage="Offering Letter Amount must be > 0.00";
					return false;	
		        }*/
							
				if (amountInt<'0'){
					lang.getObject('amount', false, thisForm).invalidMessage="Offering Letter Amount can not be negative.";
					return false;
				}						
							  		
			if (amountInt.toString().length>15){
				lang.getObject('amount', false, thisForm).invalidMessage="Please enter an OL amount that is 15 characters (including cents) or less.";
				return false;
			}
			
			if(thisForm.isNumberKey(thisForm.id+"_amount")){
				
				lang.getObject('amount', false, thisForm).invalidMessage="Special Characters are not allowed in amount";
				
				return false;
				
			}
	
			return true;
			}
			

				thisForm.number.set("missingMessage","Offering Letter Number is a required field.");
				thisForm.amount.set("missingMessage","Offering Letter amount is a required field.");
				thisForm.customerName.set("missingMessage","Customer Name is required field.");
				
				var  auxOLDate = thisForm.olDate.get("value");
				
					if (auxOLDate!=null) {
				
				if (date.difference(auxOLDate,new Date()) > 300	)	{
					
					thisForm.olDate.set("invalidMessage","Offering Latter Date must not be greater than 300 days old.");					
					
					return false;
				}
				
				return true;

			}
	
		
		}


	});
	return OfferingLetterDialog;

});
