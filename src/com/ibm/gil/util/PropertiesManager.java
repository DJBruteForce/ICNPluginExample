package com.ibm.gil.util;

import java.util.Properties;

public class PropertiesManager {
	
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
	private static java.util.Properties propertiesManager = null;   
	
	/** The backend region. */
	private static int backendRegion = PRODUCTION;
	
	
    public static final String[] devRegionLiterals = { "Development", "Alternate Development", "Development Integration Test" };
    public static final int[] devRegions = {DEVELOPMENT, ALTERNATEDEVELOPMENT, USERTEST};

	public static java.util.Properties getPropertiesManager() {

		propertiesManager = new Properties();

		propertiesManager.setProperty("ACCESS_MISC_COUNTRIES",
						"AR,AT,AU,BE,BR,CA,CH,CL,CN,CO,CZ,DE,DK,ES,FI,FR,GB,HK,HR,HU,IE,IL,IN,IT,KR,MX,MY,NL,NO,NZ,PE,PH,PL,PT,SE,SG,SI,SK,TH,TR,TW,US,UY,VE,ZA");
		return propertiesManager;
	}

}
