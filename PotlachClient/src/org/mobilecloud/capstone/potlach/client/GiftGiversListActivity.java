package org.mobilecloud.capstone.potlach.client;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.Callable;

import org.mobilecloud.capstone.potlach.common.repository.GiversInfo;

public class GiftGiversListActivity extends FragmentActivity {
  
	@InjectView(R.id.list_of_things) 
	protected TableLayout listGivers;  
	
	@InjectView(R.id.title_givers_list) 
	protected TextView title_;
	
	@InjectView(R.id.tv_11) 
	protected TextView columnKey;
	
	private MenuItem menuItem;
  
	@Override 
	protected void onCreate(Bundle savedInstanceState) {	  
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_givers_list);
		ButterKnife.inject(this);  

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME  | ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_CUSTOM);
	}	  
  
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_givers_list, menu);
		return true;
	}
  
	@Override
	protected void onResume() {
	 	super.onResume();		
	 	
	 	title_.setText("Top gift givers");
	 	columnKey.setText("Gift");
	 	
	 	refreshGiftGiversList();
	}
  
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {	    	
	    	case R.id.menu_load:
	    	      menuItem = item;
	    	      menuItem.setActionView(R.layout.progressbar);
	    	      menuItem.expandActionView();	
	    	      refreshGiftGiversList();
	    	      break;
	    	default:
	    		break;
	    }	
	    return false;
	}
  
	private void refreshGiftGiversList() {
	  
		CallableTask.invoke(new Callable<Collection<GiversInfo>>() {

			@Override
			public Collection<GiversInfo> call() throws Exception {
				return PotlachContext.getInstance().getFlagSvcApi().getGiftGiversInfo();
			}
		}, new TaskCallback<Collection<GiversInfo>>() {
			
			@Override
			public void success(Collection<GiversInfo> result) {
				loadTable(result);	
				
				if (menuItem!=null){
					menuItem.collapseActionView();
					menuItem.setActionView(null);
				}
			}

			@Override
			public void error(Exception e) {
				Toast.makeText(GiftGiversListActivity.this, "Top gift givers not loaded.", Toast.LENGTH_SHORT).show();					
			}
		});      
  	}
  
	private void loadTable(Collection<GiversInfo> giversInfo){
		listGivers.removeAllViews();
		
		TableRow row;
		TextView t1, t2, t3;     
		int dip = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) 1, getResources().getDisplayMetrics());

		Iterator<GiversInfo> iteratorGiversInfo = giversInfo.iterator();
		while(iteratorGiversInfo.hasNext()){
			GiversInfo gInfo = iteratorGiversInfo.next();
    	  
			row = new TableRow(this);

			t1 = new TextView(this);         
			t2 = new TextView(this);           
			t3 = new TextView(this);    
          
			t1.setText(gInfo.name);
			t2.setText(String.valueOf(gInfo.numTouched));
			t3.setText(String.valueOf(gInfo.numInnapropiated));

			t1.setTypeface(null, 1);
			t2.setTypeface(null, 1);
			t3.setTypeface(null, 1);

			t1.setTextSize(12);
			t2.setTextSize(12);
			t3.setTextSize(12);

			t1.setWidth(150 * dip);
			t2.setWidth(70 * dip);
			t3.setWidth(70 * dip);
          
			t1.setPadding(20*dip, 0, 0, 0);
			t2.setPadding(15*dip, 0, 0, 0);
			t3.setPadding(20*dip, 0, 0, 0);
          
			row.addView(t1);
			row.addView(t2);
			row.addView(t3);
			
			listGivers.addView(row, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		}
	}
}
