package com.kevin.wang.cva;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;

import androidx.core.view.LayoutInflaterCompat;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class CustomViewAnnotations {
    private CustomViewAnnotations(){}

    public static LayoutInflater init(Activity activity){
        LayoutInflater inflater = LayoutInflater.from(activity).cloneInContext(activity);
        LayoutInflaterCompat.setFactory2(inflater, new CustomInflaterFactory(context));
        return inflater;
    }
}
