package com.ibm.gil.customization.filters.request;

import javax.servlet.http.HttpServletRequest;



import com.ibm.ecm.extension.PluginRequestFilter;
import com.ibm.ecm.extension.PluginServiceCallbacks;
import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONArtifact;
import com.ibm.json.java.JSONObject;

/**
 * Provides an abstract class that is extended to create a filter for requests to a particular service. The filter is provided with the 
 * request parameters before being examined by the service. The filter can change the parameters or reject the request.
 */
public class EmailRequestFilter extends PluginRequestFilter {

	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(EmailRequestFilter.class);

	/**
	 * Returns the names of the services that are extended by this filter.
	 * 
	 * @return A <code>String</code> array that contains the names of the services.
	 */
	public String[] getFilteredServices() {
		return new String[] { "/email" };
	}

	/**
	 * Filters a request that is submitted to a service.
	 * 
	 * @param callbacks
	 *            An instance of <code>PluginServiceCallbacks</code> that contains several functions that can be used by the
	 *            service. These functions provide access to plug-in configuration and content server APIs.
	 * @param request
	 *            The <code>HttpServletRequest</code> object that provides the request. The service can access the invocation parameters from the
	 *            request. <strong>Note:</strong> The request object can be passed to a response filter to allow a plug-in to communicate 
	 *            information between a request and response filter.
	 * @param jsonRequest
	 *            A <code>JSONArtifact</code> that provides the request in JSON format. If the request does not include a <code>JSON Artifact</code>  
	 *            object, this parameter returns <code>null</code>.
	 * @return A <code>JSONObject</code> object. If this object is not <code>null</code>, the service is skipped and the
	 *            JSON object is used as the response.
	 */
	public JSONObject filter(PluginServiceCallbacks callbacks, HttpServletRequest request, JSONArtifact jsonRequest) throws Exception {
		logger.debug("Adding \"reply to\" info on e-mail's message.");
		JSONObject errorMessage = null;
		
		try{
			JSONObject jr = (JSONObject) jsonRequest;
			logger.debug(jsonRequest);
			jr.put("from", "igfgiadm@us.ibm.com");
			addFooterMessage((JSONObject) (jsonRequest));
			
			logger.info("User [USERNAME] attempted to send an e-mail with SUBJECT: " + jr.get("subject") + ", FROM: " + 
			jr.get("from") + ", TO: " + jr.get("to") + ", CC: " + jr.get("cc") + " and BCC: " + jr.get("bcc"));
		} catch(Exception e){
			errorMessage = new JSONObject();
			JSONArray listofErrors = new JSONArray();
			JSONObject error = new JSONObject();
			error.put("text", "The e-mail could not be sent.");
			error.put("explanation", "An error occurred when sending your e-mail request.");
			error.put("number", "5");
			error.put("userResponse", "Please contact your system administrator to check logs.");
			listofErrors.add(error);            
			errorMessage.put("errors", listofErrors);
			logger.error(e);
		}
		
		return errorMessage;
	}

	private void addFooterMessage(JSONObject request) {
		String fromEmail = (String) (request.get("from"));
		String message = (String) (request.get("message"));
		message += "\n\nThis e-mail was sent by an automatic system. Please do not reply to \"" + fromEmail +
				"\" as these messages will be ignored.";
		request.put("message", message);
		
	}
}
