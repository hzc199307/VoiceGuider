package com.ne.voiceguider.bean;

import java.io.Serializable;

import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.google.android.gms.maps.model.LatLng;

public class CityBean implements Serializable,Bean{
	
	//private static final long serialVersionUID = 1L;
	private int cityID=-1;
	private String cityName;
	private String cityPinyin;
	private double longtitude;
	private double latitude;
	public GeoPoint getGeoPoint()
	{
		return new GeoPoint((int)(latitude*1e6), (int)(longtitude*1e6));
	}
	@Override
	public LatLng getLatLng() {
		return new LatLng(latitude,longtitude);
	}
	public int getCityID() {
		return cityID;
	}
	public void setCityID(int cityID) {
		this.cityID = cityID;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getCityPinyin() {
		return cityPinyin;
	}
	public void setCityPinyin(String cityPinyin) {
		this.cityPinyin = cityPinyin;
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
