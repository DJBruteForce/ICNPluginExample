package com.ibm.gil.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;


import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.ibm.gil.business.OEMProduct;
import com.ibm.gil.util.UtilGilConstants;
import com.ibm.igf.gil.CountryProperties;
import com.ibm.igf.gil.RegionManager;
import com.ibm.igf.gil.SpringApplicationContext;
import com.ibm.igf.hmvc.CountryManager;
import com.ibm.igf.hmvc.PluginConfigurationPane;
import com.sun.rowset.CachedRowSetImpl;



public class OEMProductDataModel {
	
	
	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(OEMProductDataModel.class);
	
	//Attributes for transaction management
	private CountryProperties countryProperties = null;
	private ApplicationContext appContext;
	private DataSourceTransactionManager dataSourceTransactionManagerIcfs = null;

	
    OEMProduct oemProduct= null;
	
    OEMProductDataModel oEMProductDataModel=null;
    
	public OEMProduct getOEMProduct() {
		return oemProduct;
	}

	public void setOEMProduct(OEMProduct oemProduct) {
		this.oemProduct = oemProduct;
	}
	
	
   public OEMProductDataModel(OEMProduct oemProduct)  {
		
		countryProperties = CountryManager.getCountryProperties(oemProduct.getCOUNTRY());
		appContext = SpringApplicationContext.getInstance();
		dataSourceTransactionManagerIcfs=(DataSourceTransactionManager)appContext.getBean(countryProperties.getIcfsSpringTransactionManagerId());
		this.oemProduct = oemProduct;
	} 

	    
	public ResultSet getManufacturers() throws SQLException, IllegalArgumentException, ParseException{
		
			String tableJFCV8600 = null;
			String tableJFCV8100 = null;
		    StringBuffer sql = new StringBuffer();
			String region = "DSND";
		   //String schema = getDatabaseDriver().getSchema(region, getCOUNTRY());
		    
		    CountryProperties countryProperties = CountryManager
					.getCountryProperties(getOEMProduct().getCOUNTRY());
			String schema = countryProperties.getIcfsSchema();
			
			Connection conn = null;
			Statement stmt=null;
		    
			 ResultSet results=null;
			 CachedRowSetImpl cachedRs = new CachedRowSetImpl();
		    
		    //String enterprise = getFinEnterpriseNum(getOEMProduct().getCOUNTRY(), RegionManager.getBackendRegion());
			String regionKey = PluginConfigurationPane.getPropertiesMap().get(UtilGilConstants.REGION);
			//String enterprise = getFinEnterpriseNum(getOEMProduct().getCOUNTRY(), regionKey);
			
			String enterpriseNum = null;		    
		   
		    enterpriseNum =  countryProperties.getIbmEnterpriseNum();
		    
		    
		    if(regionKey.equalsIgnoreCase("DEV") || getOEMProduct().getCOUNTRY().equals("US")||getOEMProduct().getCOUNTRY().equals("CA")){
		    	tableJFCV8600 = "JFCV8600";
		    	tableJFCV8100 = "JFCV8100";
		    }else{
		    	tableJFCV8600 = "JFCV8600_"+enterpriseNum;
		    	tableJFCV8100 = "JFCV8100_"+enterpriseNum;

		    }		
	   
		    sql.append("SELECT DISTINCT AL3.MANUFACTURER_NAME FROM ");
		    sql.append(schema);
		    sql.append(tableJFCV8600).append(" AL1, ");		    
		    sql.append(schema);
		    sql.append(tableJFCV8100).append(" AL3 ");		    
		    sql.append("WHERE ");
		    sql.append("AL1.MANUFACTURER_ID = AL3.MANUFACTURER_ID AND ");
		    sql.append("AL1.FIN_ENTERP_NUM = AL3.FIN_ENTERP_NUM AND ");
		    sql.append("AL1.PROD_TYPE NOT BETWEEN '0000' AND '9ZZZ' AND ");
		    sql.append("AL1.FIN_ENTERP_NUM = " + enterpriseNum);
		    sql.append(" ORDER by AL3.MANUFACTURER_NAME");
		    	    	    
		    logger.debug("Manufacturer Query:"+sql);
	        try
	        {
	        	
	        	//conn = getDataSourceTransactionManager().getDataSource().getConnection();
	        	conn = dataSourceTransactionManagerIcfs.getDataSource().getConnection();
	        	stmt= conn.createStatement();
	            //ResultSet results = getDatabaseDriver().executeQuery(getDatabaseDriver().getConnection(region, getOEMProduct().getCOUNTRY()), sql.toString());
	           results=	stmt.executeQuery(sql.toString());        
	           cachedRs.populate(results);
	           
	         
	           
	            
	        } catch (SQLException exc) {
	        	logger.fatal("Database Exception:"+exc, exc);
	        } finally {
	        	
	        	stmt.close();
	        	conn.close();
	        }
	        
	        // return the results	        
	        return cachedRs;
		}
		
