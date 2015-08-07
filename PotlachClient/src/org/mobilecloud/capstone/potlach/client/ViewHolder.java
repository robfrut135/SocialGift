package org.mobilecloud.capstone.potlach.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.ref.WeakReference;

import org.mobilecloud.capstone.potlach.common.repository.Gift;

import butterknife.ButterKnife;
import butterknife.InjectView;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

abstract class ViewHolder {
	private static final String TAG = "ViewHolder";
	
	@InjectView(R.id.imageGiftHolder) ImageView image_;
	
	protected Gift gift_;
	protected Context context_;	
	protected File realFile;
	
	private MessengerHandler handler = new MessengerHandler(this);
	
	protected ViewHolder(View view, Gift gift, Context c) {
		gift_ 		= gift;
		context_ 	= c;			
		ButterKnife.inject(this, view);						
	}
	
	public void displayBitmap (String pathname) {    	
				
		try {			
			if (gift_.isImage()){
				image_.setImageBitmap(BitmapFactory.decodeStream(new FileInputStream(pathname)));	
			}else{							
				realFile 			= new File(pathname);
	            Bitmap thumbnail 	= ThumbnailUtils.createVideoThumbnail(realFile.getAbsolutePath(), MediaStore.Video.Thumbnails.MINI_KIND);                	                	                
	           
	            image_.setImageBitmap( Utils.getBitmapWithIconPlay(context_, thumbnail));
			}	
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
		}		    	
    }
				    
    protected void runServiceMultimedia() {
    	Log.i(TAG, "runServiceMultimedia Launch ThreadPoolDownloadService to " + gift_.getId() );
    	context_.startService(ThreadPoolDownloadService.makeIntent(context_.getApplicationContext(), handler, gift_.getId()));        	     
    }
        
    public static class MessengerHandler extends Handler {
	    
    	WeakReference<ViewHolder> outerClass;
    	    	
    	public MessengerHandler(ViewHolder outer) {
            outerClass = new WeakReference<ViewHolder>(outer);
    	}
    	
    	@Override
		public void handleMessage(Message msg) {
    		          
            final ViewHolder activity = outerClass.get();
    		
            if (activity != null) {                
            	Bundle data 	= msg.getData();
            	String pathname = data.getString(DownloadUtils.PATHNAME_KEY);
            	activity.displayBitmap(pathname);
            }
    	}
    }
}
