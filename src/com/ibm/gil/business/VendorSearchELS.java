package com.ibm.gil.business;


import java.sql.ResultSet;
import java.util.ArrayList;




//
//import com.ibm.igf.gil.IndexingDataModel;
//import com.ibm.igf.gil.InvoiceDataModel;


import com.ibm.gil.model.VendorSearchDataModelELS;
import com.ibm.gil.util.RegionalBigDecimal;
import com.ibm.gil.util.RegionalDateConverter;


import com.ibm.igf.hmvc.DB2;

public class VendorSearchELS extends Indexing{
	
	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(VendorSearchELS.class);
	 
	    private String VENDORNAME;
	    private String VENDORNUMBER;
		 private String  VENDORCOMMISSION;
		 private String  SUPPLIERADDRESS;
		 private String  SUPPLIERADDRESS2;
		 private String  SUPPLIERADDRESS3;
		 private String  VATREGISTRATIONNUMBER;
		 private String  BLOCKINDC;
		 private String  SRNUMBER;   
		 private String  QUERYITEM;
		
		private String SRENDDATE="";
		private String SRSTARTDATE="";
	    
		private String VATREGISTRATIONNUMBERREQUIRED="";
		
		private VendorSearchDataModelELS vendorSearchDataModelELS=null;
	   
	    public static final transient String ALLX = "XXXXXX";
	    public static final transient short SEARCHBYNAME = 1;
	    public static final transient short SEARCHBYNUMBER = 2;
	    public static final transient short SEARCHBYVAT = 3;
	    
	   
	    
	    public static short getSearchbyname() {
			return SEARCHBYNAME;
		}

		public static short getSearchbynumber() {
			return SEARCHBYNUMBER;
		}

		public static short getSearchbyaccount() {
			return SEARCHBYVAT;
		}
		
	    
	    private ArrayList<VendorSearchELS> vendorSearchELSTableList = null;

	    public VendorSearchELS() {
			
		}
		
		
	    
	    public VendorSearchELS(String country)
	    {
	       this.setCOUNTRY(country);
	       vendorSearchDataModelELS=new VendorSearchDataModelELS(this);
	    }

	    /*
	     * getters and setters
	     */
	    
	    
	    public VendorSearchDataModelELS getVendorSearchDataModelELS() {
			return vendorSearchDataModelELS;
		}

		public void setVendorSearchDataModelELS(VendorSearchDataModelELS vendorSearchDataModelELS) {
			this.vendorSearchDataModelELS = vendorSearchDataModelELS;
		}
	    

		public String getVENDORNAME() {
			return VENDORNAME;
		}

		public void setVENDORNAME(String vENDORNAME) {
			VENDORNAME = vENDORNAME;
		}

		public String getVENDORNUMBER() {
			return VENDORNUMBER;
		}

		public void setVENDORNUMBER(String vENDORNUMBER) {
			VENDORNUMBER = vENDORNUMBER;
		}
		
		public String getVENDORCOMMISSION() {
			return VENDORCOMMISSION;
		}

		public void setVENDORCOMMISSION(String vENDORCOMMISSION) {
			VENDORCOMMISSION = vENDORCOMMISSION;
		}

		public String getSUPPLIERADDRESS() {
			return SUPPLIERADDRESS;
		}

		public void setSUPPLIERADDRESS(String sUPPLIERADDRESS) {
			SUPPLIERADDRESS = sUPPLIERADDRESS;
		}

		public String getSUPPLIERADDRESS2() {
			return SUPPLIERADDRESS2;
		}

		public void setSUPPLIERADDRESS2(String sUPPLIERADDRESS2) {
			SUPPLIERADDRESS2 = sUPPLIERADDRESS2;
		}

		public String getSUPPLIERADDRESS3() {
			return SUPPLIERADDRESS3;
		}

		public void setSUPPLIERADDRESS3(String sUPPLIERADDRESS3) {
			SUPPLIERADDRESS3 = sUPPLIERADDRESS3;
		}

