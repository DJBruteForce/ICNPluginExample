package com.ibm.gil.service;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.json.JSONArray;

import com.google.gson.Gson;
import com.ibm.ecm.extension.PluginService;
import com.ibm.ecm.extension.PluginServiceCallbacks;
import com.ibm.ecm.json.JSONResponse;
import com.ibm.gil.business.SerialNumberELS;
import com.ibm.gil.util.UtilGilConstants;

/**
 * 
 * @author ferandra
 *
 */
public class SerialNumberELSService  extends PluginService   {

	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(SerialNumberELSService.class);

	public String getId() {
		return "SerialNumberELSService";
	}

	public String getOverriddenService() {	
		return null;
	}

public void execute(PluginServiceCallbacks callbacks, HttpServletRequest request, HttpServletResponse response)	throws Exception {
	 
		JSONResponse jsonResults = new JSONResponse();
		ServiceHelper.setMDC(request,callbacks);
		String userId = request.getParameter("userId");
		String country = request.getParameter("country");
		String frontEndAction = request.getParameter("frontEndAction");
	
		SerialNumberELS serialNumber = new SerialNumberELS();
		serialNumber.setLocale(callbacks.getLocale());
		serialNumber.setUSERID(userId);
		serialNumber.setCOUNTRY(country);
		
		try {
				if (frontEndAction.equalsIgnoreCase(UtilGilConstants.ADD_SERIAL)) {
					
					generateSerial( callbacks, jsonResults, request, serialNumber);			
					
				} else if (frontEndAction.equalsIgnoreCase(UtilGilConstants.IMPORT_SERIAL)) {
					
						  importSerial(callbacks, jsonResults, request, serialNumber);	
						  
				} else if (frontEndAction.equalsIgnoreCase(UtilGilConstants.GET_SERIALS)) {
					
						  getSerials(callbacks, jsonResults, request, serialNumber);	
			}

		}	catch(Exception e)	{
				logger.fatal(e.getMessage(),e);
				serialNumber.getDialogErrorMsgs().add(e.getMessage());
				jsonResults.put("serialNumberJson",ServiceHelper.objectToJson(serialNumber));
				
		} finally {
			jsonResults.serialize(response.getOutputStream());	
			ServiceHelper.clearMDC();
		}
	}

	private void generateSerial(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request,SerialNumberELS serialNumber )	throws Exception {	

		Gson gson = new Gson();
		List<SerialNumberELS> listOfSerials = null;
		serialNumber.setSerialStart(gson.fromJson(request.getParameter("serialStart"), String[].class));
		serialNumber.setSerialEnd(request.getParameter("serialEnd"));
		serialNumber.setSerialChange(gson.fromJson(request.getParameter("serialChange"), boolean[].class));
		serialNumber.setQuantity(Integer.valueOf(request.getParameter("quantity")));
		serialNumber.setIncludeVowels(Boolean.parseBoolean(request.getParameter("includeVowels")));
		listOfSerials = serialNumber.addSerial(request.getParameter("type"),request.getParameter("model"),request.getParameter("unitprice"));
		ServiceHelper.putJsonResponse(jsonResults, "snJsonList", listOfSerials);
		ServiceHelper.putJsonResponse(jsonResults, "snJson", serialNumber);
		
	}
	
	private void importSerial(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request, SerialNumberELS serialNumber )	throws Exception {
	
		 BufferedReader brCsv = ServiceHelper.getUploadedFile(callbacks.getRequestActionForm());
		 ArrayList<SerialNumberELS> listOfSerialNumbers = serialNumber.importSerials(brCsv);
		 ServiceHelper.putJsonResponse(jsonResults, "snImportJsonList", listOfSerialNumbers);
	}
	
	private void getSerials(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request, SerialNumberELS serialNumber )	throws Exception {
		
		JSONArray jsonSerialNumbersArray;
		ArrayList<SerialNumberELS> listOfSerialNumbers = new ArrayList<SerialNumberELS>();
		jsonSerialNumbersArray = new JSONArray(request.getParameter("jsonSerialNumbers"));
		SerialNumberELS serialNumberAux =null;
		for(int i =0; i<jsonSerialNumbersArray.length();i++) {			
			org.json.JSONObject jsonResult =(org.json.JSONObject)jsonSerialNumbersArray.get(i);
			serialNumberAux=(SerialNumberELS)ServiceHelper.JSONToObject(jsonResult,SerialNumberELS.class);
			listOfSerialNumbers.add(serialNumberAux);
		}
		ServiceHelper.putJsonResponse(jsonResults, "snObjsJsonList", listOfSerialNumbers);
		ServiceHelper.putJsonResponse(jsonResults, "snJson", serialNumber);

	}
}
