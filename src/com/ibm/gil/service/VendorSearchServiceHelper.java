package com.ibm.gil.service;

import java.util.ArrayList;
import java.util.Iterator;



import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ibm.gil.business.VendorSearch;

public class VendorSearchServiceHelper extends ServiceHelper{
	

	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(VendorSearchServiceHelper.class);
	
    public static String buildJsonForTableGrid (ArrayList<VendorSearch> colOfData) {
    	
		 JsonArray datasetsArr = new JsonArray();
		 JsonObject dataset = new JsonObject();
		
		Iterator<VendorSearch> itVendorSearch = colOfData.iterator();
			
		while(itVendorSearch.hasNext()) {
			
			VendorSearch vendorSearch = itVendorSearch.next();

			dataset = new JsonObject();
			dataset.addProperty("accountNumber", vendorSearch.getACCOUNTNUMBER());
			dataset.addProperty("supplierName", vendorSearch.getVENDORNAME());
			dataset.addProperty("supplierNumber", vendorSearch.getVENDORNUMBER());
			dataset.addProperty("blockInd", vendorSearch.getBLOCKINDC() );
			dataset.addProperty("legalIdNumber", vendorSearch.getVATREGISTRATIONNUMBER());
			dataset.addProperty("addr1", vendorSearch.getADDRESS());
			dataset.addProperty("addr2", vendorSearch.getADDRESS2());
			dataset.addProperty("addr3", vendorSearch.getADDRESS3());
			dataset.addProperty("commision", vendorSearch.getVENDORCOMMISSION());
			dataset.addProperty("srnumber", vendorSearch.getSRNUMBER());

			datasetsArr.add(dataset);
			  
		}
	
		logger.debug(datasetsArr.toString());
		return datasetsArr.toString();
    }
}