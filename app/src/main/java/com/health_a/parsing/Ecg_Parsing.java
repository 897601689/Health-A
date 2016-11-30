package com.health_a.parsing;

import android.os.Environment;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

import android_serialport_api.MySerialPort;


/**
 * Created by Admin on 2016/4/7.    心电数据解析类
 */
public class Ecg_Parsing {

    byte[] bytes = new byte[512];
    byte[] data = new byte[9];
    final List<Byte> list = new ArrayList<>();

    boolean isHeartBeaten = false;//是否包含心跳
    boolean isPacingPulse = false;//是否包含起搏脉冲

    //region 实体类
    //导联状态
    public static class LeadState {
        public int LL;
        public int LA;
        public int RA;
        public int V1;
        public int V2;
        public int V3;
        public int V4;
        public int V5;
        public int V6;
    }

    //ST数据
    public static class ECG_ST {
        public float ST1;     //ST1
        public float ST2;     //ST2
        public float ST3;     //ST3
        public float ST4;     //ST4
        public float ST5;     //ST5
        public float ST6;     //ST6
        public float ST7;     //ST7
        public float ST8;     //ST8
    }

    //ST模版
    public static class ECG_ST_Template {
        public int Channel;      //通道号
        public int Num;          //顺序号

        public int ST1;          //ST1
        public int ST2;          //ST2
        public int ST3;          //ST3
        public int ST4;          //ST4
        public int ST5;          //ST5
    }

    //自检
    public static class Self_Check {
        public int ECG;
        public int AD;
        public int WatchDog;
        public int ROM;
        public int RAM;
        public int CPU;
    }

    //ECG信息
    public class EcgMessage {
        public String Channel1;//通道号
        public String LeadMessage1;//导联信息
        public String FilterMode1;//滤波方式
        public String Gain1;//增益

        public String Channel2;//通道号
        public String LeadMessage2;//导联信息
        public String FilterMode2;//滤波方式
        public String Gain2;//增益

        public String Channel3;//通道号
        public String LeadMessage3;//导联信息
        public String FilterMode3;//滤波方式
        public String Gain3;//增益

        public String AnalysisChannel;    //心律失常分析通道
        public String XianboHz;  //50/60Hz 陷波设置
        public String XianboSwitch; //是否开陷波

    }

    //心电曲线数据
    public static class ECG_DATA {
        public List<Integer> ECG_I = new CopyOnWriteArrayList<>();
        public List<Integer> ECG_II = new CopyOnWriteArrayList<>();
        public List<Integer> ECG_III = new CopyOnWriteArrayList<>();
        public List<Integer> ECG_AVR = new CopyOnWriteArrayList<>();
        public List<Integer> ECG_AVL = new CopyOnWriteArrayList<>();
        public List<Integer> ECG_AVF = new CopyOnWriteArrayList<>();

        public List<Integer> ECG_V1 = new CopyOnWriteArrayList<>();
        public List<Integer> ECG_V2 = new CopyOnWriteArrayList<>();
        public List<Integer> ECG_V3 = new CopyOnWriteArrayList<>();
        public List<Integer> ECG_V4 = new CopyOnWriteArrayList<>();
        public List<Integer> ECG_V5 = new CopyOnWriteArrayList<>();
        public List<Integer> ECG_V6 = new CopyOnWriteArrayList<>();

        public List<Integer> RESP = new CopyOnWriteArrayList<>();

        public void clear() {
            ECG_I.clear();
            ECG_II.clear();
            ECG_III.clear();
            ECG_AVR.clear();
            ECG_AVL.clear();
            ECG_AVF.clear();
            ECG_V1.clear();
            ECG_V2.clear();
            ECG_V3.clear();
            ECG_V4.clear();
            ECG_V5.clear();
            ECG_V6.clear();
            RESP.clear();
        }
    }
    //endregion

    //报警
    public List<String> AlertMessage = new ArrayList<>();

