package com.health_a.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.health_a.R;
import com.health_a.bean.TestList;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Admin on 2016/8/2. 检测信息列表适配器 TestInfoActivity使用
 */
public class TestListAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater inflater;
    List<TestList.DataEntity> list;

    public TestListAdapter(Context mContext, LayoutInflater inflater, List<TestList.DataEntity> list) {
        this.mContext = mContext;
        this.inflater = inflater;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.item_test, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if (i % 2 != 0) {
            holder.itemTitleLayout.setBackgroundResource(R.drawable.info_item_white);
        } else {
            holder.itemTitleLayout.setBackgroundResource(R.drawable.info_item_gray);
        }
        holder.itemName.setText(list.get(i).getTestName());
        holder.itemResult.setText(list.get(i).getTestResult());
        holder.itemTime.setText(list.get(i).getTestTime());
        holder.itemDoctor.setText(list.get(i).getTestDoctor());
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.item_name)
        TextView itemName;
        @BindView(R.id.item_result)
        TextView itemResult;
        @BindView(R.id.item_time)
        TextView itemTime;
        @BindView(R.id.item_doctor)
        TextView itemDoctor;
        @BindView(R.id.item_title_layout)
        LinearLayout itemTitleLayout;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
