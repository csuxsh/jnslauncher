package com.jslauncher.steven;

import com.jslauncher.launcher.LauncherApplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Message;

public class PopWindowReciver extends BroadcastReceiver {


	LauncherApplication mApp;
	MyFrame myframe;


	public PopWindowReciver(LauncherApplication mapp, MyFrame myframe)
	{
		mApp = mapp;
		this.myframe = myframe;
	}
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub

		final String action = arg1.getAction();
		
		if(action.equals(MyFrame.ACTION_SAVE_LIST))
		{
			for(int i = 1; i < ((LauncherApplication)myframe.mLauncher.getApplicationContext()).mApplist.length; i++)
			{
				myframe.popwindow.saveList(((LauncherApplication)myframe.mLauncher.getApplicationContext()).mApplist[i]);
			}
		}

		if(Intent.ACTION_PACKAGE_REMOVED.equals(action))
		{
			final String packageName2 = arg1.getData().getSchemeSpecificPart();
			
			for(int i = 1; i < mApp.mApplist.length; i++)
			{
				AppList app3 = mApp.mApplist[i];
				for(int j = 0; j < app3.appinfo.size(); j++)
				{
					if(packageName2.endsWith(app3.appinfo.get(j).getComponentName().getPackageName()))
					{
						app3.remove(j);
					}
				}
			}
			/*
			AppList app3 = mApp.mtvlist;
			for(int i = 0; i < app3.appinfo.size(); i++)
			{
				if(packageName2.endsWith(app3.appinfo.get(i).getComponentName().getPackageName()))
				{
					app3.remove(i);
				}
			}
			app3 = mApp.mgamelist;
			for(int i = 0; i < app3.appinfo.size(); i++)
			{
				if(packageName2.endsWith(app3.appinfo.get(i).getComponentName().getPackageName()))
				{
					app3.remove(i);
				}
			}
			app3 = mApp.mmarketlist;
			for(int i = 0; i < app3.appinfo.size(); i++)
			{
				if(packageName2.endsWith(app3.appinfo.get(i).getComponentName().getPackageName()))
				{
					app3.remove(i);
				}
			}
			app3 = mApp.mmedialist;
			for(int i = 0; i < app3.appinfo.size(); i++)
			{
				if(packageName2.endsWith(app3.appinfo.get(i).getComponentName().getPackageName()))
				{
					app3.remove(i);
				}
			}
			app3 = mApp.msetuplist;
			for(int i = 0; i < app3.appinfo.size(); i++)
			{
				if(packageName2.endsWith(app3.appinfo.get(i).getComponentName().getPackageName()))
				{
					app3.remove(i);
				}
			}
			app3 = mApp.mweblist;
			for(int i = 0; i < app3.appinfo.size(); i++)
			{
				if(packageName2.endsWith(app3.appinfo.get(i).getComponentName().getPackageName()))
				{
					app3.remove(i);
				}
			}
			*/
			try{
				Message m = new Message();
				m.what = PopWindow.NOTIFY;
				myframe.popwindow.myMessageHandler.sendMessage(m);
			}
			catch(Exception e)
			{
				for(int i = 1; i < ((LauncherApplication)myframe.mLauncher.getApplicationContext()).mApplist.length; i++)
				{
					myframe.popwindow.saveList(((LauncherApplication)myframe.mLauncher.getApplicationContext()).mApplist[i]);
				}
				/*
				myframe.popwindow.saveList(((LauncherApplication)myframe.mLauncher.getApplicationContext()).mtvlist);
				myframe.popwindow.saveList(((LauncherApplication)myframe.mLauncher.getApplicationContext()).mweblist);
				myframe.popwindow.saveList(((LauncherApplication)myframe.mLauncher.getApplicationContext()).mmedialist);
				myframe.popwindow.saveList(((LauncherApplication)myframe.mLauncher.getApplicationContext()).mmarketlist);
				myframe.popwindow.saveList(((LauncherApplication)myframe.mLauncher.getApplicationContext()).mgamelist);
				myframe.popwindow.saveList(((LauncherApplication)myframe.mLauncher.getApplicationContext()).msetuplist);
				*/
			}
		}
	}

}
