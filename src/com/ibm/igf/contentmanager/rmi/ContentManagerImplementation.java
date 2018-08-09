package com.ibm.igf.contentmanager.rmi;

//import java.rmi.server.UnicastRemoteObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;

import com.ibm.mm.sdk.common.DKAttrDefICM;
import com.ibm.mm.sdk.common.DKAuthorizationMgmtICM;
import com.ibm.mm.sdk.common.DKComponentTypeDefICM;
import com.ibm.mm.sdk.common.DKConstant;
import com.ibm.mm.sdk.common.DKConstantICM;
import com.ibm.mm.sdk.common.DKDDO;
import com.ibm.mm.sdk.common.DKDatastoreAdminICM;
import com.ibm.mm.sdk.common.DKDatastoreDefICM;
import com.ibm.mm.sdk.common.DKDate;
import com.ibm.mm.sdk.common.DKDocRoutingServiceICM;
import com.ibm.mm.sdk.common.DKDocRoutingServiceMgmtICM;
import com.ibm.mm.sdk.common.DKException;
import com.ibm.mm.sdk.common.DKFolder;
import com.ibm.mm.sdk.common.DKLobICM;
import com.ibm.mm.sdk.common.DKNVPair;
import com.ibm.mm.sdk.common.DKParts;
import com.ibm.mm.sdk.common.DKPid;
import com.ibm.mm.sdk.common.DKPidICM;
import com.ibm.mm.sdk.common.DKPrivilegeSetICM;
import com.ibm.mm.sdk.common.DKResults;
import com.ibm.mm.sdk.common.DKRetrieveOptionsICM;
import com.ibm.mm.sdk.common.DKSequentialCollection;
import com.ibm.mm.sdk.common.DKSizeOutOfBoundsException;
import com.ibm.mm.sdk.common.DKTextICM;
import com.ibm.mm.sdk.common.DKTimestamp;
import com.ibm.mm.sdk.common.DKUsageError;
import com.ibm.mm.sdk.common.DKUserDefICM;
import com.ibm.mm.sdk.common.DKUserMgmtICM;
import com.ibm.mm.sdk.common.DKWorkListICM;
import com.ibm.mm.sdk.common.DKWorkPackageICM;
import com.ibm.mm.sdk.common.dkAttrDef;
import com.ibm.mm.sdk.common.dkCollection;
import com.ibm.mm.sdk.common.dkDataObjectBase;
import com.ibm.mm.sdk.common.dkIterator;
import com.ibm.mm.sdk.common.dkUserManagement;
import com.ibm.mm.sdk.common.dkWorkFlowService;
import com.ibm.mm.sdk.common.dkXDO;
import com.ibm.mm.sdk.server.DKDatastoreICM;

public class ContentManagerImplementation implements ContentManagerInterface {
	
	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(ContentManagerImplementation.class);
    public class ConnectionPoolItem {
        int connectionCount = 0;

        int connectionNumber = -1;

        DKDatastoreICM dsDL = null;

        long firstReserved = 0;

        IdleMonitor idler = null;

        Boolean isConnected = Boolean.FALSE;

        Boolean isWorkflowConnected = Boolean.FALSE;
        
		DKDocRoutingServiceICM routingService = null;

        String threadId = null;

        boolean shouldDispose = false;

        boolean isDisposing = false;

        public void dispose()
        {
            if (!isDisposing)
            {
                isDisposing = true;
                if (idler != null)
                    idler.terminate();
                isDisposing = false;
            }
        }

        public void init()
        {
            dsDL = null;
            routingService = null;
            isConnected = Boolean.FALSE;
            isWorkflowConnected = Boolean.FALSE;
            threadId = null;
            connectionCount = 0;
            idler = null;
            shouldDispose = false;
        }
    }

    public class IdleMonitor implements Runnable {
        ConnectionPoolItem connectionPoolItem = null;

        Thread runner = null;

        boolean terminateFlag = false;

        public IdleMonitor(ConnectionPoolItem aConnectionPoolItem)
        {
            runner = new Thread(this, "CM Idle Monitor");
            connectionPoolItem = aConnectionPoolItem;
            runner.start();
        }

        private void handleException(Throwable exc)
        {
            logger.debug(" : Exception Thrown");
            logger.debug(" : " + exc.toString());
        }

        public void logOff()
        {
            logger.debug(" : Starting logoff of CM.  Connection # " + connectionPoolItem.connectionNumber);
            if ((connectionPoolItem.dsDL != null) && (connectionPoolItem.isConnected.equals(Boolean.TRUE)))
            {
                try
                {
                    logger.debug(" : Logging off of CM.  Connection # " + connectionPoolItem.connectionNumber);
                    synchronized (connectionPool)
                    {
                        connectionPoolItem.isConnected = Boolean.FALSE;
                        connectionPoolItem.isWorkflowConnected = Boolean.FALSE;
                        if (connectionPoolItem.dsDL != null)
                        {
                            if (connectionPoolItem.dsDL.isConnected())
                            {
                                try
                                {
                                    connectionPoolItem.dsDL.disconnect();
                                } catch (DKException exc)
                                {
                                    handleException(exc);
                                }
                            }
                            try
                            {
                                connectionPoolItem.dsDL.destroy();
                            } catch (DKException exc)
                            {
                                handleException(exc);
                            }
                        }
                        if (connectionPoolItem.routingService != null)
                        {

                            if (connectionPoolItem.routingService.isConnected())
                            {
                                try
                                {
                                    connectionPoolItem.routingService.disconnect();
                                } catch (DKException exc)
                                {
                                    handleException(exc);
                                }
                            }
                        }
                        connectionPoolItem.init();
                        connectionPool.notifyAll();
                    }
                } catch (DKException exc)
                {
                    handleException(exc);
                } catch (Exception exc)
                {
                    handleException(exc);
                }
            } else
            {
                logger.debug(" : Aborting logoff of CM.  Connection # " + connectionPoolItem.connectionNumber + " is not connected");
            }
            connectionPoolItem.init();
        }

        public void run()
        {

            long sleep = 1000 * 60 * 28;

            while (true)
            {
                try
                {
                    // terminate
                    if (terminateFlag)
                    {
                        logOff();
                        return;
                    }

                    Thread.sleep(sleep);

                    // idle out because the sleep wasn't interrupted
                    logOff();
                    return;
                } catch (InterruptedException exc)
                {
                    // was updated with new time
                }
            }

        }

		public void terminate()
        {
            terminateFlag = true;
            runner.interrupt();
        }

        public void update()
        {
            runner.interrupt();
        }
    }

    public static final transient int CONNECTIONPOOLSIZE = 16;
    
    private static final transient int MAXRECORDS = 100;

    private final static long serialVersionUID = -1797287709987711756L;

    private dkAttrDef[][] attributeCache = null;

    private String[][] attributeList = null;

    private String[] classList = null;

    // private String[] cm7ServerArray = null;
    private String[] cm8ServerArray = null;

    private ConnectionPoolItem[] connectionPool = new ConnectionPoolItem[CONNECTIONPOOLSIZE];

    private Hashtable dkddoCache = new Hashtable();
    
    private HashMap<String,String> usersCache = null;

    private DKComponentTypeDefICM[] entityCache = null;

    private Hashtable lastException = new Hashtable();

    private String password;

    private String server;

    private String userid;

    private boolean cascadeFailures = true;

    private static String XMLLogFilename = "ContentManagerRMI.xml";
    
    private static SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public ContentManagerImplementation(DKDatastoreICM datastore) 
    
    {
    	
    	recacheEntityCache(datastore);

    }

    public ContentManagerImplementation(String server, String user, String password) 
    {
        super();
       // DOMConfigurator.configure(XMLLogFilename);
        setServer(server);
        setUserid(user);
        setPassword(password);
        initializeConnections();
    }

    private boolean addItemToFolder(DKDDO item, DKDDO folderDDO,DKDatastoreICM datastore)
    {
        // check parameters
        String methodName = "addItemToFolder(DKDDO item, DKDDO folderDDO)";
        if ((folderDDO == null) || (item == null))
        {
            return false;
        }
        try
        {
            //DKDatastoreICM datastore = connect(methodName);

            // get the folder
            folderDDO.retrieve(DKConstant.DK_CM_CONTENT_ITEMTREE);
            DKFolder dkFolder = (DKFolder) folderDDO.getData(folderDDO.dataId(DKConstant.DK_CM_NAMESPACE_ATTR, DKConstant.DK_CM_DKFOLDER));

            if (dkFolder == null)
            {
                logger.debug(" : item specified is not a folder");
                
                //below line commented by Krishna
                //disconnect(methodName);
               //// datastore.disconnect();
                return false;
            } else
            {
                dkIterator pIter = dkFolder.createIterator();
                // check to see if the folder contains this item
                while (pIter.more())
                {
                    Object aTestDDO = pIter.next();
                    if (aTestDDO instanceof DKDDO)
                    {
                        if (((DKDDO) aTestDDO).getPidObject().getPrimaryId().equals(item.getPidObject().getPrimaryId()))
                        {
                            logger.debug(" : item exists in folder");
                            //below line commented by Krishna
                           //disconnect(methodName);
                           //// datastore.disconnect();
                            return true;
                        }
                    }
                }

                dkFolder.addElement(item);

                boolean locked = false;
                boolean isUpdated = false;
                try
                {
                    logger.debug("datastore checkOut(folderDDO)");
                    datastore.checkOut(folderDDO);
                    locked = true;
                } catch (Exception exc)
                {
                    logger.debug("folder can not be locked");
                    handleException(exc);
                }
                try
                {
                    logger.debug("datastore folderDDO.update()");
                    folderDDO.update();
                    isUpdated = true;
                } catch (Exception exc)
                {
                    logger.debug("folder can not be updated");
                    handleException(exc);
                }
                try
                {
                    logger.debug("datastore checkIn(folderDDO)");
                    datastore.checkIn(folderDDO);
                } catch (Exception exc)
                {
                    logger.debug("folder can not be unlocked");
                    handleException(exc);
                }

                //below line commented by Krishna
                //disconnect(methodName);
               //// datastore.disconnect();
                return isUpdated;
            }
        } catch (DKUsageError exc)
        {
            handleException(exc);
        } catch (DKException exc)
        {
            handleException(exc);
        } catch (Exception exc)
        {
            handleException(exc);
        }

      //below line commented by Krishna
      //disconnect(methodName);
        return false;
    }

    public String[] attributeDescriptionToName(String index, String[] fields,DKDatastoreICM datastore) throws Exception
    {
        String methodName = "attributeDescriptionToName(String index, String[] fields)";
        // connect (methodName);
        
        datastore.connection();

        dkAttrDef[] attributeTypes = getIndexFieldAttributes(index);
        if (attributeTypes != null)
        {
            String description = null;
            String name = null;
            for (int f = 0; f < fields.length; f++)
            {
                name = normalizeString(fields[f]);
                for (int i = 0; i < attributeTypes.length; i++)
                {
                    description = normalizeString(attributeTypes[i].getDescription());
                    if (name.equals(description))
                    {
                        fields[f] = attributeTypes[i].getName();
                        break;
                    }
                }
            }
        }

        // disconnect (methodName);
        
        return fields;
    }

    public String[] attributeNameToDescription(String index, String[] fields,DKDatastoreICM datastore) 
    {
        String methodName = "attributeNameToDescription(String index, String[] fields)";
        // connect (methodName);

        dkAttrDef[] attributeTypes = getIndexFieldAttributes(index);
        String description = null;
        String name = null;
        if (attributeTypes != null)
        {
            for (int f = 0; f < fields.length; f++)
            {
                description = normalizeString(fields[f]);
                for (int i = 0; i < attributeTypes.length; i++)
                {
                    name = normalizeString(attributeTypes[i].getName());
                    if (name.equals(description))
                    {
                        fields[f] = attributeTypes[i].getDescription();
                        break;
                    }
                }
            }
        }

        //below line commented by Krishna
        // disconnect (methodName);
        return fields;
    }

    public String workflowDescriptionToName(String workflow)
    {
        String methodName = "workflowDescriptionToName(String workflow)";
        DKDatastoreICM datastore = connect(methodName);

        String name;
        String description;

        // convert the workflow descritive name to the real workflow name

        try
        {
            // Obtain the Routing Management object.
            DKDocRoutingServiceMgmtICM routingMgmt = new DKDocRoutingServiceMgmtICM(datastore);
            if (routingMgmt != null)
            {
                // Obtain all Work Lists in the System.
                dkCollection workLists = routingMgmt.listWorkLists();
                if (workLists != null)
                {
                    dkIterator iter = workLists.createIterator();
                    while (iter != null && iter.more())
                    {
                        // Move pointer to next element and obtain that next
                        // element.
                        DKWorkListICM workList = (DKWorkListICM) iter.next();

                        if (workList != null && (workflow.equalsIgnoreCase(workList.getDescription()) || workflow.equalsIgnoreCase(workList.getName())))
                        {
                            // change the workflow name
                            workflow = workList.getName();
                            logger.debug("Renaming workflow " + workList.getDescription() + " to " + workflow);
                        }
                    }
                }
            }
        } catch (Exception exc)
        {
            logger.debug("Exception renaming workflow : " + workflow);
            logger.debug(exc.toString());
        }

      //below line commented by Krishna
      //  disconnect(methodName);
        return workflow;

    }

    public void forceDisconnect()
    {
        try
        {
            ConnectionPoolItem item = null;
            synchronized (connectionPool)
            {
                logger.debug(" forcing closed connections");
                for (int i = 0; i < CONNECTIONPOOLSIZE; i++)
                {
                    // find unused connections
                    if (connectionPool[i] != null)
                    {
                        item = connectionPool[i];
                        if (item != null)
                        {
                            if (connectionPool[i].isConnected == Boolean.TRUE)
                            {
                                if (connectionPool[i].connectionCount == 0)
                                {
                                    logger.debug("forcing disposed connection # " + item.connectionNumber);
                                    item.dispose();
                                } else
                                {
                                	if (isCascadeFailures() || item.threadId.equals(getThreadID()) )
                                	{
                                        logger.debug(" forcing closed connection # " + item.connectionNumber);
                                        item.shouldDispose = true;
                                	}
                                }
                            }
                        }
                    }
                }
                connectionPool.notifyAll();
            }
        } catch (Exception exc)
        {
            exc.printStackTrace();
        }
    }

