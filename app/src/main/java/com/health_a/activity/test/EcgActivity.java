package com.health_a.activity.test;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.health_a.R;
import com.health_a.activity.MenuEcgActivity;
import com.health_a.dao.DBOperation;
import com.health_a.parsing.Ecg_Parsing;
import com.health_a.util.Global;
import com.health_a.util.MySurfaceView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Admin on 2016/8/2. 心电检测
 */
public class EcgActivity extends Activity {

    @BindView(R.id.ecg_back)
    ImageView ecgBack;
    @BindView(R.id.ecg_name_txt)
    TextView ecgNameTxt;
    @BindView(R.id.ecg_sex_txt)
    TextView ecgSexTxt;
    @BindView(R.id.ecg_age_txt)
    TextView ecgAgeTxt;
    @BindView(R.id.ecg_info_layout)
    LinearLayout ecgInfoLayout;
    @BindView(R.id.ecg_txt)
    TextView ecgTxt;
    @BindView(R.id.resp_txt)
    TextView respTxt;
    @BindView(R.id.btn_ecg_gain)
    TextView btnEcgGain;
    @BindView(R.id.ecg_time_txt)
    TextView ecgTimeTxt;

    @BindView(R.id.ecg_I)
    MySurfaceView ecgI;
    @BindView(R.id.ecg_II)
    MySurfaceView ecgII;
    @BindView(R.id.ecg_III)
    MySurfaceView ecgIII;
    @BindView(R.id.ecg_AVR)
    MySurfaceView ecgAVR;
    @BindView(R.id.ecg_AVL)
    MySurfaceView ecgAVL;
    @BindView(R.id.ecg_AVF)
    MySurfaceView ecgAVF;
    @BindView(R.id.ecg_V1)
    MySurfaceView ecgV1;
    @BindView(R.id.ecg_V2)
    MySurfaceView ecgV2;
    @BindView(R.id.ecg_V3)
    MySurfaceView ecgV3;
    @BindView(R.id.ecg_V4)
    MySurfaceView ecgV4;
    @BindView(R.id.ecg_V5)
    MySurfaceView ecgV5;
    @BindView(R.id.ecg_V6)
    MySurfaceView ecgV6;
    @BindView(R.id.txt_ra)
    TextView txtRa;
    @BindView(R.id.txt_ll)
    TextView txtLl;
    @BindView(R.id.txt_la)
    TextView txtLa;
    @BindView(R.id.txt_v1)
    TextView txtV1;
    @BindView(R.id.txt_v2)
    TextView txtV2;
    @BindView(R.id.txt_v3)
    TextView txtV3;
    @BindView(R.id.txt_v4)
    TextView txtV4;
    @BindView(R.id.txt_v5)
    TextView txtV5;
    @BindView(R.id.txt_v6)
    TextView txtV6;
    @BindView(R.id.txt_st1)
    TextView txtSt1;
    @BindView(R.id.txt_st2)
    TextView txtSt2;
    @BindView(R.id.txt_st3)
    TextView txtSt3;
    @BindView(R.id.txt_st4)
    TextView txtSt4;
    @BindView(R.id.txt_st5)
    TextView txtSt5;
    @BindView(R.id.txt_st6)
    TextView txtSt6;
    @BindView(R.id.txt_st7)
    TextView txtSt7;
    @BindView(R.id.txt_st8)
    TextView txtSt8;

