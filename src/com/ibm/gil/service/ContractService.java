package com.ibm.gil.service;

import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;



import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ibm.ecm.extension.PluginServiceCallbacks;
import com.ibm.ecm.json.JSONResponse;
import com.ibm.gil.business.Contract;
import com.ibm.gil.business.Indexing;
import com.ibm.gil.model.DataModel;
import com.ibm.gil.util.UtilGilConstants;
import com.ibm.mm.sdk.server.DKDatastoreICM;

public class ContractService extends GILService {

	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(ContractService.class);

	public String getId() {
		return "ContractService";
	}

	public String getOverriddenService() {
		return null;
	}

	private void validateOnSave(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request,
			Contract contract) throws Exception {

		JsonObject jsonDataObject = new JsonParser().parse(request.getParameter("jsonDataObject")).getAsJsonObject();
		String repositoryId = request.getParameter("repositoryId");
		contract = (Contract) ServiceHelper.jsonToObject(jsonDataObject, Contract.class);
		contract.setDatastore(callbacks.getCMDatastore(repositoryId));
		contract.validateInput();
		ServiceHelper.putJsonResponse(jsonResults,"contractJsonString",contract);
		contract.getContractDataModel().getContentManager().clear();
		
	}

	private void rollbackIndexing(PluginServiceCallbacks callbacks, JSONResponse jsonResults,
			HttpServletRequest request, Contract contract) throws Exception {

		JsonObject jsonDataObject = new JsonParser().parse(request.getParameter("jsonDataObject")).getAsJsonObject();
		contract.setCONTRACTDATE(jsonDataObject.get("CONTRACTDATE").getAsString());
		contract.setOLDCONTRACTDATE(jsonDataObject.get("OLDCONTRACTDATE").getAsString());
		contract.rollbackIndexing();
		contract.getContractDataModel().unlockItem();
		ServiceHelper.putJsonResponse(jsonResults,"contractJsonString",contract);
		contract.getContractDataModel().getContentManager().clear();
		
	}

	private void rollbackCountryIndexing(PluginServiceCallbacks callbacks, JSONResponse jsonResults,
			HttpServletRequest request, Contract contract) throws Exception {

		contract.rollbackCountryIndexing();
		ServiceHelper.putJsonResponse(jsonResults,"contractJsonString",contract);
		contract.getContractDataModel().getContentManager().clear();
		
	}

	@Override
	protected Indexing initializeVariables(PluginServiceCallbacks callbacks, JSONResponse jsonResults,
			HttpServletRequest request) {

		String repositoryId = request.getParameter("repositoryId");
		JsonObject jsonBusinessAttributes = new JsonParser().parse(request.getParameter("businessAttributes")).getAsJsonObject();
		String ItemId = request.getParameter("itemId");
		String userId = request.getParameter("userId");
		String className = request.getParameter("className");
		String country = request.getParameter("country");
		String frontEndAction = request.getParameter("frontEndAction");
		TimeZone userTimeZone = TimeZone.getTimeZone(request.getParameter("userTimeZone"));
		DKDatastoreICM datastore = callbacks.getCMDatastore(repositoryId);
		String createdTimestamp = jsonBusinessAttributes.get("createdTimestamp").getAsString();

		Contract contract = new Contract();
		contract.setCOUNTRY(country);
		contract.setOBJECTID(ItemId);
		contract.setINDEXCLASS(className);
		contract.setDatastore(datastore);
		contract.setLocale(callbacks.getLocale());
		contract.setUserTimezone(userTimeZone);
		contract.setCreatedTimeStamp(createdTimestamp);
		contract.setUSERID(userId);

		logger.info("User ID:" + contract.getUSERID());
		logger.info("Item Type:" + contract.getINDEXCLASS());
		logger.info("Country:" + contract.getCOUNTRY());
		logger.info("Object ID:" + contract.getOBJECTID());
		logger.info("Action:" + frontEndAction);

		return contract;
	}

	@Override
	protected void selectAction(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request,
			Indexing indexing) throws Exception {

		String frontEndAction = request.getParameter("frontEndAction");
		Contract contract = ((Contract) (indexing));

		if (frontEndAction.equalsIgnoreCase(UtilGilConstants.INDEX)) {
			index(callbacks, jsonResults, request, contract);

		} else if (frontEndAction.equalsIgnoreCase(UtilGilConstants.SAVE)) {
			save(callbacks, jsonResults, request, contract);

		} else if (frontEndAction.equalsIgnoreCase(UtilGilConstants.VALIDATE_ON_SAVE)) {
			validateOnSave(callbacks, jsonResults, request, contract);

		} else if (frontEndAction.equalsIgnoreCase(UtilGilConstants.ROLLBACK_INDEXING)) {
			rollbackIndexing(callbacks, jsonResults, request, contract);

		} else if (frontEndAction.equalsIgnoreCase(UtilGilConstants.ROLLBACK_COUNTRY_INDEXING)) {
			rollbackCountryIndexing(callbacks, jsonResults, request, contract);
		}

	}

	@Override
	protected void handleException(PluginServiceCallbacks callbacks, JSONResponse jsonResults,
			HttpServletRequest request, Indexing indexing, Exception e) {

		Contract contract = ((Contract) (indexing));
		logger.fatal(e.getMessage(),e);
		contract.getDialogErrorMsgs().add(e.getMessage());
		ServiceHelper.putJsonResponse(jsonResults,"contractJsonString",contract);
		

	}

	@Override
	protected DataModel getDataModel(Indexing indexing) {
		return ((Contract) (indexing)).getContractDataModel();
	}

	@Override
	protected void indexDocument(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request,
			Indexing indexing) throws Exception {

		Contract contract = ((Contract) (indexing));
		contract.initalizeIndexing();
		ServiceHelper.putJsonResponse(jsonResults,"contractJsonString",contract);
		contract.getContractDataModel().getContentManager().clear();
		
	}

	@Override
	protected void saveDocument(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request,
			Indexing indexing) throws Exception {

		String repositoryId = request.getParameter("repositoryId");
		Contract contract = ((Contract) (indexing));
		JsonObject jsonDataObject = new JsonParser().parse(request.getParameter("jsonDataObject")).getAsJsonObject();
		contract = (Contract) ServiceHelper.jsonToObject(jsonDataObject, Contract.class);
		contract.setDatastore(callbacks.getCMDatastore(repositoryId));
		contract.setUserTimezone(TimeZone.getTimeZone(request.getParameter("userTimeZone")));
		contract.updateIndexValues();
		contract.saveDocument();
		ServiceHelper.putJsonResponse(jsonResults,"contractJsonString",contract);
		contract.getContractDataModel().getContentManager().clear();

	}
}
