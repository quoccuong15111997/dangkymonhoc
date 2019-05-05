package com.example.dangkymonhoc;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adapter.MonHocRecycleAdapter;
import com.example.impl.CheckBoxIsCheck;
import com.example.model.BienLai;
import com.example.model.MonHoc;
import com.example.model.ThongTinHocPhi;

import java.util.ArrayList;

public class ThemMonHocActivity extends AppCompatActivity implements CheckBoxIsCheck {
    RecyclerView rcyMonHoc;
    MonHocRecycleAdapter recycleAdapter;
    ImageView imgBack, imgSave;
    ArrayList<MonHoc> dsMonHoc;
    SQLiteDatabase database = null;
    ArrayList<MonHoc> dsMonHocChon;
    ArrayList<MonHoc> dsMonHocDaDangKy;
    EditText edtSeach;
    ImageView imgSeach;
    TextView txtSoTinChi;
    int tongSoTinChi=0;
    public static String DATABASE_NAME="DHMH.db";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_mon_hoc);
        addControls();
        addEvents();
    }

    private void addEvents() {
        imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyLuu();
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", 1);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
        imgSeach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyTim();
            }
        });
    }

    private void xuLyTim() {
        if (edtSeach.getText().toString().equals("") == false) {
            TimmonHocTheoTenTask task = new TimmonHocTheoTenTask();
            task.execute(edtSeach.getText().toString());
        } else {
            dsMonHoc.clear();
            layDanhSachMonHoc(dsMonHocDaDangKy);
        }
    }

    private void xuLyLuu() {
        LuuMonHocTask task= new LuuMonHocTask();
        task.execute(dsMonHocChon);

    }

    private void addControls() {

        openDatabase();

        dsMonHocDaDangKy= new ArrayList<>();
        dsMonHocChon = new ArrayList<>();
        dsMonHoc = new ArrayList<>();
        rcyMonHoc = findViewById(R.id.rcy_MonHoc);
        recycleAdapter = new MonHocRecycleAdapter(ThemMonHocActivity.this, dsMonHoc);
        recycleAdapter.isChecked(this);
        rcyMonHoc.setLayoutManager(new LinearLayoutManager(ThemMonHocActivity.this));
        layDanhSachMonHocDaDangKy();
        layDanhSachMonHoc(dsMonHocDaDangKy);
        rcyMonHoc.setAdapter(recycleAdapter);
        imgBack = findViewById(R.id.iv_back);
        imgSave = findViewById(R.id.iv_Save);
        edtSeach = findViewById(R.id.edtSeach);
        imgSeach = findViewById(R.id.imgSeach);
        txtSoTinChi=findViewById(R.id.txtSoTinChi);
        for(MonHoc mh : MonHocFragments.dsMonHoc){
            tongSoTinChi+=mh.getSoTC();
        }
        txtSoTinChi.setText(tongSoTinChi+" Tín chỉ");
    }
    private void openDatabase() {

        database=openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE,null);
    }

    private void layDanhSachMonHocDaDangKy() {
        LayDanhSachMonHocTheoMaSinhVienTask task= new LayDanhSachMonHocTheoMaSinhVienTask();
        task.execute(HomeActivity.sinhVienLogin.getMaSinhVien());
    }

    @Override
    public void isChecked(int position, boolean check) {
        View view = findViewById(R.id.layout_ThemMonHoc);
        MonHoc monHoc = dsMonHoc.get(position);
        if (check == true) {
            //Toast.makeText(view.getContext(), "Bạn chọn: " + monHoc.getTenMH(), Toast.LENGTH_LONG).show();
            boolean kq=KiemTraTonTai(monHoc);
            if(kq==true){
                dsMonHocChon.add(monHoc);
                tongSoTinChi+=monHoc.getSoTC();
                txtSoTinChi.setText(tongSoTinChi+"");
            }

        } else if (check == false) {
            //Toast.makeText(view.getContext(), "Bạn bỏ chọn: " + monHoc.getTenMH(), Toast.LENGTH_LONG).show();
            Snackbar.make(view, "Bạn bỏ chọn: " + monHoc.getTenMH(), Snackbar.LENGTH_LONG).show();
            dsMonHocChon.remove(monHoc);
            tongSoTinChi-=monHoc.getSoTC();
            txtSoTinChi.setText(tongSoTinChi+" Tín chỉ");
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

    public void layDanhSachMonHoc(ArrayList<MonHoc> ds) {
        dsMonHoc.clear();
        Cursor cursor = database.rawQuery("select * from MONHOC ", null);
        while (cursor.moveToNext()) {
            String maMh = cursor.getString(0);
            String tenMH = cursor.getString(1);
            int soTC = cursor.getInt(2);
            MonHoc monHoc = new MonHoc();
            monHoc.setMaMH(maMh);
            monHoc.setSoTC(soTC);
            monHoc.setTenMH(tenMH);
            dsMonHoc.add(monHoc);
        }
        for (MonHoc monHocDaChon : ds) {
            for (MonHoc monHocMoi : dsMonHoc) {
                if (monHocDaChon.getMaMH().equals(monHocMoi.getMaMH())) {
                    monHocMoi.setChon(true);
                } else
                    monHocDaChon.setChon(false);
            }
        }
        recycleAdapter.notifyDataSetChanged();
        cursor.close();
    }

    class TimmonHocTheoTenTask extends AsyncTask<String, Void, ArrayList<MonHoc>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<MonHoc> monHocs) {
            super.onPostExecute(monHocs);
            if (monHocs.size() != 0) {
                dsMonHoc.clear();
                dsMonHoc.addAll(monHocs);
                recycleAdapter.notifyDataSetChanged();
            } else {
                xuLyTimTheoMa();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected ArrayList<MonHoc> doInBackground(String... strings) {
            ArrayList<MonHoc> monHocs = new ArrayList<>();
            Cursor cursor = database.rawQuery("select * from MONHOC where TENMH like '%" + strings[0] + "%'", null);
            while (cursor.moveToNext()) {
                String ma = cursor.getString(0);
                String ten = cursor.getString(1);
                int soTC = cursor.getInt(2);
                MonHoc monHoc = new MonHoc(ma, ten, soTC, false,false);
                monHocs.add(monHoc);
            }
            cursor.close();
            for (MonHoc monHocDaChon : dsMonHocDaDangKy) {
                for (MonHoc monHocMoi : monHocs) {
                    if (monHocDaChon.getMaMH().equals(monHocMoi.getMaMH())) {
                        monHocMoi.setChon(true);
                    } else
                        monHocDaChon.setChon(false);
                }
            }
            return monHocs;
        }
    }

    private void xuLyTimTheoMa() {
        TimmonHocTheoMaTask timmonHocTheoMaTask = new TimmonHocTheoMaTask();
        timmonHocTheoMaTask.execute(edtSeach.getText().toString());
    }

    class TimmonHocTheoMaTask extends AsyncTask<String, Void, ArrayList<MonHoc>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<MonHoc> monHocs) {
            super.onPostExecute(monHocs);
            if (monHocs.size() != 0) {
                dsMonHoc.clear();
                dsMonHoc.addAll(monHocs);
                recycleAdapter.notifyDataSetChanged();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(ThemMonHocActivity.this);
                builder.setTitle("Không tìm thấy môn học").setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setIcon(R.drawable.ic_error).show();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected ArrayList<MonHoc> doInBackground(String... strings) {
            ArrayList<MonHoc> monHocs = new ArrayList<>();
            Cursor cursor = database.rawQuery("select * from MONHOC where MAMH like '%" + strings[0] + "%'", null);
            while (cursor.moveToNext()) {
                String ma = cursor.getString(0);
                String ten = cursor.getString(1);
                int soTC = cursor.getInt(2);
                MonHoc monHoc = new MonHoc(ma, ten, soTC, true,false);
                monHocs.add(monHoc);
            }
            cursor.close();
            for (MonHoc monHocDaChon : dsMonHocDaDangKy) {
                for (MonHoc monHocMoi : monHocs) {
                    if (monHocDaChon.getMaMH().equals(monHocMoi.getMaMH())) {
                        monHocMoi.setChon(true);
                    } else
                        monHocDaChon.setChon(false);
                }
            }
            return monHocs;
        }
    }
    class LuuMonHocTask extends AsyncTask<ArrayList<MonHoc>,Void,Boolean>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            AlertDialog.Builder builder = new AlertDialog.Builder(ThemMonHocActivity.this);
            builder.setTitle("Lưu thành công").setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).setIcon(R.drawable.ic_ok).show();
            dsMonHoc.clear();
            layDanhSachMonHocDaDangKy();
            layDanhSachMonHoc(dsMonHocDaDangKy);
            recycleAdapter.notifyDataSetChanged();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Boolean doInBackground(ArrayList<MonHoc>... arrayLists) {
            for (MonHoc monHoc : arrayLists[0]) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("SOBL", MonHocFragments.bienLaiHocPhi.getSoBL());
                contentValues.put("MAMH", monHoc.getMaMH());
                contentValues.put("SOTIEN", monHoc.getSoTC() * 400000);
                database.insert("THONGTINHOCPHI", null, contentValues);
            }
            return true;
        }
    }
    class LayDanhSachMonHocTheoMaSinhVienTask extends AsyncTask<String,Void,ArrayList<MonHoc>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<MonHoc> monHocs) {
            super.onPostExecute(monHocs);
            dsMonHocDaDangKy.clear();;
            dsMonHocDaDangKy.addAll(monHocs);
            layDanhSachMonHoc(monHocs);
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
}