		public ArrayList<OEMProduct> getTypeMods()throws SQLException, IllegalArgumentException, ParseException {
			
			ArrayList<OEMProduct> oemTableList = new ArrayList<OEMProduct>();
			String tableJFCV8600 = null;
			String tableJFCV8100 = null;
			String tableJFCV8B00 = null;
		    StringBuffer sql = new StringBuffer();
			String region = "DSND";
		    //String schema = getDatabaseDriver().getSchema(region, getOEMProduct().getCOUNTRY());
			//String enterprise = getFinEnterpriseNum(getOEMProduct().getCOUNTRY(), RegionManager.getBackendRegion());
			String regionKey = PluginConfigurationPane.getPropertiesMap().get(UtilGilConstants.REGION);
		    //String enterprise = getFinEnterpriseNum(getOEMProduct().getCOUNTRY(), regionKey);
		    
		    String enterpriseNum =null;
		    
		    enterpriseNum =  countryProperties.getIbmEnterpriseNum();
		    
		    
		    CountryProperties countryProperties = CountryManager
					.getCountryProperties(getOEMProduct().getCOUNTRY());
			String schema = countryProperties.getIcfsSchema();
			
		
			
			Connection conn = null;
			Statement stmt=null;
			 CachedRowSetImpl cachedRs = new CachedRowSetImpl();
		   // if(isDevRegion() || getOEMProduct().getCOUNTRY().equals("US")||getOEMProduct().getCOUNTRY().equals("CA")){
		    if(regionKey.equalsIgnoreCase("DEV") || getOEMProduct().getCOUNTRY().equals("US")||getOEMProduct().getCOUNTRY().equals("CA")){	
		    	tableJFCV8600 = "JFCV8600";
		    	tableJFCV8100 = "JFCV8100";
		    	tableJFCV8B00 = "JFCV8B00";
		    }else{
		    	tableJFCV8600 = "JFCV8600_"+enterpriseNum;
		    	tableJFCV8100 = "JFCV8100_"+enterpriseNum;
		    	tableJFCV8B00 = "JFCV8B00_"+enterpriseNum;	    	
		    }		
		    
		    
		    
			String ghzmhz = getOEMProduct().getGHZMHZ().trim().toUpperCase();
			String inch = getOEMProduct().getINCH().trim().toUpperCase();
			String gbmb = getOEMProduct().getGBMB().trim().toUpperCase();
			String proc = getOEMProduct().getProcessor().trim().toUpperCase();
			String equip = getOEMProduct().getEquipmentType().substring(0, 1).trim().toUpperCase();
			String mname = getOEMProduct().getManufacturerName().trim().toUpperCase();
			String iBMorLenovoEquip = getOEMProduct().getIBMorLenovoEquip().trim().toUpperCase();
		    
			logger.debug("Ibm Equipment:" + iBMorLenovoEquip);
		    if(getOEMProduct().getIBMorLenovoEquip().equalsIgnoreCase("false")){
		    	sql.append("SELECT DISTINCT AL4.PROD_TYPE, AL4.PROD_MOD, AL4.PROD_DESCR, AL3.MANUFACTURER_NAME FROM ");
			    sql.append(schema);
			    sql.append(tableJFCV8600).append(" AL1, ");
			    sql.append(schema);
			    sql.append(tableJFCV8100).append(" AL3, ");
			    sql.append(schema);
			    sql.append(tableJFCV8B00).append(" AL4 ");
			    sql.append("WHERE (AL4.PROD_TYPE=AL1.PROD_TYPE AND AL4.PROD_MOD=AL1.PROD_MOD AND ");
			    sql.append("AL4.FIN_ENTERP_NUM=AL1.FIN_ENTERP_NUM AND AL1.MANUFACTURER_ID=AL3.MANUFACTURER_ID AND ");
			    sql.append("AL1.FIN_ENTERP_NUM=AL3.FIN_ENTERP_NUM) AND AL4.PROD_TYPE NOT LIKE '0%' AND AL4.PROD_TYPE NOT LIKE '1%' AND ");
			    sql.append("AL4.PROD_TYPE NOT LIKE '2%' AND AL4.PROD_TYPE NOT LIKE '3%' AND AL4.PROD_TYPE NOT LIKE '4%' AND ");
			    sql.append("AL4.PROD_TYPE NOT LIKE '5%' AND AL4.PROD_TYPE NOT LIKE '6%' AND AL4.PROD_TYPE NOT LIKE '7%' AND ");
			    sql.append("AL4.PROD_TYPE NOT LIKE '8%' AND AL4.PROD_TYPE NOT LIKE '9%'");
			    sql.append("AND AL4.PROD_DESCR LIKE '%");
			    sql.append(ghzmhz);
			    sql.append("%' AND AL4.PROD_DESCR LIKE '%");
			    sql.append(inch);
			    sql.append("%' AND AL4.PROD_DESCR LIKE '%");
			    sql.append(gbmb);
			    sql.append("%' AND AL4.PROD_DESCR LIKE '%");
			    sql.append(proc);
			    sql.append("%' AND AL4.PROD_TYPE LIKE '%");
			    sql.append(equip);
			    sql.append("' AND AL3.MANUFACTURER_NAME LIKE '");
			    sql.append(mname);
			    sql.append("%'");

		    }else{
		    	sql.append("SELECT DISTINCT AL4.PROD_TYPE, AL4.PROD_MOD, AL4.PROD_DESCR, AL3.MANUFACTURER_NAME FROM ");
		    	sql.append(schema);
			    sql.append(tableJFCV8600).append(" AL1, ");
			    sql.append(schema);
			    sql.append(tableJFCV8100).append(" AL3, ");
			    sql.append(schema);
			    sql.append(tableJFCV8B00).append(" AL4 ");
			    sql.append("WHERE (AL4.PROD_TYPE=AL1.PROD_TYPE AND AL4.PROD_MOD=AL1.PROD_MOD AND ");
			    sql.append("AL4.FIN_ENTERP_NUM=AL1.FIN_ENTERP_NUM AND AL1.MANUFACTURER_ID=AL3.MANUFACTURER_ID AND ");
			    sql.append("AL1.FIN_ENTERP_NUM=AL3.FIN_ENTERP_NUM)");
			    sql.append("AND AL4.PROD_DESCR LIKE '%");
			    sql.append(ghzmhz);
			    sql.append("%' AND AL4.PROD_DESCR LIKE '%");
			    sql.append(inch);
			    sql.append("%' AND AL4.PROD_DESCR LIKE '%");
			    sql.append(gbmb);
			    sql.append("%' AND AL4.PROD_DESCR LIKE '%");
			    sql.append(proc);
			    sql.append("%' AND AL4.PROD_TYPE LIKE '%");
			    sql.append(equip);
			    sql.append("' UNION SELECT DISTINCT AL4.PROD_TYPE, AL4.PROD_MOD, AL4.PROD_DESCR,");
			    sql.append("AL3.MANUFACTURER_NAME FROM ");
		    	sql.append(schema);
			    sql.append(tableJFCV8600).append(" AL1, ");
			    sql.append(schema);
			    sql.append(tableJFCV8100).append(" AL3, ");
			    sql.append(schema);
			    sql.append(tableJFCV8B00).append(" AL4 ");
			    sql.append("WHERE (AL4.PROD_TYPE=AL1.PROD_TYPE AND AL4.PROD_MOD=AL1.PROD_MOD AND ");
			    sql.append("AL4.FIN_ENTERP_NUM=AL1.FIN_ENTERP_NUM AND AL1.MANUFACTURER_ID=AL3.MANUFACTURER_ID AND ");
			    sql.append("AL1.FIN_ENTERP_NUM=AL3.FIN_ENTERP_NUM) AND (AL1.MANUFACTURER_ID=3242)");
			    sql.append("AND AL4.PROD_DESCR LIKE '%");
			    sql.append(ghzmhz);
			    sql.append("%' AND AL4.PROD_DESCR LIKE '%");
			    sql.append(inch);
			    sql.append("%' AND AL4.PROD_DESCR LIKE '%");
			    sql.append(gbmb);
			    sql.append("%' AND AL4.PROD_DESCR LIKE '%");
			    sql.append(proc);
			    sql.append("%' AND AL4.PROD_TYPE LIKE '%");
			    sql.append(equip);
			    sql.append("' AND AL3.MANUFACTURER_NAME LIKE '");
			    sql.append(mname);
			    sql.append("%'");	
			   
			    
		    }
		    
	        try
	        {
	        	logger.debug("Type/Model Query:"+sql);
                conn = dataSourceTransactionManagerIcfs.getDataSource().getConnection();
	        	stmt= conn.createStatement();
	        	
	        	ResultSet results= stmt.executeQuery(sql.toString());
	        	OEMProduct oemProduct = null;
	        	 
	    		while(results.next()){    	                
	                    oemProduct  = new OEMProduct();
	                	oemProduct.setEquipmentType(results.getString(1).trim());
	                	oemProduct.setModel(results.getString(2).trim());
	                	oemProduct.setProductDescription(results.getString(3).trim());
	                	oemProduct.setManufacturerName(results.getString(4).trim());
	                	oemTableList.add(oemProduct);
	    		}	

	    		if(oemTableList.isEmpty()) logger.debug("Type/Model Information not found");

	            return oemTableList;
	            
	        } catch (SQLException exc)
	        {
	            logger.fatal("Exception in getTypeMods() is :"+ exc, exc);
	            throw exc;
	        }finally {
	        	
	        	stmt.close();
	        	conn.close();
	        }
			
		}
		

