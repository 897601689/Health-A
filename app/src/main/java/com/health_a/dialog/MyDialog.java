package com.health_a.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.health_a.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2016/2/17.
 */
public class MyDialog extends Dialog {
        private String[] list;
        private String title;
        private TextView button;

        //类似于自定义View，必须实现一个非默认的构造方法
        public MyDialog(Context context, String[] list, String title, View view) {
            super(context);
            this.list = list;
            this.title = title;
            this.button = (TextView) view;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //设置不显示对话框标题栏
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            //设置对话框显示哪个布局文件
            setContentView(R.layout.add_dialog);
            //对话框也可以通过资源id找到布局文件中的组件，从而设置点击侦听
            TextView listTitle = (TextView) findViewById(R.id.dialog_txt);
            TextView cancel = (TextView) findViewById(R.id.add_cancel_txt);
            ListView lis = (ListView) findViewById(R.id.dialog_list);

            listTitle.setText(title);//设置标题
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            //设置列表适配器
            lis.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.select_dialog_item, getData()));
            lis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    button.setText(list[position]);
                    dismiss();
                }
            });
        }

        public List<String> getData() {
            List<String> data = new ArrayList<>();
            for (int i = 0; i < list.length; i++) {
                data.add(list[i]);
            }
            return data;
        }
    }
