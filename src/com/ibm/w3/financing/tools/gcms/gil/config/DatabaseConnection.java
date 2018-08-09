/**
 * <copyright>
 * </copyright>
 *
 * $Id: DatabaseConnection.java,v 1.1 2008/01/30 21:58:35 sbaber Exp $
 */
package com.ibm.w3.financing.tools.gcms.gil.config;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Database Connection</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.DatabaseConnection#getConnectionName <em>Connection Name</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.DatabaseConnection#getServerName <em>Server Name</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.DatabaseConnection#getHighLevelQualifier <em>High Level Qualifier</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.DatabaseConnection#getUserId <em>User Id</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage#getDatabaseConnection()
 * @model 
 * @generated
 */
public interface DatabaseConnection
{
  /**
   * Returns the value of the '<em><b>Connection Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Connection Name</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Connection Name</em>' attribute.
   * @see #setConnectionName(String)
   * @see com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage#getDatabaseConnection_ConnectionName()
   * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
   * @generated
   */
  String getConnectionName();

  /**
   * Sets the value of the '{@link com.ibm.w3.financing.tools.gcms.gil.config.DatabaseConnection#getConnectionName <em>Connection Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Connection Name</em>' attribute.
   * @see #getConnectionName()
   * @generated
   */
  void setConnectionName(String value);

  /**
   * Returns the value of the '<em><b>Server Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Server Name</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Server Name</em>' attribute.
   * @see #setServerName(String)
   * @see com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage#getDatabaseConnection_ServerName()
   * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
   * @generated
   */
  String getServerName();

  /**
   * Sets the value of the '{@link com.ibm.w3.financing.tools.gcms.gil.config.DatabaseConnection#getServerName <em>Server Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Server Name</em>' attribute.
   * @see #getServerName()
   * @generated
   */
  void setServerName(String value);

  /**
   * Returns the value of the '<em><b>High Level Qualifier</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>High Level Qualifier</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>High Level Qualifier</em>' attribute.
   * @see #setHighLevelQualifier(String)
   * @see com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage#getDatabaseConnection_HighLevelQualifier()
   * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
   * @generated
   */
  String getHighLevelQualifier();

  /**
   * Sets the value of the '{@link com.ibm.w3.financing.tools.gcms.gil.config.DatabaseConnection#getHighLevelQualifier <em>High Level Qualifier</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>High Level Qualifier</em>' attribute.
   * @see #getHighLevelQualifier()
   * @generated
   */
  void setHighLevelQualifier(String value);

  /**
   * Returns the value of the '<em><b>User Id</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>User Id</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>User Id</em>' attribute.
   * @see #setUserId(String)
   * @see com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage#getDatabaseConnection_UserId()
   * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
   * @generated
   */
  String getUserId();

  /**
   * Sets the value of the '{@link com.ibm.w3.financing.tools.gcms.gil.config.DatabaseConnection#getUserId <em>User Id</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>User Id</em>' attribute.
   * @see #getUserId()
   * @generated
   */
  void setUserId(String value);

} // DatabaseConnection
