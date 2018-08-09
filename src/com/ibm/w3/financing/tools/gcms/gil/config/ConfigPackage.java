/**
 * <copyright>
 * </copyright>
 *
 * $Id: ConfigPackage.java,v 1.2 2008/02/11 14:31:31 sbaber Exp $
 */
package com.ibm.w3.financing.tools.gcms.gil.config;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see com.ibm.w3.financing.tools.gcms.gil.config.ConfigFactory
 * @generated
 */
public interface ConfigPackage extends EPackage{
  /**
   * The package name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNAME = "config";

  /**
   * The package namespace URI.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_URI = "http://w3.ibm.com/financing/tools/GCMS/GILConfig";

  /**
   * The package namespace name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_PREFIX = "config";

  /**
   * The singleton instance of the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  ConfigPackage eINSTANCE = com.ibm.w3.financing.tools.gcms.gil.config.impl.ConfigPackageImpl.init();

  /**
   * The meta object id for the '{@link com.ibm.w3.financing.tools.gcms.gil.config.impl.BackendSelectionImpl <em>Backend Selection</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.ibm.w3.financing.tools.gcms.gil.config.impl.BackendSelectionImpl
   * @see com.ibm.w3.financing.tools.gcms.gil.config.impl.ConfigPackageImpl#getBackendSelection()
   * @generated
   */
  int BACKEND_SELECTION = 0;

  /**
   * The feature id for the '<em><b>Geography</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BACKEND_SELECTION__GEOGRAPHY = 0;

  /**
   * The feature id for the '<em><b>Global APTS</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BACKEND_SELECTION__GLOBAL_APTS = 1;

  /**
   * The feature id for the '<em><b>Global GCMS</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BACKEND_SELECTION__GLOBAL_GCMS = 2;

  /**
   * The feature id for the '<em><b>Product Reference</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BACKEND_SELECTION__PRODUCT_REFERENCE = 3;

  /**
   * The feature id for the '<em><b>RDC</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BACKEND_SELECTION__RDC = 4;

  /**
   * The feature id for the '<em><b>RMI</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BACKEND_SELECTION__RMI = 5;

  /**
   * The feature id for the '<em><b>GCMS</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BACKEND_SELECTION__GCMS = 6;

  /**
   * The number of structural features of the the '<em>Backend Selection</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BACKEND_SELECTION_FEATURE_COUNT = 7;

  /**
   * The meta object id for the '{@link com.ibm.w3.financing.tools.gcms.gil.config.impl.ConfigurationImpl <em>uration</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.ibm.w3.financing.tools.gcms.gil.config.impl.ConfigurationImpl
   * @see com.ibm.w3.financing.tools.gcms.gil.config.impl.ConfigPackageImpl#getConfiguration()
   * @generated
   */
  int CONFIG_URATION = 1;

  /**
   * The feature id for the '<em><b>Server Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CONFIG_URATION__SERVER_ID = 0;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CONFIG_URATION__NAME = 1;

  /**
   * The feature id for the '<em><b>Description</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CONFIG_URATION__DESCRIPTION = 2;

  /**
   * The feature id for the '<em><b>Group</b></em>' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CONFIG_URATION__GROUP = 3;

  /**
   * The feature id for the '<em><b>Localized Backend Selections</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CONFIG_URATION__LOCALIZED_BACKEND_SELECTIONS = 4;

  /**
   * The number of structural features of the the '<em>uration</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CONFIG_URATION_FEATURE_COUNT = 5;

  /**
   * The meta object id for the '{@link com.ibm.w3.financing.tools.gcms.gil.config.impl.ContentManagerServerImpl <em>Content Manager Server</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.ibm.w3.financing.tools.gcms.gil.config.impl.ContentManagerServerImpl
   * @see com.ibm.w3.financing.tools.gcms.gil.config.impl.ConfigPackageImpl#getContentManagerServer()
   * @generated
   */
  int CONTENT_MANAGER_SERVER = 2;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CONTENT_MANAGER_SERVER__NAME = 0;

  /**
   * The feature id for the '<em><b>Description</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CONTENT_MANAGER_SERVER__DESCRIPTION = 1;

  /**
   * The number of structural features of the the '<em>Content Manager Server</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int CONTENT_MANAGER_SERVER_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link com.ibm.w3.financing.tools.gcms.gil.config.impl.DatabaseConnectionImpl <em>Database Connection</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.ibm.w3.financing.tools.gcms.gil.config.impl.DatabaseConnectionImpl
   * @see com.ibm.w3.financing.tools.gcms.gil.config.impl.ConfigPackageImpl#getDatabaseConnection()
   * @generated
   */
  int DATABASE_CONNECTION = 3;

