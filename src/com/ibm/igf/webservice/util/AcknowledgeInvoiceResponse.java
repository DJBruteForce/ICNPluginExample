package com.ibm.igf.webservice.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ibm.xmlns.ims._1_3.acknowledgeinvoice_2005_03_04.AcknowledgeInvoice;
import com.ibm.xmlns.ims._1_3.acknowledgeinvoice_2005_03_04.Application;
import com.ibm.xmlns.ims._1_3.acknowledgeinvoice_2005_03_04.Status;

public class AcknowledgeInvoiceResponse {
	private AcknowledgeInvoice ackInvoice=null;

	public AcknowledgeInvoiceResponse(AcknowledgeInvoice ackInvoice) {
		super();
		this.ackInvoice = ackInvoice;
	}
	
	
	
	public List<String> getErrorCodes() {
		List<String> errorCodes = new ArrayList<String>();
		
		Iterator<?> appItr = ackInvoice.getApplicationArea().getApplication().iterator();
		while (appItr.hasNext()) {
			Object obj1 = appItr.next();
			if (obj1 instanceof Application) {
				Application application = (Application) obj1;
				Iterator<?> appStatusItr = application.getApplicationStatus().iterator();
				while (appStatusItr.hasNext()) {
					Object obj2 = appStatusItr.next();
					if (obj2 instanceof Status) {
						Status appStatus = 
							(Status) obj2;
						errorCodes.add(appStatus.getErrorCode().getValue());
					}
				}
			}
		}
		
		return errorCodes;
	}
	
}
