package com.ibm.gil.util;

/**
 * @author ajimena
 * 
 */
public class UtilGilConstants {

	public static final String GET_HISTORY_LOG = "getHistoryLog";
	public static final String LOCK_ITEM = "lockItem";
	public static final String UNLOCK_ITEM = "unlockItem";
	public static final String GET_APPLICATION_SERVER_OFFSET = "getServerOffset";

	public static final String RETRIEVE_CONTAINING_FOLDERS = "retrieveContainingFolders";
	public static final String ADD_TO_FOLDER = "addToFolder";
	public static final String REMOVE_FROM_FOLDER = "removeFromFolder";

	public static final String INDEX = "index";

	public static final String SAVE = "save";

	public static final String RECALCULATE_INVAMT = "recalculateInvAmt";

	

	public static final String SEARCH_BYOL = "searchByOffering";

	public static final String SEARCH_BYOL_CUST_NAME = "searchByCustomerName";

	public static final String SEARCH_BY_INST_CUSTNAME = "searchByInstalledCustomerName";

	public static final String SEARCH_BY_INST_CUSTNUM = "searchByInstalledCustomer";

	public static final String VALIDATE_INPUT = "validateInput";

	public static final String VALIDATE_ON_SAVE = "validateOnSave";

	public static final String ROLLBACK_INDEXING = "rollbackIndexing";

	public static final String ROLLBACK_COUNTRY_INDEXING = "rollbackCountryIndexing";

	public static final String ADD_CONTRACT = "addContract";

	public static final String DEFAULTS = "defaults";

	public static final String SEARCH_BY_VENDOR_NUMBER = "getAllByVendorNumber";

	public static final String SEARCH_BY_VENDOR_NAME = "getAllByVendorName";

	public static final String SEARCH_BY_ACCOUNT = "getAllByAccount";

	public static final String SEARCH_REF_INV = "searchByReferenceInvoice";

	public static final String SAVE_STATE = "saveState";
	
	public static final String REMOVE_QLINEITEMS = "removeQuoteLineItems";
	
	public static final String GET_ENDPOINTS = "getEndpoints";
	
	public static final String SET_ENDPOINT = "setEndpoint";

	// public static final String BUILD_LINE_ITEMS="buildLineItems";

	

		
	public static final String SPREAD_TAX_ROUNDING_ERROR = "spreadTaxRoundingError";
	
	public static final String RENUMBER_AND_SPREAD ="renumberAndSpread";
	
	public static final String RENUMBER_LINE_ITEMS = "renumberLineItems";

	public static final String ADD_SERIAL = "addSerial";

	public static final String IMPORT_SERIAL = "importSerial";
	
	public static final String IMPORT_PARTS = "importParts";
	
	public static final String VALIDATE_SAVE_LINE_ITEMS = "revalidateInput";
	
	public static final String SELECT_TM_DESC ="lookupTypeModelDescription";
	
	public static final String GET_SERIALS = "getSerials";

	public static final String VALIDATE_SERIAL = "validateSerial";

	public static final String GIL_MSG_EXC = "\n Check GIL CN logs for further information.";

	public static final String IS_FROM_APTS = "Invoice is from APTS and can't be indexed to GCMS";

	public static final String DUPLICATE_INVNUM = "Duplicate Invoice Number / Invoice Date entered";

	public static final String INIT = "initialize";

	public static final String SUCCESS_CTS = "Successfull response from CTS";

	public static final String FAIL_CTS = "Failed response from CTS";

	public static final String GCMS_SERVER = "gcmsServer";

	public static final String GCMS_User = "gilGcmsUser";

	public static final String GCMS_ACCESS = "gilGcmsPw";

	public static final String GILCN_SCHEMA = "gilSchema";

	public static final String ICFS_DEV_SCHEMA = "icfsDevSchema";

	public static final String RDC_SERVER = "rdcServer";
	
	public static final String INDIA_GST_EFFECTIVE_DATE="inGSTEffDate";

	public static final String PROCESSINVOICE_WS = "processinvoice.wss";

	public static final String GETINVOICE_WS = "getinvoice.wss";

	public static final String GETOL_WS = "getofferingletter.wss";

	public static final String UPDATECOA_WS = "updatecoa.wss";

	public static final String GETCOA_WS = "getcoa.wss";

	public static final String UPDATEOL_WS = "updateofferingletter.wss";

	public static final String CREATEOL_WS = "autocreateofferletter.wss";

	public static final String CTS_WS = "ctswebservice.wss";

	public static final int ONE = 1;
	public static final String CTS_FIN_TYPE = "FMV";
	public static final String CTS_TRANS_TYPE = "NLSE";
	public static final String CTS_GENERIC_MATERIAL_CODE = "01A 16";
	public static final String CTS_US_COUNTRY_CODE = "896";
	public static final String CTS_SUBSIDAIRY_CODE = "01";
	public static final String CTS_US_CURRENCY_CODE = "USD";
	public static final String APPLICATION_NAME = "GIL";
	public static final String CTS_ID = "1";
	public static final String YES_FLAG = "Y";

	public static final String DECODE_PASSWORD = "Y2hhbmdlaXQ=";
	public static final String USTAX_CALC="usTaxCalculation";
	
	
	
	public static final String SEARCH_BYINVOICE="searchByInvoice";
	
	
//	public static final String BUILD_LINE_ITEMS="buildLineItems";
	
	public static final String INIT_LINE_ITEMS="initLineItems";
	
	public static final String VALIDATE_INPUT_LN_ITEMS="validateOnServer";
	
	public static final String ADD_TYPE_MODEL="addTypeAndModel";
	
	public static final String CREATEMISC_WS = "DocumentMonitorWebService.wss";	
	public static final String RETURN_CODE_00 = "00";
	public static final String RETURN_CODE_01 = "01";
	public static final String RETURN_CODE_02 = "02";
	public static final String REQUEST_TYPE = "INQUIRY";
	public static final String SUB_TAB_NUM = "C11";
	public static final String ITEM_ID = "TAXATION_OF_LEASE";
	public static final String EMPTY_STRING = "";
	
	public static final String REGION ="region";
	
	public static final String LOG_JSON_REQUEST = "JSON Request Data";
	public static final String LOG_JSON_RESPONSE = "JSON Response Data";
	

}
