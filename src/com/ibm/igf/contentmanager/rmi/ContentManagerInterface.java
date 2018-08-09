package com.ibm.igf.contentmanager.rmi;



import com.ibm.mm.sdk.server.DKDatastoreICM;

public interface ContentManagerInterface  {
    public String createDocument(String index, String[] fields, String[] values, byte[][] multiDocumentData, String[] mimeTypes,DKDatastoreICM datastore) ;

    public String createDocument(String index, String[] fields, String[] values, byte[] documentData, String mimeType,DKDatastoreICM datastore) ;

    public String createDocument(String index, String[] fields, String[] values, byte[][] multiDocumentData, String[] mimeTypes, String[] fileNames,DKDatastoreICM datastore) ;

    public String createDocument(String index, String[] fields, String[] values, byte[] documentData, String mimeType, String fileName,DKDatastoreICM datastore) ;

    public String createFolder(String index, String[] fields, String[] values,DKDatastoreICM datastore) throws Exception ;

    public String executeQuery(String searchText,DKDatastoreICM datastore) ;

    public String executeQuery(String index, String searchText,DKDatastoreICM datastore) ;

    public String executeQuery(String index, String[] fields, String[] values,DKDatastoreICM datastore) ;

    public String getCreatedTimestamp(String objectId,DKDatastoreICM datastore) ;

    public java.util.Date getCreatedDate(String objectId,DKDatastoreICM datastore) ;
    
    public String[] getIndexFields(String index) ;

    public String[] getIndexValues(String objectId,DKDatastoreICM datastore) ;

    public java.lang.String getLastException() ;

    public boolean moveDocument(String objectId, String newIndexClass,DKDatastoreICM datastore) ;

    public boolean moveDocumentToFolder(String objectId, String folderId,DKDatastoreICM datastore) throws Exception;

    public byte[][] retrieveDocumentContents(String objectId,DKDatastoreICM datastore) ;

    public byte[] retrieveDocumentContents(String objectId, int partNum,DKDatastoreICM datastore) ;

    public String retrieveDocumentProperties(String objectId,DKDatastoreICM datastore) ;

    public boolean startWorkflow(String objectId, String workflow,DKDatastoreICM datastore) ;

    public boolean updateDocument(String objectId, String[] fields, String[] values, DKDatastoreICM dkDatastoreICM) ;

    public boolean addNotelog(String objectId, String notes,DKDatastoreICM datastore) ;

    public String[] getIndexNames() ;

    public String[] attributeNameToDescription(String index, String[] fields,DKDatastoreICM datastore) ;

    public String[] attributeDescriptionToName(String index, String[] fields,DKDatastoreICM datastore) throws Exception;

    public String indexDescriptionToName(String index,DKDatastoreICM datastore) ;

    public String indexNameToDescription(String index,DKDatastoreICM datastore) ;

    public String retrieveDocumentMimeType(String objectId, int partnum,DKDatastoreICM datastore) ;

    public String[] retrieveDocumentMimeTypes(String objectId,DKDatastoreICM datastore) ;

    public String isDocumentInFolder(String documentItemType, String documentPID, String folderItemType, String folderPID,DKDatastoreICM datastore) ;

    public String listWorkflow(String workflow,DKDatastoreICM datastore) ;

    public boolean completeWorkflow (String objectId, String workflow,DKDatastoreICM datastore) ;
    
    public String retrievePrivilegeSetName(String userId,DKDatastoreICM datastore);
    
    public void clear();
}