  /**
   * The feature id for the '<em><b>Connection Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DATABASE_CONNECTION__CONNECTION_NAME = 0;

  /**
   * The feature id for the '<em><b>Server Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DATABASE_CONNECTION__SERVER_NAME = 1;

  /**
   * The feature id for the '<em><b>High Level Qualifier</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DATABASE_CONNECTION__HIGH_LEVEL_QUALIFIER = 2;

  /**
   * The feature id for the '<em><b>User Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DATABASE_CONNECTION__USER_ID = 3;

  /**
   * The number of structural features of the the '<em>Database Connection</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DATABASE_CONNECTION_FEATURE_COUNT = 4;

  /**
   * The meta object id for the '{@link com.ibm.w3.financing.tools.gcms.gil.config.impl.DB2ServerImpl <em>DB2 Server</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.ibm.w3.financing.tools.gcms.gil.config.impl.DB2ServerImpl
   * @see com.ibm.w3.financing.tools.gcms.gil.config.impl.ConfigPackageImpl#getDB2Server()
   * @generated
   */
  int DB2_SERVER = 4;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DB2_SERVER__NAME = 0;

  /**
   * The feature id for the '<em><b>Instance Node</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DB2_SERVER__INSTANCE_NODE = 1;

  /**
   * The feature id for the '<em><b>Target Database</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DB2_SERVER__TARGET_DATABASE = 2;

  /**
   * The feature id for the '<em><b>Hostname</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DB2_SERVER__HOSTNAME = 3;

  /**
   * The feature id for the '<em><b>Port</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DB2_SERVER__PORT = 4;

  /**
   * The feature id for the '<em><b>OS Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DB2_SERVER__OSTYPE = 5;

  /**
   * The number of structural features of the the '<em>DB2 Server</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DB2_SERVER_FEATURE_COUNT = 6;

  /**
   * The meta object id for the '{@link com.ibm.w3.financing.tools.gcms.gil.config.impl.DefinitionImpl <em>Definition</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.ibm.w3.financing.tools.gcms.gil.config.impl.DefinitionImpl
   * @see com.ibm.w3.financing.tools.gcms.gil.config.impl.ConfigPackageImpl#getDefinition()
   * @generated
   */
  int DEFINITION = 5;

  /**
   * The feature id for the '<em><b>Content Manager Server Definition</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEFINITION__CONTENT_MANAGER_SERVER_DEFINITION = 0;

  /**
   * The feature id for the '<em><b>DB2 Server Definition</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEFINITION__DB2_SERVER_DEFINITION = 1;

  /**
   * The feature id for the '<em><b>GCMS Webserver Definition</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEFINITION__GCMS_WEBSERVER_DEFINITION = 2;

  /**
   * The feature id for the '<em><b>RMI Server Definition</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEFINITION__RMI_SERVER_DEFINITION = 3;

  /**
   * The feature id for the '<em><b>RDC Server Definition</b></em>' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEFINITION__RDC_SERVER_DEFINITION = 4;

  /**
   * The number of structural features of the the '<em>Definition</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DEFINITION_FEATURE_COUNT = 5;

  /**
   * The meta object id for the '{@link com.ibm.w3.financing.tools.gcms.gil.config.impl.DocumentRootImpl <em>Document Root</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.ibm.w3.financing.tools.gcms.gil.config.impl.DocumentRootImpl
   * @see com.ibm.w3.financing.tools.gcms.gil.config.impl.ConfigPackageImpl#getDocumentRoot()
   * @generated
   */
  int DOCUMENT_ROOT = 6;

  /**
   * The feature id for the '<em><b>Mixed</b></em>' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DOCUMENT_ROOT__MIXED = 0;

  /**
   * The feature id for the '<em><b>XMLNS Prefix Map</b></em>' map.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DOCUMENT_ROOT__XMLNS_PREFIX_MAP = 1;

  /**
   * The feature id for the '<em><b>XSI Schema Location</b></em>' map.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DOCUMENT_ROOT__XSI_SCHEMA_LOCATION = 2;

  /**
   * The feature id for the '<em><b>Gil Config</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DOCUMENT_ROOT__GIL_CONFIG = 3;

  /**
   * The number of structural features of the the '<em>Document Root</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DOCUMENT_ROOT_FEATURE_COUNT = 4;

  /**
   * The meta object id for the '{@link com.ibm.w3.financing.tools.gcms.gil.config.impl.GeographyImpl <em>Geography</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.ibm.w3.financing.tools.gcms.gil.config.impl.GeographyImpl
   * @see com.ibm.w3.financing.tools.gcms.gil.config.impl.ConfigPackageImpl#getGeography()
   * @generated
   */
  int GEOGRAPHY = 7;

