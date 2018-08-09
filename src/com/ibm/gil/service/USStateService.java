package com.ibm.gil.service;

import javax.servlet.http.HttpServletRequest;






import com.ibm.ecm.extension.PluginServiceCallbacks;
import com.ibm.ecm.json.JSONResponse;
import com.ibm.gil.business.Indexing;

import com.ibm.gil.business.USState;
import com.ibm.gil.model.DataModel;
import com.ibm.gil.util.CNUtilConstants;
import com.ibm.gil.util.UtilGilConstants;


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
public class USStateService  extends GILService {
	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(USStateService.class);
	
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
		return "USStateService";
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

	/**
	 * Performs the action of this service.
	 * 
	 * @param callbacks
	 *            An instance of the <code>PluginServiceCallbacks</code> class
	 *            that contains several functions that can be used by the
	 *            service. These functions provide access to the plug-in
	 *            configuration and content server APIs.
	 * @param request
	 *            The <code>HttpServletRequest</code> object that provides the
	 *            request. The service can access the invocation parameters from
	 *            the request.
	 * @param response
	 *            The <code>HttpServletResponse</code> object that is generated
	 *            by the service. The service can get the output stream and
	 *            write the response. The response must be in JSON format.
	 * @throws Exception
	 *             For exceptions that occur when the service is running. If the
	 *             logging level is high enough to log errors, information about
	 *             the exception is logged by IBM Content Navigator.
	 */
	private  void buildResponse(JSONResponse jsonResponse, USState usState) throws Exception{
		String usStateObject = ServiceHelper.objectToJson(usState);
		
//		logger.debug("Us Json:"+jsonString);
		
		jsonResponse.put(CNUtilConstants.US_STATE_JSON,usStateObject);
		
//		jsonResponse.put("createdTimestamp", "");
		
		logger.debug("USState Json:"+ServiceHelper.prettyPrint(usStateObject));

	}
	
	 public void initializeUSStateCodes(USState usState){
		 
			
				//load state codes into usState
	    	try{
	    		//load state codes into usState
				 usState.getUsStateDataModel().getUSStateCodes();
				
			}catch(Exception exc){
		           logger.fatal(exc.toString());
		           logger.error("Error initializing US State Codes");
		           usState.getDialogErrors().add("Error initializing US State Codes");
			}
			

			
	    }
	 public void validateInput(){
//done in frontend
		}

	@Override
	protected Indexing initializeVariables(PluginServiceCallbacks callbacks, JSONResponse jsonResults,
			HttpServletRequest request) throws Exception {
		String country	=request.getParameter("country");
		USState usState=new USState(country);
		return usState;
	}

	@Override
	protected void selectAction(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request,
			Indexing indexing) throws Exception {
		String optionToExecute = request.getParameter("request");
		USState usState=(USState)indexing;
		
		
		if(optionToExecute.equals(UtilGilConstants.INIT)){
			initializeUSStateCodes(usState);
		}
		
		buildResponse(jsonResults, usState);
		
	}

	@Override
	protected void handleException(PluginServiceCallbacks callbacks, JSONResponse jsonResults,
			HttpServletRequest request, Indexing indexing, Exception e) {

		USState usState= (USState)indexing;
		logger.fatal(e.getMessage());
		usState.getDialogErrors().add(e.getMessage());
	}

	@Override
	protected DataModel getDataModel(Indexing indexing) {
		
		return ((USState)indexing).getUsStateDataModel();
	}

	@Override
	protected void indexDocument(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request,
			Indexing indexing) throws Exception {
		//nothing to do here
		
	}

	@Override
	protected void saveDocument(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request,
			Indexing indexing) throws Exception {
		// nothing to do here
		
	}
		
}