    private DKDatastoreICM connect(String methodName)
    {

        while (true)
        {
            try
            {
                ConnectionPoolItem item = null;
                synchronized (connectionPool)
                {
                    // see if I have one open allready
                    for (int i = 0; i < CONNECTIONPOOLSIZE; i++)
                    {
                        if (connectionPool[i].threadId != null)
                        {
                            if (connectionPool[i].threadId.equals(getThreadID()))
                            {
                                item = connectionPool[i];
                            }
                        }
                    }
                    for (int i = 0; ((item == null) && (i < CONNECTIONPOOLSIZE)); i++)
                    {
                        // found an unsed connection
                        if (connectionPool[i].threadId == null)
                        {
                            connectionPool[i].threadId = getThreadID();
                            item = connectionPool[i];
                            item.firstReserved = new Date().getTime();
                        }
                    }
                    connectionPool.notifyAll();

                    if (item != null)
                    {
                        if (item.connectionCount == 0)
                        {
                            logger.debug(" using connection # " + item.connectionNumber + " from method " + methodName);
                        } else
                        {
                            logger.debug(getThreadID() + " using connection # " + item.connectionNumber + " from method " + methodName);
                        }
                        item.connectionCount++;
                        connectToCM(item);
                        return item.dsDL;
                    }
                }
            } catch (Exception exc)
            {
                handleException(exc);
                return null;
            }
        }
    }
    

    private DKDocRoutingServiceICM connectWorkflow(String methodName)
    {

        try
        {
            ConnectionPoolItem item = null;
            synchronized (connectionPool)
            {
                // find the connection open allready
                for (int i = 0; i < CONNECTIONPOOLSIZE; i++)
                {
                    if (connectionPool[i].threadId != null)
                    {
                        if (connectionPool[i].threadId.equals(getThreadID()))
                        {
                            item = connectionPool[i];
                            if (item.isWorkflowConnected.equals(Boolean.FALSE))
                            {
                                logger.debug(" creating workflow connection # " + item.connectionNumber + " from method " + methodName);
                                item.routingService = new DKDocRoutingServiceICM(item.dsDL);
                                item.isWorkflowConnected = Boolean.TRUE;
                            } else if (item.routingService == null)
                            {
                                logger.debug(" creating workflow connection # " + item.connectionNumber + " from method " + methodName);
                                item.routingService = new DKDocRoutingServiceICM(item.dsDL);
                                item.isWorkflowConnected = Boolean.TRUE;
                            } else if (item.routingService.isConnected() == false)
                            {
                                logger.debug(" recreating workflow connection # " + item.connectionNumber + " from method " + methodName);
                                item.routingService = new DKDocRoutingServiceICM(item.dsDL);
                                item.isWorkflowConnected = Boolean.TRUE;
                            } else
                            {
                                logger.debug(" reusing workflow connection # " + item.connectionNumber + " from method " + methodName);
                            }

                            return item.routingService;
                        }
                    }
                }
            }
        } catch (Exception exc)
        {
            handleException(exc);
        }

        logger.debug( " workflow connection error " + " from method " + methodName);
        return null;
    }

    private DKDatastoreICM connectToCM(ConnectionPoolItem connectionPoolItem)
    {
       // DOMConfigurator.configure(XMLLogFilename);
        if ((connectionPoolItem.dsDL == null) || (connectionPoolItem.isConnected == Boolean.FALSE))
        {
            logger.debug(" with user id " + getUserid());
            getServerList();
            if (connectionPoolItem.idler == null)
            {
                connectionPoolItem.idler = new IdleMonitor(connectionPoolItem);
            }

            if (logonDigitalLibrary(getUserid(), getPassword(), getServer(), connectionPoolItem))
            {
                logger.debug("connected to server");
                connectionPoolItem.isConnected = Boolean.TRUE;

                // if this is the zero connection, try recaching
                if (connectionPoolItem.connectionNumber == 0)
                {
                    //DOMConfigurator.configure(XMLLogFilename);
                    recacheEntityCache(connectionPoolItem.dsDL);
                }

            } else
            {
                logger.debug("connection failed");
            }
        }
        connectionPoolItem.idler.update();

        return connectionPoolItem.dsDL;
    }

    /**
     * Creates a new document in Content Manager <BR>
     * Creation date: (2/12/2004 4:39:56 PM) <BR>
     * <code><pre>
     * ContentManagerRMIInterface cm = (ContentManagerRMIInterface) Naming.lookup(&quot;//don29.pok.ibm.com:6028/ContentMangerRMIServer/&quot;);
     * String[] fields = { &quot;Contract Number&quot;, &quot;Customer Number&quot;, &quot;Customer Name&quot;, &quot;Userid&quot;, &quot;Source&quot;, &quot;Timestamp&quot; };
     * String[] values = { &quot;SBC040212&quot;, &quot;SB100&quot;, &quot;STEVE B&quot;, &quot;US821083&quot;, &quot;RMI&quot;, new java.util.Date().toString() };
     * String data = &quot;The contract is the invoice&quot;;
     * String objectId = null;
     * try
     * {
     *     objectId = cm.createDocument(&quot;Contract US&quot;, fields, values, data.getBytes(), &quot;text/plain&quot;);
     *     if (objectId == null)
     *         debug(&quot;Contract creation failed&quot;);
     *     else
     *         debug(&quot;Contract created : &quot; + objectId);
     * } catch (RemoteException exc)
     * {
     *     debug(&quot;error creating document : &quot;);
     *     return;
     * }
     * </pre></code><BR>
     * 
     * @return java.lang.String -- The object id of the created document. null
     *         if no document was created.
     * @param index --
     *            java.lang.String -- Name of index class to create document in
     * @param fields --
     *            java.lang.String[] -- Fields of the index class to be
     *            populated
     * @param values --
     *            java.lang.String[] -- Values of the index class fields to be
     *            populated
     * @param documentData --
     *            byte[] -- Byte array containing the content of the document to
     *            be created
     * @param mimeType --
     *            java.lang.String -- Mimetype of the contents, e.g.
     *            &lt;application/pdf&gt; or &lt;text/plain&gt;
     */

    public String createDocument(String index, String[] fields, String[] values, byte[] documentData, String mimeType,DKDatastoreICM datastore) 
    {
        String methodName = "createDocument(String index, String[] fields, String[] values, byte[] documentData, String mimeType)";

        // do some cm stuff
        try
        {
        	//below line commented by Krishna	
           //connect(methodName);

            // return the ddo object id
            String pid = createDocument(index, fields, values, documentData, mimeType, null,datastore);

            //below line commented by Krishna
           //disconnect(methodName);
           //// datastore.disconnect();
            return pid;

        } catch (Exception exc)
        {
            handleException(exc);
        }
      //below line commented by Krishna
      // disconnect(methodName);
        return (null);
    }

    /**
     * Creates a new document in Content Manager <BR>
     * Creation date: (2/12/2004 4:39:56 PM) <BR>
     * <code><pre>
     * ContentManagerRMIInterface cm = (ContentManagerRMIInterface) Naming.lookup(&quot;//don29.pok.ibm.com:6028/ContentMangerRMIServer/&quot;);
     * String[] fields = { &quot;Contract Number&quot;, &quot;Customer Number&quot;, &quot;Customer Name&quot;, &quot;Userid&quot;, &quot;Source&quot;, &quot;Timestamp&quot; };
     * String[] values = { &quot;SBC040212&quot;, &quot;SB100&quot;, &quot;STEVE B&quot;, &quot;US821083&quot;, &quot;RMI&quot;, new java.util.Date().toString() };
     * String data = &quot;The contract is the invoice&quot;;
     * String objectId = null;
     * try
     * {
     *     objectId = cm.createDocument(&quot;Contract US&quot;, fields, values, data.getBytes(), &quot;text/plain&quot;);
     *     if (objectId == null)
     *         debug(&quot;Contract creation failed&quot;);
     *     else
     *         debug(&quot;Contract created : &quot; + objectId);
     * } catch (RemoteException exc)
     * {
     *     debug(&quot;error creating document : &quot;);
     *     return;
     * }
     * </pre></code><BR>
     * 
     * @return java.lang.String -- The object id of the created document. null
     *         if no document was created.
     * @param index --
     *            java.lang.String -- Name of index class to create document in
     * @param fields --
     *            java.lang.String[] -- Fields of the index class to be
     *            populated
     * @param values --
     *            java.lang.String[] -- Values of the index class fields to be
     *            populated
     * @param documentData --
     *            byte[] -- Byte array containing the content of the document to
     *            be created
     * @param mimeType --
     *            java.lang.String -- Mimetype of the contents, e.g.
     *            &lt;application/pdf&gt; or &lt;text/plain&gt;
     */

    public String createDocument(String index, String[] fields, String[] values, byte[] documentData, String mimeType, String fileName,DKDatastoreICM datastore) 
    {
        String methodName = "createDocument(String index, String[] fields, String[] values, byte[] documentData, String mimeType, String fileName)";

        // do some cm stuff
        try
        {
            //connect(methodName);

            byte[][] multiDocumentData = new byte[1][1];
            multiDocumentData[0] = documentData;
            String[] multiMimeTypes = new String[1];
            multiMimeTypes[0] = mimeType;
            String[] fileNames = new String[1];
            fileNames[0] = fileName;

            String pid = createDocument(index, fields, values, multiDocumentData, multiMimeTypes, fileNames,datastore);
            
          //below line commented by Krishna
          //disconnect(methodName);
            return pid;

        } catch (Exception exc)
        {
            handleException(exc);
        }
       //below line commented by Krishna
       // disconnect(methodName);
        return (null);
    }

    /**
     * Creates a new document in Content Manager <BR>
     * Creation date: (2/12/2004 4:39:56 PM) <BR>
     * <code><pre>
     * ContentManagerRMIInterface cm = (ContentManagerRMIInterface) Naming.lookup(&quot;//don29.pok.ibm.com:6028/ContentMangerRMIServer/&quot;);
     * String[] fields = { &quot;Contract Number&quot;, &quot;Customer Number&quot;, &quot;Customer Name&quot;, &quot;Userid&quot;, &quot;Source&quot;, &quot;Timestamp&quot; };
     * String[] values = { &quot;SBC040212&quot;, &quot;SB100&quot;, &quot;STEVE B&quot;, &quot;US821083&quot;, &quot;RMI&quot;, new java.util.Date().toString() };
     * String data = &quot;The contract is the invoice&quot;;
     * String objectId = null;
     * try
     * {
     *     objectId = cm.createDocument(&quot;Contract US&quot;, fields, values, data.getBytes(), &quot;text/plain&quot;);
     *     if (objectId == null)
     *         debug(&quot;Contract creation failed&quot;);
     *     else
     *         debug(&quot;Contract created : &quot; + objectId);
     * } catch (RemoteException exc)
     * {
     *     debug(&quot;error creating document : &quot;);
     *     return;
     * }
     * </pre></code><BR>
     * 
     * @return java.lang.String -- The object id of the created document. null
     *         if no document was created.
     * @param index --
     *            java.lang.String -- Name of index class to create document in
     * @param fields --
     *            java.lang.String[] -- Fields of the index class to be
     *            populated
     * @param values --
     *            java.lang.String[] -- Values of the index class fields to be
     *            populated
     * @param multiDocumentData --
     *            byte[][] -- Byte arrays containing the content of the
     *            documents to be created
     * @param mimeType --
     *            java.lang.String[] -- Mimetypes of the contents, e.g.
     *            &lt;application/pdf&gt; or &lt;text/plain&gt;
     */

