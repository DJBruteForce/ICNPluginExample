package com.ibm.gil.business;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;



import com.ibm.gil.model.InvoiceDataModelELS;
import com.ibm.gil.util.RegionalBigDecimal;
import com.ibm.gil.util.RegionalDateConverter;
import com.ibm.igf.hmvc.ResultSetCache;
import com.ibm.mm.sdk.server.DKDatastoreICM;

/**
 * @author ajimena
 *
 */
public class InvoiceELS  extends Indexing{
	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(InvoiceELS.class);
	private String displayTaxWarn="N";
	private String displayRefInvSearch="N";
	private String displayChangeInvNum="N";
	private String displayErrorDuplicateInvNum="N";
	private boolean invDialfieldsEditable=false;//disabling false
	/***	
	 * 	Story 1582060: GILGUI reading POEX default code
	 ****/
	private String defaultVENPoex="";
	private String defaultCOMPoex="";
	
	

//    /*
//     * units and comments
//     */
    private ArrayList<LineItemELS> lineItems = new ArrayList();
    private ArrayList<InvoiceCommentsELS> comments = new ArrayList();

    private ArrayList<String> errorCodes = new ArrayList();
    private ArrayList<String> dialogErrorMsgs = new ArrayList();
    private ArrayList<String> dialogWarningMsgs = new ArrayList();
    public static final  RegionalBigDecimal ONEHUNDRED = new RegionalBigDecimal(100.0);
    private ArrayList<String> supplierNums = new ArrayList();
    private ArrayList<FormSelect> refListData=new ArrayList();
    private HashMap<String, String> refDataValues=new HashMap();
    private  InvoiceDataModelELS invoiceDataModelELS;
//
    public boolean isDistributorListExisting= false; 
    
    public InvoiceELS(){
    	
    }
    public InvoiceELS(String country)
    {
    	this.country=country;
//        super(FIELDCOUNT);
    	this.invoiceDataModelELS= new InvoiceDataModelELS(this);
    }
    public InvoiceELS(String country,DKDatastoreICM  datastore )
    {
    	this.country=country;
    	setDatastore(datastore);
//        super(FIELDCOUNT);
    	this.invoiceDataModelELS= new InvoiceDataModelELS(this);
    }
    

    public InvoiceDataModelELS getInvoiceDataModelELS() {
    	if(invoiceDataModelELS==null){
    		this.invoiceDataModelELS= new InvoiceDataModelELS(this);
    	}
		return invoiceDataModelELS;
	}

	public void setInvoiceDataModelELS(InvoiceDataModelELS invoiceDataModelELS) {
		this.invoiceDataModelELS = invoiceDataModelELS;
	}


	// Story 1750051 - CA Gil Changes
	
	private String provinceCode = "";
    private String billedToCustomer = "";

    
    
	public String getPROVINCECODE() {
		return provinceCode;
	}
	public void setPROVINCECODE(String pROVINCECODE) {
		provinceCode = pROVINCECODE;
	}
	public String getBilledToCustomer() {
		return billedToCustomer;
	}
	public void setBilledToCustomer(String billedToCustomer) {
		this.billedToCustomer = billedToCustomer;
	}

	// End Story 1750051 - CA Gil Changes

	public ArrayList<String> getDialogErrorMsgs() {
		return dialogErrorMsgs;
	}


	public ArrayList<String> getDialogWarningMsgs() {
		return dialogWarningMsgs;
	}
	public void setDialogWarningMsgs(ArrayList<String> dialogWarningMsgs) {
		this.dialogWarningMsgs = dialogWarningMsgs;
	}
	public ArrayList<FormSelect> getRefListData() {
		return refListData;
	}
	
	public String getDisplayRefInvSearch() {
		return displayRefInvSearch;
	}
	public void setDisplayRefInvSearch(String displayRefInvSearch) {
		this.displayRefInvSearch = displayRefInvSearch;
	}
	



	public String getDefaultVENPoex() {
		return defaultVENPoex;
	}
	public void setDefaultVENPoex(String defaultVENPoex) {
		this.defaultVENPoex = defaultVENPoex;
	}
	public String getDefaultCOMPoex() {
		return defaultCOMPoex;
	}
	public void setDefaultCOMPoex(String defaultCOMPoex) {
		this.defaultCOMPoex = defaultCOMPoex;
	}
	public HashMap<String, String> getRefDataValues() {
		return refDataValues;
	}
	public void setRefDataValues(HashMap<String, String> refDataValues) {
		this.refDataValues = refDataValues;
	}




	/*
     * getters and setters
     */
//    public short getINVOICENUMBERidx()
//    {
//        return INVOICENUMBERidx;
//    }
    String invoiceNumber="";
    String invoiceDate="";
    public String getINVOICENUMBER()
    {
    	return this.invoiceNumber;
       
    }

    public void setINVOICENUMBER(String invoiceNumber)
    {
        this.invoiceNumber=invoiceNumber;	
    }


    public String getINVOICEDATE()
    {
        return this.invoiceDate;
    }

    public Date getINVOICEDATEasDate()
    {
        try
        {
            String invoiceDate = getINVOICEDATE();
            if (invoiceDate.trim().length() == 0)
                return null;

            Date invoiceDateAsDate = RegionalDateConverter.parseDate("GUI", invoiceDate);
            return (invoiceDateAsDate);
        } catch (Exception exc)
        {
            return (null);
        }
    }

    public void setINVOICEDATE(String invoiceDate)
    {
        this.invoiceDate=invoiceDate;
    }

    public void setINVOICEDATEasDate(Date invoiceDateAsDate)
    {
        try
        {
            setINVOICEDATE(RegionalDateConverter.formatDate("GUI", invoiceDateAsDate));
        } catch (Exception exc)
        {
            setINVOICEDATE("");
        }
    }
    
    private String country="";
    
    public String getCOUNTRY(){
    	return this.country;
    }
    public void setCOUNTRY(String country){
    	this.country=country;
    }
    String oldInvoiceNumber="";
    public String getOLDINVOICENUMBER()
    {
        return this.oldInvoiceNumber;
    }

    public void setOLDINVOICENUMBER(String oldInvoiceNumber)
    {
        this.oldInvoiceNumber=oldInvoiceNumber;
    }

//    public short getOLDINVOICEDATEidx()
//    {
//        return OLDINVOICEDATEidx;
//    }

    String oldInvoiceDate="";
    public String getOLDINVOICEDATE()
    {
        return this.oldInvoiceDate;
    }

    public void setOLDINVOICEDATE(String oldInvoiceDate)
    {
        this.oldInvoiceDate=oldInvoiceDate;
    }

//    public short getINVOICETYPEidx()
//    {
//        return INVOICETYPEidx;
//    }
    String invoiceType="";
    public String getINVOICETYPE()
    {
        return this.invoiceType;
    }

    public void setINVOICETYPE(String invoiceType)
    {
       this.invoiceType=invoiceType;
    }

//    public short getTOTALAMOUNTidx()
//    {
//        return TOTALAMOUNTidx;
//    }
    String totalAmount="";
    public String getTOTALAMOUNT()
    {
        return this.totalAmount;
    }

    public void setTOTALAMOUNT(String totalAmount)
    {
       this.totalAmount=  new RegionalBigDecimal(totalAmount).setScale(2, RegionalBigDecimal.ROUND_HALF_UP).toString();
    }

    public void setTOTALAMOUNTasRegionalBigDecimal(RegionalBigDecimal totalAmount)
    {
    	this.totalAmount= totalAmount.setScale(RegionalBigDecimal.getDefaultScale(), BigDecimal.ROUND_HALF_UP).toString();
    }
    
