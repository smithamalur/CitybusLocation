package com.arr.tramlocation_bus;

import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;





import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Fromcbs extends Activity  {

	
	
	LinearLayout linearLayout;
	Button[] textViews = new Button[500];
	ArrayList<TextView> textList = new ArrayList<TextView>();
	
	
	String ip="",route_result="";
	//TextView busrouteTextView;
	//Button updateButton;
	String IMEI="",latit="",longit="";
	String nextroutes[]=null;
	ArrayList<String> routesinfo = new ArrayList<String>();
	
	
	double mySpeed, maxSpeed;
	private LocationManager locatemanager;
	private LocationListener locateListener = new MyLocationListener();

	private boolean GPSON = false;
	private boolean OPERATORON = false;
	
	
	ProgressDialog dialog;
	Handler handler;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.from_citybs);
		
		
		handler = new Handler();
		
		dialog = new ProgressDialog(this);
		dialog.setTitle("Connecting Server");
		dialog.setMessage("Pls wait..");
		dialog.setCancelable(false);
		
	//	ThreadPolicy policy = new ThreadPolicy.Builder().permitAll().build();
		//StrictMode.setThreadPolicy(policy);
		
		
		Bundle bundle = getIntent().getExtras();
		ip = bundle.getString("ip");
		
		//busrouteTextView =(TextView) findViewById(R.id.textView1);
		//updateButton = (Button) findViewById(R.id.updatebutton2);
		
		 linearLayout = (LinearLayout) findViewById(R.id.linearLayout1);
	        
		
		
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		IMEI = telephonyManager.getDeviceId();
		
		Getroutedetails getroutedetails = new Getroutedetails();
		getroutedetails.execute(new String[]{"routeinfo"});
		
		locatemanager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

		try {
			GPSON = locatemanager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (Exception ex) {
		}

		try {
			OPERATORON = locatemanager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (Exception ex) {

		}

		if (!GPSON && !OPERATORON) {
			Toast.makeText(
					getApplicationContext(),
					"Sorry, location is not determined. Please enable location providers",
					Toast.LENGTH_SHORT).show();
		}

		if (GPSON) {
			locatemanager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
					0, 0, locateListener);
		}

		if (OPERATORON) {
			locatemanager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, 0, 0, locateListener);
		}

		


		
		 
		
	}
	
	public void routesinfo()
	{
		
		
		String URL = "http://" +ip+ ":8080/TramLocation_Server/services/Service?wsdl";
		String NAME_SPACE = "http://mctc.arr.com";
		String METHOD_NAME = "myroute";
		String SOAP_ACTION = "http://mctc.arr.com/myroute";

		SoapObject soapObject, soapResult;
		SoapSerializationEnvelope envelope;
		HttpTransportSE httpTransportSE;

		try {
			soapObject = new SoapObject(NAME_SPACE, METHOD_NAME);

			
			soapObject.addProperty("imei", IMEI);

			envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.setOutputSoapObject(soapObject);
			httpTransportSE = new HttpTransportSE(URL);
			httpTransportSE.call(SOAP_ACTION, envelope);
			soapResult = (SoapObject) envelope.bodyIn;
			route_result = soapResult.getProperty(0).toString();
			

			
		
		} catch (Exception e) {
			Toast.makeText(Fromcbs.this, e.toString(), Toast.LENGTH_LONG)
					.show();

		}
	
	}
	
	class MyLocationListener implements LocationListener {

		public void onLocationChanged(Location mylocation) {

			
			 latit = "" + mylocation.getLatitude();
			 longit = "" + mylocation.getLongitude();
			
			Toast.makeText(getApplicationContext(), latit+","+longit, Toast.LENGTH_LONG).show();


		}

		public void onProviderDisabled(String provider) {

		}

		public void onProviderEnabled(String provider) {

		}

		public void onStatusChanged(String provider, int status, Bundle extras) {

		}
	}

	
	
	OnClickListener textViewListener = new OnClickListener() {
		
		public void onClick(View v) {
			int buttonId = v.getId();
			
			Button button = (Button) v;
			button.setBackgroundColor(Color.GREEN);
			
			String buttonTitle = textList.get(buttonId).getText().toString();
			Toast.makeText(getApplicationContext(), buttonTitle, Toast.LENGTH_SHORT).show();
			
			routesinfo.remove(buttonTitle);
			
			String nextstopis = routesinfo.toString();
			
				UpdateTrackdetails trackdetails  = new UpdateTrackdetails();
				trackdetails.execute(new String[]{nextstopis});
	
			
		}
	};

	
	class Getroutedetails extends AsyncTask<String, Void, String>
	{
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub

				handler.post(new Runnable() {
					
					public void run() {
						// TODO Auto-generated method stub
						dialog.show();
					}
				});
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			routesinfo();
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			
			handler.post(new Runnable() {
				
				public void run() {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});
			
			String[] available_resource = route_result.split(",");
			
			
			
			nextroutes = route_result.split(",");
			
			for (int i = 0; i < nextroutes.length; i++) {
				routesinfo.add(nextroutes[i]);
			}
			
			
			
			if(available_resource.length == 0) {
    			Toast.makeText(getApplicationContext(), "No place found", Toast.LENGTH_LONG).show();
    		}else {
    			
    			for(int i = 0; i < available_resource.length ; i++) {
    				
    				textViews[i] = new Button(getApplicationContext());
    				
    				
    				SpannableString resorceName = SpannableString.valueOf(available_resource[i]);
             		resorceName.setSpan(new URLSpan(""), 0, available_resource[i].length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
             		
             		textViews[i].setText(resorceName);
             		textViews[i].setClickable(true);
    				textViews[i].setId(i);
    				
    				textViews[i].setOnClickListener(textViewListener);
    				
    				textList.add(textViews[i]);
    				
    				textViews[i].setTextSize(21);
    				textViews[i].setTextColor(Color.RED);
    				
    				linearLayout.addView(textViews[i]);
    			}
    		}
			super.onPostExecute(result);
		}
		
	}
	
	
	class UpdateTrackdetails extends AsyncTask<String, Void, String>
	{
		
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			
			handler.post(new Runnable() {
				
				public void run() {
					// TODO Auto-generated method stub
					dialog.show();
				}
			});
			
			super.onPreExecute();
		}
		

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			
			String URL = "http://" +ip+ ":8080/TramLocation_Server/services/Service?wsdl";
			String NAME_SPACE = "http://mctc.arr.com";
			String METHOD_NAME = "update_track";
			String SOAP_ACTION = "http://mctc.arr.com/update_track";

			SoapObject soapObject, soapResult;
			SoapSerializationEnvelope envelope;
			HttpTransportSE httpTransportSE;

			try {
				soapObject = new SoapObject(NAME_SPACE, METHOD_NAME);
				soapObject.addProperty("imei", IMEI);
				soapObject.addProperty("loc", latit+","+longit);
				soapObject.addProperty("dest", "up");
				soapObject.addProperty("nextstops", arg0[0]);
				

				
				envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
				envelope.setOutputSoapObject(soapObject);
				httpTransportSE = new HttpTransportSE(URL);
				httpTransportSE.call(SOAP_ACTION, envelope);
				soapResult = (SoapObject) envelope.bodyIn;
				
			
			} catch (Exception e) {
				
				Log.d("exception", e.toString());
				

			}
			
			return null;
		}
		
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			
			handler.post(new Runnable() {
				
				public void run() {
				
					dialog.dismiss();
					
				}
			});
			
			
			super.onPostExecute(result);
		}
		
	}
}
