package com.hci.happycaster.ActivityClass;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hci.happycaster.CustomView.LocationImageView;
import com.hci.happycaster.CustomView.LocationImageView_Reverse;
import com.hci.happycaster.CustomView.ReferenceView;
import com.hci.happycaster.R;
import com.hci.happycaster.ServerComunication.DataHandler;
import com.hci.happycaster.ServerComunication.DataObject;
import com.hci.happycaster.ServerComunication.WebServiceManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RealTimeMapActivity extends AppCompatActivity {

    private LocationImageView seoul, daegu, daejeon, gwangju, ulsan;
    private LocationImageView_Reverse busan,incheon;
    private Intent intent;
    private ReferenceView tv;
    private ProgressDialog mProgressDialog;
    private ArrayList<Double> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_time_map);

        tv = (ReferenceView)findViewById(R.id.Timeview);
        seoul = (LocationImageView)findViewById(R.id.loc_seoul_view);
        busan = (LocationImageView_Reverse) findViewById(R.id.loc_busan_view);
        incheon = (LocationImageView_Reverse) findViewById(R.id.loc_incheon_view);
        daegu = (LocationImageView)findViewById(R.id.loc_daegu_view);
        daejeon = (LocationImageView)findViewById(R.id.loc_deojeon_view);
        gwangju = (LocationImageView)findViewById(R.id.loc_gwangju_view);
        ulsan = (LocationImageView)findViewById(R.id.loc_ulsan_view);

        mProgressDialog = ProgressDialog.show(
                RealTimeMapActivity.this, "데이터 수집 중",
                "잠시만 기다려주세요...");
        try {
            WebServiceManager service = new WebServiceManager();
            service.setServiceNamespace("http://webservice.class.inje.ac.kr");
            service.setServiceURL("http://203.241.246.158/khac/MyService");
            service.setOperationName("getScore");
            //System.out.println(service.getResponse());

            service.setHandler(new ScoresHandler());
            service.start();
            try
            {
                service.join();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            Toast.makeText(this,"서버 연결",Toast.LENGTH_SHORT).show();
        }
        catch(Exception e) {
            e.printStackTrace();
            Toast.makeText(this,"서버 오류",Toast.LENGTH_SHORT).show();
        }

        BestLocation();
    }

    public void mOnClick(View v)
    {
         switch (v.getId())
         {
             case R.id.loc_seoul_view:
                 intent = new Intent(getBaseContext(),RealTimeDetailActivity.class);
                 intent.putExtra("City","서울");
                 startActivity(intent);
                 break;
             case R.id.loc_busan_view:
                 intent = new Intent(getBaseContext(),RealTimeDetailActivity.class);
                 intent.putExtra("City","부산");
                 startActivity(intent);
                 break;
             case R.id.loc_incheon_view:
                 intent = new Intent(getBaseContext(),RealTimeDetailActivity.class);
                 intent.putExtra("City","인천");
                 startActivity(intent);
                 break;
             case R.id.loc_daegu_view:
                 intent = new Intent(getBaseContext(),RealTimeDetailActivity.class);
                 intent.putExtra("City","대구");
                 startActivity(intent);
                 break;
             case R.id.loc_deojeon_view:
                 intent = new Intent(getBaseContext(),RealTimeDetailActivity.class);
                 intent.putExtra("City","대전");
                 startActivity(intent);
                 break;
             case R.id.loc_gwangju_view:
                 intent = new Intent(getBaseContext(),RealTimeDetailActivity.class);
                 intent.putExtra("City","광주");
                 startActivity(intent);
                 break;
             case R.id.loc_ulsan_view:
                 intent = new Intent(getBaseContext(),RealTimeDetailActivity.class);
                 intent.putExtra("City","울산");
                 startActivity(intent);
                 break;
         }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();

        // Custom Actionbar를 사용하기 위해 CustomEnabled을 true 시키고 필요 없는 것은 false 시킨다
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);            //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayShowTitleEnabled(false);        //액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowHomeEnabled(false);            //홈 아이콘을 숨김처리합니다.

        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.menu_realtime_map_bar));

        return true;
    }
    private void BestLocation()
    {
        Double max = Double.MIN_VALUE;
        data.add(Double.parseDouble(seoul.getText()));
        data.add(Double.parseDouble(incheon.getText()));
        data.add(Double.parseDouble(gwangju.getText()));
        data.add(Double.parseDouble(daejeon.getText()));
        data.add(Double.parseDouble(daegu.getText()));
        data.add(Double.parseDouble(ulsan.getText()));
        data.add(Double.parseDouble(busan.getText()));


        for(int i=0;i<data.size();i++) {
            // 최대값 Max
            if (data.get(i) > max) {
                max = data.get(i);
            }
        }

        if(max.equals(Double.parseDouble(seoul.getText())))
        {
            seoul.setBest(View.VISIBLE);
        }
        else if(max.equals(Double.parseDouble(incheon.getText())))
        {
            incheon.setBest(View.VISIBLE);
        }
        else if(max.equals(Double.parseDouble(gwangju.getText())))
        {
            gwangju.setBest(View.VISIBLE);
        }
        else if(max.equals(Double.parseDouble(daegu.getText())))
        {
            daegu.setBest(View.VISIBLE);
        }
        else if(max.equals(Double.parseDouble(daejeon.getText())))
        {
            daejeon.setBest(View.VISIBLE);
        }
        else if(max.equals(Double.parseDouble(ulsan.getText())))
        {
            ulsan.setBest(View.VISIBLE);
        }
        else if(max.equals(Double.parseDouble(busan.getText())))
        {
            busan.setBest(View.VISIBLE);
        }
        System.out.println("최대값 : " + max);
    }
    private void WeatherSelect(LocationImageView_Reverse locationImageView)
    {
        Double HappyPer = Double.parseDouble(locationImageView.text.getText().toString());
        if(HappyPer >= 70)
        {
            locationImageView.setWeather(R.drawable.menu_realtime_icon1);
        }
        else if(HappyPer >= 65 && HappyPer < 70)
        {
            locationImageView.setWeather(R.drawable.menu_realtime_icon2);
        }
        else if(HappyPer >= 60 && HappyPer < 65)
        {
            locationImageView.setWeather(R.drawable.menu_realtime_icon3);
        }
        else if(HappyPer >= 55 && HappyPer < 60)
        {
            locationImageView.setWeather(R.drawable.menu_realtime_icon4);
        }
        else if(HappyPer < 55)
        {
            locationImageView.setWeather(R.drawable.menu_realtime_icon5);
        }

    }
    private void WeatherSelect(LocationImageView locationImageView)
    {
        Double HappyPer = Double.parseDouble(locationImageView.text.getText().toString());
        if(HappyPer >= 70)
        {
            locationImageView.setWeather(R.drawable.menu_realtime_icon1);
        }
        else if(HappyPer >= 65 && HappyPer < 70)
        {
            locationImageView.setWeather(R.drawable.menu_realtime_icon2);
        }
        else if(HappyPer >= 60 && HappyPer < 65)
        {
            locationImageView.setWeather(R.drawable.menu_realtime_icon3);
        }
        else if(HappyPer >= 55 && HappyPer < 60)
        {
            locationImageView.setWeather(R.drawable.menu_realtime_icon4);
        }
        else if(HappyPer < 55)
        {
            locationImageView.setWeather(R.drawable.menu_realtime_icon5);
        }

    }
    private class ScoresHandler extends DataHandler {
        public void handler(ArrayList<DataObject> objects) throws Exception{
            String Timestr = "";
            String Regionstr = "";
            String Scorestr = "";
            Double score;
            Date d;

            if(objects.size() > 0)
            {
                for(int i=0; i<objects.size(); i++) {
                    /*String str = "";
                    str += objects.get(i).getValue("time") + " ";
                    str += objects.get(i).getValue("region") + " ";
                    str += objects.get(i).getValue("score") + "\n";
                    System.out.println("Size : "+objects.size());

                    System.out.println("Data "+i+" : "+str);*/



                    Timestr = objects.get(i).getValue("time");
                    System.out.println("Data "+i+" : "+Timestr);
                    Regionstr = objects.get(i).getValue("region");
                    System.out.println("Data "+i+" : "+Regionstr);
                    Scorestr = objects.get(i).getValue("score");
                    System.out.println("Data "+i+" : "+Scorestr);

                    System.out.println("getTime : "+ Timestr);
                    SimpleDateFormat dt = new SimpleDateFormat("yyyymmddHH");
                    d = dt.parse(Timestr);
                    dt = new SimpleDateFormat("yyyy년 mm월 dd일 HH시 기준");

                    tv.setText(dt.format(d));
                    score = Double.parseDouble(Scorestr);
                    System.out.println("Region : "+Regionstr.equals("서울"));
                    System.out.println("Score : "+ score);
                    System.out.println("Date : "+ d);
                    System.out.println("Size : " + objects.size());
                    if(Regionstr.equals("서울"))
                    {
                        System.out.println("seoul : "+Scorestr);
                        seoul.setText(score);
                        WeatherSelect(seoul);
                    }
                    else if(Regionstr.equals("부산"))
                    {
                        System.out.println("busan : "+Scorestr);
                        busan.setText(score);
                        WeatherSelect(busan);
                    }
                    else if(Regionstr.equals("대전"))
                    {
                        System.out.println("daejeon : "+Scorestr);
                        daejeon.setText(score);
                        WeatherSelect(daejeon);
                    }
                    else if(Regionstr.equals("대구"))
                    {
                        System.out.println("daegu : "+Scorestr);
                        daegu.setText(score);
                        WeatherSelect(daegu);
                    }
                    else if(Regionstr.equals("인천"))
                    {
                        System.out.println("incheon : "+Scorestr);
                        incheon.setText(score);
                        WeatherSelect(incheon);
                    }
                    else if(Regionstr.equals("울산"))
                    {
                        System.out.println("ulsan : "+Scorestr);
                        ulsan.setText(score);
                        WeatherSelect(ulsan);
                    }
                    else if(Regionstr.equals("광주"))
                    {
                        System.out.println("gwangju : "+Scorestr);
                        gwangju.setText(score);
                        WeatherSelect(gwangju);
                    }
                }

                mProgressDialog.dismiss();
            }
            else
            {
                Toast.makeText(getBaseContext(),objects.toString(),Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
