package com.example.dangkymonhoc;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.model.ThongBao;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Random;

import cz.msebera.android.httpclient.HttpHeaders;
import cz.msebera.android.httpclient.entity.StringEntity;

public class ThemThongBaoActivity extends AppCompatActivity {
    EditText edtTile, edtMessege;
    ImageView imgBack, imgSave;
    DatabaseReference mData;
    SimpleDateFormat simpleDateFormat= new SimpleDateFormat("dd/MM/yyyy");
    private String SENDER_ID="591498136370";
    Random random= new Random();
    private int messageId=random.nextInt();

    private static final String TAG = "ThemThongBaoActivity";
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
            sendNotification();
        }
    }

    private void sendNotification() {
        try {
            String url = "https://fcm.googleapis.com/fcm/send";
            AsyncHttpClient client = new AsyncHttpClient();

            client.addHeader(HttpHeaders.AUTHORIZATION, "key=AIzaSyBLaZ09LAGUjRScDuwed6nYPYFO1tX0UyU");
            client.addHeader(HttpHeaders.CONTENT_TYPE, RequestParams.APPLICATION_JSON);
            JSONObject params = new JSONObject();

            params.put("to","/topics/ThongBao");

            JSONObject notificationObject = new JSONObject();
            notificationObject.put("body", "New Notification");
            notificationObject.put("title", "PTIT HCM");

            params.put("notification", notificationObject);

            StringEntity entity = new StringEntity(params.toString());

            client.post(getApplicationContext(), url, entity, RequestParams.APPLICATION_JSON, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                    Log.i(TAG, responseString);
                }

                @Override
                public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString) {
                    Log.i(TAG, responseString);
                }
            });
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void addControls() {
      edtMessege=findViewById(R.id.edtMessege);
      edtTile=findViewById(R.id.edtTitle);
      imgBack=findViewById(R.id.iv_back);
      imgSave=findViewById(R.id.iv_Save);
    }

}
