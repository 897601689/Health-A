package com.health_a.activity.test;

import android.app.Activity;
import android.os.Bundle;

import com.health_a.R;

import butterknife.ButterKnife;

/**
 * Created by Admin on 2016/8/2.血压检测
 */
public class BpActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bp);
        ButterKnife.bind(this);
        init();
    }

    private void init() {

    }
}
