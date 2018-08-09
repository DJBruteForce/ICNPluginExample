///**
// *  
// */
package com.ibm.igf.webservice.rdc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.MarshalException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
//import javax.xml.crypto.MarshalException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import com.ibm.gcms.consumer.RDCConsumer;
import com.ibm.gil.exception.GILCNException;
import com.ibm.gil.util.UtilGilConstants;
import com.ibm.igf.gil.CountryProperties;
import com.ibm.igf.hmvc.CountryManager;
import com.ibm.igf.hmvc.PluginConfigurationPane;
import com.ibm.igf.hmvc.WebServicesEnpointManager;
import com.ibm.igf.hmvc.WebServicesEnpointManager.SYSTEMS;

import financing.tools.gcps.common.domain.Customer;
import financing.tools.gcps.common.domain.Customer_impl;
import financing.tools.gcps.common.exceptions.GcpsException;
import financing.tools.gcps.common.keys.GcpsMessageKeys;

//import com.ibm.igf.gil.DefaultPropertyManager;

import financing.tools.gcps.xml.jaxb.rdc.retrieve.request.RetrieveXML;
import financing.tools.gcps.xml.jaxb.rdc.retrieve.request.StateProvCde;
import financing.tools.gcps.xml.jaxb.rdc.retrieve.request.impl.CityImpl;
import financing.tools.gcps.xml.jaxb.rdc.retrieve.request.impl.CountryKeyImpl;
import financing.tools.gcps.xml.jaxb.rdc.retrieve.request.impl.CoverageTypeImpl;
import financing.tools.gcps.xml.jaxb.rdc.retrieve.request.impl.CusClassImpl;
import financing.tools.gcps.xml.jaxb.rdc.retrieve.request.impl.DunsNumberImpl;
import financing.tools.gcps.xml.jaxb.rdc.retrieve.request.impl.FaxNumberImpl;
import financing.tools.gcps.xml.jaxb.rdc.retrieve.request.impl.InacNumberImpl;
import financing.tools.gcps.xml.jaxb.rdc.retrieve.request.impl.IndCatCodeImpl;
import financing.tools.gcps.xml.jaxb.rdc.retrieve.request.impl.IsuCodeImpl;
import financing.tools.gcps.xml.jaxb.rdc.retrieve.request.impl.LanguageCodeImpl;
import financing.tools.gcps.xml.jaxb.rdc.retrieve.request.impl.MppIdImpl;
import financing.tools.gcps.xml.jaxb.rdc.retrieve.request.impl.NumEmpforYrImpl;
import financing.tools.gcps.xml.jaxb.rdc.retrieve.request.impl.POBoxCityImpl;
import financing.tools.gcps.xml.jaxb.rdc.retrieve.request.impl.POBoxImpl;
import financing.tools.gcps.xml.jaxb.rdc.retrieve.request.impl.POBoxPostalCodeImpl;
import financing.tools.gcps.xml.jaxb.rdc.retrieve.request.impl.PostalCodeImpl;
import financing.tools.gcps.xml.jaxb.rdc.retrieve.request.impl.RetrieveInputImpl;
import financing.tools.gcps.xml.jaxb.rdc.retrieve.request.impl.RetrieveXMLImpl;
import financing.tools.gcps.xml.jaxb.rdc.retrieve.request.impl.StateProvDescImpl;
import financing.tools.gcps.xml.jaxb.rdc.retrieve.request.impl.StreetAddress1Impl;
import financing.tools.gcps.xml.jaxb.rdc.retrieve.request.impl.StreetAddress2Impl;
import financing.tools.gcps.xml.jaxb.rdc.retrieve.request.impl.TaxExemptIndImpl;
import financing.tools.gcps.xml.jaxb.rdc.retrieve.request.impl.TaxationClassImpl;
import financing.tools.gcps.xml.jaxb.rdc.retrieve.request.impl.TelephoneNumberImpl;
import financing.tools.gcps.xml.jaxb.rdc.retrieve.request.impl.UnSicCodeImpl;
import financing.tools.gcps.xml.jaxb.rdc.retrieve.response.RetrieveOutput;
import financing.tools.gcps.xml.jaxb.rdc.retrieve.response.impl.RetrieveErrorImpl;
import financing.tools.gcps.xml.jaxb.rdc.retrieve.response.impl.RetrieveOutputImpl;
import financing.tools.gcps.xml.jaxb.rdc.search.request.City;
import financing.tools.gcps.xml.jaxb.rdc.search.request.CountryKey;
import financing.tools.gcps.xml.jaxb.rdc.search.request.CustomerName;
import financing.tools.gcps.xml.jaxb.rdc.search.request.ObjectFactory;
import financing.tools.gcps.xml.jaxb.rdc.search.request.OutputListType;
import financing.tools.gcps.xml.jaxb.rdc.search.request.SearchXML;
import financing.tools.gcps.xml.jaxb.rdc.search.request.impl.BODIDImpl;
import financing.tools.gcps.xml.jaxb.rdc.search.request.impl.CMRLegSysCtryImpl;
import financing.tools.gcps.xml.jaxb.rdc.search.request.impl.CentralOrderBlockImpl;
import financing.tools.gcps.xml.jaxb.rdc.search.request.impl.ClientNameImpl;
import financing.tools.gcps.xml.jaxb.rdc.search.request.impl.CustomerName2Impl;
import financing.tools.gcps.xml.jaxb.rdc.search.request.impl.CustomerNameImpl;
import financing.tools.gcps.xml.jaxb.rdc.search.request.impl.DPLIndImpl;
import financing.tools.gcps.xml.jaxb.rdc.search.request.impl.ExtendedHeaderImpl;
import financing.tools.gcps.xml.jaxb.rdc.search.request.impl.InputListTypeImpl;
import financing.tools.gcps.xml.jaxb.rdc.search.request.impl.LegCMRCusNumImpl;
import financing.tools.gcps.xml.jaxb.rdc.search.request.impl.LogicalDeleteFlagImpl;
import financing.tools.gcps.xml.jaxb.rdc.search.request.impl.MaxRowsImpl;
import financing.tools.gcps.xml.jaxb.rdc.search.request.impl.MppIdAcctGrpImpl;
import financing.tools.gcps.xml.jaxb.rdc.search.request.impl.OutputListTypeImpl;
import financing.tools.gcps.xml.jaxb.rdc.search.request.impl.QueryNameImpl;
import financing.tools.gcps.xml.jaxb.rdc.search.request.impl.SearchInputImpl;
import financing.tools.gcps.xml.jaxb.rdc.search.request.impl.SearchXMLImpl;
import financing.tools.gcps.xml.jaxb.rdc.search.response.ExtendedHeader;
import financing.tools.gcps.xml.jaxb.rdc.search.response.SearchOutput;
import financing.tools.gcps.xml.jaxb.rdc.search.response.impl.OutputListImpl;
import financing.tools.gcps.xml.jaxb.rdc.search.response.impl.SearchErrorImpl;
import financing.tools.gcps.xml.jaxb.rdc.search.response.impl.SearchOutputImpl;

public class RDcService_impl  {
	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(RDcService_impl.class);
	private static final org.apache.logging.log4j.Logger loggerWS = org.apache.logging.log4j.LogManager.getLogger("WsLogger");

//    /**
//     * The Implementation of the RDcService.
//     */
//    private static final String APP_LOG = RDcService_impl.class.getName();
//    private static final String ENTRY_LOG = new StringBuffer("entry.").append(APP_LOG).toString();
//    private static final String EXIT_LOG = new StringBuffer("exit.").append(APP_LOG).toString();
//    //private static LogContext logCtx =
//    //		LogContextFactory.singleton().getLogContext();
    private String clientName = "IGFELS";
    private List emptyMppList;
    private List interfaceRequestList = new ArrayList();
    private List mppList;
    private String retrieveRequestPackageName = "financing.tools.gcps.xml.jaxb.rdc.retrieve.request";
    private String retrieveResponsePackageName = "financing.tools.gcps.xml.jaxb.rdc.retrieve.response";
    private String searchRequestPackageName = "financing.tools.gcps.xml.jaxb.rdc.search.request";
    private String searchResponsePackageName = "financing.tools.gcps.xml.jaxb.rdc.search.response";
    private RDCConsumer rdcConsumer=null;
    private CountryProperties countryProperties = null;
	
    
	private String urlService=null;
	    
