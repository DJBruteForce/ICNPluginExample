package com.ibm.igf.hmvc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.ResultSet;
//import java.sql.Connection;
//import java.sql.DatabaseMetaData;
//import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;

import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import javax.sql.DataSource;


public class DB2 extends com.ibm.igf.hmvc.DB2ConnectionPool {
	
	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(DB2.class);
	
	public static final String DB2_DATE_FORMAT = "yyy-MM-dd";

    private static java.util.Properties propertiesManager = new Properties();

    public static transient boolean ideFlag = false;

    private static transient java.util.Date lastConnection = new java.util.Date();

    private static java.lang.String userId = null;

    private static java.lang.String userCC = null;
    
    private static DB2 instanceVariable = new DB2();

    /**
     * DB2Connection constructor comment.
     */
    public DB2()
    {
        super();
    }

    /**
     * Insert the method's description here. Creation date: (7/9/2003 10:32:01
     * AM)
     * 
     * @param in
     *            java.io.InputStream
     */
    static public void dumpStream(InputStream inStream)
    {
        try
        {
            BufferedReader in = new BufferedReader(new InputStreamReader(inStream));
            String data = null;
            data = in.readLine();
            while (data != null)
            {
                logger.debug(data);
                data = in.readLine();
            }
            in.close();
        } catch (IOException exc)
        {
            logger.debug("Error reading file: db2cli.ini");
            return;
        }
    }

    public java.sql.Connection getConnection() throws SQLException, NamingException
    {
       // java.sql.Connection conn = getConnection(region + "_" + country, RegionManager.getBackendRegion());
        
        java.sql.Connection conn = getConnection();
        if (conn == null)
        	//conn = getConnection(region, RegionManager.getBackendRegion());
            conn = getConnection();
        return conn;
    }

   /* public static java.sql.Connection getConnection(String region, int db2RegionToRetrieve) throws SQLException
    {

        // update the properties in case they changed, every 30 minutes minimum
        long diff = (new java.util.Date().getTime() - lastConnection.getTime());
        if (diff > 30 * 60 * 1000)
        {
            lastConnection = new java.util.Date();
        }

        String connectionProperty = getPropertiesManager().getProperty(region + "_" + RegionManager.getBackendRegionPropertyKey(db2RegionToRetrieve) + "_REGION");
        String username = getPropertiesManager().getProperty(connectionProperty + "_USERNAME");
        String password = getPropertiesManager().getProperty(connectionProperty + "_PASSWORD");

        int nextDB2Region = RegionManager.getNextCascadingBackendRegion(db2RegionToRetrieve);
        java.sql.Connection conn = null;

        if ((region != null) && (username != null) && (password != null))
        {
        	String dbURL = getPropertiesManager().getProperty(connectionProperty+"_DRIVERURL");
        	if(dbURL == null || dbURL.trim().length() == 0){
        		dbURL = connectionProperty;
        	}
            conn = getConnection(region, dbURL, username, password);
        }
        if ((conn == null) && (nextDB2Region != db2RegionToRetrieve))
        {
            conn = getConnection(region, nextDB2Region);
        }

        return conn;
    }*/

    
/*	public static java.sql.Connection getConnection() throws SQLException,NamingException {
		
		java.sql.Connection conn = null;

		InitialContext initialContext = new InitialContext();
		try {

			DataSource dataSource = (DataSource) initialContext	.lookup("GAPTSDBJndi");
			conn = dataSource.getConnection();

		}

		catch (NameNotFoundException e) {
			
			logger.fatal("naming exception is :" + e);
			throw e;

		}

		return conn;

	}*/
	
	public static java.sql.Connection getConnection(String dsJndiName) throws SQLException,NamingException {
		
			java.sql.Connection conn = null;
			InitialContext initialContext = new InitialContext();
			
			try {
			
				DataSource dataSource = (DataSource) initialContext.lookup(dsJndiName);
				conn = dataSource.getConnection();
			
			}
			
			catch (NameNotFoundException e) {
				
				logger.fatal("naming exception is :" + e);
				throw e;
			
			}
			
			return conn;

}
    

    public static Properties getPropertiesManager() throws NullPointerException
    {
        if (propertiesManager == null)
        {
            throw new NullPointerException("Properties manager not initialized");
        }
        return propertiesManager;
    }

