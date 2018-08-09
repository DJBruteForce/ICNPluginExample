

package com.ibm.igf.webservice;

import java.util.ArrayList;
import java.util.List;

import com.ibm.gcms.consumer.GetInvoiceConsumer;
import com.ibm.gil.business.InvoiceCommentsELS;
import com.ibm.gil.business.InvoiceELS;
import com.ibm.gil.business.LineItemELS;
import com.ibm.gil.util.UtilGilConstants;
import com.ibm.igf.hmvc.PluginConfigurationPane;
//import com.ibm.igf.hmvc.RegionManager;
import com.ibm.igf.webservice.util.GetInvoiceBuilder;
import com.ibm.igf.webservice.util.ShowInvoiceResponse;

import financing.tools.gcps.ws.domain.InvoiceAddress;
import financing.tools.gcps.ws.domain.jaxb.InvoiceComment_jaxb;
import financing.tools.gcps.ws.domain.jaxb.InvoiceDetail_jaxb;
import financing.tools.gcps.ws.domain.jaxb.Invoice_jaxb;


/**
 * @author ajimena
 *
 */
public class GCPSGetInvoice extends GCPSWebService {
//    private GetInvoice getInvoice = null;
//    private ShowInvoice showInvoice = null;
	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(GCPSGetInvoice.class);
    
  private String urlService=null;
    
    private String getUrlWS(){
    	    
    	if(urlService==null){
    		urlService=PluginConfigurationPane.getPropertiesMap().get(UtilGilConstants.GCMS_SERVER)+UtilGilConstants.GETINVOICE_WS;
    		
    	}
    	logger.info("URL Service: "+urlService);
    	return urlService ;
    }
    
    private String getServiceName(){
	    
    	return UtilGilConstants.GETINVOICE_WS;
    }

    public com.ibm.xmlns.ims._1_3.showinvoice.ShowInvoice  doGetInvoice(com.ibm.gil.business.InvoiceELS gilInvoice)
    {
    	com.ibm.xmlns.ims._1_3.showinvoice.ShowInvoice showInvoice=null;
        try
        {

        	GetInvoiceBuilder getInvoice=new GetInvoiceBuilder(gilInvoice);
        	//getting from front end the url
        	//GetInvoiceConsumer getInvoiceWs=new GetInvoiceConsumer(getUrlWS(),getGCMSId(),getGCMSAccess());
        	GetInvoiceConsumer getInvoiceWs=new GetInvoiceConsumer(gilInvoice.getGcmsEndpoint().getWSUrl() + getServiceName(),gilInvoice.getGcmsEndpoint().getFunctionalId(),gilInvoice.getGcmsEndpoint().getAccess());
        	logger.debug("Call gcps get invoice");
        	//invoking gerInvoiceService
        	showInvoice=getInvoiceWs.callGcpsGetInvoice(getInvoice.buildGetInvoice());
        	
        	logger.debug("Show invoice retrieved");

            if ((showInvoice == null) )
            {
                logger.info("Invoice not found");
                return null;
            }


            if (showInvoice.getApplicationArea()!=null &&
            		 showInvoice.getApplicationArea().getApplication().get(0).getApplicationStatus().get(0).getStatusId().equals("SUCCESS"))
            {
              //nothing to do, it's fine
            	logger.debug("showInvoice.getApplicationArea()"+showInvoice.getApplicationArea());
            	logger.debug(showInvoice.getApplicationArea().getApplication().get(0).getApplicationStatus().get(0).getStatusId());
            } else
            {
            	logger.debug("Returning null");
            	//TODO debug
                return null;
            }

        } catch (NullPointerException exc)
        {
        	logger.fatal("No gcms invoice retrieved");
        	
            // no data retrieved
            return null;
        } catch (Exception je)
        {
        	logger.fatal("Call to GetInvoice exc: "+je.getMessage());
        	
        	return null;
        }

        return showInvoice;
    }

