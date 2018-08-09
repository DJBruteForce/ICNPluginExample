/**
 * <copyright>
 * </copyright>
 *
 * $Id: ConfigAdapterFactory.java,v 1.2 2008/02/13 21:50:22 sbaber Exp $
 */
package com.ibm.w3.financing.tools.gcms.gil.config.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
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
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see com.ibm.w3.financing.tools.gcms.gil.config.ConfigPackage
 * @generated
 */
public class ConfigAdapterFactory extends AdapterFactoryImpl
{
  /**
   * The cached model package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static ConfigPackage modelPackage;

  /**
   * Creates an instance of the adapter factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ConfigAdapterFactory()
  {
    if (modelPackage == null)
    {
      modelPackage = ConfigPackage.eINSTANCE;
    }
  }

  /**
   * Returns whether this factory is applicable for the type of the object.
   * <!-- begin-user-doc -->
   * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
   * <!-- end-user-doc -->
   * @return whether this factory is applicable for the type of the object.
   * @generated
   */
  public boolean isFactoryForType(Object object)
  {
    if (object == modelPackage)
    {
      return true;
    }
    if (object instanceof EObject)
    {
      return ((EObject)object).eClass().getEPackage() == modelPackage;
    }
    return false;
  }

  /**
   * The switch the delegates to the <code>createXXX</code> methods.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ConfigSwitch modelSwitch =
    new ConfigSwitch()
    {
      public Object caseBackendSelection(BackendSelection object)
      {
        return createBackendSelectionAdapter();
      }
      public Object caseConfiguration(Configuration object)
      {
        return createConfigurationAdapter();
      }
      public Object caseContentManagerServer(ContentManagerServer object)
      {
        return createContentManagerServerAdapter();
      }
      public Object caseDatabaseConnection(DatabaseConnection object)
      {
        return createDatabaseConnectionAdapter();
      }
      public Object caseDB2Server(DB2Server object)
      {
        return createDB2ServerAdapter();
      }
      public Object caseDefinition(Definition object)
      {
        return createDefinitionAdapter();
      }
      public Object caseDocumentRoot(DocumentRoot object)
      {
        return createDocumentRootAdapter();
      }
      public Object caseGeography(Geography object)
      {
        return createGeographyAdapter();
      }
      public Object caseGILConfigType(GILConfigType object)
      {
        return createGILConfigTypeAdapter();
      }
      public Object caseNewComplexType(NewComplexType object)
      {
        return createNewComplexTypeAdapter();
      }
      public Object caseRMIServer(RMIServer object)
      {
        return createRMIServerAdapter();
      }
      public Object caseService(Service object)
      {
        return createServiceAdapter();
      }
      public Object caseServiceSuite(ServiceSuite object)
      {
        return createServiceSuiteAdapter();
      }
      public Object caseWebserver(Webserver object)
      {
        return createWebserverAdapter();
      }
      public Object defaultCase(EObject object)
      {
        return createEObjectAdapter();
      }
    };

  /**
   * Creates an adapter for the <code>target</code>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param target the object to adapt.
   * @return the adapter for the <code>target</code>.
   * @generated
   */
  public Adapter createAdapter(Notifier target)
  {
    return (Adapter)modelSwitch.doSwitch((EObject)target);
  }


  /**
   * Creates a new adapter for an object of class '{@link com.ibm.w3.financing.tools.gcms.gil.config.BackendSelection <em>Backend Selection</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.BackendSelection
   * @generated
   */
  public Adapter createBackendSelectionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.ibm.w3.financing.tools.gcms.gil.config.Configuration <em>uration</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.Configuration
   * @generated
   */
  public Adapter createConfigurationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.ibm.w3.financing.tools.gcms.gil.config.ContentManagerServer <em>Content Manager Server</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.ContentManagerServer
   * @generated
   */
  public Adapter createContentManagerServerAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.ibm.w3.financing.tools.gcms.gil.config.DatabaseConnection <em>Database Connection</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.DatabaseConnection
   * @generated
   */
  public Adapter createDatabaseConnectionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.ibm.w3.financing.tools.gcms.gil.config.DB2Server <em>DB2 Server</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.DB2Server
   * @generated
   */
  public Adapter createDB2ServerAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.ibm.w3.financing.tools.gcms.gil.config.Definition <em>Definition</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.Definition
   * @generated
   */
  public Adapter createDefinitionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.ibm.w3.financing.tools.gcms.gil.config.DocumentRoot <em>Document Root</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.DocumentRoot
   * @generated
   */
  public Adapter createDocumentRootAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.ibm.w3.financing.tools.gcms.gil.config.Geography <em>Geography</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.Geography
   * @generated
   */
  public Adapter createGeographyAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.ibm.w3.financing.tools.gcms.gil.config.GILConfigType <em>GIL Config Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.GILConfigType
   * @generated
   */
  public Adapter createGILConfigTypeAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.ibm.w3.financing.tools.gcms.gil.config.NewComplexType <em>New Complex Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.NewComplexType
   * @generated
   */
  public Adapter createNewComplexTypeAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.ibm.w3.financing.tools.gcms.gil.config.RMIServer <em>RMI Server</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.RMIServer
   * @generated
   */
  public Adapter createRMIServerAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.ibm.w3.financing.tools.gcms.gil.config.Service <em>Service</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.Service
   * @generated
   */
  public Adapter createServiceAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.ibm.w3.financing.tools.gcms.gil.config.ServiceSuite <em>Service Suite</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.ServiceSuite
   * @generated
   */
  public Adapter createServiceSuiteAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.ibm.w3.financing.tools.gcms.gil.config.Webserver <em>Webserver</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.ibm.w3.financing.tools.gcms.gil.config.Webserver
   * @generated
   */
  public Adapter createWebserverAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for the default case.
   * <!-- begin-user-doc -->
   * This default implementation returns null.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @generated
   */
  public Adapter createEObjectAdapter()
  {
    return null;
  }

} //ConfigAdapterFactory
