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
import com.ne.voiceguider.activity.HikingActivity.HikingCityOnClickListener;
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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;
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
	private int cityId = -1;
	private MKOLUpdateElement cityUpdateInfo = null;
	private Button city_scenelist_button,city_scenemap_button,city_head_back;
	private FrameLayout city_scene_mapdownload_button;
	private RoundProgressBar city_scene_alldownload_button_roundProgressBar;
	private ImageView city_scene_alldownload_button_imageView;
	private Boolean isMapDownload,isMapNow;
	private RelativeLayout city_scene_top_layout,city_scene_mapdownload_layout,city_scene_download_listview;

	private MapController mMapController;

	private GestureDetector detector;
	private ViewFlipper viewFlipper;
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
			cityId = mOfflineMapUtil.search(cityName);
			cityUpdateInfo = mOfflineMapUtil.getUpdateInfo(cityId);
			Log.e(TAG,cityName+" "+cityId+" "+(cityUpdateInfo==null));
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
		viewFlipper.addView(LayoutInflater.from(this).inflate(R.layout.fragment_city1, null));
		viewFlipper.addView(LayoutInflater.from(this).inflate(R.layout.fragment_city2, null));
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
		city_scene_download_listview = (RelativeLayout)findViewById(R.id.city_scene_download_listview);
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

		mOverlayUtil = new OverlayUtil(mMapView, this);
		/**
		 * ׼��overlay ����
		 */
		GeoPoint p1 = new GeoPoint ((int)(23.143637*1E6),(int)(113.274189*1E6));
		OverlayItem item1 = new OverlayItem(p1,"����������","");
		/**
		 * ����overlayͼ�꣬�粻���ã���ʹ�ô���ItemizedOverlayʱ��Ĭ��ͼ��.
		 */
		item1.setMarker(getResources().getDrawable(R.drawable.city_scene_overlay_icon));

		GeoPoint p2 = new GeoPoint ((int)(23.14274*1E6),(int)(113.269904*1E6));
		OverlayItem item2 = new OverlayItem(p2,"�����ų�ǽ","");
		item1.setMarker(getResources().getDrawable(R.drawable.city_scene_overlay_icon));
		/**
		 * ��item ��ӵ�overlay��
		 * ע�⣺ ͬһ��itemֻ��addһ��
		 */
		mOverlayUtil.addItem(item1);
		mOverlayUtil.addItem(item2);
		/**
		 * ��������item���Ա�overlay��reset���������
		 */
		mOverlayUtil.showAll();

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
