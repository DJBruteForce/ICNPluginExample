package com.ibm.gil.service;

import java.util.ArrayList;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.ibm.ecm.extension.PluginService;
import com.ibm.ecm.extension.PluginServiceCallbacks;
import com.ibm.ecm.json.JSONMessage;
import com.ibm.ecm.json.JSONResponse;
import com.ibm.gil.business.ContractInvoice;
import com.ibm.gil.business.Invoice;
import com.ibm.gil.util.UtilGilConstants;
import com.ibm.mm.sdk.server.DKDatastoreICM;


public class InvoiceSearchService  extends PluginService   {

	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(InvoiceSearchService.class);

	public String getId() {
		
		return "InvoiceSearchService";
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
		
		Invoice invoice = new Invoice(country);
		invoice.setDatastore(datastore);
		invoice.setLocale(callbacks.getLocale());
		invoice.setUserTimezone(userTimeZone);
		invoice.setUSERID(userId);
        
		try {
				if (frontEndAction.equalsIgnoreCase(UtilGilConstants.SEARCH_BYINVOICE))	{	
					searchByInvoice( callbacks, jsonResults, request, invoice);			
				} 
	
		}	catch(Exception e)	{
				logger.fatal(e.getMessage());
				JSONMessage jsonErroMessage = new JSONMessage(0, "Error in Invoice Search.", e.getMessage(), "","Check the IBM Content Navigator logs for more details.", "");
				jsonResults.addErrorMessage(jsonErroMessage);
		}finally{
			ServiceHelper.clearMDC();
			jsonResults.serialize(response.getOutputStream());		
		}
		
	}
	
	private void searchByInvoice(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request,Invoice invoice)	throws Exception {
																			    
		String listObj = request.getParameter("currentInvoicesList");
		String offeringLetter = request.getParameter("offeringLetter");
		invoice.setOFFERINGLETTER(offeringLetter);
		JsonParser jsonParser = new JsonParser();
		JsonArray jsonArray= jsonParser.parse(listObj).getAsJsonArray();
		ArrayList<ContractInvoice> listOfContractInvoices =(ArrayList<ContractInvoice>)ServiceHelper.jsonToObject(jsonArray.toString(), new TypeToken<ArrayList<ContractInvoice>>(){}.getType());
		String jsonTableGridValues = InvoiceSearchServiceHelper.buildJsonForTableGrid(invoice.searchByInvoice(listOfContractInvoices));	
		jsonResults.put("jsonInvoiceSearchGridValues",jsonTableGridValues);
		
	}

}
