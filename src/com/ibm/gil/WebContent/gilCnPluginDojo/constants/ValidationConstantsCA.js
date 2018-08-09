define(["dojo/_base/lang"], function(lang){

    var constants  = {
    		//#Story 1831306 - ELS CA GIL - indexing combination

/*    		"poexCodesCombinationsto_CA11" : "Invoice type, company code or currency are incorrect for this POEX code. <br><br>POEX code: CA11 SUB PAYING BP CAD <br>Invoice type: VEN, SAL, LOA, RBD <br>Currency: CAD <br>Company code: 1441 <br><br>You cannot save until you fix the combination.<br>",
    		"poexCodesCombinationsto_CA12" : "Invoice type, company code or currency are incorrect for this POEX code. <br><br>POEX code: CA12 SUB PAYING IBM CAD <br>Invoice type: IBM <br>Currency: CAD <br>Company code: 1441 <br><br>You cannot save until you fix the combination.<br>",
    		"poexCodesCombinationsto_CA13" : "Invoice type, company code or currency are incorrect for this POEX code. <br><br>POEX code: CA13 SUB COMMISSION <br>Invoice type: COM <br>Currency: USD, CAD <br>Company code: 1441 <br><br>You cannot save until you fix the combination.<br>",
    		"poexCodesCombinationsto_CA14" : "Invoice type, company code or currency are incorrect for this POEX code. <br><br>POEX code: CA14 DIV PAYING BP CAD <br>Invoice type: VEN, SAL, LOA, RBD <br>Currency: CAD <br>Company code: 0026 <br><br>You cannot save until you fix the combination.",
    		"poexCodesCombinationsto_CA15" : "Invoice type, company code or currency are incorrect for this POEX code. <br><br>POEX code: CA15 DIV PAYING IBM CAD <br>Invoice type: IBM <br>Currency: CAD <br>Company code: 0026 <br><br>You cannot save until you fix the combination.<br>",
    		"poexCodesCombinationsto_CA16" : "Invoice type, company code or currency are incorrect for this POEX code. <br><br>POEX code: CA16 DIV COMMISSION <br>Invoice type: COM <br>Currency: USD, CAD <br>Company code: 0026 <br><br>You cannot save until you fix the combination.<br>",
    		"poexCodesCombinationsto_CA17" : "Invoice type, company code or currency are incorrect for this POEX code. <br><br>POEX code: CA17 SUB PAYING BP USD <br>Invoice type: VEN, SAL, LOA, RBD <br>Currency: USD <br>Company code: 1441 <br><br>You cannot save until you fix the combination.<br>",
    		"poexCodesCombinationsto_CA18" : "Invoice type, company code or currency are incorrect for this POEX code. <br><br>POEX code: CA18 SUB PAYING IBM USD <br>Invoice type: IBM <br>Currency: USD <br>Company code: 1441 <br><br>You cannot save until you fix the combination.<br>",
    		"poexCodesCombinationsto_CA19" : "Invoice type, company code or currency are incorrect for this POEX code. <br><br>POEX code: CA19 DIV PAYING BP USD <br>Invoice type: VEN, SAL, LOA, RBD <br>Currency: USD <br>Company code: 0026 <br><br>You cannot save until you fix the combination.<br>",
    		"poexCodesCombinationsto_CA20" : "Invoice type, company code or currency are incorrect for this POEX code. <br><br>POEX code: CA20 DIV PAYING IBM USD <br>Invoice type: IBM <br>Currency: USD <br>Company code: 0026 <br><br>You cannot save until you fix the combination.<br>",
*/    		
    		
    		
    		"COMBINATION_ERROR" : "Invoice type, company code or currency are incorrect for this POEX code. <br><br>POEX code: #pcode <br>Invoice type: #itype <br>Currency: #cur <br>Company code: #cc <br><br>You cannot save until you fix the combination.<br>",
    		
    		"INVOICE_TYPE_FOR_CA11" : "VEN, SAL, LOA, RBD",
    		"INVOICE_TYPE_FOR_CA12" : "IBM",
    		"INVOICE_TYPE_FOR_CA13" : "COM",
    		"INVOICE_TYPE_FOR_CA14" : "VEN, SAL, LOA, RBD",
    		"INVOICE_TYPE_FOR_CA15" : "IBM",
    		"INVOICE_TYPE_FOR_CA16" : "COM",
    		"INVOICE_TYPE_FOR_CA17" : "VEN, SAL, LOA, RBD",
    		"INVOICE_TYPE_FOR_CA18" : "IBM",
    		"INVOICE_TYPE_FOR_CA19" : "VEN, SAL, LOA, RBD",
    		"INVOICE_TYPE_FOR_CA20" : "IBM",
    		
    		"CURRENCY_FOR_CA11" : "CAD",
    		"CURRENCY_FOR_CA12" : "CAD",
    		"CURRENCY_FOR_CA13" : "USD, CAD",
    		"CURRENCY_FOR_CA14" : "CAD",
    		"CURRENCY_FOR_CA15" : "CAD",
    		"CURRENCY_FOR_CA16" : "USD, CAD",
    		"CURRENCY_FOR_CA17" : "USD",
    		"CURRENCY_FOR_CA18" : "USD",
    		"CURRENCY_FOR_CA19" : "USD",
    		"CURRENCY_FOR_CA20" : "USD",	
    		
    		"COMPANY_CODE_FOR_CA11" : "1441",
    		"COMPANY_CODE_FOR_CA12" : "1441",
    		"COMPANY_CODE_FOR_CA13" : "1441",
    		"COMPANY_CODE_FOR_CA14" : "0026",
    		"COMPANY_CODE_FOR_CA15" : "0026",
    		"COMPANY_CODE_FOR_CA16" : "0026",
    		"COMPANY_CODE_FOR_CA17" : "1441",
    		"COMPANY_CODE_FOR_CA18" : "1441",
    		"COMPANY_CODE_FOR_CA19" : "0026",
    		"COMPANY_CODE_FOR_CA20" : "0026",	
        	
    };


    return function(cname){
        if(typeof cname == "undefined"){
           
            return lang.clone(constants);
        }else{
                    
            if(constants.hasOwnProperty(cname)){
                return constants[cname];
            }else{
                return "";
            }
        }
    };

});