/*
 * Created on Aug 24, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ibm.gil.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.TimeZone;





/**
 * @author SteveBaber
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class RegionalDateConverter {
	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(RegionalDateConverter.class);
    private static Hashtable dateFormatters = new Hashtable();
    private static Hashtable timeFormatters = new Hashtable();
    private static Hashtable timeStampFormatters = new Hashtable();

    private static transient final int FORMAT = 0;
    private static transient final int PATTERN = 1;
    private static transient final int BLANK = 2;
    private static transient final int DEFAULT = 3;
    private static transient final int NUMSETTINGS = 4;

    private static void addFormat(Hashtable formatters, String key, String format, String pattern, String blankValue, String defaultValue)
    {
        Object[] settings = new Object[NUMSETTINGS];
        SimpleDateFormat dateformatter =new SimpleDateFormat(format); 
        dateformatter.setCalendar(new GregorianCalendar());

        settings[FORMAT] = dateformatter;
        settings[PATTERN] = pattern;
        settings[BLANK] = blankValue;
        settings[DEFAULT] = defaultValue;

        formatters.put(key, settings);
       
    }

    public static void addDateFormatter(String key, String format, String pattern, String blankValue, String defaultValue)
    {
    
       if (!dateFormatters.containsKey(key)){
    	addFormat(dateFormatters, key, format, pattern, blankValue, defaultValue);
       
       }
    }

    public static void addTimeFormatter(String key, String format, String pattern, String blankValue, String defaultValue)
    {
        addFormat(timeFormatters, key, format, pattern, blankValue, defaultValue);
    }

    public static void addTimeStampFormatter(String key, String format, String pattern, String blankValue, String defaultValue)
    {
        addFormat(timeStampFormatters, key, format, pattern, blankValue, defaultValue);
    }

    private static String convert(Object[] fromSettings, Object[] toSettings, String fromValue, TimeZone tz) throws IllegalArgumentException, ParseException
    {
        if ((fromSettings == null) || (toSettings == null))
        {
            throw new IllegalArgumentException("Invalid key specified");
        }

        SimpleDateFormat fromFormat = (SimpleDateFormat) fromSettings[FORMAT];
        fromFormat.setTimeZone(tz);
        SimpleDateFormat toFormat = (SimpleDateFormat) toSettings[FORMAT];
        toFormat.setTimeZone(tz);

        if (fromValue == null)
        {
            return (String) fromSettings[BLANK];
        }

        String results = null;
        Date date = null;
        if ((fromValue.trim().length() == 0) || (fromValue.equals((String) fromSettings[BLANK])))
        {
            return (String) toSettings[BLANK];
        } else
        {
            date = fromFormat.parse(fromValue);
            results = toFormat.format(date);
            return results;
        }

    }

    public static String convertDate(String fromKey, String toKey, String fromValue) throws IllegalArgumentException, ParseException
    {
        Object[] fromSettings = new Object[NUMSETTINGS];
        Object[] toSettings = new Object[NUMSETTINGS];
        
      

        fromSettings = (Object[]) dateFormatters.get(fromKey);
        toSettings = (Object[]) dateFormatters.get(toKey);

        //if (fromValue.equals((String) fromSettings[BLANK]))
        if (fromValue==null) 	
        {
            return (String) toSettings[BLANK];
        }
        
        return convert(fromSettings, toSettings, fromValue, TimeZone.getDefault());
    }
    
    public static String convertDate(String fromKey, String toKey, String fromValue, TimeZone timezone) throws IllegalArgumentException, ParseException
    {
        Object[] fromSettings = new Object[NUMSETTINGS];
        Object[] toSettings = new Object[NUMSETTINGS];

        fromSettings = (Object[]) dateFormatters.get(fromKey);
        toSettings = (Object[]) dateFormatters.get(toKey);
        
        // if (fromValue.equals((String) fromSettings[BLANK]))
        if (fromValue==null) 	
        {
            return (String) toSettings[BLANK];
        }
        return convert(fromSettings, toSettings, fromValue, timezone);
    }

    public static String convertTime(String fromKey, String toKey, String fromValue) throws IllegalArgumentException, ParseException
    {
        Object[] fromSettings = null;
        Object[] toSettings = null;

        fromSettings = (Object[]) timeFormatters.get(fromKey);
        toSettings = (Object[]) timeFormatters.get(toKey);

        return convert(fromSettings, toSettings, fromValue, TimeZone.getDefault());

    }

    public static String convertTimeStamp(String fromKey, String toKey, String fromValue) throws IllegalArgumentException, ParseException
    {
        Object[] fromSettings = null;
        Object[] toSettings = null;

        fromSettings = (Object[]) timeStampFormatters.get(fromKey);
        toSettings = (Object[]) timeStampFormatters.get(toKey);

        return convert(fromSettings, toSettings, fromValue, TimeZone.getDefault());

    }

    private static String getValue(Hashtable formatters, String key, int value)
    {
        Object[] settings = (Object[]) formatters.get(key);

        if (settings == null)
        {
            throw new IllegalArgumentException("Invalid key specified");
        }

        return (String) settings[value];
    }

    public static String getDefaultDate(String key)
    {
        return getValue(dateFormatters, key, DEFAULT);
    }

    public static String getBlankDate(String key)
    {
        return getValue(dateFormatters, key, BLANK);
    }

    public static String getPatternDate(String key)
    {
        return getValue(dateFormatters, key, PATTERN);
    }

    public static String getDefaultTime(String key)
    {
        return getValue(timeFormatters, key, DEFAULT);
    }

    public static String getBlankTime(String key)
    {
        return getValue(timeFormatters, key, BLANK);
    }

    public static String getPatternTime(String key)
    {
        return getValue(timeFormatters, key, PATTERN);
    }

    public static String getDefaultTimeStamp(String key)
    {
        return getValue(timeStampFormatters, key, DEFAULT);
    }

    public static String getBlankTimeStamp(String key)
    {
        return getValue(timeStampFormatters, key, BLANK);
    }

    public static String getPatternTimeStamp(String key)
    {
        return getValue(timeStampFormatters, key, PATTERN);
    }

    private static boolean isValueValid(Hashtable formatters, String key, String valueToCheck)
    {
    	return isValueValid (formatters, key, valueToCheck, TimeZone.getDefault());
    }
    
    private static boolean isValueValid(Hashtable formatters, String key, String valueToCheck, TimeZone tz)
    {
        Object[] settings = (Object[]) formatters.get(key);

        if (settings == null)
        {
            throw new IllegalArgumentException("Invalid key specified");
        }

        SimpleDateFormat format = (SimpleDateFormat) settings[FORMAT];
        format.setTimeZone(tz);
        
        if (valueToCheck == null)
            return false;

        if (valueToCheck.equals(settings[BLANK]))
            return true;

        Date checkDate = null;
        try
        {
            checkDate = format.parse(valueToCheck);
        } catch (ParseException exc)
        {
        }
        if (checkDate == null)
            return false;
        else
            return true;
    }

    public static boolean isDateValid(String key, String dateToCheck)
    {
        return isValueValid(dateFormatters, key, dateToCheck);
    }

    public static boolean isTimeValid(String key, String dateToCheck)
    {
        return isValueValid(timeFormatters, key, dateToCheck);
    }

    public static boolean isTimeStampValid(String key, String dateToCheck)
    {
        return isValueValid(timeStampFormatters, key, dateToCheck);
    }

    private static String format(Hashtable formatters, String key, Date valueToFormat, TimeZone tz)
    {
    	
        Object[] settings = (Object[]) formatters.get(key);
       
        
        if (settings == null)
        {
            throw new IllegalArgumentException("Invalid key specified");
        }

        SimpleDateFormat format = (SimpleDateFormat) settings[FORMAT];
        format.setTimeZone(tz);

        if (valueToFormat == null)
            return (String) settings[BLANK];

        return format.format(valueToFormat);
    }

    private static Date parse(Hashtable formatters, String key, String valueToParse) throws ParseException
    {
    	return parse (formatters, key, valueToParse, TimeZone.getDefault());
    }
    
    private static Date parse(Hashtable formatters, String key, String valueToParse, TimeZone tz) throws ParseException
    {
        Object[] settings = (Object[]) formatters.get(key);

        if (settings == null)
        {
            throw new IllegalArgumentException("Invalid key specified");
        }

        SimpleDateFormat format = (SimpleDateFormat) settings[FORMAT];
        format.setTimeZone(tz);
        
        if (valueToParse == null)
            return null;

        return format.parse(valueToParse);
    }

    public static String formatDate(String key, Date aDate)
    {
        return format(dateFormatters, key, aDate, TimeZone.getDefault());
    }

    public static String formatTime(String key, Date aDate)
    {
    	 return format(timeFormatters, key, aDate, TimeZone.getDefault());
    }

    public static String formatTimeStamp(String key, Date aDate)
    {
        return format(timeStampFormatters, key, aDate, TimeZone.getDefault());
    }

    public static String formatDate(String key, Date aDate, TimeZone tz)
    {
        return format(dateFormatters, key, aDate, tz);
    }

    public static String formatTime(String key, Date aDate, TimeZone tz)
    {   	
    	     	
        return format(timeFormatters, key, aDate, tz);
    }

    public static String formatTimeStamp(String key, Date aDate, TimeZone tz)
    {
        return format(timeStampFormatters, key, aDate, tz);
    }

    public static Date parseDate(String key, String aDate) throws ParseException
    {
        return parse(dateFormatters, key, aDate);
    }

    public static Date parseTime(String key, String aDate) throws ParseException
    {
        return parse(timeFormatters, key, aDate);
    }

    public static Date parseTimeStamp(String key, String aDate) throws ParseException
    {
        return parse(timeStampFormatters, key, aDate);
    }
    
    public static void main(String args[]){
    	try {
			System.out.println(new SimpleDateFormat("yyyy-MM-dd").parse("2012-09-11"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}
