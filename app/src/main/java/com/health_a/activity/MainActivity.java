package com.health_a.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.health_a.R;
import com.health_a.activity.test.EcgActivity;
import com.health_a.dao.DBOperation;
import com.health_a.parsing.Mcu_Parsing;
import com.health_a.parsing.Spo2_Parsing;
import com.health_a.util.Global;
import com.health_a.util.MySurfaceView;

import java.io.IOException;

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
    //endregion

    private String UserID = null;//当前进行检测用户
    private String DoctorID = null;//当前检测医生
    private DBOperation db;

    Spo2_Parsing spo2 = new Spo2_Parsing(); //血氧协议解析
    Mcu_Parsing mcu = new Mcu_Parsing();//单片机协议解析


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);   //应用运行时，保持屏幕高亮，不锁屏
        ButterKnife.bind(this);
        Toast.makeText(this, "串口打开成功！", Toast.LENGTH_SHORT).show();
        init();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
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
        }
    }

    private void init() {
        ecg_Curve.setBackColor(Color.rgb(77, 77, 77));//设置背景颜色
        db = new DBOperation(MainActivity.this);
        //DoctorID = Global.GetDoctorInfo();//获得登录医生信息
        try {
            Global.ecg_Com.Open("/dev/ttyMT1", 115200);
            Global.spo2_Com.Open("/dev/ttyMT3", 4800);
            Global.mcu_Com.Open("/dev/ttyMT2", 38400);
            Global.ecg_Com.Write(new byte[]{0x51, (byte) 0x80, (byte) 0x81});//2K体温探头
            //Global.mcu_Com.Write(Mcu_Parsing.bp_Start);
            Toast.makeText(this, "串口打开成功！", Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            Toast.makeText(this, "串口打开失败！", Toast.LENGTH_SHORT).show();
        }

        new Thread(new Spo2Thread()).start();
        new Thread(new EcgThread()).start();
        new Thread(new McuThread()).start();
    }

    @OnClick({R.id.m_scan, R.id.bp_text, R.id.m_save, R.id.ecg_info, R.id.m_user_info, R.id.m_test_info, R.id.m_sys_info})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.m_scan:
                break;
            case R.id.bp_text:
                Mcu_Parsing.SendBpCmd(Global.mcu_Com, Mcu_Parsing.bp_Start);//开始手动测量
                txtBpState.setText("测量中");
                break;
            case R.id.m_save:
                Intent intent = new Intent(this, MenuBpActivity.class);
                startActivity(intent);
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
                case 101:
                    txtEcg.setText(String.valueOf(Global.ecg.getEcg()));
                    txtResp.setText(String.valueOf(Global.ecg.getResp()));
                    float temp = Global.ecg.getTempData1() / 10f;
                    if (temp != 0.1f && temp != 0) {
                        txtTemp.setText(String.valueOf(temp));
                    } else {
                        txtTemp.setText("--");
                    }
                    break;
                case 102:
                    break;
                case 201:
                    txtSpo2.setText(String.valueOf(spo2.getSpo2_value()));
                    txtPulse.setText(String.valueOf(spo2.getPulse_value()));
                    //Log.e("SPO2", "" + spo2.getSpo2_value());
                    //Log.e("pulse", "" + spo2.getPulse_value());
                    break;
                case 301:
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
                while (true) {
                    Thread.sleep(10);
                    Global.ecg.Parsing(Global.ecg_Com);
                    if (Global.isEcgAll) {
                        continue;
                    }
                    ecg_Curve.setInfo("I");
                    if (Global.ecg.getEcg_data().ECG_I.size() > 0) {
                        for (int i : Global.ecg.getEcg_data().ECG_I) {
                            ecg_Curve.setCurve(i);
                        }
                    } else {
                        ecg_Curve.setCurve(-1);
                    }
                    /*for (int i = 0; i < Global.ecg.getEcg_data().ECG_I.size(); i+=2) {
                        ecg_Curve.setCurve(Global.ecg.getEcg_data().ECG_I.get(i));
                    }*/

                    // 发送这个消息到消息队列中
                    mHandler.sendEmptyMessage(101);
                }
            } catch (Exception e) {
                //e.printStackTrace();
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
                ex.printStackTrace();
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
                ex.printStackTrace();
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
}
