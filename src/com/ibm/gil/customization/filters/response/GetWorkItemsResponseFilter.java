package com.ibm.gil.customization.filters.response;

import javax.servlet.http.HttpServletRequest;

import com.ibm.ecm.extension.PluginResponseFilter;
import com.ibm.ecm.extension.PluginServiceCallbacks;
import com.ibm.ecm.json.JSONResultSetResponse;
import com.ibm.gil.customization.filters.request.SearchRequestFilter;
import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;

/**
 * Provides an abstract class that is extended to create a filter for responses
 * from a particular service. The response from the service is provided to the
 * filter in JSON format before it is returned to the web browser. The filter
 * can then modify that response, and the modified response is returned to the
 * web browser.
 */
public class GetWorkItemsResponseFilter extends PluginResponseFilter {

	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(SearchRequestFilter.class);
	
	/**
	 * Returns an array of the services that are extended by this filter.
	 * 
	 * @return A <code>String</code> array of names of the services. These are
	 *         the servlet paths or Struts action names.
	 */
	public String[] getFilteredServices() {
		return new String[] { "/cm/getWorkItems" };
	}

	/**
	 * Filters the response from the service.
	 * 
	 * @param serverType
	 *            A <code>String</code> that indicates the type of server that
	 *            is associated with the service. This value can be one or more
	 *            of the following values separated by commas:
	 *            <table border="1">
	 *            <tr>
	 *            <th>Server Type</th>
	 *            <th>Description</th>
	 *            </tr>
	 *            <tr>
	 *            <td><code>p8</code></td>
	 *            <td>IBM FileNet P8</td>
	 *            </tr>
	 *            <tr>
	 *            <td><code>cm</code></td>
	 *            <td>IBM Content Manager</td>
	 *            </tr>
	 *            <tr>
	 *            <td><code>od</code></td>
	 *            <td>IBM Content Manager OnDemand</td>
	 *            </tr>
	 *         	  <tr>
	 *         		<td><code>cmis</code></td>
	 *         		<td>Content Management Interoperability Services</td>
	 *         	  </tr>
	 *            <tr>
	 *            <td><code>common</code></td>
	 *            <td>For services that are not associated with a particular
	 *            server</td>
	 *            </tr>
	 *            </table>
	 * @param callbacks
	 *            An instance of the
	 *            <code>{@link com.ibm.ecm.extension.PluginServiceCallbacks PluginServiceCallbacks}</code>
	 *            class that contains functions that can be used by the service.
	 *            These functions provide access to plug-in configuration and
	 *            content server APIs.
	 * @param request
	 *            An <code>HttpServletRequest</code> object that provides the
	 *            request. The service can access the invocation parameters from
	 *            the request.
	 * @param jsonResponse
	 *            The <code>JSONObject</code> object that is generated by the
	 *            service. Typically, this object is serialized and sent as the
	 *            response. The filter modifies this object to change the
	 *            response that is sent.
	 * @throws Exception
	 *             For exceptions that occur when the service is running.
	 *             Information about the exception is logged as part of the
	 *             client logging and an error response is automatically
	 *             generated and returned.
	 */
	public void filter(String serverType, PluginServiceCallbacks callbacks,
			HttpServletRequest request, JSONObject jsonResponse) throws Exception {
		JSONResultSetResponse resultSet = (JSONResultSetResponse) jsonResponse;
		
		try {
			JSONArray cells = (JSONArray) ((JSONArray) (((JSONObject) resultSet.get("columns")).get("cells"))).get(0);
			if(cells.size() > 3){
				logger.debug("Columns size greather than 3. Reordering columns");
				reorderColumns(cells);
			}
		} catch(Exception e){
			logger.error(e);
		}
	}

	private void reorderColumns(JSONArray cells) {
		JSONObject aux = (JSONObject) cells.get(3);
		int i;
		for(i = 0; i < cells.size(); i++){
			JSONObject current = (JSONObject) cells.get(i);
			if(current.containsKey("field") && ((String) current.get("field")).equals("processLastMovedDate")){
				cells.set(3, current);
				cells.set(i, aux);
				logger.debug("Last Moved Date Columns found");
			}
		}
		
	}
	
	
}
