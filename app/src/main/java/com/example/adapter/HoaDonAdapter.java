package com.example.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.dangkymonhoc.R;
import com.example.model.BienLai;
import com.example.model.MonHoc;

import java.util.List;

public class HoaDonAdapter extends ArrayAdapter<BienLai> {
    Activity context=null;
    List<BienLai> objects;
    int resource;

    public HoaDonAdapter(Context context, int resource, List<BienLai> objects) {
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
            viewHolder.txtSoBienLai=convertView.findViewById(R.id.txtSoBienLai);
            viewHolder.txtNgayDong=convertView.findViewById(R.id.txtNgayDong);
            viewHolder.txtSoTien=convertView.findViewById(R.id.txtSoTien);
            viewHolder.position=position;

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        BienLai bienLai= objects.get(position);
        viewHolder.txtSoBienLai.setText(bienLai.getSoBL()+"");
        viewHolder.txtNgayDong.setText(bienLai.getNgayHP());
        viewHolder.txtSoTien.setText(bienLai.getSoTien()+"");
        return convertView;
    }

    public static class ViewHolder{
        TextView txtSoBienLai;
        TextView txtNgayDong;
        TextView txtSoTien;
        int position;
    }
}
