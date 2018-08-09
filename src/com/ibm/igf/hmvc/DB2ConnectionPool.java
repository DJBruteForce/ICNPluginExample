package com.ibm.igf.hmvc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Insert the type's description here. Creation date: (11/29/2001 11:20:04 AM)
 * 
 * @author: Steve Baber
 */
public class DB2ConnectionPool extends ConnectionPool {
	
	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(DB2ConnectionPool.class);
	
    protected static Thread inactivityMonitor = null;

    protected static int DEFAULTIDLEWAIT = 10 * 60 * 1000;

    protected static int DEFAULTIDLEOUT = 20 * 60 * 1000;

    /**
     * DB2ConnectionPool constructor comment.
     */
    public DB2ConnectionPool()
    {
        super();
    }

    public synchronized static int checkForInactiveConnections()
    {
        java.util.Date connectionTimer = null;
        Connection connection = null;
        String key = null;
        int nextIdleOut = DEFAULTIDLEWAIT;

        //debug ("Checking for idle db2 connections at " + convertToDB2TS (new
        // java.util.Date()) + "\n");

        // list connections
        for (Enumeration enum1 = connectionPool.keys(); enum1.hasMoreElements();)
        {
            key = (String) enum1.nextElement();

            connection = (Connection) searchDataCache(connectionPool, key);
            connectionTimer = (java.util.Date) searchDataCache(connectionPoolTimer, key);

            // see if this connection has timed out
            if (connectionTimer != null)
            {

                java.util.Date now = new java.util.Date();
                //debug ("Checking connection for " + key + " idle for " +
                // ((now.getTime() - connectionTimer.getTime()) / 1000) + "
                // seconds\n");
                if ((now.getTime() - connectionTimer.getTime()) > DEFAULTIDLEOUT)
                {
                    // debug("Recycling timed out connection for " + key +
                    // "\n");
                    try
                    {
                        if (connection != null)
                            connection.close();
                    } catch (Throwable exc)
                    {
                    }

                    // clean out the connection
                    connectionPool.remove(key);
                    connectionPoolTimer.remove(key);
                } else
                {
                    // see if this is the next idle out
                    if ((now.getTime() - connectionTimer.getTime()) < nextIdleOut)
                    {
                        nextIdleOut = (int) (now.getTime() - connectionTimer.getTime());
                    }
                }
            }
        }

        return nextIdleOut;
    }

    /**
     * Insert the method's description here. Creation date: (10/19/2000 2:17:49
     * PM)
     * 
     * @return java.sql.Connection
     */
    public static java.sql.Connection getConnection(String dbname, String username, String password) throws SQLException
    {
        return getConnection(dbname, dbname, username, password);
    }

    /**
     * Insert the method's description here. Creation date: (10/19/2000 2:17:49
     * PM)
     * 
     * @return java.sql.Connection
     */
    public synchronized static java.sql.Connection getConnection(String key, String dbname, String username, String password) throws SQLException
    {
        Connection connection = null;
        java.util.Date connectionTimer = null;
        startConnectionInactivityMonitor();

        // see if the connection exists
        connection = (Connection) searchDataCache(connectionPool, key);
        connectionTimer = (java.util.Date) searchDataCache(connectionPoolTimer, key);

        // did it exist
        if ((connection == null) || (connection.isClosed()))
        {
            try
            {
                // create a new connection
                logger.info("connect to " + dbname + " user " + username);
                loadDriver();
                
                Properties props = new Properties();
                props.put("user", username);
                props.put("password", password);
                props.put("PATCH2", "15");
                props.put("DISABLEUNICODE","1");
               
                connection = DriverManager.getConnection(getURL() + dbname, props);
            } catch (Exception exc)
            {
                logger.fatal(exc.toString());
            }
        }

        // did it connect
        if (connection == null)
        {
            logger.fatal("Error connecting to database " + getURL() + dbname + "  " + username + "  ");
            return null;
        }

        // save in the cache
        saveToDataCache(connectionPool, key, connection);
        saveToDataCache(connectionPoolTimer, key, new java.util.Date());

        return connection;
    }

    public static String getURL()
    {
        return "jdbc:db2:";
    }

    public static void loadDriver() throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        boolean found = false;

        try
        {
            Class.forName("com.ibm.db2.jcc.DB2Driver").newInstance();
            found = true;
        } catch (ClassNotFoundException exc)
        {
        }
       
        if (!found)
        {
            throw new ClassNotFoundException("Could not load db2 driver");
        }
    }

    /**
     * Insert the method's description here. Creation date: (6/5/2003 1:26:04
     * PM)
     */
    public static void startConnectionInactivityMonitor()
    {
        if (inactivityMonitor == null)
        {
            inactivityMonitor = new Thread("DB2 Inactivity Monitor") {
                public void run()
                {
                    int timeout = DEFAULTIDLEWAIT;
                    while (true)
                    {
                        try
                        {
                            Thread.sleep(timeout);
                        } catch (InterruptedException exc)
                        {
                        }
                        timeout = checkForInactiveConnections();
                    }
                }
            };
            inactivityMonitor.start();
        }

    }
}