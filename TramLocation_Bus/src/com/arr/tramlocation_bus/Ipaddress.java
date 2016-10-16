package com.arr.tramlocation_bus;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Ipaddress extends Activity {

	EditText ipaddressEditText;
	Button  setButton;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ipaddress);
		
		
		ipaddressEditText =(EditText) findViewById(R.id.editText1);
		setButton =(Button) findViewById(R.id.ipaddress_button);
		
		
		ipaddressEditText.setText("192.168.1.160");
		setButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) 
			{

				String ip = ipaddressEditText.getText().toString();
				
				if(ip.equals(null)||ip.equals(""))
				{
					Toast.makeText(getApplicationContext(), "Pls enter ip address", Toast.LENGTH_LONG).show();
				}else
				{
					
					Intent intent = new Intent();
					intent.setClass(getApplicationContext(), MainActivity.class);
					intent.putExtra("ip", ip);
					startActivity(intent);
					
					
				}
				
			}
		});
		
		
		
		
	}


	

}
