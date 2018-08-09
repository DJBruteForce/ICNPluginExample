package com.ibm.igf.webservice.util;

import com.ibm.gil.business.OfferingLetterELS;
import com.ibm.xmlns.igf.gcps.getofferingletter.ApplicationArea;
import com.ibm.xmlns.igf.gcps.getofferingletter.DataArea;
import com.ibm.xmlns.igf.gcps.getofferingletter.GetOfferingLetterRequest;

/**
 * 
 * @author ferandra
 *
 */
public class GetOfferingLetterBuilder {
	
	private String oLNum;
	private String uniqueReqNum;
	private String country;
	private String cmObjectId;
	private GetOfferingLetterRequest offerLetterToSend;
	private final static String APP_ID="GIL";
	
	public GetOfferingLetterBuilder(OfferingLetterELS oLS){
		
		this.oLNum=oLS.getOFFERINGNUMBER();
		this.country=oLS.getCOUNTRY();
		this.cmObjectId=oLS.getOBJECTID();
		oLS.generateUNIQUEREQUESTNUMBER();
		this.uniqueReqNum=oLS.getUNIQUEREQUESTNUMBER();
	}
	
	public GetOfferingLetterRequest buildGetOfferingLetter() throws Exception{
		
		setOfferLetter(createDataArea(), createApplicationArea());
		return offerLetterToSend;
	}
	private void setOfferLetter(DataArea dataArea, ApplicationArea appArea){
		
		 offerLetterToSend= new GetOfferingLetterRequest();
		 offerLetterToSend.setDataArea(dataArea);
		 offerLetterToSend.setApplicationArea(appArea);
	 }

	 private DataArea createDataArea(){
		 
		 DataArea da=new DataArea();
		 da.setCMObjectId(this.cmObjectId);
		 da.setCountryCode(this.country);
		 da.setOfferingLetterNumber(this.oLNum);
		 return da; 
	 }
	 
	 private ApplicationArea createApplicationArea(){
		 
		 ApplicationArea aa=new ApplicationArea();
		 aa.setApplicationId(APP_ID);
		 aa.setDocumentId(this.uniqueReqNum);
		 return aa;
	 }
	
}
