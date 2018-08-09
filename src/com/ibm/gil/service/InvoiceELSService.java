package com.ibm.gil.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;






import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ibm.ecm.extension.PluginServiceCallbacks;

import com.ibm.ecm.json.JSONResponse;
import com.ibm.gil.business.Indexing;
import com.ibm.gil.business.FormSelect;
import com.ibm.gil.business.InvoiceELS;
import com.ibm.gil.business.InvoiceLineItemsELS;
import com.ibm.gil.business.LineItemELS;
import com.ibm.gil.business.OfferingLetterELS;
import com.ibm.gil.business.VendorSearchELS;
import com.ibm.gil.exception.GILCNException;
import com.ibm.gil.model.DataModel;

import com.ibm.gil.service.helper.LineItemELSServiceHelper;
import com.ibm.gil.util.CNUtilConstants;
import com.ibm.gil.util.GilUtility;
import com.ibm.gil.util.UtilGilConstants;





//import com.ibm.igf.gil.InvoiceDataModel;
//import com.ibm.igf.gil.VendorSearchDataModel;
//import com.ibm.igf.gil.InvoiceDataModel;
//import com.ibm.igf.gil.InvoiceFrame;
//import com.ibm.igf.gil.LineItemDataModel;
//import com.ibm.igf.gil.LineItemEventController;
//import com.ibm.igf.gil.OfferingLetterDataModel;
import com.ibm.gil.util.RegionalBigDecimal;

import com.ibm.json.java.JSON;

import com.ibm.json.java.JSONObject;
import com.ibm.mm.sdk.server.DKDatastoreICM;
import com.ibm.w3.financing.tools.gcms.query.results.DOCUMENT;



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
public class InvoiceELSService  extends GILService {

	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(InvoiceELSService.class);
		
		 
	
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
		return "InvoiceELSService";
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
	
//	public void execute(PluginServiceCallbacks callbacks,
//			HttpServletRequest request, HttpServletResponse response)
//			throws Exception {
//		this.request=request;
//		this.callbacks=callbacks;
//		jsonResponse=new JSONResponse();
//		
//		String configuration = callbacks.loadConfiguration();
//		
//		PluginConfigurationPane.loadPropertiesMap(configuration);
//		
//		
//		businessAttributes=request.getParameter("businessAttributes");
//		country	=request.getParameter("country");
//		repositoryId =request.getParameter("repositoryId");
//		optionToExecute=request.getParameter("request");
//		indexClass=request.getParameter("className");
//		objectId=request.getParameter("itemId");
//		datastore = callbacks.getCMDatastore(repositoryId);
//	
//	
//		JSONObject jsonObjectBA=(JSONObject)JSON.parse(businessAttributes);
//		
//		
//
//		//Getting values needed for backend request service
//		
//		String invoiceNum=	(String)jsonObjectBA.get("invNum");
//		
//		
//		String invoiceDate=(String)jsonObjectBA.get("invDate");
//		logger.debug("from CN Invoice date: "+invoiceDate);
//		
//		
//		String source=		(String)jsonObjectBA.get("source");
//		
//		String userId=		(String)jsonObjectBA.get("userId");
//		String timeStamp=	(String)jsonObjectBA.get("createdTimestamp");
//		String businessPartner=(String)jsonObjectBA.get("businessPartner");
//
//		invoice=new InvoiceELS(country,datastore);
//		invoice.setLocale(callbacks.getLocale());	
//			
//		invoice.setINVOICENUMBER(invoiceNum);
//		
//		invoice.setOBJECTID(objectId);
////		invoice.generateUNIQUEREQUESTNUMBER();
//		invoice.setUSERID(userId);
//		invoice.setINDEXCLASS(indexClass);
//	
//		if(optionToExecute.equals(UtilGilConstants.INDEX)){
//			//load index values new way*** won't use loadIndexValues method since getting the index values from CN 
//			String[] cmValues={invoiceNum,invoiceDate,businessPartner,source,timeStamp, userId};	
//			invoice.setINVOICEDATE(GilUtility.formatDate(invoiceDate, CNUtilConstants.CN_DATE_FORMAT, CNUtilConstants.GIL_DATE_FORMAT));
//			invoice.setCREATEDATE(GilUtility.returnDate(timeStamp, CNUtilConstants.CN_DATE_FORMAT2, CNUtilConstants.GIL_DATE_FORMAT));
//			logger.debug("index invoice created date as date: "+invoice.getCREATEDATEasDate());
//			logger.debug("index invoice created date as string: "+invoice.getCREATEDATE());
//			logger.debug("index invoice date formatted: "+invoice.getINVOICEDATE());
//			invoice.setVENDORNAME(businessPartner);
//			invoice.setValues(cmValues);
//			if(invoice.getINVOICEDATE()==null ||invoice.getINVOICEDATE().isEmpty() || 
//					invoice.getCREATEDATE()==null || invoice.getCREATEDATE().isEmpty()||  invoice.getVENDORNAME()==null){
//				invoice.getDialogErrorMsgs().add("Waiting for Indexing to complete. Please try again later.");
//			}else{
//			//*****************
//				index();
//			}
//		}else if (optionToExecute.equalsIgnoreCase(UtilGilConstants.SAVE))	{
//			save();				
//		}else if (optionToExecute.equalsIgnoreCase(UtilGilConstants.RECALCULATE_INVAMT))	{
//			recalculateNetAmount();
//		}else if (optionToExecute.equalsIgnoreCase(UtilGilConstants.USTAX_CALC))	{
//			usTaxCalculation();
//		}else if (optionToExecute.equalsIgnoreCase(UtilGilConstants.SEARCH_BYOL))	{
//			searchByOffering();
//		}else if (optionToExecute.equalsIgnoreCase(UtilGilConstants.SEARCH_BYOL_CUST_NAME))	{
//			searchByCustomerName();
//		}else if (optionToExecute.equalsIgnoreCase(UtilGilConstants.SEARCH_BY_INST_CUSTNAME))	{
//			searchByInstalledCustomerName();
//		}else if (optionToExecute.equalsIgnoreCase(UtilGilConstants.SEARCH_BY_INST_CUSTNUM))	{
//			searchByInstalledCustomer();
//		}else if (optionToExecute.equalsIgnoreCase(UtilGilConstants.VALIDATE_INPUT)){
//			validateInput();
//		}else if (optionToExecute.equalsIgnoreCase(UtilGilConstants.SEARCH_REF_INV)){
//			searchByReferenceInvoice();
//		}else if (optionToExecute.equalsIgnoreCase(UtilGilConstants.SAVE_STATE)){
//			saveState();
//		}
////		else if (optionToExecute.equalsIgnoreCase(UtilGilConstants.BUILD_LINE_ITEMS)){
////			buildLineItemsToPresent();
////		}
//	//java 7	
////		try{
////			switch(optionToExecute) {
////			
////			case UtilGilConstants.INDEX:
////				index(callbacks, request, jsonResponse);
////				break;
////			case UtilGilConstants.SAVE:
////				break;
////				
////			default:
////				logger.debug("Invalid InvoiceELS Action: "+optionToExecute);
////				jsonResponse.addErrorMessage(new JSONMessage(0, "Invalid action with " + indexClass +". ","", "","Check the IBM Content Navigator logs for more details.", ""));
////				break;
////				
////			}
////		}catch(Exception e){
////			logger.fatal(e.getMessage());
////			JSONMessage jsonErroMessage = new JSONMessage(0, "Indexing " + indexClass +" failed. ", e.getMessage(), "","Check the IBM Content Navigator logs for more details.", "");
////			jsonResponse.addErrorMessage(jsonErroMessage);
////		}
//		buildResponse();
////		jsonResponse.serialize(response.getOutputStream());
////		callbacks.getLogger().logExit(this, "execute", request);
//	}
	

	
	private void searchByReferenceInvoice( InvoiceELS invoice) throws Exception
	{
		logger.debug("searchByReferenceInvoice method");
//		JsonObject jsonDataObject= new JsonParser().parse(request.getParameter("jsonInvoiceEls")).getAsJsonObject();
//		invoice = (InvoiceELS)ServiceHelper.jsonToObject(jsonDataObject, InvoiceELS.class);
//		invoice.getDialogErrorMsgs().clear();
//		invoice.setDatastore(this.datastore);
		
		if(!displayReferenceInvoiceSearchWindow(invoice)){
			logger.debug("displayReferenceInvoiceSearchWindow: false");
			invoice.setREFERENCEINVOICENUMBER("");
		}
		logger.debug("End searchByReferenceInvoice method");
	}
	
	
//	private void index(InvoiceELS invoice) throws Exception {
//	
//			initalizeIndexing(invoice);
//
//	
//	}
//	
	private  void buildResponse(InvoiceELS invoice, JSONResponse jsonResponse) throws Exception{
		String invoiceElsObject = ServiceHelper.objectToJson(invoice);
		logger.debug("building InvoiceELSService response...");
//		logger.debug("InvoiceELS Json:"+jsonString);
		
		jsonResponse.put(CNUtilConstants.INVOICE_ELS_JSON,invoiceElsObject);
		
//		jsonResponse.put("createdTimestamp", "");
		
		logger.debug("InvoiceELS Json:"+ServiceHelper.prettyPrint(invoiceElsObject));

	}
	
	
	
