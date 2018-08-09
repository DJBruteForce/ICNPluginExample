/*
 * Created on aug 18, 2017
 *
 */
package com.ibm.igf.webservice;



import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

import com.ibm.gcms.consumer.UpdateOfferingLetterConsumer;
import com.ibm.gil.business.OfferingLetterELS;
import com.ibm.gil.util.UtilGilConstants;
import com.ibm.igf.hmvc.PluginConfigurationPane;
import com.ibm.igf.webservice.util.UpdateOfferLetterBuilder;
import com.ibm.xmlns.igf.gcps.updateofferingletterack.Acknowledgement;

import financing.tools.gcps.ws.service.updateofferletter.request.UpdateOfferLetter;
import financing.tools.gcps.ws.service.updateofferletter.request.UpdateOfferLetterFactory;


/**
 * @author ferandra
 * 
 */
public class GCPSUpdateOL extends GCPSWebService {
	
   private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(GCPSUpdateOL.class);
   private UpdateOfferLetter updateOfferLetter = null;
   private Acknowledgement acknowledgment = null;
   private String urlService=null;
    
    private String getUrlWS(){
    	    
    	if(urlService==null){
    		urlService=PluginConfigurationPane.getPropertiesMap().get(UtilGilConstants.GCMS_SERVER)+UtilGilConstants.UPDATEOL_WS;
    	}
    	
    	logger.info("URL Service: "+urlService);
    	
    	return urlService ;
    }
    
    private String getServiceName(){
	    
    	return UtilGilConstants.UPDATEOL_WS;
    }
    
    
   public Acknowledgement doUpdateOL(OfferingLetterELS OLDM) throws Exception

    {
        try
        {
       
        	UpdateOfferLetterBuilder olBuilder = new UpdateOfferLetterBuilder(OLDM);
        	updateOfferLetter = UpdateOfferLetterFactory.singleton().create();
        	loadUpdateOfferLetter(OLDM);
        	//UpdateOfferingLetterConsumer updateOLConsumer = new UpdateOfferingLetterConsumer(getUrlWS(),getGCMSId(),getGCMSAccess());
        	UpdateOfferingLetterConsumer updateOLConsumer = new UpdateOfferingLetterConsumer(OLDM.getGcmsEndpoint().getWSUrl()+getServiceName(),OLDM.getGcmsEndpoint().getFunctionalId(), OLDM.getGcmsEndpoint().getAccess());
        	acknowledgment = updateOLConsumer.callGcpsUpdateOfferingLetter(olBuilder.buildUpdateOfferingLetter());


        } catch (JAXBException je) {
        	logger.fatal("Error in Web Service GCPSUpdateOL: "+ je.getMessage());
        	logger.debug(je.toString());
        	je.printStackTrace();
            throw je;
            
        } catch (ParserConfigurationException pce) {
        	logger.fatal("Error in Web Service GCPSUpdateOL: "+ pce.getMessage());
        	logger.debug(pce.toString());
        	pce.printStackTrace();
            throw pce;
            
        } catch (Exception e) {
        	logger.fatal("Error in Web Service GCPSUpdateOL: "+ e.getMessage());
        	logger.debug(e.toString());
        	e.printStackTrace();
            throw e;
        }
        return acknowledgment;
    }

   public Acknowledgement updateOL(OfferingLetterELS aOLDM) throws Exception  {

    	Acknowledgement updateOfferLetterAck = doUpdateOL(aOLDM);
    	return updateOfferLetterAck;
    }

    private void loadUpdateOfferLetter(OfferingLetterELS OLDM) {

        ObjectTransform.transform(OLDM, updateOfferLetter, new String[] { 
														        		"COUNTRY",
														        		"OFFERINGNUMBER", 
														        		"OBJECTID",
														        		"CREATEDATEasLong",
														        		"SIGNEDCOUNTERSIGNED",
														        		"CUSTOMERSIGNATURE", 
														        		"CUSTOMERSIGNATUREDATEasDate", 
														        		"IBMSIGNATUREDATEasDate",
														                "SIGNEDCOUNTERSIGNED" });
        updateOfferLetter.setReceiptDate(OLDM.getCreatedTimeStampAsDate().getTime());
        updateOfferLetter.setApplicationId("GIL");
        OLDM.generateUNIQUEREQUESTNUMBER();
        updateOfferLetter.setDocumentId(OLDM.getUNIQUEREQUESTNUMBER());
    }


}
