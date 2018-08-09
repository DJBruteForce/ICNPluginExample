define(["dojo/_base/lang", "dojo/_base/declare" ], function(lang, declare){
	 return declare("gilCnPluginDojo.util.StringFormatterUtils",[], {
		 

			    
			    removeSpecialCharacters: function(text) {
		
			    	return text.replace(/[`~!@#$%^&*()_|+\-=?;:'",.<>\{\}\[\]\\\/]/gi, '');
			    	
			    }
	
	
	 });
});