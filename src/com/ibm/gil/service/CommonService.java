package com.ibm.gil.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.ibm.ecm.extension.PluginService;
import com.ibm.ecm.extension.PluginServiceCallbacks;
import com.ibm.ecm.json.JSONResponse;
import com.ibm.gil.util.UtilGilConstants;
import com.ibm.igf.contentmanager.rmi.ContentManagerImplementation;
import com.ibm.mm.sdk.common.DKDDO;
import com.ibm.mm.sdk.common.DKEventDefICM;
import com.ibm.mm.sdk.common.DKEventMgmtICM;
import com.ibm.mm.sdk.common.DKEventTypeDefICM;
import com.ibm.mm.sdk.common.DKException;
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
public class CommonService extends PluginService {

	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(CommonService.class);
	
	private HashMap<Integer, String> icnEvents;

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
		return "CommonService";
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
	public void execute(PluginServiceCallbacks callbacks, HttpServletRequest request, 
			HttpServletResponse response) throws Exception {

		JSONResponse jsonResults = new JSONResponse();
		String repositoryId = request.getParameter("repositoryId");
		ServiceHelper.setMDC(request,callbacks);
		
		try{
			Object synch = callbacks.getSynchObject(repositoryId, "cm");
			if(synch != null){
				synchronized(synch){
					selectAction(callbacks, jsonResults, request);
				}
			} else{
				selectAction(callbacks, jsonResults, request);
			}
		}catch(Exception e){
			logger.error(e);
		} finally{
			jsonResults.serialize(response.getOutputStream());
			ServiceHelper.clearMDC();
		}

	}
	
	private void selectAction(PluginServiceCallbacks callbacks, JSONResponse jsonResults,
			HttpServletRequest request){
		String repositoryId = request.getParameter("repositoryId");
		String itemId = request.getParameter("itemId");
		DKDatastoreICM datastore = callbacks.getCMDatastore(repositoryId);
		String frontEndAction = request.getParameter("frontEndAction");
		boolean b = false;
		logger.info("Action:" + frontEndAction);
		if(frontEndAction.equalsIgnoreCase(UtilGilConstants.LOCK_ITEM)){
			b = lockItem(datastore, itemId);
			
		} else if(frontEndAction.equalsIgnoreCase(UtilGilConstants.UNLOCK_ITEM)){
			b = unlockItem(datastore, itemId);
			
		} else if(frontEndAction.equalsIgnoreCase(UtilGilConstants.GET_HISTORY_LOG)){
			//String userTimezone =  request.getParameter("userTimezone");
			getHistoryLog(datastore, itemId, jsonResults/*, userTimezone*/);
			
		} else if(frontEndAction.equalsIgnoreCase(UtilGilConstants.GET_APPLICATION_SERVER_OFFSET)){
			//String userTimezone =  request.getParameter("userTimezone");
			getTimeZone(datastore, request, jsonResults);
			
		} else{
			logger.error("Unknown action - " + frontEndAction);
		}
		jsonResults.put("success", b);
		
	}

/**
	 * Locks an item on Content Manager.
	 * 
	 * @param datastore - the datastore to be used
	 * @param itemId - the id of the item to be locked
	 * @return true if the item was locked successfully, false otherwise.
	 */
	private boolean lockItem(DKDatastoreICM datastore, String itemId){
		try {
			ContentManagerImplementation cmi = new ContentManagerImplementation(datastore);
			DKDDO item = cmi.getObjectFromLibrary(itemId, false, datastore);
			datastore.checkOut(item);
			logger.info("Item "+itemId+" Locked");
			return true;
			
		} catch (DKException e) {
			logger.info(e.getMessage());
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			
		}
		return false;
	}
	

