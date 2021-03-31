package com.hci.happycaster.ActivityClass;

import android.content.Intent;
import android.graphics.DashPathEffect;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.hci.happycaster.ChartClass.LineChartCreate;
import com.hci.happycaster.ChartClass.PieChartCreate;
import com.hci.happycaster.R;
import com.hci.happycaster.ServerComunication.DataHandler;
import com.hci.happycaster.ServerComunication.DataObject;
import com.hci.happycaster.ServerComunication.WebServiceManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

public class PeriodDetailActivity extends AppCompatActivity{

    private LineChart lineChart;
    private PieChart pieChart;
    private LineChartCreate lineChartCreate;
    private PieChartCreate pieChartCreate;
    private Intent intent;
    private String city, startday , endday;
    private Date startDate, endDate;
    private int Totalcnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_period_detail);

        lineChart = (LineChart)findViewById(R.id.PeriodLinechart);
        pieChart = (PieChart)findViewById(R.id.PeriodPiechart);

        intent = getIntent();

        city = intent.getStringExtra("City");

        startday = intent.getStringExtra("startDay"); endday = intent.getStringExtra("endDay");

        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
            startDate = sf.parse(startday);
            endDate = sf.parse(endday);
            System.out.println("startdate : "+startDate);
            System.out.println("startdate : "+endDate);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }

        //기간별 분석 - 그래프
        System.out.println("기간별 분석 - 그래프");
        try {
            WebServiceManager service7 = new WebServiceManager();
            service7.setServiceNamespace("http://webservice.class.inje.ac.kr");
            service7.setServiceURL("http://203.241.246.158/khac/MyService");
            service7.setOperationName("getGraphByPeriod");
            service7.setParameter("0", city);
            service7.setParameter("1", startday);    //시작 일자
            service7.setParameter("2", endday);    //종료 일자..시작 일자가 종료 일자보다 크면 안됨 주의!!
            service7.setHandler(new MyHandler7());
            service7.start();
            try{
                service7.join();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        System.out.println("기간별 분석 - 키워드");
        try {

            WebServiceManager service8 = new WebServiceManager();
            service8.setServiceNamespace("http://webservice.class.inje.ac.kr");
            service8.setServiceURL("http://203.241.246.158/khac/MyService");
            service8.setOperationName("getKeywordsByPreiod");
            service8.setParameter("0", city);
            service8.setParameter("1", startday);    //시작 일자
            service8.setParameter("2", endday);    //종료 일자..시작 일자가 종료 일자보다 크면 안됨 주의!!
            service8.setHandler(new MyHandler8());
            service8.start();
            try{
                service8.join();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //기간별 분석 - 그래프
    class MyHandler7 extends DataHandler{
        public void handler(final ArrayList<DataObject> objects) throws Exception{
            for(int i=0; i<objects.size(); i++) {
                System.out.print(objects.get(i).getValue("time") + "\t");
                System.out.println(objects.get(i).getValue("score"));
            }
            System.out.println();


            lineChartCreate = new LineChartCreate() {
                @Override
                public void Create() {
                    lineChart.setOnChartValueSelectedListener(this);
                    lineChart.setOnChartGestureListener(this);
                    lineChart.setDrawGridBackground(false);

                    lineChart.getDescription().setEnabled(false);
                    lineChart.setDrawBorders(true);

                    DashPathEffect dashPath =
                            new DashPathEffect(new float[]{20,3}, 1);

                    lineChart.getAxisLeft().setEnabled(true);
                    lineChart.getAxisLeft().setTypeface(Typeface.DEFAULT_BOLD);
                    lineChart.getAxisLeft().setDrawGridLines(false);
                    lineChart.getXAxis().setEnabled(true);
                    lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                    lineChart.getXAxis().setDrawGridLines(true);
                    lineChart.getXAxis().setGridDashedLine(dashPath);
                    lineChart.getXAxis().setAxisMaximum(objects.size()-1);
                    lineChart.getXAxis().setAxisMinimum(0f);
                    lineChart.getXAxis().setLabelCount(objects.size()-1);

                    // add data
                    setData();

                    // get the legend (only possible after setting data)
                    Legend l = lineChart.getLegend();

                    // modify the legend ...
                    // l.setPosition(LegendPosition.LEFT_OF_CHART);
                    l.setForm(Legend.LegendForm.LINE);
                    l.setTextSize(12f);

                    // enable touch gestures
                    lineChart.setTouchEnabled(true);
                    // enable scaling and dragging
                    lineChart.setDragEnabled(false);
                    lineChart.setScaleEnabled(false);
                    // mChart.setScaleXEnabled(true);
                    // mChart.setScaleYEnabled(true);

                    lineChart.setPinchZoom(false);

                    YAxis leftAxis = lineChart.getAxisLeft();
                    //leftAxis.setAxisMaximum(100f);
                    //leftAxis.setAxisMinimum(0f);
                    //leftAxis.setYOffset(20f);
                    leftAxis.enableGridDashedLine(10f, 10f, 0f);
                    //leftAxis.setDrawZeroLine(false);

                    // limit lines are drawn behind data (and not on top)
                    leftAxis.setDrawLimitLinesBehindData(true);

                    lineChart.getAxisRight().setEnabled(false);

                    //mChart.getViewPortHandler().setMaximumScaleY(2f);
                    //mChart.getViewPortHandler().setMaximumScaleX(2f);

                    //mChart.animateX(2500, Easing.EasingOption.EaseInOutQuart);

                    //  dont forget to refresh the drawing
                    lineChart.invalidate();
                }

                @Override
                public ArrayList<String> setXAxisValues() {
                    Calendar cal = new GregorianCalendar(Locale.KOREA);
                    cal.setTime(startDate);
                    System.out.println("startdate : "+startDate);
                    System.out.println("startdate : "+cal.getTime());
                    SimpleDateFormat fm = new SimpleDateFormat("MM/dd");
                    ArrayList<String> xVals = new ArrayList<String>();
                    for(int i=0; i<objects.size(); i++) {
                        System.out.println("startdate : "+fm.format(cal.getTime()));
                        xVals.add(fm.format(cal.getTime()));
                        cal.add(Calendar.DAY_OF_YEAR,1);
                    }
                    return xVals;
                }

                @Override
                public ArrayList<Entry> setYAxisValues() {
                    ArrayList<Entry> yVals = new ArrayList<Entry>();
                    for(int i=0; i<objects.size(); i++) {
                        yVals.add(new Entry(i, Float.parseFloat(objects.get(i).getValue("score").toString())));
                    }
                    return yVals;
                }

                @Override
                public void setData() {
                    final ArrayList<String> xVals = setXAxisValues();
                    ArrayList<Entry> yVals = setYAxisValues();

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
                    set1.setLabel(city);
                    dataSets.add(set1);// add the datasets

                    // create a data object with the datasets
                    LineData data = new LineData(dataSets);

                    XAxis xAxis = lineChart.getXAxis();
                    xAxis.setValueFormatter(new IAxisValueFormatter() {
                        @Override
                        public String getFormattedValue(float value, AxisBase axis) {
                            return xVals.get((int) value % xVals.size());
                        }
                    });

                    // set data
                    lineChart.setData(data);
                }
            };
            lineChartCreate.Create();

        }
    }

    //기간별 분석 - 키워드
    class MyHandler8 extends DataHandler {
        public void handler(final ArrayList<DataObject> objects) throws Exception{
            for(int i=0; i<objects.size(); i++) {
                //objects의 길이는 10이며 키워드 순위가 높은것부터 차례대로 10개가 리턴
                //count는 사용자가 지정한 기간 동안의 키워드 빈도수
                System.out.print(objects.get(i).getValue("keyword") + "\t");
                System.out.println(objects.get(i).getValue("count"));
                Totalcnt += Integer.parseInt(objects.get(i).getValue("count").toString());
            }
            System.out.println("Total Cnt : "+Totalcnt);
            System.out.println();
            pieChartCreate = new PieChartCreate(pieChart) {
                @Override
                public ArrayList<String> setXAxisValues() {
                    ArrayList<String> xVals = new ArrayList<String>();
                    for(int i=0; i<objects.size(); i++) {
                        xVals.add(objects.get(i).getValue("keyword").toString());
                    }
                    return xVals;
                }

                @Override
                public ArrayList<PieEntry> setYAxisValues() {
                    ArrayList<PieEntry> yVals = new ArrayList<PieEntry>();
                    for(int i=0; i<objects.size(); i++) {
                        yVals.add(new PieEntry(Percent(Float.parseFloat(objects.get(i).getValue("count").toString()),(float) Totalcnt), objects.get(i).getValue("keyword").toString()));
                    }


                    return yVals;
                }
            };
            pieChartCreate.Create();
        }
    }
    private float Percent(float cnt, float totalcnt)
    {
        float per;
        per = (float) (cnt/totalcnt)*100;
        System.out.println("Percent : "+ (cnt/totalcnt));
        return per;
    }
}
