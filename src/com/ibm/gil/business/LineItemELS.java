package com.ibm.gil.business;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;


import com.ibm.gil.model.LineItemDataModelELS;
import com.ibm.gil.util.GilUtility;
import com.ibm.gil.util.RegionalBigDecimal;
import com.ibm.igf.hmvc.ResultSetCache;

public class LineItemELS extends Indexing implements Comparable {
	private List<String> dialogErrors=new ArrayList();
	private List<String> dialogWarns=new ArrayList();
	
	
	public List<String> getDialogWarns() {
		return dialogWarns;
	}
	/*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object) sort by line number ,
     *      sub line number
     */
    public int compareTo(Object arg0)
    {
    	//TODO check if it works....
        if (arg0 instanceof LineItemELS)
        {
            LineItemELS aDataModel = (LineItemELS) arg0;
            int result = 0;

            if (result == 0)
                result = GilUtility.getInt(this.getLINENUMBER()) - GilUtility.getInt(aDataModel.getLINENUMBER());

            if (result == 0)
                result = GilUtility.getInt(this.getSUBLINENUMBER()) - GilUtility.getInt(aDataModel.getSUBLINENUMBER());

            return result;
        } else
        {
            return 0;
        }
    }

	 /*
     * supporting table caches
     */
    private static transient ResultSetCache VATCodesResults = null;
    private static transient ResultSetCache OtherChargesResults = null;
    private static transient Hashtable TypeModelResults = new Hashtable();

    /*
     * misc local vars
     */
    private  ArrayList<FormSelect> vatCodes=new ArrayList<FormSelect>();
    private  ArrayList<FormSelect> otherCharges=new ArrayList<FormSelect>();
    
    public  ArrayList<FormSelect> getVatCodes() {
		return vatCodes;
	}
	public  void setVatCodes(ArrayList<FormSelect> vatCodes) {
		this.vatCodes = vatCodes;
	}
	public  ArrayList<FormSelect> getOtherCharges() {
		return otherCharges;
	}
	public  void setOtherCharges(ArrayList<FormSelect> otherCharges) {
		this.otherCharges = otherCharges;
	}

	//    private static transient ComboItemDescription defaultVATCode;
    private static Hashtable<String,com.ibm.gil.util.RegionalBigDecimal> vatPercentages = null;
//    private static Hashtable otherCharges;
    public static final transient RegionalBigDecimal ONEHUNDRED = new RegionalBigDecimal(100.0);
    private LineItemDataModelELS lineItemDataModel;
    
