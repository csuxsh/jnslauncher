package com.jslauncher.steven;

import java.util.StringTokenizer;

import com.jslauncher.launcher.HandleView;
import com.jslauncher.launcher.Launcher;
import com.jslauncher.launcher.LauncherApplication;
import com.jslauncher.launcher.R;


import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;


/**
 * Launcher js 的MODLE类
 */


public class FrameIcon {

	private Launcher mLauncher;

	FrameLayout icon_handle;  				// 整个图标的handle
	PopwindowIcon	icon_button;			// 整个主体部分包括文字和ICON图片
	PopwindowIcon 	icon_bubble;			// 水泡背景层
	HandleView apps_handle;				//  all_apps_buton 沿用的原本 launcher2的类型没改动
	PopView popwindow;		
	MyFrame frame;// 与ICON对应的弹出框
	boolean isLoadOK = false;
	private final static float TOKEN_BG_SCALE = 1.3f;

	public FrameLayout getIcon_handle() {
		return icon_handle;
	}

	public void setIcon_handle(FrameLayout icon_handle) {
		this.icon_handle = icon_handle;
	}

	public PopwindowIcon getIcon_button() {
		return icon_button;
	}

	public void setIcon_button(PopwindowIcon icon_button) {
		this.icon_button = icon_button;
	}

	public PopwindowIcon getIcon_bubble() {
		return icon_bubble;
	}

	public void setIcon_bubble(PopwindowIcon icon_bubble) {
		this.icon_bubble = icon_bubble;
	}

	public ImageView getIcon_token() {
		return icon_token;
	}

	public void setIcon_token(ImageView icon_token) {
		this.icon_token = icon_token;
	}
	ImageView 	icon_token;
	@SuppressWarnings("unused")
	private int 	icon_applist_type;

	private Animation tolargeanim;
	private Animation tonormalanim;




