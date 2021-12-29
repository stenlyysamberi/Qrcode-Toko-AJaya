package com.example.ajaya;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ajaya.Internet.Interfaces;
import com.example.ajaya.Internet.RestClinet;
import com.example.ajaya.Model.MySatuan;
import com.example.ajaya.Model.ResponAPI;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewBacode extends AppCompatActivity {
    TextView getBarcode;
    ImageView fotop;
    TextView path;
    Button simpan_produk_btn;
    ProgressBar progressBar;
    Spinner satuan;
    Context mContext;

    float mx,my;
    boolean idSatuan;

    private Bitmap bitmap;
    private static final int INTENT_REQUEST_CODE = 777;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_bacode);
        getBarcode = (TextView)findViewById(R.id.idBarcode);
        fotop      = (ImageView) findViewById(R.id.foto_produk);
        path       = (TextView)findViewById(R.id.path);
        simpan_produk_btn = (Button) findViewById(R.id.btn_simpan_product);
        satuan     = (Spinner) findViewById(R.id.satuan);
        TextView titile = (TextView)findViewById(R.id.title_id);
        titile.setText("Back");


        mContext = this;

        titile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent back = new Intent(NewBacode.this,MainActivity.class);
                startActivity(back);
                finish();
            }
        });

        getMysatuan();




        getBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scancode();

            }
        });

        simpan_produk_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                method_simpan_product();
            }
        });
    }

    public void open_galery(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, INTENT_REQUEST_CODE);

    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void geserImage(){
        fotop.setOnTouchListener(new View.OnTouchListener() {
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
                        fotop.scrollBy((int) (mx - curX), (int) (my - curY));
                        mx = curX;
                        my = curY;
                        break;
                    case MotionEvent.ACTION_UP:
                        curX = event.getX();
                        curY = event.getY();
                        fotop.scrollBy((int) (mx - curX), (int) (my - curY));
                        break;
                }

                return true;
            }
        });
    }

    private String imageToString() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imagebyte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imagebyte, Base64.DEFAULT);
    }

    private void scancode(){
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setCaptureActivity(CaptureAct.class);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        intentIntegrator.setPrompt("Toko Anugrah Jaya - Papua");
        intentIntegrator.initiateScan();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode, data);
        if(result != null){
            if (result.getContents() != null){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(result.getContents());
                builder.setTitle("Barcode");
                builder.setPositiveButton("Coba lagi...!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        scancode();
                    }
                }).setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        getBarcode.setText(result.getContents());

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }else{
                Toast.makeText(this, "No Result", Toast.LENGTH_SHORT).show();
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }


        //Blok Untuk hendele Gambar
        if (requestCode == INTENT_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                fotop.setImageBitmap(bitmap);
                fotop.setVisibility(View.VISIBLE);
                geserImage();
                return;

            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        //Blok Untuk hendele Gambar
    }

    void method_simpan_product(){
        try {

            TextView idbarcode = (TextView) findViewById(R.id.idBarcode);
            EditText name      = (EditText) findViewById(R.id.produk_name);
            Spinner jenis_brg = (Spinner) findViewById(R.id.satuan);

            String code     = idbarcode.getText().toString();
            String nama     = name.getText().toString();
            String satuan   = jenis_brg.getSelectedItem().toString();


            if (code.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please Enter Text ID Barcode", Toast.LENGTH_SHORT).show();

            }else if(nama.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please Enter Products name", Toast.LENGTH_SHORT).show();

            }else if(satuan.isEmpty()){
                Toast.makeText(getApplicationContext(), "Please Enter Satuan", Toast.LENGTH_SHORT).show();
            }else{
                progressBar  = (ProgressBar) findViewById(R.id.progress);
                progressBar.setVisibility(View.VISIBLE);
                String img = imageToString();
                Interfaces post = RestClinet.getRetrofitInstance().create(Interfaces.class);
                Call<ResponAPI> RestAPI = post.new_product("47",code,img,nama,satuan);
                RestAPI.enqueue(new Callback<ResponAPI>() {
                    @Override
                    public void onResponse(Call<ResponAPI> call, Response<ResponAPI> response) {
                        if (response.isSuccessful() && response.body() !=null){
                            progressBar.setVisibility(View.INVISIBLE);
                            if (response.body().getPesan().equals("2")){
                                Toast.makeText(getApplicationContext(), "new product created!", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(NewBacode.this,MainActivity.class);
                                startActivity(i);
                            }else if(response.body().getPesan().equals("1")){
                                Toast.makeText(getApplicationContext(), "product is register, try again", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);
                            }else{
                                Toast.makeText(getApplicationContext(), "product created failed", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);
                            }

                        }else{
                            Toast.makeText(getApplicationContext(), "" + response.body().getPesan(), Toast.LENGTH_SHORT);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponAPI> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Failed Connect to server, Try again", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
            }


        }catch (Exception e){
            Log.e("Post Product", "error" + e);
        }
    }

    void getMysatuan(){
        try {
            Interfaces mySatuan = RestClinet.getRetrofitInstance().create(Interfaces.class);
            Call<List<MySatuan>> my = mySatuan.getMySatuan();
            my.enqueue(new Callback<List<MySatuan>>() {

                @Override
                public void onResponse(Call<List<MySatuan>> call, Response<List<MySatuan>> response) {
                    List<MySatuan> value = response.body();
                    if (response.isSuccessful() && response.body() !=null){

                        List<String> list = new ArrayList<String>();
                        for (int i = 0; i<value.size(); i++){
                            list.add(value.get(i).getIdSatuan() + "." + value.get(i).getNamaSatuan());

                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item,list);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        satuan.setAdapter(adapter);


                    }else{
                        Toast.makeText(getApplicationContext(), "Respon Body Kosong",  Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<MySatuan>> call, Throwable t) {

                }
            });

        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "MysatuanError",  Toast.LENGTH_SHORT).show();
        }
    }

}