/*
 * Created on Jun 6, 2017
 *
 * Data Model class for COA Index
 * 
 */
package com.ibm.gil.business;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;



import com.google.gson.annotations.Expose;
import com.ibm.gil.model.COADataModelELS;
import com.ibm.gil.util.RegionalBigDecimal;
import com.ibm.gil.util.RegionalDateConverter;
import com.ibm.igf.contentmanager.rmi.ContentManagerImplementation;
import com.ibm.igf.hmvc.ResultSetCache;
import com.ibm.igf.webservice.GCPSGetCOA;
import com.ibm.igf.webservice.GCPSUpdateCOA;
import com.ibm.w3.financing.tools.gcms.query.results.DOCUMENT;
import com.ibm.w3.financing.tools.gcms.query.results.DocumentRoot;
import com.ibm.w3.financing.tools.gcms.query.results.QUERYRESULTSType;
import com.ibm.w3.financing.tools.gcms.query.results.util.ResultsResourceUtil;
import com.ibm.xmlns.igf.gcps.updatecoaack.Acknowledgement;
import com.ibm.xmlns.igf.gcps.updatecoaack.Messages;


public class COAELS  extends Indexing {
	
	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(COAELS.class);

    @Expose private String COANUMBER;
    @Expose private String OLDCOANUMBER;
    @Expose private String AMOUNT;
    @Expose private String CURRENCY;
    @Expose private String CUSTOMERNAME;
    @Expose private String CUSTOMERNUMBER;
    @Expose private String OFFERINGLETTER;
    @Expose private String OFFERINGLETTERVALIDITYDATE;
    private long OFFERINGLETTERVALIDITYDATELaslong;
    @Expose private String FOLDERID;
    @Expose private String TEAM;
    @Expose private String OFFERINGLETTERID;
    @Expose private String LOCKINDICATOR;
    @Expose private String SIGNEDBY;
    @Expose  private String COASTATUS;
    @Expose private String createdTimeStamp;
    private ResultSetCache currencyResults = null;
    private COADataModelELS coaDataModelELS = null;
    @Expose private ArrayList<String> dialogErrorMsgs = new ArrayList<String>();
    private boolean validationIsDuplicateCOAFromCM = false;
    private boolean validationIsDuplicateCOANumberEntered = false;
    private boolean validationIsCoaNotFoundInGCMS = false;
    private boolean validationIsCoaLockedInGCMS = false;
    private boolean validationIsCoaDeactivatedInGCMS = false;
    private boolean validationIsPendingPricingApprovalStatus = false;
    private boolean validationIsIncorrectStatusForCoa = false;
    private boolean validationIsDocumentProperlyIndexed= true;
    
    /*
     * web services
     */
    private transient GCPSGetCOA aGCPSGetCOA = null;
    
    public COAELS()
    {
        setAMOUNT(RegionalBigDecimal.ZERO.setScale(2).toString());
    }
    

	public COAELS(String country) {
		
		this.setCOUNTRY(country);
		coaDataModelELS = new COADataModelELS(this);
		
	}


	public COADataModelELS getCoaDataModelELS() {
		
    	if (coaDataModelELS == null){
    		return new COADataModelELS(this);
    	} else {
    		return coaDataModelELS;
    	}
	}
	