	private void save(InvoiceELS invoice) throws Exception {
		logger.debug("save method");

		
		updateLineItemsWithPOandCC(invoice);
		updateLineItemsWithEquipSource(invoice);
		if(invoice.getInvoiceDataModelELS().saveDocument()){
			invoice.getInvoiceDataModelELS().updateIndexValues();
		}else{
			invoice.getDialogErrorMsgs().add("Failure Saving Invoice, for more information please review Gil Log. ");
		}
		logger.debug("End save method");
	}
	private void validateInput(InvoiceELS invoice) throws Exception{
		logger.debug("validateInput method");
		
		//----
		
		 RegionalBigDecimal commission = RegionalBigDecimal.ZERO;
		  RegionalBigDecimal NinetyNinePointNineNineNine = new RegionalBigDecimal(99.999).setScale(3, RegionalBigDecimal.ROUND_HALF_UP);
	        RegionalBigDecimal bd99 = NinetyNinePointNineNineNine;
	        try
	        {
	            commission = GilUtility.getDecimal(invoice.getVENDORCOMMISSION());
	        } catch (Exception exc)
	        
	        {	invoice.getDialogErrorMsgs().add(exc.getMessage());
	        	logger.debug(exc.getMessage());
	        return;
	        }
	        logger.debug("InvoiceELS Validating - VendorCommission value: "+ invoice.getVENDORCOMMISSION());
	        if (invoice.isCommissionInvoice() && (!commission.equals(bd99)))
	        {
	            if (invoice.getDOCUMENTMODE() != Indexing.UPDATEMODE)
	            {
	            	logger.debug("InvoiceELS Validating -documentMode value: "+ invoice.getDOCUMENTMODE());
	                
	                invoice.getDialogErrorMsgs().add("Commission Invoices are not valid for this supplier");
	                return;
	            }
	        }
	        RegionalBigDecimal netAmountvalue = RegionalBigDecimal.ZERO;
	        RegionalBigDecimal vatAmountvalue = RegionalBigDecimal.ZERO;
	        vatAmountvalue = GilUtility.getDecimal(invoice.getVATAMOUNT());
	        netAmountvalue = GilUtility.getDecimal(invoice.getNETAMOUNT());
	        float vatValue = vatAmountvalue.floatValue(); 
	        float netValue = netAmountvalue.floatValue()  ; 
	        
      	
	        // retrieve the scandate from CM not useful
//			invoice.getInvoiceDataModelELS().retrieveCreatedDate();		
			Date scanDate = invoice.getCREATEDATEasDate();
			Date invoiceDate  = invoice.getINVOICEDATEasDate();
			if (scanDate.getTime() < invoiceDate.getTime()) {
				invoice.getDialogErrorMsgs().add("Scan Date cannot be before the Invoice Date");
				return;
			}
			 RegionalBigDecimal vatamount = RegionalBigDecimal.ZERO;
			 RegionalBigDecimal totalamount = RegionalBigDecimal.ZERO;
			 vatamount= GilUtility.getDecimal(invoice.getVATAMOUNT());
			 totalamount= GilUtility.getDecimal(invoice.getTOTALAMOUNT());
	        // validate the total and tax amounts
	        if (vatamount.compareTo(totalamount) > 0)
	        {
	        	invoice.getDialogErrorMsgs().add("Tax amount must not be greater than Total amount");
	        	return;
	        }
	        
	        //---
	        ArrayList results;
	        try
	        {
	            if ((!invoice.getINVOICENUMBER().equals(invoice.getOLDINVOICENUMBER())) || 
	            		(!invoice.getINVOICEDATE().equals(invoice.getOLDINVOICEDATE())))
	            {
	                results = invoice.getInvoiceDataModelELS().selectByInvoiceStatement();
	                for (int i = 0; i < results.size(); i++)
	                {
	                    InvoiceELS aIDM = (InvoiceELS) results.get(i);
	                    if ((aIDM.getINVOICENUMBER().equals(invoice.getINVOICENUMBER()) && (aIDM.getINVOICEDATE().equals(invoice.getINVOICEDATE()))) && (!aIDM.getOBJECTID().equals(invoice.getOBJECTID())))
	                    {
	                    	invoice.getDialogErrorMsgs().add("Duplicate Invoice entered");
	                    	return;
	                    }
	                }
	            }
	        } catch (Exception e)
	        {
	        	invoice.getDialogErrorMsgs().add("Error validating Invoice Number");
	        	invoice.getDialogErrorMsgs().add(e.toString());
	        	logger.fatal(e.toString());
	        	return;

	        }
	        
	    	if(invoice.isCRInvoice() && invoice.getREFERENCEINVOICENUMBER().trim().length()>0){
	    		boolean supplierFound = false;
	    		boolean debitInvoiceFound = true;
	    		int invoiceCount = 0;
	    		String supNum = "";
	    		results = invoice.getInvoiceDataModelELS().selectByReferenceInvoiceStatement();
	    		if(results.size() == 0){
	    			debitInvoiceFound = false;		
	    		}
	            for (int i = 0; i < results.size(); i++)
	            {
	                InvoiceELS aIDM = (InvoiceELS) results.get(i);
	                if(aIDM.isDBInvoice()){
	                	invoiceCount++;
	               		if(aIDM.getINVOICEDATE().equals(invoice.getREFERENCEINVOICEDATE())){
	                 			if(aIDM.getVENDORNUMBER().equals(invoice.getVENDORNUMBER().trim())){
	                 				supplierFound = true;
	                 			}else{
	                 				supNum = aIDM.getVENDORNUMBER();
	                 			}
	              		}
	                }else{
	                	debitInvoiceFound = false;
	                }
	            }
	            if(!debitInvoiceFound && invoiceCount == 0){
	            	logger.debug("Debit invoice is not found in GCMS");
	            	invoice.getDialogErrorMsgs().add("Debit invoice is not found in GCMS");
	            	return;
	            }
	            if(!supplierFound){
	            	invoice.getDialogErrorMsgs().add("Supplier number on debit invoice is not the same as credit  invoice ("+supNum +") for debit)");
	            	return;
	            }
	    	}
	        if (invoice.areLineItemsRequired())
	        {
	            RegionalBigDecimal unitvatvalue = RegionalBigDecimal.ZERO;
	            try
	            {
	                unitvatvalue = invoice.getDecimal(invoice.getLINEITEMVATAMOUNT());
	                if (unitvatvalue.compareTo(RegionalBigDecimal.ZERO) < 0)
	                {
	                	invoice.getDialogErrorMsgs().add("Line Item Tax Amount must be >= 0.00");
	                	return;
	                }

	            } catch (Exception exc)
	            {
	            	invoice.getDialogErrorMsgs().add("Line Item Tax Amount is invalid");
	            	return;
	            }

	            RegionalBigDecimal unitnetvalue = RegionalBigDecimal.ZERO;
	            RegionalBigDecimal netvalue = GilUtility.getDecimal(invoice.getNETAMOUNT());
	            try
	            {
	                unitnetvalue = invoice.getDecimal(invoice.getLINEITEMNETAMOUNT());
	                
	                if (!netvalue.equals(unitnetvalue))
	                {
	                	invoice.getDialogErrorMsgs().add("Invoice Net Amount must equal Line Item Net Total");
	                	return;
	                }
	            } catch (Exception exc)
	            {
	            	invoice.getDialogErrorMsgs().add("Unit Net Amount is invalid");
	            	return;
	            }
	            HashMap<String, RegionalBigDecimal> vatPercentages= new HashMap<String, RegionalBigDecimal>();
		        List<FormSelect> vatCodes= new ArrayList<FormSelect>();
		        LineItemELS dummyLnItem=new LineItemELS( invoice.getCOUNTRY());
		        InvoiceLineItemsELS invoiceLineItem = new InvoiceLineItemsELS(invoice);
		        dummyLnItem.getLineItemDataModelELS().loadSelectVatCodes(vatPercentages, vatCodes, invoiceLineItem);
	            if(!invoice.isUSCountry() && vatamount.compareTo(RegionalBigDecimal.ZERO)== 0){
	            	ArrayList<LineItemELS> listLineItems  = invoice.getLineItems();
	            	for(LineItemELS lineItem: listLineItems){
	            		if(lineItem.getLineItemDataModelELS().getVATPercentage(vatPercentages).compareTo(RegionalBigDecimal.ZERO)>0){
	            			invoice.getDialogErrorMsgs().add("Tax Amount is 0.00 on the header screen you cannot select a Non 0% Tax Code on line item");
	                		return;
	            		}
	            	}
	            }
	            
	            RegionalBigDecimal vatVariance = RegionalBigDecimal.ZERO;
	            try
	            {
	                vatVariance = invoice.getDecimal(invoice.getVATVARIANCE());
	                RegionalBigDecimal vatDiff = unitvatvalue.subtract(vatamount).abs();
	                if (vatDiff.compareTo(vatVariance) > 0)
	                {
	                    // verify with the user that this is what they wanted to do
	                	invoice.getDialogErrorMsgs().add("Tax Amount must equal " + unitvatvalue.toString() + " +/- $" + vatVariance);
	                    return;
	                }
	            } catch (Exception exc)
	            {logger.fatal(exc.getMessage());
	            return;
	            }
	        }
	        
	        if(!indexDocument(invoice)){
	        	invoice.getDialogErrorMsgs().add("Document did not index correctly");
	        }
	        
	        
	        for (String error : invoice.getDialogErrorMsgs()) {
	        	logger.debug("Collected Errors: "+error);
				
			}
		
		logger.debug("End validateInput");
	}
	private boolean indexDocument(InvoiceELS invoice){
		// validate the document got indexed correctly
        boolean exists = false;
        try
        {
            DOCUMENT cmdoc = invoice.getInvoiceDataModelELS().queryDocument();
            if (cmdoc != null)
            {
                exists = true;
            }
        } catch (Exception exc)
        {
        	logger.error("Index document Exc: "+exc);
            exists = false;
        }
        
           return exists; 
	}
	private void recalculateNetAmount(InvoiceELS invoice) throws Exception {
		
		logger.debug("recalculateNetAmount method");

		//Story 1750051 CA GIL changes
		if(invoice.getCOUNTRY().equalsIgnoreCase("CA")) {
			invoice.getInvoiceDataModelELS().recalculateCANetAmount();
		} else {
			invoice.getInvoiceDataModelELS().recalculateNetAmount();
		}
		//End Story 1750051 CA GIL changes
		
		logger.debug("End recalculateNetAmount method");
		
		
	}
	private InvoiceELS initInvoice(HttpServletRequest request, PluginServiceCallbacks callbacks){
		JsonObject jsonDataObject= new JsonParser().parse(request.getParameter("jsonInvoiceEls")).getAsJsonObject();

		String repositoryId =request.getParameter("repositoryId");
		DKDatastoreICM datastore = callbacks.getCMDatastore(repositoryId);
		InvoiceELS invoice = (InvoiceELS)ServiceHelper.jsonToObject(jsonDataObject, InvoiceELS.class);
		invoice.setDatastore(datastore);
		invoice.getDialogWarningMsgs().clear();	
		invoice.getDialogErrorMsgs().clear();	
		invoice.getInvoiceDataModelELS();
		return invoice;
	}
	private void usTaxCalculation(InvoiceELS invoice) throws Exception {
		logger.debug("Enter usTaxCalculation method");
		
//		
		
		 boolean choice = false;
	        RegionalBigDecimal taxamount =GilUtility.getDecimal(invoice.getVATAMOUNT());
	        if (taxamount.compareTo(RegionalBigDecimal.ZERO) == 0){
	        	invoice.setUSTAXPERCENT(null);
	        	invoice.getInvoiceDataModelELS().recalculateNetAmount();
	        }else if(invoice.getTAXESINDICATOR().equals("N")){
	        	invoice.setDisplayTaxWarn("Y");

	        }else if(invoice.getTAXESINDICATOR().equals("Y")){
	    		invoice.getInvoiceDataModelELS().recalculateMultipleTaxNetAmount();
	    		
	        }
		
	        logger.debug("End usTaxCalculation method");
	}
	
