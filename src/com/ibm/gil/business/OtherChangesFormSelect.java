package com.ibm.gil.business;

public class OtherChangesFormSelect extends FormSelect {
	
	public void setLabel(String labl){
		if(value.trim().length()==0 && labl.trim().length()==0){
			label = "" ;
		}else{
			label = value + " - " + labl ;
		}
		
	}
	
	public String getLabel() {
		return label ;
	}
}