    public void setTOTALAMOUNTasBigDecimal(BigDecimal totalAmount)
    {
    	this.totalAmount= new RegionalBigDecimal(totalAmount.toString(), RegionalBigDecimal.PERIODDECIMALSEPARATOR).toString();
    }

    
    String igfBilledToAmt="";
    public String getIGFBILLEDTOAMOUNT()
    {
        return this.igfBilledToAmt;
    }

    public void setIGFBILLEDTOAMOUNT(String igfBilledToAmt)
    {
    	this.igfBilledToAmt= new RegionalBigDecimal(igfBilledToAmt).setScale(2, RegionalBigDecimal.ROUND_HALF_UP).toString();
    }

    public void setIGFBILLEDTOAMOUNTasRegionalBigDecimal(RegionalBigDecimal igfBilledToAmt)
    {
        setIGFBILLEDTOAMOUNT(igfBilledToAmt.setScale(RegionalBigDecimal.getDefaultScale(), BigDecimal.ROUND_HALF_UP).toString());
    }
    
    public void setIGFBILLEDTOAMOUNTasBigDecimal(BigDecimal igfBilledToAmt)
    {
        setIGFBILLEDTOAMOUNTasRegionalBigDecimal(new RegionalBigDecimal(igfBilledToAmt.toString(), RegionalBigDecimal.PERIODDECIMALSEPARATOR));
    }

    public BigDecimal getIGFBILLEDTOAMOUNTasBigDecimal()
    {
        return getDecimal(igfBilledToAmt).toBigDecimal();
    }    


    String vatAmount="";
    public String getVATAMOUNT()
    {
        return  this.vatAmount;
    }

    public void setVATAMOUNT(String vatAmount)
    {
        this.vatAmount= new RegionalBigDecimal(vatAmount).setScale(2, RegionalBigDecimal.ROUND_HALF_UP).toString();
    }

    public void setVATAMOUNTasRegionalBigDecimal(RegionalBigDecimal vatAmount)
    {
        setVATAMOUNT(vatAmount.setScale(RegionalBigDecimal.getDefaultScale(), BigDecimal.ROUND_HALF_UP).toString());
    }
    
    public void setVATAMOUNTasBigDecimal(BigDecimal vatAmount)
    {
        setVATAMOUNTasRegionalBigDecimal(new RegionalBigDecimal(vatAmount.toString(), RegionalBigDecimal.PERIODDECIMALSEPARATOR));
    }

    public BigDecimal getVATAMOUNTasBigDecimal()
    {
        return getDecimal(this.vatAmount).toBigDecimal();
    }
    
    String taxesBilledToIgf="";
//    public short getTAXESBILLEDTOIGFidx()
//    {
//        return TAXESBILLEDTOIGFidx;
//    }

    public String getTAXESBILLEDTOIGF()
    {
        return this.taxesBilledToIgf;
    }
    
    public String getDisplayErrorDuplicateInvNum() {
		return displayErrorDuplicateInvNum;
	}
	public void setDisplayErrorDuplicateInvNum(String displayErrorDuplicateInvNum) {
		this.displayErrorDuplicateInvNum = displayErrorDuplicateInvNum;
	}
	public String getDisplayChangeInvNum() {
		return displayChangeInvNum;
	}
	public void setDisplayChangeInvNum(String displayChangeInvNum) {
		this.displayChangeInvNum = displayChangeInvNum;
	}
	public void setTAXESBILLEDTOIGF(String taxesBilledToIgf)
    {
        this.taxesBilledToIgf= new RegionalBigDecimal(taxesBilledToIgf).setScale(2, RegionalBigDecimal.ROUND_HALF_UP).toString();
    }

    public void setTAXESBILLEDTOIGFasRegionalBigDecimal(RegionalBigDecimal taxesBilledToIgf)
    {
        setTAXESBILLEDTOIGF(taxesBilledToIgf.setScale(RegionalBigDecimal.getDefaultScale(), BigDecimal.ROUND_HALF_UP).toString());
    }
    
    public void setTAXESBILLEDTOIGFasBigDecimal(BigDecimal taxesBilledToIgf)
    {
        setTAXESBILLEDTOIGFasRegionalBigDecimal(new RegionalBigDecimal(taxesBilledToIgf.toString(), RegionalBigDecimal.PERIODDECIMALSEPARATOR));
    }

    public BigDecimal getTAXESBILLEDTOIGFasBigDecimal()
    {
    	return getDecimal(taxesBilledToIgf).toBigDecimal();
    }    


    private String taxesBilledToCust="";
    public String getTAXESBILLEDTOCUST()
    {
        return this.taxesBilledToCust;
    }

    public void setTAXESBILLEDTOCUST(String taxesBilledToCust)
    {
        this.taxesBilledToCust= new RegionalBigDecimal(taxesBilledToCust).setScale(2, RegionalBigDecimal.ROUND_HALF_UP).toString();
    }

    public void setTAXESBILLEDTOCUSTasRegionalBigDecimal(RegionalBigDecimal taxesBilledToCust)
    {
        setTAXESBILLEDTOCUST(taxesBilledToCust.setScale(RegionalBigDecimal.getDefaultScale(), BigDecimal.ROUND_HALF_UP).toString());
    }
    
    public void setTAXESBILLEDTOCUSTasBigDecimal(BigDecimal taxesBilledToCust)
    {
        setTAXESBILLEDTOCUSTasRegionalBigDecimal(new RegionalBigDecimal(taxesBilledToCust.toString(), RegionalBigDecimal.PERIODDECIMALSEPARATOR));
    }

    private String vatBalance="";
    public String getVATBALANCE()
    {
        return this.vatBalance;
    }

    public void setVATBALANCE(String vatBalance)
    {
       this.vatBalance= new RegionalBigDecimal(vatBalance).setScale(2, RegionalBigDecimal.ROUND_HALF_UP).toString();
    }

    public void setVATBALANCEasRegionalBigDecimal(RegionalBigDecimal vatBalance)
    {
        setVATBALANCE(vatBalance.setScale(RegionalBigDecimal.getDefaultScale(), BigDecimal.ROUND_HALF_UP).toString());
    }
    public void setVATBALANCEasBigDecimal(BigDecimal vatBalance)
    {
        setVATBALANCEasRegionalBigDecimal(new RegionalBigDecimal(vatBalance.toString(), RegionalBigDecimal.PERIODDECIMALSEPARATOR));
    }
    
    public BigDecimal getVATBALANCEasBigDecimal()
    {
        return getDecimal(this.vatBalance).toBigDecimal();
    }


    private String netAmount="";
    public String getNETAMOUNT()
    {
        return this.netAmount;
    }

    public void setNETAMOUNT(String netAmount)
    {
        this.netAmount= new RegionalBigDecimal(netAmount).setScale(2, RegionalBigDecimal.ROUND_HALF_UP).toString();
    }

    public void setNETAMOUNTasRegionalBigDecimal(RegionalBigDecimal netAmount)
    {
        setNETAMOUNT(netAmount.setScale(RegionalBigDecimal.getDefaultScale(), BigDecimal.ROUND_HALF_UP).toString());
    }
    
    public void setNETAMOUNTasBigDecimal(BigDecimal netAmount)
    {
        setNETAMOUNTasRegionalBigDecimal(new RegionalBigDecimal(netAmount.toString(), RegionalBigDecimal.PERIODDECIMALSEPARATOR));
    }

    public BigDecimal getNETAMOUNTasBigDecimal()
    {
        return getDecimal(this.netAmount).toBigDecimal();
    }

//    public short getNETBALANCEidx()
//    {
//        return NETBALANCEidx;
//    }
    private String netBalance="";
    public String getNETBALANCE()
    {
        return this.netBalance;
    }

