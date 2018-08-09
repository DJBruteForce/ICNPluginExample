package com.ibm.gil.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.ibm.gil.business.WebServicesEndpoint;
import com.ibm.gil.service.ServiceHelper;
import com.ibm.gil.util.UtilGilConstants;
import com.ibm.igf.gil.SpringApplicationContext;
import com.ibm.igf.hmvc.PluginConfigurationPane;

public class WebServicesEnpointDataModel extends DataModel  {
	
	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(WebServicesEnpointDataModel.class);
	
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

	public WebServicesEnpointDataModel() {
		
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

	public HashMap<String, List<WebServicesEndpoint>> loadWebServicesEndpoints()  {
		
		logger.debug("Accesing ICN Database to get Gil WebServices values");
		String gilSchema=PluginConfigurationPane.getPropertiesMap().get(UtilGilConstants.GILCN_SCHEMA);
		HashMap<String, List<WebServicesEndpoint>> epMap = new HashMap<String, List<WebServicesEndpoint>>();
		List<WebServicesEndpoint> listOfEp =new ArrayList<WebServicesEndpoint>();
		String emptyStr = ServiceHelper.HTML_EMPTY_SPACE;
        
		try {
			Connection conn = getDataSourceTransactionManager().getDataSource().getConnection();
			PreparedStatement pstmt1 = conn.prepareStatement("SET ENCRYPTION PASSWORD = '=ZAQWSX-543MSKOP'");
			pstmt1.execute();

			PreparedStatement pstmt2 = conn.prepareStatement("SELECT SYSTEM,URL,NAME,USERID,decrypt_char(PASSWORD) as PASSWORD, DEFAULT FROM "+gilSchema+"GIL_WEBSERVICES ORDER BY SYSTEM");
			ResultSet rs = pstmt2.executeQuery();

			String sys , sysAnt = "";
			WebServicesEndpoint ws = null;
			
			ws = new WebServicesEndpoint();
    		ws.setSystem(emptyStr);
    		ws.setValue(emptyStr);
    		ws.setLabel(emptyStr);
    		ws.setFunctionalId(emptyStr);
    		ws.setAccess(emptyStr);	
			listOfEp.add(ws);
			
			while(rs.next()){

	    		ws = new WebServicesEndpoint();
	    		sys = rs.getString("SYSTEM");
	    		
	    		ws.setSystem(rs.getString("SYSTEM"));
	    		ws.setValue(rs.getString("URL"));
	    		ws.setLabel(rs.getString("NAME"));
	    		ws.setFunctionalId(rs.getString("USERID"));
	    		ws.setAccess(rs.getString("PASSWORD"));
	    		String defEp = rs.getString("DEFAULT");
	    		ws.setDefaultEp((defEp!=null && defEp.equalsIgnoreCase("Y"))?true:false);
	
	    		 if (sys.equalsIgnoreCase(sysAnt)){
	    			 listOfEp.add(ws); 
	    		 } else { 			
	    			 listOfEp =new ArrayList<WebServicesEndpoint>();
	    			 	WebServicesEndpoint wsAux = new WebServicesEndpoint();
	    			 	wsAux.setSystem(emptyStr);
	    			 	wsAux.setValue(emptyStr);
	    			 	wsAux.setLabel(emptyStr);
	    			 	wsAux.setFunctionalId(emptyStr);
	    			 	wsAux.setAccess(emptyStr);	
	    				listOfEp.add(wsAux);
	    				
	    			 listOfEp.add(ws);
	    		 }
	    		 
	    		 epMap.put(sys, listOfEp);
	    		 sysAnt = sys;
			}
    	
		} catch (SQLException e) {
			logger.error(e,e);
		} catch (Exception e) {
			logger.error(e,e);
		}

    	logger.debug("Gil WebServices values retrieved successfully.");
    	return epMap;
    }

}
