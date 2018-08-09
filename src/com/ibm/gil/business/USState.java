package com.ibm.gil.business;

import java.util.ArrayList;
import java.util.List;

import com.ibm.gil.model.USStateDataModel;

public class USState extends Indexing{
	private USStateDataModel usStateDataModel=null;
	private String state="";
	private List<String> dialogErrors=new ArrayList();
	private List<String> stateCodeResults=new ArrayList();
	public USState(String country){
		setCOUNTRY(country);
		usStateDataModel= new USStateDataModel(this);
		
	}
	
	
	public USStateDataModel getUsStateDataModel() {
		if(usStateDataModel==null){
			this.usStateDataModel=new USStateDataModel(this);
		}
		return usStateDataModel;
	}


	public void setUsStateDataModel(USStateDataModel usStateDataModel) {
		this.usStateDataModel = usStateDataModel;
	}


	public List<String> getStateCodeResults() {
		return stateCodeResults;
	}


	public void setStateCodeResults(List<String> stateCodeResults) {
		this.stateCodeResults = stateCodeResults;
	}


	public List<String> getDialogErrors() {
		return dialogErrors;
	}

	public void setSTATE(String state){
		this.state=state;
	}

	public String getSTATE(){
		return this.state;
	}
	private String name="";
	public void setNAME(String name){
		this.name=name;
	}

	public String getNAME(){
		return this.name;
	}
	private String toleranceAmt="";
	public void setTOLERANCEAMT(String toleranceAmt){
		this.toleranceAmt=toleranceAmt;
	}
	
	public String getTOLERANCEAMT(){
		return this.toleranceAmt;
	}
}
