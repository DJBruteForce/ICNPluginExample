/**
 * <copyright>
 * </copyright>
 *
 * $Id: GILConfigType.java,v 1.2 2008/02/11 14:31:31 sbaber Exp $
 */
package com.ibm.w3.financing.tools.gcms.gil.config;

import java.util.List;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>GIL Config Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.GILConfigType#getConfiguration <em>Configuration</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.GILConfigType#getContentManagerServerDefinition <em>Content Manager Server Definition</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.GILConfigType#getDB2ServerDefinitions <em>DB2 Server Definitions</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.GILConfigType#getServiceSuites <em>Service Suites</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.GILConfigType#getGCMSWebserviceDefinitions <em>GCMS Webservice Definitions</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.GILConfigType#getRDCServerDefinitions <em>RDC Server Definitions</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.GILConfigType#getDatabaseConnections <em>Database Connections</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.GILConfigType#getGeographies <em>Geographies</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage#getGILConfigType()
 * @model 
 * @generated
 */
public interface GILConfigType {
  /**
   * Returns the value of the '<em><b>Configuration</b></em>' containment reference list.
   * The list contents are of type {@link com.ibm.w3.financing.tools.gcms.gil.config.Configuration}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Configuration</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Configuration</em>' containment reference list.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage#getGILConfigType_Configuration()
   * @model type="com.ibm.w3.financing.tools.gcms.gil.config.Configuration" containment="true" resolveProxies="false" required="true"
   * @generated
   */
  List getConfiguration();

  /**
   * Returns the value of the '<em><b>Content Manager Server Definition</b></em>' containment reference list.
   * The list contents are of type {@link com.ibm.w3.financing.tools.gcms.gil.config.ContentManagerServer}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Content Manager Server Definition</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Content Manager Server Definition</em>' containment reference list.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage#getGILConfigType_ContentManagerServerDefinition()
   * @model type="com.ibm.w3.financing.tools.gcms.gil.config.ContentManagerServer" containment="true" resolveProxies="false" required="true"
   * @generated
   */
  List getContentManagerServerDefinition();

  /**
   * Returns the value of the '<em><b>DB2 Server Definitions</b></em>' containment reference list.
   * The list contents are of type {@link com.ibm.w3.financing.tools.gcms.gil.config.DB2Server}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>DB2 Server Definitions</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>DB2 Server Definitions</em>' containment reference list.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage#getGILConfigType_DB2ServerDefinitions()
   * @model type="com.ibm.w3.financing.tools.gcms.gil.config.DB2Server" containment="true" resolveProxies="false" required="true"
   * @generated
   */
  List getDB2ServerDefinitions();

  /**
   * Returns the value of the '<em><b>Service Suites</b></em>' containment reference list.
   * The list contents are of type {@link com.ibm.w3.financing.tools.gcms.gil.config.ServiceSuite}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Service Suites</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Service Suites</em>' containment reference list.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage#getGILConfigType_ServiceSuites()
   * @model type="com.ibm.w3.financing.tools.gcms.gil.config.ServiceSuite" containment="true" resolveProxies="false" required="true"
   * @generated
   */
  List getServiceSuites();

  /**
   * Returns the value of the '<em><b>GCMS Webservice Definitions</b></em>' containment reference list.
   * The list contents are of type {@link com.ibm.w3.financing.tools.gcms.gil.config.Webserver}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>GCMS Webservice Definitions</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>GCMS Webservice Definitions</em>' containment reference list.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage#getGILConfigType_GCMSWebserviceDefinitions()
   * @model type="com.ibm.w3.financing.tools.gcms.gil.config.Webserver" containment="true" resolveProxies="false" required="true"
   * @generated
   */
  List getGCMSWebserviceDefinitions();

  /**
   * Returns the value of the '<em><b>RDC Server Definitions</b></em>' containment reference list.
   * The list contents are of type {@link com.ibm.w3.financing.tools.gcms.gil.config.Webserver}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>RDC Server Definitions</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>RDC Server Definitions</em>' containment reference list.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage#getGILConfigType_RDCServerDefinitions()
   * @model type="com.ibm.w3.financing.tools.gcms.gil.config.Webserver" containment="true" resolveProxies="false" required="true"
   * @generated
   */
  List getRDCServerDefinitions();

  /**
   * Returns the value of the '<em><b>Database Connections</b></em>' containment reference list.
   * The list contents are of type {@link com.ibm.w3.financing.tools.gcms.gil.config.DatabaseConnection}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Database Connections</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Database Connections</em>' containment reference list.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage#getGILConfigType_DatabaseConnections()
   * @model type="com.ibm.w3.financing.tools.gcms.gil.config.DatabaseConnection" containment="true" resolveProxies="false" required="true"
   * @generated
   */
  List getDatabaseConnections();

  /**
   * Returns the value of the '<em><b>Geographies</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Geographies</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Geographies</em>' containment reference.
   * @see #setGeographies(Geography)
   * @see com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage#getGILConfigType_Geographies()
   * @model containment="true" resolveProxies="false" required="true"
   * @generated
   */
  Geography getGeographies();

  /**
   * Sets the value of the '{@link com.ibm.w3.financing.tools.gcms.gil.config.GILConfigType#getGeographies <em>Geographies</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Geographies</em>' containment reference.
   * @see #getGeographies()
   * @generated
   */
  void setGeographies(Geography value);

} // GILConfigType
