/**
 */
package com.ibm.igf.webservice.rdc;

import java.util.List;

/**
 * Maps the Interface Search Response
 */
public class InterfaceSearchResponse {
    private String extendedHeader;
    private List searchError;
    private List searchOutput;

    public InterfaceSearchResponse()
    {
    }

    public String getExtendedHeader()
    {
        return extendedHeader;
    }

    public List getSearchError()
    {
        return searchError;
    }

    public List getSearchOutput()
    {
        return searchOutput;
    }

    public void setExtendedHeader(String aExtendedHdr)
    {
        extendedHeader = aExtendedHdr;
    }

    public void setSearchError(List error)
    {
        searchError = error;
    }

    public void setSearchOutput(List output)
    {
        searchOutput = output;
    }

}
