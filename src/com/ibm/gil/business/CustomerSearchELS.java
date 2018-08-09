package com.ibm.gil.business;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.ibm.gil.model.InvoiceDataModelELS;


public class CustomerSearchELS extends Indexing{
	
	private static final Logger logger = LogManager.getLogger(CustomerSearchELS.class);
	
	
	
	private  short INSTALLEDCUSTOMERNUMBER ;
	 
	 private   String CUSTOMERNAME ;
	 private   String CUSTOMERNUMBER ;
	 
	 private   String QUERYITEM ;
	 
	 private  String CUSTOMERADDRESS1 ;
	 private  String CUSTOMERADDRESS2 ;
	 private  String CUSTOMERADDRESS3 ;
	 private  String CUSTOMERADDRESS4 ;
	 private  String CUSTOMERADDRESS5 ;
	 private  String CUSTOMERADDRESS6 ;
	 private  short INSTALLEDCUSTOMERADDRESS1 ;
	 private  short INSTALLEDCUSTOMERADDRESS2 ;
	 private  short INSTALLEDCUSTOMERADDRESS3 ;
	 private  short INSTALLEDCUSTOMERADDRESS4 ;
	 private  short INSTALLEDCUSTOMERADDRESS5 ;
	 private  short INSTALLEDCUSTOMERADDRESS6 ;
	 
	 
	
	private  short INSTALLEDCUSTOMERNAME ;
	 public short getINSTALLEDCUSTOMERNAME() {
		return INSTALLEDCUSTOMERNAME;
	}


	public void setINSTALLEDCUSTOMERNAME(short iNSTALLEDCUSTOMERNAME) {
		INSTALLEDCUSTOMERNAME = iNSTALLEDCUSTOMERNAME;
	}


	public short getINSTALLEDCUSTOMERNUMBER() {
		return INSTALLEDCUSTOMERNUMBER;
	}


