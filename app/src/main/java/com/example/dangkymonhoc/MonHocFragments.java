package com.example.dangkymonhoc;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.adapter.MonHocAdapter;
import com.example.impl.CheckBoxIsCheck;
import com.example.model.BienLai;
import com.example.model.MonHoc;
import com.example.model.ThongTinHocPhi;

import java.security.spec.ECField;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class MonHocFragments extends Fragment implements CheckBoxIsCheck{
    View view;
    SQLiteDatabase database = null;
    public static String DATABASE_NAME="DHMH.db";
    public static BienLai bienLaiHocPhi;
    ArrayList<ThongTinHocPhi> dsThongTin;
    public static ArrayList<MonHoc> dsMonHoc;
    RecyclerView recycle_monHoc;
    MonHocAdapter monHocAdapter;
    ArrayList<MonHoc> dsMonHocChon;
    FloatingActionButton fabAdd, fabDelete, fabMenu;
    int REQUEST_CODE_ADD_MONHOC = 1;
    TextView txtSoTinChi;
    public  static int tongSoTinChi=0;
    Animation animation_out,animation_in;
    boolean trangTrai= false;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_monhoc, container, false);
        addControls();
        addEvents();
        return view;

    }

    private void addEvents() {
        fabMenu.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                if(trangTrai==false){
                    fabMenu.startAnimation(animation_in);
                    fabAdd.startAnimation(animation_in);
                    fabAdd.setVisibility(View.VISIBLE);
                    fabDelete.startAnimation(animation_in);
                    fabDelete.setVisibility(View.VISIBLE);
                    trangTrai=true;
                }
                else if(trangTrai==true){
                    fabMenu.startAnimation(animation_in);
                    fabAdd.startAnimation(animation_out);
                    fabAdd.setVisibility(View.INVISIBLE);
                    fabDelete.startAnimation(animation_out);
                    fabDelete.setVisibility(View.INVISIBLE);
                    trangTrai=false;
                }
            }
        });
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), ThemMonHocActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ADD_MONHOC);
            }
        });
        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Xác nhận xóa").setNegativeButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        xuLyXoaMonHoc();
                    }
                }).setPositiveButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
        });
    }

    private void xuLyXoaMonHoc() {
        XoaMonHocTask task= new XoaMonHocTask();
        task.execute(dsMonHocChon);
    }

    private void addControls() {

        openDatabase();

        LayThongTinBienLaiTask task= new LayThongTinBienLaiTask();
        task.execute(HomeActivity.sinhVienLogin.getMaSinhVien());

       recycle_monHoc=view.findViewById(R.id.recycle_mon_hoc);
        recycle_monHoc.setLayoutManager(new LinearLayoutManager(this.view.getContext()));
        dsMonHoc = new ArrayList<>();
        monHocAdapter= new MonHocAdapter(view.getContext(),dsMonHoc);
        monHocAdapter.isChecked(this);
        txtSoTinChi=view.findViewById(R.id.txtSoTinChi);
        layDuLieuDatabase();
        recycle_monHoc.setAdapter(monHocAdapter);
        dsMonHocChon = new ArrayList<>();
        fabAdd = view.findViewById(R.id.fabAdd);
        fabDelete = view.findViewById(R.id.fabDelete);
        bienLaiHocPhi = new BienLai();
        dsThongTin = new ArrayList<>();
        fabMenu=view.findViewById(R.id.fabMenu);

        animation_out= AnimationUtils.loadAnimation(view.getContext(),R.anim.fab_amin);
        animation_in=AnimationUtils.loadAnimation(view.getContext(),R.anim.fab_in);
    }
    //mở database
    private void openDatabase() {

        database=view.getContext().openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE,null);

    }

    private void layDuLieuDatabase() {
        LayDanhSachMonHocTheoMaSinhVienTask task= new LayDanhSachMonHocTheoMaSinhVienTask();
        task.execute(HomeActivity.sinhVienLogin.getMaSinhVien());
    }

    class LayDanhSachMonHocTheoMaSinhVienTask extends AsyncTask<String,Void,ArrayList<MonHoc>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<MonHoc> monHocs) {
            super.onPostExecute(monHocs);
            for(MonHoc monHoc : monHocs){
                tongSoTinChi+=monHoc.getSoTC();
            }
            txtSoTinChi.setText(tongSoTinChi+" Tín Chỉ");
            dsMonHoc.addAll(monHocs);
            monHocAdapter.notifyDataSetChanged();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected ArrayList<MonHoc> doInBackground(String... strings) {
            ArrayList<MonHoc> danhSachMonHoc=new ArrayList<>();
            String maSinhVien=strings[0];
            BienLai bienLai= new BienLai();
            ArrayList<ThongTinHocPhi> dsThongTinHP = new ArrayList<>();
            Cursor cursorBienLai = database.rawQuery("select * from BIENLAIHOCPHI where MASV like '" + maSinhVien + "'", null);
            bienLai= new BienLai();
            while (cursorBienLai.moveToNext()) {
                int soBL = cursorBienLai.getInt(0);
                String ngayHp = cursorBienLai.getString(1);
                String mSv = cursorBienLai.getString(2);
                bienLai.setMaSV(mSv);
                bienLai.setNgayHP(ngayHp);
                bienLai.setSoBL(soBL);
            }
            cursorBienLai.close();

            Cursor cursorThongTin = database.rawQuery("select * from THONGTINHOCPHI where SOBL like '" + bienLai.getSoBL() + "'", null);
            while (cursorThongTin.moveToNext()) {
                int soBl = cursorThongTin.getInt(0);
                String maMH = cursorThongTin.getString(1);
                int soTien = cursorThongTin.getInt(2);
                ThongTinHocPhi thongTinHocPhi = new ThongTinHocPhi(soBl, maMH, soTien);
                dsThongTinHP.add(thongTinHocPhi);
            }
            cursorThongTin.close();

            for (ThongTinHocPhi thongTinHocPhi : dsThongTinHP){
                Cursor cursor = database.rawQuery("select * from MONHOC where MAMH like '" + thongTinHocPhi.getMaMH() + "'", null);
                while (cursor.moveToNext()) {
                    String maMh = cursor.getString(0);
                    String tenMH = cursor.getString(1);
                    int soTC = cursor.getInt(2);
                    MonHoc monHoc = new MonHoc(maMh, tenMH, soTC,true,false);
                    danhSachMonHoc.add(monHoc);
                }
                cursor.close();
            }
            return danhSachMonHoc;
        }
    }
    class  LayThongTinBienLaiTask extends AsyncTask<String,Void,BienLai>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(BienLai bienLai) {
            super.onPostExecute(bienLai);
            bienLaiHocPhi.setSoBL(bienLai.getSoBL());
            bienLaiHocPhi.setNgayHP(bienLai.getNgayHP());
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected BienLai doInBackground(String... strings) {
            BienLai bienLai= new BienLai();
            ArrayList<ThongTinHocPhi> dsThongTinHP = new ArrayList<>();
            Cursor cursorBienLai = database.rawQuery("select * from BIENLAIHOCPHI where MASV like '" + strings[0] + "'", null);
            bienLai= new BienLai();
            while (cursorBienLai.moveToNext()) {
                int soBL = cursorBienLai.getInt(0);
                String ngayHp = cursorBienLai.getString(1);
                String mSv = cursorBienLai.getString(2);
                bienLai.setMaSV(mSv);
                bienLai.setNgayHP(ngayHp);
                bienLai.setSoBL(soBL);
            }
            cursorBienLai.close();
            return bienLai;
        }
    }
    class XoaMonHocTask extends AsyncTask<ArrayList<MonHoc>,Void,Boolean>{

        @Override
        protected Boolean doInBackground(ArrayList<MonHoc>... arrayLists) {
            try {
                for(MonHoc mh : arrayLists[0]){
                    database.delete("THONGTINHOCPHI", "SOBL=? and MAMH like ?", new String[]{String.valueOf(bienLaiHocPhi.getSoBL()), mh.getMaMH()});
                }
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("Xóa thành công").setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            }).show();
            tongSoTinChi=0;
            dsMonHoc.clear();
            layDuLieuDatabase();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
    public void isChecked(int position, boolean check) {
        MonHoc monHoc = dsMonHoc.get(position);
        if (check == true) {
            //Toast.makeText(view.getContext(), "Bạn chọn: " + monHoc.getTenMH(), Toast.LENGTH_LONG).show();
            boolean kq=KiemTraTonTai(monHoc);
            if(kq==true){
                dsMonHocChon.add(monHoc);
            }

        } else if (check == false) {
            //Toast.makeText(view.getContext(), "Bạn bỏ chọn: " + monHoc.getTenMH(), Toast.LENGTH_LONG).show();
            dsMonHocChon.remove(monHoc);
        }
    }
    private boolean KiemTraTonTai(MonHoc monHoc) {
        for(MonHoc mh : dsMonHocChon){
            if(mh.getMaMH().equals(monHoc.getMaMH())){
                return false;
            }
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_ADD_MONHOC) {
            if (resultCode == Activity.RESULT_OK) {
                dsMonHoc.clear();
                tongSoTinChi=0;
                layDuLieuDatabase();
            }
        }
    }
}
