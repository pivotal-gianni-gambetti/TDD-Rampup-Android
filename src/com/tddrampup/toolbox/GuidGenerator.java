package com.tddrampup.toolbox;

import java.util.Random;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class GuidGenerator {

	private Random random;
	
	@Inject
	public GuidGenerator(Random r){
		random = r;
	}

	public String getGuid(){
		return Long.toString( random.nextLong() );
	}
	
}
