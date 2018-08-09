package com.ibm.igf.webservice.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import com.ibm.xmlns.igf.gcps.showofferingletter.Message;
import com.ibm.xmlns.igf.gcps.showofferingletter.OfferingLetter;
import com.ibm.xmlns.igf.gcps.showofferingletter.OfferingLetterQuoteDetail;
import com.ibm.xmlns.igf.gcps.showofferingletter.ShowOfferingLetter;

import financing.tools.gcps.common.util.StringUtil;
import financing.tools.gcps.ws.domain.OfferLetter;
import financing.tools.gcps.ws.domain.QuoteDetail;
import financing.tools.gcps.ws.domain.QuoteHeader;
import financing.tools.gcps.ws.domain.jaxb.OfferLetter_jaxb;
import financing.tools.gcps.ws.domain.jaxb.QuoteDetail_jaxb;
import financing.tools.gcps.ws.domain.jaxb.QuoteHeader_jaxb;

public class ShowOfferingLetterResponse {

	
	private ShowOfferingLetter showOfferingLetterRetrieved = null;


	public ShowOfferingLetterResponse(ShowOfferingLetter showOfferingLetterRetrieved){
		this.showOfferingLetterRetrieved=showOfferingLetterRetrieved;
	}
	
	
	/**
	 * Get application id.
	 *
	 * @return String
	 */
	public String getApplicationId() {
		return showOfferingLetterRetrieved.getApplicationArea().getApplicationId();
	}

	/**
	 * Get document id.
	 *
	 * @return String
	 */
	public String getDocumentId() {
		return showOfferingLetterRetrieved.getApplicationArea().getDocumentId();
	}

	/**
	 * Get return codes.
	 *
	 * @return List
	 */
	public List<String> getReturnCodes() {
		List<String> returnCodes = new ArrayList<String>();
		List<Message> messages = showOfferingLetterRetrieved.getApplicationArea().getMessage();
		Iterator<Message> rcInt = messages.iterator();
		while (rcInt.hasNext()) {
			Message code = (Message) rcInt.next();
			returnCodes.add(code.getReturnCode());
		}
		return returnCodes;
	}

	/**
	 * Get return messages.
	 *
	 * @return List
	 */
	public List<String> getReturnMessages() {
		List<String> returnCodes = new ArrayList<String>();
		List<Message> messages = showOfferingLetterRetrieved.getApplicationArea().getMessage();
		Iterator<Message> rcInt = messages.iterator();
		while (rcInt.hasNext()) {
			Message message = (Message) rcInt.next();
			returnCodes.add(message.getReturnMessage());
		}
		return returnCodes;
	}

