package com.ibm.gil.service;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;


import org.json.JSONArray;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ibm.ecm.extension.PluginServiceCallbacks;
import com.ibm.ecm.json.JSONResponse;
import com.ibm.gil.business.FormSelect;
import com.ibm.gil.business.Indexing;
import com.ibm.gil.business.InvoiceELS;
import com.ibm.gil.business.InvoiceLineItemsELS;
import com.ibm.gil.business.LineItemELS;
import com.ibm.gil.business.OtherChangesFormSelect;
import com.ibm.gil.business.SerialNumberELS;
import com.ibm.gil.business.VatCodesNonUSFormSelect;
import com.ibm.gil.model.DataModel;
import com.ibm.gil.service.helper.LineItemELSServiceHelper;
import com.ibm.gil.util.CNUtilConstants;
import com.ibm.gil.util.RegionalBigDecimal;
import com.ibm.gil.util.UtilGilConstants;
import com.ibm.igf.hmvc.ResultSetCache;
import com.ibm.igf.webservice.security.GILConstants;
import com.ibm.mm.sdk.server.DKDatastoreICM;



/**
 * @author ajimena
 *
 */
public class LineItemELSService  extends GILService {
	
	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(LineItemELSService.class);


	
	@Override
	protected DataModel getDataModel(Indexing indexing) {
		return ((LineItemELS) (indexing)).getLineItemDataModelELS();
	}
	
	@Override
	protected Indexing initializeVariables(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request) {
		
		//depends of the option to execute the Indexing object that will be created then initializing at selectAction method
		
		return null;
	}

		
	
	@Override
	protected void selectAction(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request,
			Indexing indexing) throws Exception {
		logger.debug("selectAction() ");
		String optionToExecute = request.getParameter("request");

		logger.debug("option:"+optionToExecute);
		if(optionToExecute.equals(UtilGilConstants.INIT_LINE_ITEMS)){
			//in the case is new if not assign the existing value that will come in serviceParameters
				initLineItems(request,jsonResults);
//				typeOfresponse=CNUtilConstants.LINE_ITEM_DIALOG_R;
				
			}else if(optionToExecute.equals(UtilGilConstants.VALIDATE_INPUT_LN_ITEMS)	){
				
				validateInput(callbacks,request, jsonResults);
//				typeOfresponse=CNUtilConstants.A_LINE_ITEM_R;
				
			}else if(optionToExecute.equals(UtilGilConstants.ADD_TYPE_MODEL)){
				addTypeAndModel(callbacks,request, jsonResults);
//				typeOfresponse=CNUtilConstants.A_LINE_ITEM_R;
				
			}else if(optionToExecute.equals(UtilGilConstants.RENUMBER_LINE_ITEMS)){
				renumberLineItems(callbacks,request, jsonResults);
//				typeOfresponse=CNUtilConstants.LIST_LINE_ITEMS_R;
			}
			else if(optionToExecute.equals(UtilGilConstants.SPREAD_TAX_ROUNDING_ERROR)){
				spreadTaxRoundingError(request, jsonResults);
//				typeOfresponse=CNUtilConstants.LIST_LINE_ITEMS_R;
			}else if(optionToExecute.equals(UtilGilConstants.RENUMBER_AND_SPREAD)){
				renumberAndSpread(callbacks,request, jsonResults);
//				typeOfresponse=CNUtilConstants.LIST_LINE_ITEMS_R;
			}else if(optionToExecute.equals(UtilGilConstants.IMPORT_SERIAL)){
				
				
				importSerial(callbacks, request,jsonResults);
//				typeOfresponse=CNUtilConstants.LIST_LINE_ITEMS_R;
				
			}else if(optionToExecute.equals(UtilGilConstants.IMPORT_PARTS)){
				

				importParts(callbacks, request, jsonResults);
//				typeOfresponse=CNUtilConstants.LIST_LINE_ITEMS_R;
				
			}else if(optionToExecute.equals(UtilGilConstants.VALIDATE_SAVE_LINE_ITEMS)){
				

				validateSaveLineItems(callbacks, request, jsonResults);
//				typeOfresponse=CNUtilConstants.LIST_LINE_ITEMS_R;
				
			}else if(optionToExecute.equals(UtilGilConstants.SELECT_TM_DESC)){
				getTMDesc(callbacks,request, jsonResults);
			}

		

	}
	
	@Override
	protected void handleException(PluginServiceCallbacks callbacks, JSONResponse jsonResults,
			HttpServletRequest request, Indexing indexing, Exception e) {
		
		logger.debug("Handle exception-LineItemELSService");
		logger.fatal(e.getMessage());


	}
	

	public String getId() {
		return "LineItemELSService";
	}


