/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.ibm.w3.financing.tools.gcms.query.results.impl;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.sdo.impl.EDataObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.ibm.w3.financing.tools.gcms.query.results.DOCUMENT;
import com.ibm.w3.financing.tools.gcms.query.results.FOLDER;
import com.ibm.w3.financing.tools.gcms.query.results.QUERYRESULTSType;
import com.ibm.w3.financing.tools.gcms.query.results.ResultsPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>QUERYRESULTS Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.query.results.impl.QUERYRESULTSTypeImpl#getDOCUMENT <em>DOCUMENT</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.query.results.impl.QUERYRESULTSTypeImpl#getFOLDER <em>FOLDER</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class QUERYRESULTSTypeImpl extends EDataObjectImpl implements QUERYRESULTSType
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static final long serialVersionUID = 1L;

  /**
   * The cached value of the '{@link #getDOCUMENT() <em>DOCUMENT</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDOCUMENT()
   * @generated
   * @ordered
   */
  protected EList dOCUMENT;

  /**
   * The cached value of the '{@link #getFOLDER() <em>FOLDER</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getFOLDER()
   * @generated
   * @ordered
   */
  protected EList fOLDER;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected QUERYRESULTSTypeImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return ResultsPackage.Literals.QUERYRESULTS_TYPE;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public List getDOCUMENT()
  {
    if (dOCUMENT == null)
    {
      dOCUMENT = new EObjectContainmentEList(DOCUMENT.class, this, ResultsPackage.QUERYRESULTS_TYPE__DOCUMENT);
    }
    return dOCUMENT;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public List getFOLDER()
  {
    if (fOLDER == null)
    {
      fOLDER = new EObjectContainmentEList(FOLDER.class, this, ResultsPackage.QUERYRESULTS_TYPE__FOLDER);
    }
    return fOLDER;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case ResultsPackage.QUERYRESULTS_TYPE__DOCUMENT:
        return ((InternalEList)getDOCUMENT()).basicRemove(otherEnd, msgs);
      case ResultsPackage.QUERYRESULTS_TYPE__FOLDER:
        return ((InternalEList)getFOLDER()).basicRemove(otherEnd, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
      case ResultsPackage.QUERYRESULTS_TYPE__DOCUMENT:
        return getDOCUMENT();
      case ResultsPackage.QUERYRESULTS_TYPE__FOLDER:
        return getFOLDER();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("unchecked")
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case ResultsPackage.QUERYRESULTS_TYPE__DOCUMENT:
        getDOCUMENT().clear();
        getDOCUMENT().addAll((Collection)newValue);
        return;
      case ResultsPackage.QUERYRESULTS_TYPE__FOLDER:
        getFOLDER().clear();
        getFOLDER().addAll((Collection)newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
      case ResultsPackage.QUERYRESULTS_TYPE__DOCUMENT:
        getDOCUMENT().clear();
        return;
      case ResultsPackage.QUERYRESULTS_TYPE__FOLDER:
        getFOLDER().clear();
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case ResultsPackage.QUERYRESULTS_TYPE__DOCUMENT:
        return dOCUMENT != null && !dOCUMENT.isEmpty();
      case ResultsPackage.QUERYRESULTS_TYPE__FOLDER:
        return fOLDER != null && !fOLDER.isEmpty();
    }
    return super.eIsSet(featureID);
  }

} //QUERYRESULTSTypeImpl
