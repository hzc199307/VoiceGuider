package com.ne.voiceguider.dao;

import java.util.ArrayList;
import java.util.List;

import com.ne.voiceguider.DBHelper.TableColumns.BigSceneColumns;
import com.ne.voiceguider.DBHelper.TableColumns.CityColumns;
import com.ne.voiceguider.DBHelper.TableColumns.SmallSceneColumns;
import com.ne.voiceguider.bean.BigScene;
import com.ne.voiceguider.bean.CityBean;
import com.ne.voiceguider.bean.MusicInfoBean;
import com.ne.voiceguider.bean.SmallScene;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CitySceneDao {

	private SQLiteDatabase db;
	private final String TAG ="CitySceneDao";

	public CitySceneDao(Context mContext){
		db=mContext.openOrCreateDatabase
				("city_scene.db", Context.MODE_PRIVATE, null);

	}

	/**
	 * 查询数据库的城市列表
	 * @return List<CityBean>
	 */
	public List<CityBean> getCityBeans(){
		List<CityBean> listCityBeans = new ArrayList<CityBean>();
		Cursor cr=db.query("city", null, null, null, null, null, null);
		int count=cr.getCount();
		Log.v(TAG, "start getCityBeans() count = "+count);
		if(cr!=null){
			cr.moveToFirst();		
			for (int i = 0; i < count; i++) {
				CityBean cb=new CityBean();
				cb.setCityID(cr.getInt(cr.getColumnIndex(CityColumns.cityID)));
				cb.setCityName(cr.getString(cr.getColumnIndex(CityColumns.cityName)));
				cb.setCityPinyin(cr.getString(cr.getColumnIndex(CityColumns.cityPinyin)));
				cb.setLatitude(cr.getDouble(cr.getColumnIndex(CityColumns.latitude)));
				cb.setLongtitude(cr.getDouble(cr.getColumnIndex(CityColumns.longtitude)));
				listCityBeans.add(cb);
				cr.moveToNext();
			}
		}
		Log.v(TAG, "finish getCityBeans() count = "+count);
		return listCityBeans;
	}

	/**
	 * 根据城市ID查询它包含的大景点
	 * @param cityID
	 * @return List<BigScene>
	 */
	public List<BigScene> getBigScenes(int cityID){
		List<BigScene> listBigScenes=new ArrayList<BigScene>();
		String[] args={cityID+""};
		Cursor cr=db.query
				("bigscene", null, "CityID=?",args , null, null, null);
		int count=cr.getCount();
		Log.v(TAG, "start getBigScenes() count = "+count);
		if(cr!=null){
			cr.moveToFirst();
			
			for (int i = 0; i < count; i++) {
				BigScene bs=new BigScene();
				bs.setBigSceneID(cr.getInt(cr.getColumnIndex(BigSceneColumns.bigSceneID)));
				bs.setBigSceneName(cr.getString(cr.getColumnIndex(BigSceneColumns.bigSceneName)));
				bs.setBigScenePinyin(cr.getString(cr.getColumnIndex(BigSceneColumns.bigScenePinyin)));
				bs.setLatitude(cr.getDouble(cr.getColumnIndex(BigSceneColumns.latitude)));
				bs.setLongitude(cr.getDouble(cr.getColumnIndex(BigSceneColumns.longtitude)));
				bs.setCityID(cr.getInt(cr.getColumnIndex(BigSceneColumns.cityID)));
				bs.setMP3Downloaded(cr.getInt(cr.getColumnIndex(BigSceneColumns.isMP3Downloaded)));
				listBigScenes.add(bs);
				cr.moveToNext();
			}	
		}
		Log.v(TAG, "finish getBigScenes() count = "+count);
		return listBigScenes;
	}

	//根据大景点查询他包含的小景点
	public List<SmallScene> getSmallScenes(int bigSceneID){
		List<SmallScene> listSmallScene=new ArrayList<SmallScene>(); 
		String[] args={bigSceneID+""};
		Cursor cr=db.query("smallscene", null, "bigSceneID=?" ,
				args, null, null, null);
		int count=cr.getCount();
		Log.v(TAG, "start getSmallScenes() count = "+count);
		if (cr != null) {
			cr.moveToFirst();
			for (int i = 0; i < count; i++) {
				SmallScene ss=new SmallScene();
				ss.setSmallSceneID(cr.getInt(cr.getColumnIndex(SmallSceneColumns.smallSceneID)));
				ss.setSmallSceneName(cr.getString(cr.getColumnIndex(SmallSceneColumns.smallSceneName)));
				ss.setSmallScenePinyin(cr.getString(cr.getColumnIndex(SmallSceneColumns.smallScenePinyin)));
				ss.setLatitude(cr.getDouble(cr.getColumnIndex(SmallSceneColumns.latitude)));
				ss.setLongtitude(cr.getDouble(cr.getColumnIndex(SmallSceneColumns.longtitude)));
				ss.setBigSceneID(cr.getInt(cr.getColumnIndex(SmallSceneColumns.bigSceneID)));
				ss.setCityID(cr.getInt(cr.getColumnIndex(SmallSceneColumns.cityID)));
				ss.setMp3Time(cr.getInt(cr.getColumnIndex(SmallSceneColumns.mp3Time)));
				listSmallScene.add(ss);
				cr.moveToNext();
			}
		}
		Log.v(TAG, "finish getSmallScenes() count = "+count);
		return listSmallScene;
	}
}