    public void setNETBALANCE(String netBalance)
    {
    	this.netBalance =new RegionalBigDecimal(netBalance).setScale(2, RegionalBigDecimal.ROUND_HALF_UP).toString();
    }
   

//    public String getOTAMOUNT()
//    {
//        return (String) get(OTAMOUNTidx);
//    }
    private String otAmount="";
    
    public void setOTAMOUNT(String otAmount)
    {
        this.otAmount=new RegionalBigDecimal(otAmount).setScale(2, RegionalBigDecimal.ROUND_HALF_UP).toString();
    }
    public String getOTAMOUNT(){
    	return this.otAmount;
    }
    public void setNETBALANCEasRegionalBigDecimal(RegionalBigDecimal otAmount)
    {
        setNETBALANCE(otAmount.setScale(RegionalBigDecimal.getDefaultScale(), BigDecimal.ROUND_HALF_UP).toString());
    }

    public void setNETBALANCEasBigDecimal(BigDecimal otAmount)
    {
        setNETBALANCEasRegionalBigDecimal(new RegionalBigDecimal(otAmount.toString(), RegionalBigDecimal.PERIODDECIMALSEPARATOR));
    }

    public BigDecimal getNETBALANCEasBigDecimal()
    {
    	return getDecimal(this.netBalance).toBigDecimal();
    }

//    public short getLINEITEMTOTALAMOUNTidx()
//    {
//        return LINEITEMTOTALAMOUNTidx;
//    }
    private String lineItemTotalAmount="";
    public String getLINEITEMTOTALAMOUNT()
    {
        return this.lineItemTotalAmount;
    }

    public void setLINEITEMTOTALAMOUNT(String lineItemTotalAmount)
    {
        this.lineItemTotalAmount= new RegionalBigDecimal(lineItemTotalAmount).setScale(2, RegionalBigDecimal.ROUND_HALF_UP).toString();
    }

    
    private String lineItemVatAmount="";
    public String getLINEITEMVATAMOUNT()
    {
        return this.lineItemVatAmount;
    }

    public void setLINEITEMVATAMOUNT(String lineItemVatAmount)
    {
        this.lineItemVatAmount= new RegionalBigDecimal(lineItemVatAmount).setScale(2, RegionalBigDecimal.ROUND_HALF_UP).toString();
    }

    private String lineItemNetAmount="";
    public String getLINEITEMNETAMOUNT()
    {
        return this.lineItemNetAmount;
    }

    public void setLINEITEMNETAMOUNT(String lineItemNetAmount)
    {
        this.lineItemNetAmount= new RegionalBigDecimal(lineItemNetAmount).setScale(2, RegionalBigDecimal.ROUND_HALF_UP).toString();
    }

    private String vatVariance="";

    public String getVATVARIANCE()
    {
        return this.vatVariance;
    }

    public void setVATVARIANCE(String vatVariance)
    {
        this.vatVariance= new RegionalBigDecimal(vatVariance).setScale(2, RegionalBigDecimal.ROUND_HALF_UP).toString();
    }


    private String costCenter="";
    public String getCOSTCENTER()
    {
        return this.costCenter;
    }

    public void setCOSTCENTER(String costCenter)
    {
        this.costCenter=costCenter;
    }


    private String vatRegistrationNumber="";
    
    public String getVATREGISTRATIONNUMBER()
    {
        return this.vatRegistrationNumber;
    }

    public void setVATREGISTRATIONNUMBER(String vatRegistrationNumber)
    {
        this.vatRegistrationNumber=vatRegistrationNumber;
    }

//    public short getVATREGISTRATIONNUMBERREQUIREDidx()
//    {
//        return VATREGISTRATIONNUMBERREQUIREDidx;
//    }
    private String vatRegistrationNumReq="";
    public String getVATREGISTRATIONNUMBERREQUIRED()
    {
        return this.vatRegistrationNumReq;
    }

    public void setVATREGISTRATIONNUMBERREQUIRED(String vatRegistrationNumReq)
    {
        this.vatRegistrationNumReq=vatRegistrationNumReq;
    }

    public boolean isVATRegistrationNumberRequired()
    {
        String value = getVATREGISTRATIONNUMBERREQUIRED();
        if (value == null)
            return false;
        if (value.equals("Y"))
            return true;

        return false;
    }

//    public short getELSINDCidx()
//    {
//        return ELSINDCidx;
//    }
    private String elsIndc="";
    
    public String getELSINDC()
    {
        return this.elsIndc;
    }

    public void setELSINDC(String elsIndc)
    {
        this.elsIndc=elsIndc;
    }    

    
    public Boolean getELSINDCasBoolean()
    {
        if (getELSINDC().equals("Y"))
            return Boolean.TRUE;
        else
            return Boolean.FALSE;

    }
    
    private String toleranceIndc="";
    
    public String getTOLERANCEINDC()
    {
        return this.toleranceIndc;
    }

    public void setTOLERANCEINDC(String toleranceIndc)
    {
        this.toleranceIndc=toleranceIndc;
    }    

    public boolean isTolerance()
    {
    	return getTOLERANCEINDC().equals("Y");
    }    
    
    private String stateCode="";
    public String getSTATECODE()
    {
        return this.stateCode;
    }

    public void setSTATECODE(String stateCode)
    {
        this.stateCode=stateCode;
    }        
    
//	public short getDISTRIBUTORNUMBERidx() {
//		return DISTRIBUTORNUMBERidx;
//	}
	
    private String distributorNum="";
	public String getDISTRIBUTORNUMBER()
	{
		return this.distributorNum;
	}

	public void setDISTRIBUTORNUMBER(String distributorNum)
	{
		this.distributorNum=distributorNum;
	} 
	
//    public short getPURCHASEORDERNUMBERidx()
//    {
//        return PURCHASEORDERNUMBERidx;
//    }

	private String purchaseOrderNum="";
    public String getPURCHASEORDERNUMBER()
    {
        return this.purchaseOrderNum;
    }

    public void setPURCHASEORDERNUMBER(String purchaseOrderNum)
    {
        this.purchaseOrderNum=purchaseOrderNum;
    }
    
    private String usTaxPercent="";
    public void setUSTAXPERCENT(String usTaxPercent)
    {
        this.usTaxPercent=usTaxPercent;
    }
//
//    public short getUSTAXPERCENTidx()
//    {
//        return USTAXPERCENTidx;
//    }

    public String getUSTAXPERCENT()
    {
        return this.usTaxPercent;
    }    
//    public short getDBCRidx()
//    {
//        return DBCRidx;
//    }
    
    private String dbCr="";
    
    public String getDBCR()
    {
        return this.dbCr;
    }

    public void setDBCR(String dbCr)
    {
        this.dbCr=dbCr;
    }

//    
//    public short getORIGIONALDBCRidx()
//    {
//        return ORIGIONALDBCRidx;
//    }
    
    private String originalDbCr="";
        
    public String getORIGIONALDBCR()
    {
        return this.originalDbCr;
    }

    public void setORIGIONALDBCR(String originalDbCr)
    {
        this.originalDbCr=originalDbCr;
    }

    private String referenceInvNum="";
    public String getREFERENCEINVOICENUMBER()
    {
        return this.referenceInvNum;
    }
    
    public void setREFERENCEINVOICENUMBER(String referenceInvNum)
    {
       this.referenceInvNum=referenceInvNum;
    }

//    public short getREFERENCEINVOICENUMBERidx()
//    {
//        return REFERENCEINVOICENUMBERidx;
//    }
    
    private String referenceInvDate="";
    
