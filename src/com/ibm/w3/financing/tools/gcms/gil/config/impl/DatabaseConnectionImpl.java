/**
 * <copyright>
 * </copyright>
 *
 * $Id: DatabaseConnectionImpl.java,v 1.2 2008/02/13 21:50:22 sbaber Exp $
 */
package com.ibm.w3.financing.tools.gcms.gil.config.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.sdo.impl.EDataObjectImpl;

import com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage;
import com.ibm.w3.financing.tools.gcms.gil.config.DatabaseConnection;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Database Connection</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.impl.DatabaseConnectionImpl#getConnectionName <em>Connection Name</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.impl.DatabaseConnectionImpl#getServerName <em>Server Name</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.impl.DatabaseConnectionImpl#getHighLevelQualifier <em>High Level Qualifier</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.impl.DatabaseConnectionImpl#getUserId <em>User Id</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DatabaseConnectionImpl extends EDataObjectImpl implements DatabaseConnection
{
  /**
   * The default value of the '{@link #getConnectionName() <em>Connection Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getConnectionName()
   * @generated
   * @ordered
   */
  protected static final String CONNECTION_NAME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getConnectionName() <em>Connection Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getConnectionName()
   * @generated
   * @ordered
   */
  protected String connectionName = CONNECTION_NAME_EDEFAULT;

  /**
   * The default value of the '{@link #getServerName() <em>Server Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getServerName()
   * @generated
   * @ordered
   */
  protected static final String SERVER_NAME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getServerName() <em>Server Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getServerName()
   * @generated
   * @ordered
   */
  protected String serverName = SERVER_NAME_EDEFAULT;

  /**
   * The default value of the '{@link #getHighLevelQualifier() <em>High Level Qualifier</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getHighLevelQualifier()
   * @generated
   * @ordered
   */
  protected static final String HIGH_LEVEL_QUALIFIER_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getHighLevelQualifier() <em>High Level Qualifier</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getHighLevelQualifier()
   * @generated
   * @ordered
   */
  protected String highLevelQualifier = HIGH_LEVEL_QUALIFIER_EDEFAULT;

  /**
   * The default value of the '{@link #getUserId() <em>User Id</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getUserId()
   * @generated
   * @ordered
   */
  protected static final String USER_ID_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getUserId() <em>User Id</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getUserId()
   * @generated
   * @ordered
   */
  protected String userId = USER_ID_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected DatabaseConnectionImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected EClass eStaticClass()
  {
    return ConfigPackage.eINSTANCE.getDatabaseConnection();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getConnectionName()
  {
    return connectionName;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setConnectionName(String newConnectionName)
  {
    String oldConnectionName = connectionName;
    connectionName = newConnectionName;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.DATABASE_CONNECTION__CONNECTION_NAME, oldConnectionName, connectionName));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getServerName()
  {
    return serverName;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setServerName(String newServerName)
  {
    String oldServerName = serverName;
    serverName = newServerName;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.DATABASE_CONNECTION__SERVER_NAME, oldServerName, serverName));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getHighLevelQualifier()
  {
    return highLevelQualifier;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setHighLevelQualifier(String newHighLevelQualifier)
  {
    String oldHighLevelQualifier = highLevelQualifier;
    highLevelQualifier = newHighLevelQualifier;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.DATABASE_CONNECTION__HIGH_LEVEL_QUALIFIER, oldHighLevelQualifier, highLevelQualifier));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getUserId()
  {
    return userId;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setUserId(String newUserId)
  {
    String oldUserId = userId;
    userId = newUserId;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.DATABASE_CONNECTION__USER_ID, oldUserId, userId));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Object eGet(EStructuralFeature eFeature, boolean resolve)
  {
    switch (eDerivedStructuralFeatureID(eFeature))
    {
      case ConfigPackage.DATABASE_CONNECTION__CONNECTION_NAME:
        return getConnectionName();
      case ConfigPackage.DATABASE_CONNECTION__SERVER_NAME:
        return getServerName();
      case ConfigPackage.DATABASE_CONNECTION__HIGH_LEVEL_QUALIFIER:
        return getHighLevelQualifier();
      case ConfigPackage.DATABASE_CONNECTION__USER_ID:
        return getUserId();
    }
    return eDynamicGet(eFeature, resolve);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void eSet(EStructuralFeature eFeature, Object newValue)
  {
    switch (eDerivedStructuralFeatureID(eFeature))
    {
      case ConfigPackage.DATABASE_CONNECTION__CONNECTION_NAME:
        setConnectionName((String)newValue);
        return;
      case ConfigPackage.DATABASE_CONNECTION__SERVER_NAME:
        setServerName((String)newValue);
        return;
      case ConfigPackage.DATABASE_CONNECTION__HIGH_LEVEL_QUALIFIER:
        setHighLevelQualifier((String)newValue);
        return;
      case ConfigPackage.DATABASE_CONNECTION__USER_ID:
        setUserId((String)newValue);
        return;
    }
    eDynamicSet(eFeature, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void eUnset(EStructuralFeature eFeature)
  {
    switch (eDerivedStructuralFeatureID(eFeature))
    {
      case ConfigPackage.DATABASE_CONNECTION__CONNECTION_NAME:
        setConnectionName(CONNECTION_NAME_EDEFAULT);
        return;
      case ConfigPackage.DATABASE_CONNECTION__SERVER_NAME:
        setServerName(SERVER_NAME_EDEFAULT);
        return;
      case ConfigPackage.DATABASE_CONNECTION__HIGH_LEVEL_QUALIFIER:
        setHighLevelQualifier(HIGH_LEVEL_QUALIFIER_EDEFAULT);
        return;
      case ConfigPackage.DATABASE_CONNECTION__USER_ID:
        setUserId(USER_ID_EDEFAULT);
        return;
    }
    eDynamicUnset(eFeature);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean eIsSet(EStructuralFeature eFeature)
  {
    switch (eDerivedStructuralFeatureID(eFeature))
    {
      case ConfigPackage.DATABASE_CONNECTION__CONNECTION_NAME:
        return CONNECTION_NAME_EDEFAULT == null ? connectionName != null : !CONNECTION_NAME_EDEFAULT.equals(connectionName);
      case ConfigPackage.DATABASE_CONNECTION__SERVER_NAME:
        return SERVER_NAME_EDEFAULT == null ? serverName != null : !SERVER_NAME_EDEFAULT.equals(serverName);
      case ConfigPackage.DATABASE_CONNECTION__HIGH_LEVEL_QUALIFIER:
        return HIGH_LEVEL_QUALIFIER_EDEFAULT == null ? highLevelQualifier != null : !HIGH_LEVEL_QUALIFIER_EDEFAULT.equals(highLevelQualifier);
      case ConfigPackage.DATABASE_CONNECTION__USER_ID:
        return USER_ID_EDEFAULT == null ? userId != null : !USER_ID_EDEFAULT.equals(userId);
    }
    return eDynamicIsSet(eFeature);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String toString()
  {
    if (eIsProxy()) return super.toString();

    StringBuffer result = new StringBuffer(super.toString());
    result.append(" (connectionName: ");
    result.append(connectionName);
    result.append(", serverName: ");
    result.append(serverName);
    result.append(", highLevelQualifier: ");
    result.append(highLevelQualifier);
    result.append(", userId: ");
    result.append(userId);
    result.append(')');
    return result.toString();
  }

} //DatabaseConnectionImpl
