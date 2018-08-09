/*
 * Created on Apr 7, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ibm.igf.webservice;

import java.util.ArrayList;



import com.ibm.gcms.consumer.ProcessInvoiceConsumer;
import com.ibm.gil.business.InvoiceCommentsELS;
import com.ibm.gil.business.InvoiceELS;
import com.ibm.gil.business.LineItemELS;
import com.ibm.gil.util.UtilGilConstants;

import com.ibm.igf.hmvc.PluginConfigurationPane;
import com.ibm.igf.webservice.util.AcknowledgeInvoiceResponse;
import com.ibm.igf.webservice.util.ProcessInvoiceBuilder;
import com.ibm.xmlns.ims._1_3.acknowledgeinvoice_2005_03_04.AcknowledgeInvoice;

//import com.ibm.igf.gil.CommentsDataModel;
//import com.ibm.igf.gil.InvoiceDataModel;
//import com.ibm.igf.gil.LineItemDataModel;
//import com.ibm.igf.hmvc.Debugger;


import financing.tools.gcps.ws.domain.Invoice;
import financing.tools.gcps.ws.domain.InvoiceFactory;
import financing.tools.gcps.ws.domain.jaxb.BillToAddress_jaxb;
import financing.tools.gcps.ws.domain.jaxb.InstallToAddress_jaxb;
import financing.tools.gcps.ws.domain.jaxb.InvoiceComment_jaxb;
import financing.tools.gcps.ws.domain.jaxb.InvoiceDetail_jaxb;
import financing.tools.gcps.ws.domain.jaxb.SoldToAddress_jaxb;

import financing.tools.gcps.ws.service.processinvoice.request.ProcessInvoice;
import financing.tools.gcps.ws.service.processinvoice.request.ProcessInvoiceFactory;

import financing.tools.gcps.ws.service.processinvoice.request.jaxb.ProcessInvoice_jaxb;
//import financing.tools.gcps.ws.service.processinvoice.response.AcknowledgeInvoice;



/**
 * @author ajimena
 * 
 * 
 */
public class GCPSProcessInvoice extends GCPSWebService {
	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(GCPSProcessInvoice.class);
    private ProcessInvoice_jaxb ajaxb = null;

    private ProcessInvoice processInvoice = null;
    private Invoice domainInvoice = null;
//    private AcknowledgeInvoice confirmInvoice = null;

    private String urlService=null;
    
    private String getUrlWS(){
    	    
    	if(urlService==null){
    		urlService=PluginConfigurationPane.getPropertiesMap().get(UtilGilConstants.GCMS_SERVER)+UtilGilConstants.PROCESSINVOICE_WS;
    	}
    	logger.info("URL Service: "+urlService);
    	return urlService ;
    }
    
    private String getServiceName(){
	    
    	return UtilGilConstants.PROCESSINVOICE_WS;
    }
    
    /**Invoice Processinvoice values **/
//    InvoiceHeader invoiceHeader= null;
//    List<InvoiceLine> invoiceLines=null;
    public AcknowledgeInvoice doProcessInvoice(InvoiceELS invoiceDM)
    {
    	AcknowledgeInvoice ackInvoice=null;

        try
        {
            processInvoice = ProcessInvoiceFactory.singleton().create();
            domainInvoice = InvoiceFactory.singleton().create();

            
            loadDomainInvoice(invoiceDM);

            
            //ProcessInvoiceConsumer processInvoiceConsumer=new ProcessInvoiceConsumer(getUrlWS(),getGCMSId(),getGCMSAccess());
            ProcessInvoiceConsumer processInvoiceConsumer=new ProcessInvoiceConsumer(invoiceDM.getGcmsEndpoint().getWSUrl() + getServiceName(),invoiceDM.getGcmsEndpoint().getFunctionalId(),invoiceDM.getGcmsEndpoint().getAccess());
            
            		
            ProcessInvoiceBuilder processInvoiceBuilder=new ProcessInvoiceBuilder();
            
            ackInvoice= processInvoiceConsumer.callGcpsProcessInvoice(invoiceDM.getSENDER(),invoiceDM.getUNIQUEREQUESTNUMBER(),processInvoiceBuilder.createInvoiceJaxbObjects(domainInvoice));

            if (ackInvoice == null)
                return null;

            logger.debug("AcknowledgeInvoice not null");
            
            

       } catch (Exception je)
        {
            logger.fatal(je.toString());
        }

        return ackInvoice;
    }

