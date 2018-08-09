/**
 * <copyright>
 * </copyright>
 *
 * $Id: DB2ServerImpl.java,v 1.2 2008/02/13 21:50:22 sbaber Exp $
 */
package com.ibm.w3.financing.tools.gcms.gil.config.impl;

import java.math.BigInteger;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.sdo.impl.EDataObjectImpl;

import com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage;
import com.ibm.w3.financing.tools.gcms.gil.config.DB2Server;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>DB2 Server</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.impl.DB2ServerImpl#getName <em>Name</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.impl.DB2ServerImpl#getInstanceNode <em>Instance Node</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.impl.DB2ServerImpl#getTargetDatabase <em>Target Database</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.impl.DB2ServerImpl#getHostname <em>Hostname</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.impl.DB2ServerImpl#getPort <em>Port</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.impl.DB2ServerImpl#getOSType <em>OS Type</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DB2ServerImpl extends EDataObjectImpl implements DB2Server
{
  /**
   * The default value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected static final String NAME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected String name = NAME_EDEFAULT;

  /**
   * The default value of the '{@link #getInstanceNode() <em>Instance Node</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getInstanceNode()
   * @generated
   * @ordered
   */
  protected static final String INSTANCE_NODE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getInstanceNode() <em>Instance Node</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getInstanceNode()
   * @generated
   * @ordered
   */
  protected String instanceNode = INSTANCE_NODE_EDEFAULT;

  /**
   * The default value of the '{@link #getTargetDatabase() <em>Target Database</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTargetDatabase()
   * @generated
   * @ordered
   */
  protected static final String TARGET_DATABASE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getTargetDatabase() <em>Target Database</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTargetDatabase()
   * @generated
   * @ordered
   */
  protected String targetDatabase = TARGET_DATABASE_EDEFAULT;

  /**
   * The default value of the '{@link #getHostname() <em>Hostname</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getHostname()
   * @generated
   * @ordered
   */
  protected static final String HOSTNAME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getHostname() <em>Hostname</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getHostname()
   * @generated
   * @ordered
   */
  protected String hostname = HOSTNAME_EDEFAULT;

  /**
   * The default value of the '{@link #getPort() <em>Port</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPort()
   * @generated
   * @ordered
   */
  protected static final BigInteger PORT_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getPort() <em>Port</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPort()
   * @generated
   * @ordered
   */
  protected BigInteger port = PORT_EDEFAULT;

  /**
   * The default value of the '{@link #getOSType() <em>OS Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOSType()
   * @generated
   * @ordered
   */
  protected static final String OSTYPE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getOSType() <em>OS Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOSType()
   * @generated
   * @ordered
   */
  protected String oSType = OSTYPE_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected DB2ServerImpl()
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
    return ConfigPackage.eINSTANCE.getDB2Server();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getName()
  {
    return name;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setName(String newName)
  {
    String oldName = name;
    name = newName;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.DB2_SERVER__NAME, oldName, name));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getInstanceNode()
  {
    return instanceNode;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setInstanceNode(String newInstanceNode)
  {
    String oldInstanceNode = instanceNode;
    instanceNode = newInstanceNode;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.DB2_SERVER__INSTANCE_NODE, oldInstanceNode, instanceNode));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getTargetDatabase()
  {
    return targetDatabase;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setTargetDatabase(String newTargetDatabase)
  {
    String oldTargetDatabase = targetDatabase;
    targetDatabase = newTargetDatabase;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.DB2_SERVER__TARGET_DATABASE, oldTargetDatabase, targetDatabase));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getHostname()
  {
    return hostname;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setHostname(String newHostname)
  {
    String oldHostname = hostname;
    hostname = newHostname;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.DB2_SERVER__HOSTNAME, oldHostname, hostname));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BigInteger getPort()
  {
    return port;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setPort(BigInteger newPort)
  {
    BigInteger oldPort = port;
    port = newPort;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.DB2_SERVER__PORT, oldPort, port));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getOSType()
  {
    return oSType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setOSType(String newOSType)
  {
    String oldOSType = oSType;
    oSType = newOSType;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.DB2_SERVER__OSTYPE, oldOSType, oSType));
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
      case ConfigPackage.DB2_SERVER__NAME:
        return getName();
      case ConfigPackage.DB2_SERVER__INSTANCE_NODE:
        return getInstanceNode();
      case ConfigPackage.DB2_SERVER__TARGET_DATABASE:
        return getTargetDatabase();
      case ConfigPackage.DB2_SERVER__HOSTNAME:
        return getHostname();
      case ConfigPackage.DB2_SERVER__PORT:
        return getPort();
      case ConfigPackage.DB2_SERVER__OSTYPE:
        return getOSType();
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
      case ConfigPackage.DB2_SERVER__NAME:
        setName((String)newValue);
        return;
      case ConfigPackage.DB2_SERVER__INSTANCE_NODE:
        setInstanceNode((String)newValue);
        return;
      case ConfigPackage.DB2_SERVER__TARGET_DATABASE:
        setTargetDatabase((String)newValue);
        return;
      case ConfigPackage.DB2_SERVER__HOSTNAME:
        setHostname((String)newValue);
        return;
      case ConfigPackage.DB2_SERVER__PORT:
        setPort((BigInteger)newValue);
        return;
      case ConfigPackage.DB2_SERVER__OSTYPE:
        setOSType((String)newValue);
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
      case ConfigPackage.DB2_SERVER__NAME:
        setName(NAME_EDEFAULT);
        return;
      case ConfigPackage.DB2_SERVER__INSTANCE_NODE:
        setInstanceNode(INSTANCE_NODE_EDEFAULT);
        return;
      case ConfigPackage.DB2_SERVER__TARGET_DATABASE:
        setTargetDatabase(TARGET_DATABASE_EDEFAULT);
        return;
      case ConfigPackage.DB2_SERVER__HOSTNAME:
        setHostname(HOSTNAME_EDEFAULT);
        return;
      case ConfigPackage.DB2_SERVER__PORT:
        setPort(PORT_EDEFAULT);
        return;
      case ConfigPackage.DB2_SERVER__OSTYPE:
        setOSType(OSTYPE_EDEFAULT);
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
      case ConfigPackage.DB2_SERVER__NAME:
        return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
      case ConfigPackage.DB2_SERVER__INSTANCE_NODE:
        return INSTANCE_NODE_EDEFAULT == null ? instanceNode != null : !INSTANCE_NODE_EDEFAULT.equals(instanceNode);
      case ConfigPackage.DB2_SERVER__TARGET_DATABASE:
        return TARGET_DATABASE_EDEFAULT == null ? targetDatabase != null : !TARGET_DATABASE_EDEFAULT.equals(targetDatabase);
      case ConfigPackage.DB2_SERVER__HOSTNAME:
        return HOSTNAME_EDEFAULT == null ? hostname != null : !HOSTNAME_EDEFAULT.equals(hostname);
      case ConfigPackage.DB2_SERVER__PORT:
        return PORT_EDEFAULT == null ? port != null : !PORT_EDEFAULT.equals(port);
      case ConfigPackage.DB2_SERVER__OSTYPE:
        return OSTYPE_EDEFAULT == null ? oSType != null : !OSTYPE_EDEFAULT.equals(oSType);
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
    result.append(" (name: ");
    result.append(name);
    result.append(", instanceNode: ");
    result.append(instanceNode);
    result.append(", targetDatabase: ");
    result.append(targetDatabase);
    result.append(", hostname: ");
    result.append(hostname);
    result.append(", port: ");
    result.append(port);
    result.append(", oSType: ");
    result.append(oSType);
    result.append(')');
    return result.toString();
  }

} //DB2ServerImpl
