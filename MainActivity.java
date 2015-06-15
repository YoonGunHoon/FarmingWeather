package com.example.nongsaro;


import android.app.Activity;
import android.content.Intent;
import android.graphics.*;
import android.net.*;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		//현재날씨
		Button btn0 = (Button)findViewById(R.id.btn0);
		btn0.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
				startActivity(intent);
			}
		});
		
		
		//농업기술
		Button btn1 = (Button)findViewById(R.id.btn1);
		btn1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, TechActivity.class);
				startActivity(intent);
			}
		});
		//소득
		Button btn2 = (Button)findViewById(R.id.btn2);
		btn2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, IncomeActivity.class);
				startActivity(intent);
			}
		});
		//병해충
		Button btn3 = (Button)findViewById(R.id.btn3);
		btn3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, DbyhsActivity.class);
				startActivity(intent);
			}
		});
		Button btn4 = (Button)findViewById(R.id.btn4);
		btn4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			
				Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse("http://m.kma.go.kr/m/warning/warning_01.jsp"));
	
				startActivity(intent);
			}
		});
		TextView tvMain1 = (TextView)findViewById(R.id.textView1);
		tvMain1.setTypeface(Typeface.createFromAsset(getAssets(), "binpio2.ttf"));
		TextView tvMain2 = (TextView)findViewById(R.id.textView2);
		tvMain2.setTypeface(Typeface.createFromAsset(getAssets(), "sehoon.ttf"));
		
	}
}
