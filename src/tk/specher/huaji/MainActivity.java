package tk.specher.huaji;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends Activity {
	Settings setting;
	Switch s ;
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_demo);
		setting = new Settings(this);
		s = (Switch) findViewById(R.id.switch1);
		s.setChecked(setting.isStarted());
		s.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO 自动生成的方法存根
				setting.setStarted(isChecked);
			}
		});
		
	}
	
}
