package com.health_a.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.health_a.R;
import com.health_a.activity.test.EcgActivity;
import com.health_a.bean.UserInfo;
import com.health_a.dao.DBOperation;
import com.health_a.dialog.ActionSheetDialog;
import com.health_a.dialog.ActionSheetDialog.OnSheetItemClickListener;
import com.health_a.dialog.ActionSheetDialog.SheetItemColor;
import com.health_a.parsing.Mcu_Parsing;
import com.health_a.parsing.Spo2_Parsing;
import com.health_a.util.Global;
import com.health_a.util.MySurfaceView;
import com.health_a.util.Utils;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Admin on 2016/7/27. 主界面
 */
public class MainActivity extends Activity {

    //region控件
    @BindView(R.id.m_name)
    TextView mName;
    @BindView(R.id.m_sex)
    TextView mSex;
    @BindView(R.id.m_age)
    TextView mAge;
    @BindView(R.id.m_scan)
    TextView mScan;

    @BindView(R.id.bp_text)
    TextView bpText;
    @BindView(R.id.txt_mode)
    TextView txtMode;
    @BindView(R.id.txt_high_bp)
    TextView txtHighBp;
    @BindView(R.id.txt_low_bp)
    TextView txtLowBp;
    @BindView(R.id.txt_bp)
    TextView txtBp;
    @BindView(R.id.txt_bp_state)
    TextView txtBpState;

    @BindView(R.id.txt_spo2_high)
    TextView txtSpo2High;
    @BindView(R.id.txt_spo2_low)
    TextView txtSpo2Low;
    @BindView(R.id.txt_spo2)
    TextView txtSpo2;
    @BindView(R.id.txt_pulse_high)
    TextView txtPulseHigh;
    @BindView(R.id.txt_pulse_low)
    TextView txtPulseLow;
    @BindView(R.id.txt_pulse)
    TextView txtPulse;

    @BindView(R.id.txt_resp_high)
    TextView txtRespHigh;
    @BindView(R.id.txt_resp_low)
    TextView txtRespLow;
    @BindView(R.id.txt_resp)
    TextView txtResp;

    @BindView(R.id.txt_temp_high)
    TextView txtTempHigh;
    @BindView(R.id.txt_temp_low)
    TextView txtTempLow;
    @BindView(R.id.txt_temp)
    TextView txtTemp;

    @BindView(R.id.ecg_info)
    TextView ecgInfo;
    @BindView(R.id.txt_ecg_high)
    TextView txtEcgHigh;
    @BindView(R.id.txt_ecg_low)
    TextView txtEcgLow;
    @BindView(R.id.txt_ecg)
    TextView txtEcg;

    @BindView(R.id.m_save)
    TextView mSave;
    @BindView(R.id.m_user_info)
    TextView mUserInfo;
    @BindView(R.id.m_test_info)
    TextView mTestInfo;
    @BindView(R.id.m_sys_info)
    TextView mSysInfo;
    @BindView(R.id.ecg)
    MySurfaceView ecg_Curve;
    @BindView(R.id.btn_last)
    TextView btnLast;
    @BindView(R.id.btn_next)
    TextView btnNext;
    @BindView(R.id.ecg_lead)
    TextView ecgLead;

    //endregion

