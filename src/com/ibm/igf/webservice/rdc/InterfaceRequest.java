/**

 */
package com.ibm.igf.webservice.rdc;

import java.util.List;

import financing.tools.gcps.common.exceptions.GcpsException;

//import financing.tools.gcps.common.keys.ParameterKeys;
//import financing.tools.gcps.login.domain.SystemParameterFactory;

/**
 * Interface Request Class. Legacy Customer, Country Code, account Group and
 * Logical Delete Flag
 */
public class InterfaceRequest {
    private String customerId = "";
    private String customerName = "";
    private String customerCity = "";
    private String ibmCountryCode = "";
    private int interfaceRequestIdentifier;
    private String isoCountryCode = "";

    public String getAccountGroup() throws GcpsException
    {
        List parms = null;
        //SystemParameterFactory.singleton().findSystemValue(getIsoCountryCode(),
        //ParameterKeys.ACCOUNT_GROUP, ParameterKeys.RDC);
        //return (String) parms.get(0);
        return "ZS01";
    }

    public String getCustomerId()
    {
        return customerId;
    }

    public String getCustomerName()
    {
        return customerName;
    }

    public String getCustomerCity()
    {
        return customerCity;
    }

    public String getIbmCountryCode()
    {
        return ibmCountryCode;
    }

    public int getInterfaceRequestIdentifier()
    {
        return interfaceRequestIdentifier;
    }

    public String getIsoCountryCode()
    {
        return isoCountryCode;
    }

    public String getLogicalDeleteFlag() throws GcpsException
    {
        List parms = null;
        //SystemParameterFactory.singleton().findSystemValue(getIsoCountryCode(),
        //ParameterKeys.LOGICAL_DELETE, ParameterKeys.RDC);
        //return (String) parms.get(0);
        return "Y";
    }

    public void setCustomerId(String aCustId)
    {
        customerId = aCustId;
    }

    public void setCustomerName(String aCustName)
    {
        customerName = aCustName;
    }

    public void setCustomerCity(String aCustCity)
    {
        customerCity = aCustCity;
    }

    public void setIbmCountryCode(String aIbmCountryCode)
    {
        ibmCountryCode = aIbmCountryCode;
    }

    public void setInterfaceRequestIdentifier(int newValue)
    {
        interfaceRequestIdentifier = newValue;
    }

    public void setIsoCountryCode(String isoCc)
    {
        isoCountryCode = isoCc;
    }

}
