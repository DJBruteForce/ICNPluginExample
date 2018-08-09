/**
 * <copyright>
 * </copyright>
 *
 * $Id: DefinitionImpl.java,v 1.2 2008/02/13 21:50:22 sbaber Exp $
 */
package com.ibm.w3.financing.tools.gcms.gil.config.impl;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.sdo.impl.EDataObjectImpl;
import org.eclipse.emf.ecore.util.EDataTypeEList;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage;
import com.ibm.w3.financing.tools.gcms.gil.config.ContentManagerServer;
import com.ibm.w3.financing.tools.gcms.gil.config.DB2Server;
import com.ibm.w3.financing.tools.gcms.gil.config.Definition;
import com.ibm.w3.financing.tools.gcms.gil.config.RMIServer;
import com.ibm.w3.financing.tools.gcms.gil.config.Webserver;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Definition</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.impl.DefinitionImpl#getContentManagerServerDefinition <em>Content Manager Server Definition</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.impl.DefinitionImpl#getDB2ServerDefinition <em>DB2 Server Definition</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.impl.DefinitionImpl#getGCMSWebserverDefinition <em>GCMS Webserver Definition</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.impl.DefinitionImpl#getRMIServerDefinition <em>RMI Server Definition</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.impl.DefinitionImpl#getRDCServerDefinition <em>RDC Server Definition</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DefinitionImpl extends EDataObjectImpl implements Definition
{
  /**
   * The cached value of the '{@link #getContentManagerServerDefinition() <em>Content Manager Server Definition</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getContentManagerServerDefinition()
   * @generated
   * @ordered
   */
  protected EList contentManagerServerDefinition = null;

  /**
   * The cached value of the '{@link #getDB2ServerDefinition() <em>DB2 Server Definition</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDB2ServerDefinition()
   * @generated
   * @ordered
   */
  protected EList dB2ServerDefinition = null;

  /**
   * The cached value of the '{@link #getGCMSWebserverDefinition() <em>GCMS Webserver Definition</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getGCMSWebserverDefinition()
   * @generated
   * @ordered
   */
  protected EList gCMSWebserverDefinition = null;

  /**
   * The cached value of the '{@link #getRMIServerDefinition() <em>RMI Server Definition</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRMIServerDefinition()
   * @generated
   * @ordered
   */
  protected EList rMIServerDefinition = null;

  /**
   * The cached value of the '{@link #getRDCServerDefinition() <em>RDC Server Definition</em>}' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRDCServerDefinition()
   * @generated
   * @ordered
   */
  protected EList rDCServerDefinition = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected DefinitionImpl()
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
    return ConfigPackage.eINSTANCE.getDefinition();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public List getContentManagerServerDefinition()
  {
    if (contentManagerServerDefinition == null)
    {
      contentManagerServerDefinition = new EObjectContainmentEList(ContentManagerServer.class, this, ConfigPackage.DEFINITION__CONTENT_MANAGER_SERVER_DEFINITION);
    }
    return contentManagerServerDefinition;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public List getDB2ServerDefinition()
  {
    if (dB2ServerDefinition == null)
    {
      dB2ServerDefinition = new EObjectContainmentEList(DB2Server.class, this, ConfigPackage.DEFINITION__DB2_SERVER_DEFINITION);
    }
    return dB2ServerDefinition;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public List getGCMSWebserverDefinition()
  {
    if (gCMSWebserverDefinition == null)
    {
      gCMSWebserverDefinition = new EObjectContainmentEList(Webserver.class, this, ConfigPackage.DEFINITION__GCMS_WEBSERVER_DEFINITION);
    }
    return gCMSWebserverDefinition;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public List getRMIServerDefinition()
  {
    if (rMIServerDefinition == null)
    {
      rMIServerDefinition = new EObjectContainmentEList(RMIServer.class, this, ConfigPackage.DEFINITION__RMI_SERVER_DEFINITION);
    }
    return rMIServerDefinition;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public List getRDCServerDefinition()
  {
    if (rDCServerDefinition == null)
    {
      rDCServerDefinition = new EDataTypeEList(String.class, this, ConfigPackage.DEFINITION__RDC_SERVER_DEFINITION);
    }
    return rDCServerDefinition;
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
        case ConfigPackage.DEFINITION__CONTENT_MANAGER_SERVER_DEFINITION:
          return ((InternalEList)getContentManagerServerDefinition()).basicRemove(otherEnd, msgs);
        case ConfigPackage.DEFINITION__DB2_SERVER_DEFINITION:
          return ((InternalEList)getDB2ServerDefinition()).basicRemove(otherEnd, msgs);
        case ConfigPackage.DEFINITION__GCMS_WEBSERVER_DEFINITION:
          return ((InternalEList)getGCMSWebserverDefinition()).basicRemove(otherEnd, msgs);
        case ConfigPackage.DEFINITION__RMI_SERVER_DEFINITION:
          return ((InternalEList)getRMIServerDefinition()).basicRemove(otherEnd, msgs);
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
      case ConfigPackage.DEFINITION__CONTENT_MANAGER_SERVER_DEFINITION:
        return getContentManagerServerDefinition();
      case ConfigPackage.DEFINITION__DB2_SERVER_DEFINITION:
        return getDB2ServerDefinition();
      case ConfigPackage.DEFINITION__GCMS_WEBSERVER_DEFINITION:
        return getGCMSWebserverDefinition();
      case ConfigPackage.DEFINITION__RMI_SERVER_DEFINITION:
        return getRMIServerDefinition();
      case ConfigPackage.DEFINITION__RDC_SERVER_DEFINITION:
        return getRDCServerDefinition();
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
      case ConfigPackage.DEFINITION__CONTENT_MANAGER_SERVER_DEFINITION:
        getContentManagerServerDefinition().clear();
        getContentManagerServerDefinition().addAll((Collection)newValue);
        return;
      case ConfigPackage.DEFINITION__DB2_SERVER_DEFINITION:
        getDB2ServerDefinition().clear();
        getDB2ServerDefinition().addAll((Collection)newValue);
        return;
      case ConfigPackage.DEFINITION__GCMS_WEBSERVER_DEFINITION:
        getGCMSWebserverDefinition().clear();
        getGCMSWebserverDefinition().addAll((Collection)newValue);
        return;
      case ConfigPackage.DEFINITION__RMI_SERVER_DEFINITION:
        getRMIServerDefinition().clear();
        getRMIServerDefinition().addAll((Collection)newValue);
        return;
      case ConfigPackage.DEFINITION__RDC_SERVER_DEFINITION:
        getRDCServerDefinition().clear();
        getRDCServerDefinition().addAll((Collection)newValue);
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
      case ConfigPackage.DEFINITION__CONTENT_MANAGER_SERVER_DEFINITION:
        getContentManagerServerDefinition().clear();
        return;
      case ConfigPackage.DEFINITION__DB2_SERVER_DEFINITION:
        getDB2ServerDefinition().clear();
        return;
      case ConfigPackage.DEFINITION__GCMS_WEBSERVER_DEFINITION:
        getGCMSWebserverDefinition().clear();
        return;
      case ConfigPackage.DEFINITION__RMI_SERVER_DEFINITION:
        getRMIServerDefinition().clear();
        return;
      case ConfigPackage.DEFINITION__RDC_SERVER_DEFINITION:
        getRDCServerDefinition().clear();
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
      case ConfigPackage.DEFINITION__CONTENT_MANAGER_SERVER_DEFINITION:
        return contentManagerServerDefinition != null && !contentManagerServerDefinition.isEmpty();
      case ConfigPackage.DEFINITION__DB2_SERVER_DEFINITION:
        return dB2ServerDefinition != null && !dB2ServerDefinition.isEmpty();
      case ConfigPackage.DEFINITION__GCMS_WEBSERVER_DEFINITION:
        return gCMSWebserverDefinition != null && !gCMSWebserverDefinition.isEmpty();
      case ConfigPackage.DEFINITION__RMI_SERVER_DEFINITION:
        return rMIServerDefinition != null && !rMIServerDefinition.isEmpty();
      case ConfigPackage.DEFINITION__RDC_SERVER_DEFINITION:
        return rDCServerDefinition != null && !rDCServerDefinition.isEmpty();
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
    result.append(" (rDCServerDefinition: ");
    result.append(rDCServerDefinition);
    result.append(')');
    return result.toString();
  }

} //DefinitionImpl
