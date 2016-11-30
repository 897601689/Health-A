package com.health_a.parsing;

import android.os.Environment;
import android.util.Log;

import com.health_a.readutil.DataUtil;
import com.ivsign.android.IDCReader.IDCReaderSDK;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import android_serialport_api.MySerialPort;

/**
 * Created by Admin on 2016/11/3. 按键板、血压、身份证
 */
public class Mcu_Parsing {

    private byte[] buffer;
    private List<Byte> list = new ArrayList<>();
    private List<byte[]> data = new ArrayList<>();

    byte[] mcu_data = new byte[12];
    byte[] bp_data1 = new byte[10];
    byte[] bp_data2 = new byte[42];
    byte[] id_data = new byte[11];

    //region 血压
    private String cuff_Pressure;   //袖带压
    private String bp_H = "--";     //收缩压
    private String bp_L = "--";     //舒张压
    private String bp_Avg = "--";   //平均压
    private String bp_Pulse = "--"; //脉率
    private String bp_Error = "--"; //错误代码
    private String bp_Mode = "--";  //病人模式
    private String bp_state = "--"; //系统状态
    private String bp_Test = "手动";  //测量模式
    private String bp_leak = "--";  //漏气率

    public String getCuff_Pressure() {
        return cuff_Pressure;
    }

    public String getBp_H() {
        return bp_H;
    }

    public String getBp_L() {
        return bp_L;
    }

    public String getBp_Avg() {
        return bp_Avg;
    }

    public String getBp_Pulse() {
        return bp_Pulse;
    }

    public String getBp_Error() {
        return bp_Error;
    }

    public String getBp_Mode() {
        return bp_Mode;
    }

    public String getBp_state() {
        return bp_state;
    }

    public String getBp_Test() {
        return bp_Test;
    }

    public String getBp_leak() {
        return bp_leak;
    }
    //endregion

    //region 按键板
    public String getKey() {
        return key;
    }

    public String getBattery() {
        return battery;
    }

    private String key;     // 飞梭按钮
    private String battery; // 电池电量

    public static byte[] DeviceOFF = new byte[]{(byte) 0x81};   // 主机发送关机命令

    public static byte[] UmOn = new byte[]{(byte) 0x82};    // 尿机模块上电
    public static byte[] UmOff = new byte[]{(byte) 0x83};   // 尿机模块断电

    public static byte[] EcgOn = new byte[]{(byte) 0x84};   // 心电模块上电
    public static byte[] EcgOff = new byte[]{(byte) 0x85};  // 心电模块断电

    public static byte[] Spo2On = new byte[]{(byte) 0x86};  // 血氧模块上电
    public static byte[] Spo2Off = new byte[]{(byte) 0x87}; // 血氧模块断电

    public static byte[] BpOn = new byte[]{(byte) 0x88};    // 血压模块上电
    public static byte[] BpOff = new byte[]{(byte) 0x89};   // 血压模块断电

    public static byte[] IDOn = new byte[]{(byte) 0x8a};    // ID模块上电
    public static byte[] IDOff = new byte[]{(byte) 0x8b};   // ID模块断电

    public static byte[] cellCmd = new byte[]{(byte) 0x90};     // 主机询问当前电池状态,

    public static byte[] buzzerOn = new byte[]{(byte) 0x91};    // 蜂鸣器报警开
    public static byte[] buzzerOff = new byte[]{(byte) 0x92};   // 蜂鸣器报警关


    public static byte[] UmDeviceOn = new byte[]{(byte) 0x93};  // 尿机开机
    public static byte[] UmDeviceOff = new byte[]{(byte) 0x94}; // 尿机关机
    public static byte[] UmTest = new byte[]{(byte) 0x95};      // 尿机 测试
    public static byte[] UmPass = new byte[]{(byte) 0x96};      // 尿机 跳过
    public static byte[] UmReturn = new byte[]{(byte) 0x97};    // 尿机 返回

    public static byte[] AlarmHigh = new byte[]{(byte) 0x98};   // 报警 高
    public static byte[] AlarmMid = new byte[]{(byte) 0x99};    // 报警 中
    public static byte[] AlarmLow = new byte[]{(byte) 0x9a};    // 报警 低
    public static byte[] AlarmOff = new byte[]{(byte) 0x9b};    // 报警 关

