package com.ibm.gil.business;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.Logger;

import com.ibm.gil.model.MiscInvoiceDataModel;
import com.ibm.gil.util.PropertiesManager;
import com.ibm.gil.util.RegionalBigDecimal;
import com.ibm.gil.util.RegionalDateConverter;
import com.ibm.igf.contentmanager.rmi.ContentManagerImplementation;
import com.ibm.igf.contentmanager.rmi.ContentManagerInterface;
import com.ibm.igf.hmvc.DB2;
import com.ibm.igf.hmvc.ResultSetCache;
import com.ibm.w3.financing.tools.gcms.query.results.DOCUMENT;



// Referenced classes of package com.ibm.gil.business:
//            Indexing, ComboItemDescription

public class MiscInvoice extends Indexing
{

	private String INVOICENUMBER;
	private String OLDINVOICENUMBER;
	private String INVOICEDATE;
	private String OLDINVOICEDATE;
	private String TOTALAMOUNT;
	private String VATAMOUNT;
	private String VATTARGETAMOUNT;
	private String NETAMOUNT;
	private String CURRENCY;
	private String DBCR;
	private String INVOICETYPE;
	private String PROVINCECODE;
	private String VATCODE;
	private String POEXCODE;
	private String VENDORNAME;
	private String VENDORNUMBER;
	private String SRNUMBER;
	private String BLOCKINDC;
	private String OLDVENDORNUMBER;
	private String OCR;
	private String FOLDERID;
	private String TEAM;
	private String CONTRACTID;
	private String LASTSAVEDCONTRACTNUMBER;
	private String OCRREQUIRED;
	private String VENDORCOMMISSION;
	private String COMPANYCODE;
	private String OLDCOMPANYCODE;
	private String TAXSUPPLYDATE;
	private String OLDCOUNTRY;
	private String TAXINVOICENUMBER;
	private String NETEQBAL;
	private String INVVARAMT;
	private String INVBALAMT;
	private String INVOICESUFFIX;
	private String OLDVATCODE;
	private String SEARCHTYPE ;
	private boolean isDuplicateInvoicenumber;    
	private boolean isHUFRounded;
	private ArrayList<FormSelect> currencyCodeSelectList;
	private PoexCodeFormSelect  defaultPoexCodeForinvoiceTypeCom;
	private ArrayList<FormSelect> poexCodesSelectList;
	private PoexCodeFormSelect  defaultPoexCode;
	private ArrayList<FormSelect> invoiceTypesSelectList;
	private ArrayList<FormSelect> companyCodeSelectList;
	private ArrayList<FormSelect> dbCrSelectList;    
	private ArrayList<FormSelect> vatCodesSelectList;
	private ArrayList<FormSelect> provinceCodesSelectList;
	private ArrayList<FormSelect> cacheCAIniCodesSelectList;
	private ArrayList<FormSelect> invoiceSuffixesSelectList ;
	private ArrayList<FormSelect> caVATCodesSelectList;

	private boolean promptDatePopup = false;
	private boolean promptSupplyDatePopup = false;

	boolean vendorSearchRequired;
	boolean validationPaid;
	boolean validationVarianced;
	boolean validationCountryCode;
	boolean validationSaveConvination;
	private boolean validationIsInvoiceNumberChanging = false;
	private ArrayList dialogErrorMsgs;
	private int detailCount;
	private String createdTimeStamp;
	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(MiscInvoice.class);
	private MiscInvoiceDataModel miscInvoiceDataModel;// = new MiscInvoiceDataModel(this);
	ContentManagerInterface CM;
	private RegionalBigDecimal VATVariance;
	boolean isSearchWindowActive;
	boolean isSupplierSelected;
	private static final RegionalBigDecimal NinetyNinePointNineNineNine = (new RegionalBigDecimal(99.998999999999995D)).setScale(3, 4);
	private String VATVarianceStr = "0.00";
	private ArrayList<String> dialogwarnMsgs ;
	public MiscInvoice()
	{

	}

	public MiscInvoice(String country)
	{
		vendorSearchRequired = false;
		dialogErrorMsgs = new ArrayList();
		dialogwarnMsgs = new ArrayList<String>();
		detailCount = 0;
		createdTimeStamp = null;
		miscInvoiceDataModel = null;
		CM = new ContentManagerImplementation(this.getDatastore());
		VATVariance = RegionalBigDecimal.ZERO;
		isSearchWindowActive = false;
		isSupplierSelected = false;
		setCOUNTRY(country);
		setTOTALAMOUNT(RegionalBigDecimal.ZERO.setScale(2).toString());
		setVATAMOUNT(RegionalBigDecimal.ZERO.setScale(2).toString());
		setNETAMOUNT(RegionalBigDecimal.ZERO.setScale(2).toString());
		setVATTARGETAMOUNT(RegionalBigDecimal.ZERO.setScale(2).toString());
		miscInvoiceDataModel = new MiscInvoiceDataModel(this);
	}

	public ArrayList<String> getDialogwarnMsgs() {
		return dialogwarnMsgs;
	}

	public void setDialogwarnMsgs(ArrayList<String> dialogwarnMsgs) {
		this.dialogwarnMsgs = dialogwarnMsgs;
	}

	public ArrayList getDialogErrorMsgs()
	{
		return dialogErrorMsgs;
	}

	public void setDialogErrorMsgs(ArrayList dialogErrorMsgs)
	{
		this.dialogErrorMsgs = dialogErrorMsgs;
	}

	public String getINVOICENUMBER()
	{
		return INVOICENUMBER;
	}

	public void setINVOICENUMBER(String iNVOICENUMBER)
	{
		INVOICENUMBER = iNVOICENUMBER;
	}

	public String getOLDINVOICENUMBER()
	{
		return OLDINVOICENUMBER;
	}

	public void setOLDINVOICENUMBER(String oLDINVOICENUMBER)
	{
		OLDINVOICENUMBER = oLDINVOICENUMBER;
	}

	public String getINVOICEDATE()
	{
		return INVOICEDATE;
	}

	public void setINVOICEDATE(String iNVOICEDATE)
	{
		INVOICEDATE = iNVOICEDATE;
	}

	public String getOLDINVOICEDATE()
	{
		return OLDINVOICEDATE;
	}

	public void setOLDINVOICEDATE(String oLDINVOICEDATE)
	{
		OLDINVOICEDATE = oLDINVOICEDATE;
	}

	public String getTOTALAMOUNT()
	{
		return TOTALAMOUNT;
	}

	public void setTOTALAMOUNT(String tOTALAMOUNT)
	{
		TOTALAMOUNT = tOTALAMOUNT;
	}

	public String getVATAMOUNT()
	{
		return VATAMOUNT;
	}

	public void setVATAMOUNT(String vATAMOUNT)
	{
		VATAMOUNT = vATAMOUNT;
	}

	public String getVATTARGETAMOUNT()
	{
		return VATTARGETAMOUNT;
	}

	public void setVATTARGETAMOUNT(String vATTARGETAMOUNT)
	{
		VATTARGETAMOUNT = vATTARGETAMOUNT;
	}

	public String getNETAMOUNT()
	{
		return NETAMOUNT;
	}

	public void setNETAMOUNT(String nETAMOUNT)
	{
		NETAMOUNT = nETAMOUNT;
	}

	public String getCURRENCY()
	{
		return CURRENCY;
	}

	public void setCURRENCY(String cURRENCY)
	{
		CURRENCY = cURRENCY;
	}

	public String getDBCR()
	{
		return DBCR;
	}

	public void setDBCR(String dBCR)
	{
		DBCR = dBCR;
	}

	public String getINVOICETYPE()
	{
		return INVOICETYPE;
	}

	public void setINVOICETYPE(String iNVOICETYPE)
	{
		INVOICETYPE = iNVOICETYPE;
	}

	public String getPROVINCECODE()
	{
		return PROVINCECODE;
	}

	public void setPROVINCECODE(String pROVINCECODE)
	{
		PROVINCECODE = pROVINCECODE;
	}

	public String getVATCODE()
	{
		return VATCODE;
	}

	public void setVATCODE(String vATCODE)
	{
		VATCODE = vATCODE;
	}

	public String getPOEXCODE()
	{
		return POEXCODE;
	}

	public void setPOEXCODE(String pOEXCODE)
	{
		POEXCODE = pOEXCODE;
	}

	public String getVENDORNAME()
	{
		return VENDORNAME;
	}

	public void setVENDORNAME(String vENDORNAME)
	{
		VENDORNAME = vENDORNAME;
	}

	public String getVENDORNUMBER()
	{
		return VENDORNUMBER;
	}

	public void setVENDORNUMBER(String vENDORNUMBER)
	{
		VENDORNUMBER = vENDORNUMBER;
	}

	public String getSRNUMBER()
	{
		return SRNUMBER;
	}

	public void setSRNUMBER(String sRNUMBER)
	{
		SRNUMBER = sRNUMBER;
	}

