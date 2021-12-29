package com.example.ajaya.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.ajaya.DetilProduk;
import com.example.ajaya.MainActivity;
import com.example.ajaya.Model.GetAll;
import com.example.ajaya.NewBacode;
import com.example.ajaya.R;

import java.util.List;

import retrofit2.Callback;

public class All_Products extends RecyclerView.Adapter<All_Products.get_produk> {

    List<GetAll> getAlls;
    Context context;
    float mx,my;


    public All_Products(List<GetAll> getAlls, Context context) {
        this.getAlls = getAlls;
        this.context = context;
    }

    @NonNull
    @Override
    public get_produk onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_barcode, parent, false);
        return new get_produk(view);
    }

    @Override
    public void onBindViewHolder(@NonNull get_produk holder, int position) {
        holder.status.setText(getAlls.get(position).getNamaBarang());
        holder.stok.setText(getAlls.get(position).getStok());
        holder.barcode.setText(getAlls.get(position).getCodeBarcode());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, DetilProduk.class);
                i.putExtra("id_produk", getAlls.get(position).getIdBarcode());
                context.startActivity(i);

            }
        });


        Glide.with(context)
                .load(getAlls.get(position).getImg())
                .placeholder(R.mipmap.ic_launcher_round)
                .into(holder.img);

    }

    @Override
    public int getItemCount() {
            return getAlls.size();
    }

    public class get_produk extends RecyclerView.ViewHolder {
        TextView status,stok,barcode;
        ImageView img;

        public get_produk(@NonNull View itemView) {
            super(itemView);
            img    = itemView.findViewById(R.id.image_produk);
            status = itemView.findViewById(R.id.nama_barang);
            stok   = itemView.findViewById(R.id.stok);
            barcode= itemView.findViewById(R.id.id_barcode);

        }
    }


}