	public String getOverriddenService() {
		return null;
	}

//	public void execute(PluginServiceCallbacks callbacks,HttpServletRequest request, HttpServletResponse response)		throws Exception {
//		
//		this.request=request;
//		this.callbacks=callbacks;
//		jsonResponse=new JSONResponse();
//		country	=request.getParameter("country");
//		String userId = request.getParameter("userId");
//		repositoryId =request.getParameter("repositoryId");
//		optionToExecute=request.getParameter("request");
//		datastore = callbacks.getCMDatastore(repositoryId);
//		int typeOfresponse=1000;//impossible type...
//		
//		if(optionToExecute.equals(UtilGilConstants.INIT_LINE_ITEMS)){
//		//in the case is new if not assign the existing value that will come in serviceParameters
//			initLineItems(request);
//			typeOfresponse=CNUtilConstants.LINE_ITEM_DIALOG_R;
//			
//		}else if(optionToExecute.equals(UtilGilConstants.VALIDATE_INPUT_LN_ITEMS)	){
//			validateInput();
//			typeOfresponse=CNUtilConstants.A_LINE_ITEM_R;
//			
//		}else if(optionToExecute.equals(UtilGilConstants.ADD_TYPE_MODEL)){
//			addTypeAndModel();
//			typeOfresponse=CNUtilConstants.A_LINE_ITEM_R;
//		}else if(optionToExecute.equals(UtilGilConstants.RENUMBER_LINE_ITEMS)){
//			renumberLineItems();
//			typeOfresponse=CNUtilConstants.LIST_LINE_ITEMS_R;
//		}
//		else if(optionToExecute.equals(UtilGilConstants.SPREAD_TAX_ROUNDING_ERROR)){
//			spreadTaxRoundingError();
//			typeOfresponse=CNUtilConstants.LIST_LINE_ITEMS_R;
//		}else if(optionToExecute.equals(UtilGilConstants.RENUMBER_AND_SPREAD)){
//			renumberAndSpread();
//			typeOfresponse=CNUtilConstants.LIST_LINE_ITEMS_R;
//		}else if(optionToExecute.equals(UtilGilConstants.IMPORT_SERIAL)){
//			
//			SerialNumberELS serialNumber = new SerialNumberELS();
//			serialNumber.setLocale(callbacks.getLocale());
//			serialNumber.setUSERID(userId);
//			serialNumber.setCOUNTRY(country);
//			billedToIgfIndc		=request.getParameter("billedToIgfIndc");
//			usTaxPercent	=request.getParameter("usTaxPercent");
//			usTaxIndc		=request.getParameter("usTaxIndc");
//			importSerial(callbacks, request, serialNumber);
//			typeOfresponse=CNUtilConstants.LIST_LINE_ITEMS_R;
//			
//		}else if(optionToExecute.equals(UtilGilConstants.IMPORT_PARTS)){
//			
//			SerialNumberELS serialNumber = new SerialNumberELS();
//			serialNumber.setLocale(callbacks.getLocale());
//			serialNumber.setUSERID(userId);
//			serialNumber.setCOUNTRY(country);
//			billedToIgfIndc		=request.getParameter("billedToIgfIndc");
//			usTaxPercent	=request.getParameter("usTaxPercent");
//			usTaxIndc		=request.getParameter("usTaxIndc");
//			importParts(callbacks, request, serialNumber);
//			typeOfresponse=CNUtilConstants.LIST_LINE_ITEMS_R;
//			
//		}
//		buildResponse(typeOfresponse);
//		jsonResponse.serialize(response.getOutputStream());
//		callbacks.getLogger().logExit(this, "execute", request);
//
//	}
	private void renumberAndSpread(PluginServiceCallbacks callbacks, HttpServletRequest request,JSONResponse jsonResponse){
		List<LineItemELS> lineItems=new ArrayList<LineItemELS>();
		InvoiceELS invoice=initInvoice(request);
		InvoiceLineItemsELS invoiceLineItemDialog= new InvoiceLineItemsELS(invoice);
		initListItems(lineItems,request);
		LineItemELSServiceHelper.renumberLineItems(lineItems);
		LineItemELSServiceHelper.spreadTaxRoundingError(invoiceLineItemDialog,lineItems);
		
		//build response
		String lineItemElsObject = ServiceHelper.objectToJson(lineItems);
		logger.debug("building LineItemELSService response...");			
		jsonResponse.put(CNUtilConstants.LINE_ITEMS_JSON,lineItemElsObject);
	}
	
	private void spreadTaxRoundingError( HttpServletRequest request,JSONResponse jsonResponse){

		List<LineItemELS> lineItems=new ArrayList<LineItemELS>();
		initListItems(lineItems,request);
		InvoiceELS invoice=initInvoice(request);
		InvoiceLineItemsELS invoiceLineItemDialog= new InvoiceLineItemsELS(invoice);
		LineItemELSServiceHelper.spreadTaxRoundingError(invoiceLineItemDialog,lineItems);
		
		//build response
		String lineItemElsObject = ServiceHelper.objectToJson(lineItems);
		logger.debug("building LineItemELSService response...");			
		jsonResponse.put(CNUtilConstants.LINE_ITEMS_JSON,lineItemElsObject);
	}
	
	private void renumberLineItems(PluginServiceCallbacks callbacks, HttpServletRequest request,JSONResponse jsonResponse){
		List<LineItemELS> lineItems=new ArrayList<LineItemELS>();
		initListItems(lineItems, request);
		LineItemELSServiceHelper.renumberLineItems(lineItems);
		
		//build response
		String lineItemElsObject = ServiceHelper.objectToJson(lineItems);
		logger.debug("building LineItemELSService response...");			
		jsonResponse.put(CNUtilConstants.LINE_ITEMS_JSON,lineItemElsObject);

	}
	
	private void addTypeAndModel(PluginServiceCallbacks callbacks, HttpServletRequest request,JSONResponse jsonResponse){
		String repositoryId =request.getParameter("repositoryId");
		DKDatastoreICM datastore = callbacks.getCMDatastore(repositoryId);
		LineItemELS currentLnItem=initLineItem(request,datastore);
		List<LineItemELS> lineItems= new ArrayList<LineItemELS>();
		initListItems(lineItems,request);
		doAddTypeModel(currentLnItem, lineItems);
		
		//build response
		String lineItemElsObject = ServiceHelper.objectToJson(currentLnItem);
		logger.debug("building LineItemELSService response...");
		jsonResponse.put(CNUtilConstants.LN_ITEM_JSON,lineItemElsObject);
	}
	
	private void buildLnItemResponse(JSONResponse jsonResponse, LineItemELS currentLnItem){
		//build response
				String lineItemElsObject = ServiceHelper.objectToJson(currentLnItem);
				logger.debug("building LineItemELSService response...");
				jsonResponse.put(CNUtilConstants.LN_ITEM_JSON,lineItemElsObject);

	}
	
