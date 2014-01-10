package com.jslauncher.steven;

import com.jslauncher.launcher.R;


import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.PopupWindow;

/**
 * �̳���SDK��PopupWindow��Ϊ����Ӵ򿪺͹رյĶ�������
 */
public class MyPopupWindow extends PopupWindow {
	
	private int apptype;
	
	public void setApptype(int apptype) {
		this.apptype = apptype;
	}
	
	private MyFrame frame;
	private PopView popwindow;
	
	public MyPopupWindow()
	{
		super();
	}
	public MyPopupWindow(Context context)
	{
		super(context);
	}
	public MyPopupWindow(MyFrame frame, View view, int i, int j)
	{
		super(view, i, j); 
		this.frame = frame;
	}
	public MyPopupWindow(MyFrame frame, PopView view, int i, int j, boolean focusable)
	{
		super(view, i, j, focusable); 
		this.frame = frame;
		this.popwindow = view;
	}
	public MyPopupWindow(View popwindow, int i, int j) {
		// TODO Auto-generated constructor stub
		super(popwindow, i, j);
	}
	public void update()
	{
		
		super.update();
		switch(apptype)
		{
			case AppList.TV:
				frame.tv_icon.icon_bubble.setBackgroundResource(R.anim.bubble);
				AnimationDrawable animationDrawable_tv = (AnimationDrawable)frame.tv_icon.icon_bubble.getBackground();  
				// �����Ƿ���������  
				if(animationDrawable_tv.isRunning()){  
					//ֹͣ��������  
					animationDrawable_tv.stop();  
				}  
				else{  
					//��ʼ���߼�����������  
				//	frame.tv_handle.clearAnimation();
					animationDrawable_tv.start();  
				}  
				break;
			case AppList.WEB:
				frame.web_icon.icon_bubble.setBackgroundResource(R.anim.bubble);
				AnimationDrawable animationDrawable_web = (AnimationDrawable)frame.web_icon.icon_bubble.getBackground();  

				// �����Ƿ���������  
				if(animationDrawable_web.isRunning()){  
					//ֹͣ��������  
					animationDrawable_web.stop();  
				}  
				else{  
					//��ʼ���߼�����������  
					animationDrawable_web.start();  
			//		frame.web_handle.clearAnimation();
				}  
				break;
			case AppList.MEDIA:
				frame.media_icon.icon_bubble.setBackgroundResource(R.anim.bubble);
				AnimationDrawable animationDrawable_media = (AnimationDrawable)frame.media_icon.icon_bubble.getBackground();  

				// �����Ƿ���������  
				if(animationDrawable_media.isRunning()){  
					//ֹͣ��������  
					animationDrawable_media.stop();
					
				}  
				else{  
					//��ʼ���߼�����������  
					animationDrawable_media.start();  
				//	frame.meida_handle.clearAnimation();
				}  
				
				break;
			case AppList.SETUP:
				frame.setup_icon.icon_bubble.setBackgroundResource(R.anim.bubble);
				AnimationDrawable animationDrawable_setup = (AnimationDrawable)frame.setup_icon.icon_bubble.getBackground();  

				// �����Ƿ���������  
				if(animationDrawable_setup.isRunning()){  
					//ֹͣ��������  
					animationDrawable_setup.stop();  
				}  
				else{  
					//��ʼ���߼�����������  
					animationDrawable_setup.start();
			//		frame.setup_handle.clearAnimation();
				}  

				break;
			case AppList.GAME:
				frame.game_icon.icon_bubble.setBackgroundResource(R.anim.bubble);
				AnimationDrawable animationDrawable_game = (AnimationDrawable)frame.game_icon.icon_bubble.getBackground();  

				// �����Ƿ���������  
				if(animationDrawable_game.isRunning()){  
					//ֹͣ��������  
					animationDrawable_game.stop();  
				}  
				else{  
					//��ʼ���߼�����������  
					animationDrawable_game.start();
				//	frame.game_handle.clearAnimation();
				}  
				
				break;
			case AppList.MARKET:
				frame.market_icon.icon_bubble.setBackgroundResource(R.anim.bubble);
				AnimationDrawable animationDrawable_shop = (AnimationDrawable)frame.market_icon.icon_bubble.getBackground();  

				// �����Ƿ���������  
				if(animationDrawable_shop.isRunning()){  
					//ֹͣ��������  
					animationDrawable_shop.stop();  
				}  
				else{  
					//��ʼ���߼�����������  
					animationDrawable_shop.start();  
				//	frame.market_handle.clearAnimation();
				}  
				
				break;
		}
	}
	public void dismiss()
	{
		//���ظ��� dismiss�ر�ʱ�޸�����
		super.dismiss();
		
		frame.mLauncher.getDragController().removeDropTarget(popwindow);
		
		switch(apptype)
		{
			case AppList.TV:
				frame.tv_icon.icon_bubble.setBackgroundResource(R.drawable.buble_00);
			//	frame.tv_handle.startAnimation(frame.tvtoNomalAnimation);
				break;
			case AppList.WEB:
				frame.web_icon.icon_bubble.setBackgroundResource(R.drawable.buble_00);
			//	frame.web_handle.startAnimation(frame.webtoNomalAnimation);
				break;
			case AppList.MEDIA:
				frame.media_icon.icon_bubble.setBackgroundResource(R.drawable.buble_00);
			//	frame.meida_handle.startAnimation(frame.mediatoNomalAnimation);
				break;
			case AppList.SETUP:
				frame.setup_icon.icon_bubble.setBackgroundResource(R.drawable.buble_00);
			//	frame.setup_handle.startAnimation(frame.setuptoNomalAnimation);
				break;
			case AppList.GAME:
				frame.game_icon.icon_bubble.setBackgroundResource(R.drawable.buble_00);
			//	frame.game_handle.startAnimation(frame.gametoNomalAnimation);
				break;
			case AppList.MARKET:
				frame.market_icon.icon_bubble.setBackgroundResource(R.drawable.buble_00);
		//		frame.market_handle.startAnimation(frame.markettoNomalAnimation);
				break;
		}
	
	}

}
