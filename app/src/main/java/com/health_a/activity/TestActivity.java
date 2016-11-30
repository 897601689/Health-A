package com.health_a.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.health_a.R;
import com.health_a.adapter.TestListAdapter;
import com.health_a.bean.BsTest;
import com.health_a.bean.TempTest;
import com.health_a.bean.TestList;
import com.health_a.bean.UserInfo;
import com.health_a.dao.DBOperation;
import com.health_a.dialog.DatePickerDialog;
import com.health_a.util.Global;
import com.health_a.util.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Admin on 2016/8/13.
 */
public class TestActivity extends Activity {

    @BindView(R.id.test_name_txt)
    TextView testNameTxt;
    @BindView(R.id.test_sex_txt)
    TextView testSexTxt;
    @BindView(R.id.test_age_txt)
    TextView testAgeTxt;
    @BindView(R.id.test_query_edit)
    EditText testQueryEdit;
    @BindView(R.id.test_query_btn)
    TextView testQueryBtn;
    @BindView(R.id.test_user_btn)
    TextView testUserBtn;
    @BindView(R.id.test_all_btn)
    TextView testAllBtn;
    @BindView(R.id.test_start_txt)
    TextView testStartTxt;
    @BindView(R.id.test_end_txt)
    TextView testEndTxt;
    @BindView(R.id.test_list)
    ListView testList;
    @BindView(R.id.test_query_spn)
    Spinner testQuerySpn;
    @BindView(R.id.test_back)
    ImageView testBack;


