package com.ibm.igf.gil;

import org.springframework.context.ApplicationContext;

public class SpringApplicationContext {
	
    private static ApplicationContext instance;

	public static ApplicationContext getInstance() {
		return instance;
	}

	public void setSpringApplicationContext(ApplicationContext instance) {
		SpringApplicationContext.instance = instance;
	}


    
    
}
