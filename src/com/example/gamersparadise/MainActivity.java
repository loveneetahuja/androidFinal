package com.example.gamersparadise;

import java.util.HashMap;

import com.example.helper.SQLiteHandler;
import com.example.helper.SessionManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private static final String TAG = MainActivity.class.getSimpleName();
	private TextView nameTV, emailTV;
	private Button logoutBtn;
	private SessionManager session;
	private SQLiteHandler db;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		nameTV= (TextView) findViewById(R.id.tvName);
		emailTV= (TextView) findViewById(R.id.tvEmail);
		logoutBtn= (Button) findViewById(R.id.bLogout);
		
		session = new SessionManager(getApplication());
		db = new SQLiteHandler(getApplicationContext());
		
		if(!session.isLoggedIn()){
			logoutUser();
			
			
		}
		HashMap<String, String> detail = db.getUserDetails();
		
		String name = detail.get("name");
		String email = detail.get("email");
		
		nameTV.setText(name);
		emailTV.setText(email);
		
		logoutBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				logoutUser();
				
			}
			
			
		});
	}
	
	private void logoutUser(){
		session.setLogin(false);
		db.deleteUser();
		
		Intent i = new Intent(this, Login.class);
		startActivity(i);
		finish();
	}

	
}
