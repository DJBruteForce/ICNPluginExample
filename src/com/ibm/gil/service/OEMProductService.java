package com.ibm.gil.service;

import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ibm.ecm.extension.PluginService;
import com.ibm.ecm.extension.PluginServiceCallbacks;
import com.ibm.ecm.json.JSONMessage;
import com.ibm.ecm.json.JSONResponse;
import com.ibm.gil.business.Indexing;
import com.ibm.gil.business.OEMProduct;
import com.ibm.gil.util.UtilGilConstants;

public class OEMProductService extends PluginService {

	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(OEMProductService.class);

	@Override
	public String getId() {
		return "OEMProductService";
	}

	@Override
	public String getOverriddenService() {
		return null;
	}
	

	public void execute(PluginServiceCallbacks callbacks, HttpServletRequest request, HttpServletResponse response)	throws Exception {
		
		OEMProduct oemProduct =null;
		String ItemId = null;
		String className = null;
		String country = null;
		String frontEndAction = null;
		String userId =null;
		TimeZone userTimeZone = null;
		JSONResponse jsonResults = new JSONResponse();	
		ServiceHelper.setMDC(request,callbacks);

		ItemId 			= request.getParameter("itemId");
		className 		= request.getParameter("className"); 
		country 		= request.getParameter("country");
		frontEndAction  = request.getParameter("frontEndAction");
		userId          = request.getParameter("desktopUserId");
		userTimeZone   	 = TimeZone.getTimeZone(request.getParameter("userTimeZone"));
		userId = request.getParameter("userId");
		
		oemProduct=new OEMProduct(country);
		oemProduct.setOBJECTID(ItemId);
		oemProduct.setINDEXCLASS(className);
		oemProduct.setUSERID(userId);
		oemProduct.setUserTimezone(userTimeZone);
		oemProduct.setUSERID(userId);
		
		
		try {
	
			   if (frontEndAction.equalsIgnoreCase(UtilGilConstants.INDEX))	{
				   indexDocument( callbacks, jsonResults, request, oemProduct);
					
				} else if (frontEndAction.equalsIgnoreCase("initializeOEMProductGrid"))	{
						 initializeOEMProductGrid(callbacks, jsonResults, request, oemProduct);		
				} 

	} catch(Exception e) {
			logger.fatal(e.getMessage());
			JSONMessage jsonErroMessage = new JSONMessage(0, "Error in Vendor Search.", e.getMessage(), "","Check the IBM Content Navigator logs for more details.", "");
			jsonResults.addErrorMessage(jsonErroMessage);
			
	} finally {
		ServiceHelper.clearMDC();
		jsonResults.serialize(response.getOutputStream());
		
	}

	}

	protected void indexDocument(PluginServiceCallbacks callbacks,JSONResponse jsonResults, HttpServletRequest request,	Indexing indexing) throws Exception {
	
		OEMProduct oemProduct = ((OEMProduct) (indexing));
		logger.debug("Loading OEM Product Dialog");
		oemProduct.initializeModel();
    	String jsonString = ServiceHelper.objectToJson(oemProduct);
		jsonResults.put("oemProductJsonString",jsonString);	
	}


	
   private void initializeOEMProductGrid(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request,Indexing indexing)	throws Exception {
		
	    OEMProduct oemProduct = ((OEMProduct) (indexing));
	    logger.debug("Initializing OEM Product Grid");
	    JsonObject jsonDataObject= new JsonParser().parse(request.getParameter("jsonDataObject")).getAsJsonObject();
	    oemProduct = (OEMProduct)ServiceHelper.jsonToObject(jsonDataObject, OEMProduct.class); 
	    //8String jsonTableGridValues = OEMProductServiceHelper.buildJacksonJsonForTableGrid(oemProduct.searchByProductInfo());
	    String jsonTableGridValues = OEMProductServiceHelper.buildJsonForTableGrid(oemProduct.searchByProductInfo());
	    
		jsonResults.put("jsonOEMGridValues",jsonTableGridValues);
		
		
	}
   
}
