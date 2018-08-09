package com.ibm.gil.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


import org.springframework.dao.DataAccessException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.ibm.gil.business.Contract;
import com.ibm.gil.business.ContractInvoice;
import com.ibm.gil.business.Indexing;
import com.ibm.gil.business.Invoice;
import com.ibm.gil.util.LoggableStatement;
import com.ibm.gil.util.RegionalDateConverter;
import com.ibm.igf.contentmanager.rmi.ContentManagerImplementation;
import com.ibm.igf.hmvc.DB2;
import com.ibm.igf.hmvc.ResultSetCache;
import com.sun.rowset.CachedRowSetImpl;


public class ContractDataModel extends DataModel {

	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(InvoiceDataModel.class);
	
	public ContractDataModel(Contract contract)  {
		
		super(contract);
		this.contract = contract;	
	}
    
    private Contract contract = null;
     
    public Contract getContract() {
		return contract;
	}
    
	public void setContract(Contract contract) {
		this.contract = contract;
	}
    
    //Story 1768398 GIL - additional change for GCMS JR
    //private static transient ResultSetCache fieldhybridResults = null;
    //End Story 1768398 
    
    public void rollbackIndexing() throws Exception
    {
    	
    	logger.info("Rolling back Indexing Started");
    	
        // load the contract if it exists
        try
        {
            // CR GIP00000061 08/14/2006
            ResultSet results = this.selectByObjectIdStatement();
            if (results.next())
            {
                // restore the old fields
                getContract().setCONTRACTNUMBER(DB2.getString(results, 1));
                getContract().setCUSTOMERNAME(DB2.getString(results, 5));
                getContract().setCUSTOMERNUMBER(DB2.getString(results, 6));
                getContract().setOFFERINGLETTER(DB2.getString(results, 7));
               
            }
        } catch (Exception exc)
        {
            logger.fatal("Error retrieving document's database values:" + exc.toString());
            throw exc;
        }
        this.updateIndexValuesForRollback();
        
        logger.info("Rolling back Indexing Finished");
    }
    

    public void updateFoldering () throws Exception {

       try {
		 	    ContentManagerImplementation cm  = new ContentManagerImplementation(getContract().getDatastore());
		 	   
		 	    // move the contract into a contract folder
		        this.createContractFolder();
		        if (getContract().getFOLDERID() == null || getContract().getFOLDERID().trim().length() == 0)
		        {
		        	getContract().setValidationIsContractFolderCreated(false);
		            return;
		        }
		        cm.moveDocumentToFolder(getContract().getOBJECTID(), getContract().getFOLDERID(), getContract().getDatastore() );
		        this.updateFolderIdStatement();
		
		        // make sure all the records in cntinvdtl are in the contract folder
		        ResultSet results = this.selectDetailsStatement();
		        while (results.next())
		        {
		     	   cm.moveDocumentToFolder(DB2.getString(results, 1), getContract().getFOLDERID(), getContract().getDatastore());
		        }
	        
	       }catch(Exception e){
	    	   logger.error(e,e);
	        	getContract().setValidationIsContractFolderCreated(false);
	            return;
	       }
        
    }
	
	   public void insertDocument() throws Exception
	   {
	       try
	       {
	           // perform the insert
	           this.insertContractStatement();
	           
	           updateFoldering();

	           // add any detail records
	          this.insertInvoices();
	           
	           
	           
	           // remove items from the to be index worklist
	           this.continueWorkflow();

	       } catch (Exception exc)
	       {
	           logger.fatal("Database error saving contract: "+exc.toString(),exc);
	           throw exc;
	     
	       }

	       getContract().setDIRTYFLAG(Boolean.FALSE);
	       getContract().setDOCUMENTMODE(Indexing.UPDATEMODE);

	   }
	   
