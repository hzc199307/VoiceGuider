package com.ne.voiceguider.fragment;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.ne.voiceguider.MainActivity;
import com.ne.voiceguider.R;
import com.ne.voiceguider.activity.CityActivity;
import com.ne.voiceguider.adapter.BigSceneListAdapter;
import com.ne.voiceguider.adapter.CityBeanListAdapter;
import com.ne.voiceguider.bean.CityBean;
import com.ne.voiceguider.util.BaiduLocationUtil;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

/**
 * 
 * @ClassName: HikingFragment 
 * @Description: TODO 
 * @author HeZhichao
 * @date 2014��5��29�� ����3:41:46 
 *
 */
public class HikingFragment extends Fragment {

	private final String TAG = "HikingFragment";
	int mNum;  
	private Context mContext ; 
	private LinearLayout hiking_Location ;
	private ListView citybean_listview = null;
	private CityBeanListAdapter mCityBeanListAdapter = null;
	
	private BaiduLocationUtil mLocationUtil;
	private MyBDLocationListenner mMyBDLocationListenner;
	private AlertDialog.Builder builder;
	@Override  
	public void onCreate(Bundle savedInstanceState) {  
		super.onCreate(savedInstanceState);  
		
	}  


	@Override  
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  
			Bundle savedInstanceState) {  
		Log.v(TAG, "onCreateView");
		mContext = inflater.getContext();
		View view = inflater.inflate(R.layout.fragment_hiking, container, false);

		citybean_listview = (ListView)view.findViewById(R.id.citybean_listview);
		mCityBeanListAdapter = new CityBeanListAdapter(mContext);
		citybean_listview.setAdapter(mCityBeanListAdapter);
		Log.v(TAG, "citybean_listview");

		citybean_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				CityBean mCityBean = mCityBeanListAdapter.getItem(position);
				Intent intent = new Intent(mContext,CityActivity.class); // ��ת�����о�������ҳ�� 
				Bundle bundle = new Bundle();                           //����Bundle����   
				bundle.putString("cityName", mCityBean.getCityName());     //װ������  
				bundle.putInt("cityID", mCityBean.getCityID());
				bundle.putString("cityPinyin", mCityBean.getCityPinyin());
				intent.putExtras(bundle);                            //��Bundle����Intent����   
				startActivity(intent);                                     //��ʼ�л� 
			}
		});

		hiking_Location = (LinearLayout)view.findViewById(R.id.hiking_Location);
		hiking_Location.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mLocationUtil.requestLocation();
				
			}
		});
		mMyBDLocationListenner = new MyBDLocationListenner();
		mLocationUtil = new BaiduLocationUtil(mContext, mMyBDLocationListenner);
		return view;  
	}  

	/**
	 * ��λSDK��������
	 */
	public class MyBDLocationListenner implements BDLocationListener {

		private String cityName;
		private CityBean mCityBean;
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return ;
			mCityBean = mCityBeanListAdapter.getNearestCity(location.getLatitude(),location.getLongitude());
//			cityName = location.getCity();
			Log.v(TAG, "city:"+location.getLatitude());
//			Log.v(TAG, "city:"+location.getCity());
			builder = new Builder(mContext);
			builder.setMessage("ȷ��������");
			builder.setTitle("����ĳ���  "+mCityBean.getCityName());
			builder.setPositiveButton("ȷ��", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(mContext,CityActivity.class); // ��ת�����о�������ҳ�� 
					Bundle bundle = new Bundle();                           //����Bundle����   
					bundle.putString("cityName", mCityBean.getCityName());     //װ������  
					bundle.putInt("cityID", mCityBean.getCityID());
					intent.putExtras(bundle);                               //��Bundle����Intent����   
					startActivity(intent);                                     //��ʼ�л� 
					dialog.dismiss();

				}
			});
			builder.setNegativeButton("ȡ��", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			builder.create().show();
		}

		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null){
				return ;
			}
		}
	}

	@Override  
	public void onActivityCreated(Bundle savedInstanceState) {  
		super.onActivityCreated(savedInstanceState);     
	}  


	@Override  
	public void onDestroyView(){  
		System.out.println(mNum + "mNumDestory");  
		super.onDestroyView();  
	}  

	@Override  
	public void onDestroy(){  
		mLocationUtil.stop();
		mLocationUtil=null;
		super.onDestroy();   
	}  

}
