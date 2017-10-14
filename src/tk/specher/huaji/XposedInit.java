package tk.specher.huaji;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.XModuleResources;
import android.content.res.XResources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.XC_MethodHook.MethodHookParam;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class XposedInit implements IXposedHookLoadPackage ,IXposedHookZygoteInit,IXposedHookInitPackageResources {
	private static String MODULE_PATH = null;
	private final String TAG = "Xposed";
	Settings setting = new Settings();
    public static XModuleResources sModRes;

	public void log(String s){
		Log.d(TAG, s);
		XposedBridge.log(s);
	}

	@Override
	public void handleLoadPackage(final LoadPackageParam loadPackageParam){
		
		//这里是为了解决app多dex进行hook的问题，Xposed默认是hook主dex
		XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
			@Override
			protected void afterHookedMethod(MethodHookParam param) throws Throwable {
				ClassLoader cl = ((Context)param.args[0]).getClassLoader();
				Class<?> hookclass = null,imageView=null;
				try {
					hookclass = cl.loadClass("android.graphics.drawable.Drawable");
					imageView = cl.loadClass("android.widget.ImageView");
					setting.reload();
					if(hookclass!=null)
					XposedBridge.hookAllMethods(hookclass, "drawableFromBitmap", new XC_MethodHook() {
					
						protected void afterHookedMethod(MethodHookParam methodHookParam) throws Throwable {
							
							  if (methodHookParam.args.length == 6) {

					            if (((byte[]) methodHookParam.args[2]) == null) {
					            	if(setting.isStarted())
					                 methodHookParam.setResult(sModRes.getDrawable( R.drawable.ic_launcher));
						                }
							
							  }
						}
					});
					
					
					if(imageView!=null)
						XposedBridge.hookAllMethods(imageView,"setImageDrawable", new XC_MethodHook() {
							
							protected void beforeHookedMethod(MethodHookParam methodHookParam) throws Throwable {
									if(setting.isStarted())
						           methodHookParam.args[0]=sModRes.getDrawable(R.drawable.ic_launcher);	  
							}
						});
					//View.class.unscheduleDrawable drable
						
					
					
					
				} catch (Exception e) {
					XposedBridge.log("huaji:"+e.toString());
				}


			}
			});

	}
	
	
	
	
	
	@Override
	public void initZygote(IXposedHookZygoteInit.StartupParam startupParam) throws Throwable {
	//    XResources.setSystemWideReplacement("android", "bool", "config_unplugTurnsOnScreen", false);
		MODULE_PATH = startupParam.modulePath;
        sModRes = XModuleResources.createInstance(MODULE_PATH, null);

	}

	
	
	@Override
	public void handleInitPackageResources(InitPackageResourcesParam resparam) throws Throwable {
	    
		//XModuleResources modRes = XModuleResources.createInstance(MODULE_PATH, resparam.res);
       // resparam.res.setReplacement("com.android.systemui", "drawable", "stat_sys_battery", modRes.fwd(R.drawable.ic_launcher));
		
		
	}
	
	   public static Bitmap drawableToBitmap(Drawable drawable) {

	        int w = drawable.getIntrinsicWidth();
	        int h = drawable.getIntrinsicHeight();
	        Bitmap.Config config =
	                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
	                        : Bitmap.Config.RGB_565;
	        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
	        //注意，下面三行代码要用到，否则在View或者SurfaceView里的canvas.drawBitmap会看不到图
	        Canvas canvas = new Canvas(bitmap);
	        drawable.setBounds(0, 0, w, h);
	        drawable.draw(canvas);

	        return bitmap;
	    }
}
