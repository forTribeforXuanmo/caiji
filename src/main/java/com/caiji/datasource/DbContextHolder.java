package com.caiji.datasource;


public class DbContextHolder {
	public  static final ThreadLocal<String> contextHoler=new ThreadLocal<String>();
	///设置当前数据源
	public static void setDbType(DBTypeEnum dbTypeEnum){
		contextHoler.set(dbTypeEnum.getValue());
	}
	//取得当前数据源
	public static  String  getDbType(){
		
		return (String) contextHoler.get();
	}
	//清空
	public static void  remove (){
		
		contextHoler.remove();
	}
	
}
