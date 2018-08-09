
package com.ibm.gil.business;

import java.io.ByteArrayInputStream;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;



import com.google.gson.annotations.Expose;
import com.ibm.gil.model.ContractDataModel;
import com.ibm.gil.util.RegionalBigDecimal;
import com.ibm.gil.util.RegionalDateConverter;
import com.ibm.igf.contentmanager.rmi.ContentManagerImplementation;
import com.ibm.igf.hmvc.DB2;
import com.ibm.igf.hmvc.ResultSetCache;
import com.ibm.w3.financing.tools.gcms.query.results.DOCUMENT;
import com.ibm.w3.financing.tools.gcms.query.results.DocumentRoot;
import com.ibm.w3.financing.tools.gcms.query.results.QUERYRESULTSType;
import com.ibm.w3.financing.tools.gcms.query.results.util.ResultsResourceUtil;


public class Contract extends Indexing {

	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(Contract.class);
	
	public Contract() {
		
	}
	
	public Contract(String country) {
		
		this.setCOUNTRY(country);
		contractDataModel = new ContractDataModel(this);
	}
	
	private ContractDataModel contractDataModel;
    @Expose private String  CONTRACTNUMBER;
    @Expose private String  OLDCONTRACTNUMBER;
    @Expose private String  CONTRACTDATE;
    @Expose private String  OLDCONTRACTDATE;
    @Expose private String  AMOUNT;   
    @Expose private String  BALANCE;
    @Expose private String  CURRENCY;
    @Expose private String  CUSTOMERNAME;
    @Expose private String  OLDCUSTOMERNAME;
    @Expose private String  CUSTOMERNUMBER;
    @Expose private String  OFFERINGLETTER;
    @Expose private String  FOLDERID;
    @Expose private String  TEAM;
    @Expose private String  OFFERINGLETTERID;
    private String  INVOICENUMBER;
    private String  INVOICEDATE;
    private String  PAYMENTDATE;
    private String  OLDOFFERINGLETTER;
    private String  OLDCOUNTRY;
    private String  NETEQBAL;
    private String  ZEROBAL;
    @Expose  private String createdTimeStamp;
    private ResultSetCache currencyResults = null;
	private ResultSetCache cccmpcodeCache = null;
    private ArrayList<FormSelect> currencyCodeSelectList;
    @Expose private ArrayList<ContractInvoice> invoicesList = new ArrayList<ContractInvoice>();
    private boolean validationPaymentDone =  false;
    private boolean validationPartiallyPaid = false;
    private boolean validationIsDuplicateContractNumber = false;
    private boolean validationIsOfferLetterInCM = true;
    private boolean validationIsOfferLetterValidHybr = true;
    private boolean validationIsDocumentIndexedProperly = true;
    private boolean validationIsContractFolderCreated = true;
    
	public boolean isValidationIsContractFolderCreated() {
		return validationIsContractFolderCreated;
	}

	public void setValidationIsContractFolderCreated(
			boolean validationIsContractFolderCreated) {
		this.validationIsContractFolderCreated = validationIsContractFolderCreated;
	}

	private ArrayList<String> dialogErrorMsgs = new ArrayList<String>();
    
    
    public ArrayList<String> getDialogErrorMsgs() {
		return dialogErrorMsgs;
	}

	public void setDialogErrorMsgs(ArrayList<String> dialogErrorMsgs) {
		this.dialogErrorMsgs = dialogErrorMsgs;
	}
	
    
      public ArrayList<ContractInvoice> getInvoicesList() {
		return invoicesList;
	}

	public void setInvoicesList(ArrayList<ContractInvoice> invoicesList) {
		this.invoicesList = invoicesList;
	}

	public String getBALANCE() {
		return BALANCE;
	}

