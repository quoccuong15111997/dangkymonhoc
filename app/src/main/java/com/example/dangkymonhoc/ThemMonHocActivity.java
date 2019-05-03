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
    EditText edtSeach;
    ImageView imgSeach;
    TextView txtSoTinChi;
    int tongSoTinChi=0;

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
            layDanhSachMonHoc();
        }
    }

    private void xuLyLuu() {
        for (MonHoc monHoc : dsMonHocChon) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("SOBL", MonHocFragment.bienLaiHocPhi.getSoBL());
            contentValues.put("MAMH", monHoc.getMaMH());
            contentValues.put("SOTIEN", monHoc.getSoTC() * 400000);
            database.insert("THONGTINHOCPHI", null, contentValues);
            AlertDialog.Builder builder = new AlertDialog.Builder(ThemMonHocActivity.this);
            builder.setTitle("Lưu thành công").setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).setIcon(R.drawable.ic_ok).show();
        }
    }

    private void addControls() {
        dsMonHocChon = new ArrayList<>();
        dsMonHoc = new ArrayList<>();
        rcyMonHoc = findViewById(R.id.rcy_MonHoc);
        recycleAdapter = new MonHocRecycleAdapter(ThemMonHocActivity.this, dsMonHoc);
        recycleAdapter.isChecked(this);
        rcyMonHoc.setLayoutManager(new LinearLayoutManager(ThemMonHocActivity.this));
        layDanhSachMonHoc();
        rcyMonHoc.setAdapter(recycleAdapter);
        imgBack = findViewById(R.id.iv_back);
        imgSave = findViewById(R.id.iv_Save);
        edtSeach = findViewById(R.id.edtSeach);
        imgSeach = findViewById(R.id.imgSeach);
        txtSoTinChi=findViewById(R.id.txtSoTinChi);
        for(MonHoc mh : MonHocFragment.dsMonHoc){
            tongSoTinChi+=mh.getSoTC();
        }
        txtSoTinChi.setText(tongSoTinChi+" Tín chỉ");
    }

    @Override
    public void isChecked(int position, boolean check) {
        View view = findViewById(R.id.layout_ThemMonHoc);
        MonHoc monHoc = dsMonHoc.get(position);
        if (check == true) {
            //Toast.makeText(view.getContext(), "Bạn chọn: " + monHoc.getTenMH(), Toast.LENGTH_LONG).show();
            Snackbar.make(view, "Bạn chọn: " + monHoc.getTenMH(), Snackbar.LENGTH_LONG).show();
            tongSoTinChi+=monHoc.getSoTC();
            dsMonHocChon.add(monHoc);
            txtSoTinChi.setText(tongSoTinChi+" Tín chỉ");
        } else if (check == false) {
            //Toast.makeText(view.getContext(), "Bạn bỏ chọn: " + monHoc.getTenMH(), Toast.LENGTH_LONG).show();
            Snackbar.make(view, "Bạn bỏ chọn: " + monHoc.getTenMH(), Snackbar.LENGTH_LONG).show();
            dsMonHocChon.remove(monHoc);
            tongSoTinChi-=monHoc.getSoTC();
            txtSoTinChi.setText(tongSoTinChi+" Tín chỉ");
        }
    }

    public void layDanhSachMonHoc() {
        database = openOrCreateDatabase("DHMH.db", MODE_PRIVATE, null);
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
        for (MonHoc monHocDaChon : MonHocFragment.dsMonHoc) {
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
            database = ThemMonHocActivity.this.openOrCreateDatabase("DHMH.db", MODE_PRIVATE, null);
            Cursor cursor = database.rawQuery("select * from MONHOC where TENMH like '%" + strings[0] + "%'", null);
            while (cursor.moveToNext()) {
                String ma = cursor.getString(0);
                String ten = cursor.getString(1);
                int soTC = cursor.getInt(2);
                MonHoc monHoc = new MonHoc(ma, ten, soTC, true);
                monHocs.add(monHoc);
            }
            cursor.close();
            for (MonHoc monHocDaChon : MonHocFragment.dsMonHoc) {
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
            database = ThemMonHocActivity.this.openOrCreateDatabase("DHMH.db", MODE_PRIVATE, null);
            Cursor cursor = database.rawQuery("select * from MONHOC where MAMH like '%" + strings[0] + "%'", null);
            while (cursor.moveToNext()) {
                String ma = cursor.getString(0);
                String ten = cursor.getString(1);
                int soTC = cursor.getInt(2);
                MonHoc monHoc = new MonHoc(ma, ten, soTC, true);
                monHocs.add(monHoc);
            }
            cursor.close();
            for (MonHoc monHocDaChon : MonHocFragment.dsMonHoc) {
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
}