	public void setCoaDataModelELS(COADataModelELS coaDataModelELS) {
		this.coaDataModelELS = coaDataModelELS;
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
    

	public String getCOANUMBER() {
		return COANUMBER;
	}

	public void setCOANUMBER(String cOANUMBER) {
		COANUMBER = cOANUMBER;
	}

	public String getOLDCOANUMBER() {
		return OLDCOANUMBER;
	}

	public void setOLDCOANUMBER(String oLDCOANUMBER) {
		OLDCOANUMBER = oLDCOANUMBER;
	}

	public String getAMOUNT() {
		return AMOUNT;
	}

	public void setAMOUNT(String aMOUNT) {
		AMOUNT = aMOUNT;
	}

	public String getCURRENCY() {
		return CURRENCY;
	}

	public void setCURRENCY(String cURRENCY) {
		CURRENCY = cURRENCY;
	}

	public String getCUSTOMERNAME() {
		return CUSTOMERNAME;
	}

	public void setCUSTOMERNAME(String cUSTOMERNAME) {
		CUSTOMERNAME = cUSTOMERNAME;
	}

	public String getCUSTOMERNUMBER() {
		return CUSTOMERNUMBER;
	}

	public void setCUSTOMERNUMBER(String cUSTOMERNUMBER) {
		CUSTOMERNUMBER = cUSTOMERNUMBER;
	}

	public String getOFFERINGLETTER() {
		return OFFERINGLETTER;
	}

	public void setOFFERINGLETTER(String oFFERINGLETTER) {
		OFFERINGLETTER = oFFERINGLETTER;
	}

	public String getOFFERINGLETTERVALIDITYDATE() {
		return OFFERINGLETTERVALIDITYDATE;
	}
	
    public boolean isLOCKED()
    {
        return (getLOCKINDICATOR().equals("Y"));
    }


	public void setOFFERINGLETTERVALIDITYDATE(String oFFERINGLETTERVALIDITYDATE) {
		OFFERINGLETTERVALIDITYDATE = oFFERINGLETTERVALIDITYDATE;
	}

	public String getFOLDERID() {
		return FOLDERID;
	}

	public void setFOLDERID(String fOLDERID) {
		FOLDERID = fOLDERID;
	}

	public String getTEAM() {
		return TEAM;
	}

	public void setTEAM(String tEAM) {
		TEAM = tEAM;
	}

	public String getOFFERINGLETTERID() {
		return OFFERINGLETTERID;
	}

	public void setOFFERINGLETTERID(String oFFERINGLETTERID) {
		OFFERINGLETTERID = oFFERINGLETTERID;
	}

	public String getLOCKINDICATOR() {
		return LOCKINDICATOR;
	}

	public void setLOCKINDICATOR(String lOCKINDICATOR) {
		LOCKINDICATOR = lOCKINDICATOR;
	}

	public String getSIGNEDBY() {
		return SIGNEDBY;
	}

	public void setSIGNEDBY(String sIGNEDBY) {
		SIGNEDBY = sIGNEDBY;
	}

	public String getCOASTATUS() {
		return COASTATUS;
	}

	public void setCOASTATUS(String cOASTATUS) {
		COASTATUS = cOASTATUS;
	}

	public void setAMOUNTasRegionalBigDecimal(RegionalBigDecimal value)
    {
        setAMOUNT(value.setScale(RegionalBigDecimal.getDefaultScale(), BigDecimal.ROUND_HALF_UP).toString());
    }


    public void setAMOUNTasBigDecimal(BigDecimal value)
    {
        setAMOUNTasRegionalBigDecimal(new RegionalBigDecimal(value.doubleValue()));
    }

   
    
    public boolean isDeactivated()
    {
        boolean result = false;

        result = getCOASTATUS().equals("CDEACT");

        return result;
    }
    public Long getOFFERINGLETTERVALIDITYDATEasLong()
    {
        try
        {
            String offeringDate = getOFFERINGLETTERVALIDITYDATE();
            Date offeringDateAsDate = RegionalDateConverter.parseDate("GUI", offeringDate);
            return (new Long(offeringDateAsDate.getTime()));
        } catch (Exception exc)
        {
            logger.error(exc,exc);
        	return (null);
        }
    }

    public void setOFFERINGLETTERVALIDITYDATEasLong(Long offeringDateAsLong)
    {
        try
        {
        	this.OFFERINGLETTERVALIDITYDATELaslong = offeringDateAsLong;
            Date offeringDateAsDate = new Date(offeringDateAsLong.longValue());
            //RegionalDateConverter.
            setOFFERINGLETTERVALIDITYDATE(RegionalDateConverter.formatDate("GUI", offeringDateAsDate,TimeZone.getTimeZone("GMT")));
        } catch (Exception exc)
        {
            setOFFERINGLETTERVALIDITYDATE("");
        }
    }
    
    public boolean loadIndexValues() throws Exception
    {
    	
    	logger.info("Loading Indexing Values Started");
        try
        {

            String[] values = null;
            String[] fields = null;
            boolean validData = false;
            
            ContentManagerImplementation cm  = new ContentManagerImplementation(this.getDatastore());

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


            if (validData)
            {
            	setCOANUMBER( getFieldMatch("GCPS COA Number", fields, values));
            	setCUSTOMERNAME(getFieldMatch("GCPS Customer Name", fields, values));
            }
            
            logger.info("Fields:" + Arrays.toString(fields));
            logger.info("Values:" + Arrays.toString(values));
            
        } catch (Exception exc)
        {
            logger.fatal("Error retrieving Content Manager values:" + exc.toString(),exc);
            throw exc;
        }
        logger.info("Loading Indexing Values Finished");

        return true;
    }



    private GCPSGetCOA getGCPSGetCOA()
    {
        if (aGCPSGetCOA == null)
        {
            aGCPSGetCOA = new GCPSGetCOA();
        }
        return aGCPSGetCOA;
    }

    private transient static GCPSUpdateCOA aGCPSUpdateCOA = null;

    private GCPSUpdateCOA getGCPSUpdateCOA()
    {
        if (aGCPSUpdateCOA == null)
        {
            aGCPSUpdateCOA = new GCPSUpdateCOA();
        }
        return aGCPSUpdateCOA;
    }

    
    public void saveDocument() throws Exception
    {
    	if (save()) {
    		updateIndexValues();
    	}
    }
    
    

    public boolean save() throws Exception
    {
        generateUNIQUEREQUESTNUMBER();
        Acknowledgement ack = getGCPSUpdateCOA().updateCOA((this));
        
        if (ack!=null &&  ack.getDataArea().getSuccessIndicator().equals("Y")) {
        	
            getCoaDataModelELS().continueWorkflow(getCOUNTRY(), getOBJECTID(), getTOBEINDEXEDWORKFLOW(), getDatastore());
            setDIRTYFLAG(Boolean.FALSE);
            setDOCUMENTMODE(UPDATEMODE);
            return true;
            
        } else {
        	
        	getErrorMessages(ack);
            return false;
        }
    	
    }
    
    
    void getErrorMessages(Acknowledgement ack) {
    	
    	List<Messages> listMessages = ack.getApplicationArea().getMessages();
        
    	if(listMessages!=null){
    		Iterator<Messages> itListMessages = listMessages.iterator();
    		while(itListMessages.hasNext()) {
    			
    			Messages msg = (Messages)itListMessages.next();
    			dialogErrorMsgs.add("Error saving COA: "+ msg.getReturnMessage());
    			logger.error("Error saving COA: "+ msg.getReturnMessage());
    		}
    		
      
    	}
    	
    }
    

    /**
     * sync up content manager with any changes from the gui
     * @throws Exception 
     */
    public void updateIndexValues() throws Exception
    {
    	
    	logger.info("Updating Index Values Started");
        try
        {
        	ContentManagerImplementation cm  = new ContentManagerImplementation(this.getDatastore());
            String objectId = getOBJECTID();
            String[] fields = { "GCPS COA Number", "GCPS Customer Name" };
            String[] values = { getCOANUMBER(), getCUSTOMERNAME() };
            logger.info("Fields:" + Arrays.toString(fields));
            logger.info("Values:" + Arrays.toString(values));
            if (objectId.trim().length() > 0)
            {
                boolean rc = cm.updateDocument(objectId, fields, values,this.getDatastore());
            }
        } catch (Exception exc)
        {
            logger.fatal("Error setting Content Manager index values: "+ exc.toString(), exc);
            throw exc;
        }
        
        logger.info("Updating Index Values Finished");
    }

    public void rollbackIndexing() throws Exception
    {
        logger.info("Rolling Back Started");
        try {
        	  updateIndexValues();
        } catch (Exception exc)
        {
            logger.fatal("Error setting Content Mananger index values:" + exc.toString(),exc);
            throw exc;
        }
        logger.info("Rolling Back Finished");
    }

    /*
     * retrieving the invoice control data by the object id as well as invoice
     * number and date
     */
    public ArrayList<COAELS> selectMatchingCOAs() throws Exception
    {
    	logger.info("Searching COA # " + getCOANUMBER());
    	
    	ArrayList results = null;
        COAELS aCOADM = new COAELS();
        aCOADM.setOBJECTID(getOBJECTID());
        aCOADM.setCOUNTRY(getCOUNTRY());
        aCOADM.setCOANUMBER(getCOANUMBER());
        aCOADM.setUSERID(getUSERID());
        aCOADM.generateUNIQUEREQUESTNUMBER();
        aCOADM.setLocale(getLocale());
        aCOADM.setGcmsEndpoint(getGcmsEndpoint());

        if (aCOADM.getCOUNTRY().equals("UK"))
        {
            aCOADM.setCOUNTRY("GB");
        }

        try
        {
            if (getCOANUMBER().trim().length() <= 9)
            {
                results = getGCPSGetCOA().getCOA(aCOADM);
            }
        } catch (Exception exc)
        {
            logger.fatal(exc.toString(),exc);
            throw exc;
        }

        if (results == null)
            return new ArrayList();
        else
        	return results;	
    }
    

    public boolean isFromAPTS()
    {
        return false;
    }

    public boolean isFromGCPS()
    {
        return !isFromAPTS();
    }
    
    
    public void initalizeIndexing() throws Exception {
    	
    	if(loadIndexValues()) {
    		initializeDatabaseValues();
    	}
    }
    
    private boolean initializeDatabaseValues() throws Exception
    {

    	 logger.info("Initializing Database Values");
    	 
        try
        {
            // load the contract if it exists
            ArrayList results = selectMatchingCOAs();

            COAELS retrievedCOA = null;
            COAELS COANumMatch = null;
            COAELS COANumAndObjMatch = null;

            for (int i = 0; i < results.size(); i++)
            {
            	COAELS aRetievedCOA = (COAELS) results.get(i);
                if (aRetievedCOA.getCOANUMBER().equals(this.getCOANUMBER()))
                {
                    COANumMatch = aRetievedCOA;
                    if (aRetievedCOA.getOBJECTID().equals(this.getOBJECTID()))
                    {
                        //this is a re-index
                        COANumAndObjMatch = aRetievedCOA;
                    }else{
                    	DOCUMENT cmdoc = aRetievedCOA.queryDocument();
                        if (cmdoc != null)
                        {
                        	String oldIndexClass = cmdoc.getItemtype();
                        	String indexClass = this.getINDEXCLASS();
                        	boolean isValidIndexPath = true;
                        	if (indexClass.endsWith("SignCOA"))
                        	{
                        		// old item type must be PDF
                        		if (! oldIndexClass.endsWith("COA_PDF"))
                        		{
                        			isValidIndexPath = false;
                        		}
                        	}
                        	
                        	// check the indexing path
                        	if (!isValidIndexPath)
                        	{
                                //this object id is on another OL in GCPS
                        		validationIsDuplicateCOAFromCM = true;
                        		logger.info("Duplicate COA retrieved from Content Manager");
                                return false;
                        	}
                        }                                       	
                    }
                } else if (aRetievedCOA.getOBJECTID().equals(this.getOBJECTID()))
                {
                    //this object id is on another COA in GCPS
                	validationIsDuplicateCOANumberEntered = true;
                	logger.info("Duplicate CAO Number entered");
                    return false;
                }
            }

            if (COANumAndObjMatch != null)
            {
                retrievedCOA = COANumAndObjMatch;
            } else if (COANumMatch != null)
            {
                retrievedCOA = COANumMatch;
            }

            if (retrievedCOA == null)
            {
            	logger.info("COA not found in GCMS");
            	validationIsCoaNotFoundInGCMS = true;
                return false;
            } else
            {

                COAELS aCOADM = (COAELS) results.get(0);

               
                // check for changed fields from the index
                if (( aCOADM.getCUSTOMERNAME() == null || this.getCUSTOMERNAME() == null) 
                		 ||  ( !aCOADM.getCUSTOMERNAME().trim().equals(this.getCUSTOMERNAME().trim()))  )
                {
                	this.setDIRTYFLAG(Boolean.TRUE);
                }

                if (( aCOADM.getCOANUMBER() == null || this.getCOANUMBER() == null) 
                  		 || ( !aCOADM.getCOANUMBER().trim().equals(this.getCOANUMBER().trim())) )
                {
                	this.setDIRTYFLAG(Boolean.TRUE);
                }
             
                this.setCOUNTRY(aCOADM.getCOUNTRY());
                this.setCOANUMBER(aCOADM.getCOANUMBER());
                this.setAMOUNT(aCOADM.getAMOUNT());
                this.setCURRENCY(aCOADM.getCURRENCY());
                this.setOFFERINGLETTER(aCOADM.getOFFERINGLETTER());
                
                //this.setOFFERINGLETTERVALIDITYDATEasLong(aCOADM.getOFFERINGLETTERVALIDITYDATEasLong())
                this.setOFFERINGLETTERVALIDITYDATE(aCOADM.getOFFERINGLETTERVALIDITYDATE());
                
                this.setCUSTOMERNAME(aCOADM.getCUSTOMERNAME());
                this.setCOASTATUS(aCOADM.getCOASTATUS());

                if (aCOADM.isLOCKED())
                {
                	logger.info("This COA is locked in GCMS, please unlock and try later.");
                    this.setDIRTYFLAG(Boolean.FALSE);
                    validationIsCoaLockedInGCMS = true;
                    return false;
                }
                
                if (this.isDeactivated())
                {
                	logger.info("COA has been deactivated in GCMS");
                	validationIsCoaDeactivatedInGCMS = true;
                    this.setDIRTYFLAG(Boolean.FALSE);
                    return false;
                }
                if (this.getINDEXCLASS().endsWith("SignCOA"))
                {
                	logger.debug("Signed");
                    if(this.getCOASTATUS().equals("CPPA")){
                    	 logger.info("Do not allow Sign COA to be reindexed if in Pending Pricer Approval Status");
                    	 validationIsPendingPricingApprovalStatus = true;
                        this.setDIRTYFLAG(Boolean.FALSE);
                        return false;                    	
                    }
                    if (!(this.getCOASTATUS().equals("CSENT") || this.getCOASTATUS().equals("CAVAIL")))
                    {
                    	logger.info("COA has incorrect status for Signed COA");
                    	validationIsIncorrectStatusForCoa = true;
                        this.setDIRTYFLAG(Boolean.FALSE);
                        return false;
                    }
                }                
                
            }

           
            return true;

        } catch (Exception exc)
        {
            logger.fatal("Error initializing COA: " + exc.toString(),exc);
            throw exc;
        }
       
    }
    
    public void validateInput()
    {
    	logger.info("Validating Data Started");
        boolean exists = false;
        try
        {
            DOCUMENT cmdoc = this.queryDocument();
            if (cmdoc != null)
            {
                exists = true;
            }
        } catch (Exception exc)
        {
            exists = false;
        }
        if (exists == false)
        {
        	validationIsDocumentProperlyIndexed = false;
          
        }
        logger.info("Validating Data Finished");

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
    
    
    
 
}