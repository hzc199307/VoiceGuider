package com.ne.voiceguider;


import java.io.IOException;

import com.ne.voiceguider.R;
import com.ne.voiceguider.DBHelper.DBHelper;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * @ClassName: MainActivity 
 * @Description: TODO 
 * @author HeZhichao
 * @date 2014年5月15日 下午3:18:51 
 *
 */
public class FirstActivity extends Activity {

	private final String TAG = "FirstActivity";
	protected boolean _active = true;
	protected int _splashTime = 3000;

	final ScaleAnimation animation =new ScaleAnimation(1.0f, 1.25f, 1.0f, 1.25f, 
			Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f); 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_first);
		Log.v(TAG,"onCreate");
		Thread splashTread = new Thread() {
			@Override
			public void run() {
				try {
					int waited = 0;
					while(_active && (waited < _splashTime)) {
						sleep(100);
						if(_active) {
							waited += 100;
						}
					}
				} catch(InterruptedException e) {
					// do nothing
				} finally {
					finish();
					// 启动主应用
					startActivity(new Intent(FirstActivity.this,MainActivity.class)); //startActivity(new Intent("com.ne.voiceguider.TabsActivity"));
				}
			}
		};
		splashTread.start();
		onDB();
		ImageView mImageView = (ImageView)findViewById(R.id.helloImage);
		animation.setDuration(3000);//设置动画持续时间 
		animation.setFillAfter(true);//动画执行完后是否停留在执行完的状态  
		mImageView.setAnimation(animation); 
		/** 开始动画 */ 
		animation.startNow();

		//		new Handler().postDelayed(new Runnable() {
		//			@Override
		//			public void run() {
		//				
		//				Intent intent = new Intent(MainActivity.this,
		//						TabsActivity.class); // 跳转到主页面
		//				Log.v("1","1");
		//				startActivity(intent);
		//				MainActivity.this.finish();
		//
		//			}
		//		}, 1000);// 停留1秒
	}

	/**
	 * 对数据库的操作
	 */
	public void onDB() {
		DBHelper mDBHelper = new DBHelper(this);
		TextView mTextView = (TextView) findViewById(R.id.hello);
		try {
			if(mDBHelper.createDataBase())

				;//mTextView.setText("hashahahhahahha");
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		Cursor mCursor = mDBHelper
				.query("select * from city where cityid=1 ",
						null);
		mCursor.moveToFirst();
		//mTextView.setText(mCursor.getString(2));
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.v(TAG,"onDestroy");
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			_active = false;
		}
		return true;
	}

}
