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

public class RegisterActivity extends Activity {

	@InjectView(R.id.name)
	protected EditText name_;
	
	@InjectView(R.id.userName)
	protected EditText userName_;

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
		setContentView(R.layout.activity_register);
		ButterKnife.inject(this);
	}
	
	@OnClick(R.id.registerButton)
	public void register() {		
						
		final User newUser = new User(name_.getText().toString(), userName_.getText().toString(), password_.getText().toString());
		
		switch (questionFrecuency_.getCheckedRadioButtonId()) {
			case R.id.refreshMinute:
				newUser.setFrecuencyUpdateFlags(FrecuencyUpdates.MINUTE);
				break;
			case R.id.refreshFiveMinute:
				newUser.setFrecuencyUpdateFlags(FrecuencyUpdates.FIVE_MINUTES);
				break;
			case R.id.refreshHourly:
				newUser.setFrecuencyUpdateFlags(FrecuencyUpdates.HOUR);
				break;
		} 
		
		switch (questionOrderBy_.getCheckedRadioButtonId()) {
			case R.id.orderGiftsByDate:
				newUser.setOrderByGifts(OrderType.DATE);
				break;
			case R.id.orderGiftsByLikes:
				newUser.setOrderByGifts(OrderType.MOST_TOUCHED);
				break;			
		} 		
		
		newUser.setViewInappropiatedGift(viewInappropiated_.isChecked());
		
		
		CallableTask.invoke(new Callable<User>() {

			@Override
			public User call() throws Exception {
				User userPrivate = PotlachSvc.getUserSvcApi().addUser(newUser);												
				return userPrivate;
			}
		}, new TaskCallback<User>() {

			@Override
			public void success(User result) {								
				PotlachContext.getInstance().setUser(result);
				
				startActivity(new Intent(RegisterActivity.this, GiftListActivity.class));					
			}

			@Override
			public void error(Exception e) {
				Toast.makeText( RegisterActivity.this, "Register KO.", Toast.LENGTH_SHORT).show();					
			}
		});		
	}
}
