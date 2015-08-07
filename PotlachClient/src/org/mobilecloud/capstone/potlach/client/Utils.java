package org.mobilecloud.capstone.potlach.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

import org.mobilecloud.capstone.potlach.common.repository.Gift;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;

public class Utils {

	private static final String PREFIX_KEY = "GIFT_";
	
	public static void openDialogWithImage(Context context_, ImageView image_){
		Dialog mSplashDialog = new Dialog(context_);
		mSplashDialog.requestWindowFeature((int) Window.FEATURE_NO_TITLE);
		mSplashDialog.setContentView(R.layout.gift_fullscreen);
		ImageView imageFullScreen = (ImageView) mSplashDialog.findViewById(R.id.imageGiftFullScreen);
		imageFullScreen.setImageDrawable(image_.getDrawable());
		mSplashDialog.getWindow().setFlags(LayoutParams.FLAG_FULLSCREEN, LayoutParams.FLAG_FULLSCREEN);
		mSplashDialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mSplashDialog.setCancelable(true);
		mSplashDialog.show();
	}
	
	/*
	 * PUBLIC FILES
	 */	
	public static File getFileGift() {
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());	   
	    File image = getFileGift(timeStamp);
	    return image;
	}		
	
	public static File getFileGift(String giftId){	   
	    String imageFileName = PREFIX_KEY + giftId;
	    File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
	    File image = new File(storageDir, imageFileName);
	    return image;
	}
	/*
	 * 
	 */
	
	/*
	 * PRIVATE FILES
	 */
	public static File getFileGift(final Context context,  final String giftId){   		
        return context.getFileStreamPath(PREFIX_KEY + giftId);
    }  
	
	public static File getFileGift(final Context context) {
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());	   
	    File image = getFileGift(context, timeStamp);
	    return image;
	}				
	
	public static boolean existsGift(final Context context,  final String giftId){		
	    File realFile = getFileGift(context, giftId);		
	    if (realFile.length()==0){
	    	return false;
	    }else{
	    	return true;
	    }
	}
	
	public static InputStream getStreamGift(final Context context,  final String giftId){		
	    File realFile = getFileGift(context, giftId);		
	    try {
			return new FileInputStream(realFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}		
	
	public static void saveStreamGift(final Context context,  final String giftId, InputStream giftData){
	    File realFile = getFileGift(context, giftId);			            	
	    saveStreamGift(realFile, giftData);
	}
	
	public static void saveStreamGift(File realFile, InputStream giftData){	    			       
    	try {
        	byte[] buffer = new byte[giftData.available()];
			giftData.read(buffer);			
        	OutputStream outFile = new FileOutputStream(realFile);	
            outFile.write(buffer);
            outFile.flush();
            outFile.close();		                    		                    
        } catch (Exception e) {
            e.printStackTrace();
        }	    
	}
	
	public static void compressGift(Bitmap bitmap, File fileGift){	
		try {
			fileGift.delete();
			
        	OutputStream outFile = new FileOutputStream(fileGift);
        	bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
            outFile.flush();
            outFile.close();                
        } catch (Exception e) {
            e.printStackTrace();
        }        		
	}
	
	public static void compressGift(File fileGift){	
		try {
        	InputStream inputFile = new FileInputStream(fileGift);                    
            byte[] buffer = new byte[inputFile.available()];
            inputFile.read(buffer);
            inputFile.close();
			
            OutputStream outputFile = new FileOutputStream(fileGift);
            
            Deflater comp = new Deflater();
    		comp.setLevel(Deflater.BEST_COMPRESSION);
    		 
    		DeflaterOutputStream dos = new DeflaterOutputStream(outputFile, comp);            		 
    		dos.write(buffer);
    		dos.finish(); 
    		dos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	/*
	 * @deprecated
	 */
	public static void executeMultimediaCall(final Gift gift, final ViewHolder holder){
		
		CallableTask.invoke(new Callable<InputStream>() {
			@Override
			public InputStream call() throws Exception {
				if (gift.isImage()){
					return PotlachContext.getInstance().getGiftSvcApi().getData(gift.getId()).getBody().in();
				}else{
					if (Utils.existsGift(holder.context_, gift.getId())){
						return Utils.getStreamGift(holder.context_, gift.getId());
					}else{
						Utils.saveStreamGift(holder.context_, gift.getId(), PotlachContext.getInstance().getGiftSvcApi().getData(gift.getId()).getBody().in());
						return Utils.getStreamGift(holder.context_, gift.getId());
					}
				}
			}
		}, new TaskCallback<InputStream>() {			
			@Override
			public void success(InputStream giftData) {						
				if (gift.isImage()){
					holder.image_.setImageBitmap(BitmapFactory.decodeStream(giftData));	
				}else{							
					holder.realFile = Utils.getFileGift(holder.context_, gift.getId());
	                Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(holder.realFile.getAbsolutePath(), MediaStore.Video.Thumbnails.MINI_KIND);                	                	                
	               
	                holder.image_.setImageBitmap( Utils.getBitmapWithIconPlay(holder.context_, thumbnail));
				}				
			}			
			@Override
			public void error(Exception e) {									
			}
		});	
	}
	
	public static Bitmap getBitmapWithIconPlay(Context ctx, Bitmap thumb){				
               
        Bitmap bmBackground = Bitmap.createBitmap(thumb.getWidth(), thumb.getHeight(), thumb.getConfig());
        Bitmap iconPlay 	= BitmapFactory.decodeResource(ctx.getResources(), R.drawable.play2);
        
        Paint paint 		= new Paint(Paint.FILTER_BITMAP_FLAG);
        Canvas canvas		= new Canvas(bmBackground);
        
        canvas.scale((float) 1.0, (float) 1.0);        
        canvas.drawBitmap(thumb, 0, 0, paint);
        canvas.drawBitmap(iconPlay, bmBackground.getWidth()/2-iconPlay.getWidth()/2, bmBackground.getHeight()/2-iconPlay.getHeight()/2, paint);
        canvas.save();
        
        return bmBackground;			      
	}
}
