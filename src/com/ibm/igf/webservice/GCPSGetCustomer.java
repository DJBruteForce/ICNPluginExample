/*
 * Created on Mar 4, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ibm.igf.webservice;

import java.util.ArrayList;





import com.ibm.gil.business.InvoiceELS;
import com.ibm.igf.webservice.rdc.RDcWebServiceClient;

/**
 * @author ajimena
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class GCPSGetCustomer extends GCPSWebService {
	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(GCPSGetCustomer.class);
    RDcWebServiceClient rdc = new RDcWebServiceClient();

    public ArrayList getCustomer(InvoiceELS aInvoiceDM)
    {
        ArrayList returnList = new ArrayList();

        try
        {
            returnList = rdc.getCustomerData(aInvoiceDM);
            logger.debug("inside GCPSGetCustomer ");
           
        } catch (Exception exc)
        {
            logger.fatal(exc.toString());
        }
        
        logger.debug("list length:" + returnList.size() );

        return returnList;

    }

}
