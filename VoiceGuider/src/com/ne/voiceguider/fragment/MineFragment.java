package com.ne.voiceguider.fragment;

import com.ne.voiceguider.R;
import com.ne.voiceguider.activity.CityActivity;
import com.ne.voiceguider.activity.GuiderActivity;
import com.ne.voiceguider.adapter.BigSceneHadListAdapter;
import com.ne.voiceguider.adapter.CityHadListAdapter;
import com.ne.voiceguider.bean.BigScene;
import com.ne.voiceguider.bean.CityBean;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

public class MineFragment extends Fragment{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_mine, container, false);
		getFragmentManager().beginTransaction()
		.add(R.id.mine_fragment, new FirstFragment()).commit();


		return view;
	}

	public static class FirstFragment extends Fragment {

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
			return rootView;
		}
	}

	public static class SecondFragment extends Fragment {

		private ListView mine_city_listview;
		private CityHadListAdapter mCityHadListAdapter ;
		public SecondFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_mine_second, container,
					false);
			mine_city_listview = (ListView)rootView.findViewById(R.id.mine_city_listview);
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
			return rootView;
		}
		
		public static class ThirdFragment extends Fragment {

			private ListView mine_bigscene_listview;
			private BigSceneHadListAdapter mBigSceneHadListAdapter ;
			public ThirdFragment() {
			}

			@Override
			public View onCreateView(LayoutInflater inflater, ViewGroup container,
					Bundle savedInstanceState) {
				View rootView = inflater.inflate(R.layout.fragment_mine_third, container,
						false);
				int cityID = getArguments().getInt("cityID");
				mine_bigscene_listview = (ListView)rootView.findViewById(R.id.mine_bigscene_listview);
				mBigSceneHadListAdapter = new BigSceneHadListAdapter(inflater.getContext(),cityID);
				mine_bigscene_listview.setAdapter(mBigSceneHadListAdapter);
				return rootView;
			}
		}
	}
}
