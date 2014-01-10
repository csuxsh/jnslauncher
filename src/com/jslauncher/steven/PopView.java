package com.jslauncher.steven;

import java.util.StringTokenizer;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jslauncher.launcher.ApplicationInfo;
import com.jslauncher.launcher.DeleteZone;
import com.jslauncher.launcher.DragController;
import com.jslauncher.launcher.DragScroller;
import com.jslauncher.launcher.DragSource;
import com.jslauncher.launcher.DragView;
import com.jslauncher.launcher.DropTarget;
import com.jslauncher.launcher.ItemInfo;
import com.jslauncher.launcher.Launcher;
import com.jslauncher.launcher.LauncherApplication;
import com.jslauncher.launcher.LauncherSettings;
import com.jslauncher.launcher.R;
import com.jslauncher.launcher.ShortcutInfo;

/**
 * 自定义的View为了继承DragSource, DropTarget
 */

public class PopView extends LinearLayout implements  DragScroller,
DragSource, DropTarget {

	private int type;
	private AppsAdapter adapter;
	private Handler handler;
	
	public Launcher getLauncher() {
		return launcher;
	}

	public void setLauncher(Launcher launcher) {
		this.launcher = launcher;
	}

	private Launcher launcher;
	private DragController mDragController;

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public AppsAdapter getAdapter() {
		return adapter;
	}

	public void setAdapter(AppsAdapter adapter) {
		this.adapter = adapter;
	}

	public PopView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public PopView(Context context,AttributeSet paramAttributeSet)
	{
		super(context,paramAttributeSet);
	}


	public boolean acceptDrop(DragSource source, int x, int y, int xOffset, int yOffset,
			DragView dragView, Object dragInfo) {
		final ItemInfo item = (ItemInfo) dragInfo;
		final int itemType = item.itemType;
		return (itemType == LauncherSettings.Favorites.ITEM_TYPE_APPLICATION ||
				itemType == LauncherSettings.Favorites.ITEM_TYPE_SHORTCUT);

	}

	public Rect estimateDropLocation(DragSource source, int x, int y, int xOffset, int yOffset,
			DragView dragView, Object dragInfo, Rect recycle) {
		return null;
	}

	@SuppressWarnings("static-access")
	public void onDrop(DragSource source, int x, int y, int xOffset, int yOffset,
			DragView dragView, Object dragInfo) {
		ShortcutInfo item;
		ResolveInfo rf;
		AppList apps = null;
		String packagename;
		String activityname;
		PackageManager  pm = super.mContext.getPackageManager();
		if (dragInfo instanceof ApplicationInfo) {
			// Came from all apps -- make a copy
			item = ((ApplicationInfo)dragInfo).makeShortcut();
		} else {
			item = (ShortcutInfo)dragInfo;
		}
		rf = pm.resolveActivity(item.intent, pm.GET_INTENT_FILTERS);
		packagename = rf.activityInfo.packageName;
		activityname = rf.activityInfo.name;
		
		apps = ((LauncherApplication)(super.mContext.getApplicationContext())).mApplist[type];
		/*
		switch(type)
		{
		case AppList.TV:
			apps = ((LauncherApplication)(super.mContext.getApplicationContext())).mtvlist;
			break;
		case AppList.WEB:
			apps = ((LauncherApplication)(super.mContext.getApplicationContext())).mweblist;
			break;
		case AppList.MARKET:
			apps = ((LauncherApplication)(super.mContext.getApplicationContext())).mmarketlist;
			break;
		case AppList.MEDIA:
			apps = ((LauncherApplication)(super.mContext.getApplicationContext())).mmedialist;
			break;
		case AppList.GAME:
			apps = ((LauncherApplication)(super.mContext.getApplicationContext())).mgamelist;
			break;
		case AppList.SETUP:
			apps = ((LauncherApplication)(super.mContext.getApplicationContext())).msetuplist;
			break;
		default:
			Log.v("Popview.Ondrop", "type error");
			break;
		}
		*/
		for(int i = 0; i <apps.size();i++)
		{
			StringTokenizer token = new StringTokenizer(apps.get(i), "#");
			String spackagename = token.nextToken();
			String sactivityname = token.nextToken();
		
		//	if(rf.equals(apps.getResolveInfo(i, launcher)))		
			if(spackagename.equals(packagename) && sactivityname.equals(activityname))
			{	
				Toast.makeText(getContext(), R.string.app_exist_in_dir, Toast.LENGTH_SHORT).show();
				return;
			}	
		}

		apps.add(packagename+"#"+activityname);
		Log.v("drag log",""+adapter.toString());
		Message m = new Message();
		m.what = PopWindow.NOTIFY;
		handler.sendMessage(m);
		
		DeleteZone deletezone = (DeleteZone) launcher.findViewById(R.id.delete_zone);
		deletezone.onDrop(source, x, y, xOffset, yOffset, dragView, dragInfo);
		PopwindowIcon appbubble = (PopwindowIcon) launcher.findViewById(R.id.apps_bubble);
		appbubble.setBackgroundResource(R.drawable.buble_00);
	}

	public void onDragEnter(DragSource source, int x, int y, int xOffset, int yOffset,
			DragView dragView, Object dragInfo) {
	}

	public void onDragOver(DragSource source, int x, int y, int xOffset, int yOffset,
			DragView dragView, Object dragInfo) {
	}

	public void onDragExit(DragSource source, int x, int y, int xOffset, int yOffset,
			DragView dragView, Object dragInfo) {
	}

	@Override
	public void onDropCompleted(View target, boolean success) {
		if (success) {
			
		}
	}

	@Override
	public void setDragController(DragController dragger) {
		mDragController = dragger;
		// TODO Auto-generated method stub

	}

	@Override
	public void scrollLeft() {
		// TODO Auto-generated method stub

	}

	@Override
	public void scrollRight() {
		// TODO Auto-generated method stub

	}

	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return mDragController.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		return mDragController.onTouchEvent(ev);
	}

}
