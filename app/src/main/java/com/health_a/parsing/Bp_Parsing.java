package com.health_a.parsing;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android_serialport_api.MySerialPort;


/**
 * Created by Admin on 2016/3/4.
 */
public class Bp_Parsing {

    private byte[] bytes = new byte[512];
    private byte[] data1 = new byte[6];
    private byte[] data2 = new byte[10];
    private byte[] data3 = new byte[42];
    private final List<Byte> list = new ArrayList<>();

    private String cuff_Pressure;   //袖带压
    private String bp_H = "--";     //收缩压
    private String bp_L = "--";       //舒张压
    private String bp_Avg = "--";   //平均压
    private String bp_Pulse = "--"; //脉率
    private String bp_Error = "--"; //错误代码
    private String bp_Mode = "--";  //病人模式
    private String bp_state = "--"; //系统状态
    private String bp_Test = "--";  //测量模式
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

    public Bp_Parsing() {

    }

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


    /**
     * 发送命令
     *
     * @param cmdPort 串口对象
     * @param cmd     命令
     */
    public static void SendCmd(MySerialPort cmdPort, byte[] cmd) {
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
     * 解析血压数据
     *
     * @param port 串口对象
     */
    public void Parsing(MySerialPort port) {
        try {
            bytes = port.Read();

            if (bytes != null) {
                //Log.e("有数据","数据");
                for (byte aByte : bytes) {
                    list.add(aByte);
                }
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i) == 0x02) {
                        if (i + 5 < list.size()) {//测量结束标志包
                            if (list.get(i + 4) == 0x03 && list.get(i + 5) == 0x0D) {
                                for (int index = 0; index < 6; index++) {
                                    data1[index] = list.get(i);
                                    list.remove(i);
                                }
                                i = i - 1;
                                char a = (char) (int) (data1[1]);
                                char b = (char) (int) (data1[2]);
                                char c = (char) (int) (data1[3]);
                                bp_state = "测量终止";
                                SendCmd(port,bp_State);
                                break;
                            }
                        } else
                            break;
                        if (i + 9 < list.size()) {//袖带压
                            if (list.get(i + 8) == 0x03 && list.get(i + 9) == 0x0D) {
                                for (int index = 0; index < 10; index++) {
                                    data2[index] = list.get(i);
                                    list.remove(i);
                                }
                                i = i - 1;
                                char a = (char) (int) (data2[1]);
                                char b = (char) (int) (data2[2]);
                                char c = (char) (int) (data2[3]);
                                cuff_Pressure = String.valueOf(a) + String.valueOf(b) + String.valueOf(c);
                            }
                        } else
                            break;
                        if (i + 41 < list.size()) {//系统状态
                            if (list.get(i + 40) == 0x03 && list.get(i + 41) == 0x0D) {
                                Log.e("返回", "系统状态");
                                for (int index = 0; index < 42; index++) {
                                    data3[index] = list.get(i);
                                    list.remove(i);
                                }
                                i = i - 1;

                                switch (data3[2]) {
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
                                switch (data3[5]) {
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
                                char d = (char) (int) (data3[8]);
                                char e = (char) (int) (data3[9]);
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
                                        bp_Test = "---";
                                        break;
                                }
                                d = (char) (int) (data3[12]);
                                e = (char) (int) (data3[13]);
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
                                char a = (char) (int) (data3[16]);
                                char b = (char) (int) (data3[17]);
                                char c = (char) (int) (data3[18]);
                                bp_H = String.valueOf(a) + String.valueOf(b) + String.valueOf(c);

                                a = (char) (int) (data3[19]);
                                b = (char) (int) (data3[20]);
                                c = (char) (int) (data3[21]);
                                bp_L = String.valueOf(a) + String.valueOf(b) + String.valueOf(c);

                                a = (char) (int) (data3[22]);
                                b = (char) (int) (data3[23]);
                                c = (char) (int) (data3[24]);
                                bp_Avg = String.valueOf(a) + String.valueOf(b) + String.valueOf(c);

                                a = (char) (int) (data3[27]);
                                b = (char) (int) (data3[28]);
                                c = (char) (int) (data3[29]);
                                bp_Pulse = String.valueOf(a) + String.valueOf(b) + String.valueOf(c);
                            }
                        } else
                            break;
                    }
                }
            } else {
                //Log.e("null", "null");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
