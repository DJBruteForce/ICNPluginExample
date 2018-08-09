/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.ibm.w3.financing.tools.gcms.query.results;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see com.ibm.w3.financing.tools.gcms.query.results.ResultsFactory
 * @model kind="package"
 * @generated
 */
public interface ResultsPackage extends EPackage
{
  /**
   * The package name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNAME = "results";

  /**
   * The package namespace URI.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_URI = "http://w3.ibm.com/financing/tools/GCMS/QueryResults";

  /**
   * The package namespace name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_PREFIX = "results";

  /**
   * The singleton instance of the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  ResultsPackage eINSTANCE = com.ibm.w3.financing.tools.gcms.query.results.impl.ResultsPackageImpl.init();

  /**
   * The meta object id for the '{@link com.ibm.w3.financing.tools.gcms.query.results.impl.DOCUMENTImpl <em>DOCUMENT</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.ibm.w3.financing.tools.gcms.query.results.impl.DOCUMENTImpl
   * @see com.ibm.w3.financing.tools.gcms.query.results.impl.ResultsPackageImpl#getDOCUMENT()
   * @generated
   */
  int DOCUMENT = 0;

  /**
   * The feature id for the '<em><b>Index</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DOCUMENT__INDEX = 0;

  /**
   * The feature id for the '<em><b>Image</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DOCUMENT__IMAGE = 1;

  /**
   * The feature id for the '<em><b>Itemtype</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DOCUMENT__ITEMTYPE = 2;

  /**
   * The feature id for the '<em><b>Pid</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DOCUMENT__PID = 3;

  /**
   * The number of structural features of the '<em>DOCUMENT</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DOCUMENT_FEATURE_COUNT = 4;

  /**
   * The meta object id for the '{@link com.ibm.w3.financing.tools.gcms.query.results.impl.DocumentRootImpl <em>Document Root</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.ibm.w3.financing.tools.gcms.query.results.impl.DocumentRootImpl
   * @see com.ibm.w3.financing.tools.gcms.query.results.impl.ResultsPackageImpl#getDocumentRoot()
   * @generated
   */
  int DOCUMENT_ROOT = 1;

  /**
   * The feature id for the '<em><b>Mixed</b></em>' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DOCUMENT_ROOT__MIXED = 0;

  /**
   * The feature id for the '<em><b>XMLNS Prefix Map</b></em>' map.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DOCUMENT_ROOT__XMLNS_PREFIX_MAP = 1;

  /**
   * The feature id for the '<em><b>XSI Schema Location</b></em>' map.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DOCUMENT_ROOT__XSI_SCHEMA_LOCATION = 2;

  /**
   * The feature id for the '<em><b>QUERYRESULTS</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DOCUMENT_ROOT__QUERYRESULTS = 3;

  /**
   * The number of structural features of the '<em>Document Root</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int DOCUMENT_ROOT_FEATURE_COUNT = 4;

  /**
   * The meta object id for the '{@link com.ibm.w3.financing.tools.gcms.query.results.impl.FOLDERImpl <em>FOLDER</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.ibm.w3.financing.tools.gcms.query.results.impl.FOLDERImpl
   * @see com.ibm.w3.financing.tools.gcms.query.results.impl.ResultsPackageImpl#getFOLDER()
   * @generated
   */
  int FOLDER = 2;

  /**
   * The feature id for the '<em><b>Index</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FOLDER__INDEX = 0;

  /**
   * The feature id for the '<em><b>DOCUMENT</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FOLDER__DOCUMENT = 1;

  /**
   * The feature id for the '<em><b>Itemtype</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FOLDER__ITEMTYPE = 2;

  /**
   * The feature id for the '<em><b>Pid</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FOLDER__PID = 3;

  /**
   * The number of structural features of the '<em>FOLDER</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int FOLDER_FEATURE_COUNT = 4;

  /**
   * The meta object id for the '{@link com.ibm.w3.financing.tools.gcms.query.results.impl.ImageImpl <em>Image</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.ibm.w3.financing.tools.gcms.query.results.impl.ImageImpl
   * @see com.ibm.w3.financing.tools.gcms.query.results.impl.ResultsPackageImpl#getImage()
   * @generated
   */
  int IMAGE = 3;

