package com.health_a.util;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Admin on 2016/1/21.
 */
public class Utils {

    public static boolean isViewEmpty(TextView view) {
        if (isStringEmpty(strFromView(view))) {
            return true;
        }
        return false;
    }

    public static String strFromView(TextView view) {
        String strText = "";
        if (null != view) {
            strText = view.getText().toString().trim();
        }
        return strText;
    }

    public static boolean isStringEmpty(String str) {
        if (null == str || "".equals(str)) {
            return true;
        }
        return false;
    }

    /**
     * 身份证的有效验证*****************************************************************
     *
     * @param IDStr 身份证号
     * @return 有效：true 无效：其他字符均代表无效（提示了无效的原因）
     * @throws ParseException
     * @throws NumberFormatException
     */
    public static String IDCardValidate(String IDStr) {
        String[] ValCodeArr = {"1", "0", "X", "9", "8", "7", "6", "5", "4",
                "3", "2"};
        String[] Wi = {"7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7",
                "9", "10", "5", "8", "4", "2"};
        String Ai = "";

        // 号码的长度 15位或18位
        if (IDStr.length() != 15 && IDStr.length() != 18) {
            return "身份证位数不对";
        }

        // 数字 除最后以为都为数字
        if (IDStr.length() == 18) {
            Ai = IDStr.substring(0, 17);
        } else if (IDStr.length() == 15) {
            Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);
        }
        if (isNumeric(Ai) == false) {
            return "前17位不全是数字";
        }

        String strYear = Ai.substring(6, 10);// 年份
        String strMonth = Ai.substring(10, 12);// 月份
        String strDay = Ai.substring(12, 14);// 日期

        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
                    || (gc.getTime().getTime() - s.parse(
                    strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
                // 生日不在有效范围
                return "生日超过150岁小于现在了";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
            // 月份无效
            return "月份无效";
        }
        if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
            // 日期无效
            return "日期无效";
        }

        // 地区码
        Hashtable h = GetAreaCode();
        if (h.get(Ai.substring(0, 2)) == null) {// 地区编码错误
            return "地区码不对";
        }

        // 判断最后一位的值
        int TotalmulAiWi = 0;
        for (int i = 0; i < 17; i++) {
            TotalmulAiWi = TotalmulAiWi
                    + Integer.parseInt(String.valueOf(Ai.charAt(i)))
                    * Integer.parseInt(Wi[i]);
        }
        int modValue = TotalmulAiWi % 11;
        String strVerifyCode = ValCodeArr[modValue];
        Ai = Ai + strVerifyCode;

        if (IDStr.length() == 18) {
            if (Ai.equals(IDStr) == false) {
                return "ai与idStr不等";
            }
        } else {
            return "true";
        }
        return "true";
    }

    /**
     * 判断字符串是否为数字
     *
     * @param str
     * @return
     */
    private static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (isNum.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 中国省份
     */
    private static Hashtable<String, String> GetAreaCode() {
        Hashtable<String, String> hashtable = new Hashtable<String, String>();
        hashtable.put("11", "北京");
        hashtable.put("12", "天津");
        hashtable.put("13", "河北");
        hashtable.put("14", "山西");
        hashtable.put("15", "内蒙古");
        hashtable.put("21", "辽宁");
        hashtable.put("22", "吉林");
        hashtable.put("23", "黑龙江");
        hashtable.put("31", "上海");
        hashtable.put("32", "江苏");
        hashtable.put("33", "浙江");
        hashtable.put("34", "安徽");
        hashtable.put("35", "福建");
        hashtable.put("36", "江西");
        hashtable.put("37", "山东");
        hashtable.put("41", "河南");
        hashtable.put("42", "湖北");
        hashtable.put("43", "湖南");
        hashtable.put("44", "广东");
        hashtable.put("45", "广西");
        hashtable.put("46", "海南");
        hashtable.put("50", "重庆");
        hashtable.put("51", "四川");
        hashtable.put("52", "贵州");
        hashtable.put("53", "云南");
        hashtable.put("54", "西藏");
        hashtable.put("61", "陕西");
        hashtable.put("62", "甘肃");
        hashtable.put("63", "青海");
        hashtable.put("64", "宁夏");
        hashtable.put("65", "新疆");
        hashtable.put("71", "台湾");
        hashtable.put("81", "香港");
        hashtable.put("82", "澳门");
        hashtable.put("91", "国外");
        return hashtable;
    }

    /**
     * 根据出生日期或身份证计算年龄
     *
     * @param csrq
     * @return
     */
    public static int getAge(String csrq) {
        SimpleDateFormat ft = new SimpleDateFormat("yyyy");

        int age = 0;
        if (csrq.length() == 8) {
            csrq = csrq.substring(0, 4);
            int cs = Integer.parseInt(csrq);
            int xz = Integer.parseInt(ft.format(new Date()));
            age = xz - cs;
        } else if (csrq.length() >= 15) {
            csrq = csrq.substring(6, 10);
            int cs = Integer.parseInt(csrq);
            int xz = Integer.parseInt(ft.format(new Date()));
            age = xz - cs;
        }

        return age;
    }

    /**
     * 转换日期格式 如： 20160101 转 2016-01-01
     *
     * @param csrq
     * @return
     */
    public static String getCsrq(String csrq) {
        if (csrq.length() == 8) {
            csrq = csrq.substring(0, 4) + "-" + csrq.substring(4, 6) + "-"
                    + csrq.substring(6, 8);

        } else if (csrq.length() >= 15) {
            csrq = csrq.substring(6, 10) + "-" + csrq.substring(10, 12) + "-"
                    + csrq.substring(12, 14);
        }
        return csrq;
    }

    /**
     * 是否是整数
     *
     * @param str
     * @return
     */
    public static boolean isInteger(String str) {
        try {
            Integer intValue = Integer.valueOf(str);
            return true;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 是否是浮点数
     *
     * @param str
     * @return
     */
    public static boolean isFloat(String str) {
        try {
            Float floatValue = Float.valueOf(str);
            return true;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 是否包含半角英字、数字以外的字符(A-Z,a-z,0-9)
     *
     * @param regexString
     * @return
     */
    public static boolean isIncludedInHalfCharacter(String regexString) {

        boolean flag = true;
        final String regex = "^[A-Za-z0-9]+$";
        Pattern p = Pattern.compile(regex);
        Matcher matcher = p.matcher(regexString);
        boolean isMatch = matcher.matches();
        if (isMatch) {
            flag = true;
        } else {
            flag = false;
        }

        return flag;
    }

    /**
     * @return 系统当前时间
     */
    public static String getNowTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置时间格式
        return df.format(new Date());//new Date();获取当前系统时间
    }

    //
    public static void AlertDialog(Context context,String msg){
        final AlertDialog builder = new AlertDialog(context);
        builder.setMessage(msg);
        builder.setPositiveButton("确   定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });
    }
}

