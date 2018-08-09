package com.ibm.gil.model;


import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.naming.NamingException;



import com.ibm.gil.business.FormSelect;
import com.ibm.gil.business.Indexing;
import com.ibm.gil.business.Invoice;
import com.ibm.gil.business.InvoiceELS;
import com.ibm.gil.business.InvoiceLineItemsELS;
import com.ibm.gil.business.LineItemELS;
import com.ibm.gil.business.OfferingLetterELS;
import com.ibm.gil.exception.GILCNException;
import com.ibm.gil.service.helper.LineItemELSServiceHelper;
import com.ibm.gil.util.CNUtilConstants;
import com.ibm.gil.util.GilUtility;
import com.ibm.gil.util.RegionalBigDecimal;
import com.ibm.gil.util.RegionalDateConverter;
import com.ibm.igf.hmvc.ResultSetCache;
import com.ibm.igf.webservice.CTSGetTaxes;
import com.ibm.igf.webservice.GCPSGetCustomer;
import com.ibm.igf.webservice.GCPSProcessInvoice;
//import com.ibm.igf.webservice.CTSGetTaxes;
//import com.ibm.igf.webservice.GCPSGetCustomer;
import com.ibm.igf.webservice.GCPSGetInvoice;
//import com.ibm.igf.webservice.GCPSGetOL;
//import com.ibm.igf.webservice.GCPSProcessInvoice;
import com.ibm.igf.webservice.GCPSGetOL;

import com.sun.rowset.CachedRowSetImpl;

public class InvoiceDataModelELS extends DataModel {
	
	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(InvoiceDataModelELS.class);
	private InvoiceELS invoiceParent;
		
	public InvoiceDataModelELS(InvoiceELS invoiceParent){
		super(invoiceParent);
		this.invoiceParent=invoiceParent;
	}
//	   // story 1497875 
//
	    /*
	     * supporting table caches
	     */
	    private static transient ResultSetCache currencyResults = null;
	    private static transient ResultSetCache invoiceTypesResults = null;
	    private static transient ResultSetCache POEXCodesResults = null;
	    private static transient ResultSetCache countryResults = null;
	    private static transient ResultSetCache gcpsInvoiceTypesResults = null;
	    private static transient ResultSetCache companyCodesResults= null;
		public void init()
	    {
//	        super.init();
	        invoiceParent.setEXCHANGERATE(new RegionalBigDecimal("0").setScale(4).toString());
	        invoiceParent.setTOTALAMOUNT(RegionalBigDecimal.ZERO.setScale(RegionalBigDecimal.getDefaultScale()).toString());
	        invoiceParent.setVATAMOUNT(RegionalBigDecimal.ZERO.setScale(RegionalBigDecimal.getDefaultScale()).toString());
	        invoiceParent.setNETAMOUNT(RegionalBigDecimal.ZERO.setScale(RegionalBigDecimal.getDefaultScale()).toString());
	        invoiceParent.setVATBALANCE(RegionalBigDecimal.ZERO.setScale(RegionalBigDecimal.getDefaultScale()).toString());
	        invoiceParent.setNETBALANCE(RegionalBigDecimal.ZERO.setScale(RegionalBigDecimal.getDefaultScale()).toString());
	        invoiceParent.setLINEITEMTOTALAMOUNT(RegionalBigDecimal.ZERO.setScale(RegionalBigDecimal.getDefaultScale()).toString());
	        invoiceParent.setLINEITEMVATAMOUNT(RegionalBigDecimal.ZERO.setScale(RegionalBigDecimal.getDefaultScale()).toString());
	        invoiceParent.setLINEITEMNETAMOUNT(RegionalBigDecimal.ZERO.setScale(RegionalBigDecimal.getDefaultScale()).toString());
	        invoiceParent.setVATVARIANCE(RegionalBigDecimal.ZERO.setScale(RegionalBigDecimal.getDefaultScale()).toString());
	        invoiceParent.setEXCEPTIONSINDICATOR("N");
	        invoiceParent.setCOAWITHINVOICEINDICATOR("N");
	        invoiceParent.setROFOFFERLETTERINDICATOR("N");
	        invoiceParent.setROFINVOICEINDC("N");
	        invoiceParent.setTOLERANCEINDC("Y");
	        try
	        {
	        	invoiceParent.setSRENDDATE(RegionalDateConverter.getBlankDate("GUI"));
	        	invoiceParent.setINVOICEDATE(RegionalDateConverter.getBlankDate("GUI"));
	        } catch (IllegalArgumentException exc)
	        {

	        }
	    }

	    private transient static GCPSGetOL aGCPSGetOfferingLetter = null;
//
	    private GCPSGetOL getGCPSGetOfferingLetter()
	    {
	        if (aGCPSGetOfferingLetter == null)
	        {
	            aGCPSGetOfferingLetter = new GCPSGetOL();
	        }
	        return aGCPSGetOfferingLetter;
	    }

	    private transient static GCPSGetInvoice aGCPSGetInvoice = null;

	    private GCPSGetInvoice getGCPSGetInvoice()
	    {
	        if (aGCPSGetInvoice == null)
	        {
	            aGCPSGetInvoice = new GCPSGetInvoice();
	        }
	        return aGCPSGetInvoice;
	    }

	    private transient static GCPSProcessInvoice aGCPSProcessInvoice = null;

	    private GCPSProcessInvoice getGCPSProcessInvoice()
	    {
	        if (aGCPSProcessInvoice == null)
	        {
	            aGCPSProcessInvoice = new GCPSProcessInvoice();
	        }
	        return aGCPSProcessInvoice;
	    }
	    
	    private transient static CTSGetTaxes aCTSGetTaxes = null;
	    
	    private CTSGetTaxes getCTSGetTaxes(){
	    	if(aCTSGetTaxes == null){
	    		aCTSGetTaxes = new CTSGetTaxes();
	    	}
	    	return aCTSGetTaxes;
	    }
	    
//	    public void setGCPSGetInvoice(GCPSGetInvoice vGCPSGetInvoice)
//	    {
//	    	this.aGCPSGetInvoice = vGCPSGetInvoice; 
//	    }
//
	    private transient static GCPSGetCustomer aGCPSGetCustomer = null;
//
	    private GCPSGetCustomer getGCPSGetCustomer()
	    {
	        if (aGCPSGetCustomer == null)
	        {
	            aGCPSGetCustomer = new GCPSGetCustomer();
	        }
	        return aGCPSGetCustomer;
	    }

	    
	    /*
	     * reset index values if the indexing is cancelled
	     */
	    public void rollbackIndexing()
	    {
	        // load the database record if it exists
	        try
	        {
	            ArrayList results = selectByObjectIdStatement();
	            if (results != null && results.size() > 0)
	            {
	                InvoiceELS aIDM = (InvoiceELS) results.get(0);
	                // restore the old fields
	                invoiceParent.setINVOICENUMBER(aIDM.getINVOICENUMBER());
	                invoiceParent.setINVOICEDATE(aIDM.getINVOICEDATE());
	                invoiceParent.setVENDORNAME(aIDM.getVENDORNAME());
	                invoiceParent.setCUSTOMERNAME(aIDM.getCUSTOMERNAME());
	                invoiceParent.setCUSTOMERNUMBER(aIDM.getCUSTOMERNUMBER());
	                invoiceParent.setOFFERINGLETTERNUMBER(aIDM.getOFFERINGLETTERNUMBER());
	            }
	        } catch (Exception exc)
	        {
	            logger.fatal(exc.toString());
	            logger.error("Error retrieving document's database values");
	        }
	        updateIndexValues();
	    }

