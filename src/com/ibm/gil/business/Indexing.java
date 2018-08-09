package com.ibm.gil.business;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
//import com.ibm.igf.gui.JTextFieldDate;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import java.util.TimeZone;


import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.gson.annotations.Expose;
import com.ibm.gil.util.RegionalBigDecimal;
import com.ibm.gil.util.RegionalDateConverter;
import com.ibm.igf.contentmanager.rmi.ContentManagerImplementation;
import com.ibm.igf.hmvc.PluginConfigurationPane;
import com.ibm.mm.sdk.server.DKDatastoreICM;

import financing.tools.docgen.xml.databind.persist.types.webservice.CMDocumentType;
import financing.tools.docgen.xml.databind.persist.types.webservice.ImageInformationType;
import financing.tools.docgen.xml.databind.persist.types.webservice.ItemAttributeType;

public class Indexing {

    @Expose private java.util.Date createdDate = null;
    
    private Locale locale;
    
    private TimeZone userTimezone;
	
    protected static final transient String emptyString = "";
    
    public static int UNCHANGEDMODE = 0;

    public static int  ADDMODE = 1;

    public static int  UPDATEMODE = 3;

    public static int  REMOVEMODE = 4;

    public static int  MASSUPDATEMODE = 5;

    @Expose private String  INDEXCLASS="";

    @Expose private String  OBJECTID="";

    @Expose  private String  USERID="";

    @Expose private String  TIMESTAMP="";

    private String  SOURCE="";

    @Expose private String  COUNTRY="";

    private boolean DIRTYFLAG;

    @Expose private Integer DOCUMENTMODE;

    private String UNIQUEREQUESTNUMBER="";

    private short REQUIREDFIELDS;

    protected String TOBEINDEXEDWORKFLOW = ".*To Be Indexed.*";
    
    @Expose  private boolean isWorkAcess = false;
    
    private WebServicesEndpoint gcmsEndpoint = new WebServicesEndpoint();
    
    private WebServicesEndpoint rdcEndpoint = new WebServicesEndpoint();
    
    //private static DB2 database = null;
    
    public WebServicesEndpoint getRdcEndpoint() {
		return rdcEndpoint;
	}

	public void setRdcEndpoint(WebServicesEndpoint rdcEndpoint) {
		this.rdcEndpoint = rdcEndpoint;
	}
	public boolean db2TransactionFailed;
    String aptsregion = "APTS";
    /*
     * values
     */
    String[] values = null;
    
    /*
     * xml parsing
     */
   // private static transient DocumentBuilderFactory factory = null;
  //  private static transient DocumentBuilder builder = null;

    /*
     * cm
     */
    //private static transient ContentManagerConnection CM = new ContentManagerConnection();

   
    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(Indexing.class);
    
    private String serverTimezone = "";

    private transient DKDatastoreICM datastore ;

    
    

    public WebServicesEndpoint getGcmsEndpoint() {
		return gcmsEndpoint;
	}

	public void setGcmsEndpoint(WebServicesEndpoint gcmsEndpoint) {
		this.gcmsEndpoint = gcmsEndpoint;
	}

	public TimeZone getUserTimezone() {
		return userTimezone;
	}

	public void setUserTimezone(TimeZone userTimezone) {
		this.userTimezone = userTimezone;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		
		this.locale = locale;
		ResourceBundle resLocaleResources = null;
		resLocaleResources = java.util.ResourceBundle.getBundle("com.ibm.igf.resources.LocaleResources",this.locale);
		
        String format = resLocaleResources.getString("CMDATEFORMAT");
        String pattern = resLocaleResources.getString("GUIDATEPATTERN");
        String blankValue = resLocaleResources.getString("DB2BLANKDATETEXT");
        String defaultValue = resLocaleResources.getString("GUIDEFAULTDATETEXT");
        RegionalDateConverter.addDateFormatter("GUI-"+locale.toString(), format, pattern, blankValue, defaultValue);
            
        format = resLocaleResources.getString("DEFAULTTIMESTAMPFORMAT");
        pattern = resLocaleResources.getString("DEFAULTTIMESTAMPPATTERN");
        blankValue = resLocaleResources.getString("DEFAULTBLANKTIMESTAMP");
        defaultValue = resLocaleResources.getString("DEFAULTTIMESTAMP");
        RegionalDateConverter.addTimeStampFormatter("GUI-"+locale.toString(), format, pattern, blankValue, defaultValue);
        
        format = resLocaleResources.getString("DEFAULTTIMEFORMAT");
        pattern = resLocaleResources.getString("DEFAULTTIMEPATTERN");
        blankValue = resLocaleResources.getString("DEFAULTBLANKTIME");
        defaultValue = resLocaleResources.getString("DEFAULTTIME");
        RegionalDateConverter.addTimeFormatter("GUI-"+locale.toString(), format, pattern, blankValue, defaultValue);
      }

