package com.nyu.cs9033.eta.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Person implements Parcelable 
{
	protected String name;
	protected String location;
	protected String phone;
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}

	
	
	public static final Parcelable.Creator<Person> CREATOR = new Parcelable.Creator<Person>() {
		public Person createFromParcel(Parcel p) {
			return new Person(p);
		}

		public Person[] newArray(int size) {
			return new Person[size];
		}
	};
	
	/**
	 * Create a Person model object from a Parcel
	 * 
	 * @param p The Parcel used to populate the
	 * Model fields.
	 */
	public Person(Parcel p) 
	{
		name = p.readString();
		location = p.readString();
		phone = p.readString();
	}
	
	/**
	 * Create a Person model object from arguments
	 * 
	 * @param args Add arbitrary number of arguments to
	 * instantiate trip class.
	 */
	public Person(String name, String location, String phone) 
	{
		this.name = name;
		this.location = location;
		this.phone = phone;
	}

	@Override
	public void writeToParcel(Parcel out, int arg1) 
	{
		out.writeString(name);
		out.writeString(location);
		out.writeString(phone);
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
