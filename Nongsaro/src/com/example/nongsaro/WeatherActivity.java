package com.example.mapweather;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class WeatherActivity extends Activity {

    private int mX, mY;
    ListView w_list;//하단 날시 리스트 뷰

    ArrayList<WeatherDataTime> pDataList = new ArrayList<WeatherDataTime>(); //시간별 날씨 데이터 저장 리스트
    ArrayList<WeatherDataDays> dDataList = new ArrayList<WeatherDataDays>(); //주간날씨 데이터 저장 리스트
    ArrayList<WeatherDataDaysAll> aDataList = new ArrayList<WeatherDataDaysAll>(); //전국 날씨 데이터 저장 리스트

    //시간별 날씨는 기상청 rss  http://www.kma.go.kr/wid/queryDFSRSS.jsp?이용
    //주간별 날씨는 공공데이터포털 http://newsky2.kma.go.kr/service/MiddleFrcstInfoService/getMiddleLandWeather 이용
    //전국 날씨는 기상청 rss   http://www.kma.go.kr/weather/forecast/mid-term-rss3.jsp 이용

    String resionCodeArray[] = { "4200000000", "4100000000", "4800000000",
            "4700000000", "2900000000", "2700000000", "3000000000",
            "2600000000", "1100000000", "3600000000", "3100000000",
            "2800000000", "4600000000", "4500000000", "5000000000",
            "4400000000", "4300000000" };  //시간별 날씨 지역코드
    
    String gangwonCodeArray[] = { "4215000000", "4282000000", "4217000000",
            "4223000000", "4221000000", "4280000000", "4283000000",
            "4275000000", "4213000000", "4281000000", "4277000000",
            "4278000000", "4211000000", "4219000000", "4276000000",
            "4272000000", "4279000000", "4273000000" };
    
    String resionArray[] = { "강원도", "경기도", "경상남도", "경상북도", "광주광역시", "대구광역시",
            "대전광역시", "부산광역시", "서울특별시", "세종특별자치시", "울산광역시", "인천광역시", "전라남도",
            "전라북도", "제주특별자치도", "충청남도", "충청북도" }; //시간별 날씨 지역 텍스트
    
    String gangwonArray[] = { "강릉시", "고성군", "동해시", "삼척시", "속초시", "양구군",
            "양양군", "영월군", "원주시", "인제군", "정선군", "철원군", "춘천시",
            "태백시", "평창군", "홍천군", "화천군","횡성군" }; //시간별 날씨 지역 텍스트
    
    String resionArray1[] = { "서울,인천,경기도", "강원도,영서", "강원도,영동 ", "대전,세종,충청남도",
            "충청북도", "광주,전라남도", "전라북도", "대구 경상북도", "부산 ,울산 ,경상남도", "제주도" }; //주간날씨 지역 텍스트

    String resionCodeArray1[] = { "11B00000", "11D10000", "11D20000",
            "11C20000", "11C10000", "11F20000", "11F10000", "11H10000",
            "11H20000", "11G0000" }; //주간날씨 지역 코드

    String dayArrary[] = { "오늘", "내일", "모레" }; //시간별 날씨의 날자

    String resionCode = "1100000000";//시간별 날씨 디폴트 코드(서울)
    String resionCode1 = "11B00000"; //주간날시 디폴트 코드(서울,인천,경기도)


    ArrayList<String> middleWeaherList = new ArrayList<String>(); //주간날씨 리스트 임시 저장 리스트

    String middleTxt[] = {"10일 후 ", "3일 후 오전", "3일 후 오후", "4일 후 오전", "4일 후 오후",
            "5일 후 오전", "5일 후 오후", "6일 후 오전", "6일 후 오후", "7일 후 오전", "7일 후 오후", "8일 후",
            "9일 후"}; //주간날씨 텍스트

    TextView resion_id; // 지역선택 텍스트 뷰

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        Intent intent = getIntent();
        mX = intent.getIntExtra("Weather_X", 0);
        mY = intent.getIntExtra("Weather_Y", 0);

        setButton(); //탭 버튼 이동 세팅
        new getWeatherTask().execute();//시간별 날씨 불러온다

    }

    private void setButton() {
        final LinearLayout resion_view = (LinearLayout) findViewById(R.id.resion_view); // 지역선택
        // 텍스트
        // 뷰

        resion_id = (TextView) findViewById(R.id.resion_id); // 지역선택 텍스트 뷰
        resion_id.setText("지역 : 서울특별시 ");
        resion_id.setOnClickListener(new View.OnClickListener() {//지역 선택시 AlertDialog로 띄워서 지역선택하여 resionCode저장

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        WeatherActivity.this);
                builder.setTitle("지역선택");
                builder.setItems(resionArray,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int pos) {
                            	resionCode = resionCodeArray[pos];
                            
                            	if(resionCode == "4200000000"){
                            		AlertDialog.Builder innBuilder= new AlertDialog.Builder(
                                            WeatherActivity.this);
                                	innBuilder.setTitle("세부선택");
                                	innBuilder.setItems(gangwonArray, new DialogInterface.OnClickListener(){
                                		public void onClick(DialogInterface dialog, int pos) {
                                			
                                			resion_id.setText("지역 : "
                            						+ gangwonArray[pos].toString());
                            				resionCode = gangwonCodeArray[pos];
                            				new getWeatherTask().execute();
                            				dialog.dismiss();
                                		}
                                	});
                                	AlertDialog alert = innBuilder.create();
                                	innBuilder.show();
                            	}
    		
                            
                            }
                              
                            
                           
                            
                                            
                            
                        });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });
       
        	
       
        
        final Button time_view = (Button) findViewById(R.id.time_view); //시간별날씨 탭 이미지
        final Button day_view = (Button) findViewById(R.id.day_view);//주간별날씨 탭 이미지
        //final Button all_view = (Button) findViewById(R.id.all_view);//전국별날씨 탭 이미지

        time_view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {// 시간별 날씨보기
                resion_id.setText("지역 : 서울특별시 ");
                resion_id.setOnClickListener(null);
                resionCode="1100000000";//지역코드 서울로 초기화

                resion_id.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                WeatherActivity.this);
                        builder.setTitle("지역선택");
                        builder.setItems(resionArray,
                        		new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int pos) {
                            	resionCode = resionCodeArray[pos];
                            
                            	if(resionCode == "4200000000"){
                            		AlertDialog.Builder innBuilder= new AlertDialog.Builder(
                                            WeatherActivity.this);
                                	innBuilder.setTitle("세부선택");
                                	innBuilder.setItems(gangwonArray, new DialogInterface.OnClickListener(){
                                		public void onClick(DialogInterface dialog, int pos) {
                                			
                                			resion_id.setText("지역 : "
                            						+ gangwonArray[pos].toString());
                            				resionCode = gangwonCodeArray[pos];
                            				new getWeatherTask().execute();
                            				dialog.dismiss();
                                		}
                                	});
                                	AlertDialog alert = innBuilder.create();
                                	innBuilder.show();
                            	}
    		
                            
                            }
  
                        });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });

                resion_view.setVisibility(View.VISIBLE);
                time_view.setBackgroundColor(Color.parseColor("#76A6FA"));//이미지 배경 바꿈
                day_view.setBackgroundColor(Color.parseColor("#8876A6FA"));
               // all_view.setBackgroundColor(Color.parseColor("#8876A6FA"));

                new getWeatherTask().execute();//시간별 날씨 데이터 다시 불러옮
            }
        });

        day_view.setOnClickListener(new View.OnClickListener() {// 주간 날씨 보기

            @Override
            public void onClick(View v) {
                resion_view.setVisibility(View.VISIBLE);
                resion_id.setText("서울,인천,경기도");
                resion_id.setOnClickListener(null);
                resionCode1="11B00000"; //지역코드 서울,인천,경기도 초기화

                resion_id.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                WeatherActivity.this);
                        builder.setTitle("지역선택");
                        builder.setItems(resionArray1,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int pos) {
                                        dialog.dismiss();
                                        resion_id.setText(resionArray1[pos].toString());
                                        resionCode1 = resionCodeArray1[pos];
                                        new getWeatherTaskDay().execute();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();

                    }
                });

                time_view.setBackgroundColor(Color.parseColor("#8876A6FA"));//이미지 바꿔줌
                day_view.setBackgroundColor(Color.parseColor("#76A6FA"));
               // all_view.setBackgroundColor(Color.parseColor("#8876A6FA"));

                new getWeatherTaskDay().execute();
            }
        });

        //all_view.setOnClickListener(new View.OnClickListener() {// 전국 날씨 보기

        /*@Override
            public void onClick(View v) {
                resion_view.setVisibility(View.GONE);
                time_view.setBackgroundColor(Color.parseColor("#8876A6FA"));
                day_view.setBackgroundColor(Color.parseColor("#8876A6FA"));
                all_view.setBackgroundColor(Color.parseColor("#76A6FA"));

                new getWeatherTaskAll().execute();//전국날씨
           /
        }); 
        */

    }

    private class getWeatherTask extends AsyncTask<String, Void, Void> {

        ProgressDialog loagindDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loagindDialog = ProgressDialog.show(WeatherActivity.this,
                    "날씨 정보 가져오는 중..", "please wait....", true, true, null);
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                getList();
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        }

        public String getNowDateDate() {//오늘날짜
            Calendar calendar = Calendar.getInstance();
            Date date = calendar.getTime();
            return new SimpleDateFormat("yyyyMMdd").format(date);
        }

        public String getYesterDay() {//어제 날짜
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);
            Date date = calendar.getTime();
            return new SimpleDateFormat("yyyyMMdd").format(date);
        }

        public String getNowDateTime() {//오늘시간
            Calendar calendar = Calendar.getInstance();
            Date date = calendar.getTime();
            return new SimpleDateFormat("HHmm").format(date);
        }

        private void getList() throws UnsupportedEncodingException, IOException {
            pDataList.clear();

            URL url = new URL("http://www.kma.go.kr/wid/queryDFSRSS.jsp?zone="+ resionCode); // URL
            /*String urlStr = "http://newsky2.kma.go.kr/service/SecndSrtpdFrcstInfoService/ForecastGrib?ServiceKey=kYn%2BODkxhnELMkuYn7%2F%2BpnTjZT0CusSP%2BQUtTIaocJVfglrPZ6nxOq4hJtOUNrExwWSwWyhuF7lUDdxTId%2Fj1Q%3D%3D";

            String nowTime = getNowDateTime();
            String nowTimeStr = "";
            String nowData = "";

            if (Integer.parseInt(nowTime) >= 601) {
                // 기준시간으로 한다
                nowTimeStr = "0600";
                nowData = "&base_date=" + getNowDateDate() + "&base_time=" + nowTimeStr;
            } else {
                nowTimeStr = "1800"; 
                nowData = "&base_date=" + getNowDateDate() + "&base_time=" + nowTimeStr;
            }

            HttpGet httpGet = new HttpGet(urlStr + nowData + "&nx=" + mX + "&ny=" + mY + "&pageNo=1&numOfRows=1");*/
            // ,
            // 서울의
            // 날씨정보를
            // 기상청에서
            // xml로
            // 가져온다
            HttpURLConnection http = (HttpURLConnection) url.openConnection();

            http.setDefaultUseCaches(false);
            http.setRequestMethod("POST");
            http.setDoOutput(true);

            http.setRequestProperty("content-type",
                    "application/x-www-form-urlencoded");
            StringBuffer buffer = new StringBuffer();

            OutputStreamWriter outStream = new OutputStreamWriter(
                    http.getOutputStream(), "UTF-8");
            PrintWriter writer = new PrintWriter(outStream);
            writer.write(buffer.toString());
            writer.flush();
            InputStreamReader tmp = new InputStreamReader(
                    http.getInputStream(), "UTF-8");
            BufferedReader reader = new BufferedReader(tmp);
            StringBuilder builder = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {
                builder.append(str);
            }

            ParsingsearchList(builder.toString());
        }

        private void ParsingsearchList(String data)
                throws UnsupportedEncodingException, IOException {// xml  파싱

            try {
                XmlPullParserFactory parserCreator = XmlPullParserFactory
                        .newInstance();
                XmlPullParser parser = parserCreator.newPullParser();
                InputStream input = new ByteArrayInputStream(
                        data.getBytes("UTF-8"));
                parser.setInput(input, "UTF-8");

                int parserEvent = parser.getEventType();
                String tag;
                boolean inText = false;
                boolean lastMatTag = false;
                String f_array[] = new String[19];
                int colIdx = 0;

                while (parserEvent != XmlPullParser.END_DOCUMENT) {
                    switch (parserEvent) {
                        case XmlPullParser.START_TAG:
                            tag = parser.getName();

                            if (tag.compareTo("rss") == 0) {
                                inText = false;
                            } else if (tag.compareTo("channel") == 0) {
                                inText = false;
                            } else if (tag.compareTo("link") == 0) {
                                inText = false;
                            } else if (tag.compareTo("description") == 0) {
                                inText = false;
                            } else if (tag.compareTo("language") == 0) {
                                inText = false;
                            } else if (tag.compareTo("title") == 0) {
                                inText = false;
                            } else if (tag.compareTo("item") == 0) {
                                inText = false;
                            } else if (tag.compareTo("generator") == 0) {
                                inText = false;
                            } else if (tag.compareTo("pubDate") == 0) {
                                inText = false;
                            } else if (tag.compareTo("item") == 0) {
                                inText = false;
                            } else if (tag.compareTo("author") == 0) {
                                inText = false;
                            } else if (tag.compareTo("category") == 0) {
                                inText = false;
                            } else if (tag.compareTo("link") == 0) {
                                inText = false;
                            } else if (tag.compareTo("guid") == 0) {
                                inText = false;
                            } else if (tag.compareTo("description") == 0) {
                                inText = false;
                            } else if (tag.compareTo("header") == 0) {
                                inText = false;
                            } else if (tag.compareTo("tm") == 0) {
                                inText = false;
                            } else if (tag.compareTo("ts") == 0) {
                                inText = false;
                            } else if (tag.compareTo("x") == 0) {
                                inText = false;
                            } else if (tag.compareTo("y") == 0) {
                                inText = false;
                            } else if (tag.compareTo("body") == 0) {
                                inText = false;
                            } else if (tag.compareTo("data") == 0) {
                                inText = false;
                            } else {
                                inText = true;
                                if (tag.compareTo("s06") == 0) {
                                    lastMatTag = true;
                                }

                            }
                            break;
                        case XmlPullParser.TEXT:
                            tag = parser.getName();

                            if (inText) {
                                if (parser.getText() == null) {
                                    f_array[colIdx] = "";
                                } else {
                                    f_array[colIdx] = parser.getText().trim();
                                }

                                if (lastMatTag) { // 파싱된 데이터를 WeatherDataTime 클래스로
                                    // 만들어 pDataList 에 add해준다
                                    WeatherDataTime com = new WeatherDataTime(
                                            f_array[0], f_array[1], f_array[2],
                                            f_array[3], f_array[4], f_array[5],
                                            f_array[6], f_array[7], f_array[8],
                                            f_array[9], f_array[10], f_array[11],
                                            f_array[12], f_array[13], f_array[14],
                                            f_array[15], f_array[16], f_array[17],
                                            f_array[18]);
                                    pDataList.add(com);

								
								<hour>18</hour>
								<day>0</day>
								<temp>30.0</temp>
								<tmx>-999.0</tmx>
								<tmn>-999.0</tmn>
								<sky>2</sky>
								<pty>0</pty>
								<wfKor>구름 조금</wfKor>
								<wfEn>Partly Cloudy</wfEn>
								<pop>10</pop>
								<r12>0.0</r12>
								<s12>0.0</s12>
								<ws>3.0</ws>
								<wd>6</wd>
								<wdKor>서</wdKor>
								<wdEn>W</wdEn>
								<reh>20</reh>
								<r06>0.0</r06>
								<s06>0.0</s06>
								*/


                                    colIdx = -1;
                                }
                                colIdx++;
                            }
                            inText = false;
                            lastMatTag = false;
                            break;
                        case XmlPullParser.END_TAG:
                            tag = parser.getName();
                            inText = false;
                            break;

                    }
                    parserEvent = parser.next();
                }
            } catch (Exception e) {
                Log.e("dd", "Error in network call", e);
            }
        }

        @Override
        protected void onPostExecute(Void result) {
            loagindDialog.dismiss();


           
            TextView now_temp = (TextView) findViewById(R.id.now_temp); // 현재온도
            now_temp.setText(pDataList.get(0).getTemp() + "℃");

            TextView now_reh = (TextView) findViewById(R.id.now_reh); // 습도 ,
            // 강수확률
            now_reh.setText("습도 " + pDataList.get(0).getReh() + "%  "
                    + " 강수확률 " + pDataList.get(0).getPop() + "%  "
                    + pDataList.get(0).getWdKor() + pDataList.get(0).getWs()
                    + "m/s");

            TextView now_status = (TextView) findViewById(R.id.now_status); // 현재날씨
            now_status.setText(pDataList.get(0).getWfKor());//풍향
            String wStatus = pDataList.get(0).getWfKor();

            LinearLayout now_img = (LinearLayout) findViewById(R.id.now_img);

            if (wStatus.equals("맑음")) { //날씨 상태에 따라 이미지를 세팅해준다
                now_img.setBackgroundResource(R.drawable.w01);
            } else if (wStatus.equals("구름 조금")) {
                now_img.setBackgroundResource(R.drawable.w02);
            } else if (wStatus.equals("구름 많음")) {
                now_img.setBackgroundResource(R.drawable.w03);
            } else if (wStatus.equals("흐림")) {
                now_img.setBackgroundResource(R.drawable.w03);
            } else if (wStatus.equals("비")) {
                now_img.setBackgroundResource(R.drawable.w04);
            }

            w_list = (ListView) findViewById(R.id.w_list);
            TextView nodata = (TextView) findViewById(R.id.nodata);

            if (pDataList.size() > 0) {
                nodata.setVisibility(View.GONE);
            } else {
                nodata.setVisibility(View.VISIBLE);
            }

            GoodsAdapter mAdapter = new GoodsAdapter(WeatherActivity.this,
                    R.layout.listview_weather, pDataList); 

            w_list.setAdapter(mAdapter);//리스트뷰에 연결
            mAdapter.notifyDataSetChanged();
        }

    }

    public class GoodsAdapter extends ArrayAdapter<WeatherDataTime> {
        private ArrayList<WeatherDataTime> items;
        WeatherDataTime fInfo;

        public GoodsAdapter(Context context, int textViewResourseId,
                            ArrayList<WeatherDataTime> items) {
            super(context, textViewResourseId, items);
            this.items = items;
        }

        public View getView(int position, View convertView, ViewGroup parent) {// listview

            View v = convertView;
            fInfo = items.get(position); 

            LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.listview_weather, null);

            TextView now_time = (TextView) v.findViewById(R.id.now_time); // 현재시간
            now_time.setText(dayArrary[Integer.parseInt(fInfo.getDay())] + "\n"+ fInfo.getHour() + ":00");

            TextView now_temp = (TextView) v.findViewById(R.id.now_temp); // 현재온도
            now_temp.setText(fInfo.getTemp() + "℃");

            TextView now_status = (TextView) v.findViewById(R.id.now_status); // 현재날씨
            now_status.setText(fInfo.getWfKor());

            ImageView now_img = (ImageView) v.findViewById(R.id.now_img); // 현재날씨 이미지
            // 이미지
            String wStatus = fInfo.getWfKor();

            if (wStatus.equals("맑음")) {
                now_img.setBackgroundResource(R.drawable.icon01);
            } else if (wStatus.equals("구름 조금")) {
                now_img.setBackgroundResource(R.drawable.icon02);
            } else if (wStatus.equals("구름 많음")) {
                now_img.setBackgroundResource(R.drawable.icon03);
            } else if (wStatus.equals("흐림")) {
                now_img.setBackgroundResource(R.drawable.icon04);
            } else if (wStatus.equals("비")) {
                now_img.setBackgroundResource(R.drawable.icon05);
            }

            return v;
        }
    }

    public class getWeatherTaskDay extends AsyncTask<String, Void, Void> {// 주간별
        // 날씨
        // 정보

        ProgressDialog loagindDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loagindDialog = ProgressDialog.show(WeatherActivity.this,
                    "날씨 정보 가져오는 중..", "please wait....", true, true, null);
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                getList();
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        }

        public String getNowDateDate() {//오늘날짜
            Calendar calendar = Calendar.getInstance();
            Date date = calendar.getTime();
            return new SimpleDateFormat("yyyyMMdd").format(date);
        }

        public String getYesterDay() {//어제 날짜
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -1);
            Date date = calendar.getTime();
            return new SimpleDateFormat("yyyyMMdd").format(date);
        }

        public String getNowDateTime() {//오늘시간
            Calendar calendar = Calendar.getInstance();
            Date date = calendar.getTime();
            return new SimpleDateFormat("HHmm").format(date);
        }

        private void getList() throws UnsupportedEncodingException, IOException {
            dDataList.clear();
            String urlStr = "http://newsky2.kma.go.kr/service/MiddleFrcstInfoService/getMiddleLandWeather?ServiceKey=47xyP83R8bgNzss6zaOOCkZb9%2FBysmhZBLW%2FmGChSnyWbSAveS%2F3komspst8hH6DE0goFNdUnylt6hx7FKGTIg%3D%3D&numOfRows=1&pageNo=1";

            String nowTime = getNowDateTime();
            String nowTimeStr = "";
            String nowData = "";

            
            if (Integer.parseInt(nowTime) >= 601) {
                // 기준시간으로 한다
                nowTimeStr = "0600";
                nowData = "&tmFc=" + getNowDateDate() + nowTimeStr;
            } else {
                nowTimeStr = "1800"; 
                nowData = "&tmFc=" + getYesterDay() + nowTimeStr;
            }



            String regID = "&regId="+resionCode1; //지역코드를 정해준다


            HttpGet httpGet = new HttpGet(urlStr + nowData+regID);
            HttpClient client = new DefaultHttpClient();
            HttpResponse response;
            String data = "";
            try {
                response = client.execute(httpGet);
                HttpEntity entity = response.getEntity();
                InputStream stream = entity.getContent();

                data = convertStreamToString(stream);
            } catch (ClientProtocolException e) {
            } catch (IOException e) {
            }

            ParsingsearchList(data);
        }

        public String convertStreamToString(InputStream is) throws IOException {
            if (is != null) {
                StringBuilder sb = new StringBuilder();
                String line;

                try {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(is, "UTF-8"));
                    while ((line = reader.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                } finally {
                    is.close();
                }
                return sb.toString();
            } else {
                return "";
            }
        }

        private void ParsingsearchList(String data)
                throws UnsupportedEncodingException, IOException {
            middleWeaherList.clear();
            dDataList.clear();

            try {
                XmlPullParserFactory parserCreator = XmlPullParserFactory
                        .newInstance();
                XmlPullParser parser = parserCreator.newPullParser();
                InputStream input = new ByteArrayInputStream(
                        data.getBytes("UTF-8"));
                parser.setInput(input, "UTF-8");

                int parserEvent = parser.getEventType();
                String tag;
                boolean inText = false;
                boolean lastMatTag = false;
                String f_array[] = new String[15];
                int colIdx = 0;

                while (parserEvent != XmlPullParser.END_DOCUMENT) {
                    switch (parserEvent) {
                        case XmlPullParser.START_TAG:
                            tag = parser.getName();

                            if (tag.compareTo("response") == 0) {
                                inText = false;
                            } else if (tag.compareTo("header") == 0) {
                                inText = false;
                            } else if (tag.compareTo("resultCode") == 0) {
                                inText = false;
                            } else if (tag.compareTo("resultMsg") == 0) {
                                inText = false;
                            } else if (tag.compareTo("body") == 0) {
                                inText = false;
                            } else if (tag.compareTo("items") == 0) {
                                inText = false;
                            } else if (tag.compareTo("item") == 0) {
                                inText = false;
                            } else if (tag.compareTo("numOfRows") == 0) {
                                inText = false;
                            } else if (tag.compareTo("pageNo") == 0) {
                                inText = false;
                            } else if (tag.compareTo("totalCount") == 0) {
                                inText = false;
                            } else if (tag.compareTo("regId") == 0) {
                                inText = false;
                            }

                            else {
                                inText = true;
                            }
                            break;
                        case XmlPullParser.TEXT:
                            tag = parser.getName();

                            if (inText) {
                                if (parser.getText() == null) {
                                    f_array[colIdx] = "";
                                } else {
                                    f_array[colIdx] = parser.getText().trim();
                                }

                                middleWeaherList.add(f_array[colIdx]);
                                colIdx++;
                            }
                            inText = false;
                            lastMatTag = false;
                            break;
                        case XmlPullParser.END_TAG:
                            tag = parser.getName();
                            inText = false;
                            break;

                    }
                    parserEvent = parser.next();
                }
            } catch (Exception e) {
                Log.e("dd", "Error in network call", e);
            }
        }
        public String getDay(int i) {   //날짜
            Calendar calendar = Calendar.getInstance();
            if(i == 0 || i == 1){
                calendar.add(Calendar.DATE, 3);
            }else if(i == 2 || i == 3){
                calendar.add(Calendar.DATE, 4);
            }else if(i == 4 || i == 5) {
                calendar.add(Calendar.DATE, 5);
            }else if(i == 6 || i == 7) {
                calendar.add(Calendar.DATE, 6);
            }else if(i == 8 || i == 9) {
                calendar.add(Calendar.DATE, 7);
            }else if(i == 10) {
                calendar.add(Calendar.DATE, 8);
            }else if(i == 11) {
                calendar.add(Calendar.DATE, 9);
            }else if(i == 12){
                calendar.add(Calendar.DATE, 10);
            }
            Date date = calendar.getTime();
            return new SimpleDateFormat("yyyy-MM-dd").format(date);
        }
        @Override
        protected void onPostExecute(Void result) {
            loagindDialog.dismiss();

            w_list = (ListView) findViewById(R.id.w_list);
            TextView nodata = (TextView) findViewById(R.id.nodata);

            if (middleWeaherList.size() > 0) {
                nodata.setVisibility(View.GONE);

            } else {
                nodata.setVisibility(View.VISIBLE);
            }


            for (int i = 0; i < middleWeaherList.size(); i++) { 
               

                if(i == middleWeaherList.size()-1){
                    dDataList.add(new WeatherDataDays(getDay(i) + "\n" + middleTxt[0],
                            middleWeaherList.get(0).toString())); 
                }else {
                    dDataList.add(new WeatherDataDays(getDay(i) + "\n" + middleTxt[i+1],
                            middleWeaherList.get(i+1).toString()));
                }
            }

            GoodsAdapter1 mAdapter = new GoodsAdapter1(WeatherActivity.this,
                    R.layout.listview_weather, dDataList);

            w_list.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }

    }
    public class GoodsAdapter1 extends ArrayAdapter<WeatherDataDays> {
        private ArrayList<WeatherDataDays> items;
        WeatherDataDays fInfo;

        public GoodsAdapter1(Context context, int textViewResourseId,
                             ArrayList<WeatherDataDays> items) {
            super(context, textViewResourseId, items);
            this.items = items;
        }

        public View getView(int position, View convertView, ViewGroup parent) {// listview

            View v = convertView;
            fInfo = items.get(position);

            LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.listview_weather_middle, null);

            TextView now_time = (TextView) v.findViewById(R.id.now_time); // 현재날짜
            now_time.setText(fInfo.getDays());

            TextView now_status = (TextView) v.findViewById(R.id.now_status);
            // 현재날씨
            now_status.setText(fInfo.getStatus());

            ImageView now_img = (ImageView) v.findViewById(R.id.now_img); // 현재날씨
            // 이미지
            String wStatus = fInfo.getStatus();

            if (wStatus == null) {
                wStatus = "";
            }

            if (wStatus.equals("맑음")) {
                now_img.setBackgroundResource(R.drawable.icon01);
            } else if (wStatus.equals("구름조금")) {
                now_img.setBackgroundResource(R.drawable.icon02);
            } else if (wStatus.equals("구름많음")) {
                now_img.setBackgroundResource(R.drawable.icon03);
            } else if (wStatus.equals("흐림")) {
                now_img.setBackgroundResource(R.drawable.icon04);
            } else if (wStatus.equals("비") || wStatus.equals("흐리고 비")
                    || wStatus.equals("구름많고 비")) {
                now_img.setBackgroundResource(R.drawable.icon05);
            }

            return v;
        }
    }

    /*private class getWeatherTaskAll extends AsyncTask<String, Void, Void> {// 전국날씨
        // 정보

        ProgressDialog loagindDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loagindDialog = ProgressDialog.show(WeatherActivity.this,
                    "날씨 정보 가져오는 중..", "please wait....", true, true, null);
        }

        @Override
        protected Void doInBackground(String... params) {//시간별 날씨랑 코드는 동일하다 
            try {
                getList();
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        }

        private void getList() throws UnsupportedEncodingException, IOException {
            dDataList.clear();

            URL url = new URL(
                    "http://www.kma.go.kr/weather/forecast/mid-term-rss3.jsp?stnId=108"); // URL
            HttpURLConnection http = (HttpURLConnection) url.openConnection();

            http.setDefaultUseCaches(false);
            http.setRequestMethod("POST");
            http.setDoOutput(true);

            http.setRequestProperty("content-type",
                    "application/x-www-form-urlencoded");
            StringBuffer buffer = new StringBuffer();

            OutputStreamWriter outStream = new OutputStreamWriter(
                    http.getOutputStream(), "UTF-8");
            PrintWriter writer = new PrintWriter(outStream);
            writer.write(buffer.toString());
            writer.flush();
            InputStreamReader tmp = new InputStreamReader(
                    http.getInputStream(), "UTF-8");
            BufferedReader reader = new BufferedReader(tmp);
            StringBuilder builder = new StringBuilder();
            String str;
            while ((str = reader.readLine()) != null) {
                builder.append(str);
            }

            ParsingsearchList(builder.toString());
        }

        private void ParsingsearchList(String data)
                throws UnsupportedEncodingException, IOException {// 데이터 파싱

            try {
                XmlPullParserFactory parserCreator = XmlPullParserFactory
                        .newInstance();
                XmlPullParser parser = parserCreator.newPullParser();
                InputStream input = new ByteArrayInputStream(
                        data.getBytes("UTF-8"));
                parser.setInput(input, "UTF-8");

                int parserEvent = parser.getEventType();
                String tag;
                boolean inText = false;
                boolean lastMatTag = false;
                String f_array[] = new String[8];
                int colIdx = 0;
                boolean isfirst = true;
                boolean proTag = false;

                while (parserEvent != XmlPullParser.END_DOCUMENT) {
                    switch (parserEvent) {
                        case XmlPullParser.START_TAG:
                            tag = parser.getName();

                            if (tag.compareTo("channel") == 0) {
                                inText = false;
                            } else if (tag.compareTo("title") == 0) {
                                inText = false;
                            } else if (tag.compareTo("link") == 0) {
                                inText = false;
                            } else if (tag.compareTo("description") == 0) {
                                inText = false;
                            } else if (tag.compareTo("wf") == 0) {//wf 태그가 2개가 나오므로 처음나오는 wf태그는 파싱하지 않음
                                if (isfirst) {
                                    inText = false;
                                    isfirst = false;
                                } else {
                                    inText = true;
                                }

                            } else if (tag.compareTo("language") == 0) {
                                inText = false;
                            } else if (tag.compareTo("generator") == 0) {
                                inText = false;
                            } else if (tag.compareTo("province") == 0) {
                                inText = false;
                            } else if (tag.compareTo("pubDate") == 0) {
                                inText = false;
                            } else if (tag.compareTo("item") == 0) {
                                inText = false;
                            } else if (tag.compareTo("author") == 0) {
                                inText = false;
                            } else if (tag.compareTo("category") == 0) {
                                inText = false;
                            } else if (tag.compareTo("guid") == 0) {
                                inText = false;
                            } else if (tag.compareTo("header") == 0) {
                                inText = false;
                            } else if (tag.compareTo("tm") == 0) {
                                inText = false;
                            } else if (tag.compareTo("body") == 0) {
                                inText = false;
                            } else if (tag.compareTo("location") == 0) {
                                inText = false;
                            } else if (tag.compareTo("city") == 0) {
                                inText = false;
                                proTag = true;
                            } else if (tag.compareTo("data") == 0) {
                                inText = false;
                            } else {
                                inText = true;
                                if (tag.compareTo("reliability") == 0) {
                                    lastMatTag = true;
                                }
                            }
                            break;
                        case XmlPullParser.TEXT:
                            tag = parser.getName();

                            if (proTag) {
                                provice = parser.getText().trim();
                                proTag = false;
                            }

                            if (inText) {
                                if (parser.getText() == null) {

                                    f_array[colIdx] = "";
                                } else {

                                    f_array[colIdx] = parser.getText().trim();

                                }

                                if (lastMatTag) { // 파싱된 데이터를 WeatherDataDaysAll
                                    // 클래스로
                                    // 만들어 pDataList 에 add해준다
                                    WeatherDataDaysAll com = new WeatherDataDaysAll(
                                            provice, f_array[0], f_array[1],
                                            f_array[2], f_array[3], f_array[4],
                                            f_array[5]);

                                    aDataList.add(com);
                                    colIdx = -1;
                                }
                                colIdx++;
                            }
                            inText = false;
                            lastMatTag = false;
                            break;
                        case XmlPullParser.END_TAG:
                            tag = parser.getName();
                            inText = false;
                            break;

                    }
                    parserEvent = parser.next();
                }
            } catch (Exception e) {
                Log.e("dd", "Error in network call", e);
            }
        }

        @Override
        protected void onPostExecute(Void result) {
            loagindDialog.dismiss();

            w_list = (ListView) findViewById(R.id.w_list);
            TextView nodata = (TextView) findViewById(R.id.nodata);

            if (aDataList.size() > 0) {
                nodata.setVisibility(View.GONE);

            } else {
                nodata.setVisibility(View.VISIBLE);
            }

            GoodsAdapter2 mAdapter = new GoodsAdapter2(WeatherActivity.this,
                    R.layout.listview_weather, aDataList);

            w_list.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }

    }

    String provice = "";

    public class GoodsAdapter2 extends ArrayAdapter<WeatherDataDaysAll> {
        private ArrayList<WeatherDataDaysAll> items;
        WeatherDataDaysAll fInfo;

        public GoodsAdapter2(Context context, int textViewResourseId,
                             ArrayList<WeatherDataDaysAll> items) {
            super(context, textViewResourseId, items);
            this.items = items;
        }

        public View getView(int position, View convertView, ViewGroup parent) {// listview

            View v = convertView;
            fInfo = items.get(position);

            LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.listview_weather, null);

            TextView now_time = (TextView) v.findViewById(R.id.now_time); // 현재시간
            now_time.setText(fInfo.getTmEf().substring(0, 10) + "\n"
                    + fInfo.getProvince());

            TextView now_temp = (TextView) v.findViewById(R.id.now_temp); // 현재온도
            now_temp.setText(fInfo.getTmx() + "℃" + "/" + fInfo.getTmn() + "℃");

            TextView now_status = (TextView) v.findViewById(R.id.now_status); // 현재날씨
            now_status.setText(fInfo.getWf());

            ImageView now_img = (ImageView) v.findViewById(R.id.now_img); // 현재날씨이미지
            // 이미지
            String wStatus = fInfo.getWf();

            if (wStatus == null) {
                wStatus = "";
            }

            if (wStatus.equals("맑음")) {
                now_img.setBackgroundResource(R.drawable.icon01);
            } else if (wStatus.equals("구름조금")) {
                now_img.setBackgroundResource(R.drawable.icon02);
            } else if (wStatus.equals("구름많음")) {
                now_img.setBackgroundResource(R.drawable.icon03);
            } else if (wStatus.equals("흐림")) {
                now_img.setBackgroundResource(R.drawable.icon04);
            } else if (wStatus.equals("비") || wStatus.equals("흐리고 비")
                    || wStatus.equals("구름많고 비")) {
                now_img.setBackgroundResource(R.drawable.icon05);
            }

            return v;
        }
    }
 */
}
