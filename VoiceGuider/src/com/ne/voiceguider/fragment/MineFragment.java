package com.ne.voiceguider.fragment;

import com.ne.voiceguider.MainActivity;
import com.ne.voiceguider.R;
import com.ne.voiceguider.activity.CityActivity;
import com.ne.voiceguider.activity.GuiderActivity;
import com.ne.voiceguider.adapter.BigSceneHadListAdapter;
import com.ne.voiceguider.adapter.CityHadListAdapter;
import com.ne.voiceguider.bean.BigScene;
import com.ne.voiceguider.bean.CityBean;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MineFragment extends Fragment{

	private final String TAG = "MineFragment";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		MainActivity.whichFragment = 10;
		View view = inflater.inflate(R.layout.fragment_mine, container, false);
		getFragmentManager().beginTransaction()
		.add(R.id.mine_fragment, new FirstFragment()).commit();
		return view;
	}

	public static class FirstFragment extends Fragment {

		private final String TAG = "FirstFragment";
		private Button mine_head_edit;
		private ImageButton mine_head_back;
		private TextView mine_head_text;
		
		public FirstFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_mine_first, container,
					false);
			ImageView mine_downloaded_button = (ImageView)rootView.findViewById(R.id.mine_downloaded_button);
			mine_downloaded_button.setOnClickListener(new ImageView.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					SecondFragment mSecondFragment = new SecondFragment();  
					FragmentTransaction transaction = getFragmentManager().beginTransaction();    
					transaction.replace(R.id.mine_fragment, mSecondFragment);  
					transaction.addToBackStack(null);
					transaction.commit();
				}
			});
			mine_head_back = (ImageButton)((Activity) inflater.getContext()).findViewById(R.id.mine_head_back);
			mine_head_edit = (Button)((Activity) inflater.getContext()).findViewById(R.id.mine_head_edit);
			mine_head_back.setVisibility(View.GONE);
			mine_head_edit.setVisibility(View.GONE);
			mine_head_text = (TextView)((Activity) inflater.getContext()).findViewById(R.id.mine_head_text);
			return rootView;
		}
		@Override
		public void onResume() {
			super.onResume();
			mine_head_text.setText("我的旅程");
			MainActivity.whichFragment = 11;
			Log.v(TAG,"onResume");
		}
	}

	public static class SecondFragment extends Fragment {

		private TextView mine_head_text;
		private Button mine_head_edit;
		private ImageButton mine_head_back;
		private ListView mine_city_listview;
		private CityHadListAdapter mCityHadListAdapter ;
		public SecondFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			MainActivity.whichFragment = 12;
			View rootView = inflater.inflate(R.layout.fragment_mine_second, container,
					false);
			mine_city_listview = (ListView)rootView.findViewById(R.id.mine_cityhad_listview);
			mCityHadListAdapter = new CityHadListAdapter(inflater.getContext());
			mine_city_listview.setAdapter(mCityHadListAdapter);
			mine_city_listview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					// TODO Auto-generated method stub

					CityBean mCityBean = mCityHadListAdapter.getItem(position);
					Bundle bundle = new Bundle();                           //创建Bundle对象   
					bundle.putString("cityName", mCityBean.getCityName());     //装入数据  
					bundle.putInt("cityID", mCityBean.getCityID());
					ThirdFragment mThirdFragment = new ThirdFragment();  
					mThirdFragment.setArguments(bundle);//把Bundle塞入mThirdFragment
					FragmentTransaction transaction = getFragmentManager().beginTransaction();    
					transaction.replace(R.id.mine_fragment, mThirdFragment);  
					transaction.addToBackStack(null);
					transaction.commit(); 
				}
			});
			mine_head_back = (ImageButton)((Activity) inflater.getContext()).findViewById(R.id.mine_head_back);
			mine_head_edit = (Button)((Activity) inflater.getContext()).findViewById(R.id.mine_head_edit);
			mine_head_back.setVisibility(View.VISIBLE);
			mine_head_edit.setVisibility(View.VISIBLE);
			mine_head_back.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					getFragmentManager().popBackStack();
					

				}
			});
			mine_head_text = (TextView)((Activity) inflater.getContext()).findViewById(R.id.mine_head_text);
			return rootView;
		}
		@Override
		public void onResume() {
			super.onResume();
			mine_head_text.setText("已下载");
			MainActivity.whichFragment = 12;
		}
		
		public static class ThirdFragment extends Fragment {

			private final String TAG = "ThirdFragment";
			private Button mine_head_edit;
			private ListView mine_bigscene_listview;
			private BigSceneHadListAdapter mBigSceneHadListAdapter ;
			private TextView citymaphad_name_textview;
			public ThirdFragment() {
			}

			@Override
			public View onCreateView(LayoutInflater inflater, ViewGroup container,
					Bundle savedInstanceState) {
				MainActivity.whichFragment = 13;
				View rootView = inflater.inflate(R.layout.fragment_mine_third, container,
						false);
				int cityID = getArguments().getInt("cityID");
				String cityName = getArguments().getString("cityName");
				mine_bigscene_listview = (ListView)rootView.findViewById(R.id.mine_bigscenehad_listview);
				mBigSceneHadListAdapter = new BigSceneHadListAdapter(inflater.getContext(),cityID);
				mine_bigscene_listview.setAdapter(mBigSceneHadListAdapter);
				
				mine_head_edit = (Button)((Activity) inflater.getContext()).findViewById(R.id.mine_head_edit);
				mine_head_edit.setVisibility(View.VISIBLE);
				citymaphad_name_textview = (TextView)rootView.findViewById(R.id.citymaphad_name_textview);
				citymaphad_name_textview.setText(cityName);
				return rootView;
			}
			@Override
			public void onResume() {
				super.onResume();
				MainActivity.whichFragment = 13;
			}
			@Override
			public void onDestroyView() {
				// TODO Auto-generated method stub
				super.onDestroyView();
				Log.v(TAG,"onDestroyView");
			}
		}
	}
}
