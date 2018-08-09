package com.ibm.gil.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;


import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.ibm.gil.util.UtilGilConstants;
import com.ibm.igf.gil.CountryProperties;
import com.ibm.igf.gil.SpringApplicationContext;
import com.ibm.igf.hmvc.PluginConfigurationPane;

public class CountryPropertiesDataModel extends DataModel  {
	
	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(CountryPropertiesDataModel.class);
	
	private ApplicationContext appContext;
	private DataSourceTransactionManager dataSourceTransactionManager = null;
    private JdbcTemplate icnJdbcTemplate;
    private DataSource dataSource;
    
	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public CountryPropertiesDataModel() {
		
		appContext = SpringApplicationContext.getInstance();
		dataSourceTransactionManager = (DataSourceTransactionManager) appContext.getBean("ECMClientTransactionManager");
		icnJdbcTemplate = new JdbcTemplate(dataSourceTransactionManager.getDataSource());
	}
	
    public ApplicationContext getAppContext() {
		return appContext;
	}

	public void setAppContext(ApplicationContext appContext) {
		this.appContext = appContext;
	}

	public DataSourceTransactionManager getDataSourceTransactionManager() {
		return dataSourceTransactionManager;
	}

	public void setDataSourceTransactionManager(
			DataSourceTransactionManager dataSourceTransactionManager) {
		this.dataSourceTransactionManager = dataSourceTransactionManager;
	}

	public JdbcTemplate getIcnJdbcTemplate() {
		return icnJdbcTemplate;
	}

	public void setIcnJdbcTemplate(JdbcTemplate icnJdbcTemplate) {
		this.icnJdbcTemplate = icnJdbcTemplate;
	}

	public HashMap<String, CountryProperties> selectAllCountryProperties()  {
    	
    	HashMap<String, CountryProperties> countryPropertiesMap = new HashMap<String, CountryProperties>();
    	logger.debug("Accesing ICN Database to get Gil properties values");
    	String gilSchema=PluginConfigurationPane.getPropertiesMap().get(UtilGilConstants.GILCN_SCHEMA);
    	List<Map<String, Object>> lCountryPropertiesMaps = getIcnJdbcTemplate().queryForList("SELECT * FROM "+gilSchema+"GIL_PROPERTY");
    	CountryProperties countryProp = null;
    	Iterator it = lCountryPropertiesMaps.iterator();
    	
    	while(it.hasNext()){
    		
    		Map<String, Object> cMap = (Map)it.next();
    		countryProp = new CountryProperties();
    		
    		countryProp.setCountry((String)cMap.get("COUNTRY"));
    		countryProp.setCountryName((String)cMap.get("COUNTRY_NAME"));
    		countryProp.setIbmCountryCode((String)cMap.get("IBM_COUNTRY_CODE"));
    		countryProp.setIbmEnterpriseNum((String)cMap.get("IBM_ENTERPRISE_NUM"));
    		countryProp.setGcmsSchema((String)cMap.get("GCMS_SCHEMA"));
    		countryProp.setAptsSchema((String)cMap.get("APTS_SCHEMA"));
    		countryProp.setIcfsSchema((String)cMap.get("ICFS_SCHEMA"));
    		countryProp.setMiscSchema((String)cMap.get("MISC_SCHEMA"));
    		countryProp.setRdcUseAddrSeqNum((String)cMap.get("RDC_USE_ADDR_SEQ_NO"));
    		countryProp.setRdcUseSeqNum((String)cMap.get("RDC_USE_SEQ_NO"));
    		countryProp.setGaptsJndi((String)cMap.get("GAPTS_JNDI"));
    		countryProp.setIcfsJndi((String)cMap.get("ICFS_JNDI"));
    		countryProp.setHybrJndi((String)cMap.get("HYBR_JNDI"));
    		countryProp.setHybrSchema((String)cMap.get("HYBR_SCHEMA"));
    		
      		countryPropertiesMap.put(countryProp.getCountry(), countryProp);
    		
    	}
    	logger.debug("Gil properties values retrieved successfully.");
    	return countryPropertiesMap;
    }

}
