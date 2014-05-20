package com.ne.voiceguider.util;

import java.util.ArrayList;

import android.content.Context;
import android.widget.Toast;

import com.baidu.mapapi.map.MKOLSearchRecord;
import com.baidu.mapapi.map.MKOLUpdateElement;
import com.baidu.mapapi.map.MKOfflineMap;
import com.baidu.mapapi.map.MKOfflineMapListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.platform.comapi.basestruct.GeoPoint;

/**
 * ���ߵ�ͼ������
 * @ClassName: OfflineMapUtil 
 * @Description: TODO 
 * @author HeZhichao
 * @date 2014��5��20�� ����4:39:17 
 *
 */
public class OfflineMapUtil {

	private MapView mMapView = null;
	private MKOfflineMap mOffline = null;
	private Context mContext = null;
	public MKOfflineMap getmOffline() {
		return mOffline;
	}
	private MapController mMapController = null;
	
	public OfflineMapUtil(Context mContext,MapView mMapView) {
		// TODO Auto-generated constructor stub
		this.mMapView = mMapView;
		this.mContext = mContext;
		mOffline = new MKOfflineMap();  
        /**
         * ��ʼ�����ߵ�ͼģ��,MapControler�ɴ�MapView.getController()��ȡ
         */
		MyMKOfflineMapListener mMyMKOfflineMapListener = new MyMKOfflineMapListener();
        mOffline.init(mMapView.getController(), null);
	}
	/**
	 * ���������ص����ߵ�ͼ��Ϣ�б�
	 */
	public ArrayList<MKOLUpdateElement> getAllUpdateInfo()
	{
		ArrayList<MKOLUpdateElement> localMapList = mOffline.getAllUpdateInfo();
	    if ( localMapList == null ){
	        localMapList = new ArrayList<MKOLUpdateElement>();	
	    }
	    return localMapList;
	}
	
	/**
	 * ���������ص����ߵ�ͼ��Ϣ�б�
	 */
	public GeoPoint searchGeoPoint(String cityName){
		ArrayList<MKOLUpdateElement> records = getAllUpdateInfo();
		for(int i=0;i<records.size();i++)
		{
			MKOLUpdateElement e = records.get(i);
			if(cityName.equals(e.cityName))
				return e.geoPt;
		}
		return null;
	}
	public void destroy()
	{
		mOffline.destroy();
	}
	
	/**
	 * �������߳���
	 * @param view
	 */
	public int search(String cityName){
		ArrayList<MKOLSearchRecord> records = mOffline.searchCity(cityName);
		if (records == null || records.size() != 1)
			return -1;
		return records.get(0).cityID;
	}
	
	/**
	 * ��ʼ(����)����
	 * @param view
	 */
	public void start(int cityID){
		if(cityID>0)
			mOffline.start(cityID);
		else
			return ;
		Toast.makeText( mContext,"��ʼ�������ߵ�ͼ. cityid: "+cityID, Toast.LENGTH_SHORT)
		          .show();
	}
	/**
	 * ��ͣ����
	 * @param view
	 */
	public void stop(int cityID){
		mOffline.pause(cityID);
		Toast.makeText(mContext, "��ͣ�������ߵ�ͼ. cityid: "+cityID, Toast.LENGTH_SHORT)
		          .show();
	}
	/**
	 * ɾ�����ߵ�ͼ
	 * @param view
	 */
	public void remove(int cityID){
		mOffline.remove(cityID);
		Toast.makeText(mContext, "ɾ�����ߵ�ͼ. cityid: "+cityID, Toast.LENGTH_SHORT)
		          .show();
	}
	/**
	 * ��SD���������ߵ�ͼ��װ��
	 * @param view
	 */
    public void importFromSDCard(){
		int num = mOffline.scan();
		String msg = "";
		if ( num == 0){
			msg = "û�е������߰�������������߰�����λ�ò���ȷ�������߰��Ѿ������";
		}
		else{
		   msg = String.format("�ɹ����� %d �����߰������������ع���鿴",num);	
		}
		Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
	}

	public class MyMKOfflineMapListener implements MKOfflineMapListener{
		@Override
		public void onGetOfflineMapState(int type, int state) {
//			switch (type) {
//			case MKOfflineMap.TYPE_DOWNLOAD_UPDATE:
//				{
//					MKOLUpdateElement update = mOffline.getUpdateInfo(state);
//					//�������ؽ��ȸ�����ʾ
//					if ( update != null ){
//					    stateView.setText(String.format("%s : %d%%", update.cityName, update.ratio));
//					    updateView();
//					}
//				}
//				break;
//			case MKOfflineMap.TYPE_NEW_OFFLINE:
//				//�������ߵ�ͼ��װ
//				Log.d("OfflineDemo", String.format("add offlinemap num:%d", state));
//				break;
//			case MKOfflineMap.TYPE_VER_UPDATE:
//			    // �汾������ʾ
//	            //	MKOLUpdateElement e = mOffline.getUpdateInfo(state);
//				
//				break;
//			}
			 
		}
	}
}
