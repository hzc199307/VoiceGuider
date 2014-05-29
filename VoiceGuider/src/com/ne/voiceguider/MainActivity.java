package com.ne.voiceguider;

import com.ne.voiceguider.fragment.HikingFragment;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
	
	private ViewPager viewPager;//ҳ������
	private MyFragmentPagerAdapter mMyFragmentPagerAdapter;
	private ImageView imageView;// ����ͼƬ
	private TextView textView1,textView2,textView3;
	private int offset = 0;// ����ͼƬƫ����
	private int currIndex = 0;// ��ǰҳ�����
	private int bmpW;// ����ͼƬ���

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		InitImageView();
		InitTextView();
		InitViewPager();
	}

	private void InitViewPager() {
		viewPager=(ViewPager) findViewById(R.id.main_viewPager);
		mMyFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager() );    
		viewPager.setAdapter(mMyFragmentPagerAdapter);  
		viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}
	 
	/**
	  *  ��ʼ�����²���ͷ��
	  */
	private void InitTextView() {
		textView1 = (TextView) findViewById(R.id.main_bottom_text1);
		textView2 = (TextView) findViewById(R.id.main_bottom_text2);
		textView3 = (TextView) findViewById(R.id.main_bottom_text3);

		textView1.setOnClickListener(new MyOnClickListener(0));
		textView2.setOnClickListener(new MyOnClickListener(1));
		textView3.setOnClickListener(new MyOnClickListener(2));
	}

	
	/**
	 * ��ʼ�����²��Ķ���
	 **/
	private void InitImageView() {
		imageView= (ImageView) findViewById(R.id.main_cursor);
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.main_cursor).getWidth();// ��ȡͼƬ���
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// ��ȡ�ֱ��ʿ��
		offset = (screenW / 3 - bmpW) / 2;// ����ƫ����
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		imageView.setImageMatrix(matrix);// ���ö�����ʼλ��
	}

	/** 
	 * �²�ͷ�������� 3    
	 **/
	private class MyOnClickListener implements OnClickListener{
        private int index=0;
        public MyOnClickListener(int i){
        	index=i;
        }
		public void onClick(View v) {
			viewPager.setCurrentItem(index);			
		}
		
	}
	
	/**
	 * ���������ᱣ�����е�Fragment     ��FragmentStatePagerAdapterֻ�ᱣ��3��
	 * @ClassName: MyFragmentPagerAdapter 
	 * @Description: TODO 
	 * @author HeZhichao
	 * @date 2014��5��29�� ����3:57:35 
	 *
	 */
	public class MyFragmentPagerAdapter extends FragmentPagerAdapter{
		int NUM_ITEMS = 3;
		public MyFragmentPagerAdapter(FragmentManager fm ) {  
			super(fm);  
		}  

		@Override  
		public int getCount() {  
			return NUM_ITEMS;  
		}  

		@Override  
		public Fragment getItem(int position) {  
			// ������Ӧ��  fragment  
			switch(position)
			{
			case 0:return new HikingFragment();
			case 1:return new HikingFragment();//TODO
			case 2:return new HikingFragment();//TODO
			default:return null;
			}
		}  

		@Override  
		public void destroyItem(ViewGroup container, int position, Object object) {  
			super.destroyItem(container, position, object);  
		} 
	}

    public class MyOnPageChangeListener implements OnPageChangeListener{

    	int one = offset * 2 + bmpW;// ҳ��1 -> ҳ��2 ƫ����
		int two = one * 2;// ҳ��1 -> ҳ��3 ƫ����
		public void onPageScrollStateChanged(int arg0) {
				
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
				
		}

		public void onPageSelected(int arg0) {
			/*
			Animation animation = null;
			switch (arg0) {
			case 0:
				if (currIndex == 1) {
					animation = new TranslateAnimation(one, 0, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, 0, 0, 0);
				}
				break;
			case 1:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, one, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, one, 0, 0);
				}
				break;
			case 2:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, two, 0, 0);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, two, 0, 0);
				}
				break;
				
			}
			*/
			Animation animation = new TranslateAnimation(one*currIndex, one*arg0, 0, 0);
			currIndex = arg0;
			animation.setFillAfter(true);// True:ͼƬͣ�ڶ�������λ��
			animation.setDuration(300);
			imageView.startAnimation(animation);
			//Toast.makeText(null, "��ѡ����"+ viewPager.getCurrentItem()+"ҳ��", Toast.LENGTH_SHORT).show();
		}
    	
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
