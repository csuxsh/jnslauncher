package com.jslauncher.steven;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;



public class DrawableUtil {

	@SuppressWarnings("deprecation")
	public static Drawable zoomDrawable(Drawable drawable, int w, int h)  
	{  
		int width = drawable.getIntrinsicWidth();  
		int height= drawable.getIntrinsicHeight();  
		Bitmap oldbmp = drawableToBitmap(drawable); // drawable转换成bitmap  
		Matrix matrix = new Matrix();   // 创建操作图片用的Matrix对象  
		float scaleWidth = ((float)w / width);   // 计算缩放比例  
		float scaleHeight = ((float)h / height);  
		matrix.postScale(scaleWidth, scaleHeight);         // 设置缩放比例  
		Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height, matrix, true);       // 建立新的bitmap，其内容是对原bitmap的缩放后的图  
		return new BitmapDrawable(newbmp);       // 把bitmap转换成drawable并返回  
	}  
	
	@SuppressWarnings("deprecation")
	public static Drawable zoomDrawable(Bitmap bitmap, int w, int h)  
	{  
		int width = bitmap.getWidth();  
		int height= bitmap.getHeight();  
		//Bitmap oldbmp = drawableToBitmap(drawable); // drawable转换成bitmap  
		Matrix matrix = new Matrix();   // 创建操作图片用的Matrix对象  
		float scaleWidth = ((float)w / width);   // 计算缩放比例  
		float scaleHeight = ((float)h / height);  
		matrix.postScale(scaleWidth, scaleHeight);         // 设置缩放比例  
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);       // 建立新的bitmap，其内容是对原bitmap的缩放后的图  
		return new BitmapDrawable(newbmp);       // 把bitmap转换成drawable并返回  
	}  

	public static Bitmap drawableToBitmap(Drawable drawable) // drawable 转换成bitmap  
	{  
		int width = drawable.getIntrinsicWidth();   // 取drawable的长宽  
		int height = drawable.getIntrinsicHeight();  
		Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888:Bitmap.Config.RGB_565;         // 取drawable的颜色格式  
		Bitmap bitmap = Bitmap.createBitmap(width, height, config);     // 建立对应bitmap  
		Canvas canvas = new Canvas(bitmap);         // 建立对应bitmap的画布  
		drawable.setBounds(0, 0, width, height);  
		drawable.draw(canvas);      // 把drawable内容画到画布中  
		return bitmap;  
	}  

}
