/*
 * Created on Mar 18, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ibm.igf.webservice;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;



import com.ibm.gcms.consumer.UpdateCOAConsumer;
import com.ibm.gil.business.COAELS;
import com.ibm.gil.util.UtilGilConstants;
import com.ibm.igf.hmvc.PluginConfigurationPane;
import com.ibm.igf.webservice.util.UpdateCOABuilder;
import com.ibm.xmlns.igf.gcps.updatecoaack.Acknowledgement;
import financing.tools.gcps.ws.service.updatecoa.request.UpdateCoa;
import financing.tools.gcps.ws.service.updatecoa.request.UpdateCoaFactory;


/**
 * 
 * @author ferandra
 *
 */
public class GCPSUpdateCOA extends GCPSWebService {
	
   private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(GCPSUpdateCOA.class);
   private UpdateCoa updateCOA = null;
   private Acknowledgement ack = null; 
   private String urlService=null;
    
    private String getUrlWS(){
    	    
    	if(urlService==null){
    		urlService=PluginConfigurationPane.getPropertiesMap().get(UtilGilConstants.GCMS_SERVER)+UtilGilConstants.UPDATECOA_WS;
    	}
    	
    	logger.info("URL Service: "+urlService);
    	
    	return urlService ;
    }
    
    private String getServiceName(){
	    
    	return UtilGilConstants.UPDATECOA_WS;
    }
    

    public Acknowledgement doUpdateCOA(COAELS COADM) throws Exception {
        
    	try  {
        	
	        	UpdateCOABuilder coaBuilder = new UpdateCOABuilder(COADM);
	        	updateCOA = UpdateCoaFactory.singleton().create();
	        	loadUpdateCOA(COADM);
	        	//UpdateCOAConsumer updateCOAConsumer = new UpdateCOAConsumer(getUrlWS(), getGCMSId(),getGCMSAccess());
	        	UpdateCOAConsumer updateCOAConsumer = new UpdateCOAConsumer(COADM.getGcmsEndpoint().getWSUrl()+getServiceName(),COADM.getGcmsEndpoint().getFunctionalId(), COADM.getGcmsEndpoint().getAccess());
	        	ack = updateCOAConsumer.callGcpsUpdateCoa(coaBuilder.buildGetCOA());	

        } catch (JAXBException je) {
        	logger.fatal("Error in Web Service GCPSUpdateCOA: "+ je.getMessage());
        	je.printStackTrace();
            logger.debug(je.toString());
            throw je;
            
        } catch (ParserConfigurationException pce) {
        	logger.fatal("Error in Web Service GCPSUpdateCOA: "+ pce.getMessage());
        	pce.printStackTrace();
            logger.debug(pce.toString());
            throw pce;
            
        } catch (Exception e) {
        	logger.fatal("Error in Web Service GCPSUpdateCOA: "+ e.getMessage());
        	e.printStackTrace();
            logger.debug(e.toString());
            throw e;
        }

        return ack;
    }

    public Acknowledgement updateCOA(COAELS aOLDM) throws Exception  {
    	
        Acknowledgement updateCoaAck = doUpdateCOA(aOLDM);
        return updateCoaAck;
    }

    private void loadUpdateCOA(COAELS COADM)
    {
        updateCOA.setCountryCode(COADM.getCOUNTRY());
        updateCOA.setCoaNumber(COADM.getCOANUMBER());
        updateCOA.setCoaCmObjectId(COADM.getOBJECTID());
        updateCOA.setScanTimestamp(COADM.getCreatedTimeStampAsDate().getTime());
        // R2
        updateCOA.setCoaSignature(COADM.getSIGNEDBY());
        COADM.generateUNIQUEREQUESTNUMBER();
        updateCOA.setApplicationId(COADM.getSENDER());
        updateCOA.setDocumentId(COADM.getUNIQUEREQUESTNUMBER());
    }

}
