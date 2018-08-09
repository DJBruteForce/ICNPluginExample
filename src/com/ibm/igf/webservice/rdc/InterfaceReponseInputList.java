/**
 *  
 */
package com.ibm.igf.webservice.rdc;

/*
 * Maps the Interfaface Response Input List
 */
public class InterfaceReponseInputList {
    private String cmrLegSysCntry;
    private String legCMRCusNum;
    private String customerName;
    private String city;
    private String countryCode;
    private String logicalDeleteFlag;
    private String mppIdAcctGrp;

    public String getCmrLegSysCntry()
    {
        return cmrLegSysCntry;
    }

    public String getLegCMRCusNum()
    {
        return legCMRCusNum;
    }

    public String getLogicalDeleteFlag()
    {
        return logicalDeleteFlag;
    }

    public String getMppIdAcctGrp()
    {
        return mppIdAcctGrp;
    }

    public void setCmrLegSysCntry(String aCmrLegSysCntry)
    {
        cmrLegSysCntry = aCmrLegSysCntry;
    }

    public void setLegCMRCusNum(String aLegCMRCusNum)
    {
        legCMRCusNum = aLegCMRCusNum;
    }

    public void setLogicalDeleteFlag(String aLogicalDeleteFlag)
    {
        logicalDeleteFlag = aLogicalDeleteFlag;
    }

    public void setMppIdAcctGrp(String aMppIdAcctGrp)
    {
        mppIdAcctGrp = aMppIdAcctGrp;
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
