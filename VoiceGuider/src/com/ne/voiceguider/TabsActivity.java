package com.ne.voiceguider;


import com.ne.voiceguider.R;
import com.ne.voiceguider.activity.HikingActivity;
import com.ne.voiceguider.activity.VoicePlayerActivity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;

public class TabsActivity extends TabActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tabs);

		Log.v("1","1");
		TabHost mTabHost = getTabHost();
		mTabHost.setup();

		mTabHost.addTab(mTabHost
				.newTabSpec("hiking")
				.setIndicator("发现旅程",
						getResources().getDrawable(R.drawable.selector_hiking))
				.setContent(new Intent(this, HikingActivity.class)));

		mTabHost.addTab(mTabHost
				.newTabSpec("mine")
				.setIndicator("我的",
						getResources().getDrawable(R.drawable.selector_hiking))
				.setContent(new Intent(this, HikingActivity.class)));

		mTabHost.addTab(mTabHost
				.newTabSpec("more")
				.setIndicator("更多",
						getResources().getDrawable(R.drawable.selector_hiking))
				.setContent(new Intent(this, VoicePlayerActivity.class)));
		

		mTabHost.setCurrentTabByTag("hiking");
	}

}
