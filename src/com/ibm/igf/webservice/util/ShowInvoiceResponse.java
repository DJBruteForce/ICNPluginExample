package com.ibm.igf.webservice.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import javax.xml.bind.JAXBElement;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;


import com.ibm.xmlns.ims._1_3.showinvoice.*;

import com.ibm.xmlns.ims._1_3.showinvoice.Status;


import org.openapplications.oagis._8_2.showinvoice.*;

import financing.tools.gcps.ws.domain.BillToAddress;
import financing.tools.gcps.ws.domain.InstallToAddress;
import financing.tools.gcps.ws.domain.InvoiceComment;
import financing.tools.gcps.ws.domain.InvoiceDetail;
import financing.tools.gcps.ws.domain.ShipToAddress;
import financing.tools.gcps.ws.domain.SoldToAddress;
import financing.tools.gcps.ws.domain.jaxb.BillToAddress_jaxb;
import financing.tools.gcps.ws.domain.jaxb.InstallToAddress_jaxb;
import financing.tools.gcps.ws.domain.jaxb.InvoiceComment_jaxb;
import financing.tools.gcps.ws.domain.jaxb.InvoiceDetail_jaxb;
import financing.tools.gcps.ws.domain.jaxb.Invoice_jaxb;
import financing.tools.gcps.ws.domain.jaxb.ShipToAddress_jaxb;
import financing.tools.gcps.ws.domain.jaxb.SoldToAddress_jaxb;


public class ShowInvoiceResponse {
	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(ShowInvoiceResponse.class);
	private com.ibm.xmlns.ims._1_3.showinvoice.ShowInvoice showInvoiceRetrieved = null;	
	
	
	public ShowInvoiceResponse( com.ibm.xmlns.ims._1_3.showinvoice.ShowInvoice  showInvoiceRetrieved){
		this.showInvoiceRetrieved=showInvoiceRetrieved;
		
	}
	/**
	 * Get component.
	 *
	 * @return String
	 */
	public String getComponent() {
		return showInvoiceRetrieved.getApplicationArea().getSender().getComponent();
	}
	
	/**
	 * Get creation date time.
	 * 
	 * @return long
	 */
	public long getCreationDateTime() {
		return showInvoiceRetrieved.getApplicationArea().getCreationDateTime().toGregorianCalendar().getTimeInMillis();
	}
	
	/**
	 * Get bod id.
	 * 
	 * @return String
	 */
	public String getBodId() {
		return showInvoiceRetrieved.getApplicationArea().getBODId();
	}
	
	/**
	 * Get application status.
	 * 
	 * @return List
	 */
	public List<String[]> getApplicationStatus() {
		List<String[]> appStatusList = new ArrayList<String[]>();
		String[] statusLine = new String[4];
		
		
		Iterator<Application> appItr = showInvoiceRetrieved.getApplicationArea().getApplication().iterator();
		while (appItr.hasNext()) {
			Object obj1 = appItr.next();
			if (obj1 instanceof Application) {
				Application application = (Application) obj1;
				Iterator<Status> appStatusItr = 
					application.getApplicationStatus().iterator();
				while (appStatusItr.hasNext()) {
					Object obj2 = appStatusItr.next();
					if (obj2 instanceof Status) {
						Status appStatus = (Status) obj2;
						if (appStatus.getErrorData().size() == 1 &&
							appStatus.getErrorData().get(0) instanceof NameValuePair) {
							statusLine[0] = appStatus.getStatusId();
							statusLine[1] = appStatus.getErrorCode().getValue();
							statusLine[2] = appStatus.getErrorDescription();
							statusLine[3] = ((NameValuePair) appStatus.getErrorData().get(0)).getValue();
							appStatusList.add(statusLine);
						}
					}
				}
			}
		}
		
		return appStatusList;
	}
	
	/**
	 * Get status ids.
	 * 
	 * @return List
	 */
	public List<String> getStatusIds() {
		List<String> statusIds = new ArrayList<String>();
		
		Iterator<Application> appItr = showInvoiceRetrieved.getApplicationArea().getApplication().iterator();
		while (appItr.hasNext()) {
			Object obj1 = appItr.next();
			if (obj1 instanceof Application) {
				Application application = (Application) obj1;
				Iterator<Status> appStatusItr = 
					application.getApplicationStatus().iterator();
				while (appStatusItr.hasNext()) {
					Object obj2 = appStatusItr.next();
					if (obj2 instanceof Status) {
						Status appStatus = 
							(Status) obj2;
						statusIds.add(appStatus.getStatusId());
					}
				}
			}
		}
		
		return statusIds;
	}
	
	/**
	 * Get error codes.
	 * 
	 * @return List
	 */
	public List<String> getErrorCodes() {
		List<String> errorCodes = new ArrayList<String>();
		
		Iterator<Application> appItr = showInvoiceRetrieved.getApplicationArea().getApplication().iterator();
		while (appItr.hasNext()) {
			Object obj1 = appItr.next();
			if (obj1 instanceof Application) {
				Application application = (Application)obj1;
				Iterator<Status> appStatusItr = 
					application.getApplicationStatus().iterator();
				while (appStatusItr.hasNext()) {
					Object obj2 = appStatusItr.next();
					if (obj2 instanceof Status) {
						Status appStatus = 
							(Status) obj2;
						errorCodes.add(appStatus.getErrorCode().getValue());
					}
				}
			}
		}
		
		return errorCodes;
	}
	
	/**
	 * Get error descriptions.
	 * 
	 * @return List
	 */
	public List<String> getErrorDescriptions() {
		List<String> errorDesc = new ArrayList<String>();
		
		Iterator<Application> appItr = showInvoiceRetrieved.getApplicationArea().getApplication().iterator();
		while (appItr.hasNext()) {
			Object obj1 = appItr.next();
			if (obj1 instanceof Application) {
				Application application = (Application) obj1;
				Iterator<Status> appStatusItr = 
					application.getApplicationStatus().iterator();
				while (appStatusItr.hasNext()) {
					Object obj2 = appStatusItr.next();
					if (obj2 instanceof Status) {
						Status appStatus = 
							(Status) obj2;
						errorDesc.add(appStatus.getErrorDescription());
					}
				}
			}
		}
		
		return errorDesc;
	}
	
	/**
	 * Get error data.
	 * 
	 * @return List
	 */
	public List<String> getErrorData() {
		List<String> errordata = new ArrayList<String>();
		
		Iterator<Application> appItr = showInvoiceRetrieved.getApplicationArea().getApplication().iterator();
		while (appItr.hasNext()) {
			Object obj1 = appItr.next();
			if (obj1 instanceof Application) {
				Application application = (Application) obj1;
				Iterator<Status> appStatusItr = 
					application.getApplicationStatus().iterator();
				while (appStatusItr.hasNext()) {
					Object obj2 = appStatusItr.next();
					if (obj2 instanceof Status) {
						Status appStatus = 
							(Status) obj2;
						if (appStatus.getErrorData().size() == 1 &&
							appStatus.getErrorData().get(0) instanceof NameValuePair) {
							errordata.add(((NameValuePair) appStatus.getErrorData()
									.get(0)).getValue());
						}
					}
				}
			}
		}
		
		return errordata;
	}	
	
	/**
	 * Get invoices.
	 *
	 * @return List - financing.tools.gcps.ws.domain.Invoice
	 */
	public List<financing.tools.gcps.ws.domain.Invoice> getInvoices() {
		List<financing.tools.gcps.ws.domain.Invoice> invoices = new ArrayList<financing.tools.gcps.ws.domain.Invoice>();
		Iterator<Invoice> invoiceItr = null;
		Iterator<InvoiceLine> detailItr = null;
		Iterator<Note> commentItr = null;
		
		invoiceItr = showInvoiceRetrieved.getDataArea().getInvoice().iterator();
		while (invoiceItr.hasNext()) {
		    Object obj = invoiceItr.next();
		    if (obj instanceof Invoice) {
		    	com.ibm.xmlns.ims._1_3.showinvoice.Invoice invoice_jaxb = (com.ibm.xmlns.ims._1_3.showinvoice.Invoice) obj;
			    financing.tools.gcps.ws.domain.Invoice invoice = new Invoice_jaxb();
			    Calendar cal1, cal2 = null;
			    
//			    // invoice header.
			    invoice.setCountryCode(getCountryCode(invoice_jaxb));
			    invoice.setInvoiceNumber(getInvoiceNumber(invoice_jaxb));
			    /*
			     * invoice date. 
			     * the date from the server has an offset of +0 (GMT). the gil client
			     * needs to reconstruct the date with the locale timezone but without
			     * having the date/time adjusted by the offset.
			     */
			    cal2 = Calendar.getInstance();
			    cal2.setTimeZone (TimeZone.getTimeZone("GMT"));
			    cal2.setTimeInMillis(getInvoiceDate(invoice_jaxb));
			    cal1 = Calendar.getInstance();
				cal1.set(cal2.get(Calendar.YEAR), 
				        cal2.get(Calendar.MONTH), 
				        cal2.get(Calendar.DATE),  
				        0, 0, 1);
			    invoice.setInvoiceDate(cal1.getTime());
			    logger.debug("invoice retrieved date: "+invoice.getInvoiceDate().toString());
		        invoice.setTpidCode(getTpidCode(invoice_jaxb));
		        invoice.setTpidCountry(getTpidCountry(invoice_jaxb));
		        invoice.setInvoiceSequenceNumber(getInvoiceSequenceNumber(invoice_jaxb));
		        invoice.setCreateTimestamp(new Date(getCreateTimestamp(invoice_jaxb)));
			    /*
			     * invoice receipt date. 
			     * the date from the server has an offset of +0 (GMT). the gil client
			     * needs to reconstruct the date with the locale timezone but without
			     * having the date/time adjusted by the offset.
			     */
			    cal2 = Calendar.getInstance();
			    cal2.setTimeZone (TimeZone.getTimeZone("GMT"));
			    cal2.setTimeInMillis(getInvoiceReceiptDate(invoice_jaxb));
			    cal1 = Calendar.getInstance();
				cal1.set(cal2.get(Calendar.YEAR), 
				        cal2.get(Calendar.MONTH), 
				        cal2.get(Calendar.DATE),  
				        0, 0, 1);
		        invoice.setInvoiceReceiveDate(cal1.getTime());
		        invoice.setCmObjectId(getCmObjectId(invoice_jaxb));
		        invoice.setOfferLetterNumber(getOfferLetterNumber(invoice_jaxb));
		        invoice.setLegalId(getLegalId(invoice_jaxb));
		        invoice.setCoaInvoiceIndicator(new Boolean(getCoaInvoiceIndicator(invoice_jaxb)));
		        invoice.setInvoiceType(getInvoiceType(invoice_jaxb).trim());
		        invoice.setPoexCode(getPoexCode(invoice_jaxb));
		        invoice.setResponsibleId(getResponsibleId(invoice_jaxb));
		        invoice.setReferenceInvoiceNumber(getReferenceInvoiceNumber(invoice_jaxb));
		        invoice.setSaNumber(getSaNumber(invoice_jaxb));
		        invoice.setCompanyCode(getCompanyCode(invoice_jaxb));
		        invoice.setInvoiceStatus(getInvoiceStatus(invoice_jaxb));
		        invoice.setException(new Boolean(getException(invoice_jaxb)));
		        invoice.setCreateId(getCreateId(invoice_jaxb));		        
		        invoice.setTotalInvoiceAmount(new BigDecimal(getTotalInvoiceAmount(invoice_jaxb))); 		        
		        invoice.setTotalTaxAmount(new BigDecimal(getTotalTaxAmount(invoice_jaxb)));
		        invoice.setAvailableUnitAmount(new BigDecimal(getAvailableUnitAmount(invoice_jaxb)));
		        invoice.setAvailableTaxAmount(new BigDecimal(getAvailableTaxAmount(invoice_jaxb)));
		        invoice.setExchangeRate(new BigDecimal(Double.toString(getExchangeRate(invoice_jaxb))));
		        invoice.setCurrency(getCurrency(invoice_jaxb));
		        invoice.setDistVendorNumber(getDistVendorNumber(invoice_jaxb));
		        invoice.setDebitCreditIndicator(getDebitCreditIndicator(invoice_jaxb));
		        invoice.setInvoiceSource(getInvoiceSource(invoice_jaxb));
		        invoice.setVendorNumber(getVendorNumber(invoice_jaxb));
		        invoice.setVendorName(getVendorName(invoice_jaxb));
		        invoice.setOcrKidReference(getOcrKidReference(invoice_jaxb));
		        invoice.setLastUpdateId(getLastUpdateId(invoice_jaxb));
		        invoice.setLastUpdateTimestamp(new Date(getLastUpdateTimeStamp(invoice_jaxb)));
		        invoice.setUrgentInvoiceIndc(new Boolean(getUrgentIndicator(invoice_jaxb)));
		        invoice.setVarianceReason(getVarianceReason(invoice_jaxb));
		        invoice.setSourceIndc(getSourceIndc(invoice_jaxb));
		        invoice.setAutoCreateCoaIndc(getAutoCreateCoaIndc(invoice_jaxb));
			      //Story 1750051 CA GIL changes   
		        invoice.setProvinceCode((getProvinceCode(invoice_jaxb)));
		      //End Story 1750051 CA GIL changes  
		        
		        // invoice detail lines.
				detailItr = invoice_jaxb.getLine().iterator();
				while (detailItr.hasNext()) {
				    Object obj2 = detailItr.next();
				    if (obj2 instanceof InvoiceLine) {
				    	InvoiceLine detail_jaxb = (InvoiceLine) obj2;
				        InvoiceDetail invoiceDet = getInvoiceLine(detail_jaxb, 
				                getCostCenter(invoice_jaxb));
				        invoice.addInvoiceDetail(invoiceDet);
				    }
				}
				
		        // addresses.
				invoice.setBillToAddress(getBillToAddress(invoice_jaxb));
				invoice.setInstallToAddress(getInstallToAddress(invoice_jaxb));
				invoice.setShipToAddress(getShipToAddress(invoice_jaxb));
				invoice.setSoldToAddress(getSoldToAddress(invoice_jaxb));
				
				// invoice comments.
				commentItr = invoice_jaxb.getHeader().getNote().iterator();
				while (commentItr.hasNext()) {
				    Object obj2 = commentItr.next();
				    if (obj2 instanceof Note) {
				        Note note = (Note) obj2;
				        InvoiceComment invComment = getInvoiceComment(note);
				        invoice.addInvComment(invComment);
				    }
				}
				
				invoices.add(invoice);
		    }
		}
		
		return invoices;
	}

