package com.ibm.gil.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.ecm.extension.PluginService;
import com.ibm.ecm.extension.PluginServiceCallbacks;
import com.ibm.ecm.json.JSONMessage;
import com.ibm.ecm.json.JSONResponse;

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
public class OpenIndexerPageService  extends PluginService {

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
		return "OpenIndexerPageService";
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
	public void execute(PluginServiceCallbacks callbacks,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		JSONResponse jsonResults = new JSONResponse();
		String docId = null;
		String repository  = null;
		String desktop = null;
		String parmTeste = null;
	
		
		
		try {
			
			callbacks.getLogger().logEntry(this, "execute", request);
			//docId = request.getParameterNames()Parameter("documentoAttributes");
			//repository = request.getParameterMap()("documentoAttributes");
			//desktop = request.getParameter("backendServer");
			//ServletContext
			/*InitialContext initialContext = new InitialContext(); 
			try { 
				DataSource dataSource = (DataSource) initialContext.lookup(jndiResource); 
				Connection conn = dataSource.getConnection(); 
				if (conn != null) { 
					DatabaseMetaData dmd = conn.getMetaData(); 
					if (dmd != null) { 
						ResultSet rs = dmd.getSchemas(); 
					}
				}
			} catch (NameNotFoundException e) { 
							e.printStackTrace();
						}*/
				
			
			
			JSONMessage jsonMessage = new JSONMessage(0, "Request data: ", "", "DOCID: "+ docId,"DOCID: "+ docId, "");
			jsonResults.addInfoMessage(jsonMessage);
			jsonResults.put("anyProperty", "anyPropertyValue");

	
		}catch(Exception e){
			
			callbacks.getLogger().logError(this, "execute", request, e);
			JSONMessage jsonErroMessage = new JSONMessage(0, "Retrieving Document information failed. ", e.getMessage(), "","Check the IBM Content Navigator logs for more details.", "");
			
			jsonResults.addErrorMessage(jsonErroMessage);
			
		
		}
		
		jsonResults.serialize(response.getOutputStream());
		callbacks.getLogger().logExit(this, "execute", request);
		

	}
}
