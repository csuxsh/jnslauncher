ǰһ�ܣ� 1.�������Ŀ¼���ݱ�����ڵ�BUG
	 2.������ICONʱICON������ʱ��������BUG
	 3.���ӴӶ���Ŀ¼��קͼ�������վɾ���Ĺ��ܡ�
	 4.���Ӵ�workspace��קͼ�굽����Ŀ¼�Ĺ��ܡ�
         5.���Ӵ�workspace��קͼ�굽ICON�Ӷ����ӵ���Ӧ����Ŀ¼�Ĺ��ܡ�
2012-5-28  ������Ӧ���б���ѡ��ĳ��ChekBox��һҳͬһλ��Ҳ��ѡ�е�BUG
2012-5-31  ���ͼ�������ܣ���Ҫ�޸��ļ�ΪLauncherModel.java AllApps2D.java AllAppsView.java
2012-5-5    ���ɾ��������棬��ALLAPP����ͼ��Ŵ���96
2012-6-6    ������Ӧ��ʱͬһ�����ڲ�ͬӦ����Ӳ�������BUG��
	    �޸�POPUPWINDOW����ʱWORKSPACE���ɼ����رտɼ�
	    �޸�WORKSPACE��Χ������ײ�200dp
2012-6-5    �޸�WORKSPACE��ʾICON�Ĵ�СΪ96*96���޸��ļ�Ϊres\values-land\dimens.xml
	    �޸�WORKSPACE��ʾ����Ϊ3�У��޸��ļ�Ϊres\layout-land\workspce_screen.xml
2012-6-11   �޸������ԣ�������ʾ��������BUG.
2012-6-14   ��Ҫ�޸�PAGEUP ��PAGEDOWM ���޸�D:\vmshare\gingerbread\frameworks\base\policy\src\com\android\internal\policy\impl\PhoneWindowManager.java�е�1229�д�
2012-6-15   ��Ҫ�޸�����꣬���޸�com.android.internal.R.drawable.mouse_cursorͼƬ

2012-10-19  ��frameworks/base/core/res/res/valuesĿ¼�ҵ�Config.xml�ļ�,���һ���ж���:
 
<string name="default_wallpaper_component">null</string>  
 
�����޸�Ϊ��Ӧ��live wallpaper������component����,��Ҫ��Ĭ�ϱ�ֽ����Ϊ2.1�Դ���galaxy,����д����:
 
<string name="default_wallpaper_component">com.android.wallpaper/com.android.wallpaper.galaxy.GalaxyWallpaper</string>
 
��Ҫע�����:��̬��ͼƬ��ֽ��live wallpaper������ϵͳ,���ص�ʱ������ȫ���׻���,��̬ͼƬͨ��launcher�����һ��xml�ļ�����������ϵͳĬ�Ͼ�̬��ֽ,live wallpaper����ͨ��intent������ϵͳ��Ѱ����ƥ��Activity,����,�����Ĭ��ֵΪnull,�������Ǹ�ϵͳĬ�Ͼ�̬��ֽ�ĵ�ַ,ϵͳ�Ƕ�config.xml�ļ�������,��Ϊnull�ż������Ҿ�̬��ֽ��������.
 
���ֻ��Ҫ�޸�Ĭ�Ͼ�̬��ֽ,�滻frameworks/base/core/res/res/drawable/default_wallpaper.jpg����,������Դ�����޸Ķ�Ӧdefault_wallpaper��ַ.