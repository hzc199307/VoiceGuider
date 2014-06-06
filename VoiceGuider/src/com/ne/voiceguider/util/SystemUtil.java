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
		// 0);//����
		// mAudioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION,
		// progress, 0);//֪ͨ
		// mAudioManager.setStreamVolume(AudioManager.STREAM_ALARM, progress,
		// 0);//����
		// mAudioManager.setStreamVolume(AudioManager.STREAM_DTMF, progress,
		// 0);//˫����Ƶ�绰��MIUI֧�֣�
		// mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress,
		// 0);//����
		// mAudioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, progress,
		// 0);//ϵͳ
		// mAudioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,
		// progress, 0);//ͨ����MIUI��֧�֣�
	}

	public int getAudioVolume(int streamType) {
		// TODO Auto-generated method stub
		return (int)(mAudioManager.getStreamVolume(streamType)*100.0/getAudioMaxVolume(streamType));
		// mAudioManager.getStreamVolume(AudioManager.STREAM_RING);//����
		// mAudioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION);//֪ͨ
		// mAudioManager.getStreamVolume(AudioManager.STREAM_ALARM);//����
		// mAudioManager.getStreamVolume(AudioManager.STREAM_DTMF);//˫����Ƶ�绰
		// mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);//����
		// mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);//ϵͳ
		// mAudioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);//ͨ��
	}
	
	public int getAudioMaxVolume(int streamType) {
		// TODO Auto-generated method stub
		return mAudioManager.getStreamMaxVolume(streamType);
		// mAudioManager.getStreamMaxVolume(AudioManager.STREAM_RING);//����
		// mAudioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION);//֪ͨ
		// mAudioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);//����
		// mAudioManager.getStreamMaxVolume(AudioManager.STREAM_DTMF);//˫����Ƶ�绰
		// mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);//����
		// mAudioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);//ϵͳ
		// mAudioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);//ͨ��
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
