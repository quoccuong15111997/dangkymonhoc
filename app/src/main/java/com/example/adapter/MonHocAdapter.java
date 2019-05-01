package com.example.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.dangkymonhoc.R;
import com.example.impl.CheckBoxIsCheck;
import com.example.model.MonHoc;

import java.util.List;

public class MonHocAdapter extends ArrayAdapter<MonHoc> {
    Activity context=null;
    List<MonHoc> objects;
    int resource;
    CheckBoxIsCheck checkBoxIsCheck;
    public void isChecked(CheckBoxIsCheck checkBoxIsCheck){
        this.checkBoxIsCheck=checkBoxIsCheck;
    }

    public MonHocAdapter(Context context, int resource, List<MonHoc> objects) {
        super(context, resource, objects);
        this.resource=resource;
        this.objects=objects;
        this.context= (Activity) context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        LayoutInflater layoutInflater = this.context.getLayoutInflater();
        if(convertView==null){
            convertView=layoutInflater.inflate(this.resource,null);
            viewHolder=new ViewHolder();
            viewHolder.txtTenMH=convertView.findViewById(R.id.txtTenMonHoc);
            viewHolder.txtSoTC=convertView.findViewById(R.id.txtSoTC);
            viewHolder.txtMaMH=convertView.findViewById(R.id.txtMaMH);
            viewHolder.chkMonHoc=convertView.findViewById(R.id.chkMonHoc);
            viewHolder.position=position;

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        MonHoc monHoc=objects.get(position);
        viewHolder.txtTenMH.setText(monHoc.getTenMH());
        viewHolder.txtSoTC.setText(monHoc.getSoTC()+"");
        viewHolder.txtMaMH.setText(monHoc.getMaMH());

        viewHolder.chkMonHoc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true){
                    checkBoxIsCheck.isChecked(position,true);
                }
                else if(isChecked==false){
                    checkBoxIsCheck.isChecked(position,false);
                }
            }
        });
        return convertView;
    }

    public static class ViewHolder{
        TextView txtTenMH;
        TextView txtSoTC;
        TextView txtMaMH;
        CheckBox chkMonHoc;
        int position;
    }
}
