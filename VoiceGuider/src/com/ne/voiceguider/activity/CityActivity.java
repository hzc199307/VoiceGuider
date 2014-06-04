package com.ne.voiceguider.activity;

import java.util.ArrayList;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKOLUpdateElement;
import com.baidu.mapapi.map.MKOfflineMap;
import com.baidu.mapapi.map.MKOfflineMapListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.ne.voiceguider.R;
import com.ne.voiceguider.R.id;
import com.ne.voiceguider.R.layout;
import com.ne.voiceguider.R.menu;
import com.ne.voiceguider.VoiceGuiderApplication;
import com.ne.voiceguider.adapter.BigSceneListAdapter;
import com.ne.voiceguider.bean.BigScene;
import com.ne.voiceguider.bean.CityBean;
import com.ne.voiceguider.dao.CitySceneDao;
import com.ne.voiceguider.util.BMapUtil;
import com.ne.voiceguider.util.OfflineMapUtil;
import com.ne.voiceguider.util.OverlayUtil;
import com.ne.voiceguider.view.RoundProgressBar;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.animation.AnimationUtils;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.widget.AdapterView.OnItemClickListener;
import android.os.Build;

/**
 * 
 * @ClassName: CityActivity 
 * @Description: TODO 
 * @author HeZhichao
 * @date 2014��5��20�� ����3:01:52 
 *
 */
public class CityActivity extends ActionBarActivity implements OnGestureListener 
{

	private String TAG = "CityActivity";
	private MapView mMapView = null;
	private OfflineMapUtil mOfflineMapUtil = null;
	private String cityName = null;
	private int cityID,cityDownloadId = -1;
	private MKOLUpdateElement cityUpdateInfo = null;
	private Button city_scenelist_button,city_scenemap_button,city_head_back;
	private TextView city_head_text;
	private FrameLayout city_scene_mapdownload_button;
	private RoundProgressBar city_scene_alldownload_button_roundProgressBar;
	private ImageView city_scene_alldownload_button_imageView;
	private Boolean isMapDownload,isMapNow;
	private RelativeLayout city_scene_top_layout,city_scene_mapdownload_layout;

	private MapController mMapController;

	private GestureDetector detector;
	private ViewFlipper viewFlipper;
	private View fragment_city_bigscenelist,fragment_city_map;
	
	private ListView city_scene_download_listview;
	private BigSceneListAdapter mBigSceneListAdapter = null;

	// ��λ���
	private LocationClient mLocClient;
	private LocationData locData = null;
	public MyBDLocationListenner myListener = new MyBDLocationListenner();
	private boolean isLocating = false;//�Ƿ����ڶ�λ
//	private GeoPoint nowGeoPoint ;
//	private float nowZoomLevel = 10;//������Ҫ
	//��λͼ��
	private MyLocationOverlay myLocationOverlay = null;
	private ImageButton city_location_button ;
	private boolean isFirstLocation = true;//�Ƿ��״ζ�λ
	//�������
	private SensorManager sensorManager;
	private MySensorEventListener mySensorEventListener;

	/**
	 * ��ͼ������
	 */
	private OverlayUtil mOverlayUtil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.v("CityActivity", "onCreate");
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		initMap();

		list_maplayoutSwitch();

		mapDownload();

		city_head();

		initOverlay();

		bigSceneListview();

		initLocation();

