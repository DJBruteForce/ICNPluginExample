define(["dojo/_base/declare",
        "dojo/_base/lang",
        "dojox/string/Builder",
        "gilCnPluginDojo/DialogMessages"

        ], 
        

    function(declare,lang,Builder,DialogMessages){  
	
		  
		var DialogMessages =  declare("gilCnPluginDojo.InvoiceDialogMessages",[DialogMessages], {
		
			constructor: function(args){
			
				lang.mixin(this, args);
		      
		    },
		    
		    getAllMessagesOnSave: function() {
		    	
		    	 if (this.hasWarningMessages() && !this.hasErrorMessages()){
		    		
		    		 if(this.wMessages!=null && this.wMessages.length > 0) {
		    			 
			    		var sb = new Builder();
			    		sb.append("<H3>There are warning messages. Do you wish to correct ? <br></H3>")
			    		sb.concat(this.warningMessages());
			    		return sb.toString()
		    		 } else {
		    			 return "";
		    		 }
		    		
		    	} else {
		    		
		    		return this.errorMessages() + this.warningMessages();
		    		
		    	}
		    	
		    },
		    
			 
    }); 
	  
	  return DialogMessages;
});