	public FrameIcon(Launcher launcher, int resid, MyFrame frame)
	{
		icon_handle = (FrameLayout) launcher.findViewById(resid);
		popwindow = (PopView) launcher.getLayoutInflater().inflate(R.layout.popwindow, null, false);
		mLauncher = launcher;
		this.frame = frame; 
		@SuppressWarnings("unused")
		int text_num = 0;

		switch(resid)
		{
		case R.id.tv_handle:
			icon_button = (PopwindowIcon) launcher.findViewById(R.id.tv);
			icon_button.setCompoundDrawablesWithIntrinsicBounds(null, launcher.getResources().getDrawable(R.drawable.tv), null, null);
			icon_button.setHandle(icon_handle);
			icon_bubble = (PopwindowIcon) launcher.findViewById(R.id.tv_bubble);
			icon_applist_type = AppList.TV;
			icon_button.setText(R.string.tv_icon_text);
			icon_button.setLauncher(launcher);
			icon_token = (ImageView) launcher.findViewById(R.id.tv_token_bg);
			//text_num = icon_button.getText().length();
			/*
				if(text_num > 8)
					text_num = 8;
				if(text_num < 4)
					text_num =4;

				icon_token.setScaleX((text_num * icon_button.getTextSize()) /175 );
			 */
			if(icon_button.getText().length() * icon_button.getTextSize() > 170)
				icon_token.setScaleX(FrameIcon.TOKEN_BG_SCALE);

			Thread tvth = new Thread(new Runnable()
			{

				@Override
				public void run() {
					// TODO Auto-generated method stub
					popwindow.setBackgroundResource(R.anim.tv_pop);
				}

			});
			//	popwindow.setBackgroundResource(R.anim.tv_pop);
			tvth.start();
			createList(AppList.TV, "tv");
			icon_button.apps = ((LauncherApplication)mLauncher.getApplicationContext()).mApplist[AppList.TV];
			break;
		case R.id.web_handle:
			icon_button = (PopwindowIcon) launcher.findViewById(R.id.web);
			icon_button.setCompoundDrawablesWithIntrinsicBounds(null, launcher.getResources().getDrawable(R.drawable.web), null, null);
			icon_button.setHandle(icon_handle);
			icon_bubble = (PopwindowIcon) launcher.findViewById(R.id.web_bubble);
			icon_button.setText(R.string.web_icon_text);
			icon_button.setLauncher(launcher);
			icon_token = (ImageView) launcher.findViewById(R.id.web_token_bg);
			/*
				text_num = icon_button.getText().length();
				if(text_num > 8)
					text_num = 8;
				if(text_num < 4)
					text_num =4;

				icon_token.setScaleX((text_num * icon_button.getTextSize()) /175 );
			 */
			if(icon_button.getText().length() * icon_button.getTextSize() > 170)

				icon_token.setScaleX(FrameIcon.TOKEN_BG_SCALE);
			icon_applist_type = AppList.WEB;
			Thread webth = new Thread(new Runnable()
			{

				@Override
				public void run() {
					// TODO Auto-generated method stub
					popwindow.setBackgroundResource(R.anim.web_pop);
				}

			});
			webth.start();
			//popwindow.setBackgroundResource(R.anim.web_pop);
			createList(AppList.WEB, "web");
			icon_button.apps = ((LauncherApplication)mLauncher.getApplicationContext()).mApplist[AppList.WEB];
			break;
		case R.id.media_handle:
			icon_button = (PopwindowIcon) launcher.findViewById(R.id.media);
			icon_button.setCompoundDrawablesWithIntrinsicBounds(null, launcher.getResources().getDrawable(R.drawable.media), null, null);
			icon_button.setHandle(icon_handle);
			icon_bubble = (PopwindowIcon) launcher.findViewById(R.id.media_bubble);
			icon_applist_type = AppList.MEDIA;
			icon_button.setText(R.string.media_icon_text);
			icon_button.setLauncher(launcher);
			icon_token = (ImageView) launcher.findViewById(R.id.media_token_bg);
			/*
				text_num = icon_button.getText().length();
				if(text_num > 8)
					text_num = 8;
				if(text_num < 4)
					text_num =4;

				icon_token.setScaleX((text_num * icon_button.getTextSize()) /175 );
			 */
			if(icon_button.getText().length() * icon_button.getTextSize() > 170)

				icon_token.setScaleX(FrameIcon.TOKEN_BG_SCALE);
			Thread meth = new Thread(new Runnable()
			{

				@Override
				public void run() {
					// TODO Auto-generated method stub
					popwindow.setBackgroundResource(R.anim.meida_pop);
				}

			});
			meth.start();
			//popwindow.setBackgroundResource(R.anim.meida_pop);
			createList(AppList.MEDIA, "media");
			icon_button.apps = ((LauncherApplication)mLauncher.getApplicationContext()).mApplist[AppList.MEDIA];
			break;
		case R.id.all_apps_handle:
			apps_handle = (HandleView) launcher.findViewById(R.id.all_apps_button);
			icon_bubble = (PopwindowIcon) launcher.findViewById(R.id.apps_bubble);
			icon_bubble.setCompoundDrawablesWithIntrinsicBounds(null,null,
					null, null);
			icon_token = (ImageView) launcher.findViewById(R.id.apps_token_bg);
			/*
				text_num = apps_handle.getText().length();
				if(text_num > 8)
					text_num = 8;
				if(text_num < 4)
					text_num =4;

				icon_token.setScaleX((text_num * apps_handle.getTextSize()) /175 );
			 */
			if(apps_handle.getText().length() * apps_handle.getTextSize() > 170)

				icon_token.setScaleX(FrameIcon.TOKEN_BG_SCALE);
			popwindow.setBackgroundResource(Color.TRANSPARENT);
			icon_applist_type = 0;
			break;
		case R.id.shop_handle:
			icon_button = (PopwindowIcon)launcher.findViewById(R.id.shop);
			icon_button.setHandle(icon_handle);
			icon_bubble = (PopwindowIcon)launcher.findViewById(com.jslauncher.launcher.R.id.shop_bubble);
			icon_button.setCompoundDrawablesWithIntrinsicBounds(null, launcher.getResources().getDrawable(R.drawable.market), null, null);
			icon_button.setText(R.string.market_icon_text);
			icon_button.setLauncher(launcher);

			icon_token = (ImageView) launcher.findViewById(R.id.market_token_bg);

			//	icon_token.setScaleX((icon_button.getText().length() * icon_button.getTextSize())/175 );
			/*
				text_num = icon_button.getText().length();
				if(text_num > 8)
					text_num = 8;
				if(text_num < 4)
					text_num =4;

				icon_token.setScaleX((text_num * icon_button.getTextSize()) /175 );
			 */
			if(icon_button.getText().length() * icon_button.getTextSize() > 170)

				icon_token.setScaleX(FrameIcon.TOKEN_BG_SCALE);
			icon_applist_type = AppList.MARKET;
			Thread math = new Thread(new Runnable()
			{

				@Override
				public void run() {
					// TODO Auto-generated method stub
					popwindow.setBackgroundResource(R.anim.market_pop);
				}

			});
			math.start();
			//popwindow.setBackgroundResource(R.anim.market_pop);
			createList(AppList.MARKET, "market");
			icon_button.apps = ((LauncherApplication)mLauncher.getApplicationContext()).mApplist[AppList.MARKET];
			break;
		case R.id.game_handle:
			icon_button = (PopwindowIcon)launcher.findViewById(R.id.game);
			icon_button.setHandle(icon_handle);
			icon_bubble = (PopwindowIcon)launcher.findViewById(com.jslauncher.launcher.R.id.game_bubble);
			icon_button.setCompoundDrawablesWithIntrinsicBounds(null, launcher.getResources().getDrawable(R.drawable.game), null, null);
			icon_button.setText(R.string.game_icon_text);
			icon_button.setLauncher(launcher);
			icon_token = (ImageView) launcher.findViewById(R.id.game_token_bg);
			//		icon_token.setScaleX((icon_button.getText().length() * icon_button.getTextSize()) /175);
			/*
				text_num = icon_button.getText().length();
				if(text_num > 8)
					text_num = 8;
				if(text_num < 4)
					text_num =4;

				icon_token.setScaleX((text_num * icon_button.getTextSize()) /175 );
			 */
			if(icon_button.getText().length() * icon_button.getTextSize() > 170)

				icon_token.setScaleX(FrameIcon.TOKEN_BG_SCALE);
			icon_applist_type = AppList.GAME;
			Thread gath = new Thread(new Runnable()
			{

				@Override
				public void run() {
					// TODO Auto-generated method stub
					popwindow.setBackgroundResource(R.anim.game_pop);
				}

			});
			gath.start();
			//popwindow.setBackgroundResource(R.anim.game_pop);
			createList(AppList.GAME, "game");
			icon_button.apps = ((LauncherApplication)mLauncher.getApplicationContext()).mApplist[AppList.GAME];

			break;
		case R.id.setting_handle:
			icon_button = (PopwindowIcon)launcher.findViewById(R.id.setting);
			icon_button.setHandle(icon_handle);
			icon_bubble = (PopwindowIcon)launcher.findViewById(com.jslauncher.launcher.R.id.setting_bubble);
			icon_button.setCompoundDrawablesWithIntrinsicBounds(null, launcher.getResources().getDrawable(R.drawable.setup), null, null);
			icon_button.setText(R.string.setup_icon_text);
			icon_button.setLauncher(launcher);
			icon_token = (ImageView) launcher.findViewById(R.id.setup_token_bg);
			/*
				text_num = icon_button.getText().length();
				if(text_num > 8)
					text_num = 8;
				if(text_num < 4)
					text_num =4;

				icon_token.setScaleX((text_num * icon_button.getTextSize()) /175 );
			 */
			if(icon_button.getText().length() * icon_button.getTextSize() > 170)

				icon_token.setScaleX(FrameIcon.TOKEN_BG_SCALE);

			icon_applist_type = AppList.SETUP;
			Thread seth = new Thread(new Runnable()
			{

				@Override
				public void run() {
					// TODO Auto-generated method stub
					popwindow.setBackgroundResource(R.anim.setup_pop);
				}

			});
			seth.start();
			//popwindow.setBackgroundResource(R.anim.setup_pop);
			createList(AppList.SETUP, "setup");
			icon_button.apps = ((LauncherApplication)mLauncher.getApplicationContext()).mApplist[AppList.SETUP];
			break;
		default:
			break;


		}
		if(resid != R.id.all_apps_handle)
			launcher.getDragController().addDropTarget(icon_button);

		tolargeanim = AnimationUtils.loadAnimation(launcher, R.anim.icon_to_large);	
		tolargeanim.setFillAfter(true);
		tolargeanim.setFillBefore(false);
		tonormalanim = AnimationUtils.loadAnimation(launcher, R.anim.icon_to_nomal);	
		tonormalanim.setFillAfter(true);
		tonormalanim.setFillBefore(true);

	}

