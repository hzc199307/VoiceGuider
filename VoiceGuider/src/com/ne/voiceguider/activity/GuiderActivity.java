package com.ne.voiceguider.activity;


import com.ne.voiceguider.R;
import com.ne.voiceguider.adapter.SmallSceneAdapter;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 小景点的acitivity 包括语音播放
 * @ClassName: GuiderActivity 
 * @Description: TODO 
 * @author HeZhichao
 * @date 2014年5月28日 下午12:16:29 
 *
 */
public class GuiderActivity extends ActionBarActivity {

	private String bigSceneName ;
	private int bigSceneID ;
	private TextView guider_head_text ;
	private Button guider_head_back,scene_voice_text_button;
	private WebView guider_text_webview ;
	private ListView smallscene_listview = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_guider);
		
		guider_head();
		
		webview();
		
		
		smallscene_listview = (ListView)findViewById(R.id.smallscene_listview);
		smallscene_listview.setAdapter(new SmallSceneAdapter(this, bigSceneID));
	}
	
	/**
	 *  此acitivity的头部处理
	 */
	void guider_head()
	{
		guider_head_text = (TextView)findViewById(R.id.guider_head_text);
		Intent  intent = getIntent();
		if ( intent.hasExtra("bigSceneName")  ){
			Bundle b = intent.getExtras();
			bigSceneName = b.getString("bigSceneName");
			guider_head_text.setText(bigSceneName);
		}
		if ( intent.hasExtra("bigSceneID")  ){
			Bundle b = intent.getExtras();
			bigSceneID = b.getInt("bigSceneID");
		}
		guider_head_back= (Button)findViewById(R.id.guider_head_back);
		guider_head_back.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}

	/**
	 * 语音的文字 webview部分的处理
	 */
	void webview()
	{
		guider_text_webview = (WebView)findViewById(R.id.guider_text_webview);
		scene_voice_text_button = (Button)findViewById(R.id.scene_voice_text_button);
		scene_voice_text_button.setOnClickListener(new Button.OnClickListener() {
			private boolean haveWebView = false;
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(haveWebView==false)
				{
					guider_text_webview.setVisibility(View.VISIBLE);
					haveWebView = true;
				}
				else
				{
					guider_text_webview.setVisibility(View.INVISIBLE);
					haveWebView = false;
				}
			}
		});
		
		loadAssetHtml();
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
		guider_text_webview.loadUrl("file:///android_asset/HTML/html/guangzhou/yuexiugongyuan/guangzhoubowuguan.html");
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
