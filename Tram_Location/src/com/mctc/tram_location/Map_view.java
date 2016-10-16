package com.mctc.tram_location;

import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MapView.LayoutParams;
import com.google.android.maps.Overlay;
 
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
 
public class Map_view extends MapActivity {  
	
    MapView mapView; 
    MapController mc;
    GeoPoint p;
    Button back;
    String endlatlon="";
    String startpoint="";
   
 
    /** Called when the activity is first created. */
  
    @SuppressWarnings("deprecation")
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.latlon);
 
        mapView = (MapView) findViewById(R.id.mapView);
        
        Bundle bundle = getIntent().getExtras();
        startpoint = bundle.getString("startpoint");
        
        
   	 /* request button on click  */

        
        
        
        
        
        LinearLayout zoomLayout = (LinearLayout)findViewById(R.id.zoom);  
        View zoomView = mapView.getZoomControls(); 
 
        zoomLayout.addView(zoomView, 
            new LinearLayout.LayoutParams (
                LayoutParams.WRAP_CONTENT, 
                LayoutParams.WRAP_CONTENT)); 
        mapView.displayZoomControls(true);
 
        mc = mapView.getController();
        
        String coordinates[] = startpoint.split(",");
        
        double lat = Double.parseDouble(coordinates[0]);
        double lng = Double.parseDouble(coordinates[1]);
 
        p = new GeoPoint(
            (int) (lat * 1E6), 
            (int) (lng * 1E6));
 
        mc.animateTo(p);
        mc.setZoom(17); 
        mapView.invalidate();
        
      //---Add a location marker---
        MapOverlay mapOverlay = new MapOverlay();
        List<Overlay> listOfOverlays = mapView.getOverlays();
        listOfOverlays.clear();
        listOfOverlays.add(mapOverlay);      
 
        mapView.invalidate();
        
      
    }

    @Override
    protected boolean isRouteDisplayed() {
       
        return false;
    }
    
    
    class MapOverlay extends Overlay {
    	
    	 @Override
         public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) {
             super.draw(canvas, mapView, shadow);                   
  
             //---translate the GeoPoint to screen pixels---
             Point screenPts = new Point();
             mapView.getProjection().toPixels(p, screenPts);
  
             //---add the marker---
             Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.bus);            
             canvas.drawBitmap(bmp, screenPts.x, screenPts.y-50, null);         
             return true;
         }
    	
    	
    	/*@Override
    	public boolean onTouchEvent(MotionEvent event, MapView mapView) {
    		//---when user lifts his finger---
    		if (event.getAction() == 1) {              
    			GeoPoint p = mapView.getProjection().fromPixels(
    					(int) event.getX(),
    					(int) event.getY());
    			Toast.makeText(getBaseContext(), 
    						p.getLatitudeE6() / 1E6 + "," + 
                    		p.getLongitudeE6() /1E6 , 
                    		Toast.LENGTH_SHORT).show();
    			
    			endlatlon = p.getLatitudeE6() / 1E6 + "," + p.getLongitudeE6() /1E6 ;
    			
    		} 
    		return super.onTouchEvent(event, mapView);
	}*/
    	
    }
	
  
}

