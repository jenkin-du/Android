package com.android.guesswords;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DialogActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alert_dialog);
		
		Button button_ok_dialog=(Button) findViewById(R.id.button_ok_dialog);
		TextView textView=(TextView) findViewById(R.id.textView);
		Intent intent=getIntent();
		Bundle bundle=intent.getExtras();
		String string =bundle.getString("rightWord");
		textView.setText("正确的单词是："+string);
		
		button_ok_dialog.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent();
				intent.setClass(DialogActivity.this, OtherActivity.class);
				startActivity(intent);
				
			}
		});
	}

}
