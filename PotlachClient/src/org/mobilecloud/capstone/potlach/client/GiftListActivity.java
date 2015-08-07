package org.mobilecloud.capstone.potlach.client;

import java.util.Collection;
import java.util.concurrent.Callable;

import org.mobilecloud.capstone.potlach.client.CallableTask;
import org.mobilecloud.capstone.potlach.client.R;
import org.mobilecloud.capstone.potlach.client.TaskCallback;
import org.mobilecloud.capstone.potlach.common.repository.Gift;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class GiftListActivity extends Activity {
	private static final String TAG 			= "GiftListActivity";  
	
	public static GiftListActivity giftListActivity;
	
	@InjectView(R.id.giftList)
	protected ListView giftList_;
	
	@InjectView(R.id.searchText)
	protected EditText searchText_;
	
	private MenuItem menuItem;		
	
	private PendingIntent pendingIntent;	
	
	private GiftListAdapter adapter;
	
	@OnClick(R.id.addGiftButton)
	public void addGift() {
		Intent intent = new Intent(GiftListActivity.this, GiftActivity.class);
		intent.putExtra("chainId", "");
		startActivity(intent);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gift_list);
		ButterKnife.inject(this);				
		
		/*
		 * ADAPTER
		 */
		adapter = new GiftListAdapter(GiftListActivity.this);
		giftList_.setAdapter(adapter);		
		
		searchText_.addTextChangedListener(new TextWatcher() {							        
	        @Override
	        public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {			        	
	        	refreshGifts();
	        }
	         
	        @Override
	        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
	            // TODO Auto-generated method stub			             
	        }
	         
	        @Override
	        public void afterTextChanged(Editable arg0) {
	        	// TODO Auto-generated method stub                          
        	}
	    });
		
		/*
		 * ACTION BAR
		 */
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME  | ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_CUSTOM);	
	    
	    /*
	     * ALARM
	     */
	    Intent alarmIntent = new Intent(GiftListActivity.this, UpdatesReceiver.class);	    
        pendingIntent = PendingIntent.getBroadcast(GiftListActivity.this, 0, alarmIntent, 0);
	}
	
	@Override
	protected void onResume() {
		super.onResume();			
		
		giftListActivity = this;
		
		refreshGifts();		
		
		start();
	}	
	
	@Override
	protected void onPause() {
	    super.onPause();	    
	    cancel();	    
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.activity_gift_list, menu);
	    return true;
	}
	
	private String getTitleToSearch(){
		return searchText_.getText().toString();
	}
	
	private void start() {                      
        Log.i(TAG, "FrecuencyUpdates: " + PotlachContext.getInstance().getUser().getFrecuencyUpdateFlags());
        
    	long start 	= System.currentTimeMillis()+getInterval();
    	        
    	AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, start, getInterval(), pendingIntent);       
    }

    private void cancel() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);        
    }	
    
    private long getInterval(){
    	long interval = 0; 	    	
    	switch (PotlachContext.getInstance().getUser().getFrecuencyUpdateFlags()){
	    	case MINUTE:
	    	{
	    		interval = 1000 * 60 * 1;  //1minuto
	    		break;
	    	}	    	
	    	case FIVE_MINUTES:
	    	{
	    		interval = 1000 * 60 * 5;  //5minutos
	    		break;
	    	}
	    	case HOUR:
	    	{
	    		interval = 1000 * 60 * 60;  //60minutos
	    		break;
	    	}
    	}    	
    	return interval;
    }
	  
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    	case R.id.menu_settings_account:
	    		startActivity(new Intent(GiftListActivity.this, SettingsActivity.class));
	    		break;
	    	case R.id.menu_settings_new_gift:
	    		startActivity(new Intent(GiftListActivity.this, GiftActivity.class));
	    		break;
	    	case R.id.menu_settings_gift_givers:
	    		startActivity(new Intent(GiftListActivity.this, GiftGiversListActivity.class));
	    		break;
	    	case R.id.menu_settings_user_givers:
	    		startActivity(new Intent(GiftListActivity.this, UserGiversListActivity.class));
	    		break;
	    	case R.id.menu_load:
	    	      menuItem = item;
	    	      menuItem.setActionView(R.layout.progressbar);
	    	      menuItem.expandActionView();	
	    	      
	    	      Toast.makeText(GiftListActivity.this, "Updating Gift list...", Toast.LENGTH_LONG).show();
	    	      
	    	      refreshGifts();
	    	      
	    	      cancel();
	    	      start();
	    	      break;
	    	default:
	    		break;
	    }	
	    return false;
	}

	public void refreshGifts() {
		
		CallableTask.invoke(new Callable<Collection<Gift>>() {

			@Override
			public Collection<Gift> call() throws Exception {
				if (getTitleToSearch().length()>0){
					return PotlachContext.getInstance().getGiftSvcApi().findGiftByTitle(getTitleToSearch());
				}else{
					return PotlachContext.getInstance().getGiftSvcApi().getGiftList();
				}
			}
		}, new TaskCallback<Collection<Gift>>() {
			
			@Override
			public void success(Collection<Gift> result) {																									
					
				Gift current = adapter.getItem(giftList_.getSelectedItemPosition());
				if (current!=null)
					Log.i(TAG, "GetCurrent: " + current.getId());
				
				adapter.updateGifts(result);
				Log.i(TAG, "updateGifts...");
					
				if (current!=null){
					Log.i(TAG, "new position: " + adapter.getPosition(current.getId()));
					giftList_.smoothScrollToPosition(adapter.getPosition(current.getId()));																
				}
				
				if (menuItem!=null){
					menuItem.collapseActionView();
					menuItem.setActionView(null);
				}								
			}

			@Override
			public void error(Exception e) {				
				Toast.makeText(GiftListActivity.this, "Unable to fetch the video list, please login again.", Toast.LENGTH_SHORT).show();
				startActivity(new Intent(GiftListActivity.this, LoginScreenActivity.class));
			}
		});		
	}				
}