	/**
	 * Get invoice detail line from jaxb object.
	 * 
	 * @param line_jaxb ImsLineElement
	 * @param costCenter String
	 * 
	 * @return InvoiceDetail
	 */
	private InvoiceDetail getInvoiceLine(InvoiceLine line_jaxb,
	        String costCenter) {
	    InvoiceDetail invoiceDet = new InvoiceDetail_jaxb();
	    
	    Iterator<AmountPerQuantity> unitPriceItr = null;
	    Iterator<AmountPerQuantity> unitDiscountItr = null;
	    Iterator<Tax> unitTaxItr = null;
	    Iterator<Id> mfgPartNumItr = null;
	    Iterator<Id> machineTypeItr = null;
	    Iterator<Id> machineModelItr = null;
	    Iterator<Description> itemDescItr = null;
	    Iterator<?> usedInConfigItr = null;
	    Iterator<Tax> taxCodeItr = null;
	    Iterator<?> purchaseOrderItr = null;
	    Iterator<ItemCategoryId> itemTypeItr = null;
	    Iterator<AssetId> assetTagItr = null;
	    Iterator<?> mesNumItr = null;
	    Iterator<AssetId> serialNumItr = null;
	    Iterator<org.openapplications.oagis._8_2.showinvoice.Status> lastUpdateTmspItr = null;
	    Iterator<?> quoteItemNumItr = null;
	    Iterator<ItemCategoryId> prodCatItr = null;
	    Iterator<ItemCategoryId> prodClassItr = null;
	    
	    // detail line number.
	    if (line_jaxb.getLineNumber() != null) {
	        invoiceDet.setLineNumber(line_jaxb.getLineNumber().trim());
	    }
	    // detail line sequence number.
	    if (line_jaxb.getLineSeq() != null) {
	        invoiceDet.setLineSequenceNumber(line_jaxb.getLineSeq().trim());
	    }
	    // unit price.
	    unitPriceItr = line_jaxb.getUnitPrice().iterator();
	    while (unitPriceItr.hasNext()) {
	        Object obj = unitPriceItr.next();
	        if (obj instanceof AmountPerQuantity) {
	            AmountPerQuantity unitPriceAmountPerQuantity = (AmountPerQuantity) obj;
	            if (unitPriceAmountPerQuantity.getAmount().getBasis()
	                    .equalsIgnoreCase("ListPrice")) {
	                invoiceDet.setUnitPrice(unitPriceAmountPerQuantity.getAmount()
	                        .getValue());
	            }
	        }
	    }
	    // unit discount.
	    unitDiscountItr = line_jaxb.getUnitPrice().iterator();
	    while (unitDiscountItr.hasNext()) {
	        Object obj = unitDiscountItr.next();
	        if (obj instanceof AmountPerQuantity) {
	            AmountPerQuantity unitDiscountAmountPerQuantity = (AmountPerQuantity) obj;
	            if (unitDiscountAmountPerQuantity.getAmount().getBasis()
	                    .equalsIgnoreCase("DiscountAmount")) {
	                invoiceDet.setUnitDiscount(unitDiscountAmountPerQuantity.getAmount()
	                        .getValue());
	            }
	        }
	    }
	    // unit tax.
	    unitTaxItr = line_jaxb.getTax().iterator();
	    while (unitTaxItr.hasNext()) {
	        Object obj = unitTaxItr.next();
	        if (obj instanceof Tax) {
	            Tax unitTax = (Tax) obj;
	            if (unitTax.getTaxAmount().getBasis()
	                    .equalsIgnoreCase("unit_tax")) {
	                invoiceDet.setUnitTax(unitTax.getTaxAmount().getValue());
	            }
	        }
	    }
	    // manufacturing part number.
	    mfgPartNumItr = line_jaxb.getOrderItem().getItemIds().getItemId().getId().iterator();
	    while (mfgPartNumItr.hasNext()) {
	        Object obj = mfgPartNumItr.next();
	        if (obj instanceof Id) {
	            Id mfgPartNumId = (Id) obj;
	            if (mfgPartNumId.getIdOrigin().equalsIgnoreCase("Vendor")) {
	                if (mfgPartNumId.getValue() != null) {
	                    invoiceDet.setManufacturerPartNumber(mfgPartNumId.getValue().trim());
	                }
	            }
	        }
	    }
	    // machine type.
	    machineTypeItr = line_jaxb.getOrderItem().getItemIds().getItemId().getId().iterator();
	    while (machineTypeItr.hasNext()) {
	        Object obj = machineTypeItr.next();
	        if (obj instanceof Id) {
	            Id machineTypeId = (Id) obj;
	            if (machineTypeId.getIdOrigin().equalsIgnoreCase("IBM/MachineType")) {
	                if (machineTypeId.getValue() != null) {
	                    invoiceDet.setMachineType(machineTypeId.getValue().trim());
	                }
	            }
	        }
	    }
	    // machine model.
	    machineModelItr = line_jaxb.getOrderItem().getItemIds().getItemId().getId().iterator();
	    while (machineModelItr.hasNext()) {
	        Object obj = machineModelItr.next();
	        if (obj instanceof Id) {
	            Id machineModelId = (Id)obj;
	            if (machineModelId.getIdOrigin().equalsIgnoreCase("IBM/Model")) {
	                if (machineModelId.getValue() != null) {
	                    invoiceDet.setMachineModel(machineModelId.getValue().trim());
	                }
	            }
	        }
	    }
	    // item description.
	    itemDescItr = line_jaxb.getOrderItem().getDescription().iterator();
	    while (itemDescItr.hasNext()) {
	        Object obj = itemDescItr.next();
	        if (obj instanceof Description) {
	            Description itemDescription = (Description)obj;
	            if (itemDescription.getValue() != null) {
	                invoiceDet.setItemDescription(itemDescription.getValue().trim());
	            }
	        }
	    }
	    // used in configuration flag.
	    usedInConfigItr = line_jaxb.getDocumentReferences().getDocumentReference().iterator();
	    while (usedInConfigItr.hasNext()) {
	        Object obj = usedInConfigItr.next();
	        if (obj instanceof JAXBElement) {
	        	if(((JAXBElement<?>) obj).getName().getLocalPart().equals("InvoiceDocumentReference")){
	        		OrderDocumentReference usedInConfigInvDocRef =
	                	(OrderDocumentReference) ((JAXBElement<?>) obj).getValue();
	            if (usedInConfigInvDocRef.getStatus().getCode().equalsIgnoreCase("true") ||
	                usedInConfigInvDocRef.getStatus().getCode().equalsIgnoreCase("Y")) {
		            invoiceDet.setInConfiguration(new Boolean(true));
	            } else {
		            invoiceDet.setInConfiguration(new Boolean(false));
	            }	
	        	}
	        	
	        }
	    }
	    // tax code.
	    taxCodeItr = line_jaxb.getTax().iterator();
	    while (taxCodeItr.hasNext()) {
	        Object obj = taxCodeItr.next();
	        if (obj instanceof Tax) {
	            Tax taxCodeTax = (Tax) obj;
	            if (taxCodeTax.getTaxCode().getValue() != null) {
	                invoiceDet.setTaxCode(taxCodeTax.getTaxCode().getValue().trim());
	            }
	        }
	    }
	    // cost center.
	    invoiceDet.setCostCenter(costCenter);
	    // purchase order number.
	    purchaseOrderItr = line_jaxb.getDocumentReferences().getDocumentReference().iterator();
	    while (purchaseOrderItr.hasNext()) {
	        Object obj = purchaseOrderItr.next();
	        if (obj instanceof JAXBElement) {
	        	if(((JAXBElement<?>) obj).getName().getLocalPart().equals("PurchaseOrderDocumentReference")){
	        		OrderDocumentReference purchaseOrderDocRef = 
	                	(OrderDocumentReference) ((JAXBElement<?>)obj).getValue();
	            Iterator<?> purchaseOrderDocIdsItr = purchaseOrderDocRef.getDocumentIds()
	            		.getDocumentIdType().iterator();
	            while (purchaseOrderDocIdsItr.hasNext()) {
	                Object obj2 = purchaseOrderDocIdsItr.next();
	                if (obj2 instanceof JAXBElement) {
	                	if(((JAXBElement<?>) obj2).getName().getLocalPart().equals("SupplierDocumentId")){
	                		PartyDocumentId purchaseOrderDocId = 
	                        	(PartyDocumentId) ((JAXBElement<?>)obj2).getValue();
	                    if (purchaseOrderDocId.getId().getValue() != null
								&& purchaseOrderDocId.getId().getIdOrigin() != null
								&& purchaseOrderDocId.getId().getIdOrigin()
										.trim().equalsIgnoreCase("Supplier")) {
	                        invoiceDet.setPurchaseOrderNumber(purchaseOrderDocId.getId().getValue().trim());
	                    }
	                	}
	                	
	                }
	            }	
	        	}
	        	
	        }
	    }
	    // invoice line item type.
	    itemTypeItr = line_jaxb.getOrderItem().getItemCategoryId().iterator();
	    while (itemTypeItr.hasNext()) {
	        Object obj = itemTypeItr.next();
	        if (obj instanceof ItemCategoryId) {
	            ItemCategoryId itemTypeItemCatId = (ItemCategoryId) obj;
	            if (itemTypeItemCatId.getType().equalsIgnoreCase("ItemType")) {
	                if (itemTypeItemCatId.getValue() != null) {
	                    invoiceDet.setInvoiceLineItemType(itemTypeItemCatId.getValue().trim());
	                }
	            }
	        }
	    }
	    // asset tag.
	    assetTagItr = line_jaxb.getOrderItem().getAssetId().iterator();
	    while (assetTagItr.hasNext()) {
	        Object obj = assetTagItr.next();
	        if (obj instanceof AssetId) {
	        	AssetId assetTagAssetId = (AssetId) obj;
	            Iterator<Id> idItr = assetTagAssetId.getId().iterator();
	            while (idItr.hasNext()) {
	                Object obj2 = idItr.next();
	                if (obj2 instanceof Id) {
	                    Id assetTagId = (Id) obj2;
	                	if (assetTagId.getIdOrigin().equalsIgnoreCase("Supplier")) {
	                	    if (assetTagId.getValue() != null) {
	                	        invoiceDet.setPartTag(assetTagId.getValue().trim());
	                	    }
	                	}
	                }
	            }
	        }
	    }
	    // mes number.
	    mesNumItr = line_jaxb.getDocumentReferences().getDocumentReference().iterator();
	    while (mesNumItr.hasNext()) {
	        Object obj = mesNumItr.next();
	        if (obj instanceof JAXBElement) {
	        	if(((JAXBElement<?>) obj).getName().getLocalPart().equals("PurchaseOrderDocumentReference")){
	        		OrderDocumentReference mesNumDocRef =
		                (OrderDocumentReference) ((JAXBElement<?>)obj).getValue();
		            Iterator<?> mesNumDocIdsItr = mesNumDocRef.getDocumentIds()
		            	.getDocumentIdType().iterator();
		            while (mesNumDocIdsItr.hasNext()) {
		                Object obj2 = mesNumDocIdsItr.next();
		                if (obj2 instanceof JAXBElement) {
		                	if(((JAXBElement<?>) obj2).getName().getLocalPart().equals("SupplierDocumentId")){
		                		PartyDocumentId mesNumDocId =
			                        (PartyDocumentId) ((JAXBElement<?>)obj2).getValue();
			                    if (mesNumDocId.getId().getIdOrigin().equalsIgnoreCase("IBM/MES")) {
			                        if (mesNumDocId.getId().getValue() != null) {
			                            invoiceDet.setMesNumber(mesNumDocId.getId().getValue().trim());
			                        }
			                    }	
		                	}
		                	
		                }
		            }
	        	}
	        	
	        }
	    }
	    // serial number.
	    serialNumItr = line_jaxb.getOrderItem().getAssetId().iterator();
	    while (serialNumItr.hasNext()) {
	        Object obj = serialNumItr.next();
	        if (obj instanceof AssetId) {
	        	AssetId serialNumAssetId = (AssetId) obj;
	            if (serialNumAssetId.getSerialNumber() != null) {
	                invoiceDet.setSerialNumber(serialNumAssetId.getSerialNumber().trim());
	            }
	        }
	    }
	    // last update timestamp.
	    lastUpdateTmspItr = line_jaxb.getOrderStatus().getAcknowledgementDetail().iterator();
	    while (lastUpdateTmspItr.hasNext()) {
	        Object obj = lastUpdateTmspItr.next();
	        if (obj instanceof Status) {
		        org.openapplications.oagis._8_2.showinvoice.Status lastUpdateTmspStatus = (org.openapplications.oagis._8_2.showinvoice.Status) obj;
		        Iterator<StateChange> lastUpdateTmspStatusItr = lastUpdateTmspStatus.getChange().iterator();
		        while (lastUpdateTmspStatusItr.hasNext()) {
		            Object obj2 = lastUpdateTmspStatusItr.next();
		            if (obj2 instanceof StateChange) {
		                StateChange lastUpdatedTmspStateChange = (StateChange) obj2;
		                invoiceDet.setLastUpdateTimestamp(new Date(lastUpdatedTmspStateChange
		                        .getChangeDate().toGregorianCalendar().getTimeInMillis()));
		            }
		        }
	        }
	    }
	    // quantity.
	    invoiceDet.setQuantity(new Integer(line_jaxb.getItemQuantity().getValue().intValue()));
	    // quote item number.
	    quoteItemNumItr = line_jaxb.getDocumentReferences().getDocumentReference().iterator();
	    while (quoteItemNumItr.hasNext()) {
	        Object obj = quoteItemNumItr.next();
	        if (obj instanceof JAXBElement) {
	        	if(((JAXBElement<?>) obj).getName().getLocalPart().equals("QuoteDocumentReference")){
	        		OrderDocumentReference quoteItemNumDocRef =
	                	(OrderDocumentReference)((JAXBElement<?>) obj).getValue();
	            if (quoteItemNumDocRef.getLineNumber() != null) {
	                invoiceDet.setQuoteItemNum(quoteItemNumDocRef.getLineNumber().toString());
	            }
	        	}
	        	
	        }
	    }
	    // financing type.
	    if (line_jaxb.getFinanceDesired() != null) {
	        invoiceDet.setFinancingType(line_jaxb.getFinanceDesired().trim());
	    }
	    // term.
	    if (line_jaxb.getLeaseTerms().getPeriodTime().getDuration() != null) {
	        invoiceDet.setTerm(line_jaxb.getLeaseTerms().getPeriodTime().getDuration().trim());
	    }
	    // mes indicator.
	    if (line_jaxb.getOrderItem().getUpgradeOrMES().equalsIgnoreCase("MES")) {
	        invoiceDet.setMesIndc(new Boolean(true));
	    } else {
	        invoiceDet.setMesIndc(new Boolean(false));
	    }
	    
	    if(line_jaxb.getOrderItem().getEquipmentSource()!=null){
	    	invoiceDet.setEquipmentSource(line_jaxb.getOrderItem().getEquipmentSource());	
	    }
	    
	    prodCatItr = line_jaxb.getOrderItem().getItemCategoryId().iterator();
	    while (prodCatItr.hasNext()) {
	        Object obj = prodCatItr.next();
	        if (obj instanceof ItemCategoryId) {
	            ItemCategoryId prodCatItemCatId = (ItemCategoryId) obj;
	            if (prodCatItemCatId.getType().equalsIgnoreCase("ProductCategory")) {
	                if (prodCatItemCatId.getValue() != null) {
	                    invoiceDet.setProdCat(prodCatItemCatId.getValue().trim());
	                }
	            }
	        }
	    }
	    // product class.
	    prodClassItr = line_jaxb.getOrderItem().getItemCategoryId().iterator();
	    while (prodClassItr.hasNext()) {
	        Object obj = prodClassItr.next();
	        if (obj instanceof ItemCategoryId) {
	            ItemCategoryId prodClassItemCatId = (ItemCategoryId) obj;
	            if (prodClassItemCatId.getType().equalsIgnoreCase("ProductClass")) {
	                if (prodClassItemCatId.getValue() != null) {
	                    invoiceDet.setProdClass(prodClassItemCatId.getValue().trim());
	                }
	            }
	        }
	    }
	    
	    return invoiceDet;
	}
	
