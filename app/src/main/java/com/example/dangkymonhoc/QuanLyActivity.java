package com.example.dangkymonhoc;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;

import com.example.adapter.MonHocRecycleAdapter;
import com.example.adapter.SinhVienRecycleAdapter;
import com.example.adapter.ThongBaoAdapter;
import com.example.impl.CheckBoxIsCheck;
import com.example.model.MonHoc;
import com.example.model.SinhVien;
import com.example.model.ThongBao;

import java.util.ArrayList;

public class QuanLyActivity extends AppCompatActivity implements CheckBoxIsCheck {
    View view;
    SQLiteDatabase database = null;
    public static String DATABASE_NAME = "DHMH.db";
    TabHost tabHost;
    RecyclerView recy_SinhVien,recy_MonHoc;
    ListView lvThongBao;
    MonHocRecycleAdapter monHocAdapter;
    SinhVienRecycleAdapter sinhVienAdapter;
    ThongBaoAdapter thongBaoAdapter;
    ArrayList<SinhVien> dsSinhVien;
    ArrayList<MonHoc> dsMonHoc;
    ArrayList<ThongBao> dsThongBao;
    int soMon=0;
    FloatingActionButton fabMenu, fabDelete, fabAdd;
    Boolean trangThaiFab=false;
    Animation animation_in, animation_out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly);
        addControls();
        addEvents();
        openDatabase();
        layDanhSachMonHoc();

    }

    private void addEvents() {
        fabMenu.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                if (trangThaiFab == false) {
                    fabAdd.startAnimation(animation_in);
                    fabAdd.setVisibility(View.VISIBLE);
                    fabDelete.startAnimation(animation_in);
                    fabDelete.setVisibility(View.VISIBLE);
                    trangThaiFab=true;
                }
                else if(trangThaiFab==true){
                    fabAdd.startAnimation(animation_out);
                    fabAdd.setVisibility(View.INVISIBLE);
                    fabDelete.startAnimation(animation_out);
                    fabDelete.setVisibility(View.INVISIBLE);
                    trangThaiFab=false;
                }
            }
        });
    }

    private void addControls() {
        setupTasbHost();
        setupControl();
    }

    private void setupControl() {

        dsMonHoc= new ArrayList<>();
        dsSinhVien= new ArrayList<>();
        dsThongBao= new ArrayList<>();

        recy_SinhVien=findViewById(R.id.recy_sinhvien);
        recy_SinhVien.setLayoutManager(new LinearLayoutManager(QuanLyActivity.this));
        sinhVienAdapter= new SinhVienRecycleAdapter(QuanLyActivity.this,dsSinhVien,soMon);
        recy_SinhVien.setAdapter(sinhVienAdapter);

        recy_MonHoc=findViewById(R.id.recy_monhoc);
        recy_MonHoc.setLayoutManager(new LinearLayoutManager(QuanLyActivity.this));
        monHocAdapter= new MonHocRecycleAdapter(QuanLyActivity.this,dsMonHoc);
        monHocAdapter.isChecked(this);
        recy_MonHoc.setAdapter(monHocAdapter);

        lvThongBao=findViewById(R.id.lvThongBao);
        thongBaoAdapter= new ThongBaoAdapter(QuanLyActivity.this,R.layout.item_thong_bao,dsThongBao);
        lvThongBao.setAdapter(thongBaoAdapter);

        fabMenu=findViewById(R.id.fabMenu);
        fabAdd=findViewById(R.id.fabAdd);
        fabDelete=findViewById(R.id.fabDelete);

        animation_out= AnimationUtils.loadAnimation(QuanLyActivity.this,R.anim.fab_amin);
        animation_in=AnimationUtils.loadAnimation(QuanLyActivity.this,R.anim.fab_in);
    }

    private void setupTasbHost() {
        tabHost = findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec tab1 = tabHost.newTabSpec("t1");
        tab1.setIndicator("", getDrawable(R.drawable.ic_account));
        tab1.setContent(R.id.tabSinhVien);
        tabHost.addTab(tab1);

        TabHost.TabSpec tab2 = tabHost.newTabSpec("t2");
        tab2.setIndicator("",getDrawable(R.drawable.ic_book));
        tab2.setContent(R.id.tabMonHoc);
        tabHost.addTab(tab2);

        TabHost.TabSpec tab3 = tabHost.newTabSpec("t3");
        tab3.setIndicator("",getDrawable(R.drawable.ic_noti));
        tab3.setContent(R.id.tabThongBao);
        tabHost.addTab(tab3);
    }
    private void openDatabase() {

        database=openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE,null);

    }
    public void layDanhSachMonHoc() {
        LayDanhSachTatCaMonHocTask task= new LayDanhSachTatCaMonHocTask();
        task.execute();

    }

    @Override
    public void isChecked(int position, boolean check) {

    }

    class LayDanhSachTatCaMonHocTask extends AsyncTask<Void,Void,ArrayList<MonHoc>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<MonHoc> monHocs) {
            super.onPostExecute(monHocs);
            dsMonHoc.clear();
            dsMonHoc.addAll(monHocs);
            monHocAdapter.notifyDataSetChanged();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected ArrayList<MonHoc> doInBackground(Void... voids) {
            ArrayList<MonHoc> ds = new ArrayList<>();
            Cursor cursor = database.rawQuery("select * from MONHOC ", null);
            while (cursor.moveToNext()) {
                String maMh = cursor.getString(0);
                String tenMH = cursor.getString(1);
                int soTC = cursor.getInt(2);
                MonHoc monHoc = new MonHoc();
                monHoc.setMaMH(maMh);
                monHoc.setSoTC(soTC);
                monHoc.setTenMH(tenMH);
                monHoc.setTinhTrang(false);
                monHoc.setChon(false);
                ds.add(monHoc);
            }
            cursor.close();
            return ds;
        }
    }
}