    //endregion

    //region 身份证
    public String getIdState() {
        return idState;
    }

    public String[] getIdInfo() {
        return idInfo;
    }

    String idState = "";
    String[] idInfo = new String[10];
    //endregion

    //region 血压命令
    public static byte[] bp_Stop = new byte[]{0x58};//停止操作命令
    public static byte[] bp_Start = new byte[]{0x30, 0x31, 0x44, 0x37};//01开启手动测量
    public static byte[] bp_Manual = new byte[]{0x30, 0x33, 0x44, 0x39};//03结束自动测量方式，转为手动测量方式
    public static byte[] bp_AC_1 = new byte[]{0x30, 0x34, 0x44, 0x41};//04设置成周期为1 分钟的自动测量方式
    public static byte[] bp_AC_2 = new byte[]{0x30, 0x35, 0x44, 0x42};//05设置成周期为2 分钟的自动测量方式
    public static byte[] bp_AC_3 = new byte[]{0x30, 0x36, 0x44, 0x43};//06设置成周期为3 分钟的自动测量方式
    public static byte[] bp_AC_4 = new byte[]{0x30, 0x37, 0x44, 0x44};//07设置成周期为4 分钟的自动测量方式
    public static byte[] bp_AC_5 = new byte[]{0x30, 0x38, 0x44, 0x45};//08设置成周期为5 分钟的自动测量方式
    public static byte[] bp_AC_10 = new byte[]{0x30, 0x39, 0x44, 0x46};//09设置成周期为10 分钟的自动测量方式

    public static byte[] bp_AC_15 = new byte[]{0x31, 0x30, 0x44, 0x37};//10设置成周期为15 分钟的自动测量方式
    public static byte[] bp_AC_30 = new byte[]{0x31, 0x31, 0x44, 0x38};//11设置成周期为30 分钟的自动测量方式
    public static byte[] bp_AC_60 = new byte[]{0x31, 0x32, 0x44, 0x39};//12设置成周期为60 分钟的自动测量方式
    public static byte[] bp_AC_90 = new byte[]{0x31, 0x33, 0x44, 0x41};//13设置成周期为90 分钟的自动测量方式

    public static byte[] bp_Cal = new byte[]{0x31, 0x34, 0x44, 0x42};//14开始校准方式（返回实时袖带压力数据）
    public static byte[] bp_Watchdog = new byte[]{0x31, 0x35, 0x44, 0x43};//15进行Watchdog 检测（检测成功则系统复位）
    public static byte[] bp_Reset = new byte[]{0x31, 0x36, 0x44, 0x44};//16系统复位，完成自检
    public static byte[] bp_Leak = new byte[]{0x31, 0x37, 0x44, 0x45};//17进行气路漏气检测
    public static byte[] bp_State = new byte[]{0x31, 0x38, 0x44, 0x46};//18返回系统状态

    public static byte[] bp_B_Cuff_140 = new byte[]{0x32, 0x31, 0x44, 0x39};//21转换为大袖套模式，设置预充气压力为140mmHg
    public static byte[] bp_S_Cuff_160 = new byte[]{0x32, 0x32, 0x44, 0x41};//22在大袖套/小袖套模式时，设置预充气压力为160mmHg
    public static byte[] bp_S_Cuff_180 = new byte[]{0x32, 0x33, 0x44, 0x42};//23在大袖套/小袖套模式时，设置预充气压力为180mmHg
    public static byte[] bp_B_Cuff_150 = new byte[]{0x32, 0x34, 0x44, 0x43};//24转换为大袖套模式，设置预充气压力为150mmHg //成人模式
    public static byte[] bp_Cont = new byte[]{0x32, 0x37, 0x44, 0x46};//27开始5 分钟的连续测量方式
    public static byte[] bp_Cuff_80 = new byte[]{0x32, 0x38, 0x45, 0x30};//28在大袖套模式/小袖套时，设置预充气压力为80mmHg
    public static byte[] bp_Cuff_100 = new byte[]{0x32, 0x39, 0x45, 0x31};//29在大袖套模式/小袖套时，设置预充气压力为100mmHg

