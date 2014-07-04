package com.ne.voiceguider.util;

import java.lang.reflect.Method;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.util.Log;

public class SystemUtil {

	private static AudioManager mAudioManager;
	private static LocationManager mLocationManager;
	private static WifiManager mWifiManager;
	private static ConnectivityManager mConnectivityManager;
	private static Context thisContext;

	/*
	 * application �����ʼ��
	 */
	public static void init(Context mContext) {

		thisContext = mContext;
		mLocationManager = (LocationManager) thisContext
				.getSystemService(Context.LOCATION_SERVICE);

		mWifiManager = (WifiManager) thisContext
				.getSystemService(Context.WIFI_SERVICE);

		mConnectivityManager = (ConnectivityManager) thisContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		mAudioManager = (AudioManager) thisContext
				.getSystemService(Context.AUDIO_SERVICE);
	}
	
	/**************************** ���� ******************************************************/
	
	public static void setAudioVolume(int streamType, int index) {
		mAudioManager.setStreamVolume(streamType, (int)(index/100.0*getAudioMaxVolume(streamType)), 0);
		
		// mAudioManager.setStreamVolume(AudioManager.STREAM_RING, progress,
		// 0);//����
		// mAudioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION,
		// progress, 0);//֪ͨ
		// mAudioManager.setStreamVolume(AudioManager.STREAM_ALARM, progress,
		// 0);//����
		// mAudioManager.setStreamVolume(AudioManager.STREAM_DTMF, progress,
		// 0);//˫����Ƶ�绰��MIUI֧�֣�
		// mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress,
		// 0);//����
		// mAudioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, progress,
		// 0);//ϵͳ
		// mAudioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,
		// progress, 0);//ͨ����MIUI��֧�֣�
	}

	public static int getAudioVolume(int streamType) {
		// TODO Auto-generated method stub
		return (int)(mAudioManager.getStreamVolume(streamType)*100.0/getAudioMaxVolume(streamType));
		// mAudioManager.getStreamVolume(AudioManager.STREAM_RING);//����
		// mAudioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION);//֪ͨ
		// mAudioManager.getStreamVolume(AudioManager.STREAM_ALARM);//����
		// mAudioManager.getStreamVolume(AudioManager.STREAM_DTMF);//˫����Ƶ�绰
		// mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);//����
		// mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);//ϵͳ
		// mAudioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);//ͨ��
	}
	
	public static int getAudioMaxVolume(int streamType) {
		// TODO Auto-generated method stub
		return mAudioManager.getStreamMaxVolume(streamType);
		// mAudioManager.getStreamMaxVolume(AudioManager.STREAM_RING);//����
		// mAudioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION);//֪ͨ
		// mAudioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);//����
		// mAudioManager.getStreamMaxVolume(AudioManager.STREAM_DTMF);//˫����Ƶ�绰
		// mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);//����
		// mAudioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);//ϵͳ
		// mAudioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);//ͨ��
	}
	
	public static double getMusicVolume()
	{
		return getAudioVolume(AudioManager.STREAM_MUSIC)*1.0/getAudioMaxVolume(AudioManager.STREAM_MUSIC);
	}
	
	public static void addMusicVolume(int index)
	{
		Log.v("", getAudioMaxVolume(AudioManager.STREAM_MUSIC)+"addMusicVolume");
		setAudioVolume(AudioManager.STREAM_MUSIC,getAudioMaxVolume(AudioManager.STREAM_MUSIC)+index);
//		if(getAudioMaxVolume(AudioManager.STREAM_MUSIC)<(100-index))
//			setAudioVolume(AudioManager.STREAM_MUSIC,getAudioMaxVolume(AudioManager.STREAM_MUSIC)+index);
//		else
//			setAudioVolume(AudioManager.STREAM_MUSIC,100);
	}
	
	public static void subtractMusicVolume(int index)
	{
		Log.v("", getAudioMaxVolume(AudioManager.STREAM_MUSIC)+"subtractMusicVolume");
		setAudioVolume(AudioManager.STREAM_MUSIC,getAudioMaxVolume(AudioManager.STREAM_MUSIC)-index);
//		if(getAudioMaxVolume(AudioManager.STREAM_MUSIC)>index)
//			setAudioVolume(AudioManager.STREAM_MUSIC,getAudioMaxVolume(AudioManager.STREAM_MUSIC)-index);
//		else
//			setAudioVolume(AudioManager.STREAM_MUSIC,0);
	}
	
