package com.example.doan_mobile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AdminAddNewProductCategoryActivity extends AppCompatActivity {

    EditText ProductCategoryName;
    ImageView back;
    Button add;

    DatabaseReference ProductCategoryRef;
    Integer MaHangSP = 0;
    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product_category);

        MaHangSP = getIntent().getIntExtra("MaHangSP", 0);

        matching();

        ProductCategoryRef = FirebaseDatabase.getInstance().getReference().child("HangSP");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminAddNewProductCategoryActivity.this, AdminViewProductCategoryActivity.class));
                finish();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(ProductCategoryName.getText().toString())) {
                    Toast.makeText(AdminAddNewProductCategoryActivity.this, "Vui lòng nhập tên danh mục", Toast.LENGTH_SHORT).show();
                } else {
                    addNewProductCategory();
                }
            }
        });



    }

    private void addNewProductCategory() {

        String sProductCategoryName = ProductCategoryName.getText().toString().trim();

        HashMap<String, Object> producCategorytMap = new HashMap<>();
        producCategorytMap.put("TenHangSP", sProductCategoryName);

        ProductCategoryRef.child(String.valueOf(MaHangSP + 1)).
                updateChildren(producCategorytMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(AdminAddNewProductCategoryActivity.this, AdminViewProductCategoryActivity.class));

                    loadingBar.dismiss();
                    Toast.makeText(AdminAddNewProductCategoryActivity.this, "Thêm danh mục sản phẩm thành công ^^", Toast.LENGTH_SHORT).show();
                    finish();

                } else {
                    loadingBar.dismiss();
                    Toast.makeText(AdminAddNewProductCategoryActivity.this, "Error: " + task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void matching() {
        ProductCategoryName = (EditText) findViewById(R.id.adminAddNewProductCategory_et_name);
        add = (Button) findViewById(R.id.adminAddNewProductCategory_btn_add);
        loadingBar = new ProgressDialog(this);
        back = (ImageView) findViewById(R.id.adminAddNewProductCategory_iv_back);
    }
}