  /**
   * The feature id for the '<em><b>Geography</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GEOGRAPHY__GEOGRAPHY = 0;

  /**
   * The feature id for the '<em><b>Description</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GEOGRAPHY__DESCRIPTION = 1;

  /**
   * The feature id for the '<em><b>Group</b></em>' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GEOGRAPHY__GROUP = 2;

  /**
   * The feature id for the '<em><b>Sub Geography</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GEOGRAPHY__SUB_GEOGRAPHY = 3;

  /**
   * The number of structural features of the the '<em>Geography</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GEOGRAPHY_FEATURE_COUNT = 4;

  /**
   * The meta object id for the '{@link com.ibm.w3.financing.tools.gcms.gil.config.impl.GILConfigTypeImpl <em>GIL Config Type</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.ibm.w3.financing.tools.gcms.gil.config.impl.GILConfigTypeImpl
   * @see com.ibm.w3.financing.tools.gcms.gil.config.impl.ConfigPackageImpl#getGILConfigType()
   * @generated
   */
  int GIL_CONFIG_TYPE = 8;

  /**
   * The feature id for the '<em><b>Configuration</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GIL_CONFIG_TYPE__CONFIGURATION = 0;

  /**
   * The feature id for the '<em><b>Content Manager Server Definition</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GIL_CONFIG_TYPE__CONTENT_MANAGER_SERVER_DEFINITION = 1;

  /**
   * The feature id for the '<em><b>DB2 Server Definitions</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GIL_CONFIG_TYPE__DB2_SERVER_DEFINITIONS = 2;

  /**
   * The feature id for the '<em><b>Service Suites</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GIL_CONFIG_TYPE__SERVICE_SUITES = 3;

  /**
   * The feature id for the '<em><b>GCMS Webservice Definitions</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GIL_CONFIG_TYPE__GCMS_WEBSERVICE_DEFINITIONS = 4;

  /**
   * The feature id for the '<em><b>RDC Server Definitions</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GIL_CONFIG_TYPE__RDC_SERVER_DEFINITIONS = 5;

  /**
   * The feature id for the '<em><b>Database Connections</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GIL_CONFIG_TYPE__DATABASE_CONNECTIONS = 6;

  /**
   * The feature id for the '<em><b>Geographies</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GIL_CONFIG_TYPE__GEOGRAPHIES = 7;

  /**
   * The number of structural features of the the '<em>GIL Config Type</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GIL_CONFIG_TYPE_FEATURE_COUNT = 8;

  /**
   * The meta object id for the '{@link com.ibm.w3.financing.tools.gcms.gil.config.impl.NewComplexTypeImpl <em>New Complex Type</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.ibm.w3.financing.tools.gcms.gil.config.impl.NewComplexTypeImpl
   * @see com.ibm.w3.financing.tools.gcms.gil.config.impl.ConfigPackageImpl#getNewComplexType()
   * @generated
   */
  int NEW_COMPLEX_TYPE = 9;

  /**
   * The number of structural features of the the '<em>New Complex Type</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int NEW_COMPLEX_TYPE_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link com.ibm.w3.financing.tools.gcms.gil.config.impl.RMIServerImpl <em>RMI Server</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.ibm.w3.financing.tools.gcms.gil.config.impl.RMIServerImpl
   * @see com.ibm.w3.financing.tools.gcms.gil.config.impl.ConfigPackageImpl#getRMIServer()
   * @generated
   */
  int RMI_SERVER = 10;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RMI_SERVER__NAME = 0;

  /**
   * The feature id for the '<em><b>URL</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RMI_SERVER__URL = 1;

  /**
   * The feature id for the '<em><b>Service</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RMI_SERVER__SERVICE = 2;

  /**
   * The number of structural features of the the '<em>RMI Server</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RMI_SERVER_FEATURE_COUNT = 3;

  /**
   * The meta object id for the '{@link com.ibm.w3.financing.tools.gcms.gil.config.impl.ServiceImpl <em>Service</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.ibm.w3.financing.tools.gcms.gil.config.impl.ServiceImpl
   * @see com.ibm.w3.financing.tools.gcms.gil.config.impl.ConfigPackageImpl#getService()
   * @generated
   */
  int SERVICE = 11;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SERVICE__NAME = 0;

  /**
   * The feature id for the '<em><b>Web Service</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SERVICE__WEB_SERVICE = 1;

  /**
   * The feature id for the '<em><b>Namespace</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SERVICE__NAMESPACE = 2;

  /**
   * The number of structural features of the the '<em>Service</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SERVICE_FEATURE_COUNT = 3;

  /**
   * The meta object id for the '{@link com.ibm.w3.financing.tools.gcms.gil.config.impl.ServiceSuiteImpl <em>Service Suite</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.ibm.w3.financing.tools.gcms.gil.config.impl.ServiceSuiteImpl
   * @see com.ibm.w3.financing.tools.gcms.gil.config.impl.ConfigPackageImpl#getServiceSuite()
   * @generated
   */
  int SERVICE_SUITE = 12;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SERVICE_SUITE__NAME = 0;

