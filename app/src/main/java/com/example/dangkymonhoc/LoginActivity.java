package com.example.dangkymonhoc;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

    CheckBox chkRemember;
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String USERNAME = "userNameKey";
    public static final String PASS = "passKey";
    public static final String REMEMBER = "remember";
    public static String KEY_NHAN_VIEN="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        processCopy();
        addControls();
        addEvents();
        loadData();
    }

    private void addEvents() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtMa.getText()!=null && edtMa.getText()!=null){
                    if (chkRemember.isChecked())
                        //lưu lại thông tin đăng nhập
                        saveData(edtMa.getText().toString(),edtPass.getText().toString());
                    else
                        clearData();

                    initDatabase(edtMa.getText().toString(),edtPass.getText().toString());
                }
                else
                    Toast.makeText(LoginActivity.this,"Vui lòng nhập Mã sinh viên mật khẩu",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addControls() {
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        btnLogin=findViewById(R.id.btnLogin);
        edtMa=findViewById(R.id.edtUserName);
        edtPass=findViewById(R.id.edtPassword);
        chkRemember= findViewById(R.id.chkRemember);
    }
    private void clearData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    private void saveData(String username, String Pass) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USERNAME, username);
        editor.putString(PASS, Pass);
        editor.putBoolean(REMEMBER,chkRemember.isChecked());
        editor.commit();
    }
    private void loadData() {
        if(sharedPreferences.getBoolean(REMEMBER,false)) {
            edtMa.setText(sharedPreferences.getString(USERNAME, ""));
            edtPass.setText(sharedPreferences.getString(PASS, ""));
            chkRemember.setChecked(true);
        }
        else
            chkRemember.setChecked(false);

    }
    private void initDatabase(String maSv, String password) {
        sinhVien = new SinhVien();
        database=openOrCreateDatabase("DHMH.db",MODE_PRIVATE,null);
        Cursor cursor=database.rawQuery("select * from SinhVien where MASV like '"+ maSv+"'",null);
        if(cursor.getCount()==0){
            AlertDialog builder = new AlertDialog.Builder(LoginActivity.this).setTitle("Sai Mã sinh viên hoặc Mật khẩu").setIcon(R.drawable.ic_error)
                    .setNegativeButton("Thử lại", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
        }
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

                if (pass.equals(password) == true) {
                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    intent.putExtra("SV", sinhVien);
                    startActivity(intent);
                } else
                {
                    AlertDialog builder = new AlertDialog.Builder(LoginActivity.this).setTitle("Sai Mã sinh viên hoặc Mật khẩu").setIcon(R.drawable.ic_error)
                            .setNegativeButton("Thử lại", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                }
            }
        cursor.close();
        database.close();
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
