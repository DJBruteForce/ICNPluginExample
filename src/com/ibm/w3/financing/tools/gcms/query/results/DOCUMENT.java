/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.ibm.w3.financing.tools.gcms.query.results;

import java.util.List;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>DOCUMENT</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.query.results.DOCUMENT#getIndex <em>Index</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.query.results.DOCUMENT#getImage <em>Image</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.query.results.DOCUMENT#getItemtype <em>Itemtype</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.query.results.DOCUMENT#getPid <em>Pid</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.ibm.w3.financing.tools.gcms.query.results.ResultsPackage#getDOCUMENT()
 * @model extendedMetaData="name='DOCUMENT' kind='elementOnly'"
 * @generated
 */
public interface DOCUMENT
{
  /**
   * Returns the value of the '<em><b>Index</b></em>' containment reference list.
   * The list contents are of type {@link com.ibm.w3.financing.tools.gcms.query.results.Index}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Index</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Index</em>' containment reference list.
   * @see com.ibm.w3.financing.tools.gcms.query.results.ResultsPackage#getDOCUMENT_Index()
   * @model type="com.ibm.w3.financing.tools.gcms.query.results.Index" containment="true" required="true"
   *        extendedMetaData="kind='element' name='index'"
   * @generated
   */
  List getIndex();

  /**
   * Returns the value of the '<em><b>Image</b></em>' containment reference list.
   * The list contents are of type {@link com.ibm.w3.financing.tools.gcms.query.results.Image}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Image</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Image</em>' containment reference list.
   * @see com.ibm.w3.financing.tools.gcms.query.results.ResultsPackage#getDOCUMENT_Image()
   * @model type="com.ibm.w3.financing.tools.gcms.query.results.Image" containment="true"
   *        extendedMetaData="kind='element' name='image'"
   * @generated
   */
  List getImage();

  /**
   * Returns the value of the '<em><b>Itemtype</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Itemtype</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Itemtype</em>' attribute.
   * @see #setItemtype(String)
   * @see com.ibm.w3.financing.tools.gcms.query.results.ResultsPackage#getDOCUMENT_Itemtype()
   * @model dataType="org.eclipse.emf.ecore.xml.type.String"
   *        extendedMetaData="kind='attribute' name='itemtype'"
   * @generated
   */
  String getItemtype();

  /**
   * Sets the value of the '{@link com.ibm.w3.financing.tools.gcms.query.results.DOCUMENT#getItemtype <em>Itemtype</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Itemtype</em>' attribute.
   * @see #getItemtype()
   * @generated
   */
  void setItemtype(String value);

  /**
   * Returns the value of the '<em><b>Pid</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Pid</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Pid</em>' attribute.
   * @see #setPid(String)
   * @see com.ibm.w3.financing.tools.gcms.query.results.ResultsPackage#getDOCUMENT_Pid()
   * @model dataType="org.eclipse.emf.ecore.xml.type.String"
   *        extendedMetaData="kind='attribute' name='pid'"
   * @generated
   */
  String getPid();

  /**
   * Sets the value of the '{@link com.ibm.w3.financing.tools.gcms.query.results.DOCUMENT#getPid <em>Pid</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Pid</em>' attribute.
   * @see #getPid()
   * @generated
   */
  void setPid(String value);

} // DOCUMENT
