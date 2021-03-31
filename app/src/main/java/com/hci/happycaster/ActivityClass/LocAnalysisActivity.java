package com.hci.happycaster.ActivityClass;

import android.app.ProgressDialog;
import android.graphics.DashPathEffect;
import android.graphics.Typeface;
import android.media.midi.MidiOutputPort;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.hci.happycaster.ChartClass.LineChartCreate;
import com.hci.happycaster.CustomView.HappyRanking;
import com.hci.happycaster.CustomView.HappyRanking1Image;
import com.hci.happycaster.CustomView.HappyRanking2;
import com.hci.happycaster.CustomView.HappyRanking3;
import com.hci.happycaster.R;
import com.hci.happycaster.ServerComunication.DataHandler;
import com.hci.happycaster.ServerComunication.DataObject;
import com.hci.happycaster.ServerComunication.WebServiceManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

public class LocAnalysisActivity extends AppCompatActivity{

    private HappyRanking1Image happy1,happy2,happy3;
    private HappyRanking ranking1;
    private HappyRanking2 ranking2;
    private HappyRanking3 ranking3;
    private LineChart mChart;
    private SimpleDateFormat dt = new SimpleDateFormat("yyyymmdd");
    private LineChartCreate lineChartCreate;
    private ProgressDialog mProgressDialog;
    private Date d;
    private ArrayList<ArrayList<ArrayList<String>>> MonthArray = new ArrayList<ArrayList<ArrayList<String>>>();
    private String[] regions = {"서울","대전","대구","부산","인천","울산","광주"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loc_analysis);

        happy1 = (HappyRanking1Image)findViewById(R.id.Ranking1view) ;
        happy2 = (HappyRanking1Image)findViewById(R.id.Ranking2view) ;
        happy3 = (HappyRanking1Image)findViewById(R.id.Ranking3view) ;

        ranking1 = (HappyRanking)findViewById(R.id.HappyRankingview) ;
        ranking2 = (HappyRanking2)findViewById(R.id.HappyRankingview2) ;
        ranking3 = (HappyRanking3)findViewById(R.id.HappyRankingview3) ;

        mChart = (LineChart) findViewById(R.id.Chartview);

        mProgressDialog = ProgressDialog.show(
                LocAnalysisActivity.this, "데이터 수집 중",
                "잠시만 기다려주세요...");


