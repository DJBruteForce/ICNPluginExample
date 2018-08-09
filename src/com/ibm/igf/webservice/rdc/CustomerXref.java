/**
 *  
 */
package com.ibm.igf.webservice.rdc;

public class CustomerXref {
    private String mppId;
    private String legacyCustomerNum;
    private String legacyCountryCode;
    private String customerName;
    private String city;
    private String countryCode;

    public String getLegacyCountryCode()
    {
        return legacyCountryCode;
    }

    public String getLegacyCustomerNum()
    {
        return legacyCustomerNum;
    }

    public String getMppId()
    {
        return mppId;
    }

    public void setLegacyCountryCode(String aLegacyCountryCode)
    {
        legacyCountryCode = aLegacyCountryCode;
    }

    public void setLegacyCustomerNum(String aLegacyCustomerNum)
    {
        legacyCustomerNum = aLegacyCustomerNum;
    }

    public void setMppId(String aMppId)
    {
        mppId = aMppId;
    }

    /**
     * @return Returns the city.
     */
    public String getCity()
    {
        return city;
    }

    /**
     * @param city
     *            The city to set.
     */
    public void setCity(String city)
    {
        this.city = city;
    }

    /**
     * @return Returns the countryCode.
     */
    public String getCountryCode()
    {
        return countryCode;
    }

    /**
     * @param countryCode
     *            The countryCode to set.
     */
    public void setCountryCode(String countryCode)
    {
        this.countryCode = countryCode;
    }

    /**
     * @return Returns the customerName.
     */
    public String getCustomerName()
    {
        return customerName;
    }

    /**
     * @param customerName
     *            The customerName to set.
     */
    public void setCustomerName(String customerName)
    {
        this.customerName = customerName;
    }
}