	public Integer getUNCHANGEDMODE() {
		return UNCHANGEDMODE;
	}

	public void setUNCHANGEDMODE(Integer uNCHANGEDMODE) {
		UNCHANGEDMODE = uNCHANGEDMODE;
	}

	public Integer getADDMODE() {
		return ADDMODE;
	}

	public void setADDMODE(Integer aDDMODE) {
		ADDMODE = aDDMODE;
	}

	public Integer getUPDATEMODE() {
		return UPDATEMODE;
	}

	public void setUPDATEMODE(Integer uPDATEMODE) {
		UPDATEMODE = uPDATEMODE;
	}

	public Integer getREMOVEMODE() {
		return REMOVEMODE;
	}

	public void setREMOVEMODE(Integer rEMOVEMODE) {
		REMOVEMODE = rEMOVEMODE;
	}

	public Integer getMASSUPDATEMODE() {
		return MASSUPDATEMODE;
	}

	public void setMASSUPDATEMODE(Integer mASSUPDATEMODE) {
		MASSUPDATEMODE = mASSUPDATEMODE;
	}

	public String getINDEXCLASS() {
		return INDEXCLASS;
	}

	public void setINDEXCLASS(String iNDEXCLASS) {
		INDEXCLASS = iNDEXCLASS;
	}

	public String getOBJECTID() {
		return OBJECTID;
	}

	public void setOBJECTID(String oBJECTID) {
		OBJECTID = oBJECTID;
	}

	public String getUSERID() {
		return USERID;
	}

	public void setUSERID(String uSERID) {
		USERID = uSERID;
	}

	public String getTIMESTAMP() {
		return TIMESTAMP;
	}

	public void setTIMESTAMP(String tIMESTAMP) {
		TIMESTAMP = tIMESTAMP;
	}

	public String getSOURCE() {
		return SOURCE;
	}

	public void setSOURCE(String sOURCE) {
		SOURCE = sOURCE;
	}

	public String getCOUNTRY() {
		return COUNTRY;
	}

	public void setCOUNTRY(String cOUNTRY) {
		COUNTRY = cOUNTRY;
	}

	public boolean isDIRTYFLAG() {
		return DIRTYFLAG;
	}

	public void setDIRTYFLAG(boolean dIRTYFLAG) {
		DIRTYFLAG = dIRTYFLAG;
	}

	public Integer getDOCUMENTMODE() {
		return DOCUMENTMODE;
	}

	public void setDOCUMENTMODE(Integer dOCUMENTMODE) {
		DOCUMENTMODE = dOCUMENTMODE;
	}

	public String getUNIQUEREQUESTNUMBER() {
		return UNIQUEREQUESTNUMBER;
	}

	public void setUNIQUEREQUESTNUMBER(String uNIQUEREQUESTNUMBER) {
		UNIQUEREQUESTNUMBER = uNIQUEREQUESTNUMBER;
	}

	public short getREQUIREDFIELDS() {
		return REQUIREDFIELDS;
	}

	public void setREQUIREDFIELDS(short rEQUIREDFIELDS) {
		REQUIREDFIELDS = rEQUIREDFIELDS;
	}

	public String getTOBEINDEXEDWORKFLOW() {
		return TOBEINDEXEDWORKFLOW;
	}

	public void setTOBEINDEXEDWORKFLOW(String tOBEINDEXEDWORKFLOW) {
		TOBEINDEXEDWORKFLOW = tOBEINDEXEDWORKFLOW;
	}

	public boolean isDb2TransactionFailed() {
		return db2TransactionFailed;
	}

	public void setDb2TransactionFailed(boolean db2TransactionFailed) {
		this.db2TransactionFailed = db2TransactionFailed;
	}

	public String getAptsregion() {
		return aptsregion;
	}

	public void setAptsregion(String aptsregion) {
		this.aptsregion = aptsregion;
	}

