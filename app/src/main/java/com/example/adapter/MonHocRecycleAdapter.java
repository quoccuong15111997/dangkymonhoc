package com.example.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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

public class MonHocRecycleAdapter extends RecyclerView.Adapter<MonHocRecycleAdapter.ViewHolder> {
    private Context context;
    private ArrayList<MonHoc> data;
    private ArrayList<MonHoc> dsMaDaChonTruoc;

    public static final  int TYPE_NEW=1;
    public static final  int TYPE_OLD=2;
    CheckBoxIsCheck checkBoxIsCheck;
    public void isChecked(CheckBoxIsCheck checkBoxIsCheck){
        this.checkBoxIsCheck=checkBoxIsCheck;
    }

    public MonHocRecycleAdapter(Context context, ArrayList<MonHoc> data, ArrayList<MonHoc> dsMaDaChonTruoc){
        this.dsMaDaChonTruoc=dsMaDaChonTruoc;
        this.context=context;
        this.data=data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        View view=null;
        if(i==TYPE_OLD)
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
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        MonHoc monHoc= data.get(position);
        if (monHoc!=null) {
            for(MonHoc m : dsMaDaChonTruoc){
               if(m.getMaMH().equals(monHoc.getMaMH())==true){
                   return TYPE_OLD;
               }
               else
                   return TYPE_NEW;
            }
        }
            return -1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTenMH;
        TextView txtMaMH;
        TextView txtSoTC;
        CheckBox chkMonHoc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
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