	private void doAddTypeModel(LineItemELS currentLnItem,List<LineItemELS> lineItems){
		
		LineItemELS aDataModel=null;
    	ArrayList<String> modelList = new ArrayList<String>();
    	if(currentLnItem.getTYPE().trim().equals("") && currentLnItem.getMODEL().trim().equals("")){
    		if(lineItems.size()<1){
    			currentLnItem.setTYPE("XXXX");
    			currentLnItem.setMODEL("001");
    			currentLnItem.setBLANKTYPEMODEL("Y");
    		}else{
    			for (int i=0; i<lineItems.size(); i++){   
    				aDataModel = lineItems.get(i);
    				if(aDataModel.getTYPE().equals("XXXX")){
    					modelList.add(aDataModel.getMODEL());
    				}
    			}
    			if(modelList.size()>0){
    				Collections.sort(modelList);
    				int value = (Integer.valueOf((String)modelList.get(modelList.size()-1))).intValue();
    				String model = "00"+(++value); 
    				if(model.length()>3){
    					model = model.substring(model.length()-3);
    				}
    				currentLnItem.setTYPE("XXXX");
    				currentLnItem.setMODEL(model);
    				currentLnItem.setBLANKTYPEMODEL("Y");
    			}else{
        			currentLnItem.setTYPE("XXXX");
        			currentLnItem.setMODEL("001");
        			currentLnItem.setBLANKTYPEMODEL("Y");
    			}
    		}
    	}else{
    		currentLnItem.setBLANKTYPEMODEL("N");
     	}

	}
	
	private void validateInput(PluginServiceCallbacks callbacks, HttpServletRequest request , JSONResponse jsonResponse){
		String repositoryId =request.getParameter("repositoryId");
		DKDatastoreICM datastore = callbacks.getCMDatastore(repositoryId);
		LineItemELS currentLnItem=initLineItem(request, datastore);
		validateLnItemInput(currentLnItem);
		//build reponse
		String lineItemElsObject = ServiceHelper.objectToJson(currentLnItem);
		logger.debug("building LineItemELSService response...");
		jsonResponse.put(CNUtilConstants.LN_ITEM_JSON,lineItemElsObject);
	}
	
	private void validateLnItemInput(LineItemELS currentLnItem){
		
		logger.debug("entry validateLnItemInput");
		
		 // validate the unit price > 0
	       RegionalBigDecimal unitprice = RegionalBigDecimal.ZERO;
	       try
	       {
	           unitprice = currentLnItem.getDecimal(currentLnItem.getUNITPRICEasBigDecimal());

	           if (currentLnItem.isTypeModel())
	           {
	               if (unitprice.compareTo(RegionalBigDecimal.ZERO) < 0)
	               {
	                   currentLnItem.getDialogErrors().add("Unit Price must be >= 0.00");
	                   logger.debug("Unit Price must be >= 0.00");
	                   return ;
	               }
	           } else
	           {
	               if (unitprice.compareTo(RegionalBigDecimal.ZERO) <= 0)
	               {
	            	   currentLnItem.getDialogErrors().add("Unit Price must be > 0.00");
	                   logger.debug("Unit Price must be > 0.00");
	                   
	                   return;
	               }
	           }
	       } catch (Exception exc)
	       {	
	    	   currentLnItem.getDialogErrors().add("Unit Price is  invalid");
	          
	           return;
	       }

	        // validate the unit price is in max price range -- warning
	        RegionalBigDecimal maxPrice = currentLnItem.getDecimal(currentLnItem.getMAXUNITPRICE());
	        RegionalBigDecimal minPrice = currentLnItem.getDecimal(currentLnItem.getMINUNITPRICE());
	        if (maxPrice.compareTo(RegionalBigDecimal.ZERO) != 0)
	        {
	            if (unitprice.compareTo(maxPrice) > 0)
	            {
	               currentLnItem.getDialogWarns().add("Unit Price exceeds maximum allowable value of " + maxPrice);
	               logger.debug("Unit Price exceeds maximum allowable value of " + maxPrice);
	            }
	            if (unitprice.compareTo(minPrice) < 0)
	            {
	            	currentLnItem.getDialogWarns().add("Unit Price is less than minimum allowable value of " + minPrice);
	            	logger.debug("Unit Price is less than minimum allowable value of " + minPrice);
	            }
	        }
	      
	        logger.debug("exit validateLnItemInput");
	}
	private void initLineItems(HttpServletRequest request, JSONResponse jsonResponse){
		String country	=request.getParameter("country");
		InvoiceELS invoice=initInvoice(request);
		updateLineItemsWithPOandCC(invoice);
		InvoiceLineItemsELS invoiceLineItemDialog= new InvoiceLineItemsELS(country);
		invoiceLineItemDialog.setLineItems(invoice.getLineItems());
		
		//Story 1750051 CA GIL changes
		invoiceLineItemDialog.setInvoiceNumber(invoice.getINVOICENUMBER());
		invoiceLineItemDialog.setInvoiceDate(invoice.getINVOICEDATE());
		invoiceLineItemDialog.setProvinceCode(invoice.getPROVINCECODE());
		invoiceLineItemDialog.setVatAmount(invoice.getVATAMOUNT());
		//End Story 1750051 CA GIL changes
		
		
		initializeVATCodes(country, invoiceLineItemDialog);
		initializeOtherCharges(country, invoiceLineItemDialog);
		
		
		String lineItemElsObject = ServiceHelper.objectToJson(invoiceLineItemDialog);
		logger.debug("building LineItemELSService response...");			
		jsonResponse.put(CNUtilConstants.INV_LN_ITEM_JSON,lineItemElsObject);
		
		
	}

