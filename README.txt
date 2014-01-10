前一周： 1.解决二级目录数据保存存在的BUG
	 2.解决点击ICON时ICON焦点有时不正常的BUG
	 3.增加从二级目录拖拽图标掉回收站删除的功能。
	 4.增加从workspace拖拽图标到二级目录的功能。
         5.增加从workspace拖拽图标到ICON从而增加到响应二级目录的功能。
2012-5-28  解决添加应用列表中选择某个ChekBox下一页同一位置也被选中的BUG
2012-5-31  完成图标排序功能，主要修改文件为LauncherModel.java AllApps2D.java AllAppsView.java
2012-5-5    添加删除排序界面，将ALLAPP界面图标放大至96
2012-6-6    解决添加应用时同一个包内不同应用添加不正常的BUG。
	    修改POPUPWINDOW弹出时WORKSPACE不可见，关闭可见
	    修改WORKSPACE范围，距离底部200dp
2012-6-5    修改WORKSPACE显示ICON的大小为96*96，修改文件为res\values-land\dimens.xml
	    修改WORKSPACE显示行数为3行，修改文件为res\layout-land\workspce_screen.xml
2012-6-11   修复切语言，界面显示不正常的BUG.
2012-6-14   若要修改PAGEUP 和PAGEDOWM 需修改D:\vmshare\gingerbread\frameworks\base\policy\src\com\android\internal\policy\impl\PhoneWindowManager.java中的1229行处
2012-6-15   如要修改鼠标光标，需修改com.android.internal.R.drawable.mouse_cursor图片

2012-10-19  在frameworks/base/core/res/res/values目录找到Config.xml文件,最后一行有定义:
 
<string name="default_wallpaper_component">null</string>  
 
将其修改为对应的live wallpaper的启动component即可,如要把默认壁纸设置为2.1自带的galaxy,这样写即可:
 
<string name="default_wallpaper_component">com.android.wallpaper/com.android.wallpaper.galaxy.GalaxyWallpaper</string>
 
需要注意的是:静态的图片壁纸和live wallpaper是两个系统,加载的时候是完全两套机制,静态图片通过launcher里面的一个xml文件配置来管理系统默认静态壁纸,live wallpaper则是通过intent机制向系统搜寻所有匹配Activity,所以,这里的默认值为null,而不是那个系统默认静态壁纸的地址,系统是读config.xml文件的设置,若为null才继续查找静态壁纸配置内容.
 
如果只需要修改默认静态壁纸,替换frameworks/base/core/res/res/drawable/default_wallpaper.jpg即可,或者在源码中修改对应default_wallpaper地址.