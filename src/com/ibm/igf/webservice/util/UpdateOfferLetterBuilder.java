package com.ibm.igf.webservice.util;

import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;



import com.ibm.gil.business.OfferingLetterELS;
import com.ibm.xmlns.igf.gcps.updateofferringletter.UpdateOfferingLetterRequest;


/**
 * 
 * @author ferandra
 *
 */
public class UpdateOfferLetterBuilder {
	
	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(UpdateOfferLetterBuilder.class);
   
	private String uniqueReqNum;
    private String country;
	private String olNumber;
	private String cmObjectId;
	private String signedCounterSigned;
	private String customerSignature;
	private long customerSignatureDate;
	private long ibmSignatureDate;
	private long receiptDate;

	
	private UpdateOfferingLetterRequest olRequestToSend;
	
	private final static String APP_ID="GIL";
	
	public UpdateOfferLetterBuilder(OfferingLetterELS ol) {
		
		ol.generateUNIQUEREQUESTNUMBER();
		this.uniqueReqNum= ol.getUNIQUEREQUESTNUMBER();
		this.olNumber =  ol.getOFFERINGNUMBER();
		this.country =  ol.getCOUNTRY();
		this.cmObjectId =  ol.getOBJECTID();
		this.signedCounterSigned =  ol.getSIGNEDCOUNTERSIGNED();
		this.customerSignature =  ol.getCUSTOMERSIGNATURE();
		this.receiptDate = ol.getCreatedTimeStampAsDate().getTime();
		
		if (ol.getCUSTOMERSIGNATUREDATEasDate()!=null){
			this.customerSignatureDate =  ol.getCUSTOMERSIGNATUREDATEasDate().getTime();
		}
		
		if (ol.getIBMSIGNATUREDATEasDate()!=null){
			this.ibmSignatureDate =  ol.getIBMSIGNATUREDATEasDate().getTime();
		}
		
	}
	
	public UpdateOfferingLetterRequest buildUpdateOfferingLetter() throws Exception{
		
		setOfferingLetter(createDataArea(), createApplicationArea());
		return olRequestToSend;
	}
	private void setOfferingLetter(com.ibm.xmlns.igf.gcps.updateofferringletter.DataArea dataArea, com.ibm.xmlns.igf.gcps.updateofferringletter.ApplicationArea appArea){
		
		olRequestToSend= new UpdateOfferingLetterRequest();
		olRequestToSend.setDataArea(dataArea);
		olRequestToSend.setApplicationArea(appArea);
	 }

	 private com.ibm.xmlns.igf.gcps.updateofferringletter.DataArea createDataArea(){
		 
			com.ibm.xmlns.igf.gcps.updateofferringletter.DataArea da=new com.ibm.xmlns.igf.gcps.updateofferringletter.DataArea();
			GregorianCalendar custSignDt = new GregorianCalendar();
			GregorianCalendar ibmSignDt = new GregorianCalendar();
			GregorianCalendar recDt = new GregorianCalendar(); 
		 
			da.setOfferingLetterNumber(this.olNumber);
		    da.setCountryCode(this.country);
			da.setSignedOrCounterSigned(this.signedCounterSigned);
			da.setCustomerSignature(this.customerSignature);
			da.setOfferingLetterCMObjectId(this.cmObjectId);
	 
			try { 
					if (this.customerSignatureDate > 0) {
						 custSignDt.setTimeInMillis(this.customerSignatureDate);
						 da.setCustomerSignDate(DatatypeFactory.newInstance().newXMLGregorianCalendar( custSignDt));
					} else { 
						da.setCustomerSignDate(null);
					}
					 
				}catch (DatatypeConfigurationException e) { 
					logger.fatal(e);	
				}
			
			try {
					if (this.ibmSignatureDate > 0) {
						 ibmSignDt.setTimeInMillis(this.ibmSignatureDate);
						 da.setIGFSignDate(DatatypeFactory.newInstance().newXMLGregorianCalendar( ibmSignDt));
					} else {
						 da.setIGFSignDate(null);
					}
					 
				} catch (DatatypeConfigurationException e) {  
					logger.fatal(e); 
				}
			
			try {
					if (this.receiptDate > 0) {
						 recDt.setTimeInMillis(this.receiptDate);
						 da.setReceiptDate(DatatypeFactory.newInstance().newXMLGregorianCalendar( recDt));
					} else {
						 da.setReceiptDate(null);
					}
					 
				} catch (DatatypeConfigurationException e) { 
					logger.fatal(e); 
				}

		 return da;
		 
	 }
	 private com.ibm.xmlns.igf.gcps.updateofferringletter.ApplicationArea createApplicationArea(){
		 
		 com.ibm.xmlns.igf.gcps.updateofferringletter.ApplicationArea aa=new com.ibm.xmlns.igf.gcps.updateofferringletter.ApplicationArea();
		 aa.setApplicationId(APP_ID);
		 aa.setDocumentId(this.uniqueReqNum);
		 
		 return aa;
	 }
	
}
