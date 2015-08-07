package org.mobilecloud.capstone.potlach.client;

import java.util.concurrent.Callable;

import org.mobilecloud.capstone.potlach.client.R;
import org.mobilecloud.capstone.potlach.common.api.UserSvcApi;
import org.mobilecloud.capstone.potlach.common.repository.User;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 
 * This application uses ButterKnife. AndroidStudio has better support for
 * ButterKnife than Eclipse, but Eclipse was used for consistency with the other
 * courses in the series. If you have trouble getting the login button to work,
 * please follow these directions to enable annotation processing for this
 * Eclipse project:
 * 
 * http://jakewharton.github.io/butterknife/ide-eclipse.html
 * 
 */
public class LoginScreenActivity extends Activity {

	@InjectView(R.id.userName)
	protected EditText userName_;

	@InjectView(R.id.password)
	protected EditText password_;

	@OnClick(R.id.registerButton)
	public void newAccount() {
		startActivity(new Intent(LoginScreenActivity.this,RegisterActivity.class));
	}
	
	@OnClick(R.id.loginButton)
	public void login() {						
		final UserSvcApi apiPublic 	= PotlachSvc.getUserSvcApi();
		
		CallableTask.invoke(new Callable<User>() {

			@Override
			public User call() throws Exception {
				User user_login 	= new User("LOGIN", userName_.getText().toString(), password_.getText().toString());
				User userPrivate = apiPublic.login(user_login.getUsername(), user_login.getPassword());												
				return userPrivate;
			}
		}, new TaskCallback<User>() {

			@Override
			public void success(User result) {								
				PotlachContext.getInstance().setUser(result);
				
				startActivity(new Intent(LoginScreenActivity.this, GiftListActivity.class));					
			}

			@Override
			public void error(Exception e) {
				Toast.makeText( LoginScreenActivity.this, "Data access not valid.", Toast.LENGTH_SHORT).show();					
			}
		});					
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_screen);
		ButterKnife.inject(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();		
		
		if (PotlachContext.getInstance().getUser()!=null)
			startActivity(new Intent(LoginScreenActivity.this, GiftListActivity.class));
	}	
}
