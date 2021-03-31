package com.hci.happycaster.ActivityClass;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.DashPathEffect;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.hci.happycaster.ChartClass.LineChartCreate;
import com.hci.happycaster.CustomView.HappyAndRankingView;
import com.hci.happycaster.R;
import com.hci.happycaster.ServerComunication.DataHandler;
import com.hci.happycaster.ServerComunication.DataObject;
import com.hci.happycaster.ServerComunication.WebServiceManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class RealTimeDetailActivity extends AppCompatActivity{

    private LineChart mChart;
    private LineChartCreate lineChartCreate;
    private HappyAndRankingView happyAndRankingView;
    private ProgressDialog mProgressDialog;
    private String city,time;
    private Intent intent;
    private Float max=Float.MIN_VALUE,min=Float.MAX_VALUE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_time_detail);

        happyAndRankingView = (HappyAndRankingView)findViewById(R.id.happyRankingview) ;

        mChart = (LineChart)findViewById(R.id.LineChartView);

        intent = getIntent();
        city = intent.getStringExtra("City");
        System.out.println("City : " + city);

        time = intent.getStringExtra("Time");
        System.out.println("time : " + time);

        mProgressDialog = ProgressDialog.show(
                RealTimeDetailActivity.this, "데이터 수집 중",
                "잠시만 기다려주세요...");

        try {
            //실시간 분석(세부화면) - 지도에서 탭한 지역의 행복도
            WebServiceManager service = new WebServiceManager();
            service.setServiceNamespace("http://webservice.class.inje.ac.kr");
            service.setServiceURL("http://203.241.246.158/khac/MyService");
            service.setOperationName("getRealtimeDetailScore");
            service.setParameter("0", city);
            service.setHandler(new MyHandler1());
            service.start();
            try
            {
                service.join();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }

            //실시간 분석(세부화면) - 그래프용 일간 행복도
            WebServiceManager service2 = new WebServiceManager();
            service2.setServiceNamespace("http://webservice.class.inje.ac.kr");
            service2.setServiceURL("http://203.241.246.158/khac/MyService");
            service2.setOperationName("getRealtimeDetailGraph");
            service2.setParameter("0", city);
            service2.setHandler(new MyHandler2());
            service2.start();
            try
            {
                service2.join();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }

            //실시간 분석(세부화면) - 지도에서 탭한 지역의 키워드 3개
            WebServiceManager service3 = new WebServiceManager();
            service3.setServiceNamespace("http://webservice.class.inje.ac.kr");
            service3.setServiceURL("http://203.241.246.158/khac/MyService");
            service3.setOperationName("getRealtimeDetailKeyword");
            service3.setParameter("0", city);
            service3.setHandler(new MyHandler3());
            service3.start();
            try
            {
                service3.join();
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
    class MyHandler1 extends DataHandler {
        public void handler(ArrayList<DataObject> objects) throws Exception{
            if(objects.size() > 0) {
                for (int i = 0; i < objects.size(); i++) {
                    System.out.println("Return : " + objects.get(i).getValue("return"));
                    happyAndRankingView.setHappyText(objects.get(i).getValue("return").toString() + "%");
                }
            }
        }
    }

    class MyHandler2 extends DataHandler{
        public void handler(final ArrayList<DataObject> objects) throws Exception {
            if (objects.size() > 0) {
                System.out.println("objects : "+objects.size());
                for (int i = 0; i < objects.size(); i++) {
                    System.out.println("Time " + i + " : " + objects.get(i).getValue("time"));
                    System.out.println("score " + i + " : " + objects.get(i).getValue("score"));
                }
                lineChartCreate = new LineChartCreate() {
                    @Override
                    public void Create() {
                        mChart.setOnChartValueSelectedListener(this);
                        mChart.setOnChartGestureListener(this);
                        mChart.setDrawGridBackground(false);

                        mChart.getDescription().setEnabled(false);
                        mChart.setDrawBorders(true);

                        DashPathEffect dashPath =
                                new DashPathEffect(new float[]{20, 3}, 1);

                        mChart.getAxisLeft().setEnabled(true);
                        mChart.getAxisLeft().setTypeface(Typeface.DEFAULT_BOLD);
                        mChart.getAxisLeft().setDrawGridLines(false);
                        mChart.getXAxis().setEnabled(true);
                        mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                        mChart.getXAxis().setDrawGridLines(true);
                        mChart.getXAxis().setGridDashedLine(dashPath);
                        mChart.getXAxis().setAxisMaximum(objects.size() - 1);
                        mChart.getXAxis().setAxisMinimum(0f);
                        mChart.getXAxis().setLabelCount(objects.size() - 1);

                        // add data
                        setData();

                        // get the legend (only possible after setting data)
                        Legend l = mChart.getLegend();
                        l.setTextSize(12f);

                        // modify the legend ...
                        // l.setPosition(LegendPosition.LEFT_OF_CHART);
                        l.setForm(Legend.LegendForm.LINE);

                        // enable touch gestures
                        mChart.setTouchEnabled(true);
                        // enable scaling and dragging
                        mChart.setDragEnabled(false);
                        mChart.setScaleEnabled(false);
                        // mChart.setScaleXEnabled(true);
                        // mChart.setScaleYEnabled(true);

                        mChart.setPinchZoom(false);

                        YAxis leftAxis = mChart.getAxisLeft();
                        for (int i = 0; i < objects.size(); i++) {
                            // 최대값 Max
                            if (Float.parseFloat(objects.get(i).getValue("score")) > max) {
                                max = Float.parseFloat(objects.get(i).getValue("score"));
                            }
                            // 최소값 Min
                            if (Float.parseFloat(objects.get(i).getValue("score")) < min) {
                                min = Float.parseFloat(objects.get(i).getValue("score"));
                            }
                        }
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
                        ArrayList<String> xVals = new ArrayList<String>();
                        for (int i = 0; i < objects.size(); i++) {
                            xVals.add(objects.get(i).getValue("time").toString() + "시");
                        }
                        return xVals;
                    }

                    @Override
                    public ArrayList<Entry> setYAxisValues() {
                        ArrayList<Entry> yVals = new ArrayList<>();
                        for (int i = 0; i < objects.size(); i++) {
                            yVals.add(new Entry(i, Float.parseFloat(objects.get(i).getValue("score").toString())));
                        }
                        return yVals;
                    }

                    @Override
                    public void setData() {
                        final ArrayList<String> xVals = setXAxisValues();

                        final ArrayList<Entry> yVals = setYAxisValues();

                        LineDataSet set1;

                        // create a dataset and give it a type
                        set1 = new LineDataSet(yVals, "DataSet 1");
                        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
                        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);

                        set1.setFillAlpha(110);
                        // set1.setFillColor(Color.RED);

                        // set the line to be drawn like this "- - - - - -"
                        // set1.enableDashedLine(10f, 5f, 0f);
                        // set1.enableDashedHighlightLine(10f, 5f, 0f);
                        set1.setColor(getApplicationContext().getResources().getColor(R.color.Seoul));
                        set1.setCircleColor(getApplicationContext().getResources().getColor(R.color.Seoul));
                        set1.setLineWidth(3f);
                        set1.setCircleHoleRadius(7f);
                        set1.setCircleRadius(7f);
                        set1.setDrawCircleHole(true);
                        set1.setValueTextSize(10f);
                        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
                        set1.setLabel(city + "의 행복도");
                        dataSets.add(set1);// add the datasets

                        // create a data object with the datasets
                        LineData data = new LineData(dataSets);

                        XAxis xAxis = mChart.getXAxis();
                        xAxis.setValueFormatter(new IAxisValueFormatter() {
                            @Override
                            public String getFormattedValue(float value, AxisBase axis) {
                                System.out.println("value : "+(int) value);
                                return xVals.get((int) value % xVals.size());
                            }
                        });
                        // set data
                        mChart.setData(data);

                    }
                };
                lineChartCreate.Create();
            }
        }
    }

    class MyHandler3 extends DataHandler{

        public void handler(ArrayList<DataObject> objects) throws Exception{
            if(objects.size()>0) {
                for (int i = 0; i < objects.size(); i++) {
                    System.out.println("Keyword" + i + " : " + objects.get(i).getValue("keyword"));
                    System.out.println("Count " + i + " : " + objects.get(i).getValue("count"));
                }
                happyAndRankingView.setRanking1Text(objects.get(0).getValue("keyword").toString());
                happyAndRankingView.setRanking2Text(objects.get(1).getValue("keyword").toString());
                happyAndRankingView.setRanking3Text(objects.get(2).getValue("keyword").toString());
            }

                mProgressDialog.dismiss();

        }
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();

        // Custom Actionbar를 사용하기 위해 CustomEnabled을 true 시키고 필요 없는 것은 false 시킨다
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);            //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayShowTitleEnabled(false);        //액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowHomeEnabled(false);            //홈 아이콘을 숨김처리합니다.

        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.menu_realtime_detail_bar));

        return true;
    }
}
