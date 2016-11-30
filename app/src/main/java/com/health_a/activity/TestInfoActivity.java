package com.health_a.activity;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.health_a.R;
import com.health_a.adapter.TestDialogAdapter;
import com.health_a.dao.DBOperation;
import com.health_a.util.Global;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Admin on 2016/7/29. 检测报告
 */
public class TestInfoActivity extends Activity {


    @BindView(R.id.info_name_txt)
    TextView infoNameTxt;
    @BindView(R.id.test_info_list)
    ListView testInfoList;

    ArrayList<ArrayList<String>> lists;
    TestDialogAdapter adapter;
    private DBOperation db;
    private String UserID = null;//当前进行检测用户

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_info);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        db = new DBOperation(TestInfoActivity.this);
//        SharedPreferences config = this.getSharedPreferences("config", MODE_PRIVATE);
//        SharedPreferences.Editor edit = config.edit();
//        UserID = config.getString("UserID", "");
//        edit.apply();
        lists = new ArrayList<>();
        //实例化自定义内容适配类
        adapter = new TestDialogAdapter(TestInfoActivity.this, getLayoutInflater(), lists);
        //为listView设置适配
        testInfoList.setAdapter(adapter);
        fillList();
    }
    /**
     * 填充测试详细信息
     */
    private void fillList() {
        ArrayList<String> info = new ArrayList<>();
        info.add("测试值");
        info.add("参考值");
        lists.add(info);
        infoNameTxt.setText(Global.TestName);
        switch (Global.TestName) {

            case "血压检测":
                AddBpData();
                break;
            case "血糖检测":
                AddBsData();
                break;
            case "血氧检测":
                AddSpo2Data();
                break;
            case "体温检测":
                AddTempData();
                break;
            case "xindian/心率":
                AddEcgData();
                break;
            case "尿常规检测":
                AddUmData();
                break;
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 添加血压数据
     */
    private void AddBpData() {
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = db.GetBpDataByUserIdAndTime(Global.UserID, Global.TestTime);
        if (cursor != null) {
            cursor.moveToNext();
            list.add(cursor.getString(2) + " mmHg");
            list.add("参考范围：90-140mmHg\n参考范围：60-90mmHg");
            lists.add(list);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 添加血糖数据
     */
    private void AddBsData() {
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = db.GetBsDataByUserIdAndTime(Global.UserID, Global.TestTime);
        if (cursor != null) {
            cursor.moveToNext();
            list.add(cursor.getString(3) + " mmol/L");
            if (cursor.getInt(2) == 1) {
                list.add("3.8~6.1 mmol/L(空腹)");
            } else {
                list.add("3.8~7.8 mmol/L");
            }

            lists.add(list);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 添加血氧数据
     */
    private void AddSpo2Data() {
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = db.GetSpo2DataByUserIdAndTime(Global.UserID, Global.TestTime);
        if (cursor != null) {
            cursor.moveToNext();
            list.add("血氧 " + cursor.getString(2) + " %");
            list.add("94%~100%");
            lists.add(list);

            list = new ArrayList<>();
            list.add("脉率 " + cursor.getString(3) + " bmp");
            list.add("50bmp~80bmp");
            lists.add(list);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 添加体温数据
     */
    private void AddTempData() {
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = db.GetTempDataByUserIdAndTime(Global.UserID, Global.TestTime);
        if (cursor != null) {
            cursor.moveToNext();
            list.add(cursor.getString(2) + " ℃");
            list.add("36℃~37℃");
            lists.add(list);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 添加心电/心率数据
     */
    private void AddEcgData() {
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = db.GetEcgDataByUserIdAndTime(Global.UserID, Global.TestTime);
        if (cursor != null) {
            cursor.moveToNext();
            list.add(cursor.getString(0) + "bmp");
            list.add("90~60");
            lists.add(list);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 添加尿常规数据
     */
    private void AddUmData() {
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = db.GetUmDataByUserIdAndTime(Global.UserID, Global.TestTime);
        if (cursor != null) {
            cursor.moveToNext();

            list.add("白细胞（LEU）" + cursor.getString(2));
            list.add("阴性（—）");
            lists.add(list);

            list = new ArrayList<>();
            list.add("亚硝酸盐（NIT）" + cursor.getString(3));
            list.add("阴性（—）");
            lists.add(list);

            list = new ArrayList<>();
            list.add("尿胆原（UBG）" + cursor.getString(4));
            list.add("<16");
            lists.add(list);

            list = new ArrayList<>();
            list.add("蛋白质（PRO）" + cursor.getString(5));
            list.add("阴性或者仅有微量");
            lists.add(list);

            list = new ArrayList<>();
            list.add("酸碱度（PH）" + cursor.getString(6));
            list.add("4.6-8.0 平均为6.0");
            lists.add(list);

            list = new ArrayList<>();
            list.add("红细胞（BLD）" + cursor.getString(7));
            list.add("阴性（—）");
            lists.add(list);

            list = new ArrayList<>();
            list.add("比重（SG）" + cursor.getString(8));
            list.add("1.015-1.025");
            lists.add(list);

            list = new ArrayList<>();
            list.add("酮体（KET）" + cursor.getString(9));
            list.add("阴性（—）");
            lists.add(list);

            list = new ArrayList<>();
            list.add("胆红素（BIL）" + cursor.getString(10));
            list.add("阴性（—）");
            lists.add(list);

            list = new ArrayList<>();
            list.add("葡萄糖（GLU）" + cursor.getString(11));
            list.add("阴性（—）");
            lists.add(list);

            list = new ArrayList<>();
            list.add("维生素C（VC）" + cursor.getString(12));
            list.add("阴性（—）");
            lists.add(list);
            adapter.notifyDataSetChanged();
        }
    }

}
