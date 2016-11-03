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
 * ������Բ�Ĳ���
 * */
//			�Զ���view��������ȫ·��
public class CircleView extends View {
	/**
	 * �Զ���View �Ĳ���
	 * 1��д���췽��
	 * 2��дonMeasure ��������  ������������View�Ŀ��   �������ý�ȥ
	 * 3  ondraw ���Զ���view ���ڻ�����   
	 * */
	Paint p=new Paint();//����
	RectF rf;//����
	int startAngle=-90;//���  Բ�����12������-90��
	int stopAngle=0;//����λ��
	int[] back=new int[]{-10,-10,-10,-15,-18,-20};//��������
	int backIndex=0;//�����±�  ��ֹԽ��
	int [] go=new int[]{8,8,8,10,10,12};//ǰ������
	int goIndex=0;//�����±�
	int bgcolor=0;//������ɫ
	boolean isstart=false;//�Ƿ��ڻ�   
	int state=0;//0�ǻ��� 1��ǰ��
	
	public CircleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		stopAngle=360;
		//ˢ��ui
		postInvalidate();
		bgcolor=Color.RED;
		isstart=false;//Ĭ��δִ��
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		//��ò����Ŀ��
		int w = MeasureSpec.getSize(widthMeasureSpec);
		int h = MeasureSpec.getSize(heightMeasureSpec);
		//����ʵ�ʿ��ʵ��������
		rf=new RectF(0, 0, w, h);
		//�Ѳ��������Ŀ�����ø�view
		setMeasuredDimension(w, h);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		p.setColor(bgcolor);
		p.setAntiAlias(true);//Ĭ�ϲ�����ʱ�뻭    �˷���
		canvas.drawArc(rf, startAngle, stopAngle, true, p);
	}
	
	public void setAngle (final int angle){
		if (isstart) {
			return;
		}
		isstart=true;
		//new ��ʱ��
		final Timer timer=new Timer();
		//timer���첽
		TimerTask task=new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				switch (state) {
				case 0:
					stopAngle+=back[backIndex++];//����  
					if (backIndex>=back.length-1) {
						backIndex=0;//����±굽���һ����
					}
					postInvalidate();//ˢ��
					if (stopAngle<=0) {
					
						stopAngle=0;
						backIndex=0;
						isstart=false;
						state=1;
					}//�����ǰ�̶�ֵС�ڵ���0  
					break;
				case 1:
					stopAngle+=go[goIndex++];//ǰ��
					if (goIndex>=go.length-1) {
						goIndex=0;
						postInvalidate();
						if (stopAngle>=angle) {
							timer.cancel();
							goIndex=0;
							isstart=false;
							stopAngle=angle;//�����ⲿ��������ֵλ��
							state=0;//״̬����
						}//�����ǰ�̶�ֵ���ڵ��ڴ�������ֵ  Ӧֹͣ
					}
					break;
				default:
					break;
				}
				
			}
		};
		timer.schedule(task, 24, 24);//�ⲿ���÷���ǰ����������  taskͨ��timer����
	}
}