    public String createDocument(String index, String[] fields, String[] values, byte[][] documentData, String[] mimeType,DKDatastoreICM datastore) 
    {
        String methodName = "createDocument(String index, String[] fields, String[] values, byte[][] documentData, String[] mimeType)";

        String[] fileNames = new String[mimeType.length];

        try
        {
           // DKDatastoreICM datastore = connect(methodName);

            String pid = createDocument(index, fields, values, documentData, mimeType, fileNames,datastore);
            // return the ddo object id
            //below line commented by Krishna
           // disconnect(methodName);
           // datastore.disconnect();
            return pid;
        } catch (Exception exc)
        {
            handleException(exc);
        }
      //below line commented by Krishna
       // disconnect(methodName);
        
/*        try {
			//datastore.disconnect();
		} catch (DKException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
        return (null);

    }

    /**
     * Creates a new document in Content Manager <BR>
     * Creation date: (2/12/2004 4:39:56 PM) <BR>
     * <code><pre>
     * ContentManagerRMIInterface cm = (ContentManagerRMIInterface) Naming.lookup(&quot;//don29.pok.ibm.com:6028/ContentMangerRMIServer/&quot;);
     * String[] fields = { &quot;Contract Number&quot;, &quot;Customer Number&quot;, &quot;Customer Name&quot;, &quot;Userid&quot;, &quot;Source&quot;, &quot;Timestamp&quot; };
     * String[] values = { &quot;SBC040212&quot;, &quot;SB100&quot;, &quot;STEVE B&quot;, &quot;US821083&quot;, &quot;RMI&quot;, new java.util.Date().toString() };
     * String data = &quot;The contract is the invoice&quot;;
     * String objectId = null;
     * try
     * {
     *     objectId = cm.createDocument(&quot;Contract US&quot;, fields, values, data.getBytes(), &quot;text/plain&quot;, &quot;filename.txt&quot;);
     *     if (objectId == null)
     *         debug(&quot;Contract creation failed&quot;);
     *     else
     *         debug(&quot;Contract created : &quot; + objectId);
     * } catch (RemoteException exc)
     * {
     *     debug(&quot;error creating document : &quot;);
     *     return;
     * }
     * </pre></code><BR>
     * 
     * @return java.lang.String -- The object id of the created document. null
     *         if no document was created.
     * @param index --
     *            java.lang.String -- Name of index class to create document in
     * @param fields --
     *            java.lang.String[] -- Fields of the index class to be
     *            populated
     * @param values --
     *            java.lang.String[] -- Values of the index class fields to be
     *            populated
     * @param multiDocumentData --
     *            byte[][] -- Byte arrays containing the content of the
     *            documents to be created
     * @param mimeType --
     *            java.lang.String[] -- Mimetypes of the contents, e.g.
     *            &lt;application/pdf&gt; or &lt;text/plain&gt;
     * @param fileName --
     *            java.lang.String[] -- File names of the contents
     */
    public String createDocument(String index, String[] fields, String[] values, byte[][] documentData, String[] mimeType, String[] fileNames,DKDatastoreICM datastore) 
    {
        String methodName = "createDocument(String index, String[] fields, String[] values, byte[][] documentData, String[] mimeType, String[] fileNames)";
        // do some cm stuff
        try
        {
           // DKDatastoreICM datastore = connect(methodName);

            // convert from the description to the name
            index = indexDescriptionToName(index, datastore);
            attributeDescriptionToName(index, fields,datastore);

            // Create new DDO/XDO Object.
            logger.debug("datastore createDDO(" + index + " ,DKConstant.DK_CM_DOCUMENT)");
            DKDDO aDDO = datastore.createDDO(index, DKConstant.DK_CM_DOCUMENT);

            setAttributes(aDDO, index, fields, values,datastore);

            // load the data
            DKLobICM lob = null;
            DKParts dkParts = (DKParts) aDDO.getData(aDDO.dataId(DKConstant.DK_CM_NAMESPACE_ATTR, DKConstant.DK_CM_DKPARTS));

            for (int i = 0; i < documentData.length && i < fileNames.length && i < mimeType.length && documentData[i].length > 0; i++)
            {
                logger.debug("datastore createDDO(\"ICMBASE\", DKConstantICM.DK_ICM_SEMANTIC_TYPE_BASE)");
                lob = (DKLobICM) datastore.createDDO("ICMBASE", DKConstantICM.DK_ICM_SEMANTIC_TYPE_BASE);
                lob.setContent(documentData[i]);
                lob.setMimeType(mimeType[i]);
                lob.setOrgFileName(fileNames[i]);

                dkParts.addElement(lob);
            }

            // persist the ddo
            logger.debug("datastore aDDO.add()");
            aDDO.add();

            String pid = aDDO.getPidObject().getPrimaryId();

            // return the ddo object id
            
          //below line commented by Krishna
           // disconnect(methodName);
           // datastore.disconnect();
            return pid;
        } catch (DKUsageError exc)
        {
            handleException(exc);
        } catch (DKException exc)
        {
            handleException(exc);
        } catch (Exception exc)
        {
            handleException(exc);
        }
        
       //below line commented by Krishna
       // disconnect(methodName);
/*        try {
		//	datastore.disconnect();
		} catch (DKException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
        return (null);
    }

    /**
     * Creates a new folder in Content Manager <BR>
     * Creation date: (2/12/2004 4:39:56 PM) <code><pre>
     * ContentManagerRMIInterface cm = (ContentManagerRMIInterface) Naming.lookup(&quot;//don29.pok.ibm.com:6028/ContentMangerRMIServer/&quot;);
     * String[] fields = { &quot;Contract Number&quot;, &quot;Customer Number&quot;, &quot;Customer Name&quot;, &quot;Userid&quot;, &quot;Source&quot;, &quot;Timestamp&quot; };
     * String[] values = { &quot;SBC040212&quot;, &quot;SB100&quot;, &quot;STEVE B&quot;, &quot;US821083&quot;, &quot;RMI&quot;, new java.util.Date().toString() };
     * String folderId;
     * try
     * {
     *     folderId = cm.createFolder(&quot;Contract Folder US&quot;, fields, values);
     *     if (folderId == null)
     *         debug(&quot;Folder creation failed&quot;);
     *     else
     *         debug(&quot;Folder created : &quot; + folderId);
     * } catch (RemoteException exc)
     * {
     *     debug(&quot;error creating folder : &quot;);
     *     return;
     * }
     * </pre></code><BR>
     * 
     * @return java.lang.String -- The object id of the created folder. null if
     *         no document was created.
     * @param index --
     *            java.lang.String -- Name of index class to create document in
     * @param fields --
     *            java.lang.String[] -- Fields of the index class to be
     *            populated
     * @param values --
     *            java.lang.String[] -- Values of the index class fields to be
     *            populated
     * @throws Exception 
     */
    public String createFolder(String index, String[] fields, String[] values,DKDatastoreICM datastore) throws Exception 
    {
        String methodName = "createFolder(String index, String[] fields, String[] values)";
        try
        {
           // DKDatastoreICM datastore = connect(methodName);

            // convert from the description to the name
        	datastore.connection();
            index = indexDescriptionToName(index,datastore);
            attributeDescriptionToName(index, fields,datastore);

            // create an item
            logger.debug("datastore createDDO(" + index + ", DKConstant.DK_CM_FOLDER)");
            DKDDO aDDO = (DKDDO) datastore.createDDO(index, DKConstant.DK_CM_FOLDER);
            
            logger.debug("aDDO:"+aDDO);

            setAttributes(aDDO, index, fields, values,datastore);
            

            // persist the ddo
           // logger.debug("datastore aDDO.add()");
            aDDO.add();
            
            //below line commented by Krishna

           // disconnect(methodName);
           //datastore.disconnect();

            // return the ddo object id
            return (aDDO.getPidObject().getPrimaryId());
        } catch (DKUsageError exc)
        {
        	logger.fatal("in create folder DKUsageError:"+exc);
            handleException(exc);
            throw exc;
            
        } catch (DKException exc)
        {
        	logger.fatal("in create folder DKException:"+exc);
            handleException(exc);
            throw exc;
            
        } catch (Exception exc)
        {
        	logger.fatal("in create folder Exception:"+exc);
            handleException(exc);
            throw exc;
        }

        //below line commented by Krishna
       // disconnect(methodName);
        /*try {
			//datastore.disconnect();
		} catch (DKException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
      
    }

  /*  private void disconnect(String methodName)
    {
        try
        {
            for (int i = 0; i < CONNECTIONPOOLSIZE; i++)
            {
                synchronized (connectionPool)
                {
                    if (connectionPool[i].threadId != null)
                    {
                        if (connectionPool[i].threadId.equals(getThreadID()))
                        {
                            connectionPool[i].connectionCount--;
                            if (connectionPool[i].connectionCount <= 0)
                            {
                                connectionPool[i].threadId = null;
                                long duration = new Date().getTime() - connectionPool[i].firstReserved;
                                logger.debug("connection # " + connectionPool[i].connectionNumber + " free after " + duration + " ms from method " + methodName);
                                if (connectionPool[i].shouldDispose)
                                {
                                    logger.debug("connection # " + connectionPool[i].connectionNumber + " forced disconnect from method " + methodName);
                                    connectionPool[i].dispose();
                                }
                            } else
                            {
                                logger.debug("connection # " + connectionPool[i].connectionNumber + " disconnect from method " + methodName);
                            }
                            return;
                        }
                    }
                    connectionPool.notifyAll();
                }
            }
        } catch (Exception exc)
        {
            handleException(exc);
        }
    }*/

    /**
     * Retrieves the contents of documents matching a query from Content Manager
     * and formats them in xml <BR>
     * Creation date: (2/18/2004 4:55:14 PM) <code><pre>
     * try
     * {
     *     ContentManagerRMIInterface cm = (ContentManagerRMIInterface) Naming.lookup(&quot;//don29.pok.ibm.com:6028/ContentMangerRMIServer/&quot;);
     *     String searchText = &quot;@ITEMID='ASDFASDFASDFASDFASDFASDFASD'&quot;;
     *     String data = cm.executeQuery(searchText);
     *     if (data == null)
     *         logger.debug(&quot;docs not found&quot;);
     *     else
     *         logger.debug(&quot;docs retrieved&quot;);
     * } catch (RemoteException exc)
     * {
     *     logger.debug(&quot;error retrieving contract : &quot;);
     *     return;
     * }
     * 
     * 
     * </pre></code><BR>
     * 
     * @return java.lang.String -- the parametric query to execute
     * @param query
     *            java.lang.String -- the xml data for the results
     */
    public String executeQuery(String searchText,DKDatastoreICM datastore)
    {
        try
        {
            String xml = executeQuery("*", searchText,datastore);
            return xml;
        } catch (Exception exc)
        {
            handleException(exc);
        }

        return null;
    }

    /**
     * Retrieves the contents of documents matching a query from Content Manager
     * and formats them in xml <BR>
     * Creation date: (2/18/2004 4:55:14 PM) <code><pre>
     * try
     * {
     *     ContentManagerRMIInterface cm = (ContentManagerRMIInterface) Naming.lookup(&quot;//don29.pok.ibm.com:6028/ContentMangerRMIServer/&quot;);
     *     String searchText = &quot;SEARCH=(INDEX_CLASS='Contract US', MAX_RESULTS=100, COND=('Contract Number' = 'SBI1000')); OPTION=(CONTENT=NO; TYPE_QUERY=DYNAMIC; TYPE_FILTER=FOLDERDOC)&quot;;
     *     String data = cm.executeQuery(searchText);
     *     if (data == null)
     *         logger.debug(&quot;docs not found&quot;);
     *     else
     *         logger.debug(&quot;docs retrieved&quot;);
     * } catch (RemoteException exc)
     * {
     *     logger.debug(&quot;error retrieving contract : &quot;);
     *     return;
     * }
     * 
     * 
     * </pre></code><BR>
     * 
     * @return java.lang.String -- the parametric query to execute
     * @param query
     *            java.lang.String -- the xml data for the results
     */
    public String executeQuery(String index, String searchText,DKDatastoreICM datastore)
    {
        String methodName = "executeQuery(String index, String searchText)";
        String results = null;
        try
        {
            //connect(methodName);
            
            results = executeQuery (index, searchText, MAXRECORDS,datastore);
        } catch (Exception exc)
        {
            handleException(exc);
        }

      //below line commented by Krishna
       // disconnect(methodName);
        return results;
    }
    
    public String executeQuery(String index, String searchText, int maxRecords,DKDatastoreICM datastore)
    {
    	return executeQuery(index, searchText, maxRecords, true,datastore);
    }
    
    public String executeQuery(String index, String searchText, int maxRecords, boolean includeImageData,DKDatastoreICM datastore)
    {
        String methodName = "executeQuery(String index, String searchText, int maxRecords)";

        try {
            // convert from the description to the name
        	datastore.connection();
            index = indexDescriptionToName(index,datastore);

            StringBuffer results = new StringBuffer();

            results.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
            results.append("<QUERYRESULTS>");
            results.append("<!-- Results from a parametric search of Content Manager -->");

            DKResults pCur = getObjectsFromLibrary(index, searchText, maxRecords,  includeImageData,datastore);

            // parse the results of the query
            DKDDO item = null;
            if (pCur != null)
            {
                dkIterator iter = pCur.createIterator();
                while (iter.more())
                {
                    item = (DKDDO) iter.next();
                    if (item != null)
                    {
                        results.append(formatItem(item, false, includeImageData,datastore));
                    }
                }
            }

            results.append("</QUERYRESULTS>");

          //below line commented by Krishna
           //disconnect(methodName);
            
           /* if (datastore!=null)
             // datastore.disconnect();*/
            
            return results.toString();
        } catch (DKException exc)
        {
            if (exc.getErrorId() != 6057)
            {
                logger.debug(" : Query Error " + exc.getErrorId());
                handleException(exc);
            } else
            {
                logger.debug(" : " + exc.toString());
            }
        } catch (Exception exc)
        {
            handleException(exc);
        }

        
        //below line commented by Krishna
       //disconnect(methodName);
       /* try {
			datastore.disconnect();
		} catch (DKException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
        return null;
    }

    /**
     * Retrieves the contents of documents matching a query from Content Manager
     * and formats them in xml <BR>
     * Performs a search on the index fields provided and retrieves the
     * documents that matches <BR>
     * <code><pre>
     * ContentManagerRMIInterface cm = (ContentManagerRMIInterface) Naming.lookup(&quot;//don29.pok.ibm.com:6028/ContentMangerRMIServer/&quot;);
     * String[] fields = { &quot;Contract Number&quot;, &quot;Customer Number&quot; };
     * String[] values = { &quot;SBC040212&quot;, &quot;SB100&quot; };
     * String[] data = cm.executeQuery(&quot;Contract US&quot;, fields, values);
     * try
     * {
     *     if (data == null)
     *         logger.debug(&quot;docs not found&quot;);
     *     else
     *         logger.debug(&quot;docs retrieved&quot;);
     * } catch (RemoteException exc)
     * {
     *     logger.debug(&quot;error retrieving contract : &quot;);
     *     return;
     * }
     * 
     * 
     * </pre></code><BR>
     * 
     * Creation date: (2/12/2004 4:39:56 PM)
     * 
     * @return String -- xml stream of data containing information about the
     *         documents retrieved
     * @param index --
     *            java.lang.String -- Name of index class to search
     * @param fields --
     *            java.lang.String[] -- Fields of the index class to be searched
     *            on
     * @param values --
     *            java.lang.String[] -- Values of the index class fields to be
     *            searched on
     */
    public String executeQuery(String index, String[] fields, String[] values,DKDatastoreICM datastore) 
    {
    	logger.debug("inside contentManagerImplemnetation executequery call");
    	return executeQuery(index, fields, values, true,datastore);
    }
    
    public String executeQuery(String index, String[] fields, String[] values, boolean includeImageData,DKDatastoreICM datastore ) 
    {
        String methodName = "executeQuery(String index, String[] fields, String[] values)";
        try
        {
            
        	 //below line commneted by Krishna
        	//connect(methodName);
            datastore.connection();
            
            logger.debug("inside contentManagerImplemnetation executequery implementation call");
            
            logger.debug("index before method call :"+index);

            // convert from the description to the name
            index = indexDescriptionToName(index,datastore);
            
            logger.debug("index in execute query is :"+index);
            
            attributeDescriptionToName(index, fields,datastore);

            String searchText = "[";

            for (int i = 0; i < fields.length && i < values.length; i++)
            {
                searchText += "@" + fields[i] + " = \"" + values[i] + "\" ";
                if (i < fields.length - 1)
                {
                    searchText += " and ";
                }
            }
            searchText += "]";

            //below line commented by Krishna
           // disconnect(methodName);
            
            //datastore.disconnect();
            return executeQuery(index, searchText,datastore);

        } catch (Exception exc)
        {
            handleException(exc);
        }

      //below line commented by Krishna
        //disconnect(methodName);
        
/*        try {
			//datastore.disconnect();
		} catch (DKException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
        return (null);
    }

    /**
     * Insert the method's description here. Creation date: (2/18/2004 6:54:00
     * PM)
     * 
     * @return java.lang.String
     * @param item
     *            com.ibm.mm.sdk.common.DKDDO
     */
    private String formatItem(DKDDO item, boolean showURL,DKDatastoreICM datastore)
    {
    	return formatItem(item, showURL, true,datastore);
    }
    
    private String formatItem(DKDDO item, boolean showURL, boolean showImages,DKDatastoreICM datastore)
    {
        String[] fields = null;
        String[] names = null;
        Object[] values = null;
        String type = null;
        StringBuffer results = new StringBuffer();

        try
        {
            fields = getIndexFields(item);
            values = getIndexValues(item);
            type = getItemType(item);

            // copy the names
            names = new String[fields.length];
            for (int i = 0; ((fields != null) && (i < fields.length)); i++)
            {
                names[i] = fields[i];
            }

            // convert from the name to the description
            attributeNameToDescription(item.getObjectType(), fields,datastore);

            results.append("<" + type + " pid=\"" + item.getPidObject().getPrimaryId() + "\" index=\"" + item.getObjectType() + "\">");

            for (int i = 0; ((fields != null) && (values != null) && (i < fields.length) && (i < values.length)); i++)
            {
                if (fields[i].equals(DKDDO.DKFOLDER))
                {
                    DKFolder folder = (DKFolder) values[i];
                    if (folder != null)
                    {
                        dkIterator iter = folder.createIterator();
                        DKDDO mddo;
                        int count = 0;
                        while (iter.more())
                        {
                            mddo = (DKDDO) iter.next();
                            logger.debug("datastore mddo.retrieve (DKConstant.DK_CM_CONTENT_YES + DKConstant.DK_CM_CONTENT_ITEMTREE)");
                            mddo.retrieve(DKConstant.DK_CM_CONTENT_ATTRONLY);
                            results.append(formatItem(mddo, false,datastore));
                            count++;
                        }
                        if (count == 0)
                        {
                            logger.debug("Empty folder");
                        }
                    }

                } else if (values[i] instanceof dkCollection)
                {
                } else if ((values[i] instanceof DKParts) || (fields[i].equals("DKParts")))
                {
                } else if (values[i] == null)
                {
                    results.append("<index field=\"" + formatXML(fields[i]) + "\" name=\"" + formatXML(names[i]) + "\" value=\"" + "null" + "\"/>");
                } else
                {
                    results.append("<index field=\"" + formatXML(fields[i]) + "\" name=\"" + formatXML(names[i]) + "\" value=\"" + formatXML(values[i].toString()) + "\"/>");
                }
            }
            
            dkXDO[] dkxdo = null;
            if (showImages)
            {
            	dkxdo = getImageAttributesFromDatastore(item);
            }
            if ((dkxdo != null) && (dkxdo.length > 0))
            {
                for (int i = 0; i < dkxdo.length; i++)
                {
                    results.append("<image partnum=\"" + i + "\"");
                    results.append(" mimetype=\"" + dkxdo[i].getMimeType() + "\"");
                    results.append(" size=\"" + dkxdo[i].size() + "\"");
                    if (dkxdo[i] instanceof DKLobICM)
                    {
                        results.append(" orgfilename=\"" + formatXML(((DKLobICM) dkxdo[i]).getOrgFileName()) + "\"");
                        
                        if (showURL)
                        {
                            try {
                                String[] urls = ((DKLobICM)dkxdo[i]).getContentURLs(DKConstant.DK_CM_VERSION_LATEST , -1, -1, 1);
                                if ((urls != null ) && (urls.length > 0))
                                {
                                    for (int u=0; u<urls.length; u++)
                                    {
                                        results.append (" url=\"" +
                                                formatXML(urls[u]) + "\"");
                                        results.append (" resourcemanager=\"" +
                                                ((DKLobICM)dkxdo[i]).getRMName() + "\"");
                                    }
                                }
                            } catch (Exception e) {
                                
                            }
                         
                        }
                        results.append(" created=\"" + ((DKLobICM) dkxdo[i]).getCreatedTimestamp() + "\"");
                    }
                    results.append("/>");
                }
            }

            results.append("</" + type + ">");

            return results.toString();
        } catch (Exception exc)
        {
            handleException(exc);
        }

        return "";
    }

     
    private String formatXML(String text)
    {
      if (text == null) {
        return "";
      }
      String newText = text;
      String[] badValues = { "\"", "&", "<", ">", "'" };
      String[] goodValues = { "&quot;", "&amp;", "&lt;", "&gt;", "&apos;" };
      
      int offset = 0;
      int length = 0;
      for (int i = 0; i < badValues.length; i++)
      {
        offset = newText.indexOf(badValues[i]);
        length = badValues[i].length();
        while (offset > 0)
        {
          newText = newText.substring(0, offset) + goodValues[i] + newText.substring(offset + length);
          offset = newText.indexOf(badValues[i], offset + length);
        }
      }
      return newText;
    }
    
    public static void main (String[] args)
    {
    	ContentManagerImplementation oneOfThis;
		try {
			String objectId = "A1001001A10C31B21426G97164";
			if (args.length > 0)
				objectId = args[0];
			
			String partCreateTimestampPrefix = "2010-03-31 ";
			String partCreateTimestampSuffix = ":30:00.000";
			for (int i=10; i<24; i++)
			{
				Date partCreateDate = timeStampFormat.parse(partCreateTimestampPrefix + i + partCreateTimestampSuffix);
				System.out.println (partCreateDate);
			}
			System.exit(0);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }

    /**
     * Retrieves the created date for an object Creation date: (2/18/2004
     * 5:04:14 PM)
     * 
     * @return java.util.Date -- the created date
     * @param index --
     *            DKDDO -- the object to retrieve the created date for
     */
    private java.util.Date getCreatedDate(dkXDO part)
    {
        try
        {
            String partCreateTimestamp = ((DKLobICM) part).getCreatedTimestamp();
            java.util.Date partCreateDate = new Date(0);
            try {
                partCreateDate = timeStampFormat.parse(partCreateTimestamp);
                logger.debug(part.getPidObject().getPrimaryId() + " Created " + partCreateTimestamp + " ?= " + partCreateDate);
            } catch (Exception e) {
                // do nuthing
            }
            return (partCreateDate);

        } catch (Exception exc)
        {
            handleException(exc);
        }
        return (null);
    }

    /**
     * Retrieves the created date for an object Creation date: (2/18/2004
     * 5:04:14 PM)
     * 
     * @return java.lang.String -- the created date
     * @param index --
     *            DKDDO -- the object to retrieve the created date for
     */
    private String getCreatedTimestamp(dkXDO part)
    {
        try
        {
            String partCreateTimestamp = ((DKLobICM) part).getCreatedTimestamp();
            return (partCreateTimestamp);

        } catch (Exception exc)
        {
            handleException(exc);
        }
        return (null);
    }

    /**
     * Retrieves the created date for an object Creation date: (2/18/2004
     * 5:04:14 PM)
     * 
     * @return java.util.Date -- the created date
     * @param index --
     *            java.lang.String -- the object id to retrieve the created date
     *            for
     */
    public java.util.Date getCreatedDate(String objectId,DKDatastoreICM datastore)
    {
        String methodName = "getCreatedDate(String objectId)";
        logger.debug(methodName);
        try
        {
           // connect(methodName);
            logger.debug("retrieving object created date for " + objectId + "\n");

            DKDDO item = getObjectFromLibrary(objectId, true,datastore);
            if (item == null)
            {
                logger.debug("object not found in library server " + objectId + "\n");
            } else
            {
                dkXDO[] part = getImageAttributesFromDatastore(item);
                if ((part == null) || (part.length == 0) || (part[0] == null))
                {
                    logger.debug("object not found in object server " + objectId + "\n");
                } else
                {
                	//below line commented by Krishna
                   //disconnect(methodName);
                    return getCreatedDate(part[0]);
                }
            }

        } catch (Throwable exc)
        {	logger.fatal(exc.getMessage());
            handleException(exc);
        }

      //below line commented by Krishna
       // disconnect(methodName);
        return null;
    }

    /**
     * Retrieves the created date for an object Creation date: (2/18/2004
     * 5:04:14 PM)
     * 
     * @return java.lang.String -- the created date
     * @param index --
     *            java.lang.String -- the object id to retrieve the created date
     *            for
     */
    public String getCreatedTimestamp(String objectId,DKDatastoreICM datastore)
    {
        String methodName = "getCreatedTimestamp(String objectId)";

        try
        {
            //connect(methodName);
            logger.debug("retrieving object created timestamp for " + objectId + "\n");

            DKDDO item = getObjectFromLibrary(objectId, true,datastore);
            if (item == null)
            {
                logger.debug("object not found in library server " + objectId + "\n");
            } else
            {
                dkXDO[] part = getImageAttributesFromDatastore(item);
                if ((part == null) || (part.length == 0) || (part[0] == null))
                {
                    logger.debug("object not found in object server " + objectId + "\n");
                } else
                {
                	//below line commented by Krishna
                   //disconnect(methodName);
                    return getCreatedTimestamp(part[0]);
                }
            }

        } catch (Throwable exc)
        {
            handleException(exc);
        }
 
      //below line commented by Krishna
      //disconnect(methodName);
        return null;
    }

    private void recacheEntityCache(DKDatastoreICM datastore)
    {
        try
        {
        	
          //  synchronized (connectionPool)
          //  {
                entityCache = null;
                getEntityCache(datastore);
                //getUsersPrivilegeSetCache(datastore);
               //connectionPool.notifyAll();
         //   }
            logger.debug("Entities recached");
        } catch (Exception exc)
        {
            handleException(exc);
        }
    }
    
    
    public String getUserPrivilegeSet(String userId, DKDatastoreICM datastore){
    	
    	DKDatastoreICM dsICM = null;
    	DKDatastoreDefICM dkDef  = null;
    	DKDatastoreAdminICM dsAdminICM = null;
    	DKUserMgmtICM dkUMgmtICM = null;
    	DKUserDefICM udfUser = null;
    	DKAuthorizationMgmtICM aclMgmt = null;
    	long lPrivId = -1;
    	DKPrivilegeSetICM privSet = null;
    	
    	try {
    		
	    		dsICM = datastore;
	    		dkDef =(DKDatastoreDefICM)dsICM.datastoreDef();
	    		dsAdminICM =(DKDatastoreAdminICM) dkDef.datastoreAdmin();
	    		dkUMgmtICM = (DKUserMgmtICM)dsAdminICM.userManagement();
	    		udfUser=(DKUserDefICM)dkUMgmtICM.retrieveUserDef(userId );
	    		aclMgmt =(DKAuthorizationMgmtICM)dsAdminICM.authorizationMgmt();
	    		lPrivId = udfUser.getPrivSetCode();
	    		privSet =   (DKPrivilegeSetICM)aclMgmt.retrievePrivilegeSet(lPrivId);
    		 
    		}catch(Exception e){
    			
    			e.printStackTrace();
    			logger.fatal(e);
    			handleException(e);
    		}
    	
    	return privSet.getName();
    }
    
    private HashMap<String,String> getUsersPrivilegeSetCache(DKDatastoreICM datastore){
        try
        {
           // synchronized (connectionPool)
          //  {
            	if(usersCache == null && datastore!=null);
            	{
            		usersCache = new HashMap<String, String>();
                    DKDatastoreDefICM        dsDefICM = (DKDatastoreDefICM)datastore.datastoreDef();                   
                    DKDatastoreAdminICM      dsAdmin  = (DKDatastoreAdminICM) dsDefICM.datastoreAdmin();             
                    dkUserManagement dsUserManagement = (dkUserManagement)dsAdmin.userManagement();
                    
                    dkCollection userDefsList = dsUserManagement.listUserDefs();;
                    if (userDefsList != null)
                    {
                        dkIterator iter = userDefsList.createIterator();
                        while (iter != null && iter.more()){
                        	DKUserDefICM userDef = (DKUserDefICM) iter.next();
                        	usersCache.put(userDef.getName(), userDef.getPrivSetName());
                        }
                        logger.debug("User Privileges retrieved");
                    }
            	//}
            	//connectionPool.notifyAll();
            }
            
        } catch (Exception exc)
        {
            handleException(exc);
        }
        return usersCache;
    }

    private DKComponentTypeDefICM[] getEntityCache(DKDatastoreICM datastore)
    {
        try
        {
           // synchronized (connectionPool)
        	
       
        	//synchronized (connectionPool)
          //  {
                if (entityCache == null && datastore!=null)
                {
                    Date now = new Date();
                    logger.debug("datastore listEntities started at " + now.toString());
                    DKSequentialCollection pCol = (DKSequentialCollection) (datastore.listEntities());
                    logger.debug( (new Date().getTime() - now.getTime()));

                    dkIterator pIter = pCol.createIterator();
                    DKComponentTypeDefICM icDef;
                    int count = 0;

                    // count the non FRN ones
                    while (pIter.more())
                    {
                        icDef = (DKComponentTypeDefICM) pIter.next();
                        if (!icDef.getName().startsWith("FRN$"))
                        {
                            count++;
                        }
                        Thread.sleep(0);
                    }

                    // allocate
                    entityCache = new DKComponentTypeDefICM[count];
                    classList = new String[count];
                    attributeCache = new dkAttrDef[count][];
                    attributeList = new String[count][];
                    pIter.reset();
                    int i = 0;

                    // copy them into the cache
                    while (pIter.more())
                    {
                        icDef = (DKComponentTypeDefICM) pIter.next();
                        if (!icDef.getName().startsWith("FRN$"))
                        {
                            // retrieve the attributes
                            DKSequentialCollection pAttrCol;
                            logger.debug("datastore listEntityAttrs(" + icDef.getName() + ")");
                            pAttrCol = (DKSequentialCollection) datastore.listEntityAttrs(icDef.getName());
                            if (pAttrCol != null)
                            {
                                dkIterator pAttrIter = pAttrCol.createIterator();
                                if (pAttrIter != null)
                                {
                                    dkAttrDef attrDef;
                                    dkAttrDef[] fieldAttributes = new dkAttrDef[pAttrCol.cardinality()];
                                    String[] fieldNames = new String[pAttrCol.cardinality()];
                                    int attrNum = 0;
                                    while (pAttrIter.more())
                                    {
                                        attrDef = (dkAttrDef) pAttrIter.next();
                                        if (attrDef == null)
                                        {
                                            fieldAttributes[attrNum] = new DKAttrDefICM(datastore);
                                            fieldNames[attrNum] = "";
                                        } else
                                        {
                                            fieldAttributes[attrNum] = attrDef;
                                            fieldNames[attrNum] = attrDef.getDescription();
                                        }
                                        attrNum++;
                                    }

                                    entityCache[i] = icDef;
                                    classList[i] = icDef.getDescription();
                                    attributeCache[i] = fieldAttributes;
                                    attributeList[i] = fieldNames;
                                    i++;
                                }
                            }
                        }
                        Thread.sleep(0);
                    }

                    // copy in what we really got
                    DKComponentTypeDefICM[] tmpEntityCache = entityCache;
                    String[] tmpClassList = classList;
                    dkAttrDef[][] tmpAttributeCache = attributeCache;
                    String[][] tmpAttributeList = attributeList;

                    entityCache = new DKComponentTypeDefICM[i];
                    classList = new String[i];
                    attributeCache = new dkAttrDef[i][];
                    attributeList = new String[i][];

                    for (int j = 0; j < i; j++)
                    {
                        entityCache[j] = tmpEntityCache[j];
                        classList[j] = tmpClassList[j];
                        attributeCache[j] = tmpAttributeCache[j];
                        attributeList[j] = tmpAttributeList[j];
                    }

                    Arrays.sort(classList);
                    logger.debug("Item attributes retrieved");

                }
             //   connectionPool.notifyAll();
          //  }
        } catch (Exception exc)
        {
            handleException(exc);
        }

        return entityCache;
    }

    private dkXDO[] getImageAttributesFromDatastore(DKDDO item)
    {
        return getXDOFromDatastore(item, DKConstant.DK_CM_CONTENT_ATTRONLY);
    }

    private dkXDO[] getImageAttributesFromDatastore(String objectId,DKDatastoreICM datastore)
    {
        try
        {
            DKDDO item = getObjectFromLibrary(objectId, true,datastore);
            dkXDO[] part = getImageAttributesFromDatastore(item);
            return part;
        } catch (Exception exc)
        {
            handleException(exc);
        }
        return null;
    }

    private dkXDO[] getImageFromDatastore(DKDDO item)
    {
        return getXDOFromDatastore(item, DKConstant.DK_CM_CONTENT_ONLY);
    }

    private dkXDO[] getImageFromDatastore(String objectId,DKDatastoreICM datastore)
    {
        try
        {
            DKDDO item = getObjectFromLibrary(objectId, true,datastore);
            dkXDO[] part = getImageFromDatastore(item);
            return part;
        } catch (Exception exc)
        {
            handleException(exc);
        }
        return null;
    }

    /**
     * Retrieves the list of fields for an index class Creation date: (2/18/2004
     * 5:04:14 PM)
     * 
     * @return java.lang.String[]
     * @param index --
     *            java.lang.String -- The index class to list fields for
     */
    private dkAttrDef[] getIndexFieldAttributes(String index)
    {

        
    	//synchronized (connectionPool)
       // {
        	
        	
        	
            DKComponentTypeDefICM itemDef = null;
            for (int i = 0; i < entityCache.length; i++)
            {
                itemDef = entityCache[i];
                if (itemDef != null)
                {
                    if (itemDef.getName().equals(index))
                    {
                        //connectionPool.notifyAll();
                        return attributeCache[i];
                    }
                }
            }
           // connectionPool.notifyAll();
       // }
        return null;
    }

    /**
     * Retrieves the values for all the index fields for an object Creation
     * date: (2/18/2004 5:04:14 PM)
     * 
     * @return java.lang.String[] -- the values of all the fields
     * @param index --
     *            java.lang.String -- the object id to retrieve the list for
     */
    private String[] getIndexFields(DKDDO ddo)
    {
        try
        {
            if (ddo == null)
            {
                return null;
            }

            String[] fields = new String[ddo.dataCount()];
            short j;

            for (short i = 1; i <= ddo.dataCount(); i++)
            {
                fields[i - 1] = ddo.getDataName(i);
            }
            return fields;

        } catch (Throwable exc)
        {
            handleException(exc);
        }

        return null;
    }

    /**
     * Retrieves the list of fields for an index class Creation date: (2/18/2004
     * 5:04:14 PM)
     * 
     * @return java.lang.String[]
     * @param index --
     *            java.lang.String -- The index class to list fields for
     */
    public String[] getIndexFields(String index)
    {
        DKComponentTypeDefICM itemDef = null;
        for (int i = 0; i < entityCache.length; i++)
        {
            itemDef = entityCache[i];
            if (itemDef != null)
            {
                if (itemDef.getName().equals(index))
                {
                    return attributeList[i];
                }
            }
        }
        return null;
    }
    
    /**
     * Retrieves the list of fields for an index class to be filtered by 
     * {@link com.ibm.gil.customization.filters.request.SearchRequestFilter}.
     * 
     * @return java.lang.String[]
     * @param index --
     *            java.lang.String -- The index class to list fields for
     */
    public String[] getIndexFieldsForFiltering(String index)
    {
        DKComponentTypeDefICM itemDef = null;
        for (int i = 0; i < entityCache.length; i++)
        {
            itemDef = entityCache[i];
            if (itemDef != null)
            {
                if (itemDef.getName().equals(index))
                {
                    try {
						return itemDef.listAttrNames();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return null;
					}
                }
            }
        }
        return null;
    }

    /**
     * Retrieves the names for all the indexes Creation date: (2/18/2004 5:04:14
     * PM)
     * 
     * @return java.lang.String[] -- the names of all the indexs
     */
    public String[] getIndexNames()
    {

        try
        {
            return (classList);
        } catch (Throwable exc)
        {
            handleException(exc);
        }
        return null;
    }

    /**
     * Retrieves the values for all the index values for an object Creation
     * date: (2/18/2004 5:04:14 PM)
     * 
     * @return java.lang.String[] -- the values of all the values
     * @param index --
     *            java.lang.String -- the object id to retrieve the list for
     */
    private Object[] getIndexValues(DKDDO ddo)
    {
        try
        {
            if (ddo == null)
            {
                return null;
            }

            Object[] values = new Object[ddo.dataCount()];
            Object data = null;
            short j;

            for (short i = 1; i < ddo.dataCount(); i++)
            {
                data = ddo.getData(i);
                values[i - 1] = data;
            }
            return values;

        } catch (Throwable exc)
        {
            handleException(exc);
        }

        return null;
    }

    /**
     * Retrieves the values for all the index fields for an object Creation
     * date: (2/18/2004 5:04:14 PM)
     * 
     * @return java.lang.String[] -- the values of all the fields
     * @param index --
     *            java.lang.String -- the object id to retrieve the list for
     */
    public String[] getIndexValues(String objectId,DKDatastoreICM datastore)
    {
        String methodName = "getIndexValues(String objectId)";

        try
        {
           //connect(methodName);

            DKDDO ddo = getObjectFromLibrary(objectId, false,datastore);
            String[] indexFields = getIndexFields(ddo);

            Object[] data = getIndexValues(ddo);

            String[] results = new String[indexFields.length];
            for (int i = 0; i < results.length; i++)
            {
                if (data[i] == null)
                    results[i] = "";
                else
                    results[i] = data[i].toString();
            }
            
          //below line commented by Krishna
           //disconnect(methodName);
            return results;

        } catch (Throwable exc)
        {
            handleException(exc);
        }

      //below line commented by Krishna
       //disconnect(methodName);
        return null;
    }

    /**
     * Insert the method's description here. Creation date: (2/18/2004 5:45:57
     * PM)
     * 
     * @return java.lang.String
     * @param item
     *            com.ibm.mm.sdk.common.DKDDO
     */
    private String getItemType(DKDDO ddo)
    {
        try
        {
            short itemType = ((Short) ddo.getPropertyByName(DKDDO.DK_CM_PROPERTY_ITEM_TYPE)).shortValue();
            if (itemType == DKDDO.DK_CM_FOLDER)
                return ("FOLDER");
            else if (itemType == DKDDO.DK_CM_DOCUMENT)
                return ("DOCUMENT");
        } catch (Exception exc)
        {
        }

        return ("UNKNOWN");
    }

    /**
     * Retrieves the last exception that was caught. The exception isn't
     * necessary set or cleared at any given time. Creation date: (2/18/2004
     * 6:47:00 PM)
     * 
     * @return java.lang.String
     */
    public java.lang.String getLastException()
    {

        return (String) lastException.get(getThreadID());
    }

    public DKDDO getObjectFromLibrary(String objectId, boolean imageInformation,DKDatastoreICM datastore)
    {
        try
        {
            // parse the results of the query
            DKDDO item = null;

            DKResults results = getObjectsFromLibrary("*", "[@ITEMID=\"" + objectId + "\"]", 0, imageInformation,datastore);
            dkIterator iter = results.createIterator();
            if (iter.more())
                item = (DKDDO) iter.next();

            return (item);
        } catch (Exception exc)
        {
            handleException(exc);
        }
        return (null);
    }

    private DKResults getObjectsFromLibrary(String index, String searchText, int maxRecords, boolean imageInformation,DKDatastoreICM datastore)
    {
        String methodName = "getObjectsFromLibrary(String index, String searchText)";

        try
        {

            // execute the query
            //DKDatastoreICM datastore = connect(methodName);
            
            datastore.connection();

            DKNVPair options[] = new DKNVPair[3];
            DKRetrieveOptionsICM _dkRetrieveOptions = DKRetrieveOptionsICM.createInstance(datastore);
            _dkRetrieveOptions.baseAttributes(true);
            _dkRetrieveOptions.childListOneLevel(imageInformation);
            
            options[0] = new DKNVPair(DKConstant.DK_CM_PARM_MAX_RESULTS, Integer.toString(maxRecords));
            options[1] = new DKNVPair(DKConstant.DK_CM_PARM_RETRIEVE, 
            		_dkRetrieveOptions);
            		// new Integer(DKConstant.DK_CM_CONTENT_NO + DKConstant.DK_CM_CONTENT_ITEMTREE_NO_LINKS));
            		// new Integer(DKConstant.DK_CM_CONTENT_NO + DKConstant.DK_CM_CONTENT_ITEMTREE));
            options[2] = new DKNVPair(DKConstant.DK_CM_PARM_END, null);

            searchText = "/" + index + searchText;

            // look in the cache
            DKResults cursor = null;
           //logger.debug("datastore evaulate(" + searchText + ")");
            Date now = new Date();
            cursor = (DKResults) datastore.evaluate(searchText, DKConstantICM.DK_CM_XQPE_QL_TYPE, options);
           //logger.debug("search stats : " + searchText + " count: " + ((cursor == null) ? 0 : cursor.cardinality()) + "  ms: " + (new Date().getTime() - now.getTime()));
          //below line commented by Krishna
           // disconnect(methodName);
            return cursor;

        } catch (DKException exc)
        {
           //logger.debug(" : Query Error " + exc.toString());
        } catch (Exception exc)
        {
            handleException(exc);
        }

      //below line commented by Krishna
      //disconnect(methodName);
        return (null);
    }

    private String getPassword()
    {
        return password;
    }

    private String getServer()
    {
        return server;
    }

    public String[] getServerList()
    {
        String[] serverArray = { "IGFUAT", "IGFDEV" };
        try
        {
            cm8ServerArray = new DKDatastoreICM().listDataSourceNames();
        } catch (ClassNotFoundException exc)
        {
        } catch (DKException exc)
        {
            cm8ServerArray = serverArray;
            handleException(exc);
        } catch (Throwable exc)
        {
            handleException(exc);
        }
        /*
         * cm 7 - cm 8 migration try { cm7ServerArray = new
         * DKDatastoreDL().listDataSourceNames(); } catch
         * (ClassNotFoundException exc) { } catch (DKException exc) {
         * handleException(exc); } catch (Throwable exc) { handleException(exc); }
         */

        int serverCount = 0;
        /*
         * cm 7 - cm 8 migration if (cm7ServerArray != null) { serverCount +=
         * cm7ServerArray.length; }
         */
        if (cm8ServerArray != null)
        {
            serverCount += cm8ServerArray.length;
        }

        if (serverCount > 0)
        {
            serverArray = new String[serverCount];
            int n = 0;

            /*
             * cm 7 - cm 8 migration for (int i = 0; cm7ServerArray != null && i <
             * cm7ServerArray.length; i++) { serverArray[n++] =
             * cm7ServerArray[i]; }
             */

            for (int i = 0; cm8ServerArray != null && i < cm8ServerArray.length; i++)
            {
                serverArray[n++] = cm8ServerArray[i];
            }
        }

        return (serverArray);
    }

    private String getThreadID()
    {
        // search the has for this ip connection
        String currentThread = Thread.currentThread().toString();
        int leftOffset = currentThread.indexOf("(");
        int rightOffset = currentThread.indexOf(",");
        String ip = currentThread;

        try
        {
        	if (leftOffset < 0 || rightOffset < 0)
        	{
        		ip = currentThread;
        	} else {
        		ip = currentThread.substring(leftOffset, rightOffset);
        	}
        } catch (Exception exc)
        {

        }

        return ip;
    }

    private String getUserid()
    {
        return userid;
    }

    private dkWorkFlowService getWorkflowService() throws DKException, Exception
    {
        /*
         * cm 7 - cm 8 migration if (wfDL == null) { throw (new DKException
         * ("getWorkflowService(): error: workflow servicew not created")); }
         * 
         * 
         * return wfDL;
         */
        throw (new DKException("getWorkflowService(): error: workflow servicew not created"));

    }

    private dkXDO[] getXDOFromDatastore(DKDDO item, int flags)
    {
        try
        {
           //logger.debug("datastore item.retrieve()");
            item.retrieve();

            // parse the results of the query
            DKParts aCollection = null;
            dkIterator anIterator = null;
            short itemType;
            String itemTypeText;
            itemType = ((Short) item.getProperty(item.propertyId(DKConstant.DK_PROPERTY_ITEM_TYPE))).shortValue();

            // look at the parts
            dkDataObjectBase part = null;
            short lastPart;
            DKPid partPid = null;
            short partType;
            String partTypeText;
            ArrayList parts = new ArrayList();
            lastPart = item.dataCount();
            for (short i = 1; i <= lastPart; i++)
            {
                if (item.getData(i) instanceof DKParts)
                {
                    aCollection = (DKParts) item.getData(i);
                    anIterator = aCollection.createIterator();

                    while (anIterator.more() == true)
                    {
                        part = (dkDataObjectBase) anIterator.next();
                        if ((part != null) && (part instanceof dkXDO))
                        {
                            partPid = ((dkXDO) part).getPidObject();
                            partType = 0;
                            partTypeText = ((dkXDO) part).getMimeType();
                           //logger.debug("datastore ((dkXDO) part).retrieve()");
                            ((dkXDO) part).retrieve();

                            if ((((dkXDO) part).getContent() != null) && (((dkXDO) part).getContent().length > 0))
                            {
                                parts.add((dkXDO) part);
                            } else {
                               // logger.debug("Invalid Image Information: " + item.getPidObject().getPrimaryId() + "  ->  " + partPid);
                            }
                        }
                    }
                }
            }

            dkXDO[] xdoParts = new dkXDO[parts.size()];
            for (int i = 0; i < parts.size(); i++)
            {
                xdoParts[i] = (dkXDO) parts.get(i);
            }

            return xdoParts;

        } catch (Exception exc)
        {
            handleException(exc);
        }
        return (null);
    }

    private void handleException(Throwable exc) {
    	exc.printStackTrace();
        logger.fatal(exc,exc);
        setLastException(exc.toString());
    }

    public String indexDescriptionToName(String index,DKDatastoreICM datastore) 
    {
        String methodName = "indexDescriptionToName(String index)";
        try
        {
           // DKDatastoreICM datastore = connect(methodName);

        	datastore.connection();
            // check for all indexes
            if (index.equals("*"))
            {
            	//below line commented by Krishna
                //disconnect(methodName);
            	//datastore.disconnect();
                return index;
            }

            DKComponentTypeDefICM icDef;
            String description;
            String indexname = normalizeString(index);
            for (int i = 0; i < getEntityCache(datastore).length; i++)
            {
                icDef = getEntityCache(datastore)[i];
                description = normalizeString(icDef.getDescription());
                if (description.equals(indexname))
                {
                	//below line commented by Krishna
                    //disconnect(methodName);
                    return (icDef.getName());
                }
            }
          //logger.debug("Warning no matching index for description " + index);
        } catch (Exception e)
        {
            handleException(e);
        }
        //below line commented by Krishna
       //disconnect(methodName);
        return index;
    }

    public String indexNameToDescription(String index,DKDatastoreICM datastore) 
    {
        String methodName = "indexNameToDescription(String index)";
        try
        {
            //DKDatastoreICM datastore = connect(methodName);

            DKComponentTypeDefICM icDef;
            String name;
            String indexname = normalizeString(index);
            for (int i = 0; i < getEntityCache(datastore).length; i++)
            {
                icDef = getEntityCache(datastore)[i];
                name = normalizeString(icDef.getName());
                if (name.equals(indexname))
                {
                	//below line commented by Krishna
                    //disconnect(methodName);
                    return (icDef.getDescription());
                }
            }
            logger.debug("Warning no matching description for index " + index);
        } catch (Exception e)
        {
            handleException(e);
        }

        //below line commented by Krishna
       //disconnect(methodName);
        return index;
    }

    private void initializeConnections()
    {
        try
        {
            synchronized (connectionPool)
            {
                try
                {
                    for (int i = 0; i < CONNECTIONPOOLSIZE; i++)
                    {
                        connectionPool[i] = new ConnectionPoolItem();
                        connectionPool[i].connectionNumber = i;
                    }
                    
                    // create the first connection to login and retrieve index info
                	if (connectToCM(connectionPool[0]) == null)
                	{
                        logger.debug("Failed to establish initial connection to Content Manager");
                        throw new IllegalStateException ("Failed to establish initial connection to Content Manager");
                	}
                } catch (IllegalStateException e) {
                    throw e;
                } catch (Exception exc)
                {
                    handleException(exc);
                }
            }
        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception exc)
        {
            handleException(exc);
        }
        logger.debug("Connections initialized");
    }

    public String isDocumentInFolder(String documentItemType, String documentPID, String folderItemType, String folderPID,DKDatastoreICM datastore)
    {

        String methodName = "isDocumentInFolder (String documentItemType, String documentPID, String folderItemType, String folderPID)";

        try
        {
        	//DKDatastoreICM datastore = connect(methodName);

            documentItemType = indexDescriptionToName(documentItemType,datastore);
            folderItemType = indexDescriptionToName(folderItemType,datastore);

            String searchText = "/*[@ITEMID = \"" + folderPID + "\"]";

            StringBuffer results = new StringBuffer();
            results.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
            results.append("<QUERYRESULTS>");
            results.append("<!-- Results from a parametric search of Content Manager -->");

            DKNVPair options[] = new DKNVPair[3];
            options[0] = new DKNVPair(DKConstant.DK_CM_PARM_MAX_RESULTS, Integer.toString(MAXRECORDS));
            options[1] = new DKNVPair(DKConstant.DK_CM_PARM_RETRIEVE, new Integer(DKConstant.DK_CM_CONTENT_YES | DKConstant.DK_CM_CONTENT_ITEMTREE));
            options[2] = new DKNVPair(DKConstant.DK_CM_PARM_END, null);

            DKResults pCur = null;
            logger.debug("datastore evaulate(" + searchText + ")");
            Date now = new Date();
            pCur = (DKResults) datastore.evaluate(searchText, DKConstantICM.DK_CM_XQPE_QL_TYPE, options);
            logger.debug("search stats : " + searchText + " count: " + ((pCur == null) ? 0 : pCur.cardinality()) + "  ms: " + (new Date().getTime() - now.getTime()));

            // parse the results of the query
            DKDDO item = null;
            if (pCur != null)
            {
                dkIterator iter = pCur.createIterator();
                while (iter.more())
                {
                    item = (DKDDO) iter.next();
                    if (item != null)
                    {
                        results.append(formatItem(item, false,datastore));
                    }
                }
            }

            results.append("</QUERYRESULTS>");

            // return the xml document
            //below line commented by Krishna
            //disconnect(methodName);
            return results.toString();
        } catch (Exception exc)
        {
            handleException(exc);
           //below line commented by Krishna
           // disconnect(methodName);
            return "";
        }

    }

    /**
     * Insert the method's description here. Creation date: (3/13/00 9:28:41 AM)
     */
    private boolean logonDigitalLibrary(String userId, String password, String server, ConnectionPoolItem connectionPoolItem)
    {
        // connect to the digital library
        try
        {
            for (int i = 0; ((cm8ServerArray != null) && (i < cm8ServerArray.length)); i++)
            {
                logger.debug("checking server: " + cm8ServerArray[i].toUpperCase());
                if (server.toUpperCase().equals(cm8ServerArray[i].toUpperCase()))
                {
                    if (connectionPoolItem.dsDL == null)
                        connectionPoolItem.dsDL = (new DKDatastoreICM());
                    
                    try
                    {
                        logger.debug("connecting to cm");
                        Date now = new Date();
                        connectionPoolItem.dsDL.connect(server, userId, password, "");
                        logger.debug("cm connected taking ms: " + (new Date().getTime() - now.getTime()));
                        if (connectionPoolItem.dsDL.isConnected())
                        {
                            return true;
                        }
                    } catch (Exception exc)
                    {
                        handleException(exc);
                        connectionPoolItem.dsDL = null;
                    }
                }
            }

        } catch (DKException exc)
        {
            handleException(exc);
            return (false);
        } catch (java.rmi.RemoteException exc)
        {
            handleException(exc);
            return (false);
        } catch (Exception exc)
        {
            handleException(exc);
            return (false);
        }
        return (false);
    }

    /**
     * Moves an object id from one index class to another Creation date:
     * (2/26/2004 2:10:07 PM)
     * 
     * @return boolean -- true if the operation suceeded
     * @param objectId --
     *            java.lang.String -- The object id of the document to be moved
     * @param newIndexClass --
     *            java.lang.String -- The index class the document should be
     *            moved to
     */
    public boolean moveDocument(String objectId, String newIndexClass,DKDatastoreICM datastore)
    {
        String methodName = "moveDocument(String objectId, String newIndexClass)";
        try
        {
            //DKDatastoreICM datastore = connect(methodName);

            DKDDO item = getObjectFromLibrary(objectId, false,datastore);

            newIndexClass = indexDescriptionToName(newIndexClass,datastore);

            if (item != null)
            {
                if (datastore instanceof DKDatastoreICM)
                {
                    // check out the item
                    logger.debug("datastore checkOut(item)");
                    try {
                    	((DKDatastoreICM) datastore).checkOut(item);
                    } catch (Exception e) {
                        logger.debug("datastore checkOut(item) failed");
					}
                    // change the type
                    logger.debug("datastore moveObject(item, " + newIndexClass + ")");
                    ((DKDatastoreICM) datastore).moveObject(item, newIndexClass);

                    // check in the item
                    logger.debug("datastore checkIn(item)");
                    try {
                    	((DKDatastoreICM) datastore).checkIn(item);
                    } catch (Exception e) {
                        logger.debug("datastore checkIn(item) failed");
					}
                }

            }
        } catch (Exception exc)
        {
            handleException(exc);
            
            //below line commented by Krishna
            //disconnect(methodName);
            return false;
        }

        //below line commented by Krishna
        //disconnect(methodName);
        return true;
    }
    
    public boolean moveDocument(String objectId, String newItemType, String[] fields, String[] values,DKDatastoreICM datastore) 
    {
        String methodName = "moveDocument(String objectId, String newItemType, String[] fields, String[] values)";
        boolean isUpdated = false;
        try
        {
            //DKDatastoreICM datastore = connect(methodName);

            newItemType = indexDescriptionToName(newItemType,datastore);
            
            // retrieve the existing item
            DKDDO ddo = getObjectFromLibrary(objectId, false,datastore);
            short indexField = 0;

            if (ddo == null)
            {
                logger.debug("object id does not exist on cm server ");
                
                //below line commented by Krishna
                //disconnect(methodName);
                return false;
            }

            // check int the item
            try {
                datastore.checkIn(ddo);
            } catch (Exception e) {
                // don't care, probably not checked out
            }

            // check out the item
            try {
                datastore.checkOut(ddo);
            } catch (Exception e) {
                logger.debug ("Error checking item out for move");
            }

            // create a target ddo
            short itemType = ((Short) ddo.getPropertyByName(DKDDO.DK_CM_PROPERTY_ITEM_TYPE)).shortValue();
            DKDDO targetItem = datastore.createDDO (newItemType, itemType);

            // copy over the attributes to the new object
            attributeDescriptionToName(newItemType, fields,datastore);
            setAttributes(targetItem, newItemType, fields, values,datastore);
            
            // move the old ddo to the new ddo
            datastore.moveObject(ddo, targetItem, DKConstant.DK_CM_CHECKIN );

            isUpdated = true;
        } catch (Exception exc)
        {
            handleException(exc);
            //below line commented by Krishna
           //disconnect(methodName);
            return false;
        }

        // return the success
        //below line commented by Krishna
        //disconnect(methodName);
        return isUpdated;
	}    

    /**
     * Adds a document to a folder <BR>
     * Creation date: (2/12/2004 4:39:56 PM) <code><pre>
     * ContentManagerRMIInterface cm = (ContentManagerRMIInterface) Naming.lookup(&quot;//don29.pok.ibm.com:6028/ContentMangerRMIServer/&quot;);
     * String objectId = &quot;ASDFASDFASDFASDF&quot;;
     * String folderId = &quot;QWERQWERQWERQWER&quot;;
     * try
     * {
     *     results = cm.moveDocumentToFolder(objectId, folderId);
     *     if (results == false)
     *         logger.debug(&quot;error moving contract into folder&quot;);
     *     else
     *         logger.debug(&quot;Contract moved into folder&quot;);
     * } catch (RemoteException exc)
     * {
     *     logger.debug(&quot;error moving contract into folder : &quot;);
     *     return;
     * }
     * </pre></code><BR>
     * 
     * @return boolean -- True if the move was sucessful
     * @param objectId --
     *            java.lang.String -- Object Id of the item to move
     * @param folderId --
     *            java.lang.String[] -- Object Id of the folder to move into
     */
    public boolean moveDocumentToFolder(String objectId, String folderId,DKDatastoreICM dataStore) 
    {
        String methodName = "moveDocumentToFolder(String objectId, String folderId)";

        // check parameters
        if ((objectId == null) || (folderId == null))
        {
            return false;
        }
        try
        {
            //connect(methodName);

            DKDDO itemDDO = getObjectFromLibrary(objectId, false,dataStore);
            DKDDO folderDDO = getObjectFromLibrary(folderId, false,dataStore);

            if (folderDDO == null)
                logger.debug("folder not found in CM : " + folderId);
            else if (itemDDO == null)
                logger.debug("object not found in CM : " + objectId);
            else
            {
            	//below line commented by Krishna
               //disconnect(methodName);
                return addItemToFolder(itemDDO, folderDDO,dataStore);
            }

        } catch (Exception exc)
        {
            handleException(exc);
            //below line commented by Krishna
            //disconnect(methodName);
            return false;
        }

        //below line commented by Krishna
       //disconnect(methodName);
        return false;
    }

    private String normalizeString(String value)
    {
        if (value == null)
            return "";

        value = value.toUpperCase();
        value = value.replace('_', ' ');
        value = value.trim();
        return value;
    }

    /**
     * Retrieves the all of the contents of a document from Content Manager <BR>
     * Creation date: (2/12/2004 4:39:56 PM) <code><pre>
     * ContentManagerRMIInterface cm = (ContentManagerRMIInterface) Naming.lookup(&quot;//don29.pok.ibm.com:6028/ContentMangerRMIServer/&quot;);
     * String objectId = &quot;ASDFASDFASDFASDF&quot;;
     * try
     * {
     *     data = new String(cm.retrieveDocumentContents(objectId));
     *     if (data == null)
     *         logger.debug(&quot;error retrieving contract&quot;);
     *     else
     *         logger.debug(&quot;contract retrieved&quot;);
     * } catch (RemoteException exc)
     * {
     *     logger.debug(&quot;error retrieving contract : &quot;);
     *     return;
     * }
     * </pre></code><BR>
     * 
     * @return byte[][] -- An array of byte arrays of the data associated with
     *         this file. null if no document was retrieved.
     * @param objectId --
     *            java.lang.String -- Object Id of the document to be retrieved
     */
    public byte[][] retrieveDocumentContents(String objectId,DKDatastoreICM datastore) 
    {
        String methodName = "retrieveDocumentContents(String objectId)";
        try
        {
            //connect(methodName);

            dkXDO[] item = getImageFromDatastore(objectId,datastore);
            if ((item != null) && (item.length > 0))
            {
                byte[][] data = new byte[item.length][0];
                for (int i = 0; i < item.length; i++)
                {
                    logger.debug("datastore item[ " + i + "].getContent()");
                    data[i] = item[i].getContent();
                }

                //below line commented by Krishna
               //disconnect(methodName);
                return data;
            }
        } catch (Exception exc)
        {
            handleException(exc);
        }

        //below line commented by Krishna
        //disconnect(methodName);
        return null;
    }

    /**
     * Retrieves the all of the contents of a document from Content Manager <BR>
     * Creation date: (2/12/2004 4:39:56 PM) <code><pre>
     * ContentManagerRMIInterface cm = (ContentManagerRMIInterface) Naming.lookup(&quot;//don29.pok.ibm.com:6028/ContentMangerRMIServer/&quot;);
     * String objectId = &quot;ASDFASDFASDFASDF&quot;;
     * try
     * {
     *     data = new String(cm.retrieveDocumentContents(objectId, 0));
     *     if (data == null)
     *         logger.debug(&quot;error retrieving contract&quot;);
     *     else
     *         logger.debug(&quot;contract retrieved&quot;);
     * } catch (RemoteException exc)
     * {
     *     logger.debug(&quot;error retrieving contract : &quot;);
     *     return;
     * }
     * </pre></code><BR>
     * 
     * @return byte[][] -- An array of byte arrays of the data associated with
     *         this file. null if no document was retrieved.
     * @param objectId --
     *            java.lang.String -- Object Id of the document to be retrieved
     */
    public byte[] retrieveDocumentContents(String objectId, int partNum,DKDatastoreICM datastore) 
    {
        String methodName = "retrieveDocumentContents(String objectId, int partNum)";
        try
        {
           // connect(methodName);

            dkXDO[] item = getImageFromDatastore(objectId,datastore);
            if ((item != null) && (item.length > 0) && (partNum < item.length))
            {
            	//below line commented by Krishna
                //disconnect(methodName);
                logger.debug("datastore item[" + partNum + "].getContent()");
                return item[partNum].getContent();
            }
        } catch (Exception exc)
        {
            handleException(exc);
        }

        //below line commented by Krishna
        //disconnect(methodName);
        return null;
    }

    /**
     * Retrieves the all of the content mimetypes of a document from Content
     * Manager <BR>
     * Creation date: (2/12/2004 4:39:56 PM) <code><pre>
     * ContentManagerRMIInterface cm = (ContentManagerRMIInterface) Naming.lookup(&quot;//don29.pok.ibm.com:6028/ContentMangerRMIServer/&quot;);
     * String objectId = &quot;ASDFASDFASDFASDF&quot;;
     * try
     * {
     *     String mimetype = cm.retrieveDocumentMimeType(objectId, 0);
     *     if (mimetype == null)
     *         logger.debug(&quot;error retrieving mimetypes&quot;);
     *     else
     *         logger.debug(&quot;mimetypes retrieved&quot;);
     * } catch (RemoteException exc)
     * {
     *     logger.debug(&quot;error retrieving mimetypes : &quot;);
     *     return;
     * }
     * </pre></code><BR>
     * 
     * @return byte[][] -- An array of byte arrays of the data associated with
     *         this file. null if no document was retrieved.
     * @param objectId --
     *            java.lang.String -- Object Id of the document to be retrieved
     */
    public String retrieveDocumentMimeType(String objectId, int partnum,DKDatastoreICM datastore) 
    {
        String methodName = "retrieveDocumentMimeType(String objectId, int partnum)";
        try
        {
           // connect(methodName);

            dkXDO[] item = getImageAttributesFromDatastore(objectId,datastore);
            if ((item != null) && (item.length > partnum))
            {
                String data = item[partnum].getMimeType();

                //below line commented by Krishna
                //disconnect(methodName);
                return data;
            }
        } catch (Exception exc)
        {
            handleException(exc);
        }

        //below line commented by Krishna
       //disconnect(methodName);
        return null;
    }

    /**
     * Retrieves the all of the content mimetypes of a document from Content
     * Manager <BR>
     * Creation date: (2/12/2004 4:39:56 PM) <code><pre>
     * ContentManagerRMIInterface cm = (ContentManagerRMIInterface) Naming.lookup(&quot;//don29.pok.ibm.com:6028/ContentMangerRMIServer/&quot;);
     * String objectId = &quot;ASDFASDFASDFASDF&quot;;
     * try
     * {
     *     String[] mimetypes = cm.retrieveDocumentMimeTypes(objectId);
     *     if (mimetypes == null)
     *         logger.debug(&quot;error retrieving mimetypes&quot;);
     *     else
     *         logger.debug(&quot;mimetypes retrieved&quot;);
     * } catch (RemoteException exc)
     * {
     *     logger.debug(&quot;error retrieving mimetypes : &quot;);
     *     return;
     * }
     * </pre></code><BR>
     * 
     * @return byte[][] -- An array of byte arrays of the data associated with
     *         this file. null if no document was retrieved.
     * @param objectId --
     *            java.lang.String -- Object Id of the document to be retrieved
     */
    public String[] retrieveDocumentMimeTypes(String objectId,DKDatastoreICM datastore) 
    {
        String methodName = "retrieveDocumentMimeTypes(String objectId)";
        try
        {
            //connect(methodName);

            dkXDO[] item = getImageAttributesFromDatastore(objectId,datastore);
            if ((item != null) && (item.length > 0))
            {
                String[] data = new String[item.length];
                for (int i = 0; i < item.length; i++)
                {
                    data[i] = item[i].getMimeType();
                }

                //below line commented by Krishna
                //disconnect(methodName);
                return data;
            }
        } catch (Exception exc)
        {
            handleException(exc);
        }

        //below line commented by Krishna
        //disconnect(methodName);
        return null;
    }

    /**
     * Retrieves the contents of a document from Content Manager <BR>
     * Creation date: (2/12/2004 4:39:56 PM) <code><pre>
     * ContentManagerRMIInterface cm = (ContentManagerRMIInterface) Naming.lookup(&quot;//don29.pok.ibm.com:6028/ContentMangerRMIServer/&quot;);
     * String objectId = &quot;ASDFASDFASDFASDF&quot;;
     * try
     * {
     *     data = new String(cm.retrieveDocumentProperties(objectId));
     *     if (data == null)
     *         logger.debug(&quot;error retrieving contract&quot;);
     *     else
     *         logger.debug(&quot;contract retrieved&quot;);
     * } catch (RemoteException exc)
     * {
     *     logger.debug(&quot;error retrieving contract : &quot;);
     *     return;
     * }
     * </pre></code><BR>
     * 
     * @return String -- A xml stream of the properties associated with this
     *         file. null if no document was retrieved.
     * @param objectId --
     *            java.lang.String -- Object Id of the document to be retrieved
     */
    public String retrieveDocumentProperties(String objectId,DKDatastoreICM datastore) 
    {
        String methodName = "retrieveDocumentProperties(String objectId)";
        try
        {
            //connect(methodName);
        	datastore.connection();

            DKDDO item = getObjectFromLibrary(objectId, true,datastore);
            StringBuffer results = new StringBuffer();
            results.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
            if (item != null)
            {
                results.append(formatItem(item, true,datastore));
            }

            //below line commented by Krishna
            //disconnect(methodName);
            return results.toString();
        } catch (Exception exc)
        {
            handleException(exc);
        }

        //below line commented by Krishna
       //disconnect(methodName);
        return null;
    }

    private void setAttributes(DKDDO aDDO, String index, String[] fields, String[] values,DKDatastoreICM datastore)
    {
        try
        {
            // Set Attributes
            short dataid = 0;
            dkAttrDef[] attributeTypes = getIndexFieldAttributes(index);
            
            
            for (int i = 0; i < fields.length && i < values.length && i < attributeTypes.length; i++)
            {
            	logger.debug("attributetypes:"+ attributeTypes[i]);
                dataid = aDDO.dataId(DKConstant.DK_CM_NAMESPACE_ATTR, fields[i]);
                logger.debug("dataid:"+dataid);
                if ((dataid > 0) && (dataid - 1 < attributeTypes.length))
                {
                	logger.debug("inside first if:");
                	// check if this is a string type
                    if ((attributeTypes[dataid - 1].getType() == DKConstant.DK_VSTRING) || (attributeTypes[dataid - 1].getType() == DKConstant.DK_CM_FSTRING))
                    {
                    	logger.debug("inside second if:");
                    	// see if it's null or zero length
                        if ((values[i] == null) || (values[i].trim().length() == 0))
                        {
                        	logger.debug("inside third if:");
                        	// see if it's nullable preferably
                        	if (attributeTypes[dataid - 1].isNullable())
                        	{
                        		logger.debug("inside fourth if:");
                        		aDDO.setNull(dataid);
                        	} else {
                        		// try and make an dashed string as big as the min
                        		logger.debug("inside fourth if else:");
                        		StringBuffer empty = new StringBuffer();
                        		for (int emptyLen=0; emptyLen<attributeTypes[dataid - 1].getMin(); emptyLen++)
                        		{
                        			empty.append("-");
                        		}
                       			aDDO.setData(dataid, empty.toString());
                        	}
                        } else {
                    		// try and make an dashed string as big as the min
                        	logger.debug("inside third if else:");
                    		StringBuffer filledUp = new StringBuffer(values[i]);
                    		for (int emptyLen = filledUp.length(); emptyLen<attributeTypes[dataid - 1].getMin(); emptyLen++)
                    		{
                    			filledUp.append("-");
                    		}

                    		// value should be good so just assign it
                            aDDO.setData(dataid, filledUp.toString());
                            logger.debug("attributeTypes[i]"+attributeTypes[i]);
                            logger.debug("fields[i]"+fields[i]);
                            logger.debug("values[i]"+values[i]);
                        }
                    } else if (attributeTypes[dataid - 1].getType() == DKConstant.DK_CM_TIMESTAMP)
                    {
                        boolean parsed = false;
                        try
                        {
                            aDDO.setData(dataid, java.sql.Timestamp.valueOf(values[i]));
                            parsed = true;
                        } catch (Exception exc)
                        {
                        }
                        try
                        {
                            if (parsed == false)
                            {
                                aDDO.setData(dataid, DKTimestamp.valueOf(values[i]));
                                parsed = true;
                            }
                        } catch (Exception exc)
                        {
                        }
                        if (!parsed)
                        {
                            logger.debug("Invalid timestamp format specified " + values[i]);
                        }
                    } else if (attributeTypes[dataid - 1].getType() == DKConstant.DK_CM_DATE)
                    {
                        boolean parsed = false;
                        try
                        {
                            aDDO.setData(dataid, DKDate.valueOf(values[i]));
                            parsed = true;
                        } catch (Exception exc)
                        {
                        }
                        try
                        {
                            if (parsed == false)
                            {
                                aDDO.setData(dataid, java.sql.Date.valueOf(values[i]));
                                parsed = true;
                            }
                        } catch (Exception exc)
                        {
                        }
                        if (!parsed)
                        {
                            logger.debug("Invalid date format specified " + values[i]);
                        }
                    } else
                    {
                        logger.debug("Unsupported attribute type " + attributeTypes[dataid - 1].getType());
                    }
                } else
                {
                    if (dataid == 0)
                    {
                        logger.debug("Unknown attribute name " + fields[i]);
                    }
                    if ((dataid - 1 >= attributeTypes.length))
                    {
                        logger.debug("Unknown attribute type " + fields[i] + "  dataid = " + dataid);
                    }
                }
            }
        } catch (Throwable exc)
        {
            handleException(exc);
            logger.debug("exception in set attributes is :"+exc);
        }

    }

    /**
     * Insert the method's description here. Creation date: (2/18/2004 6:47:00
     * PM)
     * 
     * @param newLastException
     *            java.lang.String
     */
    private void setLastException(java.lang.String newLastException)
    {
        lastException.put(getThreadID(), newLastException);
    }

    protected void setPassword(String aPassword)
    {
        password = aPassword;
    }

    protected void setServer(String aServer)
    {
        server = aServer;
    }

    protected void setUserid(String aUserid)
    {
        userid = aUserid;
    }

    /**
     * Insert the method's description here. Creation date: (2/26/2004 4:13:02
     * PM)
     * 
     * @return boolean -- true if sucessfull
     * @param objectId --
     *            java.lang.String -- Object Id of the document / folder to be
     *            routed to a workflow
     * @param workflow --
     *            java.lang.String -- Name of the workflow to start it on
     */
    public boolean startWorkflow(String objectId, String workflow,DKDatastoreICM datastore)
    {
        String methodName = "startWorkflow(String objectId, String workflow)";
        try
        {

           //DKDatastoreICM datastore = connect(methodName);
            String workPackagePidStr = null;

            // convert the workflow descritive name to the real workflow name
            workflow = workflowDescriptionToName(workflow);

            //  Obtain the core routing service object.
            DKDocRoutingServiceICM routingService = connectWorkflow(methodName);

            // find the object
            DKDDO aDDO = getObjectFromLibrary(objectId, false,datastore);

            //  start the process
            if ((aDDO != null) && (routingService != null))
            {
                logger.debug("Starting " + aDDO.getPidObject().pidString() + " on " + workflow);
                workPackagePidStr = routingService.startProcess(workflow, aDDO.getPidObject().pidString(), 1, getUserid());
            } else
            {
                logger.debug("Unable to start " + aDDO.getPidObject().pidString() + " on " + workflow);
            }

            //below line commented by Krishna
            
            //disconnect(methodName);

            if ((workPackagePidStr == null) || (aDDO == null))
            {
                return false;
            }

            return true;

        } catch (Exception exc)
        {
            handleException(exc);
        }
        
        
       //below line commented by Krishna
       //disconnect(methodName);
        return false;
    }

    /**
     * Continue the regex workflows for an object id
     * 
     * @return boolean -- true if sucessfull
     * @param objectId --
     *            java.lang.String -- Object Id of the document / folder to be
     *            continued in a workflow
     * @param workflow --
     *            java.lang.String -- Name of the workflow to continue on
     */
    public boolean completeWorkflow(String objectId, String workflow,DKDatastoreICM datastore)
    {
        String methodName = "completeWorkflow(String objectId, String workflow)";
        try
        {

            //DKDatastoreICM datastore = connect(methodName);
            dkCollection workPackages = null;
            String workPackageName = null;
            boolean wasCompleted = false;

            // convert the workflow descritive name to the real workflow name
            workflow = normalizeString(workflow);

            //  Obtain the core routing service object.
            DKDocRoutingServiceICM routingService = new DKDocRoutingServiceICM(datastore);

            // find the object
            DKDDO aDDO = getObjectFromLibrary(objectId, false,datastore);

            //  continue the process
            if ((aDDO != null) && (routingService != null))
            {
                workPackages = routingService.listWorkPackagesWithItem(aDDO.getPidObject().pidString());
                
                if (workPackages != null)
                {
                	dkIterator iterator = workPackages.createIterator();
                	while (iterator != null && iterator.more())
                	{
                		DKWorkPackageICM workPackage = (DKWorkPackageICM)iterator.next();
                        logger.debug("Complete " + aDDO.getPidObject().pidString() + " on " + workPackage.getProcessName());
                		workPackageName = normalizeString(workPackage.getProcessName());
                		if (workPackageName.matches(workflow))
                		{
                			routingService.terminateProcess(workPackage.getPidString());
                			wasCompleted = true;
                		}
                	}
                } else
                {
                    logger.debug("No workpackages for " + aDDO.getPidObject().pidString());
                }
            } else
            {
                logger.debug("Unable to start " + aDDO.getPidObject().pidString() + " on " + workflow);
            }

           //below line commented by Krishna
            //disconnect(methodName);

            if (aDDO == null)
            {
                return false;
            }

            return wasCompleted;

        } catch (Exception exc)
        {
            handleException(exc);
        }

        //below line commented by Krishna
        //disconnect(methodName);
        return false;
    }

    /**
     * Insert the method's description here. Creation date: (2/26/2004 4:13:02
     * PM)
     * 
     * @return java.lang.String -- the xml data for the results
     * 
     * @param workflow --
     *            java.lang.String -- Name of the workflow to list
     * @param owner --
     *            java.lang.String -- Owner of the work packages to list
     */
    public String listWorkflow(String workflow,DKDatastoreICM datastore)
    {
        String methodName = "listWorkflow(String workflow)";

        StringBuffer results = new StringBuffer();

        results.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
        results.append("<QUERYRESULTS>");
        results.append("<!-- Results from a parametric search of Content Manager -->");

        try
        {

            // convert the workflow descritive name to the real workflow name
            workflow = workflowDescriptionToName(workflow);

           //DKDatastoreICM datastore = connect(methodName);
            DKDDO item = null;
            DKPidICM pid = null;

            //  Obtain the core routing service object.
            DKDocRoutingServiceICM routingService = new DKDocRoutingServiceICM(datastore);

            if (routingService != null)
            {
                // found the worklist, list the documents
                dkCollection packages = routingService.listWorkPackages(workflow, getUserid());
                if (packages != null)
                {
                    dkIterator iter = packages.createIterator();
                    while (iter != null && iter.more())
                    {
                        // Move pointer to next element and obtain that next
                        // element.
                        DKWorkPackageICM workPackage = (DKWorkPackageICM) iter.next();
                        pid = new DKPidICM(workPackage.getItemPidString());
                        item = getObjectFromLibrary(pid.getItemId(), false,datastore);
                        if (item != null)
                        {
                            results.append(formatItem(item, false,datastore));
                        }

                    }

                }
            }

        } catch (Exception exc)
        {
            handleException(exc);
        }
        results.append("</QUERYRESULTS>");

        //below line commented by Krishna
        //disconnect(methodName);
        return results.toString();
    }

    /**
     * Updates fields of an existing document in Content Manager <BR>
     * Creation date: (2/12/2004 4:39:56 PM) <code><pre>
     * ContentManagerRMIInterface cm = (ContentManagerRMIInterface) Naming.lookup(&quot;//don29.pok.ibm.com:6028/ContentMangerRMIServer/&quot;);
     * String[] fields = { &quot;Contract Number&quot;, &quot;Customer Number&quot;, &quot;Customer Name&quot; };
     * String[] newValues = { &quot;SBC040212&quot;, &quot;SB200&quot;, &quot;SCOTT B&quot; };
     * try
     * {
     *     results = cm.updateDocument(objectId, fields, newValues);
     *     if (results == false)
     *         logger.debug(&quot;error updating contract fields&quot;);
     *     else
     *         logger.debug(&quot;Index fields updated&quot;);
     * } catch (RemoteException exc)
     * {
     *     logger.debug(&quot;error updating contract fields : &quot;);
     *     return;
     * }
     * </pre></code><BR>
     * 
     * @return boolean -- True if sucessful
     * @param objectId --
     *            java.lang.String -- Object Id of the document to be modified
     * @param fields --
     *            java.lang.String[] -- Fields of the index class to be modified
     * @param values --
     *            java.lang.String[] -- Values of the index class fields to be
     *            modifed
     */
    public boolean updateDocument(String objectId, String[] fields, String[] values,DKDatastoreICM datastore) 
    {
        String methodName = "updateDocument(String objectId, String[] fields, String[] values)";
        boolean isUpdated = false;
        try
        {
           // DKDatastoreICM datastore = connect(methodName);

            DKDDO ddo = getObjectFromLibrary(objectId, true,datastore);
            short indexField = 0;

            if (ddo == null)
            {
                logger.debug("object id does not exist on cm server ");
              //below line commented by Krishna
               // datastore.disconnect();
              //disconnect(methodName);
                return false;
            }

            String index = ddo.getObjectType();
            attributeDescriptionToName(index, fields,datastore);
            setAttributes(ddo, index, fields, values,datastore);

            // persist the ddo
            if (datastore instanceof DKDatastoreICM)
            {
                boolean isLocked = false;
                try
                {
                    ((DKDatastoreICM) datastore).checkIn(ddo);
                } catch (Exception exc)
                {
                    logger.debug("Error checking in item id " + objectId + " - " + exc);
                    //handleException (exc);
                }
                try
                {
                    logger.debug("datastore checkOut(ddo)");
                    ((DKDatastoreICM) datastore).checkOut(ddo);
                    isLocked = true;
                } catch (Exception exc)
                {
                    logger.debug("Error checking out item id " + objectId + " - " + exc);
                    //handleException(exc);
                }
                try
                {
                    logger.debug("datastore ddo.update()");
                    ddo.update();
                    isUpdated = true;
                } catch (Exception exc)
                {
                    logger.debug("Error updating attributes");
                    if (exc instanceof DKSizeOutOfBoundsException)
                    {
                        logger.debug(exc.toString());
                    } else {
                        handleException(exc);
                    }
                }
                try
                {
                    logger.debug("datastore checkIn(ddo)");
                    ((DKDatastoreICM) datastore).checkIn(ddo);
                } catch (Exception exc)
                {
                    logger.debug("Error checking in item id " + objectId);
                    //handleException(exc);
                }
            } else
            {
                logger.debug("datastore ddo.update()");
                ddo.update();
            }

        } catch (Exception exc)
        {
            handleException(exc);
           //below line commented by Krishna
           //disconnect(methodName);
            return false;
        }

        // return the success
        //below line commented by Krishna
       //disconnect(methodName);
        return isUpdated;
    }

    /**
     * Add a addNotelog to an existing document in Content Manager <BR>
     * Creation date: (2/12/2004 4:39:56 PM) <code><pre>
     * </pre></code><BR>
     * 
     * @return boolean -- True if sucessful
     * @param objectId --
     *            java.lang.String -- Object Id of the document to be modified
     * @param notes --
     *            java.lang.String -- Text to be added as a notelog
     */
    public boolean addNotelog(String objectId, String notes,DKDatastoreICM datastore) 
    {
        String methodName = "addNotelog(String objectId, String note)";
        boolean isAdded = false;
        try
        {
           //DKDatastoreICM datastore = connect(methodName);

            DKDDO ddo = getObjectFromLibrary(objectId, true,datastore);
            getImageFromDatastore(ddo);

            short indexField = 0;

            if (ddo == null)
            {
                logger.debug("object id does not exist on cm server ");
                //below line commented by Krishna
               //disconnect(methodName);
                return false;
            }

            DKParts dkParts = (DKParts) ddo.getData(ddo.dataId(DKConstant.DK_CM_NAMESPACE_ATTR, DKConstant.DK_CM_DKPARTS));
            DKTextICM lob = null;
            dkDataObjectBase part = null;

            // look for a notelog to append to
            dkIterator anIterator = dkParts.createIterator();
            while (anIterator.more() == true)
            {
                part = (dkDataObjectBase) anIterator.next();
                if ((part != null) && (part instanceof DKTextICM))
                {
                    lob = (DKTextICM) part;
                }
            }

            if (lob == null)
            {
                // create a new one
                lob = (DKTextICM) datastore.createDDO("ICMNOTELOG", DKConstantICM.DK_ICM_SEMANTIC_TYPE_NOTE);
                lob.setCCSID(1252);
                lob.setMimeType("text/plain");
                lob.setContent(notes.getBytes());

                dkParts.addElement(lob);
            } else
            {
                // append to the existing one
                if (lob.getContent() != null)
                {
                    notes = "\r\n" + new String(lob.getContent()) + "\r\n**************\r\n" + new Date() + "\r\n**************\r\n" + notes;
                }
                lob.setContent(notes.getBytes());
            }

            // persist the ddo
            if (datastore instanceof DKDatastoreICM)
            {
                boolean isLocked = false;
                try
                {
                    ((DKDatastoreICM) datastore).checkIn(ddo);
                } catch (Exception exc)
                {
                    //logger.debug
                    // ("Error checking in item id " + objectId);
                    //handleException (exc);
                }
                try
                {
                    logger.debug("datastore checkOut(ddo)");
                    ((DKDatastoreICM) datastore).checkOut(ddo);
                    isLocked = true;
                } catch (Exception exc)
                {
                    logger.debug("Error checking out item id " + objectId);
                    handleException(exc);
                }
                try
                {
                    logger.debug("datastore ddo.update()");
                    ddo.update();
                    isAdded = true;
                } catch (Exception exc)
                {
                    logger.debug("Error updating notelog");
                    handleException(exc);
                }
                try
                {
                    logger.debug("datastore checkIn(ddo)");
                    ((DKDatastoreICM) datastore).checkIn(ddo);
                } catch (Exception exc)
                {
                    logger.debug("Error checking in item id " + objectId);
                    handleException(exc);
                }
            } else
            {
                logger.debug("datastore ddo.update()");
                ddo.update();
            }

        } catch (Exception exc)
        {
            handleException(exc);
           //below line commented by Krishna
           //disconnect(methodName);
            return false;
        }

        // return the success
        //below line commented by Krishna
       //disconnect(methodName);
        return isAdded;
    }
    
    public String retrievePrivilegeSetName(String userId,DKDatastoreICM datastore)  {
        String methodName = "retrievePrivilegeSetName(String userId)";
        String privilegeSetName = null;
        HashMap<String, String> usersPrivilegeMap = null;
        try
        {
        	//DKDatastoreICM datastore = connect(methodName); 
        	usersPrivilegeMap = getUsersPrivilegeSetCache(datastore);
        	privilegeSetName = usersPrivilegeMap.get(userId);
        } catch (Exception exc)
        {
            handleException(exc);
        }

       //below line commented by Krishna
       //disconnect(methodName);  
        return privilegeSetName;
    }

    public boolean isCascadeFailures() {
		return cascadeFailures;
	}

	public void setCascadeFailures(boolean cascadeFailures) {
		this.cascadeFailures = cascadeFailures;
	}
	
	public void clear() {
		entityCache = null;
		classList = null;
		attributeCache = null;
		attributeList = null;
	}

}

