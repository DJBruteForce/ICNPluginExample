package com.ibm.gil.service;

import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;



import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ibm.ecm.extension.PluginServiceCallbacks;
import com.ibm.ecm.json.JSONResponse;
import com.ibm.gil.business.Indexing;
import com.ibm.gil.business.OfferingLetter;
import com.ibm.gil.model.DataModel;
import com.ibm.gil.util.UtilGilConstants;
import com.ibm.mm.sdk.server.DKDatastoreICM;

/**
 * 
 * @author ferandra
 *
 */
public class OfferingLetterService  extends GILService {

	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(MiscInvoiceService.class);

	public String getId() {
		return "OfferingLetterService";
	}

	public String getOverriddenService() {
		return null;
	}
	
		
	
	
	
	protected void indexDocument(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request, Indexing indexing)	throws Exception {

		OfferingLetter offeringLetter = ((OfferingLetter) (indexing));
		offeringLetter.initializeIndexing();
		ServiceHelper.putJsonResponse(jsonResults,"offeringLetterJsonString",offeringLetter);
	}
	
	protected void saveDocument(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request, Indexing indexing)	throws Exception {
		
		OfferingLetter offeringLetter = ((OfferingLetter) (indexing));
		String repositoryId = request.getParameter("repositoryId");
		TimeZone userTimeZone = TimeZone.getTimeZone(request.getParameter("userTimeZone"));
		DKDatastoreICM datastore = callbacks.getCMDatastore(repositoryId);
		JsonObject jsonDataObject= new JsonParser().parse(request.getParameter("jsonDataObject")).getAsJsonObject();
		offeringLetter = (OfferingLetter)ServiceHelper.jsonToObject(jsonDataObject, OfferingLetter.class);
		offeringLetter.setDatastore(datastore);
		offeringLetter.setUserTimezone(userTimeZone);
		offeringLetter.updateIndexValues();
		offeringLetter.saveDocument();
		ServiceHelper.putJsonResponse(jsonResults,"offeringLetterJsonString",offeringLetter);
	}
	
	
	private void validateOnSave(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request, OfferingLetter offeringLetter)	throws Exception {
		
		String repositoryId = request.getParameter("repositoryId");
		DKDatastoreICM datastore = callbacks.getCMDatastore(repositoryId);
		JsonObject jsonDataObject= new JsonParser().parse(request.getParameter("jsonDataObject")).getAsJsonObject();
		offeringLetter = (OfferingLetter)ServiceHelper.jsonToObject(jsonDataObject, OfferingLetter.class);
		offeringLetter.setDatastore(datastore);		
		offeringLetter.validateInput();
		ServiceHelper.putJsonResponse(jsonResults,"offeringLetterJsonString",offeringLetter);
	}	

	
	private void rollbackIndexing(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request, OfferingLetter offeringLetter)	throws Exception {
		
		offeringLetter.rollbackIndexing();
		ServiceHelper.putJsonResponse(jsonResults,"offeringLetterJsonString",offeringLetter);
	}
	
	
	@Override
	protected void handleException(PluginServiceCallbacks callbacks, JSONResponse jsonResults,
			HttpServletRequest request, Indexing indexing, Exception e) {

		OfferingLetter offeringLetter = ((OfferingLetter) (indexing));
		logger.fatal(e.getMessage());		
		ServiceHelper.putJsonResponse(jsonResults,"offeringLetterJsonString",offeringLetter);
	}

	@Override
	protected DataModel getDataModel(Indexing indexing) {
		return ((OfferingLetter) (indexing)).getOfferingLetterDataModel();
	}
	

	@Override
	protected Indexing initializeVariables(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request) {

		
		
		
		jsonResults = new JSONResponse();		
		JsonObject jsonBusinessAttributes= new JsonParser().parse(request.getParameter("businessAttributes")).getAsJsonObject();	    
	    String offeringNumber=	jsonBusinessAttributes.get("number").getAsString();
		
		String repositoryId 	= request.getParameter("repositoryId");
		String ItemId 			= request.getParameter("itemId");
		String docId 			= request.getParameter("docId");
		String className 		= request.getParameter("className"); 
		String country 		= request.getParameter("country");
		String frontEndAction  = request.getParameter("frontEndAction");
		String userId          = request.getParameter("desktopUserId");
		DKDatastoreICM datastore 		= callbacks.getCMDatastore(repositoryId);
		TimeZone userTimeZone   	 = TimeZone.getTimeZone(request.getParameter("userTimeZone"));
		String createdTimestamp = jsonBusinessAttributes.get("createdTimestamp").getAsString();
		
		OfferingLetter offeringLetter=new OfferingLetter(country);
		
		offeringLetter.setOFFERINGNUMBER(offeringNumber);
		offeringLetter.setOBJECTID(ItemId);
		offeringLetter.setINDEXCLASS(className);
		offeringLetter.setDatastore(datastore);
		offeringLetter.setUSERID(userId);
		offeringLetter.setLocale(callbacks.getLocale());
		offeringLetter.setUserTimezone(userTimeZone);
		offeringLetter.setCreatedTimeStamp(createdTimestamp);

		return offeringLetter;
	}
	

	@Override
	protected void selectAction(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request,
			Indexing indexing) throws Exception {

		String frontEndAction = request.getParameter("frontEndAction");
		OfferingLetter offeringLetter = ((OfferingLetter) (indexing));

		if (frontEndAction.equalsIgnoreCase(UtilGilConstants.INDEX))	{
			
			index( callbacks, jsonResults, request, offeringLetter);
			
		} else if (frontEndAction.equalsIgnoreCase(UtilGilConstants.SAVE))	{
			
				   save(callbacks, jsonResults, request, offeringLetter);
					
		}  else if (frontEndAction.equalsIgnoreCase(UtilGilConstants.VALIDATE_ON_SAVE))	{
			
				   validateOnSave(callbacks, jsonResults, request, offeringLetter);
					
		} else if (frontEndAction.equalsIgnoreCase(UtilGilConstants.ROLLBACK_INDEXING))	{
			
			     rollbackIndexing(callbacks, jsonResults, request, offeringLetter);	
			
		}
	}
	
	
}