	/********** TODO**************** GPS ���Ƕ�λ ********************************************************/

	public static void setGPSOpen() {
		// TODO Auto-generated method stub
//		Intent myIntent = new Intent(
//				android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//		startActivity(myIntent);
		// �ֶ����ô�
		toggleGPS();
	}

	public static void setGPSClose() {
		// TODO Auto-generated method stub
//		Intent myIntent = new Intent(
//				android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//		startActivity(myIntent);
		// �ֶ����ùر�
		toggleGPS();
	}

	// �򿪻��߹ر�GPS
	private static void toggleGPS() {
		Intent gpsIntent = new Intent();
		gpsIntent.setClassName("com.android.settings",
				"com.android.settings.widget.SettingsAppWidgetProvider");// ����java���书�ܣ����͹㲥��
		gpsIntent.addCategory("android.intent.category.ALTERNATIVE");
		gpsIntent.setData(Uri.parse("custom:3"));
		// ����ǹؼ���GPS�Ĵ���Ϊ��3��ֻҪ�����ֵ��һ�¾Ϳ���ʵ�������ļ������ܡ�
		// ����WIFI��0������߶ȣ�1��ͬ�����ݣ�2��GPS��3��������4
		try {
			PendingIntent.getBroadcast(thisContext, 0, gpsIntent, 0).send();
		} catch (CanceledException e) {
			e.printStackTrace();
		}
		
//		Intent myIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//		startActivity(myIntent);
		//�ֶ����ô�
	}
	public static void setGPS(int isOpen) {
		if (isOpen == 1)
			setGPSOpen();
		else
			setGPSClose();
	}

	public static int getGPSStatus() {
		return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
				&& mLocationManager
						.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ? 1
				: 0;
	}

	/**************************** Wifi ���߾����� ******************************************************/

	public static void setWifiOpen() {
		// ���wifi�Ѿ��ر��Ҳ��ڴ�״̬�оʹ�wifi
				if (!mWifiManager.isWifiEnabled()
						&& mWifiManager.getWifiState() != WifiManager.WIFI_STATE_ENABLING) {
					mWifiManager.setWifiEnabled(true);
				}
		
	}

	public static void setWifiClose() {
		// ���wifi�Ѿ����� �͹ر�wifi
				if (mWifiManager.isWifiEnabled()) {
					mWifiManager.setWifiEnabled(false);
				}
	}

	public static void setWifi(int isOpen) {
		if (isOpen == 1)
			setWifiOpen();
		else
			setWifiClose();
	}

	public static int getWifiStatus() {
		return mWifiManager.isWifiEnabled() ? 1 : 0;
	}
	
//	public static int getWifiStatus(Context context) {
//		mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//		return mWifiManager.isWifiEnabled() ? 1 : 0;
//	}

	/****************************** GPRS �ƶ����� ****************************************************/

	public static void setGPRSOpen() {
		Class cmClass = mConnectivityManager.getClass();
		Class[] argClasses = new Class[1];
		argClasses[0] = boolean.class;

		try {
			Method method = cmClass.getMethod("setMobileDataEnabled",
					argClasses);
			method.invoke(mConnectivityManager, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void setGPRSClose() {
		Class cmClass = mConnectivityManager.getClass();
		Class[] argClasses = new Class[1];
		argClasses[0] = boolean.class;

		try {
			Method method = cmClass.getMethod("setMobileDataEnabled",
					argClasses);
			method.invoke(mConnectivityManager, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void setGPRS(int isOpen) {
		if (isOpen == 1)
			setGPRSOpen();
		else
			setGPRSClose();
	}

	public static int geGPRSStatus() {
		Class cmClass = mConnectivityManager.getClass();
		Class[] argClasses = null;
		Object[] argObject = null;

		Boolean isOpen = false;
		try {
			Method method = cmClass.getMethod("getMobileDataEnabled",
					argClasses);
			isOpen = (Boolean) method.invoke(mConnectivityManager, argObject);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return isOpen ? 1 : 0;
	}
	
}
