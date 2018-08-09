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
import com.ibm.gil.business.MiscVendorSearch;
import com.ibm.gil.util.UtilGilConstants;
import com.ibm.mm.sdk.server.DKDatastoreICM;


public class MiscVendorSearchService  extends PluginService   {

	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(MiscVendorSearchService.class);

	public String getId() {
		return "MiscVendorSearchService";
	}

	public String getOverriddenService() {
		return null;
	}

	public void execute(PluginServiceCallbacks callbacks, HttpServletRequest request, HttpServletResponse response)	throws Exception {
	 
		JSONResponse jsonResults = new JSONResponse();
		
		JsonObject jsonBusinessAttributes	= new JsonParser().parse(request.getParameter("businessAttributes")).getAsJsonObject();
		
		String repositoryId = request.getParameter("repositoryId");
		String userId = request.getParameter("userId");		
		String country=	jsonBusinessAttributes.get("countryCode").getAsString();
		String frontEndAction = request.getParameter("frontEndAction");
		TimeZone userTimeZone = TimeZone.getTimeZone(request.getParameter("userTimeZone"));
		DKDatastoreICM datastore = callbacks.getCMDatastore(repositoryId);
		String queryItem = request.getParameter("queryItem");
		
		
		MiscVendorSearch miscVendorSearch = new MiscVendorSearch(country);
		miscVendorSearch.setQUERYITEM(queryItem);
		miscVendorSearch.setDatastore(datastore);
		miscVendorSearch.setLocale(callbacks.getLocale());
		miscVendorSearch.setUserTimezone(userTimeZone);
		miscVendorSearch.setUSERID(userId);
        
		try {
	
				if (frontEndAction.equalsIgnoreCase(UtilGilConstants.SEARCH_BY_VENDOR_NUMBER))	{
					
					 getAllByVendorNumber( callbacks, jsonResults, request, miscVendorSearch);			
					
				} else if (frontEndAction.equalsIgnoreCase(UtilGilConstants.SEARCH_BY_VENDOR_NAME))	{
					
							getAllByVendorName(callbacks, jsonResults, request, miscVendorSearch);
							
				} else if (frontEndAction.equalsIgnoreCase(UtilGilConstants.SEARCH_BY_ACCOUNT))	{
					
							getAllByAccount(callbacks, jsonResults, request, miscVendorSearch);		
				} 
	
		} catch(Exception e)	{
				logger.fatal(e.getMessage());
				JSONMessage jsonErroMessage = new JSONMessage(0, "Error in Vendor Search.", e.getMessage(), "","Check the IBM Content Navigator logs for more details.", "");
				jsonResults.addErrorMessage(jsonErroMessage);
		} finally {
			jsonResults.serialize(response.getOutputStream());
		}
	}
	
	private void getAllByVendorNumber(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request, MiscVendorSearch miscVendorSearch)	throws Exception {
		
		String jsonTableGridValues = VendorSearchServiceHelper.buildJsonForTableGrid(miscVendorSearch.selectSupplierNumberStatement());
		jsonResults.put("jsonVendorSearchGridValues",jsonTableGridValues);
	}
	
	private void getAllByVendorName(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request, MiscVendorSearch miscVendorSearch)	throws Exception {
		
		String jsonTableGridValues = VendorSearchServiceHelper.buildJsonForTableGrid(miscVendorSearch.selectSupplierStatement());
		jsonResults.put("jsonVendorSearchGridValues",jsonTableGridValues);
	}
	
	private void getAllByAccount(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request, MiscVendorSearch miscVendorSearch)	throws Exception {
		
		String jsonTableGridValues = VendorSearchServiceHelper.buildJsonForTableGrid(miscVendorSearch.selectAccountStatement());
		jsonResults.put("jsonVendorSearchGridValues",jsonTableGridValues);
	}
}
