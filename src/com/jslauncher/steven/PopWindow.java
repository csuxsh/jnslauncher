package com.jslauncher.steven;


import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import com.jslauncher.launcher.ApplicationInfo;
import com.jslauncher.launcher.DragController;
import com.jslauncher.launcher.Launcher;
import com.jslauncher.launcher.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;


/**
 * 用于控制弹出框的类
 */

public class PopWindow {

	private Activity activity;
	
	public MyPopupWindow getPopupwindow() {
		return popupwindow;
	}

	public void setPopupwindow(MyPopupWindow popupwindow) {
		this.popupwindow = popupwindow;
	}
	private MyPopupWindow popupwindow;
	private View anchor;
    PopView popwindow;
	private int mScreewidth = 0;
	@SuppressWarnings("unused")
	private int mScreeheight = 0;
	Thread hanlemessage;
	AppsAdapter madapter;
	public Handler myMessageHandler;
	public AppList mapps;
	GridView grid;
	//TextView wname;
	//Button close;
	//Button sort;
	//Button delete;
	public final static int 	NOTIFY = 0x100;
	public final static int    POP_REQUEST_PICK_SHORTCUT = 255;
	private MyFrame frame;


	public PopWindow(MyFrame frame, Activity activity, View anchor)
	{
		this.activity = activity;
		this.anchor = anchor;
		this.frame = frame;
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
	//	popwindow = (PopView) this.activity.getLayoutInflater().inflate(R.layout.popwindow, null, false);
		mScreewidth = dm.widthPixels;
		mScreeheight = dm.heightPixels;
	}