  /**
   * The feature id for the '<em><b>Created</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IMAGE__CREATED = 0;

  /**
   * The feature id for the '<em><b>Mimetype</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IMAGE__MIMETYPE = 1;

  /**
   * The feature id for the '<em><b>Orgfilename</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IMAGE__ORGFILENAME = 2;

  /**
   * The feature id for the '<em><b>Partnum</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IMAGE__PARTNUM = 3;

  /**
   * The feature id for the '<em><b>Size</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IMAGE__SIZE = 4;

  /**
   * The number of structural features of the '<em>Image</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int IMAGE_FEATURE_COUNT = 5;

  /**
   * The meta object id for the '{@link com.ibm.w3.financing.tools.gcms.query.results.impl.IndexImpl <em>Index</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.ibm.w3.financing.tools.gcms.query.results.impl.IndexImpl
   * @see com.ibm.w3.financing.tools.gcms.query.results.impl.ResultsPackageImpl#getIndex()
   * @generated
   */
  int INDEX = 4;

  /**
   * The feature id for the '<em><b>Field</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INDEX__FIELD = 0;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INDEX__NAME = 1;

  /**
   * The feature id for the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INDEX__VALUE = 2;

  /**
   * The number of structural features of the '<em>Index</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int INDEX_FEATURE_COUNT = 3;

  /**
   * The meta object id for the '{@link com.ibm.w3.financing.tools.gcms.query.results.impl.QUERYRESULTSTypeImpl <em>QUERYRESULTS Type</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see com.ibm.w3.financing.tools.gcms.query.results.impl.QUERYRESULTSTypeImpl
   * @see com.ibm.w3.financing.tools.gcms.query.results.impl.ResultsPackageImpl#getQUERYRESULTSType()
   * @generated
   */
  int QUERYRESULTS_TYPE = 5;

  /**
   * The feature id for the '<em><b>DOCUMENT</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QUERYRESULTS_TYPE__DOCUMENT = 0;

  /**
   * The feature id for the '<em><b>FOLDER</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QUERYRESULTS_TYPE__FOLDER = 1;

  /**
   * The number of structural features of the '<em>QUERYRESULTS Type</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int QUERYRESULTS_TYPE_FEATURE_COUNT = 2;


  /**
   * Returns the meta object for class '{@link com.ibm.w3.financing.tools.gcms.query.results.DOCUMENT <em>DOCUMENT</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>DOCUMENT</em>'.
   * @see com.ibm.w3.financing.tools.gcms.query.results.DOCUMENT
   * @generated
   */
  EClass getDOCUMENT();

  /**
   * Returns the meta object for the containment reference list '{@link com.ibm.w3.financing.tools.gcms.query.results.DOCUMENT#getIndex <em>Index</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Index</em>'.
   * @see com.ibm.w3.financing.tools.gcms.query.results.DOCUMENT#getIndex()
   * @see #getDOCUMENT()
   * @generated
   */
  EReference getDOCUMENT_Index();

  /**
   * Returns the meta object for the containment reference list '{@link com.ibm.w3.financing.tools.gcms.query.results.DOCUMENT#getImage <em>Image</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Image</em>'.
   * @see com.ibm.w3.financing.tools.gcms.query.results.DOCUMENT#getImage()
   * @see #getDOCUMENT()
   * @generated
   */
  EReference getDOCUMENT_Image();

  /**
   * Returns the meta object for the attribute '{@link com.ibm.w3.financing.tools.gcms.query.results.DOCUMENT#getItemtype <em>Itemtype</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Itemtype</em>'.
   * @see com.ibm.w3.financing.tools.gcms.query.results.DOCUMENT#getItemtype()
   * @see #getDOCUMENT()
   * @generated
   */
  EAttribute getDOCUMENT_Itemtype();

