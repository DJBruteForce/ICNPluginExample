package com.ibm.gil.business;

import java.io.ByteArrayInputStream;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;



import com.google.gson.annotations.Expose;
import com.ibm.gil.model.InvoiceDataModel;
import com.ibm.gil.util.RegionalBigDecimal;
import com.ibm.gil.util.RegionalDateConverter;
import com.ibm.gil.util.UtilGilConstants;
import com.ibm.igf.contentmanager.rmi.ContentManagerImplementation;
import com.ibm.igf.gil.CountryProperties;
import com.ibm.igf.hmvc.DB2;
import com.ibm.igf.hmvc.PluginConfigurationPane;
import com.ibm.igf.hmvc.ResultSetCache;
import com.ibm.w3.financing.tools.gcms.query.results.DOCUMENT;
import com.ibm.w3.financing.tools.gcms.query.results.DocumentRoot;
import com.ibm.w3.financing.tools.gcms.query.results.QUERYRESULTSType;
import com.ibm.w3.financing.tools.gcms.query.results.util.ResultsResourceUtil;


/**
 * @author ferandra
 *
 */
public class Invoice extends Indexing {
	
	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(Invoice.class);
	
	public Invoice() {
		
	}

	public Invoice(String country) {
		
		this.setCOUNTRY(country);
		this.setSILENTCOA("N");
		this.setTOTALAMOUNT(RegionalBigDecimal.ZERO.setScale(2).toString());
		this.setVATAMOUNT(RegionalBigDecimal.ZERO.setScale(2).toString());
		this.setNETAMOUNT(RegionalBigDecimal.ZERO.setScale(2).toString());
		this.setVATTARGETAMOUNT(RegionalBigDecimal.ZERO.setScale(2).toString());
		invoiceDataModel = new InvoiceDataModel(this);
		
	}
	
	private InvoiceDataModel invoiceDataModel = null;
	private boolean isFromGCMS = false;
	private boolean vendorSearchRequired = false;
	private  CountryProperties countryProperties;
	@Expose	private String INVOICENUMBER;
	@Expose private String OLDINVOICENUMBER;
	@Expose private String INVOICEDATE;
	@Expose private String OLDINVOICEDATE;
	@Expose private String TOTALAMOUNT;
	@Expose private String VATAMOUNT;
	@Expose private String VATTARGETAMOUNT;
	@Expose private String  NETAMOUNT;
	@Expose private String CURRENCY;
	@Expose private String DBCR;
	@Expose private String INVOICETYPE;
    
    // 1504959 - Handling of invoice suffix - MANG 08-04-2016
	@Expose private String INVOICESUFFIX;
    // End 1504959
    
	@Expose private String VATCODE;
	@Expose private String POEXCODE;
	@Expose private String SILENTCOA;
	@Expose private String VENDORNAME;
	@Expose private String VENDORNUMBER;
	@Expose private String SRNUMBER;  
	@Expose private String BLOCKINDC;
	@Expose private String OLDVENDORNUMBER;
	@Expose private String OLDVENDORNAME;
	@Expose private String ACCOUNTNUMBER;
	@Expose private String OLDACCOUNTNUMBER;
	@Expose private String CUSTOMERNAME;
	@Expose private String OLDCUSTOMERNAME;
	@Expose private String CUSTOMERNUMBER;
	@Expose private String OCR;
	@Expose private String CONTRACTNUMBER;
	@Expose private String OFFERINGLETTER;
	@Expose private String FOLDERID;
	@Expose private String TEAM;
	@Expose private String CONTRACTID;
	@Expose private String OFFERINGLETTERID;
    private String LASTSAVEDCONTRACTNUMBER;
    @Expose private String OCRREQUIRED;
    @Expose private String VENDORCOMMISSION;
    @Expose private String COMPANYCODE;
    @Expose private String OLDCOMPANYCODE;
    @Expose private String TAXSUPPLYDATE;    
    @Expose private String OLDCOUNTRY;
    @Expose private String TAXINVOICENUMBER;
    @Expose private String DISTRIBUTORNUMBER;
    @Expose private String NETEQBAL;
    @Expose private String INVVARAMT;
    @Expose private String INVBALAMT;
    //Begin 1627912
    @Expose private String EFEDATE;
    @Expose private String BILLTO;
    @Expose private String BILLFROM;
    @Expose private String SHIPTO;
    @Expose private String INCGST;
    @Expose private String INSGST;
    @Expose private String INIGST;
    @Expose private String SUPPGSTREGNUMBER;
    @Expose private String LOAN;
    //End 1627912
    
    //Story 1692411 AR - 2 new fields in GIL to be sent to GAPTS/datasets
    @Expose private String CAICAE;
    @Expose private String CAICAEDate;
    //End Story 1692411 

    private int detailCount = 0;
    private Hashtable VATPercentages = null;
    @Expose private Hashtable VATPercentagesStr = null;
    private RegionalBigDecimal VATVariance = RegionalBigDecimal.ZERO;
    
	@Expose 
    private String VATVarianceStr = "0.00";
	private ArrayList<ContractInvoice> invoiceSearchList;
    private ArrayList<FormSelect> countryInfoSelectList;
    private ArrayList<FormSelect> currencyCodeSelectList;
    private ArrayList<FormSelect> invoiceSuffixesSelectList;
    private ArrayList<FormSelect> invoiceTypesSelectList;
    private ArrayList<FormSelect> poexCodesSelectList;
    private PoexCodeFormSelect  defaultPoexCodeForinvoiceTypeCom;
    private PoexCodeFormSelect  defaultPoexCode;
    private ArrayList<FormSelect> vatCodesSelectList;
    private ArrayList<FormSelect> dbCrSelectList;
    private ArrayList<FormSelect> silentCoaSelectList;
    private ArrayList<FormSelect> companyCodeSelectList;
    private ArrayList<FormSelect> distributorCodeSelectList;
    private ArrayList<FormSelect> billFromSelectList;
    private ArrayList<FormSelect> billToSelectList;
    private ArrayList<FormSelect> shipToSelectList;
    private ArrayList<FormSelect> loanSelectList;
    //Story 1768398 GIL - additional change for GCMS JR
    private ResultSetCache fieldhybridResults = null;
    //End Story 1768398  
    private ResultSetCache companyCodesResults= null;
    private ResultSetCache currencyResults = null;
    private ResultSetCache invoiceTypesResults = null;
    //1504959 - Handling of invoice suffix - MANG 08-04-2016
    private ResultSetCache invoiceSuffixesResults = null;
    //End 1504959

    private boolean validationIsOfferLetterInCM = true;
    private boolean validationIsInvoiceNumberChanging = false;
    private boolean validationIsDocumentIndexedProperly = false;
    private boolean validationIsDuplicatedInvoice = false;
    private boolean validationTaxAmountEqualTargetvatQuestion = false;
    private boolean validationIsContractFolderCreated = true;
    private String validationErrorIsContractFolderCreated = "";
	private boolean validationIsContractCreated = true;
	private boolean validationDuplicateContract = false;
	private boolean validationIsOfferLetterValidHybr = true;
	private boolean promptDatePopup = false;
	private boolean promptSupplyDatePopup = false;
	
    private String validationInvVarAmountStr = "0.00";
    private String validationCreditVarAmountStr = "0.00";
    private String validationDebitVarAmountStr = "0.00";
    private String validationNetAmountStr = "0.00";
    private String validationNetBalanceStr= "0.00";
    private boolean validationVarianced = false;
    private boolean validationPaid = false;
    private boolean indiaGSTFieldsVisible = true;
    private boolean indiaGSTFieldsEditable = true;
    @Expose private ArrayList<String> dialogErrorMsgs = new ArrayList<String>();
    @Expose private ArrayList<String> dialogwarnMsgs = new ArrayList<String>();
    private ResultSetCache VATCodesResults = null;
    private ResultSetCache POEXCodesResults = null;
    private ResultSetCache countryResults = null;
    private ResultSetCache ocrRequiredResults = null;
    @Expose private String createdTimeStamp = null;
    
    public Hashtable getVATPercentagesStr() {
		return VATPercentagesStr;
	}

	public void setVATPercentagesStr(Hashtable vATPercentagesStr) {
		VATPercentagesStr = vATPercentagesStr;
	}

	
	public ArrayList<String> getDialogwarnMsgs() {
		return dialogwarnMsgs;
	}

	public void setDialogwarnMsgs(ArrayList<String> dialogwarnMsgs) {
		this.dialogwarnMsgs = dialogwarnMsgs;
	}

	
    public ArrayList<String> getDialogErrorMsgs() {
		return dialogErrorMsgs;
	}

	public void setDialogErrorMsgs(ArrayList<String> dialogErrorMsgs) {
		this.dialogErrorMsgs = dialogErrorMsgs;
	}

	public boolean isValidationDuplicateContract() {
		return validationDuplicateContract;
	}

	public void setValidationDuplicateContract(boolean validationDuplicateContract) {
		this.validationDuplicateContract = validationDuplicateContract;
	}

	public String getValidationErrorIsContractFolderCreated() {
		return validationErrorIsContractFolderCreated;
	}

