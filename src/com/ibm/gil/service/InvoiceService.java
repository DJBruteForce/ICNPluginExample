package com.ibm.gil.service;

import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;



import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ibm.ecm.extension.PluginServiceCallbacks;
import com.ibm.ecm.json.JSONResponse;
import com.ibm.gil.business.Indexing;
import com.ibm.gil.business.Invoice;
import com.ibm.gil.model.DataModel;
import com.ibm.gil.util.UtilGilConstants;
import com.ibm.mm.sdk.server.DKDatastoreICM;

/** 
 * 
 * @author ferandra
 *
 */
public class InvoiceService  extends GILService {

	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(InvoiceService.class);

	public String getId() {
		return "InvoiceService";
	}

	public String getOverriddenService() {
		return null;
	}
	
	protected void indexDocument(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request, Indexing indexing)	throws Exception {

		
		Invoice invoice = ((Invoice) (indexing));
		invoice.initializeIndexing();
		ServiceHelper.putJsonResponse(jsonResults,"invoiceJsonString",invoice);
	}
	
	protected void saveDocument(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request, Indexing indexing)	throws Exception {
		
		
		
		Invoice invoice = ((Invoice) (indexing));
		String repositoryId = request.getParameter("repositoryId");
		TimeZone userTimeZone = TimeZone.getTimeZone(request.getParameter("userTimeZone"));
		DKDatastoreICM datastore = callbacks.getCMDatastore(repositoryId);
		JsonObject jsonDataObject= new JsonParser().parse(request.getParameter("jsonDataObject")).getAsJsonObject();
		invoice = (Invoice)ServiceHelper.jsonToObject(jsonDataObject, Invoice.class);
		invoice.getDialogErrorMsgs().clear();
		invoice.setDatastore(datastore);
		invoice.setUserTimezone(userTimeZone);		
		invoice.updateIndexValues();
		invoice.saveDocument();
		ServiceHelper.putJsonResponse(jsonResults,"invoiceJsonString",invoice);
	}
	
	private void addContract(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request, Invoice invoice)	throws Exception {
		
		
		String repositoryId = request.getParameter("repositoryId");
		DKDatastoreICM datastore = callbacks.getCMDatastore(repositoryId);
		JsonObject jsonDataObject= new JsonParser().parse(request.getParameter("jsonDataObject")).getAsJsonObject();
		invoice = (Invoice)ServiceHelper.jsonToObject(jsonDataObject, Invoice.class);
		invoice.setDatastore(datastore);
		invoice.getDialogErrorMsgs().clear();
		invoice.saveDocument();
		ServiceHelper.putJsonResponse(jsonResults,"invoiceJsonString",invoice);
		invoice.getInvoiceDataModel().getContentManager().clear();

	}
	
	private void validateOnSave(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request, Invoice invoice)	throws Exception {
		
		
		String repositoryId = request.getParameter("repositoryId");
		DKDatastoreICM datastore = callbacks.getCMDatastore(repositoryId);
		JsonObject jsonDataObject= new JsonParser().parse(request.getParameter("jsonDataObject")).getAsJsonObject();
		invoice = (Invoice)ServiceHelper.jsonToObject(jsonDataObject, Invoice.class);
		invoice.setDatastore(datastore);
		invoice.getDialogErrorMsgs().clear();
		invoice.validateInput();
		ServiceHelper.putJsonResponse(jsonResults,"invoiceJsonString",invoice);
		invoice.getInvoiceDataModel().getContentManager().clear();

	}
	
	private void defaults(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request, Invoice invoice)	throws Exception {
		
		
		String repositoryId = request.getParameter("repositoryId");
		DKDatastoreICM datastore = callbacks.getCMDatastore(repositoryId);
		JsonObject jsonDataObject= new JsonParser().parse(request.getParameter("jsonDataObject")).getAsJsonObject();
		invoice = (Invoice)ServiceHelper.jsonToObject(jsonDataObject, Invoice.class);
		invoice.setDatastore(datastore);
		invoice.defaultToLastInsertedRecord();
		ServiceHelper.putJsonResponse(jsonResults,"invoiceJsonString",invoice);
		invoice.getInvoiceDataModel().getContentManager().clear();
	}
	
