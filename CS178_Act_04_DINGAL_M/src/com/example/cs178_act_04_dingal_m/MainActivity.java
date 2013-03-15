package com.example.cs178_act_04_dingal_m;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.app.Activity;
import android.app.AlertDialog;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends Activity implements OnMapClickListener
{
  static final LatLng uscTc = new LatLng(10.35410, 123.91145);
  static final LatLng uscMain = new LatLng(10.30046, 123.88822);
  static final LatLng home = new LatLng(10.258051, 123.838403);

  private int checker = 0;
  private int maximum = 3;
  private GoogleMap googleMap;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) 
  {
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.activity_main);
    
	    if(GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext()) == ConnectionResult.SUCCESS){
	    	googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();    	    
	    	if(googleMap != null){
	    		Marker usc_tc = googleMap.addMarker(new MarkerOptions().position(uscTc).title("USC-tc"));
	    		Marker usc_main = googleMap.addMarker(new MarkerOptions().position(uscMain).title("USC-main"));
	    		Marker my_place = googleMap.addMarker(new MarkerOptions().position(home).title("My home"));
	    	    	
	    		googleMap.setOnMapClickListener(this);
	    		googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(uscTc, 15), 2000, null);
	    	}
	    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) 
  {
    getMenuInflater().inflate(R.menu.activity_main, menu);
    return true;
  }

public void onMapClick(LatLng point) {
	DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
	    public void onClick(DialogInterface dialog, int check) {
	        switch (check)
	        {
	        	case DialogInterface.BUTTON_POSITIVE:
	            
	        	switch(checker)
	            {
		            case 0:	googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(uscTc, 15), 2000, null); 
		            	break;
		            case 1:	googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(uscMain, 15), 2000, null);
		            	break;
		            case 2:	googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(home, 15), 2000, null); 
	            }
	            
	            checker = (checker + 1) % maximum;
	            break;

	        case DialogInterface.BUTTON_NEGATIVE:
	            //No button clicked
	            break;
	        }
	    }
	};

	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	builder.setMessage("Go to the next destination?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();	
}

} 