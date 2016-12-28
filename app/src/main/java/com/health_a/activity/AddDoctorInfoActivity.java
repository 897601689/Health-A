package com.health_a.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.health_a.R;
import com.health_a.bean.DoctorInfo;
import com.health_a.dao.DBOperation;
import com.health_a.dialog.MyDialog;
import com.health_a.dialog.MyAlertDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Admin on 2016/8/8.
 */
public class AddDoctorInfoActivity extends Activity {
    @BindView(R.id.add_doctor_back)
    ImageView addDoctorBack;
    @BindView(R.id.add_title_txt)
    TextView addTitleTxt;
    @BindView(R.id.add_title_layout)
    RelativeLayout addTitleLayout;
    @BindView(R.id.ge_ren_ji_ben_xin_xi)
    TextView geRenJiBenXinXi;
    @BindView(R.id.add_doctorId_edit)
    EditText addDoctorIdEdit;
    @BindView(R.id.add_password_edit)
    EditText addPasswordEdit;
    @BindView(R.id.add_doctorName_edit)
    EditText addDoctorNameEdit;
    @BindView(R.id.add_doctorSex_btn)
    TextView addDoctorSexBtn;
    @BindView(R.id.add_doctorLevel_btn)
    TextView addDoctorLevelBtn;
    @BindView(R.id.add_dCancel_btn)
    TextView addDCancelBtn;
    @BindView(R.id.add_dSave_btn)
    TextView addDSaveBtn;

    private DBOperation db;
    private boolean isUpadte = false;
    private String level;
    private String work;
    MyDialog myDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_doctor);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        db = new DBOperation(AddDoctorInfoActivity.this);
        Intent intent = getIntent();
        String id;
        if ("update".equals(intent.getStringExtra("type"))) {
            id = intent.getStringExtra("id");
            Log.e("id", id);
            Cursor cursor = db.GetDoctorInfoByID(id);
            if (cursor.moveToNext()) {
                addDoctorIdEdit.setText(cursor.getString(0));
                addDoctorNameEdit.setText(cursor.getString(1));
                addDoctorSexBtn.setText(cursor.getString(2));
                addPasswordEdit.setText(cursor.getString(3));
                addDoctorLevelBtn.setText(getDoctorWorkByLevel(cursor.getString(4)));
            }
            isUpadte = true;
            addTitleTxt.setText("修改医生信息");
        } else {
            addDoctorIdEdit.setText(getDoctorId());
        }
    }

    private String getDoctorLevelByWork(String work) {
        this.work = work;
        int level = 0;
        switch (work) {
            case "医生":
                level = 1;
                break;
            case "主任":
                level = 2;
                break;
            case "专家":
                level = 3;
                break;
        }
        return String.valueOf(level);
    }

    private String getDoctorWorkByLevel(String level) {
        this.level = level;
        String work = "";
        switch (level) {
            case "1":
                work = "医生";
                break;
            case "2":
                work = "主任";
                break;
            case "3":
                work = "专家";
                break;
            case "4":
                work = "管理员";
                break;
        }
        return work;
    }

    private String getDoctorId() {
        return String.valueOf(db.GetDoctorID() + 1);
    }

    @OnClick({R.id.add_doctor_back, R.id.add_doctorSex_btn, R.id.add_doctorLevel_btn, R.id.add_dCancel_btn, R.id.add_dSave_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_doctor_back:
                finish();
                break;
            case R.id.add_doctorSex_btn:
                myDialog = new MyDialog(AddDoctorInfoActivity.this, new String[]{"男", "女"}, "性别选择", addDoctorSexBtn);
                myDialog.show();
                break;
            case R.id.add_doctorLevel_btn:
                myDialog = new MyDialog(AddDoctorInfoActivity.this, new String[]{"医生", "主任", "专家"}, "职位选择", addDoctorLevelBtn);
                myDialog.show();
                break;
            case R.id.add_dCancel_btn:
                finish();
                break;
            case R.id.add_dSave_btn:
                DoctorInfo doctor = new DoctorInfo();
                String name = addDoctorNameEdit.getText().toString().trim();
                String password = addPasswordEdit.getText().toString().trim();
                String work = addDoctorLevelBtn.getText().toString();
                if(!name.isEmpty()){
                    if(!password.isEmpty()){

                        doctor.setName(name);
                        doctor.setPassword(password);
                        doctor.setSex(addDoctorSexBtn.getText().toString());
                        doctor.setLevel(getDoctorLevelByWork(work));
                        if(isUpadte){
                            db.UpdateDoctorInfo(addDoctorIdEdit.getText().toString(), doctor);
                        }else{
                            db.AddDoctorInfo(doctor);
                        }
                        this.finish();
                    }else{
                        final MyAlertDialog builder = new MyAlertDialog(AddDoctorInfoActivity.this);
                        builder.setMessage("密码不能为空！");
                        builder.setPositiveButton("确   定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                builder.dismiss();
                            }
                        });
                    }
                }else{
                    final MyAlertDialog builder = new MyAlertDialog(AddDoctorInfoActivity.this);
                    builder.setMessage("姓名不能为空！");
                    builder.setPositiveButton("确   定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            builder.dismiss();
                        }
                    });
                }
                break;
        }
    }
}