    public boolean processInvoice(InvoiceELS aIDM)
    {
        AcknowledgeInvoice results = doProcessInvoice(aIDM);
       AcknowledgeInvoiceResponse ackResponse=new AcknowledgeInvoiceResponse(results);
        if (results == null)
            return false;

        return (ackResponse.getErrorCodes().get(0).equals("gcps.messages.service.process.invoice.success"));
    }

    private void loadDomainInvoice(InvoiceELS invoiceDM)
    {
        ObjectTransform.transform(invoiceDM, processInvoice, new String[] { "SENDER", "UNIQUEREQUESTNUMBER" });

        // set some default values
        
        domainInvoice.setInvoiceSource("P");
        domainInvoice.setTpidCode("XXXX");
        domainInvoice.setTpidCountry(invoiceDM.getCOUNTRY());
        domainInvoice.setInvoiceReceiveDate(invoiceDM.getCREATEDATEasDate());

        if (invoiceDM.getINSTALLEDCUSTOMERNUMBER().trim().length() != 0)
        {
            // add the sold to address
            SoldToAddress_jaxb soldTo = new SoldToAddress_jaxb();
            transformProcessInvoiceSoldToFromInstalled(invoiceDM, soldTo);
            domainInvoice.setSoldToAddress(soldTo);
        } else
        {
            // add the sold to address
            SoldToAddress_jaxb soldTo = new SoldToAddress_jaxb();
            transformProcessInvoiceSoldToFromBillTo(invoiceDM, soldTo);
            domainInvoice.setSoldToAddress(soldTo);
        }

        // add the install at address
        InstallToAddress_jaxb installedTo = new InstallToAddress_jaxb();
        transformProcessInvoiceInstalledTo(invoiceDM, installedTo);
        domainInvoice.setInstallToAddress(installedTo);

        // add the bill to address
        BillToAddress_jaxb billTo = new BillToAddress_jaxb();
        transformProcessInvoiceBillTo(invoiceDM, billTo);
        domainInvoice.setBillToAddress(billTo);

        // add the units
        ArrayList lineItemDataModels = invoiceDM.getLineItems();
        LineItemELS aLineItemDataModel = null;

        for (int i = 0; i < lineItemDataModels.size(); i++)
        {
            aLineItemDataModel = ((LineItemELS) lineItemDataModels.get(i));

            InvoiceDetail_jaxb detailToAdd = new InvoiceDetail_jaxb();

            // copy over the line item to each of the detail implementation
            // we'll be using
            transformProcessInvoiceLineItems(aLineItemDataModel, detailToAdd);

            domainInvoice.addInvoiceDetail(detailToAdd);
        }

        // add the comments
        ArrayList commentDataModels = invoiceDM.getComments();
        InvoiceCommentsELS aCommentsDataModel = new InvoiceCommentsELS();
        for (int i = 0; i < commentDataModels.size(); i++)
        {
            InvoiceComment_jaxb commentImpl = new InvoiceComment_jaxb();
            aCommentsDataModel = (InvoiceCommentsELS) commentDataModels.get(i);
            transformProcessInvoiceComments(aCommentsDataModel, commentImpl);
            domainInvoice.addInvComment(commentImpl);
        }

        //Story 1750051 CA GIL changes
        ObjectTransform.transform(invoiceDM, domainInvoice, new String[] { "OBJECTID", "INVOICENUMBER", "INVOICEDATEasDate", "COUNTRY", "INVOICETYPE", "NETAMOUNTasBigDecimal", "NETBALANCEasBigDecimal", "VATAMOUNTasBigDecimal", "VATBALANCEasBigDecimal",
                "VATREGISTRATIONNUMBER", "DBCR", "REFERENCEINVOICENUMBER", "COAWITHINVOICEINDICATORasBoolean", "CURRENCY", "EXCHANGERATEasBigDecimal", "EXCEPTIONSINDICATORasBoolean", "POEXCODE", "OFFERINGLETTERNUMBER", "RESPONSIBLEPARTYID",
                "ROFINVOICESOURCE", "AUTOCREATECOAINDC", "PROVINCECODE",

                "VENDORNAME", "VENDORNUMBER", "SRNUMBER",

                "COMPANYCODE", "DISTRIBUTORNUMBER",

                "URGENTINDICATORasBoolean" });
      //End Story 1750051 CA GIL changes

    }

