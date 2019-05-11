package com.example.dangkymonhoc;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.example.impl.MonHocOnClickListener;
import com.example.impl.myOnClickListener;

public class DialogMonHoc extends Dialog {
    public DialogMonHoc(Context context, MonHocOnClickListener monHocOnClickListener) {
        super(context);
        this.monHocOnClickListener=monHocOnClickListener;
    }


    MonHocOnClickListener monHocOnClickListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_mon_hoc);

        final EditText edtTen=findViewById(R.id.edtTenMonHoc);
        final EditText edtMa=findViewById(R.id.edtMaMonHoc);
        final EditText edtTC=findViewById(R.id.edtSoTinChi);

        Button btnLuu = (Button) findViewById(R.id.btnLuuMonHoc);
        btnLuu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                monHocOnClickListener.onButtonClick(edtTen.getText().toString(),
                        edtMa.getText().toString(),
                        Integer.parseInt(edtTC.getText().toString()));
                edtMa.setText("");
                edtTC.setText("");
                edtTen.setText("");
            }
        });
        Button btnHuy=findViewById(R.id.btnHuyMonHoc);
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
