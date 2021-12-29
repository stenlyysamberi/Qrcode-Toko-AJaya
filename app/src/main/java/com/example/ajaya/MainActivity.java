package com.example.ajaya;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ajaya.Adapter.All_Products;
import com.example.ajaya.Internet.Interfaces;
import com.example.ajaya.Internet.RestClinet;
import com.example.ajaya.Model.GetAll;
import com.example.ajaya.Model.SessionMangger;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    SwipeRefreshLayout refreshLayout;
     SessionMangger sessionMangger;
     ImageView scan;
     RecyclerView product_Recycler;
     RecyclerView.LayoutManager layoutManager;
     All_Products product_Adapter;
     List<GetAll> product_List;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //refreshLayout = findViewById(R.id.swap);





        product_Recycler = findViewById(R.id.recy_products);
        layoutManager    = new LinearLayoutManager(this);
        product_Recycler.setLayoutManager(layoutManager);
        product_Recycler.setHasFixedSize(true);
        TextView uname   = (TextView) findViewById(R.id.nama_title);

        sessionMangger = new SessionMangger(getApplicationContext());
        HashMap<String, String> user = sessionMangger.getUserDetails();
        String get_name_title = user.get(SessionMangger.nama_lengkap);
        uname.setText(get_name_title);



        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        changeStatusBarColor();
        get_all_produt();

        scan = findViewById(R.id.btnScan);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewBacode.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void get_all_produt(){
        try {
            Interfaces cl = RestClinet.getRetrofitInstance().create(Interfaces.class);
            Call<List<GetAll>> getAll = cl.getKon();
            getAll.enqueue(new Callback<List<GetAll>>() {
                @Override
                public void onResponse(Call<List<GetAll>> call, Response<List<GetAll>> response) {
                    List<GetAll> values = response.body();
                    if (response.isSuccessful() && response.body()!=null){
                        //Toast.makeText(getApplicationContext(), "" + values.get(0).getAlert(), Toast.LENGTH_SHORT).show();
                        if(values.get(0).getAlert().equals("true")){
                            product_List = response.body();
                            product_Adapter = new All_Products(product_List,MainActivity.this);
                            product_Recycler.setAdapter(product_Adapter);
                            product_Adapter.notifyDataSetChanged();
                        }else {
                            produk_empty_dialog();
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<GetAll>> call, Throwable t) {
                    Log.e("Retrofit error", String.valueOf(t));
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            System.out.println("Baris ini adalah Finally yang dieksekusi");
        }
        System.out.println("Akhir Program");
    }

    private void produk_empty_dialog() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                MainActivity.this, R.style.bottomSheetDialogTheme
        );

        View bottomSheetView = LayoutInflater.from(getApplicationContext())
                .inflate(
                        R.layout.empty_produk_bottom_sheet, (LinearLayout) findViewById(R.id.barcode_sheet)
                );

        bottomSheetView.findViewById(R.id.btn_add_produk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent i = new Intent(MainActivity.this, NewBacode.class);
               startActivity(i);
               finish();
            }
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }


    private void newBarcode_dialog (){
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                MainActivity.this, R.style.bottomSheetDialogTheme
        );

        View bottomSheetView = LayoutInflater.from(getApplicationContext())
                .inflate(
                        R.layout.barcode_bottom_sheet, (LinearLayout) findViewById(R.id.barcode_sheet)
                );

        bottomSheetView.findViewById(R.id.btn_coba_lagi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TextView barcode = bottomSheetView.findViewById(R.id.newBarode);
                //scancode();
            }
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }


    public void view_profil(View view) {
        Intent i = new Intent(MainActivity.this, MyProfile.class);
        startActivity(i);
        finish();
    }

    public void serach(View view) {
        newBarcode_dialog();
    }
}