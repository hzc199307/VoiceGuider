package com.ne.voiceguider.bean;

public class BigScene {
	private int bigSceneID=-1;
	private String bigSceneName;
	private String bigScenePinyin;
	private double latitude;
	private double longitude;
	private int cityID;
	private int isMP3Downloaded = 0; 
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

	
}
