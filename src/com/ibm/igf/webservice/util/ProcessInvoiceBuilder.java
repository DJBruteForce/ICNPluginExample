package com.ibm.igf.webservice.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;

import org.openapplications.oagis._8_2.processinvoice_2012_12_20.Address;
import org.openapplications.oagis._8_2.processinvoice_2012_12_20.AddressLine;
import org.openapplications.oagis._8_2.processinvoice_2012_12_20.Addresses;
import org.openapplications.oagis._8_2.processinvoice_2012_12_20.Amount;
import org.openapplications.oagis._8_2.processinvoice_2012_12_20.AmountPerQuantity;
import org.openapplications.oagis._8_2.processinvoice_2012_12_20.Contact;
import org.openapplications.oagis._8_2.processinvoice_2012_12_20.Contacts;
import org.openapplications.oagis._8_2.processinvoice_2012_12_20.Country;
import org.openapplications.oagis._8_2.processinvoice_2012_12_20.Description;
import org.openapplications.oagis._8_2.processinvoice_2012_12_20.DocumentIdType;
import org.openapplications.oagis._8_2.processinvoice_2012_12_20.DocumentIds;
import org.openapplications.oagis._8_2.processinvoice_2012_12_20.DocumentReferences;
import org.openapplications.oagis._8_2.processinvoice_2012_12_20.FunctionalAmount;
import org.openapplications.oagis._8_2.processinvoice_2012_12_20.GenericDocumentReference;
import org.openapplications.oagis._8_2.processinvoice_2012_12_20.Id;
import org.openapplications.oagis._8_2.processinvoice_2012_12_20.ItemCategoryId;
import org.openapplications.oagis._8_2.processinvoice_2012_12_20.ItemIdType;
import org.openapplications.oagis._8_2.processinvoice_2012_12_20.ItemIds;
import org.openapplications.oagis._8_2.processinvoice_2012_12_20.Name;
import org.openapplications.oagis._8_2.processinvoice_2012_12_20.NameValuePair;
import org.openapplications.oagis._8_2.processinvoice_2012_12_20.Note;
import org.openapplications.oagis._8_2.processinvoice_2012_12_20.OrderDocumentReference;
import org.openapplications.oagis._8_2.processinvoice_2012_12_20.OrderStatus;
import org.openapplications.oagis._8_2.processinvoice_2012_12_20.OrganizationalUnit;
import org.openapplications.oagis._8_2.processinvoice_2012_12_20.OrganizationalUnitBase;
import org.openapplications.oagis._8_2.processinvoice_2012_12_20.Parties;
import org.openapplications.oagis._8_2.processinvoice_2012_12_20.PartyDocumentId;
import org.openapplications.oagis._8_2.processinvoice_2012_12_20.PartyId;
import org.openapplications.oagis._8_2.processinvoice_2012_12_20.PartyIdType;
import org.openapplications.oagis._8_2.processinvoice_2012_12_20.PartyInstitutional;
import org.openapplications.oagis._8_2.processinvoice_2012_12_20.Quantity;
import org.openapplications.oagis._8_2.processinvoice_2012_12_20.Reason;
import org.openapplications.oagis._8_2.processinvoice_2012_12_20.RelatedUnitType;
import org.openapplications.oagis._8_2.processinvoice_2012_12_20.SplitPay;
import org.openapplications.oagis._8_2.processinvoice_2012_12_20.SplitPayInfo;
import org.openapplications.oagis._8_2.processinvoice_2012_12_20.StateChange;
import org.openapplications.oagis._8_2.processinvoice_2012_12_20.Tax;
import org.openapplications.oagis._8_2.processinvoice_2012_12_20.TaxCode;

import com.ibm.gil.business.InvoiceELS;



import com.ibm.xmlns.ims._1_3.processinvoice_2012_12_20.AssetId;
import com.ibm.xmlns.ims._1_3.processinvoice_2012_12_20.CurrencyConversion;
import com.ibm.xmlns.ims._1_3.processinvoice_2012_12_20.InterfaceData;
import com.ibm.xmlns.ims._1_3.processinvoice_2012_12_20.Invoice;
import com.ibm.xmlns.ims._1_3.processinvoice_2012_12_20.InvoiceHeader;
import com.ibm.xmlns.ims._1_3.processinvoice_2012_12_20.InvoiceLine;
import com.ibm.xmlns.ims._1_3.processinvoice_2012_12_20.LeaseTime;
import com.ibm.xmlns.ims._1_3.processinvoice_2012_12_20.ObjectFactory;
import com.ibm.xmlns.ims._1_3.processinvoice_2012_12_20.OrderItem;

import com.ibm.xmlns.ims._1_3.processinvoice_2012_12_20.TermsPerPeriod;


import financing.tools.gcps.ws.domain.InvSplitPay;
import financing.tools.gcps.ws.domain.InvoiceComment;
import financing.tools.gcps.ws.domain.InvoiceDetail;
//import financing.tools.gcps.ws.service.processinvoice.request.jaxb.ProcessInvoice_jaxb;
//import financing.tools.gcps.xml.jaxb.processinvoice.ims.ImsCurrencyConversionElement;

public class ProcessInvoiceBuilder {
	Invoice invoice=null;
	InvoiceELS invoiceELS=null;
	InvoiceHeader invoiceHeader=null;
	List<InvoiceLine> listInvoiceLines=null;

