package com.ibm.gil.service;

import java.util.ArrayList;
import java.util.Iterator;



import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ibm.gil.business.VendorSearchELS;

public class VendorSearchELSServiceHelper extends ServiceHelper{
	

	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(VendorSearchELSServiceHelper.class);
	
    public static String buildJsonForTableGrid (ArrayList<VendorSearchELS> colOfData) {
    	
		 JsonArray datasetsArr = new JsonArray();
		 JsonObject dataset = new JsonObject();
		
		
		Iterator<VendorSearchELS> itVendorSearch = colOfData.iterator();
		
		while(itVendorSearch.hasNext()) {
			
			VendorSearchELS  vendorSearchELS = itVendorSearch.next();

			dataset = new JsonObject();
			dataset.addProperty("supplierName", vendorSearchELS.getVENDORNAME());
			dataset.addProperty("supplierNumber", vendorSearchELS.getVENDORNUMBER());
			dataset.addProperty("blockInd", vendorSearchELS.getBLOCKINDC());
			dataset.addProperty("legalIdNumber", vendorSearchELS.getVATREGISTRATIONNUMBER());
			dataset.addProperty("addr1", vendorSearchELS.getSUPPLIERADDRESS());
			dataset.addProperty("addr2", vendorSearchELS.getSUPPLIERADDRESS2());
			dataset.addProperty("addr3", vendorSearchELS.getSUPPLIERADDRESS3());
			dataset.addProperty("sanumber", vendorSearchELS.getSRNUMBER());
			logger.debug("Vendor Search ELS-commission helper: "+vendorSearchELS.getVENDORCOMMISSION());
			dataset.addProperty("commission", vendorSearchELS.getVENDORCOMMISSION());
			dataset.addProperty("sadate",vendorSearchELS.getSRENDDATE());
			datasetsArr.add(dataset);
			  
		}
	
		logger.debug(datasetsArr.toString());
		
		return datasetsArr.toString();
	
    }
    
    
    

}


