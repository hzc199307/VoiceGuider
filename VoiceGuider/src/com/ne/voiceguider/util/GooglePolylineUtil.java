package com.ne.voiceguider.util;

import java.util.List;

import android.graphics.Color;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.ne.voiceguider.bean.Bean;


/**
 * google地图 路线描绘工具类
 * @ClassName: GooglePolylineUtil 
 * @Description: TODO 
 * @author HeZhichao
 * @date 2014年6月27日 上午10:56:10 
 * 
 * @param <MyClass>
 */
public class GooglePolylineUtil<MyClass> {

	private PolylineOptions options;
	private Polyline mPolyline;
	private GoogleMap mMap;
	
	public GooglePolylineUtil(GoogleMap mMap) {
		this.mMap = mMap;
	}

	public void setListData(List<MyClass> mListBean)
	{
		int size = mListBean.size();
		options=null;
		options = new PolylineOptions();
		for(int i=0;i<size;i++)
		{
			options.add(((Bean)(mListBean.get(i))).getLatLng());
		}
		
		int color = Color.HSVToColor(255, new float[] {255, 1, 1});//红绿蓝
		options.color(color).width(5);
	}

	public void showAll() 
	{
		mPolyline = mMap.addPolyline(options);
	}

	public void removeAll() 
	{
		mPolyline.setVisible(false);
	}
}
