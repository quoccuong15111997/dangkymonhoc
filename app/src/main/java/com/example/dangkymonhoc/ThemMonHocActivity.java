package com.example.dangkymonhoc;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.adapter.MonHocRecycleAdapter;
import com.example.impl.CheckBoxIsCheck;
import com.example.model.MonHoc;

import java.util.ArrayList;

public class ThemMonHocActivity extends AppCompatActivity implements CheckBoxIsCheck {
    RecyclerView rcyMonHoc;
    MonHocRecycleAdapter recycleAdapter;
    ImageView imgBack, imgSave;
    ArrayList<MonHoc> dsMonHoc;
    SQLiteDatabase database = null;
    ArrayList<MonHoc> dsMonHocChon;

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
    }

    private void xuLyLuu() {
        for (MonHoc monHoc : dsMonHocChon) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("SOBL", MonHocFragment.bienLaiHocPhi.getSoBL());
            contentValues.put("MAMH", monHoc.getMaMH());
            contentValues.put("SOTIEN", monHoc.getSoTC() * 400000);
            database.insert("THONGTINHOCPHI", null, contentValues);
            Toast.makeText(ThemMonHocActivity.this, "Thêm thành công", Toast.LENGTH_LONG).show();
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
    }

    @Override
    public void isChecked(int position, boolean check) {
        View view = findViewById(R.id.layout_ThemMonHoc);
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
        for(MonHoc monHocDaChon : MonHocFragment.dsMonHoc){
            for (MonHoc monHocMoi : dsMonHoc){
                if(monHocDaChon.getMaMH().equals(monHocMoi.getMaMH())){
                    monHocMoi.setChon(1);
                }
                else
                    monHocDaChon.setChon(2);
            }
        }
        recycleAdapter.notifyDataSetChanged();
        cursor.close();
    }
}