	    public void updateDetailsCountryStatement() throws Exception
	    {

	        StringBuffer sql = new StringBuffer();
	        String schema = getCountryProperties().getAptsSchema();

	        sql.append("update ");
	        sql.append(schema);
	        sql.append("cntinvd set CICTYCOD = ");
	        sql.append(getSqlString(getContract().getCOUNTRY()));        
	        sql.append(" where cicntnbr = ");
	        sql.append(getSqlString(getContract().getOLDCONTRACTNUMBER()));
	        sql.append(" and cictycod = ");
	        sql.append(getSqlString(getContract().getOLDCOUNTRY()));

	        try
	        {
	        	logger.debug("###SQL### "+sql);
	        	getGaptsJdbcTemplate().update(sql.toString());
	            
	        } catch (DataAccessException exc)
	        {
	            logger.fatal(exc.toString() + "\n" + sql + "\n",exc);
	            throw exc;
	        }catch(Exception e){
	        	logger.fatal(e.toString() + "\n" + sql + "\n",e);
	            throw e; 
	        } 
	    } 
	    
	    
	    public void updateDetailsStatement() throws Exception
	    {
	        java.util.Date now = new java.util.Date();
	        StringBuffer sql = new StringBuffer();
	        String region = "APTS";
	        String schema = getDatabaseDriver().getSchema(region, getContract().getCOUNTRY());

	        String username = getContract().trimUSERID();


	        sql.append("update ");
	        sql.append(schema);
	        sql.append("cntinvd set cicntnbr = ");
	        sql.append(getSqlString(getContract().getCONTRACTNUMBER()));
	        sql.append(", cictycod = ");
	        sql.append(getSqlString(getContract().getCOUNTRY()));
	        sql.append(", ciuserid = ");
	        sql.append(DB2.sqlString(username));
	        sql.append(", cilstdate = ");
	        sql.append(getSqlString(RegionalDateConverter.formatDate("DB2", now)));
	        sql.append(", cilsttime = ");
	        sql.append(getSqlString(RegionalDateConverter.formatTime("DB2", now)));
	        sql.append(" where cicntnbr = ");
	        sql.append(getSqlString(getContract().getOLDCONTRACTNUMBER()));
	        sql.append(" and cictycod = ");
	        sql.append(getSqlString(getContract().getCOUNTRY()));

	        try
	        {
	        	logger.debug("###SQL### "+sql);
	        	getGaptsJdbcTemplate().update(sql.toString());
	            
	        } catch (DataAccessException exc){
	            logger.fatal(exc.toString() + "\n" + sql + "\n",exc);
	            throw exc;
	        }catch(Exception e){
	        	logger.fatal(e.toString() + "\n" + sql + "\n",e);
	            throw e; 
	        } 
	    }
	    
	    
	    /**
	     * create an update statement for updating the Invoice control information
	     * Creation date: (3/13/00 11:44:07 AM)
	     * @throws Exception 
	     */
	    public void updateContractNumberOnInvoicesStatement() throws Exception  {
	    	
	        StringBuffer sql = new StringBuffer();
	        String region = "APTS";
	        String schema = getDatabaseDriver().getSchema(region, getContract().getCOUNTRY());

	        sql.append("update ");
	        sql.append(schema);
	        sql.append("WINVCNTL set INVCNT# = ");
	        sql.append(getSqlString(getContract().getCONTRACTNUMBER()));
	         sql.append(" WHERE INVCNT# = ");
	        sql.append(getSqlString(getContract().getOLDCONTRACTNUMBER()));
	        sql.append(" AND INVCCTYCD = ");
	        sql.append(getSqlString(getContract().getCOUNTRY()));
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
	        sql.append("winvcntlc set INVCNT# = ");
	        sql.append(getSqlString(getContract().getCONTRACTNUMBER()));
	        sql.append(" WHERE INVCNT# = ");
	        sql.append(getSqlString(getContract().getOLDCONTRACTNUMBER()));
	        sql.append(" AND INVCCTYCD = ");
	        sql.append(getSqlString(getContract().getCOUNTRY()));
	        try
	        {
	        	logger.debug("###SQL### "+sql);
	        	getGaptsJdbcTemplate().update(sql.toString());
	            
	        } catch (DataAccessException exc)
	        {
	            logger.fatal(exc.toString() + "\n" + sql + "\n",exc);
	            throw exc;
	        }   catch(Exception e){
	        	logger.fatal(e.toString() + "\n" + sql + "\n",e);
	            throw e; 
	        }    
	    }
	    
	   
	   public void updateDocument()
	   {
	       try
	       {
	           // allready exists, update it
	           updateContractStatement();
	           
	           if(!getContract().getCOUNTRY().equals(getContract().getOLDCOUNTRY())){
	           	updateDetailsCountryStatement();
	           }

	           // update any detail records
	           if (!getContract().getOLDCONTRACTNUMBER().equals(getContract().getCONTRACTNUMBER()))
	           {
	               updateDetailsStatement();
	               updateContractNumberOnInvoicesStatement();
	           }

	           updateFoldering();
	           
	           // add any detail records
	           insertInvoices();

	         
	           
	       } catch (Exception exc) {
	           logger.fatal("Database error updating contract: " + exc.toString(),exc);
	       }
	       getContract().setDIRTYFLAG(Boolean.FALSE);
	   }
	   
	   
	   

	
	   public void saveDocument() throws Exception {
		   
	        DefaultTransactionDefinition txDef = null;
	        TransactionStatus txStatus = null;
	        
	        logger.info("Saving Document Started");
	        
	        try  {
		           txDef = new DefaultTransactionDefinition();
		           txDef.setIsolationLevel(DefaultTransactionDefinition.ISOLATION_READ_UNCOMMITTED);
		        	
			       if (getContract().getDOCUMENTMODE().equals(Indexing.ADDMODE))   {  
				       insertDocument();
				   } else { 
				       updateDocument();
				   }
			       
			       
			       
	        	}  catch (Exception exc) {
		        	getDataSourceTransactionManager().rollback(txStatus);
		            logger.fatal("Error saving Contract: " + exc.toString(),exc);
		            throw exc;
		        }
	        
	        logger.info("Saving Document Finished");
	   }
	   
	   
	    public void updateContractStatement() throws Exception
	    {
	        Date now = new Date();
	        StringBuffer sql = new StringBuffer();
	        String schema = getCountryProperties().getAptsSchema();

	        retrieveCreatedDate();

	        // build the sql, use append because it is more efficient
	        sql.append("update ");
	        sql.append(schema);
	        sql.append("cntcntl set CCCNT# = ");
	        sql.append(getSqlString(getContract().getCONTRACTNUMBER()));
	        if(getContract().getNETEQBAL()!=null && getContract().getNETEQBAL().equals("Y")){
	        	sql.append(", cccntamt = ");
	        	sql.append(getContract().getAMOUNT().replace(',', '.'));
	        	sql.append(", cccntbal = ");
	        	sql.append(getContract().getAMOUNT().replace(',', '.')); 
	        }
	        sql.append(", CCCTYCOD = ");  
	        sql.append(getSqlString(getContract().getCOUNTRY()));        
	        sql.append(", CCCURCDE = ");
	        sql.append(getSqlString(getContract().getCURRENCY()));
	        sql.append(", CCUSERID = ");
	        sql.append(DB2.sqlString(getContract().getUSERID()));
	        sql.append(", CCLSTDAT = ");
	        sql.append(getSqlString(RegionalDateConverter.formatDate("DB2", now)));
	        sql.append(", CCLSTTIM = ");
	        sql.append(getSqlString(RegionalDateConverter.formatTime("DB2", now)));
	        sql.append(", CCOFFER# = ");
	        sql.append(getSqlString(getContract().getOFFERINGLETTER()));
	        sql.append(", CCCUST# = ");
	        sql.append(getSqlString(getContract().getCUSTOMERNUMBER()));
	        sql.append(", CCCUSTNAME = ");
	        sql.append(getSqlString(getContract().getCUSTOMERNAME()));
	        sql.append(", CCCNTRCT =  ");
	        sql.append(getSqlString(RegionalDateConverter.formatDate("DB2", getContract().getCreatedTimeStampAsDate())));
	        sql.append(" where CCCNT# = ");
	        sql.append(getSqlString(getContract().getOLDCONTRACTNUMBER()));
	        sql.append(" and CCCTYCOD = ");
	        sql.append(getSqlString(getContract().getOLDCOUNTRY()));

	        try
	        {
	        	logger.debug("###SQL### "+sql);
	        	getGaptsJdbcTemplate().update(sql.toString());
	            
	        } catch (DataAccessException exc)
	        {
	            logger.fatal(exc.toString() + "\n" + sql + "\n",exc);
	            throw exc;
	        }
	        
	        sql.setLength(0);

	        sql.append("update ");
	        sql.append(schema);
	        sql.append("cntcntlc set CCCNT# = ");
	        sql.append(getSqlString(getContract().getCONTRACTNUMBER()));
	        sql.append(", CCCTYCOD = ");
	        sql.append(getSqlString(getContract().getCOUNTRY()));
	        sql.append(", CCOFFER# = ");
	        sql.append(getSqlString(getContract().getOFFERINGLETTER()));
	        sql.append(", ccsigndat = ");
	        sql.append(getSqlString(RegionalDateConverter.formatDate("DB2", getContract().getCreatedTimeStampAsDate())));
	        sql.append(" where CCCCMOBJID = ");
	        sql.append(getSqlString(getContract().getOBJECTID()));

	        try
	        {
	        	logger.debug("###SQL### "+sql);
	        	getGaptsJdbcTemplate().update(sql.toString());
	            
	        } catch (DataAccessException exc)
	        {
	            logger.fatal(exc.toString() + "\n" + sql + "\n",exc);
	            throw exc;
	        }catch(Exception e){
	        	logger.fatal(e.toString() + "\n" + sql + "\n",e);
	            throw e; 
	        } 

	    }
	    
	   

