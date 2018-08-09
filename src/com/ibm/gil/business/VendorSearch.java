
package com.ibm.gil.business;

import java.sql.ResultSet;
import java.util.ArrayList;



import com.ibm.gil.model.VendorSearchDataModel;
import com.ibm.gil.util.RegionalBigDecimal;
import com.ibm.igf.hmvc.DB2;


public class VendorSearch  extends Indexing {
	
	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(VendorSearch.class);
	
	public VendorSearch() {
		
	}
	
	public VendorSearch(String country) {
		
		this.setCOUNTRY(country);
		vendorSearchDataModel = new VendorSearchDataModel(this);
		
	}
	
	private VendorSearchDataModel vendorSearchDataModel = null;

    private String ACCOUNTNUMBER;
    private String VENDORNAME;
    private String VENDORNUMBER;
    private String  ADDRESS;
    private String  ADDRESS2;
    private String  ADDRESS3;
    private String  CUSTOMERNAME;
    private String  VENDORCOMMISSION;
    private String  SUPPLIERADDRESS;
    private String  SUPPLIERADDRESS2;
    private String  SUPPLIERADDRESS3;
    private String  VATREGISTRATIONNUMBER;
    private String  BLOCKINDC;
    private String  SRNUMBER;   
    private String  QUERYITEM;

    public static final String ALLX = "XXXXXX";
    public static final short SEARCHBYNAME = 1;
    public static final short SEARCHBYNUMBER = 2;
    public static final short SEARCHBYACCOUNT = 3;
    
    private ArrayList<VendorSearch> vendorSearchTableList;
    
 
    public VendorSearchDataModel getVendorSearchDataModel() {
		return vendorSearchDataModel;
	}

	public void setVendorSearchDataModel(VendorSearchDataModel vendorSearchDataModel) {
		this.vendorSearchDataModel = vendorSearchDataModel;
	}

	public String getACCOUNTNUMBER() {
		return ACCOUNTNUMBER;
	}