  /**
   * The feature id for the '<em><b>Description</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SERVICE_SUITE__DESCRIPTION = 1;

  /**
   * The feature id for the '<em><b>Services</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SERVICE_SUITE__SERVICES = 2;

  /**
   * The number of structural features of the the '<em>Service Suite</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int SERVICE_SUITE_FEATURE_COUNT = 3;

  /**
   * The meta object id for the '{@link com.ibm.w3.financing.tools.gcms.gil.config.impl.WebserverImpl <em>Webserver</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.ibm.w3.financing.tools.gcms.gil.config.impl.WebserverImpl
   * @see com.ibm.w3.financing.tools.gcms.gil.config.impl.ConfigPackageImpl#getWebserver()
   * @generated
   */
  int WEBSERVER = 13;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int WEBSERVER__NAME = 0;

  /**
   * The feature id for the '<em><b>URL</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int WEBSERVER__URL = 1;

  /**
   * The feature id for the '<em><b>Service Suite</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int WEBSERVER__SERVICE_SUITE = 2;

  /**
   * The number of structural features of the the '<em>Webserver</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int WEBSERVER_FEATURE_COUNT = 3;


  /**
   * Returns the meta object for class '{@link com.ibm.w3.financing.tools.gcms.gil.config.BackendSelection <em>Backend Selection</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Backend Selection</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.BackendSelection
   * @generated
   */
  EClass getBackendSelection();

  /**
   * Returns the meta object for the attribute '{@link com.ibm.w3.financing.tools.gcms.gil.config.BackendSelection#getGeography <em>Geography</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Geography</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.BackendSelection#getGeography()
   * @see #getBackendSelection()
   * @generated
   */
  EAttribute getBackendSelection_Geography();

  /**
   * Returns the meta object for the attribute '{@link com.ibm.w3.financing.tools.gcms.gil.config.BackendSelection#getGlobalAPTS <em>Global APTS</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Global APTS</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.BackendSelection#getGlobalAPTS()
   * @see #getBackendSelection()
   * @generated
   */
  EAttribute getBackendSelection_GlobalAPTS();

  /**
   * Returns the meta object for the attribute '{@link com.ibm.w3.financing.tools.gcms.gil.config.BackendSelection#getGlobalGCMS <em>Global GCMS</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Global GCMS</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.BackendSelection#getGlobalGCMS()
   * @see #getBackendSelection()
   * @generated
   */
  EAttribute getBackendSelection_GlobalGCMS();

  /**
   * Returns the meta object for the attribute '{@link com.ibm.w3.financing.tools.gcms.gil.config.BackendSelection#getProductReference <em>Product Reference</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Product Reference</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.BackendSelection#getProductReference()
   * @see #getBackendSelection()
   * @generated
   */
  EAttribute getBackendSelection_ProductReference();

  /**
   * Returns the meta object for the attribute '{@link com.ibm.w3.financing.tools.gcms.gil.config.BackendSelection#getRDC <em>RDC</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>RDC</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.BackendSelection#getRDC()
   * @see #getBackendSelection()
   * @generated
   */
  EAttribute getBackendSelection_RDC();

  /**
   * Returns the meta object for the attribute '{@link com.ibm.w3.financing.tools.gcms.gil.config.BackendSelection#getRMI <em>RMI</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>RMI</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.BackendSelection#getRMI()
   * @see #getBackendSelection()
   * @generated
   */
  EAttribute getBackendSelection_RMI();

  /**
   * Returns the meta object for the attribute '{@link com.ibm.w3.financing.tools.gcms.gil.config.BackendSelection#getGCMS <em>GCMS</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>GCMS</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.BackendSelection#getGCMS()
   * @see #getBackendSelection()
   * @generated
   */
  EAttribute getBackendSelection_GCMS();

  /**
   * Returns the meta object for class '{@link com.ibm.w3.financing.tools.gcms.gil.config.Configuration <em>uration</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>uration</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.Configuration
   * @generated
   */
  EClass getConfiguration();

  /**
   * Returns the meta object for the attribute '{@link com.ibm.w3.financing.tools.gcms.gil.config.Configuration#getServerId <em>Server Id</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Server Id</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.Configuration#getServerId()
   * @see #getConfiguration()
   * @generated
   */
  EAttribute getConfiguration_ServerId();

  /**
   * Returns the meta object for the attribute '{@link com.ibm.w3.financing.tools.gcms.gil.config.Configuration#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.Configuration#getName()
   * @see #getConfiguration()
   * @generated
   */
  EAttribute getConfiguration_Name();

