package com.example.doan_mobile.Model;

public class NguoiDung {
    private String HoTen, DienThoai, MatKhau;

    public NguoiDung() {

    }

    public NguoiDung(String hoTen, String dienThoai, String matKhau) {
        HoTen = hoTen;
        DienThoai = dienThoai;
        MatKhau = matKhau;
    }

    public String getHoTen() {
        return HoTen;
    }

    public void setHoTen(String hoTen) {
        HoTen = hoTen;
    }

    public String getDienThoai() {
        return DienThoai;
    }

    public void setDienThoai(String dienThoai) {
        DienThoai = dienThoai;
    }

    public String getMatKhau() {
        return MatKhau;
    }

    public void setMatKhau(String matKhau) {
        MatKhau = matKhau;
    }
}
