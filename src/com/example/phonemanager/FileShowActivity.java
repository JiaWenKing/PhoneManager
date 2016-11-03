package com.example.phonemanager;

import java.io.File;
import java.util.List;

import com.example.adapter.FileShowAdapter;
import com.example.model.Fileinfo;
import com.example.utils.FileManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
/**
 * 文件管理——分类展现
 * **/
public class FileShowActivity extends Activity implements OnClickListener{
	private String head;
	private List<Fileinfo> info;
	private long infosize=0;
	private FileShowAdapter adapter;
	private FileManager fm;
	private String type;
	private TextView tv_show_number;
	private TextView tv_show_size;
	private AlertDialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.file_show_activity);
		initData();
		initHead();
		initView();
	}
	private void initView() {
		tv_show_number = (TextView) findViewById(R.id.tv_show_number);
		tv_show_size = (TextView) findViewById(R.id.tv_show_size);
		tv_show_number.setText(info.size()+"项");
		tv_show_size.setText(Formatter.formatFileSize(this, infosize));
		findViewById(R.id.bt_delete).setOnClickListener(this);//删除键监听
		
		ListView lv_file_show=(ListView) findViewById(R.id.lv_file_show);
		adapter = new FileShowAdapter(this, info);
		lv_file_show.setAdapter(adapter);//关联适配器
	}
	//初始化数据
	private void initData() {
		fm = FileManager.getFileManager();
		type = getIntent().getStringExtra("type");
		if (type.equals(FileManager.TYPE_ANY)) {
			head="所有文件";
			info=fm.Listall;
			infosize=fm.ListallSize;
		}else if (type.equals(FileManager.TYPE_APK)) {
			head="程序包";
			info=fm.Listapk;
			infosize=fm.ListapkSize;
		}else if (type.equals(FileManager.TYPE_AUDIO)) {
			head="音频";
			info=fm.Listaudio;
			infosize=fm.ListaudioSize;
		}else if (type.equals(FileManager.TYPE_IMAGE)) {
			head="图片";
			info=fm.Listimage;
			infosize=fm.ListimageSize;
		}else if (type.equals(FileManager.TYPE_TXT)) {
			head="文本";
			info=fm.Listtext;
			infosize=fm.ListtextSize;
		}else if (type.equals(FileManager.TYPE_VIDEO)) {
			head="视频";
			info=fm.Listvideo;
			infosize=fm.ListvideoSize;
		}else if (type.equals(FileManager.TYPE_ZIP)) {
			head="压缩包";
			info=fm.Listzip;
			infosize=fm.ListzipSize;
		}
	}

	/**
	 * 头
	 * */
	private void initHead() {
		ImageView head_left = (ImageView) findViewById(R.id.head_left);
		head_left.setImageResource(R.drawable.back);
		head_left.setOnClickListener(this);
		TextView head_tv = (TextView) findViewById(R.id.head_tv);
		head_tv.setText(head);
		ImageButton head_right = (ImageButton) findViewById(R.id.head_right);
		head_right.setVisibility(View.GONE);// 隐藏右边图片
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId()==R.id.bt_delete) {
			List<Fileinfo> select_list = adapter.select_list;
			for (int i = 0; i < select_list.size(); i++) {
				Fileinfo fileinfo = select_list.get(i);
				//每循环一次需要减去去掉的大小  得到当前的内存大小
				if (type.equals(FileManager.TYPE_ANY)) {
					fm.ListallSize=fm.ListallSize-fileinfo.getF().length();
					fm.Listall.remove(fileinfo);//...
				}else if (type.equals(FileManager.TYPE_APK)) {
					fm.ListapkSize=fm.ListapkSize-fileinfo.getF().length();
					fm.ListallSize=fm.ListallSize-fileinfo.getF().length();//...
					fm.Listall.remove(fileinfo);
				}else if (type.equals(FileManager.TYPE_AUDIO)) {
					fm.ListaudioSize=fm.ListaudioSize-fileinfo.getF().length();
					fm.ListallSize=fm.ListallSize-fileinfo.getF().length();
					fm.Listall.remove(fileinfo);
				}else if (type.equals(FileManager.TYPE_IMAGE)) {
					fm.ListimageSize=fm.ListimageSize-fileinfo.getF().length();
					fm.ListallSize=fm.ListallSize-fileinfo.getF().length();
					fm.Listall.remove(fileinfo);
				}else if (type.equals(FileManager.TYPE_TXT)) {
					fm.ListtextSize=fm.ListtextSize-fileinfo.getF().length();
					fm.ListallSize=fm.ListallSize-fileinfo.getF().length();
					fm.Listall.remove(fileinfo);
				}else if (type.equals(FileManager.TYPE_VIDEO)) {
					fm.ListvideoSize=fm.ListvideoSize-fileinfo.getF().length();
					fm.ListallSize=fm.ListallSize-fileinfo.getF().length();
					fm.Listall.remove(fileinfo);
				}else if (type.equals(FileManager.TYPE_ZIP)) {
					fm.ListzipSize=fm.ListzipSize-fileinfo.getF().length();
					fm.ListallSize=fm.ListallSize-fileinfo.getF().length();
					fm.Listall.remove(fileinfo);
				}
				info.remove(fileinfo);
				File f = fileinfo.getF();
				if (f.exists()) {//文件是否存在
					//添加选择框---------------------------------------------------------
					
						AlertDialog.Builder builder = new Builder(this);
						builder.setTitle("biubiubiu");// 头部
						builder.setMessage("是否删除");
						builder.setPositiveButton("确定",null);
					f.delete();//存在就删除
				}
				
			}
			adapter.notifyDataSetChanged();
			initData();//再刷新一次ui
			tv_show_number.setText(info.size()+"项");
			tv_show_size.setText(Formatter.formatFileSize(this, infosize));//改变总大小
		}else if (v.getId()==R.id.head_left) {
			Intent intent=new Intent();
			intent.putExtra("type", type);
			setResult(200, intent);
			this.finish();
		}//点击返回回调
	}
	//手机下部返回键
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode==4) {
			Intent intent=new Intent();
			intent.putExtra("type", type);
			setResult(200, intent);
			this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}
