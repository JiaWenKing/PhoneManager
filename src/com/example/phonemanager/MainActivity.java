package com.example.phonemanager;

import java.io.IOException;
import java.util.List;

import com.example.model.Taskinfo;
import com.example.utils.PhoneUtils;
import com.example.view.CircleView;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;
/**
 * 主界面
 * **/
public class MainActivity extends Activity implements OnClickListener {

	private CircleView cv;
	private TextView yuanbaifenbi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initHead();
		initView();
		setAngle();
	}

	private void initHead() {
		// TODO Auto-generated method stub
		ImageButton head_right = (ImageButton) findViewById(R.id.head_right);
		TextView head_tv=(TextView) findViewById(R.id.head_tv);
		head_right.setOnClickListener(this);
	}
	public void setAngle(){
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
						/ Double.valueOf(maxMemory + "") * 360;//圆设置的百分比
				double progress_show = Double.valueOf(number + "")
						/ Double.valueOf(maxMemory + "") * 100;//实际百分比
				cv.setAngle((int)progress);
				yuanbaifenbi.setText((int)progress_show+"");//加空格
	}

	private void initView() {
		// TODO Auto-generated method stub
		findViewById(R.id.rb_accelerate).setOnClickListener(this);
		findViewById(R.id.rb_software).setOnClickListener(this);
		findViewById(R.id.rb_testing).setOnClickListener(this);
		findViewById(R.id.rb_contacts).setOnClickListener(this);
		findViewById(R.id.rb_file).setOnClickListener(this);
		findViewById(R.id.rb_garbage).setOnClickListener(this);
		findViewById(R.id.img_circle).setOnClickListener(this);
		findViewById(R.id.bt_jiasu).setOnClickListener(this);
		
		
		cv = (CircleView) findViewById(R.id.circleview);
		yuanbaifenbi = (TextView) findViewById(R.id.yuanbaifenbi);
	}
	

	// 顶部监听
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.head_right) {
			jump(ConfigActivity.class);
		} else if (v.getId() == R.id.rb_accelerate) {
			jump(PhoneAccelerate.class);
		} else if (v.getId() == R.id.rb_software) {
			jump(AppManagerActivity.class);
		} else if (v.getId() == R.id.rb_testing) {
			jump(PhoneTestingActivity.class); 
		} else if (v.getId() == R.id.rb_contacts) {  
			jump(ContactsActivity.class);
		}else if (v.getId() == R.id.rb_file) {
			jump(FileActivity.class);
		}else if (v.getId() == R.id.rb_garbage) {
			jump(GarbageDisposalActivity.class); 
		}else if (v.getId()==R.id.img_circle||v.getId()==R.id.bt_jiasu) {
			List<Taskinfo> taskinfo = PhoneUtils
					.getTaskinfo(MainActivity.this);
			PhoneUtils.killApp(taskinfo);
			setAngle();
		}
	} 

	// 跳转方法调用
	public void jump(Class c) {
		Intent intent = new Intent(this, c);
		startActivity(intent);
	}

}
