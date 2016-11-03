package com.example.adapter;

import java.util.List;

import com.example.model.Garbageinfo;
import com.example.phonemanager.R;

import android.content.Context;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GarbageAdapter extends BaseAdapter {
	Context context;
	public List<Garbageinfo> info;
	LayoutInflater layout;
	GarbageAdapter adapter;
	public GarbageAdapter(Context context, List<Garbageinfo> info) {
		super();
		this.context = context;
		this.info = info;
		this.layout = LayoutInflater.from(context);
		adapter=this;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return info.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return info.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View v, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHodler vh=null;
		if (v==null) {
			vh=new ViewHodler();
			v=layout.inflate(R.layout.garbage_disposal_item, null);
			v.setTag(vh);
		}else{
			vh=(ViewHodler) v.getTag();
		}
		
		Garbageinfo gf = info.get(position);
		vh.img_select=(ImageView) v.findViewById(R.id.img_select);
		vh.img_icon=(ImageView) v.findViewById(R.id.img_icon);
		vh.tv_name=(TextView) v.findViewById(R.id.tv_file_name);
		vh.tv_apkname=(TextView) v.findViewById(R.id.tv_file_time);
		vh.tv_file_size=(TextView) v.findViewById(R.id.tv_file_size);
		vh.img_icon.setImageDrawable(gf.getDrawable());
		vh.tv_name.setText(gf.getName());
		//Í¼Æ¬Ñ¡Ôñ
		if (gf.isIsselect()) {
			vh.img_select.setImageResource(R.drawable.radio_select_yes);
		}else{
			vh.img_select.setImageResource(R.drawable.radio_select_no);
		}
		vh.img_select.setOnClickListener(new MyOnClick(position));
		if (gf.getApkname().length()>=15) {
			vh.tv_apkname.setText(gf.getApkname().substring(0, 15)+"...");
		}else{
			vh.tv_apkname.setTag(gf.getApkname());
		}
		vh.tv_file_size.setText(Formatter.formatFileSize(context, gf.getFilesize()));
		return v;
	}
	class MyOnClick implements OnClickListener{
		int index;

		public MyOnClick(int index) {
			super();
			this.index = index;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Garbageinfo garbageinfo = info.get(index);
			if (garbageinfo.isIsselect()) {
				garbageinfo.setIsselect(false);
			}else{
				garbageinfo.setIsselect(true);
			}
			adapter.notifyDataSetChanged();//shuaxin
		}
	}
static class ViewHodler{
	ImageView img_select;
	ImageView img_icon;
	TextView tv_name;
	TextView tv_apkname;
	TextView tv_file_size;
}
}
