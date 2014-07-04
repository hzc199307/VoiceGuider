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
	 * application 那里初始化
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
	
	/**************************** 音量 ******************************************************/
	
	public static void setAudioVolume(int streamType, int index) {
		mAudioManager.setStreamVolume(streamType, (int)(index/100.0*getAudioMaxVolume(streamType)), 0);
		
		// mAudioManager.setStreamVolume(AudioManager.STREAM_RING, progress,
		// 0);//铃声
		// mAudioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION,
		// progress, 0);//通知
		// mAudioManager.setStreamVolume(AudioManager.STREAM_ALARM, progress,
		// 0);//闹铃
		// mAudioManager.setStreamVolume(AudioManager.STREAM_DTMF, progress,
		// 0);//双音多频电话（MIUI支持）
		// mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress,
		// 0);//音乐
		// mAudioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, progress,
		// 0);//系统
		// mAudioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,
		// progress, 0);//通话（MIUI不支持）
	}

	public static int getAudioVolume(int streamType) {
		// TODO Auto-generated method stub
		return (int)(mAudioManager.getStreamVolume(streamType)*100.0/getAudioMaxVolume(streamType));
		// mAudioManager.getStreamVolume(AudioManager.STREAM_RING);//铃声
		// mAudioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION);//通知
		// mAudioManager.getStreamVolume(AudioManager.STREAM_ALARM);//闹铃
		// mAudioManager.getStreamVolume(AudioManager.STREAM_DTMF);//双音多频电话
		// mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);//音乐
		// mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);//系统
		// mAudioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);//通话
	}
	
	public static int getAudioMaxVolume(int streamType) {
		// TODO Auto-generated method stub
		return mAudioManager.getStreamMaxVolume(streamType);
		// mAudioManager.getStreamMaxVolume(AudioManager.STREAM_RING);//铃声
		// mAudioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION);//通知
		// mAudioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);//闹铃
		// mAudioManager.getStreamMaxVolume(AudioManager.STREAM_DTMF);//双音多频电话
		// mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);//音乐
		// mAudioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);//系统
		// mAudioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);//通话
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
	
	/********** TODO**************** GPS 卫星定位 ********************************************************/

	public static void setGPSOpen() {
		// TODO Auto-generated method stub
//		Intent myIntent = new Intent(
//				android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//		startActivity(myIntent);
		// 手动设置打开
		toggleGPS();
	}

	public static void setGPSClose() {
		// TODO Auto-generated method stub
//		Intent myIntent = new Intent(
//				android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//		startActivity(myIntent);
		// 手动设置关闭
		toggleGPS();
	}

	// 打开或者关闭GPS
	private static void toggleGPS() {
		Intent gpsIntent = new Intent();
		gpsIntent.setClassName("com.android.settings",
				"com.android.settings.widget.SettingsAppWidgetProvider");// 利用java反射功能，发送广播：
		gpsIntent.addCategory("android.intent.category.ALTERNATIVE");
		gpsIntent.setData(Uri.parse("custom:3"));
		// 这句是关键，GPS的代码为：3，只要把这个值改一下就可以实现其它的几个功能。
		// 其中WIFI：0；背光高度：1；同步数据：2；GPS：3；蓝牙：4
		try {
			PendingIntent.getBroadcast(thisContext, 0, gpsIntent, 0).send();
		} catch (CanceledException e) {
			e.printStackTrace();
		}
		
//		Intent myIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//		startActivity(myIntent);
		//手动设置打开
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

	/**************************** Wifi 无线局域网 ******************************************************/

	public static void setWifiOpen() {
		// 如果wifi已经关闭且不在打开状态中就打开wifi
				if (!mWifiManager.isWifiEnabled()
						&& mWifiManager.getWifiState() != WifiManager.WIFI_STATE_ENABLING) {
					mWifiManager.setWifiEnabled(true);
				}
		
	}

	public static void setWifiClose() {
		// 如果wifi已经打开了 就关闭wifi
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

	/****************************** GPRS 移动数据 ****************************************************/

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
