
package com.ibm.gil.model;

import com.ibm.gil.business.Indexing;
import com.ibm.gil.business.MiscInvoice;

import com.ibm.gil.util.RegionalBigDecimal;
import com.ibm.gil.util.RegionalDateConverter;
import com.ibm.igf.gil.CountryProperties;
import com.ibm.igf.hmvc.*;
import com.sun.rowset.CachedRowSetImpl;
import java.sql.*;
import java.text.ParseException;
import java.util.*;
import java.util.Date;

import javax.naming.NamingException;
//import javax.sql.DataSource;

//import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//import com.ibm.gil.util.PropertiesManager;

// Referenced classes of package com.ibm.gil.model:
//            DataModel

public class MiscInvoiceDataModel extends DataModel
{
	
	
	  private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(MiscInvoiceDataModel.class);
	    private MiscInvoice miscInvoice = null;
	    private static transient ResultSetCache invoiceSuffixesResults = null;
	    private static transient ResultSetCache invoiceTypesResults = null;
	    private static transient ResultSetCache VATCodesResults = null;
	    private static transient ResultSetCache POEXCodesResults = null;
	    private static transient ResultSetCache POEXDescResults = null;
	    private static transient ResultSetCache POEXDescIniResults = null;
	    private static transient ResultSetCache countryResults = null;
	    private static transient ResultSetCache ProvinceResults = null;
	    private static transient ResultSetCache IniCAVatresults = null;
	    private static transient ResultSetCache CAVatresults = null;
	    private static transient ResultSetCache ocrRequiredResults = null;
	    private static transient ResultSetCache currencyResults = null;
	    private static transient ResultSetCache CAVatDescresults = null;
	    public boolean db2TransactionFailed;
	   
	    Hashtable VATPercentages;

    public MiscInvoiceDataModel(MiscInvoice miscInvoice)
    {
        super(miscInvoice);               
        VATPercentages = null;
        this.miscInvoice = miscInvoice;
    }

    public MiscInvoice getmiscInvoice()
    {
    	if(miscInvoice==null)
    		miscInvoice=new MiscInvoice();    	
        return miscInvoice;
    }

    public void setmiscInvoice(MiscInvoice miscInvoice)
    {
        this.miscInvoice = miscInvoice;
    }

    public void setVATPercentages(Hashtable percentages)
    {
        VATPercentages = percentages;
    }

    public RegionalBigDecimal getVATPercentage()
    {
        if(VATPercentages == null)
            return RegionalBigDecimal.ZERO;
        RegionalBigDecimal result = (RegionalBigDecimal)VATPercentages.get(miscInvoice.getVATCODE());
        if(result == null)
            return RegionalBigDecimal.ZERO;
        else
            return result;
    }

    public void insertDetailStatement()
        throws SQLException, IllegalArgumentException, ParseException
    {
        Date now = new Date();
        StringBuffer sql = new StringBuffer();
        String region = "APTS";
        CountryProperties countryProperties = CountryManager.getCountryProperties(miscInvoice.getCOUNTRY());
        String schema = countryProperties.getAptsSchema();
        String jndiName = countryProperties.getGaptsJndi();
        CachedRowSetImpl cachedRs = new CachedRowSetImpl();
        Connection conn = null;
        Statement stmt = null;
        String username = trimUSERID(miscInvoice.getUSERID());
        sql.append("insert into ");
        sql.append(schema);
        sql.append("cntinvd values (");
        sql.append("''");
        sql.append(",");
        sql.append(getSqlString(miscInvoice.getCOUNTRY()));
        sql.append(",");
        sql.append(getSqlString(miscInvoice.getCOMPANYCODE()));
        sql.append(",");
        sql.append(getSqlString(miscInvoice.getVENDORNUMBER()));
        sql.append(",");
        sql.append(getSqlString(miscInvoice.getINVOICENUMBER()));
        sql.append(",");
        sql.append(RegionalDateConverter.convertDate("GUI", "DB2INSERT", miscInvoice.getINVOICEDATE()));
        sql.append(",'C',");
        sql.append(getDatabaseDriver().sqlString(username));
        sql.append(",");
        sql.append(RegionalDateConverter.formatDate("DB2INSERT", now));
        sql.append(",");
        sql.append(RegionalDateConverter.formatTime("DB2INSERT", now));
        sql.append(",");
        sql.append(getSqlString(miscInvoice.getDBCR()));
        sql.append(",");
        sql.append(getSqlString(miscInvoice.getINVOICETYPE()));
        sql.append(",");
        sql.append(getSqlString(miscInvoice.getCURRENCY()));
        sql.append(", 0.00, 0.00, '')");
        try
        {
            conn = getDataSourceTransactionManager().getDataSource().getConnection();
            stmt = conn.createStatement();
            int i = stmt.executeUpdate(sql.toString());
        }
        catch(SQLException exc)
        {
            db2TransactionFailed = true;
            throw exc;
        } finally {
        	conn.close();
        	stmt.close();
        }
    }

    public void updateDetailsStatement()
        throws SQLException, IllegalArgumentException, ParseException
    {
        Date now = new Date();
        StringBuffer sql = new StringBuffer();
        String region = "APTS";
        CountryProperties countryProperties = CountryManager.getCountryProperties(miscInvoice.getCOUNTRY());
        String schema = countryProperties.getAptsSchema();
        String jndiName = countryProperties.getGaptsJndi();
        CachedRowSetImpl cachedRs = new CachedRowSetImpl();
        Connection conn = null;
        Statement stmt = null;
        String username = trimUSERID(miscInvoice.getUSERID());
        sql.append("update ");
        sql.append(schema);
        sql.append("cntinvd set CICMPCODE= ");
        sql.append(getSqlString(miscInvoice.getCOMPANYCODE()));
        sql.append(", CISUPLR# = ");
        sql.append(getSqlString(miscInvoice.getVENDORNUMBER()));
        sql.append(", CIINVDBCR = ");
        sql.append(getSqlString(miscInvoice.getDBCR()));
        sql.append(", CIINVTYPE = ");
        sql.append(getSqlString(miscInvoice.getINVOICETYPE()));
        sql.append(", CICURCDE = ");
        sql.append(getSqlString(miscInvoice.getCURRENCY()));
        sql.append(", cilstdate = ");
        sql.append(RegionalDateConverter.formatDate("DB2INSERT", now));
        sql.append(", cilsttime = ");
        sql.append(RegionalDateConverter.formatTime("DB2INSERT", now));
        sql.append(" where ciinv# = ");
        sql.append(getSqlString(miscInvoice.getINVOICENUMBER()));
        sql.append(" and ciinvdate = ");
        sql.append(RegionalDateConverter.convertDate("GUI", "DB2INSERT", miscInvoice.getINVOICEDATE()));
        sql.append(" and CICTYCOD = ");
        sql.append(getSqlString(miscInvoice.getCOUNTRY()));
        try
        {
            conn = getDataSourceTransactionManager().getDataSource().getConnection();
            stmt = conn.createStatement();
            int i = stmt.executeUpdate(sql.toString());
        }
        catch(SQLException exc)
        {
            db2TransactionFailed = true;
            throw exc;
        } finally {
        	conn.close();
        	stmt.close();
        	
        }
    }

