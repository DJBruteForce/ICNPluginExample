package com.ibm.gil.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.ThreadContext;
import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;
import org.json.JSONObject;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ibm.ecm.extension.PluginServiceCallbacks;
import com.ibm.ecm.json.JSONResponse;
import com.ibm.gil.business.FormSelect;
import com.ibm.gil.business.PoexCodeFormSelect;
import com.ibm.gil.business.TaxCodeFormSelect;
import com.ibm.gil.util.UtilGilConstants;


public class ServiceHelper {
	

	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(ServiceHelper.class);
	
	public static final String HTML_EMPTY_SPACE = "&nbsp;";
	
	private static Random random = new Random();
	
	private static final int RandomMIN=10000;
	
	private static final int RandomMAX=99999;
	
	
	public static int getRandom() {
		
		if(random==null){
			random = new Random();
		}
		
	return random.nextInt((RandomMAX - RandomMIN) + 1) + RandomMIN;
		
	}
	
    public static String buildJsonForSelect (ArrayList<FormSelect> colOfData) {
    	
		 JsonArray datasetsArr = new JsonArray();
		 JsonObject dataset = new JsonObject();
		 FormSelect code = null;
		
		Iterator<FormSelect> itOptions = colOfData.iterator();
			
		while(itOptions.hasNext()) {
			
			Object obj = itOptions.next();
			
			if (obj instanceof TaxCodeFormSelect)	{
				
						code = (TaxCodeFormSelect)obj;
					
				} else if (obj instanceof PoexCodeFormSelect)	{
				
							code = (PoexCodeFormSelect)obj;
							
				} 	else if (obj instanceof FormSelect)	{
					
					code = (FormSelect)obj;
					
				} 
			
			
			dataset = new JsonObject();
			dataset.addProperty("label", code.getLabel());
			dataset.addProperty("value", code.getValue() );
			if (code.isSelected()) {
				dataset.addProperty("selected","selected");  
			}
		    
		    datasetsArr.add(dataset);
			  
		}
	
		logger.debug(datasetsArr.toString());
		
		return datasetsArr.toString();
	
    }
    
    
    

	@SuppressWarnings("unchecked")
	public static Object jsonToObject (JsonObject jsonObj, Class gilClass) {
    	
		logger.debug("Request JSON: "+ jsonObj.toString());
		Gson gson = new Gson();
		return gson.fromJson(jsonObj, gilClass);

    }
	

	@SuppressWarnings("unchecked")
	public static Object JSONToObject(JSONObject jsonObj, Class gilClass) {
    	
		Gson gson = new Gson();
		return gson.fromJson(jsonObj.toString(), gilClass);

    }
	public static Object jsonToObject (JsonObject jsonObj, Type typeOfT) {
    	
		Gson gson = new Gson();
		return gson.fromJson(jsonObj, typeOfT);

    }
	
	public static Object jsonToObject (String jsonObj, Type typeOfT) {
    	
		Gson gson = new Gson();
		return gson.fromJson(jsonObj, typeOfT);

    }
    
    public static String objectToJson (Object obj)  {

		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.setExclusionStrategies(new ExclusionStrategy() {  
		    @Override
		    public boolean shouldSkipField(FieldAttributes f) {
		        return f.getName().contains("_");
		    }

		    @Override
		    public boolean shouldSkipClass(Class<?> incomingClass) {
		        return incomingClass ==  java.text.DecimalFormat.class ||  
		        		incomingClass == com.ibm.gil.model.InvoiceDataModel.class  || 
		        		incomingClass == com.ibm.gil.model.InvoiceDataModelELS.class ||
				        incomingClass == com.ibm.gil.model.LineItemDataModelELS.class |
		        		incomingClass == com.ibm.gil.model.ContractDataModel.class  ||
		        		incomingClass == com.ibm.gil.model.COADataModelELS.class  ||
		        		incomingClass == com.ibm.gil.model.USStateDataModel.class  ||
		        		incomingClass == com.ibm.igf.gil.CountryProperties.class ||
		        		incomingClass == com.ibm.gil.model.OfferingLetterDataModel.class ||
		        		incomingClass == com.ibm.gil.model.OfferingLetterDataModelELS.class ||
		        		incomingClass == com.ibm.igf.contentmanager.rmi.ContentManagerImplementation.class || 
		        		incomingClass == com.ibm.igf.contentmanager.rmi.ContentManagerInterface.class || 
		        		incomingClass == com.ibm.gil.model.MiscInvoiceDataModel.class||
		        		incomingClass == com.ibm.gil.model.OEMProductDataModel.class||
		        		incomingClass == java.util.TimeZone.class || 
		        		incomingClass == com.ibm.gil.business.WebServicesEndpoint.class;
		    }
		});
		
		Gson gson = gsonBuilder.create();
		String jsonString = gson.toJson(obj); 

	
		return jsonString.toString();
	
    }
    
    public static void putJsonResponse(JSONResponse jsonResults, String parmName, Object ind) {
    	
    	String jsonString = ServiceHelper.objectToJson(ind);
		jsonResults.put(parmName,jsonString);
		
		if(logger.isDebugEnabled()){
			logger.debug(UtilGilConstants.LOG_JSON_RESPONSE+":"+jsonString);
		} else {
			print(ind);
		}
		
		
    	
    }
    
    public static void setMDC (HttpServletRequest request,PluginServiceCallbacks callbacks) {
    	
    	String userId = "";
    	try{
    		userId	= callbacks.getUserId();
    	}catch(NullPointerException e) {
    		userId = request.getParameter("userId");
    		userId = userId==null?"":userId;
    	}
    	
    	ThreadContext.put("userName", userId.toUpperCase());
    	ThreadContext.put("sessionId", request.getSession().getId());
    	ThreadContext.put("actionId", String.valueOf(getRandom()));
    } 
    
    public static void clearMDC() {
    	ThreadContext.clearAll();
    }
    
    /**
     * Prints JSON in a formatted and pretty way.
     * @param jsonUglyStr
     * @return
     */
    public static String prettyPrint (String jsonUglyStr) {
 		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(jsonUglyStr);
		String prettyJsonString = gson.toJson(je);
		
		return prettyJsonString;
	
    }
    
    public static void print (String msg, Object obj) {
    	
    	GsonBuilder gbuilder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
		Gson gson = gbuilder.create();
		String jsonStringInfo = gson.toJson(obj);
		logger.info(msg+":"+jsonStringInfo);
    }
    
    private static void print (Object obj) {
    	
    	GsonBuilder gbuilder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
		Gson gson = gbuilder.create();
		String jsonStringInfo = gson.toJson(obj);
		logger.info(UtilGilConstants.LOG_JSON_RESPONSE+":"+jsonStringInfo);
    }
    
    

	public static BufferedReader getUploadedFile(ActionForm form) throws Exception {
		
		FormFile uploadFile = null;
		InputStream is = null;

		if (form != null && form.getMultipartRequestHandler() != null) {
			Map fileElements = form.getMultipartRequestHandler().getFileElements();
			Iterator it = fileElements.values().iterator();
			while (it.hasNext()) {
				uploadFile = (FormFile) it.next();
				if (uploadFile != null)
					break;
			}
		}
		
		if (uploadFile != null) {
			is = uploadFile.getInputStream();
		}
		
		InputStreamReader isReader = new InputStreamReader(is, "UTF-8");
		BufferedReader br = new BufferedReader(isReader);
		
		return br;
	}
	

}