    public LineItemELS(String country){
    	setCOUNTRY(country);
    	lineItemDataModel=new LineItemDataModelELS(this);
    	
    }
    public void copyLineItem(LineItemELS guiLineItem){
    
    	
    	this.LINENUMBER=guiLineItem.getLINENUMBER();
    	this.SUBLINENUMBER=guiLineItem.getSUBLINENUMBER();
    	this.QUANTITY=guiLineItem.getQUANTITY();
    	this.CONFIGUSED=guiLineItem.getCONFIGUSED();
    	this.FROMQUOTE=guiLineItem.getFROMQUOTE();
    	this.BLANKTYPEMODEL=guiLineItem.getBLANKTYPEMODEL();
    	this.BILLEDTOIGFINDC=guiLineItem.getBILLEDTOIGFINDC();
    	this.UNITPRICE=guiLineItem.getUNITPRICE();
    	this.VATAMOUNT=guiLineItem.getVATAMOUNT();
    	this.EXTENDEDPRICE=guiLineItem.getEXTENDEDPRICE();
    	this.TOTALVATAMOUNT=guiLineItem.getTOTALVATAMOUNT();
    	this.COSTCENTER=guiLineItem.getCOSTCENTER();
    	this.DESCRIPTION=guiLineItem.getDESCRIPTION();
    	this.EQUIPSOURCE=guiLineItem.getEQUIPSOURCE();
    	this.EXTENSIONIND=guiLineItem.getEXTENSIONINDC();
    	this.FINANCINGTYPE=guiLineItem.getFINANCINGTYPE();
    	this.INVOICEITEMTYPE=guiLineItem.getINVOICEITEMTYPE();
    	this.MAXUNITPRICE=guiLineItem.getMAXUNITPRICE();
    	this.MESINDICATOR=guiLineItem.getMESINDICATOR();
    	this.MESNUMBER=guiLineItem.getMESNUMBER();
    	this.MINUNITPRICE=guiLineItem.getMINUNITPRICE();
    	this.MODEL=guiLineItem.getMODEL();
    	this.OTHERCHARGE=guiLineItem.getOTHERCHARGE();
    	this.PARTNUMBER=guiLineItem.getPARTNUMBER();
    	this.PONUMBER=guiLineItem.getPONUMBER();
    	this.PRODCAT=guiLineItem.getPRODCAT();
    	this.SERIAL=guiLineItem.getSERIAL();
    	this.TAXESINDICATOR=guiLineItem.getTAXESINDICATOR();
    	this.TERM=guiLineItem.getTERM();
    	this.TRANSACTIONTYPE=guiLineItem.getTRANSACTIONTYPE();
    	this.TYPE=guiLineItem.getTYPE();
    	this.USTAXPERCENT=guiLineItem.getUSTAXPERCENT();
    	this.VATCODE=guiLineItem.getVATCODE();
   
    }
    public LineItemDataModelELS getLineItemDataModelELS() {
    	if(lineItemDataModel==null){
    		this.lineItemDataModel= new LineItemDataModelELS(this);
    	}
		return lineItemDataModel;
	}
//    public LineItemDataModelELS getLineItemDataModel() {
//		return lineItemDataModel;
//	}

	public void setLineItemDataModelELS(LineItemDataModelELS lineItemDataModel) {
		this.lineItemDataModel = lineItemDataModel;
	}

	String INVOICEITEMTYPE="";
    public void setINVOICEITEMTYPE(String INVOICEITEMTYPE)
    {
        this.INVOICEITEMTYPE=INVOICEITEMTYPE;
    }

    public String getINVOICEITEMTYPE()
    {
        return this.INVOICEITEMTYPE;
    }
    private String gridIndex="";
    
    public String getGridIndex() {
		return gridIndex;
	}
	public void setGridIndex(String gridIndex) {
		this.gridIndex = gridIndex;
	}

	private String TYPE="";
    private String OTHERCHARGE="";
    public void setTYPE(String TYPE)
    {
        this.TYPE=TYPE;
        this.OTHERCHARGE=getTYPE() + "/" + getMODEL();
    }

    

    public String getTYPE()
    {
        return this.TYPE;
    }

    private String MODEL="";
    public void setMODEL(String MODEL)
    {
        this.MODEL=MODEL;
        this.OTHERCHARGE=getTYPE() + "/" + getMODEL();
    }


    public String getMODEL()
    {
        return this.MODEL;
    }

    private String PARTNUMBER="";
    public void setPARTNUMBER(String PARTNUMBER)
    {
       this.PARTNUMBER=PARTNUMBER;
    }



    public String getPARTNUMBER()
    {
        return this.PARTNUMBER;
    }

    public void setOTHERCHARGE(String OTHERCHARGE)
    {
        this.OTHERCHARGE=OTHERCHARGE;
    }


    public String getOTHERCHARGE()
    {
        return this.OTHERCHARGE;
    }

    private String LINENUMBER="";
    public void setLINENUMBER(String LINENUMBER)
    {
        this.LINENUMBER=LINENUMBER;
    }

    
    public String getLINENUMBER()
    {
        return this.LINENUMBER;
    }

    public Integer getLINENUMBERasInteger()
    {
        try
        {
            return (new Integer(getLINENUMBER()));
        } catch (Exception e)
        {
            return null;
        }
    }