	/**
     * create a select statement for retieving the contract control data by
     * contract number / country code Creation date: (3/13/00 11:44:07 AM)
     * 
     * @return ResultSet
	 * @throws Exception 
     */
    public ResultSet selectByContractStatement() throws Exception
    {
        StringBuffer sql = new StringBuffer();
        String schema = getCountryProperties().getAptsSchema();
        Connection conn = null;
        Statement stmt = null;
        CachedRowSetImpl cachedRs = new CachedRowSetImpl();

        // build the sql statement, use append because it is more efficent
        sql.append("select A.cccnt#,A.ccctycod, B.CCCCMOBJID from ");
        sql.append(schema);
        sql.append("cntcntl A left join ");
        sql.append(schema);
        sql.append("cntcntlc B on B.cccnt# = A.cccnt# and a.ccctycod = b.ccctycod where A.cccnt# = ");
        sql.append(getSqlString(getContract().getCONTRACTNUMBER()));
        sql.append(" and A.ccctycod = ");
        if (getContract().getCOUNTRY().equals("GB"))
            sql.append("'UK'");
        else
            sql.append(getSqlString(getContract().getCOUNTRY()));

        
        try
        {
        	conn = getDataSourceTransactionManager().getDataSource().getConnection();
        	stmt = conn.createStatement();
             ResultSet results =  stmt.executeQuery(sql.toString());
           //Recommended way to do it but will only work with Java 7
            //cachedRs = RowSetProvider.newFactory().createCachedRowSet();
            cachedRs.populate(results);

            return (cachedRs);
        } catch (SQLException exc)  {
            logger.fatal(exc.toString() + "\n" + sql + "\n",exc);
            throw exc;
            
        }catch(Exception e){
        	logger.fatal(e.toString() + "\n" + sql + "\n",e);
            throw e; 
        } finally {
        	stmt.close();
			conn.close();
		}
    }
    
    
    public void createContractFolder() throws Exception
    {
        // create contract folder if it doesn't exist allready
        String[] fields = { "Contract Number" };
        String[] values = { getContract().getCONTRACTNUMBER() };
        String folderXML = CM.executeQuery("Contract Folder " + getContract().getCOUNTRY(), fields, values, getContract().getDatastore());
        String folderId = getContract().getObjectId(folderXML);

        String[] s = new String[0];
        ArrayList<String> newfields = new ArrayList<String>();
        ArrayList<String> newvalues = new ArrayList<String>();
        copyAttributes( getContract().getOBJECTID(),  getContract().getINDEXCLASS(), folderId, "Contract Folder " +  getContract().getCOUNTRY(), newfields, newvalues, getContract().getCOUNTRY(),getContract().getDatastore());

        if (folderId == null || folderId.trim().length() == 0)
        {
            folderId = CM.createFolder("Contract Folder " + getContract().getCOUNTRY(), (String[]) (newfields.toArray(s)), (String[]) (newvalues.toArray(s)),getContract().getDatastore());
        } else
        {
        	CM.updateDocument(folderId, (String[]) (newfields.toArray(s)), (String[]) (newvalues.toArray(s)), getContract().getDatastore());
        }

        getContract().setFOLDERID(folderId);
    }
    
    
    public void insertInvoices () throws Exception
    {
   
        ArrayList<ContractInvoice> invList = getContract().getInvoicesList();
        Invoice invoice = new Invoice(getContract().getCOUNTRY());
       
        
        for (int i=0; i<invList.size(); i++)	 {
        	
        	ContractInvoice contractInvoice = (ContractInvoice)invList.get(i);
        	
        	//invoice = new Invoice(getContract().getCOUNTRY());
        	invoice.setOBJECTID(contractInvoice.getOBJECTID());
        	invoice.setINVOICENUMBER(contractInvoice.getINVOICENUMBER());
        	invoice.setINVOICEDATE(contractInvoice.getINVOICEDATE());
        	invoice.setLocale(getContract().getLocale());
        	invoice.setDatastore(getContract().getDatastore());
        	invoice.setCONTRACTNUMBER(contractInvoice.getCONTRACTNUMBER());
        	if (invoice.getOBJECTID() == null ) invoice.selectInvoiceId();
        	
        	
            updateInvoiceIndexValues(invoice);
            
            // see if the record exists, it has a contract number if it came from gapts
            if (invoice.getCONTRACTNUMBER() == null || invoice.getCONTRACTNUMBER().trim().length() == 0)
            {
                // no record exists so assign remaining values add it

                // retrieve the rest of the invoice by objectid
                ResultSet results = invoice.getInvoiceDataModel().selectByObjectIdStatement();
                if (results.next())
                {
                	invoice.setDBCR(DB2.getString(results, 9));
                	invoice.setCURRENCY(DB2.getString(results, 12));
                	invoice.setINVOICETYPE(DB2.getString(results, 13));
                	invoice.setVENDORNUMBER(DB2.getString(results, 14));
                	invoice.setCOMPANYCODE(DB2.getString(results, 22));
                }
                
                // transfer over known values
                invoice.setCONTRACTNUMBER(getContract().getCONTRACTNUMBER());
                invoice.setUSERID(getContract().getUSERID());
                invoice.setOFFERINGLETTER(getContract().getOFFERINGLETTER());
                invoice.setCUSTOMERNAME(getContract().getCUSTOMERNAME());
                invoice.setCUSTOMERNUMBER(getContract().getCUSTOMERNUMBER());
                if(null!= invoice.getCONTRACTNUMBER() && !"".equals(invoice.getCONTRACTNUMBER()))
                	invoice.getInvoiceDataModel().insertDetailStatement();
                
                // and update the winvcntl record
                invoice.getInvoiceDataModel().updateInvoiceWithContractStatement();
            }
        }
    }
    
    

    
    /**
     * create an insert statement to presist the contract into contract conrtol
     * Creation date: (3/13/00 11:44:07 AM)
     * @throws Exception 
     */
    public void insertContractStatement() throws Exception
    {
    	java.util.Date now = new java.util.Date();
    	StringBuffer sql = new StringBuffer();
        String schema = getCountryProperties().getAptsSchema();
        
        
        //retrieveCreatedDate();
         
        // clip the username
        String username = getContract().trimUSERID();


        // build the sql statement, use append because it is more efficient
        sql.append("insert into ");
        sql.append(schema);
        sql.append("cntcntl values (");
        sql.append(getSqlString(getContract().getCONTRACTNUMBER()));
        sql.append(",");
        sql.append(getContract().getAMOUNT().replace(',', '.'));
        sql.append(",");
        sql.append(getContract().getAMOUNT().replace(',', '.'));
        sql.append(",");
        sql.append(getSqlString(getContract().getCURRENCY()));
        sql.append(",");
        sql.append("''");
        sql.append(",");
        sql.append(getSqlString(getContract().getCOUNTRY()));
        sql.append(",");
        sql.append("''");
        sql.append(",");
        sql.append("''");
        sql.append(",");
        sql.append(DB2.sqlString(username));
        sql.append(",");
        sql.append(getSqlString(RegionalDateConverter.formatDate("DB2", now)));
        sql.append(",");
        sql.append(getSqlString(RegionalDateConverter.formatTime("DB2", now)));
        sql.append(",");
        sql.append(getSqlString(getContract().getOFFERINGLETTER()));
        sql.append(",");
        sql.append(getSqlString(getContract().getCUSTOMERNUMBER()));
        sql.append(",");
        sql.append(getSqlString(getContract().getCUSTOMERNAME()));
        sql.append(",");
        sql.append("''");
        sql.append(",");
        sql.append(getSqlString(RegionalDateConverter.formatDate("DB2", getContract().getCreatedTimeStampAsDate())));
        sql.append(")");

        try
        {
        	logger.debug("###SQL### "+sql);
        	getGaptsJdbcTemplate().update(sql.toString());
        	
        } catch (DataAccessException exc)
        {

            logger.fatal(exc.toString() + "\n" + sql + "\n",exc);
            throw exc;
        }

        sql.setLength(0);
        // build the sql statement, insert value to the objectID
        sql.append("insert into ");
        sql.append(schema);
        sql.append("cntcntlc values (");
        sql.append(getSqlString(getContract().getCONTRACTNUMBER()));
        sql.append(",");
        sql.append(getSqlString(getContract().getCOUNTRY()));
        sql.append(",");
        sql.append(getSqlString(getContract().getOBJECTID()));
        sql.append(",");
        sql.append(getSqlString(getContract().getFOLDERID()));
        sql.append(",");
        sql.append(getSqlString(getContract().getOFFERINGLETTER()));
        sql.append(",");
        sql.append(getSqlString(RegionalDateConverter.formatDate("DB2", getContract().getCreatedTimeStampAsDate())));
        sql.append(")");

        try
        {
        	logger.debug("###SQL### "+sql);
        	getGaptsJdbcTemplate().update(sql.toString());
        	
        } catch (DataAccessException exc)
        {

            logger.fatal(exc.toString() + "\n" + sql + "\n",exc);
            throw exc;
        }catch(Exception e){
        	logger.fatal(e.toString() + "\n" + sql + "\n",e);
            throw e; 
        } 
    }
    
    
    
