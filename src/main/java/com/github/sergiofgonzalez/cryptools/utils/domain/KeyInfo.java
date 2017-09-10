package com.github.sergiofgonzalez.cryptools.utils.domain;

public class KeyInfo {
	private KeystoreInfo keystoreInfo;
	private String alias;
	private String type;
	private String passwd;	
	
	protected KeyInfo() {		
	}
	
	public KeyInfo(KeystoreInfo keyStoreInfo, String alias, String type, String passwd) {
		this.keystoreInfo = keyStoreInfo;
		this.alias = alias;
		this.type = type;
		this.passwd = passwd;
	}	
	
	public KeystoreInfo getKeystoreInfo() {
		return keystoreInfo;
	}

	public String getAlias() {
		return alias;
	}

	public String getType() {
		return type;
	}

	public String getPasswd() {
		return passwd;
	}

	@Override
	public String toString() {
		return "KeyInfo [keystoreInfo=" + keystoreInfo + ", alias=" + alias + ", type=" + type + ", passwd=" + passwd
				+ "]";
	}
}
