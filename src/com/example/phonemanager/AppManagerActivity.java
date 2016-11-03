package com.example.phonemanager;

import java.util.ArrayList;
import java.util.List;

import com.example.adapter.AppinfoAdapter;
import com.example.model.Appinfo;
import com.example.utils.PhoneUtils;
import com.example.view.MyDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 软件管理
 * **/
public class AppManagerActivity extends Activity implements OnClickListener {
	
	Handler mhandler=new Handler(){ 
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
//				MyDialog.dismiss();
				showinfos.clear();
				if (index==0) {
					showinfos.addAll(appinfoall);
				}else if (index==1) {
					showinfos.addAll(appinfosystem);
				}else if (index==2) {
					showinfos.addAll(appinfouser);
				}
				adapter.notifyDataSetChanged();
				break;

			default:
				break;
			}
		};
	};
	private ImageView img_manager;
	List<Appinfo> showinfos;
	List<Appinfo> appinfoall;
	List<Appinfo> appinfosystem;
	List<Appinfo> appinfouser;
	//0所有 1系统 2用户
	private int index=0;
	private AppinfoAdapter adapter;
	private Animation loadAnimation;
	private ListView lv_appmanager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_manager_activity);
		initHead();
		initView();
//		Toast.makeText(this, Build.VERSION.SDK, 1).show();//api版本
	}

	private void initView() {
		// TODO Auto-generated method stub
		// 动画旋转
		img_manager = (ImageView) findViewById(R.id.img_manager);
		loadAnimation = AnimationUtils.loadAnimation(
				AppManagerActivity.this, R.anim.manager_rotate);
		loadAnimation.setFillAfter(true);// 什么意思   不停止
		img_manager.startAnimation(loadAnimation);
		ProgressBar pb_in = (ProgressBar) findViewById(R.id.pb_in);
		ProgressBar pb_out = (ProgressBar) findViewById(R.id.pb_out);
		TextView tv_memory_in1 = (TextView) findViewById(R.id.tv_memory_in1);
		TextView tv_memory_in2 = (TextView) findViewById(R.id.tv_memory_in2);
		TextView tv_memory_out = (TextView) findViewById(R.id.tv_memory_out);
		// 内置 可用存储空间
		long builtinAvailable = PhoneUtils.getBuiltinAvailable();
		// 内置 最大存储空间
		long builtinAvailableMax = PhoneUtils.getBuiltinAvailableMax();
		// 已用的=最大-可用
		long builtinhasbeenused = builtinAvailableMax - builtinAvailable;
		// 百分比
		double progress = Double.valueOf("" + builtinhasbeenused)
				/ Double.valueOf("" + builtinAvailableMax) * 100;
		pb_in.setProgress((int) progress);// 强转int
		tv_memory_in1.setText("已用"
				+ Formatter.formatFileSize(this, builtinhasbeenused));
		tv_memory_in2.setText("/"
				+ Formatter.formatFileSize(this, builtinAvailable));
		// 外置
		// 可用
		long externalAvailable = PhoneUtils.getExternalAvailable();
		// 最大
		long externalAvailableMax = PhoneUtils.getExternalAvailableMax();
		if (externalAvailable == 0 && externalAvailableMax == 0) {
			pb_out.setProgress(0);// 进度
			tv_memory_out.setText("请插入存储卡");
		} else {
			// 已用
			long externalhasbeenused = externalAvailableMax - externalAvailable;
			double progress1 = Double.valueOf("" + externalhasbeenused)
					/ Double.valueOf("" + externalAvailableMax) * 100;
			pb_out.setProgress((int) progress1);
			tv_memory_out.setText("已用"
					+ Formatter.formatFileSize(this, externalhasbeenused) + "/"
					+ Formatter.formatFileSize(this, externalAvailableMax));
		}
		//显示程序
		findViewById(R.id.rb_managerall).setOnClickListener(this);
		findViewById(R.id.rb_managersystem).setOnClickListener(this);
		findViewById(R.id.rb_manageruser).setOnClickListener(this);
		lv_appmanager = (ListView) findViewById(R.id.lv_appmanager);
		showinfos=new ArrayList<Appinfo>();
		adapter = new AppinfoAdapter(showinfos, this);
		lv_appmanager.setAdapter(adapter);
		loadData();
		//监听listview
		lv_appmanager.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
			}
		});
		

	}
	private void loadData() {
		// TODO Auto-generated method stub
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				appinfoall = PhoneUtils.getAppinfo(AppManagerActivity.this);
				appinfosystem=new ArrayList<Appinfo>();
				appinfouser=new ArrayList<Appinfo>();
				for (int i = 0; i < appinfoall.size(); i++) {
					Appinfo appinfo = appinfoall.get(i);
					if (appinfo.isIsuserapp()==true) {
						 appinfosystem.add(appinfo);
					}else{
						appinfouser.add(appinfo);
					}
				}
				mhandler.sendEmptyMessage(1);
			}
		}).start();
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
		head_tv.setText("软件管理");
		ImageButton head_right = (ImageButton) findViewById(R.id.head_right);
		head_right.setVisibility(View.GONE);// 隐藏右边图片
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.head_left) {
			this.finish();
		}else if (v.getId()==R.id.rb_managerall) {
			index=0;
			mhandler.sendEmptyMessage(1);
		}else if (v.getId()==R.id.rb_managersystem) {
			index=1;
			mhandler.sendEmptyMessage(1);
		}else if (v.getId()==R.id.rb_manageruser) {
			index=2;
			mhandler.sendEmptyMessage(1);
		}
	}
	

}
