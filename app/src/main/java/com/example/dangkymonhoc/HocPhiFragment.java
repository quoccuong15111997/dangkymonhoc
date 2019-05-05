package com.example.dangkymonhoc;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.adapter.HocPhiAdapter;
import com.example.model.MonHoc;

import java.util.ArrayList;

public class HocPhiFragment extends Fragment {
    View view;
    TextView txtTongSoTien;
    ListView lvMonHoc;
    HocPhiAdapter hocPhiAdapter;
    ArrayList<MonHoc> dsMonHoc;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_hocphi, container, false);
        addControls();
        addEvents();
        return view;
    }

    private void addEvents() {
    }

    private void addControls() {
        dsMonHoc= new ArrayList<>();
        dsMonHoc.addAll(MonHocFragments.dsMonHoc);
        txtTongSoTien=view.findViewById(R.id.txtTongSoTien);
        lvMonHoc=view.findViewById(R.id.lvMonHoc);
        hocPhiAdapter= new HocPhiAdapter(view.getContext(),R.layout.item_hoc_phi,dsMonHoc);
        lvMonHoc.setAdapter(hocPhiAdapter);
        int soTien=MonHocFragments.tongSoTinChi*400000;
        txtTongSoTien.setText(soTien+" VNĐ");
    }
}
