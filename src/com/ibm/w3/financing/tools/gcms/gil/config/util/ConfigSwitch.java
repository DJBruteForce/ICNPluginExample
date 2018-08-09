/**
 * <copyright>
 * </copyright>
 *
 * $Id: ConfigSwitch.java,v 1.2 2008/02/13 21:50:22 sbaber Exp $
 */
package com.ibm.w3.financing.tools.gcms.gil.config.util;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.ibm.w3.financing.tools.gcms.gil.config.BackendSelection;
import com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage;
import com.ibm.w3.financing.tools.gcms.gil.config.Configuration;
import com.ibm.w3.financing.tools.gcms.gil.config.ContentManagerServer;
import com.ibm.w3.financing.tools.gcms.gil.config.DB2Server;
import com.ibm.w3.financing.tools.gcms.gil.config.DatabaseConnection;
import com.ibm.w3.financing.tools.gcms.gil.config.Definition;
import com.ibm.w3.financing.tools.gcms.gil.config.DocumentRoot;
import com.ibm.w3.financing.tools.gcms.gil.config.GILConfigType;
import com.ibm.w3.financing.tools.gcms.gil.config.Geography;
import com.ibm.w3.financing.tools.gcms.gil.config.NewComplexType;
import com.ibm.w3.financing.tools.gcms.gil.config.RMIServer;
import com.ibm.w3.financing.tools.gcms.gil.config.Service;
import com.ibm.w3.financing.tools.gcms.gil.config.ServiceSuite;
import com.ibm.w3.financing.tools.gcms.gil.config.Webserver;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage
 * @generated
 */
public class ConfigSwitch
{
  /**
   * The cached model package
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static ConfigPackage modelPackage;

  /**
   * Creates an instance of the switch.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ConfigSwitch()
  {
    if (modelPackage == null)
    {
      modelPackage = ConfigPackage.eINSTANCE;
    }
  }

  /**
   * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the first non-null result returned by a <code>caseXXX</code> call.
   * @generated
   */
  public Object doSwitch(EObject theEObject)
  {
    return doSwitch(theEObject.eClass(), theEObject);
  }

  /**
   * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the first non-null result returned by a <code>caseXXX</code> call.
   * @generated
   */
  protected Object doSwitch(EClass theEClass, EObject theEObject)
  {
    if (theEClass.eContainer() == modelPackage)
    {
      return doSwitch(theEClass.getClassifierID(), theEObject);
    }
    else
    {
      List eSuperTypes = theEClass.getESuperTypes();
      return
        eSuperTypes.isEmpty() ?
          defaultCase(theEObject) :
          doSwitch((EClass)eSuperTypes.get(0), theEObject);
    }
  }

