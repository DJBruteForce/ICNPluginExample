package com.ibm.gil.service;

import java.util.ArrayList;

import java.util.Iterator;



import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ibm.gil.business.OEMProduct;


/*import org.json.JSONArray;
import org.json.JSONException;*/
//import org.json.JSONObject;


public class OEMProductServiceHelper {
	
private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(OEMProductServiceHelper.class);
	
    public static String buildJsonForTableGrid (ArrayList<OEMProduct> colOfData) {
    	
		 JsonArray datasetsArr = new JsonArray();
		 JsonObject dataset = new JsonObject();
		
		
		Iterator<OEMProduct> oemProduct = colOfData.iterator();
		
		while(oemProduct.hasNext()) {
			
			OEMProduct  oemProductobj = oemProduct.next();

			dataset = new JsonObject();
			dataset.addProperty("Type", oemProductobj.getEquipmentType());
			dataset.addProperty("Model", oemProductobj.getModel());
			dataset.addProperty("ManufacturerName", oemProductobj.getManufacturerName());			
			dataset.addProperty("ProductDescription", oemProductobj.getProductDescription());			
			datasetsArr.add(dataset);
			  
		}		
		return datasetsArr.toString();
	
    }
    
/*public static String buildJacksonJsonForTableGrid(ArrayList<OEMProduct> colOfData){

    	Iterator<OEMProduct> oemProduct = colOfData.iterator();
    	List <OEMProduct> oemList = null; 
    	org.json.JSONArray jArray = new org.json.JSONArray();
    	org.json.JSONObject jObject = new org.json.JSONObject();

    	try{

    		while(oemProduct.hasNext()) {

    			OEMProduct  oemProductobj = oemProduct.next();
    			
    			jObject = new JSONObject();
    			jObject.put("Type", oemProductobj.getEquipmentType());
    			jObject.put("Model", oemProductobj.getModel());
    			jObject.put("ManufacturerName", oemProductobj.getManufacturerName());			
    			jObject.put("ProductDescription", oemProductobj.getProductDescription());			
    			jArray.put(jObject);			

    		}

    		jArray.put(oemList).toString();

    	} catch(Exception exc){

    		logger.fatal(exc,exc);
    	}

    	return jArray.toString(); 
    }*/

}