    public void insertInvoiceStatement()
        throws SQLException, IllegalArgumentException, ParseException
    {
        Date now = new Date();
        StringBuffer sql = new StringBuffer();
        String region = "MISC";
        CountryProperties countryProperties = CountryManager.getCountryProperties(miscInvoice.getCOUNTRY());
        String schema = countryProperties.getMiscSchema();
        String jndiName = countryProperties.getGaptsJndi();
        CachedRowSetImpl cachedRs = new CachedRowSetImpl();
        Connection conn = null;
        Statement stmt = null;
        retrieveCreatedDate();
        String username = trimUSERID(miscInvoice.getUSERID());
        String POEXCode = (new StringBuilder()).append("'").append(getSqlString(miscInvoice.getPOEXCODE()).substring(1, 5)).append("'").toString();
        
        //String POEXCode ="'"+ miscInvoice.getPOEXCODE().substring(1, 5)+"'";
        sql.append("insert into ");
        sql.append(schema);
        sql.append("MWINVCNTL (INVCINV#,INVCINVDTE,INVCRCVDTE,INVCSUPNME,INVCTOTAMT,INVCINVAMT,INVCINVBAL,INVCVATAMT,INVCVATBAL");
        sql.append(",INVCUSERID,INVCLSTDAT,INVCLSTTIM,INVCDBCR,INVCCRTDT,INVCACCT,INVCEUSRNM,INVCCTYCD,INVCCMID");
        sql.append(",INVCCURR,INVCCURRC,INVCINVTYP,INVCSUPLR#,INVCPOEX,INVCVAT,INVCMPCDE,INVCOCRKID");
        if(miscInvoice.getCOUNTRY().equals("KR"))
            sql.append(",INVCTXINUM");
        if(miscInvoice.getCOUNTRY().equals("CZ") || miscInvoice.getCOUNTRY().equals("HU") || miscInvoice.getCOUNTRY().equals("PL"))
            sql.append(",INVCTXDTE");
        else
        if(miscInvoice.getCOUNTRY().equals("PL") && !miscInvoice.getTAXSUPPLYDATE().equals(RegionalDateConverter.getBlankDate("GUI")))
            sql.append(",INVCTXDTE");
        if(miscInvoice.getCOUNTRY().equals("CA"))
            sql.append(",INVCPROV");
        if(miscInvoice.getCOUNTRY().equals("CL") || miscInvoice.getCOUNTRY().equals("CO") || miscInvoice.getCOUNTRY().equals("MX") || miscInvoice.getCOUNTRY().equals("PE"))
            sql.append(",INVCSUFX");
        sql.append(") values (");
        sql.append(getSqlString(miscInvoice.getINVOICENUMBER()));
        sql.append(",");
        sql.append(RegionalDateConverter.convertDate("GUI", "DB2INSERT", miscInvoice.getINVOICEDATE()));
        sql.append(",");
        // defect 1832051
        //sql.append("'");
        //sql.append(RegionalDateConverter.formatDate("DB2INSERT", miscInvoice.getCREATEDATEasDate(), TimeZone.getTimeZone(miscInvoice.getServerTimezone())));
        sql.append(RegionalDateConverter.formatDate("DB2INSERT", miscInvoice.getCREATEDATEasDate())); 
       // sql.append("'");
        sql.append(",");
        sql.append(getSqlString(miscInvoice.getVENDORNAME()));
        sql.append(",");
        sql.append(miscInvoice.getTOTALAMOUNT().replace(',', '.'));
        sql.append(",");
        sql.append(miscInvoice.getNETAMOUNT().replace(',', '.'));
        sql.append(",");
        sql.append(miscInvoice.getNETAMOUNT().replace(',', '.'));
        sql.append(",");
        sql.append(miscInvoice.getVATAMOUNT().replace(',', '.'));
        sql.append(",");
        sql.append(miscInvoice.getVATAMOUNT().replace(',', '.'));
        sql.append(",");
        sql.append(getDatabaseDriver().sqlString(username));
        sql.append(",");
        sql.append(RegionalDateConverter.formatDate("DB2INSERT", now));
        sql.append(",");
        sql.append(RegionalDateConverter.formatTime("DB2INSERT", now));
        sql.append(",");
        sql.append(getSqlString(miscInvoice.getDBCR()));
        sql.append(",");
        sql.append(RegionalDateConverter.formatDate("DB2INSERT", now));
        sql.append(",");
        sql.append("''");
        sql.append(",");
        sql.append("''");
        sql.append(",");
        sql.append(getSqlString(miscInvoice.getCOUNTRY()));
        sql.append(",");
        sql.append(getSqlString(miscInvoice.getOBJECTID()));
        sql.append(",");
        sql.append(getSqlString(miscInvoice.getCURRENCY()));
        sql.append(",");
        sql.append("''");
        sql.append(",");
        sql.append(getSqlString(miscInvoice.getINVOICETYPE()));
        sql.append(",");
        sql.append(getSqlString(miscInvoice.getVENDORNUMBER()));
        sql.append(",");
        sql.append(POEXCode);
        sql.append(",");
        sql.append(getSqlString(miscInvoice.getVATCODE().substring(0, 2)));
        sql.append(",");
        sql.append(getSqlString(miscInvoice.getCOMPANYCODE()));
        sql.append(",");
        sql.append(getSqlString(miscInvoice.getOCR()));
        if(miscInvoice.getCOUNTRY().equals("KR"))
        {
            sql.append(",");
            sql.append(getSqlString(miscInvoice.getTAXINVOICENUMBER()));
        }
        if(miscInvoice.getCOUNTRY().equals("CZ") || miscInvoice.getCOUNTRY().equals("HU") || miscInvoice.getCOUNTRY().equals("PL"))
        {
            /*sql.append(",");
            sql.append(RegionalDateConverter.convertDate("GUI", "DB2INSERT", miscInvoice.getTAXSUPPLYDATE()));*/
        	if(miscInvoice.getTAXSUPPLYDATE()!=null && !miscInvoice.getTAXSUPPLYDATE().isEmpty())
       	 	{
            	sql.append(",");
                sql.append(RegionalDateConverter.convertDate("GUI", "DB2INSERT", miscInvoice.getTAXSUPPLYDATE()));
       	 	}else{
       	 		sql.append(",");
      		  	sql.append("'");
                sql.append(RegionalDateConverter.convertDate("GUI", "DB2INSERT", miscInvoice.getTAXSUPPLYDATE()));
                sql.append("'");
       	 	}  
        	
        } else
        if(miscInvoice.getCOUNTRY().equals("PL") && !miscInvoice.getTAXSUPPLYDATE().equals(RegionalDateConverter.getBlankDate("GUI")))
        {
            sql.append(",");
            sql.append(RegionalDateConverter.convertDate("GUI", "DB2INSERT", miscInvoice.getTAXSUPPLYDATE()));
        }
        if(miscInvoice.getCOUNTRY().equals("CA"))
        {
            sql.append(",");
            sql.append(getSqlString(miscInvoice.getPROVINCECODE()));
        }
        if(miscInvoice.getCOUNTRY().equals("CL") || miscInvoice.getCOUNTRY().equals("CO") || miscInvoice.getCOUNTRY().equals("MX") || miscInvoice.getCOUNTRY().equals("PE"))
        {
            sql.append(",");
            sql.append(getSqlString(miscInvoice.getINVOICESUFFIX()));
        }
        sql.append(")");
        logger.debug("Inside insert invoice statement:"+sql.toString());
        try
        {
            conn = getDataSourceTransactionManager().getDataSource().getConnection();
            stmt = conn.createStatement();
            int i = stmt.executeUpdate(sql.toString());
        }
        catch(SQLException exc)
        {
            db2TransactionFailed = true;
            logger.debug("Exception in insertinvoicestatement:"+exc);
            throw exc;
        } finally {
        	conn.close();
        	stmt.close();
        }
    }

    public void insertInvoiceConbination()
        throws SQLException, IllegalArgumentException, ParseException
    {
        StringBuffer sql = new StringBuffer();
        String region = "MISC";
        CountryProperties countryProperties = CountryManager.getCountryProperties(miscInvoice.getCOUNTRY());
        String schema = countryProperties.getMiscSchema();
        String jndiName = countryProperties.getGaptsJndi();
        CachedRowSetImpl cachedRs = new CachedRowSetImpl();
        Connection conn = null;
        Statement stmt =null;
        
        String POEXCodeVal = (new StringBuilder()).append("'").append(getSqlString(miscInvoice.getPOEXCODE()).substring(1, 5)).append("'").toString();
        String POEXCodeDes = (new StringBuilder()).append("'").append(getSqlString(miscInvoice.getPOEXCODE()).substring(POEXCodeVal.length()).replace("'", "")).append("'").toString();
        sql.append("insert into ");
        sql.append(schema);
        sql.append("MINVTYPPF (ITCOUNTRY, ITINVTYP,ITCMPCDE,ITPOEX,ITCURR");
        sql.append(") values (");
        sql.append(getSqlString(miscInvoice.getCOUNTRY()));
        sql.append(",");
        sql.append(getSqlString(getmiscInvoice().getINVOICETYPE()));
        sql.append(",");
        sql.append(getSqlString(getmiscInvoice().getCOMPANYCODE()));
        sql.append(",");
        sql.append(POEXCodeVal.trim());
        sql.append(",");
        sql.append(getSqlString(getmiscInvoice().getCURRENCY()));
        sql.append(")");
        try
        {
            conn = getDataSourceTransactionManager().getDataSource().getConnection();
            stmt = conn.createStatement();
            int i = stmt.executeUpdate(sql.toString());
        }
        catch(SQLException exc)
        {
            db2TransactionFailed = true;
            throw exc;
        } finally{
        	conn.close();
        	stmt.close();
        	
        }
        
    }

    public ResultSetCache selectInvoiceSuffixesStatement()
        throws SQLException
    {
        if(invoiceSuffixesResults != null && invoiceSuffixesResults.getIndexKey().equals(miscInvoice.getCOUNTRY()))
        {
            invoiceSuffixesResults.beforeFirst();
            return invoiceSuffixesResults;
        }
        StringBuffer sql = new StringBuffer();
        String region = "APTS";
        CountryProperties countryProperties = CountryManager.getCountryProperties(miscInvoice.getCOUNTRY());
        String schema = countryProperties.getAptsSchema();
        String jndiName = countryProperties.getGaptsJndi();
        CachedRowSetImpl cachedRs = new CachedRowSetImpl();
        Connection conn =null;
        Statement stmt = null;
        
        sql.append("select SUFXCODE, SUFDFTIND from ");
        sql.append(schema);
        sql.append("SUFXTBL");
        sql.append(" where SUFXCTY= ");
        sql.append(getSqlString(miscInvoice.getCOUNTRY()));
        logger.debug("sql in  selectInvoiceSuffixesStatement:"+sql);
        try
        {
            conn = getDataSourceTransactionManager().getDataSource().getConnection();
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery(sql.toString());
            invoiceSuffixesResults = new ResultSetCache(results, miscInvoice.getCOUNTRY(), 0, 1, "");
            return invoiceSuffixesResults;
        }
        catch(SQLException exc)
        {
            throw exc;
        } finally{
        	conn.close();
        	stmt.close();
        }
    }

    public ResultSetCache selectCurrencyCodesStatement()
        throws SQLException
    {
        if(currencyResults != null && currencyResults.getIndexKey().equals(miscInvoice.getCOUNTRY()))
        {
            currencyResults.beforeFirst();
            return currencyResults;
        }
        StringBuffer sql = new StringBuffer();
        String region = "APTS";
        CountryProperties countryProperties = CountryManager.getCountryProperties(miscInvoice.getCOUNTRY());
        String schema = countryProperties.getAptsSchema();
        String jndiName = countryProperties.getGaptsJndi();
        CachedRowSetImpl cachedRs = new CachedRowSetImpl();
        Connection conn =null;
        Statement stmt =null;
        sql.append("select distinct cccurr, CCCDFT from ");
        sql.append(schema);
        sql.append("ccccntl where cccntcode = ");
        sql.append(getSqlString(miscInvoice.getCOUNTRY()));
        sql.append("  order by cccurr");
        try
        {
            conn = getDataSourceTransactionManager().getDataSource().getConnection();
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery(sql.toString());
            currencyResults = new ResultSetCache(results, miscInvoice.getCOUNTRY(), 0, 1, "*");
            return currencyResults;
        }
        catch(SQLException exc)
        {
            throw exc;
        } finally{
        	conn.close();
        	stmt.close();
        }
    }

