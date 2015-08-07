package org.mobilecloud.capstone.potlach.client;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

import org.mobilecloud.capstone.potlach.client.R;
import org.mobilecloud.capstone.potlach.common.repository.Chain;
import org.mobilecloud.capstone.potlach.common.repository.Gift;
import org.mobilecloud.capstone.potlach.common.repository.GiftStatus;
import org.mobilecloud.capstone.potlach.common.repository.GiftStatus.GiftState;

import com.google.common.io.Files;

import retrofit.mime.TypedFile;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class GiftActivity extends Activity {

	private final int REQUEST_CAMERA_PHOTO 	= 1;
	private final int SELECT_PHOTO 			= 2;
	private final int REQUEST_CAMERA_VIDEO 	= 3;
	private final int SELECT_VIDEO 			= 4;	
	
	private final String OPTION_MAIN 		= "Add Gift!";
	private final String TAKE_PHOTO 		= "Take Photo";
	private final String TAKE_VIDEO 		= "Take Video";
	private final String CHOOSE_PHOTO 		= "Choose Photo";	
	private final String CHOOSE_VIDEO 		= "Choose Video";
	private final String CANCEL_MULTIMEDIA 	= "Cancel";
	
	private int SELECTED_REQUEST;
		
	@InjectView(R.id.newGiftTitle)
	protected EditText title_;
	
	@InjectView(R.id.newGiftDescription)
	protected EditText description_;
	
	@InjectView(R.id.newGiftImage)
	protected ImageButton image_;
	
	private File imageFile_;
	
	@OnClick(R.id.saveGiftButton)
	public void saveGift() {
		final Gift newGift = new Gift(title_.getText().toString(), description_.getText().toString(), Gift.getContentTypeFromFile(imageFile_) );						
		
		final String chainId = getIntent().getStringExtra("chainId");		
		
		CallableTask.invoke(new Callable<Gift>() {

			@Override
			public Gift call() throws Exception {
				Gift g = PotlachContext.getInstance().getGiftSvcApi().addGift(newGift);									
				GiftStatus status = PotlachContext.getInstance().getGiftSvcApi().setGiftData(g.getId(), new TypedFile(g.getContentType(), imageFile_));			
				if (!status.getState().equals(GiftState.READY)){
					throw new Exception("Gift not ready");
				}
				
				Chain c = null;
				if (chainId.length()>0){
					g.setChain(chainId);	
					c = PotlachContext.getInstance().getChainSvcApi().getChainById(chainId);
				}else{
					c = PotlachContext.getInstance().getChainSvcApi().addChain(new Chain(g));
					g.setChain(c.getId());
				}												
				g = PotlachContext.getInstance().getGiftSvcApi().addGift(g);
				
				c.getGifts().add(g.getId());
				PotlachContext.getInstance().getChainSvcApi().addChain(c);
				
				return g;
			}
		}, new TaskCallback<Gift>() {
			
			@Override
			public void success(Gift result){				
				
				try {			
					if (SELECTED_REQUEST==SELECT_PHOTO 			|| SELECTED_REQUEST==SELECT_VIDEO)
						Files.copy(imageFile_, Utils.getFileGift(GiftActivity.this, result.getId()));		
					
					if (SELECTED_REQUEST==REQUEST_CAMERA_PHOTO 	|| SELECTED_REQUEST==REQUEST_CAMERA_VIDEO)
						Files.move(imageFile_, Utils.getFileGift(GiftActivity.this, result.getId()));
					
				} catch (IOException e) {					
					e.printStackTrace();
				}
				
				startActivity(new Intent(GiftActivity.this, GiftListActivity.class));
			}

			@Override
			public void error(Exception e) {
				Toast.makeText(GiftActivity.this, "Gift not saved, error.", Toast.LENGTH_SHORT).show();					
			}
		});
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gift);
		ButterKnife.inject(this);				
	}
	
	@OnClick(R.id.newGiftImage)
	public void selectImage() {
        final CharSequence[] items = { TAKE_PHOTO, TAKE_VIDEO, CHOOSE_PHOTO, CHOOSE_VIDEO, CANCEL_MULTIMEDIA };
 
        AlertDialog.Builder builder = new AlertDialog.Builder(GiftActivity.this);
        builder.setTitle(OPTION_MAIN);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(TAKE_PHOTO)) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);                    
                    if (intent.resolveActivity(getPackageManager()) != null) {                                                                    
                        imageFile_ = Utils.getFileGift();                                              
                        if (imageFile_ != null) {
                        	intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile_));
                            startActivityForResult(intent, REQUEST_CAMERA_PHOTO);
                        }
                    }                    
                } else if (items[item].equals(TAKE_VIDEO)) {
                	Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);                    
                    if (intent.resolveActivity(getPackageManager()) != null) {                                                                   
                        imageFile_ = Utils.getFileGift();                                             
                        if (imageFile_ != null) {
                        	intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile_));
                            startActivityForResult(intent, REQUEST_CAMERA_VIDEO);
                        }
                    }
                } else if (items[item].equals(CHOOSE_PHOTO)) {
                    Intent intent = new Intent( Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult( intent, SELECT_PHOTO);                    
                } else if (items[item].equals(CHOOSE_VIDEO)) {
                	 Intent intent = new Intent( Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                     intent.setType("video/*");
                     startActivityForResult( intent, SELECT_VIDEO);
                } else if (items[item].equals(CANCEL_MULTIMEDIA)) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA_PHOTO) {  
            	SELECTED_REQUEST					= REQUEST_CAMERA_PHOTO;
            	BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                Bitmap imageBitmap 					= BitmapFactory.decodeFile(imageFile_.getAbsolutePath(), bitmapOptions);
                image_.setImageBitmap(imageBitmap);                                      
                
                Utils.compressGift(imageBitmap, imageFile_);  
                
            } else if (requestCode == REQUEST_CAMERA_VIDEO) {   
            	SELECTED_REQUEST = REQUEST_CAMERA_VIDEO;
                Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(imageFile_.getAbsolutePath(), MediaStore.Images.Thumbnails.MINI_KIND);                
                image_.setImageBitmap(Utils.getBitmapWithIconPlay(GiftActivity.this, thumbnail)); 
                   
//                TODO: compress video
//                Utils.compressGift(imageFile_);    
                
            } else if (requestCode == SELECT_PHOTO) {  
            	SELECTED_REQUEST 			= SELECT_PHOTO;
            	String path 				= getRealPathImageFromURI(data.getData());
            	imageFile_ 					= new File(path);               					
				Bitmap yourSelectedImage 	= BitmapFactory.decodeFile(path);  					
	            image_.setImageBitmap(yourSelectedImage);
	            
	            Utils.compressGift(yourSelectedImage, imageFile_);  
	            
        	} else if (requestCode == SELECT_VIDEO) {
        		SELECTED_REQUEST 			= SELECT_VIDEO;
        		String path 				= getRealPathVideoFromURI(data.getData());
            	imageFile_ 					= new File(path);               					
            	Bitmap thumbnail 			= ThumbnailUtils.createVideoThumbnail(imageFile_.getAbsolutePath(), MediaStore.Images.Thumbnails.MINI_KIND);                
                image_.setImageBitmap(Utils.getBitmapWithIconPlay(GiftActivity.this, thumbnail)); 

//                TODO: compress video
//                Utils.compressGift(imageFile_); 
        	}
        }
    }		
	
	private String getRealPathImageFromURI(Uri contentURI) {
	    String result;
	    String[] filePathColumn = { MediaStore.Images.Media.DATA };
	    Cursor cursor = getContentResolver().query(contentURI, filePathColumn, null, null, null);
	    if (cursor == null) { 
	        result = contentURI.getPath();
	    } else { 
	        cursor.moveToFirst(); 
	        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA); 
	        result = cursor.getString(idx);
	        cursor.close();
	    }	    
	    return result;
	}	
	
	private String getRealPathVideoFromURI(Uri contentURI) {
	    String result;
	    String[] filePathColumn = { MediaStore.Video.Media.DATA };
	    Cursor cursor = getContentResolver().query(contentURI, filePathColumn, null, null, null);
	    if (cursor == null) { 
	        result = contentURI.getPath();
	    } else { 
	        cursor.moveToFirst(); 
	        int idx = cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA); 
	        result = cursor.getString(idx);
	        cursor.close();
	    }	    
	    return result;
	}	
}