  /**
   * Returns the meta object for the attribute '{@link com.ibm.w3.financing.tools.gcms.query.results.DOCUMENT#getPid <em>Pid</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Pid</em>'.
   * @see com.ibm.w3.financing.tools.gcms.query.results.DOCUMENT#getPid()
   * @see #getDOCUMENT()
   * @generated
   */
  EAttribute getDOCUMENT_Pid();

  /**
   * Returns the meta object for class '{@link com.ibm.w3.financing.tools.gcms.query.results.DocumentRoot <em>Document Root</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Document Root</em>'.
   * @see com.ibm.w3.financing.tools.gcms.query.results.DocumentRoot
   * @generated
   */
  EClass getDocumentRoot();

  /**
   * Returns the meta object for the attribute list '{@link com.ibm.w3.financing.tools.gcms.query.results.DocumentRoot#getMixed <em>Mixed</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute list '<em>Mixed</em>'.
   * @see com.ibm.w3.financing.tools.gcms.query.results.DocumentRoot#getMixed()
   * @see #getDocumentRoot()
   * @generated
   */
  EAttribute getDocumentRoot_Mixed();

  /**
   * Returns the meta object for the map '{@link com.ibm.w3.financing.tools.gcms.query.results.DocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the map '<em>XMLNS Prefix Map</em>'.
   * @see com.ibm.w3.financing.tools.gcms.query.results.DocumentRoot#getXMLNSPrefixMap()
   * @see #getDocumentRoot()
   * @generated
   */
  EReference getDocumentRoot_XMLNSPrefixMap();

  /**
   * Returns the meta object for the map '{@link com.ibm.w3.financing.tools.gcms.query.results.DocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the map '<em>XSI Schema Location</em>'.
   * @see com.ibm.w3.financing.tools.gcms.query.results.DocumentRoot#getXSISchemaLocation()
   * @see #getDocumentRoot()
   * @generated
   */
  EReference getDocumentRoot_XSISchemaLocation();

  /**
   * Returns the meta object for the containment reference '{@link com.ibm.w3.financing.tools.gcms.query.results.DocumentRoot#getQUERYRESULTS <em>QUERYRESULTS</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>QUERYRESULTS</em>'.
   * @see com.ibm.w3.financing.tools.gcms.query.results.DocumentRoot#getQUERYRESULTS()
   * @see #getDocumentRoot()
   * @generated
   */
  EReference getDocumentRoot_QUERYRESULTS();

  /**
   * Returns the meta object for class '{@link com.ibm.w3.financing.tools.gcms.query.results.FOLDER <em>FOLDER</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>FOLDER</em>'.
   * @see com.ibm.w3.financing.tools.gcms.query.results.FOLDER
   * @generated
   */
  EClass getFOLDER();

  /**
   * Returns the meta object for the containment reference list '{@link com.ibm.w3.financing.tools.gcms.query.results.FOLDER#getIndex <em>Index</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Index</em>'.
   * @see com.ibm.w3.financing.tools.gcms.query.results.FOLDER#getIndex()
   * @see #getFOLDER()
   * @generated
   */
  EReference getFOLDER_Index();

  /**
   * Returns the meta object for the containment reference list '{@link com.ibm.w3.financing.tools.gcms.query.results.FOLDER#getDOCUMENT <em>DOCUMENT</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>DOCUMENT</em>'.
   * @see com.ibm.w3.financing.tools.gcms.query.results.FOLDER#getDOCUMENT()
   * @see #getFOLDER()
   * @generated
   */
  EReference getFOLDER_DOCUMENT();

  /**
   * Returns the meta object for the attribute '{@link com.ibm.w3.financing.tools.gcms.query.results.FOLDER#getItemtype <em>Itemtype</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Itemtype</em>'.
   * @see com.ibm.w3.financing.tools.gcms.query.results.FOLDER#getItemtype()
   * @see #getFOLDER()
   * @generated
   */
  EAttribute getFOLDER_Itemtype();

