package com.onevol.onevol2048;

import com.onevol.onevol2048.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	public MainActivity() {
		mainActivity = this;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		tvScore = (TextView) findViewById(R.id.tvScore);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.action_quit:
			finish();
			break;
		case R.id.action_new:
			GameView gv = new GameView(this);
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void clearScore(){
		score = 0;
		showScore();
	}
	
	public void showScore(){
		tvScore.setText(score+"");
	}
	
	public void addScore(int s){
		score+=s;
		showScore();
	}

	private int score = 0;
	private TextView tvScore;
	
	private static MainActivity mainActivity = null;
	
	public static MainActivity getMainActivity() {
		return mainActivity;
	}

}
