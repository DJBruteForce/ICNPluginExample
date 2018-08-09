package com.ibm.gil.service.helper;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.ibm.gil.business.FormSelect;
import com.ibm.gil.business.InvoiceLineItemsELS;
import com.ibm.gil.business.LineItemELS;
import com.ibm.gil.model.LineItemDataModelELS;
import com.ibm.gil.util.GilUtility;
import com.ibm.gil.util.RegionalBigDecimal;






public class LineItemELSServiceHelper {

	 private void renumberLineItems()
	    {
		  //TODO*********************************nothing moe to do remove this*LineItemsDialog
//       ArrayList lineItems = //get lineitems from lineitems table getArrayList();
//       renumberLineItems(lineItems);
	    }

	    public static void renumberLineItems(List<LineItemELS> lineItems)
	    {
	        LineItemELS aDataModel = null;
	        LineItemELS advancedDataModel = null;
	        int nextLineNumber = 0;
	        int nextSubNumber = 0;
	        int advancedLineNumber = 0;
	        int advancedSublineNumber = 0;
	        int lineNumber = 0;
	        int sublineNumber = 0;
	        int previousLineNumber = 0;
	        int previousSublineNumber = 0;

	        if (lineItems.size() < 1)
	            return;

	        // sort by the line # / sub line #
	        Collections.sort(lineItems);

	        // find the last line number
	        aDataModel = (LineItemELS) lineItems.get(lineItems.size() - 1);
	        lineNumber = aDataModel.getLineItemDataModelELS().getInt(aDataModel.getLINENUMBER());

	        nextLineNumber = lineNumber + 1;

	        // zero out the ones that need to be reassgined new line numbers
	        advancedDataModel = null;
	        for (int i = 0; i < lineItems.size(); i++)
	        {
	            aDataModel = (LineItemELS) lineItems.get(i);
	            if (advancedDataModel != null)
	            {
	                lineNumber = aDataModel.getLineItemDataModelELS().getInt(aDataModel.getLINENUMBER());
	                advancedLineNumber = advancedDataModel.getLineItemDataModelELS().getInt(advancedDataModel.getLINENUMBER());
	                if (advancedLineNumber == lineNumber)
	                {
	                    if (advancedDataModel.getLineItemDataModelELS().isMatchingLineNumberItem(aDataModel))
	                    {
	                        advancedDataModel = aDataModel;
	                    } else
	                    {
	                        aDataModel.setLINENUMBER("0");
	                    }
	                } else
	                {
	                    advancedDataModel = aDataModel;
	                }
	            } else
	            {
	                advancedDataModel = aDataModel;;
	            }
	        }

	        // sort by the line # / sub line #
	        Collections.sort(lineItems);

	        // first pass assign the line numbers to the zero ones
	        advancedDataModel = null;
	        for (int i = 0; i < lineItems.size(); i++)
	        {
	            aDataModel = (LineItemELS) lineItems.get(i);

	            lineNumber = GilUtility.getInt(aDataModel.getLINENUMBER());
	            if (lineNumber == 0)
	            {
	                if (advancedDataModel != null)
	                {
	                    if (!advancedDataModel.getLineItemDataModelELS().isMatchingLineNumberItem(aDataModel))
	                    {
	                        nextLineNumber++;
	                    }
	                }

	                aDataModel.setLINENUMBER(Integer.toString(nextLineNumber));
	            }

	            advancedDataModel = aDataModel;
	        }

	        // re-sort
	        Collections.sort(lineItems);

	        // second pass assign the sub line numbers to the zero ones
	        for (int i = 0; i < lineItems.size(); i++)
	        {
	            aDataModel = (LineItemELS) lineItems.get(i);

	            lineNumber = GilUtility.getInt(aDataModel.getLINENUMBER());
	            sublineNumber =  GilUtility.getInt(aDataModel.getSUBLINENUMBER());

	            // reset the next sublineNumber to max when we advance the line
	            // number
	            if (lineNumber != previousLineNumber)
	            {
	                nextSubNumber = 1;
	                previousLineNumber = 0;

	                // find the biggest subline number for this line number
	                for (int j = i + 1; j < lineItems.size(); j++)
	                {
	                    advancedDataModel = (LineItemELS) lineItems.get(j);

	                    advancedLineNumber =  GilUtility.getInt(aDataModel.getLINENUMBER());
	                    advancedSublineNumber =  GilUtility.getInt(aDataModel.getSUBLINENUMBER());
	                    if (lineNumber == advancedLineNumber)
	                    {
	                        nextSubNumber = advancedSublineNumber + 1;
	                    } else
	                    {
	                        break;
	                    }
	                }
	            }

	            // see if we need to renumber this sub line number
	            if (sublineNumber == 0)
	            {
	                sublineNumber = nextSubNumber;
	                aDataModel.setSUBLINENUMBER(Integer.toString( sublineNumber));
	                nextSubNumber++;
	            }

	        }

	        // re-sort
	        Collections.sort(lineItems);

	        // check for duplicate seq numbers
	        previousLineNumber = 0;
	        previousSublineNumber = 0;
	        for (int i = 0; i < lineItems.size(); i++)
	        {
	            aDataModel = (LineItemELS) lineItems.get(i);

	            lineNumber =  GilUtility.getInt(aDataModel.getLINENUMBER());
	            sublineNumber =  GilUtility.getInt(aDataModel.getSUBLINENUMBER());

	            // check to see if we are repeating the subline #
	            if (lineNumber == previousLineNumber)
	            {
	                if (sublineNumber <= previousSublineNumber)
	                {
	                    // use the previous sub line number so that it pushes out
	                    // the rest of the list
	                    sublineNumber = previousSublineNumber + 1;
	                    aDataModel.setSUBLINENUMBER( Integer.toString(sublineNumber));
	                }
	            }
	            previousLineNumber = lineNumber;
	            previousSublineNumber = sublineNumber;

	            // debug ("> line # " + lineNumber + " subline # " + sublineNumber);
	        }

	        // sort by the line # / sub line #
	        Collections.sort(lineItems);
//TODO check what to do in fron end
//	        getTablePanel().recordChanged();
	    }
	    

