/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.ibm.w3.financing.tools.gcms.query.results.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

import com.ibm.w3.financing.tools.gcms.query.results.DocumentRoot;
import com.ibm.w3.financing.tools.gcms.query.results.ResultsFactory;
import com.ibm.w3.financing.tools.gcms.query.results.ResultsPackage;

/**
 * The utility class for loading and storing SDO instances as XML files.
 * @generated
 */
public class ResultsResourceUtil 
{
  /**
   * The single instance of this class.
   * @generated
   */
  private static ResultsResourceUtil instance;

  /**
   * Return the single instance of this class.
   * @generated
   */  
  public static ResultsResourceUtil getInstance()
  {
  	if (instance == null)
  	{	
  	  instance = new ResultsResourceUtil();
  	}
  	return instance;
  }
  
  /**
   * The default constructor.
   * @generated
   */  
  public ResultsResourceUtil() 
  {
    initialize();
  }

  /**
   * @generated
   */
  private void initialize()
  {
    Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xml", new ResultsResourceFactoryImpl());
    ResultsPackage pkg = ResultsPackage.eINSTANCE;   
    ResultsFactory factory = ResultsFactory.eINSTANCE;
  }

  /**
   * Load an existing XML file.
   * @param filename the absolute path name of the XML file to be loaded.
   * @exception IOException failed loading from an XML file.
   * @return DocumentRoot
   * @generated
   */  
  public DocumentRoot load(String filename) throws IOException
  {
    ResultsResourceImpl resource = (ResultsResourceImpl)(new ResultsResourceFactoryImpl()).createResource(URI.createURI(filename));
    resource.load(null);
    DocumentRoot documentRoot = (DocumentRoot)resource.getContents().get(0);
    return documentRoot;
  }

  /**
   * Load an existing XML file.
   * @param istream the InputStream to load the XML content from.
   * @exception IOException failed loading from an XML file.
   * @return DocumentRoot
   * @generated
   */   
  public DocumentRoot load(InputStream istream) throws IOException
  {
    ResultsResourceImpl resource = (ResultsResourceImpl)(new ResultsResourceFactoryImpl()).createResource(URI.createURI("*.xml"));
    resource.load(istream,null);
    DocumentRoot documentRoot = (DocumentRoot)resource.getContents().get(0);
    return documentRoot;
  }
  
  /**
   * Save as an XML file.
   * @param documentRoot the document root of the SDO instances.
   * @param filename the absolute path name of the XML file to be created.
   * @exception IOException failed storing to an XML file.
   * @generated
   */
  public void save(DocumentRoot documentRoot, String filename) throws IOException
  {
  	ResultsResourceImpl resource = getResultsResourceImpl(documentRoot);
    resource.setURI(URI.createURI(filename));
    if (!resource.getContents().contains(documentRoot))
    { 	
      resource.getContents().add((EObject)documentRoot);
    }  
    resource.setEncoding("UTF-8");
    resource.save(null);
  } 
 
  /**
   * Save as an XML output stream.
   * @param documentRoot the document root of the SDO instances.
   * @param ostream the OutputStream where the XML content is to be written.
   * @exception IOException failed storing to an XML file.
   * @generated
   */ 
  public void save(DocumentRoot documentRoot, OutputStream ostream) throws IOException
  {
  	ResultsResourceImpl resource = getResultsResourceImpl(documentRoot);
    if (!resource.getContents().contains(documentRoot))
    { 	
      resource.getContents().add((EObject)documentRoot);
    }  
    resource.setEncoding("UTF-8");
    resource.save(ostream,null);
  } 
  
  /**
   * Return a resource associated with documentRoot.
   * @param documentRoot the document root of the SDO instances.
   * @return ResultsResourceImpl
   * @generated
   */   
  private ResultsResourceImpl getResultsResourceImpl(DocumentRoot documentRoot)
  {
  	ResultsResourceImpl resource = (ResultsResourceImpl)((EObject)documentRoot).eResource();
    if (resource == null)
      resource = (ResultsResourceImpl)(new ResultsResourceFactoryImpl()).createResource(URI.createURI("*.xml"));
    return resource;    
  }

}
