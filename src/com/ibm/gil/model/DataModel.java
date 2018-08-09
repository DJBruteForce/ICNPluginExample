package com.ibm.gil.model;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;


import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.ibm.gil.business.Indexing;
import com.ibm.gil.util.RegionalBigDecimal;
import com.ibm.igf.contentmanager.rmi.ContentManagerImplementation;
import com.ibm.igf.contentmanager.rmi.ContentManagerInterface;
import com.ibm.igf.gil.CountryProperties;
import com.ibm.igf.gil.SpringApplicationContext;
import com.ibm.igf.hmvc.CountryManager;
import com.ibm.igf.hmvc.DB2;
import com.ibm.igf.hmvc.ResultSetCache;
import com.ibm.mm.sdk.common.DKDDO;
import com.ibm.mm.sdk.common.DKEventDefICM;
import com.ibm.mm.sdk.common.DKException;
import com.ibm.mm.sdk.common.DKUsageError;
import com.ibm.mm.sdk.common.dkCollection;
import com.ibm.mm.sdk.common.dkIterator;
import com.ibm.mm.sdk.server.DKDatastoreICM;
import com.ibm.w3.financing.tools.gcms.query.results.DOCUMENT;
import com.ibm.w3.financing.tools.gcms.query.results.DocumentRoot;
import com.ibm.w3.financing.tools.gcms.query.results.QUERYRESULTSType;
import com.ibm.w3.financing.tools.gcms.query.results.util.ResultsResourceUtil;

public class DataModel {
	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(DataModel.class);
	
	//Attributes for transaction management
	private CountryProperties countryProperties = null;
	private ApplicationContext appContext;
	private DataSourceTransactionManager dataSourceTransactionManager = null;//for gapts
	private DataSourceTransactionManager dataSourceTransactionManagerIcfs = null;
	private DataSourceTransactionManager dataSourceTransactionManagerHybr = null;
	
    private JdbcTemplate gaptsJdbcTemplate;
    Indexing indexingObj = null;
    //Story 1640938: GIL - changes needed for GCMS JR
    private ResultSetCache fieldhybridResults = null;
    
    private ResultSetCache fielOLResults = null;
    // End Story 1640938
	
   //private static transient ContentManagerConnection CM = new ContentManagerConnection();
    
    protected ContentManagerInterface CM ;
    
    private java.util.Date createdDate = null;
    private SimpleDateFormat createDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa zzz");
    private String serverTimezone = "";
    private static DB2 database = null;
    String aptsregion = "APTS";
    
    public DataModel(Indexing indexingObj)	{
    	
		countryProperties = CountryManager.getCountryProperties(indexingObj.getCOUNTRY());
		appContext = SpringApplicationContext.getInstance();
		dataSourceTransactionManager = (DataSourceTransactionManager) appContext.getBean(countryProperties.getAptsSpringTransactionManagerId());
		dataSourceTransactionManagerIcfs=(DataSourceTransactionManager)appContext.getBean(countryProperties.getIcfsSpringTransactionManagerId());
		dataSourceTransactionManagerHybr=(DataSourceTransactionManager)appContext.getBean(countryProperties.getAptsSpringTransactionManagerId());
		gaptsJdbcTemplate = new JdbcTemplate(dataSourceTransactionManager.getDataSource());
		this.indexingObj = indexingObj;
		CM = new ContentManagerImplementation(indexingObj.getDatastore());
		
    }
    
    public Indexing getIndexingObj() {
		return indexingObj;
	}

	public void setIndexingObj(Indexing indexingObj) {
		this.indexingObj = indexingObj;
	}

	public DataModel (){

    }
	
	 public ContentManagerInterface getContentManager() {
			return CM;
		}

		public void setContentManager(ContentManagerInterface cM) {
			CM = cM;
		}
      
	    public DataSourceTransactionManager getDataSourceTransactionManagerHybr() {
			return dataSourceTransactionManagerHybr;
		}

