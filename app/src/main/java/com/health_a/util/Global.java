package com.health_a.util;

import android.util.Log;

import com.health_a.parsing.Ecg_Parsing;

import android_serialport_api.MySerialPort;

/**
 * Created by Admin on 2016/1/26. 全局变量
 */
public class Global {

    public static MySerialPort ecg_Com = new MySerialPort();  //心电串口
    public static MySerialPort spo2_Com = new MySerialPort(); //血氧串口
    public static MySerialPort mcu_Com = new MySerialPort();  //单片机串口

    public static Ecg_Parsing ecg = new Ecg_Parsing();    //心电协议解析

    public static boolean isEcgAll = false;

    public static String UserID = "";       //关联的用户IDCard
    public static String DoctorID = "";     //当前登录医生的ID
    public static String DoctorName = "";   //当前登录医生的姓名
    public static int DoctorLevel = 0;      //当前登录医生的权限级别

    public static String TestTime = "";     //当前条检测信息的测试时间
    public static String TestName = "";     //当前条检测信息的项目名称

    public static int index = 0;//临时变量调试使用

    public static int ecg_gain = 0;

    //region 医生信息操作
    // 获取当前的医生登录信息
    public static String GetDoctorInfo() {
        Log.e("医生姓名", "" + DoctorID + " " + DoctorName);
        return DoctorID;
    }
    // 设置当前的医生登录信息
    public static void SetDoctorInfo(String id,String name,int level) {
        DoctorID = id;
        DoctorName = name;
        DoctorLevel = level;
    }
    // 删除医生登录状态
    public static void DeleteDoctorLoginStart() {
        DoctorID = "";
        DoctorName = "";
        DoctorLevel = 0;
    }
    //endregion

}
