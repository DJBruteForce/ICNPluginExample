package com.ibm.gil.business;

import java.io.ByteArrayInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;



import com.google.gson.annotations.Expose;
import com.ibm.gil.model.OfferingLetterDataModelELS;
import com.ibm.gil.util.RegionalBigDecimal;
import com.ibm.gil.util.RegionalDateConverter;
import com.ibm.igf.contentmanager.rmi.ContentManagerImplementation;
import com.ibm.igf.hmvc.ResultSetCache;
import com.ibm.igf.webservice.GCPSCreateOL;
//import com.ibm.igf.webservice.GCPSCreateOL;
//import com.ibm.igf.webservice.GCPSGetOL;
//import com.ibm.igf.webservice.GCPSUpdateOL;
import com.ibm.igf.webservice.GCPSGetOL;
import com.ibm.igf.webservice.GCPSUpdateOL;
import com.ibm.w3.financing.tools.gcms.query.results.DOCUMENT;
import com.ibm.w3.financing.tools.gcms.query.results.DocumentRoot;
import com.ibm.w3.financing.tools.gcms.query.results.QUERYRESULTSType;
import com.ibm.w3.financing.tools.gcms.query.results.util.ResultsResourceUtil;
import com.ibm.xmlns.igf.gcps.updateofferingletterack.Acknowledgement;
import com.ibm.xmlns.igf.gcps.updateofferingletterack.Messages;
import com.ibm.xmlns.ims._1_3.showfinancialofferingletter_2004_11_30.Application;
import com.ibm.xmlns.ims._1_3.showfinancialofferingletter_2004_11_30.ShowFinancialOfferingLetter;
import com.ibm.xmlns.ims._1_3.showfinancialofferingletter_2004_11_30.Status;

public class OfferingLetterELS extends Indexing{
	
		private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(OfferingLetterELS.class);
		
		@Expose private String OFFERINGNUMBER="";
		@Expose private String OLDOFFERINGNUMBER="";
		@Expose private String AMOUNT="";
		@Expose private String BALANCE="";
		@Expose private String BILLENTITYINDICATOR="";
		@Expose private String CURRENCY="";
		@Expose private String CUSTOMERNUMBER="";
		@Expose private String CUSTOMERNAME="";
		@Expose private String CUSTOMERADDRESS1="";
		@Expose private String CUSTOMERADDRESS2="";
		@Expose private String CUSTOMERADDRESS3="";
		@Expose private String TEAM="";
		@Expose private String FOLDERID="";
		@Expose private String INVOICEOBJECTID="";
		@Expose private String GCPSSTATUS="";
		@Expose private String LOCKINDICATOR="";
		@Expose private String SIGNEDCOUNTERSIGNED="";
		@Expose private String RESPONSIBLEPARTYID="";
		@Expose private String VALIDITYSTARTDATE="";
		@Expose private String VALIDITYENDDATE="";
		@Expose private String CUSTOMERSIGNATURE="";
		@Expose  private String CUSTOMERSIGNATUREDATE="";
		@Expose private String DEALTYPE="";
		@Expose private String IBMSIGNATUREDATE="";
		@Expose private String QUOTENUMBER="";
		@Expose private String QUOTEVERSIONNUMBER="";
		@Expose private String ROLLUPINDC="";
		@Expose private String EXTERNALOFFERLETTERNUMBER="";
		@Expose private String SOURCEINDC="";
		@Expose private ArrayList<String> dialogErrorMsgs = new ArrayList<String>();
	 	private ResultSetCache currencyResults = null;
	 	public static transient final String ROFOLITEMTYPE = "ROF Signed Offer Letter";
	    private transient ArrayList quoteLineItems = null;
	    private transient ArrayList rofLineItems = null;
	    private OfferingLetterDataModelELS oLDataModel=null;
	    private boolean validationOfferLetterShouldBeIndexedIntoMyStampduty = false;
	    private boolean validationOfferLetterIncorrectStatusForCSignedOfferLetter = false;
	    private boolean validationOfferLetterIncorrectStatusForSignedOfferLetter = false;
	    private boolean validationOfferletterDeactivatedInGCMS = false;
	    private boolean validationOfferLetterNotFoundInGCMS = false;
	    private boolean validationDuplicateOfferLetterEntered = false;
	    private boolean validationDuplicatedOfferLetterFromCM = false;
	    private boolean validationOfferLetterIncorrectStatusForStampduty = false;
	    private boolean validationIsDocumentProperlyIndexed = false;
	    private ArrayList<OfferingLetterELSFieldOption> listOfFieldOptionValues = new ArrayList<OfferingLetterELSFieldOption>();
	    private String createdTimeStamp = null;
	    private String createOfferingLetterStatusMessage = "";
	    private boolean isGCPSOLCreated = true;
	    /*
	     * web services
	     */
	    private transient static GCPSCreateOL aGCPSCreateOfferingLetter = null;
	    private transient static GCPSGetOL aGCPSGetOfferingLetter = null;
	    

	    public OfferingLetterELS()
	    {
	       
	    }
	    
		public OfferingLetterELS(String country) {
			
			this.setCOUNTRY(country);
			oLDataModel = new OfferingLetterDataModelELS(this);
			
		}

	    public OfferingLetterDataModelELS getoLDataModel() {
	    	if(oLDataModel==null){
	    		this.oLDataModel= new OfferingLetterDataModelELS(this);
	    	}
			
			return oLDataModel;
		}

		public void setoLDataModel(OfferingLetterDataModelELS oLDataModel) {
			this.oLDataModel = oLDataModel;
		}
		
		

		public ArrayList<String> getDialogErrorMsgs() {
			return dialogErrorMsgs;
		}

		public void setDialogErrorMsgs(ArrayList<String> dialogErrorMsgs) {
			this.dialogErrorMsgs = dialogErrorMsgs;
		}

		public String getCreatedTimeStamp() {
			return createdTimeStamp;
		}