	public java.util.Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(java.util.Date createdDate) {
		this.createdDate = createdDate;
	}



	public void setServerTimezone(String serverTimezone) {
		this.serverTimezone = serverTimezone;
	}

	
	 public DKDatastoreICM getDatastore() {
			return datastore;
		}



		public void setDatastore(DKDatastoreICM datastore) {
			this.datastore = datastore;
		}	

   /* public IndexingDataModel(int count)
    {
        super(count);
        createDateFormat.setCalendar(new GregorianCalendar());

    }*/

/*    public void init()
    {
        super.init();
        setDOCUMENTMODE(ADDMODE);
        setDIRTYFLAG(Boolean.FALSE);
    }*/



    public String getSENDER()
    {
        return "GIL";
    }

    public String getCREATEDATEinGMT()
    {
        return RegionalDateConverter.formatTimeStamp("CM", createdDate, TimeZone.getTimeZone("GMT"));
    }
    
    public String getCREATEDATE()
    {
        return RegionalDateConverter.formatTimeStamp("CM", createdDate);
    }

    public void setCREATEDATE(String value)
    {
    	
        try {
            createdDate = RegionalDateConverter.parseTimeStamp("CM", value);
           
        } catch (Exception e) {
            createdDate = null;
        }
    }

    public void setCREATEDATE(Date value)
    {
        try {
            createdDate = value;
        } catch (Exception e) {
        	logger.debug(e.getMessage());
            createdDate = null;
        }
    }

    public Date getCREATEDATEasDate()
    {
        return createdDate;
    }

    public Long getCREATEDATEasLong()
    {
        return new Long(createdDate.getTime());
    }

    public void setCREATEDATEasLong(Long value)
    {
        createdDate.setTime(value.longValue());
    }


    
    public String getServerTimezone(){
    	return serverTimezone;
    }

    public String getCountryCodeFromIndex(String classId)
    {
    	String countryCode = null;
    	
    	// remove this assignment
    	
    	countryCode="GB";
    	
    	
      /*  if (classId.equals(OfferingLetterDataModel.ROFOLITEMTYPE))
        {
        	countryCode = GILConsole.getConsole().getDefaultCountry();
        } else
        {
        	try {
        		countryCode = classId.substring(0, 2);
        	} catch (Exception e) {
    			countryCode = null;
    		}
        }*/

        if (countryCode == null || countryCode.trim().length() == 0)
        {
        	// we won't know the country code based on the index name
        	// setup a default of GB just in case
        	return ("GB");
        } else {
        	return countryCode;
        }
    }
    
    public String[] getFields (CMDocumentType document)
    {
    	String[] fields = new String[0];
    	if ((document != null) && (document.getItemAttributes() != null && document.getItemAttributes().length > 0))
    	{
    		ItemAttributeType[] attributes = document.getItemAttributes();
    		fields = new String[attributes.length];
    		for (int i=0; i<attributes.length; i++)
    		{
    			fields[i] = attributes[i].getFieldName();
    			logger.debug("Indexing field[i] :"+ fields[i]);
    		}
    	}
    	
    	return fields;
    }
    
    public String[] getValues (CMDocumentType document)
    {
    	String[] values = new String[0];
    	if ((document != null) && (document.getItemAttributes() != null && document.getItemAttributes().length > 0))
    	{
    		ItemAttributeType[] attributes = document.getItemAttributes();
    		values = new String[attributes.length];
    		for (int i=0; i<attributes.length; i++)
    		{
    			values[i] = attributes[i].getFieldValue();
    			logger.debug("Indexing values[i]:"+values[i]);
    		}
    	}
    	
    	return values;
    }

    public Date getCreateDate (CMDocumentType document)
    {
    	Date create = null;
    	if ((document != null) && (document.getImages() != null) && (document.getImages().length > 0))
    	{
    		create = new Date();
    		ImageInformationType[] images = document.getImages();
    		for (int i=0; i<images.length; i++)
    		{
    			if (create.getTime() > images[i].getCreated().getTimeInMillis())
    			{
    				create.setTime(images[i].getCreated().getTimeInMillis());
    			}
    		}
    	}
    	return create;
    }
    
