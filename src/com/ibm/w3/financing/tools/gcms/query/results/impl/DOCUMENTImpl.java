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
import com.ibm.w3.financing.tools.gcms.query.results.Image;
import com.ibm.w3.financing.tools.gcms.query.results.Index;
import com.ibm.w3.financing.tools.gcms.query.results.ResultsPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>DOCUMENT</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.query.results.impl.DOCUMENTImpl#getIndex <em>Index</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.query.results.impl.DOCUMENTImpl#getImage <em>Image</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.query.results.impl.DOCUMENTImpl#getItemtype <em>Itemtype</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.query.results.impl.DOCUMENTImpl#getPid <em>Pid</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DOCUMENTImpl extends EDataObjectImpl implements DOCUMENT
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
   * The cached value of the '{@link #getImage() <em>Image</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getImage()
   * @generated
   * @ordered
   */
  protected EList image;

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
  protected DOCUMENTImpl()
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
    return ResultsPackage.Literals.DOCUMENT;
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
      index = new EObjectContainmentEList(Index.class, this, ResultsPackage.DOCUMENT__INDEX);
    }
    return index;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public List getImage()
  {
    if (image == null)
    {
      image = new EObjectContainmentEList(Image.class, this, ResultsPackage.DOCUMENT__IMAGE);
    }
    return image;
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
      eNotify(new ENotificationImpl(this, Notification.SET, ResultsPackage.DOCUMENT__ITEMTYPE, oldItemtype, itemtype));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ResultsPackage.DOCUMENT__PID, oldPid, pid));
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
      case ResultsPackage.DOCUMENT__INDEX:
        return ((InternalEList)getIndex()).basicRemove(otherEnd, msgs);
      case ResultsPackage.DOCUMENT__IMAGE:
        return ((InternalEList)getImage()).basicRemove(otherEnd, msgs);
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
      case ResultsPackage.DOCUMENT__INDEX:
        return getIndex();
      case ResultsPackage.DOCUMENT__IMAGE:
        return getImage();
      case ResultsPackage.DOCUMENT__ITEMTYPE:
        return getItemtype();
      case ResultsPackage.DOCUMENT__PID:
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
      case ResultsPackage.DOCUMENT__INDEX:
        getIndex().clear();
        getIndex().addAll((Collection)newValue);
        return;
      case ResultsPackage.DOCUMENT__IMAGE:
        getImage().clear();
        getImage().addAll((Collection)newValue);
        return;
      case ResultsPackage.DOCUMENT__ITEMTYPE:
        setItemtype((String)newValue);
        return;
      case ResultsPackage.DOCUMENT__PID:
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
      case ResultsPackage.DOCUMENT__INDEX:
        getIndex().clear();
        return;
      case ResultsPackage.DOCUMENT__IMAGE:
        getImage().clear();
        return;
      case ResultsPackage.DOCUMENT__ITEMTYPE:
        setItemtype(ITEMTYPE_EDEFAULT);
        return;
      case ResultsPackage.DOCUMENT__PID:
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
      case ResultsPackage.DOCUMENT__INDEX:
        return index != null && !index.isEmpty();
      case ResultsPackage.DOCUMENT__IMAGE:
        return image != null && !image.isEmpty();
      case ResultsPackage.DOCUMENT__ITEMTYPE:
        return ITEMTYPE_EDEFAULT == null ? itemtype != null : !ITEMTYPE_EDEFAULT.equals(itemtype);
      case ResultsPackage.DOCUMENT__PID:
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

} //DOCUMENTImpl
