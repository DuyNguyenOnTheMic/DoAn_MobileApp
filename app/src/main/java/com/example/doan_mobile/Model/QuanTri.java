package com.example.doan_mobile.Model;

public class QuanTri {
    private String HoTen, DienThoai, MatKhau, Avatar, VaiTro;

    public QuanTri() {

    }

    public QuanTri(String hoTen, String dienThoai, String matKhau, String avatar, String vaiTro) {
        HoTen = hoTen;
        DienThoai = dienThoai;
        MatKhau = matKhau;
        Avatar = avatar;
        VaiTro = vaiTro;
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

    public String getVaiTro() {
        return VaiTro;
    }

    public void setVaiTro(String vaiTro) {
        VaiTro = vaiTro;
    }
}