    public String getFieldMatch(String fieldName, String[] fields, String[] values)
    {
        if (fields == null)
            return "";

        if (values == null)
            return "";

        for (int i = 0; i < fields.length && i < values.length; i++)
        {
        	//logger.debug("Indexing  fields[i]:"+fields[i] );
            if (fields[i].equals(fieldName))
            {
                if (values[i] != null)
                {
                	//logger.debug("Indexing values[i]:"+values[i]);
                    return values[i].trim();
                } else
                {
                    //Debugger.debug("Invalid field specified " + fieldName);
                    return null;
                }
            }
        }
        return "";
    }

    public String getObjectId(String xmlData)
    {
        int pididx = -1;
        pididx = xmlData.indexOf("pid=\"");
        if (pididx > 0)
        {
            pididx += 5;
            int pidend = xmlData.indexOf("\"", pididx);
            String pid = xmlData.substring(pididx, pidend);
            return pid;
        }
        return "";
    }

    public void generateUNIQUEREQUESTNUMBER()
    {
        this.UNIQUEREQUESTNUMBER= "GIL" + getOBJECTID() + new Date().getTime();
    }



    public String getAttributeValue(Node aNode, String attribute)
    {
        Node item = null;
        for (int j = 0; j < aNode.getAttributes().getLength(); j++)
        {
            item = aNode.getAttributes().item(j);
            if (item.getNodeName().equals(attribute))
            {
                if (item.getNodeValue().equals("null"))
                    return "";
                else
                    return item.getNodeValue();
            }
        }
        return null;
    }

    public String getAttributeValue(Node aNode, String element, String matchKey, String attribute, String key) throws NoSuchElementException
    {
        NodeList aNodeList = null;
        aNodeList = aNode.getChildNodes();
        for (int i = 0; i < aNodeList.getLength(); i++)
        {
            aNode = aNodeList.item(i);
            if (aNode.getNodeName().equals(element))
            {
                if (attribute.equals(getAttributeValue(aNode, matchKey)))
                {
                    return getAttributeValue(aNode, key);
                }

            }
        }

        throw new NoSuchElementException("Attribute not found: Element=" + element + " Attribute=" + attribute);
    }

    public String getItemAttribute(Node aNode, String attribute)
    {
        return getAttributeValue(aNode, "index", "field", attribute, "value");
    }

    public String getCreateDate(Node aNode)
    {
        return getAttributeValue(aNode, "image", "partnum", "0", "created");
    }

    public Node getMatchingDocument(Document document, String indexClass, String objectId)
    {
        NodeList aNodeList = null;
        Node aNode = null;

        aNodeList = document.getElementsByTagName("DOCUMENT");
        for (int i = 0; i < aNodeList.getLength(); i++)
        {
            aNode = aNodeList.item(i);
            if (objectId.equals(getAttributeValue(aNode, "pid")))
            {
                if (indexClass.equals(getAttributeValue(aNode, "index")))
                {
                    return aNode;
                }
            }
        }
        return null;
    }


	protected String fixVersion(String xml)
	{
		String oldtxt = "<QUERYRESULTS>";
		String newtxt = "<tns:QUERYRESULTS xmlns:tns=\"http://w3.ibm.com/financing/tools/GCMS/QueryResults\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://w3.ibm.com/financing/tools/GCMS/QueryResults QueryResults.xsd \">	";
		
		xml = xml.replaceFirst(oldtxt, newtxt);
		xml = xml.replaceFirst("</QUERYRESULTS>", "</tns:QUERYRESULTS>");
		xml = xml.replaceAll("index=", "itemtype=");
		
		return xml;
	}

    public static String formatXML(String text)
    {
    	if (text == null)
    		return "";
    	
        String newText = text;

        char c;
        int length = 0;
        String goodValue;
        // look thru the string
        for (int i = 0; i < newText.length(); i++)
        {
        	// one character at a time
            c = newText.charAt(i);
            
            // is it non letter or digit
            if (isQuoteableCharacter(c))
            {
            	// set the default good value
            	goodValue = "&#x" + Integer.toHexString(c) + ";";
            	
            	// determine the length
            	length = goodValue.length();
            	
            	// substitute the text
                newText = newText.substring(0, i) + goodValue + newText.substring(i + 1);
                
                // skip the rest of the substituted string
                i += length - 1;
            }
        }
        return newText;
    }