		public void setCreatedTimeStamp(String createdTimeStamp) {
			this.createdTimeStamp = createdTimeStamp;
		}
		
	    public Date getCreatedTimeStampAsDate()
	    {
	       if (createdTimeStamp!=null) {
	    	   
	    		 Timestamp timestamp = Timestamp.valueOf(createdTimeStamp);
	    		 return new Date(timestamp.getTime());
	       } else {
	    	   return null;
	       }
	    }

		public ArrayList getQuoteLineItems() {
			return quoteLineItems;
		}



		public void setQuoteLineItems(ArrayList quoteLineItems) {
			this.quoteLineItems = quoteLineItems;
		}



		public ArrayList getRofLineItems() {
			return rofLineItems;
		}



		public void setRofLineItems(ArrayList rofLineItems) {
			this.rofLineItems = rofLineItems;
		}

	    
	    public String getOFFERINGNUMBER()
	    {
	        return this.OFFERINGNUMBER;
	    }

	    public void setOFFERINGNUMBER(String OFFERINGNUMBER)
	    {
		    this.OFFERINGNUMBER=OFFERINGNUMBER;
	    }

	   
	    public String getOLDOFFERINGNUMBER()
	    {
	        return this.OLDOFFERINGNUMBER;
	    }

	    public void setOLDOFFERINGNUMBER(String OLDOFFERINGNUMBER)
	    {
	        this.OLDOFFERINGNUMBER=OLDOFFERINGNUMBER;
	    }

	    public String getAMOUNT()
	    {
	        return this.AMOUNT;
	    }

	    public void setAMOUNT(String AMOUNT)
	    {
	        if (AMOUNT.lastIndexOf('.') == -1)
	        {
	        	AMOUNT = AMOUNT + ".00";
	        }
	        this.AMOUNT=AMOUNT;
	    }

	    public void setAMOUNTasDouble(Double value)
	    {
	        setAMOUNT(new RegionalBigDecimal(value.floatValue()).toString());
	    }

	    public Double getAMOUNTasDouble()
	    {
	        return new Double(getDecimal(getAMOUNT()).doubleValue());
	    }

	    
	    public String getBALANCE()
	    {
	        return this.BALANCE;
	    }

	    public void setBALANCE(String BALANCE)
	    {
	        this.BALANCE=BALANCE;
	    }
	    
	    // story 1497875
	    
	    
	    public String getBILLENTITYINDICATOR()
	    {
	        return this.BILLENTITYINDICATOR;
	    }

	    public void setBILLENTITYINDICATOR(String BILLENTITYINDICATOR)
	    {
	        this.BILLENTITYINDICATOR=BILLENTITYINDICATOR;
	    }
	    // story 1497875


	    public String getCURRENCY()
	    {
	        return this.CURRENCY;
	    }

	    public void setCURRENCY(String CURRENCY)
	    {
	        this.CURRENCY=CURRENCY;
	    }


	    public String getCUSTOMERNUMBER()
	    {
	        return this.CUSTOMERNUMBER;
	    }

	    public void setCUSTOMERNUMBER(String CUSTOMERNUMBER)
	    {
	        this.CUSTOMERNUMBER=CUSTOMERNUMBER;
	    }


	    public String getCUSTOMERNAME()
	    {
	        return this.CUSTOMERNAME;
	    }

	    public void setCUSTOMERNAME(String CUSTOMERNAME)
	    {
	       this.CUSTOMERNAME=CUSTOMERNAME;
	    }

	    public String getCUSTOMERADDRESS1()
	    {
	        return this.CUSTOMERADDRESS1;
	    }

	    public void setCUSTOMERADDRESS1(String CUSTOMERADDRESS1)
	    {
	        this.CUSTOMERADDRESS1=CUSTOMERADDRESS1;
	    }

	    public String getCUSTOMERADDRESS2()
	    {
	        return this.CUSTOMERADDRESS2;
	    }

	    public void setCUSTOMERADDRESS2(String CUSTOMERADDRESS2)
	    {
	        this.CUSTOMERADDRESS2=CUSTOMERADDRESS2;
	    }


	    public String getCUSTOMERADDRESS3()
	    {
	        return this.CUSTOMERADDRESS3;
	    }

	    public void setCUSTOMERADDRESS3(String CUSTOMERADDRESS3)
	    {
	        this.CUSTOMERADDRESS3=CUSTOMERADDRESS3;
	    }


	    public String getTEAM()
	    {
	        return this.TEAM;
	    }

	    public void setTEAM(String TEAM)
	    {
	        this.TEAM=TEAM;
	    }


	    public String getFOLDERID()
	    {
	        return this.FOLDERID;
	    }

	    public void setFOLDERID(String FOLDERID)
	    {
	        this.FOLDERID=FOLDERID;
	    }


	    public String getINVOICEOBJECTID()
	    {
	        return this.INVOICEOBJECTID;
	    }

	    public void setINVOICEOBJECTID(String INVOICEOBJECTID)
	    {
	        this.INVOICEOBJECTID=INVOICEOBJECTID;
	    }


	   private String CONTRACTOBJECTID="";

	    public String getCONTRACTOBJECTID()
	    {
	        return this.CONTRACTOBJECTID;
	    }

	    public void setCONTRACTOBJECTID(String CONTRACTOBJECTID)
	    {
	       this.CONTRACTOBJECTID=CONTRACTOBJECTID;
	    }


	    public String getGCPSSTATUS()
	    {
	        return this.GCPSSTATUS;
	    }

	    public void setGCPSSTATUS(String GCPSSTATUS)
	    {
	        this.GCPSSTATUS=GCPSSTATUS;
	    }

	    public String getLOCKINDICATOR()
	    {
	        return this.LOCKINDICATOR;
	    }

	    public void setLOCKINDICATOR(String LOCKINDICATOR)
	    {
	       this.LOCKINDICATOR=LOCKINDICATOR;
	    }

	    public boolean isLOCKED()
	    {
	        return (getLOCKINDICATOR().equals("Y"));
	    }


