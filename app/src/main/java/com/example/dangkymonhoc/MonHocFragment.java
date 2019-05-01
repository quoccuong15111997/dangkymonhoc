package com.example.dangkymonhoc;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.model.BienLai;
import com.example.model.MonHoc;
import com.example.model.SinhVien;
import com.example.model.ThongTinHocPhi;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class MonHocFragment extends Fragment {
    View view;
    SQLiteDatabase database=null;
    BienLai bienLai;
    ArrayList<ThongTinHocPhi>dsThongTin;
    ArrayList<MonHoc> dsMonHoc;
    ListView lvMonHoc;
    ArrayAdapter<MonHoc> monHocAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_monhoc,container,false);
        addControls();
        addEvents();
        return view;
    }

    private void addEvents() {
    }

    private void addControls() {
        lvMonHoc=view.findViewById(R.id.lvMonHoc);
        monHocAdapter=new ArrayAdapter<>(view.getContext(),android.R.layout.simple_list_item_1);
        lvMonHoc.setAdapter(monHocAdapter);
        layBienLaiHP(HomeActivity.sinhVienLogin.getMaSinhVien());
        layThongTinBienLaiHP(bienLai.getSoBL());
        for (ThongTinHocPhi thongTinHocPhi : dsThongTin){
            layChiTietMonHoc(thongTinHocPhi.getMaMH());
        }
    }

    public void layBienLaiHP(String maSv) {
        database=view.getContext().openOrCreateDatabase("DHMH.db",MODE_PRIVATE,null);
        Cursor cursor=database.rawQuery("select * from BIENLAIHOCPHI where MASV like '"+ maSv+"'",null);
        while (cursor.moveToNext())
        {
            int soBL=cursor.getInt(0);
            String ngayHp=cursor.getString(1);
            String mSv=cursor.getString(2);
            bienLai= new BienLai(soBL,ngayHp,maSv);
        }
        cursor.close();
    }
    public void layThongTinBienLaiHP(int soBL) {
        dsThongTin= new ArrayList<>();
        database=view.getContext().openOrCreateDatabase("DHMH.db",MODE_PRIVATE,null);
        Cursor cursor=database.rawQuery("select * from THONGTINHOCPHI where SOBL like '"+ soBL+"'",null);
        while (cursor.moveToNext())
        {
            int soBl=cursor.getInt(0);
            String maMH=cursor.getString(1);
            int soTien=cursor.getInt(2);
            ThongTinHocPhi thongTinHocPhi= new ThongTinHocPhi(soBl,maMH,soTien);
            dsThongTin.add(thongTinHocPhi);
        }
        cursor.close();
    }
    public void layChiTietMonHoc(String maMH) {
        dsMonHoc= new ArrayList<>();
        database=view.getContext().openOrCreateDatabase("DHMH.db",MODE_PRIVATE,null);
        Cursor cursor=database.rawQuery("select * from MONHOC where MAMH like '"+ maMH+"'",null);
        while (cursor.moveToNext())
        {
            String maMh=cursor.getString(0);
            String tenMH=cursor.getString(1);
            int soTC=cursor.getInt(2);
            MonHoc monHoc= new MonHoc(maMh,tenMH,soTC);
            dsMonHoc.add(monHoc);
        }
        monHocAdapter.addAll(dsMonHoc);
        monHocAdapter.notifyDataSetChanged();
        cursor.close();
    }
}
