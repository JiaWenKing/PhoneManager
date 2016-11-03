package com.example.adapter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.example.model.Fileinfo;
import com.example.phonemanager.R;
import com.example.utils.FileManager;
import com.example.utils.PhoneUtils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.util.LruCache;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 文件展示适配器
 * */
public class FileShowAdapter extends BaseAdapter {
	Context context;
	List<Fileinfo> list; 
	LayoutInflater layout;
	private Resources res;
	private SimpleDateFormat fmat;
	private LruCache<String, Bitmap> lru;
	public List<Fileinfo> select_list;
	FileShowAdapter adapter;
	public FileShowAdapter(Context context, List<Fileinfo> list) {
		super(); 
		this.context = context;
		this.list = list;
		this.layout =LayoutInflater.from(context);
		res = context.getResources();
		fmat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");//时间
		lru = new LruCache<String, Bitmap>(1024*10);//
		select_list=new ArrayList<Fileinfo>();
		adapter=this;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
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
			v=layout.inflate(R.layout.file_show_item, null);
			v.setTag(vh);
		}else {
			vh=(ViewHodler) v.getTag();
		}
		Fileinfo info = list.get(position);//拿到对象
		vh.img_select=(ImageView) v.findViewById(R.id.img_select);
		vh.img_select.setOnClickListener(new MyOnClick(position));//单独监听
		vh.img_icon=(ImageView) v.findViewById(R.id.img_icon);
		vh.tv_file_name=(TextView) v.findViewById(R.id.tv_file_name);
		vh.tv_file_time=(TextView) v.findViewById(R.id.tv_file_time);
		vh.tv_file_size=(TextView) v.findViewById(R.id.tv_file_size);
		//选中文件
		if (info.isIsselect()==true) {
			vh.img_select.setImageResource(R.drawable.radio_select_yes);
		}else{
			vh.img_select.setImageResource(R.drawable.radio_select_no);
		}
		File f = info.getF();
		//文件图片
		if (info.getFileType().equals(FileManager.TYPE_IMAGE)) {
			Bitmap bitmap = lru.get(f.getAbsolutePath());
			if (bitmap!=null) {
				vh.img_icon.setImageBitmap(bitmap);//加载过一次后再进从内存拿图片
			}else {
				Bitmap loadBitmap = PhoneUtils.loadBitmap(f.getAbsolutePath(), 40, 40, context);
				vh.img_icon.setImageBitmap(loadBitmap);
				if(f.getAbsolutePath()!= null && loadBitmap != null){
				lru.put(f.getAbsolutePath(), loadBitmap);
				}//如果不等于空
			}
			
			
		}else{
			int resid = res.getIdentifier(info.getFileiocn(), "drawable", context.getPackageName());
			if (resid<=0) {
				resid=R.drawable.ic_system;
			}
			Drawable drawable = res.getDrawable(resid);
			vh.img_icon.setImageDrawable(drawable);
		}
		//文件名
		if (f.getName().length()>=20) {
			vh.tv_file_name.setText(f.getName().subSequence(0, 20)+"...");
		}else{
			vh.tv_file_name.setText(f.getName());
		}
		//时间
		long lastModified = f.lastModified();
		vh.tv_file_time.setText(fmat.format(lastModified));
		//文件大小
		vh.tv_file_size.setText(Formatter.formatFileSize(context, f.length()));
		return v;
	}
	static class ViewHodler{
		ImageView img_select;
		ImageView img_icon;
		TextView tv_file_name;
		TextView tv_file_time;
		TextView tv_file_size;
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
			Fileinfo fileinfo = list.get(index);
			if (fileinfo.isIsselect()) {
				fileinfo.setIsselect(false);//点击变未选中
				boolean contains = select_list.contains(fileinfo);
				if (contains) {
					select_list.remove(fileinfo);
				}
			}else{
				fileinfo.setIsselect(true);//未点击变选中
				select_list.add(fileinfo);
			}
			adapter.notifyDataSetChanged();
		}
		
	}

}
