package com.pivotallabs.toolbox;

import com.google.inject.Singleton;

@Singleton
public class UserGuid {

	private final String uid;
	
	public UserGuid(String uid) {
		if(uid == null || uid.length() == 0){
			throw new IllegalArgumentException("UID can not be null or empty");
		}
		this.uid = uid;
	}

	public String getGuid(){
		return uid;
	}
	
}