    public String getREFERENCEINVOICEDATE()
    {
        return this.referenceInvDate;
    }
    
    public void setREFERENCEINVOICEDATE(String referenceInvDate)
    {
       this.referenceInvDate=referenceInvDate;
    }
    
  
	

    

//    public short getREFERENCEINVOICEDATEidx()
//    {
//        return REFERENCEINVOICEDATEidx;
//    }    
//
//    public short getREFERENCEINVOICEVATAMOUNTidx()
//    {
//        return REFERENCEINVOICEVATAMOUNTidx;
//    }


	private String referenceInvVatAmt="";
    public String getREFERENCEINVOICEVATAMOUNT()
    {
        return this.referenceInvVatAmt;
    }

    public void setREFERENCEINVOICEVATAMOUNT(String referenceInvVatAmt)
    {
       this.referenceInvVatAmt=referenceInvVatAmt;
    }

    public void setREFERENCEINVOICEVATAMOUNTasRegionalBigDecimal(RegionalBigDecimal referenceInvVatAmt)
    {
        setREFERENCEINVOICEVATAMOUNT(referenceInvVatAmt.setScale(RegionalBigDecimal.getDefaultScale(), BigDecimal.ROUND_HALF_UP).toString());
    }

    public void setREFERENCEINVOICEVATAMOUNTasBigDecimal(BigDecimal referenceInvVatAmt)
    {
        setREFERENCEINVOICEVATAMOUNTasRegionalBigDecimal(new RegionalBigDecimal(referenceInvVatAmt.toString(), RegionalBigDecimal.PERIODDECIMALSEPARATOR));
    }

//    public BigDecimal getREFERENCEINVOICEVATAMOUNTasBigDecimal()
//    {
////        return getDecimal(REFERENCEINVOICEVATAMOUNTidx).toBigDecimal();
//    }
//
//    public short getREFERENCEINVOICENETAMOUNTidx()
//    {
//        return REFERENCEINVOICENETAMOUNTidx;
//    }

    private String referenceInvNetAmt="";
    public String getREFERENCEINVOICENETAMOUNT()
    {
        return this.referenceInvNetAmt;
    }

    public void setREFERENCEINVOICENETAMOUNT(String referenceInvNetAmt)
    {
        this.referenceInvNetAmt=referenceInvNetAmt;
    }

    
//    public void setREFERENCEINVOICENETAMOUNTasRegionalBigDecimal(RegionalBigDecimal value)
//    {
//        setREFERENCEINVOICENETAMOUNT(value.setScale(RegionalBigDecimal.getDefaultScale(), BigDecimal.ROUND_HALF_UP).toString());
//    }
//
//    public void setREFERENCEINVOICENETAMOUNTasBigDecimal(BigDecimal value)
//    {
//        setREFERENCEINVOICENETAMOUNTasRegionalBigDecimal(new RegionalBigDecimal(value.toString(), RegionalBigDecimal.PERIODDECIMALSEPARATOR));
//    }
//
//    public short getCOAWITHINVOICEINDICATORidx()
//    {
//        return COAWITHINVOICEINDICATORidx;
//    }

    private String coaWithInvIndc="N";
    public String getCOAWITHINVOICEINDICATOR()
    {
        return this.coaWithInvIndc;
    }

    public void setCOAWITHINVOICEINDICATOR(String coaWithInvIndc)
    {
        this.coaWithInvIndc=coaWithInvIndc;
    }
//    public short getCONFIGUREDINVOICEINDCidx()
//    {
//        return CONFIGUREDINVOICEINDCidx;
//    }

    public Boolean getCOAWITHINVOICEINDICATORasBoolean()
    {
        if (getCOAWITHINVOICEINDICATOR().equals("Y"))
            return Boolean.TRUE;
        else
            return Boolean.FALSE;

    }

    public void setCOAWITHINVOICEINDICATORasBoolean(Boolean value)
    {
        if (value.equals(Boolean.TRUE))
            setCOAWITHINVOICEINDICATOR( "Y");
        else
            setCOAWITHINVOICEINDICATOR( "N");
    }
    
    private String configuredInvIndc="N";
    
    public String getCONFIGUREDINVOICEINDC()
    {
        return this.configuredInvIndc;
    }

    public void setCONFIGUREDINVOICEINDC(String configuredInvIndc)
    {
        this.configuredInvIndc=configuredInvIndc;
    }    

    public boolean isConfiguredInvoice(){
    	if(getCONFIGUREDINVOICEINDC().equals("Y")){
    		return true;
    	}
    	return false;
    }
//
//    public short getCURRENCYidx()
//    {
//        return CURRENCYidx;
//    }

    private String currency="";
    public String getCURRENCY()
    {
        return this.currency;
    }
//    
//    private String validState="N";
//    
//	public String getValidState() {
//		return validState;
//	}
//	public void setValidState(String validState) {
//		this.validState = validState;
//	}
	public void setTOLERANCEAMT(String toleranceAmt){
		this.toleranceAmt=toleranceAmt;
	}
	
    private String toleranceAmt="";
    
	public String getTOLERANCEAMT(){
		return this.toleranceAmt;
	}    

    public void setCURRENCY(String currency)
    {
        this.currency=currency;
    }
    
    private String exchangeRate="";
   
    public String getEXCHANGERATE()
    {
        return this.exchangeRate;
    }

    public void setEXCHANGERATE(String exchangeRate)
    {
    	this.exchangeRate=exchangeRate;
    }

    public BigDecimal getEXCHANGERATEasBigDecimal()
    {
        return getDecimal(this.exchangeRate).toBigDecimal();
    }

    public void setEXCHANGERATEasRegionalBigDecimal(RegionalBigDecimal value)
    {
    	// text field is 9, so most decimals can be 0.1234567
        setEXCHANGERATE(value.toString());
    }

    public void setEXCHANGERATEasBigDecimal(BigDecimal value)
    {
        setEXCHANGERATEasRegionalBigDecimal(new RegionalBigDecimal(value.toString(), RegionalBigDecimal.PERIODDECIMALSEPARATOR));
    }

    private String exceptionIndc="N";
    
    public String getEXCEPTIONSINDICATOR()
    {
        return this.exceptionIndc;
    }

    public void setEXCEPTIONSINDICATOR(String exceptionIndc)
    {
        this.exceptionIndc=exceptionIndc;
    }

    public Boolean getEXCEPTIONSINDICATORasBoolean()
    {
        return new Boolean(getEXCEPTIONSINDICATOR().equals("Y"));
    }

    public void setEXCEPTIONSINDICATORasBoolean(Boolean exceptionIndc)
    {
        if (exceptionIndc.equals(Boolean.TRUE))
            setEXCEPTIONSINDICATOR("Y");
        else
        	 setEXCEPTIONSINDICATOR("N");
    }

    private String poexCode="";

    public String getPOEXCODE()
    {
        return this.poexCode;
    }

    public void setPOEXCODE(String poexCode)
    {
        this.poexCode=poexCode;
    }
    
    private String vendorName="";

    public String getVENDORNAME()
    {
        return this.vendorName;
    }

    public void setVENDORNAME(String vendorName)
    {
        this.vendorName=vendorName;
    }

    private String vendorNum="";

    public String getVENDORNUMBER()
    {
        return this.vendorNum;
    }

    public void setVENDORNUMBER(String vendorNum)
    {
        this.vendorNum=vendorNum;
    }

    private String srNumber="";

    public String getSRNUMBER()
    {
        return this.srNumber;
    }

    public void setSRNUMBER(String srNumber)
    {
        this.srNumber=srNumber;
    }
    
    private String blockIndc="";
    public void setBLOCKINDC(String blockIndc)
    {
    	this.blockIndc=blockIndc;
    }
    
