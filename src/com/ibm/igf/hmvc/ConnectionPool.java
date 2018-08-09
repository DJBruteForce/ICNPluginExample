package com.ibm.igf.hmvc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

public class ConnectionPool {
	
	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(ConnectionPool.class);
			
    protected static final String quotes = "'";

    protected static final String blank = "' '";

    protected static final Hashtable connectionPool = new Hashtable();

    protected static final Hashtable connectionPoolTimer = new Hashtable();

    public static final int OK = 0;

    public static final int DBERROR = 1;

    public static final int INSERTERROR = 2;

    public static final int UPDATEERROR = 3;

    public static final int DELETEERROR = 4;

    public static final int NOTFOUND = 100;

    public static final int DEFAULTTIMEOUT = 3600;

    private static StatementProgressThread sqlMonitor = null;

    private static boolean debugFlag = false;

    /**
     * Model constructor comment.
     */
    public ConnectionPool()
    {
        super();
    }

    /**
     * Insert the method's description here. Creation date: (3/14/2001 2:47:56
     * PM)
     * 
     * @return boolean
     */
    public static void closeDBConnections()
    {
    	logger.debug("closing db connections");

        Connection connection = null;

        for (Enumeration e = connectionPool.elements(); e.hasMoreElements();)
        {
            connection = (Connection) e.nextElement();

            try
            {
                if ((connection != null) && (!connection.isClosed()))
                {
                    if (connection.getAutoCommit() == false)
                    {
                        connection.rollback();
                    }

                    connection.close();
                }
            } catch (Throwable exc)
            {
                //fatal(exc.toString());
            } finally
            {
                connection = null;
            }
        }

        connectionPool.clear();
    }

    public static int[] executeBatch(Connection dbconnection, int queryTimeout, ArrayList batchStmtList) throws SQLException
    {
        int[] results = null;
        int[] finalresult = new int[batchStmtList.size()];
        StatementProgressThread aThread = null;
        try
        {
            // create a new statement
            Statement batchStmt = dbconnection.createStatement();

            // set the timeout
//            batchStmt.setQueryTimeout(queryTimeout);

            // start a progress monitor
            aThread = startProgressWindow(batchStmt, queryTimeout);

            // determine the start time
            java.util.Date startTime = new java.util.Date();
            logger.debug("Batch start = " + startTime);

            int j = 0;
            int batchByteCount = 0;
            int nresults = 0;
            String sqlCommand = null;
            for (int i = 0; i < batchStmtList.size(); i++)
            {
                if (batchStmtList.get(i) instanceof StringBuffer)
                    sqlCommand = ((StringBuffer) batchStmtList.get(i)).toString();
                else
                    sqlCommand = (String) batchStmtList.get(i);

                batchByteCount += sqlCommand.length();
                batchStmt.addBatch(sqlCommand);
                logger.debug(sqlCommand);
                // flush the batch
                if ((batchByteCount > 4096) || (j++ == 100) || (i + 1 == batchStmtList.size()))
                {
                    j = 0;
                    batchByteCount = 0;
                    // process the sql
                    results = batchStmt.executeBatch();

                    // append the results to the final results
                    for (int k = 0; k < results.length; k++)
                    {
                        finalresult[nresults++] = results[k];
                    }

                    // clean up the batch
                    batchStmt.clearBatch();
                }
            }

            logger.debug("Batch time = " + (new java.util.Date().getTime() - startTime.getTime()) + " ms");

            // clear the batchStmt variable
            // batchStmtList.clear();
            batchStmt.close();

        } catch (SQLException exc)
        {
        	logger.debug("Batch error " + exc.toString());
            if (exc.getErrorCode() != 0)
            {
                throw exc;
            }
        } finally
        {
            if (aThread != null)
                aThread.statementCompleted();
        }

        // return the results
        return (finalresult);

    }

    public static int[] executeBatch(Connection dbconnection, ArrayList batchStmtList) throws SQLException
    {
        return (executeBatch(dbconnection, DEFAULTTIMEOUT, batchStmtList));
    }

    public ResultSet executeQuery(Connection dbconnection, String sql) throws SQLException
    {
        return executeQuery(dbconnection, sql, DEFAULTTIMEOUT);
    }

    public static ResultSet executeQuery(Connection dbconnection, String sql, int queryTimeout) throws SQLException
    {
        ResultSet results = null;
        StatementProgressThread aThread = null;
        try
        {
            // prepare the statement to execute
            Statement stmt = dbconnection.createStatement();

            // set the timeout
            // stmt.setQueryTimeout(queryTimeout);

            // start a progress monitor
           // aThread = startProgressWindow(stmt, queryTimeout);

            // determine start time
            java.util.Date startTime = new java.util.Date();
           // Debugger.debug("Query start = " + startTime + " SQL = " + sql);

            // perform the sql
            results = stmt.executeQuery(sql.toString());
           // Debugger.debug("Query time = " + (new java.util.Date().getTime() - startTime.getTime()) + " ms  SQL = " + sql);
        } catch (SQLException exc)
        {
            closeDBConnections();
            logger.fatal(exc.toString());
            throw (exc);
        } finally
        {
            if (aThread != null)
                aThread.statementCompleted();
        }

        // return the results
        return (results);

    }

