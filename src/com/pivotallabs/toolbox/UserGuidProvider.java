package com.pivotallabs.toolbox;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

@Singleton
public class UserGuidProvider implements Provider<UserGuid> {

	private final GuidGenerator gen;
	
	@Inject
	public UserGuidProvider( GuidGenerator guid ) {
		gen = guid;
	}

	@Override
	public UserGuid get() {
		return new UserGuid(gen.getGuid());
	}

	
	
}