	    private String getUrlWS(){
	    	    
	    	if(urlService==null){
	    		
	    		urlService=PluginConfigurationPane.getPropertiesMap().get(UtilGilConstants.RDC_SERVER);
	    	}
	    	logger.debug("URL Service: "+urlService);
	    	return urlService ;
	 }

    public RDcService_impl(String country)
    {
    	countryProperties=CountryManager.getCountryProperties(country);

    	//rdcConsumer=new RDCConsumer(getUrlWS());
    	rdcConsumer=new RDCConsumer(WebServicesEnpointManager.getDefaultEndpoint(SYSTEMS.RDC).getWSUrl());
    	//"https://ibmrrdc2.portsmouth.uk.ibm.com/SRServiceHTTPRouter/services/SRServiceBSI");
        
    }

    /**
     * checkForDuplicateCustomers method.
     * 
     * @param customerList
     *            List
     * 
     * @return List
     */
    private List checkForDuplicateCustomers(List customerList)
    {
        int listCount = customerList.size();
        Set customersSet = null;
        List list = new ArrayList();
        Iterator iter = null;
        //logCtx.info(ENTRY_LOG, "List checkForDuplicateCustomers(List
        // customerList)");
        customersSet = new HashSet();
        boolean found = false;
        iter = customerList.iterator();
        while (iter.hasNext())
        {
            Customer customer = (Customer_impl) iter.next();

            // check if customer id is a duplicate.
            Iterator custSetItr = customersSet.iterator();
            found = false;
            while (custSetItr.hasNext())
            {
                Customer custObj = (Customer) custSetItr.next();
                if (custObj.getSofCustomerNumber() != null && customer.getSofCustomerNumber() != null && custObj.getSofCustomerNumber().trim().equals(customer.getSofCustomerNumber().trim()))
                {
                    /*
                     * customer.setError(true);
                     * customer.getErrorCodes().add(GcpsMessageKeys.GCPS_CLIENT_DUPLICATE_CUSTOMER);
                     */
                    found = true;
                    break;
                    //logCtx.error(APP_LOG,
                    //	"checkForDuplicateCustomers: Duplicate customer found for
                    // "
                    //	+ customer.getSofCustomerNumber());
                }
            }
            if ((!found) && (!customer.getError()))
            {
                customersSet.add(customer);
            }
        }
        list.addAll(customersSet);

        //logCtx.info(EXIT_LOG, "List checkForDuplicateCustomers(List
        // customerList)");

        return list;
    }

    /**
     * Method called to create the HashMap returned to the Server Code
     * 
     * @param customerList
     *            List
     * 
     * @return customerMap HashMap
     */
    private HashMap createCustomerHashMap(List customerList)
    {
        //logCtx.info(ENTRY_LOG, "HashMap createCustomerHashMap(List
        // customerList)");
        Iterator i = customerList.iterator();
        HashMap customerMap = new HashMap();
        while (i.hasNext())
        {
            Customer_impl customer = (Customer_impl) i.next();
            customerMap.put(customer.getSofCustomerNumber(), customer);
        }
        //logCtx.debug(APP_LOG, "Customer Map Size" + customerMap.size());
        //logCtx.info(EXIT_LOG, "HashMap createCustomerHashMap(List
        // customerList)");
        return customerMap;
    }

    /**
     * Method called by the Server. Marshals Request and Unmarshals Response
     * 
     * @param retrieveResponse
     *            InterfaceRetrieveResponse
     * @return customerList List
     */
    private List createCustomers(InterfaceRetrieveResponse retrieveResponse)
    {
        //logCtx.info(ENTRY_LOG,
        //	 "List createCustomers(InterfaceRetrieveResponse retrieveResponse)");
        List customerList = new ArrayList();

        /**
         * Check if the RetrieveResponse XML is empty
         */
        if (!retrieveResponse.getRetrieveResponseError().isEmpty())
        {
            Iterator j = retrieveResponse.getRetrieveResponseError().iterator();
            while (j.hasNext())
            {
                List errorCodesList = new ArrayList();
                InterfaceRetrieveResponseError errorOutput = (InterfaceRetrieveResponseError) j.next();
                Customer_impl customer = new Customer_impl();
                customer.setSofCustomerNumber(errorOutput.getLegacyCustomerNumber());
                customer.setError(true);
                errorCodesList.add(GcpsMessageKeys.GCPS_CLIENT_CUSTOMER_NOT_FOUND);
                customer.setErrorCodes(errorCodesList);
                customerList.add(customer);
                //logCtx.error(APP_LOG, "Customer Not Found");
            }
        }
        /**
         * If the RetriveResponse is not NOT Empty & Valid
         */
        Iterator i = retrieveResponse.getRetrieveResponseOutput().iterator();
        if (!retrieveResponse.getRetrieveResponseOutput().isEmpty())
        {
            while (i.hasNext())
            {
                InterfaceRetrieveResponseOutput output = (InterfaceRetrieveResponseOutput) i.next();
                Customer_impl customer = new Customer_impl();
                customer.setAddress1(output.getOutputList().getStreetAddress1());
                customer.setAddress2(output.getOutputList().getStreetAddress2());
                customer.setCentralOrderBlockFlag(output.getOutputList().getCentralOrderBlock());
                customer.setCity(output.getOutputList().getCity());
                customer.setClassification(output.getOutputList().getCusClass());
                customer.setCountryCode(output.getOutputList().getCMRLegSysCtry());
                customer.setCustomerName(output.getOutputList().getCustomerName());
                customer.setCustomerName2(output.getOutputList().getCustomerName2());
                customer.setDunsNumber(output.getOutputList().getDunsNumber());
                customer.setError(false);
                customer.setFax(output.getOutputList().getFaxNo());
                customer.setIndustryCategoryCode(output.getOutputList().getIndCatCode());
                customer.setLegacyCountryCode(output.getOutputList().getCMRLegSysCtry());
                customer.setLogicalDeleteFlag(output.getOutputList().getLogicalDeleteFlg());
                customer.setMppCustomerNumber(output.getOutputList().getMppId());
                if (output.getOutputList().getNumEmpforYr().equals(""))
                {
                    customer.setNumberOfEmployees(0);
                } else
                {
                    customer.setNumberOfEmployees(Integer.parseInt(output.getOutputList().getNumEmpforYr()));
                }
                customer.setPoBox(output.getOutputList().getPoBox());
                customer.setPoBoxCity(output.getOutputList().getPoBoxCity());
                customer.setPoBoxPostalCode(output.getOutputList().getPOBoxPostalCode());
                customer.setPostalCode(output.getOutputList().getPostalCode());
                customer.setSofCustomerNumber(output.getOutputList().getLegCMRCusNum());
                customer.setState(output.getOutputList().getStateProvCde());
                customer.setStateDescription(output.getOutputList().getStateProvDesc());
                customer.setTaxationClass(output.getOutputList().getTaxationClass());
                customer.setTaxationExemptIndicator(output.getOutputList().getTaxExemptInd());
                customer.setTelephone(output.getOutputList().getTelephoneNo());
                customer.setBranchOffice(output.getOutputList().getBranchOffice());
                customer.setIndustrySolutionCode(output.getOutputList().getIsuCode());
                customer.setDeniedPartyIndicator(output.getOutputList().getDPLInd());
                customer.setStandardIndustryClassificationCode(output.getOutputList().getUnsiCode());
                customer = validateCustomer(customer);
                customerList.add(customer);
            }
            /**
             * Error in the RetrieveResponse XML
             */
            Iterator k = getEmptyMppList().iterator();
            while (k.hasNext())
            {
                CustomerXref custXref = (CustomerXref) k.next();
                List errorCodesList = new ArrayList();
                Customer_impl customer = new Customer_impl();
                customer.setLegacyCountryCode(custXref.getLegacyCountryCode());
                customer.setSofCustomerNumber(custXref.getLegacyCustomerNum());
                customer.setError(true);
                errorCodesList.add(GcpsMessageKeys.GCPS_CLIENT_RDC_INTERNAL_ERROR);
                customer.setErrorCodes(errorCodesList);
                customerList.add(customer);
                //logCtx.error(APP_LOG, "RDcInternalError");
            }
        }
        customerList = checkForDuplicateCustomers(customerList);
        //logCtx.info(EXIT_LOG,
        //	 "List createCustomers(InterfaceRetrieveResponse retrieveResponse)");
        return customerList;
    }

