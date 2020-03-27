package com.example.toko_kelontong_a11201710423_anzal_syarifur_rozaqi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //menuju SignUp Activity
    public void SignUp(View view) {
        Intent intent = new Intent(MainActivity.this, user_signup.class);
        startActivity(intent);
    }

    //menuju SignIn Activity
    public void SignIn(View view) {
        Intent intent = new Intent(MainActivity.this, user_signin.class);
        startActivity(intent);
    }
}
