package com.ne.voiceguider.activity;

import java.util.ArrayList;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MKOLUpdateElement;
import com.baidu.mapapi.map.MKOfflineMap;
import com.baidu.mapapi.map.MKOfflineMapListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
	private FrameLayout city_scene_mapdownload_button;
	private RoundProgressBar city_scene_alldownload_button_roundProgressBar;
	private ImageView city_scene_alldownload_button_imageView;
	private Boolean isMapDownload,isMapNow;
	private RelativeLayout city_scene_top_layout,city_scene_mapdownload_layout;

	private MapController mMapController;

	private GestureDetector detector;
	private ViewFlipper viewFlipper;
	
	private ListView city_scene_download_listview;
	private BigSceneListAdapter mBigSceneListAdapter = null;
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
				bundle.putInt("bigSceneID", mBigScene.getBigSceneId());
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
		viewFlipper.addView(LayoutInflater.from(this).inflate(R.layout.fragment_city1, null));
		viewFlipper.addView(LayoutInflater.from(this).inflate(R.layout.fragment_city2, null));
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

		mOverlayUtil = new OverlayUtil(mMapView, this);
		
		CityBean mCityBean = new CityBean();
		mCityBean.setCityID(cityID);
		CitySceneDao mCitySceneDao = new CitySceneDao(this);
		mOverlayUtil.setListObject(mCitySceneDao.getBigScenes(mCityBean));
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
		mOfflineMapUtil.pause(cityDownloadId);
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
