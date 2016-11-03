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
 * 工具 
 * **/
/**
 * 手机加速
 * */
public class PhoneUtils {
	//删除文件
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
		}//是文件
	}
	//计算文件大小
	public static long filesize(File f){
		long size=0;
		if (!f.isDirectory()) {
			return f.length();
		}//不是文件夹
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
		}//是文件夹
		return size;
	}
	//查询垃圾清理数据
	public static List<Garbageinfo> getGarbageinfo(String path, Context context){
		PackageManager pm = context.getPackageManager();//拿到图片
		String builtinPath = getBuiltinPath();//路径
		List<Garbageinfo> infos=new ArrayList<Garbageinfo>();
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);//路径   只读
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
				
				gf.setFilepath(builtinPath+filepath);//内置路径+查出的路径
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
				}//文件存在就加进来
			}
		}
		return infos;
	}
	//	复制db文件（来自assets）		传流进来   路径
	public static void copyDb(InputStream is, String path) throws IOException{
		File f=new File(path);
		if (f.exists()) {//如果文件存在
			return;
		}
		BufferedInputStream bis=new BufferedInputStream(is);//读流
		BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(f));//写流
		int len=0;//长度
		byte[] b=new byte[1024];
		while((len=bis.read(b))!=-1){
			bos.write(b, 0, len);
		}
		bos.flush();//刷新
		bis.close();
		bos.close();
		is.close();
	}
	
	// 获取牌子
	public static String getBrand() {
		return android.os.Build.BRAND;
	}

	// 获取版本号 系统版本号
	public static String getVersion() {
		return android.os.Build.MODEL + " " + android.os.Build.VERSION.RELEASE;
	}

	// 获取最大内存 需抛异常
	public static long getMaxMemory() throws IOException  {
		FileReader fr=new FileReader("/proc/meminfo");
		// 读文件
		BufferedReader br = new BufferedReader(fr);
		String text = br.readLine();
		
		String[] str = text.split("\\s+");
		String number = str[1];
		// 获取最大内存 拿出来的单位是kb*1024 转成byte byte kb mb gb t
		long count = Long.valueOf(number) * 1024l;
		br.close();
		return count;
	}

	// 获取剩余内存
	public static long getFreeMemory(Context context) {
		MemoryInfo mi = new MemoryInfo();
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		am.getMemoryInfo(mi);
		return mi.availMem;
	}

	public static List<Taskinfo> getTaskinfo(Context context) {
		List<Taskinfo> taskinfos = new ArrayList<Taskinfo>();
		// 包名管理器
		PackageManager pm = context.getPackageManager();
		// activity管理器
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		// 当前api 函数方法 只在4.4包括4.4以内有效 以上已经屏蔽
		List<RunningAppProcessInfo> runningAppProcesses = am
				.getRunningAppProcesses();
		for (int i = 0; i < runningAppProcesses.size(); i++) {
			Taskinfo taskinfo = new Taskinfo();
			RunningAppProcessInfo info = runningAppProcesses.get(i);
			int pid = info.pid;
			taskinfo.setPid(pid);
			String packname = info.processName;
			taskinfo.setPackname(packname);
			try { // 区分当前是用户还是系统
				ApplicationInfo applicationInfo = pm.getApplicationInfo(
						packname, 0);
				// 加载图片
				Drawable draw = pm.getApplicationIcon(applicationInfo);
				if (draw == null) {
					Drawable drawable = context.getResources().getDrawable(
							R.drawable.ic_system);
					taskinfo.setDrawable(drawable);
				} else {
					taskinfo.setDrawable(draw);
				}
				// 应用名字
				String appname = pm.getApplicationLabel(applicationInfo)
						.toString();
				// 赋值
				taskinfo.setAppname(appname);
				// 用户还是系统
				boolean isuserapp = isuserapp(applicationInfo);
				taskinfo.setIsuserapp(isuserapp);// 是不是用户app
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Drawable drawable = context.getResources().getDrawable(
						R.drawable.ic_system);
				taskinfo.setDrawable(drawable);
				taskinfo.setAppname(info.processName);// 设置为包名
				taskinfo.setPackname(info.processName);// 应用包名
				taskinfo.setIsuserapp(true);
			}
			android.os.Debug.MemoryInfo[] processMemoryInfo = am
					.getProcessMemoryInfo(new int[] { pid });
			android.os.Debug.MemoryInfo memoryInfo = processMemoryInfo[0];
			int totalPrivateDirty = memoryInfo.getTotalPrivateDirty();// 当前程序占得内存
			taskinfo.setMemory(totalPrivateDirty);// 转成long类型
			taskinfos.add(taskinfo);
		}
		return taskinfos;
	}

	public static boolean isuserapp(ApplicationInfo applicationInfo) {
		// 当前的标识 是不是 本身是系统的 升级过 变成用户的 1&1返回1 1&2返回0
		if ((applicationInfo.flags & applicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
			return false;
			// 当前标识 系统标识 ==0就是用户的
		} else if ((applicationInfo.flags & applicationInfo.FLAG_SYSTEM) == 0) {
			return false;
		}
		return true;
	}

	/**
	 * 手机加速 杀掉程序 清理
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
	 * 软件管理界面 获得手机外置存储路径
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
	 * 软件管理界面 内置存储
	 * **/
	public static String getBuiltinPath() {
		String externalStorageState = Environment.getExternalStorageState();
		if (externalStorageState.equals(Environment.MEDIA_MOUNTED)) {
			return Environment.getExternalStorageDirectory().getAbsolutePath();
		}
		return null;
	}

	// 获得外置存储路径用大小
	public static long getExternalAvailable() {
		String externalPath = getExternalPath();
		if (externalPath == null) {
			return 0;
		}
		StatFs sf = new StatFs(externalPath);
		long blockSize = sf.getBlockSize();// 文件夹数目
		long availableBlocks = sf.getAvailableBlocks();// 可用
		return blockSize * availableBlocks;
	}

	// 外置最大
	public static long getExternalAvailableMax() {
		String externalPath = getExternalPath();
		if (externalPath == null) {
			return 0;
		}
		StatFs sf = new StatFs(externalPath);
		long blockSize = sf.getBlockSize();// 文件夹数目
		long blockCount = sf.getBlockCount();// 最大
		return blockSize * blockCount;

	}

	// 计算内置已用存储大小
	@SuppressWarnings("deprecation")
	public static long getBuiltinAvailable() {
		// 获得内置路径
		String builtinPath = getBuiltinPath();
		StatFs sf = new StatFs(builtinPath);
		long blockSize = sf.getBlockSize();// 文件夹数目
		long availableBlocks = sf.getAvailableBlocks();// 可用
		return blockSize * availableBlocks;
	}

	// 获得内置存储最大
	@SuppressWarnings("deprecation")
	public static long getBuiltinAvailableMax() {
		// 获得内置路径
		String builtinPath = getBuiltinPath();
		StatFs sf = new StatFs(builtinPath);
		long blockSize = sf.getBlockSize();// 文件夹数目
		long BlockCount = sf.getBlockCount();// 最大
		return blockSize * BlockCount;
	}

	// 获得手机app
	public static List<Appinfo> getAppinfo(Context context) {
		PackageManager pm = context.getPackageManager();
		List<Appinfo> appinfos = new ArrayList<Appinfo>();
		List<ApplicationInfo> installedApplications = pm
				.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
		for (int i = 0; i < installedApplications.size(); i++) {
			Appinfo ai = new Appinfo();
			ApplicationInfo Info = installedApplications.get(i);
			// 设置包名
			String packageName = Info.packageName;
			ai.setPackname(packageName);
			// 设置图片
			Drawable drawable = Info.loadIcon(pm);
			ai.setAppdrawable(drawable);
			// 设置名字
			String appname = Info.loadLabel(pm).toString();
			ai.setAppname(appname);
			try {
				// 版本号
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
	 * 手机检测
	 * */
	//获取cpu
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
	
	//获取cpu数量
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
	
	//获取手机分辨率
	public static String getPhoneResolvingpower(Activity a){
		DisplayMetrics dm=new DisplayMetrics();
		a.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels+"*"+dm.heightPixels;
	}
	//相机分辨率
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
		//图片压缩方法
		/** 单位转换 */
		public static int dp2px(Context context, int dp) {
			float scale = context.getResources().getDisplayMetrics().density;
			return (int) (dp * scale + 0.5f);
		}

		/** 单位转换 */
		public static int px2dp(Context context, int px) {
			float scale = context.getResources().getDisplayMetrics().density;
			return (int) (px / scale + 0.5f);
		}
		public static Bitmap loadBitmap(String pathName, int h, int w,
				Context context) {
			// 加载的图像目标大小
			int targetw = dp2px(context, w);
			int targeth = dp2px(context, h);
			Options options = new Options();
			options.inJustDecodeBounds = true; // 打开"边界处理"
			BitmapFactory.decodeFile(pathName, options);
			int resw = options.outWidth;
			int resh = options.outHeight;
			// 40x40
			if (resw <= targetw && resh <= targeth) {
				options.inSampleSize = 1; // 设置加载时的资源比例
			}
			// 比例计算
			else {
				int scalew = resw / targetw;
				int scaleh = resh / targeth;
				int scale = scalew > scaleh ? scalew : scaleh;
				options.inSampleSize = scale;// 设置加载时的资源比例
			}
			options.inJustDecodeBounds = false;// 关闭"边界处理"
			Bitmap bitmap = BitmapFactory.decodeFile(pathName, options);
			return bitmap;
		}
	
	
}