	public void startLargeAnimation()
	{
		icon_handle.startAnimation(tolargeanim);
		icon_token.setVisibility(View.VISIBLE);
	}
	public void startNornalAnimation()
	{
		icon_handle.startAnimation(tonormalanim);
		icon_token.setVisibility(View.GONE);
	}

	private void createList(final int type, final String name)
	{
		Thread loadlist = new Thread(new Runnable(){
			Cursor cursor = null;
			@Override
			synchronized public void run() {
				// TODO Auto-generated method stub
				AppList apps = new AppList();
				apps.setmLauncher(mLauncher);
				String listname = name;
				String str = "";
				Log.d("loadlist", type+"");
				Log.d("loadlist", name);
				cursor = frame.apph.Qurey(listname);
				if(cursor.moveToFirst())
				{	
					Log.d("steven_db", "cursor is not none");	
					str = cursor.getString(cursor.getColumnIndex("_description"));
				}
				else
				{
					while(!cursor.moveToFirst())
					{	
						frame.CopyDatabase();
						try {
							Thread.sleep(5);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						cursor = frame.apph.Qurey(listname);
						if(cursor.moveToFirst())
						{	
							Log.d("steven_db", "cursor is not none");	
							str = cursor.getString(cursor.getColumnIndex("_description"));
						}
					}
				}

				Log.d("loadlist", "mlist = "+str);

				StringTokenizer token = new StringTokenizer(str, "^");
				while(token.hasMoreTokens())
				{	
					String pakname = token.nextToken();
					while(!apps.add(pakname))
					{
						try {
							Thread.sleep(5);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				switch(type)
				{
				case AppList.TV:
					apps.type = AppList.TV;
					apps.name = "tv";
					((LauncherApplication)mLauncher.getApplicationContext()).mApplist[AppList.TV] = apps;
					listname = "tv";
					isLoadOK = true;
					break;
				case AppList.GAME:
					apps.type = AppList.GAME;
					apps.name = "game";
					((LauncherApplication)mLauncher.getApplicationContext()).mApplist[AppList.GAME] = apps;
					listname = "game";
					isLoadOK = true;
					break;
				case AppList.MEDIA:
					apps.name = "media";
					apps.type = AppList.MEDIA;
					((LauncherApplication)mLauncher.getApplicationContext()).mApplist[AppList.MEDIA] = apps;
					listname = "media";
					isLoadOK = true;
					break;
				case AppList.WEB:
					apps.name = "web";
					apps.type = AppList.WEB;
					((LauncherApplication)mLauncher.getApplicationContext()).mApplist[AppList.WEB] = apps;
					listname = "web";
					isLoadOK = true;
					break;
				case AppList.SETUP:
					apps.type = AppList.SETUP;
					apps.name = "setup";
					((LauncherApplication)mLauncher.getApplicationContext()).mApplist[AppList.SETUP] = apps;
					listname = "setup";
					isLoadOK = true;
					break;
				case AppList.MARKET:
					apps.type = AppList.MARKET;
					apps.name = "market";
					((LauncherApplication)mLauncher.getApplicationContext()).mApplist[AppList.MARKET] = apps;
					listname = "market";
					isLoadOK = true;
					break;
				default:
					break;
				}

			}

		});
		loadlist.start();
	}
}
