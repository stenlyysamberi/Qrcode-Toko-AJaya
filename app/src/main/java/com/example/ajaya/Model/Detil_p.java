package com.example.ajaya.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Detil_p {
    @SerializedName("ipesan")
    @Expose
    private String pesan;

    @SerializedName("alert")
    @Expose
    private String alert;

    @SerializedName("id_barcode")
    @Expose
    private String idBarcode;
    @SerializedName("code_barcode")
    @Expose
    private String codeBarcode;
    @SerializedName("nama_barang")
    @Expose
    private String namaBarang;
    @SerializedName("img")
    @Expose
    private String img;
    @SerializedName("stok")
    @Expose
    private String stok;
    @SerializedName("ket_join")
    @Expose
    private String ketJoin;
    @SerializedName("nama_satuan")
    @Expose
    private String namaSatuan;

    @SerializedName("harga")
    @Expose
    private String harga;

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getPesan() {
        return pesan;
    }

    public void setPesan(String pesan) {
        this.pesan = pesan;
    }

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public String getIdBarcode() {
        return idBarcode;
    }

    public void setIdBarcode(String idBarcode) {
        this.idBarcode = idBarcode;
    }

    public String getCodeBarcode() {
        return codeBarcode;
    }

    public void setCodeBarcode(String codeBarcode) {
        this.codeBarcode = codeBarcode;
    }

    public String getNamaBarang() {
        return namaBarang;
    }

    public void setNamaBarang(String namaBarang) {
        this.namaBarang = namaBarang;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getStok() {
        return stok;
    }

    public void setStok(String stok) {
        this.stok = stok;
    }

    public String getKetJoin() {
        return ketJoin;
    }

    public void setKetJoin(String ketJoin) {
        this.ketJoin = ketJoin;
    }

    public String getNamaSatuan() {
        return namaSatuan;
    }

    public void setNamaSatuan(String namaSatuan) {
        this.namaSatuan = namaSatuan;
    }
}