    public String getBLOCKINDC(){
    	return this.blockIndc;
    }
   
    private String srEndDate="";
    public String getSRENDDATE()
    {
        return this.srEndDate;
    }

    public void setSRENDDATE(String srEndDate)
    {
        this.srEndDate=srEndDate;
    }

    private String srStartDate="";

    public String getSRSTARTDATE()
    {
        return this.srStartDate;
    }

    public void setSRSTARTDATE(String srStartDate)
    {
        this.srStartDate=srStartDate;
    }

    private String vendorCommission="";

    public String getVENDORCOMMISSION()
    {
        return this.vendorCommission;
    }

    public void setVENDORCOMMISSION(String vendorCommission)
    {
        this.vendorCommission=vendorCommission;
    }
    
    
    private String vendorAdd1="";
    public String getVENDORADDRESS1()
    {
        return this.vendorAdd1;
    }

    public void setVENDORADDRESS1(String vendorAdd1)
    {
       this.vendorAdd1=vendorAdd1;
    }

 
    private String vendorAdd2="";
    
    public String getVENDORADDRESS2()
    {
        return this.vendorAdd2;
    }

    public void setVENDORADDRESS2(String vendorAdd2)
    {
        this.vendorAdd2=vendorAdd2;
    }
    
    private String vendorAdd3="";

    public String getVENDORADDRESS3()
    {
        return this.vendorAdd3;
    }

    public void setVENDORADDRESS3(String vendorAdd3)
    {
       this.vendorAdd3=vendorAdd3;
    }

    private String offeringLetterNum="";

    public String getOFFERINGLETTERNUMBER()
    {
        return this.offeringLetterNum;
    }

    public void setOFFERINGLETTERNUMBER(String offeringLetterNum)
    {
        this.offeringLetterNum=offeringLetterNum;
    }

    private String oLCurrency="";

    public String getOFFERINGLETTERCURRENCY()
    {
        return this.oLCurrency;
    }

    public void setOFFERINGLETTERCURRENCY(String oLCurrency)
    {
        this.oLCurrency=oLCurrency;
    }
    
 // story 1497875 [start] 
 
    private String billEntityIndc="";

    public String getBILLENTITYINDICATOR()
    {
        return this.billEntityIndc;
    }

    public void setBILLENTITYINDICATOR(String billEntityIndc)
    {
        this.billEntityIndc=billEntityIndc;
    }
    
   // story 1497875 [end] 

    private String customerNum="";
   

    public String getCUSTOMERNUMBER()
    {
        return this.customerNum;
    }

    public void setCUSTOMERNUMBER(String customerNum)
    {
        this.customerNum=customerNum;
    }

    private String  oLCustomerNum="";

    public String getOFFERINGLETTERCUSTOMERNUMBER()
    {
        return this.oLCustomerNum;
    }

    public void setOFFERINGLETTERCUSTOMERNUMBER(String oLCustomerNum)
    {
        this.oLCustomerNum=oLCustomerNum;
    }


    private String installedCustomerNum="";
    
    public String getINSTALLEDCUSTOMERNUMBER()
    {
        return this.installedCustomerNum;
    }

    public void setINSTALLEDCUSTOMERNUMBER(String installedCustomerNum)
    {
        this.installedCustomerNum=installedCustomerNum;
    }

    private String customerName="";

    public String getCUSTOMERNAME()
    {
        return this.customerName;
    }

    public void setCUSTOMERNAME(String customerName)
    {
        this.customerName=customerName;
    }

    private String oLCustomerName="";

    public String getOFFERINGLETTERCUSTOMERNAME()
    {
        return this.oLCustomerName;
    }

    public void setOFFERINGLETTERCUSTOMERNAME(String oLCustomerName)
    {
        this.oLCustomerName=oLCustomerName;
    }

    private String installedCustomerName="";

    public String getINSTALLEDCUSTOMERNAME()
    {
        return this.installedCustomerName;
    }

    public void setINSTALLEDCUSTOMERNAME(String installedCustomerName)
    {
        this.installedCustomerName=installedCustomerName;
    }

    
   public boolean isInvDialfieldsEditable() {
		return invDialfieldsEditable;
	}
	public void setInvDialfieldsEditable(boolean invDialfieldsEditable) {
		this.invDialfieldsEditable = invDialfieldsEditable;
	}



private String responsiblePartyId="";

    public String getRESPONSIBLEPARTYID()
    {
        return this.responsiblePartyId;
    }

    public void setRESPONSIBLEPARTYID(String responsiblePartyId)
    {
        this.responsiblePartyId=responsiblePartyId;
    }

    private String invoiceStatus="";

    public String getINVOICESTATUS()
    {
        return this.invoiceStatus;
    }

    public void setINVOICESTATUS(String invoiceStatus)
    {
        this.invoiceStatus=invoiceStatus;
    }

    private String rofInvoiceSrc="";

    public String getROFINVOICESOURCE()
    {
        return this.rofInvoiceSrc;
    }

    public void setROFINVOICESOURCE(String rofInvoiceSrc)
    {
        this.rofInvoiceSrc=rofInvoiceSrc;
    }

    private String autocreateCoaIndc="";
    
    public String getAUTOCREATECOAINDC()
    {
        return this.autocreateCoaIndc;
    }

    public void setAUTOCREATECOAINDC(String autocreateCoaIndc)
    {
        this.autocreateCoaIndc=autocreateCoaIndc;
    }

    private String rofInvoiceIndc="";

    public String getROFINVOICEINDC()
    {
        return this.rofInvoiceIndc;
    }

    public boolean isROFInvoice()
    {
        return getROFINVOICEINDC().equals("Y");
    }

    public void setROFINVOICEINDC(String rofInvoiceIndc)
    {
        this.rofInvoiceIndc=rofInvoiceIndc;
    }

    private String customerAdd1="";

    public String getCUSTOMERADDRESS1()
    {
        return this.customerAdd1;
    }

    public void setCUSTOMERADDRESS1(String customerAdd1)
    {
        this.customerAdd1=customerAdd1;
    }

    private String customerAdd2="";

    public String getCUSTOMERADDRESS2()
    {
        return this.customerAdd2;
    }
    
    public void setCUSTOMERADDRESS2(String customerAdd2)
    {
        this.customerAdd2=customerAdd2;
    }

    private String customerAdd3="";

    public String getCUSTOMERADDRESS3()
    {
        return this.customerAdd3;
    }

    public void setCUSTOMERADDRESS3(String customerAdd3)
    {
        this.customerAdd3=customerAdd3;
    }
    
    private String customerAdd4="";
    
    public String getCUSTOMERADDRESS4()
    {
        return this.customerAdd4;
    }

    public void setCUSTOMERADDRESS4(String customerAdd4)
    {
        this.customerAdd4=customerAdd4;
    }

    private String customerAdd5="";
    
    public String getCUSTOMERADDRESS5()
    {
        return this.customerAdd5;
    }

    public void setCUSTOMERADDRESS5(String customerAdd5)
    {
        this.customerAdd5=customerAdd5;
    }

    private String customerAdd6="";

    public String getCUSTOMERADDRESS6()
    {
        return this.customerAdd6;
    }

    public void setCUSTOMERADDRESS6(String customerAdd6)
    {
        this.customerAdd6=customerAdd6;
    }

    private String installedCustomerAdd1="";

    public String getINSTALLEDCUSTOMERADDRESS1()
    {
        return this.installedCustomerAdd1;
    }

    public void setINSTALLEDCUSTOMERADDRESS1(String installedCustomerAdd1)
    {
        this.installedCustomerAdd1=installedCustomerAdd1;
    }

