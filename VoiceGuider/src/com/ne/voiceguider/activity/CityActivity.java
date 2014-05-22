package com.ne.voiceguider.activity;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.MKOLUpdateElement;
import com.baidu.mapapi.map.MKOfflineMap;
import com.baidu.mapapi.map.MKOfflineMapListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.ne.voiceguider.R;
import com.ne.voiceguider.R.id;
import com.ne.voiceguider.R.layout;
import com.ne.voiceguider.R.menu;
import com.ne.voiceguider.VoiceGuiderApplication;
import com.ne.voiceguider.activity.HikingActivity.HikingCityOnClickListener;
import com.ne.voiceguider.util.OfflineMapUtil;
import com.ne.voiceguider.view.RoundProgressBar;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.os.Build;

/**
 * 
 * @ClassName: CityActivity 
 * @Description: TODO 
 * @author HeZhichao
 * @date 2014��5��20�� ����3:01:52 
 *
 */
public class CityActivity extends ActionBarActivity {

	int a;
	private String TAG = "CityActivity";
	private MapView mMapView = null;
	private OfflineMapUtil mOfflineMapUtil = null;
	private String cityName = null;
	private int cityId = -1;
	private MKOLUpdateElement cityUpdateInfo = null;
	private Button city_scenelist_button,city_scenemap_button,city_head_back;
	private FrameLayout city_scene_mapdownload_button;
	private RoundProgressBar city_scene_alldownload_button_roundProgressBar;
	private ImageView city_scene_alldownload_button_imageView;
	private Boolean isMapDownload;
	private RelativeLayout city_scene_top_layout,city_scene_mapdownload_layout,city_scene_download_listview;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.v("CityActivity", "onCreate");
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		initMap();

		list_maplayoutSwitch();

		mapDownload();

