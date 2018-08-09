package com.ibm.gil.util;


import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;


import com.ibm.igf.hmvc.DB2;

public class GilUtility {

	public static String formatDate(String currentDate,String currentFormat, String newFormat) {
		String newDate="";
		
		SimpleDateFormat sdf = new SimpleDateFormat(currentFormat);
		SimpleDateFormat sdf2 = new SimpleDateFormat(newFormat);
				
	
			java.util.Date date;
			try {
				date = sdf.parse(currentDate);
				newDate=sdf2.format(date);
				
			} catch (ParseException e) {
				
				e.printStackTrace();
				newDate=null;
			}
			
		

		return newDate;
	}
	
	public static java.util.Date returnDate(String currentDate,String currentFormat, String newFormat) {
		java.util.Date newDate=null;
		
		SimpleDateFormat sdf = new SimpleDateFormat(currentFormat);
		SimpleDateFormat sdf2 = new SimpleDateFormat(newFormat);
				
	
//			java.util.Date date;
			try {
				newDate = sdf.parse(currentDate);
				newDate=sdf2.parse(sdf2.format(newDate));
				
			} catch (ParseException e) {
				
				e.printStackTrace();
				newDate=null;
			}
			
		

		return newDate;
	}
	/**
     * Insert the method's description here. Creation date: (3/12/2001 12:21:44
     * PM)
     * 
     * @return RegionalBigDecimal
     * @param index
     *            int
     */
    public static RegionalBigDecimal getDecimal(Object obj)
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
    public static int getInt(Object obj)
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
    public static String getString(Object obj )
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
    public static String getSqlString(Object obj)
    {

        if (obj == null)
            return DB2.sqlString("");
        else if (obj instanceof String)
            return DB2.sqlString((String) obj);
        else
            return DB2.sqlString(obj.toString());
    }
    
    public static Date getCreatedTimeStampAsDate(String createdTimeStamp)
    {
       if (createdTimeStamp!=null) {
    	   
    		 Timestamp timestamp = Timestamp.valueOf(createdTimeStamp);
    		 return new Date(timestamp.getTime());
       } else {
    	   return null;
       }
    }
   
    /**
     * Checks if an index class (aka item type) has a GIL GUI.
     * @param indexClass - a string containing the item type
     * @return true if the given item type has a GIL GUI, false otherwise.
     */
    public static boolean hasGILGUI(String indexClass) {
		if(
				(indexClass.startsWith("Invoice") && indexClass.length() == 10) ||
				(indexClass.startsWith("Misc Invoice") && indexClass.length() == indexClass.length()) ||
				(indexClass.startsWith("Contract") && indexClass.length() == 11) ||
				(indexClass.startsWith("Offering Letter") && indexClass.length() == 18) ||
				(indexClass.endsWith("SignCOA") && indexClass.length() == 9) ||
				(indexClass.endsWith("StampdutyOL") && indexClass.length() == 13) ||
				(indexClass.endsWith("CountersignOL") && indexClass.length() == 15) ||
				(indexClass.endsWith("SignOL") && indexClass.length() == 8) ||
				(indexClass.equals("ROF Signed Offer Letter")) ||
				(indexClass.endsWith("Invoice") && indexClass.length() == 9) ||
				(indexClass.endsWith("Misc") && indexClass.length() == 6)){
			return true;
		}
		return false;
	}
}
