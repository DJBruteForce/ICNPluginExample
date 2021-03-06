/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.ibm.w3.financing.tools.gcms.query.results.util;

import java.util.Map;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.xmi.util.XMLProcessor;

import com.ibm.w3.financing.tools.gcms.query.results.ResultsPackage;

/**
 * This class contains helper methods to serialize and deserialize XML documents
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ResultsXMLProcessor extends XMLProcessor
{

  /**
   * Public constructor to instantiate the helper.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ResultsXMLProcessor()
  {
    super((EPackage.Registry.INSTANCE));
    ResultsPackage.eINSTANCE.eClass();
  }
  
  /**
   * Register for "*" and "xml" file extensions the ResultsResourceFactoryImpl factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected Map getRegistrations()
  {
    if (registrations == null)
    {
      super.getRegistrations();
      registrations.put(XML_EXTENSION, new ResultsResourceFactoryImpl());
      registrations.put(STAR_EXTENSION, new ResultsResourceFactoryImpl());
    }
    return registrations;
  }

} //ResultsXMLProcessor
