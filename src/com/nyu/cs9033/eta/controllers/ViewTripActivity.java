package com.nyu.cs9033.eta.controllers;

import java.util.ArrayList;

import com.nyu.cs9033.eta.database.TripDatabaseHelper;
import com.nyu.cs9033.eta.models.Person;
import com.nyu.cs9033.eta.models.Trip;
import com.nyu.cs9033.eta.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class ViewTripActivity extends Activity
{	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_linear);
		// TODO - fill in here
		Trip trip= getTrip(this.getIntent());
		initView(trip);
	}
	
	/**
	 * Create the most recent trip that
	 * was passed to TripViewer.
	 * 
	 * @param i The Intent that contains
	 * the most recent trip data.
	 * 
	 * @return The Trip that was most recently
	 * passed to TripViewer, or null if there
	 * is none.
	 */
	public Trip getTrip(Intent i) {
		
		// TODO - fill in here
		Trip trip = null;
		try
		{
			Bundle b = i.getExtras();
			trip = b.getParcelable("trip");	
		}
		catch(Exception e)
		{
			TextView error = (TextView) findViewById(R.id.errordev);
			error.setText("Please Create a trip!");
		}
		return trip;
	}

	/**
	 * Populate the View using a Trip model.
	 * 
	 * @param trip The Trip model used to
	 * populate the View.
	 */
	public void initView(Trip trip) 
	{
		try
		{
			TextView tripname= (TextView) findViewById(R.id.viewtripname);
			tripname.setText(trip.getTripName());
			
			TextView destname= (TextView) findViewById(R.id.viewdestname);
			destname.setText(trip.getDestinationName());
			
			TextView creator= (TextView) findViewById(R.id.viewcreator);
			creator.setText(trip.getCreator());
			
			TextView date= (TextView) findViewById(R.id.viewdate);
			date.setText(trip.getDate());

			ArrayList<Person> friends= trip.getFriends();
			
			for(int i=1; i <= friends.size(); i++)
			{
				Person f = friends.get(i-1);

				initFriendsView(f, i);
			}
		}
		catch(Exception e)
		{
			if(trip == null)
			{
				Toast.makeText(this, "ERROR : Please Create a Trip First!",Toast.LENGTH_LONG).show();
			}
		}
	}
	
	public int dp(int dps)
	{
		final float scale = this.getResources().getDisplayMetrics().density;
		int pixels = (int) (dps * scale + 0.5f);
		return pixels;
	}
	public void initFriendsView(Person p, int i)
	{
		try
		{
			LinearLayout parent = (LinearLayout) findViewById(R.id.friendlayout);
			
			LinearLayout child = new LinearLayout(this);
			LinearLayout.LayoutParams myLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			myLayoutParams.topMargin = dp(15);
			child.setOrientation(LinearLayout.HORIZONTAL);
			child.setLayoutParams(myLayoutParams);
			
			TextView label = new TextView(this);
			LayoutParams labelParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			labelParams.leftMargin = dp(10);
			label.setLayoutParams(labelParams);
			label.setText("Friend " + i);
			label.setTextAppearance(this, android.R.style.TextAppearance_Small);
			label.setTypeface(Typeface.MONOSPACE);
			label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
			label.setPadding(dp(5), dp(5), dp(5), dp(5));
			
			TextView name = new TextView(this);
			LayoutParams nameParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			nameParams.leftMargin = dp(30);
			name.setText(p.getName());
			name.setLayoutParams(nameParams);
			name.setWidth(dp(80));
			name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
			name.setPadding(dp(5), dp(5), dp(5), dp(5));
		
			TextView loc = new TextView(this);
			LayoutParams locParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			locParams.leftMargin = dp(5);
			loc.setText(p.getLocation());
			loc.setLayoutParams(nameParams);
			loc.setWidth(dp(80));
			loc.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
			loc.setPadding(dp(5), dp(5), dp(5), dp(5));
			
			child.addView(label);
			child.addView(name);
			child.addView(loc);
			
			parent.addView(child);
		}
		catch(Exception e)
		{
			Toast.makeText(this, "EXCEPTION : "+e.toString(),Toast.LENGTH_LONG).show();
		}
	}
	
	public void deleteTrip(View v)
	{
		Trip trip = null;
		try
		{
			Intent i = getIntent();
			Bundle b = i.getExtras();
			trip = b.getParcelable("trip");	
			
			TripDatabaseHelper tdh = new TripDatabaseHelper(this);
			tdh.deleteTrip(trip.getTripId());
			Toast.makeText(this, "Deleting Trip : " + trip.getTripName(),Toast.LENGTH_LONG).show();
			
			setResult(Activity.RESULT_OK);
			finish();
		}
		catch(Exception e)
		{
			Toast.makeText(this, "EXCEPTION : "+e.toString(),Toast.LENGTH_LONG).show();
		}
	}
}
