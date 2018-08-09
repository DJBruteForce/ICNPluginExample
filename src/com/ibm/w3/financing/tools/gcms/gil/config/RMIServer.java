/**
 * <copyright>
 * </copyright>
 *
 * $Id: RMIServer.java,v 1.1 2008/01/30 21:58:35 sbaber Exp $
 */
package com.ibm.w3.financing.tools.gcms.gil.config;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>RMI Server</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.RMIServer#getName <em>Name</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.RMIServer#getURL <em>URL</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.RMIServer#getService <em>Service</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage#getRMIServer()
 * @model 
 * @generated
 */
public interface RMIServer
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
   * @see com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage#getRMIServer_Name()
   * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link com.ibm.w3.financing.tools.gcms.gil.config.RMIServer#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

  /**
   * Returns the value of the '<em><b>URL</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>URL</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>URL</em>' attribute.
   * @see #setURL(String)
   * @see com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage#getRMIServer_URL()
   * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
   * @generated
   */
  String getURL();

  /**
   * Sets the value of the '{@link com.ibm.w3.financing.tools.gcms.gil.config.RMIServer#getURL <em>URL</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>URL</em>' attribute.
   * @see #getURL()
   * @generated
   */
  void setURL(String value);

  /**
   * Returns the value of the '<em><b>Service</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Service</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Service</em>' containment reference.
   * @see #setService(Service)
   * @see com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage#getRMIServer_Service()
   * @model containment="true" resolveProxies="false" required="true"
   * @generated
   */
  Service getService();

  /**
   * Sets the value of the '{@link com.ibm.w3.financing.tools.gcms.gil.config.RMIServer#getService <em>Service</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Service</em>' containment reference.
   * @see #getService()
   * @generated
   */
  void setService(Service value);

} // RMIServer
