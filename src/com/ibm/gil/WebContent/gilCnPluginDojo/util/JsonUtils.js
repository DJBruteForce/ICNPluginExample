define(["dojo/_base/lang", "dojo/_base/declare" ], function(lang, declare){
	 return declare("gilCnPluginDojo.util.JsonUtils",[], {
				    
		 	getValueIfOtherKeyExists: function(data, value, key) {
		
			    	for (var i in data) {
			    	    if (data[i].selected == key) return (data[i][value]);
			    	}
			    	
			    },
			    
				/**
				 * Changes for Story 1750051 - [ELS Canada] CA GIL changes
				 */
			 	getDefaultOrSelected: function(data, value, key) {
			 		var selectedValue = '';
			 		if(data==null || data == undefined) {
			 			return selectedValue;
			 		} else {
						
				    	for (var i in data) {
				    	    if (data[i].selected == key) 
				    	    	selectedValue = (data[i][value]);
				    	}
				    	if ((selectedValue==null || selectedValue.trim().length == 0) && (data!=null && data != undefined) ){
				    		selectedValue = (data[0][value]);
				    	}
				    	return selectedValue;
			 		}
			    }
				/**
				 * End Changes for Story 1750051 - [ELS Canada] CA GIL changes
				 */
	
	
	 });
});