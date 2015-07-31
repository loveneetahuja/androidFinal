package com.example.gamersparadise;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
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

public class Register extends Activity {

	private EditText nameTF, emailTF, passwordTF;
	private Button registerBtn;
	private TextView gotoLogBtn;
	private SessionManager session;
	private SQLiteHandler db;
	private ProgressDialog pDialog;
	
	private static final String TAG = Register.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

		nameTF = (EditText) findViewById(R.id.etName);
		emailTF = (EditText) findViewById(R.id.etEmail);
		passwordTF = (EditText) findViewById(R.id.etPassword);
		registerBtn = (Button) findViewById(R.id.bRegister);
		gotoLogBtn = (TextView) findViewById(R.id.tvLogin);

		session = new SessionManager(getApplicationContext());
		db = new SQLiteHandler(getApplicationContext());

		pDialog = new ProgressDialog(this);

		if (session.isLoggedIn()) {

			Intent i = new Intent(this, MainActivity.class);
			startActivity(i);
			finish();
		}
		gotoLogBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Register.this, Login.class);
				startActivity(i);
				finish();
			}

		});
		registerBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String name = nameTF.getText().toString();
				String email = emailTF.getText().toString();
				String password = passwordTF.getText().toString();

				if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
					registerCheck(name, email, password);

				}
			}

		});
	}

	public void registerCheck(final String name, final String email,
			final String password) {

		String tag_req = "register_request";
		pDialog.setMessage("Registering...");
		showDialog();

		StringRequest strR = new StringRequest(Method.POST,
				AppConfig.REGISTER_URL, new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						hideDialog();

						try {
							JSONObject jsb = new JSONObject(response);
							boolean error = jsb.getBoolean("error");
							if (!error) {
								String uid = jsb.getString("uid");

								JSONObject user = jsb.getJSONObject("user");
								String name = user.getString("name");
								String email = user.getString("email");
								String created_at = user
										.getString("created_at");
								db.addUser(name, email, uid, created_at);

								session.setLogin(true);

								Intent i = new Intent(Register.this,
										MainActivity.class);
								startActivity(i);
								finish();

							} else {
								String error_msg = jsb.getString("error_msg");
								Toast.makeText(Register.this, error_msg,
										Toast.LENGTH_LONG).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						Log.e(TAG, error.getMessage());
					}

				}) {
			@Override
			public Map<String, String> getParams() {
				Map<String, String> p = new HashMap<String, String>();
				p.put("tag", "register");
				p.put("name", name);
				p.put("email", email);
				p.put("password", password);
				return p;
			}

		};

		AppController.getInstance().addRequestQueue(strR, tag_req);
	}

	public void showDialog() {
		if (!pDialog.isShowing()) {
			pDialog.show();
		}
	}

	public void hideDialog() {
		if (pDialog.isShowing()) {

			pDialog.dismiss();
		}

	}
}