	/**
	 * Get invoice comment.
	 * 
	 * @param note Note
	 * 
	 * @return InvoiceComment
	 */
	private InvoiceComment getInvoiceComment(Note note) {
	    InvoiceComment invComment = new InvoiceComment_jaxb();
	    
	    // comment sequence number.
	    if (note.getId() != null) {
	        invComment.setCommentSequenceNumber(note.getId().trim());
	    }
	    // comment type.
	    if (note.getType() != null) {
	        invComment.setCommentType(note.getType().trim());
	    }
	    // comment
	    if (note.getValue() != null) {
	        invComment.setCommentDescription(note.getValue().trim());
	    }
	    // comment create id.
	    if (note.getAuthor() != null) {
	        invComment.setCreateId(note.getAuthor().trim());
	    }
	    // comment create timestamp.
	    invComment.setCreateTimestamp(new Date(note.getEntryDateTime().toGregorianCalendar().getTimeInMillis()));
	    
	    return invComment;
	}
	
	/**
	 * Set component.
	 *
	 * @param component String
	 */
	public void setComponent(String component) {
		showInvoiceRetrieved.getApplicationArea().getSender().setComponent(
			component);
	}

	/**
	 * Set creation date time.
	 *
	 * @param creationDateTime long
	 * @throws DatatypeConfigurationException 
	 */
	public void setCreationDateTime(long creationDateTime) {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.clear();
		calendar.setTimeInMillis(creationDateTime);
		XMLGregorianCalendar xmlGregorianCalendar = null;
		try {
			xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
		} catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		showInvoiceRetrieved.getApplicationArea().setCreationDateTime(xmlGregorianCalendar);
	}

	/**
	 * Set bod id.
	 *
	 * @param bodId String
	 */
	public void setBodId(String bodId) {
		showInvoiceRetrieved.getApplicationArea().setBODId(bodId);
	}

	


	/**
	 * getAvailableUnitAmount method.
	 * 
	 * @param invoice_jaxb ImsInvoiceElement
	 * 
	 * @return double
	 */
	private double getAvailableUnitAmount(com.ibm.xmlns.ims._1_3.showinvoice.Invoice invoice_jaxb) { 
		double totalInvoiceAmt = 0;
		
		Iterator<Amount> totalAmtItr = invoice_jaxb.getHeader().getTotalAmount().iterator();
	    while (totalAmtItr.hasNext()) {
	        Object obj = totalAmtItr.next();
	        if (obj instanceof Amount) {
	            Amount amt = (Amount) obj;
	            if (amt.getBasis().equalsIgnoreCase("AvailableUnitAmount")) {
	                totalInvoiceAmt = amt.getValue().doubleValue();
	            }
	        }
	    }
		
		return totalInvoiceAmt;
	}

	/**
	 * getAvailableTaxAmount method.
	 * 
	 * @param invoice_jaxb  
	 * 
	 * @return double
	 */
	private double getAvailableTaxAmount(com.ibm.xmlns.ims._1_3.showinvoice.Invoice invoice_jaxb) { 
		double availableTaxAmt = 0;
		
	    Iterator<Amount> totalTaxItr = invoice_jaxb.getHeader().getTotalTax().iterator();
	    while (totalTaxItr.hasNext()) {
	        Object obj = totalTaxItr.next();
	        if (obj instanceof Amount) {
	            Amount amount = (Amount) obj;
	            if (amount.getBasis().equalsIgnoreCase("AvailableTax")) {
	                availableTaxAmt = amount.getValue().doubleValue();
	            }
	        }
	    }
		
		return availableTaxAmt;
	}

	/**
	 * getCmObjectId method.
	 * 
	 * @param invoice_jaxb  
	 * 
	 * @return String
	 */
	private String getCmObjectId(com.ibm.xmlns.ims._1_3.showinvoice.Invoice invoice_jaxb){   
	    String cmObjectId = null;
	    
	    Iterator<?> docRefItr = invoice_jaxb.getHeader().getDocumentReferences()
	    		.getDocumentReference().iterator();
	    while (docRefItr.hasNext()) {
	        Object obj = docRefItr.next();
	        if (obj instanceof JAXBElement) {
	        	if(((JAXBElement<?>) obj).getName().getLocalPart().equals("InvoiceDocumentReference")){
	        		OrderDocumentReference invDocRef = 
	                	(OrderDocumentReference) ((JAXBElement<?>)obj).getValue();
	            Iterator<?> docIdItr = invDocRef.getDocumentIds().getDocumentIdType().iterator();
	            while (docIdItr.hasNext()) {
	                Object obj2 = docIdItr.next();
	                if (obj2 instanceof JAXBElement) {
	                	if(((JAXBElement<?>) obj2).getName().getLocalPart().equals("DocumentIdType")){
	                		DocumentIdType documentId = (DocumentIdType) ((JAXBElement<?>)obj2).getValue();
		                    if (documentId.getId().getIdOrigin().equalsIgnoreCase("ContentManager")) {
		                        if (documentId.getId().getValue() != null) {
		                            cmObjectId = documentId.getId().getValue().trim();
		                        }
		                    }
	                	}
	                	
	                }
	            }	
	        	}
	        	
	        }
	    }
	    
	    return cmObjectId;
	}

