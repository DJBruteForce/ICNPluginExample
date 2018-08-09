package com.ibm.igf.webservice;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;



import com.ibm.gcms.consumer.CTSConsumer;
import com.ibm.gil.business.InvoiceELS;
import com.ibm.gil.business.InvoiceLineItemsELS;
import com.ibm.gil.business.LineItemELS;
import com.ibm.gil.service.helper.LineItemELSServiceHelper;
import com.ibm.gil.util.UtilGilConstants;
//
//import com.ibm.igf.hmvc.Debugger;
import com.ibm.igf.hmvc.PluginConfigurationPane;

import financing.tools.gcps.ws.service.cts.BusinessFaultM;
import financing.tools.gcps.ws.service.cts.CommonTaxSolutionWebServiceProxy;
import financing.tools.gcps.ws.service.cts.MovedPermanentlyM;
import financing.tools.gcps.ws.service.cts.ServiceUnavailableM;
import financing.tools.gcps.ws.service.cts.SystemErrorTypeM;
import financing.tools.gcps.ws.service.cts.common.header.CommonTaxHeaders;
import financing.tools.gcps.ws.service.cts.gettaxes.GetTaxserviceRequest;
import financing.tools.gcps.ws.service.cts.gettaxes.GetTaxserviceResponse;
import financing.tools.gcps.ws.service.cts.gettaxes.GetTaxserviceResponse.Contract.Line.TaxLineItems;
import financing.tools.gcps.ws.service.cts.gettaxes.GetTaxserviceResponse.Contract.Line.TaxLineItems.TaxLine;
/**
 * 
 * @author srinivas
 * Calling Get Taxes on Common Tax Service
 */

public class CTSGetTaxes extends GCPSWebService{
	GetTaxserviceRequest taxserviceRequest = null;
	GetTaxserviceResponse response = null;
	CTSConsumer ctsConsumer = null;
	CommonTaxSolutionWebServiceProxy proxy = null;
    private String webService = null;
	
    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(CTSGetTaxes.class);

	private String urlService=null;
    
    private String getUrlWS(){
    	    
    	if(urlService==null){
    		urlService=PluginConfigurationPane.getPropertiesMap().get(UtilGilConstants.GCMS_SERVER)+UtilGilConstants.CTS_WS;//"https://w3beta-2.toronto.ca.ibm.com:443/financing/tools/gcps/services/"+UtilGilConstants.CTS_WS;//
    	}
    	logger.info("URL Service: "+urlService);
    	return urlService ;
    }
    
    private String getServiceName(){
	    
    	return UtilGilConstants.CTS_WS;
    }
    
	public GetTaxserviceResponse doGetTaxes(final InvoiceELS invoiceDataModel){
		logger.debug("Enter doGetTaxes method");
		taxserviceRequest = new GetTaxserviceRequest();
		loadGetTaxes(invoiceDataModel);

		
		//CTSConsumer ctsConsumer=new CTSConsumer(getUrlWS(),getGCMSId(),getGCMSAccess());
		CTSConsumer ctsConsumer=new CTSConsumer(invoiceDataModel.getGcmsEndpoint().getWSUrl() + getServiceName());
		
				try{
					response=ctsConsumer.callCTS(taxserviceRequest,null);
					
				} catch (BusinessFaultM bf) {
					logger.debug(bf.getMessage());
					setCTSErrors("The Common Tax Service failed with the business error: " +bf.getMessage(),invoiceDataModel);
				} catch (MovedPermanentlyM mp) {
					logger.debug(mp.getMessage());
					setCTSErrors(mp.getMessage(),invoiceDataModel);					
				} catch (ServiceUnavailableM su) {
					logger.debug(su.getMessage());
					setCTSErrors("The Common Tax Service failed with the service unavailable error: "+su.getMessage(),invoiceDataModel);					
				} catch (SystemErrorTypeM se) {
					logger.debug(se.getMessage());
					setCTSErrors("The Common Tax Service failed with a system error. Please contact support. "+se.getMessage(),invoiceDataModel);					
				} catch (Exception ex) {
					logger.debug(ex.getMessage());
					setCTSErrors("The Common Tax Service failed with general exception: "+ex.getMessage(),invoiceDataModel);					
				}
				if(invoiceDataModel.getErrorCodes().isEmpty()){
					
					loadCTSResponse(response,invoiceDataModel);
					logger.debug("*******Successfull response from CTS********");
					if(!invoiceDataModel.isTolerance()){
		//
						logger.debug("Taxes on the invoice are not within the states tax tolerance values.");
						//displaying  front end
//						invoiceDataModel.getDialogErrorMsgs().add("Taxes on the invoice are not within the states tax tolerance values." );
					}else{
						logger.debug("processUSLineItems");
						processUSLineitems(invoiceDataModel);
					}
				}else{
	//
					logger.debug("Failed response from CTS");
					for (String errorCodes : invoiceDataModel.getErrorCodes()) {
						invoiceDataModel.getDialogErrorMsgs().add(errorCodes);
					}
					
//					invoiceDataModel.getEventController().actionPerformed(new ActionEvent(invoiceDataModel, ActionEvent.ACTION_PERFORMED, "Failed response from CTS"));					
				}
				logger.debug("End doGetTaxes method");			
		return response;
	}
	
