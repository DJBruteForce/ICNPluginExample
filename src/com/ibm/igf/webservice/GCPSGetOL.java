package com.ibm.igf.webservice;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;



import com.ibm.gcms.consumer.GetOfferingLetterConsumer;
import com.ibm.gil.business.LineItemELS;
import com.ibm.gil.business.OfferingLetterELS;
import com.ibm.gil.util.UtilGilConstants;
import com.ibm.igf.hmvc.PluginConfigurationPane;
import com.ibm.igf.webservice.util.GetOfferingLetterBuilder;
import com.ibm.igf.webservice.util.ShowOfferingLetterResponse;
import com.ibm.xmlns.igf.gcps.showofferingletter.ShowOfferingLetter;
import financing.tools.gcps.ws.domain.OfferLetter;
import financing.tools.gcps.ws.domain.QuoteHeader;
import financing.tools.gcps.ws.domain.jaxb.QuoteDetail_jaxb;

public class GCPSGetOL extends GCPSWebService {

	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(GCPSGetOL.class);
	private String urlService = null;

	private String getUrlWS() {

		if (urlService == null) {
			urlService = PluginConfigurationPane.getPropertiesMap().get(
					UtilGilConstants.GCMS_SERVER)
					+ UtilGilConstants.GETOL_WS;
		}

		logger.info("URL Service: " + urlService);

		return urlService;
	}
	
	private String getServiceName() {

		return UtilGilConstants.GETOL_WS;
	}
	

	public ShowOfferingLetter doGetOL(OfferingLetterELS offerLetterEls) {

		ShowOfferingLetter showOfferLetter = null;
		try {

			GetOfferingLetterBuilder getOfferingLetter = new GetOfferingLetterBuilder(offerLetterEls);
			
			//GetOfferingLetterConsumer getOlWs = new GetOfferingLetterConsumer(getUrlWS(), getGCMSId(), getGCMSAccess());
			GetOfferingLetterConsumer getOlWs = new GetOfferingLetterConsumer(offerLetterEls.getGcmsEndpoint().getWSUrl()+getServiceName(),offerLetterEls.getGcmsEndpoint().getFunctionalId(), offerLetterEls.getGcmsEndpoint().getAccess());
			showOfferLetter = getOlWs.callGcpsGetOfferingLetter(getOfferingLetter.buildGetOfferingLetter());

			if (showOfferLetter == null) {
				return null;
			}

			if (showOfferLetter.getApplicationArea() != null
					&& showOfferLetter
							.getApplicationArea()
							.getMessage()
							.get(0)
							.getReturnCode()
							.equals("gcps.messages.service.get.offer.letter.success")) {
			} else {
				return null;
			}

		} catch (JAXBException je) {
			logger.fatal("Error in Web Service GCPSGetOL: " + je.getMessage());
			je.printStackTrace();
			logger.debug(je.toString());

		} catch (ParserConfigurationException pce) {
			logger.fatal("Error in Web Service GCPSGetOL: " + pce.getMessage());
			pce.printStackTrace();
			logger.debug(pce.toString());

		} catch (NullPointerException e) {
			logger.fatal("Error in Web Service GCPSGetOL: " + e.getMessage());
			e.printStackTrace();
			logger.debug(e.toString());

		} catch (Exception e) {
			logger.fatal("Error in Web Service GCPSGetOL: " + e.getMessage());
			logger.debug(e.getMessage());
			e.printStackTrace();
		}

		return showOfferLetter;
	}

	public ArrayList<OfferingLetterELS> getOfferingLetter(OfferingLetterELS oL) {
		try {

			ShowOfferingLetter showOfferLetter = doGetOL(oL);

			if (showOfferLetter == null) {
				return new ArrayList<OfferingLetterELS>();
			}

			ShowOfferingLetterResponse showOL = new ShowOfferingLetterResponse(
					showOfferLetter);

			List<OfferLetter> olList = showOL.getOfferingLetters();
			List<String> messages = showOL.getReturnMessages();
			showMessages(messages);
			if (olList == null) {
				return new ArrayList<OfferingLetterELS>();
			}

			ArrayList<OfferingLetterELS> offeringLetters = new ArrayList<OfferingLetterELS>(
					olList.size());
			for (int i = 0; ((olList != null) && (i < olList.size())); i++) {

				OfferLetter aOfferLetter = (OfferLetter) olList.get(i);
				OfferingLetterELS retrievedOLS = new OfferingLetterELS();
				transformShowOfferLetter(aOfferLetter, retrievedOLS);

				QuoteHeader aQuoteHeader = aOfferLetter.getQuoteHeader();
				if (aQuoteHeader != null) {

					transformQuoteHeader(aQuoteHeader, retrievedOLS);
					List quoteDetails = aOfferLetter.getQuoteHeader()
							.getQuoteDetails();
					ArrayList quoteLineItems = new ArrayList();

					for (int j = 0; ((quoteDetails != null) && (j < quoteDetails
							.size())); j++) {

						QuoteDetail_jaxb aQuoteDetail = (QuoteDetail_jaxb) quoteDetails
								.get(j);
						LineItemELS aLineItemDataModel = new LineItemELS(
								oL.getCOUNTRY());
						transformQuoteDetail(aQuoteDetail, aLineItemDataModel);
						quoteLineItems.add(aLineItemDataModel);
					}
					retrievedOLS.setQuoteLineItems(quoteLineItems);
				}
				// add it to the return list
				offeringLetters.add(retrievedOLS);
			}

			// return the list
			return (offeringLetters);

		} catch (Exception exc) {
			logger.fatal("Error retrieving offering letter information:"
					+ exc.getMessage());
			exc.printStackTrace();
		}

		return null;
	}

	private void transformShowOfferLetter(OfferLetter aOfferLetter,
			OfferingLetterELS oLS) {

		ObjectTransform.transform(aOfferLetter, oLS,
				new String[] { "CmObjectId", "OfferLetterNumber",
						"AmtFinanced", "AmtFinCurrency", "CountryCode",
						"Status", "IsLockIndc", "ResponsibleId",
						"QuoteValidityStartDate", "QuoteValidityEndDate",
						"CustomerSignature", "CustomerSignDate", "IGFSignDate",
						"ExternalOfferLetterNumber", "SourceIndicator",
						"RollupIndc", "QuoteNum", "QuoteVersionNum" });
	}

	// story 1497875 - added BillEntityIndicator to the string array
	private void transformQuoteHeader(QuoteHeader aQuoteHeader,
			OfferingLetterELS aOfferingLetterDataModel) {
		ObjectTransform.transform(aQuoteHeader, aOfferingLetterDataModel,
				new String[] { "CustomerNum", "CustomerName", "DealType",
						"BillEntityIndicator" });
	}

	private void transformQuoteDetail(QuoteDetail_jaxb aQuoteDetail,
			LineItemELS aLineItemDataModel) {
		ObjectTransform.transform(aQuoteDetail, aLineItemDataModel,
				new String[] { "MachineType", "MachineModel",
						"NetPurchasePrice", "MachineQuantity", "ItemNumber",
						"SubItemNumber", "ExtensionIndicator",
						"TransactionType", "Term", "TypeOfFinance" });
	}

}
