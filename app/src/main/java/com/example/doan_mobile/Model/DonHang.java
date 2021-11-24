package com.example.doan_mobile.Model;

public class DonHang {
    private String DiaChi, Ngay, SDT, TenKH, ThanhPho, ThoiGian, TinhTrang, TongTien;

    public DonHang() {

    }

    public DonHang(String diaChi, String ngay, String SDT, String tenKH, String thanhPho, String thoiGian, String tinhTrang, String tongTien) {
        DiaChi = diaChi;
        Ngay = ngay;
        this.SDT = SDT;
        TenKH = tenKH;
        ThanhPho = thanhPho;
        ThoiGian = thoiGian;
        TinhTrang = tinhTrang;
        TongTien = tongTien;
    }

    public String getDiaChi() {
        return DiaChi;
    }

    public void setDiaChi(String diaChi) {
        DiaChi = diaChi;
    }

    public String getNgay() {
        return Ngay;
    }

    public void setNgay(String ngay) {
        Ngay = ngay;
    }

    public String getSDT() {
        return SDT;
    }

    public void setSDT(String SDT) {
        this.SDT = SDT;
    }

    public String getTenKH() {
        return TenKH;
    }

    public void setTenKH(String tenKH) {
        TenKH = tenKH;
    }

    public String getThanhPho() {
        return ThanhPho;
    }

    public void setThanhPho(String thanhPho) {
        ThanhPho = thanhPho;
    }

    public String getThoiGian() {
        return ThoiGian;
    }

    public void setThoiGian(String thoiGian) {
        ThoiGian = thoiGian;
    }

    public String getTinhTrang() {
        return TinhTrang;
    }

    public void setTinhTrang(String tinhTrang) {
        TinhTrang = tinhTrang;
    }

    public String getTongTien() {
        return TongTien;
    }

    public void setTongTien(String tongTien) {
        TongTien = tongTien;
    }
}
