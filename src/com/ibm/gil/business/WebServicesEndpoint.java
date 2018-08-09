package com.ibm.gil.business;


public class WebServicesEndpoint extends FormSelect{
	
	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(WebServicesEndpoint.class);
	private String system;
	
	private String connectionId;
	
	private String access;
	
	private boolean defaultEp;
	
	public boolean isDefaultEp() {
		return defaultEp;
	}

	public void setDefaultEp(boolean defaultEp) {
		this.defaultEp = defaultEp;
	}

	public String getWSUrl() {
		logger.debug("URL ws: "+value);
		return value;
	}
	
	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public String getFunctionalId() {
		return connectionId;
	}

	public void setFunctionalId(String functionalId) {
		this.connectionId = functionalId;
	}

	public WebServicesEndpoint(String value){
		
		this.value = value;
	}
	
	public WebServicesEndpoint(){
		
	}
	
	
    @Override
    public int compareTo(FormSelect otherObject) {
        return this.value.compareTo(otherObject.value);
    }

	public String getFunctionId() {
		return connectionId;
	}

	public void setFunctionId(String functionId) {
		this.connectionId = functionId;
	}

	public String getAccess() {
		return access;
	}

	public void setAccess(String access) {
		this.access = access;
	}
    
	public String toString() {
		
		return getLabel() + " - " + getValue();
		
	}
    
}
