package com.ne.voiceguider.activity;


import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.ne.voiceguider.R;
import com.ne.voiceguider.VoiceGuiderApplication;
import com.ne.voiceguider.activity.CityActivity.MyMKOfflineMapListener;
import com.ne.voiceguider.adapter.SmallSceneAdapter;
import com.ne.voiceguider.dao.CitySceneDao;
import com.ne.voiceguider.fragment.HikingFragment.MyBDLocationListenner;
import com.ne.voiceguider.util.LocationUtil;
import com.ne.voiceguider.util.MusicPlayerUtils;
import com.ne.voiceguider.util.OfflineMapUtil;
import com.ne.voiceguider.util.OverlayUtil;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

/**
 * С�����acitivity ������������
 * @ClassName: GuiderActivity 
 * @Description: TODO 
 * @author HeZhichao
 * @date 2014��5��28�� ����12:16:29 
 *
 */
public class GuiderActivity extends ActionBarActivity {

	private final String TAG = "GuiderActivity";
	private String bigSceneName ;
	private int bigSceneID ;
	//head
	private Button guider_scenelist_button,guider_scenemap_button;//private TextView guider_head_text ;
	private Button guider_head_back,scene_voice_text_button;

	private ImageView guider_cursor1, guider_cursor2;

	//webview
	private WebView guider_text_webview ;

	private ListView smallscene_listview = null;


	//flipper
	private ViewFlipper mViewFlipper ;
	private View fragment_guider_map,fragment_guider_list;
	private Boolean isMapNow;

	// music player
	private SeekBar seekBar;
	private Button startMedia;
	private Button stop;
	private MediaPlayer mp;  
	boolean isPlaying= false;
	TextView text_place_name;
	TextView text_time_already;
	TextView text_time_total;

	Handler mHandler= null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Intent  intent = getIntent();
		if ( intent.hasExtra("bigSceneName")  ){
			Bundle b = intent.getExtras();
			bigSceneName = b.getString("bigSceneName");
			
		}
		if ( intent.hasExtra("bigSceneID")  ){
			Bundle b = intent.getExtras();
			bigSceneID = b.getInt("bigSceneID");
		}