    public ArrayList getInvoices(InvoiceELS invoice) throws Exception
    {
        try
        {
            // call the web service wrapper
        	com.ibm.xmlns.ims._1_3.showinvoice.ShowInvoice showInvoice = doGetInvoice(invoice);
            if (showInvoice == null)
            {
                return new ArrayList();
            }
            
            ShowInvoiceResponse showInvoiceResponse=new ShowInvoiceResponse(showInvoice);
//			financing.tools.gcps.ws.domain.jaxb.Invoice_jaxb invoiceImpl2=(Invoice_jaxb)showInvoiceResponse.getInvoices().get(0);
            List invoiceList = showInvoiceResponse.getInvoices();
            if (invoiceList == null)
            {
                return new ArrayList();
            }
            ArrayList invoices = new ArrayList(invoiceList.size());
            for (int i = 0; i < invoiceList.size(); i++)
            {
                // convert from jaxb objects into datamodel objects
                Invoice_jaxb invoiceImpl = (Invoice_jaxb) invoiceList.get(i);
                InvoiceELS retrievedInvoice = new InvoiceELS(invoice.getCOUNTRY(), invoice.getDatastore());
                transformShowInvoice(invoiceImpl, retrievedInvoice);

                List detailList = invoiceImpl.getDetails();
                for (int j = 0; (detailList != null && j < detailList.size()); j++)
                {
                    InvoiceDetail_jaxb detailImpl = (InvoiceDetail_jaxb) detailList.get(j);
                    LineItemELS retrievedDetail = new LineItemELS(invoice.getCOUNTRY());
                    transformShowInvoiceLineItems(detailImpl, retrievedDetail);
                    retrievedDetail.setCOUNTRY(retrievedInvoice.getCOUNTRY());
                    retrievedInvoice.getLineItems().add(retrievedDetail);
                    if (j == 0)
                    {
                        retrievedInvoice.setPURCHASEORDERNUMBER(retrievedDetail.getPONUMBER());
                        retrievedInvoice.setCOSTCENTER(retrievedDetail.getCOSTCENTER());
                    }

                }

                List commentsList = invoiceImpl.getComments();
                for (int j = 0; (commentsList != null && j < commentsList.size()); j++)
                {
                    InvoiceComment_jaxb commentImpl = (InvoiceComment_jaxb) commentsList.get(j);
                    InvoiceCommentsELS comment = new InvoiceCommentsELS();
                    transformShowInvoiceComments(commentImpl, comment);

                    retrievedInvoice.getComments().add(comment);
                }

                // customer address
                InvoiceAddress customerAddress = invoiceImpl.getBillToAddress();
                if (customerAddress != null)
                    transformShowInvoiceCustomerAddress(customerAddress, retrievedInvoice);

                InvoiceAddress installedAddress = invoiceImpl.getInstallToAddress();
                if (installedAddress != null)
                    transformShowInvoiceInstalledAddress(installedAddress, retrievedInvoice);
                logger.debug("After transform retrievedInvoice date: "+retrievedInvoice.getINVOICEDATE());
                logger.debug("After transform retrievedInvoiceas date: "+retrievedInvoice.getINVOICEDATEasDate().toString());
                // add it to the return list
                invoices.add(retrievedInvoice);
            }

            // return the list
            return (invoices);

        } catch (Exception exc)
        {
            logger.info("Error retrieving gcms invoice information");
            logger.fatal(exc.toString());
            throw exc;
        }
        //return null;
    }

    private void transformShowInvoiceComments(InvoiceComment_jaxb commentImpl, InvoiceCommentsELS commentDataModel)
    {
        ObjectTransform.transform(commentImpl, commentDataModel, new String[] { "CommentSequenceNumber", "CommentDescription" });
    }

