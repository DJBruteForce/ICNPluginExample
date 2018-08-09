
package com.ibm.gil.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import javax.naming.NamingException;


import org.springframework.dao.DataAccessException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.ibm.gil.business.Contract;
import com.ibm.gil.business.Invoice;
import com.ibm.gil.util.LoggableStatement;
import com.ibm.gil.util.RegionalBigDecimal;
import com.ibm.gil.util.RegionalDateConverter;
import com.ibm.igf.contentmanager.rmi.ContentManagerImplementation;
import com.ibm.igf.hmvc.DB2;
import com.ibm.igf.hmvc.ResultSetCache;
import com.sun.rowset.CachedRowSetImpl;

/**
 * @author ferandra
 * 
 */

public class InvoiceDataModel extends DataModel  {
	
	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(InvoiceDataModel.class);
	
	public InvoiceDataModel(Invoice invoice)  {
		super(invoice);
		this.invoice = invoice;
	}
    
    private Invoice invoice = null;
    private RegionalBigDecimal VATVariance = RegionalBigDecimal.ZERO;
	private ResultSetCache companyCodesResults= null;
    private ResultSetCache VATCodesResults = null;
    private ResultSetCache billSuffixesResults = null;
    private ResultSetCache POEXCodesResults = null;
    private ResultSetCache countryResults = null;
    private ResultSetCache ocrRequiredResults = null;
    private ResultSetCache currencyResults = null;
    private ResultSetCache invoiceTypesResults = null;
    private ResultSetCache invoiceSuffixesResults = null;	
    
    public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	public RegionalBigDecimal getVATVariance()
    {
        return VATVariance;
    }
	
    public void rollbackIndexing() throws Exception
    {
    	logger.info("Rollback Indexing Started ");
    	
        // load the contract if it exists
        try
        {

            ResultSet results = this.selectByObjectIdStatement();
            if (results.next())
            {
                // restore the old fields
                getInvoice().setINVOICENUMBER(DB2.getString(results, 1));
                String invoiceDate = DB2.getString(results, 2);
                invoiceDate = RegionalDateConverter.convertDate("DB2", "GUI", invoiceDate);
                getInvoice().setINVOICEDATE(invoiceDate);
                getInvoice().setVENDORNAME(DB2.getString(results, 8));
                getInvoice().setACCOUNTNUMBER(DB2.getString(results, 10));
                getInvoice().setCUSTOMERNAME(DB2.getString(results, 11));
                getInvoice().setOFFERINGLETTER((DB2.getString(results, 20)));
            }
        } catch (Exception exc) {
            logger.fatal(exc.toString());
            throw exc;
        } 
        
        this.updateIndexValues();
        
        logger.info("Rollback Indexing Finished ");
    }

	
    /**
     * create a select statement for retieving the invoice control data by
     * invoice number / date / country code Creation date: (3/13/00 11:44:07 AM)
     * 
     * @return ResultSet
     * @throws ParseException
     * @throws IllegalArgumentException
     */
    public ResultSet selectByInvoiceStatement() throws SQLException, IllegalArgumentException, ParseException
    {
        StringBuffer sql = new StringBuffer();
        String schema = getCountryProperties().getAptsSchema();
        Statement stmt = null;
    	CachedRowSetImpl cachedRs = new CachedRowSetImpl();
        Connection conn = null;

        // build the sql statement, use append because it is more efficent
        sql.append("select A.invcinv#, A.invcinvdte,B.invcccmid from ");
        sql.append(schema);
        //sql.append("winvcntl A left join ");
        sql.append("winvcntl A, ");
        sql.append(schema);
        //sql.append("winvcntlc B on B.invcinv#=A.invcinv# and B.invcctycd =
        // A.invcctycd where A.invcinv# = ");
        sql.append("winvcntlc B where A.invcinv# = ");
        sql.append(getSqlString(getInvoice().getINVOICENUMBER()));
        sql.append(" and A.invcinvdte = ");
        String  invoiceDate = RegionalDateConverter.convertDate("GUI", "DB2",this.getInvoice().getINVOICEDATE()) ;
        sql.append(getSqlString(invoiceDate));
        sql.append(" and A.INVCCTYCD = ");
        sql.append(getSqlString(getInvoice().getCOUNTRY()));
        sql.append(" and B.INVCCTYCD = ");
        sql.append(getSqlString(getInvoice().getCOUNTRY()));
        sql.append(" and B.invcinv# = ");
        sql.append(getSqlString(getInvoice().getINVOICENUMBER()));
        sql.append(" and B.invcinvdte = ");
        sql.append(getSqlString(invoiceDate));
       
        // prepare the statement to execute
        try
        {
        	conn = getDataSourceTransactionManager().getDataSource().getConnection();
        	stmt = conn.createStatement();
        	logger.debug("###SQL### "+sql);
        	ResultSet results = stmt.executeQuery(sql.toString());
            //cachedRs = RowSetProvider.newFactory().createCachedRowSet();
            cachedRs.populate(results);
            
           return (cachedRs);
        } catch (SQLException exc)  {
            logger.fatal(exc.toString() + "\n" + sql + "\n");
            throw exc;  
            
        }finally {
        	conn.close();
        	cachedRs = null;
        }
    }
    


    
    /**
     * create a select statement for checking same invoice number/supplier number/date in invoice table
     * @return ResultSet
     * @throws Exception 
     */
    public ResultSet selectCheckBySupplierStatement() throws Exception
    {
        StringBuffer sql = new StringBuffer();
        String schema = getCountryProperties().getAptsSchema();
        Statement stmt = null;
    	CachedRowSetImpl cachedRs = new CachedRowSetImpl();
        Connection conn = null;

        
        // build the sql statement, use append because it is more efficent
        sql.append("select * from ");
        sql.append(schema);
        //sql.append("winvcntl A left join ");
        sql.append("winvcntl A, ");
        sql.append(schema);
        //sql.append("winvcntlc B on B.invcinv#=A.invcinv# and B.invcctycd =
        // A.invcctycd where A.invcinv# = ");
        sql.append("winvcntlc B where A.invcinv# = ");
        sql.append(getSqlString(getInvoice().getINVOICENUMBER()));
        sql.append(" and A.INVCSUPLR# = ");
        sql.append(getSqlString(getInvoice().getVENDORNUMBER()));
        sql.append(" and A.INVCCTYCD = ");
        sql.append(getSqlString(getInvoice().getCOUNTRY()));
        sql.append(" and B.INVCCTYCD = ");
        sql.append(getSqlString(getInvoice().getCOUNTRY()));
        sql.append(" and B.invcinv# = ");
        sql.append(getSqlString(getInvoice().getINVOICENUMBER()));        
        //Fix for Test Defect 1717297	CM8.5-Index Invoice-Non-ELS- Warning Invoice Number/Supplier Number already exists
	       sql.append(" and  a.invcinvdte =  B.INVCINVDTE ");
        //
        sql.append(" and B.invcccmid <> ");
        sql.append(getSqlString(getInvoice().getOBJECTID()));
 

        try
        {
        	conn = getDataSourceTransactionManager().getDataSource().getConnection();
         	stmt = conn.createStatement();
         	logger.debug("###SQL### "+sql);
         	ResultSet results = stmt.executeQuery(sql.toString());
         	cachedRs.populate(results);

            return (cachedRs);
            
        } catch (SQLException exc) {
            logger.fatal(exc.toString() + "\n" + sql + "\n");
            throw exc;
            
        }catch (Exception exc) {
            logger.fatal(exc.toString() + "\n" + sql + "\n");
            throw exc;
            
        } finally	{
			stmt.close();
			conn.close();
			cachedRs=null;
			
        }
    } 

	
    /**
     * sync up content manager with any changes from the gui
     * @throws Exception 
     */
    public void updateIndexValues() throws Exception  {
        try
        {
        	logger.info("Updating Index Values Started");
        	
        	CM = new ContentManagerImplementation(getInvoice().getDatastore());

            String invoiceDate = RegionalDateConverter.convertDate("GUI", "CM", getInvoice().getINVOICEDATE());
            String objectId = getInvoice().getOBJECTID();

            // update the indexes
            String[] fields = { "Invoice Number", "Invoice Date", "Vendor Name", "Account Number", "Customer Name", "Customer Number", "Offering Letter Number" };
            String[] values = { getInvoice().getINVOICENUMBER(), invoiceDate, getInvoice().getVENDORNAME(), getInvoice().getACCOUNTNUMBER(), getInvoice().getCUSTOMERNAME(),getInvoice().getCUSTOMERNUMBER(), getInvoice().getOFFERINGLETTER() };
            logger.info("Fields:" + Arrays.toString(fields));
            logger.info("Values:" + Arrays.toString(values));
            if (objectId.trim().length() > 0)
            {
            	CM.updateDocument(objectId, fields, values, getInvoice().getDatastore());
            }
            
            logger.info("Updating Index Values Finished");
            
        } catch (Exception exc){
            logger.fatal("Cannot update Content Manager index values. Please close the document image, check-in and try again."+ exc.toString());
            throw exc;
        }
    }
	
    public void rollbackCountryIndexing() throws Exception  {
    	
    	
        try
        {
        	ContentManagerImplementation cm = new ContentManagerImplementation(getInvoice().getDatastore());
            String objectId = getInvoice().getOBJECTID();
 
            if (objectId.trim().length() > 0)
            {
            	cm.moveDocument(objectId, "Invoice " + getInvoice().getCOUNTRY(),getInvoice().getDatastore());
            }
        } catch (Exception exc) {
            logger.fatal(exc.toString());
            throw exc;
        }
    }  
	
	
	/**
     * creates an insert statement for persisting the contract invoice details
     * Creation date: (3/13/00 11:44:07 AM)
     * 
     * @throws ParseException
     * @throws IllegalArgumentException
     *  
     */
    public void insertDetailStatement() throws SQLException, IllegalArgumentException, ParseException
    {
    	java.util.Date now = new java.util.Date();
    	StringBuffer sql = new StringBuffer();
        String schema = getCountryProperties().getAptsSchema();
  
        String username = getInvoice().trimUSERID();

        // build the sql statement, use append since it is more efficient
        sql.append("insert into ");
        sql.append(schema);
        sql.append("cntinvd values (");
        sql.append(getSqlString(getInvoice().getCONTRACTNUMBER()));
        sql.append(",");
        sql.append(getSqlString(getInvoice().getCOUNTRY()));
        sql.append(",");
        sql.append(getSqlString(getInvoice().getCOMPANYCODE()));
        sql.append(",");
        sql.append(getSqlString(getInvoice().getVENDORNUMBER()));
        sql.append(",");
        sql.append(getSqlString(getInvoice().getINVOICENUMBER()));
        sql.append(",");
        sql.append(getSqlString(RegionalDateConverter.convertDate("GUI", "DB2", getInvoice().getINVOICEDATE())));
        sql.append(",'C',");
        sql.append(DB2.sqlString(username));
        sql.append(",");
        sql.append(getSqlString(RegionalDateConverter.formatDate("DB2", now)));
        sql.append(",");
        sql.append(getSqlString(RegionalDateConverter.formatTime("DB2", now)));
        sql.append(",");
        sql.append(getSqlString(getInvoice().getDBCR()));
        sql.append(",");
        sql.append(getSqlString(getInvoice().getINVOICETYPE()));
        sql.append(",");
        sql.append(getSqlString(getInvoice().getCURRENCY()));
        sql.append(", 0.00, 0.00, '')");

        try
        {
        	logger.debug("###SQL### "+sql);
        	getGaptsJdbcTemplate().update(sql.toString());
        	
        } catch (DataAccessException exc){
            logger.fatal(exc.toString() + "\n" + sql + "\n");
            throw exc;
        }
    }

