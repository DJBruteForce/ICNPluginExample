/**
 * <copyright>
 * </copyright>
 *
 * $Id: WebserverImpl.java,v 1.2 2008/02/13 21:50:22 sbaber Exp $
 */
package com.ibm.w3.financing.tools.gcms.gil.config.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.sdo.impl.EDataObjectImpl;

import com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage;
import com.ibm.w3.financing.tools.gcms.gil.config.Webserver;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Webserver</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.impl.WebserverImpl#getName <em>Name</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.impl.WebserverImpl#getURL <em>URL</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.impl.WebserverImpl#getServiceSuite <em>Service Suite</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class WebserverImpl extends EDataObjectImpl implements Webserver
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
   * The default value of the '{@link #getURL() <em>URL</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getURL()
   * @generated
   * @ordered
   */
  protected static final String URL_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getURL() <em>URL</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getURL()
   * @generated
   * @ordered
   */
  protected String uRL = URL_EDEFAULT;

  /**
   * The default value of the '{@link #getServiceSuite() <em>Service Suite</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getServiceSuite()
   * @generated
   * @ordered
   */
  protected static final String SERVICE_SUITE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getServiceSuite() <em>Service Suite</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getServiceSuite()
   * @generated
   * @ordered
   */
  protected String serviceSuite = SERVICE_SUITE_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected WebserverImpl()
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
    return ConfigPackage.eINSTANCE.getWebserver();
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
      eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.WEBSERVER__NAME, oldName, name));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getURL()
  {
    return uRL;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setURL(String newURL)
  {
    String oldURL = uRL;
    uRL = newURL;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.WEBSERVER__URL, oldURL, uRL));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getServiceSuite()
  {
    return serviceSuite;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setServiceSuite(String newServiceSuite)
  {
    String oldServiceSuite = serviceSuite;
    serviceSuite = newServiceSuite;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.WEBSERVER__SERVICE_SUITE, oldServiceSuite, serviceSuite));
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
      case ConfigPackage.WEBSERVER__NAME:
        return getName();
      case ConfigPackage.WEBSERVER__URL:
        return getURL();
      case ConfigPackage.WEBSERVER__SERVICE_SUITE:
        return getServiceSuite();
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
      case ConfigPackage.WEBSERVER__NAME:
        setName((String)newValue);
        return;
      case ConfigPackage.WEBSERVER__URL:
        setURL((String)newValue);
        return;
      case ConfigPackage.WEBSERVER__SERVICE_SUITE:
        setServiceSuite((String)newValue);
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
      case ConfigPackage.WEBSERVER__NAME:
        setName(NAME_EDEFAULT);
        return;
      case ConfigPackage.WEBSERVER__URL:
        setURL(URL_EDEFAULT);
        return;
      case ConfigPackage.WEBSERVER__SERVICE_SUITE:
        setServiceSuite(SERVICE_SUITE_EDEFAULT);
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
      case ConfigPackage.WEBSERVER__NAME:
        return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
      case ConfigPackage.WEBSERVER__URL:
        return URL_EDEFAULT == null ? uRL != null : !URL_EDEFAULT.equals(uRL);
      case ConfigPackage.WEBSERVER__SERVICE_SUITE:
        return SERVICE_SUITE_EDEFAULT == null ? serviceSuite != null : !SERVICE_SUITE_EDEFAULT.equals(serviceSuite);
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
    result.append(", uRL: ");
    result.append(uRL);
    result.append(", serviceSuite: ");
    result.append(serviceSuite);
    result.append(')');
    return result.toString();
  }

} //WebserverImpl
