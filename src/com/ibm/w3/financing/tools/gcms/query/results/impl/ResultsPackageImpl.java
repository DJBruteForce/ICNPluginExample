/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.ibm.w3.financing.tools.gcms.query.results.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

import com.ibm.w3.financing.tools.gcms.query.results.DocumentRoot;
import com.ibm.w3.financing.tools.gcms.query.results.Image;
import com.ibm.w3.financing.tools.gcms.query.results.Index;
import com.ibm.w3.financing.tools.gcms.query.results.QUERYRESULTSType;
import com.ibm.w3.financing.tools.gcms.query.results.ResultsFactory;
import com.ibm.w3.financing.tools.gcms.query.results.ResultsPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ResultsPackageImpl extends EPackageImpl implements ResultsPackage
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass documentEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass documentRootEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass folderEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass imageEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass indexEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass queryresultsTypeEClass = null;

  /**
   * Creates an instance of the model <b>Package</b>, registered with
   * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
   * package URI value.
   * <p>Note: the correct way to create the package is via the static
   * factory method {@link #init init()}, which also performs
   * initialization of the package, or returns the registered package,
   * if one already exists.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.emf.ecore.EPackage.Registry
   * @see com.ibm.w3.financing.tools.gcms.query.results.ResultsPackage#eNS_URI
   * @see #init()
   * @generated
   */
  private ResultsPackageImpl()
  {
    super(eNS_URI, ResultsFactory.eINSTANCE);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static boolean isInited = false;

  /**
   * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
   * 
   * <p>This method is used to initialize {@link ResultsPackage#eINSTANCE} when that field is accessed.
   * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #eNS_URI
   * @see #createPackageContents()
   * @see #initializePackageContents()
   * @generated
   */
  public static ResultsPackage init()
  {
    if (isInited) return (ResultsPackage)EPackage.Registry.INSTANCE.getEPackage(ResultsPackage.eNS_URI);

    // Obtain or create and register package
    ResultsPackageImpl theResultsPackage = (ResultsPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof ResultsPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new ResultsPackageImpl());

    isInited = true;

    // Initialize simple dependencies
    XMLTypePackage.eINSTANCE.eClass();

    // Create package meta-data objects
    theResultsPackage.createPackageContents();

    // Initialize created meta-data
    theResultsPackage.initializePackageContents();

    // Mark meta-data to indicate it can't be changed
    theResultsPackage.freeze();

  
    // Update the registry and return the package
    EPackage.Registry.INSTANCE.put(ResultsPackage.eNS_URI, theResultsPackage);
    return theResultsPackage;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getDOCUMENT()
  {
    return documentEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getDOCUMENT_Index()
  {
    return (EReference)documentEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getDOCUMENT_Image()
  {
    return (EReference)documentEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDOCUMENT_Itemtype()
  {
    return (EAttribute)documentEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDOCUMENT_Pid()
  {
    return (EAttribute)documentEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getDocumentRoot()
  {
    return documentRootEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getDocumentRoot_Mixed()
  {
    return (EAttribute)documentRootEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getDocumentRoot_XMLNSPrefixMap()
  {
    return (EReference)documentRootEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getDocumentRoot_XSISchemaLocation()
  {
    return (EReference)documentRootEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getDocumentRoot_QUERYRESULTS()
  {
    return (EReference)documentRootEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getFOLDER()
  {
    return folderEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getFOLDER_Index()
  {
    return (EReference)folderEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getFOLDER_DOCUMENT()
  {
    return (EReference)folderEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getFOLDER_Itemtype()
  {
    return (EAttribute)folderEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getFOLDER_Pid()
  {
    return (EAttribute)folderEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getImage()
  {
    return imageEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getImage_Created()
  {
    return (EAttribute)imageEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getImage_Mimetype()
  {
    return (EAttribute)imageEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getImage_Orgfilename()
  {
    return (EAttribute)imageEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getImage_Partnum()
  {
    return (EAttribute)imageEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getImage_Size()
  {
    return (EAttribute)imageEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIndex()
  {
    return indexEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getIndex_Field()
  {
    return (EAttribute)indexEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getIndex_Name()
  {
    return (EAttribute)indexEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getIndex_Value()
  {
    return (EAttribute)indexEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getQUERYRESULTSType()
  {
    return queryresultsTypeEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getQUERYRESULTSType_DOCUMENT()
  {
    return (EReference)queryresultsTypeEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getQUERYRESULTSType_FOLDER()
  {
    return (EReference)queryresultsTypeEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ResultsFactory getResultsFactory()
  {
    return (ResultsFactory)getEFactoryInstance();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private boolean isCreated = false;

  /**
   * Creates the meta-model objects for the package.  This method is
   * guarded to have no affect on any invocation but its first.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void createPackageContents()
  {
    if (isCreated) return;
    isCreated = true;

    // Create classes and their features
    documentEClass = createEClass(DOCUMENT);
    createEReference(documentEClass, DOCUMENT__INDEX);
    createEReference(documentEClass, DOCUMENT__IMAGE);
    createEAttribute(documentEClass, DOCUMENT__ITEMTYPE);
    createEAttribute(documentEClass, DOCUMENT__PID);

    documentRootEClass = createEClass(DOCUMENT_ROOT);
    createEAttribute(documentRootEClass, DOCUMENT_ROOT__MIXED);
    createEReference(documentRootEClass, DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
    createEReference(documentRootEClass, DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
    createEReference(documentRootEClass, DOCUMENT_ROOT__QUERYRESULTS);

    folderEClass = createEClass(FOLDER);
    createEReference(folderEClass, FOLDER__INDEX);
    createEReference(folderEClass, FOLDER__DOCUMENT);
    createEAttribute(folderEClass, FOLDER__ITEMTYPE);
    createEAttribute(folderEClass, FOLDER__PID);

    imageEClass = createEClass(IMAGE);
    createEAttribute(imageEClass, IMAGE__CREATED);
    createEAttribute(imageEClass, IMAGE__MIMETYPE);
    createEAttribute(imageEClass, IMAGE__ORGFILENAME);
    createEAttribute(imageEClass, IMAGE__PARTNUM);
    createEAttribute(imageEClass, IMAGE__SIZE);

    indexEClass = createEClass(INDEX);
    createEAttribute(indexEClass, INDEX__FIELD);
    createEAttribute(indexEClass, INDEX__NAME);
    createEAttribute(indexEClass, INDEX__VALUE);

    queryresultsTypeEClass = createEClass(QUERYRESULTS_TYPE);
    createEReference(queryresultsTypeEClass, QUERYRESULTS_TYPE__DOCUMENT);
    createEReference(queryresultsTypeEClass, QUERYRESULTS_TYPE__FOLDER);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private boolean isInitialized = false;

  /**
   * Complete the initialization of the package and its meta-model.  This
   * method is guarded to have no affect on any invocation but its first.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void initializePackageContents()
  {
    if (isInitialized) return;
    isInitialized = true;

    // Initialize package
    setName(eNAME);
    setNsPrefix(eNS_PREFIX);
    setNsURI(eNS_URI);

    // Obtain other dependent packages
    XMLTypePackage theXMLTypePackage = (XMLTypePackage)EPackage.Registry.INSTANCE.getEPackage(XMLTypePackage.eNS_URI);

    // Add supertypes to classes

    // Initialize classes and features; add operations and parameters
    initEClass(documentEClass, com.ibm.w3.financing.tools.gcms.query.results.DOCUMENT.class, "DOCUMENT", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getDOCUMENT_Index(), this.getIndex(), null, "index", null, 1, -1, com.ibm.w3.financing.tools.gcms.query.results.DOCUMENT.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getDOCUMENT_Image(), this.getImage(), null, "image", null, 0, -1, com.ibm.w3.financing.tools.gcms.query.results.DOCUMENT.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getDOCUMENT_Itemtype(), theXMLTypePackage.getString(), "itemtype", null, 0, 1, com.ibm.w3.financing.tools.gcms.query.results.DOCUMENT.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getDOCUMENT_Pid(), theXMLTypePackage.getString(), "pid", null, 0, 1, com.ibm.w3.financing.tools.gcms.query.results.DOCUMENT.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(documentRootEClass, DocumentRoot.class, "DocumentRoot", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getDocumentRoot_Mixed(), ecorePackage.getEFeatureMapEntry(), "mixed", null, 0, -1, null, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getDocumentRoot_XMLNSPrefixMap(), ecorePackage.getEStringToStringMapEntry(), null, "xMLNSPrefixMap", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getDocumentRoot_XSISchemaLocation(), ecorePackage.getEStringToStringMapEntry(), null, "xSISchemaLocation", null, 0, -1, null, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getDocumentRoot_QUERYRESULTS(), this.getQUERYRESULTSType(), null, "qUERYRESULTS", null, 0, -2, null, IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

    initEClass(folderEClass, com.ibm.w3.financing.tools.gcms.query.results.FOLDER.class, "FOLDER", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getFOLDER_Index(), this.getIndex(), null, "index", null, 1, -1, com.ibm.w3.financing.tools.gcms.query.results.FOLDER.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getFOLDER_DOCUMENT(), this.getDOCUMENT(), null, "dOCUMENT", null, 0, -1, com.ibm.w3.financing.tools.gcms.query.results.FOLDER.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getFOLDER_Itemtype(), theXMLTypePackage.getString(), "itemtype", null, 0, 1, com.ibm.w3.financing.tools.gcms.query.results.FOLDER.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getFOLDER_Pid(), theXMLTypePackage.getString(), "pid", null, 0, 1, com.ibm.w3.financing.tools.gcms.query.results.FOLDER.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(imageEClass, Image.class, "Image", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getImage_Created(), theXMLTypePackage.getString(), "created", null, 0, 1, Image.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getImage_Mimetype(), theXMLTypePackage.getString(), "mimetype", null, 0, 1, Image.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getImage_Orgfilename(), theXMLTypePackage.getString(), "orgfilename", null, 0, 1, Image.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getImage_Partnum(), theXMLTypePackage.getString(), "partnum", null, 0, 1, Image.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getImage_Size(), theXMLTypePackage.getString(), "size", null, 0, 1, Image.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(indexEClass, Index.class, "Index", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getIndex_Field(), theXMLTypePackage.getString(), "field", null, 0, 1, Index.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getIndex_Name(), theXMLTypePackage.getString(), "name", null, 0, 1, Index.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getIndex_Value(), theXMLTypePackage.getString(), "value", null, 0, 1, Index.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(queryresultsTypeEClass, QUERYRESULTSType.class, "QUERYRESULTSType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getQUERYRESULTSType_DOCUMENT(), this.getDOCUMENT(), null, "dOCUMENT", null, 0, -1, QUERYRESULTSType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getQUERYRESULTSType_FOLDER(), this.getFOLDER(), null, "fOLDER", null, 0, -1, QUERYRESULTSType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    // Create resource
    createResource(eNS_URI);

    // Create annotations
    // http:///org/eclipse/emf/ecore/util/ExtendedMetaData
    createExtendedMetaDataAnnotations();
  }

  /**
   * Initializes the annotations for <b>http:///org/eclipse/emf/ecore/util/ExtendedMetaData</b>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void createExtendedMetaDataAnnotations()
  {
    String source = "http:///org/eclipse/emf/ecore/util/ExtendedMetaData";		
    addAnnotation
      (documentEClass, 
       source, 
       new String[] 
       {
       "name", "DOCUMENT",
       "kind", "elementOnly"
       });		
    addAnnotation
      (getDOCUMENT_Index(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "index"
       });		
    addAnnotation
      (getDOCUMENT_Image(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "image"
       });		
    addAnnotation
      (getDOCUMENT_Itemtype(), 
       source, 
       new String[] 
       {
       "kind", "attribute",
       "name", "itemtype"
       });		
    addAnnotation
      (getDOCUMENT_Pid(), 
       source, 
       new String[] 
       {
       "kind", "attribute",
       "name", "pid"
       });		
    addAnnotation
      (documentRootEClass, 
       source, 
       new String[] 
       {
       "name", "",
       "kind", "mixed"
       });		
    addAnnotation
      (getDocumentRoot_Mixed(), 
       source, 
       new String[] 
       {
       "kind", "elementWildcard",
       "name", ":mixed"
       });		
    addAnnotation
      (getDocumentRoot_XMLNSPrefixMap(), 
       source, 
       new String[] 
       {
       "kind", "attribute",
       "name", "xmlns:prefix"
       });		
    addAnnotation
      (getDocumentRoot_XSISchemaLocation(), 
       source, 
       new String[] 
       {
       "kind", "attribute",
       "name", "xsi:schemaLocation"
       });		
    addAnnotation
      (getDocumentRoot_QUERYRESULTS(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "QUERYRESULTS",
       "namespace", "##targetNamespace"
       });		
    addAnnotation
      (folderEClass, 
       source, 
       new String[] 
       {
       "name", "FOLDER",
       "kind", "elementOnly"
       });		
    addAnnotation
      (getFOLDER_Index(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "index"
       });		
    addAnnotation
      (getFOLDER_DOCUMENT(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "DOCUMENT"
       });		
    addAnnotation
      (getFOLDER_Itemtype(), 
       source, 
       new String[] 
       {
       "kind", "attribute",
       "name", "itemtype"
       });		
    addAnnotation
      (getFOLDER_Pid(), 
       source, 
       new String[] 
       {
       "kind", "attribute",
       "name", "pid"
       });		
    addAnnotation
      (imageEClass, 
       source, 
       new String[] 
       {
       "name", "Image",
       "kind", "empty"
       });		
    addAnnotation
      (getImage_Created(), 
       source, 
       new String[] 
       {
       "kind", "attribute",
       "name", "created"
       });		
    addAnnotation
      (getImage_Mimetype(), 
       source, 
       new String[] 
       {
       "kind", "attribute",
       "name", "mimetype"
       });		
    addAnnotation
      (getImage_Orgfilename(), 
       source, 
       new String[] 
       {
       "kind", "attribute",
       "name", "orgfilename"
       });		
    addAnnotation
      (getImage_Partnum(), 
       source, 
       new String[] 
       {
       "kind", "attribute",
       "name", "partnum"
       });		
    addAnnotation
      (getImage_Size(), 
       source, 
       new String[] 
       {
       "kind", "attribute",
       "name", "size"
       });		
    addAnnotation
      (indexEClass, 
       source, 
       new String[] 
       {
       "name", "Index",
       "kind", "empty"
       });		
    addAnnotation
      (getIndex_Field(), 
       source, 
       new String[] 
       {
       "kind", "attribute",
       "name", "field"
       });		
    addAnnotation
      (getIndex_Name(), 
       source, 
       new String[] 
       {
       "kind", "attribute",
       "name", "name"
       });		
    addAnnotation
      (getIndex_Value(), 
       source, 
       new String[] 
       {
       "kind", "attribute",
       "name", "value"
       });		
    addAnnotation
      (queryresultsTypeEClass, 
       source, 
       new String[] 
       {
       "name", "QUERYRESULTS_._type",
       "kind", "elementOnly"
       });		
    addAnnotation
      (getQUERYRESULTSType_DOCUMENT(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "DOCUMENT"
       });		
    addAnnotation
      (getQUERYRESULTSType_FOLDER(), 
       source, 
       new String[] 
       {
       "kind", "element",
       "name", "FOLDER"
       });
  }

} //ResultsPackageImpl
