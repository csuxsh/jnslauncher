package com.jslauncher.steven;



import com.jslauncher.launcher.ApplicationInfo;
import com.jslauncher.launcher.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 用于显示popupwindow中item的adapter
 */

public class AppsAdapter extends BaseAdapter {

	private AppList apps ;
	private Activity launcher;
	LayoutInflater inflater;

	public AppList getApps() {
		return apps;
	}

	public void setApps(AppList apps) {
		this.apps = apps;
	}

	public AppsAdapter(Activity launcher, AppList apps){

		super();
		this.launcher = launcher;
		this.apps = apps;
		inflater = (LayoutInflater) this.launcher.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Log.d("new adapter", this.launcher.toString());
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return apps.size()+1;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return apps.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		Log.d("getvkiew","enter getview");
		Log.d("getvkiew",""+apps.size());
		if(position == apps.size())
		{
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.appitem, parent, false);
			}

			TextView textView = (TextView)convertView;
			Drawable da = launcher.getResources().getDrawable(R.drawable.add);
			da.setBounds(0, 0, 96, 96);
			//textView.setCompoundDrawablesWithIntrinsicBounds(null,da, null, null);
			textView.setCompoundDrawables(null, da, null, null);
			textView.setText(R.string.click_to_add);
			return textView;
		}

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.appitem, parent, false);
		}

		//ResolveInfo ri = apps.getResolveInfo(position);
		ApplicationInfo app = apps.getAppinfo().get(position);
		TextView textView = (TextView)convertView;
		@SuppressWarnings("deprecation")
		Drawable da =  new BitmapDrawable(app.iconBitmap);//ri.activityInfo.loadIcon( launcher.getPackageManager());
		da.setBounds(0, 0, 96, 96);
		//textView.setCompoundDrawablesWithIntrinsicBounds(null,da, null, null);
		textView.setCompoundDrawables(null, da, null, null);
		textView.setText(app.getTitle());//ri.activityInfo.loadLabel(launcher.getPackageManager()));
		textView.setGravity(Gravity.CENTER_HORIZONTAL);
		return textView;
	}

}
