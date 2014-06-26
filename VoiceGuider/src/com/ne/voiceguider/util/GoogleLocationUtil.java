package com.ne.voiceguider.util;

import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

/**
 * google地图 定位工具
 * @ClassName: GoogleLocationUtil 
 * @Description: TODO 
 * @author HeZhichao
 * @date 2014年6月26日 下午3:11:08 
 *
 */
public class GoogleLocationUtil {

	private LocationClient mLocationClient;
	private Context mContext;
	private LocationListener locationListener;
	
	// These settings are the same as the settings for the map. They will in fact give you updates
    // at the maximal rates currently possible.
    private LocationRequest REQUEST = LocationRequest.create()
            .setInterval(5000)         // 5 seconds
            .setFastestInterval(16)    // 16ms = 60fps
            .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    
	public GoogleLocationUtil(Context mContext,LocationListener locationListener) {
		this.mContext = mContext;
		this.locationListener = locationListener;
	}
	
	public void setLocationRequest(LocationRequest requst)
	{
		REQUEST = requst;
	}
	
	public LocationRequest getLocationRequest()
	{
		return REQUEST;
	}
	
	public void connect() {
    	if (mLocationClient == null) {
            mLocationClient = new LocationClient(
                    mContext,
                    new MyConnectionCallbacks(),  // ConnectionCallbacks
                    new MyOnConnectionFailedListener()); // OnConnectionFailedListener
            
        }
        if(mLocationClient.isConnected()==false)
        	mLocationClient.connect();
    }

    public void disconnect() {
        if (mLocationClient != null&&mLocationClient.isConnected()) {
            mLocationClient.disconnect();
            mLocationClient=null;
        }
    }
    
    class MyConnectionCallbacks implements ConnectionCallbacks
    {

		@Override
		public void onConnected(Bundle arg0) {
			mLocationClient.requestLocationUpdates(
	                REQUEST,
	                locationListener);  // LocationListener
		}

		@Override
		public void onDisconnected() {
			// 在执行 LocationClient.disconnect(); 之前可能因为故障而失去连接
			mLocationClient = null;
		}
    	
    }
    
    class MyOnConnectionFailedListener implements OnConnectionFailedListener
    {

		@Override
		public void onConnectionFailed(ConnectionResult arg0) {
			// TODO Auto-generated method stub
			
		}
		
    }
    /**
     * 在别处实现 定位变化 回调函数
     */
//    class MyLocationListener implements LocationListener
//    {
//
//		@Override
//		public void onLocationChanged(Location arg0) {
//			// TODO Auto-generated method stub
//			
//		}
//    	
//    }
}