	private void searchByOffering( InvoiceELS invoice) throws Exception{
		logger.debug("searchByOffering method");

		
		invoice.setCUSTOMERNUMBER("");
		invoice.setCUSTOMERNAME("");
		
		searchForOfferingLetter(invoice,false);
		invoiceForSWVinvoiceType(invoice);

		logger.debug("End searchByOffering method");
	}

	

	 /*
     * Setup the gui for indexing the document
     */
    private void initalizeIndexing(InvoiceELS invoice) throws Exception
    {

    	logger.debug("initalizeIndexing method");

     // invoice.loadIndexValues(); won't use since getting the index values from CN 
      invoice.initializeCurrencies();
      invoice.initializeDbCr();
      invoice.initializeCompanyCode();
      invoice.initializeInvoiceTypes();
      invoice.initializePOEXCodes();

//        initializeVATPercentages(); //check not doing something business logic only gui
       invoice.initializeVATVariance();          
       invoice.initializeELSCountry();        
       invoice.initializeDistributor();        

       initializeDatabaseValues(invoice);
       
       //Story 1750051 CA GIL changes
       if(invoice.getCOUNTRY().equalsIgnoreCase("CA")) {
    	   invoice.initializeShiptoProvinceCodes();
       }
       //End Story 1750051 CA GIL changes

       logger.debug("End initalizeIndexing method");
    }

    public void invoiceForSWVinvoiceType(InvoiceELS invoice){
        logger.debug("invoiceForSWVinvoiceType entry");
        if(invoice.isUSCountry() && invoice.getINVOICETYPE().equals("SWV")&&invoice.getDOCUMENTMODE() ==Indexing.ADDMODE){
        	String invoiceNumber = invoice.getINVOICENUMBER();
        	if(invoice.getOFFERINGLETTERNUMBER().trim().length()>0 && invoice.getCUSTOMERNAME().trim().length()>0){
        		invoiceNumber = invoice.getINVOICETYPE()+invoice.getOFFERINGLETTERNUMBER();
        	}else{
        		//invoiceNumber = "";
        	}
        	invoice.setINVOICENUMBER(invoiceNumber);
        	
        }
        logger.debug("invoiceForSWVinvoiceType exit");
    }
    /*
     * load the associated document data from the database if it exists
     */
    private void initializeDatabaseValues(InvoiceELS invoice) throws GILCNException
    {
//      InvoiceDataModel aDataModel = (InvoiceDataModel) getDataModel();

        // initialize the index values from the db
        try
        {

            // load the invoice if it exists
            ArrayList<String> supplierNums = new ArrayList<String>();
            InvoiceELS invoiceToGCMS=new InvoiceELS(invoice.getCOUNTRY(), invoice.getDatastore());
            //only setting important values for getInvoice ws
            
            invoiceToGCMS.setCOUNTRY(invoice.getCOUNTRY().equals("UK")?"GB":invoice.getCOUNTRY());
            invoiceToGCMS.setINVOICENUMBER(invoice.getINVOICENUMBER());
            invoiceToGCMS.setOBJECTID("");//Gil Client is setting as empty... maybe shouldn't be empty...
            invoiceToGCMS.generateUNIQUEREQUESTNUMBER();
            invoiceToGCMS.setGcmsEndpoint(invoice.getGcmsEndpoint());
            invoiceToGCMS.setRdcEndpoint(invoice.getGcmsEndpoint());
			
            ArrayList results = invoiceToGCMS.getInvoiceDataModelELS().selectMatchingInvoices(invoice.getOBJECTID());
            //check if this invoice is on APTS
            
           invoice.getInvoiceDataModelELS().validateAptsInvoice(results);
           
            logger.debug("Results SelectMatchingInvoices: "+results.size());
            // check for apts invoices
            InvoiceELS retrievedInvoice = null;
            for (int i = 0; i < results.size(); i++)
            {
                InvoiceELS anInvoice = (InvoiceELS) results.get(i);


                // is it a duplicate invoice number / date
                if ((anInvoice.getINVOICENUMBER().equals(invoice.getINVOICENUMBER())) && (anInvoice.getINVOICEDATE().equals(invoice.getINVOICEDATE())))
                {
                    // is it from apts
                    if (anInvoice.isFromAPTS())
                    {
                    	invoice.getDialogErrorMsgs().add("Invoice is from APTS and can't be indexed to GCMS");
                    	
                        return;
                    } 
                            
                   
                    if (!anInvoice.getOBJECTID().equals(invoice.getOBJECTID()))
                    {
                    	invoice.getDialogErrorMsgs().add("Duplicate Invoice Number / Invoice Date entered");
                    	invoice.setDisplayErrorDuplicateInvNum("Y");
//                        error("Duplicate Invoice Number / Invoice Date entered");
                    	
                    	logger.error("Duplicate Invoice Number / Invoice Date entered: "+invoice.getINVOICENUMBER());
                    	return;
                       
                    }
                }
                if (anInvoice.getOBJECTID().equals(invoice.getOBJECTID()))
                {	
                	logger.debug("Retrieved invoice success");
                    retrievedInvoice = anInvoice;
                }else{
                	supplierNums.add(anInvoice.getVENDORNUMBER());
                }

            }
            

            if (retrievedInvoice == null)
            {
            	invoice.setInvDialfieldsEditable(false);// disabling fields false
//                ((InvoiceFrame) getViewFrame()).setFieldsEditable(true);

                invoice.setDOCUMENTMODE(Indexing.ADDMODE);
                invoice.setDIRTYFLAG(Boolean.TRUE);
                invoice.setOLDINVOICENUMBER(invoice.getINVOICENUMBER());
                invoice.setOLDINVOICEDATE(invoice.getINVOICEDATE());
                invoice.setSupplierNums(supplierNums);
                initializeValues(invoice);

                
            } else
            {
                // store the new invoice number
                String newInvoiceNumber = invoice.getINVOICENUMBER();
                String oldInvoiceNumber = retrievedInvoice.getINVOICENUMBER();
                String newInvoiceDate = invoice.getINVOICEDATE();
                String oldInvoiceDate = retrievedInvoice.getINVOICEDATE();
                // oldInvoiceDate = RegionalDateConverter.convertDate("GUI",
                // "DB2", oldInvoiceDate);

                // preserve index fields
                String vendorName = invoice.getVENDORNAME();
                String vatVariance = invoice.getVATVARIANCE();
                retrievedInvoice.setUSERID(invoice.getUSERID());
                retrievedInvoice.setINDEXCLASS(invoice.getINDEXCLASS());
                retrievedInvoice.setOBJECTID(invoice.getOBJECTID());
                retrievedInvoice.setELSINDC(invoice.getELSINDC());

                setRetrievedValues(invoice,retrievedInvoice);
                
                invoice.getLineItems().addAll(retrievedInvoice.getLineItems());
                invoice.getComments().addAll(retrievedInvoice.getComments());
                invoice.setDOCUMENTMODE(Indexing.UPDATEMODE);
                invoice.setDIRTYFLAG(Boolean.FALSE);
                
                // update special fields
                invoice.setVATVARIANCE(vatVariance);
                invoice.setOLDINVOICENUMBER(oldInvoiceNumber);
                invoice.setOLDINVOICEDATE(oldInvoiceDate);
                invoice.setORIGIONALDBCR(invoice.getDBCR());
                
                invoice.setSupplierNums(supplierNums);

                // check for balances
                retrievedInvoice.getInvoiceDataModelELS().recalculateUnitAmounts();
                RegionalBigDecimal taxBalance = new RegionalBigDecimal(retrievedInvoice.getVATBALANCE());
                RegionalBigDecimal netBalance = new RegionalBigDecimal(retrievedInvoice.getNETBALANCE());
                if ((taxBalance.compareTo(RegionalBigDecimal.ZERO) == 0) && (netBalance.compareTo(RegionalBigDecimal.ZERO) == 0))
                {
                	invoice.setCONFIGUREDINVOICEINDC("Y");
                	invoice.getDialogWarningMsgs().add("All items on this invoice are currently configured in GCMS.\nNo changes to the invoice are allowed in GIL at this time.");
                	logger.warn("All items on this invoice are currently configured in GCMS.\nNo changes to the invoice are allowed in GIL at this time.");
                }

                // check to see if the vendor info has changed
                String vatRegNum = invoice.getVATREGISTRATIONNUMBER();
                boolean didSearch = false;
                if ((invoice.isCommissionOrStampDutyInvoice()) && ((!(vendorName.trim().equals(invoice.getVENDORNAME().trim())))))
                {
                	invoice.setDIRTYFLAG(Boolean.TRUE);
                	invoice.getDialogErrorMsgs().add("Vendor and Account cannot be changed for commision invoices");
                    logger.warn("Vendor and Account cannot be changed for commision invoices");
                    
                } else if (!(vendorName.trim().equals(invoice.getVENDORNAME().trim())) && (vendorName.trim().length() > 0))
                {
                    // launch the search window
                	invoice.setVENDORNAME(vendorName);
                	invoice.setDIRTYFLAG(Boolean.TRUE);
                    
                }

                // check to see if the invoice number has changed
                //instead need to get from invoice returned invoice.getNewInvoiceNumber
                if ((!newInvoiceNumber.trim().equals(oldInvoiceNumber.trim())) || (!newInvoiceDate.trim().equals(oldInvoiceDate.trim())))
                {	
                	logger.debug("newInvNum: "+newInvoiceNumber + "-oldInvNum: "+oldInvoiceNumber);
                	logger.debug("newInvDate: "+newInvoiceDate + "-oldInvDate: "+oldInvoiceDate);
                    
                	invoice.setDisplayChangeInvNum("Y");
//                        // yes selected

                }

               

                invoice.getInvoiceDataModelELS().recalculateTotalAmount();

                assignLineItemEventController();
                invoice.getInvoiceDataModelELS().recalculateUnitAmounts();

                
                // make sure you keep the same vat reg num if they pick the same
                // vendor
                if (retrievedInvoice.getVENDORNUMBER().equals(invoice.getVENDORNUMBER()))
                {
                	invoice.setVATREGISTRATIONNUMBER(vatRegNum);
                }

               
                if (invoice.getREFERENCEINVOICENUMBER().trim().length() > 0)
                {
                    displayReferenceInvoiceSearchWindow(invoice);
                }

//                setProgressComplete();

                searchForOfferingLetter(invoice,true);
                searchSupplier(invoice);
                // they can only enter a few fields on edit
                invoice.setInvDialfieldsEditable(true);//disabling true.

            }

//            return (true);

        } catch (Exception exc)
        {
        	logger.fatal("Error initializingDatabaseValues. "+exc);
        	
            logger.fatal(exc.toString());
            logger.error("Error initializing invoice");

//            return false;
        }
    }
    