    public ResultSetCache selectCurrencyCodesStatement() throws Exception
    {

        StringBuffer sql = new StringBuffer();
        String schema = getCountryProperties().getAptsSchema();
        Connection conn = null;
        PreparedStatement pstmt = null;


        sql.append("select distinct cccurr, CCCDFT from ");
        sql.append(schema);
        sql.append("ccccntl where cccntcode = ? ");
        sql.append("  order by cccurr");

        try
        {
         	conn = getDataSourceTransactionManager().getDataSource().getConnection();
         	pstmt = new LoggableStatement(conn,sql.toString());
         	pstmt.setString(1, getIndexingObj().getCOUNTRY());
            ResultSet results =  pstmt.executeQuery();
            getContract().setCurrencyResults( new ResultSetCache(results, getIndexingObj().getCOUNTRY(), 0, 1, "*"));
            return (getContract().getCurrencyResults());
            
        } catch (SQLException exc) {
        	logger.fatal(exc.toString() + "\n"  + ((LoggableStatement)pstmt).getQueryString()+ "\n",exc  );
            throw exc;
            
        } catch(Exception e){
        	logger.fatal(e.toString() + "\n" + sql + "\n",e);
            throw e; 
        } finally {
        	pstmt.close();
			conn.close();
		}
    }
    
    

    
    

