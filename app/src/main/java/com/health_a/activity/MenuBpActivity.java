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
import android.widget.TextView;

import com.health_a.R;
import com.health_a.parsing.Mcu_Parsing;
import com.health_a.util.Global;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Admin on 2016/11/28. 血压菜单
 */
public class MenuBpActivity extends Activity {
    @BindView(R.id.spinner_ac)
    Spinner spinnerAc;
    @BindView(R.id.spinner_gas)
    Spinner spinnerGas;
    @BindView(R.id.rdoBtn_m1)
    RadioButton rdoBtnM1;
    @BindView(R.id.rdoBtn_m2)
    RadioButton rdoBtnM2;
    @BindView(R.id.rdoBtn_m3)
    RadioButton rdoBtnM3;
    @BindView(R.id.mode)
    RadioGroup mode;
    @BindView(R.id.txt_pressure)
    TextView txtPressure;
    @BindView(R.id.btn_bp_leak)
    TextView btnBpLeak;
    @BindView(R.id.btn_bp_Cal)
    TextView btnBpCal;
    @BindView(R.id.btn_bp_stop)
    TextView btnBpStop;
    @BindView(R.id.btn_bp_Reset)
    TextView btnBpReset;

    List<byte[]> ac = new ArrayList<>();
    List<byte[]> gas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_bp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);   //应用运行时，保持屏幕高亮，不锁屏
        ButterKnife.bind(this);
        init();
    }

    private void init() {

        //region 自动血压命令
        ac.add(Mcu_Parsing.bp_Manual);
        ac.add(Mcu_Parsing.bp_AC_1);
        ac.add(Mcu_Parsing.bp_AC_2);
        ac.add(Mcu_Parsing.bp_AC_3);
        ac.add(Mcu_Parsing.bp_AC_4);
        ac.add(Mcu_Parsing.bp_AC_5);
        ac.add(Mcu_Parsing.bp_AC_10);
        ac.add(Mcu_Parsing.bp_AC_15);
        ac.add(Mcu_Parsing.bp_AC_30);
        ac.add(Mcu_Parsing.bp_AC_60);
        ac.add(Mcu_Parsing.bp_AC_90);
        ac.add(Mcu_Parsing.bp_AC_120);
        ac.add(Mcu_Parsing.bp_AC_180);
        ac.add(Mcu_Parsing.bp_AC_240);
        ac.add(Mcu_Parsing.bp_AC_480);
        //endregion

        //region 预充气值
        gas.add(Mcu_Parsing.bp_Cuff_80);
        gas.add(Mcu_Parsing.bp_Cuff_100);
        gas.add(Mcu_Parsing.bp_Cuff_120);
        gas.add(Mcu_Parsing.bp_Cuff_140);
        gas.add(Mcu_Parsing.bp_Cuff_160);
        gas.add(Mcu_Parsing.bp_Cuff_180);
        gas.add(Mcu_Parsing.bp_Cuff_200);
        gas.add(Mcu_Parsing.bp_B_Cuff_220);
        gas.add(Mcu_Parsing.bp_B_Cuff_240);
        //endregion

        spinnerAc.setSelection(0, true);    //
        spinnerGas.setSelection(6, true);   //
        //自动测量
        spinnerAc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Mcu_Parsing.SendBpCmd(Global.mcu_Com, ac.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //预充气值
        spinnerGas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Mcu_Parsing.SendBpCmd(Global.mcu_Com, gas.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //病人模式
        mode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (rdoBtnM1.getId() == checkedId) {
                    Mcu_Parsing.SendBpCmd(Global.mcu_Com, Mcu_Parsing.bp_B_Cuff_140);//成人
                }
                if (rdoBtnM2.getId() == checkedId) {
                    Mcu_Parsing.SendBpCmd(Global.mcu_Com, Mcu_Parsing.bp_S_Cuff_200);//儿童
                }
                if (rdoBtnM3.getId() == checkedId) {
                    Mcu_Parsing.SendBpCmd(Global.mcu_Com, Mcu_Parsing.bp_N_Cuff);   //新生儿
                }
            }
        });
    }

    @OnClick({R.id.btn_bp_leak, R.id.btn_bp_Cal, R.id.btn_bp_stop, R.id.btn_bp_Reset})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_bp_leak:
                Mcu_Parsing.SendBpCmd(Global.mcu_Com, Mcu_Parsing.bp_Leak);   //漏气检测
                break;
            case R.id.btn_bp_Cal:
                Mcu_Parsing.SendBpCmd(Global.mcu_Com, Mcu_Parsing.bp_Cal);    //校准
                break;
            case R.id.btn_bp_stop:
                Mcu_Parsing.SendBpCmd(Global.mcu_Com, Mcu_Parsing.bp_Stop);   //停止操作
                break;
            case R.id.btn_bp_Reset:
                Mcu_Parsing.SendBpCmd(Global.mcu_Com, Mcu_Parsing.bp_Reset);  //系统复位
                break;
        }
    }
}
