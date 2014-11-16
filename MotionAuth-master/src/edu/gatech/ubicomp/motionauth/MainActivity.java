package edu.gatech.ubicomp.motionauth;

import com.example.motionauth.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;

public class MainActivity extends Activity {
	private static final String TAG = "MobiAuth";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		startService(new Intent(this, MonitoringService.class));

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent e)
	{
	         if(e.getAction() == MotionEvent.ACTION_DOWN)
	         {
	        	 Log.w(TAG,"X:"+e.getX());
	        	 Log.w(TAG,"Y:"+e.getY());
	         }
	         return super.onTouchEvent(e);
	}
}
