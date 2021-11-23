package com.example.doan_mobile.Model;

public class Voucher {
    String MaVoucher, MucGiam, ThoiGianBatDau, ThoiGianKetThuc, GhiChu;

    public Voucher() {

    }

    public Voucher(String maVoucher, String mucGiam, String thoiGianBatDau, String thoiGianKetThuc, String ghiChu) {
        MaVoucher = maVoucher;
        MucGiam = mucGiam;
        ThoiGianBatDau = thoiGianBatDau;
        ThoiGianKetThuc = thoiGianKetThuc;
        GhiChu = ghiChu;
    }

    public String getMaVoucher() {
        return MaVoucher;
    }

    public void setMaVoucher(String maVoucher) {
        MaVoucher = maVoucher;
    }

    public String getMucGiam() {
        return MucGiam;
    }

    public void setMucGiam(String mucGiam) {
        MucGiam = mucGiam;
    }

    public String getThoiGianBatDau() {
        return ThoiGianBatDau;
    }

    public void setThoiGianBatDau(String thoiGianBatDau) {
        ThoiGianBatDau = thoiGianBatDau;
    }

    public String getThoiGianKetThuc() {
        return ThoiGianKetThuc;
    }

    public void setThoiGianKetThuc(String thoiGianKetThuc) {
        ThoiGianKetThuc = thoiGianKetThuc;
    }

    public String getGhiChu() {
        return GhiChu;
    }

    public void setGhiChu(String ghiChu) {
        GhiChu = ghiChu;
    }
}
