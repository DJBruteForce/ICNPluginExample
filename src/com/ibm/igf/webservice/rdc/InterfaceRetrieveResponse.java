/**

 */
package com.ibm.igf.webservice.rdc;

import java.util.List;

/**
 * Maps the JAXB generated XML Retrieve Response to InterfaceRetrieveResponse.
 */
public class InterfaceRetrieveResponse {
    private String extendedHeader;
    private List retrieveError;
    private List retrieveOutput;

    public InterfaceRetrieveResponse()
    {
    }

    public String getExtendedHeader()
    {
        return extendedHeader;
    }

    public List getRetrieveResponseError()
    {
        return retrieveError;
    }

    public List getRetrieveResponseOutput()
    {
        return retrieveOutput;
    }

    public void setExtendedHeader(String aExtendedHeader)
    {
        extendedHeader = aExtendedHeader;
    }

    public void setRetrieveResponseError(List error)
    {
        retrieveError = error;
    }

    public void setRetrieveResponseOutput(List output)
    {
        retrieveOutput = output;
    }
}
