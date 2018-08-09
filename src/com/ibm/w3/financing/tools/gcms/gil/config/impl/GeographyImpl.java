/**
 * <copyright>
 * </copyright>
 *
 * $Id: GeographyImpl.java,v 1.2 2008/02/13 21:50:22 sbaber Exp $
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
import com.ibm.w3.financing.tools.gcms.gil.config.Geography;
import commonj.sdo.Sequence;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Geography</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.impl.GeographyImpl#getGeography <em>Geography</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.impl.GeographyImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.impl.GeographyImpl#getGroup <em>Group</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.impl.GeographyImpl#getSubGeography <em>Sub Geography</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class GeographyImpl extends EDataObjectImpl implements Geography
{
  /**
   * The default value of the '{@link #getGeography() <em>Geography</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getGeography()
   * @generated
   * @ordered
   */
  protected static final String GEOGRAPHY_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getGeography() <em>Geography</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getGeography()
   * @generated
   * @ordered
   */
  protected String geography = GEOGRAPHY_EDEFAULT;

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
  protected GeographyImpl()
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
    return ConfigPackage.eINSTANCE.getGeography();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getGeography()
  {
    return geography;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setGeography(String newGeography)
  {
    String oldGeography = geography;
    geography = newGeography;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.GEOGRAPHY__GEOGRAPHY, oldGeography, geography));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.GEOGRAPHY__DESCRIPTION, oldDescription, description));
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
      group = new BasicESequence(new BasicFeatureMap(this, ConfigPackage.GEOGRAPHY__GROUP));
    }
    return group;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public List getSubGeography()
  {
    return ((ESequence)getGroup()).featureMap().list(ConfigPackage.eINSTANCE.getGeography_SubGeography());
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
        case ConfigPackage.GEOGRAPHY__GROUP:
        return ((InternalEList)((ESequence)getGroup()).featureMap()).basicRemove(otherEnd, msgs);
        case ConfigPackage.GEOGRAPHY__SUB_GEOGRAPHY:
          return ((InternalEList)getSubGeography()).basicRemove(otherEnd, msgs);
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
      case ConfigPackage.GEOGRAPHY__GEOGRAPHY:
        return getGeography();
      case ConfigPackage.GEOGRAPHY__DESCRIPTION:
        return getDescription();
      case ConfigPackage.GEOGRAPHY__GROUP:
        return ((ESequence)getGroup()).featureMap();
      case ConfigPackage.GEOGRAPHY__SUB_GEOGRAPHY:
        return getSubGeography();
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
      case ConfigPackage.GEOGRAPHY__GEOGRAPHY:
        setGeography((String)newValue);
        return;
      case ConfigPackage.GEOGRAPHY__DESCRIPTION:
        setDescription((String)newValue);
        return;
      case ConfigPackage.GEOGRAPHY__GROUP:
        ((ESequence)getGroup()).featureMap().clear();
        ((ESequence)getGroup()).featureMap().addAll((Collection)newValue);
        return;
      case ConfigPackage.GEOGRAPHY__SUB_GEOGRAPHY:
        getSubGeography().clear();
        getSubGeography().addAll((Collection)newValue);
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
      case ConfigPackage.GEOGRAPHY__GEOGRAPHY:
        setGeography(GEOGRAPHY_EDEFAULT);
        return;
      case ConfigPackage.GEOGRAPHY__DESCRIPTION:
        setDescription(DESCRIPTION_EDEFAULT);
        return;
      case ConfigPackage.GEOGRAPHY__GROUP:
        ((ESequence)getGroup()).featureMap().clear();
        return;
      case ConfigPackage.GEOGRAPHY__SUB_GEOGRAPHY:
        getSubGeography().clear();
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
      case ConfigPackage.GEOGRAPHY__GEOGRAPHY:
        return GEOGRAPHY_EDEFAULT == null ? geography != null : !GEOGRAPHY_EDEFAULT.equals(geography);
      case ConfigPackage.GEOGRAPHY__DESCRIPTION:
        return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
      case ConfigPackage.GEOGRAPHY__GROUP:
        return group != null && !group.featureMap().isEmpty();
      case ConfigPackage.GEOGRAPHY__SUB_GEOGRAPHY:
        return !getSubGeography().isEmpty();
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
    result.append(" (geography: ");
    result.append(geography);
    result.append(", description: ");
    result.append(description);
    result.append(", group: ");
    result.append(group);
    result.append(')');
    return result.toString();
  }

} //GeographyImpl