	    public String getSIGNEDCOUNTERSIGNED()
	    {
	        return this.SIGNEDCOUNTERSIGNED;
	    }

	    public void setSIGNEDCOUNTERSIGNED(String SIGNEDCOUNTERSIGNED)
	    {
	        this.SIGNEDCOUNTERSIGNED=SIGNEDCOUNTERSIGNED;
	    }


	    public String getRESPONSIBLEPARTYID()
	    {
	        return this.RESPONSIBLEPARTYID;
	    }

	    public void setRESPONSIBLEPARTYID(String RESPONSIBLEPARTYID)
	    {
	        this.RESPONSIBLEPARTYID=RESPONSIBLEPARTYID;
	    }


	    public String getVALIDITYSTARTDATE()
	    {
	        return this.VALIDITYSTARTDATE;
	    }

	    public void setVALIDITYSTARTDATE(String VALIDITYSTARTDATE)
	    {
	        this.VALIDITYSTARTDATE=VALIDITYSTARTDATE;
	    }

	    public void setVALIDITYSTARTDATE(Long value)
	    {
	        try
	        {
	            Date dateValue = new Date(value.longValue());
	            setVALIDITYSTARTDATE(RegionalDateConverter.formatDate("GUI", dateValue));
	        } catch (Exception exc)
	        {
	            setVALIDITYSTARTDATE("");
	        }
	    }



	    public String getVALIDITYENDDATE()
	    {
	        return this.VALIDITYENDDATE;
	    }

	    public void setVALIDITYENDDATE(String VALIDITYENDDATE)
	    {
	       this.VALIDITYENDDATE=VALIDITYENDDATE;
	    }

	    public void setVALIDITYENDDATE(Long VALIDITYENDDATE)
	    {
	        try
	        {
	            Date dateValue = new Date(VALIDITYENDDATE.longValue());
	            setVALIDITYENDDATE(RegionalDateConverter.formatDate("GUI", dateValue));
	        } catch (Exception exc)
	        {
	            setVALIDITYENDDATE("");
	        }
	    }


	    public String getCUSTOMERSIGNATURE()
	    {
	        return this.CUSTOMERSIGNATURE;
	    }

	    public void setCUSTOMERSIGNATURE(String CUSTOMERSIGNATURE)
	    {
	       this.CUSTOMERSIGNATURE=CUSTOMERSIGNATURE;
	    }

	    public String getCUSTOMERSIGNATUREDATE()
	    {
	        return this.CUSTOMERSIGNATUREDATE;
	    }

	    public void setCUSTOMERSIGNATUREDATE(String CUSTOMERSIGNATUREDATE)
	    {
	        this.CUSTOMERSIGNATUREDATE=CUSTOMERSIGNATUREDATE;
	    }

	    public String getDEALTYPE()
	    {
	        return this.DEALTYPE;
	    }

	    public void setDEALTYPE(String DEALTYPE)
	    {
	        this.DEALTYPE=DEALTYPE;
	    }
    
	    public Date getCUSTOMERSIGNATUREDATEasDate()
	    {
	        try
	        {
	            String sigDate = getCUSTOMERSIGNATUREDATE();
	            if (sigDate.trim().length() == 0)
	                return null;

	            Date sigDateAsDate = RegionalDateConverter.parseDate("GUI", sigDate);
	            return (sigDateAsDate);
	        } catch (Exception exc)
	        {
	            return (null);
	        }
	    }

	    public void setCUSTOMERSIGNATUREDATEasDate(Date sigDateAsDate)
	    {
	        try
	        {
	            setCUSTOMERSIGNATUREDATE(RegionalDateConverter.formatDate("GUI", sigDateAsDate));
	        } catch (Exception exc)
	        {
	            setCUSTOMERSIGNATUREDATE("");
	        }
	    }


	    public String getIBMSIGNATUREDATE()
	    {
	        return this.IBMSIGNATUREDATE;
	    }

	    public Date getIBMSIGNATUREDATEasDate()
	    {
	        try
	        {
	            String sigDate = getIBMSIGNATUREDATE();
	            if (sigDate.trim().length() == 0)
	                return null;

	            Date sigDateAsDate = RegionalDateConverter.parseDate("GUI", sigDate);
	            return (sigDateAsDate);
	        } catch (Exception exc)
	        {
	            return (null);
	        }
	    }

	    public void setIBMSIGNATUREDATEasDate(Date sigDateAsDate)
	    {
	        try
	        {
	            setIBMSIGNATUREDATE(RegionalDateConverter.formatDate("GUI", sigDateAsDate));
	        } catch (Exception exc)
	        {
	            setIBMSIGNATUREDATE("");
	        }
	    }

	    
	    public void setIBMSIGNATUREDATE(String IBMSIGNATUREDATE)
	    {
	       this.IBMSIGNATUREDATE=IBMSIGNATUREDATE;
	    }


	    public String getQUOTENUMBER()
	    {
	        return this.QUOTENUMBER;
	    }

	    public void setQUOTENUMBER(String QUOTENUMBER)
	    {
	       this.QUOTENUMBER=QUOTENUMBER;
	    }


	    public String getQUOTEVERSIONNUMBER()
	    {
	        return this.QUOTEVERSIONNUMBER;
	    }

	    public void setQUOTEVERSIONNUMBER(String QUOTEVERSIONNUMBER)
	    {
	        this.QUOTEVERSIONNUMBER=QUOTEVERSIONNUMBER;
	    }

	    

	    public String getROLLUPINDC()
	    {
	        return this.ROLLUPINDC;
	    }

	    public void setROLLUPINDC(String ROLLUPINDC)
	    {
	        this.ROLLUPINDC=ROLLUPINDC;
	    }

	    
	    public String getEXTERNALOFFERLETTERNUMBER()
	    {
	        return this.EXTERNALOFFERLETTERNUMBER;
	    }

	    public void setEXTERNALOFFERLETTERNUMBER(String EXTERNALOFFERLETTERNUMBER)
	    {
	        this.EXTERNALOFFERLETTERNUMBER=EXTERNALOFFERLETTERNUMBER;
	    }