    /**
     * update the contract invoice details
     * 
     * @throws ParseException
     * @throws IllegalArgumentException
     */
    public void updateDetailsStatement() throws SQLException, IllegalArgumentException, ParseException
    {
    	java.util.Date now = new java.util.Date();
    	StringBuffer sql = new StringBuffer();
        String schema = getCountryProperties().getAptsSchema();
  

               sql.append("update ");
        sql.append(schema);
        sql.append("cntinvd set CICMPCODE= ");
        sql.append(getSqlString(getInvoice().getCOMPANYCODE()));
        sql.append(", CISUPLR# = ");
        sql.append(getSqlString(getInvoice().getVENDORNUMBER()));
        sql.append(", CIINVDBCR = ");
        sql.append(getSqlString(getInvoice().getDBCR()));
        sql.append(", CIINVTYPE = ");
        sql.append(getSqlString(getInvoice().getINVOICETYPE())); 
        sql.append(", CICURCDE = ");
        sql.append(getSqlString(getInvoice().getCURRENCY()));         
        sql.append(", cilstdate = ");
        sql.append(getSqlString(RegionalDateConverter.formatDate("DB2", now)));
        sql.append(", cilsttime = ");
        sql.append(getSqlString(RegionalDateConverter.formatTime("DB2", now)));
        sql.append(" where ciinv# = ");
        sql.append(getSqlString(getInvoice().getINVOICENUMBER()));
        sql.append(" and ciinvdate = ");
        sql.append(getSqlString(RegionalDateConverter.convertDate("GUI", "DB2", getInvoice().getINVOICEDATE())));
        sql.append(" and CICTYCOD = ");
        sql.append(getSqlString(getInvoice().getCOUNTRY()));
        
         try
        {
        	logger.debug("###SQL### "+sql);
        	getGaptsJdbcTemplate().update(sql.toString());
        	
        } catch (DataAccessException exc) {
            logger.fatal(exc.toString() + "\n" + sql + "\n");
            throw exc;
        }
    }
    
    
  /**
     * create a select statement for retrieving the last invoice entered
     * Creation date: (3/13/00 11:44:07 AM)
     * 
     * @return ResultSet
     */
    public ResultSet selectLastInvoiceEnteredStatement() throws SQLException
    {
        Date now  = new Date();
    	StringBuffer sql = new StringBuffer();
    	PreparedStatement pstmt = null;
    	CachedRowSetImpl cachedRs = new CachedRowSetImpl();
        String schema = getCountryProperties().getAptsSchema();
        Connection conn = null;
        java.sql.Date dt = null;
        

        // clip the username
        String username = trimUSERID(invoice.getUSERID());;

        // build the sql statement, use append because it is more efficient
        sql.append("select invcsupnme, invcinvamt, invcdbcr, invcacct, invceusrnm, invcctycd, invccurr, invcinvtyp, invcsuplr# from ");
        sql.append(schema);
        sql.append("winvcntl where invcuserid = ?");
        //sql.append(DB2.sqlString(username));
        sql.append(" and invclstdat = ?");
       // sql.append(RegionalDateConverter.formatDate("DB2INSERT", now));
        sql.append(" and invcctycd = ?");
       // sql.append(getSqlString(getInvoice().getCOUNTRY()));
        sql.append("order by invclsttim desc");

        // prepare the statement to execute
        try
        {
        	conn = getDataSourceTransactionManager().getDataSource().getConnection();
        	pstmt = new LoggableStatement(conn,sql.toString());
            pstmt.setString(1, username);
            dt = new java.sql.Date(now.getTime());
            pstmt.setDate(2, dt);
            pstmt.setString(3,   getInvoice().getCOUNTRY());
            
            ResultSet results =  pstmt.executeQuery();
           //Recommended way to do it but will only work with Java 7
            //cachedRs = RowSetProvider.newFactory().createCachedRowSet();
            cachedRs.populate(results);

            return (cachedRs);
            
        }  catch (SQLException exc) { 	
			logger.fatal(exc.toString() + "\n"  + ((LoggableStatement)pstmt).getQueryString()+ "\n"  );
			throw exc;
			
		} finally {
			pstmt.close();
			conn.close();
			cachedRs=null;
		}
    }

    /**
     * create an update statement for updating the Invoice control information
     * Creation date: (3/13/00 11:44:07 AM)
     * 
     * @throws ParseException
     * @throws IllegalArgumentException
     */
    public void updateInvoiceStatement() throws SQLException, IllegalArgumentException, ParseException
    {
    	java.util.Date now = new java.util.Date();
    	StringBuffer sql = new StringBuffer();
        String schema = getCountryProperties().getAptsSchema();

        String username = getInvoice().trimUSERID();


        // build the sql, use append because it is more efficient
        sql.append("update ");
        sql.append(schema);
        sql.append("winvcntl set INVCSUPNME = ");
		sql.append(DB2.sqlString(getInvoice().getVENDORNAME()));
		if((getInvoice().getNETEQBAL()!=null && getInvoice().getNETEQBAL().equals("Y")) && getDecimal(getInvoice().getINVVARAMT()).compareTo(RegionalBigDecimal.ZERO)== 0){
			sql.append(", INVCTOTAMT = ");
			sql.append(getInvoice().getTOTALAMOUNT().replace(',', '.'));
			sql.append(", INVCINVAMT = ");
			sql.append(getInvoice().getNETAMOUNT().replace(',', '.'));
			sql.append(", INVCINVBAL = ");
			sql.append(getInvoice().getNETAMOUNT().replace(',', '.'));		
			sql.append(", INVCVATAMT = ");
			sql.append(getInvoice().getVATAMOUNT().replace(',', '.'));
			sql.append(", INVCVATBAL = ");
			sql.append(getInvoice().getVATAMOUNT().replace(',', '.'));
		}
		sql.append(", invcuserid = ");
        sql.append(DB2.sqlString(username));
        sql.append(", invclstdat = ");
        sql.append(DB2.sqlString(RegionalDateConverter.formatDate("DB2", now)));
        sql.append(", invclsttim = ");
        sql.append(DB2.sqlString(RegionalDateConverter.formatTime("DB2", now)));
		sql.append(", INVCDBCR = ");
		sql.append(DB2.sqlString(getInvoice().getDBCR()));
		sql.append(", INVCCRTDT = ");
		sql.append(DB2.sqlString(RegionalDateConverter.formatDate("DB2", now)));
		sql.append(", INVCACCT = ");
		sql.append(DB2.sqlString(getInvoice().getACCOUNTNUMBER()));	
		sql.append(", INVCEUSRNM = ");
		sql.append(DB2.sqlString(getInvoice().getCUSTOMERNAME()));
		sql.append(", INVCCURR = ");
        sql.append(DB2.sqlString(getInvoice().getCURRENCY()));	
		sql.append(", INVCINVTYP = ");	
        sql.append(DB2.sqlString(getInvoice().getINVOICETYPE()));
        sql.append(", INVCSUPLR# = ");
        sql.append(DB2.sqlString(getInvoice().getVENDORNUMBER()));
		sql.append(", INVCPOEX = ");
        sql.append(DB2.sqlString(getInvoice().getPOEXCODE()));	
		sql.append(", INVCVAT = ");		
        sql.append(DB2.sqlString(getInvoice().getVATCODE()));
        sql.append(", INVCNT# = ");
        sql.append(DB2.sqlString(getInvoice().getCONTRACTNUMBER()));		
        sql.append(", INVCOFFER# = ");
        sql.append(DB2.sqlString(getInvoice().getOFFERINGLETTER()));
        sql.append(", INVCOCRKID = ");
        sql.append(DB2.sqlString(getInvoice().getOCR()));
        sql.append(" where invcinv# = ");
        sql.append(DB2.sqlString(getInvoice().getINVOICENUMBER()));
        sql.append(" and invcinvdte = ");
        sql.append(DB2.sqlString(RegionalDateConverter.convertDate("GUI", "DB2", getInvoice().getINVOICEDATE())));
        sql.append(" and INVCCTYCD = ");
        sql.append(DB2.sqlString(getInvoice().getCOUNTRY())  + " WITH UR " );

        try
        {
        	logger.debug("###SQL### "+sql);
        	getGaptsJdbcTemplate().update(sql.toString());
        	
        } catch (DataAccessException exc){
            logger.fatal(exc.toString() + "\n" + sql + "\n");
            throw exc;
        }
        
        sql.setLength(0);
        sql.append("update ");
        sql.append(schema);
        sql.append("winvcntlc set invcinv# = ");
        sql.append(DB2.sqlString(getInvoice().getINVOICENUMBER()));
        sql.append(", invcinvdte= ");
        sql.append((DB2.sqlString(RegionalDateConverter.convertDate("GUI", "DB2", getInvoice().getINVOICEDATE()))));
		sql.append(", INVCCTYCD = ");
        sql.append(DB2.sqlString(getInvoice().getCOUNTRY()));		
        sql.append(", INVCOFFER# = ");
        sql.append(DB2.sqlString(getInvoice().getOFFERINGLETTER()));
        sql.append(", INVCNT# = ");
        sql.append(DB2.sqlString(getInvoice().getCONTRACTNUMBER()));
        sql.append(", INVCMPCDE= ");
        sql.append(DB2.sqlString(getInvoice().getCOMPANYCODE()));
        if(getInvoice().getCOUNTRY().equals("CZ") || getInvoice().getCOUNTRY().equals("HU")){
        	sql.append(", INVCTXDTE = ");
        	sql.append(DB2.sqlString(RegionalDateConverter.convertDate("GUI", "DB2", getInvoice().getTAXSUPPLYDATE())));			
        }else if(getInvoice().getCOUNTRY().equals("PL")&& getInvoice().getTAXSUPPLYDATE()!=null){
        	sql.append(", INVCTXDTE = ");
        	sql.append(DB2.sqlString(RegionalDateConverter.convertDate("GUI", "DB2", getInvoice().getTAXSUPPLYDATE())));			
         // 1504959 - Handling of invoice suffix - MANG 08-24-2016
        }else if (getInvoice().getCOUNTRY().equals("CL") || getInvoice().getCOUNTRY().equals("CO") || getInvoice().getCOUNTRY().equals("MX") || getInvoice().getCOUNTRY().equals("PE")){
        	sql.append(",INVCSUFX=");
        	sql.append(DB2.sqlString(getInvoice().getINVOICESUFFIX()));
        // End 1504959 	
        }else if (getInvoice().getCOUNTRY().equals("AR")){
        	sql.append(", INVCAI=");
        	sql.append(DB2.sqlString(getInvoice().getCAICAE()));
        	sql.append(", INVCAIDUE=");
        	sql.append(DB2.sqlString(RegionalDateConverter.convertDate("GUI", "DB2", getInvoice().getCAICAEDate())));
        //End Story 1692411	
        }
        sql.append(", INVCTXINUM = ");	
        sql.append(DB2.sqlString(getInvoice().getTAXINVOICENUMBER()));		
        sql.append(", INVCDIST= ");
        sql.append(DB2.sqlString(getInvoice().getDISTRIBUTORNUMBER()));        
        sql.append(" where invcccmid = ");
        sql.append(DB2.sqlString(getInvoice().getOBJECTID()));

        try
        {
        	logger.debug("###SQL### "+sql);
        	getGaptsJdbcTemplate().update(sql.toString());
        	
        } catch (DataAccessException exc) {
            logger.fatal(exc.toString() + "\n" + sql + "\n");
            throw exc;
        }
        

    }
    
   
    public void updateInvoiceWithContractStatement() throws Exception
    {
        java.util.Date now = new java.util.Date();
        StringBuffer sql = new StringBuffer();
        StringBuffer sqlc = new StringBuffer();
        String region = "APTS";
        String schema = getDatabaseDriver().getSchema(region,invoice.getCOUNTRY());
        //clip the username
        String username = trimUSERID(invoice.getUSERID());;
        sql.append("update ");
        sql.append(schema);
        sql.append(" winvcntl set INVCNT# = ");
        sql.append(getSqlString(getInvoice().getCONTRACTNUMBER()));
        sql.append(", invcuserid = ");
        sql.append(DB2.sqlString(username));
        sql.append(", invclstdat = ");
        sql.append(getSqlString(RegionalDateConverter.formatDate("DB2", now)));
        sql.append(", invclsttim = ");
        sql.append(getSqlString(RegionalDateConverter.formatTime("DB2", now)));
        sql.append(", INVCEUSRNM = ");
        sql.append(getSqlString(getInvoice().getCUSTOMERNAME()));
        sql.append(", INVCOFFER# = ");
        sql.append(getSqlString(getInvoice().getOFFERINGLETTER()));

        sql.append(" where invcinv# = ");
        sql.append(getSqlString(getInvoice().getINVOICENUMBER()));
        sql.append(" and invcinvdte = ");
        sql.append(DB2.sqlString(RegionalDateConverter.convertDate("GUI", "DB2", getInvoice().getINVOICEDATE())));
        sql.append(" AND INVCCTYCD = ");
        sql.append(getSqlString(getInvoice().getCOUNTRY()));
        
        try
        {
        	logger.debug("###SQL### "+sql);
        	getGaptsJdbcTemplate().update(sql.toString());
        	
        } catch (DataAccessException exc)
        {
            logger.fatal(exc.toString() + "\n" + sql + "\n",exc);
            throw exc;        
         }
        

        sqlc.append("update ");
        sqlc.append(schema);
        sqlc.append("winvcntlc set INVCNT#  = ");
        sqlc.append(getSqlString(getInvoice().getCONTRACTNUMBER()));
        sqlc.append(", INVCOFFER# = ");
        sqlc.append(getSqlString(getInvoice().getOFFERINGLETTER()));

        sqlc.append(" where invcinv# = ");
        sqlc.append(getSqlString(getInvoice().getINVOICENUMBER()));
        sqlc.append(" and invcinvdte = ");
        sqlc.append(getSqlString(RegionalDateConverter.convertDate ("GUI", "DB2", getInvoice().getINVOICEDATE())));
        sqlc.append(" AND INVCCTYCD = ");
        sqlc.append(getSqlString(getInvoice().getCOUNTRY()));

        try
        {
        	logger.debug(sqlc);
        	getGaptsJdbcTemplate().update(sql.toString());
        	
        } catch (DataAccessException exc) {
            logger.fatal(exc.toString() + "\n" + sqlc + "\n",exc);
            throw exc;
        }
    }

    