    /**
     * Method called by the Server. Marshals Request and Unmarshals Response
     * 
     * @param str
     *            String
     * @return xmlDoc Document
     * @throws GcpsException
     */
    private Document createDocument(String str) throws Exception
    {
        //logCtx.info(ENTRY_LOG, "Document createDocument(String str)");
        Document xmlDoc = null;
        try
        {
            DocumentBuilderFactory dFactory = DocumentBuilderFactory.newInstance();
            dFactory.setNamespaceAware(true);
            DocumentBuilder dBuilder = dFactory.newDocumentBuilder();

            // xml needs to be char-encoded in UTF-8 format
            ByteArrayInputStream is = new ByteArrayInputStream(str.getBytes("UTF8"));

            xmlDoc = dBuilder.parse(is);
        } catch (Exception e)
        {
            logger.error( "createDocument: Exception found",e);
        }
        //logCtx.info(EXIT_LOG, "Document createDocument(String str)");
        return xmlDoc;
    }

    private List createEmtpyCustomers(List emptyList)
    {
        List customerList = new ArrayList();
        Iterator i = emptyList.iterator();
        while (i.hasNext())
        {
            Customer_impl customer = new Customer_impl();
            CustomerXref xref = (CustomerXref) i.next();
            customer.setError(true);
            customer.setLegacyCountryCode(xref.getLegacyCountryCode());
            customer.setSofCustomerNumber(xref.getLegacyCustomerNum());
            List errorList = new ArrayList();
            errorList.add(GcpsMessageKeys.GCPS_CLIENT_CUSTOMER_NOT_FOUND);
            customer.setErrorCodes(errorList);
            customerList.add(customer);
        }
        return customerList;
    }

    /**
     * Create Search Error customer List. And set the error to Customer not
     * found.
     * 
     * @param searchError
     *            List
     * @return errorList List
     */
    private List createErrorCustomers(List searchError)
    {
        //logCtx.info(ENTRY_LOG, "createErrorCustomers(List searchError)");
        List errorList = new ArrayList();
        List errorCodes = new ArrayList();
        Iterator i = searchError.iterator();
        while (i.hasNext())
        {
            Customer_impl customer = new Customer_impl();
            InterfaceSearchResponseError responseError = (InterfaceSearchResponseError) i.next();
            customer.setError(true);
            customer.setSofCustomerNumber(responseError.getResponseInputList().getLegCMRCusNum());
            errorCodes.add(GcpsMessageKeys.GCPS_CLIENT_CUSTOMER_NOT_FOUND);
            customer.setErrorCodes(errorCodes);
            errorList.add(customer);

        }
        //logCtx.info(EXIT_LOG, "createErrorCustomers(List searchError)");
        return errorList;
    }

    /**
     * Creates Application and sets data from request
     * 
     * @param mpp
     *            String
     * @param searchInput
     *            RetrieveInputImpl
     * @return retrieveObjectFactory ObjectFactory
     * @throws JAXBException
     */
    private financing.tools.gcps.xml.jaxb.rdc.retrieve.request.impl.InputListTypeImpl createRetrieveRequestInputList(String mpp, RetrieveInputImpl searchInput, financing.tools.gcps.xml.jaxb.rdc.retrieve.request.ObjectFactory retrieveObjectFactory)
            throws JAXBException
    {
        //logCtx.info(ENTRY_LOG, "
        // createRetrieveRequestInputList(mpp,RetrieveInputImpl,retrieveObjectFactory
        // ");

        /** Create a new InputListTypeImpl element */
        financing.tools.gcps.xml.jaxb.rdc.retrieve.request.impl.InputListTypeImpl inputList = (financing.tools.gcps.xml.jaxb.rdc.retrieve.request.impl.InputListTypeImpl) retrieveObjectFactory.createInputList();

        MppIdImpl mppId = (MppIdImpl) retrieveObjectFactory.createMppId();
        inputList.setMppId(mpp);
        //logCtx.info(ENTRY_LOG, "
        // createRetrieveRequestInputList(mpp,RetrieveInputImpl,retrieveObjectFactory
        // ");
        return inputList;
    }

