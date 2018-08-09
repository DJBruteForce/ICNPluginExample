package com.ibm.gil.service.helper;




import com.ibm.gil.business.InvoiceELS;
//import com.ibm.igf.gil.GILMessages;
//import com.ibm.igf.gil.IndexingDataModel;
//import com.ibm.igf.gil.InvoiceDataModel;
//import com.ibm.igf.gil.InvoiceFrame;
////import com.ibm.igf.gil.LineItemDataModel;
//import com.ibm.igf.gui.RegionalBigDecimal;
//import com.ibm.igf.gui.RegionalDateConverter;
import com.ibm.w3.financing.tools.gcms.query.results.DOCUMENT;

public class InvoiceELSServiceHelper {
	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(InvoiceELSServiceHelper.class);
    public static boolean validateInput(InvoiceELS	aDataModel)
    {
        
//        InvoiceFrame aViewFrame = ((InvoiceFrame) getViewFrame());

        // required feilds
//        if (aDataModel.getINVOICENUMBER().trim().length() == 0)
//        {
//            logger.error("Invoice Number is a required field");
//            requestFieldFocus(aDataModel.getINVOICENUMBERidx());
//            return false;
//        }
//
//        if (aDataModel.getINVOICENUMBER().trim().length() >= 16)
//        {
//            error("Invoice Number must not be greater than 15 characters in length");
//            requestFieldFocus(aDataModel.getINVOICENUMBERidx());
//            return false;
//        }
//
//        if (aDataModel.getINVOICEDATE().equals(RegionalDateConverter.getBlankDate("GUI")))
//        {
//            error("Invoice Date is a required field");
//            requestFieldFocus(aDataModel.getINVOICEDATEidx());
//            return false;
//        }
//        
//        if(!aDataModel.isTolerance()){
//        	error("Taxes on the invoice are not within the states tax tolerance values." );
//        	requestFieldFocus(aDataModel.getTOTALAMOUNTidx());
//        	return false;
//        }
//        if(aDataModel.isUSCountry() &&((aDataModel.getINVOICENUMBER().toString().toUpperCase().startsWith("SWV") && !aDataModel.getINVOICETYPE().toString().equalsIgnoreCase("SWV")))){
//    		error("Invoice type must be SWV");
//      		return false;
//    	}
//    	List<String> poexCodes = null;
//    	
//    	if(aDataModel.isUSCountry() && (aDataModel.getINVOICETYPE().toString().equalsIgnoreCase("SWV") ||aDataModel.getINVOICETYPE().toString().equalsIgnoreCase("IBM") ||aDataModel.getINVOICETYPE().toString().equalsIgnoreCase("COM") ||aDataModel.getINVOICETYPE().toString().equalsIgnoreCase("VEN"))){
//    		String upfrontStateArray = GILMessages.getString("poexCodesforInvoiceType_"+aDataModel.getINVOICETYPE().toString());
//    		String[] stateArray = upfrontStateArray.split(",");
//    		poexCodes = Arrays.asList(stateArray);
//    		if(!poexCodes.contains(aDataModel.getPOEXCODE().toString().substring(0, 4))){
//    			error("POEX code is not valid for this type of invoice. Please select valid poexcode.");
//    			return false;
//    		}       		
//    	}	
//    	
    	//Added error message when DB/CR values don't match the invoice type
//    	try{
//    	if(aDataModel.getINVOICETYPE().toString().startsWith("RBD") && !"CR".equalsIgnoreCase(aDataModel.getDBCR().toString())){
//    		error("RBD invoice type must be CR");
//    		return false;
//    	}
//    	}
//    	catch(Exception e){
//    		return false;
//    	}
//
//        RegionalBigDecimal totalamount = RegionalBigDecimal.ZERO;
//        try
//        {
//            totalamount = aDataModel.getDecimal(aDataModel.getTOTALAMOUNTidx());
//            if (totalamount.compareTo(RegionalBigDecimal.ZERO) <= 0)
//            {
//                error("Invoice Amount must be > 0.00");
//                requestFieldFocus(aDataModel.getTOTALAMOUNTidx());
//                return false;
//            }
//        } catch (Exception exc)
//        {
//            error("Invoice Amount is invalid");
//            requestFieldFocus(aDataModel.getTOTALAMOUNTidx());
//            return false;
//        }
//
//        RegionalBigDecimal vatamount = RegionalBigDecimal.ZERO;
//        try
//        {
//            vatamount = aDataModel.getDecimal(aDataModel.getVATAMOUNTidx());
//            if (vatamount.compareTo(RegionalBigDecimal.ZERO) < 0)
//            {
//                error("Tax Amount must be >= 0.00");
//                requestFieldFocus(aDataModel.getVATAMOUNTidx());
//                return false;
//            }
//        } catch (Exception exc)
//        {
//            error("Tax Amount is invalid");
//            requestFieldFocus(aDataModel.getVATAMOUNTidx());
//            return false;
//        }
//
//        RegionalBigDecimal netvalue = RegionalBigDecimal.ZERO;
//        try
//        {
//            netvalue = aDataModel.getDecimal(aDataModel.getNETAMOUNTidx());
//            if (netvalue.compareTo(RegionalBigDecimal.ZERO) <= 0)
//            {
//                error("Net Amount must be > 0.00");
//                requestFieldFocus(aDataModel.getTOTALAMOUNTidx());
//                return false;
//            }
//        } catch (Exception exc)
//        {
//            error("Net Amount is invalid");
//            requestFieldFocus(aDataModel.getTOTALAMOUNTidx());
//            return false;
//        }

        // check for a changed db/cr
//        RegionalBigDecimal taxBalance = new RegionalBigDecimal(aDataModel.getVATBALANCE());
//        RegionalBigDecimal netBalance = new RegionalBigDecimal(aDataModel.getNETBALANCE());
//        // partially applied or not status of new
//        if ((taxBalance.compareTo(vatamount) != 0) || (netBalance.compareTo(netvalue) != 0) || (!(aDataModel.getINVOICESTATUS().equals("INEW") || aDataModel.getINVOICESTATUS().equals("IRESRCH"))))
//        {
//            if (aDataModel.getORIGIONALDBCR().trim().length() > 0)
//            {
//                if (!aDataModel.getORIGIONALDBCR().equals(aDataModel.getDBCR()))
//                {
//                    error("DB/CR Indicator cannot be changed for configured Invoice");
//                    requestFieldFocus(aDataModel.getDBCRidx());
//                    return false;
//                }
//            }
//        }
//        
//        //Added GIP342
//        // check for the Configured Invoice
//        if (aDataModel.isConfiguredInvoice())
//        {
//            error("All items on this invoice are currently configured in GCMS.\nNo changes to the invoice are allowed in GIL at this time.");
//            return false;
//        }
//        
//
//        // check for balances
//        if ((aDataModel.getVENDORNAME().trim().length() == 0) && (aDataModel.getVENDORNUMBER().trim().length() == 0))
//        {
//            aViewFrame.selectTab("Supplier");
//            error("Vendor Name or Vendor Number are required fields");
//            requestFieldFocus(aDataModel.getVENDORNAMEidx());
//            return false;
//        
//        }
//        
//        //  Story 1439460 - throw error when net amount is less than tax amount [Start]
//        RegionalBigDecimal netAmountvalue = RegionalBigDecimal.ZERO;
//        RegionalBigDecimal vatAmountvalue = RegionalBigDecimal.ZERO;
//        
//       
//        vatAmountvalue = aDataModel.getDecimal(aDataModel.getVATAMOUNTidx());
//        netAmountvalue = aDataModel.getDecimal(aDataModel.getNETAMOUNTidx());
//        float vatValue = vatAmountvalue.floatValue(); 
//        float netValue = netAmountvalue.floatValue()  ; 
//        
//        if((vatValue!=0 )&& (netValue < vatValue )) 
//        {
//        	error("Tax amount cannot be more than the Net amount");
//        	requestFieldFocus(aDataModel.getTOTALAMOUNTidx());
//        	return false;
//        }
//        
//                     
//    //  Story 1439460 - throw error when net amount is less than tax amount [End]
//        
//        RegionalBigDecimal commission = RegionalBigDecimal.ZERO;
//        RegionalBigDecimal bd99 = NinetyNinePointNineNineNine;
//        try
//        {
//            commission = aDataModel.getDecimal(aDataModel.getVENDORCOMMISSIONidx());
//        } catch (Exception exc)
//        {
//        }
//
//        if (aDataModel.isCommissionInvoice() && (!commission.equals(bd99)))
//        {
//            if (aDataModel.getDOCUMENTMODE() != InvoiceDataModel.UPDATEMODE)
//            {
//                aViewFrame.selectTab("Supplier");
//                error("Commission Invoices are not valid for this supplier");
//                requestFieldFocus(aDataModel.getINVOICETYPEidx());
//                return false;
//            }
//        }
//        
//        if(aDataModel.isCOMInvoice() && aDataModel.isEXEMPTorStartswithDEVofSRNumber()){
//            aViewFrame.selectTab("Supplier");        	
//            error("Commission Invoices are not valid for this supplier");
//            requestFieldFocus(aDataModel.getINVOICETYPEidx());
//            return false;        	
//        }   
//        
//        // formats
//        Date invoiceDate = null;
//
//        boolean dateOk = true;
//        try
//        {
//            invoiceDate = aDataModel.getDate(aDataModel.getINVOICEDATEidx());
//            if (invoiceDate == null)
//            {
//                dateOk = false;
//            }
//        } catch (Exception exc)
//        {
//            dateOk = false;
//        }
//        if (dateOk == false)
//        {
//            error("Invoice Date is a required field");
//            requestFieldFocus(aDataModel.getINVOICEDATEidx());
//            return false;
//        }
//
//        // validation
//        Date now = new Date();
//        if (invoiceDate.getTime() > now.getTime())
//        {
//            error("Invoice Date must not be a future date");
//            requestFieldFocus(aDataModel.getINVOICEDATEidx());
//            return false;
//        }
//
//        long MS_1_AFTER = 86400000;
//        if (invoiceDate.getTime() < (now.getTime() - (300 * MS_1_AFTER)))
//        {
//            error("Invoice Date must not be greater than 300 days old");
//            requestFieldFocus(aDataModel.getINVOICEDATEidx());
//            return false;
//        }
//                	
//        // retrieve the scandate from CM
//		aDataModel.retrieveCreatedDate();		
//		Date scanDate = aDataModel.getCREATEDATEasDate();
//		
//		if (scanDate.getTime() < invoiceDate.getTime()) {
//			error("Scan Date cannot be before the Invoice Date");
//			requestFieldFocus(aDataModel.getINVOICEDATEidx());
//			return false;
//		}
//		
//        // validate the total and tax amounts
//        if (vatamount.compareTo(totalamount) > 0)
//        {
//            error("Tax amount must not be greater than Total amount");
//            requestFieldFocus(aDataModel.getVATAMOUNTidx());
//            return false;
//        }
//
//        // validate the vat registartion number
//        if (aDataModel.isVATRegistrationNumberRequired())
//        {
//            if (aDataModel.getVATREGISTRATIONNUMBER().trim().length() == 0)
//            {
//                aViewFrame.selectTab("Supplier");
//                error("Legal Id Number is a required field");
//                requestFieldFocus(aDataModel.getVATREGISTRATIONNUMBERidx());
//                return false;
//            }
//        }
//
//        // validate the ol number
//        if (!offeringLetterValid)
//        {
//            aViewFrame.selectTab("Offer Letter");
//            error("Offer Letter doesn't exist or isn't valid");
//            requestFieldFocus(aDataModel.getOFFERINGLETTERNUMBERidx());
//            return false;
//        }
//
//        // validate the exchange rate
//        if (!aViewFrame.getDefaultCountryCurrencyCode().equals(aDataModel.getCURRENCY()))
//        {
//            if (aDataModel.getOFFERINGLETTERNUMBER().trim().length() != 0)
//            {
//                RegionalBigDecimal exchangeRate = aDataModel.getDecimal(aDataModel.getEXCHANGERATEidx());
//                if (exchangeRate.compareTo(new RegionalBigDecimal(1)) == 0)
//                {
//                    aViewFrame.selectTab("Offer Letter");
//                    error("Exchange rate must not be one");
//                    requestFieldFocus(aDataModel.getEXCHANGERATEidx());
//                    return false;
//                }
//                if (exchangeRate.compareTo(RegionalBigDecimal.ZERO) == 0)
//                {
//                    aViewFrame.selectTab("Offer Letter");
//                    error("Exchange rate must not be zero");
//                    requestFieldFocus(aDataModel.getEXCHANGERATEidx());
//                    return false;
//                }
//            }
//        }
//
//        // validate credit invoice on rof invoice
//        if ((aDataModel.isROFInvoice()) && (aDataModel.isCRInvoice()))
//        {
//            error("ROF Invoice must be debit invoice only");
//            requestFieldFocus(aDataModel.getDBCRidx());
//            return false;
//        }
//
//        // validate the customer number
//        if ((aDataModel.getINSTALLEDCUSTOMERNAME().trim().length() == 0) && (aDataModel.getINSTALLEDCUSTOMERNUMBER().trim().length() == 0))
//        {
//            customerValid = true;
//        }
//
//        if (!customerValid && null != aDataModel.getOFFERINGLETTERNUMBER() && aDataModel.getOFFERINGLETTERNUMBER().length()>0)
//        {
//            aViewFrame.selectTab("Offer Letter");
//            error("Customer doesn't exist");
//            requestFieldFocus(aDataModel.getCUSTOMERNUMBERidx());
//            return false;
//        }
//
//        // check if the invoice exists allready
//        ArrayList results;
//        try
//        {
//            if ((!aDataModel.getINVOICENUMBER().equals(aDataModel.getOLDINVOICENUMBER())) || (!aDataModel.getINVOICEDATE().equals(aDataModel.getOLDINVOICEDATE())))
//            {
//                results = aDataModel.selectByInvoiceStatement();
//                for (int i = 0; i < results.size(); i++)
//                {
//                    InvoiceDataModel aIDM = (InvoiceDataModel) results.get(i);
//                    if ((aIDM.getINVOICENUMBER().equals(aDataModel.getINVOICENUMBER()) && (aIDM.getINVOICEDATE().equals(aDataModel.getINVOICEDATE()))) && (!aIDM.getOBJECTID().equals(aDataModel.getOBJECTID())))
//                    {
//                        error("Duplicate Invoice entered");
//                        requestFieldFocus(aDataModel.getINVOICENUMBERidx());
//                        return false;
//                    }
//                }
//            }
//        } catch (Exception e)
//        {
//            error("Error validating Invoice Number");
//            fatal(e.toString());
//            requestFieldFocus(aDataModel.getINVOICENUMBERidx());
//            return false;
//        }
//        
//    	if(aDataModel.getDOCUMENTMODE() == InvoiceDataModel.ADDMODE){
//	    	String supplierNum = aDataModel.getVENDORNUMBER().trim();
//	        if (supplierNum.length() > 0 && aDataModel.getSupplierNums().contains(supplierNum))
//	        {
//	       		boolean choice = prompt("Invoice number/Supplier number already exists, Do you wish to change the invoice number ?");
//	       		if(choice == true){
//	                requestFieldFocus(aDataModel.getINVOICENUMBERidx());
//	                return false;
//	       		}
//	
//	        }
//    	}
//    	if(aDataModel.isCRInvoice() && aDataModel.getREFERENCEINVOICENUMBER().trim().length()>0){
//    		boolean supplierFound = false;
//    		boolean debitInvoiceFound = true;
//    		int invoiceCount = 0;
//    		String supNum = "";
//    		results = aDataModel.selectByReferenceInvoiceStatement();
//    		if(results.size() == 0){
//    			debitInvoiceFound = false;		
//    		}
//            for (int i = 0; i < results.size(); i++)
//            {
//                InvoiceDataModel aIDM = (InvoiceDataModel) results.get(i);
//                if(aIDM.isDBInvoice()){
//                	invoiceCount++;
//               		if(aIDM.getINVOICEDATE().equals(aDataModel.getREFERENCEINVOICEDATE())){
//                 			if(aIDM.getVENDORNUMBER().equals(aDataModel.getVENDORNUMBER().trim())){
//                 				supplierFound = true;
//                 			}else{
//                 				supNum = aIDM.getVENDORNUMBER();
//                 			}
//              		}
//                }else{
//                	debitInvoiceFound = false;
//                }
//            }
//            if(!debitInvoiceFound && invoiceCount == 0){
//            	error("Debit invoice is not found in GCMS");
//            	return false;
//            }
//            if(!supplierFound){
//            	error("Supplier number on debit invoice is not the same as credit  invoice ("+supNum +") for debit)");
//            	return false;
//            }
//    	}
//    	
//      
//        if (aDataModel.areLineItemsRequired())
//        {
//            RegionalBigDecimal unitvatvalue = RegionalBigDecimal.ZERO;
//            try
//            {
//                unitvatvalue = aDataModel.getDecimal(aDataModel.getLINEITEMVATAMOUNTidx());
//                if (unitvatvalue.compareTo(RegionalBigDecimal.ZERO) < 0)
//                {
//                    error("Line Item Tax Amount must be >= 0.00");
//                    return false;
//                }
//
//            } catch (Exception exc)
//            {
//                error("Line Item Tax Amount is invalid");
//                return false;
//            }
//
//            RegionalBigDecimal unitnetvalue = RegionalBigDecimal.ZERO;
//            try
//            {
//                unitnetvalue = aDataModel.getDecimal(aDataModel.getLINEITEMNETAMOUNTidx());
//                if (!netvalue.equals(unitnetvalue))
//                {
//                    error("Invoice Net Amount must equal Line Item Net Total");
//                    requestFieldFocus(aDataModel.getTOTALAMOUNTidx());
//                    return false;
//                }
//            } catch (Exception exc)
//            {
//                error("Unit Net Amount is invalid");
//                return false;
//            }
//
//            if(!aDataModel.isUSCountry() && vatamount.compareTo(RegionalBigDecimal.ZERO)== 0){
//            	ArrayList<LineItemDataModel> listLineItems  = aDataModel.getLineItems();
//            	for(LineItemDataModel lineItem: listLineItems){
//            		if(lineItem.getVATPercentage().compareTo(RegionalBigDecimal.ZERO)>0){
//                		error("Tax Amount is 0.00 on the header screen you cannot select a Non 0% Tax Code on line item");
//                		return false;
//            		}
//            	}
//            }
//            
//            RegionalBigDecimal vatVariance = RegionalBigDecimal.ZERO;
//            try
//            {
//                vatVariance = aDataModel.getDecimal(aDataModel.getVATVARIANCEidx());
//                RegionalBigDecimal vatDiff = unitvatvalue.subtract(vatamount).abs();
//                if (vatDiff.compareTo(vatVariance) > 0)
//                {
//                    // verify with the user that this is what they wanted to do
//                    error("Tax Amount must equal " + unitvatvalue.toString() + " +/- $" + vatVariance);
//                    requestFieldFocus(aDataModel.getVATAMOUNTidx());
//                    return false;
//                }
//            } catch (Exception exc)
//            {
//
//            }
//        }
//
//        if ((aDataModel.getOFFERINGLETTERNUMBER().trim().length() == 0) && (aDataModel.getDOCUMENTMODE() == IndexingDataModel.ADDMODE))
//        {
//            boolean choice = false;
//            choice = prompt("Offer Letter Number is blank.\nDo you wish to correct?");
//            if (choice == true)
//            {
//                // yes selected
//                aViewFrame.selectTab("Offer Letter");
//                requestFieldFocus(aDataModel.getOFFERINGLETTERNUMBERidx());
//                return false;
//            }
//        }
//        if (aDataModel.getOFFERINGLETTERNUMBER().trim().length() != 0)
//        {
//            if (aDataModel.getCUSTOMERNUMBER().trim().length() != 0 && !aDataModel.getOFFERINGLETTERCUSTOMERNUMBER().equals(aDataModel.getCUSTOMERNUMBER()))
//            {
//                error ("Customer Number must be blank or equal to the Offer Letter Customer Number");
//                requestFieldFocus (aDataModel.getCUSTOMERNUMBERidx());
//                return false;
//            }
//        }
//        /*Story 1288020 - Modify validation when country code is BR and comp code is 0001 the invoice type is must be IBM
//        added COM invoice also in the condition*/
//        if(aDataModel.getCOUNTRY().equals("BR") && aDataModel.isCompanyCodeOf0001() && !aDataModel.isIBMInvoice() && !aDataModel.isCommissionInvoice()){
//            error("Please select Invoice Type of IBM or COM for the Comp Code of 0001 and Country BR");
//            requestFieldFocus(aDataModel.getINVOICETYPEidx());
//            return false;
//        }  
//        if(aDataModel.getLineItems().size() == 0){
//        	error("Please add the LineItems in the LineItemsGUI Screen");
//        	return false;
//        }        
    
        // validate the document got indexed correctly
        boolean exists = false;
        try
        {
            DOCUMENT cmdoc = aDataModel.getInvoiceDataModelELS().queryDocument();
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
            logger.error("Document did not index correctly");
//            requestFieldFocus(aDataModel.getINVOICENUMBERidx());
            return false;
        }
            
       return true;
    }
}
