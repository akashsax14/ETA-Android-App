package com.nyu.cs9033.eta.controllers;


import java.util.ArrayList;
import java.util.Calendar;

import com.nyu.cs9033.eta.database.TripDatabaseHelper;
import com.nyu.cs9033.eta.models.Person;
import com.nyu.cs9033.eta.models.Trip;
import com.nyu.cs9033.eta.R;

//import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
//import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;


public class CreateTripActivity extends FragmentActivity 
{
	static String date;
	static int frnd_num = 0;
	private static final int FRIENDNAMEIDPREFIX = 100000;
	private static final int FRIENDLOCATIONIDPREFIX = 200000;
	private static final int FRIENDLAYOUTIDPREFIX = 900000;
	
	//*************Used from <http://developer.android.com/guide/topics/ui/controls/pickers.html> *************
	public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener 
	{
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) 
		{
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);
			
			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}
		
		public void onDateSet(DatePicker view, int year, int month, int day) 
		{
			// Do something with the date chosen by the user
			try
			{
				date = month+"/"+day+"/"+year;
			}
			catch(Exception e)
			{
				//TextView error = (TextView) findViewById(R.id.errordev);
				//error.setText(e.toString());
			}
		}
	}
	public void showDatePickerDialog(View v) 
	{
	    DialogFragment newFragment = new DatePickerFragment();
	    newFragment.show(getSupportFragmentManager(), "datePicker");
	}
	//***********************************************************************************************************
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		try
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_create_linear);
		}
		catch(Exception e)
		{
			TextView error = (TextView) findViewById(R.id.errordev);
			error.setText(e.toString());
		}
	}
	
	public void onClick(View v)
	{
		try
		{
			Button b = (Button)v;
			switch(b.getId())
			{
				case R.id.btncreatetrip:
					persistTrip(createTrip());
					break;
				case R.id.btnadd:
					frnd_num++;
					addPersonToLayout();
					break;
				case R.id.btndelete:
					deletePersonFromLayout();
					frnd_num--;
					break;
			}
		}
		catch(Exception e)
		{
			TextView error = (TextView) findViewById(R.id.errordev);
			error.setText(e.toString());
		}
	}
	
	public int dp(int dps)
	{
		final float scale = this.getResources().getDisplayMetrics().density;
		int pixels = (int) (dps * scale + 0.5f);
		return pixels;
	}
	public void addPersonToLayout()
	{
		try
		{
			LinearLayout parent = (LinearLayout) findViewById(R.id.friendlayout);
			
			LinearLayout child = new LinearLayout(this);
			LinearLayout.LayoutParams myLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			myLayoutParams.topMargin = dp(5);
			child.setId(FRIENDLAYOUTIDPREFIX + frnd_num);
			child.setOrientation(LinearLayout.HORIZONTAL);
			child.setLayoutParams(myLayoutParams);
			
			TextView label = new TextView(this);
			LayoutParams labelParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			labelParams.leftMargin = dp(10);
			label.setLayoutParams(labelParams);
			label.setText("Friend "+frnd_num);
			label.setTextAppearance(this, android.R.style.TextAppearance_Small);
			label.setTypeface(Typeface.MONOSPACE);
			label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
			label.setPadding(dp(5), dp(5), dp(5), dp(5));
			
			EditText name = new EditText(this);
			LayoutParams nameParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			nameParams.leftMargin = dp(10);
			name.setId(FRIENDNAMEIDPREFIX + frnd_num);
			name.setLayoutParams(nameParams);
			name.setHeight(dp(10));
			name.setWidth(dp(100));
			
			EditText loc = new EditText(this);
			LayoutParams locParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			locParams.leftMargin = dp(5);
			loc.setId(FRIENDLOCATIONIDPREFIX + frnd_num);
			loc.setLayoutParams(nameParams);
			loc.setHeight(dp(10));
			loc.setWidth(dp(100));
			
			child.addView(label);
			child.addView(name);
			child.addView(loc);
			
			parent.addView(child);
		}
		catch(Exception e)
		{
			Toast.makeText(this, "Exception : "+e.toString(),Toast.LENGTH_LONG).show();
		}
	}
	
	public void deletePersonFromLayout()
	{
		try
		{
			LinearLayout parent = (LinearLayout) findViewById(R.id.friendlayout);
			
			parent.removeView(findViewById(FRIENDLAYOUTIDPREFIX + frnd_num));
		}
		catch(Exception e)
		{
			Toast.makeText(this, "Exception : "+e.toString(),Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
	public void onBackPressed() 
	{
		cancelTripCreation();
	}
	
	/**
	 * This method should be used to
	 * instantiate a Trip model.
	 * 
	 * @return The Trip as represented
	 * by the View.
	 */
	public Trip createTrip() 
	{
		// TODO - fill in here
		try
		{
			EditText tripNameW = (EditText) findViewById(R.id.edittripname);
			String tripName = tripNameW.getText().toString();
			EditText destNameW = (EditText) findViewById(R.id.editdestname);
			String destName = destNameW.getText().toString();
			EditText creatorW = (EditText) findViewById(R.id.editcreatorname);
			String creator = creatorW.getText().toString();
			
			ArrayList<Person> friends = new ArrayList<Person>();
			for(int i = 1; i <= frnd_num; i++)
			{
				EditText personName = (EditText) findViewById(FRIENDNAMEIDPREFIX + i);
				String name = personName.getText().toString();
				
				EditText personLocation = (EditText) findViewById(FRIENDLOCATIONIDPREFIX + i);
				String location = personLocation.getText().toString();
				
				friends.add(new Person(name, location));
			}
			Trip t = new Trip(0, tripName, destName, creator, date, friends);
			return t;
		}
		catch(Exception e)
		{
			TextView error = (TextView) findViewById(R.id.errordev);
			error.setText(e.toString());
			return null;
		}
	}

	/**
	 * For HW2 you should treat this method as a 
	 * way of sending the Trip data back to the
	 * main Activity
	 * 
	 * Note: If you call finish() here the Activity 
	 * eventually end and pass an Intent back to 
	 * the previous Activity using setResult().
	 * 
	 * @return whether the Trip was successfully 
	 * persisted.
	 */
	public boolean persistTrip(Trip trip) 
	{
		try
		{
			TripDatabaseHelper db = new TripDatabaseHelper(this);
			
			long trip_id = db.insertTrip(trip);
			
			for(Person p : trip.getFriends())
			{
				/*long person_id = */db.insertPerson(trip_id, p);
			}
			Toast.makeText(this, "Trip Saved",Toast.LENGTH_LONG).show();
			finish();
			return true;
		}
		catch(Exception e)
		{
			TextView error = (TextView) findViewById(R.id.errordev);
			error.setText(e.toString());
			return false;
		}
	}

	/**
	 * This method should be used when a
	 * user wants to cancel the creation of
	 * a Trip.
	 * 
	 * Note: You most likely want to call this
	 * if your activity dies during the process
	 * of a trip creation or if a cancel/back
	 * button event occurs. Should return to
	 * the previous activity without a result
	 * using finish() and setResult().
	 */
	public void cancelTripCreation() 
	{
		// TODO - fill in here
		try
		{
			setResult(RESULT_CANCELED, getIntent());
			Toast.makeText(this, "Trip Creation Cancelled!",Toast.LENGTH_SHORT).show();
			finish();
		}
		catch(Exception e)
		{
			TextView error = (TextView) findViewById(R.id.errordev);
			error.setText(e.toString());
		}
	}
}
