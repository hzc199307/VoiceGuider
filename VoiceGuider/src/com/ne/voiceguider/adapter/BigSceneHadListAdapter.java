package com.ne.voiceguider.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ne.voiceguider.R;
import com.ne.voiceguider.bean.BigScene;
import com.ne.voiceguider.dao.CitySceneDao;

public class BigSceneHadListAdapter extends BaseAdapter{
	
		private final String TAG = "BigSceneHadListAdapter";
		private List<BigScene> listData = null;
		private LayoutInflater inflater = null;

		public BigSceneHadListAdapter(Context context,int cityID) {
			CitySceneDao mCitySceneDao = new CitySceneDao(context);
			setListData(mCitySceneDao.getBigScenes(cityID));
			inflater = LayoutInflater.from(context);
			Log.v(TAG, listData.size()+"");
		}
		public BigSceneHadListAdapter(Context context,List<BigScene> list) {
			setListData(list);
			inflater = LayoutInflater.from(context);
		}

		public void setListData(List<BigScene> list){
			if (list != null) {
				listData = list;
			}else {
				listData = new ArrayList<BigScene>();
			}
		}
		@Override
		public int getCount() {
			return listData.size();
		}

		@Override
		public BigScene getItem(int position) {
			if(listData == null) return null;
			return listData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			//TODO
			Holder holder = null;

			if (convertView == null) {// 这样做可以使view循环利用，而不会有多少个item就产生多少个view
				holder = new Holder();
				convertView = inflater.inflate(R.layout.item_bigscene, null);// 引用布局文件
				holder.city_scene_textview = (TextView) convertView.findViewById(R.id.city_scene_textview);	
				convertView.setTag(holder);// 如果是新产生的view，则设置tag
			} 
			else
			{
				holder = (Holder) convertView.getTag();// 如果是使用已经存在的view，则从tag中获取就可以了
			}
			BigScene myBigScene = listData.get(position);
			if (myBigScene != null) {
				// 对相应的控件赋值
				holder.city_scene_textview.setText(myBigScene.getBigSceneName());
			}
			else {
				holder.city_scene_textview.setText("error");
			}
			return convertView;
		}

		final class Holder {
			public TextView city_scene_textview;
		}

		/**
		 * 添加列表项
		 * @param item
		 */
		public void addItem(BigScene item) {
			this.listData.add(item);
		}
		
}
