define(["dojo/_base/lang"], function(lang){

    var constants  = {
        "datePattern" 		: "MM/dd/yyyy",
        "timeStampPattern"  : "yyyy-MM-ddThh:mm:ssZ",
        "CMTimeStampPattern"  : "yyyy-MM-ddThh:mm:ssZ",
        "DefaultTimeStampPattern"  : "yyyy-MM-dd HH:mm:ss.SSSSSS",
        
        "dateRegex" 		: 	/(0[1-9]|1[012])[- \/.](0[1-9]|[12][0-9]|3[01])[- \/.](19|20)\d\d/ , // reg exp for "MM/dd/yyyy"
        "decimalRegex"		: /^([1-9][0-9]*|([0-9]\d*)(\.\d+)?)$/	, // reg exp for xxxxx.xx
        "digitsOnlyRegex"	: /^\d+$/
        	
    };


    return function(cname){
        if(typeof cname == "undefined"){
           
            return lang.clone(constants);
        }else{
                    
            if(constants.hasOwnProperty(cname)){
                return constants[cname];
            }else{
                throw "Constant '"+cname+"' does not exist.";
            }
        }
    };

});