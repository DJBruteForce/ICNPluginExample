

package com.ibm.gil.business;

import com.ibm.gil.model.MiscVendorSearchDataModel;
import com.ibm.gil.util.RegionalBigDecimal;
import com.ibm.igf.hmvc.DB2;
import java.sql.ResultSet;
import java.util.ArrayList;


// Referenced classes of package com.ibm.gil.business:
//            Indexing, VendorSearch

public class MiscVendorSearch extends Indexing
{

	
	
	 private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(MiscVendorSearch.class);
	    private MiscVendorSearchDataModel miscVendorSearchDataModel;
	    private String ACCOUNTNUMBER;
	    private String VENDORNAME;
	    private String VENDORNUMBER;
	    private String ADDRESS;
	    private String ADDRESS2;
	    private String ADDRESS3;
	    private String CUSTOMERNAME;
	    private String VENDORCOMMISSION;
	    private String SUPPLIERADDRESS;
	    private String SUPPLIERADDRESS2;
	    private String SUPPLIERADDRESS3;
	    private String VATREGISTRATIONNUMBER;
	    private String BLOCKINDC;
	    private String SRNUMBER;
	    private String QUERYITEM;
	    public static final String ALLX = "XXXXXX";
	    public static final short SEARCHBYNAME = 1;
	    public static final short SEARCHBYNUMBER = 2;
	    public static final short SEARCHBYACCOUNT = 3;
	    private ArrayList vendorSearchTableList;
    public MiscVendorSearch()
    {
        miscVendorSearchDataModel = null;
    }

    public MiscVendorSearch(String country)
    {
        miscVendorSearchDataModel = null;
        setCOUNTRY(country);
        miscVendorSearchDataModel = new MiscVendorSearchDataModel(this);
    }

    public MiscVendorSearchDataModel getVendorSearchDataModel()
    {
        return miscVendorSearchDataModel;
    }

    public void setVendorSearchDataModel(MiscVendorSearchDataModel miscVendorSearchDataModel)
    {
        this.miscVendorSearchDataModel = miscVendorSearchDataModel;
    }

    public String getACCOUNTNUMBER()
    {
        return ACCOUNTNUMBER;
    }

    public void setACCOUNTNUMBER(String aCCOUNTNUMBER)
    {
        ACCOUNTNUMBER = aCCOUNTNUMBER;
    }

    public String getVENDORNAME()
    {
        return VENDORNAME;
    }

    public void setVENDORNAME(String vENDORNAME)
    {
        VENDORNAME = vENDORNAME;
    }

    public String getVENDORNUMBER()
    {
        return VENDORNUMBER;
    }

    public void setVENDORNUMBER(String vENDORNUMBER)
    {
        VENDORNUMBER = vENDORNUMBER;
    }

    public String getADDRESS()
    {
        return ADDRESS;
    }

    public void setADDRESS(String aDDRESS)
    {
        ADDRESS = aDDRESS;
    }

    public String getADDRESS2()
    {
        return ADDRESS2;
    }

    public void setADDRESS2(String aDDRESS2)
    {
        ADDRESS2 = aDDRESS2;
    }

    public String getADDRESS3()
    {
        return ADDRESS3;
    }

    public void setADDRESS3(String aDDRESS3)
    {
        ADDRESS3 = aDDRESS3;
    }

    public String getCUSTOMERNAME()
    {
        return CUSTOMERNAME;
    }

    public void setCUSTOMERNAME(String cUSTOMERNAME)
    {
        CUSTOMERNAME = cUSTOMERNAME;
    }

    public String getVENDORCOMMISSION()
    {
        return VENDORCOMMISSION;
    }

    public void setVENDORCOMMISSION(String vENDORCOMMISSION)
    {
        VENDORCOMMISSION = vENDORCOMMISSION;
    }

    public String getSUPPLIERADDRESS()
    {
        return SUPPLIERADDRESS;
    }

    public void setSUPPLIERADDRESS(String sUPPLIERADDRESS)
    {
        SUPPLIERADDRESS = sUPPLIERADDRESS;
    }

    public String getSUPPLIERADDRESS2()
    {
        return SUPPLIERADDRESS2;
    }

    public void setSUPPLIERADDRESS2(String sUPPLIERADDRESS2)
    {
        SUPPLIERADDRESS2 = sUPPLIERADDRESS2;
    }

    public String getSUPPLIERADDRESS3()
    {
        return SUPPLIERADDRESS3;
    }

    public void setSUPPLIERADDRESS3(String sUPPLIERADDRESS3)
    {
        SUPPLIERADDRESS3 = sUPPLIERADDRESS3;
    }

    public String getVATREGISTRATIONNUMBER()
    {
        return VATREGISTRATIONNUMBER;
    }

    public void setVATREGISTRATIONNUMBER(String vATREGISTRATIONNUMBER)
    {
        VATREGISTRATIONNUMBER = vATREGISTRATIONNUMBER;
    }

