package com.jslauncher.steven;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import com.jslauncher.launcher.ApplicationInfo;
import com.jslauncher.launcher.Launcher;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;


@SuppressWarnings("serial")
public class AppList extends ArrayList<String> implements Serializable {

	/**
	 * 
	 */
	public static final int TV = 1;
	public static final int WEB = 2;
	public static final int MARKET = 3;
	public static final int MEDIA = 4;
	public static final int GAME = 5;
	public static final int SETUP = 6;
	
	public int type;
	public List<ApplicationInfo> appinfo ;
	public String name;
	private Launcher mLauncher;
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AppList()
	{
		super();
		appinfo = new ArrayList<ApplicationInfo>();
	}
	
	public boolean add(String string)
	{	
		try
		{
		//	super.add(string);
			ApplicationInfo app = new ApplicationInfo(getResolveInfo(string), mLauncher.mIconCache);
			appinfo.add(app);
			super.add(string);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public String remove(int index)
	{
		appinfo.remove(index);
		return super.remove(index);
	}
	public ResolveInfo getResolveInfo(int position)
	{
		if(this.size() == 0)
			return null;
		return getResolveInfo(this.get(position));
	}
	public List<ApplicationInfo> getAppinfo() {
		return appinfo;
	}

	public void setAppinfo(List<ApplicationInfo> appinfo) {
		this.appinfo = appinfo;
	}

	public ResolveInfo getResolveInfo(String string)
	{
		Log.d("loadlist", "the string is"+string);
		ResolveInfo ri = null;
		StringTokenizer token = new StringTokenizer(string, "#");
		String packagename = token.nextToken();
		String activityname = token.nextToken();
		
		Intent resolveIntent = new Intent();
		resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		resolveIntent.setPackage(packagename);

		
		PackageManager  pm = mLauncher.getPackageManager();
		List<ResolveInfo> apps = (List<ResolveInfo>) pm.queryIntentActivities(resolveIntent, 0);
		Iterator<ResolveInfo> iterator = apps.iterator();
	
		while(iterator.hasNext())
		{
			ri = iterator.next();
			if(ri.activityInfo.name.equals(activityname))
				break;
		}
		
		return ri;
	}

	public Launcher getmLauncher() {
		return mLauncher;
	}

	public void setmLauncher(Launcher mLauncher) {
		this.mLauncher = mLauncher;
	}
}
