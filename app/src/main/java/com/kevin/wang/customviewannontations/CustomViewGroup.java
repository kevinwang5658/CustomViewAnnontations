package com.kevin.wang.customviewannontations;

import android.content.Context;
import android.util.AttributeSet;

import com.kevin.wang.cva.annotations.CustomView;

import androidx.constraintlayout.widget.ConstraintLayout;

@CustomView
public class CustomViewGroup extends ConstraintLayout {

    public CustomViewGroup(Context context) {
        super(context);
    }

    public CustomViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