    public ResultSetCache selectInvoiceMISCTypesStatement()
        throws SQLException
    {
        if(invoiceTypesResults != null && invoiceTypesResults.getIndexKey().equals(miscInvoice.getCOUNTRY()))
        {
            invoiceTypesResults.beforeFirst();
            return invoiceTypesResults;
        }
        StringBuffer sql = new StringBuffer();
        String region = "MISC";
        CountryProperties countryProperties = CountryManager.getCountryProperties(miscInvoice.getCOUNTRY());
        String schema = countryProperties.getMiscSchema();
        String jndiName = countryProperties.getGaptsJndi();
        CachedRowSetImpl cachedRs = new CachedRowSetImpl();
        Connection conn =null;
        Statement stmt =null ;
        
        sql.append("select ITINVTYP, ITDFTIND from ");
        sql.append(schema);
        sql.append("MINVTYPPF where ITCOUNTRY=");
        sql.append(getSqlString(miscInvoice.getCOUNTRY()));
        try
        {
            conn = getDataSourceTransactionManager().getDataSource().getConnection();
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery(sql.toString());
            invoiceTypesResults = new ResultSetCache(results, miscInvoice.getCOUNTRY(), 0, 1, "*");
            return invoiceTypesResults;
        }
        catch(SQLException exc)
        {
            throw exc;
        } finally{
        	conn.close();
        	stmt.close();
        }
    }

    public ResultSetCache selectMISCCombinationCodesStatement(String defaultInvoiceType, String defaultCurrency)
        throws SQLException
    {
        if(POEXCodesResults != null && POEXCodesResults.getIndexKey().equals(miscInvoice.getCOUNTRY()))
        {
            POEXCodesResults.beforeFirst();
            return POEXCodesResults;
        }
        StringBuffer sql = new StringBuffer();
        String region = "MISC";
        CountryProperties countryProperties = CountryManager.getCountryProperties(miscInvoice.getCOUNTRY());
        String schema = countryProperties.getMiscSchema();
        String jndiName = countryProperties.getGaptsJndi();
        CachedRowSetImpl cachedRs = new CachedRowSetImpl();
        Connection conn =null;
        Statement stmt =null;
        
        sql.append("select ITPOEX, ITCMPCDE from ");
        sql.append(schema);
        sql.append("MINVTYPPF where ITCOUNTRY=");
        sql.append(getSqlString(miscInvoice.getCOUNTRY()));
        sql.append("and ITINVTYP =");
        sql.append((new StringBuilder()).append("'").append(defaultInvoiceType).append("'").toString());
        sql.append(" and ITCURR =");
        sql.append((new StringBuilder()).append("'").append(defaultCurrency).append("'").toString());
        sql.append(" order by ITCOUNTRY");
        logger.debug("inside selectMISCCombinationCodesStatement sql: " + sql);
        try
        {
            conn = getDataSourceTransactionManager().getDataSource().getConnection();
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery(sql.toString());
            POEXCodesResults = new ResultSetCache(results, miscInvoice.getCOUNTRY(), 0, 1, "");
            return POEXCodesResults;
        }
        catch(SQLException exc)
        {
            throw exc;
        } finally{
        	conn.close();
        	stmt.close();
        }
    }

    public ResultSetCache selectProvinceCodesStatement()
        throws SQLException
    {
        if(ProvinceResults != null && ProvinceResults.getIndexKey().equals(miscInvoice.getCOUNTRY()))
        {
            ProvinceResults.beforeFirst();
            return ProvinceResults;
        }
        CountryProperties countryProperties = CountryManager.getCountryProperties(miscInvoice.getCOUNTRY());
        String schema = countryProperties.getAptsSchema();
        String jndiName = countryProperties.getGaptsJndi();
        CachedRowSetImpl cachedRs = new CachedRowSetImpl();
        StringBuffer sql = new StringBuffer();
        Connection conn = null;
        Statement stmt = null;
        String region = "APTS";
        sql.append("select PT1PROV from ");
        sql.append(schema);
        sql.append("PROVTAX1");
        sql.append(" where PT1DFTFLG= ");
        sql.append("'*'");
        sql.append(" order by PT1PROV desc");
        logger.debug("sql in selectProvinceCodesStatement"+sql);
        try
        {
            conn = getDataSourceTransactionManager().getDataSource().getConnection();
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery(sql.toString());
            ProvinceResults = new ResultSetCache(results, miscInvoice.getCOUNTRY(), 0, 0, "");
            return ProvinceResults;
        }
        catch(SQLException exc)
        {
            throw exc;
        }finally{
        	conn.close();
        	stmt.close();
        }
        
    }

    public ResultSetCache selectVATCodesStatement()
        throws SQLException
    {
        CountryProperties countryProperties = CountryManager.getCountryProperties(miscInvoice.getCOUNTRY());
        String schema = countryProperties.getAptsSchema();
        String jndiName = countryProperties.getGaptsJndi();
        CachedRowSetImpl cachedRs = new CachedRowSetImpl();
        Connection conn=null;
        Statement stmt = null;
        if(VATCodesResults != null && VATCodesResults.getIndexKey().equals(miscInvoice.getCOUNTRY()))
        {
            VATCodesResults.beforeFirst();
            return VATCodesResults;
        }
        StringBuffer sql = new StringBuffer();
        String region = "APTS";
        sql.append("select VMCCODE, VMVAT, VMDFTIND, VMVATDESC, VMVATPCT from ");
        sql.append(schema);
        sql.append("VATMPF where VMCCODE=");
        sql.append(getSqlString(miscInvoice.getCOUNTRY()));
        sql.append(" order by VMCCODE");
        try
        {
            conn = getDataSourceTransactionManager().getDataSource().getConnection();
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery(sql.toString());
            VATCodesResults = new ResultSetCache(results, miscInvoice.getCOUNTRY(), 1, 2, "*");
            return VATCodesResults;
        }
        catch(SQLException exc)
        {
            throw exc;
        }
        finally{
        	conn.close();
        	stmt.close();
        }
    }

    public ResultSet selectCAVATCodesStatement()
        throws SQLException, IllegalArgumentException, ParseException
    {
       /* if(CAVatresults != null && CAVatresults.getIndexKey().equals(miscInvoice.getPROVINCECODE()))
        {
            CAVatresults.beforeFirst();
            return CAVatresults;
        }*/
        StringBuffer sql = new StringBuffer();
        String region = "APTS";
        Connection conn =null;
        Statement stmt =null;
        CountryProperties countryProperties = CountryManager.getCountryProperties(miscInvoice.getCOUNTRY());
        String schema = countryProperties.getAptsSchema();
        String jndiName = countryProperties.getGaptsJndi();
        CachedRowSetImpl cachedRs = new CachedRowSetImpl();
        
        String Date = miscInvoice.getINVOICEDATE();
        String invoiceDate = "'"+RegionalDateConverter.convertDate("DB2", "GUI", miscInvoice.getINVOICEDATE())+"'";
        logger.debug("inside selectCAVATCodesStatement");
        sql.append("select PT1TAXCD, PT1PCT, PT1DFTFLG from ");
        sql.append(schema);
        sql.append("PROVTAX1 where PT1PROV=");
        sql.append(getSqlString(miscInvoice.getPROVINCECODE()));
        sql.append(" and PT1EFFFR <= ");
        //sql.append(RegionalDateConverter.convertDate("GUI", "DB2INSERT", miscInvoice.getINVOICEDATE()));
        sql.append(invoiceDate);
        sql.append(" and PT1EFFTO >= ");
        sql.append(invoiceDate);
        //sql.append(RegionalDateConverter.convertDate("GUI", "DB2INSERT", miscInvoice.getINVOICEDATE()));
        sql.append(" order by PT1TAXCD desc");
        logger.debug("Sql in selectCAVATCodesStatement:"+sql);
        try
        {
            conn = getDataSourceTransactionManager().getDataSource().getConnection();
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery(sql.toString());        
           
            cachedRs.populate(results);             
            return cachedRs;
           /* CAVatresults = new ResultSetCache(results, miscInvoice.getPROVINCECODE(), 0, 1, "*");
            return CAVatresults;*/
        }
        catch(SQLException exc)
        {
            logger.debug("exception in selectCAVATCodesStatement:"+exc);
        	throw exc;
        }
        finally{
        	conn.close();
        	stmt.close();
        }
    }

