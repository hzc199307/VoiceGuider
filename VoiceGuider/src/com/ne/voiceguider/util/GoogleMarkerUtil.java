package com.ne.voiceguider.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.LatLngBounds.Builder;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ne.voiceguider.R;
import com.ne.voiceguider.bean.BigScene;

/**
 * �ȸ��ͼmarker��ǹ��� 
 * @ClassName: GoogleMarkerUtil 
 * @Description: TODO 
 * @author HeZhichao
 * @date 2014��6��23�� ����4:37:45 
 *
 */
public class GoogleMarkerUtil {

	private Context mContext;
	private List<BigScene> listObject;
	private List<MarkerOptions> listMarkerOptions;//marker
	private List<LatLng> listLatLngs;//����
	private GoogleMap mGoogleMap;

	public GoogleMarkerUtil(Context mContext,GoogleMap mGoogleMap) {
		this.mContext = mContext;
		this.mGoogleMap = mGoogleMap;
		mGoogleMap.setInfoWindowAdapter(new MyInfoWindowAdapter());
	}


	public void setListObject(List<BigScene> listObject)
	{
		this.listObject = listObject;
		int size = listObject.size();

		if(listMarkerOptions==null)
			listMarkerOptions = new ArrayList<MarkerOptions>();
		else
			listMarkerOptions.clear();
		
		if(listLatLngs==null)
			listLatLngs = new ArrayList<LatLng>();
		else
			listLatLngs.clear();
		
		for(int i=0;i<size;i++)
		{
			LatLng ll = new LatLng(listObject.get(i).getLatitude(), listObject.get(i).getLongitude());
			listLatLngs.add(ll);
			int iconID = mContext.getResources().getIdentifier("bigscene_"+listObject.get(i).getBigScenePinyin() ,"drawable","com.ne.voiceguider");
			MarkerOptions mo = new MarkerOptions()
					.position(ll)
					.title(listObject.get(i).getBigSceneName())
					.snippet("")
					.icon(BitmapDescriptorFactory.fromResource(iconID));
			listMarkerOptions.add(mo);
		}
	}
	/**
	 * ��ʾ���е�marker
	 * @Title: showAll 
	 * @Description: TODO
	 * @author HeZhichao
	 * @date 2014��6��23�� ����2:26:24 
	 * @param 
	 * @return void 
	 * @throws
	 */
	public void showAll()
	{
		int size = listMarkerOptions.size();
		for(int i=0;i<size;i++)
		{
			mGoogleMap.addMarker(listMarkerOptions.get(i));
		}
	}

	/**
	 * ��ʾ�պÿ�ס���е�marker����ͼ
	 * @Title: showSpan 
	 * @Description: TODO
	 * @author HeZhichao
	 * @date 2014��6��23�� ����2:27:23 
	 * @param 
	 * @return void 
	 * @throws
	 */
	public void showSpan()
	{
		Builder builder = new LatLngBounds.Builder();
		int size = listLatLngs.size();
		for(int i=0;i<size;i++)
		{
			builder.include(listLatLngs.get(i));
		}
		LatLngBounds bounds = builder.build();
		mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
	}
	
	/*
	 * marker������view
	 */
	class MyInfoWindowAdapter implements InfoWindowAdapter
	{

		private View infoView;
		public MyInfoWindowAdapter() {
			infoView = LayoutInflater.from(mContext).inflate(R.layout.use_popinfowindow, null);
		}
		
		@Override
		public View getInfoContents(Marker mMarker) {//��������� �˺���ִ�����ִ�� getInfoWindow
			return null;
		}

		@Override
		public View getInfoWindow(Marker mMarker) {//����Ǳ���
			TextView textcache = ((TextView) infoView.findViewById(R.id.textcache));
			textcache.setText(mMarker.getTitle());
			return infoView;
		}
		
	}
}
