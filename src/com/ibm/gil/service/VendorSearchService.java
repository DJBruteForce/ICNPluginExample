package com.ibm.gil.service;

import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import com.ibm.ecm.extension.PluginService;
import com.ibm.ecm.extension.PluginServiceCallbacks;
import com.ibm.ecm.json.JSONMessage;
import com.ibm.ecm.json.JSONResponse;
import com.ibm.gil.business.VendorSearch;
import com.ibm.gil.util.UtilGilConstants;
import com.ibm.mm.sdk.server.DKDatastoreICM;


public class VendorSearchService  extends PluginService   {

	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(VendorSearchService.class);

	public String getId() {
		return "VendorSearchService";
	}

	public String getOverriddenService() {
		return null;
	}

	public void execute(PluginServiceCallbacks callbacks, HttpServletRequest request, HttpServletResponse response)	throws Exception {
	 
		JSONResponse jsonResults = new JSONResponse();
		
		String repositoryId = request.getParameter("repositoryId");
		String userId = request.getParameter("userId");
		String country = request.getParameter("country");
		String frontEndAction = request.getParameter("frontEndAction");
		TimeZone userTimeZone = TimeZone.getTimeZone(request.getParameter("userTimeZone"));
		DKDatastoreICM datastore = callbacks.getCMDatastore(repositoryId);
		String queryItem = request.getParameter("queryItem");
		ServiceHelper.setMDC(request,callbacks);
		
		VendorSearch vendorSearch = new VendorSearch(country);
		vendorSearch.setQUERYITEM(queryItem);
		vendorSearch.setDatastore(datastore);
		vendorSearch.setLocale(callbacks.getLocale());
		vendorSearch.setUserTimezone(userTimeZone);
		vendorSearch.setUSERID(userId);
        
    	logger.info("User ID:" + vendorSearch.getUSERID());
    	logger.info("Country:" + country);
    	logger.info("Action:" +frontEndAction);
    	logger.info("Criteria:" +queryItem);
    	
		try {
	
				if (frontEndAction.equalsIgnoreCase(UtilGilConstants.SEARCH_BY_VENDOR_NUMBER))	{
					
					 getAllByVendorNumber( callbacks, jsonResults, request, vendorSearch);			
					
				} else if (frontEndAction.equalsIgnoreCase(UtilGilConstants.SEARCH_BY_VENDOR_NAME))	{
					
							getAllByVendorName(callbacks, jsonResults, request, vendorSearch);
							
				} else if (frontEndAction.equalsIgnoreCase(UtilGilConstants.SEARCH_BY_ACCOUNT))	{
					
							getAllByAccount(callbacks, jsonResults, request, vendorSearch);		
				} 
	
		} catch(Exception e)	{
				logger.fatal(e.getMessage());
				JSONMessage jsonErroMessage = new JSONMessage(0, "Error in Vendor Search.", e.getMessage(), "","Check the IBM Content Navigator logs for more details.", "");
				jsonResults.addErrorMessage(jsonErroMessage);
		} finally {
			ServiceHelper.clearMDC();
			jsonResults.serialize(response.getOutputStream());
		}
	}
	
	private void getAllByVendorNumber(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request, VendorSearch vendorSearch)	throws Exception {
		
		String jsonTableGridValues = VendorSearchServiceHelper.buildJsonForTableGrid(vendorSearch.selectSupplierNumberStatement());
		jsonResults.put("jsonVendorSearchGridValues",jsonTableGridValues);
	}
	
	private void getAllByVendorName(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request, VendorSearch vendorSearch)	throws Exception {
		
		String jsonTableGridValues = VendorSearchServiceHelper.buildJsonForTableGrid(vendorSearch.selectSupplierStatement());
		jsonResults.put("jsonVendorSearchGridValues",jsonTableGridValues);
	}
	
	private void getAllByAccount(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request, VendorSearch vendorSearch)	throws Exception {
		
		String jsonTableGridValues = VendorSearchServiceHelper.buildJsonForTableGrid(vendorSearch.selectAccountStatement());
		jsonResults.put("jsonVendorSearchGridValues",jsonTableGridValues);
	}
}
