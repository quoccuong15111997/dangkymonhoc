package com.example.model;

import java.io.Serializable;

public class ThongTinHocPhi implements Serializable {
    private int soBL;
    private String maMH;
    private int soTien;

    public ThongTinHocPhi(int soBL, String maMH, int soTien) {
        this.soBL = soBL;
        this.maMH = maMH;
        this.soTien = soTien;
    }

    public ThongTinHocPhi() {
    }

    public int getSoBL() {
        return soBL;
    }

    public void setSoBL(int soBL) {
        this.soBL = soBL;
    }

    public String getMaMH() {
        return maMH;
    }

    public void setMaMH(String maMH) {
        this.maMH = maMH;
    }

    public int getSoTien() {
        return soTien;
    }

    public void setSoTien(int soTien) {
        this.soTien = soTien;
    }
}
