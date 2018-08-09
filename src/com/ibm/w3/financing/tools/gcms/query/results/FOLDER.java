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
 * A representation of the model object '<em><b>FOLDER</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.query.results.FOLDER#getIndex <em>Index</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.query.results.FOLDER#getDOCUMENT <em>DOCUMENT</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.query.results.FOLDER#getItemtype <em>Itemtype</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.query.results.FOLDER#getPid <em>Pid</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.ibm.w3.financing.tools.gcms.query.results.ResultsPackage#getFOLDER()
 * @model extendedMetaData="name='FOLDER' kind='elementOnly'"
 * @generated
 */
public interface FOLDER
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
   * @see com.ibm.w3.financing.tools.gcms.query.results.ResultsPackage#getFOLDER_Index()
   * @model type="com.ibm.w3.financing.tools.gcms.query.results.Index" containment="true" required="true"
   *        extendedMetaData="kind='element' name='index'"
   * @generated
   */
  List getIndex();

  /**
   * Returns the value of the '<em><b>DOCUMENT</b></em>' containment reference list.
   * The list contents are of type {@link com.ibm.w3.financing.tools.gcms.query.results.DOCUMENT}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>DOCUMENT</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>DOCUMENT</em>' containment reference list.
   * @see com.ibm.w3.financing.tools.gcms.query.results.ResultsPackage#getFOLDER_DOCUMENT()
   * @model type="com.ibm.w3.financing.tools.gcms.query.results.DOCUMENT" containment="true"
   *        extendedMetaData="kind='element' name='DOCUMENT'"
   * @generated
   */
  List getDOCUMENT();

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
   * @see com.ibm.w3.financing.tools.gcms.query.results.ResultsPackage#getFOLDER_Itemtype()
   * @model dataType="org.eclipse.emf.ecore.xml.type.String"
   *        extendedMetaData="kind='attribute' name='itemtype'"
   * @generated
   */
  String getItemtype();

  /**
   * Sets the value of the '{@link com.ibm.w3.financing.tools.gcms.query.results.FOLDER#getItemtype <em>Itemtype</em>}' attribute.
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
   * @see com.ibm.w3.financing.tools.gcms.query.results.ResultsPackage#getFOLDER_Pid()
   * @model dataType="org.eclipse.emf.ecore.xml.type.String"
   *        extendedMetaData="kind='attribute' name='pid'"
   * @generated
   */
  String getPid();

  /**
   * Sets the value of the '{@link com.ibm.w3.financing.tools.gcms.query.results.FOLDER#getPid <em>Pid</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Pid</em>' attribute.
   * @see #getPid()
   * @generated
   */
  void setPid(String value);

} // FOLDER
