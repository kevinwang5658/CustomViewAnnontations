package com.kevin.wang.test;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.kevin.wang.cva.annotations.CustomView;

@CustomView
public class CustomView1 extends FrameLayout {

    public CustomView1(Context context) {
        super(context);
    }

    public CustomView1(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomView1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