		public String getVATREGISTRATIONNUMBER() {
			return VATREGISTRATIONNUMBER;
		}

		public void setVATREGISTRATIONNUMBER(String vATREGISTRATIONNUMBER) {
			VATREGISTRATIONNUMBER = vATREGISTRATIONNUMBER;
		}

		public String getBLOCKINDC() {
			return BLOCKINDC;
		}

		public void setBLOCKINDC(String bLOCKINDC) {
			BLOCKINDC = bLOCKINDC;
		}

		public String getSRNUMBER() {
			return SRNUMBER;
		}

		public void setSRNUMBER(String sRNUMBER) {
			SRNUMBER = sRNUMBER;
		}

		public String getQUERYITEM() {
			return QUERYITEM;
		}

		public void setQUERYITEM(String qUERYITEM) {
			QUERYITEM = qUERYITEM;
		}

		
		
	   	    public String getSRENDDATE() {
			return SRENDDATE;
		}

		public void setSRENDDATE(String sRENDDATE) {
			SRENDDATE = sRENDDATE;
		}

		public String getSRSTARTDATE() {
			return SRSTARTDATE;
		}

		public void setSRSTARTDATE(String sRSTARTDATE) {
			SRSTARTDATE = sRSTARTDATE;
		}

		public String getVATREGISTRATIONNUMBERREQUIRED() {
			return VATREGISTRATIONNUMBERREQUIRED;
		}

		public void setVATREGISTRATIONNUMBERREQUIRED(
				String vATREGISTRATIONNUMBERREQUIRED) {
			VATREGISTRATIONNUMBERREQUIRED = vATREGISTRATIONNUMBERREQUIRED;
		}

		/**
	     * creates a select statement for retrieving account/supplier data by
	     * supplier name Creation date: (3/13/00 11:44:07 AM)
	     * 
	     * @return java.lang.String
	     */
	    public ArrayList<VendorSearchELS> selectSupplierNumberStatement() throws Exception
	    {
	    	logger.debug("inside selectSupplierNumberStatement VendorSearchELS:");
	    	return this.loadResults(getVendorSearchDataModelELS().selectSupplierNumberStatement());
	    	
	       // return vendorSearchDataModelELS.doSelect("stsuplr");
	    }

	    /**
	     * creates a select statement for retrieving account/supplier data by
	     * supplier name Creation date: (3/13/00 11:44:07 AM)
	     * 
	     * @return java.lang.String
	     */
	    public ArrayList<VendorSearchELS> selectSupplierNameStatement() throws Exception
	    {
	    	
	    	logger.debug("inside selectSupplierNameStatement VendorSearchELS:");
	    	return this.loadResults(getVendorSearchDataModelELS().selectSupplierNameStatement());
	       // return vendorSearchDataModelELS.doSelect("stname");
	    }

