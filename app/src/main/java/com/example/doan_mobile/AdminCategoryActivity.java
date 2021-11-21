package com.example.doan_mobile;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import io.paperdb.Paper;

public class AdminCategoryActivity extends AppCompatActivity {

    ImageView lenovo, acer, asus, hp, apple, dell, msi, lg;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);


        matching();

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        loadFragment(new ManageFragment());


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

    private void loadFragment(androidx.fragment.app.Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_adminCategory:
                    fragment = new ManageFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_account:
                    fragment = new AccountFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_logout:
                    AlertDialog.Builder dialog = new AlertDialog.Builder(AdminCategoryActivity.this);
                    AlertDialog alert = dialog.create();

                    dialog.setTitle("Thông báo");
                    dialog.setMessage("Bạn có chắc muốn đăng xuất?");
                    dialog.setPositiveButton("Đăng xuất", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Paper.book().destroy();

                            Intent intent = new Intent(AdminCategoryActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                    });
                    dialog.setNegativeButton("Huỷ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            alert.dismiss();
                        }
                    });
                    dialog.show();
                    return true;
            }
            return false;
        }
    };

    private void matching() {
        lenovo = (ImageView) findViewById(R.id.adminCategory_iv_lenovo);
        acer = (ImageView) findViewById(R.id.adminCategory_iv_acer);
        asus = (ImageView) findViewById(R.id.adminCategory_iv_asus);
        hp = (ImageView) findViewById(R.id.adminCategory_iv_hp);
        apple = (ImageView) findViewById(R.id.adminCategory_iv_apple);
        dell = (ImageView) findViewById(R.id.adminCategory_iv_dell);
        msi = (ImageView) findViewById(R.id.adminCategory_iv_msi);
        lg = (ImageView) findViewById(R.id.adminCategory_iv_lg);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);

    }
}