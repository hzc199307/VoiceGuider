package com.ne.voiceguider.service;

import java.io.IOException;

import com.ne.voiceguider.R;
import com.ne.voiceguider.util.MusicPlayerUtil;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class VoicePlayerService extends Service {

	private String TAG = "VoicePlayerService";
	static final int PLAY_LIST[] = {
		R.raw.test_music
	};
	
	private MediaPlayer mediaPlayer;
	
	private MediaBinder mBinder;
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.v(TAG, "onCreate");
		mBinder = new MediaBinder();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		try {
			mediaPlayer.stop();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.v(TAG, "onBind");
		return mBinder;
	}
	
	public class MediaBinder extends Binder {
		
		private String bigScenePinyin;
		private int position;
		public MediaBinder()
		{
			mediaPlayer = new MediaPlayer();
		}
		public String getBigScenePinyin() {
			return bigScenePinyin;
		}
		public int getPosition() {
			return position;
		}
		public void setDataSource(int position,String cityPinyin,String bigScenePinyin,String smallScenePinyin) 
		{
			this.bigScenePinyin = bigScenePinyin;
			this.position = position;
//			mediaPlayer = new MediaPlayer();
			try {Log.v(TAG, MusicPlayerUtil.getVoicePath(cityPinyin,bigScenePinyin,smallScenePinyin));
			    Log.v(TAG, "mediaPlayer==null? "+(mediaPlayer==null));
				mediaPlayer.setDataSource(MusicPlayerUtil.getVoicePath(cityPinyin,bigScenePinyin,smallScenePinyin));
				mediaPlayer.prepare();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
//			mediaPlayer = MediaPlayer.create(VoicePlayerService.this, PLAY_LIST[0]);
		}
		
		public void play() {
			if(mediaPlayer != null) {
				mediaPlayer.start();
				Log.v(TAG, "MediaBinder.play()"+mediaPlayer.isPlaying());
			}
			
		}
		
		public void pause() {
			if(mediaPlayer != null&&mediaPlayer.isPlaying()) {
				mediaPlayer.pause();	
			}
			Log.v(TAG, "MediaBinder.pause()"+!mediaPlayer.isPlaying());
		}
		
		public void stop() {
			if(mediaPlayer != null&&mediaPlayer.isPlaying()) {
				pause();
				mediaPlayer.stop();
			}
			Log.v(TAG, "MediaBinder.stop()");
			mediaPlayer.release();//TODO 这里的先释放  再赋空  再重新创建对象
			mediaPlayer = null;
			mediaPlayer = new MediaPlayer();
		}
		
		public boolean isPlaying() {
			if(mediaPlayer != null) {
				return mediaPlayer.isPlaying();
			}
			return false;
		}
		
//		public boolean isPlaying(String smallScenePinyin) {
//			if(mediaPlayer != null) {
//				if(mediaPlayer.isPlaying())
//					if(smallScenePinyin.equals(nowSmallScenePinyin))
//						return true;
//			}
//			return false;
//		}
		
		public int getDuration() {
			if(mediaPlayer != null) {
				return mediaPlayer.getDuration();
			}
			return 0;
		}
		
		public int getCurrentPosition() {
			if(mediaPlayer != null) {
				return mediaPlayer.getCurrentPosition();
			}
			return 0;
		}
		
		public void seekTo(int sec) {
			if(mediaPlayer != null) {
				try {
					mediaPlayer.seekTo(sec);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	public interface OnMediaChangeListener {

		public void onMediaPlay();
		public void onMediaPause();
		public void onMediaStop();
		public void onMediaCompletion();
	}
}