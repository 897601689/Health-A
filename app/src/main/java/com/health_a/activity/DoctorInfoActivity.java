package com.health_a.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.health_a.R;
import com.health_a.adapter.DoctorListAdapter;
import com.health_a.dao.DBOperation;
import com.health_a.dialog.MyAlertDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Admin on 2016/7/29. 医生信息管理
 */
public class DoctorInfoActivity extends Activity {
    @BindView(R.id.doctor_back)
    ImageView doctorBack;
    @BindView(R.id.doctor_id_edit)
    EditText doctorIdEdit;
    @BindView(R.id.doctor_name_edit)
    EditText doctorNameEdit;
    @BindView(R.id.doctor_query_btn)
    TextView doctorQueryBtn;
    @BindView(R.id.doctor_add_btn)
    TextView doctorAddBtn;
    @BindView(R.id.doctor_update_btn)
    TextView doctorUpdateBtn;
    @BindView(R.id.doctor_del_btn)
    TextView doctorDelBtn;
    @BindView(R.id.doctor_info_list)
    ListView doctorInfoList;

    private ArrayList<ArrayList<String>> lists;
    private DBOperation db;
    private DoctorListAdapter adapter;
    public HashMap<String, Boolean> checkState = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        db = new DBOperation(DoctorInfoActivity.this);
        lists = new ArrayList<>();
        //实例化自定义适配器类
        adapter = new DoctorListAdapter(DoctorInfoActivity.this, getLayoutInflater(), lists);
        //为listView设置适配器
        doctorInfoList.setAdapter(adapter);
        FillDoctorInfo();// 填充医生信息
    }

    // 填充医生信息
    private void FillDoctorInfo() {
        if (lists.size() > 0) {
            lists.clear();
        }
        String id = doctorIdEdit.getText().toString().trim();
        String name = doctorNameEdit.getText().toString().trim();
        Cursor cursor;
        if (id.isEmpty()) {
            if (name.isEmpty()) {
                cursor = db.GetAllDoctorInfo();
            } else {
                cursor = db.GetDoctorInfoByName(name);
            }
        } else {
            if (name.isEmpty()) {
                cursor = db.GetDoctorInfoByID(id);
            } else {
                cursor = db.GetDoctorInfoByIDAndName(id, name);
            }
        }
        if (cursor != null) {
            while (cursor.moveToNext()) {
                ArrayList<String> info = new ArrayList<>();
                if ("10000".equals(cursor.getString(0))) {
                    continue;
                }
                info.add(cursor.getString(0));
                info.add(cursor.getString(1));
                info.add(cursor.getString(2));
                info.add(getDoctorWorkByLevel(cursor.getString(4)));
                lists.add(info);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private String getDoctorWorkByLevel(String level) {
        String work = "";
        switch (level) {
            case "1":
                work = "护士";
                break;
            case "2":
                work = "医生";
                break;
            case "3":
                work = "主任";
                break;
            case "4":
                work = "专家";
                break;
            case "9":
                work = "超管";
                break;
        }

        return work;
    }

    @OnClick(R.id.doctor_back)
    protected void setDoctorBack() {
        this.finish();
    }

    @OnClick(R.id.doctor_query_btn)
    protected void setDoctorQueryBtn() {
        FillDoctorInfo();
    }

    @OnClick(R.id.doctor_add_btn)
    protected void setDoctorAddBtn() {
        Intent intent = new Intent(DoctorInfoActivity.this, AddDoctorInfoActivity.class);
        intent.putExtra("type", "add");
        startActivity(intent);
    }

    @OnClick(R.id.doctor_update_btn)
    protected void setDoctorUpdateBtn() {
        ArrayList<String> listKey = new ArrayList<>();
        Iterator<String> it = checkState.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next().toString();
            listKey.add(key);
        }
        if (listKey.size() == 1) {
            Intent intent = new Intent(DoctorInfoActivity.this, AddDoctorInfoActivity.class);
            intent.putExtra("type", "update");
            intent.putExtra("id", listKey.get(0));
            startActivity(intent);
            Log.e("修改了", "" + listKey.get(0));//获得了 IDCard
            this.finish();
        } else {
            if (listKey.size() == 0) {
                final MyAlertDialog builder = new MyAlertDialog(DoctorInfoActivity.this);
                builder.setMessage("没选中任何项");
                builder.setPositiveButton("确   定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder.dismiss();
                    }
                });
            } else {
                final MyAlertDialog builder = new MyAlertDialog(DoctorInfoActivity.this);
                builder.setMessage("只能选中一项进行修改");
                builder.setPositiveButton("确   定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder.dismiss();
                    }
                });
            }
        }
    }

    @OnClick(R.id.doctor_del_btn)
    protected void setDoctorDelBtn() {
        final ArrayList<String> listKey = new ArrayList<>();
        Iterator<String> it = checkState.keySet().iterator();
        Toast.makeText(this,""+checkState.size(),Toast.LENGTH_LONG).show();
        Log.e("选择个数", "" + checkState.size());
        while (it.hasNext()) {
            String key = it.next();
            listKey.add(key);
        }

        if (listKey.size() > 0) {
            final MyAlertDialog builder = new MyAlertDialog(DoctorInfoActivity.this);
            builder.setMessage("确定要删除选中项吗？");
            builder.setPositiveButton("确   定", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < listKey.size(); i++) {
                        db.DeleteDoctorInfo(listKey.get(i));
                    }
                    FillDoctorInfo();//重新填充医生信息
                    checkState.clear();//清除选择数组
                    builder.dismiss();
                }
            });
            builder.setNegativeButton("取   消", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    builder.dismiss();
                }
            });
        } else {
            final MyAlertDialog builder = new MyAlertDialog(DoctorInfoActivity.this);
            builder.setMessage("没选中任何项");
            builder.setPositiveButton("确   定", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    builder.dismiss();
                }
            });
        }

    }
}
