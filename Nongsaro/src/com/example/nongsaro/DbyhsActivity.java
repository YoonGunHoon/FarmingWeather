﻿package com.example.nongsaro;

import java.util.ArrayList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.*;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class DbyhsActivity extends Activity {
	ArrayList<CategoryData> arrayMainCategory = new ArrayList<CategoryData>();
	ArrayList<TechData> arrayMiddleCategory = new ArrayList<TechData>();
	
	CallApiForGetXmlData api;
	
	TextView tvSelect1, tvSelect2; 
	Button btnChoice1, btnChoice2, btnChoice3;
	Button btnChoice1OnTech, btnChoice2OnTech, btnChoice3OnTech;
	Button btnSelectCrop, btnSelectTech;
	int nCurrentChoice = 1;//1>작물선택 2>기술선택
	String strSelectedMain = "", strSelectedMiddle = "", strSelectedSub = "";
	String strSubCategoryCode = "";
	
	private ListView mListView;
	private ListAdapter mAdapter = null;
	private ProgressDialog mProgressDialog;
	EditText et;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_dbyhs);
	
		api = new CallApiForGetXmlData("dbyhsCccrrncInfo/dbyhsCccrrncInfoYear");
        api.setmHandler(resultMainCategory);
        new Thread(api).start();	
        
        mListView = (ListView)findViewById(R.id.listView);
        mAdapter = new ListAdapter(this);
		mListView.setAdapter(mAdapter);
		
		//검색창
		et = (EditText)findViewById(R.id.editText1);
		et.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				//키보드에서 완료 눌렀을 때 제목으로 검색
				if(event.getAction()==KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
					InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(et.getWindowToken(),0);
					
					arrayMiddleCategory.clear();
					
					//서버로 검색요청
					api = new CallApiForGetXmlData("dbyhsCccrrncInfo/dbyhsCccrrncInfoList");
					api.appendParam("sText", et.getText().toString() );
					api.appendParam("sType", "sCntntsSj");
					api.appendParam("numOfRows", "100");
			        api.setmHandler(resultMiddleCategory);
			        new Thread(api).start();
					return true;
				}
				return false;
			}
		});
		
		//검색버튼
		Button btnSearch = (Button)findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(et.getWindowToken(),0);
				
				arrayMiddleCategory.clear();
				//서버로 검색요청
				api = new CallApiForGetXmlData("dbyhsCccrrncInfo/dbyhsCccrrncInfoList");
				api.appendParam("sText", et.getText().toString() );
				api.appendParam("sType", "sCntntsSj");
				api.appendParam("numOfRows", "100");
		        api.setmHandler(resultMiddleCategory);
		        new Thread(api).start();
			}
		});
		
		//연도선택
		btnChoice1 = (Button)findViewById(R.id.btnChoiceCategory1);
		btnChoice1.setTypeface(Typeface.createFromAsset(getAssets(), "mom.ttf"));
		btnChoice1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(nCurrentChoice == 1){
					ArrayList<String> arrayName = new ArrayList<String>();
					for (int i = 1; i < arrayMainCategory.size(); i++){
						CategoryData cd = arrayMainCategory.get(i);
						arrayName.add(cd.strCode);
					}
					
					ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(DbyhsActivity.this, android.R.layout.select_dialog_item, arrayName);
					AlertDialog.Builder dg = new AlertDialog.Builder(DbyhsActivity.this);
					dg.setTitle("연도선택");
					dg.setAdapter(listAdapter, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							CategoryData cd = arrayMainCategory.get(which+1);
							
							strSelectedMiddle = "";
							strSelectedSub = "";
							arrayMiddleCategory.clear();
							
							strSelectedMain = cd.strCode;
							btnChoice1.setText(cd.strName);
							//연도로 검색요청
							api = new CallApiForGetXmlData("dbyhsCccrrncInfo/dbyhsCccrrncInfoList");
							api.appendParam("sYear", cd.strCode);
							api.appendParam("numOfRows", "100");
					        api.setmHandler(resultMiddleCategory);
					        new Thread(api).start();
						}
					});
					dg.show();	
				}
			}
		});
		
		tvSelect1 = (TextView)findViewById(R.id.tvSelect1);
		tvSelect1.setTypeface(Typeface.createFromAsset(getAssets(), "pio.ttf"));
		tvSelect2 = (TextView)findViewById(R.id.tvSelect2);
		tvSelect2.setTypeface(Typeface.createFromAsset(getAssets(), "pio.ttf"));
	}
	//연도 데이터 얻어옴
	private Handler resultMainCategory = new Handler(){
		@Override 
		public void handleMessage(android.os.Message msg) {
			ArrayList<String> arrayName = getXmlDatas("//yearVal");
			ArrayList<String> arrayCode = getXmlDatas("//yearCode");
			for (int i = 0; i < arrayName.size(); i++){
				CategoryData cd = new CategoryData();

				cd.strName = arrayName.get(i);
				cd.strCode = arrayCode.get(i);
				arrayMainCategory.add(cd);
			}	
			if(arrayMainCategory.size() > 0){
				btnChoice1.setEnabled(true);
			}
		} 
	};
	//검색결과
	private Handler resultMiddleCategory = new Handler(){
		@Override 
		public void handleMessage(android.os.Message msg) {
			ArrayList<String> arrayName = getXmlDatas("//cntntsSj");
			ArrayList<String> arrayCode = getXmlDatas("//svcDt");
			ArrayList<String> arrayCode2 = getXmlDatas("//downFile");
			if(arrayName == null){
				AlertDialog.Builder alert = new AlertDialog.Builder(DbyhsActivity.this);
				alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				alert.setMessage("검색결과가 없습니다");
				alert.show();
			}else{
				for (int i = 0; i < arrayName.size(); i++){
					TechData cd = new TechData();
					cd.strName = arrayName.get(i);
					cd.strDate = arrayCode.get(i);
					cd.strUrl = arrayCode2.get(i);
					arrayMiddleCategory.add(cd);
				}	
			}
			if(arrayMiddleCategory.size() > 0){
				mAdapter.notifyDataSetChanged();
			}
		} 
	};
	
	//xml데이터 추출
	private ArrayList<String> getXmlDatas(String strExpression){
		ArrayList<String> arrReturn = null;
		Document document = api.doc;
		if(document != null){
			XPath xpath = XPathFactory.newInstance().newXPath();
			NodeList cols = null;
			try {
				cols = (NodeList)xpath.evaluate(strExpression, document, XPathConstants.NODESET);
			} catch (XPathExpressionException e) {
				e.printStackTrace();
			}
			if(cols != null && cols.getLength() > 0){
				arrReturn = new ArrayList<String>();
				for(int idx=0; idx<cols.getLength(); idx++ ){
					arrReturn.add(cols.item(idx).getTextContent());
				}	
			}
		}
		return arrReturn;
	}
	
	private class CategoryData{
		public String strName;
		public String strCode;
		public String strCode2;
	}
	private class TechData{
		public String strName;
		public String strDate;
		public String strUrl;
	}
	private class ListAdapter extends BaseAdapter{
		private Context context;
		private LayoutInflater inflater;

		public ListAdapter(Context c) {
			context = c;
			inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		@Override
		public int getCount() {
			return arrayMiddleCategory.size();
		}

		@Override
		public Object getItem(int position) {
			return arrayMiddleCategory.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		private class ViewHolder{
			TextView tvDate;
			TextView tvTitle;
			Button btnAction;
		}
		//리스트 처리
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final TechData td = arrayMiddleCategory.get(position);
			final ViewHolder holder;
			if(convertView == null){
				convertView = inflater.inflate(R.layout.adapter_default, null);
				holder = new ViewHolder();
				holder.tvDate = (TextView)convertView.findViewById(R.id.tvDate);
				holder.tvTitle = (TextView)convertView.findViewById(R.id.tvTitle);
				holder.btnAction = (Button)convertView.findViewById(R.id.btnAction);
				holder.btnAction.setVisibility(View.VISIBLE);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder)convertView.getTag();
			}
			holder.tvTitle.setText(td.strName);
			holder.btnAction.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent i = new Intent(Intent.ACTION_VIEW); 
					Uri u = Uri.parse(td.strUrl); 
					i.setData(u); 
					startActivity(i);
				}
			});	
			
			return convertView;
		}
	}
}
