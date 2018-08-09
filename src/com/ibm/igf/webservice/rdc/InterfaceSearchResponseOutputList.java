/**

 */
package com.ibm.igf.webservice.rdc;

/**
 * Maps Interface Search Response Output List
 */
public class InterfaceSearchResponseOutputList {
    private String mppId;
    private String cMRLegSysCtry;
    private String legCMRCusNum;
    private String centralOrderBlock;
    private String logicalDeleteFlg;
    private String dPLInd;
    private String customerName;
    private String countryCode;
    private String city;
    private String customerName2;

    public String getCentralOrderBlock()
    {
        return centralOrderBlock;
    }

    public String getCMRLegSysCtry()
    {
        return cMRLegSysCtry;
    }

    public String getCustomerName()
    {
        return customerName;
    }

    public String getCustomerName2()
    {
        return customerName2;
    }

    public String getDPLInd()
    {
        return dPLInd;
    }

    public String getLegCMRCusNum()
    {
        return legCMRCusNum;
    }

    public String getLogicalDeleteFlg()
    {
        return logicalDeleteFlg;
    }

    public String getMppId()
    {
        return mppId;
    }

    public void setCentralOrderBlock(String aCenOrdBlck)
    {
        centralOrderBlock = aCenOrdBlck;
    }

    public void setCMRLegSysCtry(String aLegSysCtry)
    {
        cMRLegSysCtry = aLegSysCtry;
    }

    public void setCustomerName(String aCustName)
    {
        customerName = aCustName;
    }

    public void setCustomerName2(String aCustName2)
    {
        customerName2 = aCustName2;
    }

    public void setDPLInd(String aDPLInd)
    {
        dPLInd = aDPLInd;
    }

    public void setLegCMRCusNum(String aLegCusNum)
    {
        legCMRCusNum = aLegCusNum;
    }

    public void setLogicalDeleteFlg(String aLogDelFlg)
    {
        logicalDeleteFlg = aLogDelFlg;
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
}
