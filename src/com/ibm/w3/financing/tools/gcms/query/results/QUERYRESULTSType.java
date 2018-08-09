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
 * A representation of the model object '<em><b>QUERYRESULTS Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.query.results.QUERYRESULTSType#getDOCUMENT <em>DOCUMENT</em>}</li>
 *   <li>{@link com.ibm.w3.financing.tools.gcms.query.results.QUERYRESULTSType#getFOLDER <em>FOLDER</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.ibm.w3.financing.tools.gcms.query.results.ResultsPackage#getQUERYRESULTSType()
 * @model extendedMetaData="name='QUERYRESULTS_._type' kind='elementOnly'"
 * @generated
 */
public interface QUERYRESULTSType
{
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
   * @see com.ibm.w3.financing.tools.gcms.query.results.ResultsPackage#getQUERYRESULTSType_DOCUMENT()
   * @model type="com.ibm.w3.financing.tools.gcms.query.results.DOCUMENT" containment="true"
   *        extendedMetaData="kind='element' name='DOCUMENT'"
   * @generated
   */
  List getDOCUMENT();

  /**
   * Returns the value of the '<em><b>FOLDER</b></em>' containment reference list.
   * The list contents are of type {@link com.ibm.w3.financing.tools.gcms.query.results.FOLDER}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>FOLDER</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>FOLDER</em>' containment reference list.
   * @see com.ibm.w3.financing.tools.gcms.query.results.ResultsPackage#getQUERYRESULTSType_FOLDER()
   * @model type="com.ibm.w3.financing.tools.gcms.query.results.FOLDER" containment="true"
   *        extendedMetaData="kind='element' name='FOLDER'"
   * @generated
   */
  List getFOLDER();

} // QUERYRESULTSType
