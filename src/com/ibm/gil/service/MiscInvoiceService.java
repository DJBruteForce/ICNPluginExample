package com.ibm.gil.service;

import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;



import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ibm.ecm.extension.PluginServiceCallbacks;
import com.ibm.ecm.json.JSONResponse;
import com.ibm.gil.business.Indexing;
import com.ibm.gil.business.MiscInvoice;
import com.ibm.gil.model.DataModel;
import com.ibm.gil.util.CNUtilConstants;
import com.ibm.gil.util.GilUtility;
import com.ibm.gil.util.UtilGilConstants;
import com.ibm.mm.sdk.server.DKDatastoreICM;

/**
 * 
 * @author 
 *
 */
public class MiscInvoiceService  extends GILService {

	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(MiscInvoiceService.class);

	@Override
	public String getId() {
		return "MiscInvoiceService";
	}

	@Override
	public String getOverriddenService() {
		return null;
	}
	
	@Override
	protected void indexDocument(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request, Indexing indexing)	throws Exception {

		MiscInvoice miscInvoice = ((MiscInvoice) (indexing));
		miscInvoice.initializeIndexing();
		ServiceHelper.putJsonResponse(jsonResults,"miscInvoiceJsonString",miscInvoice);
	}
	
	@Override
	protected void saveDocument(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request, Indexing indexing)	throws Exception {
		
		MiscInvoice miscInvoice = ((MiscInvoice) (indexing));
		String repositoryId = request.getParameter("repositoryId");
		TimeZone userTimeZone = TimeZone.getTimeZone(request.getParameter("userTimeZone"));
		DKDatastoreICM datastore = callbacks.getCMDatastore(repositoryId);
		JsonObject jsonDataObject= new JsonParser().parse(request.getParameter("jsonDataObject")).getAsJsonObject();
		miscInvoice = (MiscInvoice)ServiceHelper.jsonToObject(jsonDataObject, MiscInvoice.class);
		miscInvoice.getDialogErrorMsgs().clear();
		miscInvoice.setDatastore(datastore);
		miscInvoice.setUserTimezone(userTimeZone);
		miscInvoice.updateIndexValues();
		miscInvoice.saveDocument();
		ServiceHelper.putJsonResponse(jsonResults,"miscInvoiceJsonString",miscInvoice);
	}
	
	private void addContract(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request, MiscInvoice miscInvoice)	throws Exception {
		
		String repositoryId = request.getParameter("repositoryId");
		DKDatastoreICM datastore = callbacks.getCMDatastore(repositoryId);
		JsonObject jsonDataObject= new JsonParser().parse(request.getParameter("jsonDataObject")).getAsJsonObject();
		miscInvoice = (MiscInvoice)ServiceHelper.jsonToObject(jsonDataObject, MiscInvoice.class);
		miscInvoice.setDatastore(datastore);
		miscInvoice.getDialogErrorMsgs().clear();
		miscInvoice.saveDocument();
		ServiceHelper.putJsonResponse(jsonResults,"miscInvoiceJsonString",miscInvoice);
	}
	
	private void validateOnSave(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request, MiscInvoice miscInvoice)	throws Exception {
		
		String repositoryId = request.getParameter("repositoryId");
		DKDatastoreICM datastore = callbacks.getCMDatastore(repositoryId);
		JsonObject jsonDataObject= new JsonParser().parse(request.getParameter("jsonDataObject")).getAsJsonObject();
		miscInvoice = (MiscInvoice)ServiceHelper.jsonToObject(jsonDataObject, MiscInvoice.class);
		miscInvoice.setDatastore(datastore);
		miscInvoice.getDialogErrorMsgs().clear();
		miscInvoice.validateInput();
		ServiceHelper.putJsonResponse(jsonResults,"miscInvoiceJsonString",miscInvoice);
	}
	
	private void defaults(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request, MiscInvoice miscInvoice)	throws Exception {
		
		String repositoryId = request.getParameter("repositoryId");
		DKDatastoreICM datastore = callbacks.getCMDatastore(repositoryId);
		JsonObject jsonDataObject= new JsonParser().parse(request.getParameter("jsonDataObject")).getAsJsonObject();
		miscInvoice = (MiscInvoice)ServiceHelper.jsonToObject(jsonDataObject, MiscInvoice.class);
		miscInvoice.setDatastore(datastore);
		miscInvoice.defaultToLastInsertedRecord();
		ServiceHelper.putJsonResponse(jsonResults,"miscInvoiceJsonString",miscInvoice);
	}
	
	private void rollbackIndexing(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request, MiscInvoice miscInvoice)	throws Exception {
		
		miscInvoice.rollbackIndexing();
		ServiceHelper.putJsonResponse(jsonResults,"miscInvoiceJsonString",miscInvoice);
	}
	
	private void rollbackCountryIndexing(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request, MiscInvoice miscInvoice)	throws Exception {
		
		miscInvoice.rollbackCountryIndexing();
		ServiceHelper.putJsonResponse(jsonResults,"miscInvoiceJsonString",miscInvoice);
	}
	