 /**
     * select the silent coa flag
     * 
     * @throws ParseException
     * @throws IllegalArgumentException
     */
    public ResultSet getSilentCOAStatement() throws SQLException, IllegalArgumentException, ParseException
    {
        StringBuffer sql = new StringBuffer();
    	PreparedStatement pstmt = null;
    	CachedRowSetImpl cachedRs = new CachedRowSetImpl();
        String schema = getCountryProperties().getAptsSchema();
        Connection conn = null;

        // build the sql statement, use append because it is more efficient
        sql.append("select SESCOAIND from ");
        sql.append(schema);
        sql.append("SESCOAPF where SESCTYCOD = ?");
        sql.append(" and SESINV# = ?");
        sql.append(" and SESINVDATE = ?");

        try
        {
        	conn = getDataSourceTransactionManager().getDataSource().getConnection();
        	pstmt = new LoggableStatement(conn,sql.toString());
            pstmt.setString(1, this.getInvoice().getCOUNTRY());
            pstmt.setString(2, this.getInvoice().getINVOICENUMBER());
            pstmt.setDate(3,   DB2.toSqlDate(RegionalDateConverter.parseDate("GUI",getInvoice().getINVOICEDATE())));
            
            ResultSet results =  pstmt.executeQuery();
           //Recommended way to do it but will only work with Java 7
            //cachedRs = RowSetProvider.newFactory().createCachedRowSet();
            cachedRs.populate(results);

            return (cachedRs);
            
        } catch (SQLException exc) {
        	logger.fatal(exc.toString() + "\n"  + ((LoggableStatement)pstmt).getQueryString()+ "\n"  );
            throw exc;
            
        } finally {
			pstmt.close();
			conn.close();
			cachedRs=null;			
		}
    }

    /**
     * insert the silent coa flag
     * 
     * @throws ParseException
     * @throws IllegalArgumentException
     */
    public void insertSilentCOAStatement() throws SQLException, IllegalArgumentException, ParseException
    {
        StringBuffer sql = new StringBuffer();
        String schema = getCountryProperties().getAptsSchema();

        // build the sql statement, use append because it is more efficient
        sql.append("insert into ");
        sql.append(schema);
        sql.append("SESCOAPF values (");
        sql.append(getSqlString(getInvoice().getINVOICENUMBER()));
        sql.append(",");
        sql.append(getSqlString(RegionalDateConverter.convertDate("GUI", "DB2", getInvoice().getINVOICEDATE())));
        sql.append(",");
        sql.append(getSqlString(getInvoice().getCOUNTRY()));
        sql.append(",");
        sql.append(getSqlString(getInvoice().getSILENTCOA()));
        sql.append(")");

            try
            {
            	logger.debug("###SQL### "+sql);
            	getGaptsJdbcTemplate().update(sql.toString());
            	
            } catch (DataAccessException exc){
                logger.fatal(exc.toString() + "\n" + sql + "\n");
                throw exc;
            }
    }

    /**
     * update the silent coa flag
     * 
     * @throws ParseException
     * @throws IllegalArgumentException
     */
    public void updateSilentCOAStatement() throws SQLException, IllegalArgumentException, ParseException
    {
        StringBuffer sql = new StringBuffer();
        String schema = getCountryProperties().getAptsSchema();


        // build the sql statement, use append because it is more efficient
        sql.append("update ");
        sql.append(schema);
        sql.append("SESCOAPF SET SESCOAIND = ");
        sql.append(getSqlString(getInvoice().getSILENTCOA()));
        sql.append(" where SESINV# = ");
        sql.append(getSqlString(getInvoice().getINVOICENUMBER()));
        sql.append(" and SESINVDATE = ");
        sql.append(getSqlString(RegionalDateConverter.convertDate("GUI", "DB2", getInvoice().getINVOICEDATE())));
        sql.append(" and SESCTYCOD = ");
        sql.append(getSqlString(getInvoice().getCOUNTRY()));

        try
        {
        	logger.debug("###SQL### "+sql);
        	getGaptsJdbcTemplate().update(sql.toString());
        	
        } catch (DataAccessException exc) {
            logger.fatal(exc.toString() + "\n" + sql + "\n");
            throw exc;
        }
    }

    /**
     * select the silent coa flag
     */
    public ResultSet selectByObjectIdStatement() throws Exception
    {
        PreparedStatement pstmt = null;
        StringBuffer sql = new StringBuffer();
        String schema = getCountryProperties().getAptsSchema();
        CachedRowSetImpl cachedRs = new CachedRowSetImpl();
        Connection conn = null;

        // build the sql statement, use append because it is more efficient
        //Story 1692411 AR - 2 new fields in GIL to be sent to GAPTS/datasets
        //story 1708793   
        sql.append("select B.invcinv#, B.invcinvdte, B.INVCVATAMT,B.INVCVATBAL,B.INVCINVAMT,B.invcinvbal,B.INVCTOTAMT,B.INVCSUPNME,B.INVCDBCR,B.INVCACCT,B.INVCEUSRNM,B.INVCCURR,B.INVCINVTYP,B.INVCSUPLR#,B.INVCOCRKID,B.INVCPOEX,PEDESC,B.INVCVAT,VMVATDESC, B.INVCOFFER#,B.INVCNT#,A.INVCMPCDE,A.INVCTXDTE, A.invcctycd, A.INVCTXINUM, A.INVCDIST, A.INVCSUFX, A.INVCAI, A.INVCAIDUE  from ");
        //end story 1708793  
        //end Story 1692411 AR - 2 new fields in GIL to be sent to GAPTS/datasets
        sql.append(schema);
        sql.append("winvcntl B left join ");
        sql.append(schema);
        sql.append("poexmpf on PEPOEX = INVCPOEX and PECCODE = ?");
       // sql.append(getSqlString(COUNTRYidx));
        sql.append(" left join ");
        sql.append(schema);
        sql.append("vatmpf on INVCVAT = VMVAT and VMCCODE = ?");
       // sql.append(getSqlString(COUNTRYidx));
        sql.append(" left join ");
        sql.append(schema);
        sql.append("winvcntlc A on A.invcinv#=B.invcinv# AND A.invcinvdte=B.invcinvdte AND A.invcctycd = B.invcctycd where A.invcccmid = ?");
        //sql.append(getSqlString(OBJECTIDidx));

        // prepare the statement to execute
        try
        {

         	conn = getDataSourceTransactionManager().getDataSource().getConnection();
         	pstmt = new LoggableStatement(conn,sql.toString());
            pstmt.setString(1, getInvoice().getCOUNTRY());
            pstmt.setString(2, getInvoice().getCOUNTRY());
            pstmt.setString(3, getInvoice().getOBJECTID());
            ResultSet results =  pstmt.executeQuery();
            cachedRs.populate(results);
           
            return (cachedRs);
            
        } catch (Exception exc)  {
        	logger.fatal(exc.toString() + "\n"  + ((LoggableStatement)pstmt).getQueryString()+ "\n"  );
            throw exc;
            
        } finally {  	
        	pstmt.close();
        	conn.close();
        	cachedRs=null;
        }
    }

    public ResultSetCache selectCurrencyCodesStatement() throws SQLException, NamingException { 	

        StringBuffer sql = new StringBuffer();
        String schema = getCountryProperties().getAptsSchema();
        PreparedStatement pstmt = null;
        Connection conn = null;
        
        sql.append("select distinct cccurr, CCCDFT from ");
        sql.append(schema);
        sql.append("ccccntl where cccntcode = ?");
        sql.append("  order by cccurr");
        
        try
        {

        	conn = getDataSourceTransactionManager().getDataSource().getConnection();
        	pstmt = new LoggableStatement(conn,sql.toString());
            pstmt.setString(1, getIndexingObj().getCOUNTRY());
            
            ResultSet results =  pstmt.executeQuery();
            currencyResults = new ResultSetCache(results,getIndexingObj().getCOUNTRY(), 0, 1, "*");
            
            logger.debug("Currency Codes found:"+ currencyResults.size());
            
            return (currencyResults);
            
        } catch (SQLException exc) {
        	logger.fatal(exc.toString() + "\n"  + ((LoggableStatement)pstmt).getQueryString()+ "\n");
            throw exc;
            
        } finally	{
			pstmt.close();
			conn.close();
			currencyResults=null;
		}
    }
    
