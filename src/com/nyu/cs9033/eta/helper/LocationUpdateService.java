package com.nyu.cs9033.eta.helper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class LocationUpdateService extends IntentService implements LocationListener
{
	Intent i;
	private static final String TAG = "LOCATIONUPLOADSERVICE";
	public LocationUpdateService() 
	{
		super(TAG);
	}
	
	@Override
	protected void onHandleIntent(Intent intent) 
	{
		try
		{
			i = intent;
			LocationManager lm = (LocationManager)getSystemService(this.LOCATION_SERVICE);
			if(lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
				updateLocation(lm);
		}
		catch(Exception e)
		{
			Log.i(TAG, "Exception in onHandleIntent :" + e.toString());
		}
	}
	
	public void updateLocation(LocationManager lm)
	{
		try
		{
			LocationUpdateService listener = new LocationUpdateService();
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 15000, 100, listener);//15minutes or 100meters
		}
		catch(Exception e)
		{
			Log.i(TAG, "Exception in updateLocation :" + e.toString());
		}
	}

	@Override
	public void onLocationChanged(Location location) 
	{
		try
		{
			double lat = location.getLatitude();
			double lon = location.getLongitude();
			long epoch = System.currentTimeMillis()/1000L;
			JSONObject json = new JSONObject();
			json.put("command", "UPDATE_LOCATION");
			json.put("latitude", lat);
			json.put("longitude", lon);
			json.put("datetime", epoch);
			
			String uri = "http://cs9033-homework.appspot.com/";
			HttpClient client = new DefaultHttpClient();
		    HttpPost post = new HttpPost(uri);
		    StringEntity se = new StringEntity(json.toString());
		    se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
		    post.setHeader("Accept", "application/json");
		    post.setHeader("Content-type", "application/json");
		    post.setEntity(se);
		    HttpResponse response = client.execute(post);
		    
		    HttpEntity resultentity = response.getEntity();
		    InputStream inputstream = resultentity.getContent();
		    String resultstring = convertStreamToString(inputstream);
		    inputstream.close();
		    JSONObject responseJson = new JSONObject(resultstring);
		    
		    if(responseJson.getString("response_code").equalsIgnoreCase("0"))
		    	Toast.makeText(this, "Location Updated",Toast.LENGTH_LONG).show();
		}
		catch(Exception e)
		{
			Log.i(TAG, "Exception in onLocationChanged :" + e.toString());
		}
	}
	private String convertStreamToString(InputStream is) 
	{
	    String line = "";
	    StringBuilder total = new StringBuilder();
	    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	    try 
	    {
	        while ((line = rd.readLine()) != null) 
	        {
	            total.append(line);
	        }
	    } 
	    catch (Exception e) 
	    {
	    	Log.i(TAG, "Exception in convertStreamToString :" + e.toString());
	    }
	    return total.toString();
	}
	@Override
	public void onProviderDisabled(String provider) {}

	@Override
	public void onProviderEnabled(String provider) {}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {}
}