	    /*
	     * sync up content manager with any changes from the gui
	     */
	    public void updateIndexValues()
	    {
	        try
	        {
	            String invoiceDate = RegionalDateConverter.convertDate("GUI", "CM", invoiceParent.getINVOICEDATE());
	            String objectId = invoiceParent.getOBJECTID();

	            // update the indexes
	            String[] fields = { "Invoice Number", "Invoice Date", "Business Partner Name" };
	            String[] values = { invoiceParent.getINVOICENUMBER(), invoiceDate, invoiceParent.getVENDORNAME() };
	            if (objectId.trim().length() > 0)
	            {
	            	boolean rc = getContentManager().updateDocument(objectId, fields, values, invoiceParent.getDatastore());

	            }
	        } catch (Exception exc)
	        {	invoiceParent.getDialogErrorMsgs().add(exc.getMessage());
	        	invoiceParent.getDialogErrorMsgs().add("Error setting Content Manager index values");
	            logger.fatal(exc.toString());
	            logger.error("Error setting Content Manager index values");
	        }
	    }

	    
	    /*
	     * retieving the reference invoice control data by invoice number / country
	     * code
	     */
	    public ArrayList selectByReferenceInvoiceStatement()  throws Exception
	    {
	        InvoiceELS aIDM = new InvoiceELS();
	        aIDM.setINVOICENUMBER(invoiceParent.getREFERENCEINVOICENUMBER());
	        aIDM.setCOUNTRY(invoiceParent.getCOUNTRY());
	        aIDM.setUSERID(invoiceParent.getUSERID());
	        aIDM.setGcmsEndpoint(invoiceParent.getGcmsEndpoint());
	        aIDM.setRdcEndpoint(invoiceParent.getRdcEndpoint());
	        aIDM.generateUNIQUEREQUESTNUMBER();

	        ArrayList results = getGCPSGetInvoice().getInvoices(aIDM);

	        return results;
	    }
	    /*
	     * creates a select statement for retrieving the currency information base
	     * on the country
	     */
	    public ResultSetCache selectCurrencyCodesStatement() throws SQLException
	    {

//	        if ((currencyResults != null) && (currencyResults.getIndexKey().equals(getIndexingObj().getCOUNTRY())))
//	        {
//	            currencyResults.beforeFirst();
//	            return (currencyResults);
//	        }

	        StringBuffer sql = new StringBuffer();
	        String schema = getCountryProperties().getAptsSchema();

	        PreparedStatement pstmt = null;
	        Connection conn = null;
	        
	        // build the sql statement, use append since it is more efficient
	        sql.append("select distinct cccurr, CCCDFT, cccmpcode from ");
	        sql.append(schema);
	        sql.append("ccccntl where cccntcode = ?");
	        sql.append("  order by cccurr");

	        // prepare the statement to execute
	        try
	        {
	        	conn=getDataSourceTransactionManager().getDataSource().getConnection();
	        	pstmt=conn.prepareStatement(sql.toString());
	        	pstmt.setString(1, getIndexingObj().getCOUNTRY());
	            ResultSet results =pstmt.executeQuery();
	            currencyResults = new ResultSetCache(results,getIndexingObj().getCOUNTRY(), 0, 1, "*");

	            // return the results
	            return (currencyResults);
	        } catch (SQLException exc)
	        {
//	            fatal(exc.toString() + "\n" + sql + "\n");
	            throw exc;
	        }finally{
	        	pstmt.close();
	        	conn.close();
	        }
	    }
	    
	    /**
	     * creates a select statement for retrieving the Company Code information
	     * Creation date: (09/25/07 11:44:07 AM)
	     * 
	     * @return ResultSetCache
	     */
	    public ResultSetCache selectCompanyCodesStatement() throws SQLException {
//
//			if ((companyCodesResults != null)
//					&& (companyCodesResults.getIndexKey().equals(invoiceParent.getCOUNTRY()))) {
//				companyCodesResults.beforeFirst();
//				return (companyCodesResults);
//			}

			StringBuffer sql = new StringBuffer();
			String schema = getCountryProperties().getAptsSchema();
			PreparedStatement pstmt = null;
			Connection conn = null;
			// build the sql statement, use append since it is more efficient
			sql.append("select distinct CCCMPCODE, CCCDFT, cccurr from ");
			sql.append(schema);
			sql.append("CCCCNTL where cccntcode = ?");

			// prepare the statement to execute
			try {
				
				
				logger.debug("Searching for Company Codes");
				 
	        	conn = getDataSourceTransactionManager().getDataSource().getConnection();
	            pstmt = conn.prepareStatement(sql.toString());
	            pstmt.setString(1, getIndexingObj().getCOUNTRY());
	            ResultSet results =  pstmt.executeQuery();
				companyCodesResults  = new ResultSetCache(results,getIndexingObj().getCOUNTRY(), 0, 1, "*");
				 logger.debug("Found:"+ companyCodesResults.size());
				// return the results
				return (companyCodesResults);
			} catch (SQLException exc) {
				logger.fatal(exc.toString() + "\n" + sql + "\n");
				throw exc;
			}finally	{
				
				pstmt.close();
				conn.close();
				
			}
		}
	    
	    /*
	     * retrieve the country default invoice type
	     */
	    public ResultSet selectInvoiceTypesDefaultStatement() throws Exception
	    {
	        String defaultInvoiceType = "IBM";
	        StringBuffer sql = new StringBuffer();
	        String schema = getCountryProperties().getAptsSchema();
	        Statement stmt = null;
	    	CachedRowSetImpl cachedRs = new CachedRowSetImpl();
	        Connection conn = null;

	        // build the sql statement, use append since it is more efficient
	        sql.append("select DIDFINVTYP from ");
	        sql.append(schema);
	        sql.append("DFTINVTYP where DICTYCD = ");
	        sql.append(getSqlString(invoiceParent.getCOUNTRY()));

	        // prepare the statement to execute
	        try
	        {
	        	conn = getDataSourceTransactionManager().getDataSource().getConnection();
	         	stmt = conn.createStatement();
	            ResultSet results = stmt.executeQuery(sql.toString());
	            
	            cachedRs.populate(results);

	            // return the results
	            return (cachedRs);
	        }  catch (SQLException exc)
	        {
	            logger.fatal(exc.toString() + "\n" + sql + "\n");
	            throw exc;
	            
	        }catch (Exception exc)
	        {
	            logger.fatal(exc.toString() + "\n" + sql + "\n");
	            throw exc;
	            
	        } finally	{
				
				stmt.close();
				conn.close();
	        }

	    }
//
	    /*
	     * creates a select statement for retrieving the invoice types information
	     * Creation date: (04/05/07 11:44:07 AM)
	     * 
	     * @return ResultSetCache
	     */
	    public ResultSet selectDefaultInvoiceTypesStatement() throws SQLException
	    {

	        StringBuffer sql = new StringBuffer();
	        String schema = getCountryProperties().getAptsSchema();
	        PreparedStatement pstmt = null;
	    	CachedRowSetImpl cachedRs = new CachedRowSetImpl();
	        Connection conn = null;
	        // build the sql statement, use append since it is more efficient
	        sql.append("select DIDFINVTYP from ");
	        sql.append(schema);
	        sql.append("DFTINVTYP");
	        sql.append(" where dictycd= ?");
	       

	        // prepare the statement to execute
	        try
	        {
	        	conn = getDataSourceTransactionManager().getDataSource().getConnection();
	            pstmt = conn.prepareStatement(sql.toString());
	            pstmt.setString(1, getIndexingObj().getCOUNTRY());
	            ResultSet results =  pstmt.executeQuery();
	           // cachedRs = RowSetProvider.newFactory().createCachedRowSet();
	            cachedRs.populate(results);

	            return cachedRs;
	        }  catch (SQLException exc)
	        {
	            logger.fatal(exc.toString() + "\n" + sql + "\n");
	            throw exc;
	            
	        } finally	{
	        	conn.close();
				pstmt.close();
				
			}
	    }

