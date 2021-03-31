package com.hci.happycaster.CustomView;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hci.happycaster.R;

/**
 * Created by w on 2017-10-31.
 */

public class HappyRanking1Image extends LinearLayout {
    TextView tx;
    ImageView Heart, Arrow;

    public HappyRanking1Image(Context context) {
        super(context);
        initView();
    }
    public HappyRanking1Image(Context context, AttributeSet attrs) {
        super(context,attrs);
        initView();
        getAttrs(attrs);
    }
    public HappyRanking1Image(Context context, AttributeSet attrs, int defStyle) {
        super(context,attrs);
        initView();
        getAttrs(attrs,defStyle);
    }

    private void initView() {
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
        View v = li.inflate(R.layout.happy_ranking1, this, false);
        addView(v);

        tx = (TextView) findViewById(R.id.location_Text1);
        Heart = (ImageView) findViewById(R.id.heartImageView1);
        Arrow = (ImageView) findViewById(R.id.happyranking1);
    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.HappyRanking1Image);
        setTypeArray(typedArray);
    }
    private void getAttrs(AttributeSet attrs, int defStyle) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.HappyRanking1Image, defStyle, 0);
        setTypeArray(typedArray);
    }

    private void setTypeArray(TypedArray typedArray) {
        int textColor = typedArray.getColor(R.styleable.HappyRanking1Image_TextColor,0);
        tx.setTextColor(textColor);

        String text = typedArray.getString(R.styleable.HappyRanking1Image_Text);
        tx.setText(text);

        int HeartimageID = typedArray.getResourceId(R.styleable.HappyRanking1Image_HeartSrc,R.drawable.loc_heart1);
        Heart.setImageResource(HeartimageID);

        int ArrowimageID = typedArray.getResourceId(R.styleable.HappyRanking1Image_ImageSrc,R.drawable.loc_1st_arrow1);
        Arrow.setImageResource(ArrowimageID);

        typedArray.recycle();
    }

    public void setTextColor(int text) {
        tx.setTextColor(text);
    }

    public void setArrow(int arrow) {
        Arrow.setImageResource(arrow);
    }

    public void setHeart(int heart) {
        Heart.setImageResource(heart);
    }

    public void setText(String tx) {
        this.tx.setText(tx);
    }
}
