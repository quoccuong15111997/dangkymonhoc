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
import com.example.model.ThongBao;

import java.util.List;

public class ThongBaoAdapter extends ArrayAdapter<ThongBao> {
    Activity context=null;
    List<ThongBao> objects;
    int resource;

    public ThongBaoAdapter(Context context, int resource, List<ThongBao> objects) {
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
            viewHolder.txtTitle=convertView.findViewById(R.id.txtTile);
            viewHolder.txtMessege=convertView.findViewById(R.id.txtMessege);
            viewHolder.txtDate=convertView.findViewById(R.id.txtDate);
          viewHolder.txtUser=convertView.findViewById(R.id.txtUser);

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        ThongBao thongBao=objects.get(position);
        viewHolder.txtTitle.setText(thongBao.getTitle());
        viewHolder.txtMessege.setText(thongBao.getMessege());
        viewHolder.txtDate.setText("Gửi vào: "+thongBao.getDate());
        viewHolder.txtUser.setText("Gửi từ: "+thongBao.getUser());

        return convertView;
    }

    public static class ViewHolder{
        TextView txtTitle;
        TextView txtMessege;
        TextView txtDate;
        TextView txtUser;
    }
}
