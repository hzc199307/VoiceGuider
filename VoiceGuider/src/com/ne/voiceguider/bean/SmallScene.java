package com.ne.voiceguider.bean;

public class SmallScene {
	private int smallSceneID;
	private String smallSceneName;
	private String smallScenePath;
	private double smallSceneLatitude;
	private double smallSceneLongtitude;
	private int fkBigSceneId;
	private int fkCityID;
	private MusicInfoBean musicInfo;

	public MusicInfoBean getMusicInfo() {
		return musicInfo;
	}
	public void setMusicInfo(MusicInfoBean musicInfo) {
		this.musicInfo = musicInfo;
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
	public String getSmallScenePath() {
		return smallScenePath;
	}
	public void setSmallScenePath(String smallScenePath) {
		this.smallScenePath = smallScenePath;
	}
	public double getSmallSceneLatitude() {
		return smallSceneLatitude;
	}
	public void setSmallSceneLatitude(double smallSceneLatitude) {
		this.smallSceneLatitude = smallSceneLatitude;
	}
	public double getSmallSceneLongtitude() {
		return smallSceneLongtitude;
	}
	public void setSmallSceneLongtitude(double smallSceneLongtitude) {
		this.smallSceneLongtitude = smallSceneLongtitude;
	}
	public int getFkBigSceneId() {
		return fkBigSceneId;
	}
	public void setFkBigSceneId(int fkBigSceneId) {
		this.fkBigSceneId = fkBigSceneId;
	}
	public int getFkCityID() {
		return fkCityID;
	}
	public void setFkCityID(int fkCityID) {
		this.fkCityID = fkCityID;
	}
}