	/**
	 * getCoaInvoiceIndicator method.
	 * 
	 * @param invoice_jaxb  
	 * 
	 * @return boolean
	 */
	private boolean getCoaInvoiceIndicator(com.ibm.xmlns.ims._1_3.showinvoice.Invoice invoice_jaxb) { 	
		boolean coaInvIndc = false;
		
		Iterator<org.openapplications.oagis._8_2.showinvoice.Status> ackDetItr = invoice_jaxb.getHeader().getOrderStatus()
				.getAcknowledgementDetail().iterator();
		while (ackDetItr.hasNext()) {
		    Object obj = ackDetItr.next();
		    
		    if (obj instanceof org.openapplications.oagis._8_2.showinvoice.Status) {
		        org.openapplications.oagis._8_2.showinvoice.Status status = (org.openapplications.oagis._8_2.showinvoice.Status) obj;
		        Iterator<Description> descItr = status.getDescription().iterator();
		        while (descItr.hasNext()) {
		            Object obj2 = descItr.next();
		            if (obj2 instanceof Description) {
		                Description desc = (Description) obj2;
		                if (desc.getValue().equalsIgnoreCase("invoice_header-coa_with_invoice_indc")) {
		                    if (status.getCode().equalsIgnoreCase("Y")) {
		                        coaInvIndc = true;
		                    }
		                }
		            }
		        }
		    }
		}
		
		return coaInvIndc;
	}

	/**
	 * getCompanyCode method.
	 * 
	 * @param invoice_jaxb  
	 * 
	 * @return String
	 */
	private String getCompanyCode(Invoice invoice_jaxb) { 
	    String companyCode = null;
	    
	    Iterator<?> partiesItr = invoice_jaxb.getHeader().getParties().getPartyType().iterator();
	    while (partiesItr.hasNext()) {
	        Object obj = partiesItr.next();
	        if (obj instanceof JAXBElement) {
	        	if(((JAXBElement<?>) obj).getName().getLocalPart().equals("ShipToParty")){
	        		PartyInstitutional shipToParty = (PartyInstitutional) ((JAXBElement<?>)obj).getValue();
		            Iterator<PartyId> partyIdItr = shipToParty.getPartyId().getId().iterator();
		            while (partyIdItr.hasNext()) {
		                Object obj2 = partyIdItr.next();
		                if (obj2 instanceof PartyId) {
		                    PartyId partyId = (PartyId) obj2;
		                    if (partyId.getIdOrigin().equalsIgnoreCase("IBM")) {
		                        if (partyId.getValue() != null) {
		                            companyCode = partyId.getValue().trim();
		                        }
		                    }
		                }
		            }
	        	}
	        	
	        }
	    }
	    
	    return companyCode;
	}

	/**
	 * getCostCenter method.
	 * 
	 * @param invoice_jaxb  
	 * 
	 * @return String
	 */
	private String getCostCenter(com.ibm.xmlns.ims._1_3.showinvoice.Invoice invoice_jaxb) {
	    String costCenter = null;
	    
	    Iterator<?> partiesItr = invoice_jaxb.getHeader().getParties().getPartyType().iterator();
	    while (partiesItr.hasNext()) {
	        Object obj = partiesItr.next();
	        if (obj instanceof JAXBElement) {
	        	if(((JAXBElement<?>) obj).getName().getLocalPart().equals("ShipToParty")){
	        		PartyInstitutional shipToParty = (PartyInstitutional) ((JAXBElement<?>)obj).getValue();
		            if (shipToParty.getBusiness() != null &&
		            	shipToParty.getBusiness().getRelatedUnit() != null) {
			            Iterator<RelatedUnitType> relatedUnitItr = shipToParty.getBusiness().getRelatedUnit().iterator();
			            while (relatedUnitItr.hasNext()) {
			                Object obj2 = relatedUnitItr.next();
			                if (obj2 instanceof RelatedUnitType) {
			                    RelatedUnitType relatedUnitType = (RelatedUnitType) obj2;
			                    if (relatedUnitType.getRelationship().equalsIgnoreCase("CostCenter")) {
			                        if (relatedUnitType.getUnit().getId() != null) {
			                            costCenter = relatedUnitType.getUnit().getId().trim();
			                        }
			                    }
			                }
			            }
		            }	
	        	}
	        	
	        }
	    }
	    
	    return costCenter;
	}
	
	/**
	 * getCountryCode method.
	 * 
	 * @param invoice_jaxb  
	 * 
	 * @return String
	 */
	private String getCountryCode(com.ibm.xmlns.ims._1_3.showinvoice.Invoice invoice_jaxb) {
	    String countryCode = null;
	    
	    Iterator<?> partiesItr = invoice_jaxb.getHeader().getParties().getPartyType().iterator();
	    while (partiesItr.hasNext()) {
	        Object obj = partiesItr.next();
	        if (obj instanceof JAXBElement) {
	        	if(((JAXBElement<?>) obj).getName().getLocalPart().equals("SupplierParty")){
	        		PartyInstitutional supplierParty = (PartyInstitutional)((JAXBElement<?>) obj).getValue();
		            Iterator<?> addressesItr = supplierParty.getAddresses().getAddress().iterator();
		            while (addressesItr.hasNext()) {
		                Object obj2 = addressesItr.next();
		                if (obj2 instanceof JAXBElement) {
		                	if(((JAXBElement<?>) obj2).getName().getLocalPart().equals("PrimaryAddress")){
		                		Address primaryAddress = (Address)((JAXBElement<?>) obj2).getValue();
			                    if (primaryAddress.getCountry().getValue() != null) {
			                        countryCode = primaryAddress.getCountry().getValue().trim();
			                    }
		
		                	}
		                }
		            }	
	        	}
	        	
	        }
	    }
	    
	    return countryCode;
	}
	
	/**
	 * getCreateId method.
	 * 
	 * @param invoice_jaxb  
	 * 
	 * @return String
	 */
	private String getCreateId(com.ibm.xmlns.ims._1_3.showinvoice.Invoice invoice_jaxb) {  
	    String createId = null;
	    
	    Iterator<?> partiesItr = invoice_jaxb.getHeader().getParties().getPartyType().iterator();
	    while (partiesItr.hasNext()) {
	        Object obj = partiesItr.next();
	        if (obj instanceof JAXBElement) {
	        	if(((JAXBElement<?>) obj).getName().getLocalPart().equals("EmployeeParty")){
	        		PartyInstitutional employeeParty = (PartyInstitutional) ((JAXBElement<?>)obj).getValue();
		            Iterator<Contact> contactsItr = employeeParty.getContacts().getContactAbs().iterator();
		            while (contactsItr.hasNext()) {
		                Object obj2 = contactsItr.next();
		                if (obj2 instanceof Contact) {
		                	Contact contactAbs = (Contact) obj2;
		                    if (contactAbs.getJobTitle().equalsIgnoreCase("Indexer-Creator")) {
			                    Iterator<Contact.EMailAddress> emailItr = contactAbs.getEMailAddress().iterator();
			                    while (emailItr.hasNext()) {
			                        Object obj3 = emailItr.next();
			                        if (obj3 instanceof Contact.EMailAddress) {
			                            Contact.EMailAddress emailAddress = 
			                                (Contact.EMailAddress) obj3;
			                            if (emailAddress.getValue() != null) {
			                                createId = emailAddress.getValue().trim();
			                            }
			                        }
			                    }
		                    }
		                }
		            }
	        	}
	        	
	        }
	    }
	    
	    return createId;
	}

	/**
	 * getCreateTimestamp method.
	 * 
	 * @param invoice_jaxb  
	 * 
	 * @return long
	 */
	private long getCreateTimestamp(com.ibm.xmlns.ims._1_3.showinvoice.Invoice invoice_jaxb) {
	    long createTimestamp = 0;
	    
	    // TODO add create timestamp
	    
	    return createTimestamp;
	}
	