    public String getSchema(String region, String country)
    {
/*        String schema = DB2.getSchema(region + "_" + country, RegionManager.getBackendRegion());
        if (schema == null)
            schema = DB2.getSchema(region, RegionManager.getBackendRegion());

        return schema;*/
    	
    	return "WAPT_INT.";
    }

    public static String getSchema(String region, int db2RegionToRetrieve)
    {

        String key = region + "_" + RegionManager.getBackendRegionPropertyKey(db2RegionToRetrieve) + "_SCHEMA";
        String schema = getPropertiesManager().getProperty(key);

        int nextDB2Region = RegionManager.getNextCascadingBackendRegion(db2RegionToRetrieve);
        if ((schema == null) && (nextDB2Region != db2RegionToRetrieve))
            schema = getSchema(region, nextDB2Region);

        return schema;
    }

    /**
     * Insert the method's description here. Creation date: (7/8/2003 6:36:20
     * PM)
     * 
     * @return java.lang.String
     */
    public static java.lang.String getUserCC()
    {
        return userCC;
    }

    /**
     * Insert the method's description here. Creation date: (7/8/2003 6:36:05
     * PM)
     * 
     * @return java.lang.String
     */
    public static java.lang.String getUserId()
    {
        return userId;
    }

    public static boolean isIDE()
    {
        return ideFlag;
    }

    public static void setIDE(boolean vFlag)
    {
        ideFlag = vFlag;
    }

    public static void setPropertiesManager(Properties aPropertiesManager)
    {
        propertiesManager = aPropertiesManager;
    }

    /**
     * Insert the method's description here. Creation date: (7/8/2003 6:36:20
     * PM)
     * 
     * @param newUserCC
     *            java.lang.String
     */
    public static void setUserCC(java.lang.String newUserCC)
    {
        userCC = newUserCC;
    }

    /**
     * Insert the method's description here. Creation date: (7/8/2003 6:36:05
     * PM)
     * 
     * @param newUserId
     *            java.lang.String
     */
    public static void setUserId(java.lang.String newUserId)
    {
        userId = newUserId;
    }

