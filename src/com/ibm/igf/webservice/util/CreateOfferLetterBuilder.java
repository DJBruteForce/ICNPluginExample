package com.ibm.igf.webservice.util;

import java.math.BigInteger;
import java.util.GregorianCalendar;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;


import org.openapplications.oagis._8_2.getfinancialofferingletter_2004_11_30.Address;
import org.openapplications.oagis._8_2.getfinancialofferingletter_2004_11_30.AddressLine;
import org.openapplications.oagis._8_2.getfinancialofferingletter_2004_11_30.AddressLine.Line;
import org.openapplications.oagis._8_2.getfinancialofferingletter_2004_11_30.Addresses;
import org.openapplications.oagis._8_2.getfinancialofferingletter_2004_11_30.Country;
import org.openapplications.oagis._8_2.getfinancialofferingletter_2004_11_30.DocumentIds;
import org.openapplications.oagis._8_2.getfinancialofferingletter_2004_11_30.DocumentReferences;
import org.openapplications.oagis._8_2.getfinancialofferingletter_2004_11_30.Get;
import org.openapplications.oagis._8_2.getfinancialofferingletter_2004_11_30.Id;
import org.openapplications.oagis._8_2.getfinancialofferingletter_2004_11_30.NameValuePair;
import org.openapplications.oagis._8_2.getfinancialofferingletter_2004_11_30.OrderDocumentReference;
import org.openapplications.oagis._8_2.getfinancialofferingletter_2004_11_30.OrganizationalUnit;
import org.openapplications.oagis._8_2.getfinancialofferingletter_2004_11_30.Parties;
import org.openapplications.oagis._8_2.getfinancialofferingletter_2004_11_30.PartyDocumentId;
import org.openapplications.oagis._8_2.getfinancialofferingletter_2004_11_30.PartyInstitutional;
import org.openapplications.oagis._8_2.getfinancialofferingletter_2004_11_30.RequestVerb.ReturnCriteria;
import org.openapplications.oagis._8_2.getfinancialofferingletter_2004_11_30.Revision;
import org.openapplications.oagis._8_2.getfinancialofferingletter_2004_11_30.Sender;

import com.ibm.gil.business.OfferingLetterELS;
import com.ibm.xmlns.ims._1_3.getfinancialofferingletter_2004_11_30.Application;
import com.ibm.xmlns.ims._1_3.getfinancialofferingletter_2004_11_30.ApplicationArea;
import com.ibm.xmlns.ims._1_3.getfinancialofferingletter_2004_11_30.ApplicationDetails;
import com.ibm.xmlns.ims._1_3.getfinancialofferingletter_2004_11_30.FinancialOfferingLetter;
import com.ibm.xmlns.ims._1_3.getfinancialofferingletter_2004_11_30.FinancialOfferingLetterHeader;
import com.ibm.xmlns.ims._1_3.getfinancialofferingletter_2004_11_30.GetFinancialOfferingLetter;
import com.ibm.xmlns.ims._1_3.getfinancialofferingletter_2004_11_30.GetFinancialOfferingLetterDataArea;
import com.ibm.xmlns.ims._1_3.getfinancialofferingletter_2004_11_30.ObjectFactory;
import com.ibm.xmlns.ims._1_3.getfinancialofferingletter_2004_11_30.Transaction;

import financing.tools.gcps.ws.service.createofferletter.request.CreateOfferLetter;



public class CreateOfferLetterBuilder {
	
	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(CreateOfferLetterBuilder.class);
	
	private String uniqueReqNum;
	private String cmObjectId;
	private String country;
	private String olNumber;
	private long createDate;
	private GetFinancialOfferingLetter olRequestToSend;
	private final static String APP_ID="GIL";
	private final static String REVISION="1.3.0";
	private org.openapplications.oagis._8_2.getfinancialofferingletter_2004_11_30.ObjectFactory factory = new org.openapplications.oagis._8_2.getfinancialofferingletter_2004_11_30.ObjectFactory();
	private ObjectFactory imsFactory = new ObjectFactory();
	
	public CreateOfferLetterBuilder(OfferingLetterELS ol) {
		
		ol.generateUNIQUEREQUESTNUMBER();
		this.uniqueReqNum= ol.getUNIQUEREQUESTNUMBER();
		this.olNumber =  ol.getOFFERINGNUMBER();
		this.country =  ol.getCOUNTRY();
		this.cmObjectId =  ol.getOBJECTID();
		this.createDate = ol.getCreatedTimeStampAsDate().getTime();
		
	}
	
	public CreateOfferLetterBuilder(CreateOfferLetter ol) {
		
		this.uniqueReqNum= ol.getBodId();
		this.olNumber =  ol.getOfferLetterNumber();
		this.country =  ol.getCountryCode();
		this.cmObjectId =  ol.getCmObjectId();
		this.createDate = ol.getCmScanDate();
	}
	
