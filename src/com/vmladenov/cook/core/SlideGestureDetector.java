package com.vmladenov.cook.core;

import android.app.Activity;
import android.view.GestureDetector;
import android.view.MotionEvent;

public final class SlideGestureDetector extends GestureDetector.SimpleOnGestureListener
{

	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private GestureDetector gestureDetector;
	private OnSlideListener slideListener;

	public SlideGestureDetector(Activity context, OnSlideListener slideListener)
	{
		this.gestureDetector = new GestureDetector(context, this);
		this.slideListener = slideListener;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
	{
		if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)
		{
			this.slideListener.onSlideRight();
			return true;
		}
		else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)
		{
			this.slideListener.onSlideLeft();
			return true;
		}
		return false;

	}

	public Boolean onTouchEvent(MotionEvent event)
	{
		return gestureDetector.onTouchEvent(event);
	}
}
