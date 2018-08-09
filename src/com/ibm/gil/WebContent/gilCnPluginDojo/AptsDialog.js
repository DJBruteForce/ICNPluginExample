define(["dojo/_base/declare", "gilCnPluginDojo/InvoiceDialogMessages",], function(declare,InvoiceDialogMessages){
  var AptsDialog = declare(null,{
	  
	  
	  
	  serviceParameters : null,
	  actionClick:0,
	  counterCancel:0,

	  
	  
		/**
		 * Class constructor that sets value for country suing classname
		  * @constructor
		 */
	  
	    constructor: function(args){
	        declare.safeMixin(this,args);
	        this.serviceParameters['country'] = this.serviceParameters['className'].substring(this.serviceParameters['className'].length - 2);
	    },
	   

	/**
	 * Checks whether a function was called twice by accidental double-click. 
	  * @param {button id} buttonToDisable - Button that needs to be disabled to stop the second click
	 */
	isDoubleClick: function(buttonToDisable) {
			
			this.actionClick++;
			if (this.actionClick > 1) { 
				buttonToDisable.set("disabled", true );
				return true;
			} else {
				return false;
			}

				
		},
		
		/**
		 * Enables a button after a function called by this button finishes
		  * @param {button id} buttonToEnable - Button that needs to be enabled after a functin execution.
		 */
		enableClick: function(buttonToEnable) {
			
    		this.actionClick = 0;
    		buttonToEnable.set("disabled", false );	
			
			
		},
		
		cancelConfirmation : function() {
			this.counterCancel=this.counterCancel+1;
			var dialogMsgs=new InvoiceDialogMessages();
			dialogMsgs.addConfirmMessages("You have unsaved changes. Are you sure you want to quit?");
			if(this.changesCounter>0){
				this.showMessage("Confirm",dialogMsgs.confirmMessages(), true, this.choiceYCancel,this.choiceNCancel);
			} else {
				thisForm.destroyRecursive();
			}

		},
		choiceYCancel:function(){
	
			try { 
				thisForm.destroyRecursive();
			}catch(e){
				console.log(e);
			}
		},
		choiceNCancel:function(){
			//nothing to do
			this.counterCancel=0;
		},
		

	  

  });
  
  return AptsDialog;
});