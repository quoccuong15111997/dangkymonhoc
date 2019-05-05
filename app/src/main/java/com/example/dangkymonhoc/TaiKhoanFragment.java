package com.example.dangkymonhoc;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.adapter.HoaDonAdapter;
import com.example.firebase.SinhVienFirebase;
import com.example.model.BienLai;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;

import static android.content.Context.MODE_PRIVATE;

public class TaiKhoanFragment extends Fragment {
    View view;
    DatabaseReference mData;
    AvatarView avatarView;
    IImageLoader iImageLoader;
    TextView txtTen,txtPhone, txtMaSv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_taikhoan, container, false);
        initFirebase();
        addControls();
        addEvents();
        return view;
    }

    private void initFirebase() {
        mData= FirebaseDatabase.getInstance().getReference();
    }

    private void addEvents() {
    }

    private void addControls() {
        avatarView=view.findViewById(R.id.avatar_view);
        txtTen=view.findViewById(R.id.txtTen);
        txtMaSv=view.findViewById(R.id.txtMaSV);
        txtPhone=view.findViewById(R.id.txtPhone);
        iImageLoader= new PicassoLoader();

        txtPhone.setText(HomeActivity.sinhVienLogin.getPhone()+"");
        txtMaSv.setText(HomeActivity.sinhVienLogin.getMaSinhVien());
        txtTen.setText(HomeActivity.sinhVienLogin.getTenSinhVien());

        mData.child("SinhVien").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                SinhVienFirebase sinhVienFirebase=dataSnapshot.getValue(SinhVienFirebase.class);
                if(sinhVienFirebase.getMaSinhVien().equals(HomeActivity.sinhVienLogin.getMaSinhVien())==true){
                    iImageLoader.loadImage(avatarView,sinhVienFirebase.getUrlImage(),sinhVienFirebase.getTenSinhVien());
                }
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

}