		public void setDataSourceTransactionManagerHybr(DataSourceTransactionManager dataSourceTransactionManagerHybr) {
			this.dataSourceTransactionManagerHybr = dataSourceTransactionManagerHybr;
		}	
	
    public DataSourceTransactionManager getDataSourceTransactionManagerIcfs() {
			return dataSourceTransactionManagerIcfs;
		}

	public void setDataSourceTransactionManagerIcfs(
				DataSourceTransactionManager dataSourceTransactionManagerIcfs) {
			this.dataSourceTransactionManagerIcfs = dataSourceTransactionManagerIcfs;
		}

	public CountryProperties getCountryProperties() {
		return countryProperties;
	}

	public void setCountryProperties(CountryProperties countryProperties) {
		this.countryProperties = countryProperties;
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

	public JdbcTemplate getGaptsJdbcTemplate() {
			return gaptsJdbcTemplate;
	}

	public void setGaptsJdbcTemplate(JdbcTemplate jdbcTemplate) {
			this.gaptsJdbcTemplate = jdbcTemplate;
	}
	
    
    public void continueWorkflow(String country, String objectId, String toBeIndexedWorkFlow,DKDatastoreICM datastore)
    {
    	try {
      		//getContentManager(country).completeWorkflow(objectId, toBeIndexedWorkFlow,datastore);
    		CM.completeWorkflow(objectId, toBeIndexedWorkFlow,datastore);
    	} catch (Exception exc) {
            logger.debug("Error continuing workflow process");
            logger.debug(exc.toString());
		}
    }
    
    public void copyAttributes(String fromObjectId, String fromIndex, String toObjectId, String toIndex, ArrayList copyFields, ArrayList copyValues, String country,DKDatastoreICM datastore)
    {
        try
        {
            
        	CM = new ContentManagerImplementation(indexingObj.getDatastore());
            fromIndex = CM.indexDescriptionToName(fromIndex,datastore);
            toIndex = CM.indexDescriptionToName(toIndex,datastore);

            String[] fromfields = CM.getIndexFields(fromIndex);
            String[] fromvalues = CM.getIndexValues(fromObjectId,datastore);
            String[] newfields = CM.getIndexFields(toIndex);
            Hashtable hashValues = new Hashtable();

            for (int i = 0; ((i < fromfields.length) && (i < fromvalues.length)); i++)
            {
                String fromname = fromfields[i];
                Object fromvalue = fromvalues[i];
                if ((fromvalue != null) && (fromvalue.toString().trim().length() > 0))
                {
                    for (int j = 0; ((newfields != null) && (j < newfields.length)); j++)
                    {
                        // match up the new and old attributes
                        if (fromname.equals(newfields[j]))
                        {
                            // check if the field has been assigned
                            if (!copyFields.contains(newfields[j]))
                            {
                                hashValues.put(newfields[j], fromvalue);
                            }
                        }
                    }
                }
            }

            for (Enumeration e = hashValues.keys(); e.hasMoreElements();)
            {
                String field = (String) e.nextElement();
                String value = (String) hashValues.get(field);
                copyFields.add(field);
                copyValues.add(value);
            }
        } catch (Exception exc)
        {
            logger.debug("Error copying item attributes to folder");
            logger.debug(exc.toString());
        }
    }
	
    public String getFieldMatch(String fieldName, String[] fields, String[] values)
    {
        if (fields == null)
            return "";

        if (values == null)
            return "";

        for (int i = 0; i < fields.length && i < values.length; i++)
        {
            if (fields[i].equals(fieldName))
            {
                if (values[i] != null)
                {
                    return values[i].trim();
                } else
                {
                    logger.debug("Invalid field specified " + fieldName);
                    return null;
                }
            }
        }
        return "";
    }
    
    public static DB2 getDatabaseDriver()
    {
    	if (database == null)
    	{
    		database = DB2.getInstance();
    	}
    	return database;
    }
    
    
    public static void setDatabaseDriver(DB2 driver)
    {
    	database = driver;
    }
    

    public String trimUSERID(String userId)
    {
        String username = userId;
        int atsign = username.indexOf("@");
        if (atsign > 0)
            username = username.substring(0, atsign);
        if(username.length()>10)
        	username = username.substring(0,10);        

        return username;
    }
    

/**
     * Insert the method's description here. Creation date: (3/12/2001 12:21:44
     * PM)
     * 
     * @return RegionalBigDecimal
     * @param index
     *            int
     */
    public RegionalBigDecimal getDecimal(Object obj)
    {
       
        if (obj == null)
            return RegionalBigDecimal.ZERO;
        else if (obj instanceof RegionalBigDecimal)
            return (RegionalBigDecimal) obj;
        else
        {
            String value = obj.toString().trim();
            if (value.length() == 0)
                return RegionalBigDecimal.ZERO;
            else
                return new RegionalBigDecimal(value);
        }
    }
    
    /**
     * Insert the method's description here. Creation date: (3/12/2001 12:21:44
     * PM)
     * 
     * @return java.lang.String
     * @param index
     *            int
     */
    public int getInt(Object obj)
    {
        try
        {
           
            if (obj == null)
                return 0;
            else if (obj instanceof String)
                return Integer.parseInt((String) obj);
            else if (obj instanceof Integer)
                return ((Integer) obj).intValue();
        } catch (Exception exc)
        {
        }
        return 0;
    }
    
    /**
     * Insert the method's description here. Creation date: (3/12/2001 12:21:44
     * PM)
     * 
     * @return java.lang.String
     * @param index
     *            int
     */
    public String getString(Object obj )
    {
        
        if (obj == null)
            return "";
        else if (obj instanceof String)
            return (String) obj;
        else
            return obj.toString();
    }
    
    
    /**
     * Insert the method's description here. Creation date: (3/12/2001 12:21:44
     * PM)
     * 
     * @return java.lang.String
     * @param index
     *            int
     */
    public String getSqlString(Object obj)
    {

        if (obj == null)
            return DB2.sqlString("");
        else if (obj instanceof String)
            return DB2.sqlString((String) obj);
        else
            return DB2.sqlString(obj.toString());
    }
    
    
    public Date retrieveCreatedDate()
    {
        
        try
        {
            if (createdDate == null)
            {
                Date now = new Date();
                
                // lookup the timezone of the server where the resource objects are stored
                //CM.lookupContentManager();
                //String RMServerTimezone = ContentManagerConnection.getServerTimezone();

                // get the created date from RMI daemon
                // the origional string was the timezone of the rm server
                // the rmi server thinks is in the timezone of the rmi server
                // JST->JST works fine, but JST RM server -> GMT RMI Server doesn't
                // so it needs to have the timezone offset undone if they don't match
               // logger.debug("ObjectID: "+getIndexingObj().getOBJECTID());
               // logger.debug("Datastore: "+getIndexingObj().getDatastore().datastoreName());
                createdDate = CM.getCreatedDate(getIndexingObj().getOBJECTID(), getIndexingObj().getDatastore());;
               //String RMIServerTimezone = ContentManagerConnection.getServerTimezone();
            	//logger.debug("Created Date Retrieved : " + com.ibm.gil.util.RegionalDateConverter.formatTimeStamp("GUI-"+getIndexingObj().getLocale(), createdDate));
                //logger.debug("createdDate:"+createdDate);
                
/*                if (! RMIServerTimezone.equals(RMServerTimezone))
                {
                	// didn't retrieve the date from the "right" rmi server
                	// subtract off the offset added by the "wrong" rmi server
                	createdDate.setTime (createdDate.getTime() - TimeZone.getTimeZone(RMServerTimezone).getOffset(createdDate.getTime()));

                	logger.debug("RM Server Timezone: " + RMServerTimezone);
                	logger.debug("RMI Server Timezone: " + RMIServerTimezone);
                	logger.debug("Created Date Adjusted : " + RegionalDateConverter.formatTimeStamp("GUI", createdDate));
                }*/
                
               // logger.info("Retrieved Document created date taking (ms) " + (new Date().getTime() - now.getTime()));
               // logger.info(com.ibm.gil.util.RegionalDateConverter.formatTimeStamp("GUI", createdDate) + " " + TimeZone.getDefault().getDisplayName());

               
                
            }
            
            
            
        } catch (Exception exc)
        {
            logger.debug("Error retrieving created date");
            logger.debug(exc.toString());;
        }
        
        return createdDate;

    }
	
    
	protected String fixVersion(String xml)
	{
		String oldtxt = "<QUERYRESULTS>";
		String newtxt = "<tns:QUERYRESULTS xmlns:tns=\"http://w3.ibm.com/financing/tools/GCMS/QueryResults\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://w3.ibm.com/financing/tools/GCMS/QueryResults QueryResults.xsd \">	";
		
		xml = xml.replaceFirst(oldtxt, newtxt);
		xml = xml.replaceFirst("</QUERYRESULTS>", "</tns:QUERYRESULTS>");
		xml = xml.replaceAll("index=", "itemtype=");
		
		return xml;
	}

    public static String formatXML(String text)
    {
    	if (text == null)
    		return "";
    	
        String newText = text;

        char c;
        int length = 0;
        String goodValue;
        // look thru the string
        for (int i = 0; i < newText.length(); i++)
        {
        	// one character at a time
            c = newText.charAt(i);
            
            // is it non letter or digit
            if (isQuoteableCharacter(c))
            {
            	// set the default good value
            	goodValue = "&#x" + Integer.toHexString(c) + ";";
            	
            	// determine the length
            	length = goodValue.length();
            	
            	// substitute the text
                newText = newText.substring(0, i) + goodValue + newText.substring(i + 1);
                
                // skip the rest of the substituted string
                i += length - 1;
            }
        }
        return newText;
    }
    
    private static boolean isQuoteableCharacter (char c)
    {
    	if (c > 126)
    		return true;
    	
    	return false;
    }
    
    public DOCUMENT queryDocument() throws Exception
    {
    	
    	ContentManagerImplementation cm  = new ContentManagerImplementation(getIndexingObj().getDatastore());
    	
    	if (getIndexingObj().getOBJECTID().trim().length() == 0)
    	{
    		return null;
    	}
        // query the item
        logger.info("Searching for document in Content Manager");
        String xml = cm.executeQuery("*", "[@ITEMID=\"" + getIndexingObj().getOBJECTID() + "\"]",getIndexingObj().getDatastore());
        String indexName = cm.indexDescriptionToName(getIndexingObj().getINDEXCLASS(),getIndexingObj().getDatastore());
        
        if (xml == null)
        	return null;
        if (xml.indexOf(indexName) < 0)
        	return null; 
        if (xml.indexOf(getIndexingObj().getOBJECTID()) < 0)
        	return null;
 
        xml = fixVersion(xml);
        xml = formatXML(xml);
               
		ByteArrayInputStream bais = new ByteArrayInputStream(xml.getBytes());

		// load the document
		ResultsResourceUtil util = new ResultsResourceUtil();
		DocumentRoot document = util.load(bais);

		// retrieve the configuration
		QUERYRESULTSType results = document.getQUERYRESULTS();

		List<DOCUMENT> docs = results.getDOCUMENT();
		for (Iterator<DOCUMENT> itr = docs.iterator(); itr.hasNext();) {
			DOCUMENT aDOCUMENT = itr.next();
			return aDOCUMENT;
		}

		return null;
        
    }
    
    
    
    public void continueWorkflow()
    {
    	
    	try {
    		
    		
    		CM.completeWorkflow(getIndexingObj().getOBJECTID(), getIndexingObj().getTOBEINDEXEDWORKFLOW(), getIndexingObj().getDatastore());
    		
    	} catch (Exception exc) {
           
    		logger.debug("Error continuing workflow process:" + exc.toString());
		}
    }
    
    /**
	 * Locks an item on Content Manager.
	 * 
	 * @param datastore - the datastore to be used
	 * @param itemId - the id of the item to be locked
	 * @return true if the item was locked successfully, false otherwise.
	 */
	public boolean lockItem(){
		String itemId = indexingObj.getOBJECTID();
		DKDatastoreICM datastore = indexingObj.getDatastore();
		try {
			DKDDO item = ((ContentManagerImplementation) CM).getObjectFromLibrary(itemId, false, datastore);
			item.retrieve();
			datastore.checkOut(item);
			return true;
			
		} catch (DKException e) {
			logger.debug(e.getMessage());
			logger.debug("Item is not checked out");
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return false;
	}
	

	/**
	 * Unlocks an item on Content Manager.
	 * 
	 * @param datastore - the datastore to be used
	 * @param itemId - the id of the item to be unlocked
	 * @return true if the item was unlocked successfully, false otherwise.
	 */
	public boolean unlockItem(){
		DKDatastoreICM datastore = indexingObj.getDatastore();
		String itemId = indexingObj.getOBJECTID();
		try {
			ContentManagerImplementation cmi = new ContentManagerImplementation(datastore);
			DKDDO item = cmi.getObjectFromLibrary(itemId, false, datastore);
			datastore.checkIn(item);
			return true;
			
		} catch (DKException e) {
			logger.debug(e.getMessage());
			logger.debug("Item is checked out already");
			
		} catch (Exception e) {
			logger.fatal(e.getMessage());
			
		}
		return false;
	}
	
	public void getHistoryLog(){
		DKDatastoreICM datastore = indexingObj.getDatastore();
		String itemId = indexingObj.getOBJECTID();
		dkCollection itemEvents = null;
		dkIterator it = null;
		DKEventDefICM event = null;
		
		try {
			itemEvents = datastore.listEvents(itemId);
		} catch (DKException e1) {
			logger.fatal(e1.getMessage());
		} catch (Exception e1) {
			logger.fatal(e1.getMessage());
		}
		
		it = itemEvents.createIterator();
		while(it.more()){
			try {
				event = ((DKEventDefICM)(it.next()));
				logger.debug(
						"Event code     :" + event.getCode() + "\n" +
						"Event item id  :" + event.getItemId() + "\n" +
						"Event timestamp:" + event.createTimestamp().toGMTString() + "\n" +
						"Event userid   :" + event.userId() + "\n" +
						"Event code     :" + event.getCode() + "\n" +
						"Event data 1   :" + event.getEventData1() + "\n" +
						"Event data 2   :" + event.getEventData2() + "\n" +
						"Event data 3   :" + event.getEventData3() + "\n" +
						"Event data 4   :" + event.getEventData4() + "\n" +
						"Event data 5   :" + event.getEventData5() + "\n"
				);
			} catch (DKUsageError e) {
				logger.fatal(e.getMessage());
			}
		}
	}
    
    //Story 1640938: GIL - changes needed for GCMS JR
    public ResultSetCache selectFieldhybridStatement() throws SQLException {

        StringBuffer sql = new StringBuffer();      
		CountryProperties countryProperties = CountryManager.getCountryProperties(getIndexingObj().getCOUNTRY());
		String schema = countryProperties.getHybrSchema();
		Connection conn;
		Statement stmt;

        // build the sql statement, use append since it is more efficient
        sql.append("select GCPSJCTYCD from ");
        sql.append(schema);	
        sql.append("GCPSJRCTY");
                
        try {
            conn=getDataSourceTransactionManagerHybr().getDataSource().getConnection();
            stmt= conn.createStatement();
            logger.debug("###SQL### "+sql);
            ResultSet results = stmt.executeQuery(sql.toString());
            
            fieldhybridResults = new ResultSetCache(results,getIndexingObj().getCOUNTRY(), 0, 0, "");

            // return the results
            return (fieldhybridResults);
            
        } catch (SQLException exc) {
            logger.fatal(exc.toString() + "\n" + sql + "\n");
            throw exc;
        }
    }
    
}
