/*
 * Created on Apr 13, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.ibm.igf.webservice;

import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Hashtable;

import com.ibm.gil.business.InvoiceCommentsELS;
import com.ibm.gil.business.InvoiceELS;
import com.ibm.gil.business.LineItemELS;
import com.ibm.gil.business.MiscELS;
import com.ibm.gil.business.OfferingLetterELS;

import financing.tools.gcps.ws.domain.Coa;
import financing.tools.gcps.ws.domain.InvoiceAddress;
import financing.tools.gcps.ws.domain.QuoteHeader;
import financing.tools.gcps.ws.domain.jaxb.BillToAddress_jaxb;
import financing.tools.gcps.ws.domain.jaxb.InstallToAddress_jaxb;
import financing.tools.gcps.ws.domain.jaxb.InvoiceComment_jaxb;
import financing.tools.gcps.ws.domain.jaxb.InvoiceDetail_jaxb;
import financing.tools.gcps.ws.domain.jaxb.Invoice_jaxb;
import financing.tools.gcps.ws.domain.jaxb.OfferLetter_jaxb;
import financing.tools.gcps.ws.domain.jaxb.QuoteDetail_jaxb;
import financing.tools.gcps.ws.domain.jaxb.SoldToAddress_jaxb;
import financing.tools.gcps.ws.service.createofferletter.request.jaxb.CreateOfferLetter_jaxb;
import financing.tools.gcps.ws.service.getinvoice.request.GetInvoice;
import financing.tools.gcps.ws.service.getofferletter.request.jaxb.GetOfferLetter_jaxb;
import financing.tools.gcps.ws.service.updateofferletter.request.jaxb.UpdateOfferLetter_jaxb;

/**
 * @author SteveBaber
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ObjectTransform {
	
	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(ObjectTransform.class);

    private static Hashtable invoiceDataModelToXMLMap = null;
    private static Hashtable xmlToInvoiceDataModelToMap = null;

    private static Hashtable lineItemDataModelToXMLMap = null;
    private static Hashtable xmlToLineItemDataModelToMap = null;

    private static Hashtable offeringLetterDataModelToXMLMap = null;
    private static Hashtable createOfferingLetterDataModelToXMLMap = null;
    private static Hashtable miscDataModelToXMLMap = null;
    private static Hashtable xmlToOfferingLetterDataModelToMap = null;

    private static Hashtable commentsDataModelToXMLMap = null;
    private static Hashtable xmlToCommentsDataModelToMap = null;

    private static Hashtable soldToDataModelToXMLMap = null;
    private static Hashtable xmlToSoldToDataModelToMap = null;

    private static Hashtable billToDataModelToXMLMap = null;
    private static Hashtable xmlToBillToDataModelToMap = null;

    private static Hashtable installToDataModelToXMLMap = null;
    private static Hashtable xmlToInstallToDataModelToMap = null;

    private static Hashtable vendorDataModelToXMLMap = null;
    private static Hashtable xmlToVendorDataModelToMap = null;

    private static Hashtable COADataModelToXMLMap = null;
    private static Hashtable xmlToCOADataModelToMap = null;

    private static Hashtable offeringLetterDataModelToXMLQuoteMap = null;
    private static Hashtable xmlQuoteToOfferingLetterDataModelToMap = null;
    private static Hashtable xmlQuoteDetailToLineItemDataModelToMap = null;

    public static void transform(Object fromObject, Object toObject, String[] valueList)
    {
        Hashtable map = null;

        // map from the datamodelsOfferingLetterELS
        if ((fromObject instanceof InvoiceELS) && (toObject instanceof SoldToAddress_jaxb))
            map = getSoldToDataModelToXMLMap();
        else if ((fromObject instanceof InvoiceELS) && (toObject instanceof BillToAddress_jaxb))
            map = getBillToDataModelToXMLMap();
        else if ((fromObject instanceof InvoiceELS) && (toObject instanceof InstallToAddress_jaxb))
            map = getInstallToDataModelToXMLMap();
        else if (fromObject instanceof InvoiceELS)
            map = getInvoiceDataModelToXMLMap();
        else if (fromObject instanceof LineItemELS)
            map = getLineItemDataModelToXMLMap();
        else if (fromObject instanceof InvoiceCommentsELS)
            map = getCommentsDataModelToXMLMap();
        else if ((fromObject instanceof OfferingLetterELS) && (toObject instanceof GetOfferLetter_jaxb))
            map = getOfferingLetterDataModelToXMLMap();
        else if ((fromObject instanceof OfferingLetterELS) && (toObject instanceof CreateOfferLetter_jaxb))
            map = getCreateOfferingLetterDataModelToXMLMap();
        else if ((fromObject instanceof OfferingLetterELS) && (toObject instanceof UpdateOfferLetter_jaxb))
            map = getOfferingLetterDataModelToXMLMap();
//        else if (fromObject instanceof COADataModel)
//            map = getCOADataModelToXMLMap();
        else if (fromObject instanceof MiscELS)
        	map = getMiscDataModelToXMLMap();

        // map from the jaxb
        else if (fromObject instanceof GetInvoice)
            map = getXMLToInvoiceDataModelMap();
        else if (fromObject instanceof Invoice_jaxb)
            map = getXMLToInvoiceDataModelMap();
        else if (fromObject instanceof InvoiceAddress)
            map = getXMLToSoldToDataModelMap();
        else if (fromObject instanceof InvoiceDetail_jaxb)
            map = getXMLToLineItemDataModelMap();
        else if (fromObject instanceof SoldToAddress_jaxb)
            map = getXMLToSoldToDataModelMap();
        else if (fromObject instanceof BillToAddress_jaxb)
            map = getXMLToBillToDataModelMap();
        else if (fromObject instanceof InstallToAddress_jaxb)
            map = getXMLToInstallToDataModelMap();
        else if (fromObject instanceof OfferLetter_jaxb)
            map = getXMLToOfferingLetterDataModelMap();
        else if (fromObject instanceof InvoiceComment_jaxb)
            map = getXMLToCommentsDataModelMap();
        else if (fromObject instanceof UpdateOfferLetter_jaxb)
            map = getXMLToOfferingLetterDataModelMap();
        else if (fromObject instanceof Coa)
            map = getXMLToCOADataModelMap();
        else if (fromObject instanceof QuoteHeader)
            map = getXMLQuoteToOfferingLetterDataModelMap();
        else if (fromObject instanceof QuoteDetail_jaxb)
            map = getXMLQuoteDetailToLineItemDataModelMap();
        else
            throw new IllegalArgumentException("No mapping from specified classes: " + fromObject.getClass() + " -> " + toObject.getClass());

        String from = null;
        String to = null;
        Method fromM = null;
        Method toM = null;
        Class[] noparms = new Class[0];
        Class[] inparms = new Class[1];
        Object[] noin = new Object[0];
        Object[] in = new Object[1];
        for (int i = 0; i < valueList.length; i++)
        {
            try
            {
                from = "get" + valueList[i];
                to = "set" + (String) map.get(valueList[i]);

                fromM = fromObject.getClass().getMethod(from, noparms);
                in[0] = fromM.invoke(fromObject, noin);
                if (in[0] != null)
                {
                    if (in[0] instanceof String)
                    {
                        in[0] = ((String) in[0]).trim();
                    }
                    inparms[0] = in[0].getClass();
                    toM = toObject.getClass().getMethod(to, inparms);
                    toM.invoke(toObject, in);
                }
            } catch (Exception exc)
            {
            	logger.fatal("No conversion from " + from + " -> " + to + " - "+ exc.toString(),exc);
 
            }

        }
    }

    private static Hashtable getInvoiceDataModelToXMLMap()
    {
        if (invoiceDataModelToXMLMap == null)
        {
            /*
             * Invoice_jaxb aninv = null; aninv.get
             */
            invoiceDataModelToXMLMap = new Hashtable();
            invoiceDataModelToXMLMap.put("OBJECTID", "CmObjectId");
            invoiceDataModelToXMLMap.put("INVOICENUMBER", "InvoiceNumber");
            invoiceDataModelToXMLMap.put("INVOICEDATEasDate", "InvoiceDate");
            invoiceDataModelToXMLMap.put("SENDER", "Component");
            invoiceDataModelToXMLMap.put("UNIQUEREQUESTNUMBER", "BodId");
            invoiceDataModelToXMLMap.put("COUNTRY", "CountryCode");
            invoiceDataModelToXMLMap.put("INVOICETYPE", "InvoiceType");
            invoiceDataModelToXMLMap.put("VATAMOUNTasBigDecimal", "TotalTaxAmount");
            invoiceDataModelToXMLMap.put("VATBALANCEasBigDecimal", "AvailableTaxAmount");
            invoiceDataModelToXMLMap.put("NETAMOUNTasBigDecimal", "TotalInvoiceAmount");
            invoiceDataModelToXMLMap.put("NETBALANCEasBigDecimal", "AvailableUnitAmount");
            invoiceDataModelToXMLMap.put("VATREGISTRATIONNUMBER", "LegalId");
            invoiceDataModelToXMLMap.put("DBCR", "DebitCreditIndicator");
            invoiceDataModelToXMLMap.put("REFERENCEINVOICENUMBER", "ReferenceInvoiceNumber");
            invoiceDataModelToXMLMap.put("COAWITHINVOICEINDICATORasBoolean", "CoaInvoiceIndicator");
            invoiceDataModelToXMLMap.put("CURRENCY", "Currency");
            invoiceDataModelToXMLMap.put("EXCHANGERATEasBigDecimal", "ExchangeRate");
            invoiceDataModelToXMLMap.put("EXCEPTIONSINDICATORasBoolean", "Exception");
            invoiceDataModelToXMLMap.put("POEXCODE", "PoexCode");
            invoiceDataModelToXMLMap.put("OFFERINGLETTERNUMBER", "OfferLetterNumber");
            invoiceDataModelToXMLMap.put("ROFINVOICESOURCE", "SourceIndc");
            invoiceDataModelToXMLMap.put("AUTOCREATECOAINDC", "AutoCreateCoaIndc");
            invoiceDataModelToXMLMap.put("RESPONSIBLEPARTYID", "ResponsibleId");
            invoiceDataModelToXMLMap.put("COSTCENTER", "CostCenter");
            invoiceDataModelToXMLMap.put("URGENTINDICATORasBoolean", "UrgentInvoiceIndc");
            
            //Story 1750051 CA GIL changes
            invoiceDataModelToXMLMap.put("PROVINCECODE", "ProvinceCode");
            //End Story 1750051

            invoiceDataModelToXMLMap.put("VENDORNAME", "VendorName");
            invoiceDataModelToXMLMap.put("VENDORNUMBER", "VendorNumber");
            invoiceDataModelToXMLMap.put("VENDORADDRESS1", "VendorAddress1");
            invoiceDataModelToXMLMap.put("VENDORADDRESS2", "VendorAddress2");
            invoiceDataModelToXMLMap.put("VENDORADDRESS3", "VendorAddress3");

            invoiceDataModelToXMLMap.put("CUSTOMERNUMBER", "CustomerNumber");
            invoiceDataModelToXMLMap.put("CUSTOMERNAME", "CustomerName");
            invoiceDataModelToXMLMap.put("CUSTOMERADDRESS1", "CustomerAddress1");
            invoiceDataModelToXMLMap.put("CUSTOMERADDRESS2", "CustomerAddress2");
            invoiceDataModelToXMLMap.put("CUSTOMERADDRESS3", "CustomerAddress3");

            invoiceDataModelToXMLMap.put("SRNUMBER", "SaNumber");
            invoiceDataModelToXMLMap.put("SRENDDATE", "SraDate");
            invoiceDataModelToXMLMap.put("VENDORCOMMISSION", "VendorCommission");

            invoiceDataModelToXMLMap.put("COMPANYCODE", "CompanyCode");
            invoiceDataModelToXMLMap.put("INVOICESTATUS", "InvoiceStatus");
            invoiceDataModelToXMLMap.put("DISTRIBUTORNUMBER", "DistVendorNumber");

            xmlToInvoiceDataModelToMap = new Hashtable();
            for (Enumeration enumeration = invoiceDataModelToXMLMap.keys(); enumeration.hasMoreElements();)
            {
                String key = (String) enumeration.nextElement();
                xmlToInvoiceDataModelToMap.put(invoiceDataModelToXMLMap.get(key), key);
            }
        }
        return (invoiceDataModelToXMLMap);
    }

    private static Hashtable getXMLToInvoiceDataModelMap()
    {
        if (xmlToInvoiceDataModelToMap == null)
        {
            getInvoiceDataModelToXMLMap();
        }
        return xmlToInvoiceDataModelToMap;
    }

    private static Hashtable getLineItemDataModelToXMLMap()
    {
        if (lineItemDataModelToXMLMap == null)
        {
            InvoiceDetail_jaxb ajaxb = null;
            lineItemDataModelToXMLMap = new Hashtable();
            lineItemDataModelToXMLMap.put("INVOICEITEMTYPE", "InvoiceLineItemType");
            lineItemDataModelToXMLMap.put("TYPE", "MachineType");
            lineItemDataModelToXMLMap.put("MODEL", "MachineModel");
            lineItemDataModelToXMLMap.put("PARTNUMBER", "ManufacturerPartNumber");
            lineItemDataModelToXMLMap.put("LINENUMBER", "LineNumber");
            lineItemDataModelToXMLMap.put("QUANTITYasInteger", "Quantity");
            lineItemDataModelToXMLMap.put("SUBLINENUMBER", "LineSequenceNumber");
            lineItemDataModelToXMLMap.put("SERIAL", "SerialNumber");
            lineItemDataModelToXMLMap.put("UNITPRICEasBigDecimal", "UnitPrice");
            lineItemDataModelToXMLMap.put("MESNUMBER", "MesNumber");
            lineItemDataModelToXMLMap.put("VATCODE", "TaxCode");
            lineItemDataModelToXMLMap.put("VATAMOUNTasBigDecimal", "UnitTax");
            lineItemDataModelToXMLMap.put("PONUMBER", "PurchaseOrderNumber");
            lineItemDataModelToXMLMap.put("COSTCENTER", "CostCenter");
            lineItemDataModelToXMLMap.put("DESCRIPTION", "ItemDescription");
            lineItemDataModelToXMLMap.put("CONFIGUSEDasBool", "InConfiguration");
            lineItemDataModelToXMLMap.put("MESINDICATORasBool", "MesIndc");
            lineItemDataModelToXMLMap.put("TERM", "Term");
            lineItemDataModelToXMLMap.put("FINANCINGTYPE", "FinancingType");
            lineItemDataModelToXMLMap.put("PRODCAT", "ProdCat");
            lineItemDataModelToXMLMap.put("PRODTYPE", "ProdClass");
            lineItemDataModelToXMLMap.put("EQUIPSOURCE", "EquipmentSource");
           
            xmlToLineItemDataModelToMap = new Hashtable();
            for (Enumeration enumeration = lineItemDataModelToXMLMap.keys(); enumeration.hasMoreElements();)
            {
                String key = (String) enumeration.nextElement();
                xmlToLineItemDataModelToMap.put(lineItemDataModelToXMLMap.get(key), key);
            }
        }
        return lineItemDataModelToXMLMap;
    }

    private static Hashtable getXMLToLineItemDataModelMap()
    {
        if (xmlToLineItemDataModelToMap == null)
        {
            getLineItemDataModelToXMLMap();
        }
        return xmlToLineItemDataModelToMap;
    }

    private static Hashtable getCreateOfferingLetterDataModelToXMLMap()
    {
        if (createOfferingLetterDataModelToXMLMap == null)
        {
            createOfferingLetterDataModelToXMLMap = new Hashtable();
            createOfferingLetterDataModelToXMLMap.put("OBJECTID", "CmObjectId");
            createOfferingLetterDataModelToXMLMap.put("COUNTRY", "CountryCode");
            createOfferingLetterDataModelToXMLMap.put("OFFERINGNUMBER", "ExternalOlNum");
        }
        return createOfferingLetterDataModelToXMLMap;
    }

    private static Hashtable getOfferingLetterDataModelToXMLMap()
    {
        if (offeringLetterDataModelToXMLMap == null)
        {
            offeringLetterDataModelToXMLMap = new Hashtable();
            offeringLetterDataModelToXMLMap.put("OBJECTID", "CmObjectId");
            offeringLetterDataModelToXMLMap.put("OFFERINGNUMBER", "OfferLetterNumber");
            offeringLetterDataModelToXMLMap.put("AMOUNTasDouble", "AmtFinanced");
            offeringLetterDataModelToXMLMap.put("CURRENCY", "AmtFinCurrency");
            offeringLetterDataModelToXMLMap.put("COUNTRY", "CountryCode");
            offeringLetterDataModelToXMLMap.put("CUSTOMERNUMBER", "CustomerNumber");
            offeringLetterDataModelToXMLMap.put("CUSTOMERNAME", "CustomerName");
            offeringLetterDataModelToXMLMap.put("CUSTOMERADDRESS1", "CustomerAddress1");
            offeringLetterDataModelToXMLMap.put("CUSTOMERADDRESS2", "CustomerAddress2");
            offeringLetterDataModelToXMLMap.put("CUSTOMERADDRESS3", "CustomerAddress3");
            offeringLetterDataModelToXMLMap.put("CREATEDATEasLong", "ReceiptDate");
            offeringLetterDataModelToXMLMap.put("GCPSSTATUS", "Status");
            offeringLetterDataModelToXMLMap.put("LOCKINDICATOR", "IsLockIndc");
            offeringLetterDataModelToXMLMap.put("RESPONSIBLEPARTYID", "ResponsibleId");
            offeringLetterDataModelToXMLMap.put("SIGNEDCOUNTERSIGNED", "SignedOrCounterSigned");
            offeringLetterDataModelToXMLMap.put("VALIDITYENDDATE", "QuoteValidityEndDate");
            offeringLetterDataModelToXMLMap.put("VALIDITYSTARTDATE", "QuoteValidityStartDate");
            offeringLetterDataModelToXMLMap.put("CUSTOMERSIGNATURE", "CustomerSignature");
            offeringLetterDataModelToXMLMap.put("CUSTOMERSIGNATUREDATEasDate", "CustomerSignDate");
            offeringLetterDataModelToXMLMap.put("IBMSIGNATUREDATEasDate", "IGFSignDate");
            offeringLetterDataModelToXMLMap.put("EXTERNALOFFERLETTERNUMBER", "ExternalOfferLetterNumber");
            offeringLetterDataModelToXMLMap.put("SOURCEINDC", "SourceIndicator");
            offeringLetterDataModelToXMLMap.put("ROLLUPINDC", "RollupIndc");
            offeringLetterDataModelToXMLMap.put("QUOTENUMBER", "QuoteNum");
            offeringLetterDataModelToXMLMap.put("QUOTEVERSIONNUMBER", "QuoteVersionNum");

            xmlToOfferingLetterDataModelToMap = new Hashtable();
            for (Enumeration enumeration = offeringLetterDataModelToXMLMap.keys(); enumeration.hasMoreElements();)
            {
                String key = (String) enumeration.nextElement();
                xmlToOfferingLetterDataModelToMap.put(offeringLetterDataModelToXMLMap.get(key), key);
            }
        }
        return offeringLetterDataModelToXMLMap;
    }

    private static Hashtable getXMLToOfferingLetterDataModelMap()
    {
        if (xmlToOfferingLetterDataModelToMap == null)
        {
            getOfferingLetterDataModelToXMLMap();
        }
        return xmlToOfferingLetterDataModelToMap;
    }

    private static Hashtable getCommentsDataModelToXMLMap()
    {
        if (commentsDataModelToXMLMap == null)
        {
            commentsDataModelToXMLMap = new Hashtable();
            commentsDataModelToXMLMap.put("COMMENTNUMBER", "CommentSequenceNumber");
            commentsDataModelToXMLMap.put("USERID", "UserId");
            commentsDataModelToXMLMap.put("CREATED", "Created");
            commentsDataModelToXMLMap.put("COMMENTS", "CommentDescription");

            xmlToCommentsDataModelToMap = new Hashtable();
            for (Enumeration enumeration = commentsDataModelToXMLMap.keys(); enumeration.hasMoreElements();)
            {
                String key = (String) enumeration.nextElement();
                xmlToCommentsDataModelToMap.put(commentsDataModelToXMLMap.get(key), key);
            }
        }
        return commentsDataModelToXMLMap;
    }

    private static Hashtable getXMLToCommentsDataModelMap()
    {
        if (xmlToCommentsDataModelToMap == null)
        {
            getCommentsDataModelToXMLMap();
        }
        return xmlToCommentsDataModelToMap;
    }

    private static Hashtable getCOADataModelToXMLMap()
    {
        if (COADataModelToXMLMap == null)
        {
            COADataModelToXMLMap = new Hashtable();
            COADataModelToXMLMap.put("COANUMBER", "CoaNumber");
            COADataModelToXMLMap.put("COUNTRY", "CountryCode");
            COADataModelToXMLMap.put("AMOUNTasBigDecimal", "TotalFinancedAmount");
            COADataModelToXMLMap.put("CURRENCY", "Currency");
            COADataModelToXMLMap.put("CUSTOMERNAME", "CustomerName");
            COADataModelToXMLMap.put("CUSTOMERNUMBER", "CustomerNumber");
            COADataModelToXMLMap.put("OFFERINGLETTER", "OfferLetterNumber");
            COADataModelToXMLMap.put("OFFERINGLETTERVALIDITYDATEasLong", "QuoteValidityStartDate");
            COADataModelToXMLMap.put("LOCKINDICATOR", "IsLockIndc");
            COADataModelToXMLMap.put("SIGNEDBY", "COASignature");
            COADataModelToXMLMap.put("COASTATUS", "Status");

            xmlToCOADataModelToMap = new Hashtable();
            for (Enumeration enumeration = COADataModelToXMLMap.keys(); enumeration.hasMoreElements();)
            {
                String key = (String) enumeration.nextElement();
                xmlToCOADataModelToMap.put(COADataModelToXMLMap.get(key), key);
            }
        }
        return COADataModelToXMLMap;
    }
    
    private static Hashtable getMiscDataModelToXMLMap()
    {
        if (miscDataModelToXMLMap == null)
        {
        	miscDataModelToXMLMap = new Hashtable();
        	miscDataModelToXMLMap.put("COUNTRY", "CountryCode");
        	miscDataModelToXMLMap.put("OFFERINGNUMBER", "OfferLetterNumber");
        	miscDataModelToXMLMap.put("COANUMBER", "CoaNumber");
           	miscDataModelToXMLMap.put("INVOICENUMBER", "InvoiceNumber");
        	miscDataModelToXMLMap.put("USERID", "UserId");
        	miscDataModelToXMLMap.put("CREATEDATE", "ScanDate");        	
        }
        return miscDataModelToXMLMap;
    }    

    private static Hashtable getXMLToCOADataModelMap()
    {
        if (xmlToCOADataModelToMap == null)
        {
            getCOADataModelToXMLMap();
        }
        return xmlToCOADataModelToMap;
    }

    private static Hashtable getSoldToDataModelToXMLMap()
    {
        if (soldToDataModelToXMLMap == null)
        {
            soldToDataModelToXMLMap = new Hashtable();
            soldToDataModelToXMLMap.put("CUSTOMERNAME", "CustomerName");
            soldToDataModelToXMLMap.put("CUSTOMERNUMBER", "CustomerNumber");
            soldToDataModelToXMLMap.put("CUSTOMERADDRESS1", "AddressLine1");
            soldToDataModelToXMLMap.put("CUSTOMERADDRESS2", "AddressLine2");
            soldToDataModelToXMLMap.put("CUSTOMERADDRESS3", "AddressLine3");
            soldToDataModelToXMLMap.put("CUSTOMERADDRESS4", "AddressLine4");
            soldToDataModelToXMLMap.put("CUSTOMERADDRESS5", "AddressLine5");
            soldToDataModelToXMLMap.put("CUSTOMERADDRESS6", "AddressLine6");
            soldToDataModelToXMLMap.put("INSTALLEDCUSTOMERNAME", "CustomerName");
            soldToDataModelToXMLMap.put("INSTALLEDCUSTOMERNUMBER", "CustomerNumber");
            soldToDataModelToXMLMap.put("INSTALLEDCUSTOMERADDRESS1", "AddressLine1");
            soldToDataModelToXMLMap.put("INSTALLEDCUSTOMERADDRESS2", "AddressLine2");
            soldToDataModelToXMLMap.put("INSTALLEDCUSTOMERADDRESS3", "AddressLine3");
            soldToDataModelToXMLMap.put("INSTALLEDCUSTOMERADDRESS4", "AddressLine4");
            soldToDataModelToXMLMap.put("INSTALLEDCUSTOMERADDRESS5", "AddressLine5");
            soldToDataModelToXMLMap.put("INSTALLEDCUSTOMERADDRESS6", "AddressLine6");

            xmlToSoldToDataModelToMap = new Hashtable();
            for (Enumeration enumeration = soldToDataModelToXMLMap.keys(); enumeration.hasMoreElements();)
            {
                String key = (String) enumeration.nextElement();
                xmlToSoldToDataModelToMap.put(soldToDataModelToXMLMap.get(key), key);
            }
        }
        return soldToDataModelToXMLMap;
    }

    private static Hashtable getXMLToSoldToDataModelMap()
    {
        if (xmlToSoldToDataModelToMap == null)
        {
            getSoldToDataModelToXMLMap();
        }
        return xmlToSoldToDataModelToMap;
    }

    private static Hashtable getBillToDataModelToXMLMap()
    {
        if (billToDataModelToXMLMap == null)
        {
            billToDataModelToXMLMap = new Hashtable();
            billToDataModelToXMLMap.put("CUSTOMERNAME", "CustomerName");
            billToDataModelToXMLMap.put("CUSTOMERNUMBER", "CustomerNumber");
            billToDataModelToXMLMap.put("CUSTOMERADDRESS1", "AddressLine1");
            billToDataModelToXMLMap.put("CUSTOMERADDRESS2", "AddressLine2");
            billToDataModelToXMLMap.put("CUSTOMERADDRESS3", "AddressLine3");
            billToDataModelToXMLMap.put("CUSTOMERADDRESS4", "AddressLine4");
            billToDataModelToXMLMap.put("CUSTOMERADDRESS5", "AddressLine5");
            billToDataModelToXMLMap.put("CUSTOMERADDRESS6", "AddressLine6");

            xmlToBillToDataModelToMap = new Hashtable();
            for (Enumeration enumeration = billToDataModelToXMLMap.keys(); enumeration.hasMoreElements();)
            {
                String key = (String) enumeration.nextElement();
                xmlToBillToDataModelToMap.put(billToDataModelToXMLMap.get(key), key);
            }
        }
        return billToDataModelToXMLMap;
    }

    private static Hashtable getXMLToBillToDataModelMap()
    {
        if (xmlToBillToDataModelToMap == null)
        {
            getBillToDataModelToXMLMap();
        }
        return xmlToBillToDataModelToMap;
    }

    private static Hashtable getInstallToDataModelToXMLMap()
    {
        if (installToDataModelToXMLMap == null)
        {
            installToDataModelToXMLMap = new Hashtable();
            installToDataModelToXMLMap.put("INSTALLEDCUSTOMERNAME", "CustomerName");
            installToDataModelToXMLMap.put("INSTALLEDCUSTOMERNUMBER", "CustomerNumber");
            installToDataModelToXMLMap.put("INSTALLEDCUSTOMERADDRESS1", "AddressLine1");
            installToDataModelToXMLMap.put("INSTALLEDCUSTOMERADDRESS2", "AddressLine2");
            installToDataModelToXMLMap.put("INSTALLEDCUSTOMERADDRESS3", "AddressLine3");
            installToDataModelToXMLMap.put("INSTALLEDCUSTOMERADDRESS4", "AddressLine4");
            installToDataModelToXMLMap.put("INSTALLEDCUSTOMERADDRESS5", "AddressLine5");
            installToDataModelToXMLMap.put("INSTALLEDCUSTOMERADDRESS6", "AddressLine6");

            xmlToInstallToDataModelToMap = new Hashtable();
            for (Enumeration enumeration = installToDataModelToXMLMap.keys(); enumeration.hasMoreElements();)
            {
                String key = (String) enumeration.nextElement();
                xmlToInstallToDataModelToMap.put(installToDataModelToXMLMap.get(key), key);
            }
        }
        return installToDataModelToXMLMap;
    }

    private static Hashtable getXMLToInstallToDataModelMap()
    {
        if (xmlToInstallToDataModelToMap == null)
        {
            getInstallToDataModelToXMLMap();
        }
        return xmlToInstallToDataModelToMap;
    }

    private static Hashtable getOfferingLetterDataModelToXMLQuoteMap()
    {
        if (offeringLetterDataModelToXMLQuoteMap == null)
        {
            offeringLetterDataModelToXMLQuoteMap = new Hashtable();
            offeringLetterDataModelToXMLQuoteMap.put("CUSTOMERNAME", "CustomerName");
            offeringLetterDataModelToXMLQuoteMap.put("CUSTOMERNUMBER", "CustomerNum");
            offeringLetterDataModelToXMLQuoteMap.put("DEALTYPE", "DealType");
            //story -1497875
            offeringLetterDataModelToXMLQuoteMap.put("BILLENTITYINDICATOR", "BillEntityIndicator");

            xmlQuoteToOfferingLetterDataModelToMap = new Hashtable();
            for (Enumeration enumeration = offeringLetterDataModelToXMLQuoteMap.keys(); enumeration.hasMoreElements();)
            {
                String key = (String) enumeration.nextElement();
                xmlQuoteToOfferingLetterDataModelToMap.put(offeringLetterDataModelToXMLQuoteMap.get(key), key);
            }
        }
        return offeringLetterDataModelToXMLQuoteMap;
    }

    private static Hashtable getLineItemDataModelToXMLQuoteDetailMap()
    {
        if (xmlQuoteDetailToLineItemDataModelToMap == null)
        {
            xmlQuoteDetailToLineItemDataModelToMap = new Hashtable();
            xmlQuoteDetailToLineItemDataModelToMap.put("MachineType", "TYPE");
            xmlQuoteDetailToLineItemDataModelToMap.put("MachineModel", "MODEL");
            xmlQuoteDetailToLineItemDataModelToMap.put("NetPurchasePrice", "UNITPRICEasDouble");
            xmlQuoteDetailToLineItemDataModelToMap.put("MachineQuantity", "QUANTITYasInteger");
            xmlQuoteDetailToLineItemDataModelToMap.put("ItemNumber", "LINENUMBERasInteger");
            xmlQuoteDetailToLineItemDataModelToMap.put("SubItemNumber", "SUBLINENUMBERasInteger");
            xmlQuoteDetailToLineItemDataModelToMap.put("ExtensionIndicator", "EXTENSIONINDC");
            xmlQuoteDetailToLineItemDataModelToMap.put("TransactionType", "TRANSACTIONTYPE");
            xmlQuoteDetailToLineItemDataModelToMap.put("Term", "TERMasInteger");
            xmlQuoteDetailToLineItemDataModelToMap.put("TypeOfFinance", "FINANCINGTYPE");
            

        }
        return xmlQuoteDetailToLineItemDataModelToMap;
    }

    private static Hashtable getXMLQuoteToOfferingLetterDataModelMap()
    {
        if (xmlQuoteToOfferingLetterDataModelToMap == null)
        {
            getOfferingLetterDataModelToXMLQuoteMap();
        }

        return xmlQuoteToOfferingLetterDataModelToMap;
    }

    private static Hashtable getXMLQuoteDetailToLineItemDataModelMap()
    {
        if (xmlQuoteDetailToLineItemDataModelToMap == null)
        {
            getLineItemDataModelToXMLQuoteDetailMap();
        }

        return xmlQuoteDetailToLineItemDataModelToMap;
    }

}
