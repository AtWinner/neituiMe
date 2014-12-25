package com.example.event;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.example.neituime.R;

public class myOnTouchListenerChangeCityBackground implements OnTouchListener {

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			v.setBackgroundResource(R.drawable.city_item_background_white);
			break;

		case MotionEvent.ACTION_UP:
			v.setBackgroundResource(R.drawable.city_item_background);
			break;
		}
		return false;
	}

}