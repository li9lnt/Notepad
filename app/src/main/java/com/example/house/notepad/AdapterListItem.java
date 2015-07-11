package com.example.house.notepad;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by house on 2015/07/01.
 */
public class AdapterListItem extends ArrayAdapter<ThumbInfo> {
    public AdapterListItem(Context context, int resource, ArrayList<ThumbInfo> objects) {
        super(context, resource, objects);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        ThumbInfo thumb_info = getItem(position);
        holder.chk_statement.setText(thumb_info.statement);
        holder.txv_update_time.setText(thumb_info.update_time);
        return convertView;
    }
    // レイアウト内のViewに対し、高速にアクセスできるようにインスタンスを保持しておく
    private static class ViewHolder {
        public CheckBox chk_statement;
        public TextView txv_update_time;
        ViewHolder(View view) {
            chk_statement = (CheckBox)view.findViewById(R.id.checkBox);
            txv_update_time = (TextView)view.findViewById(R.id.textView);
        }
    }
}