    private String installedCustomerAdd2="";

    public String getINSTALLEDCUSTOMERADDRESS2()
    {
        return this.installedCustomerAdd2;
    }

    public void setINSTALLEDCUSTOMERADDRESS2(String installedCustomerAdd2)
    {
        this.installedCustomerAdd2=installedCustomerAdd2;
    }

    private String installedCustomerAdd3="";

    public String getINSTALLEDCUSTOMERADDRESS3()
    {
        return this.installedCustomerAdd3;
    }

    public void setINSTALLEDCUSTOMERADDRESS3(String installedCustomerAdd3)
    {
        this.installedCustomerAdd3=installedCustomerAdd3;
    }

    private String installedCustomerAdd4="";

    public String getINSTALLEDCUSTOMERADDRESS4()
    {
        return this.installedCustomerAdd4;
    }

    public void setINSTALLEDCUSTOMERADDRESS4(String installedCustomerAdd4)
    {
        this.installedCustomerAdd4=installedCustomerAdd4;
    }

    private String installedCustomerAdd5="";

    public String getINSTALLEDCUSTOMERADDRESS5()
    {
        return this.installedCustomerAdd5;
    }

    public void setINSTALLEDCUSTOMERADDRESS5(String installedCustomerAdd5)
    {
        this.installedCustomerAdd5=installedCustomerAdd5;
    }

    private String installedCustomerAdd6="";

    public String getINSTALLEDCUSTOMERADDRESS6()
    {
        return this.installedCustomerAdd6;
    }

    public void setINSTALLEDCUSTOMERADDRESS6(String installedCustomerAdd6)
    {
        this.installedCustomerAdd6=installedCustomerAdd6;
    }

    
    private String companyCode="";

    public String getCOMPANYCODE()
    {
        return this.companyCode;
    }

    public void setCOMPANYCODE(String companyCode)
    {
        this.companyCode=companyCode;
    }

    private String urgentIndc="";
    
    public String getURGENTINDICATOR()
    {
        return this.urgentIndc;
    }

    public void setURGENTINDICATOR(String urgentIndc)
    {
        this.urgentIndc=urgentIndc;
    }
    
  
    private String taxesIndc="";
    
    public String getTAXESINDICATOR()
    {
        return this.taxesIndc;
    }

    public void setTAXESINDICATOR(String taxesIndc)
    {
        this.taxesIndc=taxesIndc;
    }    

    public Boolean getURGENTINDICATORasBoolean()
    {
        if (getURGENTINDICATOR().equals("Y"))
            return Boolean.TRUE;
        else
            return Boolean.FALSE;

    }

    public void setURGENTINDICATORasBoolean(Boolean value)
    {
        if (value.equals(Boolean.TRUE))
            setURGENTINDICATOR("Y");
        else
        	setURGENTINDICATOR("N");
    }
    
    private String rofOLIndc="";

     public String getROFOFFERLETTERINDICATOR()
    {
        return this.rofOLIndc;
    }

    public void setROFOFFERLETTERINDICATOR(String rofOLIndc)
    {
        this.rofOLIndc=rofOLIndc;
    }

  
    public Boolean getROFOFFERLETTERINDICATORasBoolean()
    {
        if (getROFOFFERLETTERINDICATOR().equals("Y"))
            return Boolean.TRUE;
        else
            return Boolean.FALSE;

    }

    public void setROFOFFERLETTERINDICATORasBoolean(Boolean value)
    {
        if (value.equals(Boolean.TRUE))
            setROFOFFERLETTERINDICATOR( "Y");
        else
        	setROFOFFERLETTERINDICATOR( "N");
    }

    /*
     * maintains the list of detail records
     */
    public void setLineItems(ArrayList<LineItemELS> lineItems)
    {
        this.lineItems = lineItems;
    }

    public ArrayList<LineItemELS> getLineItems()
    {
        return lineItems;
    }
    
    /*
     * maintains the list of supplier numbers
     */
    public void setSupplierNums(ArrayList supplierNums){
    	this.supplierNums = supplierNums;
    }
    
    public ArrayList getSupplierNums(){
    	return supplierNums;
    }
    /*
     * maintains the list of comment records
     */
    public void setComments(ArrayList comments)
    {
        this.comments = comments;
    }

    public ArrayList getComments()
    {
        return comments;
    }
    
    public void setErrorCodes(ArrayList<String> errorCodes){
    	this.errorCodes = errorCodes;
    }
    
    public ArrayList<String> getErrorCodes(){
    	return errorCodes;
    }
    
    
    
    public boolean isFromAPTS()
    {
        if (getINVOICESTATUS().trim().length() == 0)
        {
            return true;
        }
        return false;
    }

    public boolean isFromGCPS()
    {
        return !isFromAPTS();
    }

    /**
     * @return booelan - is this a debit invoice
     */
    public boolean isDBInvoice()
    {
        
        return getDBCR().equals("DB");
    }

    public boolean isCRInvoice()
    {
        return !isDBInvoice();
    }
    
    public boolean isCOMInvoice(){
        if (getINVOICETYPE().trim().equals("COM"))
            return true;   
        
        return false;
    }
    
    public boolean isEXEMPTorStartswithDEVofSRNumber(){
    	if(getSRNUMBER().trim().equals("EXEMPT"))
    		return true;
    	if(getSRNUMBER().startsWith("DEV"))
    		return true;
    	
    	return false;
    }    

    public boolean areLineItemsRequired()
    {
        /*
         * if (isCommissionInvoice() || isPassThruInvoice() ||
         * isRateBuyDownInvoice()) return true;
         * 
         * if (isCRInvoice() && (! doesReferenceInvoiceNumberHasPayments())) {
         * return false; }
         */
        return true;
    }

    public boolean isCommissionOrStampDutyInvoice()
    {
        if (getINVOICETYPE().equals("COM"))
            return true;

        if (getINVOICETYPE().equals("STP"))
            return true;

        return false;

    }
    
    public String getDisplayTaxWarn() {
		return displayTaxWarn;
	}
	public void setDisplayTaxWarn(String displayTaxWarn) {
		this.displayTaxWarn = displayTaxWarn;
	}
	public boolean isCommissionInvoice()
    {
        if (getINVOICETYPE().equals("COM"))
            return true;
    	
        return false;
    }

    public boolean isPassThruInvoice()
    {
        return (getINVOICETYPE().equals("PTR"));
    }

    public boolean isRateBuyDownInvoice()
    {
        return (getINVOICETYPE().equals("RBD"));
    }

    public boolean isIBMInvoice()
    {
    	return getINVOICETYPE().trim().equals("IBM");
    }
    
    public boolean isCompanyCodeOf0001(){
    	
    	return getCOMPANYCODE().trim().equals("0001");
    } 
    
    public boolean isUpfrontState(){
    	return !getSTATECODE().equals("OT");
    }
    //Story 1289894 - GIL - change to US tax call to CTS  for IL
    public boolean isStateIL(){
    	return getSTATECODE().equals("IL");
    }
    
    public void determineROFInvoice()
    {
        // check the item type for rof
        if (getROFINVOICEINDC().equals("Y"))
        {
            setROFINVOICESOURCE("ROF");
            setAUTOCREATECOAINDC("N");
            setROFINVOICEINDC("Y");
            setCOAWITHINVOICEINDICATOR("Y");
        } else
        {
            setROFINVOICESOURCE("GIL");
            setAUTOCREATECOAINDC("N");
            setROFINVOICEINDC("N");
        }
    }
    
    public boolean isUSCountry(){
    	if(getCOUNTRY().equals("US")){
    		return true;
    	}
    	return false;
    }

