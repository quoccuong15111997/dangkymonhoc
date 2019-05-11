package com.example.firebase;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.dangkymonhoc.R;
import com.example.model.BienLai;
import com.example.model.SinhVien;
import com.example.model.ThongBao;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PushDataFirebaseActivity extends AppCompatActivity {
    DatabaseReference mData;
    SQLiteDatabase database=null;
    public static String DATABASE_NAME="DHMH.db";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_data_firebase);
        mData=FirebaseDatabase.getInstance().getReference();
        //pushDataSinhVien();
        //pushDataThongBao();
    }

    private void pushDataThongBao() {
        SimpleDateFormat simpleDateFormat= new SimpleDateFormat("dd/MM/yyyy");
        ThongBao thongBao= new ThongBao("admin","Đóng học phí","Sinh viên đóng học phí đúng ngày",simpleDateFormat.format(Calendar.getInstance().getTime()));
        mData.child("ThongBao").push().setValue(thongBao);
    }

    private void pushDataSinhVien() {
        database=openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE,null);
        Cursor cursor = database.rawQuery("select * from SINHVIEN", null);
        while (cursor.moveToNext()) {
            String maSv = cursor.getString(0);
            String soDt = cursor.getString(1);
            String ten = cursor.getString(3);
            String pass=cursor.getString(2);
            SinhVienFirebase sinhVien= new SinhVienFirebase(maSv,ten,"https://firebasestorage.googleapis.com/v0/b/dangkymonhoc-1b60a.appspot.com/o/ptit.png?alt=media&token=3a15856a-ce02-4350-b471-c6dbca6efcc9");
          FirebaseDatabase.getInstance().getReference().child("SinhVien").push().setValue(sinhVien);
        }
        cursor.close();
    }
}
