package com.example.model;

import java.io.Serializable;

public class BienLai implements Serializable {
    private int soBL;
    private String ngayHP;
    private String maSV;
    private int soTien;

    public BienLai(int soBL, String ngayHP, String maSV, int soTien) {
        this.soBL = soBL;
        this.ngayHP = ngayHP;
        this.maSV = maSV;
        this.soTien = soTien;
    }

    public BienLai() {
    }

    public int getSoBL() {
        return soBL;
    }

    public void setSoBL(int soBL) {
        this.soBL = soBL;
    }

    public String getNgayHP() {
        return ngayHP;
    }

    public void setNgayHP(String ngayHP) {
        this.ngayHP = ngayHP;
    }

    public String getMaSV() {
        return maSV;
    }

    public void setMaSV(String maSV) {
        this.maSV = maSV;
    }

    public int getSoTien() {
        return soTien;
    }

    public void setSoTien(int soTien) {
        this.soTien = soTien;
    }
}