	public String getBLOCKINDC()
	{
		return BLOCKINDC;
	}

	public void setBLOCKINDC(String bLOCKINDC)
	{
		BLOCKINDC = bLOCKINDC;
	}

	public String getOLDVENDORNUMBER()
	{
		return OLDVENDORNUMBER;
	}

	public void setOLDVENDORNUMBER(String oLDVENDORNUMBER)
	{
		OLDVENDORNUMBER = oLDVENDORNUMBER;
	}

	public String getOCR()
	{
		return OCR;
	}

	public void setOCR(String oCR)
	{
		OCR = oCR;
	}

	public String getFOLDERID()
	{
		return FOLDERID;
	}

	public void setFOLDERID(String fOLDERID)
	{
		FOLDERID = fOLDERID;
	}

	public String getTEAM()
	{
		return TEAM;
	}

	public void setTEAM(String tEAM)
	{
		TEAM = tEAM;
	}

	public String getCONTRACTID()
	{
		return CONTRACTID;
	}

	public void setCONTRACTID(String cONTRACTID)
	{
		CONTRACTID = cONTRACTID;
	}

	public String getLASTSAVEDCONTRACTNUMBER()
	{
		return LASTSAVEDCONTRACTNUMBER;
	}

	public void setLASTSAVEDCONTRACTNUMBER(String lASTSAVEDCONTRACTNUMBER)
	{
		LASTSAVEDCONTRACTNUMBER = lASTSAVEDCONTRACTNUMBER;
	}

	public String getOCRREQUIRED()
	{
		return OCRREQUIRED;
	}

	public void setOCRREQUIRED(String oCRREQUIRED)
	{
		OCRREQUIRED = oCRREQUIRED;
	}

	public String getVENDORCOMMISSION()
	{
		return VENDORCOMMISSION;
	}

	public void setVENDORCOMMISSION(String vENDORCOMMISSION)
	{
		VENDORCOMMISSION = vENDORCOMMISSION;
	}

	public String getCOMPANYCODE()
	{
		return COMPANYCODE;
	}

	public void setCOMPANYCODE(String cOMPANYCODE)
	{
		COMPANYCODE = cOMPANYCODE;
	}

	public String getOLDCOMPANYCODE()
	{
		return OLDCOMPANYCODE;
	}

	public void setOLDCOMPANYCODE(String oLDCOMPANYCODE)
	{
		OLDCOMPANYCODE = oLDCOMPANYCODE;
	}

	public String getTAXSUPPLYDATE()
	{
		return TAXSUPPLYDATE;
	}

	public void setTAXSUPPLYDATE(String tAXSUPPLYDATE)
	{
		TAXSUPPLYDATE = tAXSUPPLYDATE;
	}

	public String getOLDCOUNTRY()
	{
		return OLDCOUNTRY;
	}

	public void setOLDCOUNTRY(String oLDCOUNTRY)
	{
		OLDCOUNTRY = oLDCOUNTRY;
	}

	public String getTAXINVOICENUMBER()
	{
		return TAXINVOICENUMBER;
	}

	public void setTAXINVOICENUMBER(String tAXINVOICENUMBER)
	{
		TAXINVOICENUMBER = tAXINVOICENUMBER;
	}

	public String getNETEQBAL()
	{
		return NETEQBAL;
	}

	public void setNETEQBAL(String nETEQBAL)
	{
		NETEQBAL = nETEQBAL;
	}

	public String getINVVARAMT()
	{
		return INVVARAMT;
	}

	public void setINVVARAMT(String iNVVARAMT)
	{
		INVVARAMT = iNVVARAMT;
	}

	public String getINVBALAMT()
	{
		return INVBALAMT;
	}

	public void setINVBALAMT(String iNVBALAMT)
	{
		INVBALAMT = iNVBALAMT;
	}

	public String getINVOICESUFFIX()
	{
		return INVOICESUFFIX;
	}

	public void setINVOICESUFFIX(String iNVOICESUFFIX)
	{
		INVOICESUFFIX = iNVOICESUFFIX;
	}

	public MiscInvoiceDataModel getMiscInvoiceDataModel()
	{

		if (miscInvoiceDataModel == null){
			return new MiscInvoiceDataModel(this);
		} else {
			return miscInvoiceDataModel;
		}

	}

	public void setMiscInvoiceDataModel(MiscInvoiceDataModel miscInvoiceDataModel)
	{
		this.miscInvoiceDataModel = miscInvoiceDataModel;
	}

	public static Logger getLogger()
	{
		return logger;
	}

	public int getDetailCount()
	{
		return detailCount;
	}

	public void setDetailCount(int vDetailCount)
	{
		detailCount = vDetailCount;
	}

	public boolean isIBMInvoice()
	{
		return getINVOICETYPE().trim().equals("IBM");
	}

	public boolean isCompanyCodeOf0001()
	{
		return getCOMPANYCODE().trim().equals("0001");
	}

	public boolean isCOMInvoice()
	{
		return getINVOICETYPE().trim().equals("COM");
	}

	public boolean isEXEMPTorStartswithDEVofSRNumber()
	{
		if(getSRNUMBER().trim().equals("EXEMPT"))
			return true;
		return getSRNUMBER().startsWith("DEV");
	}

	public String getCreatedTimeStamp()
	{
		return createdTimeStamp;
	}

	public Date getCreatedTimeStampAsDate()
	{
		if(createdTimeStamp != null)
		{
			Timestamp timestamp = Timestamp.valueOf(createdTimeStamp);
			return new Date(timestamp.getTime());
		} else
		{
			return null;
		}
	}

	public void setCreatedTimeStamp(String value)
	{
		try
		{
			createdTimeStamp = value;
		}
		catch(Exception e)
		{
			createdTimeStamp = null;
		}
	}

	Hashtable VATPercentages = null;

	/**
	 * maintains the list of vat code x vat percentages
	 */
	public void setVATPercentages(Hashtable percentages)
	{
		VATPercentages = percentages;
	}

	Hashtable VATPercentagesStr = null;

	public Hashtable getVATPercentagesStr() {
		return VATPercentagesStr;
	}

	public void setVATPercentagesStr(Hashtable vATPercentagesStr) {
		VATPercentagesStr = vATPercentagesStr;
	}




	public String getOLDVATCODE() {
		return OLDVATCODE;
	}

	public void setOLDVATCODE(String oLDVATCODE) {
		OLDVATCODE = oLDVATCODE;
	}

	public boolean isCommissionInvoice()
	{
		if(getINVOICETYPE().trim().equals("COM"))
			return true;
		return getINVOICETYPE().trim().equals("STP");
	}

	public RegionalBigDecimal getVATVariance()
	{
		return VATVariance;
	}

	public void setVATVariance(RegionalBigDecimal vVATVariance)
	{
		VATVariance = vVATVariance;
		VATVariance = vVATVariance;
		if (vVATVariance!=null)
			VATVarianceStr = vVATVariance.toString();

	}

	public String getSEARCHTYPE() {
		return SEARCHTYPE;
	}

	public void setSEARCHTYPE(String sEARCHTYPE) {
		SEARCHTYPE = sEARCHTYPE;
	}

	public boolean initializeIndexing()
			throws IllegalArgumentException, ParseException, Exception
	{



		if (!this.loadIndexValues())
		{
			// handleEvents(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Abort"));
			return false;
		}

		// End 1662854 
		//1664571 [GST] GIL should show different input invoice screens based on the effective date of the invoice
		/*
         EffectiveINDate();
         initializeBillFields();
         initializeLoadField();
		 */
		// End 1664571
		//initializeCurrencies();
		//1676297 GIL MISC - add currency

		initializeMISCombinationCodes();
		//remove below code after clarification from Miguel-Krishna
		//initializeInvoiceTypes();
		//End 1676297
		//POEXCodeCH1
		//CAProvincecodeCH1
		//Test Defect 1613871: GILGUI - not picking up correct defaults from Story 1557681 MANG 11-02-2016


		if (this.getCOUNTRY().equals("CA")){
			initializeProvinceCodes();
		}else{
			initializeVATCodes(); 
		} 





		//End Test Defect 1613871
		//CAProvincecodeCH1

		initializeVATVariance();
		initializeCountryInfo();
		initializeDbCr(); 
		initializeDatabaseValues();
		//Story 1662854 [MISC] CAAPS LA Handling of Invoice Suffix
		if (this.getCOUNTRY().equals("CL") || this.getCOUNTRY().equals("CO") || this.getCOUNTRY().equals("MX") || this.getCOUNTRY().equals("PE")){
			initializeInvoiceSuffixes(); 
		}
		//initializeCompanyCode();




		return true;
	}



