package com.health_a.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.health_a.R;
import com.health_a.activity.UserInfoActivity;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Admin on 2016/7/29. 用户信息列表适配器 UserInfoActivity使用
 */
public class UserListAdapter extends BaseAdapter {

    UserInfoActivity mContext;
    private LayoutInflater inflater;
    private ArrayList<ArrayList<String>> lists;
    HashMap<String, Boolean> checkState = new HashMap<>();

    public UserListAdapter(UserInfoActivity mContext, LayoutInflater inflater, ArrayList<ArrayList<String>> lists) {
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
    public Object getItem(int i) {
        return lists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ArrayList<String> list = lists.get(i);
        final ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.item_info, null);
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
        holder.itemId.setText(list.get(0));
        holder.itemName.setText(list.get(1));
        holder.itemSex.setText(list.get(2));
        holder.itemAge.setText(list.get(3));
        holder.itemChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    checkState.put(list.get(0), true);
                    mContext.checkState.put(list.get(0), true);
                } else {
                    checkState.remove(list.get(0));
                    mContext.checkState.remove(list.get(0));
                }
            }
        });
        //处理listView中的复选框错乱，这段代码必须放在监听事件后面
        holder.itemChk.setChecked(checkState.get(list.get(0)) != null);
        return view;
    }


    static class ViewHolder {
        @BindView(R.id.item_chk)
        CheckBox itemChk;
        @BindView(R.id.item_id)
        TextView itemId;
        @BindView(R.id.item_name)
        TextView itemName;
        @BindView(R.id.item_sex)
        TextView itemSex;
        @BindView(R.id.item_age)
        TextView itemAge;
        @BindView(R.id.item_title_layout)
        LinearLayout itemTitleLayout;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