	/**
	 * getCurrency method.
	 * 
	 * @param invoice_jaxb  
	 * 
	 * @return String
	 */
	private String getCurrency(com.ibm.xmlns.ims._1_3.showinvoice.Invoice invoice_jaxb) {  
		String currency = null;
		
		Iterator<Amount> totalAmtItr = invoice_jaxb.getHeader().getTotalAmount().iterator();
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
	 * getDebitCreditIndicator method.
	 * 
	 * @param invoice_jaxb  
	 * 
	 * @return String
	 */
	private String getDebitCreditIndicator(com.ibm.xmlns.ims._1_3.showinvoice.Invoice invoice_jaxb) {
		String debitCreditIndicator = null;
		
		Iterator<Amount> totalAmtItr = invoice_jaxb.getHeader().getTotalAmount().iterator();
	    while (totalAmtItr.hasNext()) {
	        Object obj = totalAmtItr.next();
	        if (obj instanceof Amount) {
	            Amount amt = (Amount) obj;
	            if (amt.getBasis().equalsIgnoreCase("TotalAmount")) {
	                if (amt.getType() != null) {
	                    debitCreditIndicator = amt.getType().trim();
	                }
	            }
	        }
	    }
		
		return debitCreditIndicator;
	}

	/** 
	 * getException method.
	 * 
	 * @param invoice_jaxb  
	 * 
	 * @return boolean
	 */
	private boolean getException(com.ibm.xmlns.ims._1_3.showinvoice.Invoice invoice_jaxb) {
		boolean exception = false;
		
		Iterator<org.openapplications.oagis._8_2.showinvoice.Status> ackDetItr = invoice_jaxb.getHeader().getOrderStatus()
				.getAcknowledgementDetail().iterator();
		while (ackDetItr.hasNext()) {
		    Object obj = ackDetItr.next();
		    if (obj instanceof org.openapplications.oagis._8_2.showinvoice.Status) {
		        org.openapplications.oagis._8_2.showinvoice.Status status = (org.openapplications.oagis._8_2.showinvoice.Status) obj;
		        Iterator<Description> descItr = status.getDescription().iterator();
		        while (descItr.hasNext()) {
		            Object obj2 = descItr.next();
		            if (obj2 instanceof Description) {
		                Description desc = (Description) obj2;
		                if (desc.getValue().equalsIgnoreCase("invoice_header-exception_ind")) {
		                    if (status.getCode().equalsIgnoreCase("Y")) {
		                        exception = true;
		                    }
		                }
		            }
		        }
		    }
		}
		
		return exception;
	}

	/**
	 * getExchangeRate method.
	 * 
	 * @param invoice_jaxb  
	 * 
	 * @return double
	 */
	private double getExchangeRate(com.ibm.xmlns.ims._1_3.showinvoice.Invoice invoice_jaxb) {  
		double exchangeRate = 0;
	    
		Iterator<CurrencyConversion> currConvItr = invoice_jaxb.getHeader().getCurrencyConversion().iterator();
		while (currConvItr.hasNext()) {
	        Object obj = currConvItr.next();
	        if (obj instanceof CurrencyConversion) {
	        	CurrencyConversion currConv = (CurrencyConversion) obj;
	            Iterator<FunctionalAmount> convertedItr = currConv.getConverted().iterator();
	            while (convertedItr.hasNext()) {
	                Object obj2 = convertedItr.next();
	                if (obj2 instanceof FunctionalAmount) {
	                    FunctionalAmount funcAmt = (FunctionalAmount) obj2;
	                    exchangeRate = funcAmt.getConversionFactor().doubleValue();
	                }
	            }
	        }
		}
	    
	    return exchangeRate;
	}

	/**
	 * getInvoiceDate method.
	 * 
	 * @param invoice_jaxb  
	 * 
	 * @return long
	 */
	private long getInvoiceDate(com.ibm.xmlns.ims._1_3.showinvoice.Invoice invoice_jaxb) {   
		return invoice_jaxb.getHeader().getDocumentDateTime().toGregorianCalendar().getTimeInMillis();
	}

	/**
	 * getInvoiceNumber method.
	 * 
	 * @param invoice_jaxb  
	 * 
	 * @return String
	 */
	private String getInvoiceNumber(com.ibm.xmlns.ims._1_3.showinvoice.Invoice invoice_jaxb){   
	    String invoiceNum = null;
		
	    Iterator<?> documentIdsItr = invoice_jaxb.getHeader().getDocumentIds()
	    		.getDocumentIdType().iterator();
	    while (documentIdsItr.hasNext()) {
	        Object obj = documentIdsItr.next();
	        if (obj instanceof JAXBElement) {
	        	if(((JAXBElement<?>) obj).getName().getLocalPart().equals("SupplierDocumentId")){
	        		PartyDocumentId supplierDocumentId = (PartyDocumentId)((JAXBElement<?>) obj).getValue();
		            if (supplierDocumentId.getId().getIdOrigin().equalsIgnoreCase("IBM/Invoice")) {
		                if (supplierDocumentId.getId().getValue() != null) {
		                    invoiceNum = supplierDocumentId.getId().getValue().trim();
		                }
		            }
	        	}
	        	
	        }
	    }
	    
		return invoiceNum;
	}

	/**
	 * getInvoiceReceiptDate method.
	 * 
	 * @param invoice_jaxb  
	 * 
	 * @return long
	 */
	private long getInvoiceReceiptDate(com.ibm.xmlns.ims._1_3.showinvoice.Invoice invoice_jaxb) {  
	    long invoiceReceiptDate = 0;
	    
	    Iterator<?> docRefItr = invoice_jaxb.getHeader().getDocumentReferences()
				.getDocumentReference().iterator();
		while (docRefItr.hasNext()) {
		    Object obj = docRefItr.next();
		    if (obj instanceof JAXBElement) {
		    	if(((JAXBElement<?>) obj).getName().getLocalPart().equals("InvoiceDocumentReference")){
		    		OrderDocumentReference invDocRef = 
		            	(OrderDocumentReference) ((JAXBElement<?>)obj).getValue();
		        invoiceReceiptDate = invDocRef.getDocumentDate().toGregorianCalendar().getTimeInMillis();
		    	}
		    	
		    }
		}
	    
	    return invoiceReceiptDate;
	}

	/**
	 * getInvoiceSequenceNumber method.
	 * 
	 * @param invoice_jaxb  
	 * 
	 * @return String
	 */
	private String getInvoiceSequenceNumber(com.ibm.xmlns.ims._1_3.showinvoice.Invoice invoice_jaxb){   
	    String invoiceSeqNum = null;
	    
	    Iterator<?> docRefItr = invoice_jaxb.getHeader().getDocumentReferences()
	    		.getDocumentReference().iterator();
	    while (docRefItr.hasNext()) {
	        Object obj = docRefItr.next();
	        if (obj instanceof JAXBElement) {
	        	if(((JAXBElement<?>) obj).getName().getLocalPart().equals("InvoiceDocumentReference")){
	        		OrderDocumentReference invDocRef = 
	                	(OrderDocumentReference) ((JAXBElement<?>)obj).getValue();
	            Iterator<?> docIdItr = invDocRef.getDocumentIds().getDocumentIdType().iterator();
	            while (docIdItr.hasNext()) {
	                Object obj2 = docIdItr.next();
	                if (obj2 instanceof JAXBElement) {
	                	DocumentIdType documentId = (DocumentIdType) ((JAXBElement<?>)obj2).getValue();
		                if (documentId.getId().getIdOrigin().equalsIgnoreCase("GCPS")) {
		                    if (documentId.getId().getValue() != null) {
		                        invoiceSeqNum = documentId.getId().getValue().trim();
		                     }
		                }
	                	
	                }
	            }	
	        	}
	        	
	        }
	    }
	    
	    return invoiceSeqNum;
	}

	/**
	 * getInvoiceSource method.
	 * 
	 * @param invoice_jaxb  
	 * 
	 * @return String
	 */
	private String getInvoiceSource(com.ibm.xmlns.ims._1_3.showinvoice.Invoice invoice_jaxb) {   
	    String invoiceSource = null;
		
	    Iterator<?> documentIdsItr = invoice_jaxb.getHeader().getDocumentIds()
	    		.getDocumentIdType().iterator();
	    while (documentIdsItr.hasNext()) {
	        Object obj = documentIdsItr.next();
	        if (obj instanceof JAXBElement) {
	        	if(((JAXBElement<?>) obj).getName().getLocalPart().equals("SupplierDocumentId")){
	        		PartyDocumentId supplierDocumentId = 
	                	(PartyDocumentId) ((JAXBElement<?>)obj).getValue();
	            if (supplierDocumentId.getId().getIdOrigin().equalsIgnoreCase("IBM/Invoice")) {
	                if (supplierDocumentId.getId().getType() != null) {
	                    invoiceSource = supplierDocumentId.getId().getType().trim();
	                }
	            }
	        	}
	        	
	        }
	    }
	    
		return invoiceSource;
	}

	/**
	 * getInvoiceStatus method.
	 * 
	 * @param invoice_jaxb  
	 * 
	 * @return String
	 */
	private String getInvoiceStatus(com.ibm.xmlns.ims._1_3.showinvoice.Invoice invoice_jaxb) {  
		String invStatus = null;

		Iterator<Description> descItr = invoice_jaxb.getHeader().getOrderStatus()
				.getDescription().iterator();
		while (descItr.hasNext()) {
		    Object obj = descItr.next();
		    if (obj instanceof Description) {
                Description desc = (Description) obj;
	            if (desc.getValue().equalsIgnoreCase("Invoice_header-Invoice_status")) {
	                if (invoice_jaxb.getHeader().getOrderStatus().getCode() != null) {
	                    invStatus = invoice_jaxb.getHeader().getOrderStatus().getCode().trim();
	                }
		        }
		    }
		}
		
		return invStatus;
	}

	/**
	 * getInvoiceType method.
	 * 
	 * @param invoice_jaxb  
	 * 
	 * @return String
	 */
	private String getInvoiceType(com.ibm.xmlns.ims._1_3.showinvoice.Invoice invoice_jaxb) {			
		String invoiceType = null;
	    
	    if (invoice_jaxb.getHeader().getType() != null) {
	         invoiceType = invoice_jaxb.getHeader().getType();
	    }
	    
	    return invoiceType;
	}

	/**
	 * getLastUpdateId method.
	 * 
	 * @param invoice_jaxb  
	 * 
	 * @return String
	 */
	private String getLastUpdateId(com.ibm.xmlns.ims._1_3.showinvoice.Invoice invoice_jaxb) {  
	    String lastUpdateId = null;
	    
	    Iterator<?> partiesItr = invoice_jaxb.getHeader().getParties().getPartyType().iterator();
	    while (partiesItr.hasNext()) {
	        Object obj = partiesItr.next();
	        if (obj instanceof JAXBElement) {
	        	if(((JAXBElement<?>) obj).getName().getLocalPart().equals("EmployeeParty")){
	        		PartyInstitutional employeeParty = (PartyInstitutional) ((JAXBElement<?>)obj).getValue();
		            Iterator<Contact> contactsItr = employeeParty.getContacts().getContactAbs().iterator();
		            while (contactsItr.hasNext()) {
		                Object obj2 = contactsItr.next();
		                if (obj2 instanceof Contact) {
		                	Contact contactAbs = (Contact) obj2;
		                    if (contactAbs.getJobTitle().equalsIgnoreCase("Indexer-Modified")) {
			                    Iterator<Contact.EMailAddress> emailItr = contactAbs.getEMailAddress().iterator();
			                    while (emailItr.hasNext()) {
			                        Object obj3 = emailItr.next();
			                        if (obj3 instanceof Contact.EMailAddress) {
			                            Contact.EMailAddress emailAddress = 
			                                (Contact.EMailAddress) obj3;
			                            if (emailAddress.getValue() != null) {
			                                lastUpdateId = emailAddress.getValue().trim();
			                            }
			                        }
			                    }
		                    }
		                }
		            }	
	        	}
	        	
	        }
	    }
	    
	    return lastUpdateId;
	}

	/**
	 * getLastUpdateTimeStamp method.
	 * 
	 * @param invoice_jaxb  
	 * 
	 * @return long
	 */
	private long getLastUpdateTimeStamp(com.ibm.xmlns.ims._1_3.showinvoice.Invoice invoice_jaxb) {
		long lastUpdateTimestamp = 0;
		
		if (invoice_jaxb.getHeader() != null) {
			InvoiceHeader invoiceHeader = invoice_jaxb.getHeader();
			if (invoiceHeader.getLastModificationDateTime() != null) {
				lastUpdateTimestamp = invoiceHeader.getLastModificationDateTime().toGregorianCalendar().getTimeInMillis();
			}
		}
		
		return lastUpdateTimestamp;
	}

	/**
	 * getLegalId method.
	 * 
	 * @param invoice_jaxb  
	 * 
	 * @return String
	 */
	private String getLegalId(com.ibm.xmlns.ims._1_3.showinvoice.Invoice invoice_jaxb){   
	    String legalId = null;
	    
	    Iterator<?> partiesItr = invoice_jaxb.getHeader().getParties().getPartyType().iterator();
	    while (partiesItr.hasNext()) {
	        Object obj = partiesItr.next();
	        if (obj instanceof JAXBElement) {
	        	if(((JAXBElement<?>) obj).getName().getLocalPart().equals("SupplierParty")){
	        		PartyInstitutional supplierParty = (PartyInstitutional) ((JAXBElement<?>)obj).getValue();
		            Iterator<Id> taxIdItr = supplierParty.getTaxId().iterator();
		            while (taxIdItr.hasNext()) {
		                Object obj2 = taxIdItr.next();
		                if (obj2 instanceof Id) {
		                    Id taxId = (Id) obj2;
		                    if (taxId.getIdOrigin().equalsIgnoreCase("VAT")) {
		                        if (taxId.getValue() != null) {
		                            legalId = taxId.getValue().trim();
		                        }
		                    }
		                }
		            }	
	        	}
	        	
	        }
	    }
	    
	    return legalId;
	}

	/**
	 * getOcrKidReference method.
	 * 
	 * @param invoice_jaxb  
	 * 
	 * @return String
	 */
	private String getOcrKidReference(com.ibm.xmlns.ims._1_3.showinvoice.Invoice invoice_jaxb) {   
	    String ocrKidRef = null;
	    
	    Iterator<?> partiesItr = invoice_jaxb.getHeader().getParties()
	    		.getPartyType().iterator();
	    while (partiesItr.hasNext()) {
	        Object obj = partiesItr.next();
	        if (obj instanceof JAXBElement) {
	        	if(((JAXBElement<?>) obj).getName().getLocalPart().equals("RemitToParty")){
	        		PartyInstitutional remitToParty = (PartyInstitutional) ((JAXBElement<?>) obj).getValue();
		            Iterator<PartyId> partyIdItr = remitToParty.getPartyId().getId().iterator();
		            while (partyIdItr.hasNext()) {
		                Object obj2 = partyIdItr.next();
		                if (obj2 instanceof PartyId) {
		                    PartyId partyId = (PartyId) obj2;
		                    if (partyId.getIdOrigin().equalsIgnoreCase("GIL")) {
		                        if (partyId.getValue() != null) {
			                        ocrKidRef = partyId.getValue().trim();
		                        }
		                    }
		                }
		            }
	        	}
	        	
	        }
	    }
	    
	    return ocrKidRef;
	}

	/**
	 * getOfferLetterNumber method.
	 * 
	 * @param invoice_jaxb  
	 * 
	 * @return String
	 */
	private String getOfferLetterNumber(com.ibm.xmlns.ims._1_3.showinvoice.Invoice invoice_jaxb){   
	    String offerLetterNum = null;
	    
	    Iterator<?> docRefItr = invoice_jaxb.getHeader().getDocumentReferences()
	    		.getDocumentReference().iterator();
	    while (docRefItr.hasNext()) {
	        Object obj = docRefItr.next();
	        if (obj instanceof JAXBElement) {
	        	if(((JAXBElement<?>) obj).getName().getLocalPart().equals("FinancialOfferingLetterDocumentReference")){
	        		GenericDocumentReference offerLetterDocRef = 
	                	(GenericDocumentReference) ((JAXBElement<?>)obj).getValue();
	            Iterator<?> docIdItr = offerLetterDocRef.getDocumentIds().getDocumentIdType().iterator();
	            while (docIdItr.hasNext()) {
	                Object obj2 = docIdItr.next();
	                if (obj2 instanceof JAXBElement) {
                	  		if(((JAXBElement<?>) obj2).getName().getLocalPart().equals("DocumentIdType")){
	                		DocumentIdType documentId = (DocumentIdType) ((JAXBElement<?>)obj2).getValue();
		                    if (documentId.getId().getValue() != null) {
		                        offerLetterNum = documentId.getId().getValue().trim();
		                    }
		                	
	                	}
	                	
	                	
	                }
	            }
	        	}
	        	
	        }
	    }
	    
	    return offerLetterNum;
	}

	/**
	 * getPoexCode method.
	 * 
	 * @param invoice_jaxb  
	 * 
	 * @return String
	 */
	private String getPoexCode(com.ibm.xmlns.ims._1_3.showinvoice.Invoice invoice_jaxb) {   
	    String poexCode = null;
	    
	    Iterator<?> partiesItr = invoice_jaxb.getHeader().getParties().getPartyType().iterator();
	    while (partiesItr.hasNext()) {
	        Object obj = partiesItr.next();
	        if (obj instanceof JAXBElement) {
	        	if(((JAXBElement<?>) obj).getName().getLocalPart().equals("SupplierParty")){
	        		PartyInstitutional supplierParty = (PartyInstitutional) ((JAXBElement<?>)obj).getValue();
		            Iterator<PartyId> partyIdItr = supplierParty.getPartyId().getId().iterator();
		            while (partyIdItr.hasNext()) {
		                Object obj2 = partyIdItr.next();
		                if (obj2 instanceof PartyId) {
		                    PartyId partyId = (PartyId) obj2;
		                    if (partyId.getIdOrigin().equalsIgnoreCase("POEX")) {
		                        if (partyId.getValue() != null) {
		                            poexCode = partyId.getValue().trim();
		                        }
		                    }
		                }
		            }
	        	}
	        	
	        }
	    }
	    
	    return poexCode;
	}

	/**
	 * getReferenceInvoiceNumber method.
	 * 
	 * @param invoice_jaxb  
	 * 
	 * @return String
	 */
	private String getReferenceInvoiceNumber(com.ibm.xmlns.ims._1_3.showinvoice.Invoice invoice_jaxb) {   
	    String referenceInvoiceNum = null;
	    
	    Iterator<?> docRefItr = invoice_jaxb.getHeader().getDocumentReferences()
	    		.getDocumentReference().iterator();
	    while (docRefItr.hasNext()) {
	        Object obj = docRefItr.next();
	        if (obj instanceof JAXBElement) {
	        	if(((JAXBElement<?>) obj).getName().getLocalPart().equals("InvoiceDocumentReference")){
	        		OrderDocumentReference invDocRef = 
	                	(OrderDocumentReference) ((JAXBElement<?>)obj).getValue();
	            Iterator<?> docIdItr = invDocRef.getDocumentIds().getDocumentIdType().iterator();
	            while (docIdItr.hasNext()) {
	                Object obj2 = docIdItr.next();
	                if (obj2 instanceof JAXBElement) {
	                	if(((JAXBElement<?>) obj2).getName().getLocalPart().equals("DocumentIdType")){
	                		DocumentIdType documentId = (DocumentIdType) ((JAXBElement<?>)obj2).getValue();
		                    if (documentId.getId().getIdOrigin().equalsIgnoreCase("GIL")) {
		                        if (documentId.getId().getValue() != null) {
		                            referenceInvoiceNum = documentId.getId().getValue().trim();
		                        }
		                    }
	                	}
	                	
	                }
	            }	
	        	}
	        	
	        }
	    }
	    
	    return referenceInvoiceNum;
	}
	
	/**
	 * getResponsibleId method.
	 * 
	 * @param invoice_jaxb  
	 * 
	 * @return String
	 */
	private String getResponsibleId(com.ibm.xmlns.ims._1_3.showinvoice.Invoice invoice_jaxb) { 
	    String responsibleId = null;
	    
	    Iterator<?> partiesItr = invoice_jaxb.getHeader().getParties().getPartyType().iterator();
	    while (partiesItr.hasNext()) {
	        Object obj = partiesItr.next();
	        if (obj instanceof JAXBElement) {
	        	if(((JAXBElement<?>) obj).getName().getLocalPart().equals("EmployeeParty")){
	        		PartyInstitutional employeeParty = (PartyInstitutional) ((JAXBElement<?>)obj).getValue();
		            Iterator<Contact> contactsItr = employeeParty.getContacts().getContactAbs().iterator();
		            while (contactsItr.hasNext()) {
		                Object obj2 = contactsItr.next();
		                if (obj2 instanceof Contact) {
		                	Contact contactAbs = (Contact) obj2;
		                    if (contactAbs.getJobTitle().equalsIgnoreCase("BSP")) {
			                    Iterator<Contact.EMailAddress> emailItr = contactAbs.getEMailAddress().iterator();
			                    while (emailItr.hasNext()) {
			                        Object obj3 = emailItr.next();
			                        if (obj3 instanceof Contact.EMailAddress) {
			                            Contact.EMailAddress emailAddress = 
			                                (Contact.EMailAddress) obj3;
			                            if (emailAddress.getValue() != null) {
			                                responsibleId = emailAddress.getValue().trim();
			                            }
			                        }
			                    }
		                    }
		                }
		            }	
	        	}
	        	
	        }
	    }
	    
	    return responsibleId;
	}

	/**
	 * getSaNumber method.
	 * 
	 * @param invoice_jaxb  
	 * 
	 * @return String
	 */
	private String getSaNumber(com.ibm.xmlns.ims._1_3.showinvoice.Invoice invoice_jaxb) {   
	    String saNum = null;
	    
	    Iterator<?> docRefItr = invoice_jaxb.getHeader().getDocumentReferences()
	    		.getDocumentReference().iterator();
	    while (docRefItr.hasNext()) {
	        Object obj = docRefItr.next();
	        if (obj instanceof JAXBElement) {
	        	if(((JAXBElement<?>) obj).getName().getLocalPart().equals("ContractDocumentReference")){
	        		OrderDocumentReference contractDocRef = 
	                	(OrderDocumentReference) ((JAXBElement<?>)obj).getValue();
	            Iterator<?> docIdItr = contractDocRef.getDocumentIds().getDocumentIdType().iterator();
	            while (docIdItr.hasNext()) {
	                Object obj2 = docIdItr.next();
	                if (obj2 instanceof JAXBElement) {
	                	if(((JAXBElement<?>) obj2).getName().getLocalPart().equals("DocumentIdType")){
	                		DocumentIdType documentId = (DocumentIdType) ((JAXBElement<?>)obj2).getValue();
		                    if (documentId.getId().getIdOrigin().equalsIgnoreCase("IBM")) {
		                        if (documentId.getId().getValue() != null) {
		                            saNum = documentId.getId().getValue().trim();
		                        }
		                    }	
	                	}
	                	
	                }
	            }	
	        	}
	        	
	        }
	    }
	    
	    return saNum;
	}

	/**
	 * getTotalInvoiceAmount method.
	 * 
	 * @param invoice_jaxb  
	 * 
	 * @return double
	 */
	private double getTotalInvoiceAmount(com.ibm.xmlns.ims._1_3.showinvoice.Invoice invoice_jaxb) { 
		double totalInvoiceAmt = 0;
		
		Iterator<Amount> totalAmtItr = invoice_jaxb.getHeader().getTotalAmount().iterator();
	    while (totalAmtItr.hasNext()) {
	        Object obj = totalAmtItr.next();
	        if (obj instanceof Amount) {
	            Amount amt = (Amount) obj;
	            if (amt.getBasis().equalsIgnoreCase("TotalAmount")) {
	                totalInvoiceAmt = amt.getValue().doubleValue();
	            }
	        }
	    }
		
		return totalInvoiceAmt;
	}

	//Story 1750051 CA GIL changes   
	private String getProvinceCode(com.ibm.xmlns.ims._1_3.showinvoice.Invoice invoice_jaxb) {   

	    String provinceCode = null;
	    
	    Iterator<NameValuePair> interfacePropertyIter = invoice_jaxb.getHeader().getInterfaceData()
	    														 .getInterfaceProperty()
	    														 .iterator();
	    while(interfacePropertyIter.hasNext())
	    {
	        Object obj = interfacePropertyIter.next();
	        if(obj instanceof NameValuePair)
	        {
	            NameValuePair nameValuePair = (NameValuePair) obj;
	            if(nameValuePair.getName().equalsIgnoreCase("ProvinceCode"))
	            {
	            	provinceCode = nameValuePair.getValue();
	            }
	        }
	    }
	    
	    return provinceCode;

	}
	//End Story 1750051 CA GIL changes  
	
	/**
	 * getTotalTaxAmount method.
	 * 
	 * @param invoice_jaxb  
	 * 
	 * @return double
	 */
	private double getTotalTaxAmount(com.ibm.xmlns.ims._1_3.showinvoice.Invoice invoice_jaxb) {   
		double totalTaxAmt = 0;
		
	    Iterator<Amount> totalTaxItr = invoice_jaxb.getHeader().getTotalTax().iterator();
	    while (totalTaxItr.hasNext()) {
	        Object obj = totalTaxItr.next();
	        if (obj instanceof Amount) {
	            Amount amount = (Amount) obj;
	            if (amount.getBasis().equalsIgnoreCase("TotalTax")) {
	                totalTaxAmt = amount.getValue().doubleValue();
	            }
	        }
	    }
		
		return totalTaxAmt;
	}

	/**
	 * getTpidCode method.
	 * 
	 * @param invoice_jaxb  
	 * 
	 * @return String
	 */
	private String getTpidCode(com.ibm.xmlns.ims._1_3.showinvoice.Invoice invoice_jaxb) {   
	    String tpidCode = null;
	    
	    Iterator<?> partiesItr = invoice_jaxb.getHeader().getParties().getPartyType().iterator();
	    while (partiesItr.hasNext()) {
	        Object obj = partiesItr.next();
	        if (obj instanceof JAXBElement) {
	        	if(((JAXBElement<?>) obj).getName().getLocalPart().equals("SupplierParty")){
	        		PartyInstitutional supplierParty = (PartyInstitutional) ((JAXBElement<?>)obj).getValue();
		            Iterator<PartyId> partyIdItr = supplierParty.getPartyId().getId().iterator();
		            while (partyIdItr.hasNext()) {
		                Object obj2 = partyIdItr.next();
		                if (obj2 instanceof PartyId) {
		                    PartyId partyId = (PartyId) obj2;
		                    if (partyId.getIdOrigin().equalsIgnoreCase("Supplier")) {
		                        if (partyId.getValue() != null) {
		                            tpidCode = partyId.getValue().trim();
		                        }
		                    }
		                }
		            }	
	        	}
	        	
	        }
	    }
	    
	    return tpidCode;
	}

	/**
	 * getTpidCountry method.
	 * 
	 * @param invoice_jaxb  
	 * 
	 * @return String
	 */
	private String getTpidCountry(com.ibm.xmlns.ims._1_3.showinvoice.Invoice invoice_jaxb) {   
	    String tpidCountry = null;
	    
	    Iterator<?> partiesItr = invoice_jaxb.getHeader().getParties().getPartyType().iterator();
	    while (partiesItr.hasNext()) {
	        Object obj = partiesItr.next();
	        if (obj instanceof JAXBElement) {
	        	if(((JAXBElement<?>) obj).getName().getLocalPart().equals("SupplierParty")){
	        		PartyInstitutional supplierParty = (PartyInstitutional) ((JAXBElement<?>)obj).getValue();
		            Iterator<?> addressesItr = supplierParty.getAddresses().getAddress().iterator();
		            while (addressesItr.hasNext()) {
		                Object obj2 = addressesItr.next();
		                if (obj2 instanceof JAXBElement) {
		                	if(((JAXBElement<?>) obj2).getName().getLocalPart().equals("SecondaryAddress")){
		                		Address secondaryAddress = (Address) ((JAXBElement<?>)obj2).getValue();
			                    if (secondaryAddress.getCountry().getValue() != null) {
			                        tpidCountry = secondaryAddress.getCountry().getValue().trim();
			                    }	
		                	}
		                	
		                }
		            }	
	        	}
	        	
	        }
	    }
	    
	    return tpidCountry;
	}

	/**
	 * getUrgentIndicator method.
	 * 
	 * @param invoice_jaxb  
	 * 
	 * @return boolean
	 */
	private boolean getUrgentIndicator(com.ibm.xmlns.ims._1_3.showinvoice.Invoice invoice_jaxb) {  
	    boolean urgentIndc = false;
	    
	    if (invoice_jaxb.getHeader().getPriority().equalsIgnoreCase("Priority")) {
	        urgentIndc = true;
	    } 

	    return urgentIndc;
	}
	    
	/**
	 * getVarianceReason method.
	 * 
	 * @param invoice_jaxb  
	 * 
	 * @return String
	 */
	private String getVarianceReason(com.ibm.xmlns.ims._1_3.showinvoice.Invoice invoice_jaxb) {  
	    String varianceReason = null;
	    
	    Iterator<Reason> varReasonItr = invoice_jaxb.getHeader().getReason().iterator();
	    while (varReasonItr.hasNext()) {
	        Object obj = varReasonItr.next();
	        if (obj instanceof Reason) {
	            Reason reason = (Reason) obj;
	            if (reason.getCode().equalsIgnoreCase("Variance_Reason")) {
	                if (reason.getValue() != null) {
	                    varianceReason = reason.getValue().trim();
	                }
	            }
	        }
	    }

	    return varianceReason;
	}

	/**
	 * getVendorName method.
	 * 
	 * @param invoice_jaxb  
	 * 
	 * @return String
	 */
	private String getVendorName(com.ibm.xmlns.ims._1_3.showinvoice.Invoice invoice_jaxb) {   
	    String vendorName = null;
	    
	    Iterator<?> partiesItr = invoice_jaxb.getHeader().getParties()
	    		.getPartyType().iterator();
	    while (partiesItr.hasNext()) {
	        Object obj = partiesItr.next();
	        if (obj instanceof JAXBElement) {
	        	if(((JAXBElement<?>) obj).getName().getLocalPart().equals("SupplierParty")){
	        		PartyInstitutional supplierParty = (PartyInstitutional) ((JAXBElement<?>)obj).getValue();
		            Iterator<Name> nameItr = supplierParty.getName().iterator();
		            while (nameItr.hasNext()) {
		                Object obj2 = nameItr.next();
		                if (obj2 instanceof Name) {
		                    Name name = (Name) obj2;
		                    if (name.getValue() != null) {
		                        vendorName = name.getValue().trim();
		                    }
		                }
		            }	
	        	}
	        	
	        }
	    }
	    
	    return vendorName;
	}

	/** 
	 * getVendorNumber method.
	 * 
	 * @param invoice_jaxb  
	 * 
	 * @return String
	 */
	private String getVendorNumber(com.ibm.xmlns.ims._1_3.showinvoice.Invoice invoice_jaxb) {   
	    String vendorNum = null;
	    
	    Iterator<?> partiesItr = invoice_jaxb.getHeader().getParties()
	    		.getPartyType().iterator();
	    while (partiesItr.hasNext()) {
	        Object obj = partiesItr.next();
	        if (obj instanceof JAXBElement) {
	        	if(((JAXBElement<?>) obj).getName().getLocalPart().equals("SupplierParty")){
	        		PartyInstitutional supplierParty = (PartyInstitutional)((JAXBElement<?>) obj).getValue();
		            Iterator<PartyId> partyIdItr = supplierParty.getPartyId().getId().iterator();
		            while (partyIdItr.hasNext()) {
		                Object obj2 = partyIdItr.next();
		                if (obj2 instanceof PartyId) {
		                    PartyId partyId = (PartyId) obj2;
		                    if (partyId.getIdOrigin().equalsIgnoreCase("SAP")) {
		                        if (partyId.getValue() != null) {
		                            vendorNum = partyId.getValue().trim();
		                        }
		                    }
		                }
		            }
	        	}
	        	
	        }
	    }
	    
	    return vendorNum;
	}

	/**
	 * getBillToAddress method.
	 * 
	 * @param invoice_jaxb  
	 * 
	 * @return BillToAddress
	 */
	private BillToAddress getBillToAddress(com.ibm.xmlns.ims._1_3.showinvoice.Invoice invoice_jaxb) {
	    BillToAddress billToAddress = null;
	    
	    Iterator<?> partiesItr = invoice_jaxb.getHeader().getParties()
	    		.getPartyType().iterator();
	    while (partiesItr.hasNext()) {
	        Object obj = partiesItr.next();
	        if (obj instanceof JAXBElement) {
	        	if(((JAXBElement<?>) obj).getName().getLocalPart().equals("BillToParty")){
	        		PartyInstitutional billToParty = (PartyInstitutional) ((JAXBElement<?>)obj).getValue();
		            
		            Iterator<PartyId> partyIdItr = null;
		            Iterator<Name> nameItr = null;
		            Iterator<?> addressItr = null;
		            
		            // check if there is bill to address information.
		            if (billToParty.getPartyId().getId().size() == 0 ||
			            ((PartyId) billToParty.getPartyId().getId().get(0)).getValue().trim().equals("")) {
		                break;
		            }
		            
		            billToAddress = new BillToAddress_jaxb();
		            
		            // country code;
		            billToAddress.setCountryCode(getCountryCode(invoice_jaxb));
		            // invoice sequence number.
		            billToAddress.setInvoiceSequenceNumber(getInvoiceSequenceNumber(invoice_jaxb));
		            // customer number.
		            partyIdItr = billToParty.getPartyId().getId().iterator();
		            while (partyIdItr.hasNext()) {
		                Object obj2 = partyIdItr.next();
		                if (obj2 instanceof PartyId) {
		                    PartyId partyId = (PartyId)  obj2;
		                    if (partyId.getValue() != null) {
		                        billToAddress.setCustomerNumber(partyId.getValue().trim());
		                    }
		                }
		            }
		            // customer name.
		            nameItr = billToParty.getName().iterator();
		            while (nameItr.hasNext()) {
		                Object obj2 = nameItr.next();
		                if (obj2 instanceof Name) {
		                    Name name = (Name) obj2;
		                    if (name.getValue() != null) {
		                        billToAddress.setCustomerName(name.getValue().trim());
		                    }
		                }
		            }
		            // address lines.
		            addressItr = billToParty.getAddresses().getAddress().iterator();
		            while (addressItr.hasNext()) {
		                Object obj2 = addressItr.next();
		                if (obj2 instanceof JAXBElement) {
		                	if(((JAXBElement<?>) obj2).getName().getLocalPart().equals("PrimaryAddress")){
		                		Address primaryAddress = 
			                        (Address) ((JAXBElement<?>)obj2).getValue();
			                    Iterator<AddressLine.Line> lineItr = primaryAddress.getAddressLine().getLine().iterator();
			                    while (lineItr.hasNext()) {
			                        Object obj3 = lineItr.next();
			                        if (obj3 instanceof AddressLine.Line) {
			                            AddressLine.Line lineType = (AddressLine.Line) obj3;
			                            switch (lineType.getSequence().intValue()) {
		                            	case 1:
		                            	    if (lineType.getValue() != null) {
		                            	        billToAddress.setAddressLine1(lineType.getValue().trim());
		                            	    }
		                            	    break;
		                            	case 2:
		                            	    if (lineType.getValue() != null) {
		                            	        billToAddress.setAddressLine2(lineType.getValue().trim());
		                            	    }
		                            	    break;
		                            	case 3:
		                            	    if (lineType.getValue() != null) {
		                            	        billToAddress.setAddressLine3(lineType.getValue().trim());
		                            	    }
		                            	    break;
		                            	case 4:
		                            	    if (lineType.getValue() != null) {
		                            	        billToAddress.setAddressLine4(lineType.getValue().trim());
		                            	    }
		                            	    break;
		                            	case 5:
		                            	    if (lineType.getValue() != null) {
		                            	        billToAddress.setAddressLine5(lineType.getValue().trim());
		                            	    }
		                            	    break;
		                            	case 6:
		                            	    if (lineType.getValue() != null) {
		                            	        billToAddress.setAddressLine6(lineType.getValue().trim());
		                            	    }
		                            	    break;
			                            }
			                        }
			                    }
			                }
		                	}
		                	
		            }
		        }
	        	}
	        	
	            
	    }

	    return billToAddress;
	}
	
	/**
	 * getInstallToAddress method.
	 * 
	 * @param invoice_jaxb  
	 * 
	 * @return InstallToAddress
	 */
	private InstallToAddress getInstallToAddress(com.ibm.xmlns.ims._1_3.showinvoice.Invoice invoice_jaxb) {
	    InstallToAddress installToAddress = null;
	    
	    Iterator<?> partiesItr = invoice_jaxb.getHeader().getParties()
	    		.getPartyType().iterator();
	    while (partiesItr.hasNext()) {
	        Object obj = partiesItr.next();
	        if (obj instanceof JAXBElement) {
	        	if(((JAXBElement<?>) obj).getName().getLocalPart().equals("InstalledAtParty")){
	        		PartyInstitutional intalledAtParty = (PartyInstitutional) ((JAXBElement<?>)obj).getValue();
		            
		            Iterator<PartyId> partyIdItr = null;
		            Iterator<Name> nameItr = null;
		            Iterator<?> addressItr = null;
		            
		            // check if there is bill to address information.
		            if (intalledAtParty.getPartyId().getId().size() == 0 ||
				        ((PartyId) intalledAtParty.getPartyId().getId().get(0)).getValue().trim().equals("")) {
		                break;
		            }
		            
		            installToAddress = new InstallToAddress_jaxb();
		            
		            // country code;
		            installToAddress.setCountryCode(getCountryCode(invoice_jaxb));
		            // invoice sequence number.
		            installToAddress.setInvoiceSequenceNumber(getInvoiceSequenceNumber(invoice_jaxb));
		            // customer number.
		            partyIdItr = intalledAtParty.getPartyId().getId().iterator();
		            while (partyIdItr.hasNext()) {
		                Object obj2 = partyIdItr.next();
		                if (obj2 instanceof PartyId) {
		                    PartyId partyId = (PartyId) obj2;
		                    if (partyId.getValue() != null) {
		                        installToAddress.setCustomerNumber(partyId.getValue().trim());
		                    }
		                }
		            }
		            // customer name.
		            nameItr = intalledAtParty.getName().iterator();
		            while (nameItr.hasNext()) {
		                Object obj2 = nameItr.next();
		                if (obj2 instanceof Name) {
		                    Name name = (Name) obj2;
		                    if (name.getValue() != null) {
		                        installToAddress.setCustomerName(name.getValue().trim());
		                    }
		                }
		            }
		            // address lines.
		            addressItr = intalledAtParty.getAddresses().getAddress().iterator();
		            while (addressItr.hasNext()) {
		                Object obj2 = addressItr.next();
		                if (obj2 instanceof JAXBElement) {
		                	if(((JAXBElement<?>) obj2).getName().getLocalPart().equals("PrimaryAddress")){
		                		Address primaryAddress = 
			                        (Address) ((JAXBElement<?>)obj2).getValue();
			                    Iterator<AddressLine.Line> lineItr = primaryAddress.getAddressLine().getLine().iterator();
			                    while (lineItr.hasNext()) {
			                        Object obj3 = lineItr.next();
			                        if (obj3 instanceof AddressLine.Line) {
			                            AddressLine.Line lineType = (AddressLine.Line) obj3;
			                            switch (lineType.getSequence().intValue()) {
		                            	case 1:
		                            	    if (lineType.getValue() != null) {
		                            	        installToAddress.setAddressLine1(lineType.getValue().trim());
		                            	    }
		                            	    break;
		                            	case 2:
		                            	    if (lineType.getValue() != null) {
		                            	        installToAddress.setAddressLine2(lineType.getValue().trim());
		                            	    }
		                            	    break;
		                            	case 3:
		                            	    if (lineType.getValue() != null) {
		                            	        installToAddress.setAddressLine3(lineType.getValue().trim());
		                            	    }
		                            	    break;
		                            	case 4:
		                            	    if (lineType.getValue() != null) {
		                            	        installToAddress.setAddressLine4(lineType.getValue().trim());
		                            	    }
		                            	    break;
		                            	case 5:
		                            	    if (lineType.getValue() != null) {
		                            	        installToAddress.setAddressLine5(lineType.getValue().trim());
		                            	    }
		                            	    break;
		                            	case 6:
		                            	    if (lineType.getValue() != null) {
		                            	        installToAddress.setAddressLine6(lineType.getValue().trim());
		                            	    }
		                            	    break;
			                            }
			                        }
			                    }
			                }
		                	}
		                	
		            }
		        }
	        	}
	            
	    }

	    return installToAddress;
	}

	/**
	 * getShipToAddress method.
	 * 
	 * @param invoice_jaxb  
	 * 
	 * @return ShipToAddress
	 */
	private ShipToAddress getShipToAddress(com.ibm.xmlns.ims._1_3.showinvoice.Invoice invoice_jaxb) {
	    ShipToAddress shipToAddress = null;
	    
	    Iterator<?> partiesItr = invoice_jaxb.getHeader().getParties()
	    		.getPartyType().iterator();
	    while (partiesItr.hasNext()) {
	        Object obj = partiesItr.next();
	        if (obj instanceof JAXBElement) {
	        	if(((JAXBElement<?>) obj).getName().getLocalPart().equals("ShipToParty")){
	        		PartyInstitutional shipToParty = (PartyInstitutional)((JAXBElement<?>) obj).getValue();
		            
		            Iterator<PartyId> partyIdItr = null;
		            Iterator<Name> nameItr = null;
		            Iterator<?> addressItr = null;
		            
		            // check if there is ship to address information.
		            if (shipToParty.getPartyId().getId().size() == 0 ||
					    ((PartyId) shipToParty.getPartyId().getId().get(0)).getValue().trim().equals("")) {
		                break;
		            } 
		            
	                partyIdItr = shipToParty.getPartyId().getId().iterator();
	                while (partyIdItr.hasNext()) {
	                    Object obj2 = partyIdItr.next();
	                    if (obj2 instanceof PartyId) {
	                        PartyId partyId = (PartyId) obj2;
	                        if (partyId.getIdOrigin().equalsIgnoreCase("IBM/ShipTo")) {
	            	            shipToAddress = new ShipToAddress_jaxb();
	            	            
	            	            // country code;
	            	            shipToAddress.setCountryCode(getCountryCode(invoice_jaxb));
	            	            // invoice sequence number.
	            	            shipToAddress.setInvoiceSequenceNumber(getInvoiceSequenceNumber(invoice_jaxb));
	            	            // customer number.
	               	            if (partyId.getValue() != null) {
	            	                shipToAddress.setCustomerNumber(partyId.getValue().trim());
	            	            }
	             	            // customer name.
	            	            nameItr = shipToParty.getName().iterator();
	            	            while (nameItr.hasNext()) {
	            	                Object obj3 = nameItr.next();
	            	                if (obj3 instanceof Name) {
	            	                    Name name = (Name) obj3;
	            	                    if (name.getValue() != null) {
	            	                        shipToAddress.setCustomerName(name.getValue().trim());
	            	                    }
	            	                }
	            	            }
	            	            // address lines.
	            	            addressItr = shipToParty.getAddresses().getAddress().iterator();
	            	            while (addressItr.hasNext()) {
	            	                Object obj3 = addressItr.next();
	            	                if (obj3 instanceof JAXBElement) {
	            	                	if(((JAXBElement<?>) obj3).getName().getLocalPart().equals("PrimaryAddress")){
	            	                		Address primaryAddress = 
		            	                        (Address) ((JAXBElement<?>)obj3).getValue();
		            	                    Iterator<AddressLine.Line> lineItr = primaryAddress.getAddressLine().getLine().iterator();
		            	                    while (lineItr.hasNext()) {
		            	                        Object obj4 = lineItr.next();
		            	                        if (obj4 instanceof AddressLine.Line) {
		            	                            AddressLine.Line lineType = (AddressLine.Line) obj4;
		            	                            switch (lineType.getSequence().intValue()) {
			                                        	case 1:
			                                        	    if (lineType.getValue() != null) {
			                                        	        shipToAddress.setAddressLine1(lineType.getValue().trim());
			                                        	    }
			                                        	    break;
			                                        	case 2:
			                                        	    if (lineType.getValue() != null) {
			                                        	        shipToAddress.setAddressLine2(lineType.getValue().trim());
			                                        	    }
			                                        	    break;
			                                        	case 3:
			                                        	    if (lineType.getValue() != null) {
			                                        	        shipToAddress.setAddressLine3(lineType.getValue().trim());
			                                        	    }
			                                        	    break;
			                                        	case 4:
			                                        	    if (lineType.getValue() != null) {
			                                        	        shipToAddress.setAddressLine4(lineType.getValue().trim());
			                                        	    }
			                                        	    break;
			                                        	case 5:
			                                        	    if (lineType.getValue() != null) {
			                                        	        shipToAddress.setAddressLine5(lineType.getValue().trim());
			                                        	    }
			                                        	    break;
			                                        	case 6:
			                                        	    if (lineType.getValue() != null) {
			                                        	        shipToAddress.setAddressLine6(lineType.getValue().trim());
			                                        	    }
			                                        	    break;
		            	                            }
		            	                        }
		            	                    }
		            	                }	
	            	                	}
	            	                	
	            	            }
	                        }
	                    }
	                }
	            }
	        	}
	        	
        }

	    return shipToAddress;
	}

	/**
	 * getSoldToAddress method.
	 * 
	 * @param invoice_jaxb  
	 * 
	 * @return SoldToAddress
	 */
	private SoldToAddress getSoldToAddress(com.ibm.xmlns.ims._1_3.showinvoice.Invoice invoice_jaxb) {
	    SoldToAddress soldToAddress = null;
	    
	    Iterator<?> partiesItr = invoice_jaxb.getHeader().getParties()
	    		.getPartyType().iterator();
	    while (partiesItr.hasNext()) {
	        Object obj = partiesItr.next();
	        if (obj instanceof JAXBElement) {
	        	if(((JAXBElement<?>) obj).getName().getLocalPart().equals("SoldToParty")){
	        		PartyInstitutional soldToParty = (PartyInstitutional) ((JAXBElement<?>)obj).getValue();
		            
		            Iterator<PartyId> partyIdItr = null;
		            Iterator<Name> nameItr = null;
		            Iterator<?> addressItr = null;
		            
		            // check if there is sold to address information.
		            if (soldToParty.getPartyId().getId().size() == 0 ||
		                ((PartyId) soldToParty.getPartyId().getId().get(0)).getValue().trim().equals("")) {
		                break;
		            }
		            
	                partyIdItr = soldToParty.getPartyId().getId().iterator();
	                while (partyIdItr.hasNext()) {
	                    Object obj2 = partyIdItr.next();
	                    if (obj2 instanceof PartyId) {
	                        PartyId partyId = (PartyId) obj2;
	                        if (partyId.getIdOrigin().equalsIgnoreCase("IBM/SoldTo")) {
	                            soldToAddress = new SoldToAddress_jaxb();
	            	            
	            	            // country code;
	                            soldToAddress.setCountryCode(getCountryCode(invoice_jaxb));
	            	            // invoice sequence number.
	                            soldToAddress.setInvoiceSequenceNumber(getInvoiceSequenceNumber(invoice_jaxb));
	            	            // customer number.
	                            if (partyId.getValue() != null) {
	                                soldToAddress.setCustomerNumber(partyId.getValue().trim());
	                            }
	            	            // customer name.
	            	            nameItr = soldToParty.getName().iterator();
	            	            while (nameItr.hasNext()) {
	            	                Object obj3 = nameItr.next();
	            	                if (obj3 instanceof Name) {
	            	                    Name name = (Name) obj3;
	            	                    if (name.getValue() != null) {
	            	                        soldToAddress.setCustomerName(name.getValue().trim());
	            	                    }
	            	                }
	            	            }
	            	            // address lines.
	            	            addressItr = soldToParty.getAddresses().getAddress().iterator();
	            	            while (addressItr.hasNext()) {
	            	                Object obj3 = addressItr.next();
	            	                if (obj3 instanceof JAXBElement) {
	            	                	if(((JAXBElement<?>) obj3).getName().getLocalPart().equals("PrimaryAddress")){
	            	                		Address primaryAddress = 
		            	                        (Address) ((JAXBElement<?>)obj3).getValue();
		            	                    Iterator<AddressLine.Line> lineItr = primaryAddress.getAddressLine().getLine().iterator();
		            	                    while (lineItr.hasNext()) {
		            	                        Object obj4 = lineItr.next();
		            	                        if (obj4 instanceof AddressLine.Line) {
		            	                            AddressLine.Line lineType = (AddressLine.Line)  obj4;
		            	                            switch (lineType.getSequence().intValue()) {
			                                        	case 1:
			                                        	    if (lineType.getValue() != null) {
			                                        	        soldToAddress.setAddressLine1(lineType.getValue().trim());
			                                        	    }
			                                        	    break;
			                                        	case 2:
			                                        	    if (lineType.getValue() != null) {
			                                        	        soldToAddress.setAddressLine2(lineType.getValue().trim());
			                                        	    }
			                                        	    break;
			                                        	case 3:
			                                        	    if (lineType.getValue() != null) {
			                                        	        soldToAddress.setAddressLine3(lineType.getValue().trim());
			                                        	    }
			                                        	    break;
			                                        	case 4:
			                                        	    if (lineType.getValue() != null) {
			                                        	        soldToAddress.setAddressLine4(lineType.getValue().trim());
			                                        	    }
			                                        	    break;
			                                        	case 5:
			                                        	    if (lineType.getValue() != null) {
			                                        	        soldToAddress.setAddressLine5(lineType.getValue().trim());
			                                        	    }
			                                        	    break;
			                                        	case 6:
			                                        	    if (lineType.getValue() != null) {
			                                        	        soldToAddress.setAddressLine6(lineType.getValue().trim());
			                                        	    }
			                                        	    break;
		            	                            }
		            	                        }
		            	                    }
		            	                }
	            	                	}
	            	                	
	            	            }
	                        }
	                    }
	                }
	            }
	        	}
	        	
        }

	    return soldToAddress;
	}

	

	


	/**
	 * Get Source Indicator.
	 * 
	 * @param invoice_jaxb  
	 * 
	 * @return sourceIndc String
	 */
	private String getSourceIndc(com.ibm.xmlns.ims._1_3.showinvoice.Invoice invoice_jaxb)
	{
	    String sourceIndc = null;
	    
	    Iterator<NameValuePair> interfacePropertyIter = invoice_jaxb.getHeader().getInterfaceData()
	    														 .getInterfaceProperty()
	    														 .iterator();
	    while(interfacePropertyIter.hasNext())
	    {
	        Object obj = interfacePropertyIter.next();
	        if(obj instanceof NameValuePair)
	        {
	            NameValuePair nameValuePair = (NameValuePair) obj;
	            if(nameValuePair.getName().equalsIgnoreCase("SourceIndc"))
	            {
	                sourceIndc = nameValuePair.getValue();
	            }
	        }
	    }
	    
	    return sourceIndc;
	}

	/**
	 * Get Auto Create COA Indicator.
	 * 
	 * @param invoice_jaxb  
	 * 
	 * @return autoCreateCoaIndc String
	 */
	private String getAutoCreateCoaIndc(com.ibm.xmlns.ims._1_3.showinvoice.Invoice invoice_jaxb)
	{
	    String autoCreateCoaIndc = null;
	    
	    Iterator<NameValuePair> interfacePropertyIter = invoice_jaxb.getHeader().getInterfaceData()
	    														 .getInterfaceProperty()
	    														 .iterator();
	    while(interfacePropertyIter.hasNext())
	    {
	        Object obj = interfacePropertyIter.next();
	        if(obj instanceof NameValuePair)
	        {
	            NameValuePair nameValuePair = (NameValuePair) obj;
	            if(nameValuePair.getName().equalsIgnoreCase("AutoCreateCoaIndc"))
	            {
	                autoCreateCoaIndc = nameValuePair.getValue();
	            }
	        }
	    }
	     
	    return autoCreateCoaIndc;
	}
	
//	
	/**
	 * Get Dist Vendor Number
	 * 
	 * @param invoice_jaxb  
	 * 
	 * @return autoCreateCoaIndc String
	 */
	private String getDistVendorNumber(com.ibm.xmlns.ims._1_3.showinvoice.Invoice invoice_jaxb)
	{
	    String distVendorNumber = null;
	    
	    Iterator<NameValuePair> interfacePropertyIter = invoice_jaxb.getHeader().getInterfaceData()
	    														 .getInterfaceProperty()
	    														 .iterator();
	    while(interfacePropertyIter.hasNext())
	    {
	        Object obj = interfacePropertyIter.next();
	        if(obj instanceof NameValuePair)
	        {
	            NameValuePair nameValuePair = (NameValuePair) obj;
	            if(nameValuePair.getName().equalsIgnoreCase("distVendorNumber"))
	            {
	                distVendorNumber = nameValuePair.getValue(); 
	                break;
	            }
	        }
	    }
	    return distVendorNumber;
	}

	
}