  /**
   * Returns the meta object for the attribute '{@link com.ibm.w3.financing.tools.gcms.gil.config.Configuration#getDescription <em>Description</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Description</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.Configuration#getDescription()
   * @see #getConfiguration()
   * @generated
   */
  EAttribute getConfiguration_Description();

  /**
   * Returns the meta object for the attribute list '{@link com.ibm.w3.financing.tools.gcms.gil.config.Configuration#getGroup <em>Group</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute list '<em>Group</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.Configuration#getGroup()
   * @see #getConfiguration()
   * @generated
   */
  EAttribute getConfiguration_Group();

  /**
   * Returns the meta object for the containment reference list '{@link com.ibm.w3.financing.tools.gcms.gil.config.Configuration#getLocalizedBackendSelections <em>Localized Backend Selections</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Localized Backend Selections</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.Configuration#getLocalizedBackendSelections()
   * @see #getConfiguration()
   * @generated
   */
  EReference getConfiguration_LocalizedBackendSelections();

  /**
   * Returns the meta object for class '{@link com.ibm.w3.financing.tools.gcms.gil.config.ContentManagerServer <em>Content Manager Server</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Content Manager Server</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.ContentManagerServer
   * @generated
   */
  EClass getContentManagerServer();

  /**
   * Returns the meta object for the attribute '{@link com.ibm.w3.financing.tools.gcms.gil.config.ContentManagerServer#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.ContentManagerServer#getName()
   * @see #getContentManagerServer()
   * @generated
   */
  EAttribute getContentManagerServer_Name();

  /**
   * Returns the meta object for the attribute '{@link com.ibm.w3.financing.tools.gcms.gil.config.ContentManagerServer#getDescription <em>Description</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Description</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.ContentManagerServer#getDescription()
   * @see #getContentManagerServer()
   * @generated
   */
  EAttribute getContentManagerServer_Description();

  /**
   * Returns the meta object for class '{@link com.ibm.w3.financing.tools.gcms.gil.config.DatabaseConnection <em>Database Connection</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Database Connection</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.DatabaseConnection
   * @generated
   */
  EClass getDatabaseConnection();

  /**
   * Returns the meta object for the attribute '{@link com.ibm.w3.financing.tools.gcms.gil.config.DatabaseConnection#getConnectionName <em>Connection Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Connection Name</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.DatabaseConnection#getConnectionName()
   * @see #getDatabaseConnection()
   * @generated
   */
  EAttribute getDatabaseConnection_ConnectionName();

  /**
   * Returns the meta object for the attribute '{@link com.ibm.w3.financing.tools.gcms.gil.config.DatabaseConnection#getServerName <em>Server Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Server Name</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.DatabaseConnection#getServerName()
   * @see #getDatabaseConnection()
   * @generated
   */
  EAttribute getDatabaseConnection_ServerName();

  /**
   * Returns the meta object for the attribute '{@link com.ibm.w3.financing.tools.gcms.gil.config.DatabaseConnection#getHighLevelQualifier <em>High Level Qualifier</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>High Level Qualifier</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.DatabaseConnection#getHighLevelQualifier()
   * @see #getDatabaseConnection()
   * @generated
   */
  EAttribute getDatabaseConnection_HighLevelQualifier();

  /**
   * Returns the meta object for the attribute '{@link com.ibm.w3.financing.tools.gcms.gil.config.DatabaseConnection#getUserId <em>User Id</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>User Id</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.DatabaseConnection#getUserId()
   * @see #getDatabaseConnection()
   * @generated
   */
  EAttribute getDatabaseConnection_UserId();

  /**
   * Returns the meta object for class '{@link com.ibm.w3.financing.tools.gcms.gil.config.DB2Server <em>DB2 Server</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>DB2 Server</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.DB2Server
   * @generated
   */
  EClass getDB2Server();

  /**
   * Returns the meta object for the attribute '{@link com.ibm.w3.financing.tools.gcms.gil.config.DB2Server#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.DB2Server#getName()
   * @see #getDB2Server()
   * @generated
   */
  EAttribute getDB2Server_Name();

  /**
   * Returns the meta object for the attribute '{@link com.ibm.w3.financing.tools.gcms.gil.config.DB2Server#getInstanceNode <em>Instance Node</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Instance Node</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.DB2Server#getInstanceNode()
   * @see #getDB2Server()
   * @generated
   */
  EAttribute getDB2Server_InstanceNode();

  /**
   * Returns the meta object for the attribute '{@link com.ibm.w3.financing.tools.gcms.gil.config.DB2Server#getTargetDatabase <em>Target Database</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Target Database</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.DB2Server#getTargetDatabase()
   * @see #getDB2Server()
   * @generated
   */
  EAttribute getDB2Server_TargetDatabase();