    public void updateObjectIdStatement() throws Exception
    {
        StringBuffer sql = new StringBuffer();
        String schema = getCountryProperties().getAptsSchema();

        sql.append("update ");
        sql.append(schema);
        sql.append("cntcntlc set CCCCMOBJID =  ");
        sql.append(getSqlString(getContract().getOBJECTID()));
        sql.append(" where cccnt# =  ");
        sql.append(getSqlString(getContract().getCONTRACTNUMBER()));
        sql.append("and ccctycod = ");
        sql.append(getSqlString(getContract().getCOUNTRY()));

        try  {
        	
        	logger.debug("###SQL### "+sql);
        	getGaptsJdbcTemplate().update(sql.toString());
        	
        } catch (DataAccessException exc)
        {
            logger.fatal(exc.toString() + "\n" + sql + "\n",exc);
            throw exc;
        }catch(Exception e){
        	logger.fatal(e.toString() + "\n" + sql + "\n",e);
            throw e; 
        } 
    }
    
    
    public ResultSet selectByObjectIdStatement() throws Exception
    {
        StringBuffer sql = new StringBuffer();
    	PreparedStatement pstmt = null;
    	CachedRowSetImpl cachedRs = new CachedRowSetImpl();
        String schema = getCountryProperties().getAptsSchema();
        Connection conn = null;

        sql.append("select A.cccnt#, A.cccntamt, A.cccntbal, A.CCCURCDE, A.CCCUSTNAME, A.CCCUST#, A.CCOFFER#, B.CCCCMFLDID,B.ccsigndat, A.ccctycod from ");
        sql.append(schema);
        sql.append("cntcntl A left join ");
        sql.append(schema);
        sql.append("cntcntlc B on B.cccnt# = A.cccnt# and B.ccctycod = A.ccctycod where CCCCMOBJID = ?");

        try
        {
          	
        	conn = getDataSourceTransactionManager().getDataSource().getConnection();
            pstmt = new LoggableStatement(conn,sql.toString());
            pstmt.setString(1, this.getContract().getOBJECTID());
            ResultSet results =  pstmt.executeQuery();
           //Recommended way to do it but will only work with Java 7
            //cachedRs = RowSetProvider.newFactory().createCachedRowSet();
            cachedRs.populate(results);

            return (cachedRs);
            
        } catch (SQLException exc)  {
        	logger.fatal(exc.toString() + "\n"  + ((LoggableStatement)pstmt).getQueryString()+ "\n" ,exc );
            throw exc;
            
        } catch(Exception e){
        	logger.fatal(e.toString() + "\n"  + ((LoggableStatement)pstmt).getQueryString()+ "\n" ,e );
            throw e;
            
        }  finally {
        	pstmt.close();
        	conn.close();
        }
    }
    
