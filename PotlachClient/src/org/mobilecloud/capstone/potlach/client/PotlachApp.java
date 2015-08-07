package org.mobilecloud.capstone.potlach.client;

import android.app.Application;
import butterknife.ButterKnife;

public class PotlachApp extends Application {
  
	@Override 
	public void onCreate() {
		super.onCreate();
		ButterKnife.setDebug(BuildConfig.DEBUG);
	}
}
