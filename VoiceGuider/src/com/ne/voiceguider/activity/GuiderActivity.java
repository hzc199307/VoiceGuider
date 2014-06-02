package com.ne.voiceguider.activity;


import com.ne.voiceguider.R;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * 小景点的acitivity
 * @ClassName: GuiderActivity 
 * @Description: TODO 
 * @author HeZhichao
 * @date 2014年5月28日 下午12:16:29 
 *
 */
public class GuiderActivity extends ActionBarActivity {

	private TextView guider_head_text ;
	private WebView guider_text_webview ;
	
	// music player
    private SeekBar seekBar;
    private Button startMedia;
    private Button stop;
    private MediaPlayer mp;  
    boolean isPlaying= false;
    int currentPos= 0;
    Drawable mThumb= null;
    
	
    private OnTouchListener onTouchListener_voice= new OnTouchListener() {
		
		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
			// TODO Auto-generated method stub
			
			if(arg1.getAction() != MotionEvent.ACTION_DOWN)
				return false;
			int id= arg0.getId();
			switch(id){
			
			case R.id.scene_music_seekbar:
				int nextPos= seekBar.getProgress();
				if(Math.abs(nextPos - currentPos) > 100)
					return false;
				if(isPlaying){
					mp.pause();
					seekBar.setThumb(getResources().getDrawable(R.drawable.thumb_pause));
					isPlaying= false;
				}
				else{
					if(mp == null){
						mp = MediaPlayer.create(GuiderActivity.this, R.raw.test_music); // set the music rc
						seekBar.setProgress(0);
						seekBar.setMax(mp.getDuration());
					}
					mp.start();
					seekBar.setThumb(getResources().getDrawable(R.drawable.thumb_playing));
					isPlaying= true;
				}
				break;
			default:
				break;
			}
			return false;
		}
	}; 
	
	private SeekBar.OnSeekBarChangeListener onSeekBarChangeListener= new SeekBar.OnSeekBarChangeListener() {
		
		
		
		@Override
		public void onStopTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
			// TODO Auto-generated method stub
			arg0.setProgress(arg1);
			currentPos= arg1;
			
		}
	};
			
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_guider);
		guider_head_text = (TextView)findViewById(R.id.guider_head_text);
		Intent  intent = getIntent();
		if ( intent.hasExtra("bigSceneName")  ){
			//当用intent参数时，设置中心点为指定点
			Bundle b = intent.getExtras();
			guider_head_text.setText(b.getString("bigSceneName"));
		}
		loadAssetHtml();
		
		// music player
        seekBar = (SeekBar) findViewById(R.id.scene_music_seekbar);
        mp = MediaPlayer.create(GuiderActivity.this, R.raw.test_music); // set the music rc
		seekBar.setProgress(0);
        seekBar.setMax(mp.getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
        	int lastProgress;
        	int originalProgress;
        	boolean isThumbClick= false;

        	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
        		if(fromTouch == true){
        			// only allow changes by 1 up or down
        			if(Math.abs(progress-lastProgress)*1.0 / seekBar.getMax() > 0.1){
        				seekBar.setProgress(lastProgress);
        			} else {
        				lastProgress = progress;
        				isThumbClick= true;
        			}
        		} 
        	}

        	@Override
        	public void onStopTrackingTouch(SeekBar seekBar) {
        		if(! isThumbClick)
        			return;
        		if(Math.abs(lastProgress-originalProgress)*1.0 / seekBar.getMax() > 0.1)
        			return;
        		originalProgress= lastProgress;
        		if(isPlaying){
					mp.pause();
					seekBar.setThumb(getResources().getDrawable(R.drawable.thumb_pause));
					isPlaying= false;
				}
				else{
					mp.start();
					seekBar.setThumb(getResources().getDrawable(R.drawable.thumb_playing));
					isPlaying= true;
				}
        	}

        	@Override
        	public void onStartTrackingTouch(SeekBar seekBar) {
        		originalProgress= lastProgress = seekBar.getProgress();
        	}
        });

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.guider, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * 载入文本视图
	 * @Title: loadAssetHtml 
	 * @Description: TODO
	 * @author HeZhichao
	 * @date 2014年5月28日 下午2:16:09 
	 * @param 
	 * @return void 
	 * @throws
	 */
	public void loadAssetHtml() {

	    final String mimeType = "text/html";  
	    final String encoding = "utf-8"; 
	    String url = "http://www.baidu.com/";
		guider_text_webview = (WebView) findViewById(R.id.guider_text_webview);
		// 设置WebView属性，能够执行JavaScript脚本
		guider_text_webview.getSettings().setJavaScriptEnabled(true);
		guider_text_webview.getSettings().setDefaultTextEncodingName(encoding);
		guider_text_webview.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
		guider_text_webview.setHorizontalScrollBarEnabled(false);
		guider_text_webview.getSettings().setSupportZoom(true);
		guider_text_webview.getSettings().setBuiltInZoomControls(true);
		guider_text_webview.setInitialScale(70);
		guider_text_webview.setHorizontalScrollbarOverlay(true);
		guider_text_webview.setWebViewClient(new WebViewClient());
		guider_text_webview.loadUrl("file:///android_asset/HTML/introduction.html");
		guider_text_webview.getSettings().setUseWideViewPort(true);
		guider_text_webview.getSettings().setLoadWithOverviewMode(true);
		guider_text_webview.getSettings().setBuiltInZoomControls(false);
	}
	/**
	 * 监控返回键
	 * @param position
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			finish();
		}
		return true;
	}

	public class MyWebViewClient extends WebViewClient {
		/**
		* Show in webview not system webview.
		*/
		public boolean shouldOverviewUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return super.shouldOverrideUrlLoading(view, url);
		}
	}
}

