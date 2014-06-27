package com.ne.voiceguider.util;

import java.util.List;

import android.content.Context;

import com.baidu.mapapi.map.Geometry;
import com.baidu.mapapi.map.Graphic;
import com.baidu.mapapi.map.GraphicsOverlay;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Symbol;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.ne.voiceguider.bean.SmallScene;


/**
 * 基于百度地图2.4.1的 自定义折线图工具类
 * @ClassName: PolylineUtil 
 * @Description: TODO 
 * @author HeZhichao
 * @date 2014年6月26日 上午11:30:00 
 *
 */
public class BaiduPolylineUtil {

	private List<SmallScene> listSmallScenes;
	private GraphicsOverlay graphicsOverlay;
	private MapView mMapView;
	private GeoPoint[]  geoPoints = null;
	public BaiduPolylineUtil(Context mContext,MapView mMapView) {
		this.mMapView = mMapView;
		graphicsOverlay = new GraphicsOverlay(mMapView);
		mMapView.refresh();
	}
	
	public void setListData(List<SmallScene> mlistSmallScenes)
	{
		this.listSmallScenes = mlistSmallScenes;
		int size = listSmallScenes.size();
		geoPoints = null;
		geoPoints = new GeoPoint[size];
		for(int i=0;i<size;i++)
		{
			geoPoints[i] = listSmallScenes.get(i).getGeoPoint();
		}
		graphicsOverlay.setData(drawLine());
	}
	
	/**
     * 绘制折线 TODO 样式需要调整
     * @return 折线对象
     */
    private Graphic drawLine(){
	  
	    //构建线
  		Geometry lineGeometry = new Geometry();

  		lineGeometry.setPolyLine(geoPoints);
  		//设定样式
  		Symbol lineSymbol = new Symbol();
  		Symbol.Color lineColor = lineSymbol.new Color();
  		lineColor.red = 255;
  		lineColor.green = 0;
  		lineColor.blue = 0;
  		lineColor.alpha = 255;
  		lineSymbol.setLineSymbol(lineColor, 10);
  		//生成Graphic对象
  		Graphic lineGraphic = new Graphic(lineGeometry, lineSymbol);
  		return lineGraphic;
    }
	
	public void showAll()
	{
		mMapView.getOverlays().add(graphicsOverlay);
		mMapView.refresh();
	}
	
	public void removeAll()
	{
		mMapView.getOverlays().remove(graphicsOverlay);
		mMapView.refresh();
	}
}

