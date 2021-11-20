package com.example.doan_mobile.Model;

public class GioHang {
    private String MaSP, TenSP, GiaGoc, SoLuongMua, GiamGia;

    public GioHang() {

    }

    public GioHang(String maSP, String tenSP, String giaGoc, String soLuongMua, String giamGia) {
        MaSP = maSP;
        TenSP = tenSP;
        GiaGoc = giaGoc;
        SoLuongMua = soLuongMua;
        GiamGia = giamGia;
    }

    public String getMaSP() {
        return MaSP;
    }

    public void setMaSP(String maSP) {
        MaSP = maSP;
    }

    public String getTenSP() {
        return TenSP;
    }

    public void setTenSP(String tenSP) {
        TenSP = tenSP;
    }

    public String getGiaGoc() {
        return GiaGoc;
    }

    public void setGiaGoc(String giaGoc) {
        GiaGoc = giaGoc;
    }

    public String getSoLuongMua() {
        return SoLuongMua;
    }

    public void setSoLuongMua(String soLuongMua) {
        SoLuongMua = soLuongMua;
    }

    public String getGiamGia() {
        return GiamGia;
    }

    public void setGiamGia(String giamGia) {
        GiamGia = giamGia;
    }
}
