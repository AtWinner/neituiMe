package com.example.event;

import com.example.neituime.R;
import com.example.neituime.R.drawable;

import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

public class myOnTouchListenerChangeBackground implements OnTouchListener {

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			v.setBackgroundColor(Color.GRAY);
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			v.setBackgroundColor(Color.WHITE);
			break;
		}
		return false;
	}

}
