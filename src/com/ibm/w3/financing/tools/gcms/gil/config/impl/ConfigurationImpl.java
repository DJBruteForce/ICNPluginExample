/**
 * <copyright>
 * </copyright>
 *
 * $Id: ConfigurationImpl.java,v 1.3 2008/02/13 21:50:22 sbaber Exp $
 */
package com.ibm.w3.financing.tools.gcms.gil.config.impl;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.sdo.impl.EDataObjectImpl;
import org.eclipse.emf.ecore.sdo.util.BasicESequence;
import org.eclipse.emf.ecore.sdo.util.ESequence;
import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

import com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage;
import com.ibm.w3.financing.tools.gcms.gil.config.Configuration;
import commonj.sdo.Sequence;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>uration</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.impl.ConfigurationImpl#getServerId <em>Server Id</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.impl.ConfigurationImpl#getName <em>Name</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.impl.ConfigurationImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.impl.ConfigurationImpl#getGroup <em>Group</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.impl.ConfigurationImpl#getLocalizedBackendSelections <em>Localized Backend Selections</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ConfigurationImpl extends EDataObjectImpl implements Configuration
{
  /**
   * The default value of the '{@link #getServerId() <em>Server Id</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getServerId()
   * @generated
   * @ordered
   */
  protected static final String SERVER_ID_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getServerId() <em>Server Id</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getServerId()
   * @generated
   * @ordered
   */
  protected String serverId = SERVER_ID_EDEFAULT;

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
   * The default value of the '{@link #getDescription() <em>Description</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDescription()
   * @generated
   * @ordered
   */
  protected static final String DESCRIPTION_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getDescription() <em>Description</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDescription()
   * @generated
   * @ordered
   */
  protected String description = DESCRIPTION_EDEFAULT;

  /**
   * The cached value of the '{@link #getGroup() <em>Group</em>}' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getGroup()
   * @generated
   * @ordered
   */
  protected ESequence group = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ConfigurationImpl()
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
    return ConfigPackage.eINSTANCE.getConfiguration();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getServerId()
  {
    return serverId;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setServerId(String newServerId)
  {
    String oldServerId = serverId;
    serverId = newServerId;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.CONFIG_URATION__SERVER_ID, oldServerId, serverId));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.CONFIG_URATION__NAME, oldName, name));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getDescription()
  {
    return description;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setDescription(String newDescription)
  {
    String oldDescription = description;
    description = newDescription;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.CONFIG_URATION__DESCRIPTION, oldDescription, description));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Sequence getGroup()
  {
    if (group == null)
    {
      group = new BasicESequence(new BasicFeatureMap(this, ConfigPackage.CONFIG_URATION__GROUP));
    }
    return group;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public List getLocalizedBackendSelections()
  {
    return ((ESequence)getGroup()).featureMap().list(ConfigPackage.eINSTANCE.getConfiguration_LocalizedBackendSelections());
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs)
  {
    if (featureID >= 0)
    {
      switch (eDerivedStructuralFeatureID(featureID, baseClass))
      {
        case ConfigPackage.CONFIG_URATION__GROUP:
        return ((InternalEList)((ESequence)getGroup()).featureMap()).basicRemove(otherEnd, msgs);
        case ConfigPackage.CONFIG_URATION__LOCALIZED_BACKEND_SELECTIONS:
          return ((InternalEList)getLocalizedBackendSelections()).basicRemove(otherEnd, msgs);
        default:
          return eDynamicInverseRemove(otherEnd, featureID, baseClass, msgs);
      }
    }
    return eBasicSetContainer(null, featureID, msgs);
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
      case ConfigPackage.CONFIG_URATION__SERVER_ID:
        return getServerId();
      case ConfigPackage.CONFIG_URATION__NAME:
        return getName();
      case ConfigPackage.CONFIG_URATION__DESCRIPTION:
        return getDescription();
      case ConfigPackage.CONFIG_URATION__GROUP:
        return ((ESequence)getGroup()).featureMap();
      case ConfigPackage.CONFIG_URATION__LOCALIZED_BACKEND_SELECTIONS:
        return getLocalizedBackendSelections();
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
      case ConfigPackage.CONFIG_URATION__SERVER_ID:
        setServerId((String)newValue);
        return;
      case ConfigPackage.CONFIG_URATION__NAME:
        setName((String)newValue);
        return;
      case ConfigPackage.CONFIG_URATION__DESCRIPTION:
        setDescription((String)newValue);
        return;
      case ConfigPackage.CONFIG_URATION__GROUP:
        ((ESequence)getGroup()).featureMap().clear();
        ((ESequence)getGroup()).featureMap().addAll((Collection)newValue);
        return;
      case ConfigPackage.CONFIG_URATION__LOCALIZED_BACKEND_SELECTIONS:
        getLocalizedBackendSelections().clear();
        getLocalizedBackendSelections().addAll((Collection)newValue);
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
      case ConfigPackage.CONFIG_URATION__SERVER_ID:
        setServerId(SERVER_ID_EDEFAULT);
        return;
      case ConfigPackage.CONFIG_URATION__NAME:
        setName(NAME_EDEFAULT);
        return;
      case ConfigPackage.CONFIG_URATION__DESCRIPTION:
        setDescription(DESCRIPTION_EDEFAULT);
        return;
      case ConfigPackage.CONFIG_URATION__GROUP:
        ((ESequence)getGroup()).featureMap().clear();
        return;
      case ConfigPackage.CONFIG_URATION__LOCALIZED_BACKEND_SELECTIONS:
        getLocalizedBackendSelections().clear();
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
      case ConfigPackage.CONFIG_URATION__SERVER_ID:
        return SERVER_ID_EDEFAULT == null ? serverId != null : !SERVER_ID_EDEFAULT.equals(serverId);
      case ConfigPackage.CONFIG_URATION__NAME:
        return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
      case ConfigPackage.CONFIG_URATION__DESCRIPTION:
        return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
      case ConfigPackage.CONFIG_URATION__GROUP:
        return group != null && !group.featureMap().isEmpty();
      case ConfigPackage.CONFIG_URATION__LOCALIZED_BACKEND_SELECTIONS:
        return !getLocalizedBackendSelections().isEmpty();
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
    result.append(" (serverId: ");
    result.append(serverId);
    result.append(", name: ");
    result.append(name);
    result.append(", description: ");
    result.append(description);
    result.append(", group: ");
    result.append(group);
    result.append(')');
    return result.toString();
  }

} //ConfigurationImpl
