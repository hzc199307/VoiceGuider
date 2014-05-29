package com.ne.voiceguider.bean;

public class BigScene {
	private int bigSceneId=-1;
	private String bigSceneName;
	private String bigScenePath;
	private int bigSceneLatitude;
	private int bigSceneLongitude;
	private int fkCityID;
	public int getBigSceneId() {
		return bigSceneId;
	}
	public void setBigSceneId(int bigSceneId) {
		this.bigSceneId = bigSceneId;
	}
	public String getBigSceneName() {
		return bigSceneName;
	}
	public void setBigSceneName(String bigSceneName) {
		this.bigSceneName = bigSceneName;
	}
	public String getBigScenePath() {
		return bigScenePath;
	}
	public void setBigScenePath(String bigScenePath) {
		this.bigScenePath = bigScenePath;
	}
	public int getBigSceneLatitude() {
		return bigSceneLatitude;
	}
	public void setBigSceneLatitude(int bigSceneLatitude) {
		this.bigSceneLatitude = bigSceneLatitude;
	}
	public int getBigSceneLongitude() {
		return bigSceneLongitude;
	}
	public void setBigSceneLongitude(int bigSceneLongitude) {
		this.bigSceneLongitude = bigSceneLongitude;
	}
	public int getFkCityID() {
		return fkCityID;
	}
	public void setFkCityID(int fkCityID) {
		this.fkCityID = fkCityID;
	}
	
}