	    private void spreadTaxRoundingError()
	    {
	    	//TODO fornend missing line item dialog
//	        ArrayList lineItems = getArrayList();
//	        spreadTaxRoundingError(lineItems);
	    }

	    public static void spreadTaxRoundingError(InvoiceLineItemsELS invoice,List<LineItemELS> lineItems)
	    {
	    	String country = invoice.getCountry();
	        LineItemELS aDataModel = null;
	        Hashtable lineNumberGroups = new Hashtable();
	        
	        Vector lineNumberList = null;
	        String key = null;
	        int quantity = 0;
	        HashMap<String, RegionalBigDecimal> vatPercentages= new HashMap<String, RegionalBigDecimal>();
	        List<FormSelect> vatCodes= new ArrayList<FormSelect>();
	        LineItemELS dummyLineItem = new LineItemELS(country);
	        dummyLineItem.getLineItemDataModelELS().loadSelectVatCodes(vatPercentages, vatCodes, invoice);
	        
	        // build a hash table of the line numbers
	        for (int i = 0; i < lineItems.size(); i++)
	        {
	            aDataModel = (LineItemELS) lineItems.get(i);

	            // check the quantity to make sure it's not > 1
	            quantity = GilUtility.getInt(aDataModel.getQUANTITY());
	            if (quantity != 1)
	                continue;

	            key = aDataModel.getLINENUMBER() + "|" + aDataModel.getVATCODE() + "|" + aDataModel.getUNITPRICE() + "|" + aDataModel.getBILLEDTOIGFINDC();

	            // see if this item exists
	            lineNumberList = (Vector) lineNumberGroups.get(key);
	            if (lineNumberList == null)
	            {
	                // if not add a new list to group them
	                lineNumberList = new Vector();
	                lineNumberGroups.put(key, lineNumberList);
	            }

	            // add this item into the list
	            lineNumberList.add(aDataModel);
	        }

	        // now go through each group and spread the taxes
	        boolean addOneCent = false;
	        RegionalBigDecimal qty = null;
	        RegionalBigDecimal vatTotal = null;
	        RegionalBigDecimal unitVat = null;
	        RegionalBigDecimal unitVatRounding = null;
	        RegionalBigDecimal vatRemainder = null;
	        RegionalBigDecimal oneCent = new RegionalBigDecimal(0.01).setScale(2);
	        for (Enumeration enum1 = lineNumberGroups.keys(); enum1.hasMoreElements();)
	        {
	            key = (String) enum1.nextElement();
	            lineNumberList = (Vector) lineNumberGroups.get(key);

	            // only need to spread tax on qty > 1
	            quantity = lineNumberList.size();
	            if (quantity > 1)
	            {
	                aDataModel = (LineItemELS) lineNumberList.get(0);
	                qty = new com.ibm.gil.util.RegionalBigDecimal(quantity);
	                vatTotal = aDataModel.getLineItemDataModelELS().getVATPercentage(vatPercentages);
	                vatTotal = vatTotal.multiply(qty);
	                vatTotal = vatTotal.multiply(aDataModel.getDecimal(aDataModel.getUNITPRICE()));
	                vatTotal = vatTotal.divide(LineItemDataModelELS.ONEHUNDRED, 2, com.ibm.gil.util.RegionalBigDecimal.ROUND_HALF_UP);

	                unitVat = vatTotal.divide(qty, 2, com.ibm.gil.util.RegionalBigDecimal.ROUND_HALF_UP);
	                vatRemainder = vatTotal.subtract(unitVat.multiply(qty)).setScale(2, com.ibm.gil.util.RegionalBigDecimal.ROUND_HALF_UP);
	                if (vatRemainder.compareTo(com.ibm.gil.util.RegionalBigDecimal.ZERO) > 0)
	                {
	                    unitVatRounding = unitVat.add(oneCent);
	                    addOneCent = true;
	                } else
	                {
	                    unitVatRounding = unitVat.subtract(oneCent);
	                    addOneCent = false;
	                }

	                for (int j = 0; j < quantity; j++)
	                {
	                    aDataModel = (LineItemELS) lineNumberList.get(j);

	                    // determine if the tax roll up still needed
	                    if (vatRemainder.compareTo(com.ibm.gil.util.RegionalBigDecimal.ZERO) == 0)
	                    {
	                        aDataModel.setVATAMOUNT(unitVat.toString());
	                        aDataModel.setTOTALVATAMOUNT(unitVat.toString());
	                    } else if (addOneCent)
	                    {
	                    	vatRemainder = vatRemainder.subtract(oneCent);
	                        aDataModel.setVATAMOUNT(unitVatRounding.toString());
	                        aDataModel.setTOTALVATAMOUNT(unitVatRounding.toString());
	                    } else
	                    {
	                    	vatRemainder = vatRemainder.add(oneCent);
	                        aDataModel.setVATAMOUNT(unitVatRounding.toString());
	                        aDataModel.setTOTALVATAMOUNT(unitVatRounding.toString());
	                    }
	                }

	            } else
	            {
	                // qty = 1
	                aDataModel = (LineItemELS) lineNumberList.get(0);
	                qty = aDataModel.getDecimal(aDataModel.getQUANTITY());
	                vatTotal = aDataModel.getLineItemDataModelELS().getVATPercentage(vatPercentages);
	                vatTotal = vatTotal.multiply(qty);
	                vatTotal = vatTotal.multiply(aDataModel.getDecimal(aDataModel.getUNITPRICE()));
	                vatTotal = vatTotal.divide(LineItemELS.ONEHUNDRED, 2, com.ibm.gil.util.RegionalBigDecimal.ROUND_HALF_UP);
	                aDataModel.setVATAMOUNT(vatTotal.toString());
	                aDataModel.setTOTALVATAMOUNT(vatTotal.toString());
	            }
	        }

	    }

