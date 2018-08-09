define(["dojo/_base/lang",
        "ecm/model/Action", 
        "ecm/model/Request", 
        "dojo/_base/declare", 
		"ecm/widget/dialog/BaseDialog",
		"dojo/json",
		"gilCnPluginDojo/util/JsonUtils",
		"dojo/text!./templates/SerialNumberDialog.html",
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
	    "dojo/parser",
	    "dojo/dom-attr",
	    "gilCnPluginDojo/AptsDialog",
	    "gilCnPluginDojo/NonModalConfirmDialog",
	    "dojo/i18n",
	    "dojo/number",
	    "dijit/form/Button",
	    "gilCnPluginDojo/ConfirmationDialog",
	    "dojox/string/Builder",
	    "gilCnPluginDojo/DialogMessages",
	    "dijit/Tooltip",
	    "dijit/focus",
	    "dojo/_base/connect", 
	    "dojo/_base/event",
	    "dojox/grid/DataGrid",
	    "dojo/data/ItemFileWriteStore",
	    "dojo/_base/event",
	    "dojo/_base/array",
	    "dojo/io-query",
	    "dojo/dom-class",    
	    "dojox/grid/DataGrid",
	    "dojo/data/ItemFileWriteStore",
	    "dojo/dom",
	    "dijit/focus",
	    "dojo/store/DataStore",
	    "gilCnPluginDojo/GILDialog",
	    "dojox/form/Uploader",
	   "dojox/form/uploader/plugins/Flash",
	   "dojox/form/uploader/FileList",
	   "dojo/keys", "dojo/on",
	],
	
	function(lang, action, request,declare,	BaseDialog,	json,JsonUtils, template, array, dom,domReady,form,date,
			locale,domStyle,registry,query,construct,parser,domattr,AptsDialog,NonModalConfirmDialog,i18n,
			number,Button,ConfirmationDialog,Builder,DialogMessages,Tooltip,focus,connect, event,DataGrid,
			ItemFileWriteStore,event,array,ioquery,domclass,DataGrid,ItemFileWriteStore,dom,focusUtil,
			DataStore,GILDialog,Uploader,Flash,keys,on) {

		var SerialNumberDialog = declare("gilCnPluginDojo.SerialNumberDialog",[BaseDialog,GILDialog], {
		
		contentString: template,
		widgetsInTemplate: true,
		jsonSerialList : null,
		jsonSerialNumber : null,
		jsonImportSerialNumberList:null,
		jsonSerialNumbers: [],
		localeBundle:null,
		LineItemsDialog: null,
		serviceParameters:null,
		serialNumberGrid:null,
		storeSerialNumberGrid:null,
		snFieldsAmt:30,
		rowCount:0,
		
		constructor: function(args){	
			lang.mixin(this, args);    
		},	

		postCreate: function() {
		
			this.inherited(arguments);
//			this.serviceParameters = {};
			this.setSize(800, 530);
			this.setResizable(false);
			this.setMaximized(false);
			this.addButton("Cancel", "cancel", false, true,"cancelButton");
			this.addButton("Ok", "ok", false, true,"okButton");
			
			this.setTitle("Serial Number Creation");
			this.setWidgetsInitialization();
			this.setCustomizedValidation();
			this.upperCaseFields();
			this.handleEnter(this.serialNumber.id);
//			this.handleBackSpace();
	
		},
		
		handleEnter: function(fieldId) {
			dojo.connect(dojo.byId(fieldId),"keydown", lang.hitch(this, function (evt) {
				if (evt.keyCode == dojo.keys.ENTER) {
					evt.stopPropagation();
					evt.preventDefault();
					registry.byId(fieldId).focus() 
			    	this.addSerial();
			    }	
				}));
		},
//		handleBackSpace: function() {
//			dojo.connect(dojo.byId(this.id),"keydown", lang.hitch(this, function (evt) {
//				if (evt.keyCode == dojo.keys.BACKSPACE) {
//					evt.stopPropagation();
//					evt.preventDefault();
////					registry.byId(fieldId).focus() 
////			    	this.addSerial();
//			    }	
//				}));
//		},

		
		upperCaseFields: function() {
			
			var allValTextbox = dojo.query('input#[id^="'+ this.id +'_"]');
			for(var i=0; i<allValTextbox.length; i++){
				this.toUpperCase(allValTextbox[i]);
			}
		},

		
		importSerial : function(){
			
			var file = this.importFile._files[0];
			var mimetype = file.type;
			var parm_part_filename = (file.fileName ? file.fileName : file.name)

			var formData = new FormData();
			formData.append('frontEndAction', 'importSerial');
			formData.append('file', file);
			formData.append('mimetype', file.type);
			formData.append('fileName', parm_part_filename); 
			var pluginParams = {
					    requestCompleteCallback: lang.hitch(this, function (response) {
					    	var dialogMessagesResponse = new DialogMessages();
					    	this.jsonImportSerialNumberList	= JSON.parse(response.snImportJsonList);
					    	this.fillSerialNumberGrid(this.jsonImportSerialNumberList);
					    	this.numberAvailable.set('value',this.rowCount);
					    	this.importFile.focusNode.value="";
					    })
					}; 
			 request.postFormToPluginService("GilCnPlugin", "SerialNumberELSService", formData, pluginParams);
		},


		setWidgetsInitialization: function() {
			
			var packageName = "dojo.cldr";
		    var bundleName = "gregorian";    
		    var localeName = dojo.locale; 
		    this.localeBundle = i18n.getLocalization(packageName, bundleName, localeName);
			dojo.style(this.cancelButton.domNode,"display","none"); 
			
			var txtBox1 = null;
			var txtBox2 = null;
			var thisForm = this;
			
			this.numberToAdd.set('value',this.LineItemsDialog.qty.get("value") );
			
		    var tr = construct.create("tr", {}, "serialNumberTb");
	        var td = construct.create("td",tr);
	        var label = construct.create("label", {innerHTML: 'Start:', style:'margin-right: 18px;','for':'Start' }, tr, 'first');
	        for(var i = 0; i < this.snFieldsAmt; i++) {
	        	 txtBox1 = new ecm.widget.ValidationTextBox({   
		             'class': 'serialStartEnd',
		             name: 'serialStart'+i, 
		             title: 'serialStart'+i,
		             idNum: i,
		             maxlength:1,
		             id:thisForm.id+"_"+'serialStart'+i,
		             title: 'serialStart'+i,
		             tabindex:i+1,
		             onKeyPress: function(evt){
		            	 
		 				if (evt.keyCode == dojo.keys.DELETE) {
		 					registry.byId(thisForm.id+"_"+'serialStart'+(this.idNum)).set('value',null);
		 					document.getElementById(thisForm.id+"_"+'serialStart'+(this.idNum+1)).focus();
							evt.stopPropagation();
							evt.preventDefault();
		 				} else {
	 						if ((this.idNum+1) == thisForm.snFieldsAmt) {
	 							document.getElementById(thisForm.id+"_"+'serialEnd0').focus();
	 							return;
	 						}
	 						var val = registry.byId(thisForm.id+"_"+'serialStart'+(this.idNum+1)).get('value');
							registry.byId(thisForm.id+"_"+'serialStart'+(this.idNum+1)).set('value',val);
							document.getElementById(thisForm.id+"_"+'serialStart'+(this.idNum+1)).focus();
		 				}
 					}
		             }).placeAt(tr, 'last');
			}
	        
		    var tr = construct.create("tr", {}, "serialNumberTb");
	        var td = construct.create("td",tr);
	        var label = construct.create("label", {innerHTML: 'End:',style:'margin-right: 23px;', 'for':'End' }, tr, 'first');
	        for(var i = 0; i < this.snFieldsAmt; i++) {
	        	 txtBox2 = new ecm.widget.ValidationTextBox({   
		             'class': 'serialStartEnd',
		             name: 'serialEnd'+i, 
		             title: 'serialEnd'+i,
		             idNum: i,
		             maxlength:1,
		             id:thisForm.id+"_"+'serialEnd'+i,
		             title: 'serialEnd'+i,
		             tabindex:i+1,
		             onKeyPress: function(evt){
		            	 if (evt.keyCode == dojo.keys.DELETE) {
								registry.byId(thisForm.id+"_"+'serialEnd'+(this.idNum)).set('value','');
								document.getElementById(thisForm.id+"_"+'serialEnd'+(this.idNum+1)).focus(); 
		            	 } else {
	 						if ((this.idNum+1) == thisForm.snFieldsAmt) return;
	 						var val = registry.byId(thisForm.id+"_"+'serialEnd'+(this.idNum+1)).get('value');
							registry.byId(thisForm.id+"_"+'serialEnd'+(this.idNum+1)).set('value',val);
							document.getElementById(thisForm.id+"_"+'serialEnd'+(this.idNum+1)).focus();
		            	 }
 					}
		             }).placeAt(tr, 'last');
			}
	        
		    var tr = construct.create("tr", {}, "serialNumberTb");
	        var td = construct.create("td",tr);
	        var label = construct.create("label", {innerHTML: 'Change:', style:'margin-right: 5px;', 'for':'Change' }, tr, 'first');
	        for(var i = 0; i < this.snFieldsAmt; i++) {
	        	var chkBox = new dijit.form.CheckBox({   
		             'class': 'serialChange',		             
		             name: "change"+i,
		             id:thisForm.id+"_"+'serialChange'+i,
		             checked: false,
		             id:thisForm.id+"_"+'serialChange'+i,
		             value:"true"}).placeAt(tr, 'last');
			} 
		    var layout = [[
			    		    {'name': 'Type', 			 'field': 'type', 		  'width': '70px'},
			    		    {'name': 'Model', 		 	 'field': 'model', 		  'width': '70px'},
			    		    {'name': 'Serial Numbers', 	 'field': 'serialNumber', 'width': '208px'},
			    		    {'name': 'Unit Price', 	 	 'field': 'unitPrice', 	  'width': '95px'},

			    		 ]];
		    	    this.serialNumberGrid = new DataGrid({
		    	        store: thisForm.storeSerialNumberGrid,
		    	        selectionMode:'multiple',
		    	        structure: layout,
		    	        rowSelector: '1px',});
		    	    this.serialNumberGrid.placeAt("idSerialNumberGrid");
		    	    this.serialNumberGrid.startup();
    
		},
		
		cancel: function() {
			try {
				this.destroyRecursive();
				this.jsonSerialNumbers=[];
			}catch(e){
				thisDialog.destroyRecursive();
				thisDialog.jsonSerialNumbers=[];
			}
			
			
		},
		
		fillSerialNumberGrid: function(snList) {
			
			var data = {  items: [] };
			var arr = [];
			for (var i in snList) {
				
				this.rowCount++;
				var newItem = {	type:snList[i].TYPE,
								model:snList[i].MODEL, 
								serialNumber:snList[i].SERIALNUMBER, 
								unitPrice:this.formatNumber((snList[i].UNITPRICE == 0 ? "": snList[i].UNITPRICE),2),
							};
				
			
				if (this.serialNumberGrid!=null && this.serialNumberGrid.store!=null){
					this.serialNumberGrid.store.newItem(newItem);
				} else {
					  this.storeSerialNumberGrid = new ItemFileWriteStore({data:data});
					  this.storeSerialNumberGrid.newItem(newItem);
				}
			
			}
		    this.serialNumberGrid.set("store", this.storeSerialNumberGrid);
		    this.serialNumberGrid._lastScrollTop=this.serialNumberGrid.scrollTop;
		    this.serialNumberGrid._refresh();

		},
		
		checkDuplicate: function(addingSN) {
			
			var isDuplicate = false;
			var check = function(items, request){
						  for(var i = 0; i < items.length; i++){
							  isDuplicate = true;
						  }
						};
				
				if (this.serialNumberGrid!=null && this.serialNumberGrid.store!=null) {
					this.serialNumberGrid.store.fetch({query: {serialNumber:addingSN}, queryOptions: {ignoreCase: true}, onComplete: check});
				}
				return isDuplicate;
		},
		noConfirm: function() {

			
		},
		
		/***
	    *				   
	    * Usage : Check which fields have been filled and add the serial  number sequence.
	    * if field serial number is filled, uses that one, otherwise calculate the serial
	    * number sequence based on what was filled in start, end and change fields.
	    * 
	    * */
    	addSerial: function() {
    		var thisForm = this;
    		this.serviceParameters['frontEndAction'] = arguments.callee.nom;
    		var snToAdd = this.serialNumber.get('value');
    		var dlgMsg = new DialogMessages();
    		if (snToAdd!=null && snToAdd.trim()!=''){
    			
    			if( this.checkDuplicate(snToAdd) ) {
    				dlgMsg.addErrorMessages("Duplicate Serial");
    				this.showMessage("Error",dlgMsg.getAllMessages(),false,null,null);
    			}
    			
    			this.jsonSerialList = [{TYPE:this.LineItemsDialog.newLineItem.TYPE, MODEL:this.LineItemsDialog.newLineItem.MODEL, SERIALNUMBER:snToAdd, UNITPRICE:this.LineItemsDialog.newLineItem.UNITPRICE},];
    			this.fillSerialNumberGrid(this.jsonSerialList);
    			this.numberAvailable.set('value',this.rowCount);
    	  		var qty = this.LineItemsDialog.qty.get("value");
        		this.updateLineItemsSerialNumbers();
    			
    		} else {
    			
	    		this.formatSerialStart();
	    		this.formatSerialEnd();
	    		var anyChange = this.formatSerialChange();
	    		if(!anyChange) {
    				dlgMsg.addErrorMessages("You must select at least one changing field");
    				this.showMessage("Error",dlgMsg.getAllMessages(),false,null,null);
    				return
	    		}
	    		
	    		this.formatQuantity();
				this.formatIncludeVowels();
				this.serviceParameters['type'] = this.LineItemsDialog.newLineItem.TYPE;
				this.serviceParameters['model'] = this.LineItemsDialog.newLineItem.MODEL;
				this.serviceParameters['unitprice'] = this.LineItemsDialog.newLineItem.UNITPRICE;
				this.generateSerials();

    		}
		},
		
		/***
		*				   
		* Usage : Generate Serial Numbers using fields start, end and change.
		* 
		* */
		generateSerials: function() {
			
			this.serviceParameters['frontEndAction'] = "addSerial";
			request.postPluginService("GilCnPlugin", "SerialNumberELSService",'application/x-www-form-urlencoded', {
	    		requestBody : ioquery.objectToQuery(this.serviceParameters),
			    requestCompleteCallback : lang.hitch(this, function (response) {
	    			
			    	var dialogMessagesResponse = new DialogMessages();
			    	this.jsonSerialList	= JSON.parse(response.snJsonList);
			    	this.jsonSerialNumber = JSON.parse(response.snJson);
			    	
					if(this.jsonSerialNumber.dialogErrorMsgs.length>0){
						var msgs=this.jsonSerialNumber.dialogErrorMsgs;
						for (var i in msgs) {
							dialogMessagesResponse.addErrorMessages(msgs[i]);
						}
						this.showMessage("Error",dialogMessagesResponse.errorMessages(),false,null,null);
					} 
	    			
	    			if(this.jsonSerialNumber.validationNotEnoughDigForSN) {
	    				dialogMessagesResponse.addErrorMessages("Not enought digits to increment serial number.")
	    				this.showMessage("Error",dialogMessagesResponse.errorMessages(),false,null,null);
	    				return;
	    			}
					
	    			if(this.jsonSerialNumber.validationEndSerialBeforeQtyReached) {
	    				dialogMessagesResponse.addWarningMessages("End serial number reached before quantity reached");
	    				this.showMessage("Warning",dialogMessagesResponse.warningMessages(),false,null,null);
	    			}
	    			
	    			this.fillSerialNumberGrid(this.jsonSerialList);
	    			this.numberAvailable.set('value',this.rowCount);

	    		})
			});	
			
			
			
		},
		
		/***
		*				   
		* Usage : Prepare Serial Number objects to be added in Line items screens.
		* */
		getSerials: function(close) {
			
			this.serviceParameters['frontEndAction'] = arguments.callee.nom;
			var thisForm = this;
			request.postPluginService("GilCnPlugin", "SerialNumberELSService",'application/x-www-form-urlencoded', {
	    		requestBody : ioquery.objectToQuery(this.serviceParameters),
			    requestCompleteCallback : lang.hitch(this, function (response) {
			    	
			    	this.jsonSerialNumber=JSON.parse(response.snJson);
			    	var dialogMessagesResponse=new DialogMessages();
					if(this.jsonSerialNumber.dialogErrorMsgs.length>0){
						var msgs=this.jsonSerialNumber.dialogErrorMsgs;
						for (var i in msgs) {
							dialogMessagesResponse.addErrorMessages(msgs[i]);
						}
		    			if (dialogMessagesResponse.hasErrorMessages()) {
		    				this.showMessage("Error",dialogMessagesResponse.getAllMessages(),false,null,null);
		    				return;
		    			}
					}

					this.LineItemsDialog.jsonSerialNumbers=JSON.parse(response.snObjsJsonList);
					this.LineItemsDialog.addItemSerials();
					this.LineItemsDialog.renumberAndSpread(thisDialog.clearFields);
    	        	this.jsonSerialNumbers=[];
    	        	if(close){
    	        		this.cancel();
    				}
    	        	
//    	        	this.cancel();

	    		})
			});	
			
		},

    	deleteSerial: function() {
    		
    		var thisForm = this;
    		var storeAux = null;
            var items = thisForm.serialNumberGrid.selection.getSelected();
            if(items.length) {
            	array.forEach(items, function(selectedItem){
                    if(selectedItem !== null){
                    	thisForm.serialNumberGrid.store.deleteItem(selectedItem);
                    	thisForm.rowCount--;
                    } 
                });
            	setTimeout(function(){ thisForm.serialNumberGrid.setStore(thisForm.serialNumberGrid.store);}, 1000); 
            	thisForm.serialNumberGrid._refresh();
            }
            this.numberAvailable.set('value',this.rowCount);
		},
		
		formatIncludeVowels: function() {
			this.serviceParameters['includeVowels'] =  dijit.byId(this.id+"_vowels").checked;
		},
		
		formatQuantity: function() {
			this.serviceParameters['quantity'] = this.LineItemsDialog.qty.get("value");
		},
		
		formatSerialStart: function() {
	      	 
	      	  var serialStartArr = [];
	    	  var allSerialStart = dojo.query('[id^="'+this.id+'_serialStart"]');
	    	  var i = 0;
	    	  allSerialStart.forEach(function(field) {
			      var widget = dijit.getEnclosingWidget(field);
			      var value = widget.get("value");
			      serialStartArr[i] = value!=null && value.trim() == "" ? "\xa0" : value;
			      i++;
			    });
	    	  this.serviceParameters['serialStart'] = JSON.stringify(serialStartArr);
		},
		
		formatSerialEnd: function() {

	      	  var serialEnd = "";
	    	  var allSerialEnd = dojo.query('[id^="'+this.id+'_serialEnd"]');
	    	  var i = 0;
	    	  allSerialEnd.forEach(function(field) {
			      var widget = dijit.getEnclosingWidget(field);
			      if(widget.get("value")==null || widget.get("value").trim()=='')
			    	  serialEnd = serialEnd + " ";
			      else {
			    	  serialEnd = serialEnd + widget.get("value");
			      }
			      i++;
			    }); 
	    	  this.serviceParameters['serialEnd'] = serialEnd;	
		},
		
		formatSerialChange: function() {

	      	  var serialChangeArr = [];
	    	  var allSerialChange = dojo.query('[id^="'+this.id+'_serialChange"]');
	    	  var anyChange = false;
	    	  var i = 0;
	    	  allSerialChange.forEach(function(field) {
			      var widget = dijit.getEnclosingWidget(field);
			      serialChangeArr[i] = widget.checked;
			      if(widget.checked) anyChange = true;
			      i++;
			    });
	    	  
	    	  this.serviceParameters['serialChange'] = JSON.stringify(serialChangeArr);	
	    	  return anyChange;
		},
		
		
    	checkAll: function() {
    		
    	  var thisForm = this;
    	  var allChgCheckBox = dojo.query('[id^="'+thisForm.id+'_serialChange"]'); 
		  allChgCheckBox.forEach(function(field) {
		      var widget = dijit.getEnclosingWidget(field);
		      widget.set("checked",true);
		    });
		  
		},
		
    	uncheckAll: function() {

      	  var thisForm = this;
    	  var allChgCheckBox = dojo.query('[id^="'+thisForm.id+'_serialChange"]'); 
		  allChgCheckBox.forEach(function(field) {
		      var widget = dijit.getEnclosingWidget(field);
		      widget.set("checked",false);
		    });	
		},
		
    	checkUsed: function() {
    		
	      	  var thisForm = this;
	    	  var allSerialStart= dojo.query('[id^="'+thisForm.id+'_serialStart"]'); 
	    	  var allSerialEnd = dojo.query('[id^="'+thisForm.id+'_serialEnd"]'); 
	    	  var allSerialChange = dojo.query('[id^="'+thisForm.id+'_serialChange"]'); 
	    	  
	    	  for (var i= 0;i<30;i++){ 
	    		  var widget1 = dijit.getEnclosingWidget( allSerialStart[i]);
	    		  var widget2 = dijit.getEnclosingWidget( allSerialEnd[i]);
	    		  if((widget1.get("value")!=null && widget1.get("value")!='') || (widget2.get("value").trim()!=null && widget2.get("value").trim()!='') ){
	    			  dijit.getEnclosingWidget(allSerialChange[i]).set("checked",true);
	    		  } else {
	    			  dijit.getEnclosingWidget(allSerialChange[i]).set("checked",false);
	    		  }
	    	  }	
		},
		
    	ok: function() {

    		var thisForm = this;
    		var dlgMsg = new DialogMessages();
    		thisForm.jsonSerialNumbers = [];
    		var qty = this.LineItemsDialog.qty.get("value");
    		thisForm.updateLineItemsSerialNumbers();
	        var matchingSerialCount = this.LineItemsDialog.countMatchingSerials();
	        
	        if ((qty > 0) && (qty > matchingSerialCount)){
	        	dlgMsg.addConfirmMessages("Not enough serial numbers generated. Proceed?");
	        	this.showMessage("Confirm",dlgMsg.confirmMessages(),true ,thisForm.yesConfirm, thisForm.noConfirm);
	        } else {
	        	
	        	this.getSerials(true);//true means to close

	        }
	        
	        this.jsonSerialNumbers=[];
		},
		
		yesConfirm: function() {
			thisForm.updateLineItemsSerialNumbers();
			thisForm.getSerials(true);	
		},
		
		updateLineItemsSerialNumbers: function(){
			this.jsonSerialNumbers=[]; //clear listvalues will be added by
    		var store = this.serialNumberGrid.store;
    		this.serialNumberGrid.store.fetch( {
						               onItem: lang.hitch(this, function (item) {
								            	   this.jsonSerialNumbers.push({
								            		   TYPE: 			store.getValue( item, 'type' ),
								            		   MODEL: 			store.getValue( item, 'model' ),
								            		   SERIALNUMBER: 	store.getValue( item, 'serialNumber' ),
								            		   UNITPRICE: 		this.getUnitPrice(store.getValue( item, 'unitPrice' ))
								            	   });
						               })
    		});
    		this.serviceParameters['jsonSerialNumbers'] = JSON.stringify(this.jsonSerialNumbers);
    		this.LineItemsDialog.jsonSerialNumbers = this.jsonSerialNumbers;
		},
		
		
		getUnitPrice: function(up) {
			
     	   var unitPrice = parseFloat(up);
    	   if(!isNaN(unitPrice)){
    		   unitPrice = parseFloat(unitPrice);
    	   } else {
    		   unitPrice = 0;
    	   }
    	   return unitPrice;
    	   
		},
		
		show: function() {
			this.inherited("show", []);
		},

		setCustomizedValidation : function() {
					
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
		            	
		            	callbackYes();;
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
			            	
			            	d1.destroyRecursive();; 
			            	
			            } });
			        
			        buttonOk.placeAt(actionBar);
			        domclass.add(buttonOk.focusNode, "yesNoOkButton");
			        
			      var buttonCancel = new dijit.form.Button({"label": "Cancel", 
			            onClick: function(){
			            	
			            	d1.destroyRecursive();;
			            	
			            	} });
			        
			        buttonCancel.placeAt(actionBar);
			        domclass.add(buttonCancel.focusNode, "cancelButton")
            }
            
	        d1.startup();
            d1.show();
			
		},

	});
		
		return SerialNumberDialog;
});

