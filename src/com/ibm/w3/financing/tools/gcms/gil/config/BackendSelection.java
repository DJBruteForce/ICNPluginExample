/**
 * <copyright>
 * </copyright>
 *
 * $Id: BackendSelection.java,v 1.2 2008/02/11 14:31:31 sbaber Exp $
 */
package com.ibm.w3.financing.tools.gcms.gil.config;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Backend Selection</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.BackendSelection#getGeography <em>Geography</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.BackendSelection#getGlobalAPTS <em>Global APTS</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.BackendSelection#getGlobalGCMS <em>Global GCMS</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.BackendSelection#getProductReference <em>Product Reference</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.BackendSelection#getRDC <em>RDC</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.BackendSelection#getRMI <em>RMI</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.BackendSelection#getGCMS <em>GCMS</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage#getBackendSelection()
 * @model 
 * @generated
 */
public interface BackendSelection {
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
   * @see com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage#getBackendSelection_Geography()
   * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
   * @generated
   */
  String getGeography();

  /**
   * Sets the value of the '{@link com.ibm.w3.financing.tools.gcms.gil.config.BackendSelection#getGeography <em>Geography</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Geography</em>' attribute.
   * @see #getGeography()
   * @generated
   */
  void setGeography(String value);

  /**
   * Returns the value of the '<em><b>Global APTS</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Global APTS</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Global APTS</em>' attribute.
   * @see #setGlobalAPTS(String)
   * @see com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage#getBackendSelection_GlobalAPTS()
   * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
   * @generated
   */
  String getGlobalAPTS();

  /**
   * Sets the value of the '{@link com.ibm.w3.financing.tools.gcms.gil.config.BackendSelection#getGlobalAPTS <em>Global APTS</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Global APTS</em>' attribute.
   * @see #getGlobalAPTS()
   * @generated
   */
  void setGlobalAPTS(String value);

  /**
   * Returns the value of the '<em><b>Global GCMS</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Global GCMS</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Global GCMS</em>' attribute.
   * @see #setGlobalGCMS(String)
   * @see com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage#getBackendSelection_GlobalGCMS()
   * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
   * @generated
   */
  String getGlobalGCMS();

  /**
   * Sets the value of the '{@link com.ibm.w3.financing.tools.gcms.gil.config.BackendSelection#getGlobalGCMS <em>Global GCMS</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Global GCMS</em>' attribute.
   * @see #getGlobalGCMS()
   * @generated
   */
  void setGlobalGCMS(String value);

  /**
   * Returns the value of the '<em><b>Product Reference</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Product Reference</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Product Reference</em>' attribute.
   * @see #setProductReference(String)
   * @see com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage#getBackendSelection_ProductReference()
   * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
   * @generated
   */
  String getProductReference();

  /**
   * Sets the value of the '{@link com.ibm.w3.financing.tools.gcms.gil.config.BackendSelection#getProductReference <em>Product Reference</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Product Reference</em>' attribute.
   * @see #getProductReference()
   * @generated
   */
  void setProductReference(String value);

  /**
   * Returns the value of the '<em><b>RDC</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>RDC</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>RDC</em>' attribute.
   * @see #setRDC(String)
   * @see com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage#getBackendSelection_RDC()
   * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
   * @generated
   */
  String getRDC();

  /**
   * Sets the value of the '{@link com.ibm.w3.financing.tools.gcms.gil.config.BackendSelection#getRDC <em>RDC</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>RDC</em>' attribute.
   * @see #getRDC()
   * @generated
   */
  void setRDC(String value);

  /**
   * Returns the value of the '<em><b>RMI</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>RMI</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>RMI</em>' attribute.
   * @see #setRMI(String)
   * @see com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage#getBackendSelection_RMI()
   * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
   * @generated
   */
  String getRMI();

  /**
   * Sets the value of the '{@link com.ibm.w3.financing.tools.gcms.gil.config.BackendSelection#getRMI <em>RMI</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>RMI</em>' attribute.
   * @see #getRMI()
   * @generated
   */
  void setRMI(String value);

  /**
   * Returns the value of the '<em><b>GCMS</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>GCMS</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>GCMS</em>' attribute.
   * @see #setGCMS(String)
   * @see com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage#getBackendSelection_GCMS()
   * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
   * @generated
   */
  String getGCMS();

  /**
   * Sets the value of the '{@link com.ibm.w3.financing.tools.gcms.gil.config.BackendSelection#getGCMS <em>GCMS</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>GCMS</em>' attribute.
   * @see #getGCMS()
   * @generated
   */
  void setGCMS(String value);

} // BackendSelection
