package com.ne.voiceguider.bean;

import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.google.android.gms.maps.model.LatLng;

public class SmallScene implements Bean{
	private int smallSceneID;
	private String smallSceneName;
	private String smallScenePinyin;
	private double latitude;
	private double longtitude;
	private int bigSceneID;
	private int cityID;
	public int mp3Time;

	public GeoPoint getGeoPoint()
	{
		return new GeoPoint((int)(latitude*1e6), (int)(longtitude*1e6));
	}
	@Override
	public LatLng getLatLng() {
		return new LatLng(latitude,longtitude);
	}
	public int getSmallSceneID() {
		return smallSceneID;
	}
	public void setSmallSceneID(int smallSceneID) {
		this.smallSceneID = smallSceneID;
	}
	public String getSmallSceneName() {
		return smallSceneName;
	}
	public void setSmallSceneName(String smallSceneName) {
		this.smallSceneName = smallSceneName;
	}
	public String getSmallScenePinyin() {
		return smallScenePinyin;
	}
	public void setSmallScenePinyin(String smallScenePinyin) {
		this.smallScenePinyin = smallScenePinyin;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongtitude() {
		return longtitude;
	}
	public void setLongtitude(double longtitude) {
		this.longtitude = longtitude;
	}
	public int getBigSceneID() {
		return bigSceneID;
	}
	public void setBigSceneID(int bigSceneID) {
		this.bigSceneID = bigSceneID;
	}
	public int getCityID() {
		return cityID;
	}
	public void setCityID(int cityID) {
		this.cityID = cityID;
	}
	public int getMp3Time() {
		return mp3Time;
	}
	public void setMp3Time(int mp3Time) {
		this.mp3Time = mp3Time;
	}

	@Override
	public int getID() {
		return smallSceneID;
	}
	@Override
	public void setID(int id) {
		smallSceneID = id;
	}
	@Override
	public String getName() {
		return smallSceneName;
	}
	@Override
	public void setName(String name) {
		smallSceneName = name;
	}
	@Override
	public String getResourceName() {
		// TODO Auto-generated method stub
		return "smallscene_"+getSmallScenePinyin();
	}
}
