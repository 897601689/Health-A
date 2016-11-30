package com.health_a.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.health_a.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Admin on 2016/08/13. 单击测试信息弹出 适配器 TestInfoActivity 使用
 */
public class TestDialogAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater inflater;
    ArrayList<ArrayList<String>> lists;

    public TestDialogAdapter(Context mContext, LayoutInflater inflater, ArrayList<ArrayList<String>> lists) {
        super();
        this.mContext = mContext;
        this.inflater = inflater;
        this.lists = lists;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ArrayList<String> list = lists.get(position);
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.dialog_test, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.itemTxt1.setText(list.get(0));
        holder.itemTxt2.setText(list.get(1));
        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.item_txt1)
        TextView itemTxt1;
        @BindView(R.id.item_txt2)
        TextView itemTxt2;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
