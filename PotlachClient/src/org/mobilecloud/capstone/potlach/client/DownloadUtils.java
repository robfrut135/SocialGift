package org.mobilecloud.capstone.potlach.client;

import java.io.File;

import org.mobilecloud.capstone.potlach.common.repository.User;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class DownloadUtils {
    
    static final String TAG 					= "DownloadUtils";    
	public static final String MESSENGER_KEY 	= "MESSENGER";	
	public static final String PATHNAME_KEY 	= "PATHNAME";	
	public static final String GIFT_KEY 		= "GIFTID";
	public static final String USER_KEY 		= "USERNAME";
	public static final String PASS_KEY 		= "PASSWORD";	
    
    public static Intent makeMessengerIntent(Context context,  Class<?> service, Handler handler, String giftId) {
    	Messenger messenger = new Messenger(handler);
    	
    	Intent intent = new Intent(context,  service);
    	intent.putExtra(MESSENGER_KEY,  messenger);
    	intent.putExtra(GIFT_KEY, giftId);
    	intent.putExtra(USER_KEY, PotlachContext.getInstance().getUser().getUsername());
    	intent.putExtra(PASS_KEY, PotlachContext.getInstance().getUser().getPassword());
    	    	
        return intent;
    }

    public static void sendPath (String outputPath, Messenger messenger) {
        Message msg = Message.obtain();
        Bundle data = new Bundle();
        data.putString(PATHNAME_KEY, outputPath);
        
        // Make the Bundle the "data" of the Message.
        msg.setData(data);
        
        try {
            // Send the Message back to the client Activity.
            messenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
   
    public static void downloadAndRespond(	final Context context, 
    										final Messenger messenger, 
    										final String giftId, 
    										final String username,
    										final String password) {
    	    	    	
    	try{    
    		Log.i(TAG, "downloadAndRespond: " + username + " - " + giftId);
    		
    		String pathToReturn	= null;
			File file 			= Utils.getFileGift(context, giftId); 						
			
			if (file.length()==0){		
				User user = new User();
				user.setUsername(username);
				user.setPassword(password);			
				PotlachContext.getInstance().setUser(user);
				
				Utils.saveStreamGift(file, PotlachContext.getInstance().getGiftSvcApi().getData(giftId).getBody().in());
	            
	            pathToReturn = file.getAbsolutePath();
            }else if (file.length()>0){
            	pathToReturn = file.getAbsolutePath();            	
            }
			
			Log.i(TAG, "Sendpath " + pathToReturn);
			
			sendPath(pathToReturn, messenger);	

		}catch (Exception e) {				
			Log.e(TAG, "Exception while downloading. Returning null.");
            Log.e(TAG, e.toString());
            e.printStackTrace();
            sendPath(null, messenger);			
		};	
    }              
}
