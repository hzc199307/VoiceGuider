package com.ne.voiceguider.activity;

import com.ne.voiceguider.R;
import com.ne.voiceguider.R.id;
import com.ne.voiceguider.R.layout;
import com.ne.voiceguider.R.menu;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.os.Build;

/**
 * 
 * @ClassName: HikingActivity 
 * @Description: TODO 
 * @author HeZhichao
 * @date 2014年5月16日 上午11:18:57 
 *
 */
public class HikingActivity extends ActionBarActivity {


	private RelativeLayout hiking_city ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hiking);

		hiking_city = (RelativeLayout)findViewById(R.id.hiking_city);
		hiking_city.setOnClickListener(new View.OnClickListener() {

			@Override //TODO
			public void onClick(View arg0) {
				Intent intent = new Intent(HikingActivity.this,CityActivity.class); // 跳转到城市景点详情页面 
				Bundle bundle = new Bundle();                           //创建Bundle对象   
				bundle.putString("cityName", "广州市");     //装入数据   
				intent.putExtras(bundle);                            //把Bundle塞入Intent里面   
				startActivity(intent);                                     //开始切换 
			}
		});
	}
	class HikingCityOnClickListener implements RelativeLayout.OnClickListener
	{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(HikingActivity.this,
					CityActivity.class); // 跳转到主页面
			startActivity(intent);
		}

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.hiking, menu);
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




}
