package org.mobilecloud.capstone.potlach.client;

import java.util.concurrent.Callable;

import org.mobilecloud.capstone.potlach.client.R;
import org.mobilecloud.capstone.potlach.common.repository.User;
import org.mobilecloud.capstone.potlach.common.repository.User.FrecuencyUpdates;
import org.mobilecloud.capstone.potlach.common.repository.User.OrderType;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class SettingsActivity extends Activity {

	@InjectView(R.id.name)
	protected EditText name_;
	
	@InjectView(R.id.oldPassword)
	protected EditText oldPassword_;

	@InjectView(R.id.password)
	protected EditText password_;
	
	@InjectView(R.id.questionFrecuency)
	protected RadioGroup questionFrecuency_;
	
	@InjectView(R.id.viewInappropiated)
	protected CheckBox viewInappropiated_;
	
	@InjectView(R.id.questionOrderBy)
	protected RadioGroup questionOrderBy_;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		ButterKnife.inject(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();		
		loadUserSettings();
	}
	
	@OnClick(R.id.settingsButton)
	public void settings() {
		
		PotlachContext.getInstance().getUser().setName(name_.getText().toString());		
		PotlachContext.getInstance().getUser().setPassword(password_.getText().toString());
		
		switch (questionFrecuency_.getCheckedRadioButtonId()) {
			case R.id.refreshMinute:
				PotlachContext.getInstance().getUser().setFrecuencyUpdateFlags(FrecuencyUpdates.MINUTE);
				break;
			case R.id.refreshFiveMinute:
				PotlachContext.getInstance().getUser().setFrecuencyUpdateFlags(FrecuencyUpdates.FIVE_MINUTES);
				break;
			case R.id.refreshHourly:
				PotlachContext.getInstance().getUser().setFrecuencyUpdateFlags(FrecuencyUpdates.HOUR);
				break;
		} 
		
		switch (questionOrderBy_.getCheckedRadioButtonId()) {
			case R.id.orderGiftsByDate:
				PotlachContext.getInstance().getUser().setOrderByGifts(OrderType.DATE);
				break;
			case R.id.orderGiftsByLikes:
				PotlachContext.getInstance().getUser().setOrderByGifts(OrderType.MOST_TOUCHED);
				break;			
		} 		
		
		PotlachContext.getInstance().getUser().setViewInappropiatedGift(viewInappropiated_.isChecked());
		
		
		CallableTask.invoke(new Callable<User>() {

			@Override
			public User call() throws Exception {
				User userPrivate = PotlachContext.getInstance().getUserSvcApi().saveUser(PotlachContext.getInstance().getUser());												
				return userPrivate;
			}
		}, new TaskCallback<User>() {

			@Override
			public void success(User result) {								
				PotlachContext.getInstance().setUser(result);
				
				Toast.makeText( SettingsActivity.this, "Save settings OK.", Toast.LENGTH_SHORT).show();
				
				startActivity(new Intent(SettingsActivity.this, GiftListActivity.class));					
			}

			@Override
			public void error(Exception e) {
				Toast.makeText( SettingsActivity.this, "Save settings KO.", Toast.LENGTH_SHORT).show();					
			}
		});	
	}
	
	private void loadUserSettings(){
		name_.setText(PotlachContext.getInstance().getUser().getName());
		viewInappropiated_.setChecked(PotlachContext.getInstance().getUser().getViewInappropiatedGift());
		
		switch (PotlachContext.getInstance().getUser().getFrecuencyUpdateFlags()) {
			case MINUTE:
				questionFrecuency_.check(R.id.refreshMinute);
				break;
			case FIVE_MINUTES:
				questionFrecuency_.check(R.id.refreshFiveMinute);
				break;
			case HOUR:
				questionFrecuency_.check(R.id.refreshHourly);
				break;
			default:
				break;
		} 
	
		switch (PotlachContext.getInstance().getUser().getOrderByGifts()) {
			case MOST_TOUCHED:
				questionOrderBy_.check(R.id.orderGiftsByLikes);
				break;
			case DATE:
				questionOrderBy_.check(R.id.orderGiftsByDate);
				break;			
		} 	
	}
}
