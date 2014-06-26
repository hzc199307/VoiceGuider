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
 * ���ڰٶȵ�ͼ2.4.1�� �Զ�������ͼ������
 * @ClassName: PolylineUtil 
 * @Description: TODO 
 * @author HeZhichao
 * @date 2014��6��26�� ����11:30:00 
 *
 */
public class PolylineUtil {

	private List<SmallScene> listSmallScenes;
	private GraphicsOverlay graphicsOverlay;
	private MapView mMapView;
	private GeoPoint[]  geoPoints = null;
	public PolylineUtil(Context mContext,MapView mMapView) {
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
     * �������� TODO ��ʽ��Ҫ����
     * @return ���߶���
     */
    private Graphic drawLine(){
	  
	    //������
  		Geometry lineGeometry = new Geometry();

  		lineGeometry.setPolyLine(geoPoints);
  		//�趨��ʽ
  		Symbol lineSymbol = new Symbol();
  		Symbol.Color lineColor = lineSymbol.new Color();
  		lineColor.red = 255;
  		lineColor.green = 0;
  		lineColor.blue = 0;
  		lineColor.alpha = 255;
  		lineSymbol.setLineSymbol(lineColor, 10);
  		//����Graphic����
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