    //public ResultSetCache selectCAIniVATCodesStatement(String ProvinceCodeDefault)
    public ResultSet selectCAIniVATCodesStatement(String ProvinceCodeDefault)
        throws SQLException, IllegalArgumentException, ParseException
    {
        CountryProperties countryProperties = CountryManager.getCountryProperties(miscInvoice.getCOUNTRY());
        String schema = countryProperties.getAptsSchema();
        String jndiName = countryProperties.getGaptsJndi();
        CachedRowSetImpl cachedRs = new CachedRowSetImpl();
        /*if(IniCAVatresults != null && IniCAVatresults.getIndexKey().equals(ProvinceCodeDefault))
        {
            IniCAVatresults.beforeFirst();
            return IniCAVatresults;
        }*/
        StringBuffer sql = new StringBuffer();
        String region = "APTS";
        sql.append("select PT1TAXCD, PT1DFTFLG, PT1PCT from ");
        sql.append(schema);
        sql.append("PROVTAX1 where PT1PROV=");
        sql.append((new StringBuilder()).append("'").append(ProvinceCodeDefault).append("'").toString());
        sql.append(" and PT1EFFFR <= ");
        sql.append(RegionalDateConverter.convertDate("GUI", "DB2INSERT", miscInvoice.getINVOICEDATE()));
        sql.append(" and PT1EFFTO >= ");
        sql.append(RegionalDateConverter.convertDate("GUI", "DB2INSERT", miscInvoice.getINVOICEDATE()));
        sql.append(" order by PT1TAXCD desc");
        logger.debug("inside selectCAIniVATCodesStatement sql : " + sql);
        try
        {
            Connection conn = getDataSourceTransactionManager().getDataSource().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery(sql.toString());
            cachedRs.populate(results);
            //IniCAVatresults = new ResultSetCache(results, ProvinceCodeDefault, 0, 2, "");
            //return IniCAVatresults;
            return cachedRs;
        }
        catch(SQLException exc)
        {
            throw exc;
        }
    }
    
    
    
    public ResultSetCache selectCAVATCodeDescription(String vatCode)
            throws SQLException, IllegalArgumentException, ParseException
        {
            if(CAVatDescresults != null && CAVatDescresults.getIndexKey().equals(vatCode))
            {
            	CAVatDescresults.beforeFirst();
                return CAVatDescresults;
            }
            StringBuffer sql = new StringBuffer();           
            String region = "APTS";
            Connection conn =null;
            Statement stmt =null;
            CountryProperties countryProperties = CountryManager.getCountryProperties(miscInvoice.getCOUNTRY());
            String schema = countryProperties.getAptsSchema();
            String jndiName = countryProperties.getGaptsJndi();
            CachedRowSetImpl cachedRs = new CachedRowSetImpl();
            sql.append("select PT2DESC from ");
            sql.append(schema);
            sql.append("PROVTAX2 where PT2TAXCD=");
            sql.append("'");
            sql.append(vatCode);
            sql.append("'");
         
           
            try
            {
                conn = getDataSourceTransactionManager().getDataSource().getConnection();
                stmt = conn.createStatement();
                ResultSet results = stmt.executeQuery(sql.toString());
                CAVatDescresults = new ResultSetCache(results, vatCode, 0, 1, "*");
                return CAVatDescresults;
            }
            catch(SQLException exc)
            {
                logger.debug("exception in selectCAVATCodeDescription:"+exc);
            	throw exc;
            }
            finally{
            	conn.close();
            	stmt.close();
            }
        }
    
    //Test Defect 1814285 Gil Misc- Canada- default tax code with multiple effective dates
    public ResultSet selectCAIniVATCodesStatementDef(String ProvinceCodeDefault,String methodName ) throws SQLException, IllegalArgumentException, ParseException
    {
    	
    	StringBuffer sql = new StringBuffer();
        String region = "APTS";      
        
        CountryProperties countryProperties = CountryManager.getCountryProperties(miscInvoice.getCOUNTRY());
        String schema = countryProperties.getAptsSchema();
        String jndiName = countryProperties.getGaptsJndi();
        CachedRowSetImpl cachedRs = new CachedRowSetImpl();
        Connection conn = null;
        Statement stmt = null;
        String invoiceDate = null;
        if(methodName.equalsIgnoreCase("SCVC")){
         invoiceDate = "'"+RegionalDateConverter.convertDate("DB2", "GUI", miscInvoice.getINVOICEDATE())+"'";
         logger.debug("inside if" + invoiceDate);
        }
        else {
        
        invoiceDate = RegionalDateConverter.convertDate("GUI", "DB2INSERT", miscInvoice.getINVOICEDATE());
        logger.debug("inside else :" + invoiceDate );
        }
        
        logger.debug("invoiceDate" + invoiceDate );
        
       
        

        // build the sql statement, use append since it is more efficient
       
        /*sql.append("select PT1TAXCD, PT1DFTFLG from ");
        sql.append(schema);
        sql.append("PROVTAX1 where PT1PROV=");
        sql.append("'"+ProvinceCodeDefault+"'");
        sql.append(" order by PT1TAXCD desc");*/
        logger.debug("inside selectCAIniVATCodesStatementDef");
        
        sql.append("select PT1TAXCD, PT1DFTFLG from ");
        sql.append(schema);
        sql.append(" PROVTAX1 where PT1PROV=");
        sql.append("'"+ProvinceCodeDefault+"'");
        sql.append("and ");
        sql.append("PT1EFFTO >"); 
        sql.append(invoiceDate);        
        sql.append(" order by PT1EFFTO asc");
        
        logger.debug("sql inside selectCAIniVATCodesStatementDef :"+ sql );
                
        // prepare the statement to execute
        try
        {
            
            
            conn = getDataSourceTransactionManager().getDataSource().getConnection();
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery(sql.toString());
            cachedRs.populate(results);          
        
           
            // return the results           
            return cachedRs;
        } catch (SQLException exc)
        {
            logger.debug("exception in selectCAIniVATCodesStatementDef :"+exc);
            throw exc;
        }finally {
        	
        	conn.close();
        	stmt.close();
        }
    }
    //End Test Defect 1814285 

    public ResultSetCache selectCountryInfoStatement()
        throws SQLException
    {
        CountryProperties countryProperties = CountryManager.getCountryProperties(miscInvoice.getCOUNTRY());
        String schema = countryProperties.getAptsSchema();
        String jndiName = countryProperties.getGaptsJndi();
        CachedRowSetImpl cachedRs = new CachedRowSetImpl();
        Connection conn =null;
        Statement stmt = null;
        if(countryResults != null && countryResults.getIndexKey().equals(miscInvoice.getCOUNTRY()))
        {
            countryResults.beforeFirst();
            return countryResults;
        }
        StringBuffer sql = new StringBuffer();
        String region = "APTS";
        sql.append("select cccntcode, cccmpcode, cccurr from ");
        sql.append(schema);
        sql.append("ccccntl where cccdft = '*' and cccntcode = ");
        sql.append(getSqlString(miscInvoice.getCOUNTRY()));
        try
        {
            conn = getDataSourceTransactionManager().getDataSource().getConnection();
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery(sql.toString());
            countryResults = new ResultSetCache(results, miscInvoice.getCOUNTRY(), 0, 1, "*");
            return countryResults;
        }
        catch(SQLException exc)
        {
        	
            throw exc;
        }
        finally{
        	conn.close();
        	stmt.close();
        }
    }

    public ResultSetCache selectOCRRequiredStatement()
        throws SQLException
    {
        CountryProperties countryProperties = CountryManager.getCountryProperties(miscInvoice.getCOUNTRY());
        String schema = countryProperties.getAptsSchema();
        String jndiName = countryProperties.getGaptsJndi();
        CachedRowSetImpl cachedRs = new CachedRowSetImpl();
        Connection conn =null;
        Statement stmt =null;
        if(ocrRequiredResults != null && ocrRequiredResults.getIndexKey().equals(miscInvoice.getCOUNTRY()))
        {
            ocrRequiredResults.beforeFirst();
            return ocrRequiredResults;
        }
        StringBuffer sql = new StringBuffer();
        String region = "APTS";
        sql.append("select OKCSTATUS, OKCCTYCOD from ");
        sql.append(schema);
        sql.append("OCRKIDCTL where OKCCTYCOD = ");
        sql.append(getSqlString(miscInvoice.getCOUNTRY()));
        try
        {
            conn = getDataSourceTransactionManager().getDataSource().getConnection();
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery(sql.toString());
            ocrRequiredResults = new ResultSetCache(results, miscInvoice.getCOUNTRY(), 0, 1, "");
            return ocrRequiredResults;
        }
        catch(SQLException exc)
        {
            throw exc;
        } finally {
        	conn.close();
        	stmt.close();
        }
    }

    public ResultSetCache selectMISCPOEXDescStatement(String PoexCode)
        throws SQLException
    {
        CountryProperties countryProperties = CountryManager.getCountryProperties(getmiscInvoice().getCOUNTRY());
        String schema = countryProperties.getMiscSchema();
        String jndiName = countryProperties.getGaptsJndi();
        CachedRowSetImpl cachedRs = new CachedRowSetImpl();
        Connection conn =null;
        Statement stmt = null;
        if(POEXDescResults != null && POEXDescResults.getIndexKey().equals(getmiscInvoice().getCOUNTRY()))
        {
            POEXDescResults.beforeFirst();
            return POEXDescResults;
        }
        StringBuffer sql = new StringBuffer();
        String region = "MISC";
        sql.append("select MPDESC from ");
        sql.append(schema);
        sql.append("mpoexmpf where MPCCODE=");
        sql.append(getSqlString(miscInvoice.getCOUNTRY()));
        sql.append(" and MPPOEX =");
        sql.append("'" + PoexCode +"'");
        sql.append(" order by MPCCODE");
        try
        {
            conn = getDataSourceTransactionManager().getDataSource().getConnection();
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery(sql.toString());
            POEXDescResults = new ResultSetCache(results, getmiscInvoice().getCOUNTRY(), 0, 0, "*");
            return POEXDescResults;
        }
        catch(SQLException exc)
        {
            throw exc;
        } finally {
        	conn.close();
        	stmt.close();
        }
        
    }