	public GetFinancialOfferingLetter buildCreateOfferingLetter() throws Exception{
		
		setOfferingLetter(createDataArea(), createApplicationArea());
		return olRequestToSend;
	}
	private void setOfferingLetter(GetFinancialOfferingLetterDataArea dataArea, ApplicationArea appArea){
		
		olRequestToSend= new GetFinancialOfferingLetter();
		olRequestToSend.setDataArea(dataArea);
		olRequestToSend.setApplicationArea(appArea);
		olRequestToSend.setRevision(REVISION);
		
	 }

	 private GetFinancialOfferingLetterDataArea createDataArea(){

		 GetFinancialOfferingLetterDataArea getfinOLDataArea= imsFactory.createGetFinancialOfferingLetterDataArea();
			
			//Get tag
			//========================================================
			
			Get get = factory.createGet();
			ReturnCriteria returnCriteria = new ReturnCriteria();
			returnCriteria.getSelectExpression().add("Get");
			get.setReturnCriteria(returnCriteria);
			
			getfinOLDataArea.setGet(get);
			
			FinancialOfferingLetter financialOL = imsFactory.createFinancialOfferingLetter();
			FinancialOfferingLetterHeader financialOLHeader = imsFactory.createFinancialOfferingLetterHeader();
			financialOL.setHeader(financialOLHeader);
			
			//Instruction Names tags
			//============================================================
			
			String InstructionName[] = 
					{"VALIDITY_PERIOD","EXTERNAL_REFERENCE","OUTPUT_FORMAT","CLAUSE_ID","ASSOCIATED_OL_NUMBER","CM_SCAN_DATE",
					"DEEMED_ACCEPT_FLAG","DEEMED_ACCEPT_DATE","CREATE_MFA_FLAG","AMENDMENT", "IBM_EMAIL", "COA_BSP","OL_BSP",
					"ROLLUP", "RENDER_NOW", "OFFER_LETTER_NUM",	"IMBED_COA_INDC","SUPPLIER_NAME","SUPPLIER_ADDR_1",
					"SUPPLIER_ADDR_2","SUPPLIER_ADDR_3","SUPPLIER_ADDR_4","ACCT_EXTERNAL_ID","SOURCE_INDC","EXTERNAL_OL_NUM",
					"CM_OBJECT_ID","ERROR_MSG","OVERRIDE_CONTACT_INFO_1","OVERRIDE_CONTACT_INFO_2","SUPPLIER_NUM_1",
					"SUPPLIER_NUM_2","SUPPLIER_NUM_3","SUPPLIER_NUM_4","SUPPLIER_NUM_5","CREATE_BSP_ID","OPERATION_INDC"
					,"COMMENT","OLD_OFFER_LETTER_NUM","VALIDITY_PERIOD_START","VALIDITY_PERIOD_END"};
			
			String InstructionValue[] = {"","","","","",String.valueOf(this.createDate),"","",
										 "","","","","","","","","","","","","","","","ROF",
										 this.olNumber,this.cmObjectId,"","","","",	"","","","",
										 "","","","","",""};
			
			for (int i=0; i<InstructionName.length; i++){
				
				NameValuePair nameValueInstruction = factory.createNameValuePair();
				nameValueInstruction.setName(InstructionName[i]);
				nameValueInstruction.setValue(InstructionValue[i]);
				financialOLHeader.getInstruction().add(nameValueInstruction);
			}
			
			
			//DocumentIds tag
		    //================================================================
			DocumentIds documentsIds = factory.createDocumentIds();
			Id idSupDocId = new Id();
			idSupDocId.setIdOrigin("QUOTE");
			Revision revSupDocId = new Revision();
			revSupDocId.setValue("");
			
			PartyDocumentId partyDocumentId = factory.createPartyDocumentId();
			partyDocumentId.setId(idSupDocId);
			partyDocumentId.setRevision(revSupDocId);
			
			JAXBElement<PartyDocumentId> supplierDocumentId = factory.createSupplierDocumentId(partyDocumentId);
			documentsIds.getDocumentIdType().add(supplierDocumentId);
			financialOLHeader.setDocumentIds(documentsIds);
			
			//DocumentReferences tag
			//====================================================================
			
			
			DocumentReferences documentReferences = factory.createDocumentReferences();
			OrderDocumentReference orderDocumentReference = new OrderDocumentReference();
			JAXBElement<OrderDocumentReference> contractDocumentReference = factory.createContractDocumentReference(orderDocumentReference);
			DocumentIds documentIdsDocRef = factory.createDocumentIds();
			
			//ContractDocumentReference tag
			//=======================================================================
			
			Id idSupDocRef = new Id();
			idSupDocRef.setIdOrigin("MasterAgreement");
			PartyDocumentId partyDocumentIdDocRef = factory.createPartyDocumentId();
			partyDocumentIdDocRef.setId(idSupDocRef);
			JAXBElement<PartyDocumentId> supplierDocumentIdDocRef = factory.createSupplierDocumentId(partyDocumentIdDocRef);
			documentIdsDocRef.getDocumentIdType().add(supplierDocumentIdDocRef);
			orderDocumentReference.setDocumentIds(documentIdsDocRef);
			documentReferences.getDocumentReference().add(contractDocumentReference);
			
			//QuoteDocumentReference tag
			//=======================================================================
			
			OrderDocumentReference orderDocumentReference2 = new OrderDocumentReference();
			DocumentIds documentIdQuoteRef = factory.createDocumentIds();
			JAXBElement<OrderDocumentReference> quoteDocumentReference = factory.createQuoteDocumentReference(orderDocumentReference2);
			Id idQuoteDocRef = new Id();
			idQuoteDocRef.setIdOrigin("CreditRequestNumber");
			PartyDocumentId partyDocumentIdQuoteDoc = factory.createPartyDocumentId();
			partyDocumentIdQuoteDoc.setId(idQuoteDocRef);
			JAXBElement<PartyDocumentId> supplierDocumentIdQuoteDoc= factory.createSupplierDocumentId(partyDocumentIdQuoteDoc);
			documentIdQuoteRef.getDocumentIdType().add(supplierDocumentIdQuoteDoc);
			orderDocumentReference2.setDocumentIds(documentIdQuoteRef);
			documentReferences.getDocumentReference().add(quoteDocumentReference);
			//==============================================
			
			financialOLHeader.setDocumentReferences(documentReferences);
			
			//Parties tag
			//================================================
			//InstalledatParty tag
			
			Parties parties = new Parties();
			PartyInstitutional partyinstitutional = factory.createPartyInstitutional();
			Addresses addresses = factory.createAddresses();
			Address address   = factory.createAddress();
			Country country = new Country();
			country.setValue(this.country);
			country.setIdOrigin("IBM-CMR");
			address.setCountry(country);			
			JAXBElement<Address> primaryAddress = factory.createPrimaryAddress(address);
			addresses.getAddress().add(primaryAddress);
			JAXBElement<PartyInstitutional> JAXBPartyInstitutional = factory.createInstalledAtParty(partyinstitutional);
			partyinstitutional.setAddresses(addresses);
			OrganizationalUnit orgUnit = factory.createOrganizationalUnit();
			orgUnit.setId("");
			partyinstitutional.setBusiness(orgUnit);
			parties.getPartyType().add(JAXBPartyInstitutional);
			
			//Supplier Party tag
			//=========================================================
			
			PartyInstitutional supPartyInstitutional = factory.createPartyInstitutional();
			JAXBElement<PartyInstitutional> JAXBSupPartyInstitutional = factory.createSupplierParty(supPartyInstitutional);
			Addresses supAddresses = factory.createAddresses();
			Address supAddress   = factory.createAddress();
			AddressLine supAddressLine = factory.createAddressLine();
			
			for(int i=1; i<=6; i++){
				Line line = new Line();
				line.setSequence(new BigInteger(String.valueOf(i)));
				supAddressLine.getLine().add(line);
			}
			
			
			supAddress.setAddressLine(supAddressLine);
			JAXBElement<Address> supPrimaryAdress = factory.createPrimaryAddress(supAddress);
			supPartyInstitutional.setAddresses(supAddresses);
			supPartyInstitutional.getAddresses().getAddress().add(supPrimaryAdress);
			parties.getPartyType().add(JAXBSupPartyInstitutional);
			
			//============================================================
			
			financialOLHeader.setParties(parties);
			getfinOLDataArea.getFinancialOfferingLetter().add(financialOL);
			return getfinOLDataArea;
		 
	 }
	 private ApplicationArea createApplicationArea(){
		 
			ObjectFactory factory2 = new ObjectFactory();
		 
			com.ibm.xmlns.ims._1_3.getfinancialofferingletter_2004_11_30.ApplicationArea appArea = factory2.createApplicationArea();
			appArea.setBODId("" );		
			appArea.setSender( new Sender());
			GregorianCalendar createDt = new GregorianCalendar();
			createDt.setTimeInMillis(this.createDate);
			
			try {
				appArea.setCreationDateTime(DatatypeFactory.newInstance().newXMLGregorianCalendar(createDt));
			} catch (DatatypeConfigurationException e) {
				e.printStackTrace();
			}
			
			Application application = factory2.createApplication();
			ApplicationDetails appDetails = factory2.createApplicationDetails();
			appDetails.setApplicationName(APP_ID);
			application.getInteractingApplication().add(appDetails);
			Transaction transaction  = new Transaction();
			transaction.setCategory("OFFERINGLETTER");
			factory2.createApplicationTransaction(transaction);
			appArea.getApplication().add(application);
		 
		 return appArea;
	 }
	
}
