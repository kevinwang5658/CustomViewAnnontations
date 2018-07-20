package com.kevin.wang.customviewannontations;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.kevin.wang.cva.annotations.CustomView;

@CustomView
public class CustomViewGroup1 extends RelativeLayout {
    public CustomViewGroup1(Context context) {
        super(context);
    }

    public CustomViewGroup1(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomViewGroup1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
