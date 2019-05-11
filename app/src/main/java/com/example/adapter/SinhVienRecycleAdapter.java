package com.example.adapter;

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
import com.example.impl.ItemClick;
import com.example.impl.ItemLongClick;
import com.example.model.MonHoc;
import com.example.model.SinhVien;

import java.util.ArrayList;

public class SinhVienRecycleAdapter extends RecyclerView.Adapter<SinhVienRecycleAdapter.ViewHolder>{
    private Context context;
    private ArrayList<SinhVien> data;
    ItemLongClick itemLongClick;
    ItemClick itemClick;

    public SinhVienRecycleAdapter(Context context, ArrayList<SinhVien> data, ItemLongClick itemLongClick, ItemClick itemClick){
        this.context=context;
        this.data=data;
        this.itemLongClick=itemLongClick;
        this.itemClick=itemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        View view=null;
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_sinhvien,viewGroup,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        final SinhVien sinhVien=data.get(i);
        viewHolder.txtTen.setText(sinhVien.getTenSinhVien());
        viewHolder.txtMaSv.setText(sinhVien.getMaSinhVien());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtTen;
        TextView txtMaSv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
           txtTen=itemView.findViewById(R.id.txtTen);
           txtMaSv= itemView.findViewById(R.id.txtMaSV);
           itemView.setOnLongClickListener(new View.OnLongClickListener() {
               @Override
               public boolean onLongClick(View v) {
                   itemLongClick.isClicedItem(getAdapterPosition());
                   return false;
               }
           });
           itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   itemClick.isItemClick(getAdapterPosition());
               }
           });
        }
    }
}