	public ProcessInvoiceBuilder(){
//			InvoiceELS invoiceELS) {
//		super();
//		this.invoiceELS=invoiceELS;
//		
	}
	
//	public Invoice buildInvoice(){
//		
//		return invoice;
//	}
//	private void buildInvoiceHeader(){
//		
//	}
//	private void buildInvoiceLines(){
//		List lineItems = invoiceELS.getLineItems();
//        LineItemELS lineItem = null;
//        InvoiceLine invoiceLine=null;
//        for (int i = 0; i < lineItems.size(); i++){
//        	lineItem = ((LineItemELS) lineItems.get(i));
//        	invoiceLine=new InvoiceLine();
//        	
//        	
//        }
//        
//
//		
//	}
//	
//	private void createOrderItem(){
//		OrderItem orderItem=new OrderItem();
//		orderItem.setItemIds(value)
//		ItemIds itemIds=new ItemIds();
//		ProcessInvoice_jaxb
//		itemIds.setItemId()
//		ItemIdType itemIdType=new ItemIdType();
//		itemIdType.getId()
//		Id id=new Id();
//		id.setType("Vendor");
//		id.setValue();
//		Id id=new Id();
//		id.
//	}
	
	
	private void createDetailJaxbObjects(
//			financing.tools.gcps.xml.jaxb.processinvoice.ims.Invoice invoice,
			Invoice invoice,
			InvoiceDetail invoiceDetailWs) throws JAXBException,
			DatatypeConfigurationException {
//		financing.tools.gcps.xml.jaxb.processinvoice.ims.ObjectFactory imsFactory = new financing.tools.gcps.xml.jaxb.processinvoice.ims.ObjectFactory();
		ObjectFactory imsFactory= new ObjectFactory();
//		financing.tools.gcps.xml.jaxb.processinvoice.oagis.ObjectFactory oagisFactory = new financing.tools.gcps.xml.jaxb.processinvoice.oagis.ObjectFactory();
		org.openapplications.oagis._8_2.processinvoice_2012_12_20.ObjectFactory oagisFactory= new org.openapplications.oagis._8_2.processinvoice_2012_12_20.ObjectFactory();
		InvoiceLine line = null;
		AmountPerQuantity.Amount unitPriceAmountType, unitDiscountAmountType = null;
		AmountPerQuantity unitPriceAmountPerQuantity, unitDiscountAmountPerQuantity = null;
		Amount unitTaxAmount = null;
		TaxCode taxCode = null;
		Tax unitTaxTax = null;
		Id mfgPartNumId, machineTypeId, machineModelId, purchaseOrderNumId, assetTagId, mesNumId = null;
		ItemIdType itemId = null;
		ItemIds itemIds = null;
		OrderItem orderItem = null;
		Description description = null;
		org.openapplications.oagis._8_2.processinvoice_2012_12_20.Status configUsedStatus, lastUpdateTimestampStatus = null;
		OrderDocumentReference invoiceDocumentReference = null;
		JAXBElement<OrderDocumentReference> invoiceDocumentReferenceElement = null;
		DocumentReferences documentReferences = null;
		PartyDocumentId purchaseOrderNumSupplierDocumentId, mesNumSupplierDocumentId = null;
		JAXBElement<PartyDocumentId> purchaseOrderNumSupplierDocumentIdElement = null;
		JAXBElement<PartyDocumentId> mesNumSupplierDocumentIdElement = null;
		DocumentIds documentIds = null;
		OrderDocumentReference purchaseOrderDocumentReference = null;
		JAXBElement<OrderDocumentReference> purchaseOrderDocumentReferenceElement = null;
		ItemCategoryId itemTypeItemCategoryId, prodCatItemCategoryId, prodClassItemCategoryId = null;
		AssetId assetId = null;
		StateChange lastUpdateTimestampStateChange = null;
		OrderStatus orderStatus = null;
		Quantity quantity, unitPriceQuantity, unitDiscountQuantity = null;
		LeaseTime termPeriodTime = null;
		TermsPerPeriod termTermsPerPeriod = null;
		OrderDocumentReference quoteDocumentReference = null;
		JAXBElement<OrderDocumentReference> quoteDocumentReferenceElement = null;
		// line element.
		line = imsFactory.createInvoiceLine();
		// line number element.
		line.setLineNumber(invoiceDetailWs.getLineNumber()); // line number.
		line.setLineSeq(invoiceDetailWs.getLineSequenceNumber()); // line
																	// sequence
																	// number.
		// amount type element.
		unitPriceAmountType = oagisFactory.createAmountPerQuantityAmount();
		unitPriceAmountType.setBasis("ListPrice");
		unitPriceAmountType.setCurrency(getCurrency(invoice)); // currency is
																// required.
		unitPriceAmountType.setValue(invoiceDetailWs.getUnitPrice()); // unit
																		// price.
		unitDiscountAmountType = oagisFactory.createAmountPerQuantityAmount();
		unitDiscountAmountType.setBasis("DiscountAmount");
		unitDiscountAmountType.setCurrency(getCurrency(invoice)); // currency is
																	// required.
		if (invoiceDetailWs.getUnitDiscount() != null) {
			unitDiscountAmountType.setValue(invoiceDetailWs.getUnitDiscount()); // unit
																				// discount.
		} else {
			unitDiscountAmountType.setValue(new BigDecimal(0)); // unit
																// discount.
		}
		// quantity element.
		unitPriceQuantity = oagisFactory.createQuantity();
		unitPriceQuantity.setValue(new BigDecimal(1));
		unitPriceQuantity.setUom("");
		unitDiscountQuantity = oagisFactory.createQuantity();
		unitDiscountQuantity.setValue(new BigDecimal(1));
		unitDiscountQuantity.setUom("");
		// amount per quantity element.
		unitPriceAmountPerQuantity = oagisFactory.createAmountPerQuantity();
		unitPriceAmountPerQuantity.setPerQuantity(unitPriceQuantity); // per
																		// quantity
																		// is
																		// required.
		unitPriceAmountPerQuantity.setAmount(unitPriceAmountType);
		unitDiscountAmountPerQuantity = oagisFactory.createAmountPerQuantity();
		unitDiscountAmountPerQuantity.setPerQuantity(unitDiscountQuantity); // per
																			// quantity
																			// is
																			// required.
		unitDiscountAmountPerQuantity.setAmount(unitDiscountAmountType);
		line.getUnitPrice().add(unitPriceAmountPerQuantity);
		line.getUnitPrice().add(unitDiscountAmountPerQuantity);
		// tax amount element.
		unitTaxAmount = oagisFactory.createAmount();
		unitTaxAmount.setBasis("unit_tax");
		unitTaxAmount.setCurrency(getCurrency(invoice)); // currency is
															// required.
		unitTaxAmount.setValue(invoiceDetailWs.getUnitTax()); // unit tax.
		// tax code element.
		taxCode = oagisFactory.createTaxCode();
		taxCode.setValue(invoiceDetailWs.getTaxCode()); // tax code.
		// tax element.
		unitTaxTax = oagisFactory.createTax();
		unitTaxTax.setTaxAmount(unitTaxAmount);
		unitTaxTax.setTaxCode(taxCode);
		line.getTax().add(unitTaxTax);
		// id element.
		mfgPartNumId = oagisFactory.createId();
		mfgPartNumId.setIdOrigin("Vendor");
		if (invoiceDetailWs.getManufacturerPartNumber() != null) { // manufacturer
																	// part
																	// number.
			mfgPartNumId.setValue(invoiceDetailWs.getManufacturerPartNumber());
		} else {
			mfgPartNumId.setValue("");
		}
		machineTypeId = oagisFactory.createId();
		machineTypeId.setIdOrigin("IBM/MachineType");
		if (invoiceDetailWs.getMachineType() != null) { // machine type.
			machineTypeId.setValue(invoiceDetailWs.getMachineType());
		} else {
			machineTypeId.setValue("");
		}
		machineModelId = oagisFactory.createId();
		machineModelId.setIdOrigin("IBM/Model");
		if (invoiceDetailWs.getMachineModel() != null) { // machine model.
			machineModelId.setValue(invoiceDetailWs.getMachineModel());
		} else {
			machineModelId.setValue("");
		}
		purchaseOrderNumId = oagisFactory.createId();
		purchaseOrderNumId.setIdOrigin("Supplier");
		if (invoiceDetailWs.getPurchaseOrderNumber() != null) { // purchase
																// order number.
			purchaseOrderNumId.setValue(invoiceDetailWs
					.getPurchaseOrderNumber());
		} else {
			purchaseOrderNumId.setValue("");
		}
		assetTagId = oagisFactory.createId();
		assetTagId.setIdOrigin("Supplier");
		if (invoiceDetailWs.getPartTag() != null) { // part tag.
			assetTagId.setValue(invoiceDetailWs.getPartTag());
		} else {
			assetTagId.setValue("");
		}
		mesNumId = oagisFactory.createId();
		mesNumId.setIdOrigin("IBM/MES");
		if (invoiceDetailWs.getMesNumber() != null) { // mes number.
			mesNumId.setValue(invoiceDetailWs.getMesNumber());
		} else {
			mesNumId.setValue("");
		}
		// item id element.
		itemId = oagisFactory.createItemIdType();
		itemId.getId().add(mfgPartNumId);
		itemId.getId().add(machineTypeId);
		itemId.getId().add(machineModelId);
		// item ids element.
		itemIds = oagisFactory.createItemIds();
		itemIds.setItemId(itemId);
		// description element.
		description = oagisFactory.createDescription();
		description.setValue(invoiceDetailWs.getItemDescription()); // item
																	// description.
		// item category id element.
		itemTypeItemCategoryId = oagisFactory.createItemCategoryId();
		itemTypeItemCategoryId.setType("ItemType");
		if (invoiceDetailWs.getInvoiceLineItemType() != null) { // item type.
			itemTypeItemCategoryId.setValue(invoiceDetailWs
					.getInvoiceLineItemType());
		} else {
			itemTypeItemCategoryId.setValue("");
		}
		prodCatItemCategoryId = oagisFactory.createItemCategoryId();
		prodCatItemCategoryId.setType("ProductCategory");
		if (invoiceDetailWs.getProdCat() != null) { // product category.
			prodCatItemCategoryId.setValue(invoiceDetailWs.getProdCat());
		} else {
			prodCatItemCategoryId.setValue("");
		}
		prodClassItemCategoryId = oagisFactory.createItemCategoryId();
		prodClassItemCategoryId.setType("ProductClass");
		if (invoiceDetailWs.getProdClass() != null) { // product class.
			prodClassItemCategoryId.setValue(invoiceDetailWs.getProdClass());
		} else {
			prodClassItemCategoryId.setValue("");
		}
		// asset tag element.
		assetId = imsFactory.createAssetId();
		assetId.getId().add(assetTagId);
		assetId.setSerialNumber(invoiceDetailWs.getSerialNumber()); // serial
																	// number.
		// order item element.
		orderItem = imsFactory.createOrderItem();
		orderItem.setItemIds(itemIds);
		orderItem.getDescription().add(description);
		orderItem.getItemCategoryId().add(itemTypeItemCategoryId);
		orderItem.getItemCategoryId().add(prodCatItemCategoryId);
		orderItem.getItemCategoryId().add(prodClassItemCategoryId);
		orderItem.getAssetId().add(assetId);
		if (invoiceDetailWs.getMesIndc() != null
				&& invoiceDetailWs.getMesIndc().booleanValue()) { // mes
																	// indicator.
			orderItem.setUpgradeOrMES("MES");
		} else {
			orderItem.setUpgradeOrMES("Upgrade");
		}
		// equipment source element
		// R9 - Story 315223 (Source code requirement - GCMS)
		if (invoiceDetailWs.getEquipmentSource() != null) {
			orderItem.setEquipmentSource(invoiceDetailWs.getEquipmentSource());
		} else {
			orderItem.setEquipmentSource("");
		}
		// R9 - Story 315223 (Source code requirement - GCMS)

		line.setOrderItem(orderItem);
		// state change element.
		lastUpdateTimestampStateChange = oagisFactory.createStateChange();
		GregorianCalendar calendar = new GregorianCalendar();
		if (invoiceDetailWs.getLastUpdateTimestamp() != null) {
			calendar.clear();
			calendar.setTime(invoiceDetailWs.getLastUpdateTimestamp()); // last
																		// update
																		// timestamp.
		}
		XMLGregorianCalendar changeDate = null;
		changeDate = javax.xml.datatype.DatatypeFactory.newInstance().newXMLGregorianCalendar(
				calendar);
		lastUpdateTimestampStateChange.setChangeDate(changeDate);
		// status element.
		configUsedStatus = oagisFactory.createStatus();
		if (invoiceDetailWs.isInConfiguration() != null
				&& invoiceDetailWs.isInConfiguration().booleanValue()) { // configuration
																			// used
																			// indicator.
			configUsedStatus.setCode("true");
		} else {
			configUsedStatus.setCode("false");
		}
		lastUpdateTimestampStatus = oagisFactory.createStatus();
		lastUpdateTimestampStatus.getChange().add(
				lastUpdateTimestampStateChange);
		// invoice document reference element.
		invoiceDocumentReference = oagisFactory.createOrderDocumentReference();
		invoiceDocumentReference.setStatus(configUsedStatus);
		invoiceDocumentReferenceElement = oagisFactory
				.createInvoiceDocumentReference(invoiceDocumentReference);
		// supplier document id element
		purchaseOrderNumSupplierDocumentId = oagisFactory
				.createPartyDocumentId();
		purchaseOrderNumSupplierDocumentId.setId(purchaseOrderNumId);
		purchaseOrderNumSupplierDocumentIdElement = oagisFactory
				.createSupplierDocumentId(purchaseOrderNumSupplierDocumentId);
		mesNumSupplierDocumentId = oagisFactory.createPartyDocumentId();
		mesNumSupplierDocumentId.setId(mesNumId);
		mesNumSupplierDocumentIdElement = oagisFactory
				.createSupplierDocumentId(mesNumSupplierDocumentId);
		// document ids element
		documentIds = oagisFactory.createDocumentIds();
		documentIds.getDocumentIdType().add(
				purchaseOrderNumSupplierDocumentIdElement);
		documentIds.getDocumentIdType().add(mesNumSupplierDocumentIdElement);
		// purchase order document reference element.
		purchaseOrderDocumentReference = oagisFactory
				.createOrderDocumentReference();
		purchaseOrderDocumentReference.setDocumentIds(documentIds);
		purchaseOrderDocumentReferenceElement = oagisFactory
				.createPurchaseOrderDocumentReference(purchaseOrderDocumentReference);
		// quote document reference element.
		quoteDocumentReference = oagisFactory.createOrderDocumentReference();
		if (invoiceDetailWs.getQuoteItemNum() != null) { // quote line item
															// number.
			quoteDocumentReference.setLineNumber(new BigInteger(invoiceDetailWs
					.getQuoteItemNum()));
			quoteDocumentReferenceElement = oagisFactory
					.createQuoteDocumentReference(quoteDocumentReference);
		}
		// document references element.
		documentReferences = oagisFactory.createDocumentReferences();
		documentReferences.getDocumentReference().add(
				invoiceDocumentReferenceElement);
		documentReferences.getDocumentReference().add(
				purchaseOrderDocumentReferenceElement);
		documentReferences.getDocumentReference().add(
				quoteDocumentReferenceElement);
		line.setDocumentReferences(documentReferences);
		// order status element.
		orderStatus = oagisFactory.createOrderStatus();
		orderStatus.getAcknowledgementDetail().add(lastUpdateTimestampStatus);
		line.setOrderStatus(orderStatus);
		// item quantity element.
		quantity = oagisFactory.createQuantity();
		quantity.setValue(new BigDecimal(invoiceDetailWs.getQuantity()
				.intValue())); // quantity.
		quantity.setUom("");
		line.setItemQuantity(quantity);
		line.setFinanceDesired(invoiceDetailWs.getFinancingType()); // financing
																	// type.
		// period time element.
		termPeriodTime = imsFactory.createLeaseTime();
		termPeriodTime.setDuration(invoiceDetailWs.getTerm()); // term.
		// terms per period element.
		termTermsPerPeriod = imsFactory.createTermsPerPeriod();
		termTermsPerPeriod.setPeriodTime(termPeriodTime);
		line.setLeaseTerms(termTermsPerPeriod);
		
		// Change to ROF Facsimile multiple sold to's (US) - Story 1064780
		
		PartyId customerPartyId = oagisFactory.createPartyId();
		customerPartyId.setValue(invoiceDetailWs.getCustomerNum());
		PartyIdType customerPartyIdType = oagisFactory.createPartyIdType();
		customerPartyIdType.getId().add(customerPartyId);
		line.setPartyId(customerPartyIdType);

		invoice.getLine().add(line);
	}


/**
 * getCurrency method.
 * 
 * @param invoice_jaxb
 *            ImsInvoiceElement
 * 
 * @return String
 */
private String getCurrency(
		Invoice invoice_jaxb) {
	String currency = null;

	Iterator<Amount> totalAmtItr = invoice_jaxb.getHeader()
			.getTotalAmount().iterator();
	while (totalAmtItr.hasNext()) {
		Object obj = totalAmtItr.next();
		if (obj instanceof Amount) {
			Amount amt = (Amount) obj;
			if (amt.getBasis().equalsIgnoreCase("TotalAmount")) {
				if (amt.getCurrency() != null) {
					currency = amt.getCurrency().trim();
				}
			}
		}
	}

	return currency;
}


/**
 * Create JAXB objects with invoice comment information.
 * 
 * @param invoice
 *            ImsInvoiceElement
 * @param invoiceCommentWs
 *            InvoiceComment
 * 
 * @throws JAXBException
 * @throws DatatypeConfigurationException
 */
private void createCommentJaxbObjects(
		Invoice invoice,
		InvoiceComment invoiceCommentWs) throws JAXBException,
		DatatypeConfigurationException {
	org.openapplications.oagis._8_2.processinvoice_2012_12_20.ObjectFactory oagisFactory = new org.openapplications.oagis._8_2.processinvoice_2012_12_20.ObjectFactory();

	Note commentNote = null;

	// note element.
	commentNote = oagisFactory.createNote();
	commentNote.setId(invoiceCommentWs.getCommentSequenceNumber()); // comment
																	// sequence
																	// number.
	commentNote.setType(invoiceCommentWs.getCommentType()); // comment type.
	commentNote.setAuthor(invoiceCommentWs.getCreateId()); // create id.
	GregorianCalendar calendar = new GregorianCalendar();
	if (invoiceCommentWs.getCreateTimestamp() != null) {
		calendar.clear();
		calendar.setTime(invoiceCommentWs.getCreateTimestamp());
	}
	XMLGregorianCalendar entryDateTime = null;
	entryDateTime = javax.xml.datatype.DatatypeFactory.newInstance().newXMLGregorianCalendar(
			calendar);
	commentNote.setEntryDateTime(entryDateTime); // create timestamp.
	commentNote.setValue(invoiceCommentWs.getCommentDescription()); // comment.
	invoice.getHeader().getNote().add(commentNote);
}

/**
 * Create JAXB objects with invoice information.
 * 
 * @param invoiceWs
 *            Invoice
 * 
 * @throws JAXBException
 * @throws DatatypeConfigurationException
 */
public Invoice createInvoiceJaxbObjects(financing.tools.gcps.ws.domain.Invoice invoiceWs)
		throws JAXBException, DatatypeConfigurationException {
	ObjectFactory imsFactory = new ObjectFactory();
	org.openapplications.oagis._8_2.processinvoice_2012_12_20.ObjectFactory oagisFactory = new org.openapplications.oagis._8_2.processinvoice_2012_12_20.ObjectFactory();
	GregorianCalendar calendar1 = new GregorianCalendar();
	GregorianCalendar calendar2 = new GregorianCalendar();
	XMLGregorianCalendar documentDateTime = null;
	XMLGregorianCalendar lastModificationDateTime = null;
	Iterator<?> detailItr, commentsItr = null;
	Invoice invoice = null;
	InvoiceHeader invoiceHeader = null;
	Country supplierPrimaryCountry, supplierSecondaryCountry = null;
	Address primaryAddress = null;
	JAXBElement<Address> primaryAddressElement = null;
	Address secondaryAddress = null;
	JAXBElement<Address> secondaryAddressElement = null;
	Addresses addresses = null;
	PartyInstitutional supplierParty = null;
	JAXBElement<PartyInstitutional> supplierPartyElement = null;
	PartyInstitutional employeeParty = null;
	JAXBElement<PartyInstitutional> employeePartyElement = null;
	PartyInstitutional shipToParty = null;
	JAXBElement<PartyInstitutional> shipToPartyElement = null;
	PartyInstitutional remitToParty = null;
	JAXBElement<PartyInstitutional> remitToPartyElement = null;
	PartyInstitutional billToParty = null;
	JAXBElement<PartyInstitutional> billToPartyElement = null;
	PartyInstitutional soldToParty = null;
	JAXBElement<PartyInstitutional> soldToPartyElement = null;
	PartyInstitutional soldToSecondaryParty = null;
	JAXBElement<PartyInstitutional> soldToSecondaryPartyElement = null;
	PartyInstitutional installToParty = null;
	JAXBElement<PartyInstitutional> installToPartyElement = null;
	Parties parties = null;
	Id invoiceNumId, invoiceSeqNumId, cmObjectId, refInvoiceNumId, offerLetterNumId, legalId, saNumId = null;
	PartyDocumentId supplierDocumentId = null;
	JAXBElement<PartyDocumentId> supplierDocumentIdElement = null;
	DocumentIdType invoiceSeqNumberDocumentId, cmObjectDocumentId, refInvoiceNumDocumentId, offerLetterNumDocumentId, saNumDocumentId = null;
	JAXBElement<DocumentIdType> invoiceSeqNumberDocumentIdElement = null;
	JAXBElement<DocumentIdType> cmObjectDocumentIdElement = null;
	JAXBElement<DocumentIdType> refInvoiceNumDocumentIdElement = null;
	JAXBElement<DocumentIdType> offerLetterNumDocumentIdElement = null;
	JAXBElement<DocumentIdType> saNumDocumentIdElement = null;
	DocumentIds invoiceNumDocumentIds, invoiceDocumentIds, offerLetterDocumentIds, saNumDocumentIds = null;
	PartyId tpidCodePartyId, poexPartyId, vendorNumPartyId, companyCodeId, ocrKidRefPartyId = null;
	PartyIdType supplierPartyIdType, shipToPartyIdType, ocrKidRefPartyIdType = null;
	OrderDocumentReference invoiceDocumentReference = null;
	JAXBElement<OrderDocumentReference> invoiceDocumentReferenceElement = null;
	GenericDocumentReference offerletterDocumentReference = null;
	JAXBElement<GenericDocumentReference> offerletterDocumentReferenceElement = null;
	OrderDocumentReference contractDocumentReference = null;
	JAXBElement<OrderDocumentReference> contractDocumentReferenceElement = null;
	DocumentReferences documentReferences = null;
	Contact.EMailAddress respIdEmailAddress, createIdEmailAddress, lastUpdateIdEmailAddress = null;
	Contact respIdContactAbs, createIdContactAbs, lastUpdateIdContactAbs = null;
	SplitPay splitPay = null;
	Contacts employeeContacts = null;
	SplitPayInfo splitPayInfo = null;
	Amount totalInvoiceAmtAmount, totalTaxAmtAmount, availableUnitAmtAmount, availableTaxAmtAmount, currencyConvAmount = null;
	FunctionalAmount exchangeRateFunctionalAmount = null;
//	ImsCurrencyConversionElement exchangeRateCurrencyConversion = null;
	CurrencyConversion exchangeRateCurrencyConversion=null;
	Name vendorNameName = null;
	Description coaInvoiceIndcDescription, invoiceStatusDescription, exceptionIndcDescription = null;
	org.openapplications.oagis._8_2.processinvoice_2012_12_20.Status coaInvoiceIndcStatus, exceptionIndcStatus = null;
	OrderStatus orderStatus = null;
	Reason varianceReason = null;
	OrganizationalUnitBase costCenterOrgUnitBase = null;
	RelatedUnitType relatedUnitType = null;
	OrganizationalUnit businessElement = null;
	/** R4.0 */
	InterfaceData interfaceData = null;
	NameValuePair sourceIndc = null;
	NameValuePair cpsNumber = null; // Added CPS Number for Story #602545
	NameValuePair autoCreateCoaIndc = null;
	 //Story 1750051 CA GIL changes
	NameValuePair provinceCode = null;
	 //End Story 1750051 CA GIL changes
	NameValuePair distVendorNum=null;

	// invoice header element.
	invoice = imsFactory.createInvoice();
	invoiceHeader = imsFactory.createInvoiceHeader();
	// invoice data time element.
	/*
	 * invoice date. the date on the server needs an offset of +0 (GMT). the
	 * gil client needs to construct the date with the timezone of GMT but
	 * without having the date/time adjusted by the local timezone.
	 */
	calendar2.setTime(invoiceWs.getInvoiceDate());
	calendar1.clear();
	calendar1.setTimeZone(TimeZone.getTimeZone("GMT"));
	calendar1.set(calendar2.get(Calendar.YEAR),
			calendar2.get(Calendar.MONTH), calendar2.get(Calendar.DATE), 0,
			0, 1);

	documentDateTime = javax.xml.datatype.DatatypeFactory.newInstance()
			.newXMLGregorianCalendar(calendar1);
	invoiceHeader.setDocumentDateTime(documentDateTime); // invoice date

	// last modification date element.
	if (invoiceWs.getLastUpdateTimestamp() != null) {
		calendar1.clear();
		calendar1.setTime(invoiceWs.getLastUpdateTimestamp()); // last
																// update
																// timestamp.
	}
	// if null, current date/time is used.
	lastModificationDateTime = javax.xml.datatype.DatatypeFactory.newInstance()
			.newXMLGregorianCalendar(calendar1);
	invoiceHeader.setLastModificationDateTime(lastModificationDateTime);
	// type element.
	invoiceHeader.setType(invoiceWs.getInvoiceType()); // invoice type.
	// priority element.
	if (invoiceWs.getUrgentInvoiceIndc() != null
			&& invoiceWs.getUrgentInvoiceIndc().booleanValue()) { // urgent
																	// indicator.
		invoiceHeader.setPriority("Priority");
	} else {
		invoiceHeader.setPriority("Standard");
	}

	// id element.
	invoiceNumId = oagisFactory.createId();
	invoiceNumId.setIdOrigin("IBM/Invoice");
	if (invoiceWs.getInvoiceNumber() != null) { // invoice number.
		invoiceNumId.setValue(invoiceWs.getInvoiceNumber());
	} else {
		invoiceNumId.setValue("");
	}
	invoiceNumId.setType(invoiceWs.getInvoiceSource()); // invoice source.
	invoiceSeqNumId = oagisFactory.createId();
	invoiceSeqNumId.setIdOrigin("GCPS");
	if (invoiceWs.getInvoiceSequenceNumber() != null) { // invoice sequence
														// number.
		invoiceSeqNumId.setValue(invoiceWs.getInvoiceSequenceNumber());
	} else {
		invoiceSeqNumId.setValue("");
	}
	cmObjectId = oagisFactory.createId();
	cmObjectId.setIdOrigin("ContentManager");
	if (invoiceWs.getCmObjectId() != null) { // cm object id.
		cmObjectId.setValue(invoiceWs.getCmObjectId());
	} else {
		cmObjectId.setValue("");
	}
	refInvoiceNumId = oagisFactory.createId();
	refInvoiceNumId.setIdOrigin("GIL");
	if (invoiceWs.getReferenceInvoiceNumber() != null) { // reference
															// invoice
															// number.
		refInvoiceNumId.setValue(invoiceWs.getReferenceInvoiceNumber());
	} else {
		refInvoiceNumId.setValue("");
	}
	offerLetterNumId = oagisFactory.createId();
	if (invoiceWs.getOfferLetterNumber() != null) { // offering letter
													// number.
		offerLetterNumId.setValue(invoiceWs.getOfferLetterNumber());
	} else {
		offerLetterNumId.setValue("");
	}
	legalId = oagisFactory.createId();
	legalId.setIdOrigin("VAT");
	if (invoiceWs.getLegalId() != null) { // legal id number.
		legalId.setValue(invoiceWs.getLegalId());
	} else {
		legalId.setValue("");
	}
	saNumId = oagisFactory.createId();
	saNumId.setIdOrigin("IBM");
	if (invoiceWs.getSaNumber() != null) { // sa number.
		saNumId.setValue(invoiceWs.getSaNumber());
	} else {
		saNumId.setValue("");
	}
	// supplier document id element.
	supplierDocumentId = oagisFactory.createPartyDocumentId();
	supplierDocumentId.setId(invoiceNumId);
	supplierDocumentIdElement = oagisFactory
			.createSupplierDocumentId(supplierDocumentId);
	// document id element.
	invoiceSeqNumberDocumentId = oagisFactory.createDocumentIdType();
	invoiceSeqNumberDocumentId.setId(invoiceSeqNumId);
	invoiceSeqNumberDocumentIdElement = oagisFactory
			.createDocumentIdType(invoiceSeqNumberDocumentId);
	cmObjectDocumentId = oagisFactory.createDocumentIdType();
	cmObjectDocumentId.setId(cmObjectId);
	cmObjectDocumentIdElement = oagisFactory
			.createDocumentIdType(cmObjectDocumentId);
	refInvoiceNumDocumentId = oagisFactory.createDocumentIdType();
	refInvoiceNumDocumentId.setId(refInvoiceNumId);
	refInvoiceNumDocumentIdElement = oagisFactory
			.createDocumentIdType(refInvoiceNumDocumentId);
	offerLetterNumDocumentId = oagisFactory.createDocumentIdType();
	offerLetterNumDocumentId.setId(offerLetterNumId);
	offerLetterNumDocumentIdElement = oagisFactory
			.createDocumentIdType(offerLetterNumDocumentId);
	saNumDocumentId = oagisFactory.createDocumentIdType();
	saNumDocumentId.setId(saNumId);
	saNumDocumentIdElement = oagisFactory
			.createDocumentIdType(saNumDocumentId);
	// document ids element.
	invoiceNumDocumentIds = oagisFactory.createDocumentIds();
	invoiceNumDocumentIds.getDocumentIdType()
			.add(supplierDocumentIdElement);
	invoiceHeader.setDocumentIds(invoiceNumDocumentIds);
	invoiceDocumentIds = oagisFactory.createDocumentIds();
	invoiceDocumentIds.getDocumentIdType().add(
			invoiceSeqNumberDocumentIdElement);
	invoiceDocumentIds.getDocumentIdType().add(cmObjectDocumentIdElement);
	invoiceDocumentIds.getDocumentIdType().add(
			refInvoiceNumDocumentIdElement);
	offerLetterDocumentIds = oagisFactory.createDocumentIds();
	offerLetterDocumentIds.getDocumentIdType().add(
			offerLetterNumDocumentIdElement);
	saNumDocumentIds = oagisFactory.createDocumentIds();
	saNumDocumentIds.getDocumentIdType().add(saNumDocumentIdElement);
	// invoice document reference element.
	invoiceDocumentReference = oagisFactory.createOrderDocumentReference();
	invoiceDocumentReference.setDocumentIds(invoiceDocumentIds);
	/*
	 * invoice receipt date. the date on the server needs an offset of +0
	 * (GMT). the gil client needs to construct the date with the timezone
	 * of GMT but without having the date/time adjusted by the local
	 * timezone.
	 */
	if (invoiceWs.getInvoiceReceiptDate() != null) {
		calendar2.setTime(invoiceWs.getInvoiceReceiptDate());
		calendar1.clear();
		calendar1.setTimeZone(TimeZone.getTimeZone("GMT"));
		calendar1.set(calendar2.get(Calendar.YEAR),
				calendar2.get(Calendar.MONTH),
				calendar2.get(Calendar.DATE), 0, 0, 1);
	} // if null, current date/time is used.
	XMLGregorianCalendar documentDate = null;
	documentDate = javax.xml.datatype.DatatypeFactory.newInstance().newXMLGregorianCalendar(
			calendar1);
	invoiceDocumentReference.setDocumentDate(documentDate);
	invoiceDocumentReferenceElement = oagisFactory
			.createInvoiceDocumentReference(invoiceDocumentReference);
	// offer letter document reference element.
	offerletterDocumentReference = oagisFactory
			.createGenericDocumentReference();
	offerletterDocumentReference.setDocumentIds(offerLetterDocumentIds);
	offerletterDocumentReferenceElement = oagisFactory
			.createFinancialOfferingLetterDocumentReference(offerletterDocumentReference);
	// contract document reference element.
	contractDocumentReference = oagisFactory.createOrderDocumentReference();
	contractDocumentReference.setDocumentIds(saNumDocumentIds);
	contractDocumentReferenceElement = oagisFactory
			.createContractDocumentReference(contractDocumentReference);
	// document references element.
	documentReferences = oagisFactory.createDocumentReferences();
	documentReferences.getDocumentReference().add(
			invoiceDocumentReferenceElement);
	documentReferences.getDocumentReference().add(
			offerletterDocumentReferenceElement);
	documentReferences.getDocumentReference().add(
			contractDocumentReferenceElement);
	invoiceHeader.setDocumentReferences(documentReferences);
	// country element.
	supplierPrimaryCountry = oagisFactory.createCountry();
	supplierPrimaryCountry.setValue(invoiceWs.getCountryCode()); // country
																	// code.
	supplierSecondaryCountry = oagisFactory.createCountry();
	if (invoiceWs.getTpidCountry() != null) { // tpid country code.
		supplierSecondaryCountry.setValue(invoiceWs.getTpidCountry());
	} else {
		supplierSecondaryCountry.setValue("");
	}
	// primary address element.
	primaryAddress = oagisFactory.createAddress();
	primaryAddress.setCountry(supplierPrimaryCountry);
	primaryAddressElement = oagisFactory
			.createPrimaryAddress(primaryAddress);
	// secondary address element.
	secondaryAddress = oagisFactory.createAddress();
	secondaryAddress.setCountry(supplierSecondaryCountry);
	secondaryAddressElement = oagisFactory
			.createSecondaryAddress(secondaryAddress);
	// addresses element.
	addresses = oagisFactory.createAddresses();
	addresses.getAddress().add(primaryAddressElement);
	addresses.getAddress().add(secondaryAddressElement);
	// party id element.
	tpidCodePartyId = oagisFactory.createPartyId();
	tpidCodePartyId.setIdOrigin("Supplier");
	if (invoiceWs.getTpidCode() != null) { // tpid code.
		tpidCodePartyId.setValue(invoiceWs.getTpidCode());
	} else {
		tpidCodePartyId.setValue("");
	}
	poexPartyId = oagisFactory.createPartyId();
	poexPartyId.setIdOrigin("POEX");
	if (invoiceWs.getPoexCode() != null) { // poex code.
		poexPartyId.setValue(invoiceWs.getPoexCode());
	} else {
		poexPartyId.setValue("");
	}

	vendorNumPartyId = oagisFactory.createPartyId();
	vendorNumPartyId.setIdOrigin("SAP");
	if (invoiceWs.getVendorNumber() != null) { // vendor number.
		vendorNumPartyId.setValue(invoiceWs.getVendorNumber());
	} else {
		vendorNumPartyId.setValue("");
	}
	companyCodeId = oagisFactory.createPartyId();
	companyCodeId.setIdOrigin("IBM");
	if (invoiceWs.getCompanyCode() != null) { // company code.
		companyCodeId.setValue(invoiceWs.getCompanyCode());
	} else {
		companyCodeId.setValue("");
	}
	ocrKidRefPartyId = oagisFactory.createPartyId();
	ocrKidRefPartyId.setIdOrigin("GIL");
	if (invoiceWs.getOcrKidReference() != null) { // ocr kid reference.
		ocrKidRefPartyId.setValue(invoiceWs.getOcrKidReference());
	} else {
		ocrKidRefPartyId.setValue("");
	}
	// party id type element.
	supplierPartyIdType = oagisFactory.createPartyIdType();
	supplierPartyIdType.getId().add(tpidCodePartyId);
	supplierPartyIdType.getId().add(poexPartyId);
	// supplierPartyIdType.getId().add(cpsPartyId);
	supplierPartyIdType.getId().add(vendorNumPartyId);
	shipToPartyIdType = oagisFactory.createPartyIdType();
	shipToPartyIdType.getId().add(companyCodeId);
	ocrKidRefPartyIdType = oagisFactory.createPartyIdType();
	ocrKidRefPartyIdType.getId().add(ocrKidRefPartyId);
	// name element.
	vendorNameName = oagisFactory.createName();
	vendorNameName.setValue(invoiceWs.getVendorName()); // vendor name.
	// supplier party element.
	supplierParty = oagisFactory.createPartyInstitutional();
	supplierParty.setAddresses(addresses);
	supplierParty.setPartyId(supplierPartyIdType);
	supplierParty.getTaxId().add(legalId);
	supplierParty.getName().add(vendorNameName);
	supplierPartyElement = oagisFactory.createSupplierParty(supplierParty);
	// email address element.
	respIdEmailAddress = oagisFactory.createContactEMailAddress();
	respIdEmailAddress.setSequence(new BigInteger("1"));
	if (invoiceWs.getResponsibleId() != null) { // responsible id.
		respIdEmailAddress.setValue(invoiceWs.getResponsibleId());
	} else {
		respIdEmailAddress.setValue("");
	}
	createIdEmailAddress = oagisFactory.createContactEMailAddress();
	createIdEmailAddress.setSequence(new BigInteger("1"));
	if (invoiceWs.getCreateId() != null) { // create id.
		createIdEmailAddress.setValue(invoiceWs.getCreateId());
	} else {
		createIdEmailAddress.setValue("");
	}
	lastUpdateIdEmailAddress = oagisFactory.createContactEMailAddress();
	lastUpdateIdEmailAddress.setSequence(new BigInteger("1"));
	if (invoiceWs.getLastUpdateId() != null) { // last update id.
		lastUpdateIdEmailAddress.setValue(invoiceWs.getLastUpdateId());
	} else {
		lastUpdateIdEmailAddress.setValue("");
	}
	// contact abstract element.
	respIdContactAbs = oagisFactory.createContact();
	respIdContactAbs.setJobTitle("BSP");
	respIdContactAbs.getEMailAddress().add(respIdEmailAddress);
	createIdContactAbs = oagisFactory.createContact();
	createIdContactAbs.setJobTitle("Indexer-Creator");
	createIdContactAbs.getEMailAddress().add(createIdEmailAddress);
	lastUpdateIdContactAbs = oagisFactory.createContact();
	lastUpdateIdContactAbs.setJobTitle("Indexer-Modified");
	lastUpdateIdContactAbs.getEMailAddress().add(lastUpdateIdEmailAddress);
	// contacts element.
	employeeContacts = oagisFactory.createContacts();
	employeeContacts.getContactAbs().add(respIdContactAbs);
	employeeContacts.getContactAbs().add(createIdContactAbs);
	employeeContacts.getContactAbs().add(lastUpdateIdContactAbs);
	// employee party element.
	employeeParty = oagisFactory.createPartyInstitutional();
	employeeParty.setContacts(employeeContacts);

	// Supplier Information for story 812685

	Amount supplierPaymentAmt, supplierTaxAmt = null;
	TaxCode taxCode = null;
	ArrayList<InvSplitPay> splitPayList = (ArrayList<InvSplitPay>) invoiceWs
			.getSplitPay();
	splitPayInfo = oagisFactory.createSplitPayInfo();
	if (splitPayList != null) {
		for (InvSplitPay splitPayment : splitPayList) {
			splitPay = oagisFactory.createSplitPay();
			supplierPaymentAmt = oagisFactory.createAmount();
			supplierPaymentAmt.setValue(splitPayment
					.getSupplierPaymentAmt());
			supplierTaxAmt = oagisFactory.createAmount();
			supplierTaxAmt.setValue(splitPayment.getSupplierTaxAmt());
			taxCode = oagisFactory.createTaxCode();
			taxCode.setValue(splitPayment.getTaxCode());
			splitPay.setSupplierNum(splitPayment.getSupplierNum());
			splitPay.setSupplierPaymentAmt(supplierPaymentAmt);
			splitPay.setSupplierTaxAmt(supplierTaxAmt);
			splitPay.setTaxCode(taxCode);
			splitPay.setCommFinancingIndc(splitPayment
					.getCommFinancingIndc());
			splitPayInfo.getSplitPay().add(splitPay);
		}
	}

	employeeParty.setSplitPayInfo(splitPayInfo);

	employeePartyElement = oagisFactory.createEmployeeParty(employeeParty);

	// organizational unit element.
	costCenterOrgUnitBase = oagisFactory.createOrganizationalUnitBase();
	if (invoiceWs.getDetails() != null
			&& invoiceWs.getDetails().size() != 0) {
		costCenterOrgUnitBase.setId(((InvoiceDetail) invoiceWs.getDetails()
				.get(0)).getCostCenter()); // cost center.
	}
	// related unit type.
	relatedUnitType = oagisFactory.createRelatedUnitType();
	relatedUnitType.setRelationship("CostCenter");
	relatedUnitType.setUnit(costCenterOrgUnitBase);
	// business element..
	businessElement = oagisFactory.createOrganizationalUnit();
	businessElement.getRelatedUnit().add(relatedUnitType);
	// ship to party element
	shipToParty = oagisFactory.createPartyInstitutional();
	shipToParty.setPartyId(shipToPartyIdType);
	shipToParty.setBusiness(businessElement);
	shipToPartyElement = oagisFactory.createShipToParty(shipToParty);
	// remit to party element
	remitToParty = oagisFactory.createPartyInstitutional();
	remitToParty.setPartyId(ocrKidRefPartyIdType);
	remitToPartyElement = oagisFactory.createRemitToParty(remitToParty);
	// parties element.
	parties = oagisFactory.createParties();
	parties.getPartyType().add(supplierPartyElement);
	parties.getPartyType().add(employeePartyElement);
	parties.getPartyType().add(shipToPartyElement);
	parties.getPartyType().add(remitToPartyElement);
	invoiceHeader.setParties(parties);
	// total amount element.
	totalInvoiceAmtAmount = oagisFactory.createAmount();
	totalInvoiceAmtAmount.setBasis("TotalAmount");
	totalInvoiceAmtAmount.setValue(invoiceWs.getTotalInvoiceAmount()); // invoice
																		// total
																		// amount.
	totalInvoiceAmtAmount.setCurrency(invoiceWs.getCurrency()); // currency.
	totalInvoiceAmtAmount.setType(invoiceWs.getDebitCreditIndicator()); // debit
																		// credit
																		// indicator.
	totalTaxAmtAmount = oagisFactory.createAmount();
	totalTaxAmtAmount.setBasis("TotalTax");
	totalTaxAmtAmount.setCurrency(invoiceWs.getCurrency()); // currency is
															// required.
	totalTaxAmtAmount.setValue(invoiceWs.getTotalTaxAmount()); // invoice
																// total tax
																// amount.
	availableUnitAmtAmount = oagisFactory.createAmount();
	availableUnitAmtAmount.setBasis("AvailableUnitAmount");
	availableUnitAmtAmount.setCurrency(invoiceWs.getCurrency()); // currency
																	// is
																	// required.
	availableUnitAmtAmount.setValue(invoiceWs.getAvailableUnitAmount()); // available
																			// unit
																			// amount.
	availableTaxAmtAmount = oagisFactory.createAmount();
	availableTaxAmtAmount.setBasis("AvailableTax");
	availableTaxAmtAmount.setCurrency(invoiceWs.getCurrency()); // currency
																// is
																// required.
	availableTaxAmtAmount.setValue(invoiceWs.getAvailableTaxAmount()); // available
																		// tax
																		// amount.
	currencyConvAmount = oagisFactory.createAmount();
	currencyConvAmount.setCurrency(invoiceWs.getCurrency()); // currency is
																// required.
	currencyConvAmount.setValue(new BigDecimal(0));
	invoiceHeader.getTotalAmount().add(totalInvoiceAmtAmount);
	invoiceHeader.getTotalAmount().add(availableUnitAmtAmount);
	invoiceHeader.getTotalTax().add(totalTaxAmtAmount);
	invoiceHeader.getTotalTax().add(availableTaxAmtAmount);
	// functional amount element.
	exchangeRateFunctionalAmount = oagisFactory.createFunctionalAmount();
	exchangeRateFunctionalAmount.setCurrency(invoiceWs.getCurrency()); // currency
																		// is
																		// required.
	exchangeRateFunctionalAmount.setConversionFactor(invoiceWs
			.getExchangeRate()); // exchange rate.
	// currency conversion element.
	exchangeRateCurrencyConversion = imsFactory
			.createCurrencyConversion();
	exchangeRateCurrencyConversion.setActual(currencyConvAmount); // actual
																	// is
																	// required.
	exchangeRateCurrencyConversion.getConverted().add(
			exchangeRateFunctionalAmount);
	invoiceHeader.getCurrencyConversion().add(
			exchangeRateCurrencyConversion);
	// description element.
	coaInvoiceIndcDescription = oagisFactory.createDescription();
	coaInvoiceIndcDescription
			.setValue("invoice_header-coa_with_invoice_indc");
	invoiceStatusDescription = oagisFactory.createDescription();
	invoiceStatusDescription.setValue("Invoice_header-Invoice_status");
	exceptionIndcDescription = oagisFactory.createDescription();
	exceptionIndcDescription.setValue("invoice_header-exception_ind");
	// status element.
	coaInvoiceIndcStatus = oagisFactory.createStatus();
	coaInvoiceIndcStatus.getDescription().add(coaInvoiceIndcDescription);
	if (invoiceWs.getCoaInvoiceIndicator() != null
			&& invoiceWs.getCoaInvoiceIndicator().booleanValue()) { // coa
																	// invoice
																	// indicator.
		coaInvoiceIndcStatus.setCode("Y");
	} else {
		coaInvoiceIndcStatus.setCode("N");
	}
	exceptionIndcStatus = oagisFactory.createStatus();
	exceptionIndcStatus.getDescription().add(exceptionIndcDescription);
	if (invoiceWs.hasException() != null
			&& invoiceWs.hasException().booleanValue()) { // exception
															// indicator.
		exceptionIndcStatus.setCode("Y");
	} else {
		exceptionIndcStatus.setCode("N");
	}
	// order status element.
	orderStatus = oagisFactory.createOrderStatus();
	orderStatus.getAcknowledgementDetail().add(coaInvoiceIndcStatus);
	orderStatus.getAcknowledgementDetail().add(exceptionIndcStatus);
	orderStatus.getDescription().add(invoiceStatusDescription);
	orderStatus.setCode(invoiceWs.getInvoiceStatus()); // invoice status.
	invoiceHeader.setOrderStatus(orderStatus);
	// reason element.
	varianceReason = oagisFactory.createReason();
	varianceReason.setCode("Variance_Reason");
	if (invoiceWs.getVarianceReason() != null) { // variance reason.
		varianceReason.setValue(invoiceWs.getVarianceReason());
	} else {
		varianceReason.setValue("");
	}
	invoiceHeader.getReason().add(varianceReason);

	/** R4.0 */
	// Source Indicator
	sourceIndc = oagisFactory.createNameValuePair();
	sourceIndc.setName("SourceIndc"); // Name is required
	sourceIndc.setValue(invoiceWs.getSourceIndc());
	// Auto Create COA Indicator

	/* Added CPS Number for Story #602545 */
	cpsNumber = oagisFactory.createNameValuePair();
	cpsNumber.setName("CPS NUMBER"); // Name is required
	cpsNumber.setValue(invoiceWs.getCpsNumber());
	
	autoCreateCoaIndc = oagisFactory.createNameValuePair();
	autoCreateCoaIndc.setName("AutoCreateCoaIndc"); // Name is required
	autoCreateCoaIndc.setValue(invoiceWs.getAutoCreateCoaIndc());
	
	//Story 1750051 CA GIL changes
	provinceCode = oagisFactory.createNameValuePair();
	provinceCode.setName("ProvinceCode"); // Name is required
	provinceCode.setValue(invoiceWs.getProvinceCode());
	//End Story 1750051 CA GIL changes
	distVendorNum = oagisFactory.createNameValuePair();
	distVendorNum.setName("distVendorNumber"); // Name is required
	distVendorNum.setValue(invoiceWs.getDistVendorNumber());
	
	// InterfaceData
	interfaceData = imsFactory.createInterfaceData();
	interfaceData.getInterfaceProperty().add(sourceIndc);
	interfaceData.getInterfaceProperty().add(cpsNumber); // Added CPS Number
															// for Story
															// #602545
	// interfaceData.getInterfaceProperty().add(equipmentSource);
	interfaceData.getInterfaceProperty().add(autoCreateCoaIndc);
	interfaceData.getInterfaceProperty().add(distVendorNum);  
	//Story 1750051 CA GIL changes
	interfaceData.getInterfaceProperty().add(provinceCode); 
	//End Story 1750051 CA GIL changes
	invoiceHeader.setInterfaceData(interfaceData);

	invoice.setHeader(invoiceHeader);

	if (invoiceWs.getDetails() != null) {
		detailItr = invoiceWs.getDetails().iterator();
		while (detailItr.hasNext()) {
			Object obj = detailItr.next();
			if (obj instanceof InvoiceDetail) {
				InvoiceDetail invDetail = (InvoiceDetail) obj;
				createDetailJaxbObjects(invoice, invDetail);
			}
		}
	}

	if (invoiceWs.getBillToAddress() != null) {
		Country billToCountry = null;
		Address billToPrimaryAddress = null;
		JAXBElement<Address> billToPrimaryAddressElement = null;
		Addresses billToAddresses = null;
		PartyId billToPartyId = null;
		PartyIdType billToPartyIdType = null;
		Name billToName = null;
		AddressLine.Line billToLineType = null;
		AddressLine billToAddressLine = null;

		billToParty = oagisFactory.createPartyInstitutional();

		// party id element.
		billToPartyId = oagisFactory.createPartyId();
		billToPartyId.setValue(invoiceWs.getBillToAddress()
				.getCustomerNumber()); // bill to customer number.
		// party id type element.
		billToPartyIdType = oagisFactory.createPartyIdType();
		billToPartyIdType.getId().add(billToPartyId);
		billToParty.setPartyId(billToPartyIdType);
		// name element.
		billToName = oagisFactory.createName();
		billToName.setValue(invoiceWs.getBillToAddress().getCustomerName()); // bill
																				// to
																				// customer
																				// name.
		billToParty.getName().add(billToName);
		// country element.
		billToCountry = oagisFactory.createCountry();
		if (invoiceWs.getBillToAddress().getCountryCode() != null) { // bill
																		// to
																		// country
																		// code.
			billToCountry.setValue(invoiceWs.getBillToAddress()
					.getCountryCode());
		} else {
			billToCountry.setValue(getCountryCode(invoice));
		}
		// address line element.
		billToAddressLine = oagisFactory.createAddressLine();
		if (invoiceWs.getBillToAddress().getAddressLine1() != null
				&& !invoiceWs.getBillToAddress().getAddressLine1()
						.equals("")) {
			// line type element.
			billToLineType = oagisFactory.createAddressLineLine();
			billToLineType.setSequence(new BigInteger("1"));
			billToLineType.setValue(invoiceWs.getBillToAddress()
					.getAddressLine1()); // bill to address line 1.
			billToAddressLine.getLine().add(billToLineType);
		}
		if (invoiceWs.getBillToAddress().getAddressLine2() != null
				&& !invoiceWs.getBillToAddress().getAddressLine2()
						.equals("")) {
			// line type element.
			billToLineType = oagisFactory.createAddressLineLine();
			billToLineType.setSequence(new BigInteger("2"));
			billToLineType.setValue(invoiceWs.getBillToAddress()
					.getAddressLine2()); // bill to address line 2.
			billToAddressLine.getLine().add(billToLineType);
		}
		if (invoiceWs.getBillToAddress().getAddressLine3() != null
				&& !invoiceWs.getBillToAddress().getAddressLine3()
						.equals("")) {
			// line type element.
			billToLineType = oagisFactory.createAddressLineLine();
			billToLineType.setSequence(new BigInteger("3"));
			billToLineType.setValue(invoiceWs.getBillToAddress()
					.getAddressLine3()); // bill to address line 3.
			billToAddressLine.getLine().add(billToLineType);
		}
		if (invoiceWs.getBillToAddress().getAddressLine4() != null
				&& !invoiceWs.getBillToAddress().getAddressLine4()
						.equals("")) {
			// line type element.
			billToLineType = oagisFactory.createAddressLineLine();
			billToLineType.setSequence(new BigInteger("4"));
			billToLineType.setValue(invoiceWs.getBillToAddress()
					.getAddressLine4()); // bill to address line 4.
			billToAddressLine.getLine().add(billToLineType);
		}
		if (invoiceWs.getBillToAddress().getAddressLine5() != null
				&& !invoiceWs.getBillToAddress().getAddressLine5()
						.equals("")) {
			// line type element.
			billToLineType = oagisFactory.createAddressLineLine();
			billToLineType.setSequence(new BigInteger("5"));
			billToLineType.setValue(invoiceWs.getBillToAddress()
					.getAddressLine5()); // bill to address line 5.
			billToAddressLine.getLine().add(billToLineType);
		}
		if (invoiceWs.getBillToAddress().getAddressLine6() != null
				&& !invoiceWs.getBillToAddress().getAddressLine6()
						.equals("")) {
			// line type element.
			billToLineType = oagisFactory.createAddressLineLine();
			billToLineType.setSequence(new BigInteger("6"));
			billToLineType.setValue(invoiceWs.getBillToAddress()
					.getAddressLine6()); // bill to address line 6.
			billToAddressLine.getLine().add(billToLineType);
		}
		// primary address element.
		billToPrimaryAddress = oagisFactory.createAddress();
		billToPrimaryAddress.setCountry(billToCountry);
		billToPrimaryAddress.setAddressLine(billToAddressLine);
		billToPrimaryAddressElement = oagisFactory
				.createPrimaryAddress(billToPrimaryAddress);
		// addresses element.
		billToAddresses = oagisFactory.createAddresses();
		billToAddresses.getAddress().add(billToPrimaryAddressElement);
		billToParty.setAddresses(billToAddresses);
		billToPartyElement = oagisFactory.createBillToParty(billToParty);
		parties.getPartyType().add(billToPartyElement);
	}

	if (invoiceWs.getInstallToAddress() != null) {
		Country installToCountry = null;
		Address installToPrimaryAddress = null;
		JAXBElement<Address> installToPrimaryAddressElement = null;
		Addresses installToAddresses = null;
		PartyId installToPartyId = null;
		PartyIdType installToPartyIdType = null;
		Name installToName = null;
		AddressLine.Line installToLineType = null;
		AddressLine installToAddressLine = null;

		installToParty = oagisFactory.createPartyInstitutional();

		// party id element.
		installToPartyId = oagisFactory.createPartyId();
		installToPartyId.setValue(invoiceWs.getInstallToAddress()
				.getCustomerNumber()); // install to customer number.
		// party id type element.
		installToPartyIdType = oagisFactory.createPartyIdType();
		installToPartyIdType.getId().add(installToPartyId);
		installToParty.setPartyId(installToPartyIdType);
		// name element.
		installToName = oagisFactory.createName();
		installToName.setValue(invoiceWs.getInstallToAddress()
				.getCustomerName()); // install to customer name.
		installToParty.getName().add(installToName);
		// country element.
		installToCountry = oagisFactory.createCountry();
		if (invoiceWs.getInstallToAddress().getCountryCode() != null) { // install
																		// to
																		// country
																		// code.
			installToCountry.setValue(invoiceWs.getInstallToAddress()
					.getCountryCode());
		} else {
			installToCountry.setValue(getCountryCode(invoice));
		}
		installToAddressLine = oagisFactory.createAddressLine();
		if (invoiceWs.getInstallToAddress().getAddressLine1() != null
				&& !invoiceWs.getInstallToAddress().getAddressLine1()
						.equals("")) {
			// line type element.
			installToLineType = oagisFactory.createAddressLineLine();
			installToLineType.setSequence(new BigInteger("1"));
			installToLineType.setValue(invoiceWs.getInstallToAddress()
					.getAddressLine1()); // install to address line 1.
			installToAddressLine.getLine().add(installToLineType);
		}
		if (invoiceWs.getInstallToAddress().getAddressLine2() != null
				&& !invoiceWs.getInstallToAddress().getAddressLine2()
						.equals("")) {
			// line type element.
			installToLineType = oagisFactory.createAddressLineLine();
			installToLineType.setSequence(new BigInteger("2"));
			installToLineType.setValue(invoiceWs.getInstallToAddress()
					.getAddressLine2()); // install to address line 2.
			installToAddressLine.getLine().add(installToLineType);
		}
		if (invoiceWs.getInstallToAddress().getAddressLine3() != null
				&& !invoiceWs.getInstallToAddress().getAddressLine3()
						.equals("")) {
			// line type element.
			installToLineType = oagisFactory.createAddressLineLine();
			installToLineType.setSequence(new BigInteger("3"));
			installToLineType.setValue(invoiceWs.getInstallToAddress()
					.getAddressLine3()); // install to address line 3.
			installToAddressLine.getLine().add(installToLineType);
		}
		if (invoiceWs.getInstallToAddress().getAddressLine4() != null
				&& !invoiceWs.getInstallToAddress().getAddressLine4()
						.equals("")) {
			// line type element.
			installToLineType = oagisFactory.createAddressLineLine();
			installToLineType.setSequence(new BigInteger("4"));
			installToLineType.setValue(invoiceWs.getInstallToAddress()
					.getAddressLine4()); // install to address line 4.
			installToAddressLine.getLine().add(installToLineType);
		}
		if (invoiceWs.getInstallToAddress().getAddressLine5() != null
				&& !invoiceWs.getInstallToAddress().getAddressLine5()
						.equals("")) {
			// line type element.
			installToLineType = oagisFactory.createAddressLineLine();
			installToLineType.setSequence(new BigInteger("5"));
			installToLineType.setValue(invoiceWs.getInstallToAddress()
					.getAddressLine5()); // install to address line 5.
			installToAddressLine.getLine().add(installToLineType);
		}
		if (invoiceWs.getInstallToAddress().getAddressLine6() != null
				&& !invoiceWs.getInstallToAddress().getAddressLine6()
						.equals("")) {
			// line type element.
			installToLineType = oagisFactory.createAddressLineLine();
			installToLineType.setSequence(new BigInteger("6"));
			installToLineType.setValue(invoiceWs.getInstallToAddress()
					.getAddressLine6()); // install to address line 6.
			installToAddressLine.getLine().add(installToLineType);
		}
		// primary address element.
		installToPrimaryAddress = oagisFactory.createAddress();
		installToPrimaryAddress.setCountry(installToCountry);
		installToPrimaryAddress.setAddressLine(installToAddressLine);
		installToPrimaryAddressElement = oagisFactory
				.createPrimaryAddress(installToPrimaryAddress);
		// addresses element.
		installToAddresses = oagisFactory.createAddresses();
		installToAddresses.getAddress().add(installToPrimaryAddressElement);
		installToParty.setAddresses(installToAddresses);
		installToPartyElement = oagisFactory
				.createInstalledAtParty(installToParty);

		parties.getPartyType().add(installToPartyElement);
	}

	if (invoiceWs.getShipToAddress() != null) {
		Country shipToCountry = null;
		Address shipToPrimaryAddress = null;
		JAXBElement<Address> shipToPrimaryAddressElement = null;
		Addresses shipToAddresses = null;
		PartyId shipToPartyId = null;
		// PartyIdType shipToPartyIdType = null;
		Name shipToName = null;
		AddressLine.Line shipToLineType = null;
		AddressLine shipToAddressLine = null;

		shipToParty = oagisFactory.createPartyInstitutional();

		// party id element.
		shipToPartyId = oagisFactory.createPartyId();
		shipToPartyId.setIdOrigin("IBM/ShipTo");
		shipToPartyId.setValue(invoiceWs.getShipToAddress()
				.getCustomerNumber()); // ship to customer number.
		// party id type element.
		shipToPartyIdType = oagisFactory.createPartyIdType();
		shipToPartyIdType.getId().add(shipToPartyId);
		shipToParty.setPartyId(shipToPartyIdType);
		// name element.
		shipToName = oagisFactory.createName();
		shipToName.setValue(invoiceWs.getShipToAddress().getCustomerName()); // ship
																				// to
																				// customer
																				// name.
		shipToParty.getName().add(shipToName);
		// country element.
		shipToCountry = oagisFactory.createCountry();
		if (invoiceWs.getShipToAddress().getCountryCode() != null) { // ship
																		// to
																		// country
																		// code.
			shipToCountry.setValue(invoiceWs.getShipToAddress()
					.getCountryCode());
		} else {
			shipToCountry.setValue(getCountryCode(invoice));
		}
		// address line element.
		shipToAddressLine = oagisFactory.createAddressLine();
		if (invoiceWs.getShipToAddress().getAddressLine1() != null
				&& !invoiceWs.getShipToAddress().getAddressLine1()
						.equals("")) {
			// line type element.
			shipToLineType = oagisFactory.createAddressLineLine();
			shipToLineType.setSequence(new BigInteger("1"));
			shipToLineType.setValue(invoiceWs.getShipToAddress()
					.getAddressLine1()); // ship to address line 1.
			shipToAddressLine.getLine().add(shipToLineType);
		}
		if (invoiceWs.getShipToAddress().getAddressLine2() != null
				&& !invoiceWs.getShipToAddress().getAddressLine2()
						.equals("")) {
			// line type element.
			shipToLineType = oagisFactory.createAddressLineLine();
			shipToLineType.setSequence(new BigInteger("2"));
			shipToLineType.setValue(invoiceWs.getShipToAddress()
					.getAddressLine2()); // ship to address line 2.
			shipToAddressLine.getLine().add(shipToLineType);
		}
		if (invoiceWs.getShipToAddress().getAddressLine3() != null
				&& !invoiceWs.getShipToAddress().getAddressLine3()
						.equals("")) {
			// line type element.
			shipToLineType = oagisFactory.createAddressLineLine();
			shipToLineType.setSequence(new BigInteger("3"));
			shipToLineType.setValue(invoiceWs.getShipToAddress()
					.getAddressLine3()); // ship to address line 3.
			shipToAddressLine.getLine().add(shipToLineType);
		}
		if (invoiceWs.getShipToAddress().getAddressLine4() != null
				&& !invoiceWs.getShipToAddress().getAddressLine4()
						.equals("")) {
			// line type element.
			shipToLineType = oagisFactory.createAddressLineLine();
			shipToLineType.setSequence(new BigInteger("4"));
			shipToLineType.setValue(invoiceWs.getShipToAddress()
					.getAddressLine4()); // ship to address line 4.
			shipToAddressLine.getLine().add(shipToLineType);
		}
		if (invoiceWs.getShipToAddress().getAddressLine5() != null
				&& !invoiceWs.getShipToAddress().getAddressLine5()
						.equals("")) {
			// line type element.
			shipToLineType = oagisFactory.createAddressLineLine();
			shipToLineType.setSequence(new BigInteger("5"));
			shipToLineType.setValue(invoiceWs.getShipToAddress()
					.getAddressLine5()); // ship to address line 5.
			shipToAddressLine.getLine().add(shipToLineType);
		}
		if (invoiceWs.getShipToAddress().getAddressLine6() != null
				&& !invoiceWs.getShipToAddress().getAddressLine6()
						.equals("")) {
			// line type element.
			shipToLineType = oagisFactory.createAddressLineLine();
			shipToLineType.setSequence(new BigInteger("6"));
			shipToLineType.setValue(invoiceWs.getShipToAddress()
					.getAddressLine6()); // ship to address line 6.
			shipToAddressLine.getLine().add(shipToLineType);
		}
		// primary address element.
		shipToPrimaryAddress = oagisFactory.createAddress();
		shipToPrimaryAddress.setCountry(shipToCountry);
		shipToPrimaryAddress.setAddressLine(shipToAddressLine);
		shipToPrimaryAddressElement = oagisFactory
				.createPrimaryAddress(shipToPrimaryAddress);
		// addresses element.
		shipToAddresses = oagisFactory.createAddresses();
		shipToAddresses.getAddress().add(shipToPrimaryAddressElement);
		shipToParty.setAddresses(shipToAddresses);
		shipToPartyElement = oagisFactory.createShipToParty(shipToParty);
		parties.getPartyType().add(shipToPartyElement);
	}

	if (invoiceWs.getSoldToAddress() != null) {
		Country soldToCountry = null;
		Address soldToPrimaryAddress = null;
		JAXBElement<Address> soldToPrimaryAddressElement = null;
		Addresses soldToAddresses = null;
		PartyId soldToPartyId = null;
		PartyIdType soldToPartyIdType = null;
		Name soldToName = null;
		Amount amount = null; // Change to ROF Facsimile multiple sold to's (US) - Story 1064780
		AddressLine.Line soldToLineType = null;
		AddressLine soldToAddressLine = null;

		soldToParty = oagisFactory.createPartyInstitutional();

		// party id element.
		soldToPartyId = oagisFactory.createPartyId();
		soldToPartyId.setIdOrigin("IBM/SoldTo");
		soldToPartyId.setValue(invoiceWs.getSoldToAddress()
				.getCustomerNumber()); // sold to customer number.
		// party id type element.
		soldToPartyIdType = oagisFactory.createPartyIdType();
		soldToPartyIdType.getId().add(soldToPartyId);
		soldToParty.setPartyId(soldToPartyIdType);
		// name element.
		soldToName = oagisFactory.createName();
		
		soldToName.setValue(invoiceWs.getSoldToAddress().getCustomerName()); // sold
																				// to
																				// customer
																				// name.
		soldToParty.getName().add(soldToName);
		// country element.
		soldToCountry = oagisFactory.createCountry();
		if (invoiceWs.getSoldToAddress().getCountryCode() != null) { // sold
																		// to
																		// country
																		// code.
			soldToCountry.setValue(invoiceWs.getSoldToAddress()
					.getCountryCode());
		} else {
			soldToCountry.setValue(getCountryCode(invoice));
		}
		// address line element.
		soldToAddressLine = oagisFactory.createAddressLine();
		if (invoiceWs.getSoldToAddress().getAddressLine1() != null
				&& !invoiceWs.getSoldToAddress().getAddressLine1()
						.equals("")) {
			// line type element.
			soldToLineType = oagisFactory.createAddressLineLine();
			soldToLineType.setSequence(new BigInteger("1"));
			soldToLineType.setValue(invoiceWs.getSoldToAddress()
					.getAddressLine1()); // sold to address line 1.
			soldToAddressLine.getLine().add(soldToLineType);
		}
		if (invoiceWs.getSoldToAddress().getAddressLine2() != null
				&& !invoiceWs.getSoldToAddress().getAddressLine2()
						.equals("")) {
			// line type element.
			soldToLineType = oagisFactory.createAddressLineLine();
			soldToLineType.setSequence(new BigInteger("2"));
			soldToLineType.setValue(invoiceWs.getSoldToAddress()
					.getAddressLine2()); // sold to address line 2.
			soldToAddressLine.getLine().add(soldToLineType);
		}
		if (invoiceWs.getSoldToAddress().getAddressLine3() != null
				&& !invoiceWs.getSoldToAddress().getAddressLine3()
						.equals("")) {
			// line type element.
			soldToLineType = oagisFactory.createAddressLineLine();
			soldToLineType.setSequence(new BigInteger("3"));
			soldToLineType.setValue(invoiceWs.getSoldToAddress()
					.getAddressLine3()); // sold to address line 3.
			soldToAddressLine.getLine().add(soldToLineType);
		}
		if (invoiceWs.getSoldToAddress().getAddressLine4() != null
				&& !invoiceWs.getSoldToAddress().getAddressLine4()
						.equals("")) {
			// line type element.
			soldToLineType = oagisFactory.createAddressLineLine();
			soldToLineType.setSequence(new BigInteger("4"));
			soldToLineType.setValue(invoiceWs.getSoldToAddress()
					.getAddressLine4()); // sold to address line 4.
			soldToAddressLine.getLine().add(soldToLineType);
		}
		if (invoiceWs.getSoldToAddress().getAddressLine5() != null
				&& !invoiceWs.getSoldToAddress().getAddressLine5()
						.equals("")) {
			// line type element.
			soldToLineType = oagisFactory.createAddressLineLine();
			soldToLineType.setSequence(new BigInteger("5"));
			soldToLineType.setValue(invoiceWs.getSoldToAddress()
					.getAddressLine5()); // sold to address line 5.
			soldToAddressLine.getLine().add(soldToLineType);
		}
		if (invoiceWs.getSoldToAddress().getAddressLine6() != null
				&& !invoiceWs.getSoldToAddress().getAddressLine6()
						.equals("")) {
			// line type element.
			soldToLineType = oagisFactory.createAddressLineLine();
			soldToLineType.setSequence(new BigInteger("6"));
			soldToLineType.setValue(invoiceWs.getSoldToAddress()
					.getAddressLine6()); // sold to address line 6.
			soldToAddressLine.getLine().add(soldToLineType);
		}
		// primary address element.
		soldToPrimaryAddress = oagisFactory.createAddress();
		soldToPrimaryAddress.setCountry(soldToCountry);
		soldToPrimaryAddress.setAddressLine(soldToAddressLine);
		soldToPrimaryAddressElement = oagisFactory
				.createPrimaryAddress(soldToPrimaryAddress);
		// addresses element.
		soldToAddresses = oagisFactory.createAddresses();
		soldToAddresses.getAddress().add(soldToPrimaryAddressElement);
		
		// Change to ROF Facsimile multiple sold to's (US) - Story 1064780
		
		amount = oagisFactory.createAmount();
		amount.setValue(invoiceWs.getSoldToAddress().getTotalTax());
		soldToParty.getTotalTax().add(amount);

		soldToParty.setAddresses(soldToAddresses);
		
		soldToPartyElement = oagisFactory.createSoldToParty(soldToParty);
		parties.getPartyType().add(soldToPartyElement);
	}
	
	// Change to ROF Facsimile multiple sold to's (US) - Story 1064780
	
	if (invoiceWs.getSoldToSecondaryAddress() != null) {
		Country soldToCountry = null;
		Address soldToSecondaryAddress = null;
		JAXBElement<Address> soldToSecondaryAddressElement = null;
		Addresses soldToSecondaryAddresses = null;
		PartyId soldToSecondaryPartyId = null;
		PartyIdType soldToSecondaryPartyIdType = null;
		Name soldToName = null;
		Amount amount = null;
		AddressLine.Line soldToSecondaryLineType = null;
		AddressLine soldToSecondaryAddressLine = null;

		soldToSecondaryParty = oagisFactory.createPartyInstitutional();

		// party id element.
		soldToSecondaryPartyId = oagisFactory.createPartyId();
		soldToSecondaryPartyId.setIdOrigin("IBM/SoldToSecondary");
		soldToSecondaryPartyId.setValue(invoiceWs.getSoldToSecondaryAddress()
				.getCustomerNumber()); // sold to customer number.
		// party id type element.
		soldToSecondaryPartyIdType = oagisFactory.createPartyIdType();
		soldToSecondaryPartyIdType.getId().add(soldToSecondaryPartyId);
		soldToSecondaryParty.setPartyId(soldToSecondaryPartyIdType);
		// name element.
		soldToName = oagisFactory.createName();
		
		soldToName.setValue(invoiceWs.getSoldToSecondaryAddress().getCustomerName()); // sold
																				// to
																				// customer
																				// name.
		soldToSecondaryParty.getName().add(soldToName);
		// country element.
		soldToCountry = oagisFactory.createCountry();
		if (invoiceWs.getSoldToSecondaryAddress().getCountryCode() != null) { // sold
																		// to
																		// country
																		// code.
			soldToCountry.setValue(invoiceWs.getSoldToSecondaryAddress()
					.getCountryCode());
		} else {
			soldToCountry.setValue(getCountryCode(invoice));
		}
		// address line element.
		soldToSecondaryAddressLine = oagisFactory.createAddressLine();
		if (invoiceWs.getSoldToSecondaryAddress().getAddressLine1() != null
				&& !invoiceWs.getSoldToSecondaryAddress().getAddressLine1()
						.equals("")) {
			// line type element.
			soldToSecondaryLineType = oagisFactory.createAddressLineLine();
			soldToSecondaryLineType.setSequence(new BigInteger("1"));
			soldToSecondaryLineType.setValue(invoiceWs.getSoldToSecondaryAddress()
					.getAddressLine1()); // sold to address line 1.
			soldToSecondaryAddressLine.getLine().add(soldToSecondaryLineType);
		}
		if (invoiceWs.getSoldToSecondaryAddress().getAddressLine2() != null
				&& !invoiceWs.getSoldToSecondaryAddress().getAddressLine2()
						.equals("")) {
			// line type element.
			soldToSecondaryLineType = oagisFactory.createAddressLineLine();
			soldToSecondaryLineType.setSequence(new BigInteger("2"));
			soldToSecondaryLineType.setValue(invoiceWs.getSoldToSecondaryAddress()
					.getAddressLine2()); // sold to address line 2.
			soldToSecondaryAddressLine.getLine().add(soldToSecondaryLineType);
		}
		if (invoiceWs.getSoldToSecondaryAddress().getAddressLine3() != null
				&& !invoiceWs.getSoldToSecondaryAddress().getAddressLine3()
						.equals("")) {
			// line type element.
			soldToSecondaryLineType = oagisFactory.createAddressLineLine();
			soldToSecondaryLineType.setSequence(new BigInteger("3"));
			soldToSecondaryLineType.setValue(invoiceWs.getSoldToSecondaryAddress()
					.getAddressLine3()); // sold to address line 3.
			soldToSecondaryAddressLine.getLine().add(soldToSecondaryLineType);
		}
		if (invoiceWs.getSoldToSecondaryAddress().getAddressLine4() != null
				&& !invoiceWs.getSoldToSecondaryAddress().getAddressLine4()
						.equals("")) {
			// line type element.
			soldToSecondaryLineType = oagisFactory.createAddressLineLine();
			soldToSecondaryLineType.setSequence(new BigInteger("4"));
			soldToSecondaryLineType.setValue(invoiceWs.getSoldToSecondaryAddress()
					.getAddressLine4()); // sold to address line 4.
			soldToSecondaryAddressLine.getLine().add(soldToSecondaryLineType);
		}
		if (invoiceWs.getSoldToSecondaryAddress().getAddressLine5() != null
				&& !invoiceWs.getSoldToSecondaryAddress().getAddressLine5()
						.equals("")) {
			// line type element.
			soldToSecondaryLineType = oagisFactory.createAddressLineLine();
			soldToSecondaryLineType.setSequence(new BigInteger("5"));
			soldToSecondaryLineType.setValue(invoiceWs.getSoldToSecondaryAddress()
					.getAddressLine5()); // sold to address line 5.
			soldToSecondaryAddressLine.getLine().add(soldToSecondaryLineType);
		}
		if (invoiceWs.getSoldToSecondaryAddress().getAddressLine6() != null
				&& !invoiceWs.getSoldToSecondaryAddress().getAddressLine6()
						.equals("")) {
			// line type element.
			soldToSecondaryLineType = oagisFactory.createAddressLineLine();
			soldToSecondaryLineType.setSequence(new BigInteger("6"));
			soldToSecondaryLineType.setValue(invoiceWs.getSoldToSecondaryAddress()
					.getAddressLine6()); // sold to address line 6.
			soldToSecondaryAddressLine.getLine().add(soldToSecondaryLineType);
		}
		// primary address element.
		soldToSecondaryAddress = oagisFactory.createAddress();
		soldToSecondaryAddress.setCountry(soldToCountry);
		soldToSecondaryAddress.setAddressLine(soldToSecondaryAddressLine);
		soldToSecondaryAddressElement = oagisFactory
				.createPrimaryAddress(soldToSecondaryAddress);
		
		// addresses element.
		soldToSecondaryAddresses = oagisFactory.createAddresses();
		soldToSecondaryAddresses.getAddress().add(soldToSecondaryAddressElement);
		
		amount = oagisFactory.createAmount();
		amount.setValue(invoiceWs.getSoldToSecondaryAddress().getTotalTax());
		soldToSecondaryParty.getTotalTax().add(amount);

		soldToSecondaryParty.setAddresses(soldToSecondaryAddresses);
		soldToSecondaryPartyElement = oagisFactory.createSoldToSecondaryParty(soldToSecondaryParty);
		parties.getPartyType().add(soldToSecondaryPartyElement);
	}

	if (invoiceWs.getComments() != null) {
		commentsItr = invoiceWs.getComments().iterator();
		while (commentsItr.hasNext()) {
			Object obj = commentsItr.next();
			if (obj instanceof InvoiceComment) {
				InvoiceComment invComment = (InvoiceComment) obj;
				createCommentJaxbObjects(invoice, invComment);
			}
		}
	}
	return invoice;
//	jaxbRequest.getDataArea().getInvoice().add(invoice);
}

/**
 * getCountryCode method.
 * 
 * @param invoice_jaxb
 *            ImsInvoiceElement
 * 
 * @return String
 */
private String getCountryCode(
		Invoice invoice_jaxb) {
	String countryCode = null;
	Iterator<?> partiesItr = invoice_jaxb.getHeader().getParties()
			.getPartyType().iterator();
	PartyInstitutional supplierParty = null;
	while (partiesItr.hasNext()) {
		Object obj = partiesItr.next();
		if (obj instanceof JAXBElement) {
			if (((JAXBElement<?>) obj).getName().getLocalPart()
					.equals("SupplierParty")) {
				supplierParty = (PartyInstitutional) ((JAXBElement<?>) obj)
						.getValue();
				Iterator<?> addressesItr = supplierParty.getAddresses()
						.getAddress().iterator();
				while (addressesItr.hasNext()) {
					Object obj2 = addressesItr.next();
					if (obj2 instanceof JAXBElement) {
						if (((JAXBElement<?>) obj2).getName()
								.getLocalPart().equals("PrimaryAddress")) {
							Address primaryAddress = (Address) ((JAXBElement<?>) obj2)
									.getValue();
							if (primaryAddress.getCountry().getValue() != null) {
								countryCode = primaryAddress.getCountry()
										.getValue().trim();
							}
						}

					}
				}
			}

		}
	}

	return countryCode;
}


}
