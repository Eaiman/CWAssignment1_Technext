package com.technext.tassignment1.dialog;

import android.app.ProgressDialog;
import android.content.Context;

import com.technext.tassignment1.R;

public class SplashProgressDialog extends ProgressDialog{

	public SplashProgressDialog(Context context) {
		super(context);
	}
	
	@Override
	public void show(){
		super.show();
		setContentView(R.layout.loading);
	}
}