    private static boolean isQuoteableCharacter (char c)
    {
    	if (c > 126)
    		return true;
    	
    	return false;
    }	
  /*  public void copyAttributes(String fromObjectId, String fromIndex, String toObjectId, String toIndex, ArrayList copyFields, ArrayList copyValues)
    {
        try
        {
            fromIndex = getContentManager().indexDescriptionToName(fromIndex);
            toIndex = getContentManager().indexDescriptionToName(toIndex);

            String[] fromfields = getContentManager().getIndexFields(fromIndex);
            String[] fromvalues = getContentManager().getIndexValues(fromObjectId);
            String[] newfields = getContentManager().getIndexFields(toIndex);
            Hashtable hashValues = new Hashtable();

            for (int i = 0; ((i < fromfields.length) && (i < fromvalues.length)); i++)
            {
                String fromname = fromfields[i];
                Object fromvalue = fromvalues[i];
                if ((fromvalue != null) && (fromvalue.toString().trim().length() > 0))
                {
                    for (int j = 0; ((newfields != null) && (j < newfields.length)); j++)
                    {
                        // match up the new and old attributes
                        if (fromname.equals(newfields[j]))
                        {
                            // check if the field has been assigned
                            if (!copyFields.contains(newfields[j]))
                            {
                                hashValues.put(newfields[j], fromvalue);
                            }
                        }
                    }
                }
            }

            for (Enumeration e = hashValues.keys(); e.hasMoreElements();)
            {
                String field = (String) e.nextElement();
                String value = (String) hashValues.get(field);
                copyFields.add(field);
                copyValues.add(value);
            }
        } catch (Exception exc)
        {
            Debugger.debug("Error copying item attributes to folder");
            Debugger.debug(exc.toString());
        }
    }

    public void retrieveCreatedDate()
    {
        
        try
        {
            if (createdDate == null)
            {
                Date now = new Date();
                
                // lookup the timezone of the server where the resource objects are stored
                CM.lookupContentManager();
                String RMServerTimezone = ContentManagerConnection.getServerTimezone();

                // get the created date from RMI daemon
                // the origional string was the timezone of the rm server
                // the rmi server thinks is in the timezone of the rmi server
                // JST->JST works fine, but JST RM server -> GMT RMI Server doesn't
                // so it needs to have the timezone offset undone if they don't match
                createdDate = getContentManager().getCreatedDate(getOBJECTID());
                String RMIServerTimezone = ContentManagerConnection.getServerTimezone();
            	Debugger.debug("Created Date Retrieved : " + RegionalDateConverter.formatTimeStamp("GUI", createdDate));
                
                if (! RMIServerTimezone.equals(RMServerTimezone))
                {
                	// didn't retrieve the date from the "right" rmi server
                	// subtract off the offset added by the "wrong" rmi server
                	createdDate.setTime (createdDate.getTime() - TimeZone.getTimeZone(RMServerTimezone).getOffset(createdDate.getTime()));

                	Debugger.debug("RM Server Timezone: " + RMServerTimezone);
                	Debugger.debug("RMI Server Timezone: " + RMIServerTimezone);
                	Debugger.debug("Created Date Adjusted : " + RegionalDateConverter.formatTimeStamp("GUI", createdDate));
                }
                
                Debugger.info("Retrieved Document created date taking (ms) " + (new Date().getTime() - now.getTime()));
                Debugger.info(RegionalDateConverter.formatTimeStamp("GUI", createdDate) + " " + TimeZone.getDefault().getDisplayName());

            }
        } catch (Exception exc)
        {
            Debugger.debug("Error retrieving created date");
            Debugger.debug(exc.toString());
        }

    }*/
    
 /*   public void continueWorkflow()
    {
    	try {
      		getContentManager().completeWorkflow(getOBJECTID(), TOBEINDEXEDWORKFLOW);
    	} catch (Exception exc) {
            Debugger.debug("Error continuing workflow process");
            Debugger.debug(exc.toString());
		}
    }*/
    
    
    
    public boolean isWorkAccess(String userId)
    {
    	String accessLevel = null;
    	String accessLevels = null;
    	String[]  accessArray= null;
    
    	try {
    		ContentManagerImplementation cm  = new ContentManagerImplementation(this.getDatastore());
        	accessLevels = PluginConfigurationPane.getPropertiesMap().get("accessLevelPrivileges");
        	accessArray= accessLevels.split(",");
        	List<String> accesLevelList = Arrays.asList(accessArray);   		
        	accessLevel = cm.getUserPrivilegeSet(userId, this.getDatastore());
     		if(accessLevel != null && accesLevelList.contains(accessLevel)){
     			this.isWorkAcess = true;
    		}

   		 logger.debug("Priviledge Set: " +accessLevel);

    	} catch (Exception exc) {
            logger.fatal("Error retrieving the User Access Level:" + exc.toString());
            exc.printStackTrace();
		} 

    	return this.isWorkAcess;
    }
    

