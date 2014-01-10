package com.jslauncher.steven;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class AppHelper {
	
	private DBHelper dbh;
	
	public AppHelper(DBHelper dbh)
	{
		this.dbh = dbh;
	}

	public DBHelper getDbh() {
		return dbh;
	}



	public void setDbh(DBHelper dbh) {
		this.dbh = dbh;
	}

	synchronized public boolean Insert(String name, String list)
	{
		SQLiteDatabase db = dbh.getReadableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("_name", name);
		cv.put("_description", list);
		try {
			delete(name);
			if(db.insert("_jns_launcher", "", cv)> -1)
				return true;
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}
	public boolean delete(String name)
	{
		SQLiteDatabase db = dbh.getReadableDatabase();
		if(db.delete("_jns_launcher", "_name=?", new String[] { name }) >0)
			return false;
		return true;
	}
	synchronized public Cursor Qurey(String arg)
	{
		//String arg = startdate+" and "+enddate;
		String selection = " _name='"+arg+"'";
		System.out.println(selection);
		SQLiteDatabase db = dbh.getReadableDatabase();
		Cursor cursor = null;
		try {
				cursor = db.query("_jns_launcher", null, selection,
						null, null, null, "_id");
				if(cursor.moveToFirst())
				{
					System.out.println("cuisor has content");
				}
				else
				{
					System.out.println("cuisor has none");
					String filename = android.os.Environment.getExternalStorageDirectory() + "/_jns_launcher";
					
					SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(filename, null);
					try{
						cursor = sqLiteDatabase.query("_jns_launcher", null, selection,
							null, null, null, "_id");
						cursor.moveToFirst();
					if(!Insert(arg, cursor.getString(cursor.getColumnIndex("_description"))))
						Log.d("steven_create databas", "insert failed");
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
					finally 
					{
						sqLiteDatabase.close();
					}
				}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return cursor;
	}
}