    private TestListAdapter testListAdapter;
    private List<TestList.DataEntity> list;
    private DBOperation db;
    private String UserID = null;//当前进行检测用户
    private int SpinnerIndex; //标记要填充的类型索引
    private int SelectIndex;  //保存关联用户时选中的索引
    TextView time_txt;
    TextView name_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        init();
        testQuerySpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!UserID.equals("")) {
                    SpinnerIndex = position;
                    FillList(SpinnerIndex);
                    testListAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getApplicationContext(), "没有关联检测用户", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        testList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                time_txt = (TextView) view.findViewById(R.id.item_time);
                name_txt = (TextView) view.findViewById(R.id.item_name);
                Global.TestTime = time_txt.getText().toString();//保存本条信息的检测时间
                Global.TestName = name_txt.getText().toString();//保存本条信息的项目名称
                Intent intent = new Intent(TestActivity.this, TestInfoActivity.class);
                startActivity(intent);
            }
        });
    }

    @OnClick({R.id.test_back, R.id.test_query_btn, R.id.test_user_btn, R.id.test_start_txt, R.id.test_end_txt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.test_back:
                finish();
                break;
            case R.id.test_query_btn:
                break;
            case R.id.test_user_btn:
                Cursor cursor = db.GetAllUserInfo();
                if (cursor != null) {
                    String[] list = new String[cursor.getCount()];
                    int index = 0;
                    while (cursor.moveToNext()) {
                        list[index] = cursor.getString(1) + " " + cursor.getString(0);
                        index++;
                    }
                    ShowUserInfoList(list);
                    cursor.close();
                } else {
                    Toast.makeText(getApplicationContext(), "没有用户信息", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.test_start_txt:
                DatePickerDialog(testStartTxt);
                break;
            case R.id.test_end_txt:
                DatePickerDialog(testEndTxt);
                break;
        }
    }

    private void aa() {
        db.AddUserInfo(new UserInfo("1504231993", "张三", "20", "男", "100100"));
        db.AddUserInfo(new UserInfo("1504231994", "李四", "21", "男", "110110"));
        db.AddUserInfo(new UserInfo("1504231995", "王五", "22", "男", "111111"));
        db.AddTempTestData(new TempTest("1504231993", "2016-01-15 10:05:00", "36.2", "10001"));
        db.AddTempTestData(new TempTest("1504231994", "2016-01-11 15:07:00", "36.5", "10001"));
        db.AddTempTestData(new TempTest("1504231995", "2016-01-03 16:12:00", "36.4", "10003"));
        db.AddBsTestData(new BsTest("1504231993", "2016-01-21 11:06:00", "1", "6.5", "10002"));
        db.AddBsTestData(new BsTest("1504231994", "2016-01-20 12:35:00", "1", "4.5", "10003"));
        db.AddBsTestData(new BsTest("1504231995", "2016-01-22 13:54:00", "1", "5.5", "10002"));
    }

    private void init() {

        SpinnerIndex = 0;
        SelectIndex = 0;
        testStartTxt.setText(Utils.getNowTime());
        testEndTxt.setText(Utils.getNowTime());
        db = new DBOperation(TestActivity.this);
        list = new ArrayList<>();
        //实例化自定义内容适配类
        testListAdapter = new TestListAdapter(TestActivity.this, getLayoutInflater(), list);
        //为listView设置适配
        testList.setAdapter(testListAdapter);

        //aa();
        UserID = Global.UserID;//由缓存区获得当前关联用户IDCard

        if (!UserID.equals("")) {
            FillUserInfo(UserID);//填充被测量用户信息
            //testUserBtn.setEnabled(false);//禁用用户关联按钮
            FillList(0);
        } else {
            Toast.makeText(getApplicationContext(), "没有关联检测用户", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 调整时间
     *
     * @param textView 显示时间的控件
     */
    private void DatePickerDialog(final TextView textView) {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(TestActivity.this, 0, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth,
                                  TimePicker timePicker, int hour, int minute) {
                String Time = String.format("%d-%02d-%02d %02d:%02d:00", year, monthOfYear + 1, dayOfMonth, hour, minute);
                if (textView.getId() == R.id.test_start_txt)
                    testStartTxt.setText(Time);
                else {
                    testEndTxt.setText(Time);
                }
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
    }

    /**
     * 显示要关联的用户信息
     *
     * @param list 信息数组
     */
    private void ShowUserInfoList(final String[] list) {
        //显示下载列表
        Dialog alertDialog = new AlertDialog.Builder(TestActivity.this)
                .setTitle("请选择用户")
                .setIcon(R.drawable.ic_launcher)
                .setSingleChoiceItems(list, SelectIndex, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SelectIndex = which;

                    }
                })
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String[] str = list[SelectIndex].split(" ");
                        UserID = str[1];
                        FillUserInfo(UserID);//填充用户信息
                        FillList(SpinnerIndex);//填充用户检测
                        Global.UserID = UserID;//存入缓存区
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create();
        alertDialog.show();
    }

    /**
     * 填充用户信息
     *
     * @param userID 用户IDCard
     */
    private void FillUserInfo(String userID) {
        Cursor cursor = db.GetUserByCardId(userID);
        if (cursor != null) {
            if (cursor.moveToNext()) {
                testNameTxt.setText(cursor.getString(1));
                testSexTxt.setText(cursor.getString(2));
                testAgeTxt.setText(cursor.getString(3));
            }
            cursor.close();
        }
    }

    /**
     * 填充用户检测数据
     *
     * @param Type 需要填充数据的类型
     */
    private void FillList(int Type) {
        if (list.size() > 0) {
            list.clear();
        }

        switch (Type) {
            case 0:
                AddEcgData();
                AddSpo2Data();
                AddTempData();
                AddBpData();
                break;
            case 1:
                //AddEcgData();
                break;
            case 2:
                AddSpo2Data();
                break;
            case 3:
                AddTempData();
                break;
            case 4:
                //AddBpData();
                break;
            default:
                break;
        }
    }

    /**
     * 添加血压数据 未完成
     */
    private void AddBpData() {
        Cursor cursor = db.GetAllBpDataByUserId(UserID);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                TestList.DataEntity dataEntity = new TestList.DataEntity();
                dataEntity.setTestName("血压检测");
                dataEntity.setTestResult(cursor.getString(0) + "℃");
                dataEntity.setTestTime(cursor.getString(1));
                dataEntity.setTestDoctor(cursor.getString(2));
                list.add(dataEntity);
            }
            testListAdapter.notifyDataSetChanged();
            cursor.close();
        }
    }

    /**
     * 添加血糖数据
     */
    private void AddBsData() {
        Cursor cursor = db.GetAllBsDataByUserId(UserID);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                TestList.DataEntity dataEntity = new TestList.DataEntity();
                dataEntity.setTestName("血糖检测");
                dataEntity.setTestResult(cursor.getString(0) + "mmol/L");
                dataEntity.setTestTime(cursor.getString(1));
                dataEntity.setTestDoctor(cursor.getString(2));
                list.add(dataEntity);
            }
            testListAdapter.notifyDataSetChanged();
            cursor.close();
        }
    }

    /**
     * 添加血氧数据
     */
    private void AddSpo2Data() {
        Cursor cursor = db.GetAllSpo2DataByUserId(UserID);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                TestList.DataEntity dataEntity = new TestList.DataEntity();
                dataEntity.setTestName("血氧检测");
                dataEntity.setTestResult(cursor.getString(0) + "%");//+cursor.getString(1)+"bpm"
                dataEntity.setTestTime(cursor.getString(2));
                dataEntity.setTestDoctor(cursor.getString(3));
                list.add(dataEntity);
            }
            testListAdapter.notifyDataSetChanged();
            cursor.close();
        }
    }

    /**
     * 添加体温数据
     */
    private void AddTempData() {
        Cursor cursor = db.GetAllTempDataByUserId(UserID);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                TestList.DataEntity dataEntity = new TestList.DataEntity();
                dataEntity.setTestName("体温检测");
                dataEntity.setTestResult(cursor.getString(0) + "℃");
                dataEntity.setTestTime(cursor.getString(1));
                dataEntity.setTestDoctor(cursor.getString(2));
                list.add(dataEntity);
            }
            testListAdapter.notifyDataSetChanged();
            cursor.close();
        }
    }

    /**
     * 添加心电数据 未完成
     */
    private void AddEcgData() {
        Cursor cursor = db.GetAllEcgDataByUserId(UserID);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                TestList.DataEntity dataEntity = new TestList.DataEntity();
                dataEntity.setTestName("心电检测");
                dataEntity.setTestResult(cursor.getString(0));
                dataEntity.setTestTime(cursor.getString(1));
                dataEntity.setTestDoctor(cursor.getString(2));
                list.add(dataEntity);
            }
            testListAdapter.notifyDataSetChanged();
            cursor.close();
        }
    }

    /**
     * 添加尿常规数据
     */
    private void AddUmData() {
        Cursor cursor = db.GetAllUmDataByUserId(UserID);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                TestList.DataEntity dataEntity = new TestList.DataEntity();
                dataEntity.setTestName("尿常规检测");
                dataEntity.setTestResult(cursor.getString(0));
                dataEntity.setTestTime(cursor.getString(1));
                dataEntity.setTestDoctor(cursor.getString(2));
                list.add(dataEntity);
            }
            testListAdapter.notifyDataSetChanged();
            cursor.close();
        }
    }


}
