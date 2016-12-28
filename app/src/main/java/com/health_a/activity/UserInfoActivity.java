package com.health_a.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.health_a.R;
import com.health_a.adapter.UserListAdapter;
import com.health_a.dao.DBOperation;
import com.health_a.dialog.MyAlertDialog;
import com.health_a.util.Global;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Admin on 2016/7/29. 用户信息管理
 */
public class UserInfoActivity extends Activity {

    @BindView(R.id.user_back)
    ImageView userBack;
    @BindView(R.id.user_id_edit)
    EditText userIdEdit;
    @BindView(R.id.user_name_edit)
    EditText userNameEdit;
    @BindView(R.id.user_query_btn)
    TextView userQueryBtn;
    @BindView(R.id.user_add_btn)
    TextView userAddBtn;
    @BindView(R.id.user_update_btn)
    TextView userUpdateBtn;
    @BindView(R.id.user_del_btn)
    TextView userDelBtn;
    @BindView(R.id.user_info_list)
    ListView userInfoList;

    private ArrayList<ArrayList<String>> lists;
    private DBOperation db;
    private UserListAdapter adapter;
    public HashMap<String, Boolean> checkState = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);
        init();
        userInfoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView time_txt = (TextView) view.findViewById(R.id.item_id);
                Intent intent = new Intent(UserInfoActivity.this, AddUserInfoActivity.class);
                intent.putExtra("type", "info");
                intent.putExtra("id", time_txt.getText().toString());
                startActivity(intent);
                Log.e("22", "11");
            }
        });
    }

    // 初始化
    private void init() {
        db = new DBOperation(UserInfoActivity.this);
        lists = new ArrayList<>();
        //实例化自定义适配器类
        adapter = new UserListAdapter(UserInfoActivity.this, getLayoutInflater(), lists);
        //为listView设置适配器
        userInfoList.setAdapter(adapter);


//        db.AddUserInfo(new UserInfo("150423199303090016","张三","男","20","13812345678"));
//        db.AddUserInfo(new UserInfo("150423199303090017","李四","男","21","13812345688"));
//        db.AddUserInfo(new UserInfo("150423199303090018","王五","男","22","13812345698"));
//        db.AddUserInfo(new UserInfo("150423199303090019","赵六","男","23","13812345608"));

        FillUserInfo();
    }

    // 填充列表
    private void FillUserInfo() {

        if (lists.size() > 0) {
            lists.clear();
        }
        String id = userIdEdit.getText().toString().trim();
        String name = userNameEdit.getText().toString().trim();
        Cursor cursor;
        if (id.isEmpty()) {
            if (name.isEmpty()) {
                cursor = db.GetAllUserInfo();
            } else {
                cursor = db.GetUserByName(name);
            }
        } else {
            if (name.isEmpty()) {
                cursor = db.GetUserByCardId(id);
            } else {
                cursor = db.GetUserByNameAndIDCard(id, name);
            }
        }
        if (cursor != null) {
            while (cursor.moveToNext()) {
                ArrayList<String> info = new ArrayList<>();
                info.add(cursor.getString(0));
                info.add(cursor.getString(1));
                info.add(cursor.getString(2));
                info.add(cursor.getString(3));
                lists.add(info);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @OnClick(R.id.user_back)
    protected void setUserBack() {
        this.finish();
    }

    @OnClick(R.id.user_query_btn)
    protected void setUserQueryBtn() {
        FillUserInfo();
    }

    @OnClick(R.id.user_add_btn)
    protected void setUserAddBtn() {
        Intent intent = new Intent(UserInfoActivity.this, AddUserInfoActivity.class);
        intent.putExtra("type", "add");
        startActivity(intent);
        FillUserInfo();
    }

    @OnClick(R.id.user_update_btn)
    protected void setUserUpdateBtn() {
        ArrayList<String> listKey = new ArrayList<>();
        Iterator<String> it = checkState.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next().toString();
            listKey.add(key);
        }
        if (listKey.size() == 1) {
            Intent intent = new Intent(UserInfoActivity.this, AddUserInfoActivity.class);
            intent.putExtra("type", "update");
            intent.putExtra("id", listKey.get(0));
            startActivity(intent);
            Log.e("修改了", "" + listKey.get(0));//获得了 IDCard
            this.finish();
        } else {
            if (listKey.size() == 0) {
                final MyAlertDialog builder = new MyAlertDialog(UserInfoActivity.this);
                builder.setMessage("没选中任何项");
                builder.setPositiveButton("确   定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder.dismiss();
                    }
                });
                //Toast.makeText(getApplicationContext(), "没选中任何项", Toast.LENGTH_SHORT).show();
            } else {
                final MyAlertDialog builder = new MyAlertDialog(UserInfoActivity.this);
                builder.setMessage("只能选中一项进行修改");
                builder.setPositiveButton("确   定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder.dismiss();
                    }
                });
                //Toast.makeText(getApplicationContext(), "只能选中一项进行修改", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @OnClick(R.id.user_del_btn)
    protected void setUserDelBtn() {
        final ArrayList<String> listKey = new ArrayList<>();
        Iterator<String> it = checkState.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            listKey.add(key);
        }

        if (listKey.size() > 0) {
            final MyAlertDialog builder = new MyAlertDialog(UserInfoActivity.this);
            builder.setMessage("检测数据将同时被删除，\n确定要删除选中项吗？");
            builder.setPositiveButton("确   定", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < listKey.size(); i++) {
                        db.DeleteUserInfo(listKey.get(i));
                        if (Global.UserID.equals(listKey.get(i)))//如果删除的是当前关联用户 则删除关联状态
                            Global.UserID = "";
                        DelAll(listKey.get(i));//删除此用户的全部测量信息
                    }
                    FillUserInfo();//重新填充用户信息
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

            //Toast.makeText(getApplicationContext(), "确定要删除选中项吗？", Toast.LENGTH_SHORT).show();
        } else {

            final MyAlertDialog builder = new MyAlertDialog(UserInfoActivity.this);
            builder.setMessage("没选中任何项");
            builder.setPositiveButton("确   定", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    builder.dismiss();
                }
            });
            //Toast.makeText(getApplicationContext(), "没选中任何项", Toast.LENGTH_SHORT).show();
        }
    }


    //删除用户的全部测量信息
    private void DelAll(String IDCard) {
        db.DeleteTempTsetData(IDCard);
        db.DeleteBsTestData(IDCard);
        db.DeleteUmTestData(IDCard);
        db.DeleteSpo2TestData(IDCard);
    }
}
