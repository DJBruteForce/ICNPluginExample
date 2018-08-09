/*
 * Created on Aug 6, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ibm.igf.gil;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
//import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
//import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;
import java.util.Properties;

//import javax.swing.SwingUtilities;

import com.ibm.igf.hmvc.Cryptography;
import com.ibm.igf.hmvc.RegionManager;

/**
 * @author SteveBaber
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class DefaultPropertyManager {
	
	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(DefaultPropertyManager.class);
	
	private final static String propertiesContents = "GIL";
	private static boolean initialized = false;

	private static java.util.Properties propertiesManager = null;
	private static final transient String overrideFileName = "giloverride.ini.cya";
	private static final transient String propertiesFileName = "gil.ini.cya";
	private static final transient String ftpPropertiesFileName = "gil_passwords.ini.cya";
	private static final String NBSP = "&nbsp";
	public static String ftpURL = null;
	private static final transient String[] apCountries = { "AU", "CN", "IN", "KR", "MY", "NZ", "PH", "SG", "TH", "TW" };
	private static final transient String[] usCountries = { "AR", "BR", "CL", "CO", "MX", "PE", "US", "UY", "VE" };

	public static java.util.Properties getPropertiesManager() {
		if (propertiesManager == null) {
			propertiesManager = new Properties();

			// title bar
			propertiesManager.setProperty("APPLICATIONTITLE", "GIL");
			propertiesManager.setProperty("FTPDATA", "qerty");

			// determine the lang and country
			String lang = "";
			String cntry = "";
			try {
				String localestring = System.getProperty("FRNLANG"); // "en_GB";
				if (localestring != null) {
					if (localestring.indexOf("_") >= 0) {
						int index = localestring.indexOf("_");
						lang = localestring.substring(0, index);
						cntry = localestring.substring(index + 1, localestring.length());
					} else {
						lang = localestring;
					}

					Locale.setDefault(new Locale(lang, cntry));
				} else {
					lang = Locale.getDefault().getLanguage();
					cntry = Locale.getDefault().getCountry();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

			//
			// load from the ini file
			//
			try {
				FileInputStream is = new FileInputStream(propertiesFileName);
				propertiesManager.load(decryptInputStream(is));
			} catch (Exception exc) {
				debug(exc.toString());
			}

			// ftp setup -- different areas for different countries
			String primaryServer = "ehngsa.ibm.com";
			String secondaryServer = "pokgsa.ibm.com";

			if (isAPServer(cntry)) {
				primaryServer = "jpngsa.ibm.com";
				secondaryServer = "ehngsa.ibm.com";
			}
			if (isUSServer(cntry)) {
				primaryServer = "pokgsa.ibm.com";
				secondaryServer = "ehngsa.ibm.com";
			}

			propertiesManager.setProperty("FTP_URL_0", "http://" + primaryServer + "/home/i/g/igfuser/web/public/update/gil/");
			propertiesManager.setProperty("FTP_URL_1", "http://" + secondaryServer + "/home/i/g/igfuser/web/public/update/gil/");

			//
			// check to see if this is a uat box
			//
			/*if (UpdateManager.isUAT()) {
				// append to the ftp urls
				propertiesManager.setProperty("FTP_URL_0", "http://" + primaryServer + "/home/i/g/igfuser/web/public/update/uat/gil/");
				propertiesManager.setProperty("FTP_URL_1", "http://" + secondaryServer + "/home/i/g/igfuser/web/public/update/uat/gil/");
			}*/

			//
			// set some default values
			//
			/*
			 * this is the order that the regions cascade 
			 * PRODUCTION
			 * ==================== 
			 * PRODUCTIONSUPPORT 
			 * ====================
			 * PREPRODUCTIONSUPPORT ALTERNATESYSTEMTEST SYSTEMTEST CUSTOMERTEST
			 * ==================== 
			 * USERTEST ALTERNATEDEVELOPMENT DEVELOPMENT
			 */
			propertiesManager.setProperty("PROD_CMSERVER", "IGFPROD");
			propertiesManager.setProperty("PRODUCTIONSUPPORT_CMSERVER", "IGFSIM3");
			propertiesManager.setProperty("CUSTTEST_CMSERVER", "IGFUAT");
			propertiesManager.setProperty("CUSTTEST2_CMSERVER", "UAT2TEST");
			propertiesManager.setProperty("DEV_CMSERVER", "IGFDEV3");

			propertiesManager.setProperty("PROD_RMISERVER", "//ibmctmp1.portsmouth.uk.ibm.com:6029/ContentManagerRMI/");
			propertiesManager.setProperty("PRODUCTIONSUPPORT_RMISERVER", "//igfgid1.pok.ibm.com:6030/ContentManagerRMI/");
			propertiesManager.setProperty("CUSTTEST_RMISERVER", "//igfgid1.pok.ibm.com:6029/ContentManagerRMI/");
			propertiesManager.setProperty("DEV_RMISERVER", "//gftwas604.sby.ibm.com:6028/ContentManagerRMI/");
			//story 1721238 GCMS migration to POK - re-point GCMS and CTS web service
			//propertiesManager.setProperty("PROD_WEBSERVER", "https://w3-03.ibm.com/financing/tools/gcps/services/");
			propertiesManager.setProperty("PROD_WEBSERVER", "https://w3-01.ibm.com/financing/tools/gcps/services/");
			//End story 1721238
			propertiesManager.setProperty("PRODUCTIONSUPPORT_WEBSERVER", "https://w3alpha-2.toronto.ca.ibm.com:443/financing/tools/gcpsmaint/services/");
			//propertiesManager.setProperty("PREPRODUCTIONSUPPORT_WEBSERVER", "https://w3-03preprod.ibm.com:443/financing/tools/gcps/services/");
			//Story 1285310 - commented out the above line and added direct URL for GCMS PreProduction Region
			propertiesManager.setProperty("PREPRODUCTIONSUPPORT_WEBSERVER", "https://g03aciwass001.ahe.boulder.ibm.com/financing/tools/gcps/services/");
			propertiesManager.setProperty("ALTERNATESYSTEMTEST_WEBSERVER", "https://w3beta-2.toronto.ca.ibm.com:443/financing/tools/gcpsprime/services/");
			propertiesManager.setProperty("SYSTEMTEST_WEBSERVER", "https://w3beta-2.toronto.ca.ibm.com:443/financing/tools/gcps/services/");
			propertiesManager.setProperty("CUSTTEST_WEBSERVER", "https://w3alpha-2.toronto.ca.ibm.com:443/financing/tools/gcpsmaint/services/");
			propertiesManager.setProperty("USERTEST_WEBSERVER", "https://c25a0569.toronto.ca.ibm.com/financing/tools/gcps/services/");
			//propertiesManager.setProperty("USERTEST_WEBSERVER", "https://9.109.47.31/financing/tools/gcps/services/");
			//propertiesManager.setProperty("USERTEST_WEBSERVER", "http://9.193.86.171:9083/financing/tools/gcps/services/");
			//propertiesManager.setProperty("USERTEST_WEBSERVER", "http://9.193.84.104:9092/financing/tools/gcps/services/");
			//Test new link
			
			 //Story 1695422 Update GCMS Web Service Authentication
			 propertiesManager.setProperty("ALTERNATEDEV_WEBSERVER", "https://w3alpha-2.toronto.ca.ibm.com/financing/tools/gcps/services/");
			 //propertiesManager.setProperty("ALTERNATEDEV_WEBSERVER", "https://9.23.253.24:443/financing/tools/gcps/services/");
			 //End Story 1695422 
			 
			 //propertiesManager.setProperty("ALTERNATEDEV_WEBSERVER", "https://w3alpha-2.toronto.ca.ibm.com/financing/tools/gcpsprime/services/");
			 //propertiesManager.setProperty("ALTERNATEDEV_WEBSERVER", "http://gfdgcms1.sby.ibm.com/financing/tools/gcpsprime/services/");
			 //propertiesManager.setProperty("ALTERNATEDEV_WEBSERVER", "http://9.66.183.31:9081/financing/tools/gcpsprime/services/ ");
			 // End new test
			propertiesManager.setProperty("DEV_WEBSERVER", "http://gfdgcms1.sby.ibm.com/financing/tools/gcps/services/");

			propertiesManager.setProperty("PROD_WEBSERVER_USERNAME", "teddyng@us.ibm.com");
			propertiesManager.setProperty("PRODUCTIONSUPPORT_WEBSERVER_USERNAME", "teddyng@us.ibm.com");
			propertiesManager.setProperty("PREPRODUCTIONSUPPORT_WEBSERVER_USERNAME", "teddyng@us.ibm.com");
			propertiesManager.setProperty("ALTERNATESYSTEMTEST_WEBSERVER_USERNAME", "teddyng@us.ibm.com");
			propertiesManager.setProperty("SYSTEMTEST_WEBSERVER_USERNAME", "teddyng@us.ibm.com");
			propertiesManager.setProperty("CUSTTEST_WEBSERVER_USERNAME", "teddyng@us.ibm.com");
			propertiesManager.setProperty("USERTEST_WEBSERVER_USERNAME", "teddyng@us.ibm.com");
			propertiesManager.setProperty("ALTERNATEDEV_WEBSERVER_USERNAME", "teddyng@us.ibm.com");
			propertiesManager.setProperty("DEV_WEBSERVER_USERNAME", "teddyng@us.ibm.com");

			propertiesManager.setProperty("PROD_WEBSERVER_PASSWORD", "apr05apr");
			propertiesManager.setProperty("PRODUCTIONSUPPORT_WEBSERVER_PASSWORD", "apr05apr");
			propertiesManager.setProperty("PREPRODUCTIONSUPPORT_WEBSERVER_PASSWORD", "apr05apr");
			propertiesManager.setProperty("ALTERNATESYSTEMTEST_WEBSERVER_PASSWORD", "apr05apr");
			propertiesManager.setProperty("SYSTEMTEST_WEBSERVER_PASSWORD", "apr05apr");
			propertiesManager.setProperty("CUSTTEST_WEBSERVER_PASSWORD", "apr05apr");
			propertiesManager.setProperty("USERTEST_WEBSERVER_PASSWORD", "apr05apr");
			propertiesManager.setProperty("ALTERNATEDEV_WEBSERVER_PASSWORD", "apr05apr");
			propertiesManager.setProperty("DEV_WEBSERVER_PASSWORD", "apr05apr");

			propertiesManager.setProperty("PROD_RDCURL", "https://ibmprdc1.portsmouth.uk.ibm.com/SRServiceHTTPRouter/services/SRServiceBSI");
			propertiesManager.setProperty("PRODUCTIONSUPPORT_RDCURL", "https://ibmprdc1.portsmouth.uk.ibm.com/SRServiceHTTPRouter/services/SRServiceBSI");
			propertiesManager.setProperty("CUSTTEST_RDCURL", "https://ibmrrdc2.portsmouth.uk.ibm.com/SRServiceHTTPRouter/services/SRServiceBSI");
			propertiesManager.setProperty("USERTEST_RDCURL", "https://ibmrrdc2.portsmouth.uk.ibm.com/SRServiceHTTPRouter/services/SRServiceBSI");
			propertiesManager.setProperty("DEV_RDCURL", "https://ibmrrdc2.portsmouth.uk.ibm.com/SRServiceHTTPRouter/services/SRServiceBSI");

			propertiesManager.setProperty("DEV_MY_IBM_COUNTRY_CODE", "778");
			propertiesManager.setProperty("DEV_SG_IBM_COUNTRY_CODE", "834");
			propertiesManager.setProperty("DEV_TH_IBM_COUNTRY_CODE", "856");
			propertiesManager.setProperty("DEV_PH_IBM_COUNTRY_CODE", "818");
			propertiesManager.setProperty("DEV_GB_IBM_COUNTRY_CODE", "866");
			propertiesManager.setProperty("DEV_IT_IBM_COUNTRY_CODE", "758");
			propertiesManager.setProperty("DEV_AU_IBM_COUNTRY_CODE", "616");
			propertiesManager.setProperty("DEV_NZ_IBM_COUNTRY_CODE", "796");
			propertiesManager.setProperty("DEV_BE_IBM_COUNTRY_CODE", "624");
			propertiesManager.setProperty("DEV_NL_IBM_COUNTRY_CODE", "788");
			propertiesManager.setProperty("DEV_BR_IBM_COUNTRY_CODE", "631");
			propertiesManager.setProperty("DEV_DE_IBM_COUNTRY_CODE", "724");
			propertiesManager.setProperty("DEV_US_IBM_COUNTRY_CODE", "896");
			propertiesManager.setProperty("DEV_UY_IBM_COUNTRY_CODE", "869");
			propertiesManager.setProperty("DEV_FR_IBM_COUNTRY_CODE", "706");

			propertiesManager.setProperty("DEV_MY_IBM_ENTERPRISE_NUMBER", "77801");
			propertiesManager.setProperty("DEV_SG_IBM_ENTERPRISE_NUMBER", "83401");
			propertiesManager.setProperty("DEV_TH_IBM_ENTERPRISE_NUMBER", "85601");
			propertiesManager.setProperty("DEV_PH_IBM_ENTERPRISE_NUMBER", "81801");
			propertiesManager.setProperty("DEV_GB_IBM_ENTERPRISE_NUMBER", "86600");
			propertiesManager.setProperty("DEV_IT_IBM_ENTERPRISE_NUMBER", "75800");
			propertiesManager.setProperty("DEV_AU_IBM_ENTERPRISE_NUMBER", "61601");
			propertiesManager.setProperty("DEV_NZ_IBM_ENTERPRISE_NUMBER", "79601");
			propertiesManager.setProperty("DEV_BE_IBM_ENTERPRISE_NUMBER", "62400");
			propertiesManager.setProperty("DEV_NL_IBM_ENTERPRISE_NUMBER", "78800");
			propertiesManager.setProperty("DEV_BR_IBM_ENTERPRISE_NUMBER", "63101");
			propertiesManager.setProperty("DEV_DE_IBM_ENTERPRISE_NUMBER", "72400");
			propertiesManager.setProperty("DEV_US_IBM_ENTERPRISE_NUMBER", "200");
			propertiesManager.setProperty("DEV_UY_IBM_ENTERPRISE_NUMBER", "86901");
			propertiesManager.setProperty("DEV_FR_IBM_ENTERPRISE_NUMBER", "70600");


			propertiesManager.setProperty("DEV_NZ_RDC_USE_ADDRESS_SEQ_NUM", "Y");
			propertiesManager.setProperty("DEV_AU_RDC_USE_ADDRESS_SEQ_NUM", "Y");
			propertiesManager.setProperty("DEV_MY_RDC_USE_ADDRESS_SEQ_NUM", "Y");
			propertiesManager.setProperty("DEV_PH_RDC_USE_ADDRESS_SEQ_NUM", "Y");
			propertiesManager.setProperty("DEV_SG_RDC_USE_ADDRESS_SEQ_NUM", "Y");
			propertiesManager.setProperty("DEV_TH_RDC_USE_ADDRESS_SEQ_NUM", "Y");

			propertiesManager.setProperty("DEV_NZ_RDC_ADDRESS_SEQ_NUM", "02");
			propertiesManager.setProperty("DEV_AU_RDC_ADDRESS_SEQ_NUM", "07");
			propertiesManager.setProperty("DEV_MY_RDC_ADDRESS_SEQ_NUM", "AA");
			propertiesManager.setProperty("DEV_PH_RDC_ADDRESS_SEQ_NUM", "AA");
			propertiesManager.setProperty("DEV_SG_RDC_ADDRESS_SEQ_NUM", "AA");
			propertiesManager.setProperty("DEV_TH_RDC_ADDRESS_SEQ_NUM", "AA");

			//
			// apts
			//
			propertiesManager.setProperty("APTS_PROD_REGION", "IRFS01");
			propertiesManager.setProperty("APTS_PROD_SCHEMA", "WAPT_PRD.");

			propertiesManager.setProperty("APTS_MY_PROD_REGION", "IRFS02");
			propertiesManager.setProperty("APTS_MY_PROD_SCHEMA", "WAPT_PRD.");

			propertiesManager.setProperty("APTS_SG_PROD_REGION", "IRFS02");
			propertiesManager.setProperty("APTS_SG_PROD_SCHEMA", "WAPT_PRD.");

			propertiesManager.setProperty("APTS_PH_PROD_REGION", "IRFS02");
			propertiesManager.setProperty("APTS_PH_PROD_SCHEMA", "WAPT_PRD.");

			propertiesManager.setProperty("APTS_TH_PROD_REGION", "IRFS02");
			propertiesManager.setProperty("APTS_TH_PROD_SCHEMA", "WAPT_PRD.");

			propertiesManager.setProperty("APTS_AU_PROD_REGION", "IRFS02");
			propertiesManager.setProperty("APTS_AU_PROD_SCHEMA", "WAPT_PRD.");

			propertiesManager.setProperty("APTS_NZ_PROD_REGION", "IRFS02");
			propertiesManager.setProperty("APTS_NZ_PROD_SCHEMA", "WAPT_PRD.");

			propertiesManager.setProperty("APTS_KR_PROD_REGION", "IRFS02");
			propertiesManager.setProperty("APTS_KR_PROD_SCHEMA", "WAPT_PRD.");

			propertiesManager.setProperty("APTS_CN_PROD_REGION", "IRFS02");
			propertiesManager.setProperty("APTS_CN_PROD_SCHEMA", "WAPT_PRD.");

			propertiesManager.setProperty("APTS_TW_PROD_REGION", "IRFS02");
			propertiesManager.setProperty("APTS_TW_PROD_SCHEMA", "WAPT_PRD.");

			propertiesManager.setProperty("APTS_HK_PROD_REGION", "IRFS02");
			propertiesManager.setProperty("APTS_HK_PROD_SCHEMA", "WAPT_PRD.");

			propertiesManager.setProperty("APTS_IL_PROD_REGION", "IRFS01");
			propertiesManager.setProperty("APTS_IL_PROD_SCHEMA", "WAPT_PRD.");

			propertiesManager.setProperty("APTS_IN_PROD_REGION", "IRFS02");
			propertiesManager.setProperty("APTS_IN_PROD_SCHEMA", "WAPT_PRD.");

			propertiesManager.setProperty("APTS_AR_PROD_REGION", "IRFS03");
			propertiesManager.setProperty("APTS_AR_PROD_SCHEMA", "WAPT_PRD.");

			propertiesManager.setProperty("APTS_CL_PROD_REGION", "IRFS03");
			propertiesManager.setProperty("APTS_CL_PROD_SCHEMA", "WAPT_PRD.");

			propertiesManager.setProperty("APTS_CO_PROD_REGION", "IRFS03");
			propertiesManager.setProperty("APTS_CO_PROD_SCHEMA", "WAPT_PRD.");

			propertiesManager.setProperty("APTS_PE_PROD_REGION", "IRFS03");
			propertiesManager.setProperty("APTS_PE_PROD_SCHEMA", "WAPT_PRD.");

			propertiesManager.setProperty("APTS_VE_PROD_REGION", "IRFS03");
			propertiesManager.setProperty("APTS_VE_PROD_SCHEMA", "WAPT_PRD.");

			propertiesManager.setProperty("APTS_BR_PROD_REGION", "IRFS03");
			propertiesManager.setProperty("APTS_BR_PROD_SCHEMA", "WAPT_PRD.");
			
			propertiesManager.setProperty("APTS_US_PROD_REGION", "IRFS03");
			propertiesManager.setProperty("APTS_US_PROD_SCHEMA", "WAPT_PRD.");
			
			propertiesManager.setProperty("APTS_UY_PROD_REGION", "IRFS03");
			propertiesManager.setProperty("APTS_UY_PROD_SCHEMA", "WAPT_PRD.");

			propertiesManager.setProperty("APTS_MX_PROD_REGION", "IRFS03");
			propertiesManager.setProperty("APTS_MX_PROD_SCHEMA", "WAPT_PRD.");
			
			propertiesManager.setProperty("APTS_CA_PROD_REGION", "IRFS03");
			propertiesManager.setProperty("APTS_CA_PROD_SCHEMA", "WAPT_PRD.");

			propertiesManager.setProperty("APTS_PRODUCTIONSUPPORT_REGION", "DEVAS400");
			propertiesManager.setProperty("APTS_PRODUCTIONSUPPORT_SCHEMA", "WAPT_PRD.");

			propertiesManager.setProperty("APTS_CUSTTEST_REGION", "DEVAS400");
			propertiesManager.setProperty("APTS_CUSTTEST_SCHEMA", "WAPT_UAT.");

			propertiesManager.setProperty("APTS_DEV_REGION", "DEVAS400");
			propertiesManager.setProperty("APTS_DEV_SCHEMA", "WAPT_INT.");
			
			// New MISC table qualifier
			//MISC Invoice NA:
			propertiesManager.setProperty("MISC_CA_PROD_REGION", "IRFS03");
			propertiesManager.setProperty("MISC_CA_PROD_SCHEMA", "GAMSCPRD.");

			propertiesManager.setProperty("MISC_US_PROD_REGION", "IRFS03");
			propertiesManager.setProperty("MISC_US_PROD_SCHEMA", "GAMSCPRD.");

			//MISC Invoice LA:
			propertiesManager.setProperty("MISC_AR_PROD_REGION", "IRFS03");
			propertiesManager.setProperty("MISC_AR_PROD_SCHEMA", "GAMSCPRD.");

			propertiesManager.setProperty("MISC_BR_PROD_REGION", "IRFS03");
			propertiesManager.setProperty("MISC_BR_PROD_SCHEMA", "GAMSCPRD.");

			propertiesManager.setProperty("MISC_CL_PROD_REGION", "IRFS03");
			propertiesManager.setProperty("MISC_CL_PROD_SCHEMA", "GAMSCPRD.");

			propertiesManager.setProperty("MISC_CO_PROD_REGION", "IRFS03");
			propertiesManager.setProperty("MISC_CO_PROD_SCHEMA", "GAMSCPRD.");

			propertiesManager.setProperty("MISC_MX_PROD_REGION", "IRFS03");
			propertiesManager.setProperty("MISC_MX_PROD_SCHEMA", "GAMSCPRD.");

			propertiesManager.setProperty("MISC_PE_PROD_REGION", "IRFS03");
			propertiesManager.setProperty("MISC_PE_PROD_SCHEMA", "GAMSCPRD.");

			propertiesManager.setProperty("MISC_UY_PROD_REGION", "IRFS03");
			propertiesManager.setProperty("MISC_UY_PROD_SCHEMA", "GAMSCPRD.");

			propertiesManager.setProperty("MISC_VE_PROD_REGION", "IRFS03");
			propertiesManager.setProperty("MISC_VE_PROD_SCHEMA", "GAMSCPRD.");

			//MISC Invoice AP:
			propertiesManager.setProperty("MISC_AU_PROD_REGION", "IRFS02");
			propertiesManager.setProperty("MISC_AU_PROD_SCHEMA", "GAMSCPRD.");

			propertiesManager.setProperty("MISC_CN_PROD_REGION", "IRFS02");
			propertiesManager.setProperty("MISC_CN_PROD_SCHEMA", "GAMSCPRD.");

			propertiesManager.setProperty("MISC_HK_PROD_REGION", "IRFS02");
			propertiesManager.setProperty("MISC_HK_PROD_SCHEMA", "GAMSCPRD.");

			propertiesManager.setProperty("MISC_IN_PROD_REGION", "IRFS02");
			propertiesManager.setProperty("MISC_IN_PROD_SCHEMA", "GAMSCPRD.");

			propertiesManager.setProperty("MISC_KR_PROD_REGION", "IRFS02");
			propertiesManager.setProperty("MISC_KR_PROD_SCHEMA", "GAMSCPRD.");

			propertiesManager.setProperty("MISC_MY_PROD_REGION", "IRFS02");
			propertiesManager.setProperty("MISC_MY_PROD_SCHEMA", "GAMSCPRD.");

			propertiesManager.setProperty("MISC_NZ_PROD_REGION", "IRFS02");
			propertiesManager.setProperty("MISC_NZ_PROD_SCHEMA", "GAMSCPRD.");

			propertiesManager.setProperty("MISC_PH_PROD_REGION", "IRFS02");
			propertiesManager.setProperty("MISC_PH_PROD_SCHEMA", "GAMSCPRD.");

			propertiesManager.setProperty("MISC_SG_PROD_REGION", "IRFS02");
			propertiesManager.setProperty("MISC_SG_PROD_SCHEMA", "GAMSCPRD.");			

			propertiesManager.setProperty("MISC_TH_PROD_REGION", "IRFS02");
			propertiesManager.setProperty("MISC_TH_PROD_SCHEMA", "GAMSCPRD.");

			propertiesManager.setProperty("MISC_TW_PROD_REGION", "IRFS02");
			propertiesManager.setProperty("MISC_TW_PROD_SCHEMA", "GAMSCPRD.");			

			//MISC Invoice EMEA:
			propertiesManager.setProperty("MISC_AT_PROD_REGION", "IRFS01");
			propertiesManager.setProperty("MISC_AT_PROD_SCHEMA", "GAMSCPRD.");

			propertiesManager.setProperty("MISC_BE_PROD_REGION", "IRFS01");
			propertiesManager.setProperty("MISC_BE_PROD_SCHEMA", "GAMSCPRD.");

			propertiesManager.setProperty("MISC_CH_PROD_REGION", "IRFS01");
			propertiesManager.setProperty("MISC_CH_PROD_SCHEMA", "GAMSCPRD.");

			propertiesManager.setProperty("MISC_CZ_PROD_REGION", "IRFS01");
			propertiesManager.setProperty("MISC_CZ_PROD_SCHEMA", "GAMSCPRD.");

			propertiesManager.setProperty("MISC_DE_PROD_REGION", "IRFS01");
			propertiesManager.setProperty("MISC_DE_PROD_SCHEMA", "GAMSCPRD.");

			propertiesManager.setProperty("MISC_DK_PROD_REGION", "IRFS01");
			propertiesManager.setProperty("MISC_DK_PROD_SCHEMA", "GAMSCPRD.");

			propertiesManager.setProperty("MISC_ES_PROD_REGION", "IRFS01");
			propertiesManager.setProperty("MISC_ES_PROD_SCHEMA", "GAMSCPRD.");

			propertiesManager.setProperty("MISC_FI_PROD_REGION", "IRFS01");
			propertiesManager.setProperty("MISC_FI_PROD_SCHEMA", "GAMSCPRD.");

			propertiesManager.setProperty("MISC_FR_PROD_REGION", "IRFS01");
			propertiesManager.setProperty("MISC_FR_PROD_SCHEMA", "GAMSCPRD.");

			propertiesManager.setProperty("MISC_GB_PROD_REGION", "IRFS01");
			propertiesManager.setProperty("MISC_GB_PROD_SCHEMA", "GAMSCPRD.");

			propertiesManager.setProperty("MISC_HR_PROD_REGION", "IRFS01");
			propertiesManager.setProperty("MISC_HR_PROD_SCHEMA", "GAMSCPRD.");

			propertiesManager.setProperty("MISC_HU_PROD_REGION", "IRFS01");
			propertiesManager.setProperty("MISC_HU_PROD_SCHEMA", "GAMSCPRD.");

			propertiesManager.setProperty("MISC_IE_PROD_REGION", "IRFS01");
			propertiesManager.setProperty("MISC_IE_PROD_SCHEMA", "GAMSCPRD.");

			propertiesManager.setProperty("MISC_IL_PROD_REGION", "IRFS01");
			propertiesManager.setProperty("MISC_IL_PROD_SCHEMA", "GAMSCPRD.");

			propertiesManager.setProperty("MISC_IT_PROD_REGION", "IRFS01");
			propertiesManager.setProperty("MISC_IT_PROD_SCHEMA", "GAMSCPRD.");

			propertiesManager.setProperty("MISC_NL_PROD_REGION", "IRFS01");
			propertiesManager.setProperty("MISC_NL_PROD_SCHEMA", "GAMSCPRD.");

			propertiesManager.setProperty("MISC_NO_PROD_REGION", "IRFS01");
			propertiesManager.setProperty("MISC_NO_PROD_SCHEMA", "GAMSCPRD.");

			propertiesManager.setProperty("MISC_PL_PROD_REGION", "IRFS01");
			propertiesManager.setProperty("MISC_PL_PROD_SCHEMA", "GAMSCPRD.");

			propertiesManager.setProperty("MISC_PT_PROD_REGION", "IRFS01");
			propertiesManager.setProperty("MISC_PT_PROD_SCHEMA", "GAMSCPRD.");

			propertiesManager.setProperty("MISC_SE_PROD_REGION", "IRFS01");
			propertiesManager.setProperty("MISC_SE_PROD_SCHEMA", "GAMSCPRD.");

			propertiesManager.setProperty("MISC_SI_PROD_REGION", "IRFS01");
			propertiesManager.setProperty("MISC_SI_PROD_SCHEMA", "GAMSCPRD.");

			propertiesManager.setProperty("MISC_SK_PROD_REGION", "IRFS01");
			propertiesManager.setProperty("MISC_SK_PROD_SCHEMA", "GAMSCPRD.");

			propertiesManager.setProperty("MISC_TR_PROD_REGION", "IRFS01");
			propertiesManager.setProperty("MISC_TR_PROD_SCHEMA", "GAMSCPRD.");

			propertiesManager.setProperty("MISC_ZA_PROD_REGION", "IRFS01");
			propertiesManager.setProperty("MISC_ZA_PROD_SCHEMA", "GAMSCPRD.");

			//MISC Default by region:
			propertiesManager.setProperty("MISC_PROD_REGION", "IRFS01");
			propertiesManager.setProperty("MISC_PROD_SCHEMA", "GAMSCPRD.");

			propertiesManager.setProperty("MISC_PRODUCTIONSUPPORT_REGION", "DEVAS400");
			propertiesManager.setProperty("MISC_PRODUCTIONSUPPORT_SCHEMA", "GAMSCPRD.");

			propertiesManager.setProperty("MISC_CUSTTEST_REGION", "DEVAS400");
			propertiesManager.setProperty("MISC_CUSTTEST_SCHEMA", "GAMSCUAT.");

			propertiesManager.setProperty("MISC_DEV_REGION", "DEVAS400");
			propertiesManager.setProperty("MISC_DEV_SCHEMA", "GAMSCINT.");	
			// End New MISC table qualifier
			
			
			//Story 1640938: GIL - changes needed for GCMS JR
			// New HYBR table qualifier
			//HYBR Invoice NA:
			propertiesManager.setProperty("HYBR_CA_PROD_REGION", "IRFS03");
			propertiesManager.setProperty("HYBR_CA_PROD_SCHEMA", "GCPSPRD.");

			propertiesManager.setProperty("HYBR_US_PROD_REGION", "IRFS03");
			propertiesManager.setProperty("HYBR_US_PROD_SCHEMA", "GCPSPRD.");

			//HYBR Invoice LA:
			propertiesManager.setProperty("HYBR_AR_PROD_REGION", "IRFS03");
			propertiesManager.setProperty("HYBR_AR_PROD_SCHEMA", "GCPSPRD.");

			propertiesManager.setProperty("HYBR_BR_PROD_REGION", "IRFS03");
			propertiesManager.setProperty("HYBR_BR_PROD_SCHEMA", "GCPSPRD.");

			propertiesManager.setProperty("HYBR_CL_PROD_REGION", "IRFS03");
			propertiesManager.setProperty("HYBR_CL_PROD_SCHEMA", "GCPSPRD.");

			propertiesManager.setProperty("HYBR_CO_PROD_REGION", "IRFS03");
			propertiesManager.setProperty("HYBR_CO_PROD_SCHEMA", "GCPSPRD.");

			propertiesManager.setProperty("HYBR_MX_PROD_REGION", "IRFS03");
			propertiesManager.setProperty("HYBR_MX_PROD_SCHEMA", "GCPSPRD.");

			propertiesManager.setProperty("HYBR_PE_PROD_REGION", "IRFS03");
			propertiesManager.setProperty("HYBR_PE_PROD_SCHEMA", "GCPSPRD.");

			propertiesManager.setProperty("HYBR_UY_PROD_REGION", "IRFS03");
			propertiesManager.setProperty("HYBR_UY_PROD_SCHEMA", "GCPSPRD.");

			propertiesManager.setProperty("HYBR_VE_PROD_REGION", "IRFS03");
			propertiesManager.setProperty("HYBR_VE_PROD_SCHEMA", "GCPSPRD.");

			//HYBR Invoice AP:
			propertiesManager.setProperty("HYBR_AU_PROD_REGION", "IRFS02");
			propertiesManager.setProperty("HYBR_AU_PROD_SCHEMA", "GCPSPRD.");

			propertiesManager.setProperty("HYBR_CN_PROD_REGION", "IRFS02");
			propertiesManager.setProperty("HYBR_CN_PROD_SCHEMA", "GCPSPRD.");

			propertiesManager.setProperty("HYBR_HK_PROD_REGION", "IRFS02");
			propertiesManager.setProperty("HYBR_HK_PROD_SCHEMA", "GCPSPRD.");

			propertiesManager.setProperty("HYBR_IN_PROD_REGION", "IRFS02");
			propertiesManager.setProperty("HYBR_IN_PROD_SCHEMA", "GCPSPRD.");

			propertiesManager.setProperty("HYBR_KR_PROD_REGION", "IRFS02");
			propertiesManager.setProperty("HYBR_KR_PROD_SCHEMA", "GCPSPRD.");

			propertiesManager.setProperty("HYBR_MY_PROD_REGION", "IRFS02");
			propertiesManager.setProperty("HYBR_MY_PROD_SCHEMA", "GCPSPRD.");

			propertiesManager.setProperty("HYBR_NZ_PROD_REGION", "IRFS02");
			propertiesManager.setProperty("HYBR_NZ_PROD_SCHEMA", "GCPSPRD.");

			propertiesManager.setProperty("HYBR_PH_PROD_REGION", "IRFS02");
			propertiesManager.setProperty("HYBR_PH_PROD_SCHEMA", "GCPSPRD.");

			propertiesManager.setProperty("HYBR_SG_PROD_REGION", "IRFS02");
			propertiesManager.setProperty("HYBR_SG_PROD_SCHEMA", "GCPSPRD.");			

			propertiesManager.setProperty("HYBR_TH_PROD_REGION", "IRFS02");
			propertiesManager.setProperty("HYBR_TH_PROD_SCHEMA", "GCPSPRD.");

			propertiesManager.setProperty("HYBR_TW_PROD_REGION", "IRFS02");
			propertiesManager.setProperty("HYBR_TW_PROD_SCHEMA", "GCPSPRD.");			

			//HYBR Invoice EMEA:
			propertiesManager.setProperty("HYBR_AT_PROD_REGION", "IRFS01");
			propertiesManager.setProperty("HYBR_AT_PROD_SCHEMA", "GCPSPRD.");

			propertiesManager.setProperty("HYBR_BE_PROD_REGION", "IRFS01");
			propertiesManager.setProperty("HYBR_BE_PROD_SCHEMA", "GCPSPRD.");

			propertiesManager.setProperty("HYBR_CH_PROD_REGION", "IRFS01");
			propertiesManager.setProperty("HYBR_CH_PROD_SCHEMA", "GCPSPRD.");

			propertiesManager.setProperty("HYBR_CZ_PROD_REGION", "IRFS01");
			propertiesManager.setProperty("HYBR_CZ_PROD_SCHEMA", "GCPSPRD.");

			propertiesManager.setProperty("HYBR_DE_PROD_REGION", "IRFS01");
			propertiesManager.setProperty("HYBR_DE_PROD_SCHEMA", "GCPSPRD.");

			propertiesManager.setProperty("HYBR_DK_PROD_REGION", "IRFS01");
			propertiesManager.setProperty("HYBR_DK_PROD_SCHEMA", "GCPSPRD.");

			propertiesManager.setProperty("HYBR_ES_PROD_REGION", "IRFS01");
			propertiesManager.setProperty("HYBR_ES_PROD_SCHEMA", "GCPSPRD.");

			propertiesManager.setProperty("HYBR_FI_PROD_REGION", "IRFS01");
			propertiesManager.setProperty("HYBR_FI_PROD_SCHEMA", "GCPSPRD.");

			propertiesManager.setProperty("HYBR_FR_PROD_REGION", "IRFS01");
			propertiesManager.setProperty("HYBR_FR_PROD_SCHEMA", "GCPSPRD.");

			propertiesManager.setProperty("HYBR_GB_PROD_REGION", "IRFS01");
			propertiesManager.setProperty("HYBR_GB_PROD_SCHEMA", "GCPSPRD.");

			propertiesManager.setProperty("HYBR_HR_PROD_REGION", "IRFS01");
			propertiesManager.setProperty("HYBR_HR_PROD_SCHEMA", "GCPSPRD.");

			propertiesManager.setProperty("HYBR_HU_PROD_REGION", "IRFS01");
			propertiesManager.setProperty("HYBR_HU_PROD_SCHEMA", "GCPSPRD.");

			propertiesManager.setProperty("HYBR_IE_PROD_REGION", "IRFS01");
			propertiesManager.setProperty("HYBR_IE_PROD_SCHEMA", "GCPSPRD.");

			propertiesManager.setProperty("HYBR_IL_PROD_REGION", "IRFS01");
			propertiesManager.setProperty("HYBR_IL_PROD_SCHEMA", "GCPSPRD.");

			propertiesManager.setProperty("HYBR_IT_PROD_REGION", "IRFS01");
			propertiesManager.setProperty("HYBR_IT_PROD_SCHEMA", "GCPSPRD.");

			propertiesManager.setProperty("HYBR_NL_PROD_REGION", "IRFS01");
			propertiesManager.setProperty("HYBR_NL_PROD_SCHEMA", "GCPSPRD.");

			propertiesManager.setProperty("HYBR_NO_PROD_REGION", "IRFS01");
			propertiesManager.setProperty("HYBR_NO_PROD_SCHEMA", "GCPSPRD.");

			propertiesManager.setProperty("HYBR_PL_PROD_REGION", "IRFS01");
			propertiesManager.setProperty("HYBR_PL_PROD_SCHEMA", "GCPSPRD.");

			propertiesManager.setProperty("HYBR_PT_PROD_REGION", "IRFS01");
			propertiesManager.setProperty("HYBR_PT_PROD_SCHEMA", "GCPSPRD.");

			propertiesManager.setProperty("HYBR_SE_PROD_REGION", "IRFS01");
			propertiesManager.setProperty("HYBR_SE_PROD_SCHEMA", "GCPSPRD.");

			propertiesManager.setProperty("HYBR_SI_PROD_REGION", "IRFS01");
			propertiesManager.setProperty("HYBR_SI_PROD_SCHEMA", "GCPSPRD.");

			propertiesManager.setProperty("HYBR_SK_PROD_REGION", "IRFS01");
			propertiesManager.setProperty("HYBR_SK_PROD_SCHEMA", "GCPSPRD.");

			propertiesManager.setProperty("HYBR_TR_PROD_REGION", "IRFS01");
			propertiesManager.setProperty("HYBR_TR_PROD_SCHEMA", "GCPSPRD.");

			propertiesManager.setProperty("HYBR_ZA_PROD_REGION", "IRFS01");
			propertiesManager.setProperty("HYBR_ZA_PROD_SCHEMA", "GCPSPRD.");

			//HYBR Default by region:
			propertiesManager.setProperty("HYBR_PROD_REGION", "IRFS01");
			propertiesManager.setProperty("HYBR_PROD_SCHEMA", "GCPSPRD.");

			propertiesManager.setProperty("HYBR_PRODUCTIONSUPPORT_REGION", "DEVAS400");
			propertiesManager.setProperty("HYBR_PRODUCTIONSUPPORT_SCHEMA", "GCPSPRD.");

			propertiesManager.setProperty("HYBR_CUSTTEST_REGION", "DEVAS400");
			propertiesManager.setProperty("HYBR_CUSTTEST_SCHEMA", "GCPSUAT.");

			propertiesManager.setProperty("HYBR_DEV_REGION", "DEVAS400");
			propertiesManager.setProperty("HYBR_DEV_SCHEMA", "GCPSINT.");	
			// End New HYBR table qualifier			
			//End Story 1640938

			propertiesManager.setProperty("GCMS_PROD_REGION", "IRFS01");
			propertiesManager.setProperty("GCMS_PROD_SCHEMA", "GCPSPRD.");

			propertiesManager.setProperty("GCMS_MY_PROD_REGION", "IRFS02");
			propertiesManager.setProperty("GCMS_MY_PROD_SCHEMA", "GCPSPRD.");

			propertiesManager.setProperty("GCMS_SG_PROD_REGION", "IRFS02");
			propertiesManager.setProperty("GCMS_SG_PROD_SCHEMA", "GCPSPRD.");

			propertiesManager.setProperty("GCMS_PH_PROD_REGION", "IRFS02");
			propertiesManager.setProperty("GCMS_PH_PROD_SCHEMA", "GCPSPRD.");

			propertiesManager.setProperty("GCMS_TH_PROD_REGION", "IRFS02");
			propertiesManager.setProperty("GCMS_TH_PROD_SCHEMA", "GCPSPRD.");
			
			propertiesManager.setProperty("GCMS_AU_PROD_REGION", "IRFS02");
			propertiesManager.setProperty("GCMS_AU_PROD_SCHEMA", "GCPSPRD.");

			propertiesManager.setProperty("GCMS_NZ_PROD_REGION", "IRFS02");
			propertiesManager.setProperty("GCMS_NZ_PROD_SCHEMA", "GCPSPRD.");
			
			propertiesManager.setProperty("GCMS_BE_PROD_REGION", "IRFS01");
			propertiesManager.setProperty("GCMS_BE_PROD_SCHEMA", "GCPSPRD.");

			propertiesManager.setProperty("GCMS_NL_PROD_REGION", "IRFS01");
			propertiesManager.setProperty("GCMS_NL_PROD_SCHEMA", "GCPSPRD.");
			
			propertiesManager.setProperty("GCMS_DE_PROD_REGION", "IRFS01");
			propertiesManager.setProperty("GCMS_DE_PROD_SCHEMA", "GCPSPRD.");
			
			propertiesManager.setProperty("GCMS_GB_PROD_REGION", "IRFS01");
			propertiesManager.setProperty("GCMS_GB_PROD_SCHEMA", "GCPSPRD.");
			
			propertiesManager.setProperty("GCMS_IT_PROD_REGION", "IRFS01");
			propertiesManager.setProperty("GCMS_IT_PROD_SCHEMA", "GCPSPRD.");
			
			propertiesManager.setProperty("GCMS_BR_PROD_REGION", "IRFS03");
			propertiesManager.setProperty("GCMS_BR_PROD_SCHEMA", "GCPSPRD.");
			
			propertiesManager.setProperty("GCMS_US_PROD_REGION", "IRFS03");
			propertiesManager.setProperty("GCMS_US_PROD_SCHEMA", "GCPSPRD.");	
			
			propertiesManager.setProperty("GCMS_UY_PROD_REGION", "IRFS03");
			propertiesManager.setProperty("GCMS_UY_PROD_SCHEMA", "GCPSPRD.");			

			propertiesManager.setProperty("GCMS_PRODUCTIONSUPPORT_REGION", "DEVAS400");
			propertiesManager.setProperty("GCMS_PRODUCTIONSUPPORT_SCHEMA", "GCPSPRD.");

			propertiesManager.setProperty("GCMS_CUSTTEST_REGION", "DEVAS400");
			propertiesManager.setProperty("GCMS_CUSTTEST_SCHEMA", "GCPSUAT.");

			propertiesManager.setProperty("GCMS_DEV_REGION", "DEVAS400");
			propertiesManager.setProperty("GCMS_DEV_SCHEMA", "GCPSINT.");

			//
			// dsnd
			//
			propertiesManager.setProperty("DSND_PROD_REGION", "DP1A");
			propertiesManager.setProperty("DSND_PROD_SCHEMA", "ICP1ADBA.");

			propertiesManager.setProperty("DSND_PRODUCTIONSUPPORT_REGION", "DP1A");
			propertiesManager.setProperty("DSND_PRODUCTIONSUPPORT_SCHEMA", "ICR3ADBA.");

			propertiesManager.setProperty("DSND_SYSTEMTEST_REGION", "DSND");
			propertiesManager.setProperty("DSND_SYSTEMTEST_SCHEMA", "ICU1IDBA.");

			propertiesManager.setProperty("DSND_CUSTTEST_REGION", "DP1A");
			propertiesManager.setProperty("DSND_CUSTTEST_SCHEMA", "ICR1ADBA.");

			propertiesManager.setProperty("DSND_DEV_REGION", "DSND");
			propertiesManager.setProperty("DSND_DEV_SCHEMA", "ICU1IDBA.");
			propertiesManager.setProperty("DSNDSP_DEV_SCHEMA", "ICT98DBA.");
						
			// country specific regions
			propertiesManager.setProperty("DSND_GB_PROD_REGION", "DP1E");
			propertiesManager.setProperty("DSND_GB_PROD_SCHEMA", "ICP1EDBA.");

			propertiesManager.setProperty("DSND_GB_PRODUCTIONSUPPORT_REGION", "DP1E");
			propertiesManager.setProperty("DSND_GB_PRODUCTIONSUPPORT_SCHEMA", "ICR3EDBA.");

			propertiesManager.setProperty("DSND_GB_CUSTTEST_REGION", "DP1E");
			propertiesManager.setProperty("DSND_GB_CUSTTEST_SCHEMA", "ICR1EDBA.");

			propertiesManager.setProperty("DSND_DE_PROD_REGION", "DP1E");
			propertiesManager.setProperty("DSND_DE_PROD_SCHEMA", "ICP1EDBA.");

			propertiesManager.setProperty("DSND_DE_PRODUCTIONSUPPORT_REGION", "DP1E");
			propertiesManager.setProperty("DSND_DE_PRODUCTIONSUPPORT_SCHEMA", "ICR3EDBA.");

			propertiesManager.setProperty("DSND_DE_CUSTTEST_REGION", "DP1E");
			propertiesManager.setProperty("DSND_DE_CUSTTEST_SCHEMA", "ICR1EDBA.");

			propertiesManager.setProperty("DSND_IT_PROD_REGION", "DP1E");
			propertiesManager.setProperty("DSND_IT_PROD_SCHEMA", "ICP1EDBA.");

			propertiesManager.setProperty("DSND_IL_PROD_REGION", "DP1E");	
			propertiesManager.setProperty("DSND_IL_PROD_SCHEMA", "ICP1EDBA.");

			propertiesManager.setProperty("DSND_IT_PRODUCTIONSUPPORT_REGION", "DP1E");
			propertiesManager.setProperty("DSND_IT_PRODUCTIONSUPPORT_SCHEMA", "ICR3EDBA.");

			propertiesManager.setProperty("DSND_IT_CUSTTEST_REGION", "DP1E");
			propertiesManager.setProperty("DSND_IT_CUSTTEST_SCHEMA", "ICR1EDBA.");

			propertiesManager.setProperty("DSND_FR_PROD_REGION", "DP1E");
			propertiesManager.setProperty("DSND_FR_PROD_SCHEMA", "ICP1EDBA.");

			propertiesManager.setProperty("DSND_FR_PRODUCTIONSUPPORT_REGION", "DP1E");
			propertiesManager.setProperty("DSND_FR_PRODUCTIONSUPPORT_SCHEMA", "ICR3EDBA.");

			propertiesManager.setProperty("DSND_FR_CUSTTEST_REGION", "DP1E");
			propertiesManager.setProperty("DSND_FR_CUSTTEST_SCHEMA", "ICR1EDBA.");

			propertiesManager.setProperty("DSND_MY_PROD_REGION", "DP1M");
			propertiesManager.setProperty("DSND_MY_PROD_SCHEMA", "ICP1MDBA.");

			propertiesManager.setProperty("DSND_MY_PRODUCTIONSUPPORT_REGION", "DP1M");
			propertiesManager.setProperty("DSND_MY_PRODUCTIONSUPPORT_SCHEMA", "ICR3MDBA.");

			propertiesManager.setProperty("DSND_MY_CUSTTEST_REGION", "DP1M");
			propertiesManager.setProperty("DSND_MY_CUSTTEST_SCHEMA", "ICR1MDBA.");

			propertiesManager.setProperty("DSND_SG_PROD_REGION", "DP1M");
			propertiesManager.setProperty("DSND_SG_PROD_SCHEMA", "ICP1MDBA.");

			propertiesManager.setProperty("DSND_SG_PRODUCTIONSUPPORT_REGION", "DP1M");
			propertiesManager.setProperty("DSND_SG_PRODUCTIONSUPPORT_SCHEMA", "ICR3MDBA.");

			propertiesManager.setProperty("DSND_SG_CUSTTEST_REGION", "DP1M");
			propertiesManager.setProperty("DSND_SG_CUSTTEST_SCHEMA", "ICR1MDBA.");

			propertiesManager.setProperty("DSND_PH_PROD_REGION", "DP1M");
			propertiesManager.setProperty("DSND_PH_PROD_SCHEMA", "ICP1MDBA.");

			propertiesManager.setProperty("DSND_PH_PRODUCTIONSUPPORT_REGION", "DP1M");
			propertiesManager.setProperty("DSND_PH_PRODUCTIONSUPPORT_SCHEMA", "ICR3MDBA.");

			propertiesManager.setProperty("DSND_PH_CUSTTEST_REGION", "DP1M");
			propertiesManager.setProperty("DSND_PH_CUSTTEST_SCHEMA", "ICR1MDBA.");

			propertiesManager.setProperty("DSND_TH_PROD_REGION", "DP1M");
			propertiesManager.setProperty("DSND_TH_PROD_SCHEMA", "ICP1MDBA.");

			propertiesManager.setProperty("DSND_TH_PRODUCTIONSUPPORT_REGION", "DP1M");
			propertiesManager.setProperty("DSND_TH_PRODUCTIONSUPPORT_SCHEMA", "ICR3MDBA.");

			propertiesManager.setProperty("DSND_TH_CUSTTEST_REGION", "DP1M");
			propertiesManager.setProperty("DSND_TH_CUSTTEST_SCHEMA", "ICR1MDBA.");

			propertiesManager.setProperty("DSND_AU_PROD_REGION", "DP1M");
			propertiesManager.setProperty("DSND_AU_PROD_SCHEMA", "ICP1MDBA.");

			propertiesManager.setProperty("DSND_AU_PRODUCTIONSUPPORT_REGION", "DP1M");
			propertiesManager.setProperty("DSND_AU_PRODUCTIONSUPPORT_SCHEMA", "ICR3MDBA.");

			propertiesManager.setProperty("DSND_AU_CUSTTEST_REGION", "DP1M");
			propertiesManager.setProperty("DSND_AU_CUSTTEST_SCHEMA", "ICR1MDBA.");

			propertiesManager.setProperty("DSND_NZ_PROD_REGION", "DP1M");
			propertiesManager.setProperty("DSND_NZ_PROD_SCHEMA", "ICP1MDBA.");

			propertiesManager.setProperty("DSND_NZ_PRODUCTIONSUPPORT_REGION", "DP1M");
			propertiesManager.setProperty("DSND_NZ_PRODUCTIONSUPPORT_SCHEMA", "ICR3MDBA.");

			propertiesManager.setProperty("DSND_NZ_CUSTTEST_REGION", "DP1M");
			propertiesManager.setProperty("DSND_NZ_CUSTTEST_SCHEMA", "ICR1MDBA.");

			propertiesManager.setProperty("DSND_BE_PROD_REGION", "DP1E");
			propertiesManager.setProperty("DSND_BE_PROD_SCHEMA", "ICP1EDBA.");

			propertiesManager.setProperty("DSND_BE_PRODUCTIONSUPPORT_REGION", "DP1E");
			propertiesManager.setProperty("DSND_BE_PRODUCTIONSUPPORT_SCHEMA", "ICR3EDBA.");

			propertiesManager.setProperty("DSND_BE_CUSTTEST_REGION", "DP1E");
			propertiesManager.setProperty("DSND_BE_CUSTTEST_SCHEMA", "ICR1EDBA.");

			propertiesManager.setProperty("DSND_NL_PROD_REGION", "DP1E");
			propertiesManager.setProperty("DSND_NL_PROD_SCHEMA", "ICP1EDBA.");

			propertiesManager.setProperty("DSND_NL_PRODUCTIONSUPPORT_REGION", "DP1E");
			propertiesManager.setProperty("DSND_NL_PRODUCTIONSUPPORT_SCHEMA", "ICR3EDBA.");

			propertiesManager.setProperty("DSND_NL_CUSTTEST_REGION", "DP1E");
			propertiesManager.setProperty("DSND_NL_CUSTTEST_SCHEMA", "ICR1EDBA.");
			
			propertiesManager.setProperty("DSND_BR_CUSTTEST_REGION", "DP1A");
			propertiesManager.setProperty("DSND_BR_CUSTTEST_SCHEMA", "ICR1ADBA.");
			
			propertiesManager.setProperty("DSND_BR_PROD_REGION", "DP1A");
			propertiesManager.setProperty("DSND_BR_PROD_SCHEMA", "ICP1ADBA.");
			
			propertiesManager.setProperty("DSND_BR_PRODUCTIONSUPPORT_REGION", "DP1A");
			propertiesManager.setProperty("DSND_BR_PRODUCTIONSUPPORT_SCHEMA", "ICP1ADBA.");
			
			propertiesManager.setProperty("DSND_US_PROD_REGION", "DSN");
			propertiesManager.setProperty("DSND_US_PROD_SCHEMA", "UCFPDBA.");
			propertiesManager.setProperty("DSNDSP_US_PROD_SCHEMA", "UCFPDBA.");
			
			propertiesManager.setProperty("DSND_US_PRODUCTIONSUPPORT_REGION", "DSN");
			propertiesManager.setProperty("DSND_US_PRODUCTIONSUPPORT_SCHEMA", "ICR2UDBA.");	
			propertiesManager.setProperty("DSNDSP_US_PRODUCTIONSUPPORT_SCHEMA", "ICR2UDBA.");
			
			propertiesManager.setProperty("DSND_US_CUSTTEST_REGION", "DSN");
			propertiesManager.setProperty("DSND_US_CUSTTEST_SCHEMA", "ICR2UDBA.");
			propertiesManager.setProperty("DSNDSP_US_CUSTTEST_SCHEMA", "ICR2UDBA.");	
			
			propertiesManager.setProperty("DSND_UY_CUSTTEST_REGION", "DP1A");
			propertiesManager.setProperty("DSND_UY_CUSTTEST_SCHEMA", "ICR1ADBA.");
			
			propertiesManager.setProperty("DSND_UY_PROD_REGION", "DP1A");
			propertiesManager.setProperty("DSND_UY_PROD_SCHEMA", "ICP1ADBA.");
			
			propertiesManager.setProperty("DSND_UY_PRODUCTIONSUPPORT_REGION", "DP1A");
			propertiesManager.setProperty("DSND_UY_PRODUCTIONSUPPORT_SCHEMA", "ICP1ADBA.");
			
			
			//Type4 driver
			propertiesManager.setProperty("DSN_DRIVERURL", "//ICCMVS2.POK.IBM.COM:6009/DSN");
			propertiesManager.setProperty("DP1E_DRIVERURL", "//ICCMVS2.POK.IBM.COM:6000/USIBMVRDP1E");
			propertiesManager.setProperty("DP1M_DRIVERURL", "//ICCMVS2.POK.IBM.COM:6006/DP1M");
			propertiesManager.setProperty("DP1A_DRIVERURL", "//ICCMVS2.POK.IBM.COM:6003/USIBMAS7DP1A");
			propertiesManager.setProperty("DSND_DRIVERURL", "//ICCMVS1.POK.IBM.COM:6000/DSND");
			propertiesManager.setProperty("DEVAS400_DRIVERURL", "//DEVAS400.LEXINGTON.IBM.COM:446/DEVAS400");
			propertiesManager.setProperty("IRFS01_DRIVERURL", "//IRFS01.POK.IBM.COM:446/IRFS01");
			propertiesManager.setProperty("IRFS02_DRIVERURL", "//IRFS02.POK.IBM.COM:446/IRFS02");
			propertiesManager.setProperty("IRFS03_DRIVERURL", "//IRFS03.POK.IBM.COM:446/IRFS03");
			

			// gcps webservice
			propertiesManager.setProperty("CUSTTEST_WEBSERVICEGETCOA", "getcoa.wss");
			propertiesManager.setProperty("CUSTTEST_XMLNAMESPACEGETCOA", "http://www.ibm.com/xmlns/igf/gcps/getcoa");
			propertiesManager.setProperty("CUSTTEST_WEBSERVICEGETOL", "getofferingletter.wss");
			propertiesManager.setProperty("CUSTTEST_XMLNAMESPACEGETOL", "http://www.ibm.com/xmlns/igf/gcps/getofferingletter");
			propertiesManager.setProperty("CUSTTEST_WEBSERVICEGETINVOICE", "getinvoice.wss");
			propertiesManager.setProperty("CUSTTEST_XMLNAMESPACEGETINVOICE", "http://www.ibm.com/xmlns/ims/1.3/GetInvoice_2005-03-04");
			propertiesManager.setProperty("CUSTTEST_WEBSERVICEPROCESSINVOICE", "processinvoice.wss");
			propertiesManager.setProperty("CUSTTEST_XMLNAMESPACEPROCESSINVOICE", "http://www.ibm.com/xmlns/ims/1.3/ProcessInvoice_2012-12-20");
			propertiesManager.setProperty("CUSTTEST_WEBSERVICEUPDATECOA", "updatecoa.wss");
			propertiesManager.setProperty("CUSTTEST_XMLNAMESPACEUPDATECOA", "http://www.ibm.com/xmlns/igf/gcps/updatecoa");
			propertiesManager.setProperty("CUSTTEST_WEBSERVICEUPDATEOL", "updateofferingletter.wss");
			propertiesManager.setProperty("CUSTTEST_XMLNAMESPACEUPDATEOL", "http://www.ibm.com/xmlns/igf/gcps/updateOfferringLetter");
			propertiesManager.setProperty("CUSTTEST_WEBSERVICECREATEOL", "autocreateofferletter.wss");
			propertiesManager.setProperty("CUSTTEST_XMLNAMESPACECREATEOL", "http://www.ibm.com/xmlns/igf/gcps/autocreateofferletter");
			propertiesManager.setProperty("CUSTTEST_WEBSERVICECREATEMISC", "DocumentMonitorWebService.wss");
			propertiesManager.setProperty("CUSTTEST_XMLNAMESPACECREATEMISC", "http://w3.ibm.com/xmlns/ibmww/igf/gcps/documentMonitorWebService");			
			propertiesManager.setProperty("CUSTTEST_WEBSERVICEGETTAXES", "ctswebservice.wss");			

			propertiesManager.setProperty("DEV_WEBSERVICEGETCOA", "getcoa.wss");
			propertiesManager.setProperty("DEV_XMLNAMESPACEGETCOA", "http://www.ibm.com/xmlns/igf/gcps/getcoa");
			propertiesManager.setProperty("DEV_WEBSERVICEGETOL", "getofferingletter.wss");
			propertiesManager.setProperty("DEV_XMLNAMESPACEGETOL", "http://www.ibm.com/xmlns/igf/gcps/getofferingletter");
			propertiesManager.setProperty("DEV_WEBSERVICEGETINVOICE", "getinvoice.wss");
			propertiesManager.setProperty("DEV_XMLNAMESPACEGETINVOICE", "http://www.ibm.com/xmlns/ims/1.3/GetInvoice_2005-03-04");
			propertiesManager.setProperty("DEV_WEBSERVICEPROCESSINVOICE", "processinvoice.wss");
			propertiesManager.setProperty("DEV_XMLNAMESPACEPROCESSINVOICE", "http://www.ibm.com/xmlns/ims/1.3/ProcessInvoice_2012-12-20");
			propertiesManager.setProperty("DEV_WEBSERVICEUPDATECOA", "updatecoa.wss");
			propertiesManager.setProperty("DEV_XMLNAMESPACEUPDATECOA", "http://www.ibm.com/xmlns/igf/gcps/updatecoa");
			propertiesManager.setProperty("DEV_WEBSERVICEUPDATEOL", "updateofferingletter.wss");
			propertiesManager.setProperty("DEV_XMLNAMESPACEUPDATEOL", "http://www.ibm.com/xmlns/igf/gcps/updateOfferringLetter");
			propertiesManager.setProperty("DEV_WEBSERVICECREATEOL", "autocreateofferletter.wss");
			propertiesManager.setProperty("DEV_XMLNAMESPACECREATEOL", "http://www.ibm.com/xmlns/igf/gcps/autocreateofferletter");
			propertiesManager.setProperty("DEV_WEBSERVICECREATEMISC", "DocumentMonitorWebService.wss");
			propertiesManager.setProperty("DEV_XMLNAMESPACECREATEMISC", "http://w3.ibm.com/xmlns/ibmww/igf/gcps/documentMonitorWebService");
			propertiesManager.setProperty("DEV_WEBSERVICEGETTAXES", "ctswebservice.wss");	
			
			//Story 1695422 Update GCMS Web Service Authentication
			propertiesManager.setProperty("DEV_WSUSERID", "someuser");
			propertiesManager.setProperty("DEV_WSPASSWORD", "GILtoGCPS");
			//propertiesManager.setProperty("DEV_WSUSERID", "igfgidev@tst.ibm.com");
			//propertiesManager.setProperty("DEV_WSPASSWORD", " Blu3b00k" )
			//End Story 1695422 

			// other settings
			propertiesManager.setProperty("AXISCLIENTCONFIG", "client-config.wsdd");
			propertiesManager.setProperty("SNTPHost", "timeserver.btv.ibm.com");
			propertiesManager.setProperty("ACCESS_LEVEL_PRIVILEGES","IGF Workprivs,IGFWorkprivs,IGFVIEWPRIVS2,IGF Enhanced View Privleges");			
			refreshPropertiesManager();
			overridePropertiesManager();
			propertiesManager.setProperty("DEV_CMSERVER", "IGFDEV3");
			// overrides
			// propertiesManager.setProperty ("DEV_WEBSERVER",
			// "http://9.65.227.142:1234/financing/tools/gcps/services/");
			// propertiesManager.setProperty ("PRODUCTIONSUPPORT_WEBSERVER",
			// "http://9.65.231.112:1234/financing/tools/gcps/services/");
			// propertiesManager.setProperty ("USERTEST_WEBSERVER",
			// "http://9.59.100.225:1234/financing/tools/gcps/services/");

			propertiesManager.setProperty("DSND_USERTEST_REGION", "DSND");
			propertiesManager.setProperty("DSND_USERTEST_SCHEMA", "ICU1IDBA.");
			propertiesManager.setProperty("DSND_DEV_REGION", "DSND");
			propertiesManager.setProperty("DSND_DEV_SCHEMA", "ICU1IDBA.");

			// presist web service development defaults
			propertiesManager.setProperty("PERSIST_DEV_USERID", "-053RK897@tst.ibm.com");
			propertiesManager.setProperty("PERSIST_DEV_PASSWORD", "cGs1MnB3");
			propertiesManager.setProperty("PERSIST_DEV_CLIENTID", "-053RK897@tst.ibm.com");
			propertiesManager.setProperty("PERSIST_DEV_CMDB2NODE", "ICMNLSDB");
			propertiesManager.setProperty("PERSIST_DEV_CONNECTSTRING", "SCHEMA=ICMADMIN");
			propertiesManager.setProperty("PERSIST_DEV_ENVID", "DEV");
			propertiesManager.setProperty("PERSIST_DEV_APPLICATIONNAME", "GIL");
			//Test Defect 1615760-GIL-MISC- Change error message for invalid country code, remove defaults button on Gil/Gui screen MANG 03-11-2016
			propertiesManager.setProperty("ACCESS_MISC_COUNTRIES","AR,AT,AU,BE,BR,CA,CH,CL,CN,CO,CZ,DE,DK,ES,FI,FR,GB,HK,HR,HU,IE,IL,IN,IT,KR,MX,MY,NL,NO,NZ,PE,PH,PL,PT,SE,SG,SI,SK,TH,TR,TW,US,UY,VE,ZA");
			//End Test Defect 1615760
			//1664571 [GST] GIL should show different input invoice screens based on the effective date of the invoice
			// Prod Date 2017-07-01 00:00:00.0
			propertiesManager.setProperty("APTS_EFFECTIVE_DATE", "2017-07-01 00:00:00.0");
			//End 1664571
			//1676209 [GST MISC]- Gil Should show different invoice screens based on the effective date
			// Prod Date 2017-07-01 00:00:00.0
			//propertiesManager.setProperty("MISC_EFFECTIVE_DATE", "2017-03-01 00:00:00.0");
			//End 1676209
		}
		return propertiesManager;
	}

	public static void refreshPropertiesManager() {

		// open the ftp connection, try 2 different versions
		boolean loaded = false;
		for (int i = 0; i < 2 && !loaded; i++) {
			String ftpurl = propertiesManager.getProperty("FTP_URL_" + i);
			if (ftpurl != null && ftpurl.length() > 0) {
				// load from the ftp server
				String url = null;
				try {
					url = ftpurl + ftpPropertiesFileName;
					URLConnection con = (new java.net.URL(url)).openConnection();
					if (con != null) {
						long filesize = con.getContentLength();

						// get the input stream to read
						BufferedInputStream is = new BufferedInputStream(con.getInputStream());
						if (is != null) {
							// debug ("Properties loaded from " +
							// con.toString());
							propertiesManager.load(decryptInputStream(is));
							loaded = true;
							ftpURL = ftpurl;
						}
					}
				} catch (Exception exc) {
					debug(exc.toString() + " url = " + url);
				}
			}
		}
	}

	public static void overridePropertiesManager() {

		try {
			// get the file
			FileInputStream fis = new FileInputStream(overrideFileName);

			// get the input stream to read
			BufferedInputStream is = new BufferedInputStream(fis);
			if (is != null) {
				// debug ("Properties loaded from " + overrideFileName);
				propertiesManager.load(decryptInputStream(is));
			}
		} catch (Exception exc) {

		}
	}

	public static InputStream decryptInputStream(InputStream source) throws IOException, FileNotFoundException, StreamCorruptedException {
		ObjectInputStream is = new ObjectInputStream(source);
		ByteArrayOutputStream os = new ByteArrayOutputStream();

		String password = propertiesManager.getProperty("FTPDATA");
		Cryptography.dcryptStream(is, os, password.toCharArray());

		os.flush();
		ByteArrayInputStream dis = new ByteArrayInputStream(os.toByteArray());

		os.close();

		return (dis);
	}

	public static void encryptOutputStream(ByteArrayOutputStream source, String destination) throws IOException, FileNotFoundException, StreamCorruptedException {
		// get the data to write
		ByteArrayInputStream is = new ByteArrayInputStream(source.toByteArray());

		// remove the old file
		File dest = new File(destination);
		dest.delete();

		// create a new output stream to write it to
		ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(destination));

		// get the password
		String password = propertiesManager.getProperty("FTPDATA");

		// encrypt the input stream to the output stream
		Cryptography.ecryptStream(is, os, password.toCharArray(), "No Hint");

		is.close();
		os.flush();
		os.close();
	}

	public static void debug(String txt) {
		logger.debug(txt);
	}

	public static void saveProperties() {
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			getPropertiesManager().store(os, "Properties");
			encryptOutputStream(os, propertiesFileName);
		} catch (Exception exc) {
			debug(exc.toString());
		}
	}

	private static transient String[] countryList = { "AR", "AT", "AU", "BE", "BR", "CH", "CL", "CN", "CO", "CZ", "DE", "DK", "ES", "FI", "FR", "GB", "HK", "HR", "HU", "IE", "IL", "IN", "IT", "KR", "MX", "MY", "NL", "NO", "NZ", "PE", "PH", "PL", "PT",
			"RU", "SE", "SG", "SI", "SK", "TH", "TW", "US", "UY", "VE", "ZA" };
	private static transient String[] globalPropertyHeader = { "GCMS WEBSERVER", "RDC WEBSERVER" };
	private static transient String[] globalPropertyList = { "%PROPKEY%_WEBSERVER", "%PROPKEY%_RDCURL" };

	private static transient String[] countryPropertyHeader = { "IBM COUNTRY CODE", "IBM ENTERPRISE NUMBER", "RDC USE ADDR SEQ NO", "RDC ADDR SEQ NO" };
	private static transient String[] countryPropertyList = { "%PROPKEY%_%COUNTRY%_IBM_COUNTRY_CODE", "%PROPKEY%_%COUNTRY%_IBM_ENTERPRISE_NUMBER", "%PROPKEY%_%COUNTRY%_RDC_USE_ADDRESS_SEQ_NUM", "%PROPKEY%_%COUNTRY%_RDC_ADDRESS_SEQ_NUM" };

	private static transient String[] databasePropertyHeader = { "DSND", "DSND SCHEMA", "APTS", "APTS SCHEMA", "GCMS", "GCMS SCHEMA" };
	private static transient String[] databasePropertyList = { "DSND_%COUNTRY%_%PROPKEY%_REGION", "DSND_%COUNTRY%_%PROPKEY%_SCHEMA", "APTS_%COUNTRY%_%PROPKEY%_REGION", "APTS_%COUNTRY%_%PROPKEY%_SCHEMA", "GCMS_%COUNTRY%_%PROPKEY%_REGION",
			"GCMS_%COUNTRY%_%PROPKEY%_SCHEMA" };

	private static transient String[] requiredConnectionsHeader = {"ID", "DATASOURCE", "DATABASE NAME", "SERVER", "PORT", "OS"}; 
	
	private static transient String[] regionsHeader = { "Production Support", "Production", "Pre-Production", "System Test", "Alternate System Test", "Development", "Alternate Development" };
	private static transient int[] regionsToScan = { RegionManager.PRODUCTIONSUPPORT, RegionManager.PRODUCTION, RegionManager.PREPRODUCTIONSUPPORT, RegionManager.SYSTEMTEST, RegionManager.ALTERNATESYSTEMTEST, RegionManager.DEVELOPMENT,
			RegionManager.ALTERNATEDEVELOPMENT };
	
	private static transient String[] cmServerList = {"PROD", "PRODUCTIONSUPPORT", "CUSTTEST2", "CUSTTEST", "DEV"};
	private static transient String[][] backendServerList = {
		{"Production"},
		{"Production Support"},
		{"Production Support"},
		/*GILConsole.testRegionLiterals,
		GILConsole.devRegionLiterals*/
	};

	public final static Comparator<String> countrySort = new Comparator<String>() {
		public int compare(String object1, String object2) {
			boolean apGeo1 = (Arrays.binarySearch(apCountries, object1) >= 0); 
			boolean amGeo1 = (Arrays.binarySearch(usCountries, object1) >= 0); 
			boolean apGeo2 = (Arrays.binarySearch(apCountries, object2) >= 0); 
			boolean amGeo2 = (Arrays.binarySearch(usCountries, object2) >= 0);
			
			int geo1 = apGeo1 ? 0 : (amGeo1 ? 1 : 2);
			int geo2 = apGeo2 ? 0 : (amGeo2 ? 1 : 2);
			
			int result = (geo1 - geo2);
			if (result == 0)
			{
				result = object1.compareTo(object2);
			}
			
			
			return result;
		}
	};
	
	/*public static void main(String[] args) {
		GILConsole.getHiddenConsole();
		Properties props = DefaultPropertyManager.getPropertiesManager();

		try {
			
			Arrays.sort (countryList, countrySort);
			
			File aFile = new File("c:\\temp\\gilprops.html");
			FileWriter aFileWriter = new FileWriter(aFile);

			String key = null;
			String value = null;

			// list cm to region associations
			aFileWriter.write("<HTML><BODY><FONT SIZE='-2'>\n");
			aFileWriter.write("<TABLE BORDER='1'>\n");
			aFileWriter.write("<TR>\n");
			aFileWriter.write("<TH>\n");
			aFileWriter.write("CM SERVER");
			aFileWriter.write("</TH>\n");
			aFileWriter.write("<TH>\n");
			aFileWriter.write("GIL BACKENDS");
			aFileWriter.write("</TH>\n");
			aFileWriter.write("</TR>\n");
			
			for (int cmServerListIdx = 0; cmServerListIdx < cmServerList.length; cmServerListIdx++) 
			{
				aFileWriter.write("<TR>\n");
				aFileWriter.write("<TD>\n");
				aFileWriter.write(props.getProperty(cmServerList[cmServerListIdx] + "_CMSERVER"));
				aFileWriter.write("</TD>\n");
				
				String[] values = backendServerList[cmServerListIdx];
				for (int valuesIdx = 0; valuesIdx<values.length; valuesIdx++)
				{
					if (valuesIdx > 0)
					{
						aFileWriter.write("</TR>\n");
						aFileWriter.write("<TR>\n");
						aFileWriter.write("<TD>\n");
						aFileWriter.write(NBSP);
						aFileWriter.write("</TD>\n");
					}
					aFileWriter.write("<TD>\n");
					aFileWriter.write(values[valuesIdx]);
					aFileWriter.write("</TD>\n");
				}
				aFileWriter.write("</TR>\n");
			}
			aFileWriter.write("</TABLE>\n");
			aFileWriter.write("<BR>");
			aFileWriter.write("<BR>");

			// list global settings
			aFileWriter.write("<TABLE BORDER='1'>\n");
			for (int globalPropertyListIdx = 0; globalPropertyListIdx < globalPropertyList.length; globalPropertyListIdx++) {
				key = globalPropertyList[globalPropertyListIdx];
				aFileWriter.write("<TR>\n");
				aFileWriter.write("<TH COLSPAN='3'>\n");
				aFileWriter.write(globalPropertyHeader[globalPropertyListIdx]);
				aFileWriter.write("</TH>\n");
				aFileWriter.write("</TR>\n");
				for (int regionsToScanIdx = 0; regionsToScanIdx < regionsToScan.length; regionsToScanIdx++) {
					RegionManager.setBackendRegion(regionsToScan[regionsToScanIdx]);
					value = RegionManager.getCascadingProperty(key);
					aFileWriter.write("<TR>\n");
					aFileWriter.write("<TD WIDTH='20'>&nbsp;\n");
					aFileWriter.write("</TD>\n");
					aFileWriter.write("<TD>\n");
					aFileWriter.write(regionsHeader[regionsToScanIdx]);
					aFileWriter.write("</TD>\n");
					aFileWriter.write("<TD>\n");
					aFileWriter.write(value);
					aFileWriter.write("</TD>\n");
					aFileWriter.write("</TR>\n");
				}
			}
			aFileWriter.write("</TABLE>\n");
			aFileWriter.write("<BR>");
			aFileWriter.write("<BR>");

			// list country settings
			for (int countryPropertyListIdx = 0; countryPropertyListIdx < countryPropertyList.length; countryPropertyListIdx += 2) {
				aFileWriter.write("<TABLE BORDER='1'>\n");
				aFileWriter.write("<TR>\n");
				aFileWriter.write("<TD>&nbsp;\n");
				aFileWriter.write("</TD>\n");
				aFileWriter.write("<TH NOWRAP>\n");
				aFileWriter.write(countryPropertyHeader[countryPropertyListIdx]);
				aFileWriter.write("</TH>\n");
				aFileWriter.write("<TH NOWRAP>\n");
				aFileWriter.write(countryPropertyHeader[countryPropertyListIdx + 1]);
				aFileWriter.write("</TH>\n");
				aFileWriter.write("</TR>\n");

				RegionManager.setBackendRegion(RegionManager.DEVELOPMENT);
				for (int countryIdx = 0; countryIdx < countryList.length; countryIdx++) {
					aFileWriter.write("<TR>\n");
					aFileWriter.write("<TD>\n");
					aFileWriter.write(countryList[countryIdx]);
					aFileWriter.write("</TD>\n");

					key = countryPropertyList[countryPropertyListIdx];
					value = RegionManager.getCascadingProperty(countryList[countryIdx], key);
					if (value == null)
						value = NBSP;
					aFileWriter.write("<TD>\n");
					aFileWriter.write(value);
					aFileWriter.write("</TD>\n");

					key = countryPropertyList[countryPropertyListIdx + 1];
					value = RegionManager.getCascadingProperty(countryList[countryIdx], key);
					if (value == null)
						value = NBSP;
					aFileWriter.write("<TD>\n");
					aFileWriter.write(value);
					aFileWriter.write("</TD>\n");
					aFileWriter.write("</TR>\n");
				}
				aFileWriter.write("</TABLE>\n");
				aFileWriter.write("<BR>");
				aFileWriter.write("<BR>");
			}

			// list database entries
			for (int databasePropertyListIdx = 0; databasePropertyListIdx < databasePropertyList.length; databasePropertyListIdx += 2) {
				aFileWriter.write("<TABLE BORDER='1'>\n");
				aFileWriter.write("<TR>\n");
				for (int regionsToScanIdx = 0; regionsToScanIdx < regionsToScan.length; regionsToScanIdx++) {
					aFileWriter.write("<TD>&nbsp;\n");
					aFileWriter.write("</TD>\n");
					aFileWriter.write("<TH NOWRAP ALIGN='CENTER' COLSPAN='2'>\n");
					aFileWriter.write(regionsHeader[regionsToScanIdx]);
					aFileWriter.write("</TH>\n");
				}
				aFileWriter.write("</TR>\n");
				aFileWriter.write("<TR>\n");
				for (int regionsToScanIdx = 0; regionsToScanIdx < regionsToScan.length; regionsToScanIdx++) {
					key = databasePropertyList[databasePropertyListIdx];
					aFileWriter.write("<TD>&nbsp;\n");
					aFileWriter.write("</TD>\n");
					aFileWriter.write("<TH NOWRAP>\n");
					aFileWriter.write(databasePropertyHeader[databasePropertyListIdx]);
					aFileWriter.write("</TH>\n");
					aFileWriter.write("<TH NOWRAP>\n");
					aFileWriter.write(databasePropertyHeader[databasePropertyListIdx + 1]);
					aFileWriter.write("</TH>\n");
				}
				aFileWriter.write("</TR>\n");
				for (int countryIdx = 0; countryIdx < countryList.length; countryIdx++) {
					aFileWriter.write("<TR>\n");
					aFileWriter.write("<TD>\n");
					aFileWriter.write(countryList[countryIdx]);
					aFileWriter.write("</TD>\n");
					for (int regionsToScanIdx = 0; regionsToScanIdx < regionsToScan.length; regionsToScanIdx++) {
						RegionManager.setBackendRegion(regionsToScan[regionsToScanIdx]);
						key = databasePropertyList[databasePropertyListIdx];
						value = RegionManager.getCascadingProperty(countryList[countryIdx], key);
						if (value == null)
							value = NBSP;
						aFileWriter.write("<TD>\n");
						aFileWriter.write(value);
						aFileWriter.write("</TD>\n");

						key = databasePropertyList[databasePropertyListIdx + 1];
						value = RegionManager.getCascadingProperty(countryList[countryIdx], key);
						if (value == null)
							value = NBSP;
						aFileWriter.write("<TD>\n");
						aFileWriter.write(value);
						aFileWriter.write("</TD>\n");

						if (regionsToScanIdx < regionsToScan.length - 1) {
							aFileWriter.write("<TD>&nbsp;\n");
							aFileWriter.write("</TD>\n");
						}
					}
					aFileWriter.write("</TR>\n");
				}
				aFileWriter.write("</TABLE>\n");
				aFileWriter.write("<BR>");
				aFileWriter.write("<BR>");
			}

			aFileWriter.write("<TABLE BORDER='1'>\n");
			aFileWriter.write("<TR>\n");
			aFileWriter.write("<TH>RMI SERVER\n");
			aFileWriter.write("</TH>\n");
			for (int regionsToScanIdx = 0; regionsToScanIdx < regionsToScan.length; regionsToScanIdx++) {
				aFileWriter.write("<TH NOWRAP>\n");
				aFileWriter.write(regionsHeader[regionsToScanIdx]);
				aFileWriter.write("</TH>\n");
			}
			aFileWriter.write("</TR>\n");
			ContentManagerConnection aContentManagerConnection = new ContentManagerConnection();
			for (int countryIdx = 0; countryIdx < countryList.length; countryIdx++) {
				aFileWriter.write("<TR>\n");
				aFileWriter.write("<TD>\n");
				aFileWriter.write(countryList[countryIdx]);
				aFileWriter.write("</TD>\n");
				for (int regionsToScanIdx = 0; regionsToScanIdx < regionsToScan.length; regionsToScanIdx++) {
					RegionManager.setBackendRegion(regionsToScan[regionsToScanIdx]);
					try {
						aContentManagerConnection.setCountry(countryList[countryIdx]);
						aContentManagerConnection.lookupContentManager();
						aFileWriter.write("<TD>\n");
						
						// primary
						value = ContentManagerConnection.getURL();
						if (value == null || value.trim().length() == 0) {
							value = NBSP;
						} else {
							try {
								URL aURL = new URL ("http:" + value);
								value = aURL.getHost() + ":" + aURL.getPort();
							} catch (Exception e) {
							}
						}
						aFileWriter.write(value);
						aFileWriter.write("<BR>");
						
						// secondary
						value = ContentManagerConnection.getSecondaryURL();
						if (value == null || value.trim().length() == 0) {
							value = NBSP;
						} else {
							try {
								URL aURL = new URL ("http:" + value);
								value = aURL.getHost() + ":" + aURL.getPort();
							} catch (Exception e) {
							}
								
						}
						aFileWriter.write(value);
						aFileWriter.write("<BR>");
						
						// tertiary
						value = ContentManagerConnection.getTertiaryURL();
						if (value == null || value.trim().length() == 0) {
							value = NBSP;
						} else {
							try {
								URL aURL = new URL ("http:" + value);
								value = aURL.getHost() + ":" + aURL.getPort();
							} catch (Exception e) {
							}

						}
						aFileWriter.write(value);
						aFileWriter.write("</TD>\n");
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				aFileWriter.write("</TR>\n");
			}
			aFileWriter.write("</TABLE>\n");

			aFileWriter.write("<BR>");
			aFileWriter.write("<BR>");
			
			
			aFileWriter.write("<TABLE BORDER='1'>\n");
			aFileWriter.write("<TR>\n");
			for (int requiredConnectionsHeaderIdx = 0; requiredConnectionsHeaderIdx < requiredConnectionsHeader.length; requiredConnectionsHeaderIdx++)
			{
				aFileWriter.write("<TH>\n");
				aFileWriter.write(requiredConnectionsHeader[requiredConnectionsHeaderIdx]);
				aFileWriter.write("</TH>\n");
			}
			aFileWriter.write("</TR>\n");
			
			String[] values = null;
			for (int dbConnectionsIdx = 0 ; dbConnectionsIdx < GILConsole.requiredConnections.length; dbConnectionsIdx++)
			{
				values = GILConsole.requiredConnections[dbConnectionsIdx];
				aFileWriter.write("<TR>\n");
				for (int valuesIdx = 0 ; valuesIdx < values.length; valuesIdx++)
				{
					aFileWriter.write("<TD>\n");
					aFileWriter.write(values[valuesIdx]);
					aFileWriter.write("</TD>\n");
				}
				aFileWriter.write("</TR>\n");
			}
			aFileWriter.write("</TABLE>\n");

			aFileWriter.write("<BR>");
			aFileWriter.write("<BR>");		
			
			aFileWriter.write("<TABLE BORDER='1'>\n");
			aFileWriter.write("<TR>\n");
			aFileWriter.write("<TH NOWRAP>\n");
			aFileWriter.write("Country Code");
			aFileWriter.write("</TH>\n");
			aFileWriter.write("<TH NOWRAP>\n");
			aFileWriter.write("Country Name");
			aFileWriter.write("</TH>\n");
			aFileWriter.write("</TR>\n");
			Locale aLocale = null;
			for (int countryIdx = 0; countryIdx < countryList.length; countryIdx++) {
				aFileWriter.write("<TR>\n");
				aFileWriter.write("<TD>\n");
				aFileWriter.write(countryList[countryIdx]);
				aFileWriter.write("</TD>\n");
				aLocale = new Locale("en", countryList[countryIdx].toLowerCase());
				String name = aLocale.getDisplayCountry();
				aFileWriter.write("<TD>\n");
				aFileWriter.write(name);
				aFileWriter.write("</TD>\n");
				aFileWriter.write("</TR>\n");
			}
			aFileWriter.write("</TABLE>\n");

			aFileWriter.write("</FONT></BODY></HTML>");

			aFileWriter.flush();
			aFileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				System.exit(0);
			}

		});
	}*/

	public static boolean isAPServer(String cntry)
	{
		if (Arrays.binarySearch(apCountries, cntry) != -1) {
			return true;
		}
		return false;
	}
	
	public static boolean isUSServer(String cntry)
	{
		if (Arrays.binarySearch(usCountries, cntry) != -1) {
			return true;
		}
		return false;
	}
}
