package com.example.phonemanager;

import java.util.ArrayList;
import java.util.List;

import com.example.adapter.ViewPagerAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
/**
 * °ïÖúËµÃ÷
 * */
public class HelpActivity extends Activity implements OnClickListener{
	int res[] = new int[]{R.drawable.adware_style_applist,R.drawable.adware_style_banner,R.drawable.adware_style_creditswall};
	ViewPager vp;
	private RadioGroup rg;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help_activity);
		initHead();
		initView();
	}
	/**
	 * Í·²¿
	 * **/
	private void initHead() {
		// TODO Auto-generated method stub
		ImageView head_left = (ImageView) findViewById(R.id.head_left);
		head_left.setImageResource(R.drawable.back);
		head_left.setOnClickListener(this);
		TextView head_tv = (TextView) findViewById(R.id.head_tv);
		head_tv.setText("°ïÖúËµÃ÷");
		ImageButton head_right = (ImageButton) findViewById(R.id.head_right);
		head_right.setVisibility(View.GONE);// Òþ²ØÓÒ±ßÍ¼Æ¬
	}
	private void initView() {
		// TODO Auto-generated method stub
		vp=(ViewPager) findViewById(R.id.vp);
		rg = (RadioGroup) findViewById(R.id.rg);
		rg.check(R.id.rb_1);
		List<View> views = getViewPagerData();
		ViewPagerAdapter adapter=new ViewPagerAdapter(views);
		vp.setAdapter(adapter);
		vp.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				if (arg0==0) {
					rg.check(R.id.rb_1);
				}else if (arg0==1) {
					rg.check(R.id.rb_2);
				}else if (arg0==2) {
					rg.check(R.id.rb_3);
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	public List<View> getViewPagerData(){
		List<View> views=new ArrayList<View>();
		for (int i = 0; i < res.length; i++) {
			ImageView img=new ImageView(this);
			LayoutParams params =new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
			img.setLayoutParams(params);
			img.setImageResource(res[i]);
			views.add(img);
		}
		return views;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId()==R.id.head_left) {
			this.finish();
		}
	}
}
