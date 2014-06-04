package com.ne.voiceguider.util;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.Button;

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
	// ��λ���
	private LocationClient mLocClient;
	private LocationData locData = null;
	public BDLocationListener myListener;
	private boolean isLocating = false;//�Ƿ����ڶ�λ
	private GeoPoint nowGeoPoint ;
	private float nowZoomLevel = 10;
	private LocationClientOption option = new LocationClientOption();
	//��λͼ��
	private boolean isFirstLocation = true;//�Ƿ��״ζ�λ
	public LocationUtil(Context mContext,BDLocationListener mListener)
	{
		this.mContext = mContext;
		//��λ��ʼ��
		mLocClient = new LocationClient(mContext);
		locData = new LocationData();
		myListener = mListener;
		mLocClient.registerLocationListener(myListener);
		option.setLocationMode(LocationMode.Hight_Accuracy);//���ö�λģʽ
		option.setOpenGps(true);//��gps
		option.setCoorType("bd09ll");     //������������
		option.setScanSpan(0);
		option.setIsNeedAddress(true);//���صĶ�λ���������ַ��Ϣ
		//option.setNeedDeviceDirect(true);
		mLocClient.setLocOption(option);	
	}

	private MyLocationOverlay myLocationOverlay = null;
	public LocationUtil(Context mContext,BDLocationListener mListener,MapView mMapView)
	{
		this.mContext = mContext;
		initOrientation();
		//��λ��ʼ��
		mLocClient = new LocationClient(mContext);
		locData = new LocationData();
		myListener = mListener;
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);//���ö�λģʽ
		option.setOpenGps(true);//��gps
		option.setCoorType("bd09ll");     //������������
		option.setScanSpan(1000);
		option.setNeedDeviceDirect(true);
		mLocClient.setLocOption(option);
		//��λͼ���ʼ��
		myLocationOverlay = new MyLocationOverlay(mMapView);
		//���ö�λ����
		myLocationOverlay.setData(locData);
		//��Ӷ�λͼ��
		mMapView.getOverlays().add(myLocationOverlay);
		myLocationOverlay.enableCompass();
		//�޸Ķ�λ���ݺ�ˢ��ͼ����Ч
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
		//�������ʾ��λ����Ȧ����accuracy��ֵΪ0����
		locData.accuracy = mBDLocation.getRadius();
		// �˴��������� locData�ķ�����Ϣ, �����λ SDK δ���ط�����Ϣ���û������Լ�ʵ�����̹�����ӷ�����Ϣ��
		//locData.direction = location.getDirection();
	}
	public void setData()
	{
		myLocationOverlay.setData(locData);
	}
	
	//�������
	private SensorManager sensorManager;
	private MySensorEventListener mySensorEventListener;
	/**
	 * ���򴫸���
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
		//���Եõ�������ʵʱ���������ı仯ֵ
		public void onSensorChanged(SensorEvent event) {
			//���򴫸���
			if(event.sensor.getType()==Sensor.TYPE_ORIENTATION){
				//x��ʾ�ֻ�ָ��ķ�λ��0��ʾ��,90��ʾ����180��ʾ�ϣ�270��ʾ��
				float x = event.values[SensorManager.DATA_X];
				locData.direction = x;//�Ż��ٶȵ�ͼ����׼������
//				myLocationOverlay.setData(locData);
				//Log.v(TAG, "����"+x);
			}
		}
		@Override
		public void onAccuracyChanged(Sensor arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}
	}
	
	


}
