/*
 * Created on Aug 6, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ibm.gil.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import javax.naming.NamingException;

//import java.util.List;

//import java.util.TimeZone;

//import com.ibm.gil.service.OfferingLetterService;

//import com.ibm.igf.gil.IndexingDataModel;



import com.ibm.gil.business.OfferingLetter;
//import com.ibm.gil.model.APTSIndexingDataModel;
import com.ibm.gil.util.GilUtility;
//import com.ibm.igf.gil.CountryProperties;
//import com.ibm.igf.hmvc.CountryManager;
//import com.ibm.igf.gil.Debugger;
//import com.ibm.igf.gil.IndexingDataModel;
//import com.ibm.gil.util.RegionalBigDecimal;
import com.ibm.gil.util.RegionalDateConverter;
import com.ibm.igf.gil.CountryProperties;
import com.ibm.igf.hmvc.CountryManager;
import com.ibm.igf.hmvc.ResultSetCache;
import com.ibm.mm.sdk.server.DKDatastoreICM;
import com.sun.rowset.CachedRowSetImpl;
/**
 * @author SteveBaber
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
//public class OfferingLetterDataModel extends APTSIndexingDataModel {

public class OfferingLetterDataModel extends DataModel {
/*public OfferingLetterDataModel(int count) {
		super(count);
		// TODO Auto-generated constructor stub
	}*/

	
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(OfferingLetterDataModel.class);

	
	

	OfferingLetter offeringLetter= null;
	
	
	public OfferingLetter getOfferingLetter() {
		return offeringLetter;
	}

	public void setOfferingLetter(OfferingLetter offeringLetter) {
		this.offeringLetter = offeringLetter;
	}

	private static transient ResultSetCache currencyResults = null;
	
	
	// below lines created by Krishna
	String[] values = null;
	
	 public void setValues(String[] vValues)
	    {
	        values = vValues;
	    }

	    public String[] getValues()
	    {
	        return values;
	    }
	
	
	
	
