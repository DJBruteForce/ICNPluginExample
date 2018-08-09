package com.ibm.igf.webservice.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import com.ibm.xmlns.igf.gcps.showcoa.Configuration;
import com.ibm.xmlns.igf.gcps.showcoa.Messages;
import com.ibm.xmlns.igf.gcps.showcoa.ShowCOA;

import financing.tools.gcps.ws.domain.Coa;
import financing.tools.gcps.ws.domain.jaxb.Coa_jaxb;

public class ShowCOAResponse {

	
	private ShowCOA showCOARetrieved = null;


	public ShowCOAResponse(ShowCOA showCoaRetrieved){
		this.showCOARetrieved=showCoaRetrieved;
	}
	
	
	/**
	 * Get application id.
	 *
	 * @return String
	 */
	public String getApplicationId() {
		return showCOARetrieved.getApplicationArea().getApplicationId();
	}

	/**
	 * Get document id.
	 *
	 * @return String
	 */
	public String getDocumentId() {
		return showCOARetrieved.getApplicationArea().getDocumentId();
	}

	/**
	 * Get return codes.
	 *
	 * @return List
	 */
	public List<String> getReturnCodes() {
		List<String> returnCodes = new ArrayList<String>();
		List<Messages> messages = showCOARetrieved.getApplicationArea().getMessages();
		Iterator<Messages> rcInt = messages.iterator();
		while (rcInt.hasNext()) {
			Messages code = (Messages) rcInt.next();
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
		List<Messages> messages = showCOARetrieved.getApplicationArea().getMessages();
		Iterator<Messages> rcInt = messages.iterator();
		while (rcInt.hasNext()) {
			Messages message = (Messages) rcInt.next();
			returnCodes.add(message.getReturnMessage());
		}
		return returnCodes;
	}

	/**
	 * Get offering letters.
	 *
	 * @return List<OfferLetter>
	 */
	public List<Coa> getCOAs() {
		
	    Calendar cal1 = null;
	    Calendar cal2 = null;
	    List<Coa> listOfCoas = new ArrayList<Coa>();
	    
		List<Configuration> coaConfigurations = showCOARetrieved.getDataArea().getConfiguration();
		Iterator<Configuration> coaConfiguration = coaConfigurations.iterator();
		
		while (coaConfiguration.hasNext()) {
			
			Configuration configuration = (Configuration) coaConfiguration.next();
			Coa coa = new Coa_jaxb();
			
			if(configuration.getCountryCode()!=null) {
				coa.setCountryCode(configuration.getCountryCode());
			}
			
			if (configuration.getCOANumber()!=null) {
				coa.setCoaNumber(configuration.getCOANumber());
			}
			
			if (configuration.getCreateCurrency()!=null) {
				coa.setCurrency(configuration.getCreateCurrency());
			}
			
			if(configuration.getOfferingLetter().getOfferLetterNumber()!=null) {
				coa.setOfferLetterNumber(configuration.getOfferingLetter().getOfferLetterNumber());
			}
			
			if(configuration.getOfferingLetter().getQuoteCustomerName()!=null) {
				coa.setCustomerName(configuration.getOfferingLetter().getQuoteCustomerName());
			}
			
			if(configuration.getTotalFinancedAmount()!=null) {
				coa.setTotalFinancedAmount(configuration.getTotalFinancedAmount());
			}
			
			if(configuration.getStatus()!=null) {
				coa.setStatus(configuration.getStatus());
			}
			
			if(configuration.getIsLockedIndicator()!=null) {
				coa.setIsLockIndc(configuration.getIsLockedIndicator());
			}
			
			if (configuration.getOfferingLetter().getQuoteValidityStartDate()!= null)
			{
			    cal2 = Calendar.getInstance();
			    cal2.clear();
			    cal2.setTimeZone(TimeZone.getTimeZone("GMT"));
			    cal2.setTimeInMillis(configuration.getOfferingLetter().getQuoteValidityStartDate().toGregorianCalendar().getTimeInMillis());
			    cal1 = Calendar.getInstance();
			    cal1.clear();
			    cal1.set(cal2.get(Calendar.YEAR),
			            cal2.get(Calendar.MONTH),
			            cal2.get(Calendar.DATE),
			            0, 0, 1);
			    coa.setQuoteValidityStartDate(cal1.getTimeInMillis());
			}
			
			listOfCoas.add(coa);	
			
		}

		
		return listOfCoas;
	}

	/**
	 * Set application id.
	 *
	 * @param applicationId String
	 */
	public void setApplicationId(String applicationId) {
		showCOARetrieved.getApplicationArea().setApplicationId(applicationId);
	}




		
	
	
	
}
