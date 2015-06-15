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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.*;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class TechActivity extends Activity {
	ArrayList<CategoryData> arrayMainCategory = new ArrayList<CategoryData>();
	ArrayList<CategoryData> arrayMiddleCategory = new ArrayList<CategoryData>();
	ArrayList<CategoryData> arraySubCategory = new ArrayList<CategoryData>();
	ArrayList<CategoryData> arrayMainTech = new ArrayList<CategoryData>();
	ArrayList<CategoryData> arraySubTech = new ArrayList<CategoryData>();
	ArrayList<TechData> arrayTechInfo = new ArrayList<TechData>();
	
	CallApiForGetXmlData api;
	
	LinearLayout layoutCrop, layoutTech;
	TextView tvSelect1, tvSelect2;
	TextView tvSelect3, tvSelect4;
	Button btnChoice1, btnChoice2;//, btnChoice3;
	Button btnChoice1OnTech, btnChoice2OnTech, btnChoice3OnTech;
	Button btnSelectCrop, btnSelectTech ;
	
	
	int nCurrentChoice = 1;//1>작물선택 2>기술선택
	String strSelectedMain = "", strSelectedMiddle = "", strSelectedSub = "";
	String strSubCategoryCode = "";
	
	private ListView mListView, mListViewOnTech;
	private ListAdapter mAdapter = null;
	private ListAdapter2 mAdapterOnTech = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_tech);
	
		api = new CallApiForGetXmlData("cropTechInfo/mainCategoryList");
        api.setmHandler(resultMainCategory);
        new Thread(api).start();	
        
        layoutCrop = (LinearLayout)findViewById(R.id.layoutCrop);
        layoutTech = (LinearLayout)findViewById(R.id.layoutTech);
        mListViewOnTech = (ListView)findViewById(R.id.listViewOnTech);
        mListView = (ListView)findViewById(R.id.listView);
        mAdapter = new ListAdapter(this);
		mListView.setAdapter(mAdapter);
		mAdapterOnTech = new ListAdapter2(this);
		mListViewOnTech.setAdapter(mAdapterOnTech);
        
		//품목 대분류 선택버튼
		btnChoice1 = (Button)findViewById(R.id.btnChoiceCategory1);
		btnChoice1.setTypeface(Typeface.createFromAsset(getAssets(), "mom.ttf"));
		btnChoice1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(nCurrentChoice == 1){
					ArrayList<String> arrayName = new ArrayList<String>();
					for (int i = 0; i < arrayMainCategory.size(); i++){
						CategoryData cd = arrayMainCategory.get(i);
						arrayName.add(cd.strName);
					}
					
					ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(TechActivity.this, android.R.layout.select_dialog_item, arrayName);
					AlertDialog.Builder dg = new AlertDialog.Builder(TechActivity.this);
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
							//중분류 요청
							api = new CallApiForGetXmlData("cropTechInfo/middleCategoryList");
							api.appendParam("mainCategoryCode", cd.strCode);
					        api.setmHandler(resultMiddleCategory);
					        new Thread(api).start();
						}
					});
					dg.show();	
				}
			}
		});
		
	
		//품목 중분류 버튼
		btnChoice2 = (Button)findViewById(R.id.btnChoiceCategory2);
		btnChoice2.setTypeface(Typeface.createFromAsset(getAssets(), "mom.ttf"));
		btnChoice2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(nCurrentChoice == 1){
					ArrayList<String> arrayName = new ArrayList<String>();
					for (int i = 0; i < arrayMiddleCategory.size(); i++){
						CategoryData cd = arrayMiddleCategory.get(i);
						arrayName.add(cd.strName);
					}
					
					ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(TechActivity.this, android.R.layout.select_dialog_item, arrayName);
					AlertDialog.Builder dg = new AlertDialog.Builder(TechActivity.this);
					dg.setTitle("작물선택1");
					dg.setAdapter(listAdapter, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							CategoryData cd = arrayMiddleCategory.get(which);
							arraySubCategory.clear();
							
							strSelectedMiddle = cd.strCode;
							btnChoice2.setText(cd.strName);
							//소분류 요청
							api = new CallApiForGetXmlData("cropTechInfo/subCategoryList");
							api.appendParam("middleCategoryCode", cd.strCode);
					        api.setmHandler(resultSubCategory);
					        new Thread(api).start();
						}
					});
					dg.show();	
				}
			}
		});
		//기술코드 대분류버튼
		btnChoice1OnTech = (Button)findViewById(R.id.btnChoiceCategory1OnTech);
		btnChoice1OnTech.setTypeface(Typeface.createFromAsset(getAssets(), "mom.ttf"));
		btnChoice1OnTech.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(nCurrentChoice == 2){
					ArrayList<String> arrayName = new ArrayList<String>();
					for (int i = 0; i < arrayMainTech.size(); i++){
						CategoryData cd = arrayMainTech.get(i);
						arrayName.add(cd.strName);
					}
					
					ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(TechActivity.this, android.R.layout.select_dialog_item, arrayName);
					AlertDialog.Builder dg = new AlertDialog.Builder(TechActivity.this);
					dg.setTitle("기술코드(대분류)");
					dg.setAdapter(listAdapter, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							CategoryData cd = arrayMainTech.get(which);

							btnChoice1OnTech.setText(cd.strName);
							
							arraySubTech.clear();
							btnChoice2OnTech.setText("선택하세요");
							
							if(cd.strName.equals("동영상")){//동영상 선택 시 서버 요청
								arrayTechInfo.clear();
								mAdapterOnTech.notifyDataSetChanged();
								btnChoice2OnTech.setText("선택하세요");
								
								api = new CallApiForGetXmlData("cropTechInfo/videoList");
								api.appendParam("subCategoryCode", strSelectedSub);
								api.appendParam("numOfRows", "100");
						        api.setmHandler(resultTechInfoList3);
						        new Thread(api).start();								
							}else{//나머지 선택 시 서버 요청
								api = new CallApiForGetXmlData("cropTechInfo/subTechList");
								api.appendParam("subCategoryCode", strSelectedSub);
								api.appendParam("mainTechCode", cd.strCode);
						        api.setmHandler(resultSubTech);
						        new Thread(api).start();	
							}
						}
					});
					dg.show();	
				}
			}
		});
		//기술코드 소분류 버튼
		btnChoice2OnTech = (Button)findViewById(R.id.btnChoiceCategory2OnTech);
		btnChoice2OnTech.setTypeface(Typeface.createFromAsset(getAssets(), "mom.ttf"));
		btnChoice2OnTech.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(nCurrentChoice == 2){
					ArrayList<String> arrayName = new ArrayList<String>();
					for (int i = 0; i < arraySubTech.size(); i++){
						CategoryData cd = arraySubTech.get(i);
						arrayName.add(cd.strName);
					}
					
					ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(TechActivity.this, android.R.layout.select_dialog_item, arrayName);
					AlertDialog.Builder dg = new AlertDialog.Builder(TechActivity.this);
					dg.setTitle("기술코드(소분류)");
					dg.setAdapter(listAdapter, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							arrayTechInfo.clear();
							mAdapterOnTech.notifyDataSetChanged();
							
							CategoryData cd = arraySubTech.get(which);
							btnChoice2OnTech.setText(cd.strName);
							//선택한 값에 따라 서버에 요청
							if(cd.strName.equals("품종.대목")){
								api = new CallApiForGetXmlData("cropTechInfo/varietyList");
								api.setmHandler(resultTechInfoList2);
							}else{
								api = new CallApiForGetXmlData("cropTechInfo/techInfoList");
								api.setmHandler(resultTechInfoList1);
								api.appendParam("subTechCode", cd.strCode);
							}
							api.appendParam("subCategoryCode", strSelectedSub);
							api.appendParam("numOfRows", "100");
					        new Thread(api).start();
						}
					});
					dg.show();	
				}
			}
		});
		//작물선택 탭
		btnSelectCrop = (Button)findViewById(R.id.btnSelectCrop);
		btnSelectCrop.setTypeface(Typeface.createFromAsset(getAssets(), "hoon.ttf"));
		
		btnSelectCrop.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				nCurrentChoice = 1;
				UpdateUI();
			}
		});
		
		//기술선택 탭
		
		btnSelectTech = (Button)findViewById(R.id.btnSelectTech);
		btnSelectTech.setTypeface(Typeface.createFromAsset(getAssets(), "hoon.ttf"));
		btnSelectTech.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				nCurrentChoice = 2;
				UpdateUI();
			}
			
		});
		
		tvSelect1 = (TextView)findViewById(R.id.tvSelect1);
		tvSelect1.setTypeface(Typeface.createFromAsset(getAssets(), "pio.ttf"));
		tvSelect2 = (TextView)findViewById(R.id.tvSelect2);
		tvSelect2.setTypeface(Typeface.createFromAsset(getAssets(), "pio.ttf"));
		
		tvSelect3 = (TextView)findViewById(R.id.tvSelect1OnTech);
		tvSelect3.setTypeface(Typeface.createFromAsset(getAssets(), "pio.ttf"));
		tvSelect4 = (TextView)findViewById(R.id.tvSelect2OnTech);
		tvSelect4.setTypeface(Typeface.createFromAsset(getAssets(), "pio.ttf"));
	
		
	}
	
	//작물선택 대분류 데이터
	private Handler resultMainCategory = new Handler(){
		@Override 
		public void handleMessage(android.os.Message msg) {
			ArrayList<String> arrayName = getXmlDatas("//mainCategoryNm");
			ArrayList<String> arrayCode = getXmlDatas("//mainCategoryCode");
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
	//작물선택 중분류 데이터
	private Handler resultMiddleCategory = new Handler(){
		@Override 
		public void handleMessage(android.os.Message msg) {
			ArrayList<String> arrayName = getXmlDatas("//middleCategoryNm");
			ArrayList<String> arrayCode = getXmlDatas("//middleCategoryCode");
			for (int i = 0; i < arrayName.size(); i++){
				CategoryData cd = new CategoryData();
				cd.strName = arrayName.get(i);
				cd.strCode = arrayCode.get(i);
				arrayMiddleCategory.add(cd);
			}
		} 
	};
	//작물선택 소분류 데이터
	private Handler resultSubCategory = new Handler(){
		@Override 
		public void handleMessage(android.os.Message msg) {
			ArrayList<String> arrayName = getXmlDatas("//subCategoryNm");
			ArrayList<String> arrayCode = getXmlDatas("//subCategoryCode");
			for (int i = 0; i < arrayName.size(); i++){
				CategoryData cd = new CategoryData();
				cd.strName = arrayName.get(i);
				cd.strCode = arrayCode.get(i);
				arraySubCategory.add(cd);
			}
			
			if(arraySubCategory.size() > 0){
				mAdapter.notifyDataSetChanged();
			}
		}
	};
	//기술선택 대분류 데이터
	private Handler resultMainTech = new Handler(){
		@Override 
		public void handleMessage(android.os.Message msg) {
			ArrayList<String> arrayName = getXmlDatas("//mainTechNm");
			ArrayList<String> arrayCode = getXmlDatas("//mainTechCode");
			for (int i = 0; i < arrayName.size(); i++){
				CategoryData cd = new CategoryData();
				cd.strName = arrayName.get(i);
				cd.strCode = arrayCode.get(i);
				arrayMainTech.add(cd);
			}
			arrayTechInfo.clear();
			arraySubTech.clear();
			mAdapterOnTech.notifyDataSetChanged();
			btnChoice1OnTech.setText("선택하세요");
			btnChoice2OnTech.setText("선택하세요");
	
			//작물을 다 선택 했으므로 기술선택 UI로 변경
			nCurrentChoice = 2;
			UpdateUI();
		} 
	};
	//기술선택 소분류 데이터
	private Handler resultSubTech = new Handler(){
		@Override 
		public void handleMessage(android.os.Message msg) {
			ArrayList<String> arrayName = getXmlDatas("//subTechNm");
			ArrayList<String> arrayCode = getXmlDatas("//subTechCode");
			for (int i = 0; i < arrayName.size(); i++){
				CategoryData cd = new CategoryData();
				cd.strName = arrayName.get(i);
				cd.strCode = arrayCode.get(i);
				arraySubTech.add(cd);
			}
		} 
	};
	//기술 정보
	private Handler resultTechInfoList1 = new Handler(){
		@Override 
		public void handleMessage(android.os.Message msg) {
			ArrayList<String> arrayName = getXmlDatas("//techNm");
			ArrayList<String> arrayDate = getXmlDatas("//regDt");
			ArrayList<String> arrayUrl = getXmlDatas("//fileDownUrl");
			for (int i = 0; i < arrayName.size(); i++){
				TechData td = new TechData();
				td.strName = arrayName.get(i);
				td.strDate = arrayDate.get(i);
				td.strUrl = arrayUrl.get(i);
				arrayTechInfo.add(td);
			}
			if(arrayTechInfo.size() > 0){
				mAdapterOnTech.notifyDataSetChanged();
			}
		} 
	};
	//품종 정보
	private Handler resultTechInfoList2 = new Handler(){
		@Override 
		public void handleMessage(android.os.Message msg) {
			ArrayList<String> arrayName = getXmlDatas("//varietyNm");
			ArrayList<String> arrayUrl = getXmlDatas("//atchFileLink");
			for (int i = 0; i < arrayName.size(); i++){
				TechData td = new TechData();
				td.strName = arrayName.get(i);
				td.strUrl = arrayUrl.get(i);
				arrayTechInfo.add(td);
			}
			if(arrayTechInfo.size() > 0){
				mAdapterOnTech.notifyDataSetChanged();
			}
		} 
	};
	//동영상 정보
	private Handler resultTechInfoList3 = new Handler(){
		@Override 
		public void handleMessage(android.os.Message msg) {
			ArrayList<String> arrayName = getXmlDatas("//videoTitle");
			ArrayList<String> arrayUrl = getXmlDatas("//videoLink");
			for (int i = 0; i < arrayName.size(); i++){
				TechData td = new TechData();
				td.strName = arrayName.get(i);
				td.strUrl = arrayUrl.get(i);
				arrayTechInfo.add(td);
			}
			if(arrayTechInfo.size() > 0){
				mAdapterOnTech.notifyDataSetChanged();
			}
		} 
	};
	//선택한 탭에 따라 ui변경
	private void UpdateUI(){
		if(nCurrentChoice == 1){
			layoutCrop.setVisibility(View.VISIBLE);
			layoutTech.setVisibility(View.GONE);
			tvSelect1.setText("품목선택");
			tvSelect2.setText("작물선택1");
			
			btnSelectCrop.setBackgroundColor(Color.parseColor("#76A6FA"));
			btnSelectTech.setBackgroundColor(Color.parseColor("#8876A6FA"));
		}else{
			layoutCrop.setVisibility(View.GONE);
			layoutTech.setVisibility(View.VISIBLE);
			
			tvSelect1.setText("기술선택");
			tvSelect2.setText("기술정보");
			
			btnSelectCrop.setBackgroundColor(Color.parseColor("#8876A6FA"));
			btnSelectTech.setBackgroundColor(Color.parseColor("#76A6FA"));
		}
	}
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
			return arraySubCategory.size();
		}

		@Override
		public Object getItem(int position) {
			return arraySubCategory.get(position);
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
		//작물정보 리스트 구성
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final CategoryData cd = arraySubCategory.get(position);
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
					strSelectedSub = cd.strCode;
					
					arrayMainTech.clear();
					
					api = new CallApiForGetXmlData("cropTechInfo/mainTechList");
					api.appendParam("subCategoryCode", cd.strCode);
			        api.setmHandler(resultMainTech);
			        new Thread(api).start();
				}
			});
			
			return convertView;
		}
	}
	
	private class ListAdapter2 extends BaseAdapter{
		private Context context;
		private LayoutInflater inflater;

		public ListAdapter2(Context c) {
			context = c;
			inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		@Override
		public int getCount() {
			return arrayTechInfo.size();
		}

		@Override
		public Object getItem(int position) {
			return arrayTechInfo.get(position);
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
		//기술정보 리스트 구성
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final TechData td = arrayTechInfo.get(position);
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

			holder.tvDate.setText(td.strDate);
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