	@SuppressLint("HandlerLeak")
	protected void initPopoWindow(AppList apps)
	{
		mapps = apps;
		madapter = new AppsAdapter(activity, mapps);	
		popwindow.setType(apps.type);
		popwindow.setDragController(frame.mLauncher.getDragController());
		popwindow.setLauncher(frame.mLauncher);
		Log.d("before new popwindow", System.currentTimeMillis()+"");
		popupwindow = new MyPopupWindow(frame, popwindow,mScreewidth - 40 ,600 * (mScreewidth - 40)/1208 - 80, false);
		popupwindow.setApptype(apps.type);
		popupwindow.setAnimationStyle(android.R.style.Animation_InputMethod);
		popupwindow.update();  
		popupwindow.setFocusable(true);
		popupwindow.setOutsideTouchable(true); 

		AnimationDrawable animationDrawable_shop = (AnimationDrawable)popwindow.getBackground();  

		// 动画是否正在运行  
		if(animationDrawable_shop.isRunning()){  
			//停止动画播放  
			animationDrawable_shop.stop();  
		}  
		else{  
			//开始或者继续动画播放  
			animationDrawable_shop.start();  
		}  
		grid = (GridView) popwindow.findViewById(R.id.pop_list);//new GridView(activity);//(GridView) activity.findViewById(R.id.pop_list);
		
		grid.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				Log.v("onclick", ""+position);
				if (position == parent.getCount() -1 )
				{
					//	Log.v("Popview add ","click add");
					Intent pickIntent = new Intent(Intent.ACTION_CREATE_SHORTCUT);
					pickIntent.putExtra(Intent.EXTRA_INTENT, new Intent(Intent.ACTION_CREATE_SHORTCUT));
					//	pickIntent.putExtra(Intent.EXTRA_TITLE, activity.getText(R.string.title_select_shortcut));
					((Launcher)activity).setMrilisttype(((AppsAdapter)parent.getAdapter()).getApps().type);
					//	activity.startActivityForResult(pickIntent, POP_REQUEST_PICK_SHORTCUT);
					Intent in  = new Intent("com.jslauncher.steven.AddAppActivity.POPADD",null);
					in.putExtra("listtype", mapps.type);
					activity.startActivity(in);
					return;
				}
			
				ApplicationInfo app = ((AppsAdapter)parent.getAdapter()).getApps().getAppinfo().get(position);
				if (app!=null) {
					Intent intent = app.getIntent();
					//for save usefrq
					SharedPreferences sharedAppCounter = activity.getSharedPreferences("appcounter", Activity.MODE_PRIVATE);
					int usecounter = sharedAppCounter.getInt(app.getComponentName().getPackageName(), 0);
					usecounter++;
					SharedPreferences.Editor editor = sharedAppCounter.edit();
					editor.putInt(app.getComponentName().getPackageName(), usecounter);
					editor.commit();
					PopWindow.this.activity.startActivity(intent);

				}
			}
		});
		grid.setOnItemLongClickListener(new OnItemLongClickListener()
		{

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				if (position == parent.getCount() -1 )
					return false;
				StringTokenizer token = new StringTokenizer((String) parent.getItemAtPosition(position), "#");
				String packagename = token.nextToken();
				String activityname = token.nextToken();
				//String packagename = (String) parent.getItemAtPosition(position);
				if (packagename != null && !packagename.equals("")) {
					Intent resolveIntent = new Intent();
					try
					{
						PackageInfo pi = activity.getPackageManager().getPackageInfo(packagename, 0);
						resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
						resolveIntent.setPackage(pi.packageName);
						PackageManager  pm = activity.getPackageManager();
						List<ResolveInfo> apps = (List<ResolveInfo>) pm.queryIntentActivities(resolveIntent, 0);
						Iterator<ResolveInfo> iterator = apps.iterator();
						ResolveInfo ri = null;

						while(iterator.hasNext())
						{
							ri = iterator.next();
							if(ri.activityInfo.name.equals(activityname))
								break;
						}
						//ResolveInfo ri = apps.iterator().next();
						/*
						((Launcher)activity).setMri(ri);
						((Launcher)activity).setMriposition(position);
						((Launcher)activity).setMrilisttype(((AppsAdapter)parent.getAdapter()).getApps().type);
						activity.showDialog(Launcher.DIALOG_DELETE_APP);
						 */

						ApplicationInfo app = new ApplicationInfo(ri, frame.mLauncher.mIconCache);
						app = new ApplicationInfo(app);
						frame.mLauncher.getDragController().startDrag(arg1, popwindow, app, DragController.DRAG_ACTION_COPY);
						frame.apps_icon.icon_bubble.setBackgroundColor(Color.TRANSPARENT);
						mapps.remove(position);
						Message m = new Message();
						m.what = PopWindow.NOTIFY;
						myMessageHandler.sendMessage(m);
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}

				return false;
			}

		});
		grid.setOnKeyListener(new OnKeyListener()
		{
			private boolean key_pressed = false;

			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				// TODO Auto-generated method stub
				if(arg2.getAction() == KeyEvent.ACTION_DOWN && key_pressed == false)
				{
					key_pressed = true;

					switch(arg1)
					{
					case KeyEvent.KEYCODE_BACK:
					case KeyEvent.KEYCODE_ESCAPE:
						if(popwindow != null)
						popupwindow.dismiss(); 
						frame.setPre_listype(mapps.type);
						frame.mLauncher.setMrilisttype(0);
						frame.mLauncher.getDragController().removeDropTarget(popwindow);
						frame.mLauncher.mWorkspace.setVisibility(View.VISIBLE);
						frame.mLauncher.findViewById(R.id.drag_layer).setBackgroundDrawable(null);
						popupwindow = null;
						frame.ispopshown = false;
						return true;
					case KeyEvent.KEYCODE_MENU:
						break;
					case KeyEvent.KEYCODE_PAGE_DOWN:
						dealPageUpDown(KeyEvent.KEYCODE_PAGE_DOWN);
						return true;
					case KeyEvent.KEYCODE_PAGE_UP:
						dealPageUpDown(KeyEvent.KEYCODE_PAGE_UP);
						return true;
					default:
						break;
					}
				}
				if(arg2.getAction() == KeyEvent.ACTION_UP)
					key_pressed = false;
				
				return false;
			}
			private void dealPageUpDown(int keycode)
			{
				int listtype  = frame.mLauncher.getMrilisttype();

				if(keycode == KeyEvent.KEYCODE_PAGE_DOWN)
				{
					if(AppList.SETUP == listtype)
						listtype = AppList.TV;
					else
						listtype++; 
				}
				else
				{
					if(AppList.TV == listtype)
						listtype = AppList.SETUP;
					else
						listtype--; 
				}

				switch(listtype)
				{
				case AppList.TV:
					frame.tv_icon.icon_handle.performClick();
					break;
				case AppList.WEB:
					frame.web_icon.icon_handle.performClick();
					break;
				case AppList.MARKET:
					frame.market_icon.icon_handle.performClick();
					break;
				case AppList.MEDIA:
					frame.media_icon.icon_handle.performClick();
					break;
				case AppList.SETUP:
					frame.setup_icon.icon_handle.performClick();
					break;
				case AppList.GAME:
					frame.game_icon.icon_handle.performClick();
					break;
				default:
					break;
				}
			}

		});
		///		delete.setOnClickListener(ocl);
		//		sort.setOnClickListener(ocl);
		//		close.setOnClickListener(ocl);
		Log.v("make adapter",madapter.toString());
		grid.setAdapter(madapter);
		popwindow.setAdapter((AppsAdapter)grid.getAdapter());
		//popwindow.setBackgroundResource(R.drawable.biankuan);
		//	popupwindow.showAsDropDown(anchor, -200, 25);
		
		
		
		
		Log.d("berforShowpopwindow", System.currentTimeMillis()+"");
		popupwindow.showAtLocation(anchor.getRootView(), Gravity.TOP, 0, 0); //0 , 50

		
		Log.d("beforsendomesg", System.currentTimeMillis()+"");
		
		//设置父窗口也可获得焦点
		WindowManager wm =  (WindowManager) PopWindow.this.activity.getSystemService(Context.WINDOW_SERVICE);
		WindowManager.LayoutParams p = (WindowManager.LayoutParams)popwindow.getLayoutParams();
		p.flags |= WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
		wm.updateViewLayout(popwindow, p);

		//设置消息处理循环来处理list的刷新
		myMessageHandler = new Handler() {
			// @Override
			@SuppressLint("HandlerLeak")
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case PopWindow.NOTIFY:
					madapter.notifyDataSetChanged();
					Intent intent = new Intent();
					intent.setAction(MyFrame.ACTION_SAVE_LIST);
					Uri uri = Uri.parse("package://" + "" + "");
					intent.setData(uri);
					frame.mLauncher.sendBroadcast(intent);
				}
				super.handleMessage(msg);
			}
		};
		popwindow.setHandler(myMessageHandler);
		frame.MessageHandler = myMessageHandler;
		Log.d("aftesendmes", System.currentTimeMillis()+"");
		frame.mLauncher.getDragController().addDropTarget(popwindow);
	}

	public PopView getPopwindow() {
		return popwindow;
	}

	public void setPopwindow(PopView popwindow) {
		this.popwindow = popwindow;
	}

	public MyPopupWindow getPopupWindow(AppList apps) {   
		/*
		if(null != popupwindow) {  	
			popupwindow.dismiss(); 
			popupwindow = null;
			return popupwindow;   
		}else {   
			initPopoWindow(apps);   
			return popupwindow;
		}   
		 */		
		if(null != popupwindow) {  	


			frame.setPre_listype(mapps.type);
			if(apps.type == mapps.type)
			{	
				popupwindow.dismiss();
				popupwindow = null;
		//		frame.mLauncher.getDragController().removeDropTarget(popwindow);
				frame.mLauncher.mWorkspace.setVisibility(View.VISIBLE);
				frame.mLauncher.findViewById(R.id.drag_layer).setBackgroundDrawable(null);
				return null;
			}
			else
			{	
				popupwindow.dismiss();
				mapps = apps;
				madapter.setApps(apps);
				grid.setAdapter(madapter);
				popwindow.setType(apps.type);
				Message m = new Message();
				m.what = PopWindow.NOTIFY;
				myMessageHandler.sendMessage(m);
			//	return popupwindow;
			}
		}   
		frame.mLauncher.mWorkspace.setVisibility(View.GONE);
		Log.d("startinitPopWindow", System.currentTimeMillis()+"");
		initPopoWindow(apps);   
		return popupwindow;

	}
	boolean saveList(AppList apps)
	{		
		
		//保存当前更改的list
		String str = "";

		for(int i = 0; i < apps.size(); i++)
		{
			str = str + apps.get(i);
			str = str+"^";
		}
		try
		{   
			
			if(frame.apph.Insert(apps.name, str))
				return true;
			else
				return false;
			/*
			SharedPreferences sharedPreferences = this.activity.getSharedPreferences("applist", Activity.MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putString(apps.name, str);
			Log.v("sabelist", "listname="+apps.name);
			Log.v("sabelist", "listlength="+apps.size());
			editor.commit();
			return true;
			*/
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}	
	/*		
		SharedPreferences sharedPreferences = this.activity.getSharedPreferences("base64", Activity.MODE_PRIVATE);
		ByteArrayOutputStream baos;
		ObjectOutputStream oos;	  
		Log.v("sabelist", "listname="+apps.name);
		Log.v("sabelist", "listlength="+apps.size());
		try 
		{
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(apps);
			Editor editor = sharedPreferences.edit();
			String tvBase64 = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
			editor.putString(apps.name, tvBase64);
			editor.commit();
			oos.close();
		}
		catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			Toast.makeText(activity, "保存失败", Toast.LENGTH_SHORT).show();
			return false; 
		}
		return true;

	}
	 */	
}
