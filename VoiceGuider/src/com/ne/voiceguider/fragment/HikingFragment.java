package com.ne.voiceguider.fragment;

import com.ne.voiceguider.R;
import com.ne.voiceguider.activity.CityActivity;
import com.ne.voiceguider.activity.HikingActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * 
 * @ClassName: CityFragment 
 * @Description: TODO 
 * @author HeZhichao
 * @date 2014年5月29日 下午3:41:46 
 *
 */
public class HikingFragment extends Fragment {

	int mNum;  
	Context mContext ; 
	private RelativeLayout hiking_city ;
	@Override  
	public void onCreate(Bundle savedInstanceState) {  
		super.onCreate(savedInstanceState);  
	}  


	@Override  
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  
			Bundle savedInstanceState) {   
		mContext = inflater.getContext();
		View view = inflater.inflate(R.layout.activity_hiking, container, false); 
		hiking_city = (RelativeLayout)view.findViewById(R.id.hiking_city);
		hiking_city.setOnClickListener(new View.OnClickListener() {

			@Override //TODO
			public void onClick(View arg0) {
				Intent intent = new Intent(mContext,CityActivity.class); // 跳转到城市景点详情页面 
				Bundle bundle = new Bundle();                           //创建Bundle对象   
				bundle.putString("cityName", "广州市");     //装入数据   
				intent.putExtras(bundle);                            //把Bundle塞入Intent里面   
				startActivity(intent);                                     //开始切换 
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