    /**
     * Processing US line items after successful response from CTS
     */
    private void processUSLineitems(InvoiceELS invoice){
    
//        InvoiceDataModel aDataModel = (InvoiceDataModel) getDataModel();
        ArrayList<LineItemELS> unitLineItems = invoice.getLineItems();
        LineItemELSServiceHelper.renumberLineItems(unitLineItems);
        LineItemELSServiceHelper.spreadTaxRoundingError(new InvoiceLineItemsELS(invoice),unitLineItems);
        
        invoice.getInvoiceDataModelELS().recalculateUnitAmounts();
//        getViewPanel().toGUI(aDataModel);
    }
	private void loadGetTaxes(InvoiceELS invoiceDataModel){
		CommonTaxHeaders header = new CommonTaxHeaders();
		GetTaxserviceRequest.Contract.Line line = new GetTaxserviceRequest.Contract.Line();
		GetTaxserviceRequest.Contract.Line.ShipTo shipTo = new GetTaxserviceRequest.Contract.Line.ShipTo();
		GetTaxserviceRequest.Contract contract = new GetTaxserviceRequest.Contract();
		
		header.setApplicationName(UtilGilConstants.APPLICATION_NAME);
		header.setApplicationId(UtilGilConstants.APPLICATION_NAME);		
		header.setClientLoggingId(invoiceDataModel.getUSERID());
		header.setCtsRequestID(invoiceDataModel.getUSERID());
		
		
		contract.setCountryCode(UtilGilConstants.CTS_US_COUNTRY_CODE);
		contract.setSubsidairyCode(UtilGilConstants.CTS_SUBSIDAIRY_CODE);
		contract.setCurrencyCode(UtilGilConstants.CTS_US_CURRENCY_CODE);
		contract.setContractNumber(invoiceDataModel.getINVOICENUMBER());
		
		line.setID(UtilGilConstants.CTS_ID);		
		GregorianCalendar calendar = new GregorianCalendar();		
		DatatypeFactory df=null;
		try {
			df = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
	
		}
		
		calendar.setTime(invoiceDataModel.getINVOICEDATEasDate());
		contract.setContractDate(df.newXMLGregorianCalendar(calendar));
		line.setInvoiceDate(df.newXMLGregorianCalendar(calendar));		
		line.setFinanced(UtilGilConstants.YES_FLAG);
		line.setTransactionType(UtilGilConstants.CTS_TRANS_TYPE);
		line.setFinanceType(UtilGilConstants.CTS_FIN_TYPE);
		line.setOriginalFinanceType(UtilGilConstants.CTS_FIN_TYPE);
		line.setUpfrontIndicator(UtilGilConstants.YES_FLAG);
		line.setInvoiceLineNumber(UtilGilConstants.ONE);
		line.setQuantity(UtilGilConstants.ONE);
		if(invoiceDataModel.getTAXESINDICATOR().equals("Y")){
			line.setNetPurchasePrice(invoiceDataModel.getIGFBILLEDTOAMOUNTasBigDecimal());
			line.setTaxesPaid(invoiceDataModel.getTAXESBILLEDTOIGFasBigDecimal());			
		}else{
			line.setNetPurchasePrice(invoiceDataModel.getNETAMOUNTasBigDecimal());
			line.setTaxesPaid(invoiceDataModel.getVATAMOUNTasBigDecimal());
		}
		line.setSumOfPayments(BigDecimal.ZERO);		
		line.setGenericMaterialCode(UtilGilConstants.CTS_GENERIC_MATERIAL_CODE);
		line.setToleranceAmount(Integer.parseInt(invoiceDataModel.getTOLERANCEAMT()));
		line.setShipTo(shipTo);		
		
		shipTo.setCountryAbbreviation(invoiceDataModel.getCOUNTRY());
		shipTo.setStateCode(invoiceDataModel.getSTATECODE());
		
		contract.getLine().add(line);		
		
		taxserviceRequest.setCommonTaxHeader(header);
		taxserviceRequest.setContract(contract);
	}
	
    
    private void setCTSErrors(String errorMessage, InvoiceELS invDataModel){
    	if(errorMessage!=null){
    		invDataModel.getErrorCodes().add(errorMessage);
    	}else{
    		errorMessage = "GIL did not receive a response from the Common Tax Service within the time limit.";
    		invDataModel.getErrorCodes().add(errorMessage);
    	}
    }
    
