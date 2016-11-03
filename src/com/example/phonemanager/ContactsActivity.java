package com.example.phonemanager;

import java.util.ArrayList;
import java.util.List;

import com.example.adapter.ContactsAdapter;
import com.example.model.Contentinfo;
import com.example.utils.ContactsService;
import com.example.view.MyDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 联系人
 * **/
public class ContactsActivity extends Activity implements OnClickListener{
	Handler mhandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				MyDialog.dismiss();
				list.clear();
				List<Contentinfo> contentinfo=(List<Contentinfo>) msg.obj;
				list.addAll(contentinfo);
				adapter.notifyDataSetChanged();
				break;

			default:
				break;
			}
		};
	};
	private ContactsService cs;
	private List<Contentinfo> list;
	private ContactsAdapter adapter;
	private ListView lv_contact;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contacts_activity);
		cs = new ContactsService(this);
		List<Contentinfo> contentinfo = cs.getContentinfo();
		initHead();
		initView();
	}
	private void initView() {
		lv_contact = (ListView) findViewById(R.id.lv_contact);
		list = new ArrayList<Contentinfo>();
		adapter = new ContactsAdapter(list, this);
		lv_contact.setAdapter(adapter);
		loadData();
	}
	public void loadData(){
		MyDialog.ShowDialog(this);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				List<Contentinfo> contentinfo = cs.getContentinfo();
				Message obtainMessage = mhandler.obtainMessage();
				obtainMessage.what=1;
				obtainMessage.obj=contentinfo;
				mhandler.sendMessage(obtainMessage);
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
		head_tv.setText("联系人");
		ImageButton head_right = (ImageButton) findViewById(R.id.head_right);
		head_right.setVisibility(View.GONE);// 隐藏右边图片
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId()==R.id.head_left) {
			this.finish();
		}if (v.getId()==R.id.lv_contact) {
			 Choice();
		}
	}
	private int index=0;
	private String[] strs;
	private AlertDialog dialog;
	public void Choice(){
		strs = new String[] { "拨号", "发信息", "" };
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("选择");// 头部
		Drawable icon = getResources().getDrawable(R.drawable.ic_launcher);
		builder.setIcon(icon);// 图片
		dialog = builder.create();
		dialog.show();
	}
}
