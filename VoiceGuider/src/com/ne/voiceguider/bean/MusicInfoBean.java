package com.ne.voiceguider.bean;
/**
 * 
* @ClassName: MusicInfoBean 
* @Description: 音乐信息dto
* @author 许圣�?
* @date 2014�?5�?26�? 上午11:35:38 
*
 */
public class MusicInfoBean {
	private int _id;
	private String name;
	private int musicTime;
	private int musicSize;
	private String musicPath;
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getMusicTime() {
		return musicTime;
	}
	public void setMusicTime(int musicTime) {
		this.musicTime = musicTime;
	}
	public int getMusicSize() {
		return musicSize;
	}
	public void setMusicSize(int musicSize) {
		this.musicSize = musicSize;
	}
	public String getMusicPath() {
		return musicPath;
	}
	public void setMusicPath(String musicPath) {
		this.musicPath = musicPath;
	}
}
