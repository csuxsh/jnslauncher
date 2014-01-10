package com.jslauncher.steven;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.R;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.widget.Toast;

public class CheckUpataThread extends Service implements Runnable {

	public static final String HOST_ADDR = "192.168.1.40";
	public static final int PORT = 2399;


	
	private Socket socket = null;
	private BufferedReader br = null;
	private InputStream is = null;
	private OutputStream os = null;
	


	public void run()
	{   
		if(checkVersion())
		{
			showDownloadDailog();
		}
		else
		{
			OutputStreamWriter osw = new OutputStreamWriter(os);
			BufferedWriter bw = new BufferedWriter(osw);
			try {
				bw.write("noupdate");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.onDestroy();
		}
	}
	@SuppressLint("UseValueOf")
	private boolean checkVersion()
	{
		try {
			socket = new Socket(HOST_ADDR, PORT);
		} catch (UnknownHostException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}


		try {
			is = socket.getInputStream();
			os = socket.getOutputStream();
			InputStreamReader ir = new InputStreamReader(is);
			br = new BufferedReader(ir);
			int versioncode = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionCode;
			int version = (new Integer(br.readLine()));
			br.close();
			ir.close();
			if(version >  versioncode)
				return true;
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		return false;
	}
	
    private void showDownloadDailog()
    {
    	new AlertDialog.Builder(this).setIcon(R.id.icon).setTitle("系统有新版本更新")
    	.setPositiveButton("马上更新", 
    			new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						try{
							OutputStreamWriter osw = new OutputStreamWriter(os);
							BufferedWriter bw = new BufferedWriter(osw);
							InputStreamReader ir = new InputStreamReader(is);
							br = new BufferedReader(ir);
							bw.write("update");
							if(br.readLine().equals("sta"))
								bw.write("sta");
							else
							    throw new Exception();
							FileOutputStream fileos = new FileOutputStream(android.os.Environment.getExternalStorageDirectory() + "/jnslauncher.apk");
							byte[] buffer = new byte[8192];
							int count = 0;
							while((count = is.read(buffer))>=0)
							{
								fileos.write(buffer, 0, count);
							}
							fileos.close();
							is.close();
				//			bw.write("updateok");
							bw.close();
							socket.close();
							Intent localIntent = new Intent("android.intent.action.VIEW");
						    Uri localUri = Uri.fromFile(new File(android.os.Environment.getExternalStorageDirectory() + "/jnslauncher.apk"));
						    localIntent.setDataAndType(localUri, "application/vnd.android.package-archive");
						    startActivity(localIntent);
						    onDestroy();
						}
						catch(Exception e)
						{
							e.printStackTrace();
							Toast.makeText(CheckUpataThread.this , "更新失败", Toast.LENGTH_SHORT).show();
							File file = new File(android.os.Environment.getExternalStorageDirectory() + "/jnslauncher.apk");
						    if(file.exists())
						    	file.delete();
						    onDestroy();
						}
						
					}
				}).setNegativeButton("下次再说", new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						OutputStreamWriter osw = new OutputStreamWriter(os);
						BufferedWriter bw = new BufferedWriter(osw);
						try
						{
						bw.write("noupdate");
						socket.close();
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
						 onDestroy();
					}
					
				}).show();
    }
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		this.run();
		return null;
	}
}