	    /*
	     * creates a select statement for retrieving the invoice types information
	     */
	    public ResultSetCache selectInvoiceTypesStatement() throws SQLException
	    {

//	        if ((invoiceTypesResults != null) && (invoiceTypesResults.getIndexKey().equals("")))
//	        {
//	            invoiceTypesResults.beforeFirst();
//	            return (invoiceTypesResults);
//	        }

	        StringBuffer sql = new StringBuffer();
	        String schema = getCountryProperties().getAptsSchema();
	        Connection conn = null;
	        Statement stmt = null;
	        // build the sql statement, use append since it is more efficient
	        sql.append("select ITINVTYP from ");
	        sql.append(schema);
	        sql.append("invtyppf");

	        // prepare the statement to execute
	        try
	        {
	        	 logger.debug("Searching for Invoice Types");
	        	 
	          	conn = getDataSourceTransactionManager().getDataSource().getConnection();
	          	stmt = conn.createStatement();
	            ResultSet results = stmt.executeQuery(sql.toString());
	            invoiceTypesResults = new ResultSetCache(results, "", 0, 0, "");
	            logger.debug("Found:"+ invoiceTypesResults.size());
	            // return the results
	            return (invoiceTypesResults);
	        } catch (SQLException exc)
	        {
	            logger.fatal(exc.toString() + "\n" + sql + "\n");
	            throw exc;
	        }finally	{
				stmt.close();
	        	conn.close();
				
			}
	    }

	    /*
	     * creates a select statement for retrieving the invoice types information
	     */
	    public ResultSetCache selectGCPSInvoiceTypesStatement() throws SQLException
	    {
//
//	        if ((gcpsInvoiceTypesResults != null) && (gcpsInvoiceTypesResults.getIndexKey().equals("")))
//	        {
//	            gcpsInvoiceTypesResults.beforeFirst();
//            return (gcpsInvoiceTypesResults);
//	        }

	        StringBuffer sql = new StringBuffer();
	        String schema = getCountryProperties().getGcmsSchema();
	        Connection conn = null;
	        Statement stmt = null;
	        
	        // build the sql statement, use append since it is more efficient
	        sql.append("select IGINVTYP from ");
	        sql.append(schema);
	        sql.append("INVTYPPFG");

	        // prepare the statement to execute
	        try
	        {
	        	conn = getDataSourceTransactionManager().getDataSource().getConnection();
	         	stmt = conn.createStatement();
	            ResultSet results =stmt.executeQuery(sql.toString());
	            gcpsInvoiceTypesResults = new ResultSetCache(results, "", 0, 0, "");

	            // return the results
	            return (gcpsInvoiceTypesResults);
	        } catch (SQLException exc)
	        {
	            logger.fatal(exc.toString() + "\n" + sql + "\n");
	            throw exc;
	        } finally	{
				stmt.close();
	        	conn.close();
				
			}
	    }

	    /*
	     * creates a select statement for retrieving the country info
	     */
	    public ResultSetCache selectCountryInfoStatement() throws Exception
	    {

//	        if ((countryResults != null) && (countryResults.getIndexKey().equals(invoiceParent.getCOUNTRY())))
//	        {
//	            countryResults.beforeFirst();
//	            return (countryResults);
//	        }

	        StringBuffer sql = new StringBuffer();
	        Connection conn = null;
	        Statement stmt = null;
	        String schema = getCountryProperties().getAptsSchema();

	        // build the sql statement, use append since it is more efficient
	        sql.append("select cccntcode, cccmpcode, cccurr from ");
	        sql.append(schema);
	        sql.append("ccccntl where cccdft = '*' and cccntcode = ");
	        sql.append(getSqlString(invoiceParent.getCOUNTRY()));

	        // prepare the statement to execute
	        try
	        {
	        	conn = getDataSourceTransactionManager().getDataSource().getConnection();
	        	stmt = conn.createStatement();
	            ResultSet results =stmt.executeQuery(sql.toString());
	            		//getDatabaseDriver().executeQuery(getDatabaseDriver().getConnection(region, getCOUNTRY()), sql.toString());
	            countryResults = new ResultSetCache(results, invoiceParent.getCOUNTRY(), 0, 1, "*");

	            // return the results
	            return (countryResults);
	        } catch (Exception exc)
	        {
	            logger.fatal(exc.toString() + "\n" + sql + "\n");
	            throw exc;
	            
	        } finally	{
				
				stmt.close();
				conn.close();
	        }
	    }
//
	   //
	    /*
	     * inserts or updates the database record
	     */
	    public boolean saveDocument()
	    {
	        /*
	         * inserts or updates the database record
	         */
	        spreadVAT(invoiceParent.getCOUNTRY());
	        if(invoiceParent.getDialogErrorMsgs().isEmpty()){
		        invoiceParent.determineROFInvoice();
	
		        retrieveCreatedDate();
		        invoiceParent.generateUNIQUEREQUESTNUMBER();
	
		        if (invoiceParent.getDOCUMENTMODE().equals(Indexing.ADDMODE))
		        {
		            return insertDocument();
		        } else
		        {
		            return updateDocument();
		        }
	        }else{
	        	return false;
	        }
	    }

	    private boolean insertDocument()
	    {
	        if (!getGCPSProcessInvoice().processInvoice(invoiceParent))
	        {	invoiceParent.getDialogErrorMsgs().add("Error saving invoice");
	            logger.error("Error saving invoice");
	            return false;
	        } else
	        {
	            // remove items from the to be index worklist
	            continueWorkflow(invoiceParent.getCOUNTRY(), invoiceParent.getOBJECTID(), invoiceParent.getTOBEINDEXEDWORKFLOW(), invoiceParent.getDatastore());
	        	
	            invoiceParent.setDIRTYFLAG(Boolean.FALSE);
	           invoiceParent. setDOCUMENTMODE(Indexing.UPDATEMODE);
	            return true;
	        }
	    }

	    private boolean updateDocument()
	    {
	        if (!getGCPSProcessInvoice().processInvoice(invoiceParent))
	        {	invoiceParent.getDialogErrorMsgs().add("Error updating invoice");
	           logger. error("Error updating invoice");
	            return false;
	        } else
	        {
	            invoiceParent.setDIRTYFLAG(Boolean.FALSE);
	            invoiceParent.setDOCUMENTMODE(Indexing.UPDATEMODE);
	            return true;
	        }
	    }

	    /*
	     * retieving the offering letter by offering letter number
	     */
	    public ArrayList selectByOfferingLetterStatement()
	    {
	    	OfferingLetterELS oL=new OfferingLetterELS();
	       
	        oL.setOFFERINGNUMBER(invoiceParent.getOFFERINGLETTERNUMBER());
	        oL.setCOUNTRY(invoiceParent.getCOUNTRY());
	        oL.setGcmsEndpoint(invoiceParent.getGcmsEndpoint());
	        oL.setRdcEndpoint(invoiceParent.getRdcEndpoint());
	        ArrayList offeringLetterList = getGCPSGetOfferingLetter().getOfferingLetter(oL);
	        return offeringLetterList;
	    }

	    /*
	     * retieving the customer by customer number
	     */
	    public ArrayList selectByCustomerStatement(String customerNumber, String errors)
	    {
	        InvoiceELS aInvoiceDM = new InvoiceELS(invoiceParent.getCOUNTRY(),invoiceParent.getDatastore());
	        logger.debug("selectByCustomerStatement Customer :" + customerNumber);
	        aInvoiceDM.setCUSTOMERNUMBER(customerNumber);
	        aInvoiceDM.setCOUNTRY(invoiceParent.getCOUNTRY());
	        logger.debug("inside InvoiceELSDataModel:selectByCustomerStatement");
	        aInvoiceDM.setGcmsEndpoint(invoiceParent.getGcmsEndpoint());
	        aInvoiceDM.setRdcEndpoint(invoiceParent.getRdcEndpoint());
	        ArrayList results=(getGCPSGetCustomer().getCustomer(aInvoiceDM));
	        if(!aInvoiceDM.getDialogErrorMsgs().isEmpty() && errors!=null)
	        	errors=aInvoiceDM.getDialogErrorMsgs().get(0);
	        return results;
	    }

