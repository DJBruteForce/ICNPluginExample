package com.ibm.igf.hmvc;

import java.util.HashMap;
import java.util.List;

import com.ibm.gil.business.WebServicesEndpoint;
import com.ibm.gil.model.WebServicesEnpointDataModel;

public class WebServicesEnpointManager {
	
	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(WebServicesEnpointManager.class);
	
	   public enum SYSTEMS{
	       RDC, GCMS;
	   }
	   
	private  static WebServicesEndpoint defaultGCMSEndpoint;
	
	private  static WebServicesEndpoint defaultRDCEndpoint;

	private static HashMap<String, List<WebServicesEndpoint>> wsEnpointsMap;
	
	public static HashMap<String, List<WebServicesEndpoint>> getWsEnpointsMap() {
		return wsEnpointsMap;
	}
	
	public static List<WebServicesEndpoint> getWsEnpointsBySystem(SYSTEMS system) {
		return wsEnpointsMap.get(system.toString());
	}
	
	public static void  loadWebServicesEndpointMap() {
		
		WebServicesEnpointDataModel datamodel = new WebServicesEnpointDataModel();
		wsEnpointsMap = datamodel.loadWebServicesEndpoints();	
		setDefaultEndpoints();
		
	}
	
	public static WebServicesEndpoint getDefaultEndpoint(SYSTEMS system) {
		
		if(system.equals(SYSTEMS.RDC)) {
			return defaultRDCEndpoint;
		} else if (system.equals(SYSTEMS.GCMS)) {
			return defaultGCMSEndpoint;
		}
		return null;	
	}
	
	
	
	
	public static void setDefaultEndpoints() {
		
		if(wsEnpointsMap!=null && !wsEnpointsMap.isEmpty()) {
			List<WebServicesEndpoint> listOfEndPoints = wsEnpointsMap.get(SYSTEMS.GCMS.toString());
			for(WebServicesEndpoint ep: listOfEndPoints){
				if(ep.isDefaultEp())
					defaultGCMSEndpoint = ep;
			}
			
			List<WebServicesEndpoint> listOfEndPoints2 = wsEnpointsMap.get(SYSTEMS.RDC.toString());
			for(WebServicesEndpoint ep: listOfEndPoints2){
				if(ep.isDefaultEp())
					defaultRDCEndpoint = ep;
			}
			
		}
	}
	


}