	public void setValidationErrorIsContractFolderCreated(
			String validationErrorIsContractFolderCreated) {
		this.validationErrorIsContractFolderCreated = validationErrorIsContractFolderCreated;
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
    


	public void setValidationIsContractCreated(boolean validationIsContractCreated) {
		this.validationIsContractCreated = validationIsContractCreated;
	}

	public boolean isValidationIsContractFolderCreated() {
		return validationIsContractFolderCreated;
	}

	public void setValidationIsContractFolderCreated(
			boolean validationIsContractFolderCreated) {
		this.validationIsContractFolderCreated = validationIsContractFolderCreated;
	}

	public boolean isFromGCMS() {
		return isFromGCMS;
	}

	public void setFromGCMS(boolean isFromGCMS) {
		this.isFromGCMS = isFromGCMS;
	}
    
    
    public InvoiceDataModel getInvoiceDataModel() {
    	
    	if (invoiceDataModel == null){
    		invoiceDataModel = new InvoiceDataModel(this);
    		return invoiceDataModel;
    	} else {
    		return invoiceDataModel;
    	}
	}

	public void setInvoiceDataModel(InvoiceDataModel invoiceDataModel) {
		this.invoiceDataModel = invoiceDataModel;
	}

	public ArrayList<FormSelect> getDistributorCodeSelectList() {
		return distributorCodeSelectList;
	}

	public void setDistributorCodeSelectList(
			ArrayList<FormSelect> distributorCodeSelectList) {
		this.distributorCodeSelectList = distributorCodeSelectList;
	}

	public ArrayList<FormSelect> getDbCrSelectList() {
		return dbCrSelectList;
	}

	public void setDbCrSelectList(ArrayList<FormSelect> dbCrSelectList) {
		this.dbCrSelectList = dbCrSelectList;
	}

	public ArrayList<FormSelect> getCompanyCodeSelectList() {
		return companyCodeSelectList;
	}

	public void setCompanyCodeSelectList(ArrayList<FormSelect> companyCodeSelectList) {
		this.companyCodeSelectList = companyCodeSelectList;
	}

	public ArrayList<FormSelect> getCountryInfoSelectList() {
		return countryInfoSelectList;
	}

	public void setCountryInfoSelectList(ArrayList<FormSelect> countryInfoSelectList) {
		this.countryInfoSelectList = countryInfoSelectList;
	}

	public ArrayList<FormSelect> getCurrencyCodeSelectList() {
		return currencyCodeSelectList;
	}

	public void setCurrencyCodeSelectList(
			ArrayList<FormSelect> currencyCodeSelectList) {
		this.currencyCodeSelectList = currencyCodeSelectList;
	}

	public ArrayList<FormSelect> getInvoiceSuffixesSelectList() {
		return invoiceSuffixesSelectList;
	}

	public void setInvoiceSuffixesSelectList(
			ArrayList<FormSelect> invoiceSuffixesSelectList) {
		this.invoiceSuffixesSelectList = invoiceSuffixesSelectList;
	}

	public ArrayList<FormSelect> getInvoiceTypesSelectList() {
		return invoiceTypesSelectList;
	}

	public void setInvoiceTypesSelectList(
			ArrayList<FormSelect> invoiceTypesSelectList) {
		this.invoiceTypesSelectList = invoiceTypesSelectList;
	}

	public ArrayList<FormSelect> getPoexCodesSelectList() {
		return poexCodesSelectList;
	}

	public void setPoexCodesSelectList(ArrayList<FormSelect> poexCodesSelectList) {
		this.poexCodesSelectList = poexCodesSelectList;
	}

	public ArrayList<FormSelect> getVatCodesSelectList() {
		return vatCodesSelectList;
	}

	public void setVatCodesSelectList(ArrayList<FormSelect> vatCodesSelectList) {
		this.vatCodesSelectList = vatCodesSelectList;
	}

    

    // End 1504959
    
    //1627912 [GST] New Fields on APTS Index Invoices Screen (GIL) - MANG 01-27-17
    
	   

    public boolean isVendorSearchRequired() {
		return vendorSearchRequired;
	}

	public void setVendorSearchRequired(boolean vendorSearchRequired) {
		this.vendorSearchRequired = vendorSearchRequired;
	}

	public String getValidationInvVarAmountStr() {
		return validationInvVarAmountStr;
	}

	public void setValidationInvVarAmountStr(String validationInvVarAmountStr) {
		this.validationInvVarAmountStr = validationInvVarAmountStr;
	}

	public String getValidationCreditVarAmountStr() {
		return validationCreditVarAmountStr;
	}

	public void setValidationCreditVarAmountStr(String validationCreditVarAmountStr) {
		this.validationCreditVarAmountStr = validationCreditVarAmountStr;
	}

	public String getValidationDebitVarAmountStr() {
		return validationDebitVarAmountStr;
	}

	public void setValidationDebitVarAmountStr(String validationDebitVarAmountStr) {
		this.validationDebitVarAmountStr = validationDebitVarAmountStr;
	}

	public boolean isValidationVarianced() {
		return validationVarianced;
	}

	public void setValidationVarianced(boolean validationVarianced) {
		this.validationVarianced = validationVarianced;
	}

	public boolean isValidationPaid() {
		return validationPaid;
	}

	public void setValidationPaid(boolean validationPaid) {
		this.validationPaid = validationPaid;
	}

	public RegionalBigDecimal getVATVariance()
    {
        return VATVariance;
    }

    public String getBILLTO() {
		return BILLTO;
	}

	public void setBILLTO(String bILLTO) {
		BILLTO = bILLTO;
	}

	public String getBILLFROM() {
		return BILLFROM;
	}

	public void setBILLFROM(String bILLFROM) {
		BILLFROM = bILLFROM;
	}

	public String getINCGST() {
		return INCGST;
	}

	public void setINCGST(String iNCGST) {
		INCGST = iNCGST;
	}

	public String getINSGST() {
		return INSGST;
	}

	public void setINSGST(String iNSGST) {
		INSGST = iNSGST;
	}

	public String getINIGST() {
		return INIGST;
	}

	public void setINIGST(String iNIGST) {
		INIGST = iNIGST;
	}

	public String getSUPPGSTREGNUMBER() {
		return SUPPGSTREGNUMBER;
	}

	public void setSUPPGSTREGNUMBER(String sUPPGSTREGNUMBER) {
		SUPPGSTREGNUMBER = sUPPGSTREGNUMBER;
	}

	public String getLOAN() {
		return LOAN;
	}

	public void setLOAN(String lOAN) {
		LOAN = lOAN;
	}

	public CountryProperties getCountryProperties() {
		return countryProperties;
	}

	public void setCountryProperties(CountryProperties countryProperties) {
		this.countryProperties = countryProperties;
	}

	public void setVATVariance(RegionalBigDecimal vVATVariance)
    {
        VATVariance = vVATVariance;
        if (vVATVariance!=null)
        	VATVarianceStr = vVATVariance.toString();
    }

    
    //End 1627912

    /**
     * maintains the list of vat code x vat percentages
     */
    public void setVATPercentages(Hashtable percentages)
    {
        VATPercentages = percentages;
    }
   
    
	/*
     * returns the vat percentage that corresponds to the vat code @author
     * SteveBaber
     *  
     */
    public RegionalBigDecimal getVATPercentage()
    {
    	RegionalBigDecimal res = null;
        if (VATPercentages == null) {
        	res= RegionalBigDecimal.ZERO;
        }

        RegionalBigDecimal result = (RegionalBigDecimal) VATPercentages.get(getVATCODE());

        if (result == null){
        	res = RegionalBigDecimal.ZERO;
        }
        else {
            res = (result);
            VATPercentage = res.toString();
        }
        
        
        return res;
    }
    
    
    
    private String VATPercentage = RegionalBigDecimal.ZERO.toString();
    
    
    public void setVATPercentage(String vATPercentage) {
		VATPercentage = vATPercentage;
	}


    public String getINVOICENUMBER() {
		return INVOICENUMBER;
	}
    
    

	public String getEFEDATE() {
		return EFEDATE;
	}


	public void setEFEDATE(String eFEDATE) {
		EFEDATE = eFEDATE;
	}


	public String getSHIPTO() {
		return SHIPTO;
	}


	public void setSHIPTO(String sHIPTO) {
		SHIPTO = sHIPTO;
	}


	public String getCAICAE() {
		return CAICAE;
	}


	public void setCAICAE(String cAICAE) {
		CAICAE = cAICAE;
	}


	public String getCAICAEDate() {
		return CAICAEDate;
	}


	public void setCAICAEDate(String cAICAEDate) {
		CAICAEDate = cAICAEDate;
	}


	public String getOLDACCOUNTNUMBER() {
		return OLDACCOUNTNUMBER;
	}

	public void setOLDACCOUNTNUMBER(String oLDACCOUNTNUMBER) {
		OLDACCOUNTNUMBER = oLDACCOUNTNUMBER;
	}

	public String getOLDVENDORNAME() {
		return OLDVENDORNAME;
	}

	public void setOLDVENDORNAME(String oLDVENDORNAME) {
		OLDVENDORNAME = oLDVENDORNAME;
	}

	public void setINVOICENUMBER(String iNVOICENUMBER) {
		INVOICENUMBER = iNVOICENUMBER;
	}

	public String getOLDINVOICENUMBER() {
		return OLDINVOICENUMBER;
	}

	public void setOLDINVOICENUMBER(String oLDINVOICENUMBER) {
		OLDINVOICENUMBER = oLDINVOICENUMBER;
	}

	public String getINVOICEDATE() {
		return INVOICEDATE;
	}

	public void setINVOICEDATE(String iNVOICEDATE) {
		INVOICEDATE = iNVOICEDATE;
	}

	public String getOLDINVOICEDATE() {
		return OLDINVOICEDATE;
	}

	public void setOLDINVOICEDATE(String oLDINVOICEDATE) {
		OLDINVOICEDATE = oLDINVOICEDATE;
	}

	public String getTOTALAMOUNT() {
		return TOTALAMOUNT;
	}

	public void setTOTALAMOUNT(String tOTALAMOUNT) {
		TOTALAMOUNT = tOTALAMOUNT;
	}

	public String getVATAMOUNT() {
		return VATAMOUNT;
	}

	public void setVATAMOUNT(String vATAMOUNT) {
		VATAMOUNT = vATAMOUNT;
	}

	public String getVATTARGETAMOUNT() {
		return VATTARGETAMOUNT;
	}

	public void setVATTARGETAMOUNT(String vATTARGETAMOUNT) {
		VATTARGETAMOUNT = vATTARGETAMOUNT;
	}

	public String getNETAMOUNT() {
		return NETAMOUNT;
	}

	public void setNETAMOUNT(String nETAMOUNT) {
		NETAMOUNT = nETAMOUNT;
	}

	public String getCURRENCY() {
		return CURRENCY;
	}

	public void setCURRENCY(String cURRENCY) {
		CURRENCY = cURRENCY;
	}

	public String getDBCR() {
		return DBCR;
	}

	public void setDBCR(String dBCR) {
		DBCR = dBCR;
	}

	public String getINVOICETYPE() {
		return INVOICETYPE;
	}

	public void setINVOICETYPE(String iNVOICETYPE) {
		INVOICETYPE = iNVOICETYPE;
	}

	public String getINVOICESUFFIX() {
		return INVOICESUFFIX;
	}

	public void setINVOICESUFFIX(String iNVOICESUFFIX) {
		INVOICESUFFIX = iNVOICESUFFIX;
	}

	public String getVATCODE() {
		return VATCODE;
	}

	public void setVATCODE(String vATCODE) {
		VATCODE = vATCODE;
	}

	public String getPOEXCODE() {
		return POEXCODE;
	}

	public void setPOEXCODE(String pOEXCODE) {
		POEXCODE = pOEXCODE;
	}

	public String getSILENTCOA() {
		return SILENTCOA;
	}

	public void setSILENTCOA(String sILENTCOA) {
		SILENTCOA = sILENTCOA;
	}

	public String getVENDORNAME() {
		return VENDORNAME;
	}

	public void setVENDORNAME(String vENDORNAME) {
		VENDORNAME = vENDORNAME;
	}

	public String getVENDORNUMBER() {
		return VENDORNUMBER;
	}

	public void setVENDORNUMBER(String vENDORNUMBER) {
		VENDORNUMBER = vENDORNUMBER;
	}

	public String getSRNUMBER() {
		return SRNUMBER;
	}

	public void setSRNUMBER(String sRNUMBER) {
		SRNUMBER = sRNUMBER;
	}

	public String getBLOCKINDC() {
		return BLOCKINDC;
	}

	public void setBLOCKINDC(String bLOCKINDC) {
		BLOCKINDC = bLOCKINDC;
	}

	public String getOLDVENDORNUMBER() {
		return OLDVENDORNUMBER;
	}

	public void setOLDVENDORNUMBER(String oLDVENDORNUMBER) {
		OLDVENDORNUMBER = oLDVENDORNUMBER;
	}

	public String getACCOUNTNUMBER() {
		return ACCOUNTNUMBER;
	}

	public void setACCOUNTNUMBER(String aCCOUNTNUMBER) {
		ACCOUNTNUMBER = aCCOUNTNUMBER;
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

    public String getOLDCUSTOMERNAME() {
		return OLDCUSTOMERNAME;
	}

	public void setOLDCUSTOMERNAME(String oLDCUSTOMERNAME) {
		OLDCUSTOMERNAME = oLDCUSTOMERNAME;
	}

	public String getOCR() {
		return OCR;
	}

	public void setOCR(String oCR) {
		OCR = oCR;
	}

	public String getCONTRACTNUMBER() {
		return CONTRACTNUMBER;
	}

	public void setCONTRACTNUMBER(String cONTRACTNUMBER) {
		CONTRACTNUMBER = cONTRACTNUMBER;
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

	public String getCONTRACTID() {
		return CONTRACTID;
	}

	public void setCONTRACTID(String cONTRACTID) {
		CONTRACTID = cONTRACTID;
	}

	public String getOFFERINGLETTERID() {
		return OFFERINGLETTERID;
	}

	public void setOFFERINGLETTERID(String oFFERINGLETTERID) {
		OFFERINGLETTERID = oFFERINGLETTERID;
	}

	public String getLASTSAVEDCONTRACTNUMBER() {
		return LASTSAVEDCONTRACTNUMBER;
	}

	public void setLASTSAVEDCONTRACTNUMBER(String lASTSAVEDCONTRACTNUMBER) {
		LASTSAVEDCONTRACTNUMBER = lASTSAVEDCONTRACTNUMBER;
	}

	public String getOCRREQUIRED() {
		return OCRREQUIRED;
	}

	public void setOCRREQUIRED(String oCRREQUIRED) {
		OCRREQUIRED = oCRREQUIRED;
	}

	public String getVENDORCOMMISSION() {
		return VENDORCOMMISSION;
	}

	public void setVENDORCOMMISSION(String vENDORCOMMISSION) {
		VENDORCOMMISSION = vENDORCOMMISSION;
	}

	public String getCOMPANYCODE() {
		return COMPANYCODE;
	}

	public void setCOMPANYCODE(String cOMPANYCODE) {
		COMPANYCODE = cOMPANYCODE;
	}

	public String getOLDCOMPANYCODE() {
		return OLDCOMPANYCODE;
	}

	public void setOLDCOMPANYCODE(String oLDCOMPANYCODE) {
		OLDCOMPANYCODE = oLDCOMPANYCODE;
	}

	public String getTAXSUPPLYDATE() {
		return TAXSUPPLYDATE;
	}

	public void setTAXSUPPLYDATE(String tAXSUPPLYDATE) {
		TAXSUPPLYDATE = tAXSUPPLYDATE;
	}

	public String getOLDCOUNTRY() {
		return OLDCOUNTRY;
	}

	public void setOLDCOUNTRY(String oLDCOUNTRY) {
		OLDCOUNTRY = oLDCOUNTRY;
	}

	public String getTAXINVOICENUMBER() {
		return TAXINVOICENUMBER;
	}

	public void setTAXINVOICENUMBER(String tAXINVOICENUMBER) {
		TAXINVOICENUMBER = tAXINVOICENUMBER;
	}

	public String getDISTRIBUTORNUMBER() {
		return DISTRIBUTORNUMBER;
	}

	public void setDISTRIBUTORNUMBER(String dISTRIBUTORNUMBER) {
		DISTRIBUTORNUMBER = dISTRIBUTORNUMBER;
	}

	public String getNETEQBAL() {
		return NETEQBAL;
	}

	public void setNETEQBAL(String nETEQBAL) {
		NETEQBAL = nETEQBAL;
	}

	public String getINVVARAMT() {
		return INVVARAMT;
	}

	public void setINVVARAMT(String iNVVARAMT) {
		INVVARAMT = iNVVARAMT;
	}

	public String getINVBALAMT() {
		return INVBALAMT;
	}

	public void setINVBALAMT(String iNVBALAMT) {
		INVBALAMT = iNVBALAMT;
	}

	public int getDetailCount() {
		return detailCount;
	}

	public void setDetailCount(int detailCount) {
		this.detailCount = detailCount;
	}

	public Hashtable getVATPercentages() {
		return VATPercentages;
	}
	
    public boolean isCommissionInvoice()
    {
        // TODO Auto-generated method stub
        if (getINVOICETYPE().trim().equals("COM"))
            return true;

        if (getINVOICETYPE().trim().equals("STP"))
            return true;

        return false;
    }
    
    public boolean isCreditInvoice()
    {
        return getDBCR().trim().equals("CR");
    }
    
    
    public boolean isIBMInvoice()
    {
    	return getINVOICETYPE().trim().equals("IBM");
    }
    
    public boolean isCompanyCodeOf0001(){
    	
    	return getCOMPANYCODE().trim().equals("0001");
    }    

    public boolean isCOMInvoice(){
        if (getINVOICETYPE().trim().equals("COM"))
            return true;   
        
        return false;
    }
    
    
    //1664571 [GST] GIL should show different input invoice screens based on the effective date of the invoice
    private void EffectiveINDate() throws Exception  {
    	
    	try
        {
    		java.util.Date invoiceDate = null;
    		invoiceDate = RegionalDateConverter.parseDate("GUI",getINVOICEDATE());
    		String EffDate = PluginConfigurationPane.getPropertiesMap().get(UtilGilConstants.INDIA_GST_EFFECTIVE_DATE);
    		java.util.Date EffectiveDate = RegionalDateConverter.parseDate("GUI", EffDate);
    	    
    		if (invoiceDate.getTime() >= EffectiveDate.getTime()){
    			setEFEDATE("Y") ; 
            }else{
            	setEFEDATE("N") ; 
            }
        } catch (Exception exc) {
            logger.fatal("Error checking EffectiveINDate: " + exc.toString());
            throw exc;
        }
    }
    //End 1664571 
    
    public boolean isEXEMPTorStartswithDEVofSRNumber(){
    	
    	if(getSRNUMBER().trim().equals("EXEMPT"))
    		return true;
    	if(getSRNUMBER().startsWith("DEV"))
    		return true;
    	
    	return false;
    }
    
    public String getHash() {
        return getINVOICENUMBER() + "~" + getINVOICEDATE();
    }

	/*
     * overrides the contract number to be blank, 1 contract number, or the word
     * multiple
     */
    public void overrideContractNumber(){
    	
        if (getDetailCount() == 0)
            setCONTRACTNUMBER("");
        if (getDetailCount() > 1)
            setCONTRACTNUMBER("MULTIPLE");
    }


    
/*    *//**
     * load the currency values from the database and populate the gui
 * @throws Exception 
     *  
     */
    public void initializeCurrencies() throws Exception  {
    	
    	FormSelect code = null;
    	ArrayList<FormSelect> codes = new ArrayList<FormSelect>();
    	boolean hasDefault = false;
    	 boolean hasSelectedValue = (this.CURRENCY!=null && !"".equals(this.CURRENCY));
        try
        {

            // load the currency types
            String defaultCurrency = " ";
            String currencyCode;

            ResultSetCache cachedResults = getInvoiceDataModel().selectCurrencyCodesStatement();
            
            while (cachedResults.next())
            {
            	code = new FormSelect();
                currencyCode = cachedResults.getString(1);
                code.setValue(currencyCode);
                code.setLabel(currencyCode);
                
                if( hasSelectedValue &&  this.CURRENCY.equalsIgnoreCase(currencyCode)	) {
                	code.setSelected(true);
                	hasDefault = true;
                } else if (!hasSelectedValue && cachedResults.getString(2).equals("*"))   {
                			code.setSelected(true);
                			hasDefault = true;
                } 
                codes.add(code);
            }

        } catch (Exception exc) {
        	logger.fatal(exc.toString());
        	throw exc;
        }
        
        this.currencyCodeSelectList = codes;
    }
    
    public void initializePOEXCodes() throws Exception
    {
    	

        ArrayList<FormSelect> codes = new ArrayList<FormSelect>();
        FormSelect defaultCodeGeneral = null;
        FormSelect defaultCodeInvoiceTypeCom = null;
        boolean hasMatch = false;
        boolean hasSelectedValue = (this.POEXCODE!=null && !"".equals(this.POEXCODE.trim()));
        boolean isInvoiceTypeCom =  (this.INVOICETYPE!=null && this.INVOICETYPE.equalsIgnoreCase("COM"));
        this.poexCodesSelectList = new ArrayList<FormSelect>();
        ResultSetCache cachedResults =null;
        
        try
        {
           
            cachedResults = getInvoiceDataModel().selectPOEXCodesStatement();
            while (cachedResults.next())
            {
            	FormSelect code = new PoexCodeFormSelect();
            	String value = cachedResults.getString(2);
            	String label = cachedResults.getString(4);
            	
            	code.setValue(value);
            	code.setLabel(label);
            	String defaultCodeStr = cachedResults.getString(3);
            	
            	// story 1582060 
                if (defaultCodeStr.equals("R") ) {
                
                	this.defaultPoexCode = new PoexCodeFormSelect();
                	this.defaultPoexCode.setValue(value);
                	this.defaultPoexCode.setLabel(label);
                	this.defaultPoexCode.setSelected(true);
                	
                } else if (defaultCodeStr.equals("C")) {
                	
                	this.defaultPoexCodeForinvoiceTypeCom = new PoexCodeFormSelect();
                	this.defaultPoexCodeForinvoiceTypeCom.setValue(value);
                	this.defaultPoexCodeForinvoiceTypeCom.setLabel(label);
                	this.defaultPoexCodeForinvoiceTypeCom.setSelected(true);
                }
             
            	if (hasSelectedValue && this.POEXCODE.equalsIgnoreCase(code.getValue())){
            		
            		hasMatch = true;
            		code.setSelected(true);
            	}
               
            	codes.add(code);
            }
            
            this.poexCodesSelectList = codes;
            
            if(!hasMatch) {
            
			            Iterator<FormSelect> it = codes.iterator();
			            while(it.hasNext()) {
			            	
			            	PoexCodeFormSelect poexcode = (PoexCodeFormSelect)it.next();
			            	
			            	if(isInvoiceTypeCom) {
				            	if (this.defaultPoexCodeForinvoiceTypeCom!=null &&  poexcode.getValue().equalsIgnoreCase(this.defaultPoexCodeForinvoiceTypeCom.getValue()) ) {
				            		poexcode.setSelected(true);
				            	}
			            	
			            	} else {
				            	if (this.defaultPoexCode!=null &&  poexcode.getValue().equalsIgnoreCase(this.defaultPoexCode.getValue()) ) {
				            		poexcode.setSelected(true);
				            	}
			            	}
			            }
            }

         // end story 1582060 
        } catch (Exception exc) {
        	logger.fatal(exc.toString());
        	throw exc;
        } finally {
        	cachedResults=null;
        }

    }
    
    public  void initializeInvoiceTypes() throws Exception {
    	
        ArrayList<FormSelect> codes = new ArrayList<FormSelect>();
        FormSelect code = null;
        FormSelect codeDefault = null;
        int matchCount = 0;
        ArrayList<FormSelect> resultinvoicetypes = null ;
        ResultSet defaultResults = null;
        ResultSetCache cachedResults =null;
        boolean hasSelectedValue = (this.INVOICETYPE!=null && !"".equals(this.INVOICETYPE));
    	
        try
        {

           String InvoiceTypestring = "";
            resultinvoicetypes  = new ArrayList<FormSelect>();
            String defaultInvoiceType = "VEN";
            String invoiceType;
            cachedResults = getInvoiceDataModel().selectInvoiceTypesStatement();
            
            if (hasSelectedValue) {

                while (cachedResults.next())
                {
                	invoiceType = cachedResults.getString(1);
                	
                	code = new FormSelect();
                	code.setValue(invoiceType);
                    code.setLabel(invoiceType);

                    if(this.INVOICETYPE.equalsIgnoreCase(invoiceType)){
                    	code.setSelected(true);
                    }
                    
                    codes.add(code);
                }
                
            } else {
            
		            defaultResults = getInvoiceDataModel().selectDefaultInvoiceTypesStatement();
		            if (defaultResults.next())
		            {
		                InvoiceTypestring = defaultResults.getString(1);
		            	
		            	codeDefault = new FormSelect();
		            	codeDefault.setValue(InvoiceTypestring);
		            	codeDefault.setLabel(InvoiceTypestring);
		            	codeDefault.setSelected(true);
		            } else {
		            	codeDefault = new FormSelect();
		            	codeDefault.setValue(defaultInvoiceType);
		            	codeDefault.setLabel(defaultInvoiceType);
		            	codeDefault.setSelected(true);
		            }
		            
	                while (cachedResults.next())
	                {
	                	invoiceType = cachedResults.getString(1);
	                	
	                	code = new FormSelect();
	                	code.setValue(invoiceType);
	                    code.setLabel(invoiceType);                   
	                    codes.add(code);
	                }
		            for (FormSelect codeFor : codes) {
		            	if (codeFor.getValue().equalsIgnoreCase(codeDefault.getValue())){
		            		
		            		codeFor.setSelected(true);
		            		resultinvoicetypes.add(codeFor);
		            		matchCount++;
		            	} else {
		            		resultinvoicetypes.add(codeFor);
		            	}
		               
		            }
		            
		            if (matchCount == 0) resultinvoicetypes.add(codeDefault);
            }

        } catch (Exception exc) {
            logger.fatal(exc.toString());
            throw exc;
        } finally{
        	cachedResults=null;
        }
        
        invoiceTypesSelectList = codes;
    }
    
	public void  initializeCompanyCode() throws Exception {
		
        FormSelect code = null;
        FormSelect defaultCode = null;
        boolean hasDefaultSelected = false;
        ResultSetCache defaultCompanyCodeResults =null;
        ArrayList<FormSelect> companyCodesList = null;
        boolean hasSelectedValue = (this.COMPANYCODE!=null && !"".equals(this.COMPANYCODE));
        
		try {

			String defaultCompanyCode = " ";
			companyCodesList = new ArrayList<FormSelect>();
			defaultCompanyCodeResults = getInvoiceDataModel().selectCountryInfoStatement();
			
			
			if (defaultCompanyCodeResults.next()) {
				
				defaultCompanyCode = defaultCompanyCodeResults.getString(2);
				defaultCode = new FormSelect();
				defaultCode.setValue(defaultCompanyCode);
				defaultCode.setLabel(defaultCompanyCode);;
				
				if (hasSelectedValue &&  defaultCompanyCode.equalsIgnoreCase(this.COMPANYCODE))	{
					defaultCode.setSelected(true);
				 }	else if (!hasSelectedValue){
					 defaultCode.setSelected(true);
				 }
				
				//companyCodesList.add(code);
			} 

			ResultSetCache cachedResults = getInvoiceDataModel().selectCompanyCodesStatement();
		
			while (cachedResults.next()) {
				
				String result = cachedResults.getString(1);
				
				if (defaultCode!=null && defaultCode.getValue().equalsIgnoreCase(result)){
					
					companyCodesList.add(defaultCode);
					hasDefaultSelected = true;
					continue;
				}
				
				code = new FormSelect();
				code.setValue(result);
				code.setLabel(result);
				if (hasSelectedValue &&  result.equalsIgnoreCase(this.COMPANYCODE)){
					
					hasDefaultSelected = true;
					code.setSelected(true);
				
				}
				
				
				companyCodesList.add(code);
			}

			
			if (!hasDefaultSelected && defaultCode!=null) companyCodesList.add(defaultCode);

		} catch (Exception exc) {
			logger.fatal(exc.toString());
			throw exc;
		} finally{
			defaultCompanyCodeResults=null;
		}
		
		this.companyCodeSelectList = companyCodesList;
	}
	

    /**
     * load the country defaults from the database and populate the data model
     * @throws Exception 
     *  
     */
    public void initializeCountryInfo() throws Exception {
        
    	ResultSetCache ocrRequiredResults =null;
    	try
        {

            // load the country defaults
           // ResultSetCache countryResults = aDataModel.selectCountryInfoStatement();
            
            ocrRequiredResults = getInvoiceDataModel().selectOCRRequiredStatement();
            if (ocrRequiredResults.next())
            {
                this.setOCRREQUIRED(ocrRequiredResults.getString(1));
            }
        } catch (Exception exc){
            logger.fatal(exc.toString());
            throw exc;
        } finally {
        	 ocrRequiredResults = null;
        }
    }
    
    
    public  void initializeInvoiceSuffixes() throws Exception   {
    	
    	 ArrayList<FormSelect> codes = null;
    	 boolean hasSelectedValue = (this.INVOICESUFFIX!=null && !"".equals(this.INVOICESUFFIX));
    	 boolean hasMatch = false;
    	 ResultSetCache cachedResults = null;
    	 
        try {

            String invoiceSuffix;
            codes = new ArrayList<FormSelect> ();
            //FormSelect code = null;
            FormSelect defaultCode = null;
            defaultCode = new FormSelect();
            defaultCode.setValue("");
            defaultCode.setLabel("");
           
            cachedResults = getInvoiceDataModel().selectInvoiceSuffixesStatement();
            while (cachedResults.next())
            {
            	FormSelect code = new FormSelect();
            	invoiceSuffix = cachedResults.getString(1);
            	if (cachedResults.getString(2).equals("*")){
            		defaultCode.setLabel(invoiceSuffix);
            		defaultCode.setValue(invoiceSuffix);
            		codes.add(defaultCode);
           	    } else {

            	code.setValue(invoiceSuffix);
            	code.setLabel(invoiceSuffix);
            	codes.add(code);
           	    }
            	
            	if (hasSelectedValue && this.INVOICESUFFIX.equalsIgnoreCase(invoiceSuffix)){
            		//code.setSelected(true);
            		FormSelect codeAux = new FormSelect();
            		codeAux.setLabel(this.INVOICESUFFIX);
            		codeAux.setValue(this.INVOICESUFFIX);
            		int index = codes.indexOf(codeAux);
            		if(index>=0) {
            			codes.get(index).setSelected(true);
            			hasMatch = true;
            		}
            	}
            }
            
            if (!hasMatch){
            	defaultCode.setSelected(true);            	
            }

        } catch (Exception exc) {
            logger.fatal(exc.toString());
            throw exc;
        }finally{
        	cachedResults=null;
        }
        this.invoiceSuffixesSelectList = codes;
    }
    
    private void initializeBillFields()  throws Exception {
    	
    	ResultSetCache cachedResults = null;
        try
        {
          
            String BillSuffix;
            billFromSelectList = new ArrayList<FormSelect> ();
            shipToSelectList  = new ArrayList<FormSelect> ();
            //Story 1699089 India GST:  State table changes for GIL
            billToSelectList = new ArrayList<FormSelect> ();
            // End Story 1699089 
            
            cachedResults = getInvoiceDataModel().selectBillSuffixesStatement();
            while (cachedResults.next()) {
            	BillSuffix = cachedResults.getString(1);
            	
            	FormSelect codeBillFrom = new FormSelect();
            	codeBillFrom.setLabel(BillSuffix);
            	codeBillFrom.setValue(BillSuffix);
            	if(this.getBILLFROM()!=null && this.getBILLFROM().equalsIgnoreCase(BillSuffix)) {
            		codeBillFrom.setSelected(true);
            	}
            	billFromSelectList.add(codeBillFrom);
            	
            	FormSelect codeShipTo  = new FormSelect();
            	codeShipTo.setLabel(BillSuffix);
            	codeShipTo.setValue(BillSuffix);
            	if(this.getSHIPTO()!=null && this.getSHIPTO().equalsIgnoreCase(BillSuffix)) {
            		codeShipTo.setSelected(true);
            	}
            	shipToSelectList.add(codeShipTo);  

            	FormSelect codeBillTo  = new FormSelect();            	
            	codeBillTo.setLabel(BillSuffix);
            	codeBillTo.setValue(BillSuffix);

            	 //Story 1699089 India GST:  State table changes for GIL
            	 if (cachedResults.getString(2).equals("Y")){
                 	if(this.getBILLTO()!=null && this.getBILLTO().equalsIgnoreCase(BillSuffix)) {
                 		codeBillTo.setSelected(true);
                 	}
                 	billToSelectList.add(codeBillTo);
            	 }  
            	 // End Story 1699089 
            }
        } catch (Exception exc) {
        	 logger.fatal("Error in initializeBillFields:" + exc.toString());
        	 exc.printStackTrace();
        	 throw exc;
        }finally{
        	cachedResults=null;
        }
    }
    
    
    private void initializeLoanField() throws Exception {
        
    	try {
    		loanSelectList = new ArrayList<FormSelect>();
    		String arrLoan[]={"N","Y"};
    		for (String s: arrLoan) {           
            	FormSelect codeLoan  = new FormSelect();
            	if(s.equalsIgnoreCase(this.getLOAN())){
            		codeLoan.setSelected(true);
            	}
            		
            	codeLoan.setLabel(s);
            	codeLoan.setValue(s);   
            	loanSelectList.add(codeLoan);
    	    }
        } catch (Exception exc){
            logger.fatal("Error in initializeLoanField:" + exc.toString());
            exc.printStackTrace();
            throw exc;

        }
    }

    
    public void selectInvoiceId() throws Exception {
    	
    	ContentManagerImplementation cm  = new ContentManagerImplementation(this.getDatastore());
        String invoiceDate = RegionalDateConverter.convertDate("GUI", "CM",  getINVOICEDATE());
        String[] fields = { "Invoice Number", "Invoice Date" };
        String[] values = { getINVOICENUMBER(), invoiceDate};
        String objectXML = cm.executeQuery("Invoice " +  getCOUNTRY(), fields, values, getDatastore());
        String objectId = getObjectId(objectXML);
        setOBJECTID(objectId);
    }
    
    /*
     * retrieves the offering letter object id if it exists
     */
    public void selectOfferingLetter() throws Exception
    {
    	
    	ContentManagerImplementation cm  = new ContentManagerImplementation(this.getDatastore());
        String[] fields = { "Offering Letter Number" };
        String[] values = { getOFFERINGLETTER() };
        String objectXML = cm.executeQuery("Offering Letter " + getCOUNTRY(), fields, values,getDatastore());
        String objectId = getObjectId(objectXML);
        setOFFERINGLETTERID(objectId);
    }
    
    
    /**
     * load the vat codes from the database and populate the gui
     * @throws Exception 
     * @throws ParseException 
     * @throws IllegalArgumentException 
     *  
     */
    public void initializeVATCodes() throws Exception
    {

    	ArrayList<FormSelect> codes = new ArrayList<FormSelect>();
    	boolean hasSelectedValue = (this.VATCODE!=null && !"".equals(this.VATCODE));
    	boolean hasMatch = false;
     	FormSelect code = null;
     	FormSelect defaultCode = null;
     	vatCodesSelectList = new ArrayList<FormSelect>();
     	ResultSetCache cachedResults = null;
    	
        try
        {
  
            Hashtable<String, com.ibm.gil.util.RegionalBigDecimal> VATPercentages = new Hashtable<String, com.ibm.gil.util.RegionalBigDecimal>();
            cachedResults = getInvoiceDataModel().selectVATCodesStatement();
            while (cachedResults.next())
            {
            	code = new TaxCodeFormSelect();
            	String value = cachedResults.getString(2);
            	String label = cachedResults.getString(4);
            	
            	code.setValue(value);
            	code.setLabel(label);
            	
                if (cachedResults.getString(3).equals("*"))	{
                	
                	defaultCode = new TaxCodeFormSelect();
                	defaultCode.setValue(value);
                	defaultCode.setLabel(label);
                	defaultCode.setSelected(true);
                }
                
            	if (hasSelectedValue && code.getValue().equalsIgnoreCase(this.VATCODE)){
            		
            		code.setSelected(true);
            		hasMatch = true;
            	}  

            	codes.add(code);
        	}
            
        	this.vatCodesSelectList = codes;
            
            if(!hasMatch) {

	            Iterator<FormSelect> it = codes.iterator();
	            while(it.hasNext()) {
	            	
	            	TaxCodeFormSelect vatcode = (TaxCodeFormSelect)it.next();
	            	if ( defaultCode!=null &&  vatcode.getValue().equalsIgnoreCase(defaultCode.getValue()) ) {
	            		vatcode.setSelected(true);
	            	}
	            }
    }
            
            
        } catch (Exception exc) {
            logger.fatal(exc.toString());
            throw exc;
        }finally{
        	cachedResults = null;
        }

    }
    
    
    
    public void initializeVATPercentages() throws Exception  {
        
    	ResultSetCache cachedResults  = null;
    	try
        {
  
            Hashtable<String, com.ibm.gil.util.RegionalBigDecimal> VATPercentages = new Hashtable<String, com.ibm.gil.util.RegionalBigDecimal>();
            Hashtable<String, String> VATPercentagesStr = new Hashtable<String, String>();
            cachedResults = getInvoiceDataModel().selectVATCodesStatement();
            while (cachedResults.next())
            {
            	VATPercentages.put(cachedResults.getString(2), cachedResults.getRegionalBigDecimal(5));
            	VATPercentagesStr.put(cachedResults.getString(2), cachedResults.getRegionalBigDecimal(5).toString());
        	}
            
            this.setVATPercentages(VATPercentages);
            this.setVATPercentagesStr(VATPercentagesStr);
            
        } catch (Exception exc) {
            logger.fatal(exc.toString());
            throw exc;
        }finally{
        	cachedResults = null;
        }
    }
    
    private void initializeVATVariance() throws Exception  {
      
      ResultSet varianceResults = null;
      try
        {
            // load the vat variance
            varianceResults = getInvoiceDataModel().getVATVarianceStatement();
            if (varianceResults.next())
            {
                RegionalBigDecimal variance = new RegionalBigDecimal(varianceResults.getString(1), RegionalBigDecimal.PERIODDECIMALSEPARATOR);
                this.setVATVariance(variance);
            }
        } catch (Exception exc)  {
            logger.fatal(exc.toString());
            throw exc;
        }finally{
        	varianceResults = null; 
        }

    }
    
    private void initializeDbCr() throws Exception
    {
    	String[] defaultValues = { "DB", "CR" };
    	FormSelect code = null;
    	ArrayList<FormSelect> codes = null;
    	boolean hasSelectedValue = (this.DBCR!=null && !"".equals(this.DBCR));
    	
     try
     {

         codes = new ArrayList<FormSelect>();
  
           for (int i = 0; i< defaultValues.length; i++)
            {
            	code = new FormSelect();
            	code.setValue(defaultValues[i]);
            	code.setLabel(defaultValues[i]);
            	
            	if(hasSelectedValue && defaultValues[i].equalsIgnoreCase(this.DBCR)) {
            		
            		code.setSelected(true);
            		
            	} else  if (!hasSelectedValue && i==0) {
            		
            				code.setSelected(true);
            	}
                codes.add(code);
            }

        } catch (Exception exc) {
            logger.fatal(exc.toString());
            throw exc;
        }
        this.dbCrSelectList = codes;
    }
    
    
    
    private void initializeSilentCoa() throws Exception {
    	
    	String[] defaultValues = { "Y", "N" };
    	FormSelect code = null;
    	ArrayList<FormSelect> codes = null;
    	boolean hasSelectedValue = (this.SILENTCOA!=null && !"".equals(this.SILENTCOA));
    	
     try
     {

         codes = new ArrayList<FormSelect>();
  
           for (int i = 0; i< defaultValues.length; i++) {
            	code = new FormSelect();
            	code.setValue(defaultValues[i]);
            	code.setLabel(defaultValues[i]);
            	
            	if(hasSelectedValue && defaultValues[i].equalsIgnoreCase(this.SILENTCOA)) {
            		code.setSelected(true);
            	} else if (!hasSelectedValue && i==0) {
            			   code.setSelected(true);
            	}
                codes.add(code);
            }

        } catch (Exception exc) {
            logger.fatal(exc.toString());
            throw exc;
        }
        this.silentCoaSelectList = codes;
    }
    
    
    
    private void initializeDistributor() throws Exception
    {
    	
    	FormSelect code = null;
    	ArrayList<FormSelect> codes = new ArrayList<FormSelect>();
    	boolean hasSelectedValue = (this.DISTRIBUTORNUMBER!=null && !"".equals(this.DISTRIBUTORNUMBER.trim()));
    	ResultSet cachedResults  = null;
        try
        {
        	FormSelect	codeBlank = new FormSelect();
        	codeBlank.setValue(" ");
        	codeBlank.setLabel("&nbsp");
            cachedResults = getInvoiceDataModel().selectDistributorInfoStatement();
            codes.add(codeBlank);
            while (cachedResults.next())
            {
            	code = new DistributorFormSelect();
            	code.setValue(cachedResults.getString(2).trim());
            	code.setLabel(cachedResults.getString(3).trim());
            	
            	if(hasSelectedValue && code.getValue().equalsIgnoreCase(this.DISTRIBUTORNUMBER)){
            		
            		code.setSelected(true);
            	} else {
            		codeBlank.setSelected(true);
            	}
            	codes.add(code);
            }
            
           
            distributorCodeSelectList = codes;

        } catch (Exception exc) {
           logger.fatal(exc.toString());
           throw exc;
        }finally{
        	cachedResults=null;
        }
    }
    
    /**
     * recalculates the total invoice amount
     */
    public void recalculateNetAmount()
    {
        RegionalBigDecimal total = RegionalBigDecimal.ZERO.setScale(2);
        RegionalBigDecimal tax = RegionalBigDecimal.ZERO.setScale(2);
        try
        {
            total = new RegionalBigDecimal(getTOTALAMOUNT()).setScale(2);
        } catch (NumberFormatException exc)
        {
        }
        try
        {
            tax = new RegionalBigDecimal(getVATAMOUNT()).setScale(2);
        } catch (NumberFormatException exc)
        {
        }
        RegionalBigDecimal net = total.subtract(tax);

        RegionalBigDecimal percentage = getVATPercentage();
        RegionalBigDecimal targetVat = RegionalBigDecimal.ZERO.setScale(2);
        try
        {
            if (percentage.compareTo(RegionalBigDecimal.ZERO) != 0)
            {
                RegionalBigDecimal hundred = new RegionalBigDecimal("100").setScale(2);

                targetVat = total.multiply(percentage);
                targetVat = targetVat.setScale(4, RegionalBigDecimal.ROUND_HALF_UP);
                targetVat = targetVat.divide(percentage.add(hundred), RegionalBigDecimal.ROUND_HALF_UP);
                targetVat = targetVat.setScale(2, RegionalBigDecimal.ROUND_HALF_UP);
            }
        } catch (NumberFormatException exc)
        {
            logger.error(exc.toString());
        }

        setVATTARGETAMOUNT(targetVat.toString());
        setTOTALAMOUNT(total.toString());
        setVATAMOUNT(tax.toString());
        setNETAMOUNT(net.toString());
    }

    /**
     * load the associated document data from the database if it exists
     */
    public boolean initializeDatabaseValues() {

          // initialize the index values from the db
        try
        {
        	logger.info("Initializing Database Values");
        	
        	this.setFromGCMS(false);
        	
            validationInvVarAmountStr = "0.00";
            validationCreditVarAmountStr = "0.00";
            validationDebitVarAmountStr = "0.00";
            validationNetAmountStr = "0.00";
            validationNetBalanceStr= "0.00";
            validationVarianced = false;
            validationPaid = false;
            vendorSearchRequired = false;

            // load the invoice if it exists
            ResultSet results = getInvoiceDataModel().selectByObjectIdStatement();
            if (!results.next()) 
            {
            	
            	logger.info("Invoice " + this.getINVOICENUMBER() +" "+ this.getINVOICEDATE()+" "+ this.getCOUNTRY()+" not found in GAPTS");
                this.setDOCUMENTMODE(Indexing.ADDMODE);
                this.setDIRTYFLAG(Boolean.TRUE);
                this.setOLDCOUNTRY(this.getCOUNTRY());
                this.setOLDINVOICENUMBER(this.getINVOICENUMBER());
                this.setOLDINVOICEDATE(this.getINVOICEDATE());
                // launch a search window on adds
                vendorSearchRequired = true;
            } else
            {
            	logger.info("Loading Invoice " + this.getINVOICENUMBER()+" "+this.getINVOICEDATE()+" "+ this.getCOUNTRY()+ " from GAPTS");
            	this.setDOCUMENTMODE(Indexing.UPDATEMODE);
            	this.setDIRTYFLAG(Boolean.FALSE);

                // index into the result set fields
                int i = 1;

                // store the new invoice number
                String newInvoiceNumber = this.getINVOICENUMBER();
                String oldInvoiceNumber = DB2.getString(results, i++);
                this.setOLDINVOICENUMBER(oldInvoiceNumber);
                String newInvoiceDate = this.getINVOICEDATE();
                String oldInvoiceDate = DB2.getString(results, i++);
                oldInvoiceDate = RegionalDateConverter.convertDate("DB2", "GUI", oldInvoiceDate);
                this.setOLDINVOICEDATE(oldInvoiceDate);

                //check for amount changes
                RegionalBigDecimal taxAmount = new RegionalBigDecimal(DB2.getString(results, i++), RegionalBigDecimal.PERIODDECIMALSEPARATOR);
                RegionalBigDecimal taxBalance = new RegionalBigDecimal(DB2.getString(results, i++), RegionalBigDecimal.PERIODDECIMALSEPARATOR);
                RegionalBigDecimal netAmount = new RegionalBigDecimal(DB2.getString(results, i++), RegionalBigDecimal.PERIODDECIMALSEPARATOR);
                RegionalBigDecimal netBalance = new RegionalBigDecimal(DB2.getString(results, i++), RegionalBigDecimal.PERIODDECIMALSEPARATOR);
                RegionalBigDecimal totalAmount = new RegionalBigDecimal(DB2.getString(results, i++), RegionalBigDecimal.PERIODDECIMALSEPARATOR);

                // load non index fields from the database
                this.setTOTALAMOUNT(totalAmount.toString());
                this.setVATAMOUNT(taxAmount.toString());
                this.setINVBALAMT(netBalance.toString());
                String vendorName = (DB2.getString(results, i++));
                this.setOLDVENDORNAME(vendorName);
                this.setDBCR(DB2.getString(results, i++));
                String accountNumber = (DB2.getString(results, i++));
                String customerName = (DB2.getString(results, i++));
                this.setCURRENCY(DB2.getString(results, i++));
                this.setINVOICETYPE(DB2.getString(results, i++));
                
                String venderNumber=DB2.getString(results, i++);
                this.setVENDORNUMBER(venderNumber);
                this.setOLDVENDORNUMBER(venderNumber);
                this.setOLDACCOUNTNUMBER(accountNumber);
                //ADDED FOR DEFECT NO 1774555
                this.setOLDCUSTOMERNAME(customerName);
                
                this.setOCR(DB2.getString(results, i++));
                this.setPOEXCODE(DB2.getString(results, i++));
                i++;
                this.setVATCODE(DB2.getString(results, i++));
                i++;
                this.setOFFERINGLETTER(DB2.getString(results, i++));
                this.setCONTRACTNUMBER(DB2.getString(results, i++));
                this.setLASTSAVEDCONTRACTNUMBER(this.getCONTRACTNUMBER());
                this.setCOMPANYCODE(DB2.getString(results, i++));
                this.setOLDCOMPANYCODE(this.getCOMPANYCODE());
                String taxSupplyDate = DB2.getString(results, i++);                
                if(this.getCOUNTRY().equals("CZ")|| this.getCOUNTRY().equals("HU") || this.getCOUNTRY().equals("PL")){
                	taxSupplyDate = RegionalDateConverter.convertDate("DB2", "GUI", taxSupplyDate);
                	this.setTAXSUPPLYDATE(taxSupplyDate);
                }                
                String oldCountry = DB2.getString(results, i++);
                this.setOLDCOUNTRY(oldCountry); 
                this.setTAXINVOICENUMBER(DB2.getString(results, i++));
                this.setDISTRIBUTORNUMBER(DB2.getString(results, i++));
                //story 1708793 
                this.setINVOICESUFFIX(DB2.getString(results, i++));
                //story end 1708793 
                
                //Story 1692411 AR - 2 new fields in GIL to be sent to GAPTS/datasets
                if(this.getCOUNTRY().equals("AR")){
                	this.setCAICAE(DB2.getString(results, i++));
                	String CAIDATE = DB2.getString(results, i++);
                    CAIDATE = RegionalDateConverter.convertDate("DB2", "GUI", CAIDATE);
                    this.setCAICAEDate(CAIDATE);
                }
                //End Story 1692411 
                
                
                // check for changed fields from the index
                if (!accountNumber.trim().equals(this.getACCOUNTNUMBER().trim()))
                {
                	this.setDIRTYFLAG(Boolean.TRUE);
                }
                if (!customerName.trim().equals(this.getCUSTOMERNAME().trim()))
                {
                	this.setDIRTYFLAG(Boolean.TRUE);
                }


                RegionalBigDecimal invVarAmount = RegionalBigDecimal.ZERO;
                RegionalBigDecimal creditVarAmount = RegionalBigDecimal.ZERO;
                RegionalBigDecimal debitVarAmount = RegionalBigDecimal.ZERO;
                
                ResultSet rs = getInvoiceDataModel().retrieveInvoiceVarianceAmt();
                while(rs.next()){
                	String invoiceType = rs.getString(1);
                	if(invoiceType.equals("CR")){
                		creditVarAmount = creditVarAmount.add(new RegionalBigDecimal(rs.getString(2), RegionalBigDecimal.PERIODDECIMALSEPARATOR));
                	}
                	if(invoiceType.equals("DB")){
                		debitVarAmount = debitVarAmount.add(new RegionalBigDecimal(rs.getString(2), RegionalBigDecimal.PERIODDECIMALSEPARATOR));
                	}
        	
                }
                invVarAmount = creditVarAmount.subtract(debitVarAmount);
                this.setINVVARAMT(invVarAmount.toString());
                


                // load the contract invoice detail count
                int count = 0;
                results = getInvoiceDataModel().selectDetailsCountStatement();
                if (results.next())
                {
                    count = results.getInt(1);
                }
                this.setDetailCount(count);
                this.overrideContractNumber();
                this.recalculateNetAmount();
                

                
                //1627923	[GST] Coding Total Tax Amount on APTS Index Invoices Screen (GIL) - MANG 01-27-17
                if(this.getCOUNTRY().equals("IN") && (this.getEFEDATE().equals("Y"))){ 
                	results = getInvoiceDataModel().selectINGSTStatement();
                    if (results.next())
                    {
                    	int j = 1;
                    	String BillTo = DB2.getString(results, j++);                    	
                        this.setBILLTO(BillTo);
                        String BillFrom = DB2.getString(results, j++);
                        this.setBILLFROM(BillFrom);
                        RegionalBigDecimal CGST = new RegionalBigDecimal(DB2.getString(results, j++), RegionalBigDecimal.PERIODDECIMALSEPARATOR);
                        RegionalBigDecimal SGST = new RegionalBigDecimal(DB2.getString(results, j++), RegionalBigDecimal.PERIODDECIMALSEPARATOR);
                        RegionalBigDecimal IGST = new RegionalBigDecimal(DB2.getString(results, j++), RegionalBigDecimal.PERIODDECIMALSEPARATOR);
                        this.setINCGST(CGST.toString());
                        this.setINSGST(SGST.toString());                        
                        this.setINIGST(IGST.toString());
                        this.setSUPPGSTREGNUMBER(DB2.getString(results, j++));
                        String LOAN = DB2.getString(results, j++);                        
                        this.setLOAN(LOAN);
                        this.setSHIPTO(DB2.getString(results, j++));

                        if ((LOAN.equals("N")) && (!BillTo.equals(BillFrom))){
                        	indiaGSTFieldsVisible = false;         	
                        }else if ((!LOAN.equals("N")) && (BillTo.equals(BillFrom))){
                        	indiaGSTFieldsEditable = false;                             
                        }else if ((!LOAN.equals("N")) && (!BillTo.equals(BillFrom))){
                        	indiaGSTFieldsEditable= false; 
                        	indiaGSTFieldsVisible = false;                        	
                        }

                    }else{
                    	RegionalBigDecimal net = RegionalBigDecimal.ZERO.setScale(2);
                    	this.setNETAMOUNT(net.toString());
                    }
                }
                //End 1627923    

                this.setSILENTCOA("N");
                results = getInvoiceDataModel().selectSilentCOAStatement();
                if (results.next())
                {
                	this.setSILENTCOA(DB2.getString(results, 1));
                }

                
                if(netAmount.compareTo(netBalance) == 0){
                	this.setNETEQBAL("Y");
                }
                
               validationInvVarAmountStr = invVarAmount.toString();
               validationCreditVarAmountStr = creditVarAmount.toString();
               validationDebitVarAmountStr = debitVarAmount.toString();
               validationNetAmountStr = netAmount.toString();
               validationNetBalanceStr = netBalance.toString();
               
               logger.info("Invoice Values Loaded");
            }


        } catch (Exception exc){
            logger.fatal(exc.toString(),exc);
            return false;
        }
        this.isWorkAccess(this.getUSERID());
        return true;
    }
 
    
    public void updateIndexValues() throws Exception	 {
    	getInvoiceDataModel().updateIndexValues();
    }
     
    private boolean isContractNumberRecordExists = false;
    
    
    public boolean isContractNumberRecordExists() {
		return isContractNumberRecordExists;
	}

	public void setContractNumberRecordExists(boolean isContractNumberRecordExists) {
		this.isContractNumberRecordExists = isContractNumberRecordExists;
	}

	public boolean contractNumberRecordExists() {
    	return isContractNumberRecordExists;
    }
    
    public void saveDocument() throws Exception {
        
     try {
        	getInvoiceDataModel().saveDocument();
        } catch (Exception sqx)  {
        	logger.fatal(sqx);
        	throw sqx;
        }
    }
    
    public void addContract() throws Exception {
        
      try {
        	getInvoiceDataModel().saveDocument();
        } catch (Exception sqx) {
        	logger.fatal(sqx);
        	throw sqx;
        }
    }

    public void rollbackIndexing() throws Exception {
    	
        try {
        	getInvoiceDataModel().rollbackIndexing();
        } catch (Exception sqx) {
        	logger.fatal(sqx);
        	throw sqx;
        }
    }
    

    public void rollbackCountryIndexing() throws Exception  {
        
    	try {
        	getInvoiceDataModel().rollbackCountryIndexing();
        } catch (Exception sqx) {
        	logger.fatal(sqx);
        	throw sqx;
        }
    }
    
    //1627921 [GST] Coding Net Amount on APTS Index Invoices Screen (GIL) - MANG 01-27-17
    
    public void recalculateINNetAmount() {
    	
        RegionalBigDecimal total = RegionalBigDecimal.ZERO.setScale(2);
        RegionalBigDecimal CGST = RegionalBigDecimal.ZERO.setScale(2);
        RegionalBigDecimal SGST = RegionalBigDecimal.ZERO.setScale(2);
        RegionalBigDecimal IGST = RegionalBigDecimal.ZERO.setScale(2);
        RegionalBigDecimal net = RegionalBigDecimal.ZERO.setScale(2);
        RegionalBigDecimal tax = RegionalBigDecimal.ZERO.setScale(2);
        RegionalBigDecimal Default = RegionalBigDecimal.ZERO.setScale(2);
                
        try
        {
            total = new RegionalBigDecimal(getTOTALAMOUNT()).setScale(2);
        } catch (NumberFormatException exc)
        {
        }
        try
        {
        	CGST = new RegionalBigDecimal(getINCGST()).setScale(2);
        } catch (NumberFormatException exc)
        {
        }
        try
        {
        	SGST = new RegionalBigDecimal(getINSGST()).setScale(2);
        } catch (NumberFormatException exc)
        {
        }
        try
        {
        	IGST = new RegionalBigDecimal(getINIGST()).setScale(2);
        } catch (NumberFormatException exc)
        {
        }
        
        try
        {     
        	if(getBILLTO().equals(getBILLFROM())){
        		net = total.subtract(CGST);
        		net = net.subtract(SGST);
        		tax = CGST.add(SGST);  
        		setINIGST(Default.toString());
        	}else{
        		net = total.subtract(IGST);
        		tax = IGST;
        		setINCGST(Default.toString());
        		setINSGST(Default.toString());
        }
        } catch (NumberFormatException exc)
        {
        }
        	setTOTALAMOUNT(total.toString());
        	setVATAMOUNT(tax.toString());
        	setNETAMOUNT(net.toString());
    }
    
    //End 1627921
    
    
    
    public DOCUMENT queryDocument() throws Exception  {
    	
    	ContentManagerImplementation cm  = new ContentManagerImplementation(this.getDatastore());
    	
    	if (getOBJECTID().trim().length() == 0)
    	{
    		return null;
    	}
        // query the item
        logger.debug("Searching for document in Content Manager");
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
    
    
    

    public boolean loadIndexValues() throws RemoteException {
    	
    	ContentManagerImplementation cm  = new ContentManagerImplementation(this.getDatastore());
        try
        {
        	logger.info("Loading Indexing Values Started");

            String[] values = null;
            String[] fields = null;
            boolean validData = false;
            boolean passedValues = true;

                try
                {
                    fields = cm.getIndexFields(cm.indexDescriptionToName(getINDEXCLASS(), this.getDatastore()));
                    values = getValues();


                    if (values == null)
                    {
                        values = cm.getIndexValues(getOBJECTID(),this.getDatastore());
                        passedValues = false;
                    }

                    if ((values != null) && (fields != null) && (values.length > 0) && (fields.length > 0) && (values[0].trim().length() > 0))
                    {
                        validData = true;
                        
                    }
                    
                    logger.info("Fields:" + Arrays.toString(fields));
                    logger.info("Values:" + Arrays.toString(values));
                    
                } catch (Exception exc)
                {
                    logger.error(exc,exc);
                }

            if (validData)
            {
                setINVOICENUMBER(getFieldMatch("Invoice Number", fields, values));
                String fromDate = getFieldMatch("Invoice Date", fields, values);

                if (passedValues)
                {
                    try
                    {
                        fromDate = RegionalDateConverter.convertDate("CM", "GUI", fromDate);

                    } catch (Exception exc)
                    {
                        fromDate = RegionalDateConverter.convertDate("GUI", "GUI", fromDate);

                    }
                } else
                {
                    fromDate = RegionalDateConverter.convertDate("CM", "GUI", fromDate);

                }

                setINVOICEDATE(fromDate);
                setCUSTOMERNAME(getFieldMatch("Customer Name", fields, values));
                setCUSTOMERNUMBER(getFieldMatch("Customer Number", fields, values));
                setTEAM(getFieldMatch("Team", fields, values));
                setVENDORNAME(getFieldMatch("Vendor Name", fields, values));
                setACCOUNTNUMBER(getFieldMatch("Account Number", fields, values));
                setOFFERINGLETTER(getFieldMatch("Offering Letter Number", fields, values));
                setCONTRACTNUMBER(getFieldMatch("Contract Number", fields, values));
                setSOURCE(getFieldMatch("Source", fields, values));

                String timestamp = getFieldMatch("Timestamp", fields, values);
                try
                {
                    timestamp = RegionalDateConverter.convertTimeStamp("CM", "GUI", timestamp);
                    setTIMESTAMP(timestamp);
                } catch (Exception exc)
                {
                   logger.info("Timestamp field is blank");
                }
                
                setCREATEDATE(getInvoiceDataModel().retrieveCreatedDate());
                
            } else {
            	return false;
            }

            logger.info("Loading Indexing Values Finished");
            
        } catch (Exception exc) {
            logger.error(exc,exc);
            return false;
        }

        return true;
    } 
    
    public void validateInput() throws Exception {
 
        ResultSet resultsDupicateInvoice;
        getDialogErrorMsgs().clear();
        
        logger.info("Validating Data Started");
        
        try
        {
        	resultsDupicateInvoice = getInvoiceDataModel().selectByInvoiceStatement();
            if (resultsDupicateInvoice.next())
            {
                if (!DB2.getString(resultsDupicateInvoice, 3).equals(this.getOBJECTID()))
                {
                	validationIsDuplicatedInvoice = true;
                }
            }
        } catch (Exception exc)
        {
            logger.error(exc.toString());
            throw exc;
        }
        
    	
        if (!(this.getCOUNTRY().equals("FR") && (this.isCommissionInvoice())))
        {
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
            			selectOfferingLetter();
            		}
            		//End Story 1768398
            	} catch (Exception exc)
            	{
            		validationIsOfferLetterValidHybr = false;
            		throw exc;		
            	}
            	if (getOFFERINGLETTERID()==null || getOFFERINGLETTERID().trim().length() == 0)
            	{
            		validationIsOfferLetterInCM = false;
            	}
        }
        }
        
        
        try
        {
            ResultSet results = getInvoiceDataModel().selectCheckBySupplierStatement();
            if (results.next())  {
            	validationIsInvoiceNumberChanging = true;
            } else {
            	validationIsInvoiceNumberChanging = false;
            }
        } catch (Exception e) {
            logger.error("Error validating Invoice Number/Supplier Number:" + e.getLocalizedMessage(),e);
            throw e;
        } 
        
        // validate the document got indexed correctly
        boolean exists = false;
        try
        {
            DOCUMENT cmdoc = this.queryDocument();
            if (cmdoc != null)
            {
            	logger.debug("document exists:"+cmdoc.toString());
                exists = true;
            }
        } catch (Exception exc)
        {
            logger.error("Error searching for document:"+exc,exc);
        	exists = false;
        	throw exc;
        }
        
        validationIsDocumentIndexedProperly = exists;
        
		if (this.CUSTOMERNAME == null || this.CUSTOMERNAME.trim().length() == 0 ){
			
			logger.debug("Customer Name is a required field.");
			getDialogErrorMsgs().add("Customer Name is a required field.");
		}
        
		if ((this.VENDORNAME == null || this.VENDORNAME.trim().length() == 0)  &&  (this.ACCOUNTNUMBER == null || this.ACCOUNTNUMBER.trim().length() == 0) ){
			
			logger.debug("Vendor Name or Account Number are required fields.");
			getDialogErrorMsgs().add("Vendor Name or Account Number are required fields.");
		}
		
		
		 java.util.Date invoiceDate = RegionalDateConverter.parseDate("GUI", this.INVOICEDATE);
		 java.util.Date scanDate = getCreatedTimeStampAsDate();
		
		
        // validation
		 java.util.Date now = new Date(System.currentTimeMillis());
        
        if (invoiceDate.getTime() > now.getTime())
        {
        	logger.debug("Invoice Date must not be a future date");
        	getDialogErrorMsgs().add("Invoice Date must not be a future date");  
        }

        long MS_1_AFTER = 86400000;
        if (invoiceDate.getTime() < (now.getTime() - (300 * MS_1_AFTER)))
        {
        	logger.debug("Invoice Date must not be greater than 300 days old");
        	promptDatePopup = true;
        } else {
        	promptDatePopup = false;
        }
        
        if(this.getCOUNTRY().equals("CZ")|| this.getCOUNTRY().equals("HU") || this.getCOUNTRY().equals("PL"))
        {
        	
        	boolean countryPL = this.getCOUNTRY().equals("PL");
        	if(this.getTAXSUPPLYDATE()==null || this.getTAXSUPPLYDATE().trim().length()==0){
        		if(!countryPL){
        			getDialogErrorMsgs().add("Tax Supply Date is a required field");
     			
        		}
        	} else { 
        		java.util.Date taxSupplyDate = RegionalDateConverter.parseDate("GUI", this.TAXSUPPLYDATE);
        		if	(taxSupplyDate.getTime() < (now.getTime() - (300 * MS_1_AFTER))){
	                	logger.debug("Invoice Date must not be greater than 300 days old");
	                	promptSupplyDatePopup = true;
                } else {
                	promptSupplyDatePopup = false;
                }
        	}
        } 
        

        if (this.getCOUNTRY().equals("CZ") || this.getCOUNTRY().equals("HU") || this.getCOUNTRY().equals("PL") ){
        	
        	
        }
       
		
		if (scanDate.getTime() < invoiceDate.getTime()) {
			logger.debug("Scan Date cannot be before the Invoice Date");
			getDialogErrorMsgs().add("Scan Date cannot be before the Invoice Date");
		}
		
		RegionalBigDecimal totalamount = RegionalBigDecimal.ZERO;
        try
        {
            totalamount = getInvoiceDataModel().getDecimal(this.getTOTALAMOUNT());
            if (totalamount.compareTo(RegionalBigDecimal.ZERO) <= 0)
            {
            	logger.debug("Invoice Amount must be > 0.00");
            	getDialogErrorMsgs().add("Invoice Amount must be > 0.00");
            }
        } catch (Exception exc)
        {
        	logger.debug("Invoice Amount is invalid");
        	getDialogErrorMsgs().add("Invoice Amount is invalid");
        }
        RegionalBigDecimal vatamount = RegionalBigDecimal.ZERO;
        try
    	{
    		vatamount = getInvoiceDataModel().getDecimal(this.getVATAMOUNT());
    		if (vatamount.compareTo(RegionalBigDecimal.ZERO) < 0)
    		{
    			logger.debug("Tax Amount must be >= 0.00");
    			getDialogErrorMsgs().add("Tax Amount must be >= 0.00");
    		}
    	} catch (Exception exc)
    	{
    		logger.debug("Tax Amount is invalid");
    		getDialogErrorMsgs().add("Tax Amount is invalid");
    	}
        
        RegionalBigDecimal netvalue = RegionalBigDecimal.ZERO;
        try
        {
            netvalue = getInvoiceDataModel().getDecimal(this.getNETAMOUNT());
            if (netvalue.compareTo(RegionalBigDecimal.ZERO) <= 0)
            {
               logger.debug("Net Amount must be > 0.00");
            	getDialogErrorMsgs().add("Net Amount must be > 0.00");
            }
        } catch (Exception exc)
        {
        	logger.debug("Net Amount is invalid");
        	getDialogErrorMsgs().add("Net Amount must be > 0.00");
        }
        
        
        if ((this.getOCRREQUIRED()!=null && this.getOCRREQUIRED().equals("R")) && (this.getOCR()!=null && this.getOCR().trim().length() == 0))
        {
        	getDialogErrorMsgs().add("OCR/KID is a required field");
        }
        
    	
    	if((this.getINVOICETYPE()!=null && this.getINVOICETYPE().toString().startsWith("RBD")) && (this.getDBCR()!=null && !"CR".equalsIgnoreCase(this.getDBCR().toString()))){
    		getDialogErrorMsgs().add("RBD invoice type must be CR");
    	}

    	
        if(this.getCOUNTRY().equals("BR") && this.isCompanyCodeOf0001() && !this.isIBMInvoice()){
        	getDialogErrorMsgs().add("Please select Invoice Type of IBM for the Comp Code of 0001");
        }
        

        
        if(this.getCOUNTRY().equals("NO")&& (this.getOCR()!=null && this.getOCR().trim().length()>0)){
        	String text = this.getOCR().trim();
        	String numbers = "0123456789";
        	for(int i=0;i<text.length();i++){
        		if(numbers.indexOf(text.charAt(i))<0){
        			getDialogErrorMsgs().add("OCR/KID field must contain numbers only");
	
        		}
        	}
        }  
        
        //Story 1692411 AR - 2 new fields in GIL to be sent to GAPTS/datasets
        if(this.getCOUNTRY().equals("AR")){
        	if (this.getCAICAE().trim().length() == 0) {
        		getDialogErrorMsgs().add("CAI/CAE is a required field");
            }
        	 boolean validCaiCaeDate = true;
        	 java.util.Date caiCaeDate = null;
        	try {
        		caiCaeDate = RegionalDateConverter.parseDate("GUI", this.getCAICAEDate());
        	}catch(ParseException e){
        		validCaiCaeDate = false;
        	}
        	if(!validCaiCaeDate || caiCaeDate==null  )  {
        		getDialogErrorMsgs().add("CAI/CAE due Date is a required field");
            }
        }       
        //End Story 1692411
        
        
        
        if(this.getCOUNTRY().equals("IN") && this.getEFEDATE().equals("Y")){
        	
        	if (this.BILLFROM == null || this.BILLFROM.trim().length() == 0 ){
        		getDialogErrorMsgs().add("Bill From is a required field");
        	}
        	if (this.BILLTO == null || this.BILLTO.trim().length() == 0 ){
        		getDialogErrorMsgs().add("Bill To is a required field");
        	}
        	
        	if (this.SUPPGSTREGNUMBER == null || this.SUPPGSTREGNUMBER.trim().length() == 0 ){
        		getDialogErrorMsgs().add("Supplier GST Registration Number is a required field");
        	}
        }
        
        logger.info("Validating Data Finished");
    }
    
    public void defaultToLastInsertedRecord() throws Exception {
    	
    	logger.info("Retrieving Last Inserted Record Started");
        try
        {
        	int i = 0;
            // load the last inserted invoice if it exists
            ResultSet results = getInvoiceDataModel().selectLastInvoiceEnteredStatement();
            if (results.next())
            {

                    this.setVENDORNAME( DB2.getString(results, 1));
                    this.setTOTALAMOUNT( DB2.getString(results, 2));
                    this.setDBCR( DB2.getString(results, 3));
                    this.setACCOUNTNUMBER( DB2.getString(results, 4));
                    this.setCUSTOMERNAME( DB2.getString(results, 5));
                    this.setCOUNTRY( DB2.getString(results, 6));
                    this.setCURRENCY( DB2.getString(results, 7));
                    this.setINVOICETYPE( DB2.getString(results, 8));
                    this.setVENDORNUMBER( DB2.getString(results, 9));
                
                // fix up the amount to the regional format
                RegionalBigDecimal amount = new RegionalBigDecimal (this.getTOTALAMOUNT(), RegionalBigDecimal.PERIODDECIMALSEPARATOR);
                this.setTOTALAMOUNT(amount.toString());
                
            }
            
            logger.info("Retrieving Last Inserted Record Finished");
            
        } catch (SQLException exc) {
            logger.fatal(exc.toString());
            throw exc;
        }catch (Exception exc) {
            logger.fatal(exc.toString());
            throw exc;
        }
    }
    
   public boolean initializeIndexing() throws IllegalArgumentException, ParseException, Exception {

	   	   loadIndexValues();
	   	   
	   	   initializeVATPercentages();
	   	   initializeVATVariance();
	   
	   	   EffectiveINDate();
	 	   initializeDatabaseValues();
	   
	 	   initializeCurrencies();
	 	   initializeInvoiceTypes();
		   initializeInvoiceSuffixes();
		   
		   initializeVATCodes();
		   initializePOEXCodes();
		   initializeCountryInfo();
		   initializeCompanyCode();
		   initializeDbCr();
		   initializeDistributor();
		   initializeSilentCoa();
		   
	       //1664571 [GST] GIL should show different input invoice screens based on the effective date of the invoice
	       //End 1664571 
		   //1627912 [GST] New Fields on APTS Index Invoices Screen (GIL) - MANG 01-27-17
		   initializeBillFields();
		   initializeLoanField();

		   return true;
   }
   
   
/**
 * 
 * @param selectedInvoiceList - List of invoices that are shown in the contract screen data grid and should not be shown on the list 
 * of invoices to be selected from GAPTS
 * @return
 * @throws Exception
 */
  public ArrayList<ContractInvoice> searchByInvoice(ArrayList<ContractInvoice> selectedInvoiceList) throws Exception   {
	   	
	   ResultSet results = null;
	   results = getInvoiceDataModel().selectInvoiceStatement();
	   loadInvoiceSearchResults(results,selectedInvoiceList);
	   return invoiceSearchList;
   }

   private void loadInvoiceSearchResults(ResultSet results,ArrayList<ContractInvoice> selectedInvoiceList) throws Exception  {
       try
       {
    	   ContractInvoice contractInvoice = null;
           invoiceSearchList = new ArrayList<ContractInvoice>();
           
           while (results.next())
           {
        	   int i = 1;
        	   contractInvoice = new ContractInvoice();
        	   contractInvoice.setINVOICENUMBER(DB2.getString(results, i++));
        	   contractInvoice.setINVOICEDATE(DB2.getString(results, i++));
        	   contractInvoice.setCUSTOMERNAME(DB2.getString(results, i++));
               String date = RegionalDateConverter.convertDate("DB2", "GUI",contractInvoice.getINVOICEDATE());
               contractInvoice.setINVOICEDATE(date);
               if(!selectedInvoiceList.contains(contractInvoice)) {
                   invoiceSearchList.add(contractInvoice);
               }  
           }
       } catch (Exception exc){
           logger.fatal(exc.toString());
           throw exc;
      }
   }

   
   //Story 1768398 GIL - additional change for GCMS JR
   public boolean selectOfferingLetterNum() throws Exception {
	   
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
   
   public boolean selectCountersignOfferingLetter() throws Exception {
	   
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
   
   public boolean getHybridCountry() throws Exception  {
	   
       try
       {
           String Hcountry;
           ResultSetCache cachedResults = getInvoiceDataModel().selectFieldhybridStatement();
           while (cachedResults.next()){
           	Hcountry = cachedResults.getString(1);
           	if (Hcountry.equals(getCOUNTRY())){
           		return true; 
           	}                          
           }
       } catch (Exception exc)  {
           logger.fatal(exc.toString(),exc);
           throw exc;
       }
       return false;
   }   
  
}