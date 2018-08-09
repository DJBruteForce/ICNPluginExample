package com.ibm.gil.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ibm.ecm.extension.PluginService;
import com.ibm.ecm.extension.PluginServiceCallbacks;
import com.ibm.ecm.json.JSONResponse;
import com.ibm.gil.business.Indexing;
import com.ibm.gil.business.WebServicesEndpoint;
import com.ibm.gil.model.DataModel;
import com.ibm.igf.hmvc.WebServicesEnpointManager;
import com.ibm.igf.hmvc.WebServicesEnpointManager.SYSTEMS;

/**
 * This class is supposed to ease {@link com.ibm.ecm.extension.PluginService} implementation
 * by handling synchronization and lock/unlock a document.
 * 
 * @author mdevino
 *
 */
public abstract class GILService extends PluginService {
	
	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(COAELSService.class);

	/**
	 * Executes a {@link com.ibm.extension.PluginService} execution logic handling
	 * {@link com.ibm.mm.sdk.server.DKDatastoreICM} synchronization, if needed.
	 */
	public final void execute(PluginServiceCallbacks callbacks, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		JSONResponse jsonResults = new JSONResponse();
		String repositoryId = request.getParameter("repositoryId");
		Object synch = callbacks.getSynchObject(repositoryId, "cm");
		ServiceHelper.setMDC(request,callbacks);
		Indexing indexing = null;

		try {
			//indexing = initializeVariables(callbacks, jsonResults, request);
			if (synch != null) {
				synchronized (synch) {
					indexing = initializeVariables(callbacks, jsonResults, request);
					selectAction(callbacks, jsonResults, request, indexing);
				}
			} else {
				indexing = initializeVariables(callbacks, jsonResults, request);
				selectAction(callbacks, jsonResults, request, indexing);
			}
			
		} catch (Exception e) {
			handleException(callbacks, jsonResults, request, indexing, e);
			
		} finally {
			ServiceHelper.clearMDC();
			jsonResults.serialize(response.getOutputStream());
			callbacks.getLogger().logExit(this, "execute", request);
			jsonResults = null;
		}

	}

	/**
	 * Executes the index functionality handling the item checkout.
	 * 
	 * @param callbacks - An instance of the {@link com.ibm.extension.PluginServiceCallbacks} class that 
	 * contains several functions that can be used by the service.
	 * @param jsonResults - the instance of the JSON object that will be written on 
	 * {@link javax.servlet.http.HttpServletResponse}'s output stream.
	 * @param request - The {@link javax.servlet.http.HttpServletRequest} object that is generated by the service.
	 * The service can get the output stream and write the response. The response must be in JSON format.
	 * @param indexing - an instance of the document to be saved.
	 * @throws Exception - a generic exception, in case the exception handling must be delegated.
	 */
	protected final void index(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request,
			Indexing indexing) throws Exception {

		DataModel dm = getDataModel(indexing);
		dm.lockItem();
		indexDocument(callbacks, jsonResults, request, indexing);

	}

	/**
	 * Executes the save functionality handling the item checkin.
	 * 
	 * @param callbacks - An instance of the {@link com.ibm.extension.PluginServiceCallbacks} class that 
	 * contains several functions that can be used by the service.
	 * @param jsonResults - the instance of the JSON object that will be written on 
	 * {@link javax.servlet.http.HttpServletResponse}'s output stream.
	 * @param request - The {@link javax.servlet.http.HttpServletRequest} object that is generated by the service.
	 * The service can get the output stream and write the response. The response must be in JSON format.
	 * @param indexing - an instance of the document to be saved.
	 * @throws Exception - a generic exception, in case the exception handling must be delegated.
	 */
	protected final void save(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request,
			Indexing indexing) throws Exception {

		DataModel dm = getDataModel(indexing);
		saveDocument(callbacks, jsonResults, request, indexing);
		dm.unlockItem();
	}

	// Abstract methods

	/**
	 * This method should be implemented to initialize an instance of the document to be indexed.
	 * 
	 * @param callbacks - An instance of the {@link com.ibm.extension.PluginServiceCallbacks} class that 
	 * contains several functions that can be used by the service.
	 * @param jsonResults - the instance of the JSON object that will be written on 
	 * {@link javax.servlet.http.HttpServletResponse}'s output stream.
	 * @param request - The {@link javax.servlet.http.HttpServletRequest} object that is generated by the service.
	 * The service can get the output stream and write the response. The response must be in JSON format.
	 * @return an instance of the document to be indexed.
	 * @throws Exception - a generic exception, in case the exception handling must be delegated.
	 */
	protected abstract Indexing initializeVariables(PluginServiceCallbacks callbacks, JSONResponse jsonResults,
			HttpServletRequest request) throws Exception;

