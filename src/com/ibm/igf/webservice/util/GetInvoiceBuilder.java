package com.ibm.igf.webservice.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.openapplications.oagis._8_2.getinvoice.Address;
import org.openapplications.oagis._8_2.getinvoice.Addresses;
import org.openapplications.oagis._8_2.getinvoice.Country;
import org.openapplications.oagis._8_2.getinvoice.DocumentIdType;
import org.openapplications.oagis._8_2.getinvoice.DocumentIds;
import org.openapplications.oagis._8_2.getinvoice.DocumentReferences;
import org.openapplications.oagis._8_2.getinvoice.Get;
import org.openapplications.oagis._8_2.getinvoice.Id;
import org.openapplications.oagis._8_2.getinvoice.ObjectFactory;
import org.openapplications.oagis._8_2.getinvoice.OrderDocumentReference;
import org.openapplications.oagis._8_2.getinvoice.Parties;
import org.openapplications.oagis._8_2.getinvoice.PartyDocumentId;
import org.openapplications.oagis._8_2.getinvoice.PartyInstitutional;
import org.openapplications.oagis._8_2.getinvoice.Sender;
import org.openapplications.oagis._8_2.getinvoice.RequestVerb.ReturnCriteria;

import com.ibm.gil.business.InvoiceELS;
import com.ibm.xmlns.ims._1_3.getinvoice.ApplicationArea;
import com.ibm.xmlns.ims._1_3.getinvoice.Invoice;
import com.ibm.xmlns.ims._1_3.getinvoice.InvoiceHeader;
import com.ibm.xmlns.ims._1_3.getinvoice.GetInvoice.DataArea;

public class GetInvoiceBuilder {
	
	private final static String TIMEZONE="GMT";
	private final static String REVISION="1.0";
	private final static String INVOICE_ID_ORIGIN="IBM/Invoice";
	private final static String CM_ID_ORIGIN="ContentManager";
	private final static String SELECT_EXPRESSION="Get";
	private final static String COMPONENT="GIL";
	
//	private GetInvoice_Service service;
//	private GetInvoice getInvoice;
	private com.ibm.xmlns.ims._1_3.getinvoice.GetInvoice invoiceToSend;
	private String invoiceNumber;
	private String component;
	private String bodId;
	private String country;
	private String cmObjectId;
	
	public GetInvoiceBuilder(InvoiceELS gilInvoice){
		//Values which will be set to the GetInvoice, needed to retrieve ShowInvoice 
		this.invoiceNumber=gilInvoice.getINVOICENUMBER();
		this.component=gilInvoice.getSENDER();
		this.bodId=gilInvoice.getUNIQUEREQUESTNUMBER();
		this.country=gilInvoice.getCOUNTRY();
		// in GilClientCode have found it comes empty but leaving this setting in case it changes in future
		this.cmObjectId=gilInvoice.getOBJECTID();
	}
	public com.ibm.xmlns.ims._1_3.getinvoice.GetInvoice buildGetInvoice() throws Exception{
		
		
		DataArea dataArea=createDataArea();
		
		ApplicationArea applicationArea= createAppArea();
		
		setInvoice(REVISION, applicationArea, dataArea);
		
		return invoiceToSend;
	}
	private  Sender getInvoiceSender() throws Exception{
		Sender s = new Sender();
		s.setComponent(this.component);
		return s;
	}
	
	private ReturnCriteria getReturnCriteria(){
		String []crit={SELECT_EXPRESSION};
		ReturnCriteria rc= new ReturnCriteria();
		rc.getSelectExpression().addAll(Arrays.asList(crit));
		return rc;
	}
	
	private  Get getGet(ReturnCriteria value){
		Get get= new Get();
		get.setReturnCriteria(value);
		return get;
	}
	
	private List<Invoice> createInvoicesList() throws DatatypeConfigurationException  {
		List<Invoice> invoiceList= new ArrayList<Invoice>();
		Invoice i=new Invoice();
		
		
		Parties parties=getParties();

		GregorianCalendar calendar = new GregorianCalendar();
		calendar.clear();
	    XMLGregorianCalendar  creationDateTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
		//invoice date area
	    
	    calendar.clear();
	    calendar.setTimeZone(TimeZone.getTimeZone(TIMEZONE));
	    creationDateTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
		
		DocumentIds documentIds=getDocumentIds(getSupplierDocumentId(this.invoiceNumber,INVOICE_ID_ORIGIN));
		DocumentReferences documentReferences=getDocumentReferences();
		
		
		i.setHeader(getHeader(creationDateTime, documentIds, documentReferences, parties));
		
		invoiceList.add(i);
		return invoiceList;
	}
	