    private void loadCTSResponse(GetTaxserviceResponse response, InvoiceELS invDataModel){
    	
    	try{
    		List<financing.tools.gcps.ws.service.cts.gettaxes.GetTaxserviceResponse.Contract.Line> lineList = response.getContract().getLine();
    		for(financing.tools.gcps.ws.service.cts.gettaxes.GetTaxserviceResponse.Contract.Line line:lineList){
    			List<TaxLineItems> taxLineItemList= line.getTaxLineItems();
    			for(TaxLineItems taxLineItems:taxLineItemList){
    				List<TaxLine> taxLineList = taxLineItems.getTaxLine();
    				for(TaxLine taxLine: taxLineList){
    					logger.debug("Taxline withinTolerance: "+taxLine.getWithinTolerance());
    					if(taxLine.getWithinTolerance().equals("N")){
    						invDataModel.setTOLERANCEINDC("N");
    						break;
    					}else{
    						invDataModel.setTOLERANCEINDC("Y");
    						break;
    					}
    				}
    			}
    		
    		}
    		logger.debug("Tolerance indc: "+invDataModel.getTOLERANCEINDC());
    		logger.debug("Taxes indc: "+invDataModel.getTAXESINDICATOR());
    		if(invDataModel.getTOLERANCEINDC().equals("Y")){
    			if(invDataModel.getTAXESINDICATOR().equals("Y")){
    				invDataModel.getInvoiceDataModelELS().calculateIGFLineItemTaxAmount();
    			}else{
    				invDataModel.getInvoiceDataModelELS().calculateLineItemTaxAmount();
    			}
    		}
    		
    	} catch (Exception exc)
        {
            logger.info("Error calculating CTS Response");
            logger.fatal(exc.toString());
        }
   }
    
    public boolean getTaxes(InvoiceELS invoiceDataModel)
    {
        try
        {
            // call the web service wrapper
        	GetTaxserviceResponse response = doGetTaxes(invoiceDataModel);

        } catch (Exception exc)
        {
            logger.info("Error getting CTS Taxes");
            logger.fatal(exc.toString());
            return false;
        }

        return true;
    } 
    
    //this can be managed by was
//    public Map<String, Object> getCTSTimeOutConfig(){
//    	Map<String, Object> timeOutConfig= new HashMap<String, Object>();
//    	timeOutConfig.put(com.ibm.ws.websvcs.transport.common.TransportConstants.READ_TIMEPOUT,"120");
//    	timeOutConfig.put(com.ibm.ws.websvcs.transport.common.TransportConstants.CONN_TIMEOUT,"60"); 
//    	return timeOutConfig;
//   	
//    }
    
 
}
