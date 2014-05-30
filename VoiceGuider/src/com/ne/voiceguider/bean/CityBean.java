package com.ne.voiceguider.bean;

import java.io.Serializable;

public class CityBean implements Serializable{
	/** 
	* @Fields serialVersionUID : TODO
	*/ 
	
	private static final long serialVersionUID = 1L;
	private int CityID=-1;
	private String CityName;
	private String dirPath;
	private double longtitude;
	private double latitude;
	public int getCityID() {
		return CityID;
	}
	public void setCityID(int cityID) {
		CityID = cityID;
	}
	public String getCityName() {
		return CityName;
	}
	public void setCityName(String cityName) {
		CityName = cityName;
	}
	public String getDirPath() {
		return dirPath;
	}
	public void setDirPath(String dirPath) {
		this.dirPath = dirPath;
	}
	public double getLongtitude() {
		return longtitude;
	}
	public void setLongtitude(double longtitude) {
		this.longtitude = longtitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
}
