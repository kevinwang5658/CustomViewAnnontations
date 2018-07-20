package com.kevin.wang.test;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.kevin.wang.cva.annotations.CustomView;

@CustomView
public class CustomView2 extends View {
    public CustomView2(Context context) {
        super(context);
    }

    public CustomView2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
