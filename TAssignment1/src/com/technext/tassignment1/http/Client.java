package com.technext.tassignment1.http;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.technext.tassignment1.model.User;



public class Client {
	//login params
	public static final String URL_BASE = "http://hellojewel.net/projects/cwc/public/";
	// authentication
	public static final String URL_LOGIN = "login";
	public static final String URL_REGISTER = "register";
	
	// authenticated
	public static final String URL_PROFILE_EDIT = "/user/edit";
	public static final String URL_UPLOAD_PRO_PIC = "/user/upload/profilePic";
	
	/**
	 * Param keys
	 */
	public static final String PARAM_USER_ID = "user_id";
	public static final String PARAM_SESSION_TOKEN = "session_token";
	public static final String PARAM_EMAIL = "email";
	public static final String PARAM_PASSWORD = "password";
	public static final String PARAM_NEW_PASSWORD = "new_password";
	public static final String PARAM_PASSWORD_CONFIRMATION = "password_confirmation";
	public static final String PARAM_PROFILE_PIC = "profile_pic";
	public static final String PARAM_FIRSTNAME = "firstname";
	public static final String PARAM_LASTNAME = "lastname";
	/**
	 * Param keys end
	 */
	
	
	
	private static AsyncHttpClient client = new AsyncHttpClient();
	
	//static User object to easily get user infomation
	private static User user;
	private static Gson gson = new Gson();
	
	public static User getUser(){
		return user;
	}
	
	public static void setUser(User loggedUser){
		user = loggedUser;
	}
	
	public static AsyncHttpClient getClient(){
		return client;
	}
	public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler){
		//client.addHeader("User-Agent", "AppleCoreMedia/1.0.0.9B206 (iPad; U; CPU OS 5_1_1 like Mac OS X; en_us)");
		client.get(getAbsoluteUrl(url), params, responseHandler);
	}
	/**
	 * Post action without checking the session_token's expire_time
	 * 
	 * @param url
	 * @param params
	 * @param responseHandler
	 */
	public static void raw_post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler){
		client.post(getAbsoluteUrl(url), params, responseHandler);
	}
	
	/**
	 * Post action which will check and refresh session_token when needed
	 * 
	 * @param context
	 * @param url
	 * @param params
	 * @param responseHandler
	 */
	public static void post(Context context, String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
	    //check session_token expire_time, if expired, referesh it
		if(isValidSession(context))
			client.post(getAbsoluteUrl(url), params, responseHandler);
		else{
			Toast.makeText(context, "You are not Logged in", Toast.LENGTH_SHORT).show();
		}
	  }
	
	private static String getAbsoluteUrl(String relativeUrl) {
		return URL_BASE + relativeUrl;
	}
	
	public static boolean isValidSession(Context context){
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
		String userString = pref.getString("user", null);
		if(userString != null && !userString.equalsIgnoreCase("")){
			return true;
		}
		
		return false; //no session_token provided or lost, need to get it again
	}
	
	/**
	 * Save the user data into SharedPreference
	 * @param context
	 * @param user
	 */
	
	public static void saveSession(Context context, User user){
		//save the token
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = pref.edit();
		/*editor.putString("session_token", user.getSession_token());
		editor.putLong("user_id", user.getId());
		editor.putString("email", user.getEmail());
		if(user.getProfile_pic_url_value()!=null){
			editor.putString("profile_pic_url", user.getProfile_pic_url_value());
		}
		if(user.getProfile_pic_extension()!=null){
			editor.putString("profile_pic_extension", user.getProfile_pic_extension());
		}*/
		
		editor.putString("user",gson.toJson(user));
		
		editor.commit();
	}
	
	public static void removeSession(Context context){
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = pref.edit();
		editor.remove("user");
		editor.commit();
	}
	
	
	public static User getUserFromSession(Context context){
		
		if(user != null ){
			return user;
		}else{
			SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
			String userString = pref.getString("user", null);
			if(userString != null && !userString.equalsIgnoreCase("")){
				user = gson.fromJson(userString, User.class);
				return user;
			}
			return null;

		}
	}
	
}
