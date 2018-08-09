package com.ibm.gil.model;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


import org.springframework.dao.DataAccessException;

import com.ibm.gil.business.InvoiceELS;
import com.ibm.gil.business.LineItemELS;
import com.ibm.gil.business.OfferingLetterELS;
import com.ibm.gil.util.RegionalDateConverter;
import com.ibm.igf.hmvc.DB2;
import com.ibm.igf.hmvc.ResultSetCache;
import com.ibm.igf.webservice.GCPSGetOL;
import com.sun.rowset.CachedRowSetImpl;


public class OfferingLetterDataModelELS  extends DataModel  {
	
	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(InvoiceDataModel.class);
	

	private OfferingLetterELS offeringLetterParent;
	private transient static GCPSGetOL aGCPSGetOfferingLetter = null;
	
	public OfferingLetterDataModelELS(OfferingLetterELS offeringLetterParent){
		super(offeringLetterParent);
		this.offeringLetterParent=offeringLetterParent;
		
	}
	
	public OfferingLetterELS getOfferingLetterELS() {
		return offeringLetterParent;
	}

	public void setOfferingLetterELS(OfferingLetterELS offeringLetterELS) {
		this.offeringLetterParent = offeringLetterELS;
	}

    private GCPSGetOL getGCPSGetOfferingLetter()
    {
        if (aGCPSGetOfferingLetter == null)
        {
            aGCPSGetOfferingLetter = new GCPSGetOL();
        }
        return aGCPSGetOfferingLetter;
    }

    /*
     * maintains the list of detail records from the quote
     */

    public void recalculateQuoteLineItems(InvoiceELS invoice)
    {	
    	logger.debug("Recalculate Quote Line Items Entry ");
    	
    	ArrayList lineItems=offeringLetterParent.getQuoteLineItems();
        LineItemELS aLineItemDataModel = null;
        String defaultVatCode="";
        
        for (int i = 0; i < lineItems.size(); i++)
        {
            aLineItemDataModel = (LineItemELS) lineItems.get(i);
            aLineItemDataModel.setFROMQUOTE("Y");
            aLineItemDataModel.setINVOICEITEMTYPE("TM");
            aLineItemDataModel.setCOUNTRY(invoice.getCOUNTRY());
            if(i==0){
            	aLineItemDataModel.getLineItemDataModelELS().setDefaultVatCode(invoice);
            	defaultVatCode=aLineItemDataModel.getDefaultVatCode();
            }
            if (!defaultVatCode.equals(""))
            	
            {
                aLineItemDataModel.setVATCODE(defaultVatCode);
            }
        
        }
        logger.debug("Recalculate Quote Line Items Exit ");
    }
   
    
    public void insertOfferingStatement() throws Exception
    {
    	java.util.Date now = new java.util.Date();
    	StringBuffer sql = new StringBuffer();
        String schema = getCountryProperties().getAptsSchema();
  
        String username = getOfferingLetterELS().trimUSERID();
        
        String CUSTNAME;
        if(getSqlString(getOfferingLetterELS().getCUSTOMERNAME()).length() >= 36){
        	CUSTNAME = "'"+getSqlString(getOfferingLetterELS().getCUSTOMERNAME()).substring(1, 36)+"'";
        }else{        	
        	CUSTNAME = getSqlString(getOfferingLetterELS().getCUSTOMERNAME());
        }

        // build the sql statement, use append because it is more efficient
        sql.append("insert into ");
        sql.append(schema);
        sql.append("OFFERCTL values (");
        sql.append(getSqlString(getOfferingLetterELS().getOFFERINGNUMBER()));
        sql.append(",");
        sql.append(getSqlString(getOfferingLetterELS().getCOUNTRY()));
        sql.append(",");
        sql.append(getSqlString(getOfferingLetterELS().getCURRENCY()));
        sql.append(",");
        sql.append(getOfferingLetterELS().getAMOUNT().replace(',', '.'));
        sql.append(",");
        sql.append(getOfferingLetterELS().getAMOUNT().replace(',', '.'));
        sql.append(",");
        sql.append(DB2.sqlString(username));
        sql.append(",");
        sql.append(getSqlString(RegionalDateConverter.formatDate("DB2", now)));
        sql.append(",");
        sql.append(getSqlString(RegionalDateConverter.formatTime("DB2", now)));
        sql.append(",");
        sql.append(getSqlString(getOfferingLetterELS().getCUSTOMERNUMBER()));
        sql.append(",");
        sql.append(CUSTNAME.trim());
        sql.append(",");
        sql.append("''");
        sql.append(",");
        sql.append(getSqlString(RegionalDateConverter.formatDate("DB2", getOfferingLetterELS().getCreatedTimeStampAsDate())));
       
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
        sql.append("offerctlc values (");
        sql.append(getSqlString(getOfferingLetterELS().getOFFERINGNUMBER()));
        sql.append(",");
        sql.append(getSqlString(getOfferingLetterELS().getCOUNTRY()));
        sql.append(",");
        sql.append(getSqlString(getOfferingLetterELS().getOBJECTID()));
        sql.append(",");
        sql.append(getSqlString(RegionalDateConverter.formatDate("DB2", getOfferingLetterELS().getCreatedTimeStampAsDate())));
        sql.append(")");

        try
        {
        	logger.debug("###SQL### "+sql);
        	getGaptsJdbcTemplate().update(sql.toString());
        	
        } catch (DataAccessException exc)
        {
            logger.fatal(exc.toString() + "\n" + sql + "\n",exc);
            throw exc;
        }    catch (Exception exc)  {
            logger.fatal(exc.toString() + "\n" + sql + "\n",exc);
            throw exc; 
        }  
    }  
    
    
    