    /**
     * Creates Retrieve Request OutputList
     * 
     * @param interfaceRequest
     * @param retriveObjectFactor
     *            ObjectFactory
     * @param interfaceRequest
     *            InterfaceRequest
     * @return outputList OutputListTypeImpl
     * @throws JAXBException
     */
    private financing.tools.gcps.xml.jaxb.rdc.retrieve.request.impl.OutputListTypeImpl createRetrieveRequestOutputList(InterfaceRequest interfaceRequest, RetrieveInputImpl retrieveInput,
            financing.tools.gcps.xml.jaxb.rdc.retrieve.request.ObjectFactory retrieveObjectFactory) throws JAXBException
    {
        //logCtx.info(ENTRY_LOG, "
        // createRetrieveRequestOutputList(interfaceRequest," +
        //	"retrieveInput, objectFactory");

        /** Create a new InputList element */
        financing.tools.gcps.xml.jaxb.rdc.retrieve.request.impl.OutputListTypeImpl outputList = (financing.tools.gcps.xml.jaxb.rdc.retrieve.request.impl.OutputListTypeImpl) retrieveObjectFactory.createOutputList();
        financing.tools.gcps.xml.jaxb.rdc.retrieve.request.OutputListType list = retrieveInput.getOutputList();

        MppIdImpl mppId = (MppIdImpl) retrieveObjectFactory.createMppId("");
        financing.tools.gcps.xml.jaxb.rdc.retrieve.request.impl.CustomerNameImpl custName = (financing.tools.gcps.xml.jaxb.rdc.retrieve.request.impl.CustomerNameImpl) retrieveObjectFactory.createCustomerName("");
        financing.tools.gcps.xml.jaxb.rdc.retrieve.request.impl.CustomerName2Impl custName2 = (financing.tools.gcps.xml.jaxb.rdc.retrieve.request.impl.CustomerName2Impl) retrieveObjectFactory.createCustomerName2("");
        financing.tools.gcps.xml.jaxb.rdc.retrieve.request.impl.CustomerName3Impl custName3 = (financing.tools.gcps.xml.jaxb.rdc.retrieve.request.impl.CustomerName3Impl) retrieveObjectFactory.createCustomerName3("");
        financing.tools.gcps.xml.jaxb.rdc.retrieve.request.impl.LegCMRCusNumImpl cusNum = (financing.tools.gcps.xml.jaxb.rdc.retrieve.request.impl.LegCMRCusNumImpl) retrieveObjectFactory.createLegCMRCusNum("");
        financing.tools.gcps.xml.jaxb.rdc.retrieve.request.impl.CentralOrderBlockImpl cob = (financing.tools.gcps.xml.jaxb.rdc.retrieve.request.impl.CentralOrderBlockImpl) retrieveObjectFactory.createCentralOrderBlock("");
        financing.tools.gcps.xml.jaxb.rdc.retrieve.request.impl.CMRLegSysCtryImpl cmrCtry = (financing.tools.gcps.xml.jaxb.rdc.retrieve.request.impl.CMRLegSysCtryImpl) retrieveObjectFactory.createCMRLegSysCtry("");
        financing.tools.gcps.xml.jaxb.rdc.retrieve.request.impl.DPLIndImpl dblind = (financing.tools.gcps.xml.jaxb.rdc.retrieve.request.impl.DPLIndImpl) retrieveObjectFactory.createDPLInd("");
        financing.tools.gcps.xml.jaxb.rdc.retrieve.request.impl.LogicalDeleteFlagImpl logFlg = (financing.tools.gcps.xml.jaxb.rdc.retrieve.request.impl.LogicalDeleteFlagImpl) retrieveObjectFactory.createLogicalDeleteFlag("");
        financing.tools.gcps.xml.jaxb.rdc.retrieve.request.impl.MppIdAcctGrpImpl accGrp = (financing.tools.gcps.xml.jaxb.rdc.retrieve.request.impl.MppIdAcctGrpImpl) retrieveObjectFactory.createMppIdAcctGrp("");
        StreetAddress1Impl address1 = (StreetAddress1Impl) retrieveObjectFactory.createStreetAddress1("");
        StreetAddress2Impl address2 = (StreetAddress2Impl) retrieveObjectFactory.createStreetAddress2("");
        CityImpl city = (CityImpl) retrieveObjectFactory.createCity("");
        StateProvCde provCde = (StateProvCde) retrieveObjectFactory.createStateProvCde("");
        PostalCodeImpl postalCode = (PostalCodeImpl) retrieveObjectFactory.createPostalCode("");
        CountryKeyImpl key = (CountryKeyImpl) retrieveObjectFactory.createCountryKey("");
        TelephoneNumberImpl telephone = (TelephoneNumberImpl) retrieveObjectFactory.createTelephoneNumber("");
        FaxNumberImpl fax = (FaxNumberImpl) retrieveObjectFactory.createFaxNumber("");
        LanguageCodeImpl lang = (LanguageCodeImpl) retrieveObjectFactory.createLanguageCode("");
        NumEmpforYrImpl emp = (NumEmpforYrImpl) retrieveObjectFactory.createNumEmpforYr("");
        CusClassImpl cusClass = (CusClassImpl) retrieveObjectFactory.createCusClass("");
        TaxationClassImpl taxClass = (TaxationClassImpl) retrieveObjectFactory.createTaxationClass("");
        TaxExemptIndImpl taxExempt = (TaxExemptIndImpl) retrieveObjectFactory.createTaxExemptInd("");
        IndCatCodeImpl indCat = (IndCatCodeImpl) retrieveObjectFactory.createIndCatCode("");
        IsuCodeImpl isu = (IsuCodeImpl) retrieveObjectFactory.createIsuCode("");
        /**
         * Removed for Release 1.0 CW BranchOfficeImpl branchOffice =
         * (BranchOfficeImpl) retrieveObjectFactory.createBranchOffice("");
         */

        DunsNumberImpl duns = (DunsNumberImpl) retrieveObjectFactory.createDunsNumber("");
        CoverageTypeImpl coverageType = (CoverageTypeImpl) retrieveObjectFactory.createCoverageType("");
        /**
         * Removed for Release 1.0 CW CoverageIDImpl converageId =
         * (CoverageIDImpl)retrieveObjectFactory.createCoverageID("");
         */
        StateProvDescImpl stateDesc = (StateProvDescImpl) retrieveObjectFactory.createStateProvDesc("");
        POBoxImpl poBox = (POBoxImpl) retrieveObjectFactory.createPOBox("");
        POBoxCityImpl poCity = (POBoxCityImpl) retrieveObjectFactory.createPOBoxCity("");
        POBoxPostalCodeImpl poPostalCode = (POBoxPostalCodeImpl) retrieveObjectFactory.createPOBoxPostalCode("");
        InacNumberImpl inac = (InacNumberImpl) retrieveObjectFactory.createInacNumber("");
        UnSicCodeImpl unSic = (UnSicCodeImpl) retrieveObjectFactory.createUnSicCode("");
        outputList.setMppId(mppId.getValue());
        outputList.setCentralOrderBlock(cob.getValue());
        outputList.setCMRLegSysCtry(cmrCtry.getValue());
        outputList.setCustomerName(custName.getValue());
        outputList.setCustomerName2(custName2.getValue());
        outputList.setCustomerName3(custName3.getValue());
        outputList.setDPLInd(dblind.getValue());
        outputList.setLegCMRCusNum(cusNum.getValue());
        outputList.setLogicalDeleteFlag(logFlg.getValue());
        outputList.setMppIdAcctGrp(accGrp.getValue());
        outputList.setStreetAddress1(address1.getValue());
        outputList.setStreetAddress2(address2.getValue());
        outputList.setCity(city.getValue());
        outputList.setStateProvCde(provCde.getValue());
        outputList.setPostalCode(postalCode.getValue());
        outputList.setCountryKey(key.getValue());
        outputList.setTelephoneNumber(telephone.getValue());
        outputList.setFaxNumber(fax.getValue());
        outputList.setLanguageCode(lang.getValue());
        outputList.setNumEmpforYr(emp.getValue());
        outputList.setCusClass(cusClass.getValue());
        outputList.setTaxationClass(taxClass.getValue());
        outputList.setTaxExemptInd(taxExempt.getValue());
        outputList.setInacNumber(inac.getValue());
        outputList.setIndCatCode(indCat.getValue());
        outputList.setIsuCode(isu.getValue());
        outputList.setUnSicCode(unSic.getValue());
        /**
         * Removed for Release 1.0 CW
         * outputList.setBranchOffice(branchOffice.getValue());
         */
        outputList.setDunsNumber(duns.getValue());
        /**
         * Removed for Release 1.0 CW
         * outputList.setCoverageID(converageId.getValue());
         */
        outputList.setCoverageType(coverageType.getValue());
        outputList.setStateProvDesc(stateDesc.getValue());
        outputList.setPOBox(poBox.getValue());
        outputList.setPOBoxCity(poCity.getValue());
        outputList.setPOBoxPostalCode(poPostalCode.getValue());

        //logCtx.info(EXIT_LOG,
        // "createRetrieveRequestOutputList(interfaceRequest, retrieveInput, " +
        //	"objectFactory)");
        return outputList;
    }

    /**
     * Create Retrieve Response Error
     * 
     * @param retrieveError
     *            RetrieveErrorImpl
     * @return error InterfaceRetrieveResponseError
     */
    private InterfaceRetrieveResponseError createRetrieveResponseError(RetrieveErrorImpl retrieveError)
    {
        //logCtx.info(ENTRY_LOG, "
        // createRetrieveResponseError(retrieveError)");
        InterfaceRetrieveResponseError error = new InterfaceRetrieveResponseError();
        error.setErrorCode(retrieveError.getErrorCode());
        error.setErrorDetails(retrieveError.getErrorDetails());
        error.setErrorMessage(retrieveError.getErrorMessage());

        if (retrieveError.getInputList() != null)
        {
            error.setMppId(retrieveError.getInputList().getMppId());
            //	error.setLegCMRCusNum());
            //	error.setResponseInputList();
        }
        //logCtx.info(EXIT_LOG, " createRetrieveResponseError(retrieveError)");
        return error;
    }

