package com.jslauncher.steven;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.jslauncher.launcher.LauncherApplication;
import com.jslauncher.launcher.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 用于显示添加界面的adapter
 */

public class PopAddAdapter extends BaseAdapter {
	
	LayoutInflater inflater;
	public List<Map<String, Object>> apps;
	public static Map<Integer, Boolean> isSelected; 
	 PackageManager pm;
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return apps.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return apps.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			
			convertView = inflater.inflate(R.layout.popaddlist, parent, false);
		}
		
		LinearLayout  hold = (LinearLayout) convertView;
		ImageView iv = (ImageView) hold.findViewById(R.id.img);
		TextView tv = (TextView)hold.findViewById(R.id.title);
		CheckBox cb= (CheckBox) hold.findViewById(R.id.cb);
		ResolveInfo  ri = (ResolveInfo)(apps.get(position).get("resolveInfo"));
		iv.setBackgroundDrawable(ri.activityInfo.loadIcon(pm));
		tv.setText(ri.activityInfo.loadLabel(pm));
		cb.setChecked(isSelected.get(position));
		return convertView;
	}
	
	public void init(Activity activity) {    
        apps = new ArrayList<Map<String, Object>>();
        inflater  = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);;
        LauncherApplication application = (LauncherApplication)activity.getApplication();
        pm = activity.getPackageManager();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> rlist = pm.queryIntentActivities(mainIntent, 0);
        isSelected = new HashMap<Integer, Boolean>();
        Iterator<ResolveInfo> iterator = rlist.iterator();
       
        // 判断应用是否已存在于二级目录中
        while(iterator.hasNext())
        {
        	ResolveInfo ri = (ResolveInfo) iterator.next();
        	String packagname = ri.activityInfo.packageName;
        	String activityname = ri.activityInfo.name;
        	Log.v("iteratorpackage", packagname);
        	if(searchApp(application.mApplist[AppList.TV], packagname, activityname))
        		continue;
        	if(searchApp(application.mApplist[AppList.GAME], packagname, activityname))
        		continue;
        	if(searchApp(application.mApplist[AppList.MARKET], packagname, activityname))
        		continue;
        	if(searchApp(application.mApplist[AppList.MEDIA], packagname, activityname))
        		continue;
        	if(searchApp(application.mApplist[AppList.SETUP], packagname, activityname))
        		continue;
        	if(searchApp(application.mApplist[AppList.WEB], packagname, activityname))
        		continue;
        	Map<String, Object> map = new HashMap<String, Object>();
        	map.put("resolveInfo", ri);
        	apps.add(map);
        }
        
        //这儿定义isSelected这个map是记录每个listitem的状态，初始状态全部为false。      
        for (int i = 0; i < apps.size(); i++) {    
            isSelected.put(i, false);    
        }    
	}    
	
	private  boolean  searchApp(AppList app, String packagename, String activityname)
	{
		for(int i = 0; i< app.size(); i++)
		{
			StringTokenizer token = new StringTokenizer(app.get(i), "#");
			String rPackagename = token.nextToken();
			String rActivityname = token.nextToken();
			
			if(rPackagename.equals(packagename) && rActivityname.equals(activityname))
				return true;
		}
		return false;
	}
}
