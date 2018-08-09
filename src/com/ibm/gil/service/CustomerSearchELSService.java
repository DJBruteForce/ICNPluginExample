package com.ibm.gil.service;

import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import com.ibm.ecm.extension.PluginService;
import com.ibm.ecm.extension.PluginServiceCallbacks;
import com.ibm.ecm.json.JSONMessage;
import com.ibm.ecm.json.JSONResponse;
import com.ibm.gil.business.CustomerSearchELS;
import com.ibm.mm.sdk.server.DKDatastoreICM;

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
public class CustomerSearchELSService  extends PluginService {
	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(CustomerSearchELSService.class);

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
		return "CustomerSearchELSService";
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
	
	
	private CustomerSearchELS customerSearchELS = null;
	private String repositoryId = null;
	private String queryItem = null;
	private String userId = null;
	private String className = null;
	private String country = null;
	private String frontEndAction = null;
	private DKDatastoreICM datastore = null;
	private TimeZone userTimeZone = null;
	private String city=null;
	public void execute(PluginServiceCallbacks callbacks,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		
		ServiceHelper.setMDC(request,callbacks);
        JSONResponse jsonResults = new JSONResponse();
		
		repositoryId 	 = request.getParameter("repositoryId");
		userId 			 = request.getParameter("userId");
		country 		 = request.getParameter("country");
		frontEndAction	 = request.getParameter("frontEndAction");
		userTimeZone   	 = TimeZone.getTimeZone(request.getParameter("userTimeZone"));
		datastore 		 = callbacks.getCMDatastore(repositoryId);
		queryItem 	 	= request.getParameter("queryItem");
		city            = request.getParameter("city");
		
		customerSearchELS = new CustomerSearchELS(country);
		customerSearchELS.setQUERYITEM(queryItem);
		customerSearchELS.setDatastore(datastore);
		customerSearchELS.setLocale(callbacks.getLocale());
		customerSearchELS.setUserTimezone(userTimeZone);
		customerSearchELS.setUSERID(userId);
		customerSearchELS.setCUSTOMERADDRESS3(city);
		
		
		try {
			
			if (frontEndAction.equalsIgnoreCase("getAllByCustomerNumber"))	{
				
				
				// getAllByVendorNumber( callbacks, jsonResults, request);	
				
				logger.debug(" inside getAllByCustomerNumber ");
				getAllByCustomerNumber(callbacks, jsonResults, request);
				
			} else if (frontEndAction.equalsIgnoreCase("getAllByCustomerName"))	{
				
				        
					//getAllByVendorName(callbacks, jsonResults, request);
				getAllbyCustomerName(callbacks, jsonResults, request);
						
			}  

	}	catch(Exception e)	{
		
			//logger.fatal(e.getMessage());
			JSONMessage jsonErroMessage = new JSONMessage(0, "Indexing " + className +" failed. ", e.getMessage(), "","Check the IBM Content Navigator logs for more details.", "");
			jsonResults.addErrorMessage(jsonErroMessage);
			
	} finally {
		ServiceHelper.clearMDC();
	}
	
	jsonResults.serialize(response.getOutputStream());
		
		

	}
	
	
	
	private void getAllByCustomerNumber(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request)	throws Exception {
		
		String jsonTableGridValues = CustomerSearchELSServiceHelper.buildJsonForTableGrid(customerSearchELS.searchForCustomerNumber());
		
		logger.debug(" inside getAllByCustomerNumber jsontablegridvalues:"+jsonTableGridValues);
		jsonResults.put("jsonCustomerSearchGridValues",jsonTableGridValues);

	}
	
  private void getAllbyCustomerName(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request)	throws Exception {
		
		String jsonTableGridValues = CustomerSearchELSServiceHelper.buildJsonForTableGrid(customerSearchELS.searchForCustomerName());
		
		logger.debug(" inside getAllbyCustomerName jsontablegridvalues:"+jsonTableGridValues);
		
		jsonResults.put("jsonCustomerSearchGridValues",jsonTableGridValues);

	}
	
}
