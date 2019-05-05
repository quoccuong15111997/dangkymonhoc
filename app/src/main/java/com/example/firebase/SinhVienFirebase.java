package com.example.firebase;

import java.io.Serializable;

public class SinhVienFirebase implements Serializable {
    private String maSinhVien;
    private String tenSinhVien;
    private String urlImage;

    public SinhVienFirebase(String maSinhVien, String tenSinhVien, String urlImage) {
        this.maSinhVien = maSinhVien;
        this.tenSinhVien = tenSinhVien;
        this.urlImage = urlImage;
    }

    public SinhVienFirebase() {
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

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
}
