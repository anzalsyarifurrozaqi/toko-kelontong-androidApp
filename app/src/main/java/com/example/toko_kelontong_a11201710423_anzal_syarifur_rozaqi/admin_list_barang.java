package com.example.toko_kelontong_a11201710423_anzal_syarifur_rozaqi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class admin_list_barang extends AppCompatActivity {

    //Global Variable
    private static String TAG = "anzalTag";
    List<ModelBarang> barangList = new ArrayList<>();
    static DatabaseReference reffBarang;
    ListView listViewBarang;
    BarangAdapter barangAdapter;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_list_barang);

        //get id dari layout activity
        listViewBarang = (ListView) findViewById(R.id.listBarang);
        listViewBarang.setNestedScrollingEnabled(true);

        //set adapter pada listview
        barangAdapter = new BarangAdapter(admin_list_barang.this, barangList);
        listViewBarang.setAdapter(barangAdapter);

        //inisiasi DatabaseReference
        reffBarang = FirebaseDatabase.getInstance().getReference().child("Barang");

        reffBarang.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                //datasnapshot berupa data barang di firebase child Barang
                ModelBarang barang = dataSnapshot.getValue(ModelBarang.class);
                barangList.add(barang);
                barangAdapter.notifyDataSetChanged();

                Log.i(TAG, dataSnapshot.getKey());
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

        //set click listiner pada listview
        listViewBarang.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               Log.d(TAG, "list item");
                DialogFragment dialog = ListBarangDialog.newInstance();
                ((ListBarangDialog) dialog).setBarang(barangList.get(position));
                dialog.show(getSupportFragmentManager(), "tag");
            }
        });
    }

    //dialog saat klik row barang di listview
    public static class ListBarangDialog extends DialogFragment implements View.OnClickListener
    {
        //global variable di dialog ListBarangDialog
        private TextView tambah_barang, edit_barang, hapus_barang;
        private ModelBarang barang;

        //static ketika class object Order dialog dipanggil
        static ListBarangDialog newInstance()
        {
            return new ListBarangDialog();
        }

        //fungsi set data barang
        public void setBarang(ModelBarang barang)
        {
            this.barang = barang;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            //get id dari layout activity
            View view = inflater.inflate(R.layout.dialog_list_barang, container, false);
            tambah_barang = view.findViewById(R.id.tambah_barang);
            edit_barang = view.findViewById(R.id.edit_barang);
            hapus_barang = view.findViewById(R.id.hapus_barang);


            //set click listiner
            tambah_barang.setOnClickListener(this);
            edit_barang.setOnClickListener(this);
            hapus_barang.setOnClickListener(this);

            return view;
        }

        @Override
        public void onClick(View v) {
            //v.getId berisi id pada layout activity
            int id = v.getId();
            Intent intent;

            switch (id)
            {
                //button tambah barang
                ///menuju activity admin input barang
                case R.id.tambah_barang:
                    Log.d(TAG, "tambah barang");
                    intent = new Intent(getContext(), admin_input_barang.class);
                    startActivity(intent);
                    break;

                //button edit barang
                //menuju activity admin input barang
                //denga intent data barang yang akan diedit
                case R.id.edit_barang:
                    Log.d(TAG, "edit barang");
                    intent = new Intent(getContext(), admin_input_barang.class);
                    intent.putExtra("barang", barang);
                    startActivity(intent);
                    break;

                //button hapus barang
                //menghapus barang dengan parameter kode barang yang akan dihapus
                case  R.id.hapus_barang:
                    Log.d(TAG, "hapus barang");
                    reffBarang.child(barang.getKode_barang()).getRef().removeValue();
                    intent = new Intent(getContext(), admin_list_barang.class);
                    startActivity(intent);
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
