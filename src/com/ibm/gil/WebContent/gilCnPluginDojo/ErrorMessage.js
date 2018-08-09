define(["dojo/_base/lang",
        "ecm/model/Action", 
        "ecm/model/Request", 
        "dojo/_base/declare",
        "ecm/widget/dialog/BaseDialog", 
	     "dojo/dom",
	     "dojo/dom-style",
	    "dojo/dom-construct",
	    "dojo/_base/lang",
	    "dojo/_base/event",
	    "dojo/_base/array",
	    "dojo/dom-class",
	    "gilCnPluginDojo/NonModalConfirmDialog",
	    "gilCnPluginDojo/DialogMessages",
        "dojo/text!./templates/ErrorMessage.html",
	    ],	
		function(lang, Action, Request, declare, BaseDialog,dom,domStyle,construct,lang,on,array,
				domclass,NonModalConfirmDialog,DialogMessages,template)
				{


	return declare("gilCnPluginDojo.ErrorMessage", [ BaseDialog ], {

	/**
	 * This Error Dialog won't allow user to click widgets behind it.
	 * 
	 */
		contentString: template,
		widgetsInTemplate: true,

		callerDialog: null,

		thisDialog:null,
		
		destroyCallerDialog:null,
		
		errorMsg:"",
		
		
		constructor: function(args){
			debugger;
			lang.mixin(this, args);
		      
		 },
	
		postCreate: function() {
			
			debugger;
			this.inherited(arguments);
			
			dojo.style(this.cancelButton.domNode,"display","none");
			this.setTitle("Error");
			this.addButton("Cancel", "close", false, true);
			this.addButton("OK", "close", false, true);
			
			this.showActionBar(true);
			this.setSize(400, 180);
			this.setResizable(false);
			this.setMaximized(false);
			thisDialog=this;
			thisDialog.initialize();
			
		},
	

	initialize:function(){
		
		var div = dojo.byId(thisDialog.id+"_error");
	
		div.innerHTML += thisDialog.errorMsg;
		
//		construct.place(thisDialog.charge.domNode, thisDialog.id+"_charge","replace");
		thisDialog.show();
		
		var underlay = dojo.byId(thisDialog.id+"_underlay");
		domStyle.set(underlay, 'opacity', '.75');
		
	},
	close: function() {
		debugger;
		if(thisDialog.destroyCallerDialog){
			thisDialog.callerDialog.rollbackIndexing();
		}
		
		thisDialog.destroyRecursive();


	},
	});
});
