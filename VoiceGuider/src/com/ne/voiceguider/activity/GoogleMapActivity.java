package com.ne.voiceguider.activity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ne.voiceguider.R;
import com.ne.voiceguider.dao.CitySceneDao;
import com.ne.voiceguider.util.GoogleMarkerUtil;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

/**
 * 
 * @ClassName: GoogleMapActivity 
 * @Description: TODO 
 * @author HeZhichao
 * @date 2014年6月23日 下午4:38:12 
 *
 */
public class GoogleMapActivity extends ActionBarActivity {
	private static final LatLng BRISBANE = new LatLng(-27.47093, 153.0235);
	private static final LatLng MELBOURNE = new LatLng(-37.81319, 144.96298);
	private static final LatLng SYDNEY = new LatLng(-33.87365, 151.20689);
	private static final LatLng ADELAIDE = new LatLng(-34.92873, 138.59995);
	private static final LatLng PERTH = new LatLng(-31.952854, 115.857342);

	private GoogleMap googleMap;
	private View googleMapView;
	private GoogleMarkerUtil mGoogleMarkerUtil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_google_map);
		  googleMapView = getSupportFragmentManager().findFragmentById(R.id.googleMap).getView();
		setUpMapIfNeeded();
		
		mGoogleMarkerUtil = new GoogleMarkerUtil(this, googleMap);
		CitySceneDao mCitySceneDao = new CitySceneDao(this);
		mGoogleMarkerUtil.setListObject(mCitySceneDao.getBigScenes(1));

		
	}

	@Override
	protected void onResume() {
		super.onResume();

		setUpMapIfNeeded();
	}

	private void setUpMapIfNeeded() {
		if (googleMap == null) {
			googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap)).getMap();
			if (googleMap != null) {
				setUpMap();
			}
		}
	}

	private void setUpMap() {
		googleMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
		moveToCenter();
//		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-33.87365, 151.20689), 10));
	}
	
	private void moveToCenter()
	{
		if (googleMapView.getViewTreeObserver().isAlive()) {
			googleMapView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
				@SuppressWarnings("deprecation") // We use the new method when supported
				@SuppressLint("NewApi") // We check which build version we are using.
				@Override
				public void onGlobalLayout() {
//					LatLngBounds bounds = new LatLngBounds.Builder()
//					.include(PERTH)
//					.include(SYDNEY)
//					.include(ADELAIDE)
//					.include(BRISBANE)
//					.include(MELBOURNE)
//					.build();
					if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
						googleMapView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
					} else {
						googleMapView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
					}
					mGoogleMarkerUtil.showAll();
					mGoogleMarkerUtil.showSpan();
					//googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
				}
			});
		}
	}

}