		initMap();
		initLocation();
		initOverlay();
		guider_head();
		guider_music();
		webview();
	}

	private MapView mMapView = null;
	private MapController mMapController;
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
		setContentView(R.layout.activity_guider);
		initFlipper();
		mMapView=(MapView)fragment_guider_map.findViewById(R.id.guider_scenemap);
		//mMapView.setBuiltInZoomControls(true);//�����������õ����ſؼ�
		mMapController=mMapView.getController();
		// �õ�mMapView�Ŀ���Ȩ,�����������ƺ�����ƽ�ƺ�����
		GeoPoint point = new GeoPoint((int)(39.915* 1E6),(int)(116.404* 1E6));//�찲��
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
	 * ��λ���
	 * @Title: initLocation 
	 * @Description: TODO
	 * @author HeZhichao
	 * @date 2014��6��4�� ����5:29:57 
	 * @param 
	 * @return void 
	 * @throws
	 */
	private LocationUtil mLocationUtil;
	private MyBDLocationListenner mMyBDLocationListenner;
	private ImageButton guider_location_button ;
	private boolean isLocating = false;//�Ƿ����ڶ�λ
	private boolean isFirstLocation = true;//�Ƿ��״ζ�λ
	public void initLocation()
	{
		mMyBDLocationListenner = new MyBDLocationListenner();
		mLocationUtil = new LocationUtil(this, mMyBDLocationListenner,mMapView);
		guider_location_button = (ImageButton)findViewById(R.id.guider_location_button);
		guider_location_button.setOnClickListener(new ImageButton.OnClickListener() {


			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.v(TAG,"city_location_button");
				if(isLocating==false)
				{
					//					nowGeoPoint = mMapView.getMapCenter();//���涨λǰ��״̬
					//					nowZoomLevel = mMapView.getZoomLevel();
					mLocationUtil.start();
					Toast.makeText(GuiderActivity.this, "���ڶ�λ����", Toast.LENGTH_SHORT).show();
					if(mLocationUtil.isStarted())
					{
						isLocating = true;
						guider_location_button.setImageResource(R.drawable.location_button_return);
						//city_location_button.setText("����");
						Log.v(TAG, "��λ�� ");						
					}
					//mLocClient.requestLocation();	
				}
				else
				{
					mLocationUtil.stop();
					isFirstLocation = true;
					if(mLocationUtil.isStarted()==false)
					{
						isLocating = false;
						Toast.makeText(GuiderActivity.this, "���ؾ���λ�á���", Toast.LENGTH_SHORT).show();
						mOverlayUtil.showSpan();
						//						mMapController.setCenter(nowGeoPoint);//���õ�ͼ���ĵ㣺��һ�ε�λ��
						//						mMapController.setZoom(nowZoomLevel);//���õ�ͼ���ż���
						guider_location_button.setImageResource(R.drawable.location_button_loc);//city_location_button.setText("��λ");
						Log.v(TAG, "��λ�رգ����ؾ���λ�� ");
					}
					else
					{
						Toast.makeText(GuiderActivity.this, "����ʧ�ܣ������ԡ���", Toast.LENGTH_SHORT).show();
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
				guider_location_button.setImageResource(R.drawable.location_button_return);//city_location_button.setText("����");
				Log.v(TAG, "��λ�� ");						
			}
			mLocationUtil.updateLocationData(location);
			//���¶�λ����
			mLocationUtil.setData();
			//����ͼ������ִ��ˢ�º���Ч
			mMapView.refresh();
			//���ֶ�����������״ζ�λʱ���ƶ�����λ��
			if(isFirstLocation)
			{
				mMapController.animateTo(new GeoPoint((int)(location.getLatitude()* 1e6), (int)(location.getLongitude() *  1e6)));
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
	 * ��ͼ������
	 */
	private OverlayUtil mOverlayUtil;
	public void initOverlay()
	{

		mOverlayUtil = OverlayUtil.newInstanceForSmallScenes(mMapView, this);
		CitySceneDao mCitySceneDao = new CitySceneDao(this);
		mOverlayUtil.setListObject(mCitySceneDao.getSmallScenes(bigSceneID));
		mOverlayUtil.showAll();
	}
	
	private void guider_music() {
		// music player
		mp = MediaPlayer.create(GuiderActivity.this, R.raw.test_music); // set the music rc

		text_place_name= (TextView) findViewById(R.id.scene_music_place_name);

		text_time_already= (TextView) findViewById(R.id.scene_music_time_already);
		text_time_total= (TextView) findViewById(R.id.scene_music_time_total);

		seekBar = (SeekBar) findViewById(R.id.scene_music_seekbar);
		seekBar.setProgress(0);
		seekBar.setMax(mp.getDuration());
		updateMusicProgressText();
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			int lastProgress;
			int originalProgress;//����Ľ�����
			boolean isThumbClick= false;

			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
				if(fromTouch == true){
					// only allow changes by 1 up or down
					if(Math.abs(progress-lastProgress)*1.0 / seekBar.getMax() > 0.1){
						seekBar.setProgress(lastProgress);
					} else {
						lastProgress = progress;
						isThumbClick= true;
					}
				} 
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

				if(! isThumbClick){
					// update timer progress again
					updateMusicProgressText();
					return;
				}

				mp.seekTo(lastProgress);
				// update timer progress again
				updateMusicProgressText();
				if(Math.abs(lastProgress-originalProgress)*1.0 / seekBar.getMax() > 0.1){
					return;
				}

				if(isPlaying){
					mp.pause();
					seekBar.setThumb(getResources().getDrawable(R.drawable.thumb_pause));
					isPlaying= false;
				}
				else{
					mp.start();
					seekBar.setThumb(getResources().getDrawable(R.drawable.thumb_playing));
					isPlaying= true;
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				mHandler.removeCallbacks(mUpdateTimeTask);
				originalProgress= lastProgress = seekBar.getProgress();
			}
		});

	}

	/**
	 *  ��acitivity��ͷ������
	 */
	void guider_head()
	{
		guider_scenelist_button = (Button)findViewById(R.id.guider_scenelist_button);
		guider_scenemap_button = (Button)findViewById(R.id.guider_scenemap_button);
		//guider_head_text = (TextView)findViewById(R.id.guider_head_text);
		guider_scenelist_button.setText(bigSceneName+"�б�");
		guider_scenemap_button.setText(bigSceneName+"��ͼ");
		//guider_head_text.setText(bigSceneName);
		guider_cursor1 = (ImageView)findViewById(R.id.guider_cursor1);
		guider_cursor2 = (ImageView)findViewById(R.id.guider_cursor2);
		View.OnClickListener switchOnClickListener = new View.OnClickListener() {

			int index;
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				switch (view.getId()) {  
				case R.id.guider_scenelist_button:
				{
					if(isMapNow == true)
					{
						leftToRight();
						isMapNow = false;
					}
					guider_cursor1.setVisibility(View.VISIBLE);
					guider_cursor2.setVisibility(View.INVISIBLE);
					index=0;break;
				}

				case R.id.guider_scenemap_button:
				{
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
						Toast.makeText(GuiderActivity.this, "���ؾ���λ�á���", Toast.LENGTH_SHORT).show();
					}
					guider_cursor2.setVisibility(View.VISIBLE);
					guider_cursor1.setVisibility(View.INVISIBLE);
					index=1;break;
				}
				default:
					index=-1;
					break;
				}
			}
		};
		guider_scenelist_button.setOnClickListener(switchOnClickListener);
		guider_scenemap_button.setOnClickListener(switchOnClickListener);


		guider_head_back= (Button)findViewById(R.id.guider_head_back);
		guider_head_back.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}

	//��ͼ���б��flipper
	void initFlipper() {
		isMapNow = false;
		mViewFlipper = (ViewFlipper) findViewById(R.id.flipper);
		fragment_guider_list = LayoutInflater.from(this).inflate(R.layout.fragment_guider_list, null);
		mViewFlipper.addView(fragment_guider_list);
		fragment_guider_map = LayoutInflater.from(this).inflate(R.layout.fragment_guider_map, null);
		mViewFlipper.addView(fragment_guider_map);
		smallscene_listview = (ListView)fragment_guider_list.findViewById(R.id.smallscene_listview);
		smallscene_listview.setAdapter(new SmallSceneAdapter(this, bigSceneID));
	}
	void leftToRight()
	{
		mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
				R.anim.rightin));
		mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
				R.anim.rightout));
		mViewFlipper.showNext();
		isMapNow = !isMapNow;
	}
	void RightToLeft()
	{
		mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
				R.anim.leftin));
		mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
				R.anim.leftout));
		mViewFlipper.showPrevious();
		isMapNow = !isMapNow;
	}

	/**
	 * ���������� webview���ֵĴ���
	 */
	void webview()
	{
		loadAssetHtml();
		guider_text_webview = (WebView)findViewById(R.id.guider_text_webview);
		scene_voice_text_button = (Button)findViewById(R.id.scene_voice_text_button);
		scene_voice_text_button.setOnClickListener(new Button.OnClickListener() {
			private boolean haveWebView = false;
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(haveWebView==false)
				{
					guider_text_webview.setVisibility(View.VISIBLE);
					haveWebView = true;
				}
				else
				{
					guider_text_webview.setVisibility(View.INVISIBLE);
					haveWebView = false;
				}
			}
		});

		loadAssetHtml();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.guider, menu);
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
	 * �����ı���ͼ
	 * @Title: loadAssetHtml 
	 * @Description: TODO
	 * @author HeZhichao
	 * @date 2014��5��28�� ����2:16:09 
	 * @param 
	 * @return void 
	 * @throws
	 */
	public void loadAssetHtml() {

		final String mimeType = "text/html";  
		final String encoding = "utf-8"; 
		String url = "http://www.baidu.com/";
		guider_text_webview = (WebView) findViewById(R.id.guider_text_webview);
		// ����WebView���ԣ��ܹ�ִ��JavaScript�ű�
		guider_text_webview.getSettings().setJavaScriptEnabled(true);
		guider_text_webview.getSettings().setDefaultTextEncodingName(encoding);
		guider_text_webview.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
		guider_text_webview.setHorizontalScrollBarEnabled(false);
		guider_text_webview.getSettings().setSupportZoom(true);
		guider_text_webview.getSettings().setBuiltInZoomControls(true);
		guider_text_webview.setInitialScale(70);
		guider_text_webview.setHorizontalScrollbarOverlay(true);
		guider_text_webview.setWebViewClient(new WebViewClient());
		guider_text_webview.loadUrl("file:///android_asset/HTML/html/guangzhou/yuexiugongyuan/guangzhoubowuguan.html");
		guider_text_webview.getSettings().setUseWideViewPort(true);
		guider_text_webview.getSettings().setLoadWithOverviewMode(true);
		guider_text_webview.getSettings().setBuiltInZoomControls(false);
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

	public class MyWebViewClient extends WebViewClient {
		/**
		 * Show in webview not system webview.
		 */
		public boolean shouldOverviewUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return super.shouldOverrideUrlLoading(view, url);
		}
	}

	public void updateMusicProgressText() {
		if( mHandler == null ){
			mHandler= new Handler();
		}
		mHandler.postDelayed(mUpdateTimeTask, 100);
	}   

	/**
	 * Background Runnable thread
	 * */
	private Runnable mUpdateTimeTask = new Runnable() {
		public void run() {
			int totalDuration = mp.getDuration();
			int currentDuration = mp.getCurrentPosition();

			// Updating progress bar
			seekBar.setProgress(currentDuration);

			// Displaying Total Duration time
			text_time_total.setText(""+ MusicPlayerUtils.milliSecondsToTimer(totalDuration));
			// Displaying time completed playing
			text_time_already.setText(""+ MusicPlayerUtils.milliSecondsToTimer(currentDuration));

			// Running this thread after 100 milliseconds
			mHandler.postDelayed(this, 100);
		}
	};

}

