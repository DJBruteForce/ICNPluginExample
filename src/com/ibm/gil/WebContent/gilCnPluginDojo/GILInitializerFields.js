define(["dojo/_base/lang", "dojo/_base/declare" ], function(lang, declare){
	
	 /**
	  * This class will return for each of the className(documents folder) the attributes array. 
	  */
	 return declare("gilCnPluginDojo.GILInitializerFields",[], {
		 
		    constructor: function(className){
			      this.className = className;
			    },
				   
			    /**
			     *  Return the attributes array. If type of attribute is doc returns the documents business attributes.
			     *  if type of attribute is form, returns the dialog form attributes(data-dojo-attach-point of each input 
			     *  type in the html form).
			     *  
			     *  Both arrays must have the attributes listed in the same order. If the doc array has some attribute that 
			     *  is not shown in the form, please create hidden attributes in html templates setting the 
			     *  property style like this: style="display: none;" ( see InvoiceDialog.html)
			     *  
			     */
			    get: function(type) {
		
				 if ( this.className.startsWith("Offering Letter") && this.className.length == 18) {
					 
					 if ( type == 'doc')	
						 return ['{NAME}' , 'Customer_Name', 'Customer_Number','createdTimestamp'];
					 
					 else  if ( type == 'form')  
						 return ['number' ,  'customerName', 'customerNumber', 'createdTimestamp','amount', 'currency', 'olDate','createdTimestamp'];
				 } else
				 
				 if (this.className.startsWith("Invoice") && this.className.length == 10) {
						
					 if ( type == 'doc')	
						 return ['Invoice_Number', 'Account_Number' , 'Contract_Number', 'Customer_Number', 'Customer_Name', 'Invoice_Date' , 'Vendor_Name', 'Offering_L_0002', 'Additional_Info', 'Team', 'Due_Date', 'SOURCE','createdTimestamp', 'USER_ID' ];
					 
					 else  if ( type == 'form')  
						 		return ['invoiceNumber', 'accountNumber', 'contractNumber', 'customerNumber', 'customerName', 'invoiceDate','supplierName', 'offeringLetterNumber', 'additionalInfo', 'team', 'dueDate', 'source', 'createdTimestamp', 'userId','invoiceAmountTotal', 'invoiceAmountTax', 'netAmount', 'taxCode', 'poexCode','supplierNumber', 'ocrKid' , 'dbCr', 'invoiceCurrency', 'invoiceType', 'compCode', 'country'];
						
					}
				 else if ( this.className.endsWith("Invoice")) {
					 
					 if ( type == 'doc')//values from cm
						 return ['Invoice_Number' , 'Invoice_Date','SOURCE','USER_ID','createdTimestamp','Business_Partne'];//add all needed 
					 
					 else  if ( type == 'form')
						 	  return ['invNum' , 'invDate', 'source','userId','createdTimestamp','businessPartner','invAmt','taxAmt','netAmt' ];
					 
				 } else if (this.className.startsWith("Contract") && this.className.length == 11){
							 
					 		if ( type == 'doc')	
								 return ['Contract_Number', 'Customer_Name', 'Customer_Number' ,'Offering_L_002' ,  'createdTimestamp' ,'Team', 'SOURCE','USER_ID' ];
							 
							 else  if ( type == 'form')  
								 		return ['contractNumber','customerName', 'customerNumber',  'offeringLetterNumber', 'createdTimestamp' ,'Team', 'SOURCE','USER_ID' ];

				 }else if (this.className.endsWith("SignCOA") && this.className.length == 9){
					 
				 		if ( type == 'doc')	
							 return ['GCPS_COA_Number', 'GCPS_Cust_Name' ,  'createdTimestamp' ,'Team', 'SOURCE','USER_ID' ];
						 
						 else  if ( type == 'form')  
							 		return ['coaNumber','coaCustomerName', 'createdTimestamp', 'Team', 'SOURCE','USER_ID','customerNumber',  'offeringLetterNumber',  ];

			    } else if (this.className.endsWith("StampdutyOL") && this.className.length == 13){
			    	
					 		if ( type == 'doc')	
								 return ['GCPS_Cust_Name','GCPS_OL_Number', 'createdTimestamp' ,'Team', 'SOURCE','USER_ID' ];
							 
							 else  if ( type == 'form')  
								 		return ['offeringLetterCustomerName' , 'offerringLetterNumber',  'createdTimestamp' ,'Team', 'SOURCE','USER_ID' ];

			    	
			    } else if (this.className.endsWith("CountersignOL") && this.className.length == 15){
			    	
					 		if ( type == 'doc')	
					 			 return ['GCPS_Cust_Name','GCPS_OL_Number', 'createdTimestamp' ,'Team', 'SOURCE','USER_ID' ];
							 
							 else  if ( type == 'form')  
								 		return ['offeringLetterCustomerName' , 'offerringLetterNumber',  'createdTimestamp' ,'Team', 'SOURCE','USER_ID' ];
			    	
			    } else if (this.className.endsWith("SignOL") && this.className.length == 8){
			    	
					 		if ( type == 'doc')	
					 			 return ['GCPS_Cust_Name','GCPS_OL_Number', 'createdTimestamp' ,'Team', 'SOURCE','USER_ID' ];
							 
							 else  if ( type == 'form')  
								 return ['offeringLetterCustomerName' , 'offerringLetterNumber',  'createdTimestamp' ,'Team', 'SOURCE','USER_ID' ];
			    	
			    } else if (this.className == "ROF Signed Offer Letter"){
			    	
			 		if ( type == 'doc')	
			 			 return ['ROF_OL_Number','Country_Code', 'createdTimestamp' ,'Team', 'SOURCE','USER_ID' ];
					 
					 else  if ( type == 'form')  
						 return ['offerringLetterNumber', 'countryCode', 'createdTimestamp' ,'Team', 'SOURCE','USER_ID' ];
	    	
			    } else if (this.className.endsWith('Misc') && this.className.length == 6){
			    	if(type == 'doc')
			    		return ['Subject', 'L_CustNum', 'GCPS_Cust_Name', 'GCPS_OL_Number', 'GCPS_COA_Number', 
			    			'Invoice_Number', 'SOURCE', 'createdTimestamp', 'USER_ID'];
			    	else if(type == 'form')
			    		return ['subject', 'lCustNumber', 'gcpsCustName', 'gcpsOLNumber', 'gcpsCOANumber', 
			    			'invoiceNumber', 'source', 'createdTimestamp', 'userId'];
			    } else if ( this.className.startsWith("Misc Invoice")) {
					 
					 if ( type == 'doc')//values from cm
						 return ['Invoice_Number' , 'Invoice_Date','Country_Code','Vendor_Name','SOURCE', 'createdTimestamp','USER_ID'];//add all needed 
					 
					 else  if ( type == 'form')
						 	  return ['invNum' , 'invDate', 'countryCode','vendorName','source', 'createdTimestamp','userId'];
	    }

               
					 
				 
				 return [];
					  
			}
	
	
	 });
});