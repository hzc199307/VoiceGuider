package com.ne.voiceguider.bean;

public class SmallScene {
	private int smallSceneID;
	private String smallSceneName;
	private String smallScenePinyin;
	private double latitude;
	private double longtitude;
	private int bigSceneID;
	private int cityID;
	public int mp3Time;
	
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

}
