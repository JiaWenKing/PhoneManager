package com.example.utils;

import java.util.ArrayList;
import java.util.List;

import com.example.model.Contentinfo;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
/**
 * 联系人读取工具
 * **/
public class ContactsService {
	private ContentResolver contentResolver;

	//实现app之间数据共享
	public ContactsService(Context context){
		contentResolver = context.getContentResolver();
	}
	public List<Contentinfo> getContentinfo(){
		List<Contentinfo> contentinfo=new ArrayList<Contentinfo>();
		String AUTHORITY= ContactsContract.AUTHORITY;//联系人包名
		Uri uri=Uri.parse("content://"+AUTHORITY+"/raw_contacts");//路径+表
		Cursor csr = contentResolver.query(uri, new String[]{"_id"}, null, null, null);
		while(csr.moveToNext()){
			Contentinfo cf=new Contentinfo();
			int _id = csr.getInt(0);
			cf.set_id(_id);
			Uri uri1=Uri.parse("content://"+AUTHORITY+"/raw_contacts/"+_id+"/data");//路径+表+数据
			Cursor cs = contentResolver.query(uri1, new String[]{"data1","mimetype"}, null, null, null);
			while(cs.moveToNext()){
				String data1 = cs.getString(0);
				String type = cs.getString(1);
				if (type.equals("vnd.android.cursor.item/phone_v2")) {
					cf.setNumber(data1);
				}else if (type.equals("vnd.android.cursor.item/name")) {
					cf.setName(data1);
				}
			}
			if (cf.getNumber()!=null&&cf.getNumber().length()>0&&cf.getName()!=null&&cf.getName().length()>0) {
				contentinfo.add(cf);
			}
			cs.close();
		}
		csr.close();
		return contentinfo;
	}
}
