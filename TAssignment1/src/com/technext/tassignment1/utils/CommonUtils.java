package com.technext.tassignment1.utils;

import android.text.TextUtils;

public class CommonUtils {

	public final static boolean isValidEmail(CharSequence target) {
		  return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
		}
}
