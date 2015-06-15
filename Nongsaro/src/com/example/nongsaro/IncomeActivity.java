package com.example.nongsaro;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class IncomeActivity extends Activity {
	ArrayList<CategoryData> arrayMainCategory = new ArrayList<CategoryData>();
	ArrayList<CategoryData> arrayMiddleCategory = new ArrayList<CategoryData>();
	ArrayList<CategoryData> arraySubCategory = new ArrayList<CategoryData>();
	ArrayList<CategoryData> arrayDetailCategory = new ArrayList<CategoryData>();
	ArrayList<CategoryData> arrayMainTech = new ArrayList<CategoryData>();
	ArrayList<CategoryData> arraySubTech = new ArrayList<CategoryData>();
	ArrayList<TechData> arrayTechInfo = new ArrayList<TechData>();

	CallApiForGetXmlData api;

	LinearLayout layoutCrop, layoutTech;
	TextView tvSelect1, tvSelect2, tvSelect3; 
	Button btnChoice1, btnChoice2, btnChoice3;
	Button btnChoice1OnTech, btnChoice2OnTech, btnChoice3OnTech;
	Button btnSelectCrop, btnSelectTech;
	String strSelectedMain = "", strSelectedMiddle = "", strSelectedSub = "";
	String strSubCategoryCode = "";

	private ListView mListView;
	private ListAdapter mAdapter = null;
	private ProgressDialog mProgressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_income);
		//품목 가져오기
		api = new CallApiForGetXmlData("farmPrdtIncome/farmPrdtIncomeMtchprList");
		api.setmHandler(resultMainCategory);
		new Thread(api).start();	

		layoutCrop = (LinearLayout)findViewById(R.id.layoutCrop);
		layoutTech = (LinearLayout)findViewById(R.id.layoutTech);
		mListView = (ListView)findViewById(R.id.listView);
		mAdapter = new ListAdapter(this);
		mListView.setAdapter(mAdapter);
		
		//품목선택버튼
		btnChoice1 = (Button)findViewById(R.id.btnChoiceCategory1);
		btnChoice1.setTypeface(Typeface.createFromAsset(getAssets(), "mom.ttf"));
		btnChoice1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ArrayList<String> arrayName = new ArrayList<String>();
				for (int i = 0; i < arrayMainCategory.size(); i++){
					CategoryData cd = arrayMainCategory.get(i);
					arrayName.add(cd.strName);
				}

				ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(IncomeActivity.this, android.R.layout.select_dialog_item, arrayName);
				AlertDialog.Builder dg = new AlertDialog.Builder(IncomeActivity.this);
				dg.setTitle("품목선택");
				dg.setAdapter(listAdapter, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						CategoryData cd = arrayMainCategory.get(which);

						strSelectedMiddle = "";
						strSelectedSub = "";
						arrayMiddleCategory.clear();
						arraySubCategory.clear();

						strSelectedMain = cd.strCode;
						btnChoice1.setText(cd.strName);
						btnChoice2.setText("선택하세요");
						//연도 요청
						api = new CallApiForGetXmlData("farmPrdtIncome/farmPrdtIncomeYearList");
						api.appendParam("svcCode", cd.strCode);
						api.setmHandler(resultMiddleCategory);
						new Thread(api).start();
					}
				});
				dg.show();	
			}
		});

		//연도 버튼
		btnChoice2 = (Button)findViewById(R.id.btnChoiceCategory2);
		btnChoice2.setTypeface(Typeface.createFromAsset(getAssets(), "mom.ttf"));
		btnChoice2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ArrayList<String> arrayName = new ArrayList<String>();
				for (int i = 0; i < arrayMiddleCategory.size(); i++){
					CategoryData cd = arrayMiddleCategory.get(i);
					arrayName.add(cd.strName);
				}

				ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(IncomeActivity.this, android.R.layout.select_dialog_item, arrayName);
				AlertDialog.Builder dg = new AlertDialog.Builder(IncomeActivity.this);
				dg.setTitle("연도선택");
				dg.setAdapter(listAdapter, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						CategoryData cd = arrayMiddleCategory.get(which);
						arraySubCategory.clear();

						strSelectedMiddle = cd.strCode;
						btnChoice2.setText(cd.strName);
						//지역 요청
						api = new CallApiForGetXmlData("farmPrdtIncome/farmPrdtIncomeSidoList");
						api.appendParam("year", cd.strCode);
						api.appendParam("svcCode", strSelectedMain);
						api.setmHandler(resultSubCategory);
						new Thread(api).start();
					}
				});
				dg.show();	
			}
		});

		//지역 선택 버튼
		btnChoice3 = (Button)findViewById(R.id.btnChoiceCategory3);
		btnChoice3.setTypeface(Typeface.createFromAsset(getAssets(), "mom.ttf"));
		btnChoice3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ArrayList<String> arrayName = new ArrayList<String>();
				for (int i = 0; i < arraySubCategory.size(); i++){
					CategoryData cd = arraySubCategory.get(i);
					arrayName.add(cd.strName);
				}

				ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(IncomeActivity.this, android.R.layout.select_dialog_item, arrayName);
				AlertDialog.Builder dg = new AlertDialog.Builder(IncomeActivity.this);
				dg.setTitle("지역선택");
				dg.setAdapter(listAdapter, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						CategoryData cd = arraySubCategory.get(which);

						arrayDetailCategory.clear();
						strSelectedSub = cd.strCode;
						btnChoice3.setText(cd.strName);
						//리스트 요청
						api = new CallApiForGetXmlData("farmPrdtIncome/farmPrdtIncomeList");
						api.appendParam("year", strSelectedMiddle);
						api.appendParam("atptCode", cd.strCode);
						api.appendParam("svcCode", strSelectedMain);
						api.setmHandler(resultDetailCategory);
						new Thread(api).start();
					}
				});
				dg.show();	
			}
		});

		tvSelect1 = (TextView)findViewById(R.id.tvSelect1);
		tvSelect1.setTypeface(Typeface.createFromAsset(getAssets(), "pio.ttf"));
		
		tvSelect2 = (TextView)findViewById(R.id.tvSelect2);
		tvSelect2.setTypeface(Typeface.createFromAsset(getAssets(), "pio.ttf"));
		tvSelect3 = (TextView)findViewById(R.id.tvSelect3);
		tvSelect3.setTypeface(Typeface.createFromAsset(getAssets(), "pio.ttf"));
	}

	//품목요청결과
	private Handler resultMainCategory = new Handler(){
		@Override 
		public void handleMessage(android.os.Message msg) {
			ArrayList<String> arrayName = getXmlDatas("//codeNm");
			ArrayList<String> arrayCode = getXmlDatas("//code");
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
	//연도요청결과
	private Handler resultMiddleCategory = new Handler(){
		@Override 
		public void handleMessage(android.os.Message msg) {
			ArrayList<String> arrayName = getXmlDatas("//year");
			ArrayList<String> arrayCode = getXmlDatas("//year");
			for (int i = 0; i < arrayName.size(); i++){
				CategoryData cd = new CategoryData();
				cd.strName = arrayName.get(i);
				cd.strCode = arrayCode.get(i);
				arrayMiddleCategory.add(cd);
			}
		} 
	};
	//지역요청결과
	private Handler resultSubCategory = new Handler(){
		@Override 
		public void handleMessage(android.os.Message msg) {
			ArrayList<String> arrayName = getXmlDatas("//atptNm");
			ArrayList<String> arrayCode = getXmlDatas("//atptCode");
			for (int i = 0; i < arrayName.size(); i++){
				CategoryData cd = new CategoryData();
				cd.strName = arrayName.get(i);
				cd.strCode = arrayCode.get(i);
				arraySubCategory.add(cd);
			}
		}
	};
	//리스트 데이터 요청결과
	private Handler resultDetailCategory = new Handler(){
		@Override 
		public void handleMessage(android.os.Message msg) {
			ArrayList<String> arrayName = getXmlDatas("//eqpNm");
			ArrayList<String> arrayCode = getXmlDatas("//eqpCode");
			for (int i = 0; i < arrayName.size(); i++){
				CategoryData cd = new CategoryData();
				cd.strName = arrayName.get(i);
				cd.strCode = arrayCode.get(i);
				arrayDetailCategory.add(cd);
			}

			//데이터가 있으면 리스트 구성
			if(arrayDetailCategory.size() > 0){
				mAdapter.notifyDataSetChanged();
			}
		}
	};

	//리스트 아이템 선택 시 데이터처리
	private Handler resultMainTech = new Handler(){
		@Override 
		public void handleMessage(android.os.Message msg) {
			ArrayList<String> arrayName = getXmlDatas("//listNm");
			ArrayList<String> arrayCode = getXmlDatas("//incomeTotAmount");
			ArrayList<String> arrayCode2 = getXmlDatas("//incomeAmount");
			for (int i = 0; i < arrayName.size(); i++){
				CategoryData cd = new CategoryData();
				cd.strName = arrayName.get(i);
				cd.strCode = arrayCode.get(i);
				cd.strCode2 = arrayCode2.get(i);
				arrayMainTech.add(cd);
			}

			AlertDialog.Builder alert = new AlertDialog.Builder(IncomeActivity.this);
			alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			String str1 = "", str2 = "", str3 = "", str4 = "";
			for(int i = 0; i < arrayMainTech.size(); i++){
				CategoryData cd = arrayMainTech.get(i);
				if(cd.strName.equals("주산물가액"))
					str1 = cd.strCode2;
				else if(cd.strName.equals("소득"))
					str2 = cd.strCode;
				else if(cd.strName.equals("부가가치"))
					str3 = cd.strCode;
				else if(cd.strName.equals("소득율(%)"))
					str4 = cd.strCode;
			}
			mProgressDialog.dismiss();

			alert.setMessage(String.format("단가 : %s원\n소득 : %s원\n부가가치 : %s원\n소득율 : %s%%", str1, str2, str3, str4));
			alert.show();
		} 
	};

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
			return arrayDetailCategory.size();
		}

		@Override
		public Object getItem(int position) {
			return arrayDetailCategory.get(position);
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
		//리스트 구성
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final CategoryData cd = arrayDetailCategory.get(position);
			final ViewHolder holder;
			if(convertView == null){
				convertView = inflater.inflate(R.layout.adapter_default, null);
				holder = new ViewHolder();
				holder.tvTitle = (TextView)convertView.findViewById(R.id.tvTitle);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder)convertView.getTag();
			}

			holder.tvTitle.setText(cd.strName);
			convertView.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					arrayMainTech.clear();

					api = new CallApiForGetXmlData("farmPrdtIncome/farmPrdtIncomeDetailList");
					api.appendParam("year", strSelectedMiddle);
					api.appendParam("atptCode", strSelectedSub);
					api.appendParam("svcCode", strSelectedMain);
					api.appendParam("eqpCode", cd.strCode);
					api.setmHandler(resultMainTech);
					new Thread(api).start();
					mProgressDialog = ProgressDialog.show(IncomeActivity.this,"", "잠시만 기다려 주세요.",true);
				}
			});

			return convertView;
		}
	}
}