	    public ArrayList selectByCustomerNameStatement(String customerName, String city)
	    {
	        InvoiceELS aInvoiceDM = new InvoiceELS();
	        //aInvoiceDM.setCUSTOMERNAME(invoiceParent.getCUSTOMERNAME());
	        aInvoiceDM.setCUSTOMERNAME(customerName);
	       // aInvoiceDM.setCUSTOMERADDRESS3(invoiceParent.getINSTALLEDCUSTOMERADDRESS3());
	        aInvoiceDM.setCUSTOMERADDRESS3(city);
	        aInvoiceDM.setCOUNTRY(invoiceParent.getCOUNTRY());
	        aInvoiceDM.setGcmsEndpoint(invoiceParent.getGcmsEndpoint());
	        aInvoiceDM.setRdcEndpoint(invoiceParent.getRdcEndpoint());
	        logger.debug("inside InvoiceELSDataModel:selectByCustomerNameStatement");
	        return (getGCPSGetCustomer().getCustomer(aInvoiceDM));
	    }

	    /*
	     * retieving the invoice control data by invoice number / date / country
	     * code
	     */
	    public ArrayList selectByInvoiceStatement() throws Exception
	    {
	        InvoiceELS aIDM = new InvoiceELS();
	        aIDM.setINVOICENUMBER(invoiceParent.getINVOICENUMBER());
	        aIDM.setINVOICEDATE(invoiceParent.getINVOICEDATE());
	        aIDM.setCOUNTRY(invoiceParent.getCOUNTRY());
	        aIDM.setUSERID(invoiceParent.getUSERID());
	        aIDM.setGcmsEndpoint(invoiceParent.getGcmsEndpoint());
	        aIDM.setRdcEndpoint(invoiceParent.getRdcEndpoint());
	        aIDM.generateUNIQUEREQUESTNUMBER();

	        ArrayList results = getGCPSGetInvoice().getInvoices(aIDM);

	        return results;

	    }


	    public void spreadVAT(String country)
	    {
	    	invoiceParent.setCOUNTRY(country);
	    	InvoiceLineItemsELS invoiceLineItemDialog= new InvoiceLineItemsELS(invoiceParent);
	        ArrayList lineItemDataModels = invoiceParent.getLineItems();
	        LineItemELS aLineItemDataModel = null;
	        LineItemELS aNewLineItemDataModel = null;

//	        InvoiceEventController aInvoiceEventController = (InvoiceEventController) getEventController();
//	        LineItemEventController aLineItemEventController = aInvoiceEventController.getLineItemEventController();

	        // account for the vat variance cr 701 only on the add mode since update
	        // will retrieve it
	        boolean vatApplied = false;
	        RegionalBigDecimal value = null;
	        RegionalBigDecimal vatVariance = getDecimal(invoiceParent.getVATAMOUNT());
	        vatVariance = vatVariance.subtract(getDecimal(invoiceParent.getLINEITEMVATAMOUNT()));
	        if (vatVariance.equals(RegionalBigDecimal.ZERO.setScale(2, RegionalBigDecimal.ROUND_HALF_UP)))
	        {
	            vatApplied = true;
	        }

	        int qty = 0;
	        RegionalBigDecimal unitVat = null;

	        for (int i = 0; ((vatApplied == false) && (i < lineItemDataModels.size())); i++)
	        {
	            aLineItemDataModel = ((LineItemELS) lineItemDataModels.get(i));
	            if (vatApplied == false)
	            {
	                unitVat = aLineItemDataModel.getDecimal(aLineItemDataModel.getTOTALVATAMOUNT());
	                if (unitVat.compareTo(RegionalBigDecimal.ZERO) > 0)
	                {
	                    // check the qty if it's > 1 we need to split it out to qty
	                    // 1
	                    qty = aLineItemDataModel.getQUANTITYasInteger().intValue();
	                    if (qty > 1)
	                    {
	                        // make this a qty of 1
	                        aLineItemDataModel.setQUANTITY("1");
	                        aLineItemDataModel.setSUBLINENUMBER("1");
	                        aLineItemDataModel.setTOTALVATAMOUNT(aLineItemDataModel.getVATAMOUNT());
	                        aLineItemDataModel.setEXTENDEDPRICE(aLineItemDataModel.getUNITPRICE());
	                        /**
	                         * Jun 2018
	                         * Evaluate if this is necessary in future, 
	                         * qty can't be >1 since each time user give a qty 
	                         * bigger than 1 in the field it will split the item into
	                         *  those number of items.  
	                         */
	                        // add (qty - 1) number of times to the list
	                        for (int j = 1; j < qty; j++)
	                        {
	                            LineItemELS item = new LineItemELS(country);
	                            
//	                            item.copy(aLineItemDataModel);
	                            lineItemDataModels.add(item);
	                        }
	                        /**
	                         * END
	                         */

	                        // fixup the sequence numbers and tax rounding error
	                        LineItemELSServiceHelper.renumberLineItems(lineItemDataModels);
	                        LineItemELSServiceHelper.spreadTaxRoundingError(invoiceLineItemDialog,lineItemDataModels);

	                        // pull out the vat on the origional
	                        unitVat = GilUtility.getDecimal(aLineItemDataModel.getTOTALVATAMOUNT());
	                    }

	                    // get the vat and add the variance
	                    unitVat = unitVat.add(vatVariance);
	                    if(unitVat.compareTo(RegionalBigDecimal.ZERO)>= 0){
	                    	vatVariance = RegionalBigDecimal.ZERO;
	                    	aLineItemDataModel.setTOTALVATAMOUNT(unitVat.toString());
	                    	aLineItemDataModel.setVATAMOUNT(unitVat.toString());
	                    	vatApplied = true;
	                    }else{
	                    	vatVariance = unitVat;
	                    	aLineItemDataModel.setTOTALVATAMOUNT(RegionalBigDecimal.ZERO.toString());
	                    	aLineItemDataModel.setVATAMOUNT(RegionalBigDecimal.ZERO.toString());
	                    	vatApplied = false;
	                    }
	                }
	            }
	        }

	        if(vatVariance.compareTo(RegionalBigDecimal.ZERO)<0){
	        	//front end
	        	logger.error("Insufficient Line Item VAT Amount to accommodate the VAT Variance");
	        	invoiceParent.getDialogErrorMsgs().add("Insufficient Line Item VAT Amount to accommodate the VAT Variance");
	        	return;
	        }
	        
	        // recalculate to set the balances
	        recalculateUnitAmounts();

	    }
	    
	    public void calculateLineItemTaxAmount(){
	        // get the values
	    	calculateTaxPercentage();
	        RegionalBigDecimal vatPercent = getDecimal(invoiceParent.getUSTAXPERCENT());
	        if(vatPercent != null){
	        	LineItemELS aLineItemDataModel = null;
	        	ArrayList<LineItemELS> lineItemDataModels =invoiceParent.getLineItems();
	        	LineItemELS dummy = new LineItemELS(invoiceParent.getCOUNTRY());
            	
                HashMap<String, RegionalBigDecimal> vatPercentages= new HashMap<String, RegionalBigDecimal>();
    	        List<FormSelect> vatCodes= new ArrayList<FormSelect>();
    	        InvoiceLineItemsELS invoiceLineItemDialog = new InvoiceLineItemsELS(invoiceParent);
    	        dummy.getLineItemDataModelELS().loadSelectVatCodes(vatPercentages, vatCodes, invoiceLineItemDialog);
	            for (int i = 0; i < lineItemDataModels.size(); i++)
	            {
	            	aLineItemDataModel = ((LineItemELS) lineItemDataModels.get(i));
	                aLineItemDataModel.setUSTAXPERCENT(vatPercent.toString());
	                if(vatPercent.equals(0)){
	                	aLineItemDataModel.setVATCODE(CNUtilConstants.NO_TAX); 
	                }else{
	                	aLineItemDataModel.setVATCODE(CNUtilConstants.WITH_TAX); 
	                }
	                               
	                aLineItemDataModel.getLineItemDataModelELS().recalculateVATAmount(vatPercentages);
	            }

	        }       
	        	
	    }
	    
