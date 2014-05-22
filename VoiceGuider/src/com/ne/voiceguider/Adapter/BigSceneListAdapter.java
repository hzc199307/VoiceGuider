package com.ne.voiceguider.Adapter;

import java.util.List;
import java.util.ArrayList;

import com.ne.voiceguider.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ne.voiceguider.bean.BigScene;

public class BigSceneListAdapter extends BaseAdapter{


	private List<BigScene> listData = null;
	private LayoutInflater inflater = null;

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
	public Object getItem(int position) {
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

		if (convertView == null) {// ����������ʹviewѭ�����ã��������ж��ٸ�item�Ͳ������ٸ�view
			holder = new Holder();
			convertView = inflater.inflate(R.layout.item_bigscene, null);// ���ò����ļ�
			holder.city_scene_textview = (TextView) convertView.findViewById(R.id.city_scene_textview);	
			convertView.setTag(holder);// ������²�����view��������tag
		} 
		else
		{
			holder = (Holder) convertView.getTag();// �����ʹ���Ѿ����ڵ�view�����tag�л�ȡ�Ϳ�����
		}
		BigScene myBigScene = listData.get(position);
		if (myBigScene != null) {
			// ����Ӧ�Ŀؼ���ֵ
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
	 * ����б���
	 * @param item
	 */
	public void addItem(BigScene item) {
		this.listData.add(item);
	}



}