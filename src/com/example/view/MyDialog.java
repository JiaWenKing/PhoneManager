package com.example.view;

import com.example.phonemanager.R;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
/**
 * º”‘ÿÃı
 * **/
public class MyDialog {
	private static ImageView img;
	private static Dialog dialog;

	public static void ShowDialog(Context context){
		LayoutInflater layout = LayoutInflater.from(context);
		View view = layout.inflate(R.layout.load_dialog, null);
		Animation loadAnimation = AnimationUtils.loadAnimation(context, R.anim.load_rotate);
		img = (ImageView) view.findViewById(R.id.iv);
		dialog = new Dialog(context, R.style.my_dialog);
//		Window window = dialog.getWindow();
//		LayoutParams a=new LayoutParams();
//		a.width=LayoutParams.WRAP_CONTENT;
//		a.height=LayoutParams.WRAP_CONTENT;
//		a.gravity=Gravity.CENTER;
//		window.setAttributes(a);
		dialog.setContentView(view);
		img.startAnimation(loadAnimation);
		dialog.show();
	}
	public static void dismiss(){
		if (dialog!=null) {
			dialog.dismiss();
			dialog=null;
		}
	}
	
	
}
