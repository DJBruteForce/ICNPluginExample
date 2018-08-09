/*
 * Created on Aug 6, 2004
 * 
 * TODO To change the template for this generated file go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
package com.ibm.gil.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;



import com.ibm.gil.business.FormSelect;
//import com.ibm.igf.gui.ComboItemDescription;
import com.ibm.gil.business.InvoiceELS;
import com.ibm.gil.business.InvoiceLineItemsELS;
import com.ibm.gil.business.LineItemELS;
import com.ibm.gil.business.VatCodesNonUSFormSelect;
import com.ibm.gil.util.CNUtilConstants;
import com.ibm.gil.util.RegionalBigDecimal;
import com.ibm.gil.util.RegionalDateConverter;
import com.ibm.igf.hmvc.ResultSetCache;
import com.sun.rowset.CachedRowSetImpl;

/*
 * @author SteveBaber
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class LineItemDataModelELS extends DataModel implements Comparable {
	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(LineItemDataModelELS.class);

    /*
     * supporting table caches
     */
    private static transient ResultSetCache VATCodesResults = null;
    private static transient ResultSetCache OtherChargesResults = null;
    private static transient Hashtable TypeModelResults = new Hashtable();

    /*
     * misc local vars
     */
    private static transient ArrayList VATCodes;