public OfferingLetterDataModel(OfferingLetter offeringLetter)  {
		
		super(offeringLetter);
		this.offeringLetter = offeringLetter;
		
	}
  

    
 
    
    
    private boolean isValidOLChange() throws SQLException, NamingException{
    	//OfferingLetterDataModel aDataModel = (OfferingLetterDataModel) getDataModel();
   	    //ResultSet results = aDataModel.selectContractNumber();
    	
   	    ResultSet results = selectContractNumber();
   	    boolean cntNumExist = false;
   	    if(results.next()){
   	    	getOfferingLetter().setCONTRACTNUMBER(results.getString(1).trim());
   	    	cntNumExist = true;
   	    }
   	    
   	    if(cntNumExist){
   	    	results = selectPaymentDate();
   	    	if(results.next()){
   	    		String payDate = results.getString(1).trim();
   	    		if(!(payDate == null || payDate.equals("")) ){
   	    			return false;
   	    		}
   	    	}
   	    }
 
    	
    	return true;
    }
    
    
    
    public void saveDocument() throws NamingException
    {
        try
        {
        	//below line commented by Krishna
           // beginTransaction();
        	
        	logger.debug("inside saveDocument of OfferingLetterDataModel"); 
        	logger.debug("Document mode in saveDocument:"+getOfferingLetter().getDOCUMENTMODE());
           if (getOfferingLetter().getDOCUMENTMODE().equals(getOfferingLetter().getADDMODE()))
           {
        	    logger.debug("inside saveDocument of OfferingLetterDataModel ADDMODE");
                insertDocument();
           } else
            {
        	   logger.debug("inside saveDocument of OfferingLetterDataModel updatemode");
                updateDocument();
            }
         //below line commented by Krishna
           // commitTransaction();
        } catch (Exception exc)
        {
        	//below block commented by Krishna
            try
            {
            	           	
                //rollbackTransaction();
               
            	logger.debug("Exception in save document is :"+exc);
            } catch (Exception e)
            {
            	logger.debug("saveDocument " + e.toString() + "\n" + e.getMessage() + "\n");
            }
        }
    }

    public void createOfferingFolder() throws Exception {
       
    	try {
    	
	        String[] fields = { "Offering Letter Number" };
	        String[] values = {getOfferingLetter().getOFFERINGNUMBER() };
	       
	        String folderXML = getContentManager().executeQuery("Offering Letter Folder " + getOfferingLetter().getCOUNTRY(), fields, values,getOfferingLetter().getDatastore());
	        String folderId =getOfferingLetter().getObjectId(folderXML);
	        String objectId=getOfferingLetter().getOBJECTID();
	
	        String[] s = new String[0];
	        ArrayList newfields = new ArrayList();
	        ArrayList newvalues = new ArrayList();
	             
	        copyAttributes(getOfferingLetter().getOBJECTID(), getOfferingLetter().getINDEXCLASS(), folderId, "Offering Letter Folder " + getOfferingLetter().getCOUNTRY(), newfields, newvalues,getOfferingLetter().getCOUNTRY(),getOfferingLetter().getDatastore());
	
	        if (folderId == null || folderId.trim().length() == 0)  {
	        	folderId = getContentManager().createFolder("Offering Letter Folder " + getOfferingLetter().getCOUNTRY(), (String[]) (newfields.toArray(s)), (String[]) (newvalues.toArray(s)),getOfferingLetter().getDatastore());
	        } else {
	        	 getContentManager().updateDocument(folderId, (String[]) (newfields.toArray(s)), (String[]) (newvalues.toArray(s)), getOfferingLetter().getDatastore());
	        }
	
	        getOfferingLetter().setFOLDERID(folderId);
        
    	} catch (Exception e) {
    		logger.error(e);
    	}
    }

    public String selectFolderId(DKDatastoreICM datastore) throws Exception
    {
        String[] fields = { "Offering Letter Number" };
        String[] values = { getOfferingLetter().getOLDOFFERINGNUMBER() };
        
        String folderXML = getContentManager().executeQuery("Offering Letter Folder " +getOfferingLetter().getOLDCOUNTRY(), fields, values,datastore);
        String folderId = getOfferingLetter().getObjectId(folderXML);
        getOfferingLetter().setFOLDERID(folderId);       
       
        return  folderId;
    }

    public void insertDocument()
    {
        try
        {

             createOfferingFolder();
             
            if (getOfferingLetter().getFOLDERID() == null || getOfferingLetter().getFOLDERID().trim().length() == 0)  {
               logger.debug("Error creating offering letter folder");
               getOfferingLetter().setValidationIsContractFolderCreated(false);
               return;
            }

             getContentManager().moveDocumentToFolder(getOfferingLetter().getOBJECTID(), getOfferingLetter().getFOLDERID(),getOfferingLetter().getDatastore());

             insertOfferingStatement();
            
            // remove items from the to be index worklist
            continueWorkflow();

        } catch (Exception exc) {

           exc.printStackTrace();
           logger.debug("exception in insertDocument():"+exc);
           return;
        }

        getOfferingLetter().setDIRTYFLAG(Boolean.FALSE);
        getOfferingLetter().setDOCUMENTMODE(OfferingLetter.UPDATEMODE);

    }

    public void updateDocument()
    {
        try
        {

        	if(!getOfferingLetter().getCOUNTRY().equals(getOfferingLetter().getOLDCOUNTRY())){
        		updateOfferingLetterCountry();
        	} 

            if (!getOfferingLetter().getOLDOFFERINGNUMBER().equals(getOfferingLetter().getOFFERINGNUMBER()))
            {
                updateInvoiceStatement();
                updateContractStatement();
                updateOfferingDetailsStatement();
            }
            
            // move the Offer Letter into a Offer Letter folder
            createOfferingFolder();
			if (getOfferingLetter().getFOLDERID() == null || getOfferingLetter().getFOLDERID().trim().length() == 0) {
				logger.error("Error creating Offer Letter folder");
				getOfferingLetter().setValidationIsContractFolderCreated(false);
				return;
			}
			
			logger.debug("folderId in update document :" +getOfferingLetter().getFOLDERID());
			getContentManager().moveDocumentToFolder(getOfferingLetter().getOBJECTID(),getOfferingLetter().getFOLDERID(),getOfferingLetter().getDatastore());
			

			// find any matching contract control records
            ResultSet results = selectContractStatement();
            logger.debug("inside updateDocument() after results");
            while (results.next())
            {
            	logger.debug("inside updateDocument() after results inside while loop");
            	getOfferingLetter().setCONTRACTOBJECTID(getDatabaseDriver().getString(results, 1));
                updateRelatedIndexValues();
               
            }
            // find any matching invoice control records
            results = selectInvoiceStatement();
            while (results.next())
            {
            	getOfferingLetter().setINVOICEOBJECTID(getDatabaseDriver().getString(results, 1));
                updateRelatedIndexValues();
            }
            // already exists, update it
            updateOfferingStatement();

        } catch (Exception exc)
        {
        	logger.debug("exception in updateDocument()  :" +exc);
            return;

        }

       getOfferingLetter().setDIRTYFLAG(Boolean.FALSE);

    }

	public void rollbackIndexing() {
		// load the Offering if it exists
		try {
			ResultSet results = selectByObjectIdStatement(getOfferingLetter()
					.getOBJECTID(), getOfferingLetter().getCOUNTRY());
			if (results.next()) {
				// restore the old fields
				getOfferingLetter().setOFFERINGNUMBER(
						getDatabaseDriver().getString(results, 1));
				getOfferingLetter().setCUSTOMERNUMBER(
						getDatabaseDriver().getString(results, 5));
				getOfferingLetter().setCUSTOMERNAME(
						getDatabaseDriver().getString(results, 6));
				String offerDate = getDatabaseDriver().getString(results, 7);
				logger.debug("offerDate in rollbackIndexing: " + offerDate);
				offerDate = RegionalDateConverter.convertDate("DB2", "GUI",
						offerDate);
				getOfferingLetter().setOFFERDATE(offerDate);
			}
		} catch (Exception exc) {
			logger.debug("exception in rollbackIndexing():" + exc);
		}
		updateIndexValues();
	}

    /**
     * sync up content manager with any changes from the gui
     */
	public void updateIndexValues() {
		try {

			String objectId = getOfferingLetter().getOBJECTID();
			String folderId = getOfferingLetter().getFOLDERID();
			String offerLetterDate = RegionalDateConverter.convertDate("GUI", "CM", getOfferingLetter().getOFFERDATE());
			logger.debug("offerdate in updateIndexValues:"+ offerLetterDate);

			if (!getOfferingLetter().getCOUNTRY().equals(getOfferingLetter().getOLDCOUNTRY())) {
				getContentManager().moveDocument(getOfferingLetter().getFOLDERID(),"Offering Letter Folder "+ getOfferingLetter().getCOUNTRY(),getOfferingLetter().getDatastore());
			}
			
			if(folderId==null){
				folderId= selectFolderId(getOfferingLetter().getDatastore());
			}
			
			String[] fields = { "Offering Letter Number", "Customer Name","Customer Number", "Offering Letter Date" };
			String[] values = { getOfferingLetter().getOFFERINGNUMBER(),getOfferingLetter().getCUSTOMERNAME(),getOfferingLetter().getCUSTOMERNUMBER(),offerLetterDate };
			
			if (folderId!=null && folderId.trim().length() > 0) {
				boolean retry = false;
				do {

					boolean rc = getContentManager().updateDocument(folderId,fields, values, getOfferingLetter().getDatastore());

					if (!rc) {
					} else {
						retry = false;
					}
				} while (retry);
			}
			if (objectId.trim().length() > 0) {
				boolean retry = false;
				do {

					boolean rc = getContentManager().updateDocument(objectId,fields, values, getOfferingLetter().getDatastore());

					if (!rc) {
						
					} else {
						retry = false;
					}
				} while (retry);
			}
		} catch (Exception exc) {
			logger.debug("exception in updateIndexValues(): " + exc);
		}
	}
    
	public void rollbackCountryIndexing() {
		try {
			String objectId = getOfferingLetter().getOBJECTID();

			if (objectId.trim().length() > 0) {
				boolean retry = false;
				do {

					boolean rc = getContentManager().moveDocument(
							objectId,
							"Offering Letter "
									+ getOfferingLetter().getCOUNTRY(),
							getOfferingLetter().getDatastore());
					if (!rc) {
						// retry =
						// getEventController().prompt("Cannot update Content Manager index values.\nPlease close the document image and check-in.\nRetry?");
					} else {
						retry = false;
					}
				} while (retry);
			}
		} catch (Exception exc) {
			logger.debug("exception in rollbackIndexing():" + exc);
		}
	}    

	/**
	 * sync up content manager with any changes from the gui
	 */
	public void updateRelatedIndexValues() {
		try {
			// update the indexes
			String[] fields = { "Offering Letter Number" };
			String[] values = { getOfferingLetter().getOFFERINGNUMBER() };
			if (getOfferingLetter().getCONTRACTOBJECTID().trim().length() > 0) {
				boolean retry = false;
				do {

					boolean rc = getContentManager().updateDocument(
							getOfferingLetter().getCONTRACTOBJECTID(), fields,
							values, getOfferingLetter().getDatastore());
					logger.debug("in updateRelatedIndexValues ,rc is :" + rc);
					if (!rc) {
						// retry =
						// getEventController(OL.getCOUNTRY()).prompt("Cannot update Content Manager index values.\nPlease close the document image and check-in.\nRetry?");
					}
				} while (retry);
			}
			if (getOfferingLetter().getINVOICEOBJECTID().trim().length() > 0) {
				boolean retry = false;
				do {

					boolean rc = getContentManager().updateDocument(
							getOfferingLetter().getINVOICEOBJECTID(), fields,
							values, getOfferingLetter().getDatastore());
					if (!rc) {
						// retry =
						// getEventController().prompt("Cannot update Content Manager index values.\nPlease close the document image and check-in.\nRetry?");
					}
				} while (retry);

			}
		} catch (Exception exc) {
			logger.debug("exception in updateRelatedIndexValues():" + exc);
		}
	}

	/**
	 * create an insert statement to presist the Offering into Offering conrtol
	 * Creation date: (3/13/00 11:44:07 AM)
	 * 
	 * @throws ParseException
	 * @throws IllegalArgumentException
	 * @throws NamingException
	 */
	public void insertOfferingStatement() throws SQLException,
			IllegalArgumentException, ParseException, NamingException {
		java.util.Date now = new java.util.Date();
		StringBuffer sql = new StringBuffer();

		CountryProperties countryProperties = CountryManager
				.getCountryProperties(getOfferingLetter().getCOUNTRY());
		String schema = countryProperties.getAptsSchema();
		
		Connection conn = null;
		Statement stmt=null;

		// clip the username
		// String username = trimUSERID();
		String username = getOfferingLetter().getUSERID();

		// build the sql statement, use append because it is more efficient
		sql.append("insert into ");
		sql.append(schema);
		sql.append("OFFERCTL values (");
		sql.append("'");
		sql.append(getOfferingLetter().getOFFERINGNUMBER());
		sql.append("'");
		sql.append(",");
		sql.append("'");
		sql.append(getOfferingLetter().getCOUNTRY());
		sql.append("'");
		sql.append(",");
		sql.append("'");
		sql.append((getOfferingLetter().getCURRENCY()));
		sql.append("'");
		sql.append(",");
		sql.append(getOfferingLetter().getAMOUNT().replace(',', '.'));
		sql.append(",");
		sql.append(getOfferingLetter().getAMOUNT().replace(',', '.'));
		sql.append(",");
		sql.append("'");
		sql.append(username);
		sql.append("'");
		sql.append(",");
	 
		sql.append("'");
		sql.append(RegionalDateConverter.formatDate("GUI-"+getOfferingLetter().getLocale().toString(), now));
		sql.append("'");

		sql.append(",");
		sql.append("'");
		sql.append(RegionalDateConverter.formatTime("GUI-"+getOfferingLetter().getLocale().toString(), now));
		sql.append("'");
		sql.append(",");
		sql.append("'");
		sql.append(getOfferingLetter().getCUSTOMERNUMBER());
		sql.append("'");
		sql.append(",");
		sql.append("'");
		sql.append(getOfferingLetter().getCUSTOMERNAME());
		sql.append("'");
		sql.append(",");
		sql.append("''");
		sql.append(",");		
		sql.append(getSqlString(RegionalDateConverter.formatDate("DB2", GilUtility.getCreatedTimeStampAsDate(getOfferingLetter().getCreatedTimeStamp()))));

		sql.append(")");

		logger.debug("sql inside insertofferingstatement is :" + sql);

		// prepare the statement for execution
		try {

			conn = getDataSourceTransactionManager().getDataSource()
					.getConnection();

			 stmt = conn.createStatement();

			int i = stmt.executeUpdate(sql.toString());

			logger.debug("i in the first block is :" + i);

		} catch (SQLException exc) {
			logger.debug("exception in first try insertOfferingStatement(OfferingLetter OL) :"
					+ exc);
			throw exc;
		}
		logger.debug("insert offerctlc");
		logger.debug("locale: "+getOfferingLetter().getLocale().toString());
		logger.debug("getcreateddate"+ getOfferingLetter().getCREATEDATEasDate());
		logger.debug("offering letter date: "+ getOfferingLetter().getOFFERDATE());
		
		
		sql.setLength(0);
		sql.append("insert into ");
		sql.append(schema);
		sql.append("offerctlc values (");
		sql.append("'");
		sql.append(getOfferingLetter().getOFFERINGNUMBER());
		sql.append("'");
		sql.append(",");
		sql.append("'");
		sql.append(getOfferingLetter().getCOUNTRY());
		sql.append("'");
		sql.append(",");		
		sql.append("'");
		sql.append(getOfferingLetter().getOBJECTID());
		sql.append("'");
		sql.append(",");
		
	
		if( getOfferingLetter().getOFFERDATE().equals("")){

			sql.append("'0001-01-01'");
		}else{
		sql.append(getSqlString(RegionalDateConverter.convertDate("GUI", "DB2", getOfferingLetter().getOFFERDATE(),this.getOfferingLetter().getUserTimezone())));
		}
		
		sql.append(")");

		// prepare the statement for execution
		try {

			conn = getDataSourceTransactionManager().getDataSource()
					.getConnection();
			stmt = conn.createStatement();

			logger.debug("second sql is :" + sql.toString());

			int i = stmt.executeUpdate(sql.toString());

			logger.debug("value of i in insert offering statement:" + i);

		} catch (SQLException exc) {
			logger.debug("exception in second try insertOfferingStatement(OfferingLetter OL) :"
					+ exc);
			throw exc;
		} finally {
			conn.close();
			stmt.close();
		}

	}

    /**
     * create a select statement for retieving the Offering control data by
     * Offering number / country code Creation date: (3/13/00 11:44:07 AM)
     * 
     * @return ResultSet
     * @throws NamingException 
     */
    public ResultSet selectByOfferingStatement() throws SQLException, NamingException
    {
        StringBuffer sql = new StringBuffer();
        
     
        CountryProperties countryProperties = CountryManager.getCountryProperties(getOfferingLetter().getCOUNTRY());
        String schema = countryProperties.getAptsSchema();
        
        CachedRowSetImpl cachedRs = new CachedRowSetImpl();
        Connection conn =null;
        PreparedStatement pstmt=null;

        // build the sql statement, use append because it is more efficent
        sql.append("select A.ofcoffer#,A.ofcoframt,A.ofcofrbal,B.ofccmobjid from ");
        sql.append(schema);
        sql.append("offerctl A left join ");
        sql.append(schema);
        sql.append("offerctlc B on B.ofcoffer# = A.ofcoffer# where A.ofcoffer# = ?");
        //sql.append((getOfferingLetter().getOFFERINGNUMBER()));
        sql.append(" and A.ofcctycd = ");

        if (getOfferingLetter().getCOUNTRY().equals("GB"))
            sql.append("'UK'");
        else
        	sql.append("'");
            sql.append(getOfferingLetter().getCOUNTRY());
            sql.append("'");
        logger.debug("in selectByOfferingStatement() sql :" + sql);
        // prepare the statement to execute
       
        try
        {
       
        	conn=getDataSourceTransactionManager().getDataSource().getConnection();         
            pstmt= conn.prepareStatement(sql.toString());
            pstmt.setString(1,getOfferingLetter().getOFFERINGNUMBER());
            
            ResultSet results =pstmt.executeQuery();
            cachedRs.populate(results);            
            // return the results
            return cachedRs;
        } catch (SQLException exc)
        {
        	logger.debug("exception in selectByOfferingStatement():"+exc);
            throw exc;
        }
        finally{
        	conn.close();
        	pstmt.close();
        	
        }
    }

    /**
     * create a select statement for retieving the Offering control data by
     * object id Creation date: (3/13/00 11:44:07 AM)
     * 
     * @return ResultSet
     * @throws NamingException 
     */
    public ResultSet selectByObjectIdStatement(String itemID,String country) throws SQLException, NamingException
    {
        StringBuffer sql = new StringBuffer();
             
        
        CountryProperties countryProperties = CountryManager.getCountryProperties(getOfferingLetter().getCOUNTRY());
        String schema = countryProperties.getAptsSchema();
        
        CachedRowSetImpl cachedRs = new CachedRowSetImpl();
        Connection conn= null;
        PreparedStatement pstmt=null;
        
        // build the sql statement, use append because it is more efficient
        sql.append("select A.ofcoffer#,A.ofcoframt,A.ofcofrbal,A.OFCCURR,A.OFCUST#,A.OFCUSTNAME,B.ofsigndat,A.ofcctycd from ");
        sql.append(schema);
        sql.append("offerctl A left join ");
        sql.append(schema);
        sql.append("offerctlc B on B.ofcoffer# = A.ofcoffer# AND A.ofcctycd =B.ofcctycd where ofccmobjid= ? ");
        // TIR USRO-R-MFRS-9V8MQL - User should have received msg that OL has a 0.00 bal & cannot be updated. The below code commented.
       /* sql.append("(select ofcoffer# from ");
        sql.append(schema);
        sql.append("offerctlc where ofccmobjid = ");*/
       // sql.append(getSqlString(OBJECTIDidx));
       
      
         logger.debug("sql in selectbyobjectid:"+sql.toString());
         logger.debug("objectId in selectbyobjectId:"+getOfferingLetter().getOBJECTID());
         
        // prepare the statement to execute
        try
        {
            conn=getDataSourceTransactionManager().getDataSource().getConnection();
            logger.debug("connection in selectbyObjectId is :" +conn);
            pstmt= conn.prepareStatement(sql.toString());
            pstmt.setString(1, getOfferingLetter().getOBJECTID());
            ResultSet results = pstmt.executeQuery();
            cachedRs.populate(results);
            logger.debug("results in selectByObjectIdStatement():"+results);
            

            // return the results
            return cachedRs;
        } catch (SQLException exc)
        {
        	logger.debug("exception in selectByObjectIdStatement is :" +exc);
        	throw exc;
        }
        finally
        {
        	conn.close();
        	pstmt.close();
        }
    }

    /**
     * create a select statement for retieving the contract control data by
     * offering letter # Creation date: (3/13/00 11:44:07 AM)
     * 
     * @return ResultSet
     * @throws NamingException 
     */
    public ResultSet selectContractStatement() throws SQLException, NamingException
    {
        StringBuffer sql = new StringBuffer();
              
        logger.debug("inside selectContractStatement() ");
        
        CountryProperties countryProperties = CountryManager.getCountryProperties(getOfferingLetter().getCOUNTRY());
        String schema = countryProperties.getAptsSchema();
        
        
        Connection conn=null;
        CachedRowSetImpl cachedRs = new CachedRowSetImpl();
        PreparedStatement pstmt=null;

        // build the sql statement, use append because it is more efficent
        sql.append("select B.CCCCMOBJID from ");
        sql.append(schema);
        sql.append("cntcntl A left join ");
        sql.append(schema);
        sql.append("cntcntlc B on B.cccnt# = A.cccnt# and a.ccctycod = b.ccctycod where A.CCOFFER# = ?");
        sql.append(" and A.ccctycod = ?");
      
        
              
        logger.debug("sql inside selectContractStatement"+sql);
       
        

        // prepare the statement to execute
        try
        {
            conn = getDataSourceTransactionManager().getDataSource().getConnection();
            pstmt= conn.prepareStatement(sql.toString());
            pstmt.setString(1, getOfferingLetter().getOFFERINGNUMBER());
            pstmt.setString(2, getOfferingLetter().getCOUNTRY());
            ResultSet results = pstmt.executeQuery();
            cachedRs.populate(results);
           
            logger.debug("results in selectContractStatement"+results);               
            // return the results
            return cachedRs;
        } catch (SQLException exc)
        {
        	logger.debug("exception in selectContractStatement() :"+exc);
        	throw exc;
        }
        finally
        {
        	conn.close();
        	pstmt.close();
        }
    }

    /**
     * create a select statement for retieving the invoice control data by
     * offering letter # Creation date: (3/13/00 11:44:07 AM)
     * 
     * @return ResultSet
     * @throws NamingException 
     */
    public ResultSet selectInvoiceStatement() throws SQLException, NamingException
    {
        StringBuffer sql = new StringBuffer();
        
        CountryProperties countryProperties = CountryManager.getCountryProperties(getOfferingLetter().getCOUNTRY());
        String schema = countryProperties.getAptsSchema();
        
        Connection conn=null;
        PreparedStatement pstmt=null;
        CachedRowSetImpl cachedRs = new CachedRowSetImpl();
        
        
        // build the sql statement, use append because it is more efficient
        sql.append("select B.invcccmid from ");
        sql.append(schema);
        sql.append("winvcntl A left join ");
        sql.append(schema);
        sql.append("winvcntlc B on B.invcinv#=A.invcinv# and B.invcctycd = A.invcctycd where A.INVCOFFER# = ?");
       
        logger.debug("sql inside selectInvoiceStatement():"+sql); 
        logger.debug("Inside selectInvoiceStatement:"+getOfferingLetter().getOFFERINGNUMBER());
        
        // prepare the statement to execute
        try
        {
            
            
        	conn=getDataSourceTransactionManager().getDataSource().getConnection();
             pstmt= conn.prepareStatement(sql.toString());
            pstmt.setString(1, getOfferingLetter().getOFFERINGNUMBER());
            ResultSet results = pstmt.executeQuery();
            cachedRs.populate(results);

            // return the results
            return cachedRs;
        } catch (SQLException exc)
        {
        	logger.debug("exception in selectInvoiceStatement() :"+ exc);
            throw exc;
        }
        finally
        {
        	conn.close();
        	pstmt.close();
        }
    }

    /**
     * creates a select statement for retrieving the currency information base
     * on the country Creation date: (3/13/00 11:44:07 AM)
     * 
     * @return ResultSetCache
     * @throws NamingException 
     */
    public ResultSetCache selectCurrencyCodesStatement() throws SQLException, NamingException
   // public ResultSet selectCurrencyCodesStatement() throws SQLException, NamingException
    {
    	//Below code is commented by Krishna
        
       if ((currencyResults != null) && (currencyResults.getIndexKey().equals(getOfferingLetter().getCOUNTRY())))
        {
            currencyResults.beforeFirst();
            return (currencyResults);
        }

        StringBuffer sql = new StringBuffer();
              
        CountryProperties countryProperties = CountryManager.getCountryProperties(getOfferingLetter().getCOUNTRY());
        String schema = countryProperties.getAptsSchema();
        Connection conn=null;
        PreparedStatement pstmt=null;
          
         
        // build the sql statement, use append since it is more efficient
        sql.append("select distinct cccurr, CCCDFT from ");
        sql.append(schema);
        sql.append("ccccntl where cccntcode = ?");              
        sql.append(" order by cccurr");
               

        // prepare the statement to execute
        try
        {
                       
            conn=getDataSourceTransactionManager().getDataSource().getConnection();
            pstmt= conn.prepareStatement(sql.toString());
            pstmt.setString(1,getOfferingLetter().getCOUNTRY());
            
            ResultSet results = pstmt.executeQuery();
            
               
              //below line commented by Krishna        
            currencyResults = new ResultSetCache(results, getOfferingLetter().getCOUNTRY(), 0, 1, "*");
           
            return (currencyResults);
           // return results ;
        } catch (SQLException exc)
        {
            logger.debug("exception in selectCurrencyCodesStatement is :"+exc);
            throw exc;
        }
        finally
        {
        	pstmt.close();
        	conn.close();
        }
    }

    /**
     * create an update statement for updating the Offering Letter control
     * information Creation date: (3/13/00 11:44:07 AM)
     * 
     * @throws ParseException
     * @throws IllegalArgumentException
     * @throws NamingException 
     */
    public void updateOfferingStatement() throws SQLException, IllegalArgumentException, ParseException, NamingException
    {
        Date now = new Date();
        StringBuffer sql = new StringBuffer();

       
              
        CountryProperties countryProperties = CountryManager.getCountryProperties(getOfferingLetter().getCOUNTRY());
        String schema = countryProperties.getAptsSchema();
  
        Connection conn=null;
        PreparedStatement pstmt=null;
    
        
//        String offeringDate= getOfferingLetter().getOFFERDATE();


        logger.debug("Offering Date from front end is :"+ getOfferingLetter().getOFFERDATE() );
        
  
         
        
        
      
        logger.debug("inside updateOfferingStatement" );

      //below line commented by Krishna
        
        retrieveCreatedDate();

        // clip the username
        String username = trimUSERID(getOfferingLetter().getUSERID());

        // build the sql, use append because it is more efficient
        sql.append("update ");
        sql.append(schema);
        sql.append("OFFERCTL set OFCOFFER# = ?");
        sql.append(", OFCCURR = ?");
        if(getOfferingLetter().getNETEQBAL().equals("Y")){
        	sql.append(", OFCOFRAMT=");
        	sql.append(getOfferingLetter().getAMOUNT().replace(',', '.'));
        	sql.append(", OFCOFRBAL=");
        	sql.append(getOfferingLetter().getAMOUNT().replace(',', '.'));
        }
        sql.append(", OFCUSERID= ?");
        sql.append(", OFCLSTDAT=");
        sql.append("'");
        sql.append(RegionalDateConverter.formatDate("GUI-"+getOfferingLetter().getLocale().toString(), now));
        sql.append("'");
        sql.append(", OFCLSTTIM=");
        sql.append("'");
        sql.append(RegionalDateConverter.formatTime("GUI-"+getOfferingLetter().getLocale().toString(), now));
        sql.append("'");
        sql.append(", OFCUST# = ?");
        sql.append(", OFCUSTNAME = ?");
        sql.append(", OFCRECDAT = ");

        sql.append(getSqlString(RegionalDateConverter.formatDate("DB2", GilUtility.getCreatedTimeStampAsDate(getOfferingLetter().getCreatedTimeStamp()))));//CM creation date

        sql.append(" where OFCOFFER#= ?");
        sql.append(" and OFCCTYCD = ?");
        
        
        
        logger.debug("first sql inside updateOfferingStatement():"+sql);
        

        // prepare the statement for execution
        try
        {   
            conn=getDataSourceTransactionManager().getDataSource().getConnection();
            
            pstmt= conn.prepareStatement(sql.toString());
            pstmt.setString(1, getOfferingLetter().getOFFERINGNUMBER());
            pstmt.setString(2,getOfferingLetter().getCURRENCY());
            pstmt.setString(3, username);
            pstmt.setString(4,getOfferingLetter().getCUSTOMERNUMBER());
            pstmt.setString(5,getOfferingLetter().getCUSTOMERNAME());
            pstmt.setString(6,getOfferingLetter().getOFFERINGNUMBER());
            pstmt.setString(7,getOfferingLetter().getCOUNTRY());
            
           int i= pstmt.executeUpdate();
           
           
           logger.debug("update int inside updateOfferingStatement():"+i);
           
        } catch (SQLException exc)
        {
        	logger.debug("exception in updateOfferingStatement() first try block:"+exc);
        	logger.debug("exception in updateOfferingStatement() first try block"+ exc);
            throw exc;
        }

        sql.setLength(0);
        sql.append("update ");
        sql.append(schema);
        sql.append("offerctlc set OFCOFFER#= ?");
        sql.append(", OFCCTYCD = ?");
        sql.append(", ofsigndat = "); //OL DATE

        if( getOfferingLetter().getOFFERDATE().equals("")){

			sql.append("'0001-01-01'");
		}else{
			sql.append(getSqlString(RegionalDateConverter.convertDate("GUI", "DB2",
					getOfferingLetter().getOFFERDATE(),this.getOfferingLetter().getUserTimezone())));
		}
        sql.append("where OFCCMOBJID= ?");
        

        
        logger.debug("second sql in updateOfferingStatement()"+sql);
       

        try
        {
                      
            conn=getDataSourceTransactionManager().getDataSource().getConnection();
            pstmt= conn.prepareStatement(sql.toString());
            pstmt.setString(1, getOfferingLetter().getOFFERINGNUMBER());
            pstmt.setString(2,getOfferingLetter().getCOUNTRY());
            pstmt.setString(3,getOfferingLetter().getOBJECTID());
            
            int i= pstmt.executeUpdate();          
           
            logger.debug("update int inside updateOfferingStatement() second try:"+i);
            
            
            
        } catch (SQLException exc)
        {
           
        	logger.debug("exception in updateOfferingStatement() in secon try block is : " +exc);
            throw exc;
        }
        finally{
        	conn.close();
        	pstmt.close();
        }
    }

    /**
     * create an update statement for updating the Invoice control information
     * Creation date: (3/13/00 11:44:07 AM)
     * @throws NamingException 
     */
    public void updateInvoiceStatement() throws SQLException, NamingException
    {
        
        StringBuffer sql = new StringBuffer();
    
              
        CountryProperties countryProperties = CountryManager.getCountryProperties(getOfferingLetter().getCOUNTRY());
        String schema = countryProperties.getAptsSchema();
        Connection conn=null;
        PreparedStatement pstmt=null;

        // build the sql, use append because it is more efficient
        sql.append("update ");
        sql.append(schema);
        sql.append("WINVCNTL set INVCOFFER# = ?");
        sql.append(" WHERE INVCOFFER# = ?");
        sql.append(" and INVCCTYCD = ?");
       
        
        logger.debug("first sql inside updateInvoiceStatement():"+sql);

        // prepare the statement for execution
        try
        {
        	       	
        	
        	conn=getDataSourceTransactionManager().getDataSource().getConnection();
        	pstmt= conn.prepareStatement(sql.toString());
        	pstmt.setString(1,getOfferingLetter().getOFFERINGNUMBER());
        	pstmt.setString(2, getOfferingLetter().getOLDOFFERINGNUMBER());
        	pstmt.setString(3, getOfferingLetter().getCOUNTRY());
        	
        	int i=pstmt.executeUpdate();
        	
        	 logger.debug("update int i inside updateInvoiceStatement():"+ i);
        	
        } catch (SQLException exc)
        {
        
        	logger.debug("exception in updateInvoiceStatement()  first try block:" +exc);
            throw exc;
        }

        sql.setLength(0);
        sql.append("update ");
        sql.append(schema);
        sql.append("winvcntlc set ");
        sql.append("INVCOFFER# = ?");
        sql.append(" WHERE INVCOFFER# = ?");
        sql.append(" and INVCCTYCD = ?");
        logger.debug("second sql in updateInvoiceStatement():"+sql);
        
        try
        {
                       
            conn=getDataSourceTransactionManager().getDataSource().getConnection();
            pstmt= conn.prepareStatement(sql.toString());
            pstmt.setString(1,getOfferingLetter().getOFFERINGNUMBER());
            pstmt.setString(2,getOfferingLetter().getOLDOFFERINGNUMBER());
            pstmt.setString(3, getOfferingLetter().getOLDCOUNTRY());
            
            int i= pstmt.executeUpdate();
            
            logger.debug("update int i inside updateInvoiceStatement() second try :"+ i);
            
            
        } catch (SQLException exc)
        {
        	logger.debug("exception in updateInvoiceStatement()  second  try block:" +exc);
            throw exc;
        }
        finally{
        	conn.close();
        	pstmt.close();
        }

    }

    /**
     * create an update statement for updating the Invoice control information
     * Creation date: (3/13/00 11:44:07 AM)
     * @throws NamingException 
     */
    public void updateContractStatement() throws SQLException, NamingException
    {
        
        StringBuffer sql = new StringBuffer();
         
        CountryProperties countryProperties = CountryManager.getCountryProperties(getOfferingLetter().getCOUNTRY());
        String schema = countryProperties.getAptsSchema();
        
        Connection conn=null;
        PreparedStatement pstmt=null;
        // build the sql, use append because it is more efficient
        sql.append("update ");
        sql.append(schema);
        sql.append("CNTCNTL set CCOFFER# = ?");
        sql.append(" WHERE CCOFFER# = ?");
        sql.append(" and CCCTYCOD = ?");
      
        
        logger.debug("sql inside uupdateContractStatement():"+sql);

        // prepare the statement for execution
        try
        {
                     
            conn=getDataSourceTransactionManager().getDataSource().getConnection();
            pstmt= conn.prepareStatement(sql.toString());
            pstmt.setString(1,getOfferingLetter().getOFFERINGNUMBER());
            pstmt.setString(2,getOfferingLetter().getOLDOFFERINGNUMBER());
            pstmt.setString(3,getOfferingLetter().getOLDCOUNTRY());
            
            int i=pstmt.executeUpdate();
            
            logger.debug("update i inside uupdateContractStatement():"+ i);
            
        } catch (SQLException exc)
        {
        	logger.debug(" excption in updateContractStatement() :" +exc);
            throw exc;
        }

        sql.setLength(0);
        sql.append("update ");
        sql.append(schema);
        sql.append("cntcntlc set ");
        sql.append("CCOFFER# = ? ");
        sql.append(" WHERE CCOFFER# = ?");
        sql.append(" and CCCTYCOD = ?");
//        sql.append(getOfferingLetter().getCOUNTRY());

        // prepare the statement for execution
        try
        {
        	       	
        	conn=getDataSourceTransactionManager().getDataSource().getConnection();
        	pstmt= conn.prepareStatement(sql.toString());
        	pstmt.setString(1,getOfferingLetter().getOFFERINGNUMBER());
        	pstmt.setString(2,getOfferingLetter().getOLDOFFERINGNUMBER());
        	pstmt.setString(3,getOfferingLetter().getCOUNTRY());
        	
        	int i = pstmt.executeUpdate();//sql.toString());
        	
        	logger.debug("update i inside uupdateContractStatement() second try :"+ i);
        	
        	
        } catch (SQLException exc)
        {
        	logger.debug("exception in updateContractStatement():"+exc);
            throw exc;
        }
        finally
        {
        	conn.close();
        	pstmt.close();
        }

    }

    public void updateOfferingDetailsStatement() throws SQLException, IllegalArgumentException, ParseException, NamingException
    {
        
        StringBuffer sql = new StringBuffer();

        
        CountryProperties countryProperties = CountryManager.getCountryProperties(getOfferingLetter().getCOUNTRY());
        String schema = countryProperties.getAptsSchema();
        
        PreparedStatement pstmt=null;
        Connection conn=null;

        sql.append("update ");
        sql.append(schema);
        sql.append("OFFERCTL set OFCOFFER# = ?");
        sql.append(" where OFCOFFER#= ?");
        sql.append(" and OFCCTYCD = ?");
            

        try
        {
                    
           conn=getDataSourceTransactionManager().getDataSource().getConnection();
           pstmt= conn.prepareStatement(sql.toString());
           pstmt.setString(1,getOfferingLetter().getOFFERINGNUMBER());
           pstmt.setString(2,getOfferingLetter().getOLDOFFERINGNUMBER());
           pstmt.setString(3, getOfferingLetter().getCOUNTRY());
           
           pstmt.executeUpdate();
           
           
        } catch (SQLException exc)
        {
        	logger.debug("exception in updateOfferingDetailsStatement( ):"+exc);
            throw exc;
        }
        finally
        {
        	conn.close();
        	pstmt.close();
        }
        
    }
    
    public ResultSet selectContractNumber() throws SQLException, NamingException
    {
        StringBuffer sql = new StringBuffer();

        CountryProperties countryProperties = CountryManager.getCountryProperties(getOfferingLetter().getCOUNTRY());
        String schema = countryProperties.getAptsSchema();
        
        Connection conn=null;
        PreparedStatement pstmt=null;
        CachedRowSetImpl cachedRs = new CachedRowSetImpl();

        // build the sql statement, use append because it is more efficent
        sql.append("select cccnt# from ");
        sql.append(schema);
        sql.append("cntcntl where ccoffer# = ?");
        sql.append(" and ccctycod = ");
        if (getOfferingLetter().getCOUNTRY().equals("GB"))
            sql.append("'UK'");
        else
            sql.append("'");
        	sql.append(getOfferingLetter().getCOUNTRY());
        	sql.append("'");
        	logger.debug("sql in selectContractNumber():"+sql);
        	
        // prepare the statement to execute
        try
        {
        	       		
        	        	 
        	 conn=getDataSourceTransactionManager().getDataSource().getConnection();
        	 pstmt= conn.prepareStatement(sql.toString());
        	 pstmt.setString(1, getOfferingLetter().getOLDOFFERINGNUMBER());
        	 ResultSet results = pstmt.executeQuery();
        	 
        	 cachedRs.populate(results);
        	 
        	 // return the results
            return cachedRs;
        } catch (SQLException exc)
        {
            logger.debug("exception in selectContractNumber():" + exc);
            throw exc;
        }
        finally{
        	conn.close();
        	pstmt.close();
        }
    }
    
	public ResultSet selectPaymentDate() throws SQLException, NamingException {
		StringBuffer sql = new StringBuffer();

		CountryProperties countryProperties = CountryManager
				.getCountryProperties(getOfferingLetter().getCOUNTRY());
		String schema = countryProperties.getAptsSchema();

		Connection conn = null;
		PreparedStatement pstmt = null;
		CachedRowSetImpl cachedRs = new CachedRowSetImpl();

		sql.append("select PHPAYDATE from ");
		sql.append(schema);
		sql.append("WPAYRHDR where PHCON# = ?");
		sql.append(" and PHCTYCOD = ?");
		logger.debug("sql inside selectPaymentDate():" + sql);

		try {
			conn = getDataSourceTransactionManager().getDataSource()
					.getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1, getOfferingLetter().getCONTRACTNUMBER());
			pstmt.setString(2, getOfferingLetter().getCOUNTRY());

			ResultSet results = pstmt.executeQuery();

			cachedRs.populate(results);

			return cachedRs;
		} catch (SQLException exc) {
			logger.debug("exception in  selectPaymentDate()  : " + exc);
			throw exc;
		} finally {
			conn.close();
			pstmt.close();
		}
	}
    
    public void updateOfferingLetterCountry() throws SQLException, IllegalArgumentException, ParseException, NamingException
    {

        StringBuffer sql = new StringBuffer();

        
        CountryProperties countryProperties = CountryManager.getCountryProperties(getOfferingLetter().getCOUNTRY());
        String schema = countryProperties.getAptsSchema();
        
        Connection conn=null;
        PreparedStatement pstmt=null;


        sql.append("update ");
        sql.append(schema);
        sql.append("OFFERCTL set OFCCTYCD = ?");
        sql.append(getOfferingLetter().getCOUNTRY());
        sql.append(" where OFCOFFER#= ?");
        sql.append(" and OFCCTYCD = ?");
        
        
        logger.debug("first sql inside updateOfferingLetterCountry(): "+sql);

        try
        {
                       
            conn=getDataSourceTransactionManager().getDataSource().getConnection();
            pstmt= conn.prepareStatement(sql.toString());
            pstmt.setString(1, getOfferingLetter().getCOUNTRY());
            pstmt.setString(2, getOfferingLetter().getOLDOFFERINGNUMBER());
            pstmt.setString(3, getOfferingLetter().getOLDCOUNTRY());
            
            pstmt.executeUpdate();
            
            
        } catch (SQLException exc)
        {
           logger.debug(" exception in updateOfferingLetterCountry() first try block:" +exc);
            throw exc;
        }
        
        sql.setLength(0);
        sql.append("update ");
        sql.append(schema);
        sql.append("OFFERCTLC set OFCCTYCD = ?");
        sql.append(" where OFCOFFER#= ?");
        sql.append(" and OFCCTYCD = ?");
        
        logger.debug("second sql inside updateOfferingLetterCountry(): "+sql);
       

        // prepare the statement for execution
        try
        {
            //getDatabaseDriver().executeUpdate(getDatabaseDriver().getConnection(region, OL.getCOUNTRY()), sql.toString());
            
            conn=getDataSourceTransactionManager().getDataSource().getConnection();    
            pstmt= conn.prepareStatement(sql.toString());
            pstmt.setString(1,getOfferingLetter().getCOUNTRY());
            pstmt.setString(2,getOfferingLetter().getOLDOFFERINGNUMBER());
            pstmt.setString(3,getOfferingLetter().getOLDCOUNTRY());
            
            pstmt.executeUpdate();
            
            
        } catch (SQLException exc)
        {
        	logger.debug(" exception in updateOfferingLetterCountry() second try block:" +exc);
            throw exc;
        }     
        finally{
        	conn.close();
        	pstmt.close();
        }
    }    
}