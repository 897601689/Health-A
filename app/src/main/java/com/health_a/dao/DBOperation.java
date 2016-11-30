package com.health_a.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.health_a.bean.BsTest;
import com.health_a.bean.DoctorInfo;
import com.health_a.bean.Spo2Test;
import com.health_a.bean.TempTest;
import com.health_a.bean.UmTest;
import com.health_a.bean.UserInfo;


/**
 * Created by Admin on 2016/1/8. 数据库操作类
 */
public class DBOperation {
    private DatabaseHelper helper = null;
    private SQLiteDatabase db = null;
    String sql = null;
    Context context;

    public DBOperation(Context context) {
        helper = new DatabaseHelper(context);
        this.context = context;
    }

/*******医生信息操作****************************************************************/

    /**
     * 添加医生信息
     *
     * @param doctor 医生信息类
     */
    public void AddDoctorInfo(DoctorInfo doctor) {
        db = helper.getWritableDatabase();
        sql = "insert into doctor(ID,name,sex,password,level) values(?,?,?,?,?)";
        db.beginTransaction();
        try {
            db.execSQL(sql, new String[]{doctor.getID(),doctor.getName(),doctor.getSex(), doctor.getPassword(), doctor.getLevel()});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    /**
     * 根据ID删除医生信息
     * @param ID 工作编号
     * @return true为成功
     */
    public boolean DeleteDoctorInfo(String ID) {
        boolean flag = false;
        db = helper.getWritableDatabase();
        db.beginTransaction();
        try {
            int count = db.delete("doctor", "ID=?", new String[]{ID});
            if (count > 0) {
                flag = true;
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
        return flag;
    }

    /**
     * 判断医生登录信息
     *
     * @param id       医生ID
     * @param password 登录密码
     * @return true 为登录成功
     */
    public boolean DoctorExist(String id, String password) {
        boolean flag = false;
        sql = "select * from doctor where ID = ? and password = ?";
        db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, new String[]{id, password});
        if (cursor.getCount() > 0) {
            flag = true;
        }
        cursor.close();
        db.close();
        return flag;
    }

    /**
     * 根据医生ID查询医生姓名
     *
     * @param id 医生的ID
     * @return 返回姓名 不存在返回""
     */
    public String GetDoctorNameByID(String id) {
        String name = "";
        sql = "select name from doctor where ID = ?";
        db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, new String[]{id});
        if (cursor.moveToNext()) {
            name = cursor.getString(0);
        }
        cursor.close();
        return name;
    }

    /**
     * 根据医生ID查询医生职位等级
     *
     * @param id 医生的ID
     * @return 返回等级 不存在返回""
     */
    public int GetDoctorLevelByID(String id) {
        int level = 0;
        sql = "select level from doctor where ID = ?";
        db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, new String[]{id});
        if (cursor.moveToNext()) {
            level = cursor.getInt(0);
        }
        cursor.close();
        return level;
    }

    public Cursor GetDoctorInfoByID(String id) {
        db = helper.getReadableDatabase();
        String sqlId = "select * from doctor where ID like ?";
        Cursor cursor = db.rawQuery(sqlId, new String[]{"%" + id + "%"});
        if (cursor.getCount() == 0) {
            cursor = null;
        }
        db.close();
        return cursor;
    }

    public Cursor GetDoctorInfoByName(String name) {
        db = helper.getReadableDatabase();
        String sqlId = "select * from doctor where name like ?";
        Cursor cursor = db.rawQuery(sqlId, new String[]{"%" + name + "%"});
        if (cursor.getCount() == 0) {
            cursor = null;
        }
        db.close();
        return cursor;
    }

    public Cursor GetDoctorInfoByIDAndName(String id, String name) {
        db = helper.getReadableDatabase();
        String sqlId = "select * from doctor where ID like ? and name like ?";
        Cursor cursor = db.rawQuery(sqlId, new String[]{"%" + id + "%", "%" + name + "%"});
        if (cursor.getCount() == 0) {
            cursor = null;
        }
        db.close();
        return cursor;
    }

    public Cursor GetAllDoctorInfo() {
        db = helper.getReadableDatabase();
        String sqlId = "select * from doctor";
        Cursor cursor = db.rawQuery(sqlId, null);
        if (cursor.getCount() == 0) {
            cursor = null;
        }
        db.close();
        return cursor;
    }

    public int GetDoctorID(){
        int id = 0;
        sql = "select * from doctor order by ID desc";
        db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToNext()) {
            id = cursor.getInt(0);
        }
        cursor.close();
        return id;
    }
    /**
     * 修改医生信息
     *
     * @param ID 医生工作证
     * @param doctor 医生信息类
     * @return true为修改成功
     */
    public boolean UpdateDoctorInfo(String ID, DoctorInfo doctor) {
        boolean flag = false;
        db = helper.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put("name", doctor.getName());
            values.put("password", doctor.getPassword());
            values.put("sex", doctor.getSex());
            values.put("level", doctor.getLevel());
            int count = db.update("doctor", values, "ID=?", new String[]{ID});
            if (count > 0) {
                flag = true;
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
        return flag;
    }
/*******医生信息操作****************************************************************/

/*******用户信息操作****************************************************************/
    /**
     * 根据身份证号查询用户是否存在
     *
     * @param CardId 身份证号
     * @return 返回true为已存在
     */
    public boolean UserExist(String CardId) {
        db = helper.getReadableDatabase();
        boolean flag = false;
        String sqlId = "select * from user where IDCard = ?";
        Cursor cursor = db.rawQuery(sqlId, new String[]{CardId});
        if (cursor.getCount() > 0) {
            flag = true;
        }
        cursor.close();
        db.close();
        return flag;
    }

    /**
     * 添加用户信息
     *
     * @param user 用户信息类
     */
    public void AddUserInfo(UserInfo user) {
        db = helper.getWritableDatabase();
        sql = "insert into user(IDCard,name,sex,age,phone) values(?,?,?,?,?)";
        db.beginTransaction();
        try {
            db.execSQL(sql, new String[]{user.getIDCard(), user.getName(), user.getSex(), user.getAge(), user.getPhone()});
            db.setTransactionSuccessful();
        } catch (SQLException ex) {
            Log.e("", ex.toString());
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    /**
     * 根据IDCard删除用户信息
     *
     * @param IDCard 身份证号
     */
    public boolean DeleteUserInfo(String IDCard) {
        boolean flag = false;
        //sql = "delete from user where IDCard = ?"; //为了获取是否删除成功 因此使用db.delete
        db = helper.getWritableDatabase();
        db.beginTransaction();
        try {
            int count = db.delete("user", "IDCard=?", new String[]{IDCard});
            if (count > 0) {
                flag = true;
            }
            //db.execSQL(sql,new String[]{IDCard});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
        return flag;
    }

    /**
     * 修改用户信息
     *
     * @param IDCard 用户身份证
     * @param user   用户信息类
     * @return true为修改成功
     */
    public boolean UpdateUserInfo(String IDCard, UserInfo user) {
        boolean flag = false;
        db = helper.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put("name", user.getName());
            values.put("sex", user.getSex());
            values.put("age", user.getAge());
            values.put("phone", user.getPhone());
            int count = db.update("user", values, "IDCard=?", new String[]{IDCard});
            if (count > 0) {
                flag = true;
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
        return flag;
    }

    /**
     * 按 IDCard 查询用户信息
     *
     * @param IDCard 身份证号
     * @return Cursor
     */
    public Cursor GetUserByCardId(String IDCard) {
        db = helper.getReadableDatabase();
        String sqlId = "select * from user where IDCard like ?";
        Cursor cursor = db.rawQuery(sqlId, new String[]{"%" + IDCard + "%"});
        if (cursor.getCount() == 0) {
            cursor = null;
        }
        db.close();
        return cursor;
    }

    /**
     * 按 name 查询用户信息
     *
     * @param name 姓名
     * @return Cursor
     */
    public Cursor GetUserByName(String name) {
        db = helper.getReadableDatabase();
        String sqlId = "select * from user where name like ?";
        Cursor cursor = db.rawQuery(sqlId, new String[]{"%" + name + "%"});
        if (cursor.getCount() == 0) {
            cursor = null;
        }
        db.close();
        return cursor;
    }

    /**
     * 按 IDCard和name 同时查询用户信息
     *
     * @param IDCard 身份证号
     * @param name   姓名
     * @return Cursor
     */
    public Cursor GetUserByNameAndIDCard(String IDCard, String name) {
        db = helper.getReadableDatabase();
        String sqlId = "select * from user where IDCard like ? and name like ?";
        Cursor cursor = db.rawQuery(sqlId, new String[]{"%" + IDCard + "%", "%" + name + "%"});
        if (cursor.getCount() == 0) {
            cursor = null;
        }
        db.close();
        return cursor;
    }

    /**
     * 获取所有用户信息
     *
     * @return Cursor
     */
    public Cursor GetAllUserInfo() {
        db = helper.getReadableDatabase();
        String sqlId = "select * from user";
        Cursor cursor = db.rawQuery(sqlId, null);
        if (cursor.getCount() == 0) {
            cursor = null;
        }
        db.close();
        return cursor;
    }

/*******用户信息操作****************************************************************/

/*******用户检测信息操作****************************************************************/
    /**
     * 添加体温检测数据
     *
     * @param tempTest 体温检测类
     */
    public void AddTempTestData(TempTest tempTest) {
        sql = "insert into tempTest(IDCard,time,temp,doctorID) values(?,?,?,?)";
        db = helper.getWritableDatabase();
        db.beginTransaction();
        try {
            db.execSQL(sql, new String[]{tempTest.getIDCard(), tempTest.getTime(), tempTest.getTemp(), tempTest.getDoctor()});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    /**
     * 根据 IDCard 删除体温检测数据
     *
     * @param IDCard 身份证号
     * @return true 为修改成功
     */
    public boolean DeleteTempTsetData(String IDCard) {
        boolean flag = false;
        db = helper.getWritableDatabase();
        db.beginTransaction();
        try {
            int count = db.delete("tempTest", "IDCard=?", new String[]{IDCard});
            if (count > 0) {
                flag = true;
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
        return flag;
    }

    /**
     * 根据 IDCard 修改体温检测数据
     *
     * @param IDCard   用户身份证
     * @param tempTest 体温检测类
     * @return true为修改成功
     */
    public boolean UpdateTempTestData(String IDCard, TempTest tempTest) {
        boolean flag = false;
        db = helper.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put("IDCard", tempTest.getIDCard());
            values.put("time", tempTest.getTime());
            values.put("temp", tempTest.getTemp());
            values.put("doctorID", tempTest.getDoctor());
            int count = db.update("tempTest", values, "IDCard=?", new String[]{IDCard});
            if (count > 0) {
                flag = true;
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
        return flag;
    }

    /**
     * 根据 IDCard 查询体温检测数据
     *
     * @param IDCard 身份证号
     * @return Cursor
     */
    public Cursor TempTestDataByID(String IDCard) {
        db = helper.getReadableDatabase();
        String sqlId = "select * from tempTest where IDCard = ?";
        Cursor cursor = db.rawQuery(sqlId, new String[]{IDCard});
        if (cursor.getCount() == 0) {
            cursor = null;
        }
        db.close();
        return cursor;
    }

    /**
     * 添加血糖检测数据
     *
     * @param bsTest 血糖检测类
     */
    public void AddBsTestData(BsTest bsTest) {
        sql = "insert into bsTest(IDCard,time,fbg,bs,doctorID) values(?,?,?,?,?)";
        db = helper.getWritableDatabase();
        db.beginTransaction();
        try {
            db.execSQL(sql, new String[]{bsTest.getIDCard(), bsTest.getTime(), bsTest.getFbg(), bsTest.getBs(), bsTest.getDoctor()});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    /**
     * 根据 IDCard 删除血糖检测数据
     *
     * @param IDCard 身份证号
     * @return true为删除成功
     */
    public boolean DeleteBsTestData(String IDCard) {
        boolean flag = false;
        db = helper.getWritableDatabase();
        db.beginTransaction();
        try {
            int count = db.delete("bsTest", "IDCard=?", new String[]{IDCard});
            if ((count > 0)) {
                flag = true;
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }

        return flag;
    }

    /**
     * 根据 IDCard 修改血糖检测数据
     *
     * @param IDCard 身份证号
     * @param bsTest 血糖检测列
     * @return true为修改成功
     */
    public boolean UpdateBsTestData(String IDCard, BsTest bsTest) {
        boolean flag = false;
        db = helper.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put("IDCard", bsTest.getIDCard());
            values.put("time", bsTest.getTime());
            values.put("fbg", bsTest.getFbg());
            values.put("bs", bsTest.getBs());
            values.put("doctorID", bsTest.getDoctor());
            int count = db.update("bsTest", values, "IDCard=?", new String[]{IDCard});
            if (count > 0) {
                flag = true;
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
        return flag;
    }

    /**
     * 根据 IDCard 查询血糖检测数据
     *
     * @param IDCard 身份证号
     * @return Cursor
     */
    public Cursor BsTestDataByID(String IDCard) {
        db = helper.getReadableDatabase();
        String sqlId = "select * from bsTest where IDCard = ?";
        Cursor cursor = db.rawQuery(sqlId, new String[]{IDCard});
        if (cursor.getCount() == 0) {
            cursor = null;
        }
        db.close();
        return cursor;
    }

    /**
     * 添加尿常规检测数据
     *
     * @param umTest 尿常规检测类
     */
    public void AddUmTestData(UmTest umTest) {
        sql = "insert into umTest(IDCard,time,LEU,NIT,UBG,PRO,PH,SG,BLD,KET,BIL,GLU,VC,doctorID)";
        db = helper.getWritableDatabase();
        db.beginTransaction();
        try {
            db.execSQL(sql, new String[]{umTest.getIDCard(), umTest.getTime(), umTest.getLEU(),
                    umTest.getNIT(), umTest.getUBG(), umTest.getPRO(), umTest.getPH(), umTest.getSG(),
                    umTest.getBLD(), umTest.getKET(), umTest.getBIL(), umTest.getGLU(), umTest.getVC(), umTest.getDoctor()});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    /**
     * 根据 IDCard 删除尿常规检测数据
     *
     * @param IDCard 身份证号
     * @return true为删除成功
     */
    public boolean DeleteUmTestData(String IDCard) {
        boolean flag = false;
        db = helper.getWritableDatabase();
        db.beginTransaction();
        try {
            int count = db.delete("umTest", "IDCard=?", new String[]{IDCard});
            if ((count > 0)) {
                flag = true;
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
        return flag;
    }

    /**
     * 根据 IDCard 修改尿常规检测数据
     *
     * @param IDCard 身份证号
     * @param umTest 尿常规检测类
     * @return true为修改成功
     */
    public boolean UpdateUmTestData(String IDCard, UmTest umTest) {
        boolean flag = false;
        db = helper.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put("IDCard", umTest.getIDCard());
            values.put("time", umTest.getTime());
            values.put("LEU", umTest.getLEU());
            values.put("NIT", umTest.getNIT());
            values.put("UBG", umTest.getUBG());
            values.put("PRO", umTest.getPRO());
            values.put("PH", umTest.getPH());
            values.put("SG", umTest.getSG());
            values.put("BLD", umTest.getBLD());
            values.put("KET", umTest.getKET());
            values.put("BIL", umTest.getBIL());
            values.put("GLU", umTest.getGLU());
            values.put("VC", umTest.getVC());
            values.put("doctorID", umTest.getDoctor());
            int count = db.update("umTemp", values, "IDCard=?", new String[]{IDCard});
            if (count > 0) {
                flag = true;
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();

        }
        return flag;
    }

    /**
     * 根据 IDCard 查询尿常规检测数据
     *
     * @param IDCard 身份证号
     * @return Cursor
     */
    public Cursor UmTestDataId(String IDCard) {
        db = helper.getReadableDatabase();
        String sqlId = "select * from umTest where IDCard = ?";
        Cursor cursor = db.rawQuery(sqlId, new String[]{IDCard});
        if (cursor.getCount() == 0) {
            cursor = null;
        }
        db.close();
        return cursor;
    }

    /**
     * 添加血氧检测数据
     *
     * @param spo2Test 血氧检测类
     */
    public void AddSpo2TestData(Spo2Test spo2Test) {
        sql = "insert into spo2Test(IDCard,time,spo2,pulse,doctorID) values(?,?,?,?,?)";
        db = helper.getWritableDatabase();
        db.beginTransaction();
        try {
            db.execSQL(sql, new String[]{spo2Test.getIDCard(), spo2Test.getTime(), spo2Test.getSpo2(), spo2Test.getPulse(), spo2Test.getDoctor()});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    /**
     * 根据 IDCard 删除血氧检测数据
     *
     * @param IDCard 身份证号
     * @return true为删除成功
     */
    public boolean DeleteSpo2TestData(String IDCard) {
        boolean flag = false;
        db = helper.getWritableDatabase();
        db.beginTransaction();
        try {
            int count = db.delete("spo2Test", "IDCard=?", new String[]{IDCard});
            if ((count > 0)) {
                flag = true;
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }

        return flag;
    }

    /**
     * 根据 IDCard 修改血氧检测数据
     *
     * @param IDCard 身份证号
     * @param spo2Test 血氧检测列
     * @return true为修改成功
     */
    public boolean UpdateSpo2TestData(String IDCard, Spo2Test spo2Test) {
        boolean flag = false;
        db = helper.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put("IDCard", spo2Test.getIDCard());
            values.put("time", spo2Test.getTime());
            values.put("spo2", spo2Test.getSpo2());
            values.put("pulse", spo2Test.getPulse());
            values.put("doctorID", spo2Test.getDoctor());
            int count = db.update("spo2Test", values, "IDCard=?", new String[]{IDCard});
            if (count > 0) {
                flag = true;
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
        return flag;
    }

    /**
     * 根据 IDCard 查询血氧检测数据
     *
     * @param IDCard 身份证号
     * @return Cursor
     */
    public Cursor Spo2TestDataByID(String IDCard) {
        db = helper.getReadableDatabase();
        String sqlId = "select * from spo2Test where IDCard = ?";
        Cursor cursor = db.rawQuery(sqlId, new String[]{IDCard});
        if (cursor.getCount() == 0) {
            cursor = null;
        }
        db.close();
        return cursor;
    }



///////////////////////////////////////////////////////////////////////////////////
    /**
     * 按IDCard 获取所有血压检测数据 未实现
     *
     * @param IDCard 用户ID
     * @return Cursor
     */
    public Cursor GetAllBpDataByUserId(String IDCard) {
        db = helper.getReadableDatabase();
        String sqlId = "select t.temp,t.time,d.name from user as u inner join tempTest as t on u.IDCard = t.IDCard inner join doctor as d on d.ID = t.doctorID where u.IDCard = ? order by t.time DESC";
        Cursor cursor = db.rawQuery(sqlId, new String[]{IDCard});
        if (cursor.getCount() == 0) {
            cursor = null;
        }
        db.close();
        return cursor;
    }

    /**
     * 按IDCard 获取所有血糖检测数据
     *
     * @param IDCard 用户ID
     * @return Cursor
     */
    public Cursor GetAllBsDataByUserId(String IDCard) {
        db = helper.getReadableDatabase();
        String sqlId = "select b.bs,b.time,d.name from user as u inner join bsTest as b on u.IDCard = b.IDCard inner join doctor as d on d.ID = b.doctorID where u.IDCard = ? order by b.time DESC";
        Cursor cursor = db.rawQuery(sqlId, new String[]{IDCard});
        if (cursor.getCount() == 0) {
            cursor = null;
        }
        db.close();
        return cursor;
    }

    /**
     * 按IDCard 获取所有血氧检测数据
     *
     * @param IDCard 用户ID
     * @return Cursor
     */
    public Cursor GetAllSpo2DataByUserId(String IDCard) {
        db = helper.getReadableDatabase();
        String sqlId = "select s.spo2,s.pulse,s.time,d.name from user as u inner join spo2Test as s on u.IDCard = s.IDCard inner join doctor as d on d.ID = s.doctorID where u.IDCard = ? order by s.time DESC";
        Cursor cursor = db.rawQuery(sqlId, new String[]{IDCard});
        if (cursor.getCount() == 0) {
            cursor = null;
        }
        db.close();
        return cursor;
    }

    /**
     * 按IDCard 获取所有体温检测数据
     *
     * @param IDCard 用户ID
     * @return Cursor
     */
    public Cursor GetAllTempDataByUserId(String IDCard) {
        db = helper.getReadableDatabase();
        String sqlId = "select t.temp,t.time,d.name from user as u inner join tempTest as t on u.IDCard = t.IDCard inner join doctor as d on d.ID = t.doctorID where u.IDCard = ? order by t.time DESC";
        Cursor cursor = db.rawQuery(sqlId, new String[]{IDCard});
        if (cursor.getCount() == 0) {
            cursor = null;
        }
        db.close();
        return cursor;
    }

    /**
     * 按IDCard 获取所有心电检测数据
     *
     * @param IDCard 用户ID
     * @return Cursor
     */
    public Cursor GetAllEcgDataByUserId(String IDCard) {
        db = helper.getReadableDatabase();
        String sqlId = "select e.value,e.time,d.name from user as u inner join ecgTest as e on u.IDCard = e.IDCard inner join doctor as d on d.ID = e.doctorID where u.IDCard = ? order by e.time DESC";
        Cursor cursor = db.rawQuery(sqlId, new String[]{IDCard});
        if (cursor.getCount() == 0) {
            cursor = null;
        }
        db.close();
        return cursor;
    }

    /**
     * 按IDCard 获取所有尿常规检测数据
     *
     * @param IDCard 用户ID
     * @return Cursor
     */
    public Cursor GetAllUmDataByUserId(String IDCard) {
        db = helper.getReadableDatabase();
        String sqlId = "select um.LEU,um.time,d.name from user as u inner join umTest as um on u.IDCard = um.IDCard inner join doctor as d on d.ID = um.doctorID where u.IDCard = ? order by um.time DESC";
        Cursor cursor = db.rawQuery(sqlId, new String[]{IDCard});
        if (cursor.getCount() == 0) {
            cursor = null;
        }
        db.close();
        return cursor;
    }

    /**
     * 按IDCard 和TIme 获取血压检测数据
     *
     * @param IDCard 用户ID
     * @param Time   检测时间
     * @return Cursor
     */
    public Cursor GetBpDataByUserIdAndTime(String IDCard, String Time) {
        db = helper.getReadableDatabase();
        String sqlId = "select * from bpTest where IDCard = ? and time =?";
        Cursor cursor = db.rawQuery(sqlId, new String[]{IDCard, Time});
        if (cursor.getCount() == 0) {
            cursor = null;
        }
        db.close();
        return cursor;
    }

    /**
     * 按IDCard 和TIme 获取血糖检测数据
     *
     * @param IDCard 用户ID
     * @param Time   检测时间
     * @return Cursor
     */
    public Cursor GetBsDataByUserIdAndTime(String IDCard, String Time) {
        db = helper.getReadableDatabase();
        String sqlId = "select * from bsTest where IDCard = ? and time =?";
        Cursor cursor = db.rawQuery(sqlId, new String[]{IDCard, Time});
        if (cursor.getCount() == 0) {
            cursor = null;
        }
        db.close();
        return cursor;
    }

    /**
     * 按IDCard 和TIme 获取血氧检测数据
     *
     * @param IDCard 用户ID
     * @param Time   检测时间
     * @return Cursor
     */
    public Cursor GetSpo2DataByUserIdAndTime(String IDCard, String Time) {
        db = helper.getReadableDatabase();
        String sqlId = "select * from spo2Test where IDCard = ? and time =?";
        Cursor cursor = db.rawQuery(sqlId, new String[]{IDCard, Time});
        if (cursor.getCount() == 0) {
            cursor = null;
        }
        db.close();
        return cursor;
    }

    /**
     * 按IDCard 和TIme 获取体温检测数据
     *
     * @param IDCard 用户ID
     * @param Time   检测时间
     * @return Cursor
     */
    public Cursor GetTempDataByUserIdAndTime(String IDCard, String Time) {
        db = helper.getReadableDatabase();
        String sqlId = "select * from tempTest where IDCard = ? and time =?";
        Cursor cursor = db.rawQuery(sqlId, new String[]{IDCard, Time});
        if (cursor.getCount() == 0) {
            cursor = null;
        }
        db.close();
        return cursor;
    }

    /**
     * 按IDCard 和TIme 获取心电检测数据
     *
     * @param IDCard 用户ID
     * @param Time   检测时间
     * @return Cursor
     */
    public Cursor GetEcgDataByUserIdAndTime(String IDCard, String Time) {
        db = helper.getReadableDatabase();
        String sqlId = "select * from ecgTest where IDCard = ? and time =?";
        Cursor cursor = db.rawQuery(sqlId, new String[]{IDCard, Time});
        if (cursor.getCount() == 0) {
            cursor = null;
        }
        db.close();
        return cursor;
    }

    /**
     * 按IDCard 和TIme 获取尿常规检测数据
     *
     * @param IDCard 用户ID
     * @param Time   检测时间
     * @return Cursor
     */
    public Cursor GetUmDataByUserIdAndTime(String IDCard, String Time) {
        db = helper.getReadableDatabase();
        String sqlId = "select * from umTest where IDCard = ? and time =?";
        Cursor cursor = db.rawQuery(sqlId, new String[]{IDCard, Time});
        if (cursor.getCount() == 0) {
            cursor = null;
        }
        db.close();
        return cursor;
    }


}
