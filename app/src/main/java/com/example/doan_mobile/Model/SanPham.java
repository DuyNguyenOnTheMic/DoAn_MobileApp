package com.example.doan_mobile.Model;

public class SanPham {
    String ID, ThongTinChiTietSP, GiaGoc, SoLuongTonKho, HinhAnhSP, TenSP, TenHangSP;

    public  SanPham() {

    }

    public SanPham(String ID, String thongTinChiTietSP, String giaGoc, String soLuongTonKho, String hinhAnhSP, String tenSP, String tenHangSP) {
        this.ID = ID;
        ThongTinChiTietSP = thongTinChiTietSP;
        GiaGoc = giaGoc;
        SoLuongTonKho = soLuongTonKho;
        HinhAnhSP = hinhAnhSP;
        TenSP = tenSP;
        TenHangSP = tenHangSP;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getThongTinChiTietSP() {
        return ThongTinChiTietSP;
    }

    public void setThongTinChiTietSP(String thongTinChiTietSP) {
        ThongTinChiTietSP = thongTinChiTietSP;
    }

    public String getGiaGoc() {
        return GiaGoc;
    }

    public void setGiaGoc(String giaGoc) {
        GiaGoc = giaGoc;
    }

    public String getSoLuongTonKho() {
        return SoLuongTonKho;
    }

    public void setSoLuongTonKho(String soLuongTonKho) {
        SoLuongTonKho = soLuongTonKho;
    }

    public String getHinhAnhSP() {
        return HinhAnhSP;
    }

    public void setHinhAnhSP(String hinhAnhSP) {
        HinhAnhSP = hinhAnhSP;
    }

    public String getTenSP() {
        return TenSP;
    }

    public void setTenSP(String tenSP) {
        TenSP = tenSP;
    }

    public String getTenHangSP() {
        return TenHangSP;
    }

    public void setTenHangSP(String tenHangSP) {
        TenHangSP = tenHangSP;
    }
}
