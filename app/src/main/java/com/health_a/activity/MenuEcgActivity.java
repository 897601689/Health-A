package com.health_a.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.health_a.R;
import com.health_a.util.Global;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Admin on 2016/11/16. 心电菜单
 */
public class MenuEcgActivity extends Activity {

    @BindView(R.id.spinner_channel)
    Spinner spinnerChannel;
    @BindView(R.id.spinner_gain)
    Spinner spinnerGain;
    @BindView(R.id.spinner_filter)
    Spinner spinnerFilter;
    @BindView(R.id.spinner_notch_filter)
    Spinner spinnerNotchFilter;
    @BindView(R.id.rdoBtn_channel12)
    RadioButton rdoBtnChannel12;
    @BindView(R.id.rdoBtn_channel5)
    RadioButton rdoBtnChannel5;
    @BindView(R.id.rdoBtn_channel3)
    RadioButton rdoBtnChannel3;
    @BindView(R.id.rdoBtn_off)
    RadioButton rdoBtnOff;
    @BindView(R.id.rdoBtn_on)
    RadioButton rdoBtnOn;
    @BindView(R.id.rdoBtn_st_off)
    RadioButton rdoBtnStOff;
    @BindView(R.id.rdoBtn_st_on)
    RadioButton rdoBtnStOn;
    @BindView(R.id.rdoBtn_adult)
    RadioButton rdoBtnAdult;
    @BindView(R.id.rdoBtn_child)
    RadioButton rdoBtnChild;
    @BindView(R.id.channel)
    RadioGroup channel;
    @BindView(R.id.qibo)
    RadioGroup qibo;
    @BindView(R.id.st)
    RadioGroup st;

    //region 命令数组
    static byte[] notch_off = new byte[]{(byte) 0x4d, (byte) 0x80, (byte) 0x80};
    static byte[] notch_50 = new byte[]{(byte) 0x4d, (byte) 0x80, (byte) 0x90};
    static byte[] notch_60 = new byte[]{(byte) 0x4d, (byte) 0x80, (byte) 0x91};

    static byte[] gain_I_25 = new byte[]{(byte) 0x42, (byte) 0x80, (byte) 0x80};
    static byte[] gain_I_50 = new byte[]{(byte) 0x42, (byte) 0x80, (byte) 0x81};
    static byte[] gain_I_100 = new byte[]{(byte) 0x42, (byte) 0x80, (byte) 0x82};
    static byte[] gain_I_200 = new byte[]{(byte) 0x42, (byte) 0x80, (byte) 0x83};

    static byte[] gain_II_25 = new byte[]{(byte) 0x42, (byte) 0x80, (byte) 0x90};
    static byte[] gain_II_50 = new byte[]{(byte) 0x42, (byte) 0x80, (byte) 0x91};
    static byte[] gain_II_100 = new byte[]{(byte) 0x42, (byte) 0x80, (byte) 0x92};
    static byte[] gain_II_200 = new byte[]{(byte) 0x42, (byte) 0x80, (byte) 0x93};

    static byte[] gain_III_25 = new byte[]{(byte) 0x42, (byte) 0x80, (byte) 0xa0};
    static byte[] gain_III_50 = new byte[]{(byte) 0x42, (byte) 0x80, (byte) 0xa1};
    static byte[] gain_III_100 = new byte[]{(byte) 0x42, (byte) 0x80, (byte) 0xa2};
    static byte[] gain_III_200 = new byte[]{(byte) 0x42, (byte) 0x80, (byte) 0xa3};

    static byte[] filter_diagnosis = new byte[]{(byte) 0x41, (byte) 0x80, (byte) 0x83};
    static byte[] filter_monitor = new byte[]{(byte) 0x41, (byte) 0x80, (byte) 0x82};
    static byte[] filter_surgery = new byte[]{(byte) 0x41, (byte) 0x80, (byte) 0x81};
    static byte[] filter_strong = new byte[]{(byte) 0x41, (byte) 0x80, (byte) 0x84};

    public static List<byte[]> gain_I_cmd = new ArrayList<byte[]>(){{
        add(gain_I_25);
        add(gain_I_50);
        add(gain_I_100);
        add(gain_I_200);
    }};
    public static List<byte[]> gain_II_cmd = new ArrayList<byte[]>(){{
        add(gain_II_25);
        add(gain_II_50);
        add(gain_II_100);
        add(gain_II_200);
    }};
    public static List<byte[]> gain_III_cmd = new ArrayList<byte[]>(){{
        add(gain_III_25);
        add(gain_III_50);
        add(gain_III_100);
        add(gain_III_200);
    }};
    List<byte[]> notch_cmd = new ArrayList<byte[]>(){{
        add(notch_off);
        add(notch_50);
        add(notch_60);
    }};
    List<byte[]> filter_cmd = new ArrayList<byte[]>(){{
        add(filter_diagnosis);
        add(filter_monitor);
        add(filter_surgery);
        add(filter_strong);
    }};

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_ecg);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);   //应用运行时，保持屏幕高亮，不锁屏
        ButterKnife.bind(this);
        init();
    }

    private void init() {

        spinnerChannel.setSelection(0, true);    //
        spinnerGain.setSelection(2, true);       //
        spinnerFilter.setSelection(1, true);     //
        spinnerNotchFilter.setSelection(0, true);//
        //通道选择
        spinnerChannel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("1121212", "" + spinnerChannel.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //增益选择
        spinnerGain.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (spinnerChannel.getSelectedItemPosition()) {
                    case 0:
                        SendCmd(gain_I_cmd.get(position));
                        break;
                    case 1:
                        SendCmd(gain_II_cmd.get(position));
                        break;
                    case 2:
                        SendCmd(gain_III_cmd.get(position));
                        break;
                    case 3:
                        SendCmd(gain_I_cmd.get(position));
                        SendCmd(gain_II_cmd.get(position));
                        SendCmd(gain_III_cmd.get(position));
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //滤波选择
        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SendCmd(filter_cmd.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //陷波滤波
        spinnerNotchFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SendCmd(notch_cmd.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //导联系统
        channel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (rdoBtnChannel12.getId() == checkedId) {
                    SendCmd(new byte[]{0x52, (byte) 0x80, (byte) 0x82});
                }
                if (rdoBtnChannel5.getId() == checkedId) {
                    SendCmd(new byte[]{0x52, (byte) 0x80, (byte) 0x81});
                }
                if (rdoBtnChannel3.getId() == checkedId) {
                    SendCmd(new byte[]{0x52, (byte) 0x80, (byte) 0x80});
                }
            }
        });
        //起搏分析
        qibo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (rdoBtnOff.getId() == checkedId) {
                }
                if (rdoBtnOn.getId() == checkedId) {
                }
            }
        });
        //ST分析
        st.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (rdoBtnStOff.getId() == checkedId) {
                }
                if (rdoBtnStOn.getId() == checkedId) {
                }
            }
        });

    }

    /**
     * 发送串口命令
     *
     * @param cmd 命令数组
     */
    public static void SendCmd(byte[] cmd) {
        try {
            Global.ecg_Com.Write(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
