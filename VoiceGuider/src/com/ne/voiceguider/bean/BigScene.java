package com.ne.voiceguider.bean;

/**
 * �󾰵�ʵ����
 * @ClassName: BigScene 
 * @Description: TODO 
 * @author HeZhichao
 * @date 2014��5��21�� ����6:58:04 
 *
 */
public class BigScene {
	private String bigSceneName;//��������
	private int bigSceneID;//����ID
	private int cityID;//��������ID
	private double longitude;//����
	private double latitude;//γ��
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
