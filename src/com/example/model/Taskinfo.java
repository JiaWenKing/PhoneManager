package com.example.model;

import android.graphics.drawable.Drawable;
/**
 * ��ֵ
 * **/
public class Taskinfo {
	//����  ���г����Ψһ��ʶ
	private int pid;
	//Ӧ�õ�ͼƬ
	private Drawable drawable;
	//Ӧ�õ�����
	private String appname;
	//��ǰ�������ֻ���ռ�õ��ڴ�
	private long memory;
	//�Ƿ�ѡ��
	private boolean isselect=false;
	//�Ƿ����û�����   false��   true����
	private boolean isuserapp=false;
	//����    app��Ψһ��ʶ
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