	/**
	 * This method should be implemented to select the action which will be executed in the indexing document.
	 * 
	 * @param callbacks - An instance of the {@link com.ibm.extension.PluginServiceCallbacks} class that 
	 * contains several functions that can be used by the service.
	 * @param jsonResults - the instance of the JSON object that will be written on 
	 * {@link javax.servlet.http.HttpServletResponse}'s output stream.
	 * @param request - The {@link javax.servlet.http.HttpServletRequest} object that is generated by the service.
	 * The service can get the output stream and write the response. The response must be in JSON format.
	 * @param indexing - the document which the action will be executed.
	 * @throws Exception - a generic exception, in case the exception handling must be delegated.
	 */
	protected abstract void selectAction(PluginServiceCallbacks callbacks, JSONResponse jsonResults,
			HttpServletRequest request, Indexing indexing) throws Exception;
	

	/**
	 * This method should be implemented to handle any eventual exception that might occur during the service
	 * execution.
	 * 
	 * @param callbacks - An instance of the {@link com.ibm.extension.PluginServiceCallbacks} class that 
	 * contains several functions that can be used by the service.
	 * @param jsonResults - the instance of the JSON object that will be written on 
	 * {@link javax.servlet.http.HttpServletResponse}'s output stream.
	 * @param request - The {@link javax.servlet.http.HttpServletRequest} object that is generated by the service.
	 * The service can get the output stream and write the response. The response must be in JSON format.
	 * @param indexing - the document which the action was executed.
	 * @param e - the exception caught.
	 */
	protected abstract void handleException(PluginServiceCallbacks callbacks, JSONResponse jsonResults,
			HttpServletRequest request, Indexing indexing, Exception e);

	/**
	 * @brief This method should be implemented to return the data model of the document to be indexed.
	 * Basically what's needed to be done is cast the indexing object to the class needed and return the
	 * get[ItemType]DataModel return. This is needed as the superclass {@link com.ibm.gil.business.Indexing}
	 * doesn't have a method to retrieve the object data model.
	 * 
	 * @param indexing - the document which data model should be retrieved.
	 * @return
	 */
	protected abstract DataModel getDataModel(Indexing indexing);

	/**
	 * This method should implement the index service logic. Handling the lock of the object is not needed.
	 * 
	 * @param callbacks - An instance of the {@link com.ibm.extension.PluginServiceCallbacks} class that 
	 * contains several functions that can be used by the service.
	 * @param jsonResults - the instance of the JSON object that will be written on 
	 * {@link javax.servlet.http.HttpServletResponse}'s output stream.
	 * @param request - The {@link javax.servlet.http.HttpServletRequest} object that is generated by the service.
	 * The service can get the output stream and write the response. The response must be in JSON format.
	 * @param indexing - the document which will be indexed.
	 * @throws Exception - a generic exception, in case the exception handling must be delegated.
	 */
	protected abstract void indexDocument(PluginServiceCallbacks callbacks, JSONResponse jsonResults,
			HttpServletRequest request, Indexing indexing) throws Exception;

	/**
	 * This method should implement the save service logic. Handling the lock of the object is not needed.
	 * 
	 * @param callbacks - An instance of the {@link com.ibm.extension.PluginServiceCallbacks} class that 
	 * contains several functions that can be used by the service.
	 * @param jsonResults - the instance of the JSON object that will be written on 
	 * {@link javax.servlet.http.HttpServletResponse}'s output stream.
	 * @param request - The {@link javax.servlet.http.HttpServletRequest} object that is generated by the service.
	 * The service can get the output stream and write the response. The response must be in JSON format.
	 * @param indexing - the document which will be saved.
	 * @throws Exception - a generic exception, in case the exception handling must be delegated.
	 */
	protected abstract void saveDocument(PluginServiceCallbacks callbacks, JSONResponse jsonResults,
			HttpServletRequest request, Indexing indexing) throws Exception;

	
	/**
	 * Set the end point chosen in the front end if it's not empty, if no end points have been chosen
	 * get the default from configuration panel.
	 * @param session
	 * @param indexing
	 */
	protected void setWSEndPoint(HttpSession session,Indexing indexing ){
		
		List<WebServicesEndpoint> listOfEndPoints = WebServicesEnpointManager.getWsEnpointsBySystem(WebServicesEnpointManager.SYSTEMS.GCMS);
		String gcmsEp  = (String) session.getAttribute("gcmsEndpoint");
		WebServicesEndpoint ws = new WebServicesEndpoint(gcmsEp);
		int index = listOfEndPoints.indexOf(ws);
		WebServicesEndpoint wsEndPoint = new WebServicesEndpoint();
		if (index>=0 && !ServiceHelper.HTML_EMPTY_SPACE.equals(gcmsEp)){
			wsEndPoint = listOfEndPoints.get(index);
		} else {
			wsEndPoint = WebServicesEnpointManager.getDefaultEndpoint(SYSTEMS.GCMS);
		}
		
		indexing.setGcmsEndpoint(wsEndPoint);
		indexing.setRdcEndpoint(WebServicesEnpointManager.getDefaultEndpoint(SYSTEMS.RDC));
		
		logger.info("GCMS end point set:"+wsEndPoint.toString());
		logger.info("RDC end point set:"+indexing.getRdcEndpoint().toString());
	}
	

	
	
}