	    public void calculateTaxPercentage(){
	    	
	    	RegionalBigDecimal vatPercent = RegionalBigDecimal.ZERO;
	        RegionalBigDecimal netAmt = getDecimal(invoiceParent.getNETAMOUNT());
	        RegionalBigDecimal totalVatAmt = getDecimal(invoiceParent.getVATAMOUNT());
	        if(totalVatAmt.compareTo(RegionalBigDecimal.ZERO)>0){
	        	vatPercent = totalVatAmt.multiply(oneHundred);
	        	vatPercent = vatPercent.divide(netAmt, 2, RegionalBigDecimal.ROUND_HALF_UP);  
	        	invoiceParent.setUSTAXPERCENT(vatPercent.toString());
	        }
	    }
	    
		public void calculateTotalIGFLineItemAmount(){
			LineItemELS aLineItemDataModel = null;
			BigDecimal totalIGFAmount = BigDecimal.ZERO;
	       	ArrayList<LineItemELS> lineItemDataModels = invoiceParent.getLineItems();
	        for (int i = 0; i < lineItemDataModels.size(); i++)
	        {
	        	aLineItemDataModel = ((LineItemELS) lineItemDataModels.get(i));
	        	if(aLineItemDataModel.getBILLEDTOIGFINDC().equals("Y")){
	        		totalIGFAmount = totalIGFAmount.add(aLineItemDataModel.getUNITPRICEasBigDecimal());
	        	}
	        }
			
			invoiceParent.setIGFBILLEDTOAMOUNTasBigDecimal(totalIGFAmount);
		}
		
	    public void calculateIGFLineItemTaxAmount(){
	        // get the values
	    	calculateIGFTaxPercentage();
	        RegionalBigDecimal vatPercent = getDecimal(invoiceParent.getUSTAXPERCENT());
	         if(vatPercent != null){
	        	LineItemELS aLineItemDataModel = null;
	        	ArrayList<LineItemELS> lineItemDataModels = invoiceParent.getLineItems();
	        	LineItemELS dummy = new LineItemELS(invoiceParent.getCOUNTRY());
            	
                HashMap<String, RegionalBigDecimal> vatPercentages= new HashMap<String, RegionalBigDecimal>();
    	        List<FormSelect> vatCodes= new ArrayList<FormSelect>();
    	        InvoiceLineItemsELS invoiceLineItemDialog = new InvoiceLineItemsELS(invoiceParent);
    	        dummy.getLineItemDataModelELS().loadSelectVatCodes(vatPercentages, vatCodes, invoiceLineItemDialog);
	            for (int i = 0; i < lineItemDataModels.size(); i++)
	            {
	            	aLineItemDataModel = ((LineItemELS) lineItemDataModels.get(i));
	            	if(aLineItemDataModel.getBILLEDTOIGFINDC().equals("Y")){
		                aLineItemDataModel.setUSTAXPERCENT(vatPercent.toString());
		                if(vatPercent.equals(0)){
		                	aLineItemDataModel.setVATCODE(CNUtilConstants.NO_TAX); 
		                }else{
		                	aLineItemDataModel.setVATCODE(CNUtilConstants.WITH_TAX); 
		                }       
		                aLineItemDataModel.getLineItemDataModelELS().recalculateVATAmount(vatPercentages);
	            	}
	            }

	        }       
	        	
	    }
	    
	    public void calculateIGFTaxPercentage(){
	    	
	    	RegionalBigDecimal vatPercent = RegionalBigDecimal.ZERO;
	        RegionalBigDecimal netAmt = getDecimal(invoiceParent.getIGFBILLEDTOAMOUNT());
	        RegionalBigDecimal totalVatAmt = getDecimal(invoiceParent.getTAXESBILLEDTOIGF());
	        if(totalVatAmt.compareTo(RegionalBigDecimal.ZERO)>0){
	        	vatPercent = totalVatAmt.multiply(oneHundred);
	        	vatPercent = vatPercent.divide(netAmt, 2, RegionalBigDecimal.ROUND_HALF_UP);  
	        	invoiceParent.setUSTAXPERCENT(vatPercent.toString());
	        }
	    }
	  
	    
	    public boolean selectByCTSTaxesStatement(){
	    	 return getCTSGetTaxes().getTaxes(invoiceParent);
	    	 
	    }

	    /*
	     * retrieving the invoice control data by the object id
	     */
	    public ArrayList selectByObjectIdStatement() throws Exception
	    {
	        InvoiceELS aIDM = new InvoiceELS();
	        //check i objectid is set in invoiceparent
	        
	        aIDM.setOBJECTID(invoiceParent.getOBJECTID());
	        aIDM.setCOUNTRY(invoiceParent.getCOUNTRY());
	        aIDM.setUSERID(invoiceParent.getUSERID());
	        aIDM.setGcmsEndpoint(invoiceParent.getGcmsEndpoint());
	        aIDM.setRdcEndpoint(invoiceParent.getRdcEndpoint());
	        aIDM.generateUNIQUEREQUESTNUMBER();

	        ArrayList results = getGCPSGetInvoice().getInvoices(aIDM);

	        return results;
	    }

	    /*
	     * retrieving the invoice control data by the object id as well as invoice
	     * number and date
	     */
	    public ArrayList selectMatchingInvoices(String clientObjectId) throws Exception
	    {
	        

	        // search for the invoice number / date
	        ArrayList results = new ArrayList();
	        ArrayList searchResults = null;
	        try
	        {
	            if (invoiceParent.getINVOICENUMBER().trim().length() <= 22)
	            {
	                searchResults = getGCPSGetInvoice().getInvoices(invoiceParent);
	            }
	        } catch (Exception exc)
	        {	
	        	invoiceParent.getDialogErrorMsgs().add("Error while retrieving GCMS Invoice. "+exc.getMessage());
	        	logger.fatal("Error while retrieving GCMS Invoice. "+exc.getMessage());
//	        	throw new GILCNException("Error while retrieving GCMS Invoice. ",exc);

	        }

	        if (searchResults != null)
	        {
	            results.addAll(searchResults);
	        }

	        // see if we got the record with this object id already, if so return
	        // the results
	        for (int i = 0; i < results.size(); i++)
	        {
	            InvoiceELS aRetrievedIDM = (InvoiceELS) results.get(i);
	            if (aRetrievedIDM.getOBJECTID().equals(clientObjectId))
	            {
	                return results;
	            }
	        }

	        // search for the object id
	        invoiceParent.setOBJECTID(clientObjectId);//TODO CHECK this twice
	        invoiceParent.setINVOICENUMBER("");
	        invoiceParent.setINVOICEDATE("");
	        searchResults = null;

	        try
	        {
	            searchResults = getGCPSGetInvoice().getInvoices(invoiceParent);
	        } catch (Exception exc)
	        {
	        	invoiceParent.getDialogErrorMsgs().add("Error while retrieving GCMS Invoice. "+exc.getMessage());
	        	logger.fatal("Error while retrieving GCMS Invoice without invNum. "+exc.getMessage());
	           
	        }

	        if (searchResults != null)
	        {
	            results.addAll(searchResults);
	        }
	        
	       


	        return results;
	    }

