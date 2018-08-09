package com.ibm.igf.hmvc;

import java.util.HashMap;



import com.ibm.gil.model.CountryPropertiesDataModel;
import com.ibm.igf.gil.CountryProperties;

public class CountryManager {
	
	private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(CountryPropertiesDataModel.class);
	
	private static HashMap<String, CountryProperties> countryPropertiesMap;
	private static HashMap<String, CountryProperties> getCountryPropertiesMap() {

		if (countryPropertiesMap == null){
			
			CountryPropertiesDataModel countryPropertiesModel = new CountryPropertiesDataModel();
			countryPropertiesMap = countryPropertiesModel.selectAllCountryProperties();
			return countryPropertiesMap;
			
		} else {
			
			return countryPropertiesMap;
		}
			
	}
	
	public static CountryProperties getCountryProperties(String country) {
		
		logger.debug("Getting properties for Country: " + country);
		if(country=="UK"){
			country="GB";
		}
		CountryProperties props = new CountryProperties();
		
		if (countryPropertiesMap == null){
			
			countryPropertiesMap = getCountryPropertiesMap();
			props = countryPropertiesMap.get(country);
			
		} else {
			
			props = countryPropertiesMap.get(country);
		}
		
		logger.debug("Properties:" +props.toString());
		return props;
			
	}
		
	public static void loadCountryPropertiesMap() {
		
		CountryPropertiesDataModel countryPropertiesModel = new CountryPropertiesDataModel();
		countryPropertiesMap = countryPropertiesModel.selectAllCountryProperties();
			
	}

	
}
