package com.example.toko_kelontong_a11201710423_anzal_syarifur_rozaqi;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import java.text.SimpleDateFormat;
import java.util.Date;

public class admin_input_barang extends AppCompatActivity implements View.OnClickListener{
    //Global variable
    private static String TAG = "anzalTag";
    public static final int PICK_ME = 1;
    EditText editTextKodeBarang, editTextNamaBarang, editTextSatuan, editTextJumlah, editTextHarga;
    Button buttonUploadImage, buttonSaveBarang, buttonViewBarang;
    Uri selectedImage;
    String img_url;
    String fileName;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    private StorageTask uploadTask;
    ProgressBar progressBar;
    ModelBarang barang;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_input_barang);
        //pada layout input barang ini mempunyai 2 fungsi
        //untuk input barang baru
        //untuk edit barang lama
        //barang lama dididapt dari intent

        //get Intent barang yang akan diedit
        //data berupa ModelBarang
        Intent intent = getIntent();
        barang = (ModelBarang) intent.getSerializableExtra("barang");
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Barang");

        //Get id dari layout activity
        editTextNamaBarang = (EditText) findViewById(R.id.editTextNamaBarang);
        editTextSatuan = (EditText) findViewById(R.id.editTextSatuan);
        editTextJumlah = (EditText) findViewById(R.id.editTextJumlah);
        editTextHarga = (EditText) findViewById(R.id.editTextHarga);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        buttonUploadImage = (Button) findViewById(R.id.buttonImgUpload);
        buttonSaveBarang = (Button) findViewById(R.id.buttonSaveBarang);
        buttonViewBarang = (Button) findViewById(R.id.buttonViewBarang);

        //jika barang tidak null / kosong
        //data barang ditampilkan didalam form pada layout activity
        //id sesuai kode barang yang akan diedit
        if (barang != null)
        {
            id = barang.getKode_barang();
            editTextNamaBarang.setText(barang.getNama_barang());
            editTextSatuan.setText(barang.getSatuan());
            editTextJumlah.setText(Integer.valueOf(barang.getJumlah()).toString());
            editTextHarga.setText(Integer.valueOf(barang.getHarga()).toString());
            img_url = barang.getImg_url();
        }
        //jika barang bernilai null / kosong
        //id berisi kode barang baru
        else
        {
            id = databaseReference.push().getKey();
        }


        buttonUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
                img_url = null;
            }
        });

        buttonSaveBarang.setOnClickListener(this);
        buttonViewBarang.setOnClickListener(this);
    }

    //select image diginakan untuk memilih gambar
    //tampilan akan menuju local storage pada handphone atau device yang digunakan
    private void SelectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "select image"), PICK_ME);
    }

    //pada local storage
    //jika gambar sudah dipilih
    //sistem akan mencari dan mendapatkan nama filenya
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_ME && resultCode == RESULT_OK && null != data) {
            selectedImage = data.getData();
           String schema = selectedImage.getScheme();

           if (schema.equals("file")) {
               //Log.d(TAG, "filename" + selectedImage.getLastPathSegment());
               fileName = selectedImage.getLastPathSegment();
           }else if (schema.equals("content")){
               Cursor cursor = getApplicationContext().getContentResolver().query(selectedImage,null, null, null, null);
               try {
                   if (cursor != null && cursor.moveToFirst()){

                       fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                   }
               } finally {
                   cursor.close();
               }
           }
        }
    }
    //uploadImage digunakan untuk upload gambar yang terpilih
    //gambar di kirim ke firebase storage
    private void UploadImage() {
        //pathImage digunakan untuk child pada firebase storage
        final String pathImage = "Images/"+fileName;
        uploadTask = storageReference.child(pathImage).putFile(selectedImage)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            //jika upload sukses maka img_url berisi url pada gambar yang ada di storage
                            if (task.isSuccessful()) {
                                Log.d(TAG, "upload success");
                                storageReference.child(pathImage).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        img_url = uri.toString();
                                        Log.d(TAG, img_url);
                                    }
                                });
                            }
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            Log.d(TAG, "upload on progress");
                        }
                    });
    }

    //SaveBarang dijalankan ketika button barang
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void SaveBarang() {
        asnyc a = new asnyc();
        a.execute();
        Log.d(TAG, "saved");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        //v.getId() berisi id pada Layout activity
        int i = v.getId();

        //fungsi pada buttonSaveBarang
        if (i == R.id.buttonSaveBarang) {
            //jika proses upload SaveBarang tidak bisa berjalan
            if (uploadTask != null && uploadTask.isInProgress()){
                Log.d(TAG, "upload in progress");
            } else {
                SaveBarang();
            }
        }
        //button untuk melihat daftar barang
        else if (i == R.id.buttonViewBarang) {
            Intent intent = new Intent(admin_input_barang.this, admin_list_barang.class);
            startActivity(intent);
        }
    }

    private class asnyc extends AsyncTask<Void, Integer, Integer> {
        private ProgressBar dialog;
        ModelBarang barang = new ModelBarang();


        @Override
        protected void onPreExecute() {
            //inisiasi progressBar
            super.onPreExecute();
            Log.d(TAG, "onpre");
            progressBar.setMax(100);
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            //progress berjalan dari 0 hing 100
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            //jika bernilai img_url bernilai null
            //fungsi UploadImage() berjalan
            if (img_url == null)
            {
                UploadImage();
            }

            //Menambah value pada progress bar
            for (int i=0; i < 100; i++) {
                publishProgress(i);

                try {
                    Thread.sleep(100);
                }catch (InterruptedException ie){
                    ie.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            Log.d(TAG, "upload finished async");

            //Mengambil data pada form Layout activity
            String nama_barang = editTextNamaBarang.getText().toString();
            String satuan = editTextSatuan.getText().toString();
            int jumlah = Integer.valueOf(editTextJumlah.getText().toString());
            int harga = Integer.valueOf(editTextHarga.getText().toString());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH;mm;ss");
            Date date = new Date(System.currentTimeMillis());
            String tgl_beli = formatter.format(date);


            //set data pada ModelBarang
            barang.setKode_barang(id);
            barang.setNama_barang(nama_barang);
            barang.setSatuan(satuan);
            barang.setJumlah(jumlah);
            barang.setTgl_beli(tgl_beli);
            barang.setHarga(harga);
            barang.setImg_url(img_url);

            //Mengirim data pada firebase
            //jika melakukan edit barang id bernilai kode barang lama
            //jika melakukan input barang baru id bernilai kode barang baru
            databaseReference.child(id).setValue(barang);
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, admin_list_barang.class);
        startActivity(intent);
    }
}
