package com.ibm.gil.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.ibm.ecm.extension.PluginService;
import com.ibm.ecm.extension.PluginServiceCallbacks;
import com.ibm.ecm.json.JSONResponse;
import com.ibm.gil.util.UtilGilConstants;
import com.ibm.igf.contentmanager.rmi.ContentManagerImplementation;
import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;
import com.ibm.mm.sdk.common.DKConstant;
import com.ibm.mm.sdk.common.DKConstantICM;
import com.ibm.mm.sdk.common.DKDDO;
import com.ibm.mm.sdk.common.DKException;
import com.ibm.mm.sdk.common.DKFolder;
import com.ibm.mm.sdk.common.DKMessageIdICM;
import com.ibm.mm.sdk.common.DKPidICM;
import com.ibm.mm.sdk.common.DKRetrieveOptionsICM;
import com.ibm.mm.sdk.common.DKUsageError;
import com.ibm.mm.sdk.common.dkCollection;
import com.ibm.mm.sdk.common.dkIterator;
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
public class FolderService extends PluginService {

	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(FolderService.class);

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
		return "FolderService";
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
			String repositoryId = request.getParameter("repositoryId");
			Object synch = callbacks.getSynchObject(repositoryId, "cm");
			
		try{
			ServiceHelper.setMDC(request, callbacks);
			if(synch != null){
				synchronized(synch){
					selectAction(callbacks, jsonResults, request);
				}
			} else {
				selectAction(callbacks, jsonResults, request);
			}
		} catch(Exception e){
			e.printStackTrace();
		} finally {
			jsonResults.serialize(response.getOutputStream());
			ServiceHelper.clearMDC();
		}

	}
	
	private void selectAction(PluginServiceCallbacks callbacks, JSONResponse jsonResults,
			HttpServletRequest request){
		String frontEndAction = request.getParameter("frontEndAction");
		logger.info("Executing CommonService - frontEndAction = " + frontEndAction);
		try{
			if(frontEndAction.equalsIgnoreCase(UtilGilConstants.ADD_TO_FOLDER)){
				addItemToFolder(callbacks, jsonResults, request);
				
			} else if(frontEndAction.equalsIgnoreCase(UtilGilConstants.RETRIEVE_CONTAINING_FOLDERS)){
				retrieveContentFolders(callbacks, jsonResults, request);
				
			} else if(frontEndAction.equalsIgnoreCase(UtilGilConstants.REMOVE_FROM_FOLDER)){
				removeFromFolder(callbacks, jsonResults, request);
			} else{
				logger.error("Unknown action: " + frontEndAction);
				jsonResults.put("error", "Unknown action: " + frontEndAction);
			}
		} catch(Exception e){
			logger.error(e);
		}
	}

	private void addItemToFolder(PluginServiceCallbacks callbacks, JSONResponse jsonResults,
			HttpServletRequest request) {
		String repositoryId = request.getParameter("repositoryId");
		DKDatastoreICM datastore = callbacks.getCMDatastore(repositoryId);
		JsonArray items = new JsonParser().parse(request.getParameter("itemIds")).getAsJsonArray();
		JsonArray folderIds = new JsonParser().parse(request.getParameter("folderIds")).getAsJsonArray();
		ContentManagerImplementation cmi = new ContentManagerImplementation(datastore);
		String objectId = null, folderId;
		for(int i = 0; i < folderIds.size(); i++){
			folderId = folderIds.get(i).getAsString();
			try{
				DKDDO folder = cmi.getObjectFromLibrary(folderId, false, datastore);
				short prop_id = folder.propertyId(DKConstant.DK_CM_PROPERTY_ITEM_TYPE);
				if (prop_id > 0) {
				    short type = ((Short) folder.getProperty(prop_id)).shortValue();
				    if(type == DKConstant.DK_CM_FOLDER){
				         // --- process a folder
				   		for(int j = 0; j < items.size(); j++){
				   			objectId = items.get(j).getAsString();
				   			if(cmi.moveDocumentToFolder(objectId, folderId, datastore)){
				   				logger.debug("Item " + objectId + " added to folder " + folderId + " successfully.");
				   			} else {
				   				logger.error("Failed to add item " + objectId + " to folder " + folderId);
				   			}
				   		}
				    } else {
				    	logger.info("Item " + folderId + " is not a folder.");
						jsonResults.put("error", "The selected item is not a folder.");
				    }
				} 
			}catch(DKUsageError e){
				logger.error(e);
				jsonResults.put("error", "Something went wrong.");
			}
		}
	}
	
	
	private void retrieveContentFolders(PluginServiceCallbacks callbacks, JSONResponse jsonResults,
			HttpServletRequest request) throws IOException {

		String repositoryId = request.getParameter("repositoryId");
		DKDatastoreICM datastore = callbacks.getCMDatastore(repositoryId);
		ContentManagerImplementation cmi = new ContentManagerImplementation(datastore);
		
		JSONArray itemIds = JSONArray.parse(request.getParameter("itemIds"));
		DKDDO item;
		JSONArray arrayArray = new JSONArray(), foldersArray, intersectArray;
		JSONObject jsonFolder = null;
		DKRetrieveOptionsICM dkRetrieveOpts;
		String itemId;
		dkCollection folders;
		int numberOfArrays, arraysThatMatched;
		
		for(int i = 0; i < itemIds.size(); i++){
			try {
				itemId = (String) (itemIds.get(i));
				item = cmi.getObjectFromLibrary(itemId, false, datastore);
				dkRetrieveOpts = DKRetrieveOptionsICM.createInstance(datastore);
				dkRetrieveOpts.linksInbound(true);
				dkRetrieveOpts.linksInboundFolderSources(true);
				dkRetrieveOpts.linksTypeFilter(DKConstantICM.DK_ICM_LINKTYPENAME_DKFOLDER);

				// dkRetrieveOpts.baseAttributes(true);
				item.retrieve(dkRetrieveOpts.dkNVPair());

				folders = null;
				short propId = item.propertyId(DKConstant.DK_CM_PROPERTY_FOLDER_SOURCES);
				if (propId > 0)
					folders = (dkCollection) item.getProperty(propId);

				if (folders == null) {
					logger.info("No source folders found for item " + itemId);
					jsonResults.put("folders", new JSONArray());
					return;
				} else {
					logger.info("Found '" + folders.cardinality() + "' source folders that contain item " + itemId);
					foldersArray = new JSONArray();
					dkIterator iter = folders.createIterator();
					while (iter.more()) {
						DKDDO folder = (DKDDO) iter.next();
						folder.retrieve();
						jsonFolder = new JSONObject();

						jsonFolder.put("itemId", ((DKPidICM) (folder.getPidObject())).getItemId());
						jsonFolder.put("name", datastore.getName(folder));
						jsonFolder.put("class", cmi.indexNameToDescription(folder.getObjectType(), datastore));

						foldersArray.add(jsonFolder);
					}
					arrayArray.add(foldersArray);
					
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		} // end for
		
		numberOfArrays = arrayArray.size();
		intersectArray = new JSONArray();
		foldersArray = (JSONArray) (arrayArray.get(0));
		for(int i = 0; i < foldersArray.size(); i++){
			arraysThatMatched = 0;
			jsonFolder = (JSONObject) (foldersArray.get(i));
			for(int j = 0; j < numberOfArrays; j++){
				if(((JSONArray) (arrayArray.get(j))).contains(jsonFolder)){
					arraysThatMatched++;
				}
				if(arraysThatMatched == numberOfArrays){
					intersectArray.add(jsonFolder);
				}
			}
		}
		
		jsonResults.put("folders", intersectArray);
	}
	


	private void removeFromFolder(PluginServiceCallbacks callbacks, JSONResponse jsonResults,
			HttpServletRequest request) throws IOException {

		String repositoryId = request.getParameter("repositoryId");
		DKDatastoreICM datastore = callbacks.getCMDatastore(repositoryId);
		ContentManagerImplementation cmi = new ContentManagerImplementation(datastore);
		JSONArray itemIds = JSONArray.parse(request.getParameter("itemIds"));
		JSONArray folderIds = JSONArray.parse(request.getParameter("folderIds"));
		List<DKDDO> items = retrieveObjectsDDOs(itemIds, cmi, datastore);
		DKDDO folder;
		DKFolder dkFolder;
		dkIterator iter;

		for (int i = 0; i < folderIds.size(); i++) {
			folder = cmi.getObjectFromLibrary((String) folderIds.get(i), false, datastore);
			DKRetrieveOptionsICM dkRetrieveOpts;
			try {
				dkRetrieveOpts = DKRetrieveOptionsICM.createInstance(datastore);
				dkRetrieveOpts.baseAttributes(true);
				dkRetrieveOpts.linksOutbound(true);
				folder.retrieve(dkRetrieveOpts.dkNVPair());
				try{
				datastore.checkOut(folder);
				} catch (DKException  e){
					if (e.getErrorId() == DKMessageIdICM.DK_CM_MSG_CHECK_OUT_FAILED) {
						logger.info("Item checked out already.");

					} else if (e.getErrorId() ==  DKMessageIdICM.DK_ICM_MSG_RC_ITEM_CHECKEDOUT_BYOTHER) {
				      logger.info("Item checked out by another user");
				      throw e;
				    } 
				}
				dkFolder = (DKFolder) folder
						.getData(folder.dataId(DKConstant.DK_CM_NAMESPACE_ATTR, DKConstant.DK_CM_DKFOLDER));
				iter = dkFolder.createIterator();
				while (iter.more()) {
					DKDDO ddo = (DKDDO) iter.next();
					if (isOnDeletionList(ddo, items)) {
						// The item to be removed is found.
						// Remove it (current element in the iterator)
						dkFolder.removeElementAt(iter);
						logger.info("Object " + ddo.getPidObject().getIdString() + " removed from folder " +
								folder.getPidObject().getIdString());
						iter.reset();
					}

				}
				folder.update();
				datastore.checkIn(folder);
				jsonResults.put("success", true);

			} catch (DKUsageError e) {
				logger.error(e);
			} catch (Exception e) {
				logger.error(e);
			}

		}
	}

	private List<DKDDO> retrieveObjectsDDOs(JSONArray itemIds, ContentManagerImplementation cmi,
			DKDatastoreICM datastore) {
		List<DKDDO> items = new ArrayList<DKDDO>();
		for(int i = 0; i < itemIds.size(); i++){
			items.add(cmi.getObjectFromLibrary((String) (itemIds.get(i)), false, datastore));
		}
		
		return items;
	}
	
	private boolean isOnDeletionList(DKDDO currentItem, List<DKDDO> items){
		for(DKDDO item : items){
			if(currentItem.getPidObject().pidString().equals(item.getPidObject().pidString())){
				return true;
			}
		}
		return false;
	}
}