  /**
   * Returns the meta object for the attribute '{@link com.ibm.w3.financing.tools.gcms.query.results.FOLDER#getPid <em>Pid</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Pid</em>'.
   * @see com.ibm.w3.financing.tools.gcms.query.results.FOLDER#getPid()
   * @see #getFOLDER()
   * @generated
   */
  EAttribute getFOLDER_Pid();

  /**
   * Returns the meta object for class '{@link com.ibm.w3.financing.tools.gcms.query.results.Image <em>Image</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Image</em>'.
   * @see com.ibm.w3.financing.tools.gcms.query.results.Image
   * @generated
   */
  EClass getImage();

  /**
   * Returns the meta object for the attribute '{@link com.ibm.w3.financing.tools.gcms.query.results.Image#getCreated <em>Created</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Created</em>'.
   * @see com.ibm.w3.financing.tools.gcms.query.results.Image#getCreated()
   * @see #getImage()
   * @generated
   */
  EAttribute getImage_Created();

  /**
   * Returns the meta object for the attribute '{@link com.ibm.w3.financing.tools.gcms.query.results.Image#getMimetype <em>Mimetype</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Mimetype</em>'.
   * @see com.ibm.w3.financing.tools.gcms.query.results.Image#getMimetype()
   * @see #getImage()
   * @generated
   */
  EAttribute getImage_Mimetype();

  /**
   * Returns the meta object for the attribute '{@link com.ibm.w3.financing.tools.gcms.query.results.Image#getOrgfilename <em>Orgfilename</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Orgfilename</em>'.
   * @see com.ibm.w3.financing.tools.gcms.query.results.Image#getOrgfilename()
   * @see #getImage()
   * @generated
   */
  EAttribute getImage_Orgfilename();

  /**
   * Returns the meta object for the attribute '{@link com.ibm.w3.financing.tools.gcms.query.results.Image#getPartnum <em>Partnum</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Partnum</em>'.
   * @see com.ibm.w3.financing.tools.gcms.query.results.Image#getPartnum()
   * @see #getImage()
   * @generated
   */
  EAttribute getImage_Partnum();

  /**
   * Returns the meta object for the attribute '{@link com.ibm.w3.financing.tools.gcms.query.results.Image#getSize <em>Size</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Size</em>'.
   * @see com.ibm.w3.financing.tools.gcms.query.results.Image#getSize()
   * @see #getImage()
   * @generated
   */
  EAttribute getImage_Size();

  /**
   * Returns the meta object for class '{@link com.ibm.w3.financing.tools.gcms.query.results.Index <em>Index</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Index</em>'.
   * @see com.ibm.w3.financing.tools.gcms.query.results.Index
   * @generated
   */
  EClass getIndex();

  /**
   * Returns the meta object for the attribute '{@link com.ibm.w3.financing.tools.gcms.query.results.Index#getField <em>Field</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Field</em>'.
   * @see com.ibm.w3.financing.tools.gcms.query.results.Index#getField()
   * @see #getIndex()
   * @generated
   */
  EAttribute getIndex_Field();

  /**
   * Returns the meta object for the attribute '{@link com.ibm.w3.financing.tools.gcms.query.results.Index#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see com.ibm.w3.financing.tools.gcms.query.results.Index#getName()
   * @see #getIndex()
   * @generated
   */
  EAttribute getIndex_Name();

  /**
   * Returns the meta object for the attribute '{@link com.ibm.w3.financing.tools.gcms.query.results.Index#getValue <em>Value</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Value</em>'.
   * @see com.ibm.w3.financing.tools.gcms.query.results.Index#getValue()
   * @see #getIndex()
   * @generated
   */
  EAttribute getIndex_Value();

  /**
   * Returns the meta object for class '{@link com.ibm.w3.financing.tools.gcms.query.results.QUERYRESULTSType <em>QUERYRESULTS Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>QUERYRESULTS Type</em>'.
   * @see com.ibm.w3.financing.tools.gcms.query.results.QUERYRESULTSType
   * @generated
   */
  EClass getQUERYRESULTSType();

