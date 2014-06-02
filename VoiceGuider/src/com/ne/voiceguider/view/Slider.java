package com.ne.voiceguider.view;

import com.ne.voiceguider.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;

public class Slider extends SeekBar {

	private Drawable mThumb;
	private Context c;

	public Slider(Context context) {
		super(context);
		c= context;
	}

	public Slider(Context context, AttributeSet attrs) {
		super(context, attrs);
		c= context;
	}

//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		if (event.getAction() == MotionEvent.ACTION_DOWN) {
//			mThumb= c.getResources().getDrawable(R.drawable.thumb_pause);
//
//			if (event.getX() >= mThumb.getBounds().left
//					&& event.getX() <= mThumb.getBounds().right
//					&& event.getY() <= mThumb.getBounds().bottom
//					&& event.getY() >= mThumb.getBounds().top) {
//
//				super.onTouchEvent(event);
//			} else {
//				return false;
//			}
//		} else if (event.getAction() == MotionEvent.ACTION_UP) {
//			return false;
//		} else {
//			super.onTouchEvent(event);
//		}
//
//		return true;
//	}
	}