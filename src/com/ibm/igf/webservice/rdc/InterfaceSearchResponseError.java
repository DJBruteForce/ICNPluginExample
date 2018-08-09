/**
 */
package com.ibm.igf.webservice.rdc;

/**
 * Maps Interface Search Response Error
 */
public class InterfaceSearchResponseError {
    private String errorCode;
    private String errorDetails;
    private String errorMessage;
    private InterfaceReponseInputList responseInputList;

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

    public InterfaceReponseInputList getResponseInputList()
    {
        return responseInputList;
    }

    public void setErrorCode(String aErrorCode)
    {
        errorCode = aErrorCode;
    }

    public void setErrorDetails(String aErrorDetail)
    {
        errorDetails = aErrorDetail;
    }

    public void setErrorMessage(String aMessage)
    {
        errorMessage = aMessage;
    }

    public void setResponseInputList(InterfaceReponseInputList aResponseList)
    {
        responseInputList = aResponseList;
    }

}
