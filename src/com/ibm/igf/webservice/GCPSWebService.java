/*
 * Created on Feb 23, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ibm.igf.webservice;


import java.util.List;



import com.ibm.gil.util.UtilGilConstants;
import com.ibm.igf.hmvc.PluginConfigurationPane;

/**
 * @author ajimena
 * 
 * 
 */
public class GCPSWebService {
	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(GCPSGetInvoice.class);

	private String userId=null;
	private String pw=null;
	protected String getGCMSId(){
	    
    	if(userId==null){
    		userId=PluginConfigurationPane.getPropertiesMap().get(UtilGilConstants.GCMS_User);
    	}
   	
    	return userId ;
    }
    
	protected String getGCMSAccess(){
	    
    	if(pw==null){
    		pw=PluginConfigurationPane.getPropertiesMap().get(UtilGilConstants.GCMS_ACCESS);
    	}
   	
    	return pw ;
    }
    
    public void showMessages(List messages)
    {
        if (messages == null)
            return;

        for (int i = 0; i < messages.size(); i++)
        {
            if (((String) messages.get(i)).indexOf("Success") == -1)
            {

                logger.info(messages.get(i));
            }
        }

    }
}
