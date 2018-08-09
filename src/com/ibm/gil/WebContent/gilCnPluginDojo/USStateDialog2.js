define(["dojo/_base/lang",
        "ecm/model/Action", 
        "ecm/model/Request",
        "ecm/model/Desktop",
        "dojo/_base/declare",
        "ecm/widget/dialog/BaseDialog",
        "gilCnPluginDojo/GILDialog",
	    "dojo/data/ItemFileWriteStore",
	    "dojo/dom",
	    "dojo/_base/array",
	    "gilCnPluginDojo/NonModalConfirmDialog",
	    "gilCnPluginDojo/DialogMessages",
	    "dijit/registry",
	    "dijit/form/SimpleTextarea",
	    "dijit/form/Button",
	     "dojo/dom",
	     "dojo/on",
	     "dojo/_base/connect",
	     "dijit/form/Form",
	     "dojo/dom-style",
	     "dojo/dom-class",
		 
		 "dojo/ready",
		 "dojo/dom-construct",
	     "dojo/aspect",	    
	     "gilCnPluginDojo/DialogMessages",
	     "dojo/text!./templates/USStateDialog.html",
	     "dojo/io-query"
	     ],	
		function(lang, Action, Request, Desktop, declare, BaseDialog, GILDialog, 
				 ItemFileWriteStore, dom, array, NonModalConfirmDialog,
				DialogMessages, registry, SimpleTextarea, Button, dom, on, connect, Form,
				domStyle, domclass, ready, construct, aspect,  DialogMessages, template, ioquery){


	return declare("gilCnPluginDojo.USStateDialog2", [ BaseDialog, GILDialog ], {

	
		contentString: template,
		widgetsInTemplate: true,
		callerDialog: null,
		jsonObject:null,
		jsonUsStates:null,
		serviceParameters:null,
		thisDialog:null,
		    
		constructor: function(args){	
			lang.mixin(this, args);
			numComments = jsonInvoiceEls.comments.length;
		},
	
		postCreate: function() {
			thisDialog=this;
			this.inherited(arguments);
			this.setMaximized(false);
			dojo.style(this.cancelButton.domNode,"display","none");
			this.setTitle("US State Selection");
			this.addButton("Cancel", "cleanValues", false, true);
			this.addButton("OK", "saveState", false, true);
			this.showActionBar(true);
			this.setSize(400,310);
			this.setResizable(true);
			this.setMaximized(false);
			this.initialize();
			
			this.setLayout();
			
    	},
    	ready:function(){
    		if(!domclass.contains(dijit.byId(this.contentArea).domNode,'usStateOverflow')){
				domclass.add(dijit.byId(this.contentArea).domNode, 'usStateOverflow');
				
			}
    	},
				
		setSize:function (width, height) {
			if (!this._sizeToViewportRatio) {
				domStyle.set(this.domNode, {width:width + "px", height:height + "px",});
				
			}
			this.resize();
		},
		
		setLayout: function(){			
			//DataGrid
			var resizeGrid = (foo) => {
				if(foo != null){
					dojo.style(this.contentArea, 'overflow', 'scroll !important');
					console.log("resized");
					
					
					
				}
			};

			aspect.after(this.resizeHandle, 'onResize', resizeGrid, this.domNode);
			aspect.after(this._maximizeButton, 'onClick', resizeGrid, this.domNode);
			aspect.after(this._restoreButton, 'onClick', resizeGrid, this.domNode);
			
//			domclass.add(dijit.byId(this.contentArea).domNode, 'usStateOverflow');
		},
		
		initialize:function(){


			thisDialog.serviceParameters['request']=arguments.callee.nom;
			//clean server error messages
			
//			this.serviceParameters['jsonInvoiceEls']= JSON.stringify(jsonInvoiceEls);
//			
			var msg=new DialogMessages();
			ecm.messages.progress_message_USStateService = "Loading States...";
			 
				
			Request.postPluginService("GilCnPlugin","USStateService",'application/x-www-form-urlencoded',{
				
				requestBody : ioquery.objectToQuery(thisDialog.serviceParameters),
				requestCompleteCallback: lang.hitch(this,function(response) {
					debugger;
					
					jsonUsStates=JSON.parse(response.usStatesJson);


					if(jsonUsStates.dialogErrors.length>0){
						var msgs=jsonUsStates.dialogErrors;
						for (var i in msgs) {
							msg.addErrorMessages(msgs[i]);
						}
					}
					if(msg.hasErrorMessages()){
						
						thisDialog.showMessage("Error",msg.errorMessages(),null,null);
						
						
						return false;
					}else{
						thisDialog.jsonObject['toleranceAmt']=jsonUsStates.toleranceAmt;
						thisDialog.createStatesCheckboxes();
						
						return true;
					}


				})
			
			});

//			 dojo.place("<label for='"+thisDialog.id+"_state_"+state+"' style='visibility:hidden'>"+state+"</label><br>", dijit.byId(thisDialog.id+"_state_"+state).domNode, "after");
//			this.invPicker.set('options',thisDialog.jsonObject.refListData);
//			var value="";
//			for (var i in thisDialog.jsonObject.refListData) {
//	    	    if (thisDialog.jsonObject.refListData[i].selected == true)  value=(thisDialog.jsonObject.refListData[i]["value"]);
//	    	}
//			this.invPicker.set('value', value);

			
		},
		cancelY:function(){
			thisDialog.destroyRecursive();
		},
		cancelN:function(){
			//nothing to do
		},
		cleanValues: function() {
			var dialogMsgs=new DialogMessages();
			dialogMsgs.addConfirmMessages("You have not selected the US State.\nAre you sure you want to quit?");
			
			thisDialog.showWarning("Confirm",dialogMsgs.confirmMessages(),true,thisDialog.cancelY,thisDialog.cancelN);
//			if(thisDialog.picked==null){
//				var dialogMessages=new DialogMessages();
//				dialogMessages.addErrorMessages("Reference Invoice must be selected");
//				thisDialog.showMessage("Error",dialogMessages.errorMessages());
//				thisDialog.jsonObject['referenceInvNum']="";
//				thisDialog.callerDialog.refInvNum.set('value',"");
//			}
//			
			

		},
	//	
		saveState : function() {
		
			if(thisDialog.validateInput()){
				thisDialog.jsonObject['stateCode']=jsonUsStates.state;	
//				thisDialog.jsonObject['validState']="Y";	
				
				
				thisDialog.serviceParameters['request']=arguments.callee.nom;
				//clean server error messages
				
				this.serviceParameters['jsonInvoiceEls']= JSON.stringify(thisDialog.jsonObject);
//				
				var msg=new DialogMessages();
				ecm.messages.progress_message_InvoiceELSService = "Retrieving tax information. <br> Please wait.";
				 
					
				var m=dojo.byId("ecm_widget_dialog_StatusDialog_0_message");
				m.title="Retrieving tax info.";
				m.innerText="Retrieving tax information."+"\n"+"Please wait.";
				
				Request.postPluginService("GilCnPlugin","InvoiceELSService",'application/x-www-form-urlencoded',{
						
						requestBody : ioquery.objectToQuery(thisDialog.serviceParameters),
					requestCompleteCallback: lang.hitch(this,function(response) {
						debugger;
						
						thisDialog.jsonObject=JSON.parse(response.invoiceElsJson);


						if(thisDialog.jsonObject.dialogErrorMsgs.length>0){
							var msgs=thisDialog.jsonObject.dialogErrorMsgs;
							for (var i in msgs) {
								msg.addErrorMessages(msgs[i]);
							}
						}
						if(msg.hasErrorMessages() ){
							
							thisDialog.showMessage("Error",msg.errorMessages(),thisDialog.finish,null,null);

							
						}else {
							
//							msg.addInfoMessages("Successful response from CTS");
//							thisDialog.showMessage("Info",msg.infoMessages(),
									
									thisDialog.isTolerance();
							
							
						}

					})
				
				});
			}
			},
			isTolerance:function(){
				var msg2=new DialogMessages();
				//add state code that will be already checked next time user opens US State dialog.
				thisDialog.callerDialog.usTaxStateSelected=thisDialog.jsonObject.stateCode;
				
				if(thisDialog.jsonObject.toleranceIndc=="N"){
					msg2.addErrorMessages("Taxes on the invoice are not within the states tax tolerance values.");
					thisDialog.showMessage("Error",msg2.errorMessages(),thisDialog.finish,null);
				}else{
					
					//loading new netamount into net amt field
					//loading invoice retrieved 
					jsonInvoiceEls=thisDialog.jsonObject;
					if(thisDialog.jsonObject.netAmount<0){
						thisDialog.callerDialog.netAmt.set('value',"0.00");
						jsonInvoiceEls.netAmount="0.00";
					}else{
						thisDialog.callerDialog.netAmt.set('value',thisDialog.jsonObject.netAmount);
						thisDialog.callerDialog.invAmt.set('value', thisDialog.jsonObject.totalAmount);
						thisDialog.callerDialog.taxAmt.set('value', thisDialog.jsonObject.vatAmount);
						
						thisDialog.callerDialog.lnItemTot.set('value', thisDialog.jsonObject.lineItemTotalAmount);
						thisDialog.callerDialog.lnItemTxTot.set('value', thisDialog.jsonObject.lineItemVatAmount);
						thisDialog.callerDialog.lnItemNetTot.set('value', thisDialog.jsonObject.lineItemNetAmount);
					}
					
					thisDialog.finish();
//					thisDialog.callerDialog.loadBasicFields();
				}
			},
			finish:function(){
				thisDialog.destroyRecursive();
//				if(document.getElementById(thisDialog.id+"_underlay")!=null)
//					document.getElementById(thisDialog.id+"_underlay").remove();
//			document.getElementById(thisDialog.id).remove();
			},
			validateInput: function(){
				debugger;
				var states=jsonUsStates.stateCodeResults;
				var stateSelected="";
				var count=0;
				
				for(var i in states ){
				
					var checkBox=dijit.byId(thisDialog.id+"_state_"+states[i]);
					if(checkBox.get('value') ){
						stateSelected=states[i];
						count++;
						
					}
					
				}

				var checkBox=dijit.byId(thisDialog.id+"_state_OT");
				if(checkBox.get('value') ){
					stateSelected="OT";
					count++;
					
				}
				
				if(count==0){
					var dialogMsgs=new DialogMessages();
					
					dialogMsgs.addErrorMessages("Please select the state.");
					
					thisDialog.showMessage("Error",dialogMsgs.errorMessages(),null,null);
					console.log("0 checkboxes selected");
					return false;
				}
				if(count>1){
					var dialogMsgs=new DialogMessages();
					dialogMsgs.addErrorMessages("More than one State Selected.");
					thisDialog.showMessage("Error",dialogMsgs.errorMessages(),null,null);
					console.log("More  than one checkbox selected");
					return false;
				}
				jsonUsStates['state']=stateSelected;
				return true;
				
			},
			createStatesCheckboxes:function(){
				thisDialog.show();
				
				var underlay = dojo.byId(thisDialog.id+"_underlay");
				domclass.add(underlay, "lightOpacity");
				 
				
				var states=jsonUsStates.stateCodeResults;
				var state="";
				dojo.style(this.contentArea, 'overflow', 'scroll !important');
				var checkFlag=false;
				for(var i in states){
					state=states[i];
					if(thisDialog.callerDialog.usTaxStateSelected!= null && thisDialog.callerDialog.usTaxStateSelected==state){
						checkFlag=true;
					}
					
//					_label =construct.toDom("  <label  id='${id}_lblstate_"+state+"' for='${id}_state_"+state+"'>"+state+":</label>&nbsp;");
					console.log("<br><label for='"+thisDialog.id+"_state_"+state+"'>"+state+"</label>");
					console.log(thisDialog.id);
//					dojo.place("<br><label for='"+thisDialog.id+"_state_"+state+"'>"+state+"</label>", dojo.byId(thisDialog.id+"_usStatePane"), i);/*thisDialog.id+"_statesList"*/
			        _widget	 = new ecm.widget.CheckBox({
			        	
			        	id:thisDialog.id+"_state_"+state, 
			        	name:"checkbox"+ i,
			        	value:"true",
			        	checked: checkFlag,
			        	
			          }).placeAt(thisDialog.id+"_usStatePane");
			        //clean flag
			        checkFlag=false;
			        console.log("i: "+i+" state: "+state);
			        registry.byId(thisDialog.id+"_state_"+state).startup();
//			        if(i==0) dojo.place("<ul>", dojo.byId(thisDialog.id+"_state_"+state), "before");
//			        dojo.place("<li>",  dijit.byId(thisDialog.id+"_state_"+state).domNode, "before");
			        dojo.place("<label for='"+thisDialog.id+"_state_"+state+"'>"+state+"</label><br><br><br>", dijit.byId(thisDialog.id+"_state_"+state).domNode, "after");
			        
				}
				
				 _widget	 = new ecm.widget.CheckBox({
			        	
			        	id:thisDialog.id+"_state_OT", 
			        	name:"checkbox"+ states.length,
			        	value:"true",
			        	checked: false,

			          }).placeAt(thisDialog.id+"_usStatePane");

				 registry.byId(thisDialog.id+"_state_"+state).startup();
				 dojo.place("<label for='"+thisDialog.id+"_state_OT'>OT</label><br><br><br>", dijit.byId(thisDialog.id+"_state_OT").domNode, "after");
				 
				 
			},
			
			showMessage: function(titl, msg, callback) {	
				
				
	            var d1 = new NonModalConfirmDialog({
	                title: titl, 
	                style: "min-width:350px;",
	                content:msg,
	            })
	            

	            
//				        var actionBar = dojo.create("div", {"class": "dijitDialogPaneContentArea",}, d1.containerNode);
				        var actionBar = dojo.create("div", { "class": "dijitDialogPaneActionBar"  }, d1.containerNode);
	            if(callback!=null ){
			       var buttonOk= new dijit.form.Button({"label": "OK",
			            onClick: function(){
			            	callback();
		
			            	d1.destroyRecursive();  
			            	
			            } });
			       buttonOk.placeAt(actionBar);
			        domclass.add(buttonOk.focusNode, "yesNoOkButton");
			        
	            }else
	            {
	            	var buttonOk=new dijit.form.Button({"label": "OK",
			            onClick: function(){
			            	
			            	d1.destroyRecursive(); 
			            	
			            } });
	            	buttonOk.placeAt(actionBar);
			        domclass.add(buttonOk.focusNode, "yesNoOkButton");
	            }
//				        new dijit.form.Button({"label": "Cancel",
//				            onClick: function(){
//				            	
//				            	d1.destroyRecursive();
//				            	
//				            	} }).placeAt(actionBar);
//	       
	            
		        d1.startup();
	            d1.show();
				
			},
			
	showWarning: function(titl, msg, needConfirmation, callbackYes, callbackNo) {	
				
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
		
			        var buttonYes= new dijit.form.Button({ "label": "Yes",
			            onClick: function(){
			            	
			            	callbackYes();
			            	d1.destroyRecursive();  
			            	
			            }});
			        buttonYes.placeAt(actionBar);
			        domclass.add(buttonYes.focusNode, "yesNoOkButton");
			        
			        var buttonNo=new dijit.form.Button({"label": "No",
			            onClick: function(){
			            	
			            	callbackNo();
			            	d1.destroyRecursive(); 
			            	
			            	}});
			        buttonNo.placeAt(actionBar);
			        domclass.add(buttonNo.focusNode, "yesNoOkButton");
	            } else {
	            
				        var actionBar = dojo.create("div", {"class": "dijitDialogPaneContentArea",}, d1.containerNode);
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
});
