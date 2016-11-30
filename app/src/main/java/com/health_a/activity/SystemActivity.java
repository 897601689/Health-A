package com.health_a.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.health_a.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Admin on 2016/8/2. 系统维护
 */
public class SystemActivity extends Activity {

    @BindView(R.id.sys_back)
    ImageView sysBack;
    @BindView(R.id.sys_server)
    TextView sysServer;
    @BindView(R.id.sys_upload)
    TextView sysUpload;
    @BindView(R.id.sys_timer)
    TextView sysTimer;
    @BindView(R.id.sys_export)
    TextView sysExport;
    @BindView(R.id.sys_password)
    TextView sysPassword;
    @BindView(R.id.sys_download)
    TextView sysDownload;
    @BindView(R.id.sys_doctor)
    TextView sysDoctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.sys_back)
    protected void sysBack() {
        finish();
    }

    @OnClick(R.id.sys_server)
    protected void sysServer() {
    }

    @OnClick(R.id.sys_upload)
    protected void sysUpload() {
    }

    @OnClick(R.id.sys_timer)
    protected void sysTimer() {
    }

    @OnClick(R.id.sys_export)
    protected void sysExport() {
    }

    @OnClick(R.id.sys_password)
    protected void sysPassword() {
    }

    @OnClick(R.id.sys_download)
    protected void sysDownload() {
    }

    @OnClick(R.id.sys_doctor)
    protected void sysDoctor() {
        Intent intent = new Intent(this, DoctorInfoActivity.class);
        startActivity(intent);
    }

}
