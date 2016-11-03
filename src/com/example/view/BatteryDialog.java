package com.example.view;

import com.example.phonemanager.R;
import com.example.phonemanager.R.drawable;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;
/**
 * ������ʾ
 * **/
public class BatteryDialog {
	private static TextView tv_dianliang;
	private static TextView tv_wendu;
	private static Dialog dialog;

	public static void showDialog(Context context,String dianliang,String wendu){
		LayoutInflater layout=LayoutInflater.from(context);//���ּ�����
		View view=layout.inflate(R.layout.battery_dialog, null);//��Ӳ���
		tv_dianliang = (TextView) view.findViewById(R.id.tv_dianliang);
		tv_wendu = (TextView) view.findViewById(R.id.tv_wendu);
		dialog = new Dialog(context,R.style.my_dialog);
		Window window=dialog.getWindow();//����
		LayoutParams lp=new LayoutParams();
		lp.width=LayoutParams.WRAP_CONTENT;
		lp.height=LayoutParams.WRAP_CONTENT;
		lp.gravity=Gravity.CENTER;
		window.setAttributes(lp);
		dialog.setContentView(view,lp);
		tv_dianliang.setText("Electricity:"+dianliang+"%");
		tv_wendu.setText("Temperature:"+wendu+"��");
		dialog.show();
		
	}
	public static void dismiss(){
		if (dialog!=null) {
			dialog.dismiss();
			dialog=null;
		}
	}
}
