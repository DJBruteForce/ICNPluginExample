package com.ibm.gil.business;

import java.util.ArrayList;

import com.ibm.gil.model.InvoiceDataModel;


public class ContractInvoice {
	


	InvoiceDataModel invoiceDataModel = null;
	
	
	private String OBJECTID;
	
	private String INVOICENUMBER;
	
	private String INVOICEDATE;
	
	private String COUNTRY;
	
	private String CONTRACTNUMBER;
	
	private String CUSTOMERNAME;
	
	private ArrayList<String> errors = new ArrayList<String>();
	
    public String getHash()
    {
        return getINVOICENUMBER() + "~" + getINVOICEDATE();
    }
	
	
	public ArrayList<String> getErrors() {
		return errors;
	}

	public void setErrors(ArrayList<String> errors) {
		this.errors = errors;
	}

	public String getOBJECTID() {
		return OBJECTID;
	}

	public void setOBJECTID(String oBJECTID) {
		OBJECTID = oBJECTID;
	}

	public String getINVOICENUMBER() {
		return INVOICENUMBER;
	}

	public void setINVOICENUMBER(String iNVOICENUMBER) {
		INVOICENUMBER = iNVOICENUMBER;
	}

	public String getINVOICEDATE() {
		return INVOICEDATE;
	}

	public void setINVOICEDATE(String iNVOICEDATE) {
		INVOICEDATE = iNVOICEDATE;
	}

	public String getCOUNTRY() {
		return COUNTRY;
	}

	public void setCOUNTRY(String cOUNTRY) {
		COUNTRY = cOUNTRY;
	}

	public String getCONTRACTNUMBER() {
		return CONTRACTNUMBER;
	}

	public void setCONTRACTNUMBER(String cONTRACTNUMBER) {
		CONTRACTNUMBER = cONTRACTNUMBER;
	}

	public String getCUSTOMERNAME() {
		return CUSTOMERNAME;
	}

	public void setCUSTOMERNAME(String cUSTOMERNAME) {
		CUSTOMERNAME = cUSTOMERNAME;
	}
	


	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((INVOICEDATE == null) ? 0 : INVOICEDATE.hashCode());
		result = prime * result
				+ ((INVOICENUMBER == null) ? 0 : INVOICENUMBER.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ContractInvoice other = (ContractInvoice) obj;
		if (INVOICEDATE == null) {
			if (other.INVOICEDATE != null)
				return false;
		} else if (!INVOICEDATE.equals(other.INVOICEDATE))
			return false;
		if (INVOICENUMBER == null) {
			if (other.INVOICENUMBER != null)
				return false;
		} else if (!INVOICENUMBER.equals(other.INVOICENUMBER))
			return false;
		return true;
	}
    
	
	

}
