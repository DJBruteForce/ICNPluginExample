package com.ibm.gil.business;

public class PoexCodeFormSelect  extends FormSelect {
	

	
	public void setLabel(String labl){
		label = value + " - " + labl ;
	}
	
	public String getLabel() {
		return label ;
	}

}
