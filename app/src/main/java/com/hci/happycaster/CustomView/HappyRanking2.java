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

public class HappyRanking2 extends LinearLayout {
    TextView text, text1, text2, text3, text4;

    public HappyRanking2(Context context) {
        super(context);
        initView();
    }
    public HappyRanking2(Context context, AttributeSet attrs) {
        super(context,attrs);
        initView();
        getAttrs(attrs);
    }
    public HappyRanking2(Context context, AttributeSet attrs, int defStyle) {
        super(context,attrs);
        initView();
        getAttrs(attrs,defStyle);
    }

    private void initView() {
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
        View v = li.inflate(R.layout.location_ranking2, this, false);
        addView(v);

        text = (TextView) findViewById(R.id.Monthtext2);
        text1 = (TextView) findViewById(R.id.happyPerText2);
        text2 = (TextView) findViewById(R.id.keyword2Ranking1);
        text3 = (TextView) findViewById(R.id.keyword2Ranking2);
        text4 = (TextView) findViewById(R.id.keyword2Ranking3);
    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.HappyLocRanking2Image);
        setTypeArray(typedArray);
    }
    private void getAttrs(AttributeSet attrs, int defStyle) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.HappyLocRanking2Image, defStyle, 0);
        setTypeArray(typedArray);
    }

    private void setTypeArray(TypedArray typedArray) {
        String text = typedArray.getString(R.styleable.HappyLocRanking2Image_MonthText2);
        this.text.setText(text);

        String text1 = typedArray.getString(R.styleable.HappyLocRanking2Image_HappyPerText2);
        this.text1.setText(text1);

        String text2 = typedArray.getString(R.styleable.HappyLocRanking2Image_LocRanking1Text2);
        this.text2.setText(text2);

        String text3 = typedArray.getString(R.styleable.HappyLocRanking2Image_LocRanking2Text2);
        this.text3.setText(text3);

        String text4 = typedArray.getString(R.styleable.HappyLocRanking2Image_LocRanking3Text2);
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

