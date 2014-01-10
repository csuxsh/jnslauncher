package com.jslauncher.steven;

import java.util.StringTokenizer;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
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
 * lancher js 桌面的系统图标及文字 handle，主要设置了DRAG属性
 */

public class PopwindowIcon extends TextView implements DragScroller,
		DragSource, DropTarget {

	public AppList apps;
	private Launcher mlauncher;
	private static final int ORIENTATION_HORIZONTAL = 1;
    @SuppressWarnings("unused")
	private int mOrientation = ORIENTATION_HORIZONTAL;
    // private TransitionDrawable mTransition;
    private final Paint mTrashPaint = new Paint();
    
	public void setHandle(FrameLayout handle) {
	}

	public Launcher getLauncher() {
		return mlauncher;
	}

	public void setLauncher(Launcher mlauncher) {
		this.mlauncher = mlauncher;
	}
/*
	public AppList getApps() {
		return apps;
	}

	public void setApps(AppList apps) {
		this.apps = apps;
	}
*/
	public PopwindowIcon(Context context, AttributeSet attrs) {
		super(context, attrs);
		/// this(context, attrs, 0);
		// TODO Auto-generated constructor stub
		final int srcColor = context.getResources().getColor(R.color.moveto_color_filter);
	    mTrashPaint.setColorFilter(new PorterDuffColorFilter(srcColor, PorterDuff.Mode.SRC_ATOP));
		//mTransition = (TransitionDrawable) (new ImageView(context).getDrawable());
	}
	public PopwindowIcon(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HandleView, defStyle, 0);
        mOrientation = a.getInt(R.styleable.HandleView_direction, ORIENTATION_HORIZONTAL);
        a.recycle();
        final int srcColor = context.getResources().getColor(R.color.moveto_color_filter);
        mTrashPaint.setColorFilter(new PorterDuffColorFilter(srcColor, PorterDuff.Mode.SRC_ATOP));
        setContentDescription(context.getString(R.string.all_apps_button_label));
       // mTransition = (TransitionDrawable) (new ImageView(context).getDrawable());
    }

	@SuppressWarnings("static-access")
	@Override
	public void onDrop(DragSource source, int x, int y, int xOffset,
			int yOffset, DragView dragView, Object dragInfo) {
		// TODO Auto-generated method stub
		Log.d("popwindowdrag", "ondrop");
		apps = ((LauncherApplication)mlauncher.getApplicationContext()).mApplist[apps.type];
		
		/*
		switch(apps.type)
		{

		case AppList.TV:
			apps = ((LauncherApplication)mlauncher.getApplicationContext()).mtvlist;
			break;
		case AppList.GAME:
			apps = ((LauncherApplication)mlauncher.getApplicationContext()).mgamelist;
			break;
		case AppList.MEDIA:
			apps = ((LauncherApplication)mlauncher.getApplicationContext()).mmedialist ;
			break;
		case AppList.WEB:
			apps = ((LauncherApplication)mlauncher.getApplicationContext()).mweblist ;
			break;
		case AppList.SETUP:
			apps = ((LauncherApplication)mlauncher.getApplicationContext()).msetuplist ;
			break;
		case AppList.MARKET:
			apps = ((LauncherApplication)mlauncher.getApplicationContext()).mmarketlist ;
			break;
		default:
			break;
		
		}
		*/
		ShortcutInfo item;
		ResolveInfo rf;
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
		
		for(int i = 0; i <apps.size();i++)
		{
			
			StringTokenizer token = new StringTokenizer(apps.get(i), "#");
			String spackagename = token.nextToken();
			String sactivityname = token.nextToken();
			
			//if(rf.equals(apps.getResolveInfo(i, mlauncher)))
			if(spackagename.equals(packagename) && sactivityname.equals(activityname))
			{	
				Toast.makeText(getContext(), R.string.app_exist_in_dir, Toast.LENGTH_SHORT).show();
				return;
			}	
		}
		
		apps.add(packagename+"#"+activityname);
		((LauncherApplication)mlauncher.getApplicationContext()).mApplist[apps.type] = apps;
		/*
		switch(apps.type)
		{

		case AppList.TV:
			((LauncherApplication)mlauncher.getApplicationContext()).mtvlist = apps;
			break;
		case AppList.GAME:
			((LauncherApplication)mlauncher.getApplicationContext()).mgamelist = apps;
			break;
		case AppList.MEDIA:
			((LauncherApplication)mlauncher.getApplicationContext()).mmedialist = apps;;
			break;
		case AppList.WEB:
			((LauncherApplication)mlauncher.getApplicationContext()).mweblist = apps;;
			break;
		case AppList.SETUP:
			((LauncherApplication)mlauncher.getApplicationContext()).msetuplist = apps; ;
			break;
		case AppList.MARKET:
			((LauncherApplication)mlauncher.getApplicationContext()).mmarketlist = apps;;
			break;
		default:
			break;
		
		}
		*/
		Intent intent = new Intent();
		intent.setAction(MyFrame.ACTION_SAVE_LIST);
		Uri uri = Uri.parse("package://" + "" + "");
		intent.setData(uri);
		mlauncher.sendBroadcast(intent);
	//	if(saveList(apps))
	//	{	
			DeleteZone deletezone = (DeleteZone) mlauncher.findViewById(R.id.delete_zone);
			deletezone.onDrop(source, x, y, xOffset, yOffset, dragView, dragInfo);
	//	}
	}

	@Override
	public void onDragEnter(DragSource source, int x, int y, int xOffset,
			int yOffset, DragView dragView, Object dragInfo) {
		// TODO Auto-generated method stub
		Log.d("popwindicondrag", "dragenter");
		//handle.setBackgroundColor(Color.GREEN);
//		  mTransition.reverseTransition(250);
	      dragView.setPaint(mTrashPaint);
	}

	@Override
	public void onDragOver(DragSource source, int x, int y, int xOffset,
			int yOffset, DragView dragView, Object dragInfo) {
		// TODO Auto-generated method stub
		Log.d("popwindicondrag", "dragover");
	
	}

	@Override
	public void onDragExit(DragSource source, int x, int y, int xOffset,
			int yOffset, DragView dragView, Object dragInfo) {
		// TODO Auto-generated method stub
		Log.d("popwindicondrag", "exit");
	//	handle.setBackgroundColor(Color.TRANSPARENT);
	//	  mTransition.reverseTransition(250);
	      dragView.setPaint(null);
	}

	@Override
	public boolean acceptDrop(DragSource source, int x, int y, int xOffset,
			int yOffset, DragView dragView, Object dragInfo) {
		// TODO Auto-generated method stub
		final ItemInfo item = (ItemInfo) dragInfo;
		final int itemType = item.itemType;
		return (itemType == LauncherSettings.Favorites.ITEM_TYPE_APPLICATION ||
				itemType == LauncherSettings.Favorites.ITEM_TYPE_SHORTCUT);
	}

	@Override
	public Rect estimateDropLocation(DragSource source, int x, int y,
			int xOffset, int yOffset, DragView dragView, Object dragInfo,
			Rect recycle) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDragController(DragController dragger) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDropCompleted(View target, boolean success) {
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
}
