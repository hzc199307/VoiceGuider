package com.ne.voiceguider.fragment;

import com.ne.voiceguider.R;
import com.ne.voiceguider.activity.CityActivity;
import com.ne.voiceguider.activity.HikingActivity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

	int mNum;  
	Context mContext ; 
	private RelativeLayout hiking_city,hiking_Location ;
	@Override  
	public void onCreate(Bundle savedInstanceState) {  
		super.onCreate(savedInstanceState);  
	}  


	@Override  
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  
			Bundle savedInstanceState) {   
		mContext = inflater.getContext();
		View view = inflater.inflate(R.layout.fragment_hiking, container, false);
		hiking_Location = (RelativeLayout)view.findViewById(R.id.hiking_Location);
		hiking_Location.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new Builder(mContext);
				builder.setMessage("ȷ��������");

				builder.setTitle("��ʾ");

				builder.setPositiveButton("ȷ��", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
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
		});
		hiking_city = (RelativeLayout)view.findViewById(R.id.hiking_city);
		hiking_city.setOnClickListener(new View.OnClickListener() {

			@Override //TODO
			public void onClick(View arg0) {
				Intent intent = new Intent(mContext,CityActivity.class); // ��ת�����о�������ҳ�� 
				Bundle bundle = new Bundle();                           //����Bundle����   
				bundle.putString("cityName", "������");     //װ������   
				intent.putExtras(bundle);                            //��Bundle����Intent����   
				startActivity(intent);                                     //��ʼ�л� 
			}
		});
		return view;  
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
		super.onDestroy();   
	}  

}
