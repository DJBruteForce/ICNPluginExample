package com.ibm.gil.business;



import com.ibm.gil.model.MiscDataModelELS;
import com.ibm.igf.contentmanager.rmi.ContentManagerImplementation;
import com.ibm.igf.webservice.GCPSCreateMisc;

public class MiscELS extends Indexing {
	
	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(COAELS.class);

	private String subject;
	private String legacyCustomerNumber;
	private String gcpsCustomerName;
	
    private String OFFERINGNUMBER;
    private String COANUMBER;
    private String INVOICENUMBER;
    
    private MiscDataModelELS dataModel;
    
    //Constructors
    public MiscELS(String country){
    	this.setCOUNTRY(country);
    	this.dataModel = new MiscDataModelELS(this);
    }
    
    //Getters and setters	
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public String getLegacyCustomerNumber() {
		return legacyCustomerNumber;
	}

	public void setLegacyCustomerNumber(String legacyCustomerNumber) {
		this.legacyCustomerNumber = legacyCustomerNumber;
	}

	public String getGcpsCustomerName() {
		return gcpsCustomerName;
	}

	public void setGcpsCustomerName(String gcpsCustomerName) {
		this.gcpsCustomerName = gcpsCustomerName;
	}
    
	public String getOFFERINGNUMBER() {
		return OFFERINGNUMBER;
	}

	public void setOFFERINGNUMBER(String oFFERINGNUMBER) {
		this.OFFERINGNUMBER = oFFERINGNUMBER;
	}
	
	public String getCOANUMBER() {
		return COANUMBER;
	}
	
	public void setCOANUMBER(String cOANUMBER) {
		this.COANUMBER = cOANUMBER;
	}
	
	public String getINVOICENUMBER() {
		return INVOICENUMBER;
	}
	
	public void setINVOICENUMBER(String iNVOICENUMBER) {
		this.INVOICENUMBER = iNVOICENUMBER;
	}
	
	public MiscDataModelELS getDataModel(){
		if(this.dataModel != null)
			return new MiscDataModelELS(this);
		else
			return this.dataModel;
	}
	
	public void setDataModel(MiscDataModelELS dataModel){
		this.dataModel = dataModel;
	}
    
    //etc
	public boolean loadIndexValues()
    {
        try
        {
            //setCOUNTRY(getCountryCodeFromIndex(getINDEXCLASS()));

            String[] values = null;
            String[] fields = null;
            boolean retry = false;
            boolean validData = false;
            //boolean passedValues = true;
            ContentManagerImplementation cm = new ContentManagerImplementation(this.getDatastore());
            //do {
                try
                {
                    fields = cm.getIndexFields(getINDEXCLASS());
                    // TODO retrieveCreatedDate();

                    values = getValues();

                    if (values == null)
                    {
                    	//values = getContentManager().getIndexValues(getOBJECTID());
                    	values = cm.getIndexValues(getOBJECTID(), getDatastore());
                        //passedValues = false;
                    }

                    if ((values != null) && (fields != null) && (getCREATEDATEasDate() != null) && (values.length > 0) && (fields.length > 0) && (values[0].trim().length() > 0))
                    {
                        validData = true;
                        retry = false;
                    } else
                    {
                        logger.info("Waiting for CM to release object");
                        retry = true;
                    }
                } catch (Exception exc)
                {
                	logger.fatal(exc.toString());
                    retry = true;
                }
                if (retry)
                {
                	// TODO go back to front end and prompt?
                    //retry = getEventController().prompt("Waiting for indexing to complete.  Retry?");
                    if (!retry)
                    {
                        return false;
                    }
                    return false;
                }
            //} while (retry == true);

            if (validData)
            {
               setOFFERINGNUMBER(getFieldMatch("GCPS OL Number", fields, values));
               setCOANUMBER(getFieldMatch("GCPS COA Number", fields, values));
               setINVOICENUMBER(getFieldMatch("Invoice Number", fields, values));
                
            }

        } catch (Exception exc)
        {
        	logger.fatal(exc.toString());
        	logger.error("Error retrieving Content Manager values");
            return false;
        }

        return true;

    }
	

	public boolean isValid() {
	   	if(this.getCOANUMBER().trim().length()>0 || this.getOFFERINGNUMBER().trim().length()>0 || 
	   			this.getINVOICENUMBER().trim().length()>0){
	   		return true;
	   	}
	   	return false;
	}
	

    /**
     * call the create Misc web service
     * 
     * @return boolean
     */
    public boolean createMiscDocument()
    {
        /*
         * create Misc Document
         */
    	return getGCPSCreateMiscDoc().createMisc(this);

    }    
    /*
     * web services
     */
    private transient static GCPSCreateMisc aGCPSCreateMisc = null;

    private GCPSCreateMisc getGCPSCreateMiscDoc()
    {
        if (aGCPSCreateMisc == null)
        {
        	aGCPSCreateMisc = new GCPSCreateMisc();
        }
        return aGCPSCreateMisc;
    }    
	

}
