package com.ne.voiceguider;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
import com.ne.voiceguider.service.VoicePlayerService;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * 
 * @ClassName: VoiceGuiderApplication 
 * @Description: 
 * @author HeZhichao
 * @date 2014��5��22�� ����2:41:15 
 *
 */
public class VoiceGuiderApplication extends Application {

	private String TAG = "VoiceGuiderApplication";
	private static VoiceGuiderApplication mInstance = null;
	public boolean m_bKeyRight = true;
	public BMapManager mBMapManager = null;
	private Intent serviceIntent;
	@Override
	public void onCreate() {
		Log.v(TAG, "onCreate");
		
		mInstance = this;
		initEngineManager(this);
		Log.v(TAG, "onCreate1");
//		initVoicePlayerService();//service�󶨵�MainActivityȥ  �˴��ᱨ�� TODO
		Log.v(TAG, "onCreate2");
		super.onCreate();
	}

	public VoicePlayerService.MediaBinder mMediaBinder;
	public ServiceConnection mConnection = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.d("TAG", "<-----onServiceDisconnected---->");
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.d("TAG", "onServiceConnected---->");
			mMediaBinder = (VoicePlayerService.MediaBinder) service;
		}
	};
//	public void initVoicePlayerService()
//	{
//		serviceIntent = new Intent(getApplicationContext(), VoicePlayerService.class);
//		bindService(serviceIntent, mConnection, BIND_AUTO_CREATE);
//	}
	public void initEngineManager(Context context) {
		Log.v(TAG, "initEngineManager");
		if (mBMapManager == null) {
			mBMapManager = new BMapManager(context);
		}

		if (!mBMapManager.init(new MyGeneralListener())) {
			Toast.makeText(VoiceGuiderApplication.getInstance().getApplicationContext(), 
					"BMapManager  ��ʼ������!", Toast.LENGTH_LONG).show();
		}
	}

	public static VoiceGuiderApplication getInstance() {
		return mInstance;
	}
	// �����¼���������������ͨ�������������Ȩ��֤�����
	public static class MyGeneralListener implements MKGeneralListener {

		@Override
		public void onGetNetworkState(int iError) {
			if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
				Toast.makeText(VoiceGuiderApplication.getInstance().getApplicationContext(), "���������������",
						Toast.LENGTH_LONG).show();
			}
			else if (iError == MKEvent.ERROR_NETWORK_DATA) {
				Toast.makeText(VoiceGuiderApplication.getInstance().getApplicationContext(), "������ȷ�ļ���������",
						Toast.LENGTH_LONG).show();
			}
			// ...
		}

		@Override
		public void onGetPermissionState(int iError) {
			//����ֵ��ʾkey��֤δͨ��
			if (iError != 0) {
				//��ȨKey����
				Toast.makeText(VoiceGuiderApplication.getInstance().getApplicationContext(), 
						"���� DemoApplication.java�ļ�������ȷ����ȨKey,������������������Ƿ�������error: "+iError, Toast.LENGTH_LONG).show();
				VoiceGuiderApplication.getInstance().m_bKeyRight = false;
			}
			else{
				VoiceGuiderApplication.getInstance().m_bKeyRight = true;
				Toast.makeText(VoiceGuiderApplication.getInstance().getApplicationContext(), 
						"key��֤�ɹ�", Toast.LENGTH_LONG).show();
			}
		}
	}
}