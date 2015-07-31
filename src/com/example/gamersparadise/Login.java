package com.example.gamersparadise;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.app.AppConfig;
import com.example.app.AppController;
import com.example.helper.SQLiteHandler;
import com.example.helper.SessionManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends Activity {

	private EditText emailTF,passwordTF;
	private Button loginBtn;
	private TextView gotoRegBtn;
	private ProgressDialog pDialog;
	private SessionManager session;
	private SQLiteHandler db;
	
	private static final String TAG = Login.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		emailTF = (EditText) findViewById(R.id.etEmail);
		passwordTF = (EditText) findViewById(R.id.etPassword);
		loginBtn = (Button) findViewById(R.id.bLogin);
		gotoRegBtn = (TextView) findViewById(R.id.bRegister);
		
		pDialog = new ProgressDialog(this);
		session = new SessionManager (getApplicationContext());
		db = new SQLiteHandler(getApplicationContext());
		
		if(session.isLoggedIn()){
			Intent i = new Intent(this, MainActivity.class);
			startActivity(i);
			finish();
			
		}
		
		gotoRegBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Login.this, Register.class);
				startActivity(i);
				finish();
				
			}
			
		});
		
		loginBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String email = emailTF.getText().toString();
				String password = passwordTF.getText().toString();
				
				if(email.isEmpty() && password.isEmpty()){
					checkLogin(email, password);
					
				}
				else{
					Toast.makeText(Login.this,"Please Eneter Details" ,Toast.LENGTH_LONG).show();
				}
			}
			
			
		});
	}
	
	public void checkLogin(final String email, final String password){
		
		String tag_req = "login_request";
		pDialog.setMessage("Logging In...");
		showDialog();
		
		StringRequest strR = new StringRequest(
				Method.POST, AppConfig.LOGIN_URL,
				new Response.Listener<String>()  {
					@Override
					public void onResponse(String response){
						hideDialog();
						
						try{
							JSONObject jsb = new JSONObject(response);
							boolean error = jsb.getBoolean("error");
							if(!error){
								String uid = jsb.getString("uid");
								
								JSONObject user = jsb.getJSONObject("user");
								String name = user.getString("name");
								String email = user.getString("email");
								String created_at = user.getString("created_at");
								db.addUser(name, email, uid, created_at);
							
								session.setLogin(true);
								
								Intent i = new Intent(Login.this, MainActivity.class);
								startActivity(i);
								finish();
								
							}else{
								String error_msg = jsb.getString("error_msg");
								Toast.makeText(Login.this, error_msg , Toast.LENGTH_LONG).show();
							}
						}
						catch(JSONException e){
							e.printStackTrace();
						}
					}
				},
				new Response.ErrorListener(){
					
					@Override
					public void onErrorResponse(VolleyError error){
						Log.e(TAG, error.getMessage());
					}
					
				} 
				){
			@Override
			public Map<String, String> getParams(){
				Map<String, String> p = new HashMap<String, String>();
				p.put("tag","login");
				p.put("email", email);
				p.put("password",password);
				return p;
			}
			
		};
		
		AppController.getInstance().addRequestQueue(strR, tag_req);
	}
	
	public void showDialog(){
		if (!pDialog.isShowing()){
			pDialog.show();
		}
	}
	
	public void hideDialog(){
		if(pDialog.isShowing()){
			
			pDialog.dismiss();
		}
		
	}
}
