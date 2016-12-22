package com.health_a.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.health_a.R;
import com.health_a.bean.UserInfo;
import com.health_a.dao.DBOperation;
import com.health_a.dialog.DatePickerDialog;
import com.health_a.dialog.MyDialog;
import com.health_a.util.AlertDialog;
import com.health_a.util.Utils;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Admin on 2016/8/8.
 */
public class AddUserInfoActivity extends Activity {
    @BindView(R.id.add_user_back)
    ImageView addUserBack;
    @BindView(R.id.add_title_txt)
    TextView addTitleTxt;
    @BindView(R.id.ge_ren_ji_ben_xin_xi)
    TextView geRenJiBenXinXi;
    @BindView(R.id.add_userName_edit)
    EditText addUserNameEdit;
    @BindView(R.id.add_userId_edit)
    EditText addUserIdEdit;
    @BindView(R.id.add_userSex_btn)
    TextView addUserSexBtn;
    @BindView(R.id.add_userAge_btn)
    EditText addUserAgeBtn;
    @BindView(R.id.add_diploma_btn)
    TextView addDiplomaBtn;
    @BindView(R.id.add_userAge_edit)
    EditText addUserAgeEdit;
    @BindView(R.id.add_gong_zuo_dan_wei)
    EditText addGongZuoDanWei;
    @BindView(R.id.add_phone_edit)
    EditText addPhoneEdit;
    @BindView(R.id.add_lian_xi_ren_xing_ming)
    EditText addLianXiRenXingMing;
    @BindView(R.id.add_lian_xi_ren_dian_hua)
    EditText addLianXiRenDianHua;
    @BindView(R.id.add_domicile_btn)
    TextView addDomicileBtn;
    @BindView(R.id.add_min_zu)
    TextView addMinZu;
    @BindView(R.id.add_shao_shu_min_zu)
    EditText addShaoShuMinZu;
    @BindView(R.id.add_xue_xing)
    TextView addXueXing;
    @BindView(R.id.add_RH_yin_xing)
    TextView addRHYinXing;
    @BindView(R.id.add_uCancel_btn)
    TextView addUCancelBtn;
    @BindView(R.id.add_uSave_btn)
    TextView addUSaveBtn;