	private LineItemELS initLineItem(HttpServletRequest request, DKDatastoreICM datastore){
		
		JsonObject jsonDataObject= new JsonParser().parse(request.getParameter("jsonLineItem")).getAsJsonObject();
		
		LineItemELS templnItem = (LineItemELS)ServiceHelper.jsonToObject(jsonDataObject,LineItemELS.class);
		LineItemELS currentLnItem=new LineItemELS(templnItem.getCOUNTRY());
		currentLnItem.copyLineItem(templnItem);
		currentLnItem.getDialogErrors().clear();
		currentLnItem.setDatastore(datastore);
		
		return currentLnItem;
		
		
	}
	private void initListItems(List<LineItemELS> lineItems, HttpServletRequest request){
		
		logger.debug("initLisItems");
		JSONArray a;
		try {
			logger.debug(request.getParameter("jsonLineItems"));
			a = new JSONArray(request.getParameter("jsonLineItems"));
			logger.debug("array: "+a);
			LineItemELS tempLineItemELS=null;


			for(int i =0; i<a.length();i++)
			{	logger.debug(a.get(i));
				org.json.JSONObject jsonResult =(org.json.JSONObject)a.get(i);
				 tempLineItemELS=(LineItemELS)ServiceHelper.JSONToObject(jsonResult,LineItemELS.class);
				LineItemELS	lnItem=new LineItemELS(tempLineItemELS.getCOUNTRY());
				lnItem.copyLineItem(tempLineItemELS);
				lineItems.add(lnItem);
			}
		} catch (Exception e) {
			logger.error("initListItems");
			logger.error(e.getMessage());
		}
		
		 
	}
	private InvoiceELS initInvoice(HttpServletRequest request) {
		
		JsonObject jsonDataObject= new JsonParser().parse(request.getParameter("jsonInvoiceEls")).getAsJsonObject();
		InvoiceELS invoice = (InvoiceELS)ServiceHelper.jsonToObject(jsonDataObject, InvoiceELS.class);
		invoice.getDialogErrorMsgs().clear();
		invoice.getInvoiceDataModelELS();
		return invoice;
	}
	
	private InvoiceLineItemsELS initInvoiceLineItemsELS(HttpServletRequest request){
		
		InvoiceLineItemsELS invoiceLineItemDialog=new InvoiceLineItemsELS();	
		invoiceLineItemDialog.setCountry(request.getParameter("country"));
		invoiceLineItemDialog.setInvoiceNumber(request.getParameter("invoiceNumber"));
		invoiceLineItemDialog.setInvoiceDate(request.getParameter("invoiceDate"));
		invoiceLineItemDialog.setProvinceCode( request.getParameter("provinceCode"));
		invoiceLineItemDialog.setVatAmount( request.getParameter("vatAmount"));

		return invoiceLineItemDialog;
		
	}
	
	private  void buildResponse(Object responseObject, JSONResponse jsonResponse, int type) throws Exception{
		
		String lineItemElsObject="";
		if(type==CNUtilConstants.A_LINE_ITEM_R){
			 lineItemElsObject = ServiceHelper.objectToJson((LineItemELS)responseObject);//currentlnitem
			logger.debug("building LineItemELSService response...");
			jsonResponse.put(CNUtilConstants.LN_ITEM_JSON,lineItemElsObject);
			
		}else if(type==CNUtilConstants.LINE_ITEM_DIALOG_R){
			lineItemElsObject = ServiceHelper.objectToJson((InvoiceLineItemsELS)responseObject);//invoiceLineItemDialog);
			logger.debug("building LineItemELSService response...");			
			jsonResponse.put(CNUtilConstants.INV_LN_ITEM_JSON,lineItemElsObject);
			
		}else if(type==CNUtilConstants.LIST_LINE_ITEMS_R){
			lineItemElsObject = ServiceHelper.objectToJson((List<LineItemELS>)responseObject);
			logger.debug("building LineItemELSService response...");			
			jsonResponse.put(CNUtilConstants.LINE_ITEMS_JSON,lineItemElsObject);
			
		}
		
		logger.debug("InvoiceELS Json:"+ServiceHelper.prettyPrint(lineItemElsObject));

	}
	

	 public void updateLineItemsWithPOandCC (InvoiceELS invoice) {
	        
	        LineItemELS aLineItemDataModel = null;

	        // move the data into the details gui
	        ArrayList details = invoice.getLineItems();
	        
	        // setup the cost center and the po number
	        for (int i=0; i<details.size(); i++)
	        {
	            aLineItemDataModel = (LineItemELS)details.get(i);
	            aLineItemDataModel.setCOSTCENTER(invoice.getCOSTCENTER());
	            if (aLineItemDataModel.getPONUMBER().trim().length() == 0)
	            {
	                aLineItemDataModel.setPONUMBER(invoice.getPURCHASEORDERNUMBER());
	            }
	        } 
	    }
	
	  /**
     * load the other charges from the database and populate the gui
     *  
     */
    private void initializeOtherCharges(String country, InvoiceLineItemsELS invoiceLineItemDialog)  {
    	
        try  {
        	LineItemELS dummyLineItem=new LineItemELS(country);

            dummyLineItem.setCOUNTRY(country);

            ArrayList<FormSelect> otherCharges=new ArrayList<FormSelect>();
            ResultSetCache cachedResults = dummyLineItem.getLineItemDataModelELS().selectOtherChargesStatement();
            FormSelect code=new OtherChangesFormSelect();
            // add a blank item
            code.setValue("");
        	code.setLabel("");
        	code.setSelected(true);
        	otherCharges.add(code);


            while (cachedResults.next())  {
            	
            	code=new OtherChangesFormSelect();
            	code.setValue(cachedResults.getString(1) + "/" + cachedResults.getString(2));
            	code.setLabel(cachedResults.getString(3));
            	otherCharges.add(code);
            }

            dummyLineItem.setOtherCharges(otherCharges);
            invoiceLineItemDialog.setOtherCharges(otherCharges);

        } catch (Exception exc)
        {
            logger.fatal(exc.toString());
            logger.error("Error initializing Other Charges");
        }
    }

