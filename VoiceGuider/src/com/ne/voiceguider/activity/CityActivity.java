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
 * @date 2014年5月20日 下午3:01:52 
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

	// 定位相关
	private LocationClient mLocClient;
	private LocationData locData = null;
	public MyBDLocationListenner myListener = new MyBDLocationListenner();
	private boolean isLocating = false;//是否正在定位
//	private GeoPoint nowGeoPoint ;
//	private float nowZoomLevel = 10;//需求不需要
	//定位图层
	private MyLocationOverlay myLocationOverlay = null;
	private ImageButton city_location_button ;
	private boolean isFirstLocation = true;//是否首次定位
	//方向相关
	private SensorManager sensorManager;
	private MySensorEventListener mySensorEventListener;

	/**
	 * 地图上面插标
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

		//initOrientation(); 放到onResume()里面

	}

	/**
	 * 方向传感器
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

	/**
	 * 定位相关
	 */
	protected void initLocation()
	{
		//定位初始化
		mLocClient = new LocationClient( this );
		locData = new LocationData();
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

		city_location_button = (ImageButton)findViewById(R.id.city_location_button);
		city_location_button.setOnClickListener(new ImageButton.OnClickListener() {


			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.v(TAG,"city_location_button");
				if(isLocating==false)
				{
//					nowGeoPoint = mMapView.getMapCenter();//储存定位前的状态
//					nowZoomLevel = mMapView.getZoomLevel();
					mLocClient.start();
					Toast.makeText(CityActivity.this, "正在定位……", Toast.LENGTH_SHORT).show();
					if(mLocClient.isStarted())
					{
						isLocating = true;
						city_location_button.setImageResource(R.drawable.location_button_return);
						//city_location_button.setText("返回");
						Log.v(TAG, "定位打开 ");						
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
						Toast.makeText(CityActivity.this, "返回景点位置……", Toast.LENGTH_SHORT).show();
						mOverlayUtil.showSpan();
//						mMapController.setCenter(nowGeoPoint);//设置地图中心点：上一次的位置
//						mMapController.setZoom(nowZoomLevel);//设置地图缩放级别
						city_location_button.setImageResource(R.drawable.location_button_loc);//city_location_button.setText("定位");
						Log.v(TAG, "定位关闭，返回景点位置 ");

					}
					else
					{
						Toast.makeText(CityActivity.this, "返回失败，请重试……", Toast.LENGTH_SHORT).show();
					}

				}

			}
		});
	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyBDLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return ;
			if(isFirstLocation)
			{
				isLocating = true;
				city_location_button.setImageResource(R.drawable.location_button_return);//city_location_button.setText("返回");
				Log.v(TAG, "定位打开 ");						
			}
			locData.latitude = location.getLatitude();
			locData.longitude = location.getLongitude();
			//如果不显示定位精度圈，将accuracy赋值为0即可
			locData.accuracy = location.getRadius();
			// 此处可以设置 locData的方向信息, 如果定位 SDK 未返回方向信息，用户可以自己实现罗盘功能添加方向信息。
			//locData.direction = location.getDirection();
			Log.v(TAG,"BDLocationListener Derect: "+locData.direction);
			//更新定位数据
			myLocationOverlay.setData(locData);
			//更新图层数据执行刷新后生效
			mMapView.refresh();
			//是手动触发请求或首次定位时，移动到定位点
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
	 * @date 2014年5月29日 下午7:01:59 
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
				Intent intent = new Intent(CityActivity.this,GuiderActivity.class); // 跳转到城市景点详情页面 
				Bundle bundle = new Bundle();                           //创建Bundle对象   
				bundle.putString("bigSceneName", mBigScene.getBigSceneName());     //装入数据  
				bundle.putInt("bigSceneID", mBigScene.getBigSceneID());
				intent.putExtras(bundle);                            //把Bundle塞入Intent里面   
				startActivity(intent);                                     //开始切换 
			}
		});
		Log.v(TAG, "bigSceneListview()");
	}
	/**
	 * 
	 * @Title: initMap 
	 * @Description: 
	 * @author HeZhichao
	 * @date 2014年5月20日 下午3:01:52 
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
			 * 如果BMapManager没有初始化则初始化BMapManager
			 */
			app.mBMapManager.init(new VoiceGuiderApplication.MyGeneralListener());
		}
		//注意：请在试用setContentView前初始化BMapManager对象，否则会报错
		setContentView(R.layout.activity_city);
		initFlipper();
		mMapView=(MapView)findViewById(R.id.city_scenemap);
		//mMapView.setBuiltInZoomControls(true);//设置启用内置的缩放控件
		mMapController=mMapView.getController();
		// 得到mMapView的控制权,可以用它控制和驱动平移和缩放
		GeoPoint point = null;
		Intent  intent = getIntent();
		mOfflineMapUtil = new OfflineMapUtil(this,mMapView,new MyMKOfflineMapListener());
		if ( intent.hasExtra("cityName")  ){
			//当用intent参数时，设置中心点为指定点
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
			//设置中心点为天安门
			point = new GeoPoint((int)(39.915* 1E6),(int)(116.404* 1E6));
		}
		//用给定的经纬度构造一个GeoPoint，单位是微度 (度 * 1E6)
		mMapController.setCenter(point);//设置地图中心点
		mMapController.setZoom(12);//设置地图zoom级别
		mMapController.setCompassMargin(90, 90);//设置指南针位置
		/**
		 *  设置地图是否响应点击事件  .
		 */
		mMapController.enableClick(true);
	}
	/**
	 * 初始化ViewFlipper   （实现接口OnGestureListener也是为了ViewFlipper）
	 * @Title: initFlipper 
	 * @Description: TODO
	 * @author HeZhichao
	 * @date 2014年5月28日 下午5:20:44 
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
	 * 有关于界面切换显示的逻辑处理
	 * @Title: list_maplayoutSwitch 
	 * @Description: 
	 * @author HeZhichao
	 * @date 2014年5月20日
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
					Thread thread = new Thread(){//设置晚0.1s显示 解决地图界面显示错误
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
				else//如果正处于地图位置  返回景点位置
				{
					mOverlayUtil.showSpan();
					Toast.makeText(CityActivity.this, "返回景点位置……", Toast.LENGTH_SHORT).show();
				}


			}
		});


	}

	/**
	 * 地图下载的设置
	 * @Title: mapDownload 
	 * @Description: 
	 * @author HeZhichao
	 * @date 2014年5月21日 下午4:31:43 
	 * @param 
	 * @return void 
	 * @throws
	 */
	public void mapDownload()
	{
		isMapDownload = false;
		city_scene_alldownload_button_roundProgressBar = (RoundProgressBar)findViewById(R.id.city_scene_alldownload_button_roundProgressBar);
		city_scene_alldownload_button_imageView = (ImageView)findViewById(R.id.city_scene_alldownload_button_imageView);
		if(cityUpdateInfo!=null&&cityUpdateInfo.ratio>0)//如果之前下载过  界面要和上次离开时一样
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
	 * @date 2014年5月21日 下午7:06:35 
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
	 * 地图覆盖物
	 * @Title: initOverlay 
	 * @Description: TODO
	 * @author HeZhichao
	 * @date 2014年5月22日 下午5:48:09 
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
		//		 * 准备overlay 数据
		//		 */
		//		GeoPoint p1 = new GeoPoint ((int)(23.143637*1E6),(int)(113.274189*1E6));
		//		OverlayItem item1 = new OverlayItem(p1,"广州美术馆","");
		//		/**
		//		 * 设置overlay图标，如不设置，则使用创建ItemizedOverlay时的默认图标.
		//		 */
		//		item1.setMarker(getResources().getDrawable(R.drawable.city_scene_overlay_icon));
		//
		//		GeoPoint p2 = new GeoPoint ((int)(23.14274*1E6),(int)(113.269904*1E6));
		//		OverlayItem item2 = new OverlayItem(p2,"明代古城墙","");
		//		item1.setMarker(getResources().getDrawable(R.drawable.city_scene_overlay_icon));
		//		/**
		//		 * 将item 添加到overlay中
		//		 * 注意： 同一个item只能add一次
		//		 */
		//		mOverlayUtil.addItem(item1);
		//		mOverlayUtil.addItem(item2);
		/**
		 * 保存所有item，以便overlay在reset后重新添加
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
		mOfflineMapUtil.pause(cityDownloadId);//只有destroy的时候才暂停下载
		mOfflineMapUtil.destroy();//destroy的顺序一定不能改
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
	 * @date 2014年5月21日 下午12:55:09 
	 *
	 */
	public class MyMKOfflineMapListener implements MKOfflineMapListener{
		@Override
		public void onGetOfflineMapState(int type, int state) {
			switch (type) {
			case MKOfflineMap.TYPE_DOWNLOAD_UPDATE:
			{
				MKOLUpdateElement update = mOfflineMapUtil.getUpdateInfo(state);
				//处理下载进度更新提示
				if ( update != null ){
					//圆形进度条实时显示
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
				//有新离线地图安装
				Log.d("OfflineDemo", String.format("add offlinemap num:%d", state));
				//				isMapDownload = false;
				//				city_scene_alldownload_button_imageView.setImageResource(R.drawable.city_scene_download_button_loading);
				break;
			case MKOfflineMap.TYPE_VER_UPDATE:
				// 版本更新提示
				//	MKOLUpdateElement e = mOffline.getUpdateInfo(state);

				break;
			}

		}
	}


	/**
	 * 监控返回键
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


	////// 以下是OnGestureListener接口的函数   

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		//将该Activity上的触碰事件交给GestureDetector处理
		return detector.onTouchEvent(event);
	}
	@Override
	/***
	 * 滑动手势
	 */
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		Log.v("", "onFling");
		/*
		 * 如果第二个触点事件的X座标大于第一个触点事件的X座标超过FLIP_DISTANCE 
		 * 也就是手势从左向右滑。
		 */
		if (e2.getX() - e1.getX() > 50) {
			leftToRight();
			return true;

		} 
		/*
		 * 如果第 一个触点事件的X座标大于第二个触点事件的X座标超过FLIP_DISTANCE 
		 * 也就是手势从右向左滑。
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
