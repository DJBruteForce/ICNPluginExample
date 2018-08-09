package com.ibm.gil.customization.filters.request;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;



import com.ibm.ecm.extension.PluginRequestFilter;
import com.ibm.ecm.extension.PluginServiceCallbacks;
import com.ibm.gil.exception.GILCNException;
import com.ibm.gil.service.ServiceHelper;
import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONArtifact;
import com.ibm.json.java.JSONObject;

/**
 * Provides an abstract class that is extended to create a filter for requests to a particular service. The filter is provided with the 
 * request parameters before being examined by the service. The filter can change the parameters or reject the request.
 */
public class EditAttributesRequestFilter extends PluginRequestFilter {

	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(EditAttributesRequestFilter.class);

	/**
	 * Returns the names of the services that are extended by this filter.
	 * 
	 * @return A <code>String</code> array that contains the names of the services.
	 */
	public String[] getFilteredServices() {
		return new String[] { "/cm/editAttributes" };
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

		try {
			ServiceHelper.setMDC(request,callbacks);
			logger.info("Executing edit attributes request filter for " + request.getPathInfo());
			logger.debug(jsonRequest.toString());
			String repositoryId = request.getParameter("repositoryId");
			Object synch = callbacks.getSynchObject(repositoryId, "cm");
			synchronized(synch){
				setImmutableFields(callbacks, request, (JSONObject) ((JSONArray) (jsonRequest)).get(0));
				if(isMiscELS(request.getParameter("new_template_name")) && 
						!isValidMisc((JSONObject) ((JSONArray) (jsonRequest)).get(0))){
					logger.info("Invalid misc document");
					errorMessage = new JSONObject();
					JSONArray listofErrors = new JSONArray();
					JSONObject error = new JSONObject();
					error.put("text", "Invalid Misc ELS document");
					error.put("explanation", "A Misc ELS document must be indexed against either an" +
							" OL, COA or Invoice number.");
					error.put("number", "4");
					error.put("userResponse", "Make sure to fill at least one of these fields: GCPS OL Number, " + 
					"GCPS COA Number or  Invoice Number.");
					listofErrors.add(error);            
					errorMessage.put("errors", listofErrors);
					
				}
				if(isNotelogEdited(callbacks, request, (JSONObject) ((JSONArray) (jsonRequest)).get(0))){
					throw new GILCNException("Notelog is being edited. Canceling item edit request.");
				}
			}
			
			
		} catch(Exception e){
			logger.error(e.toString() + ": " + e.getMessage());
			e.printStackTrace();
			
			
			errorMessage = new JSONObject();
			JSONArray listofErrors = new JSONArray();
			JSONObject error = new JSONObject();
			error.put("text", "Error while modifying edit item request");
			error.put("explanation", "Item could not be edited.");
			error.put("number", "3");
			error.put("userResponse", "An error occurred while editing this item. " +
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
	 * Checks if the Notelog of an item is on edit mode.
	 * 
	 * @param jsonObject - the jsonObject containing the edit item request.
	 * @return - True if the item Notelog is on edit mode. False, otherwise.
	 */
	private boolean isNotelogEdited(PluginServiceCallbacks callbacks, HttpServletRequest request,
			JSONObject jsonObject) {
		JSONArray criterias = (JSONArray) (jsonObject.get("criterias"));
		for(Object obj : criterias){
			JSONObject attr = (JSONObject) (obj);
			if(attr.containsKey("notelogType") && ((String) attr.get("notelogType")).equals("edit") &&
					attr.containsKey("editMode") && ((Boolean) attr.get("editMode")).equals(true))
				return true;
		}		
		return false;
	}

	/**
	 * Checks if Misc ELS attributes are valid.
	 * 
	 * @param jsonObject - A Json object containing an array with Misc ELS attributes.
	 * @return True if the Misc ELS is valid. False, otherwise.
	 */
	private boolean isValidMisc(JSONObject jsonObject) {
		JSONArray criterias = (JSONArray) (jsonObject.get("criterias"));
		int blankFields = 0;
		for(Object obj : criterias){
			JSONObject attr = (JSONObject) (obj);
			if(isRequired(attr) && isBlank(attr)){
				blankFields++;
			}
		}
		
		if(blankFields == 3){
			return false;
		}
		return true;
	}

	/**
	 * Checks if a json object value is empty.
	 * 
	 * @param attr - The json object which value will be checked.
	 * @return True if the given json value is empty. False otherwise.
	 */
	private boolean isBlank(JSONObject attr) {
		if(((String) attr.get("value")).trim().length() == 0)
			return true;
		return false;
	}

	/**
	 * Checks if a json object is a required field for Misc ELS.
	 * 
	 * @param attr - A Json object containing the attribute to be checked.
	 * @return True if the given json object is a OL, COA or Invoice number. False, otherwise.
	 */
	private boolean isRequired(JSONObject attr) {
		if(((String) attr.get("name")).equals("GCPS_OL_Number") ||
				((String) attr.get("name")).equals("GCPS_COA_Number") ||
				((String) attr.get("name")).equals("Invoice_Number")){
			return true;
		}
		return false;
	}

	/**
	 * Checks if the given template is a Misc ELS.
	 * 
	 * @param indexClass - A String containing the template name.
	 * @return True if the given string matches a Misc ELS template. False, otherwise.
	 */
	private boolean isMiscELS(String indexClass) {
		if(indexClass.startsWith("GCPS_MISC_") && indexClass.length() == 12)
			return true;
		return false;
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
		ArrayList<Object> immutableFields = new ArrayList<Object>();
		for(Object obj : criterias){
			JSONObject attr = (JSONObject) (obj);
			if(isImmutable(attr)){
				immutableFields.add(attr);
			}
		}
		criterias.removeAll(immutableFields);
		
	}
	
	/**
	 * Checks if the given attribute is either Source, Timestamp or User Id.
	 * 
	 * @param attr - the json attribute to be checked.
	 * @return True if the given json represents Source, Timestamp or User Id. False, otherwise.
	 */
	private boolean isImmutable(JSONObject attr) {
		Object name = attr.get("name");
		if( name != null && (((String) name).equals("USER_ID") ||
				((String) name).equals("TIMESTAMP") || ((String) name).equals("SOURCE"))){
			return true;
		}
		return false;
	}
}
