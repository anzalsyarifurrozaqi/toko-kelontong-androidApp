package com.example.toko_kelontong_a11201710423_anzal_syarifur_rozaqi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class dashboard extends AppCompatActivity implements View.OnClickListener {
    private static String TAG = "anzalTAg";
    private CardView stock_barang,list_transaksi, about_me, exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        stock_barang = (CardView) findViewById(R.id.stock_Barang);
        list_transaksi = (CardView) findViewById(R.id.list_transaksi);
        about_me = (CardView) findViewById(R.id.about_me);
        exit = (CardView) findViewById(R.id.exit);


        stock_barang.setOnClickListener(this);
        list_transaksi.setOnClickListener(this);
        about_me.setOnClickListener(this);
        exit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent;

        switch (id) {
            //menuju layout activity stock barang
            case R.id.stock_Barang:
                Log.d(TAG, "Stock Barang");
                intent = new Intent(this, admin_list_barang.class);
                startActivity(intent);
                break;
            //menuju layout activity list transaksi
            case R.id.list_transaksi:
                Log.d(TAG, "list transaksi");
                intent = new Intent(this, list_barang.class);
                startActivity(intent);
                break;
            //menuju layout activity about me
            case R.id.about_me:
                Log.d(TAG, "about me");
                intent = new Intent(this, about_me.class);
                startActivity(intent);
                break;
            //exit
            case R.id.exit:
                Log.d(TAG, "exit");
                finishAffinity();
                System.exit(1);
                break;
        }
    }
}