	/**
	 * Unlocks an item on Content Manager.
	 * 
	 * @param datastore - the datastore to be used
	 * @param itemId - the id of the item to be unlocked
	 * @return true if the item was unlocked successfully, false otherwise.
	 */
	private boolean unlockItem(DKDatastoreICM datastore, String itemId){
		try {
			ContentManagerImplementation cmi = new ContentManagerImplementation(datastore);
			DKDDO item = cmi.getObjectFromLibrary(itemId, false, datastore);
			datastore.checkIn(item);
			logger.info("Item "+itemId+" Unlocked");
			return true;
		} catch (DKException e) {
			// TODO Auto-generated catch block
			logger.info(e.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		}
		return false;
	}
	
	/**
	 * Retrieves the history log of the given document.
	 * 
	 * @param datastore - the datastore to be used
	 * @param itemId - the id of the item which history log should be retrieved
	 */
	private void getHistoryLog(DKDatastoreICM datastore, String itemId, JSONResponse jsonResults/*, String userTimezone*/){
		dkCollection itemEvents = null;
		dkIterator it = null;
		DKEventDefICM event = null;
		JsonArray events = new JsonArray();
		JsonArray data = null;
		JsonObject anEvent = null;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		//df.setTimeZone(TimeZone.getTimeZone(userTimezone));
		df.setTimeZone(TimeZone.getTimeZone("GMT"));
		
		try {
			itemEvents = datastore.listEvents(itemId, Short.MAX_VALUE);
		} catch (DKException e1) {
			logger.error(e1.getClass() + ": " + e1.getMessage());
		} catch (Exception e1) {
			logger.error(e1.getClass() + ": " + e1.getMessage());
		}
		
		if(icnEvents == null){
			icnEvents = setEventMap(datastore);
		}
		
		it = itemEvents.createIterator();
		while(it.more()){
			try {
				event = ((DKEventDefICM)(it.next()));
				logger.debug(
						"\nEvent item id   : " + event.getItemId() + "\n" +
						"Event timestamp : " + event.createTimestamp().toGMTString() + "\n" +
						"Event userid    : " + event.userId().trim() + "\n" +
						"Event code      : " + event.getCode() + "\n" +
						"Event name      : " + icnEvents.get(event.getCode()) + "\n" +
						"Event data count: " + event.getEventDataCount() + "\n" +
						"Event data 1    :" + event.getEventData1() + "\n" +
						"Event data 2    :" + event.getEventData2() + "\n" +
						"Event data 3    :" + event.getEventData3() + "\n" +
						"Event data 4    :" + event.getEventData4() + "\n" +
						"Event data 5    :" + event.getEventData5() + "\n\n"
				);
				
				anEvent = new JsonObject();
				anEvent.add("timestamp", new JsonPrimitive(df.format(event.createTimestamp())));
				anEvent.add("userId", new JsonPrimitive(event.userId().trim()));
				
				//Adds event name to json if it's mapped. The event code is added, otherwise.
				if(icnEvents.containsKey(event.getCode())){
					anEvent.add("code", new JsonPrimitive(icnEvents.get(event.getCode())));
				}else {
					anEvent.add("code", new JsonPrimitive(event.getCode()));
				}
				
				anEvent.add("dataCount", new JsonPrimitive(event.getEventDataCount()));
				
				data = new JsonArray();
				data.add(new JsonPrimitive(event.getEventData1()));
				data.add(new JsonPrimitive(event.getEventData2()));
				data.add(new JsonPrimitive(event.getEventData3()));
				data.add(new JsonPrimitive(event.getEventData4()));
				data.add(new JsonPrimitive(event.getEventData5()));
				anEvent.add("data", data);
				
				events.add(anEvent);
				
			} catch (DKUsageError e) {
				logger.error(e.getMessage());
			}
		}
		jsonResults.put("events", events.toString());
	}
	
	/**
	 * @brief Populates a HashMap with event names.
	 * Only relevant events are mapped.
	 * 
	 * @return a {@code java.util.HashMap} containing event codes as keys and event names as values.
	 */
	private HashMap<Integer, String> setEventMap(DKDatastoreICM datastore){
		HashMap<Integer, String> e = new HashMap<Integer, String>();
		DKEventMgmtICM emgmt;
		try {
			emgmt = new DKEventMgmtICM(datastore);
			dkCollection eventTypes = emgmt.listEventTypes();
			dkIterator it = eventTypes.createIterator();
			DKEventTypeDefICM edef = null;
			while(it.more()){
				edef = (DKEventTypeDefICM)(it.next());
				logger.debug(
						"\nName       : " + edef.getName() + "\n" + 
						"Description: " + edef.getDescription() + "\n" +
						"Code       : " + edef.getCode() + "\n" +
						"Lang code  : " + edef.getLanguageCode() + "\n"
				);
				e.put(edef.getCode(), edef.getName());
			}

		} catch (Exception e1) {
			logger.error(e1.getClass()+ ": " + e1.getMessage());
			return null;
		}
		
		return e;
	}
	
	public void getTimeZone(DKDatastoreICM datastore, HttpServletRequest request,  JSONResponse jsonResults){
		String timestamp = request.getParameter("timestamp");
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
		long offsetInMillis = 0;
		try {
			Date date = sdf.parse(timestamp);
			Integer serverMillis = TimeZone.getDefault().getOffset(date.getTime());
			offsetInMillis = serverMillis / 1000 / 60;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			logger.error(e);
		}
		jsonResults.put("offset", offsetInMillis);
		
		
	}
	
}