		city_head();


	}


	public void initMap()
	{
		VoiceGuiderApplication app = (VoiceGuiderApplication)this.getApplication();
		if (app.mBMapManager == null) {
			app.mBMapManager = new BMapManager(getApplicationContext());
			/**
			 * ���BMapManagerû�г�ʼ�����ʼ��BMapManager
			 */
			app.mBMapManager.init(new VoiceGuiderApplication.MyGeneralListener());
		}
		//ע�⣺��������setContentViewǰ��ʼ��BMapManager���󣬷���ᱨ��
		setContentView(R.layout.activity_city);
		mMapView=(MapView)findViewById(R.id.city_scenemap);
		//mMapView.setBuiltInZoomControls(true);//�����������õ����ſؼ�
		MapController mMapController=mMapView.getController();
		// �õ�mMapView�Ŀ���Ȩ,�����������ƺ�����ƽ�ƺ�����
		GeoPoint point = null;
		Intent  intent = getIntent();
		mOfflineMapUtil = new OfflineMapUtil(this,mMapView,new MyMKOfflineMapListener());
		if ( intent.hasExtra("cityName")  ){
			//����intent����ʱ���������ĵ�Ϊָ����
			Bundle b = intent.getExtras();
			cityName = b.getString("cityName");
			cityId = mOfflineMapUtil.search(cityName);
			cityUpdateInfo = mOfflineMapUtil.getUpdateInfo(cityId);
			Log.e(TAG,cityName+" "+cityId+" "+(cityUpdateInfo==null));
			Log.v("intent",cityName);
			point = mOfflineMapUtil.searchGeoPoint(cityName);
			Log.v(TAG,"latE6:"+point.getLatitudeE6()+" ,lonE6"+point.getLongitudeE6());
		}else{
			//�������ĵ�Ϊ�찲��
			point = new GeoPoint((int)(39.915* 1E6),(int)(116.404* 1E6));
		}
		//�ø����ľ�γ�ȹ���һ��GeoPoint����λ��΢�� (�� * 1E6)
		mMapController.setCenter(point);//���õ�ͼ���ĵ�
		mMapController.setZoom(12);//���õ�ͼzoom����
		mMapController.setCompassMargin(90, 90);//����ָ����λ��
	}

	/**
	 * �й��ڽ����л���ʾ���߼�����
	 * @Title: list_maplayoutSwitch 
	 * @Description: TODO
	 * @author HeZhichao
	 * @date 2014��5��20��
	 * @param 
	 * @return void 
	 * @throws
	 */
	public void list_maplayoutSwitch()
	{

		city_scene_top_layout = (RelativeLayout)findViewById(R.id.city_scene_top_layout);
		city_scene_mapdownload_layout = (RelativeLayout)findViewById(R.id.city_scene_mapdownload_layout);
		city_scene_download_listview = (RelativeLayout)findViewById(R.id.city_scene_download_listview);
		city_scenelist_button = (Button)findViewById(R.id.city_scenelist_button);
		city_scenelist_button.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Thread thread = new Thread(){
					@Override
					public void run(){
						try {
							Thread.currentThread().sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						mMapView.setVisibility(View.INVISIBLE);
					}
				};
				city_scene_top_layout.getBackground().setAlpha(255);//��ȫ��͸��
				city_scene_mapdownload_layout.setVisibility(View.VISIBLE);
				city_scene_download_listview.setVisibility(View.VISIBLE);

			}
		});
		city_scenemap_button = (Button)findViewById(R.id.city_scenemap_button);
		city_scenemap_button.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				city_scene_top_layout.getBackground().setAlpha(0);//��ȫ͸����
				city_scene_mapdownload_layout.setVisibility(View.INVISIBLE);
				city_scene_download_listview.setVisibility(View.INVISIBLE);
				mMapView.setVisibility(View.VISIBLE);
				city_scenelist_button.setVisibility(View.VISIBLE);
				city_scenemap_button.setVisibility(View.VISIBLE);
			}
		});

	}

	/**
	 * ��ͼ���ص�����
	 * @Title: mapDownload 
	 * @Description: TODO
	 * @author HeZhichao
	 * @date 2014��5��21�� ����4:31:43 
	 * @param 
	 * @return void 
	 * @throws
	 */
	public void mapDownload()
	{
		isMapDownload = false;
		city_scene_alldownload_button_roundProgressBar = (RoundProgressBar)findViewById(R.id.city_scene_alldownload_button_roundProgressBar);
		city_scene_alldownload_button_imageView = (ImageView)findViewById(R.id.city_scene_alldownload_button_imageView);
		if(cityUpdateInfo!=null&&cityUpdateInfo.ratio>0)//���֮ǰ���ع�  ����Ҫ���ϴ��뿪ʱһ��
		{
			if(cityUpdateInfo.ratio<100)
				city_scene_alldownload_button_roundProgressBar.setProgress(cityUpdateInfo.ratio);
			city_scene_alldownload_button_imageView.setImageResource(R.drawable.city_scene_download_button_loading);
		}
		city_scene_mapdownload_button = (FrameLayout)findViewById(R.id.city_scene_mapdownload_button);
		city_scene_mapdownload_button.setOnClickListener(new FrameLayout.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isMapDownload)
				{
					Log.e(TAG,"stop download "+cityName+" "+cityId);
					if(mOfflineMapUtil.pause(cityId))
					{
						isMapDownload = false;
						//city_scene_alldownload_button_imageView.setBackgroundResource(R.drawable.city_scene_download_button_loading);
						city_scene_alldownload_button_imageView.setImageResource(R.drawable.city_scene_download_button_loading);
					}		
				}
				else
				{
					Log.e(TAG,"start download "+cityName+" "+cityId);
					if(mOfflineMapUtil.start(cityId))
					{
						isMapDownload = true;
						city_scene_alldownload_button_imageView.setImageResource(R.drawable.city_scene_download_button_pause);
						//city_scene_alldownload_button_imageView.setBackgroundResource(R.drawable.city_scene_download_button_pause);
					}	
				}
			}
		});
	}

	/**
	 * 
	 * @Title: city_head 
	 * @Description: TODO
	 * @author HeZhichao
	 * @date 2014��5��21�� ����7:06:35 
	 * @param 
	 * @return void 
	 * @throws
	 */
	public void city_head()
	{
		city_head_back = (Button)findViewById(R.id.city_head_back);
		city_head_back.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CityActivity.this.finish();
			}
		});
	}

	@Override
	protected void onStart(){
		if(isMapDownload)
			mOfflineMapUtil.start(cityId);
		Log.v("CityActivity", "onStart");
		super.onStart();
	}

	@Override
	protected void onDestroy(){
		Log.v("CityActivity", "onDestroy");
		mOfflineMapUtil.pause(cityId);
		mOfflineMapUtil.destroy();//destroy��˳��һ�����ܸ�
		if(mMapView!=null)
		{
			mMapView.destroy();
			mMapView=null;
		}
		super.onDestroy();
	}
	@Override
	protected void onPause(){
		Log.v("CityActivity", "onPause");
		mMapView.onPause();
		super.onPause();
	}
	@Override
	protected void onResume(){
		Log.v("CityActivity", "onResume");
		mMapView.onResume();
		super.onResume();
	}
	@Override
	protected void onStop(){
		Log.v("CityActivity", "onStop");
		super.onStop();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.city, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 
	 * @ClassName: MyMKOfflineMapListener 
	 * @Description: TODO 
	 * @author HeZhichao
	 * @date 2014��5��21�� ����12:55:09 
	 *
	 */
	public class MyMKOfflineMapListener implements MKOfflineMapListener{
		@Override
		public void onGetOfflineMapState(int type, int state) {
			switch (type) {
			case MKOfflineMap.TYPE_DOWNLOAD_UPDATE:
			{
				MKOLUpdateElement update = mOfflineMapUtil.getUpdateInfo(state);
				//�������ؽ��ȸ�����ʾ
				if ( update != null ){
					//Բ�ν�����ʵʱ��ʾ
					city_scene_alldownload_button_roundProgressBar.setProgress(update.ratio);
					Log.e(TAG,"downing "+update.ratio);
					//					    stateView.setText(String.format("%s : %d%%", update.cityName, update.ratio));
					//					    updateView();
				}
			}
			break;
			case MKOfflineMap.TYPE_NEW_OFFLINE:
				//�������ߵ�ͼ��װ
				Log.d("OfflineDemo", String.format("add offlinemap num:%d", state));
				break;
			case MKOfflineMap.TYPE_VER_UPDATE:
				// �汾������ʾ
				//	MKOLUpdateElement e = mOffline.getUpdateInfo(state);

				break;
			}

		}
	}

	/**
	 * ��ط��ؼ�
	 * @param position
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			finish();
		}
		return true;
	}
}