  /**
   * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the first non-null result returned by a <code>caseXXX</code> call.
   * @generated
   */
  protected Object doSwitch(int classifierID, EObject theEObject)
  {
    switch (classifierID)
    {
      case ConfigPackage.BACKEND_SELECTION:
      {
        BackendSelection backendSelection = (BackendSelection)theEObject;
        Object result = caseBackendSelection(backendSelection);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ConfigPackage.CONFIG_URATION:
      {
        Configuration configuration = (Configuration)theEObject;
        Object result = caseConfiguration(configuration);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ConfigPackage.CONTENT_MANAGER_SERVER:
      {
        ContentManagerServer contentManagerServer = (ContentManagerServer)theEObject;
        Object result = caseContentManagerServer(contentManagerServer);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ConfigPackage.DATABASE_CONNECTION:
      {
        DatabaseConnection databaseConnection = (DatabaseConnection)theEObject;
        Object result = caseDatabaseConnection(databaseConnection);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ConfigPackage.DB2_SERVER:
      {
        DB2Server dB2Server = (DB2Server)theEObject;
        Object result = caseDB2Server(dB2Server);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ConfigPackage.DEFINITION:
      {
        Definition definition = (Definition)theEObject;
        Object result = caseDefinition(definition);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ConfigPackage.DOCUMENT_ROOT:
      {
        DocumentRoot documentRoot = (DocumentRoot)theEObject;
        Object result = caseDocumentRoot(documentRoot);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ConfigPackage.GEOGRAPHY:
      {
        Geography geography = (Geography)theEObject;
        Object result = caseGeography(geography);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ConfigPackage.GIL_CONFIG_TYPE:
      {
        GILConfigType gilConfigType = (GILConfigType)theEObject;
        Object result = caseGILConfigType(gilConfigType);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ConfigPackage.NEW_COMPLEX_TYPE:
      {
        NewComplexType newComplexType = (NewComplexType)theEObject;
        Object result = caseNewComplexType(newComplexType);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ConfigPackage.RMI_SERVER:
      {
        RMIServer rmiServer = (RMIServer)theEObject;
        Object result = caseRMIServer(rmiServer);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ConfigPackage.SERVICE:
      {
        Service service = (Service)theEObject;
        Object result = caseService(service);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ConfigPackage.SERVICE_SUITE:
      {
        ServiceSuite serviceSuite = (ServiceSuite)theEObject;
        Object result = caseServiceSuite(serviceSuite);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ConfigPackage.WEBSERVER:
      {
        Webserver webserver = (Webserver)theEObject;
        Object result = caseWebserver(webserver);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      default: return defaultCase(theEObject);
    }
  }

  /**
   * Returns the result of interpretting the object as an instance of '<em>Backend Selection</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpretting the object as an instance of '<em>Backend Selection</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseBackendSelection(BackendSelection object)
  {
    return null;
  }

  /**
   * Returns the result of interpretting the object as an instance of '<em>uration</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpretting the object as an instance of '<em>uration</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseConfiguration(Configuration object)
  {
    return null;
  }

  /**
   * Returns the result of interpretting the object as an instance of '<em>Content Manager Server</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpretting the object as an instance of '<em>Content Manager Server</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseContentManagerServer(ContentManagerServer object)
  {
    return null;
  }

  /**
   * Returns the result of interpretting the object as an instance of '<em>Database Connection</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpretting the object as an instance of '<em>Database Connection</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseDatabaseConnection(DatabaseConnection object)
  {
    return null;
  }

  /**
   * Returns the result of interpretting the object as an instance of '<em>DB2 Server</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpretting the object as an instance of '<em>DB2 Server</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseDB2Server(DB2Server object)
  {
    return null;
  }

  /**
   * Returns the result of interpretting the object as an instance of '<em>Definition</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpretting the object as an instance of '<em>Definition</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseDefinition(Definition object)
  {
    return null;
  }

  /**
   * Returns the result of interpretting the object as an instance of '<em>Document Root</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpretting the object as an instance of '<em>Document Root</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseDocumentRoot(DocumentRoot object)
  {
    return null;
  }

  /**
   * Returns the result of interpretting the object as an instance of '<em>Geography</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpretting the object as an instance of '<em>Geography</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseGeography(Geography object)
  {
    return null;
  }

  /**
   * Returns the result of interpretting the object as an instance of '<em>GIL Config Type</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpretting the object as an instance of '<em>GIL Config Type</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseGILConfigType(GILConfigType object)
  {
    return null;
  }

  /**
   * Returns the result of interpretting the object as an instance of '<em>New Complex Type</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpretting the object as an instance of '<em>New Complex Type</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseNewComplexType(NewComplexType object)
  {
    return null;
  }

  /**
   * Returns the result of interpretting the object as an instance of '<em>RMI Server</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpretting the object as an instance of '<em>RMI Server</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseRMIServer(RMIServer object)
  {
    return null;
  }

  /**
   * Returns the result of interpretting the object as an instance of '<em>Service</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpretting the object as an instance of '<em>Service</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseService(Service object)
  {
    return null;
  }

  /**
   * Returns the result of interpretting the object as an instance of '<em>Service Suite</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpretting the object as an instance of '<em>Service Suite</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseServiceSuite(ServiceSuite object)
  {
    return null;
  }

  /**
   * Returns the result of interpretting the object as an instance of '<em>Webserver</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpretting the object as an instance of '<em>Webserver</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public Object caseWebserver(Webserver object)
  {
    return null;
  }

  /**
   * Returns the result of interpretting the object as an instance of '<em>EObject</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch, but this is the last case anyway.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpretting the object as an instance of '<em>EObject</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject)
   * @generated
   */
  public Object defaultCase(EObject object)
  {
    return null;
  }

} //ConfigSwitch