    public String getBLOCKINDC()
    {
        return BLOCKINDC;
    }

    public void setBLOCKINDC(String bLOCKINDC)
    {
        BLOCKINDC = bLOCKINDC;
    }

    public String getSRNUMBER()
    {
        return SRNUMBER;
    }

    public void setSRNUMBER(String sRNUMBER)
    {
        SRNUMBER = sRNUMBER;
    }

    public String getQUERYITEM()
    {
        return QUERYITEM;
    }

    public void setQUERYITEM(String qUERYITEM)
    {
        QUERYITEM = qUERYITEM;
    }

    public static String getAllx()
    {
        return "XXXXXX";
    }

    public static short getSearchbyname()
    {
        return 1;
    }

    public static short getSearchbynumber()
    {
        return 2;
    }

    public static short getSearchbyaccount()
    {
        return 3;
    }

    public ArrayList selectAccountStatement()
        throws Exception
    {
        return loadResults(getVendorSearchDataModel().selectAccountStatement());
    }

    public ArrayList selectSupplierStatement()
        throws Exception
    {
        return loadResults(getVendorSearchDataModel().selectSupplierStatement());
    }

    public ArrayList selectSupplierNumberStatement()
        throws Exception
    {
        return loadResults(getVendorSearchDataModel().selectSupplierNumberStatement());
    }

    private ArrayList loadResults(ResultSet results)
        throws Exception
    {
        try
        {
            vendorSearchTableList = new ArrayList();
            VendorSearch auxVendorSearch;
            for(; results.next(); vendorSearchTableList.add(auxVendorSearch))
            {
                int i = 1;
                auxVendorSearch = new VendorSearch();
                auxVendorSearch.setACCOUNTNUMBER(DB2.getString(results, i++));
                auxVendorSearch.setVENDORNAME(DB2.getString(results, i++));
                auxVendorSearch.setVENDORNUMBER(DB2.getString(results, i++));
                auxVendorSearch.setADDRESS(DB2.getString(results, i++));
                auxVendorSearch.setADDRESS2(DB2.getString(results, i++));
                auxVendorSearch.setADDRESS3(DB2.getString(results, i++));
                auxVendorSearch.setCUSTOMERNAME(DB2.getString(results, i++));
                auxVendorSearch.setVENDORCOMMISSION(DB2.getString(results, i++));
                auxVendorSearch.setSUPPLIERADDRESS(DB2.getString(results, i++));
                auxVendorSearch.setSUPPLIERADDRESS2(DB2.getString(results, i++));
                auxVendorSearch.setSUPPLIERADDRESS3(DB2.getString(results, i++));
                auxVendorSearch.setVATREGISTRATIONNUMBER(DB2.getString(results, i++));
                auxVendorSearch.setSRNUMBER(DB2.getString(results, i++));
                auxVendorSearch.setBLOCKINDC(DB2.getString(results, i++));
                RegionalBigDecimal amount = new RegionalBigDecimal(auxVendorSearch.getVENDORCOMMISSION(), RegionalBigDecimal.PERIODDECIMALSEPARATOR);
                auxVendorSearch.setVENDORCOMMISSION(amount.toString());
                if(auxVendorSearch.getADDRESS().length() == 0 && auxVendorSearch.getADDRESS2().length() == 0 && auxVendorSearch.getADDRESS3().length() == 0)
                {
                    auxVendorSearch.setADDRESS(auxVendorSearch.getSUPPLIERADDRESS());
                    auxVendorSearch.setADDRESS2(auxVendorSearch.getSUPPLIERADDRESS2());
                    auxVendorSearch.setADDRESS3(auxVendorSearch.getSUPPLIERADDRESS3());
                }
            }

            auxVendorSearch = new VendorSearch();
            auxVendorSearch.setACCOUNTNUMBER("XXXXXX");
            auxVendorSearch.setVENDORNAME("XXXXXX");
            auxVendorSearch.setVENDORNUMBER("XXXXXX");
            auxVendorSearch.setADDRESS("XXXXXX");
            auxVendorSearch.setADDRESS2("XXXXXX");
            auxVendorSearch.setADDRESS3("XXXXXX");
            auxVendorSearch.setCUSTOMERNAME("XXXXXX");
            auxVendorSearch.setVENDORCOMMISSION("XXXXXX");
            auxVendorSearch.setSUPPLIERADDRESS("XXXXXX");
            auxVendorSearch.setSUPPLIERADDRESS2("XXXXXX");
            auxVendorSearch.setSUPPLIERADDRESS3("XXXXXX");
            auxVendorSearch.setVATREGISTRATIONNUMBER("XXXXXX");
            auxVendorSearch.setSRNUMBER("XXXXXX");
            auxVendorSearch.setBLOCKINDC("XXXXXX");
            vendorSearchTableList.add(auxVendorSearch);
        }
        catch(Exception exc)
        {
            logger.fatal(exc.toString());
            throw exc;
        }
        return vendorSearchTableList;
    }

   

}
