package com.jslauncher.steven;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	
	private static DBHelper dbh= null;

	private DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub
		String sql = "CREATE TABLE _jns_launcher(" +
		"_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
		 "_name VARCHAR," +
		 "_description VARCHAR)";
		
		arg0.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		String sql1 =  "drop table _jns_launcher";
		arg0.execSQL(sql1);
		String sql = "CREATE TABLE _jns_launcher(" +
		"_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
		 "_name VARCHAR," +
		 "_description VARCHAR)";
		
		arg0.execSQL(sql);
			

	}
	
	public static DBHelper getDBHelper(Activity activity)
	{	
		if(dbh == null)
			dbh = new  DBHelper(activity, "_jns_launcher", null, 1);
		return dbh;
	}

}