	public void setACCOUNTNUMBER(String aCCOUNTNUMBER) {
		ACCOUNTNUMBER = aCCOUNTNUMBER;
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

	public String getADDRESS() {
		return ADDRESS;
	}

	public void setADDRESS(String aDDRESS) {
		ADDRESS = aDDRESS;
	}

	public String getADDRESS2() {
		return ADDRESS2;
	}

	public void setADDRESS2(String aDDRESS2) {
		ADDRESS2 = aDDRESS2;
	}

	public String getADDRESS3() {
		return ADDRESS3;
	}

	public void setADDRESS3(String aDDRESS3) {
		ADDRESS3 = aDDRESS3;
	}

	public String getCUSTOMERNAME() {
		return CUSTOMERNAME;
	}

	public void setCUSTOMERNAME(String cUSTOMERNAME) {
		CUSTOMERNAME = cUSTOMERNAME;
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

	public static String getAllx() {
		return ALLX;
	}

	public static short getSearchbyname() {
		return SEARCHBYNAME;
	}

	public static short getSearchbynumber() {
		return SEARCHBYNUMBER;
	}

	public static short getSearchbyaccount() {
		return SEARCHBYACCOUNT;
	}

	/**
     * creates a select statement for retrieving account/supplier data by
     * account number Creation date: (3/13/00 11:44:07 AM)
     * 
     * @param aController
     *            SupplierSearchResultsController
     * @return ResultSet
	 * @throws Exception 
     */
    public ArrayList<VendorSearch> selectAccountStatement() throws Exception
    {
    	
    	logger.info("Searching suppliers by account: " + getQUERYITEM());
    	return this.loadResults(getVendorSearchDataModel().selectAccountStatement());
        
    }

    /**
     * creates a select statement for retrieving account/supplier data by
     * supplier name Creation date: (3/13/00 11:44:07 AM)
     * 
     * @return java.lang.String
     * @throws Exception 
     */
    public ArrayList<VendorSearch> selectSupplierStatement() throws Exception
    {
    	
    	logger.info("Searching suppliers by supplier name: " + getQUERYITEM());
    	return this.loadResults(getVendorSearchDataModel().selectSupplierStatement());
    }
    
    /**
     * creates a select statement for retrieving account/supplier data by
     * supplier name Creation date: (3/13/00 11:44:07 AM)
     * 
     * @return java.lang.String
     * @throws Exception 
     */
    public ArrayList<VendorSearch> selectSupplierNumberStatement() throws Exception
    {
    	
    	logger.info("Searching suppliers by supplier number: " + getQUERYITEM());
    	return this.loadResults(getVendorSearchDataModel().selectSupplierNumberStatement());
    }    
    
    private ArrayList<VendorSearch> loadResults(ResultSet results) throws Exception
    {
        try
        {
        	
        	vendorSearchTableList = new ArrayList<VendorSearch>();
        	VendorSearch auxVendorSearch;
 
            while (results.next())
            {
                int i = 1;
                	
                	auxVendorSearch = new VendorSearch();
                	
                	auxVendorSearch.setACCOUNTNUMBER( DB2.getString(results, i++));
                	auxVendorSearch.setVENDORNAME( DB2.getString(results, i++));
                	auxVendorSearch.setVENDORNUMBER( DB2.getString(results, i++));
                	auxVendorSearch.setADDRESS( DB2.getString(results, i++));
                	auxVendorSearch.setADDRESS2( DB2.getString(results, i++));
                	auxVendorSearch.setADDRESS3( DB2.getString(results, i++));
                	auxVendorSearch.setCUSTOMERNAME( DB2.getString(results, i++));
                	auxVendorSearch.setVENDORCOMMISSION( DB2.getString(results, i++));
                	auxVendorSearch.setSUPPLIERADDRESS( DB2.getString(results, i++));
                	auxVendorSearch.setSUPPLIERADDRESS2( DB2.getString(results, i++));
                	auxVendorSearch.setSUPPLIERADDRESS3( DB2.getString(results, i++));
                	auxVendorSearch.setVATREGISTRATIONNUMBER( DB2.getString(results, i++));
                	auxVendorSearch.setSRNUMBER( DB2.getString(results, i++));
                	auxVendorSearch.setBLOCKINDC( DB2.getString(results, i++));
                    
           

                // fix up the decimals
                RegionalBigDecimal amount = new RegionalBigDecimal (auxVendorSearch.getVENDORCOMMISSION(), RegionalBigDecimal.PERIODDECIMALSEPARATOR);
                auxVendorSearch.setVENDORCOMMISSION(amount.toString());
                
                // check for a blank address on the account field
                if ((auxVendorSearch.getADDRESS().length() == 0) && (auxVendorSearch.getADDRESS2().length() == 0) && (auxVendorSearch.getADDRESS3().length() == 0))
                {
                	auxVendorSearch.setADDRESS(auxVendorSearch.getSUPPLIERADDRESS());
                	auxVendorSearch.setADDRESS2(auxVendorSearch.getSUPPLIERADDRESS2());
                	auxVendorSearch.setADDRESS3(auxVendorSearch.getSUPPLIERADDRESS3());
                }

                vendorSearchTableList.add(auxVendorSearch);
            }

            auxVendorSearch = new VendorSearch();
            
            auxVendorSearch.setACCOUNTNUMBER( VendorSearch.ALLX);
            auxVendorSearch.setVENDORNAME( VendorSearch.ALLX);
            auxVendorSearch.setVENDORNUMBER( VendorSearch.ALLX);
            auxVendorSearch.setADDRESS( VendorSearch.ALLX);
            auxVendorSearch.setADDRESS2( VendorSearch.ALLX);
            auxVendorSearch.setADDRESS3(VendorSearch.ALLX);
            auxVendorSearch.setCUSTOMERNAME( VendorSearch.ALLX);
            auxVendorSearch.setVENDORCOMMISSION( VendorSearch.ALLX);
            auxVendorSearch.setSUPPLIERADDRESS( VendorSearch.ALLX);
            auxVendorSearch.setSUPPLIERADDRESS2( VendorSearch.ALLX);
            auxVendorSearch.setSUPPLIERADDRESS3( VendorSearch.ALLX);
            auxVendorSearch.setVATREGISTRATIONNUMBER( VendorSearch.ALLX);
            auxVendorSearch.setSRNUMBER(VendorSearch.ALLX);
            auxVendorSearch.setBLOCKINDC( VendorSearch.ALLX);
            
            vendorSearchTableList.add(auxVendorSearch);

        } catch (Exception exc)
        {
            logger.fatal(exc.toString());
            throw exc;
        }
        
        return vendorSearchTableList;
    }
}
