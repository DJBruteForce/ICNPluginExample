package com.ibm.gil.service;

import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;



import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ibm.ecm.extension.PluginServiceCallbacks;
import com.ibm.ecm.json.JSONResponse;
import com.ibm.gil.business.Indexing;
import com.ibm.gil.business.OfferingLetterELS;
import com.ibm.gil.model.DataModel;
import com.ibm.gil.util.UtilGilConstants;
import com.ibm.mm.sdk.server.DKDatastoreICM;


public class OfferingLetterELSService extends GILService {

	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(OfferingLetterELSService.class);

	public String getId() {
		return "OfferingLetterELSService";
	}

	public String getOverriddenService() {
		return null;
	}

	protected void indexDocument(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request, Indexing indexing)	throws Exception {
		
		OfferingLetterELS offeringLetterEls = ((OfferingLetterELS) (indexing));
		JsonObject jsonBusinessAttributes	= new JsonParser().parse(request.getParameter("businessAttributes")).getAsJsonObject();
    	if (offeringLetterEls.getINDEXCLASS().equalsIgnoreCase(OfferingLetterELS.ROFOLITEMTYPE)) {
    		offeringLetterEls.setOFFERINGNUMBER(jsonBusinessAttributes.get("offerringLetterNumber").getAsString());
    		offeringLetterEls.setCOUNTRY(jsonBusinessAttributes.get("countryCode").getAsString());
    		offeringLetterEls.initalizeROFIndexing();
    	} else {
    		offeringLetterEls.initalizeIndexing();
    	}
    	ServiceHelper.putJsonResponse(jsonResults,"OLELSJsonString",offeringLetterEls);
    	offeringLetterEls.getoLDataModel().getContentManager().clear();
	}

	protected void saveDocument(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request,Indexing indexing)	throws Exception {
		
		String repositoryId = request.getParameter("repositoryId");
		OfferingLetterELS offeringLetterEls = ((OfferingLetterELS) (indexing));
		TimeZone userTimeZone = TimeZone.getTimeZone(request.getParameter("userTimeZone"));
		DKDatastoreICM datastore = callbacks.getCMDatastore(repositoryId);
		JsonObject jsonDataObject= new JsonParser().parse(request.getParameter("jsonDataObject")).getAsJsonObject();
		offeringLetterEls = (OfferingLetterELS)ServiceHelper.jsonToObject(jsonDataObject, OfferingLetterELS.class);
		offeringLetterEls.setDatastore(datastore);
		offeringLetterEls.setUserTimezone(userTimeZone);
		setWSEndPoint(request.getSession(),offeringLetterEls);
		offeringLetterEls.saveDocument();
		ServiceHelper.putJsonResponse(jsonResults,"OLELSJsonString",offeringLetterEls);
		offeringLetterEls.getoLDataModel().getContentManager().clear();
	}
	
	private void validateOnSave(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request,OfferingLetterELS offeringLetterEls)	throws Exception {
		
		String repositoryId = request.getParameter("repositoryId");
		DKDatastoreICM datastore = callbacks.getCMDatastore(repositoryId);
		JsonObject jsonDataObject= new JsonParser().parse(request.getParameter("jsonDataObject")).getAsJsonObject();
		offeringLetterEls = (OfferingLetterELS)ServiceHelper.jsonToObject(jsonDataObject, OfferingLetterELS.class);
		offeringLetterEls.setDatastore(datastore);
		offeringLetterEls.validateInput();
		ServiceHelper.putJsonResponse(jsonResults,"OLELSJsonString",offeringLetterEls);
		offeringLetterEls.getoLDataModel().getContentManager().clear();
	}
	
	private void rollbackIndexing(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request,OfferingLetterELS offeringLetterEls)	throws Exception {
		
		offeringLetterEls.rollbackIndexing();
		ServiceHelper.putJsonResponse(jsonResults,"OLELSJsonString",offeringLetterEls);
		offeringLetterEls.getoLDataModel().getContentManager().clear();
	}
	
	
	
	@Override
	protected Indexing initializeVariables(PluginServiceCallbacks callbacks, JSONResponse jsonResults,
			HttpServletRequest request) {

		JsonObject jsonBusinessAttributes = null;
		String createdTimestamp = null;
		jsonBusinessAttributes	= new JsonParser().parse(request.getParameter("businessAttributes")).getAsJsonObject();
		String repositoryId = request.getParameter("repositoryId");
		String ItemId = request.getParameter("itemId");
		String userId = request.getParameter("userId");
		String className = request.getParameter("className"); 
		String country = request.getParameter("country");
		String frontEndAction = request.getParameter("frontEndAction");
		TimeZone userTimeZone = TimeZone.getTimeZone(request.getParameter("userTimeZone"));
		DKDatastoreICM datastore = callbacks.getCMDatastore(repositoryId);
		createdTimestamp = jsonBusinessAttributes.get("createdTimestamp").getAsString();
		
		OfferingLetterELS offeringLetterEls = new OfferingLetterELS(country);
		offeringLetterEls.setOBJECTID(ItemId);
		offeringLetterEls.setINDEXCLASS(className);
		offeringLetterEls.setDatastore(datastore);
		offeringLetterEls.setLocale(callbacks.getLocale());
		offeringLetterEls.setUserTimezone(userTimeZone);
		offeringLetterEls.setCreatedTimeStamp(createdTimestamp);
		offeringLetterEls.setUSERID(userId);
		setWSEndPoint(request.getSession(),offeringLetterEls);
    	
    	logger.info("User ID:" + offeringLetterEls.getUSERID());
    	logger.info("ItemType:" + offeringLetterEls.getINDEXCLASS());
    	logger.info("Country:" + offeringLetterEls.getCOUNTRY());
    	logger.info("Object ID:" + offeringLetterEls.getOBJECTID());
    	logger.info("Action:" +frontEndAction);

		return offeringLetterEls;
	}
	
	@Override
	protected void selectAction(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request,
			Indexing indexing) throws Exception {

		String frontEndAction = request.getParameter("frontEndAction");
		OfferingLetterELS offeringLetterEls = ((OfferingLetterELS) (indexing));

		if (frontEndAction.equalsIgnoreCase(UtilGilConstants.INDEX))	{
			
			index( callbacks, jsonResults, request, offeringLetterEls);			
			
		} else if (frontEndAction.equalsIgnoreCase(UtilGilConstants.SAVE))	{
			
				 save(callbacks, jsonResults, request, offeringLetterEls);
					
		} else if (frontEndAction.equalsIgnoreCase(UtilGilConstants.VALIDATE_ON_SAVE))	{
			
				validateOnSave(callbacks, jsonResults, request, offeringLetterEls);
					
		} else if (frontEndAction.equalsIgnoreCase(UtilGilConstants.ROLLBACK_INDEXING))	{
			
				rollbackIndexing(callbacks, jsonResults, request, offeringLetterEls);	
		}

	}

	@Override
	protected void handleException(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request, Indexing indexing, Exception e) {

		OfferingLetterELS offeringLetterEls = ((OfferingLetterELS) (indexing));
		logger.fatal(e.getMessage(),e);
		offeringLetterEls.getDialogErrorMsgs().add(e.getMessage());
		ServiceHelper.putJsonResponse(jsonResults,"OLELSJsonString",offeringLetterEls);
		offeringLetterEls.getoLDataModel().getContentManager().clear();
	}

	@Override
	protected DataModel getDataModel(Indexing indexing) {
		return ((OfferingLetterELS) (indexing)).getoLDataModel();
	}
	
	
	
}
