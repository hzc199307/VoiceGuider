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
import com.ne.voiceguider.bean.CityBean;
import com.ne.voiceguider.dao.CitySceneDao;

public class CityHadListAdapter extends BaseAdapter{
	
		private final String TAG = "CityHadListAdapter";
		private List<CityBean> listData = null;
		private LayoutInflater inflater = null;

		public CityHadListAdapter(Context context) {
			CitySceneDao mCitySceneDao = new CitySceneDao(context);
			setListData(mCitySceneDao.getCityBeans());
			inflater = LayoutInflater.from(context);
			Log.v(TAG, listData.size()+"");
		}
		public CityHadListAdapter(Context context,List<CityBean> list) {
			setListData(list);
			inflater = LayoutInflater.from(context);
		}

		public void setListData(List<CityBean> list){
			if (list != null) {
				listData = list;
			}else {
				listData = new ArrayList<CityBean>();
			}
		}
		@Override
		public int getCount() {
			return listData.size();
		}

		@Override
		public CityBean getItem(int position) {
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
				convertView = inflater.inflate(R.layout.item_citybean, null);// ���ò����ļ�
				//holder.citybean_textview = (TextView) convertView.findViewById(R.id.citybean_textview);	
				convertView.setTag(holder);// ������²�����view��������tag
			} 
			else
			{
				holder = (Holder) convertView.getTag();// �����ʹ���Ѿ����ڵ�view�����tag�л�ȡ�Ϳ�����
			}
			CityBean myCityBean = listData.get(position);
//			if (myCityBean != null) {
//				// ����Ӧ�Ŀؼ���ֵ
//				holder.citybean_textview.setText(myCityBean.getCityName());
//			}
//			else {
//				holder.citybean_textview.setText("error");
//			}
			return convertView;
		}

		final class Holder {
			public TextView citybean_textview;
		}

		/**
		 * ����б���
		 * @param item
		 */
		public void addItem(CityBean item) {
			this.listData.add(item);
		}




}
