package com.ibm.gil.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



import com.ibm.gil.business.COAELS;
import com.sun.rowset.CachedRowSetImpl;

public class COADataModelELS  extends DataModel {

	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(COADataModelELS.class);
	
	public COADataModelELS(COAELS coaEls)  {	
		super(coaEls);
		this.coaEls = coaEls;
	}
    
    private COAELS coaEls = null;
     
    public COAELS getCOAELS() {
		return coaEls;
	}
    
    /**
     * create a select statement for retieving the contract control data from
     * Global APTS by contract number / country code Creation date: (3/13/00
     * 11:44:07 AM)
     * 
     * @return ResultSet
     * @throws Exception 
     */
    public ResultSet selectByContractStatementFromGAPTS() throws Exception
    {
        StringBuffer sql = new StringBuffer();
    	Statement stmt = null;
    	CachedRowSetImpl cachedRs = new CachedRowSetImpl();
        String schema = getCountryProperties().getAptsSchema();
        Connection conn = null;

        // build the sql statement, use append because it is more efficent
        sql.append("select cccnt#, ccctycod, CCCMOBJID from ");
        sql.append(schema);
        sql.append("cntcntl where cccnt# = ");
        sql.append(getSqlString(getCOAELS().getCOANUMBER()));
        sql.append("and ccctycod = ");

        if (getCOAELS().getCOUNTRY().equals("GB"))
        {
            sql.append("UK");
        } else
        {
            sql.append(getSqlString(getCOAELS().getCOUNTRY()));
        }

        // prepare the statement to execute
        try
        {
        	conn = getDataSourceTransactionManager().getDataSource().getConnection();
            stmt = conn.createStatement();
            logger.debug("###SQL### "+sql);
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
        }  finally {   	
        	stmt.close();
        	conn.close();	
        }
    }    
}
