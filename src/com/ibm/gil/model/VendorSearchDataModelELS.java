package com.ibm.gil.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;




import com.ibm.gil.business.VendorSearchELS;
import com.ibm.igf.hmvc.DB2;
import com.sun.rowset.CachedRowSetImpl;

public class VendorSearchDataModelELS extends DataModel{


	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(VendorSearchDataModelELS.class);
	private VendorSearchELS vendorParent;

	public VendorSearchDataModelELS(VendorSearchELS vendorParent){
		super(vendorParent);
		this.vendorParent=vendorParent;
	}
	
	 public ResultSet selectSupplierNumberStatement() throws Exception
	    {
		 logger.debug("inside vendor search datamodel selectSupplierNumberStatement");
	        return doSelect("stsuplr");
	    }
	 
	 public ResultSet selectSupplierNameStatement() throws Exception
	    {
		  logger.debug("inside vendor search datamodel selectSupplierNameStatement");
	        return doSelect("stname");
	    }
	 
	 public ResultSet selectVATRegistrionStatement() throws Exception
	    {
		    logger.debug("inside vendor search datamodel selectVATRegistrionStatement");
	        return doSelect("stvatreg#");
	    }
	
	
    public ResultSet doSelect(String field) throws Exception
    {

        StringBuffer sql = new StringBuffer();
//        String region = "APTS";
        String schema = getCountryProperties().getAptsSchema();
        Statement stmt = null;
        ResultSet results = null;
        String queryItem = vendorParent.getQUERYITEM();
        Connection conn = null;
        
        CachedRowSetImpl cachedRs = new CachedRowSetImpl();
        
        conn = getDataSourceTransactionManager().getDataSource().getConnection();
    	stmt = conn.createStatement();
    	
//        try
//        {
//            getDatabaseDriver().getConnection(region, getCOUNTRY()).setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
//        } catch (Exception exc)
//        {
//            // don't worry about it
//        }

        // copy the country code in from the invoice header
//        InvoiceDataModel anInvoiceDataModel = (InvoiceDataModel) getEventController().getParentController().getDataModel();
        
        // check for the exact match and append wildcard if not present
        
        int count = 0;
        if(field.equals("stname")){
        	if((queryItem.indexOf('%') < 0) && (queryItem.indexOf('_') < 0))
        		queryItem += "%";
        }else{
        	sql.append("select count(*) from ");
            sql.append(schema);
            sql.append("suplrtbll1 where ");
            sql.append(field);
            if ((queryItem.indexOf('%') < 0) && (queryItem.indexOf('_') < 0))
            {
                sql.append(" = ");
            	
            } else
            {
                sql.append(" like ");
            }
            sql.append(DB2.sqlString(queryItem));
            sql.append(" and STCTYCOD =");
            sql.append(getSqlString(vendorParent.getCOUNTRY()));
            sql.append(" WITH UR");
            
            logger.debug("queryItem :"+queryItem);
            logger.debug("inside vendor search datamodel query :"+ sql );
            
            	stmt = conn.createStatement();
            	results = stmt.executeQuery(sql.toString());
            	
          
//            results = getDatabaseDriver().executeQuery(getDatabaseDriver().getConnection(region, getCOUNTRY()), sql.toString());

            // return the results
            if (results.next())
            {
            	logger.debug("inside results if ");
                count = results.getInt(1);
                logger.debug("count:"+count);
                if (count == 0)
                {
                		queryItem += "%";
                }
            }        	
        }
        

        // build the select statement, use append since it is more efficient
        sql.setLength(0);
        sql.append("select stname, stsuplr, STDFTCOMM, STADDR1, STADDR2, STADDR3, STSRA#, STSRAEFFDT, STVATREG#, STSRASTRDT, LEGID, STBLSUPIND from ");
        sql.append(schema);
        sql.append("suplrtbll1 left outer join ");
        sql.append(schema);
        sql.append("SRANUMPF on STCTYCOD = CNTCOD where ");
        sql.append(field);
        if ((queryItem.indexOf('%') < 0) && (queryItem.indexOf('_') < 0))
        {
            sql.append(" = ");
        } else
        {
            sql.append(" like ");
        }
        sql.append(DB2.sqlString(queryItem));
        sql.append(" and STCTYCOD =");
        sql.append(getSqlString(vendorParent.getCOUNTRY()));
        sql.append(" ORDER BY stname, stsuplr WITH UR");
        
        logger.debug("second sql: " +sql);
        // prepare the statement to execute
        try{
        	stmt = conn.createStatement();
        	results = stmt.executeQuery(sql.toString());
        	cachedRs.populate(results);
        	return cachedRs;
        	//return results;
        }catch (SQLException e){
            logger.fatal(e.toString() + "\n" + sql + "\n");
            throw e;
        } catch (Exception exc)
        {
            logger.fatal(exc.toString() + "\n" + sql + "\n");
            throw exc;
        } finally	{
			
			stmt.close();
			conn.close();
        }
    }
}
