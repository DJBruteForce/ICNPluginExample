package com.ibm.igf.webservice;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

import com.ibm.gcms.consumer.GetCOAConsumer;
import com.ibm.gil.business.COAELS;
import com.ibm.gil.util.UtilGilConstants;
import com.ibm.igf.hmvc.PluginConfigurationPane;
import com.ibm.igf.webservice.util.GetCOABuilder;
import com.ibm.igf.webservice.util.ShowCOAResponse;
import com.ibm.xmlns.igf.gcps.showcoa.ShowCOA;

import financing.tools.gcps.ws.domain.Coa;

/**
 * 
 * @author ferandra
 *
 */
public class GCPSGetCOA extends GCPSWebService {
	
	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(GCPSUpdateCOA.class);    
   
	private String urlService=null;
	 
    private String getServiceName(){
	    
    	return UtilGilConstants.GETCOA_WS;
    }
    
    private String getUrlWS(){
	    
    	if(urlService==null){
    		urlService=PluginConfigurationPane.getPropertiesMap().get(UtilGilConstants.GCMS_SERVER)+UtilGilConstants.GETCOA_WS;
    	}
    	
    	logger.info("URL Service: "+urlService);
    	
    	return urlService ;
    }
    

    public ShowCOA doGetCOA(COAELS COADM) throws Exception
    {
    	ShowCOA showCoa = null;
    	
		try {			
				GetCOABuilder getCoa = new GetCOABuilder(COADM);
				logger.info("URL Service: "+COADM.getGcmsEndpoint().getWSUrl()+getServiceName());
				System.out.println(COADM.getGcmsEndpoint().getFunctionalId());
				System.out.println(COADM.getGcmsEndpoint().getAccess());
				//GetCOAConsumer getCOAWs = new GetCOAConsumer(getUrlWS(), getGCMSId(),getGCMSAccess());
				GetCOAConsumer getCOAWs = new GetCOAConsumer(COADM.getGcmsEndpoint().getWSUrl()+getServiceName(),COADM.getGcmsEndpoint().getFunctionalId(), COADM.getGcmsEndpoint().getAccess());
				
				showCoa = getCOAWs.callGcpsGetCoa(getCoa.buildGetCOA());
			    
				if (showCoa == null) {
					return null;
			    }
				
				if(showCoa.getApplicationArea()!=null &&
					showCoa.getApplicationArea().getMessages().get(0).getReturnCode().equals("gcps.messages.service.get.coa.success")){
				} else {
					return null;
				}
		
		} catch (JAXBException je) {	
			  logger.fatal("Error in Web Service GCPSGetCOA: "+ je.getMessage());
			  je.printStackTrace();
			  logger.fatal(je.toString());
			  throw je;
	   
	} catch (ParserConfigurationException pce) {
			logger.fatal("Error in Web Service GCPSGetCOA: "+ pce.getMessage());
			pce.printStackTrace();
			logger.fatal(pce.toString());
			throw pce;
		
	} catch (NullPointerException e) {
			logger.fatal("Error in Web Service GCPSGetCOA: "+ e.getMessage());
			e.printStackTrace();
			logger.fatal(e.toString());
			throw e;
		
	} catch (Exception e) {
			logger.fatal("Error in Web Service GCPSGetCOA: "+ e.getMessage());
			e.printStackTrace();
			throw e;
	}

	return showCoa;
    }

    public ArrayList<COAELS> getCOA(COAELS aCOADM) throws Exception  {
    	
    	 ArrayList<COAELS> coas = null;
    	
    	try {
	    	    ShowCOA showCoa = doGetCOA(aCOADM);
	    	    
	    	    if (showCoa == null) {
	    	    	return new ArrayList<COAELS>();
	    	    }
	
	    	    ShowCOAResponse showCoaResp = new ShowCOAResponse(showCoa);
	    	    
	    	    List<Coa> olList = showCoaResp.getCOAs();
	    	    List<String>  messages = showCoaResp.getReturnMessages();
	    	    showMessages(messages);
	    	    if (olList == null) {
	    	    	return new ArrayList<COAELS>();
	    	    }
	
	    	    coas = new ArrayList<COAELS>(olList.size());
	    	    for (int i = 0; ((olList != null) && (i < olList.size())); i++) {
	    	    	
		    		Coa coa = (Coa) olList.get(i);
		    		COAELS retrievedCOAELS = new COAELS();
		    		retrievedCOAELS.setLocale(aCOADM.getLocale());
		    		transformShowCoa(coa, retrievedCOAELS);
		    		coas.add(retrievedCOAELS);
	    	    }

    	} catch (Exception exc) {
    		exc.printStackTrace();
    	    logger.fatal("Error retrieving COA information:"+exc.toString());
    	    throw exc;
    	}
		return coas;
    	
    }

    private void transformShowCoa(Coa aCOA, COAELS coaels) {
        ObjectTransform.transform(aCOA, coaels, new String[] { "CoaNumber", "TotalFinancedAmount", "Currency", "CustomerName", "CountryCode", "OfferLetterNumber", "QuoteValidityStartDate", "IsLockIndc", "Status" });
    }
}