    /**
     * Creates Interface Retrieve Response Output
     * 
     * @param retrieveOutput
     *            RetrieveOutput
     * @return newOutput InterfaceRetrieveResponseOutput
     */
    private InterfaceRetrieveResponseOutput createRetrieveResponseOutput(RetrieveOutput retrieveOutput)
    {
        //logCtx.info(ENTRY_LOG, "
        // InterfaceRetrieveResponseOutput(retrieveOutput)");
        InterfaceRetrieveResponseOutputList output = new InterfaceRetrieveResponseOutputList();
        output.setStreetAddress1(retrieveOutput.getOutputList().getStreetAddress1());
        output.setStreetAddress2(retrieveOutput.getOutputList().getStreetAddress2());
        output.setStateProvCde(retrieveOutput.getOutputList().getStateProvCde());
        output.setStateProvDesc(retrieveOutput.getOutputList().getStateProvDesc());
        output.setCentralOrderBlock(retrieveOutput.getOutputList().getCentralOrderBlock());
        output.setCMRLegSysCtry(retrieveOutput.getOutputList().getCMRLegSysCtry());
        output.setCustomerName(retrieveOutput.getOutputList().getCustomerName());
        output.setCustomerName2(retrieveOutput.getOutputList().getCustomerName2());
        output.setDPLInd(retrieveOutput.getOutputList().getDPLInd());
        output.setLegCMRCusNum(retrieveOutput.getOutputList().getLegCMRCusNum());
        output.setLogicalDeleteFlg(retrieveOutput.getOutputList().getLogicalDeleteFlag());
        output.setMppId(retrieveOutput.getOutputList().getMppId());
        output.setBranchOffice(retrieveOutput.getOutputList().getBranchOffice());
        output.setCity(retrieveOutput.getOutputList().getCity());
        output.setCountryCode(retrieveOutput.getOutputList().getCountryKey());
        output.setCoverageType(retrieveOutput.getOutputList().getCoverageType());
        output.setCusClass(retrieveOutput.getOutputList().getCusClass());
        output.setCustomerName3(retrieveOutput.getOutputList().getCustomerName3());
        output.setDunsNumber(retrieveOutput.getOutputList().getDunsNumber());
        output.setFaxNo(retrieveOutput.getOutputList().getFaxNumber());
        output.setInacNum(retrieveOutput.getOutputList().getInacNumber());
        output.setIndCatCode(retrieveOutput.getOutputList().getIndCatCode());
        output.setIsuCode(retrieveOutput.getOutputList().getIsuCode());
        output.setLanguageCode(retrieveOutput.getOutputList().getLanguageCode());
        output.setNumEmpforYr(retrieveOutput.getOutputList().getNumEmpforYr());
        output.setPoBox(retrieveOutput.getOutputList().getPOBox());
        output.setPoBoxCity(retrieveOutput.getOutputList().getPOBoxCity());
        output.setPOBoxPostalCode(retrieveOutput.getOutputList().getPOBoxPostalCode());
        output.setPostalCode(retrieveOutput.getOutputList().getPostalCode());
        output.setUnsiCode(retrieveOutput.getOutputList().getUnSicCode());

        InterfaceRetrieveResponseOutput newOutput = new InterfaceRetrieveResponseOutput();
        newOutput.setOutputList(output);
        //logCtx.info(EXIT_LOG, "
        // InterfaceRetrieveResponseOutput(retrieveOutput)");
        return newOutput;
    }

    /**
     * Creates RetriveXML and sets data from request
     * 
     * @param mppIds
     *            List
     * @return RetrieveXML retrieveXML
     * @throws JAXBException
     */
    private RetrieveXML createRetrieveXML(List mppIds) throws JAXBException
    {
        //logCtx.info(ENTRY_LOG, "RetrieveXML createRetrieveXML(mppIds");
        financing.tools.gcps.xml.jaxb.rdc.retrieve.request.ObjectFactory retrieveObjectFactory = new financing.tools.gcps.xml.jaxb.rdc.retrieve.request.ObjectFactory();

        /** Create a new retrieveXML element */
        RetrieveXMLImpl retrieveXML = (RetrieveXMLImpl) retrieveObjectFactory.createRetrieveXML();
        /** Create a new ClientName element */
        financing.tools.gcps.xml.jaxb.rdc.retrieve.request.impl.ClientNameImpl clientName = (financing.tools.gcps.xml.jaxb.rdc.retrieve.request.impl.ClientNameImpl) retrieveObjectFactory.createClientName("IGFELS");

        /** Set the ClientName & ExtendedHeader to the SearchXML */
        retrieveXML.setClientName(clientName.getValue());
        /** Create a new searchInputList */
        List retrieveInputList = retrieveXML.getRetrieveInput();
        Iterator i = mppIds.iterator();
        List list = new ArrayList();
        while (i.hasNext())
        {
            RetrieveInputImpl retrieveInput = (RetrieveInputImpl) retrieveObjectFactory.createRetrieveInput();
            InterfaceRequest interfaceRequest = new InterfaceRequest();
            String cust = ((CustomerXref) i.next()).getMppId();
            /** Create a InputList and OutputList */
            retrieveInput.setInputList(createRetrieveRequestInputList(cust, retrieveInput, retrieveObjectFactory));
            retrieveInput.setOutputList(createRetrieveRequestOutputList(interfaceRequest, retrieveInput, retrieveObjectFactory));
            //retrieveInput.setBODID()
            retrieveInputList.add(retrieveInput);
            /** Create a QueryImpl & set on the searchInput */
            financing.tools.gcps.xml.jaxb.rdc.retrieve.request.impl.QueryNameImpl queryName = (financing.tools.gcps.xml.jaxb.rdc.retrieve.request.impl.QueryNameImpl) retrieveObjectFactory.createQueryName("Retrieve");
            retrieveInput.setQueryName(queryName.getValue());
        }
        //logCtx.info(EXIT_LOG, "RetrieveXML createRetrieveXML(mppIds");
        return retrieveXML;
    }

    /**
     * Creates Search Request Input List
     * 
     * @param interfaceRequest
     *            InterfaceRequest
     * @return inputList InputListTypeImpl
     * @throws JAXBException
     */
    private InputListTypeImpl createSearchRequestInputList(InterfaceRequest interfaceRequest, SearchInputImpl searchInput, ObjectFactory searchObjectFactory) throws JAXBException
    {
        //logCtx.info(ENTRY_LOG, " createInputList(interfaceRequest");

        /** Create a new InputList element */
        InputListTypeImpl inputList = (InputListTypeImpl) searchObjectFactory.createInputList();

        CMRLegSysCtryImpl cmrCntry = (CMRLegSysCtryImpl) searchObjectFactory.createCMRLegSysCtry();
        LegCMRCusNumImpl legNum = (LegCMRCusNumImpl) searchObjectFactory.createLegCMRCusNum();
        CustomerName legName = (CustomerName) searchObjectFactory.createCustomerName();
        CountryKey countryKey = (CountryKey) searchObjectFactory.createCountryKey();
        City city = (City) searchObjectFactory.createCity();
        LogicalDeleteFlagImpl logDel = (LogicalDeleteFlagImpl) searchObjectFactory.createLogicalDeleteFlag();

        try
        {
            legNum.setValue(interfaceRequest.getCustomerId());
            legName.setValue(interfaceRequest.getCustomerName());
            countryKey.setValue(interfaceRequest.getIsoCountryCode());
            city.setValue(interfaceRequest.getCustomerCity());
            logDel.setValue(interfaceRequest.getLogicalDeleteFlag());
          
            cmrCntry.setValue(interfaceRequest.getIsoCountryCode().equals("US")? "897":countryProperties.getIbmCountryCode());
        } catch (Exception e)
        {
            logger.error( "Exception occured: "+e.getMessage(),e);
            //throw new GcpsException(APP_LOG, e);
        }

        if (legNum.getValue().trim().length() != 0)
        {
            inputList.setLegCMRCusNum(legNum.getValue());
            inputList.setCMRLegSysCtry(cmrCntry.getValue());
        } else
        {
            inputList.setCustomerName(legName.getValue());
            inputList.setCountryKey(countryKey.getValue());
            inputList.setCity(city.getValue());
        }
        inputList.setLogicalDeleteFlag(logDel.getValue());

        logger.debug("RDC Service - checking AddressSeqNum FLAG");

        String addressSeqNumFlag = countryProperties.getRdcUseAddrSeqNum();

        if (addressSeqNumFlag.equalsIgnoreCase("Y"))
        {
         logger.debug("RDC Service - addressSeqnum Y");
            createAddressSeqNum(inputList, interfaceRequest);
        } else
        {
        	logger.debug("RDC Service - setting MppIdAcctGrp");
            createMppIdAcctGrp(inputList, interfaceRequest);
        }

        return inputList;
    }

