package org.mobilecloud.capstone.potlach.client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Messenger;
import android.util.Log;

public class ThreadPoolDownloadService extends Service {
	private static final String TAG 		= "ThreadPoolDownloadService";  
	private static final int MAX_THREADS 	= 6;  
        
    ExecutorService mExecutor;

    @Override
	public void onCreate() {      	
        mExecutor = Executors.newFixedThreadPool(MAX_THREADS);                  
    }

    public static Intent makeIntent(Context context, Handler handler, String giftId) {        	    
        return DownloadUtils.makeMessengerIntent(context, ThreadPoolDownloadService.class, handler, giftId);
    }

    @Override
	public int onStartCommand(final Intent intent,  int flags, int startId) {       
        Runnable downloadRunnable = new Runnable() {			
			@Override
			public void run() {					
				DownloadUtils.downloadAndRespond(	getApplicationContext(), 
													(Messenger) intent.getExtras().get(DownloadUtils.MESSENGER_KEY),
													(String)intent.getExtras().get(DownloadUtils.GIFT_KEY), 
													(String)intent.getExtras().get(DownloadUtils.USER_KEY),
													(String)intent.getExtras().get(DownloadUtils.PASS_KEY)
												);
			}
		};
		
		Log.i(TAG, "Call to: mExecutor.execute(downloadRunnable)");
		
        mExecutor.execute(downloadRunnable);        
            
        return START_REDELIVER_INTENT;
    }

    @Override
	public void onDestroy() {    	
        mExecutor.shutdown();
    }

    @Override
	public IBinder onBind (Intent intent) {
        return null;
    }
}
