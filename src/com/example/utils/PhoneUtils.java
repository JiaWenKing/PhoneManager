package com.example.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.example.model.Appinfo;
import com.example.model.Garbageinfo;
import com.example.model.Taskinfo;
import com.example.phonemanager.R;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.os.Environment;
import android.os.StatFs;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * ���� 
 * **/
/**
 * �ֻ�����
 * */
public class PhoneUtils {
	//ɾ���ļ�
	public static void deletefile(File f){
		if (f.isDirectory()) {
			File[] listFiles = f.listFiles();
			if (listFiles!=null) {
				for (int i = 0; i < listFiles.length; i++) {
					if (listFiles[i]!=null) {
						if (listFiles[i].isDirectory()) {
							deletefile(listFiles[i]);
						}else{
							listFiles[i].delete();
						}
					}
				}
			}
			
		}else{
			f.delete();
		}//���ļ�
	}
	//�����ļ���С
	public static long filesize(File f){
		long size=0;
		if (!f.isDirectory()) {
			return f.length();
		}//�����ļ���
		else{
			File[] listFiles = f.listFiles();
			if (listFiles!=null) {
				for (int i = 0; i < listFiles.length; i++) {
					if (listFiles[i]!=null) {
						if (listFiles[i].isDirectory()) {
							size=size+filesize(listFiles[i]);
						}else{
							size=size+listFiles[i].length();
						}
					}
				}
			}
		}//���ļ���
		return size;
	}
	//��ѯ������������
	public static List<Garbageinfo> getGarbageinfo(String path, Context context){
		PackageManager pm = context.getPackageManager();//�õ�ͼƬ
		String builtinPath = getBuiltinPath();//·��
		List<Garbageinfo> infos=new ArrayList<Garbageinfo>();
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);//·��   ֻ��
		if (db.isOpen()) {
			String []columns=new String[]{"_id","softChinesename","apkname","filepath"};
			Cursor c = db.query("softdetail", columns, null, null, null, null, null);
			while(c.moveToNext()){
				int id_index = c.getColumnIndex("_id");
				int name_index = c.getColumnIndex("softChinesename");
				int apkname_index = c.getColumnIndex("apkname");
				int filepath_index = c.getColumnIndex("filepath");
				int id = c.getInt(id_index);
				String name = c.getString(name_index);
				String apkname = c.getString(apkname_index);
				String filepath = c.getString(filepath_index);
				Garbageinfo gf=new Garbageinfo();
				gf.set_id(id);
				gf.setName(name);
				gf.setApkname(apkname);
				
				gf.setFilepath(builtinPath+filepath);//����·��+�����·��
				File f=new File(gf.getFilepath());
				if (f.exists()) {
					Drawable icon=null;
					try {
						 icon = pm.getApplicationIcon(gf.getApkname());
					} catch (NameNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (icon==null) {
						icon=context.getResources().getDrawable(R.drawable.ic_system);
					}
					gf.setDrawable(icon);
					infos.add(gf);
				}//�ļ����ھͼӽ���
			}
		}
		return infos;
	}
	//	����db�ļ�������assets��		��������   ·��
	public static void copyDb(InputStream is, String path) throws IOException{
		File f=new File(path);
		if (f.exists()) {//����ļ�����
			return;
		}
		BufferedInputStream bis=new BufferedInputStream(is);//����
		BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(f));//д��
		int len=0;//����
		byte[] b=new byte[1024];
		while((len=bis.read(b))!=-1){
			bos.write(b, 0, len);
		}
		bos.flush();//ˢ��
		bis.close();
		bos.close();
		is.close();
	}
	
	// ��ȡ����
	public static String getBrand() {
		return android.os.Build.BRAND;
	}

	// ��ȡ�汾�� ϵͳ�汾��
	public static String getVersion() {
		return android.os.Build.MODEL + " " + android.os.Build.VERSION.RELEASE;
	}

	// ��ȡ����ڴ� �����쳣
	public static long getMaxMemory() throws IOException  {
		FileReader fr=new FileReader("/proc/meminfo");
		// ���ļ�
		BufferedReader br = new BufferedReader(fr);
		String text = br.readLine();
		
		String[] str = text.split("\\s+");
		String number = str[1];
		// ��ȡ����ڴ� �ó����ĵ�λ��kb*1024 ת��byte byte kb mb gb t
		long count = Long.valueOf(number) * 1024l;
		br.close();
		return count;
	}

	// ��ȡʣ���ڴ�
	public static long getFreeMemory(Context context) {
		MemoryInfo mi = new MemoryInfo();
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		am.getMemoryInfo(mi);
		return mi.availMem;
	}

	public static List<Taskinfo> getTaskinfo(Context context) {
		List<Taskinfo> taskinfos = new ArrayList<Taskinfo>();
		// ����������
		PackageManager pm = context.getPackageManager();
		// activity������
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		// ��ǰapi �������� ֻ��4.4����4.4������Ч �����Ѿ�����
		List<RunningAppProcessInfo> runningAppProcesses = am
				.getRunningAppProcesses();
		for (int i = 0; i < runningAppProcesses.size(); i++) {
			Taskinfo taskinfo = new Taskinfo();
			RunningAppProcessInfo info = runningAppProcesses.get(i);
			int pid = info.pid;
			taskinfo.setPid(pid);
			String packname = info.processName;
			taskinfo.setPackname(packname);
			try { // ���ֵ�ǰ���û�����ϵͳ
				ApplicationInfo applicationInfo = pm.getApplicationInfo(
						packname, 0);
				// ����ͼƬ
				Drawable draw = pm.getApplicationIcon(applicationInfo);
				if (draw == null) {
					Drawable drawable = context.getResources().getDrawable(
							R.drawable.ic_system);
					taskinfo.setDrawable(drawable);
				} else {
					taskinfo.setDrawable(draw);
				}
				// Ӧ������
				String appname = pm.getApplicationLabel(applicationInfo)
						.toString();
				// ��ֵ
				taskinfo.setAppname(appname);
				// �û�����ϵͳ
				boolean isuserapp = isuserapp(applicationInfo);
				taskinfo.setIsuserapp(isuserapp);// �ǲ����û�app
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Drawable drawable = context.getResources().getDrawable(
						R.drawable.ic_system);
				taskinfo.setDrawable(drawable);
				taskinfo.setAppname(info.processName);// ����Ϊ����
				taskinfo.setPackname(info.processName);// Ӧ�ð���
				taskinfo.setIsuserapp(true);
			}
			android.os.Debug.MemoryInfo[] processMemoryInfo = am
					.getProcessMemoryInfo(new int[] { pid });
			android.os.Debug.MemoryInfo memoryInfo = processMemoryInfo[0];
			int totalPrivateDirty = memoryInfo.getTotalPrivateDirty();// ��ǰ����ռ���ڴ�
			taskinfo.setMemory(totalPrivateDirty);// ת��long����
			taskinfos.add(taskinfo);
		}
		return taskinfos;
	}

	public static boolean isuserapp(ApplicationInfo applicationInfo) {
		// ��ǰ�ı�ʶ �ǲ��� ������ϵͳ�� ������ ����û��� 1&1����1 1&2����0
		if ((applicationInfo.flags & applicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
			return false;
			// ��ǰ��ʶ ϵͳ��ʶ ==0�����û���
		} else if ((applicationInfo.flags & applicationInfo.FLAG_SYSTEM) == 0) {
			return false;
		}
		return true;
	}

	/**
	 * �ֻ����� ɱ������ ����
	 * **/
	public static void killApp(List<Taskinfo> taskinfo) {
		for (int i = 0; i < taskinfo.size(); i++) {
			Taskinfo taskinfo2 = taskinfo.get(i);
			int pid = taskinfo2.getPid();
			if (pid != android.os.Process.myPid()
					&& taskinfo2.isIsselect() == true) {
				android.os.Process.killProcess(pid);
			}
		}
	}

	/**
	 * ���������� ����ֻ����ô洢·��
	 * **/
	public static String getExternalPath() {
		Map<String, String> getenv = System.getenv();
		if (getenv.containsKey("SECONDARY_STORAGE")) {
			String value = getenv.get("SECONDARY_STORAGE");
			String[] split = value.split(":");
			return split[0];
		}
		return null;
	}

	/**
	 * ���������� ���ô洢
	 * **/
	public static String getBuiltinPath() {
		String externalStorageState = Environment.getExternalStorageState();
		if (externalStorageState.equals(Environment.MEDIA_MOUNTED)) {
			return Environment.getExternalStorageDirectory().getAbsolutePath();
		}
		return null;
	}

	// ������ô洢·���ô�С
	public static long getExternalAvailable() {
		String externalPath = getExternalPath();
		if (externalPath == null) {
			return 0;
		}
		StatFs sf = new StatFs(externalPath);
		long blockSize = sf.getBlockSize();// �ļ�����Ŀ
		long availableBlocks = sf.getAvailableBlocks();// ����
		return blockSize * availableBlocks;
	}

	// �������
	public static long getExternalAvailableMax() {
		String externalPath = getExternalPath();
		if (externalPath == null) {
			return 0;
		}
		StatFs sf = new StatFs(externalPath);
		long blockSize = sf.getBlockSize();// �ļ�����Ŀ
		long blockCount = sf.getBlockCount();// ���
		return blockSize * blockCount;

	}

	// �����������ô洢��С
	@SuppressWarnings("deprecation")
	public static long getBuiltinAvailable() {
		// �������·��
		String builtinPath = getBuiltinPath();
		StatFs sf = new StatFs(builtinPath);
		long blockSize = sf.getBlockSize();// �ļ�����Ŀ
		long availableBlocks = sf.getAvailableBlocks();// ����
		return blockSize * availableBlocks;
	}

	// ������ô洢���
	@SuppressWarnings("deprecation")
	public static long getBuiltinAvailableMax() {
		// �������·��
		String builtinPath = getBuiltinPath();
		StatFs sf = new StatFs(builtinPath);
		long blockSize = sf.getBlockSize();// �ļ�����Ŀ
		long BlockCount = sf.getBlockCount();// ���
		return blockSize * BlockCount;
	}

	// ����ֻ�app
	public static List<Appinfo> getAppinfo(Context context) {
		PackageManager pm = context.getPackageManager();
		List<Appinfo> appinfos = new ArrayList<Appinfo>();
		List<ApplicationInfo> installedApplications = pm
				.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
		for (int i = 0; i < installedApplications.size(); i++) {
			Appinfo ai = new Appinfo();
			ApplicationInfo Info = installedApplications.get(i);
			// ���ð���
			String packageName = Info.packageName;
			ai.setPackname(packageName);
			// ����ͼƬ
			Drawable drawable = Info.loadIcon(pm);
			ai.setAppdrawable(drawable);
			// ��������
			String appname = Info.loadLabel(pm).toString();
			ai.setAppname(appname);
			try {
				// �汾��
				PackageInfo packageInfo = pm.getPackageInfo(packageName, 0);
				String versionName = packageInfo.versionName;
				ai.setVersion(versionName);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			boolean isuserapp = isuserapp(Info);
			ai.setIsuserapp(isuserapp);
			appinfos.add(ai);
		}
		return appinfos;
	}
	/**
	 * �ֻ����
	 * */
	//��ȡcpu
	public static String getCpuname(){
		try {
			FileReader fr=new FileReader("/proc/cpuinfo");
			BufferedReader br=new BufferedReader(fr);
			String text = br.readLine();
			String[] split = text.split("\\s", 2);
			return split[1];
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	//��ȡcpu����
	public static int getCpunumber(){
		class CpuFilter implements FileFilter{

			@Override
			public boolean accept(File pathname) {
				// TODO Auto-generated method stub
				if (Pattern.matches("cpu[0-9]", pathname.getName())) {
					return true;
				}
				return false;
			}
		}
		File f=new File("/sys/devices/system/cpu/");
		File[] listFiles = f.listFiles(new CpuFilter());	
		
		return listFiles.length;
	}
	
	//��ȡ�ֻ��ֱ���
	public static String getPhoneResolvingpower(Activity a){
		DisplayMetrics dm=new DisplayMetrics();
		a.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels+"*"+dm.heightPixels;
	}
	//����ֱ���
	public static String getCameraResolvingpower(){
		Camera camera = Camera.open(); 
		Parameters parameters = camera.getParameters();
		List<Size> sizeinfo = parameters.getSupportedPictureSizes();
		Size size=sizeinfo.get(0);
		for (int i = 1; i < sizeinfo.size(); i++) {
			Size sz=sizeinfo.get(i);
			if (size.width*size.height<sz.width*sz.height) {
				size=sz;
			}
		}
		return size.width+"*"+size.height;
	}
	//root
	public static boolean isroot(){
		File f=new 	File("/system/bin/su");
		File f1=new 	File("/system/xbin/su");
		if (f.exists()&&f1.exists()) {
			return true;
		}
		return false;
	}
		//ͼƬѹ������
		/** ��λת�� */
		public static int dp2px(Context context, int dp) {
			float scale = context.getResources().getDisplayMetrics().density;
			return (int) (dp * scale + 0.5f);
		}

		/** ��λת�� */
		public static int px2dp(Context context, int px) {
			float scale = context.getResources().getDisplayMetrics().density;
			return (int) (px / scale + 0.5f);
		}
		public static Bitmap loadBitmap(String pathName, int h, int w,
				Context context) {
			// ���ص�ͼ��Ŀ���С
			int targetw = dp2px(context, w);
			int targeth = dp2px(context, h);
			Options options = new Options();
			options.inJustDecodeBounds = true; // ��"�߽紦��"
			BitmapFactory.decodeFile(pathName, options);
			int resw = options.outWidth;
			int resh = options.outHeight;
			// 40x40
			if (resw <= targetw && resh <= targeth) {
				options.inSampleSize = 1; // ���ü���ʱ����Դ����
			}
			// ��������
			else {
				int scalew = resw / targetw;
				int scaleh = resh / targeth;
				int scale = scalew > scaleh ? scalew : scaleh;
				options.inSampleSize = scale;// ���ü���ʱ����Դ����
			}
			options.inJustDecodeBounds = false;// �ر�"�߽紦��"
			Bitmap bitmap = BitmapFactory.decodeFile(pathName, options);
			return bitmap;
		}
	
	
}
