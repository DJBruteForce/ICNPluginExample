

package com.ibm.gil.model;

import com.ibm.gil.business.MiscVendorSearch;
import com.ibm.igf.hmvc.DB2;
import com.sun.rowset.CachedRowSetImpl;
import java.sql.*;



// Referenced classes of package com.ibm.gil.model:
//            DataModel, VendorSearchDataModel

public class MiscVendorSearchDataModel extends DataModel
{

	
	 private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(MiscVendorSearchDataModel.class);
	    private MiscVendorSearch miscVendorSearch;
    public MiscVendorSearchDataModel(MiscVendorSearch miscVendorSearch)
    {
        super(miscVendorSearch);
        this.miscVendorSearch = miscVendorSearch;
    }

    public MiscVendorSearch getMiscVendorSearch()
    {
        return miscVendorSearch;
    }

    public void setVendorSearch(MiscVendorSearch miscVendorSearch)
    {
        this.miscVendorSearch = miscVendorSearch;
    }

    public ResultSet selectAccountStatement()
        throws Exception
    {
        StringBuffer sql;
        Statement stmt;
        CachedRowSetImpl cachedRs;
        Connection conn;
        String queryItem = getMiscVendorSearch().getQUERYITEM();
        sql = new StringBuffer();
        String schema = getCountryProperties().getAptsSchema();
        stmt = null;
        cachedRs = new CachedRowSetImpl();
        conn = null;
        String comparisonOperator = "";
        if(queryItem.indexOf('%') < 0 && queryItem.indexOf('_') < 0)
            comparisonOperator = " = ";
        else
            comparisonOperator = " like ";
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
        sql.append(getSqlString(getMiscVendorSearch().getCOUNTRY()));
        conn = getDataSourceTransactionManager().getDataSource().getConnection();
        stmt = conn.createStatement();
        ResultSet results = stmt.executeQuery(sql.toString());
        if(results.next())
        {
            int count = results.getInt(1);
            if(count == 0)
                queryItem = (new StringBuilder()).append(queryItem).append("%").toString();
        }
        sql.setLength(0);
        sql.append("select atacct#, stname, atsuplr#, ataddr1, ataddr2, ataddr3, atname, STDFTCOMM, STADDR1, STADDR2, STADDR3, STVATREG#, STSRA#, STBLSUPIND from ");
        sql.append(schema);
        sql.append("accttbl, ");
        sql.append(schema);
        sql.append("suplrtbl where atsuplr# = stsuplr and atacct# ");
        if(queryItem.indexOf('%') < 0 && queryItem.indexOf('_') < 0)
            sql.append(" = ");
        else
            sql.append(" like ");
        sql.append(DB2.sqlString(queryItem));
        sql.append(" and STCTYCOD =");
        sql.append(getSqlString(getMiscVendorSearch().getCOUNTRY()));
        sql.append(" ORDER BY stname, stsuplr WITH UR");
        CachedRowSetImpl cachedrowsetimpl;
        try
        {
            stmt = conn.createStatement();
            results = stmt.executeQuery(sql.toString());
            cachedRs.populate(results);
            cachedrowsetimpl = cachedRs;
        }
        catch(SQLException exc)
        {
            logger.fatal((new StringBuilder()).append(exc.toString()).append("\n").append(sql).append("\n").toString());
            throw exc;
        }
        catch(Exception exc)
        {
            logger.fatal((new StringBuilder()).append(exc.toString()).append("\n").append(sql).append("\n").toString());
            throw exc;
        }
        finally{
        	stmt.close();
            conn.close();	
        }
        
        return cachedrowsetimpl;
       /* Exception exception;
        exception;*/
       /* stmt.close();
        conn.close();*/
      // throw exception;
    }

