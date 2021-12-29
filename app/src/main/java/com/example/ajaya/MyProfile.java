package com.example.ajaya;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ajaya.Internet.Interfaces;
import com.example.ajaya.Internet.RestClinet;
import com.example.ajaya.Model.Myprofile;
import com.example.ajaya.Model.ResponAPI;
import com.example.ajaya.Model.SessionMangger;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyProfile extends AppCompatActivity {
    SessionMangger sessionMangger;
    String id;
    Context context;
    ImageView alert_image;
    TextView alert_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);


        sessionMangger = new SessionMangger(getApplicationContext());
        HashMap<String, String> user = sessionMangger.getUserDetails();
        id = user.get(SessionMangger.kunci_id);

        TextView title = (TextView) findViewById(R.id.title_id);
        title.setText("My Profile");

        getProfile();
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MyProfile.this, MainActivity.class);
                startActivity(i);
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

    public void close(View view) {

        sessionMangger.logout();
        finish();
    }

    private void getProfile(){
        try {
            Interfaces pr = RestClinet.getRetrofitInstance().create(Interfaces.class);

            Call<Myprofile> mymodel = pr.myProfile(id);
            mymodel.enqueue(new Callback<Myprofile>() {
                @Override
                public void onResponse(Call<Myprofile> call, Response<Myprofile> response) {

                    if (response.isSuccessful() && response.body()!=null){
                        TextView nm = (TextView) findViewById(R.id.nama_lengkap_profil_saya);
                        nm.setText(response.body().getNama());

                        TextView alamat = (TextView) findViewById(R.id.alamat_rumah);
                        alamat.setText(response.body().getAlamat());

                        ImageView imageView = (ImageView) findViewById(R.id.image_profil_saya);

                        Picasso.get()
                                .load(response.body().getGambar())
//                                .placeholder(R.drawable.placeholder)
//                                .error(R.drawable.error)
                                .fit()
                                .into(imageView);

                    }else{
                        Log.e("body_error","BodyKosong");
                    }

                }

                @Override
                public void onFailure(Call<Myprofile> call, Throwable t) {

                }
            });

        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "error"+ e, Toast.LENGTH_SHORT).show();
        }

    }

    public void kirimMasukan(View view) {

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"stenly815@gmail.com"});
            intent.putExtra(Intent.EXTRA_CC, new String[] {"stenly815@@gmail.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "Kirim masukan ke @Developer");
            intent.putExtra(Intent.EXTRA_TEXT, "Hai, Team akupa.id");

            try {
                startActivity(Intent.createChooser(intent, "Ingin Mengirim Email ?"));
            } catch (android.content.ActivityNotFoundException ex) {
                //do something else
            }

    }

    public void berbagi_dengan_teman(View view) {

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"sacode@gmail.com"});
            intent.putExtra(Intent.EXTRA_CC, new String[] {"sacode@gmail.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "Kirim masukan ke @Developer");
            intent.putExtra(Intent.EXTRA_TEXT, "Hai, *Toko Anugrah Jaya - Papua*");

            try {
                startActivity(Intent.createChooser(intent, "Berbagi dengan teman!"));
            } catch (android.content.ActivityNotFoundException ex) {
                //do something else
            }

    }

    public void edit_phone(View view) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                MyProfile.this, R.style.bottomSheetDialogTheme
        );

        View bottomSheetView = LayoutInflater.from(getApplicationContext())
                .inflate(
                        R.layout.layout_bottom_sheet_edit_phone, (LinearLayout) findViewById(R.id.edit_phone_she)
                );

        bottomSheetView.findViewById(R.id.btn_edit_phone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText phone = bottomSheetView.findViewById(R.id.new_phone);
                String getPhone = phone.getText().toString();

                if (getPhone.isEmpty()){
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.toast_costum,(ViewGroup) findViewById(R.id.toast_root));
                    TextView txt = layout.findViewById(R.id.toast_pesan);
                    ImageView img = layout.findViewById(R.id.img_toast);

                    txt.setText("Please enter phone number");
                    img.setImageResource(R.drawable.ic_baseline_close_24);

                    Toast toast = new Toast(getApplicationContext());
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();

                }else{
                    try {

                        Interfaces interfaces = RestClinet.getRetrofitInstance().create(Interfaces.class);
                        Call<ResponAPI> call = interfaces.changePhone(id,getPhone);
                        call.enqueue(new Callback<ResponAPI>() {
                            @Override
                            public void onResponse(Call<ResponAPI> call, Response<ResponAPI> response) {
                                if (response.isSuccessful() && response.body() != null){
                                    if (response.body().getPesan().equals("Berhasil")){
                                        Toast.makeText(getApplicationContext(), "Phone Number Updated", Toast.LENGTH_SHORT).show();
                                        bottomSheetDialog.dismiss();
                                    }else{
                                        Toast.makeText(getApplicationContext(), "Phone Number failed Updated", Toast.LENGTH_SHORT).show();
                                        bottomSheetDialog.dismiss();
                                    }

                                }
                            }

                            @Override
                            public void onFailure(Call<ResponAPI> call, Throwable t) {

                            }
                        });


                    }catch (Exception e){
                        Log.e("errorPhone", String.valueOf(e));
                    }
                }

            }
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();

    }

    private void pesan(){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_costum,(ViewGroup) findViewById(R.id.toast_root));
        TextView txt = layout.findViewById(R.id.toast_pesan);
        ImageView img = layout.findViewById(R.id.img_toast);

        txt.setText("Please enter new password");
        img.setImageResource(R.drawable.ic_baseline_close_24);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER,0,0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    public void ubah_sandi(View view) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                MyProfile.this, R.style.bottomSheetDialogTheme
        );

        View bottomSheetView = LayoutInflater.from(getApplicationContext())
                .inflate(
                        R.layout.layout_bottom_sheet_edit_password, (LinearLayout) findViewById(R.id.edit_password_she)
                );

        bottomSheetView.findViewById(R.id.btn_edit_pass).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText phone = bottomSheetView.findViewById(R.id.new_pass);
                String getpss = phone.getText().toString();

                if (getpss.isEmpty()){
                    pesan();

                }else{
                    try {

                        Interfaces interfaces = RestClinet.getRetrofitInstance().create(Interfaces.class);
                        Call<ResponAPI> call = interfaces.changePass(id,getpss);
                        call.enqueue(new Callback<ResponAPI>() {
                            @Override
                            public void onResponse(Call<ResponAPI> call, Response<ResponAPI> response) {
                                if (response.isSuccessful() && response.body() != null){
                                    if (response.body().getPesan().equals("Berhasil")){
                                        Toast.makeText(getApplicationContext(), "Password Updated", Toast.LENGTH_SHORT).show();
                                        bottomSheetDialog.dismiss();
                                    }else{
                                        Toast.makeText(getApplicationContext(), "Password failed Updated", Toast.LENGTH_SHORT).show();
                                        bottomSheetDialog.dismiss();
                                    }

                                }
                            }

                            @Override
                            public void onFailure(Call<ResponAPI> call, Throwable t) {

                            }
                        });


                    }catch (Exception e){
                        Log.e("errorPhone", String.valueOf(e));
                    }
                }

            }
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }
}