package com.example.adapter;

import java.util.List;

import com.example.model.Appinfo;
import com.example.phonemanager.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * »Ìº˛π‹¿Ìapp  ≈‰∆˜
 * **/
public class AppinfoAdapter extends BaseAdapter {
	private List<Appinfo> appinfos;
	private Context context;
	private LayoutInflater layout;
	public AppinfoAdapter(List<Appinfo> appinfos, Context context) {
		super();
		this.appinfos = appinfos;
		this.context = context;
		this.layout=LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return appinfos.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return appinfos.get(position);
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
			v=layout.inflate(R.layout.appinfo_item, null);
			v.setTag(vh);
		}else {
			vh=(ViewHodler) v.getTag();
		}
		vh.img_app=(ImageView) v.findViewById(R.id.img_app);
		vh.app_name=(TextView) v.findViewById(R.id.app_name);
		vh.app_packname=(TextView) v.findViewById(R.id.app_packname);
		vh.app_version=(TextView) v.findViewById(R.id.app_version);
		Appinfo appinfo = appinfos.get(position);
		vh.img_app.setImageDrawable(appinfo.getAppdrawable());
		vh.app_name.setText(appinfo.getAppname());
		if (appinfo.getPackname().length()>=15) {
			vh.app_packname.setText(appinfo.getPackname().subSequence(0, 15)+"...");
		}else{
			vh.app_packname.setText(appinfo.getPackname());
		}
		if (appinfo.getVersion().length()>=10) {
			vh.app_version.setText(appinfo.getVersion().subSequence(0, 10)+"...");
		}else{
			vh.app_version.setText(appinfo.getVersion());
		}
		return v;
	}
	static class ViewHodler{
		ImageView img_app;
		TextView app_name;
		TextView app_packname;
		TextView app_version;
	}

}
