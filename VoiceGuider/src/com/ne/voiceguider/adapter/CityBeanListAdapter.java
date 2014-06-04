package com.ne.voiceguider.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.ne.voiceguider.R;
import com.ne.voiceguider.bean.CityBean;
import com.ne.voiceguider.dao.CitySceneDao;

public class CityBeanListAdapter extends BaseAdapter{
	private final String TAG = "CityBeanListAdapter";
	private List<CityBean> listData = null;
	private LayoutInflater inflater = null;
	private Context mContext;
	int size = 0;

	public CityBeanListAdapter(Context context) {
		mContext = context;
		CitySceneDao mCitySceneDao = new CitySceneDao(context);
		setListData(mCitySceneDao.getCityBeans());
		inflater = LayoutInflater.from(context);
		Log.v(TAG, listData.size()+"");
	}
	public CityBeanListAdapter(Context context,List<CityBean> list) {
		setListData(list);
		inflater = LayoutInflater.from(context);
	}

	public void setListData(List<CityBean> list){
		if (list != null) {
			listData = list;
		}else {
			listData = new ArrayList<CityBean>();
		}
		size = getCount();
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

		if (convertView == null) {// 这样做可以使view循环利用，而不会有多少个item就产生多少个view
			holder = new Holder();
			convertView = inflater.inflate(R.layout.item_citybean, null);// 引用布局文件
			holder.helloImage = (ImageView)convertView.findViewById(R.id.helloImage);	
			convertView.setTag(holder);// 如果是新产生的view，则设置tag
		} 
		else
		{
			holder = (Holder) convertView.getTag();// 如果是使用已经存在的view，则从tag中获取就可以了
		}
		CityBean myCityBean = listData.get(position);
		int id = mContext.getResources().getIdentifier("city_"+myCityBean.getCityPinyin() ,"drawable","com.ne.voiceguider");
		holder.helloImage.setImageResource(id);
		return convertView;
	}

	final class Holder {
		public ImageView helloImage;
	}

	/**
	 * 添加列表项
	 * @param item
	 */
	public void addItem(CityBean item) {
		this.listData.add(item);
	}


	public CityBean getNearestCity(double lat,double longt)
	{
		double min=-1;
		int minIndex = 0;
		double distance = 0;
		CityBean mCityBean ; 
		for(int i=0;i<size;i++)
		{
			mCityBean = listData.get(i);
			distance = Math.sqrt((lat-mCityBean.getLatitude())*(lat-mCityBean.getLatitude())
					+(longt-mCityBean.getLongtitude())*(longt-mCityBean.getLongtitude()));
			if(distance<min)
			{
				minIndex = i;
				min = distance;
			}
		}
		return listData.get(minIndex);
	}


}