	private  Parties getParties() {
		Parties p=new Parties();
		
		ObjectFactory factory= new ObjectFactory();
		
		PartyInstitutional partyBase=new PartyInstitutional();
		Addresses addresses= new Addresses();
		Address addressV=new Address();
		
		Country c= new Country();
		c.setValue(this.country);
		addressV.setCountry(c);
		
		
		JAXBElement<Address> primaryAddress=factory.createPrimaryAddress(addressV);		
		addresses.getAddress().add(primaryAddress);
		
		partyBase.setAddresses(addresses);
		JAXBElement<PartyInstitutional>  supplierParty=factory.createSupplierParty(partyBase);
		
//		JAXBElement<PartyBase> element=factory.createPartyType(supplierParty);
		p.getPartyType().add(supplierParty);
		
		
		return p;
	}
	
	private static InvoiceHeader getHeader(XMLGregorianCalendar documentDateType,DocumentIds documentIds,DocumentReferences documentReferences,Parties parties) {
		InvoiceHeader header= new InvoiceHeader();
		header.setDocumentDateTime(documentDateType);
		header.setDocumentIds(documentIds);
		header.setDocumentReferences(documentReferences);
		header.setParties(parties);
		return header;
	}


	private  void setInvoice(String revision, ApplicationArea applicationArea,DataArea dataArea) throws Exception{
		invoiceToSend=new com.ibm.xmlns.ims._1_3.getinvoice.GetInvoice();
		invoiceToSend.setDataArea(dataArea);
		invoiceToSend.setApplicationArea(applicationArea);
		invoiceToSend.setRevision(revision);
		
	}
	private  DocumentReferences getDocumentReferences() {
		DocumentReferences dr= new DocumentReferences();
		ObjectFactory of=new ObjectFactory();
		OrderDocumentReference documentReference= new OrderDocumentReference();
		
		
		DocumentIds docIds= getDocumentIds(getdocumentIdType(this.cmObjectId, CM_ID_ORIGIN));
		documentReference.setDocumentIds(docIds);
//		JAXBElement<DocumentReference> docRef=of.createDocumentReference(documentReference);
		JAXBElement<OrderDocumentReference> invoiceDocRef=of.createInvoiceDocumentReference(documentReference);
		
		dr.getDocumentReference().add(invoiceDocRef);
		return dr;
	}

	@SuppressWarnings("unchecked")
	private  DocumentIds getDocumentIds(Object innerObject) {
		DocumentIds ids= new DocumentIds();
		
		ids.getDocumentIdType().add((JAXBElement<DocumentIdType>)innerObject);
		return ids;
	}

	private  JAXBElement<PartyDocumentId> getSupplierDocumentId(String value,String origin){
		ObjectFactory of= new ObjectFactory();
		PartyDocumentId partyDocId =of.createPartyDocumentId();
		Id id= new Id();
		id.setIdOrigin(origin);
		if(value!=null)id.setValue(value);
		partyDocId.setId(id);
		JAXBElement<PartyDocumentId> el=of.createSupplierDocumentId(partyDocId);
		return el;
	}
	
	private  JAXBElement<DocumentIdType> getdocumentIdType(String value,String origin){
		ObjectFactory of= new ObjectFactory();
		DocumentIdType docIdype=new DocumentIdType();
		Id id= new Id();
		id.setIdOrigin(origin);
		if(value!=null)id.setValue(value);
		docIdype.setId(id);
		JAXBElement<DocumentIdType> docIdType=of.createDocumentIdType(docIdype);
		return docIdType;
	}
	
	private DataArea createDataArea(){
		DataArea da= new DataArea();
		List<Invoice> invoices;
		try {
					
			da.setGet(getGet(getReturnCriteria()));
			invoices = createInvoicesList();
			da.getInvoice().addAll(invoices);
			
		} catch (DatatypeConfigurationException e) {
			
			e.printStackTrace();
		}
		
		return da;
	}
	
	private  ApplicationArea createAppArea() throws Exception{
		Sender sender=getInvoiceSender();
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.clear();
	    XMLGregorianCalendar  creationDateTime = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
		ApplicationArea aa= new ApplicationArea();
		aa.setBODId(this.bodId);
		aa.setCreationDateTime(creationDateTime);
		aa.setSender(sender);
		return aa;
	}

}
