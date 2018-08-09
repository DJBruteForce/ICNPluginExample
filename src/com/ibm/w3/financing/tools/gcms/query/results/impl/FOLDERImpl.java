/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.ibm.w3.financing.tools.gcms.query.results.impl;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.sdo.impl.EDataObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.ibm.w3.financing.tools.gcms.query.results.DOCUMENT;
import com.ibm.w3.financing.tools.gcms.query.results.FOLDER;
import com.ibm.w3.financing.tools.gcms.query.results.Index;
import com.ibm.w3.financing.tools.gcms.query.results.ResultsPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>FOLDER</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.query.results.impl.FOLDERImpl#getIndex <em>Index</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.query.results.impl.FOLDERImpl#getDOCUMENT <em>DOCUMENT</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.query.results.impl.FOLDERImpl#getItemtype <em>Itemtype</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.query.results.impl.FOLDERImpl#getPid <em>Pid</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class FOLDERImpl extends EDataObjectImpl implements FOLDER
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static final long serialVersionUID = 1L;

  /**
   * The cached value of the '{@link #getIndex() <em>Index</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getIndex()
   * @generated
   * @ordered
   */
  protected EList index;

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
   * The default value of the '{@link #getItemtype() <em>Itemtype</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getItemtype()
   * @generated
   * @ordered
   */
  protected static final String ITEMTYPE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getItemtype() <em>Itemtype</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getItemtype()
   * @generated
   * @ordered
   */
  protected String itemtype = ITEMTYPE_EDEFAULT;

  /**
   * The default value of the '{@link #getPid() <em>Pid</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPid()
   * @generated
   * @ordered
   */
  protected static final String PID_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getPid() <em>Pid</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPid()
   * @generated
   * @ordered
   */
  protected String pid = PID_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected FOLDERImpl()
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
    return ResultsPackage.Literals.FOLDER;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public List getIndex()
  {
    if (index == null)
    {
      index = new EObjectContainmentEList(Index.class, this, ResultsPackage.FOLDER__INDEX);
    }
    return index;
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
      dOCUMENT = new EObjectContainmentEList(DOCUMENT.class, this, ResultsPackage.FOLDER__DOCUMENT);
    }
    return dOCUMENT;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getItemtype()
  {
    return itemtype;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setItemtype(String newItemtype)
  {
    String oldItemtype = itemtype;
    itemtype = newItemtype;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ResultsPackage.FOLDER__ITEMTYPE, oldItemtype, itemtype));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getPid()
  {
    return pid;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setPid(String newPid)
  {
    String oldPid = pid;
    pid = newPid;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ResultsPackage.FOLDER__PID, oldPid, pid));
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
      case ResultsPackage.FOLDER__INDEX:
        return ((InternalEList)getIndex()).basicRemove(otherEnd, msgs);
      case ResultsPackage.FOLDER__DOCUMENT:
        return ((InternalEList)getDOCUMENT()).basicRemove(otherEnd, msgs);
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
      case ResultsPackage.FOLDER__INDEX:
        return getIndex();
      case ResultsPackage.FOLDER__DOCUMENT:
        return getDOCUMENT();
      case ResultsPackage.FOLDER__ITEMTYPE:
        return getItemtype();
      case ResultsPackage.FOLDER__PID:
        return getPid();
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
      case ResultsPackage.FOLDER__INDEX:
        getIndex().clear();
        getIndex().addAll((Collection)newValue);
        return;
      case ResultsPackage.FOLDER__DOCUMENT:
        getDOCUMENT().clear();
        getDOCUMENT().addAll((Collection)newValue);
        return;
      case ResultsPackage.FOLDER__ITEMTYPE:
        setItemtype((String)newValue);
        return;
      case ResultsPackage.FOLDER__PID:
        setPid((String)newValue);
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
      case ResultsPackage.FOLDER__INDEX:
        getIndex().clear();
        return;
      case ResultsPackage.FOLDER__DOCUMENT:
        getDOCUMENT().clear();
        return;
      case ResultsPackage.FOLDER__ITEMTYPE:
        setItemtype(ITEMTYPE_EDEFAULT);
        return;
      case ResultsPackage.FOLDER__PID:
        setPid(PID_EDEFAULT);
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
      case ResultsPackage.FOLDER__INDEX:
        return index != null && !index.isEmpty();
      case ResultsPackage.FOLDER__DOCUMENT:
        return dOCUMENT != null && !dOCUMENT.isEmpty();
      case ResultsPackage.FOLDER__ITEMTYPE:
        return ITEMTYPE_EDEFAULT == null ? itemtype != null : !ITEMTYPE_EDEFAULT.equals(itemtype);
      case ResultsPackage.FOLDER__PID:
        return PID_EDEFAULT == null ? pid != null : !PID_EDEFAULT.equals(pid);
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString()
  {
    if (eIsProxy()) return super.toString();

    StringBuffer result = new StringBuffer(super.toString());
    result.append(" (itemtype: ");
    result.append(itemtype);
    result.append(", pid: ");
    result.append(pid);
    result.append(')');
    return result.toString();
  }

} //FOLDERImpl
