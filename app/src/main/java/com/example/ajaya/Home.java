package com.example.ajaya;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.ajaya.Internet.Interfaces;
import com.example.ajaya.Internet.RestClinet;
import com.example.ajaya.Model.GetAll;
import com.example.ajaya.Model.SessionMangger;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Home extends AppCompatActivity {
    SessionMangger sessionMangger;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        changeStatusBarColor();
        getKoneksi();
    }

    public void view_profil(View view) {
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public void getKoneksi() {
        try {

            Interfaces apiInterface = RestClinet.getRetrofitInstance().create(Interfaces.class);
            Call<List<GetAll>> call = apiInterface.getKon();
            call.enqueue(new Callback<List<GetAll>>() {
                @Override
                public void onResponse(Call<List<GetAll>> call, Response<List<GetAll>> response) {

                    GetAll value = new GetAll();

                    if (response.isSuccessful() && response.body() !=null){

                        sessionMangger = new SessionMangger(getApplicationContext());
                        if (!sessionMangger.is_login()){
                            Intent intent = new Intent(Home.this, Login.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Intent intent = new Intent(Home.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }



                    }else{
                        Toast.makeText(getApplicationContext(), "Koneksi Anda terputus, Silakan coba lagi" , Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<GetAll>> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Koneksi Anda terputus, Silakan coba lagi" , Toast.LENGTH_SHORT).show();
                    Log.e("Kesalahan", String.valueOf(t.fillInStackTrace()));

                }
            });



        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("Baris ini adalam finnaly yang dieksekusi");
        }
        System.out.println("Akhir Program");
    }
}