    public static byte[] bp_Cuff_120 = new byte[]{0x33, 0x30, 0x44, 0x39};//30在大袖套模式/小袖套时，设置预充气压力为120mmHg
    public static byte[] bp_Cuff_140 = new byte[]{0x33, 0x31, 0x44, 0x41};//31在大袖套模式/小袖套时，设置预充气压力为140mmHg
    public static byte[] bp_Cuff_160 = new byte[]{0x33, 0x32, 0x44, 0x42};//32在大袖套模式/小袖套时，设置预充气压力为160mmHg
    public static byte[] bp_Cuff_180 = new byte[]{0x33, 0x33, 0x44, 0x43};//33在大袖套模式/小袖套时，设置预充气压力为180mmHg
    public static byte[] bp_Cuff_200 = new byte[]{0x33, 0x34, 0x44, 0x44};//34在大袖套模式/小袖套时，设置预充气压力为200mmHg
    public static byte[] bp_B_Cuff_220 = new byte[]{0x33, 0x35, 0x44, 0x45};//35在大袖套模式时，设置预充气压力为220mmHg
    public static byte[] bp_B_Cuff_240 = new byte[]{0x33, 0x36, 0x44, 0x46};//36在大袖套模式时，设置预充气压力为240mmHg

    public static byte[] bp_AC_120 = new byte[]{0x34, 0x31, 0x44, 0x42};//41设置成周期为120 分钟的自动测量方式
    public static byte[] bp_AC_180 = new byte[]{0x34, 0x32, 0x44, 0x43};//42设置成周期为180 分钟的自动测量方式
    public static byte[] bp_AC_240 = new byte[]{0x34, 0x33, 0x44, 0x44};//43设置成周期为240 分钟的自动测量方式
    public static byte[] bp_AC_480 = new byte[]{0x34, 0x34, 0x44, 0x45};//44设置成周期为480 分钟的自动测量方式
    public static byte[] bp_S_Cuff_200 = new byte[]{0x34, 0x36, 0x45, 0x30};//46转换为小袖套模式，设置预充气压力为200mmHg //小儿模式

    public static byte[] bp_N_Cuff = new byte[]{0x33, 0x37, 0x45, 0x30};//25转换为新生儿模式
    public static byte[] bp_N_Cuff_60 = new byte[]{0x33, 0x37, 0x45, 0x30};//37在新生儿模式，设置预充气压力为60mmHg
    public static byte[] bp_N_Cuff_80 = new byte[]{0x33, 0x38, 0x45, 0x31};//38在新生儿模式，设置预充气压力为60mmHg
    public static byte[] bp_N_Cuff_100 = new byte[]{0x33, 0x39, 0x45, 0x32};//39在新生儿模式，设置预充气压力为60mmHg
    public static byte[] bp_N_Cuff_120 = new byte[]{0x32, 0x30, 0x44, 0x38};//20在新生儿模式，设置预充气压力为60mmHg
    //endregion

    private boolean mcu = false;
    private boolean bp = false;
    private boolean card = false;

    public void Parsing(MySerialPort com) {
        try {

            buffer = com.Read();
            if (buffer != null) {
                bp_Error="--";
                for (byte aByte : buffer) {
                    list.add(aByte);
                }
                for (int i = 0; i < list.size(); i++) {

                    //按键板 信息
                    if (list.get(i) == 0xff && buffer[i + 11] == 0xee) {
                        mcu_data = GetData(i, 12, list);
                        i = i - 1;
                        Parsing_Mcu(mcu_data);
                    }

                    //血压信息
                    if (list.get(i) == 0x02) {
                        if (list.get(i + 4) == 0x03 && list.get(i + 5) == 0x0D) {
                            GetData(i,6,list);
                            i = i - 1;
                            bp_state = "测量终止";
                            //Log.e("测量",""+bp_state);
                            SendBpCmd(com, bp_State);//发送查询系统状态命令
                            break;
                        }
                        if (list.get(i + 8) == 0x03 && list.get(i + 9) == 0x0D) {
                            bp_data1 = GetData(i, 10, list);
                            i = i - 1;
                            Parsing_Bp(bp_data1);
                        }
                        if (list.get(i + 40) == 0x03 && list.get(i + 41) == 0x0D) {
                            bp_data2 = GetData(i, 42, list);
                            i = i - 1;
                            Parsing_Bp(bp_data2);
                        }
                    }
                    //身份证信息
                    if (list.get(i) == 0xAA) {
                        if (list.get(i + 1) == 0xAA && list.get(i + 2) == 0xAA) {
                            if (list.get(i + 5) == 0x00) {
                                switch (list.get(i + 6)) {
                                    case 0x04:
                                        id_data = GetData(i, 11, list);
                                        i = i - 1;
                                        break;
                                    case 0x08:
                                        id_data = GetData(i, 15, list);
                                        i = i - 1;
                                        break;
                                    case 0x0C:
                                        id_data = GetData(i, 19, list);
                                        i = i - 1;
                                        break;
                                }
                            } else if (list.get(i + 5) == 0x05) {
                                id_data = GetData(i, 1295, list);
                                i = i - 1;
                            }
                        }
                    }
                }
            }

        } catch (Exception ex) {
           // Log.e("ERROR", ex.toString());
        }

    }

