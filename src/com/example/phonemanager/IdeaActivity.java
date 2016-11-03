package com.example.phonemanager;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 意见反馈
 * **/
public class IdeaActivity extends Activity implements OnClickListener{
	Handler mhandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				Toast.makeText(IdeaActivity.this, "提交成功", 1).show();
				IdeaActivity.this.finish();
				break;

			default:
				break;
			}
		};
	};
	private EditText ed_text;
	private Button bt_ok;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.idea_activity);
		initHead();
		initView();
	}
	private void initView() {
		bt_ok = (Button) findViewById(R.id.bt_ok);
		bt_ok.setOnClickListener(this);
		ed_text = (EditText) findViewById(R.id.ed_text);
		
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
			head_tv.setText("Feedback");
			ImageButton head_right = (ImageButton) findViewById(R.id.head_right);
			head_right.setVisibility(View.GONE);// 隐藏右边图片
		}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId()==R.id.head_left) {
			this.finish();
		}else if (v.getId()==R.id.bt_ok) {
			String text = ed_text.getText().toString();
			if (text==null||text.length()<=0) {
				Toast.makeText(this, "请输入反馈内容", 1).show();
				return;
			}
			mhandler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					mhandler.sendEmptyMessage(1);
				}
			}, 2000);
		}
	}
}
