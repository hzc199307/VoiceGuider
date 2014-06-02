//package com.ne.voiceguider.dao;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.util.Log;
//
//import com.ne.voiceguider.bean.BigScene;
//import com.ne.voiceguider.bean.CityBean;
//import com.ne.voiceguider.bean.MusicInfoBean;
//import com.ne.voiceguider.bean.SmallScene;
//
//public class CitySceneDao2 {
//	Context mContext;
//	SQLiteDatabase db;
//	private String tag="CitySceneDao";
//	public CitySceneDao2(Context context){
//		this.mContext=context;
//		db=mContext.openOrCreateDatabase
//				("city_scene.db", mContext.MODE_PRIVATE, null);
//	
//	}
//	//���ݴ󾰵��ѯ��������С����
//	public List<SmallScene> getContentScene(BigScene bs){
//		if(bs==null||bs.getBigSceneId()==-1){
//			Log.e(tag, "BigScene bsΪ��");
//			return null;
//		}
//		List<SmallScene> listSmallScene=new ArrayList<SmallScene>(); 
//		String[] columns={"BigSceneID","SmallSceneID",
//				"SmallSceneName","path","latitude","longtitude",
//				"mp3PlayTime","mp3PlaySize","CityID"};
//		String bId=bs.getBigSceneId()+"";
//		Cursor cr=db.query("smallscene", columns, "BigSceneID=?" ,
//				new String[]{bId}, null, null, null);
//
//		if (cr != null) {
//			cr.moveToFirst();
//			Log.v(tag, cr.getCount()+"count");
//			int count=cr.getCount();
//			for (int i = 0; i < count; i++) {
//				SmallScene sScene=new SmallScene();
//				sScene.setFkBigSceneId(cr.getInt(0));
//				sScene.setSmallSceneID(cr.getInt(1));
//				sScene.setSmallSceneName(cr.getString(2));
//				sScene.setSmallScenePath(cr.getString(3));
//				sScene.setSmallSceneLatitude(cr.getDouble(4));
//				sScene.setSmallSceneLongtitude(cr.getDouble(5));
//				MusicInfoBean mib=new MusicInfoBean();
//				mib.setMusicPath(cr.getString(3));
//				mib.setName(cr.getString(2));
//				mib.setMusicTime(cr.getInt(6));
//				mib.setMusicSize(cr.getInt(7));
//				sScene.setMusicInfo(mib);
//				sScene.setFkCityID(cr.getInt(8));
//				listSmallScene.add(sScene);
//				cr.moveToNext();
//			}
//		}
//		return listSmallScene;
//	}
//	//���ݳ��в�ѯ�����Ĵ󾰵�
//	public List<BigScene> getBigScenes(CityBean cb){
//		if(cb==null||cb.getCityID()==-1){
//			Log.e(tag, "CityBean cbΪ��");
//			return null;
//		}
//		List<BigScene> listBigScene=new ArrayList<BigScene>();
//		String [] columns=new String[]
//				{"BigSceneID","BigSceneName","path","latitude","longitude","CityID"};
//		String[] args={cb.getCityID()+""};
//		Cursor cr=db.query
//				("bigscene", columns, "CityID=?",args , null, null, null);
//		if(cr!=null){
//			cr.moveToFirst();
//			int count=cr.getCount();
//			for (int i = 0; i < count; i++) {
//				BigScene bs=new BigScene();
//				bs.setBigSceneId(cr.getInt(0));
//				bs.setBigSceneName(cr.getString(1));
//				bs.setBigScenePath(cr.getString(2));
//				bs.setBigSceneLatitude(cr.getDouble(3));
//				bs.setBigSceneLongitude(cr.getDouble(4));
//				bs.setFkCityID(cr.getInt(5));
//				listBigScene.add(bs);
//				cr.moveToNext();
//			}	
//		}
//		return listBigScene;
//	}
//	//��ѯ������
//	public List<CityBean> getCityBeans(){
//		List<CityBean> listCity=new ArrayList<CityBean>();
//		String[] columns={"CityID","CityName",
//				"path","latitude","longtitude"};
//		Cursor cr=db.query("city", columns, null, null, null, null, null);
//		int count=cr.getCount();
//		Log.v(tag, count+"");
//		if(cr!=null){
//			cr.moveToFirst();
//			for (int i = 0; i < count; i++) {
//				CityBean cb=new CityBean();
//				cb.setCityID(cr.getInt(0));
//				cb.setCityName(cr.getString(1));
//				cb.setDirPath(cr.getString(2));
//				cb.setLatitude(cr.getDouble(3));
//				cb.setLongtitude(cr.getDouble(4));
//				listCity.add(cb);
//				Log.v(tag, "ִ������");
//				cr.moveToNext();
//			}
//		}
//		return listCity;
//	}
//	//���ݴ󾰵�ݹ�ɾ���󾰵�����е�С����
//	public int deleteBigCity(BigScene bs){
//		boolean isDelete=false;
//		//db.execSQL("PRAGMA foreign_keys = ON");
//		String [] args={bs.getBigSceneId()+""};
//		int result_row=db.delete("bigscene", "BigSceneID=?",args);
//		int result_row_ss=db.delete("smallscene", "BigSceneID=?", args);
//		return result_row+result_row_ss;
//		
//	}
//	//���ݳ���ɾ������
//	public int deleteCity(CityBean cb){
//		boolean isDelete=false;
//		String [] args={cb.getCityID()+""};
//		int resultCity=db.delete("city", "CityID=?", args);
//		int resultBs=db.delete("bigscene", "CityID=?", args);
//		int resultSS=db.delete("smallscene", "CityID=?", args);
//		return resultCity+resultBs+resultSS;
//		
//	}
//}
