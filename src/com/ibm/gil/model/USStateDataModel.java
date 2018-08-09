package com.ibm.gil.model;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



import com.ibm.gil.business.USState;
import com.ibm.gil.util.UtilGilConstants;


import com.ibm.igf.hmvc.PluginConfigurationPane;
import com.ibm.igf.webservice.security.GILConstants;

import financing.tools.gcps.common.util.StringUtil;

public class USStateDataModel extends DataModel {
	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(USStateDataModel.class);
	private USState usStateParent;
	
	public USStateDataModel(USState usStateParent){
		super(usStateParent);
		this.usStateParent=usStateParent;
	}
   
  
	
	
	
	
	public List<String> getUSStateCodes()throws Exception{
		CallableStatement cs = null;
		Connection dbConnection = null;
		ResultSet rs = null;
		List<String> stateCodeResults = null;
//        String region = "DSND";
//        String spregion = "DSNDSP";
        Connection conn=null;
        String schema =null;
        String spSchema=PluginConfigurationPane.getPropertiesMap().get(UtilGilConstants.ICFS_DEV_SCHEMA);
        logger.debug("spSchema:"+spSchema+"-");
        if(spSchema!=null && !spSchema.equals("")  ){
        	
        	schema=spSchema;
        	
        }else{
        	schema= getCountryProperties().getIcfsSchema();
        	logger.debug("spSchema from configuration panel is empty or null then getting from DB");
        	
        }
        logger.debug("schema: "+schema);
//        String schema =//getDatabaseDriver().getSchema(spregion, getCOUNTRY());
        StringBuffer sql = new StringBuffer();
        
        // build the sql statement, use append since it is more efficient
        sql.append("{CALL ");
        sql.append(schema);
        sql.append("JFCS00A(?,?,?,?,?,?,?,?,?,?,?)}");
        
        String sqlSP = sql.toString();
        logger.debug("Get US state codes: "+sql);
        
        try
        {
        	conn=getDataSourceTransactionManagerIcfs().getDataSource().getConnection();
//        	dbConnection = getDatabaseDriver().getConnection(region, getCOUNTRY());
        	cs = conn.prepareCall(sqlSP);
        	
			// IN parameters
			cs.setString(1, GILConstants.REQUEST_TYPE);
			cs.setString(2, GILConstants.CTS_US_COUNTRY_CODE);
			cs.setString(3, GILConstants.CTS_SUBSIDAIRY_CODE);
			cs.setString(4, GILConstants.SUB_TAB_NUM);
			cs.setString(5, GILConstants.ITEM_ID);
			cs.setString(6, GILConstants.EMPTY_STRING);
			cs.setString(7, GILConstants.EMPTY_STRING);
			
		
			// OUT Parameters
			cs.registerOutParameter(8, java.sql.Types.CHAR);
			cs.registerOutParameter(9, java.sql.Types.INTEGER);
			cs.registerOutParameter(10, java.sql.Types.CHAR);
			cs.registerOutParameter(11, java.sql.Types.INTEGER);
			
            java.util.Date startTime = new java.util.Date();
            logger.debug("Query start = " + startTime + " SQL = " + sql);

	        // perform the sql
            cs.execute();
	            
            logger.debug("Procedure Call Query time = " + (new java.util.Date().getTime() - startTime.getTime()) + " ms  SQL = " + sql);
            
			// Process the ICFS stored procedure return codes;
			processSPOutParameters(cs.getString(8), cs.getString(10));
 			
			usStateParent.setTOLERANCEAMT(cs.getString(11));
			stateCodeResults  = new ArrayList<String>();				
			rs = cs.getResultSet();
			while(rs.next()) {
				String stateCode = rs.getString(1);
				String upfrontIndc = rs.getString(2);
				String finType = rs.getString(4);
				if(upfrontIndc.equals("Y") && (finType.equals("FMV") || finType.startsWith("*"))){
					logger.debug("State Code: "+stateCode);
					stateCodeResults.add(stateCode);
				}

			}
			//loading stateCodes into usState object
			usStateParent.getStateCodeResults().clear();
			usStateParent.getStateCodeResults().addAll(stateCodeResults);
            
			
        } catch (SQLException exc)
        {
        	
//        	ConnectionPool.closeDBConnections();
//            db2TransactionFailed = true;
            logger.fatal(exc.toString() + "\n" + sql + "\n");
            throw exc;
        }finally{
        	rs.close();
        	cs.close();
        	conn.close();
        }
        return stateCodeResults;
		
	}
	
    private void processSPOutParameters(String returnCode, String errorDesc)throws Exception{
   	
		if (returnCode != null	&& !StringUtil.isNullOrEmptyString(returnCode)) {
			if (returnCode.equalsIgnoreCase(GILConstants.RETURN_CODE_00)) {
				logger.debug("getUpfrontStates(): Request Processed Successfully in ICFS for upfront state stored procedure");	
			} else if (returnCode.equalsIgnoreCase(GILConstants.RETURN_CODE_01)) {
				logger.fatal("getUpfrontStates(): Request match not found in ICFS for upfront state stored procedure");
				throw new SQLException(errorDesc);				
			} else if (returnCode.equalsIgnoreCase(GILConstants.RETURN_CODE_02)) {
				logger.fatal("getUpfrontStates(): Fault Code 02 returned from ICFS for upfront state stored procedure");
				throw new SQLException(errorDesc);				
			} else {
				logger.fatal("getUpfrontStates(): Fault Code returned from ICFS for upfront state stored procedure");
				throw new SQLException(errorDesc);			
			}    	
		}
	
    }
		


}
