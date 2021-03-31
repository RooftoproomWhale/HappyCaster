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
 * Created by w on 2017-10-26.
 */

public class HappyAndRankingView extends LinearLayout {

    TextView text,text2,text3,text4;

    public HappyAndRankingView(Context context) {
        super(context);
        initView();
    }

    public HappyAndRankingView(Context context,AttributeSet attrs) {
        super(context,attrs);
        initView();
        getAttrs(attrs);
    }

    public HappyAndRankingView(Context context,AttributeSet attrs,int defStyle) {
        super(context,attrs);
        initView();
        getAttrs(attrs,defStyle);
    }

    private void initView() {
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
        View v = li.inflate(R.layout.happyandkeyword, this, false);
        addView(v);

        text = (TextView) findViewById(R.id.perText);
        text2 = (TextView) findViewById(R.id.ranking1text);
        text3 = (TextView) findViewById(R.id.ranking2text);
        text4 = (TextView) findViewById(R.id.ranking3text);
    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.HappyAndRankingImage);
        setTypeArray(typedArray);
    }

    private void getAttrs(AttributeSet attrs, int defStyle) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.HappyAndRankingImage, defStyle, 0);
        setTypeArray(typedArray);
    }

    private void setTypeArray(TypedArray typedArray) {
        String text = typedArray.getString(R.styleable.HappyAndRankingImage_HappyText);
        this.text.setText(text);

        String text2 = typedArray.getString(R.styleable.HappyAndRankingImage_Ranking1Text);
        this.text2.setText(text2);

        String text3 = typedArray.getString(R.styleable.HappyAndRankingImage_Ranking2Text);
        this.text3.setText(text3);

        String text4 = typedArray.getString(R.styleable.HappyAndRankingImage_Ranking3Text);
        this.text4.setText(text4);

        typedArray.recycle();
    }
    public void setHappyText(String text) {
        this.text.setText(text);
    }

    public void setRanking1Text(String text) {
        this.text2.setText(text);
    }

    public void setRanking2Text(String text) {
        this.text3.setText(text);
    }

    public void setRanking3Text(String text) {
        this.text4.setText(text);
    }

}
