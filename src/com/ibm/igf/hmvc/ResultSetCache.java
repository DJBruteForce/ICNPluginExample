/*
 * Created on Aug 6, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ibm.igf.hmvc;

import java.sql.ResultSet;
import java.util.Enumeration;
import java.util.Hashtable;

import com.ibm.gil.util.RegionalBigDecimal;

/**
 * @author SteveBaber
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ResultSetCache extends Hashtable {
    private transient Enumeration enum1 = null;

    private transient String[] currentData = null;

    private java.lang.String indexKey = null;

    /**
     * ResultSet constructor comment.
     */
    public ResultSetCache()
    {
        super();
    }

    /**
     * ResultSet constructor comment.
     * 
     * @param initialCapacity
     *            int
     */
    public ResultSetCache(int initialCapacity)
    {
        super(initialCapacity);
    }

    /**
     * ResultSet constructor comment.
     * 
     * @param initialCapacity
     *            int
     * @param capacityIncrement
     *            int
     */
    public ResultSetCache(int initialCapacity, int capacityIncrement)
    {
        super(initialCapacity, capacityIncrement);
    }

    /**
     * ResultSet constructor comment.
     */
    public ResultSetCache(ResultSet results, String key, int keyColumn, int defaultIndicatorColumn, String defaultIndicator) throws java.sql.SQLException
    {
        super();
        loadData(results, keyColumn, defaultIndicatorColumn, defaultIndicator);
        setIndexKey(key);
    }
         
    /**
     * Insert the method's description here. Creation date: (6/8/00 3:20:54 PM)
     */
    public void beforeFirst()
    {
        enum1 = elements();
    }

    /**
     * Insert the method's description here. Creation date: (6/8/00 3:19:22 PM)
     * 
     * @return bigDecimal
     * @param fieldNum
     *            int
     */
    public RegionalBigDecimal getBigDecimal(int fieldNum)
    {
        fieldNum--;
        if ((fieldNum >= 0) && (fieldNum < currentData.length))
        {
            try
            {
                return (new RegionalBigDecimal(currentData[fieldNum]));
            } catch (NumberFormatException exc)
            {
                return RegionalBigDecimal.ZERO;
            }
        } else
        {
            return RegionalBigDecimal.ZERO;
        }

    }

    /**
     * Insert the method's description here. Creation date: (6/8/00 3:32:46 PM)
     * 
     * @return java.lang.String
     */
    public java.lang.String getIndexKey()
    {
        return indexKey;
    }

    /**
     * Insert the method's description here. Creation date: (6/8/00 3:19:22 PM)
     * 
     * @return bigDecimal
     * @param fieldNum
     *            int
     */
    public RegionalBigDecimal getRegionalBigDecimal(int fieldNum)
    {
        fieldNum--;
        if ((fieldNum >= 0) && (fieldNum < currentData.length))
        {
            try
            {
                return (new RegionalBigDecimal(currentData[fieldNum], RegionalBigDecimal.PERIODDECIMALSEPARATOR));
            } catch (NumberFormatException exc)
            {
                return RegionalBigDecimal.ZERO;
            }
        } else
        {
            return RegionalBigDecimal.ZERO;
        }

    }

    /**
     * Insert the method's description here. Creation date: (6/8/00 3:14:42 PM)
     * 
     * @return java.lang.String
     * @param fieldNum
     *            int
     */
    public String getString(int fieldNum)
    {
        fieldNum--;
        if ((fieldNum >= 0) && (fieldNum < currentData.length))
        {
            return (currentData[fieldNum]);
        } else
        {
            return "";
        }
    }

    /**
     * Insert the method's description here. Creation date: (6/8/00 3:04:12 PM)
     */
    public void loadData(ResultSet results, int keyColumn, int defaultIndicatorColumn, String defaultIndicator) throws java.sql.SQLException
    {

        clear();
        if (results == null)
        {
            return;
        }

        int columnCount = results.getMetaData().getColumnCount();
        String field = null;
        // run through the result set and store the data in the vector

        while (results.next())
        {
            String[] data = new String[columnCount];
            // load all of the columns into the string array
            for (int i = 0; i < columnCount; i++)
            {
                field = results.getString(i + 1);
                if (field == null)
                    field = "";
                else
                    field = field.trim();
                data[i] = field;
            }
            // check to see if the entry exists            
            if (containsKey(data[keyColumn]))
            {
                // check to make sure we don't throw away the default record
                String[] existingEntry = (String[]) get(data[keyColumn]);
                if (existingEntry[defaultIndicatorColumn].equals(defaultIndicator))
                {
                    data[defaultIndicatorColumn] = defaultIndicator;
                }
            }           
            put(data[keyColumn], data);
        }
        beforeFirst();
    }
    

    /**
     * Insert the method's description here. Creation date: (6/8/00 3:10:54 PM)
     * 
     * @return boolean
     */
    public boolean next()
    {
    	if (enum1 == null)
    	{
    		beforeFirst();
    	}
        if (enum1.hasMoreElements())
        {
            currentData = (String[]) enum1.nextElement();
            return true;
        } else
            return false;
    }

    /**
     * Insert the method's description here. Creation date: (6/8/00 3:32:46 PM)
     * 
     * @param newIndexKey
     *            java.lang.String
     */
    public void setIndexKey(java.lang.String newIndexKey)
    {
        indexKey = newIndexKey;
    }

}