  /**
   * Returns the meta object for the attribute '{@link com.ibm.w3.financing.tools.gcms.gil.config.DB2Server#getHostname <em>Hostname</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Hostname</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.DB2Server#getHostname()
   * @see #getDB2Server()
   * @generated
   */
  EAttribute getDB2Server_Hostname();

  /**
   * Returns the meta object for the attribute '{@link com.ibm.w3.financing.tools.gcms.gil.config.DB2Server#getPort <em>Port</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Port</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.DB2Server#getPort()
   * @see #getDB2Server()
   * @generated
   */
  EAttribute getDB2Server_Port();

  /**
   * Returns the meta object for the attribute '{@link com.ibm.w3.financing.tools.gcms.gil.config.DB2Server#getOSType <em>OS Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>OS Type</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.DB2Server#getOSType()
   * @see #getDB2Server()
   * @generated
   */
  EAttribute getDB2Server_OSType();

  /**
   * Returns the meta object for class '{@link com.ibm.w3.financing.tools.gcms.gil.config.Definition <em>Definition</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Definition</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.Definition
   * @generated
   */
  EClass getDefinition();

  /**
   * Returns the meta object for the containment reference list '{@link com.ibm.w3.financing.tools.gcms.gil.config.Definition#getContentManagerServerDefinition <em>Content Manager Server Definition</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Content Manager Server Definition</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.Definition#getContentManagerServerDefinition()
   * @see #getDefinition()
   * @generated
   */
  EReference getDefinition_ContentManagerServerDefinition();

  /**
   * Returns the meta object for the containment reference list '{@link com.ibm.w3.financing.tools.gcms.gil.config.Definition#getDB2ServerDefinition <em>DB2 Server Definition</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>DB2 Server Definition</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.Definition#getDB2ServerDefinition()
   * @see #getDefinition()
   * @generated
   */
  EReference getDefinition_DB2ServerDefinition();

  /**
   * Returns the meta object for the containment reference list '{@link com.ibm.w3.financing.tools.gcms.gil.config.Definition#getGCMSWebserverDefinition <em>GCMS Webserver Definition</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>GCMS Webserver Definition</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.Definition#getGCMSWebserverDefinition()
   * @see #getDefinition()
   * @generated
   */
  EReference getDefinition_GCMSWebserverDefinition();

  /**
   * Returns the meta object for the containment reference list '{@link com.ibm.w3.financing.tools.gcms.gil.config.Definition#getRMIServerDefinition <em>RMI Server Definition</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>RMI Server Definition</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.Definition#getRMIServerDefinition()
   * @see #getDefinition()
   * @generated
   */
  EReference getDefinition_RMIServerDefinition();

  /**
   * Returns the meta object for the attribute list '{@link com.ibm.w3.financing.tools.gcms.gil.config.Definition#getRDCServerDefinition <em>RDC Server Definition</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute list '<em>RDC Server Definition</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.Definition#getRDCServerDefinition()
   * @see #getDefinition()
   * @generated
   */
  EAttribute getDefinition_RDCServerDefinition();

  /**
   * Returns the meta object for class '{@link com.ibm.w3.financing.tools.gcms.gil.config.DocumentRoot <em>Document Root</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Document Root</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.DocumentRoot
   * @generated
   */
  EClass getDocumentRoot();

  /**
   * Returns the meta object for the attribute list '{@link com.ibm.w3.financing.tools.gcms.gil.config.DocumentRoot#getMixed <em>Mixed</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute list '<em>Mixed</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.DocumentRoot#getMixed()
   * @see #getDocumentRoot()
   * @generated
   */
  EAttribute getDocumentRoot_Mixed();

  /**
   * Returns the meta object for the map '{@link com.ibm.w3.financing.tools.gcms.gil.config.DocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the map '<em>XMLNS Prefix Map</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.DocumentRoot#getXMLNSPrefixMap()
   * @see #getDocumentRoot()
   * @generated
   */
  EReference getDocumentRoot_XMLNSPrefixMap();

  /**
   * Returns the meta object for the map '{@link com.ibm.w3.financing.tools.gcms.gil.config.DocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the map '<em>XSI Schema Location</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.DocumentRoot#getXSISchemaLocation()
   * @see #getDocumentRoot()
   * @generated
   */
  EReference getDocumentRoot_XSISchemaLocation();

  /**
   * Returns the meta object for the containment reference '{@link com.ibm.w3.financing.tools.gcms.gil.config.DocumentRoot#getGilConfig <em>Gil Config</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Gil Config</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.DocumentRoot#getGilConfig()
   * @see #getDocumentRoot()
   * @generated
   */
  EReference getDocumentRoot_GilConfig();

