package com.example.phonemanager;

import com.example.utils.MySharedPreferences;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * 主界面head_right配置
 * **/
public class ConfigActivity extends Activity implements OnClickListener{
	// private ImageView img_start;
	private MySharedPreferences sf;
	private ToggleButton img_start;
	private ToggleButton img_notify;
	private ToggleButton img_push;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.config_activity);
		sf = new MySharedPreferences(this);
		initHead();
		initView();
	}
/**
 * 按钮
 * **/
	private void initView() {
		boolean getisStart = sf.getisStart();
		boolean getisnotify = sf.getisnotify();
		boolean getispush = sf.getispush();
		img_start = (ToggleButton) findViewById(R.id.img_start);
		img_notify = (ToggleButton) findViewById(R.id.img_notify);
		img_push = (ToggleButton) findViewById(R.id.img_push);
		if (getisStart) {
			img_start.setChecked(true);
		} else {
			img_start.setChecked(false);
		}
		if (getisnotify) {
			img_notify.setChecked(true);
		} else {
			img_notify.setChecked(false);
		}
		if (getispush) {
			img_push.setChecked(true);
		} else {
			img_push.setChecked(false);
		}
		img_start.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					img_start.setChecked(true);
					sf.updateisStart(true);
				}else {
					img_start.setChecked(false);
					sf.updateisStart(false);
				}
			}
		});
		img_notify.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					img_notify.setChecked(true);
					sf.updateisnotify(true);
				}else {
					img_notify.setChecked(false);
					sf.updateisnotify(false);
				}
			}
		});
		img_push.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					img_push.setChecked(true);
					sf.updateispush(true);
				} else {
					img_push.setChecked(false);
					sf.updateispush(false);
				}
			}
		});
		findViewById(R.id.rl_help).setOnClickListener(this);
		findViewById(R.id.rl_idea).setOnClickListener(this);
		findViewById(R.id.rl_friend).setOnClickListener(this);
		findViewById(R.id.rl_newversion).setOnClickListener(this);
		findViewById(R.id.rl_aboutus).setOnClickListener(this);
		
		
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
		head_tv.setText("设置");
		ImageButton head_right = (ImageButton) findViewById(R.id.head_right);
		head_right.setVisibility(View.GONE);// 隐藏右边图片
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId()==R.id.head_left) {
			ConfigActivity.this.finish();
		}else if (v.getId()==R.id.rl_help) {
			jump(HelpActivity.class);
		}else if (v.getId()==R.id.rl_idea) {
			jump(IdeaActivity.class);
		}else if (v.getId()==R.id.rl_friend) {
			
		}else if (v.getId()==R.id.rl_newversion) {
			Toast.makeText(ConfigActivity.this, "当前是最新版本", 1).show();
		}else if (v.getId()==R.id.rl_aboutus) {
			jump(AboutusActivity.class);
		}
	}
	
	public void jump(Class c){
		Intent intent=new Intent(this,c);
		startActivity(intent);
	}

}
