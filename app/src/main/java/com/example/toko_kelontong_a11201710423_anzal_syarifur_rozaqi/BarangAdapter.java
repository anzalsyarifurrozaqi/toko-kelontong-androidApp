package com.example.toko_kelontong_a11201710423_anzal_syarifur_rozaqi;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BarangAdapter extends ArrayAdapter<ModelBarang> {
    private Activity context;
    //Global variable adapter
    List<ModelBarang> barang;

    public BarangAdapter(Activity context, List<ModelBarang> barang) {
        super(context, R.layout.admin_row_barang, barang);
        this.context = context;
        this.barang = barang;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.admin_row_barang, null, true);

        //inisiasi dan mengambil id dari layout activity
        TextView textViewNama = (TextView) listViewItem.findViewById(R.id.rowNamaBarang);
        TextView textViewJumlah = (TextView) listViewItem.findViewById(R.id.rowJumlahBarang);
        TextView textViewHarga = (TextView) listViewItem.findViewById(R.id.rowHargaBarang);
        TextView textViewSatuan = (TextView) listViewItem.findViewById(R.id.rowSatuanBarang);
        TextView textViewTangalBeli = (TextView) listViewItem.findViewById(R.id.rowTanggalBeli);
        ImageView imageViewGambar = (ImageView) listViewItem.findViewById(R.id.rowGambarBarang);


        //set data barang untuk ditampilkan di listview
        ModelBarang Barang = barang.get(position);
        textViewNama.setText(Barang.getNama_barang());
        textViewJumlah.setText(Integer.valueOf(Barang.getJumlah()).toString());
        textViewHarga.setText(Integer.valueOf(Barang.getHarga()).toString());
        textViewSatuan.setText(Barang.getSatuan());
        textViewTangalBeli.setText(Barang.getTgl_beli());
        Picasso.get().load(Barang.getImg_url()).into(imageViewGambar);
        return listViewItem;
    }
}

