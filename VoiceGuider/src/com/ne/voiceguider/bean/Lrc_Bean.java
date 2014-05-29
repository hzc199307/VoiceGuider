package com.ne.voiceguider.bean;
/**
 * 
* @ClassName: Lrc_Bean 
* @Description: 歌词每一句信息DTO
* @author 许圣�?
* @date 2014�?5�?23�? 上午10:27:25 
*
 */
public class Lrc_Bean {
	private String Lrc;
	private int Lrc_time;
	public String getLrc() {
		return Lrc;
	}
	public void setLrc(String lrc) {
		Lrc = lrc;
	}
	public int getLrc_time() {
		return Lrc_time;
	}
	public void setLrc_time(int lrc_time) {
		Lrc_time = lrc_time;
	}
}
