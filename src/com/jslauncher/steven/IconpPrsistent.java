package com.jslauncher.steven;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;


import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;

public class IconpPrsistent {

	public static void removeIcon(Context contex, String packagename)
	{
		File icon_file = new File(contex.getFilesDir()+"/"+ packagename + ".png");
		try
		{
			if(icon_file.exists())
				icon_file.delete();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public static boolean saveIcon(Context contex, String packagename)
	{

		PackageManager pm = contex.getPackageManager();
		Bitmap icon = null;
		try {
			//	lable = (String) pm.getApplicationLabel(pm.getApplicationInfo(packagename, PackageManager.GET_UNINSTALLED_PACKAGES));
			icon = DrawableUtil.drawableToBitmap(pm.getApplicationIcon(packagename));
		} catch (NameNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		}
		File icon_file = new File(contex.getFilesDir()+"/"+ packagename + ".png");
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(icon_file);
			icon.compress(Bitmap.CompressFormat.PNG, 100, fos);
			fos.flush();
			fos.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;

	}
	public static boolean IconInit(Context context, String packagename) {


		File file = new File(context.getFilesDir()+"/"+packagename+".png");
		if(!file.exists())
		{	
			try{
				InputStream is = context.getAssets().open(packagename+".png");
				int size = is.available();
				if (size > 0)
				{

					byte[] buffer = new byte[size];
					is.read(buffer);
					FileOutputStream os = new FileOutputStream(file);
					os.write(buffer);
					os.flush();
					os.close();
					os = null;
					file = null;

				} 
				else
				{
					return false;
				} 
				is.close();
				is = null;
			}
			catch(Exception e)
			{
				e.printStackTrace();
				return false;
			}
			return true;
		}
		return true;
	}
}
