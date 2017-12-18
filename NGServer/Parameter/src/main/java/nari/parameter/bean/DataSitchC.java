package nari.parameter.bean;

import java.lang.reflect.Field;

import nari.Logger.Logger;
import nari.Logger.LoggerManager;

public class DataSitchC {
	
	private Logger logger = LoggerManager.getLogger(this.getClass());
	
	public <T> String arrayToString(T[] t,String separatorChar){
		StringBuffer cParameter = new StringBuffer();
		if(t == null || t.length == 0){
			logger.info("数据转换出错");
		}
		for(int i=0;i<t.length;i++){
			if(i==0){
				cParameter.append(String.valueOf(t[i]));
			}else{
				cParameter.append(separatorChar);
				cParameter.append(String.valueOf(t[i]));
			}
		}
		String arrayToString = cParameter.toString();
		return arrayToString;
	}
	
	//将属性整合变成String
	public <T> String propertyToString(T t,String[] donotPrint,String splitChar){
		Field[] fields = t.getClass().getDeclaredFields();
		StringBuffer cParameter = new StringBuffer();
		for(int i=0;i<fields.length;i++){
			fields[i].setAccessible(true);
			String fieldName = fields[i].getName();
			//选择不打印的属性
			boolean flag = false;
			if(donotPrint != null){
				for(int j=0;j<donotPrint.length;j++){
					if(fieldName.equalsIgnoreCase(donotPrint[j])){
						flag = true;
					}
				}
			}
			if(flag){
				continue;
			}
			Object fieldValue = null;
			//将属性分离成一个字符串
			try {
				fieldValue = String.valueOf(fields[i].get(t));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				logger.info("属性转字符串出错");
				e.printStackTrace();
			}
			if(i == 0){
				cParameter.append(fieldValue);
			}else{
				cParameter.append(splitChar);
				cParameter.append(fieldValue);	
			}
		}
		String ObjectToString = cParameter.toString();
		return ObjectToString;
	}
	
	//将String分发至属性
	public <T> T StringToProperty(String propertyString,T t,String[] donotSet,String splitChar){
		String[] properties = propertyString.split(splitChar);
		Field[] fields = t.getClass().getDeclaredFields();
		for(int i=0;i<fields.length;i++){
			fields[i].setAccessible(true);
			String fieldName = fields[i].getName();
			//选择不塞入的属性
			boolean flag = false;
			if(donotSet != null){
				for(int j=0;j<donotSet.length;j++){
					if(fieldName.equalsIgnoreCase(donotSet[j])){
						flag = true;
					}
				}
			}
			if(flag){
				continue;
			}
			try {
				fields[i].set(t, properties);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return t;
	}
	
}