	    public void validateAptsInvoice(ArrayList results) throws GILCNException{
	    	
	        Invoice aptsIDM = new Invoice(invoiceParent.getCOUNTRY().equals("GB")?"UK":invoiceParent.getCOUNTRY());
	        aptsIDM.setINVOICENUMBER(invoiceParent.getINVOICENUMBER());
	        aptsIDM.setINVOICEDATE(invoiceParent.getINVOICEDATE());
	        aptsIDM.setCOUNTRY(invoiceParent.getCOUNTRY());
	        aptsIDM.setUSERID(invoiceParent.getUSERID());
	        aptsIDM.setLocale(invoiceParent.getLocale());
//	        if (aptsIDM.getCOUNTRY().equals("GB"))
//	        {
//	            aptsIDM.setCOUNTRY("UK");
//	        }

	        try
	        {
	        	
	            ResultSet aResultSet = aptsIDM.getInvoiceDataModel().selectByInvoiceStatement();
	            while (aResultSet.next())
	            {
	                int i = 1;
	               
	                InvoiceELS aptsInvoiceDataModel = new InvoiceELS();
	                aptsInvoiceDataModel.setINVOICENUMBER(aResultSet.getString(i++));
	                aptsInvoiceDataModel.setINVOICEDATEasDate(aResultSet.getDate(i++));
	                aptsInvoiceDataModel.setOBJECTID(aResultSet.getString(i++));
	                results.add(aptsInvoiceDataModel);
	            }
	        }catch(Exception exc){
	        	invoiceParent.getDialogErrorMsgs().add("Error retrieving Invoice APTS statement. "+exc.getMessage());
		          logger.fatal("Error retrieving Invoice APTS statement. "+ exc.toString());
//		          throw new GILCNException("Error retrieving Invoice APTS statement. ");
	        }
	           
//	        }



	    }
	    /*
	     * recalculates the total invoice amount
	     */
	    public void recalculateTotalAmount()
	    {
	        RegionalBigDecimal net = RegionalBigDecimal.ZERO.setScale(2);
	        RegionalBigDecimal tax = RegionalBigDecimal.ZERO.setScale(2);
	        try
	        {
	            net = new RegionalBigDecimal(invoiceParent.getNETAMOUNT()).setScale(2);
	        } catch (NumberFormatException exc)
	        {
	        }
	        try
	        {
	            tax = new RegionalBigDecimal(invoiceParent.getVATAMOUNT()).setScale(2);
	        } catch (NumberFormatException exc)
	        {
	        }
	        RegionalBigDecimal total = net.add(tax);

	        invoiceParent.setTOTALAMOUNT(total.toString());
	        invoiceParent.setVATAMOUNT(tax.toString());
	        invoiceParent.setNETAMOUNT(net.toString());
	    }

	    /*
	     * recalculates the net invoice amount
	     */
	    public void recalculateNetAmount()
	    {
	        RegionalBigDecimal total = RegionalBigDecimal.ZERO.setScale(2);
	        RegionalBigDecimal tax = RegionalBigDecimal.ZERO.setScale(2);
	        try
	        {
	            total = new RegionalBigDecimal(invoiceParent.getTOTALAMOUNT()).setScale(2);
	        } catch (NumberFormatException exc)
	        {
	        }
	        try
	        {
	            tax = new RegionalBigDecimal(invoiceParent.getVATAMOUNT()).setScale(2);
	        } catch (NumberFormatException exc)
	        {
	        }
	        RegionalBigDecimal net = total.subtract(tax);

	        invoiceParent.setTOTALAMOUNT(total.toString());
	        invoiceParent.setVATAMOUNT(tax.toString());
	        invoiceParent.setNETAMOUNT(net.toString());
	    }
	    
	    /*
	     * recalculates the net invoice amount
	     */
	    public void recalculateOtherStateNetAmount()
	    {
	    	RegionalBigDecimal total = RegionalBigDecimal.ZERO.setScale(2);
	        RegionalBigDecimal tax = RegionalBigDecimal.ZERO.setScale(2);
	        try
	        {
	            total = new RegionalBigDecimal(invoiceParent.getTOTALAMOUNT()).setScale(2);
	        } catch (NumberFormatException exc)
	        {
	        }
	        invoiceParent.setOTAMOUNT(invoiceParent.getVATAMOUNT());
	        invoiceParent.setTOTALAMOUNT(total.toString());
	        invoiceParent.setVATAMOUNT(tax.toString());
	        invoiceParent.setNETAMOUNT(total.toString());

	    }
	    
	    /*
	     * recalculates the Net Amount for multiple billed to entities
	     */
	    public void recalculateMultipleTaxNetAmount()
	    {
	        RegionalBigDecimal total = RegionalBigDecimal.ZERO.setScale(2);
	        RegionalBigDecimal taxIGF = RegionalBigDecimal.ZERO.setScale(2);
	        RegionalBigDecimal net = RegionalBigDecimal.ZERO.setScale(2);        
	        
	        try
	        {
	            total = new RegionalBigDecimal(invoiceParent.getTOTALAMOUNT()).setScale(2);
	        } catch (NumberFormatException exc)
	        {
	        }
	   
	        try
	        {
	        	taxIGF = new RegionalBigDecimal(invoiceParent.getTAXESBILLEDTOIGF()).setScale(2);
	        } catch (NumberFormatException exc)
	        {
	        }
	        
	        net = total.subtract(taxIGF);
	     
	        invoiceParent.setTOTALAMOUNT(total.toString());
	        invoiceParent.setVATAMOUNT(taxIGF.toString());
	        invoiceParent.setNETAMOUNT(net.toString());

	    }    

	    /*
	     * recalculates the total unit price and total unit vat
	     */
	    public void recalculateUnitAmounts()
	    {

	        RegionalBigDecimal vatVariance = RegionalBigDecimal.ZERO.setScale(2);
	        RegionalBigDecimal totalUnitNet = RegionalBigDecimal.ZERO.setScale(2);
	        RegionalBigDecimal totalUnitVat = RegionalBigDecimal.ZERO.setScale(2);
	        RegionalBigDecimal availableUnitNet = RegionalBigDecimal.ZERO.setScale(2);
	        RegionalBigDecimal availableUnitVat = RegionalBigDecimal.ZERO.setScale(2);
	        RegionalBigDecimal extendedUnitPrice = null;
	        RegionalBigDecimal extendedUnitVat = null;
	        RegionalBigDecimal totalAmount = null;
	        RegionalBigDecimal lineItemTotalAmount=null;
	        RegionalBigDecimal lineItemVatAmount=null;
	        
	        boolean isConfigured;
//	        InvoiceDataModel aDataModel= new InvoiceDataModel();
	        
	              
	        RegionalBigDecimal unitVatRounding = RegionalBigDecimal.ZERO;
	    	 	
	    	    
	              
	        for (int i = 0; i < invoiceParent.getLineItems().size(); i++)
	       	{
	            LineItemELS aLineItem = (LineItemELS )invoiceParent.getLineItems().get(i);
	            extendedUnitPrice = aLineItem.getLineItemDataModelELS().getDecimal(aLineItem.getEXTENDEDPRICE());
	            extendedUnitVat = aLineItem.getLineItemDataModelELS().getDecimal(aLineItem.getTOTALVATAMOUNT());
	            isConfigured = aLineItem.getCONFIGUSEDasBool().equals(Boolean.TRUE);
	           
	            
	          
	            if (extendedUnitPrice != null)
	            {
	                totalUnitNet = totalUnitNet.add(extendedUnitPrice);
	                if (!isConfigured)
	                {
	                    availableUnitNet = availableUnitNet.add(extendedUnitPrice);
	                }
	            }
	            
								      	  
	           
	            	
	            if (extendedUnitVat != null)
	            {
	            	
	                totalUnitVat = totalUnitVat.add(extendedUnitVat);
	                if (!isConfigured)
	                {
	                    availableUnitVat = availableUnitVat.add(extendedUnitVat);
	                }
	               // System.out.println("availableUnitVat:"+availableUnitVat);
	                
	            }
	          
	        }
	        
	        invoiceParent.setLINEITEMNETAMOUNT(totalUnitNet.toString());
	        invoiceParent.setLINEITEMVATAMOUNT(totalUnitVat.toString());
	        invoiceParent.setLINEITEMTOTALAMOUNT(totalUnitVat.add(totalUnitNet).toString());
	        invoiceParent.setNETBALANCE(availableUnitNet.toString());
	        invoiceParent.setVATBALANCE(availableUnitVat.toString());
	    }
//	    /**
//	     * @return true if any of the line items are from quote
//	     */
	    public boolean hasQuoteLineItems()
	    {
	        ArrayList lineItems = invoiceParent.getLineItems();
	        LineItemELS lineItem;
	        for (int i = 0; i < lineItems.size(); i++)
	        {
	            lineItem = (LineItemELS) lineItems.get(i);
	            if (lineItem.isFromQuote())
	            {
	                return true;
	            }
	        }

	        return false;
	    }

