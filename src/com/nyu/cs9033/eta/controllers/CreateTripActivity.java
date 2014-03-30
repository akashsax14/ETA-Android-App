package com.nyu.cs9033.eta.controllers;


import java.util.ArrayList;
import java.util.Calendar;

import com.nyu.cs9033.eta.database.TripDatabaseHelper;
import com.nyu.cs9033.eta.models.Person;
import com.nyu.cs9033.eta.models.Trip;
import com.nyu.cs9033.eta.R;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
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
	int frnd_num = 0;
	private static final int FRIENDNAMEIDPREFIX = 100000;
	private static final int FRIENDLOCATIONIDPREFIX = 200000;
	private static final int FRIENDPHONEIDPREFIX = 300000;
	private static final int FRIENDLAYOUTLNONEIDPREFIX = 900000;
	private static final int FRIENDLAYOUTLNTWOIDPREFIX = 910000;
	private static final int PICK_CONTACT = 1;
	
	
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
				//Toast.makeText(this, "Exception : "+e.toString(),Toast.LENGTH_LONG).show();
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
			if(getIntent().getAction() != null)
			{
				Intent intent = getIntent();
				String action = intent.getAction();
			    if (Intent.ACTION_SEND.equals(action) & intent.getType().equals("text/plain"))
			    {
			    	String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
			    	sharedText = sharedText.substring(0, sharedText.indexOf("http")).trim();
			    	EditText destination = (EditText) findViewById(R.id.editdestname);
			    	destination.setText(sharedText);
			    }
			}	
		}
		catch(Exception e)
		{
			Toast.makeText(this, "Exception : "+e.toString(),Toast.LENGTH_LONG).show();
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
					Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
			    	startActivityForResult(intent, PICK_CONTACT);
					break;
				case R.id.btndelete:
					deletePersonFromLayout();
					frnd_num--;
					break;
			}
		}
		catch(Exception e)
		{
			Toast.makeText(this, "Exception : "+e.toString(),Toast.LENGTH_LONG).show();
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
			
			/*************************LINE 1 of Friends Layout****************************/
			LinearLayout child_line1 = new LinearLayout(this);
			LinearLayout.LayoutParams myLayoutParams1 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			myLayoutParams1.topMargin = dp(5);
			child_line1.setId(FRIENDLAYOUTLNONEIDPREFIX + frnd_num);
			child_line1.setOrientation(LinearLayout.HORIZONTAL);
			child_line1.setLayoutParams(myLayoutParams1);
			
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
			
			child_line1.addView(label);
			child_line1.addView(name);
			child_line1.addView(loc);
			/***********************END LINE 1 of Friends Layout**************************/
			
			/*************************LINE 2 of Friends Layout****************************/
			LinearLayout child_line2 = new LinearLayout(this);
			LinearLayout.LayoutParams myLayoutParams2 = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			myLayoutParams2.topMargin = dp(5);
			child_line2.setId(FRIENDLAYOUTLNTWOIDPREFIX + frnd_num);
			child_line2.setOrientation(LinearLayout.HORIZONTAL);
			child_line2.setLayoutParams(myLayoutParams2);
			
			TextView phonelabel = new TextView(this);
			LayoutParams phonelabelParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			phonelabelParams.leftMargin = dp(40);
			phonelabel.setLayoutParams(phonelabelParams);
			phonelabel.setText("Phone ");
			phonelabel.setTextAppearance(this, android.R.style.TextAppearance_Small);
			phonelabel.setTypeface(Typeface.MONOSPACE);
			phonelabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
			phonelabel.setPadding(dp(5), dp(5), dp(5), dp(5));
			
			EditText phone = new EditText(this);
			LayoutParams phoneParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			phoneParams.leftMargin = dp(10);
			phone.setId(FRIENDPHONEIDPREFIX + frnd_num);
			phone.setLayoutParams(phoneParams);
			phone.setHeight(dp(10));
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
	
	public void deletePersonFromLayout()
	{
		try
		{
			LinearLayout parent = (LinearLayout) findViewById(R.id.friendlayout);
			
			parent.removeView(findViewById(FRIENDLAYOUTLNONEIDPREFIX + frnd_num));
			parent.removeView(findViewById(FRIENDLAYOUTLNTWOIDPREFIX + frnd_num));
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
	
	public Trip createTrip() 
	{
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
				
				EditText personPhone = (EditText) findViewById(FRIENDPHONEIDPREFIX + i);
				String phone = personPhone.getText().toString();
				
				friends.add(new Person(name, location, phone));
			}
			Trip t = new Trip(0, tripName, destName, creator, date, friends);
			return t;
		}
		catch(Exception e)
		{
			Toast.makeText(this, "Exception : "+e.toString(),Toast.LENGTH_LONG).show();
			return null;
		}
	}

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
			Toast.makeText(this, "Exception : "+e.toString(),Toast.LENGTH_LONG).show();
			return false;
		}
	}

	public void cancelTripCreation() 
	{
		try
		{
			setResult(RESULT_CANCELED, getIntent());
			Toast.makeText(this, "Trip Creation Cancelled!",Toast.LENGTH_SHORT).show();
			finish();
		}
		catch(Exception e)
		{
			Toast.makeText(this, "Exception : "+e.toString(),Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data) 
	{
		super.onActivityResult(reqCode, resultCode, data);
		String name = null;
		String phone = null;
		try
		{
			switch(reqCode)
		    {
		    	case (PICK_CONTACT):
		    		if (resultCode == Activity.RESULT_OK)
		    		{
		    			Uri contactData = data.getData();
		    			Cursor c = getContentResolver().query(contactData, null, null, null, null);
		    			if (c.moveToFirst())
		    			{
		    				String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

		    				String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

		    				if (hasPhone.equalsIgnoreCase("1")) 
		    				{
		    					Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null, 
		                             ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,null, null);
		    					phones.moveToFirst();
		    					phone = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
		    					
		    					name = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));

		    					String fullname[] = name.split(" ");
						    	EditText n = (EditText) findViewById(FRIENDNAMEIDPREFIX + frnd_num);
						    	n.setText(fullname[0]);
						    	
						    	EditText p = (EditText) findViewById(FRIENDPHONEIDPREFIX + frnd_num);
						    	p.setText(phone);
		    				}
		    			}
		    		}
		    		break;
		    }
		}	
		catch(Exception e)
		{
			Toast.makeText(this, "Exception : "+e.toString(),Toast.LENGTH_LONG).show();
		}
	}
}