    public static ResultSet executeQuery(Connection dbconnection, StringBuffer sql) throws SQLException
    {
        return executeQuery(dbconnection, sql.toString(), DEFAULTTIMEOUT);
    }

    public static int executeUpdate(Connection dbconnection, String sql) throws SQLException
    {
        return executeUpdate(dbconnection, sql, DEFAULTTIMEOUT);
    }

    public static int executeUpdate(Connection dbconnection, String sql, int queryTimeout) throws SQLException
    {
        int results = 0;
        StatementProgressThread aThread = null;
        try
        {
            // prepare the statement to execute
            Statement stmt = dbconnection.createStatement();

            // set the timeout
//            stmt.setQueryTimeout(queryTimeout);

            // start a progress monitor
           // aThread = startProgressWindow(stmt, queryTimeout);

            // determine the start time
            java.util.Date startTime = new java.util.Date();
            logger.debug("Update start = " + startTime + " SQL = " + sql);

            // process the sql
            results = stmt.executeUpdate(sql);
            logger.debug("Update time = " + (new java.util.Date().getTime() - startTime.getTime()) + " ms  SQL = " + sql);
        } catch (SQLException exc)
        {
            if (exc.getErrorCode() != 0)
            {
                throw exc;
            }
        } finally
        {
            if (aThread != null)
                aThread.statementCompleted();
        }

        // return the results
        return (results);

    }

    public static int executeUpdate(Connection dbconnection, StringBuffer sql) throws SQLException
    {
        return executeUpdate(dbconnection, sql.toString(), DEFAULTTIMEOUT);
    }

    /**
     * Insert the method's description here. Creation date: (6/5/2001 7:59:21
     * AM)
     * 
     * @return java.lang.String
     * @param aResultSet
     *            java.sql.ResultSet
     * @param index
     *            int
     */
    public static String getString(ResultSet aResultSet, int index) throws SQLException
    {
        if (aResultSet == null)
            return "";

        String value = aResultSet.getString(index);
        if (value == null)
            value = "";

        return value.trim();
    }

    public static String getURL()
    {
        return "";
    }

    /**
     * Insert the method's description here. Creation date: (1/18/2002 11:06:46
     * AM)
     * 
     * @return boolean
     */
    public static boolean isDebugFlag()
    {
        return debugFlag;
    }

    public static void loadDriver() throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
    }

    public static void saveToDataCache(Hashtable dataCache, String key, Object value)
    {
        dataCache.put(key, value);
    }

    public static Object searchDataCache(Hashtable dataCache, String key)
    {
        if (dataCache == null)
        {
            return null;
        }
        Object value = dataCache.get(key);
        return value;
    }

    /**
     * Insert the method's description here. Creation date: (1/18/2002 11:06:46
     * AM)
     * 
     * @param newDebugFlag
     *            boolean
     */
    public static void setDebugFlag(boolean newDebugFlag)
    {
        debugFlag = newDebugFlag;
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

    public static String sqlString(String data, int maxLength)
    {
        if (data.length() > maxLength)
        {
            data = data.substring(0, maxLength);
        }

        return sqlString(data);
    }

    /**
     * Insert the method's description here. Creation date: (7/12/2001 5:00:05
     * PM)
     * 
     * @param stmt
     *            java.sql.Statement
     */
    public static StatementProgressThread startProgressWindow(Statement statement, int timeout)
    {

        if (sqlMonitor == null)
        {
            sqlMonitor = new StatementProgressThread();
        }
        sqlMonitor.setStatement(statement);
        sqlMonitor.setTimeout(timeout);
        sqlMonitor.monitor();
        return sqlMonitor;
    }

    public static ResultSet executePreparedQuery(Connection dbconnection, PreparedStatement stmt, String sql, int queryTimeout) throws SQLException
    {
        ResultSet results = null;
        StatementProgressThread aThread = null;
        try
        {
            // determine start time
            java.util.Date startTime = new java.util.Date();
           // Debugger.debug("Query start = " + startTime + " SQL = " + sql);

            // perform the sql
            results = stmt.executeQuery();
          //  Debugger.debug("Query time = " + (new java.util.Date().getTime() - startTime.getTime()) + " ms  SQL = " + sql);
        } catch (SQLException exc)
        {
            closeDBConnections();
            logger.fatal(exc.toString());
            throw (exc);
        }
        // return the results
        return (results);

    }

    public static int executePreparedUpdate(Connection dbconnection, PreparedStatement stmt, String sql, int queryTimeout) throws SQLException
    {
        int results = 0;
        StatementProgressThread aThread = null;
        try
        {
            // set the timeout
//            stmt.setQueryTimeout(queryTimeout);

            // determine the start time
            java.util.Date startTime = new java.util.Date();
            logger.debug("Update start = " + startTime + " SQL = " + sql);

            // process the sql
            results = stmt.executeUpdate();
            logger.debug("Update time = " + (new java.util.Date().getTime() - startTime.getTime()) + " ms  SQL = " + sql);
        } catch (SQLException exc)
        {
            if (exc.getErrorCode() != 0)
            {
                throw exc;
            }
        }

        // return the results
        return (results);

    }
}