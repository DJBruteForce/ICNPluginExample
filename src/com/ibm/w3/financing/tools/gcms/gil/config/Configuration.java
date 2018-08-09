/**
 * <copyright>
 * </copyright>
 *
 * $Id: Configuration.java,v 1.3 2008/02/13 21:50:22 sbaber Exp $
 */
package com.ibm.w3.financing.tools.gcms.gil.config;


import java.util.List;

import commonj.sdo.Sequence;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>uration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.Configuration#getServerId <em>Server Id</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.Configuration#getName <em>Name</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.Configuration#getDescription <em>Description</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.Configuration#getGroup <em>Group</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.Configuration#getLocalizedBackendSelections <em>Localized Backend Selections</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage#getConfiguration()
 * @model 
 * @generated
 */
public interface Configuration {
  /**
   * Returns the value of the '<em><b>Server Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Server Id</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Server Id</em>' attribute.
   * @see #setServerId(String)
   * @see com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage#getConfiguration_ServerId()
   * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
   * @generated
   */
  String getServerId();

  /**
   * Sets the value of the '{@link com.ibm.w3.financing.tools.gcms.gil.config.Configuration#getServerId <em>Server Id</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Server Id</em>' attribute.
   * @see #getServerId()
   * @generated
   */
  void setServerId(String value);

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
   * @see com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage#getConfiguration_Name()
   * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link com.ibm.w3.financing.tools.gcms.gil.config.Configuration#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

  /**
   * Returns the value of the '<em><b>Description</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Description</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Description</em>' attribute.
   * @see #setDescription(String)
   * @see com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage#getConfiguration_Description()
   * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
   * @generated
   */
  String getDescription();

  /**
   * Sets the value of the '{@link com.ibm.w3.financing.tools.gcms.gil.config.Configuration#getDescription <em>Description</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Description</em>' attribute.
   * @see #getDescription()
   * @generated
   */
  void setDescription(String value);

  /**
   * Returns the value of the '<em><b>Group</b></em>' attribute list.
   * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Group</em>' attribute list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Group</em>' attribute list.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage#getConfiguration_Group()
   * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
   * @generated
   */
  Sequence getGroup();

  /**
   * Returns the value of the '<em><b>Localized Backend Selections</b></em>' containment reference list.
   * The list contents are of type {@link com.ibm.w3.financing.tools.gcms.gil.config.BackendSelection}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Localized Backend Selections</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Localized Backend Selections</em>' containment reference list.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage#getConfiguration_LocalizedBackendSelections()
   * @model type="com.ibm.w3.financing.tools.gcms.gil.config.BackendSelection" containment="true" resolveProxies="false" required="true" transient="true" volatile="true" derived="true"
   * @generated
   */
  List getLocalizedBackendSelections();

} // Configuration
