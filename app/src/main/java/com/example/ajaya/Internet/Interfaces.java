package com.example.ajaya.Internet;

import com.example.ajaya.Model.Detil_p;
import com.example.ajaya.Model.GetAll;
import com.example.ajaya.Model.LoginApps;
import com.example.ajaya.Model.MySatuan;
import com.example.ajaya.Model.Myprofile;
import com.example.ajaya.Model.ResponAPI;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Interfaces {
    @GET("get_all.php")
    Call<List<GetAll>> getKon();

    @GET("MySatuan.php")
    Call<List<MySatuan>> getMySatuan();


    @FormUrlEncoded
    @POST("login_app.php")
    Call<LoginApps> login(
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("new_product.php")
    Call<ResponAPI> new_product(
            @Field("id") String id,
            @Field("barcode") String Barcode,
            @Field("foto") String foto,
            @Field("nama") String nama_product,
            @Field("satuan") String satuan_produk

    );

    @FormUrlEncoded
    @POST("Myprofile.php")
    Call<Myprofile> myProfile(
            @Field("id") String id
    );


    @FormUrlEncoded
    @POST("MyprofilSet.php")
    Call<ResponAPI>changePhone(
        @Field("id") String id,
        @Field("phone") String phone
    );

    @FormUrlEncoded
    @POST("MyprofilSet.php")
    Call<ResponAPI>changePass(
            @Field("id") String id,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("Detil_p.php")
    Call<Detil_p>Detil_product(
            @Field("id") String id
    );


    @FormUrlEncoded
    @POST("addStok.php")
    Call<ResponAPI>AddStok(
            @Field("id_user") String id_user,
            @Field("idbarcode") String idBarcode,
            @Field("stokLama") String stokLama,
            @Field("stokBaru") String stokBaru
    );




}