	    public String getSOURCEINDC()
	    {
	        return this.SOURCEINDC;
	    }

	    public void setSOURCEINDC(String SOURCEINDC)
	    {
	        this.SOURCEINDC=SOURCEINDC;
	    }
	    

	    public boolean isDeactivated()
	    {
	        boolean result = false;

	        result = getGCPSSTATUS().equals("ODEACT");

	        return result;
	    }
	    

	    public boolean isRollup()
	    {
	        // TODO Auto-generated method stub
	        return getROLLUPINDC().equals("Y");
	    }


	    public String getROFOFFERLETTERINDICATOR()
	    {
	        // TODO Auto-generated method stub
	        return ((getEXTERNALOFFERLETTERNUMBER().trim().length() == 0) ? "N" : "Y");
	    }

	    public boolean isROFOFFERLETTER()
	    {
	        if (getROFOFFERLETTERINDICATOR().equals("Y"))
	        {
	            return true;
	        }
	        return false;
	    }
	    
	    // GIP277
	    public boolean isRateCard(){
	    	if(getDEALTYPE()!=null && getDEALTYPE().trim().equals("RATE")){
	    		return true;
	    	}
	    	return false;
	    }


	    private GCPSCreateOL getGCPSCreateOfferingLetter()
	    {
	        if (aGCPSCreateOfferingLetter == null)
	        {
	            aGCPSCreateOfferingLetter = new GCPSCreateOL();
	        }
	        return aGCPSCreateOfferingLetter;
	    }


	    private GCPSGetOL getGCPSGetOfferingLetter()
	    {
	        if (aGCPSGetOfferingLetter == null)
	        {
	            aGCPSGetOfferingLetter = new GCPSGetOL();
	        }
	        return aGCPSGetOfferingLetter;
	    }

	    private transient static GCPSUpdateOL aGCPSUpdateOfferingLetter = null;

	    private GCPSUpdateOL getGCPSUpdateOfferingLetter()
	    {
	        if (aGCPSUpdateOfferingLetter == null)
	        {
	            aGCPSUpdateOfferingLetter = new GCPSUpdateOL();
	        }
	        return aGCPSUpdateOfferingLetter;
	    }
	    
	    
	    public boolean loadIndexValues() throws Exception
	    {
	    	logger.info("Loading Indexing Values Started");
	    	
	        try
	        {
	        	ContentManagerImplementation cm  = new ContentManagerImplementation(this.getDatastore());

	            String[] values = null;
	            String[] fields = null;
	            boolean validData = false;

	                try
	                {
	                    fields = cm.getIndexFields(cm.indexDescriptionToName(getINDEXCLASS(),this.getDatastore()));
	                    values = getValues();

	                    if (values == null)
	                    {
	                        values = cm.getIndexValues(getOBJECTID(),this.getDatastore());
	                    }

	                    if ((values != null) && (fields != null) && (values.length > 0) && (fields.length > 0) && (values[0].trim().length() > 0))
	                    {
	                        validData = true;
	                    }
	                } catch (Exception exc) {
	                    logger.fatal(exc.toString());
	                    exc.printStackTrace();
	                    throw exc;
	                }


	            if (validData)
	            {
	                if (getINDEXCLASS().equals(ROFOLITEMTYPE))
	                {
	                    // rof ol has the country as one of the fields
	                    setOFFERINGNUMBER(getFieldMatch("ROF Offer Letter Number", fields, values));
	                    setCOUNTRY(getFieldMatch("Country Code", fields, values));
	                } else
	                {
	                    setOFFERINGNUMBER( getFieldMatch("GCPS OL Number", fields, values));
	                    setCUSTOMERNAME( getFieldMatch("GCPS Customer Name", fields, values));
	                }
	            }
	            
	            logger.info("Fields:" + Arrays.toString(fields));
	            logger.info("Values:" + Arrays.toString(values));

	        } catch (Exception exc)
	        {
	            logger.fatal("Error retrieving Content Manager values:"+exc.toString(),exc);
	            throw exc;
	        }
	        
	        logger.info("Loading Indexing Values Finsihed");

	        return true;

	    }
	    
	    
	    public void createOfferingFolder() throws Exception
	    {
	    	
	    	ContentManagerImplementation cm  = new ContentManagerImplementation(this.getDatastore());
	    	
	        // create offering letter folder if it doesn't exist already
	        String[] fields = { "GCPS OL Number" };
	        String[] values = { getOFFERINGNUMBER() };
	        String folderXML = cm.executeQuery("Offering Letter Folder " + getCOUNTRY(), fields, values, this.getDatastore());
	        String folderId = getObjectId(folderXML);
	        if (folderId == null || folderId.trim().length() == 0)
	        {
	            String[] newfields = { "GCPS OL Number", "GCPS Customer Name", "Customer Number" };
	            String[] newvalues = { getOLDOFFERINGNUMBER(), getCUSTOMERNAME(), getCUSTOMERNUMBER() };
	            folderId = cm.createFolder("Offering Letter Folder " + getCOUNTRY(), newfields, newvalues,this.getDatastore());
	        }

	        setFOLDERID(folderId);
	    }
	    
	    
	    public void selectFolderId() throws Exception
	    {
	    	ContentManagerImplementation cm  = new ContentManagerImplementation(this.getDatastore());
	    	
	        String[] fields = { "GCPS OL Number" };
	        String[] values = { getOFFERINGNUMBER() };
	        /* TODO fix this GB / UK issue */
	        String folderXML = cm.executeQuery("Offering Letter Folder " + getCOUNTRY(), fields, values, this.getDatastore());
	        //String folderXML = getContentManager().executeQuery("Offering Letter
	        // Folder " + "UK", fields, values);
	        String folderId = getObjectId(folderXML);
	        setFOLDERID(folderId);
	    }
	    

