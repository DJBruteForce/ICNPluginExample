/*
 * Created on aug 18, 2017
 */
package com.ibm.igf.webservice;



import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;



import com.ibm.gcms.consumer.CreateOfferingLetterConsumer;
import com.ibm.gil.business.OfferingLetterELS;
import com.ibm.gil.util.UtilGilConstants;
import com.ibm.igf.hmvc.PluginConfigurationPane;
import com.ibm.igf.webservice.util.CreateOfferLetterBuilder;
import com.ibm.xmlns.ims._1_3.getfinancialofferingletter_2004_11_30.GetFinancialOfferingLetter;
import com.ibm.xmlns.ims._1_3.showfinancialofferingletter_2004_11_30.ShowFinancialOfferingLetter;

import financing.tools.gcps.ws.service.createofferletter.request.CreateOfferLetter;
import financing.tools.gcps.ws.service.createofferletter.request.CreateOfferLetterFactory;


/**
 * @author ferandra
 * 
 */
public class GCPSCreateOL extends GCPSWebService{
	
    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(GCPSCreateOL.class);
    private CreateOfferLetter createOfferLetter = null;
    private String urlService=null;
    
    private String getUrlWS(){
    	    
    	if(urlService==null){
    		urlService=PluginConfigurationPane.getPropertiesMap().get(UtilGilConstants.GCMS_SERVER)+UtilGilConstants.CREATEOL_WS;
    	}
    	
    	logger.info("URL Service: "+urlService);
    	
    	return urlService ;
    }
    
    private String getServiceName(){
	    
    	return UtilGilConstants.CREATEOL_WS;
    }
    
    
    private void loadCreateOfferLetter(OfferingLetterELS OLDM) {
    	
        ObjectTransform.transform(OLDM, createOfferLetter, new String[] { "OBJECTID", "COUNTRY", "OFFERINGNUMBER" });
        createOfferLetter.setApplicationName("GIL");
        createOfferLetter.setCmScanDate(OLDM.getCreatedTimeStampAsDate().getTime());
        createOfferLetter.setBodId(OLDM.getUNIQUEREQUESTNUMBER());
        createOfferLetter.setSourceIndc("ROF");
    }
    
    public ShowFinancialOfferingLetter createOfferingLetter(OfferingLetterELS aOLDM) throws Exception  {
    	
    	ShowFinancialOfferingLetter showOfferLetter = null;
    	
        try {
            showOfferLetter = doCreateOL(aOLDM);
        } catch (Exception exc) {
        	exc.printStackTrace();
        	logger.fatal(exc);
            throw exc;
        }
        return showOfferLetter;
    }
    
    public ShowFinancialOfferingLetter doCreateOL(final OfferingLetterELS OLDM) throws Exception
    {
        try
        {
        	
        	CreateOfferLetterBuilder olBuilder = new CreateOfferLetterBuilder(OLDM);
            createOfferLetter = CreateOfferLetterFactory.singleton().create();
            loadCreateOfferLetter(OLDM);
            GetFinancialOfferingLetter getFinancialOfferingLetter = olBuilder.buildCreateOfferingLetter();
        	//CreateOfferingLetterConsumer createOlConsumer = new CreateOfferingLetterConsumer(getUrlWS(),getGCMSId(),getGCMSAccess());
            CreateOfferingLetterConsumer createOlConsumer = new CreateOfferingLetterConsumer(OLDM.getGcmsEndpoint().getWSUrl()+getServiceName(),OLDM.getGcmsEndpoint().getFunctionalId(), OLDM.getGcmsEndpoint().getAccess());
        	ShowFinancialOfferingLetter showFinancialOfferingLetter = createOlConsumer.callGcpsCreateOL(getFinancialOfferingLetter);
        	
            return showFinancialOfferingLetter;
            
        }  catch (JAXBException je) {	
			  logger.fatal("Error in Web Service GCPSCreateOL: "+ je.getMessage());
			  je.printStackTrace();
			  logger.fatal(je.toString());
			  throw je;
		   
		} catch (ParserConfigurationException pce) {
			logger.fatal("Error in Web Service GCPSCreateOL: "+ pce.getMessage());
			pce.printStackTrace();
			logger.fatal(pce.toString());
			throw pce;
			
		} catch (NullPointerException e) {
			logger.fatal("Error in Web Service GCPSCreateOL: "+ e.getMessage());
			e.printStackTrace();
			logger.fatal(e.toString());
			throw e;
			
		} catch (Exception e) {
			logger.fatal("Error in Web Service GCPSCreateOL: "+ e.getMessage());
			e.printStackTrace();
			throw e;
		}
    }
    

}
