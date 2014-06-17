package com.ne.voiceguider.fragment;

import com.ne.voiceguider.FirstActivity;
import com.ne.voiceguider.MainActivity;
import com.ne.voiceguider.R;
import com.ne.voiceguider.activity.GoogleMapActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MoreFragment extends Fragment {

	private final String TAG = "MoreFragment";
	private Context mContext;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = inflater.getContext();
		View view = inflater.inflate(R.layout.fragment_more, container, false);
		Button googleMapButton = (Button)view.findViewById(R.id.googleMapButton);
		googleMapButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(mContext,GoogleMapActivity.class));
			}
		});
		return view;
	}
}
