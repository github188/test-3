package com.application.plugin;

public class AttributeKey {

	private String name = null;
	
	private Class<?> clazz = null;
	
	private AttributeKey(Class<?> clazz,Version version){
		this.name = clazz.getName()+"."+version.getVersion();
		this.clazz = clazz;
	}
	
	public String getKeyName(){
		return this.name;
	}
	
	public Class<?> getKeyClass(){
		return this.clazz;
	}
	
	public static AttributeKey key(Class<?> clazz,Version version){
		return new AttributeKey(clazz,version);
	}
	
	@Override
	public int hashCode() {
		return 37+17*(name==null?0:name.hashCode());
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if(!(obj instanceof AttributeKey)){
			return false;
		}
		
		if(this==obj){
			return true;
		}
		
		AttributeKey other = (AttributeKey)obj;
		return name.equals(other.getKeyName());
	}
}
