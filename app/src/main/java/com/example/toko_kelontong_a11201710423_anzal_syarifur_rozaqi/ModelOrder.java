package com.example.toko_kelontong_a11201710423_anzal_syarifur_rozaqi;

public class ModelOrder {
    private String idOrder;
    private String kode_barang;
    private int jumlah_order;
    private String tgl_beli;

    public void setIdOrder(String idOrder) {
        this.idOrder = idOrder;
    }

    public String getIdOrder() {
        return idOrder;
    }


    public void setKode_barang(String kode_barang) {
        this.kode_barang = kode_barang;
    }

    public String getKode_barang() {
        return kode_barang;
    }

    public void setJumlah_order(int jumlah_order) {
        this.jumlah_order = jumlah_order;
    }

    public int getJumlah_order() {
        return jumlah_order;
    }

    public void setTgl_beli(String tgl_beli) {
        this.tgl_beli = tgl_beli;
    }

    public String getTgl_beli() {
        return tgl_beli;
    }
}
