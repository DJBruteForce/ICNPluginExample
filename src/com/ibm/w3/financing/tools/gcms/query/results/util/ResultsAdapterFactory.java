/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.ibm.w3.financing.tools.gcms.query.results.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;

import com.ibm.w3.financing.tools.gcms.query.results.DOCUMENT;
import com.ibm.w3.financing.tools.gcms.query.results.DocumentRoot;
import com.ibm.w3.financing.tools.gcms.query.results.FOLDER;
import com.ibm.w3.financing.tools.gcms.query.results.Image;
import com.ibm.w3.financing.tools.gcms.query.results.Index;
import com.ibm.w3.financing.tools.gcms.query.results.QUERYRESULTSType;
import com.ibm.w3.financing.tools.gcms.query.results.ResultsPackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see com.ibm.w3.financing.tools.gcms.query.results.ResultsPackage
 * @generated
 */
public class ResultsAdapterFactory extends AdapterFactoryImpl
{
  /**
   * The cached model package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static ResultsPackage modelPackage;

  /**
   * Creates an instance of the adapter factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ResultsAdapterFactory()
  {
    if (modelPackage == null)
    {
      modelPackage = ResultsPackage.eINSTANCE;
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
  @Override
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
   * The switch that delegates to the <code>createXXX</code> methods.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ResultsSwitch modelSwitch =
    new ResultsSwitch()
    {
      public Object caseDOCUMENT(DOCUMENT object)
      {
        return createDOCUMENTAdapter();
      }
      public Object caseDocumentRoot(DocumentRoot object)
      {
        return createDocumentRootAdapter();
      }
      public Object caseFOLDER(FOLDER object)
      {
        return createFOLDERAdapter();
      }
      public Object caseImage(Image object)
      {
        return createImageAdapter();
      }
      public Object caseIndex(Index object)
      {
        return createIndexAdapter();
      }
      public Object caseQUERYRESULTSType(QUERYRESULTSType object)
      {
        return createQUERYRESULTSTypeAdapter();
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
  @Override
  public Adapter createAdapter(Notifier target)
  {
    return (Adapter)modelSwitch.doSwitch((EObject)target);
  }


  /**
   * Creates a new adapter for an object of class '{@link com.ibm.w3.financing.tools.gcms.query.results.DOCUMENT <em>DOCUMENT</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.ibm.w3.financing.tools.gcms.query.results.DOCUMENT
   * @generated
   */
  public Adapter createDOCUMENTAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.ibm.w3.financing.tools.gcms.query.results.DocumentRoot <em>Document Root</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.ibm.w3.financing.tools.gcms.query.results.DocumentRoot
   * @generated
   */
  public Adapter createDocumentRootAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.ibm.w3.financing.tools.gcms.query.results.FOLDER <em>FOLDER</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.ibm.w3.financing.tools.gcms.query.results.FOLDER
   * @generated
   */
  public Adapter createFOLDERAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.ibm.w3.financing.tools.gcms.query.results.Image <em>Image</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.ibm.w3.financing.tools.gcms.query.results.Image
   * @generated
   */
  public Adapter createImageAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.ibm.w3.financing.tools.gcms.query.results.Index <em>Index</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.ibm.w3.financing.tools.gcms.query.results.Index
   * @generated
   */
  public Adapter createIndexAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link com.ibm.w3.financing.tools.gcms.query.results.QUERYRESULTSType <em>QUERYRESULTS Type</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see com.ibm.w3.financing.tools.gcms.query.results.QUERYRESULTSType
   * @generated
   */
  public Adapter createQUERYRESULTSTypeAdapter()
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

} //ResultsAdapterFactory
