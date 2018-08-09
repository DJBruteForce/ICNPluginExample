package com.ibm.gil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.ibm.ecm.extension.Plugin;
import com.ibm.ecm.extension.PluginFeature;
import com.ibm.ecm.extension.PluginLayout;
import com.ibm.ecm.extension.PluginMenu;
import com.ibm.ecm.extension.PluginMenuType;
import com.ibm.ecm.extension.PluginODAuthenticationService;
import com.ibm.ecm.extension.PluginOpenAction;
import com.ibm.ecm.extension.PluginRequestFilter;
import com.ibm.ecm.extension.PluginResponseFilter;
import com.ibm.ecm.extension.PluginServiceCallbacks;
import com.ibm.ecm.extension.PluginViewerDef;
import com.ibm.gil.util.FileUtils;
import com.ibm.gil.util.RegionalBigDecimal;
import com.ibm.gil.util.RegionalDateConverter;
import com.ibm.igf.gil.SpringApplicationContext;
import com.ibm.igf.hmvc.CountryManager;
import com.ibm.igf.hmvc.PluginConfigurationPane;
import com.ibm.igf.hmvc.WebServicesEnpointManager;

public class GilCnPlugin extends Plugin { 
	
	private static ResourceBundle resLocaleResources = null;
	private String pluginPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
	private String pathBase = FileUtils.getFilePath(new File(pluginPath));
	private Logger logger = null;
	private static final String PATH_SEPARATOR = File.separator;

    public void applicationInit(HttpServletRequest request,	PluginServiceCallbacks callbacks) throws Exception {
		
    	ApplicationContext context = null;
    	SpringApplicationContext springAppContext = null;
		String logFileName = "gil_log4j.xml";
		String appContextFile = "applicationContext.xml";
		boolean result = false;
		ZipFile zip = null;
		

		try {
			
				FileUtils.deleteFile(new File(pathBase+ PATH_SEPARATOR + logFileName));

				zip = getGilPluginJar();
					
				result = extractFileFromZip(zip, "properties", logFileName);
				
				System.out.println("Plugin Jar size: "+( zip!=null?zip.size():"zip is null") );
				
				System.out.println("Log file: "+ pathBase+ PATH_SEPARATOR + logFileName );
				System.out.println("Log File exists:" + new File( pathBase+ PATH_SEPARATOR + logFileName).exists() );
				
				
				if (result) {
					System.setProperty("log4j.configurationFile",  new File(pathBase+ PATH_SEPARATOR +logFileName).toURI().toString());
					logger = LogManager.getLogger(GilCnPlugin.class);
				}

				result = extractFileFromZip(zip, "properties", appContextFile);
				
				logger.info("Spring app context file: "+ pathBase+ PATH_SEPARATOR + appContextFile);
				logger.info("Spring File exists:" + new File( pathBase+ PATH_SEPARATOR + appContextFile).exists() );
				
				
				if (result) {
					
					logger.info("Loading Spring file...Start");
					context = new FileSystemXmlApplicationContext("file:"+pathBase+ PATH_SEPARATOR + appContextFile);
					springAppContext = new SpringApplicationContext();
					springAppContext.setSpringApplicationContext(context);
					logger.info("Loading Spring file...Finish");
				}
				String configuration ="";
				if(callbacks!=null){
					 configuration = callbacks.loadConfiguration();
					logger.info("callbacks is not null");
				}
				if(configuration!=null){
					logger.info("Configuration is not null");
				PluginConfigurationPane.loadPropertiesMap(configuration);
				}
				CountryManager.loadCountryPropertiesMap();
				WebServicesEnpointManager.loadWebServicesEndpointMap();

		        resLocaleResources = java.util.ResourceBundle.getBundle("com.ibm.igf.resources.LocaleResources",Locale.US); //$NON-NLS-1$;
		        initializeRegionalSettings();
		        
		        logger.error("Testing Error Log");
		        logger.fatal("Testing Fatal Log");
		        
		}	catch(Exception e)	{
			logger.fatal(e,e);
			e.printStackTrace(System.out);
			
		}	finally	{
				zip.close();
		}


	}
    
	/**
	 * Get a instance of cn plugin jar file
	 * @return
	 * @throws ZipException
	 * @throws IOException
	 */
	public ZipFile getGilPluginJar() throws ZipException, IOException {
		
		String decodedPath = URLDecoder.decode(pluginPath, "UTF-8");
		File gilJar = new File(decodedPath);
		
		return new ZipFile(gilJar);
		
	}
	
	/**
	 * extract a file from cn plugin jar file
	 * @param zip
	 * @param FilePathInsideZip
	 * @param outputFileName
	 * @return
	 * @throws ZipException
	 * @throws IOException
	 */
	public boolean extractFileFromZip(ZipFile zip, String FilePathInsideZip, String outputFileName) throws ZipException, IOException {
		
		InputStream is = null;
		ZipEntry zipEntry = zip.getEntry(FilePathInsideZip+ "/" + outputFileName);
		is = zip.getInputStream(zipEntry);
		boolean generatedLogFile = FileUtils.inputStreamToFile(is, pathBase+ System.getProperty("file.separator"),outputFileName );
		//System.out.println("Spring File extraction(extractFileFromZip method):" +FilePathInsideZip + "/" + outputFileName);

		return generatedLogFile;
	}