  /**
   * Returns the meta object for the containment reference list '{@link com.ibm.w3.financing.tools.gcms.query.results.QUERYRESULTSType#getDOCUMENT <em>DOCUMENT</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>DOCUMENT</em>'.
   * @see com.ibm.w3.financing.tools.gcms.query.results.QUERYRESULTSType#getDOCUMENT()
   * @see #getQUERYRESULTSType()
   * @generated
   */
  EReference getQUERYRESULTSType_DOCUMENT();

  /**
   * Returns the meta object for the containment reference list '{@link com.ibm.w3.financing.tools.gcms.query.results.QUERYRESULTSType#getFOLDER <em>FOLDER</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>FOLDER</em>'.
   * @see com.ibm.w3.financing.tools.gcms.query.results.QUERYRESULTSType#getFOLDER()
   * @see #getQUERYRESULTSType()
   * @generated
   */
  EReference getQUERYRESULTSType_FOLDER();

  /**
   * Returns the factory that creates the instances of the model.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the factory that creates the instances of the model.
   * @generated
   */
  ResultsFactory getResultsFactory();

  /**
   * <!-- begin-user-doc -->
   * Defines literals for the meta objects that represent
   * <ul>
   *   <li>each class,</li>
   *   <li>each feature of each class,</li>
   *   <li>each enum,</li>
   *   <li>and each data type</li>
   * </ul>
   * <!-- end-user-doc -->
   * @generated
   */
  interface Literals
  {
    /**
     * The meta object literal for the '{@link com.ibm.w3.financing.tools.gcms.query.results.impl.DOCUMENTImpl <em>DOCUMENT</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.ibm.w3.financing.tools.gcms.query.results.impl.DOCUMENTImpl
     * @see com.ibm.w3.financing.tools.gcms.query.results.impl.ResultsPackageImpl#getDOCUMENT()
     * @generated
     */
    EClass DOCUMENT = eINSTANCE.getDOCUMENT();

    /**
     * The meta object literal for the '<em><b>Index</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference DOCUMENT__INDEX = eINSTANCE.getDOCUMENT_Index();

    /**
     * The meta object literal for the '<em><b>Image</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference DOCUMENT__IMAGE = eINSTANCE.getDOCUMENT_Image();

    /**
     * The meta object literal for the '<em><b>Itemtype</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DOCUMENT__ITEMTYPE = eINSTANCE.getDOCUMENT_Itemtype();

    /**
     * The meta object literal for the '<em><b>Pid</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DOCUMENT__PID = eINSTANCE.getDOCUMENT_Pid();

    /**
     * The meta object literal for the '{@link com.ibm.w3.financing.tools.gcms.query.results.impl.DocumentRootImpl <em>Document Root</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.ibm.w3.financing.tools.gcms.query.results.impl.DocumentRootImpl
     * @see com.ibm.w3.financing.tools.gcms.query.results.impl.ResultsPackageImpl#getDocumentRoot()
     * @generated
     */
    EClass DOCUMENT_ROOT = eINSTANCE.getDocumentRoot();

    /**
     * The meta object literal for the '<em><b>Mixed</b></em>' attribute list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute DOCUMENT_ROOT__MIXED = eINSTANCE.getDocumentRoot_Mixed();

    /**
     * The meta object literal for the '<em><b>XMLNS Prefix Map</b></em>' map feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference DOCUMENT_ROOT__XMLNS_PREFIX_MAP = eINSTANCE.getDocumentRoot_XMLNSPrefixMap();

    /**
     * The meta object literal for the '<em><b>XSI Schema Location</b></em>' map feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference DOCUMENT_ROOT__XSI_SCHEMA_LOCATION = eINSTANCE.getDocumentRoot_XSISchemaLocation();

    /**
     * The meta object literal for the '<em><b>QUERYRESULTS</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference DOCUMENT_ROOT__QUERYRESULTS = eINSTANCE.getDocumentRoot_QUERYRESULTS();

    /**
     * The meta object literal for the '{@link com.ibm.w3.financing.tools.gcms.query.results.impl.FOLDERImpl <em>FOLDER</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.ibm.w3.financing.tools.gcms.query.results.impl.FOLDERImpl
     * @see com.ibm.w3.financing.tools.gcms.query.results.impl.ResultsPackageImpl#getFOLDER()
     * @generated
     */
    EClass FOLDER = eINSTANCE.getFOLDER();

