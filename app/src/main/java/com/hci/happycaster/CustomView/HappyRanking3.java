package com.hci.happycaster.CustomView;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hci.happycaster.R;

/**
 * Created by w on 2017-10-31.
 */
public class HappyRanking3 extends LinearLayout {
    TextView text, text1, text2, text3, text4;

    public HappyRanking3(Context context) {
        super(context);
        initView();
    }
    public HappyRanking3(Context context, AttributeSet attrs) {
        super(context,attrs);
        initView();
        getAttrs(attrs);
    }
    public HappyRanking3(Context context, AttributeSet attrs, int defStyle) {
        super(context,attrs);
        initView();
        getAttrs(attrs,defStyle);
    }

    private void initView() {
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
        View v = li.inflate(R.layout.location_ranking3, this, false);
        addView(v);

        text = (TextView) findViewById(R.id.Monthtext3);
        text1 = (TextView) findViewById(R.id.happyPerText3);
        text2 = (TextView) findViewById(R.id.keyword3Ranking1);
        text3 = (TextView) findViewById(R.id.keyword3Ranking2);
        text4 = (TextView) findViewById(R.id.keyword3Ranking3);
    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.HappyLocRanking3Image);
        setTypeArray(typedArray);
    }
    private void getAttrs(AttributeSet attrs, int defStyle) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.HappyLocRanking3Image, defStyle, 0);
        setTypeArray(typedArray);
    }

    private void setTypeArray(TypedArray typedArray) {
        String text = typedArray.getString(R.styleable.HappyLocRanking3Image_MonthText3);
        this.text.setText(text);

        String text1 = typedArray.getString(R.styleable.HappyLocRanking3Image_HappyPerText3);
        this.text1.setText(text1);

        String text2 = typedArray.getString(R.styleable.HappyLocRanking3Image_LocRanking1Text3);
        this.text2.setText(text2);

        String text3 = typedArray.getString(R.styleable.HappyLocRanking3Image_LocRanking2Text3);
        this.text3.setText(text3);

        String text4 = typedArray.getString(R.styleable.HappyLocRanking3Image_LocRanking3Text3);
        this.text4.setText(text4);

        typedArray.recycle();
    }

    public void setMonthText(String text) {
        this.text.setText(text);
    }

    public void setHappyText(String text1) {
        this.text1.setText(text1);
    }

    public void setRanking1Text(String text1) {
        this.text2.setText(text1);
    }

    public void setRanking2Text(String text1) {
        this.text3.setText(text1);
    }

    public void setRanking3Text(String text1) {
        this.text4.setText(text1);
    }
}