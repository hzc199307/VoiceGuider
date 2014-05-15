package com.ne.voiceguider;


import com.ne.voiceguider.R;
import com.ne.voiceguider.activity.HikingActivity;
import com.ne.voiceguider.activity.VoicePlayerActivity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;

/**
 * 
 * @ClassName: TabsActivity 
 * @Description: TODO 
 * @author HeZhichao
 * @date 2014��5��15�� ����5:28:10 
 *
 */
public class TabsActivity extends TabActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO �Զ����ɵķ������
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tabs);

		Log.v("1","1");
		TabHost mTabHost = getTabHost();
		mTabHost.setup();

		mTabHost.addTab(mTabHost
				.newTabSpec("hiking")
				.setIndicator("�����ó�",
						getResources().getDrawable(R.drawable.selector_hiking))
				.setContent(new Intent(this, HikingActivity.class)));

		mTabHost.addTab(mTabHost
				.newTabSpec("mine")
				.setIndicator("�ҵ�",
						getResources().getDrawable(R.drawable.selector_hiking))
				.setContent(new Intent(this, HikingActivity.class)));

		mTabHost.addTab(mTabHost
				.newTabSpec("more")
				.setIndicator("����",
						getResources().getDrawable(R.drawable.selector_hiking))
				.setContent(new Intent(this, VoicePlayerActivity.class)));
		

		mTabHost.setCurrentTabByTag("hiking");
	}

}
