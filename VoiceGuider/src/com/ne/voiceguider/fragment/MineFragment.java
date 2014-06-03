package com.ne.voiceguider.fragment;

import com.ne.voiceguider.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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

		public SecondFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_mine_second, container,
					false);
			return rootView;
		}
	}
}
