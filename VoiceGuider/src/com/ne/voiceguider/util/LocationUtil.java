package com.ne.voiceguider.util;

import java.util.List;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

public class LocationUtil {

	private LocationManager mLocationManager ;
	private Location mlocation;  
	private String bestProvider;  

	private Context mContext;
	public LocationUtil(Context mContext) {
		this.mContext = mContext;
		// TODO Auto-generated constructor stub
		//创建LocationManager对象,并获取Provider  
		mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE); 
		initProvider();  
		// 设置监听器，自动更新的最小时间为间隔N秒(1秒为1*1000，这样写主要为了方便)或最小位移变化超过N  
		mLocationManager.requestLocationUpdates(bestProvider ,  3000, 8, new MyLocationListener());  
	}  
	private void initProvider() {  
		//创建LocationManager对象  

		// List all providers:  
		List<String> providers = mLocationManager.getAllProviders();  
		Criteria criteria = new Criteria();  
		criteria.setAccuracy(Criteria.ACCURACY_FINE); //精度高  
		criteria.setPowerRequirement(Criteria.POWER_MEDIUM); //电量消耗低  
		criteria.setAltitudeRequired(false); //不需要海拔  
		criteria.setSpeedRequired(false); //不需要速度  
		criteria.setCostAllowed(false); //不需要费用  
		bestProvider = mLocationManager.getBestProvider(criteria, true); 
		Toast.makeText(mContext,bestProvider, Toast.LENGTH_SHORT).show();
		mlocation = mLocationManager.getLastKnownLocation(bestProvider);  
		toast();
	} 

	private void toast()
	{
		if(mlocation!=null)
			Toast.makeText(mContext, "经度："+mlocation.getLatitude()+"  纬度：" + mlocation.getLongitude(), Toast.LENGTH_LONG).show();
		else
			Toast.makeText(mContext, "无定位信息", Toast.LENGTH_SHORT).show();
	}

	class MyLocationListener implements LocationListener
	{
		//当Provider的状态改变时  
		@Override  
		public void onStatusChanged(String provider, int status, Bundle extras) {  
			// Provider的转态在可用、暂时不可用和无服务三个状态直接切换时触发此函数  
		}  
		@Override  
		public void onProviderEnabled(String provider) {  
			//  Provider被enable时触发此函数，比如GPS被打开  
			mlocation = mLocationManager.getLastKnownLocation(provider);
			toast();
		}  
		@Override  
		public void onProviderDisabled(String provider) {  
			// Provider被disable时触发此函数，比如GPS被关闭  
		}  
		@Override  
		public void onLocationChanged(Location location) {  
			//当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发  
			//Toast.makeText(mContext,"onLocationChanged", Toast.LENGTH_SHORT).show();
			mlocation = location;
			toast();
		}

	}
}