    public ResultSet selectSupplierStatement()
        throws Exception
    {
        StringBuffer sql;
        Statement stmt;
        CachedRowSetImpl cachedRs;
        Connection conn;
        String queryItem = getMiscVendorSearch().getQUERYITEM();
        sql = new StringBuffer();
        String schema = getCountryProperties().getAptsSchema();
        stmt = null;
        cachedRs = new CachedRowSetImpl();
        conn = null;
        if(queryItem.indexOf('%') < 0 && queryItem.indexOf('_') < 0)
            queryItem = (new StringBuilder()).append(queryItem).append("%").toString();
        sql.append("select atacct#, stname, stsuplr, ataddr1, ataddr2, ataddr3, atname, STDFTCOMM, STADDR1, STADDR2, STADDR3, STVATREG#, STSRA#, STBLSUPIND from ");
        sql.append(schema);
        sql.append("suplrtbll1 left join ");
        sql.append(schema);
        sql.append("accttbll1 on atsuplr# = stsuplr where (stname like ");
        sql.append(DB2.sqlString(queryItem));
        sql.append(" or stname like ");
        sql.append(DB2.sqlString(queryItem.toUpperCase()));
        sql.append(") and STCTYCOD =");
        sql.append(getSqlString(getMiscVendorSearch().getCOUNTRY()));
        sql.append(" ORDER BY stname, stsuplr WITH UR");
        CachedRowSetImpl cachedrowsetimpl;
        try
        {
            conn = getDataSourceTransactionManager().getDataSource().getConnection();
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery(sql.toString());
            cachedRs.populate(results);
            cachedrowsetimpl = cachedRs;
        }
        catch(SQLException exc)
        {
            logger.fatal((new StringBuilder()).append(exc.toString()).append("\n").append(sql).append("\n").toString());
            throw exc;
        }
        catch(Exception exc)
        {
            logger.fatal((new StringBuilder()).append(exc.toString()).append("\n").append(sql).append("\n").toString());
            throw exc;
        }
        finally{
        stmt.close();
        conn.close();
        }
        return cachedrowsetimpl;
        /*Exception exception;
        exception;
        stmt.close();
        conn.close();
        throw exception;*/
    }

    public ResultSet selectSupplierNumberStatement()
        throws Exception
    {
        StringBuffer sql;
        Statement stmt;
        CachedRowSetImpl cachedRs;
        Connection conn;
        String queryItem = getMiscVendorSearch().getQUERYITEM();
        sql = new StringBuffer();
        String schema = getCountryProperties().getAptsSchema();
        stmt = null;
        cachedRs = new CachedRowSetImpl();
        conn = null;
        if(queryItem.indexOf('%') < 0 && queryItem.indexOf('_') < 0)
            queryItem = (new StringBuilder()).append(queryItem).append("%").toString();
        sql.append("select atacct#, stname, stsuplr, ataddr1, ataddr2, ataddr3, atname, STDFTCOMM, STADDR1, STADDR2, STADDR3, STVATREG#, STSRA#, STBLSUPIND from ");
        sql.append(schema);
        sql.append("suplrtbll1 left join ");
        sql.append(schema);
        sql.append("accttbll1 on atsuplr# = stsuplr where stsuplr like ");
        sql.append(DB2.sqlString(queryItem));
        sql.append(" and STCTYCOD =");
        sql.append(getSqlString(getMiscVendorSearch().getCOUNTRY()));
        sql.append(" ORDER BY stname, stsuplr WITH UR");
        CachedRowSetImpl cachedrowsetimpl;
        try
        {
            conn = getDataSourceTransactionManager().getDataSource().getConnection();
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery(sql.toString());
            cachedRs.populate(results);
            cachedrowsetimpl = cachedRs;
        }
        catch(SQLException exc)
        {
            logger.fatal((new StringBuilder()).append(exc.toString()).append("\n").append(sql).append("\n").toString());
            throw exc;
        }
        catch(Exception exc)
        {
            logger.fatal((new StringBuilder()).append(exc.toString()).append("\n").append(sql).append("\n").toString());
            throw exc;
        }
        finally{
        stmt.close();
        conn.close();
        }
        return cachedrowsetimpl;
       /* Exception exception;
        exception;
        stmt.close();
        conn.close();
        throw exception;*/
    }

   

}
