package com.nyu.cs9033.eta.models;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Trip implements Parcelable 
{
	protected long tripId;
	protected long serverRefId;
	protected String tripName;
	protected String destinationName;
	protected String creator;
	protected Date date;
	protected ArrayList<Person> friends = new ArrayList<Person>();
	
	private static final String TAG = "TRIP";
	
	public long getTripId() {
		return tripId;
	}
	public void setTripId(long tripId) {
		this.tripId = tripId;
	}
	public long getServerRefId() {
		return serverRefId;
	}
	public void setServerRefId(long serverRefId) {
		this.serverRefId = serverRefId;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public ArrayList<Person> getFriends() {
		return friends;
	}
	public void setFriends(ArrayList<Person> friends) {
		this.friends = friends;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getTripName() {
		return tripName;
	}
	public void setTripName(String tripName) {
		this.tripName = tripName;
	}
	public String getDestinationName() {
		return destinationName;
	}
	public void setDestinationName(String destinationName) {
		this.destinationName = destinationName;
	}

	public JSONObject toJSON()
	{
		JSONObject json = new JSONObject();
		try 
		{
			json.put("command", "CREATE_TRIP");
			json.put("location", destinationName);
			json.put("datetime", date.getTime());
			
			ArrayList<String> people = new ArrayList<String>();
			for(Person p : friends)
			{
				people.add(p.getName());
			}
			json.put("people", new JSONArray (people));
		}
		catch (Exception e) 
		{
			Log.i(TAG, "Exception in toJSON :" + e.toString());
		}
		return json;
	}

	
	//Date date;
	public static final Parcelable.Creator<Trip> CREATOR = new Parcelable.Creator<Trip>() {
		public Trip createFromParcel(Parcel p) {
			return new Trip(p);
		}

		public Trip[] newArray(int size) {
			return new Trip[size];
		}
	};
	
	public Trip()
	{
		this.serverRefId = 0;
	}
	
	/**
	 * Create a Trip model object from a Parcel
	 * 
	 * @param p The Parcel used to populate the
	 * Model fields.
	 */
	public Trip(Parcel p) 
	{
		p.readTypedList(friends, Person.CREATOR);
		tripId = p.readLong();
		serverRefId = p.readLong();
		tripName = p.readString();
		destinationName = p.readString();
		creator = p.readString();
		date = new Date(p.readLong());
	}
	
	/**
	 * Create a Trip model object from arguments
	 * @param date 
	 * 
	 * @param args Add arbitrary number of arguments to
	 * instantiate trip class.
	 */
	public Trip(long tripId, long serverRefId, String tripName, String destinationName, String creator, Date date, ArrayList<Person> friends)
	{
		this.tripId = tripId;
		this.serverRefId = serverRefId;
		this.tripName = tripName;
		this.destinationName = destinationName;
		this.creator = creator;
		this.date = date;
		this.friends = friends;
	}

	@Override
	public void writeToParcel(Parcel out, int arg1) 
	{
		out.writeTypedList(friends);
		out.writeLong(tripId);
		out.writeLong(serverRefId);
		out.writeString(tripName);
		out.writeString(destinationName);
		out.writeString(creator);
		out.writeLong(date.getTime());
	}
	
	/**
	 * Do not implement
	 */
	@Override
	public int describeContents() {
		// Do not implement!
		return 0;
	}
}