	    /**
	     * remove any line items that are from the quote
	     */
	    public void removeQuoteLineItems()
	    {
	        ArrayList unitLineItems = invoiceParent.getLineItems();
	        LineItemELS aLineItemDataModel;
	        

	        // remove the old quote units
	        for (int i = unitLineItems.size() - 1; i >= 0; i--)
	        {
	            aLineItemDataModel = (LineItemELS) unitLineItems.get(i);
	            if (aLineItemDataModel.isFromQuote())
	            {
	                unitLineItems.remove(i);
	            }
	        }
	    }
	    
	    public boolean update9TX8TAXLineItem(){
	        ArrayList lineItems = invoiceParent.getLineItems();
	        LineItemELS lineItem;
	        for (int i = 0; i < lineItems.size(); i++)
	        {
	            lineItem = (LineItemELS) lineItems.get(i);
	            if (lineItem.is9TX8TAXLine())
	            {
	                lineItem.setUNITPRICE(invoiceParent.getOTAMOUNT());
	                lineItem.setEXTENDEDPRICE(invoiceParent.getOTAMOUNT());
	                lineItem.setCOUNTRY(invoiceParent.getCOUNTRY());
	                lineItem.setVATCODE(CNUtilConstants.NO_TAX);
	                return true;
	                
	            }
	        }

	        return false;	
	    }
	    
	    public boolean updateCustomer9TX8TAXLineItem(){
	        ArrayList lineItems = invoiceParent.getLineItems();
	        LineItemELS lineItem;
	        for (int i = 0; i < lineItems.size(); i++)
	        {
	            lineItem = (LineItemELS) lineItems.get(i);
	            if (lineItem.is9TX8TAXLine())
	            {
	                lineItem.setUNITPRICE(invoiceParent.getTAXESBILLEDTOCUST());
	                lineItem.setEXTENDEDPRICE(invoiceParent.getTAXESBILLEDTOCUST());
	                lineItem.setCOUNTRY(invoiceParent.getCOUNTRY());
	                lineItem.setVATCODE(CNUtilConstants.NO_TAX);
	                return true;
	                
	            }
	        }

	        return false;	
	    }    
	    
	    /**
	     * check to see if the country is ELS or not
	     */
	    public ResultSet selectELSCountry() throws Exception
	    {

	    	
	        StringBuffer sql = new StringBuffer();
	        String schema = getCountryProperties().getGcmsSchema();
	        CachedRowSetImpl cachedRs = new CachedRowSetImpl();
	        Statement stmt = null;
	        Connection conn = null;
	        
	        // build the sql statement, use append since it is more efficient
	        sql.append("select * from ");
	        sql.append(schema);
	        sql.append("GCPSCTY where GCPSCTYCD = ");
	        sql.append(getSqlString(getIndexingObj().getCOUNTRY()));
	        
	        logger.debug("select ELS country: "+sql.toString()+" country: "+getIndexingObj().getCOUNTRY());
	        // prepare the statement to execute
	        try
	        {
	        	conn = getDataSourceTransactionManager().getDataSource().getConnection();
	        	stmt = conn.createStatement();

	            ResultSet results = stmt.executeQuery(sql.toString());
	            
	            cachedRs.populate(results);

	            // return the results
	            return (cachedRs);
	            
	            
	           
	           
	        }catch (Exception exc)
	        {
	            logger.fatal(exc.toString() + "\n" + sql + "\n");
	            throw exc;
	            
	        } finally	{
				
				stmt.close();
				
	        }
	   
	    }  
       
	  //TIR USRO-R-LFAR-9USLY3 - Not spreading tax on type/model
	    public void calLineItemTaxAmtILIBM(){
	        // get the values
	    	calTaxPerILIBM();
	        RegionalBigDecimal vatPercent = getDecimal(invoiceParent.getUSTAXPERCENT());
	         if(vatPercent != null){
	        	LineItemELS aLineItemDataModel = null;
	        	ArrayList<LineItemELS> lineItemDataModels = invoiceParent.getLineItems();
	        	LineItemELS dummy = new LineItemELS(invoiceParent.getCOUNTRY());
            	
                HashMap<String, RegionalBigDecimal> vatPercentages= new HashMap<String, RegionalBigDecimal>();
    	        List<FormSelect> vatCodes= new ArrayList<FormSelect>();
    	        InvoiceLineItemsELS invoiceLineItemDialog = new InvoiceLineItemsELS(invoiceParent);
    	        dummy.getLineItemDataModelELS().loadSelectVatCodes(vatPercentages, vatCodes, invoiceLineItemDialog);
	            for (int i = 0; i < lineItemDataModels.size(); i++)
	            {
	            	aLineItemDataModel = ((LineItemELS) lineItemDataModels.get(i));
	                aLineItemDataModel.setUSTAXPERCENT(vatPercent.toString());
	                if(vatPercent.equals(0)){
	                	aLineItemDataModel.setVATCODE(CNUtilConstants.NO_TAX); 
	                }else{
	                	aLineItemDataModel.setVATCODE(CNUtilConstants.WITH_TAX); 
	                }
	                aLineItemDataModel.getLineItemDataModelELS().recalculateVATAmount(vatPercentages);
	            }

	        }       
	        	
	    }
	    RegionalBigDecimal oneHundred=new RegionalBigDecimal(100.0);
	  //TIR USRO-R-LFAR-9USLY3 - Not spreading tax on type/model
	    public void calTaxPerILIBM(){
	    	
	    	RegionalBigDecimal vatPercent = RegionalBigDecimal.ZERO;
	        RegionalBigDecimal netAmt = getDecimal(invoiceParent.getNETAMOUNT());
	        RegionalBigDecimal totalVatAmt = getDecimal(invoiceParent.getVATAMOUNT());
	        if(totalVatAmt.compareTo(RegionalBigDecimal.ZERO)>0){
	        	vatPercent = totalVatAmt.multiply(oneHundred);
	        	// 1289894 - Production Issue in GIL MANG - 03/18/2016
	        	vatPercent = vatPercent.divide(netAmt, 10, RegionalBigDecimal.ROUND_HALF_UP);  
	        	invoiceParent.setUSTAXPERCENT(vatPercent.toString());
	        }
	    }
	  //TIR USRO-R-LFAR-9USLY3 - Not spreading tax on type/model
	    public void calIGFLineItemTaxAmtILIBM(){
	    // get the values
	    	calIGFTaxPerILIBM();
	    RegionalBigDecimal vatPercent = getDecimal(invoiceParent.getUSTAXPERCENT());
	     if(vatPercent != null){
	    	LineItemELS aLineItemDataModel = null;
	    	ArrayList<LineItemELS> lineItemDataModels = invoiceParent.getLineItems();
	    	LineItemELS dummy = new LineItemELS(invoiceParent.getCOUNTRY());
        	
            HashMap<String, RegionalBigDecimal> vatPercentages= new HashMap<String, RegionalBigDecimal>();
	        List<FormSelect> vatCodes= new ArrayList<FormSelect>();
	        InvoiceLineItemsELS invoiceLineItemDialog = new InvoiceLineItemsELS(invoiceParent);
	        dummy.getLineItemDataModelELS().loadSelectVatCodes(vatPercentages, vatCodes, invoiceLineItemDialog);
	        for (int i = 0; i < lineItemDataModels.size(); i++)
	        {
	        	aLineItemDataModel = ((LineItemELS) lineItemDataModels.get(i));
	        	if(aLineItemDataModel.getBILLEDTOIGFINDC().equals("Y")){
	                aLineItemDataModel.setUSTAXPERCENT(vatPercent.toString());
	                if(vatPercent.equals(0)){
	                	aLineItemDataModel.setVATCODE(CNUtilConstants.NO_TAX); 
	                }else{
	                	aLineItemDataModel.setVATCODE(CNUtilConstants.WITH_TAX); 
	                }                
	                aLineItemDataModel.getLineItemDataModelELS().recalculateVATAmount(vatPercentages);
	        	}
	        }

	    }       
	    	
	}
	  //TIR USRO-R-LFAR-9USLY3 - Not spreading tax on type/model
		public void calIGFTaxPerILIBM(){
		
		RegionalBigDecimal vatPercent = RegionalBigDecimal.ZERO;
	    RegionalBigDecimal netAmt = getDecimal(invoiceParent.getIGFBILLEDTOAMOUNT());
	    RegionalBigDecimal totalVatAmt = getDecimal(invoiceParent.getTAXESBILLEDTOIGF());
	    if(totalVatAmt.compareTo(RegionalBigDecimal.ZERO)>0){
	    	vatPercent = totalVatAmt.multiply(new RegionalBigDecimal(100.0));
	    	// 1289894 - Production Issue in GIL MANG - 03/18/2016
	    	vatPercent = vatPercent.divide(netAmt, 10, RegionalBigDecimal.ROUND_HALF_UP);  
	    	logger.debug("SET US Tax percentage IL-IBM: "+vatPercent);
	    	invoiceParent.setUSTAXPERCENT(vatPercent.toString());
	    }
	}

//		public void loadindexvalues() {
//				return;
//			}
//
//		public void getIvoiceInfo() {
//				return;
//			}
	    
