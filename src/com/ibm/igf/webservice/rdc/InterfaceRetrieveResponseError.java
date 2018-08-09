/**

 */
package com.ibm.igf.webservice.rdc;

/**
 * Maps the Retrieve Response Error created by the generated JAXB object. This
 * is then set in the InterfaceRetrieveResponseError Object
 */
public class InterfaceRetrieveResponseError extends InterfaceRetrieveResponse {
    private String errorCode;

    private String errorDetails;

    private String errorMessage;

    private String legacyCustomerNumber;

    private String mppId;

    public String getErrorCode()
    {
        return errorCode;
    }

    public String getErrorDetails()
    {
        return errorDetails;
    }

    public String getErrorMessage()
    {
        return errorMessage;
    }

    public String getLegacyCustomerNumber()
    {
        return legacyCustomerNumber;
    }

    public String getMppId()
    {
        return mppId;
    }

    public void setErrorCode(String aErrorCode)
    {
        errorCode = aErrorCode;
    }

    public void setErrorDetails(String aErrorDetails)
    {
        errorDetails = aErrorDetails;
    }

    public void setErrorMessage(String aErrorMessage)
    {
        errorMessage = aErrorMessage;
    }

    public void setLegacyCustomerNumber(String aLegNum)
    {
        legacyCustomerNumber = aLegNum;
    }

    public void setMppId(String aMppId)
    {
        mppId = aMppId;
    }

}