  /**
   * Returns the meta object for class '{@link com.ibm.w3.financing.tools.gcms.gil.config.Geography <em>Geography</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Geography</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.Geography
   * @generated
   */
  EClass getGeography();

  /**
   * Returns the meta object for the attribute '{@link com.ibm.w3.financing.tools.gcms.gil.config.Geography#getGeography <em>Geography</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Geography</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.Geography#getGeography()
   * @see #getGeography()
   * @generated
   */
  EAttribute getGeography_Geography();

  /**
   * Returns the meta object for the attribute '{@link com.ibm.w3.financing.tools.gcms.gil.config.Geography#getDescription <em>Description</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Description</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.Geography#getDescription()
   * @see #getGeography()
   * @generated
   */
  EAttribute getGeography_Description();

  /**
   * Returns the meta object for the attribute list '{@link com.ibm.w3.financing.tools.gcms.gil.config.Geography#getGroup <em>Group</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute list '<em>Group</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.Geography#getGroup()
   * @see #getGeography()
   * @generated
   */
  EAttribute getGeography_Group();

  /**
   * Returns the meta object for the containment reference list '{@link com.ibm.w3.financing.tools.gcms.gil.config.Geography#getSubGeography <em>Sub Geography</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Sub Geography</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.Geography#getSubGeography()
   * @see #getGeography()
   * @generated
   */
  EReference getGeography_SubGeography();

  /**
   * Returns the meta object for class '{@link com.ibm.w3.financing.tools.gcms.gil.config.GILConfigType <em>GIL Config Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>GIL Config Type</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.GILConfigType
   * @generated
   */
  EClass getGILConfigType();

  /**
   * Returns the meta object for the containment reference list '{@link com.ibm.w3.financing.tools.gcms.gil.config.GILConfigType#getConfiguration <em>Configuration</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Configuration</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.GILConfigType#getConfiguration()
   * @see #getGILConfigType()
   * @generated
   */
  EReference getGILConfigType_Configuration();

  /**
   * Returns the meta object for the containment reference list '{@link com.ibm.w3.financing.tools.gcms.gil.config.GILConfigType#getContentManagerServerDefinition <em>Content Manager Server Definition</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Content Manager Server Definition</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.GILConfigType#getContentManagerServerDefinition()
   * @see #getGILConfigType()
   * @generated
   */
  EReference getGILConfigType_ContentManagerServerDefinition();

  /**
   * Returns the meta object for the containment reference list '{@link com.ibm.w3.financing.tools.gcms.gil.config.GILConfigType#getDB2ServerDefinitions <em>DB2 Server Definitions</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>DB2 Server Definitions</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.GILConfigType#getDB2ServerDefinitions()
   * @see #getGILConfigType()
   * @generated
   */
  EReference getGILConfigType_DB2ServerDefinitions();

  /**
   * Returns the meta object for the containment reference list '{@link com.ibm.w3.financing.tools.gcms.gil.config.GILConfigType#getServiceSuites <em>Service Suites</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Service Suites</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.GILConfigType#getServiceSuites()
   * @see #getGILConfigType()
   * @generated
   */
  EReference getGILConfigType_ServiceSuites();

  /**
   * Returns the meta object for the containment reference list '{@link com.ibm.w3.financing.tools.gcms.gil.config.GILConfigType#getGCMSWebserviceDefinitions <em>GCMS Webservice Definitions</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>GCMS Webservice Definitions</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.GILConfigType#getGCMSWebserviceDefinitions()
   * @see #getGILConfigType()
   * @generated
   */
  EReference getGILConfigType_GCMSWebserviceDefinitions();

  /**
   * Returns the meta object for the containment reference list '{@link com.ibm.w3.financing.tools.gcms.gil.config.GILConfigType#getRDCServerDefinitions <em>RDC Server Definitions</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>RDC Server Definitions</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.GILConfigType#getRDCServerDefinitions()
   * @see #getGILConfigType()
   * @generated
   */
  EReference getGILConfigType_RDCServerDefinitions();

  /**
   * Returns the meta object for the containment reference list '{@link com.ibm.w3.financing.tools.gcms.gil.config.GILConfigType#getDatabaseConnections <em>Database Connections</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Database Connections</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.GILConfigType#getDatabaseConnections()
   * @see #getGILConfigType()
   * @generated
   */
  EReference getGILConfigType_DatabaseConnections();

  /**
   * Returns the meta object for the containment reference '{@link com.ibm.w3.financing.tools.gcms.gil.config.GILConfigType#getGeographies <em>Geographies</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Geographies</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.GILConfigType#getGeographies()
   * @see #getGILConfigType()
   * @generated
   */
  EReference getGILConfigType_Geographies();