	public String getId() {
		return "GilCnPlugin";
	}


	public String getName(Locale locale) {
		return "GIL Plug-in for CN";
	}


	public String getVersion() {
		return "gilCnPluginDojo";
	}


	public String getCopyright() {
		return "Optionally add a CopyRight statement here";
	}


	public String getScript() {
		return "GilCnPlugin.js";
	}


	public String getDebugScript() {
		return getScript();
	}


	public String getDojoModule() {
		return "gilCnPluginDojo";
	}


	public String getCSSFileName() {
		return "gilCnPluginDojo.css";
	}


	public String getDebugCSSFileName() {
		return getCSSFileName();
	}


	public PluginOpenAction[] getOpenActions() {
		return new PluginOpenAction[0];
	}


	public PluginRequestFilter[] getRequestFilters() {
		return new PluginRequestFilter[]{
				new com.ibm.gil.customization.filters.request.SearchRequestFilter(),
				new com.ibm.gil.customization.filters.request.EditAttributesRequestFilter(),
				new com.ibm.gil.customization.filters.request.AddItemRequestFilter(),
				new com.ibm.gil.customization.filters.request.EmailRequestFilter()
		};
	}


	public PluginResponseFilter[] getResponseFilters() {
		return new PluginResponseFilter[]{
				new com.ibm.gil.customization.filters.response.EmailResponseFilter(),
				new com.ibm.gil.customization.filters.response.SearchResponseFilter(),
				new com.ibm.gil.customization.filters.response.GetWorkItemsResponseFilter()
		};
	}

	
	public PluginODAuthenticationService getODAuthenticationService() {
		return null;
	}

	
	public String getConfigurationDijitClass() {
		return "gilCnPluginDojo.ConfigurationPane";
	}


	public PluginViewerDef[] getViewers() {
		return new PluginViewerDef[0];
	}


	public PluginLayout[] getLayouts() {
		return new PluginLayout[0];
	}


	public PluginFeature[] getFeatures() {
		return new PluginFeature[]{
				new com.ibm.gil.customization.features.ExtendedWorkPane()
		};
	}


	public PluginMenuType[] getMenuTypes() {
		return new PluginMenuType[0];
	}


	public PluginMenu[] getMenus() {
		return new PluginMenu[0];
	}


	public com.ibm.ecm.extension.PluginService[] getServices() {
		return new com.ibm.ecm.extension.PluginService[]{
				new com.ibm.gil.service.InvoiceELSService(),
				new com.ibm.gil.service.USStateService(),
				new com.ibm.gil.service.InvoiceService(), 
				new com.ibm.gil.service.ContractService(), 
				new com.ibm.gil.service.VendorSearchService(), 
				new com.ibm.gil.service.InvoiceSearchService(), 
				new com.ibm.gil.service.OfferingLetterService(), 
				new com.ibm.gil.service.GetPropertiesService() ,
				new com.ibm.gil.service.OpenIndexerPageService(), 
				new com.ibm.gil.service.SaveDialogFormService(),
				new com.ibm.gil.service.VendorSearchELSService(),
				new com.ibm.gil.service.COAELSService(),
				new com.ibm.gil.service.CustomerSearchELSService(),
				new com.ibm.gil.service.OfferingLetterELSService(),	
				new com.ibm.gil.service.LineItemELSService(),
				new com.ibm.gil.service.SerialNumberELSService(),
				new com.ibm.gil.service.CommonService(),
				new com.ibm.gil.service.MiscELSService(),
				new com.ibm.gil.service.MiscInvoiceService(),
				new com.ibm.gil.service.MiscVendorSearchService(),
				new com.ibm.gil.service.OEMProductService(),
				new com.ibm.gil.service.FolderService(),
				new com.ibm.gil.service.WSEndpointSetupService()
				};
	}