	    /**
	     * sync up content manager with any changes from the gui
	     */
	    public void updateIndexValues()
	    {
	    	logger.info("Updating Index Values Started");
	        try
	        {
	        	ContentManagerImplementation cm  = new ContentManagerImplementation(this.getDatastore());
	            String objectId = getOBJECTID();

	            // update the indexes
	            String[] fields = { "GCPS OL Number", "GCPS Customer Name" };
	            String[] values = { getOFFERINGNUMBER(), getCUSTOMERNAME() };
                logger.info("Fields:" + Arrays.toString(fields));
                logger.info("Values:" + Arrays.toString(values));
	            if (objectId.trim().length() > 0)
	            {

	               boolean rc = cm.updateDocument(objectId, fields, values, this.getDatastore());

	            }
	        } catch (Exception exc)
	        {
	            logger.error("Error setting Content Manager index values",exc);
	        }
	        
	        logger.info("Updating Index Values Finished");
	    }

	    /**
	     * sync up content manager with any changes from the gui
	     */
	    public void updateRelatedIndexValues()
	    {
	    	
	    	logger.info("Updating Related Index Values Started");
	    	
	        // update the indexes in a seperate thread
	    	final OfferingLetterELS olELS = this;
	    	final ContentManagerImplementation cm  = new ContentManagerImplementation(this.getDatastore());
	        Thread runner = new Thread() {
	            public void run()
	            {
	                try
	                {
	                    // update the indexes
	                    String[] fields = { "GCPS OL Number" };
	                    String[] values = { getOFFERINGNUMBER() };
		                logger.info("Fields:" + Arrays.toString(fields));
		                logger.info("Values:" + Arrays.toString(values));
	                    if (getCONTRACTOBJECTID().trim().length() > 0)
	                    {
	                    	cm.updateDocument(getCONTRACTOBJECTID(), fields, values, olELS.getDatastore());
	                    }
	                    if (getINVOICEOBJECTID().trim().length() > 0)
	                    {
	                    	cm.updateDocument(getINVOICEOBJECTID(), fields, values, olELS.getDatastore());
	                    }
	                } catch (Exception exc)
	                {
	                    logger.error("Error setting Content Manager index values",exc);
	                }
	                

	            }
	            
	            
	        };
	        runner.start();
	        
	        logger.info("Updating Related Index Values Finished");
	    }
	    
	    
	    
	    
	    /**
	     * create a select statement for retieving the Offering control data by
	     * Offering number / country code
	     * 
	     * @return ArrayList
	     */
	    public ArrayList selectByOfferingStatement()
	    {
	        
	        logger.info("Retrieving the offering letter by offering letter number");
	        
	        OfferingLetterELS aOLDM = new OfferingLetterELS();
	        aOLDM.setOFFERINGNUMBER(getOFFERINGNUMBER());
	        aOLDM.generateUNIQUEREQUESTNUMBER();
	        aOLDM.setGcmsEndpoint(getGcmsEndpoint());
	        ArrayList offeringLetterList = getGCPSGetOfferingLetter().getOfferingLetter(aOLDM);
	        return offeringLetterList;
	    }
	    


	    /**
	     * call the create offerletter web service by Offering number / country code
	     * 
	     * @return ArrayList
	     * @throws Exception 
	     */
	    public boolean createOfferingStatement() throws Exception   {
	    	
	    	 logger.info("Calling create offering letter web service");
	    	 
	    	ShowFinancialOfferingLetter  showFinancialOfferingLetter = getGCPSCreateOfferingLetter().createOfferingLetter(this);
	    	
	    	if (showFinancialOfferingLetter!=null) {
	    		if (showFinancialOfferingLetter.getApplicationArea()!=null){
	    			
	    			for(Iterator<Application> i = showFinancialOfferingLetter.getApplicationArea().getApplication().iterator(); i.hasNext(); ) {
	    				Application app = (Application)i.next();
	    				for(Iterator<Status> x = app.getApplicationStatus().iterator(); x.hasNext(); ) {
	    					Status status =	(Status)x.next();
	    					if (!status.getErrorCode().getValue().equals("gcps.messages.service.auto.create.offer.letter.success") ){
	    						createOfferingLetterStatusMessage = "ROF Offer Letter creation failed:" + status.getErrorDescription();
	    						isGCPSOLCreated = false;
	    					} else {
	    						isGCPSOLCreated = true;
	    						createOfferingLetterStatusMessage = "ROF Offer Letter will be created.";
	    					}
	    				}
	    				
	    			}
	    		}
	    			
	    	}

	        return true;

	    }

	    /**
	     * create a select statement for retieving the Offering control data by
	     * object id Creation date: (3/13/00 11:44:07 AM)
	     * 
	     * @return ResultSet
	     */
	    public ArrayList<OfferingLetterELS> selectByObjectIdStatement()
	    {
	    	logger.info("Retrieving the offering letter by object id and country");
	    	 
	    	OfferingLetterELS aOLDM = new OfferingLetterELS();
	        aOLDM.setOBJECTID(getOBJECTID());
	        aOLDM.setCOUNTRY(getCOUNTRY());
	        aOLDM.generateUNIQUEREQUESTNUMBER();
	        aOLDM.setGcmsEndpoint(getGcmsEndpoint());
	        ArrayList<OfferingLetterELS> offeringLetterList = getGCPSGetOfferingLetter().getOfferingLetter(aOLDM);
	        return offeringLetterList;
	    }
	    
	    
	    /*
	     * retrieving the invoice control data by the object id as well as invoice
	     * number and date
	     */
	    public ArrayList<OfferingLetterELS> selectMatchingOfferingLetters() throws SQLException
	    {
	    	logger.info("Retrieving matching Offering Letters");
	    	
	    	OfferingLetterELS aOLDM = new OfferingLetterELS();
	        aOLDM.setOBJECTID(getOBJECTID());
	        aOLDM.setOFFERINGNUMBER(getOFFERINGNUMBER());
	        aOLDM.setCOUNTRY(getCOUNTRY());
	        aOLDM.setUSERID(getUSERID());
	        aOLDM.generateUNIQUEREQUESTNUMBER();
	        aOLDM.setGcmsEndpoint(getGcmsEndpoint());

	        if (aOLDM.getCOUNTRY().equals("UK"))
	        {
	            aOLDM.setCOUNTRY("GB");
	        }

	        ArrayList results = null;

	        try
	        {
	            if (getOFFERINGNUMBER().trim().length() <= 6)
	            {
	                results = getGCPSGetOfferingLetter().getOfferingLetter(aOLDM);
	            }
	        } catch (Exception exc)
	        {
	            logger.fatal(exc.toString());
	        }

	        if (results == null) {
	        	logger.info("No Offering Letters returned");
	            return new ArrayList();
	        }
	        else {
	        	logger.info(results.size() + " results returned");
	            return results;
	        }

	    }
	    
