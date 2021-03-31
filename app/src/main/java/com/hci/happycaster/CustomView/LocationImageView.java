package com.hci.happycaster.CustomView;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hci.happycaster.R;

/**
 * Created by w on 2017-10-25.
 */

public class LocationImageView extends LinearLayout {

    public TextView text;
    public ImageView Loc, weather, best;

    public LocationImageView(Context context) {
        super(context);
        initView();
    }
    public LocationImageView(Context context,AttributeSet attrs) {
        super(context,attrs);
        initView();
        getAttrs(attrs);
    }
    public LocationImageView(Context context,AttributeSet attrs,int defStyle) {
        super(context,attrs);
        initView();
        getAttrs(attrs,defStyle);
    }

    private void initView() {
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
        View v = li.inflate(R.layout.locationimage, this, false);
        addView(v);

        text = (TextView) findViewById(R.id.Loctext);
        Loc = (ImageView) findViewById(R.id.Locimage);
        weather = (ImageView) findViewById(R.id.weatheriamge);
        best = (ImageView)findViewById(R.id.BestView);
    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.LocationImage);
        setTypeArray(typedArray);
    }
    private void getAttrs(AttributeSet attrs, int defStyle) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.LocationImage, defStyle, 0);
        setTypeArray(typedArray);
    }
    private void setTypeArray(TypedArray typedArray) {
        int textColor = typedArray.getColor(R.styleable.LocationImage_LocTextColor,0);
        text.setTextColor(textColor);

        String text = typedArray.getString(R.styleable.LocationImage_LocText);
        this.text.setText(text);

        int LocimageID = typedArray.getResourceId(R.styleable.LocationImage_LocImageSrc,R.drawable.menu_realtime_seoul);
        Loc.setImageResource(LocimageID);

        int WeatherimageID = typedArray.getResourceId(R.styleable.LocationImage_WeatherImageSrc,R.drawable.menu_realtime_icon1);
        weather.setImageResource(WeatherimageID);

        int onOff = typedArray.getInt(R.styleable.LocationImage_LocBestImage,0);
        if(onOff == 0)
        {
            best.setVisibility(INVISIBLE);
        }
        else
        {
            best.setVisibility(VISIBLE);
        }

        typedArray.recycle();
    }

    public void setLoc(int loc) {
        Loc.setImageResource(loc);
    }

    public void setWeather(int weather) {
        this.weather.setImageResource(weather);
    }

    public void setText(String text) {
        this.text.setText(text);
    }

    public void setText(Double text) {
        this.text.setText(Double.toString(text));
    }


    public String getText() {
        return text.getText().toString();
    }

    public void setTextColor(int text) {
        this.text.setTextColor(text);
    }

    public void setBest(int a) {
        best.setVisibility(a);
    }
}
