package com.android.guesswords;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DialogActivityRight extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog);
		
		TextView textview=(TextView) findViewById(R.id.textView1);
		Button button=(Button) findViewById(R.id.button_ok_dialog1);
		
		Intent intent=getIntent();
		Bundle bundle=new Bundle();
		bundle=intent.getExtras();
		int rightTimes=bundle.getInt("rightTimes");
		textview.setText("正确次数："+rightTimes);
		
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent =new Intent();
				intent.setClass(DialogActivityRight.this, OtherActivity.class);
				startActivity(intent);
				
			}
		});
	}

}
