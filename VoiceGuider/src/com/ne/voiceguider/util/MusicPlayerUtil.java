package com.ne.voiceguider.util;

import java.io.File;
import java.io.IOException;

import com.ne.voiceguider.activity.GuiderActivity;

import android.os.Environment;
import android.util.Log;
import android.widget.Toast;


public class MusicPlayerUtil {

	private final static String TAG = "MusicPlayerUtil";
	/**
	 * Function to convert milliseconds time to
	 * Timer Format
	 * Hours:Minutes:Seconds
	 * */
    public static String milliSecondsToTimer(long milliseconds){
        String finalTimerString = "";
        String secondsString = "";
 
        // Convert total duration into time
           int hours = (int)( milliseconds / (1000*60*60));
           int minutes = (int)(milliseconds % (1000*60*60)) / (1000*60);
           int seconds = (int) ((milliseconds % (1000*60*60)) % (1000*60) / 1000);
           // Add hours if there
           if(hours > 0){
               finalTimerString = hours + ":";
           }
 
           // Prepending 0 to seconds if it is one digit
           if(seconds < 10){
               secondsString = "0" + seconds;
           }else{
               secondsString = "" + seconds;}
 
           finalTimerString = finalTimerString + minutes + ":" + secondsString;
 
        // return timer string
        return finalTimerString;
    }
 
    /**
     * Function to get Progress percentage
     * @param currentDuration
     * @param totalDuration
     * */
    public static int getProgressPercentage(long currentDuration, long totalDuration){
        Double percentage = (double) 0;
 
        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);
 
        // calculating percentage
        percentage =(((double)currentSeconds)/totalSeconds)*100;
 
        // return percentage
        return percentage.intValue();
    }
 
    /**
     * Function to change progress to timer
     * @param progress -
     * @param totalDuration
     * returns current duration in milliseconds
     * */
    public static int progressToTimer(int progress, int totalDuration) {
        int currentDuration = 0;
        totalDuration = (int) (totalDuration / 1000);
        currentDuration = (int) ((((double)progress) / 100) * totalDuration);
 
        // return current duration in milliseconds
        return currentDuration * 1000;
    }
    
    private static boolean sdCardExit = false;
    private static File myVoiceDir;
    private static File myVoiceFile = null;
    public static String getVoicePath(String city,String bigScene,String smallScene) {
    	String myVoicePath = null;
    	/* 判断SD Card是否插入 */
    	sdCardExit = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		/* 取得SD Card路径做为录音的文件位置 */
		if (sdCardExit){
			myVoiceDir = Environment.getExternalStorageDirectory();
			String path = myVoiceDir.getPath()+"/VoiceGuider/voice/"+city+"/"+bigScene+"/"+smallScene+".mp3";
			myVoiceFile = new File(path);
			if (!myVoiceFile.exists()) {
				Log.v(TAG,"" );;
			}
			return myVoiceFile.getPath();
		}
		else
			Log.v(TAG,"请检查 内存卡 是否可用！" );//Toast.makeText(, "请检查 内存卡 是否可用！", Toast.LENGTH_SHORT).show();
		return null;
    }
    
}