    public ResultSet selectContractCreatedFromCOATool() throws Exception
    {
        StringBuffer sql = new StringBuffer();
    	PreparedStatement pstmt = null;
    	CachedRowSetImpl cachedRs = new CachedRowSetImpl();
        String schema = getCountryProperties().getAptsSchema();
        Connection conn = null;

        sql.append("select count(*) from ");
        sql.append(schema);
        sql.append("CNTHIST where CHCNT# = ?");

        try
        {
            
        	conn = getDataSourceTransactionManager().getDataSource().getConnection();
            pstmt = new LoggableStatement(conn,sql.toString());
            pstmt.setString(1, this.getContract().getCONTRACTNUMBER());
            ResultSet results =  pstmt.executeQuery();
           //Recommended way to do it but will only work with Java 7
            //cachedRs = RowSetProvider.newFactory().createCachedRowSet();
            cachedRs.populate(results);

            return (cachedRs);
            
            
        } catch (SQLException exc)    {
        	logger.fatal(exc.toString() + "\n"  + ((LoggableStatement)pstmt).getQueryString()+ "\n"  ,exc);
            throw exc;
            
        }catch(Exception e){
        	logger.fatal(e.toString() + "\n"  + ((LoggableStatement)pstmt).getQueryString()+ "\n" ,e );
            throw e;
            
        }  finally {
        	pstmt.close();
        	conn.close();
        }
    }
    
    
    public ResultSet selectInvoiceDetailsStatement() throws Exception
    {
        StringBuffer sql = new StringBuffer();
        StringBuffer sqls = new StringBuffer();
    	Statement stmt = null;
    	CachedRowSetImpl cachedRs = new CachedRowSetImpl();
        String schema = getCountryProperties().getAptsSchema();
        Connection conn = null;
        
        
        // check the CNTCNTL file to see if the record exists
        sqls.append("select count(*) from ");
        sqls.append(schema);
        sqls.append("cntcntlc f, ");
        sqls.append(schema);
        sqls.append("cntcntl g where f.cccnt#=");
        sqls.append(getSqlString(getContract().getCONTRACTNUMBER()));
        sqls.append(" and g.cccnt# = ");
        sqls.append(getSqlString(getContract().getCONTRACTNUMBER()));
        sqls.append(" and f.ccctycod = ");
        if (getContract().getCOUNTRY().equals("GB"))
            sqls.append("'UK'");
        else
            sqls.append(getSqlString(getContract().getCOUNTRY()));
        sqls.append(" and g.ccctycod = ");
        if (getContract().getCOUNTRY().equals("GB"))
            sqls.append("'UK'");
        else
            sqls.append(getSqlString(getContract().getCOUNTRY()));
        sqls.append(" and f.CCCCMOBJID = ");
        sqls.append(getSqlString(getContract().getOBJECTID()));
        sqls.append(" and f.CCOFFER#= ");
        sqls.append(getSqlString(getContract().getOFFERINGLETTER()));
        sqls.append(" and g.CCOFFER#= ");
        sqls.append(getSqlString(getContract().getOFFERINGLETTER()));
        int count = 0;

        try
        {
        	conn = getDataSourceTransactionManager().getDataSource().getConnection();
            stmt = conn.createStatement();
            ResultSet results =  stmt.executeQuery(sqls.toString());
        	
        	
            if (results.next())
            {
                count = results.getInt(1);
            }
            
        } catch (SQLException exc)
        {
            if (exc.getErrorCode() != 0)
            {
                logger.fatal(exc.toString() + "\n" + sql + "\n",exc);
                throw exc;
            }
        }
        sql.setLength(0);

        if (count == 0)
        {
            sql.append("select B.invcccmid, B.INVCINV#, B.invcinvdte,B.INVCOFFER# from ");
            sql.append(schema);
            sql.append("winvcntlc B, ");
            sql.append(schema);
            sql.append("winvcntl A where B.INVCINV# = A.INVCINV# and B.invcinvdte = A.invcinvdte ");
            sql.append(" AND B.invcctycd=");
            if (getContract().getCOUNTRY().equals("GB"))
                sql.append("'UK'");
            else
                sql.append(getSqlString(getContract().getCOUNTRY()));
            sql.append(" AND A.invcctycd=");
            if (getContract().getCOUNTRY().equals("GB"))
                sql.append("'UK'");
            else
                sql.append(getSqlString(getContract().getCOUNTRY()));
            sql.append(" AND A.INVCNT#=");
            sql.append(getSqlString(getContract().getCONTRACTNUMBER()));
            sql.append(" AND B.INVCNT#=");
            sql.append(getSqlString(getContract().getCONTRACTNUMBER()));
        } else
        {
            sql.append("select B.invcccmid, C.ciinv#, C.CIINVDATE,B.INVCOFFER# from ");
            sql.append(schema);
            sql.append("cntinvd C, ");
            sql.append(schema);
            sql.append("winvcntlc B, ");
            sql.append(schema);
            sql.append("winvcntl A where B.INVCINV# = A.INVCINV# and B.invcinvdte = A.invcinvdte and B.invcinv# = C.ciinv# and B.invcinvdte = C.ciinvdate");
            sql.append(" AND B.invcctycd=");
            if (getContract().getCOUNTRY().equals("GB"))
                sql.append("'UK'");
            else
                sql.append(getSqlString(getContract().getCOUNTRY()));
            sql.append(" AND A.invcctycd=");
            if (getContract().getCOUNTRY().equals("GB"))
                sql.append("'UK'");
            else
                sql.append(getSqlString(getContract().getCOUNTRY()));
            sql.append(" and C.cictycod =");
            if (getContract().getCOUNTRY().equals("GB"))
                sql.append("'UK'");
            else
                sql.append(getSqlString(getContract().getCOUNTRY()));
            sql.append(" AND A.INVCNT#=");
            sql.append(getSqlString(getContract().getCONTRACTNUMBER()));
            sql.append(" AND B.INVCNT#=");
            sql.append(getSqlString(getContract().getCONTRACTNUMBER()));
            sql.append(" AND C.cicntnbr=");
            sql.append(getSqlString(getContract().getCONTRACTNUMBER()));
            sql.append(" AND A.INVCOFFER# in (select f.CCOFFER# from ");
            sql.append(schema);
            sql.append("cntcntlc f, ");
            sql.append(schema);
            sql.append("cntcntl g where f.cccnt#=");
            sql.append(getSqlString(getContract().getCONTRACTNUMBER()));
            sql.append(" and g.cccnt# = ");
            sql.append(getSqlString(getContract().getCONTRACTNUMBER()));
            sql.append(" and f.ccctycod = ");
            if (getContract().getCOUNTRY().equals("GB"))
                sql.append("'UK'");
            else
                sql.append(getSqlString(getContract().getCOUNTRY()));
            sql.append(" and g.ccctycod = ");
            if (getContract().getCOUNTRY().equals("GB"))
                sql.append("'UK'");
            else
                sql.append(getSqlString(getContract().getCOUNTRY()));
            sql.append(" and f.CCCCMOBJID = ");
            sql.append(getSqlString(getContract().getOBJECTID()));
            sql.append(")");
        }

        try
        {

        	conn = getDataSourceTransactionManager().getDataSource().getConnection();
            stmt = conn.createStatement();
            ResultSet results =  stmt.executeQuery(sql.toString());
           //Recommended way to do it but will only work with Java 7
            //cachedRs = RowSetProvider.newFactory().createCachedRowSet();
            cachedRs.populate(results);

            return (cachedRs);
            
        }catch (SQLException exc)  {
            logger.fatal(exc.toString() + "\n" + sql + "\n",exc);
            throw exc;
            
        }catch(Exception e){
            logger.fatal(e.toString() + "\n" + sql + "\n",e);
            throw e;
            
        }  finally {
        	stmt.close();
        	conn.close();
        }
    }
    
