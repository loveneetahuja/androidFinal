package com.example.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SessionManager {

	public static final String TAG = SessionManager.class.getSimpleName();
	
	SharedPreferences pref;
	
	Editor editor;
	Context _context;
	
	int PRIVATE_MODE = 0;
	
	private static final String PREF_NAME = "LoginApiTest";
	private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
	
	public SessionManager(Context context){
		
		this._context = context;
		pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}
	
	
	
	public boolean isLoggedIn(){
		
		return pref.getBoolean(KEY_IS_LOGGEDIN, false);
	}

	public void setLogin(boolean isLoggedIn) {
		// TODO Auto-generated method stub
		editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);
		editor.commit();
		
		Log.d(TAG, "user login modified in pref");
		
	}
}
