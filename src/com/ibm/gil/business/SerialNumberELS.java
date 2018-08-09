package com.ibm.gil.business;

import java.io.BufferedReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;



/**
 * 
 * @author ferandra
 *
 */
public class SerialNumberELS extends Indexing  {
	
	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(SerialNumberELS.class);
	
    private String TYPE = "";
    private String MODEL = "";
    private String SERIALNUMBER = "";
    private BigDecimal UNITPRICE = new BigDecimal("0");
    private String TERM;
    private String DESCRIPTION;
    private String PART;  
    private String[] serialStart;
    private char[] charSerialStart = new char[30];
    private String serialEnd;
    private boolean[] serialChange = new boolean[30];
    private boolean includeVowels;
    private int quantity;
    private ArrayList<String> dialogErrorMsgs = new ArrayList<String>();
    private boolean valid= true;
    private boolean validationEndSerialBeforeQtyReached = false;
    private boolean validationNotEnoughDigForSN = false;
    
	public String getTYPE() {
		return TYPE;
	}
	public void setTYPE(String tYPE) {
		TYPE = tYPE;
	}
	public String getMODEL() {
		return MODEL;
	}
	public void setMODEL(String mODEL) {
		MODEL = mODEL;
	}
	public String getSERIALNUMBER() {
		return SERIALNUMBER;
	}
	public void setSERIALNUMBER(String sERIALNUMBER) {
		SERIALNUMBER = sERIALNUMBER;
	}

    
    
	
	public BigDecimal getUNITPRICE() {
		return UNITPRICE;
	}
	public void setUNITPRICE(com.ibm.gil.util.RegionalBigDecimal uNITPRICE) {
		UNITPRICE = uNITPRICE.setScale(com.ibm.gil.util.RegionalBigDecimal.getDefaultScale(), BigDecimal.ROUND_HALF_UP).bigDecimalValue();
	}
	public String getTERM() {
		return TERM;
	}
	public void setTERM(String tERM) {
		TERM = tERM;
	}
	public String getDESCRIPTION() {
		return DESCRIPTION;
	}
	public void setDESCRIPTION(String dESCRIPTION) {
		DESCRIPTION = dESCRIPTION;
	}
	public String getPART() {
		return PART;
	}
	public void setPART(String pART) {
		PART = pART;
	}
	public ArrayList<String> getDialogErrorMsgs() {
		return dialogErrorMsgs;
	}
	public void setDialogErrorMsgs(ArrayList<String> dialogErrorMsgs) {
		this.dialogErrorMsgs = dialogErrorMsgs;
	}

	public String[] getSerialStart() {
		return serialStart;
	}
	public void setSerialStart(String[] serialStart) {
		
		int i = 0;
		while(i<serialStart.length) {
			if(serialStart[i].length() > 0)
				charSerialStart[i] = serialStart[i].charAt(0);
			
			i++;
		}
		
		this.serialStart = serialStart;
	}
	
	public void setSerialChange(boolean[] serialChange) {		
		this.serialChange = serialChange;
	}
	
	public char[] getCharSerialStart() {
		return charSerialStart;
	}
	
	public String getSerialEnd() {
		return serialEnd;
	}
	
	
	
	
	
	public void setSerialEnd(String serialEnd) {
		
		String serialEndAux = new String(new char[30 - serialEnd.length()]).replace('\0', ' ') + serialEnd;
		
		this.serialEnd = serialEndAux;
	}
	public boolean[] getSerialChange() {
		return serialChange;
	}

