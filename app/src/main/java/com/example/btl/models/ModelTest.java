package com.example.btl.models;

public class ModelTest {
    String id, TenHang, MoTa, soluong,dongia;

    public ModelTest() {
    }

    public ModelTest(String id, String name, String uid, String soluong, String dongia) {
        this.id = id;
        this.TenHang = TenHang;
        this.MoTa = MoTa;
        this.soluong = soluong;
        this.dongia = dongia;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return TenHang;
    }

    public void setName(String name) {
        this.TenHang = name;
    }

    public String getMota() {
        return MoTa;
    }

    public void setMoTa(String moTa) {
        MoTa = moTa;
    }

    public String getSoluong() {
        return soluong;
    }
    public void setSoluong(String soluong){
        this.soluong=soluong;
    }

    public String getDongia() {
        return dongia;
    }
    public void setDongia(String dongia){
        this.dongia=dongia;
    }
}
