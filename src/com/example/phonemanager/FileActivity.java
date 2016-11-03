package com.example.phonemanager;

import com.example.utils.FileManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 文件管理
 * */
public class FileActivity extends Activity implements OnClickListener{
	Handler mhandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				tv_filemax.setText(Formatter.formatFileSize(FileActivity.this, fileManager.ListallSize));
				tv_all_size.setText(Formatter.formatFileSize(FileActivity.this, fileManager.ListallSize));
				tv_all_number.setText((fileManager.Listall.size()+"项"));
				String type=(String) msg.obj;
				if (type.equals(FileManager.TYPE_TXT)) {
					tv_file_size.setText(Formatter.formatFileSize(FileActivity.this, fileManager.ListtextSize));
					tv_file_number.setText((fileManager.Listtext.size()+"项"));
				}else if (type.equals(FileManager.TYPE_VIDEO)) {
					tv_video_size.setText(Formatter.formatFileSize(FileActivity.this, fileManager.ListvideoSize));
					tv_video_number.setText((fileManager.Listvideo.size()+"项"));
				}else if (type.equals(FileManager.TYPE_AUDIO)) {
					tv_audio_size.setText(Formatter.formatFileSize(FileActivity.this, fileManager.ListaudioSize));
					tv_audio_number.setText((fileManager.Listaudio.size()+"项"));
				}else if (type.equals(FileManager.TYPE_IMAGE)) {
					tv_image_size.setText(Formatter.formatFileSize(FileActivity.this, fileManager.ListimageSize));
					tv_image_number.setText((fileManager.Listimage.size()+"项"));
				}else if (type.equals(FileManager.TYPE_ZIP)) {
					tv_zip_size.setText(Formatter.formatFileSize(FileActivity.this, fileManager.ListzipSize));
					tv_zip_number.setText((fileManager.Listzip.size()+"项"));
				}else if (type.equals(FileManager.TYPE_APK)) {
					tv_apk_size.setText(Formatter.formatFileSize(FileActivity.this, fileManager.ListapkSize));
					tv_apk_number.setText((fileManager.Listapk.size()+"项"));
				}
				break;
			case 2:
				view2.setVisibility(View.GONE);//隐藏圈
				img2.setVisibility(View.VISIBLE);//显示箭头
				view3.setVisibility(View.GONE);//隐藏圈
				img3.setVisibility(View.VISIBLE);//显示箭头
				view4.setVisibility(View.GONE);//隐藏圈
				img4.setVisibility(View.VISIBLE);//显示箭头
				view5.setVisibility(View.GONE);//隐藏圈
				img5.setVisibility(View.VISIBLE);//显示箭头
				view6.setVisibility(View.GONE);//隐藏圈
				img6.setVisibility(View.VISIBLE);//显示箭头
				view7.setVisibility(View.GONE);//隐藏圈
				img7.setVisibility(View.VISIBLE);//显示箭头
				view1.setVisibility(View.GONE);//隐藏圈
				img1.setVisibility(View.VISIBLE);//显示箭头
				break;

