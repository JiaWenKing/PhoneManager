package com.example.model;

import android.graphics.drawable.Drawable;
/**
 * 传值
 * **/
public class Taskinfo {
	//进程  运行程序的唯一标识
	private int pid;
	//应用的图片
	private Drawable drawable;
	//应用的名字
	private String appname;
	//当前程序在手机里占用的内存
	private long memory;
	//是否选中
	private boolean isselect=false;
	//是否是用户进程   false是   true不是
	private boolean isuserapp=false;
	//包名    app的唯一标识
	private String packname;
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public Drawable getDrawable() {
		return drawable;
	}
	public void setDrawable(Drawable drawable) {
		this.drawable = drawable;
	}
	public String getAppname() {
		return appname;
	}
	public void setAppname(String appname) {
		this.appname = appname;
	}
	public long getMemory() {
		return memory;
	}
	public void setMemory(long memory) {
		this.memory = memory;
	}
	public boolean isIsselect() {
		return isselect;
	}
	public void setIsselect(boolean isselect) {
		this.isselect = isselect;
	}
	public boolean isIsuserapp() {
		return isuserapp;
	}
	public void setIsuserapp(boolean isuserapp) {
		this.isuserapp = isuserapp;
	}
	public String getPackname() {
		return packname;
	}
	public void setPackname(String packname) {
		this.packname = packname;
	}
}
