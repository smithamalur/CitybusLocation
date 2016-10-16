package com.mctc.tram_location;





import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Start_Activity extends Activity {
	
	TextView addressTextView;
	Button busstopsButton;
	
	
	String latitude="";
	String longitude="";
	String latlon="";
	String server_ip="";
	
	private LocationManager locatemanager;
	private LocationListener locateListener = new MyLocationListener();

	private boolean GPSON = false;
	private boolean OPERATORON = false;
	
	String lastcalltime_min = "";
	
	int parse_Status = 0;


	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.startpage);
		
		
		addressTextView =(TextView) findViewById(R.id.address_textView);
		busstopsButton =(Button) findViewById(R.id.button1);
		
		Bundle bundle = getIntent().getExtras();
		server_ip = bundle.getString("server_ip");
		
		
		
		
		
		addressTextView.setMovementMethod(ScrollingMovementMethod.getInstance());
		
		
		 Typeface fontbutton = Typeface.createFromAsset(getAssets(), "Trireme_3D_Italic.ttf");
		 
		 busstopsButton.setTypeface(fontbutton);
		 
		 
		 
		 busstopsButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				
				
				
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), Bus_Stops.class);
				intent.putExtra("server_ip", server_ip);
				intent.putExtra("latlon", latlon);
				startActivity(intent);
				
				
				
			}
		});
		 
		
		locatemanager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

		try {
			GPSON = locatemanager.isProviderEnabled(LocationManager.GPS_PROVIDER);
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
	
	class MyLocationListener implements LocationListener {

		public void onLocationChanged(Location mylocation) {

			
			try {
				latitude = ""+mylocation.getLatitude();
				longitude = "" + mylocation.getLongitude();
				
				latlon = latitude+","+longitude;
				addressTextView.setText(latlon);
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
				
			
			if(latitude.equals(null)||latitude.equals(""))
			{
				
			}else{
				
				try {
					
					if(parse_Status == 0)
					{
						Json_parsing json_parsing = new Json_parsing();
						String pasrseresult = json_parsing.givelatlon(latitude+","+longitude);
						parse_Status = 1;
						addressTextView.setText(pasrseresult);
						Log.d("pasrseresult", pasrseresult);
						
					}
					
			
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			
				
			}


			
			
		}

		public void onProviderDisabled(String provider) {

		}

		public void onProviderEnabled(String provider) {

		}

		public void onStatusChanged(String provider, int status, Bundle extras) {

		}
	}
	
	

}
