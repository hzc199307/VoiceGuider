package com.ne.voiceguider.activity;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.LocationSource.OnLocationChangedListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ne.voiceguider.R;
import com.ne.voiceguider.dao.CitySceneDao;
import com.ne.voiceguider.util.GoogleLocationUtil;
import com.ne.voiceguider.util.GoogleMarkerUtil;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Toast;

/**
 * 
 * @ClassName: GoogleMapActivity 
 * @Description: TODO 
 * @author HeZhichao
 * @date 2014年6月23日 下午4:38:12 
 *
 */
public class GoogleMapActivity extends ActionBarActivity {

	private GoogleMap googleMap;
	private View googleMapView;
	private GoogleMarkerUtil mGoogleMarkerUtil;
	private GoogleLocationUtil mGoogleLocationUtil;
	private LocationSourceListener mLocationSourceListener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_google_map);
		googleMapView = getSupportFragmentManager().findFragmentById(R.id.googleMap).getView();
		
		mLocationSourceListener = new LocationSourceListener();
		mGoogleLocationUtil = new GoogleLocationUtil(this,mLocationSourceListener);
		setUpMapIfNeeded();

		mGoogleMarkerUtil = new GoogleMarkerUtil(this, googleMap);
		CitySceneDao mCitySceneDao = new CitySceneDao(this);
		mGoogleMarkerUtil.setListObject(mCitySceneDao.getBigScenes(1));
	}

	@Override
	protected void onResume() {
		super.onResume();

		setUpMapIfNeeded();
		mLocationSourceListener.onResume();
		mGoogleLocationUtil.connect();
	}
	@Override
    protected void onPause() {
        super.onPause();
        mLocationSourceListener.onPause();
        mGoogleLocationUtil.disconnect();
    }

	private void setUpMapIfNeeded() {
		if (googleMap == null) {
			googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap)).getMap();
			if (googleMap != null) {
				setUpMap();
//				googleMap.getMyLocation();
				
				//				googleMap.setOnMyLocationButtonClickListener(new OnMyLocationButtonClickListener() {
				//					
				//					@Override
				//					public boolean onMyLocationButtonClick() {
				//						Toast.makeText(getApplicationContext(), "MyLocation button clicked", Toast.LENGTH_SHORT).show();
				//				        // Return false so that we don't consume the event and the default behavior still occurs
				//				        // (the camera animates to the user's current position).
				//				        
				//						return false;
				//					}
				//				});
			}
		}
	}

	private void setUpMap() {
		//googleMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
		moveToCenter();
		//googleMap.getUiSettings().setMyLocationButtonEnabled(false);
		googleMap.setLocationSource(mLocationSourceListener);
		googleMap.setMyLocationEnabled(true);
		Toast.makeText(getApplicationContext(), "setUpMap", Toast.LENGTH_SHORT).show();
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

	private static class LocationSourceListener implements LocationSource,LocationListener{

		private OnLocationChangedListener mListener;
		private boolean mPaused = false;

		@Override
		public void activate(OnLocationChangedListener listener) {
			//Toast.makeText(getApplicationContext(), "activate", Toast.LENGTH_SHORT).show();
			mListener = listener;
			Log.v("activate", "执行了");
		}

		@Override
		public void deactivate() {
			mListener = null;
		}

		public void onPause() {
			mPaused = true;
		}

		public void onResume() {
			mPaused = false;
		}

		@Override
		public void onLocationChanged(Location arg0) {
			// TODO 这里可以改定位坐标  google定位有偏移
			//Toast.makeText(getApplicationContext(), "onLocationChanged", Toast.LENGTH_SHORT).show();
			arg0.setAccuracy(10000);
			if (mListener != null) {
				mListener.onLocationChanged(arg0);
				Log.v("onLocationChanged", "执行了");
				//Toast.makeText(getApplicationContext(), "onLocationChanged mListener", Toast.LENGTH_SHORT).show();
			}
		}

	}

}
