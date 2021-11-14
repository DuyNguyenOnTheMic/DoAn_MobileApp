package com.example.doan_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class AdminCategoryActivity extends AppCompatActivity {

    ImageView lenovo, acer, asus, hp, apple, dell, msi, lg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        matching();

        lenovo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("HangSP", "Lenovo");
                startActivity(intent);
            }
        });

        acer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("HangSP", "Acer");
                startActivity(intent);
            }
        });

        asus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("HangSP", "Asus");
                startActivity(intent);
            }
        });

        hp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("HangSP", "HP");
                startActivity(intent);
            }
        });

        apple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("HangSP", "Apple");
                startActivity(intent);
            }
        });

        dell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("HangSP", "Dell");
                startActivity(intent);
            }
        });

        msi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("HangSP", "MSI");
                startActivity(intent);
            }
        });

        lg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("HangSP", "LG");
                startActivity(intent);
            }
        });

    }

    private void matching() {
        lenovo = (ImageView) findViewById(R.id.adminCategory_iv_lenovo);
        acer = (ImageView) findViewById(R.id.adminCategory_iv_acer);
        asus = (ImageView) findViewById(R.id.adminCategory_iv_asus);
        hp = (ImageView) findViewById(R.id.adminCategory_iv_hp);
        apple = (ImageView) findViewById(R.id.adminCategory_iv_apple);
        dell = (ImageView) findViewById(R.id.adminCategory_iv_dell);
        msi = (ImageView) findViewById(R.id.adminCategory_iv_msi);
        lg = (ImageView) findViewById(R.id.adminCategory_iv_lg);

    }
}