package com.kevin.wang.customviewannontations;

import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflater = LayoutInflater.from(this).cloneInContext(this);
        inflater.setFactory2(new CustomInflaterFactory());

        setContentView(R.layout.activity_main);
    }

    @NonNull
    @Override
    public LayoutInflater getLayoutInflater() {
        return inflater;
    }

    @Override
    public Object getSystemService(String name) {
        if (name.equals(LAYOUT_INFLATER_SERVICE)) {
            if (inflater== null) {
                inflater = (LayoutInflater) super.getSystemService(name);
            }
            return inflater;
        }
        return super.getSystemService(name);
    }
}