	public com.ibm.ecm.extension.PluginAction[] getActions() {
		return new com.ibm.ecm.extension.PluginAction[]{
				new com.ibm.gil.action.OpenIndexerAction(),
				new com.ibm.gil.action.OpenNoIndexAction(),
				new com.ibm.gil.action.HistoryLogAction(),
				new com.ibm.gil.action.NotelogAction(),
				new com.ibm.gil.action.AddToFolderAction(),
				new com.ibm.gil.action.AddToNewFolderAction(),
				new com.ibm.gil.action.RemoveFromFolderAction(),
				new com.ibm.gil.action.WSEndpointSetupAction()
				};
	}
	

    
    private void initializeRegionalSettings()
    {
        String format = null;
        String pattern = null;
        String blankValue = null;
        String defaultValue = null;
        String decimalSeperator = null;

        format = resLocaleResources.getString("DB2DATEFORMAT");
        pattern = resLocaleResources.getString("DB2DATEPATTERN");
        blankValue = resLocaleResources.getString("DB2BLANKDATETEXT");
        defaultValue = resLocaleResources.getString("DB2DEFAULTDATETEXT");
        RegionalDateConverter.addDateFormatter("DB2", format, pattern, blankValue, defaultValue);

        format = resLocaleResources.getString("DB2INSERTDATEFORMAT");
        RegionalDateConverter.addDateFormatter("DB2INSERT", format, pattern, blankValue, defaultValue);

        format = resLocaleResources.getString("CMDATEFORMAT");
        pattern = resLocaleResources.getString("CMDATEPATTERN");
        blankValue = resLocaleResources.getString("CMBLANKDATETEXT");
        defaultValue = resLocaleResources.getString("CMDEFAULTDATETEXT");
        RegionalDateConverter.addDateFormatter("CM", format, pattern, blankValue, defaultValue);

        format = resLocaleResources.getString("GUIDATEFORMAT");
        pattern = resLocaleResources.getString("GUIDATEPATTERN");
        blankValue = resLocaleResources.getString("GUIBLANKDATETEXT");
        defaultValue = resLocaleResources.getString("GUIDEFAULTDATETEXT");
        RegionalDateConverter.addDateFormatter("GUI", format, pattern, blankValue, defaultValue);

       // JTextFieldDate.setDateFormats(format, pattern, blankValue);

        format = resLocaleResources.getString("DEFAULTTIMESTAMPFORMAT");
        pattern = resLocaleResources.getString("DEFAULTTIMESTAMPPATTERN");
        blankValue = resLocaleResources.getString("DEFAULTBLANKTIMESTAMP");
        defaultValue = resLocaleResources.getString("DEFAULTTIMESTAMP");
        RegionalDateConverter.addTimeStampFormatter("GUI", format, pattern, blankValue, defaultValue);
        RegionalDateConverter.addTimeStampFormatter("DB2", format, pattern, blankValue, defaultValue);

        format = resLocaleResources.getString("CMTIMESTAMPFORMAT");
        RegionalDateConverter.addTimeStampFormatter("CM", format, pattern, blankValue, defaultValue);

        format = resLocaleResources.getString("DB2INSERTTIMESTAMPFORMAT");
        RegionalDateConverter.addTimeStampFormatter("DB2INSERT", format, pattern, blankValue, defaultValue);

        format = resLocaleResources.getString("DEFAULTTIMEFORMAT");
        pattern = resLocaleResources.getString("DEFAULTTIMEPATTERN");
        blankValue = resLocaleResources.getString("DEFAULTBLANKTIME");
        defaultValue = resLocaleResources.getString("DEFAULTTIME");
        RegionalDateConverter.addTimeFormatter("GUI", format, pattern, blankValue, defaultValue);
        RegionalDateConverter.addTimeFormatter("DB2", format, pattern, blankValue, defaultValue);
        RegionalDateConverter.addTimeFormatter("CM", format, pattern, blankValue, defaultValue);

        format = resLocaleResources.getString("DB2INSERTTIMEFORMAT");
        RegionalDateConverter.addTimeFormatter("DB2INSERT", format, pattern, blankValue, defaultValue);

        //format = resLocaleResources.getString("DB2INSERTDATEFORMAT");
        format = resLocaleResources.getString("DB2INSERTBLANKDATEFORMAT");
        blankValue = resLocaleResources.getString("DB2INSERTBLANKDATETEXT");
        RegionalDateConverter.addDateFormatter("AS400INSERT", format, pattern, blankValue, defaultValue);

        // setup the decimal seperator
        DecimalFormat decimalFormatter = new DecimalFormat();
        DecimalFormatSymbols symbs = decimalFormatter.getDecimalFormatSymbols();
        
        String seperator = resLocaleResources.getString("DECIMALSEPERATOR");
        if ((seperator != null) && (seperator.length() > 0))
        {
            // override the decimal seperator if specified in the file
            char seperatorChar = symbs.getDecimalSeparator();
            seperatorChar = seperator.charAt(0);
            symbs.setDecimalSeparator(seperatorChar);
            
        }
        
        String decpoints = resLocaleResources.getString("DECIMALPOINTS");
        int decimalPoints = 2;
        try
        {
            decimalPoints = Integer.parseInt(decpoints);
        } catch (Exception exc)
        {
            // don't worry bout it
        }
        decimalFormatter.setMinimumFractionDigits(decimalPoints);
        decimalFormatter.setMaximumFractionDigits(decimalPoints);
        decimalFormatter.setGroupingUsed(false);
        decimalFormatter.setDecimalFormatSymbols(symbs);
        RegionalBigDecimal.setDecimalFormatSymbols(symbs);
        RegionalBigDecimal.setDefaultScale(decimalPoints);

    }
	

	
}