    /**
     * 获取命令数组
     *
     * @param i
     * @param len
     * @param list
     * @return
     */
    private byte[] GetData(int i, int len, List<Byte> list) {
        byte[] data = new byte[len];
        if (list.size() >= len) {
            for (int index = 0; index < len; index++) {
                data[index] = list.get(i);
                list.remove(i);
            }
        } else {
            data = null;
        }
        return data;
    }

    /**
     * 按键板解析
     *
     * @param data 数据数组
     */
    private void Parsing_Mcu(byte[] data) {
        if (data[1] == 0x30 && data[2] == 0x01) {
            switch (data[3]) {
                case (byte) 0x80:
                    key = "关机";
                    break;
                case (byte) 0x82:
                    key = "顺时针";
                    break;
                case (byte) 0x83:
                    key = "逆时针";
                    break;
                case (byte) 0x84:
                    key = "确定";
                    break;
                case (byte) 0x86:
                    battery = "充满";
                    break;
                case (byte) 0x87:
                    battery = "未充满";
                    break;
                case (byte) 0x88:
                    battery = "电量高";
                    break;
                case (byte) 0x89:
                    battery = "电量中";
                    break;
                case (byte) 0x8a:
                    battery = "电量低";
                    break;
                case (byte) 0x8b:
                    battery = "欠压报警";
                    break;
                case (byte) 0x8c:
                    key = "按键-强光";
                    break;
                case (byte) 0x8d:
                    key = "按键-模式";
                    break;
                case (byte) 0x8e:
                    key = "按键-冻结";
                    break;
                case (byte) 0x8f:
                    key = "按键-静音";
                    break;
                case (byte) 0x90:
                    key = "按键-血压";
                    break;
                case (byte) 0x91:
                    key = "按键-菜单";
                    break;
                default:
                    key = null;
                    battery = null;
                    break;
            }
        }
    }

