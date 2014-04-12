package com.nyu.cs9033.eta.controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_linear);
		
		Trip trip= getTrip(this.getIntent());
		initView(trip);
	}
	
	public Trip getTrip(Intent i) 
	{
		Trip trip = null;
		try
		{
			Bundle b = i.getExtras();
			trip = b.getParcelable("trip");	
		}
		catch(Exception e)
		{
			Toast.makeText(this, "Please Create a trip!",Toast.LENGTH_LONG).show();
		}
		return trip;
	}

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
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
			String d = df.format(trip.getDate());
			date.setText(d);

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
			
			/*************************LINE 1 of Friends Layout****************************/
			LinearLayout child_line1 = new LinearLayout(this);
			LinearLayout.LayoutParams myLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			myLayoutParams.topMargin = dp(15);
			child_line1.setOrientation(LinearLayout.HORIZONTAL);
			child_line1.setLayoutParams(myLayoutParams);
			
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
			
			child_line1.addView(label);
			child_line1.addView(name);
			child_line1.addView(loc);
			/***********************END LINE 1 of Friends Layout**************************/
			
			/*************************LINE 2 of Friends Layout****************************/
			LinearLayout child_line2 = new LinearLayout(this);
			LinearLayout.LayoutParams myLayoutParams2 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			myLayoutParams2.topMargin = dp(5);
			child_line2.setOrientation(LinearLayout.HORIZONTAL);
			child_line2.setLayoutParams(myLayoutParams2);
			
			TextView phonelabel = new TextView(this);
			LayoutParams phonelabelParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			phonelabelParams.leftMargin = dp(40);
			phonelabel.setLayoutParams(phonelabelParams);
			phonelabel.setText("Phone ");
			phonelabel.setTextAppearance(this, android.R.style.TextAppearance_Small);
			phonelabel.setTypeface(Typeface.MONOSPACE);
			phonelabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
			phonelabel.setPadding(dp(5), dp(5), dp(5), dp(5));
			
			TextView phone = new TextView(this);
			LayoutParams phoneParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			phoneParams.leftMargin = dp(30);
			phone.setText(p.getPhone());
			phone.setLayoutParams(phoneParams);
			phone.setWidth(dp(150));
			
			child_line2.addView(phonelabel);
			child_line2.addView(phone);
			/***********************END LINE 2 of Friends Layout**************************/
			
			parent.addView(child_line1);
			parent.addView(child_line2);
		}
		catch(Exception e)
		{
			Toast.makeText(this, "Exception : "+e.toString(),Toast.LENGTH_LONG).show();
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
			Toast.makeText(this, "Exception : "+e.toString(),Toast.LENGTH_LONG).show();
		}
	}
}
