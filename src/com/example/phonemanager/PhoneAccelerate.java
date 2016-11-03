package com.example.phonemanager;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import com.example.adapter.AccelerateAdapter;
import com.example.model.Taskinfo;
import com.example.utils.PhoneUtils;
import com.example.view.MyDialog;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * 手机加速
 * **/
public class PhoneAccelerate extends Activity implements OnClickListener {
	//异步回调  通知刷新
	Handler mhandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				MyDialog.dismiss();
				showtaskinfo.clear();
				if (index==0) {
					showtaskinfo.addAll(usertaskinfo);
				}else if (index==1) {
					showtaskinfo.addAll(systemtaskinfo);
				}
				adapter.notifyDataSetChanged(); //刷新数据
				pb.setIndeterminate(false);
				break;

			default:
				break;
			}
		};
	};
	private List<Taskinfo> showtaskinfo;
	// 用户app
	private List<Taskinfo> usertaskinfo;
	// 系统app
	private List<Taskinfo> systemtaskinfo;
	//0表示用户  1表示系统
	private int index=0;
	private AccelerateAdapter adapter;
	private TextView tv_brand;
	private TextView tv_version;
	private TextView tv_memory;
	private TextView tv_memory2;
	private ProgressBar pb;
	private ListView listview;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.phone_accelerate);
		
		initHead();
			initView();

	}

	private void initView()  {
		// TODO Auto-generated method stub
		RadioGroup rg=(RadioGroup) findViewById(R.id.rg);
		rg.check(R.id.bt_user);
		findViewById(R.id.bt_user).setOnClickListener(this);
		findViewById(R.id.bt_system).setOnClickListener(this);
		findViewById(R.id.bt_clear).setOnClickListener(this);
		listview = (ListView) findViewById(R.id.lv_app);
		pb = (ProgressBar) findViewById(R.id.pb);
		tv_brand = (TextView) findViewById(R.id.tv_brand);
		tv_version = (TextView) findViewById(R.id.tv_version);
		tv_memory = (TextView) findViewById(R.id.tv_memory);
		tv_memory2 = (TextView) findViewById(R.id.tv_memory2);
		loadData();
	}
	public void loadData(){
		tv_brand.setText(PhoneUtils.getBrand());
		tv_version.setText(PhoneUtils.getVersion());
		// 总内存
		long maxMemory = 0;
		try {
			maxMemory = PhoneUtils.getMaxMemory();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 空闲内存
		long freeMemory = PhoneUtils.getFreeMemory(this);
		long number = maxMemory - freeMemory;
		double progress = Double.valueOf(number + "")
				/ Double.valueOf(maxMemory + "") * 100;
		pb.setProgress((int) progress);
		tv_memory.setText("已用:" + Formatter.formatFileSize(this, number));
		tv_memory2.setText("/" + Formatter.formatFileSize(this, freeMemory)
				+ "可用");
		List<Taskinfo> taskinfo = PhoneUtils.getTaskinfo(this);
		showtaskinfo = new ArrayList<Taskinfo>();// new 数据
		adapter = new AccelerateAdapter(showtaskinfo, this);
		listview.setAdapter(adapter);
		MyDialog.ShowDialog(this);
		pb.setIndeterminate(true);
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				List<Taskinfo> taskinfo = PhoneUtils
						.getTaskinfo(PhoneAccelerate.this);
				usertaskinfo = new ArrayList<Taskinfo>();
				systemtaskinfo = new ArrayList<Taskinfo>();
				for (int i = 0; i < taskinfo.size(); i++) {
					Taskinfo task = taskinfo.get(i);
					if (task != null) {
						if (task.isIsuserapp() == true) {
							systemtaskinfo.add(task);
						} else {
							task.setIsselect(true);
							usertaskinfo.add(task);
						}
					}
				}
				mhandler.sendEmptyMessage(1);
			}
		}).start();
	}
	private void initHead() {
		// TODO Auto-generated method stub
		ImageView head_left = (ImageView) findViewById(R.id.head_left);
		head_left.setImageResource(R.drawable.back);
		head_left.setOnClickListener(this);
		TextView head_tv = (TextView) findViewById(R.id.head_tv);
		head_tv.setText("手机加速");
		ImageButton head_right = (ImageButton) findViewById(R.id.head_right);
		head_right.setVisibility(View.GONE);
	}

	// 结束当前页面返回键
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.head_left) {
			this.finish();
		}else if (v.getId()==R.id.bt_user) {
			index=0;
			mhandler.sendEmptyMessage(1);
		}else if (v.getId()==R.id.bt_system) {
			index=1;
			mhandler.sendEmptyMessage(1);
		}else if (v.getId()==R.id.bt_clear) {
			PhoneUtils.killApp(usertaskinfo);
			loadData();
		}
	}
}
