package com.ibm.gil.customization.filters.request;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;



import com.ibm.ecm.extension.PluginRequestFilter;
import com.ibm.ecm.extension.PluginServiceCallbacks;
import com.ibm.gil.service.ServiceHelper;
import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONArtifact;
import com.ibm.json.java.JSONObject;

/**
 * Provides an abstract class that is extended to create a filter for requests to a particular service. The filter is provided with the 
 * request parameters before being examined by the service. The filter can change the parameters or reject the request.
 */
public class AddItemRequestFilter extends PluginRequestFilter {

	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(AddItemRequestFilter.class);

	/**
	 * Returns the names of the services that are extended by this filter.
	 * 
	 * @return A <code>String</code> array that contains the names of the services.
	 */
	public String[] getFilteredServices() {
		return new String[] { "/cm/addItem" };
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
		JSONObject errorMessage = null;
		String itemType = null;
		try {
			ServiceHelper.setMDC(request,callbacks);
			logger.info("Executing add item request filter for " + request.getPathInfo());
			String repositoryId = request.getParameter("repositoryId");
			Object synch = callbacks.getSynchObject(repositoryId, "cm");
			synchronized(synch){
				setImmutableFields(callbacks, request, (JSONObject) ((JSONArray) (jsonRequest)).get(0));
			}
			
			
		} catch(Exception e){
			logger.error(e.toString() + ": " + e.getMessage());
			
			errorMessage = new JSONObject();
			JSONArray listofErrors = new JSONArray();
			JSONObject error = new JSONObject();
			error.put("text", "Error while modifying add item request");
			error.put("explanation", "Item could not be added.");
			error.put("number", "2");
			error.put("userResponse", "An error occurred while adding this item. " +
					"Please contact your system administrator to check logs.");
			listofErrors.add(error);
			errorMessage.put("errors", listofErrors);
			
			logger.error(errorMessage.toString());
		} finally{
			ServiceHelper.clearMDC();
		}
		
		
		return errorMessage;
	}

	/**
	 * Removes fields that shouldn't be altered by the user from the json cointaining values
	 * to update an item.
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
	 */
	private void setImmutableFields(PluginServiceCallbacks callbacks, HttpServletRequest request,
			JSONObject jsonObject) {
		JSONArray criterias = (JSONArray) (jsonObject.get("criterias"));
		for(Object obj : criterias){
			JSONObject attr = (JSONObject) (obj);
			if(((String) attr.get("name")).equals("USER_ID")){
				attr.put("value", callbacks.getUserId().toUpperCase());
				
			} else if (((String) attr.get("name")).equals("TIMESTAMP")){
				setTimestampValue(attr);
				
			} else if (((String) attr.get("name")).equals("SOURCE")){
				attr.put("value", "IMPORT");
				
			}
		}
		
		
	}

	/**
	 * Updates the timestamp value on jsonRequest
	 * 
	 * @param attr - The timestamp json object.
	 */
	private void setTimestampValue(JSONObject attr) {
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZZ");
		String ts = sdf.format(now);
		ts = ts.substring(0, 26) + ":" + ts.substring(26); // needed as yyyy-MM-dd'T'HH:mm:ss.SSSXXX doesn't work on Java 6.
		logger.debug("Generated date: " + ts);
		attr.put("value", ts);
		
	}
	
}
