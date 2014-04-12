package com.nyu.cs9033.eta.database;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.nyu.cs9033.eta.models.Person;
import com.nyu.cs9033.eta.models.Trip;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TripDatabaseHelper extends SQLiteOpenHelper
{
	private static final int DATABASE_VERSION = 3;
	private static final String DATABASE_NAME = "trips";
	
	private static final String TABLE_TRIP = "trip";
	private static final String C_TRIP_ID = "_id";
	private static final String C_TRIP_NAME = "trip_name";
	private static final String C_TRIP_DESTINATION = "trip_destination";
	private static final String C_TRIP_CREATOR = "trip_creator";
	private static final String C_TRIP_DATE = "trip_date";
	
	private static final String TABLE_PERSON = "person";
	private static final String C_PERSON_ID = "_id";
	private static final String C_PERSON_NAME = "person_name";
	private static final String C_PERSON_LOCATION = "person_location";
	private static final String C_PERSON_PHONE = "person_phone";
	private static final String C_PERSON_TRIP_ID = "person_trip_id";

	private String colsTrip[] = {C_TRIP_ID, C_TRIP_NAME, C_TRIP_DESTINATION, C_TRIP_CREATOR, C_TRIP_DATE};
	private String colsPerson[] = {C_PERSON_ID, C_PERSON_NAME, C_PERSON_LOCATION, C_PERSON_TRIP_ID};
	private String colsTripHistoryList[] = {C_TRIP_ID, C_TRIP_NAME};
	
	public static final String TAG = "TripDatabaseHelper";
	
	public String[] getColsTrip() {
		return colsTrip;
	}

	public String[] getColsPerson() {
		return colsPerson;
	}

	public String[] getColsTripHistoryList() {
		return colsTripHistoryList;
	}
	
	public TripDatabaseHelper(Context context) 
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		db.execSQL("CREATE TABLE " + TABLE_TRIP + "("
				+ C_TRIP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ C_TRIP_NAME + " VARCHAR(100), "
				+ C_TRIP_DESTINATION + " VARCHAR(100), "
				+ C_TRIP_CREATOR + " VARCHAR(50), "
				+ C_TRIP_DATE + " VARCHAR(10))");
		
		db.execSQL("CREATE TABLE " + TABLE_PERSON + "("
				+ C_PERSON_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ C_PERSON_NAME + " VARCHAR(50), "
				+ C_PERSON_LOCATION + " VARCHAR(100), "
				+ C_PERSON_PHONE + " VARCHAR(20), "
				+ C_PERSON_TRIP_ID + " INTEGER)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIP);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERSON);
		
		onCreate(db);
	}
	
	public long insertTrip(Trip trip)
	{
		ContentValues cv = new ContentValues();
		cv.put(C_TRIP_NAME, trip.getTripName());
		cv.put(C_TRIP_DESTINATION, trip.getDestinationName());
		cv.put(C_TRIP_CREATOR, trip.getCreator());
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		String d = df.format(trip.getDate());
		cv.put(C_TRIP_DATE, d);
		long id = getWritableDatabase().insert(TABLE_TRIP, null, cv);
		//Log.i(TAG, "InsertTrip");
		return id;
	}

	public long insertPerson(long trip_id, Person person)
	{
		ContentValues cv = new ContentValues();
		cv.put(C_PERSON_NAME, person.getName());
		cv.put(C_PERSON_LOCATION, person.getLocation());
		cv.put(C_PERSON_PHONE, person.getPhone());
		cv.put(C_PERSON_TRIP_ID, trip_id);
		
		return getWritableDatabase().insert(TABLE_PERSON, null, cv);
	}
	
	public Cursor getAllTrips()
	{
		Cursor c = getReadableDatabase().query(TripDatabaseHelper.TABLE_TRIP, colsTrip, null, null, null, null, null);
		return c;
	}
	
	public ArrayList<Person> getTripPersons(long trip_id)
	{
		Cursor c = getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_PERSON + " WHERE " + C_PERSON_TRIP_ID + " = " + trip_id, null);
		c.moveToFirst();
		
		ArrayList<Person> alp = new ArrayList<Person>();
		while(!c.isAfterLast())
		{
			Person p = new Person(c.getString(1), c.getString(2), c.getString(3));
			
			alp.add(p);
			c.moveToNext();
		}
		c.close();
		
		return alp;
	}
	
	public Trip getTrip(long trip_id)
	{
		Cursor c = getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_TRIP + " WHERE " + C_TRIP_ID + " = " + trip_id, null);
		c.moveToFirst();
		
		Trip t = new Trip();
		try
		{
			t.setTripId(c.getLong(0));
			t.setTripName(c.getString(1));
			t.setDestinationName(c.getString(2));
			t.setCreator(c.getString(3));
			Date date = new SimpleDateFormat("MM/dd/yyyy").parse(c.getString(4));
			t.setDate(date);
			t.setFriends(null);
			
			c.close();
		}
		catch(Exception e)
		{
			
		}
		
		
		return t;
	}
	
	public void deleteTrip(long trip_id)
	{
		getWritableDatabase().delete(TABLE_TRIP, C_TRIP_ID + "=" + trip_id, null);
		getWritableDatabase().delete(TABLE_PERSON, C_PERSON_TRIP_ID + "=" + trip_id, null);
	}
}
