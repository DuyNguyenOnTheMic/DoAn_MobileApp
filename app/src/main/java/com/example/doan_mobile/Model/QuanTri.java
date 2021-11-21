package com.example.doan_mobile.Model;

public class QuanTri {
    private String HoTen, DienThoai, MatKhau, Avatar;

    public QuanTri() {

    }

    public QuanTri(String hoTen, String dienThoai, String matKhau, String avatar) {
        HoTen = hoTen;
        DienThoai = dienThoai;
        MatKhau = matKhau;
        Avatar = avatar;
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

    public String getAvatar() {
        return Avatar;
    }

    public void setAvatar(String avatar) {
        Avatar = avatar;
    }
}
