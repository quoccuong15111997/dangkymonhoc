package com.example.dangkymonhoc;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.model.SinhVien;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

public class LoginActivity extends AppCompatActivity {
    String DATABASE_NAME= "DHMH.db";
    String DB_PATH_SUFFIX = "/databases/";
    SQLiteDatabase database=null;
    Button btnLogin;
    EditText edtMa, edtPass;
    SinhVien sinhVien;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        processCopy();
        addControls();
        addEvents();
    }

    private void addEvents() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtMa.getText()!=null && edtMa.getText()!=null){
                    initDatabase(edtMa.getText().toString(),edtPass.getText().toString());
                }
                else
                    Toast.makeText(LoginActivity.this,"Vui lòng nhập Mã sinh viên mật khẩu",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addControls() {
        btnLogin=findViewById(R.id.btnLogin);
        edtMa=findViewById(R.id.edtUserName);
        edtPass=findViewById(R.id.edtPassword);
    }

    private void initDatabase(String maSv, String password) {
        sinhVien = new SinhVien();
        database=openOrCreateDatabase("DHMH.db",MODE_PRIVATE,null);
        Cursor cursor=database.rawQuery("select * from SinhVien where MASV like '"+ maSv+"'",null);
        while (cursor.moveToNext())
        {
            String ma=cursor.getString(0);
            String phone=cursor.getString(1);
            String pass=cursor.getString(2);
            String ten=cursor.getString(3);
            sinhVien.setMaSinhVien(ma);
            sinhVien.setTenSinhVien(ten);
            sinhVien.setPassword(pass);
            sinhVien.setPhone(phone);

            if(pass.equals(password)==true){
                Toast.makeText(LoginActivity.this,"Đăng nhập thành công",Toast.LENGTH_LONG).show();
                Intent intent= new Intent(LoginActivity.this,HomeActivity.class);
                intent.putExtra("SV", sinhVien);
                startActivity(intent);
            }
            else
                Toast.makeText(LoginActivity.this,"Sai Mã sinh viên hoặc mật khẩu",Toast.LENGTH_LONG).show();
        }
        cursor.close();

    }

    public void CopyDataBaseFromAsset() {

        try {

            InputStream myInput;
            myInput = getAssets().open(DATABASE_NAME);
            String outFileName = getDatabasePath();
            File f = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if (!f.exists()) f.mkdir();
            OutputStream myOutput = new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {

                myOutput.write(buffer, 0, length);
            }
            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }
    private String getDatabasePath() {
        return getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME;
    }
    private void processCopy() {
        File dbFile = getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists()) {
            try {
                CopyDataBaseFromAsset();
                Toast.makeText(this,
                        "Copying sucess from Assets folder",
                        Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