    /**
     * Insert the method's description here. Creation date: (1/28/2003 11:19:55
     * AM)
     */
    public static boolean updateConnections(String[][] requiredConnections)
    {

        logger.debug("Checking database setup.\n");

        Vector existingConnections = new Vector();
        boolean unicodeDisabled = false;
        boolean decimalPatch = false;
        String DB2Install = System.getProperty("DB2HOME");

        if (DB2Install != null)
        {
            // try and clean up anything passed in.
            StringBuffer clean = new StringBuffer(DB2Install.trim());
            int idx = 0;
            while ((idx = clean.indexOf("'")) >= 0)
                clean.deleteCharAt(idx);
            while ((idx = clean.indexOf("\"")) >= 0)
                clean.deleteCharAt(idx);

            DB2Install = clean.toString();

            if (!new File(DB2Install + "\\db2cli.opt").exists())
            {
                // no good
                DB2Install = null;
            }
        }

        // did we get the property?
        if (DB2Install == null)
        {
            // check where it might be
            String[] possibleLocations = {"c:\\progra~1\\ibm\\sqllib","c:\\progra~2\\ibm\\sqllib","c:\\progra~1\\sqllib", "c:\\sqllib" };
            for (int i = 0; i < possibleLocations.length; i++)
            {
                if (new File(possibleLocations[i]).exists())
                {
                    DB2Install = possibleLocations[i];
                    break;
                }

            }
        }

        // still didn't get it?
        if (DB2Install == null)
        {
            logger.debug("Cannot determine the directory for the db2 installation");
            return false;
        }

        Process proc = null;
        boolean changesMade = false;


        // build a list of existing connections
        try
        {
            logger.debug("Querying database connections");
            proc = Runtime.getRuntime().exec("db2cmd /c /i db2 list database directory ");
            BufferedReader in = new BufferedReader( new InputStreamReader( proc.getInputStream()));
            proc.waitFor();
            String data = null;
            data = in.readLine();
            while (data != null)
            {
                data = data.toUpperCase().trim();
                if (data.indexOf("DATABASE ALIAS") >= 0)
                {
                    int lastSpace = data.lastIndexOf("= ");
                    data = data.substring (lastSpace+2);
                    existingConnections.add(data);
                }
                data = in.readLine();
            }
            in.close();
        } catch (Exception exc)
        {
            logger.debug("Error querying database connections");
            return false;
        }

        try
        {
            logger.debug("Validating database connection information");

            File commandFile = new File("/db2create.clp");
            commandFile.deleteOnExit();
            FileOutputStream out = new FileOutputStream(commandFile);
            boolean createRequired = false;

            // verify that the required connections are installed
            for (int i = 0; i < requiredConnections.length; i++)
            {
                String nodename = requiredConnections[i][0];
                String dbalias = requiredConnections[i][1];
                String dbtarget = requiredConnections[i][2];
                String hostname = requiredConnections[i][3];
                String portnum = requiredConnections[i][4];
                String hosttype = requiredConnections[i][5];

                if (!existingConnections.contains(dbalias))
                {
                    // build the connections that do not exist
                    createRequired = true;
                    try
                    {

                        out.write(("uncatalog node " + nodename + "\n").getBytes());
                        out.write(("uncatalog database " + dbalias + "\n").getBytes());
                        out.write(("uncatalog dcs db " + dbalias + "\n").getBytes());
                        out.write(("uncatalog system odbc data source " + dbalias + "\n").getBytes());
                        out.write(("catalog tcpip node " + nodename + " remote " + hostname + " server " + portnum).getBytes());
                        if (hosttype.equals("MVS"))
                        {
                            out.write(("\n").getBytes());
                        } else
                        {
                            out.write((" ostype " + hosttype + "\n").getBytes());
                        }
                        if (hosttype.equals("MVS") || hosttype.equals("OS400"))
                        {
                            out.write(("catalog database " + dbalias + " as " + dbalias + " at node " + nodename + " authentication dcs" + "\n").getBytes());
                            out.write(("catalog dcs db " + dbalias + "  as " + dbtarget + "\n").getBytes());
                        } else
                        {
                            out.write(("catalog database " + dbtarget + " as " + dbalias + " at node " + nodename + " \n").getBytes());
                        }
                        out.write(("catalog system odbc data source " + dbalias + "\n").getBytes());
                    } catch (Exception exc)
                    {
                        logger.debug("Could not start database add for " + dbalias);
                        return false;
                    }
                }
            }

            if (createRequired)
            {
                out.write(("terminate\nquit\nexit\n").getBytes());
                out.flush();
                out.close();
                try
                {
                    logger.debug("Adding database connections");
                    proc = Runtime.getRuntime().exec("db2cmd /c /i db2 -vf /db2create.clp ");
                    dumpStream(proc.getInputStream());
                    proc.waitFor();
                } catch (InterruptedException exc)
                {
                    logger.fatal(exc.toString());
                    return false;
                }
                changesMade = true;

            }
            commandFile.delete();

        } catch (IOException exc)
        {
            logger.fatal(exc.toString());
            return false;
        }

        if (changesMade == true)
        {
            logger.error("DB2 application updated.  Please reboot to allow changes to complete.");
            return true;
        } else
        {
            logger.debug("Database connections verified.\n");
        }

        return true;

    }
    
    public static String getString(ResultSet aResultSet, int index) throws SQLException
    {
        if (aResultSet == null)
            return "";

        String value = aResultSet.getString(index);
        if (value == null)
            value = "";
        
        return value.trim();

    }
    
    public static String sqlString(String data)
    {
        StringBuffer results = new StringBuffer(data);

        // check for single quotes
        int length = results.length();
        for (int i = 0; i < length; i++)
        {
            char ch = results.charAt(i);
            if (ch == '\'')
            {
                length++;
                results.insert(i, '\'');
                i++;
            }
        }
        return "'" + results.toString().trim() + "'";
    }
    
    public static java.sql.Date toSqlDate(String strDate) throws ParseException
    {
    	 
    	if (strDate!=null) {
    		Date dt = com.ibm.gil.util.RegionalDateConverter.parseDate("DB2",strDate); 
    		return new java.sql.Date(dt.getTime());
    	} else { 
    		return null;
    	}

    }
    
    
    public static java.sql.Date toSqlDate(Date date) throws ParseException
    {
    	 
    	if (date!=null) {

    		return new java.sql.Date(date.getTime());
    	} else { 
    		return null;
    	}

    }

    
    

	public static DB2 getInstance() {
		return instanceVariable;
	}
}