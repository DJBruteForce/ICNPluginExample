/*
 * Created on Aug 20, 2004
 *
 */
package com.ibm.gil.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



import com.ibm.gil.business.VendorSearch;
import com.ibm.igf.hmvc.DB2;
import com.sun.rowset.CachedRowSetImpl;


public class VendorSearchDataModel extends DataModel {
	
	
	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(VendorSearchDataModel.class);
	
	private VendorSearch vendorSearch; 
	
	public VendorSearchDataModel(VendorSearch vendorSearch)  {
		
		super(vendorSearch);
		this.vendorSearch = vendorSearch;
		
	}

    public VendorSearch getVendorSearch() {
		return vendorSearch;
	}

	public void setVendorSearch(VendorSearch vendorSearch) {
		this.vendorSearch = vendorSearch;
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
    public ResultSet selectAccountStatement() throws Exception
    {
    	String queryItem = getVendorSearch().getQUERYITEM();
        StringBuffer sql = new StringBuffer();
        String schema = getCountryProperties().getAptsSchema();
        Statement stmt = null;
    	CachedRowSetImpl cachedRs = new CachedRowSetImpl();
        Connection conn = null;
        String comparisonOperator = "";
        if ((queryItem.indexOf('%') < 0) && (queryItem.indexOf('_') < 0))
        {
        	comparisonOperator = " = ";
        } else
        {
        	comparisonOperator = " like ";
        }
        
        

        sql.append("select count(*) from ");
        sql.append(schema);
        sql.append("accttbl, ");
        sql.append(schema);
        sql.append("suplrtbl where atsuplr# = stsuplr and (atacct# ");

        sql.append(comparisonOperator);
        
        sql.append(DB2.sqlString(queryItem));
        
        sql.append(" or atacct# ");
        
        sql.append(comparisonOperator);
        
        sql.append(DB2.sqlString(queryItem.toUpperCase()));
        
        sql.append(") and STCTYCOD =");
        sql.append(getSqlString(getVendorSearch().getCOUNTRY()));
       
    	conn = getDataSourceTransactionManager().getDataSource().getConnection();
    	stmt = conn.createStatement();
    	logger.debug("###SQL### "+sql);
    	ResultSet results = stmt.executeQuery(sql.toString());


        if (results.next())
        {
            int count = results.getInt(1);
            if (count == 0)
            {
                queryItem += "%";
            }
        }

        sql.setLength(0);
        sql.append("select atacct#, stname, atsuplr#, ataddr1, ataddr2, ataddr3, atname, STDFTCOMM, STADDR1, STADDR2, STADDR3, STVATREG#, STSRA#, STBLSUPIND from ");
        sql.append(schema);
        sql.append("accttbl, ");
        sql.append(schema);
        sql.append("suplrtbl where atsuplr# = stsuplr and atacct# ");
        if ((queryItem.indexOf('%') < 0) && (queryItem.indexOf('_') < 0))
        {
            sql.append(" = ");
        } else
        {
            sql.append(" like ");
        }
        sql.append(DB2.sqlString(queryItem));
        sql.append(" and STCTYCOD =");
        sql.append(getSqlString(getVendorSearch().getCOUNTRY()));
        sql.append(" ORDER BY stname, stsuplr WITH UR");

        try
        {
        	stmt = conn.createStatement();
        	logger.debug("###SQL### "+sql);
        	results = stmt.executeQuery(sql.toString());
            //cachedRs = RowSetProvider.newFactory().createCachedRowSet();
            cachedRs.populate(results);
            
            return (cachedRs);
            
        } catch (SQLException exc)
        {
            logger.fatal(exc.toString() + "\n" + sql + "\n",exc);
            throw exc;
        } catch (Exception exc)
        {
            logger.fatal(exc.toString() + "\n" + sql + "\n",exc);
            throw exc;
        } finally	{
			
			stmt.close();
			conn.close();
        }
    }

    /**
     * creates a select statement for retrieving account/supplier data by
     * supplier name Creation date: (3/13/00 11:44:07 AM)
     * 
     * @return java.lang.String
     * @throws Exception 
     */
    public ResultSet selectSupplierStatement() throws Exception
    {
    	String queryItem = getVendorSearch().getQUERYITEM();
    	
        StringBuffer sql = new StringBuffer();
        String schema = getCountryProperties().getAptsSchema();
        Statement stmt = null;
    	CachedRowSetImpl cachedRs = new CachedRowSetImpl();
        Connection conn = null;


        if ((queryItem.indexOf('%') < 0) && (queryItem.indexOf('_') < 0))
        {
        	queryItem += "%";
        } 

        sql.append("select atacct#, stname, stsuplr, ataddr1, ataddr2, ataddr3, atname, STDFTCOMM, STADDR1, STADDR2, STADDR3, STVATREG#, STSRA#, STBLSUPIND from ");
        sql.append(schema);
        sql.append("suplrtbll1 left join ");
        sql.append(schema);
        sql.append("accttbll1 on atsuplr# = stsuplr where (stname like ");
        sql.append(DB2.sqlString(queryItem));
        sql.append(" or stname like ");
        sql.append(DB2.sqlString(queryItem.toUpperCase()));
        sql.append(") and STCTYCOD =");
        sql.append(getSqlString(getVendorSearch().getCOUNTRY()));
        sql.append(" ORDER BY stname, stsuplr WITH UR");

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
        } catch (Exception exc)
        {
            logger.fatal(exc.toString() + "\n" + sql + "\n",exc);
            throw exc;
        } finally	{
			
			stmt.close();
			conn.close();
        }
    }
    
    /**
     * creates a select statement for retrieving account/supplier data by
     * supplier name Creation date: (3/13/00 11:44:07 AM)
     * 
     * @return java.lang.String
     * @throws Exception 
     */
    public ResultSet selectSupplierNumberStatement() throws Exception
    {
    	String queryItem = getVendorSearch().getQUERYITEM();
    	
        StringBuffer sql = new StringBuffer();
        String schema = getCountryProperties().getAptsSchema();
        Statement stmt = null;
    	CachedRowSetImpl cachedRs = new CachedRowSetImpl();
        Connection conn = null;


        if ((queryItem.indexOf('%') < 0) && (queryItem.indexOf('_') < 0))
        {
        	queryItem += "%";
        } 
        // build the select statement, use append since it is more efficient

        sql.append("select atacct#, stname, stsuplr, ataddr1, ataddr2, ataddr3, atname, STDFTCOMM, STADDR1, STADDR2, STADDR3, STVATREG#, STSRA#, STBLSUPIND from ");
        sql.append(schema);
        sql.append("suplrtbll1 left join ");
        sql.append(schema);
        sql.append("accttbll1 on atsuplr# = stsuplr where stsuplr like ");
        sql.append(DB2.sqlString(queryItem));
        sql.append(" and STCTYCOD =");
        sql.append(getSqlString(getVendorSearch().getCOUNTRY()));
        sql.append(" ORDER BY stname, stsuplr WITH UR");

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
            
        }catch (Exception exc)
        {
            logger.fatal(exc.toString() + "\n" + sql + "\n",exc);
            throw exc;
        }  finally	{
			
			stmt.close();
			conn.close();
        }
    }    
}