    public void setValues(String[] vValues)
    {
        values = vValues;
    }

    public String[] getValues()
    {
        return values;
    }

/*    public ContentManagerRMIInterface getContentManager() throws Exception
    {
        CM.setCountry(getCOUNTRY());
        return CM.getContentManager();
    }*/

    public String trimUSERID()
    {
        String username = getUSERID();
        int atsign = username.indexOf("@");
        if (atsign > 0)
            username = username.substring(0, atsign);
        if(username.length()>10)
        	username = username.substring(0,10);        

        return username;
    }



    static public void main (String args[])
    {
		String format = "yyyy-MM-dd HH:mm:ss.SSSSSS z";
		String pattern = "####-##-## ##:##:##.###### ZZZ";
		String blankValue = "    -  -     :  :  .          ";
		String defaultValue = "####-##-## ##:##:##.###### ZZZ";
     //   RegionalDateConverter.addTimeStampFormatter("TEST", format, pattern, blankValue, defaultValue);

       // TimeZone userTimezone = TimeZone.getTimeZone("JST");
       // TimeZone.setDefault(userTimezone);
        
    	//RegionManager.setBackendRegion(RegionManager.PRODUCTION);
    	/*GILConsole.getConsole();
    	
    	InvoiceDataModel aIndexingDataModel = new InvoiceDataModel();
    	aIndexingDataModel.setINDEXCLASS("MYInvoice");
    	aIndexingDataModel.setOBJECTID("A1001001A11B11A91229F69319");
    	aIndexingDataModel.loadIndexValues();
    	
    	aIndexingDataModel.debugScanDate(aIndexingDataModel);

		ContentManagerConnection.setContentManager(null, RegionManager.getBackendRegion());
		ContentManagerConnection.simulatePrimaryFailure(true);
		aIndexingDataModel.setCREATEDATE((Date)null);

		aIndexingDataModel.retrieveCreatedDate();
    	aIndexingDataModel.debugScanDate(aIndexingDataModel);

		ContentManagerConnection.setContentManager(null, RegionManager.getBackendRegion());
		ContentManagerConnection.simulateSecondaryFailure(true);
		aIndexingDataModel.setCREATEDATE((Date)null);

		aIndexingDataModel.retrieveCreatedDate();
    	aIndexingDataModel.debugScanDate(aIndexingDataModel);*/

    	 
		//System.exit(0);
    }
    
 
   /* public void debugScanDate(InvoiceDataModel aIndexingDataModel)
    {
    	TimeZone userTimezone = TimeZone.getDefault();
		Date scanDate = aIndexingDataModel.getCREATEDATEasDate();
        Date invoiceDate = aIndexingDataModel.getDate(aIndexingDataModel.getINVOICEDATEidx());

        Debugger.debug("Server URL   : " + ContentManagerConnection.getURL());
        Debugger.debug("Server TZ    : " + ContentManagerConnection.getServerTimezone());
        Debugger.debug("Scan Date    : " + RegionalDateConverter.formatTimeStamp("TEST", scanDate, userTimezone));
        Debugger.debug("Invoice Date : " + RegionalDateConverter.formatTimeStamp("TEST", invoiceDate, userTimezone));
		if (scanDate.getTime() < invoiceDate.getTime()) {
			Debugger.fatal("Scan Date cannot be before the Invoice Date");
		} else {
			Debugger.debug("Scan Date is valid and before the Invoice Date");
		}
    }*/

