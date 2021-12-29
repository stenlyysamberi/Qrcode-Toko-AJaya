package com.example.ajaya;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ajaya.Internet.Interfaces;
import com.example.ajaya.Internet.RestClinet;
import com.example.ajaya.Model.LoginApps;
import com.example.ajaya.Model.SessionMangger;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
    CardView btn_login;
    SessionMangger sessionMangger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionMangger = new SessionMangger(getApplicationContext());
        setContentView(R.layout.activity_login);
        btn_login = findViewById(R.id.btnlogin);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }



   private void login (){
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
          Login.this, R.style.bottomSheetDialogTheme
        );

        View bottomSheetView = LayoutInflater.from(getApplicationContext())
                .inflate(
                  R.layout.layout_bottom_sheet, (LinearLayout) findViewById(R.id.login_sheet)
                );

        bottomSheetView.findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText username = bottomSheetView.findViewById(R.id.username);
                String user = username.getText().toString();

                EditText password = bottomSheetView.findViewById(R.id.password);
                String sandi = password.getText().toString();

                try {

                    if (user.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Please Enter Username", Toast.LENGTH_SHORT).show();
                    }else if( sandi.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Please Enter Password", Toast.LENGTH_SHORT).show();
                    }else{

                        Interfaces x = RestClinet.getRetrofitInstance().create(Interfaces.class);
                        Call<LoginApps> call = x.login(user,sandi);

                        call.enqueue(new Callback<LoginApps>() {
                            @Override
                            public void onResponse(Call<LoginApps> call, Response<LoginApps> response) {
                                if (response.isSuccessful() && response.body()!=null){

                                    String id = response.body().getId();
                                    String name = response.body().getNama();

                                    //Toast.makeText(getApplicationContext(), "" + id + name, Toast.LENGTH_SHORT).show();

                                    if (response.body().getError().equals("Berhasil")){

                                        sessionMangger.create_session(id,name);
                                        Intent intent = new Intent(Login.this, MainActivity.class);
                                        //Toast.makeText(getApplicationContext(), "Login Succesfully", Toast.LENGTH_LONG).show();
                                        startActivity(intent);
                                        finish();
                                    }else{
                                        Toast.makeText(getApplicationContext(), "Failed username or password, Please Try again.", Toast.LENGTH_LONG).show();
                                    }

                                }
                            }

                            @Override
                            public void onFailure(Call<LoginApps> call, Throwable t) {
                                Toast.makeText(Login.this, "Gagal Login Error :" + t, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }catch (Exception e){
                    Log.e("error","Gagal!"+e);
                }


            }
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }


}