    public void setLINENUMBERasInteger(Integer LINENUMBER)
    {
        this.LINENUMBER= LINENUMBER.toString();
    }
    
    private String SUBLINENUMBER="";
    public void setSUBLINENUMBER(String SUBLINENUMBER)
    {
        this.SUBLINENUMBER=SUBLINENUMBER;
    }

    

    public String getSUBLINENUMBER()
    {
        return this.SUBLINENUMBER;
    }

    public Integer getSUBLINENUMBERasInteger()
    {
        try
        {
            return (new Integer(getSUBLINENUMBER()));
        } catch (Exception e)
        {
            return null;
        }
    }

    public void setSUBLINENUMBERasInteger(Integer SUBLINENUMBER)
    {
       this.SUBLINENUMBER= SUBLINENUMBER.toString();
    }
    
    private String QUANTITY="";
    public void setQUANTITY(String QUANTITY)
    {
        this.QUANTITY=QUANTITY;
    }

    public String getQUANTITY()
    {
        return this.QUANTITY;
    }

    public Integer getQUANTITYasInteger()
    {
        try
        {
            return (new Integer(getQUANTITY()));
        } catch (Exception e)
        {
            return null;
        }
    }

    public void setQUANTITYasInteger(Integer QUANTITY)
    {
    	this.QUANTITY=QUANTITY.toString();
    }
    private String SERIAL="";
    public void setSERIAL(String SERIAL)
    {
        this.SERIAL=SERIAL;
    }

   
    public String getSERIAL()
    {
        return this.SERIAL;
    }
    private String UNITPRICE="";
    public void setUNITPRICE(String UNITPRICE)
    {
        this.UNITPRICE=UNITPRICE;
    }

  

    public String getUNITPRICE()
    {
        return this.UNITPRICE;
    }

    public void setUNITPRICEasBigDecimal(BigDecimal value)
    {
        setUNITPRICEasRegionalBigDecimal(new RegionalBigDecimal(value.toString(), RegionalBigDecimal.PERIODDECIMALSEPARATOR));
    }

    public void setUNITPRICEasRegionalBigDecimal(RegionalBigDecimal UNITPRICE)
    {
        setUNITPRICE(UNITPRICE.setScale(RegionalBigDecimal.getDefaultScale(), BigDecimal.ROUND_HALF_UP).toString());
    }

    public BigDecimal getUNITPRICEasBigDecimal()
    {
        return getDecimal(this.UNITPRICE).toBigDecimal();
    }

    public void setUNITPRICEasDouble(Double UNITPRICE)
    {
        setUNITPRICEasRegionalBigDecimal(new RegionalBigDecimal(UNITPRICE.doubleValue()));
    }

//    public Double getUNITPRICEasDouble()
//    {
//        return getDecimal(UNITPRICEidx).toDouble();
//    }
    private String MESNUMBER="";
    public void setMESNUMBER(String MESNUMBER)
    {
        this.MESNUMBER=MESNUMBER;
    }

    public String getMESNUMBER()
    {
        return this.MESNUMBER;
    }
    private String VATCODE="";
    public void setVATCODE(String VATCODE)
    {
        this.VATCODE=VATCODE;
    }

    public String getVATCODE()
    {
        return this.VATCODE;
    }
    private String defaultVatCode="";
    
    public String getDefaultVatCode() {
		return defaultVatCode;
	}
	public void setDefaultVatCode(String defaultVatCode) {
		this.defaultVatCode = defaultVatCode;
	}

	private String TOTALVATAMOUNT="";
    public void setTOTALVATAMOUNT(String TOTALVATAMOUNT)
    {
        this.TOTALVATAMOUNT=TOTALVATAMOUNT;
    }

