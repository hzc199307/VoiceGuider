package com.ne.voiceguider.util;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.mapapi.map.Symbol;
import com.baidu.mapapi.map.TextItem;
import com.baidu.mapapi.map.TextOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.ne.voiceguider.R;
import com.ne.voiceguider.activity.CityActivity;
import com.ne.voiceguider.activity.GuiderActivity;
import com.ne.voiceguider.activity.HikingActivity;
import com.ne.voiceguider.bean.BigScene;
import com.ne.voiceguider.bean.CityBean;
import com.ne.voiceguider.bean.SmallScene;
import com.ne.voiceguider.dao.CitySceneDao;

/**
 * 
 * @ClassName: OverlayUtil 
 * @Description: TODO 
 * @author HeZhichao
 * @date 2014��5��23�� ����12:13:30 
 *
 */
public class OverlayUtil<Class> {

	private String TAG = "OverlayUtil";


	private List<Class> listObject;
	/**
	 * ��ͼ������
	 */
	private MyOverlay mOverlay = null;
	private PopupOverlay mButtonPop = null;
	private TextView popupText = null;
	private View viewCache = null;
	private View popupInfo = null;
	private View popupLeft = null;
	private View popupRight = null;
	private MapView.LayoutParams layoutParam = null;
	private OverlayItem mCurItem = null;
	private MapView mMapView = null;
	private MapController mMapController = null;
	private Context mContext = null;
	private ArrayList<MyTextItem> textItemList = null;
	private MyTextItem mCurTextItem = null;
	private TextOverlay mTextOverlay = null;

	public OverlayUtil(MapView mapView,Context context) {
		// TODO Auto-generated constructor stub
		this.mMapView = mapView;
		this.mContext = context;
		/**
		 * �����Զ���overlay
		 */
		mOverlay = new MyOverlay(mContext.getResources().getDrawable(R.drawable.city_scene_overlay_icon),mMapView);
		/**
		 * ��overlay �����MapView��
		 */
		mMapView.getOverlays().add(mOverlay);


		textItemList = new ArrayList<MyTextItem>();
		mTextOverlay = new TextOverlay(mMapView);
		mMapView.getOverlays().add(mTextOverlay);
		mMapView.refresh();
		mMapController =mMapView.getController();
		//PopupOverlay ֻ���ڵ�ͼ������һ��
		mButtonPop = new PopupOverlay(mMapView, new MyPopupClickListener()); 

		viewCache = LayoutInflater.from(mContext).inflate(R.layout.city_scene_overlay_layout, null);
		popupInfo = (View) viewCache.findViewById(R.id.popinfo);
		popupLeft = (View) viewCache.findViewById(R.id.popleft);
		popupRight = (View) viewCache.findViewById(R.id.popright);
		popupText =(TextView) viewCache.findViewById(R.id.textcache);

	}

	public void setListObject(List<Class> listObject)
	{
		this.listObject = listObject;

		int size = listObject.size();
		/**
		 * ׼��overlay ����
		 */
		for(int i=0;i<size;i++)
		{
			Class mObject = listObject.get(i);
			GeoPoint gp ;
			OverlayItem item;
			if(mObject instanceof BigScene)
			{
				BigScene mBigScene = (BigScene)mObject;
				Log.v(mBigScene.getLatitude()+"",mBigScene.getLongitude()+"");
				gp= new GeoPoint ((int)(mBigScene.getLatitude()*1E6),(int)(mBigScene.getLongitude()*1E6));
				item = new OverlayItem(gp,mBigScene.getBigSceneName(),"");

			}
			else
			{
				SmallScene mSmallScene = (SmallScene)mObject;
				Log.v(mSmallScene.getLatitude()+"",mSmallScene.getLongtitude()+"");
				gp= new GeoPoint ((int)(mSmallScene.getLatitude()*1E6),(int)(mSmallScene.getLongtitude()*1E6));
				item = new OverlayItem(gp,mSmallScene.getSmallSceneName(),"");
			}
			item.setMarker(mContext.getResources().getDrawable(R.drawable.city_scene_overlay_icon));
			addItem(item);
		}

		/**
		 * ����overlayͼ�꣬�粻���ã���ʹ�ô���ItemizedOverlayʱ��Ĭ��ͼ��.
		 */

	}
	/*
	 * ��ת���󾰵��µ�С��������ҳ�� 
	 */
	public void startActivity(int index){
		// TODO Auto-generated constructor stub
		Intent intent = new Intent(mContext,GuiderActivity.class); // ��ת���󾰵��µ�С��������ҳ�� 
		Bundle bundle = new Bundle();                       //����Bundle���� 
		BigScene mBigScene = (BigScene)(listObject.get(index));
		bundle.putString("bigSceneName", mBigScene.getBigSceneName());            //װ������   
		intent.putExtras(bundle);                            //��Bundle����Intent����   
		mContext.startActivity(intent);   
	}

