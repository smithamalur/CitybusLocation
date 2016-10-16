package com.mctc.tram_location;

import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.mctc.tram_location.R.color;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Bus_Stops extends Activity {

	String server_ip = "";
	String latlon = "";

	TextView busstops;
	String[] available_Titles = null;

	TextView[] Busstops_text = new TextView[50];
	Button[] Directionbuttons = new Button[50];
	LinearLayout linearLayout;
	ArrayList<Button> ButtonViewArraylist = new ArrayList<Button>();
	ArrayList<String> button_locinfo_list = new ArrayList<String>();

	ProgressDialog dialog;

	Handler handler;

	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.bus_stops);
		
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		Bundle bundle = getIntent().getExtras();
		server_ip = bundle.getString("server_ip");
		latlon = bundle.getString("latlon");

		busstops = (TextView) findViewById(R.id.textView1);
		linearLayout = (LinearLayout) findViewById(R.id.buttonLayout);

		handler = new Handler();

		dialog = new ProgressDialog(this);
		dialog.setTitle("Fetching info from server");
		dialog.setMessage("Pls wait..");
		dialog.setCancelable(false);
		
		try {
			new GetInfo().execute(server_ip, latlon);
		} catch (Exception e) {
			Toast.makeText(this,
					"Some Error while contacting server.. " + e.toString(),
					Toast.LENGTH_LONG).show();
		}

	}

	OnClickListener textViewListener = new OnClickListener() {

		public void onClick(View v) {

			int buttonId = v.getId();
			Button button = (Button) v;
			button.setBackgroundColor(Color.GREEN);

			String infois = available_Titles[buttonId];
			int Name_poistion = infois.indexOf("Name:");
			int loc_poistion = infois.indexOf("Location:");
			String busstopname = infois.substring(Name_poistion + 5,
					loc_poistion);

			String contentis = button_locinfo_list.get(buttonId);

			Intent intent = new Intent();

			intent.setClass(getApplicationContext(), Guide.class);

			intent.putExtra("server_ip", server_ip);
			intent.putExtra("latlon", latlon);
			intent.putExtra("stop_location", contentis);
			intent.putExtra("bustop", busstopname);

			startActivity(intent);

		}
	};

	@Override
	protected void onResume() {
	
		super.onResume();
	}

	// Get info from server
	private class GetInfo extends AsyncTask<String, String, String> {
		@Override
		protected String doInBackground(String... params) {

			String URL = "http://" + params[0]
					+ ":8080/TramLocation_Server/services/Service?wsdl";
			String NAME_SPACE = "http://mctc.arr.com";
			//String METHOD_NAME = "bustops";
			String METHOD_NAME = "bustops_logic";
			
			String SOAP_ACTION = "http://mctc.arr.com/bustops";

			SoapObject soapObject, soapResult;
			SoapSerializationEnvelope envelope;
			HttpTransportSE httpTransportSE;

			try {
				soapObject = new SoapObject(NAME_SPACE, METHOD_NAME);

				
				
				
				soapObject.addProperty("latlon", params[1]);

				envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
				envelope.setOutputSoapObject(soapObject);

				httpTransportSE = new HttpTransportSE(URL);

				httpTransportSE.call(SOAP_ACTION, envelope);

				soapResult = (SoapObject) envelope.bodyIn;
				String result = soapResult.getProperty(0).toString();

				return result;

			} catch (Exception e) {
				Toast.makeText(Bus_Stops.this, e.toString(), Toast.LENGTH_LONG)
						.show();

			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {

			dialog.dismiss();
			
			Log.i("rakshi", result);

			if (result.equalsIgnoreCase("anyType{}") || result.equals("")
					|| result.equals(null)) {
				linearLayout.removeAllViews();
				Toast.makeText(Bus_Stops.this, "No Details found!",
						Toast.LENGTH_SHORT).show();

			} else {

				linearLayout.removeAllViews();
				
			

				available_Titles = result.split("ARR");
				
				

				for (int i = 0; i < available_Titles.length; i++) {
					
					Busstops_text[i] = new TextView(getApplicationContext());
					Directionbuttons[i] = new Button(getApplicationContext());

					Log.d("title is", available_Titles[i]);

					Busstops_text[i].setText(available_Titles[i]);
					Directionbuttons[i].setText("Select");
					Busstops_text[i].setClickable(true);
					Directionbuttons[i].setClickable(true);

					Directionbuttons[i].setMaxWidth(10);
					Directionbuttons[i].setMinWidth(10);
					Directionbuttons[i].setWidth(10);

					ButtonViewArraylist.ensureCapacity(ButtonViewArraylist.size() + 1);
					button_locinfo_list.ensureCapacity(button_locinfo_list.size() + 1);

					ButtonViewArraylist.add(Directionbuttons[i]);
					int st = available_Titles[i].indexOf(":[");
					int en = available_Titles[i].indexOf("]");
					String con = available_Titles[i].substring(st + 2, en);
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

			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			handler.post(new Runnable() {

				public void run() {
					dialog.show();

				}
			});

			super.onPreExecute();
		}
	}

}