//    private static transient ComboItemDescription defaultVATCode;
    private static transient Hashtable VATPercentages = null;
    private static transient Hashtable otherCharges;
    public static final transient RegionalBigDecimal ONEHUNDRED = new RegionalBigDecimal(100.0);
    private LineItemELS lineItemParent;
    
    public LineItemDataModelELS(LineItemELS lineItemParent)
    {
    	super(lineItemParent);
    	this.lineItemParent=lineItemParent;
       
    }

    
    
 

    /*
     * getters and setters
     */

    public void determineINVOICEITEMTYPE(HashMap<String, String> otherCharges)
    {
    	
        String otherCharge =lineItemParent.getOTHERCHARGE();
        
        if (otherCharges!=null && otherCharges.get(otherCharge) != null)
        {

        	String desc=otherCharges.get(otherCharge);
//        	ComboItemDescription itemDescription = (ComboItemDescription)otherCharges.get(otherCharge);
        	lineItemParent.setDESCRIPTION(desc);
            lineItemParent.setINVOICEITEMTYPE("OC");
        } else
        {
            selectTypeModelDescription(lineItemParent.getCOUNTRY());
            if (lineItemParent.getDESCRIPTION().trim().length() != 0 || 
            	 ((lineItemParent.getTYPE()!=null && !lineItemParent.getTYPE().trim().equals("")) && 
            	 (lineItemParent.getMODEL()!=null && !lineItemParent.getMODEL().trim().equals("")) &&
            	 (lineItemParent.getPARTNUMBER()==null || lineItemParent.getPARTNUMBER().trim().equals("")) )
            	)
            {
            	lineItemParent.setINVOICEITEMTYPE("TM");
            } else
            {
            	lineItemParent.setINVOICEITEMTYPE("P");
            }
        }
    }
   public void loadSelectVatCodes(HashMap<String, com.ibm.gil.util.RegionalBigDecimal> vatPercentages,List<FormSelect> vatCodes,InvoiceLineItemsELS invoice){
	   
	   String country = invoice.getCountry();
	   HashMap<String, String> vatPercentagesStr = new HashMap<String, String> (); 
	   try {
	   
	   if(!country.equals("US")&& !country.equals("CA")){

//           ArrayList<FormSelect> vatCodes = new ArrayList<FormSelect>();
//           Hashtable<String,com.ibm.gil.util.RegionalBigDecimal> vatPercentages = new Hashtable<String, RegionalBigDecimal> (); 
           
           FormSelect code = null;
           //adding blank item
       	//Hashtable VATPercentages = new Hashtable();
           code = new VatCodesNonUSFormSelect();
       	code.setValue(" ");
       	code.setLabel(" ");
       	vatCodes.add(code);
       	
	   ResultSetCache cachedResults;
	
		cachedResults = selectVATCodesStatement();
		while (cachedResults.next())
	   	{
	   		logger.debug("line items vatcode: "+cachedResults.getString(2) +"--"+cachedResults.getString(4));
//	   		VATCode = new ComboItemDescription(cachedResults.getString(2), cachedResults.getString(4));
	   		code=new VatCodesNonUSFormSelect();
	   		code.setValue(cachedResults.getString(2));
	       	code.setLabel(cachedResults.getString(4));
	       	
//	   		vatCode=cachedResults.getString(2) +"#~#"+cachedResults.getString(4);
	   		
	   		if (cachedResults.getString(3).equals("*"))
	   		{            			
	   			code.setSelected(true);
	   			lineItemParent.setDefaultVatCode(code.getValue());
	   		}
	   		
	   		vatCodes.add(code);
	   		vatPercentages.put(cachedResults.getString(2), cachedResults.getRegionalBigDecimal(5));
	   		vatPercentagesStr.put(cachedResults.getString(2), cachedResults.getRegionalBigDecimal(5).toString());
	   		}
	
   		   }  else if (country.equals("CA")){
   			   
   			   		 loadVATCodesForCA(vatPercentages,vatPercentagesStr,vatCodes,invoice);
   		   }
	   
	   } catch (SQLException e) {
		   logger.error("Error initializing Tax codes searchbyoffering flow:" + e.getMessage(),e);
		}catch (ParseException e) {
		   logger.error("Error initializing Tax codes searchbyoffering flow:" + e.getMessage(),e);
		}catch (Exception e) {
			logger.error("Error initializing Tax codes searchbyoffering flow:" + e.getMessage(),e);
		}
   }
   public void setDefaultVatCode(InvoiceELS invoice ){
	   
	   
	   try {
		   String country = invoice.getCOUNTRY();
		   if(!country.equals("US")&& !country.equals("CA")){
	
		         FormSelect code = null;
		         //adding blank item
		       	 //Hashtable VATPercentages = new Hashtable();
		         code = new VatCodesNonUSFormSelect();
		       	 code.setValue(" ");
		       	 code.setLabel(" ");
		     
			     ResultSetCache cachedResults;
				 cachedResults = selectVATCodesStatement();
				 while (cachedResults.next())
			   	 {
			   		logger.debug("line items vatcode: "+cachedResults.getString(2) +"--"+cachedResults.getString(4));
		
			   		code=new VatCodesNonUSFormSelect();
			   		code.setValue(cachedResults.getString(2));
			       	code.setLabel(cachedResults.getString(4));
			       	//vatCode=cachedResults.getString(2) +"#~#"+cachedResults.getString(4);
			   		if (cachedResults.getString(3).equals("*"))
			   		{            			
			   			code.setSelected(true);
			   			lineItemParent.setDefaultVatCode(code.getValue());
			   		}
		
			   	 }
		   
		   } else if(country.equals("CA")){
			   		
			   		setDefaultVatCodeForCA(invoice);
		   }
		   
		} catch (SQLException e) {
			logger.error("Error initializing Tax codes searchbyoffering flow:"+ e.getLocalizedMessage(),e);
		} catch(ParseException e){
			logger.error("Error initializing Tax codes searchbyoffering flow:"+ e.getLocalizedMessage(),e);
		}catch(Exception e){
			logger.error("Error initializing Tax codes searchbyoffering flow:"+ e.getLocalizedMessage(),e);
		}
   }
   
   //Story 1750051 CA GIL changes
   /**
    * Gets the default vat code for line item based on invoice province code and date.
    * @param invoice
    * @throws IllegalArgumentException
    * @throws SQLException
    * @throws ParseException
    */
   public void setDefaultVatCodeForCA(InvoiceELS invoice ) throws IllegalArgumentException, SQLException, ParseException {

      	   
      	String provinceCode = invoice.getPROVINCECODE();
      	String vatAmount  = invoice.getVATAMOUNT();
      	String invoiceDate = invoice.getINVOICEDATE();
      	LineItemELS lineItemEls = new LineItemELS(invoice.getCOUNTRY());
      	VatCodesNonUSFormSelect code = null;
      	
      	FormSelect defaultVATCodeFlag = null;
      	FormSelect defaultVATCodeIndi = null;
      	FormSelect defaultVATCode = null;
      	RegionalBigDecimal vatamount = RegionalBigDecimal.ZERO.setScale(2);
        vatamount = new RegionalBigDecimal(vatAmount).setScale(2);
              
        if (provinceCode.equals("")){
             provinceCode = "ON";
      	}

        String GeneralVATDefault = "";
        FormSelect GeneraldefaultVATCode = null;
                                  
        ResultSet defaultVATResults = lineItemEls.getLineItemDataModelELS().selectCAVATCodesDefaultStatement(provinceCode);
      	while (defaultVATResults.next()) {  
      			if (defaultVATResults.getString(2).equals("*")){
      				GeneralVATDefault = defaultVATResults.getString(1);
      			}
        }

      	String FrmDate = RegionalDateConverter.convertDate("GUI", "DB2", invoiceDate); 
      	ResultSet defaultResults = lineItemEls.getLineItemDataModelELS().selectCAVATCodesStatement(provinceCode, FrmDate);
        while (defaultResults.next()) {
      			
             	  RegionalBigDecimal hundred = new RegionalBigDecimal("100").setScale(2); 
                  RegionalBigDecimal tax = new RegionalBigDecimal(defaultResults.getString(3));
                  tax = tax.multiply(hundred);
                  tax = tax.setScale(3, RegionalBigDecimal.ROUND_HALF_UP);             

                  String[] parts = tax.toString().split("\\.");
                  if (parts[1].equals("000")){                        
                     tax = tax.setScale(0, RegionalBigDecimal.ROUND_HALF_UP);
                  }

                  code = new VatCodesNonUSFormSelect()  ;      
                  code.setValue(defaultResults.getString(1));
                  code.setLabel(tax + "% " + defaultResults.getString(5));
                  
                  if (defaultResults.getString(2).equals("*")){
                  	defaultVATCodeFlag = code;
                  }else{
                  	if (defaultResults.getString(4).equals("*")){                        	
                  		defaultVATCodeIndi = code;
                      }
                  }
 
                  if (GeneralVATDefault.equals(defaultResults.getString(1))){
                  	GeneraldefaultVATCode = code;
                  }
              }
              
         if (vatamount.compareTo(RegionalBigDecimal.ZERO) > 0){
              defaultVATCode = defaultVATCodeFlag; 
         }else{
              defaultVATCode = defaultVATCodeIndi;
         } 

         if (defaultVATCode == null){
              defaultVATCode = GeneraldefaultVATCode;
         } 
         
         
         if(defaultVATCode!=null)
        	 lineItemParent.setDefaultVatCode(defaultVATCode.getValue());
   }
   //End Story 1750051 CA GIL changes 
   
	//Story 1750051 CA GIL changes
   /**
    * Loads vat codes for canada on any acton that needs it in line items dialog.
    * @param vatPercentages
    * @param vatCodes
    * @param invoice
    * @throws IllegalArgumentException
    * @throws SQLException
    * @throws ParseException
    */
   public void loadVATCodesForCA(HashMap<String, com.ibm.gil.util.RegionalBigDecimal> vatPercentages,HashMap<String, String> vatPercentagesStr, List<FormSelect> vatCodes,InvoiceLineItemsELS invoice) throws IllegalArgumentException, SQLException, ParseException {
	    
	    String country = invoice.getCountry();
      	String provinceCode = invoice.getProvinceCode();
      	String vatAmount  = invoice.getVATAMOUNT();
      	String invoiceDate = invoice.getInvoiceDate();
      	LineItemELS lineItemEls = new LineItemELS(country);
      	VatCodesNonUSFormSelect code = null;
    	FormSelect defaultVATCodeFlag = null;
      	FormSelect defaultVATCodeIndi = null;
      	FormSelect defaultVATCode = null;
      	RegionalBigDecimal vatamount = RegionalBigDecimal.ZERO.setScale(2);
        vatamount = new RegionalBigDecimal(vatAmount).setScale(2);
              
         if (provinceCode.equals("")){
              	provinceCode = "ON";
      	 }
              
         String GeneralVATDefault = "";
         FormSelect GeneraldefaultVATCode = null;
                                  
      	 ResultSet defaultVATResults = lineItemEls.getLineItemDataModelELS().selectCAVATCodesDefaultStatement(provinceCode);
      	 while (defaultVATResults.next()) {  
      			if (defaultVATResults.getString(2).equals("*")){
      				GeneralVATDefault = defaultVATResults.getString(1);
      			}
          }

      	String FrmDate = RegionalDateConverter.convertDate("GUI", "DB2", invoiceDate); 
      	ResultSet defaultResults = lineItemEls.getLineItemDataModelELS().selectCAVATCodesStatement(provinceCode, FrmDate);
      	while (defaultResults.next()) {
      			
             	  RegionalBigDecimal hundred = new RegionalBigDecimal("100").setScale(2); 
                  RegionalBigDecimal tax = new RegionalBigDecimal(defaultResults.getString(3));
                  tax = tax.multiply(hundred);
                  tax = tax.setScale(3, RegionalBigDecimal.ROUND_HALF_UP);
                  vatPercentages.put(defaultResults.getString(1), tax);
                  vatPercentagesStr.put(defaultResults.getString(1), tax.toString());

                  String[] parts = tax.toString().split("\\.");
                  if (parts[1].equals("000")){                        
                     tax = tax.setScale(0, RegionalBigDecimal.ROUND_HALF_UP);
                  }

                  code = new VatCodesNonUSFormSelect()  ;      
                  code.setValue(defaultResults.getString(1));
                  code.setLabel(tax + "% " + defaultResults.getString(5));
                  vatCodes.add(code);
                  if (defaultResults.getString(2).equals("*")){
                  	defaultVATCodeFlag = code;
                  }else{
                  	if (defaultResults.getString(4).equals("*")){                        	
                  		defaultVATCodeIndi = code;
                      }
                  }
                  if (GeneralVATDefault.equals(defaultResults.getString(1))){
                  	GeneraldefaultVATCode = code;
                  }
                
              }
              
              if (vatamount.compareTo(RegionalBigDecimal.ZERO) > 0){
              	defaultVATCode = defaultVATCodeFlag; 
              }else{
              	defaultVATCode = defaultVATCodeIndi;
              } 

              if (defaultVATCode == null){
              	defaultVATCode = GeneraldefaultVATCode;
              }

              if(defaultVATCode!=null)
            	  lineItemParent.setDefaultVatCode(defaultVATCode.getValue());
              
              int indexVatCode = vatCodes.indexOf(defaultVATCode);
              if(indexVatCode > -1){
              	vatCodes.get(indexVatCode).setSelected(true);
              }
   }
	//End Story 1750051 CA GIL changes  
      
    /*
     * creates a select statement for retrieving the vat codes
     */
    public ResultSetCache selectVATCodesStatement() throws SQLException
    {

//        if ((VATCodesResults != null) && (VATCodesResults.getIndexKey().equals(lineItemParent.getCOUNTRY())))
//        {
//            VATCodesResults.beforeFirst();
//            return (VATCodesResults);
//        }

        StringBuffer sql = new StringBuffer();
        String region = "APTS";
        String schema = getCountryProperties().getAptsSchema();//getDatabaseDriver().getSchema(region, getCOUNTRY());

        // build the sql statement, use append since it is more efficient
        sql.append("select VMCCODE, VMVAT, VMDFTIND, VMVATDESC, VMVATPCT from ");
        sql.append(schema);
        sql.append("VATMPF where VMCCODE=");
        sql.append(getSqlString(lineItemParent.getCOUNTRY()));
        sql.append(" order by VMCCODE");
        Connection conn = null;
        Statement stmt = null;
        logger.debug("selectVATCodesStatement: "+sql.toString());
        // prepare the statement to execute
        try
        {
        	conn = getDataSourceTransactionManager().getDataSource().getConnection();
        	stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery(sql.toString());
//            		getDatabaseDriver().executeQuery(getDatabaseDriver().getConnection(region, getCOUNTRY()), sql.toString());
            VATCodesResults = new ResultSetCache(results, lineItemParent.getCOUNTRY(), 1, 2, "*");

            // return the results
            return (VATCodesResults);
        } catch (SQLException exc)
        {
            logger.fatal(exc.toString() + "\n" + sql + "\n");
            throw exc;
        }
    }

   
    /*
     * creates a select statement for retrieving the vat codes
     */
    public ResultSetCache selectOtherChargesStatement() throws SQLException {

        String schema =getCountryProperties().getIcfsSchema();
        String enterprise = getFinEnterpriseNum(lineItemParent.getCOUNTRY());
        StringBuffer sql = new StringBuffer();

        // build the sql statement, use append since it is more efficient
        sql.append("SELECT T86.PROD_TYPE, T86.PROD_MOD, T8B.PROD_DESCR ");
        sql.append("FROM ");
        sql.append(schema);
        sql.append("JFCA8B00 T8B, ");
        sql.append(schema);
        sql.append("JFCT86 T86 ");
        sql.append("WHERE T86.PROD_TYPE = T8B.PROD_TYPE ");
        sql.append("AND T86.PROD_MOD = T8B.PROD_MOD ");
        sql.append("AND T86.FIN_ENTERP_NUM = " + enterprise);
        sql.append(" AND T8B.FIN_ENTERP_NUM = " + enterprise);
        sql.append(" AND (T86.BRAND_ID = 'NI' OR T86.BRAND_ID = 'NO') ");
        sql.append("ORDER BY T86.PROD_TYPE, T86.PROD_MOD WITH UR ");
        Connection conn=null;
        Statement stmt = null;
      	CachedRowSetImpl cachedRs = new CachedRowSetImpl();
      	
       	logger.debug("selectOtherChargesStatement: "+sql.toString());
        try {
             conn=getDataSourceTransactionManagerIcfs().getDataSource().getConnection();
             stmt=conn.createStatement();
             ResultSet results = stmt.executeQuery(sql.toString());     	
             OtherChargesResults = new ResultSetCache(results, lineItemParent.getCOUNTRY(), 1, 2, "*");

            return (OtherChargesResults);
            
        } catch (SQLException exc)
        {
        	exc.printStackTrace();//,meanwhile
        	logger.fatal(exc);
            throw exc;
        }finally {
			stmt.close();
			conn.close();
		}
    }
    public HashMap<String, String> loadOtherChargesOLItem( )  {
    	 HashMap<String, String> otherChargesMap=new HashMap<String, String>();
    	 

        try  {
        	

             ResultSetCache cachedResults =lineItemParent.getLineItemDataModelELS().selectOtherChargesStatement();


            while (cachedResults.next())  {
            	
            	String key=cachedResults.getString(1) + "/" + cachedResults.getString(2);
            	
            	otherChargesMap.put(key, cachedResults.getString(3));
            }
            
            
        } catch (Exception exc)
        {
            logger.fatal(exc.toString());
            logger.error("Error initializing Other Charges");
        }
        return otherChargesMap;
    }

    /*
     * recalculates the vat amount and the extended line price
     */
    public void recalculateVATAmount(HashMap<String, RegionalBigDecimal> vatPercentages)
    {
        // get the values
        RegionalBigDecimal price = getDecimal(lineItemParent.getUNITPRICE());
        RegionalBigDecimal quantity = getDecimal(lineItemParent.getQUANTITY());

        RegionalBigDecimal totalVat = RegionalBigDecimal.ZERO;
        RegionalBigDecimal vat = RegionalBigDecimal.ZERO;
        RegionalBigDecimal vatPercent = getVATPercentage(vatPercentages);
        RegionalBigDecimal extendedPrice = price.multiply(quantity);

        if (vatPercent != null)
        {
            vat = price.multiply(vatPercent);
            totalVat = vat.multiply(quantity);

            vat = vat.divide(ONEHUNDRED, 2, RegionalBigDecimal.ROUND_HALF_UP);
            totalVat = totalVat.divide(ONEHUNDRED, 2, RegionalBigDecimal.ROUND_HALF_UP);
        }
        if(lineItemParent.isUSCountry()){
        	if(vat.doubleValue()==0){//could be USTaxPercent is different from zero, but the VAT calculated(price* vatPercent= vat ==> (0.00*.10=0.00) gives Zero then vatCode should be NO_TAX
        		lineItemParent.setVATCODE(CNUtilConstants.NO_TAX);
        	}else {
        		lineItemParent.setVATCODE(CNUtilConstants.WITH_TAX);
        	}
        }
        lineItemParent.setEXTENDEDPRICE(extendedPrice.toString());
        lineItemParent.setVATAMOUNT(vat.toString());
        lineItemParent.setTOTALVATAMOUNT(totalVat.toString());
        lineItemParent.setUNITPRICE(price.toString());

    }
    
    

   
    /*
     * lookup the description based on the type model
     */
    public boolean selectTypeModelDescription(String country) 
    {
        boolean result=false;
		try {
			result = selectTypeModelDescription("OPTIONAL",country);
			 if (result == false)
		            result = selectTypeModelDescription("STANDARD", country);
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
       
        return result;
    }

//    public boolean selectTypeModelsDescription(List<LineItemELS> itemsToValidate, String prodDescPurpose, String country)
//    {
//
//        StringBuffer sql = new StringBuffer();
//        lineItemParent.setDESCRIPTION("");
//
//        /***this will be checked at front end, no need to check until lineItems are on server***/
////        if ((lineItemParent.getTYPE().trim().length() == 0) || (lineItemParent.getMODEL().trim().length() == 0))
////        {
////            return false;
////        }
//
//
//        lineItemParent.setCOUNTRY(country);
//
//        // check the cache for this entry
////        String key =lineItemParent.getTypeModelKey();
//        LineItemELS cacheItem = null;
//        //Create list of types/models to query
//        String types="";
//        String models="";
//        LinkedHashMap<String, LineItemELS> itemsOrdered=new LinkedHashMap<>();
//        
//        for (LineItemELS lineItemELS : itemsToValidate) {
//			types=types+lineItemELS.getTYPE()+",";
//			models=models+lineItemELS.getMODEL()+",";
//			itemsOrdered.put(lineItemELS.getTYPE()+"#"+
//							 lineItemELS.getMODEL()+"#"+
//							 lineItemELS.getLINENUMBER()+"#"+
//							 lineItemELS.getSUBLINENUMBER(), lineItemELS);
//		}
//        if(!types.equals("") && !models.equals("")){
//        	types= types.substring(0, types.length()-1);
//        	models=models.substring(0,models.length()-1);
//        	
//            String schema =getCountryProperties().getIcfsSchema();// getDatabaseDriver().getSchema(region, getCOUNTRY());
//
//            String enterprise = getFinEnterpriseNum(country);
//
//            sql.append("SELECT PROD_DESCR, PROD_CAT, CASE WHEN T86.BRAND_ID = '13'  OR T86.BRAND_ID = '4S' THEN '2'  ");
//            sql.append("WHEN T86.BRAND_ID = 'LX' THEN '3' WHEN T86.BRAND_ID = 'ON' THEN '6' "); 
//            sql.append("WHEN T86.BRAND_ID = 'OS' THEN '7' WHEN T86.BRAND_ID = 'OP' THEN '8' "); 
//            sql.append("WHEN T86.BRAND_ID = 'GP' THEN '9' WHEN T86.BRAND_ID = 'GN' THEN '10' "); 
//            sql.append("WHEN T86.BRAND_ID = '44' THEN '11' ELSE '0' END AS PROD_CLASS, ");
//            sql.append("MIN(CAP_AMOUNT_MIN), MAX(CAP_AMOUNT_MAX) FROM ");
//            sql.append(schema);
//            sql.append("JFCT86 T86, (select * from ");
//            sql.append(schema);
//            sql.append("jfca8b00 A ");
//            sql.append(" left outer join ");
//            sql.append(schema);
//            sql.append("jfdtat B ");
//            sql.append("ON A.PROD_TYPE = B.SPECIFIC_MACH_TYPE AND ");
//            sql.append("A.PROD_MOD = B.SPECIFIC_MACH_MOD ");
//            sql.append("AND b.effective_dte <= CURRENT DATE AND b.end_dte >= CURRENT DATE ");
//            sql.append("AND country_code = ");
//            sql.append(getDatabaseDriver().sqlString(getICFSCountryCode(lineItemParent.getCOUNTRY())));
//            sql.append(") as K WHERE T86.PROD_TYPE = K.PROD_TYPE ");
//            sql.append("AND T86.PROD_MOD = K.PROD_MOD AND K.FIN_ENTERP_NUM = " + enterprise + " AND K.FIN_ENTERP_NUM = T86.FIN_ENTERP_NUM AND K.PROD_TYPE ");
//            sql.append("IN ("+types+")" );//getDatabaseDriver().sqlString(lineItemParent.getTYPE()));
//            sql.append(" AND K.PROD_MOD  ");
//            sql.append("IN ("+models+")");//getDatabaseDriver().sqlString(lineItemParent.getMODEL()));
//            sql.append(" AND K.PROD_DESCR_PURP = ");
//            sql.append(getDatabaseDriver().sqlString(prodDescPurpose));
//
//            sql.append(" GROUP BY PROD_DESCR, PROD_CAT, BRAND_ID WITH UR ");
//            Connection conn=null;
//            Statement stmt = null;
////        	CachedRowSetImpl cachedRs = new CachedRowSetImpl();
//        	logger.debug("selectTypeModelsDescription: "+sql.toString());
//            // retreive a database connection
//            try
//            {
//            	
//            	conn=getDataSourceTransactionManagerIcfs().getDataSource().getConnection();
//            	stmt=conn.createStatement();
//                ResultSet results = stmt.executeQuery(sql.toString());
////                		getDatabaseDriver().executeQuery(getDatabaseDriver().getConnection(region, getCOUNTRY()), sql.toString());
//                if (results == null)
//                {
//                    lineItemParent.getDialogErrors().add("Error retrieving records");
//                    return false;
//                }
//
//                // retrieve the record
//
//                // setup the new cache item
//                cacheItem = new LineItemELS(lineItemParent.getCOUNTRY());
////                cacheItem.setCOUNTRY(lineItemParent.getCOUNTRY());
//                cacheItem.setTYPE(lineItemParent.getTYPE());
//                cacheItem.setMODEL(lineItemParent.getMODEL());
//
//                if (results.next())
//                {
//                	
//                	
//                	cacheItem.setDESCRIPTION( getDatabaseDriver().getString(results, 1));
//                	cacheItem.setPRODCAT( getDatabaseDriver().getString(results, 2));
//                	cacheItem.setPRODTYPE( getDatabaseDriver().getString(results, 3));
//                	cacheItem.setMINUNITPRICE( getDatabaseDriver().getString(results, 4));
//                	cacheItem.setMAXUNITPRICE( getDatabaseDriver().getString(results, 5));
//                	
//                   // fixup the decimals
//                    RegionalBigDecimal amount = new RegionalBigDecimal (cacheItem.getMINUNITPRICE(), RegionalBigDecimal.PERIODDECIMALSEPARATOR);
//                    cacheItem.setMINUNITPRICE(amount.toString());
//                    amount = new RegionalBigDecimal (cacheItem.getMAXUNITPRICE(), RegionalBigDecimal.PERIODDECIMALSEPARATOR);
//                    cacheItem.setMAXUNITPRICE(amount.toString());
//
//
//
//                    if(lineItemParent.getDESCRIPTION().trim().length() == 0)
//                		lineItemParent.setDESCRIPTION(cacheItem.getDESCRIPTION());        	
//                	if(lineItemParent.getPRODCAT().trim().length()==0)        	
//                		lineItemParent.setPRODCAT(cacheItem.getPRODCAT());
//                	if(lineItemParent.getPRODTYPE().trim().length()==0)
//                		lineItemParent.setPRODTYPE(cacheItem.getPRODTYPE());
//                	if(lineItemParent.getMINUNITPRICE().trim().length()==0)
//                		lineItemParent.setMINUNITPRICE(cacheItem.getMINUNITPRICE());
//                	if(lineItemParent.getMAXUNITPRICE().trim().length()==0)
//                		lineItemParent.setMAXUNITPRICE(cacheItem.getMAXUNITPRICE());
//
//                } else{
//                    return false;
//                }
//
//                // add it to the cache need to go 
////                TypeModelResults.put(key, cacheItem);
//                return true;
//
//            } catch (SQLException exc){
//            	
//               logger.fatal("selectTypeModelDescription: "+exc.toString() + "\n" + sql + "\n");
//            }
//
//            return false;
//
//        }else{
//        	logger.debug("Types and  Models are blank");
//        	return false;
//        }
//     }
//
    public boolean selectTypeModelDescription(String prodDescPurpose, String country) throws SQLException
    {

        StringBuffer sql = new StringBuffer();
        lineItemParent.setDESCRIPTION("");


        if ((lineItemParent.getTYPE().trim().length() == 0) || (lineItemParent.getMODEL().trim().length() == 0))
        {
            return false;
        }

        // copy the country code in from the invoice header
//        InvoiceDataModel anInvoiceDataModel = (InvoiceDataModel) getEventController().getParentController().getDataModel();
        lineItemParent.setCOUNTRY(country);

        // check the cache for this entry
//        String key =lineItemParent.getTypeModelKey();
        LineItemELS cacheItem = null;
        		//(LineItemELS) TypeModelResults.get(key);
//        if (cacheItem != null)
//        {
////            // copy the values over
////            for (int i = 0; i < modelList.length; i++)
////            {
////                if (getString(modelList[i]).trim().length() == 0)
////                {
////                    set(modelList[i], cacheItem.get(modelList[i]));
////                }
////            }
////            //        int[] modelList = { lineItemParent.getDESCRIPTION(), lineItemParent.getPRODCAT(), lineItemParent.getPRODTYPE(),lineItemParent.getMINUNITPRICE(),lineItemParent.getMAXUNITPRICE() };
//        	
//        	if(lineItemParent.getDESCRIPTION().trim().length() == 0)
//        		lineItemParent.setDESCRIPTION(cacheItem.getDESCRIPTION());        	
//        	if(lineItemParent.getPRODCAT().trim().length()==0)        	
//        		lineItemParent.setPRODCAT(cacheItem.getPRODCAT());
//        	if(lineItemParent.getPRODTYPE().trim().length()==0)
//        		lineItemParent.stPRODTYPE(cacheItem.getPRODTYPE());
//        	if(lineItemParent.getMINUNITPRICE().trim().length()==0)
//        		lineItemParent.setMINUNITPRICE(cacheItem.getMINUNITPRICE());
//        	if(lineItemParent.getMAXUNITPRICE().trim().length()==0)
//        		lineItemParent.setMAXUNITPRICE(cacheItem.getMAXUNITPRICE());
//        	
//        	
//            return true;
//        }

       
        String schema =getCountryProperties().getIcfsSchema();// getDatabaseDriver().getSchema(region, getCOUNTRY());

        String enterprise = getFinEnterpriseNum(lineItemParent.getCOUNTRY());

        sql.append("SELECT PROD_DESCR, PROD_CAT, CASE WHEN T86.BRAND_ID = '13'  OR T86.BRAND_ID = '4S' THEN '2'  ");
        sql.append("WHEN T86.BRAND_ID = 'LX' THEN '3' WHEN T86.BRAND_ID = 'ON' THEN '6' "); 
        sql.append("WHEN T86.BRAND_ID = 'OS' THEN '7' WHEN T86.BRAND_ID = 'OP' THEN '8' "); 
        sql.append("WHEN T86.BRAND_ID = 'GP' THEN '9' WHEN T86.BRAND_ID = 'GN' THEN '10' "); 
        sql.append("WHEN T86.BRAND_ID = '44' THEN '11' ELSE '0' END AS PROD_CLASS, ");
        sql.append("MIN(CAP_AMOUNT_MIN), MAX(CAP_AMOUNT_MAX) FROM ");
        sql.append(schema);
        sql.append("JFCT86 T86, (select * from ");
        sql.append(schema);
        sql.append("jfca8b00 A ");
        sql.append(" left outer join ");
        sql.append(schema);
        sql.append("jfdtat B ");
        sql.append("ON A.PROD_TYPE = B.SPECIFIC_MACH_TYPE AND ");
        sql.append("A.PROD_MOD = B.SPECIFIC_MACH_MOD ");
        sql.append("AND b.effective_dte <= CURRENT DATE AND b.end_dte >= CURRENT DATE ");
        sql.append("AND country_code = ");
        sql.append(getDatabaseDriver().sqlString(getICFSCountryCode(lineItemParent.getCOUNTRY())));
        sql.append(") as K WHERE T86.PROD_TYPE = K.PROD_TYPE ");
        sql.append("AND T86.PROD_MOD = K.PROD_MOD AND K.FIN_ENTERP_NUM = " + enterprise + " AND K.FIN_ENTERP_NUM = T86.FIN_ENTERP_NUM AND K.PROD_TYPE = ");
        sql.append(getDatabaseDriver().sqlString(lineItemParent.getTYPE()));
        sql.append(" AND K.PROD_MOD =  ");
        sql.append(getDatabaseDriver().sqlString(lineItemParent.getMODEL()));
        sql.append(" AND K.PROD_DESCR_PURP = ");
        sql.append(getDatabaseDriver().sqlString(prodDescPurpose));

        sql.append(" GROUP BY PROD_DESCR, PROD_CAT, BRAND_ID WITH UR ");
        Connection conn=null;
        Statement stmt = null;
//    	CachedRowSetImpl cachedRs = new CachedRowSetImpl();
    	logger.debug("selectTypeModelDescription: "+sql.toString());
        // retreive a database connection
        try
        {
        	
        	conn=getDataSourceTransactionManagerIcfs().getDataSource().getConnection();
        	stmt=conn.createStatement();
            ResultSet results = stmt.executeQuery(sql.toString());
//            		getDatabaseDriver().executeQuery(getDatabaseDriver().getConnection(region, getCOUNTRY()), sql.toString());
            if (results == null)
            {
                lineItemParent.getDialogErrors().add("Error retrieving records");
                return false;
            }

            // retrieve the record

            // setup the new cache item
            cacheItem = new LineItemELS(lineItemParent.getCOUNTRY());
//            cacheItem.setCOUNTRY(lineItemParent.getCOUNTRY());
            cacheItem.setTYPE(lineItemParent.getTYPE());
            cacheItem.setMODEL(lineItemParent.getMODEL());

            if (results.next())
            {
            	
            	
            	cacheItem.setDESCRIPTION( getDatabaseDriver().getString(results, 1));
            	cacheItem.setPRODCAT( getDatabaseDriver().getString(results, 2));
            	cacheItem.setPRODTYPE( getDatabaseDriver().getString(results, 3));
            	cacheItem.setMINUNITPRICE( getDatabaseDriver().getString(results, 4));
            	cacheItem.setMAXUNITPRICE( getDatabaseDriver().getString(results, 5));
            	
//            	
//                // retrieve the fields
//                for (int index = 1; index <= modelList.length; index++)
//                {
//                    cacheItem.set(modelList[index - 1], getDatabaseDriver().getString(results, index));
//                }

                // fixup the decimals
                RegionalBigDecimal amount = new RegionalBigDecimal (cacheItem.getMINUNITPRICE(), RegionalBigDecimal.PERIODDECIMALSEPARATOR);
                cacheItem.setMINUNITPRICE(amount.toString());
                amount = new RegionalBigDecimal (cacheItem.getMAXUNITPRICE(), RegionalBigDecimal.PERIODDECIMALSEPARATOR);
                cacheItem.setMAXUNITPRICE(amount.toString());


                // copy the values over
//                for (int i = 0; i < modelList.length; i++)
//                {
//                    if (getString(modelList[i]).trim().length() == 0)
//                    {
//                        set(modelList[i], cacheItem.get(modelList[i]));
//                    }
//                }
                if(lineItemParent.getDESCRIPTION().trim().length() == 0)
            		lineItemParent.setDESCRIPTION(cacheItem.getDESCRIPTION());        	
            	if(lineItemParent.getPRODCAT().trim().length()==0)        	
            		lineItemParent.setPRODCAT(cacheItem.getPRODCAT());
            	if(lineItemParent.getPRODTYPE().trim().length()==0)
            		lineItemParent.setPRODTYPE(cacheItem.getPRODTYPE());
            	if(lineItemParent.getMINUNITPRICE().trim().length()==0)
            		lineItemParent.setMINUNITPRICE(cacheItem.getMINUNITPRICE());
            	if(lineItemParent.getMAXUNITPRICE().trim().length()==0)
            		lineItemParent.setMAXUNITPRICE(cacheItem.getMAXUNITPRICE());

            } else{
                return false;
            }

            // add it to the cache need to go 
//            TypeModelResults.put(key, cacheItem);
            return true;

        } catch (SQLException exc){
        	
           logger.fatal("selectTypeModelDescription: "+exc.toString() + "\n" + sql + "\n");
        }catch (Exception exc){
        	
            logger.fatal("selectTypeModelDescription: "+exc.toString() + "\n" + sql + "\n");
         }
        
        finally {
        	stmt.close();
        	conn.close();
        }

        return false;
    }


    
    /*
     * lookup the other charge and change to a type model
     */
    public void selectOtherChargeDescription()
    {
        String otherCharge = lineItemParent.getOTHERCHARGE();
        if (otherCharge != null)
        {
//        	lineItemParent.setDESCRIPTION(((ComboItemDescription) otherCharges.get(otherCharge)).getDescription());
        	lineItemParent.setTYPE(otherCharge.substring(0, otherCharge.indexOf("/")));
        	lineItemParent.setMODEL(otherCharge.substring(otherCharge.indexOf("/") + 1));
        }
    }

    public boolean isMatchingLineNumberItem(LineItemELS lineItem)
    {
        if (lineItemParent.isPart())
        {
            if (!lineItem.getPARTNUMBER().equals(lineItemParent.getPARTNUMBER()))
            {
                return false;
            }
            if (!lineItem.getVATCODE().equals(lineItemParent.getVATCODE()))
            {
                return false;
            }
            if (!getDecimal(lineItem.getUNITPRICE()).equals(getDecimal(lineItemParent.getUNITPRICE())))
            {
                return false;
            }
            if (!lineItem.getBILLEDTOIGFINDC().equals(lineItemParent.getBILLEDTOIGFINDC()))
            {
                return false;
            }            
        } else
        {
            if (!lineItem.getTYPE().equals(lineItemParent.getTYPE()))
            {
                return false;
            }
            if (!lineItem.getMODEL().equals(lineItemParent.getMODEL()))
            {
                return false;
            }
            if (!lineItem.getVATCODE().equals(lineItemParent.getVATCODE()))
            {
                return false;
            }
            if (!getDecimal(lineItem.getUNITPRICE()).equals(getDecimal(lineItemParent.getUNITPRICE())))
            {
                return false;
            }
            if (!lineItem.getBILLEDTOIGFINDC().equals(lineItemParent.getBILLEDTOIGFINDC()))
            {
                return false;
            }            
        }
        return true;
    }

    /*
     * Retrieve the IBMCountry code
     * 
     * @param isoCountryCode String @return ibmCountryCode String @throws
     * GcpsException
     */
    public String getICFSCountryCode(String isoCountryCode) throws IllegalArgumentException
    {
        String countryCode = null;

//        String regionKey = RegionManager.getBackendRegionPropertyKey(db2Region);
        countryCode =getCountryProperties().getIbmCountryCode();
//        		DefaultPropertyManager.getPropertiesManager().getProperty(regionKey + "_" + isoCountryCode + "_IBM_COUNTRY_CODE");
//not using region cascade, now there is an specific environment
//        int nextDB2Region = RegionManager.getNextCascadingBackendRegion(db2Region);
//        if ((countryCode == null) && (nextDB2Region != db2Region))
//        {
//            countryCode = getICFSCountryCode(isoCountryCode, nextDB2Region);
//        }

        if (countryCode == null)
        {
            throw new IllegalArgumentException("Country Code " + isoCountryCode + " is not configured for product reference lookups");
        }

        // funky override for IT
        if (countryCode.equals("758"))
        {
            countryCode = "EUR";
        }

       logger.debug(isoCountryCode + " IBM COUNTRY CODE = " + countryCode);
        return countryCode;
    }

    public String getFinEnterpriseNum(String isoCountryCode) throws IllegalArgumentException
    {
        String enterpriseNum = null;

//        String regionKey = RegionManager.getBackendRegionPropertyKey(db2Region);
        enterpriseNum =  getCountryProperties().getIbmEnterpriseNum();
//        		DefaultPropertyManager.getPropertiesManager().getProperty(regionKey + "_" + isoCountryCode + "_IBM_ENTERPRISE_NUMBER");

//        int nextDB2Region = com.ibm.igf.hmvc.RegionManager.getNextCascadingBackendRegion(db2Region);
//        if ((enterpriseNum == null) && (nextDB2Region != db2Region))
//        {
//            enterpriseNum = getFinEnterpriseNum(isoCountryCode, nextDB2Region);
//        }

        if (enterpriseNum == null)
        {
            throw new IllegalArgumentException("Country Code " + isoCountryCode + " is not configured for product reference lookups");
        }

        logger.debug(isoCountryCode + " IBM ENTERPRISE NUMBER = " + enterpriseNum);
        return enterpriseNum;

    }

    /*
     * returns the vat percentage that corresponds to the vat code @author
     * SteveBaber
     *  
     */
    public RegionalBigDecimal getVATPercentage(HashMap<String, RegionalBigDecimal> vatPercentages)
    {
    	if(lineItemParent.isUSCountry()){
    		if(lineItemParent.getTAXESINDICATOR().equals("Y") && lineItemParent.getBILLEDTOIGFINDC().equals("N")){
    			lineItemParent.setVATCODE(CNUtilConstants.NO_TAX);
    			return  RegionalBigDecimal.ZERO;
    		}else{
    			if(lineItemParent.getUSTAXPERCENT()==null ||lineItemParent.getUSTAXPERCENT().trim().equals("") ||Double.parseDouble(lineItemParent.getUSTAXPERCENT().trim())==0){
    				lineItemParent.setVATCODE(CNUtilConstants.NO_TAX);
    			}else{
    				lineItemParent.setVATCODE(CNUtilConstants.WITH_TAX);
    			}
    			
    			return getDecimal(lineItemParent.getUSTAXPERCENT());
    		}
    		
    	}
    	
        if (vatPercentages == null)
            return RegionalBigDecimal.ZERO;

        RegionalBigDecimal result = (RegionalBigDecimal) vatPercentages.get(lineItemParent.getVATCODE());

        if (result == null)
            return (RegionalBigDecimal.ZERO);
        else
            return (result);
    }
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object) sort by line number ,
     *      sub line number
     */
    public int compareTo(Object arg0)
    {
    	//TODO check if it works....
        if (arg0 instanceof LineItemDataModelELS)
        {
            LineItemELS aDataModel = (LineItemELS) arg0;
            int result = 0;

            if (result == 0)
                result = getInt(lineItemParent.getLINENUMBER()) - getInt(aDataModel.getLINENUMBER());

            if (result == 0)
                result = getInt(lineItemParent.getSUBLINENUMBER()) - getInt(aDataModel.getSUBLINENUMBER());

            return result;
        } else
        {
            return 0;
        }
    }

    /**
     * @return
     */
    public boolean isBLEX()
    {

        String transType =lineItemParent.getTRANSACTIONTYPE();
        String extensionIndc = lineItemParent.getEXTENSIONINDC();

        if ((extensionIndc.equals("1")) || (extensionIndc.equals("2")) || (extensionIndc.equals("3")) || (transType.equals("EXTN")))
        {
            return true;
        }
        return false;
    }
    
    //Story 1750051 CA GIL changes
    // creates a select statement for retrieving the CA vat codes
    public ResultSet selectCAVATCodesStatement(String PROVINCECODE, String INVOICEDATE) throws SQLException, IllegalArgumentException, ParseException
    {   
    	 
        StringBuffer sql = new StringBuffer();
        String region = "APTS";
        String schema = getDatabaseDriver().getSchema(region, lineItemParent.getCOUNTRY());
        Connection conn = null;
        Statement stmt = null;

        // build the sql statement, use append since it is more efficient
        sql.append("select A.PT1TAXCD, A.PT1DFTFLG, A.PT1PCT, A.PT1DFTZIND, B.PT2Desc from ");
        sql.append(schema);
        sql.append("PROVTAX1 A, ");
        sql.append(schema);
        sql.append("PROVTAX2 B where A.PT1PROV=");
        sql.append(getSqlString(PROVINCECODE));
        sql.append(" and A.PT1EFFFR <= ");
        sql.append(getSqlString(INVOICEDATE));
        sql.append(" and A.PT1EFFTO >= ");
        sql.append(getSqlString(INVOICEDATE));
        sql.append(" and A.PT1TAXCD = B.PT2TAXCD ");
        sql.append(" order by PT1TAXCD desc");

        // prepare the statement to execute
        try
        {
            conn=getDataSourceTransactionManager().getDataSource().getConnection();
            stmt=conn.createStatement();
            ResultSet results = stmt.executeQuery(sql.toString());    
            
            return (results);
        } catch (SQLException exc)
        {
            logger.fatal(exc.toString() + "\n" + sql + "\n");
            throw exc;
        }
    }
    
    //Test Defect 1809379 Error in Gil due to effective dates
    public ResultSet selectCAVATCodesDefaultStatement(String PROVINCECODE) throws SQLException, IllegalArgumentException, ParseException
    {   
    	 
        StringBuffer sql = new StringBuffer();
        String region = "APTS";
        String schema = getDatabaseDriver().getSchema(region, lineItemParent.getCOUNTRY());
        Connection conn = null;
        Statement stmt = null;

        // build the sql statement, use append since it is more efficient
        sql.append("select PT1TAXCD, PT1DFTFLG, PT1DFTZIND from ");
        sql.append(schema);
        sql.append("PROVTAX1 where PT1PROV=");
        sql.append(getSqlString(PROVINCECODE));
        sql.append(" order by PT1TAXCD desc");

        // prepare the statement to execute
        try
        {
            conn=getDataSourceTransactionManager().getDataSource().getConnection();
            stmt=conn.createStatement();
            ResultSet results = stmt.executeQuery(sql.toString());    
            
            return (results);
        } catch (SQLException exc)
        {
            logger.fatal(exc.toString() + "\n" + sql + "\n");
            throw exc;
        }
    }
    //End Test Defect 1809379
  //End Story 1750051 CA GIL changes 
}