		//initOrientation(); �ŵ�onResume()����

	}

	/**
	 * ���򴫸���
	 */
	void initOrientation()
	{
		sensorManager= (SensorManager) this.getSystemService(SENSOR_SERVICE);
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

	/**
	 * ��λ���
	 */
	protected void initLocation()
	{
		//��λ��ʼ��
		mLocClient = new LocationClient( this );
		locData = new LocationData();
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

		city_location_button = (ImageButton)findViewById(R.id.city_location_button);
		city_location_button.setOnClickListener(new ImageButton.OnClickListener() {


			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.v(TAG,"city_location_button");
				if(isLocating==false)
				{
//					nowGeoPoint = mMapView.getMapCenter();//���涨λǰ��״̬
//					nowZoomLevel = mMapView.getZoomLevel();
					mLocClient.start();
					Toast.makeText(CityActivity.this, "���ڶ�λ����", Toast.LENGTH_SHORT).show();
					if(mLocClient.isStarted())
					{
						isLocating = true;
						city_location_button.setImageResource(R.drawable.location_button_return);
						//city_location_button.setText("����");
						Log.v(TAG, "��λ�� ");						
					}
					//mLocClient.requestLocation();	
				}
				else
				{
					mLocClient.stop();
					isFirstLocation = true;
					if(mLocClient.isStarted()==false)
					{
						isLocating = false;
						Toast.makeText(CityActivity.this, "���ؾ���λ�á���", Toast.LENGTH_SHORT).show();
						mOverlayUtil.showSpan();
//						mMapController.setCenter(nowGeoPoint);//���õ�ͼ���ĵ㣺��һ�ε�λ��
//						mMapController.setZoom(nowZoomLevel);//���õ�ͼ���ż���
						city_location_button.setImageResource(R.drawable.location_button_loc);//city_location_button.setText("��λ");
						Log.v(TAG, "��λ�رգ����ؾ���λ�� ");

					}
					else
					{
						Toast.makeText(CityActivity.this, "����ʧ�ܣ������ԡ���", Toast.LENGTH_SHORT).show();
					}

				}

			}
		});
	}

	/**
	 * ��λSDK��������
	 */
	public class MyBDLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return ;
			if(isFirstLocation)
			{
				isLocating = true;
				city_location_button.setImageResource(R.drawable.location_button_return);//city_location_button.setText("����");
				Log.v(TAG, "��λ�� ");						
			}
			locData.latitude = location.getLatitude();
			locData.longitude = location.getLongitude();
			//�������ʾ��λ����Ȧ����accuracy��ֵΪ0����
			locData.accuracy = location.getRadius();
			// �˴��������� locData�ķ�����Ϣ, �����λ SDK δ���ط�����Ϣ���û������Լ�ʵ�����̹�����ӷ�����Ϣ��
			//locData.direction = location.getDirection();
			Log.v(TAG,"BDLocationListener Derect: "+locData.direction);
			//���¶�λ����
			myLocationOverlay.setData(locData);
			//����ͼ������ִ��ˢ�º���Ч
			mMapView.refresh();
			//���ֶ�����������״ζ�λʱ���ƶ�����λ��
			if(isFirstLocation)
			{
				mMapController.animateTo(new GeoPoint((int)(locData.latitude* 1e6), (int)(locData.longitude *  1e6)));
				isFirstLocation = false;
			}	
		}

		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null){
				return ;
			}
		}
	}
	/**
	 * 
	 * @Title: bigSceneListview 
	 * @Description: TODO
	 * @author HeZhichao
	 * @date 2014��5��29�� ����7:01:59 
	 * @param 
	 * @return void 
	 * @throws
	 */
	void bigSceneListview()
	{
		city_scene_download_listview = (ListView)findViewById(R.id.city_scene_download_listview);
		mBigSceneListAdapter = new BigSceneListAdapter(this,cityID);
		city_scene_download_listview.setAdapter(mBigSceneListAdapter);
		city_scene_download_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				BigScene mBigScene = mBigSceneListAdapter.getItem(position);
				Intent intent = new Intent(CityActivity.this,GuiderActivity.class); // ��ת�����о�������ҳ�� 
				Bundle bundle = new Bundle();                           //����Bundle����   
				bundle.putString("bigSceneName", mBigScene.getBigSceneName());     //װ������  
				bundle.putInt("bigSceneID", mBigScene.getBigSceneID());
				intent.putExtras(bundle);                            //��Bundle����Intent����   
				startActivity(intent);                                     //��ʼ�л� 
			}
		});
		Log.v(TAG, "bigSceneListview()");
	}
	/**
	 * 
	 * @Title: initMap 
	 * @Description: 
	 * @author HeZhichao
	 * @date 2014��5��20�� ����3:01:52 
	 * @param 
	 * @return void 
	 * @throws
	 */
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
		initFlipper();
		mMapView=(MapView)findViewById(R.id.city_scenemap);
		//mMapView.setBuiltInZoomControls(true);//�����������õ����ſؼ�
		mMapController=mMapView.getController();
		// �õ�mMapView�Ŀ���Ȩ,�����������ƺ�����ƽ�ƺ�����
		GeoPoint point = null;
		Intent  intent = getIntent();
		mOfflineMapUtil = new OfflineMapUtil(this,mMapView,new MyMKOfflineMapListener());
		if ( intent.hasExtra("cityName")  ){
			//����intent����ʱ���������ĵ�Ϊָ����
			Bundle b = intent.getExtras();
			cityName = b.getString("cityName");
			cityID = b.getInt("cityID");
			cityDownloadId = mOfflineMapUtil.search(cityName);
			cityUpdateInfo = mOfflineMapUtil.getUpdateInfo(cityDownloadId);
			Log.e(TAG,cityName+" "+cityDownloadId+" "+(cityUpdateInfo==null));
			Log.v("intent",cityName);
			point = mOfflineMapUtil.searchGeoPoint(cityName);
			if(point!=null)
				Log.v(TAG,"latE6:"+point.getLatitudeE6()+" ,lonE6"+point.getLongitudeE6());
		}else{
			//�������ĵ�Ϊ�찲��
			point = new GeoPoint((int)(39.915* 1E6),(int)(116.404* 1E6));
		}
		//�ø����ľ�γ�ȹ���һ��GeoPoint����λ��΢�� (�� * 1E6)
		mMapController.setCenter(point);//���õ�ͼ���ĵ�
		mMapController.setZoom(12);//���õ�ͼzoom����
		mMapController.setCompassMargin(90, 90);//����ָ����λ��
		/**
		 *  ���õ�ͼ�Ƿ���Ӧ����¼�  .
		 */
		mMapController.enableClick(true);
	}
	/**
	 * ��ʼ��ViewFlipper   ��ʵ�ֽӿ�OnGestureListenerҲ��Ϊ��ViewFlipper��
	 * @Title: initFlipper 
	 * @Description: TODO
	 * @author HeZhichao
	 * @date 2014��5��28�� ����5:20:44 
	 * @param 
	 * @return void 
	 * @throws
	 */
	protected void initFlipper() {
		viewFlipper = (ViewFlipper) findViewById(R.id.flipper);
		fragment_city_bigscenelist = LayoutInflater.from(this).inflate(R.layout.fragment_city_bigscenelist, null);
		viewFlipper.addView(fragment_city_bigscenelist);
		fragment_city_map = LayoutInflater.from(this).inflate(R.layout.fragment_city_map, null);
		viewFlipper.addView(fragment_city_map);
		detector = new GestureDetector(this);
	}

	/**
	 * �й��ڽ����л���ʾ���߼�����
	 * @Title: list_maplayoutSwitch 
	 * @Description: 
	 * @author HeZhichao
	 * @date 2014��5��20��
	 * @param 
	 * @return void 
	 * @throws
	 */
	public void list_maplayoutSwitch()
	{

		isMapNow = false;
		city_scene_top_layout = (RelativeLayout)findViewById(R.id.city_scene_top_layout);
		city_scene_mapdownload_layout = (RelativeLayout)findViewById(R.id.city_scene_mapdownload_layout);
		city_scenelist_button = (Button)findViewById(R.id.city_scenelist_button);
		city_scenelist_button.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(isMapNow == true)
				{
					leftToRight();
					isMapNow = false;
				}
			}
		});
		city_scenemap_button = (Button)findViewById(R.id.city_scenemap_button);
		city_scenemap_button.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(isMapNow == false)
				{
					RightToLeft();
					Thread thread = new Thread(){//������0.1s��ʾ �����ͼ������ʾ����
						@Override
						public void run(){
							try {
								Thread.currentThread().sleep(100);
								mOverlayUtil.showSpan();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					};
					thread.start();
					isMapNow = true;
				}
				else//��������ڵ�ͼλ��  ���ؾ���λ��
				{
					mOverlayUtil.showSpan();
					Toast.makeText(CityActivity.this, "���ؾ���λ�á���", Toast.LENGTH_SHORT).show();
				}


			}
		});


	}

	/**
	 * ��ͼ���ص�����
	 * @Title: mapDownload 
	 * @Description: 
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
				if(isMapDownload)
				{
					Log.e(TAG,"stop download "+cityName+" "+cityDownloadId);
					if(mOfflineMapUtil.pause(cityDownloadId))
					{
						isMapDownload = false;
						//city_scene_alldownload_button_imageView.setBackgroundResource(R.drawable.city_scene_download_button_loading);
						city_scene_alldownload_button_imageView.setImageResource(R.drawable.city_scene_download_button_loading);
					}		
				}
				else
				{
					Log.e(TAG,"start download "+cityName+" "+cityDownloadId);
					if(mOfflineMapUtil.start(cityDownloadId))
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
	 * @Description: 
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
				CityActivity.this.finish();
			}
		});
		city_head_text = (TextView)findViewById(R.id.city_head_text);
		city_head_text.setText(cityName);
	}

	/**
	 * ��ͼ������
	 * @Title: initOverlay 
	 * @Description: TODO
	 * @author HeZhichao
	 * @date 2014��5��22�� ����5:48:09 
	 * @param 
	 * @return void 
	 * @throws
	 */
	public void initOverlay()
	{

		mOverlayUtil = mOverlayUtil = OverlayUtil.newInstanceForBigScenes(mMapView, this);
		CitySceneDao mCitySceneDao = new CitySceneDao(this);
		mOverlayUtil.setListObject(mCitySceneDao.getBigScenes(cityID));
		//		/**
		//		 * ׼��overlay ����
		//		 */
		//		GeoPoint p1 = new GeoPoint ((int)(23.143637*1E6),(int)(113.274189*1E6));
		//		OverlayItem item1 = new OverlayItem(p1,"����������","");
		//		/**
		//		 * ����overlayͼ�꣬�粻���ã���ʹ�ô���ItemizedOverlayʱ��Ĭ��ͼ��.
		//		 */
		//		item1.setMarker(getResources().getDrawable(R.drawable.city_scene_overlay_icon));
		//
		//		GeoPoint p2 = new GeoPoint ((int)(23.14274*1E6),(int)(113.269904*1E6));
		//		OverlayItem item2 = new OverlayItem(p2,"�����ų�ǽ","");
		//		item1.setMarker(getResources().getDrawable(R.drawable.city_scene_overlay_icon));
		//		/**
		//		 * ��item ��ӵ�overlay��
		//		 * ע�⣺ ͬһ��itemֻ��addһ��
		//		 */
		//		mOverlayUtil.addItem(item1);
		//		mOverlayUtil.addItem(item2);
		/**
		 * ��������item���Ա�overlay��reset���������
		 */
		mOverlayUtil.showAll();

	}

	@Override
	protected void onStart(){
		if(isMapDownload)
			mOfflineMapUtil.start(cityDownloadId);
		Log.v("CityActivity", "onStart");
		super.onStart();
	}

	@Override
	protected void onDestroy(){
		Log.v("CityActivity", "onDestroy");
		mLocClient.stop();
		mOfflineMapUtil.pause(cityDownloadId);//ֻ��destroy��ʱ�����ͣ����
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
		sensorManager.unregisterListener(mySensorEventListener);
		mMapView.onPause();
		super.onPause();
	}
	@Override
	protected void onResume(){
		Log.v("CityActivity", "onResume");
		initOrientation();
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
					if(update.ratio==100)
					{
						isMapDownload = false;
						city_scene_alldownload_button_imageView.setImageResource(R.drawable.city_scene_download_button_loading);
					}
				}
			}
			break;
			case MKOfflineMap.TYPE_NEW_OFFLINE:
				//�������ߵ�ͼ��װ
				Log.d("OfflineDemo", String.format("add offlinemap num:%d", state));
				//				isMapDownload = false;
				//				city_scene_alldownload_button_imageView.setImageResource(R.drawable.city_scene_download_button_loading);
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


	////// ������OnGestureListener�ӿڵĺ���   

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		//����Activity�ϵĴ����¼�����GestureDetector����
		return detector.onTouchEvent(event);
	}
	@Override
	/***
	 * ��������
	 */
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		Log.v("", "onFling");
		/*
		 * ����ڶ��������¼���X������ڵ�һ�������¼���X���곬��FLIP_DISTANCE 
		 * Ҳ�������ƴ������һ���
		 */
		if (e2.getX() - e1.getX() > 50) {
			leftToRight();
			return true;

		} 
		/*
		 * ����� һ�������¼���X������ڵڶ��������¼���X���곬��FLIP_DISTANCE 
		 * Ҳ�������ƴ������󻬡�
		 */
		else if (e1.getX() - e2.getX() > 50) {

			RightToLeft();
			return true;

		}
		return false;
	}
	void leftToRight()
	{
		viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
				R.anim.rightin));
		viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
				R.anim.rightout));
		viewFlipper.showNext();
		isMapNow = !isMapNow;
	}

	void RightToLeft()
	{
		viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
				R.anim.leftin));
		viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
				R.anim.leftout));
		viewFlipper.showPrevious();
		isMapNow = !isMapNow;
	}
	@Override
	public void onLongPress(MotionEvent e) {
	}
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}
	@Override
	public void onShowPress(MotionEvent e) {	
	}
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}
}