    public void updateOfferingStatement() throws Exception
    {
    	java.util.Date now = new java.util.Date();
    	StringBuffer sql = new StringBuffer();
        String schema = getCountryProperties().getAptsSchema();
  
        String username = getOfferingLetterELS().trimUSERID();
        String CUSTNAME;
        
        if(getSqlString(getOfferingLetterELS().getCUSTOMERNAME()).length() >= 36){
        	CUSTNAME = "'"+getSqlString(getOfferingLetterELS().getCUSTOMERNAME()).substring(1, 36)+"'";
        }else{        	
        	CUSTNAME = getSqlString(getOfferingLetterELS().getCUSTOMERNAME());
        }


        // build the sql statement, use append because it is more efficient
        sql.append("update ");
        sql.append(schema);
        sql.append("OFFERCTL set OFCOFFER# = ");
        sql.append(getSqlString(getOfferingLetterELS().getOFFERINGNUMBER()));
        sql.append(", OFCCTYCD = ");
        sql.append(getSqlString(getOfferingLetterELS().getCOUNTRY()));
        sql.append(", OFCCURR = ");
        sql.append(getSqlString(getOfferingLetterELS().getCURRENCY()));
        sql.append(", OFCOFRAMT = ");
        sql.append(getOfferingLetterELS().getAMOUNT().replace(',', '.'));
        sql.append(", OFCOFRBAL = ");
        sql.append(getOfferingLetterELS().getAMOUNT().replace(',', '.'));
        sql.append(", OFCUSERID = ");
        sql.append(getDatabaseDriver().sqlString(username));
        sql.append(", OFCLSTDAT = ");
        sql.append(getSqlString(RegionalDateConverter.formatDate("DB2", now)));
        sql.append(", OFCLSTTIM = ");
        sql.append(getSqlString(RegionalDateConverter.formatTime("DB2", now)));
        sql.append(", OFCUST# = ");
        sql.append(getSqlString(getOfferingLetterELS().getCUSTOMERNUMBER()));
        sql.append(", OFCUSTNAME = ");
        sql.append(CUSTNAME.trim());
        sql.append(", OFCMOBJID  = ");
        sql.append("''");
        sql.append(", OFCRECDAT  = ");
        sql.append(getSqlString(RegionalDateConverter.formatDate("DB2", getOfferingLetterELS().getCreatedTimeStampAsDate())));
        sql.append(" where OFCOFFER#= ");
        sql.append(getSqlString(getOfferingLetterELS().getOFFERINGNUMBER()));
        sql.append(" and OFCCTYCD = ");
        sql.append(getSqlString(getOfferingLetterELS().getCOUNTRY()));

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
        sql.append("offerctlc set OFCOFFER# = ");
        sql.append(getSqlString(getOfferingLetterELS().getOFFERINGNUMBER()));
        sql.append(", OFCCTYCD = ");
        sql.append(getSqlString(getOfferingLetterELS().getCOUNTRY()));
        sql.append(", OFCCMOBJID = ");
        sql.append(getSqlString(getOfferingLetterELS().getOBJECTID()));
        sql.append(", OFSIGNDAT = ");
        sql.append(getSqlString(RegionalDateConverter.formatDate("DB2", getOfferingLetterELS().getCreatedTimeStampAsDate())));
        sql.append(" where OFCOFFER# = ");
        sql.append(getSqlString(getOfferingLetterELS().getOFFERINGNUMBER()));
        sql.append(" and OFCCTYCD = ");
        sql.append(getSqlString(getOfferingLetterELS().getCOUNTRY()));        

        try
        {
        	logger.debug("###SQL### "+sql);
        	getGaptsJdbcTemplate().update(sql.toString());
        	
        } catch (DataAccessException exc)
        {

            logger.fatal(exc.toString() + "\n" + sql + "\n",exc);
            throw exc;
        } catch (Exception exc)  {
            logger.fatal(exc.toString() + "\n" + sql + "\n",exc);
            throw exc; 
        } 
    }
    
    
    public ResultSet selectOLStatement() throws Exception
    {
       
        StringBuffer sql = new StringBuffer();
    	CachedRowSetImpl cachedRs = new CachedRowSetImpl();
        String schema = getCountryProperties().getAptsSchema();
        Connection conn = null;
        Statement stmt = null;
        

        // build the sql statement, use append since it is more efficient
        sql.append("select A.OFCOFFER#, A.OFCCTYCD, B.OFCOFFER#, B.OFCCTYCD, B.OFCCMOBJID from ");
        sql.append(schema);
        sql.append("OFFERCTL A left join ");
        sql.append(schema);
        sql.append("offerctlc B on B.OFCOFFER# = A.OFCOFFER#");
        sql.append(" where A.OFCOFFER# = ");
        sql.append(getSqlString(getOfferingLetterELS().getOFFERINGNUMBER()));
        sql.append(" and A.OFCCTYCD = ");
        sql.append(getSqlString(getOfferingLetterELS().getCOUNTRY()));
        sql.append(" and B.OFCCMOBJID = ");
        sql.append(getSqlString(getOfferingLetterELS().getOBJECTID()));      
                
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
            
        } catch (SQLException exc)
        {
            logger.fatal(exc.toString() + "\n" + sql + "\n",exc);
            throw exc;
            
        } catch (Exception exc)  {
            logger.fatal(exc.toString() + "\n" + sql + "\n",exc);
            throw exc; 
        } finally {
        	stmt.close();
        	conn.close();
        }
    }
    
    
    
    /**
     * creates a select statement for retrieving the entry field display
     * information base on the country Creation date: (3/13/00 11:44:07 AM)
     * 
     * @return ResultSetCache
     * @throws Exception 
     */
    public ResultSetCache selectFieldOptionsStatement() throws Exception
    {
        StringBuffer sql = new StringBuffer();
    	CachedRowSetImpl cachedRs = new CachedRowSetImpl();
        String schema = getCountryProperties().getAptsSchema();
        Connection conn = null;
        Statement stmt = null;

        // build the sql statement, use append since it is more efficient
        sql.append("select GGFIELDNM, GGSTATUS from ");
        sql.append(schema);
        sql.append("GILGUICTL where GGCTYCOD = ");
        sql.append(getSqlString(getOfferingLetterELS().getCOUNTRY()));
        sql.append(" order by GGFIELDNM");

        // prepare the statement to execute
        try
        {
        	conn = getDataSourceTransactionManager().getDataSource().getConnection();
        	stmt = conn.createStatement();
        	logger.debug("###SQL### "+sql);
        	ResultSet results = stmt.executeQuery(sql.toString());
        	
            return new ResultSetCache(results, getOfferingLetterELS().getCOUNTRY(), 0, 0, "");

        } catch (SQLException exc)
        {
            logger.fatal(exc.toString() + "\n" + sql + "\n");
            throw exc;
            
        } catch (Exception exc)  {
            logger.fatal(exc.toString() + "\n" + sql + "\n",exc);
            throw exc; 
        }  finally {	
        	stmt.close();
        	conn.close();
        }
    }
    

    public ResultSet selectROFLineItems() throws Exception
    {
        StringBuffer sql = new StringBuffer();
    	CachedRowSetImpl cachedRs = new CachedRowSetImpl();
        String schema = getCountryProperties().getGcmsSchema();
        Statement stmt = null;
        Connection conn = null;

        // build the sql statement, use append since it is more efficient
        sql.append("select ");
        sql.append("ROFITEMNUM, '0', ");
        sql.append("ROFTYPE,");
        sql.append("ROFMODEL,");
        sql.append("ROFMCHQTY,");
        sql.append("ROFAMTFIN, ");
        sql.append("'NNHW', '0' ");
        sql.append("FROM ");
        sql.append(schema);
        sql.append("ROFPF where ROFCTYCD = ");
        sql.append(getSqlString(getOfferingLetterELS().getCOUNTRY()));
        sql.append(" and ROFOLNUM = ");
        sql.append(getSqlString(getOfferingLetterELS().getOFFERINGNUMBER()));
        sql.append(" and ROFEXTOL = ");
        sql.append(getSqlString(getOfferingLetterELS().getEXTERNALOFFERLETTERNUMBER()));
        sql.append(" order by ROFITEMNUM, ROFTYPE, ROFAMTFIN ");

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

        } catch (SQLException exc)
        {
            logger.fatal(exc.toString() + "\n" + sql + "\n");
            throw exc;
            
        } catch (Exception exc)  {
            logger.fatal(exc.toString() + "\n" + sql + "\n",exc);
            throw exc; 
        } finally {
        	
        	stmt.close();
        	conn.close();
        	
        }
    }

}
