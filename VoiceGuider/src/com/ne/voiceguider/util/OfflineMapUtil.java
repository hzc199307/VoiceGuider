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
 * 离线地图工具类
 * @ClassName: OfflineMapUtil 
 * @Description: TODO 
 * @author HeZhichao
 * @date 2014年5月20日 下午4:39:17 
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
         * 初始化离线地图模块,MapControler可从MapView.getController()获取
         */
		MyMKOfflineMapListener mMyMKOfflineMapListener = new MyMKOfflineMapListener();
        mOffline.init(mMapView.getController(), null);
	}
	/**
	 * 返回已下载的离线地图信息列表
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
	 * 返回已下载的离线地图信息列表
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
	 * 搜索离线城市
	 * @param view
	 */
	public int search(String cityName){
		ArrayList<MKOLSearchRecord> records = mOffline.searchCity(cityName);
		if (records == null || records.size() != 1)
			return -1;
		return records.get(0).cityID;
	}
	
	/**
	 * 开始(继续)下载
	 * @param view
	 */
	public void start(int cityID){
		if(cityID>0)
			mOffline.start(cityID);
		else
			return ;
		Toast.makeText( mContext,"开始下载离线地图. cityid: "+cityID, Toast.LENGTH_SHORT)
		          .show();
	}
	/**
	 * 暂停下载
	 * @param view
	 */
	public void stop(int cityID){
		mOffline.pause(cityID);
		Toast.makeText(mContext, "暂停下载离线地图. cityid: "+cityID, Toast.LENGTH_SHORT)
		          .show();
	}
	/**
	 * 删除离线地图
	 * @param view
	 */
	public void remove(int cityID){
		mOffline.remove(cityID);
		Toast.makeText(mContext, "删除离线地图. cityid: "+cityID, Toast.LENGTH_SHORT)
		          .show();
	}
	/**
	 * 从SD卡导入离线地图安装包
	 * @param view
	 */
    public void importFromSDCard(){
		int num = mOffline.scan();
		String msg = "";
		if ( num == 0){
			msg = "没有导入离线包，这可能是离线包放置位置不正确，或离线包已经导入过";
		}
		else{
		   msg = String.format("成功导入 %d 个离线包，可以在下载管理查看",num);	
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
//					//处理下载进度更新提示
//					if ( update != null ){
//					    stateView.setText(String.format("%s : %d%%", update.cityName, update.ratio));
//					    updateView();
//					}
//				}
//				break;
//			case MKOfflineMap.TYPE_NEW_OFFLINE:
//				//有新离线地图安装
//				Log.d("OfflineDemo", String.format("add offlinemap num:%d", state));
//				break;
//			case MKOfflineMap.TYPE_VER_UPDATE:
//			    // 版本更新提示
//	            //	MKOLUpdateElement e = mOffline.getUpdateInfo(state);
//				
//				break;
//			}
			 
		}
	}
}
