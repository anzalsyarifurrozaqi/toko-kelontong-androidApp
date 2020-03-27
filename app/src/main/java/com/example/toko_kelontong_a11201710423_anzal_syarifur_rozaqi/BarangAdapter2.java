package com.example.toko_kelontong_a11201710423_anzal_syarifur_rozaqi;


import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BarangAdapter2 extends RecyclerView.Adapter<BarangAdapter2.ViewHolder> implements Filterable {

    //interface saat klik barang
    public interface OnItemClickListener {
        void onItemClick(ModelBarang barang);
    }
    //Global variable adapter
    private static String TAG = "anzalTag";
    private List<ModelBarang> barangList;
    private List<ModelBarang> barangListFull;
    private final OnItemClickListener listener;

    //set data semua barang ke list barangListFull
    public void  setBarangListFull(List<ModelBarang> barangListFull) {
        this.barangListFull = new ArrayList<>(barangListFull);
    }

    //insiasi barangList dan listener
    public BarangAdapter2(List<ModelBarang> barangList, OnItemClickListener listener) {
        this.barangList = barangList;
        this.listener = listener;
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_barang, parent, false);
        return new ViewHolder(v);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        //binding data
        holder.bind(barangList.get(position), listener);
    }

    @Override public int getItemCount() {
        return barangList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        //global variabel viewholder
        private TextView name;
        private ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            //inissiasi dan mengabil id dari layout activity
            name = (TextView) itemView.findViewById(R.id.cardViewNamaBarang);
            image = (ImageView) itemView.findViewById(R.id.cardViewImage);
        }

        public void bind(final ModelBarang barang, final OnItemClickListener listener) {
            //set data ke recycleview yang berupa cardview
            name.setText(barang.getNama_barang());
            Picasso.get().load(barang.getImg_url()).into(image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(barang);
                }
            });
        }
    }

    @Override
    public Filter getFilter() {
        Log.d(TAG, barangList.toString());
        Log.d(TAG, barangListFull.toString());
        Log.d(TAG, "getFilter");
        return barangFilter;
    }

    private Filter barangFilter = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            Log.d(TAG, "filterresult " + constraint);
            List<ModelBarang> barangListFilter = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                Log.d(TAG, "no result");

                barangListFilter.addAll(barangListFull);
            } else {
                Log.d(TAG, "result");
                String filterPattern = constraint.toString().toLowerCase().trim();
                Log.d(TAG, filterPattern);

                for (ModelBarang barang : barangListFull) {
                    if (barang.getNama_barang().toLowerCase().contains(filterPattern)) {
                        barangListFilter.add(barang);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = barangListFilter;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            Log.d(TAG, results.values.toString());
            barangList.clear();
            barangList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}

