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
 * Created by w on 2017-11-09.
 */

public class ReferenceView extends LinearLayout {
    private TextView tx;
    private ImageView imageView;

    public ReferenceView(Context context) {
        super(context);
        initView();
    }
    public ReferenceView(Context context,AttributeSet attrs) {
        super(context,attrs);
        initView();
        getAttrs(attrs);
    }
    public ReferenceView(Context context,AttributeSet attrs,int defStyle) {
        super(context,attrs);
        initView();
        getAttrs(attrs,defStyle);
    }

    private void initView() {
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
        View v = li.inflate(R.layout.reference_time, this, false);
        addView(v);

        tx = (TextView) findViewById(R.id.referenceText);
        imageView = (ImageView) findViewById(R.id.referenceView);
    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ReferenceTimeImage);
        setTypeArray(typedArray);
    }
    private void getAttrs(AttributeSet attrs, int defStyle) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ReferenceTimeImage, defStyle, 0);
        setTypeArray(typedArray);
    }
    private void setTypeArray(TypedArray typedArray) {
        String text = typedArray.getString(R.styleable.ReferenceTimeImage_TimeText);
        tx.setText(text);

        typedArray.recycle();
    }

    public void setText(String text) {
        this.tx.setText(text);
    }

    public String getText() {
        return tx.getText().toString();
    }
}
