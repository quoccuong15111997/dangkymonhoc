package com.example.dangkymonhoc;

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
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.adapter.MonHocAdapter;
import com.example.impl.CheckBoxIsCheck;
import com.example.model.BienLai;
import com.example.model.MonHoc;
import com.example.model.ThongTinHocPhi;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class HocPhiFragment extends Fragment implements CheckBoxIsCheck {
    View view;
    SQLiteDatabase database = null;
    public static BienLai bienLaiHocPhi;
    ArrayList<ThongTinHocPhi> dsThongTin;
    public static ArrayList<MonHoc> dsMonHoc;
    ListView lvMonHoc;
    MonHocAdapter monHocAdapter;
    ArrayList<MonHoc> dsMonHocChon;
    FloatingActionButton fabAdd, fabDelete;
    int REQUEST_CODE_ADD_MONHOC = 1;

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

    private void addControls() {
        lvMonHoc = view.findViewById(R.id.lvMonHoc);
        dsMonHoc = new ArrayList<>();
        monHocAdapter = new MonHocAdapter(view.getContext(), R.layout.item_monhoc, dsMonHoc);
        monHocAdapter.isChecked(this);
        layDuLieuDatabase();
        lvMonHoc.setAdapter(monHocAdapter);
        dsMonHocChon = new ArrayList<>();
        fabAdd = view.findViewById(R.id.fabAdd);
        fabDelete = view.findViewById(R.id.fabDelete);
        bienLaiHocPhi = new BienLai();
        dsThongTin = new ArrayList<>();
    }

    private void layDuLieuDatabase() {

        layBienLaiHPTask layBienLaiHPTask = new layBienLaiHPTask();
        layBienLaiHPTask.execute(HomeActivity.sinhVienLogin.getMaSinhVien());
    }

    private void layChiTietMonHoc() {
        for (ThongTinHocPhi thongTinHocPhi : dsThongTin) {
            layChiTietMonHocTask task = new layChiTietMonHocTask();
            task.execute(thongTinHocPhi.getMaMH());
        }
    }

    private void layThongTinBienLai(int soBL) {
        layThongTinBienLaiTask layThongTinBienLaiTask = new layThongTinBienLaiTask();
        layThongTinBienLaiTask.execute(soBL);
    }

    class layBienLaiHPTask extends AsyncTask<String, Void, BienLai> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(BienLai bienLai) {
            super.onPostExecute(bienLai);
            bienLaiHocPhi.setSoBL(bienLai.getSoBL());
            bienLaiHocPhi.setNgayHP(bienLai.getNgayHP());
            bienLaiHocPhi.setMaSV(bienLai.getMaSV());
            layThongTinBienLai(bienLai.getSoBL());
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected BienLai doInBackground(String... strings) {
            String maSv = strings[0];
            database = view.getContext().openOrCreateDatabase("DHMH.db", MODE_PRIVATE, null);
            Cursor cursor = database.rawQuery("select * from BIENLAIHOCPHI where MASV like '" + maSv + "'", null);
            BienLai bl = new BienLai();
            while (cursor.moveToNext()) {
                int soBL = cursor.getInt(0);
                String ngayHp = cursor.getString(1);
                String mSv = cursor.getString(2);
                bl.setMaSV(mSv);
                bl.setNgayHP(ngayHp);
                bl.setSoBL(soBL);
            }
            cursor.close();
            return bl;
        }
    }

    class layThongTinBienLaiTask extends AsyncTask<Integer, Void, ArrayList<ThongTinHocPhi>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<ThongTinHocPhi> thongTinHocPhis) {
            super.onPostExecute(thongTinHocPhis);
            dsThongTin.clear();
            dsThongTin.addAll(thongTinHocPhis);
            layChiTietMonHoc();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected ArrayList<ThongTinHocPhi> doInBackground(Integer... integers) {
            ArrayList<ThongTinHocPhi> dsThongTinHP = new ArrayList<>();
            database = view.getContext().openOrCreateDatabase("DHMH.db", MODE_PRIVATE, null);
            Cursor cursor = database.rawQuery("select * from THONGTINHOCPHI where SOBL like '" + integers[0] + "'", null);
            while (cursor.moveToNext()) {
                int soBl = cursor.getInt(0);
                String maMH = cursor.getString(1);
                int soTien = cursor.getInt(2);
                ThongTinHocPhi thongTinHocPhi = new ThongTinHocPhi(soBl, maMH, soTien);
                dsThongTinHP.add(thongTinHocPhi);
            }
            cursor.close();
            return dsThongTinHP;
        }
    }

    private void xuLyXoaMonHoc() {

        /*for (MonHoc monHoc : dsMonHocChon){
            xoaMonHocTask task= new xoaMonHocTask();
            task.execute(monHoc.getMaMH());
        }*/
        xoaMonHocTask xoaMonHocTask = new xoaMonHocTask();
        xoaMonHocTask.execute(dsMonHocChon);

        monHocAdapter.notifyDataSetChanged();
    }

class xoaMonHocTask extends AsyncTask<ArrayList<MonHoc>, Void, Boolean> {
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
        monHocAdapter.clear();
        layDuLieuDatabase();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected Boolean doInBackground(ArrayList<MonHoc>... arrayLists) {
        for (MonHoc mh : arrayLists[0]) {
            database.delete("THONGTINHOCPHI", "SOBL=? and MAMH like ?", new String[]{String.valueOf(bienLaiHocPhi.getSoBL()), mh.getMaMH()});
        }
        return true;
    }
}


class layChiTietMonHocTask extends AsyncTask<String, Void, ArrayList<MonHoc>> {
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(ArrayList<MonHoc> monHocs) {
        super.onPostExecute(monHocs);
        dsMonHoc.addAll(monHocs);
        monHocAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected ArrayList<MonHoc> doInBackground(String... strings) {
        ArrayList<MonHoc> ds = new ArrayList<>();
        database = view.getContext().openOrCreateDatabase("DHMH.db", MODE_PRIVATE, null);
        Cursor cursor = database.rawQuery("select * from MONHOC where MAMH like '" + strings[0] + "'", null);
        while (cursor.moveToNext()) {
            String maMh = cursor.getString(0);
            String tenMH = cursor.getString(1);
            int soTC = cursor.getInt(2);
            MonHoc monHoc = new MonHoc(maMh, tenMH, soTC,1);
            ds.add(monHoc);
        }
        cursor.close();
        return ds;
    }

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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_ADD_MONHOC) {
            if (resultCode == Activity.RESULT_OK) {
                monHocAdapter.clear();
                layDuLieuDatabase();
            }
        }
    }
}
