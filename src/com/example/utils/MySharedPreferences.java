package com.example.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * ���������ݴ洢
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

	// ��ȡ�����Ƿ�����
	public boolean getisStart() {
		boolean boolean1 = sp.getBoolean("isstart", false);
		return boolean1;
	}

	// �ı��Ƿ�������ֵ
	public void updateisStart(boolean isstart) {
		Editor edit = sp.edit();
		edit.putBoolean("isstart", isstart);
		edit.commit();
	}

	// ��ȡ֪ͨͼ���Ƿ���
	public boolean getisnotify() {
		boolean boolean1 = sp.getBoolean("isnotify", false);
		return boolean1;
	}

	// �ı�֪ͨ�Ƿ�������ֵ
	public void updateisnotify(boolean isnotify) {
		Editor edit = sp.edit();
		edit.putBoolean("isnotify", isnotify);
		edit.commit();
	}

	// ��ȡ����ͼ���Ƿ�����
	public boolean getispush() {
		boolean boolean1 = sp.getBoolean("ispush", false);
		return boolean1;
	}

	// �ı�����ͼ���Ƿ�������ֵ
	public void updateispush(boolean ispush) {
		Editor edit = sp.edit();
		edit.putBoolean("ispush", ispush);
		edit.commit();
	}
}
