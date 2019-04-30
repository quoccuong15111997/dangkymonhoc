package com.example.model;

import java.io.Serializable;

public class SinhVien implements Serializable {
    private String maSinhVien;
    private String tenSinhVien;
    private String phone;
    private String password;

    public SinhVien(String maSinhVien, String tenSinhVien, String phone, String password) {
        this.maSinhVien = maSinhVien;
        this.tenSinhVien = tenSinhVien;
        this.phone = phone;
        this.password = password;
    }

    public SinhVien() {
    }

    public String getMaSinhVien() {
        return maSinhVien;
    }

    public void setMaSinhVien(String maSinhVien) {
        this.maSinhVien = maSinhVien;
    }

    public String getTenSinhVien() {
        return tenSinhVien;
    }

    public void setTenSinhVien(String tenSinhVien) {
        this.tenSinhVien = tenSinhVien;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
