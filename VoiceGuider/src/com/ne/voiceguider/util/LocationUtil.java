package com.ne.voiceguider.util;

import java.util.List;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

/**
 * android �Դ��Ķ�λ������
 * @ClassName: LocationUtil 
 * @Description: TODO 
 * @author HeZhichao
 * @date 2014��6��27�� ����6:02:02 
 *
 */
public class LocationUtil {

	private LocationManager mLocationManager ;
	private Location mlocation;  
	private String bestProvider;  

	private Context mContext;
	public LocationUtil(Context mContext) {
		this.mContext = mContext;
		// TODO Auto-generated constructor stub
		//����LocationManager����,����ȡProvider  
		mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE); 
		initProvider();  
		// ���ü��������Զ����µ���Сʱ��Ϊ���N��(1��Ϊ1*1000������д��ҪΪ�˷���)����Сλ�Ʊ仯����N  
		mLocationManager.requestLocationUpdates(bestProvider ,  3000, 8, new MyLocationListener());  
	}  
	private void initProvider() {  
		//����LocationManager����  

		// List all providers:  
		List<String> providers = mLocationManager.getAllProviders();  
		Criteria criteria = new Criteria();  
		criteria.setAccuracy(Criteria.ACCURACY_FINE); //���ȸ�  
		criteria.setPowerRequirement(Criteria.POWER_MEDIUM); //�������ĵ�  
		criteria.setAltitudeRequired(false); //����Ҫ����  
		criteria.setSpeedRequired(false); //����Ҫ�ٶ�  
		criteria.setCostAllowed(false); //����Ҫ����  
		bestProvider = mLocationManager.getBestProvider(criteria, true); 
		Toast.makeText(mContext,bestProvider, Toast.LENGTH_SHORT).show();
		mlocation = mLocationManager.getLastKnownLocation(bestProvider);  
		toast();
	} 

	private void toast()
	{
		if(mlocation!=null)
			Toast.makeText(mContext, "���ȣ�"+mlocation.getLatitude()+"  γ�ȣ�" + mlocation.getLongitude(), Toast.LENGTH_LONG).show();
		else
			Toast.makeText(mContext, "�޶�λ��Ϣ", Toast.LENGTH_SHORT).show();
	}

	class MyLocationListener implements LocationListener
	{
		//��Provider��״̬�ı�ʱ  
		@Override  
		public void onStatusChanged(String provider, int status, Bundle extras) {  
			// Provider��ת̬�ڿ��á���ʱ�����ú��޷�������״ֱ̬���л�ʱ�����˺���  
		}  
		@Override  
		public void onProviderEnabled(String provider) {  
			//  Provider��enableʱ�����˺���������GPS����  
			mlocation = mLocationManager.getLastKnownLocation(provider);
			toast();
		}  
		@Override  
		public void onProviderDisabled(String provider) {  
			// Provider��disableʱ�����˺���������GPS���ر�  
		}  
		@Override  
		public void onLocationChanged(Location location) {  
			//������ı�ʱ�����˺��������Provider������ͬ�����꣬���Ͳ��ᱻ����  
			//Toast.makeText(mContext,"onLocationChanged", Toast.LENGTH_SHORT).show();
			mlocation = location;
			toast();
		}

	}
}
