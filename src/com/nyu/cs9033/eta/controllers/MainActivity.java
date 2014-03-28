package com.nyu.cs9033.eta.controllers;

import com.nyu.cs9033.eta.models.Trip;
import com.nyu.cs9033.eta.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;
import android.util.Log;
import android.view.View;

public class MainActivity extends Activity 
{
	Trip t;
	private static final String TAG = "MainActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		try
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main_linear);
			Log.i(TAG, "onCreate");
		}
		catch(Exception e)
		{
			TextView error = (TextView) findViewById(R.id.errordev);
			error.setText(e.toString());
		}
	}
	
	public void onClick(View v)
	{
		Button b = (Button)v;
		switch(b.getId())
		{
			case R.id.btncreate:
				startTripCreator();
				break;
			case R.id.btnviewhistory:
				startTripHistoryViewer();
				break;
			case R.id.btnexit:
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_HOME);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				break;
		}
	}
	
	/**
	 * This method should start the
	 * Activity responsible for creating
	 * a Trip.
	 */
	public void startTripCreator() 
	{
		try
		{
			Intent intent = new Intent(this, CreateTripActivity.class);
			startActivity(intent);
		}
		catch(Exception e)
		{
			TextView error = (TextView) findViewById(R.id.errordev);
			error.setText(e.toString());
		}
	}
	
	/**
	 * This method should start the
	 * Activity responsible for viewing
	 * a Trip.
	 */
	public void startTripHistoryViewer() {
		
		// TODO - fill in here
		try
		{
			Intent intentTH = new Intent(this, TripHistoryActivity.class);
			startActivity(intentTH); 
		}
		catch(Exception e)
		{
			TextView error = (TextView) findViewById(R.id.errordev);
			error.setText(e.toString());
		}
	}
}
