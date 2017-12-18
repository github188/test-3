package com.application.plugin;

public class Version {
	
	private String value = null;
	
	public Version(String value){
		this.value = value;
	}
	
	public Version(Version version){
		this.value = version.getVersion();
	}
	
	public static Version version(String version){
		return new Version(version);
	}
	
	public static Version defaultVersion(){
		return new Version("0");
	}
	
	public String getVersion(){
		return value;
	}
	
}
