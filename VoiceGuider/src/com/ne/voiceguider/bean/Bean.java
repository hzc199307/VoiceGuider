package com.ne.voiceguider.bean;

import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.google.android.gms.maps.model.LatLng;

public interface Bean {

	GeoPoint getGeoPoint();
	
	LatLng getLatLng();
}
