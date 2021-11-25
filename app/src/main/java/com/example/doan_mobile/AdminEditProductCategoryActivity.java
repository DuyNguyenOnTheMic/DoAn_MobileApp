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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AdminEditProductCategoryActivity extends AppCompatActivity {

    ImageView back;
    Button update;
    EditText productCategoryName;
    ProgressDialog loadingBar;

    DatabaseReference ProductCategoryRef;
    String MaHangSP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_product_category);

        MaHangSP = getIntent().getStringExtra("MaHangSP");

        matching();

        ProductCategoryRef = FirebaseDatabase.getInstance().getReference().child("HangSP");

        productCategoryInfoDisplay(productCategoryName);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(productCategoryName.getText().toString())) {
                    Toast.makeText(AdminEditProductCategoryActivity.this, "Tên danh mục không được rỗng", Toast.LENGTH_SHORT).show();
                } else {
                    updateProductCategoryInfo();
                }
            }
        });
    }

    private void updateProductCategoryInfo() {
        String sProductCategoryName = productCategoryName.getText().toString().trim();

        HashMap<String, Object> producCategorytMap = new HashMap<>();
        producCategorytMap.put("TenHangSP", sProductCategoryName);

        ProductCategoryRef.child(String.valueOf(MaHangSP)).
                updateChildren(producCategorytMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(AdminEditProductCategoryActivity.this, AdminViewProductCategoryActivity.class));

                    loadingBar.dismiss();
                    Toast.makeText(AdminEditProductCategoryActivity.this, "Sửa danh mục sản phẩm thành công ^^", Toast.LENGTH_SHORT).show();
                    finish();

                } else {
                    loadingBar.dismiss();
                    Toast.makeText(AdminEditProductCategoryActivity.this, "Error: " + task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void productCategoryInfoDisplay(EditText productCategoryName) {
        DatabaseReference PCategoryRef = FirebaseDatabase.getInstance().getReference().
                child("HangSP").child(MaHangSP);
        PCategoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String sName = snapshot.child("TenHangSP").getValue().toString();

                    productCategoryName.setText(sName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void matching() {
        back = (ImageView) findViewById(R.id.adminEditProductCategory_iv_back);
        update = (Button) findViewById(R.id.adminEditProductCategory_btn_update);
        productCategoryName = (EditText) findViewById(R.id.adminEditProductCategory_et_name);
        loadingBar = new ProgressDialog(this);
    }
}