    /**
     * creates a select statement for retrieving the poex codes Creation date:
     * (3/13/00 11:44:07 AM)
     * 
     * @return ResultSetCache
     * @throws NamingException 
     */
    public  ResultSetCache selectPOEXCodesStatement() throws SQLException, NamingException
    {

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
        	 
          	conn = getDataSourceTransactionManager().getDataSource().getConnection();
          	pstmt = new LoggableStatement(conn,sql.toString());
            pstmt.setString(1, getIndexingObj().getCOUNTRY());
            
            ResultSet results =  pstmt.executeQuery();
        	
            POEXCodesResults = new ResultSetCache(results,getIndexingObj().getCOUNTRY(), 1, 3, "R");
            
            logger.debug("POEX Codes found:"+ POEXCodesResults.size());

            return (POEXCodesResults);
        } catch (SQLException exc){
        	logger.fatal(exc.toString() + "\n"  + ((LoggableStatement)pstmt).getQueryString()+ "\n"  );
            throw exc;
            
        } finally	{
			pstmt.close();
			conn.close();
			POEXCodesResults=null;
		}
    }
    
    /**
     * creates a select statement for retrieving the invoice types information
     * Creation date: (3/13/07 11:44:07 AM)
     * 
     * @return ResultSetCache
     * @throws NamingException 
     */
    public  ResultSet selectDefaultInvoiceTypesStatement() throws SQLException, NamingException
    {

        StringBuffer sql = new StringBuffer();
        String schema = getCountryProperties().getAptsSchema();
        PreparedStatement pstmt = null;
    	CachedRowSetImpl cachedRs = new CachedRowSetImpl();
        Connection conn = null;

        sql.append("select DIDFINVTYP from ");
        sql.append(schema);
        sql.append("DFTINVTYP");
        sql.append(" where dictycd = ?");

        try
        {
        	conn = getDataSourceTransactionManager().getDataSource().getConnection();
        	pstmt = new LoggableStatement(conn,sql.toString());
            pstmt.setString(1, getIndexingObj().getCOUNTRY());
            ResultSet results =  pstmt.executeQuery();
           // cachedRs = RowSetProvider.newFactory().createCachedRowSet();
            cachedRs.populate(results);

            return cachedRs;
            
        } catch (SQLException exc){
        	logger.fatal(exc.toString() + "\n"  + ((LoggableStatement)pstmt).getQueryString()+ "\n"  );
            throw exc;
            
        } finally	{
        	conn.close();
			pstmt.close();
			cachedRs=null;
		}
    }
    
    /**
     * creates a select statement for retrieving the invoice types information
     * Creation date: (3/13/00 11:44:07 AM)
     * 
     * @return ResultSetCache
     * @throws NamingException 
     */
    public  ResultSetCache selectInvoiceTypesStatement() throws SQLException, NamingException
    {

        StringBuffer sql = new StringBuffer();
        String schema = getCountryProperties().getAptsSchema();
        Connection conn = null;
        Statement stmt = null;
 
        sql.append("select ITINVTYP from ");
        sql.append(schema);
        sql.append("invtyppf");

        try
        {
        	 
         	conn = getDataSourceTransactionManager().getDataSource().getConnection();
         	stmt = conn.createStatement();
         	logger.debug("###SQL### "+sql);
         	ResultSet results = stmt.executeQuery(sql.toString());
			invoiceTypesResults = new ResultSetCache(results, "", 0, 0, "");
            
            logger.debug("Invoice Types found:"+ invoiceTypesResults.size());
           
            return (invoiceTypesResults);
            
        } catch (SQLException exc) {
            logger.fatal(exc.toString() + "\n" + sql + "\n");
            throw exc;
            
        } finally	{
        	conn.close();
        	invoiceTypesResults=null;
		}
    }
    
    /**
     * creates a select statement for retrieving the country info Creation date:
     * (3/13/00 11:44:07 AM)
     * 
     * @return ResultSetCache
     * @throws NamingException 
     */
    public  ResultSetCache selectCountryInfoStatement() throws SQLException, NamingException
    {
     
    	countryResults = null;
        PreparedStatement pstmt = null;
        StringBuffer sql = new StringBuffer();
        String schema = getCountryProperties().getAptsSchema();
        Connection conn = null;

        sql.append("select cccntcode, cccmpcode, cccurr from ");
        sql.append(schema);
        sql.append("ccccntl where cccdft = '*' and cccntcode = ?");

        try
        {

         	conn = getDataSourceTransactionManager().getDataSource().getConnection();
         	pstmt = new LoggableStatement(conn,sql.toString());
            pstmt.setString(1, getIndexingObj().getCOUNTRY());
            ResultSet results =  pstmt.executeQuery();
            countryResults = new ResultSetCache(results,getIndexingObj().getCOUNTRY(), 0, 1, "*");
            logger.debug("Country Info found:"+ countryResults.size());

            return (countryResults);
            
        } catch (SQLException exc) {
        	logger.fatal(exc.toString() + "\n"  + ((LoggableStatement)pstmt).getQueryString()+ "\n"  );
            throw exc;
            
        } finally	{
			pstmt.close();
			conn.close();
			countryResults=null;
		}
    }
    
    
    
    /**
     * 
     * 
     * creates a select statement for retrieving the Company Code information
     * Creation date: (09/25/07 11:44:07 AM)
     * 
     * @return ResultSetCache
     * @throws NamingException 
     */
    public  ResultSetCache selectCompanyCodesStatement() throws SQLException, NamingException {


		StringBuffer sql = new StringBuffer();
        String schema = getCountryProperties().getAptsSchema();
		PreparedStatement pstmt = null;
		Connection conn = null;

		sql.append("select distinct CCCMPCODE, CCCDFT, cccurr from ");
		sql.append(schema);
		sql.append("CCCCNTL where cccntcode = ? ");

		try {			 
	        	conn = getDataSourceTransactionManager().getDataSource().getConnection();
	        	pstmt = new LoggableStatement(conn,sql.toString());
	            pstmt.setString(1, getIndexingObj().getCOUNTRY());
	            ResultSet results =  pstmt.executeQuery();
	            
				companyCodesResults  = new ResultSetCache(results,getIndexingObj().getCOUNTRY(), 0, 1, "*");
	
				 logger.debug("Company Codes found:"+ companyCodesResults.size());
	
				return (companyCodesResults);
			
		} catch (SQLException exc) {
			logger.fatal(exc.toString() + "\n"  + ((LoggableStatement)pstmt).getQueryString()+ "\n"  );
			throw exc;
			
		} finally	{
			pstmt.close();
			conn.close();
			companyCodesResults=null;
		}
	}
    
    /**
     * creates a select statement for retrieving the ocr required flag Creation
     * date: (3/13/00 11:44:07 AM)
     * 
     * @return ResultSetCache
     * @throws NamingException 
     */
    public  ResultSetCache selectOCRRequiredStatement() throws SQLException, NamingException
    {

        PreparedStatement pstmt = null;
        StringBuffer sql = new StringBuffer();
        String schema = getCountryProperties().getAptsSchema();
        Connection conn = null;

        sql.append("select OKCSTATUS, OKCCTYCOD from ");
        sql.append(schema);
        sql.append("OCRKIDCTL where OKCCTYCOD = ?");

        try
        {
        	
        	conn = getDataSourceTransactionManager().getDataSource().getConnection();
        	pstmt = new LoggableStatement(conn,sql.toString());
            pstmt.setString(1, getIndexingObj().getCOUNTRY());
            
            ResultSet results =  pstmt.executeQuery();
            
            ocrRequiredResults = new ResultSetCache(results,getIndexingObj().getCOUNTRY(), 0, 1, "");

            logger.debug("OCR found:" + ocrRequiredResults.size());
            
            return (ocrRequiredResults);
            
        } catch (SQLException exc) {
        	logger.fatal(exc.toString() + "\n"  + ((LoggableStatement)pstmt).getQueryString()+ "\n"  );
        	throw exc;
            
        } finally{
			pstmt.close();
			conn.close();
			ocrRequiredResults=null;
		}
    }
    
    //1504959 - Handling of invoice suffix - MANG 08-04-2016
    public  ResultSetCache selectInvoiceSuffixesStatement() throws SQLException, NamingException {

        PreparedStatement pstmt = null;
        StringBuffer sql = new StringBuffer();
        String schema = getCountryProperties().getAptsSchema();
        Connection conn = null;
      
        
        //Story 1711911 GIL (Regular and MISC) -Read Default Flag on SUFXTBL table
        sql.append("select SUFXCODE, SUFDFTIND from "); 
        //End Story 1711911    
        sql.append(schema);
        sql.append("SUFXTBL");
        sql.append(" where SUFXCTY = ?");


        try
        {
        	
        	conn = getDataSourceTransactionManager().getDataSource().getConnection();
        	pstmt = new LoggableStatement(conn,sql.toString());
            pstmt.setString(1, getIndexingObj().getCOUNTRY());
            
            ResultSet results =  pstmt.executeQuery();
          //Story 1711911 GIL (Regular and MISC) -Read Default Flag on SUFXTBL table
            invoiceSuffixesResults = new ResultSetCache(results,getIndexingObj().getCOUNTRY(), 0, 1, "");
            //End Story 1711911  
            
            logger.debug("Invoice Suffixes found:" + invoiceSuffixesResults.size());

            return (invoiceSuffixesResults);
            
        } catch (SQLException exc) {
        	logger.fatal(exc.toString() + "\n"  + ((LoggableStatement)pstmt).getQueryString()+ "\n"  );
            throw exc;
            
        } finally	{
			pstmt.close();
			conn.close();
			invoiceSuffixesResults=null;
		}
    }
    
    
    //1627912 [GST] New Fields on APTS Index Invoices Screen (GIL) - MANG 01-27-17
    public ResultSetCache selectBillSuffixesStatement() throws SQLException 
    {

        PreparedStatement pstmt = null;
        StringBuffer sql = new StringBuffer();
        String schema = getCountryProperties().getAptsSchema();

        Connection conn = null;
        
        //Story 1699089 India GST:  State table changes for GIL
        sql.append("select ITSTECOD, ITREG from "); 
        //End Story 1699089 
        sql.append(schema);
        sql.append("WIGSTSTAT");
        
        // prepare the statement to execute
        try
        {
        	
        	conn = getDataSourceTransactionManager().getDataSource().getConnection();
        	pstmt = new LoggableStatement(conn,sql.toString());
        	
            ResultSet results =  pstmt.executeQuery();
            billSuffixesResults = new ResultSetCache(results,getIndexingObj().getCOUNTRY(), 0, 1, "");
            
            logger.debug("Bill Suffixes found:" + billSuffixesResults.size());            

            // return the results
            return (billSuffixesResults);
        } catch (SQLException exc) {
        	logger.fatal(exc.toString() + "\n"  + ((LoggableStatement)pstmt).getQueryString()+ "\n"  );
            throw exc;
        }finally {
        	pstmt.close();
			conn.close();
			billSuffixesResults=null;
		}
    }
    //End 1627912
    
    
    
    /**
     * creates a select statement for retrieving the vat codes Creation date:
     * (3/13/00 11:44:07 AM)
     * 
     * @return ResultSetCache
     * @throws NamingException 
     */
    public  ResultSetCache selectVATCodesStatement() throws SQLException, NamingException {

        PreparedStatement pstmt = null;
        StringBuffer sql = new StringBuffer();
        String schema = getCountryProperties().getAptsSchema();
        Connection conn = null;
        
        sql.append("select VMCCODE, VMVAT, VMDFTIND, VMVATDESC, VMVATPCT from ");
        sql.append(schema);
        sql.append("VATMPF where VMCCODE = ?");
        sql.append(" order by VMCCODE");

        try
        {      	
        	conn = getDataSourceTransactionManager().getDataSource().getConnection();
        	pstmt = new LoggableStatement(conn,sql.toString());
            pstmt.setString(1, getIndexingObj().getCOUNTRY());
            
            ResultSet results =  pstmt.executeQuery();
            VATCodesResults = new ResultSetCache(results,getIndexingObj().getCOUNTRY(), 1, 2, "*");
            
            logger.debug("VAT Codes found:" + VATCodesResults.size());
       
            return (VATCodesResults);
            
        } catch (SQLException exc) {
        	logger.fatal(exc.toString() + "\n"  + ((LoggableStatement)pstmt).getQueryString()+ "\n"  );
        	throw exc;   
        } finally {
			pstmt.close();
			conn.close();
			VATCodesResults=null;
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
        	pstmt = new LoggableStatement(conn,sql.toString());
            pstmt.setString(1, getIndexingObj().getCOUNTRY());
            
            ResultSet results =  pstmt.executeQuery();
            //cachedRs = RowSetProvider.newFactory().createCachedRowSet();
             cachedRs.populate(results);

            return (cachedRs);
            
        } catch (SQLException exc) {
        	logger.fatal(exc.toString() + "\n"  + ((LoggableStatement)pstmt).getQueryString()+ "\n"  );
        	throw exc;
            
        } finally	{
			pstmt.close();
			conn.close();
			cachedRs=null;
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
        	pstmt = new LoggableStatement(conn,sql.toString());
            pstmt.setString(1, this.getIndexingObj().getCOUNTRY());
            
            ResultSet results =  pstmt.executeQuery();
           //cachedRs = RowSetProvider.newFactory().createCachedRowSet();
            cachedRs.populate(results);
             
            return (cachedRs);
            
        } catch (SQLException exc) {
        	logger.fatal(exc.toString() + "\n"  + ((LoggableStatement)pstmt).getQueryString()+ "\n"  );
            throw exc; 
        } finally {
        	pstmt.close();
        	conn.close();
        	cachedRs=null;
        }
    } 
    
    
    /**
     * create a select statement for retrieving invoice variance amount by
     * invoice number / date 
     * 
     * @return ResultSet
     * @throws Exception 
     */
    public ResultSet retrieveInvoiceVarianceAmt() throws Exception
    {
        StringBuffer sql = new StringBuffer();
    	PreparedStatement pstmt = null;
    	CachedRowSetImpl cachedRs = new CachedRowSetImpl();
        String schema = getCountryProperties().getAptsSchema();
        Connection conn = null;

        // build the sql statement, use append because it is more efficient
        sql.append("select IVVARTYPE,IVVARAMT from ");
        sql.append(schema);
        sql.append("winvctlvr where IVINV# = ?");
        sql.append(" and IVINVDATE = ?");

        // prepare the statement to execute
        try
        {
        	conn = getDataSourceTransactionManager().getDataSource().getConnection();
        	pstmt = new LoggableStatement(conn,sql.toString());
            String  data = RegionalDateConverter.convertDate("GUI", "DB2",this.getInvoice().getOLDINVOICEDATE()) ;
            pstmt.setString(1, this.getInvoice().getOLDINVOICENUMBER());
            pstmt.setDate(2,    DB2.toSqlDate(data));
            
            ResultSet results =  pstmt.executeQuery();
           //cachedRs = RowSetProvider.newFactory().createCachedRowSet();
            cachedRs.populate(results);
            
            // return the results
            return (cachedRs);
            
        } catch (SQLException exc)  {
        	logger.fatal(exc.toString() + "\n"  + ((LoggableStatement)pstmt).getQueryString()+ "\n"  );
            throw exc;
            
        }  catch (Exception exc)  {
        	logger.fatal(exc.toString() + "\n"  + ((LoggableStatement)pstmt).getQueryString()+ "\n"  );
            throw exc;
            
        } finally {
        	pstmt.close();
        	conn.close();
        	cachedRs=null;
        }
    }
    
    
    /**
     * create a select statement for retieving contract invoice details count by
     * invoice number / date / country code Creation date: (3/13/00 11:44:07 AM)
     * 
     * @return ResultSet
     * @throws Exception 
     */
    public ResultSet selectDetailsCountStatement() throws Exception
    {
        StringBuffer sql = new StringBuffer();
    	PreparedStatement pstmt = null;
    	CachedRowSetImpl cachedRs = new CachedRowSetImpl();
        String schema = getCountryProperties().getAptsSchema();
        Connection conn = null;

        // build the sql statement, use append because it is more efficient
        sql.append("select count(*) from ");
        sql.append(schema);
        sql.append("cntinvd where cictycod = ?");
        sql.append(" and ciinv# = ?");
        sql.append(" and ciinvdate = ?");

        // prepare the statement to execute
        try
        {
        	conn = getDataSourceTransactionManager().getDataSource().getConnection();
        	pstmt = new LoggableStatement(conn,sql.toString());
            pstmt.setString(1, this.getInvoice().getOLDCOUNTRY());
            pstmt.setString(2, this.getInvoice().getOLDINVOICENUMBER());
            String  data = RegionalDateConverter.convertDate("GUI", "DB2",this.getInvoice().getOLDINVOICEDATE()) ;
            pstmt.setDate(3,   DB2.toSqlDate(data));
            
            ResultSet results =  pstmt.executeQuery();
           //TODO GILCN Recommended way to do it but will only work with Java 7
            //cachedRs = RowSetProvider.newFactory().createCachedRowSet();
            cachedRs.populate(results);

            return (cachedRs);
            
        } catch (SQLException exc){
        	logger.fatal(exc.toString() + "\n"  + ((LoggableStatement)pstmt).getQueryString()+ "\n"  );
            throw exc;
            
        } catch (Exception exc) {
        	logger.fatal(exc.toString() + "\n"  + ((LoggableStatement)pstmt).getQueryString()+ "\n"  );
            throw exc;
            
        }finally {
        	pstmt.close();
        	conn.close();
        	cachedRs=null;
        }
    }
    
    //1627923	[GST] Coding Total Tax Amount on APTS Index Invoices Screen (GIL) - MANG 01-27-17
    
    public ResultSet selectINGSTStatement() throws Exception
    {
        StringBuffer sql = new StringBuffer();
    	PreparedStatement pstmt = null;
    	CachedRowSetImpl cachedRs = new CachedRowSetImpl();
        String schema = getCountryProperties().getAptsSchema();
        Connection conn = null;

        // build the sql statement, use append because it is more efficient
        sql.append("select IHBILLTO,IHBILLFRM,IHCGSTAMT,IHSGSTAMT,IHIGSTAMT,IHGSTREG,IHLOAN,IHSHIPTO from ");
        sql.append(schema);
        sql.append("IGSTHDR where IHCTYCOD = ?");
        sql.append(" and IHINV# = ?");
		sql.append(" and IHINVDATE = ?");

        // prepare the statement to execute
        try
        {
        	conn = getDataSourceTransactionManager().getDataSource().getConnection();
        	pstmt = new LoggableStatement(conn,sql.toString());
            pstmt.setString(1, this.getInvoice().getCOUNTRY());
            pstmt.setString(2, this.getInvoice().getOLDINVOICENUMBER());
            pstmt.setDate(3,   DB2.toSqlDate((RegionalDateConverter.convertDate("GUI", "DB2",this.getInvoice().getOLDINVOICEDATE()))));
            
            ResultSet results =  pstmt.executeQuery();
           //Recommended way to do it but will only work with Java 7
            //cachedRs = RowSetProvider.newFactory().createCachedRowSet();
            cachedRs.populate(results);

            return (cachedRs);

        } catch (SQLException exc) {
        	logger.fatal(exc.toString() + "\n"  + ((LoggableStatement)pstmt).getQueryString()+ "\n"  );
            throw exc;
            
        } catch(Exception e){
        	logger.fatal(e.toString() + "\n"  + ((LoggableStatement)pstmt).getQueryString()+ "\n"  );
            throw e;
            
        }   finally {
        	pstmt.close();
        	conn.close();
        }
    }
    
    //End 1627923
    
    
    /**
     * create a select statement for retrieving the silent coa flag Creation
     * date: (3/13/00 11:44:07 AM)
     * 
     * @return ResultSet
     * @throws Exception 
     */
    public ResultSet selectSilentCOAStatement() throws Exception
    {
        StringBuffer sql = new StringBuffer();
    	PreparedStatement pstmt = null;
    	CachedRowSetImpl cachedRs = new CachedRowSetImpl();
        String schema = getCountryProperties().getAptsSchema();
        Connection conn = null;

        // build the sql statement, use append because it is more efficient
        sql.append("select SESCOAIND from ");
        sql.append(schema);
        sql.append("SESCOAPF where SESCTYCOD = ?");
        sql.append(" and SESINV# = ?");
        sql.append(" and SESINVDATE = ?");


        try
        {
        	conn = getDataSourceTransactionManager().getDataSource().getConnection();
        	pstmt = new LoggableStatement(conn,sql.toString());
            pstmt.setString(1, this.getInvoice().getCOUNTRY());
            pstmt.setString(2, this.getInvoice().getINVOICENUMBER());
            pstmt.setDate(3,   DB2.toSqlDate((RegionalDateConverter.convertDate("GUI", "DB2",this.getInvoice().getINVOICEDATE()))));
            
            ResultSet results =  pstmt.executeQuery();
           //Recommended way to do it but will only work with Java 7
            //cachedRs = RowSetProvider.newFactory().createCachedRowSet();
            cachedRs.populate(results);

            return (cachedRs);
            
            } catch (SQLException exc)  {
            	logger.fatal(exc.toString() + "\n"  + ((LoggableStatement)pstmt).getQueryString()+ "\n"  );
	            throw exc;
	            
            } catch(Exception e){
            	logger.fatal(e.toString() + "\n"  + ((LoggableStatement)pstmt).getQueryString()+ "\n"  );
	            throw e;
	            
            }  finally {
            	pstmt.close();
            	conn.close();
            	cachedRs=null;
            }
    }
    



	public void saveDocument() throws Exception  {
    	
        DefaultTransactionDefinition txDef = null;
        TransactionStatus txStatus = null;
        
        try  {
        	
        	logger.info("Saving Document Started");
        	
        	txDef = new DefaultTransactionDefinition();
        	txDef.setIsolationLevel(DefaultTransactionDefinition.ISOLATION_READ_UNCOMMITTED);
         		
        		txStatus = getDataSourceTransactionManager().getTransaction(txDef);
        		
        
                if(!getInvoice().getCOUNTRY().equals(getInvoice().getOLDCOUNTRY()))
                {
                	this.updateInvoiceCountryStatement();
                	this.updateDetailsCountryStatement();
                	
                }
                

                if ((!getInvoice().getOLDINVOICENUMBER().equals(getInvoice().getINVOICENUMBER()) ||
                		!getInvoice().getOLDINVOICEDATE().equals(getInvoice().getINVOICEDATE()) ) &&
                		!getInvoice().getDOCUMENTMODE().equals(Invoice.ADDMODE))
                {
                	this.updateInvoiceDetailsStatement();
                	this.updateContractDetailsStatement();
                	
                    //Story 1761671 When a new date is entered while reindexing a GST invoice, the details previously entered are NOT saved.
                    if(getInvoice().getCOUNTRY().equals("IN") && (getInvoice().getEFEDATE().equals("Y"))){            
                    	this.updateINGSTDetailsStatement(); 
                    	//Story 1762644 INDIA GST:  Update IGSTDTL and IGSTHDR when reindexing
                    	this.updateINGSTTLDetailsStatement();
                    	//End Story 1762644
                    } 
                    //End Story 1761671
                }
        		
                if (getInvoice().getDOCUMENTMODE().equals(Invoice.ADDMODE))
                {
                	this.insertDocument();
                } else
                {
                	this.updateDocument();
                }
                
 
                if (this.isContractDirty())
               
                {
                	this.insertDetails();
                }

        		getDataSourceTransactionManager().commit(txStatus);
        		
        		logger.info("Saving Document Finished");
        
        } catch (Exception exc) {
        	
        	getDataSourceTransactionManager().rollback(txStatus);
            logger.fatal("Error saving Invoice: " + exc.toString());
            throw exc;
            
        }	
    }
    
    private boolean isContractDirty() throws Exception
    {
        if (getInvoice().getCONTRACTNUMBER().trim().equals("MULTIPLE"))
        {
            return false;
        }
        if (getInvoice().getCONTRACTNUMBER().trim().equals(getInvoice().getLASTSAVEDCONTRACTNUMBER()))
        {
           return false;
        }
        if (getInvoice().getCONTRACTNUMBER().trim().length() != 0) {
			try {
				ResultSet results = this.selectDetailsStatement();

				if (results.next()) {

					getInvoice().getDialogErrorMsgs().add("Duplicate contract number entered:" + getInvoice().getCONTRACTNUMBER());
					logger.error("Duplicate contract number entered:" + getInvoice().getCONTRACTNUMBER());

					return false;

				} else {

					return true;
				}
			} catch (Exception ex) {

				logger.fatal( ex.toString() + "\n"+ ex.getMessage() + "\n");
				throw ex;
			}

		}
        return false;
    }
    
    public void updateInvoiceCountryStatement() throws SQLException, IllegalArgumentException, ParseException
    {

        StringBuffer sql = new StringBuffer();
        String schema = getCountryProperties().getAptsSchema();


        // build the sql, use append because it is more efficient
        sql.append("update ");
        sql.append(schema);
        sql.append("winvcntl set INVCCTYCD = ");
        sql.append( DB2.sqlString(getInvoice().getCOUNTRY()));
        sql.append(" where invcinv# = ");
        sql.append( DB2.sqlString(getInvoice().getINVOICENUMBER()));
        sql.append(" and invcinvdte = ");
        sql.append(DB2.sqlString(RegionalDateConverter.convertDate("GUI", "DB2", getInvoice().getOLDINVOICEDATE())));
        sql.append(" and INVCCTYCD = ");
        sql.append( DB2.sqlString(getInvoice().getOLDCOUNTRY()));

        try
        {
        	logger.debug("###SQL### "+sql);
        	getGaptsJdbcTemplate().update(sql.toString());
        	
        	
        } catch (DataAccessException exc)
        {

            logger.fatal(exc.toString() + "\n" + sql + "\n");
            throw exc;
        }
        
        sql.setLength(0);
        sql.append("update ");
        sql.append(schema);
        sql.append("winvcntlc set INVCCTYCD = "); 
        sql.append( DB2.sqlString(getInvoice().getCOUNTRY()));
        sql.append(" where invcccmid = ");
        sql.append( DB2.sqlString(getInvoice().getOBJECTID()));
        
        try
        {
        	logger.debug("###SQL### "+sql);
        	getGaptsJdbcTemplate().update(sql.toString());
        	
        	
        } catch (DataAccessException exc)
        {
            logger.fatal(exc.toString() + "\n" + sql + "\n");
            throw exc;
        }

    } 
    
    
    public void updateInvoiceDetailsStatement() throws SQLException, IllegalArgumentException, ParseException
    {
        StringBuffer sql = new StringBuffer();
        String schema = getCountryProperties().getAptsSchema();

        sql.append("update ");
        sql.append(schema);
        sql.append("winvcntl set invcinv# = ");
        sql.append( DB2.sqlString(getInvoice().getINVOICENUMBER()));
        sql.append(", invcinvdte = ");
        sql.append(DB2.sqlString(RegionalDateConverter.convertDate("GUI", "DB2", getInvoice().getINVOICEDATE())));
        sql.append(" where invcinv# = ");
        sql.append( DB2.sqlString(getInvoice().getOLDINVOICENUMBER()));
        sql.append(" and invcinvdte = ");
        sql.append(DB2.sqlString(RegionalDateConverter.convertDate("GUI", "DB2", getInvoice().getOLDINVOICEDATE())));
        sql.append(" and INVCCTYCD = ");
        sql.append( DB2.sqlString(getInvoice().getCOUNTRY()) );
        
        try
        {
        	logger.debug("###SQL### "+sql);
        	getGaptsJdbcTemplate().update(sql.toString());
        	
        	
        } catch (DataAccessException exc)
        {
            logger.fatal(exc.toString() + "\n" + sql + "\n");
            throw exc;
        }
    }
    
    
    /**
     * update the contract invoice details
     * 
     * @throws ParseException
     * @throws IllegalArgumentException
     */
    public void updateContractDetailsStatement() throws SQLException, IllegalArgumentException, ParseException
    {
        StringBuffer sql = new StringBuffer();
        String schema = getCountryProperties().getAptsSchema();


        // build the sql statement, use append because it is more efficient
        sql.append("update ");
        sql.append(schema);
        sql.append("cntinvd set ciinv# = ");
        sql.append( DB2.sqlString(getInvoice().getINVOICENUMBER()));
        sql.append(", ciinvdate = ");
        sql.append(DB2.sqlString(RegionalDateConverter.convertDate("GUI", "DB2", getInvoice().getINVOICEDATE())));
        sql.append(" where ciinv# = ");
        sql.append( DB2.sqlString(getInvoice().getOLDINVOICENUMBER()));
        sql.append(" and ciinvdate = ");
        sql.append(DB2.sqlString(RegionalDateConverter.convertDate("GUI", "DB2", getInvoice().getOLDINVOICEDATE())));
        sql.append(" and CICTYCOD = ");
        sql.append( DB2.sqlString(getInvoice().getCOUNTRY())  + " with UR ");

        try
        {
        	logger.debug("###SQL### "+sql);
        	getGaptsJdbcTemplate().update(sql.toString());
        	

        } catch (DataAccessException exc)
        {
            logger.fatal(exc.toString() + "\n" + sql + "\n");
            throw exc;
        }
    } 
    
    
    public void updateDetailsCountryStatement() throws SQLException, IllegalArgumentException, ParseException
    {
        StringBuffer sql = new StringBuffer();
        String schema = getCountryProperties().getAptsSchema();

        sql.append("update ");
        sql.append(schema);
        sql.append("cntinvd set CICTYCOD = ");
        sql.append( DB2.sqlString(getInvoice().getCOUNTRY()));
        sql.append(" where ciinv# = ");
        sql.append( DB2.sqlString(getInvoice().getOLDINVOICENUMBER()));
        sql.append(" and ciinvdate = ");
        sql.append(DB2.sqlString(RegionalDateConverter.convertDate("GUI", "DB2", getInvoice().getOLDINVOICEDATE())));
        sql.append(" and CICTYCOD = ");
        sql.append( DB2.sqlString(getInvoice().getOLDCOUNTRY()));

        try
        {
        	getGaptsJdbcTemplate().update(sql.toString());
        	logger.debug("###SQL### "+sql);
        	
        } catch (DataAccessException exc)
        {
            logger.fatal(exc.toString() + "\n" + sql + "\n");
            throw exc;
        }
    }
    
    /**
     * create a select statement for retieving contract invoice details by
     * invoice number / date / country code Creation date: (3/13/00 11:44:07 AM)
     * 
     * @return ResultSet
     * @throws ParseException
     * @throws IllegalArgumentException
     */
    public ResultSet selectDetailsStatement() throws SQLException, IllegalArgumentException, ParseException
    {
        StringBuffer sql = new StringBuffer();
        String schema = getCountryProperties().getAptsSchema();
        PreparedStatement pstmt = null;
    	CachedRowSetImpl cachedRs = new CachedRowSetImpl();
        Connection conn = null;

        // build the sql statement, use append because it is more efficient
        sql.append("select invccmid, ciinv#, CIINVDATE from ");
        sql.append(schema);
        sql.append("cntinvd, ");
        sql.append(schema);
        sql.append("winvcntl where cicntnbr = ?");
        sql.append(" and cictycod = ?");
        sql.append(" and invcinv# = ciinv# and invcinvdte = ciinvdate ");
        sql.append(" and ciinv# = ?");
        sql.append(" and ciinvdate = ?");
        sql.append(" and cisuplr# = ?");
        sql.append(" and cipaymthd = 'C' WITH UR");

        try
        {
        	conn = getDataSourceTransactionManager().getDataSource().getConnection();
        	pstmt = new LoggableStatement(conn,sql.toString());
            pstmt.setString(1, getInvoice().getCONTRACTNUMBER());
            pstmt.setString(2, getInvoice().getCOUNTRY());
            pstmt.setString(3, getInvoice().getINVOICENUMBER()); 
            pstmt.setDate(4, DB2.toSqlDate( RegionalDateConverter.convertDate("GUI", "DB2", getInvoice().getINVOICEDATE())));
            pstmt.setString(5, getInvoice().getVENDORNUMBER());
            
            ResultSet results =  pstmt.executeQuery();
           // cachedRs = RowSetProvider.newFactory().createCachedRowSet();
            cachedRs.populate(results);

            return cachedRs;
            
        } catch (SQLException exc){
        	logger.fatal(exc.toString() + "\n"  + ((LoggableStatement)pstmt).getQueryString()+ "\n"  );
            throw exc;
            
        } finally	{
        	conn.close();
			pstmt.close();
			cachedRs=null;
		}
    }
    
    
    
    /**
     * create an insert statement to presist the invoice into invoice conrtol
     * Creation date: (3/13/00 11:44:07 AM)
     * 
     * @throws ParseException
     * @throws IllegalArgumentException
     */
    public void insertInvoiceStatement() throws SQLException, IllegalArgumentException, ParseException
    {
    	java.util.Date now = new java.util.Date();
    	StringBuffer sql = new StringBuffer();
        String schema = getCountryProperties().getAptsSchema();

        String username = getInvoice().trimUSERID();

        // build the sql statement, use append since it is more efficient
        sql.append("insert into ");
        sql.append(schema);
        sql.append("winvcntl values (");
        sql.append(getSqlString(getInvoice().getINVOICENUMBER()));
        sql.append(",");
        sql.append(getSqlString(RegionalDateConverter.convertDate("GUI", "DB2", getInvoice().getINVOICEDATE(),this.getInvoice().getUserTimezone())));
        sql.append(",");
        sql.append(getSqlString(RegionalDateConverter.formatDate("DB2", getInvoice().getCreatedTimeStampAsDate())));
        sql.append(",");
        sql.append(getSqlString(getInvoice().getVENDORNAME()));
        sql.append(",");
        sql.append(getInvoice().getTOTALAMOUNT().replace(',', '.'));
        sql.append(",");
        sql.append(getInvoice().getNETAMOUNT().replace(',', '.'));
        sql.append(",");
        sql.append(getInvoice().getNETAMOUNT().replace(',', '.'));
        sql.append(",");
        sql.append(getInvoice().getVATAMOUNT().replace(',', '.'));
        sql.append(",");
        sql.append(getInvoice().getVATAMOUNT().replace(',', '.'));
        sql.append(",");
        sql.append(DB2.sqlString(username));
        sql.append(",");
        sql.append(getSqlString(RegionalDateConverter.formatDate("DB2", now, getInvoice().getUserTimezone())));
        sql.append(",");
        sql.append(getSqlString(RegionalDateConverter.formatTime("DB2", now)));
        sql.append(",");
        sql.append(getSqlString(getInvoice().getDBCR()));
        sql.append(",");
        sql.append("''");
        sql.append(",");
        sql.append("''");
        sql.append(",");
        sql.append("''");
        sql.append(",");
        sql.append("''");
        sql.append(",");
        sql.append(getSqlString(RegionalDateConverter.formatDate("DB2", now)));
        sql.append(",");
        sql.append(getSqlString(getInvoice().getACCOUNTNUMBER()));
        sql.append(",");
        sql.append(getSqlString(getInvoice().getCUSTOMERNAME()));
        sql.append(",");
        sql.append(getSqlString(getInvoice().getCOUNTRY()));
        sql.append(",");
        sql.append("''");
        sql.append(",");
        sql.append(getSqlString(getInvoice().getCURRENCY()));
        sql.append(",");
        sql.append("''");
        sql.append(",");
        sql.append(getSqlString(getInvoice().getINVOICETYPE()));
        sql.append(",");
        sql.append(getSqlString(getInvoice().getVENDORNUMBER()));
        sql.append(",");
        sql.append(getSqlString(getInvoice().getPOEXCODE()));
        sql.append(",");
        sql.append(getSqlString(getInvoice().getVATCODE()));
        sql.append(",");
        sql.append(getSqlString(getInvoice().getCONTRACTNUMBER()));
        sql.append(",");
        sql.append("''");
        sql.append(",");
        sql.append(getSqlString(getInvoice().getOFFERINGLETTER()));
        sql.append(",");
        sql.append(getSqlString(getInvoice().getOCR()));
        sql.append(",");
        sql.append("''");
        sql.append(")");

        try
        {
        	logger.debug("###SQL### "+sql);
        	getGaptsJdbcTemplate().update(sql.toString());
        	
        	
        } catch (DataAccessException exc)
        {

            logger.fatal(exc.toString() + "\n" + sql + "\n");
            throw exc;
        }

        sql.setLength(0);
        sql.append("insert into ");
        sql.append(schema);
        sql.append("winvcntlc (INVCINV#,INVCINVDTE,INVCCTYCD,INVCCCMID,INVCOFFER#,INVCNT#,INVCMPCDE,INVCTXINUM,INVCDIST");
        if(getInvoice().getCOUNTRY().equals("CZ") || getInvoice().getCOUNTRY().equals("HU")){
        	sql.append(",INVCTXDTE");
        }else if(getInvoice().getCOUNTRY().equals("PL")&& getInvoice().getTAXSUPPLYDATE()!=null){
        	sql.append(",INVCTXDTE");
        	
        // 1504959 - Handling of invoice suffix - MANG 08-24-2016	
        }else if (getInvoice().getCOUNTRY().equals("CL") || getInvoice().getCOUNTRY().equals("CO") || getInvoice().getCOUNTRY().equals("MX") || getInvoice().getCOUNTRY().equals("PE")){
        	sql.append(",INVCSUFX");
        // End 1504959 	
        //Story 1692411 AR - 2 new fields in GIL to be sent to GAPTS/datasets	
	    }else if (getInvoice().getCOUNTRY().equals("AR")){
	    	sql.append(",INVCAI");
	    	sql.append(",INVCAIDUE");
	    // End Story 1692411
	    }
        sql.append(") values (");
        sql.append(getSqlString(getInvoice().getINVOICENUMBER()));
        sql.append(",");
        sql.append(getSqlString(RegionalDateConverter.convertDate("GUI", "DB2", getInvoice().getINVOICEDATE())));
        sql.append(",");
        sql.append(getSqlString(getInvoice().getCOUNTRY()));
        sql.append(",");
        sql.append(getSqlString(getInvoice().getOBJECTID()));
        sql.append(",");
        sql.append(getSqlString(getInvoice().getOFFERINGLETTER()));
        sql.append(",");
        sql.append(getSqlString(getInvoice().getCONTRACTNUMBER()));
        sql.append(",");
        sql.append(getSqlString(getInvoice().getCOMPANYCODE()));
        sql.append(",");
        sql.append(getSqlString(getInvoice().getTAXINVOICENUMBER())); 
        sql.append(",");
        sql.append(getSqlString(getInvoice().getDISTRIBUTORNUMBER()));       
        
        if(getInvoice().getCOUNTRY().equals("CZ")|| getInvoice().getCOUNTRY().equals("HU")){
        	sql.append(",");
        	sql.append(getSqlString(RegionalDateConverter.convertDate("GUI", "DB2", getInvoice().getTAXSUPPLYDATE())));
        }else if(getInvoice().getCOUNTRY().equals("PL")&& getInvoice().getTAXSUPPLYDATE()!=null){
        	sql.append(",");
        	sql.append(getSqlString(RegionalDateConverter.convertDate("GUI", "DB2", getInvoice().getTAXSUPPLYDATE())));        	
        // 1504959 - Handling of invoice suffix - MANG 08-24-2016
        }else if (getInvoice().getCOUNTRY().equals("CL") || getInvoice().getCOUNTRY().equals("CO") || getInvoice().getCOUNTRY().equals("MX") || getInvoice().getCOUNTRY().equals("PE")){
        	sql.append(",");
        	sql.append(getSqlString(getInvoice().getINVOICESUFFIX()));
	    // End 1504959 	
              //Story 1692411 AR - 2 new fields in GIL to be sent to GAPTS/datasets	
        }else if (getInvoice().getCOUNTRY().equals("AR")){
		    	sql.append(",");
		    	sql.append(getSqlString(getInvoice().getCAICAE()));
		    	sql.append(",");
		    	sql.append(getSqlString(RegionalDateConverter.convertDate("GUI", "DB2", getInvoice().getCAICAEDate())));
    // End Story 1692411
    }
        sql.append(")");
        
        try
        {
        	getGaptsJdbcTemplate().update(sql.toString());
        	logger.debug("###SQL### "+sql);
        	
        } catch (DataAccessException exc)
        {

            logger.fatal(exc.toString() + "\n" + sql + "\n");
            throw exc;
        }
    }
    
    public boolean isContractNumberRecordExists() throws SQLException, IllegalArgumentException, ParseException{
    	
        StringBuffer sql = new StringBuffer();
    	Statement stmt = null;
        String schema = getCountryProperties().getAptsSchema();
        Connection conn = null;
        
        sql.append("select * from ");
        sql.append(schema);
        sql.append("cntinvd where  ciinv#=");
        sql.append(getSqlString(getInvoice().getINVOICENUMBER()));
        sql.append(" AND ciinvdate = ");
        sql.append(getSqlString(DB2.toSqlDate(RegionalDateConverter.parseDate("GUI",getInvoice().getINVOICEDATE()))));
        sql.append(" and cictycod =");
        sql.append(getSqlString(getInvoice().getCOUNTRY()));
        sql.append(" and cicntnbr =");
        sql.append(getSqlString(getInvoice().getCONTRACTNUMBER())+ "   with UR  ");
        
        try
        {
        	conn = getDataSourceTransactionManager().getDataSource().getConnection();
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            logger.debug("##SQL##"+sql);
            ResultSet results =  stmt.executeQuery(sql.toString());
            
            if(!results.next()){
         	   return false; 
            }
            
        } catch (SQLException exc)
        {
            logger.fatal(exc.toString() + "\n" + sql + "\n");
            throw exc;
            
        }	finally {
        	stmt.close();
        	conn.close();
        }
        return true;
    }
  
    
    public void continueWorkflow()
    {
    	try {

    		CM.completeWorkflow(getInvoice().getOBJECTID(), getInvoice().getTOBEINDEXEDWORKFLOW(), getInvoice().getDatastore());
    		
    	} catch (Exception exc) {
           
    		logger.debug("Error continuing workflow process:" + exc.toString());
		}
    }
    

    public void insertDetails()
    {
        try
        {
        	
        	
        	
        	if(getInvoice().getCONTRACTNUMBER() !=null && !"".equals(getInvoice().getCONTRACTNUMBER())){
        		
            // move the invoice into a contract folder
           
        	try {
        		
        		createContractFolder();
        		
        	}catch(Exception e){
        		
        		getInvoice().setValidationErrorIsContractFolderCreated("Error creating contract folder: "+ e.getMessage());
        		logger.error("Error creating contract folder: "+ e.getMessage());
        	}
            
            if (getInvoice().getFOLDERID() == null || getInvoice().getFOLDERID().trim().length() == 0)
            {

            	getInvoice().setValidationIsContractFolderCreated(false);
            	return;

            }
            CM.moveDocumentToFolder(getInvoice().getOBJECTID(), getInvoice().getFOLDERID(),getInvoice().getDatastore());

            // does the contract number match the invoice number
            if (getInvoice().getCONTRACTNUMBER().trim().equals(getInvoice().getINVOICENUMBER().trim()))
            {
                // create a dummy contract and put it in the folder
                byte[][] bytedata = new byte[1][0];
                bytedata[0] = "The contract is the invoice".getBytes();
                String[] mimetypes = { "text/plain" };
                String[] fields = { "Contract Number", "Customer Name", "Customer Number", "Team", "Timestamp", "Source", "User ID" };
                String[] values = { getInvoice().getCONTRACTNUMBER(), getInvoice().getCUSTOMERNAME(), getInvoice().getCUSTOMERNUMBER(), getInvoice().getTEAM(), getInvoice().getTIMESTAMP(), getInvoice().getSOURCE(), getInvoice().getUSERID() };

                String objectId = CM.createDocument("Contract " + getInvoice().getCOUNTRY(), fields, values, bytedata, mimetypes,getInvoice().getDatastore());
                if (objectId == null)
                {
                	
                	getInvoice().setValidationIsContractCreated(false);
                	
                } else
                {
                	getInvoice().setValidationIsContractCreated(true);
                	getInvoice().setCONTRACTID(objectId);
                    CM.moveDocumentToFolder(objectId, getInvoice().getFOLDERID(),getInvoice().getDatastore());
                    createContractToMatch();
                }
            }

            // make sure all the records in cntinvdtl are in the contract folder
            ResultSet results = this.selectDetailsStatement();
            while (results.next())
            {
                CM.moveDocumentToFolder(DB2.getString(results, 1), getInvoice().getFOLDERID(), getInvoice().getDatastore());
            }

            // perform the insert
            insertDetailStatement();

            // update the contract detail count
            getInvoice().setLASTSAVEDCONTRACTNUMBER(getInvoice().getCONTRACTNUMBER());
            getInvoice().setDetailCount(getInvoice().getDetailCount() + 1);
            getInvoice().overrideContractNumber();
        	}
        } catch (Exception exc)
        {

            logger.fatal(exc.toString());
            return;
        }

        getInvoice().setDIRTYFLAG(Boolean.FALSE);
        getInvoice().setDOCUMENTMODE(Invoice.UPDATEMODE);

    }
    
    /**
     * update/insert the silent coa flag
     * 
     * @throws SQLException
     * @throws ParseException
     * @throws IllegalArgumentException
     */
    public void updateSilentCOA() throws IllegalArgumentException, SQLException, ParseException
    {
        ResultSet results = this.getSilentCOAStatement();
        if (results.next())
        {
        	this.updateSilentCOAStatement();
        } else
        {
        	this.insertSilentCOAStatement();
        }
    }
    
    public void insertDocument() throws Exception
    {
        try
        {
            // perform the insert
           insertInvoiceStatement();

            // update the silent coa flag
            updateSilentCOA();
                   
            //1627923	[GST] Coding Total Tax Amount on APTS Index Invoices Screen (GIL) - MANG 01-27-17
            if(getInvoice().getCOUNTRY().equals("IN") && (getInvoice().getEFEDATE().equals("Y"))){        
            	insertINGSTStatement();
            } 
            //End 1627923
            
            // remove items from the to be index worklist
            continueWorkflow();

        } catch (Exception exc)
        {
            
            logger.fatal(exc.toString());
            throw exc;
           
        }

        getInvoice().setDIRTYFLAG(Boolean.FALSE);
        getInvoice().setDOCUMENTMODE(Invoice.UPDATEMODE);

    }
    

    public void createContractToMatch() throws Exception
    {
        try
        {
            Contract contract = new Contract();
            ContractDataModel aContractDataModel = new ContractDataModel(contract);

            aContractDataModel.getContract().setCONTRACTNUMBER(getInvoice().getINVOICENUMBER());
            aContractDataModel.getContract().setCOUNTRY(getInvoice().getCOUNTRY());
            aContractDataModel.getContract().setCURRENCY(getInvoice().getCURRENCY());
            aContractDataModel.getContract().setAMOUNT(getInvoice().getNETAMOUNT());
            aContractDataModel.getContract().setUSERID(getInvoice().getUSERID());
            aContractDataModel.getContract().setOFFERINGLETTER(getInvoice().getOFFERINGLETTER());
            aContractDataModel.getContract().setCUSTOMERNAME(getInvoice().getCUSTOMERNAME());
            aContractDataModel.getContract().setCUSTOMERNUMBER(getInvoice().getCUSTOMERNUMBER());
            aContractDataModel.getContract().setCREATEDATE(getInvoice().getCREATEDATE());
            aContractDataModel.getContract().setOBJECTID(getInvoice().getCONTRACTID());
            aContractDataModel.getContract().setFOLDERID(getInvoice().getFOLDERID());

            // check if the contract exists allready
            ResultSet results = aContractDataModel.selectByContractStatement();
            if (results.next())
            {
                //TODO GILCN Treat theses error message with maybe a business exception
            	//error("Duplicate contract number");
            	getInvoice().setValidationDuplicateContract(true);
                return;
            }

            // perform the insert
            aContractDataModel.insertContractStatement();
        } catch (Exception exc)
        {
            
            logger.debug(exc.toString());
            throw exc;
        }
    }
    
    public void createContractFolder() throws Exception
    {
    	
    	
        // create contract folder if it doesn't exist allready
        String[] fields = { "Contract Number" };;
        String[] values = { getInvoice().getCONTRACTNUMBER() };
        String folderXML = CM.executeQuery("Contract Folder " + getInvoice().getCOUNTRY(), fields, values,getInvoice().getDatastore());
        String folderId = getInvoice().getObjectId(folderXML);

        String[] s = new String[0];
        ArrayList newfields = new ArrayList();
        ArrayList newvalues = new ArrayList();

        // set required fields
        newfields.add("Contract Number");
        newvalues.add(getInvoice().getCONTRACTNUMBER());

        copyAttributes(getInvoice().getOBJECTID(), getInvoice().getINDEXCLASS(), folderId, "Contract Folder " + getInvoice().getCOUNTRY(), newfields, newvalues, getInvoice().getCOUNTRY(),getInvoice().getDatastore());

        if (folderId == null || folderId.trim().length() == 0)
        {
            folderId = CM.createFolder("Contract Folder " + getInvoice().getCOUNTRY(), (String[]) (newfields.toArray(s)), (String[]) (newvalues.toArray(s)), getInvoice().getDatastore());
        } else
        {
            CM.updateDocument(folderId, (String[]) (newfields.toArray(s)), (String[]) (newvalues.toArray(s)),this.getInvoice().getDatastore());
        }

        getInvoice().setFOLDERID(folderId);
    }
    
    public void updateDocument() throws Exception
    {
        try
        {
            // allready exists, update it
        	this.updateInvoiceStatement();
         // Story 1259368 - Gil Code for Contract/Invoice Relationship
            //if(getInvoice().getCONTRACTNUMBER()!= null && !"".equals(getInvoice().getCONTRACTNUMBER()) && getInvoice().getCONTRACTNUMBER().equals(getInvoice().getLASTSAVEDCONTRACTNUMBER()) && !this.isContractNumberRecordExists()){
        	getInvoice().setContractNumberRecordExists(this.isContractNumberRecordExists());
        	if(getInvoice().getCONTRACTNUMBER()!= null && !"".equals(getInvoice().getCONTRACTNUMBER()) && getInvoice().getCONTRACTNUMBER().equals(getInvoice().getLASTSAVEDCONTRACTNUMBER()) && !getInvoice().isContractNumberRecordExists()){
        	this.insertDetailStatement();	
            }
            
           if((getInvoice().getNETEQBAL()!=null && getInvoice().getNETEQBAL().equals("Y")) && getInvoice().getCONTRACTNUMBER()!= null && !"".equals(getInvoice().getCONTRACTNUMBER())){
        	   this.updateDetailsStatement();
           }
						            
            // update the silent coa flag
            updateSilentCOA();
            
            //1627923	[GST] Coding Total Tax Amount on APTS Index Invoices Screen (GIL) - MANG 01-27-17
            if(getInvoice().getCOUNTRY().equals("IN") && (getInvoice().getEFEDATE().equals("Y"))){            
            	updateINGSTStatement();
            } 
            //End 1627923

        } catch (Exception exc)
        {
            logger.fatal(exc.toString());
            exc.printStackTrace();
            throw exc;
        }

        getInvoice().setDIRTYFLAG(Boolean.FALSE);

    }

    public ResultSet selectInvoiceStatement()  throws Exception{
       
    	StringBuffer sql = new StringBuffer();
        String schema = getCountryProperties().getAptsSchema();
        Statement stmt = null;
    	CachedRowSetImpl cachedRs = new CachedRowSetImpl();
        Connection conn = null;

        // build the sql statement, use append because it is more efficient
        sql.append("select invcinv#, invcinvdte, INVCEUSRNM from ");
        sql.append(schema);
        sql.append("winvcntl ");
        sql.append("where INVCOFFER# = ");
        sql.append(getSqlString(getInvoice().getOFFERINGLETTER()));
        sql.append(" and invcctycd = ");
        sql.append(getSqlString(getInvoice().getCOUNTRY()));
         // prepare the statement to execute
        try
        {
        	conn = getDataSourceTransactionManager().getDataSource().getConnection();
        	stmt = conn.createStatement();
        	logger.debug("###SQL### " + sql);
        	ResultSet results = stmt.executeQuery(sql.toString());
            //cachedRs = RowSetProvider.newFactory().createCachedRowSet();
            cachedRs.populate(results);
            // return the results
            return (cachedRs);
        } catch (SQLException exc){
            logger.fatal(exc.toString() + "\n" + sql + "\n");
            throw exc;
        } catch (Exception exc){
            logger.fatal(exc.toString() + "\n" + sql + "\n");
            throw exc;
        } finally {
        	conn.close();
        } 
    }
    
    
    
    //Story 1761671 When a new date is entered while reindexing a GST invoice, the details previously entered are NOT saved.
    public void updateINGSTDetailsStatement() throws SQLException, IllegalArgumentException, ParseException
    {
    	StringBuffer sql = new StringBuffer();
        String schema = getCountryProperties().getAptsSchema();
        
        // clip the username
        String username = getInvoice().trimUSERID();

        // build the sql, use append because it is more efficient
        sql.append("update ");
        sql.append(schema);
        sql.append(" IGSTHDR set IHINV# = ");
        sql.append(getSqlString(getInvoice().getINVOICENUMBER()));
		sql.append(", IHINVDATE = ");
		sql.append(DB2.sqlString(RegionalDateConverter.convertDate("GUI", "DB2", getInvoice().getINVOICEDATE())));      
        sql.append(" where  IHINV# = ");
        sql.append(getSqlString(getInvoice().getOLDINVOICENUMBER()));
        sql.append(" and IHINVDATE = ");
        sql.append(DB2.sqlString(RegionalDateConverter.convertDate("GUI", "DB2", getInvoice().getOLDINVOICEDATE())));
        sql.append(" and IHCTYCOD = ");
        sql.append(getSqlString(getInvoice().getCOUNTRY()));
        
        try
        {
        	logger.debug("###SQL### " + sql);
        	getGaptsJdbcTemplate().update(sql.toString());
        	
        	
        } catch (DataAccessException exc)
        {
            logger.fatal(exc.toString() + "\n" + sql + "\n");
            throw exc;
        }
    }
    //End Story 1761671
    
    //Story 1762644 INDIA GST:  Update IGSTDTL and IGSTHDR when reindexing
    public void updateINGSTTLDetailsStatement() throws SQLException, IllegalArgumentException, ParseException
    {
        Date now = new Date();
    	StringBuffer sql = new StringBuffer();
        String schema = getCountryProperties().getAptsSchema();
        
        // clip the username
        String username = getInvoice().trimUSERID();

        // build the sql, use append because it is more efficient
        sql.append("update ");
        sql.append(schema);
        sql.append(" IGSTDTL set IDINV# = ");
        sql.append(getSqlString(getInvoice().getINVOICENUMBER()));
		sql.append(", IDINVDATE = ");
		sql.append(DB2.sqlString(RegionalDateConverter.convertDate("GUI", "DB2", getInvoice().getINVOICEDATE())));      
        sql.append(" where  IDINV# = ");
        sql.append(getSqlString(getInvoice().getOLDINVOICENUMBER()));
        sql.append(" and IDINVDATE = ");
        sql.append(DB2.sqlString(RegionalDateConverter.convertDate("GUI", "DB2", getInvoice().getOLDINVOICEDATE())));
        sql.append(" and IDCTYCOD = ");
        sql.append(getSqlString(getInvoice().getCOUNTRY()));
        
        try
        {
        	logger.debug("###SQL### " + sql);
        	getGaptsJdbcTemplate().update(sql.toString());
        	
        	
        } catch (DataAccessException exc)
        {
            logger.fatal(exc.toString() + "\n" + sql + "\n");
            throw exc;
        }
    }
    
    
    
    //1627923	[GST] Coding Total Tax Amount on APTS Index Invoices Screen (GIL) - MANG 01-27-17
    public void insertINGSTStatement() throws SQLException, IllegalArgumentException, ParseException
    {
        Date now = new Date();
    	StringBuffer sql = new StringBuffer();
        String schema = getCountryProperties().getAptsSchema();
        //retrieveCreatedDate();

        // clip the username
        String username = getInvoice().trimUSERID();

     // build the sql statement, use append since it is more efficient
        sql.append("insert into ");
        sql.append(schema);  
        sql.append("IGSTHDR (IHINV#,IHINVDATE,IHCTYCOD,IHBILLFRM,IHBILLTO,IHSHIPTO,IHLOAN");
        sql.append(",IHCGSTAMT,IHCGSTBAL,IHSGSTAMT,IHSGSTBAL,IHIGSTAMT,IHIGSTBAL,IHGSTREG");
        sql.append(") values (");    
        sql.append(getSqlString(getInvoice().getINVOICENUMBER()));
        sql.append(",");
        sql.append(DB2.sqlString(RegionalDateConverter.convertDate("GUI", "DB2", getInvoice().getINVOICEDATE())));
        sql.append(",");
        sql.append(getSqlString(getInvoice().getCOUNTRY()));
        sql.append(",");
        sql.append(getSqlString(getInvoice().getBILLFROM()));
        sql.append(",");
        sql.append(getSqlString(getInvoice().getBILLTO()));
        sql.append(",");
        sql.append(getSqlString(getInvoice().getSHIPTO()));
        sql.append(",");
        sql.append(getSqlString(getInvoice().getLOAN()));
        sql.append(",");
        sql.append(getSqlString(getInvoice().getINCGST()));
        sql.append(",");
        sql.append(getSqlString(getInvoice().getINCGST()));
        sql.append(",");
        sql.append(getSqlString(getInvoice().getINSGST()));
        sql.append(",");
        sql.append(getSqlString(getInvoice().getINSGST())); 
        sql.append(",");
        sql.append(getSqlString(getInvoice().getINIGST()));
        sql.append(",");
        sql.append(getSqlString(getInvoice().getINIGST()));  
        sql.append(",");
        sql.append(getSqlString(getInvoice().getSUPPGSTREGNUMBER()));
        sql.append(")");

        try
        {
        	logger.debug("###SQL### " + sql);
        	getGaptsJdbcTemplate().update(sql.toString());
        	
        	
        } catch (DataAccessException exc)
        {
            logger.fatal(exc.toString() + "\n" + sql + "\n");
            throw exc;
        }
    }
    //End 1627923  
    
    
    
    //1627923	[GST] Coding Total Tax Amount on APTS Index Invoices Screen (GIL) - MANG 01-27-17
    public void updateINGSTStatement() throws SQLException, IllegalArgumentException, ParseException
    {
    	StringBuffer sql = new StringBuffer();
        String schema = getCountryProperties().getAptsSchema();
       // retrieveCreatedDate();

        // clip the username
        String username = getInvoice().trimUSERID();

        // build the sql, use append because it is more efficient
        sql.append("update ");
        sql.append(schema);
        sql.append(" IGSTHDR set IHINV# = ");
        sql.append(getSqlString(getInvoice().getINVOICENUMBER()));
		sql.append(", IHINVDATE = ");
		sql.append(DB2.sqlString(RegionalDateConverter.convertDate("GUI", "DB2", getInvoice().getINVOICEDATE())));
        sql.append(", IHCTYCOD = ");
        sql.append(getSqlString(getInvoice().getCOUNTRY()));
        sql.append(", IHBILLFRM = ");
        sql.append(getSqlString(getInvoice().getBILLFROM()));
		sql.append(", IHBILLTO = ");
		sql.append(getSqlString(getInvoice().getBILLTO()));
		sql.append(", IHSHIPTO = ");
		sql.append(getSqlString(getInvoice().getSHIPTO()));
		sql.append(", IHLOAN = ");
		sql.append(getSqlString(getInvoice().getLOAN()));
		sql.append(", IHCGSTAMT = ");
		sql.append(getSqlString(getInvoice().getINCGST()));
		sql.append(", IHCGSTBAL = ");
		sql.append(getSqlString(getInvoice().getINCGST()));
		sql.append(", IHSGSTAMT = ");
		sql.append(getSqlString(getInvoice().getINSGST()));
		sql.append(", IHSGSTBAL = ");	
		sql.append(getSqlString(getInvoice().getINSGST()));
		sql.append(", IHIGSTAMT = ");
		sql.append(getSqlString(getInvoice().getINIGST()));
		sql.append(", IHIGSTBAL = ");
		sql.append(getSqlString(getInvoice().getINIGST()));
		sql.append(", IHGSTREG = ");
		sql.append(getSqlString(getInvoice().getSUPPGSTREGNUMBER()));
		sql.append(" where IHCTYCOD = ");
		sql.append(getSqlString(getInvoice().getCOUNTRY()));
		sql.append(" and IHINV# = ");
		sql.append(getSqlString(getInvoice().getINVOICENUMBER()));
		sql.append(" and IHINVDATE = ");
		sql.append(DB2.sqlString(RegionalDateConverter.convertDate("GUI", "DB2", getInvoice().getINVOICEDATE())));
		
        try
        {
        	logger.debug("###SQL### " + sql);
        	getGaptsJdbcTemplate().update(sql.toString());
        	
        	
        } catch (DataAccessException exc)
        {
            logger.fatal(exc.toString() + "\n" + sql + "\n");
            throw exc;
        }
    }
    
    

}