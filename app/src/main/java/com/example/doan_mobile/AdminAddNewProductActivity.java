package com.example.doan_mobile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class AdminAddNewProductActivity extends AppCompatActivity {

    String categoryName;
    ImageView inputImage;
    Button addNewProduct;
    EditText pName, pDescription, pPrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);

        matching();

        categoryName = getIntent().getExtras().get("HangSP").toString();


    }

    private void matching() {
        addNewProduct = (Button) findViewById(R.id.adminAddNewProduct_btn_add);
        pName = (EditText) findViewById(R.id.adminAddNewProduct_et_name);
        pDescription = (EditText) findViewById(R.id.adminAddNewProduct_et_productDescription);
        pPrice = (EditText) findViewById(R.id.adminAddNewProduct_et_price);
    }
}