    /**
     * Creates Application and sets data from request
     * 
     * @param interfaceRequest
     *            InterfaceRequest
     * @param searchInput
     *            SearchInputImpl
     * @param searchObjectFactory
     *            ObjectFactory
     * @return outputList OutputListTypeImpl
     * @throws JAXBException
     */
    private OutputListTypeImpl createSearchRequestOutputList(InterfaceRequest interfaceRequest, SearchInputImpl searchInput, ObjectFactory searchObjectFactory) throws JAXBException
    {

        //logCtx.info(ENTRY_LOG, "
        // createSearchRequestOutputList(interfaceRequest, searchInput,
        // searchObjectFactory");

        /** Create a new InputList element */
        OutputListTypeImpl outputList = (OutputListTypeImpl) searchObjectFactory.createOutputList();
        OutputListType list = searchInput.getOutputList();
        CentralOrderBlockImpl cob = (CentralOrderBlockImpl) searchObjectFactory.createCentralOrderBlock("");
        CMRLegSysCtryImpl cmrCtry = (CMRLegSysCtryImpl) searchObjectFactory.createCMRLegSysCtry("");
        CustomerNameImpl name = (CustomerNameImpl) searchObjectFactory.createCustomerName("");
        CustomerName2Impl name2 = (CustomerName2Impl) searchObjectFactory.createCustomerName2("");
        DPLIndImpl dblind = (DPLIndImpl) searchObjectFactory.createDPLInd("");
        LegCMRCusNumImpl cusNum = (LegCMRCusNumImpl) searchObjectFactory.createLegCMRCusNum("");
        LogicalDeleteFlagImpl logFlg = (LogicalDeleteFlagImpl) searchObjectFactory.createLogicalDeleteFlag("");
        MppIdAcctGrpImpl accGrp = (MppIdAcctGrpImpl) searchObjectFactory.createMppIdAcctGrp("");
        outputList.setCentralOrderBlock(cob.getValue());
        outputList.setCMRLegSysCtry(cmrCtry.getValue());
        outputList.setCustomerName(name.getValue());
        outputList.setCustomerName2(name2.getValue());
        outputList.setDPLInd(dblind.getValue());
        outputList.setLegCMRCusNum(cusNum.getValue());
        outputList.setLogicalDeleteFlag(logFlg.getValue());
        outputList.setMppId(accGrp.getValue());

        //logCtx.info(EXIT_LOG, "
        // createSearchRequestOutputList(interfaceRequest, searchInput,
        // searchObjectFactory");
        return outputList;
    }

    /**
     * Create Search Response Error
     * 
     * @param searchError
     *            SearchErrorImpl
     * @return error InterfaceSearchResponseError
     */
    private InterfaceSearchResponseError createSearchResponseError(SearchErrorImpl searchError)
    {
        //logCtx.info(ENTRY_LOG, "InterfaceSearchResponseError
        // createSearchResponseError(earchError)");
        InterfaceSearchResponseError error = new InterfaceSearchResponseError();
        error.setErrorCode(searchError.getErrorCode());
        error.setErrorDetails(searchError.getErrorDetails());
        error.setErrorMessage(searchError.getErrorMessage());

        if (searchError.getInputList() != null)
        {
            InterfaceReponseInputList inputList = new InterfaceReponseInputList();
            inputList.setCmrLegSysCntry(searchError.getInputList().getCMRLegSysCtry());
            inputList.setLegCMRCusNum(searchError.getInputList().getLegCMRCusNum());
            inputList.setLogicalDeleteFlag(searchError.getInputList().getLogicalDeleteFlag());
            inputList.setMppIdAcctGrp(searchError.getInputList().getMppIdAcctGrp());
            error.setResponseInputList(inputList);
        }
        //logCtx.info(EXIT_LOG, "InterfaceSearchResponseError
        // createSearchResponseError(earchError)");
        return error;
    }

    /**
     * Creates Searcch Response Output
     * 
     * @param searchOutput
     *            SearchOutput
     * @return interfaceResponseOutput InterfaceSearchResponseOutput
     */
    private InterfaceSearchResponseOutput createSearchResponseOutput(SearchOutput searchOutput, int count)
    {
        //logCtx.info(ENTRY_LOG, "InterfaceSearchResponseOutput " +
        //	"createSearchResponseOutput(SearchOutput searchOutput)");
        InterfaceSearchResponseOutput interfaceResponseOutput = new InterfaceSearchResponseOutput();
        List list = new ArrayList();
        if (Integer.valueOf(searchOutput.getResultCount()).intValue() > 0)
        {
            Iterator i = searchOutput.getOutputList().iterator();

            while (i.hasNext())
            {
                OutputListImpl outputList = (OutputListImpl) i.next();
                InterfaceSearchResponseOutputList output = new InterfaceSearchResponseOutputList();
                output.setCentralOrderBlock(outputList.getCentralOrderBlock());
                output.setCMRLegSysCtry(outputList.getCMRLegSysCtry());
                output.setCustomerName(outputList.getCustomerName());
                output.setCustomerName2(outputList.getCustomerName2());
                output.setDPLInd(outputList.getDPLInd());
                output.setLegCMRCusNum(outputList.getLegCMRCusNum());
                output.setLogicalDeleteFlg(outputList.getLogicalDeleteFlag());
                output.setMppId(outputList.getMppId());
                list.add(output);
            }
        } else
        {
            InterfaceSearchResponseOutputList output = new InterfaceSearchResponseOutputList();
            output.setCMRLegSysCtry(((InterfaceRequest) getInterfaceRequestList().get(count)).getIbmCountryCode());
            output.setLegCMRCusNum(((InterfaceRequest) getInterfaceRequestList().get(count)).getCustomerId());
            list.add(output);
        }
        interfaceResponseOutput.setOutputList(list);
        interfaceResponseOutput.setBodId(Integer.parseInt(searchOutput.getBODID()));
        interfaceResponseOutput.setResultCount(searchOutput.getResultCount());

        //logCtx.info(EXIT_LOG, "InterfaceSearchResponseOutput " +
        //	"createSearchResponseOutput(SearchOutput searchOutput)");
        return interfaceResponseOutput;
    }

    /**
     * Creates SearchXML and sets data from request
     * 
     * @param custIds
     *            List
     * @param countryCode
     *            String
     * @return searchXML SearchXML
     * @throws JAXBException
     */
    private SearchXML createSearchXML(InterfaceRequest interfaceRequest) throws JAXBException
    {
        //logCtx.info(ENTRY_LOG, "SearchXML createSearchXML(List custIds,
        // String countryCode");
        ObjectFactory searchObjectFactory = new ObjectFactory();
        /** Create a new searchXML element */
        SearchXMLImpl searchXML = (SearchXMLImpl) searchObjectFactory.createSearchXML();
        /** Create a new ClientName element */
        ClientNameImpl clientName = (ClientNameImpl) searchObjectFactory.createClientName("IGFELS");
        /** Create a new ExtendedHeader element */
        ExtendedHeaderImpl extendedHeader = (ExtendedHeaderImpl) searchObjectFactory.createExtendedHeader("");

        /** Set the ClientName & ExtendedHeader to the SearchXML */
        searchXML.setClientName(clientName.getValue());
        searchXML.setExtendedHeader(extendedHeader.getValue());

        /** Create a new searchInputList */
        List searchInputList = searchXML.getSearchInput();

        int bodIdCount = 0;

        SearchInputImpl searchInput = (SearchInputImpl) searchObjectFactory.createSearchInput();

        interfaceRequest.setInterfaceRequestIdentifier(bodIdCount);

        /** Create a InputList and OutputList */
        searchInput.setInputList(createSearchRequestInputList(interfaceRequest, searchInput, searchObjectFactory));
        searchInput.setOutputList(createSearchRequestOutputList(interfaceRequest, searchInput, searchObjectFactory));
        /** Create a MaxRowsImpl & set on the searchInput */
        MaxRowsImpl maxRows = (MaxRowsImpl) searchObjectFactory.createMaxRows("999");
        BODIDImpl bodId = (BODIDImpl) searchObjectFactory.createBODID(String.valueOf(bodIdCount));
        /** Create a QueryImpt & set on the searchInput */
        QueryNameImpl queryName = (QueryNameImpl) searchObjectFactory.createQueryName("Search");
        searchInput.setQueryName(queryName.getValue());
        /** Add the search Input to the SearchInputList */
        searchInput.setMaxRows(maxRows.getValue());
        searchInput.setBODID(bodId.getValue());
        searchInputList.add(searchInput);
        interfaceRequestList.add(interfaceRequest);
        bodIdCount++;

        /** Set the Interface Request List for later mapping */
        setInterfaceRequestList(interfaceRequestList);
        //logCtx.info(EXIT_LOG, "SearchXML createSearchXML(List custIds, String
        // countryCode");
        return searchXML;
    }



