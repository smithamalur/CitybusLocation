package com.mctc.tram_location;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class testingdelete extends Activity {

	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Bundle bundle = getIntent().getExtras();
		
		String lat = bundle.getString("startpoint");
		Toast.makeText(getApplicationContext(), lat,Toast.LENGTH_SHORT).show();
	}
	
	

}