    private void initializeValues(InvoiceELS invoice){
    	invoice.setEXCHANGERATE("1.0");
        invoice.setTOTALAMOUNT(RegionalBigDecimal.ZERO.setScale(RegionalBigDecimal.getDefaultScale()).toString());
        invoice.setVATAMOUNT(RegionalBigDecimal.ZERO.setScale(RegionalBigDecimal.getDefaultScale()).toString());
        invoice.setNETAMOUNT(RegionalBigDecimal.ZERO.setScale(RegionalBigDecimal.getDefaultScale()).toString());
        invoice.setVATBALANCE(RegionalBigDecimal.ZERO.setScale(RegionalBigDecimal.getDefaultScale()).toString());
        invoice.setNETBALANCE(RegionalBigDecimal.ZERO.setScale(RegionalBigDecimal.getDefaultScale()).toString());
        invoice.setLINEITEMTOTALAMOUNT(RegionalBigDecimal.ZERO.setScale(RegionalBigDecimal.getDefaultScale()).toString());
        invoice.setLINEITEMVATAMOUNT(RegionalBigDecimal.ZERO.setScale(RegionalBigDecimal.getDefaultScale()).toString());
        invoice.setLINEITEMNETAMOUNT(RegionalBigDecimal.ZERO.setScale(RegionalBigDecimal.getDefaultScale()).toString());
//        invoice.setVATVARIANCE(RegionalBigDecimal.ZERO.setScale(RegionalBigDecimal.getDefaultScale()).toString());
        invoice.setEXCEPTIONSINDICATOR("N");
        if(invoice.getCOUNTRY().equals("US")){
        	invoice.setTAXESBILLEDTOIGF(RegionalBigDecimal.ZERO.setScale(RegionalBigDecimal.getDefaultScale()).toString());
        	invoice.setTAXESBILLEDTOCUST(RegionalBigDecimal.ZERO.setScale(RegionalBigDecimal.getDefaultScale()).toString());
        }
        invoice.setCOAWITHINVOICEINDICATOR("N");
        invoice.setROFOFFERLETTERINDICATOR("N");
        invoice.setROFINVOICEINDC("N");
        invoice.setTOLERANCEINDC("Y");
        invoice.setDOCUMENTMODE(Indexing.ADDMODE);
        invoice.setDIRTYFLAG(Boolean.FALSE);
    }
    private void searchSupplier(InvoiceELS invoice){
    	logger.debug("getInvoice - vendorNum"+invoice.getVENDORNUMBER());
    	if(invoice.getVENDORNUMBER()!=null && !invoice.getVENDORNUMBER().equals("")){
	    	VendorSearchELS vendorSearchELS=new VendorSearchELS(invoice.getCOUNTRY());
	    	
	    	vendorSearchELS.setQUERYITEM(invoice.getVENDORNUMBER());
	    	
	    	try {
				List<VendorSearchELS>vendors=vendorSearchELS.selectSupplierNumberStatement();
				if(vendors!=null &&  vendors.size()==1){
					invoice.setVENDORADDRESS1(vendors.get(0).getSUPPLIERADDRESS());
					invoice.setVENDORADDRESS2(vendors.get(0).getSUPPLIERADDRESS2());
					invoice.setVENDORADDRESS3(vendors.get(0).getSUPPLIERADDRESS3());
					invoice.setSRENDDATE(vendors.get(0).getSRENDDATE());
					invoice.setVENDORCOMMISSION(vendors.get(0).getVENDORCOMMISSION());
				}
			} catch (Exception e) {
				logger.fatal("selectSupplierNumberStatement - invoice country: "+invoice.getCOUNTRY()+
						"supplr num: "+invoice.getVENDORNUMBER());
			}
    	}
    }
    private boolean offeringLetterValid = true;
    private void searchForOfferingLetter(InvoiceELS invoice, boolean keepUnits) throws CloneNotSupportedException
    {
    	
//        InvoiceDataModel invoice = (InvoiceDataModel) getDataModel();
    	LineItemELS aLineItemDataModel = null;
//
//        // make sure the line item frame is inited so that it has the tax codes
//        getLineItemFrame();
//
//        // clear any previous ol data
    	invoice.setOFFERINGLETTERCURRENCY(invoice.getCURRENCY());
    	invoice.setOFFERINGLETTERCUSTOMERNUMBER("");
    	invoice.setOFFERINGLETTERCUSTOMERNAME("");
    	invoice.setROFOFFERLETTERINDICATOR("N");
    	invoice.setRESPONSIBLEPARTYID("");
        
        if (invoice.getOFFERINGLETTERNUMBER().trim().length() == 0)
        {
            offeringLetterValid = true;
            return;
        }

        logger.info("Searching for offer number " + invoice.getOFFERINGLETTERNUMBER());

//        startProgressMonitor("Working", "Searching for offer number " + invoice.getOFFERINGLETTERNUMBER());
    	ArrayList olList = invoice.getInvoiceDataModelELS().selectByOfferingLetterStatement();
//        setProgressComplete();
//
        long invoiceDate = 0;
        long olValidEndDate = 0;
        long olValidStartDate = 0;

        if (olList.size() == 0)
        {

        	invoice.getDialogErrorMsgs().add("Offer Letter doesn't exist");
			logger.error("Offer Letter doesn't exist");
            offeringLetterValid = false;

        } else
        {
            OfferingLetterELS aOLDM = (OfferingLetterELS) olList.get(0);

            if (aOLDM.isDeactivated())
            {
            	invoice.getDialogErrorMsgs().add("Offer Letter is deactivated and cannot be used");
            	logger.error("Offer Letter is deactivated and cannot be used");
                invoice.setOFFERINGLETTERNUMBER("");
                offeringLetterValid = true;
            } else
            {

                offeringLetterValid = true;
                invoice.setOFFERINGLETTERNUMBER(aOLDM.getOFFERINGNUMBER());
                invoice.setOFFERINGLETTERCURRENCY(aOLDM.getCURRENCY());
                invoice.setOFFERINGLETTERCUSTOMERNUMBER(aOLDM.getCUSTOMERNUMBER());
                invoice.setOFFERINGLETTERCUSTOMERNAME(aOLDM.getCUSTOMERNAME());
                // story 1497875
                invoice.setBILLENTITYINDICATOR(aOLDM.getBILLENTITYINDICATOR());
             // story 1497875

                // copy in the customer name/number if blank
                if ((invoice.getCUSTOMERNUMBER().trim().length() == 0) && (invoice.getDOCUMENTMODE().equals(Indexing.ADDMODE)))
                {
                	invoice.setCUSTOMERNAME(aOLDM.getCUSTOMERNAME());
                	invoice.setCUSTOMERNUMBER(aOLDM.getCUSTOMERNUMBER());
                }

                invoice.setROFOFFERLETTERINDICATOR(aOLDM.getROFOFFERLETTERINDICATOR());
                invoice.setRESPONSIBLEPARTYID(aOLDM.getRESPONSIBLEPARTYID());
                
                //[start] story 1612235 - Fix customer information for EDI invoices
                
//                startProgressMonitor("Working", "Searching for customer number " + aOLDM.getCUSTOMERNUMBER());
                String errors="";
                ArrayList aInvoiceList = new ArrayList();
                aInvoiceList=invoice.getInvoiceDataModelELS().selectByCustomerStatement(aOLDM.getCUSTOMERNUMBER(),errors);
                if(!errors.equals(""))invoice.getDialogErrorMsgs().add(errors);
//                setProgressComplete();
                InvoiceELS returnDM = null;
                logger.debug("results SelectByCustomerStatement: "+aInvoiceList.size());
                if (aInvoiceList.size() > 0)
                {
                	
                	returnDM = (InvoiceELS) aInvoiceList.get(0);
                     logger.debug("returned customer");
                    invoice.setCUSTOMERADDRESS1(returnDM.getCUSTOMERADDRESS1());
                    invoice.setCUSTOMERADDRESS2(returnDM.getCUSTOMERADDRESS2());
                    invoice.setCUSTOMERADDRESS3(returnDM.getCUSTOMERADDRESS3());
                    invoice.setCUSTOMERADDRESS4(returnDM.getCUSTOMERADDRESS4());
                    invoice.setCUSTOMERADDRESS5(returnDM.getCUSTOMERADDRESS5());
                    logger.debug("Setting customer addresses done");
                }else{
                	logger.error("Customer list empty, then error validating offering letter.");
                	invoice.getDialogErrorMsgs().add("Error validating Offering Letter.");
                	return;
                }
                
                // As agreed with csophia@br.ibm.com when invoice customer number differs from offer letter customer number
                // the invoice must be set with offer letter customer number
                logger.debug("invoice cust num: "+invoice.getCUSTOMERNUMBER());
                logger.debug("returned invoice cust num: "+returnDM.getCUSTOMERNUMBER());
                
                if (!invoice.getCUSTOMERNUMBER().equalsIgnoreCase(returnDM.getCUSTOMERNUMBER())) {
                	
                	invoice.setCUSTOMERNUMBER(returnDM.getCUSTOMERNUMBER());
                }
                
                //[end] story 1612235
                
                

                // copy in the units for a non rollup ol
                aOLDM.getoLDataModel().recalculateQuoteLineItems(invoice);
                ArrayList quoteLineItems = aOLDM.getQuoteLineItems();
                ArrayList unitLineItems = invoice.getLineItems();

                if (!keepUnits)
                {
                    invoice.getInvoiceDataModelELS().removeQuoteLineItems();

                    // add the new quote units
                    // check for not roll-up and not credit and not comm and not
                    // rbd
                    int qty = 0;
                    if ((!aOLDM.isRollup()) && (invoice.isDBInvoice()) && (!invoice.isCommissionOrStampDutyInvoice()) && (!invoice.isRateBuyDownInvoice()) && (!aOLDM.isRateCard()))
                    {	LineItemELS dummy = new LineItemELS(invoice.getCOUNTRY());
                    	HashMap<String, String> otherChargesMap=dummy.getLineItemDataModelELS().loadOtherChargesOLItem();
                        HashMap<String, RegionalBigDecimal> vatPercentages= new HashMap<String, RegionalBigDecimal>();
            	        List<FormSelect> vatCodes= new ArrayList<FormSelect>();
            	        InvoiceLineItemsELS invoiceLineItem = new InvoiceLineItemsELS(invoice);
            	        dummy.getLineItemDataModelELS().loadSelectVatCodes(vatPercentages, vatCodes, invoiceLineItem);
            	        
                        for (int i = 0; i < quoteLineItems.size(); i++)
                        {
                            aLineItemDataModel = (LineItemELS) quoteLineItems.get(i);
                            if (!aLineItemDataModel.isBLEX())
                            {
                                qty = aLineItemDataModel.getQUANTITYasInteger().intValue();
                                aLineItemDataModel.setCOUNTRY(invoice.getCOUNTRY());
                                aLineItemDataModel.getLineItemDataModelELS().determineINVOICEITEMTYPE(otherChargesMap);
                                aLineItemDataModel.setQUANTITY("1");
                                aLineItemDataModel.setPONUMBER(invoice.getPURCHASEORDERNUMBER());
                                aLineItemDataModel.setCOSTCENTER(invoice.getCOSTCENTER());
                                RegionalBigDecimal taxamount =GilUtility.getDecimal(invoice.getVATAMOUNT());
                                if (taxamount.compareTo(RegionalBigDecimal.ZERO) == 0){
                    	        	invoice.setUSTAXPERCENT("0");
//                    	        	invoice.getInvoiceDataModelELS().recalculateNetAmount();
                    	        }
                                if(invoice.isUSCountry()){
                                	aLineItemDataModel.setUSTAXPERCENT(invoice.getUSTAXPERCENT());
                                     aLineItemDataModel.setBILLEDTOIGFINDC("N");
                                }
                                aLineItemDataModel.getLineItemDataModelELS().recalculateVATAmount(vatPercentages);
                                for (int j = 0; j < qty; j++)
                                {
//                                    try {

                                	
										unitLineItems.add(aLineItemDataModel);
//									} catch (CloneNotSupportedException e) {
//										logger.error(e.getMessage());
//										throw e;
//										
//									}
                                }
                            }
                        }
//                        
                        logger.debug("Line items Logic");
                        LineItemELSServiceHelper.renumberLineItems(unitLineItems);
                        LineItemELSServiceHelper.spreadTaxRoundingError(invoiceLineItem,unitLineItems);
                        
                        logger.debug("Done Line items Logic");
                    }
                    invoice.getInvoiceDataModelELS().recalculateUnitAmounts();

                }
                invoice.setOFFERINGLETTERCURRENCY(aOLDM.getCURRENCY());
            }
        }
//        getViewPanel().toGUI(aDataModel);
    }
    public void assignLineItemEventController()
    {
//    	TODO not sure if this is useful now....I think nope
//        InvoiceDataModel aDataModel = (InvoiceDataModel) getDataModel();
//        LineItemDataModel lineItemDataModel = null;
//        ArrayList details = invoice.getLineItems();
//        LineItemEventController lineItemEventController = getLineItemEventController();
//        for (int i = 0; i < details.size(); i++)
//        {
//            lineItemDataModel = (LineItemDataModel) details.get(i);
//            lineItemDataModel.setEventController(lineItemEventController);
//        }
    }

    
    private void saveState( InvoiceELS invoice){
    	logger.debug("Enter saveState method");
    	
    	logger.debug("State code: "+invoice.getSTATECODE());
    	if(invoice.getTAXESINDICATOR().equals("Y")){
    		processIGFandCustomerTaxes(invoice);
    	}else{
    		processUSStateTaxAmount(invoice);
    	}
    	logger.debug("End saveState method");
    	
    }
    private boolean displayReferenceInvoiceSearchWindow(InvoiceELS invoice)
    {
    	boolean result =false;
        ArrayList results;
        invoice.setDisplayRefInvSearch("N");
       invoice.setREFERENCEINVOICENETAMOUNT(RegionalBigDecimal.ZERO.setScale(RegionalBigDecimal.getDefaultScale()).toString());
       invoice.setREFERENCEINVOICEVATAMOUNT(RegionalBigDecimal.ZERO.setScale(RegionalBigDecimal.getDefaultScale()).toString());

//        isReferenceInvoiceSearchWindowActive = true;
       invoice.getRefListData().clear();
       invoice.getRefDataValues().clear();
        try
        {
            if (invoice.isCRInvoice() && invoice.getREFERENCEINVOICENUMBER().trim().length() > 0)
            {
                InvoiceELS aIDM = null;
                results = invoice.getInvoiceDataModelELS().selectByReferenceInvoiceStatement();
                logger.debug("Results SelectByReferenceInvoiceStatement: "+results.size());
                if (results.size() == 0)
                {
                	invoice.getDialogWarningMsgs().add("Reference Invoice doesn't exist.");
                    logger.warn("Reference Invoice doesn't exist.");
//                    isReferenceInvoiceSearchWindowActive = false;
                    return false;
                } 
                else if (results.size() > 1)
                {
                	// make user pick the reference invoice
                    ArrayList<FormSelect> codes = new ArrayList<FormSelect>();
                    FormSelect code = null;
                    for (int i = 0; i < results.size(); i++)
                    {
                        aIDM = (InvoiceELS) results.get(i);
                        
                        String key="";
                        if (!aIDM.isCRInvoice())
                        {
                        	key=aIDM.getINVOICENUMBER() + "  -  " + aIDM.getINVOICEDATE();
                            logger.error("Reference Invoice is a debit Invioce");
                            code=new FormSelect();
                	    	code.setValue(key);
                	    	code.setLabel(key);
                	    	if(i==0)
                	    		code.setSelected(true);
                	    	
                            codes.add(code);
                            invoice.getRefDataValues().put(key,
                            		aIDM.getNETAMOUNT()+"#"+aIDM.getVATAMOUNT()+"#"+aIDM.getINVOICEDATE());
                            
//                            requestFieldFocus(aDataModel.getREFERENCEINVOICENUMBERidx());
//                            isReferenceInvoiceSearchWindowActive = false;
                            
                        }
            	    	
                    }
                    if(!codes.isEmpty()){
                    	invoice.getRefListData().addAll(codes);                  
                        invoice.setDisplayRefInvSearch("Y");
                        return true;
                        
                    }
                    return false;
                }else if(results.size()==1){
                	 aIDM = (InvoiceELS) results.get(0);
                	 if (aIDM.isCRInvoice())
                     {		
                		 invoice.getDialogErrorMsgs().add("Reference Invoice must be a debit Invioce");
                         logger.error("Reference Invoice must be a debit Invioce");
//                         requestFieldFocus(aDataModel.getREFERENCEINVOICENUMBERidx());
//                         isReferenceInvoiceSearchWindowActive = false;
                         return false;
                     }
                	// setup the reference invoice total and balance
                     invoice.setREFERENCEINVOICENETAMOUNT(aIDM.getNETAMOUNT());
                     invoice.setREFERENCEINVOICEVATAMOUNT(aIDM.getVATAMOUNT());
                     invoice.setREFERENCEINVOICEDATE(aIDM.getINVOICEDATE());
                     return true;
                }
             
         
                        
            }

        } catch (Exception e)
        {
           logger.error("Error validating Reference Invoice Number");
           invoice.getDialogErrorMsgs().add("Error validating Reference Invoice Number");
//            requestFieldFocus(aDataModel.getREFERENCEINVOICENUMBERidx());
//            isReferenceInvoiceSearchWindowActive = false;
            return false;
        }
//        isReferenceInvoiceSearchWindowActive = false;
       return true;
    }
    public void setRetrievedValues(InvoiceELS invoice,InvoiceELS invoiceToCopy){
    	
    	invoice.setADDMODE(invoiceToCopy.getADDMODE());
    	invoice.setAptsregion(invoiceToCopy.getAptsregion());
    	invoice.setAUTOCREATECOAINDC(invoiceToCopy.getAUTOCREATECOAINDC());
    	invoice.setBILLENTITYINDICATOR(invoiceToCopy.getBILLENTITYINDICATOR());
    	invoice.setBLOCKINDC(invoiceToCopy.getBLOCKINDC());
    	invoice.setCOAWITHINVOICEINDICATOR(invoiceToCopy.getCOAWITHINVOICEINDICATOR());
//    	invoice.setComments(invoiceToCopy.getComments());
    	invoice.setCOMPANYCODE(invoiceToCopy.getCOMPANYCODE());
    	invoice.setCONFIGUREDINVOICEINDC(invoiceToCopy.getCONFIGUREDINVOICEINDC());
    	invoice.setCOSTCENTER(invoiceToCopy.getCOSTCENTER());
    	invoice.setCOUNTRY(invoiceToCopy.getCOUNTRY());
//    	invoice.setCreatedDate(invoiceToCopy.getCreatedDate());
    	invoice.setCURRENCY(invoiceToCopy.getCURRENCY());
    	invoice.setCUSTOMERADDRESS1(invoiceToCopy.getCUSTOMERADDRESS1());
    	invoice.setCUSTOMERADDRESS2(invoiceToCopy.getCUSTOMERADDRESS2());
    	invoice.setCUSTOMERADDRESS3(invoiceToCopy.getCUSTOMERADDRESS3());
    	invoice.setCUSTOMERADDRESS4(invoiceToCopy.getCUSTOMERADDRESS4());
    	invoice.setCUSTOMERADDRESS5(invoiceToCopy.getCUSTOMERADDRESS5());
    	invoice.setCUSTOMERADDRESS6(invoiceToCopy.getCUSTOMERADDRESS6());
    	invoice.setCUSTOMERNAME(invoiceToCopy.getCUSTOMERNAME());
    	invoice.setCUSTOMERNUMBER(invoiceToCopy.getCUSTOMERNUMBER());
    	invoice.setDb2TransactionFailed(invoiceToCopy.isDb2TransactionFailed());
    	invoice.setDBCR(invoiceToCopy.getDBCR());
//    	invoice.setDIRTYFLAG(invoiceToCopy.isDIRTYFLAG()); need to check condition
    	invoice.setDISTRIBUTORNUMBER(invoiceToCopy.getDISTRIBUTORNUMBER());
//    	invoice.setDOCUMENTMODE(invoiceToCopy.getDOCUMENTMODE());
    	invoice.setELSINDC(invoiceToCopy.getELSINDC());
    	invoice.setErrorCodes(invoiceToCopy.getErrorCodes());
    	invoice.setEXCEPTIONSINDICATOR(invoiceToCopy.getEXCEPTIONSINDICATOR());
    	invoice.setEXCHANGERATE(invoiceToCopy.getEXCHANGERATE());
    	invoice.setIGFBILLEDTOAMOUNT(invoiceToCopy.getIGFBILLEDTOAMOUNT());
    	invoice.setINDEXCLASS(invoiceToCopy.getINDEXCLASS());
    	invoice.setINSTALLEDCUSTOMERADDRESS1(invoiceToCopy.getINSTALLEDCUSTOMERADDRESS1());
    	invoice.setINSTALLEDCUSTOMERADDRESS2(invoiceToCopy.getINSTALLEDCUSTOMERADDRESS2());
    	invoice.setINSTALLEDCUSTOMERADDRESS3(invoiceToCopy.getINSTALLEDCUSTOMERADDRESS3());
    	invoice.setINSTALLEDCUSTOMERADDRESS4(invoiceToCopy.getINSTALLEDCUSTOMERADDRESS4());
    	invoice.setINSTALLEDCUSTOMERADDRESS5(invoiceToCopy.getINSTALLEDCUSTOMERADDRESS5());
    	invoice.setINSTALLEDCUSTOMERADDRESS6(invoiceToCopy.getINSTALLEDCUSTOMERADDRESS6());
    	invoice.setINSTALLEDCUSTOMERNAME(invoiceToCopy.getINSTALLEDCUSTOMERNAME());
    	invoice.setINSTALLEDCUSTOMERNUMBER(invoiceToCopy.getINSTALLEDCUSTOMERNUMBER());
    	//need to ask in front end is user is sure to change name
//    	setINVOICEDATE(invoiceToCopy.getINVOICEDATE());
    	logger.debug("invoice to copy date: "+invoiceToCopy.getINVOICEDATE());
//
    	invoice.setINVOICESTATUS(invoiceToCopy.getINVOICESTATUS());
    	invoice.setINVOICETYPE(invoiceToCopy.getINVOICETYPE());
    	invoice.setLINEITEMNETAMOUNT(invoiceToCopy.getLINEITEMNETAMOUNT());
//    	invoice.setLineItems(invoiceToCopy.getLineItems());
    	invoice.setLINEITEMTOTALAMOUNT(invoiceToCopy.getLINEITEMTOTALAMOUNT());
    	invoice.setLINEITEMVATAMOUNT(invoiceToCopy.getLINEITEMVATAMOUNT());
//    	setLocale(invoiceToCopy.getLocale());
    	invoice.setMASSUPDATEMODE(invoiceToCopy.getMASSUPDATEMODE());
    	invoice.setNETAMOUNT(invoiceToCopy.getNETAMOUNT());
    	invoice.setNETBALANCE(invoiceToCopy.getNETBALANCE());
    	invoice.setOBJECTID(invoiceToCopy.getOBJECTID());
    	invoice.setOFFERINGLETTERCURRENCY(invoiceToCopy.getOFFERINGLETTERCURRENCY());
    	invoice.setOFFERINGLETTERCUSTOMERNAME(invoiceToCopy.getOFFERINGLETTERCUSTOMERNAME());
    	invoice.setOFFERINGLETTERCUSTOMERNUMBER(invoiceToCopy.getOFFERINGLETTERCUSTOMERNUMBER());
    	invoice.setOFFERINGLETTERNUMBER(invoiceToCopy.getOFFERINGLETTERNUMBER());
    	invoice.setOLDINVOICEDATE(invoiceToCopy.getOLDINVOICEDATE());
    	invoice.setOLDINVOICENUMBER(invoiceToCopy.getOLDINVOICENUMBER());
    	invoice.setORIGIONALDBCR(invoiceToCopy.getORIGIONALDBCR());
    	invoice.setOTAMOUNT(invoiceToCopy.getOTAMOUNT());
    	invoice.setPOEXCODE(invoiceToCopy.getPOEXCODE());
    	invoice.setPURCHASEORDERNUMBER(invoiceToCopy.getPURCHASEORDERNUMBER());
    	invoice.setREFERENCEINVOICEDATE(invoiceToCopy.getREFERENCEINVOICEDATE());
    	invoice.setREFERENCEINVOICENETAMOUNT(invoiceToCopy.getREFERENCEINVOICENETAMOUNT());
    	invoice.setREFERENCEINVOICENUMBER(invoiceToCopy.getREFERENCEINVOICENUMBER());
    	invoice.setREFERENCEINVOICEVATAMOUNT(invoiceToCopy.getREFERENCEINVOICEVATAMOUNT());
    	invoice.setREMOVEMODE(invoiceToCopy.getREMOVEMODE());
    	invoice.setREQUIREDFIELDS(invoiceToCopy.getREQUIREDFIELDS());
    	invoice.setRESPONSIBLEPARTYID(invoiceToCopy.getRESPONSIBLEPARTYID());
    	invoice.setROFINVOICEINDC(invoiceToCopy.getROFINVOICEINDC());
    	invoice.setROFINVOICESOURCE(invoiceToCopy.getROFINVOICESOURCE());
    	invoice.setROFOFFERLETTERINDICATOR(invoiceToCopy.getROFOFFERLETTERINDICATOR());
    	invoice.setServerTimezone(invoiceToCopy.getServerTimezone());
    	invoice.setSOURCE(invoiceToCopy.getSOURCE());
    	invoice.setSRENDDATE(invoiceToCopy.getSRENDDATE());
    	invoice.setSRNUMBER(invoiceToCopy.getSRNUMBER());
    	invoice.setSRSTARTDATE(invoiceToCopy.getSRSTARTDATE());
    	invoice.setSTATECODE(invoiceToCopy.getSTATECODE());
    	invoice.setSupplierNums(invoiceToCopy.getSupplierNums());
    	invoice.setTAXESBILLEDTOCUST(invoiceToCopy.getTAXESBILLEDTOCUST());
    	invoice.setTAXESBILLEDTOIGF(invoiceToCopy.getTAXESBILLEDTOIGF());
    	invoice.setTAXESINDICATOR(invoiceToCopy.getTAXESINDICATOR());
    	invoice.setTIMESTAMP(invoiceToCopy.getTIMESTAMP());
    	invoice.setTOBEINDEXEDWORKFLOW(invoiceToCopy.getTOBEINDEXEDWORKFLOW());
    	invoice.setTOLERANCEINDC(invoiceToCopy.getTOLERANCEINDC());
    	invoice.setTOTALAMOUNT(invoiceToCopy.getTOTALAMOUNT());
    	invoice.setUNCHANGEDMODE(invoiceToCopy.getUNCHANGEDMODE());
    	invoice.setUNIQUEREQUESTNUMBER(invoiceToCopy.getUNIQUEREQUESTNUMBER());
    	invoice.setUPDATEMODE(invoiceToCopy.getUPDATEMODE());
    	invoice.setURGENTINDICATOR(invoiceToCopy.getURGENTINDICATOR());
    	invoice.setUSERID(invoiceToCopy.getUSERID());
    	invoice.setUSTAXPERCENT(invoiceToCopy.getUSTAXPERCENT());
//    	setValues(invoiceToCopy.getValues());
    	invoice.setVATAMOUNT(invoiceToCopy.getVATAMOUNT());
    	invoice.setVATBALANCE(invoiceToCopy.getVATBALANCE());
    	invoice.setVATREGISTRATIONNUMBER(invoiceToCopy.getVATREGISTRATIONNUMBER());
    	invoice.setVATREGISTRATIONNUMBERREQUIRED(invoiceToCopy.getVATREGISTRATIONNUMBERREQUIRED());
//    	invoice.setVATVARIANCE(invoiceToCopy.getVATVARIANCE()); this one is set by invoiceEls.initializeVATVariance Method
    	invoice.setVENDORADDRESS1(invoiceToCopy.getVENDORADDRESS1());
    	invoice.setVENDORADDRESS2(invoiceToCopy.getVENDORADDRESS2());
    	invoice.setVENDORADDRESS3(invoiceToCopy.getVENDORADDRESS3());
    	invoice.setVENDORCOMMISSION(invoiceToCopy.getVENDORCOMMISSION());
    	invoice.setVENDORNAME(invoiceToCopy.getVENDORNAME());
    	invoice.setVENDORNUMBER(invoiceToCopy.getVENDORNUMBER());
    	//Story 1750051 CA GIL changes
    	invoice.setPROVINCECODE((invoiceToCopy.getPROVINCECODE()));
    	//End Story 1750051 CA GIL changes
    	
    	
    }

