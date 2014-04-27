package com.nyu.cs9033.eta.controllers;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nyu.cs9033.eta.database.TripDatabaseHelper;
import com.nyu.cs9033.eta.helper.AsyncResponse;
import com.nyu.cs9033.eta.helper.SendPostRequestHelper;
import com.nyu.cs9033.eta.models.Trip;
import com.nyu.cs9033.eta.R;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.util.Log;
import android.view.View;

public class MainActivity extends Activity implements AsyncResponse
{
	private static final String TAG = "MAINACTIVITY";
	private static final int STATUSLAYOUTID = 151500;
	public SendPostRequestHelper req = new SendPostRequestHelper();
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		try
		{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main_linear);
			req.transferResult = this;
			
			showRecentTripStatus();
		}
		catch(Exception e)
		{
			Log.i(TAG, "Exception in onCreate :" + e.toString());
			Toast.makeText(this, "Exception in onCreate : "+e.toString(),Toast.LENGTH_LONG).show();
		}
	}
	
	private void showRecentTripStatus() 
	{
		try
		{
			TripDatabaseHelper db = new TripDatabaseHelper(this);
			
			Trip t = db.getMostRecentTrip();
			if(t != null)
				showStatus(t);
		}
		catch(Exception e)
		{
			Log.i(TAG, "Exception in showRecentTripStatus :" + e.toString());
			Toast.makeText(this, "Exception in showRecentTripStatus : "+e.toString(),Toast.LENGTH_LONG).show();
		}
	}

	private void showStatus(Trip t) 
	{
		try
		{
			JSONObject json = new JSONObject();
			json.put("command", "TRIP_STATUS");
			json.put("trip_id", t.getServerRefId());
			
			if(isConnected())
			{
				req = new SendPostRequestHelper();
				req.transferResult = this;
				req.execute(json);
			}
		}
		catch(Exception e)
		{
			Log.i(TAG, "Exception in showStatus :" + e.toString());
			Toast.makeText(this, "Exception in showStatus : "+e.toString(),Toast.LENGTH_LONG).show();
		}
	}

	public void onClick(View v)
	{
		try 
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
		catch (Exception e) 
		{
			Log.i(TAG, "Exception in onClick :" + e.toString());
			Toast.makeText(this, "Exception in onClick : "+e.toString(),Toast.LENGTH_LONG).show();
		}
	}
	
	public void startTripCreator() 
	{
		try
		{
			Intent intent = new Intent(this, CreateTripActivity.class);
			startActivityForResult(intent, 1);
		}
		catch(Exception e)
		{
			Log.i(TAG, "Exception in startTripCreator :" + e.toString());
			Toast.makeText(this, "Exception in startTripCreator : "+e.toString(),Toast.LENGTH_LONG).show();
		}
	}
	
	public void startTripHistoryViewer() 
	{
		try
		{
			Intent intentTH = new Intent(this, TripHistoryActivity.class);
			startActivity(intentTH); 
		}
		catch(Exception e)
		{
			Log.i(TAG, "Exception in startTripHistoryViewer :" + e.toString());
			Toast.makeText(this, "Exception in startTripHistoryViewer : "+e.toString(),Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data)
	{
		try
		{
			if(resultCode == RESULT_OK)
			{
				Bundle b = data.getExtras();
				Trip t = b.getParcelable("trip");
				showStatus(t);
			}
			else if(resultCode == RESULT_CANCELED)
			{
				showRecentTripStatus();
			}
		}
		catch(Exception e)
		{
			Log.i(TAG, "Exception in onActivityResult :" + e.toString());
			Toast.makeText(this, "Exception in onActivityResult : "+e.toString(),Toast.LENGTH_LONG).show();
		}
	}

	public int dp(int dps)
	{
		final float scale = this.getResources().getDisplayMetrics().density;
		int pixels = (int) (dps * scale + 0.5f);
		return pixels;
	}
	
	@Override
	public void processFinish(JSONObject output) 
	{
		try
		{
			//Toast.makeText(this, output.toString(),Toast.LENGTH_LONG).show();
			if(output != null)
			{
				JSONArray dl = output.getJSONArray("distance_left");
				JSONArray tl = output.getJSONArray("time_left");
				JSONArray p = output.getJSONArray("people");
				
				LinearLayout parent = (LinearLayout) findViewById(R.id.statuslayout);
				
				if(parent.getChildCount() > 1)
				{
					for(int i = 0; i < parent.getChildCount(); i++)
					{
						parent.removeView(findViewById(STATUSLAYOUTID + i));
					}
				}
				for(int i = 0; i < p.length(); i++)
				{
					String dlo = dl.getString(i);
					String tlo = tl.getString(i);
					String po = p.getString(i);
					
					
					LinearLayout child = new LinearLayout(this);
					LayoutParams childParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
					childParams.topMargin = dp(5);
					child.setLayoutParams(childParams);
					child.setOrientation(LinearLayout.HORIZONTAL);
					child.setId(STATUSLAYOUTID + i);
					
					TextView ptext = new TextView(this);
					LayoutParams personParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					ptext.setLayoutParams(personParams);
					ptext.setText(po);
					
					TextView dltext = new TextView(this);
					LayoutParams distleftParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					distleftParams.leftMargin = dp(50);
					dltext.setLayoutParams(distleftParams);
					dltext.setText(dlo);
					
					View v = new View(this);
					LayoutParams vparams = new LayoutParams(0, 0);
					
					v.setLayoutParams(vparams);
					
					TextView tltext = new TextView(this);
					LayoutParams timeleftParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					timeleftParams.leftMargin = dp(50);
					tltext.setLayoutParams(timeleftParams);
					tltext.setText(tlo);
					
					child.addView(ptext);
					child.addView(dltext);
					child.addView(v);
					child.addView(tltext);
					
					parent.addView(child);
				}
			}
		}
		catch(Exception e)
		{
			Log.i(TAG, "Exception in processFinish :" + e.toString());
			Toast.makeText(this, "Exception in processFinish : "+e.toString(),Toast.LENGTH_LONG).show();
		}
	}
	
	public boolean isConnected()
	{
		try
		{
			ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo ni = cm.getActiveNetworkInfo();
			return ni!=null && ni.isConnected();
		}
		catch(Exception e)
		{
			Log.i(TAG, "Exception in isConnected :" + e.toString());
			Toast.makeText(this, "Exception in isConnected : "+e.toString(),Toast.LENGTH_LONG).show();
		}
		return false;
	}
}
