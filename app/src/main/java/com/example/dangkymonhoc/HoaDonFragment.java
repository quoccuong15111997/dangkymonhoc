package com.example.dangkymonhoc;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.adapter.HoaDonAdapter;
import com.example.model.BienLai;
import com.example.model.MonHoc;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class HoaDonFragment extends Fragment {
    View view;
    ListView lvHoaDon;
    HoaDonAdapter hoaDonAdapter;
    ArrayList<BienLai> dsBienLai;
    SQLiteDatabase database = null;
    public static String DATABASE_NAME = "DHMH.db";
    int soTien;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_hoa_don_hoc_phi, container, false);
        openDatabase();
        addControls();
        addEvents();
        return view;
    }

    private void openDatabase() {
        database = view.getContext().openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
    }

    private void addEvents() {
    }

    private void addControls() {
        soTien = MonHocFragments.tongSoTinChi * 400000;
        lvHoaDon = view.findViewById(R.id.lvHoaDon);
        dsBienLai = new ArrayList<>();
        hoaDonAdapter = new HoaDonAdapter(view.getContext(), R.layout.item_hoa_don, dsBienLai);
        lvHoaDon.setAdapter(hoaDonAdapter);

        LayBienLaiTask task= new LayBienLaiTask();
        task.execute(HomeActivity.sinhVienLogin.getMaSinhVien());
    }

    class LayBienLaiTask extends AsyncTask<String, Void, ArrayList<BienLai>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<BienLai> bienLais) {
            super.onPostExecute(bienLais);
            dsBienLai.addAll(bienLais);
            hoaDonAdapter.notifyDataSetChanged();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected ArrayList<BienLai> doInBackground(String... strings) {
            ArrayList<BienLai> ds = new ArrayList<>();
            Cursor cursorBienLai = database.rawQuery("select * from BIENLAIHOCPHI where MASV like '" + strings[0] + "'", null);
            BienLai bienLai = new BienLai();
            while (cursorBienLai.moveToNext()) {
                int soBL = cursorBienLai.getInt(0);
                String ngayHp = cursorBienLai.getString(1);
                String mSv = cursorBienLai.getString(2);
                bienLai.setMaSV(mSv);
                bienLai.setNgayHP(ngayHp);
                bienLai.setSoBL(soBL);
                bienLai.setSoTien(soTien);
                ds.add(bienLai);
            }
            cursorBienLai.close();
            return ds;
        }
    }
}