    private void initializeVATCodes(String country, InvoiceLineItemsELS invoiceLineItemDialog)
    {
        try
        {	LineItemELS dummyLineItem=new LineItemELS(country);
       
            if(!country.equals("US")){

                List<FormSelect> vatCodes = new ArrayList<FormSelect>();
                HashMap<String,com.ibm.gil.util.RegionalBigDecimal> vatPercentages = new HashMap<String, RegionalBigDecimal> (); 
                HashMap<String, String> vatPercentagesStr = new HashMap<String, String> (); 
                FormSelect code = null;
                //adding blank item
            	//Hashtable VATPercentages = new Hashtable();
                code = new VatCodesNonUSFormSelect();
            	code.setValue(" ");
            	code.setLabel(" ");
            	vatCodes.add(code);

            	if (country.equals("CA")){
                    dummyLineItem.getLineItemDataModelELS().loadVATCodesForCA(vatPercentages, vatPercentagesStr, vatCodes, invoiceLineItemDialog);
            	}  else {
                
		            	ResultSetCache cachedResults = dummyLineItem.getLineItemDataModelELS().selectVATCodesStatement();
		            	while (cachedResults.next())
		            	{
		            		logger.debug("line items vatcode: "+cachedResults.getString(2) +"--"+cachedResults.getString(4));
		//            		VATCode = new ComboItemDescription(cachedResults.getString(2), cachedResults.getString(4));
		            		code=new VatCodesNonUSFormSelect();
		            		code.setValue(cachedResults.getString(2));
		                	code.setLabel(cachedResults.getString(4));
		                	
		//            		vatCode=cachedResults.getString(2) +"#~#"+cachedResults.getString(4);
		            		
		            		if (cachedResults.getString(3).equals("*"))
		            		{            			
		            			code.setSelected(true);
		            			dummyLineItem.setDefaultVatCode(code.getValue());
		            		}
		            		
		            		vatCodes.add(code);
		            		vatPercentages.put(cachedResults.getString(2), cachedResults.getRegionalBigDecimal(5));
		            		vatPercentagesStr.put(cachedResults.getString(2), cachedResults.getRegionalBigDecimal(5).toString());
		            	}

            }
            	
        	invoiceLineItemDialog.setVatCodes(vatCodes);
    		invoiceLineItemDialog.setVatPercentages(vatPercentages);
    		invoiceLineItemDialog.setVatPercentagesStr(vatPercentagesStr); 	
        		

                //**
            }else{
            	invoiceLineItemDialog.setDisableVatCodes(true);
            }

 
        } catch (Exception exc)
        {
            logger.fatal(exc.toString());
            logger.error("Error initializing Tax codes");
        }
    }
    
	private void importSerial(PluginServiceCallbacks callbacks,  HttpServletRequest request, JSONResponse jsonResponse)	throws Exception {
		
		SerialNumberELS serialNumber = new SerialNumberELS();
		logger.debug("Entry ImportSerial");
		logger.debug("locale:" +callbacks.getLocale());
		serialNumber.setLocale(callbacks.getLocale());
		String country	=request.getParameter("country");
		serialNumber.setCOUNTRY(country);
		

		
		List<LineItemELS> lineItems= new ArrayList<LineItemELS>();
		initListItems(lineItems,request);
		logger.debug("callback.getrequesActionForm"+callbacks.getRequestActionForm());
		 BufferedReader brCsv = ServiceHelper.getUploadedFile(callbacks.getRequestActionForm());
		 ArrayList<SerialNumberELS> listOfSerialNumbers = serialNumber.importSerials(brCsv);
		 createImportedTMs(request, lineItems,listOfSerialNumbers);
		 
		 String lineItemElsObject = ServiceHelper.objectToJson(lineItems);
		 logger.debug("building LineItemELSService response...");			
		 jsonResponse.put(CNUtilConstants.LINE_ITEMS_JSON,lineItemElsObject);

//		 ServiceHelper.putJsonResponse(jsonResults, "snImportJsonList", listOfSerialNumbers);
	}
	
