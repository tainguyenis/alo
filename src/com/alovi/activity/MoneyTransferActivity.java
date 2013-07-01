package com.alovi.activity;

import com.alovi.R;
import com.alovi.common.MainMenu;
import com.alovi.common.MessageTypes;
import com.alovi.common.Util;
import com.alovi.controller.UserController;
import com.alovi.data.GlobalResource;
import com.alovi.data.UserData;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;

public class MoneyTransferActivity extends BaseActivity {
	private TableLayout layout;
	private EditText txtOldPass, txtNewPass, txtRePass;
	private Button btnSave;
	private View.OnClickListener clickListener;
	private String newPassword;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.moneytransfer);
		
		MainMenu menu = new MainMenu(this, true);
		menu.setTextMainTitle(getString(R.string.txt_changepassword_title).toString());
		
		txtOldPass = (EditText) findViewById(R.id.txtOldPassword);
		txtNewPass = (EditText) findViewById(R.id.txtNewPassword);
		txtRePass = (EditText) findViewById(R.id.txtReNewPassword);
		btnSave = (Button) findViewById(R.id.btn_changepass_save);
		setControls();
	}

	public void setControls() {
		layout = (TableLayout) findViewById(R.id.tablelayout_changepass);
		clickListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				handleClickEvent(v);
			}
		};
		for (int i = 0; i < 3; i++) {
			TableRow row = new TableRow(this);
			for (int j = 0; j < 6; j++) {
				Button btn = new Button(this);
				btn.setPadding(15, 15, 15, 15);
				btn.setBackgroundResource(R.drawable.btn_send);
				btn.setText((6 * i + j + 1) + "");
				btn.setTextColor(Color.WHITE);
				btn.setTextSize(20);
				btn.setGravity(Gravity.CENTER);
				btn.setOnClickListener(clickListener);
				row.addView(btn, j);
			}
			layout.addView(row, i);
		}
		btnSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					if (Util.checkInternet(MoneyTransferActivity.this)) {
						showDialog(PROGRESS_DIALOG);
						new Thread(new Runnable() {
							@Override
							public void run() {
								newPassword = txtNewPass.getText().toString();
								if(newPassword.isEmpty()) {
									Util.showDialog(MoneyTransferActivity.this,getString(R.string.txt_warning), getString(R.string.msg_changepassword_null));
									return;
								}
								UserData res=UserController.changePassword(newPassword);
								GlobalResource globalResource = GlobalResource.getInstance();
								globalResource.setPassword(newPassword);
								String res1="null";
								if(res!=null)
									res1="success";
								Message msg = handler.obtainMessage();
								msg.obj=res1;
								handler.sendMessage(msg);
							}
						}).start();
					} else {
						messageBox(MessageTypes.Error, getString(R.string.text_check_net_fail).toString());
					}
				} catch (Exception ex) { }
			}
		});
	}

	private void handleClickEvent(View view) {
		if (view instanceof Button) {
			newPassword += ((Button) view).getText().toString();
			txtNewPass.setText(newPassword);
			txtRePass.setText(newPassword);
		}
	}
	
	final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {			
			dismissDialog(PROGRESS_DIALOG);
			String res= msg.obj.toString();
			if(res.equals("success")) {
				toast(getString(R.string.msg_changepassword_success));
				finish();
			}
			else
				toast(getString(R.string.msg_changepassword_fail));
		}
	};
}