    private void transformShowInvoiceLineItems(InvoiceDetail_jaxb detailImpl, LineItemELS lineItemDataModel)
    {
        ObjectTransform.transform(detailImpl, lineItemDataModel, new String[] { "InvoiceLineItemType", "MachineType", "MachineModel", "ManufacturerPartNumber", "LineNumber", "LineSequenceNumber", "SerialNumber", "UnitPrice", "MesNumber", "TaxCode",
                "UnitTax", "PurchaseOrderNumber", "ItemDescription", "CostCenter", "MesIndc", "InConfiguration", "Term", "FinancingType", "EquipmentSource" });
        lineItemDataModel.setQUANTITY("1");
        lineItemDataModel.setTOTALVATAMOUNT(lineItemDataModel.getVATAMOUNT());
        lineItemDataModel.setEXTENDEDPRICE(lineItemDataModel.getUNITPRICE());
    }

    private void transformShowInvoice(Invoice_jaxb invoice, InvoiceELS invoiceDataModel)
    {
    	//Story 1750051 CA GIL changes   
        ObjectTransform.transform(invoice, invoiceDataModel, new String[] { "CmObjectId", "InvoiceNumber", "InvoiceDate", "CountryCode", "InvoiceType", "TotalInvoiceAmount", "TotalTaxAmount", "AvailableTaxAmount", "AvailableUnitAmount", "LegalId",
                "DebitCreditIndicator", "ReferenceInvoiceNumber", "CoaInvoiceIndicator", "Currency", "ExchangeRate", "Exception", "PoexCode","ProvinceCode", "OfferLetterNumber", "ResponsibleId", "UrgentInvoiceIndc", "SourceIndc",

                "VendorName", "VendorNumber", "SaNumber", "InvoiceStatus","CompanyCode","DistVendorNumber" });
      //End Story 1750051 CA GIL changes  
        if (invoiceDataModel.getROFINVOICESOURCE().equals("ROF"))
        {
            invoiceDataModel.setROFINVOICEINDC("Y");
        } else
        {
            invoiceDataModel.setROFINVOICEINDC("N");
        }
    }



    private void transformShowInvoiceCustomerAddress(InvoiceAddress customerAddress, InvoiceELS retrievedInvoice)
    {
        retrievedInvoice.setCUSTOMERNUMBER(customerAddress.getCustomerNumber());
        retrievedInvoice.setCUSTOMERNAME(customerAddress.getCustomerName());
        retrievedInvoice.setCUSTOMERADDRESS1(customerAddress.getAddressLine1());
        retrievedInvoice.setCUSTOMERADDRESS2(customerAddress.getAddressLine2());
        retrievedInvoice.setCUSTOMERADDRESS3(customerAddress.getAddressLine3());
        retrievedInvoice.setCUSTOMERADDRESS4(customerAddress.getAddressLine4());
        retrievedInvoice.setCUSTOMERADDRESS5(customerAddress.getAddressLine5());
        retrievedInvoice.setCUSTOMERADDRESS6(customerAddress.getAddressLine6());
    }

    private void transformShowInvoiceInstalledAddress(InvoiceAddress customerAddress, InvoiceELS retrievedInvoice)
    {
        retrievedInvoice.setINSTALLEDCUSTOMERNUMBER(customerAddress.getCustomerNumber());
        retrievedInvoice.setINSTALLEDCUSTOMERNAME(customerAddress.getCustomerName());
        retrievedInvoice.setINSTALLEDCUSTOMERADDRESS1(customerAddress.getAddressLine1());
        retrievedInvoice.setINSTALLEDCUSTOMERADDRESS2(customerAddress.getAddressLine2());
        retrievedInvoice.setINSTALLEDCUSTOMERADDRESS3(customerAddress.getAddressLine3());
        retrievedInvoice.setINSTALLEDCUSTOMERADDRESS4(customerAddress.getAddressLine4());
        retrievedInvoice.setINSTALLEDCUSTOMERADDRESS5(customerAddress.getAddressLine5());
        retrievedInvoice.setINSTALLEDCUSTOMERADDRESS6(customerAddress.getAddressLine6());
    }



}
