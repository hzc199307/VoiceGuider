package com.ne.voiceguider.adapter;

import java.util.List;
import java.util.ArrayList;

import com.ne.voiceguider.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ne.voiceguider.bean.BigScene;
import com.ne.voiceguider.bean.CityBean;
import com.ne.voiceguider.dao.CitySceneDao;

public class BigSceneListAdapter extends BaseAdapter{


	private final String TAG = "BigSceneListAdapter";
	private List<BigScene> listData = null;
	private LayoutInflater inflater = null;
	

	public BigSceneListAdapter(Context context,int cityID) {
		CitySceneDao mCitySceneDao = new CitySceneDao(context);
		setListData(mCitySceneDao.getBigScenes(cityID));
		inflater = LayoutInflater.from(context);
		Log.v(TAG, listData.size()+"");
	}
	public BigSceneListAdapter(Context context,List<BigScene> list) {
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
			holder.city_scene_name_textview = (TextView) convertView.findViewById(R.id.city_scene_name_textview);	
			convertView.setTag(holder);// 如果是新产生的view，则设置tag
		} 
		else
		{
			holder = (Holder) convertView.getTag();// 如果是使用已经存在的view，则从tag中获取就可以了
		}
		BigScene myBigScene = listData.get(position);
		if (myBigScene != null) {
			// 对相应的控件赋值
			holder.city_scene_name_textview.setText(myBigScene.getBigSceneName());
		}
		else {
			holder.city_scene_name_textview.setText("error");
		}
		return convertView;
	}

	final class Holder {
		public TextView city_scene_name_textview;
	}

	/**
	 * 添加列表项
	 * @param item
	 */
	public void addItem(BigScene item) {
		this.listData.add(item);
	}



}