    /**
     * 
     * Get the customer Information
     * 
     * @param custIds
     *            List
     * @param countryCode
     *            String
     * @return customerMap HashMap
     */
    public HashMap getCustomerInformation(InterfaceRequest interfaceRequest) throws Exception
    {
//        //logCtx.info(ENTRY_LOG, "HashMap getCustomerInformation(List custIds,
//        // String countryCode)");
        HashMap customerMap = null;
        InterfaceRetrieveResponse retrieveResponse = null;
        List customerList = new ArrayList();
        

        /** Search for the Customer */
        InterfaceSearchResponse searchResponse = searchCustomers(interfaceRequest);

        /** Get the List of MppIds that Exists, exculdes empty responses */
        List customerXrefs = getCustomerXref(searchResponse);

        getMppIds(customerXrefs);

        if (!(getMppList().isEmpty()))
        {
            /** Perform Retrieve of MppIds */
            retrieveResponse = retrieveCustomers(getMppList());
            /** Create HashpMap */
            customerList.addAll(createCustomers(retrieveResponse));
        }
        if (!(getEmptyMppList().isEmpty()))
        {

            customerList.addAll(createEmtpyCustomers(getEmptyMppList()));
        }
        customerMap = createCustomerHashMap(customerList);
        logger.info("Size of list returned?" + customerList.size());
        return customerMap;
    }

    /**
     * Get CustomerXRef
     * 
     * @param response
     *            InterfaceSearchResponse
     * @return list List
     */
    private List getCustomerXref(InterfaceSearchResponse response)
    {
        //logCtx.info(ENTRY_LOG, "List getCustomerXref(InterfaceSearchResponse
        // response)");
        Iterator i = response.getSearchOutput().iterator();
        List list = new ArrayList();
        while (i.hasNext())
        {
            InterfaceSearchResponseOutput output = (InterfaceSearchResponseOutput) i.next();
            Iterator j = output.getOutputList().iterator();
            while (j.hasNext())
            {
                InterfaceSearchResponseOutputList outputList = (InterfaceSearchResponseOutputList) j.next();
                CustomerXref customer = new CustomerXref();
                customer.setMppId(outputList.getMppId());
                customer.setLegacyCustomerNum(outputList.getLegCMRCusNum());
                customer.setLegacyCountryCode(outputList.getCMRLegSysCtry());
                customer.setCustomerName(outputList.getCustomerName());
                customer.setCity(outputList.getCity());
                customer.setCountryCode(outputList.getCountryCode());
                list.add(customer);
            }
        }
        //logCtx.info(EXIT_LOG, "List getCustomerXref(InterfaceSearchResponse
        // response)");
        return list;
    }

    public List getEmptyMppList()
    {
        return emptyMppList;
    }

    public List getInterfaceRequestList()
    {
        return interfaceRequestList;
    }

    /**
     * gets the MPPIds that are not NuLL
     * 
     * @param customerXrefs
     *            List
     */
    private void getMppIds(List customerXrefs)
    {
        Iterator i = customerXrefs.iterator();
        List list = new ArrayList();
        List emptyList = new ArrayList();
        while (i.hasNext())
        {
            CustomerXref xref = (CustomerXref) i.next();
            if (xref.getMppId() == null)
            {
                emptyList.add(xref);
            } else
            {
                list.add(xref);
            }
        }
        setEmptyMppList(emptyList);
        setMppList(list);
    }

    public List getMppList()
    {
        return mppList;
    }

    /**
     * Marshal Retrieve Request
     * 
     * @param mppIds
     *            List
     * @return outputStream.toString() String
     * @throws GcpsException
     */
    private String marshallRetrieveRequest(List mppIds) throws GILCNException
    {
        //logCtx.info(ENTRY_LOG, "String marshallRetrieveRequest(List
        // mppIds)");
        ByteArrayOutputStream outputStream = null;
        try
        {
            JAXBContext jc = JAXBContext.newInstance(retrieveRequestPackageName);
            financing.tools.gcps.xml.jaxb.rdc.retrieve.request.ObjectFactory searchObjectFactory = new financing.tools.gcps.xml.jaxb.rdc.retrieve.request.ObjectFactory();
            RetrieveXML retrieveXml = createRetrieveXML(mppIds);

            outputStream = new ByteArrayOutputStream();
            Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(retrieveXml, outputStream);
        } catch (MarshalException me)
        {
    	
            logger.error( "MarshalException occured",me);
            throw new GILCNException(me.getMessage());
        } catch (JAXBException jaxbe)
        {
        	
        	logger.error("JAXBException occured",jaxbe);
        	throw new GILCNException(jaxbe.getMessage());
        }
        //logCtx.info(EXIT_LOG, "String marshallRetrieveRequest(List mppIds)");
        return outputStream.toString();
    }

    /**
     * Marshal Search Request
     * 
     * @param custIds
     *            List
     * @param countryCode
     *            String
     * @return outputStream.toString() STring
     * @throws GcpsException
     */
    private String marshallSearchRequest(InterfaceRequest interfaceRequest) throws Exception
    {
//        Document document = null;

        ByteArrayOutputStream outputStream = null;
        try
        {
            JAXBContext jc = JAXBContext.newInstance(searchRequestPackageName);
            SearchXML searchXml = createSearchXML(interfaceRequest);
            Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            outputStream = new ByteArrayOutputStream();
            marshaller.marshal(searchXml, outputStream);
        } catch (javax.xml.bind.MarshalException e)
        {
        	logger.debug("RDC error: "+e.getMessage(),e);
            throw new Exception(e);
        } catch (JAXBException jaxbe){
        	logger.fatal(jaxbe.getMessage(),jaxbe);
            throw new Exception("RDC error: "+jaxbe.getMessage(),jaxbe);
        }

        return outputStream.toString();
    }

    /**
     * Creates InterfaceRetrieveResponse
     * 
     * @param mppIds
     *            List
     * @return response InterfaceRetrieveResponse
     * @throws Exception 
     */
    private InterfaceRetrieveResponse retrieveCustomers(List mppIds) throws Exception
    {
        //logCtx.info(ENTRY_LOG, "InterfaceRetrieveResponse
        // retrieveCustomers(List mppIds)");
        String retrieveRequest = null;
        String retrieveResponse = null;

        Document xmlDocRetrieve = null;
        try
        {
            retrieveRequest = marshallRetrieveRequest(mppIds);
            loggerWS.debug("Webservice Request: \n"+retrieveRequest);
        } catch (Exception e)
        {
            //logCtx.error(ENTRY_LOG, e, "Exception when calling marshal
            // retrieve");
        }
        try
        {       	
            retrieveResponse = rdcConsumer.retrieve(retrieveRequest);
            loggerWS.debug("Webservice Response: \n"+retrieveResponse);
        } catch (Exception e)
        {
            //logCtx.error(ENTRY_LOG, e, "Exception when calling perform
            // serch");
        }
        xmlDocRetrieve = createDocument(retrieveResponse);
        InterfaceRetrieveResponse response = unmarshallRetrieve(xmlDocRetrieve);
        //logCtx.info(EXIT_LOG, "InterfaceRetrieveResponse
        // retrieveCustomers(List mppIds)");
        return response;
    }

