package com.alovi.activity;

import com.alovi.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MessageBoxActivity extends BaseActivity {

	public static int REQUEST_MSGBOX = 200;
	private Button btnOK;

	public void setMessage(String message) { }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_box);

		btnOK = (Button) findViewById(R.id.okButton);
		btnOK.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(RESULT_OK);
				MessageBoxActivity.this.finish();
			}
		});

		Intent intent = getIntent();
		String msg = intent.getStringExtra("Message");
		TextView text = (TextView) findViewById(R.id.text);
		text.setText(msg);
	}
}