	@Override
	protected void handleException(PluginServiceCallbacks callbacks, JSONResponse jsonResults,
			HttpServletRequest request, Indexing indexing, Exception e) {

		MiscInvoice miscInvoice = ((MiscInvoice) (indexing));
		logger.fatal(e.getMessage());
		miscInvoice.getDialogErrorMsgs().add(e.getMessage());
		ServiceHelper.putJsonResponse(jsonResults,"miscInvoiceJsonString",miscInvoice);
	}

	@Override
	protected DataModel getDataModel(Indexing indexing) {
		return ((MiscInvoice) (indexing)).getMiscInvoiceDataModel();
	}
   private void setCAVATCodes(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request, MiscInvoice miscInvoice)	throws Exception {
	   
	    JsonObject jsonDataObject= new JsonParser().parse(request.getParameter("jsonDataObject")).getAsJsonObject();	   
		miscInvoice = (MiscInvoice)ServiceHelper.jsonToObject(jsonDataObject, MiscInvoice.class); 		
		miscInvoice.setcavat();
		ServiceHelper.putJsonResponse(jsonResults,"miscInvoiceJsonString",miscInvoice);
	}
     
     private void addConvination(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request, MiscInvoice miscInvoice)	throws Exception {
 		
    	JsonObject jsonDataObject= new JsonParser().parse(request.getParameter("jsonDataObject")).getAsJsonObject();
 		miscInvoice = (MiscInvoice)ServiceHelper.jsonToObject(jsonDataObject, MiscInvoice.class); 
    	miscInvoice.saveConvination(); 
 		miscInvoice.setMiscComb();
 		ServiceHelper.putJsonResponse(jsonResults,"miscInvoiceJsonString",miscInvoice);
 	} 
	

	@Override
	protected Indexing initializeVariables(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request) {

		
		JsonObject jsonBusinessAttributes	= new JsonParser().parse(request.getParameter("businessAttributes")).getAsJsonObject();
		String repositoryId = request.getParameter("repositoryId");
		String ItemId = request.getParameter("itemId");
		String userId = request.getParameter("userId");
		String className = request.getParameter("className"); 
		//String country = request.getParameter("country");
		String frontEndAction = request.getParameter("frontEndAction");
		TimeZone userTimeZone = TimeZone.getTimeZone(request.getParameter("userTimeZone"));
		DKDatastoreICM datastore = callbacks.getCMDatastore(repositoryId);
		String createdTimestamp = jsonBusinessAttributes.get("createdTimestamp").getAsString();
		String country=	jsonBusinessAttributes.get("countryCode").getAsString();
		
		MiscInvoice miscInvoice = new MiscInvoice(country);
		miscInvoice.setOBJECTID(ItemId);
		miscInvoice.setINDEXCLASS(className);
		miscInvoice.setDatastore(datastore);
		miscInvoice.setLocale(callbacks.getLocale());
		miscInvoice.setUserTimezone(userTimeZone);
		miscInvoice.setCreatedTimeStamp(createdTimestamp);
		miscInvoice.setUSERID(userId);

    	//defect 1832051 
    	miscInvoice.setCREATEDATE(GilUtility.returnDate(createdTimestamp, CNUtilConstants.CN_DATE_FORMAT2, CNUtilConstants.GIL_DATE_FORMAT));

		return miscInvoice;
	}
	

	@Override
	protected void selectAction(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request,
			Indexing indexing) throws Exception {

		String frontEndAction = request.getParameter("frontEndAction");
		MiscInvoice miscInvoice = ((MiscInvoice) (indexing));

		if (frontEndAction.equalsIgnoreCase(UtilGilConstants.INDEX))	{
			
			index( callbacks, jsonResults, request, miscInvoice);
			
		} else if (frontEndAction.equalsIgnoreCase(UtilGilConstants.SAVE))	{
			
				   save(callbacks, jsonResults, request, miscInvoice);
					
		} else if (frontEndAction.equalsIgnoreCase(UtilGilConstants.ADD_CONTRACT))	{
			
					addContract(callbacks, jsonResults, request, miscInvoice);
					
		} else if (frontEndAction.equalsIgnoreCase(UtilGilConstants.VALIDATE_ON_SAVE))	{
			
				   validateOnSave(callbacks, jsonResults, request, miscInvoice);					
		
		}else if (frontEndAction.equalsIgnoreCase(UtilGilConstants.ROLLBACK_INDEXING))	{
			
			     rollbackIndexing(callbacks, jsonResults, request, miscInvoice);	
			
		}else if (frontEndAction.equalsIgnoreCase(UtilGilConstants.ROLLBACK_COUNTRY_INDEXING))	{
			
			     rollbackCountryIndexing(callbacks, jsonResults, request, miscInvoice);	
		}else if(frontEndAction.equalsIgnoreCase("SetCAVATCodes")){
			setCAVATCodes(callbacks, jsonResults, request, miscInvoice);
			
			
		}else if(frontEndAction.equalsIgnoreCase("addConvination")){
			addConvination(callbacks, jsonResults, request, miscInvoice);
		}
		
	}
	
	
}
