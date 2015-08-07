package org.mobilecloud.capstone.potlach.client;

import android.view.View;
import android.view.animation.AlphaAnimation;
import butterknife.ButterKnife;

public class Effects {

	public static final ButterKnife.Action<View> ALPHA_FADE = new ButterKnife.Action<View>() {
	    @Override 
	    public void apply(View view, int index) {
	      AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
	      alphaAnimation.setFillBefore(true);
	      alphaAnimation.setDuration(500);
	      alphaAnimation.setStartOffset(index * 100);
	      view.startAnimation(alphaAnimation);
	    }
	};	 	  
}
