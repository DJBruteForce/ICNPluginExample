/**
 * <copyright>
 * </copyright>
 *
 * $Id: GILConfigTypeImpl.java,v 1.3 2008/02/13 21:50:22 sbaber Exp $
 */
package com.ibm.w3.financing.tools.gcms.gil.config.impl;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.sdo.impl.EDataObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage;
import com.ibm.w3.financing.tools.gcms.gil.config.Configuration;
import com.ibm.w3.financing.tools.gcms.gil.config.ContentManagerServer;
import com.ibm.w3.financing.tools.gcms.gil.config.DB2Server;
import com.ibm.w3.financing.tools.gcms.gil.config.DatabaseConnection;
import com.ibm.w3.financing.tools.gcms.gil.config.GILConfigType;
import com.ibm.w3.financing.tools.gcms.gil.config.Geography;
import com.ibm.w3.financing.tools.gcms.gil.config.ServiceSuite;
import com.ibm.w3.financing.tools.gcms.gil.config.Webserver;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>GIL Config Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.impl.GILConfigTypeImpl#getConfiguration <em>Configuration</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.impl.GILConfigTypeImpl#getContentManagerServerDefinition <em>Content Manager Server Definition</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.impl.GILConfigTypeImpl#getDB2ServerDefinitions <em>DB2 Server Definitions</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.impl.GILConfigTypeImpl#getServiceSuites <em>Service Suites</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.impl.GILConfigTypeImpl#getGCMSWebserviceDefinitions <em>GCMS Webservice Definitions</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.impl.GILConfigTypeImpl#getRDCServerDefinitions <em>RDC Server Definitions</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.impl.GILConfigTypeImpl#getDatabaseConnections <em>Database Connections</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.gil.config.impl.GILConfigTypeImpl#getGeographies <em>Geographies</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class GILConfigTypeImpl extends EDataObjectImpl implements GILConfigType
{
  /**
   * The cached value of the '{@link #getConfiguration() <em>Configuration</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getConfiguration()
   * @generated
   * @ordered
   */
  protected EList configuration = null;

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
   * The cached value of the '{@link #getDB2ServerDefinitions() <em>DB2 Server Definitions</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDB2ServerDefinitions()
   * @generated
   * @ordered
   */
  protected EList dB2ServerDefinitions = null;

  /**
   * The cached value of the '{@link #getServiceSuites() <em>Service Suites</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getServiceSuites()
   * @generated
   * @ordered
   */
  protected EList serviceSuites = null;

  /**
   * The cached value of the '{@link #getGCMSWebserviceDefinitions() <em>GCMS Webservice Definitions</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getGCMSWebserviceDefinitions()
   * @generated
   * @ordered
   */
  protected EList gCMSWebserviceDefinitions = null;

  /**
   * The cached value of the '{@link #getRDCServerDefinitions() <em>RDC Server Definitions</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRDCServerDefinitions()
   * @generated
   * @ordered
   */
  protected EList rDCServerDefinitions = null;

  /**
   * The cached value of the '{@link #getDatabaseConnections() <em>Database Connections</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDatabaseConnections()
   * @generated
   * @ordered
   */
  protected EList databaseConnections = null;

  /**
   * The cached value of the '{@link #getGeographies() <em>Geographies</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getGeographies()
   * @generated
   * @ordered
   */
  protected Geography geographies = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected GILConfigTypeImpl()
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
    return ConfigPackage.eINSTANCE.getGILConfigType();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public List getConfiguration()
  {
    if (configuration == null)
    {
      configuration = new EObjectContainmentEList(Configuration.class, this, ConfigPackage.GIL_CONFIG_TYPE__CONFIGURATION);
    }
    return configuration;
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
      contentManagerServerDefinition = new EObjectContainmentEList(ContentManagerServer.class, this, ConfigPackage.GIL_CONFIG_TYPE__CONTENT_MANAGER_SERVER_DEFINITION);
    }
    return contentManagerServerDefinition;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public List getDB2ServerDefinitions()
  {
    if (dB2ServerDefinitions == null)
    {
      dB2ServerDefinitions = new EObjectContainmentEList(DB2Server.class, this, ConfigPackage.GIL_CONFIG_TYPE__DB2_SERVER_DEFINITIONS);
    }
    return dB2ServerDefinitions;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public List getServiceSuites()
  {
    if (serviceSuites == null)
    {
      serviceSuites = new EObjectContainmentEList(ServiceSuite.class, this, ConfigPackage.GIL_CONFIG_TYPE__SERVICE_SUITES);
    }
    return serviceSuites;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public List getGCMSWebserviceDefinitions()
  {
    if (gCMSWebserviceDefinitions == null)
    {
      gCMSWebserviceDefinitions = new EObjectContainmentEList(Webserver.class, this, ConfigPackage.GIL_CONFIG_TYPE__GCMS_WEBSERVICE_DEFINITIONS);
    }
    return gCMSWebserviceDefinitions;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public List getRDCServerDefinitions()
  {
    if (rDCServerDefinitions == null)
    {
      rDCServerDefinitions = new EObjectContainmentEList(Webserver.class, this, ConfigPackage.GIL_CONFIG_TYPE__RDC_SERVER_DEFINITIONS);
    }
    return rDCServerDefinitions;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public List getDatabaseConnections()
  {
    if (databaseConnections == null)
    {
      databaseConnections = new EObjectContainmentEList(DatabaseConnection.class, this, ConfigPackage.GIL_CONFIG_TYPE__DATABASE_CONNECTIONS);
    }
    return databaseConnections;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Geography getGeographies()
  {
    return geographies;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetGeographies(Geography newGeographies, NotificationChain msgs)
  {
    Geography oldGeographies = geographies;
    geographies = newGeographies;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ConfigPackage.GIL_CONFIG_TYPE__GEOGRAPHIES, oldGeographies, newGeographies);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setGeographies(Geography newGeographies)
  {
    if (newGeographies != geographies)
    {
      NotificationChain msgs = null;
      if (geographies != null)
        msgs = ((InternalEObject)geographies).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ConfigPackage.GIL_CONFIG_TYPE__GEOGRAPHIES, null, msgs);
      if (newGeographies != null)
        msgs = ((InternalEObject)newGeographies).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ConfigPackage.GIL_CONFIG_TYPE__GEOGRAPHIES, null, msgs);
      msgs = basicSetGeographies(newGeographies, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.GIL_CONFIG_TYPE__GEOGRAPHIES, newGeographies, newGeographies));
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
        case ConfigPackage.GIL_CONFIG_TYPE__CONFIGURATION:
          return ((InternalEList)getConfiguration()).basicRemove(otherEnd, msgs);
        case ConfigPackage.GIL_CONFIG_TYPE__CONTENT_MANAGER_SERVER_DEFINITION:
          return ((InternalEList)getContentManagerServerDefinition()).basicRemove(otherEnd, msgs);
        case ConfigPackage.GIL_CONFIG_TYPE__DB2_SERVER_DEFINITIONS:
          return ((InternalEList)getDB2ServerDefinitions()).basicRemove(otherEnd, msgs);
        case ConfigPackage.GIL_CONFIG_TYPE__SERVICE_SUITES:
          return ((InternalEList)getServiceSuites()).basicRemove(otherEnd, msgs);
        case ConfigPackage.GIL_CONFIG_TYPE__GCMS_WEBSERVICE_DEFINITIONS:
          return ((InternalEList)getGCMSWebserviceDefinitions()).basicRemove(otherEnd, msgs);
        case ConfigPackage.GIL_CONFIG_TYPE__RDC_SERVER_DEFINITIONS:
          return ((InternalEList)getRDCServerDefinitions()).basicRemove(otherEnd, msgs);
        case ConfigPackage.GIL_CONFIG_TYPE__DATABASE_CONNECTIONS:
          return ((InternalEList)getDatabaseConnections()).basicRemove(otherEnd, msgs);
        case ConfigPackage.GIL_CONFIG_TYPE__GEOGRAPHIES:
          return basicSetGeographies(null, msgs);
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
      case ConfigPackage.GIL_CONFIG_TYPE__CONFIGURATION:
        return getConfiguration();
      case ConfigPackage.GIL_CONFIG_TYPE__CONTENT_MANAGER_SERVER_DEFINITION:
        return getContentManagerServerDefinition();
      case ConfigPackage.GIL_CONFIG_TYPE__DB2_SERVER_DEFINITIONS:
        return getDB2ServerDefinitions();
      case ConfigPackage.GIL_CONFIG_TYPE__SERVICE_SUITES:
        return getServiceSuites();
      case ConfigPackage.GIL_CONFIG_TYPE__GCMS_WEBSERVICE_DEFINITIONS:
        return getGCMSWebserviceDefinitions();
      case ConfigPackage.GIL_CONFIG_TYPE__RDC_SERVER_DEFINITIONS:
        return getRDCServerDefinitions();
      case ConfigPackage.GIL_CONFIG_TYPE__DATABASE_CONNECTIONS:
        return getDatabaseConnections();
      case ConfigPackage.GIL_CONFIG_TYPE__GEOGRAPHIES:
        return getGeographies();
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
      case ConfigPackage.GIL_CONFIG_TYPE__CONFIGURATION:
        getConfiguration().clear();
        getConfiguration().addAll((Collection)newValue);
        return;
      case ConfigPackage.GIL_CONFIG_TYPE__CONTENT_MANAGER_SERVER_DEFINITION:
        getContentManagerServerDefinition().clear();
        getContentManagerServerDefinition().addAll((Collection)newValue);
        return;
      case ConfigPackage.GIL_CONFIG_TYPE__DB2_SERVER_DEFINITIONS:
        getDB2ServerDefinitions().clear();
        getDB2ServerDefinitions().addAll((Collection)newValue);
        return;
      case ConfigPackage.GIL_CONFIG_TYPE__SERVICE_SUITES:
        getServiceSuites().clear();
        getServiceSuites().addAll((Collection)newValue);
        return;
      case ConfigPackage.GIL_CONFIG_TYPE__GCMS_WEBSERVICE_DEFINITIONS:
        getGCMSWebserviceDefinitions().clear();
        getGCMSWebserviceDefinitions().addAll((Collection)newValue);
        return;
      case ConfigPackage.GIL_CONFIG_TYPE__RDC_SERVER_DEFINITIONS:
        getRDCServerDefinitions().clear();
        getRDCServerDefinitions().addAll((Collection)newValue);
        return;
      case ConfigPackage.GIL_CONFIG_TYPE__DATABASE_CONNECTIONS:
        getDatabaseConnections().clear();
        getDatabaseConnections().addAll((Collection)newValue);
        return;
      case ConfigPackage.GIL_CONFIG_TYPE__GEOGRAPHIES:
        setGeographies((Geography)newValue);
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
      case ConfigPackage.GIL_CONFIG_TYPE__CONFIGURATION:
        getConfiguration().clear();
        return;
      case ConfigPackage.GIL_CONFIG_TYPE__CONTENT_MANAGER_SERVER_DEFINITION:
        getContentManagerServerDefinition().clear();
        return;
      case ConfigPackage.GIL_CONFIG_TYPE__DB2_SERVER_DEFINITIONS:
        getDB2ServerDefinitions().clear();
        return;
      case ConfigPackage.GIL_CONFIG_TYPE__SERVICE_SUITES:
        getServiceSuites().clear();
        return;
      case ConfigPackage.GIL_CONFIG_TYPE__GCMS_WEBSERVICE_DEFINITIONS:
        getGCMSWebserviceDefinitions().clear();
        return;
      case ConfigPackage.GIL_CONFIG_TYPE__RDC_SERVER_DEFINITIONS:
        getRDCServerDefinitions().clear();
        return;
      case ConfigPackage.GIL_CONFIG_TYPE__DATABASE_CONNECTIONS:
        getDatabaseConnections().clear();
        return;
      case ConfigPackage.GIL_CONFIG_TYPE__GEOGRAPHIES:
        setGeographies((Geography)null);
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
      case ConfigPackage.GIL_CONFIG_TYPE__CONFIGURATION:
        return configuration != null && !configuration.isEmpty();
      case ConfigPackage.GIL_CONFIG_TYPE__CONTENT_MANAGER_SERVER_DEFINITION:
        return contentManagerServerDefinition != null && !contentManagerServerDefinition.isEmpty();
      case ConfigPackage.GIL_CONFIG_TYPE__DB2_SERVER_DEFINITIONS:
        return dB2ServerDefinitions != null && !dB2ServerDefinitions.isEmpty();
      case ConfigPackage.GIL_CONFIG_TYPE__SERVICE_SUITES:
        return serviceSuites != null && !serviceSuites.isEmpty();
      case ConfigPackage.GIL_CONFIG_TYPE__GCMS_WEBSERVICE_DEFINITIONS:
        return gCMSWebserviceDefinitions != null && !gCMSWebserviceDefinitions.isEmpty();
      case ConfigPackage.GIL_CONFIG_TYPE__RDC_SERVER_DEFINITIONS:
        return rDCServerDefinitions != null && !rDCServerDefinitions.isEmpty();
      case ConfigPackage.GIL_CONFIG_TYPE__DATABASE_CONNECTIONS:
        return databaseConnections != null && !databaseConnections.isEmpty();
      case ConfigPackage.GIL_CONFIG_TYPE__GEOGRAPHIES:
        return geographies != null;
    }
    return eDynamicIsSet(eFeature);
  }

} //GILConfigTypeImpl
