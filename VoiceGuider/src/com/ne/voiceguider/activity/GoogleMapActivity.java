package com.ne.voiceguider.activity;

import com.google.android.gms.maps.MapView;
import com.ne.voiceguider.R;
import com.ne.voiceguider.R.id;
import com.ne.voiceguider.R.layout;
import com.ne.voiceguider.R.menu;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class GoogleMapActivity extends ActionBarActivity {

	private MapView mapViewGoogle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_google_map);
		mapViewGoogle = (MapView) findViewById(R.id.mapViewGoogle);
//	    GeoPoint point = new GeoPoint((int)(23134837),(int)(113271842));
//	    mapView.getController().setCenter(point);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.google_map, menu);
		return true;
	}

	@Override 
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