	    /*
	     * load the field options from the database and enable/disable the gui
	     *  
	     */
	    private void initializeFieldOptions(boolean isCountersigned) throws Exception
	    {
	        try
	        {
	        	OfferingLetterELSFieldOption fieldOption = null;
	        	
	            OfferingLetterDataModelELS aDataModel = (OfferingLetterDataModelELS) getoLDataModel();
	          //  OfferingLetterFrame aViewFrame = ((OfferingLetterFrame) getViewFrame());

	            // load the field options
	            ResultSetCache cachedResults = aDataModel.selectFieldOptionsStatement();
	            while (cachedResults.next())
	            {
	            	fieldOption = new OfferingLetterELSFieldOption();
	            	fieldOption.setCountersigned(isCountersigned);
	            	fieldOption.setField( cachedResults.getString(1));
	            	fieldOption.setOption(cachedResults.getString(2));
	            	listOfFieldOptionValues.add(fieldOption);
	            	// aViewFrame.setFieldOption(isCountersigned, cachedResults.getString(1), cachedResults.getString(2));
	            }

	        } catch (Exception exc)
	        {
	           logger.fatal("Error initializing Field Options:"+ exc.toString());
	           throw exc;
	        }
	    }
	    
	    
	    /**
	     * load the associated document data from the database if it exists
	     */
	    private boolean initializeDatabaseValues()
	    {// initialize the offering letter values from the db
	        try
	        {
		    	
	            // load the offering letter if it exists
	            ArrayList results = this.selectMatchingOfferingLetters();

	            // find matching ol
	            OfferingLetterELS anOfferLetter;
	            OfferingLetterELS retrievedOL = null;
	            OfferingLetterELS OLNumMatch = null;
	            OfferingLetterELS OLNumAndObjMatch = null;
	            for (int i = 0; i < results.size(); i++)
	            {
	                anOfferLetter = (OfferingLetterELS) results.get(i);
	                
	                anOfferLetter.setDatastore(this.getDatastore());

	                if (anOfferLetter.getOFFERINGNUMBER().equals(this.getOFFERINGNUMBER()))
	                {
	                    OLNumMatch = anOfferLetter;
	                    if (anOfferLetter.getOBJECTID().equals(this.getOBJECTID()))
	                    {
	                        //this is a re-index
	                        OLNumAndObjMatch = anOfferLetter;
	                    } else {
	                    	// same ol # but different object id
	                    	// make sure the object id is the previous cm ol document:   IP1E OL PDF -> Sign OL -> Countersigned OL
	                    	
	                    	// retrieve the old image from cm
	                    	DOCUMENT cmdoc = anOfferLetter.queryDocument();
	                        if (cmdoc != null)
	                        {
	                        	String oldIndexClass = cmdoc.getItemtype();
	                        	String indexClass = this.getINDEXCLASS();
	                        	boolean isValidIndexPath = true;
	                        	if (indexClass.endsWith("CountersignOL"))
	                        	{
	                        		// old item type must be sign ol, rof ol, or ol pdf
	                        		if ((! oldIndexClass.startsWith("GCPS_SignOL")) && (! oldIndexClass.startsWith("ROF_SIGNED_OL")) && (! oldIndexClass.endsWith("OL_PDF")))
	                        		{
	                        			isValidIndexPath = false;
	                        		}
	                        	}
	                        	if (indexClass.endsWith("SignOL"))
	                        	{
	                        		// old item type must be PDF
	                        		if (! oldIndexClass.endsWith("OL_PDF"))
	                        		{
	                        			isValidIndexPath = false;
	                        		}
	                        	}
	                        	
	                        	// check the indexing path
	                        	if (!isValidIndexPath)
	                        	{
	                                //this object id is on another OL in GCPS
	                                logger.error("Duplicate Offer Letter retrieved from Content Manager");
	                        		validationDuplicatedOfferLetterFromCM = true;
	                                return false;
	                        	}
	                        }
	                    }
	                } else if (anOfferLetter.getOBJECTID().equals(this.getOBJECTID()))
	                {
	                    //this object id is on another OL in GCPS
	                    logger.info("Duplicate Offer Letter Number entered");
	                    validationDuplicateOfferLetterEntered = true;
	                    return false;
	                }
	            }

	            if (OLNumAndObjMatch != null)
	            {
	                retrievedOL = OLNumAndObjMatch;
	            } else if (OLNumMatch != null)
	            {
	                retrievedOL = OLNumMatch;
	            }

	            if (retrievedOL == null)
	            {
	                logger.info("Offer Letter not found in GCMS");
	                validationOfferLetterNotFoundInGCMS = true;
	                return false;
	            } else
	            {
	                this.setDOCUMENTMODE(Indexing.UPDATEMODE);
	                this.setDIRTYFLAG(Boolean.FALSE);

	                // store the new offering letter number
	                String newOfferingLetterNumber = this.getOFFERINGNUMBER();
	                String oldOfferingLetterNumber = retrievedOL.getOFFERINGNUMBER();

	                // preserve index fields
	                String customerName = this.getCUSTOMERNAME();
	                String customerNumber = this.getCUSTOMERNUMBER();

	                // copy over all the fields
	                //aDataModel.copy (retrievedOL);
	                this.setAMOUNT(retrievedOL.getAMOUNT());
	                this.setCURRENCY(retrievedOL.getCURRENCY());

	                this.setCUSTOMERNAME(retrievedOL.getCUSTOMERNAME());
	                this.setCUSTOMERNUMBER(retrievedOL.getCUSTOMERNUMBER());
	                this.setGCPSSTATUS(retrievedOL.getGCPSSTATUS());
	                this.setLOCKINDICATOR(retrievedOL.getLOCKINDICATOR());
	                this.setCUSTOMERSIGNATURE(retrievedOL.getCUSTOMERSIGNATURE());
	                this.setCUSTOMERSIGNATUREDATE(retrievedOL.getCUSTOMERSIGNATUREDATE());
	                this.setIBMSIGNATUREDATE(retrievedOL.getIBMSIGNATUREDATE());

	                logger.info("Locked Indicator:" + this.getLOCKINDICATOR());

	                if (this.isLOCKED())
	                {
	                    //error("This Offer Letter is locked in GCMS, please unlock
	                    // and try later.");
	                }

	                logger.info("Status:" + this.getGCPSSTATUS());
	                if (this.isDeactivated())
	                {
	                    logger.error("Offer Letter has been deactivated in GCMS");
	                    validationOfferletterDeactivatedInGCMS = true;
	                    return false;
	                }
	                if (this.getINDEXCLASS().endsWith("SignOL"))
	                {
	                    logger.info("Signed Offer Letter");
	                    this.setSIGNEDCOUNTERSIGNED("SIGNED");
	                    if (!(this.getGCPSSTATUS().equals("OSENT") || this.getGCPSSTATUS().equals("OAVAIL")))
	                    {
	                        logger.info("Offer Letter has incorrect status for Signed Offer Letter");
	                        validationOfferLetterIncorrectStatusForSignedOfferLetter = true;
	                        return false;
	                    }
	                    initializeFieldOptions(false);
	                } else if (this.getINDEXCLASS().endsWith("CountersignOL"))
	                {
	                    logger.info("CounterSigned OfferLetter");
	                    this.setSIGNEDCOUNTERSIGNED("COUNTERSIGNED");
	                    
	                    if ((this.getCOUNTRY().equals("MY")) && (this.getGCPSSTATUS().equals("OCOMP")))
	                    {
	                        logger.info("Offer Letter should be indexed into 'MY Stampduty OL' item type.");
	                        validationOfferLetterShouldBeIndexedIntoMyStampduty = true;
	                        return false;
	                    }
	                    
	                    if (! (this.getGCPSSTATUS().equals("OWAITCS") || (this.getGCPSSTATUS().equals("OSCS"))))
	                    {
	                        logger.info("Offer Letter has incorrect status for Countersigned Offer Letter");
	                        validationOfferLetterIncorrectStatusForCSignedOfferLetter = true;
	                        return false;
	                    }

	                    initializeFieldOptions(true);
	                } else if (this.getINDEXCLASS().endsWith("StampdutyOL"))
	                {
	                    logger.debug("XXStampdutyOL");
	                    this.setSIGNEDCOUNTERSIGNED("COUNTERSIGNED");
	                    if (!this.getGCPSSTATUS().equals("OCOMP"))
	                    {
	                        logger.info("Offer Letter has incorrect status for Stampduty Offer Letter");
	                        validationOfferLetterIncorrectStatusForStampduty = true;
	                        return false;
	                    }
	                    initializeFieldOptions(true);
	                }

	                // update special fields
	                this.setOLDOFFERINGNUMBER(oldOfferingLetterNumber);

	                // check to see if the country or number have changed
	                if (!newOfferingLetterNumber.trim().equals(oldOfferingLetterNumber.trim()))
	                {
	                	
	                    // verify with the user that this is what they wanted to do
/*	                    if (prompt("Changing Offer Letter Number.  Are you sure?"))
	                    {
	                        // yes selected
	                    	this.setDIRTYFLAG(Boolean.TRUE);
	                    	this.setOFFERINGNUMBER(newOfferingLetterNumber);
	                    }*/
	                }
	            }
	            return true;

	        } catch (Exception exc)
	        {
	        	logger.error("Error initializing Offering Letter for ELS");
	            logger.fatal(exc);
	            return false;
	        }
	        }

	    
	    /**
	     * Setup the gui for indexing the document
	     * @throws Exception 
	     */
	    public void initalizeIndexing() throws Exception {

		    	if(this.loadIndexValues()) {	
		    	   if (!this.initializeDatabaseValues())  {
		 	          this.rollbackIndexing();
		 	        }	
		    	}
	    	
	    }
	    
