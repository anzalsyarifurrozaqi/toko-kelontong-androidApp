package com.example.toko_kelontong_a11201710423_anzal_syarifur_rozaqi;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {


    //interface saat klik barang
    public interface OnItemClickListener {
        void onItemClick(ModelOrder order);
    }


    //Global variable orderAdapter
    private static String TAG="anzalTag";
    List<ModelOrder> ordersList;
    ModelBarang barang;
    private final OnItemClickListener listener;

    //inisiasi orderlist ketika class OrderAdapter dipanggil
    public OrderAdapter(List<ModelOrder> orderList, OnItemClickListener listener) {
        this.ordersList = orderList;
        this.listener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_order, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(ordersList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        //global variabel class ViewHolder
        TextView nama_barang;
        TextView jumlah_barang;
        TextView harga_barang;
        TextView tgl_beli;

        DatabaseReference databaseReference;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nama_barang = itemView.findViewById(R.id.orderNama_barang);
            jumlah_barang = itemView.findViewById(R.id.orderJumlah_barang);
            harga_barang = itemView.findViewById(R.id.orderHarga_barang);
            tgl_beli = itemView.findViewById(R.id.orderTgl_beli_barang);
        }

        public void bind(final ModelOrder order, final OnItemClickListener listener) {
            Log.d(TAG, "bind");
            Log.d(TAG, order.getKode_barang());
            final ModelBarang barang;
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Barang");

//            databaseReference.orderByKey().equalTo(order.getKode_barang())
//                    .addChildEventListener(new ChildEventListener() {
//                        @Override
//                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                            Log.d(TAG, dataSnapshot.toString());
//                            ModelBarang barang = dataSnapshot.getValue(ModelBarang.class);
//                            nama_barang.setText(barang.getNama_barang());
//                            jumlah_barang.setText(Integer.valueOf(order.getJumlah_order()).toString());
//                            harga_barang.setText(Integer.valueOf(order.getJumlah_order() * barang.getHarga()).toString());
//                            tgl_beli.setText(order.getTgl_beli());
//                        }
//
//                        @Override
//                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                        }
//
//                        @Override
//                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//                        }
//
//                        @Override
//                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });

            databaseReference.orderByKey().equalTo(order.getKode_barang())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            //data snapshot barupa barang yang kode barang sama seperti kode barang di orderlist
                            //jika barang tidak null barang ditamoilkn di cardview pada layout daftar order
                            //jika barang null order akan otomatis terhapus
                            if (dataSnapshot.getValue() != null)
                            {
                                Log.d(TAG, dataSnapshot.getValue(ModelBarang.class).toString());
                                ModelBarang barang = dataSnapshot.getChildren().iterator().next().getValue(ModelBarang.class);

                                nama_barang.setText(barang.getNama_barang());
                                jumlah_barang.setText(Integer.valueOf(order.getJumlah_order()).toString());
                                harga_barang.setText(Integer.valueOf(order.getJumlah_order() * barang.getHarga()).toString());
                                tgl_beli.setText(order.getTgl_beli());
                            }
                            else
                            {
                                Log.d(TAG, order.getIdOrder());
                                FirebaseAuth Auth = FirebaseAuth.getInstance();
                                DatabaseReference reffOrder = FirebaseDatabase.getInstance().getReference().child("Orders").child(Auth.getUid());
                                reffOrder.child(order.getIdOrder()).getRef().removeValue();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(order);
                }
            });
        }
    }
}

