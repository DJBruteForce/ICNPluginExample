/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.ibm.w3.financing.tools.gcms.query.results.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.sdo.impl.EDataObjectImpl;

import com.ibm.w3.financing.tools.gcms.query.results.Image;
import com.ibm.w3.financing.tools.gcms.query.results.ResultsPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Image</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.query.results.impl.ImageImpl#getCreated <em>Created</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.query.results.impl.ImageImpl#getMimetype <em>Mimetype</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.query.results.impl.ImageImpl#getOrgfilename <em>Orgfilename</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.query.results.impl.ImageImpl#getPartnum <em>Partnum</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.query.results.impl.ImageImpl#getSize <em>Size</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ImageImpl extends EDataObjectImpl implements Image
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static final long serialVersionUID = 1L;

  /**
   * The default value of the '{@link #getCreated() <em>Created</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getCreated()
   * @generated
   * @ordered
   */
  protected static final String CREATED_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getCreated() <em>Created</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getCreated()
   * @generated
   * @ordered
   */
  protected String created = CREATED_EDEFAULT;

  /**
   * The default value of the '{@link #getMimetype() <em>Mimetype</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMimetype()
   * @generated
   * @ordered
   */
  protected static final String MIMETYPE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getMimetype() <em>Mimetype</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMimetype()
   * @generated
   * @ordered
   */
  protected String mimetype = MIMETYPE_EDEFAULT;

  /**
   * The default value of the '{@link #getOrgfilename() <em>Orgfilename</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOrgfilename()
   * @generated
   * @ordered
   */
  protected static final String ORGFILENAME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getOrgfilename() <em>Orgfilename</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOrgfilename()
   * @generated
   * @ordered
   */
  protected String orgfilename = ORGFILENAME_EDEFAULT;

  /**
   * The default value of the '{@link #getPartnum() <em>Partnum</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPartnum()
   * @generated
   * @ordered
   */
  protected static final String PARTNUM_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getPartnum() <em>Partnum</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPartnum()
   * @generated
   * @ordered
   */
  protected String partnum = PARTNUM_EDEFAULT;

  /**
   * The default value of the '{@link #getSize() <em>Size</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSize()
   * @generated
   * @ordered
   */
  protected static final String SIZE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getSize() <em>Size</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSize()
   * @generated
   * @ordered
   */
  protected String size = SIZE_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ImageImpl()
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
    return ResultsPackage.Literals.IMAGE;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getCreated()
  {
    return created;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setCreated(String newCreated)
  {
    String oldCreated = created;
    created = newCreated;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ResultsPackage.IMAGE__CREATED, oldCreated, created));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getMimetype()
  {
    return mimetype;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setMimetype(String newMimetype)
  {
    String oldMimetype = mimetype;
    mimetype = newMimetype;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ResultsPackage.IMAGE__MIMETYPE, oldMimetype, mimetype));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getOrgfilename()
  {
    return orgfilename;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setOrgfilename(String newOrgfilename)
  {
    String oldOrgfilename = orgfilename;
    orgfilename = newOrgfilename;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ResultsPackage.IMAGE__ORGFILENAME, oldOrgfilename, orgfilename));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getPartnum()
  {
    return partnum;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setPartnum(String newPartnum)
  {
    String oldPartnum = partnum;
    partnum = newPartnum;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ResultsPackage.IMAGE__PARTNUM, oldPartnum, partnum));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getSize()
  {
    return size;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setSize(String newSize)
  {
    String oldSize = size;
    size = newSize;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ResultsPackage.IMAGE__SIZE, oldSize, size));
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
      case ResultsPackage.IMAGE__CREATED:
        return getCreated();
      case ResultsPackage.IMAGE__MIMETYPE:
        return getMimetype();
      case ResultsPackage.IMAGE__ORGFILENAME:
        return getOrgfilename();
      case ResultsPackage.IMAGE__PARTNUM:
        return getPartnum();
      case ResultsPackage.IMAGE__SIZE:
        return getSize();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case ResultsPackage.IMAGE__CREATED:
        setCreated((String)newValue);
        return;
      case ResultsPackage.IMAGE__MIMETYPE:
        setMimetype((String)newValue);
        return;
      case ResultsPackage.IMAGE__ORGFILENAME:
        setOrgfilename((String)newValue);
        return;
      case ResultsPackage.IMAGE__PARTNUM:
        setPartnum((String)newValue);
        return;
      case ResultsPackage.IMAGE__SIZE:
        setSize((String)newValue);
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
      case ResultsPackage.IMAGE__CREATED:
        setCreated(CREATED_EDEFAULT);
        return;
      case ResultsPackage.IMAGE__MIMETYPE:
        setMimetype(MIMETYPE_EDEFAULT);
        return;
      case ResultsPackage.IMAGE__ORGFILENAME:
        setOrgfilename(ORGFILENAME_EDEFAULT);
        return;
      case ResultsPackage.IMAGE__PARTNUM:
        setPartnum(PARTNUM_EDEFAULT);
        return;
      case ResultsPackage.IMAGE__SIZE:
        setSize(SIZE_EDEFAULT);
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
      case ResultsPackage.IMAGE__CREATED:
        return CREATED_EDEFAULT == null ? created != null : !CREATED_EDEFAULT.equals(created);
      case ResultsPackage.IMAGE__MIMETYPE:
        return MIMETYPE_EDEFAULT == null ? mimetype != null : !MIMETYPE_EDEFAULT.equals(mimetype);
      case ResultsPackage.IMAGE__ORGFILENAME:
        return ORGFILENAME_EDEFAULT == null ? orgfilename != null : !ORGFILENAME_EDEFAULT.equals(orgfilename);
      case ResultsPackage.IMAGE__PARTNUM:
        return PARTNUM_EDEFAULT == null ? partnum != null : !PARTNUM_EDEFAULT.equals(partnum);
      case ResultsPackage.IMAGE__SIZE:
        return SIZE_EDEFAULT == null ? size != null : !SIZE_EDEFAULT.equals(size);
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
    result.append(" (created: ");
    result.append(created);
    result.append(", mimetype: ");
    result.append(mimetype);
    result.append(", orgfilename: ");
    result.append(orgfilename);
    result.append(", partnum: ");
    result.append(partnum);
    result.append(", size: ");
    result.append(size);
    result.append(')');
    return result.toString();
  }

} //ImageImpl
