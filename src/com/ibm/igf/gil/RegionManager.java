/*
 * Created on Jul 20, 2010
 *
 *=========================================================================== * 
 *
 * IBM Confidential  
 * (C) Copyright IBM Corp., 2010. 
 * The source code for this program is not published or otherwise divested of 
 * its trade secrets, irrespective of what has been deposited with the U.S. 
 * Copyright office. 
 *=========================================================================== 
 * Module Information: 
 * Created By  : Maria Casino
 * Project     : GILClient
 * Description : RegionManager
 *=========================================================================== */
package com.ibm.igf.gil;

import java.util.Properties;

/**
 * The Class RegionManager.
 */
public class RegionManager {

	// keep track of which region we are connecting to
	/** The Constant DEVELOPMENT. */
	public static final transient int DEVELOPMENT = 0;

	/** The Constant ALTERNATEDEVELOPMENT. */
	public static final transient int ALTERNATEDEVELOPMENT = 1;

	/** The Constant USERTEST. */
	public static final transient int USERTEST = 2;

	/** The Constant CUSTOMERTEST. */
	public static final transient int CUSTOMERTEST = 3;

	/** The Constant ALTERNATESYSTEMTEST. */
	public static final transient int ALTERNATESYSTEMTEST = 4;

	/** The Constant SYSTEMTEST. */
	public static final transient int SYSTEMTEST = 5;

	/** The Constant PREPRODUCTIONSUPPORT. */
	public static final transient int PREPRODUCTIONSUPPORT = 6;

	/** The Constant PRODUCTIONSUPPORT. */
	public static final transient int PRODUCTIONSUPPORT = 7;

	/** The Constant PRODUCTION. */
	public static final transient int PRODUCTION = 8;

	/** The Constant Property Key */
	public static final String CASCADE = "%PROPKEY%";
	
	/** The constant country code key */
	public static final String COUNTRY = "%COUNTRY%";
	
	/** The properties manager. */
	private static java.util.Properties propertiesManager = new Properties();
	
	/** The backend region. */
	private static int backendRegion = PRODUCTION;

	/**
	 * Gets the backend region.
	 * 
	 * @return the backend region
	 */
	public static int getBackendRegion() {
		return backendRegion;
	}

	/**
	 * Cascade the regions so that any that aren't setup get defaulted.
	 * 
	 * @param region
	 *            the region
	 * @return next region in the cascade
	 */
	public static int getNextCascadingBackendRegion(int region) {

		int nextBackendRegion = region - 1;
		if (nextBackendRegion < DEVELOPMENT)
		{
			nextBackendRegion = DEVELOPMENT;
		}

		return nextBackendRegion;
	}

	/**
	 * Gets the properties manager.
	 * 
	 * @return the properties manager
	 * @throws NullPointerException
	 *             the null pointer exception
	 */
	private static Properties getPropertiesManager() throws NullPointerException {
		if (propertiesManager == null) {
			throw new NullPointerException("Properties manager not initialized");
		}
		return propertiesManager;
	}

	/**
	 * Gets the property.
	 * 
	 * @param propertyName
	 *            the property name
	 * @return the property
	 */
	public static String getProperty(String propertyName) {
		return getPropertiesManager().getProperty(propertyName);
	}

	/**
	 * Gets the property with cascading.
	 * 
	 * @param propertyName
	 *            the property name
	 * @return the property
	 */
	public static String getCascadingProperty(String propertyName) {
		int region = getBackendRegion();
		String propertyToLookFor = null;
		String regionPropertyKey = null;
		String property = null;
		while (property == null && region >= DEVELOPMENT)
		{
			regionPropertyKey = getBackendRegionPropertyKey(region);
			propertyToLookFor = propertyName.replace(CASCADE, regionPropertyKey);
			
			property = getPropertiesManager().getProperty(propertyToLookFor);
			
			region--;
			
		}
		return property;
		
	}

	/**
	 * Gets the property with cascading.
	 * 
	 * @param propertyName
	 *            the property name
	 * @return the property
	 */
	public static String getCascadingProperty(String country, String propertyName) {
		int region = getBackendRegion();
		String propertyToLookFor = null;
		String regionPropertyKey = null;
		String property = null;
		while (property == null && region >= DEVELOPMENT)
		{
			regionPropertyKey = getBackendRegionPropertyKey(region);
			propertyToLookFor = propertyName.replace(COUNTRY, country);
			propertyToLookFor = propertyToLookFor.replace(CASCADE, regionPropertyKey);
			
			property = getPropertiesManager().getProperty(propertyToLookFor);
			
			region--;
			
		}
		
		// look for non country setting
		if (property == null)
		{
			propertyToLookFor = propertyName.replace(COUNTRY + "_", "");
			property = getCascadingProperty(propertyToLookFor);
		}
		
		return property;
		
	}
	/**
	 * Gets the region key.
	 * 
	 * @param region
	 *            the region
	 * @return the region key
	 */
	public static String getBackendRegionPropertyKey(int region) {
		if (region == PRODUCTION) {
			return ("PROD");
		} else if (region == DEVELOPMENT) {
			return ("DEV");
		} else if (region == ALTERNATEDEVELOPMENT) {
			return ("ALTERNATEDEV");
		} else if (region == USERTEST) {
			return ("USERTEST");
		} else if (region == CUSTOMERTEST) {
			return ("CUSTTEST");
		} else if (region == PRODUCTIONSUPPORT) {
			return ("PRODUCTIONSUPPORT");
		} else if (region == PREPRODUCTIONSUPPORT) {
			return ("PREPRODUCTIONSUPPORT");
		} else if (region == SYSTEMTEST) {
			return ("SYSTEMTEST");
		} else if (region == ALTERNATESYSTEMTEST) {
			return ("ALTERNATESYSTEMTEST");
		}
		return "";
	}

	/**
	 * Sets the backend region.
	 * 
	 * @param newBackendRegion
	 *            the new backend region
	 */
	public static void setBackendRegion(int newBackendRegion) {
		backendRegion = newBackendRegion;
	}

	/**
	 * Sets the properties manager.
	 * 
	 * @param aPropertiesManager
	 *            the new properties manager
	 */
	public static void setPropertiesManager(Properties aPropertiesManager) {
		propertiesManager = aPropertiesManager;
	}

	/**
	 * Gets the backend region property key.
	 * 
	 * @return the backend region property key
	 */
	public static String getBackendRegionPropertyKey() {
		return getBackendRegionPropertyKey(getBackendRegion());
	}

}
