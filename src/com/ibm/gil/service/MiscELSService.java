package com.ibm.gil.service;

import javax.servlet.http.HttpServletRequest;



import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ibm.ecm.extension.PluginServiceCallbacks;
import com.ibm.ecm.json.JSONResponse;
import com.ibm.gil.business.Indexing;
import com.ibm.gil.business.MiscELS;
import com.ibm.gil.model.DataModel;

/**
 * Provides an abstract class that is extended to create a class implementing
 * each service provided by the plug-in. Services are actions, similar to
 * servlets or Struts actions, that perform operations on the IBM Content
 * Navigator server. A service can access content server application programming
 * interfaces (APIs) and Java EE APIs.
 * <p>
 * Services are invoked from the JavaScript functions that are defined for the
 * plug-in by using the <code>ecm.model.Request.invokePluginService</code>
 * function.
 * </p>
 * Follow best practices for servlets when implementing an IBM Content Navigator
 * plug-in service. In particular, always assume multi-threaded use and do not
 * keep unshared information in instance variables.
 */
public class MiscELSService extends GILService {

	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(MiscELSService.class);

	/**
	 * Returns the unique identifier for this service.
	 * <p>
	 * <strong>Important:</strong> This identifier is used in URLs so it must
	 * contain only alphanumeric characters.
	 * </p>
	 * 
	 * @return A <code>String</code> that is used to identify the service.
	 */
	public String getId() {
		return "MiscELSService";
	}

	/**
	 * Returns the name of the IBM Content Navigator service that this service
	 * overrides. If this service does not override an IBM Content Navigator
	 * service, this method returns <code>null</code>.
	 * 
	 * @returns The name of the service.
	 */
	public String getOverriddenService() {
		return null;
	}

	@Override
	protected Indexing initializeVariables(PluginServiceCallbacks callbacks, JSONResponse jsonResults,
			HttpServletRequest request) throws Exception {

		JsonObject busAttr = new JsonParser().parse(request.getParameter("businessAttributes")).getAsJsonObject();
		logger.debug("initializeVariables - Business Attributes: " + busAttr.toString());
		MiscELS misc = new MiscELS(request.getParameter("className").substring(0, 2)); //get country
		misc.setUSERID(request.getParameter("userId"));
		misc.setINDEXCLASS(request.getParameter("className"));
		misc.setOBJECTID(request.getParameter("itemId"));
		
		misc.setSubject(busAttr.get("subject").getAsString());
		misc.setLegacyCustomerNumber(busAttr.get("lCustNumber").getAsString());
		misc.setGcpsCustomerName(busAttr.get("gcpsCustName").getAsString());
		
		misc.setOFFERINGNUMBER(busAttr.get("gcpsOLNumber").getAsString());
		misc.setCOANUMBER(busAttr.get("gcpsCOANumber").getAsString());
		misc.setINVOICENUMBER(busAttr.get("invoiceNumber").getAsString());
		misc.setSOURCE(busAttr.get("source").getAsString());
		misc.setTIMESTAMP(busAttr.get("createdTimestamp").getAsString());
		misc.setUSERID(busAttr.get("userId").getAsString());
		setWSEndPoint(request.getSession(), misc);
		
		misc.setDatastore(callbacks.getCMDatastore(request.getParameter("repositoryId")));
		
		return misc;
	}

	@Override
	protected void selectAction(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request,
			Indexing indexing) throws Exception {
		String action = request.getParameter("frontEndAction");
		if(action.equalsIgnoreCase("index")){
			index(callbacks, jsonResults, request, indexing);
			save(callbacks, jsonResults, request, indexing); //does nothing but unlocking the item
		} else{
			logger.error("Unknown action for MiscELSService: " + action);
		}
		
	}

	@Override
	protected void handleException(PluginServiceCallbacks callbacks, JSONResponse jsonResults,
			HttpServletRequest request, Indexing indexing, Exception e) {
		logger.error("Error executing action: " + request.getParameter("frontEndAction"));
		logger.error(e.getMessage());
		
	}

	@Override
	protected DataModel getDataModel(Indexing indexing) {
		return ((MiscELS)(indexing)).getDataModel();
	}

	@Override
	protected void indexDocument(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request,
			Indexing indexing) throws Exception {
		MiscELS misc = ((MiscELS)(indexing));
		boolean wasSuccessful = false;
		if(misc.isValid()){
			wasSuccessful = misc.createMiscDocument();
		}
		jsonResults.put("wasSuccessful", wasSuccessful);
		
	}

	@Override
	protected void saveDocument(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request,
			Indexing indexing) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	
}
