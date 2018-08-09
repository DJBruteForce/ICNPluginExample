define(["dojo/_base/declare"], function(declare){
 return declare(null,{	  
	  serviceParameters : null,
	  
	    constructor: function(args){
	    	
	        declare.safeMixin(this,args);
	        var country;
	        
	        if (this.serviceParameters['className']=="ROF Signed Offer Letter")//
	        {
	        	country = "GB";//GILConsole.getConsole().getDefaultCountry();
	        } else
	        {
	        	
	        	country = this.serviceParameters['className'].substring(0,2);
	        }
	        var l= country.trim().length;
	        if(country==null || l == 0){
	        	
	        	country="GB";
	        	
	        }
	        
	        this.serviceParameters['country'] = country;
	        
	    },
	  

  });
  
  
});