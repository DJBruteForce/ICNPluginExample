/**
 * <copyright>
 * </copyright>
 *
 * $Id: DB2Server.java,v 1.1 2008/01/30 21:58:35 sbaber Exp $
 */
package com.ibm.w3.financing.tools.gcms.gil.config;

import java.math.BigInteger;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>DB2 Server</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.DB2Server#getName <em>Name</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.DB2Server#getInstanceNode <em>Instance Node</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.DB2Server#getTargetDatabase <em>Target Database</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.DB2Server#getHostname <em>Hostname</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.DB2Server#getPort <em>Port</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.DB2Server#getOSType <em>OS Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage#getDB2Server()
 * @model 
 * @generated
 */
public interface DB2Server
{
  /**
   * Returns the value of the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Name</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Name</em>' attribute.
   * @see #setName(String)
   * @see com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage#getDB2Server_Name()
   * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link com.ibm.w3.financing.tools.gcms.gil.config.DB2Server#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

  /**
   * Returns the value of the '<em><b>Instance Node</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Instance Node</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Instance Node</em>' attribute.
   * @see #setInstanceNode(String)
   * @see com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage#getDB2Server_InstanceNode()
   * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
   * @generated
   */
  String getInstanceNode();

  /**
   * Sets the value of the '{@link com.ibm.w3.financing.tools.gcms.gil.config.DB2Server#getInstanceNode <em>Instance Node</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Instance Node</em>' attribute.
   * @see #getInstanceNode()
   * @generated
   */
  void setInstanceNode(String value);

  /**
   * Returns the value of the '<em><b>Target Database</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Target Database</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Target Database</em>' attribute.
   * @see #setTargetDatabase(String)
   * @see com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage#getDB2Server_TargetDatabase()
   * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
   * @generated
   */
  String getTargetDatabase();

  /**
   * Sets the value of the '{@link com.ibm.w3.financing.tools.gcms.gil.config.DB2Server#getTargetDatabase <em>Target Database</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Target Database</em>' attribute.
   * @see #getTargetDatabase()
   * @generated
   */
  void setTargetDatabase(String value);

  /**
   * Returns the value of the '<em><b>Hostname</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Hostname</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Hostname</em>' attribute.
   * @see #setHostname(String)
   * @see com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage#getDB2Server_Hostname()
   * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
   * @generated
   */
  String getHostname();

  /**
   * Sets the value of the '{@link com.ibm.w3.financing.tools.gcms.gil.config.DB2Server#getHostname <em>Hostname</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Hostname</em>' attribute.
   * @see #getHostname()
   * @generated
   */
  void setHostname(String value);

  /**
   * Returns the value of the '<em><b>Port</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Port</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Port</em>' attribute.
   * @see #setPort(BigInteger)
   * @see com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage#getDB2Server_Port()
   * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.Integer" required="true"
   * @generated
   */
  BigInteger getPort();

  /**
   * Sets the value of the '{@link com.ibm.w3.financing.tools.gcms.gil.config.DB2Server#getPort <em>Port</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Port</em>' attribute.
   * @see #getPort()
   * @generated
   */
  void setPort(BigInteger value);

  /**
   * Returns the value of the '<em><b>OS Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>OS Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>OS Type</em>' attribute.
   * @see #setOSType(String)
   * @see com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage#getDB2Server_OSType()
   * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
   * @generated
   */
  String getOSType();

  /**
   * Sets the value of the '{@link com.ibm.w3.financing.tools.gcms.gil.config.DB2Server#getOSType <em>OS Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>OS Type</em>' attribute.
   * @see #getOSType()
   * @generated
   */
  void setOSType(String value);

} // DB2Server
