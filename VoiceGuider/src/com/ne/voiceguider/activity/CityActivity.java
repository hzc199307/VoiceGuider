package com.ne.voiceguider.activity;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.ne.voiceguider.R;
import com.ne.voiceguider.R.id;
import com.ne.voiceguider.R.layout;
import com.ne.voiceguider.R.menu;
import com.ne.voiceguider.util.OfflineMapUtil;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
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

	private BMapManager mBMapManager = null;
	private MapView mMapView = null;
	private OfflineMapUtil mOfflineMapUtil = null;

	private Button city_scenelist_button,city_scenemap_button;
	private RelativeLayout city_scene_top_layout,city_scene_alldownload_layout,city_scene_download_listview;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.v("CityActivity", "onCreate");
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		initMap();

		//�й��ڼ�����ʾ���߼�����
		city_scene_top_layout = (RelativeLayout)findViewById(R.id.city_scene_top_layout);
		city_scene_alldownload_layout = (RelativeLayout)findViewById(R.id.city_scene_alldownload_layout);
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
				city_scene_alldownload_layout.setVisibility(View.VISIBLE);
				city_scene_download_listview.setVisibility(View.VISIBLE);
				
			}
		});
		city_scenemap_button = (Button)findViewById(R.id.city_scenemap_button);
		city_scenemap_button.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				city_scene_top_layout.getBackground().setAlpha(0);//��ȫ͸����
				city_scene_alldownload_layout.setVisibility(View.INVISIBLE);
				city_scene_download_listview.setVisibility(View.INVISIBLE);
				mMapView.setVisibility(View.VISIBLE);
				city_scenelist_button.setVisibility(View.VISIBLE);
				city_scenemap_button.setVisibility(View.VISIBLE);
			}
		});

	}


	void initMap()
	{
		mBMapManager=new BMapManager(getApplication());
		mBMapManager.init(null); 
		//ע�⣺��������setContentViewǰ��ʼ��BMapManager���󣬷���ᱨ��
		setContentView(R.layout.activity_city);
		mMapView=(MapView)findViewById(R.id.city_scenemap);
		//mMapView.setBuiltInZoomControls(true);//�����������õ����ſؼ�
		MapController mMapController=mMapView.getController();
		// �õ�mMapView�Ŀ���Ȩ,�����������ƺ�����ƽ�ƺ�����
		GeoPoint point = null;
		Intent  intent = getIntent();
		mOfflineMapUtil = new OfflineMapUtil(this,mMapView);
		if ( intent.hasExtra("cityName")  ){
			//����intent����ʱ���������ĵ�Ϊָ����
			Bundle b = intent.getExtras();
			Log.v("intent","intent");
			point = mOfflineMapUtil.searchGeoPoint(b.getString("cityName"));
		}else{
			//�������ĵ�Ϊ�찲��
			point = new GeoPoint((int)(39.915* 1E6),(int)(116.404* 1E6));
		}
		//�ø����ľ�γ�ȹ���һ��GeoPoint����λ��΢�� (�� * 1E6)
		mMapController.setCenter(point);//���õ�ͼ���ĵ�
		mMapController.setZoom(12);//���õ�ͼzoom����
		mMapController.setCompassMargin(90, 90);//����ָ����λ��
		
		
	}

	@Override
	protected void onStart(){

		Log.v("CityActivity", "onStart");
		super.onStart();
	}
	
	@Override
	protected void onDestroy(){
		Log.v("CityActivity", "onDestroy");
		mOfflineMapUtil.destroy();
		mMapView.destroy();
		if(mBMapManager!=null){
			mBMapManager.destroy();
			mBMapManager=null;
		}
		super.onDestroy();
	}
	@Override
	protected void onPause(){
		Log.v("CityActivity", "onPause");
		mMapView.onPause();
		if(mBMapManager!=null){
			mBMapManager.stop();
		}
		super.onPause();
	}
	@Override
	protected void onResume(){
		Log.v("CityActivity", "onResume");
		mMapView.onResume();
		if(mBMapManager!=null){
			mBMapManager.start();
		}
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

}