	private void importParts(PluginServiceCallbacks callbacks,  HttpServletRequest request , JSONResponse jsonResponse)	throws Exception {
		
		SerialNumberELS serialNumber = new SerialNumberELS();
		serialNumber.setLocale(callbacks.getLocale());
		String country	=request.getParameter("country");
		serialNumber.setCOUNTRY(country);
		
		
		List<LineItemELS> lineItems= new ArrayList<LineItemELS>();
		initListItems(lineItems,request);
		 BufferedReader brCsv = ServiceHelper.getUploadedFile(callbacks.getRequestActionForm());
		 ArrayList<SerialNumberELS> listOfSerialNumbers = serialNumber.importParts(brCsv);
		 createImportedParts(request, lineItems, listOfSerialNumbers);
		 //build response
		 String lineItemElsObject = ServiceHelper.objectToJson(lineItems);
		 logger.debug("building LineItemELSService response...");			
		 jsonResponse.put(CNUtilConstants.LINE_ITEMS_JSON,lineItemElsObject);
		 
		 
		 
//		 ServiceHelper.putJsonResponse(jsonResults, "snImportJsonList", listOfSerialNumbers);
	}
	private void validateSaveLineItems(PluginServiceCallbacks callbacks,  HttpServletRequest request , JSONResponse jsonResponse)	throws Exception {
		String country	=request.getParameter("country");
		InvoiceELS invoice=initInvoice(request);
		
		List<LineItemELS> lineItems=new ArrayList<LineItemELS>();
		initListItems(lineItems, request);
		InvoiceLineItemsELS tmpInvoice=new InvoiceLineItemsELS(country);
		
		if(revalidateInput(tmpInvoice, lineItems, country, jsonResponse)){

				LineItemELSServiceHelper.renumberLineItems(lineItems);
				tmpInvoice.setLineItems(lineItems);
				//build response
				String lineItemElsObject = ServiceHelper.objectToJson(tmpInvoice);
				logger.debug("building LineItemELSService response...");			
				jsonResponse.put(CNUtilConstants.INV_LN_ITEM_JSON,lineItemElsObject);
				
				
				
				
		}
	}
	private void validateLineItems(){
		
	}
	private void createImportedTMs(HttpServletRequest request, List<LineItemELS> lineItems,ArrayList<SerialNumberELS> listOfSerialNumbers){
		LineItemELS tmpItem=null;
		String billedToIgfIndc	=request.getParameter("billedToIgfIndc");
		String usTaxPercent		=request.getParameter("usTaxPercent");
		String usTaxIndc		=request.getParameter("usTaxIndc");
		String country	=request.getParameter("country");
		String vatCode=request.getParameter("vatCode");
		LineItemELS dummy = new LineItemELS(country);
		HashMap<String, RegionalBigDecimal> vatPercentages= new HashMap<String, RegionalBigDecimal>();
        List<FormSelect> vatCodes= new ArrayList<FormSelect>();
        InvoiceLineItemsELS invoiceLineItemDialog= initInvoiceLineItemsELS(request);
        invoiceLineItemDialog.setLineItems(lineItems);
        dummy.getLineItemDataModelELS().loadSelectVatCodes(vatPercentages, vatCodes, invoiceLineItemDialog);
        String defaultVatCode=dummy.getDefaultVatCode();
		for (SerialNumberELS serialNumberELS : listOfSerialNumbers) {
			tmpItem=new LineItemELS(country);
			tmpItem.setQUANTITY("1");
			tmpItem.setINVOICEITEMTYPE("TM");
			tmpItem.setPARTNUMBER("");
			tmpItem.setSUBLINENUMBER("0");
			if(tmpItem.isUSCountry()){
				setUSTaxPercentage(tmpItem, country,usTaxIndc,billedToIgfIndc,usTaxPercent );
				if(usTaxPercent==null ||usTaxPercent.equals("") || Float.parseFloat(usTaxPercent)==0){
					tmpItem.setVATCODE(CNUtilConstants.NO_TAX);
				}else{
					tmpItem.setVATCODE(CNUtilConstants.WITH_TAX);
				}
				
			}else{
				if(!vatCode.equals("")){
					tmpItem.setVATCODE(vatCode);
				}
			}
			
			tmpItem.setTYPE(serialNumberELS.getTYPE()!=null?serialNumberELS.getTYPE():"");
			tmpItem.setMODEL(serialNumberELS.getMODEL()!=null?serialNumberELS.getMODEL():"");
			tmpItem.setSERIAL(serialNumberELS.getSERIALNUMBER()!=null?serialNumberELS.getSERIALNUMBER():"");
			tmpItem.setUNITPRICE(serialNumberELS.getUNITPRICE().toString()!=null?serialNumberELS.getUNITPRICE().toString():"0.00");
			tmpItem.setTERM(serialNumberELS.getTERM()!=null?serialNumberELS.getTERM():"");
			if(!country.equals("US")) tmpItem.setVATCODE(defaultVatCode);
			tmpItem.getLineItemDataModelELS().recalculateVATAmount(vatPercentages);
			tmpItem.getLineItemDataModelELS().selectTypeModelDescription(country);
			if(serialNumberELS.getDESCRIPTION().trim().length()>0){
				tmpItem.setDESCRIPTION(serialNumberELS.getDESCRIPTION().trim());
			}
			lineItems.add(tmpItem);
			
		}
		LineItemELSServiceHelper.renumberLineItems(lineItems);
		
	}
	
	private void createImportedParts(HttpServletRequest request,List<LineItemELS> lineItems, ArrayList<SerialNumberELS> listOfSerialNumbers){
		LineItemELS tmpItem=null;
		String country	=request.getParameter("country");
		String billedToIgfIndc	=request.getParameter("billedToIgfIndc");
		String usTaxPercent		=request.getParameter("usTaxPercent");
		String usTaxIndc		=request.getParameter("usTaxIndc");
		String vatCode=request.getParameter("vatCode");
		LineItemELS dummy = new LineItemELS(country);
        InvoiceLineItemsELS invoiceLineItemDialog= initInvoiceLineItemsELS(request);
        invoiceLineItemDialog.setLineItems(lineItems);
        HashMap<String, RegionalBigDecimal> vatPercentages= new HashMap<String, RegionalBigDecimal>();
        List<FormSelect> vatCodes= new ArrayList<FormSelect>();
        dummy.getLineItemDataModelELS().loadSelectVatCodes(vatPercentages, vatCodes, invoiceLineItemDialog);
        String defaultVatCode=dummy.getDefaultVatCode();
		for (SerialNumberELS serialNumberELS : listOfSerialNumbers) {
			tmpItem=new LineItemELS(country);
			tmpItem.setQUANTITY("1");
			tmpItem.setINVOICEITEMTYPE("P");
			tmpItem.setPARTNUMBER(serialNumberELS.getPART());
			tmpItem.setSUBLINENUMBER("0");
			if(tmpItem.isUSCountry()){
				setUSTaxPercentage(tmpItem, country,usTaxIndc,billedToIgfIndc,usTaxPercent );
				if(usTaxPercent==null ||usTaxPercent.equals("") || Float.parseFloat(usTaxPercent)==0){
					tmpItem.setVATCODE(CNUtilConstants.NO_TAX);
				}else{
					tmpItem.setVATCODE(CNUtilConstants.WITH_TAX);
				}
				
				
			}else{
				if(!vatCode.equals("")){
					tmpItem.setVATCODE(vatCode);
				}
			}
			
			tmpItem.setTYPE("");
			tmpItem.setMODEL("");
			tmpItem.setSERIAL("");
			
			tmpItem.setUNITPRICE(serialNumberELS.getUNITPRICE().toString());
			tmpItem.setDESCRIPTION(serialNumberELS.getDESCRIPTION());
			if(!country.equals("US"))tmpItem.setVATCODE(defaultVatCode);
			tmpItem.getLineItemDataModelELS().recalculateVATAmount(vatPercentages);
			
			lineItems.add(tmpItem);
			
		}
		LineItemELSServiceHelper.renumberLineItems(lineItems);
		
	}
    public void setUSTaxPercentage(LineItemELS tmpItem, String country, String usTaxIndc, String billedToIgfIndc, String usTaxPercent){
    	
     
        if(country.equals("US")){
//        	InvoiceDataModel anInvoiceDataModel = (InvoiceDataModel) getParentController().getDataModel();
//        	tmpItem.setUSTAXPERCENT(anInvoiceDataModel.getTAXESINDICATOR());
        	if(usTaxIndc.equals("Y") && billedToIgfIndc.equals("N")){
        		tmpItem.setUSTAXPERCENT(null);
        	}else{
        		tmpItem.setUSTAXPERCENT(usTaxPercent);//anInvoiceDataModel.getUSTAXPERCENT());
        	}
        }
    }

