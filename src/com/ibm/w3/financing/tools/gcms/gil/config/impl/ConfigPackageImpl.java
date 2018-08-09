/**
 * <copyright>
 * </copyright>
 *
 * $Id: ConfigPackageImpl.java,v 1.3 2008/02/13 21:50:22 sbaber Exp $
 */
package com.ibm.w3.financing.tools.gcms.gil.config.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;
import org.eclipse.emf.ecore.xml.type.impl.XMLTypePackageImpl;

import com.ibm.w3.financing.tools.gcms.gil.config.BackendSelection;
import com.ibm.w3.financing.tools.gcms.gil.config.ConfigFactory;
import com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage;
import com.ibm.w3.financing.tools.gcms.gil.config.Configuration;
import com.ibm.w3.financing.tools.gcms.gil.config.ContentManagerServer;
import com.ibm.w3.financing.tools.gcms.gil.config.DB2Server;
import com.ibm.w3.financing.tools.gcms.gil.config.DatabaseConnection;
import com.ibm.w3.financing.tools.gcms.gil.config.Definition;
import com.ibm.w3.financing.tools.gcms.gil.config.DocumentRoot;
import com.ibm.w3.financing.tools.gcms.gil.config.GILConfigType;
import com.ibm.w3.financing.tools.gcms.gil.config.Geography;
import com.ibm.w3.financing.tools.gcms.gil.config.NewComplexType;
import com.ibm.w3.financing.tools.gcms.gil.config.RMIServer;
import com.ibm.w3.financing.tools.gcms.gil.config.Service;
import com.ibm.w3.financing.tools.gcms.gil.config.ServiceSuite;
import com.ibm.w3.financing.tools.gcms.gil.config.Webserver;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ConfigPackageImpl extends EPackageImpl implements ConfigPackage
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass backendSelectionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass configurationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass contentManagerServerEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass databaseConnectionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass dB2ServerEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass definitionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass documentRootEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass geographyEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass gilConfigTypeEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass newComplexTypeEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass rmiServerEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass serviceEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass serviceSuiteEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass webserverEClass = null;

  /**
   * Creates an instance of the model <b>Package</b>, registered with
   * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
   * package URI value.
   * <p>Note: the correct way to create the package is via the static
   * factory method {@link #init init()}, which also performs
   * initialization of the package, or returns the registered package,
   * if one already exists.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.emf.ecore.EPackage.Registry
   * @see com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage#eNS_URI
   * @see #init()
   * @generated
   */
  private ConfigPackageImpl()
  {
    super(eNS_URI, ConfigFactory.eINSTANCE);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static boolean isInited = false;

  /**
   * Creates, registers, and initializes the <b>Package</b> for this
   * model, and for any others upon which it depends.  Simple
   * dependencies are satisfied by calling this method on all
   * dependent packages before doing anything else.  This method drives
   * initialization for interdependent packages directly, in parallel
   * with this package, itself.
   * <p>Of this package and its interdependencies, all packages which
   * have not yet been registered by their URI values are first created
   * and registered.  The packages are then initialized in two steps:
   * meta-model objects for all of the packages are created before any
   * are initialized, since one package's meta-model objects may refer to
   * those of another.
   * <p>Invocation of this method will not affect any packages that have
   * already been initialized.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #eNS_URI
   * @see #createPackageContents()
   * @see #initializePackageContents()
   * @generated
   */
  public static ConfigPackage init()
  {
    if (isInited) return (ConfigPackage)EPackage.Registry.INSTANCE.getEPackage(ConfigPackage.eNS_URI);

    // Obtain or create and register package
    ConfigPackageImpl theConfigPackage = (ConfigPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(eNS_URI) instanceof ConfigPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(eNS_URI) : new ConfigPackageImpl());

    isInited = true;

    // Initialize simple dependencies
    XMLTypePackageImpl.init();

    // Create package meta-data objects
    theConfigPackage.createPackageContents();

    // Initialize created meta-data
    theConfigPackage.initializePackageContents();

    // Mark meta-data to indicate it can't be changed
    theConfigPackage.freeze();

    return theConfigPackage;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getBackendSelection()
  {
    return backendSelectionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getBackendSelection_Geography()
  {
    return (EAttribute)backendSelectionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getBackendSelection_GlobalAPTS()
  {
    return (EAttribute)backendSelectionEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getBackendSelection_GlobalGCMS()
  {
    return (EAttribute)backendSelectionEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getBackendSelection_ProductReference()
  {
    return (EAttribute)backendSelectionEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getBackendSelection_RDC()
  {
    return (EAttribute)backendSelectionEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getBackendSelection_RMI()
  {
    return (EAttribute)backendSelectionEClass.getEStructuralFeatures().get(5);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getBackendSelection_GCMS()
  {
    return (EAttribute)backendSelectionEClass.getEStructuralFeatures().get(6);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getConfiguration()
  {
    return configurationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getConfiguration_ServerId()
  {
    return (EAttribute)configurationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getConfiguration_Name()
  {
    return (EAttribute)configurationEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getConfiguration_Description()
  {
    return (EAttribute)configurationEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getConfiguration_Group()
  {
    return (EAttribute)configurationEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getConfiguration_LocalizedBackendSelections()
  {
    return (EReference)configurationEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getContentManagerServer()
  {
    return contentManagerServerEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getContentManagerServer_Name()
  {
    return (EAttribute)contentManagerServerEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getContentManagerServer_Description()
  {
    return (EAttribute)contentManagerServerEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getDatabaseConnection()
  {
    return databaseConnectionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDatabaseConnection_ConnectionName()
  {
    return (EAttribute)databaseConnectionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDatabaseConnection_ServerName()
  {
    return (EAttribute)databaseConnectionEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDatabaseConnection_HighLevelQualifier()
  {
    return (EAttribute)databaseConnectionEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDatabaseConnection_UserId()
  {
    return (EAttribute)databaseConnectionEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getDB2Server()
  {
    return dB2ServerEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDB2Server_Name()
  {
    return (EAttribute)dB2ServerEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDB2Server_InstanceNode()
  {
    return (EAttribute)dB2ServerEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDB2Server_TargetDatabase()
  {
    return (EAttribute)dB2ServerEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDB2Server_Hostname()
  {
    return (EAttribute)dB2ServerEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDB2Server_Port()
  {
    return (EAttribute)dB2ServerEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDB2Server_OSType()
  {
    return (EAttribute)dB2ServerEClass.getEStructuralFeatures().get(5);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getDefinition()
  {
    return definitionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getDefinition_ContentManagerServerDefinition()
  {
    return (EReference)definitionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getDefinition_DB2ServerDefinition()
  {
    return (EReference)definitionEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getDefinition_GCMSWebserverDefinition()
  {
    return (EReference)definitionEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getDefinition_RMIServerDefinition()
  {
    return (EReference)definitionEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDefinition_RDCServerDefinition()
  {
    return (EAttribute)definitionEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getDocumentRoot()
  {
    return documentRootEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDocumentRoot_Mixed()
  {
    return (EAttribute)documentRootEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getDocumentRoot_XMLNSPrefixMap()
  {
    return (EReference)documentRootEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getDocumentRoot_XSISchemaLocation()
  {
    return (EReference)documentRootEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getDocumentRoot_GilConfig()
  {
    return (EReference)documentRootEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getGeography()
  {
    return geographyEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getGeography_Geography()
  {
    return (EAttribute)geographyEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getGeography_Description()
  {
    return (EAttribute)geographyEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getGeography_Group()
  {
    return (EAttribute)geographyEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getGeography_SubGeography()
  {
    return (EReference)geographyEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getGILConfigType()
  {
    return gilConfigTypeEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getGILConfigType_Configuration()
  {
    return (EReference)gilConfigTypeEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getGILConfigType_ContentManagerServerDefinition()
  {
    return (EReference)gilConfigTypeEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getGILConfigType_DB2ServerDefinitions()
  {
    return (EReference)gilConfigTypeEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getGILConfigType_ServiceSuites()
  {
    return (EReference)gilConfigTypeEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getGILConfigType_GCMSWebserviceDefinitions()
  {
    return (EReference)gilConfigTypeEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getGILConfigType_RDCServerDefinitions()
  {
    return (EReference)gilConfigTypeEClass.getEStructuralFeatures().get(5);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getGILConfigType_DatabaseConnections()
  {
    return (EReference)gilConfigTypeEClass.getEStructuralFeatures().get(6);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getGILConfigType_Geographies()
  {
    return (EReference)gilConfigTypeEClass.getEStructuralFeatures().get(7);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getNewComplexType()
  {
    return newComplexTypeEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getRMIServer()
  {
    return rmiServerEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getRMIServer_Name()
  {
    return (EAttribute)rmiServerEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getRMIServer_URL()
  {
    return (EAttribute)rmiServerEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getRMIServer_Service()
  {
    return (EReference)rmiServerEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getService()
  {
    return serviceEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getService_Name()
  {
    return (EAttribute)serviceEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getService_WebService()
  {
    return (EAttribute)serviceEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getService_Namespace()
  {
    return (EAttribute)serviceEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getServiceSuite()
  {
    return serviceSuiteEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getServiceSuite_Name()
  {
    return (EAttribute)serviceSuiteEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getServiceSuite_Description()
  {
    return (EAttribute)serviceSuiteEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getServiceSuite_Services()
  {
    return (EReference)serviceSuiteEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getWebserver()
  {
    return webserverEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getWebserver_Name()
  {
    return (EAttribute)webserverEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getWebserver_URL()
  {
    return (EAttribute)webserverEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getWebserver_ServiceSuite()
  {
    return (EAttribute)webserverEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ConfigFactory getConfigFactory()
  {
    return (ConfigFactory)getEFactoryInstance();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private boolean isCreated = false;

  /**
   * Creates the meta-model objects for the package.  This method is
   * guarded to have no affect on any invocation but its first.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void createPackageContents()
  {
    if (isCreated) return;
    isCreated = true;

    // Create classes and their features
    backendSelectionEClass = createEClass(BACKEND_SELECTION);
    createEAttribute(backendSelectionEClass, BACKEND_SELECTION__GEOGRAPHY);
    createEAttribute(backendSelectionEClass, BACKEND_SELECTION__GLOBAL_APTS);
    createEAttribute(backendSelectionEClass, BACKEND_SELECTION__GLOBAL_GCMS);
    createEAttribute(backendSelectionEClass, BACKEND_SELECTION__PRODUCT_REFERENCE);
    createEAttribute(backendSelectionEClass, BACKEND_SELECTION__RDC);
    createEAttribute(backendSelectionEClass, BACKEND_SELECTION__RMI);
    createEAttribute(backendSelectionEClass, BACKEND_SELECTION__GCMS);

    configurationEClass = createEClass(CONFIG_URATION);
    createEAttribute(configurationEClass, CONFIG_URATION__SERVER_ID);
    createEAttribute(configurationEClass, CONFIG_URATION__NAME);
    createEAttribute(configurationEClass, CONFIG_URATION__DESCRIPTION);
    createEAttribute(configurationEClass, CONFIG_URATION__GROUP);
    createEReference(configurationEClass, CONFIG_URATION__LOCALIZED_BACKEND_SELECTIONS);

    contentManagerServerEClass = createEClass(CONTENT_MANAGER_SERVER);
    createEAttribute(contentManagerServerEClass, CONTENT_MANAGER_SERVER__NAME);
    createEAttribute(contentManagerServerEClass, CONTENT_MANAGER_SERVER__DESCRIPTION);

    databaseConnectionEClass = createEClass(DATABASE_CONNECTION);
    createEAttribute(databaseConnectionEClass, DATABASE_CONNECTION__CONNECTION_NAME);
    createEAttribute(databaseConnectionEClass, DATABASE_CONNECTION__SERVER_NAME);
    createEAttribute(databaseConnectionEClass, DATABASE_CONNECTION__HIGH_LEVEL_QUALIFIER);
    createEAttribute(databaseConnectionEClass, DATABASE_CONNECTION__USER_ID);

    dB2ServerEClass = createEClass(DB2_SERVER);
    createEAttribute(dB2ServerEClass, DB2_SERVER__NAME);
    createEAttribute(dB2ServerEClass, DB2_SERVER__INSTANCE_NODE);
    createEAttribute(dB2ServerEClass, DB2_SERVER__TARGET_DATABASE);
    createEAttribute(dB2ServerEClass, DB2_SERVER__HOSTNAME);
    createEAttribute(dB2ServerEClass, DB2_SERVER__PORT);
    createEAttribute(dB2ServerEClass, DB2_SERVER__OSTYPE);

    definitionEClass = createEClass(DEFINITION);
    createEReference(definitionEClass, DEFINITION__CONTENT_MANAGER_SERVER_DEFINITION);
    createEReference(definitionEClass, DEFINITION__DB2_SERVER_DEFINITION);
    createEReference(definitionEClass, DEFINITION__GCMS_WEBSERVER_DEFINITION);
    createEReference(definitionEClass, DEFINITION__RMI_SERVER_DEFINITION);
    createEAttribute(definitionEClass, DEFINITION__RDC_SERVER_DEFINITION);

    documentRootEClass = createEClass(DOCUMENT_ROOT);
    createEAttribute(documentRootEClass, DOCUMENT_ROOT__MIXED);
    createEReference(documentRootEClass, DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
    createEReference(documentRootEClass, DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
    createEReference(documentRootEClass, DOCUMENT_ROOT__GIL_CONFIG);

    geographyEClass = createEClass(GEOGRAPHY);
    createEAttribute(geographyEClass, GEOGRAPHY__GEOGRAPHY);
    createEAttribute(geographyEClass, GEOGRAPHY__DESCRIPTION);
    createEAttribute(geographyEClass, GEOGRAPHY__GROUP);
    createEReference(geographyEClass, GEOGRAPHY__SUB_GEOGRAPHY);

    gilConfigTypeEClass = createEClass(GIL_CONFIG_TYPE);
    createEReference(gilConfigTypeEClass, GIL_CONFIG_TYPE__CONFIGURATION);
    createEReference(gilConfigTypeEClass, GIL_CONFIG_TYPE__CONTENT_MANAGER_SERVER_DEFINITION);
    createEReference(gilConfigTypeEClass, GIL_CONFIG_TYPE__DB2_SERVER_DEFINITIONS);
    createEReference(gilConfigTypeEClass, GIL_CONFIG_TYPE__SERVICE_SUITES);
    createEReference(gilConfigTypeEClass, GIL_CONFIG_TYPE__GCMS_WEBSERVICE_DEFINITIONS);
    createEReference(gilConfigTypeEClass, GIL_CONFIG_TYPE__RDC_SERVER_DEFINITIONS);
    createEReference(gilConfigTypeEClass, GIL_CONFIG_TYPE__DATABASE_CONNECTIONS);
    createEReference(gilConfigTypeEClass, GIL_CONFIG_TYPE__GEOGRAPHIES);

    newComplexTypeEClass = createEClass(NEW_COMPLEX_TYPE);

    rmiServerEClass = createEClass(RMI_SERVER);
    createEAttribute(rmiServerEClass, RMI_SERVER__NAME);
    createEAttribute(rmiServerEClass, RMI_SERVER__URL);
    createEReference(rmiServerEClass, RMI_SERVER__SERVICE);

    serviceEClass = createEClass(SERVICE);
    createEAttribute(serviceEClass, SERVICE__NAME);
    createEAttribute(serviceEClass, SERVICE__WEB_SERVICE);
    createEAttribute(serviceEClass, SERVICE__NAMESPACE);

    serviceSuiteEClass = createEClass(SERVICE_SUITE);
    createEAttribute(serviceSuiteEClass, SERVICE_SUITE__NAME);
    createEAttribute(serviceSuiteEClass, SERVICE_SUITE__DESCRIPTION);
    createEReference(serviceSuiteEClass, SERVICE_SUITE__SERVICES);

    webserverEClass = createEClass(WEBSERVER);
    createEAttribute(webserverEClass, WEBSERVER__NAME);
    createEAttribute(webserverEClass, WEBSERVER__URL);
    createEAttribute(webserverEClass, WEBSERVER__SERVICE_SUITE);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private boolean isInitialized = false;

  /**
   * Complete the initialization of the package and its meta-model.  This
   * method is guarded to have no affect on any invocation but its first.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void initializePackageContents()
  {
    if (isInitialized) return;
    isInitialized = true;

    // Initialize package
    setName(eNAME);
    setNsPrefix(eNS_PREFIX);
    setNsURI(eNS_URI);

    // Obtain other dependent packages
    XMLTypePackageImpl theXMLTypePackage = (XMLTypePackageImpl)EPackage.Registry.INSTANCE.getEPackage(XMLTypePackage.eNS_URI);

    // Add supertypes to classes

    // Initialize classes and features; add operations and parameters
    initEClass(backendSelectionEClass, BackendSelection.class, "BackendSelection", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getBackendSelection_Geography(), theXMLTypePackage.getString(), "geography", null, 1, 1, BackendSelection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getBackendSelection_GlobalAPTS(), theXMLTypePackage.getString(), "globalAPTS", null, 1, 1, BackendSelection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getBackendSelection_GlobalGCMS(), theXMLTypePackage.getString(), "globalGCMS", null, 1, 1, BackendSelection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getBackendSelection_ProductReference(), theXMLTypePackage.getString(), "productReference", null, 1, 1, BackendSelection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getBackendSelection_RDC(), theXMLTypePackage.getString(), "rDC", null, 1, 1, BackendSelection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getBackendSelection_RMI(), theXMLTypePackage.getString(), "rMI", null, 1, 1, BackendSelection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getBackendSelection_GCMS(), theXMLTypePackage.getString(), "gCMS", null, 1, 1, BackendSelection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(configurationEClass, Configuration.class, "Configuration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getConfiguration_ServerId(), theXMLTypePackage.getString(), "serverId", null, 1, 1, Configuration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getConfiguration_Name(), theXMLTypePackage.getString(), "name", null, 1, 1, Configuration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getConfiguration_Description(), theXMLTypePackage.getString(), "description", null, 1, 1, Configuration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getConfiguration_Group(), ecorePackage.getEFeatureMapEntry(), "group", null, 0, -1, Configuration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getConfiguration_LocalizedBackendSelections(), this.getBackendSelection(), null, "localizedBackendSelections", null, 1, -1, Configuration.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

    initEClass(contentManagerServerEClass, ContentManagerServer.class, "ContentManagerServer", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getContentManagerServer_Name(), theXMLTypePackage.getString(), "name", null, 1, 1, ContentManagerServer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getContentManagerServer_Description(), theXMLTypePackage.getString(), "description", null, 1, 1, ContentManagerServer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(databaseConnectionEClass, DatabaseConnection.class, "DatabaseConnection", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getDatabaseConnection_ConnectionName(), theXMLTypePackage.getString(), "connectionName", null, 1, 1, DatabaseConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getDatabaseConnection_ServerName(), theXMLTypePackage.getString(), "serverName", null, 1, 1, DatabaseConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getDatabaseConnection_HighLevelQualifier(), theXMLTypePackage.getString(), "highLevelQualifier", null, 1, 1, DatabaseConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getDatabaseConnection_UserId(), theXMLTypePackage.getString(), "userId", null, 1, 1, DatabaseConnection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(dB2ServerEClass, DB2Server.class, "DB2Server", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getDB2Server_Name(), theXMLTypePackage.getString(), "name", null, 1, 1, DB2Server.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getDB2Server_InstanceNode(), theXMLTypePackage.getString(), "instanceNode", null, 1, 1, DB2Server.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getDB2Server_TargetDatabase(), theXMLTypePackage.getString(), "targetDatabase", null, 1, 1, DB2Server.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getDB2Server_Hostname(), theXMLTypePackage.getString(), "hostname", null, 1, 1, DB2Server.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getDB2Server_Port(), theXMLTypePackage.getInteger(), "port", null, 1, 1, DB2Server.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getDB2Server_OSType(), theXMLTypePackage.getString(), "oSType", null, 1, 1, DB2Server.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(definitionEClass, Definition.class, "Definition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getDefinition_ContentManagerServerDefinition(), this.getContentManagerServer(), null, "contentManagerServerDefinition", null, 1, -1, Definition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getDefinition_DB2ServerDefinition(), this.getDB2Server(), null, "dB2ServerDefinition", null, 1, -1, Definition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getDefinition_GCMSWebserverDefinition(), this.getWebserver(), null, "gCMSWebserverDefinition", null, 1, -1, Definition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getDefinition_RMIServerDefinition(), this.getRMIServer(), null, "rMIServerDefinition", null, 1, -1, Definition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getDefinition_RDCServerDefinition(), theXMLTypePackage.getString(), "rDCServerDefinition", null, 1, -1, Definition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(documentRootEClass, DocumentRoot.class, "DocumentRoot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getDocumentRoot_Mixed(), ecorePackage.getEFeatureMapEntry(), "mixed", null, 0, -1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getDocumentRoot_XMLNSPrefixMap(), ecorePackage.getEStringToStringMapEntry(), null, "xMLNSPrefixMap", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getDocumentRoot_XSISchemaLocation(), ecorePackage.getEStringToStringMapEntry(), null, "xSISchemaLocation", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getDocumentRoot_GilConfig(), this.getGILConfigType(), null, "gilConfig", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

    initEClass(geographyEClass, Geography.class, "Geography", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getGeography_Geography(), theXMLTypePackage.getString(), "geography", null, 1, 1, Geography.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getGeography_Description(), theXMLTypePackage.getString(), "description", null, 1, 1, Geography.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getGeography_Group(), ecorePackage.getEFeatureMapEntry(), "group", null, 0, -1, Geography.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getGeography_SubGeography(), this.getGeography(), null, "subGeography", null, 0, -1, Geography.class, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

    initEClass(gilConfigTypeEClass, GILConfigType.class, "GILConfigType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getGILConfigType_Configuration(), this.getConfiguration(), null, "configuration", null, 1, -1, GILConfigType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getGILConfigType_ContentManagerServerDefinition(), this.getContentManagerServer(), null, "contentManagerServerDefinition", null, 1, -1, GILConfigType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getGILConfigType_DB2ServerDefinitions(), this.getDB2Server(), null, "dB2ServerDefinitions", null, 1, -1, GILConfigType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getGILConfigType_ServiceSuites(), this.getServiceSuite(), null, "serviceSuites", null, 1, -1, GILConfigType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getGILConfigType_GCMSWebserviceDefinitions(), this.getWebserver(), null, "gCMSWebserviceDefinitions", null, 1, -1, GILConfigType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getGILConfigType_RDCServerDefinitions(), this.getWebserver(), null, "rDCServerDefinitions", null, 1, -1, GILConfigType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getGILConfigType_DatabaseConnections(), this.getDatabaseConnection(), null, "databaseConnections", null, 1, -1, GILConfigType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getGILConfigType_Geographies(), this.getGeography(), null, "geographies", null, 1, 1, GILConfigType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(newComplexTypeEClass, NewComplexType.class, "NewComplexType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(rmiServerEClass, RMIServer.class, "RMIServer", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getRMIServer_Name(), theXMLTypePackage.getString(), "name", null, 1, 1, RMIServer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getRMIServer_URL(), theXMLTypePackage.getString(), "uRL", null, 1, 1, RMIServer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getRMIServer_Service(), this.getService(), null, "service", null, 1, 1, RMIServer.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(serviceEClass, Service.class, "Service", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getService_Name(), theXMLTypePackage.getString(), "name", null, 1, 1, Service.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getService_WebService(), theXMLTypePackage.getString(), "webService", null, 1, 1, Service.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getService_Namespace(), theXMLTypePackage.getString(), "namespace", null, 0, 1, Service.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(serviceSuiteEClass, ServiceSuite.class, "ServiceSuite", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getServiceSuite_Name(), theXMLTypePackage.getString(), "name", null, 1, 1, ServiceSuite.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getServiceSuite_Description(), theXMLTypePackage.getString(), "description", null, 1, 1, ServiceSuite.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getServiceSuite_Services(), this.getService(), null, "services", null, 1, -1, ServiceSuite.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(webserverEClass, Webserver.class, "Webserver", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getWebserver_Name(), theXMLTypePackage.getString(), "name", null, 1, 1, Webserver.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getWebserver_URL(), theXMLTypePackage.getString(), "uRL", null, 1, 1, Webserver.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getWebserver_ServiceSuite(), theXMLTypePackage.getString(), "serviceSuite", null, 1, 1, Webserver.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    // Create resource
    createResource(eNS_URI);

    // Create annotations
    // http:///org/eclipse/emf/ecore/util/ExtendedMetaData
    createExtendedMetaDataAnnotations();
  }

  /**
   * Initializes the annotations for <b>http:///org/eclipse/emf/ecore/util/ExtendedMetaData</b>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void createExtendedMetaDataAnnotations()
  {
    String source = "http:///org/eclipse/emf/ecore/util/ExtendedMetaData";		
    addAnnotation
      (backendSelectionEClass, 
       source, 
       new String[] 
       {
       "name", "BackendSelection",
       "kind", "elementOnly"
       });		
    addAnnotation
      (getBackendSelection_Geography(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "Geography"
       });		
    addAnnotation
      (getBackendSelection_GlobalAPTS(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "GlobalAPTS"
       });		
    addAnnotation
      (getBackendSelection_GlobalGCMS(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "GlobalGCMS"
       });		
    addAnnotation
      (getBackendSelection_ProductReference(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "ProductReference"
       });		
    addAnnotation
      (getBackendSelection_RDC(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "RDC"
       });		
    addAnnotation
      (getBackendSelection_RMI(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "RMI"
       });		
    addAnnotation
      (getBackendSelection_GCMS(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "GCMS"
       });		
    addAnnotation
      (configurationEClass, 
       source, 
       new String[] 
       {
       "name", "Configuration",
       "kind", "elementOnly"
       });		
    addAnnotation
      (getConfiguration_ServerId(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "ServerId"
       });		
    addAnnotation
      (getConfiguration_Name(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "Name"
       });		
    addAnnotation
      (getConfiguration_Description(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "Description"
       });		
    addAnnotation
      (getConfiguration_Group(), 
       source, 
       new String[] 
       {
       "kind", "group",
       "name", "group:3"
       });		
    addAnnotation
      (getConfiguration_LocalizedBackendSelections(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "LocalizedBackendSelections",
       "group", "#group:3"
       });		
    addAnnotation
      (contentManagerServerEClass, 
       source, 
       new String[] 
       {
       "name", "ContentManagerServer",
       "kind", "elementOnly"
       });		
    addAnnotation
      (getContentManagerServer_Name(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "Name"
       });		
    addAnnotation
      (getContentManagerServer_Description(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "Description"
       });		
    addAnnotation
      (databaseConnectionEClass, 
       source, 
       new String[] 
       {
       "name", "DatabaseConnection",
       "kind", "elementOnly"
       });		
    addAnnotation
      (getDatabaseConnection_ConnectionName(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "ConnectionName"
       });		
    addAnnotation
      (getDatabaseConnection_ServerName(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "ServerName"
       });		
    addAnnotation
      (getDatabaseConnection_HighLevelQualifier(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "HighLevelQualifier"
       });		
    addAnnotation
      (getDatabaseConnection_UserId(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "UserId"
       });		
    addAnnotation
      (dB2ServerEClass, 
       source, 
       new String[] 
       {
       "name", "DB2Server",
       "kind", "elementOnly"
       });		
    addAnnotation
      (getDB2Server_Name(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "Name"
       });		
    addAnnotation
      (getDB2Server_InstanceNode(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "InstanceNode"
       });		
    addAnnotation
      (getDB2Server_TargetDatabase(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "TargetDatabase"
       });		
    addAnnotation
      (getDB2Server_Hostname(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "Hostname"
       });		
    addAnnotation
      (getDB2Server_Port(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "Port"
       });		
    addAnnotation
      (getDB2Server_OSType(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "OSType"
       });		
    addAnnotation
      (definitionEClass, 
       source, 
       new String[] 
       {
       "name", "Definition",
       "kind", "elementOnly"
       });		
    addAnnotation
      (getDefinition_ContentManagerServerDefinition(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "ContentManagerServerDefinition"
       });		
    addAnnotation
      (getDefinition_DB2ServerDefinition(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "DB2ServerDefinition"
       });		
    addAnnotation
      (getDefinition_GCMSWebserverDefinition(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "GCMSWebserverDefinition"
       });		
    addAnnotation
      (getDefinition_RMIServerDefinition(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "RMIServerDefinition"
       });		
    addAnnotation
      (getDefinition_RDCServerDefinition(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "RDCServerDefinition"
       });		
    addAnnotation
      (documentRootEClass, 
       source, 
       new String[] 
       {
       "name", "",
       "kind", "mixed"
       });		
    addAnnotation
      (getDocumentRoot_Mixed(), 
       source, 
       new String[] 
       {
       "kind", "elementWildcard",
       "name", ":mixed"
       });		
    addAnnotation
      (getDocumentRoot_XMLNSPrefixMap(), 
       source, 
       new String[] 
       {
       "kind", "attribute",
       "name", "xmlns:prefix"
       });		
    addAnnotation
      (getDocumentRoot_XSISchemaLocation(), 
       source, 
       new String[] 
       {
       "kind", "attribute",
       "name", "xsi:schemaLocation"
       });		
    addAnnotation
      (getDocumentRoot_GilConfig(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "GILConfig",
       "namespace", "##targetNamespace"
       });		
    addAnnotation
      (geographyEClass, 
       source, 
       new String[] 
       {
       "name", "Geography",
       "kind", "elementOnly"
       });		
    addAnnotation
      (getGeography_Geography(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "Geography"
       });		
    addAnnotation
      (getGeography_Description(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "Description"
       });		
    addAnnotation
      (getGeography_Group(), 
       source, 
       new String[] 
       {
       "kind", "group",
       "name", "group:2"
       });		
    addAnnotation
      (getGeography_SubGeography(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "SubGeography",
       "group", "#group:2"
       });		
    addAnnotation
      (gilConfigTypeEClass, 
       source, 
       new String[] 
       {
       "name", "GILConfig_._type",
       "kind", "elementOnly"
       });		
    addAnnotation
      (getGILConfigType_Configuration(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "Configuration"
       });		
    addAnnotation
      (getGILConfigType_ContentManagerServerDefinition(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "ContentManagerServerDefinition"
       });		
    addAnnotation
      (getGILConfigType_DB2ServerDefinitions(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "DB2ServerDefinitions"
       });		
    addAnnotation
      (getGILConfigType_ServiceSuites(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "ServiceSuites"
       });		
    addAnnotation
      (getGILConfigType_GCMSWebserviceDefinitions(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "GCMSWebserviceDefinitions"
       });		
    addAnnotation
      (getGILConfigType_RDCServerDefinitions(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "RDCServerDefinitions"
       });		
    addAnnotation
      (getGILConfigType_DatabaseConnections(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "DatabaseConnections"
       });		
    addAnnotation
      (getGILConfigType_Geographies(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "Geographies"
       });		
    addAnnotation
      (newComplexTypeEClass, 
       source, 
       new String[] 
       {
       "name", "NewComplexType",
       "kind", "empty"
       });		
    addAnnotation
      (rmiServerEClass, 
       source, 
       new String[] 
       {
       "name", "RMIServer",
       "kind", "elementOnly"
       });		
    addAnnotation
      (getRMIServer_Name(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "Name"
       });		
    addAnnotation
      (getRMIServer_URL(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "URL"
       });		
    addAnnotation
      (getRMIServer_Service(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "Service"
       });		
    addAnnotation
      (serviceEClass, 
       source, 
       new String[] 
       {
       "name", "Service",
       "kind", "elementOnly"
       });		
    addAnnotation
      (getService_Name(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "Name"
       });		
    addAnnotation
      (getService_WebService(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "WebService"
       });		
    addAnnotation
      (getService_Namespace(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "Namespace"
       });		
    addAnnotation
      (serviceSuiteEClass, 
       source, 
       new String[] 
       {
       "name", "ServiceSuite",
       "kind", "elementOnly"
       });		
    addAnnotation
      (getServiceSuite_Name(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "Name"
       });		
    addAnnotation
      (getServiceSuite_Description(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "Description"
       });		
    addAnnotation
      (getServiceSuite_Services(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "Services"
       });		
    addAnnotation
      (webserverEClass, 
       source, 
       new String[] 
       {
       "name", "Webserver",
       "kind", "elementOnly"
       });		
    addAnnotation
      (getWebserver_Name(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "Name"
       });		
    addAnnotation
      (getWebserver_URL(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "URL"
       });		
    addAnnotation
      (getWebserver_ServiceSuite(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "ServiceSuite"
       });
  }

} //ConfigPackageImpl
