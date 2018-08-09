package com.ibm.gil.model;



import com.ibm.gil.business.MiscELS;

public class MiscDataModelELS extends DataModel {

	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(COADataModelELS.class);
	
	private MiscELS miscELS;
	
	public MiscDataModelELS(MiscELS miscELS){
		super(miscELS);
		this.miscELS = miscELS;
		logger.debug("Initializing MiscDataModelELS initialized successfully");
	}
	
	public MiscELS getMiscELS(){
		return this.miscELS;
	}

}
