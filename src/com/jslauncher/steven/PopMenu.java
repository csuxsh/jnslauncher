package com.jslauncher.steven;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jslauncher.launcher.AllApps2D;
import com.jslauncher.launcher.Launcher;
import com.jslauncher.launcher.R;

/**
 * lancher js AllApps界面中的排序下拉菜单控制类
 */

public class PopMenu {
	
	private  LinearLayout popmenu;
	private  Launcher mLauncher;
	private  PopupWindow mPopupwindow;
	
	private  OnClickListener ocl;
	public  boolean isShow = false;
	private View mAnchor;
	TextView sort_by_name;
	TextView sort_by_counter;
	TextView sort_by_date;
	static   PopMenu  mpopMenu;
	
	private PopMenu(Launcher launcher, View anchor)
	{
		if(mpopMenu == null)
		mLauncher = launcher;
		mAnchor = anchor; 
		setupView();
		
	}
	private void setupView()
	{
		popmenu = (LinearLayout) mLauncher.getLayoutInflater().inflate(R.layout.popmenu, null, false);
		sort_by_name = (TextView) popmenu.findViewById(R.id.sort_by_name);
		sort_by_counter= (TextView) popmenu.findViewById(R.id.sort_by_counter);
		sort_by_date = (TextView) popmenu.findViewById(R.id.sort_by_date);
		
		ocl = new OnClickListener()
		{
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SharedPreferences sharedAppSort = mLauncher.getSharedPreferences("appsort", Activity.MODE_PRIVATE);
				String sort = sharedAppSort.getString("sort", "byname");
				SharedPreferences.Editor editor = sharedAppSort.edit();
				Intent broadcast = new Intent("com.jslauncher.launcher.AllApps2D.SORT");
				PopMenu.this.dimss();
				
				switch(v.getId())
				{
					case R.id.sort_by_name:
						if(!sort.equalsIgnoreCase("byname"))
						{
							editor.putString("sort", "byname");
							editor.commit();
						}
						mLauncher.sendBroadcast(broadcast);
						break;
					case R.id.sort_by_counter:
						if(!sort.equalsIgnoreCase("bycounter"))
						{
							
							editor.putString("sort", "bycounter");
							editor.commit();
						}
						mLauncher.sendBroadcast(broadcast);
						break;
					case R.id.sort_by_date:
						if(!sort.equalsIgnoreCase("bydate"))
						{
							editor.putString("sort", "bydate");
							editor.commit();
						}
						mLauncher.sendBroadcast(broadcast);
						break;
					default:
							break;
				}
			}
			
		};

		OnKeyListener okl =  new OnKeyListener()
		{

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				switch(arg1)
				{
				case KeyEvent.KEYCODE_BACK:
					dimss(); 
					break;
				case KeyEvent.KEYCODE_MENU:
					break;
				default:
					break;
				}
				return false;
			}

		};

		sort_by_name.setFocusableInTouchMode(true);
		popmenu.setOnKeyListener(okl);
		sort_by_name.setOnKeyListener(okl);
		sort_by_counter.setOnKeyListener(okl);
		sort_by_date.setOnKeyListener(okl);
		sort_by_name.setOnClickListener(ocl);
		sort_by_counter.setOnClickListener(ocl);
		sort_by_date.setOnClickListener(ocl);

		mPopupwindow = new PopupWindow(popmenu, 320 ,402);
		mPopupwindow.setFocusable(true);
		mPopupwindow.setOutsideTouchable(true);
		mPopupwindow.getContentView().setOnTouchListener(new OnTouchListener()
		{

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
		//		mPopupwindow.setFocusable(false);  
				if(!isInView(v,event))
					dimss();
				return false;
			}
			
			private boolean isInView(View v, MotionEvent event)
			{
				TextView sort = (TextView) ((AllApps2D)(mLauncher.mAllAppsGrid)).findViewById(R.id.sort_by_name);
				Rect rect = new Rect();
				sort.getGlobalVisibleRect(rect);
				
				if(event.getRawX() > rect.left && event.getRawX() < rect.right
						&& (event.getRawY()-rect.top) > sort.getTop() && event.getRawY() < rect.bottom)
						return true;
				else
					return false;
			}
			
		});
		
	}
	public static PopMenu getPopMenu(Launcher launcher, View anchor)
	{
		if(mpopMenu == null)
		{	
			mpopMenu = new PopMenu(launcher,anchor);
			return mpopMenu;
		}
		else
			return mpopMenu;
	}

	public  boolean dimss()
	{
		if(mPopupwindow.isShowing())
			mPopupwindow.dismiss();
		isShow = false;
		return false;
	}
	 public boolean show()
	{
		if(!mPopupwindow.isShowing())
		{		
			
			mPopupwindow.showAsDropDown(mAnchor);
			isShow = true;
			if(sort_by_name.isInTouchMode())
				sort_by_name.requestFocusFromTouch();
			else
				sort_by_name.requestFocus();
			
			//设置父窗口也可获得焦点	
			WindowManager wm =  (WindowManager) mLauncher.getSystemService(Context.WINDOW_SERVICE);
			WindowManager.LayoutParams p = (WindowManager.LayoutParams)popmenu.getLayoutParams();
			p.flags |= WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
			wm.updateViewLayout(popmenu, p);
			
			return true;
		}	
		else
		{
			dimss();
		}
		return false;
	}
}
