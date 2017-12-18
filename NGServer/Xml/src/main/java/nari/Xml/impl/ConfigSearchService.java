package nari.Xml.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import nari.Xml.bundle.ConfigLoadManager;
import nari.Xml.bundle.ConfigSearchManager;
import nari.Xml.bundle.module.ConfigModule;
import nari.Xml.bundle.module.ConfigStrategyModule;
import nari.Xml.bundle.module.Strategy;
import nari.Xml.bundle.module.reader.PropertiesStrategyReader;
import nari.Xml.bundle.module.reader.StrategyReader;
import nari.Xml.bundle.module.reader.XMLStrategyReader;
import nari.Xml.interfaces.ConfigSearch;

public class ConfigSearchService implements ConfigSearch {

	public <T> T loadConfigCache(String configName, String configFullPath,Class<T> clazz) {
		ConfigModule module = null;
		StrategyReader reader = null;
		File file = new File(configFullPath);
		if(file!=null && file.exists()){
			InputStream stream = null;
			try {
				stream = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			if(configFullPath.endsWith("xml")){
				module = new ConfigStrategyModule(configName,stream,Strategy.XML,clazz);
				reader = new XMLStrategyReader();
			}else if(configFullPath.endsWith("properties")){
				module = new ConfigStrategyModule(configName,stream,Strategy.PROPERTIES,clazz);
				reader = new PropertiesStrategyReader();
			}
		}
		
		return ConfigLoadManager.load(module,reader);
	}

	public <T> T loadConfigCache(String configName,InputStream inputStream,String postFix,Class<T> clazz) {
		ConfigModule module = null;
		StrategyReader reader = null;
		
		if(inputStream==null){
			return null;
		}
		
		if("xml".equalsIgnoreCase(postFix)){
			module = new ConfigStrategyModule(configName,inputStream,Strategy.XML,clazz);
			reader = new XMLStrategyReader();
		}else if("properties".endsWith(postFix)){
			module = new ConfigStrategyModule(configName,inputStream,Strategy.PROPERTIES,clazz);
			reader = new PropertiesStrategyReader();
		}
		
		return ConfigLoadManager.load(module,reader);
	}
	
	public <T> T search(String configName) {
		return ConfigSearchManager.search(configName);
	}
}