	    public static boolean revalidateInput()
	    {
	    	
//	        ArrayList lineItems = getArrayList();
//	        LineItemDataModel aDataModel = null;
//	        LineItemFrame aLineItemFrame = ((LineItemFrame) getViewFrame());
//	        System.out.println("Line items size is :" +lineItems.size() );
//	        //story-1588866 restrict the total line items less than or equal to 3200
//	        if (lineItems.size() > 2500) {
//	  			error("Exceed maximum quantity of line items");
//	  			requestFieldFocus(aDataModel.getQUANTITYidx());
//	  			return false;
//
//	  		}
//
//	        for (int i = 0; i < lineItems.size(); i++)
//	        {
//	            aDataModel = (LineItemDataModel) lineItems.get(i);
//	            // validate the type /model exists
//	            if (aDataModel.isTypeModel())
//	            {
//	            	/*Story 1283297 - GIL - type/model not found errors with XXXX/001, 2 etc
//	            	added XXXX in the if condition.*/
//	            	String description = "";
//	            	if(!"".equals(aDataModel.getDESCRIPTION())&& (null != aDataModel.getDESCRIPTION())&& (!"XXXX".equals(aDataModel.getDESCRIPTION())))
//	            		description = aDataModel.getDESCRIPTION();
//	                if ((aDataModel.getBLANKTYPEMODEL().equals("N") && aDataModel.getDESCRIPTION().trim().length() == 0)|| (!aDataModel.selectTypeModelDescription()))
//	                {
//	                	if(!(aDataModel.getTYPE().equalsIgnoreCase("XXXX"))){
//	                    error("Type/Model not found");
//	                    aLineItemFrame.selectRecord(aDataModel);
//	                    requestFieldFocus(aDataModel.getTYPEidx());
//	                    // retaining description if the user changed it manually
//	                    if(!"".equals(description))
//	                    	aDataModel.setDESCRIPTION(description);
//	                    return false;
//	                }}
//	             // retaining description if the user changed it manually
//	                if(!"".equals(description))
//	                	aDataModel.setDESCRIPTION(description);
//	                
//	                if(aDataModel.getSERIAL().length()>25)
//	            	{
//	            		 aLineItemFrame.selectRecord(aDataModel);
//	            		error("Values on spreadsheet exceed field value");
//	            		return false;
//	            	}
//	                // validate the unit price > 0
//	                RegionalBigDecimal unitprice = RegionalBigDecimal.ZERO;
//	                try
//	                {
//	                    unitprice = aDataModel.getDecimal(aDataModel.getUNITPRICEidx());
//
//	                    if (unitprice.compareTo(RegionalBigDecimal.ZERO) <= 0)
//	                    {
//	                        error("Unit Price must be > 0.00");
//	                        aLineItemFrame.selectRecord(aDataModel);
//	                        requestFieldFocus(aDataModel.getUNITPRICEidx());
//	                        return false;
//	                    }
//	                } catch (Exception exc)
//	                {
//	                    error("Unit Price is invalid");
//	                    aLineItemFrame.selectRecord(aDataModel);
//	                    requestFieldFocus(aDataModel.getUNITPRICEidx());
//	                    return false;
//	                }
//
//	                // validate the unit price is in max price range -- warning
//	                RegionalBigDecimal maxPrice = aDataModel.getDecimal(aDataModel.getMAXUNITPRICEidx());
//	                RegionalBigDecimal minPrice = aDataModel.getDecimal(aDataModel.getMINUNITPRICEidx());
//	                if (maxPrice.compareTo(RegionalBigDecimal.ZERO) != 0)
//	                {
//	                    if (unitprice.compareTo(maxPrice) > 0)
//	                    {
////	                        boolean result = prompt("Unit Price exceeds maximum allowable value of " + maxPrice + ".\nDo you wish to correct?");
//	                        if (result)
//	                        {
//	                            aLineItemFrame.selectRecord(aDataModel);
//	                            return false;
//	                        }
//	                    }
//	                    if (unitprice.compareTo(minPrice) < 0)
//	                    {
////	                        boolean result = prompt("Unit Price is less than minimum allowable value of " + minPrice + ".\nDo you wish to correct?");
//	                        if (result)
//	                        {
//	                            aLineItemFrame.selectRecord(aDataModel);
//	                            return false;
//	                        }
//	                    }
//	                }
//	            }
//	        }
//
	        return true;
	    }

}
