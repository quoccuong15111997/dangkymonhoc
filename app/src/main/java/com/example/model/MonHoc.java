package com.example.model;

import java.io.Serializable;

public class MonHoc implements Serializable {
    private String maMH;
    private String TenMH;
    private int soTC;

    public MonHoc(String maMH, String tenMH, int soTC) {
        this.maMH = maMH;
        TenMH = tenMH;
        this.soTC = soTC;
    }

    public MonHoc() {
    }

    public String getMaMH() {
        return maMH;
    }

    public void setMaMH(String maMH) {
        this.maMH = maMH;
    }

    public String getTenMH() {
        return TenMH;
    }

    public void setTenMH(String tenMH) {
        TenMH = tenMH;
    }

    public int getSoTC() {
        return soTC;
    }

    public void setSoTC(int soTC) {
        this.soTC = soTC;
    }

    @Override
    public String toString() {
        return TenMH;
    }
}
