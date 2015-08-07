package org.mobilecloud.capstone.potlach.client;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public class UpdatesReceiver extends BroadcastReceiver {	
	
	private static final String TAG 				= "UpdatesReceiver"; 
	
	private static final int MY_NOTIFICATION_ID = 1;
	
	public static final String TITLE_SEARCH_KEY = "title";
	
	private final CharSequence tickerText = "Gifts are updating!";
	private final CharSequence contentTitle = "Updating Gifts";
	private final CharSequence contentText = "Ey! Gift list are updating!!";
	private Intent mNotificationIntent;
	private PendingIntent mContentIntent;
	private Uri soundURI = Uri.parse("android.resource://org.mobilecloud.capstone.potlach.client/"+ R.raw.alarm_rooster);
	private long[] mVibratePattern = { 0, 200, 200, 300 };
	
    @Override
    public void onReceive(Context context, Intent intent) {
    	
    	GiftListActivity.giftListActivity.refreshGifts();
    	
    	Log.i(TAG, "Launch refreshGifts() from UpdatesReceiver...");
                
        showNotification(context);        
        showToast(context);
    }
    
    private void showNotification(Context context) {
    	mNotificationIntent = new Intent(context, GiftListActivity.class);
		mContentIntent = PendingIntent.getActivity(context, 0, mNotificationIntent, Intent.FLAG_ACTIVITY_NEW_TASK);

		Notification.Builder notificationBuilder = new Notification.Builder(
				context).setTicker(tickerText)
				.setSmallIcon(android.R.drawable.stat_sys_warning)
				.setAutoCancel(true).setContentTitle(contentTitle)
				.setContentText(contentText).setContentIntent(mContentIntent)
				.setSound(soundURI).setVibrate(mVibratePattern);
		
		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(MY_NOTIFICATION_ID,notificationBuilder.build());
    } 
    
    private void showToast(Context context){
    	Toast.makeText(context, "Updating Gift list...", Toast.LENGTH_LONG).show();
    }
}
