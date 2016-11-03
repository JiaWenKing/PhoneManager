package com.example.phonemanager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.example.adapter.GarbageAdapter;
import com.example.model.Garbageinfo;
import com.example.utils.PhoneUtils;
import com.example.view.MyDialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 垃圾清理
 * **/
public class GarbageDisposalActivity extends Activity implements
		OnClickListener {
	long filemax=0;
	Handler mhandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1: 
				long filesize=(Long) msg.obj;
				filemax=filemax+filesize;
				tv_garbagemax.setText(Formatter.formatFileSize(GarbageDisposalActivity.this, filemax));
				
				break;
			case 2:
				MyDialog.dismiss();
				gfinfo.clear();
				List<Garbageinfo> garbageinfo=(List<Garbageinfo>) msg.obj;
				gfinfo.addAll(garbageinfo);
				adapter.notifyDataSetChanged();
				break;
			default:
				break;
			}
		};
	};
	private TextView tv_garbagemax;
	private ListView lv_garbage_show;
	private List<Garbageinfo> gfinfo;
	private GarbageAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.garbage_disposal_activity);
		gfinfo =new ArrayList<Garbageinfo>();
		initHead();
		initView();
		loadData();
	}
	public void loadData(){
		// 耗时操作开线程
		MyDialog.ShowDialog(this);
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						
						// TODO Auto-generated method stub
						String path = PhoneUtils.getBuiltinPath() + "/address.db";// 获得路径
						try {
							InputStream is = getResources().getAssets().open("address.db");// 打开文件
																						// 拿到的是一个流
							PhoneUtils.copyDb(is, path);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						List<Garbageinfo> garbageinfo = PhoneUtils.getGarbageinfo(path, GarbageDisposalActivity.this);
						for (int i = 0; i < garbageinfo.size(); i++) {
							
							Garbageinfo gf = garbageinfo.get(i);
							File f=new File(gf.getFilepath());
							long filesize = PhoneUtils.filesize(f);
							gf.setFilesize(filesize);
							Message message1 = mhandler.obtainMessage();
							message1.what=1;
							message1.obj=filesize;
							mhandler.sendMessage(message1);
						}
						Message message2 = mhandler.obtainMessage();
						message2.what=2;
						message2.obj=garbageinfo;
						mhandler.sendMessage(message2);
					}
				}).start();
	}
	private void initView() {
		// TODO Auto-generated method stub
		
		
		tv_garbagemax = (TextView) findViewById(R.id.tv_garbagemax);
		lv_garbage_show = (ListView) findViewById(R.id.lv_garbage_show);
		findViewById(R.id.bt_clear1).setOnClickListener(this);
		adapter = new GarbageAdapter(this, gfinfo);
		lv_garbage_show.setAdapter(adapter);
	}
	
	

	/**
	 * 头部
	 * **/
	private void initHead() {
		// TODO Auto-generated method stub
		ImageView head_left = (ImageView) findViewById(R.id.head_left);
		head_left.setImageResource(R.drawable.back);
		head_left.setOnClickListener(this);
		TextView head_tv = (TextView) findViewById(R.id.head_tv);
		head_tv.setText("垃圾清理");
		ImageButton head_right = (ImageButton) findViewById(R.id.head_right);
		head_right.setVisibility(View.GONE);// 隐藏右边图片
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.head_left) {
			this.finish();
		}else if (v.getId()==R.id.bt_clear1) {
			filemax=0;
			for (int i = 0; i < adapter.info.size(); i++) {
				Garbageinfo gf = adapter.info.get(i);
				if (gf.isIsselect()) {
					File f=new File(gf.getFilepath());
					PhoneUtils.deletefile(f);
					gf.setFilesize(0);
					gf.setIsselect(false);
				}
				filemax=filemax+gf.getFilesize();//刷新
			}
			//刷新
			tv_garbagemax.setText(Formatter.formatFileSize(GarbageDisposalActivity.this, filemax));
			adapter.notifyDataSetChanged();
			
			
		}
	}
}