	    public String getFinEnterpriseNum(String isoCountryCode, String db2Region) throws IllegalArgumentException
	    {
	        String enterpriseNum = null;

	        //String regionKey = RegionManager.getBackendRegionPropertyKey(db2Region);
	        
	        /*String regionKey = PluginConfigurationPane.getPropertiesMap().get(UtilGilConstants.REGION);
	        enterpriseNum = DefaultPropertyManager.getPropertiesManager().getProperty(regionKey + "_" + isoCountryCode + "_IBM_ENTERPRISE_NUMBER");*/
	        
	        enterpriseNum =  countryProperties.getIbmEnterpriseNum();

	        /*int nextDB2Region = RegionManager.getNextCascadingBackendRegion(db2Region);
	        if ((enterpriseNum == null) && (nextDB2Region != db2Region))
	        {
	            enterpriseNum = getFinEnterpriseNum(isoCountryCode, nextDB2Region);
	        }*/

	        if (enterpriseNum == null)
	        {
	            throw new IllegalArgumentException("Country Code " + isoCountryCode + " is not configured for product reference lookups");
	        }

	        logger.debug(isoCountryCode + " IBM ENTERPRISE NUMBER = " + enterpriseNum);
	        return enterpriseNum;

	    }
	    
	    
	    private boolean isDevRegion(){
	    	if(RegionManager.getBackendRegion() == RegionManager.DEVELOPMENT || RegionManager.getBackendRegion() == RegionManager.ALTERNATEDEVELOPMENT || RegionManager.getBackendRegion() == RegionManager.USERTEST ){
	    		return true;
	    	}
	    	return false;
	    }

}