	public void setINSTALLEDCUSTOMERNUMBER(short iNSTALLEDCUSTOMERNUMBER) {
		INSTALLEDCUSTOMERNUMBER = iNSTALLEDCUSTOMERNUMBER;
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


	public String getCUSTOMERADDRESS1() {
		return CUSTOMERADDRESS1;
	}


	public void setCUSTOMERADDRESS1(String cUSTOMERADDRESS1) {
		CUSTOMERADDRESS1 = cUSTOMERADDRESS1;
	}


	public String getCUSTOMERADDRESS2() {
		return CUSTOMERADDRESS2;
	}


	public void setCUSTOMERADDRESS2(String cUSTOMERADDRESS2) {
		CUSTOMERADDRESS2 = cUSTOMERADDRESS2;
	}


	public String getCUSTOMERADDRESS3() {
		return CUSTOMERADDRESS3;
	}


	public void setCUSTOMERADDRESS3(String cUSTOMERADDRESS3) {
		CUSTOMERADDRESS3 = cUSTOMERADDRESS3;
	}


	public String getCUSTOMERADDRESS4() {
		return CUSTOMERADDRESS4;
	}


	public void setCUSTOMERADDRESS4(String cUSTOMERADDRESS4) {
		CUSTOMERADDRESS4 = cUSTOMERADDRESS4;
	}


	public String getCUSTOMERADDRESS5() {
		return CUSTOMERADDRESS5;
	}


	public void setCUSTOMERADDRESS5(String cUSTOMERADDRESS5) {
		CUSTOMERADDRESS5 = cUSTOMERADDRESS5;
	}


	public String getCUSTOMERADDRESS6() {
		return CUSTOMERADDRESS6;
	}


	public void setCUSTOMERADDRESS6(String cUSTOMERADDRESS6) {
		CUSTOMERADDRESS6 = cUSTOMERADDRESS6;
	}


	public short getINSTALLEDCUSTOMERADDRESS1() {
		return INSTALLEDCUSTOMERADDRESS1;
	}


	public void setINSTALLEDCUSTOMERADDRESS1(short iNSTALLEDCUSTOMERADDRESS1) {
		INSTALLEDCUSTOMERADDRESS1 = iNSTALLEDCUSTOMERADDRESS1;
	}


	public short getINSTALLEDCUSTOMERADDRESS2() {
		return INSTALLEDCUSTOMERADDRESS2;
	}


	public void setINSTALLEDCUSTOMERADDRESS2(short iNSTALLEDCUSTOMERADDRESS2) {
		INSTALLEDCUSTOMERADDRESS2 = iNSTALLEDCUSTOMERADDRESS2;
	}


	public short getINSTALLEDCUSTOMERADDRESS3() {
		return INSTALLEDCUSTOMERADDRESS3;
	}


	public void setINSTALLEDCUSTOMERADDRESS3(short iNSTALLEDCUSTOMERADDRESS3) {
		INSTALLEDCUSTOMERADDRESS3 = iNSTALLEDCUSTOMERADDRESS3;
	}


	public short getINSTALLEDCUSTOMERADDRESS4() {
		return INSTALLEDCUSTOMERADDRESS4;
	}


	public void setINSTALLEDCUSTOMERADDRESS4(short iNSTALLEDCUSTOMERADDRESS4) {
		INSTALLEDCUSTOMERADDRESS4 = iNSTALLEDCUSTOMERADDRESS4;
	}


	public short getINSTALLEDCUSTOMERADDRESS5() {
		return INSTALLEDCUSTOMERADDRESS5;
	}


	public void setINSTALLEDCUSTOMERADDRESS5(short iNSTALLEDCUSTOMERADDRESS5) {
		INSTALLEDCUSTOMERADDRESS5 = iNSTALLEDCUSTOMERADDRESS5;
	}


	public short getINSTALLEDCUSTOMERADDRESS6() {
		return INSTALLEDCUSTOMERADDRESS6;
	}


	public void setINSTALLEDCUSTOMERADDRESS6(short iNSTALLEDCUSTOMERADDRESS6) {
		INSTALLEDCUSTOMERADDRESS6 = iNSTALLEDCUSTOMERADDRESS6;
	}


	 private InvoiceDataModelELS invoiceDataModelELS=null;
	 
	 private InvoiceELS invoiceELS = new InvoiceELS();
	 
	 
	 
	    
	    public CustomerSearchELS(String country)
	    {
	       this.setCOUNTRY(country);
	       invoiceELS.setCOUNTRY(country);
	       invoiceDataModelELS=new InvoiceDataModelELS(invoiceELS);
	       
	    }  
	    
	    
	    public CustomerSearchELS() {
			// TODO Auto-generated constructor stub
		}


		public InvoiceDataModelELS getInvoiceDataModelELS() {
			return invoiceDataModelELS;
		}
	    
	    public String getQUERYITEM() {
			return QUERYITEM;
		}


		public void setQUERYITEM(String qUERYITEM) {
			QUERYITEM = qUERYITEM;
		}
	
	
	 public ArrayList searchForCustomerNumber()
	    {
	     
		 
		   ArrayList list = new ArrayList();
		   ArrayList custList = new ArrayList();

	     
	        	try{
	        	
		        list=  getInvoiceDataModelELS().selectByCustomerStatement(getQUERYITEM(),null);
		        
		        custList= loadResults(list);
	        } catch (Exception e)
	        {
	         
	        	
	         logger.debug("Error in  searchForCustomerNumber()  CustomerSearchELS : " + e)	;
	       }

		      return  custList; 
		        
	    }

	    public ArrayList searchForCustomerName()
	    {
	        
	    	 ArrayList list = new ArrayList();
			 ArrayList custList = new ArrayList();
	    	
	        logger.debug("inside  searchForCustomerName, query item:" + getQUERYITEM()+"city"+getCUSTOMERADDRESS3());
	    	
	         try
	        {
	            
	            list=  getInvoiceDataModelELS().selectByCustomerNameStatement(getQUERYITEM() ,getCUSTOMERADDRESS3());
		        custList= loadResults(list);
	        } catch (Exception e)
	        {
	          
	            logger.debug("Error searching for customer name");
	        }
	         
	         return custList;
	    }
	
	
	
	
	
	
    public ArrayList loadResults(ArrayList aInvoiceList)
	    {
	       /* InvoiceDataModel aDataModel = (InvoiceDataModel) getDataModel();

	        TablePanel aTablePanel = ((TablePanel) getViewPanel());
	        aTablePanel.clear();*/
    	
    	   ArrayList custList = new ArrayList(); 
    	   CustomerSearchELS customerSearchELS;
    	   
    	   InvoiceELS invoiceELS;

	       for (int i = 0; i < aInvoiceList.size(); i++)
	        {
	          
	    	     	   
	    	   invoiceELS= (InvoiceELS) aInvoiceList.get(i);
	    	   customerSearchELS=new CustomerSearchELS();
	    	   customerSearchELS.setCUSTOMERNUMBER(invoiceELS.getCUSTOMERNUMBER());
	    	   customerSearchELS.setCUSTOMERNAME(invoiceELS.getCUSTOMERNAME());
	    	   customerSearchELS.setCUSTOMERADDRESS1(invoiceELS.getCUSTOMERADDRESS1());
	    	   customerSearchELS.setCUSTOMERADDRESS2(invoiceELS.getCUSTOMERADDRESS2());
	    	   customerSearchELS.setCUSTOMERADDRESS3(invoiceELS.getCUSTOMERADDRESS3());
	    	   customerSearchELS.setCUSTOMERADDRESS4(invoiceELS.getCUSTOMERADDRESS4());
	    	   customerSearchELS.setCUSTOMERADDRESS5(invoiceELS.getCUSTOMERADDRESS5());
	    	   
	    	   custList.add(customerSearchELS);
	    	   
	    	   
	        }

	        if ((aInvoiceList == null) || (aInvoiceList.size() == 0))
	        {
	           // getViewFrame().setVisible(false);
	            //fireActionPerformed("Customer Not Found");
	        	
	        	logger.debug("Customer Not Found");
	        } else if (aInvoiceList.size() == 1)
	        {
	            // select this row
	           //aTablePanel.selectItem(((CustomerSearchFrame) getViewFrame()).getJScrollPane1(), aTablePanel.getModel(), (InvoiceDataModel) aInvoiceList.get(0));

	            // use this record
	           // getViewFrame().setVisible(false);
	          //fireActionPerformed("Customer Selected");
	            logger.debug("Customer Selected");
	        } else
	        {
	            // more than one result, make the choice list
	           // displayCustomerSearchWindow(aInvoiceList);
	        }
	        
	        return custList ;

	    }
	    

}