	private void initializeMISCombinationCodes()
	{

		// load the invoice types

		ArrayList frominvoicetypes = null;
		String invoiceType;            
		ArrayList<String> invoiceTypes = new ArrayList<String>();
		String defaultInvoiceType = "";
		String defaultPOEXCode = "";
		String defaultComCode = "";
		String defaultPOEXDesc = "";
		String DefaulItemType = "";
		String defaultCurrency = "XXX";
		String currencyCode;
		ArrayList currencyCodes = new ArrayList();
		ArrayList<FormSelect> codes = new ArrayList<FormSelect>();        
		FormSelect code = null;
		FormSelect curCode = null;
		FormSelect defaultCode = null;
		currencyCodeSelectList = new ArrayList<FormSelect>();

		try
		{  

			ResultSet results = getMiscInvoiceDataModel().selectByObjectIdStatement();

			while(results.next()){

				this.setCURRENCY(results.getString(11));
				this.setINVOICETYPE(results.getString(12));
				this.setPOEXCODE(results.getString(13));   
				this.setOLDCOUNTRY(results.getString(19));
			}

			ResultSetCache cachedResults = getMiscInvoiceDataModel().selectCurrencyCodesStatement();
			boolean hasSelectedValue =(this.CURRENCY!=null && !"".equals(this.CURRENCY));
			while (cachedResults.next())
			{
				curCode = new FormSelect();
				currencyCode = cachedResults.getString(1);

				curCode.setLabel(currencyCode);
				curCode.setValue(currencyCode);

				if( hasSelectedValue &&  this.CURRENCY.equalsIgnoreCase(currencyCode)	) {

					curCode.setSelected(true);

				} else if (!hasSelectedValue && cachedResults.getString(2).equals("*"))   {

					curCode.setSelected(true);
				}
				if(cachedResults.getString(2).equals("*"))
				{
					defaultCurrency= currencyCode;
				}

				currencyCodes.add(curCode);
			}

			this.currencyCodeSelectList = currencyCodes;

		} catch (Exception exc)
		{
			//fatal(exc.toString());
			logger.debug("Error initializing currencies");
		}

		/*try
		{                          
			// Get Invoice Type
			ResultSetCache cachedResults = getMiscInvoiceDataModel().selectInvoiceMISCTypesStatement();
			boolean hasSelectedValue =(this.INVOICETYPE!=null && !"".equals(this.INVOICETYPE));
			while (cachedResults.next())
			{
				code = new FormSelect();
				invoiceType = cachedResults.getString(1);
				DefaulItemType = cachedResults.getString(2);

				code.setLabel(invoiceType);
				code.setValue(invoiceType);              

				if( hasSelectedValue &&  this.INVOICETYPE.equalsIgnoreCase(invoiceType)	) {

					code.setSelected(true);

				} else if (!hasSelectedValue && cachedResults.getString(2).equals("*"))   {


					code.setSelected(true);

				}
				if(cachedResults.getString(2).equals("*"))
				{
					defaultInvoiceType = cachedResults.getString(1); 
				}

				codes.add(code);
			}                    

			this.invoiceTypesSelectList=codes;



		}*/ 
		 
		try
		        {                          
		            // Get Invoice Type
		            ResultSetCache cachedResults = getMiscInvoiceDataModel().selectInvoiceMISCTypesStatement();
		            boolean hasSelectedValue =(this.INVOICETYPE!=null && !"".equals(this.INVOICETYPE));
		            while (cachedResults.next())
		            {
		                code = new FormSelect();
		                invoiceType = cachedResults.getString(1);
		                DefaulItemType = cachedResults.getString(2);
		                code.setLabel(invoiceType);
		                code.setValue(invoiceType);       
		                
		                if(hasSelectedValue && getCOUNTRY().equalsIgnoreCase(OLDCOUNTRY) && this.INVOICETYPE.equalsIgnoreCase(invoiceType)    ) {
		                    code.setSelected(true);
		                } else
		                {
		                    if ((!hasSelectedValue || !getCOUNTRY().equalsIgnoreCase(OLDCOUNTRY)) && cachedResults.getString(2).equals("*"))   {
		                    code.setSelected(true);
		                }
		                }
		                if(cachedResults.getString(2).equals("*"))
		                {
		                    defaultInvoiceType = cachedResults.getString(1);
		                }
		                
		                codes.add(code);
		            }                    
		            this.invoiceTypesSelectList=codes;
		 
		        }catch (Exception exc)
		{
			//fatal(exc.toString());
			//Story 1557681 - MANG 10-21-2016
			//Test Defect 1610703-Tried to index Misc invoice for CO- receive error initializing invoyce type.
			logger.debug("Error initializing Invoice types");
		}

		try
		{   
			// Get POEX Code and Company Code
			ResultSetCache cachedResultsCombination = getMiscInvoiceDataModel().selectMISCCombinationCodesStatement(defaultInvoiceType, defaultCurrency);

			while (cachedResultsCombination.next())
			{

				defaultPOEXCode = cachedResultsCombination.getString(1);
				defaultComCode = cachedResultsCombination.getString(2);            	
			}
			this.setCOMPANYCODE(defaultComCode);
			this.COMPANYCODE=this.getCOMPANYCODE();          
		} catch (Exception exc)
		{
			//fatal(exc.toString());
			//Story 1557681 - MANG 10-21-2016
			//Test Defect 1610703-Tried to index Misc invoice for CO- receive error initializing invoyce type.
			logger.debug("Error getting POEX Code or Company Code");
		}
		try
		{ 
			// Get POEX Des            
			ResultSetCache cachedPoexDesc = getMiscInvoiceDataModel().selectMISCPOEXDescStatement(defaultPOEXCode);
			while (cachedPoexDesc.next())
			{
				defaultPOEXDesc = cachedPoexDesc.getString(1);        	
			}
			this.setPOEXCODE(defaultPOEXCode + " " + defaultPOEXDesc);         
			this.POEXCODE = this.getPOEXCODE();


		} catch (Exception exc)
		{
			//fatal(exc.toString());
			//Story 1557681 - MANG 10-21-2016
			//Test Defect 1610703-Tried to index Misc invoice for CO- receive error initializing invoyce type.
			logger.debug("Error initializing POEX Description");
		}
	} 