    private String UserID = null;//当前进行检测用户
    private String DoctorID = null;//当前检测医生
    private DBOperation db;
    private int lead_index = 0;
    Spo2_Parsing spo2 = new Spo2_Parsing(); //血氧协议解析
    Mcu_Parsing mcu = new Mcu_Parsing();//单片机协议解析
    byte[] tempCmd = new byte[]{0x51, (byte) 0x80, (byte) 0x81};//2K体温探头
    private String lead;//当前导联

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);   //应用运行时，保持屏幕高亮，不锁屏
        ButterKnife.bind(this);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!"".equals(Global.UserID)) {
            Cursor cursor = db.GetUserByCardId(Global.UserID);
            if (cursor != null) {
                if (cursor.moveToNext()) {
                    mName.setText(cursor.getString(1));
                    mSex.setText(cursor.getString(2));
                    mAge.setText(cursor.getString(3));
                }
                cursor.close();
            }
        }else{
            mName.setText("");
            mSex.setText("");
            mAge.setText("");
        }
    }

    private void init() {
        ecg_Curve.setBackColor(Color.rgb(77, 77, 77));//设置背景颜色
        ecg_Curve.setPen(Color.GREEN);//设置背景颜色
        db = new DBOperation(MainActivity.this);
        //DoctorID = Global.GetDoctorInfo();//获得登录医生信息
        try {
            Global.ecg_Com.Open("/dev/ttyMT1", 115200);
            Global.spo2_Com.Open("/dev/ttyMT3", 4800);
            Global.mcu_Com.Open("/dev/ttyMT2", 38400);
            Global.ecg_Com.Write(tempCmd);//2K体温探头
            Log.e("MainActivity", "串口打开成功！");
        } catch (Exception ex) {
            Log.e("MainActivity", "串口打开失败！");
        }

        new Thread(new Spo2Thread()).start();
        new Thread(new EcgThread()).start();
        new Thread(new McuThread()).start();
    }

    @OnClick({R.id.m_scan, R.id.bp_text, R.id.btn_last, R.id.btn_next, R.id.m_save, R.id.ecg_info, R.id.m_user_info, R.id.m_test_info, R.id.m_sys_info})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.m_scan:
                byte[] cmd_find = {(byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x96, 0x69, 0x00, 0x03, 0x20, 0x01, 0x22};

                new Thread(new IDCardThread()).start();
                /*try {
                    Global.mcu_Com.Write(cmd_find);
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                //Mcu_Parsing.SendIDCmd(Global.mcu_Com, cmd_find);//开始寻卡
                break;
            case R.id.bp_text:
                Mcu_Parsing.SendBpCmd(Global.mcu_Com, Mcu_Parsing.bp_Start);//开始手动测量
                txtBpState.setText("测量中");
                break;
            case R.id.btn_last:
                lead_index--;
                if (lead_index < 0)
                    lead_index = 11;
                Log.e("MainActivity", "" + lead_index);
                break;
            case R.id.btn_next:
                lead_index++;
                if (lead_index > 11)
                    lead_index = 0;
                Log.e("MainActivity", "" + lead_index);
                break;
            case R.id.m_save:
                new ActionSheetDialog(MainActivity.this)
                        .builder()
                        .setCancelable(false)
                        .setCanceledOnTouchOutside(false)
                        .addSheetItem("血压菜单", SheetItemColor.Blue,
                                new OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        Intent intent = new Intent(MainActivity.this, MenuBpActivity.class);
                                        startActivity(intent);
                                    }
                                })
                        .addSheetItem("心电菜单", SheetItemColor.Blue,
                                new OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        Intent intent = new Intent(MainActivity.this, MenuEcgActivity.class);
                                        startActivity(intent);
                                    }
                                }).show();
                break;
            case R.id.ecg_info:
                Global.isEcgAll = true;
                intent = new Intent(this, EcgActivity.class);
                startActivity(intent);
                break;
            case R.id.m_user_info:
                intent = new Intent(this, UserInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.m_test_info:
                intent = new Intent(this, TestActivity.class);
                startActivity(intent);
                break;
            case R.id.m_sys_info:
                intent = new Intent(this, SystemActivity.class);
                startActivity(intent);
                break;
        }
    }

    //region 消息线程处理
    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 101://心电
                    txtEcg.setText(String.valueOf(Global.ecg.getEcg()));
                    txtResp.setText(String.valueOf(Global.ecg.getResp()));
                    float temp = Global.ecg.getTempData1() / 10f;
                    if (temp != 0.1f && temp != 55) {
                        txtTemp.setText(String.valueOf(temp));
                    } else {
                        txtTemp.setText("--");
                    }
                    ecgLead.setText(lead);
                    break;
                case 102://身份证
                    //mName.setText(mcu.getIdInfo()[0].trim());
                    //mSex.setText(mcu.getIdInfo()[1]);
                    //mAge.setText(String.valueOf(Utils.getAge(mcu.getIdInfo()[5])));
                    AddOrShowUserInfo();
                    break;
                case 201://血氧
                    txtSpo2.setText(String.valueOf(spo2.getSpo2_value()));
                    txtPulse.setText(String.valueOf(spo2.getPulse_value()));
                    //Log.e("SPO2", "" + spo2.getSpo2_value());
                    //Log.e("pulse", "" + spo2.getPulse_value());
                    break;
                case 301://血压
                    txtBp.setText(mcu.getBp_H() + "/" + mcu.getBp_L());
                    txtBpState.setText(mcu.getBp_Error());
                    if ("手动".equals(mcu.getBp_Test()) || "连续测量模式".equals(mcu.getBp_Test())) {
                        txtMode.setText(mcu.getBp_Test());
                    } else {
                        txtMode.setText("自动测量" + mcu.getBp_Test() + "分钟");
                    }
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    //endregion

    //心电线程
    public class EcgThread implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(1000);
                Global.ecg_Com.Write(tempCmd);//2K体温探头
                Thread.sleep(100);
                Global.ecg_Com.Write(tempCmd);//2K体温探头
                while (true) {
                    Thread.sleep(5);
                    Global.ecg.Parsing(Global.ecg_Com);
                    if (Global.isEcgAll) {
                        continue;
                    }
                    SetEcgData();

                    // 发送这个消息到消息队列中
                    mHandler.sendEmptyMessage(101);
                }
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }

        private void SetEcgData() {

            //region 判断画哪条曲线

            List<Integer> data;

            switch (lead_index) {
                case 0:
                    lead = "I";
                    data = Global.ecg.getEcg_data().ECG_I;
                    break;
                case 1:
                    lead = "II";
                    data = Global.ecg.getEcg_data().ECG_II;
                    break;
                case 2:
                    lead = "III";
                    data = Global.ecg.getEcg_data().ECG_III;
                    break;
                case 3:
                    lead = "AVR";
                    data = Global.ecg.getEcg_data().ECG_AVR;
                    break;
                case 4:
                    lead = "AVL";
                    data = Global.ecg.getEcg_data().ECG_AVL;
                    break;
                case 5:
                    lead = "AVF";
                    data = Global.ecg.getEcg_data().ECG_AVF;
                    break;
                case 6:
                    lead = "V1";
                    data = Global.ecg.getEcg_data().ECG_V1;
                    break;
                case 7:
                    lead = "V2";
                    data = Global.ecg.getEcg_data().ECG_V2;
                    break;
                case 8:
                    lead = "V3";
                    data = Global.ecg.getEcg_data().ECG_V3;
                    break;
                case 9:
                    lead = "V4";
                    data = Global.ecg.getEcg_data().ECG_V4;
                    break;
                case 10:
                    lead = "V5";
                    data = Global.ecg.getEcg_data().ECG_V5;
                    break;
                case 11:
                    lead = "V6";
                    data = Global.ecg.getEcg_data().ECG_V6;
                    break;
                default:
                    lead = "I";
                    data = Global.ecg.getEcg_data().ECG_I;
                    break;
            }
            //endregion

            ecg_Curve.setInfo("");
            ecg_Curve.setTextSize(20);
            ecg_Curve.setAmplitude(75);
            if (data.size() > 0) {
                for (int i : data) {
                    ecg_Curve.setCurve(i);
                }
            } else {
                ecg_Curve.setCurve(-1);
            }
        }

    }

    //血氧线程
    public class Spo2Thread implements Runnable {

        @Override
        public void run() {
            try {
                while (true) {
                    Thread.sleep(10);
                    spo2.Parsing(Global.spo2_Com);
                    mHandler.sendEmptyMessage(201);
                }
            } catch (Exception ex) {
                //ex.printStackTrace();
            }

        }
    }

    //单片机线程
    public class McuThread implements Runnable {

        @Override
        public void run() {
            try {
                while (true) {
                    Thread.sleep(10);
                    mcu.Parsing(Global.mcu_Com);
                    key_DataProcess();
                    mHandler.sendEmptyMessage(301);
                }
            } catch (Exception ex) {
                //ex.printStackTrace();
            }
        }
    }

    //按键命令处理
    private void key_DataProcess() {
        String cmd = mcu.getKey();
        if (cmd != null) {
            switch (cmd) {
                case "关机":
                    Mcu_Parsing.SendMcuCmd(Global.mcu_Com, Mcu_Parsing.DeviceOFF);//关机
                    try {
                        Runtime.getRuntime().exec("su -c \"/system/bin/shutdown\"");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "按键-冻结":
                    break;
                case "按键-静音":
                    break;
                case "按键-血压":
                    Mcu_Parsing.SendBpCmd(Global.mcu_Com, Mcu_Parsing.bp_Start);//开始手动测量
                    break;
                case "按键-菜单":
                    break;
            }
        }
        cmd = mcu.getBattery();
        if (cmd != null) {
            switch (cmd) {
                case "电池-充满":
                    break;
                case "电池-未充满":
                    break;
                case "电池-电量高":
                    break;
                case "电池-电量中":
                    break;
                case "电池-电量低":
                    break;
                case "电池-欠压报警":
                    break;
            }
        }
    }

    //身份证读取线程
    public class IDCardThread implements Runnable {

        @Override
        public void run() {
            Mcu_Parsing.SendIDCmd(Global.mcu_Com, Mcu_Parsing.cmd_find);//开始寻卡
            try {
                Thread.sleep(100);
                if ("寻卡成功".equals(mcu.getIdState1())) {
                    Mcu_Parsing.SendIDCmd(Global.mcu_Com, Mcu_Parsing.cmd_selt);//开始选卡
                    Thread.sleep(100);
                    if ("选卡成功".equals(mcu.getIdState2())) {
                        Mcu_Parsing.SendIDCmd(Global.mcu_Com, Mcu_Parsing.cmd_read);//开始读卡
                        Thread.sleep(1500);

                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mHandler.sendEmptyMessage(102);

        }
    }

    private void AddOrShowUserInfo(){

        Cursor cursor = db.GetUserByCardId(mcu.getIdInfo()[5]);
        if (cursor == null) {
            UserInfo user = new UserInfo();
            user.setIDCard(mcu.getIdInfo()[5]);//保存用户身份证号
            user.setName(mcu.getIdInfo()[0].trim());
            user.setAge(String.valueOf(Utils.getAge(mcu.getIdInfo()[5])));
            user.setSex(mcu.getIdInfo()[1]);
            user.setPhone("");
            db.AddUserInfo(user);
            mName.setText(mcu.getIdInfo()[0].trim());
            mSex.setText(mcu.getIdInfo()[1]);
            mAge.setText(String.valueOf(Utils.getAge(mcu.getIdInfo()[5])));

        } else {
            if (cursor.moveToNext()) {
                mName.setText(cursor.getString(1));
                mSex.setText(cursor.getString(2));
                mAge.setText(cursor.getString(3));
            }
        }
        Global.UserID = mcu.getIdInfo()[5];
    }
}