    private void transformProcessInvoiceLineItems(LineItemELS lineItemDataModel, InvoiceDetail_jaxb detailImpl)
    {
        ObjectTransform.transform(lineItemDataModel, detailImpl, new String[] { "INVOICEITEMTYPE", "TYPE", "MODEL", "PARTNUMBER", "LINENUMBER", "SUBLINENUMBER", "QUANTITYasInteger", "SERIAL", "UNITPRICEasBigDecimal", "MESNUMBER", "VATCODE",
                "VATAMOUNTasBigDecimal", "PONUMBER", "COSTCENTER", "DESCRIPTION", "CONFIGUSEDasBool", "MESINDICATORasBool", "TERM", "FINANCINGTYPE", "PRODCAT", "PRODTYPE", "EQUIPSOURCE"});
    }

    private void transformProcessInvoiceComments(InvoiceCommentsELS aCommentsDataModel, InvoiceComment_jaxb commentImpl)
    {
        ObjectTransform.transform(aCommentsDataModel, commentImpl, new String[] { "COMMENTNUMBER", "COMMENTS" });
    }

    private void transformProcessInvoiceSoldToFromBillTo(InvoiceELS aInvoiceDataModel, SoldToAddress_jaxb soldToImpl)
    {
        ObjectTransform.transform(aInvoiceDataModel, soldToImpl, new String[] { "CUSTOMERNAME", "CUSTOMERNUMBER", "CUSTOMERADDRESS1", "CUSTOMERADDRESS2", "CUSTOMERADDRESS3", "CUSTOMERADDRESS4", "CUSTOMERADDRESS5", "CUSTOMERADDRESS6" });
    }

    private void transformProcessInvoiceSoldToFromInstalled(InvoiceELS aInvoiceDataModel, SoldToAddress_jaxb soldToImpl)
    {
        ObjectTransform.transform(aInvoiceDataModel, soldToImpl, new String[] { "INSTALLEDCUSTOMERNAME", "INSTALLEDCUSTOMERNUMBER", "INSTALLEDCUSTOMERADDRESS1", "INSTALLEDCUSTOMERADDRESS2", "INSTALLEDCUSTOMERADDRESS3", "INSTALLEDCUSTOMERADDRESS4",
                "INSTALLEDCUSTOMERADDRESS5", "INSTALLEDCUSTOMERADDRESS6" });
    }

    private void transformProcessInvoiceBillTo(InvoiceELS aInvoiceDataModel, BillToAddress_jaxb billToImpl)
    {
        ObjectTransform.transform(aInvoiceDataModel, billToImpl, new String[] { "CUSTOMERNAME", "CUSTOMERNUMBER", "CUSTOMERADDRESS1", "CUSTOMERADDRESS2", "CUSTOMERADDRESS3", "CUSTOMERADDRESS4", "CUSTOMERADDRESS5", "CUSTOMERADDRESS6" });
    }

    private void transformProcessInvoiceInstalledTo(InvoiceELS aInvoiceDataModel, InstallToAddress_jaxb installToImpl)
    {
        ObjectTransform.transform(aInvoiceDataModel, installToImpl, new String[] { "INSTALLEDCUSTOMERNAME", "INSTALLEDCUSTOMERNUMBER", "INSTALLEDCUSTOMERADDRESS1", "INSTALLEDCUSTOMERADDRESS2", "INSTALLEDCUSTOMERADDRESS3", "INSTALLEDCUSTOMERADDRESS4",
                "INSTALLEDCUSTOMERADDRESS5", "INSTALLEDCUSTOMERADDRESS6" });
    }



}
