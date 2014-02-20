package com.pivotallabs.yellowpages;

import com.google.inject.Module;
import com.pivotallabs.yellowpages.injection.InjectionModule;

import roboguice.RoboGuice;
import android.app.Application;

public class App extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		
		RoboGuice.setBaseApplicationInjector(this, RoboGuice.DEFAULT_STAGE, getModules());
	};
	
	private Module[] getModules(){
		return new Module[]{
				RoboGuice.newDefaultRoboModule(this)
				, new InjectionModule()
		};
	}
	
}
