package com.health_a.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Admin on 2016/1/5. 数据库类
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "Health SQLite";

    //类没有实例化,是不能用作父类构造器的参数,必须声明为静态
    private static final String DATABASE_NAME = "Health.db"; //数据库名称
    private static final int DATABASE_VERSION = 1; //数据库版本

    private String DOCTORSQL = "create table doctor(" +
            "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "name nvarchar(6)," +
            "sex char(1)," +
            "password nvarchar(6)," +
            "level INTEGER(1) NOT NULL)";

    private String USERSQL = "create table user(" +
            "IDCard nvarchar(18) PRIMARY KEY," +
            "name nvarchar(18)," +
            "sex char(1)," +
            "age nvarchar(3)," +
            "phone nvarchar (16))";

    private String TEMPSQL = "create table tempTest(" +
            "IDCard nvarchar(18) REFERENCES user ( IDCard )," +
            "time nvarchar(18)," +
            "temp nvarchar (5)," +
            "doctorID INTEGER NOT NULL REFERENCES doctor (ID))";

    private String BSSQL = "create table bsTest(" +
            "IDCard nvarchar(18) REFERENCES user ( IDCard )," +
            "time nvarchar(18)," +
            "FBG INTEGER(1)," +
            "bs INTEGER (2,1)," +
            "doctorID INTEGER NOT NULL REFERENCES doctor (ID))";

    private String UMSQL = "create table umTest(" +
            "IDCard nvarchar(18) REFERENCES user ( IDCard )," +
            "time nvarchar(5)," +
            "LEU nvarchar(5)," +
            "NIT nvarchar(5)," +
            "UBG nvarchar(5)," +
            "PRO nvarchar(5)," +
            "PH nvarchar(5)," +
            "SG nvarchar(5)," +
            "BLD nvarchar(5)," +
            "KET nvarchar(5)," +
            "BIL nvarchar(5)," +
            "GLU nvarchar(5)," +
            "VC nvarchar(5)," +
            "doctorID INTEGER NOT NULL REFERENCES doctor (ID))";

    private String BPSQL = "create table bpTest(" +
            "IDCard nvarchar(18) REFERENCES user ( IDCard )," +
            "time nvarchar(18)," +
            "Hbp nvarchar(4)," +
            "Lbp nvarchar(4)," +
            "Mbp nvarchar(4)," +
            "doctorID INTEGER NOT NULL REFERENCES doctor (ID))";

    private String SPO2SQL = "create table spo2Test(" +
            "IDCard nvarchar(18) REFERENCES user ( IDCard )," +
            "time nvarchar(18)," +
            "spo2 nvarchar(4)," +
            "pulse nvarchar (4)," +
            "doctorID INTEGER NOT NULL REFERENCES doctor (ID))";

    private String ECGSQL = "create table ecgTest(" +
            "IDCard nvarchar(18) REFERENCES user ( IDCard )," +
            "time nvarchar(18)," +
            "value INTEGER (4)," +
            "doctorID INTEGER NOT NULL REFERENCES doctor (ID))";


    /**
     * 第一个参数是应用的上下文
     * 第二个参数是应用的数据库名字
     * 第三个参数CursorFactory指定在执行查询时获得一个游标实例的工厂类,设置为null,代表使用系统默认的工厂类
     * 第四个参数是数据库版本，必须是大于0的int（即非负数）
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //输出创建数据库的日志信息
        Log.i(TAG, "create Database------------->");
        //execSQL函数用于执行SQL语句
        db.execSQL(DOCTORSQL);  //医生表
        db.execSQL(USERSQL);    //用户表
        db.execSQL(TEMPSQL);    //温度表
        db.execSQL(BSSQL);      //血糖表
        db.execSQL(UMSQL);      //尿常规表
        db.execSQL(BPSQL);      //血压表
        db.execSQL(SPO2SQL);    //血氧表
        db.execSQL(ECGSQL);     //心电表
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //输出更新数据库的日志信息
        Log.i(TAG, "update Database------------->");
        //db.execSQL("ALTER TABLE user ADD phone VARCHAR(12) NULL "); //往表中增加一列
    }
}
