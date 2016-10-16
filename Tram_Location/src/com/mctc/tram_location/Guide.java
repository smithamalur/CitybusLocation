package com.mctc.tram_location;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Guide extends Activity {

	
	String server_ip,stop_location,bustop,latlon;
	TextView bustopnameTextView;
	Button routesButton,directonButton;
	
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guide);
		
		Bundle bundle = getIntent().getExtras();
		
		server_ip = bundle.getString("server_ip");
		stop_location = bundle.getString("stop_location");
		bustop = bundle.getString("bustop");
		latlon = bundle.getString("latlon");
		
		routesButton =(Button) findViewById(R.id.routes_button);
		directonButton =(Button) findViewById(R.id.button2);
		bustopnameTextView =(TextView) findViewById(R.id.textView1);
		bustopnameTextView.setText(bustop);
		
		
 Typeface fontbutton = Typeface.createFromAsset(getAssets(), "Trireme_3D_Italic.ttf");
		 
 		routesButton.setTypeface(fontbutton);
 		directonButton.setTypeface(fontbutton);
		
		
		routesButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				
					Intent intent = new Intent();
					intent.setClass(getApplicationContext(), Routesinfos.class);
					
					intent.putExtra("server_ip", server_ip);
					intent.putExtra("bustop", bustop);
					intent.putExtra("busstop_location", stop_location);
					startActivity(intent);
					

				
			}
		});
		
		
		
		
		directonButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				
				final Intent intent = new Intent(Intent.ACTION_VIEW,
 				       /** Using the web based turn by turn directions url. */
 				       Uri.parse("http://maps.google.com/maps?" +"saddr="+latlon+"" +"&daddr="+stop_location+""));	       
 				       startActivity(intent);
				
				
			}
		});
		
		
		
	}
	
	

}
