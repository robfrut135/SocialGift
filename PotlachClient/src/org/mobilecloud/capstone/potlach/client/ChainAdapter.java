package org.mobilecloud.capstone.potlach.client;

import java.util.Collection;

import org.mobilecloud.capstone.potlach.common.repository.Gift;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChainAdapter extends BaseAdapter {	
	private Collection<Gift> giftList;
	
	private final LayoutInflater inflater;
	
	private final Context context;

	public ChainAdapter(Context c, Collection<Gift> data) {
		inflater	= LayoutInflater.from(c);
		context 	= c;
		giftList 	= data;
	}

	@Override 
	public int getCount() {
		return giftList.size();
	}

	@Override 
	public Gift getItem(int position) {
		return (Gift) giftList.toArray()[position];
	}

	@Override 
	public long getItemId(int position) {
		return position;
	}

	@Override 
	public View getView(int position, View view, ViewGroup parent) {
		
		final Gift gift = getItem(position);
		
		final ViewHolder holder;		
		if (view != null) {
			holder = (ViewHolder) view.getTag();
		} else {
			view = inflater.inflate(R.layout.activity_chain_image_item, parent, false);
			holder = new ViewHolderChain(view, gift, context);
			view.setTag(holder);
		}
			
		/*
		 * MULTIMEDIA
		 * 
		 * old: Utils.executeMultimediaCall(gift, holder);
		 */
		holder.runServiceMultimedia();
		
		return view;
	}
		
	public static class ViewHolderChain extends ViewHolder {
					
		@OnClick(R.id.imageGiftHolder)
		public void giftFullScreen() {		
			if (gift_.isImage()){
				Utils.openDialogWithImage(context_, image_);
			}else{
				Intent intent = new Intent(context_, VideoStreamingActivity.class);
				intent.putExtra("videoURI", realFile.toString());
				context_.startActivity(intent);
			}
		}
		
		ViewHolderChain(View view, Gift gift, Context c){
			super(view, gift, c);
			ButterKnife.inject(this, view);			
		}
	}
}
