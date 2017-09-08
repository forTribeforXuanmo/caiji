package com.caiji.datasource;


public enum DBTypeEnum {

	LOCAL("local");
	
	private String value;
	DBTypeEnum(String value){
		this.value=value;
	}
	
	public String getValue(){
		
		return this.value;
	}
	
}
