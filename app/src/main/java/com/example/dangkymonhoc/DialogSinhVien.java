package com.example.dangkymonhoc;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.example.impl.myOnClickListener;

public class DialogSinhVien extends Dialog {
    public DialogSinhVien(Context context, myOnClickListener myclick) {
        super(context);
        this.myListener = myclick;
    }


    myOnClickListener myListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_sinh_vien);

        final EditText edtTen=findViewById(R.id.edtTenSinhVien);
        final EditText edtMa=findViewById(R.id.edtMaSinhVien);
        final EditText edtPhone=findViewById(R.id.edtPhone);
        final EditText edtPass=findViewById(R.id.edtPassword);

        Button btnLuu = (Button) findViewById(R.id.btnLuuSinhVien);
        btnLuu.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                myListener.onButtonClick(edtTen.getText().toString(),edtMa.getText().toString(),edtPhone.getText().toString(),edtPass.getText().toString());
                edtMa.setText("");
                edtPass.setText("");
                edtPhone.setText("");
                edtTen.setText("");
            }
        });
        Button btnHuy=findViewById(R.id.btnHuySinhVien);
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