	public void setBALANCE(String bALANCE) {
		BALANCE = bALANCE;
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

    public ArrayList<FormSelect> getCurrencyCodeSelectList() {
		return currencyCodeSelectList;
	}

	public void setCurrencyCodeSelectList(
			ArrayList<FormSelect> currencyCodeSelectList) {
		this.currencyCodeSelectList = currencyCodeSelectList;
	}

    public ContractDataModel getContractDataModel() {
    	
    	if (contractDataModel == null){
    		contractDataModel = new ContractDataModel(this);
    		return contractDataModel;
    	} else {
    		return contractDataModel;
    	}
	}

	public void setContractDataModel(ContractDataModel contractDataModel) {
		this.contractDataModel = contractDataModel;
	}

	public String getCONTRACTNUMBER() {
		return CONTRACTNUMBER;
	}

	public void setCONTRACTNUMBER(String cONTRACTNUMBER) {
		CONTRACTNUMBER = cONTRACTNUMBER;
	}

	public String getOLDCONTRACTNUMBER() {
		return OLDCONTRACTNUMBER;
	}

	public void setOLDCONTRACTNUMBER(String oLDCONTRACTNUMBER) {
		OLDCONTRACTNUMBER = oLDCONTRACTNUMBER;
	}

	public String getCONTRACTDATE() {
		return CONTRACTDATE;
	}

	public void setCONTRACTDATE(String cONTRACTDATE) {
		CONTRACTDATE = cONTRACTDATE;
	}

	public String getOLDCONTRACTDATE() {
		return OLDCONTRACTDATE;
	}

	public void setOLDCONTRACTDATE(String oLDCONTRACTDATE) {
		OLDCONTRACTDATE = oLDCONTRACTDATE;
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
	public String getOLDCUSTOMERNAME() {
		return OLDCUSTOMERNAME;
	}

	public void setOLDCUSTOMERNAME(String oLDCUSTOMERNAME) {
			OLDCUSTOMERNAME = oLDCUSTOMERNAME;
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

	public String getINVOICENUMBER() {
		return INVOICENUMBER;
	}

	public void setINVOICENUMBER(String iNVOICENUMBER) {
		INVOICENUMBER = iNVOICENUMBER;
	}

	public String getINVOICEDATE() {
		return INVOICEDATE;
	}

	public void setINVOICEDATE(String iNVOICEDATE) {
		INVOICEDATE = iNVOICEDATE;
	}

	public String getPAYMENTDATE() {
		return PAYMENTDATE;
	}

	public void setPAYMENTDATE(String pAYMENTDATE) {
		PAYMENTDATE = pAYMENTDATE;
	}

	public String getOLDOFFERINGLETTER() {
		return OLDOFFERINGLETTER;
	}

	public void setOLDOFFERINGLETTER(String oLDOFFERINGLETTER) {
		OLDOFFERINGLETTER = oLDOFFERINGLETTER;
	}

	public String getOLDCOUNTRY() {
		return OLDCOUNTRY;
	}

	public void setOLDCOUNTRY(String oLDCOUNTRY) {
		OLDCOUNTRY = oLDCOUNTRY;
	}

	public String getNETEQBAL() {
		return NETEQBAL;
	}

	public void setNETEQBAL(String nETEQBAL) {
		NETEQBAL = nETEQBAL;
	}

	public String getZEROBAL() {
		return ZEROBAL;
	}

	public void setZEROBAL(String zEROBAL) {
		ZEROBAL = zEROBAL;
	}

	public ResultSetCache getCurrencyResults() {
		return currencyResults;
	}

	public void setCurrencyResults(ResultSetCache currencyResults) {
		this.currencyResults = currencyResults;
	}

	public ResultSetCache getCccmpcodeCache() {
		return cccmpcodeCache;
	}

	public void setCccmpcodeCache(ResultSetCache cccmpcodeCache) {
		this.cccmpcodeCache = cccmpcodeCache;
	}
    
	
    public void initalizeIndexing() throws Exception {

        loadIndexValues();
        initializeDatabaseValues();
        initializeCurrencies();
        retrieveInvoices();
    }
    
    public void rollbackIndexing() throws Exception   {
    	
    	getContractDataModel().rollbackIndexing();
    }
    
    
    public void selectFolderId() throws Exception
    {
    	ContentManagerImplementation cm  = new ContentManagerImplementation(this.getDatastore());
    	
        String[] fields = { "Contract Number" };
        String[] values = { getOLDCONTRACTNUMBER() };
        String folderXML = cm.executeQuery("Contract Folder " + getOLDCOUNTRY(), fields, values,getDatastore());
        String folderId = getObjectId(folderXML);
        setFOLDERID(folderId);
    }

    public void selectOfferingLetterId() throws Exception
    {
    	ContentManagerImplementation cm  = new ContentManagerImplementation(this.getDatastore());
    	
        String[] fields = { "Offering Letter Number" };
        String[] values = { getOFFERINGLETTER() };
        String objectXML = cm.executeQuery("Offering Letter " + getCOUNTRY(), fields, values,getDatastore());
        String objectId = getObjectId(objectXML);
        setOFFERINGLETTERID(objectId);
    }
    
    public void rollbackCountryIndexing() throws Exception  {
       
    	logger.info("Rolling Back Country Started");
    	try  {
    		
        	ContentManagerImplementation cm  = new ContentManagerImplementation(this.getDatastore());
            String objectId = getOBJECTID();
            if (objectId.trim().length() > 0)  {
            	cm.moveDocument(objectId, "Contract " + getCOUNTRY(),getDatastore());  
            }
        } catch (Exception exc)  {
            logger.fatal("Error setting Content Manager index values: " + exc.toString());
            throw exc;
        }
    	
    	logger.info("Rolling Back Country Finished");
    }

    public boolean loadIndexValues() throws Exception {
        
    	try  {
    		logger.info("Loading Indexing Values Started");

		       String[] values = null;
		       String[] fields = null;
		       boolean validData = false;
		       ContentManagerImplementation cm  = new ContentManagerImplementation(this.getDatastore());
               fields = cm.getIndexFields(cm.indexDescriptionToName(this.getINDEXCLASS(),this.getDatastore()));
               values = getValues();

               if (values == null){
                   values = cm.getIndexValues(this.getOBJECTID(), this.getDatastore());
                  
               }

               if ((values != null) && (fields != null) && (values.length > 0) && (fields.length > 0) && (values[0].trim().length() > 0))  {
                    validData = true;
               } else {
            	   	logger.info("Waiting for CM to release object");
               }

            if (validData)  {
            	
                this.setCONTRACTNUMBER( getFieldMatch("Contract Number", fields, values));
                this.setOFFERINGLETTER(getFieldMatch("Offering Letter Number", fields, values) );
                this.setCUSTOMERNAME(getFieldMatch("Customer Name", fields, values) );
                this.setCUSTOMERNUMBER(getFieldMatch("Customer Number", fields, values) );
                this.setTEAM(getFieldMatch("Team", fields, values));
                this.setSOURCE(getFieldMatch("Source", fields, values));
                String timestamp = getFieldMatch("Timestamp", fields, values);
                
                try   {
                    timestamp = RegionalDateConverter.convertTimeStamp("CM", "GUI", timestamp);
                    this.setTIMESTAMP(timestamp);
                } catch (Exception exc) {
                    logger.info("Timestamp field is blank");
                }

            } else {
            	return false;
            }
            
            logger.info("Fields:" + Arrays.toString(fields));
            logger.info("Values:" + Arrays.toString(values));
            
            logger.info("Loading Indexing Values Finished");
            
        } catch (Exception exc)  {
            logger.fatal("Error loading index values from CM:"+ exc.toString(),exc);
            throw exc;
        }
        return true;
    }

    public void initializeCurrencies() throws Exception  {
    	
    	FormSelect code = null;
    	ArrayList<FormSelect> codes = new ArrayList<FormSelect>();
    	boolean hasSelectedValue = (this.CURRENCY!=null && !"".equals(this.CURRENCY));
        try  {

            // load the currency types
            String currencyCode;
            ResultSetCache cachedResults = getContractDataModel().selectCurrencyCodesStatement();
            while (cachedResults.next())  {
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
           
        } catch (Exception exc) {
        	logger.fatal(exc.toString(),exc);
        	throw exc;
        }       
        this.currencyCodeSelectList = codes;
    }
    
    
   
   private void retrieveInvoices() throws Exception {
       
	   try  {
           ResultSet results = getContractDataModel().selectDetailsStatement();
           loadResults(results);
           
       } catch (Exception exc)  {
           logger.fatal("Error while retrieving invoices:"+exc.toString(),exc);
           throw exc;
       }    	
   }
   
   private void loadResults(ResultSet results) throws Exception  {
       
	   try  {
    	   ContractInvoice invoice = null;
           // load each record into the table
           while (results.next())  {
        	   int i = 1;
        	   invoice = new ContractInvoice();
        	   invoice.setOBJECTID(DB2.getString(results, i++));
        	   invoice.setINVOICENUMBER(DB2.getString(results, i++));
        	   invoice.setINVOICEDATE(DB2.getString(results, i++)); 
               // fixup fields
               String date = RegionalDateConverter.convertDate("DB2", "GUI",invoice.getINVOICEDATE());
               invoice.setINVOICEDATE(date);
               invoice.setCOUNTRY(this.getCOUNTRY());
               // contract number will be the indicator that the record was saved to gapts
               invoice.setCONTRACTNUMBER(this.getCONTRACTNUMBER());
               invoicesList.add(invoice);
           }
           

        } catch (Exception exc) {
           logger.fatal("Error searching for Invoice:"+ exc.toString());
           throw exc;
       }
   } 
   
   public boolean initializeDatabaseValues()
   {

       try
       {
    	   
    	   logger.info("Initializing Database Values");
    		
           // see if the contract exists by number with a blank object id,
           ResultSet results = getContractDataModel().selectByContractStatement();
           boolean isCOAContract = false;
           if (results.next())
           {
               String objectId = DB2.getString(results, 3);
               if ((objectId == null) || (objectId.trim().length() == 0))
               {
                   // update the object id
            	   getContractDataModel().updateObjectIdStatement();
                   isCOAContract = true;
               }
           }

           String newCountry = this.getCOUNTRY();
           
           results = getContractDataModel().selectByObjectIdStatement();
           if (!results.next())
           {
               this.setDOCUMENTMODE(Indexing.ADDMODE);
           } else
           {
        	   this.setDOCUMENTMODE(Indexing.UPDATEMODE);
           }
           //          SEE If record exists in CNTHIST file and added by COA tool
           int countCOAContract = 0;
           ResultSet resultsCOAContract = getContractDataModel().selectContractCreatedFromCOATool();
           if (resultsCOAContract.next())
           {
               countCOAContract = resultsCOAContract.getInt(1);
           }
           if (countCOAContract == 0)
           {
               if (this.getDOCUMENTMODE().equals(Indexing.ADDMODE))
               {
                   // try to preload the ol number from the cntinvd table
                   ResultSet resultsInvoice = getContractDataModel().selectInvoiceDetailsStatement();
                   if (resultsInvoice.next())
                   {
                       this.setOFFERINGLETTER(resultsInvoice.getString(4).trim());
                   }
               }
           }
           else
           {
        	   //TODO GILCN IMPLEMENT THIS IN FRONT END
              // aViewFrame.setInvoiceFieldsEditable(false);
               
           }
           // load the contract if it exists
           results = getContractDataModel().selectByObjectIdStatement();
           if (!results.next())
           {
        	   logger.info("Contract " + this.getCONTRACTNUMBER() +" "+ this.getCOUNTRY()+" not found in GAPTS");
        	   this.setOLDCONTRACTNUMBER(this.getCONTRACTNUMBER());
        	   this.setOLDCOUNTRY(newCountry);
        	   this.setDOCUMENTMODE(Indexing.ADDMODE);
        	   this.setDIRTYFLAG(Boolean.TRUE);
           } else
           {
        	   logger.info("Loading Contract " + this.getCONTRACTNUMBER() +" "+ this.getCOUNTRY()+" from GAPTS");
        	   this.setDOCUMENTMODE(Indexing.UPDATEMODE);
        	   this.setDIRTYFLAG(Boolean.FALSE);
               
               // store the new contract number
               String newContractNumber = this.getCONTRACTNUMBER();
               String oldContractNumber = results.getString(1).trim();
               this.setOLDCONTRACTNUMBER(oldContractNumber);
               
               //check for amount changes
               RegionalBigDecimal totalAmount = new RegionalBigDecimal(results.getString(2), RegionalBigDecimal.PERIODDECIMALSEPARATOR);
               RegionalBigDecimal balance = new RegionalBigDecimal(results.getString(3), RegionalBigDecimal.PERIODDECIMALSEPARATOR);

               // load non index fields from the database
               this.setAMOUNT(totalAmount.toString());
               this.setCURRENCY(results.getString(4).trim());
               String customerName = results.getString(5).trim();
               String customerNumber = results.getString(6).trim();
               String offeringLetter = results.getString(7).trim();
               this.setFOLDERID(results.getString(8).trim());
               String oldCountry = results.getString(10).trim();
               this.setOLDCOUNTRY(oldCountry);
               this.setCOUNTRY(oldCountry);
               
               
               this.setOLDCUSTOMERNAME(customerName);
                       

               // check for changed fields from the index
               if (!customerName.trim().equals(this.getCUSTOMERNAME()))
               {
            	   this.setDIRTYFLAG(Boolean.TRUE);
               }
               if (!customerNumber.trim().equals(this.getCUSTOMERNUMBER()))
               {
            	   this.setDIRTYFLAG(Boolean.TRUE);
               }
               if (!offeringLetter.trim().equals(this.getOFFERINGLETTER()))
               {
            	   this.setDIRTYFLAG(Boolean.TRUE);
               }

               // on a coa contract, pull the values from the db instead of the
               // cm index
               if (isCOAContract)
               {
            	   this.setCUSTOMERNAME(customerName);
            	   this.setCUSTOMERNUMBER(customerNumber);
            	   this.setOFFERINGLETTER(offeringLetter);
            	   this.setDIRTYFLAG(Boolean.TRUE);
               }
               this.setOLDOFFERINGLETTER(offeringLetter);
               //String payDate = aDataModel.getPAYMENTDATE();
               boolean paymentDone =  false;
               boolean partiallyPaid = false;
               if(totalAmount.compareTo(balance) > 0 && balance.compareTo(RegionalBigDecimal.ZERO)>0){
               	partiallyPaid = true;
               }
               
               this.setBALANCE(balance.toString());

               
               // find the folder id in cm
               selectFolderId();
               
               if(totalAmount.compareTo(balance) == 0){
               	this.setNETEQBAL("Y"); 
               }
               
               if(balance.compareTo(RegionalBigDecimal.ZERO) == 0){
                 	this.setZEROBAL("Y");
              }
             
           }

       } catch (Exception exc) {
           logger.fatal("Error initializing contract: "+ exc.toString(),exc);
           return false;
       }
           
       isWorkAccess(this.getUSERID());     
       
       logger.info("Contract Values Loaded");
       
       return true;
   }
   

   public void updateIndexValues() throws Exception	 {
   	
	   getContractDataModel().updateIndexValues();
   }

   
   public void saveDocument()  throws Exception   {
	   
       try  {
       			getContractDataModel().saveDocument();
       } catch (Exception sqx)  {
	       	logger.fatal(sqx);
	       	throw sqx;
       }
   }

   public boolean validateInput() throws Exception
   {
	   
	   logger.info("Validating Data Started");

       ResultSet results;
       try
       {
           results = getContractDataModel().selectByContractStatement();
           if (results.next())
           {
               if (!results.getString(3).equals(this.getOBJECTID()))
               {
            	   validationIsDuplicateContractNumber = true;
               }
           }
       } catch (SQLException e)
       {
    	   validationIsDuplicateContractNumber = true;
       }

    	   
           if (getOFFERINGLETTER().trim().length() > 0)
           {
           	try
           	{   
           		//Story 1768398 GIL - additional change for GCMS JR
           		if (getHybridCountry()){
           			if(!selectOfferingLetterNum()){ 
           				if(!selectSignOfferingLetter()){  
           					if(!selectCountersignOfferingLetter()){
           					}
           				}
           			}
           		}else{
           			selectOfferingLetterId();
           		}
           		//End Story 1768398
           	} catch (Exception exc)
           	{
           		validationIsOfferLetterValidHybr = false;
           	}
           	if (getOFFERINGLETTERID()==null || getOFFERINGLETTERID().trim().length() == 0)
           	{
           		validationIsOfferLetterInCM = false;
           	}
           
           
       }
       // added for GIP256
      	// validate the invoices on the cm server
       	Invoice aModel = null;
       	ArrayList<ContractInvoice> newInvList = this.getInvoicesList();
       	Iterator<ContractInvoice>  invItr = newInvList.iterator();
       	while(invItr.hasNext())
       	{
       		ContractInvoice contractInvoice =(ContractInvoice)(invItr.next());
       		
       		//aModel = new Invoice(this.getCOUNTRY());
       		aModel = new Invoice();
       		aModel.setOBJECTID(contractInvoice.getOBJECTID());      
       		aModel.setINVOICENUMBER(contractInvoice.getINVOICENUMBER());
       		aModel.setINVOICEDATE(contractInvoice.getINVOICEDATE());
       		aModel.setCONTRACTNUMBER(contractInvoice.getCONTRACTNUMBER());
       		
       		if ((aModel.getINVOICENUMBER().trim().length() > 0) && (aModel.getOBJECTID()==null || aModel.getOBJECTID().trim().length() == 0))
       		{
       			try {
       			    aModel.setCOUNTRY(this.getCOUNTRY());
       			    aModel.setLocale(this.getLocale());
       			    aModel.setDatastore(this.getDatastore());
           			aModel.selectInvoiceId();
           			contractInvoice.setOBJECTID(aModel.getOBJECTID());
           			if (aModel.getOBJECTID().trim().length() == 0) {
           				contractInvoice.getErrors().add("Invoice " + aModel.getINVOICENUMBER() + " / " + aModel.getINVOICEDATE() + " does not exist in Content Manager");
           				//TODO GILCN SEE HOW/IF TO REMOVE INVOICE FROM THE LIST
           				//removeInvoice (aModel);
           				
           				//return false;
           			} else {
           			// Added for GIP343 
            			// check if the invoice balance is greter than 0 otherwise throw the error message. 
           			contractInvoice.getErrors().add(checkInvoiceBalance(aModel));
           			}
            			
       			} catch (Exception e) {
       				throw new Exception("Error validating Invoices: " + e.getMessage(),e);
  
       			}
       		}
       	}
       
       
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
   
      validationIsDocumentIndexedProperly = exists;
      
      logger.info("Validating Data Finished");
      

       return true;
   }
   
   
   private String checkInvoiceBalance(Invoice aModel) throws Exception  {
	   
	   	RegionalBigDecimal invBalance = RegionalBigDecimal.ZERO;
	   	StringBuilder error = new StringBuilder("");
	   	
	   	try {
	   		
	   		   ResultSet results = aModel.getInvoiceDataModel().selectByObjectIdStatement();
	           if (results.next()){
	           	 invBalance = new RegionalBigDecimal(DB2.getString(results,6), RegionalBigDecimal.PERIODDECIMALSEPARATOR);
	           }else{
	        	   error.append("Invoice " + aModel.getINVOICENUMBER() + " / " + aModel.getINVOICEDATE() + " does not exist in WINVCNTL table. \n");
	           }
	           // check the invoice balance
	  			if(invBalance.compareTo(RegionalBigDecimal.ZERO) == 0){
					 error.append("Invoice " + aModel.getINVOICENUMBER() + " / " + aModel.getINVOICEDATE() + " balance is $0.");
				}
	   	
	   	} catch(Exception e){
	   		logger.fatal("Error Checking Invoice Balance:" + e.getMessage(),e);
	   		throw new Exception("Error Checking Invoice Balance: " + e.getMessage());
	   	}
	   	
	   	return error.toString();
   }
   
   public DOCUMENT queryDocument() throws Exception  {
   	
	   	ContentManagerImplementation cm  = new ContentManagerImplementation(this.getDatastore());
	   	
	   	if (getOBJECTID().trim().length() == 0) {
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

   
   
   //Story 1768398 GIL - additional change for GCMS JR
   public boolean selectOfferingLetterNum() throws Exception  {
	   
	   ContentManagerImplementation cm  = new ContentManagerImplementation(this.getDatastore());
       String[] fields = { "Offering Letter Number" };
       String[] values = { getOFFERINGLETTER() };
       String objectXML = cm.executeQuery("Offering Letter " + getCOUNTRY(), fields, values,this.getDatastore());
       String objectId = getObjectId(objectXML);
       
       if (objectId.trim().length() > 0){
	       	setOFFERINGLETTERID(objectId);
	       	return true; 
       }else{
       		return false; 
       }
       
   }
   
   public boolean selectSignOfferingLetter() throws Exception  {
	   
	   ContentManagerImplementation cm  = new ContentManagerImplementation(this.getDatastore());
       String[] fields = { "GCPS OL Number" };
       String[] values = { getOFFERINGLETTER() };
       String objectXML = cm.executeQuery(getCOUNTRY()+"SignOL", fields, values,this.getDatastore());
       String objectId = getObjectId(objectXML);
       if (objectId.trim().length() > 0){
	       	setOFFERINGLETTERID(objectId);
	       	return true; 
       }else{
       		return false; 
       }
   }
   
   public boolean selectCountersignOfferingLetter() throws Exception  {
	   
	   ContentManagerImplementation cm  = new ContentManagerImplementation(this.getDatastore());
       String[] fields = { "GCPS OL Number" };
       String[] values = { getOFFERINGLETTER() };
       String objectXML = cm.executeQuery(getCOUNTRY()+"CountersignOL", fields, values,this.getDatastore());
       String objectId = getObjectId(objectXML);
       
       if (objectId.trim().length() > 0){
	       	setOFFERINGLETTERID(objectId);
	       	return true; 
       }else{
       		return false; 
       }
       
   }
   public boolean getHybridCountry()  throws Exception  {
	   
       try
       {
       	// load the field options
           String Hcountry;
           ResultSetCache cachedResults = getContractDataModel().selectFieldhybridStatement();
           while (cachedResults.next()){
           	Hcountry = cachedResults.getString(1);
           	if (Hcountry.equals(getCOUNTRY())){
           		return true; 
           	}                          
           }
       } catch (Exception exc) {
           logger.fatal(exc.toString(),exc);
           throw new Exception("Error Checking Invoice Balance: " + exc.getMessage());
       }
       return false;
   }
   
   
}