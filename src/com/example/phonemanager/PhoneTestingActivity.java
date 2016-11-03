package com.example.phonemanager;

import java.io.IOException;

import com.example.utils.PhoneUtils;
import com.example.view.BatteryDialog;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 手机检测
 * **/
public class PhoneTestingActivity extends Activity implements OnClickListener {
	private BatteryBr br;
	private ProgressBar battery_pb;
	private View battery_viewtop;
	private TextView battery_tv;
	private int LEVEL;
	private int SCALE;
	private double wendu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.phone_testing);
		IntentFilter intentfilter = new IntentFilter();// 广播过滤器
		intentfilter.addAction(Intent.ACTION_BATTERY_CHANGED);
		br = new BatteryBr();
		// 注册广播
		registerReceiver(br, intentfilter);

		initHead();
		initView();
	}

	private void initView() {
		battery_pb = (ProgressBar) findViewById(R.id.battery_pb);// 电量
		battery_viewtop = findViewById(R.id.battery_viewtop);
//		battery_tv = (TextView) findViewById(R.id.battery_tv);
		battery_pb.setOnClickListener(this);
		//手机数据
		TextView top_1 = (TextView) findViewById(R.id.top_1);
		TextView bottom_1 = (TextView) findViewById(R.id.bottom_1);
		top_1.setText("设备名称:" + PhoneUtils.getBrand());
		bottom_1.setText("系统版本:" + PhoneUtils.getVersion());
		TextView top_2 = (TextView) findViewById(R.id.top_2);
		TextView bottom_2 = (TextView) findViewById(R.id.bottom_2);
		long maxMemory;
		try {
			maxMemory = PhoneUtils.getMaxMemory();
			top_2.setText("全部运行内存:" + Formatter.formatFileSize(this, maxMemory));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// long类型数值

		long freeMemory = PhoneUtils.getFreeMemory(this);
		bottom_2.setText("剩余运行内存:" + Formatter.formatFileSize(this, freeMemory));
		TextView top_3 = (TextView) findViewById(R.id.top_3);
		TextView bottom_3 = (TextView) findViewById(R.id.bottom_3);
		String cpuname = PhoneUtils.getCpuname();
		int cpunumber = PhoneUtils.getCpunumber();
		top_3.setText("CPU名称" + cpuname);
		bottom_3.setText("CPU线程:" + cpunumber);
		TextView top_4 = (TextView) findViewById(R.id.top_4);
		TextView bottom_4 = (TextView) findViewById(R.id.bottom_4);
		top_4.setText("手机分辨率:" + PhoneUtils.getPhoneResolvingpower(this));
		bottom_4.setText("相机分辨率:" + PhoneUtils.getCameraResolvingpower());
		// bottom_4.setText("相机分辨率:没有访问权限");
		TextView top_5 = (TextView) findViewById(R.id.top_5);
		TextView bottom_5 = (TextView) findViewById(R.id.bottom_5);
		top_5.setText(android.os.Build.BOARD);// 基带版本 已过时
		boolean isroot = PhoneUtils.isroot();// 是否root
		if (isroot) {
			bottom_5.setText("已Root");
		} else {
			bottom_5.setText("没有Root");
		}
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
		head_tv.setText("手机检测");
		ImageButton head_right = (ImageButton) findViewById(R.id.head_right);
		head_right.setVisibility(View.GONE);// 隐藏右边图片
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.head_left) {
			this.finish();
		} else if (v.getId() == R.id.battery_pb) {
			BatteryDialog.showDialog(this, String.valueOf(LEVEL),
					String.valueOf(wendu));
		}
	}

	// 销毁广播
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (br != null) {
			unregisterReceiver(br);
			br = null;
		}
	}

	// 广播电池
	class BatteryBr extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			// 电量改变 广播action
			if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
				SCALE = intent.getExtras().getInt(BatteryManager.EXTRA_SCALE);
				LEVEL = intent.getExtras().getInt(BatteryManager.EXTRA_LEVEL);
				// 温度
				wendu = intent.getExtras().getInt("temperature", 0) / 10;
				// 最大电量
				battery_pb.setMax(SCALE);
				// double不丢失精度
				double progress = Double.valueOf("" + LEVEL)
						/ Double.valueOf("" + SCALE) * 100;
				battery_pb.setProgress((int) progress);
//				battery_tv.setText(LEVEL + "%");
				if (LEVEL == 100) {
					battery_viewtop.setBackgroundColor(getResources().getColor(
							R.color.greena));
				} else {
					battery_viewtop.setBackgroundColor(getResources().getColor(
							R.color.orange));
				}

			}
		}
	}

}
