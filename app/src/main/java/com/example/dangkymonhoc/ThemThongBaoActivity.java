package com.example.dangkymonhoc;

import android.content.DialogInterface;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.model.ThongBao;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class ThemThongBaoActivity extends AppCompatActivity {
    EditText edtTile, edtMessege;
    ImageView imgBack, imgSave;
    DatabaseReference mData;
    SimpleDateFormat simpleDateFormat= new SimpleDateFormat("dd/MM/yyyy");
    private String SENDER_ID="591498136370";
    Random random= new Random();
    private int messageId=random.nextInt();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_thong_bao);
        addControls();
        initFirebase();
        addEvenst();

    }

    private void initFirebase() {
        mData= FirebaseDatabase.getInstance().getReference();
    }

    private void addEvenst() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLySave();
            }
        });
    }

    private void xuLySave() {
        if(!edtMessege.getText().equals("") && !edtTile.getText().equals("")){
            ThongBao thongBao= new ThongBao();
            thongBao.setTitle(edtTile.getText().toString());
            thongBao.setMessege(edtMessege.getText().toString());
            thongBao.setUser(HomeActivity.sinhVienLogin.getTenSinhVien());
            thongBao.setDate(simpleDateFormat.format(Calendar.getInstance().getTime()));
            mData.child("ThongBao")
                    .push()
                    .setValue(thongBao, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            if(databaseError==null){
                                android.app.AlertDialog.Builder alertDialog= new android.app.AlertDialog.Builder(ThemThongBaoActivity.this);
                                alertDialog.setTitle("Lưu thành công");
                                alertDialog.setIcon(R.drawable.ic_ok);
                                alertDialog.setNegativeButton("OK",new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).show();
                            }
                        }
                    });
        }
    }

    private void addControls() {
      edtMessege=findViewById(R.id.edtMessege);
      edtTile=findViewById(R.id.edtTitle);
      imgBack=findViewById(R.id.iv_back);
      imgSave=findViewById(R.id.iv_Save);
    }
}