        System.out.println("지역별 분석 - 그래프");
        try {
            for (String regionName : regions) {
                WebServiceManager service5 = new WebServiceManager();
                service5.setServiceNamespace("http://webservice.class.inje.ac.kr");
                service5.setServiceURL("http://203.241.246.158/khac/MyService");
                service5.setOperationName("getWeeklyGraph");
                service5.setParameter("0", regionName);
                service5.setHandler(new MyHandler5());
                System.out.println(regionName);
                service5.start();
                try
                {
                    service5.join();
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                System.out.println(MonthArray);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        System.out.println(MonthArray);

        lineChartCreate = new LineChartCreate() {
            @Override
            public void Create() {
                mChart.setOnChartValueSelectedListener(this);
                mChart.setOnChartGestureListener(this);
                mChart.setDrawGridBackground(false);

                mChart.getDescription().setEnabled(false);
                mChart.setDrawBorders(true);

                DashPathEffect dashPath =
                        new DashPathEffect(new float[]{20,3}, 1);

                mChart.getAxisLeft().setEnabled(true);
                mChart.getAxisLeft().setTypeface(Typeface.DEFAULT_BOLD);
                mChart.getAxisLeft().setDrawGridLines(false);
                mChart.getXAxis().setEnabled(true);
                mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                mChart.getXAxis().setDrawGridLines(true);
                mChart.getXAxis().setGridDashedLine(dashPath);
                mChart.getXAxis().setAxisMaximum(6f);
                mChart.getXAxis().setAxisMinimum(0f);
                mChart.getXAxis().setLabelCount(6);

                // add data
                setData();

                // get the legend (only possible after setting data)
                Legend l = mChart.getLegend();

                // modify the legend ...
                // l.setPosition(LegendPosition.LEFT_OF_CHART);
                l.setForm(Legend.LegendForm.LINE);
                l.setTextSize(12f);

                // enable touch gestures
                mChart.setTouchEnabled(true);
                // enable scaling and dragging
                mChart.setDragEnabled(false);
                mChart.setScaleEnabled(false);
                // mChart.setScaleXEnabled(true);
                // mChart.setScaleYEnabled(true);

                mChart.setPinchZoom(false);

                YAxis leftAxis = mChart.getAxisLeft();
                //leftAxis.setAxisMaximum(100);
                //leftAxis.setAxisMinimum(0);
                leftAxis.enableGridDashedLine(10f, 10f, 0f);
                //leftAxis.setLabelCount(5);
                //leftAxis.setDrawZeroLine(false);

                // limit lines are drawn behind data (and not on top)
                leftAxis.setDrawLimitLinesBehindData(true);

                mChart.getAxisRight().setEnabled(false);

                //mChart.getViewPortHandler().setMaximumScaleY(2f);
                //mChart.getViewPortHandler().setMaximumScaleX(2f);

                //mChart.animateX(2500, Easing.EasingOption.EaseInOutQuart);

                //  dont forget to refresh the drawing
                mChart.invalidate();
            }

            @Override
            public ArrayList<String> setXAxisValues() {
                Calendar cal = new GregorianCalendar(Locale.KOREA);
                cal.setTime(new Date());
                cal.add(Calendar.DAY_OF_YEAR,-6);
                SimpleDateFormat fm = new SimpleDateFormat("MM/dd");
                ArrayList<String> xVals = new ArrayList<String>();
                for(int i=0; i<MonthArray.size(); i++) {
                    xVals.add(fm.format(cal.getTime()));
                    cal.add(Calendar.DAY_OF_YEAR,1);
                }
                return xVals;
            }

            @Override
            public ArrayList<Entry> setYAxisValues() {
                return null;
            }


            public ArrayList<Entry> setYAxisValues(int total) {
                ArrayList<Entry> yVals = new ArrayList<>();
                for(int i=0; i<MonthArray.size(); i++) {
                    yVals.add(new Entry(i, Float.parseFloat(MonthArray.get(total).get(i).get(1).toString())));
                }
                return yVals;
            }

            @Override
            public void setData() {
                final ArrayList<String> xVals = setXAxisValues();

                ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();

                LineDataSet set1;

                // create a dataset and give it a type

                // set1.setFillColor(Color.RED);

                // set the line to be drawn like this "- - - - - -"
                // set1.enableDashedLine(10f, 5f, 0f);
                // set1.enableDashedHighlightLine(10f, 5f, 0f);
                for(int i = 0; i<MonthArray.size(); i++)
                {
                    ArrayList<Entry> yVals = setYAxisValues(i);

                    set1 = new LineDataSet(yVals, "DataSet "+i);
                    set1.setAxisDependency(YAxis.AxisDependency.LEFT);
                    set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);

                    set1.setFillAlpha(110);

                    if(i == 0)
                    {
                        set1.setColor(getApplicationContext().getResources().getColor(R.color.Seoul));
                        set1.setCircleColor(getApplicationContext().getResources().getColor(R.color.Seoul));
                        set1.setLabel("서울");
                    }
                    else if(i == 1)
                    {
                        set1.setColor(getApplicationContext().getResources().getColor(R.color.Daejeon));
                        set1.setCircleColor(getApplicationContext().getResources().getColor(R.color.Daejeon));
                        set1.setLabel("대전");
                    }
                    else if(i == 2)
                    {
                        set1.setColor(getApplicationContext().getResources().getColor(R.color.Daegu));
                        set1.setCircleColor(getApplicationContext().getResources().getColor(R.color.Daegu));
                        set1.setLabel("대구");
                    }
                    else if(i == 3)
                    {
                        set1.setColor(getApplicationContext().getResources().getColor(R.color.Busan));
                        set1.setCircleColor(getApplicationContext().getResources().getColor(R.color.Busan));
                        set1.setLabel("부산");
                    }
                    else if(i == 4)
                    {
                        set1.setColor(getApplicationContext().getResources().getColor(R.color.GRAY));
                        set1.setCircleColor(getApplicationContext().getResources().getColor(R.color.GRAY));
                        set1.setLabel("인천");
                    }
                    else if(i == 5)
                    {
                        set1.setColor(getApplicationContext().getResources().getColor(R.color.Ulsan));
                        set1.setCircleColor(getApplicationContext().getResources().getColor(R.color.Ulsan));
                        set1.setLabel("울산");
                    }
                    else if(i == 6)
                    {
                        set1.setColor(getApplicationContext().getResources().getColor(R.color.Gwangju));
                        set1.setCircleColor(getApplicationContext().getResources().getColor(R.color.Gwangju));
                        set1.setLabel("광주");
                    }
                    //set1.setColor(getApplicationContext().getResources().getColor(R.color.Seoul));
                    //set1.setCircleColor(getApplicationContext().getResources().getColor(R.color.Seoul));
                    set1.setLineWidth(3f);
                    set1.setCircleHoleRadius(7f);
                    set1.setCircleRadius(7f);
                    set1.setDrawCircleHole(true);
                    set1.setValueTextSize(10f);
                    //set1.setLabel(""+i);
                    dataSets.add(set1);// add the datasets
                }

                // create a data object with the datasets
                LineData data = new LineData(dataSets);

                XAxis xAxis = mChart.getXAxis();
                xAxis.setValueFormatter(new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        return xVals.get((int) value % xVals.size());
                    }
                });
                // set data
                mChart.setData(data);

            }
        };
        lineChartCreate.Create();