	/**
	 * load the country defaults from the database and populate the data model
	 *  
	 */
	private void initializeCountryInfo()
	{
		try
		{

			// load the country defaults
			ResultSetCache countryResults = getMiscInvoiceDataModel().selectCountryInfoStatement();

			ResultSetCache ocrRequiredResults = getMiscInvoiceDataModel().selectOCRRequiredStatement();
			if (ocrRequiredResults.next())
			{
				this.setOCRREQUIRED(ocrRequiredResults.getString(1));
			}
		} catch (Exception exc)
		{
			//fatal(exc.toString());
			logger.debug("Error initializing currencies" +exc);
		}
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
		
		boolean hasMatch = false;
		FormSelect code = null;
		FormSelect defaultCode = null;
		vatCodesSelectList = new ArrayList<FormSelect>();
		Hashtable<String, String> VATPercentagesStr = new Hashtable<String, String>();

		try
		{
			
			ResultSet results = getMiscInvoiceDataModel().selectByObjectIdStatement();
			
			while(results.next()){

				this.setVATCODE(results.getString(14));   
				
			}
			boolean hasSelectedValue = (this.VATCODE!=null && !"".equals(this.VATCODE));

			Hashtable<String, com.ibm.gil.util.RegionalBigDecimal> VATPercentages = new Hashtable<String, com.ibm.gil.util.RegionalBigDecimal>();
			ResultSetCache cachedResults = getMiscInvoiceDataModel().selectVATCodesStatement();
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

				VATPercentages.put(cachedResults.getString(2), cachedResults.getRegionalBigDecimal(5));
				VATPercentagesStr.put(cachedResults.getString(2), cachedResults.getRegionalBigDecimal(5).toString());

				if (hasSelectedValue && code.getValue().equalsIgnoreCase(this.VATCODE)){

					code.setSelected(true);
					hasMatch = true;

				}  

				codes.add(code);

			}

			this.vatCodesSelectList = codes;
			this.setVATPercentagesStr(VATPercentagesStr);        	

			if(!hasMatch) {

				Iterator<FormSelect> it = codes.iterator();
				while(it.hasNext()) {

					TaxCodeFormSelect vatcode = (TaxCodeFormSelect)it.next();

					if ( defaultCode!=null &&  vatcode.getValue().equalsIgnoreCase(defaultCode.getValue()) ) {

						vatcode.setSelected(true);
					}

				}

			}


		} catch (Exception exc)
		{
			logger.fatal(exc.toString());
			throw exc;

		}

	}


	private void initializeVATVariance()
	{

		try
		{
			// load the vat variance
			ResultSet varianceResults = getMiscInvoiceDataModel().getVATVarianceStatement();
			if (varianceResults.next())
			{
				RegionalBigDecimal variance = new RegionalBigDecimal(varianceResults.getString(1), RegionalBigDecimal.PERIODDECIMALSEPARATOR);
				this.setVATVariance(variance);
			}
		} catch (Exception exc)
		{
			//fatal(exc.toString());
			logger.debug("Error initializing vat variance"+exc);
		}

	}


	public void initializeCurrencies() throws Exception
	{

		FormSelect code = null;
		ArrayList<FormSelect> codes = new ArrayList<FormSelect>();
		boolean hasDefault = false;
		boolean hasSelectedValue = (this.CURRENCY!=null && !"".equals(this.CURRENCY));
		try
		{

			// load the currency types
			String defaultCurrency = " ";
			String currencyCode;

			ResultSetCache cachedResults = getMiscInvoiceDataModel().selectCurrencyCodesStatement();

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


		} catch (Exception exc)
		{

			logger.fatal(exc.toString());
			throw exc;

		}        

		this.currencyCodeSelectList = codes;
	}



	private void initializeProvinceCodes()
	{

		ArrayList<FormSelect> codes = new ArrayList<FormSelect>();
		boolean hasSelectedValue = (this.VATCODE!=null && !"".equals(this.VATCODE));
		boolean hasMatch = false;
		FormSelect code = null;
		FormSelect defaultCode = null;
		ArrayList ProvinceCodes = new ArrayList ();
		provinceCodesSelectList = new ArrayList<FormSelect>();
		ArrayList orderedProvinceCodes = new ArrayList();
		try
		{


			// load the Province Codes 
			//Test Defect 1626792: GIL MISC- Canada- tax code in GIL is incorrect on re-index MANG 11-29-2016 
			String ProvinceCodeDefault = "";
			String ProvinceCC = "";
			//End Test Defect 1626792
			ArrayList fromProvinceCodes = null;
			String fromProvinceCodesvalue = "";
			fromProvinceCodes = new ArrayList();
			String ProvinceCode;      


			ArrayList VATCodes = new ArrayList();
			Hashtable VATPercentages = new Hashtable();
			Hashtable<String, String> VATPercentagesStr = new Hashtable<String, String>();


			String defaultProvinceCode = " ";
			String provinceCode;

			//Test Defect 1626792: GIL MISC- Canada- tax code in GIL is incorrect on re-index MANG 11-29-2016 
			ResultSet results = getMiscInvoiceDataModel().selectProvinceByObjectIdStatement();
			if (results.next())
			{
				//Test Defect 1636842: Gil-Misc- Reindex- cannot change country code. MANG-03-01-2017
				if (this.getCOUNTRY().equals(results.getString(2))){ 
					ProvinceCodeDefault = results.getString(1);

				}else{
					ProvinceCodeDefault = "AB";            		
				}
				//End Test Defect 1636842
			}else{
				ProvinceCodeDefault = "AB";            	
			}
			//End Test Defect 1626792

			//Test Defect 1620337: GIL-MISC-PROVTAX1 default flag is not working for Province NB MANG 11-14-2016 
			ResultSetCache cachedProvinceResults = getMiscInvoiceDataModel().selectProvinceCodesStatement();
			//End Test Defect 1620337

			boolean hasPROVINCECODESelectedValue =(this.PROVINCECODE!=null && !"".equals(this.PROVINCECODE));

			while (cachedProvinceResults.next()){
				provinceCode = cachedProvinceResults.getString(1);
				orderedProvinceCodes.add(provinceCode);            

			}
			Collections.sort(orderedProvinceCodes);  


			for(int i=0 ;i<orderedProvinceCodes.size();i++)
			{

				code = new FormSelect();
				provinceCode = orderedProvinceCodes.get(i).toString();
				code.setValue(orderedProvinceCodes.get(i).toString());
				code.setLabel(orderedProvinceCodes.get(i).toString()); 

				if(hasPROVINCECODESelectedValue &&  this.PROVINCECODE.equalsIgnoreCase(provinceCode))
				{

					code.setSelected(true);


				}else  if (!hasPROVINCECODESelectedValue && orderedProvinceCodes.get(i).toString().equals(ProvinceCodeDefault)){                   	
					code.setSelected(true);                	
				}                               

				ProvinceCodes.add(code);

			}     





			// Start block for CAVAT codes 

			//Test Defect 1814285 Gil Misc- Canada- default tax code with multiple effective dates
			String GeneralVATDefault = "";
			FormSelect GeneraldefaultVATCode = null;
			/*String invoiceDate = this.getINVOICEDATE();
     		invoiceDate = RegionalDateConverter.convertDate("DB2", "GUI", invoiceDate);
            this.setINVOICEDATE(invoiceDate);*/
			// logger.debug("invoice date in setcavatcodes is :" + invoiceDate );

			if(this.getPROVINCECODE()== null)
				this.setPROVINCECODE(ProvinceCodeDefault);            


			ResultSet defaultVATResults = this.getMiscInvoiceDataModel().selectCAIniVATCodesStatementDef(this.getPROVINCECODE(), "IPC" );
			while (defaultVATResults.next())
			{  

				GeneralVATDefault = defaultVATResults.getString(1);
				break;

			}
			//End Test Defect 1814285        

			//Test Defect 1620337: GIL-MISC-PROVTAX1 default flag is not working for Province NB MANG 11-14-2016 
			//Test Defect 1626792: GIL MISC- Canada- tax code in GIL is incorrect on re-index MANG 11-29-2016 
			// ResultSetCache cacheIniCAVatresults = getMiscInvoiceDataModel().selectCAIniVATCodesStatement(ProvinceCodeDefault);
			ResultSet cacheIniCAVatresults = getMiscInvoiceDataModel().selectCAIniVATCodesStatement(ProvinceCodeDefault);
			String cacheIniCACode = null;
			String defaultcacheIniCACode = null;
			FormSelect caVatCodes = null;
			ArrayList caVatCodesList = new ArrayList();
			

			ResultSet caresults = getMiscInvoiceDataModel().selectByObjectIdStatement();
			
			while(caresults.next()){

				this.setVATCODE(caresults.getString(14));   
				
				
			}
			boolean hasCAVATCODESelectedValue =(this.VATCODE!=null && !"".equals(this.VATCODE)); 	
			


			//End Test Defect 1620337 and Test Defect 1626792
			while (cacheIniCAVatresults.next())
			{
				caVatCodes = new FormSelect();
				cacheIniCACode = cacheIniCAVatresults.getString(1);  
				


				if(hasCAVATCODESelectedValue &&  cacheIniCACode.equalsIgnoreCase(this.VATCODE))
				{
					
					caVatCodes.setSelected(true);

				}else  if (!hasCAVATCODESelectedValue && cacheIniCAVatresults.getString(2).equals("*")){   
				
					caVatCodes.setSelected(true);

				}

				//Test Defect 1618268: GIL-MISC-CA- GIL wants 0 tax MANG 11-08-2016
				//VATPercentages.put(cachedVatResults.getString(1), cachedVatResults.getRegionalBigDecimal(3));  
				RegionalBigDecimal hundred = new RegionalBigDecimal("100").setScale(2); 
				//Story 1633744: GIL-MISC-Canada- tax rate percentage cannot be rounded MANG 12-15-2016
				/*
                RegionalBigDecimal tax = RegionalBigDecimal.ZERO.setScale(2);
                tax = new RegionalBigDecimal(cacheIniCAVatresults.getString(3)).setScale(2);
				 */ 

				/* RegionalBigDecimal tax = new RegionalBigDecimal(cacheIniCAVatresults.getString(3));
                tax = tax.multiply(hundred);
                tax = tax.setScale(3, RegionalBigDecimal.ROUND_HALF_UP);            
                String taxStr= tax.toString();*/



				String tax= cacheIniCAVatresults.getString(3);                
				float taxfloat= Float.parseFloat(tax);                
				taxfloat = taxfloat*100;                



				DecimalFormat df= new DecimalFormat("###.###");               

				String taxStr= String.valueOf(df.format(taxfloat));   


				String vatCodeDesc = null;

				ResultSetCache vatDescResults= getMiscInvoiceDataModel().selectCAVATCodeDescription(cacheIniCACode);

				while (vatDescResults.next()){
					vatCodeDesc= vatDescResults.getString(1);

				}


				String concatVatCode= cacheIniCACode+" - "+taxStr+ "%"+"  "+vatCodeDesc; 


				caVatCodes.setLabel(concatVatCode);
				caVatCodes.setValue(concatVatCode);


				//Test Defect 1814285 Gil Misc- Canada- default tax code with multiple effective dates
				if (GeneralVATDefault.equals(cacheIniCAVatresults.getString(1))){

					GeneraldefaultVATCode = caVatCodes;

				}
				//End Test Defect 1814285 	


				VATCodes.add(concatVatCode);



				RegionalBigDecimal bd = new RegionalBigDecimal(taxStr);

				//End Story 1633744
				caVatCodesList.add(caVatCodes);
				VATPercentages.put(cacheIniCAVatresults.getString(1), bd); 
				VATPercentagesStr.put(cacheIniCAVatresults.getString(1),taxStr);

				//End Test Defect 1618268
			}

			//Test Defect 1814285 Gil Misc- Canada- default tax code with multiple effective dates

			boolean boo= false;      


			Iterator<FormSelect>  vatcodes = caVatCodesList.iterator();
			FormSelect cavatcodes = null;
			while(vatcodes.hasNext()){
				cavatcodes = new FormSelect();
				cavatcodes=vatcodes.next();
				boo= cavatcodes.isSelected();		
				
                   
				if(boo)
					break;  
				else if(GeneraldefaultVATCode==null){             
					cavatcodes.setSelected(true);
					break;         		

				} else if(cavatcodes.getLabel().equals(GeneraldefaultVATCode.getLabel())){
					cavatcodes.setSelected(true);
					break;           		
				}          	
			}   

			//End Test Defect 1814285
			getMiscInvoiceDataModel().setVATPercentages(VATPercentages);
			this.setVATPercentagesStr(VATPercentagesStr);
			this.cacheCAIniCodesSelectList= caVatCodesList;
			this.vatCodesSelectList= caVatCodesList;

		} catch (Exception exc)
		{

			logger.debug("Error initializing province codes:"+ exc);
		}

		this.provinceCodesSelectList= ProvinceCodes;


	}





	public void  initializeCompanyCode() throws Exception {


		ArrayList<FormSelect> codes = new ArrayList<FormSelect>();
		FormSelect code = null;
		FormSelect defaultCode = null;
		boolean hasDefaultSelected = false;

		ArrayList<FormSelect> companyCodesList = null;
		boolean hasSelectedValue = (this.COMPANYCODE!=null && !"".equals(this.COMPANYCODE));

		try {



			// load the companyCode
			String defaultCompanyCode = " ";
			companyCodesList = new ArrayList<FormSelect>();
			ResultSetCache defaultCompanyCodeResults = getMiscInvoiceDataModel().selectCountryInfoStatement();

			/*			if (defaultCompanyCodeResults.next()) {
				defaultCompanyCode = defaultCompanyCodeResults.getString(2);
			}*/


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
			} /*else {
				defaultCode = new FormSelect();
				defaultCode.setValue(defaultCompanyCode);
				defaultCode.setLabel(defaultCompanyCode);
				defaultCode.setSelected(true);
			}*/

			ResultSet cachedResults =  getMiscInvoiceDataModel().selectMISCCompanyCodeStatement();

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

		}

		this.companyCodeSelectList = companyCodesList;
	}


	public boolean loadIndexValues()
	{
		try
		{
			//setCOUNTRY(getCountryCodeFromIndex(getINDEXCLASS()));

			String[] values = null;
			String[] fields = null;
			boolean retry = false;
			boolean validData = false;
			boolean passedValues = true;
			//Test Defect 1615760-GIL-MISC- Change error message for invalid country code, remove defaults button on Gil/Gui screen MANG 11-04-2016
			String countries = null;
			String[]  countriesArray= null;                	
			boolean flag = false;
			CM = getMiscInvoiceDataModel().getContentManager();
			//End Test Defect 1615760
			do
			{
				try
				{
					fields = CM.getIndexFields(CM.indexDescriptionToName(this.getINDEXCLASS(),this.getDatastore()));
					values = getValues();

					if (values == null)
					{
						values = CM.getIndexValues(this.getOBJECTID(),this.getDatastore());
						passedValues = false;
					}

					if ((values != null) && (fields != null) && (values.length > 0) && (fields.length > 0) && (values[0].trim().length() > 0))
					{
						validData = true;
						retry = false;
					} else
					{
						//info("Waiting for CM to release object");
						retry = true;
					}
				} catch (Exception exc)
				{
					//fatal(exc.toString());
					retry = true;
				}	             

				countries = PropertiesManager.getPropertiesManager().getProperty("ACCESS_MISC_COUNTRIES");
				countriesArray= countries.split(",");
				List<String> countrieslList = Arrays.asList(countriesArray); 
				if (!(countrieslList.contains(this.getCOUNTRY()))){
					flag = true;
				}           

				validationCountryCode=false;
				if (flag){
					//error("Invalid Country Code - " + miscInvoice.getCOUNTRY());
					validationCountryCode=true;
					return false;
				}           
				if (retry)
				{

					//Test Defect 1615760-GIL-MISC- Change error message for invalid country code, remove defaults button on Gil/Gui screen MANG 11-04-2016
					/*countries = PropertiesManager.getPropertiesManager().getProperty("ACCESS_MISC_COUNTRIES");
	                	countriesArray= countries.split(",");
	                	List<String> countrieslList = Arrays.asList(countriesArray); 
	                	if (!(countrieslList.contains(this.getCOUNTRY()))){
	                		    flag = true;
	                	}           

	                	validationCountryCode=false;
	                	if (flag){
	                		//error("Invalid Country Code - " + miscInvoice.getCOUNTRY());
	                		validationCountryCode=true;
	                		return false;
	                	}else{                	
	                		//retry = getEventController().prompt("Waiting for indexing to complete.  Retry?");
	                		if (!retry)
	                		{
	                			return false;
	                		}
	                	}*/
					//End Test Defect 1615760
				}
			} while (retry == true);

			if (validData)
			{
				this.setINVOICENUMBER(getFieldMatch("Invoice Number", fields, values));
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

				this.setINVOICEDATE(fromDate);
				this.setTEAM(getFieldMatch("Team", fields, values));
				this.setVENDORNAME(getFieldMatch("Vendor Name", fields, values));
				this.setSOURCE(getFieldMatch("Source", fields, values));

				String timestamp = getFieldMatch("Timestamp", fields, values);
				try
				{
					timestamp = RegionalDateConverter.convertTimeStamp("CM", "GUI", timestamp);
					this.setTIMESTAMP(timestamp);
				} catch (Exception exc)
				{
					logger.info("Timestamp field is blank");
				}
				// defect 1832051
				this.setCREATEDATE(getMiscInvoiceDataModel().retrieveCreatedDate());

			}

		} catch (Exception exc)
		{
			//fatal(exc.toString());
			logger.debug("Error retrieving Content Manager values");
			return false;
		}

		return true;
	}



	private void initializeDbCr() throws Exception
	{
		String[] defaultValues = { "DB", "CR" };
		FormSelect code = null;
		ArrayList<FormSelect> codes = null;

		ResultSet results = getMiscInvoiceDataModel().selectByObjectIdStatement();

		while(results.next()){
			this.setDBCR(results.getString(10));

		}

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


		} catch (Exception exc)
		{
			logger.fatal(exc.toString());
			throw exc;

		}

		this.dbCrSelectList = codes;

	}


	public  void initializeInvoiceSuffixes() throws Exception
	{
		ArrayList<FormSelect> codes = null;
		boolean hasSelectedValue = (this.INVOICESUFFIX!=null && !"".equals(this.INVOICESUFFIX));
		boolean hasMatch = false;

		try
		{

			String invoiceSuffix;
			codes = new ArrayList<FormSelect> ();
			FormSelect code = null;
			FormSelect defaultCode = null;


			defaultCode = new FormSelect();
			defaultCode.setValue("");
			defaultCode.setLabel("");



			ResultSetCache cachedResults = getMiscInvoiceDataModel().selectInvoiceSuffixesStatement();
			while (cachedResults.next())
			{
				invoiceSuffix = cachedResults.getString(1);	            	
				code = new FormSelect();
				code.setValue(invoiceSuffix);
				code.setLabel(invoiceSuffix);
				if (hasSelectedValue && this.INVOICESUFFIX.equalsIgnoreCase(invoiceSuffix)){	            		
					code.setSelected(true);
					hasMatch = true;
				} else if (!hasSelectedValue && cachedResults.getString(2).equals("*"))   {                	 

					code.setSelected(true);
				}

				codes.add(code);
			}

			/* if (!hasMatch){
	            	defaultCode.setSelected(true);
	            	codes.add(defaultCode);
	            }*/


		} catch (Exception exc)
		{
			logger.fatal(exc.toString());
			throw exc;
		}

		this.invoiceSuffixesSelectList = codes;
	}    

	private String validationInvVarAmountStr = "0.00";
	private String validationCreditVarAmountStr = "0.00";
	private String validationDebitVarAmountStr = "0.00";
	private String validationNetAmountStr = "0.00";
	private String validationNetBalanceStr= "0.00"; 




	public boolean initializeDatabaseValues()
			throws SQLException, IllegalArgumentException, ParseException
	{

		try{
			this.validationPaid=false;
			this.validationVarianced=false;
			this.setDIRTYFLAG(false);
			ResultSet results = miscInvoiceDataModel.selectByObjectIdStatement();
			if(!results.next())
			{
				setDOCUMENTMODE(Integer.valueOf(ADDMODE));
				setDIRTYFLAG(Boolean.TRUE.booleanValue());
				setOLDCOUNTRY(getCOUNTRY());
				setOLDINVOICENUMBER(getINVOICENUMBER());
				setOLDINVOICEDATE(getINVOICEDATE());
				vendorSearchRequired = true;
			} else
			{
				setDOCUMENTMODE(Integer.valueOf(UPDATEMODE));
				setDIRTYFLAG(Boolean.FALSE.booleanValue());
				int i = 1;
				String newInvoiceNumber = getINVOICENUMBER();
				String oldInvoiceNumber = DB2.getString(results, i++);
				setOLDINVOICENUMBER(oldInvoiceNumber);
				String newInvoiceDate = getINVOICEDATE();
				String oldInvoiceDate = DB2.getString(results, i++);
				oldInvoiceDate = RegionalDateConverter.convertDate("DB2", "GUI", oldInvoiceDate);
				setOLDINVOICEDATE(oldInvoiceDate);
				RegionalBigDecimal taxAmount = new RegionalBigDecimal(DB2.getString(results, i++), RegionalBigDecimal.PERIODDECIMALSEPARATOR);
				RegionalBigDecimal taxBalance = new RegionalBigDecimal(DB2.getString(results, i++), RegionalBigDecimal.PERIODDECIMALSEPARATOR);
				RegionalBigDecimal netAmount = new RegionalBigDecimal(DB2.getString(results, i++), RegionalBigDecimal.PERIODDECIMALSEPARATOR);
				RegionalBigDecimal netBalance = new RegionalBigDecimal(DB2.getString(results, i++), RegionalBigDecimal.PERIODDECIMALSEPARATOR);
				RegionalBigDecimal totalAmount = new RegionalBigDecimal(DB2.getString(results, i++), RegionalBigDecimal.PERIODDECIMALSEPARATOR);
				setINVBALAMT(netBalance.toString());
				String vendorName = DB2.getString(results, i++);
				this.setVENDORNAME(vendorName);
				setVENDORNUMBER(DB2.getString(results, i++));
				if(getCOUNTRY().equals(DB2.getString(results, 19)))
				{
					setTOTALAMOUNT(totalAmount.toString());
					setVATAMOUNT(taxAmount.toString());
					setDBCR(DB2.getString(results, i++));
					setCURRENCY(DB2.getString(results, i++));
					setINVOICETYPE(DB2.getString(results, i++));
					setOLDVENDORNUMBER(getVENDORNUMBER());
					String defaultPOEXCode = DB2.getString(results, i++);                
					String defaultPOEXDesc = "";

					ResultSetCache cachedPoexDesc = getMiscInvoiceDataModel().selectIniMISCPOEXDescStatement(defaultPOEXCode);
					while (cachedPoexDesc.next())
					{
						defaultPOEXDesc = cachedPoexDesc.getString(1); 

					}

					this.setPOEXCODE(defaultPOEXCode + " " + defaultPOEXDesc);        	

					this.POEXCODE= this.getPOEXCODE();                     
					setVATCODE(DB2.getString(results, i++));                
					String vatcode = this.getVATCODE();
					this.setCOMPANYCODE(DB2.getString(results, i++));
					this.COMPANYCODE= this.getCOMPANYCODE();
					setOLDCOMPANYCODE(getCOMPANYCODE());
					this.setPROVINCECODE(DB2.getString(results, i++));
					this.PROVINCECODE=this.getPROVINCECODE();

				} else
				{
					i = 17;
				}
				setOCR(DB2.getString(results, i++));
				String taxSupplyDate = DB2.getString(results, i++);
				if(getCOUNTRY().equals("CZ") || getCOUNTRY().equals("HU") || getCOUNTRY().equals("PL"))
				{
					taxSupplyDate = RegionalDateConverter.convertDate("DB2", "GUI", taxSupplyDate);
					setTAXSUPPLYDATE(taxSupplyDate);
				}
				String oldCountry = DB2.getString(results, i++);
				setOLDCOUNTRY(oldCountry);
				setTAXINVOICENUMBER(DB2.getString(results, i++));
				if(getCOUNTRY().equals("CL") || getCOUNTRY().equals("CO") || getCOUNTRY().equals("MX") || getCOUNTRY().equals("PE"))
					setINVOICESUFFIX(DB2.getString(results, i++));
				boolean varianced = false;
				RegionalBigDecimal invVarAmount = RegionalBigDecimal.ZERO;
				RegionalBigDecimal creditVarAmount = RegionalBigDecimal.ZERO;
				RegionalBigDecimal debitVarAmount = RegionalBigDecimal.ZERO;
				ResultSet rs = miscInvoiceDataModel.retrieveInvoiceVarianceAmt();
				do
				{
					if(!rs.next())
						break;
					String invoiceType = rs.getString(1);
					if(invoiceType.equals("CR"))
						creditVarAmount = creditVarAmount.add(new RegionalBigDecimal(rs.getString(2), RegionalBigDecimal.PERIODDECIMALSEPARATOR));
					if(invoiceType.equals("DB"))
						debitVarAmount = debitVarAmount.add(new RegionalBigDecimal(rs.getString(2), RegionalBigDecimal.PERIODDECIMALSEPARATOR));
				} while(true);
				invVarAmount = creditVarAmount.subtract(debitVarAmount);
				setINVVARAMT(invVarAmount.toString());
				boolean paid = false;
				if(invVarAmount.compareTo(RegionalBigDecimal.ZERO) == 0 && netBalance.compareTo(RegionalBigDecimal.ZERO) == 0)
				{
					if(!getCOUNTRY().equals(getOLDCOUNTRY()))
					{
						setCOUNTRY(getOLDCOUNTRY());
						return false;
					}
					paid = true;
					this.validationPaid=true;
				}
				if(netAmount.compareTo(netBalance) > 0 && netBalance.compareTo(RegionalBigDecimal.ZERO) > 0)
				{
					paid = true;
					this.validationPaid=true;

				}
				if(invVarAmount.compareTo(RegionalBigDecimal.ZERO) > 0)
				{
					varianced = true;
					this.validationVarianced=true;

				}
				if(isCommissionInvoice() && !vendorName.trim().equals(getVENDORNAME().trim()))
				{
					setDIRTYFLAG(Boolean.TRUE.booleanValue());
					setVENDORNAME(vendorName);
				} else
					if(!vendorName.trim().equals(getVENDORNAME().trim()))
						vendorSearchRequired = true;
				int count = 0;
				results = miscInvoiceDataModel.selectDetailsCountStatement();
				if(results.next())
					count = results.getInt(1);
				setDetailCount(count);
				getMiscInvoiceDataModel().recalculateNetAmount();


				if(netAmount.compareTo(netBalance) == 0)
					this.setNETEQBAL("Y");


				validationInvVarAmountStr = invVarAmount.toString();
				validationCreditVarAmountStr = creditVarAmount.toString();
				validationDebitVarAmountStr = debitVarAmount.toString();
				validationNetAmountStr = netAmount.toString();
				validationNetBalanceStr = netBalance.toString();


			}
		}catch(Exception e)
		{
			logger.debug(e.toString());
			logger.debug("Error initializing invoice");

			return false;
		}




		if(!isWorkAccess(getUSERID()));
		return true;
	} // Initialize DataBase Values end



	public void saveDocument()
			throws Exception
	{

		try
		{

			//getMiscInvoiceDataModel().saveDocument();
			miscInvoiceDataModel=new MiscInvoiceDataModel(this);
			miscInvoiceDataModel.saveDocument();
		}
		catch(Exception sqx)
		{
			logger.debug("exception in  saveDocument():"+sqx);
			throw sqx;
		}
	}

	public boolean saveConvination()
	{
		validationSaveConvination = false;
		try
		{
			ResultSet results = getMiscInvoiceDataModel().selectConvinationStatement();


			if(!results.next())
			{
				setCOMPANYCODE("");
				setPOEXCODE("");
				validationSaveConvination=true;
				return true;
			} else {

				setMiscComb();

			}


		}
		catch(Exception exc) {
			logger.debug("Exception in saveConvination(): "+exc);       	

		}
		return false;
	}


	public void setcavat()
	{
		SetCAVATCodes();
	}

	public void setMiscComb()
	{
		initializeiscComb();
	}

	public void defaultToLastInsertedRecord()
	{
		try
		{
			String defaultPOEXCode = "";
			String defaultPOEXDesc = "";
			String fields[] = {
					getVENDORNAME(), getCOMPANYCODE(), getDBCR(), getCOUNTRY(), getCURRENCY(), getINVOICETYPE(), getVENDORNUMBER(), getPOEXCODE()
			};
			ResultSet results = getMiscInvoiceDataModel().selectLastInvoiceEnteredStatement();
			if(results.next())
			{
				defaultPOEXCode = DB2.getString(results, 8);
				for(int i = 0; i < fields.length; i++);
				for(ResultSetCache cachedPoexDesc = getMiscInvoiceDataModel().selectIniMISCPOEXDescStatement(defaultPOEXCode); cachedPoexDesc.next();)
					defaultPOEXDesc = cachedPoexDesc.getString(1);

				setPOEXCODE((new StringBuilder()).append(defaultPOEXCode).append(" ").append(defaultPOEXDesc).toString());
			}
		}
		catch(SQLException exc) { }
	}

	public boolean validateInput()
	{

		this.isDuplicateInvoicenumber= false ;        


		// check if the invoice exists allready
		ResultSet results;
		try
		{

			results = this.getMiscInvoiceDataModel().selectByInvoiceStatement();
			if (results.next())
			{
				if (!DB2.getString(results, 3).equals(this.getOBJECTID()))
				{                	
					this.isDuplicateInvoicenumber= true ;
					return false;
				}




			}
		} catch (Exception e)
		{
			logger.debug("Error validating Invoice Number"+e);
			//requestFieldFocus(aDataModel.getINVOICENUMBERidx());
			return false;
		}      

		ResultSet vatresults;

		try{


			vatresults= this.getMiscInvoiceDataModel().selectByObjectIdStatement();

			while(vatresults.next()){

				this.setOLDVATCODE(DB2.getString(vatresults, 14));
				logger.debug("OLDVATCODE is :" + this.getOLDVATCODE());
			}

		} catch(Exception e){

			logger.debug("Exception for vatcode is :"+ e);
		}




		try
		{
			boolean choice;
			results = getMiscInvoiceDataModel().selectCheckBySupplierStatement();
			if (results.next())  {
				validationIsInvoiceNumberChanging = true;
			} else {
				validationIsInvoiceNumberChanging = false;
			}
		} catch (Exception e)
		{
			logger.debug("Error validating Invoice Number/Supplier Number");          
			return false;
		}   

		RegionalBigDecimal totalamount = RegionalBigDecimal.ZERO;

		RegionalBigDecimal vatamount = RegionalBigDecimal.ZERO;

		RegionalBigDecimal netvalue = RegionalBigDecimal.ZERO;


		totalamount= getMiscInvoiceDataModel().getDecimal(this.getTOTALAMOUNT());
		vatamount = getMiscInvoiceDataModel().getDecimal(this.getVATAMOUNT());
		isHUFRounded= false;

		///Story 1767639 GILGUI:  Warning message to round for currency HUF
		if(this.getCURRENCY().equals("HUF")){
			Boolean rounded = false;
			if (totalamount.subtract(new RegionalBigDecimal(totalamount.toBigInteger())).abs().compareTo(RegionalBigDecimal.ZERO) > 0){
				rounded = true;
			}else if (vatamount.subtract(new RegionalBigDecimal(vatamount.toBigInteger())).abs().compareTo(RegionalBigDecimal.ZERO) > 0){
				rounded = true;
			}else if(netvalue.subtract(new RegionalBigDecimal(netvalue.toBigInteger())).abs().compareTo(RegionalBigDecimal.ZERO) > 0){
				rounded = true;
			}

			if(rounded){

				isHUFRounded = true;
				//warn("HUF amounts must be rounded to the nearest dollar, please review.");
			}
		}
		//End Story 1767639

		// RegionalBigDecimal netvalue = RegionalBigDecimal.ZERO;
		try
		{
			netvalue = getMiscInvoiceDataModel().getDecimal(this.getNETAMOUNT());
			if (netvalue.compareTo(RegionalBigDecimal.ZERO) <= 0)
			{
				//Implement this in front end -Krishna
				//error("Net Amount must be > 0.00");
				//requestFieldFocus(aDataModel.getTOTALAMOUNTidx());
				return false;
			}
		} catch (Exception exc)
		{
			//Implement this in front end -Krishna
			logger.debug("Net Amount is invalid");
			//requestFieldFocus(aDataModel.getTOTALAMOUNTidx());
			return false;
		}

		//Story 1767639 GILGUI:  Warning message to round for currency HUF
		if(this.getCURRENCY().equals("HUF")){      
			//Implement this in front end -Krishna      
			//warn("HUF amounts must be rounded to the nearest dollar, please review.");
		}
		//End Story 1767639

		if ((this.getVENDORNAME().trim().length() == 0))
		{
			//Implement this in front end -Krishna
			//error("Vendor Name is required fields");
			//requestFieldFocus(this.getVENDORNAME());
			return false;
		}

		RegionalBigDecimal commission = RegionalBigDecimal.ZERO;
		RegionalBigDecimal bd99 = NinetyNinePointNineNineNine;
		try
		{
			commission = getMiscInvoiceDataModel().getDecimal(this.getVENDORCOMMISSION());
		} catch (Exception exc)
		{
		}

		if ((this.isCommissionInvoice()) && (!commission.equals(bd99)))
		{
			if (this.getDOCUMENTMODE() != Indexing.UPDATEMODE)
			{
				//Implement this in front end -Krishna
				//error("Commission Invoices are not valid for this supplier");
				// requestFieldFocus(aDataModel.getINVOICETYPEidx());
				return false;
			}
		}

		if(this.isCOMInvoice() && this.isEXEMPTorStartswithDEVofSRNumber()){
			//Implement this in front end -Krishna
			// error("Commission Invoices are not valid for this supplier");
			// requestFieldFocus(aDataModel.getINVOICETYPEidx());
			return false;        	
		}

		//formats
		String invoiceDate = null;
		Date invoiceDateGMT = null;

		boolean dateOk = true;
		try
		{
			invoiceDate = this.getINVOICEDATE();
			if (invoiceDate == null)
			{
				dateOk = false;
			}
		} catch (Exception exc)
		{
			dateOk = false;
		}


		// validation
		Date now = new Date();

		/**
		 * Defect : 1819908
		 * Desc: added tax supplier date & invoice date validation for more then 300 days
		 * 
		 */

		long MS_1_AFTER = 86400000;

		try {
			java.util.Date invoiceDate1 = RegionalDateConverter.parseDate("GUI", this.INVOICEDATE);
			// defect : 1819908 , change the error msg to warning.
			if (invoiceDate1.getTime() < (now.getTime() - (300 * MS_1_AFTER)))
			{
				logger.debug("Invoice Date must not be greater than 300 days old");
				promptDatePopup = true;
				//getDialogwarnMsgs().add("Invoice date is greater than 300 days old and specific approvals may apply for processing. Do you wish to correct ?");
			}else {
				promptDatePopup = false;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		if(this.getCOUNTRY().equals("CZ")|| this.getCOUNTRY().equals("HU") || this.getCOUNTRY().equals("PL"))
		{
			boolean countryPL = this.getCOUNTRY().equals("PL");
			if(this.getTAXSUPPLYDATE()==null || this.getTAXSUPPLYDATE().trim().length()==0){
				if(!countryPL){
					getDialogErrorMsgs().add("Tax Supply Date is a required field");

				}else
				{
					//PL country so supply date optional.
					promptSupplyDatePopup = false;
				}
			}else{
				java.util.Date taxSupplyDate = null;
				boolean validDate  = true;
				try{
					taxSupplyDate = RegionalDateConverter.parseDate("GUI", this.getTAXSUPPLYDATE());
					if(taxSupplyDate == null){
						validDate = false;
					}
				}catch(Exception exc){
					validDate = false;
				}
				if(validDate == false){
					getDialogErrorMsgs().add("Tax Supply Date is a required field");
				}
				if (taxSupplyDate.getTime() < (now.getTime() - (300 * MS_1_AFTER)))
				{
					//getDialogErrorMsgs().add("Tax Supply Date must not be greater than 300 days old");
					//getDialogwarnMsgs().add("Tax Supply Date is greater than 300 days old and specific approvals may apply for processing. Do you wish to correct ?");
					promptSupplyDatePopup = true;
				}else {
					promptSupplyDatePopup = false;
				}
			}
		} 

		//  long MS_1_AFTER = 86400000;
		//Implement this in the front-end -Krishna
		/* if (invoiceDate.getTime() < (now.getTime() - (300 * MS_1_AFTER)))
        {
        	//Implement this in the front-end -Krishna
           //error("Invoice Date must not be greater than 300 days old");
           //requestFieldFocus(aDataModel.getINVOICEDATEidx());
            return false;
        }*/


		// retrieve the scandate from CM
		getMiscInvoiceDataModel().retrieveCreatedDate();






		/*
		 * soft errors don't stop processing
		 */
		// vat amount must be withing getVATVariance() of the calculated
		// percentage, but it's a soft error

		//remove the comment on below block -Krishna
		RegionalBigDecimal targetVat = this.getDecimal(this.getVATTARGETAMOUNT());
		RegionalBigDecimal vatVariance = this.getVATVariance();
		vatVariance = vatVariance.setScale(2, RegionalBigDecimal.ROUND_HALF_UP);

		// check if the diff is > variance

		// }






		//TaxInvoiceNumberCH1  
		//Implement this in front end-Krishna	
		/* if(aDataModel.getCOUNTRY().equals("KR") && aDataModel.getTAXINVOICENUMBER().trim().length()==0)
        {
        	boolean choice = false;
        	//Implement below code on front end-Krishna
        	//choice = prompt("Tax Invoice Number is not entered and do they want to add");
        	if (choice == true)
        	{
        		requestFieldFocus(aDataModel.getTAXINVOICENUMBERidx());
        		return false;
             }
        } */       
		//TaxInvoiceNumberCH1

		// validate the document got indexed correctly
		boolean exists = false;
		try
		{
			DOCUMENT cmdoc = getMiscInvoiceDataModel().queryDocument();
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
			//Implement below code on front end-Krishna
			// error("Document did not index correctly");
			// requestFieldFocus(this.getINVOICENUMBER());
			return false;
		}

		//Test Defect 1693987 - GIL MISC-ADD CURRENCY
		if (saveConvination()){
			//implement the below code on front end -Krishna
			// error("Error Currency not on file for invoice type.");
			return false;
		}
		//End Test Defect 1693987

		return true;
	}

	public void updateIndexValues()
	{
		try
		{
			String invoiceDate = RegionalDateConverter.convertDate("GUI", "CM", this.getINVOICEDATE());
			String objectId = this.getOBJECTID();
			// CM = getMiscInvoiceDataModel().getContentManager();
			CM  = new ContentManagerImplementation(this.getDatastore());
			String fields[] = {"Invoice Number", "Invoice Date", "Vendor Name"};
			if(CM!=null){

				logger.debug("CM object is :" + CM);
			}

			String values[] = {this.getINVOICENUMBER(),invoiceDate, this.getVENDORNAME()};
			if(objectId.trim().length() > 0 && this.getINVOICENUMBER()!=null) 
			{
				boolean retry = false;
				do
				{
					boolean rc = CM.updateDocument(objectId, fields, values, this.getDatastore());
					if(rc)
						retry = false;
				} while(retry);
			}
		}
		catch(Exception exc) {

			logger.debug("Exception in  updateIndexValues():"+ exc);
		}
	}

	//look at the below commented block if it needs to be implemented-Krishna


	private void SetCAVATCodes()
	{
		try
		{

			FormSelect caVATCodes = null;
			ArrayList vATCodes = new ArrayList();
			Hashtable VATPercentages = new Hashtable();
			//RegionalBigDecimal tax;
			String VATCode = null;
			//Test Defect 1814285 Gil Misc- Canada- default tax code with multiple effective dates
			String GeneralVATDefault = "";
			FormSelect GeneraldefaultVATCode = null;


			ResultSet defaultVATResults = this.getMiscInvoiceDataModel().selectCAIniVATCodesStatementDef(this.getPROVINCECODE(),"SCVC");
			while (defaultVATResults.next())
			{  

				GeneralVATDefault = defaultVATResults.getString(1);
				break;

			}
			//End Test Defect 1814285


			ResultSet cachedCAVatresults = getMiscInvoiceDataModel().selectCAVATCodesStatement();
			boolean hasSelectedValue =(this.VATCODE!=null && !"".equals(this.VATCODE));
			while(cachedCAVatresults.next())
			{

				caVATCodes = new FormSelect();
				VATCode = cachedCAVatresults.getString(1);
				/*caVATCodes.setLabel(VATCode);
                caVATCodes.setValue(VATCode);*/
				boolean isvatcode = (hasSelectedValue &&  (this.VATCODE).equalsIgnoreCase(VATCode));

				/*if(hasSelectedValue &&  (this.VATCODE).equalsIgnoreCase(VATCode))
                {    
                	logger.debug("inside if");
                    caVATCodes.setSelected(true);

                } else  if (!hasSelectedValue && cachedCAVatresults.getString(3).equals("*")){ 
                	logger.debug("inside else if : ");
                	caVATCodes.setSelected(true);

                }*/


				if ( (cachedCAVatresults.getString(3)).equals("*")){ 

					caVATCodes.setSelected(true);

				}              



				String tax= cachedCAVatresults.getString(2);
				float taxfloat= Float.parseFloat(tax);
				taxfloat = taxfloat*100;              

				DecimalFormat df= new DecimalFormat("###.###");


				String taxStr= String.valueOf(df.format(taxfloat));


				String vatCodeDesc = null;

				ResultSetCache vatDescResults= getMiscInvoiceDataModel().selectCAVATCodeDescription(VATCode);

				while (vatDescResults.next()){
					vatCodeDesc= vatDescResults.getString(1);

				}



				String concatVatCode= VATCode+" -  "+ taxStr+"%"+"  "+vatCodeDesc; 

				caVATCodes.setLabel(concatVatCode);
				caVATCodes.setValue(concatVatCode);

				//Test Defect 1814285 Gil Misc- Canada- default tax code with multiple effective dates
				if (GeneralVATDefault.equals(cachedCAVatresults.getString(1))){
					logger.debug("inside equals block");
					GeneraldefaultVATCode = caVATCodes;

				}
				//End Test Defect 1814285 

				vATCodes.add(caVATCodes);


				RegionalBigDecimal bd = new RegionalBigDecimal(taxStr);

				VATPercentages.put(cachedCAVatresults.getString(1), bd);
				VATPercentagesStr.put(cachedCAVatresults.getString(1), taxStr);

			}  
			//Test Defect 1814285 Gil Misc- Canada- default tax code with multiple effective dates

			boolean boo= false;       
			Iterator<FormSelect>  vatcodes = vATCodes.iterator();
			FormSelect cavatcodes = null;
			while(vatcodes.hasNext()){
				cavatcodes = new FormSelect();
				cavatcodes=vatcodes.next();
				boo= cavatcodes.isSelected();


				if(boo)
					break;      
				else if(cavatcodes.getLabel().equals(GeneraldefaultVATCode.getLabel())){
					cavatcodes.setSelected(true);
					break;

				}
			}   

			//End Test Defect 1814285

			this.caVATCodesSelectList= vATCodes;
			getMiscInvoiceDataModel().setVATPercentages(VATPercentages);
			this.setVATPercentagesStr(VATPercentagesStr);

		}
		catch(Exception exc) { 
			logger.debug("exception inside setcavatcodes:"+ exc);


		}
	}

	//CAProvincecodeCH1

	public void initializeiscComb()
	{


		String defaultCompanyCode = "";
		String defaultPOEXCode = "";
		String defaultPOEXDesc = "";

		try
		{     
			ResultSet results = getMiscInvoiceDataModel().selectMISCCompanyCodeStatement();
			if (results.next()) {
				defaultCompanyCode = results.getString(1);
				defaultPOEXCode = results.getString(2);

				this.setCOMPANYCODE(defaultCompanyCode); 
				this.setPOEXCODE(defaultPOEXCode);  

			} 



		} catch (Exception exc)
		{ 
			//-implement on the front end-krishna
			logger.debug(exc.toString());
			//error("Currency not on file for invoice type");
		}

		try
		{ 
			// Get POEX Des   
			ResultSet resultsDesc = getMiscInvoiceDataModel().selectMISCDescStatement();
			while (resultsDesc.next())
			{
				defaultPOEXDesc = resultsDesc.getString(1);        	
			}
			this.setPOEXCODE(defaultPOEXCode + " " + defaultPOEXDesc);  		

		} catch (Exception exc)
		{

			logger.debug(exc.toString());

		}
	}

	public void rollbackIndexing()
	{
		try
		{
			ResultSet results = getMiscInvoiceDataModel().selectByObjectIdStatement();
			if(results.next())
			{

				String invoiceNumber = DB2.getString(results, 1);
				this.setINVOICENUMBER( invoiceNumber);
				String invoiceDate = DB2.getString(results, 2);
				invoiceDate = RegionalDateConverter.convertDate("DB2", "GUI", invoiceDate);
				this.setINVOICEDATE(invoiceDate);
				String vendorName= DB2.getString(results, 8);
				this.setVENDORNAME(vendorName);                
			}
		}
		catch(Exception exc) {

			logger.debug("Exception in rollbackindexing:"+ exc);


		}
		try{
			updateIndexValues();
		}
		catch(Exception e ){
			logger.debug("Exception in rollbackindexing:"+ e);
		}
	}

	public void rollbackCountryIndexing()
	{
		CM = getMiscInvoiceDataModel().getContentManager();
		try
		{
			String objectId = getOBJECTID();
			if(objectId.trim().length() > 0)
			{
				boolean retry = false;
				do
				{
					boolean rc = CM.moveDocument(objectId, (new StringBuilder()).append("Invoice ").append(getCOUNTRY()).toString(), getDatastore());
					if(rc)
						retry = false;
				} while(retry);
			}
		}
		catch(Exception exc) { }
	}



}
