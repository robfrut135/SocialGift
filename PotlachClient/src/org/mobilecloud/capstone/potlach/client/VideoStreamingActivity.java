package org.mobilecloud.capstone.potlach.client;

import butterknife.ButterKnife;
import butterknife.InjectView;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;
 
public class VideoStreamingActivity extends Activity {     
    
    @InjectView(R.id.videoStreaming)
    protected VideoView videoStreaming_;
         
    private ProgressDialog pDialog;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_streaming);
        ButterKnife.inject(this);	
                
        String videoURI = getIntent().getStringExtra("videoURI");
        
        pDialog = new ProgressDialog(VideoStreamingActivity.this);
        pDialog.setTitle("Load Gift streaming...");
        pDialog.setMessage("Buffering...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
 
        try {
            MediaController mediacontroller = new MediaController(VideoStreamingActivity.this);
            mediacontroller.setMediaPlayer(videoStreaming_);
            mediacontroller.setAnchorView(videoStreaming_);  
            mediacontroller.setVisibility(View.VISIBLE);
            mediacontroller.setEnabled(true);
            videoStreaming_.setMediaController(mediacontroller);
            videoStreaming_.setVideoPath(videoURI);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
 
        videoStreaming_.requestFocus();
        videoStreaming_.setOnPreparedListener(new OnPreparedListener() {
           
            public void onPrepared(MediaPlayer mp) {
                pDialog.dismiss();
                videoStreaming_.start();
            }
        });
 
    }
 
}