	@Override
	protected void indexDocument(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request,
			Indexing indexing) throws Exception {
		
		//Not used, the indexed document will be the InvoiceEls, LineItem is part of an Invoice
		
	}

	@Override
	protected void saveDocument(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request,
			Indexing indexing) throws Exception {
		// Not used will be saved once InvoiceEls is saved.
		
	}
	
	private boolean revalidateInput(InvoiceLineItemsELS tmpInvoice, List<LineItemELS> lineItems, String country, JSONResponse jsonResponse){
		Hashtable serials = new Hashtable();
        Hashtable dupserials = new Hashtable();
        String serialNumber = null;
        String key = null;
        String keyset = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		if(lineItems.size()>2500){
			tmpInvoice.getDialogErrorMsgs().add("Exceed maximum quantity of line items");
			return false;
		}
		int i=0;
		tmpInvoice.getDialogWarns().clear();
		tmpInvoice.getItemsWithWarnings().clear();
		for (LineItemELS lineItemELS : lineItems) {
			//cleaning possible msg errors
			
			lineItemELS.getDialogErrors().clear();
			lineItemELS.setGridIndex("");
			boolean isSerialValid=false;
			
			if(lineItemELS.isTypeModel()){
				
				String description = "";
            	if((lineItemELS.getDESCRIPTION()!=null) && 
            			!lineItemELS.getDESCRIPTION().equals(GILConstants.EMPTY_STRING) &&
            				!lineItemELS.getDESCRIPTION().equals(GILConstants.X_TYPE)){
            		description = lineItemELS.getDESCRIPTION();
            	}
            	/***
            	 * if it is not a Blank TM (XXXX) && description is empty  then go in and  show error message Type/Model not found.
            	 * OR
            	 * if it is not an XXXX and SelectTypeDesciption cant find a match in ICFs for this TM then show error message Tyep/Model not found.
            	 */
                if (((!lineItemELS.getBLANKTYPEMODEL().equals("Y") &&
                		lineItemELS.getDESCRIPTION().trim().length() == 0)&&
                		!lineItemELS.getTYPE().equalsIgnoreCase(GILConstants.X_TYPE))
                		|| 
                		(!(lineItemELS.getTYPE().equalsIgnoreCase(GILConstants.X_TYPE)) &&
                				!lineItemELS.getLineItemDataModelELS().selectTypeModelDescription(country))){
                	/**Removing this If and moving above condition, cause it was going to run queries to database even when XXXX TM
                	 * Since below there was a condition checking that if it was  XXXX TM don't show this error
                	 * it means it excludes XXXX TM, why to go to database to check this TM if it is already known this TM will return False.***/
//                	if(!(lineItemELS.getTYPE().equalsIgnoreCase(GILConstants.X_TYPE))){
                		//set index
                		logger.debug("RevalidateInput: lineItemELS"+lineItemELS.getLINENUMBER()+lineItemELS.getSUBLINENUMBER()+" blankTM:"+lineItemELS.getBLANKTYPEMODEL()+"-Description:"+lineItemELS.getDESCRIPTION().trim()+
                				"-invoiceItemType:"+lineItemELS.getINVOICEITEMTYPE()+"-Type: "+lineItemELS.getTYPE());
                		lineItemELS.setGridIndex(""+i);
                		lineItemELS.getDialogErrors().add(" Type/Model not found");
                		
                		buildLnItemResponse(jsonResponse, lineItemELS);
//                		tmpInvoice.getDialogErrorMsgs().add(lineItemELS.getLINENUMBER()+"-"+lineItemELS.getSUBLINENUMBER()+" Type/Model not found");
//                    aLineItemFrame.selectRecord(aDataModel);
//                    requestFieldFocus(aDataModel.getTYPEidx());
                    // retaining description if the user changed it manually
                    //not needed in icn Jan 2018
//                    if(!description.equals(GILConstants.EMPTY_STRING))
//                    	lineItemELS.setDESCRIPTION(description);
                    
                    	return false;
//                	}
                    	
                }
                lineItemELS.setDESCRIPTION(description);
                lineItemELS.setSERIAL(lineItemELS.getSERIAL().trim());
                if(lineItemELS.getSERIAL().length()>25)
            	{
                	lineItemELS.setGridIndex(""+i);
            		lineItemELS.getDialogErrors().add("Values on spreadsheet exceed field value");
            		buildLnItemResponse(jsonResponse, lineItemELS);
            		//                	tmpInvoice.getDialogErrorMsgs().add(lineItemELS.getLINENUMBER()+"-"+lineItemELS.getSUBLINENUMBER()+" Values on spreadsheet exceed field value");
                	return false;
            	}
             // validate the unit price > 0
                RegionalBigDecimal unitprice = RegionalBigDecimal.ZERO;
                try
                {
                    unitprice = lineItemELS.getDecimal(lineItemELS.getUNITPRICE());

                    if (unitprice.compareTo(RegionalBigDecimal.ZERO) <= 0)
                    {
                    	lineItemELS.setGridIndex(""+i);
                		lineItemELS.getDialogErrors().add(" Unit Price must be > 0.00");
                		buildLnItemResponse(jsonResponse, lineItemELS);
//                        tmpInvoice.getDialogErrorMsgs().add(lineItemELS.getLINENUMBER()+"-"+lineItemELS.getSUBLINENUMBER()+" Unit Price must be > 0.00");
//                        aLineItemFrame.selectRecord(aDataModel);
//                        requestFieldFocus(aDataModel.getUNITPRICEidx());
                        return false;
                    }
                } catch (Exception exc)
                {
                	lineItemELS.setGridIndex(""+i);
            		lineItemELS.getDialogErrors().add(" Unit Price must be > 0.00");
            		buildLnItemResponse(jsonResponse, lineItemELS);

                    logger.debug("Unit Price is invalid");
                    
                    return false;
                }

             if( !validateSerialNumber(i,serials,dupserials,key,keyset,lineItemELS,tmpInvoice)){
            	 
            	 buildLnItemResponse(jsonResponse, lineItemELS);
            	 return false;
             }
             
             lineItemELS.setPARTNUMBER(lineItemELS.getPARTNUMBER().trim());
			}else if(lineItemELS.getPARTNUMBER().length()>25){
				lineItemELS.setGridIndex(""+i);
        		lineItemELS.getDialogErrors().add("Values on spreadsheet exceed field value");
        		buildLnItemResponse(jsonResponse, lineItemELS);
				//tmpInvoice.getDialogErrorMsgs().add(lineItemELS.getLINENUMBER()+"-"+lineItemELS.getSUBLINENUMBER()+" Values on spreadsheet exceed field value" );
				return false;
			}
			i++;
			
		}
		//Will manage in front end will allow to call renumber  line items and will pop up warning, in cm8.4 seems to be not working properly
//		if(!tmpInvoice.getDialogWarns().isEmpty()){
//			
//			String lineItemElsObject = ServiceHelper.objectToJson(tmpInvoice);
//			logger.debug("building LineItemELSService response...");			
//			jsonResponse.put(CNUtilConstants.INV_LN_ITEM_JSON,lineItemElsObject);
//			return false;
//		}

		
		return true;
	}

