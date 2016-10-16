package com.mctc.tram_location;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Map_Activity extends Activity {

	TextView resulTextView;
	String[] available_Titles = null;
	
	String resultDistance =  "";	
	String resultTime ="";
	
	String server_ip,busno,busstop_location,bustopname;
	
	
	TextView[] time_distance_texviews = new TextView[20];
	Button[] Mapview_buttons = new Button[20];
	LinearLayout linearLayout;
	ArrayList<Button> ButtonViewArraylist = new ArrayList<Button>();
	ArrayList<String> button_locinfo_list = new ArrayList<String>();

	
	
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapbutton);
		
		Bundle bundle = getIntent().getExtras();
		
		server_ip = bundle.getString("server_ip");
		busno = bundle.getString("busno");
		busstop_location = bundle.getString("busstop_location");
		bustopname =  bundle.getString("bustopname");
		
		resulTextView =(TextView) findViewById(R.id.textView1);
		linearLayout = (LinearLayout) findViewById(R.id.buttonLayout);
		
		
		String URL = "http://" + server_ip+ ":8080/TramLocation_Server/services/Service?wsdl";
		String NAME_SPACE = "http://mctc.arr.com";
		String METHOD_NAME = "track_records";
		String SOAP_ACTION = "http://mctc.arr.com/track_records";

		SoapObject soapObject, soapResult;
		SoapSerializationEnvelope envelope;
		HttpTransportSE httpTransportSE;

		try {
			soapObject = new SoapObject(NAME_SPACE, METHOD_NAME);

			soapObject.addProperty("busno", busno);
			soapObject.addProperty("bustopname", bustopname);

			envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.setOutputSoapObject(soapObject);

			httpTransportSE = new HttpTransportSE(URL);

			httpTransportSE.call(SOAP_ACTION, envelope);

			soapResult = (SoapObject) envelope.bodyIn;
			String result = soapResult.getProperty(0).toString();

			
			dynamicbutton(result);

		} catch (Exception e) {
			Toast.makeText(Map_Activity.this, "1"+e.toString(), Toast.LENGTH_LONG)
					.show();

		}
		
		
	}
	
	public void dynamicbutton(String result)
	{
		
		
		if (result.equalsIgnoreCase("anyType{}") || result.equals("") || result.equals(null)|| result.equals("no")     ) {
			linearLayout.removeAllViews();
			Toast.makeText(Map_Activity.this, "No Details found!",
					Toast.LENGTH_SHORT).show();

		} else {

			linearLayout.removeAllViews();

			available_Titles = result.split("ARR");

			for (int i = 0; i < available_Titles.length; i++) {
				time_distance_texviews[i] = new TextView(getApplicationContext());
				Mapview_buttons[i] = new Button(getApplicationContext());

				Log.d("available_Titles title is", available_Titles[i]);
				
				Toast.makeText(Map_Activity.this, "available_Titles[i]"+available_Titles[i],Toast.LENGTH_SHORT).show();
				
				String info[] = available_Titles[i].split(">");
				String current_buspostion = info[1];
				String timeis = info[2];
				
				Log.d("current_buspostion", current_buspostion);
				Log.d("timeis", timeis);
				
				String jsonURL = "http://maps.googleapis.com/maps/api/directions/json?" +"origin="+busstop_location+"&" + "destination="+current_buspostion+"&sensor=false";
				
				Log.d("jsonURL", jsonURL);
				
    	       JSONObject jsonObject = getJSONfromURL(jsonURL);
    		   String distance_time = parseJSON(jsonObject);
    		   
    		   Log.d("distance_time", distance_time);
    		   String dis_tim [] =  distance_time.split("Rak");
    		   
    		   String display_content = i+1+")."+"Distance:"+ dis_tim[0]+"\n"+"Approximate Time to Reach your stop:"+dis_tim[1]+"\n"+"Last updated time:"+timeis;

    		   
    		   Log.d("display_content", display_content);
    		   
    		   
				time_distance_texviews[i].setText(display_content);
				
				Mapview_buttons[i].setText("View Map");
				time_distance_texviews[i].setClickable(true);
				Mapview_buttons[i].setClickable(true);

				Mapview_buttons[i].setMaxWidth(10);
				Mapview_buttons[i].setMinWidth(10);
				Mapview_buttons[i].setWidth(10);

				ButtonViewArraylist.ensureCapacity(ButtonViewArraylist.size() + 1);
				button_locinfo_list.ensureCapacity(button_locinfo_list.size() + 1);

				ButtonViewArraylist.add(Mapview_buttons[i]);
			
				button_locinfo_list.add(current_buspostion);

				time_distance_texviews[i].setId(i);
				Mapview_buttons[i].setId(i);

				Mapview_buttons[i].setOnClickListener(textViewListener);
				time_distance_texviews[i].setTextSize(21);
				time_distance_texviews[i].setTextColor(getResources().getColor(com.mctc.tram_location.R.color.red1));
				linearLayout.addView(time_distance_texviews[i]);
				linearLayout.addView(Mapview_buttons[i]);

			}
		}
		
		
	}
	
	
	
    /** Method to retrieve JSON Object from URL */
    public static JSONObject getJSONfromURL(String url) {

    	Log.d("url", url);
    	//initialize
		    InputStream inputStream = null;
		    String result = "";
		    JSONObject jArray = null;
		 
		    //http post
		    try{
		        HttpClient httpclient = new DefaultHttpClient();
		        HttpPost httppost = new HttpPost(url);
		        HttpResponse response = httpclient.execute(httppost);
		        HttpEntity entity = response.getEntity();
		        inputStream = entity.getContent();
		 
		    }catch(Exception e){
		    	Log.d("exception is22222222", e.toString());
		    	
		    }
		 
		    //convert response to string
		    try{
		        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"),8);
		        StringBuilder sb = new StringBuilder();
		        String line = null;
		        while ((line = reader.readLine()) != null) {
		            sb.append(line + "\n");
		        }
		        inputStream.close();
		        result=sb.toString();
		       // Log.e(TAG, "Got Result :  "+result);
		        
		    }catch(Exception e){
		    	Log.d("exception is33333333", e.toString());
		    }
		 
		    //try parse the string to a JSON object
		    try{
		           jArray = new JSONObject(result);
		    }catch(JSONException e){
		    	Log.d("exception is444444444", e.toString());
		    }
		   
		    return jArray;
		}
    
    
    /** Method to parse the JSON Object */
    public String parseJSON(JSONObject jsonObject){
    		try {
    			//get 1.1 i.e., 'routes' Array
				JSONArray jsonArray_routes_1 = jsonObject.getJSONArray("routes");
				
				//get 1.1.1 Object i.e., bounds
				//JSONObject jsonObject_routes_1_1_1 = null;
				JSONArray jsonArray_Legs = null; 
				for (int i = 0; i < jsonArray_routes_1.length(); i++) {
					// jsonObject_routes_1_1_1 = jsonArray_routes_1.getJSONObject(i).getJSONObject("bounds");
					 jsonArray_Legs = jsonArray_routes_1.getJSONObject(i).getJSONArray("legs");
				}
				
				//get from legs Array
				JSONObject legs_distance_Object  = null;
				JSONObject legs_duration_Object  = null;
				
				for(int i =0 ; i < jsonArray_Legs.length(); i++){
					legs_distance_Object = jsonArray_Legs.getJSONObject(i).getJSONObject("distance");
					
					legs_duration_Object = jsonArray_Legs.getJSONObject(i).getJSONObject("duration");
					
				}
				
			   String  totalDistance_km = legs_distance_Object.getString("text");
			   String  totalDuration_Min = legs_duration_Object.getString("text");
				
			   resultDistance =  totalDistance_km;	
			   resultTime = totalDuration_Min;

			} catch (JSONException e) {
				Log.d("Error in JSON Parsing555555555", e.toString());
				e.printStackTrace();
			}
    		
    		return resultDistance+"Rak"+resultTime;
    		
     } // parseJSON Method Close
    
	OnClickListener textViewListener = new OnClickListener() {

		public void onClick(View v) {

			int buttonId = v.getId();
			String buttoncontent = button_locinfo_list.get(buttonId);
			buttoncontent = buttoncontent.trim();

			
			Log.d("buttoncontent", buttoncontent);
        Intent intent = new Intent();
			intent.setClass(getBaseContext(),Map_view.class);
			intent.putExtra("startpoint", buttoncontent);
			startActivity(intent);
	

		}
	};
 
}
