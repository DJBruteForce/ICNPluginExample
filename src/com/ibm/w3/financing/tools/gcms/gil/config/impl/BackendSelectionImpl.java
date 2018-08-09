/**
 * <copyright>
 * </copyright>
 *
 * $Id: BackendSelectionImpl.java,v 1.3 2008/02/13 21:50:22 sbaber Exp $
 */
package com.ibm.w3.financing.tools.gcms.gil.config.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.sdo.impl.EDataObjectImpl;

import com.ibm.w3.financing.tools.gcms.gil.config.BackendSelection;
import com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Backend Selection</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.impl.BackendSelectionImpl#getGeography <em>Geography</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.impl.BackendSelectionImpl#getGlobalAPTS <em>Global APTS</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.impl.BackendSelectionImpl#getGlobalGCMS <em>Global GCMS</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.impl.BackendSelectionImpl#getProductReference <em>Product Reference</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.impl.BackendSelectionImpl#getRDC <em>RDC</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.impl.BackendSelectionImpl#getRMI <em>RMI</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.impl.BackendSelectionImpl#getGCMS <em>GCMS</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class BackendSelectionImpl extends EDataObjectImpl implements BackendSelection
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
   * The default value of the '{@link #getGlobalAPTS() <em>Global APTS</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getGlobalAPTS()
   * @generated
   * @ordered
   */
  protected static final String GLOBAL_APTS_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getGlobalAPTS() <em>Global APTS</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getGlobalAPTS()
   * @generated
   * @ordered
   */
  protected String globalAPTS = GLOBAL_APTS_EDEFAULT;

  /**
   * The default value of the '{@link #getGlobalGCMS() <em>Global GCMS</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getGlobalGCMS()
   * @generated
   * @ordered
   */
  protected static final String GLOBAL_GCMS_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getGlobalGCMS() <em>Global GCMS</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getGlobalGCMS()
   * @generated
   * @ordered
   */
  protected String globalGCMS = GLOBAL_GCMS_EDEFAULT;

  /**
   * The default value of the '{@link #getProductReference() <em>Product Reference</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getProductReference()
   * @generated
   * @ordered
   */
  protected static final String PRODUCT_REFERENCE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getProductReference() <em>Product Reference</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getProductReference()
   * @generated
   * @ordered
   */
  protected String productReference = PRODUCT_REFERENCE_EDEFAULT;

  /**
   * The default value of the '{@link #getRDC() <em>RDC</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRDC()
   * @generated
   * @ordered
   */
  protected static final String RDC_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getRDC() <em>RDC</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRDC()
   * @generated
   * @ordered
   */
  protected String rDC = RDC_EDEFAULT;

  /**
   * The default value of the '{@link #getRMI() <em>RMI</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRMI()
   * @generated
   * @ordered
   */
  protected static final String RMI_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getRMI() <em>RMI</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRMI()
   * @generated
   * @ordered
   */
  protected String rMI = RMI_EDEFAULT;

  /**
   * The default value of the '{@link #getGCMS() <em>GCMS</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getGCMS()
   * @generated
   * @ordered
   */
  protected static final String GCMS_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getGCMS() <em>GCMS</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getGCMS()
   * @generated
   * @ordered
   */
  protected String gCMS = GCMS_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected BackendSelectionImpl()
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
    return ConfigPackage.eINSTANCE.getBackendSelection();
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
      eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.BACKEND_SELECTION__GEOGRAPHY, oldGeography, geography));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getGlobalAPTS()
  {
    return globalAPTS;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setGlobalAPTS(String newGlobalAPTS)
  {
    String oldGlobalAPTS = globalAPTS;
    globalAPTS = newGlobalAPTS;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.BACKEND_SELECTION__GLOBAL_APTS, oldGlobalAPTS, globalAPTS));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getGlobalGCMS()
  {
    return globalGCMS;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setGlobalGCMS(String newGlobalGCMS)
  {
    String oldGlobalGCMS = globalGCMS;
    globalGCMS = newGlobalGCMS;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.BACKEND_SELECTION__GLOBAL_GCMS, oldGlobalGCMS, globalGCMS));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getProductReference()
  {
    return productReference;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setProductReference(String newProductReference)
  {
    String oldProductReference = productReference;
    productReference = newProductReference;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.BACKEND_SELECTION__PRODUCT_REFERENCE, oldProductReference, productReference));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getRDC()
  {
    return rDC;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setRDC(String newRDC)
  {
    String oldRDC = rDC;
    rDC = newRDC;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.BACKEND_SELECTION__RDC, oldRDC, rDC));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getRMI()
  {
    return rMI;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setRMI(String newRMI)
  {
    String oldRMI = rMI;
    rMI = newRMI;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.BACKEND_SELECTION__RMI, oldRMI, rMI));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getGCMS()
  {
    return gCMS;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setGCMS(String newGCMS)
  {
    String oldGCMS = gCMS;
    gCMS = newGCMS;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.BACKEND_SELECTION__GCMS, oldGCMS, gCMS));
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
      case ConfigPackage.BACKEND_SELECTION__GEOGRAPHY:
        return getGeography();
      case ConfigPackage.BACKEND_SELECTION__GLOBAL_APTS:
        return getGlobalAPTS();
      case ConfigPackage.BACKEND_SELECTION__GLOBAL_GCMS:
        return getGlobalGCMS();
      case ConfigPackage.BACKEND_SELECTION__PRODUCT_REFERENCE:
        return getProductReference();
      case ConfigPackage.BACKEND_SELECTION__RDC:
        return getRDC();
      case ConfigPackage.BACKEND_SELECTION__RMI:
        return getRMI();
      case ConfigPackage.BACKEND_SELECTION__GCMS:
        return getGCMS();
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
      case ConfigPackage.BACKEND_SELECTION__GEOGRAPHY:
        setGeography((String)newValue);
        return;
      case ConfigPackage.BACKEND_SELECTION__GLOBAL_APTS:
        setGlobalAPTS((String)newValue);
        return;
      case ConfigPackage.BACKEND_SELECTION__GLOBAL_GCMS:
        setGlobalGCMS((String)newValue);
        return;
      case ConfigPackage.BACKEND_SELECTION__PRODUCT_REFERENCE:
        setProductReference((String)newValue);
        return;
      case ConfigPackage.BACKEND_SELECTION__RDC:
        setRDC((String)newValue);
        return;
      case ConfigPackage.BACKEND_SELECTION__RMI:
        setRMI((String)newValue);
        return;
      case ConfigPackage.BACKEND_SELECTION__GCMS:
        setGCMS((String)newValue);
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
      case ConfigPackage.BACKEND_SELECTION__GEOGRAPHY:
        setGeography(GEOGRAPHY_EDEFAULT);
        return;
      case ConfigPackage.BACKEND_SELECTION__GLOBAL_APTS:
        setGlobalAPTS(GLOBAL_APTS_EDEFAULT);
        return;
      case ConfigPackage.BACKEND_SELECTION__GLOBAL_GCMS:
        setGlobalGCMS(GLOBAL_GCMS_EDEFAULT);
        return;
      case ConfigPackage.BACKEND_SELECTION__PRODUCT_REFERENCE:
        setProductReference(PRODUCT_REFERENCE_EDEFAULT);
        return;
      case ConfigPackage.BACKEND_SELECTION__RDC:
        setRDC(RDC_EDEFAULT);
        return;
      case ConfigPackage.BACKEND_SELECTION__RMI:
        setRMI(RMI_EDEFAULT);
        return;
      case ConfigPackage.BACKEND_SELECTION__GCMS:
        setGCMS(GCMS_EDEFAULT);
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
      case ConfigPackage.BACKEND_SELECTION__GEOGRAPHY:
        return GEOGRAPHY_EDEFAULT == null ? geography != null : !GEOGRAPHY_EDEFAULT.equals(geography);
      case ConfigPackage.BACKEND_SELECTION__GLOBAL_APTS:
        return GLOBAL_APTS_EDEFAULT == null ? globalAPTS != null : !GLOBAL_APTS_EDEFAULT.equals(globalAPTS);
      case ConfigPackage.BACKEND_SELECTION__GLOBAL_GCMS:
        return GLOBAL_GCMS_EDEFAULT == null ? globalGCMS != null : !GLOBAL_GCMS_EDEFAULT.equals(globalGCMS);
      case ConfigPackage.BACKEND_SELECTION__PRODUCT_REFERENCE:
        return PRODUCT_REFERENCE_EDEFAULT == null ? productReference != null : !PRODUCT_REFERENCE_EDEFAULT.equals(productReference);
      case ConfigPackage.BACKEND_SELECTION__RDC:
        return RDC_EDEFAULT == null ? rDC != null : !RDC_EDEFAULT.equals(rDC);
      case ConfigPackage.BACKEND_SELECTION__RMI:
        return RMI_EDEFAULT == null ? rMI != null : !RMI_EDEFAULT.equals(rMI);
      case ConfigPackage.BACKEND_SELECTION__GCMS:
        return GCMS_EDEFAULT == null ? gCMS != null : !GCMS_EDEFAULT.equals(gCMS);
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
    result.append(", globalAPTS: ");
    result.append(globalAPTS);
    result.append(", globalGCMS: ");
    result.append(globalGCMS);
    result.append(", productReference: ");
    result.append(productReference);
    result.append(", rDC: ");
    result.append(rDC);
    result.append(", rMI: ");
    result.append(rMI);
    result.append(", gCMS: ");
    result.append(gCMS);
    result.append(')');
    return result.toString();
  }

} //BackendSelectionImpl
