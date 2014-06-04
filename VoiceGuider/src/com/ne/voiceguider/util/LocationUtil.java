package com.ne.voiceguider.util;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;

public class LocationUtil {

	private Context mContext;
	// 定位相关
	private LocationClient mLocClient;
	private LocationData locData = null;
	public BDLocationListener myListener;
	private boolean isLocating = false;//是否正在定位
	private GeoPoint nowGeoPoint ;
	private float nowZoomLevel = 10;
	private LocationClientOption option = new LocationClientOption();
	//定位图层
	private boolean isFirstLocation = true;//是否首次定位
	public LocationUtil(Context mContext,BDLocationListener mListener)
	{
		this.mContext = mContext;
		//定位初始化
		mLocClient = new LocationClient(mContext);
		locData = new LocationData();
		myListener = mListener;
		mLocClient.registerLocationListener(myListener);
		option.setLocationMode(LocationMode.Hight_Accuracy);//设置定位模式
		option.setOpenGps(true);//打开gps
		option.setCoorType("bd09ll");     //设置坐标类型
		option.setScanSpan(0);
		option.setIsNeedAddress(true);//返回的定位结果包含地址信息
		//option.setNeedDeviceDirect(true);
		mLocClient.setLocOption(option);	
	}

	private MyLocationOverlay myLocationOverlay = null;
	public LocationUtil(Context mContext,BDLocationListener mListener,MapView mMapView)
	{
		this.mContext = mContext;
		initOrientation();
		//定位初始化
		mLocClient = new LocationClient(mContext);
		locData = new LocationData();
		myListener = mListener;
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);//设置定位模式
		option.setOpenGps(true);//打开gps
		option.setCoorType("bd09ll");     //设置坐标类型
		option.setScanSpan(1000);
		option.setNeedDeviceDirect(true);
		mLocClient.setLocOption(option);
		//定位图层初始化
		myLocationOverlay = new MyLocationOverlay(mMapView);
		//设置定位数据
		myLocationOverlay.setData(locData);
		//添加定位图层
		mMapView.getOverlays().add(myLocationOverlay);
		myLocationOverlay.enableCompass();
		//修改定位数据后刷新图层生效
		mMapView.refresh();
	}

	public void requestLocation()
	{
		if(mLocClient.isStarted())
			mLocClient.requestLocation();
		else
			mLocClient.start();
	}
	
	public void start() {
		mLocClient.start();
	}
	public boolean isStarted()
	{
		return mLocClient.isStarted();
	}
	public void stop() {
		mLocClient.stop();
	}
	
	public void updateLocationData(BDLocation mBDLocation)
	{
		locData.latitude = mBDLocation.getLatitude();
		locData.longitude = mBDLocation.getLongitude();
		//如果不显示定位精度圈，将accuracy赋值为0即可
		locData.accuracy = mBDLocation.getRadius();
		// 此处可以设置 locData的方向信息, 如果定位 SDK 未返回方向信息，用户可以自己实现罗盘功能添加方向信息。
		//locData.direction = location.getDirection();
	}
	public void setData()
	{
		myLocationOverlay.setData(locData);
	}
	
	//方向相关
	private SensorManager sensorManager;
	private MySensorEventListener mySensorEventListener;
	/**
	 * 方向传感器
	 */
	void initOrientation()
	{
		sensorManager= (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
		mySensorEventListener = new MySensorEventListener();
		Sensor sensor_orientation=sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
	    sensorManager.registerListener(mySensorEventListener,sensor_orientation, SensorManager.SENSOR_DELAY_UI);
	}

	private final class MySensorEventListener implements  SensorEventListener{

		@Override
		//可以得到传感器实时测量出来的变化值
		public void onSensorChanged(SensorEvent event) {
			//方向传感器
			if(event.sensor.getType()==Sensor.TYPE_ORIENTATION){
				//x表示手机指向的方位，0表示北,90表示东，180表示南，270表示西
				float x = event.values[SensorManager.DATA_X];
				locData.direction = x;//优化百度地图方向不准的问题
//				myLocationOverlay.setData(locData);
				//Log.v(TAG, "方向："+x);
			}
		}
		@Override
		public void onAccuracyChanged(Sensor arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}
	}
	
	


}