    private DBOperation db;
    private boolean isUpadte = false;//是否 修改信息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        ButterKnife.bind(this);
        init();
        addUserIdEdit.addTextChangedListener(textWatcher);//添加监听器
    }

    private void init() {
        db = new DBOperation(AddUserInfoActivity.this);
        Intent intent = getIntent();
        String id;
        if ("update".equals(intent.getStringExtra("type"))) {
            id = intent.getStringExtra("id");
            Log.e("id", id);
            Cursor cursor = db.GetUserByCardId(id);
            if (cursor.moveToNext()) {
                addUserNameEdit.setText(cursor.getString(1));
                addUserIdEdit.setText(cursor.getString(0));
                addUserSexBtn.setText(cursor.getString(2));
                addUserAgeEdit.setText(cursor.getString(3));
                addPhoneEdit.setText(cursor.getString(4));
                addUserAgeBtn.setText(Utils.getCsrq(id));
            }
            addUserIdEdit.setEnabled(false);
            isUpadte = true;
            addTitleTxt.setText("修改用户信息");
        }else if("info".equals(intent.getStringExtra("type"))){
            id = intent.getStringExtra("id");
            Log.e("id", id);
            Cursor cursor = db.GetUserByCardId(id);
            if (cursor.moveToNext()) {
                addUserNameEdit.setText(cursor.getString(1));
                addUserIdEdit.setText(cursor.getString(0));
                addUserSexBtn.setText(cursor.getString(2));
                addUserAgeEdit.setText(cursor.getString(3));
                addPhoneEdit.setText(cursor.getString(4));
                addUserAgeBtn.setText(Utils.getCsrq(id));
            }
            addUserIdEdit.setEnabled(false);
            addUserNameEdit.setEnabled(false);
            addUserSexBtn.setEnabled(false);
            addGongZuoDanWei.setEnabled(false);
            addPhoneEdit.setEnabled(false);
            addLianXiRenXingMing.setEnabled(false);
            addLianXiRenDianHua.setEnabled(false);
            addDiplomaBtn.setEnabled(false);
            addDomicileBtn.setEnabled(false);
            addMinZu.setEnabled(false);
            addXueXing.setEnabled(false);
            addRHYinXing.setEnabled(false);
            addUCancelBtn.setVisibility(View.GONE);
            addUSaveBtn.setVisibility(View.GONE);
            addTitleTxt.setText("查看用户信息");
        }
    }

    //创建编辑框监听器
    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void afterTextChanged(Editable s) {
            //Log.d("TAG", "afterTextChanged--------------->");
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //Log.d("TAG", "beforeTextChanged--------------->");
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //Log.d("TAG", "onTextChanged--------------->");
            try {
                if (Utils.IDCardValidate(addUserIdEdit.getText().toString().trim()).equals("true")) {
                    addUserAgeBtn.setText(Utils.getCsrq(addUserIdEdit.getText().toString().trim()));
                    addUserAgeEdit.setText(String.valueOf(Utils.getAge(addUserIdEdit.getText().toString().trim())));
                } else {
                    addUserAgeBtn.setText("");
                    addUserAgeEdit.setText("");
                }
            } catch (Exception e) {

            }

        }
    };

    public class EditChangedListener implements TextWatcher {
        private CharSequence temp;//监听前的文本
        private int editStart;//光标开始位置
        private int editEnd;//光标结束位置
        private final int charMaxNum = 18;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            temp = s;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            /** 得到光标开始和结束位置 ,超过最大数后记录刚超出的数字索引进行控制 */
            editStart = addUserIdEdit.getSelectionStart();
            editEnd = addUserIdEdit.getSelectionEnd();
            if (temp.length() > charMaxNum) {
                Toast.makeText(getApplicationContext(), "你输入的字数已经超过了限制！", Toast.LENGTH_LONG).show();
                s.delete(editStart - 1, editEnd);
                int tempSelection = editStart;
                addUserIdEdit.setText(s);
                addUserIdEdit.setSelection(tempSelection);
            }
        }
    }

    @OnClick(R.id.add_user_back)
    protected void setAddBack() {
        finish();
    }

    @OnClick(R.id.add_uCancel_btn)
    protected void setAddCancelBtn() {
        finish();
    }

    @OnClick(R.id.add_uSave_btn)
    protected void setAddSaveBtn() {
        UserInfo user = new UserInfo();
        String name = addUserNameEdit.getText().toString().trim();
        String IDCard = addUserIdEdit.getText().toString().trim();
        if (!name.isEmpty()) {
            user.setName(name);//保存用户姓名
            if (!IDCard.isEmpty()) {
                if (!Utils.IDCardValidate(IDCard).equals("true")) {
                    final AlertDialog builder = new AlertDialog(AddUserInfoActivity.this);
                    builder.setMessage("身份证号格式不正确！");
                    builder.setPositiveButton("确   定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            builder.dismiss();
                        }
                    });
                } else {
                    user.setIDCard(IDCard);//保存用户身份证号
                    user.setAge(addUserAgeEdit.getText().toString());
                    user.setSex(addUserSexBtn.getText().toString());
                    user.setPhone(addPhoneEdit.getText().toString());
                    if (isUpadte) {
                        db.UpdateUserInfo(IDCard, user);
                        this.finish();
                    } else {
                        if (db.GetUserByCardId(IDCard) == null) {
                            db.AddUserInfo(user);
                            this.finish();

                        } else {
                            Utils.AlertDialog(AddUserInfoActivity.this, "身份证号已存在！");
                        }
                    }

                }
            } else {
                final AlertDialog builder = new AlertDialog(AddUserInfoActivity.this);
                builder.setMessage("身份证号不能为空！");
                builder.setPositiveButton("确   定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder.dismiss();
                    }
                });
            }
        } else {
            final AlertDialog builder = new AlertDialog(AddUserInfoActivity.this);
            builder.setMessage("姓名不能为空！");
            builder.setPositiveButton("确   定", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    builder.dismiss();
                }
            });
        }
    }

    @OnClick(R.id.add_userSex_btn)
    protected void setAddUserSexBtn() {
        MyDialog dialog = new MyDialog(this, new String[]{"男", "女"}, "性别选择", addUserSexBtn);
        dialog.show();
    }

    @OnClick(R.id.add_userAge_btn)
    protected void setAddUserAgeBtn() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(AddUserInfoActivity.this, 0, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth,
                                  TimePicker timePicker, int hour, int minute) {
                String Time = String.format("%d-%02d-%02d", year, monthOfYear + 1, dayOfMonth);
                addUserAgeBtn.setText(Time);
                //addUserAgeEdit.setText(String.valueOf(Utils.getAge(String.format("%d%02d%02d", year, monthOfYear + 1, dayOfMonth))));
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
    }

    @OnClick(R.id.add_diploma_btn)
    protected void setAddDiplomaBtn() {
        MyDialog dialog = new MyDialog(this, new String[]{"文盲及半文盲", "小学", "初中", "高中/技校/中专",
                "大学专科及以上", "不详"}, "文化程度", addDiplomaBtn);
        dialog.show();
    }

    @OnClick(R.id.add_domicile_btn)
    protected void setAddDomicileBtn() {
        MyDialog dialog = new MyDialog(this, new String[]{"户籍", "非户籍"}, "常住类型", addDomicileBtn);
        dialog.show();
    }

    @OnClick(R.id.add_min_zu)
    protected void setAddMinZu() {
        MyDialog dialog = new MyDialog(this, new String[]{"汉族", "少数民族"}, "民族", addMinZu);
        dialog.show();
    }

    @OnClick(R.id.add_xue_xing)
    protected void setAddXueXing() {
        MyDialog dialog = new MyDialog(this, new String[]{"A型", "B型", "O型", "AB型", "不详"}, "血型", addXueXing);
        dialog.show();
    }

    @OnClick(R.id.add_RH_yin_xing)
    protected void setAddRHYinXing() {
        MyDialog dialog = new MyDialog(this, new String[]{"是", "否", "rh不详"}, "Rh阴性", addRHYinXing);
        dialog.show();
    }
}
