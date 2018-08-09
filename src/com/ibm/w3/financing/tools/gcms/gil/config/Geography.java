/**
 * <copyright>
 * </copyright>
 *
 * $Id: Geography.java,v 1.2 2008/02/13 21:50:22 sbaber Exp $
 */
package com.ibm.w3.financing.tools.gcms.gil.config;

import java.util.List;

import commonj.sdo.Sequence;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Geography</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.Geography#getGeography <em>Geography</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.Geography#getDescription <em>Description</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.Geography#getGroup <em>Group</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.Geography#getSubGeography <em>Sub Geography</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage#getGeography()
 * @model 
 * @generated
 */
public interface Geography
{
  /**
   * Returns the value of the '<em><b>Geography</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Geography</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Geography</em>' attribute.
   * @see #setGeography(String)
   * @see com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage#getGeography_Geography()
   * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
   * @generated
   */
  String getGeography();

  /**
   * Sets the value of the '{@link com.ibm.w3.financing.tools.gcms.gil.config.Geography#getGeography <em>Geography</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Geography</em>' attribute.
   * @see #getGeography()
   * @generated
   */
  void setGeography(String value);

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
   * @see com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage#getGeography_Description()
   * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
   * @generated
   */
  String getDescription();

  /**
   * Sets the value of the '{@link com.ibm.w3.financing.tools.gcms.gil.config.Geography#getDescription <em>Description</em>}' attribute.
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
   * @see com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage#getGeography_Group()
   * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
   * @generated
   */
  Sequence getGroup();

  /**
   * Returns the value of the '<em><b>Sub Geography</b></em>' containment reference list.
   * The list contents are of type {@link com.ibm.w3.financing.tools.gcms.gil.config.Geography}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Sub Geography</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Sub Geography</em>' containment reference list.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage#getGeography_SubGeography()
   * @model type="com.ibm.w3.financing.tools.gcms.gil.config.Geography" containment="true" resolveProxies="false" transient="true" volatile="true" derived="true"
   * @generated
   */
  List getSubGeography();

} // Geography
