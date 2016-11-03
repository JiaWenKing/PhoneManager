package com.example.phonemanager;

import java.util.ArrayList;
import java.util.List;

import com.example.adapter.ViewPagerAdapter;
import com.example.utils.MySharedPreferences;
import com.example.view.MyDialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class StartActivity extends Activity{
	Handler mhandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				MyDialog.dismiss();
				Intent intent=new Intent(StartActivity.this,MainActivity.class);
				startActivity(intent);
				StartActivity.this.finish();
				break;

			default:
				break;
			}
		};
	};
	int[] res=new int[]{R.drawable.logo_1,R.drawable.logo_2,R.drawable.logo_3,
			R.drawable.logo_4,R.drawable.logo_5,};
	ViewPager vp;
	private MySharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		sp = new MySharedPreferences(this);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		vp=(ViewPager) findViewById(R.id.vp);
		ImageView img=(ImageView) findViewById(R.id.iv);
		if (sp.getNumber()==0) {
			sp.updateNumber();
			img.setVisibility(View.GONE);
			vp.setVisibility(View.VISIBLE);
			List<View> views = getViewPagerLoadData();
			ViewPagerAdapter adapter =new ViewPagerAdapter(views);
			vp.setAdapter(adapter);
			View view = views.get(views.size()-1);
			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent=new Intent(StartActivity.this,MainActivity.class);
					startActivity(intent);
					StartActivity.this.finish();
				}
			});
		}else{
			MyDialog.ShowDialog(this);
			mhandler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					mhandler.sendEmptyMessage(1);
				}
			}, 2000);
		}
	}
	public List<View> getViewPagerLoadData(){
		List<View> views=new ArrayList<View>();
		for (int i = 0; i < res.length; i++) {
			ImageView imgview =new ImageView(this);
			LayoutParams params =new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			imgview.setLayoutParams(params);
			imgview.setScaleType(ScaleType.FIT_XY);
			imgview.setImageResource(res[i]);
			views.add(imgview);
		}
		return views;
	}
}
