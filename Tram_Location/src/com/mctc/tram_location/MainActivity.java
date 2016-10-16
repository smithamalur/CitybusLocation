package com.mctc.tram_location;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;

public class MainActivity extends Activity {
	
	TextView ipaddressTextView;
	Button setipButton;
	EditText ipaddEditText;
	
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        ipaddressTextView = (TextView) findViewById(R.id.textView1);
        setipButton = (Button) findViewById(R.id.button1);
        ipaddEditText =(EditText) findViewById(R.id.editText1);
        
        Typeface fontbutton = Typeface.createFromAsset(getAssets(), "Trireme_3D_Italic.ttf");
        Typeface headingTypeface = Typeface.createFromAsset(getAssets(), "Alexis_Expanded_Italic.ttf");


        setipButton.setTypeface(fontbutton);
        ipaddressTextView.setTypeface(headingTypeface);
        
        setipButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {

				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), Start_Activity.class);
				intent.putExtra("server_ip", ipaddEditText.getText().toString());
				startActivity(intent);
				
				
			}
		});
        
        
        
    }


    
}