    public String getTOTALVATAMOUNT()
    {
        return this.TOTALVATAMOUNT;
    }
    private String VATAMOUNT="";
    public void setVATAMOUNT(String VATAMOUNT)
    {
       this.VATAMOUNT=VATAMOUNT;
    }

    public String getVATAMOUNT()
    {
        return this.VATAMOUNT;
    }

    public void setVATAMOUNTasBigDecimal(BigDecimal VATAMOUNT)
    {
        setVATAMOUNT(new RegionalBigDecimal(VATAMOUNT.toString(), RegionalBigDecimal.PERIODDECIMALSEPARATOR).toString());
    }

    public BigDecimal getVATAMOUNTasBigDecimal()
    {
        return getDecimal(this.VATAMOUNT).toBigDecimal();
    }
    private String EXTENDEDPRICE="";
    public void setEXTENDEDPRICE(String EXTENDEDPRICE)
    {
       this.EXTENDEDPRICE=EXTENDEDPRICE;
    }


    public String getEXTENDEDPRICE()
    {
        return this.EXTENDEDPRICE;
    }

    public void setEXTENDEDPRICEasBigDecimal(BigDecimal EXTENDEDPRICE)
    {
        setEXTENDEDPRICE(new RegionalBigDecimal(EXTENDEDPRICE.toString(), RegionalBigDecimal.PERIODDECIMALSEPARATOR).toString());
    }

//    public BigDecimal getEXTENDEDPRICEasBigDecimal()
//    {
//        return getDecimal(EXTENDEDPRICE).toBigDecimal();
//    }

    public void setPONUMBER(String PONUMBER)
    {
        this.PONUMBER=PONUMBER;
    }

    private String PONUMBER="";
    public String getPONUMBER()
    {
        return this.PONUMBER;
    }

    private String COSTCENTER="";
    public void setCOSTCENTER(String COSTCENTER)
    {
        this.COSTCENTER=COSTCENTER;
    }

    public String getCOSTCENTER()
    {
        return this.COSTCENTER;
    }
    
    private String DESCRIPTION="";
    public void setDESCRIPTION(String DESCRIPTION)
    {
        this.DESCRIPTION=DESCRIPTION;
    }


    public String getDESCRIPTION()
    {
        return this.DESCRIPTION;
    }

    private String CONFIGUSED="";
    public void setCONFIGUSED(String CONFIGUSED)
    {
        this.CONFIGUSED=CONFIGUSED;
    }


    public String getCONFIGUSED()
    {
        return this.CONFIGUSED;
    }

    public void setCONFIGUSEDasBool(Boolean CONFIGUSED)
    {
        this.CONFIGUSED=CONFIGUSED.equals(Boolean.TRUE) ? "Y" : "N";
    }

    public Boolean getCONFIGUSEDasBool()
    {
        if (getCONFIGUSED().equals("Y"))
            return Boolean.TRUE;
        else
            return Boolean.FALSE;
    }
    private String MINUNITPRICE="";
    public void setMINUNITPRICE(String MINUNITPRICE)
    {
        this.MINUNITPRICE=MINUNITPRICE;
    }

    public String getMINUNITPRICE()
    {
        return this.MINUNITPRICE;
    }

    private String MAXUNITPRICE="";
    public void setMAXUNITPRICE(String MAXUNITPRICE)
    {
        this.MAXUNITPRICE=MAXUNITPRICE;
    }

    public String getMAXUNITPRICE()
    {
        return this.MAXUNITPRICE;
    }

    private String MESINDICATOR="";
    public void setMESINDICATOR(String MESINDICATOR)
    {
        this.MESINDICATOR=MESINDICATOR;
    }

    public String getMESINDICATOR()
    {
        return this.MESINDICATOR;
    }

    public Boolean getMESINDICATORasBool()
    {
        if (getMESINDICATOR().equals("Y"))
            return Boolean.TRUE;
        else
            return Boolean.FALSE;

    }

