package com.example.gamersparadise;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import com.example.gamersparadise.R;
import com.example.gamersparadise.Welcome;

public class Welcome extends Activity{
	MediaPlayer ourSong;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		ourSong = MediaPlayer.create(Welcome.this, R.raw.splash);
		ourSong.start();
		Thread timer = new Thread() {
			
			public void run(){
				try{
					sleep(14000);
					
				} catch	(InterruptedException e){
					e.printStackTrace();
				}
				finally{
					Intent openStartingPoint = new Intent("com.example.gamersparadise.LOGIN");
					startActivity(openStartingPoint);
				}
			}
			
			
		}; timer.start();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		ourSong.release();
		finish();
	}
	
	

}