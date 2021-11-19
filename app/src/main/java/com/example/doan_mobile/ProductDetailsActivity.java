package com.example.doan_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.doan_mobile.Model.SanPham;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;

public class ProductDetailsActivity extends AppCompatActivity {

    //FloatingActionButton addToCart;
    ImageView image;
    ElegantNumberButton numberButton;
    TextView price, description, name;

    String productID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productID = getIntent().getStringExtra("MaSP");

        matching();

        getProductDetails(productID);
    }

    private void getProductDetails(String productID) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("SanPham");
        ref.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    SanPham sanPham = snapshot.getValue(SanPham.class);

                    double dPrice = Double.parseDouble(sanPham.getGiaGoc());
                    NumberFormat nf = NumberFormat.getInstance(new Locale("en", "US"));
                    String sPrice = nf.format(dPrice);

                    name.setText(sanPham.getTenSP());
                    description.setText(sanPham.getThongTinChiTietSP());
                    price.setText(sPrice  + " VNĐ");
                    Picasso.get().load(sanPham.getHinhAnhSP()).into(image);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void matching() {
        //addToCart = (FloatingActionButton) findViewById(R.id.productDetails_btn_addToCart);
        image = (ImageView) findViewById(R.id.productDetails_iv_image);
        numberButton = (ElegantNumberButton) findViewById(R.id.productDetails_btn_number);
        price = (TextView) findViewById(R.id.productDetails_tv_price);
        description = (TextView) findViewById(R.id.productDetails_tv_description);
        name = (TextView) findViewById(R.id.productDetails_tv_name);
    }
}