	public void addItem(OverlayItem item)
	{
		/**
		 * ��item ��ӵ�overlay��
		 * ע�⣺ ͬһ��itemֻ��addһ��
		 */
		mOverlay.addItem(item);
		MyTextItem mTextItem = new MyTextItem(item.getTitle(),item.getPoint());
		mTextOverlay.addText(mTextItem);
	}

	//��ʾ���е�
	public void showAll() {
		mMapView.refresh();
	}
	//���ŵ�ͼ��������ָ���ľ�γ�ȷ�Χ ����ָ�����е������λ��
	public void showSpan()
	{
		Log.v(TAG, mOverlay.getLatSpanE6()+" "+mOverlay.getLonSpanE6());
		mMapController.setCenter(mOverlay.getCenter());//���õ�ͼ���ĵ�
		//mMapController.zoomToSpan(mOverlay.getLatSpanE6(), mOverlay.getLonSpanE6());//�ı��ͼ��Χ
		mMapController.zoomToSpanWithAnimation(mOverlay.getLatSpanE6(), mOverlay.getLonSpanE6(),1500);//�ı��ͼ��Χ
	}

	public class MyTextItem extends TextItem
	{
		private Symbol textSymbol = new Symbol(); 
		public MyTextItem(String text,GeoPoint pt) {

			Symbol.Color textColor = textSymbol.new Color();  
			textColor.alpha = 255;  
			textColor.red = 0;  
			textColor.blue = 255;  
			textColor.green = 0;  

			Symbol.Color textColor1 = textSymbol.new Color();  
			textColor1.alpha = 10;  
			textColor1.red = 80;  
			textColor1.blue = 80;  
			textColor1.green = 80;  

			this.fontColor = textColor;
			this.bgColor = textColor1;
			this.fontSize = 30;
			this.text = text;
			this.pt = pt;
		}
	}
	public class MyPopupClickListener implements PopupClickListener
	{
		@Override
		public void onClickedPopup(int index) {
			Log.v(TAG, "onClickedPopup startActivity");
			startActivity(index);// TODO Auto-generated method stub
			if ( index == 0){
			}
			else if(index == 1){
				//startActivity();
			}
		}
	}
	public class MyOverlay extends ItemizedOverlay{

		public MyOverlay(Drawable defaultMarker, MapView mapView) {
			super(defaultMarker, mapView);
		}

		private boolean isPopShowed = false;
		@Override
		public boolean onTap(int index){
			mCurItem = getItem(index);
			popupText.setText(getItem(index).getTitle());
			if(isPopShowed==false)//��ʾ�а�ť��pop
			{
				isPopShowed=true;
				Bitmap[] bitMaps={
						BMapUtil.getBitmapFromView(popupLeft), 		
						BMapUtil.getBitmapFromView(popupInfo), 		
						BMapUtil.getBitmapFromView(popupRight)		
				};
				mButtonPop.showPopup(bitMaps[1],mCurItem.getPoint(),32);
				Log.v("OverlayUtil", "onTap ��ʾ��תpop �رվ�������text");


			}
			else
			{
				isPopShowed=false;
				mButtonPop.hidePop();
				Log.v("OverlayUtil", "onTap ��ʾ��������text �ر���תpop");
			}
			return true;
		}

		//�����overlay�ĵ���¼�
		@Override
		public boolean onTap(GeoPoint pt , MapView mMapView){
			if (mButtonPop != null){
				mButtonPop.hidePop();
			}
			return false;
		}

	}
}
