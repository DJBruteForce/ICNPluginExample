package com.ibm.gil.business;

import java.sql.ResultSet;

import java.util.ArrayList;



import com.ibm.gil.model.OEMProductDataModel;
import com.ibm.gil.util.ComboBoxEquipmentType;


public class OEMProduct extends Indexing {
	
	private String IBMorLenovoEquip;
    private String EquipmentType;
    private String ManufacturerName;
    private String GHZMHZ;
    private String INCH;
    private String GBMB;
    private String Processor;
    private String Type;
    private String Model;    
    private String ProductDescription;
    private ArrayList<FormSelect> equipmentTypeList;
    
    private ArrayList<FormSelect> manufacturersNameList;
    
    private ArrayList<OEMProduct> oemTableList;
    
    OEMProductDataModel oemProductDataModel ;
    
    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(OEMProduct.class);
  
    public void init()
    {
        
        setEquipmentType("");
        setManufacturerName("");
        setGBMB("");
        setGHZMHZ("");
        setINCH("");
        setProcessor("");
        setIBMorLenovoEquip("N");
    }

   public OEMProduct(){
		
	}
   
   OEMProductDataModel oEMProductDataModel=null;
   
   public OEMProductDataModel getOEMProductDataModel() {
	   	
	   	if (oEMProductDataModel == null){
	   		return new OEMProductDataModel(this);
	   	} else {
	   		return oEMProductDataModel;
	   	}
		}
	
	public OEMProduct(String country) {
			this.setCOUNTRY(country);
			oemProductDataModel = new OEMProductDataModel(this);
		}


	public String getIBMorLenovoEquip() {
		return IBMorLenovoEquip;
	}




	public void setIBMorLenovoEquip(String iBMorLenovoEquip) {
		IBMorLenovoEquip = iBMorLenovoEquip;
	}




	public String getEquipmentType() {
		return EquipmentType;
	}




	public void setEquipmentType(String equipmentType) {
		EquipmentType = equipmentType;
	}




	public String getManufacturerName() {
		return ManufacturerName;
	}




	public void setManufacturerName(String manufacturerName) {
		ManufacturerName = manufacturerName;
	}




	public String getGHZMHZ() {
		return GHZMHZ;
	}




	public void setGHZMHZ(String gHZMHZ) {
		GHZMHZ = gHZMHZ;
	}




	public String getINCH() {
		return INCH;
	}




	public void setINCH(String iNCH) {
		INCH = iNCH;
	}




	public String getGBMB() {
		return GBMB;
	}




	public void setGBMB(String gBMB) {
		GBMB = gBMB;
	}




	public String getProcessor() {
		return Processor;
	}




	public void setProcessor(String processor) {
		Processor = processor;
	}




	public String getType() {
		return Type;
	}




	public void setType(String type) {
		Type = type;
	}




	public String getModel() {
		return Model;
	}




	public void setModel(String model) {
		Model = model;
	}




	public String getProductDescription() {
		return ProductDescription;
	}




	public void setProductDescription(String productDescription) {
		ProductDescription = productDescription;
	}
    
	
	public ArrayList<OEMProduct> searchByProductInfo(){
    	
    	ArrayList<OEMProduct> oemproductList  = null;
        try
        {	
    		 
    		OEMProductDataModel aDataModel = this.getOEMProductDataModel();        	
    		oemproductList = aDataModel.getTypeMods();
        	logger.debug("after getting results in searchByProductInfo ");      	
        	
     
        } catch (Exception exc)
        {
          
            logger.debug("Error searching for Product Information"+exc);
        }
        
        return oemproductList ;
	}	

    /*
     * initialize the model
     */
    public void initializeModel()
    {
        
         initializeEquipmentType();
         initializeManufacturerList();
 
    }
    private void initializeEquipmentType()
    {
    	String[]  equipmenttype= ComboBoxEquipmentType.values ;
    	
    	ArrayList<FormSelect> equipmentTypes= null;
    	
    	try{
    		
      		
    		FormSelect equipmentType = null;  
    	equipmentTypes = new ArrayList<FormSelect> (); 
    		
    for(String equipment : equipmenttype )	
    	{
    		equipmentType = new FormSelect();     	    
    	        		   	    
    	    equipmentType.setLabel(equipment);
    		equipmentType.setValue(equipment);   		
    		    		
    		if(equipment=="")
    			equipmentType.setSelected(true);
    		
    		equipmentTypes.add(equipmentType);
    	}
    	}
    	catch(Exception exc){
    		logger.debug("Error initializing EquipmentType :"+exc);
    	}
    	
    	this.equipmentTypeList= equipmentTypes;   	
    	
    }
    
    private void initializeManufacturerList()
    {
        try
        {
        	OEMProductDataModel aDataModel = this.getOEMProductDataModel();
       ;

            // load the manufacturer Names
            String manufacturerName = " ";
            ArrayList<FormSelect> manufacturerNames =new ArrayList<FormSelect> ();
       
            
            FormSelect manufacturerNameForm = null;
            /*int i=0;
            if(i==0)
            {
            	manufacturerNameForm = new FormSelect();
            	manufacturerNameForm.setSelected(true);
            	manufacturerNameForm.setLabel(manufacturerName);
            	manufacturerNameForm.setValue(manufacturerName);
            	manufacturerNames.add(manufacturerNameForm);
            	i=i+1;
            }*/
            
             
            ResultSet results = aDataModel.getManufacturers();
            while (results.next())
            {
            	
            	manufacturerNameForm = new FormSelect();
            	
            	manufacturerName = results.getString(1);            	
            	manufacturerNameForm.setLabel(manufacturerName.trim());
            	manufacturerNameForm.setValue(manufacturerName.trim());
            	
            	manufacturerNames.add(manufacturerNameForm);
            	
            	
            }

            manufacturersNameList= manufacturerNames;
            logger.debug("Number of mfgs loaded = " + manufacturerNames.size());
          
        } catch (Exception exc)
        {
           
            logger.debug("Error initializing Manufacturer Names"+exc);
        }
    }

    
}
