package com.arr.tramlocation_bus;

import android.os.Bundle;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	String ip="";
	
	Button fromcbsButton;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ThreadPolicy policy = new ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		Bundle bundle = getIntent().getExtras();
		ip = bundle.getString("ip");
		
		fromcbsButton = (Button) findViewById(R.id.from_button);
	
		
		fromcbsButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) 
			{

			
					
					Intent intent = new Intent();
					intent.setClass(getApplicationContext(), Fromcbs.class);
					intent.putExtra("ip", ip);
					startActivity(intent);
					
					
				
				
			}
		});
		
		

		
		
	}


	

}
