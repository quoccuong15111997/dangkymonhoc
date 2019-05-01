package com.example.dangkymonhoc;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.adapter.MonHocAdapter;
import com.example.impl.CheckBoxIsCheck;
import com.example.model.BienLai;
import com.example.model.MonHoc;
import com.example.model.SinhVien;
import com.example.model.ThongTinHocPhi;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.TELECOM_SERVICE;

public class MonHocFragment extends Fragment implements CheckBoxIsCheck {
    View view;
    SQLiteDatabase database = null;
    public static BienLai bienLai;
    ArrayList<ThongTinHocPhi> dsThongTin;
    public static ArrayList<MonHoc> dsMonHoc;
    ListView lvMonHoc;
    MonHocAdapter monHocAdapter;
    ImageView imgDelete;
    Button btnXoa;
    ArrayList<MonHoc> dsMonHocChon;
    FloatingActionButton fabAdd;
    int REQUEST_CODE_ADD_MONHOC=1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_monhoc, container, false);
        addControls();
        addEvents();
        return view;
    }

    private void addEvents() {
        lvMonHoc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*AlertDialog.Builder builder= new AlertDialog.Builder(view.getContext());
                builder.setTitle("Xác nhận xóa").setNegativeButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        xuLyXoaMonHoc();
                    }
                }).setPositiveButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();*/
                xuLyXoaMonHoc();
            }
        });
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(view.getContext(),ThemMonHocActivity.class);
                startActivityForResult(intent,REQUEST_CODE_ADD_MONHOC);
            }
        });
    }

    private void xuLyXoaMonHoc() {

        for (MonHoc monHoc : dsMonHocChon){
            database.delete("THONGTINHOCPHI","SOBL=? and MAMH like ?",new String[]{String.valueOf(bienLai.getSoBL()),monHoc.getMaMH()});
        }
        AlertDialog.Builder builder= new AlertDialog.Builder(view.getContext());
        builder.setTitle("Xóa thành công").setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).show();
        layDuLieuDatabase();
    }

    private void addControls() {
        lvMonHoc = view.findViewById(R.id.lvMonHoc);
        dsMonHoc = new ArrayList<>();
        monHocAdapter = new MonHocAdapter(view.getContext(), R.layout.item_monhoc, dsMonHoc);
        monHocAdapter.isChecked(this);
        layDuLieuDatabase();
        lvMonHoc.setAdapter(monHocAdapter);
        //imgDelete = view.findViewById(R.id.imgDelete);
        //imgDelete.setVisibility(View.GONE);
        dsMonHocChon= new ArrayList<>();
        fabAdd=view.findViewById(R.id.fabAdd);
        btnXoa=view.findViewById(R.id.btnXoa);
    }

    private void layDuLieuDatabase() {
        monHocAdapter.clear();
        layBienLaiHP(HomeActivity.sinhVienLogin.getMaSinhVien());
        layThongTinBienLaiHP(bienLai.getSoBL());
        for (ThongTinHocPhi thongTinHocPhi : dsThongTin) {
            layChiTietMonHoc(thongTinHocPhi.getMaMH());
        }
        monHocAdapter.notifyDataSetChanged();
    }

    public void layBienLaiHP(String maSv) {
        database = view.getContext().openOrCreateDatabase("DHMH.db", MODE_PRIVATE, null);
        Cursor cursor = database.rawQuery("select * from BIENLAIHOCPHI where MASV like '" + maSv + "'", null);
        while (cursor.moveToNext()) {
            int soBL = cursor.getInt(0);
            String ngayHp = cursor.getString(1);
            String mSv = cursor.getString(2);
            bienLai = new BienLai(soBL, ngayHp, maSv);
        }
        cursor.close();
    }

    public void layThongTinBienLaiHP(int soBL) {
        dsThongTin = new ArrayList<>();
        database = view.getContext().openOrCreateDatabase("DHMH.db", MODE_PRIVATE, null);
        Cursor cursor = database.rawQuery("select * from THONGTINHOCPHI where SOBL like '" + soBL + "'", null);
        while (cursor.moveToNext()) {
            int soBl = cursor.getInt(0);
            String maMH = cursor.getString(1);
            int soTien = cursor.getInt(2);
            ThongTinHocPhi thongTinHocPhi = new ThongTinHocPhi(soBl, maMH, soTien);
            dsThongTin.add(thongTinHocPhi);
        }
        cursor.close();
    }

    public void layChiTietMonHoc(String maMH) {
        database = view.getContext().openOrCreateDatabase("DHMH.db", MODE_PRIVATE, null);
        Cursor cursor = database.rawQuery("select * from MONHOC where MAMH like '" + maMH + "'", null);
        while (cursor.moveToNext()) {
            String maMh = cursor.getString(0);
            String tenMH = cursor.getString(1);
            int soTC = cursor.getInt(2);
            MonHoc monHoc = new MonHoc(maMh, tenMH, soTC);
            dsMonHoc.add(monHoc);
        }
        cursor.close();
    }

    @Override
    public void isChecked(int position, boolean check) {
        MonHoc monHoc = dsMonHoc.get(position);
        if (check == true) {
            //Toast.makeText(view.getContext(), "Bạn chọn: " + monHoc.getTenMH(), Toast.LENGTH_LONG).show();
            Snackbar.make(view, "Bạn chọn: " + monHoc.getTenMH(), Snackbar.LENGTH_LONG).show();
            dsMonHocChon.add(monHoc);
        } else if (check == false) {
            //Toast.makeText(view.getContext(), "Bạn bỏ chọn: " + monHoc.getTenMH(), Toast.LENGTH_LONG).show();
            Snackbar.make(view, "Bạn bỏ chọn: " + monHoc.getTenMH(), Snackbar.LENGTH_LONG).show();
            dsMonHocChon.remove(monHoc);
        }
       /* if(dsMonHocChon.isEmpty()){
            imgDelete.setVisibility(View.GONE);
        }
        else
            imgDelete.setVisibility(View.VISIBLE);*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_ADD_MONHOC) {
            if (resultCode == Activity.RESULT_OK) {
                layDuLieuDatabase();
            }
        }
    }
}
