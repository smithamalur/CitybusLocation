package com.mctc.tram_location;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;




public class Json_parsing {
	
	 String foramttedObject ="";
	
	public String givelatlon(String latandlon)
	
	{
		Log.d("latand lon", latandlon);
		String jsonURL = "http://maps.googleapis.com/maps/api/geocode/json?latlng="+latandlon+"&sensor=true";
		 JSONObject jsonObject = getJSONfromURL(jsonURL);
		  parseJSON(jsonObject);
		
		return  foramttedObject;
	}
	
	
	
	/** Method to retrieve JSON Object from URL */
    public static JSONObject getJSONfromURL(String url) {

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
		       
		    }
		 
		    //try parse the string to a JSON object
		    try{
		           jArray = new JSONObject(result);
		    }catch(JSONException e){
		       
		    }
		   
		    return jArray;
		}

    
    
    
    /** Method to parse the JSON Object */
    public void parseJSON(JSONObject jsonObject){
    		try {
				JSONArray jsonArray_routes_1 = jsonObject.getJSONArray("results");
				
				 foramttedObject = jsonArray_routes_1.getJSONObject(0).getString("formatted_address");
			   
				
				 
			} catch (JSONException e) {
				
				e.printStackTrace();
				
				
			}
    		
     } // parseJSON Method Close

}
