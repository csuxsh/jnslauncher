package com.jslauncher.steven;


import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;

public class SetupWizardActivity extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);   
		FragmentManager fm = getFragmentManager(); 
		if(fm.findFragmentById(android.R.id.content) == null) 
		{ 
			LocalePicker locale = new LocalePicker();
			locale.setActiviy(this);
			fm.beginTransaction().add(android.R.id.content, locale).commit();
		}
	}

}
