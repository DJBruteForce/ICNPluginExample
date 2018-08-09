/**
 */
package com.ibm.igf.webservice.rdc;

import financing.tools.gcps.common.exceptions.GcpsException;

/**
 *  
 */
public class RDcServiceFactory {
    /** Singleton Object for the COAProcessFacotry */
    protected static RDcServiceFactory singleton;

    //static String APP_LOG = RDcServiceFactory.class.getName();
    //	static String ENTRY_LOG = "entry." + APP_LOG;
    //	static String EXIT_LOG = "exit." + APP_LOG;
    //static LogContext logCtx = LogContextFactory.singleton().getLogContext();

    public static RDcServiceFactory singleton() throws GcpsException
    {

        if (RDcServiceFactory.singleton == null)
        {
            singleton = new RDcServiceFactory();
        }
       
        return singleton;
    }

    public RDcService_impl getService(String country)
    {
        return new RDcService_impl(country);
    }

}
