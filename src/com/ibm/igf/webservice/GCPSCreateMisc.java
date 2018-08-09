/*
 * Created on April 27, 2011
 *
 *=========================================================================== * 
 *
 * IBM Confidential  
 * (C) Copyright IBM Corp., 2010. 
 * The source code for this program is not published or otherwise divested of 
 * its trade secrets, irrespective of what has been deposited with the U.S. 
 * Copyright office. 
 *=========================================================================== 
 * Module Information: 
 * Created By  : Srinivas
 * Project     : GILClient
 * Description : GCPSCreateMisc
 *=========================================================================== */
package com.ibm.igf.webservice;



import com.ibm.gcms.consumer.CreateMiscConsumer;
import com.ibm.gil.business.MiscELS;
import com.ibm.gil.util.UtilGilConstants;
import com.ibm.igf.hmvc.PluginConfigurationPane;

public class GCPSCreateMisc extends GCPSWebService{
	
	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(GCPSCreateMisc.class);
	
	private CreateMiscConsumer consumer;
    private String urlService;
    
    private String getUrlWS(){
	    
    	if(urlService==null){
    		urlService=PluginConfigurationPane.getPropertiesMap().get(UtilGilConstants.GCMS_SERVER)+UtilGilConstants.CREATEMISC_WS;
    	}
    	
    	logger.info("URL Service: "+urlService);
    	
    	return urlService ;
    }
    
    private String getServiceName(){
	    
    	return UtilGilConstants.CREATEMISC_WS;
    }
    

    public boolean createMisc(final MiscELS misc) {
    	try {
    		consumer = new CreateMiscConsumer(misc.getGcmsEndpoint().getWSUrl()+getServiceName(),misc.getGcmsEndpoint().getFunctionalId(), misc.getGcmsEndpoint().getAccess());
			consumer.callGCPSCreateMisc(misc.getCOANUMBER(), misc.getCOUNTRY(), misc.getINVOICENUMBER(), 
					misc.getOFFERINGNUMBER(), misc.getTIMESTAMP().substring(0, 10), misc.getUSERID());
		} catch (Exception e) {
			logger.error(e.toString() + ": " + e.getMessage());
			return false;
		}
    	return true;
    }
	
	
//    public boolean createMisc(MiscELS aMiscDM)
//    {
//        try
//        {
//            // call the web service wrapper
//            MiscDocument miscDocument = doCreateMisc(aMiscDM);
//            if (miscDocument == null)
//            {
//                return false;
//            }
//
//        } catch (Exception exc)
//        {
//            Debugger.info("Error creating Misc Document");
//            Debugger.fatal(exc.toString());
//        }
//
//        return true;
//    }
//    
//    private void loadMiscDocument(MiscELS MISCDM)
//    {
//        ObjectTransform.transform(MISCDM, miscDocument, new String[] { "COUNTRY", "OFFERINGNUMBER", "COANUMBER", "INVOICENUMBER", "USERID", "CREATEDATE" });
//
//    }   
//    
//    private String getWebService()
//    {
//        if (webService == null)
//        {
//            webService = getWebServiceProperty("_WEBSERVICECREATEMISC", RegionManager.getBackendRegion());
//        }
//
//        return webService;
//    }
//
//    private String getXmlNamespace()
//    {
//        if (xmlNamespace == null)
//        {
//            xmlNamespace = getWebServiceProperty("_XMLNAMESPACECREATEMISC", RegionManager.getBackendRegion());
//        }
//        return xmlNamespace;
//    }    

}
