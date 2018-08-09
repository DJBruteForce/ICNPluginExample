define(["dojo/_base/declare",
        "dojo/_base/lang",
        "gilCnPluginDojo/GILInitializerFields",
        "gilCnPluginDojo/OfferingLetterDialog",
        "gilCnPluginDojo/InvoiceDialog",
        "gilCnPluginDojo/InvoiceELSDialog",
        "gilCnPluginDojo/ContractDialog",
        "gilCnPluginDojo/COAELSDialog",
        "gilCnPluginDojo/OfferingLetterELSDialog",
        'gilCnPluginDojo/MiscELS',
        "gilCnPluginDojo/MiscInvoiceDialog"
        
        ], 
        
   /**
    * Class that returns the dialog form class given a className(document folder). The dialog form contains 
    * all the action a dialog form will perform for each classType. The form dialog class will communicate with
    * services(servlets) passing all the necessary parameters via JSON
    */
    function(declare,lang,GILInitializerFields,OfferingLetterDialog,InvoiceDialog,InvoiceELSDialog,ContractDialog,COAELSDialog,OfferingLetterELSDialog, MiscELS,MiscInvoiceDialog){  
	
	return declare("gilCnPluginDojo.GILInitializerForm",[], {
		  
		/**
		 * class is instantiated with service parameters, all data the service  java class needs to perform backed actions
		 */    
		constructor: function(serviceParams){
		      this.serviceParams = serviceParams;
		    },	    
			 
			/**
			 * function that returns the form dialogForm using the same logic GIl uses 
			 * to check which document folder it belongs.
			 */
		    buildIndexingFormDialog: function() {
		    
		    	try {
					var dialog = null;
					var parms = this.serviceParams;
					
					if (this.serviceParams['className'].startsWith("Invoice") && this.serviceParams['className'].length == 10) {
						
						dialog = new InvoiceDialog({	serviceParameters: parms });
						dialog.index();
						
					} else if ( this.serviceParams['className'].startsWith("Offering Letter") && this.serviceParams['className'].length == 18) {
						 
							 	dialog = new OfferingLetterDialog({	serviceParameters: parms });
								dialog.index();
								
					 } else if ( this.serviceParams['className'].endsWith("Invoice") && this.serviceParams['className'].length == 9) {
						 
							 	dialog = new InvoiceELSDialog({	serviceParameters: parms });
								dialog.index();
							
					 } else if (this.serviceParams['className'].startsWith("Contract") && this.serviceParams['className'].length == 11){
						 
							 	dialog = new ContractDialog({	serviceParameters: parms });
								dialog.index();
						 
					 } else if (this.serviceParams['className'].endsWith("SignCOA") && this.serviceParams['className'].length == 9){
						 
							 	dialog = new COAELSDialog({	serviceParameters: parms });
								dialog.index();
							
					 } else if (this.serviceParams['className'].endsWith("StampdutyOL") && this.serviceParams['className'].length == 13){
					    	
							 	dialog = new OfferingLetterELSDialog({	serviceParameters: parms });
								dialog.index();
			    	
					 } else if (this.serviceParams['className'].endsWith("CountersignOL") && this.serviceParams['className'].length == 15){
							 	
						 		dialog = new OfferingLetterELSDialog({	serviceParameters: parms });
								dialog.index();
			    	
					 } else if (this.serviceParams['className'].endsWith("SignOL") && this.serviceParams['className'].length == 8){
							 	dialog = new OfferingLetterELSDialog({	serviceParameters: parms });
								dialog.index();
			    	
					 }else if (this.serviceParams['className'] == "ROF Signed Offer Letter"){
						 	dialog = new OfferingLetterELSDialog({	serviceParameters: parms });
							dialog.index();
		    	
				     } else if (this.serviceParams['className'].endsWith('Misc') && this.serviceParams['className'].length == 6){
				    	 var misc = new MiscELS(parms);
				    	 misc.index();
				    	 
				     } else if (this.serviceParams['className'].startsWith("Misc Invoice") && (this.serviceParams['className'].length == 21 || this.serviceParams['className'].length == 15 || this.serviceParams['className'].length == 17)){
						 	dialog = new MiscInvoiceDialog({serviceParameters: parms });
							dialog.index();
		    	
				 }
						
					
					
					
					/* else if (this.serviceParams['className'].startsWith("Misc Invoice")){
					 	dialog = new MiscInvoiceDialog({serviceParameters: parms });
						dialog.index();
	    	
			 }*/
					
					
					
		    	}catch(e){
		    		console.log('Error in method ' + arguments.callee.nom + ' - ' + e);
		    	}
			   		 
			   		 return dialog;			  
			},
			 
    }); 
});