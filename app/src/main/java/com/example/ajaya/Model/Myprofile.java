package com.example.ajaya.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Myprofile {
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("pesan")
    @Expose
    private String pesan;
    @SerializedName("nama")
    @Expose
    private String nama;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("gambar")
    @Expose
    private String gambar;
    @SerializedName("hp")
    @Expose
    private String hp;
    @SerializedName("levell")
    @Expose
    private String levell;
    @SerializedName("alamat")
    @Expose
    private String alamat;
    @SerializedName("create_date")
    @Expose
    private String createDate;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getPesan() {
        return pesan;
    }

    public void setPesan(String pesan) {
        this.pesan = pesan;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getHp() {
        return hp;
    }

    public void setHp(String hp) {
        this.hp = hp;
    }

    public String getLevell() {
        return levell;
    }

    public void setLevell(String levell) {
        this.levell = levell;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