  /**
   * Returns the meta object for class '{@link com.ibm.w3.financing.tools.gcms.gil.config.NewComplexType <em>New Complex Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>New Complex Type</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.NewComplexType
   * @generated
   */
  EClass getNewComplexType();

  /**
   * Returns the meta object for class '{@link com.ibm.w3.financing.tools.gcms.gil.config.RMIServer <em>RMI Server</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>RMI Server</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.RMIServer
   * @generated
   */
  EClass getRMIServer();

  /**
   * Returns the meta object for the attribute '{@link com.ibm.w3.financing.tools.gcms.gil.config.RMIServer#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.RMIServer#getName()
   * @see #getRMIServer()
   * @generated
   */
  EAttribute getRMIServer_Name();

  /**
   * Returns the meta object for the attribute '{@link com.ibm.w3.financing.tools.gcms.gil.config.RMIServer#getURL <em>URL</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>URL</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.RMIServer#getURL()
   * @see #getRMIServer()
   * @generated
   */
  EAttribute getRMIServer_URL();

  /**
   * Returns the meta object for the containment reference '{@link com.ibm.w3.financing.tools.gcms.gil.config.RMIServer#getService <em>Service</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Service</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.RMIServer#getService()
   * @see #getRMIServer()
   * @generated
   */
  EReference getRMIServer_Service();

  /**
   * Returns the meta object for class '{@link com.ibm.w3.financing.tools.gcms.gil.config.Service <em>Service</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Service</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.Service
   * @generated
   */
  EClass getService();

  /**
   * Returns the meta object for the attribute '{@link com.ibm.w3.financing.tools.gcms.gil.config.Service#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.Service#getName()
   * @see #getService()
   * @generated
   */
  EAttribute getService_Name();

  /**
   * Returns the meta object for the attribute '{@link com.ibm.w3.financing.tools.gcms.gil.config.Service#getWebService <em>Web Service</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Web Service</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.Service#getWebService()
   * @see #getService()
   * @generated
   */
  EAttribute getService_WebService();

  /**
   * Returns the meta object for the attribute '{@link com.ibm.w3.financing.tools.gcms.gil.config.Service#getNamespace <em>Namespace</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Namespace</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.Service#getNamespace()
   * @see #getService()
   * @generated
   */
  EAttribute getService_Namespace();

  /**
   * Returns the meta object for class '{@link com.ibm.w3.financing.tools.gcms.gil.config.ServiceSuite <em>Service Suite</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Service Suite</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.ServiceSuite
   * @generated
   */
  EClass getServiceSuite();

  /**
   * Returns the meta object for the attribute '{@link com.ibm.w3.financing.tools.gcms.gil.config.ServiceSuite#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.ServiceSuite#getName()
   * @see #getServiceSuite()
   * @generated
   */
  EAttribute getServiceSuite_Name();

  /**
   * Returns the meta object for the attribute '{@link com.ibm.w3.financing.tools.gcms.gil.config.ServiceSuite#getDescription <em>Description</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Description</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.ServiceSuite#getDescription()
   * @see #getServiceSuite()
   * @generated
   */
  EAttribute getServiceSuite_Description();

  /**
   * Returns the meta object for the containment reference list '{@link com.ibm.w3.financing.tools.gcms.gil.config.ServiceSuite#getServices <em>Services</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Services</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.ServiceSuite#getServices()
   * @see #getServiceSuite()
   * @generated
   */
  EReference getServiceSuite_Services();

  /**
   * Returns the meta object for class '{@link com.ibm.w3.financing.tools.gcms.gil.config.Webserver <em>Webserver</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Webserver</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.Webserver
   * @generated
   */
  EClass getWebserver();

  /**
   * Returns the meta object for the attribute '{@link com.ibm.w3.financing.tools.gcms.gil.config.Webserver#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.Webserver#getName()
   * @see #getWebserver()
   * @generated
   */
  EAttribute getWebserver_Name();

  /**
   * Returns the meta object for the attribute '{@link com.ibm.w3.financing.tools.gcms.gil.config.Webserver#getURL <em>URL</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>URL</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.Webserver#getURL()
   * @see #getWebserver()
   * @generated
   */
  EAttribute getWebserver_URL();

  /**
   * Returns the meta object for the attribute '{@link com.ibm.w3.financing.tools.gcms.gil.config.Webserver#getServiceSuite <em>Service Suite</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Service Suite</em>'.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.Webserver#getServiceSuite()
   * @see #getWebserver()
   * @generated
   */
  EAttribute getWebserver_ServiceSuite();

  /**
   * Returns the factory that creates the instances of the model.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the factory that creates the instances of the model.
   * @generated
   */
  ConfigFactory getConfigFactory();

} //ConfigPackage
