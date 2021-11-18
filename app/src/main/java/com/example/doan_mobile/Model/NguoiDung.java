package com.example.doan_mobile.Model;

public class NguoiDung {
    private String HoTen, DienThoai, MatKhau, Avatar, DiaChi;

    public NguoiDung() {

    }

    public NguoiDung(String hoTen, String dienThoai, String matKhau, String avatar, String diaChi) {
        HoTen = hoTen;
        DienThoai = dienThoai;
        MatKhau = matKhau;
        Avatar = avatar;
        DiaChi = diaChi;
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

    public String getDiaChi() {
        return DiaChi;
    }

    public void setDiaChi(String diaChi) {
        DiaChi = diaChi;
    }
}
