package com.ne.voiceguider.util;

import java.util.ArrayList;

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

/**
 * 
 * @ClassName: OverlayUtil 
 * @Description: TODO 
 * @author HeZhichao
 * @date 2014年5月23日 下午12:13:30 
 *
 */
public class OverlayUtil {

	private String TAG = "OverlayUtil";

	/**
	 * 地图上面插标
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
		 * 创建自定义overlay
		 */
		mOverlay = new MyOverlay(mContext.getResources().getDrawable(R.drawable.city_scene_overlay_icon),mMapView);
		/**
		 * 将overlay 添加至MapView中
		 */
		mMapView.getOverlays().add(mOverlay);
		

		textItemList = new ArrayList<MyTextItem>();
		mTextOverlay = new TextOverlay(mMapView);
		mMapView.getOverlays().add(mTextOverlay);
		mMapView.refresh();
		mMapController =mMapView.getController();
		//PopupOverlay 只能在地图上面标记一处
		mButtonPop = new PopupOverlay(mMapView, new MyPopupClickListener()); 

		viewCache = LayoutInflater.from(mContext).inflate(R.layout.city_scene_overlay_layout, null);
		popupInfo = (View) viewCache.findViewById(R.id.popinfo);
		popupLeft = (View) viewCache.findViewById(R.id.popleft);
		popupRight = (View) viewCache.findViewById(R.id.popright);
		popupText =(TextView) viewCache.findViewById(R.id.textcache);

	}
	/*
	 * 跳转到大景点下的小景点详情页面 
	 */
	public void startActivity(){
		// TODO Auto-generated constructor stub
		Intent intent = new Intent(mContext,GuiderActivity.class); // 跳转到大景点下的小景点详情页面 
		Bundle bundle = new Bundle();                            //创建Bundle对象   
		bundle.putString("bigSceneName", "越秀公园");            //装入数据   
		intent.putExtras(bundle);                            //把Bundle塞入Intent里面   
		mContext.startActivity(intent);   
	}

	public void addItem(OverlayItem item)
	{
		/**
		 * 将item 添加到overlay中
		 * 注意： 同一个item只能add一次
		 */
		mOverlay.addItem(item);
		MyTextItem mTextItem = new MyTextItem(item.getTitle(),item.getPoint());
		mTextOverlay.addText(mTextItem);
	}

	//显示所有的
	public void showAll() {
		mMapView.refresh();
	}
	//缩放地图到能容下指定的经纬度范围 并且指向所有点的中心位置
	public void showSpan()
	{
		Log.v(TAG, mOverlay.getLatSpanE6()+" "+mOverlay.getLonSpanE6());
		mMapController.setCenter(mOverlay.getCenter());//设置地图中心点
		mMapController.zoomToSpan(mOverlay.getLatSpanE6(), mOverlay.getLonSpanE6());//改变地图范围
		//mMapController.zoomToSpanWithAnimation(mOverlay.getLatSpanE6(), mOverlay.getLonSpanE6(),0);//改变地图范围
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
			startActivity();// TODO Auto-generated method stub
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
			if(isPopShowed==false)//显示有按钮的pop
			{
				isPopShowed=true;
				Bitmap[] bitMaps={
						BMapUtil.getBitmapFromView(popupLeft), 		
						BMapUtil.getBitmapFromView(popupInfo), 		
						BMapUtil.getBitmapFromView(popupRight)		
				};
				mButtonPop.showPopup(bitMaps[1],mCurItem.getPoint(),32);
				Log.v("OverlayUtil", "onTap 显示跳转pop 关闭景点名称text");
				

			}
			else
			{
				isPopShowed=false;
				mButtonPop.hidePop();
				Log.v("OverlayUtil", "onTap 显示景点名称text 关闭跳转pop");
			}
			return true;
		}

		//处理非overlay的点击事件
		@Override
		public boolean onTap(GeoPoint pt , MapView mMapView){
			if (mButtonPop != null){
				mButtonPop.hidePop();
			}
			return false;
		}

	}
}