    public void updateLineItemsWithPOandCC (InvoiceELS invoice)
    {
        
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
    
    public void updateLineItemsWithEquipSource(InvoiceELS invoice){
       
        LineItemELS aLineItemDataModel = null;

        // move the data into the details gui
        ArrayList details = invoice.getLineItems();
        
        // setup the equipment source
        for (int i=0; i<details.size(); i++)
        {
            aLineItemDataModel = (LineItemELS)details.get(i);
            if(invoice.getELSINDCasBoolean()){
            	if(invoice.getINVOICETYPE().equals("SAL")){
            		aLineItemDataModel.setEQUIPSOURCE("CU");
            	}else if(invoice.getINVOICETYPE().equals("IBM") || invoice.getINVOICETYPE().equals("PTR") || invoice.getINVOICETYPE().equals("LOA") || invoice.getINVOICETYPE().equals("SWV")){
            		aLineItemDataModel.setEQUIPSOURCE("IB");
            	}else if(invoice.getINVOICETYPE().equals("VEN")){
            		aLineItemDataModel.setEQUIPSOURCE("BP");
            	}else{
            		aLineItemDataModel.setEQUIPSOURCE("");
            	}
            }else{
            	aLineItemDataModel.setEQUIPSOURCE("");
            }
        } 
    }
    
	
    /**
     * Validate the Tax Billed to IGF amount
     * @return
     */
    private boolean validateIGFAmount(InvoiceELS invoice){
//        InvoiceDataModel aDataModel = (InvoiceDataModel) getDataModel();
		invoice.getInvoiceDataModelELS().calculateTotalIGFLineItemAmount();
		RegionalBigDecimal totalIGFLineItemAmount = invoice.getDecimal(invoice.getIGFBILLEDTOAMOUNT());
		if(totalIGFLineItemAmount.compareTo(RegionalBigDecimal.ZERO) <= 0){
			invoice.getDialogErrorMsgs().add("No Assets added for Billed To IGF in the Line Item Screen");
			return false;
		}
		RegionalBigDecimal totalIGFTaxesAmount = invoice.getDecimal(invoice.getTAXESBILLEDTOIGF());
		if(totalIGFTaxesAmount.compareTo(RegionalBigDecimal.ZERO) <= 0){
			invoice.getDialogErrorMsgs().add("Tax Billed To IGF must be > 0");
			return false;
		} 
		return true;
    }
    
    /**
     * Process both IGF and Customer taxes
     */
    private void processIGFandCustomerTaxes(InvoiceELS invoice){
        
        //Story 1289894 - GIL - change to US tax call to CTS  for IL - added second if condition.
        if(invoice.isUpfrontState() && validateIGFAmount(invoice)) {
        	if(!(invoice.isStateIL() && invoice.getINVOICETYPE().equalsIgnoreCase("IBM"))){
    		
//            startProgressMonitor("Common Tax Service Request", "Retrieving tax information. Please wait.");
            boolean success = invoice.getInvoiceDataModelELS().selectByCTSTaxesStatement(); 
            if(!success){
//                setProgressComplete();
            	logger.debug("WSDL Location Not Found");
            }
            RegionalBigDecimal totalCustomerTaxesAmount = invoice.getDecimal(invoice.getTAXESBILLEDTOCUST());
            if(totalCustomerTaxesAmount.compareTo(RegionalBigDecimal.ZERO) > 0){
            	addCustomer9TX8TAXLineItem(invoice);
            }
            invoice.getInvoiceDataModelELS().recalculateMultipleTaxNetAmount();
//           	getViewPanel().toGUI(aDataModel);
        	} else if(invoice.isStateIL() && invoice.getINVOICETYPE().equalsIgnoreCase("IBM")){
        		//TIR USRO-R-LFAR-9USLY3 - Not spreading tax on type/model
        		invoice.getInvoiceDataModelELS().calIGFLineItemTaxAmtILIBM();
        		
        		RegionalBigDecimal totalCustomerTaxesAmount = invoice.getDecimal(invoice.getTAXESBILLEDTOCUST());
        		// TIR USRO-R-LFAR-9UVQDZ - Multiple Sold to's on IBM invoice not managing tax correctly
                if(totalCustomerTaxesAmount.compareTo(RegionalBigDecimal.ZERO) > 0){
                	addCustomer9TX8TAXLineItem(invoice);
                }
        		invoice.getInvoiceDataModelELS().recalculateMultipleTaxNetAmount();
//        		getViewPanel().toGUI(aDataModel);
        	}
        }else if(!invoice.isUpfrontState()){
        	RegionalBigDecimal totalIGFTaxesAmount = invoice.getDecimal(invoice.getTAXESBILLEDTOIGF());
    		if(totalIGFTaxesAmount.compareTo(RegionalBigDecimal.ZERO) > 0){
    			invoice.getDialogErrorMsgs().add("there should be no taxes on IGF billed assets for these states and should not accept.");
     		}
            RegionalBigDecimal totalCustomerTaxesAmount = invoice.getDecimal(invoice.getTAXESBILLEDTOCUST());
            if(totalCustomerTaxesAmount.compareTo(RegionalBigDecimal.ZERO) > 0){
            	addCustomer9TX8TAXLineItem(invoice);
            }
         }

    	
    	
//    	isUSStateWindowActive = false;     	
    }

    /**
     * Add 9TX8/TAX Type Model for Tax Billed To Customer Amount
     */
    private void addCustomer9TX8TAXLineItem(InvoiceELS invoice){
       
//        LineItemEventController aLineItemEventController = getLineItemEventController();
        if (!invoice.getInvoiceDataModelELS().updateCustomer9TX8TAXLineItem()){
        	LineItemELS addNew = new LineItemELS(invoice.getCOUNTRY());//(LineItemEls)aLineItemEventController.getDataModel();
        	addNew.init();
        	addNew.setTYPE("9TX8");
        	addNew.setMODEL("TAX");
        	addNew.setINVOICEITEMTYPE("TM");
        	addNew.getLineItemDataModelELS().selectTypeModelDescription(invoice.getCOUNTRY());
        	addNew.setUNITPRICE(invoice.getTAXESBILLEDTOCUST());
        	addNew.setEXTENDEDPRICE(invoice.getTAXESBILLEDTOCUST());
        	/**Changing I2 TO I0==NO_TAX as Lisa asked in showcase. March 5th, 2018***************************/
        	addNew.setVATCODE(CNUtilConstants.NO_TAX);
        	addNew.setCOUNTRY(invoice.getCOUNTRY());
        	invoice.getLineItems().add(addNew);
 	 
        }
        LineItemELSServiceHelper.renumberLineItems(invoice.getLineItems());
        invoice.getInvoiceDataModelELS().recalculateUnitAmounts();
        invoice.getInvoiceDataModelELS().recalculateMultipleTaxNetAmount();
//        getViewPanel().toGUI(aDataModel);        
    } 
    
    
    /**
     * Process US State Invoice Tax Amount
     */
    private void processUSStateTaxAmount(InvoiceELS invoice){
    	logger.debug("Enter processUSStateTaxAmount method");
        //Story 1289894 - GIL - change to US tax call to CTS  for IL - added second if condition.
        if(invoice.isUpfrontState() && !(invoice.isStateIL() && invoice.getINVOICETYPE().equalsIgnoreCase("IBM"))){
        	processUpfrontState(invoice);
        }else{
        	processNonUpfrontState(invoice);
        	
        }
        logger.debug("End processUSStateTaxAmount method");
    }
    /**
     * Process Non Upfront State Invoice Tax Amount
     */
    private void  processNonUpfrontState(InvoiceELS invoice){
    	logger.debug("Enter processNonUpfrontState method");
    	
      //Story 1289894 - GIL - change to US tax call to CTS  for IL.
      	if( invoice.isStateIL() && invoice.getINVOICETYPE().equalsIgnoreCase("IBM")){
      	//processUSLineitems();
      		invoice.getInvoiceDataModelELS().recalculateNetAmount();
//           	getViewPanel().toGUI(aDataModel);
          //TIR USRO-R-LFAR-9USLY3 - Not spreading tax on type/model
      		invoice.getInvoiceDataModelELS().calLineItemTaxAmtILIBM();
      		
      	}else{
      		 invoice.setUSTAXPERCENT(null);
           	invoice.getInvoiceDataModelELS().recalculateOtherStateNetAmount();
           	add9TX8TAXLineItem(invoice);
      	}
    	
//        getViewPanel().toGUI(aDataModel);
    	
    	
//    	isUSStateWindowActive = false;   
    	logger.debug("End processNonUpfrontState method");
    }
    
    /**Add 9TX8/TAX Type Model for invoice tax amount
     * 
     */
    private void add9TX8TAXLineItem(InvoiceELS invoice){
       
//        LineItemEventController aLineItemEventController = getLineItemEventController();
        if (!invoice.getInvoiceDataModelELS().update9TX8TAXLineItem()){
        	LineItemELS addNew =new LineItemELS(invoice.getCOUNTRY()); // (LineItemELS)aLineItemEventController.getDataModel();
        	addNew.init();
        	addNew.setTYPE("9TX8");
        	addNew.setMODEL("TAX");
        	addNew.setINVOICEITEMTYPE("TM");
        	addNew.getLineItemDataModelELS().selectTypeModelDescription(invoice.getCOUNTRY());
        	addNew.setUNITPRICE(invoice.getOTAMOUNT());
        	addNew.setEXTENDEDPRICE(invoice.getOTAMOUNT());
        	/**Changing I2 TO I0 as Lisa asked in showcase. March 5th, 2018***************************/
        	addNew.setVATCODE(CNUtilConstants.NO_TAX);
        	addNew.setCOUNTRY(invoice.getCOUNTRY());
        	invoice.getLineItems().add(addNew);
         }
        LineItemELSServiceHelper.renumberLineItems(invoice.getLineItems());
        invoice.getInvoiceDataModelELS().recalculateUnitAmounts();
//        getViewPanel().toGUI(aDataModel);
    }
    private void rollbackIndexing(InvoiceELS invoice){
    	invoice.getInvoiceDataModelELS().rollbackIndexing();
    }
    
    /**
     * Process Upfront State Invoice Tax Amount
     */
    private void processUpfrontState(InvoiceELS invoice){
    	
        
        invoice.getInvoiceDataModelELS().recalculateNetAmount();
        boolean success = invoice.getInvoiceDataModelELS().selectByCTSTaxesStatement(); 
        if(!success){
           
        	logger.debug("WSDL Location Not Found");
        }
         
    }

	@Override
	protected Indexing initializeVariables(PluginServiceCallbacks callbacks, JSONResponse jsonResults,
			HttpServletRequest request) throws Exception {
		logger.debug("initializeVariables() ");
		String optionToExecute = request.getParameter("request");
		InvoiceELS invoice =null;
		if(optionToExecute.equals(UtilGilConstants.INDEX)){
			String businessAttributes=	null;
			String country=null;	
			String repositoryId =null;		
			String indexClass=null;	
			String objectId=null;		
			DKDatastoreICM datastore=null;
	
			businessAttributes=request.getParameter("businessAttributes");
			country	=request.getParameter("country");
			repositoryId =request.getParameter("repositoryId");
			optionToExecute=request.getParameter("request");
			indexClass=request.getParameter("className");
			objectId=request.getParameter("itemId");
			datastore = callbacks.getCMDatastore(repositoryId);
		
		
			JSONObject jsonObjectBA=(JSONObject)JSON.parse(businessAttributes);
			//Getting values needed for backend request service
			String invoiceNum=	(String)jsonObjectBA.get("invNum");		
			String invoiceDate=(String)jsonObjectBA.get("invDate");
			logger.debug("from CN Invoice date: "+invoiceDate);		
			String source=		(String)jsonObjectBA.get("source");		
			String userId=		(String)jsonObjectBA.get("userId");
			String timeStamp=	(String)jsonObjectBA.get("createdTimestamp");
			String businessPartner=(String)jsonObjectBA.get("businessPartner");
	
			invoice=new InvoiceELS(country,datastore);
			invoice.setLocale(callbacks.getLocale());			
			invoice.setINVOICENUMBER(invoiceNum);		
			invoice.setOBJECTID(objectId);
	//		invoice.generateUNIQUEREQUESTNUMBER();
			invoice.setUSERID(userId);
			invoice.setINDEXCLASS(indexClass);	
			//load index values new way*** won't use loadIndexValues method since getting the index values from CN 
			String[] cmValues={invoiceNum,invoiceDate,businessPartner,source,timeStamp, userId};	
			invoice.setINVOICEDATE(GilUtility.formatDate(invoiceDate, CNUtilConstants.CN_DATE_FORMAT, CNUtilConstants.GIL_DATE_FORMAT));
			invoice.setCREATEDATE(GilUtility.returnDate(timeStamp, CNUtilConstants.CN_DATE_FORMAT2, CNUtilConstants.GIL_DATE_FORMAT));
			logger.debug("index invoice created date as date: "+invoice.getCREATEDATEasDate());
			logger.debug("index invoice created date as string: "+invoice.getCREATEDATE());
			logger.debug("index invoice date formatted: "+invoice.getINVOICEDATE());
			invoice.setVENDORNAME(businessPartner);
			invoice.setValues(cmValues);
		}else{
			invoice =initInvoice(request, callbacks);
			if(invoice.getCOUNTRY()==null || invoice.getCOUNTRY().equals("") ){
				invoice.setCOUNTRY(request.getParameter("country"));
			}
		}
		return invoice;
	}

	private void removeQuoteLineItems(InvoiceELS invoice){
		invoice.getInvoiceDataModelELS().removeQuoteLineItems();
		invoice.getInvoiceDataModelELS().recalculateUnitAmounts();
	}
	
	@Override
	protected void selectAction(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request,
			Indexing indexing) throws Exception {

		logger.debug("selectAction() ");
		String optionToExecute = request.getParameter("request");
		InvoiceELS invoice=(InvoiceELS)indexing;
		setWSEndPoint(request.getSession(), invoice);
		
		if(optionToExecute.equals(UtilGilConstants.INDEX)){
			index(callbacks, jsonResults, request, invoice);
			
		}else if (optionToExecute.equalsIgnoreCase(UtilGilConstants.SAVE))	{
			save(callbacks,jsonResults,request, invoice);
			
		}else if (optionToExecute.equalsIgnoreCase(UtilGilConstants.RECALCULATE_INVAMT))	{
			recalculateNetAmount(invoice);
			
		}else if (optionToExecute.equalsIgnoreCase(UtilGilConstants.USTAX_CALC))	{
			usTaxCalculation( invoice);
			
		}else if (optionToExecute.equalsIgnoreCase(UtilGilConstants.SEARCH_BYOL))	{
			searchByOffering( invoice);
			
		}else if (optionToExecute.equalsIgnoreCase(UtilGilConstants.VALIDATE_INPUT)){
			validateInput( invoice);
			
		}else if (optionToExecute.equalsIgnoreCase(UtilGilConstants.SEARCH_REF_INV)){
			searchByReferenceInvoice(invoice);
			
		}else if (optionToExecute.equalsIgnoreCase(UtilGilConstants.REMOVE_QLINEITEMS)){
			removeQuoteLineItems(invoice);
			
		}
		else if (optionToExecute.equalsIgnoreCase(UtilGilConstants.SAVE_STATE)){
			saveState(invoice);
			
		}else if (optionToExecute.equalsIgnoreCase(UtilGilConstants.ROLLBACK_INDEXING)){
			rollbackIndexing(invoice);

		}
//		
		buildResponse(invoice, jsonResults);
				
	}

	@Override
	protected void handleException(PluginServiceCallbacks callbacks, JSONResponse jsonResults,
			HttpServletRequest request, Indexing indexing, Exception e) {
		InvoiceELS invoice = ((InvoiceELS) (indexing));
		logger.fatal(e.getMessage());
		invoice.getDialogErrorMsgs().add(e.getMessage());
		
	}

	@Override
	protected DataModel getDataModel(Indexing indexing) {

		return ((InvoiceELS) (indexing)).getInvoiceDataModelELS();
	}

	@Override
	protected void indexDocument(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request,
			Indexing indexing) throws Exception {
		InvoiceELS invoice=(InvoiceELS)indexing;
		if(invoice.getINVOICEDATE()==null ||invoice.getINVOICEDATE().isEmpty() || 
				invoice.getCREATEDATE()==null || invoice.getCREATEDATE().isEmpty()||  invoice.getVENDORNAME()==null){
			invoice.getDialogErrorMsgs().add("Waiting for Indexing to complete. Please try again later.");
		}else{
			
			initalizeIndexing(invoice);
		}
	}

	@Override
	protected void saveDocument(PluginServiceCallbacks callbacks, JSONResponse jsonResults, HttpServletRequest request,
			Indexing indexing) throws Exception {
		
		save((InvoiceELS)indexing);
		
	}
}
