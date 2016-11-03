package com.example.adapter;

import java.util.List;

import com.example.model.Taskinfo;
import com.example.phonemanager.R;

import android.content.Context;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 手机加速适配器
 * **/
public class AccelerateAdapter extends BaseAdapter {
	List<Taskinfo> infos;
	LayoutInflater layout;
	Context context;
	private AccelerateAdapter adapter;

	public AccelerateAdapter(List<Taskinfo> infos, Context context) {
		super();
		this.infos = infos;
		this.context = context;
		this.layout = LayoutInflater.from(context);
		adapter = this;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return infos.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return infos.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View v, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHodler vh = null;
		if (v == null) {
			vh = new ViewHodler();
			v = layout.inflate(R.layout.accelerate_item, null);
			v.setTag(vh);
		} else {
			vh = (ViewHodler) v.getTag();
		}
		Taskinfo taskinfo = infos.get(position);
		vh.img_select = (ImageView) v.findViewById(R.id.img_select);
		vh.img_app = (ImageView) v.findViewById(R.id.img_app);
		vh.app_name = (TextView) v.findViewById(R.id.app_name);
		vh.app_memory = (TextView) v.findViewById(R.id.app_memory);
		if (taskinfo.isIsselect()) {
			vh.img_select.setImageResource(R.drawable.radio_select_yes);
		} else {
			vh.img_select.setImageResource(R.drawable.radio_select_no);
		}
		vh.img_select.setOnClickListener(new MyOnClick(position));
		vh.img_app.setImageDrawable(taskinfo.getDrawable());
		vh.app_name.setText(taskinfo.getAppname());
		vh.app_memory.setText(Formatter.formatFileSize(context,
				taskinfo.getMemory()));

		return v;
	}

	static class ViewHodler {
		ImageView img_select;
		ImageView img_app;
		TextView app_name;
		TextView app_memory;

	}

	class MyOnClick implements OnClickListener {
		int index;

		public MyOnClick(int index) {
			super();
			this.index = index;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Taskinfo taskinfo = infos.get(index);
			if (taskinfo.isIsselect()) {
				taskinfo.setIsselect(false);
			} else {
				taskinfo.setIsselect(true);
			}
			adapter.notifyDataSetChanged();
		}
	}
}
