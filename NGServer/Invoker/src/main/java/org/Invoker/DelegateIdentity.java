package org.Invoker;

import java.io.Serializable;

public class DelegateIdentity extends Identity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6056861923586177683L;

	private String content = "";
	
	private Identity ident = null;
	
	public DelegateIdentity(Identity ident){
		this.ident = ident;
	}
	
	public void addContent(String content){
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public Identity getIdent() {
		return ident;
	}
	
}