	    public void initalizeROFIndexing() throws Exception {
	    	
	    	this.createOfferingStatement();

	    }
 
	    public void rollbackIndexing()
	    {
	        // load the Offering if it exists
	        try
	        {

	            ArrayList<OfferingLetterELS> results = this.selectByObjectIdStatement();
	            if (results != null && results.size() > 0)
	            {
	            	OfferingLetterELS offeringLetterEls = (OfferingLetterELS) results.get(0);
	                // restore the old fields
	                setOFFERINGNUMBER(offeringLetterEls.getOFFERINGNUMBER());
	                setCUSTOMERNAME(offeringLetterEls.getCUSTOMERNAME());
	                setCUSTOMERNUMBER(offeringLetterEls.getCUSTOMERNUMBER());
	            }
	        } catch (Exception exc)
	        {
	            logger.fatal("Error retrieving document's database values:" + exc.toString());
	        
	        }
	        updateIndexValues();
	    }
	    
	    public DOCUMENT queryDocument() throws Exception
	    
	    {
	    	
	    	ContentManagerImplementation cm  = new ContentManagerImplementation(this.getDatastore());
	    	
	    	if (getOBJECTID().trim().length() == 0)
	    	{
	    		return null;
	    	}
	        // query the item
	        logger.info("Searching for document in Content Manager");
	        String xml = cm.executeQuery("*", "[@ITEMID=\"" + getOBJECTID() + "\"]",getDatastore());
	        String indexName = cm.indexDescriptionToName(getINDEXCLASS(),getDatastore());
	        
	        if (xml == null)
	        	return null;
	        if (xml.indexOf(indexName) < 0)
	        	return null; 
	        if (xml.indexOf(getOBJECTID()) < 0)
	        	return null;
	 
	        xml = fixVersion(xml);
	        xml = formatXML(xml);
	               
			ByteArrayInputStream bais = new ByteArrayInputStream(xml.getBytes());

			// load the document
			ResultsResourceUtil util = new ResultsResourceUtil();
			DocumentRoot document = util.load(bais);

			// retrieve the configuration
			QUERYRESULTSType results = document.getQUERYRESULTS();

			List<DOCUMENT> docs = results.getDOCUMENT();
			for (Iterator<DOCUMENT> itr = docs.iterator(); itr.hasNext();) {
				DOCUMENT aDOCUMENT = itr.next();
				return aDOCUMENT;
			}

			return null;
	        
	    }
	    
	    
	    public void validateInput()
	    {
	        boolean exists = false;
	        try
	        {
	            DOCUMENT cmdoc = this.queryDocument();
	            if (cmdoc != null)
	            {
	                exists = true;
	                logger.debug("document exists:"+cmdoc.toString());
	            }
	        } catch (Exception exc)
	        {
	            exists = false;
	        }
	    
	        validationIsDocumentProperlyIndexed = exists;
	    }
	    
