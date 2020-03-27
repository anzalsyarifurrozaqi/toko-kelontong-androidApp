package com.example.toko_kelontong_a11201710423_anzal_syarifur_rozaqi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class list_barang extends AppCompatActivity {
    private static String TAG = "anzalTag";
    //Global variable
    RecyclerView recyclerView;
    List<ModelBarang> barangList = new ArrayList<ModelBarang>();
    DatabaseReference reffBarang;
    BarangAdapter2 barangAdapter2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_barang);

        //Mengambil id dari layout activity
        recyclerView = (RecyclerView) findViewById(R.id.idRecycleView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager myLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(myLayoutManager);

        //Inisiasi object adapter
        barangAdapter2 = new BarangAdapter2(barangList, new BarangAdapter2.OnItemClickListener() {
            @Override
            public void onItemClick(ModelBarang barang) {
                DialogFragment dialog = OrderDialog.newInstance();
                ((OrderDialog) dialog).setBarang(barang);
                ((OrderDialog) dialog).setCallback(new OrderDialog.Callback() {
                    @Override
                    public void onActionClick(String name) {
                        Log.d(TAG, "whatever");
                    }
                });
                dialog.show(getSupportFragmentManager(), "tag");
                Log.d(TAG, "clicked");
            }
        });

        recyclerView.setAdapter(barangAdapter2);

        //Searchview untuk searching data barang
        SearchView searchView = (SearchView) findViewById(R.id.searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, newText);
                barangAdapter2.getFilter().filter(newText);
                return false;
            }
        });

        //Mengambil data barang dari database
        reffBarang = FirebaseDatabase.getInstance().getReference().child("Barang");

        reffBarang.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //BarangId.add(dataSnapshot.getKey());
                ModelBarang barang = dataSnapshot.getValue(ModelBarang.class);
                barangList.add(barang);
                barangAdapter2.setBarangListFull(barangList);
                barangAdapter2.notifyDataSetChanged();

                Log.i(TAG, "snap" + dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Bottom navigation
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    //Menampilkan data barang
                    case R.id.ic_dafter_makanan:
                        Intent intent0 = new Intent(list_barang.this, list_barang.class);
                        startActivity(intent0);
                        break;

                        //Menampilkan data order
                    case R.id.ic_dafter_pesanan:
                        Intent intent1 = new Intent(list_barang.this, list_transaksi.class);
                        startActivity(intent1);
                        break;
                }
                return false;
            }
        });
    }

    public static class OrderDialog extends DialogFragment implements View.OnClickListener{
        //Global viariable pada Class order dialog
        private Callback callback;
        private ModelBarang barang;
        private ImageView gambarBarang;
        private TextView namaBarang, hargaBarang, jmlOrder, jmlstock, totalHarga;
        private Button minButtonOrder, plusButtonOrder, buttonOrder;
        private int jmlOrders = 0;
        private FirebaseAuth auth;

        //interface untuk testing callback
        public interface Callback {
            void onActionClick(String name);
        }

        //static ketika class object Order dialog dipanggil
        static OrderDialog newInstance() {
            return new OrderDialog();
        }

        public void setBarang(ModelBarang barang) {
            this.barang = barang;
        }

        //set Callback
        public void setCallback (Callback callback) {
            this.callback = callback;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setStyle(DialogFragment.STYLE_NORMAL, R.style.OrderDialog);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            //Mengambil id pada layout activity
            View view = inflater.inflate(R.layout.order_barang, container, false);
            gambarBarang = (ImageView) view.findViewById(R.id.gambar_barang);
            namaBarang = (TextView) view.findViewById(R.id.nama_barang);
            hargaBarang = (TextView) view.findViewById(R.id.harga_barang);
            jmlOrder = (TextView) view.findViewById(R.id.jmlOrder);
            jmlstock = (TextView) view.findViewById(R.id.jumlah_stock);
            totalHarga = (TextView) view.findViewById(R.id.total);
            minButtonOrder = (Button) view.findViewById(R.id.minButtonOrder);
            plusButtonOrder = (Button) view.findViewById(R.id.plusButtonOrder);
            buttonOrder = (Button) view.findViewById(R.id.buttonOrder);

            //get data user yang sign in
            auth = FirebaseAuth.getInstance();

            //set data pada layout activity
            Picasso.get().load(barang.getImg_url()).into(gambarBarang);
            namaBarang.setText(barang.getNama_barang());
            hargaBarang.setText("Rp. " + Integer.valueOf(barang.getHarga()).toString());
            jmlOrder.setText(Integer.valueOf(jmlOrders).toString());
            jmlstock.setText(Integer.valueOf(barang.getJumlah()).toString());

            //set click listerner pada button
            minButtonOrder.setOnClickListener(this);
            plusButtonOrder.setOnClickListener(this);
            buttonOrder.setOnClickListener(this);

            return view;
        }

        @Override
        public void onClick(View v) {
            //v.getId() berisi id pada Layout activity
            int id = v.getId();
            int total;

            switch (id) {
                case R.id.minButtonOrder:
                    //minButoon default tidak kurang dari 0
                    //jika jumlah order lebih dari 0 jumlah order berkurang 1
                    //total harga dikurang senilai harga barang
                    Log.d(TAG, "minButton clicked");
                    jmlOrders = jmlOrders - 1;
                    Log.d(TAG, Integer.valueOf(jmlOrders).toString());
                    if (jmlOrders < 0) {
                        jmlOrders = 0;
                    }
                    jmlOrder.setText(Integer.valueOf(jmlOrders).toString());
                    total = jmlOrders * barang.getHarga();
                    totalHarga.setText("Rp. " + Integer.valueOf(total).toString());
                    break;

                case R.id.plusButtonOrder:
                    //plusButoon menambah jumlah order
                    //total harga bertambah senilai harga barang
                    Log.d(TAG, "plusButton clicked");
                    jmlOrders = jmlOrders + 1;
                    Log.d(TAG, Integer.valueOf(jmlOrders).toString());
                    jmlOrder.setText(Integer.valueOf(jmlOrders).toString());
                    total = jmlOrders * barang.getHarga();
                    totalHarga.setText("Rp. " + Integer.valueOf(total).toString());
                    break;

                case R.id.buttonOrder:
                    //buttonOrder mengirim data ke firebase

                    Log.d(TAG, "buttonOrder clicked");

                    //inisiasi ModelOrder
                    ModelOrder order = new ModelOrder();

                    //inisiasi DatabaseReference
                    //DatabaseReference menuju child Orders dan child userId
                    DatabaseReference dataBaseReff = FirebaseDatabase.getInstance().getReference().child("Orders").child(auth.getUid());

                    //insiasi format tanggal
                    //tanggal sesuai tanggal input barang
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-YYYY");
                    Date date = new Date(System.currentTimeMillis());
                    String tgl_beli = formatter.format(date);

                    //set data ke ModelOrder
                    order.setKode_barang(barang.getKode_barang());
                    order.setJumlah_order(jmlOrders);
                    order.setTgl_beli(tgl_beli);

                    String idOrder = dataBaseReff.push().getKey();
                    order.setIdOrder(idOrder);

                    //Mengubah jumlah stock dari Barang senilai jumlah order barang
                    DatabaseReference jumlahBarangReff = FirebaseDatabase.getInstance().getReference().child("Barang").child(barang.getKode_barang()).child("jumlah");
                    int jumlah = barang.getJumlah() - order.getJumlah_order();
                    jumlahBarangReff.setValue(jumlah);

                    Log.d(TAG, order.getIdOrder());
                    Log.d(TAG, order.getKode_barang());
                    Log.d(TAG, Integer.valueOf(order.getJumlah_order()).toString());

                    //Menyimpan Data Order ke Firebase
                    dataBaseReff.child(idOrder).setValue(order);
                    dismiss();
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, dashboard.class);
        startActivity(intent);
    }
}