    /**
     * The meta object literal for the '<em><b>Index</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference FOLDER__INDEX = eINSTANCE.getFOLDER_Index();

    /**
     * The meta object literal for the '<em><b>DOCUMENT</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference FOLDER__DOCUMENT = eINSTANCE.getFOLDER_DOCUMENT();

    /**
     * The meta object literal for the '<em><b>Itemtype</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute FOLDER__ITEMTYPE = eINSTANCE.getFOLDER_Itemtype();

    /**
     * The meta object literal for the '<em><b>Pid</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute FOLDER__PID = eINSTANCE.getFOLDER_Pid();

    /**
     * The meta object literal for the '{@link com.ibm.w3.financing.tools.gcms.query.results.impl.ImageImpl <em>Image</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.ibm.w3.financing.tools.gcms.query.results.impl.ImageImpl
     * @see com.ibm.w3.financing.tools.gcms.query.results.impl.ResultsPackageImpl#getImage()
     * @generated
     */
    EClass IMAGE = eINSTANCE.getImage();

    /**
     * The meta object literal for the '<em><b>Created</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute IMAGE__CREATED = eINSTANCE.getImage_Created();

    /**
     * The meta object literal for the '<em><b>Mimetype</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute IMAGE__MIMETYPE = eINSTANCE.getImage_Mimetype();

    /**
     * The meta object literal for the '<em><b>Orgfilename</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute IMAGE__ORGFILENAME = eINSTANCE.getImage_Orgfilename();

    /**
     * The meta object literal for the '<em><b>Partnum</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute IMAGE__PARTNUM = eINSTANCE.getImage_Partnum();

    /**
     * The meta object literal for the '<em><b>Size</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute IMAGE__SIZE = eINSTANCE.getImage_Size();

    /**
     * The meta object literal for the '{@link com.ibm.w3.financing.tools.gcms.query.results.impl.IndexImpl <em>Index</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.ibm.w3.financing.tools.gcms.query.results.impl.IndexImpl
     * @see com.ibm.w3.financing.tools.gcms.query.results.impl.ResultsPackageImpl#getIndex()
     * @generated
     */
    EClass INDEX = eINSTANCE.getIndex();

    /**
     * The meta object literal for the '<em><b>Field</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute INDEX__FIELD = eINSTANCE.getIndex_Field();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute INDEX__NAME = eINSTANCE.getIndex_Name();

    /**
     * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute INDEX__VALUE = eINSTANCE.getIndex_Value();

    /**
     * The meta object literal for the '{@link com.ibm.w3.financing.tools.gcms.query.results.impl.QUERYRESULTSTypeImpl <em>QUERYRESULTS Type</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see com.ibm.w3.financing.tools.gcms.query.results.impl.QUERYRESULTSTypeImpl
     * @see com.ibm.w3.financing.tools.gcms.query.results.impl.ResultsPackageImpl#getQUERYRESULTSType()
     * @generated
     */
    EClass QUERYRESULTS_TYPE = eINSTANCE.getQUERYRESULTSType();

    /**
     * The meta object literal for the '<em><b>DOCUMENT</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference QUERYRESULTS_TYPE__DOCUMENT = eINSTANCE.getQUERYRESULTSType_DOCUMENT();

    /**
     * The meta object literal for the '<em><b>FOLDER</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference QUERYRESULTS_TYPE__FOLDER = eINSTANCE.getQUERYRESULTSType_FOLDER();

  }

} //ResultsPackage
