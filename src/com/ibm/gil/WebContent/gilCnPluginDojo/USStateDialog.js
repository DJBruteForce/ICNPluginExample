define(["dojo/_base/lang",
        "ecm/model/Action", 
        "ecm/model/Request", 
        "dojo/_base/declare",
        "ecm/widget/dialog/BaseDialog", 
        "dojox/layout/ScrollPane",
	    "dojo/data/ItemFileWriteStore",
	    "dijit/registry",
	    "dojo/dom",
	    "dojo/_base/lang",
	    "dojo/_base/event",
	    "dojo/_base/array",
	    "dojo/domReady!",
	    "dojo/dom-style",
	    "dojo/dom-construct",
	    "gilCnPluginDojo/NonModalConfirmDialog",
	    "gilCnPluginDojo/DialogMessages",
	    "dojo/dom-class",
        "dojo/text!./templates/USStateDialog.html",
	    ],	
		function(lang, Action, Request,declare, BaseDialog,ScrollPane,ItemFileWriteStore,registry,dom,lang,on,array,
				domready,domstyle,construct,	NonModalConfirmDialog,DialogMessages,domclass,template)
				{


	return declare("gilCnPluginDojo.USStateDialog", [ BaseDialog ], {

	
		contentString: template,
		widgetsInTemplate: true,

		callerDialog: null,
		jsonObject:null,
		jsonUsStates:null,
		serviceParameters:null,
		thisDialog:null,
		
		
		
		constructor: function(args){
			debugger;
			lang.mixin(this, args);
		      
		 },
	
		postCreate: function() {
			thisDialog=this;
			debugger;
			this.inherited(arguments);
			dojo.style(this.cancelButton.domNode,"display","none");
			this.setTitle("US State Selection");
//			this.setTitle("What state is the equipment shipping to?");
			this.addButton("Cancel", "cleanValues", false, true);
			this.addButton("OK", "saveState", false, true);
			this.showActionBar(true);
			this.setSize(400,310);//(430,350);
			this.setResizable(true);
			this.setMaximized(false);
			this.initialize();

		},
	

	initialize:function(){
		debugger;
		
//		name="mycheck3" data-dojo-type="dijit/form/CheckBox" value="true"
//		var states=["MD","FL","NY","AT","CG","AA","BB","CC","DD","EE","FF","GG","HH","II","JJ","KK","LL"];//jsonUsStates.stateCodeResults;
		
//		var contentArea = dojo.create("div", {"class": "dijitDialogPaneContentArea",}, thisDialog.containerNode);
//        var actionBar = dojo.create("div", { "class": "dijitDialogPaneActionBar"  }, d1.containerNode);

		thisDialog.serviceParameters['request']=arguments.callee.nom;
		//clean server error messages
		
//		this.serviceParameters['jsonInvoiceEls']= JSON.stringify(jsonInvoiceEls);
//		
		var msg=new DialogMessages();
		ecm.messages.progress_message_USStateService = "Loading States...";
		 
			
		Request.postPluginService("GilCnPlugin","USStateService",'application/x-www.form-urlencoded',{
			
			requestParams : thisDialog.serviceParameters,
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

//		 dojo.place("<label for='"+thisDialog.id+"_state_"+state+"' style='visibility:hidden'>"+state+"</label><br>", dijit.byId(thisDialog.id+"_state_"+state).domNode, "after");
//		this.invPicker.set('options',thisDialog.jsonObject.refListData);
//		var value="";
//		for (var i in thisDialog.jsonObject.refListData) {
//    	    if (thisDialog.jsonObject.refListData[i].selected == true)  value=(thisDialog.jsonObject.refListData[i]["value"]);
//    	}
//		this.invPicker.set('value', value);

		
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
//		if(thisDialog.picked==null){
//			var dialogMessages=new DialogMessages();
//			dialogMessages.addErrorMessages("Reference Invoice must be selected");
//			thisDialog.showMessage("Error",dialogMessages.errorMessages());
//			thisDialog.jsonObject['referenceInvNum']="";
//			thisDialog.callerDialog.refInvNum.set('value',"");
//		}
//		
		

	},
//	
	saveState : function() {
	
		if(thisDialog.validateInput()){
			thisDialog.jsonObject['stateCode']=jsonUsStates.state;	
//			thisDialog.jsonObject['validState']="Y";	
			
			
			thisDialog.serviceParameters['request']=arguments.callee.nom;
			//clean server error messages
			
			this.serviceParameters['jsonInvoiceEls']= JSON.stringify(thisDialog.jsonObject);
//			
			var msg=new DialogMessages();
			ecm.messages.progress_message_InvoiceELSService = "Loading...";
			 
				
			Request.postPluginService("GilCnPlugin","InvoiceELSService",'application/x-www.form-urlencoded',{
				
				requestParams : thisDialog.serviceParameters,
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
						
//						msg.addInfoMessages("Successful response from CTS");
//						thisDialog.showMessage("Info",msg.infoMessages(),
								
								thisDialog.isTolerance();
						
						
					}

				})
			
			});
		}
		},
		isTolerance:function(){
			var msg2=new DialogMessages();
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
//				thisDialog.callerDialog.loadBasicFields();
			}
		},
		finish:function(){
			thisDialog.destroyRecursive();
//			if(document.getElementById(thisDialog.id+"_underlay")!=null)
//				document.getElementById(thisDialog.id+"_underlay").remove();
//		document.getElementById(thisDialog.id).remove();
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
			var states=jsonUsStates.stateCodeResults;
			var state="";
			dojo.style(this.contentArea, 'overflow', 'hidden');
			for(var i in states){
				state=states[i];
				
//				_label =construct.toDom("  <label  id='${id}_lblstate_"+state+"' for='${id}_state_"+state+"'>"+state+":</label>&nbsp;");
				console.log("<br><label for='"+thisDialog.id+"_state_"+state+"'>"+state+"</label>");
				console.log(thisDialog.id);
//				dojo.place("<br><label for='"+thisDialog.id+"_state_"+state+"'>"+state+"</label>", dojo.byId(thisDialog.id+"_usStatePane"), i);/*thisDialog.id+"_statesList"*/
		        _widget	 = new ecm.widget.CheckBox({
		        	
		        	id:thisDialog.id+"_state_"+state, 
		        	name:"checkbox"+ i,
		        	value:"true",
		        	checked: false,
		        	
		          }).placeAt(thisDialog.usStatePane);
		        console.log("i: "+i+" state: "+state);
		        registry.byId(thisDialog.id+"_state_"+state).startup();
//		        if(i==0) dojo.place("<ul>", dojo.byId(thisDialog.id+"_state_"+state), "before");
//		        dojo.place("<li>",  dijit.byId(thisDialog.id+"_state_"+state).domNode, "before");
		        dojo.place("<label for='"+thisDialog.id+"_state_"+state+"'>"+state+"</label><br><br><br>", dijit.byId(thisDialog.id+"_state_"+state).domNode, "after");
		        
			}
			
			 _widget	 = new ecm.widget.CheckBox({
		        	
		        	id:thisDialog.id+"_state_OT", 
		        	name:"checkbox"+ states.length,
		        	value:"true",
		        	checked: false,

		          }).placeAt(thisDialog.usStatePane);

			 registry.byId(thisDialog.id+"_state_"+state).startup();
			 dojo.place("<label for='"+thisDialog.id+"_state_OT'>OT</label><br><br><br>", dijit.byId(thisDialog.id+"_state_OT").domNode, "after");
			 
			 
		},
		
		showMessage: function(titl, msg, callback) {	
			
			
            var d1 = new NonModalConfirmDialog({
                title: titl, 
                style: "min-width:350px;",
                content:msg,
                name:"dialogMessage"
            })
            

            
//			        var actionBar = dojo.create("div", {"class": "dijitDialogPaneContentArea",}, d1.containerNode);
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
//			        new dijit.form.Button({"label": "Cancel",
//			            onClick: function(){
//			            	
//			            	d1.hide();
//			            	
//			            	} }).placeAt(actionBar);
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
		            	d1.hide();  
		            	
		            }});
		        buttonYes.placeAt(actionBar);
		        domclass.add(buttonYes.focusNode, "yesNoOkButton");
		        
		        var buttonNo=new dijit.form.Button({"label": "No",
		            onClick: function(){
		            	
		            	callbackNo();
		            	d1.hide(); 
		            	
		            	}});
		        buttonNo.placeAt(actionBar);
		        domclass.add(buttonNo.focusNode, "yesNoOkButton");
            } else {
            
			        var actionBar = dojo.create("div", {"class": "dijitDialogPaneContentArea",}, d1.containerNode);
			        var actionBar = dojo.create("div", { "class": "dijitDialogPaneActionBar"  }, d1.containerNode);
		
			        var buttonOk=new dijit.form.Button({"label": "OK",
			            onClick: function(){
			            	
			            	d1.hide(); 
			            	
			            } });
			        buttonOk.placeAt(actionBar);
			        domclass.add(buttonOk.focusNode, "yesNoOkButton");
			        
			        var buttonCancel=new dijit.form.Button({"label": "Cancel",
			            onClick: function(){
			            	
			            	d1.hide();
			            	
			            	} });
			        buttonCancel.placeAt(actionBar);
			        domclass.add(buttonCancel.focusNode, "cancelButton");
            }
            
	        d1.startup();
            d1.show();
			
		},

	});
});
