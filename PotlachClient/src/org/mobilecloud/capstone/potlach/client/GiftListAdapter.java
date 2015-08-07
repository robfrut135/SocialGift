package org.mobilecloud.capstone.potlach.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.Callable;

import org.mobilecloud.capstone.potlach.common.repository.Gift;
import org.mobilecloud.capstone.potlach.common.repository.Flag.FlagType;

import com.google.common.collect.Iterators;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class GiftListAdapter extends BaseAdapter {	
	private Collection<Gift> giftList = new ArrayList<Gift>();
	
	private final LayoutInflater inflater;
	
	private final Context context;

	public GiftListAdapter(Context c) {
		inflater	= LayoutInflater.from(c);
		context 	= c;		
	}
	
	private void setData(Collection<Gift> data){
		giftList 	= data;
	}
		
	@Override 
	public int getCount() {
		return giftList.size();
	}

	@Override 
	public Gift getItem(int position) {
		if (position>=0)
			return (Gift)Iterators.get(giftList.iterator(), position);		
		return null;
	}

	@Override 
	public long getItemId(int position) {		
		return position;
	}	
	
	private int getPosition(Gift g){
		int index = Arrays.asList(giftList.toArray()).indexOf(g);
		return index<0?0:index;
	}	
	
	public int getPosition(String giftId){
		Iterator<Gift> iterator = giftList.iterator();
		while (iterator.hasNext()){
			Gift g = iterator.next();
			if (g.getId().equals(giftId)){
				return getPosition(g);
			}
		}
		return 0;
	}
	
	public void updateGifts(Collection<Gift> result){							
		setData(result);
		notifyDataSetChanged();																		
	}

	@Override 
	public View getView(int position, View view, ViewGroup parent) {
		
		final Gift gift = getItem(position);
		
		final ViewHolderListGift holder;		
		if (view != null) {
			holder = (ViewHolderListGift) view.getTag();
		} else {
			view = inflater.inflate(R.layout.activity_gift_list_item, parent, false);
			holder = new ViewHolderListGift(view, gift, context);
			view.setTag(holder);
		}
			
		/*
		 * DELETE?
		 */
		holder.deleteGift_.setVisibility(PotlachContext.getInstance().getUser().getId().equals(gift.getUser())?View.VISIBLE:View.GONE);		
		
		/*
		 * TEXT		
		 */
		holder.title_.setText(gift.getTitle());
		holder.description_.setText(gift.getDescription());			
		
		/*
		 * TOUCH COUNTER
		 */
		CallableTask.invoke(new Callable<Integer[]>() {
			@Override
			public Integer[] call() throws Exception {
				/*
				 * [0] total touched
				 * [1] user touched?
				 */
				Integer[] result = { 	PotlachContext.getInstance().getFlagSvcApi().findFlagByGiftAndType(gift.getId(), FlagType.TOUCH).size(),
										PotlachContext.getInstance().getFlagSvcApi().findFlagByUserAndGiftAndType(PotlachContext.getInstance().getUser().getId(), gift.getId(), FlagType.TOUCH).size()
									};
				return result;
			}
		}, new TaskCallback<Integer[]>() {			
			@Override
			public void success(Integer[] result) {	
				holder.counterTouched_.setText(String.valueOf(result[0]));			
				holder.counterTouched_.setChecked(result[1]>0);
			}			
			@Override
			public void error(Exception e) {									
			}
		});
		/*
		 * INNAPROPIATED
		 */
		CallableTask.invoke(new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				return PotlachContext.getInstance().getFlagSvcApi().findFlagByUserAndGiftAndType(PotlachContext.getInstance().getUser().getId(), gift.getId(), FlagType.INNAPROPIATED).size();
			}
		}, new TaskCallback<Integer>() {			
			@Override
			public void success(Integer result) {	
				holder.inappropiatedCheck_.setChecked(result>0);
			}			
			@Override
			public void error(Exception e) {									
			}
		});
		/*
		 * MULTIMEDIA
		 * 
		 * old: Utils.executeMultimediaCall(gift, holder);
		 */
		holder.runServiceMultimedia();
		
		return view;
	}
		
	public static class ViewHolderListGift extends ViewHolder{
		
		@InjectView(R.id.textViewTitle) TextView title_;
		@InjectView(R.id.textViewDescription) TextView description_;		
		@InjectView(R.id.counterTouched) CheckBox counterTouched_;
		@InjectView(R.id.inappropiatedCheck) CheckBox inappropiatedCheck_;
		@InjectView(R.id.chainGifts) ImageButton chainGifts_;		
		@InjectView(R.id.deleteGift) ImageView deleteGift_;	
		
		@OnClick(R.id.deleteGift)
		public void deleteGift() {
			new AlertDialog.Builder(context_)
				.setMessage("Are you sure want to delete this gift?")
				.setCancelable(false)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						CallableTask.invoke(new Callable<Boolean>() {
							@Override
							public Boolean call() throws Exception {					
								PotlachContext.getInstance().getGiftSvcApi().deleteGift(gift_.getId());																						
								Utils.getFileGift(context_, gift_.getId()).delete();	
								return true;
							}
						}, new TaskCallback<Boolean>() {			
							@Override
							public void success(Boolean result) {										
								Toast.makeText(context_, "Deleted gift.", Toast.LENGTH_SHORT).show();
								GiftListActivity.giftListActivity.refreshGifts();
							}
							@Override
							public void error(Exception e) {
								Toast.makeText(context_, "Error deleting...", Toast.LENGTH_SHORT).show();					
							}
						});
					}
				})
				.setNegativeButton("No", null)
				.show();
		}
		
		@OnClick(R.id.counterTouched)
		public void likeGift() {
			CallableTask.invoke(new Callable<Integer>() {
				@Override
				public Integer call() throws Exception {	
					if (counterTouched_.isChecked()){
						PotlachContext.getInstance().getGiftSvcApi().likeGift(gift_.getId());
					}else{
						PotlachContext.getInstance().getGiftSvcApi().unlikeGift(gift_.getId());
					}
					return PotlachContext.getInstance().getFlagSvcApi().findFlagByGiftAndType(gift_.getId(), FlagType.TOUCH).size();
				}
			}, new TaskCallback<Integer>() {			
				@Override
				public void success(Integer result) {				
					counterTouched_.setText(String.valueOf(result));
				}
				@Override
				public void error(Exception e) {
					Toast.makeText(context_, "Error like/unlike gift checking.", Toast.LENGTH_SHORT).show();					
				}
			});
		}
		
		@OnClick(R.id.inappropiatedCheck)
		public void innapropiatedGift() {
			CallableTask.invoke(new Callable<Integer>() {
				@Override
				public Integer call() throws Exception {
					if (inappropiatedCheck_.isChecked()){											
						PotlachContext.getInstance().getGiftSvcApi().innapropiatedGift(gift_.getId());
					}else{
						PotlachContext.getInstance().getGiftSvcApi().apropiatedGift(gift_.getId());						
					}
					return 0;
				}
			}, new TaskCallback<Integer>() {			
				@Override
				public void success(Integer result) {														
				}

				@Override
				public void error(Exception e) {
					Toast.makeText(context_, "Error innapropiated checking.", Toast.LENGTH_SHORT).show();					
				}
			});
		}
		
		@OnClick(R.id.chainGifts)
		public void chainGift() {
			Intent intent = new Intent(context_, ChainActivity.class);
			intent.putExtra("chainId", gift_.getChain());
			context_.startActivity(intent);
		}
		
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
		
		ViewHolderListGift(View view, Gift gift, Context c) {
			super(view, gift, c);		
			ButterKnife.inject(this, view);							
		}
	}
}
