package com.ibm.gil.business;


import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;

import javax.naming.NamingException;



import com.google.gson.annotations.SerializedName;
import com.ibm.gil.model.OfferingLetterDataModel;
import com.ibm.gil.util.RegionalBigDecimal;
import com.ibm.gil.util.RegionalDateConverter;
import com.ibm.igf.contentmanager.rmi.ContentManagerInterface;
import com.ibm.igf.hmvc.DB2;
import com.ibm.igf.hmvc.ResultSetCache;



 public class OfferingLetter extends Indexing{
	
    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(OfferingLetter.class);

	private  String OFFERINGNUMBER ;

	private   String OLDOFFERINGNUMBER ;

	private   String AMOUNT ;

	private   String CURRENCY ;

	private   String CUSTOMERNAME;
	
	private String OLDCUSTOMERNAME;

	private   String CUSTOMERNUMBER;
	
	private String OLDCUSTOMERNUMBER;
	
	private String OLDOFFERDATE;
	
	private   String FOLDERID ;

	private   String TEAM ;

	private   String CONTRACTOBJECTID ;

	private   String INVOICEOBJECTID ;

	private   String OFFERDATE ;
    
	private   String CONTRACTNUMBER ;
    
	private   String OLDCOUNTRY ;
    
	private   String NETEQBAL ;
	
	private boolean changeOLFlag;

	@SerializedName("INDEXED")	
	private boolean  INDEXED=false;
	
    private String createdTimeStamp = null;

    private boolean isValidChange = true;
    
	private  ResultSetCache currencyResults = null;
	
	 private ArrayList<FormSelect> currencyCodeSelectList;
	 
	private OfferingLetterDataModel offeringLetterDataModel;
	 
	private boolean validationIsDuplicatedOfferingLetter = false;
	private boolean validationIsContractFolderCreated = true;
	private boolean validationIsPartiallypaidOL= false;
	
	ContentManagerInterface CM=null;
	
	public OfferingLetter(){
		
	}
	
	public OfferingLetter(String country) {
			this.setCOUNTRY(country);
			offeringLetterDataModel = new OfferingLetterDataModel(this);
		}
	
	
	public String getOFFERINGNUMBER() {
        return OFFERINGNUMBER;
    }
	
	public boolean isValidationIsContractFolderCreated() {
		return validationIsContractFolderCreated;
	}

	public void setValidationIsContractFolderCreated(boolean validationIsContractFolderCreated) {
		this.validationIsContractFolderCreated = validationIsContractFolderCreated;
	}

	
      public boolean isChangeOLFlag() {
		return changeOLFlag;
	}

	public void setChangeOLFlag(boolean changeOLFlag) {
		this.changeOLFlag = changeOLFlag;
	}

	public OfferingLetterDataModel getOfferingLetterDataModel() {
    	
    	if (offeringLetterDataModel == null){
    		return new OfferingLetterDataModel(this);
    	} else {
    		return offeringLetterDataModel;
    	}
	}

	public void setOfferingLetterDataModell(OfferingLetterDataModel offeringLetterDataModel) {
		this.offeringLetterDataModel = offeringLetterDataModel;
	}
   

    public  void setOFFERINGNUMBER(String value)
    {
        OFFERINGNUMBER=value;
    }

    public String getOLDOFFERINGNUMBER()
    {
        return OLDOFFERINGNUMBER;
    }

   
    public void setOLDOFFERINGNUMBER(String value)
    {
    	OLDOFFERINGNUMBER=value;
    }

    public String getAMOUNT()
    {
        return AMOUNT;
    }

   

    public void setAMOUNT(String value)
    {
        AMOUNT=value;
    }

    public String getCURRENCY()
    {
        return CURRENCY;
    }

   

    public  void setCURRENCY(String value)
    {
       CURRENCY= value;
    }

    public  String getCUSTOMERNUMBER()
    {
        return CUSTOMERNUMBER;
    }

    public String getOLDCUSTOMERNUMBER() {
		return OLDCUSTOMERNUMBER;
	}

	public void setOLDCUSTOMERNUMBER(String oLDCUSTOMERNUMBER) {
		OLDCUSTOMERNUMBER = oLDCUSTOMERNUMBER;
	}

	public String getOLDOFFERDATE() {
		return OLDOFFERDATE;
	}

	public void setOLDOFFERDATE(String oLDOFFERDATE) {
		OLDOFFERDATE = oLDOFFERDATE;
	}

	public  void setCUSTOMERNUMBER(String value)
    {
       CUSTOMERNUMBER=value;
    }

    public String getCUSTOMERNAME()
    {
        return CUSTOMERNAME;
    }

    public void setCUSTOMERNAME(String value)
    {
        CUSTOMERNAME=value;
    }

    public String getTEAM()
    {
        return TEAM;
    }

    public void setTEAM(String value)
    {
        TEAM=value;
    }

    public String getFOLDERID()
    {
        return FOLDERID;
    }

   
    public void setFOLDERID(String value)
    {
       FOLDERID=value;
    }

    public  String getINVOICEOBJECTID()
    {
        return INVOICEOBJECTID;
    }

    public void setINVOICEOBJECTID(String value)
    {
        INVOICEOBJECTID=value;
    }

    public String getCONTRACTOBJECTID()
    {
        return CONTRACTOBJECTID;
    }

    public void setCONTRACTOBJECTID(String value)
    {
        CONTRACTOBJECTID=value;
    }

    public String getOFFERDATE()
    {
        return OFFERDATE;
    }

    public void setOFFERDATE(String value)
    {
        OFFERDATE=value;
    }
    
    public String getCONTRACTNUMBER()
    {
        return CONTRACTNUMBER;
    }

    public void setCONTRACTNUMBER(String value)
    {
        CONTRACTNUMBER=value;
    } 
    
    public String getOLDCOUNTRY()
    {
        return OLDCOUNTRY;
    }

    public void setOLDCOUNTRY(String value)
    {
        OLDCOUNTRY= value;
    }     

    
	public String getNETEQBAL()
	{
		return NETEQBAL;
	}

	public void setNETEQBAL(String value)
	{
		NETEQBAL=value ;
	}
	
	public boolean isINDEXED() {
		return INDEXED;
	}

	public void setINDEXED(boolean iNDEXED) {
		INDEXED = iNDEXED;
	}

	
	public String getOLDCUSTOMERNAME() {
		return OLDCUSTOMERNAME;
	}

	public void setOLDCUSTOMERNAME(String oLDCUSTOMERNAME) {
		OLDCUSTOMERNAME = oLDCUSTOMERNAME;
	}
	

	public ArrayList<FormSelect> getCurrencyCodeSelectList() {
		return currencyCodeSelectList;
	}

	public void setCurrencyCodeSelectList(
			ArrayList<FormSelect> currencyCodeSelectList) {
		this.currencyCodeSelectList = currencyCodeSelectList;
	}
	
	

	public String getCreatedTimeStamp()
    {
        return createdTimeStamp;
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


    public void setCreatedTimeStamp(String value)
    {
        try {
        	createdTimeStamp = value;
        
        } catch (Exception e) {
        	createdTimeStamp = null;
        }
    }
    
    
	
	
	   public boolean initializeIndexing() throws IllegalArgumentException, ParseException, Exception {
		    	    
				   loadIndexValues();
				   boolean valuesInitialized = initializeDatabaseValues();
				   
				   if(valuesInitialized)
				     initializeCurrencies(); 
		    
			   return true;
			   
		   }
		   
	   
	   
	   
	   public void saveDocument() throws Exception
	    {
	        try
	        {
	        	
	        	getOfferingLetterDataModel().saveDocument();
	            
	        } catch (Exception sqx)
	        {

	        	logger.fatal(sqx);
	        	throw sqx;
	        	
	        }

	    }
	   
	  
	  public boolean  loadIndexValues()
	    {
	        try
	        {
	            

	            String[] values = null;
	            String[] fields = null;
	            boolean retry = false;
	            boolean validData = false;
	            boolean passedValues = true;
	            
//	            //ContentManagerInterface CM = new ContentManagerImplementation();
//	            DKDatastoreICM datastore= this.getDatastore();
//	            CM = new ContentManagerImplementation(this.getDatastore());
	            CM=getOfferingLetterDataModel().getContentManager();
	            do
	            {
	                try
	                {
	                    fields = CM.getIndexFields(CM.indexDescriptionToName(getINDEXCLASS(),this.getDatastore()));
	                    values = getValues();

	                    if (values == null)
	                    {
	                        values = CM.getIndexValues(getOBJECTID(),this.getDatastore());
	                    }

	                    if ((values != null) && (fields != null) && (values.length > 0) && (fields.length > 0) && (values[0].trim().length() > 0))
	                    {
	                        validData = true;
	                        retry = false;
	                    } else
	                    {
	                        
	                        retry = true;
	                    }
	                } catch (Exception exc)
	                {
	                    
	                    retry = true;
	                }
	                if (retry)
	                {
	                    
	                    if (!retry)
	                    {
	                        return false;
	                    }
	                }
	            } while (retry == true);

	            if (validData)
	            {
	                String fromDate = getFieldMatch("Offering Letter Date", fields, values);
	                if (fromDate.trim().length() != 0)
	                {
	                    if (passedValues)
	                     {
	                    	logger.debug("inside offerletter passed values block");
	                        try
	                        {
	                            
	                            fromDate= RegionalDateConverter.convertDate("CM", "GUI", fromDate);
	                            logger.debug("inside offeringletter passed values try block from date:" +fromDate);
	                        } catch (Exception exc)
	                        {
	                           
	                            fromDate= RegionalDateConverter.convertDate("CM", "GUI", fromDate);
	                            logger.debug("inside offeringletter passed values catch block from date:" +fromDate);
	                        }
	                    } else
	                    {
	                       
	                        fromDate= RegionalDateConverter.convertDate("CM", "GUI", fromDate);
	                        logger.debug("inside offeringletter else block from date:" +fromDate);
	                    }
	                    setOFFERDATE(fromDate);
	                } else
	                {
	                	setOFFERDATE(RegionalDateConverter.getBlankDate("GUI"));
	                	logger.debug("inside offeringletter outer else from date:" +fromDate);
	                }

	                setOFFERINGNUMBER(getFieldMatch("Offering Letter Number", fields, values));
	                setCUSTOMERNAME( getFieldMatch("Customer Name", fields, values));
	                setCUSTOMERNUMBER(getFieldMatch("Customer Number", fields, values));
	                setTEAM(getFieldMatch("Team", fields, values));
	                setSOURCE(getFieldMatch("Source", fields, values));

	                String timestamp = getFieldMatch("Timestamp", fields, values);
	                try
	                {
	                    timestamp = RegionalDateConverter.convertTimeStamp("CM", "GUI", timestamp);
	                    setTIMESTAMP(timestamp);
	                } catch (Exception exc)
	                {
	                    
	                }
	            }
	        } catch (Exception exc)
	        {
	          
	            return false;
	        }

	       return true;
	    } 
	   
	   public boolean initializeDatabaseValues()
	    {

	        try
	        {
	            

	        	// below line by Krishna- remove it after GCMS validation is done 
	        	String newCountry = getCOUNTRY();
	        	
	            // load the offering letter if it exists
	            ResultSet results = offeringLetterDataModel.selectByObjectIdStatement(getOBJECTID(),getCOUNTRY());
	            logger.debug("results in initializedatabase values: "+results);
	            
	            if (!results.next())
	            {
	            	logger.debug("There are no results.");
	            	
	            	this.setOLDOFFERINGNUMBER(this.getOFFERINGNUMBER());
	            	this.setOLDCOUNTRY(this.getCOUNTRY());
	            	this.setDOCUMENTMODE(ADDMODE);
	            	this.setDIRTYFLAG(Boolean.TRUE);
	            	this.setINDEXED(false);
	            	
	            	
	            } else
	            {
	            	logger.debug("inside else statement in initialize database values");
	            	this.setDOCUMENTMODE(UPDATEMODE);
	            	logger.debug("after setting update mode in initialize database values:"+ getDOCUMENTMODE());
	                this.setDIRTYFLAG(Boolean.FALSE);

	                // store the new offering letter number
	                String newOfferingLetterNumber = getOFFERINGNUMBER();
	                String oldOfferingLetterNumber = results.getString(1).trim();
	                String oldCustomernumber = results.getString(5).trim();
	                String oldCustomerName = results.getString(6).trim();
	                String oldOfferdate = results.getString(7).trim();
	                oldOfferdate = RegionalDateConverter.convertDate((new StringBuilder()).append("GUI-").append(getLocale().toString()).toString(), (new StringBuilder()).append("GUI-").append(getLocale().toString()).toString(), oldOfferdate);

	                this.setOLDOFFERINGNUMBER(oldOfferingLetterNumber);
	                this.setOFFERINGNUMBER(newOfferingLetterNumber);
	                setOLDCUSTOMERNAME(oldCustomerName);
	                setCUSTOMERNAME(getCUSTOMERNAME());
	                setOLDCUSTOMERNUMBER(oldCustomernumber);
	                setOLDOFFERDATE(oldOfferdate);
	                logger.debug((new StringBuilder()).append("New OL Num: ").append(newOfferingLetterNumber).append(" Old OL Num: ").append(oldOfferingLetterNumber).toString());
	                logger.debug((new StringBuilder()).append("new customer name: ").append(getCUSTOMERNAME()).append("oldcustomername:").append(oldCustomerName).toString());
	                logger.debug((new StringBuilder()).append("new customer number:").append(getCUSTOMERNUMBER()).append("old customer number:").append(oldCustomerName).toString());
	                logger.debug((new StringBuilder()).append("new offering date:").append(getOFFERDATE()).append("old Offering date :").append(oldOfferdate).toString());

	                this.setINDEXED(true);

	                //check for amount changes
	                RegionalBigDecimal totalAmount = new RegionalBigDecimal(results.getString(2), RegionalBigDecimal.PERIODDECIMALSEPARATOR);
	                RegionalBigDecimal balance = new RegionalBigDecimal(results.getString(3), RegionalBigDecimal.PERIODDECIMALSEPARATOR);
	                
	                logger.debug("totalAmount:"+ totalAmount + "balance" + balance);
	                
	              
	               logger.debug("OL Amount: "+totalAmount);
	                
	                // load non index fields from the database
	                this.setAMOUNT(totalAmount.toString());
	                this.setCURRENCY(DB2.getString(results, 4).trim());
	                String customerNumber = DB2.getString(results, 5).trim();
	                String customerName= DB2.getString(results, 6).trim();
	                String offerdate = DB2.getString(results, 7);
	                offerdate = RegionalDateConverter.convertDate("GUI-"+getLocale().toString(), "GUI-"+getLocale().toString(), offerdate);
	                logger.debug("OfferDate from offerdate" +offerdate);
	                if(offerdate=="0001-01-01")
	                {
	                	this.setOFFERDATE("");
	                }
	                else 
	                {
	                	this.setOFFERDATE(offerdate);
	                }
	                String oldCountry = DB2.getString(results, 8).trim();
	                this.setOLDCOUNTRY(oldCountry);
	                //NEXT 2 LINES DON'T EXIST IN CM8.4 CODE REMOVING
//	                this.setCUSTOMERNAME(customerName);
//	                this.setCUSTOMERNUMBER(customerNumber);
	               // this.setOFFERDATE(offerdate);
	                
	                logger.debug("String objectId:"+getOBJECTID()+ "String country:"+ getCOUNTRY());
	                
	                logger.debug("customer name: "+customerName + "customerNumber:"+customerNumber +"offerdate :" + offerdate+  "currency:"+ getCURRENCY());

	                // check for changed fields from the index
	                 if (!customerName.trim().equals(getCUSTOMERNAME()))                	 
	                {
	                	
	                	 this.setDIRTYFLAG(Boolean.TRUE);
	                }
	                if (!customerNumber.trim().equals(getCUSTOMERNUMBER()))
	                {
	                	this.setDIRTYFLAG(Boolean.TRUE);
	                }
	                if(!offerdate.equals(getOFFERDATE())){
	                	logger.debug("inside offerdate in initialize database values");
	                	this.setDIRTYFLAG(Boolean.TRUE);
	                }
	                
	                this.setCOUNTRY(oldCountry);
	                
	                boolean validChange = true;
	                boolean partiallyPaid = false;
	                if(balance.compareTo(RegionalBigDecimal.ZERO) == 0){ //it is fully paid OL? fully paid OL == balance=0.
	                	validChange = false;
	                	isValidChange= false;
	                }
	                logger.debug("ValidChange is:" +validChange);
	                if(totalAmount.compareTo(balance) > 0 && balance.compareTo(RegionalBigDecimal.ZERO) > 0 ){
	                	partiallyPaid = true;	                	
	                	validationIsPartiallypaidOL=true;
	                	//this.setDIRTYFLAG(Boolean.TRUE);
	                	//this.setOFFERINGNUMBER(oldOfferingLetterNumber);
	                 }                 
	                
	                 //boolean validChange = isValidOLChange();
	                // check to see if the country or number have changed
	                if (!newOfferingLetterNumber.trim().equals(oldOfferingLetterNumber.trim()))
	                {	
	                	 logger.debug("New OL and OLD OL are different");
	                	/*if(partiallyPaid){
	                       // error("Offer Letter has a payment against it and cannot be updated");
	                       // offeringLetterDataModel.rollbackIndexing();
	                    	return false;               		
	                	}*/
	                	setChangeOLFlag(false);//clean flag
	                	if(validChange){
	                		logger.debug("Since ValidChange is true then displaying Changing OL number dialog");
	                		// verify with the user that this is what they wanted to do
	                		/*if (prompt("Changing Offering Letter Number.  Are you sure?"))
	                		{*/
	                			// yes selected
	                			setChangeOLFlag(true);
//	                		    this.setDIRTYFLAG(Boolean.TRUE);
//	                		//} else
//	                		{
//	                			this.setOFFERINGNUMBER(oldOfferingLetterNumber);
//	                		}
	                	}
	               }
	                
	                if(!oldCountry.equals(newCountry)){
	                	if(!validChange){
	                		offeringLetterDataModel.rollbackCountryIndexing();
	                		this.setINDEXCLASS("Offering Letter " + getCOUNTRY());                		
	                	}else{
	                		this.setCOUNTRY(newCountry);
	                	}
	                }
	                
	                if (!validChange){
	                    logger.debug("Offer Letter has a 0.00 balance and cannot be updated");
	                    
	                   // offeringLetterDataModel.rollbackIndexing();
	                	//return false;
	                }

	                // find the folder id in cm
	                //commneted below line by krishna
	                String folderId= offeringLetterDataModel.selectFolderId(this.getDatastore());
	                             
	                this.setFOLDERID(folderId);
	                
	               
	                
	                if(partiallyPaid){
	                  	// aViewFrame.setFieldsEditable(false);
	                  }           
	                if(totalAmount.compareTo(balance) == 0 ){
	                	this.setNETEQBAL("Y");
	                }
	               
	            }
	           // }
	        } catch (Exception exc)
	        {
	        	//below line commented by Krishna
	        	logger.fatal("Exception: "+ exc.getMessage());
	            return false;
	        }
	     
	        
	        isWorkAccess(this.getUSERID());
	       return true;
	    }
		    
		
	   
	   public void updateIndexValues()
	    {
		     CM = getOfferingLetterDataModel().getContentManager();
	        try
	        {
	        	
	            String objectId = getOBJECTID();
	            String folderId = getFOLDERID();
	            String offerLetterDate = RegionalDateConverter.convertDate("GUI", "CM", getOFFERDATE());
	                        
	        	if(!getCOUNTRY().equals(getOLDCOUNTRY())){
	        		CM.moveDocument(getFOLDERID(),"Offering Letter Folder " + getCOUNTRY(),this.getDatastore());
	        	} 

	            String[] fields = { "Offering Letter Number", "Customer Name", "Customer Number", "Offering Letter Date" };
	            String[] values = { getOFFERINGNUMBER(),getCUSTOMERNAME(), getCUSTOMERNUMBER(), offerLetterDate };
	            if (folderId!=null && folderId.trim().length() > 0)
	            {
	                boolean retry = false;
	                do
	                {
	                	
	                    boolean rc = CM.updateDocument(folderId, fields, values,this.getDatastore());
	                    
	                    logger.debug("in updateIndexValues , rc is :"+rc);
	                    
	                    if (!rc)
	                    {
	                    	//below line commented by Krishna
	                       // retry = getEventController().prompt("Cannot update Content Manager index values.\nPlease close the document image and check-in.\nRetry?");
	                    } else
	                    {
	                        retry = false;
	                    }
	                } while (retry);
	            }
	            if (objectId.trim().length() > 0)
	            {
	                boolean retry = false;
	                do
	                {
	                	
	                    boolean rc = CM.updateDocument(objectId, fields, values,this.getDatastore());
	                    if (!rc)
	                    {
	                       // retry = getEventController().prompt("Cannot update Content Manager index values.\nPlease close the document image and check-in.\nRetry?");
	                    } else
	                    {
	                        retry = false;
	                    }
	                } while (retry);
	            }
	        } catch (Exception exc)
	        {
	            logger.fatal(exc);
	           
	        }
	    }
	   
	   public void validateInput() throws Exception {
		   
	        ResultSet resultsDupicateOfferingLetter;
	        try
	        {
	        	resultsDupicateOfferingLetter = getOfferingLetterDataModel().selectByOfferingStatement();
	        	
	        	
	            if (resultsDupicateOfferingLetter.next())
	            {
	            	
	                if (!DB2.getString(resultsDupicateOfferingLetter, 4).equals(this.getOBJECTID()))
	                {
	                	validationIsDuplicatedOfferingLetter = true;
	                }
	            }else {
                	
                	validationIsDuplicatedOfferingLetter= false;
                }
	            
	        } catch (Exception exc)
	        {
	            logger.fatal(exc.toString());
	            throw exc;
	        }
	   }
	 

	   
	   public void rollbackIndexing() throws Exception
	    {
	        try
	        {

	        	getOfferingLetterDataModel().rollbackIndexing();
	            
	        } catch (Exception sqx)
	        {

	        	logger.fatal(sqx);
	        	throw sqx;
	        	
	        }

	    }
	    
	   private boolean isValidOLChange() throws SQLException, NamingException{
	    	//OfferingLetterDataModel aDataModel = (OfferingLetterDataModel) getDataModel();
	   	    ResultSet results = getOfferingLetterDataModel().selectContractNumber();
	   	    boolean cntNumExist = false;
	   	    if(results.next()){
	   	    	    setCONTRACTNUMBER(results.getString(1).trim());
	   	    	cntNumExist = true;
	   	    }
	   	    
	   	    if(cntNumExist){
	   	    	results = getOfferingLetterDataModel().selectPaymentDate();
	   	    	if(results.next()){
	   	    		String payDate = results.getString(1).trim();
	   	    		if(!(payDate == null || payDate.equals("")) ){
	   	    			return false;
	   	    		}
	   	    	}
	   	    }
	 
	    	
	    	return true;
	    }
	     
	   
	 
	 /*    *//**
	     * load the currency values from the database and populate the gui
	 * @return 
	 * @throws Exception 
	     *  
	     */
	    public ArrayList<FormSelect> initializeCurrencies() throws Exception
	    {
	    	
	    	FormSelect code = null;
	    	ArrayList<FormSelect> codes = new ArrayList<FormSelect>();
	    	
	    	boolean hasSelectedValue =(this.CURRENCY!=null && !"".equals(this.CURRENCY));
	        try
	        {

	            String currencyCode;
	            ResultSetCache cachedResults = offeringLetterDataModel.selectCurrencyCodesStatement();
	            
	            while (cachedResults.next())
	            {
	            	code = new FormSelect();
	                currencyCode = cachedResults.getString(1);
	                code.setValue(currencyCode);
	                code.setLabel(currencyCode);
	                
	                if( hasSelectedValue &&  this.CURRENCY.equalsIgnoreCase(currencyCode)	) {
	                	
	                	code.setSelected(true);
	                	
	                } else if (!hasSelectedValue && cachedResults.getString(2).equals("*"))   {
	                			
	                			code.setSelected(true);
	                }
	                
	                codes.add(code);
	                
	            }

	           
	        } catch (Exception exc)
	        {
	        	
	        	logger.fatal(exc.toString());
	        	throw exc;

	        }
	        
	        logger.debug(codes);
	        
	        this.currencyCodeSelectList = codes;
	        return currencyCodeSelectList;
	        
	     
	    }
	
	

}