	/**
	 * Get offering letters.
	 *
	 * @return List<OfferLetter>
	 */
	public List<OfferLetter> getOfferingLetters() {
	    Calendar cal1 = null;
	    Calendar cal2 = null;
	    
		List<OfferLetter> offerLetters = new ArrayList<OfferLetter>();
		
		List<OfferingLetter> offeringLetters = showOfferingLetterRetrieved.getDataArea().getOfferingLetter();
		Iterator<OfferingLetter> olInt = offeringLetters.iterator();
		while (olInt.hasNext()) {
			OfferingLetter offeringLetter = (OfferingLetter) olInt.next();

			OfferLetter offerLetter = new OfferLetter_jaxb();
			QuoteHeader quoteHeader = new QuoteHeader_jaxb();
			
			offerLetter.setCountryCode(offeringLetter.getCountryCode());
			offerLetter.setOfferLetterNumber(offeringLetter.getOfferingLetterNumber());
			if (offeringLetter.getCreateTimestamp() != null) {
				offerLetter.setCreateTimestamp(offeringLetter.getCreateTimestamp().toGregorianCalendar().getTimeInMillis());
			}
			offerLetter.setCmObjectId(offeringLetter.getCMObjectID());
			offerLetter.setQuoteNum(offeringLetter.getQuoteNumber());
			offerLetter.setQuoteVersionNum(offeringLetter.getQuoteVersionNumber());
			offerLetter.setStatus(offeringLetter.getStatus());
			offerLetter.setCreateCurrency(offeringLetter.getCreateCurrency());
			offerLetter.setCreateLanguage(offeringLetter.getCreateLanguage());
			offerLetter.setCreateBspId(offeringLetter.getBSPCreatorId());
			if (offeringLetter.getRenderTimestamp() != null) {
				offerLetter.setRenderTimestamp(offeringLetter.getRenderTimestamp().toGregorianCalendar().getTimeInMillis());
			}
			offerLetter.setSentId(offeringLetter.getSentId());
			if (offeringLetter.getSentTimestamp() != null) {
				offerLetter.setSentTimestamp(offeringLetter.getSentTimestamp().toGregorianCalendar().getTimeInMillis());
			}
			if (offeringLetter.getReceiptTimestamp() != null) {
				offerLetter.setReceiptTimestamp(offeringLetter.getReceiptTimestamp().toGregorianCalendar().getTimeInMillis());
			}
			if (offeringLetter.getAcceptTimestamp() != null) {
				offerLetter.setAcceptTimestamp(offeringLetter.getAcceptTimestamp().toGregorianCalendar().getTimeInMillis());
			}
			offerLetter.setMailroomId(offeringLetter.getMailroomId());
			if (offeringLetter.getRequestCountersignTimestamp() != null) {
				offerLetter.setRequestCountersignTimestamp(offeringLetter.getRequestCountersignTimestamp().toGregorianCalendar().getTimeInMillis());
			}
			offerLetter.setCountersignId(offeringLetter.getCountersignId());
			if (offeringLetter.getCountersignTimestamp() != null) {
				offerLetter.setCountersignTimestamp(offeringLetter.getCountersignTimestamp().toGregorianCalendar().getTimeInMillis());
			}
			offerLetter.setDeemedAcceptIndc(offeringLetter.getDeemedAcceptanceInd());
			if (offeringLetter.getDeemedAcceptanceDate() != null) {
				offerLetter.setDeemedAcceptDate(offeringLetter.getDeemedAcceptanceDate().toGregorianCalendar().getTimeInMillis());
			}
			offerLetter.setCreditStatus(offeringLetter.getCreditStatus());
			offerLetter.setCreditReqNum(offeringLetter.getCreditRequestNumber());
			offerLetter.setCreditApprovalNum(offeringLetter.getCreditApprovalNumber());
			offerLetter.setClauseId(offeringLetter.getClauseId());
			offerLetter.setAssociatedOl(offeringLetter.getAssociatedOfferingLetter());
			offerLetter.setMasterAgreeNum(offeringLetter.getMasterAgreementNumber());
			offerLetter.setExternalRefNum(offeringLetter.getExternalReferenceNumber());
			offerLetter.setEpricerNum(offeringLetter.getEPricerNumber());
			offerLetter.setSiebelNum(offeringLetter.getSeibelNumber());
			offerLetter.setSraNum(offeringLetter.getSRANumber());
			offerLetter.setRofNum(offeringLetter.getROFNumber());
			offerLetter.setAmendmentNum(offeringLetter.getAmmendmentNumber());
			offerLetter.setRollupIndc(offeringLetter.getRollupIndicator());
			if (offeringLetter.getQuoteValidityStartDate() != null) {
				offerLetter.setQuoteValidityStartDate(offeringLetter.getQuoteValidityStartDate().toGregorianCalendar().getTimeInMillis());
			}
			if (offeringLetter.getQuoteValidityEndDate() != null) {
				offerLetter.setQuoteValidityEndDate(offeringLetter.getQuoteValidityEndDate().toGregorianCalendar().getTimeInMillis());
			}
			offerLetter.setAmtFinanced(offeringLetter.getAmountFinanced().doubleValue());
			offerLetter.setAmtFinCurrency(offeringLetter.getAmountFinancedCurrency());
			offerLetter.setGcpsPayMethod(offeringLetter.getGCPSPaymentMethod());
			offerLetter.setCoaNumSuffix(offeringLetter.getCOANumberSuffix());
			offerLetter.setBlexCoaCreated(offeringLetter.getBLEXCOACreated());
			offerLetter.setResponsibleId(offeringLetter.getResponsibleId());
			offerLetter.setIgfPhoneOverride(offeringLetter.getIGFPhoneOverride());
			offerLetter.setIgfEmailOverride(offeringLetter.getIGFEmailOveride());
			if (offeringLetter.getIGFAddressKey() != null) {
				offerLetter.setIgfAddrKey(offeringLetter.getIGFAddressKey().toString());
			}
			offerLetter.setIssuesIndc(offeringLetter.getIssuesIndicator());
			offerLetter.setOutputFormat(offeringLetter.getOutputFormat());
			offerLetter.setLastUpdateId(offeringLetter.getLastUpdateId());
			if (offeringLetter.getLastUpdateTimestamp() != null) {
				offerLetter.setLastUpdateTimestamp(offeringLetter.getLastUpdateTimestamp().toGregorianCalendar().getTimeInMillis());
			}
			offerLetter.setIsLockIndc(offeringLetter.getIsLockedIndicator());
			offerLetter.setCustomerSignature(offeringLetter.getCustomerSignature());
			if (offeringLetter.getCustomerSignDate() != null)
			{
			    cal2 = Calendar.getInstance();
			    cal2.clear();
			    cal2.setTimeZone(TimeZone.getTimeZone("GMT"));
			    cal2.setTimeInMillis(offeringLetter.getCustomerSignDate().toGregorianCalendar().getTimeInMillis());
			    cal1 = Calendar.getInstance();
			    cal1.clear();
			    cal1.set(cal2.get(Calendar.YEAR),
			            cal2.get(Calendar.MONTH),
			            cal2.get(Calendar.DATE),
			            0, 0, 1);
			    offerLetter.setCustomerSignDate(cal1.getTime());
			}
			if (offeringLetter.getIGFSignDate() != null)
			{
			    cal2 = Calendar.getInstance();
			    cal2.setTimeZone(TimeZone.getTimeZone("GMT"));
			    cal2.setTimeInMillis(offeringLetter.getIGFSignDate().toGregorianCalendar().getTimeInMillis());
			    cal1 = Calendar.getInstance();
			    cal1.set(cal2.get(Calendar.YEAR),
			            cal2.get(Calendar.MONTH),
			            cal2.get(Calendar.DATE),
			            0, 0, 1);
			    offerLetter.setIGFSignDate(cal1.getTime());
			}
			offerLetter.setSourceIndicator(offeringLetter.getSourceIndicator());
			offerLetter.setExternalOfferLetterNumber(offeringLetter.getExternalOfferLetterNumber());
			/**
			 * BUS 216(Allow invoice to be index against any valid OL#)
			 * As part of BUS 216, invoice can be indexed for an offerLetter which dont ahve quote(waiting for quote)
			 * During GetOfferLetter service, we are able to send response back to GIL. But in this scearnio
			 * Quote values in the response will be null and while unmarshalling the response GIL is getting Null Pointer. 
			 * Hence changes are made in this wrapper class to do null check for quote header.
			 */
			if(offeringLetter.getQuoteHeader() != null){
//				 set quote header
				quoteHeader.setQuoteNum(offeringLetter.getQuoteHeader().getQuoteNumber());
				quoteHeader.setQuoteVersionNum(offeringLetter.getQuoteHeader().getQuoteVersionNumber());
				quoteHeader.setEnterpriseNum(offeringLetter.getQuoteHeader().getEnterpriseNumber());
				quoteHeader.setCompanyNum(offeringLetter.getQuoteHeader().getCompanyNumber());
				quoteHeader.setCustomerNum(offeringLetter.getQuoteHeader().getCustomerNumber());
				quoteHeader.setCustomerName(offeringLetter.getQuoteHeader().getCustomerName());
				quoteHeader.setAmtFinanced(0.00);
				if (offeringLetter.getQuoteHeader().getAmountFinanced() != null)
				{
					quoteHeader.setAmtFinanced(offeringLetter.getQuoteHeader().getAmountFinanced().doubleValue());
				}
				quoteHeader.setDealType(offeringLetter.getQuoteHeader().getDealType());
				if(!StringUtil.isNullOrEmptyString(offeringLetter.getQuoteHeader().getBillEntityInd())){
					quoteHeader.setBillEntityIndicator(offeringLetter.getQuoteHeader().getBillEntityInd());					
				}
				quoteHeader.setCreditReqNum(offeringLetter.getQuoteHeader().getCreditRequestNumber());
				quoteHeader.setAgreementType(offeringLetter.getQuoteHeader().getAgreementType());
				quoteHeader.setTcmLclInd1(offeringLetter.getQuoteHeader().getTCMLCLIndicator1());
				if (offeringLetter.getQuoteHeader().getQuoteValidityStartDate() != null) {
					quoteHeader.setQuoteValidityStartDate(
						offeringLetter.getQuoteHeader().getQuoteValidityStartDate().toGregorianCalendar().getTimeInMillis());
				}
				if (offeringLetter.getQuoteHeader().getQuoteValidityEndDate() != null) {
					quoteHeader.setQuoteValidityEndDate(
						offeringLetter.getQuoteHeader().getQuoteValidityEndDate().toGregorianCalendar().getTimeInMillis());
				}
				quoteHeader.setExtnOnlyIndc(offeringLetter.getQuoteHeader().getExtensionOnlyIndicator());
				if (offeringLetter.getQuoteHeader().getCreateTimestamp() != null) {
					quoteHeader.setCreateTimestamp(
						offeringLetter.getQuoteHeader().getCreateTimestamp().toGregorianCalendar().getTimeInMillis());
				}
				quoteHeader.setIbmCountryCode(offeringLetter.getQuoteHeader().getIBMCountryCode());
				quoteHeader.setIcfsSubsidiaryCode(offeringLetter.getQuoteHeader().getICFSSubsidiaryCode());
				
				// R4.0
				// Set Quote Details
				List<QuoteDetail> quoteDetails = new ArrayList<QuoteDetail>();
				
				List<OfferingLetterQuoteDetail> quoteDetailsJAXB = offeringLetter.getQuoteHeader().getQuoteDetail();
				Iterator<OfferingLetterQuoteDetail> quoteDetailsJAXBIter = quoteDetailsJAXB.iterator();
				while(quoteDetailsJAXBIter.hasNext())
				{
				    OfferingLetterQuoteDetail quoteDetailJAXB = (OfferingLetterQuoteDetail) quoteDetailsJAXBIter.next();
				    
				    QuoteDetail quoteDetail = new QuoteDetail_jaxb();
				    
				    quoteDetail.setNetPurchasePrice(quoteDetailJAXB.getNetPurchasePrice().doubleValue());
				    quoteDetail.setMachineType(quoteDetailJAXB.getMachineType());
				    quoteDetail.setMachineModel(quoteDetailJAXB.getMachineModel());
				    quoteDetail.setMachineQuantity(quoteDetailJAXB.getMachineQuantity().intValue());
				    quoteDetail.setPurchasePriceFinanced(quoteDetailJAXB.getPurchasePriceFinanced().doubleValue());
				    quoteDetail.setItemNumber(quoteDetailJAXB.getItemNumber().intValue());
				    quoteDetail.setSubItemNumber(quoteDetailJAXB.getSubItemNumber().intValue());
				    quoteDetail.setTransactionType(quoteDetailJAXB.getTransactionType());
				    quoteDetail.setExtensionIndicator(quoteDetailJAXB.getExtensionIndicator());
				    if (quoteDetailJAXB.getTerm() != null) {
				    	quoteDetail.setTerm(quoteDetailJAXB.getTerm().intValue());
				    }
				    quoteDetail.setTypeOfFinance(quoteDetailJAXB.getFinancingType());
				    
				    quoteDetails.add(quoteDetail);
				}
				
				quoteHeader.setQuoteDetails(quoteDetails);
			}
			
			offerLetter.setQuoteHeader(quoteHeader);

			offerLetters.add(offerLetter);
		}
		
		return offerLetters;
	}

	/**
	 * Set application id.
	 *
	 * @param applicationId String
	 */
	public void setApplicationId(String applicationId) {
		showOfferingLetterRetrieved.getApplicationArea().setApplicationId(applicationId);
	}




		
	
	
	
}