	    /**
	     * creates a select statement for retrieving account/supplier data by vat
	     * reg # Creation date: (3/13/00 11:44:07 AM)
	     * 
	     * @return java.lang.String
	     */
	    public ArrayList<VendorSearchELS> selectVATRegistrionStatement() throws Exception
	    {
	    	logger.debug("inside selectVATRegistrionStatement VendorSearchELS:");
	    	return this.loadResults(getVendorSearchDataModelELS().selectVATRegistrionStatement());
	       // return vendorSearchDataModelELS.doSelect("stvatreg#");
	    }

	    
	  private ArrayList<VendorSearchELS> loadResults(ResultSet results)
	    {
	        try
	        {
	           //TablePanel aTablePanel = ((TablePanel) getViewPanel());
	           // VendorSearchDataModel aVendorSearchDataModel = null;
	        	VendorSearchELS vendorSearchELS = null;
	        	vendorSearchELSTableList = new ArrayList<VendorSearchELS>();
	            
	           /* int[] fields = { VendorSearchDataModel.VENDORNAMEidx, VendorSearchDataModel.VENDORNUMBERidx, VendorSearchDataModel.VENDORCOMMISSIONidx, VendorSearchDataModel.SUPPLIERADDRESSidx, VendorSearchDataModel.SUPPLIERADDRESS2idx,
	                    VendorSearchDataModel.SUPPLIERADDRESS3idx, VendorSearchDataModel.SRNUMBERidx, VendorSearchDataModel.SRENDDATEidx, VendorSearchDataModel.VATREGISTRATIONNUMBERidx, VendorSearchDataModel.SRSTARTDATEidx,
	                    VendorSearchDataModel.VATREGISTRATIONNUMBERREQUIREDidx, VendorSearchDataModel.BLOCKINDCidx };*/

	           // aTablePanel.clear();
	            int count = 0;
	          
	           
	            
	            while (results.next())
	            {
	                count++;
	                
	                vendorSearchELS = new VendorSearchELS();
	               // int i =1;
	               // aVendorSearchDataModel = new VendorSearchDataModel();
	               /* for (int i = 0; i < fields.length; i++)
	                {
	                    aVendorSearchDataModel.set(fields[i], DB2.getString(results, i + 1));
	                }*/
	                logger.debug("Inside results next: ");
	               
	                vendorSearchELS.setVENDORNAME(DB2.getString(results, 1));
	                vendorSearchELS.setVENDORNUMBER(DB2.getString(results, 2));
	                vendorSearchELS.setVENDORCOMMISSION(DB2.getString(results, 3));
	                vendorSearchELS.setSUPPLIERADDRESS(DB2.getString(results, 4));
	                vendorSearchELS.setSUPPLIERADDRESS2(DB2.getString(results, 5));
	                vendorSearchELS.setSUPPLIERADDRESS3(DB2.getString(results, 6));
	                vendorSearchELS.setSRNUMBER(DB2.getString(results, 7));
	                vendorSearchELS.setSRENDDATE(RegionalDateConverter.convertDate("DB2", "GUI", DB2.getString(results, 8)));
	                vendorSearchELS.setVATREGISTRATIONNUMBER(DB2.getString(results, 9));
	                vendorSearchELS.setBLOCKINDC(DB2.getString(results, 12));
	                    
	                                              
	                // fix up the decimals

	                // fix up the decimals
	                RegionalBigDecimal amount = new RegionalBigDecimal (vendorSearchELS.getVENDORCOMMISSION(), RegionalBigDecimal.PERIODDECIMALSEPARATOR);
	                vendorSearchELS.setVENDORCOMMISSION(amount.toString());
	              /*  RegionalBigDecimal amount = new RegionalBigDecimal (getVENDORCOMMISSION(), RegionalBigDecimal.PERIODDECIMALSEPARATOR);
	                setVENDORCOMMISSION(amount.toString());
	                
	                // transform the date
	                setSRENDDATE(RegionalDateConverter.convertDate("DB2", "GUI", getSRENDDATE()));*/
	                //aTablePanel.addRow(aVendorSearchDataModel);
	                
	                vendorSearchELSTableList.add(vendorSearchELS);
	                
	               }

	           	            
	           if (count == 1)
	            {
	                // select this row
	              //aTablePanel.selectItem(((VendorSearchFrame) getViewFrame()).getJScrollPane1(), aTablePanel.getModel(), getVendorSearchDataModelELS());

	                // use this record
	               // getViewFrame().setVisible(false);
	               // fireActionPerformed("Vendor Selected");
	            } else if (count == 0)
	            {
	                // no match
	               // getViewFrame().setVisible(false);
	              //fireActionPerformed("Vendor Not Found");
	            } else
	            {
	               // getViewFrame().setVisible(true);
	            }

	        } catch (Exception exc)
	        {
	         logger.debug("error searching for vendor");
	        }
	        
	        return vendorSearchELSTableList ;

	    }
	    

}