	    /**
	     * creates a select statement for retrieving the poex codes Creation date:
	     * (3/13/00 11:44:07 AM)
	     * 
	     * @return ResultSetCache
	     * @throws NamingException 
	     */
	    public  ResultSetCache selectPOEXCodesStatement() throws SQLException, NamingException
	    {

//	        if ((POEXCodesResults != null) && (POEXCodesResults.getIndexKey().equals(getIndexingObj().getCOUNTRY())))
//	        {
//	            POEXCodesResults.beforeFirst();
//	            return (POEXCodesResults);
//	        }

	        StringBuffer sql = new StringBuffer();
	        String schema = getCountryProperties().getAptsSchema();
	        PreparedStatement pstmt = null;
	        Connection conn = null;


	        sql.append("select PECCODE, PEPOEX, PEDFTIND, PEDESC from ");
	        sql.append(schema);
	        sql.append("poexmpf where PECCODE = ?");
	        sql.append(" order by PECCODE");

	        try
	        {

	        	 logger.debug("Searching for POEX Codes");
	        	 
	          	conn = getDataSourceTransactionManager().getDataSource().getConnection();
	            pstmt = conn.prepareStatement(sql.toString());
	            pstmt.setString(1, getIndexingObj().getCOUNTRY());
	            ResultSet results =  pstmt.executeQuery();
	        	
	            POEXCodesResults = new ResultSetCache(results,getIndexingObj().getCOUNTRY(), 1, 3, "R");
	            
	            logger.debug("Found poex:"+ POEXCodesResults.size());

	            return (POEXCodesResults);
	        } catch (SQLException exc)
	        {
	        	logger.fatal(exc.toString() + "\n" + sql + "\n");
	            throw exc;
	        } finally	{
				
				pstmt.close();
				conn.close();
				
			}
	    }
	    
	    /**
	     * select the vat variance amount
	     * @throws NamingException 
	     */
	    public ResultSet getVATVarianceStatement() throws SQLException, NamingException
	    {
	    	PreparedStatement pstmt = null;
	        StringBuffer sql = new StringBuffer();
	        CachedRowSetImpl cachedRs = new CachedRowSetImpl();
	        String schema = getCountryProperties().getAptsSchema();
	        Connection conn   = null;

	        sql.append("select VVALWVAR from ");
	        sql.append(schema);
	        sql.append("VATVARPF where VVCNTCODE = ?");


	        try
	        {

	        	conn = getDataSourceTransactionManager().getDataSource().getConnection();
	            pstmt = conn.prepareStatement(sql.toString());
	            pstmt.setString(1, getIndexingObj().getCOUNTRY());
	            
	            ResultSet results =  pstmt.executeQuery();
	            //cachedRs = RowSetProvider.newFactory().createCachedRowSet();
	             cachedRs.populate(results);

	            return (cachedRs);
	            
	        } catch (SQLException exc)
	        {
	            logger.fatal(exc.toString() + "\n" + sql + "\n");
	            throw exc;
	            
	        } finally	{
				
				pstmt.close();
				conn.close();
				
			}
	    }
	    
	    public ResultSet selectDistributorInfoStatement() throws SQLException {

	        StringBuffer sql = new StringBuffer();
	    	PreparedStatement pstmt = null;
	    	CachedRowSetImpl cachedRs = new CachedRowSetImpl();
	        String schema = getCountryProperties().getAptsSchema();
	        Connection conn = null;

	        sql.append("select DISTCTY, DISTNBR, DISTNAME from ");
	        sql.append(schema);
	        sql.append("DISTTBL where DISTCTY = ?");

	        try
	        {
	        	conn = getDataSourceTransactionManager().getDataSource().getConnection();
	            pstmt = conn.prepareStatement(sql.toString());
	            pstmt.setString(1, this.getIndexingObj().getCOUNTRY());
	            ResultSet results =  pstmt.executeQuery();
	           //cachedRs = RowSetProvider.newFactory().createCachedRowSet();
	            cachedRs.populate(results);
	             
	            return (cachedRs);
	            
	        } catch (SQLException exc)
	        {
	            logger.fatal(exc.toString() + "\n" + sql + "\n");
	            throw exc;
	            
	        }	finally {
	        	
	        	pstmt.close();
	        	conn.close();
	        	
	        }
	    } 
	    
	    
	    //Story 1750051 CA GIL changes
	    
	     //recalculates the net invoice amount     
	    public void recalculateCANetAmount()
	    {
	    	String checkBoxbilledtocustomer = invoiceParent.getBilledToCustomer();
	    	
	    	
	        RegionalBigDecimal total = RegionalBigDecimal.ZERO.setScale(2);
	        RegionalBigDecimal tax = RegionalBigDecimal.ZERO.setScale(2);
	        RegionalBigDecimal DefaultTax = RegionalBigDecimal.ZERO.setScale(2);
	        try
	        {
	            total = new RegionalBigDecimal(invoiceParent.getTOTALAMOUNT()).setScale(2);
	        } catch (NumberFormatException exc)
	        {
	        }
	        try
	        {
	            tax = new RegionalBigDecimal(invoiceParent.getVATAMOUNT()).setScale(2);
	        } catch (NumberFormatException exc)
	        {
	        }
	        
	        if (checkBoxbilledtocustomer.equals("Y")){
	        	invoiceParent.setTOTALAMOUNT(total.toString());
	        	invoiceParent.setVATAMOUNT(DefaultTax.toString());
	        	invoiceParent.setNETAMOUNT(total.toString());     
	        }else{
	        	RegionalBigDecimal net = total.subtract(tax);

	        	invoiceParent.setTOTALAMOUNT(total.toString());
	        	invoiceParent.setVATAMOUNT(tax.toString());
	        	invoiceParent.setNETAMOUNT(net.toString());
	        }       
	    }
	    
	    
	    //Story 1750051 CA GIL changes
	    
	    public ResultSetCache selectShiptoProvinceCodesStatement() throws SQLException
	    {

	    	Statement stmt = null;
	        StringBuffer sql = new StringBuffer();
	        CachedRowSetImpl cachedRs = new CachedRowSetImpl();
	        String schema = getCountryProperties().getAptsSchema();
	        Connection conn   = null;
  
	        sql.append("select PT1PROV from ");
	        sql.append(schema);
	        sql.append("PROVTAX1");
	        sql.append(" where PT1DFTFLG= ");
	        sql.append("'*'");
	        sql.append(" order by PT1PROV desc");
	         
	        // prepare the statement to execute
	        try
	        {
	        	conn = getDataSourceTransactionManager().getDataSource().getConnection();
	        	stmt = conn.createStatement();

	            ResultSet results = stmt.executeQuery(sql.toString());
	            return new ResultSetCache(results, getIndexingObj().getCOUNTRY(), 0, 0, "");
	            
	        } catch (SQLException exc)
	        {
	            logger.fatal(exc.toString() + "\n" + sql + "\n");
	            throw exc;
	        }
	    }
	    
	    //End Story 1750051    
	    
}
