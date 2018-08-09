/**
 * <copyright>
 * </copyright>
 *
 * $Id: Definition.java,v 1.1 2008/01/30 21:58:35 sbaber Exp $
 */
package com.ibm.w3.financing.tools.gcms.gil.config;

import java.util.List;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Definition</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.Definition#getContentManagerServerDefinition <em>Content Manager Server Definition</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.Definition#getDB2ServerDefinition <em>DB2 Server Definition</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.Definition#getGCMSWebserverDefinition <em>GCMS Webserver Definition</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.Definition#getRMIServerDefinition <em>RMI Server Definition</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.Definition#getRDCServerDefinition <em>RDC Server Definition</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage#getDefinition()
 * @model 
 * @generated
 */
public interface Definition
{
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
   * @see com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage#getDefinition_ContentManagerServerDefinition()
   * @model type="com.ibm.w3.financing.tools.gcms.gil.config.ContentManagerServer" containment="true" resolveProxies="false" required="true"
   * @generated
   */
  List getContentManagerServerDefinition();

  /**
   * Returns the value of the '<em><b>DB2 Server Definition</b></em>' containment reference list.
   * The list contents are of type {@link com.ibm.w3.financing.tools.gcms.gil.config.DB2Server}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>DB2 Server Definition</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>DB2 Server Definition</em>' containment reference list.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage#getDefinition_DB2ServerDefinition()
   * @model type="com.ibm.w3.financing.tools.gcms.gil.config.DB2Server" containment="true" resolveProxies="false" required="true"
   * @generated
   */
  List getDB2ServerDefinition();

  /**
   * Returns the value of the '<em><b>GCMS Webserver Definition</b></em>' containment reference list.
   * The list contents are of type {@link com.ibm.w3.financing.tools.gcms.gil.config.Webserver}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>GCMS Webserver Definition</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>GCMS Webserver Definition</em>' containment reference list.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage#getDefinition_GCMSWebserverDefinition()
   * @model type="com.ibm.w3.financing.tools.gcms.gil.config.Webserver" containment="true" resolveProxies="false" required="true"
   * @generated
   */
  List getGCMSWebserverDefinition();

  /**
   * Returns the value of the '<em><b>RMI Server Definition</b></em>' containment reference list.
   * The list contents are of type {@link com.ibm.w3.financing.tools.gcms.gil.config.RMIServer}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>RMI Server Definition</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>RMI Server Definition</em>' containment reference list.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage#getDefinition_RMIServerDefinition()
   * @model type="com.ibm.w3.financing.tools.gcms.gil.config.RMIServer" containment="true" resolveProxies="false" required="true"
   * @generated
   */
  List getRMIServerDefinition();

  /**
   * Returns the value of the '<em><b>RDC Server Definition</b></em>' attribute list.
   * The list contents are of type {@link java.lang.String}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>RDC Server Definition</em>' attribute list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>RDC Server Definition</em>' attribute list.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage#getDefinition_RDCServerDefinition()
   * @model type="java.lang.String" unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
   * @generated
   */
  List getRDCServerDefinition();

} // Definition
