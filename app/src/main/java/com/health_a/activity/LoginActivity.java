package com.health_a.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.health_a.R;
import com.health_a.bean.DoctorInfo;
import com.health_a.dao.DBOperation;
import com.health_a.util.Global;
import com.health_a.util.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginActivity extends Activity {


    @BindView(R.id.edit_name)
    EditText editName;
    @BindView(R.id.edit_pass)
    EditText editPass;
    @BindView(R.id.btn_login)
    TextView btnLogin;

    DBOperation db;//数据操作对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        db = new DBOperation(LoginActivity.this);
        AdminInfo();
        editName.setText("10000");
        editPass.setText("10000");
    }

    @OnClick(R.id.btn_login)
    protected void Login() {

        if (Utils.isViewEmpty(editName)) {
            Toast.makeText(getApplicationContext(), "用户名不能为空", Toast.LENGTH_SHORT).show();
        } else if (Utils.isViewEmpty(editPass)) {
            Toast.makeText(getApplicationContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
        } else {
            String id = editName.getText().toString().trim();
            String password = editPass.getText().toString().trim();
            if (db.DoctorExist(id, password)) {
                saveUserInfo(id, db.GetDoctorNameByID(id), db.GetDoctorLevelByID(id));//保存医生登录状态

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "用户名或密码不正确", Toast.LENGTH_SHORT).show();
            }
        }


    }


    /**
     * 保存医生登录状态
     *
     * @param id   登录的ID
     * @param name 登录的姓名
     */
    private void saveUserInfo(String id, String name, int level) {
//        SharedPreferences config = getSharedPreferences("config", MODE_PRIVATE);
//        SharedPreferences.Editor edit = config.edit();
//        edit.putString("DoctorID", id);
//        edit.putString("DoctorName", name);
//        edit.commit();//提交保存
//        db.AddDoctorInfo(new DoctorInfo("李医生","男", "123456", "5"));
//        db.AddDoctorInfo(new DoctorInfo("王医生","男", "123456", "4"));
//        db.AddDoctorInfo(new DoctorInfo("张医生","男", "123456", "3"));
//        db.AddDoctorInfo(new DoctorInfo("赵医生","女", "123456", "2"));
//        db.AddDoctorInfo(new DoctorInfo("钱医生","女", "123456", "1"));
//        db.AddDoctorInfo(new DoctorInfo("孙医生","女", "123456", "1"));
//        db.AddDoctorInfo(new DoctorInfo("周医生","男", "123456", "1"));
        //保存当前医生的信息
        Global.SetDoctorInfo(id, name, level);
    }

    /**
     * 程序第一次运行时 输入超级管理员
     */
    private void AdminInfo() {
        SharedPreferences config = getSharedPreferences("config", Activity.MODE_PRIVATE);
        // 使用getString方法获得value，注意第2个参数是value的默认值
        String ss = config.getString("index", "0");
        if (ss.equals("0")) {
            db.AddDoctorInfo(new DoctorInfo("10000", "超级管理员", "未知", "10000", "9"));
            //db.DeleteDoctorInfo("10000");
            SharedPreferences.Editor edit = config.edit();
            edit.putString("index", "1");
            edit.commit();//提交保存
        }
    }
}
