package com.ibm.gil.service;

import java.util.ArrayList;
import java.util.Iterator;



import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ibm.gil.business.CustomerSearchELS;


public class CustomerSearchELSServiceHelper extends ServiceHelper{
	

	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(CustomerSearchELSServiceHelper.class);
	
    public static String buildJsonForTableGrid (ArrayList<CustomerSearchELS> colOfData) {
    	
		 JsonArray datasetsArr = new JsonArray();
		 JsonObject dataset = new JsonObject();
		
		
		Iterator<CustomerSearchELS> itVendorSearch = colOfData.iterator();
		
		while(itVendorSearch.hasNext()) {
			
			CustomerSearchELS  customerSearchELS = itVendorSearch.next();

			dataset = new JsonObject();
			dataset.addProperty("CUSTOMERNAME", customerSearchELS.getCUSTOMERNAME());
			dataset.addProperty("CUSTOMERNUMBER", customerSearchELS.getCUSTOMERNUMBER());
			dataset.addProperty("CUSTOMERADDRESS1", customerSearchELS.getCUSTOMERADDRESS1());
			dataset.addProperty("CUSTOMERADDRESS2", customerSearchELS.getCUSTOMERADDRESS2());
			dataset.addProperty("CUSTOMERADDRESS3", customerSearchELS.getCUSTOMERADDRESS3());
			dataset.addProperty("CUSTOMERADDRESS4", customerSearchELS.getCUSTOMERADDRESS4());
			dataset.addProperty("CUSTOMERADDRESS5", customerSearchELS.getCUSTOMERADDRESS5());
			dataset.addProperty("CUSTOMERADDRESS6",customerSearchELS.getCUSTOMERADDRESS6());
			datasetsArr.add(dataset);
			  
		}
	
		logger.debug(datasetsArr.toString());
		
		return datasetsArr.toString();
	
    }
    
    
    

}


