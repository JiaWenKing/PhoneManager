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
 * �ֻ����
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
		IntentFilter intentfilter = new IntentFilter();// �㲥������
		intentfilter.addAction(Intent.ACTION_BATTERY_CHANGED);
		br = new BatteryBr();
		// ע��㲥
		registerReceiver(br, intentfilter);

		initHead();
		initView();
	}

	private void initView() {
		battery_pb = (ProgressBar) findViewById(R.id.battery_pb);// ����
		battery_viewtop = findViewById(R.id.battery_viewtop);
//		battery_tv = (TextView) findViewById(R.id.battery_tv);
		battery_pb.setOnClickListener(this);
		//�ֻ�����
		TextView top_1 = (TextView) findViewById(R.id.top_1);
		TextView bottom_1 = (TextView) findViewById(R.id.bottom_1);
		top_1.setText("�豸����:" + PhoneUtils.getBrand());
		bottom_1.setText("ϵͳ�汾:" + PhoneUtils.getVersion());
		TextView top_2 = (TextView) findViewById(R.id.top_2);
		TextView bottom_2 = (TextView) findViewById(R.id.bottom_2);
		long maxMemory;
		try {
			maxMemory = PhoneUtils.getMaxMemory();
			top_2.setText("ȫ�������ڴ�:" + Formatter.formatFileSize(this, maxMemory));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// long������ֵ

		long freeMemory = PhoneUtils.getFreeMemory(this);
		bottom_2.setText("ʣ�������ڴ�:" + Formatter.formatFileSize(this, freeMemory));
		TextView top_3 = (TextView) findViewById(R.id.top_3);
		TextView bottom_3 = (TextView) findViewById(R.id.bottom_3);
		String cpuname = PhoneUtils.getCpuname();
		int cpunumber = PhoneUtils.getCpunumber();
		top_3.setText("CPU����" + cpuname);
		bottom_3.setText("CPU�߳�:" + cpunumber);
		TextView top_4 = (TextView) findViewById(R.id.top_4);
		TextView bottom_4 = (TextView) findViewById(R.id.bottom_4);
		top_4.setText("�ֻ��ֱ���:" + PhoneUtils.getPhoneResolvingpower(this));
		bottom_4.setText("����ֱ���:" + PhoneUtils.getCameraResolvingpower());
		// bottom_4.setText("����ֱ���:û�з���Ȩ��");
		TextView top_5 = (TextView) findViewById(R.id.top_5);
		TextView bottom_5 = (TextView) findViewById(R.id.bottom_5);
		top_5.setText(android.os.Build.BOARD);// �����汾 �ѹ�ʱ
		boolean isroot = PhoneUtils.isroot();// �Ƿ�root
		if (isroot) {
			bottom_5.setText("��Root");
		} else {
			bottom_5.setText("û��Root");
		}
	}

	/**
	 * ͷ��
	 * **/
	private void initHead() {
		// TODO Auto-generated method stub
		ImageView head_left = (ImageView) findViewById(R.id.head_left);
		head_left.setImageResource(R.drawable.back);
		head_left.setOnClickListener(this);
		TextView head_tv = (TextView) findViewById(R.id.head_tv);
		head_tv.setText("�ֻ����");
		ImageButton head_right = (ImageButton) findViewById(R.id.head_right);
		head_right.setVisibility(View.GONE);// �����ұ�ͼƬ
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

	// ���ٹ㲥
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (br != null) {
			unregisterReceiver(br);
			br = null;
		}
	}

	// �㲥���
	class BatteryBr extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			// �����ı� �㲥action
			if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
				SCALE = intent.getExtras().getInt(BatteryManager.EXTRA_SCALE);
				LEVEL = intent.getExtras().getInt(BatteryManager.EXTRA_LEVEL);
				// �¶�
				wendu = intent.getExtras().getInt("temperature", 0) / 10;
				// ������
				battery_pb.setMax(SCALE);
				// double����ʧ����
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