    /**
     * check to see if the country is ELS or not
     */

	public void loadindexvalues() {
			return;
		}

	public void getIvoiceInfo() {
			return;
		}

	 /***
	  *
	  *   TODO: In the future remove from every item type dialog the loadIndexvalues...
	  */
	 
	 
	 private ArrayList<FormSelect> countryInfoSelectList;
	    
	    private ArrayList<FormSelect> currencyCodeSelectList;
	    
	    private ArrayList<FormSelect> invoiceSuffixesSelectList;
	    
	    private ArrayList<FormSelect> invoiceTypesSelectList;
	    
	    private ArrayList<FormSelect> poexCodesSelectList;
	    
	    private ArrayList<FormSelect> vatCodesSelectList;
	    
	    private ArrayList<FormSelect> dbCrSelectList;
	    
	    private ArrayList<FormSelect> companyCodeSelectList;
	    
	    private ArrayList<FormSelect> distributorCodeSelectList;
	    
		 //Story 1750051 CA GIL changes
	    private ArrayList<FormSelect> provinceCodesSelectList;
	  //End Story 1750051 CA GIL changes
	  
	    
	    //Story 1750051 CA GIL changes
	    public void initializeShiptoProvinceCodes() throws Exception
	    {
	    	
	    	 try
	         {
	    		// load the Province Codes
	    		 boolean hasSelectedValue = (this.getPROVINCECODE()!=null && !"".equals(this.getPROVINCECODE().trim()));
	             String ProvinceCodeDefault = "ON";
	             String ProvinceCode;
	             FormSelect code = null;
	             FormSelect codeDefault = new FormSelect();
	             ArrayList<FormSelect> provinceCodes = new ArrayList<FormSelect>();   
	             codeDefault.setValue(ProvinceCodeDefault);
	             codeDefault.setLabel(ProvinceCodeDefault);
	             ResultSetCache cachedProvinceResults = invoiceDataModelELS.selectShiptoProvinceCodesStatement();
	             while (cachedProvinceResults.next())
	             {
		             ProvinceCode = cachedProvinceResults.getString(1);
		             code = new FormSelect();
		             code.setValue(ProvinceCode);
		             code.setLabel(ProvinceCode);
		            if ( (hasSelectedValue && this.getPROVINCECODE().equalsIgnoreCase(code.getValue())) ||
		            	  (!hasSelectedValue  && codeDefault.getValue().equalsIgnoreCase(code.getValue()))){	
		            	code.setSelected(true);
		            } 
	             	provinceCodes.add(code);
	             }
	             Collections.sort(provinceCodes);
	             this.provinceCodesSelectList = provinceCodes;
             
	         } catch (Exception exc)
	         {
	             logger.fatal("Error loading province codes: " + exc.toString());
	             throw exc;
	         }
	    }
	    
	    //End Story 1750051
	    
	    
	 
	 /*    *//**
	     * load the currency values from the database and populate the gui
	 * @throws Exception 
	     *  
	     */
	    public void initializeCurrencies() throws Exception
	    {
	    	
	    	FormSelect code = null;
	    	ArrayList<FormSelect> currencyCodes = new ArrayList<FormSelect>();
	    	 boolean hasSelectedValue = (this.currency!=null && !"".equals(this.currency));
	        try
	        {

	            // load the currency types
	            String defaultCurrency = "XXX";
	            String currencyCode;
	           
	            
	            ResultSetCache cachedResults = invoiceDataModelELS.selectCurrencyCodesStatement();
	            
	           
	            
	            while (cachedResults.next())
	            {
	            	code = new FormSelect();
	                currencyCode = cachedResults.getString(1);
	                code.setValue(currencyCode);
	                code.setLabel(currencyCode);
	                if(!currencyCodes.contains(currencyCode)){
		                if( hasSelectedValue &&  this.currency.equalsIgnoreCase(currencyCode)	) {
		                	
		                	code.setSelected(true);
		                	
		                } else if (!hasSelectedValue && cachedResults.getString(2).equals("*"))   {
		                			
		                			code.setSelected(true);
		                }
		                
		                currencyCodes.add(code);
	                }
	            }

	           
	        } catch (Exception exc)
	        {
	        	
	        	logger.fatal(exc.toString());
	        	throw exc;

	        }
	        
	        logger.debug(currencyCodes);
	        
	        this.currencyCodeSelectList = currencyCodes;
	    }
	    
