package com.ibm.igf.gil;

public class CountryProperties {
	
	private String country;
	
	private String countryName;
	
	private String ibmCountryCode;
	
	private String ibmEnterpriseNum;
	
	private String gcmsDbName;
	
	private String gcmsSchema;
	
	private String aptsDbName;
	
	private String aptsSchema;
	
	private String icfsDbName;
	
	private String icfsSchema;
	
	private String miscSchema;
	
	private String rdcUseAddrSeqNum;
	
	private String rdcUseSeqNum;
	
	private String gaptsJndi;
	
	private String icfsJndi;
	
	private String hybrSchema;
	
	private String hybrJndi;
	
	
	public String getAptsSpringTransactionManagerId() {
		
		if (gaptsJndi!=null)
			return gaptsJndi  + "TransactionManager";
		else
			return null;
	}
	
	public String getIcfsSpringTransactionManagerId() {
		
		if (icfsJndi!=null)
			return icfsJndi  + "TransactionManager";
		else
			return null;
	}
	

	public String getAptsSpringDataSourceId() {
		
		if (gaptsJndi!=null)
			return gaptsJndi  + "DataSource";
		else
			return null;
	}
	

	public String getIcfsSpringDataSourceId() {
		if (icfsJndi!=null)
			return icfsJndi  + "DataSource";
		else
			return null;
	}


	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	public String getHybrSchema() {
		return hybrSchema;
	}

	public void setHybrSchema(String hybrSchema) {
		this.hybrSchema = hybrSchema;
	}

	public String getHybrJndi() {
		return hybrJndi;
	}

	public void setHybrJndi(String hybrJndi) {
		this.hybrJndi = hybrJndi;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getIbmCountryCode() {
		return ibmCountryCode;
	}

	public void setIbmCountryCode(String ibmCountryCode) {
		this.ibmCountryCode = ibmCountryCode;
	}

	public String getIbmEnterpriseNum() {
		return ibmEnterpriseNum;
	}

	public void setIbmEnterpriseNum(String ibmEnterpriseNum) {
		this.ibmEnterpriseNum = ibmEnterpriseNum;
	}

	public String getGcmsDbName() {
		return gcmsDbName;
	}

	public void setGcmsDbName(String gcmsDbName) {
		this.gcmsDbName = gcmsDbName;
	}

	public String getGcmsSchema() {
		return gcmsSchema;
	}

	public void setGcmsSchema(String gcmsSchema) {
		this.gcmsSchema = gcmsSchema;
	}

	public String getAptsDbName() {
		return aptsDbName;
	}

	public void setAptsDbName(String aptsDbName) {
		this.aptsDbName = aptsDbName;
	}

	public String getAptsSchema() {
		return aptsSchema;
	}

	public void setAptsSchema(String aptsSchema) {
		this.aptsSchema = aptsSchema;
	}

	public String getMiscSchema() {
		return miscSchema;
	}

	public void setMiscSchema(String miscSchema) {
		this.miscSchema = miscSchema;
	}

	public String getIcfsDbName() {
		return icfsDbName;
	}

	public void setIcfsDbName(String icfsDbName) {
		this.icfsDbName = icfsDbName;
	}

	public String getIcfsSchema() {
		return icfsSchema;
	}

	public void setIcfsSchema(String icfsSchema) {
		this.icfsSchema = icfsSchema;
	}

	public String getRdcUseAddrSeqNum() {
		return rdcUseAddrSeqNum;
	}

	public void setRdcUseAddrSeqNum(String rdcUseAddrSeqNum) {
		this.rdcUseAddrSeqNum = rdcUseAddrSeqNum;
	}

	public String getRdcUseSeqNum() {
		return rdcUseSeqNum;
	}

	public void setRdcUseSeqNum(String rdcUseSeqNum) {
		this.rdcUseSeqNum = rdcUseSeqNum;
	}

	public String getGaptsJndi() {
		return gaptsJndi;
	}

	public void setGaptsJndi(String gaptsJndi) {
		this.gaptsJndi = gaptsJndi;
	}

	public String getIcfsJndi() {
		return icfsJndi;
	}

	public void setIcfsJndi(String icfsJndi) {
		this.icfsJndi = icfsJndi;
	}
	
	public String toString() {
		
		return 
		"Country Properties:" +	this.getCountry() + 
		" ### COUNTRY_NAME:" +  this.getCountryName() +   
		" ### IBM_COUNTRY_CODE:" +  this.getIbmCountryCode() + 
		" ### IBM_ENTERPRISE_NUM:" +  this.getIbmEnterpriseNum() + 
		" ### GCMS_SCHEMA:" +  this.getGcmsSchema() + 
		" ### APTS_SCHEMA:" +  this.getAptsSchema()   + 
		" ### ICFS_SCHEMA:" +  this.getIcfsSchema() + 
		" ### HYBR_SCHEMA:" +  this.getHybrSchema() + 
		" ### MISC_SCHEMA:" +  this.getMiscSchema() + 
		" ### RDC_USE_ADDR_SEQ_NO:" +  this.getRdcUseAddrSeqNum() + 
		" ### RDC_USE_SEQ_NO:" +  this.getRdcUseSeqNum() + 
		" ### GAPTS_JNDI:" +  this.getGaptsJndi() + 
		" ### ICFS_JNDI:" +  this.getIcfsJndi() + 
		" ### HYBR_JNDI:" +  this.getHybrJndi() + 		
		" ### APTS_SPRING_DATASOURCE_ID:" +  this.getAptsSpringDataSourceId() + 
		" ### APTS_SPRING_TRANSACTION_MANAGER_ID:" +  this.getAptsSpringTransactionManagerId() + 
		" ### ICFS_SPRING_DATASOURCE_ID:" +  this.getIcfsSpringDataSourceId()+
		" ### ICFS_SPRING_TRANSACTION_MANAGER_ID:" +  this.getIcfsSpringTransactionManagerId();	
	}
}