package com.ibm.gil.business;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ibm.gil.util.RegionalBigDecimal;

/*
 * This Class is a LineItemDialog abstraction.
 */
public class InvoiceLineItemsELS {
	
	public InvoiceLineItemsELS() {
		
	}
	
	public InvoiceLineItemsELS(InvoiceELS invoice) {
		
		this.country=invoice.getCOUNTRY();
		this.lineItems=invoice.getLineItems();
		this.InvoiceNumber=invoice.getINVOICENUMBER();
		this.InvoiceDate=invoice.getINVOICEDATE();
		this.provinceCode=invoice.getPROVINCECODE();
		this.vatAmount=invoice.getVATAMOUNT();
		
	}
	
	public InvoiceLineItemsELS(String country){
		this.country=country;
	}
	
	
	boolean disableVatCodes;
	private ArrayList<String> dialogErrorMsgs = new ArrayList();
	private List<String> dialogWarns=new ArrayList();
	private List<FormSelect> vatCodes=new ArrayList();
	private ArrayList<FormSelect> otherCharges=new ArrayList();
	//same list that comes in InvoiceELs, using it here to edit it at the end will be set to invoiceELS object 
	private List<LineItemELS> lineItems = new ArrayList();
//	private  Hashtable VATPercentages = new Hashtable<>();
	private HashMap<String,com.ibm.gil.util.RegionalBigDecimal> vatPercentages = null;
	private HashMap<String,String> vatPercentagesStr = null;
	private List<String> itemsWithWarnings=new ArrayList<String>();
	
	//Story 1750051 CA GIL changes
	private String InvoiceNumber="";
	private String InvoiceDate="";
	private String provinceCode="";
	private String vatAmount="";
		
	
	    public String getVATAMOUNT()
	    {
	        return  this.vatAmount;
	    }

	    public void setVATAMOUNT(String vatAmount)
	    {
	        this.vatAmount= new RegionalBigDecimal(vatAmount).setScale(2, RegionalBigDecimal.ROUND_HALF_UP).toString();
	    }

	    public void setVatAmount(String vatAmount)
	    {
	        this.vatAmount= new RegionalBigDecimal(vatAmount).setScale(2, RegionalBigDecimal.ROUND_HALF_UP).toString();
	    }

	    public void setVATAMOUNTasRegionalBigDecimal(RegionalBigDecimal vatAmount)
	    {
	        setVATAMOUNT(vatAmount.setScale(RegionalBigDecimal.getDefaultScale(), BigDecimal.ROUND_HALF_UP).toString());
	    }
	    
	    public void setVATAMOUNTasBigDecimal(BigDecimal vatAmount)
	    {
	        setVATAMOUNTasRegionalBigDecimal(new RegionalBigDecimal(vatAmount.toString(), RegionalBigDecimal.PERIODDECIMALSEPARATOR));
	    }



		public String getInvoiceNumber() {
			return InvoiceNumber;
		}
		public void setInvoiceNumber(String invoiceNumber) {
			InvoiceNumber = invoiceNumber;
		}
		public String getInvoiceDate() {
			return InvoiceDate;
		}
		public void setInvoiceDate(String invoiceDate) {
			InvoiceDate = invoiceDate;
		}
		public String getProvinceCode() {
			return provinceCode;
		}
		public void setProvinceCode(String provinceCode) {
			this.provinceCode = provinceCode;
		}
		
		//End Story 1750051 CA GIL changes
		
		
	 public HashMap<String, String> getVatPercentagesStr() {
		return vatPercentagesStr;
	}
	public void setVatPercentagesStr(HashMap<String, String> vatPercentagesStr) {
		this.vatPercentagesStr = vatPercentagesStr;
	}
	//	private Hashtable otherCharges;
	private String country="";
	

	public List<LineItemELS> getLineItems() {
		return lineItems;
	}
	
	public void setLineItems(List<LineItemELS> lineItems) {
		this.lineItems = lineItems;
	}
	public ArrayList<FormSelect> getOtherCharges() {
		return otherCharges;
	}
	public void setOtherCharges(ArrayList<FormSelect> otherCharges) {
		this.otherCharges = otherCharges;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public boolean isDisableVatCodes() {
		return disableVatCodes;
	}
	public void setDisableVatCodes(boolean disableVatCodes) {
		this.disableVatCodes = disableVatCodes;
	}
	public ArrayList<String> getDialogErrorMsgs() {
		return dialogErrorMsgs;
	}
	public void setDialogErrorMsgs(ArrayList<String> dialogErrorMsgs) {
		this.dialogErrorMsgs = dialogErrorMsgs;
	}
	public List<FormSelect> getVatCodes() {
		return vatCodes;
	}
	public void setVatCodes(List<FormSelect> vatCodes) {
		this.vatCodes = vatCodes;
	}
	public HashMap<String, com.ibm.gil.util.RegionalBigDecimal> getVatPercentages() {
		return vatPercentages;
	}
	public void setVatPercentages(
			HashMap<String, com.ibm.gil.util.RegionalBigDecimal> vatPercentages) {
		this.vatPercentages = vatPercentages;
	}
	public List<String> getDialogWarns() {
		return dialogWarns;
	}
	public List<String> getItemsWithWarnings() {
		return itemsWithWarnings;
	}
	public void setItemsWithWarnings(List<String> itemsWithWarnings) {
		this.itemsWithWarnings = itemsWithWarnings;
	}
	public void setDialogWarns(List<String> dialogWarns) {
		this.dialogWarns = dialogWarns;
	}
 
	
}
