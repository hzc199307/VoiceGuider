package com.ne.voiceguider.util;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

public class SystemUtil {

	private static AudioManager mAudioManager;
	public SystemUtil(Context mContext) {
		mAudioManager = (AudioManager) mContext
				.getSystemService(Context.AUDIO_SERVICE);
	}
	
	public void setAudioVolume(int streamType, int index) {
		mAudioManager.setStreamVolume(streamType, (int)(index/100.0*getAudioMaxVolume(streamType)), 0);
		// mAudioManager.setStreamVolume(AudioManager.STREAM_RING, progress,
		// 0);//铃声
		// mAudioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION,
		// progress, 0);//通知
		// mAudioManager.setStreamVolume(AudioManager.STREAM_ALARM, progress,
		// 0);//闹铃
		// mAudioManager.setStreamVolume(AudioManager.STREAM_DTMF, progress,
		// 0);//双音多频电话（MIUI支持）
		// mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress,
		// 0);//音乐
		// mAudioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, progress,
		// 0);//系统
		// mAudioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,
		// progress, 0);//通话（MIUI不支持）
	}

	public int getAudioVolume(int streamType) {
		// TODO Auto-generated method stub
		return (int)(mAudioManager.getStreamVolume(streamType)*100.0/getAudioMaxVolume(streamType));
		// mAudioManager.getStreamVolume(AudioManager.STREAM_RING);//铃声
		// mAudioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION);//通知
		// mAudioManager.getStreamVolume(AudioManager.STREAM_ALARM);//闹铃
		// mAudioManager.getStreamVolume(AudioManager.STREAM_DTMF);//双音多频电话
		// mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);//音乐
		// mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);//系统
		// mAudioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);//通话
	}
	
	public int getAudioMaxVolume(int streamType) {
		// TODO Auto-generated method stub
		return mAudioManager.getStreamMaxVolume(streamType);
		// mAudioManager.getStreamMaxVolume(AudioManager.STREAM_RING);//铃声
		// mAudioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION);//通知
		// mAudioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);//闹铃
		// mAudioManager.getStreamMaxVolume(AudioManager.STREAM_DTMF);//双音多频电话
		// mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);//音乐
		// mAudioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);//系统
		// mAudioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);//通话
	}
	
	public void addMusicVolume(int index)
	{
		Log.v("", getAudioMaxVolume(AudioManager.STREAM_MUSIC)+"addMusicVolume");
		setAudioVolume(AudioManager.STREAM_MUSIC,getAudioMaxVolume(AudioManager.STREAM_MUSIC)+index);
//		if(getAudioMaxVolume(AudioManager.STREAM_MUSIC)<(100-index))
//			setAudioVolume(AudioManager.STREAM_MUSIC,getAudioMaxVolume(AudioManager.STREAM_MUSIC)+index);
//		else
//			setAudioVolume(AudioManager.STREAM_MUSIC,100);
	}
	
	public void subtractMusicVolume(int index)
	{
		Log.v("", getAudioMaxVolume(AudioManager.STREAM_MUSIC)+"subtractMusicVolume");
		setAudioVolume(AudioManager.STREAM_MUSIC,getAudioMaxVolume(AudioManager.STREAM_MUSIC)-index);
//		if(getAudioMaxVolume(AudioManager.STREAM_MUSIC)>index)
//			setAudioVolume(AudioManager.STREAM_MUSIC,getAudioMaxVolume(AudioManager.STREAM_MUSIC)-index);
//		else
//			setAudioVolume(AudioManager.STREAM_MUSIC,0);
	}
	
	
}