   /* public DataModel(int initialCapacity)
    {
        super(initialCapacity);

        // add null records
        for (int i = 0; i < initialCapacity; i++)
        {
            add(null);
        }

        // initialize the records
        init();
    }

    public void copy(DataModel aDataModel)
    {
        for (int i = 0; ((i < getColumnCount()) && (i < aDataModel.getColumnCount())); i++)
        {
            set(i, aDataModel.get(i));
        }

        if (aDataModel.getEventController() != null)
        {
            setEventController(aDataModel.getEventController());
        }
    }

   protected void debug(String txt)
    {
        if (getEventController() != null)
            getEventController().debug(txt);
    }

    public void dispose()
    {
        if (eventController != null)
        {
            eventController = null;
        }

        clear();
    }

    public void error(String errorMsg)
    {
        if (getEventController() != null)
            getEventController().error(errorMsg);
    }

    public void fatal(String errorMsg)
    {
        if (getEventController() != null)
            getEventController().fatal(errorMsg);
    }

    public void info(String errorMsg)
    {
        if (getEventController() != null)
            getEventController().info(errorMsg);
    }
    
    public void warn(String errorMsg) {
		if (getEventController() != null)
			getEventController().warn(errorMsg);
	}
    
    public void notify(String errorMsg) {
		if (getEventController() != null)
			getEventController().notify(errorMsg);
	}*/
    
/*    *//**
	 * Insert the method's description here. Creation date: (10/19/2000 3:01:38
	 * PM)
	 * 
	 * @return int
	 *//*
    public int getColumnCount()
    {
        return size();
    }*/

  /*  *//**
     * 
     * @param index
     * @return Date
     *//*
    public java.util.Date getDate(int index)
    {
        return getDate (index, TimeZone.getDefault());
    }
    public java.util.Date getDate(int index, TimeZone tz)
    {
        Object obj = get(index);
        //remove below line
        java.util.Date date = new Date();
        
        if (obj == null)
            return null;
        else if (obj instanceof java.util.Date)
            return (java.util.Date) obj;
        else
        {
            try
            {
                JTextFieldDate.getDateFormatter().setTimeZone(tz);
                return JTextFieldDate.getDateFormatter().parse(obj.toString());
            	return date;
            } catch (Exception exc)
            {
                return null;
            }
        }
    }

    *//**
     * Insert the method's description here. Creation date: (3/12/2001 12:21:44
     * PM)
     * 
     * @return RegionalBigDecimal
     * @param index
     *            int
     *//*
    public RegionalBigDecimal getDecimal(int index)
    {
        Object obj = get(index);
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
    }*/

/*    *//**
     * Insert the method's description here. Creation date: (5/24/2001 12:53:56
     * PM)
     * 
     * @return com.ibm.igf.hmvc.EventController
     *//*
    public EventController getEventController()
    {
        return eventController;
    }*/

    /**
     * Insert the method's description here. Creation date: (3/12/2001 12:21:44
     * PM)
     * 
     * @return java.lang.String
     * @param index
     *            int
     */
/*    public int getInt(int index)
    {
        try
        {
            Object obj = get(index);
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
    }*/

    /**
     * Insert the method's description here. Creation date: (3/12/2001 12:21:44
     * PM)
     * 
     * @return java.lang.String
     * @param index
     *            int
     * @throws CloneNotSupportedException 
     */
/*    public String getString(int index)
    {
        Object obj = get(index);
        if (obj == null)
            return "";
        else if (obj instanceof String)
            return (String) obj;
        else
            return obj.toString();
    }*/

/*    public void init()
    {
        // add blank records
        for (int i = 0; i < getColumnCount(); i++)
        {
            set(i, emptyString);
        }
    }*/

    public Indexing newcopy() throws CloneNotSupportedException
    {
        try
        {
            // use serialization to make a deep copy of the object
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);

            // now read the object off the stream
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
            Indexing theNewCopy = (Indexing) ois.readObject();

/*            if (getEventController() != null)
            {
                theNewCopy.setEventController(getEventController());
            }*/
            return theNewCopy;
        } catch (Exception exc)
        {
            exc.printStackTrace();
            return ((Indexing) this.clone());
        }
    }

    /**
     * Insert the method's description here. Creation date: (3/23/2001 10:16:56
     * AM)
     * 
     * @param fieldIndex
     *            int
     */
/*    public void requestFieldFocus(int fieldIndex)
    {
        if (getEventController() != null)
        {
            getEventController().requestFieldFocus(fieldIndex);
        }
    }*/

    /**
     * Insert the method's description here. Creation date: (5/24/2001 12:53:56
     * PM)
     * 
     * @param newController
     *            com.ibm.igf.hmvc.EventController
     */