    public void setMESINDICATORasBool(Boolean MESINDICATOR)
    {
        if (MESINDICATOR.equals(Boolean.TRUE))
            this.MESINDICATOR= "Y";
        else
            this.MESINDICATOR= "N";
    }
    private String TERM="";
    public void setTERM(String TERM)
    {
        this.TERM=TERM;
    }

    public void setTERMasInteger(Integer TERM)
    {
        if (TERM.intValue() == 0)
            this.TERM="";
        else
            this.TERM=TERM.toString();
    }

    public String getTERM()
    {
        return this.TERM;
    }
    private String FROMQUOTE="";
    public void setFROMQUOTE(String FROMQUOTE)
    {
        this.FROMQUOTE=FROMQUOTE;
    }

    public String getFROMQUOTE()
    {
        return this.FROMQUOTE;
    }

    public void setEXTENSIONINDC(String FROMQUOTE)
    {
        this.FROMQUOTE=FROMQUOTE;
    }
    private String EXTENSIONIND="";
    public String getEXTENSIONINDC()
    {
        return this.EXTENSIONIND;
    }
    //is there a description
    private boolean tmDescFlag=false;
    
	public boolean isTmDescFlag() {
		return tmDescFlag;
	}
	public void setTmDescFlag(boolean tmDescFlag) {
		this.tmDescFlag = tmDescFlag;
	}

	private String BILLEDTOIGFINDC="N";
    public void setBILLEDTOIGFINDC(String BILLEDTOIGFINDC)
    {
        this.BILLEDTOIGFINDC=BILLEDTOIGFINDC;
    }

    public String getBILLEDTOIGFINDC()
    {
        return this.BILLEDTOIGFINDC;
    }    
    private String TRANSACTIONTYPE="";
    public void setTRANSACTIONTYPE(String TRANSACTIONTYPE)
    {
        this.TRANSACTIONTYPE=TRANSACTIONTYPE;
    }

    public String getTRANSACTIONTYPE()
    {
        return this.TRANSACTIONTYPE;
    }

    private String FINANCINGTYPE="";
    public void setFINANCINGTYPE(String FINANCINGTYPE)
    {
        this.FINANCINGTYPE=FINANCINGTYPE;
    }

    public String getFINANCINGTYPE()
    {
        return this.FINANCINGTYPE;
    }
    private String PRODCAT="";
    
    public void setPRODCAT(String PRODCAT)
    {
      this.PRODCAT=PRODCAT;
    }

    public String getPRODCAT()
    {
        return this.PRODCAT;
    }
    private String PRODTYPE="";
    public void setPRODTYPE(String PRODTYPE)
    {
        this.PRODTYPE=PRODTYPE;
    }

    public String getPRODTYPE()
    {
        return this.PRODTYPE;
    }
    private String EQUIPSOURCE="";
    public void setEQUIPSOURCE(String EQUIPSOURCE)
    {
       this.EQUIPSOURCE=EQUIPSOURCE;
    }

    public String getEQUIPSOURCE()
    {
        return this.EQUIPSOURCE;
    } 
    
    private String USTAXPERCENT="";
    public void setUSTAXPERCENT(String USTAXPERCENT)
    {
      this.USTAXPERCENT=USTAXPERCENT;
    }

    public String getUSTAXPERCENT()
    {
        return this.USTAXPERCENT;
    }    


    /*
     * returns true if VATPercentage is not null
     */
    public boolean isVATPercentageSet()
    {
        if (vatPercentages == null)
        {
            return false;
        }
        return true;
    }

    /*
     * maintains the list of vat codes and the default value
     */
    public void setVATCodes(ArrayList<FormSelect> VATCodes)//, ComboItemDescription defaultItem)
    {
        this.vatCodes = VATCodes;
//        defaultVATCode = defaultItem;
    }

