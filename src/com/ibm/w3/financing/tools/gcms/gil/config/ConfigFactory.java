/**
 * <copyright>
 * </copyright>
 *
 * $Id: ConfigFactory.java,v 1.1 2008/01/30 21:58:35 sbaber Exp $
 */
package com.ibm.w3.financing.tools.gcms.gil.config;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage
 * @generated
 */
public interface ConfigFactory extends EFactory
{
  /**
   * The singleton instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  ConfigFactory eINSTANCE = new com.ibm.w3.financing.tools.gcms.gil.config.impl.ConfigFactoryImpl();

  /**
   * Returns a new object of class '<em>Backend Selection</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Backend Selection</em>'.
   * @generated
   */
  BackendSelection createBackendSelection();

  /**
   * Returns a new object of class '<em>uration</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>uration</em>'.
   * @generated
   */
  Configuration createConfiguration();

  /**
   * Returns a new object of class '<em>Content Manager Server</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Content Manager Server</em>'.
   * @generated
   */
  ContentManagerServer createContentManagerServer();

  /**
   * Returns a new object of class '<em>Database Connection</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Database Connection</em>'.
   * @generated
   */
  DatabaseConnection createDatabaseConnection();

  /**
   * Returns a new object of class '<em>DB2 Server</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>DB2 Server</em>'.
   * @generated
   */
  DB2Server createDB2Server();

  /**
   * Returns a new object of class '<em>Definition</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Definition</em>'.
   * @generated
   */
  Definition createDefinition();

  /**
   * Returns a new object of class '<em>Document Root</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Document Root</em>'.
   * @generated
   */
  DocumentRoot createDocumentRoot();

  /**
   * Returns a new object of class '<em>Geography</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Geography</em>'.
   * @generated
   */
  Geography createGeography();

  /**
   * Returns a new object of class '<em>GIL Config Type</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>GIL Config Type</em>'.
   * @generated
   */
  GILConfigType createGILConfigType();

  /**
   * Returns a new object of class '<em>New Complex Type</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>New Complex Type</em>'.
   * @generated
   */
  NewComplexType createNewComplexType();

  /**
   * Returns a new object of class '<em>RMI Server</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>RMI Server</em>'.
   * @generated
   */
  RMIServer createRMIServer();

  /**
   * Returns a new object of class '<em>Service</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Service</em>'.
   * @generated
   */
  Service createService();

  /**
   * Returns a new object of class '<em>Service Suite</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Service Suite</em>'.
   * @generated
   */
  ServiceSuite createServiceSuite();

  /**
   * Returns a new object of class '<em>Webserver</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Webserver</em>'.
   * @generated
   */
  Webserver createWebserver();

  /**
   * Returns the package supported by this factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the package supported by this factory.
   * @generated
   */
  ConfigPackage getConfigPackage();

} //ConfigFactory
