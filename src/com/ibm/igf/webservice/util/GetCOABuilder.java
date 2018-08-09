package com.ibm.igf.webservice.util;

import com.ibm.gil.business.COAELS;
import com.ibm.xmlns.igf.gcps.getcoa.ApplicationArea;
import com.ibm.xmlns.igf.gcps.getcoa.DataArea;
import com.ibm.xmlns.igf.gcps.getcoa.GetCOARequest;

public class GetCOABuilder {
	
	private String coaNum;
	private String uniqueReqNum;
	private String country;
	private String cmObjectId;
	private GetCOARequest coaRequestToSend;
	private final static String APP_ID="GIL";
	
	public GetCOABuilder(COAELS coa) {
		
		this.coaNum = coa.getCOANUMBER();
		this.country = coa.getCOUNTRY();
		this.cmObjectId = coa.getOBJECTID();
		coa.generateUNIQUEREQUESTNUMBER();
		this.uniqueReqNum=coa.getUNIQUEREQUESTNUMBER();	
	}
	
	public GetCOARequest buildGetCOA() throws Exception{
		
		setCOA(createDataArea(), createApplicationArea());
		return coaRequestToSend;
	}
	private void setCOA(DataArea dataArea, ApplicationArea appArea){
		
		coaRequestToSend= new GetCOARequest();
		coaRequestToSend.setDataArea(dataArea);
		coaRequestToSend.setApplicationArea(appArea);
	 }

	 private DataArea createDataArea(){
		 
		 DataArea da=new DataArea();
		 da.setCMObjectId(this.cmObjectId);
		 da.setCountryCode(this.country);
		 da.setCOANumber(this.coaNum);
		 return da; 
	 }
	 
	 private ApplicationArea createApplicationArea(){
		 
		 ApplicationArea aa=new ApplicationArea();
		 aa.setApplicationId(APP_ID);
		 aa.setDocumentId(this.uniqueReqNum);
		 return aa;
	 }
	
}
