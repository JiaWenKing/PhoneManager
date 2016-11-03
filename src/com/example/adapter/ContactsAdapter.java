package com.example.adapter;

import java.util.List;

import com.example.model.Contentinfo;
import com.example.phonemanager.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
/**
 * 联系人适配器
 * **/
public class ContactsAdapter extends BaseAdapter {
	List<Contentinfo> list;
	private Context context;
	LayoutInflater layout;

	public ContactsAdapter(List<Contentinfo> list, Context context) {
		super();
		this.list = list;
		this.context = context;
		this.layout =LayoutInflater.from(context);//初始化布局加载器
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);//下标
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View v, ViewGroup parent) {
		// TODO Auto-generated method stub
		Contentinfo contentinfo = list.get(position);
		v=layout.inflate(R.layout.contacts_item, null);
		TextView tv_name=(TextView) v.findViewById(R.id.tv_name);
		TextView tv_number=(TextView) v.findViewById(R.id.tv_number);
		tv_name.setText(contentinfo.getName());
		tv_number.setText(contentinfo.getNumber());
		return v;
	}

}
