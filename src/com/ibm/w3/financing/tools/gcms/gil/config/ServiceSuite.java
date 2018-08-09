/**
 * <copyright>
 * </copyright>
 *
 * $Id: ServiceSuite.java,v 1.1 2008/01/30 21:58:35 sbaber Exp $
 */
package com.ibm.w3.financing.tools.gcms.gil.config;

import java.util.List;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Service Suite</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.ServiceSuite#getName <em>Name</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.ServiceSuite#getDescription <em>Description</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.ServiceSuite#getServices <em>Services</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage#getServiceSuite()
 * @model 
 * @generated
 */
public interface ServiceSuite {
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
   * @see com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage#getServiceSuite_Name()
   * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link com.ibm.w3.financing.tools.gcms.gil.config.ServiceSuite#getName <em>Name</em>}' attribute.
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
   * @see com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage#getServiceSuite_Description()
   * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
   * @generated
   */
  String getDescription();

  /**
   * Sets the value of the '{@link com.ibm.w3.financing.tools.gcms.gil.config.ServiceSuite#getDescription <em>Description</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Description</em>' attribute.
   * @see #getDescription()
   * @generated
   */
  void setDescription(String value);

  /**
   * Returns the value of the '<em><b>Services</b></em>' containment reference list.
   * The list contents are of type {@link com.ibm.w3.financing.tools.gcms.gil.config.Service}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Services</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Services</em>' containment reference list.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage#getServiceSuite_Services()
   * @model type="com.ibm.w3.financing.tools.gcms.gil.config.Service" containment="true" resolveProxies="false" required="true"
   * @generated
   */
  List getServices();

} // ServiceSuite
