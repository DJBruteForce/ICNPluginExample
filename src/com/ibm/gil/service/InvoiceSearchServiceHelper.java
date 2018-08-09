package com.ibm.gil.service;

import java.util.ArrayList;
import java.util.Iterator;



import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ibm.gil.business.ContractInvoice;

public class InvoiceSearchServiceHelper extends ServiceHelper{
	

	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(InvoiceSearchServiceHelper.class);
	
    public static String buildJsonForTableGrid (ArrayList<ContractInvoice> colOfData) {
    	
		 JsonArray datasetsArr = new JsonArray();
		 JsonObject dataset = new JsonObject();
		
		Iterator<ContractInvoice> it = colOfData.iterator();
			
		while(it.hasNext()) {
			
			ContractInvoice invoiceContract = it.next();

			dataset = new JsonObject();
			dataset.addProperty("objectId", invoiceContract.getOBJECTID());
			dataset.addProperty("invoiceNumber", invoiceContract.getINVOICENUMBER());
			dataset.addProperty("invoiceDate", invoiceContract.getINVOICEDATE());
			dataset.addProperty("country", invoiceContract.getCOUNTRY() );
			dataset.addProperty("contractNumber", invoiceContract.getCONTRACTNUMBER() );
			dataset.addProperty("customerName", invoiceContract.getCUSTOMERNAME() );

			datasetsArr.add(dataset);
			  
		}
	
		logger.debug(datasetsArr.toString());
		
		return datasetsArr.toString();
	
    }
    
    
    

}


