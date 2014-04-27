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

import android.os.AsyncTask;
import android.util.Log;

public class SendPostRequestHelper extends AsyncTask<JSONObject, Void, JSONObject>
{
	public AsyncResponse transferResult;
	private static final String TAG = "SENDPOSTREQUESTHELPER";
	@Override
	protected JSONObject doInBackground(JSONObject... params) 
	{
		JSONObject responseJson = null;
		try
		{
			JSONObject json = params[0];
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
		    responseJson = new JSONObject(resultstring);
		}
		catch (Exception e) 
		{
			Log.i(TAG, "Exception in doInBackground :" + e.toString());
			
		}
		return responseJson;
	}
	
	@Override
	protected void onPostExecute(JSONObject result) 
	{
		//super.onPostExecute(result);
		transferResult.processFinish(result);
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
}
