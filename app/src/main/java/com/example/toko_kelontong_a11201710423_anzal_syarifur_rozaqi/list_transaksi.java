package com.example.toko_kelontong_a11201710423_anzal_syarifur_rozaqi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class list_transaksi extends AppCompatActivity{
    //global variable list_transaksi
    private static final String TAG = "anzalTag";
    private FirebaseAuth auth;
    DatabaseReference databaseReference;
    List<ModelOrder> ordersList = new ArrayList<>();
    RecyclerView recyclerView;
    OrderAdapter orderAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_transaksi);

        //inisiasi dan get id pada layout activity
        recyclerView = (RecyclerView) findViewById(R.id.list_order);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager orderLayout = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(orderLayout);

        //set adapter pada recylerview
        orderAdapter = new OrderAdapter(ordersList, new OrderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ModelOrder order) {
                //menampilkan dialog hapus
                DialogFragment dialog = HapusDialogFragment.newInstance();
                ((HapusDialogFragment) dialog).setOrder(order);

                dialog.show(getSupportFragmentManager(), "tag");
                Log.d(TAG, "clicked");
            }
        });
        recyclerView.setAdapter(orderAdapter);


        //get data user yang login
        auth = FirebaseAuth.getInstance();

        //databaseRefence ke child order dan child user id
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Orders").child(auth.getUid());

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //datasnapshot berisi data order
                //value pada datasnapshot dimasukkan di orderlist
                //vlue berupa ModelOrder
                ModelOrder order = dataSnapshot.getValue(ModelOrder.class);
                ordersList.add(order);
                orderAdapter.notifyDataSetChanged();

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

        //bottomNavigation
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    //menuju daftar barang yang dijual
                    case R.id.ic_dafter_makanan:
                        Intent intent0 = new Intent(list_transaksi.this, list_barang.class);
                        startActivity(intent0);
                        break;

                    //menuju data order barnag
                    case R.id.ic_dafter_pesanan:
                        Intent intent1 = new Intent(list_transaksi.this, list_transaksi.class);
                        startActivity(intent1);
                        break;
                }
                return false;
            }
        });
    }



    public static class HapusDialogFragment extends DialogFragment {
        private ModelOrder order;


        static HapusDialogFragment newInstance()
        {
            return new HapusDialogFragment();
        }

        public void setOrder(ModelOrder order)
        {
            this.order = order;
        }
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final DatabaseReference databaseReference;
            FirebaseAuth auth;

            auth = FirebaseAuth.getInstance();


            databaseReference = FirebaseDatabase.getInstance().getReference().child("Orders").child(auth.getUid());

            // menggunakan builder class
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.hapus)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Log.d(TAG, "yes");
                            Log.d(TAG, order.getIdOrder());
                            databaseReference.child(order.getIdOrder()).getRef().removeValue();
                            Intent intent = new Intent(getContext(), list_transaksi.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Log.d(TAG, "no");
                        }
                    });

            return builder.create();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, dashboard.class);
        startActivity(intent);
    }
}
