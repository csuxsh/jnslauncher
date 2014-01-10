package com.jslauncher.steven;






import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnHoverListener;

import android.widget.Toast;

import com.jslauncher.launcher.Launcher;
import com.jslauncher.launcher.LauncherApplication;
import com.jslauncher.launcher.R;


/**
 * lancher js 修改部分的入口
 */

public class MyFrame{
	
	
	public static final String ACTION_SAVE_LIST = "com.steven.launcher.SAVE_LIST";
	
	final DBHelper dbh;
	public PopWindow popwindow;
	public AppList apps;
	public boolean ispopshown  = false;
	public Handler MessageHandler;
	public static boolean isLoadOk = false;
    
	AppHelper apph;
   	FrameIcon tv_icon;
	FrameIcon web_icon;
	FrameIcon market_icon;
	FrameIcon media_icon;
	FrameIcon apps_icon;
	FrameIcon setup_icon;
	FrameIcon game_icon;
	int current_hover_icon;
	int current_fause_icon;
	Launcher mLauncher;
	boolean update = false;

	private int Pre_listype;
	private int mBg_pic_res;
	private int perbuttonstat;

	private OnClickListener ocl;
	private OnFocusChangeListener ofl;
	private OnHoverListener ohl;

    @SuppressLint({ "SdCardPath", "SdCardPath" })
	void CopyDatabase()
    {
    	String filename = android.os.Environment.getExternalStorageDirectory() + "/_jns_launcher";
    	File f = new File(filename);
    	AssetFileDescriptor resFile = mLauncher.getResources().openRawResourceFd(R.raw._jns_launcher);
    	if(f.exists() && (f.length() == resFile.getLength()))
    		return;
    	
    	InputStream is = mLauncher.getResources().openRawResource(R.raw._jns_launcher);
    	try {
			FileOutputStream fos = new FileOutputStream(filename);
			byte[] buffer = new byte[8192];
			int count = 0;
			while((count = is.read(buffer))>=0)
			{
				fos.write(buffer, 0, count);
			}
			fos.close();
			is.close();
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
   

	@SuppressLint({ "NewApi", "NewApi", "NewApi", "NewApi", "NewApi", "NewApi", "NewApi" })
	public MyFrame(Launcher launcher)
	{

		this.mLauncher = launcher;
		dbh = DBHelper.getDBHelper(launcher);
		apph = new AppHelper(dbh);
		CopyDatabase();
		Intent serverintent = new Intent(mLauncher, CheckUpataThread.class);
		mLauncher.startService(serverintent);
		Log.d("frameicon_loadstart", System.currentTimeMillis()+"");
		tv_icon = new FrameIcon(mLauncher, R.id.tv_handle, this);
		web_icon = new FrameIcon(mLauncher, R.id.web_handle, this);
		media_icon = new FrameIcon(mLauncher, R.id.media_handle, this);
		market_icon = new FrameIcon(mLauncher, R.id.shop_handle, this);
		apps_icon = new FrameIcon(mLauncher, R.id.all_apps_handle, this);
		setup_icon = new FrameIcon(mLauncher, R.id.setting_handle, this);
		game_icon = new FrameIcon(mLauncher, R.id.game_handle, this);
		Log.d("frameicon_loadend", System.currentTimeMillis()+"");

		ispopshown = false;
		@SuppressWarnings("unused")
		LauncherApplication  application = (LauncherApplication)this.mLauncher.getApplicationContext();

		popwindow = new PopWindow(this, MyFrame.this.mLauncher, tv_icon.icon_button);

		ohl = new OnHoverListener()
		{


			@TargetApi(14)
			@SuppressLint({ "NewApi", "NewApi", "NewApi", "NewApi", "NewApi", "NewApi", "NewApi" })
			@Override
			public boolean onHover(View v, MotionEvent arg1) {
				// TODO Auto-generated method stub

				@SuppressWarnings("unused")
				boolean toNormal = false;
				int what = arg1.getAction(); 
				Log.d("current_hover_icon", ""+current_hover_icon);

				if(current_fause_icon == R.id.all_apps_button)
					current_fause_icon = R.id.all_apps_handle;

				if((current_fause_icon != 0) && (current_fause_icon != v.getId()))
				{
					toNormal = true;
					FrameIcon tmp = new FrameIcon(mLauncher, current_fause_icon, MyFrame.this);
					tmp.startNornalAnimation();
				}


				switch(v.getId())
				{
				case com.jslauncher.launcher.R.id.tv_handle:
					if(what == MotionEvent.ACTION_HOVER_ENTER && perbuttonstat != MotionEvent.BUTTON_PRIMARY)
					{	
						//tv_handle.setBackgroundColor(Color.TRANSPARENT);
						tv_icon.startLargeAnimation();
						current_hover_icon = R.id.tv_handle;
					}
					if(what == MotionEvent.ACTION_HOVER_MOVE)
					{
						current_hover_icon = R.id.tv_handle;
					}
					if(what == MotionEvent.ACTION_HOVER_EXIT && arg1.getButtonState()!= MotionEvent.BUTTON_PRIMARY)
					{	
						if(current_fause_icon == 0)	
							tv_icon.startNornalAnimation();
						current_hover_icon = 0;
						Log.d("hover", "hover exit");
					}
					break;
				case com.jslauncher.launcher.R.id.web_handle:
					if(what == MotionEvent.ACTION_HOVER_ENTER && perbuttonstat != MotionEvent.BUTTON_PRIMARY)
					{	
						//	web_handle.setBackgroundColor(Color.TRANSPARENT);
						web_icon.startLargeAnimation();
						current_hover_icon = R.id.web_handle;
					}	
					if(what == MotionEvent.ACTION_HOVER_MOVE)
					{
						current_hover_icon = R.id.web_handle;
					}
					if(what == MotionEvent.ACTION_HOVER_EXIT && arg1.getButtonState()!= MotionEvent.BUTTON_PRIMARY)
					{
						if(current_fause_icon == 0)
							web_icon.startNornalAnimation();
						current_hover_icon = 0;
						Log.d("hover", "hover exit");
					}
					break;
				case com.jslauncher.launcher.R.id.game_handle:
					if(what == MotionEvent.ACTION_HOVER_ENTER && perbuttonstat != MotionEvent.BUTTON_PRIMARY)
					{	
						//game_handle.setBackgroundColor(Color.TRANSPARENT);
						game_icon.startLargeAnimation();
						current_hover_icon = R.id.game_handle;
						Log.d("hover", "hover exit");
					}
					if(what == MotionEvent.ACTION_HOVER_MOVE)
					{
						current_hover_icon = R.id.game_handle;
					}
					if(what == MotionEvent.ACTION_HOVER_EXIT && arg1.getButtonState()!= MotionEvent.BUTTON_PRIMARY)
					{	
						if(current_fause_icon == 0)
							game_icon.startNornalAnimation();
						current_hover_icon = 0;
						Log.d("hover", "hover exit");
					}
					break;
				case com.jslauncher.launcher.R.id.setting_handle:
					if(what == MotionEvent.ACTION_HOVER_ENTER && perbuttonstat != MotionEvent.BUTTON_PRIMARY)
					{
						//setup_handle.setBackgroundColor(Color.TRANSPARENT);
						setup_icon.startLargeAnimation();
						current_hover_icon = R.id.setting_handle;
					}
					if(what == MotionEvent.ACTION_HOVER_MOVE)
					{
						current_hover_icon = R.id.setting_handle;
					}
					if(what == MotionEvent.ACTION_HOVER_EXIT && arg1.getButtonState()!= MotionEvent.BUTTON_PRIMARY)
					{	
						if(current_fause_icon == 0)
							setup_icon.startNornalAnimation();
						current_hover_icon = 0;
						Log.d("hover", "hover exit");
					}
					break;
				case com.jslauncher.launcher.R.id.shop_handle:
					if(what == MotionEvent.ACTION_HOVER_ENTER && perbuttonstat != MotionEvent.BUTTON_PRIMARY)
					{
						//market_handle.setBackgroundColor(Color.TRANSPARENT);
						market_icon.startLargeAnimation();
						current_hover_icon = R.id.shop_handle;
					}
					if(what == MotionEvent.ACTION_HOVER_MOVE)
					{
						current_hover_icon = R.id.shop_handle;
					}
					if(what == MotionEvent.ACTION_HOVER_EXIT && arg1.getButtonState()!= MotionEvent.BUTTON_PRIMARY)
					{
						if(current_fause_icon == 0)
							market_icon.startNornalAnimation();
						current_hover_icon = 0;
						Log.d("hover", "hover exit");
					}
					break;
				case com.jslauncher.launcher.R.id.media_handle:
					if(what == MotionEvent.ACTION_HOVER_ENTER && perbuttonstat != MotionEvent.BUTTON_PRIMARY)
					{	
						//meida_handle.setBackgroundColor(Color.TRANSPARENT);
						media_icon.startLargeAnimation();
						current_hover_icon = R.id.media_handle;
					}
					if(what == MotionEvent.ACTION_HOVER_MOVE)
					{
						current_hover_icon = R.id.media_handle;
					}
					if(what == MotionEvent.ACTION_HOVER_EXIT && arg1.getButtonState()!= MotionEvent.BUTTON_PRIMARY)
					{
						if(current_fause_icon == 0)
							media_icon.startNornalAnimation();
						current_hover_icon = 0;
						Log.d("hover", "hover exit");
					}
					break;
				case com.jslauncher.launcher.R.id.all_apps_button :
					if(what == MotionEvent.ACTION_HOVER_ENTER && perbuttonstat != MotionEvent.BUTTON_PRIMARY)
					{
						//apps_handle.setBackgroundColor(Color.TRANSPARENT);
						apps_icon.startLargeAnimation();
						current_hover_icon = R.id.all_apps_button;
					}
					if(what == MotionEvent.ACTION_HOVER_MOVE)
					{
						current_hover_icon = R.id.all_apps_button;
					}
					if(what == MotionEvent.ACTION_HOVER_EXIT && arg1.getButtonState()!= MotionEvent.BUTTON_PRIMARY)
					{
						if(current_fause_icon == 0)
							apps_icon.startNornalAnimation();
						current_hover_icon = 0;
						Log.d("hover", "hover exit");
					}
					break;
				default:
					break;
				} 
				perbuttonstat =arg1.getButtonState();
				current_fause_icon = 0;
				return false;
			}
		};

		ocl = new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				// 响应目录 button
				
			
				
				LauncherApplication application = (LauncherApplication)mLauncher.getApplicationContext();
				
				switch(v.getId())
				{
				case com.jslauncher.launcher.R.id.tv_handle:
					while(!tv_icon.isLoadOK)
					{
						Toast.makeText(mLauncher, "Loading...", Toast.LENGTH_SHORT).show();
						return;
					}
					if(ispopshown)

						mLauncher.setMrilisttype(AppList.TV );
					MyFrame.this.apps = application.mApplist[AppList.TV];
					ispopshown = true;
					tv_icon.startNornalAnimation();
					popwindow.setPopwindow(tv_icon.popwindow);
					if(popwindow.getPopupWindow(application.mApplist[AppList.TV]) == null)
						ispopshown = false;

					break;
				case com.jslauncher.launcher.R.id.web_handle:
					while(!web_icon.isLoadOK)
					{
						Toast.makeText(mLauncher, "Loading...", Toast.LENGTH_SHORT).show();
						return;
					}
					ispopshown = true;
					mLauncher.setMrilisttype(AppList.WEB );
					MyFrame.this.apps = application.mApplist[AppList.WEB];
					web_icon.startNornalAnimation();
					popwindow.setPopwindow(web_icon.popwindow);
					if(popwindow.getPopupWindow(application.mApplist[AppList.WEB]) == null)
						ispopshown = false;

					break;
				case com.jslauncher.launcher.R.id.game_handle:
					while(!game_icon.isLoadOK)
					{
						Toast.makeText(mLauncher, "Loading...", Toast.LENGTH_SHORT).show();
						return;
					}
					ispopshown = true;			
					mLauncher.setMrilisttype(AppList.GAME );
					MyFrame.this.apps = application.mApplist[AppList.GAME];
					game_icon.startNornalAnimation();
					popwindow.setPopwindow(game_icon.popwindow);
					if(popwindow.getPopupWindow(application.mApplist[AppList.GAME]) == null)
						ispopshown = false;
					break;
				case com.jslauncher.launcher.R.id.setting_handle:
					while(!setup_icon.isLoadOK)
					{
						Toast.makeText(mLauncher, "Loading...", Toast.LENGTH_SHORT).show();
						return;
					}
					ispopshown = true;					
					mLauncher.setMrilisttype(AppList.SETUP);
					MyFrame.this.apps = application.mApplist[AppList.SETUP];
					setup_icon.startNornalAnimation();
					popwindow.setPopwindow(setup_icon.popwindow);
					if(popwindow.getPopupWindow(application.mApplist[AppList.SETUP]) == null)
						ispopshown = false;
					break;
				case com.jslauncher.launcher.R.id.shop_handle:
					while(!market_icon.isLoadOK)
					{
						Toast.makeText(mLauncher, "Loading...", Toast.LENGTH_SHORT).show();
						return;
					}
					ispopshown = true;				
					mLauncher.setMrilisttype(AppList.MARKET );
					MyFrame.this.apps = application.mApplist[AppList.MARKET];
					Log.d("starnormalanimation", System.currentTimeMillis()+"");	
					market_icon.startNornalAnimation();
					popwindow.setPopwindow(market_icon.popwindow);
					Log.d("startgetpopwindow", System.currentTimeMillis()+"");
					if(popwindow.getPopupWindow(application.mApplist[AppList.MARKET]) == null)
						ispopshown = false;

					break;
				case com.jslauncher.launcher.R.id.media_handle:
					while(!media_icon.isLoadOK)
					{
						Toast.makeText(mLauncher, "Loading...", Toast.LENGTH_SHORT).show();
						return;
					}
					ispopshown = true;	
					mLauncher.setMrilisttype(AppList.MEDIA );
					MyFrame.this.apps = application.mApplist[AppList.MEDIA];
					media_icon.startNornalAnimation();
					popwindow.setPopwindow(media_icon.popwindow);
					if(popwindow.getPopupWindow(application.mApplist[AppList.MEDIA]) == null)
						ispopshown = false;

					break;
				case com.jslauncher.launcher.R.id.all_apps_handle:
					apps_icon.apps_handle.performClick();
					break;
				default:
					break;

				}
			}	
		};

		ofl = new OnFocusChangeListener(){

			@Override
			public void onFocusChange(View v, boolean arg1) {

				boolean toNormal = false;

				Log.d("current_hover_icon", ""+current_hover_icon);

				if(current_hover_icon == R.id.all_apps_button)
					current_hover_icon = R.id.all_apps_handle;

				if((current_hover_icon != 0) && (current_hover_icon != v.getId()))
				{		
					FrameIcon tmp = new FrameIcon(mLauncher, current_hover_icon, MyFrame.this);
					tmp.startNornalAnimation();
				}
				switch(v.getId())
				{
				case com.jslauncher.launcher.R.id.tv_handle:
					if(toNormal)
						tv_icon.startNornalAnimation();
					if(arg1)
					{	
						tv_icon.startLargeAnimation();
						current_fause_icon = v.getId();
					}
					else
					{	
						if(current_hover_icon ==0)
							tv_icon.startNornalAnimation();
						current_fause_icon =0 ;
					}
					break;
				case com.jslauncher.launcher.R.id.web_handle:
					if(toNormal)
						web_icon.startNornalAnimation();
					if(arg1)
					{	
						web_icon.startLargeAnimation();
						current_fause_icon = v.getId();
					}
					else
					{
						if(current_hover_icon == 0)
							web_icon.startNornalAnimation();
						current_fause_icon =0 ;
					}

					break;
				case com.jslauncher.launcher.R.id.game_handle:
					if(toNormal)
						game_icon.startNornalAnimation();
					if(arg1)
					{	
						game_icon.startLargeAnimation();
						current_fause_icon = v.getId();
					}
					else
					{
						if(current_hover_icon ==0)
							game_icon.startNornalAnimation();
						current_fause_icon =0 ;
					}
					break;
				case com.jslauncher.launcher.R.id.setting_handle:
					if(toNormal)
						setup_icon.startNornalAnimation();
					if(arg1)
					{	
						if(current_hover_icon ==0)
							setup_icon.startLargeAnimation();
						current_fause_icon = v.getId();
					}
					else
					{	
						if(current_hover_icon ==0)
							setup_icon.startNornalAnimation();
						current_fause_icon =0 ;
					}
					break;
				case com.jslauncher.launcher.R.id.shop_handle:
					if(toNormal)
						market_icon.startNornalAnimation();
					if(arg1)
					{	
						market_icon.startLargeAnimation();
						current_fause_icon = v.getId();
					}
					else
					{	
						if(current_hover_icon ==0)
							market_icon.startNornalAnimation();
						current_fause_icon =0 ;
					}
					break;
				case com.jslauncher.launcher.R.id.media_handle:
					if(toNormal)
						media_icon.startNornalAnimation();
					if(arg1)
					{	
						media_icon.startLargeAnimation();
						current_fause_icon = v.getId();
					}
					else
					{	
						if(current_hover_icon ==0)
							media_icon.startNornalAnimation();
						current_fause_icon =0 ;
					}
					break;
				case com.jslauncher.launcher.R.id.all_apps_handle:
					if(toNormal)
						apps_icon.startNornalAnimation();
					if(arg1)
					{	
						apps_icon.startLargeAnimation();
						current_fause_icon = R.id.all_apps_button;
					}
					else
					{	
						if(current_hover_icon ==0)
							apps_icon.startNornalAnimation();
						current_fause_icon =0 ;
					}
					break;
				default:
					break;


				}
				current_hover_icon =0;
			}
		};
		//注册鼠标点击事件
		tv_icon.icon_handle.setOnClickListener(ocl);
		web_icon.icon_handle.setOnClickListener(ocl);
		game_icon.icon_handle.setOnClickListener(ocl);
		media_icon.icon_handle.setOnClickListener(ocl);
		setup_icon.icon_handle.setOnClickListener(ocl);
		apps_icon.icon_handle.setOnClickListener(ocl);
		market_icon.icon_handle.setOnClickListener(ocl);
		
		//注册焦点改变事件，用于处理 左右键时图标的动画效果
		tv_icon.icon_handle.setOnFocusChangeListener(ofl);
		web_icon.icon_handle.setOnFocusChangeListener(ofl);
		game_icon.icon_handle.setOnFocusChangeListener(ofl);	
		media_icon.icon_handle.setOnFocusChangeListener(ofl);	
		setup_icon.icon_handle.setOnFocusChangeListener(ofl);
		apps_icon.apps_handle.setOnHoverListener(ohl);
		apps_icon.icon_handle.setOnFocusChangeListener(ofl);
		market_icon.icon_handle.setOnFocusChangeListener(ofl);

		// 注册鼠标悬浮事件
		tv_icon.icon_handle.setOnHoverListener(ohl);
		web_icon.icon_handle.setOnHoverListener(ohl);
		game_icon.icon_handle.setOnHoverListener(ohl);
		media_icon.icon_handle.setOnHoverListener(ohl);
		setup_icon.icon_handle.setOnHoverListener(ohl);
		//	apps_handle.setOnHoverListener(ohl);
		market_icon.icon_handle.setOnHoverListener(ohl);

		
		//设置BordcastReciver用于接收 uninstall广播更新AppList
		PopWindowReciver popreciver = new PopWindowReciver((LauncherApplication)this.mLauncher.getApplication(), this);
		IntentFilter intnet = new IntentFilter();
		intnet.addAction(Intent.ACTION_PACKAGE_REMOVED);
		intnet.addAction(MyFrame.ACTION_SAVE_LIST);
		intnet.addDataScheme("package");
		mLauncher.getApplication().registerReceiver(popreciver, intnet);
		tv_icon.icon_handle.requestFocus();
	}
    
	public int getPre_listype() {
		return Pre_listype;
	}

	public void setPre_listype(int pre_listype) {
		Pre_listype = pre_listype;
	}
	public AppList getApps() {
		return apps;

	}

	public void setApps(AppList apps) {
		this.apps = apps;
	}
	
	public FrameIcon getTv_icon() {
		return tv_icon;
	}

	public FrameIcon getWeb_icon() {
		return web_icon;
	}

	public FrameIcon getMarket_icon() {
		return market_icon;
	}

	public FrameIcon getMedia_icon() {
		return media_icon;
	}

	public FrameIcon getApps_icon() {
		return apps_icon;
	}

	public FrameIcon getSetup_icon() {
		return setup_icon;
	}

	public FrameIcon getGame_icon() {
		return game_icon;
	}

	public Launcher getmLauncher() {
		return mLauncher;
	}

	public PopWindow getPopwindow() {
		return popwindow;
	}

	public int getmBg_pic_res() {
		return mBg_pic_res;
	}

	public boolean isIspopshown() {
		return ispopshown;
	}

	public Handler getMessageHandler() {
		return MessageHandler;
	}
}