    /**
     * 血压解析
     *
     * @param data 数据数组
     */
    private void Parsing_Bp(byte[] data) {
        switch (data.length) {
            case 10:
                //region 袖带压
                char a = (char) (int) (data[1]);
                char b = (char) (int) (data[2]);
                char c = (char) (int) (data[3]);
                cuff_Pressure = String.valueOf(a) + String.valueOf(b) + String.valueOf(c);

                //Log.e("袖带压",""+cuff_Pressure);
                //endregion
                break;
            case 42:

                //region 系统状态
                switch (data[2]) {
                    case 0x30:
                        bp_state = "完成自检";//0
                        break;
                    case 0x31:
                        bp_state = "系统正常，手动模式";//1
                        break;
                    case 0x32:
                        bp_state = "系统错误，定时器未运行";//2
                        break;
                    case 0x36:
                        bp_state = "系统正常，定时器运行在自动测量5分钟连续模式下";//6
                        break;
                    default:
                        bp_state = "---";
                        break;
                }
                //endregion

                //region 袖带类型
                switch (data[5]) {
                    case 0x30:
                        bp_Mode = "成人";//0大袖套
                        break;
                    case 0x31:
                        bp_Mode = "新生儿";//1
                        break;
                    case 0x32:
                        bp_Mode = "小儿";//2小袖套
                        break;
                    default:
                        bp_Mode = "---";
                        break;
                }
                //endregion

                //region 测量模式
                char d = (char) (int) (data[8]);
                char e = (char) (int) (data[9]);
                String mode = String.valueOf(d) + String.valueOf(e);

                switch (mode) {//测量模式
                    case "00":
                        bp_Test = "手动";//00
                        break;
                    case "01":
                        bp_Test = "1";//01自动模式
                        break;
                    case "02":
                        bp_Test = "2";//02
                        break;
                    case "03":
                        bp_Test = "3";//03
                        break;
                    case "04":
                        bp_Test = "4";//04
                        break;
                    case "05":
                        bp_Test = "5";//05
                        break;
                    case "10":
                        bp_Test = "10";//10
                        break;
                    case "15":
                        bp_Test = "15";//15
                        break;
                    case "30":
                        bp_Test = "30";//30
                        break;
                    case "60":
                        bp_Test = "60";//60
                        break;
                    case "90":
                        bp_Test = "90";//90
                        break;
                    case "91":
                        bp_Test = "120";//91
                        break;
                    case "92":
                        bp_Test = "180";//92
                        break;
                    case "93":
                        bp_Test = "240";//93
                        break;
                    case "94":
                        bp_Test = "480";//94
                        break;
                    case "99":
                        bp_Test = "连续测量模式";//99
                        break;
                    default:
                        bp_Test = "手动";
                        break;
                }
                //endregion

                //region 测量状态
                d = (char) (int) (data[12]);
                e = (char) (int) (data[13]);
                mode = String.valueOf(d) + String.valueOf(e);
                switch (mode) {
                    case "00":
                        bp_Error = ("测量完成");//00，无错误
                        break;
                    case "02":
                        bp_Error = ("自检失败");//02，
                        break;
                    case "03":
                        bp_Error = ("测量完成");//03，无错误
                        break;
                    case "06":
                        bp_Error = ("袖带过松");//06，可能是袖带缠绕过松，或未接袖带
                        break;
                    case "07":
                        bp_Error = ("漏气");//07，可能是阀门或气路中漏气
                        break;
                    case "08":
                        bp_Error = ("气压错误");//08，可能是阀门无法正常打开
                        break;
                    case "09":
                        bp_Error = ("弱信号");//09，可能是测量对象脉搏太弱或袖带过松
                        break;
                    case "10":
                        bp_Error = ("超范围");//10，可能是测量对象的血压值超过了测量范围
                        break;
                    case "11":
                        bp_Error = ("过分运动");//11，可能是测量时，信号中含有运动伪差或太多干扰
                        break;
                    case "12":
                        bp_Error = ("过压");//12，袖带压超过300mmHg
                        break;
                    case "13":
                        bp_Error = ("信号饱和");//13，由于运动或其他原因使信号幅度太大
                        break;
                    case "14":
                        bp_Error = ("漏气");//14，在漏气检测中，发现系统气路漏气
                        break;
                    case "15":
                        bp_Error = ("系统错误");//15，开机后，充气泵、A/D采样、压力传感器出错，或者软件运行中指针出错
                        break;
                    case "19":
                        bp_Error = ("测量超时");//19，测量时间超过120秒
                        break;
                    default:
                        bp_Error = "---";
                        break;
                }
                //endregion

                //region 血压值
                a = (char) (int) (data[16]);
                b = (char) (int) (data[17]);
                c = (char) (int) (data[18]);
                bp_H = String.valueOf(a) + String.valueOf(b) + String.valueOf(c);

                a = (char) (int) (data[19]);
                b = (char) (int) (data[20]);
                c = (char) (int) (data[21]);
                bp_L = String.valueOf(a) + String.valueOf(b) + String.valueOf(c);

                a = (char) (int) (data[22]);
                b = (char) (int) (data[23]);
                c = (char) (int) (data[24]);
                bp_Avg = String.valueOf(a) + String.valueOf(b) + String.valueOf(c);

                a = (char) (int) (data[27]);
                b = (char) (int) (data[28]);
                c = (char) (int) (data[29]);
                bp_Pulse = String.valueOf(a) + String.valueOf(b) + String.valueOf(c);
                //endregion

                break;
        }
    }

