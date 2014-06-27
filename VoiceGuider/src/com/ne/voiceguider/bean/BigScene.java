package com.ne.voiceguider.bean;

import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.google.android.gms.maps.model.LatLng;

public class BigScene implements Bean{
	private int bigSceneID=-1;
	private String bigSceneName;
	private String bigScenePinyin;
	private double latitude;
	private double longitude;
	private int cityID;
	private int isMP3Downloaded = 0; 
	
	public GeoPoint getGeoPoint()
	{
		return new GeoPoint((int)(latitude*1e6), (int)(longitude*1e6));
	}
	public int getBigSceneID() {
		return bigSceneID;
	}
	public void setBigSceneID(int bigSceneID) {
		this.bigSceneID = bigSceneID;
	}
	public String getBigSceneName() {
		return bigSceneName;
	}
	public void setBigSceneName(String bigSceneName) {
		this.bigSceneName = bigSceneName;
	}
	public String getBigScenePinyin() {
		return bigScenePinyin;
	}
	public void setBigScenePinyin(String bigScenePinyin) {
		this.bigScenePinyin = bigScenePinyin;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public int getCityID() {
		return cityID;
	}
	public void setCityID(int cityID) {
		this.cityID = cityID;
	}
	public int isMP3Downloaded() {
		return isMP3Downloaded;
	}
	public void setMP3Downloaded(int isMP3Downloaded) {
		this.isMP3Downloaded = isMP3Downloaded;
	}
	@Override
	public LatLng getLatLng() {
		return new LatLng(latitude,longitude);
	}
	@Override
	public int getID() {
		return bigSceneID;
	}
	@Override
	public void setID(int id) {
		bigSceneID = id;
	}
	@Override
	public String getName() {
		return bigSceneName;
	}
	@Override
	public void setName(String name) {
		bigSceneName = name;
	}
	@Override
	public String getResourceName() {
		// TODO Auto-generated method stub
		return "bigscene_"+getBigScenePinyin();
	}

	
}
