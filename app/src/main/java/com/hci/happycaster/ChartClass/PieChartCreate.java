package com.hci.happycaster.ChartClass;

import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.MotionEvent;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;

/**
 * Created by w on 2017-11-04.
 */

public abstract class PieChartCreate implements OnChartValueSelectedListener {
    private PieChart mChart;

    public PieChartCreate(PieChart pieChart)
    {
        mChart = pieChart;
    }
    public void Create()
    {
        mChart.setOnChartValueSelectedListener(this);

        mChart.setUsePercentValues(true);

        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setDragDecelerationEnabled(false);

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(7f);
        mChart.setTransparentCircleRadius(10f);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(false);
        mChart.setHighlightPerTapEnabled(false);

        mChart.setDrawSliceText(false);

        setData();

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setTextSize(12f);
        l.setXEntrySpace(8f);
        l.setYEntrySpace(4f);
        l.setYOffset(0f);

        mChart.setEntryLabelColor(Color.WHITE);
        mChart.setEntryLabelTextSize(12f);
    }
    public abstract ArrayList<String> setXAxisValues();

    public abstract ArrayList<PieEntry> setYAxisValues();

    public void setData() {
        final ArrayList<String> xVals = setXAxisValues();

        final ArrayList<PieEntry> yVals = setYAxisValues();

        PieDataSet set1;

        set1 = new PieDataSet(yVals,"");

        set1.setDrawIcons(false);

        set1.setSliceSpace(3f);
        set1.setIconsOffset(new MPPointF(0, 40));
        set1.setSelectionShift(5f);

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        set1.setColors(colors);

        PieData data = new PieData(set1);

        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(14f);
        data.setValueTypeface(Typeface.DEFAULT_BOLD);
        data.setValueTextColor(Color.WHITE);

        mChart.setData(data);

        mChart.invalidate();
    }
    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getY() + ", index: " + h.getX()
                        + ", DataSet index: " + h.getDataSetIndex());
    }
    public void onNothingSelected() {
        Log.i("Nothing selected", "Nothing selected.");
    }
}
