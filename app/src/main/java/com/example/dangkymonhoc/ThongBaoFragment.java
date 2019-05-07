package com.example.dangkymonhoc;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.adapter.HocPhiAdapter;
import com.example.adapter.ThongBaoAdapter;
import com.example.model.MonHoc;
import com.example.model.ThongBao;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ThongBaoFragment extends Fragment {
    View view;
    ListView lvThongBao;
    ThongBaoAdapter thongBaoAdapter;
    ArrayList<ThongBao> dsThongBao;
    DatabaseReference mData;
    ImageView imgAdd;
    ArrayList<String> dsKEY;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_thongbao, container, false);
        addControls();
        initFirebase();
        addEvents();
        return view;
    }

    private void autoUpdate() {
        dsKEY.clear();
        dsThongBao.clear();
        initFirebase();
        thongBaoAdapter.notifyDataSetChanged();
    }

    private void initFirebase() {
        mData= FirebaseDatabase.getInstance().getReference();
        mData.child("ThongBao").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ThongBao thongBao=dataSnapshot.getValue(ThongBao.class);
                dsKEY.add(dataSnapshot.getKey());
                dsThongBao.add(thongBao);
                thongBaoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addEvents() {
        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(view.getContext(),ThemThongBaoActivity.class);
                startActivity(intent);
            }
        });
        lvThongBao.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

            }
        });
        lvThongBao.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if(HomeActivity.sinhVienLogin.getMaSinhVien().equals("admin")){
                    AlertDialog.Builder builder= new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Bạn muốn").setNegativeButton("Xóa", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            xuLyXoaMessage(position);
                        }
                    }).setPositiveButton("Hủy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
                }
                return false;
            }
        });
    }

    private void xuLyXoaMessage(int position) {
        mData.child("ThongBao").child(dsKEY.get(position)).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if(databaseError==null){
                    AlertDialog.Builder builder= new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Xóa thành công").setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).setIcon(R.drawable.ic_ok).show();

                    autoUpdate();
                }
                else {
                    AlertDialog.Builder builder= new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Xóa thất bại").setNegativeButton("Thử lại", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).setIcon(R.drawable.ic_error).show();
                }
            }
        });
    }

    private void addControls() {
        dsKEY= new ArrayList<>();
        dsThongBao= new ArrayList<>();
        lvThongBao=view.findViewById(R.id.lvThongBao);
        thongBaoAdapter= new ThongBaoAdapter(view.getContext(),R.layout.item_thong_bao,dsThongBao);
        lvThongBao.setAdapter(thongBaoAdapter);
        imgAdd=view.findViewById(R.id.iv_add);

        if(HomeActivity.sinhVienLogin.getMaSinhVien().equals("admin")){
            imgAdd.setVisibility(View.VISIBLE);
        }
    }
}
