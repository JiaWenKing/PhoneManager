package com.example.model;

import android.graphics.drawable.Drawable;

/**
 * 软件管理名
 * **/
public class Appinfo {
	
	private String appname;
	private String packname;
	private String version;
	private Drawable appdrawable;
	private boolean isuserapp=false;
	public String getAppname() {
		return appname;
	}
	public void setAppname(String appname) {
		this.appname = appname;
	}
	public String getPackname() {
		return packname;
	}
	public void setPackname(String packname) {
		this.packname = packname;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public Drawable getAppdrawable() {
		return appdrawable;
	}
	public void setAppdrawable(Drawable appdrawable) {
		this.appdrawable = appdrawable;
	}
	public boolean isIsuserapp() {
		return isuserapp;
	}
	public void setIsuserapp(boolean isuserapp) {
		this.isuserapp = isuserapp;
	}
}
