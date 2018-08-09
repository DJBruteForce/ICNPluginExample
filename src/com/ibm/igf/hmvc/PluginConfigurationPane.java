package com.ibm.igf.hmvc;


import java.util.HashMap;
import java.util.Iterator;



import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ibm.ecm.extension.PluginServiceCallbacks;



public class PluginConfigurationPane {
	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(PluginConfigurationPane.class);
	private static HashMap<String, String> propertiesMap;
	
	
	public static HashMap<String, String> getPropertiesMap() {
	
			return propertiesMap;
			
	}
	
	public static HashMap<String, String> getPropertiesMap(PluginServiceCallbacks callbacks) throws Exception {
		if (propertiesMap == null){
			loadPropertiesMap(callbacks.loadConfiguration());
		}
		
		return propertiesMap;
		
	}
	
	public static void loadPropertiesMap(String configuration) {
		if(configuration.equals("")){
			//nothing to load
			logger.debug("configuration string is empty, hence nothing to do");
		}else{
			
			JsonObject jsonPluginPropertiesPane = new JsonParser().parse(configuration).getAsJsonObject();
			
			JsonArray arr = jsonPluginPropertiesPane.get("configuration").getAsJsonArray();
			
			Iterator it = arr.iterator();
			propertiesMap= new HashMap<String, String>();
			while(it.hasNext()){
				JsonObject config = (JsonObject)it.next();
				String name=config.get("name").getAsString();
				String value=config.get("value").getAsString();
				
					logger.info("Loading: "+name);
					logger.info("Loading:****"+value+"****");
					
				
				propertiesMap.put(name, value);
			
				

			}
			
			
			logger.info("Finished loading Configuration Panel properties.");
		}


			
	}
	
	
	

}
