package com.ibm.gil.customization.filters.request;

import javax.servlet.http.HttpServletRequest;



import com.ibm.ecm.extension.PluginRequestFilter;
import com.ibm.ecm.extension.PluginServiceCallbacks;
import com.ibm.gil.service.ServiceHelper;
import com.ibm.igf.contentmanager.rmi.ContentManagerImplementation;
import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONArtifact;
import com.ibm.json.java.JSONObject;
import com.ibm.mm.sdk.server.DKDatastoreICM;

/**
 * Provides an abstract class that is extended to create a filter for requests to a particular service. The filter is provided with the 
 * request parameters before being examined by the service. The filter can change the parameters or reject the request.
 */
public class SearchRequestFilter extends PluginRequestFilter {

	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(SearchRequestFilter.class);
	
	/**
	 * Returns the names of the services that are extended by this filter.
	 * 
	 * @return A <code>String</code> array that contains the names of the services.
	 */
	public String[] getFilteredServices() {
		return new String[] { "/cm/search" };
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
		try{
			ServiceHelper.setMDC(request,callbacks);
			logger.info("Executing search request filter for " + request.getPathInfo());
			//logger.debug(jsonRequest.toString());
			if(isDefault((JSONObject) jsonRequest)){
				String repositoryId = request.getParameter("repositoryId");
				Object synch = callbacks.getSynchObject(repositoryId, "cm");
				synchronized(synch){
					setDisplayResults(callbacks, request, (JSONObject) (jsonRequest));
					sortByTimestampIfNoIndex(callbacks.getCMDatastore(repositoryId), (JSONObject) (jsonRequest));
				}
			}
			
		} catch(Exception e){
			logger.error(e);
			
			errorMessage = new JSONObject();
			JSONArray listofErrors = new JSONArray();
			JSONObject error = new JSONObject();
			error.put("text", "Error while modifying search request");
			error.put("explanation", "Search could not be performed.");
			error.put("number", "1");
			error.put("userResponse", "An error occurred while filtering your search. " +
					"Please contact your system administrator to check logs.");
			listofErrors.add(error);
			errorMessage.put("errors", listofErrors);
			
			logger.error(errorMessage.toString());
		}finally{
			ServiceHelper.clearMDC();
		}
		
		return errorMessage;
	}
	
	/**
	 * Modifies jsonResults to display custom columns for each item type.
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
	private void setDisplayResults(PluginServiceCallbacks callbacks, HttpServletRequest request, JSONObject jsonRequest){
		DKDatastoreICM datastore = callbacks.getCMDatastore(request.getParameter("repositoryId"));
		ContentManagerImplementation cmi = new ContentManagerImplementation(datastore);
		JSONObject resultsDisplay = (JSONObject) (jsonRequest.get("resultsDisplay"));
		JSONArray columns = new JSONArray();
		String itemType = (String) (jsonRequest.get("template_name"));
		if(itemType == null && jsonRequest.containsKey("query")){
			itemType = (String) (jsonRequest.get("query"));
			int start = itemType.indexOf("/") + 1;
			int end = itemType.indexOf("[");
			itemType = itemType.substring(start, end);
			//logger.debug("itemType extracted from query: " + itemType);
		}
		try {
			String[] fields = cmi.getIndexFieldsForFiltering(itemType);
			for(String field : fields){
				columns.add(field);
			}
			resultsDisplay.put("columns", columns);
			//logger.debug("Fields retrieved for " + itemType + " item type:" + columns);
			
		} catch (Exception e) {
			logger.error(e.toString() + ": " + e.getMessage());
			logger.error(e.getCause());
		}
		
	}

	/**
	 * Checks if columns to be displayed is default.
	 * @param columns - a JSONArray containing the columns to be displayed on search result
	 * @return true if columns to be displayed are default. False, otherwise.
	 */
	private boolean isDefault(JSONObject jsonRequest) {
		JSONObject resultsDisplay = (JSONObject) (jsonRequest.get("resultsDisplay"));
		if(resultsDisplay == null) { // it came from worklist
			resultsDisplay = new JSONObject();
			jsonRequest.put("resultsDisplay", resultsDisplay);
			return true;
		}
		JSONArray columns = (JSONArray) (resultsDisplay.get("columns"));
		if(columns.size() == 4 &&
				((String)(columns.get(0))).equals("{NAME}") &&
				((String)(columns.get(1))).equals("contentSize") &&
				((String)(columns.get(2))).equals("modifiedBy") &&
				((String)(columns.get(3))).equals("modifiedTimestamp")
				){
			logger.info("Columns to be displayed are default. Search filter will be executed.");
			return true;
		}
		logger.info("Columns to be displayed are not default ones. Search filter will be ignored.");
		return false;
	}
	
	private void sortByTimestampIfNoIndex(DKDatastoreICM datastore, JSONObject jsonResponse) {
		String label = (String) (jsonResponse.get("template_label"));
		if(label == null)
			label = extractItemNameFromQuery(datastore, (String) (jsonResponse.get("query")));
		if(label != null && label.toLowerCase().contains("noindex")){
			logger.debug("Noindex search. Items will be sorted by timestamp.");
			setSortIndex((JSONObject) jsonResponse.get("resultsDisplay"));
		}
			
			
		
	}

	private String extractItemNameFromQuery(DKDatastoreICM datastore, String query) {
		ContentManagerImplementation cmi = new ContentManagerImplementation(datastore);
		String itemId = query.substring(query.indexOf("/") + 1, query.indexOf("["));
		String itemName = cmi.indexNameToDescription(itemId, datastore);
		if(itemName.equals(itemId))
			return null;
		else
			return itemName;
	}

	private void setSortIndex(JSONObject resultsDisplay) {
		JSONArray columns = (JSONArray) (resultsDisplay.get("columns"));
		for(int i = 0; i < columns.size(); i++){
			String field = (String) (columns.get(i));
			if(field.equalsIgnoreCase("timestamp")){
				resultsDisplay.put("sortBy", field);
				logger.debug("Search will be indexed by timestamp.");
			}
		}
		
	}

}
