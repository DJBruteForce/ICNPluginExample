package com.ibm.gil.service;

import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import com.ibm.ecm.extension.PluginService;
import com.ibm.ecm.extension.PluginServiceCallbacks;
import com.ibm.ecm.json.JSONMessage;
import com.ibm.ecm.json.JSONResponse;
import com.ibm.gil.business.VendorSearchELS;
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
public class VendorSearchELSService  extends PluginService {
	
	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(VendorSearchELSService.class);

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
		return "VendorSearchELSService";
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
	
	
	private VendorSearchELS vendorSearchELS = null;
	private String repositoryId = null;
	private String queryItem = null;
	private String userId = null;
	private String className = null;
	private String country = null;
	private String frontEndAction = null;
	private DKDatastoreICM datastore = null;
	private TimeZone userTimeZone = null;
	
	public void execute(PluginServiceCallbacks callbacks,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		
        JSONResponse jsonResults = new JSONResponse();
        ServiceHelper.setMDC(request,callbacks);
		
		repositoryId 	 = request.getParameter("repositoryId");
		userId 			 = request.getParameter("userId");
		country 		 = request.getParameter("country");
		frontEndAction	 = request.getParameter("frontEndAction");
		userTimeZone   	 = TimeZone.getTimeZone(request.getParameter("userTimeZone"));
		datastore 		 = callbacks.getCMDatastore(repositoryId);
		queryItem 	 	= request.getParameter("queryItem");
		
		
		
		vendorSearchELS = new VendorSearchELS(country);
		vendorSearchELS.setQUERYITEM(queryItem);
		vendorSearchELS.setDatastore(datastore);
		vendorSearchELS.setLocale(callbacks.getLocale());
		vendorSearchELS.setUserTimezone(userTimeZone);
		vendorSearchELS.setUSERID(userId);
		
		
		try {
			
			if (frontEndAction.equalsIgnoreCase("getAllByVendorNumber"))	{
				
				
				 getAllByVendorNumber( callbacks, jsonResults, request);			
				
			} else if (frontEndAction.equalsIgnoreCase("getAllByVendorName"))	{
				
				        
						getAllByVendorName(callbacks, jsonResults, request);
						
			} else if (frontEndAction.equalsIgnoreCase("getAllByVat"))	{
				         
						getAllByAccount(callbacks, jsonResults, request);
						
			} 

	}	catch(Exception e)	{
		
			logger.fatal(e.getMessage());
			JSONMessage jsonErroMessage = new JSONMessage(0, "Indexing " + className +" failed. ", e.getMessage(), "","Check the IBM Content Navigator logs for more details.", "");
			jsonResults.addErrorMessage(jsonErroMessage);	
	} finally{
		ServiceHelper.clearMDC();
	}
	
	jsonResults.serialize(response.getOutputStream());
		


	}
	
	private void getAllByVendorNumber(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request)	throws Exception {
		
		String jsonTableGridValues = VendorSearchELSServiceHelper.buildJsonForTableGrid(vendorSearchELS.selectSupplierNumberStatement());
		
		jsonResults.put("jsonVendorSearchGridValues",jsonTableGridValues);
		
		
	}
	
	
	private void getAllByVendorName(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request)	throws Exception {
		
		String jsonTableGridValues = VendorSearchELSServiceHelper.buildJsonForTableGrid(vendorSearchELS.selectSupplierNameStatement());
		
		jsonResults.put("jsonVendorSearchGridValues",jsonTableGridValues);

	}
	
	
	private void getAllByAccount(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request)	throws Exception {
		
		String jsonTableGridValues = VendorSearchELSServiceHelper.buildJsonForTableGrid(vendorSearchELS.selectVATRegistrionStatement());
						
		jsonResults.put("jsonVendorSearchGridValues",jsonTableGridValues);

	}
	
}
