package com.example.ajaya;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ajaya.Internet.Interfaces;
import com.example.ajaya.Internet.RestClinet;
import com.example.ajaya.Model.Detil_p;
import com.example.ajaya.Model.Myprofile;
import com.example.ajaya.Model.ResponAPI;
import com.example.ajaya.Model.SessionMangger;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetilProduk extends AppCompatActivity {
    String id_p;

    Button btn_add_stok;
    TextView status,nama_p,harga_p,barcode_p,stok_p,id_produk;



    //Blok geser image

    float mx,my;
    ImageView imageView;
    private Bitmap bitmap;
    private static final int INTENT_REQUEST_CODE = 777;

    //end


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detil_produk);
        id_p = getIntent().getStringExtra("id_produk");
        btn_add_stok = findViewById(R.id.btn_add_stok);
        imageView = (ImageView) findViewById(R.id.image_produk_detil);

        btn_add_stok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addStok();
            }
        });

       get_produk_id();

    }

    private void addStok(){

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                DetilProduk.this, R.style.bottomSheetDialogTheme
        );

        View bottomSheetView = LayoutInflater.from(getApplicationContext())
                .inflate(
                        R.layout.add_stok_bottom_sheet, (LinearLayout) findViewById(R.id.add_Stok_bottom_sheet)
                );

        bottomSheetView.findViewById(R.id.btn_add_stok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               try {
                   EditText qty = bottomSheetView.findViewById(R.id.jumlah_tamba);
                   String stokBaru = qty.getText().toString();

                   String stokLama = stok_p.getText().toString();
                   String idbarcode = barcode_p.getText().toString();
                   String idproduk = id_produk.getText().toString();
                   if (stokBaru.isEmpty()){
                       LayoutInflater inflater = getLayoutInflater();
                       View layout = inflater.inflate(R.layout.toast_costum,(ViewGroup) findViewById(R.id.toast_root));
                       TextView txt = layout.findViewById(R.id.toast_pesan);
                       ImageView img = layout.findViewById(R.id.img_toast);

                       txt.setText("Please enter quantity");
                       img.setImageResource(R.drawable.ic_baseline_close_24);

                       Toast toast = new Toast(getApplicationContext());
                       toast.setGravity(Gravity.CENTER,0,0);
                       toast.setDuration(Toast.LENGTH_LONG);
                       toast.setView(layout);
                       toast.show();
                   }else{

                       SessionMangger sessionMangger;
                       sessionMangger = new SessionMangger(getApplicationContext());
                       HashMap<String, String> user = sessionMangger.getUserDetails();
                       String iduser = user.get(SessionMangger.kunci_id);


                       Interfaces interfaces = RestClinet.getRetrofitInstance().create(Interfaces.class);
                       Call<ResponAPI> call = interfaces.AddStok(iduser,idproduk,stokLama,stokBaru);


                       call.enqueue(new Callback<ResponAPI>() {
                           @Override
                           public void onResponse(Call<ResponAPI> call, Response<ResponAPI> response) {
                               if (response.isSuccessful() && response.body() != null){
                                   if (response.body().getPesan().equals("Berhasil")){
                                       //Toast.makeText(getApplicationContext(), "TEst" + idbarcode+ " " + stokLama+ " " + stokBaru, Toast.LENGTH_SHORT).show();
                                       Toast.makeText(getApplicationContext(), "Add stok succesfully!", Toast.LENGTH_SHORT).show();
                                       bottomSheetDialog.dismiss();
                                   }else{
                                       Toast.makeText(getApplicationContext(), "Add stok failed!", Toast.LENGTH_SHORT).show();
                                       //bottomSheetDialog.dismiss();
                                   }

                               }
                           }

                           @Override
                           public void onFailure(Call<ResponAPI> call, Throwable t) {

                               Toast.makeText(getApplicationContext(), "" + t, Toast.LENGTH_SHORT).show();

                           }
                       });
                   }
               }catch (Exception e){

               }
            }
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();

    }

    private void geserImage(){
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                float curX, curY;


                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        mx = event.getX();
                        my = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        curX = event.getX();
                        curY = event.getY();
                        imageView.scrollBy((int) (mx - curX), (int) (my - curY));
                        mx = curX;
                        my = curY;
                        break;
                    case MotionEvent.ACTION_UP:
                        curX = event.getX();
                        curY = event.getY();
                        imageView.scrollBy((int) (mx - curX), (int) (my - curY));
                        break;
                }

                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Blok Untuk hendele Gambar
        if (requestCode == INTENT_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                imageView.setImageBitmap(bitmap);
                imageView.setVisibility(View.VISIBLE);
                geserImage();
                return;

            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        //Blok Untuk hendele Gambar
    }

    private void get_produk_id(){

        try {
            Interfaces pr = RestClinet.getRetrofitInstance().create(Interfaces.class);
            Call<Detil_p> mymodel = pr.Detil_product(id_p);
            mymodel.enqueue(new Callback<Detil_p>() {
                @Override
                public void onResponse(Call<Detil_p> call, Response<Detil_p> response) {

                    if (response.isSuccessful() && response.body()!=null){
                        status = (TextView) findViewById(R.id.status_detil);
                        status.setText(response.body().getKetJoin());

                        id_produk = (TextView) findViewById(R.id.id_produk_detil);
                        id_produk.setText(response.body().getIdBarcode());

                        nama_p = (TextView) findViewById(R.id.nama_barang_detil);
                        nama_p.setText(response.body().getNamaBarang());

                         harga_p = (TextView) findViewById(R.id.harga_detil);
                         harga_p.setText(response.body().getHarga());

                         barcode_p = (TextView) findViewById(R.id.id_barcode_detil);
                         barcode_p.setText( response.body().getCodeBarcode());

                         stok_p = (TextView) findViewById(R.id.stok_detil);
                         stok_p.setText(response.body().getStok());



                        Picasso.get()
                                .load(response.body().getImg())
//                                .placeholder(R.drawable.placeholder)
//                                .error(R.drawable.error)
                                .fit()
                                .into(imageView);

                    }else{
                        Log.e("body_error","BodyKosong");
                    }

                }

                @Override
                public void onFailure(Call<Detil_p> call, Throwable t) {

                    Toast.makeText(getApplicationContext(), "error" + t, Toast.LENGTH_SHORT).show();

                }
            });

        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "error"+ e, Toast.LENGTH_SHORT).show();
        }

    }


}