package com.ibm.gil.exception;

import java.util.ArrayList;
import java.util.List;

import com.ibm.gil.util.UtilGilConstants;
 
public class GILCNException  extends Exception {

	private static final long serialVersionUID = 1L;
	private Throwable wrappedException=null;
	private List<String> errorCodes=null;

	public GILCNException(String message) {
        super(message);
        errorCodes=new ArrayList<String>();
    }
	
	public  GILCNException(String message, Throwable wrappedException) {
		super(message+ UtilGilConstants.GIL_MSG_EXC);
		this.wrappedException=wrappedException;
		 errorCodes=new ArrayList<String>();
	}

	public List<String> getErrorCodes() {
		return errorCodes;
	}

	public void setErrorCodes(List<String> errorCodes) {
		this.errorCodes = errorCodes;
	}

	public Throwable getWrappedException() {
		return wrappedException;
	}

	public void setWrappedException(Throwable wrappedException) {
		this.wrappedException = wrappedException;
	}
	
	
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return super.toString()
			+ ((this.getWrappedException() != null)
				? getWrappedException().getMessage()
				: "");
	}
	
	
}
