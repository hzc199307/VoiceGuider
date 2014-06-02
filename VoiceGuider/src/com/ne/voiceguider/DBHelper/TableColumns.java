package com.ne.voiceguider.DBHelper;

public class TableColumns {

	public interface CityColumns {
		public static final String cityID = "cityID";
		public static final String cityName = "cityName";
		public static final String cityPinyin = "cityPinyin";
		public static final String latitude = "latitude";
		public static final String longtitude = "longtitude";	
	}
	public interface BigSceneColumns {
		public static final String bigSceneID = "bigSceneID";
		public static final String bigSceneName = "bigSceneName";
		public static final String bigScenePinyin = "bigScenePinyin";
		public static final String latitude = "latitude";
		public static final String longtitude = "longtitude";	
		public static final String cityID = "cityID";
		public static final String isMP3Downloaded = "isMP3Downloaded";
	}
	public interface SmallSceneColumns {
		public static final String smallSceneID = "smallSceneID";
		public static final String smallSceneName = "smallSceneName";
		public static final String smallScenePinyin = "smallScenePinyin";
		public static final String latitude = "latitude";
		public static final String longtitude = "longtitude";	
		public static final String bigSceneID = "bigSceneID";
		public static final String cityID = "cityID";
		public static final String mp3Time = "mp3Time";
	}
}
