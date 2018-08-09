/**
 * <copyright>
 * </copyright>
 *
 * $Id: ConfigFactoryImpl.java,v 1.2 2008/02/13 21:50:22 sbaber Exp $
 */
package com.ibm.w3.financing.tools.gcms.gil.config.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.EFactoryImpl;

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
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ConfigFactoryImpl extends EFactoryImpl implements ConfigFactory
{
  /**
   * Creates and instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ConfigFactoryImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EObject create(EClass eClass)
  {
    switch (eClass.getClassifierID())
    {
      case ConfigPackage.BACKEND_SELECTION: return (EObject)createBackendSelection();
      case ConfigPackage.CONFIG_URATION: return (EObject)createConfiguration();
      case ConfigPackage.CONTENT_MANAGER_SERVER: return (EObject)createContentManagerServer();
      case ConfigPackage.DATABASE_CONNECTION: return (EObject)createDatabaseConnection();
      case ConfigPackage.DB2_SERVER: return (EObject)createDB2Server();
      case ConfigPackage.DEFINITION: return (EObject)createDefinition();
      case ConfigPackage.DOCUMENT_ROOT: return (EObject)createDocumentRoot();
      case ConfigPackage.GEOGRAPHY: return (EObject)createGeography();
      case ConfigPackage.GIL_CONFIG_TYPE: return (EObject)createGILConfigType();
      case ConfigPackage.NEW_COMPLEX_TYPE: return (EObject)createNewComplexType();
      case ConfigPackage.RMI_SERVER: return (EObject)createRMIServer();
      case ConfigPackage.SERVICE: return (EObject)createService();
      case ConfigPackage.SERVICE_SUITE: return (EObject)createServiceSuite();
      case ConfigPackage.WEBSERVER: return (EObject)createWebserver();
      default:
        throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BackendSelection createBackendSelection()
  {
    BackendSelectionImpl backendSelection = new BackendSelectionImpl();
    return backendSelection;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Configuration createConfiguration()
  {
    ConfigurationImpl configuration = new ConfigurationImpl();
    return configuration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ContentManagerServer createContentManagerServer()
  {
    ContentManagerServerImpl contentManagerServer = new ContentManagerServerImpl();
    return contentManagerServer;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public DatabaseConnection createDatabaseConnection()
  {
    DatabaseConnectionImpl databaseConnection = new DatabaseConnectionImpl();
    return databaseConnection;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public DB2Server createDB2Server()
  {
    DB2ServerImpl dB2Server = new DB2ServerImpl();
    return dB2Server;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Definition createDefinition()
  {
    DefinitionImpl definition = new DefinitionImpl();
    return definition;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public DocumentRoot createDocumentRoot()
  {
    DocumentRootImpl documentRoot = new DocumentRootImpl();
    return documentRoot;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Geography createGeography()
  {
    GeographyImpl geography = new GeographyImpl();
    return geography;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public GILConfigType createGILConfigType()
  {
    GILConfigTypeImpl gilConfigType = new GILConfigTypeImpl();
    return gilConfigType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NewComplexType createNewComplexType()
  {
    NewComplexTypeImpl newComplexType = new NewComplexTypeImpl();
    return newComplexType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public RMIServer createRMIServer()
  {
    RMIServerImpl rmiServer = new RMIServerImpl();
    return rmiServer;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Service createService()
  {
    ServiceImpl service = new ServiceImpl();
    return service;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ServiceSuite createServiceSuite()
  {
    ServiceSuiteImpl serviceSuite = new ServiceSuiteImpl();
    return serviceSuite;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Webserver createWebserver()
  {
    WebserverImpl webserver = new WebserverImpl();
    return webserver;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ConfigPackage getConfigPackage()
  {
    return (ConfigPackage)getEPackage();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @deprecated
   * @generated
   */
  public static ConfigPackage getPackage()
  {
    return ConfigPackage.eINSTANCE;
  }

} //ConfigFactoryImpl
