package com.ibm.igf.webservice.util;

import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;



import com.ibm.gil.business.COAELS;
import com.ibm.xmlns.igf.gcps.updatecoa.UpdateCOARequest;

/**
 * 
 * @author ferandra
 *
 */
public class UpdateCOABuilder {
	
	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(UpdateCOABuilder.class);
	
	private String coaNum;
	private String uniqueReqNum;
	private String country;
	private String signedBy;
	private long scanTimeStamp;
	private String cmObjectId;
	private UpdateCOARequest coaRequestToSend;
	
	private final static String APP_ID="GIL";
	
	public UpdateCOABuilder(COAELS coa) {
		
		this.coaNum = coa.getCOANUMBER();
		this.country = coa.getCOUNTRY();
		this.cmObjectId = coa.getOBJECTID();
		coa.generateUNIQUEREQUESTNUMBER();
		this.uniqueReqNum=coa.getUNIQUEREQUESTNUMBER();
		this.signedBy = coa.getSIGNEDBY();
		this.scanTimeStamp = coa.getCreatedTimeStampAsDate().getTime();
	}
	
	public UpdateCOARequest buildGetCOA() throws Exception{
		
		setCOA(createDataArea(), createApplicationArea());
		return coaRequestToSend;
	}
	private void setCOA(com.ibm.xmlns.igf.gcps.updatecoa.DataArea dataArea, com.ibm.xmlns.igf.gcps.updatecoa.ApplicationArea appArea){
		
		coaRequestToSend= new UpdateCOARequest();
		coaRequestToSend.setDataArea(dataArea);
		coaRequestToSend.setApplicationArea(appArea);
	 }

	 private com.ibm.xmlns.igf.gcps.updatecoa.DataArea createDataArea(){
		 
		 com.ibm.xmlns.igf.gcps.updatecoa.DataArea da=new com.ibm.xmlns.igf.gcps.updatecoa.DataArea();
		 da.setCountryCode(this.country);
		 da.setCOANumber(this.coaNum);
		 da.setCOACMObjectId(this.cmObjectId); 
		 da.setCOASignature(this.signedBy);

		 GregorianCalendar calendar = new GregorianCalendar();
		 calendar.setTimeInMillis(this.scanTimeStamp);
			
			try {
				da.setScanTimestamp(DatatypeFactory.newInstance().newXMLGregorianCalendar( calendar));
			} catch (DatatypeConfigurationException e) {			
				e.printStackTrace();
				logger.error(e);
			}
		 
		 return da;
		 
	 }
	 
	 private com.ibm.xmlns.igf.gcps.updatecoa.ApplicationArea createApplicationArea(){
		 
		 com.ibm.xmlns.igf.gcps.updatecoa.ApplicationArea aa=new com.ibm.xmlns.igf.gcps.updatecoa.ApplicationArea();
		 aa.setApplicationId(APP_ID);
		 aa.setDocumentId(this.uniqueReqNum);
		 return aa;
	 }
	
}