    /**
     * Creates InterfaceSearchResponse
     * 
     * @param custIds
     *            List
     * @param countryCode
     *            String
     * @return response InterfaceSearchResponse
     */
    private InterfaceSearchResponse searchCustomers(InterfaceRequest interfaceRequest) throws Exception
    {
        //logCtx.info(ENTRY_LOG, "InterfaceSearchResponse searchCustomers(List
        // mppIds)");
        String searchRequest = null;
        String searchResponse = null;
        Document xmlDocSearch = null;
//        SRServicesProxy proxy = new SRServicesProxy();

        /** Marshall the Search Request */
        try
        {
            searchRequest = marshallSearchRequest(interfaceRequest);
        } catch (Exception e)
        {
            logger.error("RDC Error - Marshalling Request"+e.getMessage(),e );
        }
        /** Invoke the Search Request to RDc */
        try
        {
      	
        	loggerWS.debug("Webservice Request: \n"+searchRequest);
            searchResponse=rdcConsumer.search(searchRequest);
            loggerWS.debug("Webservice Response: \n"+searchResponse);
        } catch (Exception e)
        {
            logger.error( "Exception when calling perform search:"+ e.getMessage(), e);
            throw new Exception("Either timed waiting for a response or a connection could not be made.",e);
        }

        /** Create a Document from the Sring returned */
        xmlDocSearch = createDocument(searchResponse);

        /** Unmarshall the Search Response */
        InterfaceSearchResponse response = unmarshallSearch(xmlDocSearch);

        //logCtx.info(EXIT_LOG, "InterfaceSearchResponse searchCustomers(List
        // mppIds)");
        return response;
    }

    public void setEmptyMppList(List list)
    {
        emptyMppList = list;
    }

    public void setInterfaceRequestList(List list)
    {
        interfaceRequestList = list;
    }

    public void setMppList(List list)
    {
        mppList = list;
    }

    /**
     * Creates InterfaceRetrieveResponse and sets data from response
     * 
     * @param xmlResponse
     *            Document
     * @return response InterfaceRetrieveResponse
     */
    private InterfaceRetrieveResponse unmarshallRetrieve(Document xmlResponse)
    {
        //logCtx.info(ENTRY_LOG, "InterfaceRetrieveResponse unmarshallRetrieve"
        // +
        //	"(Document xmlResponse))");
        InterfaceRetrieveResponse response = new InterfaceRetrieveResponse();
        JAXBContext jaxbContext;
        List list = new ArrayList();
        List errorList = new ArrayList();
        try
        {
            jaxbContext = JAXBContext.newInstance(retrieveResponsePackageName);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            financing.tools.gcps.xml.jaxb.rdc.retrieve.response.RetrieveXML retrieveXml = (financing.tools.gcps.xml.jaxb.rdc.retrieve.response.RetrieveXML) unmarshaller.unmarshal(xmlResponse);
            Iterator i = retrieveXml.getRetrieveOutputOrRetrieveError().iterator();
            while (i.hasNext())
            {
                Object o = (Object) i.next();
                if (o instanceof RetrieveOutput)
                {
                    RetrieveOutputImpl retrieveOutputImpl = (RetrieveOutputImpl) o;
                    InterfaceRetrieveResponseOutput out = createRetrieveResponseOutput(retrieveOutputImpl);
                    list.add(out);
                } else if (o instanceof financing.tools.gcps.xml.jaxb.rdc.retrieve.response.ExtendedHeader)
                {
                    financing.tools.gcps.xml.jaxb.rdc.retrieve.response.impl.ExtendedHeaderImpl extendedHeaderImpl = (financing.tools.gcps.xml.jaxb.rdc.retrieve.response.impl.ExtendedHeaderImpl) o;
                    response.setExtendedHeader(extendedHeaderImpl.getValue());
                } else
                {
                    RetrieveErrorImpl retrieveError = (RetrieveErrorImpl) o;
                    InterfaceRetrieveResponseError error = createRetrieveResponseError(retrieveError);
                    errorList.add(error);
                }
            }
        } catch (JAXBException e)
        {
            logger.error("unmarshallRetrieve: JAXB Exception found:" + e.getMessage(),e);
        }
        response.setRetrieveResponseOutput(list);
        response.setRetrieveResponseError(errorList);
        return response;
    }

    /**
     * Unmarshall document and create InterfaceSearchResponse
     * 
     * @param xmlResponse
     *            Document
     * @return response InterfaceSearchResponse
     */
    private InterfaceSearchResponse unmarshallSearch(Document xmlResponse)
    {
        //logCtx.info(ENTRY_LOG, "InterfaceSearchResponse unmarshallSearch" +
        //			"(Document xmlResponse))");
        InterfaceSearchResponse response = new InterfaceSearchResponse();
        JAXBContext jaxbContext;
        List list = new ArrayList();
        List errorList = new ArrayList();

        try
        {
            jaxbContext = JAXBContext.newInstance(searchResponsePackageName);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            financing.tools.gcps.xml.jaxb.rdc.search.response.SearchXML searchXml = (financing.tools.gcps.xml.jaxb.rdc.search.response.SearchXML) unmarshaller.unmarshal(xmlResponse);
            Iterator i = searchXml.getSearchOutputOrSearchError().iterator();
            int count = 0;
            while (i.hasNext())
            {

                Object o = (Object) i.next();
                if (o instanceof SearchOutput)
                {
                    InterfaceSearchResponseOutput output = new InterfaceSearchResponseOutput();
                    SearchOutputImpl searchOutputImpl = (SearchOutputImpl) o;
                    output = createSearchResponseOutput(searchOutputImpl, count);
                    list.add(output);
                    ++count;
                } else if (o instanceof ExtendedHeader)
                {
                    financing.tools.gcps.xml.jaxb.rdc.search.response.impl.ExtendedHeaderImpl extendedHeaderImpl = (financing.tools.gcps.xml.jaxb.rdc.search.response.impl.ExtendedHeaderImpl) o;
                    response.setExtendedHeader(extendedHeaderImpl.getValue());
                } else
                {
                    SearchErrorImpl searchError = (SearchErrorImpl) o;
                    InterfaceSearchResponseError error = createSearchResponseError(searchError);
                    errorList.add(error);
                    ++count;
                }
            }
        } catch (JAXBException e)
        {
            logger.error( "unmarshallSearch: JAXBException found:"+ e.getMessage(),e);
        }
        response.setSearchOutput(list);
        response.setSearchError(errorList);
        return response;
    }

    /**
     * Validate Customer
     * 
     * @param customer
     *            Customer_impl
     * @return customer Customer_impl
     */
    private Customer_impl validateCustomer(Customer_impl customer)
    {

        List errorCodes = new ArrayList();
        if (customer.getDeniedPartyIndicator().equalsIgnoreCase("Y"))
        {
            customer.setError(true);
            errorCodes.add(GcpsMessageKeys.GCPS_CLIENT_CUSTOMER_DENIED);
        }
        if (!(customer.getCentralOrderBlockFlag() == null || customer.getCentralOrderBlockFlag().trim().equals("")))
        {
            customer.setError(true);
            errorCodes.add(GcpsMessageKeys.GCPS_CLIENT_CUSTOMER_BLOCKED);
        }
        if (customer.getLogicalDeleteFlag().equalsIgnoreCase("Y"))
        {
            customer.setError(true);
            errorCodes.add(GcpsMessageKeys.GCPS_CLIENT_CUSTOMER_LOGICAL_DELETE);
        }
        customer.setErrorCodes(errorCodes);
        return customer;
    }
    private void createMppIdAcctGrp(InputListTypeImpl inputList, InterfaceRequest interfaceRequest)
    {
        String mppAcctGrp = null;

        try
        {
            mppAcctGrp = interfaceRequest.getAccountGroup();
            
        } catch (Exception e)
        {

        	logger.error(e.getMessage());
        	
        }

        if (mppAcctGrp != null && mppAcctGrp.length() > 0)
        {
            inputList.setMppIdAcctGrp(mppAcctGrp);
        }

    }

    private void createAddressSeqNum(InputListTypeImpl inputList, InterfaceRequest interfaceRequest)
    {

        String isoCountryCode = interfaceRequest.getIsoCountryCode();
        String addressSeqNum = null;

        addressSeqNum =countryProperties.getRdcUseSeqNum();//isoCountryCode.equals("US")? "897":countryProperties.getRdcUseSeqNum();

        if (addressSeqNum != null && addressSeqNum.length() > 0)
        {
            inputList.setAddressSeqNum(addressSeqNum);
        }

    }
   

}
