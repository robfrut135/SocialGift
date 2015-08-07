package org.mobilecloud.capstone.potlach.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;

import org.mobilecloud.capstone.potlach.client.R;
import org.mobilecloud.capstone.potlach.common.repository.Gift;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

public class ChainActivity extends Activity {
	
	@InjectView(R.id.gridGiftsChain)
	protected GridView gridGiftsChain_;
	
	private String chainId;
	private MenuItem menuItem;
		
	@OnClick(R.id.addGiftButton)
	public void addGift() {
		Intent intent = new Intent(ChainActivity.this, GiftActivity.class);		
		intent.putExtra("chainId", chainId);
		startActivity(intent);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chain);
		ButterKnife.inject(this);	
		
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME  | ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_CUSTOM);
	}
	
	@Override
	protected void onResume() {
		super.onResume();		
		refreshChain();
		
		chainId = getIntent().getStringExtra("chainId");
	}	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.activity_chain, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {	    	
	    	case R.id.menu_load:
	    	      menuItem = item;
	    	      menuItem.setActionView(R.layout.progressbar);
	    	      menuItem.expandActionView();	
	    	      refreshChain();
	    	      break;
	    	default:
	    		break;
	    }	
	    return false;
	}
	
	private void refreshChain() {
		
		CallableTask.invoke(new Callable<Collection<Gift>>() {

			@Override
			public Collection<Gift> call() throws Exception {
				Collection<String> giftsId = PotlachContext.getInstance().getChainSvcApi().getChainById(chainId).getGifts();
				
				Collection<Gift> gifts = new ArrayList<Gift>();
				for (String id: giftsId){
					gifts.add(PotlachContext.getInstance().getGiftSvcApi().findGiftById(id));
				}
				
				return gifts;				
			}
		}, new TaskCallback<Collection<Gift>>() {
			
			@Override
			public void success(Collection<Gift> result) {				
				gridGiftsChain_.setAdapter(new ChainAdapter(ChainActivity.this, result));
				
				if (menuItem!=null){
					menuItem.collapseActionView();
					menuItem.setActionView(null);
				}
			}

			@Override
			public void error(Exception e) {
				Toast.makeText(ChainActivity.this, "Unable to fetch the chain.", Toast.LENGTH_SHORT).show();					
			}
		});		
	}
}
