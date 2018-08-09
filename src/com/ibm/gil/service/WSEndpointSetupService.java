package com.ibm.gil.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ibm.ecm.extension.PluginService;
import com.ibm.ecm.extension.PluginServiceCallbacks;
import com.ibm.ecm.json.JSONMessage;
import com.ibm.ecm.json.JSONResponse;
import com.ibm.gil.business.WebServicesEndpoint;
import com.ibm.gil.util.UtilGilConstants;
import com.ibm.igf.hmvc.WebServicesEnpointManager;


public class WSEndpointSetupService  extends PluginService   {

	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(WSEndpointSetupService.class);

	public String getId() {
		return "WSEndpointSetupService";
	}

	public String getOverriddenService() {
		return null;
	}

	public void execute(PluginServiceCallbacks callbacks, HttpServletRequest request, HttpServletResponse response)	throws Exception {
	 
		JSONResponse jsonResults = new JSONResponse();
		String userId = request.getParameter("userId");
		String country = request.getParameter("country");
		String frontEndAction = request.getParameter("frontEndAction");
		ServiceHelper.setMDC(request,callbacks);
       
    	logger.info("User ID:" + userId);
    	logger.info("Country:" + country);
    	logger.info("Action:" +frontEndAction);
    	
		try {
				if (frontEndAction.equalsIgnoreCase(UtilGilConstants.GET_ENDPOINTS))	{
					
					getEndPoints( callbacks, jsonResults, request);	
					
				} else if (frontEndAction.equalsIgnoreCase(UtilGilConstants.SET_ENDPOINT))	{
					
						 setEndPoint( callbacks, jsonResults, request);	
				}
				
		} catch(Exception e)	{
				logger.fatal(e.getMessage());
				JSONMessage jsonErroMessage = new JSONMessage(0, "Error setting Endpoint", e.getMessage(), "","Check the IBM Content Navigator logs for more details.", "");
				jsonResults.addErrorMessage(jsonErroMessage);
		} finally {
			ServiceHelper.clearMDC();
			jsonResults.serialize(response.getOutputStream());
		}
	}
	
	private void getEndPoints(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request)	throws Exception {
		
		List<WebServicesEndpoint> listOfEndPoints = WebServicesEnpointManager.getWsEnpointsBySystem(WebServicesEnpointManager.SYSTEMS.GCMS);
		String gcmsEp  = (String) request.getSession().getAttribute("gcmsEndpoint");
		WebServicesEndpoint ws = new WebServicesEndpoint(gcmsEp);
		int index = listOfEndPoints.indexOf(ws);
		if (index>=0)listOfEndPoints.get(index).setSelected(true);
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.create();
		String jsonList = gson.toJson(listOfEndPoints); 
		jsonResults.put("listOfGcmsEndPoints",jsonList);
	}
	
	private void setEndPoint(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request)	throws Exception {
		
		String gcmsEndpoint = request.getParameter("gcmsEndpoint");
		request.getSession().setAttribute("gcmsEndpoint", gcmsEndpoint);
		logger.info("Setting GCMS endpoint: "+gcmsEndpoint );
	}
	


}