    //导联类型
    public String LeadType;

    public List<EcgMessage> EcgMessages = new ArrayList<>();
    private int ecg = 0;//心电
    private int resp = 0;//呼吸率
    private int tempData1 = 0; //体温1
    private int tempData2 = 0; //体温2
    private ECG_DATA ecg_data = new ECG_DATA();
    private LeadState leadState = new LeadState();

    private ECG_ST ecg_st = new ECG_ST();

    public LeadState getLeadState() {
        return leadState;
    }

    public ECG_ST getEcg_st() {
        return ecg_st;
    }

    public ECG_DATA getEcg_data() {
        return ecg_data;
    }

    public int getEcg() {
        return ecg;
    }

    public int getResp() {
        return resp;
    }

    public int getTempData1() {
        return tempData1;
    }

    public int getTempData2() {
        return tempData2;
    }

    public Ecg_Parsing() {

    }

    /**
     * 解析心电数据
     *
     * @param port 串口对象
     */
    public void Parsing(MySerialPort port) {
        try {

            AlertMessage.clear();//清除报警信息
            EcgMessages.clear();//清除导联信息
            LeadType = null;
            ecg_data.clear();//清除心电曲线数据
            bytes = port.Read();
            if (bytes != null) {
                for (byte aByte : bytes) {
                    list.add(aByte);
                }
                for (int i = 0; i < list.size(); i++)
                    switch (list.get(i)) {
                        case 0x01://波形数据1
                            if (i + 6 < list.size()) {
                                for (int index = 0; index < 7; index++) {
                                    data[index] = list.get(i);
                                    list.remove(i);
                                }
                                i = -1;
                                int hr_I = ((data[1] & 0x1) << 7 | (data[2] & 0x7F));      //ECG1
                                int hr_II = ((data[1] >> 1 & 0x1) << 7 | (data[3] & 0x7F));//ECG2
                                int hr_v1 = ((data[1] >> 2 & 0x1) << 7 | (data[4] & 0x7F));//ECG3
                                isHeartBeaten = getBoolean(data[5] & 0x1);//是否包含心跳
                                isPacingPulse = getBoolean((data[5] >> 1) & 0x1);//是否包含起搏脉冲
                                int resp = (((data[1] >> 4) & 0x1) << 7 | (data[6] & 0x7F));//呼吸波

                                int hr_III = hr_II - hr_I + 128;
                                int hr_avr = (((hr_II + hr_I - 256) >> 1) * (-1) + 128);
                                int hr_avl = (((hr_v1 - hr_I) >> 1) + 128);
                                int hr_avf = (((hr_v1 + hr_II - 258) >> 1) * (-1) + 128);

                                ecg_data.ECG_I.add(hr_I);
                                ecg_data.ECG_II.add(hr_II);
                                ecg_data.ECG_V1.add(hr_v1);

                                ecg_data.ECG_III.add(hr_III);
                                ecg_data.ECG_AVR.add(hr_avr);
                                ecg_data.ECG_AVL.add(hr_avl);
                                ecg_data.ECG_AVF.add(hr_avf);

                                ecg_data.RESP.add(resp);//呼吸曲线

                            }
                            break;
                        case 0x02://波形数据2
                            if (i + 5 < list.size()) {
                                for (int index = 0; index < 6; index++) {
                                    data[index] = list.get(i);
                                    list.remove(i);
                                }
                                i = -1;
                                int hr_I = ((data[1] & 0x1) << 7 | (data[2] & 0x7F));//ECG1
                                int hr_II = ((data[1] >> 1 & 0x1) << 7 | (data[3] & 0x7F));//ECG2
                                int hr_v1 = ((data[1] >> 2 & 0x1) << 7 | (data[4] & 0x7F));//ECG3
                                isHeartBeaten = getBoolean(data[5] & 0x1);//是否包含心跳
                                isPacingPulse = getBoolean((data[5] >> 1) & 0x1);//是否包含起搏脉冲

                                int hr_III = hr_II - hr_I + 128;
                                int hr_avr = (((hr_II + hr_I - 256) >> 1) * (-1) + 128);
                                int hr_avl = (((hr_v1 - hr_I) >> 1) + 128);
                                int hr_avf = (((hr_v1 + hr_II - 258) >> 1) * (-1) + 128);

                                ecg_data.ECG_I.add(hr_I);
                                ecg_data.ECG_II.add(hr_II);
                                ecg_data.ECG_III.add(hr_III);
                                ecg_data.ECG_AVR.add(hr_avr);
                                ecg_data.ECG_AVL.add(hr_avl);
                                ecg_data.ECG_AVF.add(hr_avf);

                                ecg_data.ECG_V1.add(hr_v1);
                            }
                            break;
                        case 0x04://心率和呼吸率
                            if (i + 5 < list.size()) {
                                for (int index = 0; index < 6; index++) {
                                    data[index] = list.get(i);
                                    list.remove(i);
                                }
                                i = -1;
                                int Data_Hr = (((data[1] & 0x1) << 7) | (data[2] & 0x7F))
                                        | (((data[1] >> 1 & 0x1) << 7) | (data[3] & 0x7F));//心率
                                int Data_Resp = (((data[1] >> 2 & 0x1) << 7) | (data[4] & 0x7F))
                                        | (((data[1] >> 3 & 0x1) << 7) | (data[5] & 0x7F));//呼吸率

                                if (Data_Hr == 255)
                                    Data_Hr = 0;
                                if (Data_Resp == 255)
                                    Data_Resp = 0;

                                ecg = Data_Hr;
                                resp = Data_Resp;
                            }
                            break;
                        case 0x05://心率失常
                            if (i + 4 < list.size()) {
                                for (int index = 0; index < 5; index++) {
                                    data[index] = list.get(i);
                                    list.remove(i);
                                }
                                i = -1;
                                switch (data[1] & 0x1) {//是否是一次心律失常事件的起始
                                    case 0:
                                        //"是";
                                        break;
                                    case 1:
                                        //"否";
                                        break;
                                }

                                String Data_Exception = Arrhythmia(data[2] & 0x7F);
                                AlertMessage.add(Data_Exception);
                                int ExceptionNum = (((data[1] >> 1 & 0x1) << 7) | (data[3] & 0x7F))
                                        | (((data[1] >> 2 & 0x1) << 7) | (data[4] & 0x7F));
                            }
                            break;
                        case 0x06://每分钟室性早搏的个数
                            if (i + 3 < list.size()) {
                                for (int index = 0; index < 4; index++) {
                                    data[index] = list.get(i);
                                    list.remove(i);
                                }
                                i = -1;
                                int spo2Num = (((data[1] & 0x1) << 7) | (data[2] & 0x7F))
                                        | (((data[1] >> 1 & 0x1) << 7) | (data[3] & 0x7F));
                            }
                            break;
                        case 0x07://ST偏移
                            if (i + 7 < list.size()) {
                                for (int index = 0; index < 8; index++) {
                                    data[index] = list.get(i);
                                    list.remove(i);
                                }
                                i = -1;
                                int ST1 = (((data[1] & 0x1) << 7) | (data[2] & 0x7F))
                                        + (((data[1] >> 1 & 0x1) << 7) | (data[3] & 0x7F));//ST1
                                int ST2 = (((data[1] >> 2 & 0x1) << 7) | (data[4] & 0x7F))
                                        + (((data[1] >> 3 & 0x1) << 7) | (data[5] & 0x7F));//ST2
                                int ST3 = (((data[1] >> 4 & 0x1) << 7) | (data[6] & 0x7F))
                                        + (((data[1] >> 5 & 0x1) << 7) | (data[7] & 0x7F));//ST3
                                ecg_st.ST1 = ST1 / 100f;
                                ecg_st.ST2 = ST2 / 100f;
                                ecg_st.ST3 = ST3 / 100f;
                            }
                            break;
                        case 0x08://ST段模版
                            if (i + 8 < list.size()) {
                                for (int index = 0; index < 9; index++) {
                                    data[index] = list.get(i);
                                    list.remove(i);
                                }
                                i = -1;
                                int Channel = data[2] & 0xF;//通道数
                                int Num = data[3] & 0x7F;//顺序号
                                int ST1 = ((data[1] >> 2 & 0x1) << 7) | (data[4] & 0x7F);//数据1
                                int ST2 = ((data[1] >> 3 & 0x1) << 7) | (data[5] & 0x7F);//数据2
                                int ST3 = ((data[1] >> 4 & 0x1) << 7) | (data[6] & 0x7F);//数据3
                                int ST4 = ((data[1] >> 5 & 0x1) << 7) | (data[7] & 0x7F);//数据4
                                int ST5 = ((data[1] >> 6 & 0x1) << 7) | (data[8] & 0x7F);//数据5

                            }
                            break;
                        case 0x09://导联连接和信号信息
                            String signal = "";

                            if (i + 2 < list.size()) {
                                for (int index = 0; index < 4; index++) {
                                    data[index] = list.get(i);
                                    list.remove(i);
                                }
                                i = -1;
                                //导联连接模式
                                switch (data[2] & 0x3) {
                                    case 0x0:
                                        signal = "三导联系统";
                                        break;
                                    case 0x1:
                                        signal = "五导联系统";
                                        break;
                                    case 0x2:
                                        signal = "12 导联系统";
                                        break;
                                }
                                LeadType = signal;//保存导联类型
                                String errorConnect = "";
                                //导联连接状态
                                int V1 = data[2] >> 2 & 0x1;
                                leadState.V1 = V1;
                                if (V1 == 1) {
                                    errorConnect += " V1";
                                }
                                int LL = data[2] >> 3 & 0x1;//AVF
                                leadState.LL = LL;
                                if (LL == 1) {
                                    errorConnect += " AVF";
                                }
                                int LA = data[2] >> 4 & 0x1;//AVL
                                leadState.LA = LA;
                                if (LA == 1) {
                                    errorConnect += " AVL";
                                }
                                int RA = data[2] >> 5 & 0x1;//AVR
                                leadState.RA = RA;
                                if (RA == 1) {
                                    errorConnect += " AVR";
                                }
                                if (!errorConnect.equals("")) {
                                    errorConnect += "脱落";
                                    AlertMessage.add(errorConnect);         //**********
                                }
                            }
                            break;
                        case 0x0A://ECG设置信息 此处包含3个通道的全部信息 此代码只处理了第一个通道的数据
                            String config = "";
                            EcgMessage message = new EcgMessage();
                            if (i + 8 < list.size()) {
                                for (int index = 0; index < 9; index++) {
                                    data[index] = list.get(i);
                                    list.remove(i);
                                }
                                i = -1;

                                String Config_Channel = getChannel(data[2] & 0x15);//通道号
                                message.Channel1 = Config_Channel;
                                String Config_Lead_Mode = getLeadInfo(((data[1] & 0x1) << 3)
                                        | ((data[2] & 0x70) >> 4));//导联信息
                                message.LeadMessage1 = Config_Lead_Mode;
                                String Config_Filter_Mode = getFilterMode(data[3] & 0x15);//滤波方式
                                message.FilterMode1 = Config_Filter_Mode;
                                String Config_Gain = getGain(((data[1] >> 1 & 0x1) << 3)
                                        | ((data[2] & 0x70) >> 4));//增益
                                message.Gain1 = Config_Gain;

                                Config_Channel = getChannel(data[4] & 0x0f);//通道号
                                message.Channel2 = Config_Channel;
                                Config_Lead_Mode = getLeadInfo((((data[1] >> 2) & 0x1) << 3)
                                        | ((data[4] & 0x70) >> 4));//导联信息
                                message.LeadMessage2 = Config_Lead_Mode;
                                Config_Filter_Mode = getFilterMode(data[5] & 0x15);//滤波方式
                                message.FilterMode2 = Config_Filter_Mode;
                                Config_Gain = getGain((((data[1] >> 3) & 0x1) << 3)
                                        | ((data[5] & 0x70) >> 4));//增益
                                message.Gain2 = Config_Gain;

                                Config_Channel = getChannel(data[6] & 0x0f);//通道号
                                message.Channel3 = Config_Channel;
                                Config_Lead_Mode = getLeadInfo((((data[1] >> 4) & 0x1) << 3)
                                        | ((data[6] & 0x70) >> 4));//导联信息
                                message.LeadMessage3 = Config_Lead_Mode;
                                Config_Filter_Mode = getFilterMode(data[7] & 0x15);//滤波方式
                                message.FilterMode3 = Config_Filter_Mode;
                                Config_Gain = getGain((((data[1] >> 5) & 0x1) << 3)
                                        | ((data[7] & 0x70) >> 4));//增益
                                message.Gain3 = Config_Gain;


                                //字节6
                                switch (data[8] & 0x7) {//心律失常分析通道
                                    case 0x000:
                                        config = "通道 1";
                                        break;
                                    case 0x001:
                                        config = "通道 2";
                                        break;
                                    case 0x010:
                                        config = "通道 3";
                                        break;
                                }
                                message.AnalysisChannel = config;

                                switch ((data[8] & 0x18) >> 3) {//50/60Hz 陷波设置
                                    case 0x00:
                                        config = "50Hz 陷波";
                                        break;
                                    case 0x01:
                                        config = "60Hz 陷波";
                                        break;
                                }
                                message.XianboHz = config;

                                switch ((data[8] & 0x60) >> 5) {//50/60Hz 陷波选择
                                    case 0x00:
                                        config = "不陷波";
                                        break;
                                    case 0x01:
                                        config = "陷波";
                                        break;
                                }
                                message.XianboSwitch = config;
                                EcgMessages.add(message);
                            }
                            break;
                        case 0x0B://窒息报警
                            String apnea = "";
                            if (i + 3 < list.size()) {
                                for (int index = 0; index < 4; index++) {
                                    data[index] = list.get(i);
                                    list.remove(i);
                                }
                                i = -1;
                                switch (data[2] & 0x1) {
                                    case 0:
                                        apnea = "无窒息";
                                        break;
                                    case 1:
                                        apnea = "窒息报警";
                                        break;
                                }
                                String ECG_Apnea = apnea;
                                AlertMessage.add(ECG_Apnea);
                            }
                            break;
                        case 0x0C://系统自检
                            if (i + 2 < list.size()) {
                                for (int index = 0; index < 3; index++) {
                                    data[index] = list.get(i);
                                    list.remove(i);
                                }
                                i = -1;
                                int ECG = data[2] & 0x1;// ECG
                                int AD = data[2] >> 1 & 0x1;// A/D
                                int WatchDog = data[2] >> 2 & 0x1;// WatchDog
                                int ROM = data[2] >> 3 & 0x1;// ROM
                                int RAM = data[2] >> 4 & 0x1;// RAM
                                int CPU = data[2] >> 5 & 0x1;// CPU
                            }
                            break;
                        case 0x0D://模块标识
                            if (i + 2 < list.size()) {
                                for (int index = 0; index < 3; index++) {
                                    data[index] = list.get(i);
                                    list.remove(i);
                                }
                                i = -1;
                            }
                            break;
                        case 0x0E://系统复位信息
                            if (i + 2 < list.size()) {
                                for (int index = 0; index < 2; index++) {
                                    data[index] = list.get(i);
                                    list.remove(i);
                                }
                                i = -1;
                            }
                            break;
                        case 0x0F://心电分析状况
                            signal = "";
                            if (i + 2 < list.size()) {
                                for (int index = 0; index < 3; index++) {
                                    data[index] = list.get(i);
                                    list.remove(i);
                                }
                                i = -1;

                                switch (data[2] & 0x7F) {
                                    case 0:
                                        signal = "正常分析";
                                        break;
                                    case 1:
                                        signal = "R 波检测正在初始化";
                                        break;
                                    case 2:
                                        signal = "心律失常分析自学习";
                                        break;
                                    case 3:
                                        signal = "信号噪声太大";
                                        break;
                                    case 4:
                                        signal = "信号太小，无法检测（可能是停搏或信号幅度太小）";
                                        break;
                                    case 5:
                                        signal = "没有信号";
                                        break;
                                }
                                String ECG_Signal = signal;
                                AlertMessage.add(ECG_Signal);
                            }
                            break;
                        case 0x10://心搏信息
                            String str;
                            if (i + 4 < list.size()) {
                                for (int index = 0; index < 5; index++) {
                                    data[index] = list.get(i);
                                    list.remove(i);
                                }
                                i = -1;
                                str = getHeartBeat((data[2] & 0x7F));
                                String ECg_Heart_Beat = str;//心搏信息
                                AlertMessage.add(ECg_Heart_Beat);
                                str = String.valueOf((((data[1] & 0x1) << 7) | (data[3] & 0x7F))
                                        | (((data[1] >> 1 & 0x1) << 7) | (data[4] & 0x7F)));
                                String ECg_Heart_Beat_Location = str;//心搏位置
                            }
                            break;
                        case 0x11://体温数据
                            String mode = "";
                            if (i + 6 < list.size()) {
                                for (int index = 0; index < 7; index++) {
                                    data[index] = list.get(i);
                                    list.remove(i);
                                }
                                i = -1;
                                switch ((data[2] & 0x7F)) {//体温状态
                                    case 0:
                                        mode = "两个体温探头都接上";
                                        break;
                                    case 1:
                                        mode = "第一通道体温探头脱落，只有第二通道体温探头";
                                        break;
                                    case 2:
                                        mode = "第二通道体温探头脱落，只有第一通道体温探头";
                                        break;
                                    case 3:
                                        mode = "两个体温探头都脱落";
                                        break;
                                }
                                if (mode != "") {
                                    AlertMessage.add(mode);//添加体温报警信息
                                }
                                tempData1 = (((data[1] >> 1 & 0x1) << 7) | (data[3] & 0x7F)) | ((((data[1] >> 2 & 0x1) << 7) | (data[4] & 0x7F)) << 8);
                                tempData2 = (((data[1] >> 3 & 0x1) << 7) | (data[5] & 0x7F)) | ((((data[1] >> 4 & 0x1) << 7) | (data[6] & 0x7F)) << 8);
                            }
                            break;
                        case 0x12://波形数据4
                            if (i + 6 < list.size()) {
                                for (int index = 0; index < 7; index++) {
                                    data[index] = list.get(i);
                                    list.remove(i);
                                }

                                i = -1;
                                int hr_v2 = ((data[1] & 0x1) << 7) | (data[2] & 0x7F);//ECG V2
                                int hr_v3 = ((data[1] >> 1 & 0x1) << 7) | (data[3] & 0x7F);//ECG V3
                                int hr_v4 = ((data[1] >> 2 & 0x1) << 7) | (data[4] & 0x7F);//ECG V4
                                int hr_v5 = ((data[1] >> 3 & 0x1) << 7) | (data[5] & 0x7F);//ECG V5
                                int hr_v6 = ((data[1] >> 4 & 0x1) << 7) | (data[6] & 0x7F);//ECG V6

                                ecg_data.ECG_V2.add(hr_v2);
                                ecg_data.ECG_V3.add(hr_v3);
                                ecg_data.ECG_V4.add(hr_v4);
                                ecg_data.ECG_V5.add(hr_v5);
                                ecg_data.ECG_V6.add(hr_v6);
                            }
                            break;
                        case 0x13://V导联连接和信号信息
                            if (i + 2 < list.size()) {
                                for (int index = 0; index < 3; index++) {
                                    data[index] = list.get(i);
                                    list.remove(i);
                                }
                                i = -1;
                                String errorConnect = "";
                                int V2 = data[2] & 0x1;
                                leadState.V2 = V2;
                                if (V2 == 1) {
                                    errorConnect += " V2";
                                }
                                int V3 = data[2] >> 1 & 0x1;
                                leadState.V3 = V3;
                                if (V3 == 1) {
                                    errorConnect += " V3";
                                }
                                int V4 = data[2] >> 2 & 0x1;
                                leadState.V4 = V4;
                                if (V4 == 1) {
                                    errorConnect += " V4";
                                }
                                int V5 = data[2] >> 3 & 0x1;
                                leadState.V5 = V5;
                                if (V5 == 1) {
                                    errorConnect += " V5";
                                }
                                int V6 = data[2] >> 4 & 0x1;
                                leadState.V6 = V6;
                                if (V6 == 1) {
                                    errorConnect += " V6";
                                }
                                if (errorConnect != "") {
                                    errorConnect += "脱落";
                                    AlertMessage.add(errorConnect);                 //**********
                                }
                            }
                            break;
                        case 0x15://ST偏移 4-6字节
                            if (i + 7 < list.size()) {
                                for (int index = 0; index < 8; index++) {
                                    data[index] = list.get(i);
                                    list.remove(i);
                                }
                                i = -1;
                                int ST4 = (((data[1] & 0x1) << 7) | (data[2] & 0x7F))
                                        | (((data[1] >> 1 & 0x1) << 7) | (data[3] & 0x7F));//ST4
                                int ST5 = (((data[1] >> 2 & 0x1) << 7) + (data[4] & 0x7F))
                                        | (((data[1] >> 3 & 0x1) << 7) | (data[5] & 0x7F));//ST5
                                int ST6 = (((data[1] >> 4 & 0x1) << 7) + (data[6] & 0x7F))
                                        | (((data[1] >> 5 & 0x1) << 7) | (data[7] & 0x7F));//ST6

                                ecg_st.ST4 = ST4 / 100f;
                                ecg_st.ST5 = ST5 / 100f;
                                ecg_st.ST6 = ST6 / 100f;
                            }
                            break;
                        case 0x16://ST偏移 7-8字节
                            if (i + 5 < list.size()) {
                                for (int index = 0; index < 6; index++) {
                                    data[index] = list.get(i);
                                    list.remove(i);
                                }
                                i = -1;
                                int ST7 = (((data[1] & 0x1) << 7) | (data[2] & 0x7F))
                                        | (((data[1] >> 1 & 0x1) << 7) | (data[3] & 0x7F));//ST7
                                int ST8 = (((data[1] >> 2 & 0x1) << 7) + (data[4] & 0x7F))
                                        | (((data[1] >> 3 & 0x1) << 7) | (data[5] & 0x7F));//ST8
                                ecg_st.ST7 = ST7 / 100f;
                                ecg_st.ST8 = ST8 / 100f;
                            }
                            break;
                    }
            } else {
                //Log.e("null", "null");
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }


    }


    private int getI(int i, int length) {
        for (int index = 0; index < length; index++) {
            data[index] = list.get(i);
            list.remove(i);
        }
        i = -1;
        return i;
    }

    //获取信息
    private static boolean getBoolean(int data) {
        boolean info = false;
        switch (data) {
            case 0:
                info = false;
                break;
            case 1:
                info = true;
                break;
        }
        return info;
    }

    /**
     * ECG增益
     *
     * @param num
     * @return
     */
    private String getGain(int num) {
        String str;
        switch (num) {//增益
            case 0x0000:
                str = "0.25V/mV";
                break;
            case 0x0001:
                str = "0.50V/mV";
                break;
            case 0x0010:
                str = "1.0V/mV";
                break;
            case 0x0011:
                str = "2.0V/mV";
                break;
            default:
                str = "";
                break;
        }
        return str;
    }

    /**
     * ECG滤波方式
     *
     * @param num
     * @return
     */
    private String getFilterMode(int num) {
        String str;
        switch (num) {//滤波方式
            case 0x0000:
                str = "诊断";
                break;
            case 0x0001:
                str = "监护";
                break;
            case 0x0010:
                str = "手术";
                break;
            case 0x0011:
                str = "强滤波";
                break;
            default:
                str = "";
                break;
        }
        return str;
    }

    /**
     * 导联信息
     *
     * @param num
     * @return
     */
    private String getLeadInfo(int num) {
        String str;
        switch (num) {//导联方式
            case 0x0000:
                str = "校准波形";
                break;
            case 0x0001:
                str = "I";
                break;
            case 0x0010:
                str = "II";
                break;
            case 0x0011:
                str = "III";
                break;
            case 0x0100:
                str = "AVR";
                break;
            case 0x0101:
                str = "AVL";
                break;
            case 0x0110:
                str = "AVF";
                break;
            case 0x0111:
                str = "V";
                break;
            default:
                str = "";
                break;
        }
        return str;
    }

    /**
     * 通道号
     *
     * @param num
     * @return
     */
    private String getChannel(int num) {
        String str;
        switch (num) {//通道号
            case 0x0000:
                str = "通道 1";
                break;
            case 0x0001:
                str = "通道 2";
                break;
            case 0x0010:
                str = "通道 3";
                break;
            default:
                str = "";
                break;
        }
        return str;
    }

    /**
     * 心搏信息
     *
     * @param index
     * @return
     */
    private String getHeartBeat(int index) {
        String str;
        switch (index) {
            case 0:
                str = "主导心搏（Dominant）";
                break;
            case 1:
                str = "异常心搏（Abnormal）";
                break;
            case 3:
                str = "未归类心搏（Unclassified）";
                break;
            case 4:
                str = "自学习使用的心搏（Learn）";
                break;
            default:
                str = "";
                break;
        }
        return str;
    }

    /**
     * 心率失常 判断
     *
     * @param Num
     * @return
     */
    private String Arrhythmia(int Num) {
        String str;
        switch (Num) {
            case 0:
                str = "停搏（ASY）";
                break;
            case 1:
                str = "室颤（VF）";
                break;
            case 2:
                str = "室速（VTA）";
                break;
            case 3:
                str = "R on T（ROT）";
                break;
            case 4:
                str = "三个或四个连发室早（RUN）";
                break;
            case 6:
                str = "二连发室早（CPT）";
                break;
            case 7:
                str = "单个室早（VPB）";
                break;
            case 8:
                str = "室早二联律（BGM）";
                break;
            case 9:
                str = "室早三联律（TGM）";
                break;
            case 10:
                str = "室上性心动过速（TAC）";
                break;
            case 11:
                str = "心动过缓（BRD）";
                break;
            case 13:
                str = "起搏器未俘获（PNC）";
                break;
            case 14:
                str = "起搏器未起搏（PNP）";
                break;
            case 16:
                str = "漏搏（MIS）";
                break;
            case 20:
                str = "正在学习（LRN）";
                break;
            case 21:
                str = "未进行心律失常检测";
                break;
            case 22:
                str = "正常心律（NML）";
                break;
            case 24:
                str = "噪声信号（NOS）";
                break;
            case 25:
                str = "信号幅度过小";
                break;
            default:
                str = "";
                break;
        }
        return str;
    }

    public static byte[] SendEcgGain(int channel, int gain) {
        byte[] data = new byte[3];
        data[0] = 0x42;
        return data;
    }
}
