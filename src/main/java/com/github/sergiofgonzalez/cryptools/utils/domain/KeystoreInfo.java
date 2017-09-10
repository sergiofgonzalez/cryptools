package com.github.sergiofgonzalez.cryptools.utils.domain;

public class KeystoreInfo {
	private String path;
	private String type;
	private String passwd;
		
	protected KeystoreInfo() {		
	}

	public KeystoreInfo(String path, String type, String passwd) {
		this.path = path;
		this.type = type;
		this.passwd = passwd;
	}

	public String getPath() {
		return path;
	}

	public String getType() {
		return type;
	}

	public String getPasswd() {
		return passwd;
	}

	@Override
	public String toString() {
		return "KeystoreInfo [path=" + path + ", type=" + type + ", passwd=" + passwd + "]";
	}	
}
