/**

 */
package com.ibm.igf.webservice.rdc;

import java.util.List;

/**
 * Maps Interface Search Response Output
 */
public class InterfaceSearchResponseOutput {
    private int bodId;
    private List outputList;
    private String resultCount;

    public int getBodId()
    {
        return bodId;
    }

    public List getOutputList()
    {
        return outputList;
    }

    public String getResultCount()
    {
        return resultCount;
    }

    public void setBodId(int i)
    {
        bodId = i;
    }

    public void setOutputList(List list)
    {
        outputList = list;
    }

    public void setResultCount(String aCount)
    {
        resultCount = aCount;
    }

}