	    public void initializeDbCr() throws Exception
	    {
	    	String[] defaultValues = { "DB", "CR" };
	    	FormSelect code = null;
	    	ArrayList<FormSelect> codes = null;
	    	boolean hasSelectedValue = (this.dbCr!=null && !"".equals(this.dbCr));
	    	
	     try
	     {

	         codes = new ArrayList<FormSelect>();
	  
	           for (int i = 0; i< defaultValues.length; i++)
	            {
	            	code = new FormSelect();
	            	code.setValue(defaultValues[i]);
	            	code.setLabel(defaultValues[i]);
	            	
	            	if(hasSelectedValue && defaultValues[i].equalsIgnoreCase(this.dbCr)) {
	            		
	            		code.setSelected(true);
	            		setDBCR(code.getValue());
	            	} else  if (!hasSelectedValue && i==0) {
	            		setDBCR(code.getValue());
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


	    public void  initializeCompanyCode() throws Exception {
			
	    	 
	        ArrayList<FormSelect> codes = new ArrayList<FormSelect>();
	        FormSelect code = null;
	        ArrayList<FormSelect> companyCodesList = null;
	        boolean hasSelectedValue = (this.companyCode!=null && !"".equals(this.companyCode));
	        
			try {
				
				// load the companyCode
				String defaultCompanyCode = "";
				String companyCode;
				companyCodesList = new ArrayList<FormSelect>();

				ResultSetCache cachedResults = invoiceDataModelELS.selectCompanyCodesStatement();
				while (cachedResults.next()) {
					
					companyCode = cachedResults.getString(1);
					if(!companyCodesList.contains(companyCode)){
						code = new FormSelect();
						code.setValue(companyCode);
						code.setLabel(companyCode);
						if (hasSelectedValue &&  companyCode.equalsIgnoreCase(this.companyCode)){
							
							code.setSelected(true);
						}
						else if (!hasSelectedValue && cachedResults.getString(2).equals("*"))
						{
							code.setSelected(true);
						}
						
						companyCodesList.add(code);
						
					}
				}


			} catch (Exception exc) {
				
				logger.fatal(exc.toString());
				throw exc;

			}
			
			this.companyCodeSelectList = companyCodesList;
		}
	    
	    public  void initializeInvoiceTypes() throws Exception
	    {
	    	

	    	LinkedHashSet<FormSelect> codes = new LinkedHashSet<FormSelect>();
	        FormSelect code = null;
	        FormSelect codeDefault = null;
	        int matchCount = 0;
	        ArrayList<FormSelect> resultinvoicetypes = null ;
	        ResultSet defaultResults = null;
//	        boolean hasSelectedValue = (this.invoiceType!=null && !"".equals(this.invoiceType));
	    	
	        
	        
	        
	        try
	        {

	           String invoiceTypestring = "";
	            resultinvoicetypes  = new ArrayList<FormSelect>();
	            String defaultInvoiceType = "IBM";
	            String invoiceType;
	            
	            ResultSetCache cachedResults = invoiceDataModelELS.selectInvoiceTypesStatement();
	            ResultSetCache gcpsResults =invoiceDataModelELS.selectGCPSInvoiceTypesStatement();
	            
	          
	            	
	                
	                while (cachedResults.next())
	                {
	                	invoiceType = cachedResults.getString(1);
	                	if(invoiceType.equals("SWV")){
	                    	if(isUSCountry()){
	                    		code = new FormSelect();
	    	                	code.setValue(invoiceType);
	    	                    code.setLabel(invoiceType);
//	    	                    if(this.invoiceType.equalsIgnoreCase(invoiceType)){
//	    	                    	code.setSelected(true);
//	    	                    }
	    	                    logger.debug("invTypeCodeQ1: "+invoiceType);
	    	                    codes.add(code);
	                    	}
	                    }else{
	                    	code = new FormSelect();
		                	code.setValue(invoiceType);
		                    code.setLabel(invoiceType);
//		                    if(this.invoiceType.equalsIgnoreCase(invoiceType)){
//		                    	code.setSelected(true);
//		                    }
		                    logger.debug("invTypeCodeQ1: "+invoiceType);
		                    codes.add(code);
	                    }
	                	
//	                	code = new FormSelect();
//	                	code.setValue(invoiceType);
//	                    code.setLabel(invoiceType);
	                    
	                    
	                  
	                }
	                
	                while (gcpsResults.next())
	                {
	                	invoiceType =gcpsResults.getString(1);
	                	if(invoiceType.equals("SWV")){
	                    	if(isUSCountry()){
	                    		code = new FormSelect();
	    	                	code.setValue(invoiceType);
	    	                    code.setLabel(invoiceType);
//	    	                    if(this.invoiceType.equalsIgnoreCase(invoiceType)){
//	    	                    	code.setSelected(true);
//	    	                    }
	    	                    logger.debug("invTypeCodeQ2: "+invoiceType);
	    	                    codes.add(code);
	                    	}
	                    }else{
	                    	code = new FormSelect();
		                	code.setValue(invoiceType);
		                    code.setLabel(invoiceType);
//		                    if(this.invoiceType.equalsIgnoreCase(invoiceType)){
//		                    	code.setSelected(true);
//		                    }
		                    logger.debug("invTypeCodeQ2: "+invoiceType);
		                    codes.add(code);
	                    }
	                	
//	                	code = new FormSelect();
//	                	code.setValue(invoiceType);
//	                    code.setLabel(invoiceType);
	                    
	                    
	                  
	                }
	                
	          
	            
			            defaultResults = invoiceDataModelELS.selectDefaultInvoiceTypesStatement();
			            if (defaultResults.next())
			            {
			                invoiceTypestring = defaultResults.getString(1);
			            	
			            	codeDefault = new FormSelect();
			            	codeDefault.setValue(invoiceTypestring);
			            	codeDefault.setLabel(invoiceTypestring);
			            	codeDefault.setSelected(true);
			            	logger.debug("invTypeCodeQ3: "+invoiceTypestring);
			            	 
			            } else {
			            	codeDefault = new FormSelect();
			            	codeDefault.setValue(defaultInvoiceType);
			            	codeDefault.setLabel(defaultInvoiceType);
			            	codeDefault.setSelected(true);
			            	logger.debug("defInvTypeCode: "+defaultInvoiceType);
			            	 
			            }
			            
			           
			            for (FormSelect codeFor : codes) {
			            	
			            	 if(codeFor.getValue().equalsIgnoreCase(codeDefault.getValue())){
			            	
			            		codeFor.setSelected(true);
			            		resultinvoicetypes.add(codeFor);
			            		matchCount++;
			            		
			            	} else {
			            		
			            		resultinvoicetypes.add(codeFor);
			            		
			            	}
			               
			            }
			            
			            if (matchCount == 0 ) resultinvoicetypes.add(codeDefault);
	            
	          
	            

	        } catch (Exception exc)
	        {
	            logger.fatal(exc.toString());
	            throw exc;
	           
	        
	        }
	        
	        invoiceTypesSelectList = resultinvoicetypes;
	    }
	    public void initializePOEXCodes() throws Exception
	    {
	    	ArrayList<FormSelect> codes = new ArrayList<FormSelect>();
	        FormSelect code = null;
	        boolean hasSelectedValue = (this.poexCode!=null && !"".equals(this.poexCode.trim()));
	        
	        try
	        {
    
	            ResultSetCache cachedResults = invoiceDataModelELS.selectPOEXCodesStatement();
	            while (cachedResults.next())
	            {
	            	code = new PoexCodeFormSelect();
	            	code.setValue(cachedResults.getString(2));
	            	code.setLabel(cachedResults.getString(4));
	            	String defaultCode = cachedResults.getString(3);
	
		            //story 1582060 Changed the default code from B to R
		            if (defaultCode.equals("R") )// || defaultCode.equals("B"))
		            {

		               	code.setSelected(true);
		            /***	
		           	 * 	Story 1582060: GILGUI reading POEX default code
		           	 ****/
		               	this.defaultVENPoex=code.getValue();
		                	
		             }else if (defaultCode.equals("C")){
		                			                
		                this.defaultCOMPoex=code.getValue();
		             }

		            if (hasSelectedValue && this.poexCode.equalsIgnoreCase(code.getValue())){
	            		
	            		code.setSelected(true);
	            		
	            	}
		        	/***	
		        	 * End:	Story 1582060: GILGUI reading POEX default code
		        	 ****/
	                codes.add(code);
	            }

            	
	            

	        } catch (Exception exc)
	        {
	        	logger.fatal(exc.toString());
	        	throw exc;
	        }
	        
	        poexCodesSelectList = codes;
	    }
	    
	    public void initializeVATVariance() throws Exception
	    {

	    	
	        try
	        {
	         

	            // load the vat variance
	            ResultSet varianceResults = invoiceDataModelELS.getVATVarianceStatement();
	            if (varianceResults.next())
	            {
	                RegionalBigDecimal variance = new RegionalBigDecimal(varianceResults.getString(1), RegionalBigDecimal.PERIODDECIMALSEPARATOR);
	                this.setVATVARIANCE(variance.toString());
	            }
	        } catch (Exception exc)
	        {
	            logger.fatal(exc.toString());
	            throw exc;
	           
	        }

	    }
	    
	    public void initializeDistributor() throws Exception
	    {
	    	
	    	FormSelect code = null;
	    	ArrayList<FormSelect> codes = new ArrayList<FormSelect>();
	    	boolean hasSelectedValue = (this.distributorNum!=null && !"".equals(this.distributorNum));
	    	
	        try
	        {

	            ResultSet cachedResults = invoiceDataModelELS.selectDistributorInfoStatement();

	            code = new FormSelect();
	            code.setValue(" ");
	            code.setLabel(" ");
	            codes.add(code);
	            
	            while (cachedResults.next())
	            {
	            	isDistributorListExisting=true;
	               	code = new FormSelect();
	            	code.setValue(cachedResults.getString(2).trim());
	            	code.setLabel(cachedResults.getString(2).trim()+"-"+ cachedResults.getString(3).trim());
	            	
	            	if(hasSelectedValue && code.getValue().equalsIgnoreCase(this.distributorNum)){
	            		
	            		code.setSelected(true);
	            	}
	            	codes.add(code);
	            }
	            
	            distributorCodeSelectList = codes;

	        } catch (Exception exc)
	        {
	           logger.fatal(exc.toString());
	           throw exc;
	        }
	        
	    }
	    
	    public void initializeELSCountry()
	    {
	        try
	        {
	            

	 
	            ResultSet results = invoiceDataModelELS.selectELSCountry();
	            if(results.next())
	            {
	            	this.setELSINDC("Y");
	            }else{
	            	this.setELSINDC("N");
	            }


	        } catch (Exception exc)
	        {
	            logger.fatal(exc.toString());
	            logger.error("Error initializing ELS Country");
	        }
	    } 
}
