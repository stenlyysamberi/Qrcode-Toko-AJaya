package com.example.ajaya.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MySatuan {

    @SerializedName("pesan")
    @Expose
    private String pesan;
    @SerializedName("alert")
    @Expose
    private String alert;
    @SerializedName("id_satuan")
    @Expose
    private String idSatuan;
    @SerializedName("nama_satuan")
    @Expose
    private String namaSatuan;

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

    public String getIdSatuan() {
        return idSatuan;
    }

    public void setIdSatuan(String idSatuan) {
        this.idSatuan = idSatuan;
    }

    public String getNamaSatuan() {
        return namaSatuan;
    }

    public void setNamaSatuan(String namaSatuan) {
        this.namaSatuan = namaSatuan;
    }
}