    public ResultSetCache selectIniMISCPOEXDescStatement(String PoexCode)
        throws SQLException
    {
        StringBuffer sql = new StringBuffer();
        String region = "MISC";
        CountryProperties countryProperties = CountryManager.getCountryProperties(miscInvoice.getCOUNTRY());
        String schema = countryProperties.getMiscSchema();
        String jndiName = countryProperties.getGaptsJndi();
        CachedRowSetImpl cachedRs = new CachedRowSetImpl();
        Connection conn =null;
        Statement stmt =null;
        sql.append("select MPDESC from ");
        sql.append(schema);
        sql.append("mpoexmpf where MPCCODE=");
        sql.append(getSqlString(miscInvoice.getCOUNTRY()));
        sql.append(" and MPPOEX =");
        sql.append((new StringBuilder()).append("'").append(PoexCode).append("'").toString());
        sql.append(" order by MPCCODE");
        try
        {
             conn = getDataSourceTransactionManager().getDataSource().getConnection();
             stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery(sql.toString());
            POEXDescIniResults = new ResultSetCache(results, "", 0, 0, "*");
            return POEXDescIniResults;
        }
        catch(SQLException exc)
        {
            throw exc;
        } finally{
        	conn.close();
        	stmt.close();
        }
    }
    
    public  ResultSetCache selectPOEXCodesStatement() throws SQLException, NamingException
    {

/*        if ((POEXCodesResults != null) && (POEXCodesResults.getIndexKey().equals(getIndexingObj().getCOUNTRY())))
        {
            POEXCodesResults.beforeFirst();
            return (POEXCodesResults);
        }*/

        StringBuffer sql = new StringBuffer();
        String schema = getCountryProperties().getMiscSchema();
        PreparedStatement pstmt = null;
        Connection conn = null;


        sql.append("select MPCCODE, MPPOEX, MPDFTIND, MPDESC from ");
        sql.append(schema);
        sql.append("mpoexmpf where MPCCODE = ?");
        sql.append(" order by MPCCODE");

        try
        {

        	 logger.debug("Searching for POEX Codes");
        	 
          	conn = getDataSourceTransactionManager().getDataSource().getConnection();
            pstmt = conn.prepareStatement(sql.toString());
            pstmt.setString(1, getIndexingObj().getCOUNTRY());
            ResultSet results =  pstmt.executeQuery();
        	
            POEXCodesResults = new ResultSetCache(results,getIndexingObj().getCOUNTRY(), 1, 3, "R");
            
            logger.debug("Found:"+ POEXCodesResults.size());

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

    public ResultSet selectDetailsCountStatement()
        throws SQLException, IllegalArgumentException, ParseException
    {
        StringBuffer sql = new StringBuffer();
        String region = "APTS";
        CountryProperties countryProperties = CountryManager.getCountryProperties(miscInvoice.getCOUNTRY());
        String schema = countryProperties.getAptsSchema();
        String jndiName = countryProperties.getGaptsJndi();
        CachedRowSetImpl cachedRs = new CachedRowSetImpl();
        Connection conn =null; 
        Statement stmt =null; 
        sql.append("select count(*) from ");
        sql.append(schema);
        sql.append("cntinvd where cictycod = ");
        sql.append(getSqlString(miscInvoice.getOLDCOUNTRY()));
        sql.append(" and ciinv# = ");
        sql.append(getSqlString(miscInvoice.getOLDINVOICENUMBER()));
        sql.append(" and ciinvdate = ");
        sql.append(RegionalDateConverter.convertDate("GUI", "DB2INSERT", miscInvoice.getOLDINVOICEDATE()));
        try
        {
            conn = getDataSourceTransactionManager().getDataSource().getConnection();
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery(sql.toString());
            cachedRs.populate(results);
            return cachedRs;
        }
        catch(SQLException exc)
        {
            throw exc;
        } finally{
        	conn.close();
        	stmt.close();
        }
    }

    public ResultSet selectINGSTStatement()
        throws SQLException, IllegalArgumentException, ParseException
    {
        StringBuffer sql = new StringBuffer();
        String region = "APTS";
        CountryProperties countryProperties = CountryManager.getCountryProperties(miscInvoice.getCOUNTRY());
        String schema = countryProperties.getMiscSchema();
        String jndiName = countryProperties.getGaptsJndi();
        CachedRowSetImpl cachedRs = new CachedRowSetImpl();
        Connection conn =null;
        Statement stmt= null;
        
        sql.append("select IHBILLTO,IHBILLFRM,IHCGSTAMT,IHSGSTAMT,IHIGSTAMT,IHGSTREG,IHLOAN,IHSHIPTO from ");
        sql.append(schema);
        sql.append("IGSTHDR where IHCTYCOD = ");
        sql.append(getSqlString(miscInvoice.getCOUNTRY()));
        sql.append(" and IHINV# = ");
        sql.append(getSqlString(miscInvoice.getINVOICENUMBER()));
        sql.append(" and IHINVDATE = ");
        sql.append(RegionalDateConverter.convertDate("GUI", "DB2INSERT", miscInvoice.getINVOICEDATE()));
        try
        {
            conn = getDataSourceTransactionManager().getDataSource().getConnection();
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery(sql.toString());
            cachedRs.populate(results);
            return cachedRs;
        }
        catch(SQLException exc)
        {
            throw exc;
        } finally {
        	conn.close();
        	stmt.close();
        	
        }
        
    }

    public ResultSet selectDetailsStatement()
        throws SQLException, IllegalArgumentException, ParseException
    {
        StringBuffer sql = new StringBuffer();
        String region = "APTS";
        CountryProperties countryProperties = CountryManager.getCountryProperties(miscInvoice.getCOUNTRY());
        String schema = countryProperties.getAptsSchema();
        String jndiName = countryProperties.getGaptsJndi();
        CachedRowSetImpl cachedRs = new CachedRowSetImpl();
        Connection conn =null;
        Statement stmt =null;
        
        sql.append("select invccmid, ciinv#, CIINVDATE from ");
        sql.append(schema);
        sql.append("cntinvd, ");
        sql.append(schema);
        sql.append("winvcntl where cicntnbr = ");
        sql.append("''");
        sql.append(" and cictycod = ");
        sql.append(getSqlString(miscInvoice.getCOUNTRY()));
        sql.append(" and invcinv# = ciinv# and invcinvdte = ciinvdate ");
        sql.append(" and ciinv# = ");
        sql.append(getSqlString(miscInvoice.getINVOICENUMBER()));
        sql.append(" and ciinvdate = ");
        sql.append(RegionalDateConverter.convertDate("GUI", "DB2INSERT", miscInvoice.getINVOICEDATE()));
        sql.append(" and cisuplr# = ");
        sql.append(getSqlString(miscInvoice.getVENDORNUMBER()));
        sql.append(" and cipaymthd = 'C'");
        try
        {
            conn = getDataSourceTransactionManager().getDataSource().getConnection();
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery(sql.toString());
            cachedRs.populate(results);
            return cachedRs;
        }
        catch(SQLException exc)
        {
            throw exc;
        } finally {
        	conn.close();
        	stmt.close();
        }
    }

    public ResultSet selectLastInvoiceEnteredStatement()
        throws SQLException
    {
        Date now = new Date();
        StringBuffer sql = new StringBuffer();
        String region = "MISC";
        CountryProperties countryProperties = CountryManager.getCountryProperties(miscInvoice.getCOUNTRY());
        String schema = countryProperties.getMiscSchema();
        String jndiName = countryProperties.getGaptsJndi();
        CachedRowSetImpl cachedRs = new CachedRowSetImpl();
        Connection conn = null;
        Statement stmt = null;
        String username = trimUSERID(miscInvoice.getUSERID());
        sql.append("select INVCSUPNME,INVCMPCDE,INVCDBCR,INVCCTYCD,INVCCURR,INVCINVTYP,INVCSUPLR#,INVCPOEX from ");
        sql.append(schema);
        sql.append("MWINVCNTL where INVCCMID =");
        sql.append(getSqlString(miscInvoice.getOBJECTID()));
        sql.append(" and INVCUSERID = ");
        sql.append(getDatabaseDriver().sqlString(username));
        try
        {
            conn = getDataSourceTransactionManager().getDataSource().getConnection();
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery(sql.toString());
            cachedRs.populate(results);
            return cachedRs;
        }
        catch(SQLException exc)
        {
            throw exc;
        } finally{
        	 conn.close();
        	 stmt.close();
        }
    }

    public void updateInvoiceStatement()
        throws SQLException, IllegalArgumentException, ParseException
    {
        Date now = new Date();
        StringBuffer sql = new StringBuffer();
        String region = "MISC";
        CountryProperties countryProperties = CountryManager.getCountryProperties(miscInvoice.getCOUNTRY());
        String schema = countryProperties.getMiscSchema();
        String jndiName = countryProperties.getGaptsJndi();
        CachedRowSetImpl cachedRs = new CachedRowSetImpl();
        Connection conn =null;
        Statement stmt = null; 
        String username = trimUSERID(miscInvoice.getUSERID());
        String POEXCode = (new StringBuilder()).append("'").append(getSqlString(miscInvoice.getPOEXCODE()).substring(1, 5)).append("'").toString();
        sql.append("update ");
        sql.append(schema);
        sql.append("MWINVCNTL set INVCINV# = ");
        sql.append(getSqlString(miscInvoice.getINVOICENUMBER()));
        sql.append(", INVCINVDTE = ");
        sql.append(RegionalDateConverter.convertDate("GUI", "DB2INSERT", miscInvoice.getINVOICEDATE()));
        sql.append(", INVCSUPNME = ");
        sql.append(getSqlString(miscInvoice.getVENDORNAME()));
        sql.append(", INVCTOTAMT = ");
        sql.append(miscInvoice.getTOTALAMOUNT().replace(',', '.'));
        sql.append(", INVCINVAMT = ");
        sql.append(miscInvoice.getNETAMOUNT().replace(',', '.'));
        sql.append(", INVCINVBAL = ");
        sql.append(miscInvoice.getNETAMOUNT().replace(',', '.'));
        sql.append(", INVCVATAMT = ");
        sql.append(miscInvoice.getVATAMOUNT().replace(',', '.'));
        sql.append(", INVCVATBAL = ");
        sql.append(miscInvoice.getVATAMOUNT().replace(',', '.'));
        sql.append(", INVCUSERID = ");
        sql.append(getDatabaseDriver().sqlString(username));
        sql.append(", INVCLSTDAT = ");
        sql.append(RegionalDateConverter.formatDate("DB2INSERT", now));
        sql.append(", INVCLSTTIM = ");
        sql.append(RegionalDateConverter.formatTime("DB2INSERT", now));
        sql.append(", INVCDBCR = ");
        sql.append(getSqlString(miscInvoice.getDBCR()));
        sql.append(", INVCCTYCD = ");
        sql.append(getSqlString(miscInvoice.getCOUNTRY()));
        sql.append(", INVCCMID = ");
        sql.append(getSqlString(miscInvoice.getOBJECTID()));
        sql.append(", INVCCURR = ");
        sql.append(getSqlString(miscInvoice.getCURRENCY()));
        sql.append(", INVCCURRC = ");
        sql.append("''");
        sql.append(", INVCINVTYP = ");
        sql.append(getSqlString(miscInvoice.getINVOICETYPE()));
        sql.append(", INVCSUPLR# = ");
        sql.append(getSqlString(miscInvoice.getVENDORNUMBER()));
        sql.append(", INVCPOEX = ");
        sql.append(POEXCode);
        sql.append(", INVCVAT = ");
        sql.append(getSqlString(miscInvoice.getVATCODE().substring(0, 2)));
        sql.append(", INVCMPCDE = ");
        sql.append(getSqlString(miscInvoice.getCOMPANYCODE()));
        sql.append(", INVCOCRKID = ");
        sql.append(getSqlString(miscInvoice.getOCR()));
        if(miscInvoice.getCOUNTRY().equals("KR"))
        {
            sql.append(", INVCTXINUM = ");
            sql.append(getSqlString(miscInvoice.getTAXINVOICENUMBER()));
        }
        if(miscInvoice.getCOUNTRY().equals("CZ") || miscInvoice.getCOUNTRY().equals("HU") || miscInvoice.getCOUNTRY().equals("PL"))
        {
        	if(miscInvoice.getTAXSUPPLYDATE()!=null && !miscInvoice.getTAXSUPPLYDATE().isEmpty())
       	 {
       		 sql.append(", INVCTXDTE = ");
                sql.append(RegionalDateConverter.convertDate("GUI", "DB2INSERT", miscInvoice.getTAXSUPPLYDATE()));
       	 }else{
       		 sql.append(", INVCTXDTE = ");
      		  	 sql.append("'");
                sql.append(RegionalDateConverter.convertDate("GUI", "DB2INSERT", miscInvoice.getTAXSUPPLYDATE()));
                sql.append("'");
       	 }
        } else
        if(miscInvoice.getCOUNTRY().equals("PL") && !miscInvoice.getTAXSUPPLYDATE().equals(RegionalDateConverter.getBlankDate("GUI")))
        {
            sql.append(", INVCTXDTE = ");
            sql.append(RegionalDateConverter.convertDate("GUI", "DB2INSERT", miscInvoice.getTAXSUPPLYDATE()));
        }
        if(miscInvoice.getCOUNTRY().equals("CA"))
        {
            sql.append(", INVCPROV = ");
            sql.append(getSqlString(miscInvoice.getPROVINCECODE()));
        }
        if(miscInvoice.getCOUNTRY().equals("CL") || miscInvoice.getCOUNTRY().equals("CO") || miscInvoice.getCOUNTRY().equals("MX") || miscInvoice.getCOUNTRY().equals("PE"))
        {
            sql.append(", INVCSUFX = ");
            sql.append(getSqlString(miscInvoice.getINVOICESUFFIX()));
        }
        sql.append(" where INVCCMID = ");
        sql.append(getSqlString(miscInvoice.getOBJECTID()));
        
        logger.debug("sql in updateinvoicestatement:"+ sql);
        try
        {
            conn = getDataSourceTransactionManager().getDataSource().getConnection();
            stmt = conn.createStatement();
            int i = stmt.executeUpdate(sql.toString());
        }
        catch(SQLException exc)
        {
            db2TransactionFailed = true;
            throw exc;
        } finally{
        	conn.close();
        	stmt.close();
        }
    }

    public void updateInvoiceWithContractStatement()
        throws SQLException, IllegalArgumentException, ParseException
    {
        Date now = new Date();
        StringBuffer sql = new StringBuffer();
        StringBuffer sqlc = new StringBuffer();
        String region = "APTS";
        CountryProperties countryProperties = CountryManager.getCountryProperties(miscInvoice.getCOUNTRY());
        String schema = countryProperties.getMiscSchema();
        String jndiName = countryProperties.getGaptsJndi();
        CachedRowSetImpl cachedRs = new CachedRowSetImpl();
        Connection conn = null;
        Statement stmt = null;
        String username = trimUSERID(miscInvoice.getUSERID());
        sql.append("update ");
        sql.append(schema);
        sql.append(" winvcntl set INVCNT# = ");
        sql.append("''");
        sql.append(", invcuserid = ");
        sql.append(getDatabaseDriver().sqlString(username));
        sql.append(", invclstdat = ");
        sql.append(RegionalDateConverter.formatDate("DB2INSERT", now));
        sql.append(", invclsttim = ");
        sql.append(RegionalDateConverter.formatTime("DB2INSERT", now));
        sql.append(", INVCEUSRNM = ");
        sql.append(" ");
        sql.append(", INVCOFFER# = ");
        sql.append(" ");
        sql.append(" where invcinv# = ");
        sql.append(getSqlString(miscInvoice.getINVOICENUMBER()));
        sql.append(" and invcinvdte = ");
        sql.append(RegionalDateConverter.convertDate("GUI", "DB2INSERT", miscInvoice.getINVOICEDATE()));
        sql.append(" AND INVCCTYCD = ");
        sql.append(getSqlString(miscInvoice.getCOUNTRY()));
        try
        {
            conn = getDataSourceTransactionManager().getDataSource().getConnection();
            stmt = conn.createStatement();
            int i = stmt.executeUpdate(sql.toString());
        }
        catch(SQLException exc)
        {
            db2TransactionFailed = true;
            throw exc;
        }
        sqlc.append("update ");
        sqlc.append(schema);
        sqlc.append("winvcntlc set INVCNT#  = ");
        sql.append("''");
        sqlc.append(", INVCOFFER# = ");
        sql.append(" ");
        sqlc.append(" where invcinv# = ");
        sqlc.append(getSqlString(miscInvoice.getINVOICENUMBER()));
        sqlc.append(" and invcinvdte = ");
        sqlc.append(RegionalDateConverter.convertDate("GUI", "DB2INSERT", miscInvoice.getINVOICEDATE()));
        sqlc.append(" AND INVCCTYCD = ");
        sqlc.append(getSqlString(miscInvoice.getCOUNTRY()));
        try
        {
            conn = getDataSourceTransactionManager().getDataSource().getConnection();
            stmt = conn.createStatement();
            int i = stmt.executeUpdate(sql.toString());
        }
        catch(SQLException exc)
        {
            db2TransactionFailed = true;
            throw exc;
        } finally {
        	conn.close();
        	stmt.close();
        	
        }
    }

    public ResultSet selectByObjectIdStatement()
        throws SQLException
    {
        StringBuffer sql = new StringBuffer();
        String region = "MISC";
        CountryProperties countryProperties = CountryManager.getCountryProperties(miscInvoice.getCOUNTRY());
        String schema = countryProperties.getMiscSchema();
        String jndiName = countryProperties.getGaptsJndi();
        CachedRowSetImpl cachedRs = new CachedRowSetImpl();
        Connection conn = null;
        Statement stmt = null;
        sql.append("select INVCINV#,INVCINVDTE,INVCVATAMT,INVCVATBAL,INVCINVAMT,INVCINVBAL,INVCTOTAMT,INVCSUPNME,INVCSUPLR#,INVCDBCR,INVCCURR,INVCINVTYP,INVCPOEX,INVCVAT,INVCMPCDE,INVCPROV,INVCOCRKID,INVCTXDTE,INVCCTYCD,INVCTXINUM,INVCSUFX from ");
        sql.append(schema);
        sql.append("MWINVCNTL");
        sql.append(" where INVCCMID = ");
        sql.append(getSqlString(miscInvoice.getOBJECTID()));
        logger.debug("sql in selectByObjectIdStatement is : "+sql);
        try
        {
            conn = getDataSourceTransactionManager().getDataSource().getConnection();
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery(sql.toString());
            cachedRs.populate(results);
            return cachedRs;
        }
        catch(SQLException exc)
        {
            throw exc;
        } finally {        	
        	conn.close();
        	stmt.close();       	
        	
        }
    }

    public ResultSet selectProvinceByObjectIdStatement()
        throws SQLException
    {
        StringBuffer sql = new StringBuffer();
        String region = "MISC";
        CountryProperties countryProperties = CountryManager.getCountryProperties(miscInvoice.getCOUNTRY());
        String schema = countryProperties.getMiscSchema();
        String jndiName = countryProperties.getGaptsJndi();
        CachedRowSetImpl cachedRs = new CachedRowSetImpl();
        Connection conn = null;
        Statement stmt = null;
        sql.append("select INVCPROV,INVCCTYCD from ");
        sql.append(schema);
        sql.append("MWINVCNTL");
        sql.append(" where INVCCMID = ");
        sql.append(getSqlString(miscInvoice.getOBJECTID()));
        try
        {
            conn = getDataSourceTransactionManager().getDataSource().getConnection();
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery(sql.toString());
            cachedRs.populate(results);
            return cachedRs;
        }
        catch(SQLException exc)
        {
            throw exc;
        } finally {
        	conn.close();
        	stmt.close();
        }
    }

    public ResultSet selectConvinationStatement()
        throws SQLException, IllegalArgumentException, ParseException
    {
        StringBuffer sql = new StringBuffer();
        String region = "MISC";
        CountryProperties countryProperties = CountryManager.getCountryProperties(miscInvoice.getCOUNTRY());
        String schema = countryProperties.getMiscSchema();
        String jndiName = countryProperties.getGaptsJndi();
        CachedRowSetImpl cachedRs = new CachedRowSetImpl();
        Connection conn = null;
        Statement stmt = null;
        sql.append("select ITCOUNTRY,ITINVTYP from ");
        sql.append(schema);
        sql.append("MINVTYPPF");
        sql.append(" where ITCOUNTRY = ");
        sql.append(getSqlString(miscInvoice.getCOUNTRY()));
        sql.append(" and ITINVTYP = ");
        sql.append(getSqlString(miscInvoice.getINVOICETYPE()));
        sql.append(" and ITCURR =");
        sql.append(getSqlString(miscInvoice.getCURRENCY()));
        logger.debug("sql inside selectConvinationStatement(): "+ sql);
        
        try
        {
            conn = getDataSourceTransactionManager().getDataSource().getConnection();
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery(sql.toString());
            cachedRs.populate(results);
            return cachedRs;
        }
        catch(SQLException exc)
        {
            throw exc;
        } finally{
        	conn.close();
        	stmt.close();
        }
    }

    public ResultSet selectMISCCompanyCodeStatement()
        throws SQLException, IllegalArgumentException, ParseException
    {
        StringBuffer sql = new StringBuffer();
        String region = "MISC";
        CountryProperties countryProperties = CountryManager.getCountryProperties(getmiscInvoice().getCOUNTRY());
        String schema = countryProperties.getMiscSchema();
        String jndiName = countryProperties.getGaptsJndi();
        CachedRowSetImpl cachedRs = new CachedRowSetImpl();
        Connection conn = null;
        Statement stmt = null;
        sql.append("select ITCMPCDE, ITPOEX from ");
        sql.append(schema);
        sql.append("MINVTYPPF");
        sql.append(" where ITCOUNTRY = ");
        sql.append(getSqlString(getmiscInvoice().getCOUNTRY()));
        sql.append(" and ITINVTYP =");
        sql.append(getSqlString(getmiscInvoice().getINVOICETYPE()));
        sql.append(" and ITCURR =");
        sql.append(getSqlString(getmiscInvoice().getCURRENCY()));
        logger.debug("sql in selectMISCCompanyCodeStatement:"+sql );
        try
        {
            conn = getDataSourceTransactionManager().getDataSource().getConnection();
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery(sql.toString());
            cachedRs.populate(results);
            return cachedRs;
        }
        catch(SQLException exc)
        {
            throw exc;
        } finally {
        	conn.close();
        	stmt.close();
        }
    }
    
 
    public ResultSet selectMISCDescStatement()
        throws SQLException, IllegalArgumentException, ParseException
    {
        //String POEXCode = (new StringBuilder()).append("'").append(getSqlString(miscInvoice.getPOEXCODE()).substring(1, 5)).append("'").toString();
        String POEXCode= getmiscInvoice().getPOEXCODE();
        StringBuffer sql = new StringBuffer();
        String region = "MISC";
        CountryProperties countryProperties = CountryManager.getCountryProperties(miscInvoice.getCOUNTRY());
        String schema = countryProperties.getMiscSchema();
        String jndiName = countryProperties.getGaptsJndi();
        CachedRowSetImpl cachedRs = new CachedRowSetImpl();
        Connection conn = null;
        Statement stmt = null;
        sql.append("select MPDESC from ");
        sql.append(schema);
        sql.append("mpoexmpf where MPCCODE="); 
        sql.append("'");
        sql.append(getmiscInvoice().getCOUNTRY());  
        sql.append("'");
        sql.append(" and MPPOEX =");
        sql.append("'");
        sql.append(POEXCode);
        sql.append("'");
        sql.append(" order by MPCCODE");
        logger.debug("sql in selectMISCDescStatement:"+ sql);
        try
        {
            conn = getDataSourceTransactionManager().getDataSource().getConnection();
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery(sql.toString());
            cachedRs.populate(results);
            return cachedRs;
        }
        catch(SQLException exc)
        {
            throw exc;
        } finally {
        	conn.close();
        	stmt.close();
        }
    }

    public ResultSet getVATVarianceStatement()
        throws SQLException
    {
        StringBuffer sql = new StringBuffer();
        String region = "APTS";
        CountryProperties countryProperties = CountryManager.getCountryProperties(miscInvoice.getCOUNTRY());
        String schema = countryProperties.getAptsSchema();
        String jndiName = countryProperties.getGaptsJndi();
        CachedRowSetImpl cachedRs = new CachedRowSetImpl();
        Connection conn = null;
        Statement stmt = null;
        sql.append("select VVALWVAR from ");
        sql.append(schema);
        sql.append("VATVARPF where VVCNTCODE=");
        sql.append(getSqlString(miscInvoice.getCOUNTRY()));
        try
        {
            conn = getDataSourceTransactionManager().getDataSource().getConnection();
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery(sql.toString());
            cachedRs.populate(results);
            return cachedRs;
        }
        catch(SQLException exc)
        {
            throw exc;
        } finally {
        	conn.close();
        	stmt.close();
        }
    }

    public ResultSet selectByInvoiceStatement()
        throws SQLException, IllegalArgumentException, ParseException
    {
        StringBuffer sql = new StringBuffer();
        String region = "MISC";
        CountryProperties countryProperties = CountryManager.getCountryProperties(miscInvoice.getCOUNTRY());
        String schema = countryProperties.getMiscSchema();
        String jndiName = countryProperties.getGaptsJndi();
        CachedRowSetImpl cachedRs = new CachedRowSetImpl();
        Connection conn = null;
        Statement stmt = null;
        sql.append("select INVCINV#,INVCINVDTE,INVCCMID from ");
        sql.append(schema);
        sql.append("MWINVCNTL where INVCINV# = ");
        sql.append(getSqlString(miscInvoice.getINVOICENUMBER()));
        sql.append(" and INVCINVDTE = ");
        sql.append(RegionalDateConverter.convertDate("GUI", "DB2INSERT", miscInvoice.getINVOICEDATE()));
        //defect 1832063 Supplier number is not required for duplicate invoice 
       /* sql.append(" and INVCSUPLR# = ");
        sql.append(getSqlString(miscInvoice.getVENDORNUMBER()));*/
        logger.debug("sql in selectByInvoiceStatement :"+sql);
        try
        {
            conn = getDataSourceTransactionManager().getDataSource().getConnection();
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery(sql.toString());
            cachedRs.populate(results);
            return cachedRs;
        }
        catch(SQLException exc)
        {
            throw exc;
        } finally {
        	
        	conn.close();
        	stmt.close();
        }
    }

    public void recalculateNetAmount()
    {
        RegionalBigDecimal total = RegionalBigDecimal.ZERO.setScale(2);
        RegionalBigDecimal tax = RegionalBigDecimal.ZERO.setScale(2);
        try
        {
            total = (new RegionalBigDecimal(miscInvoice.getTOTALAMOUNT())).setScale(2);
        }
        catch(NumberFormatException exc) { }
        try
        {
            tax = (new RegionalBigDecimal(miscInvoice.getVATAMOUNT())).setScale(2);
        }
        catch(NumberFormatException exc) { }
        RegionalBigDecimal net = total.subtract(tax);
        RegionalBigDecimal percentage = getVATPercentage();
        RegionalBigDecimal targetVat = RegionalBigDecimal.ZERO.setScale(2);
        try
        {
            if(percentage.compareTo(RegionalBigDecimal.ZERO) != 0)
            {
                RegionalBigDecimal hundred = (new RegionalBigDecimal("100")).setScale(2);
                targetVat = total.multiply(percentage);
                targetVat = targetVat.setScale(4, 4);
                targetVat = targetVat.divide(percentage.add(hundred), 4);
                targetVat = targetVat.setScale(2, 4);
            }
        }
        catch(NumberFormatException exc) { }
        miscInvoice.setVATTARGETAMOUNT(targetVat.toString());
        miscInvoice.setTOTALAMOUNT(total.toString());
        miscInvoice.setVATAMOUNT(tax.toString());
        miscInvoice.setNETAMOUNT(net.toString());
    }

    public boolean isCommissionInvoice()
    {
        if(miscInvoice.getINVOICETYPE().trim().equals("COM"))
            return true;
        return miscInvoice.getINVOICETYPE().trim().equals("STP");
    }

    public boolean isCreditInvoice()
    {
        return miscInvoice.getDBCR().trim().equals("CR");
    }

    public boolean isIBMInvoice()
    {
        return miscInvoice.getINVOICETYPE().trim().equals("IBM");
    }

    public boolean isCompanyCodeOf0001()
    {
        return miscInvoice.getCOMPANYCODE().trim().equals("0001");
    }

    public boolean isCOMInvoice()
    {
        return miscInvoice.getINVOICETYPE().trim().equals("COM");
    }

    public boolean isEXEMPTorStartswithDEVofSRNumber()
    {
        if(miscInvoice.getSRNUMBER().trim().equals("EXEMPT"))
            return true;
        return miscInvoice.getSRNUMBER().startsWith("DEV");
    }

    public ResultSet selectInvoiceStatement()
        throws SQLException, IllegalArgumentException, ParseException
    {
        StringBuffer sql = new StringBuffer();
        String region = "APTS";
        CountryProperties countryProperties = CountryManager.getCountryProperties(miscInvoice.getCOUNTRY());
        String schema = countryProperties.getMiscSchema();
        String jndiName = countryProperties.getGaptsJndi();
        CachedRowSetImpl cachedRs = new CachedRowSetImpl();
        Connection conn = null;
        Statement stmt = null;
        sql.append("select invcinv#, invcinvdte, INVCEUSRNM from ");
        sql.append(schema);
        sql.append("winvcntl ");
        sql.append("where INVCOFFER# = ");
        sql.append("''");
        sql.append(" and invcctycd = ");
        sql.append(getSqlString(miscInvoice.getCOUNTRY()));
        try
        {
            conn = getDataSourceTransactionManager().getDataSource().getConnection();
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery(sql.toString());
            cachedRs.populate(results);
            return cachedRs;
        }
        catch(SQLException exc)
        {
            throw exc;
        } finally {
        	
        	conn.close();
        	stmt.close();
        }
    }

    public String getHash()
    {
        return (new StringBuilder()).append(miscInvoice.getINVOICENUMBER()).append("~").append(miscInvoice.getINVOICEDATE()).toString();
    }

    public ResultSet selectCheckBySupplierStatement()
        throws SQLException, IllegalArgumentException, ParseException
    {
        StringBuffer sql = new StringBuffer();
        String region = "MISC";
        CountryProperties countryProperties = CountryManager.getCountryProperties(miscInvoice.getCOUNTRY());
        String schema = countryProperties.getMiscSchema();
        String jndiName = countryProperties.getGaptsJndi();
        CachedRowSetImpl cachedRs = new CachedRowSetImpl();
        Connection conn = null;
        Statement stmt = null;
        sql.append("select * from ");
        sql.append(schema);
        sql.append("MWINVCNTL where INVCINV# = ");
        sql.append(getSqlString(miscInvoice.getINVOICENUMBER()));
        sql.append(" and INVCSUPLR# = ");
        sql.append(getSqlString(miscInvoice.getVENDORNUMBER()));
        sql.append(" and INVCCTYCD = ");
        sql.append(getSqlString(miscInvoice.getCOUNTRY()));
        sql.append(" and INVCCMID <> ");
        sql.append(getSqlString(miscInvoice.getOBJECTID()));
        logger.debug("sql is :"+sql);
        try
        {
            conn = getDataSourceTransactionManager().getDataSource().getConnection();
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery(sql.toString());
            cachedRs.populate(results);     
            return cachedRs;
        }
        catch(SQLException exc)
        {
            throw exc;
        } finally {
        	
        	conn.close();
        	stmt.close();
        }
        
    }

    public ResultSet retrieveInvoiceVarianceAmt()
        throws SQLException, IllegalArgumentException, ParseException
    {
        StringBuffer sql = new StringBuffer();
        String region = "MISC";
        CountryProperties countryProperties = CountryManager.getCountryProperties(miscInvoice.getCOUNTRY());
        String schema = countryProperties.getMiscSchema();
        String jndiName = countryProperties.getGaptsJndi();
        CachedRowSetImpl cachedRs = new CachedRowSetImpl();
        Connection conn = null;
        Statement stmt = null;
        sql.append("select IVVARTYPE from ");
        sql.append(schema);
        sql.append("MWINVVAR where IVINV# = ");
        sql.append(getSqlString(miscInvoice.getOLDINVOICENUMBER()));
        sql.append(" and IVINVDATE = ");
        sql.append(RegionalDateConverter.convertDate("GUI", "DB2INSERT", miscInvoice.getOLDINVOICEDATE()));
        try
        {
            conn = getDataSourceTransactionManager().getDataSource().getConnection();
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery(sql.toString());
            cachedRs.populate(results);
            return cachedRs;
        }
        catch(SQLException exc)
        {
            throw exc;
        } finally {
        	
        	conn.close();
        	stmt.close();
        }
        
    }
    // check with Miguel  if this is used -Krishna 
 /*   public boolean isContractNumberRecordExists()
        throws SQLException, IllegalArgumentException, ParseException
    {
        StringBuffer sql = new StringBuffer();
        String region = "APTS";
        CountryProperties countryProperties = CountryManager.getCountryProperties(miscInvoice.getCOUNTRY());
        String schema = countryProperties.getAptsSchema();
        String jndiName = countryProperties.getGaptsJndi();
        CachedRowSetImpl cachedRs = new CachedRowSetImpl();
        Connection conn = null;
        Statement stmt = null;
        sql.append("select * from ");
        sql.append(schema);
        sql.append("cntinvd where  ciinv#=");
        sql.append(getSqlString(miscInvoice.getINVOICENUMBER()));
        sql.append(" AND ciinvdate =");
        sql.append(RegionalDateConverter.convertDate("GUI", "DB2INSERT", miscInvoice.getINVOICEDATE()));
        sql.append(" and cictycod =");
        sql.append(getSqlString(miscInvoice.getCOUNTRY()));
        sql.append(" and cicntnbr = ");
        sql.append("''");
        int countcontractnumber = 0;
        try
        {
            conn = getDataSourceTransactionManager().getDataSource().getConnection();
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery(sql.toString());
            if(!results.next())
                return false;
        }
        catch(SQLException exc)
        {
            throw exc;
        } finally {
        	
        	conn.close();
        	stmt.close();
        }
        
        return true;
    } */

    public void rollbackIndexing()
    {
        try
        {
            ResultSet results = selectByObjectIdStatement();
            if(results.next())
            {
                miscInvoice.setINVOICENUMBER(getDatabaseDriver().getString(results, 1));
                getDatabaseDriver();
                String invoiceDate = DB2.getString(results, 2);
                invoiceDate = RegionalDateConverter.convertDate("DB2", "GUI", invoiceDate);
                miscInvoice.setINVOICEDATE(invoiceDate);
                miscInvoice.setVENDORNAME(getDatabaseDriver().getString(results, 8));
            }
        }
        catch(Exception exc) { }
        updateIndexValues();
    }

    public void rollbackCountryIndexing()
    {
        try
        {
            String objectId = miscInvoice.getOBJECTID();
            CM = getContentManager();
            if(objectId.trim().length() > 0)
            {
                boolean retry = false;
                do
                {
                    boolean rc = CM.moveDocument(objectId, (new StringBuilder()).append("Invoice ").append(miscInvoice.getCOUNTRY()).toString(), miscInvoice.getDatastore());
                    if(rc)
                        retry = false;
                } while(retry);
            }
        }
        catch(Exception exc) { }
    }

    public void updateIndexValues()
    {
        try
        {
            String invoiceDate = RegionalDateConverter.convertDate("GUI", "CM", miscInvoice.getINVOICEDATE());
            String objectId = miscInvoice.getOBJECTID();
            CM = getContentManager();
            String fields[] = {
                "Invoice Number", "Invoice Date", "Vendor Name"
            };
            String values[] = {
                miscInvoice.getINVOICENUMBER(), invoiceDate, miscInvoice.getVENDORNAME()
            };
            if(objectId.trim().length() > 0)
            {
                boolean retry = false;
                do
                {
                    boolean rc = CM.updateDocument(objectId, fields, values, miscInvoice.getDatastore());
                    if(rc)
                        retry = false;
                } while(retry);
            }
        }
        catch(Exception exc) { }
    }

    public void insertDocument()
    {
        try
        {
            insertInvoiceStatement();
            continueWorkflow();
        }
        catch(Exception exc)
        {
            db2TransactionFailed = true;
            logger.debug("Exception in insertDocument: "+ exc);
            return;
        }
        miscInvoice.setDIRTYFLAG(Boolean.FALSE.booleanValue());
        miscInvoice.setDOCUMENTMODE((Indexing.UPDATEMODE));
    }

    public void updateDocument()
    {
        try
        {
            updateInvoiceStatement();
        }
        catch(Exception exc)
        {
            db2TransactionFailed = true;
            return;
        }
        miscInvoice.setDIRTYFLAG(Boolean.FALSE.booleanValue());
    }
        
   
  
    

    public void saveDocument()
    {
    	logger.debug("Inside save document");
        try
        {
        	logger.debug("ADD mode in misc invoice:"+getmiscInvoice().getDOCUMENTMODE());
            if(getmiscInvoice().getDOCUMENTMODE().equals((getmiscInvoice().ADDMODE)))
                insertDocument();
            else
                updateDocument();
        }
        catch(Exception sqx)
        {
            logger.debug((new StringBuilder()).append("Invoice saveDocument ").append(sqx.toString()).append("\n").append(sqx.getMessage()).append("\n").toString());
            logger.debug("Exception in saveDocument: "+sqx);
        }
    }

  

}
