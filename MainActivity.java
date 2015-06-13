package com.example.nongsaro;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
	
		Button btn1 = (Button)findViewById(R.id.btn1);
		btn1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, TechActivity.class);
				startActivity(intent);
			}
		});

		Button btn2 = (Button)findViewById(R.id.btn2);
		btn2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, IncomeActivity.class);
				startActivity(intent);
			}
		});
	
		Button btn3 = (Button)findViewById(R.id.btn3);
		btn3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, DbyhsActivity.class);
				startActivity(intent);
			}
		});
	}
}
