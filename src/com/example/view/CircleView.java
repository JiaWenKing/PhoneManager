package com.example.view;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
/**
 * 主界面圆的布局
 * */
//			自定义view复制他的全路径
public class CircleView extends View {
	/**
	 * 自定义View 的步骤
	 * 1重写构造方法
	 * 2重写onMeasure 测量方法  用来测量设置View的宽高   并且设置进去
	 * 3  ondraw 把自定义view 画在画布上   
	 * */
	Paint p=new Paint();//画笔
	RectF rf;//弧度
	int startAngle=-90;//起点  圆的起点12点钟是-90度
	int stopAngle=0;//结束位置
	int[] back=new int[]{-10,-10,-10,-15,-18,-20};//回退数组
	int backIndex=0;//数组下标  防止越界
	int [] go=new int[]{8,8,8,10,10,12};//前进数组
	int goIndex=0;//回退下标
	int bgcolor=0;//背景颜色
	boolean isstart=false;//是否在画   
	int state=0;//0是回退 1是前进
	
	public CircleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		stopAngle=360;
		//刷新ui
		postInvalidate();
		bgcolor=Color.RED;
		isstart=false;//默认未执行
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		//获得测量的宽高
		int w = MeasureSpec.getSize(widthMeasureSpec);
		int h = MeasureSpec.getSize(heightMeasureSpec);
		//根据实际宽高实例化弧度
		rf=new RectF(0, 0, w, h);
		//把测量出来的宽高设置给view
		setMeasuredDimension(w, h);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		p.setColor(bgcolor);
		p.setAntiAlias(true);//默认不能逆时针画    此方法
		canvas.drawArc(rf, startAngle, stopAngle, true, p);
	}
	
	public void setAngle (final int angle){
		if (isstart) {
			return;
		}
		isstart=true;
		//new 定时器
		final Timer timer=new Timer();
		//timer的异步
		TimerTask task=new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				switch (state) {
				case 0:
					stopAngle+=back[backIndex++];//回退  
					if (backIndex>=back.length-1) {
						backIndex=0;//如果下标到最后一个就
					}
					postInvalidate();//刷新
					if (stopAngle<=0) {
					
						stopAngle=0;
						backIndex=0;
						isstart=false;
						state=1;
					}//如果当前刻度值小于等于0  
					break;
				case 1:
					stopAngle+=go[goIndex++];//前进
					if (goIndex>=go.length-1) {
						goIndex=0;
						postInvalidate();
						if (stopAngle>=angle) {
							timer.cancel();
							goIndex=0;
							isstart=false;
							stopAngle=angle;//等于外部传进来的值位置
							state=0;//状态归零
						}//如果当前刻度值大于等于传进来的值  应停止
					}
					break;
				default:
					break;
				}
				
			}
		};
		timer.schedule(task, 24, 24);//外部调用方法前需启动方法  task通过timer启动
	}
}
