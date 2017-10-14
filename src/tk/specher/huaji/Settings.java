package tk.specher.huaji;

import android.content.Context;
import android.content.SharedPreferences;
import de.robv.android.xposed.XSharedPreferences;

public class Settings {
	private Context context = null;
	private XSharedPreferences xSharedPreferences = null;
    private SharedPreferences sharedPreferences = null;

    
    
    public Settings() {
		xSharedPreferences = new XSharedPreferences("tk.specher.huaji");
		xSharedPreferences.makeWorldReadable();
	}

	public Settings(Context context) {
        sharedPreferences = context.getSharedPreferences("tk.specher.huaji" + "_preferences", Context.MODE_WORLD_READABLE);
        this.context = context;
    }
    
	  
	  
	  public boolean isStarted() {
		
			if (sharedPreferences != null)
				return sharedPreferences.getBoolean("isOn", true);
			else if (xSharedPreferences != null)
				return xSharedPreferences.getBoolean("isOn", true);
			return false;
	    }
	  
	  public void setStarted(boolean start) {
			
		  SharedPreferences.Editor prefEditor = sharedPreferences.edit();
	        prefEditor.putBoolean("isOn",  start);
	        prefEditor.commit();		
	    }
	  

	  
	  public void reload() {
	        xSharedPreferences.reload();
	        xSharedPreferences.makeWorldReadable();
	    }
}