	private void rollbackIndexing(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request, Invoice invoice)	throws Exception {
		
		invoice.rollbackIndexing();
		ServiceHelper.putJsonResponse(jsonResults,"invoiceJsonString",invoice);
		invoice.getInvoiceDataModel().getContentManager().clear();
	}
	
	private void rollbackCountryIndexing(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request, Invoice invoice)	throws Exception {
		
		invoice.rollbackCountryIndexing();
		ServiceHelper.putJsonResponse(jsonResults,"invoiceJsonString",invoice);
		invoice.getInvoiceDataModel().getContentManager().clear();
	}
	
	@Override
	protected void handleException(PluginServiceCallbacks callbacks, JSONResponse jsonResults,
			HttpServletRequest request, Indexing indexing, Exception e) {

		Invoice invoice = ((Invoice) (indexing));
		logger.fatal(e.getMessage(),e);
		invoice.getDialogErrorMsgs().add(e.getMessage());
		ServiceHelper.putJsonResponse(jsonResults,"invoiceJsonString",invoice);
	}

	@Override
	protected DataModel getDataModel(Indexing indexing) {
		return ((Invoice) (indexing)).getInvoiceDataModel();
	}
	

	@Override
	protected Indexing initializeVariables(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request) {

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
		
		Invoice invoice = new Invoice(country);
		invoice.setOBJECTID(ItemId);
		invoice.setINDEXCLASS(className);
		invoice.setDatastore(datastore);
		invoice.setLocale(callbacks.getLocale());
		invoice.setUserTimezone(userTimeZone);
		invoice.setCreatedTimeStamp(createdTimestamp);
		invoice.setUSERID(userId);
		
    	logger.info("User ID:" + invoice.getUSERID());
    	logger.info("Item Type:" + invoice.getINDEXCLASS());
    	logger.info("Country:" + invoice.getCOUNTRY());
    	logger.info("Object ID:" + invoice.getOBJECTID());
    	logger.info("Action:" +frontEndAction);

		return invoice;
	}
	

	@Override
	protected void selectAction(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request,
			Indexing indexing) throws Exception {

		String frontEndAction = request.getParameter("frontEndAction");
		Invoice invoice = ((Invoice) (indexing));

		if (frontEndAction.equalsIgnoreCase(UtilGilConstants.INDEX))	{
			
			index( callbacks, jsonResults, request, invoice);
			
		} else if (frontEndAction.equalsIgnoreCase(UtilGilConstants.SAVE))	{
			
				   save(callbacks, jsonResults, request, invoice);
					
		} else if (frontEndAction.equalsIgnoreCase(UtilGilConstants.ADD_CONTRACT))	{
			
					addContract(callbacks, jsonResults, request, invoice);
					
		} else if (frontEndAction.equalsIgnoreCase(UtilGilConstants.VALIDATE_ON_SAVE))	{
			
				   validateOnSave(callbacks, jsonResults, request, invoice);
					
		} else if (frontEndAction.equalsIgnoreCase(UtilGilConstants.DEFAULTS))	{
			
			      defaults(callbacks, jsonResults, request, invoice);	
					
		}else if (frontEndAction.equalsIgnoreCase(UtilGilConstants.ROLLBACK_INDEXING))	{
			
			     rollbackIndexing(callbacks, jsonResults, request, invoice);	
			
		}else if (frontEndAction.equalsIgnoreCase(UtilGilConstants.ROLLBACK_COUNTRY_INDEXING))	{
			
			     rollbackCountryIndexing(callbacks, jsonResults, request, invoice);	
		}
	}
	
	
}
