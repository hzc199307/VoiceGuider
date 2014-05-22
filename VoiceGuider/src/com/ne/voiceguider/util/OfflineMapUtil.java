package com.ne.voiceguider.util;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
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

	private MKOfflineMap mOffline = null;
	private Context mContext = null;
	public MKOfflineMap getmOffline() {
		return mOffline;
	}
	private MapController mMapController = null;

	public OfflineMapUtil(Context mContext,MapView mMapView,MKOfflineMapListener mMyMKOfflineMapListener) {
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
		mOffline = new MKOfflineMap();  
		/**
		 * ��ʼ�����ߵ�ͼģ��,MapControler�ɴ�MapView.getController()��ȡ
		 */
		mOffline.init(mMapView.getController(), mMyMKOfflineMapListener);
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
	 * ����ĳһ�����ڸ��µ����ߵ�ͼ��Ϣ
	 */
	public MKOLUpdateElement getUpdateInfo(int state)
	{
		return mOffline.getUpdateInfo(state);
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
		if(mOffline!=null)
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
	public boolean start(int cityID){
		MKOLUpdateElement mMKOLUpdateElement;
		boolean isStart = false;
		if(cityID>0)
		{
			mMKOLUpdateElement= getUpdateInfo(cityID);
			Log.e("TAG","before start download status : "+(mMKOLUpdateElement==null?"null":mMKOLUpdateElement.status));
			//û����ɲ�����
			if(mMKOLUpdateElement!=null&&mMKOLUpdateElement.status != MKOLUpdateElement.FINISHED)
			{
				mOffline.start(cityID);
				Toast.makeText( mContext,"��ʼ�������ߵ�ͼ. cityid: "+cityID, Toast.LENGTH_SHORT)
				.show();
				isStart = true;
			}
			mMKOLUpdateElement= getUpdateInfo(cityID);
			Log.e("TAG","after start download status : "+(mMKOLUpdateElement==null?"null":mMKOLUpdateElement.status));
		}
		return isStart;
		
	}
	/**
	 * ��ͣ����
	 * @param view
	 */
	public boolean pause(int cityID){
		boolean isPause = false;
		MKOLUpdateElement mMKOLUpdateElement;
		Log.e("TAG","before stop download status : "+(getUpdateInfo(cityID)==null?"null":getUpdateInfo(cityID).status));
		mMKOLUpdateElement= getUpdateInfo(cityID);
		if(mMKOLUpdateElement!=null&&mMKOLUpdateElement.status != MKOLUpdateElement.FINISHED)//������֮����ͣ ���ٴ�����
		{
			mOffline.pause(cityID);
			Toast.makeText(mContext, "��ͣ�������ߵ�ͼ. cityid: "+cityID, Toast.LENGTH_SHORT)
			.show();
			isPause = true;
		}
		Log.e("TAG","after stop download status : "+(getUpdateInfo(cityID)==null?"null":getUpdateInfo(cityID).status));
		return isPause;
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


}