    public ResultSet selectDetailsStatement() throws Exception
    {
        StringBuffer sql = new StringBuffer();
    	Statement stmt = null;
    	CachedRowSetImpl cachedRs = new CachedRowSetImpl();
        String schema = getCountryProperties().getAptsSchema();
        Connection conn = null;

        // build the sql statement, use append because it is more efficient
        sql.append("select B.invcccmid, C.ciinv#, C.CIINVDATE from ");
        sql.append(schema);
        sql.append("cntinvd C, ");
        sql.append(schema);
        sql.append("winvcntlc B left join ");
        sql.append(schema);
        sql.append("winvcntl A on B.INVCINV# = A.INVCINV# ");
        sql.append("and b.invcinvdte = a.invcinvdte ");
        sql.append("and b.invcctycd = a.invcctycd ");
        sql.append("where C.cicntnbr = ");
        sql.append(getSqlString(getContract().getOLDCONTRACTNUMBER()));
        sql.append(" and C.cictycod = ");
        sql.append(getSqlString(getContract().getCOUNTRY()));
        sql.append(" and B.invcinv# = C.ciinv# and B.invcinvdte = C.ciinvdate ");
        // ADDED FOR GIP29
        sql.append(" and B.INVCCTYCD = ");
        sql.append(getSqlString(getContract().getCOUNTRY()));
        // prepare the statement to execute
        try
        {
        	conn = getDataSourceTransactionManager().getDataSource().getConnection();
            stmt = conn.createStatement();
            ResultSet results =  stmt.executeQuery(sql.toString());
           //Recommended way to do it but will only work with Java 7
            //cachedRs = RowSetProvider.newFactory().createCachedRowSet();
            cachedRs.populate(results);

            return (cachedRs);
            
        }catch (SQLException exc) {
            logger.fatal(exc.toString() + "\n" + sql + "\n",exc);
            throw exc;
            
        }catch(Exception e){
        	logger.fatal(e.toString() + "\n" + sql + "\n",e);
            throw e; 
        }  finally {
        	stmt.close();
        	conn.close();
        }
    }
    
    
    public void updateIndexValuesForRollback()   throws Exception {
        
    	try
        {
    		
    		logger.info("Updating index values for Rollback Started");
    		
    		String contractDate = "";
    		
        	CM = new ContentManagerImplementation(getContract().getDatastore());
        	
            String objectId = getContract().getOBJECTID();
            String folderId = getContract().getFOLDERID();
            
        	if(!getContract().getCOUNTRY().equals(getContract().getOLDCOUNTRY())) {
        		
        		CM.moveDocument(getContract().getFOLDERID(),"Contract Folder " + getContract().getCOUNTRY(),getContract().getDatastore());
        	}
        	
        	if(getContract().getOLDCONTRACTDATE()!=null && !"".equals(getContract().getOLDCONTRACTDATE())) {
        		contractDate = RegionalDateConverter.convertDate("GUI", "CM", getContract().getOLDCONTRACTDATE());
        	}

        	String[] Allfields = CM.getIndexFields(CM.indexDescriptionToName(getContract().getINDEXCLASS(),getContract().getDatastore()));
        	String[] fields = null;
        	String[] values = null;
        	List<String> wordList = Arrays.asList(Allfields); 
        	
        	if(wordList.contains("Contract Date")){
        		
           		fields = new String[]{ "Contract Number", "Contract Date","Customer Name", "Customer Number", "Offering Letter Number"};
                values =  new String[]{ getContract().getCONTRACTNUMBER(),contractDate,  getContract().getCUSTOMERNAME(), getContract().getCUSTOMERNUMBER(), getContract().getOFFERINGLETTER()};

        	} else {
        		fields = new String[]{ "Contract Number", "Customer Name", "Customer Number", "Offering Letter Number"};
                values =  new String[]{ getContract().getCONTRACTNUMBER(), getContract().getCUSTOMERNAME(), getContract().getCUSTOMERNUMBER(), getContract().getOFFERINGLETTER()};

        	}

            if (folderId!=null && folderId.trim().length() > 0) {
 
            	CM.updateDocument(folderId, fields, values, getContract().getDatastore());
            }
            
            if (objectId.trim().length() > 0) {

               CM.updateDocument(objectId, fields, values, getContract().getDatastore());
            }
            
        } catch (Exception exc) {
            logger.fatal("Error setting Content Manager index values: "+exc.toString(),exc);
            throw exc;
        }
    	
    	logger.info("Updating index values for Rollback Finished");
    }
    
    

    
    public void updateIndexValues()   throws Exception {
        
    	try
        {
    		
    		logger.info("Updating Index Values Started");
    		
        	CM = new ContentManagerImplementation(getContract().getDatastore());
        	
            String objectId = getContract().getOBJECTID();
            String folderId = getContract().getFOLDERID();
            
        	if(!getContract().getCOUNTRY().equals(getContract().getOLDCOUNTRY())) {
        		
        		CM.moveDocument(getContract().getFOLDERID(),"Contract Folder " + getContract().getCOUNTRY(),getContract().getDatastore());
        	}

        	String[] fields = null;
        	String[] values = null;
        	fields = new String[]{ "Contract Number", "Customer Name", "Customer Number", "Offering Letter Number"};
            values =  new String[]{ getContract().getCONTRACTNUMBER(), getContract().getCUSTOMERNAME(), getContract().getCUSTOMERNUMBER(), getContract().getOFFERINGLETTER()};
            logger.info("Fields:" + Arrays.toString(fields));
            logger.info("Values:" + Arrays.toString(values));
            if (folderId!=null && folderId.trim().length() > 0) {
 
            	CM.updateDocument(folderId, fields, values, getContract().getDatastore());
            }
            
            if (objectId.trim().length() > 0) {

               CM.updateDocument(objectId, fields, values, getContract().getDatastore());
            }
            
            logger.info("Updating Index Values Finished");
            
        } catch (Exception exc) {
            logger.fatal("Error setting Content Manager index values: "+exc.toString(),exc);
            throw exc;
        }
    }
    
