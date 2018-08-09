package com.ibm.gil.service;

import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ibm.ecm.extension.PluginServiceCallbacks;
import com.ibm.ecm.json.JSONResponse;
import com.ibm.gil.business.COAELS;
import com.ibm.gil.business.Indexing;
import com.ibm.gil.model.DataModel;
import com.ibm.gil.util.UtilGilConstants;
import com.ibm.mm.sdk.server.DKDatastoreICM;

/**
 * 
 * @author ferandra
 *
 */
public class COAELSService extends GILService {

	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(COAELSService.class);

	public String getId() {
		return "COAELSService";
	}


	public String getOverriddenService() {	
		return null;
	}
	
	protected void indexDocument(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request, Indexing indexing)	throws Exception {
		
		COAELS coa = ((COAELS) (indexing));
		coa.initalizeIndexing();
		ServiceHelper.putJsonResponse(jsonResults,"coaJsonString",coa);
		coa.getCoaDataModelELS().getContentManager().clear();
	}
	
	protected void saveDocument(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request, Indexing indexing)	throws Exception {
		
		COAELS coa = ((COAELS) (indexing));
		String repositoryId = request.getParameter("repositoryId");
		TimeZone userTimeZone = TimeZone.getTimeZone(request.getParameter("userTimeZone"));
		DKDatastoreICM datastore = callbacks.getCMDatastore(repositoryId);
		JsonObject jsonDataObject= new JsonParser().parse(request.getParameter("jsonDataObject")).getAsJsonObject();
		coa = (COAELS)ServiceHelper.jsonToObject(jsonDataObject, COAELS.class);
		coa.setDatastore(datastore);
		coa.setUserTimezone(userTimeZone);
		setWSEndPoint(request.getSession(),coa);
		coa.saveDocument();
		ServiceHelper.putJsonResponse(jsonResults,"coaJsonString",coa);
		coa.getCoaDataModelELS().getContentManager().clear();
	}
	
	private void validateOnSave(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request, COAELS coa)	throws Exception {
		
		String repositoryId = request.getParameter("repositoryId");
		DKDatastoreICM datastore = callbacks.getCMDatastore(repositoryId);
		JsonObject jsonDataObject= new JsonParser().parse(request.getParameter("jsonDataObject")).getAsJsonObject();
		coa = (COAELS)ServiceHelper.jsonToObject(jsonDataObject, COAELS.class);
		coa.setDatastore(datastore);
		coa.validateInput();
		ServiceHelper.putJsonResponse(jsonResults,"coaJsonString",coa);
		coa.getCoaDataModelELS().getContentManager().clear();
	}
	
	private void rollbackIndexing(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request, COAELS coa)	throws Exception {
		
		JsonObject jsonBusinessAttributes	= new JsonParser().parse(request.getParameter("businessAttributes")).getAsJsonObject();
		coa.setCOANUMBER(jsonBusinessAttributes.get("coaNumber").getAsString());
		coa.setCUSTOMERNAME(jsonBusinessAttributes.get("coaCustomerName").getAsString());
		coa.rollbackIndexing();
		ServiceHelper.putJsonResponse(jsonResults,"coaJsonString",coa);
		coa.getCoaDataModelELS().getContentManager().clear();
	}
	
	
	@Override
	protected Indexing initializeVariables(PluginServiceCallbacks callbacks, JSONResponse jsonResults,HttpServletRequest request) {

		JsonObject jsonBusinessAttributes	= new JsonParser().parse(request.getParameter("businessAttributes")).getAsJsonObject();
		String repositoryId = request.getParameter("repositoryId");
		String ItemId = request.getParameter("itemId");
		String userId = request.getParameter("userId");
		String className = request.getParameter("className"); 
		String country = request.getParameter("country");
		String frontEndAction = request.getParameter("frontEndAction");
		TimeZone userTimeZone = TimeZone.getTimeZone(request.getParameter("userTimeZone"));
		DKDatastoreICM datastore = callbacks.getCMDatastore(repositoryId);
		String createdTimestamp = jsonBusinessAttributes.get("createdTimestamp").getAsString();
		
		COAELS coa = new COAELS(country);
		coa.setOBJECTID(ItemId);
		coa.setINDEXCLASS(className);
		coa.setDatastore(datastore);
		coa.setLocale(callbacks.getLocale());
		coa.setUserTimezone(userTimeZone);
		coa.setCreatedTimeStamp(createdTimestamp);
		coa.setUSERID(userId);
		setWSEndPoint(request.getSession(),coa);
		
    	logger.info("User ID:" + coa.getUSERID());
    	logger.info("ItemType:" + coa.getINDEXCLASS());
    	logger.info("Country:" + coa.getCOUNTRY());
    	logger.info("Object ID:" + coa.getOBJECTID());
    	logger.info("Action:" +frontEndAction);
    	
    	return coa;
	}
	

	
	@Override
	protected void selectAction(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request,
			Indexing indexing) throws Exception {

		String frontEndAction = request.getParameter("frontEndAction");
		COAELS coa = ((COAELS) (indexing));

		if (frontEndAction.equalsIgnoreCase(UtilGilConstants.INDEX)) {
			
			index( callbacks, jsonResults, request, coa);			
			
		} else if (frontEndAction.equalsIgnoreCase(UtilGilConstants.SAVE)) {
			
				 save(callbacks, jsonResults, request, coa);
					
		} else if (frontEndAction.equalsIgnoreCase(UtilGilConstants.VALIDATE_ON_SAVE)) {
			
				validateOnSave(callbacks, jsonResults, request, coa);
					
		} else if (frontEndAction.equalsIgnoreCase(UtilGilConstants.ROLLBACK_INDEXING)) {
			
				rollbackIndexing(callbacks, jsonResults, request, coa);	
		}

	}
	
	@Override
	protected void handleException(PluginServiceCallbacks callbacks, JSONResponse jsonResults,
			HttpServletRequest request, Indexing indexing, Exception e) {

		COAELS coa = ((COAELS) (indexing));
		logger.fatal(e.getMessage(),e);
		coa.getDialogErrorMsgs().add(e.getMessage());
		ServiceHelper.putJsonResponse(jsonResults,"coaJsonString",coa);
		coa.getCoaDataModelELS().getContentManager().clear();

	}
	
	@Override
	protected DataModel getDataModel(Indexing indexing) {
		return ((COAELS) (indexing)).getCoaDataModelELS();
	}
	
}
