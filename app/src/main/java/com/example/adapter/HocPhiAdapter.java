package com.example.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.dangkymonhoc.R;
import com.example.model.MonHoc;

import java.util.List;

public class HocPhiAdapter extends ArrayAdapter<MonHoc> {
    Activity context=null;
    List<MonHoc> objects;
    int resource;

    public HocPhiAdapter(Context context, int resource, List<MonHoc> objects) {
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
            viewHolder.txtSoTien=convertView.findViewById(R.id.txtSoTien);
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
        viewHolder.txtSoTien.setText(monHoc.getSoTC()*400000+" đồng");

        return convertView;
    }

    public static class ViewHolder{
        TextView txtTenMH;
        TextView txtSoTC;
        TextView txtSoTien;
        int position;
    }
}
