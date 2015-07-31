package com.example.helper;

import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHandler extends SQLiteOpenHelper {
	
	public static final String TAG = SQLiteHandler.class.getSimpleName();
	
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "login_db";
	private static final String TABLE_NAME = "users";
	
	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_EMAIL = "email";
	private static final String KEY_UID = "uid";
	private static final String KEY_CREATED_AT = "created_at";
	
	
	public SQLiteHandler (Context context){
		
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
		String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_NAME +
				" TEXT," + KEY_EMAIL + " TEXT UNIQUE," + KEY_UID + " TEXT,"
				+KEY_CREATED_AT + " TEXT" + ")";

		db.execSQL(CREATE_TABLE);
		
		Log.d(TAG, "Database table created");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		
		onCreate(db);
		

	}

		public void addUser(String name, String email, String uid, String created_at){
			SQLiteDatabase db = this.getWritableDatabase();
			
			ContentValues values = new ContentValues();
			
			values.put(KEY_NAME, name);
			values.put(KEY_EMAIL, email);
			values.put(KEY_UID, uid);
			values.put(KEY_CREATED_AT, created_at);
			
			
			long id = db.insert(TABLE_NAME, null, values);
			
			db.close();
			
			Log.d(TAG, "New user was added to sqlite " + id);
			
		}
		
		public HashMap<String, String> getUserDetails(){
			
			HashMap<String, String > temp = new HashMap<String, String>();
			String selectQuery = "SELECT * FROM " + TABLE_NAME;
			
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);
			
			cursor.moveToFirst();
			if(cursor.getCount() > 0){
				
				temp.put("name", cursor.getString(1));
				temp.put("email", cursor.getString(2));
				temp.put("uid", cursor.getString(3));
				temp.put("created_at", cursor.getString(4));
			}
			
			cursor.close();
			db.close();
			
			Log.d(TAG,"Fetching user info from sqlite");
			
			
			return temp;
		}
		
		public int getRowCount(){
			
			String countQuery = "SELECT * FROM "+ TABLE_NAME;
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor c = db.rawQuery(countQuery, null);
			int rowcount = c.getCount();
			db.close();
			c.close();
			
			return rowcount;
		}
		
		public void deleteUser(){
			SQLiteDatabase db = this.getWritableDatabase();
			
			db.delete(TABLE_NAME, null , null);
			db.close();
			
			Log.d(TAG, "User has been deleted from sqlite");
			
			
		}
}
