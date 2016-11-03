package com.example.phonemanager;



import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * ��������
 * **/
public class AboutusActivity extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_us_activity);
		initHead();
		initVeiw();
	}
	
	private void initVeiw() {
		//app����   app�汾
		TextView tv_appname = (TextView) findViewById(R.id.tv_appname);
		TextView tv_version1 = (TextView) findViewById(R.id.tv_version1);
		//����������
		PackageManager pm=getPackageManager();
		//
		CharSequence label = pm.getApplicationLabel(getApplicationInfo());
		tv_appname.setText(label);
		//
		try {
			PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
			tv_version1.setText(packageInfo.versionName);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			head_tv.setText("About Us");
			ImageButton head_right = (ImageButton) findViewById(R.id.head_right);
			head_right.setVisibility(View.GONE);// �����ұ�ͼƬ
		}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId()==R.id.head_left) {
			this.finish();
		}
	}
}