/*    public void setEventController(EventController newController)
    {
        eventController = newController;
    }*/

    /**
     * Insert the method's description here. Creation date: (3/12/2001 12:21:44
     * PM)
     * 
     * @return java.lang.String
     * @param index
     *            int
     */
 /*   public void setInt(int index, int value)
    {
        try
        {
            set(index, Integer.toString(value));
        } catch (Exception exc)
        {
        }
        return;
    }*/

    /**
     * Insert the method's description here. Creation date: (8/22/2002 3:05:22
     * PM)
     * 
     * @return java.lang.String
     * @param value
     *            java.lang.String
     */
    public String trimZero(String value)
    {
        if ((value == null) || (value.length() == 0))
            return emptyString;

        int startIdx = 0;
        while ((value.charAt(startIdx) == '0' || value.charAt(startIdx) == ' ') && startIdx < value.length())
        {
            startIdx++;
        }
        int endIdx = value.length() - 1;
        while ((value.charAt(endIdx) == '0' || value.charAt(endIdx) == ' ') && endIdx >= 0)
        {
            endIdx--;
        }

        if (endIdx > 0)
        {
            value = value.substring(startIdx, endIdx + 1);
        } else
        {
            value = emptyString;
        }

        return value;
    }

    /**
     * Insert the method's description here. Creation date: (3/12/2001 12:21:44
     * PM)
     * 
     * @return java.lang.String
     * @param index
     *            int
     */
/*    public String getSqlString(int index)
    {
        Object obj = get(index);
        if (obj == null)
            return DB2.sqlString("");
        else if (obj instanceof String)
            return DB2.sqlString((String) obj);
        else
            return DB2.sqlString(obj.toString());
    }*/

    /**
     * Insert the method's description here. Creation date: (8/22/2002 3:05:22
     * PM)
     * 
     * @return java.lang.String
     * @param value
     *            java.lang.String
     */
    public String removeChar(String value, char aChar)
    {
        if ((value == null) || (value.length() == 0))
            return emptyString;

        if (value.indexOf(aChar) < 0)
        {
            return value;
        }

        byte[] charData = value.getBytes();
        int j = 0;
        for (int i = 0; i < charData.length; i++)
        {
            if (charData[i] != aChar)
            {
                charData[j] = charData[i];
                j++;
            }
        }

        return new String(charData, 0, j);
    }

    public static String[] split(String text, char splitChar)
    {
        ArrayList splits = new ArrayList();
        int startIdx = 0;
        int length = text.length();
        boolean inSingleQuotes = false;
        boolean inDoubleQuotes = false;

        // allocate the string array
        for (int i = 0; i < length; i++)
        {
            if ((text.charAt(i) == '"') && (!inSingleQuotes))
            {
                if (inDoubleQuotes)
                    inDoubleQuotes = false;
                else
                    inDoubleQuotes = true;
            }
            if ((text.charAt(i) == '\'') && (!inDoubleQuotes))
            {
                if (inSingleQuotes)
                    inSingleQuotes = false;
                else
                    inSingleQuotes = true;
            }
            if (((text.charAt(i) == splitChar) && (!inDoubleQuotes) && (!inSingleQuotes)) || (i == length - 1))
            {
                if ((i == length - 1) && ((text.charAt(i) != splitChar)))
                    i++;
                splits.add(text.substring(startIdx, i));
                startIdx = i + 1;
            }
        }

        String[] results = new String[splits.size()];
        for (int i = 0; i < splits.size(); i++)
        {
            results[i] = (String) splits.get(i);
        }
        return (results);
    }

    /**
     * Insert the method's description here. Creation date: (8/22/2002 3:05:22
     * PM)
     * 
     * @return java.lang.String
     * @param value
     *            java.lang.String
     */
    public String trimQuotes(String value)
    {
        if ((value == null) || (value.length() == 0) || value.equals("\"\""))
            return emptyString;

        int startIdx = 0;
        while ((value.charAt(startIdx) == '"' || value.charAt(startIdx) == '\'' || value.charAt(startIdx) == ' ') && startIdx < value.length())
        {
            startIdx++;
        }
        int endIdx = value.length() - 1;
        while ((value.charAt(endIdx) == '"' || value.charAt(startIdx) == '\'' || value.charAt(endIdx) == ' ') && endIdx >= 0)
        {
            endIdx--;
        }

        if (endIdx >= 0)
        {
            value = value.substring(startIdx, endIdx + 1);
        } else
        {
            value = emptyString;
        }

        return value;
    }
    /**
     * Insert the method's description here. Creation date: (3/12/2001 12:21:44
     * PM)
     * 
     * @return RegionalBigDecimal
     * @param index
     *            int
     */
    public RegionalBigDecimal getDecimal(Object obj)
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
    
    
}