package com.example.dangkymonhoc;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.firebase.SinhVienFirebase;
import com.example.model.BienLai;
import com.example.model.MonHoc;
import com.example.model.SinhVien;
import com.example.model.ThongTinHocPhi;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;

public class ChiTietSinhVienActivity extends AppCompatActivity {
    public static String DATABASE_NAME = "DHMH.db";
    ImageView imgBack;
    TextView txtName, txtPhone, txtMa, txtSoTC, txtHocPhi, txtDate;
    AvatarView avatarView;
    IImageLoader iImageLoader;
    SinhVien sinhVien;
    DatabaseReference mData;
    int tongSoTinChi=0;
    SQLiteDatabase database;
    BienLai bienLai;
    LinearLayout llPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_sinh_vien);
        openDatabase();
        addControls();
        initFirebase();
        addEvents();
    }
    private void openDatabase() {

        database=openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE,null);

    }
    private void initFirebase() {
        mData= FirebaseDatabase.getInstance().getReference();
        mData.child("SinhVien").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                SinhVienFirebase sinhVienFirebase=dataSnapshot.getValue(SinhVienFirebase.class);
                if(sinhVienFirebase.getMaSinhVien().equals(sinhVien.getMaSinhVien())){
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

    private void addEvents() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        llPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder= new AlertDialog.Builder(ChiTietSinhVienActivity.this);
                builder.setTitle("Thực hiện").setNegativeButton("Gọi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        xuLyGoiDienThoai();
                    }
                }).setPositiveButton("Nhắn tin", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        xuLyGuiSMS();
                    }
                }).show();

            }
        });
    }

    private void addControls() {
        Intent intent=getIntent();
        sinhVien= (SinhVien) intent.getSerializableExtra("SINHVIEN");

        imgBack=findViewById(R.id.iv_backSinhVien);
        txtName=findViewById(R.id.txtName);
        txtPhone=findViewById(R.id.txtPhone);
        txtMa=findViewById(R.id.txtMaSV);
        txtSoTC=findViewById(R.id.txtSoTC);
        txtHocPhi=findViewById(R.id.txtHocPhi);
        txtDate=findViewById(R.id.txtDate);
        avatarView=findViewById(R.id.avatar_view_example);
        iImageLoader= new PicassoLoader();

        txtName.setText(sinhVien.getTenSinhVien());
        txtMa.setText(sinhVien.getMaSinhVien());
        txtPhone.setText(sinhVien.getPhone());

        LayDanhSachMonHocTheoMaSinhVienTask task= new LayDanhSachMonHocTheoMaSinhVienTask();
        task.execute(sinhVien.getMaSinhVien());

        llPhone=findViewById(R.id.llPhone);

    }
    private void xuLyGuiSMS() {
        String phone=sinhVien.getPhone();
        Uri uri = Uri.parse("smsto:"+phone);
        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
        it.putExtra("sms_body", "Tin nhắn được gửi từ: "+HomeActivity.sinhVienLogin.getTenSinhVien());
        startActivity(it);
    }

    private void xuLyGoiDienThoai() {
        String phone=sinhVien.getPhone();
        Uri uri=Uri.parse("tel:"+phone);
        Intent intent= new Intent(Intent.ACTION_DIAL,uri);
        intent.setData(uri);
        startActivity(intent);
    }
    class LayDanhSachMonHocTheoMaSinhVienTask extends AsyncTask<String,Void,ArrayList<MonHoc>> {
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
            txtSoTC.setText(tongSoTinChi+" Tín Chỉ");
            txtHocPhi.setText(tongSoTinChi*400000+" VNĐ");
            txtDate.setText(bienLai.getNgayHP());
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected ArrayList<MonHoc> doInBackground(String... strings) {
            ArrayList<MonHoc> danhSachMonHoc=new ArrayList<>();
            String maSinhVien=strings[0];
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
