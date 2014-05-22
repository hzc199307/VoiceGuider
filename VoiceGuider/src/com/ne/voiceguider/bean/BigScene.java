package com.ne.voiceguider.bean;

/**
 * 大景点实体类
 * @ClassName: BigScene 
 * @Description: TODO 
 * @author HeZhichao
 * @date 2014年5月21日 下午6:58:04 
 *
 */
public class BigScene {
	private String bigSceneName;//景点名称
	private int bigSceneID;//景点ID
	private int cityID;//所属城市ID
	private double longitude;//经度
	private double latitude;//纬度
	public String getBigSceneName() {
		return bigSceneName;
	}
	public void setBigSceneName(String bigSceneName) {
		this.bigSceneName = bigSceneName;
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
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
}
