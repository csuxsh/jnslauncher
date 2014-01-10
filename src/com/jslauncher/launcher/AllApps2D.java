/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jslauncher.launcher;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.jslauncher.launcher.R;
import com.jslauncher.steven.DrawableUtil;
import com.jslauncher.steven.PopMenu;
import com.jslauncher.steven.PopwindowIcon;

public class AllApps2D
extends RelativeLayout
implements AllAppsView,
AdapterView.OnItemClickListener,
AdapterView.OnItemLongClickListener,
View.OnKeyListener,
DragSource {

	private static final String TAG = "Launcher.AllApps2D";
	private static final boolean DEBUG = false;
	//判断当前VIEW是否为删除应用VIEW的标志
	boolean showdelte_flag = false;
	private Launcher mLauncher;
	private DragController mDragController;

	private GridView mGrid;

	private ArrayList<ApplicationInfo> mAllAppsList = new ArrayList<ApplicationInfo>();

	// preserve compatibility with 3D all apps:
	//    0.0 -> hidden
	//    1.0 -> shown and opaque
	//    intermediate values -> partially shown & partially opaque
	private float mZoom;
	//modify by steven set private to default;
	AppsAdapter mAppsAdapter;

	// ------------------------------------------------------------

	public static class HomeButton extends ImageButton {
		public HomeButton(Context context, AttributeSet attrs) {
			super(context, attrs);
		}
		@Override
		public View focusSearch(int direction) {
			if (direction == FOCUS_UP) return super.focusSearch(direction);
			return null;
		}
	}

	public class AppsAdapter extends ArrayAdapter<ApplicationInfo> {
		private final LayoutInflater mInflater;

		public AppsAdapter(Context context, ArrayList<ApplicationInfo> apps) {
			super(context, 0, apps);
			mInflater = LayoutInflater.from(context);
		}

		@SuppressWarnings("deprecation")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ApplicationInfo info = getItem(position);

			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.application_boxed, parent, false);
			}

			//            if (!info.filtered) {
			//                info.icon = Utilities.createIconThumbnail(info.icon, getContext());
			//                info.filtered = true;
			//            }
			//modify by steven 添加了一层FrameLayout用以加载删除图标
			final FrameLayout handview = (FrameLayout) convertView;
			final TextView textView = (TextView) handview.findViewById(R.id.name);
			if (DEBUG) {
				Log.d(TAG, "icon bitmap = " + info.iconBitmap 
						+ " density = " + info.iconBitmap.getDensity());
			}
			info.iconBitmap.setDensity(Bitmap.DENSITY_NONE);
			//modified by steven add zoom for icon
			textView.setCompoundDrawablesWithIntrinsicBounds(null,  
					DrawableUtil.zoomDrawable(new BitmapDrawable(info.iconBitmap), 96, 96), null, null);
			textView.setText(info.title);
			//add by steven 如果符合显示或删除View的条件，显示删除图标
			if(showdelte_flag)
			{	
				String pname = info.componentName.getPackageName();
				//add by steven
				PackageInfo pInfo = null;
				try {
					pInfo = mLauncher.getPackageManager().getPackageInfo(pname, 0);
				} catch (NameNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
				// 如果是系统应用，则依然不会现实删除图标   
				if ((pInfo.applicationInfo.flags & android.content.pm.ApplicationInfo.FLAG_SYSTEM) == 0)
				{	
					handview.findViewById(R.id.appdelete).setVisibility(VISIBLE);
				}
				else
					handview.findViewById(R.id.appdelete).setVisibility(INVISIBLE);
			}	
			else
				handview.findViewById(R.id.appdelete).setVisibility(INVISIBLE);
			return convertView;
		}
	}


	public AllApps2D(Context context, AttributeSet attrs) {
		super(context, attrs);
		setVisibility(View.GONE);
		setSoundEffectsEnabled(false);
		setOnKeyListener(this);
		mAppsAdapter = new AppsAdapter(getContext(), mAllAppsList);
		mAppsAdapter.setNotifyOnChange(false);
		
	}

	@Override
	protected void onFinishInflate() {
	//	setBackgroundColor(Color.BLACK);

		try {
			mGrid = (GridView)findViewWithTag("all_apps_2d_grid");
			if (mGrid == null) throw new Resources.NotFoundException();
			mGrid.setOnItemClickListener(this);
			mGrid.setOnItemLongClickListener(this);
			//	mGrid.setBackgroundColor(Color.BLACK);
			//	mGrid.setCacheColorHint(Color.BLACK);

			// add by steven
			mGrid.setBackgroundColor(Color.TRANSPARENT);
		//	mGrid.setBackgroundResource(R.drawable.all_apps_bg);
			mGrid.setOnKeyListener(this);
			setOnKeyListener(this);
			ImageButton homeButton = (ImageButton) findViewWithTag("all_apps_2d_home");
			//add by setven
			TextView delete_app = (TextView) this.findViewById(R.id.all_app_delete);
			if (delete_app == null) throw new Resources.NotFoundException();
			delete_app.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if(showdelte_flag)	
						showdelte_flag = false;
					else
						showdelte_flag = true; 
					mAppsAdapter.notifyDataSetChanged();
				}
			});
			delete_app.setOnKeyListener(this);
			TextView sort_by_name = (TextView) this.findViewById(R.id.sort_by_name);
			if (sort_by_name == null) throw new Resources.NotFoundException();
			sort_by_name.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					/*
					SharedPreferences sharedAppSort = mLauncher.getSharedPreferences("appsort", Activity.MODE_PRIVATE);
					String sort = sharedAppSort.getString("sort", "byname");
					if(!sort.equalsIgnoreCase("byname"))
					{
						SharedPreferences.Editor editor = sharedAppSort.edit();
						editor.putString("sort", "byname");
						editor.commit();
					}
					Intent broadcast = new Intent("com.jslauncher.launcher.AllApps2D.SORT");
					mLauncher.sendBroadcast(broadcast);
					mAppsAdapter.notifyDataSetChanged();
					 */
					PopMenu popmenu = PopMenu.getPopMenu(mLauncher, arg0);
					if(!popmenu.isShow)
						popmenu.show();
					else
						popmenu.dimss();
				}
			});
			/*
			Button sort_by_date = (Button) this.findViewById(R.id.sort_by_date);
			if (sort_by_date == null) throw new Resources.NotFoundException();
			sort_by_date.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					SharedPreferences sharedAppSort = mLauncher.getSharedPreferences("appsort", Activity.MODE_PRIVATE);
					String sort = sharedAppSort.getString("sort", "byname");
					if(!sort.equalsIgnoreCase("bydate"))
					{
						SharedPreferences.Editor editor = sharedAppSort.edit();
						editor.putString("sort", "bydate");
						editor.commit();
					}
					Intent broadcast = new Intent("com.jslauncher.launcher.AllApps2D.SORT");
					mLauncher.sendBroadcast(broadcast);
					mAppsAdapter.notifyDataSetChanged();

				}
			});

			Button sort_by_counter = (Button) this.findViewById(R.id.sort_by_counter);
			if (sort_by_counter == null) throw new Resources.NotFoundException();
			sort_by_counter.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					SharedPreferences sharedAppCounter = mLauncher.getSharedPreferences("appsort", Activity.MODE_PRIVATE);
					String sort = sharedAppCounter.getString("sort", "byname");
					if(!sort.equalsIgnoreCase("bycounter"))
					{
						SharedPreferences.Editor editor = sharedAppCounter.edit();
						editor.putString("sort", "bycounter");
						editor.commit();
					}
					Intent broadcast = new Intent("com.jslauncher.launcher.AllApps2D.SORT");
					mLauncher.sendBroadcast(broadcast);
					mAppsAdapter.notifyDataSetChanged();

				}
			});
			 */
			if (homeButton == null) throw new Resources.NotFoundException();
			homeButton.setOnClickListener(
					new View.OnClickListener() {
						public void onClick(View v) {
							mLauncher.closeAllApps(true);
						}
					});
		} catch (Resources.NotFoundException e) {
			Log.e(TAG, "Can't find necessary layout elements for AllApps2D");
		}


	}

	public AllApps2D(Context context, AttributeSet attrs, int defStyle) {
		this(context, attrs);
		setOnKeyListener(this);
	}

	public void setLauncher(Launcher launcher) {
		mLauncher = launcher;
	}
	// mofify by steven for test 若当前VIEW为删除VIEW则按BACK先回正常VIEW
	public boolean onKey(View v, int keyCode, KeyEvent event) {

		if (!isVisible()) return false;

		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if(event.getAction() == KeyEvent.ACTION_DOWN)
			{	
				if(showdelte_flag)
				{	
					showdelte_flag = false;
					mAppsAdapter.notifyDataSetChanged();
					return true;
				}	
				else  
				{	
					mLauncher.closeAllApps(true);
					return true;
				}	
			}
			break;
		default:
			return false;
		}
		return false;
	}
	@SuppressWarnings("rawtypes")
	public void onItemClick(AdapterView parent, View v, int position, long id) {
		ApplicationInfo app = (ApplicationInfo) parent.getItemAtPosition(position);
		String pname = app.componentName.getPackageName();
		//add by steven
		PackageInfo pInfo = null;
		try {
			pInfo = mLauncher.getPackageManager().getPackageInfo(pname, 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		// 是系统软件还是用户软件     
		if(showdelte_flag && ((pInfo.applicationInfo.flags & android.content.pm.ApplicationInfo.FLAG_SYSTEM) == 0))
		{	 

			Uri packageURI = Uri.parse("package:"+pname); 
			//创建Intent
			Intent intent = new Intent(Intent.ACTION_DELETE, packageURI); 
			//执行卸载程序 
			mLauncher.startActivity(intent);

		}
		else
		{	//add by steven to save usrfrq
			SharedPreferences sharedAppCounter = mLauncher.getSharedPreferences("appcounter", Activity.MODE_PRIVATE);
			int usecounter = sharedAppCounter.getInt(pname, 0);
			usecounter++;
			SharedPreferences.Editor editor = sharedAppCounter.edit();
			editor.putInt(pname, usecounter);
			editor.commit();
			mLauncher.startActivitySafely(app.intent, app);
		}	
	}

	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		if (!view.isInTouchMode()) {
			return false;
		}

		ApplicationInfo app = (ApplicationInfo) parent.getItemAtPosition(position);
		app = new ApplicationInfo(app);

		mDragController.startDrag(view, this, app, DragController.DRAG_ACTION_COPY);
        PopwindowIcon appbubble = (PopwindowIcon) mLauncher.findViewById(R.id.apps_bubble);
		appbubble.setBackgroundColor(Color.TRANSPARENT);
		mLauncher.closeAllApps(true);

		return true;
	}

	protected void onFocusChanged(boolean gainFocus, int direction, android.graphics.Rect prev) {
		if (gainFocus) {
			mGrid.requestFocus();
		}
	}

	public void setDragController(DragController dragger) {
		mDragController = dragger;
	}

	public void onDropCompleted(View target, boolean success) {
	}

	/**
	 * Zoom to the specifed level.
	 *
	 * @param zoom [0..1] 0 is hidden, 1 is open
	 */
	public void zoom(float zoom, boolean animate) {
		//        Log.d(TAG, "zooming " + ((zoom == 1.0) ? "open" : "closed"));
		cancelLongPress();

		mZoom = zoom;

		if (isVisible()) {
			getParent().bringChildToFront(this);
			setVisibility(View.VISIBLE);
			mGrid.setAdapter(mAppsAdapter);
			if (animate) {
				startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.all_apps_2d_fade_in));
			} else {
				onAnimationEnd();
			}
		} else {
			if (animate) {
				startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.all_apps_2d_fade_out));
			} else {
				onAnimationEnd();
			}
		}
	}

	protected void onAnimationEnd() {
		if (!isVisible()) {
			setVisibility(View.GONE);
			mGrid.setAdapter(null);
			mZoom = 0.0f;
		} else {
			mZoom = 1.0f;
		}

		mLauncher.zoomed(mZoom);
	}

	public boolean isVisible() {
		return mZoom > 0.001f;
	}

	@Override
	public boolean isOpaque() {
		return mZoom > 0.999f;
	}

	//modified by steven add a input value compareaor  
	public void setApps(ArrayList<ApplicationInfo> list, Comparator<ApplicationInfo>  compareator) {
		mAllAppsList.clear();
		addApps(list, compareator);
	}

	//modified by steven add a input value compareaor    
	public void addApps(ArrayList<ApplicationInfo> list, Comparator<ApplicationInfo> compareator) {
		//        Log.d(TAG, "addApps: " + list.size() + " apps: " + list.toString());

		final int N = list.size();

		//	 Collections.sort(list,compareator);
		/*不是按名称排序时先将列表按字母降序排序
		if(!compareator.equals(LauncherModel.APP_NAME_COMPARATOR))
		{
			Collections.reverse(list);
		}
		 */

		for (int i=0; i<N; i++) {
			final ApplicationInfo item = list.get(i);
			int index = Collections.binarySearch(mAllAppsList, item,
					compareator);
			if (index < 0) {
				index = -(index+1);
			}
			mAllAppsList.add(index, item);
		}
		for(int i = 0; i< N; i++)
		{
			Log.d("allappsconter", "name="+mAllAppsList.get(i).componentName.getPackageName()+";"+mAllAppsList.get(i).useCounter);
		}
		mAppsAdapter.notifyDataSetChanged();
	}

	public void removeApps(ArrayList<ApplicationInfo> list) {
		final int N = list.size();
		for (int i=0; i<N; i++) {
			final ApplicationInfo item = list.get(i);
			int index = findAppByComponent(mAllAppsList, item);
			if (index >= 0) {
				mAllAppsList.remove(index);
			} else {
				Log.w(TAG, "couldn't find a match for item \"" + item + "\"");
				// Try to recover.  This should keep us from crashing for now.
			}
		}
		mAppsAdapter.notifyDataSetChanged();
	}

	public void updateApps(ArrayList<ApplicationInfo> list, Comparator<ApplicationInfo>  compareator) {
		// Just remove and add, because they may need to be re-sorted.
		removeApps(list);
		addApps(list, compareator);
	}

	private static int findAppByComponent(ArrayList<ApplicationInfo> list, ApplicationInfo item) {
		ComponentName component = item.intent.getComponent();
		final int N = list.size();
		for (int i=0; i<N; i++) {
			ApplicationInfo x = list.get(i);
			if (x.intent.getComponent().equals(component)) {
				return i;
			}
		}
		return -1;
	}

	public void dumpState() {
		ApplicationInfo.dumpApplicationInfoList(TAG, "mAllAppsList", mAllAppsList);
	}

	public void surrender() {
	}

}