    private TimeThread time_thread = new TimeThread();
    private DrawThread drawThread = new DrawThread();
    private String[] gain = new String[]{"0.25", "0.5", "1", "2"};
    private Ecg_Parsing.ECG_DATA ecg_data;
    private Ecg_Parsing.LeadState leadState;
    private Ecg_Parsing.ECG_ST ecg_st;
    private DBOperation db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecg);
        ButterKnife.bind(this);
        init();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Global.isEcgAll = false;
    }

    private void init() {
        db = new DBOperation(EcgActivity.this);
        if (!"".equals(Global.UserID)) {
            Cursor cursor = db.GetUserByCardId(Global.UserID);
            if (cursor != null) {
                if (cursor.moveToNext()) {
                    ecgNameTxt.setText(cursor.getString(1));
                    ecgSexTxt.setText(cursor.getString(2));
                    ecgAgeTxt.setText(cursor.getString(3));
                }
                cursor.close();
            }
        }
        ecgI.setInfo("I");
        ecgII.setInfo("II");
        ecgIII.setInfo("III");
        ecgAVR.setInfo("AVR");
        ecgAVL.setInfo("AVL");
        ecgAVF.setInfo("AVF");
        ecgV1.setInfo("V1");
        ecgV2.setInfo("V2");
        ecgV3.setInfo("V3");
        ecgV4.setInfo("V4");
        ecgV5.setInfo("V5");
        ecgV6.setInfo("V6");

        time_thread.start(); //启动时间线程
        drawThread.start();//
    }

    /**
     * 按钮点击事件
     *
     * @param view 按钮
     */
    @OnClick({R.id.ecg_back, R.id.btn_ecg_gain})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ecg_back:
                finish();
                break;
            case R.id.btn_ecg_gain:
                if (Global.ecg_gain >= gain.length - 1)
                    Global.ecg_gain = 0;
                else
                    Global.ecg_gain++;

                btnEcgGain.setText("切换增益：" + gain[Global.ecg_gain]);
                MenuEcgActivity.SendCmd(MenuEcgActivity.gain_I_cmd.get(Global.ecg_gain));
                MenuEcgActivity.SendCmd(MenuEcgActivity.gain_II_cmd.get(Global.ecg_gain));
                MenuEcgActivity.SendCmd(MenuEcgActivity.gain_III_cmd.get(Global.ecg_gain));
                break;
        }
    }

    /**
     * 更新时间线程
     */
    class TimeThread extends Thread {
        @Override
        public void run() {

            try {
                while (true) {
                    Thread.sleep(1000);
                    mHandler.sendEmptyMessage(1);
                }
            } catch (InterruptedException e) {
                //e.printStackTrace();
            }

        }
    }

    //region 线程事件处理
    //在主线程里面处理消息并更新UI界面
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    long sysTime = System.currentTimeMillis();
                    CharSequence sysTimeStr = DateFormat.format("yyyy-MM-dd HH:mm:ss", sysTime);
                    ecgTimeTxt.setText(sysTimeStr); //更新时间
                    break;
                case 2:
                    ecgTxt.setText(String.valueOf(Global.ecg.getEcg()));
                    respTxt.setText(String.valueOf(Global.ecg.getResp()));

                    if (leadState != null) {
                        txtLl.setText(leadState.LL == 0 ? "on" : "off");
                        txtLa.setText(leadState.LA == 0 ? "on" : "off");
                        txtRa.setText(leadState.RA == 0 ? "on" : "off");
                        txtV1.setText(leadState.V1 == 0 ? "on" : "off");
                        txtV2.setText(leadState.V2 == 0 ? "on" : "off");
                        txtV3.setText(leadState.V3 == 0 ? "on" : "off");
                        txtV4.setText(leadState.V4 == 0 ? "on" : "off");
                        txtV5.setText(leadState.V5 == 0 ? "on" : "off");
                        txtV6.setText(leadState.V6 == 0 ? "on" : "off");
                    }
                    if (ecg_st != null) {
                        txtSt1.setText(String.valueOf(ecg_st.ST1));
                        txtSt2.setText(String.valueOf(ecg_st.ST2));
                        txtSt3.setText(String.valueOf(ecg_st.ST3));
                        txtSt4.setText(String.valueOf(ecg_st.ST4));
                        txtSt5.setText(String.valueOf(ecg_st.ST5));
                        txtSt6.setText(String.valueOf(ecg_st.ST6));
                        txtSt7.setText(String.valueOf(ecg_st.ST7));
                        txtSt8.setText(String.valueOf(ecg_st.ST8));
                    }
                    break;
                default:
                    break;
            }
        }
    };
    //endregion

    class DrawThread extends Thread {
        @Override
        public void run() {
            try {
                Thread.sleep(1500);
                while (Global.isEcgAll) {
                    Thread.sleep(10);
                    mHandler.sendEmptyMessage(2);
                    /*if (!Global.isEcgAll) {
                        continue;
                    }*/
                    leadState = Global.ecg.getLeadState();  //导联状态
                    ecg_st = Global.ecg.getEcg_st();        //ST值

                    if (Global.ecg.getEcg_data() != null) {
                        ecg_data = Global.ecg.getEcg_data();
                    } else {
                        continue;
                    }

                    if (ecg_data.ECG_I != null && ecg_data.ECG_I.size() > 0) {
                        for (int i : ecg_data.ECG_I) {
                            ecgI.setCurve(i);
                        }
                    } else {
                        ecgI.setCurve(-1);
                    }
                    if (ecg_data.ECG_II != null && ecg_data.ECG_II.size() > 0) {
                        for (int i : ecg_data.ECG_II) {
                            ecgII.setCurve(i);
                        }
                    } else {
                        ecgII.setCurve(-1);
                    }
                    if (ecg_data.ECG_III != null && ecg_data.ECG_III.size() > 0) {
                        for (int i : ecg_data.ECG_III) {
                            ecgIII.setCurve(i);
                        }
                    } else {
                        ecgII.setCurve(-1);
                    }
                    if (ecg_data.ECG_AVR != null && ecg_data.ECG_AVR.size() > 0) {
                        for (int i : ecg_data.ECG_AVR) {
                            ecgAVR.setCurve(i);
                        }
                    } else {
                        ecgAVR.setCurve(-1);
                    }
                    if (ecg_data.ECG_AVL != null && ecg_data.ECG_AVL.size() > 0) {
                        for (int i : ecg_data.ECG_AVL) {
                            ecgAVL.setCurve(i);
                        }
                    } else {
                        ecgAVL.setCurve(-1);
                    }
                    if (ecg_data.ECG_AVF != null && ecg_data.ECG_AVF.size() > 0) {
                        for (int i : ecg_data.ECG_AVF) {
                            ecgAVF.setCurve(i);
                        }
                    } else {
                        ecgAVF.setCurve(-1);
                    }
                    if (ecg_data.ECG_V1 != null && ecg_data.ECG_V1.size() > 0) {
                        for (int i : ecg_data.ECG_V1) {
                            ecgV1.setCurve(i);
                        }
                    } else {
                        ecgV1.setCurve(-1);
                    }
                    if (ecg_data.ECG_V2 != null && ecg_data.ECG_V2.size() > 0) {
                        for (int i : ecg_data.ECG_V2) {
                            ecgV2.setCurve(i);
                        }
                    } else {
                        ecgV2.setCurve(-1);
                    }
                    if (ecg_data.ECG_V3 != null && ecg_data.ECG_V3.size() > 0) {
                        for (int i : ecg_data.ECG_V3) {
                            ecgV3.setCurve(i);
                        }
                    } else {
                        ecgV3.setCurve(-1);
                    }
                    if (ecg_data.ECG_V4 != null && ecg_data.ECG_V4.size() > 0) {
                        for (int i : ecg_data.ECG_V4) {
                            ecgV4.setCurve(i);
                        }
                    } else {
                        ecgV4.setCurve(-1);
                    }
                    if (ecg_data.ECG_V5 != null && ecg_data.ECG_V5.size() > 0) {
                        for (int i : ecg_data.ECG_V5) {
                            ecgV5.setCurve(i);
                        }
                    } else {
                        ecgV5.setCurve(-1);
                    }
                    if (ecg_data.ECG_V6 != null && ecg_data.ECG_V6.size() > 0) {
                        for (int i : ecg_data.ECG_V6) {
                            ecgV6.setCurve(i);
                        }
                    } else {
                        ecgV6.setCurve(-1);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }


}
