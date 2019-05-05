package com.example.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.dangkymonhoc.R;
import com.example.impl.CheckBoxIsCheck;
import com.example.model.MonHoc;

import java.util.ArrayList;

public class MonHocAdapter extends RecyclerView.Adapter<MonHocAdapter.ViewHolder>{
    private Context context;
    private ArrayList<MonHoc> data;

    CheckBoxIsCheck checkBoxIsCheck;
    public void isChecked(CheckBoxIsCheck checkBoxIsCheck){
        this.checkBoxIsCheck=checkBoxIsCheck;
    }

    public MonHocAdapter(Context context, ArrayList<MonHoc> data){
        this.context=context;
        this.data=data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        View view=null;
        if(i==1)
        {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_monhoc_cu,viewGroup,false);
        }
        else
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_monhoc_moi,viewGroup,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        final MonHoc monHoc= data.get(i);
        viewHolder.txtTenMH.setText(monHoc.getTenMH());
        viewHolder.txtMaMH.setText(monHoc.getMaMH());
        viewHolder.txtSoTC.setText(monHoc.getSoTC()+"");
        if(monHoc.isTinhTrang()){
            viewHolder.chkMonHoc.setChecked(true);
        }
        else
            viewHolder.chkMonHoc.setChecked(false);
        viewHolder.chkMonHoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(monHoc.isTinhTrang()){
                    monHoc.setTinhTrang(false);
                }
                else
                    monHoc.setTinhTrang(true);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 2;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtTenMH;
        TextView txtMaMH;
        TextView txtSoTC;
        CheckBox chkMonHoc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.setIsRecyclable(false);
            txtMaMH=itemView.findViewById(R.id.txtMaMH);
            txtTenMH=itemView.findViewById(R.id.txtTenMonHoc);
            txtSoTC=(TextView) itemView.findViewById(R.id.txtSoTC);
            chkMonHoc=itemView.findViewById(R.id.chkMonHoc);
            chkMonHoc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked==true){
                        checkBoxIsCheck.isChecked(getAdapterPosition(),true);
                    }
                    else if(isChecked==false){
                        checkBoxIsCheck.isChecked(getAdapterPosition(),false);
                    }
                }
            });
        }

    }
}
