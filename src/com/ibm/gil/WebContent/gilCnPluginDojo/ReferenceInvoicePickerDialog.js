define(["dojo/_base/lang",
        "ecm/model/Action", 
        "ecm/model/Request", 
        "dojo/_base/declare",
        "ecm/widget/dialog/BaseDialog", 
	    "dojo/data/ItemFileWriteStore",
	    "dojo/dom",
	    "dojo/_base/lang",
	    "dojo/_base/event",
	    "dojo/_base/array",
	    "dojo/dom-class",
	    "gilCnPluginDojo/NonModalConfirmDialog",
	    "gilCnPluginDojo/DialogMessages",
        "dojo/text!./templates/ReferenceInvoicePickerDialog.html",
	    ],	
		function(lang, Action, Request,declare, BaseDialog,ItemFileWriteStore,dom,lang,on,array,
				domclass,NonModalConfirmDialog,DialogMessages,template)
				{


	return declare("gilCnPluginDojo.ReferenceInvoicePickerDialog", [ BaseDialog ], {

	
		contentString: template,
		widgetsInTemplate: true,

		callerDialog: null,

		jsonObject:null,

		thisDialog:null,
		invPickerFlag:false,
		
		
		constructor: function(args){
			debugger;
			lang.mixin(this, args);
		      
		 },
	
		postCreate: function() {
			
			debugger;
			this.inherited(arguments);
			
			dojo.style(this.cancelButton.domNode,"display","none");
			this.setTitle("Choose Invoice");
			this.addButton("Cancel", "cleanValues", false, true);
			this.addButton("OK", "saveValue", false, true);
			
			this.showActionBar(true);
			this.setSize(570, 280);
			this.setResizable(false);
			this.setMaximized(false);
			thisDialog=this;
			this.initialize();
			
		},
	

	initialize:function(){
		
		this.invPicker.set('options',thisDialog.jsonObject.refListData);
		var value="";
		for (var i in thisDialog.jsonObject.refListData) {
    	    if (thisDialog.jsonObject.refListData[i].selected == true)  value=(thisDialog.jsonObject.refListData[i]["value"]);
    	}
		this.invPicker.set('value', value);
		thisDialog.show();
		

		
	},
	cleanValues: function() {
		
		var dialogMessages=new DialogMessages();
		dialogMessages.addErrorMessages("Reference Invoice must be selected");
		thisDialog.showMessage("Error",dialogMessages.errorMessages());
		thisDialog.jsonObject['referenceInvNum']="";
		thisDialog.callerDialog.refInvNum.set('value',"");
		thisDialog.destroyRecursive();
//		document.getElementById(thisDialog.id).remove();
//		if(document.getElementById(thisDialog.id+"_underlay")!=null)
//			document.getElementById(thisDialog.id+"_underlay").remove();
//				
////		thisDialog.destroyRecursive();
//		return;

	},
//	addInvPickerListener:function(){
//		
//		debugger;
//		if(thisDialog.invPickerFlag==false){
//		var dd=dojo.byId(thisDialog.id+"_invPicker_menu");
//		var opt=dd.getElementsByClassName("dijitReset dijitMenuItemLabel");
//		var opts=Array.from(opt);
//		opts.forEach(function(item){item.addEventListener("click",function(){
//			console.log(item.innerText);				
//			thisDialog.invoicePicked(item.innerText);
//			});});
//		thisDialog.invPickerFlag=true;
//		}
//	},
//	invoicePicked:function(item){
//		debugger;
//		var itemSelected=item;
//		var values=thisDialog.jsonObject.refDataValues;
//		var valueSelected="";
//		valueSelected=values[itemSelected];
//		thisDialog.picked = valueSelected.split("#");
//		
//		
//	},
	saveValue : function() {

			// if user clicked ok 
		var item=this.invPicker.get('value');
		var values=thisDialog.jsonObject.refDataValues;
		values=values[item];
		var picked = values.split("#");
		thisDialog.jsonObject['referenceInvNetAmt']=picked[0];
		thisDialog.jsonObject['referenceInvVatAmt']=picked[1];
		thisDialog.jsonObject['referenceInvDate']=picked[2];
			
			
		//loading refInvNum into ref inv num field
		thisDialog.callerDialog.refInvNum.set('value',thisDialog.jsonObject.referenceInvNum);
		//clean flag
		thisDialog.jsonObject["displayRefInvSearch"]="N";
	//      // setup the reference invoice total and balance
		thisDialog.destroyRecursive();
//		document.getElementById(thisDialog.id).remove();
//		if(document.getElementById(thisDialog.id+"_underlay")!=null)
//				document.getElementById(thisDialog.id+"_underlay").remove();

	},
		
	showMessage: function(titl, msg) {	

    var d1 = new NonModalConfirmDialog({
                title: titl, 
                style: "min-width:350px;",
                content:msg,
                name:"dialogMessage"
    })
            

            
//	 var actionBar = dojo.create("div", {"class": "dijitDialogPaneContentArea",}, d1.containerNode);
	 var actionBar = dojo.create("div", { "class": "dijitDialogPaneActionBar"  }, d1.containerNode);
		
			        var buttonOk=new dijit.form.Button({"label": "OK",
			            onClick: function(){
			            	
			            	d1.destroyRecursive(); 
			            	
			            } });
			        buttonOk.placeAt(actionBar);
			        domclass.add(buttonOk.focusNode, "yesNoOkButton");
            
	        d1.startup();
            d1.show();
			
		},

	});
});
