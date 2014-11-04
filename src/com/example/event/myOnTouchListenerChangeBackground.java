package com.example.event;

import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class myOnTouchListenerChangeBackground implements OnTouchListener {

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			v.setBackgroundColor(Color.GRAY);
			break;

		case MotionEvent.ACTION_UP:
			v.setBackgroundColor(Color.WHITE);
			break;
		}
		return false;
	}

}
