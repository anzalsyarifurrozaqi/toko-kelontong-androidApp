package com.example.toko_kelontong_a11201710423_anzal_syarifur_rozaqi;


import java.io.Serializable;

public class ModelBarang implements Serializable {
    private String kode_barang;
    private String nama_barang;
    private String satuan;
    private int jumlah;
    private String tgl_beli;
    private int harga;
    private String img_url;

    public void setKode_barang(String kode_barang) {
        this.kode_barang = kode_barang;
    }

    public String getKode_barang() {
        return kode_barang;
    }

    public void setNama_barang(String nama_barang) {
        this.nama_barang = nama_barang;
    }

    public String getNama_barang() {
        return nama_barang;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setTgl_beli(String tgl_beli) {
        this.tgl_beli = tgl_beli;
    }

    public String getTgl_beli() {
        return tgl_beli;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public int getHarga() {
        return harga;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getImg_url() {
        return img_url;
    }
}
