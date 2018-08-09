/**
 *  
 */
package com.ibm.igf.webservice.rdc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;



import com.ibm.gil.business.InvoiceELS;


import financing.tools.gcps.common.domain.Customer;



public class RDcWebServiceClient {
	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(RDcWebServiceClient.class);
private String country="";
    public ArrayList getCustomerData(InvoiceELS IDM)
    {
    	country=IDM.getCOUNTRY();
        int idx = IDM.getCUSTOMERNAME().indexOf("/");
        String name = IDM.getCUSTOMERNAME();
        String city = IDM.getCUSTOMERADDRESS3();
        if (idx > 0)
        {
            name = IDM.getCUSTOMERNAME().substring(0, idx);
            city = IDM.getCUSTOMERNAME().substring(idx + 1);
        }
        InterfaceRequest interfaceRequest = new InterfaceRequest();
        interfaceRequest.setCustomerId(IDM.getCUSTOMERNUMBER());
        interfaceRequest.setCustomerName(name);
        interfaceRequest.setCustomerCity(city);
        interfaceRequest.setIsoCountryCode(IDM.getCOUNTRY());
        logger.debug("inside RDcWebServiceClient getCustomerData");
        return getCustomerData(interfaceRequest, IDM.getDialogErrorMsgs());

    }

    public ArrayList getCustomerData(InterfaceRequest interfaceRequest, ArrayList<String> dialogErrorMsg)
    {
        ArrayList returnList = new ArrayList();
        HashMap values = null;

        try
        {

            values = RDcServiceFactory.singleton().getService(interfaceRequest.getIsoCountryCode()).getCustomerInformation(interfaceRequest);

            Set aSet = values.keySet();
            String key = null;
            Customer cust = null;
            InvoiceELS returnDM = null;
            for (Iterator itr = aSet.iterator(); itr.hasNext();)
            {
                key = (String) itr.next();
                cust = (Customer) values.get(key);

                if (cust.getError())
                {  
                	dialogErrorMsg.add("RDc Error : " + cust.getErrorCodes().toString());
                   logger.debug("RDc Error : " + cust.getErrorCodes().toString());
                } else
                {
                    returnDM = new InvoiceELS(country);
                    loadResults(cust, returnDM);
                    returnList.add(returnDM);
                }
            }

        } catch (Exception e)
        {
            logger.debug(e.getMessage());
        }

        return returnList;
    }

    private void loadResults(Customer cust, InvoiceELS returnDM)
    {
        returnDM.setCUSTOMERNUMBER(cust.getSofCustomerNumber());
        logger.debug("customer number :"+cust.getSofCustomerNumber());
        returnDM.setCUSTOMERNAME(cust.getCustomerName() + " " + cust.getCustomerName2());
        logger.debug("customer name:"+ cust.getCustomerName()+ " " +cust.getCustomerName2());
        returnDM.setCUSTOMERADDRESS1(cust.getAddress1());
        logger.debug("customer address:"+ cust.getAddress1());
        returnDM.setCUSTOMERADDRESS2(cust.getAddress2());
        logger.debug("customer address:"+cust.getAddress2());
        returnDM.setCUSTOMERADDRESS3(cust.getCity());
        logger.debug("city:"+ cust.getCity());
        returnDM.setCUSTOMERADDRESS4(cust.getPostalCode());
        logger.debug("postal code:"+ cust.getPostalCode() );
        returnDM.setCUSTOMERADDRESS5(cust.getState());
        logger.debug("state:"+ cust.getState());
    }

}