	public boolean validateSerialNumber(int i,Hashtable serials, Hashtable dupserials,
			String key, String keyset,LineItemELS lineItemELS, InvoiceLineItemsELS tmpInvoice){
		
		String serialNumber = lineItemELS.getSERIAL();
         
         boolean alphanumFlag = true;
         for (int j = 0; j < serialNumber.length(); j++)
         {
             if (keyset.indexOf(serialNumber.charAt(j)) < 0)
             {
                 alphanumFlag = false;
                 lineItemELS.setSERIAL(lineItemELS.getSERIAL().replace(serialNumber.charAt(j), ' '));
             }
         }
         if (alphanumFlag == false)
         { 
//             getTablePanel().recordChanged();
         	lineItemELS.setGridIndex(""+i);
     		lineItemELS.getDialogErrors().add("Serial Number " + serialNumber + " is not alpha numeric");
     		
//             tmpInvoice.getDialogErrorMsgs().add("Serial Number " + serialNumber + " is not alpha numeric");
//             aLineItemFrame.selectRecord(aDataModel);
//             requestFieldFocus(aDataModel.getSERIALidx());
             return false;
         }

         key = lineItemELS.getTYPE() + ":" + lineItemELS.getMODEL() + ":" + lineItemELS.getMESNUMBER() + ":" + serialNumber;
         if (serialNumber.trim().length() > 0)
         {
             if (serials.containsKey(key))
             {
             	lineItemELS.setGridIndex(""+i);
         		
//                 aLineItemFrame.selectRecord(aDataModel);
                	tmpInvoice.getDialogWarns().add("Serial Number " + serialNumber + " is not unique");
                	tmpInvoice.getItemsWithWarnings().add(i+"");
                	dupserials.put(key, serialNumber);
                	serials.remove(key);
             } else if(!dupserials.containsKey(key)){
                 		serials.put(key, serialNumber);
             }
             
         }	
         return true;

	}
	private void getTMDesc(PluginServiceCallbacks callbacks, HttpServletRequest request,JSONResponse jsonResponse){
		String country	=request.getParameter("country");
		String repositoryId =request.getParameter("repositoryId");
		
		DKDatastoreICM datastore = callbacks.getCMDatastore(repositoryId);
		LineItemELS dummyItem=initLineItem(request, datastore);
		dummyItem.setTmDescFlag(dummyItem.getLineItemDataModelELS().selectTypeModelDescription(country));
		
		//build response
		String lineItemElsObject = ServiceHelper.objectToJson(dummyItem);
		logger.debug("building LineItemELSService response...");
		jsonResponse.put(CNUtilConstants.LN_ITEM_JSON,lineItemElsObject);

	}
}
