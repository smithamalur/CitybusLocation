package com.mctc.tram_location;

import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Routesinfos1 extends Activity {

	String server_ip,stop_location,bustop,latlon;
	

	TextView busstops;
	String[] available_Titles = null;

	TextView[] Busstops_text = new TextView[20];
	Button[] Directionbuttons = new Button[20];
	LinearLayout linearLayout;
	ArrayList<Button> ButtonViewArraylist = new ArrayList<Button>();
	ArrayList<String> button_locinfo_list = new ArrayList<String>();



	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.bus_stops);

		Bundle bundle = getIntent().getExtras();
		
		server_ip = bundle.getString("server_ip");
		bustop = bundle.getString("bustop");

		busstops = (TextView) findViewById(R.id.textView1);
		linearLayout = (LinearLayout) findViewById(R.id.buttonLayout);

		String URL = "http://" + server_ip+ ":8080/TramLocation_Server/services/Service?wsdl";
		String NAME_SPACE = "http://mctc.arr.com";
		String METHOD_NAME = "busroutes";
		String SOAP_ACTION = "http://mctc.arr.com/busroutes";

		SoapObject soapObject, soapResult;
		SoapSerializationEnvelope envelope;
		HttpTransportSE httpTransportSE;

		try {
			soapObject = new SoapObject(NAME_SPACE, METHOD_NAME);

			soapObject.addProperty("bustop", bustop);

			envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.setOutputSoapObject(soapObject);

			httpTransportSE = new HttpTransportSE(URL);

			httpTransportSE.call(SOAP_ACTION, envelope);

			soapResult = (SoapObject) envelope.bodyIn;
			String result = soapResult.getProperty(0).toString();
			
			if (result.equalsIgnoreCase("anyType{}") || result.equals("")
					|| result.equals(null)) {
				linearLayout.removeAllViews();
				Toast.makeText(Routesinfos1.this, "No Details found!",
						Toast.LENGTH_SHORT).show();

			} else {

				linearLayout.removeAllViews();

				available_Titles = result.split("ARR");

				for (int i = 0; i < available_Titles.length; i++) {
					Busstops_text[i] = new TextView(getApplicationContext());
					Directionbuttons[i] = new Button(getApplicationContext());

					Log.d("title is", available_Titles[i]);

					Busstops_text[i].setText(available_Titles[i]);
					Directionbuttons[i].setText("Track Bus");
					Busstops_text[i].setClickable(true);
					Directionbuttons[i].setClickable(true);

					Directionbuttons[i].setMaxWidth(10);
					Directionbuttons[i].setMinWidth(10);
					Directionbuttons[i].setWidth(10);

					ButtonViewArraylist.ensureCapacity(ButtonViewArraylist.size() + 1);
					button_locinfo_list.ensureCapacity(button_locinfo_list.size() + 1);

					ButtonViewArraylist.add(Directionbuttons[i]);
					
					//Bus No:94A
					//From:City Bus Stand
					
					int st = available_Titles[i].indexOf("Bus No:");
					int en = available_Titles[i].indexOf("From:");
					String con = available_Titles[i].substring(st + 7, en);
					button_locinfo_list.add(con);

					Busstops_text[i].setId(i);
					Directionbuttons[i].setId(i);

					Directionbuttons[i].setOnClickListener(textViewListener);
					Busstops_text[i].setTextSize(21);
					Busstops_text[i].setTextColor(getResources().getColor(
							com.mctc.tram_location.R.color.red1));
					linearLayout.addView(Busstops_text[i]);
					linearLayout.addView(Directionbuttons[i]);

				}
			}

			

		} catch (Exception e) {
			Toast.makeText(Routesinfos1.this, e.toString(), Toast.LENGTH_LONG)
					.show();

		}


	}

	OnClickListener textViewListener = new OnClickListener() {

		public void onClick(View v) {

			int buttonId = v.getId();

			String busnum = button_locinfo_list.get(buttonId);
			Log.d("bus number", busnum);
			Toast.makeText(getApplicationContext(), busnum, Toast.LENGTH_LONG).show();

		

		}
	};

}
