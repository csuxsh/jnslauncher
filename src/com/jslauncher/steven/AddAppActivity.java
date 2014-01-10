package com.jslauncher.steven;


import com.jslauncher.launcher.LauncherApplication;
import com.jslauncher.launcher.R;
import com.jslauncher.steven.AppList;
import com.jslauncher.steven.PopAddAdapter;
import android.app.Activity;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

public class AddAppActivity extends Activity {

	PopAddAdapter ad;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);   
		setContentView(R.layout.popadd);
		@SuppressWarnings("deprecation")
		final int listType = (Integer) this.getIntent().getExtra("listtype");
		ListView list = (ListView) this.findViewById(R.id.lv);
		Button sure =(Button) this.findViewById(R.id.sure);
		Button cancer =(Button) this.findViewById(R.id.cancer);
		ad = new PopAddAdapter();
		ad.init(this);
		list.setAdapter(ad);
		list.setItemsCanFocus(false);    
		// list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		list.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				// TODO Auto-generated method stub
				// ViewHolder vHollder = (ViewHolder) view.getTag();    
				//在每次获取点击的item时将对于的checkbox状态改变，同时修改map的值。    
				CheckBox cbox = (CheckBox) view.findViewById(R.id.cb); 
				Log.v("check box","select at" + position);
				cbox.toggle();    
				PopAddAdapter.isSelected.put(position, cbox.isChecked());
			}

		});
		sure.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AppList apps = null;
				
			
				apps = ((LauncherApplication)AddAppActivity.this.getApplicationContext()).mApplist[listType];
				for(int i = 0; i < PopAddAdapter.isSelected.size(); i++)
				{
					if(PopAddAdapter.isSelected.get(i))
					{
						ResolveInfo rinfo = (ResolveInfo)((ad.apps.get(i)).get("resolveInfo"));
						apps.add(rinfo.activityInfo.packageName+"#"+rinfo.activityInfo.name);
					}
				}
				AddAppActivity.this.finish();
			}
			
		});
		cancer.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AddAppActivity.this.finish();
			}

		});
	}
}
