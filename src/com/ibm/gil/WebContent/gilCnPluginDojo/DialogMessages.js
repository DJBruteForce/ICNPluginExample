define(["dojo/_base/declare",
        "dojo/_base/lang",
        "dojox/string/Builder",

        ], 
        

    function(declare,lang,Builder){  
	
	  var DialogMessages =  declare(null, {
		
		eMessages:null,
		wMessages:null,
		cMessages:null,
		iMessages:null,
		hasErrors:false,
		hasWarnings:false,
		hasConfirm:false,
		hasInfo:false,
    
		    
			constructor: function(args){
			
				lang.mixin(this, args);
		      
		    },
		    
		    getAllMessages: function() {
		    	
		    	 if (this.hasWarningMessages() && !this.hasErrorMessages()){
		    		
		    		 if(this.wMessages!=null && this.wMessages.length > 0) {
		    			 
			    		var sb = new Builder();
			    		sb.append("<H3>There are warning messages. Do you want to continue? <br></H3>")
			    		sb.concat(this.warningMessages());
			    		return sb.toString()
		    		 } else {
		    			 return "";
		    		 }
		    		
		    	} else {
		    		
		    		return this.errorMessages() + this.warningMessages();
		    		
		    	}
		    	
		    },
		    
		    addInfoMessages: function(strMsg) {
		    	
		    	if(this.iMessages == null){
		    		this.iMessages =  new Builder();
		    	}
		    	
		    	 if(strMsg!=null && strMsg.length > 0) {
		    		  this.hasInfo = true;
		    		  return this.iMessages.append("<img src='./plugin/GilCnPlugin/getResource/info.png'  style='width:15px;height:15px;'> &nbsp;"+strMsg+"<br>");
		    	 }
		    	 
			},
			
		    
		    addWarningMessages: function(strMsg) {
		    	
		    	if(this.wMessages == null){
		    		this.wMessages =  new Builder();
		    	}
		    	
		    	  if(strMsg!=null && strMsg.length > 0) {
		    		  this.hasWarnings = true;
				  	  return this.wMessages.append("<img src='./plugin/GilCnPlugin/getResource/warning.png'  style='width:15px;height:15px;'> &nbsp;"+strMsg+"<br>");
		    	}	  
			},
			  addConfirmMessages: function(strMsg) {
			    	
			    	if(this.cMessages == null){
			    		this.cMessages =  new Builder();
			    	}
			    	
			    	  if(strMsg!=null && strMsg.length > 0) {
			    		  this.hasConfirm = true;
			    		  //giving 40*40px since it won't appear in a list of confirm messages, can be bigger.
					  	  return this.cMessages.append("<img src='./plugin/GilCnPlugin/getResource/confirm.png'  style='width:40px;height:40px;'> &nbsp;"+strMsg+"<br>");
			    	}	  
				},
			
		    addErrorMessages: function(strMsg) {
		    	
		    	if(this.eMessages == null){
		    		this.eMessages =  new Builder();
		    	}
			    	
		    	  if(strMsg!=null && strMsg.length > 0) {
		    		  this.hasErrors = true;
		    		  return this.eMessages.append("<img src='./plugin/GilCnPlugin/getResource/error.png'  style='width:15px;height:15px;'> &nbsp;"+strMsg+"<br>");
		    	  }

			},
			clearMessages:function(){
				this.eMessages=null;
				this.hasErrors=false;
				this.cMessages=null;
				this.hasConfirm=false;
				this.wMessages=null;
				this.hasWarnings=false;
				this.iMessages=null;
				this.hasInfo=false;
			},
			
		    setHasWarningMessages: function(val) {
			    
				  this.hasWarnings = val;
			},
			setHasConfirmMessages: function(val) {
			    
				  this.hasConfirm = val;
			},
			 
			setHasErrorMessages: function(val) {
		    	
		    	this.hasErrors = val;
				  
			},
			
			setHasInfoMessages: function(val) {
		    	
		    	this.hasInfo = val;
				  
			},
			
			hasInfoMessages: function() {
			    
				  return this.hasInfo;
			},
			
		    hasWarningMessages: function() {
			    
				  return this.hasWarnings;
			},
			hasConfirmMessages: function() {
				    
				  return this.hasConfirm;
			},
		    hasErrorMessages: function() {
		    	
		    	return this.hasErrors;
				  
			}	,

		    errorMessages: function() {
		    	
		    	if(this.eMessages == null)	return "";
		    	
		    	return this.eMessages.toString();
			}	,
			
		    warningMessages: function() {
			    
		    	if(this.wMessages == null)	return "";
		    	
		    	return this.wMessages.toString();
			},
			 confirmMessages: function() {
				    
			    	if(this.cMessages == null)	return "";
			    	
			    	return this.cMessages.toString();
			},
			
		    infoMessages: function() {
			    
		    	if(this.iMessages == null)	return "";
		    	
		    	return this.iMessages.toString();
			},

	
			 
    }); 
	  
	  return DialogMessages;
});