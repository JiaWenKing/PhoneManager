package com.example.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 轻量级数据存储
 * **/
public class MySharedPreferences {

	private SharedPreferences sp;

	public MySharedPreferences(Context context) {
		sp = context.getSharedPreferences("mysp", Context.MODE_PRIVATE);
	}

	public int getNumber() {
		return sp.getInt("number", 0);
	}

	public void updateNumber() {
		Editor edit = sp.edit();
		edit.putInt("number", 1);
		edit.commit();
	}

	// 获取开机是否启动
	public boolean getisStart() {
		boolean boolean1 = sp.getBoolean("isstart", false);
		return boolean1;
	}

	// 改变是否启动的值
	public void updateisStart(boolean isstart) {
		Editor edit = sp.edit();
		edit.putBoolean("isstart", isstart);
		edit.commit();
	}

	// 获取通知图标是否开启
	public boolean getisnotify() {
		boolean boolean1 = sp.getBoolean("isnotify", false);
		return boolean1;
	}

	// 改变通知是否启动的值
	public void updateisnotify(boolean isnotify) {
		Editor edit = sp.edit();
		edit.putBoolean("isnotify", isnotify);
		edit.commit();
	}

	// 获取推送图标是否启动
	public boolean getispush() {
		boolean boolean1 = sp.getBoolean("ispush", false);
		return boolean1;
	}

	// 改变推送图标是否启动的值
	public void updateispush(boolean ispush) {
		Editor edit = sp.edit();
		edit.putBoolean("ispush", ispush);
		edit.commit();
	}
}