    /**
     * 身份证解析
     *
     * @param data
     * @throws UnsupportedEncodingException
     */
    private void Parsing_id(byte[] data) throws UnsupportedEncodingException {
        switch (data.length) {
            case 11:
                switch (data[9]) {
                    case (byte) 0x80:
                        idState = "寻找证/卡失败";
                        break;
                    case (byte) 0x81:
                        idState = "选取证/卡失败";
                        break;
                    case (byte) 0x91:
                        idState = "证/卡中此项无内容";
                        break;
                }
                break;
            case 15:
                if (data[9] == (byte) 0x9F && data[14] == (byte) 0x97) {
                    idState = "寻卡成功";
                }
                break;
            case 19:
                if (data[9] == (byte) 0x90 && data[18] == (byte) 0x9C) {
                    idState = "选卡成功";
                }
                break;
            case 1295:
                byte[] dataBuf = new byte[256];
                for (int i = 0; i < 256; i++) {
                    dataBuf[i] = data[14 + i];
                }
                String TmpStr = new String(dataBuf, "UTF16-LE");
                TmpStr = new String(TmpStr.getBytes("UTF-8"));
                if (!"".equals(TmpStr)) {
                    idInfo[0] = TmpStr.substring(0, 15);
                    idInfo[1] = TmpStr.substring(15, 16);
                    idInfo[2] = TmpStr.substring(16, 18);
                    idInfo[3] = TmpStr.substring(18, 26);
                    idInfo[4] = TmpStr.substring(26, 61);
                    idInfo[5] = TmpStr.substring(61, 79);
                    idInfo[6] = TmpStr.substring(79, 94);
                    idInfo[7] = TmpStr.substring(94, 102);
                    idInfo[8] = TmpStr.substring(102, 110);
                    idInfo[9] = TmpStr.substring(110, 128);
                    if (idInfo[1].equals("1"))
                        idInfo[1] = "男";
                    else
                        idInfo[1] = "女";
                    try {
                        int code = Integer.parseInt(idInfo[2].toString());
                        idInfo[2] = DataUtil.decodeNation(code);
                    } catch (Exception e) {
                        idInfo[2] = "";
                    }
                } else {
                    id_data = null;
                }
                //照片解析
//                try {
//                    int ret = com.ivsign.android.IDCReader.IDCReaderSDK.wltInit(Environment.getExternalStorageDirectory() + "/wltlib");
//                    if (ret == 0) {
//                        byte[] datawlt = new byte[1024];
//                        byte[] byLicData = {(byte) 0x05, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x5B, (byte) 0x03, (byte) 0x33, (byte) 0x01, (byte) 0x5A, (byte) 0xB3, (byte) 0x1E, (byte) 0x00};
//                        for (int i = 0; i < 1024; i++) {
//                            datawlt[i] = data[270 + i];
//                        }
//                        int t = com.ivsign.android.IDCReader.IDCReaderSDK.wltGetBMP(datawlt, byLicData);
//                        if (t == 6)
//                            idState = "照片解码异常";
//                        break;
//                    }
//                } catch (Exception ex) {
//                    idState = "照片解码异常";
//                }

        }
    }

    /**
     * 发送血压命令
     *
     * @param cmdPort 串口对象
     * @param cmd     命令
     */
    public static void SendBpCmd(MySerialPort cmdPort, byte[] cmd) {
        try {
            byte[] cmdByte;
            if (cmd.length == 1) {
                cmdByte = new byte[]{0x02, cmd[0], 0x03};
            } else {
                cmdByte = new byte[]{0x02, cmd[0], cmd[1], 0x3B, 0x3B, cmd[2], cmd[3], 0x03};
            }
            cmdPort.Write(cmdByte);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送单片机命令
     *
     * @param cmdPort 串口对象
     * @param cmd     命令
     */
    public static void SendMcuCmd(MySerialPort cmdPort, byte[] cmd) {
        try {
            byte[] cmdByte;
            cmdByte = new byte[]{(byte) 0xFF, 0x30, 0x31, cmd[0], 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0xee};
            cmdPort.Write(cmdByte);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