    /*
     * retrieves the list of vat codes as specified in the setup tables @return
     * ArrayList
     */
    public ArrayList getVATCodes()
    {
        return vatCodes;
    }

//    /*
//     * retrieves the default vat code as specified in the setup tables @return
//     * String
//     */
//    public ComboItemDescription getDefaultVATCode()
//    {
//        return defaultVATCode;
//    }

    /*
     * maintains the list of vat code x vat percentages
     */
    public void setVATPercentages(Hashtable percentages)
    {
        vatPercentages = percentages;
    }

    public Hashtable getVATPercentages()
    {
        return vatPercentages;
    }
    
    private String TAXESINDICATOR="";


    public String getTAXESINDICATOR()
    {
        return this.TAXESINDICATOR;
    }

    public void setTAXESINDICATOR(String TAXESINDICATOR)
    {
        this.TAXESINDICATOR=TAXESINDICATOR;
    }      
    /*
     * maintains the list of other charges
     */
//    public void setOtherCharges(Hashtable aOtherChargesList)
//    {
//        otherCharges = aOtherChargesList;
//    }

    /*
     * retrieves the list of vat codes as specified in the setup tables @return
     * ArrayList
     */
//    public Hashtable getOtherChargess()
//    {
//        return otherCharges;
//    }

    public boolean isFromQuote()
    {
        return getFROMQUOTE().equals("Y");
    }

    /*
     * is this a part
     */
    public boolean isPart()
    {
        return getINVOICEITEMTYPE().equals("P");
    }
    
    public boolean is9TX8TAXLine(){
    	return getTYPE().equals("9TX8");
    }
    
    public boolean isUSCountry(){
    	return getCOUNTRY().equals("US");
    }

    
    /*
     * is this a type/model
     */
    public boolean isTypeModel()
    {
        return getINVOICEITEMTYPE().equals("TM");
    }

    /*
     * is this a other charge
     */
    public boolean isOtherCharge()
    {
        return getINVOICEITEMTYPE().equals("OC");
    }

    /*
     * concatenate the type/model with a seperator
     */
    public String getTypeModelKey()
    {
        return (getCOUNTRY() + ":" + getTYPE() + ":" + getMODEL());
    }
    private String BLANKTYPEMODEL="N";
    public void setBLANKTYPEMODEL(String BLANKTYPEMODEL)
    {
        this.BLANKTYPEMODEL=BLANKTYPEMODEL;
    }


    public String getBLANKTYPEMODEL()
    {
        return this.BLANKTYPEMODEL;
    }

	public void LineItemInfo() {
			return;
		}
    
    /**
     * @return
     */
    public boolean isBLEX()
    {

        String transType = getTRANSACTIONTYPE();
        String extensionIndc = getEXTENSIONINDC();

        if ((extensionIndc.equals("1")) || (extensionIndc.equals("2")) || (extensionIndc.equals("3")) || (transType.equals("EXTN")))
        {
            return true;
        }
        return false;
    }
    public void init()
    {
       
        setLINENUMBER("0");
        setSUBLINENUMBER("0");
        setQUANTITY("1");
        setCONFIGUSED("N");
        setFROMQUOTE("N");
        setBLANKTYPEMODEL("N");
        setBILLEDTOIGFINDC("N");
        setUNITPRICE(RegionalBigDecimal.ZERO.setScale(RegionalBigDecimal.getDefaultScale()).toString());
        setVATAMOUNT(RegionalBigDecimal.ZERO.setScale(RegionalBigDecimal.getDefaultScale()).toString());
        setEXTENDEDPRICE(RegionalBigDecimal.ZERO.setScale(RegionalBigDecimal.getDefaultScale()).toString());
        setTOTALVATAMOUNT(RegionalBigDecimal.ZERO.setScale(RegionalBigDecimal.getDefaultScale()).toString());
    }

	public List<String> getDialogErrors() {
		return dialogErrors;
	}

	public void setDialogErrors(List<String> dialogErrors) {
		this.dialogErrors = dialogErrors;
	}
    
    

}