			default:
				break;
			}
		};
	};
	private FileManager fileManager;
	private TextView tv_file_size;
	private TextView tv_file_number;
	View view2,img2,view1,img1,view3,view4,view5,view6,view7,img3,img4,img5,img6,img7;
	private TextView tv_all_size;
	private TextView tv_all_number;
	private TextView tv_video_size;
	private TextView tv_video_number;
	private TextView tv_audio_size;
	private TextView tv_audio_number;
	private TextView tv_image_size;
	private TextView tv_image_number;
	private TextView tv_zip_size;
	private TextView tv_zip_number;
	private TextView tv_apk_size;
	private TextView tv_apk_number;
	private TextView tv_filemax;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.file_activity);
		fileManager = FileManager.getFileManager();
		fileManager.setMhandler(mhandler);
		initHead();
		initView();
		startSelect();
		initView2();
	}
	
	
	private void initView() {
		
		tv_filemax = (TextView) findViewById(R.id.tv_filemax);
		
		tv_all_size = (TextView) findViewById(R.id.tv_all_size);
		tv_all_number = (TextView) findViewById(R.id.tv_all_number);
		
		tv_file_size = (TextView) findViewById(R.id.tv_file_size);
		tv_file_number = (TextView) findViewById(R.id.tv_file_number);
		
		tv_video_size = (TextView) findViewById(R.id.tv_video_size);
		tv_video_number = (TextView) findViewById(R.id.tv_video_number);
		
		tv_audio_size = (TextView) findViewById(R.id.tv_audio_size);
		tv_audio_number = (TextView) findViewById(R.id.tv_audio_number);
		
		tv_image_size = (TextView) findViewById(R.id.tv_image_size);
		tv_image_number = (TextView) findViewById(R.id.tv_image_number);
		
		tv_zip_size = (TextView) findViewById(R.id.tv_zip_size);
		tv_zip_number = (TextView) findViewById(R.id.tv_zip_number);
		
		tv_apk_size = (TextView) findViewById(R.id.tv_apk_size);
		tv_apk_number = (TextView) findViewById(R.id.tv_apk_number);
		
		
		view1=findViewById(R.id.progressBar1);
		view2=findViewById(R.id.progressBar2);
		view3=findViewById(R.id.progressBar3);
		view4=findViewById(R.id.progressBar4);
		view5=findViewById(R.id.progressBar5);
		view6=findViewById(R.id.progressBar6);
		view7=findViewById(R.id.progressBar7);
		img1=findViewById(R.id.img_enter1);
		img2=findViewById(R.id.img_enter2);
		img3=findViewById(R.id.img_enter3);
		img4=findViewById(R.id.img_enter4);
		img5=findViewById(R.id.img_enter5);
		img6=findViewById(R.id.img_enter6);
		img7=findViewById(R.id.img_enter7);
	}
	public void startSelect(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				fileManager.Listall.clear();
				fileManager.ListallSize=0;
				fileManager.Listaudio.clear();
				fileManager.ListaudioSize=0;
				fileManager.Listimage.clear();
				fileManager.ListimageSize=0;
				fileManager.Listtext.clear();
				fileManager.ListtextSize=0;
				fileManager.Listvideo.clear();
				fileManager.ListvideoSize=0;
				fileManager.Listzip.clear();
				fileManager.ListzipSize=0;
				fileManager.Listapk.clear();
				fileManager.ListapkSize=0;
				// TODO Auto-generated method stub
				String inPath = FileManager.inPath;
				String outPath = FileManager.outPath;
				if (inPath!=null&&outPath!=null) {
					fileManager.selectFile(inPath, false);
					fileManager.selectFile(outPath, true);
				}else if (inPath!=null&&outPath==null) {
					fileManager.selectFile(inPath, true);
				}else if (outPath!=null&&inPath==null) {
					fileManager.selectFile(outPath, true);
				}
			}
		}).start();
	
	}

	private void initView2() {
		// TODO Auto-generated method stub
		findViewById(R.id.ll_all).setOnClickListener(this);
		findViewById(R.id.ll_file).setOnClickListener(this);
		findViewById(R.id.ll_video).setOnClickListener(this);
		findViewById(R.id.ll_audio).setOnClickListener(this);
		findViewById(R.id.ll_image).setOnClickListener(this);
		findViewById(R.id.ll_zip).setOnClickListener(this);
		findViewById(R.id.ll_apk).setOnClickListener(this);
		
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
		head_tv.setText("文件管理");
		ImageButton head_right = (ImageButton) findViewById(R.id.head_right);
		head_right.setVisibility(View.GONE);// 隐藏右边图片
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId()==R.id.head_left) {
			this.finish();
		}else if (v.getId()==R.id.ll_all) {
			jump(FileManager.TYPE_ANY);
		}else if (v.getId()==R.id.ll_file) {
			jump(FileManager.TYPE_TXT);
		}else if (v.getId()==R.id.ll_video) {
			jump(FileManager.TYPE_VIDEO);
		}else if (v.getId()==R.id.ll_audio) {
			jump(FileManager.TYPE_AUDIO);
		}else if (v.getId()==R.id.ll_image) {
			jump(FileManager.TYPE_IMAGE);
		}else if (v.getId()==R.id.ll_zip) {
			jump(FileManager.TYPE_ZIP);
		}else if (v.getId()==R.id.ll_apk) {
			jump(FileManager.TYPE_APK);
		}
	}
	
	public void jump(String type){
		if(!fileManager.isloadingdata) {
			Toast.makeText(this, "文件扫描中...", 1).show();
			return;
		}//当不等于true时不能进入查看
		Intent intent=new Intent(this,FileShowActivity.class);
		intent.putExtra("type", type);
		startActivityForResult(intent, 100);
//		startActivity(intent);
	}
	//回调方法   得到里外对应大小值
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode==100&&resultCode==200) {
			tv_filemax.setText(Formatter.formatFileSize(FileActivity.this, fileManager.ListallSize));
			tv_all_size.setText(Formatter.formatFileSize(FileActivity.this, fileManager.ListallSize));
			tv_all_number.setText((fileManager.Listall.size()+"项"));
			String type=data.getStringExtra("type");//回来带type通过intent拿出来
			if (type.equals(FileManager.TYPE_TXT)) {
				tv_file_size.setText(Formatter.formatFileSize(FileActivity.this, fileManager.ListtextSize));
				tv_file_number.setText((fileManager.Listtext.size()+"项"));
			}else if (type.equals(FileManager.TYPE_VIDEO)) {
				tv_video_size.setText(Formatter.formatFileSize(FileActivity.this, fileManager.ListvideoSize));
				tv_video_number.setText((fileManager.Listvideo.size()+"项"));
			}else if (type.equals(FileManager.TYPE_AUDIO)) {
				tv_audio_size.setText(Formatter.formatFileSize(FileActivity.this, fileManager.ListaudioSize));
				tv_audio_number.setText((fileManager.Listaudio.size()+"项"));
			}else if (type.equals(FileManager.TYPE_IMAGE)) {
				tv_image_size.setText(Formatter.formatFileSize(FileActivity.this, fileManager.ListimageSize));
				tv_image_number.setText((fileManager.Listimage.size()+"项"));
			}else if (type.equals(FileManager.TYPE_ZIP)) {
				tv_zip_size.setText(Formatter.formatFileSize(FileActivity.this, fileManager.ListzipSize));
				tv_zip_number.setText((fileManager.Listzip.size()+"项"));
			}else if (type.equals(FileManager.TYPE_APK)) {
				tv_apk_size.setText(Formatter.formatFileSize(FileActivity.this, fileManager.ListapkSize));
				tv_apk_number.setText((fileManager.Listapk.size()+"项"));
			}
		}
	}
	
}