        try {
            //지역별 분석 - 종합
            System.out.println("지역별 분석 - 종합");
            WebServiceManager service6 = new WebServiceManager();
            service6.setServiceNamespace("http://webservice.class.inje.ac.kr");
            service6.setServiceURL("http://203.241.246.158/khac/MyService");
            service6.setOperationName("getRankData");
            service6.setHandler(new MyHandler6());
            service6.start();
            try
            {
                service6.join();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    //지역별 분석 - 그래프
    class MyHandler5 extends DataHandler {

        public void handler(final ArrayList<DataObject> objects) throws Exception {
            ArrayList<ArrayList<String>> CityArray = new ArrayList<ArrayList<String>>();
            System.out.println(CityArray);
            for (int i = objects.size()-1; i >= 0; i--) {
                System.out.print(objects.get(i).getValue("time") + "\t");    //오늘부터 일주일간 일 단위 시간, 2017년 11월6일, 2017년 11월5일....2017년 10월31일
                System.out.println(objects.get(i).getValue("score"));    //일 단위 평균 행복도..11월3일까지 밖에 데이터가 없어서 3일 이전에는 0으로 나온다.
                ArrayList<String> TimeScoreArray = new ArrayList<String>();
                TimeScoreArray.add(objects.get(i).getValue("time"));
                TimeScoreArray.add(objects.get(i).getValue("score"));
                CityArray.add(TimeScoreArray);
                System.out.println(CityArray);
            }
            MonthArray.add(CityArray);
            System.out.println();
            System.out.println("LocSize : " + objects.size());
            d = dt.parse(objects.get(0).getValue("time"));
            dt = new SimpleDateFormat("mm월");
            System.out.println("mouth : " + dt.format(d));

            System.out.println(MonthArray);
        }
    }

    //지역별 분석 - 종합
    //objects의 길이는 3이고, 차례대로 주간 행복도 1순위 도시, 2순위 도시, 3순위 도시 데이터
    class MyHandler6 extends DataHandler{
        public void handler(ArrayList<DataObject> objects) throws Exception{
            Double Score;
            for(int i=0; i<objects.size(); i++) {
                System.out.print(objects.get(i).getValue("region") + "\t");	//지역명
                System.out.print(objects.get(i).getValue("score") + "\t");	//지역의 일주일 총 평균 행복도
                Score = Double.parseDouble(objects.get(i).getValue("score").toString());
                System.out.print(objects.get(i).getValue("keyword1") + "\t");	//지역의 일주일 간 키워드 1순위
                System.out.print(objects.get(i).getValue("keyword2") + "\t");	//일주일 간 키워드 2순위
                System.out.println(objects.get(i).getValue("keyword3"));	//일주일 간 키워드 3순위
                if(i == 0)
                {
                    happy1.setText(objects.get(i).getValue("region").toString());
                    ranking1.setHappyText(objects.get(i).getValue("score").toString()+"%");
                   // ranking1.setMonthText(dt.format(d));
                    ranking1.setRanking1Text("1. "+objects.get(i).getValue("keyword1"));
                    ranking1.setRanking2Text("2. "+objects.get(i).getValue("keyword2"));
                    ranking1.setRanking3Text("3. "+objects.get(i).getValue("keyword3"));
                    ArrowSelect(Score, i, happy1);
                    happy1.setHeart(R.drawable.loc_heart1);
                }
                if(i == 1)
                {
                    happy2.setText(objects.get(i).getValue("region").toString());
                    ranking2.setHappyText(objects.get(i).getValue("score").toString()+"%");
                    //ranking2.setMonthText(dt.format(d));
                    ranking2.setRanking1Text("1. "+objects.get(i).getValue("keyword1"));
                    ranking2.setRanking2Text("2. "+objects.get(i).getValue("keyword2"));
                    ranking2.setRanking3Text("3. "+objects.get(i).getValue("keyword3"));
                    ArrowSelect(Score, i, happy2);
                    happy2.setHeart(R.drawable.loc_heart3);
                }
                if(i == 2)
                {
                    happy3.setText(objects.get(i).getValue("region").toString());
                    ranking3.setHappyText(objects.get(i).getValue("score").toString()+"%");
                    //ranking3.setMonthText(dt.format(d));
                    ranking3.setRanking1Text("1. "+objects.get(i).getValue("keyword1"));
                    ranking3.setRanking2Text("2. "+objects.get(i).getValue("keyword2"));
                    ranking3.setRanking3Text("3. "+objects.get(i).getValue("keyword3"));
                    ArrowSelect(Score, i, happy3);
                    happy3.setHeart(R.drawable.loc_heart4);
                }
            }
            System.out.println();
            mProgressDialog.dismiss();
        }
    }
    private void ArrowSelect(Double happy,int ranking ,HappyRanking1Image happyRanking1Image)
    {
        Double HappyPer = happy;
        if(ranking == 0) {
            if (HappyPer >= 70) {
                happyRanking1Image.setArrow(R.drawable.loc_1st_arrow1);
            } else if (HappyPer >= 65 && HappyPer < 70) {
                happyRanking1Image.setArrow(R.drawable.loc_1st_arrow2);
            } else if (HappyPer >= 60 && HappyPer < 65) {
                happyRanking1Image.setArrow(R.drawable.loc_1st_arrow3);
            } else if (HappyPer >= 55 && HappyPer < 60) {
                happyRanking1Image.setArrow(R.drawable.loc_1st_arrow4);
            } else if (HappyPer < 55) {
                happyRanking1Image.setArrow(R.drawable.loc_1st_arrow5);
            }
        }
        else if(ranking == 1) {
            if (HappyPer >= 70) {
                happyRanking1Image.setArrow(R.drawable.loc_2nd_arrow1);
            } else if (HappyPer >= 65 && HappyPer < 70) {
                happyRanking1Image.setArrow(R.drawable.loc_2nd_arrow2);
            } else if (HappyPer >= 60 && HappyPer < 65) {
                happyRanking1Image.setArrow(R.drawable.loc_2nd_arrow3);
            } else if (HappyPer >= 55 && HappyPer < 60) {
                happyRanking1Image.setArrow(R.drawable.loc_2nd_arrow4);
            } else if (HappyPer < 55) {
                happyRanking1Image.setArrow(R.drawable.loc_2nd_arrow5);
            }
        }
        else if(ranking == 2) {
            if (HappyPer >= 70) {
                happyRanking1Image.setArrow(R.drawable.loc_3rd_arrow1);
            } else if (HappyPer >= 65 && HappyPer < 70) {
                happyRanking1Image.setArrow(R.drawable.loc_3rd_arrow2);
            } else if (HappyPer >= 60 && HappyPer < 65) {
                happyRanking1Image.setArrow(R.drawable.loc_3rd_arrow3);
            } else if (HappyPer >= 55 && HappyPer < 60) {
                happyRanking1Image.setArrow(R.drawable.loc_3rd_arrow4);
            } else if (HappyPer < 55) {
                happyRanking1Image.setArrow(R.drawable.loc_3rd_arrow5);
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();

        // Custom Actionbar를 사용하기 위해 CustomEnabled을 true 시키고 필요 없는 것은 false 시킨다
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);            //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayShowTitleEnabled(false);        //액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowHomeEnabled(false);            //홈 아이콘을 숨김처리합니다.

        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.menu_realtime_loc_bar));

        return true;
    }
}
