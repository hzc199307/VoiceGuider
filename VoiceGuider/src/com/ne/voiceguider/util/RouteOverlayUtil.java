package com.ne.voiceguider.util;

import java.util.List;

import android.app.Activity;
import android.content.Context;

import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.RouteOverlay;
import com.baidu.mapapi.search.MKRoute;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.ne.voiceguider.bean.SmallScene;

/**
 * baidu地图的路线工具
 * @ClassName: RouteOverlayUtil 
 * @Description: TODO 
 * @author HeZhichao
 * @date 2014年6月26日 上午9:31:13 
 *
 */
public class RouteOverlayUtil {

	private List<SmallScene> listSmallScenes;
	private GeoPoint[] listGeoPoints;
	private RouteOverlay routeOverlay;
	private MapView mMapView;
	public RouteOverlayUtil(Context mContext,MapView mMapView) {
		routeOverlay = new RouteOverlay((Activity) mContext, mMapView);	
		this.mMapView = mMapView;
		mMapView.refresh();
	}
	
	public void setListData(List<SmallScene> mlistSmallScenes)
	{
		this.listSmallScenes = mlistSmallScenes;
		int size = listSmallScenes.size();
		listGeoPoints = new GeoPoint[size];
		for(int i=0;i<size;i++)
		{
			listGeoPoints[i] = listSmallScenes.get(i).getGeoPoint();
		}
		
		MKRoute route = new MKRoute();
		route.customizeRoute(listGeoPoints[0], listGeoPoints[size-1], listGeoPoints);	
		routeOverlay.setData(route);
	}
	
	public void showAll()
	{
		mMapView.getOverlays().add(routeOverlay);
		mMapView.refresh();
	}
	
	public void removeAll()
	{
		mMapView.getOverlays().remove(routeOverlay);
		mMapView.refresh();
	}
}