	    public void saveDocument() throws Exception {
	    	
	    	if(save()) {
	    		updateIndexValues();
	    		updateRelatedIndexValues();
	    	}
	    	
	    }

	    public boolean save() throws Exception {
	    	
    	
	        //retrieveCreatedDate();
	        generateUNIQUEREQUESTNUMBER();

	        // if it is stampduty, don't save
	        if (getSIGNEDCOUNTERSIGNED().equals("STAMPDUTY")) {
	        	
	            this.getoLDataModel().continueWorkflow();
	            setDIRTYFLAG(Boolean.FALSE);
	            setDOCUMENTMODE(UPDATEMODE);
	            return true;
	        }
	        
	        Acknowledgement ack = getGCPSUpdateOfferingLetter().updateOL(this);
        
	         if (ack!=null &&  ack.getDataArea().getSuccessIndicator().equals("Y")) {

	        	
		         	//Story 1640938: GIL - changes needed for GCMS JR 
		         	if (getINDEXCLASS().endsWith("CountersignOL")){ 
		         		try
		             	{            		
		         			if(GetHybridCountry()){   
		         				if(selectCountersignOfferingLetter()){	
		         					if(GetOLbyCountry()){
		         						getoLDataModel().updateOfferingStatement();
		         					}else{
		         						getoLDataModel().insertOfferingStatement();
		         					}
		         				} 
		         			}
		             	} catch (Exception e1){
		    	            logger.fatal("Error on GCMJ Jr. action:"+e1.toString());
		    	            logger.error(e1);
		             	}
		         	}
		            	//End Story 1640938
	        	
	            // remove items from the to be index worklist
	        	this.getoLDataModel().continueWorkflow();
	            setDIRTYFLAG(Boolean.FALSE);
	            setDOCUMENTMODE(UPDATEMODE);
	            return true;

	        } else {   
	        	
	        	getErrorMessages(ack);
	        	return false;
	        }
	         
	    }
	    
		  //Story 1640938: GIL - changes needed for GCMS JR 
	    private boolean GetOLbyCountry()
	    {
	        try
	        {
	        	// load the field options 
	            ResultSet Results = getoLDataModel().selectOLStatement();            
	            if(Results.next()){
	            	return true;
	            }
	        } catch (Exception exc) {
	            logger.fatal("Error getting Hybrid OL created:"+exc.toString());
	            logger.error(exc);
	        }
	        return false;
	    }
	  //end Story 1640938:
	    
	    //Story 1640938: GIL - changes needed for GCMS JR
	    public boolean selectCountersignOfferingLetter() throws Exception  {
	    	
	    	ContentManagerImplementation cm  = new ContentManagerImplementation(this.getDatastore());
	    	
	        String[] fields = { "GCPS OL Number" };
	        String[] values = { getOFFERINGNUMBER() };
	        String objectXML = cm.executeQuery(getCOUNTRY()+"CountersignOL", fields, values,this.getDatastore());
	        String objectId = getObjectId(objectXML);
	        
	        if (objectId.trim().length() > 0){
	        	return true; 
	        }else{
	        	return false; 
	        }
	    }
	    //End Story 1640938
	    
	    //Story 1640938: GIL - changes needed for GCMS JR
	    private boolean GetHybridCountry()    
	    {
	        try
	        {
	        	// load the field options
	            String Hcountry;
	            ResultSetCache cachedResults = getoLDataModel().selectFieldhybridStatement();
	            while (cachedResults.next()){
	            	Hcountry = cachedResults.getString(1);
	            	if (Hcountry.equals(getCOUNTRY())){
	            		return true; 
	            	}                          
	            }
	        } catch (Exception exc){
	            logger.fatal("Error getting hybrid country:"+exc.toString());
	            logger.error(exc);
	        }
	        return false;
	    }

	    void getErrorMessages(Acknowledgement ack) {
	    	
        	List<Messages> listMessages = ack.getApplicationArea().getMessages();
            
        	if(listMessages!=null){
        		Iterator<Messages> itListMessages = listMessages.iterator();
        		while(itListMessages.hasNext()) {
        			
        			Messages msg = (Messages)itListMessages.next();
        			dialogErrorMsgs.add("Error saving Offer Letter: "+ msg.getReturnMessage());
        			logger.error("Error saving Offer Letter: "+ msg.getReturnMessage());
        		}
        		
          
        	}
	    	
	    }
	    
}
