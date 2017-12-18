package com.application.plugin;

public class ActivatorGenerator {

	public static Activator newInstance(Class<?> clazz) throws Exception{
		if(clazz==null){
			return null;
		}
		
		return (Activator)clazz.getConstructor().newInstance();
	}
	
}