    public void updateInvoiceIndexValues(Invoice invoice)  throws Exception
    {
        try
        {
            String objectId = invoice.getOBJECTID();
            String folderId = getContract().getFOLDERID();
            
            CM = new ContentManagerImplementation(getContract().getDatastore());

            // update the indexes
            String[] fields = { "Contract Number", "Customer Name", "Customer Number", "Offering Letter Number" };
            String[] values = { getContract().getCONTRACTNUMBER(), getContract().getCUSTOMERNAME(), getContract().getCUSTOMERNUMBER(), getContract().getOFFERINGLETTER() };
            
            if (folderId!=null && folderId.trim().length() > 0) {
               CM.updateDocument(folderId, fields, values,getContract().getDatastore());
            }
            
            if (objectId!=null && objectId.trim().length() > 0) {
            	CM.updateDocument(objectId, fields, values, getContract().getDatastore());
            }
        } catch (Exception exc)  {
            logger.fatal("Error setting Content Manager index values: " + exc.toString(),exc);
            throw exc;
        }
    }
    
    
    public void updateFolderIdStatement() throws SQLException
    {

        StringBuffer sql = new StringBuffer();
        String schema = getCountryProperties().getAptsSchema();

        sql.append("update ");
        sql.append(schema);
        sql.append("cntcntlc set CCCCMFLDID = ");
        sql.append(getSqlString(getContract().getFOLDERID()));
        sql.append(" where CCCCMOBJID = ");
        sql.append(getSqlString(getContract().getOBJECTID()));

        try
        {
        	logger.debug("###SQL### "+sql);
        	getGaptsJdbcTemplate().update(sql.toString());
            
        } catch (DataAccessException exc)
        {
            logger.fatal(exc.toString() + "\n" + sql + "\n",exc);
            throw exc;
        }
    }
    
    
    public void continueWorkflow()  {
    	
    	try {
    		
    		CM.completeWorkflow(getContract().getOBJECTID(), getContract().getTOBEINDEXEDWORKFLOW(), getContract().getDatastore());
    		
    	} catch (Exception exc) {
    		logger.error("Error continuing workflow process:" + exc.toString());
		}
    }
       
}