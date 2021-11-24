package com.example.doan_mobile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan_mobile.Model.GioHang;
import com.example.doan_mobile.ViewHolder.AdminCartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.Locale;

public class AdminUserProductActivity extends AppCompatActivity {

    RecyclerView productList;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference cartListRef;
    ImageView back;
    TextView total;

    String userID = "";
    int overTotalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_product);

        matching();

        userID = getIntent().getStringExtra("MaKH");

        productList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        productList.setLayoutManager(layoutManager);

        cartListRef = FirebaseDatabase.getInstance().getReference()
                .child("GioHang").child("ViewQuanTri").
                        child(userID).child("SanPham");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void matching() {
        productList = findViewById(R.id.adminUserProduct_recycler_menu);
        back = (ImageView) findViewById(R.id.adminUserProduct_iv_back);
        total = (TextView) findViewById(R.id.adminUserProduct_tv_title);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<GioHang> options =
                new FirebaseRecyclerOptions.Builder<GioHang>()
                .setQuery(cartListRef, GioHang.class)
                .build();

        FirebaseRecyclerAdapter<GioHang, AdminCartViewHolder> adapter
                = new FirebaseRecyclerAdapter<GioHang, AdminCartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminCartViewHolder adminCartViewHolder, int i, @NonNull GioHang gioHang) {
                double dPrice = Double.parseDouble(gioHang.getGiaGoc());
                NumberFormat nf = NumberFormat.getInstance(new Locale("en", "US"));
                String sPrice = nf.format(dPrice);

                adminCartViewHolder.pQuantity.setText("Số lượng: " + gioHang.getSoLuongMua());
                adminCartViewHolder.pPrice.setText("Đơn giá: " + sPrice);
                adminCartViewHolder.pName.setText(gioHang.getTenSP());

                int oneTypeProductTPrice = Integer.valueOf(gioHang.getGiaGoc()) * Integer.valueOf(gioHang.getSoLuongMua());
                overTotalPrice += oneTypeProductTPrice;
                NumberFormat nf2 = NumberFormat.getInstance(new Locale("en", "US"));
                String sPrice2 = nf.format(overTotalPrice);

                total.setText("Tổng: " + sPrice2);
            }

            @NonNull
            @Override
            public AdminCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_user_product_items_layout, parent, false);
                AdminCartViewHolder holder = new AdminCartViewHolder(view);
                return holder;
            }
        };
        productList.setAdapter(adapter);
        adapter.startListening();
    }
}