	public boolean isIncludeVowels() {
		return includeVowels;
	}
	public void setIncludeVowels(boolean includeVowels) {
		this.includeVowels = includeVowels;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public List<SerialNumberELS> addSerial(String type, String model, String unitPrice) {
    	
		List<SerialNumberELS> listOfSerials = new ArrayList<SerialNumberELS>();

        char[] leftSideCharSet = null;
        char[] charSet = "0123456789".toCharArray();
        char[] withVowelCharSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        char[] withoutVowelCharSet = "BCDFGHJKLMNPQRSTVWXYZ".toCharArray();

        char[] startSerial = getCharSerialStart();
        String endSerial = new String(getSerialEnd());
        int[] values = new int[startSerial.length];
        boolean firstChange=true;
        int firstValueToChange=-1;
        int lastValueTochange=0;
        int NOCHANGE = 0;
        int NUMCHANGE = 1;
        int ALPHACHANGE = 2;
        boolean[] changable = getSerialChange();
        int[] change = new int[changable.length];
        int quantity = getQuantity();

        // determine if we use vowels or not
        if (isIncludeVowels())
        {
            leftSideCharSet = withVowelCharSet;
        } else
        {
            leftSideCharSet = withoutVowelCharSet;
        }

        // determine the initial values
        for (int i = 0; i < values.length; i++)
        {
            values[i] = 0;
            for (int j = 0; j < charSet.length; j++)
            {
                if (startSerial[i] == charSet[j])
                {
                    values[i] = j;
                }
            }
            for (int j = 0; j < leftSideCharSet.length; j++)
            {
                if (startSerial[i] == leftSideCharSet[j])
                {
                    values[i] = j;
                }
            }
        }

        // copy the changable flags
        for (int i = 0; i < changable.length; i++)
        {
            if (changable[i])
            {	if(firstChange){
            		firstValueToChange=i;
            		firstChange=false;
            	}
            	lastValueTochange=i;
                if (Character.isLetter(startSerial[i]))
                {
                    change[i] = ALPHACHANGE;
                } else
                {
                    change[i] = NUMCHANGE;
                }
            } else
            {
                change[i] = NOCHANGE;
            }
        }

        // start creating serial numbers
        StringBuffer aSerial = new StringBuffer();
        SerialNumberELS serialNumber = null;

        // create one serial for each record
        
        for (int i = 0; (quantity == 0) || (i < quantity); i++)
        {
        	
        	serialNumber = new SerialNumberELS();
        	
            aSerial.setLength(0);

            // create a serial based on the values in start and values
            for (int j = 0; j < values.length; j++)
            {
                if (change[j] == NUMCHANGE)
                {
                    aSerial.append(charSet[values[j]]);
                } else if (change[j] == ALPHACHANGE)
                {
                    aSerial.append(leftSideCharSet[values[j]]);
                } else
                {
                    aSerial.append(startSerial[j]);
                }
            }

            String aSerialAux = aSerial.toString().replaceAll("\\xa0", "");
            String endSerialAux = endSerial.trim();
            
            String subStringTmp=aSerialAux.substring(firstValueToChange, lastValueTochange+1);
            
            Integer a=new Integer(subStringTmp.compareTo(endSerialAux));
            
            // see if this serial exceeded the ending
            if ((endSerial.trim().length() > 0) && ( a.intValue()> 0))
            {
                if ((quantity > 0) && (i + 1 < quantity))
                {
                	validationEndSerialBeforeQtyReached = true;
                }
                break;
            }
                
            serialNumber.setSERIALNUMBER(aSerial.toString().trim());

            // increment the values that change
            boolean rollLeft = false;
            for (int j = values.length - 1; j >= 0; j--)
            {
                if (change[j] == NUMCHANGE)
                {
                    values[j] = values[j] + 1;
                    // roll the numbers
                    if (values[j] >= charSet.length)
                    {
                        values[j] = 0;
                    } else
                    {
                        break;
                    }
                }

                // did we run out of numeric rolls, start rolling letters from
                // the left
                if (j == 0)
                {
                    rollLeft = true;
                }
            }

            if (rollLeft)
            {
                for (int j = 0; j < values.length; j++)
                {
                    if (change[j] == ALPHACHANGE)
                    {
                        values[j] = values[j] + 1;
                        // roll the numbers
                        if (values[j] >= leftSideCharSet.length)
                        {
                            values[j] = 0;
                        } else
                        {
                            break;
                        }
                    }

                    if (j + 1 == values.length)
                    {
                        // mark the next change flag to alpha
                        for (int k = 0; k < values.length; k++)
                        {
                            if (change[k] == NUMCHANGE)
                            {
                                change[k] = ALPHACHANGE;
                                values[k] = 0;
                                break;
                            }

                            if (k + 1 == values.length)
                            {
                            	validationNotEnoughDigForSN = true;
                                i = quantity;
                            }
                        }

                    }
                }
            }
            
            serialNumber.setTYPE(type);
            serialNumber.setMODEL(model);
            serialNumber.setUNITPRICE(new com.ibm.gil.util.RegionalBigDecimal(unitPrice));
            listOfSerials.add(serialNumber);
        }
        
        
        
    	return listOfSerials;
	}

 public ArrayList<SerialNumberELS> importSerials(BufferedReader bis) {
    	
    	SerialNumberELS serialNumber = null;
    	ArrayList<SerialNumberELS> listOfSerialNumbers = new ArrayList<SerialNumberELS>();
        int lineCount = 0;

            String data = null;
            String type = null;
            String model = null;
            String serial = null;
            String unitprice = null;
            String term = null;
          //Story 1274016 - GIL - add additional fields to import function for ELS countries - 2/10/15 part
            String description = null;
            StringTokenizer fields = null;
           

            try
            { 
            	int countLn=0;
                do
                {
                	serialNumber = new SerialNumberELS();
                    data = bis.readLine();
                    if ((data != null) && (data.trim().length() > 0))
                    {	if (countLn==0) {
                    		data = removeUTF8BOM(data);
                       
                    	}
                    countLn++;
                        fields = new StringTokenizer(data, ",;\"");
                        if (fields.countTokens() == 1)
                        {
                            unitprice = "0";
                            serial = data;
                            type = "";
                            model = "";
                          //Story 1274016 - GIL - add additional fields to import function for ELS countries - 2/10/15 part
                            description = "";
                        } else if (fields.countTokens() == 3)
                        {
                            type = fields.nextToken();
                            model = fields.nextToken();
                            serial = fields.nextToken();
                            unitprice = "0";
                          //Story 1274016 - GIL - add additional fields to import function for ELS countries - 2/10/15 part
                            description = "";
                        } else if (fields.countTokens() == 4)
                        {
                            type = fields.nextToken();
                            model = fields.nextToken();
                            serial = fields.nextToken();
                            unitprice = fields.nextToken();
                          //Story 1274016 - GIL - add additional fields to import function for ELS countries - 2/10/15 part
                            description = "";
                        } else if (fields.countTokens() == 5)
                        {
                            type = fields.nextToken();
                            model = fields.nextToken();
                            serial = fields.nextToken();
                            unitprice = fields.nextToken();
                          //Story 1274016 - GIL - add additional fields to import function for ELS countries - 2/10/15 part
                            description = fields.nextToken();
                        }   else
                        {
                            continue;
                        }

                        // assign the serial
                      //Story 1274016 - GIL - add additional fields to import function for ELS countries - 2/10/15 part
                        if (serial.length()>30){
                        	//error ("Values on spreadsheet exceed field value");
                        	valid = false;
                        	//break;
                        }
                        serialNumber.setSERIALNUMBER(serial.toUpperCase());
                        serialNumber.setTYPE(type.toUpperCase());
                        serialNumber.setMODEL(model.toUpperCase());
                        try
                        {
                        	serialNumber.setUNITPRICE(new com.ibm.gil.util.RegionalBigDecimal(unitprice));
                        } catch (Exception e)
                        {
                        	serialNumber.setUNITPRICE(com.ibm.gil.util.RegionalBigDecimal.ZERO);
                        }
                      //Story 1274016 - GIL - add additional fields to import function for ELS countries - 2/10/15 part
                        serialNumber.setDESCRIPTION(description);
                        // add the value to the list
                        //getTableModel().addRow(aSerialDataModel.newcopy());
                        listOfSerialNumbers.add(serialNumber);
                        lineCount++;
                    }
                } while (data != null);
                
              
            } catch (Exception e)
            {
                logger.error("Error parsing file:" + e);
            }

            return listOfSerialNumbers;
      //Story 1274016 - GIL - add additional fields to import function for ELS countries - 2/10/15 part
       // if(valid){ -- Update even if data is wrong and highlight the wrong data.  GIL 1328028
        	//updateRecordCount();
           // getTablePanel().recordChanged();
            //TIR USRO-R-LADY-9U4NBZ - Invalid Type/Model populated on import T/S Spreadsheet - removed below line of code
            //notify("Records Loaded : " + lineCount);
      //  }else{
        //	getTableModel().clear();
       // }


 }
  private  String removeUTF8BOM(String s) {
	  String BOM="\uFEFF";
	  String bom="\ufeff";
     if (s.startsWith(BOM) || s.startsWith(bom) ) {
         s = s.substring(1);
     }
     return s;
 }
 public ArrayList<SerialNumberELS> importParts(BufferedReader bis) {
 	
 	SerialNumberELS serialNumber = null;
 	ArrayList<SerialNumberELS> listOfSerialNumbers = new ArrayList<SerialNumberELS>();
     int lineCount = 0;

         String data = null;
         String part = null;
       
         String unitprice = null;
        
       //Story 1274016 - GIL - add additional fields to import function for ELS countries - 2/10/15 part
         String description = null;
         StringTokenizer fields = null;
        

         try
         {
             do
             {
             	serialNumber = new SerialNumberELS();
                 data = bis.readLine();
                 if ((data != null) && (data.trim().length() > 0))
                 {
                     fields = new StringTokenizer(data, ",;\"");
                     if (fields.countTokens() == 1)
                     {
                         unitprice = "0";
                         part=data;
                         description = "";
                     } else if (fields.countTokens() == 2)
                     {
                         part = fields.nextToken();
                         description = fields.nextToken();
                         unitprice = "0";
                      
                         
                     }  else if (fields.countTokens() == 3)
                     {
                         part = fields.nextToken();
                         description = fields.nextToken();
                         unitprice = fields.nextToken();
                       
                     } else{
                         continue;
                     }

                     // assign the serial
                   //Story 1274016 - GIL - add additional fields to import function for ELS countries - 2/10/15 part
                     if (part.length()>25){
                     	//error ("Values on spreadsheet exceed field value");
                     	valid = false;
                     	//break;
                     }
                     serialNumber.setPART(part);
                     serialNumber.setDESCRIPTION(description);
                     
                     try
                     {
                     	serialNumber.setUNITPRICE(new com.ibm.gil.util.RegionalBigDecimal(unitprice));
                     } catch (Exception e)
                     {
                     	serialNumber.setUNITPRICE(com.ibm.gil.util.RegionalBigDecimal.ZERO);
                     }
                   
                     listOfSerialNumbers.add(serialNumber);
                     lineCount++; 
                 }
             } while (data != null);               
           
         } catch (Exception e)
         {
             logger.error("Error parsing file:" + e);
         }

         return listOfSerialNumbers;

 }

  
    
}
